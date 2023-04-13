package com.malte3d.suturo.sme.domain.model.application.settings.advanced;

import com.malte3d.suturo.commons.ddd.annotation.ValueObject;
import lombok.With;

@ValueObject

@With
public record AdvancedSettings(DebugMode debugMode) {

    public static final AdvancedSettings DEFAULT = new AdvancedSettings(DebugMode.OFF);

}
