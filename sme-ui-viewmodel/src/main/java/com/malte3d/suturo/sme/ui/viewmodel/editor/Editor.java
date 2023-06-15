package com.malte3d.suturo.sme.ui.viewmodel.editor;


import java.io.File;
import java.util.Collection;

import com.jayfella.jfx.embedded.AbstractJmeApplication;
import com.jme3.app.state.AppState;
import com.jme3.asset.plugins.FileLocator;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.Materials;
import com.jme3.material.RenderState.FaceCullMode;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.debug.Arrow;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Cylinder;
import com.jme3.scene.shape.Quad;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.Position;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.SmObject;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.SmObjectType;
import com.malte3d.suturo.sme.ui.viewmodel.editor.camera.CameraKeymap;
import com.malte3d.suturo.sme.ui.viewmodel.editor.camera.EditorCameraAppState;
import com.malte3d.suturo.sme.ui.viewmodel.editor.floor.FloorGrid;
import com.malte3d.suturo.sme.ui.viewmodel.editor.hud.coordinateaxes.CoordinateAxes;
import com.malte3d.suturo.sme.ui.viewmodel.editor.util.EditorUtil;
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

    private static final Vector3f FRAME_ORIGIN = Vector3f.ZERO;

    public static final String OBJECT_TYPE = "OBJECT_TYPE";

    @NonNull
    private Class<? extends CameraKeymap> cameraKeymap;

    /* HUD */
    private CoordinateAxes coordinateAxes;

    private FloorGrid floorGrid;

    private Node debugBox;

    private final Node scenegraph = new Node("SceneGraph");

    /**
     * Use the factory method to create a new instance of the 3D-Editor.
     *
     * @param cameraKeymap  The keymap to be used for the camera
     * @param initialStates The initial states to be added to the 3D-Editor
     */
    private Editor(@NonNull Class<? extends CameraKeymap> cameraKeymap, @NonNull AppState... initialStates) {

        super(initialStates);

        this.cameraKeymap = cameraKeymap;
    }

    /**
     * Creates and initializes a new instance of the 3D-Editor.
     * <p>
     * <b>Warning:</b> This call is blocking an may take some time to complete.
     * </p>
     *
     * @param keymap        The keymap to be used for the camera
     * @param initialStates The initial states to be added to the 3D-Editor
     * @return A new initialized instance of the 3D-Editor
     */
    public static Editor create(@NonNull Class<? extends CameraKeymap> keymap, @NonNull AppState... initialStates) {

        Editor editor = new Editor(keymap, initialStates);

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
     * @param keymap        The keymap to be used for the camera
     * @param initialStates The initial states to be added to the 3D-Editor
     * @return A new initialized instance of the 3D-Editor
     */
    public static Editor create(@NonNull Class<? extends CameraKeymap> keymap, @NonNull Collection<AppState> initialStates) {
        return create(keymap, initialStates.toArray(new AppState[0]));
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

    @Override
    public void initApp() {

        assetManager.registerLocator(ASSETS_PATH, FileLocator.class);

        stateManager.attach(new EditorCameraAppState(cameraKeymap, scenegraph, guiNode));

        viewPort.setBackgroundColor(BACKGROUND_COLOR);

        AmbientLight ambientLight = new AmbientLight(ColorRGBA.White);
        DirectionalLight directionalLight = new DirectionalLight(new Vector3f(-1.3f, -1.1f, -1.2f), ColorRGBA.White);

        rootNode.addLight(ambientLight);
        rootNode.addLight(directionalLight);

        attachHudCoordinateAxes();

        attachFloorGrid();
        attachCoordinateAxes(rootNode);

        attachDebugBox();
        rootNode.attachChild(scenegraph);
    }

    @Override
    public void simpleUpdate(float tpf) {

        debugBox.rotate(tpf * .2f, tpf * .3f, tpf * .4f);

        floorGrid.update(cam);
        coordinateAxes.update(cam);
    }

    public void addObjectToScene(@NonNull SmObject object) {

        if (object instanceof com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.primitive.Box box)
            attachBox(box);
        else if (object instanceof com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.primitive.Sphere sphere)
            attachSphere(sphere);
        else if (object instanceof com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.primitive.Cylinder cylinder)
            attachCylinder(cylinder);
        else if (object instanceof com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.primitive.Plane plane)
            attachPlane(plane);
        else
            log.error("Unsupported object type: {}", object.getType());
    }

    private void attachBox(@NonNull com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.primitive.Box object) {

        Box mesh = new Box(object.getWidth() / 2, object.getHeight() / 2, object.getDepth() / 2);

        Geometry geometry = createGeometry(mesh, object);
        geometry.setLocalTranslation(EditorUtil.toVector3f(object.getPosition()));
        geometry.setLocalRotation(EditorUtil.toQuaternion(object.getRotation()));

        scenegraph.attachChild(geometry);
    }

    private void attachCylinder(@NonNull com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.primitive.Cylinder object) {

        Cylinder mesh = new Cylinder(32, 32, object.getRadius(), object.getHeight(), true);

        Geometry geometry = createGeometry(mesh, object);
        geometry.setLocalTranslation(EditorUtil.toVector3f(object.getPosition()));
        geometry.setLocalRotation(EditorUtil.toQuaternion(object.getRotation()));

        scenegraph.attachChild(geometry);
    }

    private void attachSphere(@NonNull com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.primitive.Sphere object) {

        Sphere mesh = new Sphere(32, 32, object.getRadius());

        Geometry geometry = createGeometry(mesh, object);
        geometry.setLocalTranslation(EditorUtil.toVector3f(object.getPosition()));
        geometry.setLocalRotation(EditorUtil.toQuaternion(object.getRotation()));

        scenegraph.attachChild(geometry);
    }

    private void attachPlane(@NonNull com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.primitive.Plane object) {

        Quad mesh = new Quad(object.getWidth(), object.getHeight());

        Geometry geometry = createGeometry(mesh, object);
        Position position = object.getPosition();
        geometry.setLocalTranslation(position.getX() - object.getWidth() / 2, position.getY() - object.getHeight() / 2, position.getZ());
        geometry.setLocalRotation(EditorUtil.toQuaternion(object.getRotation()));

        scenegraph.attachChild(geometry);
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

    private void attachCoordinateAxes(Node node) {

        attachCoordinateAxesShape(node, new Arrow(Vector3f.UNIT_X.mult(2)), ColorRGBA.Red);
        attachCoordinateAxesShape(node, new Arrow(Vector3f.UNIT_Y.mult(2)), ColorRGBA.Green);
        attachCoordinateAxesShape(node, new Arrow(Vector3f.UNIT_Z.mult(2)), ColorRGBA.Blue);
    }

    private void attachCoordinateAxesShape(Node node, Mesh shape, ColorRGBA color) {

        Material mat = new Material(assetManager, Materials.UNSHADED);
        mat.setColor("Color", color);

        Geometry coordinateAxis = new Geometry("coordinate axis", shape);
        coordinateAxis.setMaterial(mat);
        coordinateAxis.setLocalTranslation(FRAME_ORIGIN);

        node.attachChild(coordinateAxis);
    }

    private void attachHudCoordinateAxes() {
        this.coordinateAxes = new CoordinateAxes(guiNode, assetManager);
    }

    private void attachFloorGrid() {
        this.floorGrid = new FloorGrid(assetManager);
        rootNode.attachChild(floorGrid.getNode());
    }

    private void attachDebugBox() {

        Texture texture = assetManager.loadTexture("com/jme3/app/Monkey.png");

        Material material = new Material(assetManager, Materials.LIGHTING);
        material.setTexture("DiffuseMap", texture);

        Geometry debugBoxMesh = new Geometry("DebugBoxGeom", new Box(0.5f, 0.5f, 0.5f));
        debugBoxMesh.setMaterial(material);

        this.debugBox = new Node("DebugBoxNode");
        this.debugBox.attachChild(debugBoxMesh);
        this.debugBox.move(0, 2, 0);

        attachCoordinateAxes(this.debugBox);

        rootNode.attachChild(this.debugBox);
    }

    private Material createDefaultMaterial() {

        Material material = new Material(assetManager, Materials.LIGHTING);
        material.setBoolean("UseMaterialColors", true);
        material.setColor("Diffuse", ColorRGBA.DarkGray);
        material.setColor("Ambient", ColorRGBA.DarkGray);
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
