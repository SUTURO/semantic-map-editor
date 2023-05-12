package com.malte3d.suturo.sme.domain.model.application.settings.advanced;

import com.malte3d.suturo.commons.ddd.annotation.ValueObject;
import lombok.Value;
import lombok.With;

@ValueObject

@With
@Value
public class AdvancedSettings {

    public static final AdvancedSettings DEFAULT = new AdvancedSettings(DebugMode.OFF);

    DebugMode debugMode;

}
