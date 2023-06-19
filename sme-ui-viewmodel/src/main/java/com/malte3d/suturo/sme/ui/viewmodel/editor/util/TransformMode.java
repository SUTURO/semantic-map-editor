package com.malte3d.suturo.sme.ui.viewmodel.editor.util;

import com.malte3d.suturo.commons.ddd.annotation.ValueObject;
import com.malte3d.suturo.commons.exception.UnsupportedEnumException;
import com.malte3d.suturo.commons.messages.Messages;
import lombok.RequiredArgsConstructor;

@ValueObject

@RequiredArgsConstructor
public enum TransformMode {

    MOVE(1),
    ROTATE(2),
    SCALE(3);

    public final int eternalId;

    public String getEternalName() {
        return String.valueOf(eternalId);
    }

    public static TransformMode of(int eternalId) {

        for (TransformMode elem : values())
            if (elem.eternalId == eternalId)
                return elem;

        throw new UnsupportedEnumException(Messages.format("{} for {} does not exist!", TransformMode.class.getSimpleName(), eternalId));
    }
}
