package com.malte3d.suturo.sme.ui;

import com.malte3d.suturo.commons.javafx.FxmlLoaderUtil;
import com.malte3d.suturo.commons.messages.Messages;
import com.malte3d.suturo.sme.ui.util.UiResources;
import com.malte3d.suturo.sme.ui.util.UiSettings;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import lombok.NonNull;
import org.scenicview.ScenicView;

import java.util.Objects;

public class MainApplication extends Application {

    @Override
    public void start(Stage stage) {

        Scene scene = new Scene(
                FxmlLoaderUtil.load(Objects.requireNonNull(MainApplication.class.getResource("semantic-map-editor.fxml"))),
                UiSettings.getSceneWidth(),
                UiSettings.getSceneHeight()
        );

        stage.setTitle(Messages.getString("Application.Name"));
        stage.getIcons().add(UiResources.APP_ICON);
        stage.setOnCloseRequest(windowEvent -> exit());

        JMetro jMetro = new JMetro(scene, UiSettings.getStyle());
        jMetro.getOverridingStylesheets().add(Objects.requireNonNull(MainApplication.class.getResource("semantic-map-editor.css")).toExternalForm());

        ScenicView.show(scene);

        stage.setScene(scene);
        stage.show();
    }

    public static void exit() {
        Platform.exit();
        System.exit(0);
    }

    public void run(@NonNull String... args) {
        launch(args);
    }
}
