package com.malte3d.suturo.sme.ui.viewmodel.editor.hud.coordinatesystem;

import com.jme3.asset.*;
import com.jme3.material.*;
import com.jme3.math.*;
import com.jme3.renderer.*;
import com.jme3.scene.*;
import com.jme3.scene.debug.*;
import lombok.*;

public class CoordinateAxes {

    private static final float SIZE = 25f;
    private static final float PADDING = 5f;

    private static final Quaternion MIRROR = new Quaternion().fromAngleAxis(FastMath.PI, Vector3f.UNIT_Y);


    private final AssetManager assetManager;

    @Getter
    private final Node node = new Node("CoordinateAxes");

    public CoordinateAxes(@NonNull AssetManager assetManager) {

        this.assetManager = assetManager;

        node.attachChild(createGeometry(new Arrow(Vector3f.UNIT_X), ColorRGBA.Red));
        node.attachChild(createGeometry(new Arrow(Vector3f.UNIT_Y), ColorRGBA.Green));
        node.attachChild(createGeometry(new Arrow(Vector3f.UNIT_Z), ColorRGBA.Blue));

        node.setLocalScale(SIZE);
    }

    public void update(@NonNull Camera cam) {
        node.setLocalTranslation(cam.getWidth() - SIZE - PADDING, cam.getHeight() - SIZE - PADDING, 0);
        node.setLocalRotation(cam.getRotation().mult(MIRROR).inverse());
    }

    private Geometry createGeometry(Mesh shape, ColorRGBA color) {

        Material mat = new Material(assetManager, Materials.UNSHADED);
        mat.setColor("Color", color);

        Geometry coordinateAxis = new Geometry("coordinate axis", shape);
        coordinateAxis.setMaterial(mat);

        return coordinateAxis;
    }
}
