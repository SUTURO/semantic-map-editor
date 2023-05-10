package com.malte3d.suturo.sme.ui.viewmodel.main.editor.camera;

import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.*;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

/**
 * This class enables the user to move the camera like in Cinema 4D.
 *
 * <p>
 * Hold down ALT and move, zoom and rotate the camera with the mouse buttons.
 * </p>
 */
@Slf4j
@EqualsAndHashCode
public class Cinema4dCamera implements AnalogListener, ActionListener {

    private static final String KEY_ALT = "KEY_ALT";
    private static final String KEY_MOUSE_LEFT = "KEY_MOUSE_LEFT";
    private static final String KEY_MOUSE_MIDDLE = "KEY_MOUSE_MIDDLE";
    private static final String KEY_MOUSE_RIGHT = "KEY_MOUSE_RIGHT";

    private static final String CAM_ZOOM_IN = "CAM_ZOOM_IN";
    private static final String CAM_ZOOM_IN_SCROLL = "CAM_ZOOM_IN_SCROLL";
    private static final String CAM_ZOOM_OUT = "CAM_ZOOM_OUT";
    private static final String CAM_ZOOM_OUT_SCROLL = "CAM_ZOOM_OUT_SCROLL";
    private static final String CAM_LEFT = "CAM_MOVE_LEFT";
    private static final String CAM_RIGHT = "CAM_MOVE_RIGHT";
    private static final String CAM_UP = "CAM_MOVE_UP";
    private static final String CAM_DOWN = "CAM_MOVE_DOWN";

    private static final String[] mappings = new String[]{

            KEY_ALT,
            KEY_MOUSE_LEFT,
            KEY_MOUSE_MIDDLE,
            KEY_MOUSE_RIGHT,

            CAM_ZOOM_IN,
            CAM_ZOOM_IN_SCROLL,
            CAM_ZOOM_OUT,
            CAM_ZOOM_OUT_SCROLL,
            CAM_LEFT,
            CAM_RIGHT,
            CAM_UP,
            CAM_DOWN,
    };

    private static final float DEFAULT_ROTATION_SPEED = 5f;
    private static final float DEFAULT_MOVE_SPEED = 10f;
    private static final float DEFAULT_ZOOM_SPEED = 10f;

    /**
     * rotation-rate multiplier
     */
    @Getter
    @Setter
    private float rotationSpeed = DEFAULT_ROTATION_SPEED;

    /**
     * translation speed (in world units per second)
     */
    @Getter
    @Setter
    private float moveSpeed = DEFAULT_MOVE_SPEED;

    /**
     * zoom-rate multiplier
     */
    @Getter
    @Setter
    private float zoomSpeed = DEFAULT_ZOOM_SPEED;

    private final Camera cam;
    private final InputManager inputManager;

    private boolean altHeld = false;
    private boolean mouseLeftHeld = false;
    private boolean mouseMiddleHeld = false;
    private boolean mouseRightHeld = false;

    public Cinema4dCamera(@NonNull Camera cam, @NonNull InputManager inputManager) {

        this.cam = cam;
        this.inputManager = inputManager;

        registerInput();
    }

    /**
     * Register this controller to receive input events from the specified input manager.
     */
    private void registerInput() {


        inputManager.addMapping(KEY_ALT, new KeyTrigger(KeyInput.KEY_LMENU));
        inputManager.addMapping(KEY_MOUSE_LEFT, new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addMapping(KEY_MOUSE_MIDDLE, new MouseButtonTrigger(MouseInput.BUTTON_MIDDLE));
        inputManager.addMapping(KEY_MOUSE_RIGHT, new MouseButtonTrigger(MouseInput.BUTTON_RIGHT));

        inputManager.addMapping(CAM_ZOOM_IN, new MouseAxisTrigger(MouseInput.AXIS_X, false), new MouseAxisTrigger(MouseInput.AXIS_Y, false));
        inputManager.addMapping(CAM_ZOOM_IN_SCROLL, new MouseAxisTrigger(MouseInput.AXIS_WHEEL, false));
        inputManager.addMapping(CAM_ZOOM_OUT, new MouseAxisTrigger(MouseInput.AXIS_X, true), new MouseAxisTrigger(MouseInput.AXIS_Y, true));
        inputManager.addMapping(CAM_ZOOM_OUT_SCROLL, new MouseAxisTrigger(MouseInput.AXIS_WHEEL, true));

        inputManager.addMapping(CAM_LEFT, new MouseAxisTrigger(MouseInput.AXIS_X, true));
        inputManager.addMapping(CAM_RIGHT, new MouseAxisTrigger(MouseInput.AXIS_X, false));
        inputManager.addMapping(CAM_UP, new MouseAxisTrigger(MouseInput.AXIS_Y, false));
        inputManager.addMapping(CAM_DOWN, new MouseAxisTrigger(MouseInput.AXIS_Y, true));

        inputManager.addListener(this, mappings);
        inputManager.setCursorVisible(true);
    }

    public void unregisterInput() {

        if (inputManager == null)
            return;

        for (String s : mappings)
            if (inputManager.hasMapping(s))
                inputManager.deleteMapping(s);

        inputManager.removeListener(this);
    }


    @Override
    public void onAction(String name, boolean isPressed, float tpf) {

        if (name.equals(KEY_ALT))
            altHeld = isPressed;

        if (name.equals(KEY_MOUSE_LEFT))
            mouseLeftHeld = isPressed;
        if (name.equals(KEY_MOUSE_MIDDLE))
            mouseMiddleHeld = isPressed;
        if (name.equals(KEY_MOUSE_RIGHT))
            mouseRightHeld = isPressed;
    }

    @Override
    public void onAnalog(String name, float value, float tpf) {

        switch (name) {
            case CAM_ZOOM_IN_SCROLL -> zoomCamera(0.2f);
            case CAM_ZOOM_OUT_SCROLL -> zoomCamera(-0.2f);
        }

        if (altHeld) {

            if (mouseLeftHeld) {

                switch (name) {
                    case CAM_LEFT -> rotateCamera(value, cam.getUp());
                    case CAM_RIGHT -> rotateCamera(-value, cam.getUp());
                    case CAM_UP -> rotateCamera(-value, cam.getLeft());
                    case CAM_DOWN -> rotateCamera(value, cam.getLeft());
                }
            }

            if (mouseMiddleHeld) {

                switch (name) {
                    case CAM_LEFT -> moveCamera(-value, true);
                    case CAM_RIGHT -> moveCamera(value, true);
                    case CAM_UP -> moveCamera(-value, false);
                    case CAM_DOWN -> moveCamera(value, false);
                }
            }

            if (mouseRightHeld) {

                switch (name) {
                    case CAM_ZOOM_IN -> zoomCamera(value);
                    case CAM_ZOOM_OUT -> zoomCamera(-value);
                }
            }
        }
    }

    private void moveCamera(float value, boolean horizontal) {

        Vector3f pos = new Vector3f();

        if (horizontal)
            cam.getLeft(pos);
        else
            cam.getUp(pos);

        pos.multLocal(value * moveSpeed);

        cam.setLocation(cam.getLocation().add(pos));
    }

    private void rotateCamera(float value, Vector3f axis) {

        Vector3f cameraPos = cam.getLocation();

        float distanceToOrigin = cameraPos.distance(Vector3f.ZERO);
        Vector3f targetPos = cameraPos.add(cam.getDirection().mult(distanceToOrigin));
        float distanceToTarget = cameraPos.distance(targetPos);

        Quaternion rotation = new Quaternion().fromAngleAxis(value * rotationSpeed, axis);
        Vector3f direction = cameraPos.subtract(targetPos).normalizeLocal();
        direction = rotation.mult(direction);
        Vector3f newCameraPos = targetPos.add(direction.mult(distanceToTarget));

        cam.setLocation(newCameraPos);
        cam.lookAt(targetPos, Vector3f.UNIT_Y);
    }

    private void zoomCamera(float value) {
        float zoomAmount = value * zoomSpeed;
        cam.setLocation(cam.getLocation().add(cam.getDirection().mult(zoomAmount)));
    }

}
