package com.malte3d.suturo.commons.javafx;

import com.google.common.base.Preconditions;
import lombok.NonNull;

public class Color {

    private static final String HEX_COLOR_PATTERN = "#([A-Fa-f0-9]{6}|[A-Fa-f0-9]{3})";

    /**
     * Checks if the given hex color string is valid.
     *
     * @param hexColor Hex color string
     * @return true if the hex color string is valid (has format #RRGGBB or #RGB)
     */
    public static boolean isValidHexColor(@NonNull String hexColor) {
        return hexColor.matches(HEX_COLOR_PATTERN);
    }

    /**
     * @param hexColor Hex color string in the format #RRGGBB or #RGB
     * @return Hex color string in the format RRGGBB
     */
    public static String getHexColorValue(@NonNull String hexColor) {

        Preconditions.checkArgument(Color.isValidHexColor(hexColor), "Invalid hex color: %s", hexColor);

        if (hexColor.length() == 7)
            return hexColor.substring(1);

        return String.valueOf(hexColor.charAt(1)) +
                hexColor.charAt(1) +
                hexColor.charAt(2) +
                hexColor.charAt(2) +
                hexColor.charAt(3) +
                hexColor.charAt(3);
    }
}
