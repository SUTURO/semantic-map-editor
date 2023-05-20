package com.malte3d.suturo.commons.javafx.fxml;

import javafx.scene.Node;
import javafx.util.Callback;
import lombok.RequiredArgsConstructor;

/**
 * Base class for view factories.
 */
@RequiredArgsConstructor
public abstract class FxmlViewFactory implements Callback<Class<?>, Object> {

    public <T extends Node> T loadView(Class<?> viewClass) {
        return loadView(this, viewClass);
    }

    protected <T extends Node> T loadView(Callback<Class<?>, Object> viewFactory, Class<?> viewClass) {
        return FxmlLoaderUtil.load(viewFactory, viewClass);
    }

}
