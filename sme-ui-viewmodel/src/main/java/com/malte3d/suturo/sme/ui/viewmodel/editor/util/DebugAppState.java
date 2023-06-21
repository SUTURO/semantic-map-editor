package com.malte3d.suturo.sme.ui.viewmodel.editor.util;

import com.jme3.app.Application;
import com.jme3.app.DebugKeysAppState;
import com.jme3.app.StatsAppState;
import com.jme3.app.state.AbstractAppState;
import com.jme3.app.state.AppStateManager;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

public class DebugAppState extends AbstractAppState {

    private AppStateManager stateManager;

    @Override
    public void initialize(AppStateManager stateManager, Application app) {

        super.initialize(stateManager, app);

        this.stateManager = stateManager;

        stateManager.attach(new StatsAppState());
        stateManager.attach(new DebugKeysAppState());
    }

    @Override
    public void cleanup() {

        super.cleanup();

        if(stateManager == null)
            return;

        stateManager.detach(stateManager.getState(StatsAppState.class));
        stateManager.detach(stateManager.getState(DebugKeysAppState.class));
    }
}
