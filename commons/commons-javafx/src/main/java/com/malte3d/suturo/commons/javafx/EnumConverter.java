package com.malte3d.suturo.commons.javafx;

import com.malte3d.suturo.commons.messages.Messages;
import javafx.util.StringConverter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class EnumConverter<T extends Enum<T>> extends StringConverter<T> {

    @Override
    public String toString(@NonNull T enumValue) {
        return Messages.getString(enumValue);
    }

    @Override
    public T fromString(@NonNull String value) {
        throw new IllegalStateException("This function should never be called!");
    }
}
