package com.malte3d.suturo.sme.ui.viewmodel.main;

import com.google.common.base.Preconditions;
import com.jme3.app.DebugKeysAppState;
import com.jme3.app.StatsAppState;
import com.jme3.app.state.AppState;
import com.malte3d.suturo.commons.ddd.event.domain.DomainEventPublisher;
import com.malte3d.suturo.commons.javafx.service.CompletableFutureTask;
import com.malte3d.suturo.commons.javafx.service.GlobalExecutor;
import com.malte3d.suturo.commons.javafx.service.UiService;
import com.malte3d.suturo.commons.messages.Messages;
import com.malte3d.suturo.sme.application.service.settings.SettingsService;
import com.malte3d.suturo.sme.domain.model.application.settings.Settings;
import com.malte3d.suturo.sme.domain.model.application.settings.advanced.DebugMode;
import com.malte3d.suturo.sme.domain.model.application.settings.advanced.DebugModeChangedEvent;
import com.malte3d.suturo.sme.ui.viewmodel.main.editor.MainEditor;
import com.malte3d.suturo.sme.ui.viewmodel.main.editor.camera.Cinema4dCameraAppState;
import javafx.application.HostServices;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

@Slf4j
@Getter
public class MainViewModel extends UiService {

    private final DomainEventPublisher domainEventPublisher;

    private final HostServices hostServices;
    private final SettingsService settingsService;

    private MainEditor mainEditor;

    @Inject
    public MainViewModel(
            @NonNull @GlobalExecutor Executor executor,
            @NonNull DomainEventPublisher domainEventPublisher,
            @NonNull HostServices hostServices,
            @NonNull SettingsService settingsService) {

        super(executor);

        this.domainEventPublisher = domainEventPublisher;
        this.hostServices = hostServices;
        this.settingsService = settingsService;

        registerEventConsumer();
    }

    private void registerEventConsumer() {
        domainEventPublisher.register(DebugModeChangedEvent.class, this::onDebugModeChanged);
    }

    private void onDebugModeChanged(DebugModeChangedEvent event) {

        Preconditions.checkNotNull(mainEditor, "Main editor must be loaded before debug mode can be changed.");

        DebugMode debugMode = event.getNewDebugMode();

        if (debugMode.isEnabled()) {

            mainEditor.getStateManager().attach(new StatsAppState());
            mainEditor.getStateManager().attach(new DebugKeysAppState());

        } else {

            mainEditor.getStateManager().detach(mainEditor.getStateManager().getState(StatsAppState.class));
            mainEditor.getStateManager().detach(mainEditor.getStateManager().getState(DebugKeysAppState.class));
        }
    }

    /**
     * Opens the URL of the copyright owner in the default browser.
     */
    public void openCopyrightOwnerUrl() {
        hostServices.showDocument(Messages.getString("Application.Help.About.CopyrightOwnerUrl"));
    }

    /**
     * Raises an {@link ExitApplicationEvent} to exit the application.
     */
    public void exitApplication() {
        domainEventPublisher.raise(new ExitApplicationEvent());
    }

    /**
     * @return a {@link CompletableFutureTask} that loads the main editor and returns it.
     */
    public CompletableFutureTask<MainEditor> loadMainEditor() {

        return this.<MainEditor>createFutureTask()
                .withErrorMessageKey("Application.Main.Editor.Initialization.Error")
                .withLoggerMessageOnError("Error while initializing MainEditor")
                .withTask(this::initializeMainEditor);
    }

    private MainEditor initializeMainEditor() {

        Settings settings = settingsService.get();

        List<AppState> initialAppStates = new ArrayList<>();
        initialAppStates.add(new Cinema4dCameraAppState());

        if (settings.getAdvanced().getDebugMode().isEnabled()) {

            initialAppStates.add(new StatsAppState());
            initialAppStates.add(new DebugKeysAppState());
        }

        this.mainEditor = MainEditor.create(initialAppStates);

        return mainEditor;
    }

    /**
     * Toggles the debug mode and saves the new mode to the application settings.
     */
    public void toggleDebugMode() {

        this.<DebugMode>createFutureTask()
                .withLoggerMessageOnError("Error while saving debug mode to settings")
                .withTask(() -> {

                    final Settings currentSettings = settingsService.get();
                    DebugMode newDebugMode = DebugMode.of(!currentSettings.getAdvanced().getDebugMode().isEnabled());

                    Settings newSettings = currentSettings.withAdvanced(currentSettings.getAdvanced().withDebugMode(newDebugMode));

                    settingsService.save(newSettings);

                    return newDebugMode;
                });
    }
}
