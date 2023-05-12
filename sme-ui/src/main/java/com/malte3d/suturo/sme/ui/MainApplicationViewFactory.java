package com.malte3d.suturo.sme.ui;

import com.google.inject.Injector;
import com.malte3d.suturo.commons.javafx.fxml.AbstractFxmlViewFactory;
import com.malte3d.suturo.commons.messages.Messages;
import com.malte3d.suturo.sme.ui.view.MainView;
import com.malte3d.suturo.sme.ui.viewmodel.main.MainViewModel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class MainApplicationViewFactory extends AbstractFxmlViewFactory {

    private final Map<Class<?>, Object> views = new HashMap<>();

    @NonNull
    private final Injector injector;

    @Override
    public Object call(Class<?> viewClass) {

        if (viewClass.isAssignableFrom(MainView.class))
            return views.computeIfAbsent(MainView.class, mainView -> new MainView(injector.getInstance(MainViewModel.class)));

        throw new IllegalArgumentException(Messages.format("No view could be created for the unknown class {}", viewClass.getName()));
    }
}
