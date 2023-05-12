package com.malte3d.suturo.sme.ui;

import com.google.common.base.Preconditions;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.jme3.util.LWJGLBufferAllocator;
import com.malte3d.suturo.commons.ddd.event.domain.DomainEventPublisher;
import com.malte3d.suturo.commons.javafx.fxml.FxmlLoaderUtil;
import com.malte3d.suturo.commons.messages.Messages;
import com.malte3d.suturo.sme.application.service.settings.SettingsService;
import com.malte3d.suturo.sme.domain.model.application.settings.advanced.DebugMode;
import com.malte3d.suturo.sme.ui.util.UiResources;
import com.malte3d.suturo.sme.ui.view.MainView;
import com.malte3d.suturo.sme.ui.viewmodel.main.ExitApplicationEvent;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import lombok.NonNull;
import org.lwjgl.system.Configuration;
import org.scenicview.ScenicView;

import java.util.Objects;

/**
 * The main application.
 *
 * <p>
 * Entry point for the application. This class is responsible for initializing the application and setting up the main view.
 * </p>
 */
public class MainApplication extends Application implements Provider<HostServices> {

    private static Injector injector;

    private final DomainEventPublisher domainEventPublisher;

    private final MainApplicationViewFactory viewFactory;

    private final SettingsService settingsService;

    /**
     * Launches the application.
     *
     * <p>Use the static factory {@link #launch(MainApplicationOptions)} to create and launch the application.</p>
     */
    public MainApplication() {

        Preconditions.checkNotNull(injector, "MainApplication has to be launched with the Injector initialized!");

        this.domainEventPublisher = injector.getInstance(DomainEventPublisher.class);

        this.viewFactory = new MainApplicationViewFactory(injector);
        this.settingsService = injector.getInstance(SettingsService.class);

        FxmlLoaderUtil.init(injector);

        registerEventConsumer();
    }

    private void registerEventConsumer() {
        domainEventPublisher.register(ExitApplicationEvent.class, MainApplication::exit);
    }

    @Override
    public void start(Stage stage) {

        DebugMode debugMode = settingsService.get().getAdvanced().getDebugMode();

        stage.setTitle(Messages.getString("Application.Name"));
        stage.getIcons().add(UiResources.APP_ICON);
        stage.setOnCloseRequest(windowEvent -> exit());

        Parent mainView = viewFactory.loadView(MainView.class);
        Scene scene = new Scene(mainView, 1200, 800);
        scene.setRoot(mainView);

        JMetro jMetro = new JMetro(scene, Style.LIGHT);
        jMetro.getOverridingStylesheets().add(Objects.requireNonNull(MainView.class.getResource("sme-base.css")).toExternalForm());

        stage.setScene(scene);
        stage.show();

        if (debugMode.isEnabled())
            ScenicView.show(mainView);

        stage.toFront();
    }

    /**
     * Exit the application gracefully.
     */
    public static void exit() {
        Platform.exit();
        System.exit(0);
    }

    /**
     * Launch the application with the given options.
     *
     * @param options the options to launch the application with
     */
    public static void launch(@NonNull MainApplicationOptions options) {

        MainApplication.injector = options.getInjector();

        setupJfxLwjglEnvironment();

        launch(options.getArgs());
    }

    /**
     * Setup some general settings for JavaFX and LWJGL for maximum compatibility
     */
    private static void setupJfxLwjglEnvironment() {

        Configuration.GLFW_CHECK_THREAD0.set(false); // need to disable to work on macOS
        Configuration.MEMORY_ALLOCATOR.set("jemalloc"); // use jemalloc
        System.setProperty("prism.lcdtext", "false"); // force antialiasing in JavaFX fonts
        System.setProperty(LWJGLBufferAllocator.PROPERTY_CONCURRENT_BUFFER_ALLOCATOR, "true");
    }

    @Override
    public HostServices get() {
        return getHostServices();
    }
}
