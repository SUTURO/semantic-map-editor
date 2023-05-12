package com.malte3d.suturo.sme.ui.viewmodel.editor;

import com.jme3.math.Vector3f;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;

class EditorUtilTest {

    private static final float EPSILON = 0.05f;


    @ParameterizedTest
    @CsvSource({
            "#000000, 0.00, 0.00, 0.00",
            "#FFFFFF, 1.00, 1.00, 1.00",
            "#3FB25C, 0.25, 0.70, 0.36",
    })
    void createsVectorFromHexColor(String hexColor, float r, float g, float b) {

        /* Given / When */
        Vector3f actualColor = EditorUtil.hexToVec3(hexColor);

        /* Then */
        Vector3f expectedColor = new Vector3f(r, g, b);

        assertThat(actualColor.distance(expectedColor)).isCloseTo(0, Assertions.within(EPSILON));
    }

    @ParameterizedTest
    @CsvSource({
            "#000, 0.00, 0.00, 0.00",
            "#FFF, 1.00, 1.00, 1.00",
            "#3BF, 0.20, 0.73, 1.00",
    })
    void createsVectorFromShortHexColor(String shortHexColor, float r, float g, float b) {

        /* Given / When */
        Vector3f actualColor = EditorUtil.hexToVec3(shortHexColor);

        /* Then */
        Vector3f expectedColor = new Vector3f(r, g, b);

        assertThat(actualColor.distance(expectedColor)).isCloseTo(0, Assertions.within(EPSILON));
    }

}