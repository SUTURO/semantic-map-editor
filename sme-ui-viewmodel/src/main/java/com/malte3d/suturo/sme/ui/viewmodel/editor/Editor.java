package com.malte3d.suturo.sme.ui.viewmodel.editor;


import java.io.File;
import java.io.IOException;
import java.util.Collection;

import com.jayfella.jfx.embedded.AbstractJmeApplication;
import com.jme3.app.state.AppState;
import com.jme3.asset.ModelKey;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.export.binary.BinaryExporter;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.LightProbe;
import com.jme3.material.Material;
import com.jme3.material.Materials;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.debug.Arrow;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Quad;
import com.jme3.scene.shape.Sphere;
import com.malte3d.suturo.commons.ddd.event.domain.DomainEventHandler;
import com.malte3d.suturo.sme.domain.model.application.settings.advanced.DebugMode;
import com.malte3d.suturo.sme.domain.model.application.settings.advanced.DebugModeChangedEvent;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.Position;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.SmObject;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.SmObjectType;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.imported.ImportObject;
import com.malte3d.suturo.sme.ui.viewmodel.editor.event.ObjectAttachedEvent;
import com.malte3d.suturo.sme.ui.viewmodel.editor.event.SceneChangedEvent;
import com.malte3d.suturo.sme.ui.viewmodel.editor.scene.camera.CameraKeymap;
import com.malte3d.suturo.sme.ui.viewmodel.editor.scene.camera.EditorCameraAppState;
import com.malte3d.suturo.sme.ui.viewmodel.editor.scene.coordinateaxes.HudCoordinateAxes;
import com.malte3d.suturo.sme.ui.viewmodel.editor.scene.floor.FloorGrid;
import com.malte3d.suturo.sme.ui.viewmodel.editor.scene.transform.TransformHandler;
import com.malte3d.suturo.sme.ui.viewmodel.editor.util.EditorUtil;
import lombok.Getter;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

/**
 * The Editor is the main entry point for the 3D-Editor.
 * <br>
 * <br>
 * <p>
 * Coordinate System: (jMonkeyEngine)
 * <ul>
 *   <li>The coordinate system is right-handed</li>
 *   <li>X-Axis: Right</li>
 *   <li>Y-Axis: Up</li>
 *   <li>Z-Axis: Front</li>
 *   <li>Origin: (0, 0, 0)</li>
 * </ul>
 * <strong>Important:</strong>: This is different to the ROS default and the coordinate system used in the SUTURO projects semantic maps.
 * </p>
 */
@Slf4j
public class Editor extends AbstractJmeApplication {

    private static final String ASSETS_PATH = System.getProperty("user.dir") + File.separator + "assets";
    private static final ColorRGBA BACKGROUND_COLOR = new ColorRGBA(EditorUtil.hexToVec3("#fafafa"));

    private static final String COORDINATE_AXIS = "CoordinateAxis";

    private static final Vector3f FRAME_ORIGIN = Vector3f.ZERO;

    public static final String OBJECT_TYPE = "OBJECT_TYPE";

    @NonNull
    private final DomainEventHandler domainEventHandler;

    @NonNull
    private Class<? extends CameraKeymap> cameraKeymap;

    @Getter
    private TransformHandler transformHandler;

    /* HUD */

    private DebugMode debugMode;

    private HudCoordinateAxes hudCoordinateAxes;
    private FloorGrid floorGrid;

    private final Node axes = new Node("CoordinateAxes");

    /* Scenegraph */

    @Getter
    private Node scenegraph = new Node("Scenegraph");

    /**
     * Use the factory method to create a new instance of the 3D-Editor.
     *
     * @param domainEventHandler The domain event handler to be used
     * @param debugMode          The initial debug mode to be used
     * @param keymap             The keymap to be used for the camera
     * @param initialStates      The initial states to be added to the 3D-Editor
     */
    private Editor(
            @NonNull DomainEventHandler domainEventHandler,
            @NonNull DebugMode debugMode,
            @NonNull Class<? extends CameraKeymap> keymap,
            @NonNull AppState... initialStates) {

        super(initialStates);

        this.domainEventHandler = domainEventHandler;
        this.debugMode = debugMode;

        this.cameraKeymap = keymap;
    }

    /**
     * Creates and initializes a new instance of the 3D-Editor.
     * <p>
     * <b>Warning:</b> This call is blocking an may take some time to complete.
     * </p>
     *
     * @param domainEventHandler The domain event handler to be used
     * @param debugMode          The initial debug mode to be used
     * @param keymap             The keymap to be used for the camera
     * @param initialStates      The initial states to be added to the 3D-Editor
     * @return A new initialized instance of the 3D-Editor
     */
    public static Editor create(
            @NonNull DomainEventHandler domainEventHandler,
            @NonNull DebugMode debugMode,
            @NonNull Class<? extends CameraKeymap> keymap,
            @NonNull AppState... initialStates) {

        Editor editor = new Editor(domainEventHandler, debugMode, keymap, initialStates);

        try {

            editor.completeInitialization().await();

        } catch (InterruptedException e) {
            log.error("Error while waiting for 3D-Editor to be initialized", e);
        }

        return editor;
    }

    /**
     * Creates and initializes a new instance of the 3D-Editor.
     * <p>
     * <b>Warning:</b> This call is blocking an may take some time to complete.
     * </p>
     *
     * @param domainEventHandler The domain event handler to be used
     * @param debugMode          The initial debug mode to be used
     * @param keymap             The keymap to be used for the camera
     * @param initialStates      The initial states to be added to the 3D-Editor
     * @return A new initialized instance of the 3D-Editor
     */
    public static Editor create(
            @NonNull DomainEventHandler domainEventHandler,
            @NonNull DebugMode debugMode,
            @NonNull Class<? extends CameraKeymap> keymap,
            @NonNull Collection<AppState> initialStates
    ) {
        return create(domainEventHandler, debugMode, keymap, initialStates.toArray(new AppState[0]));
    }

    @Override
    public void initApp() {

        domainEventHandler.register(DebugModeChangedEvent.class, event -> enqueue(() -> onDebugModeChanged(event)));

        assetManager.registerLocator(ASSETS_PATH, FileLocator.class);

        stateManager.attach(new EditorCameraAppState(domainEventHandler, cameraKeymap, scenegraph, guiNode));

        transformHandler = new TransformHandler(domainEventHandler, cam, inputManager, assetManager, scenegraph);

        viewPort.setBackgroundColor(BACKGROUND_COLOR);

        AmbientLight ambientLight = new AmbientLight(ColorRGBA.Gray);
        DirectionalLight directionalLight = new DirectionalLight(new Vector3f(-1.3f, -1.1f, -1.2f), ColorRGBA.White);

        LightProbe probeLight = (LightProbe) assetManager.loadModel("Probes/Parking_Lot.j3o").getLocalLightList().get(0);
        probeLight.setPosition(Vector3f.ZERO);
        probeLight.getArea().setRadius(1000f);

        rootNode.addLight(probeLight);
        rootNode.addLight(ambientLight);
        rootNode.addLight(directionalLight);

        initFloorGrid();
        initCoordinateAxes();
        initHudCoordinateAxes();

        scenegraph.setUserData(OBJECT_TYPE, SmObjectType.NULL.eternalId);

        rootNode.attachChild(scenegraph);
    }

    @Override
    public void simpleUpdate(float tpf) {
        floorGrid.update(cam);
        hudCoordinateAxes.update(cam);
    }

    private void onDebugModeChanged(DebugModeChangedEvent event) {
        this.debugMode = event.getNewDebugMode();
        hudCoordinateAxes.setDebugMode(debugMode);
        initCoordinateAxes();
    }

    /**
     * Updates the keymap to be used for the camera.
     *
     * @param cameraKeymap The keymap to be used for the camera
     */
    public void setCameraKeymap(@NonNull Class<? extends CameraKeymap> cameraKeymap) {
        this.cameraKeymap = cameraKeymap;
        EditorCameraAppState editorCameraAppState = getStateManager().getState(EditorCameraAppState.class);
        editorCameraAppState.setKeymap(cameraKeymap);
    }

    /**
     * Updates the scenegraph to be used.
     *
     * @param scenegraph The new scenegraph to be used
     */
    public void setScenegraph(@NonNull Node scenegraph) {
        rootNode.detachChild(this.scenegraph);
        this.scenegraph = scenegraph;
        rootNode.attachChild(this.scenegraph);
    }


    /**
     * Imports a scene from the given file.
     *
     * @param file The file from which the scene should be imported
     */
    public void importScene(@NonNull File file) {

        assetManager.registerLocator(file.getParent(), FileLocator.class);

        rootNode.detachChild(scenegraph);
        this.scenegraph = (Node) assetManager.loadModel(file.getName());
        rootNode.attachChild(scenegraph);
        
        domainEventHandler.raise(new SceneChangedEvent(scenegraph));
    }

    /**
     * Exports the current scene to the given file.
     *
     * @param file The file to which the scene should be exported
     * @throws IOException If an error occurs while exporting the scene
     */
    public void exportScene(@NonNull File file) throws IOException {

        BinaryExporter exporter = BinaryExporter.getInstance();
        exporter.save(scenegraph, file);
    }

    /**
     * Adds a new object to the scene.
     *
     * @param object The object to be added to the scene
     */
    public void addObjectToScene(@NonNull SmObject object) {

        if (object instanceof com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.general.NullObject nullObject)
            attachNullObject(nullObject);
        else if (object instanceof com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.primitive.Box box)
            attachBox(box);
        else if (object instanceof com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.primitive.Sphere sphere)
            attachSphere(sphere);
        else if (object instanceof com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.primitive.Cylinder cylinder)
            attachCylinder(cylinder);
        else if (object instanceof com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.primitive.Plane plane)
            attachPlane(plane);
        else if (object instanceof com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.imported.ImportObject importObject)
            attachImportObject(importObject);
        else
            log.error("Unsupported object type: {}", object.getType());
    }

    private void attachNullObject(@NonNull com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.general.NullObject object) {

        Node node = new Node(object.getName().getValue());
        node.setUserData(OBJECT_TYPE, object.getType().eternalId);
        node.setLocalTranslation(EditorUtil.toVector3f(object.getPosition()));
        node.setLocalRotation(EditorUtil.toQuaternion(object.getRotation()));

        attachObjectToScenegraph(node);
    }

    private void attachBox(@NonNull com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.primitive.Box object) {

        Box mesh = new Box(object.getWidth() / 2, object.getHeight() / 2, object.getDepth() / 2);

        Geometry geometry = createGeometry(mesh, object);
        geometry.setLocalTranslation(EditorUtil.toVector3f(object.getPosition()));
        geometry.setLocalRotation(EditorUtil.toQuaternion(object.getRotation()));

        attachObjectToScenegraph(geometry);
    }

    private void attachCylinder(@NonNull com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.primitive.Cylinder object) {

        Cylinder mesh = new Cylinder(32, 32, object.getRadius(), object.getHeight(), true);

        Geometry geometry = createGeometry(mesh, object);
        geometry.setLocalTranslation(EditorUtil.toVector3f(object.getPosition()));
        geometry.setLocalRotation(EditorUtil.toQuaternion(object.getRotation()));

        attachObjectToScenegraph(geometry);
    }

    private void attachSphere(@NonNull com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.primitive.Sphere object) {

        Sphere mesh = new Sphere(32, 32, object.getRadius());

        Geometry geometry = createGeometry(mesh, object);
        geometry.setLocalTranslation(EditorUtil.toVector3f(object.getPosition()));
        geometry.setLocalRotation(EditorUtil.toQuaternion(object.getRotation()));

        attachObjectToScenegraph(geometry);
    }

    private void attachPlane(@NonNull com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.primitive.Plane object) {

        Quad mesh = new Quad(object.getWidth(), object.getHeight());

        Geometry geometry = createGeometry(mesh, object);
        Position position = object.getPosition();
        geometry.setLocalTranslation(position.getX() - object.getWidth() / 2, position.getY() - object.getHeight() / 2, position.getZ());
        geometry.setLocalRotation(EditorUtil.toQuaternion(object.getRotation()));

        attachObjectToScenegraph(geometry);
    }

    private void attachImportObject(@NonNull ImportObject object) {

        String modelPath = object.getPath().getValue();
        File file = new File(modelPath);

        assetManager.registerLocator(file.getParent(), FileLocator.class);

        ModelKey modelKey = new ModelKey(file.getName());
        Spatial model = assetManager.loadModel(modelKey);
        model.setUserData(OBJECT_TYPE, object.getType().eternalId);

        attachObjectToScenegraph(model);
    }


    private void attachObjectToScenegraph(Spatial object) {

        scenegraph.attachChild(object);

        domainEventHandler.raise(new ObjectAttachedEvent(object));
    }

    private Geometry createGeometry(Mesh mesh, SmObject object) {

        Geometry geometry = new Geometry(object.getName().getValue(), mesh);
        geometry.setUserData(OBJECT_TYPE, object.getType().eternalId);

        Material material = createDefaultMaterial();

        if (SmObjectType.PLANE.equals(object.getType()))
            material.getAdditionalRenderState().setFaceCullMode(FaceCullMode.Off);

        geometry.setMaterial(material);

        return geometry;
    }

    private void initCoordinateAxes() {

        rootNode.detachChild(axes);

        if (debugMode.isEnabled())
            useJme3CoordinateAxes(axes);
        else
            useRosCoordinateAxes(axes);

        rootNode.attachChild(axes);
    }

    private void useJme3CoordinateAxes(Node node) {

        while (node.detachChildNamed(COORDINATE_AXIS) != -1) ;

        attachCoordinateAxesShape(node, new Arrow(Vector3f.UNIT_X.mult(2)), ColorRGBA.Red);
        attachCoordinateAxesShape(node, new Arrow(Vector3f.UNIT_Y.mult(2)), ColorRGBA.Green);
        attachCoordinateAxesShape(node, new Arrow(Vector3f.UNIT_Z.mult(2)), ColorRGBA.Blue);
    }

    private void useRosCoordinateAxes(Node node) {

        while (node.detachChildNamed(COORDINATE_AXIS) != -1) ;

        attachCoordinateAxesShape(node, new Arrow(Vector3f.UNIT_Z.mult(2)), ColorRGBA.Red);
        attachCoordinateAxesShape(node, new Arrow(Vector3f.UNIT_X.mult(2)), ColorRGBA.Green);
        attachCoordinateAxesShape(node, new Arrow(Vector3f.UNIT_Y.mult(2)), ColorRGBA.Blue);
    }

    private void attachCoordinateAxesShape(Node node, Mesh shape, ColorRGBA color) {

        Material mat = new Material(assetManager, Materials.UNSHADED);
        mat.setColor("Color", color);

        Geometry coordinateAxis = new Geometry(COORDINATE_AXIS, shape);
        coordinateAxis.setMaterial(mat);
        coordinateAxis.setLocalTranslation(FRAME_ORIGIN);

        node.attachChild(coordinateAxis);
    }

    private void initHudCoordinateAxes() {
        this.hudCoordinateAxes = new HudCoordinateAxes(debugMode, guiNode, assetManager);
    }

    private void initFloorGrid() {
        this.floorGrid = new FloorGrid(assetManager);
        rootNode.attachChild(floorGrid.getNode());
    }

    private Material createDefaultMaterial() {

        Material material = new Material(assetManager, Materials.LIGHTING);
        material.setBoolean("UseMaterialColors", true);
        material.setColor("Diffuse", ColorRGBA.Gray);
        material.setColor("Ambient", ColorRGBA.Gray);
        material.setColor("Specular", ColorRGBA.White);
        material.setFloat("Shininess", 1f);

        return material;
    }

    private Material createDefaultUnshadedMaterial(ColorRGBA color) {

        Material material = new Material(assetManager, Materials.UNSHADED);
        material.setColor("Color", color);

        return material;
    }

}
