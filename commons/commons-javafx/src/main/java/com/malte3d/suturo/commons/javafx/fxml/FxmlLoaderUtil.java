package com.malte3d.suturo.commons.javafx.fxml;

import com.google.common.base.Preconditions;
import com.google.inject.Injector;
import com.malte3d.suturo.commons.messages.Messages;
import javafx.fxml.FXMLLoader;
import javafx.util.Callback;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.regex.Pattern;

@Slf4j
@UtilityClass
public final class FxmlLoaderUtil {

    private static final String FXML_FILE_EXTENSION = ".fxml";
    private static final Pattern DASH_CASE = Pattern.compile("([a-z\\d])([A-Z])");

    private static Injector injector;

    public static void init(@NonNull Injector injector) {
        FxmlLoaderUtil.injector = injector;
    }

    @NonNull
    public static <T> T load(@NonNull URL fxml) {

        Preconditions.checkState(injector != null, "Injector has to be initialized with init() before calling this method!");

        try {

            FXMLLoader fxmlLoader = injector.getInstance(FXMLLoader.class);
            fxmlLoader.setLocation(fxml);
            fxmlLoader.setResources(Messages.getResourceBundle());

            return fxmlLoader.load();

        } catch (IOException e) {
            log.error("Error loading the .fxml {}", fxml, e);
            throw new IllegalStateException("Error loading the FXML", e);
        }
    }

    @NonNull
    public static <T> T load(@NonNull Callback<Class<?>, Object> viewFactory, @NonNull URL fxml) {

        Preconditions.checkState(injector != null, "Injector has to be initialized with init() before calling this method!");

        try {

            FXMLLoader fxmlLoader = injector.getInstance(FXMLLoader.class);
            fxmlLoader.setLocation(fxml);
            fxmlLoader.setResources(Messages.getResourceBundle());
            fxmlLoader.setControllerFactory(viewFactory);

            return fxmlLoader.load();

        } catch (IOException e) {
            log.error("Error loading the .fxml {} for {}", fxml, viewFactory.getClass().getSimpleName(), e);
            throw new IllegalStateException("Error loading the FXML", e);
        }
    }

    public static <T> T load(@NonNull Callback<Class<?>, Object> viewFactory, @NonNull Class<?> viewClass) {
        return load(viewFactory, resolveFxml(viewClass));
    }

    private static URL resolveFxml(Class<?> viewClass) {

        String fxmlViewName = getFxmlViewName(getViewClassName(viewClass));

        URL fxml = viewClass.getResource(fxmlViewName + FXML_FILE_EXTENSION);

        if (fxml == null)
            throw new IllegalStateException(Messages.format("Failed to resolve FXML file for {}", viewClass));

        return fxml;
    }

    private static String getViewClassName(Class<?> viewClass) {
        String[] split = viewClass.getSimpleName().split("\\$");
        return split[split.length == 1 ? 0 : split.length - 1];
    }

    private static String getFxmlViewName(String viewClassName) {
        return DASH_CASE.matcher(viewClassName).replaceAll("$1-$2").toLowerCase(Locale.ENGLISH);
    }
}
