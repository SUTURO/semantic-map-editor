package com.malte3d.suturo.sme.ui;

import com.google.inject.Injector;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

/**
 * Static holder for the injector
 */
@UtilityClass
public class InjectorProvider {

    private static Injector injector;

    public static void setInjector(@NonNull Injector injector) {
        InjectorProvider.injector = injector;
    }

    public static Injector get() {
        return injector;
    }

}
