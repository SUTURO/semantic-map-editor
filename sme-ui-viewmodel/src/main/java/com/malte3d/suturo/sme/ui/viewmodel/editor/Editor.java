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
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.Node;
import com.jme3.scene.debug.Arrow;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
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

    @NonNull
    private Class<? extends CameraKeymap> cameraKeymap;

    /* HUD */
    private CoordinateAxes coordinateAxes;

    private FloorGrid floorGrid;

    private Node box;

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

        stateManager.attach(new EditorCameraAppState(cameraKeymap, rootNode, guiNode));

        viewPort.setBackgroundColor(BACKGROUND_COLOR);

        AmbientLight ambientLight = new AmbientLight(ColorRGBA.White.mult(1.3f));
        DirectionalLight directionalLight = new DirectionalLight(new Vector3f(-1, -1, -1), ColorRGBA.White);

        rootNode.addLight(ambientLight);
        rootNode.addLight(directionalLight);

        attachHudCoordinateAxes();

        attachFloorGrid();
        attachCoordinateAxes(rootNode);

        attachDebugBox();
    }

    @Override
    public void simpleUpdate(float tpf) {

        box.rotate(tpf * .2f, tpf * .3f, tpf * .4f);

        floorGrid.update(cam);
        coordinateAxes.update(cam);
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

        Geometry debugBox = new Geometry("Box", new Box(0.5f, 0.5f, 0.5f));
        debugBox.setMaterial(new Material(assetManager, Materials.PBR));
        debugBox.getMaterial().setTexture("BaseColorMap", texture);
        debugBox.getMaterial().setColor("BaseColor", ColorRGBA.White);
        debugBox.getMaterial().setFloat("Roughness", 0.001f);
        debugBox.getMaterial().setFloat("Metallic", 0.001f);

        box = new Node("box");
        box.attachChild(debugBox);
        box.move(0, 2, 0);
        attachCoordinateAxes(box);

        rootNode.attachChild(box);
    }

}
