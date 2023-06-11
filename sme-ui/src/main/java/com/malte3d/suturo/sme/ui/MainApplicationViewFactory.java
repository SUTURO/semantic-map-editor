package com.malte3d.suturo.sme.ui;

import com.malte3d.suturo.commons.javafx.fxml.FxmlViewFactory;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * MainFactory for views.
 *
 * <p>Creates and holds views for the Semantic Map Editor application.</p>
 */
@RequiredArgsConstructor
public class MainApplicationViewFactory extends FxmlViewFactory {

    /**
     * Cache for views.
     *
     * <p>Need to keep a reference to the views, otherwise they will be garbage collected.</p>
     */
    private final Map<Class<?>, Object> views = new HashMap<>();

    @Override
    public Object call(Class<?> viewClass) {
        return views.computeIfAbsent(viewClass, this::createViewInstance);
    }

    private Object createViewInstance(Class<?> viewClass) {
        return InjectorProvider.get().getInstance(viewClass);
    }
}
