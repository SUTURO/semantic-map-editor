package com.malte3d.suturo.sme.domain.model.application.settings;

import com.malte3d.suturo.commons.ddd.annotation.AggregateRoot;
import com.malte3d.suturo.sme.domain.model.application.settings.advanced.AdvancedSettings;
import com.malte3d.suturo.sme.domain.model.application.settings.appearance.AppearanceSettings;
import lombok.With;

@AggregateRoot

@With
public record Settings(AppearanceSettings appearanceSettings,
                       AdvancedSettings advancedSettings) {

    public static final Settings DEFAULT = new Settings(
            AppearanceSettings.DEFAULT,
            AdvancedSettings.DEFAULT
    );

}
