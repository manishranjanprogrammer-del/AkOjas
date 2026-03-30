package com.ojassoft.astrosage.varta.ui.activity;

import android.text.InputFilter;
import android.text.Spanned;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * InputFilter that strips emoji symbols and digits from the user's input.
 */
public class EmojiInputFilter implements InputFilter {

    private static final Pattern EMOJI_PATTERN = Pattern.compile("[\\p{So}\\p{Cs}]");
    private static final Pattern DIGIT_PATTERN = Pattern.compile("\\d+");

    @Override
    public CharSequence filter(CharSequence source,
                               int start,
                               int end,
                               Spanned dest,
                               int dstart,
                               int dend) {
        if (source == null) {
            return null;
        }

        String input = source.subSequence(start, end).toString();
        String filtered = EMOJI_PATTERN.matcher(input).replaceAll("");
        filtered = DIGIT_PATTERN.matcher(filtered).replaceAll("");

        if (filtered.length() == input.length()) {
            return null;
        }

        return filtered;
    }
}
