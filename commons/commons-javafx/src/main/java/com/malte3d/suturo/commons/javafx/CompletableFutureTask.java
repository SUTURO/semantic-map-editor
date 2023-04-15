package com.malte3d.suturo.commons.javafx;

import com.google.common.base.Preconditions;
import com.malte3d.suturo.commons.exception.UnsupportedEnumException;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.concurrent.WorkerStateEvent;
import lombok.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.function.Consumer;

public class CompletableFutureTask<T> {

    private final Task<T> task;

    private final List<Runnable> succeededHandlers = new ArrayList<>();
    private final List<Runnable> failedHandlers = new ArrayList<>();
    private final List<Runnable> canceledHandlers = new ArrayList<>();

    private T value;
    private Throwable exception;

    public CompletableFutureTask(@NonNull Task<T> task, @NonNull Executor executor) {

        this.task = task;

        runOnUiThread(() -> {
            task.addEventHandler(WorkerStateEvent.WORKER_STATE_SUCCEEDED, event -> this.complete());
            task.addEventHandler(WorkerStateEvent.WORKER_STATE_FAILED, event -> this.complete());
            task.addEventHandler(WorkerStateEvent.WORKER_STATE_CANCELLED, event -> this.complete());
        });

        executor.execute(task);
    }

    public T get() {

        Preconditions.checkState(task.getState() == Worker.State.SUCCEEDED, "Task has to be SUCCEEDED!");

        return value;
    }

    public void cancel() {
        cancel(true);
    }

    public void cancel(boolean mayInterruptIfRunning) {
        task.cancel(mayInterruptIfRunning);
    }

    public CompletableFutureTask<T> thenConsume(@NonNull Consumer<T> succeeded) {

        onSucceed(() -> succeeded.accept(value));

        return this;
    }

    public CompletableFutureTask<T> thenConsume(@NonNull Consumer<T> succeeded, @NonNull Consumer<Throwable> failed) {

        onSucceed(() -> succeeded.accept(value));
        onFail(() -> failed.accept(exception));

        return this;
    }

    public CompletableFutureTask<T> thenRun(@NonNull Runnable succeeded) {

        onSucceed(succeeded);

        return this;
    }

    public CompletableFutureTask<T> thenRun(@NonNull Runnable succeeded, @NonNull Runnable failed) {

        onSucceed(succeeded);
        onFail(failed);

        return this;
    }

    private synchronized void complete() {

        final boolean taskIsCompleted = task.getState() == Worker.State.SUCCEEDED || task.getState() == Worker.State.FAILED || task.getState() == Worker.State.CANCELLED;
        Preconditions.checkState(taskIsCompleted, "Task should be SUCCEEDED, FAILED or CANCELLED when this Method is invoked!");

        this.value = task.getValue();
        this.exception = task.getException();

        List<Runnable> eventHandlers;

        switch (task.getState()) {

            case SUCCEEDED -> eventHandlers = succeededHandlers;
            case FAILED -> eventHandlers = failedHandlers;
            case CANCELLED -> eventHandlers = canceledHandlers;
            default -> throw new UnsupportedEnumException(task.getState());
        }

        runEventHandler(eventHandlers);
    }

    private synchronized void onSucceed(Runnable runnable) {

        if (task.getState() == Worker.State.SUCCEEDED)
            runOnUiThread(runnable);
        else
            succeededHandlers.add(runnable);
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
