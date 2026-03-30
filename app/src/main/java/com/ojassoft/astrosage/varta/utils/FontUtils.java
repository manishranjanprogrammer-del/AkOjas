package com.ojassoft.astrosage.varta.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

/**
 * Shared font helper that applies asset-backed typefaces to supported text widgets.
 * Callers are expected to fetch the target view in their Java/Kotlin class and pass it here.
 */
public class FontUtils {
    private static final Map<String, Typeface> TYPEFACE_CACHE = new HashMap<>();

    /**
     * Applies the requested asset font to a TextView.
     */
    public static void changeFont(Context context, TextView textView, String font) {
        if (textView == null) {
            return;
        }
        textView.setTypeface(getTypeface(context, font));
    }

    /**
     * Applies the requested asset font to a Button.
     */
    public static void changeFont(Context context, Button button, String font) {
        if (button == null) {
            return;
        }
        button.setTypeface(getTypeface(context, font));
    }

    /**
     * Applies the requested asset font to an EditText.
     */
    public static void changeFont(Context context, EditText edittext, String font) {
        if (edittext == null) {
            return;
        }
        edittext.setTypeface(getTypeface(context, font));
    }

    /**
     * Applies the requested asset font to a RadioButton.
     */
    public static void changeFont(Context context, RadioButton radioButton, String font) {
        if (radioButton == null) {
            return;
        }
        radioButton.setTypeface(getTypeface(context, font));
    }

    /**
     * Reuses loaded typefaces so repeated font application does not reload the same asset.
     */
    private static Typeface getTypeface(Context context, String font) {
        Typeface cachedTypeface = TYPEFACE_CACHE.get(font);
        if (cachedTypeface == null) {
            cachedTypeface = Typeface.createFromAsset(context.getAssets(), font);
            TYPEFACE_CACHE.put(font, cachedTypeface);
        }
        return cachedTypeface;
    }
}
