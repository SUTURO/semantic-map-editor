package com.malte3d.suturo.sme.domain.model.application.settings;

import com.malte3d.suturo.commons.ddd.annotation.AggregateRoot;
import com.malte3d.suturo.sme.domain.model.application.settings.advanced.AdvancedSettings;
import com.malte3d.suturo.sme.domain.model.application.settings.appearance.AppearanceSettings;
import com.malte3d.suturo.sme.domain.model.application.settings.keymap.KeymapSettings;
import lombok.Value;
import lombok.With;

@AggregateRoot

@With
@Value
public class Settings {

    public static final Settings DEFAULT = new Settings(
            AppearanceSettings.DEFAULT,
            KeymapSettings.DEFAULT,
            AdvancedSettings.DEFAULT
    );

    AppearanceSettings appearanceSettings;
    KeymapSettings keymapSettings;
    AdvancedSettings advancedSettings;

}
