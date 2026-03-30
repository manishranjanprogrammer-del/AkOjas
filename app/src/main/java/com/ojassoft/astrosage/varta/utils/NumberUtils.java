package com.ojassoft.astrosage.varta.utils;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

/**
 * Utility class for handling number conversions, especially for different numeral systems
 */
public class NumberUtils {

    private static final Map<Character, Character> devanagariToAsciiMap = new HashMap<>();

    static {
        // Initialize the mapping from Devanagari numerals to ASCII numerals
        devanagariToAsciiMap.put('०', '0');
        devanagariToAsciiMap.put('१', '1');
        devanagariToAsciiMap.put('२', '2');
        devanagariToAsciiMap.put('३', '3');
        devanagariToAsciiMap.put('४', '4');
        devanagariToAsciiMap.put('५', '5');
        devanagariToAsciiMap.put('६', '6');
        devanagariToAsciiMap.put('७', '7');
        devanagariToAsciiMap.put('८', '8');
        devanagariToAsciiMap.put('९', '9');
    }

    /**
     * Safely parses a string to float, handling Devanagari numerals
     * @param value The string value to parse, which may contain Devanagari numerals
     * @return The parsed float value
     * @throws NumberFormatException if the string cannot be parsed to a float
     */
    public static float safeParseFloat(String value) {
        if (value == null || value.isEmpty()) {
            return 0.0f;
        }
        
        // Convert Devanagari numerals to ASCII numerals
        String asciiValue = convertToAsciiNumerals(value);
        
        try {
            return Float.parseFloat(asciiValue);
        } catch (NumberFormatException e) {
            // If parsing still fails, return 0 as a fallback
            return 0.0f;
        }
    }

    /**
     * Safely parses a string to double, handling Devanagari numerals
     * @param value The string value to parse, which may contain Devanagari numerals
     * @return The parsed double value
     * @throws NumberFormatException if the string cannot be parsed to a double
     */
    public static double safeParseDouble(String value) {
        if (value == null || value.isEmpty()) {
            return 0.0;
        }
        
        // Convert Devanagari numerals to ASCII numerals
        String asciiValue = convertToAsciiNumerals(value);
        
        try {
            return Double.parseDouble(asciiValue);
        } catch (NumberFormatException e) {
            // If parsing still fails, return 0 as a fallback
            return 0.0;
        }
    }

    /**
     * Converts a string that may contain Devanagari numerals to a string with ASCII numerals
     * @param value The string that may contain Devanagari numerals
     * @return A string with all Devanagari numerals converted to ASCII numerals
     */
    public static String convertToAsciiNumerals(String value) {
        if (value == null || value.isEmpty()) {
            return value;
        }
        
        StringBuilder result = new StringBuilder(value.length());
        
        for (int i = 0; i < value.length(); i++) {
            char c = value.charAt(i);
            if (devanagariToAsciiMap.containsKey(c)) {
                result.append(devanagariToAsciiMap.get(c));
            } else {
                result.append(c);
            }
        }
        
        return result.toString();
    }

    /**
     * Formats a number to a string with the specified locale
     * @param value The number to format
     * @param locale The locale to use for formatting
     * @return The formatted string
     */
    public static String formatNumber(double value, Locale locale) {
        NumberFormat formatter = NumberFormat.getNumberInstance(locale);
        if (formatter instanceof DecimalFormat) {
            ((DecimalFormat) formatter).applyPattern("#,##0.00");
        }
        return formatter.format(value);
    }
}
