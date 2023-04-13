package com.malte3d.suturo.sme.domain.model.application.settings.appearance;

import com.malte3d.suturo.commons.ddd.annotation.ValueObject;
import com.malte3d.suturo.commons.messages.Language;
import lombok.With;

@ValueObject

@With
public record AppearanceSettings(Language language) {

    public static final AppearanceSettings DEFAULT = new AppearanceSettings(Language.ENGLISH);

}
