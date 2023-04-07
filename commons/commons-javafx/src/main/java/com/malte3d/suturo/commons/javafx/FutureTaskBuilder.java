package com.malte3d.suturo.commons.javafx;

import java.util.concurrent.Executor;
import java.util.function.Supplier;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class FutureTaskBuilder<T> {

    @NonNull
    private final Executor executor;

    private String debugInfo;
    private String errorMessageKey;

    public FutureTaskBuilder<T> withDebugInfo(@NonNull String debugInfo) {
        this.debugInfo = debugInfo;
        return this;
    }

    public FutureTaskBuilder<T> withErrorMessageKey(@NonNull String messageKey) {
        this.errorMessageKey = messageKey;
        return this;
    }

    public FutureTask<T> withTask(@NonNull Supplier<T> task) {
        return new FutureTask<>(new UiServiceTask<>(task, debugInfo), executor);
    }
}
