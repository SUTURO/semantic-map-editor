package com.malte3d.suturo.sme.ui;

import com.google.common.base.Preconditions;
import com.google.inject.Provider;
import com.jme3.util.LWJGLBufferAllocator;
import com.malte3d.suturo.commons.ddd.event.domain.DomainEventPublisher;
import com.malte3d.suturo.commons.javafx.notification.NotificationHandler;
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

    private final DomainEventPublisher domainEventPublisher;

    private final MainApplicationViewFactory viewFactory;

    private final SettingsService settingsService;

    /**
     * Creates a new instance of the application.
     *
     * <p>Use the static factory {@link #launch(String...)} to create and launch the application.</p>
     */
    public MainApplication() {

        Preconditions.checkNotNull(InjectorContext.injector, "MainApplication has to be launched with the Injector initialized!");

        setupJfxLwjglEnvironment();

        this.domainEventPublisher = InjectorContext.injector.getInstance(DomainEventPublisher.class);
        this.viewFactory = InjectorContext.injector.getInstance(MainApplicationViewFactory.class);
        this.settingsService = InjectorContext.injector.getInstance(SettingsService.class);

        registerEventConsumer();
    }

    private void registerEventConsumer() {
        domainEventPublisher.register(ExitApplicationEvent.class, MainApplication::exit);
    }

    @Override
    public void start(Stage stage) {

        NotificationHandler.stage = stage;

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
