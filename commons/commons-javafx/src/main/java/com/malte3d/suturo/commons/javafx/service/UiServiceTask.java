package com.malte3d.suturo.commons.javafx.service;

import com.malte3d.suturo.commons.javafx.notification.NotificationHandler;
import com.malte3d.suturo.commons.messages.Messages;
import javafx.concurrent.Task;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.function.Supplier;

@Slf4j
public class UiServiceTask<T> extends Task<T> {

    @NonNull
    private final Supplier<T> supplier;

    private final String logMessage;
    private final Object[] logMessageArgs;

    private final String notificationMessageKey;
    private final Object[] notificationMessageArgs;

    public UiServiceTask(
            @NonNull Supplier<T> supplier,
            @NonNull Optional<String> logMessage,
            Object[] logMessageArgs,
            @NonNull Optional<String> notificationMessageKey,
            Object[] notificationMessageArgs) {

        this.supplier = supplier;
        this.logMessage = logMessage.orElse("Unexpected Error");
        this.logMessageArgs = logMessageArgs;
        this.notificationMessageKey = notificationMessageKey.orElse("Application.Error.Unknown");
        this.notificationMessageArgs = notificationMessageArgs;
    }

    @Override
    protected T call() {
        return supplier.get();
    }

    @Override
    protected void failed() {

        Throwable throwable = getException();

        log.error(logMessage, logMessageArgs, throwable);

        NotificationHandler.create()
                .title(Messages.getString("Application.Notification.Title.Error"))
                .text(Messages.getString(notificationMessageKey, notificationMessageArgs))
                .showError();
    }
}
