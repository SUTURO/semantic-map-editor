package com.malte3d.suturo.sme.ui;

import com.google.inject.Injector;
import com.malte3d.suturo.commons.javafx.AbstractFxmlViewFactory;
import com.malte3d.suturo.commons.messages.Messages;
import com.malte3d.suturo.sme.ui.view.MainView;
import com.malte3d.suturo.sme.ui.viewmodel.MainViewModel;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

public class MainApplicationViewFactory extends AbstractFxmlViewFactory {

    private final Map<Class<?>, Object> viewControllerMap = new HashMap<>();

    @NonNull
    private final Injector injector;

    public MainApplicationViewFactory(@NonNull Executor executor, @NonNull Injector injector) {
        super(executor);
        this.injector = injector;
    }

    @Override
    public Object call(Class<?> viewClass) {

        if (viewClass.isAssignableFrom(MainView.class))
            return viewControllerMap.computeIfAbsent(MainView.class, mainView -> new MainView(injector.getInstance(MainViewModel.class)));

        throw new IllegalArgumentException(Messages.format("No view could be created for the unknown class {}", viewClass.getName()));
    }
}
