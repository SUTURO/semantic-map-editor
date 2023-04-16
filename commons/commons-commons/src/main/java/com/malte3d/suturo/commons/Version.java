package com.malte3d.suturo.commons;

import lombok.NonNull;
import lombok.experimental.UtilityClass;

@UtilityClass
public class Version {

    public static String getVersion(@NonNull Class<?> application) {
        return application.getPackage().getImplementationVersion();
    }

}
