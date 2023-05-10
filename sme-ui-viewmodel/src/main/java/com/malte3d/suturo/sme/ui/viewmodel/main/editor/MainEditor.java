package com.malte3d.suturo.sme.ui.viewmodel.main.editor;

import com.jayfella.jfx.embedded.AbstractJmeApplication;
import com.jme3.app.state.AppState;
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
import lombok.extern.slf4j.Slf4j;

import java.util.Collection;

@Slf4j
public class MainEditor extends AbstractJmeApplication {

    private static final ColorRGBA BACKGROUND_COLOR = new ColorRGBA(EditorUtil.hexToVec3("#fafafa"));

    private static final Vector3f FRAME_ORIGIN = new Vector3f(0, 0, 0);

    private Node box;

    /**
     * Use the factory method {@link #create(AppState...)} to create a new instance of the MainEditor.
     *
     * @param initialStates The initial states to be added to the MainEditor
     */
    private MainEditor(AppState... initialStates) {
        super(initialStates);
    }

    /**
     * Creates and initializes a new instance of the MainEditor.
     * <p>
     * <b>Warning:</b> This call is blocking an may take some time to complete.
     * </p>
     *
     * @param initialStates The initial states to be added to the MainEditor
     * @return A new initialized instance of the MainEditor
     */
    public static MainEditor create(AppState... initialStates) {

        MainEditor mainEditor = new MainEditor(initialStates);

        try {

            mainEditor.completeInitialization().await();

        } catch (InterruptedException e) {
            log.error("Error while waiting for MainEditor to be initialized", e);
        }

        return mainEditor;
    }

    /**
     * Creates and initializes a new instance of the MainEditor.
     * <p>
     * <b>Warning:</b> This call is blocking an may take some time to complete.
     * </p>
     *
     * @param initialStates The initial states to be added to the MainEditor
     * @return A new initialized instance of the MainEditor
     */
    public static MainEditor create(Collection<AppState> initialStates) {
        return create(initialStates.toArray(new AppState[0]));
    }

    @Override
    public void initApp() {

        flyCam.setMoveSpeed(6f);
        flyCam.setDragToRotate(true);

        viewPort.setBackgroundColor(BACKGROUND_COLOR);

        DirectionalLight directionalLight = new DirectionalLight(
                new Vector3f(-1, -1, -1).normalizeLocal(),
                ColorRGBA.White
        );

        rootNode.addLight(directionalLight);

        Texture texture = assetManager.loadTexture("com/jme3/app/Monkey.png");

        Geometry boxGeom = new Geometry("Box", new Box(1, 1, 1));
        boxGeom.setMaterial(new Material(assetManager, Materials.PBR));
        boxGeom.getMaterial().setTexture("BaseColorMap", texture);
        boxGeom.getMaterial().setColor("BaseColor", ColorRGBA.White);
        boxGeom.getMaterial().setFloat("Roughness", 0.001f);
        boxGeom.getMaterial().setFloat("Metallic", 0.001f);

        box = new Node("box");
        box.attachChild(boxGeom);
        attachCoordinateAxes(box);


        attachGrid();
        attachCoordinateAxes(rootNode);

        rootNode.attachChild(box);
    }

    @Override
    public void simpleUpdate(float tpf) {
        box.rotate(tpf * .2f, tpf * .3f, tpf * .4f);
    }

    private void attachCoordinateAxes(Node node) {

        Arrow arrow = new Arrow(Vector3f.UNIT_X.mult(2));
        attachCoordinateAxesShape(node, arrow, ColorRGBA.Red).setLocalTranslation(MainEditor.FRAME_ORIGIN);

        arrow = new Arrow(Vector3f.UNIT_Y.mult(2));
        attachCoordinateAxesShape(node, arrow, ColorRGBA.Green).setLocalTranslation(MainEditor.FRAME_ORIGIN);

        arrow = new Arrow(Vector3f.UNIT_Z.mult(2));
        attachCoordinateAxesShape(node, arrow, ColorRGBA.Blue).setLocalTranslation(MainEditor.FRAME_ORIGIN);
    }

    private Geometry attachCoordinateAxesShape(Node node, Mesh shape, ColorRGBA color) {

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setWireframe(true);
        mat.getAdditionalRenderState().setLineWidth(4);
        mat.setColor("Color", color);

        Geometry g = new Geometry("coordinate axis", shape);
        g.setMaterial(mat);

        node.attachChild(g);

        return g;
    }

    private void attachGrid() {

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setWireframe(true);
        mat.setColor("Color", ColorRGBA.Black);

        Geometry g = new Geometry("wireframe grid", new Grid(10, 10, 1.0f));
        g.setMaterial(mat);
        g.center().move(MainEditor.FRAME_ORIGIN);

        rootNode.attachChild(g);
    }

}
