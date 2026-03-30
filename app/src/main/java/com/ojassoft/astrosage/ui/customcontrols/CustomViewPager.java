package com.ojassoft.astrosage.ui.customcontrols;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

public class CustomViewPager extends ViewPager {

    public boolean enabled = true;

    public CustomViewPager(Context context) {
        super(context);
    }

    public CustomViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            if (this.enabled) {
                if (getCurrentItem() == 0 && getChildCount() == 0) {
                    return false;
                }
                return super.onTouchEvent(ev);
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        try {
            if (this.enabled) {
                if (getCurrentItem() == 0 && getChildCount() == 0) {
                    return false;
                }
                return super.onInterceptTouchEvent(ev);
            } else {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
    }
}