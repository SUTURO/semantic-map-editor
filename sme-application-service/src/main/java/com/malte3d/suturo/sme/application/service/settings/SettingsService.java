package com.malte3d.suturo.sme.application.service.settings;

import com.malte3d.suturo.commons.ddd.annotation.ApplicationService;
import com.malte3d.suturo.commons.ddd.event.domain.DomainEventHandler;
import com.malte3d.suturo.sme.domain.model.application.settings.Settings;
import com.malte3d.suturo.sme.domain.model.application.settings.SettingsChangedEvent;
import com.malte3d.suturo.sme.domain.model.application.settings.advanced.DebugMode;
import com.malte3d.suturo.sme.domain.model.application.settings.advanced.DebugModeChangedEvent;
import com.malte3d.suturo.sme.domain.model.application.settings.keymap.editor.CameraBehaviour;
import com.malte3d.suturo.sme.domain.model.application.settings.keymap.editor.CameraBehaviourChangedEvent;
import com.malte3d.suturo.sme.domain.service.application.settings.SettingsRepository;
import jakarta.inject.Inject;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Service for managing the application settings.
 */
@ApplicationService
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class SettingsService {

    @NonNull
    private final SettingsRepository settingsRepository;

    @NonNull
    private final DomainEventHandler domainEventHandler;

    /**
     * Loads the current settings.
     *
     * @return the current settings
     */
    @NonNull
    public Settings get() {
        return settingsRepository.load();
    }

    /**
     * Saves the given settings and raises a {@link SettingsChangedEvent} and possible other more specific events if
     * necessary.
     *
     * @param settings the settings to save
     */
    public void save(@NonNull Settings settings) {

        Settings oldSettings = settingsRepository.load();

        settingsRepository.save(settings);

        domainEventHandler.raise(new SettingsChangedEvent(settings));

        checkCameraBehaviourChanged(oldSettings, settings);
        checkDebugModeChanged(oldSettings, settings);
    }

    /**
     * Checks whether the camera behaviour mode has changed and raises a {@link CameraBehaviourChangedEvent} if so.
     */
    private void checkCameraBehaviourChanged(Settings oldSettings, Settings newSettings) {

        CameraBehaviour oldCameraBehaviour = oldSettings.getKeymap().getCameraBehaviour();
        CameraBehaviour newCameraBehaviour = newSettings.getKeymap().getCameraBehaviour();

        if (oldCameraBehaviour != newCameraBehaviour)
            domainEventHandler.raise(new CameraBehaviourChangedEvent(newCameraBehaviour));
    }

    /**
     * Checks whether the debug mode has changed and raises a {@link DebugModeChangedEvent} if so.
     */
    private void checkDebugModeChanged(Settings oldSettings, Settings newSettings) {

        DebugMode oldDebugMode = oldSettings.getAdvanced().getDebugMode();
        DebugMode newDebugMode = newSettings.getAdvanced().getDebugMode();

        if (oldDebugMode != newDebugMode)
            domainEventHandler.raise(new DebugModeChangedEvent(newDebugMode));
    }

}
