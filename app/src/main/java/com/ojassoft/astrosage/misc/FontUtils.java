package com.ojassoft.astrosage.misc;

import android.content.Context;
import android.graphics.Typeface;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class FontUtils {

    static Typeface customFont;

    public static void changeFont(Context context, TextView textView, String font) {

        customFont = Typeface.createFromAsset(context.getAssets(), font);

        textView.setTypeface(customFont);
    }

    public static void changeFont(Context context, Button button, String font) {

        customFont = Typeface.createFromAsset(context.getAssets(), font);

        button.setTypeface(customFont);
    }

    public static void changeFont(Context context, EditText edittext, String font) {

        customFont = Typeface.createFromAsset(context.getAssets(), font);

        edittext.setTypeface(customFont);
    }

}
