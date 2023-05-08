package com.malte3d.suturo.sme.ui.viewmodel.main.editor;

import com.jayfella.jfx.embedded.AbstractJmeApplication;
import com.jme3.app.state.AppState;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseAxisTrigger;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.material.Materials;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Geometry;
import com.jme3.scene.Mesh;
import com.jme3.scene.control.CameraControl;
import com.jme3.scene.debug.Arrow;
import com.jme3.scene.debug.Grid;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;

public class MainEditor extends AbstractJmeApplication {

    private static final ColorRGBA BACKGROUND_COLOR = new ColorRGBA(EditorUtil.hexToVec3("#fafafa"));

    private static final Vector3f FRAME_ORIGIN = new Vector3f(0, 0, 0);

    private CameraInputListener cameraInputListener;

    private Geometry box;

    public MainEditor(AppState... initialStates) {
        super(initialStates);
    }

    @Override
    public void initApp() {

        CameraNode camNode = new CameraNode("CamNode", cam);
        camNode.setControlDir(CameraControl.ControlDirection.SpatialToCamera);
        camNode.addControl(new CameraControl(cam));

        cameraInputListener = new CameraInputListener(camNode);

        inputManager.addMapping("MoveForward", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("MoveBackward", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("MoveLeft", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("MoveRight", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("RotateLeft", new MouseAxisTrigger(MouseInput.AXIS_X, true));
        inputManager.addMapping("RotateRight", new MouseAxisTrigger(MouseInput.AXIS_X, false));
        inputManager.addMapping("RotateUp", new MouseAxisTrigger(MouseInput.AXIS_Y, true));
        inputManager.addMapping("RotateDown", new MouseAxisTrigger(MouseInput.AXIS_Y, false));
        inputManager.addListener(cameraInputListener, "MoveForward", "MoveBackward", "MoveLeft", "MoveRight", "RotateLeft", "RotateRight", "RotateUp", "RotateDown");

        viewPort.setBackgroundColor(BACKGROUND_COLOR);

        DirectionalLight directionalLight = new DirectionalLight(
                new Vector3f(-1, -1, -1).normalizeLocal(),
                ColorRGBA.White
        );

        rootNode.addLight(directionalLight);

        Texture texture = assetManager.loadTexture("com/jme3/app/Monkey.png");

        box = new Geometry("Box", new Box(1, 1, 1));
        box.setMaterial(new Material(assetManager, Materials.PBR));
        box.getMaterial().setTexture("BaseColorMap", texture);
        box.getMaterial().setColor("BaseColor", ColorRGBA.White);
        box.getMaterial().setFloat("Roughness", 0.001f);
        box.getMaterial().setFloat("Metallic", 0.001f);

        attachGrid(FRAME_ORIGIN, 10, ColorRGBA.Black);
        attachCoordinateAxes(FRAME_ORIGIN);

        rootNode.attachChild(box);
    }

    @Override
    public void simpleUpdate(float tpf) {
        cameraInputListener.update(tpf);
        box.rotate(tpf * .2f, tpf * .3f, tpf * .4f);
    }

    private void attachCoordinateAxes(Vector3f pos) {

        Arrow arrow = new Arrow(Vector3f.UNIT_X);
        putShape(arrow, ColorRGBA.Red).setLocalTranslation(pos);

        arrow = new Arrow(Vector3f.UNIT_Y);
        putShape(arrow, ColorRGBA.Green).setLocalTranslation(pos);

        arrow = new Arrow(Vector3f.UNIT_Z);
        putShape(arrow, ColorRGBA.Blue).setLocalTranslation(pos);
    }

    private Geometry putShape(Mesh shape, ColorRGBA color) {

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setWireframe(true);
        mat.getAdditionalRenderState().setLineWidth(4);
        mat.setColor("Color", color);

        Geometry g = new Geometry("coordinate axis", shape);
        g.setMaterial(mat);

        rootNode.attachChild(g);

        return g;
    }

    private Geometry attachGrid(Vector3f pos, int size, ColorRGBA color) {

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        mat.getAdditionalRenderState().setWireframe(true);
        mat.setColor("Color", color);

        Geometry g = new Geometry("wireframe grid", new Grid(size, size, 0.2f));
        g.setMaterial(mat);
        g.center().move(pos);

        rootNode.attachChild(g);

        return g;
    }

}
