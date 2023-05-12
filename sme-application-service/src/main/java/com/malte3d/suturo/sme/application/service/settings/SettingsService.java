package com.malte3d.suturo.sme.application.service.settings;

import com.malte3d.suturo.commons.ddd.annotation.ApplicationService;
import com.malte3d.suturo.commons.ddd.event.domain.DomainEventPublisher;
import com.malte3d.suturo.sme.domain.model.application.settings.Settings;
import com.malte3d.suturo.sme.domain.model.application.settings.SettingsChangedEvent;
import com.malte3d.suturo.sme.domain.model.application.settings.advanced.DebugMode;
import com.malte3d.suturo.sme.domain.model.application.settings.advanced.DebugModeChangedEvent;
import com.malte3d.suturo.sme.domain.service.application.settings.SettingsRepository;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import javax.inject.Inject;

/**
 * Service for managing the application settings.
 */
@ApplicationService
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class SettingsService {

    @NonNull
    private final SettingsRepository settingsRepository;

    @NonNull
    private final DomainEventPublisher domainEventPublisher;

    /**
     * Loads the current settings.
     *
     * @return the current settings
     */
    public Settings get() {
        return settingsRepository.load();
    }

    /**
     * Saves the given settings and raises a {@link SettingsChangedEvent}.
     *
     * @param settings the settings to save
     */
    public void save(@NonNull Settings settings) {

        Settings oldSettings = settingsRepository.load();

        settingsRepository.save(settings);

        domainEventPublisher.raise(new SettingsChangedEvent(settings));
        checkDebugModeChanged(oldSettings, settings);
    }

    /**
     * Checks weather the debug mode has changed and raises a {@link DebugModeChangedEvent} if so.
     *
     * @param newSettings the new settings
     */
    private void checkDebugModeChanged(Settings oldSettings, Settings newSettings) {

        DebugMode oldDebugMode = oldSettings.getAdvanced().getDebugMode();
        DebugMode newDebugMode = newSettings.getAdvanced().getDebugMode();

        if (oldDebugMode != newDebugMode)
            domainEventPublisher.raise(new DebugModeChangedEvent(newDebugMode));
    }

}
