package com.malte3d.suturo.sme.ui.viewmodel.settings;

import com.malte3d.suturo.commons.ddd.event.domain.DomainEventHandler;
import com.malte3d.suturo.commons.javafx.service.GlobalExecutor;
import com.malte3d.suturo.commons.javafx.service.UiService;
import com.malte3d.suturo.sme.application.service.settings.SettingsService;
import com.malte3d.suturo.sme.domain.model.application.settings.Settings;
import com.malte3d.suturo.sme.domain.model.application.settings.SettingsChangedEvent;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import lombok.Getter;
import lombok.NonNull;

import javax.inject.Inject;
import java.util.concurrent.Executor;

public class SettingsViewModel extends UiService {

    private final DomainEventHandler domainEventHandler;

    private final SettingsService settingsService;

    private final ObjectProperty<Settings> settingsPropertyInternal = new SimpleObjectProperty<>();
    @Getter
    private final ReadOnlyObjectProperty<Settings> settingsProperty = settingsPropertyInternal;

    @Inject
    public SettingsViewModel(
            @NonNull @GlobalExecutor Executor executor,
            @NonNull DomainEventHandler domainEventHandler,
            @NonNull SettingsService settingsService) {

        super(executor);

        this.domainEventHandler = domainEventHandler;
        this.settingsService = settingsService;

        this.settingsPropertyInternal.setValue(loadSettings());

        registerEventConsumer();
    }

    private void registerEventConsumer() {
        domainEventHandler.register(SettingsChangedEvent.class, this::onSettingsChanged);
    }

    public Settings loadSettings() {
        return settingsService.get();
    }

    public void saveSettings(@NonNull Settings settings) {
        settingsService.save(settings);
    }

    private void onSettingsChanged(SettingsChangedEvent event) {
        settingsPropertyInternal.setValue(event.getNewSettings());
    }
}
