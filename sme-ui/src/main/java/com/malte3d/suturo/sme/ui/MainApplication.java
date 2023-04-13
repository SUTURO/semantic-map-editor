package com.malte3d.suturo.sme.ui;

import com.google.common.base.Preconditions;
import com.google.inject.Injector;
import com.malte3d.suturo.commons.javafx.FxmlLoaderUtil;
import com.malte3d.suturo.commons.messages.Messages;
import com.malte3d.suturo.sme.domain.model.application.settings.advanced.DebugMode;
import com.malte3d.suturo.sme.domain.service.application.settings.SettingsRepository;
import com.malte3d.suturo.sme.ui.util.UiResources;
import com.malte3d.suturo.sme.ui.util.UiSettings;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.scenicview.ScenicView;

import java.util.Objects;

@NoArgsConstructor
public class MainApplication extends Application {

    private static Injector injector;

    @Override
    public void init() {
        Preconditions.checkNotNull(injector, "MainApplication has to be launched with the Injector initialized!");
    }

    @Override
    public void start(Stage stage) {

        Scene scene = new Scene(
                FxmlLoaderUtil.load(Objects.requireNonNull(MainApplication.class.getResource("semantic-map-editor.fxml"))),
                UiSettings.getSceneWidth(),
                UiSettings.getSceneHeight()
        );

        SettingsRepository settingsRepository = injector.getInstance(SettingsRepository.class);
        DebugMode debugMode = settingsRepository.load().advancedSettings().debugMode();

        if (debugMode.isEnabled())
            ScenicView.show(scene);
        
        JMetro jMetro = new JMetro(scene, UiSettings.getStyle());
        jMetro.getOverridingStylesheets().add(Objects.requireNonNull(MainApplication.class.getResource("semantic-map-editor.css")).toExternalForm());

        stage.setTitle(Messages.getString("Application.Name"));
        stage.getIcons().add(UiResources.APP_ICON);
        stage.setOnCloseRequest(windowEvent -> exit());

        stage.setScene(scene);
        stage.show();
    }

    public static void exit() {
        Platform.exit();
        System.exit(0);
    }

    public static void launch(@NonNull Injector injector, String[] args) {

        MainApplication.injector = injector;

        FxmlLoaderUtil.init(injector);

        launch(args);
    }

}
