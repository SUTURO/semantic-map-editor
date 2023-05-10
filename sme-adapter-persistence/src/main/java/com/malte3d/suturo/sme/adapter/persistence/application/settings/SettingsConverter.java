package com.malte3d.suturo.sme.adapter.persistence.application.settings;

import com.malte3d.suturo.commons.messages.Language;
import com.malte3d.suturo.sme.domain.model.application.settings.Settings;
import com.malte3d.suturo.sme.domain.model.application.settings.advanced.AdvancedSettings;
import com.malte3d.suturo.sme.domain.model.application.settings.advanced.DebugMode;
import com.malte3d.suturo.sme.domain.model.application.settings.appearance.AppearanceSettings;
import com.malte3d.suturo.sme.domain.model.application.settings.keymap.KeymapSettings;
import com.malte3d.suturo.sme.domain.model.application.settings.keymap.editor.CameraBehaviour;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.util.Locale;
import java.util.Optional;
import java.util.Properties;

@UtilityClass
public class SettingsConverter {

    /* Appearance */
    private static final String APPEARANCE_LANGUAGE = "appearance.language";

    /* Keymap */
    private static final String KEYMAP_EDITOR_CAMERA_BEHAVIOUR = "keymap.editor.cameraBehaviour";

    /* Advanced */
    private static final String ADVANCED_DEBUG_MODE = "advanced.debugMode";

    public static Settings fromProperties(@NonNull Properties properties) {

        /* Appearance */

        final AppearanceSettings appeareanceDefault = AppearanceSettings.DEFAULT;
        Language language = Language.of(Locale.forLanguageTag(getOrDefault(properties, APPEARANCE_LANGUAGE, appeareanceDefault.language().locale.toLanguageTag())));
        AppearanceSettings appearanceSettings = new AppearanceSettings(language);

        /* Keymap */

        final KeymapSettings keymapSettingsDefault = KeymapSettings.DEFAULT;
        CameraBehaviour cameraBehaviour = CameraBehaviour.of(getOrDefault(properties, KEYMAP_EDITOR_CAMERA_BEHAVIOUR, keymapSettingsDefault.cameraBehaviour().eternalId));
        ;
        KeymapSettings keymapSettings = new KeymapSettings(cameraBehaviour);

        /* Advanced */

        final AdvancedSettings advancedDefault = AdvancedSettings.DEFAULT;
        DebugMode debugMode = DebugMode.of(getOrDefault(properties, ADVANCED_DEBUG_MODE, advancedDefault.debugMode().value));
        AdvancedSettings advancedSettings = new AdvancedSettings(debugMode);

        return new Settings(appearanceSettings, keymapSettings, advancedSettings);
    }

    public static Properties toProperties(@NonNull Settings settings) {

        Properties properties = new Properties();

        /* Appearance */
        properties.setProperty(APPEARANCE_LANGUAGE, settings.appearanceSettings().language().locale.toLanguageTag());

        /* Keymap */
        properties.setProperty(KEYMAP_EDITOR_CAMERA_BEHAVIOUR, String.valueOf(settings.keymapSettings().cameraBehaviour().eternalId));

        /* Advanced */
        properties.setProperty(ADVANCED_DEBUG_MODE, String.valueOf(settings.advancedSettings().debugMode().value));

        return properties;
    }

    private static int getOrDefault(Properties properties, String key, int defaultValue) {

        return Optional.ofNullable(properties.getProperty(key))
                .map(Integer::parseInt)
                .orElse(defaultValue);
    }

    private static double getOrDefault(Properties properties, String key, double defaultValue) {

        return Optional.ofNullable(properties.getProperty(key))
                .map(Double::parseDouble)
                .orElse(defaultValue);
    }

    private static boolean getOrDefault(Properties properties, String key, boolean defaultValue) {

        return Optional.ofNullable(properties.getProperty(key))
                .map(Boolean::parseBoolean)
                .orElse(defaultValue);
    }

    private static String getOrDefault(Properties properties, String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

}
