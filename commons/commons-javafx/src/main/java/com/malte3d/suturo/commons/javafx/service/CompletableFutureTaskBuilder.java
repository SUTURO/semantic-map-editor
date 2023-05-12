package com.malte3d.suturo.commons.javafx.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class CompletableFutureTaskBuilder<T> {

    @NonNull
    private final Executor executor;

    private Optional<String> loggerErrorMessage = Optional.empty();
    private Object[] loggerErrorMessageArgs;

    private Optional<String> errorMessageKey = Optional.empty();

    public CompletableFutureTaskBuilder<T> withLoggerMessageOnError(@NonNull String errorMessage, Object... args) {
        this.loggerErrorMessage = Optional.of(errorMessage);
        this.loggerErrorMessageArgs = args;
        return this;
    }

    public CompletableFutureTaskBuilder<T> withErrorMessageKey(@NonNull String messageKey) {
        this.errorMessageKey = Optional.of(messageKey);
        return this;
    }

    public CompletableFutureTask<T> withTask(@NonNull Supplier<T> task) {
        return new CompletableFutureTask<>(new UiServiceTask<>(task, loggerErrorMessage, loggerErrorMessageArgs, errorMessageKey), executor);
    }
}
