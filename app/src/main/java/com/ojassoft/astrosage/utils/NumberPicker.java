package com.ojassoft.astrosage.utils;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.FontUtils;

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
        if(view instanceof EditText){
         /*   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ((EditText) view).setTextSize(pxToDp(5, AstrosageKundliApplication.getAppContext()));
                ((TextView) view).setTypeface(Typeface.DEFAULT_BOLD);
            }
            else
            {
                ((EditText) view).setTextSize(pxToDp(8, AstrosageKundliApplication.getAppContext()));
                ((TextView) view).setTypeface(Typeface.DEFAULT_BOLD);
            }*/
            ((EditText) view).setTextSize(20);
            ((EditText) view).setTextColor(getResources().getColor(R.color.black));

            // ((EditText) view).setTextColor(Color.parseColor("#333333"));
        }
        else if(view instanceof TextView)
        {
            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                ((TextView) view).setTextSize(pxToDp(5, AstrosageKundliApplication.getAppContext()));
                ((TextView) view).setTypeface(Typeface.DEFAULT_BOLD);
            }
            else
            {
                ((TextView) view).setTextSize(pxToDp(8, AstrosageKundliApplication.getAppContext()));
                ((TextView) view).setTypeface(Typeface.DEFAULT_BOLD);
            }*/

            ((TextView) view).setTextSize(18);
            ((TextView) view).setTextColor(getResources().getColor(R.color.black));

            //((TextView) view).setTextColor(Color.parseColor("#333333"));
        }
    }


    public static int pxToDp(int px, Context context) {
        Resources r = context.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, r.getDisplayMetrics());
    }
}
