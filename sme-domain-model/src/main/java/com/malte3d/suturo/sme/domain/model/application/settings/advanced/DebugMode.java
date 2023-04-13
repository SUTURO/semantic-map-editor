package com.malte3d.suturo.sme.domain.model.application.settings.advanced;

import com.malte3d.suturo.commons.ddd.annotation.ValueObject;
import com.malte3d.suturo.commons.exception.UnsupportedEnumException;
import com.malte3d.suturo.commons.messages.Messages;
import lombok.RequiredArgsConstructor;

@ValueObject

@RequiredArgsConstructor
public enum DebugMode {

    ON(true),
    OFF(false);

    public final boolean value;

    public boolean isEnabled() {
        return value;
    }

    public static DebugMode of(boolean value) {

        for (DebugMode elem : values())
            if (elem.value == value)
                return elem;

        throw new UnsupportedEnumException(Messages.format("DebugMode for {} does not exist!", value));
    }

}
