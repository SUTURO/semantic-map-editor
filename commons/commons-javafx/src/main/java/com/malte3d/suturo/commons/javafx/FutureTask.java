package com.malte3d.suturo.commons.javafx;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

import com.malte3d.suturo.commons.exception.UnsupportedEnumException;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.concurrent.WorkerStateEvent;
import lombok.NonNull;

public class FutureTask<T> {

    private final Task<T>  task;
    private final Executor executor;

    private final List<Runnable> suceededHandlers = new ArrayList<>();
    private final List<Runnable> failedHandlers   = new ArrayList<>();
    private final List<Runnable> canceledHandlers = new ArrayList<>();

    private T         value;
    private Throwable exception;

    public FutureTask(Task<T> task, Executor executor) {

        this.task = task;
        this.executor = executor;

        runOnUiThread(() -> {
            task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, event -> this.complete());
            task.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED, event -> this.complete());
            task.addEventHandler(WorkerStateEvent.WORKER_STATE_CANCELLED, event -> this.complete());
        });

        executor.execute(task);
    }

    public T get() {

        if (this.task.getState() != Worker.State.SUCCEEDED)
            throw new IllegalStateException("Taks has to be SUCEEDED!");

        return value;
    }

    public void cancel() {
        cancel(true);
    }

    public void cancel(boolean mayInterruptIfRunning) {
        task.cancel(mayInterruptIfRunning);
    }

    public FutureTask<T> thenConsume(@NonNull Consumer<T> suceeded) {

        onSuceed(() -> suceeded.accept(value));

        return this;
    }

    public FutureTask<T> thenConsume(@NonNull Consumer<T> suceeded, @NonNull Consumer<Throwable> failed) {

        onSuceed(() -> suceeded.accept(value));
        onFail(() -> failed.accept(exception));

        return this;
    }

    public FutureTask<T> thenRun(@NonNull Runnable suceeded) {

        onSuceed(suceeded);

        return this;
    }

    public FutureTask<T> thenRun(@NonNull Runnable suceeded, @NonNull Runnable failed) {

        onSuceed(suceeded);
        onFail(failed);

        return this;
    }

    private synchronized void complete() {

        if (task.getState() != Worker.State.SUCCEEDED && task.getState() != Worker.State.FAILED && task.getState() != Worker.State.CANCELLED)
            throw new IllegalStateException("Task should be SUCCEEDED, FAILED or CANCELLED when this Method is invoked!");

        this.value = task.getValue();
        this.exception = task.getException();

        List<Runnable> eventHandlers;

        switch (task.getState()) {

            case SUCCEEDED -> eventHandlers = suceededHandlers;
            case FAILED -> eventHandlers = failedHandlers;
            case CANCELLED -> eventHandlers = canceledHandlers;
            default -> throw new UnsupportedEnumException(task.getState());
        }

        runEventHandler(eventHandlers);
    }

    private synchronized void onSuceed(Runnable runnable) {

        if (task.getState() == Worker.State.SUCCEEDED)
            runOnUiThread(runnable);
        else
            suceededHandlers.add(runnable);
    }

    private synchronized void onFail(Runnable runnable) {

        if (task.getState() == Worker.State.FAILED)
            runOnUiThread(runnable);
        else
            failedHandlers.add(runnable);
    }

    private static void runOnUiThread(Runnable runnable) {

        if (Platform.isFxApplicationThread())
            runnable.run();
        else
            Platform.runLater(runnable);
    }

    private static void runEventHandler(List<Runnable> eventHandlers) {
        runEventHandler(eventHandlers, Collections.emptyList());
    }

    private static void runEventHandler(List<Runnable> eventHandlers, List<Runnable> completed) {

        int size = eventHandlers.size();
        List<Runnable> currentRecursion = new ArrayList<>(eventHandlers);
        currentRecursion.removeAll(completed);

        for (Runnable eventHandler : eventHandlers)
            if (eventHandler != null)
                eventHandler.run();

        if (size != eventHandlers.size())
            runEventHandler(eventHandlers, currentRecursion);
    }
}
