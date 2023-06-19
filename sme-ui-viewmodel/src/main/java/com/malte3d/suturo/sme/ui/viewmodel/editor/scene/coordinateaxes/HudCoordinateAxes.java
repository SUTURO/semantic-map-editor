package com.malte3d.suturo.sme.ui.viewmodel.editor.scene.coordinateaxes;

import com.jme3.asset.AssetManager;
import com.jme3.font.BitmapFont;
import com.jme3.font.BitmapText;
import com.jme3.material.Material;
import com.jme3.material.Materials;
import com.jme3.math.ColorRGBA;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.debug.Arrow;
import com.malte3d.suturo.sme.domain.model.application.settings.advanced.DebugMode;
import lombok.NonNull;

import java.util.HashMap;
import java.util.Map;

/**
 * Helper class to display the current rotation of the coordinate axes in relation to the camera.
 *
 * <p>
 * Similar to Blender/Cinema4D, the coordinate axes will be displayed on the HUD in the upper right corner.
 * </p>
 */
public class HudCoordinateAxes {

    private static final float AXES_SIZE = 20f;
    private static final float LABEL_SIZE = 12f;

    private static final float PADDING = 5f;

    private static final Quaternion MIRROR = new Quaternion().fromAngleNormalAxis(FastMath.PI, Vector3f.UNIT_Y);

    private final AssetManager assetManager;
    private final BitmapFont font;

    private final Node axes = new Node("HudCoordinateAxes");
    private final Map<String, Node> axesLabels = new HashMap<>();

    @NonNull
    private DebugMode debugMode;

    public HudCoordinateAxes(@NonNull DebugMode debugMode, @NonNull Node guiNode, @NonNull AssetManager assetManager) {

        this.assetManager = assetManager;
        this.font = assetManager.loadFont("Interface/Fonts/Default.fnt");

        this.debugMode = debugMode;

        initAxes();
        guiNode.attachChild(axes);
    }

    public void setDebugMode(@NonNull DebugMode debugMode) {
        this.debugMode = debugMode;
        initAxes();
    }

    public void update(@NonNull Camera cam) {

        final float size = PADDING + AXES_SIZE + LABEL_SIZE;
        Quaternion rotation = cam.getRotation().mult(MIRROR);

        axes.setLocalTranslation(cam.getWidth() - size, cam.getHeight() - size, 0);
        axes.setLocalRotation(rotation.inverse());

        for (Node label : axesLabels.values())
            label.setLocalRotation(rotation);
    }

    /**
     * Creates and attaches axes based on the current debug mode.
     */
    private void initAxes() {

        if (debugMode.isEnabled())
            useJme3Axes();
        else
            useRosAxes();
    }

    /**
     * Creates and attaches axes using the default JME3 coordinate system.
     */
    private void useJme3Axes() {

        axes.detachAllChildren();

        createAndAttachAxis(Vector3f.UNIT_X, "X", ColorRGBA.Red);
        createAndAttachAxis(Vector3f.UNIT_Y, "Y", ColorRGBA.Green);
        createAndAttachAxis(Vector3f.UNIT_Z, "Z", ColorRGBA.Blue);
    }

    /**
     * Creates and attaches axes using the ROS coordinate system.
     */
    private void useRosAxes() {

        axes.detachAllChildren();

        createAndAttachAxis(Vector3f.UNIT_Z, "X", ColorRGBA.Red);
        createAndAttachAxis(Vector3f.UNIT_X, "Y", ColorRGBA.Green);
        createAndAttachAxis(Vector3f.UNIT_Y, "Z", ColorRGBA.Blue);
    }

    private void createAndAttachAxis(Vector3f extend, String label, ColorRGBA color) {

        Node labelNode = createLabelNode(label, color);
        Node axisNode = createAxisNode(extend, color);
        axisNode.attachChild(labelNode);

        axesLabels.put(label, labelNode);
        axes.attachChild(axisNode);
    }

    private Node createAxisNode(Vector3f extend, ColorRGBA color) {

        Arrow arrow = new Arrow(extend);
        Material mat = new Material(assetManager, Materials.UNSHADED);
        mat.setColor("Color", color);

        Geometry coordinateAxis = new Geometry("coordinate axis", arrow);
        coordinateAxis.setMaterial(mat);
        coordinateAxis.setLocalScale(AXES_SIZE);

        Node axisNode = new Node("Axis");
        axisNode.attachChild(coordinateAxis);

        return axisNode;
    }

    private Node createLabelNode(String label, ColorRGBA color) {

        BitmapText text = new BitmapText(font);
        text.setText(label);
        text.setColor(color);
        text.setSize(LABEL_SIZE);
        text.setLocalTranslation(-text.getLineWidth() / 2, text.getLineHeight() / 2, 0);

        Node labelNode = new Node(label + " Axis");
        labelNode.attachChild(text);

        float offset = AXES_SIZE + text.getLineHeight() / 2;

        if (debugMode.isEnabled()) {

            switch (label) {
                case "X" -> labelNode.setLocalTranslation(offset, 0, 0);
                case "Y" -> labelNode.setLocalTranslation(0, offset, 0);
                case "Z" -> labelNode.setLocalTranslation(0, 0, offset);
            }

        } else {

            switch (label) {
                case "X" -> labelNode.setLocalTranslation(0, 0, offset);
                case "Y" -> labelNode.setLocalTranslation(offset, 0, 0);
                case "Z" -> labelNode.setLocalTranslation(0, offset, 0);
            }
        }


        return labelNode;
    }
}
