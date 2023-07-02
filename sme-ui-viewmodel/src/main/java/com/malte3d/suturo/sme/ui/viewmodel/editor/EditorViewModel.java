package com.malte3d.suturo.sme.ui.viewmodel.editor;

import com.google.common.base.Preconditions;
import com.google.common.io.Files;
import com.jme3.scene.Node;
import com.malte3d.suturo.commons.ddd.event.domain.DomainEventHandler;
import com.malte3d.suturo.commons.javafx.service.CompletableFutureTask;
import com.malte3d.suturo.commons.javafx.service.GlobalExecutor;
import com.malte3d.suturo.commons.javafx.service.UiService;
import com.malte3d.suturo.sme.domain.model.application.settings.advanced.DebugMode;
import com.malte3d.suturo.sme.domain.model.application.settings.advanced.DebugModeChangedEvent;
import com.malte3d.suturo.sme.domain.model.application.settings.keymap.editor.CameraBehaviour;
import com.malte3d.suturo.sme.domain.model.application.settings.keymap.editor.CameraBehaviourChangedEvent;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.SmObject;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.SmObjectName;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.imported.ImportObject;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.imported.ImportObjectPath;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.imported.ImportObjectType;
import com.malte3d.suturo.sme.ui.viewmodel.editor.event.TransformModeChangedEvent;
import com.malte3d.suturo.sme.ui.viewmodel.editor.scene.camera.CameraKeymapBlender;
import com.malte3d.suturo.sme.ui.viewmodel.editor.scene.camera.CameraKeymapCinema4D;
import com.malte3d.suturo.sme.ui.viewmodel.editor.scene.transform.TransformMode;
import com.malte3d.suturo.sme.ui.viewmodel.editor.util.DebugAppState;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

import javax.inject.Inject;
import javax.inject.Provider;
import java.io.File;
import java.util.concurrent.Executor;

/**
 * View model for the 3D-Editor.
 *
 * <p>
 * Handles everything related to the 3D-Editor.
 * </p>
 */
@Slf4j
public class EditorViewModel extends UiService {

    private final DomainEventHandler domainEventHandler;

    @NonNull
    private final Provider<Editor> editorProvider;

    @Inject
    public EditorViewModel(
            @NonNull @GlobalExecutor Executor executor,
            @NonNull DomainEventHandler domainEventHandler,
            @NonNull Provider<Editor> editorProvider) {

        super(executor);

        this.domainEventHandler = domainEventHandler;
        this.editorProvider = editorProvider;

        domainEventHandler.register(DebugModeChangedEvent.class, this::onDebugModeChanged);
        domainEventHandler.register(CameraBehaviourChangedEvent.class, this::onCameraBehaviourChanged);
    }

    /**
     * @return a {@link CompletableFutureTask} that loads the 3D-Editor and returns it.
     */
    public CompletableFutureTask<Editor> loadEditor() {

        return this.<Editor>createFutureTask()
                .withNotificationMessageOnError("Application.Main.Editor.Initialization.Error")
                .withLoggerMessageOnError("Error while initializing the 3D-Editor")
                .withTask(this::getEditor);
    }

    protected Editor getEditor() {
        return editorProvider.get();
    }

    public Node getScenegraph() {
        return getEditor().getScenegraph();
    }

    public void setScenegraph(@NonNull Node scenegraph) {
        runInJme3Thread(() -> getEditor().setScenegraph(scenegraph));
    }

    public void setTransformMode(@NonNull TransformMode transformMode) {
        runInJme3Thread(() -> getEditor().getTransformHandler().setTransformMode(transformMode));
        domainEventHandler.raise(new TransformModeChangedEvent(transformMode));
    }

    public void addObjectToScene(@NonNull SmObject object) {
        runInJme3Thread(() -> getEditor().addObjectToScene(object));
    }

    public void importFile(@NonNull File selectedFile) {

        SmObjectName objectName = SmObjectName.of(selectedFile.getName());
        ImportObjectType objectType = ImportObjectType.of(Files.getFileExtension(selectedFile.getName()));
        ImportObjectPath objectPath = ImportObjectPath.of(selectedFile.getAbsolutePath());

        ImportObject importObject = new ImportObject(objectName, objectType, objectPath);

        addObjectToScene(importObject);
    }

    private void onDebugModeChanged(DebugModeChangedEvent event) {

        Preconditions.checkNotNull(getEditor(), "Editor must be initialized before debug mode can be changed.");

        DebugMode debugMode = event.getNewDebugMode();

        if (debugMode.isEnabled())
            getEditor().getStateManager().attach(new DebugAppState());
        else
            getEditor().getStateManager().detach(getEditor().getStateManager().getState(DebugAppState.class));
    }

    private void onCameraBehaviourChanged(CameraBehaviourChangedEvent event) {

        Preconditions.checkNotNull(getEditor(), "Editor must be initialized before camera behaviour can be changed.");

        CameraBehaviour cameraBehaviour = event.getNewCameraBehaviour();

        if (cameraBehaviour == CameraBehaviour.BLENDER)
            getEditor().setCameraKeymap(CameraKeymapBlender.class);
        else
            getEditor().setCameraKeymap(CameraKeymapCinema4D.class);
    }

    /**
     * Runs the given runnable in the JME3 thread.
     *
     * @param runnable The runnable to be executed in the JME3 thread.
     */
    private void runInJme3Thread(Runnable runnable) {
        getEditor().enqueue(runnable);
    }

}
