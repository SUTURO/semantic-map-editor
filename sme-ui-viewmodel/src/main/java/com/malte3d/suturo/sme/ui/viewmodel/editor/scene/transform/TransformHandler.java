package com.malte3d.suturo.sme.ui.viewmodel.editor.scene.transform;

import com.jme3.asset.AssetManager;
import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.material.Material;
import com.jme3.material.Materials;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Dome;
import com.malte3d.suturo.commons.ddd.event.domain.DomainEventHandler;
import com.malte3d.suturo.sme.ui.viewmodel.editor.event.CameraStateChangedEvent;
import com.malte3d.suturo.sme.ui.viewmodel.editor.event.ObjectSelectedEvent;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

/**
 * Handles the transformation of objects in the scene.
 */
@Slf4j
public class TransformHandler implements AnalogListener, ActionListener {

    private static final String TRANSFORM_SELECT = "TRANSFORM_SELECT";

    @NonNull
    private final DomainEventHandler domainEventHandler;

    @NonNull
    private final Camera cam;

    @NonNull
    private final InputManager inputManager;
    @NonNull
    private final AssetManager assetManager;

    @NonNull
    private final Node scenegraph;

    private boolean isBlocked = false;

    /**
     * The current transform mode.
     */
    @Getter
    @Setter
    private TransformMode transformMode = TransformMode.MOVE;

    /**
     * The currently selected object. Might be null.
     */
    @Getter
    private Optional<Spatial> selection;

    public TransformHandler(
            @NonNull DomainEventHandler domainEventHandler,
            @NonNull Camera cam,
            @NonNull InputManager inputManager,
            @NonNull AssetManager assetManager,
            @NonNull Node scenegraph) {

        this.domainEventHandler = domainEventHandler;

        this.cam = cam;
        this.inputManager = inputManager;
        this.assetManager = assetManager;
        this.scenegraph = scenegraph;

        inputManager.addMapping(TRANSFORM_SELECT, new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(this, TRANSFORM_SELECT);

        domainEventHandler.register(ObjectSelectedEvent.class, this::onObjectSelected);
        domainEventHandler.register(CameraStateChangedEvent.class, this::onCameraStateChanged);
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {

        if (!isBlocked && isPressed && TRANSFORM_SELECT.equals(name)) {

            this.selection = getSelectionTarget();
            domainEventHandler.raise(new ObjectSelectedEvent(selection, ObjectSelectedEvent.Origin.EDITOR));
        }
    }

    @Override
    public void onAnalog(String name, float value, float tpf) {

    }

    private void onCameraStateChanged(CameraStateChangedEvent event) {
        this.isBlocked = event.isActive();
    }


    private void onObjectSelected(ObjectSelectedEvent event) {

        if (ObjectSelectedEvent.Origin.SCENEGRAPH_VIEW.equals(event.getOrigin()))
            this.selection = event.getSelectedObject();
    }

    private void setSelection(Optional<Spatial> selection) {
        if (!this.selection.equals(selection))
            this.selection = selection;
    }

    /**
     * Calculates the selection target.
     *
     * <p>
     * It casts a ray from the cursor position and returns the first collision point with the objects of the
     * scenegraph.
     * </p>
     *
     * @return The selection target
     */
    private Optional<Spatial> getSelectionTarget() {

        Vector2f cursor2d = inputManager.getCursorPosition();
        Vector3f cursor3d = cam.getWorldCoordinates(new Vector2f(cursor2d.x, cursor2d.y), 0f).clone();

        Vector3f dir = cam.getWorldCoordinates(new Vector2f(cursor2d.x, cursor2d.y), 1f).subtractLocal(cursor3d);
        dir.normalizeLocal();

        Ray ray = new Ray(cursor3d, dir);
        CollisionResults results = new CollisionResults();

        scenegraph.collideWith(ray, results);
        CollisionResult closestCollision = results.getClosestCollision();

        if (closestCollision == null)
            return Optional.empty();

        return Optional.of(closestCollision.getGeometry());
    }

    private Node createTransformNode() {
        Node transformNode = new Node("TransformNode");
        Cylinder arrowTail = new Cylinder(32, 32, 0.05f, 1f, true);
        Dome arrowHead = new Dome(Vector3f.ZERO, 2, 32, 0.05f, false);


        return transformNode;
    }

    private Material createDefaultUnshadedMaterial(ColorRGBA color) {

        Material material = new Material(assetManager, Materials.UNSHADED);
        material.setColor("Color", color);

        return material;
    }
}
