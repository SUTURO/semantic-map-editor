package com.malte3d.suturo.commons.javafx;

import java.io.IOException;
import java.net.URL;

import com.google.inject.Injector;
import com.malte3d.suturo.commons.messages.Messages;
import javafx.fxml.FXMLLoader;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UtilityClass
public final class FXMLUtil {

    private static Injector injector;

    public static void init(@NonNull Injector injector) {
        FXMLUtil.injector = injector;
    }

    @NonNull
    public static <T> T load(@NonNull URL location) {

        validateInit();

        try {

            FXMLLoader fxmlLoader = injector.getInstance(FXMLLoader.class);
            fxmlLoader.setLocation(location);
            fxmlLoader.setResources(Messages.getResourceBundle());

            return fxmlLoader.load();

        } catch (IOException e) {
            log.error("Error loading the .fxml {}", location, e);
            throw new IllegalStateException("Error loading the FXML", e);
        }
    }

    @NonNull
    public static <T> T load(@NonNull Object controller, @NonNull URL location) {

        validateInit();

        try {

            FXMLLoader fxmlLoader = injector.getInstance(FXMLLoader.class);
            fxmlLoader.setLocation(location);
            fxmlLoader.setResources(Messages.getResourceBundle());
            fxmlLoader.setRoot(controller);
            fxmlLoader.setController(controller);

            return fxmlLoader.load();

        } catch (IOException e) {
            log.error("Error loading the .fxml {} for {} ", location, controller.getClass().getSimpleName(), e);
            throw new IllegalStateException("Error loading the FXML", e);
        }
    }

    private static void validateInit() {

        if (injector == null)
            throw new IllegalStateException("Injector has to be initialized with init() before calling this method!");
    }
}
