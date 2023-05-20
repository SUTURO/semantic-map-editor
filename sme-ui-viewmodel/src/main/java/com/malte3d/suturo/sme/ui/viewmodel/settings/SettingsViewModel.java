package com.malte3d.suturo.sme.ui.viewmodel.settings;

import com.malte3d.suturo.commons.ddd.event.domain.DomainEventPublisher;
import com.malte3d.suturo.commons.javafx.service.GlobalExecutor;
import com.malte3d.suturo.commons.javafx.service.UiService;
import com.malte3d.suturo.sme.application.service.settings.SettingsService;
import com.malte3d.suturo.sme.domain.model.application.settings.SettingsChangedEvent;
import lombok.NonNull;

import javax.inject.Inject;
import java.util.concurrent.Executor;

public class SettingsViewModel extends UiService {

    private final DomainEventPublisher domainEventPublisher;

    private final SettingsService settingsService;

    @Inject
    public SettingsViewModel(
            @NonNull @GlobalExecutor Executor executor,
            @NonNull DomainEventPublisher domainEventPublisher,
            @NonNull SettingsService settingsService) {

        super(executor);

        this.domainEventPublisher = domainEventPublisher;
        this.settingsService = settingsService;

        registerEventConsumer();
    }

    private void registerEventConsumer() {
        domainEventPublisher.register(SettingsChangedEvent.class, this::onSettingsChanged);
    }

    private void onSettingsChanged(SettingsChangedEvent event) {
        /* TODO: Impl */
    }
}
