package com.malte3d.suturo.sme.ui.viewmodel.main;

import com.jme3.app.StatsAppState;
import com.malte3d.suturo.commons.ddd.event.domain.DomainEventPublisher;
import com.malte3d.suturo.commons.javafx.CompletableFutureTask;
import com.malte3d.suturo.commons.javafx.GlobalExecutor;
import com.malte3d.suturo.commons.javafx.UiService;
import com.malte3d.suturo.commons.messages.Messages;
import com.malte3d.suturo.sme.ui.viewmodel.main.editor.MainEditor;
import javafx.application.HostServices;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.util.concurrent.Executor;

@Slf4j
@Getter
public class MainViewModel extends UiService {

    @NonNull
    private final DomainEventPublisher domainEventPublisher;

    @NonNull
    private final HostServices hostServices;

    @Inject
    public MainViewModel(
            @NonNull @GlobalExecutor Executor executor,
            @NonNull DomainEventPublisher domainEventPublisher,
            @NonNull HostServices hostServices) {

        super(executor);

        this.domainEventPublisher = domainEventPublisher;
        this.hostServices = hostServices;
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

        MainEditor mainEditor = new MainEditor(
                new StatsAppState()
        );

        mainEditor.start();

        try {

            mainEditor.getInitializedLatch().await();

        } catch (InterruptedException e) {
            log.error("Error while waiting for MainEditor to be initialized", e);
        }

        return mainEditor;
    }
}
