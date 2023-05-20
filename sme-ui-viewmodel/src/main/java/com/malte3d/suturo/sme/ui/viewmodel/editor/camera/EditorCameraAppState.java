package com.malte3d.suturo.sme.ui.viewmodel.editor.camera;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import com.jme3.input.InputManager;
import com.jme3.scene.Node;
import lombok.Getter;
import lombok.NonNull;

public class EditorCameraAppState extends AbstractAppState {

    @NonNull
    private final Node rootNode;
    @NonNull
    private final Node guiNode;

    @Getter
    private InputManager inputManager;
    @NonNull
    private Class<? extends CameraKeymap> keymap;

    @Getter
    private EditorCamera camera;

    public EditorCameraAppState(@NonNull Class<? extends CameraKeymap> keymap, @NonNull Node rootNode, @NonNull Node guiNode) {
        this.keymap = keymap;
        this.rootNode = rootNode;
        this.guiNode = guiNode;
    }

    /**
     * Updates the keymap and re-registers the new inputs for the camera.
     *
     * @param keymap the keymap to set
     */
    public void setKeymap(@NonNull Class<? extends CameraKeymap> keymap) {
        this.keymap = keymap;
        this.camera.setKeymap(keymap);
    }

    @Override
    public void initialize(AppStateManager stateManager, Application app) {

        super.initialize(stateManager, app);

        this.inputManager = app.getInputManager();
        this.camera = new EditorCamera(app.getCamera(), app.getAssetManager(), inputManager, keymap, rootNode, guiNode);
    }

    @Override
    public void cleanup() {

        super.cleanup();

        camera.unregisterInput();
    }
}
