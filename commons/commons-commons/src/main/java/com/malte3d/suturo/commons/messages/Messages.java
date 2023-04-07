package com.malte3d.suturo.commons.messages;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

import com.malte3d.suturo.commons.i18n.I18N;
import lombok.NonNull;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility class, for internationalization.
 * <p>
 * Loads translations from the {@code messages.properties} matching a MessageKey
 * </p>
 */
@Slf4j
@UtilityClass
public final class Messages {

    private static ResourceBundle messages = ResourceBundle.getBundle("messages", Locale.getDefault());

    public static ResourceBundle getResourceBundle() {
        return messages;
    }

    /**
     * @param key       The {@code message.properties} key
     * @param arguments Potential {@code arguments} of a formatted string
     * @return The formatted string for the passed key of the {@code message.properties}
     */
    public static String getString(@NonNull String key, @NonNull Object... arguments) {

        if (!messages.containsKey(key)) {

            log.warn("The message key \"{}\" does not exist!", key);

            return "!" + key + "!";
        }

        return MessageFormat.format(messages.getString(key), arguments);
    }

    /**
     * @param value The {@link Enum} value
     * @return The string for the key of the {@link I18N} annotation value
     */
    public static String getString(@NonNull Enum<?> value) {

        String enumMessageKey = getEnumMessageKey(value);

        if (enumMessageKey == null || !messages.containsKey(enumMessageKey)) {

            log.warn("The message key \"{}\" does not exist!", enumMessageKey);

            return "!" + enumMessageKey + "!";
        }

        return messages.getString(enumMessageKey);
    }

    private static String getEnumMessageKey(Enum<?> value) {

        try {

            I18N annotation = value.getClass().getField(value.name()).getAnnotation(I18N.class);

            return annotation.value();

        } catch (NoSuchFieldException e) {
            throw new IllegalStateException("This exception should never occur! An enum always has a field with its name.", e);
        }
    }

    public static Language getLanguage() {
        return Language.of(messages.getLocale());
    }

    public static void setLanguage(@NonNull Language language) {
        messages = ResourceBundle.getBundle("messages", language.locale);
    }
}