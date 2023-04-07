package com.malte3d.suturo.commons.javafx;

import java.util.function.Supplier;

import javafx.concurrent.Task;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class UiServiceTask<T> extends Task<T> {

    @NonNull
    private final Supplier<T> supplier;

    @NonNull
    private final String debugInfo;

    @Override
    protected T call() {
        return supplier.get();
    }

    @Override
    protected void failed() {

        Throwable throwable = getException();

        log.error("Error in UiServiceTask \"{}\"", debugInfo, throwable);
    }
}
