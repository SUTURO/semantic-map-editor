package com.malte3d.suturo.sme.ui;

import com.malte3d.suturo.commons.javafx.fxml.FxmlViewFactory;
import com.malte3d.suturo.commons.messages.Messages;
import com.malte3d.suturo.sme.ui.view.MainView;
import com.malte3d.suturo.sme.ui.view.settings.SettingsView;
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

        if (viewClass.isAssignableFrom(MainView.class))
            return views.computeIfAbsent(MainView.class, view -> createViewInstance(MainView.class));
        else if (viewClass.isAssignableFrom(SettingsView.class))
            return views.computeIfAbsent(SettingsView.class, view -> createViewInstance(SettingsView.class));

        throw new IllegalArgumentException(Messages.format("No view could be created for the unknown class {}", viewClass.getName()));
    }

    private static Object createViewInstance(Class<?> viewClass) {
        return InjectorProvider.get().getInstance(viewClass);
    }
}
