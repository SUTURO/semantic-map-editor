package com.malte3d.suturo.sme.ui.viewmodel.main.editor.camera;

import com.jme3.app.Application;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import lombok.NoArgsConstructor;

@NoArgsConstructor
public class Cinema4dCameraAppState extends AbstractAppState {

    private Application app;
    private Cinema4dCamera cinema4dCamera;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {

        super.initialize(stateManager, app);

        this.app = app;

        if (app.getInputManager() != null) {

            if (cinema4dCamera == null)
                cinema4dCamera = new Cinema4dCamera(app.getCamera(), app.getInputManager());
        }
    }

    @Override
    public void cleanup() {

        super.cleanup();

        if (app.getInputManager() != null)
            cinema4dCamera.unregisterInput();
    }
}
