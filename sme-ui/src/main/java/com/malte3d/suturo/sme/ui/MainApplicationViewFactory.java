package com.malte3d.suturo.sme.ui;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executor;

import com.malte3d.suturo.commons.ddd.event.domain.DomainEventPublisher;
import com.malte3d.suturo.commons.javafx.AbstractFxmlViewFactory;
import com.malte3d.suturo.commons.messages.Messages;
import com.malte3d.suturo.sme.ui.view.MainView;
import com.malte3d.suturo.sme.ui.viewmodel.MainViewModel;
import javafx.application.HostServices;
import lombok.NonNull;

public class MainApplicationViewFactory extends AbstractFxmlViewFactory {

    private final Map<Class<?>, Object> viewControllerMap = new HashMap<>();

    @NonNull
    private final DomainEventPublisher domainEventPublisher;

    @NonNull
    private final HostServices hostServices;

    public MainApplicationViewFactory(
            @NonNull Executor executor,
            @NonNull DomainEventPublisher domainEventPublisher,
            @NonNull HostServices hostServices) {

        super(executor);

        this.domainEventPublisher = domainEventPublisher;
        this.hostServices = hostServices;
    }

    @Override
    public Object call(Class<?> viewClass) {

        if (viewClass.isAssignableFrom(MainView.class))
            return viewControllerMap.computeIfAbsent(MainView.class, mainView -> new MainView(new MainViewModel(domainEventPublisher, hostServices)));

        throw new IllegalArgumentException(Messages.format("No view could be created for the unknown class {}", viewClass.getName()));
    }
}
