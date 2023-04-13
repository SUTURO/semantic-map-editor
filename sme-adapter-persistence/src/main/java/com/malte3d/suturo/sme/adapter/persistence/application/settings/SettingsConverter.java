package com.malte3d.suturo.sme.adapter.persistence.application.settings;

import com.malte3d.suturo.commons.messages.Language;
import com.malte3d.suturo.sme.domain.model.application.settings.Settings;
import com.malte3d.suturo.sme.domain.model.application.settings.advanced.AdvancedSettings;
import com.malte3d.suturo.sme.domain.model.application.settings.advanced.DebugMode;
import com.malte3d.suturo.sme.domain.model.application.settings.appearance.AppearanceSettings;
import lombok.NonNull;
import lombok.experimental.UtilityClass;

import java.util.Locale;
import java.util.Properties;

@UtilityClass
public class SettingsConverter {

    /* Appearance */
    private static final String APPEARANCE_LANGUAGE = "appearance.language";

    /* Advanced */
    private static final String ADVANCED_DEBUG_MODE = "advanced.debugMode";

    public static Settings fromProperties(@NonNull Properties properties) {

        /* Appearance */

        final AppearanceSettings appeareanceDefault = AppearanceSettings.DEFAULT;
        Language language = Language.of(Locale.forLanguageTag(properties.getProperty(APPEARANCE_LANGUAGE, appeareanceDefault.language().locale.toLanguageTag())));
        AppearanceSettings appearanceSettings = new AppearanceSettings(language);

        /* Advanced */
        final AdvancedSettings advancedDefault = AdvancedSettings.DEFAULT;
        DebugMode debugMode = DebugMode.of(Boolean.parseBoolean(properties.getProperty(ADVANCED_DEBUG_MODE, String.valueOf(advancedDefault.debugMode().value))));
        AdvancedSettings advancedSettings = new AdvancedSettings(debugMode);

        return new Settings(appearanceSettings, advancedSettings);
    }

    public static Properties toProperties(@NonNull Settings settings) {

        Properties properties = new Properties();

        /* Appearance */
        properties.setProperty(APPEARANCE_LANGUAGE, settings.appearanceSettings().language().locale.toLanguageTag());

        /* Advanced */
        properties.setProperty(ADVANCED_DEBUG_MODE, String.valueOf(settings.advancedSettings().debugMode().value));

        return properties;
    }

}
