package com.malte3d.suturo.sme.ui.viewmodel.main;

import com.jme3.app.DebugKeysAppState;
import com.jme3.app.StatsAppState;
import com.jme3.app.state.AppState;
import com.malte3d.suturo.commons.ddd.event.domain.DomainEventPublisher;
import com.malte3d.suturo.commons.javafx.CompletableFutureTask;
import com.malte3d.suturo.commons.javafx.GlobalExecutor;
import com.malte3d.suturo.commons.javafx.UiService;
import com.malte3d.suturo.commons.messages.Messages;
import com.malte3d.suturo.sme.domain.model.application.settings.Settings;
import com.malte3d.suturo.sme.domain.model.application.settings.advanced.DebugMode;
import com.malte3d.suturo.sme.domain.service.application.settings.SettingsRepository;
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

    @NonNull
    private final DomainEventPublisher domainEventPublisher;

    @NonNull
    private final HostServices hostServices;

    private final SettingsRepository settingsRepository;

    @Inject
    public MainViewModel(
            @NonNull @GlobalExecutor Executor executor,
            @NonNull DomainEventPublisher domainEventPublisher,
            @NonNull HostServices hostServices,
            @NonNull SettingsRepository settingsRepository) {

        super(executor);

        this.domainEventPublisher = domainEventPublisher;
        this.hostServices = hostServices;
        this.settingsRepository = settingsRepository;
    }


    public void openCopyrightOwnerUrl() {
        hostServices.showDocument(Messages.getString("Application.Help.About.CopyrightOwnerUrl"));
    }

    public void exitApplication() {
        domainEventPublisher.raise(new ExitApplicationEvent());
    }

    public CompletableFutureTask<MainEditor> loadMainEditor() {

        return this.<MainEditor>createFutureTask()
                .withErrorMessageKey("Application.Main.Editor.Initialization.Error")
                .withLoggerMessageOnError("Error while initializing MainEditor")
                .withTask(this::initializeMainEditor);
    }

    private MainEditor initializeMainEditor() {

        Settings settings = settingsRepository.load();

        List<AppState> appStates = new ArrayList<>();
        appStates.add(new Cinema4dCameraAppState());

        if (settings.advancedSettings().debugMode().isEnabled()) {

            appStates.add(new StatsAppState());
            appStates.add(new DebugKeysAppState());
        }

        return MainEditor.create(appStates);
    }

    public void toggleDebugMode() {

        final Settings currentSettings = settingsRepository.load();
        DebugMode debugMode = DebugMode.of(!currentSettings.advancedSettings().debugMode().isEnabled());

        Settings newSettings = currentSettings.withAdvancedSettings(currentSettings.advancedSettings().withDebugMode(debugMode));

        settingsRepository.save(newSettings);
    }
}
