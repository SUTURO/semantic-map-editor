package com.malte3d.suturo.commons.javafx;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.Executor;
import java.util.function.Supplier;

@RequiredArgsConstructor
public class CompletableFutureTaskBuilder<T> {

    @NonNull
    private final Executor executor;

    private String debugInfo = "";
    private String errorMessageKey;

    public CompletableFutureTaskBuilder<T> withDebugInfo(@NonNull String debugInfo) {
        this.debugInfo = debugInfo;
        return this;
    }

    public CompletableFutureTaskBuilder<T> withErrorMessageKey(@NonNull String messageKey) {
        this.errorMessageKey = messageKey;
        return this;
    }

    public CompletableFutureTask<T> withTask(@NonNull Supplier<T> task) {
        return new CompletableFutureTask<>(new UiServiceTask<>(task, debugInfo), executor);
    }
}
