package com.malte3d.suturo.commons.javafx;

import javafx.scene.Node;
import javafx.util.Callback;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.concurrent.Executor;

@RequiredArgsConstructor
public abstract class AbstractFxmlViewFactory implements Callback<Class<?>, Object> {

    @NonNull
    private final Executor executor;

    public <T extends Node> CompletableFutureTask<T> loadView(Class<?> viewClass) {
        return loadView(this, viewClass);
    }

    protected <T extends Node> CompletableFutureTask<T> loadView(Callback<Class<?>, Object> viewFactory, Class<?> viewClass) {

        return new CompletableFutureTaskBuilder<T>(executor)
                .withLoggerMessageOnError("Failed to load the view for {}", viewClass.getName())
                .withErrorMessageKey("Application.View.Load.Error")
                .withTask(() -> FxmlLoaderUtil.load(viewFactory, viewClass));
    }

}
