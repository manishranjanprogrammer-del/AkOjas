package com.ojassoft.astrosage.misc;

import android.content.Context;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import com.google.android.material.tabs.TabLayout;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

public class CustomTabLayout extends TabLayout {
    Context context;

    public CustomTabLayout(Context context) {
        super(context);
        this.context = context;
    }

    public CustomTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public CustomTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
    }

    @Override
    public void addTab(@NonNull Tab tab) {
        super.addTab(tab);

        Typeface typeface = CUtils.getRobotoFont(
                context,
                CUtils.getLanguageCodeFromPreference(context), CGlobalVariables.regular);

        ViewGroup mainView = (ViewGroup) getChildAt(0);
        ViewGroup tabView = (ViewGroup) mainView.getChildAt(tab.getPosition());

        int tabChildCount = tabView.getChildCount();
        for (int i = 0; i < tabChildCount; i++) {
            View tabViewChild = tabView.getChildAt(i);
            if (tabViewChild instanceof TextView) {
                ((TextView) tabViewChild).setTypeface(typeface);
            }
        }
    }
}