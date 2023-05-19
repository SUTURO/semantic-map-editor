package com.malte3d.suturo.sme.ui.viewmodel.editor.camera;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.InputListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.malte3d.suturo.sme.ui.viewmodel.editor.keymap.ActionTrigger;
import lombok.NonNull;

/**
 * Keymapping for Cinema4D-like camera controls.
 *
 * <p>
 * Hold down ALT and move, zoom and rotate the camera with the mouse buttons.
 * </p>
 */
public class CameraKeymapCinema4D extends CameraKeymap {

    private static final ActionTrigger MOVE = new ActionTrigger("CAM_CINEMA4D_MOVE", new KeyTrigger(KeyInput.KEY_LMENU), new MouseButtonTrigger(MouseInput.BUTTON_MIDDLE));
    private static final ActionTrigger ROTATE = new ActionTrigger("CAM_CINEMA4D_ROTATE", new KeyTrigger(KeyInput.KEY_LMENU), new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
    private static final ActionTrigger ZOOM = new ActionTrigger("CAM_CINEMA4D_ZOOM", new KeyTrigger(KeyInput.KEY_LMENU), new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));

    public CameraKeymapCinema4D(@NonNull InputManager inputManager, @NonNull InputListener inputListener) {
        super(inputManager, inputListener, MOVE, ROTATE, ZOOM);
    }
}
