package com.malte3d.suturo.sme.ui.viewmodel.main.editor.camera;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class EditorCameraAppState extends AbstractAppState {

    private Application app;
    private EditorCamera editorCamera;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {

        super.initialize(stateManager, app);

        this.app = app;

        if (app.getInputManager() != null) {

            if (editorCamera == null)
                editorCamera = new EditorCamera(app.getCamera(), app.getInputManager());
        }
    }

    @Override
    public void cleanup() {

        super.cleanup();

        if (app.getInputManager() != null)
            editorCamera.unregisterInput();
    }
}
