package com.malte3d.suturo.sme.ui;

import com.google.inject.Injector;
import com.google.inject.Provider;
import com.jme3.util.LWJGLBufferAllocator;
import com.malte3d.suturo.commons.ddd.event.domain.DomainEventHandler;
import com.malte3d.suturo.commons.javafx.notification.NotificationHandler;
import com.malte3d.suturo.commons.messages.Messages;
import com.malte3d.suturo.sme.ui.view.MainView;
import com.malte3d.suturo.sme.ui.view.icons.Icons;
import com.malte3d.suturo.sme.ui.viewmodel.ExitApplicationEvent;
import javafx.application.Application;
import javafx.application.HostServices;
import javafx.application.Platform;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import lombok.extern.slf4j.Slf4j;
import org.lwjgl.system.Configuration;

import java.util.Objects;

/**
 * Entry point for the application
 *
 * <p>
 * This class is responsible for initializing the application and setting up the stage with the main view.
 * </p>
 */
@Slf4j
public class MainApplication extends Application implements Provider<HostServices> {

    private final DomainEventHandler domainEventHandler;

    private final MainApplicationViewFactory viewFactory;

    /**
     * Creates a new instance of the application.
     *
     * <p>Use the static factory {@link #launch(String...)} to create and launch the application.</p>
     */
    public MainApplication() {

        log.info("Starting application");

        setupJfxLwjglEnvironment();

        Injector injector = InjectorProvider.get();
        this.domainEventHandler = injector.getInstance(DomainEventHandler.class);
        this.viewFactory = injector.getInstance(MainApplicationViewFactory.class);

        registerEventConsumer();
    }

    private void registerEventConsumer() {
        domainEventHandler.register(ExitApplicationEvent.class, MainApplication::exit);
    }

    @Override
    public void start(Stage stage) {

        NotificationHandler.stage = stage;


        stage.setTitle(Messages.getString("Application.Name"));
        stage.getIcons().add(Icons.APP_ICON);
        stage.setOnCloseRequest(windowEvent -> exit());

        Parent mainView = viewFactory.loadView(MainView.class);
        Scene scene = new Scene(mainView, 1200, 800);
        scene.setRoot(mainView);

        JMetro jMetro = new JMetro(scene, Style.LIGHT);
        jMetro.getOverridingStylesheets().add(Objects.requireNonNull(MainView.class.getResource("sme-base.css")).toExternalForm());

        stage.setScene(scene);
        stage.show();

        stage.toFront();
    }

    /**
     * Exit the application gracefully.
     */
    public static void exit() {

        log.info("Exiting application");

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
