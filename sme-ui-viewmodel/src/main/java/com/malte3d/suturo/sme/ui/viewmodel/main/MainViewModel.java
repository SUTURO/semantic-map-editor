package com.malte3d.suturo.sme.ui.viewmodel.main;

import com.jme3.app.FlyCamAppState;
import com.jme3.app.StatsAppState;
import com.jme3.system.AppSettings;
import com.malte3d.suturo.commons.ddd.event.domain.DomainEventPublisher;
import com.malte3d.suturo.commons.messages.Messages;
import javafx.application.HostServices;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import java.util.concurrent.atomic.AtomicReference;

@Slf4j
@Getter
@RequiredArgsConstructor(onConstructor_ = @Inject)
public class MainViewModel {

    @NonNull
    private final DomainEventPublisher domainEventPublisher;

    @NonNull
    private final HostServices hostServices;

    public void openCopyrightOwnerUrl() {
        hostServices.showDocument(Messages.getString("Application.Help.About.CopyrightOwnerUrl"));
    }

    public void exitApplication() {
        domainEventPublisher.raise(new ExitApplicationEvent());
    }

    public MainViewEditor loadMainViewEditor() {

        AtomicReference<MainViewEditor> jfxMainViewEditor = new AtomicReference<>();

        new Thread(new ThreadGroup("LWJGL"), () -> {

            var mainViewEditor = new MainViewEditor(
                    new StatsAppState(),
                    new FlyCamAppState()
            );

            AppSettings appSettings = mainViewEditor.getSettings();
            appSettings.setGammaCorrection(true);
            appSettings.setSamples(16);


            jfxMainViewEditor.set(mainViewEditor);
            jfxMainViewEditor.get().start();

        }, "LWJGL Renderer").start();

        try {

            while (jfxMainViewEditor.get() == null || !jfxMainViewEditor.get().isInitialized())
                Thread.sleep(100);

        } catch (InterruptedException e) {
            log.error("Error while waiting for MainViewEditor to be initialized", e);
        }
        
        domainEventPublisher.raise(new EditorInitializedEvent());

        return jfxMainViewEditor.get();
    }
}
