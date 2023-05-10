package com.malte3d.suturo.sme.domain.model.application.settings.keymap.editor;

import com.malte3d.suturo.commons.ddd.annotation.ValueObject;
import com.malte3d.suturo.commons.exception.UnsupportedEnumException;
import com.malte3d.suturo.commons.i18n.I18N;
import com.malte3d.suturo.commons.messages.Messages;
import lombok.RequiredArgsConstructor;

@ValueObject

@RequiredArgsConstructor
public enum CameraBehaviour {

    @I18N("Application.Settings.Keymap.CameraBehaviour.Blender")
    BLENDER(1),

    @I18N("Application.Settings.Keymap.CameraBehaviour.Cinema4D")
    CINEMA_4D(2);

    public final int eternalId;

    public static CameraBehaviour of(int eternalId) {

        for (CameraBehaviour elem : values())
            if (elem.eternalId == eternalId)
                return elem;

        throw new UnsupportedEnumException(Messages.format("CameraBehaviour for {} does not exist!", eternalId));
    }

}
