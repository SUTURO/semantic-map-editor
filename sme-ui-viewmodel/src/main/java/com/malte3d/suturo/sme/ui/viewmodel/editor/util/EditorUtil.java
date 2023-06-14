package com.malte3d.suturo.sme.ui.viewmodel.editor.util;

import com.google.common.base.Preconditions;
import com.jme3.math.Vector3f;
import com.malte3d.suturo.commons.javafx.fxml.Color;
import com.malte3d.suturo.sme.domain.model.semanticmap.scenegraph.object.Rotation;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class EditorUtil {

    /**
     * Converts a hex color string to a {@link Vector3f} with values between 0 and 1.
     *
     * @param hexColor Hex color string in the format #RRGGBB or #RGB
     * @return {@link Vector3f} with values between 0 and 1
     */
    public static Vector3f hexToVec3(@NonNull String hexColor) {

        Preconditions.checkArgument(Color.isValidHexColor(hexColor), "Invalid hex color: %s", hexColor);

        String hexColorValue = Color.getHexColorValue(hexColor);

        return new Vector3f(
                Integer.valueOf(hexColorValue.substring(0, 2), 16) / 255f,
                Integer.valueOf(hexColorValue.substring(2, 4), 16) / 255f,
                Integer.valueOf(hexColorValue.substring(4, 6), 16) / 255f
        );
    }

    public static Vector3f toNormalVector(Rotation rotation) {

        float magnitude = (float) Math.sqrt(rotation.getX() * rotation.getX() + rotation.getY() * rotation.getY() + rotation.getZ() * rotation.getZ() + rotation.getW() * rotation.getW());
        float x = rotation.getX() / magnitude;
        float y = rotation.getY() / magnitude;
        float z = rotation.getZ() / magnitude;

        return new Vector3f(x, y, z);
    }
}
