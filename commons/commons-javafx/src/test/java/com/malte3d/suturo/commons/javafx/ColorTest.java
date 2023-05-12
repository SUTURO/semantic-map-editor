package com.malte3d.suturo.commons.javafx;

import com.malte3d.suturo.commons.javafx.fxml.Color;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;

class ColorTest {

    @ParameterizedTest
    @CsvSource({
            "#000000",
            "#FFFFFF",
            "#3FB25C",
            "#3FB",
    })
    void isValidHexColor(String hexColor) {
        assertTrue(Color.isValidHexColor(hexColor));
    }

    @ParameterizedTest
    @CsvSource({
            "#0000000",
            "#FFFFF",
            "#3ÄB25C",
            "#3B",
    })
    void isInvalidHexColor(String hexColor) {
        assertFalse(Color.isValidHexColor(hexColor));
    }

    @ParameterizedTest
    @CsvSource({
            "#000000, 000000",
            "#FFFFFF, FFFFFF",
            "#3FB25C, 3FB25C",
            "#3FB, 33FFBB",
    })
    void createsValidHexColorValue(String hexColor, String expectedHexValue) {
        assertEquals(expectedHexValue, Color.getHexColorValue(hexColor));
    }
}