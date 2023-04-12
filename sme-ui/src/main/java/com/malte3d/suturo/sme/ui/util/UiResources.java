package com.malte3d.suturo.sme.ui.util;

import com.malte3d.suturo.sme.ui.MainApplication;
import javafx.scene.image.Image;
import lombok.experimental.UtilityClass;

import java.util.Objects;

@UtilityClass
public class UiResources {

    public static final Image APP_ICON = new Image(Objects.requireNonNull(MainApplication.class.getResourceAsStream("app-icon/semantic-map-editor-app-icon-128.png")));

}
