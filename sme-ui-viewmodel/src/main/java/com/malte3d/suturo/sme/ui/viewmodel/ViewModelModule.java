package com.malte3d.suturo.sme.ui.viewmodel;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.jme3.app.state.AppState;
import com.malte3d.suturo.commons.ddd.event.domain.DomainEventHandler;
import com.malte3d.suturo.sme.application.service.settings.SettingsService;
import com.malte3d.suturo.sme.domain.model.application.settings.Settings;
import com.malte3d.suturo.sme.domain.model.application.settings.advanced.DebugMode;
import com.malte3d.suturo.sme.domain.model.application.settings.keymap.editor.CameraBehaviour;
import com.malte3d.suturo.sme.ui.viewmodel.editor.Editor;
import com.malte3d.suturo.sme.ui.viewmodel.editor.EditorViewModel;
import com.malte3d.suturo.sme.ui.viewmodel.editor.scene.camera.CameraKeymap;
import com.malte3d.suturo.sme.ui.viewmodel.editor.scene.camera.CameraKeymapBlender;
import com.malte3d.suturo.sme.ui.viewmodel.editor.scene.camera.CameraKeymapCinema4D;
import com.malte3d.suturo.sme.ui.viewmodel.editor.util.DebugAppState;
import lombok.NonNull;

import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;

public class ViewModelModule extends AbstractModule {

    @Override
    protected void configure() {

        bind(MainViewModel.class).in(Singleton.class);
        bind(EditorViewModel.class).in(Singleton.class);
    }

    @Provides
    @Singleton
    public Editor editor(
            @NonNull DomainEventHandler domainEventHandler,
            @NonNull SettingsService settingsService
            ) {

        Settings settings = settingsService.get();
        DebugMode debugMode = settings.getAdvanced().getDebugMode();

        List<AppState> initialAppStates = new ArrayList<>();

        if (debugMode.isEnabled())
            initialAppStates.add(new DebugAppState());

        CameraBehaviour cameraBehaviour = settings.getKeymap().getCameraBehaviour();
        Class<? extends CameraKeymap> cameraKeymap = cameraBehaviour == CameraBehaviour.BLENDER ? CameraKeymapBlender.class : CameraKeymapCinema4D.class;

        return Editor.create(domainEventHandler, debugMode, cameraKeymap, initialAppStates);
    }


}
