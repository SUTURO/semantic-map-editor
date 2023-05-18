package com.malte3d.suturo.sme.ui.viewmodel.editor;

import com.jayfella.jfx.embedded.AbstractJmeApplication;
import com.jme3.app.state.AppState;
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
import com.jme3.scene.debug.Grid;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;
import com.malte3d.suturo.sme.ui.viewmodel.editor.camera.EditorCameraAppState;
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

/**
 * The Editor is the main entry point for the 3D-Editor.
 */
@Slf4j
public class Editor extends AbstractJmeApplication {

    private static final ColorRGBA BACKGROUND_COLOR = new ColorRGBA(EditorUtil.hexToVec3("#fafafa"));

    private static final Vector3f FRAME_ORIGIN = new Vector3f(0, 0, 0);

    private Node box;

    /**
     * Use the factory method {@link #create(AppState...)} to create a new instance of the 3D-Editor.
     *
     * @param initialStates The initial states to be added to the 3D-Editor
     */
    private Editor(AppState... initialStates) {
        super(initialStates);
    }

    /**
     * Creates and initializes a new instance of the 3D-Editor.
     * <p>
     * <b>Warning:</b> This call is blocking an may take some time to complete.
     * </p>
     *
     * @param initialStates The initial states to be added to the 3D-Editor
     * @return A new initialized instance of the 3D-Editor
     */
    public static Editor create(AppState... initialStates) {

        Editor editor = new Editor(initialStates);

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
     * @param initialStates The initial states to be added to the 3D-Editor
     * @return A new initialized instance of the 3D-Editor
     */
    public static Editor create(Collection<AppState> initialStates) {
        return create(initialStates.toArray(new AppState[0]));
    }

    @Override
    public void initApp() {

        stateManager.attach(new EditorCameraAppState(rootNode));

        viewPort.setBackgroundColor(BACKGROUND_COLOR);

        DirectionalLight directionalLight = new DirectionalLight(
                new Vector3f(-1, -1, -1).normalizeLocal(),
                ColorRGBA.White
        );

        rootNode.addLight(directionalLight);
        rootNode.addLight(new AmbientLight(ColorRGBA.White.mult(0.7f)));

        attachGroundGrid();
        attachCoordinateAxes(rootNode);

        attachDebugBox();
    }

    @Override
    public void simpleUpdate(float tpf) {
        box.rotate(tpf * .2f, tpf * .3f, tpf * .4f);
    }

    private void attachCoordinateAxes(Node node) {

        Arrow arrow = new Arrow(Vector3f.UNIT_X.mult(2));
        attachCoordinateAxesShape(node, arrow, ColorRGBA.Red).setLocalTranslation(FRAME_ORIGIN);

        arrow = new Arrow(Vector3f.UNIT_Y.mult(2));
        attachCoordinateAxesShape(node, arrow, ColorRGBA.Green).setLocalTranslation(FRAME_ORIGIN);

        arrow = new Arrow(Vector3f.UNIT_Z.mult(2));
        attachCoordinateAxesShape(node, arrow, ColorRGBA.Blue).setLocalTranslation(FRAME_ORIGIN);
    }

    private Geometry attachCoordinateAxesShape(Node node, Mesh shape, ColorRGBA color) {

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setWireframe(true);
        mat.getAdditionalRenderState().setLineWidth(4);
        mat.setColor("Color", color);

        Geometry coordinateAxis = new Geometry("coordinate axis", shape);
        coordinateAxis.setMaterial(mat);

        node.attachChild(coordinateAxis);

        return coordinateAxis;
    }

    private void attachGroundGrid() {

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setWireframe(true);
        mat.setColor("Color", ColorRGBA.DarkGray);

        Geometry groundGrid = new Geometry("wireframe grid", new Grid(10, 10, 1.0f));
        groundGrid.setMaterial(mat);
        groundGrid.center().move(FRAME_ORIGIN);

        rootNode.attachChild(groundGrid);
    }

    private void attachDebugBox() {

        Texture texture = assetManager.loadTexture("com/jme3/app/Monkey.png");

        Geometry debugBox = new Geometry("Box", new Box(1, 1, 1));
        debugBox.setMaterial(new Material(assetManager, Materials.PBR));
        debugBox.getMaterial().setTexture("BaseColorMap", texture);
        debugBox.getMaterial().setColor("BaseColor", ColorRGBA.White);
        debugBox.getMaterial().setFloat("Roughness", 0.001f);
        debugBox.getMaterial().setFloat("Metallic", 0.001f);

        box = new Node("box");
        box.attachChild(debugBox);
        box.move(0, 4, 0);
        attachCoordinateAxes(box);

        rootNode.attachChild(box);
    }

}
