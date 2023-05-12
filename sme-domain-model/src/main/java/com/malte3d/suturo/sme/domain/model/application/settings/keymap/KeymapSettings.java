package com.malte3d.suturo.sme.domain.model.application.settings.keymap;

import com.malte3d.suturo.commons.ddd.annotation.ValueObject;
import com.malte3d.suturo.sme.domain.model.application.settings.keymap.editor.CameraBehaviour;
import lombok.Value;
import lombok.With;

@ValueObject

@With
@Value
public class KeymapSettings {

    public static final KeymapSettings DEFAULT = new KeymapSettings(CameraBehaviour.CINEMA_4D);

    CameraBehaviour cameraBehaviour;

}
