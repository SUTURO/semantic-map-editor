package com.malte3d.suturo.sme.domain.model.application.settings.appearance;

import com.malte3d.suturo.commons.ddd.annotation.ValueObject;
import com.malte3d.suturo.commons.messages.Language;
import lombok.Value;
import lombok.With;

@ValueObject

@With
@Value
public class AppearanceSettings {

    public static final AppearanceSettings DEFAULT = new AppearanceSettings(Language.ENGLISH);

    Language language;

}
