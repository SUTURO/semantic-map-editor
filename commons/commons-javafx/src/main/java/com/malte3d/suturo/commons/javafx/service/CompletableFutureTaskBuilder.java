package com.malte3d.suturo.commons.javafx.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

/**
 * Builder for {@link CompletableFutureTask}.
 *
 * @param <T> The type of the {@link CompletableFutureTask}
 */
@RequiredArgsConstructor
public class CompletableFutureTaskBuilder<T> {

    @NonNull
    private final Executor executor;

    private Optional<String> loggerErrorMessage = Optional.empty();
    private Object[] loggerErrorMessageArgs;

    private Optional<String> notificationErrorMessageKey = Optional.empty();
    private Object[] notificationErrorMessageArgs;

    /**
     * Sets the message to log in case of an error.
     *
     * @param errorMessage The message to log in case of an error
     * @param args         Potential {@code arguments} of a formatted string
     * @return The {@link CompletableFutureTaskBuilder} instance for chaining
     */
    public CompletableFutureTaskBuilder<T> withLoggerMessageOnError(@NonNull String errorMessage, Object... args) {
        this.loggerErrorMessage = Optional.of(errorMessage);
        this.loggerErrorMessageArgs = args;
        return this;
    }

    /**
     * Sets the message to show in a notification in case of an error.
     *
     * @param messageKey The {@code message.properties} key
     * @param args       Potential {@code arguments} of a formatted string
     * @return The {@link CompletableFutureTaskBuilder} instance for chaining
     */
    public CompletableFutureTaskBuilder<T> withNotificationMessageOnError(@NonNull String messageKey, Object... args) {
        this.notificationErrorMessageKey = Optional.of(messageKey);
        this.notificationErrorMessageArgs = args;
        return this;
    }

    /**
     * Sets the task to execute.
     *
     * @param task The task to execute
     * @return The created {@link CompletableFutureTask} instance
     */
    public CompletableFutureTask<T> withTask(@NonNull Supplier<T> task) {
        return new CompletableFutureTask<>(new UiServiceTask<>(task, loggerErrorMessage, loggerErrorMessageArgs, notificationErrorMessageKey, notificationErrorMessageArgs), executor);
    }
}
