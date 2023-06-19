package com.malte3d.suturo.sme.ui.viewmodel.editor.scene.camera;


import com.jme3.asset.AssetManager;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.math.*;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.texture.Texture2D;
import com.jme3.ui.Picture;
import com.malte3d.suturo.commons.ddd.event.domain.DomainEventHandler;
import com.malte3d.suturo.sme.ui.viewmodel.editor.event.CameraStateChangedEvent;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@EqualsAndHashCode
public class EditorCamera implements AnalogListener, ActionListener {

    private static final float DEFAULT_ROTATION_SPEED = 5f;
    private static final float DEFAULT_MOVE_SPEED = 12f;
    private static final float DEFAULT_ZOOM_SPEED = 15f;
    private static final float DEFAULT_ZOOM_SCROLL = 0.04f;

    private static final float DEFAULT_TARGET_DISTANCE = 20.0f;

    @NonNull
    private final DomainEventHandler domainEventHandler;

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

    private final Picture crosshair;

    @NonNull
    private final Node scenegraph;
    @NonNull
    private final Node guiNode;

    private final Plane floor = new Plane(Vector3f.UNIT_Y, 0);

    /**
     * The target of the camera
     */
    private Vector3f target = Vector3f.ZERO.clone();

    @Getter
    private CameraKeymap keymap;

    public EditorCamera(
            @NonNull DomainEventHandler domainEventHandler,
            @NonNull Camera cam,
            @NonNull AssetManager assetManager,
            @NonNull InputManager inputManager,
            @NonNull Class<? extends CameraKeymap> keymap,
            @NonNull Node scenegraph,
            @NonNull Node guiNode) {

        this.domainEventHandler = domainEventHandler;

        this.cam = cam;
        this.inputManager = inputManager;
        this.scenegraph = scenegraph;
        this.guiNode = guiNode;

        setKeymap(keymap);

        /* Set initial camera position */
        cam.setLocation(new Vector3f(5, 4, 5));
        cam.lookAt(Vector3f.ZERO, Vector3f.UNIT_Y);

        /* Create crosshair */
        Texture2D texture = (Texture2D) assetManager.loadTexture("camera/crosshair.png");
        this.crosshair = new Picture("EditorCamera Crosshair");
        this.crosshair.setTexture(assetManager, texture, true);
        this.crosshair.setWidth(texture.getImage().getWidth());
        this.crosshair.setHeight(texture.getImage().getHeight());
    }

    /**
     * Register this camera to receive input events from the specified input manager.
     */
    private void registerInput() {
        keymap.registerInput();
    }

    /**
     * Unregister this camera's keymaps from the specified input manager.
     */
    public void unregisterInput() {

        if (keymap != null)
            keymap.unregisterInput();
    }

    /**
     * Updates the camera's keymap.
     *
     * @param keymap the keymap to set
     */
    public void setKeymap(@NonNull Class<? extends CameraKeymap> keymap) {

        unregisterInput();

        if (keymap.isAssignableFrom(CameraKeymapBlender.class))
            this.keymap = new CameraKeymapBlender(inputManager, this);
        else
            this.keymap = new CameraKeymapCinema4D(inputManager, this);

        registerInput();
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {

        if (keymap.getMove().getTriggers().contains(name))
            keymap.getMove().update(name, isPressed);

        if (keymap.getRotate().getTriggers().contains(name))
            keymap.getRotate().update(name, isPressed);

        if (keymap.getZoom().getTriggers().contains(name))
            keymap.getZoom().update(name, isPressed);

        /* Update camera target */
        if (keymap.getMove().isActive())
            target = getMoveTarget();
        else if (keymap.getZoom().isActive())
            target = getZoomTarget();
        else if (keymap.getRotate().isActive())
            target = getRotationTarget();

        if (keymap.isActive())
            showCrosshair();
        else
            hideCrosshair();
    }

    @Override
    public void onAnalog(String name, float value, float tpf) {

        switch (name) {
            case CameraKeymap.CAM_ZOOM_IN_SCROLL -> zoomCamera(DEFAULT_ZOOM_SCROLL);
            case CameraKeymap.CAM_ZOOM_OUT_SCROLL -> zoomCamera(-DEFAULT_ZOOM_SCROLL);
        }

        if (keymap.getMove().isActive()) {

            switch (name) {
                case CameraKeymap.CAM_LEFT -> moveCamera(-value, cam.getLeft());
                case CameraKeymap.CAM_RIGHT -> moveCamera(value, cam.getLeft());
                case CameraKeymap.CAM_UP -> moveCamera(-value, cam.getUp());
                case CameraKeymap.CAM_DOWN -> moveCamera(value, cam.getUp());
            }

        } else if (keymap.getZoom().isActive()) {

            switch (name) {
                case CameraKeymap.CAM_ZOOM_IN -> zoomCamera(value);
                case CameraKeymap.CAM_ZOOM_OUT -> zoomCamera(-value);
            }

        } else if (keymap.getRotate().isActive()) {

            switch (name) {
                case CameraKeymap.CAM_LEFT -> rotateCamera(value, true);
                case CameraKeymap.CAM_RIGHT -> rotateCamera(-value, true);
                case CameraKeymap.CAM_UP -> rotateCamera(value, false);
                case CameraKeymap.CAM_DOWN -> rotateCamera(-value, false);
            }
        }

        cam.update();
    }

    /**
     * Moves the camera along the given axis.
     *
     * @param value The amount to move the camera
     * @param axis  The axis to move the camera along
     */
    private void moveCamera(float value, Vector3f axis) {

        Vector3f delta = axis.mult(value * moveSpeed);

        cam.setLocation(cam.getLocation().add(delta));
        updateSpeed();
    }

    /**
     * Rotates the camera around the rotation target.
     *
     * @param value      The amount to rotate the camera
     * @param horizontal Whether to rotate the camera horizontally (yaw) or vertically (pitch)
     */
    private void rotateCamera(float value, boolean horizontal) {

        float delta = value * rotationSpeed;

        Vector3f axis = horizontal ? Vector3f.UNIT_Y : cam.getDirection().cross(cam.getUp()).normalizeLocal();
        Quaternion rotation = new Quaternion();
        rotation.fromAngleNormalAxis(delta, axis);

        Vector3f newDirection = rotation.mult(cam.getDirection());
        Vector3f newUp = rotation.mult(cam.getUp());

        cam.lookAtDirection(newDirection, newUp);

        Vector3f targetToCam = cam.getLocation().subtract(target);
        targetToCam = rotation.mult(targetToCam);
        Vector3f newCamPosition = target.add(targetToCam);

        cam.setLocation(newCamPosition);
        updateSpeed();
    }

    /**
     * Zooms/Moves the camera to the {@link #target}.
     *
     * @param value The amount to zoom the camera
     */
    private void zoomCamera(float value) {

        float delta = value * zoomSpeed;

        Vector3f cursor2d = cam.getScreenCoordinates(target);
        Vector3f dir = cam.getWorldCoordinates(new Vector2f(cursor2d.x, cursor2d.y), 1f).subtract(target);
        dir.normalizeLocal();

        cam.setLocation(cam.getLocation().add(dir.mult(delta)));
        updateSpeed();
    }


    /**
     * Adjusts the camera's speed based on the camera's height.
     */
    private void updateSpeed() {

        if (FastMath.abs(cam.getLocation().getY()) < 3) {

            moveSpeed = DEFAULT_MOVE_SPEED * 0.5f;
            rotationSpeed = DEFAULT_ROTATION_SPEED * 0.75f;
            zoomSpeed = DEFAULT_ZOOM_SPEED * 0.5f;

        } else {

            moveSpeed = DEFAULT_MOVE_SPEED;
            rotationSpeed = DEFAULT_ROTATION_SPEED;
            zoomSpeed = DEFAULT_ZOOM_SPEED;
        }
    }

    /**
     * Calculates the move target based on the camera's direction.
     *
     * @return The move target
     */
    private Vector3f getMoveTarget() {
        return cam.getDirection().mult(DEFAULT_TARGET_DISTANCE);
    }

    /**
     * Calculates the zoom target based on the cursor position.
     *
     * @return THe zoom target
     */
    private Vector3f getZoomTarget() {
        Vector2f cursor2d = inputManager.getCursorPosition();
        return cam.getWorldCoordinates(new Vector2f(cursor2d.x, cursor2d.y), 0f).clone();
    }

    /**
     * Calculates the rotation target based on the cursor position.
     *
     * <p>
     * It casts a ray from the cursor position and returns the first collision point with the objects of the scene
     * graph. If no collision is found, it returns the {@link #getDefaultRotationTarget()}.
     * </p>
     *
     * @return The rotation target
     */
    private Vector3f getRotationTarget() {

        Vector2f cursor2d = inputManager.getCursorPosition();
        Vector3f cursor3d = cam.getWorldCoordinates(new Vector2f(cursor2d.x, cursor2d.y), 0f).clone();

        Vector3f dir = cam.getWorldCoordinates(new Vector2f(cursor2d.x, cursor2d.y), 1f).subtractLocal(cursor3d);
        dir.normalizeLocal();

        Ray ray = new Ray(cursor3d, dir);
        CollisionResults results = new CollisionResults();

        scenegraph.collideWith(ray, results);
        CollisionResult closestCollision = results.getClosestCollision();

        if (closestCollision == null)
            return getDefaultRotationTarget();

        return closestCollision.getContactPoint();
    }

    /**
     * The default rotation target is maximum {@link #DEFAULT_TARGET_DISTANCE}  units away from the camera, but
     * preferably on the floor.
     *
     * @return The default rotation target of the camera.
     */
    private Vector3f getDefaultRotationTarget() {

        Vector3f origin = cam.getLocation().clone();
        Vector3f dir = cam.getDirection().clone();

        Ray ray = new Ray(origin, dir);

        Vector3f intersectionPoint = Vector3f.ZERO.clone();
        ray.intersectsWherePlane(floor, intersectionPoint);

        if (intersectionPoint.distance(origin) > DEFAULT_TARGET_DISTANCE)
            intersectionPoint = origin.add(dir.mult(DEFAULT_TARGET_DISTANCE));

        return intersectionPoint;
    }

    private void showCrosshair() {

        float widthOffset = crosshair.getLocalScale().getX() / 2;
        float heightOffset = crosshair.getLocalScale().getY() / 2;
        Vector3f position = cam.getScreenCoordinates(target).subtract(widthOffset, heightOffset, 0);

        crosshair.setLocalTranslation(position);
        guiNode.attachChild(crosshair);

        domainEventHandler.raise(new CameraStateChangedEvent(true));
    }

    private void hideCrosshair() {
        guiNode.detachChild(crosshair);
        domainEventHandler.raise(new CameraStateChangedEvent(false));
    }
}
