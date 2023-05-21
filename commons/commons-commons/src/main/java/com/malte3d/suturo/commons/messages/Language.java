package com.malte3d.suturo.commons.messages;

import com.malte3d.suturo.commons.exception.UnsupportedEnumException;
import com.malte3d.suturo.commons.i18n.I18N;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.text.MessageFormat;
import java.util.Locale;

@RequiredArgsConstructor
public enum Language {

    /* TODO: Provide german translations */
    //@I18N("Commons.Messages.Language.GERMAN")
    //GERMAN(Locale.GERMAN),

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
