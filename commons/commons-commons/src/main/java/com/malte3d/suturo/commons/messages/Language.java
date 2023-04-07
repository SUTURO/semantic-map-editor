package com.malte3d.suturo.commons.messages;

import java.text.MessageFormat;
import java.util.Locale;

import com.malte3d.suturo.commons.exception.UnsupportedEnumException;
import com.malte3d.suturo.commons.i18n.I18N;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Language {

    @I18N("Commons.Messages.Language.GERMAN")
    GERMAN(Locale.GERMAN),

    @I18N("Commons.Messages.Language.ENGLISH")
    ENGLISH(Locale.ENGLISH);

    public final Locale locale;

    public static Language of(@NonNull Locale locale) {

        for (Language language : values()) {

            if (language.locale.equals(locale))
                return language;
        }

        throw new UnsupportedEnumException(MessageFormat.format("Language for {0} does not exist", locale));
    }

}
