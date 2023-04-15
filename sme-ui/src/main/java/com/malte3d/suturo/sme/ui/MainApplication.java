package com.malte3d.suturo.sme.ui;

import com.google.common.base.Preconditions;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.malte3d.suturo.commons.javafx.CompletableFutureTask;
import com.malte3d.suturo.commons.javafx.FxmlLoaderUtil;
import com.malte3d.suturo.commons.javafx.GlobalExecutor;
import com.malte3d.suturo.commons.messages.Messages;
import com.malte3d.suturo.sme.domain.model.application.settings.advanced.DebugMode;
import com.malte3d.suturo.sme.domain.service.application.settings.SettingsRepository;
import com.malte3d.suturo.sme.ui.util.UiResources;
import com.malte3d.suturo.sme.ui.view.MainView;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import lombok.NonNull;
import org.scenicview.ScenicView;

import java.util.concurrent.Executor;

public class MainApplication extends Application {

    private static Injector injector;

    private final MainViewFactory viewFactory;
    private final SettingsRepository settingsRepository;

    private CompletableFutureTask<Parent> mainViewFuture;

    public MainApplication() {

        Preconditions.checkNotNull(injector, "MainApplication has to be launched with the Injector initialized!");

        Executor globalExecutor = injector.getInstance(Key.get(Executor.class, GlobalExecutor.class));

        this.viewFactory = new MainViewFactory(globalExecutor);
        this.settingsRepository = injector.getInstance(SettingsRepository.class);
    }

    @Override
    public void init() {

        FxmlLoaderUtil.init(injector);
        mainViewFuture = viewFactory.loadView(MainView.class);
    }

    @Override
    public void start(Stage stage) {

        DebugMode debugMode = settingsRepository.load().advancedSettings().debugMode();

        stage.setTitle(Messages.getString("Application.Name"));
        stage.getIcons().add(UiResources.APP_ICON);
        stage.setOnCloseRequest(windowEvent -> exit());

        mainViewFuture.thenConsume(mainView -> {

            Scene scene = new Scene(mainView);
            scene.setRoot(mainView);

            if (debugMode.isEnabled())
                ScenicView.show(mainView);

            stage.setScene(scene);
            stage.show();
        });
    }

    public static void exit() {
        Platform.exit();
        System.exit(0);
    }

    public static void launch(@NonNull MainApplicationOptions options) {
        MainApplication.injector = options.getInjector();
        launch(options.getArgs());
    }

}
