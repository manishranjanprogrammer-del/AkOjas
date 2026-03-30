package com.ojassoft.astrosage.varta.utils;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ojassoft.astrosage.R;

/**
 * Created by ojas-10 on 16/5/16.
 */
@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class NumberPicker extends android.widget.NumberPicker {

    public NumberPicker(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void addView(View child) {
        super.addView(child);
        updateView(child);
    }

    @Override
    public void addView(View child, int index, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, index, params);
        updateView(child);
    }

    @Override
    public void addView(View child, android.view.ViewGroup.LayoutParams params) {
        super.addView(child, params);
        updateView(child);
    }

    private void updateView(View view) {
        try {
            if (view instanceof EditText) {
                ((EditText) view).setTextSize(18);
                ((EditText) view).setTextColor(getResources().getColor(R.color.black));

                FontUtils.changeFont(getContext(), ((EditText) view), CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
            } else if (view instanceof TextView) {
                ((TextView) view).setTextSize(18);
                ((TextView) view).setTextColor(getResources().getColor(R.color.black));
                FontUtils.changeFont(getContext(), ((TextView) view), CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int pxToDp(int px, Context context) {

        Resources r = context.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, r.getDisplayMetrics());
        // return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

}