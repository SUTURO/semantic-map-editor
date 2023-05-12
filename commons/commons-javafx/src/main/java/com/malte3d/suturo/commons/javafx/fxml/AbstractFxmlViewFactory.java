package com.malte3d.suturo.commons.javafx.fxml;

import javafx.scene.Node;
import javafx.util.Callback;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public abstract class AbstractFxmlViewFactory implements Callback<Class<?>, Object> {

    public <T extends Node> T loadView(Class<?> viewClass) {
        return loadView(this, viewClass);
    }

    protected <T extends Node> T loadView(Callback<Class<?>, Object> viewFactory, Class<?> viewClass) {
        return FxmlLoaderUtil.load(viewFactory, viewClass);
    }

}
