package com.malte3d.suturo.commons.javafx.service;

import javafx.concurrent.Task;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Supplier;

@Slf4j
@RequiredArgsConstructor
public class UiServiceTask<T> extends Task<T> {

    @NonNull
    private final Supplier<T> supplier;

    @NonNull
    private final String errorMessage;
    private final Object[] errorMessageArgs;

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
