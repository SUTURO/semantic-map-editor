package com.malte3d.suturo.sme.ui.viewmodel.editor.scene.transform;

import com.jme3.collision.CollisionResult;
import com.jme3.collision.CollisionResults;
import com.jme3.input.InputManager;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.AnalogListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.math.Ray;
import com.jme3.math.Vector2f;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.malte3d.suturo.commons.ddd.event.domain.DomainEventHandler;
import com.malte3d.suturo.sme.ui.viewmodel.editor.event.CameraStateChangedEvent;
import com.malte3d.suturo.sme.ui.viewmodel.editor.event.ObjectSelectedEvent;
import lombok.Getter;
import lombok.NonNull;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

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
    private Spatial selection;

    public TransformHandler(
            @NonNull DomainEventHandler domainEventHandler,
            @NonNull Camera cam,
            @NonNull InputManager inputManager,
            @NonNull Node scenegraph) {

        this.domainEventHandler = domainEventHandler;

        domainEventHandler.register(ObjectSelectedEvent.class, this::onObjectSelected);
        domainEventHandler.register(CameraStateChangedEvent.class, this::onCameraStateChanged);

        this.cam = cam;
        this.inputManager = inputManager;
        this.scenegraph = scenegraph;

        inputManager.addMapping(TRANSFORM_SELECT, new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
        inputManager.addListener(this, TRANSFORM_SELECT);
    }

    @Override
    public void onAction(String name, boolean isPressed, float tpf) {

        if (!isBlocked && isPressed && TRANSFORM_SELECT.equals(name)) {

            Optional<Spatial> selectionTarget = getSelectionTarget();
            domainEventHandler.raise(new ObjectSelectedEvent(selectionTarget, ObjectSelectedEvent.Origin.EDITOR));
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
            this.selection = event.getSelectedObject().orElse(null);
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
}
