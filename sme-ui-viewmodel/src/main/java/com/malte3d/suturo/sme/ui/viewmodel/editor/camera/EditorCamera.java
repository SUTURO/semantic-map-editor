package com.malte3d.suturo.sme.ui.viewmodel.editor.camera;

import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.*;
import com.jme3.math.*;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
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
public class EditorCamera implements AnalogListener, ActionListener {

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
    private static final float DEFAULT_ZOOM_SCROLL = 0.02f;

    /**
     * The rotation-rate multiplier
     */
    @Getter
    @Setter
    private float rotationSpeed = DEFAULT_ROTATION_SPEED;

    /**
     * The translation speed (in world units per second)
     */
    @Getter
    @Setter
    private float moveSpeed = DEFAULT_MOVE_SPEED;

    /**
     * The zoom-rate multiplier
     */
    @Getter
    @Setter
    private float zoomSpeed = DEFAULT_ZOOM_SPEED;

    private final Camera cam;
    private final InputManager inputManager;

    private final Node rootNode;

    private float yaw;
    private float pitch;
    private Vector3f rotationTarget = Vector3f.ZERO;

    private boolean altPressed = false;
    private boolean mouseLeftPressed = false;
    private boolean mouseMiddlePressed = false;
    private boolean mouseRightPressed = false;

    public EditorCamera(@NonNull Camera cam, @NonNull InputManager inputManager, Node rootNode) {

        this.cam = cam;
        this.inputManager = inputManager;
        this.rootNode = rootNode;

        /* Set initial pitch and yaw */
        Quaternion camRotation = cam.getRotation();
        float[] angles = new float[3];
        camRotation.toAngles(angles);
        this.pitch = angles[0];
        this.yaw = angles[1];

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

        for (String mapping : mappings)
            if (inputManager.hasMapping(mapping))
                inputManager.deleteMapping(mapping);

        inputManager.removeListener(this);
    }


    @Override
    public void onAction(String name, boolean isPressed, float tpf) {

        if (name.equals(KEY_ALT))
            altPressed = isPressed;

        if (name.equals(KEY_MOUSE_LEFT))
            mouseLeftPressed = isPressed;
        if (name.equals(KEY_MOUSE_MIDDLE))
            mouseMiddlePressed = isPressed;
        if (name.equals(KEY_MOUSE_RIGHT))
            mouseRightPressed = isPressed;

        /* Update rotation target if rotation is started */
        if (altPressed && mouseLeftPressed)
            rotationTarget = getRotationTarget();
    }

    @Override
    public void onAnalog(String name, float value, float tpf) {

        switch (name) {
            case CAM_ZOOM_IN_SCROLL -> zoomCamera(DEFAULT_ZOOM_SCROLL);
            case CAM_ZOOM_OUT_SCROLL -> zoomCamera(-DEFAULT_ZOOM_SCROLL);
        }

        if (altPressed) {

            if (mouseLeftPressed) {

                switch (name) {
                    case CAM_LEFT -> rotateCamera(value, cam.getUp());
                    case CAM_RIGHT -> rotateCamera(-value, cam.getUp());
                    case CAM_UP -> rotateCamera(-value, cam.getLeft());
                    case CAM_DOWN -> rotateCamera(value, cam.getLeft());
                }
            }

            if (mouseMiddlePressed) {

                switch (name) {
                    case CAM_LEFT -> moveCamera(-value, cam.getLeft());
                    case CAM_RIGHT -> moveCamera(value, cam.getLeft());
                    case CAM_UP -> moveCamera(-value, cam.getUp());
                    case CAM_DOWN -> moveCamera(value, cam.getUp());
                }
            }

            if (mouseRightPressed) {

                switch (name) {
                    case CAM_ZOOM_IN -> zoomCamera(value);
                    case CAM_ZOOM_OUT -> zoomCamera(-value);
                }
            }
        }
    }

    private void moveCamera(float value, Vector3f axis) {

        Vector3f delta = axis.clone();
        delta.multLocal(value * moveSpeed);

        cam.setLocation(cam.getLocation().add(delta));
    }

    private void rotateCamera(float value, Vector3f axis) {

        float delta = value * rotationSpeed;

        if (axis.equals(cam.getUp()))
            yaw = (yaw + delta) % FastMath.TWO_PI;
        else if (axis.equals(cam.getLeft()))
            pitch = (pitch + delta) % FastMath.TWO_PI;

        Quaternion rotation = new Quaternion();
        rotation.fromAngles(pitch, yaw, 0);
        cam.setRotation(rotation);

        float distanceToTarget = cam.getLocation().distance(rotationTarget);
        Vector3f position = rotationTarget.add(cam.getDirection().mult(distanceToTarget).negate());
        cam.setLocation(position);
    }

    private void zoomCamera(float value) {
        float delta = value * zoomSpeed;
        cam.setLocation(cam.getLocation().add(cam.getDirection().mult(delta)));
    }

    /**
     * Gets first collision point with an object from the scene graph at the current cursor position (if present).
     * Else returns the default target.
     *
     * @return The rotation target of the camera.
     */
    private Vector3f getRotationTarget() {

        Vector2f cursor2d = inputManager.getCursorPosition();
        Vector3f cursor3d = cam.getWorldCoordinates(new Vector2f(cursor2d.x, cursor2d.y), 0f).clone();

        Vector3f dir = cam.getWorldCoordinates(new Vector2f(cursor2d.x, cursor2d.y), 1f).subtractLocal(cursor3d);
        dir.normalizeLocal();

        Ray ray = new Ray(cursor3d, dir);
        CollisionResults results = new CollisionResults();

        rootNode.collideWith(ray, results);
        CollisionResult closestCollision = results.getClosestCollision();

        if (closestCollision == null)
            return getDefaultRotationTarget();
        else
            return closestCollision.getContactPoint();
    }

    /**
     * The default rotation target is simply 20 units in front of the camera.
     *
     * @return The default rotation target of the camera.
     */
    private Vector3f getDefaultRotationTarget() {
        return cam.getLocation().add(cam.getDirection().mult(20));
    }

}
