package com.malte3d.suturo.commons.javafx.service;

import javafx.concurrent.Task;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.function.Supplier;

@Slf4j
public class UiServiceTask<T> extends Task<T> {

    @NonNull
    private final Supplier<T> supplier;

    private final String errorMessage;
    private final Object[] errorMessageArgs;

    private final String errorMessageKey;

    public UiServiceTask(
            @NonNull Supplier<T> supplier,
            @NonNull Optional<String> errorMessage,
            @NonNull Object[] errorMessageArgs,
            @NonNull Optional<String> errorMessageKey) {

        this.supplier = supplier;
        this.errorMessage = errorMessage.orElse("Unexpected Error");
        this.errorMessageArgs = errorMessageArgs;
        this.errorMessageKey = errorMessageKey.orElse("Application.Error.Unknown");
    }

    @Override
    protected T call() {
        return supplier.get();
    }

    @Override
    protected void failed() {

        Throwable throwable = getException();

        log.error(errorMessage, errorMessageArgs, throwable);
    }
}
