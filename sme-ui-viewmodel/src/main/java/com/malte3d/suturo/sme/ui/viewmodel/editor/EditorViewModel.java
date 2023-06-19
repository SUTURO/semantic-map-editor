package com.malte3d.suturo.sme.ui.viewmodel.editor;

import com.google.common.base.Preconditions;
import com.jme3.app.state.AppState;
import com.jme3.scene.Node;
import com.malte3d.suturo.commons.ddd.event.domain.DomainEventHandler;
import com.malte3d.suturo.commons.javafx.service.GlobalExecutor;
import com.malte3d.suturo.commons.javafx.service.UiService;
import com.malte3d.suturo.sme.application.service.settings.SettingsService;
import com.malte3d.suturo.sme.domain.model.application.settings.Settings;
import com.malte3d.suturo.sme.domain.model.application.settings.advanced.DebugMode;
import com.malte3d.suturo.sme.domain.model.application.settings.advanced.DebugModeChangedEvent;
import com.malte3d.suturo.sme.domain.model.application.settings.keymap.editor.CameraBehaviour;
import com.malte3d.suturo.sme.domain.model.application.settings.keymap.editor.CameraBehaviourChangedEvent;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.SmObject;
import com.malte3d.suturo.sme.ui.viewmodel.editor.scene.camera.CameraKeymap;
import com.malte3d.suturo.sme.ui.viewmodel.editor.scene.camera.CameraKeymapBlender;
import com.malte3d.suturo.sme.ui.viewmodel.editor.scene.camera.CameraKeymapCinema4D;
import com.malte3d.suturo.sme.ui.viewmodel.editor.util.DebugAppState;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;

/**
 * View model for the 3D-Editor.
 */
@Slf4j
@Singleton
public class EditorViewModel extends UiService {

    private final DomainEventHandler domainEventHandler;

    private final SettingsService settingsService;

    private Editor editor;

    @Inject
    public EditorViewModel(
            @NonNull @GlobalExecutor Executor executor,
            @NonNull DomainEventHandler domainEventHandler,
            @NonNull SettingsService settingsService) {

        super(executor);

        this.domainEventHandler = domainEventHandler;
        this.settingsService = settingsService;

        registerEventConsumer();
    }

    private void registerEventConsumer() {
        domainEventHandler.register(DebugModeChangedEvent.class, this::onDebugModeChanged);
        domainEventHandler.register(CameraBehaviourChangedEvent.class, this::onCameraBehaviourChanged);
    }

    /**
     * Initializes the editor.
     *
     * <p>
     * Should only be called once.
     * </p>
     *
     * @return The editor instance.
     */
    public Editor initializeEditor() {

        if (editor != null)
            throw new IllegalStateException("Editor already initialized.");

        Settings settings = settingsService.get();
        DebugMode debugMode = settings.getAdvanced().getDebugMode();

        List<AppState> initialAppStates = new ArrayList<>();

        if (debugMode.isEnabled())
            initialAppStates.add(new DebugAppState(editor.getStateManager()));

        CameraBehaviour cameraBehaviour = settings.getKeymap().getCameraBehaviour();
        Class<? extends CameraKeymap> cameraKeymap = cameraBehaviour == CameraBehaviour.BLENDER ? CameraKeymapBlender.class : CameraKeymapCinema4D.class;

        this.editor = Editor.create(domainEventHandler, debugMode, cameraKeymap, initialAppStates);

        return editor;
    }

    public void addObjectToScene(@NonNull SmObject object) {
        editor.enqueue(() -> editor.addObjectToScene(object));
    }

    public Node getScenegraph() {
        return editor.getScenegraph();
    }

    public void setScenegraph(Node scenegraph) {
        editor.enqueue(() -> editor.setScenegraph(scenegraph));
    }

    private void onDebugModeChanged(DebugModeChangedEvent event) {

        Preconditions.checkNotNull(editor, "Editor must be initialized before debug mode can be changed.");

        DebugMode debugMode = event.getNewDebugMode();

        if (debugMode.isEnabled())
            editor.getStateManager().attach(new DebugAppState(editor.getStateManager()));
        else
            editor.getStateManager().detach(editor.getStateManager().getState(DebugAppState.class));
    }

    private void onCameraBehaviourChanged(CameraBehaviourChangedEvent event) {

        Preconditions.checkNotNull(editor, "Editor must be initialized before camera behaviour can be changed.");

        CameraBehaviour cameraBehaviour = event.getNewCameraBehaviour();

        if (cameraBehaviour == CameraBehaviour.BLENDER)
            editor.setCameraKeymap(CameraKeymapBlender.class);
        else
            editor.setCameraKeymap(CameraKeymapCinema4D.class);
    }

}
