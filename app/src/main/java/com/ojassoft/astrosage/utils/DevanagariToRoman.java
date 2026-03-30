package com.ojassoft.astrosage.utils;

import android.os.Build;

import java.text.Normalizer;
import java.util.HashMap;
import java.util.Map;

public class DevanagariToRoman {

    // Independent vowels
    private static final Map<Character, String> INDEPENDENT_VOWELS = new HashMap<>();
    // Dependent vowels (matras)
    private static final Map<Character, String> DEPENDENT_VOWELS = new HashMap<>();
    // Consonants
    private static final Map<Character, String> CONSONANTS = new HashMap<>();
    // Special signs (anusvara, visarga, halant, etc.)
    private static final Map<Character, String> SPECIAL_SIGNS = new HashMap<>();

    static {
        // -------------------------
        // Independent Vowels
        // -------------------------
        INDEPENDENT_VOWELS.put('अ', "a");
        INDEPENDENT_VOWELS.put('आ', "aa");
        INDEPENDENT_VOWELS.put('इ', "i");
        INDEPENDENT_VOWELS.put('ई', "i");
        INDEPENDENT_VOWELS.put('उ', "u");
        INDEPENDENT_VOWELS.put('ऊ', "u");
        INDEPENDENT_VOWELS.put('ऋ', "ri");
        INDEPENDENT_VOWELS.put('ए', "e");
        INDEPENDENT_VOWELS.put('ऐ', "ai");
        INDEPENDENT_VOWELS.put('ओ', "o");
        INDEPENDENT_VOWELS.put('औ', "au");
        INDEPENDENT_VOWELS.put('ऑ', "o");

        // -------------------------
        // Dependent Vowels (Matras)
        // -------------------------
        DEPENDENT_VOWELS.put('ा', "a");
        DEPENDENT_VOWELS.put('ि', "i");
        DEPENDENT_VOWELS.put('ी', "i");
        DEPENDENT_VOWELS.put('ु', "u");
        DEPENDENT_VOWELS.put('ू', "u");
        DEPENDENT_VOWELS.put('े', "e");
        DEPENDENT_VOWELS.put('ै', "ai");
        DEPENDENT_VOWELS.put('ो', "o");
        DEPENDENT_VOWELS.put('ौ', "au");
        DEPENDENT_VOWELS.put('ृ', "ri"); // e.g. "कृ" -> "kri"

        // -------------------------
        // Consonants
        // -------------------------
        CONSONANTS.put('क', "k");
        CONSONANTS.put('ख', "kh");
        CONSONANTS.put('ग', "g");
        CONSONANTS.put('घ', "gh");
        // NOTE: changed per request: 'च' -> "ch"
        CONSONANTS.put('च', "ch");
        // 'छ' -> "chh" or "chh" (your choice)
        CONSONANTS.put('छ', "chh");

        CONSONANTS.put('ज', "j");
        CONSONANTS.put('झ', "jh");
        CONSONANTS.put('ञ', "n");

        CONSONANTS.put('ट', "t");
        CONSONANTS.put('ठ', "th");
        CONSONANTS.put('ड', "d");
        CONSONANTS.put('ढ', "dh");
        CONSONANTS.put('ण', "n");

        CONSONANTS.put('त', "t");
        CONSONANTS.put('थ', "th");
        CONSONANTS.put('द', "d");
        CONSONANTS.put('ध', "dh");
        CONSONANTS.put('न', "n");

        CONSONANTS.put('प', "p");
        CONSONANTS.put('फ', "ph");
        CONSONANTS.put('ब', "b");
        CONSONANTS.put('भ', "bh");
        CONSONANTS.put('म', "m");

        CONSONANTS.put('य', "y");
        CONSONANTS.put('र', "r");
        CONSONANTS.put('ल', "l");
        CONSONANTS.put('व', "v");

        CONSONANTS.put('श', "sh");
        CONSONANTS.put('ष', "sh");
        CONSONANTS.put('स', "s");
        CONSONANTS.put('ह', "h");

        // -------------------------
        // Special Signs
        // -------------------------
        // Anusvara (ं) => "n"
        SPECIAL_SIGNS.put('ं', "n");
        // Chandrabindu (ँ) => "n"
        SPECIAL_SIGNS.put('ँ', "n");
        // Visarga (ः) => "h"
        SPECIAL_SIGNS.put('ः', "h");
        // Nukta (़) => ""
        SPECIAL_SIGNS.put('़', "");
        // Halant (्) => skip the inherent vowel
        SPECIAL_SIGNS.put('्', "");
        // Halant ॉ => skip the inherent vowel
        SPECIAL_SIGNS.put('ॉ', "o");
        
    }

    /**
     * Transliterate Devanagari text to a simplified Roman form.
     * 
     * @param input Devanagari text
     * @return Romanized text
     */
    public static String transliterate(String input) {
        if (input == null || input.isEmpty()) {
            return "";
        }
        
        
             // 1. Normalize to NFC
        input = Normalizer.normalize(input, Normalizer.Form.NFC);

        // 2. Convert to codepoints
        int[] codePoints = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            codePoints = input.codePoints().toArray();
        }
        StringBuilder result = new StringBuilder();

        for (int i = 0; i < codePoints.length; i++) {
            int current = codePoints[i];
            char currentChar = (char) current;

            // Special case: "आं" / "आँ" => "an"
            if (currentChar == 'आ' && (i + 1 < codePoints.length)) {
                char nextChar = (char) codePoints[i + 1];
                if (nextChar == 'ं' || nextChar == 'ँ') {
                    result.append("an");
                    i++; // skip the nasal
                    continue;
                }
            }

            // 1. Independent Vowel?
            if (INDEPENDENT_VOWELS.containsKey(currentChar)) {
                result.append(INDEPENDENT_VOWELS.get(currentChar));
                continue;
            }

            // 2. Consonant?
            if (CONSONANTS.containsKey(currentChar)) {
                StringBuilder cRoman = new StringBuilder(CONSONANTS.get(currentChar));

                // Look ahead for a dependent vowel, halant, etc.
                if (i + 1 < codePoints.length) {
                    char nextChar = (char) codePoints[i + 1];

                    // If next is a dependent vowel
                    if (DEPENDENT_VOWELS.containsKey(nextChar)) {
                        cRoman.append(DEPENDENT_VOWELS.get(nextChar));
                        i++;
                    }
                    // If next is halant (्), skip adding 'a'
                    else if (nextChar == '्') {
                        i++;
                        // do not add a vowel
                    }
                    else {
                        // **Only add default 'a' if the next char is also Devanagari** 
                        if (Character.UnicodeBlock.of(nextChar) == Character.UnicodeBlock.DEVANAGARI) {
                            cRoman.append('a');
                        }
                        // else => skip 'a'
                    }
                } 
                // If it's the last character, skip the default 'a'
                // else do nothing here.

                result.append(cRoman);
                continue;
            }

            // 3. Dependent vowel standing alone
            if (DEPENDENT_VOWELS.containsKey(currentChar)) {
                result.append(DEPENDENT_VOWELS.get(currentChar));
                continue;
            }

            // 4. Special signs
            if (SPECIAL_SIGNS.containsKey(currentChar)) {
                result.append(SPECIAL_SIGNS.get(currentChar));
                continue;
            }

            // 5. Devanagari full stop "।" => "."
            if (currentChar == '।') {
                result.append('.');
                continue;
            }

            // 6. Otherwise
            result.append(currentChar);
        }

        return result.toString();
    }

    private static boolean isPunctuation(char c) {
        return "-!.,;:?()[]{}\"'".indexOf(c) >= 0;
    }
}
