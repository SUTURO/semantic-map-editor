package com.malte3d.suturo.sme.ui.viewmodel.editor.floor;

import com.jme3.asset.*;
import com.jme3.material.*;
import com.jme3.math.*;
import com.jme3.renderer.*;
import com.jme3.scene.*;
import com.jme3.scene.debug.*;
import lombok.*;

/**
 * A floor grid that can be attached to a node and updated based on the camera position to change its visibility.
 */
public class FloorGrid {

    private static final float CULLING_THRESHOLD = 0.05f;

    private static final float MIN_DISTANCE_SMALL = 5f;
    private static final float MAX_DISTANCE_SMALL = 30f;

    private static final float MIN_DISTANCE_LARGE = 50f;
    private static final float MAX_DISTANCE_LARGE = 150f;

    private final Geometry gridSmall;
    private final Geometry gridLarge;

    private final ColorRGBA colorSmall = ColorRGBA.Gray.clone();
    private final ColorRGBA colorLarge = ColorRGBA.DarkGray.clone();

    @Getter
    private final Node node = new Node("FloorGrid");

    private boolean smallVisible;
    private boolean largeVisible;

    public FloorGrid(@NonNull AssetManager assetManager) {

        Material matSmall = new Material(assetManager, Materials.UNSHADED);
        matSmall.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        matSmall.setColor("Color", this.colorSmall);

        this.gridSmall = new Geometry("FloorGrid Small", new Grid(101, 101, 1f));
        this.gridSmall.setMaterial(matSmall);
        this.gridSmall.center();

        Material matLarge = new Material(assetManager, Materials.UNSHADED);
        matLarge.getAdditionalRenderState().setBlendMode(RenderState.BlendMode.Alpha);
        matLarge.setColor("Color", this.colorLarge);

        this.gridLarge = new Geometry("FloorGrid Large", new Grid(11, 11, 10f));
        this.gridLarge.setMaterial(matLarge);
        this.gridLarge.center();

        this.node.attachChild(gridSmall);
        this.node.attachChild(gridLarge);
        this.smallVisible = true;
        this.largeVisible = true;
    }

    /**
     * Updates the visibility of the grid based on the camera distance.
     *
     * @param camera The camera to update the grid for.
     */
    public void update(@NonNull Camera camera) {

        float cameraDistance = camera.getLocation().distance(Vector3f.ZERO);

        float alphaSmall = getAlpha(cameraDistance, MIN_DISTANCE_SMALL, MAX_DISTANCE_SMALL);
        colorSmall.setAlpha(alphaSmall);

        float alphaLarge = getAlpha(cameraDistance, MIN_DISTANCE_LARGE, MAX_DISTANCE_LARGE);
        colorLarge.setAlpha(alphaLarge);

        gridSmall.getMaterial().setColor("Color", colorSmall);
        gridLarge.getMaterial().setColor("Color", colorLarge);

        if (smallVisible && alphaSmall <= CULLING_THRESHOLD) {

            gridSmall.setCullHint(Spatial.CullHint.Always);
            smallVisible = false;

        } else if (!smallVisible && alphaSmall > CULLING_THRESHOLD) {

            gridSmall.setCullHint(Spatial.CullHint.Dynamic);
            smallVisible = true;
        }

        if (largeVisible && alphaLarge <= CULLING_THRESHOLD) {

            gridLarge.setCullHint(Spatial.CullHint.Always);
            largeVisible = false;

        } else if (!largeVisible && alphaLarge > CULLING_THRESHOLD) {

            gridLarge.setCullHint(Spatial.CullHint.Dynamic);
            largeVisible = true;
        }
    }

    private float getAlpha(float cameraDistance, float minDistance, float maxDistance) {
        return FastMath.clamp(1f - ((cameraDistance - minDistance) / (maxDistance - minDistance)), 0f, 1f);
    }
}
