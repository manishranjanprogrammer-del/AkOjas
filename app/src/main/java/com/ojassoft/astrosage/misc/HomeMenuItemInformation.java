package com.ojassoft.astrosage.misc;

import android.graphics.drawable.Drawable;

/**
 * Created by Amit Rautela on 24/2/16.
 */

public class HomeMenuItemInformation {
    public String title;
    public Drawable icon;
    public int index=0;
    public boolean isSeparator;;
    public HomeMenuItemInformation(String title, Drawable icon,int index,boolean isSeparator)
    {
        this.title=title;
        this.icon = icon;
        this.index = index;
        this.isSeparator = isSeparator;
    }

}