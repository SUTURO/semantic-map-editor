package com.malte3d.suturo.sme.ui.viewmodel.editor.camera;

import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.InputListener;
import com.jme3.input.controls.MouseAxisTrigger;
import com.malte3d.suturo.sme.ui.viewmodel.editor.keymap.ActionTrigger;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

/**
 * Base class for camera keymaps.
 */
@RequiredArgsConstructor
public abstract class CameraKeymap {

    public static final String CAM_ZOOM_IN = "CAM_ZOOM_IN";
    public static final String CAM_ZOOM_IN_SCROLL = "CAM_ZOOM_IN_SCROLL";
    public static final String CAM_ZOOM_OUT = "CAM_ZOOM_OUT";
    public static final String CAM_ZOOM_OUT_SCROLL = "CAM_ZOOM_OUT_SCROLL";
    public static final String CAM_LEFT = "CAM_LEFT";
    public static final String CAM_RIGHT = "CAM_RIGHT";
    public static final String CAM_UP = "CAM_UP";
    public static final String CAM_DOWN = "CAM_DOWN";

    private static final String[] CAM_CONTROLS = new String[]{
            CAM_ZOOM_IN,
            CAM_ZOOM_IN_SCROLL,
            CAM_ZOOM_OUT,
            CAM_ZOOM_OUT_SCROLL,
            CAM_LEFT,
            CAM_RIGHT,
            CAM_UP,
            CAM_DOWN,
    };

    @NonNull
    private final InputManager inputManager;

    @NonNull
    private final InputListener inputListener;

    @Getter
    @NonNull
    private final ActionTrigger move;

    @Getter
    @NonNull
    private final ActionTrigger rotate;

    @Getter
    @NonNull
    private final ActionTrigger zoom;

    /**
     * Register this controller to receive input events from the specified input manager.
     */
    public void registerInput() {

        move.register(inputManager);
        rotate.register(inputManager);
        zoom.register(inputManager);

        inputManager.addMapping(CAM_ZOOM_IN, new MouseAxisTrigger(MouseInput.AXIS_X, false), new MouseAxisTrigger(MouseInput.AXIS_Y, false));
        inputManager.addMapping(CAM_ZOOM_IN_SCROLL, new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
        inputManager.addMapping(CAM_ZOOM_OUT, new MouseAxisTrigger(MouseInput.AXIS_X, true), new MouseAxisTrigger(MouseInput.AXIS_Y, true));
        inputManager.addMapping(CAM_ZOOM_OUT_SCROLL, new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));

        inputManager.addMapping(CAM_LEFT, new MouseAxisTrigger(MouseInput.AXIS_X, true));
        inputManager.addMapping(CAM_RIGHT, new MouseAxisTrigger(MouseInput.AXIS_X, false));
        inputManager.addMapping(CAM_UP, new MouseAxisTrigger(MouseInput.AXIS_Y, false));
        inputManager.addMapping(CAM_DOWN, new MouseAxisTrigger(MouseInput.AXIS_Y, true));

        inputManager.addListener(inputListener, CAM_CONTROLS);
        inputManager.addListener(inputListener, move.getTriggers().toArray(new String[0]));
        inputManager.addListener(inputListener, rotate.getTriggers().toArray(new String[0]));
        inputManager.addListener(inputListener, zoom.getTriggers().toArray(new String[0]));

        inputManager.setCursorVisible(true);
    }

    public void unregisterInput() {

        move.unregister(inputManager);
        rotate.unregister(inputManager);
        zoom.unregister(inputManager);

        for (String mapping : CAM_CONTROLS)
            if (inputManager.hasMapping(mapping))
                inputManager.deleteMapping(mapping);

        inputManager.removeListener(inputListener);
    }

}
