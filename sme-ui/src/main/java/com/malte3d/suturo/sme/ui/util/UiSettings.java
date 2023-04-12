package com.malte3d.suturo.sme.ui.util;

import jfxtras.styles.jmetro.Style;
import lombok.experimental.UtilityClass;

@UtilityClass
public class UiSettings {

    private static final Style SCENE_STYLE = Style.LIGHT;

    private static final int SCENE_WIDTH = 1280;
    private static final int SCENE_HEIGHT = 720;

    public static Style getStyle() {
        return SCENE_STYLE;
    }

    public static int getSceneWidth() {
        return SCENE_WIDTH;
    }

    public static int getSceneHeight() {
        return SCENE_HEIGHT;
    }
}
