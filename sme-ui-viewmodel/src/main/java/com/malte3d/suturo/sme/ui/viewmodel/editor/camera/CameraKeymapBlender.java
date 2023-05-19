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
 * Keymapping for Blender-like camera controls.
 *
 * <p>
 * Press the middle mouse button to rotate the camera. Hold down SHIFT and press the middle mouse button to move the camera. Hold down CTRL and press the middle mouse button to zoom the camera.
 * </p>
 */
public class CameraKeymapBlender extends CameraKeymap {

    private static final ActionTrigger MOVE = new ActionTrigger("CAM_BLENDER_MOVE", new KeyTrigger(KeyInput.KEY_LSHIFT), new MouseButtonTrigger(MouseInput.BUTTON_MIDDLE));
    private static final ActionTrigger ROTATE = new ActionTrigger("CAM_BLENDER_ROTATE", new MouseButtonTrigger(MouseInput.BUTTON_MIDDLE));
    private static final ActionTrigger ZOOM = new ActionTrigger("CAM_BLENDER_ZOOM", new KeyTrigger(KeyInput.KEY_LCONTROL), new MouseButtonTrigger(MouseInput.BUTTON_MIDDLE));

    public CameraKeymapBlender(@NonNull InputManager inputManager, @NonNull InputListener inputListener) {
        super(inputManager, inputListener, MOVE, ROTATE, ZOOM);
    }
}
