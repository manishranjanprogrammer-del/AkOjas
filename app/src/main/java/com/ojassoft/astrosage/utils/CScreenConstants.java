package com.ojassoft.astrosage.utils;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Typeface;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;

import androidx.core.content.ContextCompat;

import java.util.HashMap;

/**
 * This object is used to store screen height and width and other device screen dependent constants to draw kundli and other tabels.
 *
 * @author Hukum
 */
public class CScreenConstants {
    Typeface _typeface;
    Typeface regularTypeface, mediumTypeface;

    public boolean IS_DEVICE_SUPPORT_UNICODE = false;
    public static int firstTableTopMargin = 16;
    public static int secondTableTopMargin = 7;
    public static int topMargin = 36;
    public static int paddingBottom = 20;
    public static int rectHieght = 90;
    public static int textPaddingLeft = 20;
    public static int firstLineTopMargin = 36;
    public static int margininline = 36;
    public static int noteTopMarign = 16;
    public static int noteLineMargin = 16;
    public static int noteFirstLineMarign = 16;
    public static int rectHieghtMarginTop = 12;
    public static int rectHieghtMarginBottom = 12;
    public static int notetextLeftMargin = 50;
    float mGestureThreshold;

    public static int pxToDp(int px, Context context) {

        Resources r = context.getResources();
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, px, r.getDisplayMetrics());
        // return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    /**
     *
     * @param context
     * @param typeface
     */
    public CScreenConstants(Activity context, Typeface typeface) {
        _typeface = typeface;
        DisplayMetrics displaymetrics = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);

        if (displaymetrics.widthPixels > displaymetrics.heightPixels) {
            //DeviceScreenHeight = displaymetrics.widthPixels - 6;
            // SAN Exp
            // As per Punit sir direction height and width are interchanged for Samsung Dex Support
            DeviceScreenWidth = displaymetrics.heightPixels - 6;
            DeviceScreenHeight = displaymetrics.widthPixels - 80;
            NewDeviceScreenWidth = displaymetrics.heightPixels - 3;

        }
        if (displaymetrics.widthPixels < displaymetrics.heightPixels) {
            DeviceScreenWidth = displaymetrics.widthPixels - 6;
            DeviceScreenHeight = displaymetrics.heightPixels - 80;
            NewDeviceScreenWidth = displaymetrics.widthPixels - 3;
        }
        mGestureThreshold = context.getResources().getDimension(R.dimen.heading_text_size);
        initAllVariables(context);
    }

    /**
     * This cunstrutor is Used to initialize objet with specific width and small text size
     * @param context
     * @param deviceScreenWidth
     * @param typeface
     */
    public CScreenConstants(Activity context,float deviceScreenWidth, Typeface typeface) {
        _typeface = typeface;
        this.DeviceScreenWidth = deviceScreenWidth;
        this.DeviceScreenHeight = deviceScreenWidth;
        NewDeviceScreenWidth = deviceScreenWidth - 3;
        //Font scale checking
        float fontScale = context.getResources().getConfiguration().fontScale;
        if(fontScale > 1)
            mGestureThreshold = context.getResources().getDimension(R.dimen.tv_size_10);
        else
            mGestureThreshold = context.getResources().getDimension(R.dimen.tv_size_13);

        initAllVariables(context);
    }


    private void initAllVariables(Activity context) {

        _typeface = CUtils.getRobotoFont(context, ((AstrosageKundliApplication) context.getApplication())
                .getLanguageCode(), CGlobalVariables.medium);
        regularTypeface = CUtils.getRobotoFont(context, ((AstrosageKundliApplication) context.getApplication())
                .getLanguageCode(), CGlobalVariables.regular);
        mediumTypeface = CUtils.getRobotoFont(context, ((AstrosageKundliApplication) context.getApplication())
                .getLanguageCode(), CGlobalVariables.medium);
        SharedPreferences sharedPreferences = context.getSharedPreferences(CGlobalVariables.APP_PREFS_NAME, Context.MODE_PRIVATE);
        NotShowUranusNeptunePlutoInChart = sharedPreferences.getBoolean(CGlobalVariables.APP_PREFS_NotShowPlUnNa, false);


        intiCustomFontTextSizeAccordingToScreen(context);
        initNorthKundliCoordinate();
        initSouthKundliCoordinate();
        //@Tejinder Singh init EAST kundli coordinate
        initEastKundliCoordinate();
        initNorthAndSouthKundliCoordinatesForTables();
        initApplicationfontAndColors(context);
        IS_DEVICE_SUPPORT_UNICODE = CUtils.isSupportUnicodeHindi();
    }


    private void initNorthAndSouthKundliCoordinatesForTables() {

        float HKUNDLISpace = 15.00f;
        float VKUNDLISpace = 35.00f;
        float w = DeviceScreenWidth;
        float h = w;

        float x1 = 0;
        float y1 = 0;
        float x2 = w;
        float y2 = h;
        try {

            NTRashiX[0] = (HKUNDLISpace + (x2 - x1) / 2
                    - (x2 - x1) / 32 + x1);
            NTRashiX[1] = (HKUNDLISpace + (x2 - x1) / 32 + x1);
            NTRashiX[2] = (HKUNDLISpace + (x2 - x1) / 8
                    - (x2 - x1) / 20 + (x2 - x1) / 128 + x1);
            NTRashiX[3] = (HKUNDLISpace + (x2 - x1) / 4
                    - (x2 - x1) / 32 + x1);
            NTRashiX[4] = (HKUNDLISpace + (x2 - x1) / 8
                    - (x2 - x1) / 20 + (x2 - x1) / 128 + x1);
            NTRashiX[5] = (HKUNDLISpace + (x2 - x1) / 4 + x1);
            NTRashiX[6] = (HKUNDLISpace + (x2 - x1) / 2
                    - (x2 - x1) / 32 + x1);
            NTRashiX[7] = (HKUNDLISpace + (x2 - x1) / 2
                    + (x2 - x1) / 4 + x1);
            NTRashiX[8] = (HKUNDLISpace + (x2 - x1) / 2
                    + (x2 - x1) / 4 + (x2 - x1) / 8 - (x2 - x1) / 64 + x1);
            NTRashiX[9] = (HKUNDLISpace + (x2 - x1) / 2
                    + (x2 - x1) / 4 - (x2 - x1) / 32 + x1);
            NTRashiX[10] = (HKUNDLISpace + (x2 - x1) / 2
                    + (x2 - x1) / 4 + (x2 - x1) / 8 - (x2 - x1) / 64 + x1);
            NTRashiX[11] = (HKUNDLISpace + (x2 - x1) / 2
                    + (x2 - x1) / 32 + x1);

            NTRashiY[0] = (VKUNDLISpace + (y2 - y1) / 8 + y1);
            NTRashiY[1] = (VKUNDLISpace + y1);
            NTRashiY[2] = (VKUNDLISpace + (y2 - y1) / 8
                    + (y2 - y1) / 16 + (y2 - y1) / 64 + y1);
            NTRashiY[3] = (VKUNDLISpace + (y2 - y1) / 4
                    + (y2 - y1) / 8 + y1);
            NTRashiY[4] = (VKUNDLISpace + (y2 - y1) / 2
                    + (y2 - y1) / 8 + (y2 - y1) / 16 + (y2 - y1) / 64 + y1);
            NTRashiY[5] = (VKUNDLISpace + (y2 - y1) / 2
                    + (y2 - y1) / 4 + (y2 - y1) / 8 - (y2 - y1) / 48 + y1);
            NTRashiY[6] = (VKUNDLISpace + (y2 - y1) / 2
                    + (y2 - y1) / 8 + y1);
            NTRashiY[7] = (VKUNDLISpace + (y2 - y1) / 2
                    + (y2 - y1) / 4 + (y2 - y1) / 8 - (y2 - y1) / 48 + y1);
            NTRashiY[8] = (VKUNDLISpace + (y2 - y1) / 2
                    + (y2 - y1) / 4 + (y2 - y1) / 32 + (y2 - y1) / 128 + y1);
            NTRashiY[9] = (VKUNDLISpace + (y2 - y1) / 4
                    + (y2 - y1) / 8 + y1);
            NTRashiY[10] = (VKUNDLISpace + (y2 - y1) / 4
                    + (y2 - y1) / 32 + (y2 - y1) / 128 + y1);
            NTRashiY[11] = (VKUNDLISpace + y1);

            // INIT X1,Y1
            NTKundliPlanetX1[0] = (HKUNDLISpace + (x2 - x1)
                    / 2 - (x2 - x1) / 32 + x1);
            NTKundliPlanetX1[1] = (HKUNDLISpace + (x2 - x1)
                    / 2 - (x2 - x1) / 32 + x1);

            NTKundliPlanetX1[2] = (HKUNDLISpace + (x2 - x1)
                    / 4 + (x2 - x1) / 16 + x1);
            NTKundliPlanetX1[3] = (HKUNDLISpace + (x2 - x1)
                    / 4 + (x2 - x1) / 16 + x1);

            NTKundliPlanetX1[4] = (HKUNDLISpace + (x2 - x1)
                    / 4 + (x2 - x1) / 8 + x1);
            NTKundliPlanetX1[5] = (HKUNDLISpace + (x2 - x1)
                    / 4 + (x2 - x1) / 8 + x1);

            NTKundliPlanetX1[6] = (HKUNDLISpace + (x2 - x1)
                    / 2 + (x2 - x1) / 8 - (x2 - x1) / 32 - (x2 - x1) / 64 + x1);
            NTKundliPlanetX1[7] = (HKUNDLISpace + (x2 - x1)
                    / 2 + (x2 - x1) / 8 - (x2 - x1) / 32 - (x2 - x1) / 64 + x1);

            NTKundliPlanetX1[8] = (HKUNDLISpace + (x2 - x1)
                    / 2 - (x2 - x1) / 32 + x1);
            NTKundliPlanetX1[9] = (HKUNDLISpace + (x2 - x1)
                    / 2 - (x2 - x1) / 32 - (x2 - x1) / 64 + x1);

            NTKundliPlanetX1[10] = (HKUNDLISpace + (x2 - x1)
                    / 2 + (x2 - x1) / 64 + x1);
            NTKundliPlanetX1[11] = (HKUNDLISpace + (x2 - x1)
                    / 2 + (x2 - x1) / 64 + x1);

            NTKundliPlanetY1[0] = (VKUNDLISpace + (y2 - y1)
                    / 24 + y1);
            NTKundliPlanetY1[1] = (VKUNDLISpace + (y2 - y1)
                    / 12 + y1);

            NTKundliPlanetY1[2] = (VKUNDLISpace + (y2 - y1)
                    / 4 - (y2 - y1) / 24 + y1);
            NTKundliPlanetY1[3] = (VKUNDLISpace + (y2 - y1) / 4);

            NTKundliPlanetY1[4] = (VKUNDLISpace + (y2 - y1)
                    / 8 + (y2 - y1) / 64 + y1);
            NTKundliPlanetY1[5] = (VKUNDLISpace + (y2 - y1)
                    / 8 + (y2 - y1) / 16 + y1);

            NTKundliPlanetY1[6] = (VKUNDLISpace + (y2 - y1)
                    / 8 + (y2 - y1) / 64 + y1);
            NTKundliPlanetY1[7] = (VKUNDLISpace + (y2 - y1)
                    / 8 + (y2 - y1) / 16 + y1);

            NTKundliPlanetY1[8] = (VKUNDLISpace + (y2 - y1)
                    / 4 + (y2 - y1) / 16 + y1);
            NTKundliPlanetY1[9] = (VKUNDLISpace + (y2 - y1)
                    / 4 + (y2 - y1) / 8 + y1);

            NTKundliPlanetY1[10] = (VKUNDLISpace + (y2 - y1)
                    / 4 - (y2 - y1) / 24 + y1);
            NTKundliPlanetY1[11] = (VKUNDLISpace + (y2 - y1) / 4);

            // INIT X2,Y2

            NTKundliPlanetX2[0] = (HKUNDLISpace + (x2 - x1)
                    / 8 - (x2 - x1) / 28 + x1);
            NTKundliPlanetX2[1] = (HKUNDLISpace + (x2 - x1)
                    / 8 - (x2 - x1) / 28 + x1);

            NTKundliPlanetX2[2] = (HKUNDLISpace + (x2 - x1)
                    / 4 + (x2 - x1) / 8 - (x2 - x1) / 23 + x1);
            NTKundliPlanetX2[3] = (HKUNDLISpace + (x2 - x1)
                    / 4 + (x2 - x1) / 8 - (x2 - x1) / 23 + x1);

            NTKundliPlanetX2[4] = (HKUNDLISpace + (x2 - x1)
                    / 8 + (x2 - x1) / 16 - (x2 - x1) / 48 + x1);
            NTKundliPlanetX2[5] = (HKUNDLISpace + (x2 - x1)
                    / 8 + (x2 - x1) / 16 - (x2 - x1) / 48 + x1);

            NTKundliPlanetX2[6] = (HKUNDLISpace + (x2 - x1)
                    / 4 + x1);
            NTKundliPlanetX2[7] = (HKUNDLISpace + (x2 - x1)
                    / 4 + x1);

            NTKundliPlanetX2[8] = (HKUNDLISpace + (x2 - x1)
                    / 4 - (x2 - x1) / 12 + x1);
            NTKundliPlanetX2[9] = (HKUNDLISpace + (x2 - x1)
                    / 4 - (x2 - x1) / 12 + x1);

            NTKundliPlanetX2[10] = (HKUNDLISpace + (x2 - x1)
                    / 4 - (x2 - x1) / 128 + x1);
            NTKundliPlanetX2[11] = (HKUNDLISpace + (x2 - x1)
                    / 4 - (x2 - x1) / 128 + x1);

            NTKundliPlanetY2[0] = (VKUNDLISpace + y1);
            NTKundliPlanetY2[1] = (VKUNDLISpace + (y2 - y1)
                    / 16 - (y2 - y1) / 64 + y1);

            NTKundliPlanetY2[2] = (VKUNDLISpace + y1);
            NTKundliPlanetY2[3] = (VKUNDLISpace + (y2 - y1)
                    / 16 - (y2 - y1) / 64 + y1);

            NTKundliPlanetY2[4] = (VKUNDLISpace + (y2 - y1)
                    / 8 - (x2 - x1) / 32 + y1);
            NTKundliPlanetY2[5] = (VKUNDLISpace + (y2 - y1)
                    / 8 + (x2 - x1) / 128 + y1);

            NTKundliPlanetY2[6] = (VKUNDLISpace + y1);
            NTKundliPlanetY2[7] = (VKUNDLISpace + (y2 - y1)
                    / 16 - (y2 - y1) / 64 + y1);

            NTKundliPlanetY2[8] = (VKUNDLISpace + y1);
            NTKundliPlanetY2[9] = (VKUNDLISpace + (y2 - y1)
                    / 16 - (y2 - y1) / 64 + y1);

            NTKundliPlanetY2[10] = (VKUNDLISpace + (y2 - y1)
                    / 8 - (x2 - x1) / 32 + y1);
            NTKundliPlanetY2[11] = (VKUNDLISpace + (y2 - y1)
                    / 8 + (x2 - x1) / 128 + y1);

            // INIT X3,Y3

            NTKundliPlanetX3[0] = (HKUNDLISpace + x1);
            NTKundliPlanetX3[1] = (HKUNDLISpace + x1);

            NTKundliPlanetX3[2] = (HKUNDLISpace + x1);
            NTKundliPlanetX3[3] = (HKUNDLISpace + x1);

            NTKundliPlanetX3[4] = (HKUNDLISpace + x1);
            NTKundliPlanetX3[5] = (HKUNDLISpace + x1);

            NTKundliPlanetX3[6] = (HKUNDLISpace + (x2 - x1)
                    / 16 + (x2 - x1) / 64 + x1);
            NTKundliPlanetX3[7] = (HKUNDLISpace + (x2 - x1)
                    / 16 + (x2 - x1) / 64 + x1);

            NTKundliPlanetX3[8] = (HKUNDLISpace + (x2 - x1)
                    / 8 - (x2 - x1) / 24 + x1);
            NTKundliPlanetX3[9] = (HKUNDLISpace + (x2 - x1)
                    / 16 - (x2 - x1) / 128 + x1);

            NTKundliPlanetX3[10] = (HKUNDLISpace + (x2 - x1)
                    / 8 + x1);
            NTKundliPlanetX3[11] = (HKUNDLISpace + (x2 - x1)
                    / 8 + x1);

            NTKundliPlanetY3[0] = (VKUNDLISpace + (y2 - y1)
                    / 16 + y1);
            NTKundliPlanetY3[1] = (VKUNDLISpace + (y2 - y1)
                    / 8 - (y2 - y1) / 48 + y1);

            NTKundliPlanetY3[2] = (VKUNDLISpace + (y2 - y1)
                    / 8 + (y2 - y1) / 16 + y1);
            NTKundliPlanetY3[3] = (VKUNDLISpace + (y2 - y1)
                    / 4 - (y2 - y1) / 48 + y1);

            NTKundliPlanetY3[4] = (VKUNDLISpace + (y2 - y1)
                    / 4 + (y2 - y1) / 16 + y1);
            NTKundliPlanetY3[5] = (VKUNDLISpace + (y2 - y1)
                    / 4 + (y2 - y1) / 8 - (y2 - y1) / 48 - (y2 - y1) / 128 + y1);

            NTKundliPlanetY3[6] = (VKUNDLISpace + (y2 - y1)
                    / 8 + (y2 - y1) / 128 + y1);
            NTKundliPlanetY3[7] = (VKUNDLISpace + (y2 - y1)
                    / 8 + (y2 - y1) / 24 + y1);

            NTKundliPlanetY3[8] = (VKUNDLISpace + (y2 - y1)
                    / 4 + (y2 - y1) / 48 - (y2 - y1) / 64 + y1);
            NTKundliPlanetY3[9] = (VKUNDLISpace + (y2 - y1)
                    / 4 + (y2 - y1) / 16 - (y2 - y1) / 64 + y1);

            NTKundliPlanetY3[10] = (VKUNDLISpace + (y2 - y1)
                    / 8 + (y2 - y1) / 16 + (y2 - y1) / 128 + y1);
            NTKundliPlanetY3[11] = (VKUNDLISpace + (y2 - y1)
                    / 4 - (y2 - y1) / 48 + y1);

            // INIT X4,Y4

            NTKundliPlanetX4[0] = (HKUNDLISpace + (x2 - x1)
                    / 4 - (x2 - x1) / 32 + x1);
            NTKundliPlanetX4[1] = (HKUNDLISpace + (x2 - x1)
                    / 4 - (x2 - x1) / 32 + x1);

            NTKundliPlanetX4[2] = (HKUNDLISpace + (x2 - x1)
                    / 16 + x1);
            NTKundliPlanetX4[3] = (HKUNDLISpace + (x2 - x1)
                    / 16 + x1);

            NTKundliPlanetX4[4] = (HKUNDLISpace + (x2 - x1)
                    / 8 + x1);
            NTKundliPlanetX4[5] = (HKUNDLISpace + (x2 - x1)
                    / 8 + x1);

            NTKundliPlanetX4[6] = (HKUNDLISpace + (x2 - x1)
                    / 4 + (x2 - x1) / 8 - (x2 - x1) / 32 - (x2 - x1) / 64 + x1);
            NTKundliPlanetX4[7] = (HKUNDLISpace + (x2 - x1)
                    / 4 + (x2 - x1) / 8 - (x2 - x1) / 32 - (x2 - x1) / 64 + x1);

            NTKundliPlanetX4[8] = (HKUNDLISpace + (x2 - x1)
                    / 4 - (x2 - x1) / 32 + x1);
            NTKundliPlanetX4[9] = (HKUNDLISpace + (x2 - x1)
                    / 4 - (x2 - x1) / 32 - (x2 - x1) / 64 + x1);

            NTKundliPlanetX4[10] = (HKUNDLISpace + (x2 - x1)
                    / 4 + (x2 - x1) / 64 + x1);
            NTKundliPlanetX4[11] = (HKUNDLISpace + (x2 - x1)
                    / 4 + (x2 - x1) / 64 + x1);

            NTKundliPlanetY4[0] = (VKUNDLISpace + (y2 - y1)
                    / 4 + (y2 - y1) / 24 + y1);
            NTKundliPlanetY4[1] = (VKUNDLISpace + (y2 - y1)
                    / 4 + (y2 - y1) / 12 + y1);

            NTKundliPlanetY4[2] = (VKUNDLISpace + (y2 - y1)
                    / 4 + (y2 - y1) / 4 - (y2 - y1) / 24 + y1);
            NTKundliPlanetY4[3] = (VKUNDLISpace + (y2 - y1)
                    / 4 + (y2 - y1) / 4);

            NTKundliPlanetY4[4] = (VKUNDLISpace + (y2 - y1)
                    / 4 + (y2 - y1) / 8 + (y2 - y1) / 64 + y1);
            NTKundliPlanetY4[5] = (VKUNDLISpace + (y2 - y1)
                    / 4 + (y2 - y1) / 8 + (y2 - y1) / 16 + y1);

            NTKundliPlanetY4[6] = (VKUNDLISpace + (y2 - y1)
                    / 4 + (y2 - y1) / 8 + (y2 - y1) / 64 + y1);
            NTKundliPlanetY4[7] = (VKUNDLISpace + (y2 - y1)
                    / 4 + (y2 - y1) / 8 + (y2 - y1) / 16 + y1);

            NTKundliPlanetY4[8] = (VKUNDLISpace + (y2 - y1)
                    / 4 + (y2 - y1) / 4 + (y2 - y1) / 16 + y1);
            NTKundliPlanetY4[9] = (VKUNDLISpace + (y2 - y1)
                    / 4 + (y2 - y1) / 4 + (y2 - y1) / 8 + y1);

            NTKundliPlanetY4[10] = (VKUNDLISpace + (y2 - y1)
                    / 4 + (y2 - y1) / 4 - (y2 - y1) / 24 + y1);
            NTKundliPlanetY4[11] = (VKUNDLISpace + (y2 - y1)
                    / 4 + (y2 - y1) / 4);

            // INIT X5,Y5
            NTKundliPlanetX5[0] = (HKUNDLISpace + x1);
            NTKundliPlanetX5[1] = (HKUNDLISpace + x1);

            NTKundliPlanetX5[2] = (HKUNDLISpace + x1);
            NTKundliPlanetX5[3] = (HKUNDLISpace + x1);

            NTKundliPlanetX5[4] = (HKUNDLISpace + x1);
            NTKundliPlanetX5[5] = (HKUNDLISpace + x1);

            NTKundliPlanetX5[6] = (HKUNDLISpace + (x2 - x1)
                    / 16 + (x2 - x1) / 64 + x1);
            NTKundliPlanetX5[7] = (HKUNDLISpace + (x2 - x1)
                    / 16 + (x2 - x1) / 64 + x1);

            NTKundliPlanetX5[8] = (HKUNDLISpace + (x2 - x1)
                    / 8 - (x2 - x1) / 24 + x1);
            NTKundliPlanetX5[9] = (HKUNDLISpace + (x2 - x1)
                    / 16 - (x2 - x1) / 128 + x1);

            NTKundliPlanetX5[10] = (HKUNDLISpace + (x2 - x1)
                    / 8 + x1);
            NTKundliPlanetX5[11] = (HKUNDLISpace + (x2 - x1)
                    / 8 + x1);

            NTKundliPlanetY5[0] = (VKUNDLISpace + (y2 - y1)
                    / 2 + (y2 - y1) / 16 + y1);
            NTKundliPlanetY5[1] = (VKUNDLISpace + (y2 - y1)
                    / 2 + (y2 - y1) / 8 - (y2 - y1) / 48 + y1);

            NTKundliPlanetY5[2] = (VKUNDLISpace + (y2 - y1)
                    / 2 + (y2 - y1) / 8 + (y2 - y1) / 16 + y1);
            NTKundliPlanetY5[3] = (VKUNDLISpace + (y2 - y1)
                    / 2 + (y2 - y1) / 4 - (y2 - y1) / 48 + y1);

            NTKundliPlanetY5[4] = (VKUNDLISpace + (y2 - y1)
                    / 2 + (y2 - y1) / 4 + (y2 - y1) / 16 + y1);
            NTKundliPlanetY5[5] = (VKUNDLISpace + (y2 - y1)
                    / 2 + (y2 - y1) / 4 + (y2 - y1) / 8 - (y2 - y1) / 48
                    - (y2 - y1) / 128 + y1);

            NTKundliPlanetY5[6] = (VKUNDLISpace + (y2 - y1)
                    / 2 + (y2 - y1) / 8 + (y2 - y1) / 128 + y1);
            NTKundliPlanetY5[7] = (VKUNDLISpace + (y2 - y1)
                    / 2 + (y2 - y1) / 8 + (y2 - y1) / 24 + y1);

            NTKundliPlanetY5[8] = (VKUNDLISpace + (y2 - y1)
                    / 2 + (y2 - y1) / 4 + (y2 - y1) / 48 - (y2 - y1) / 64 + y1);
            NTKundliPlanetY5[9] = (VKUNDLISpace + (y2 - y1)
                    / 2 + (y2 - y1) / 4 + (y2 - y1) / 16 - (y2 - y1) / 64 + y1);

            NTKundliPlanetY5[10] = (VKUNDLISpace + (y2 - y1)
                    / 2 + (y2 - y1) / 8 + (y2 - y1) / 16 + (y2 - y1) / 128 + y1);
            NTKundliPlanetY5[11] = (VKUNDLISpace + (y2 - y1)
                    / 2 + (y2 - y1) / 4 - (y2 - y1) / 48 + y1);

            // INIT X6,Y6

            NTKundliPlanetX6[0] = (HKUNDLISpace + (x2 - x1)
                    / 4 - (x2 - x1) / 32 + x1);
            NTKundliPlanetX6[1] = (HKUNDLISpace + (x2 - x1)
                    / 8 + (x2 - x1) / 16 + (x2 - x1) / 64 + x1);

            NTKundliPlanetX6[2] = (HKUNDLISpace + (x2 - x1)
                    / 4 + (x2 - x1) / 16 + (x2 - x1) / 128 + x1);
            NTKundliPlanetX6[3] = (HKUNDLISpace + (x2 - x1)
                    / 4 + (x2 - x1) / 16 + (x2 - x1) / 128 + x1);

            NTKundliPlanetX6[4] = (HKUNDLISpace + (x2 - x1)
                    / 8 + (x2 - x1) / 48 + x1);
            NTKundliPlanetX6[5] = (HKUNDLISpace + (x2 - x1)
                    / 8 + (x2 - x1) / 48 + x1);

            NTKundliPlanetX6[6] = (HKUNDLISpace + (x2 - x1)
                    / 8 + (x2 - x1) / 64 + x1);
            NTKundliPlanetX6[7] = (HKUNDLISpace + (x2 - x1)
                    / 8 + x1);

            NTKundliPlanetX6[8] = (HKUNDLISpace + (x2 - x1)
                    / 16 + (x2 - x1) / 32 + (x2 - x1) / 128 + x1);
            NTKundliPlanetX6[9] = (HKUNDLISpace + (x2 - x1)
                    / 16 + x1);

            NTKundliPlanetX6[10] = (HKUNDLISpace + (x2 - x1)
                    / 4 - (x2 - x1) / 128 + x1);
            NTKundliPlanetX6[11] = (HKUNDLISpace + (x2 - x1)
                    / 4 - (x2 - x1) / 128 + x1);

            NTKundliPlanetY6[0] = (VKUNDLISpace + (y2 - y1)
                    / 2 + (y2 - y1) / 4 + (y2 - y1) / 32 + y1);
            NTKundliPlanetY6[1] = (VKUNDLISpace + (y2 - y1)
                    / 2 + (y2 - y1) / 4 + (y2 - y1) / 16 + y1);

            NTKundliPlanetY6[2] = (VKUNDLISpace + (y2 - y1)
                    / 2 + (y2 - y1) / 4 + (y2 - y1) / 8 + (y2 - y1) / 48 + y1);
            NTKundliPlanetY6[3] = (VKUNDLISpace + (y2 - y1)
                    / 2 + (y2 - y1) / 4 + (y2 - y1) / 8 + (y2 - y1) / 16 + y1);

            NTKundliPlanetY6[4] = (VKUNDLISpace + (y2 - y1)
                    / 2 + (y2 - y1) / 4 + (y2 - y1) / 8 + (y2 - y1) / 48 + y1);
            NTKundliPlanetY6[5] = (VKUNDLISpace + (y2 - y1)
                    / 2 + (y2 - y1) / 4 + (y2 - y1) / 8 + (y2 - y1) / 16 + y1);

            NTKundliPlanetY6[6] = (VKUNDLISpace + (y2 - y1)
                    / 2 + (y2 - y1) / 4 + (y2 - y1) / 16 + (y2 - y1) / 48 + y1);
            NTKundliPlanetY6[7] = (VKUNDLISpace + (y2 - y1)
                    / 2 + (y2 - y1) / 4 + (y2 - y1) / 8 - (y2 - y1) / 80 + y1);

            NTKundliPlanetY6[8] = (VKUNDLISpace + (y2 - y1)
                    / 2 + (y2 - y1) / 4 + (y2 - y1) / 8 + (y2 - y1) / 48 + y1);
            NTKundliPlanetY6[9] = (VKUNDLISpace + (y2 - y1)
                    / 2 + (y2 - y1) / 4 + (y2 - y1) / 8 + (y2 - y1) / 16 + y1);

            NTKundliPlanetY6[10] = (VKUNDLISpace + (y2 - y1)
                    / 2 + (y2 - y1) / 4 + (y2 - y1) / 8 + (y2 - y1) / 48
                    + (y2 - y1) / 256 + y1);
            NTKundliPlanetY6[11] = (VKUNDLISpace + (y2 - y1)
                    / 2 + (y2 - y1) / 4 + (y2 - y1) / 8 + (y2 - y1) / 16 + y1);

            // X7,Y7
            NTKundliPlanetX7[0] = (HKUNDLISpace + (x2 - x1)
                    / 2 - (x2 - x1) / 32 + x1);
            NTKundliPlanetX7[1] = (HKUNDLISpace + (x2 - x1)
                    / 2 - (x2 - x1) / 32 + x1);

            NTKundliPlanetX7[2] = (HKUNDLISpace + (x2 - x1)
                    / 4 + (x2 - x1) / 16 + x1);
            NTKundliPlanetX7[3] = (HKUNDLISpace + (x2 - x1)
                    / 4 + (x2 - x1) / 16 + x1);

            NTKundliPlanetX7[4] = (HKUNDLISpace + (x2 - x1)
                    / 4 + (x2 - x1) / 8 + x1);
            NTKundliPlanetX7[5] = (HKUNDLISpace + (x2 - x1)
                    / 4 + (x2 - x1) / 8 + x1);

            NTKundliPlanetX7[6] = (HKUNDLISpace + (x2 - x1)
                    / 2 + (x2 - x1) / 8 - (x2 - x1) / 32 - (x2 - x1) / 64 + x1);
            NTKundliPlanetX7[7] = (HKUNDLISpace + (x2 - x1)
                    / 2 + (x2 - x1) / 8 - (x2 - x1) / 32 - (x2 - x1) / 64 + x1);

            NTKundliPlanetX7[8] = (HKUNDLISpace + (x2 - x1)
                    / 2 - (x2 - x1) / 32 + x1);
            NTKundliPlanetX7[9] = (HKUNDLISpace + (x2 - x1)
                    / 2 - (x2 - x1) / 32 - (x2 - x1) / 64 + x1);

            NTKundliPlanetX7[10] = (HKUNDLISpace + (x2 - x1)
                    / 2 + (x2 - x1) / 64 + x1);
            NTKundliPlanetX7[11] = (HKUNDLISpace + (x2 - x1)
                    / 2 + (x2 - x1) / 64 + x1);

            NTKundliPlanetY7[0] = (VKUNDLISpace + (y2 - y1)
                    / 2 + (y2 - y1) / 24 + y1);
            NTKundliPlanetY7[1] = (VKUNDLISpace + (y2 - y1)
                    / 2 + (y2 - y1) / 12 + y1);

            NTKundliPlanetY7[2] = (VKUNDLISpace + (y2 - y1)
                    / 2 + (y2 - y1) / 4 - (y2 - y1) / 24 + y1);
            NTKundliPlanetY7[3] = (VKUNDLISpace + (y2 - y1)
                    / 2 + (y2 - y1) / 4);

            NTKundliPlanetY7[4] = (VKUNDLISpace + (y2 - y1)
                    / 2 + (y2 - y1) / 8 + (y2 - y1) / 64 + y1);
            NTKundliPlanetY7[5] = (VKUNDLISpace + (y2 - y1)
                    / 2 + (y2 - y1) / 8 + (y2 - y1) / 16 + y1);

            NTKundliPlanetY7[6] = (VKUNDLISpace + (y2 - y1)
                    / 2 + (y2 - y1) / 8 + (y2 - y1) / 64 + y1);
            NTKundliPlanetY7[7] = (VKUNDLISpace + (y2 - y1)
                    / 2 + (y2 - y1) / 8 + (y2 - y1) / 16 + y1);

            NTKundliPlanetY7[8] = (VKUNDLISpace + (y2 - y1)
                    / 2 + (y2 - y1) / 4 + (y2 - y1) / 16 + y1);
            NTKundliPlanetY7[9] = (VKUNDLISpace + (y2 - y1)
                    / 2 + (y2 - y1) / 4 + (y2 - y1) / 8 + y1);

            NTKundliPlanetY7[10] = (VKUNDLISpace + (y2 - y1)
                    / 2 + (y2 - y1) / 4 - (y2 - y1) / 24 + y1);
            NTKundliPlanetY7[11] = (VKUNDLISpace + (y2 - y1)
                    / 2 + (y2 - y1) / 4);

            // INIT X8,Y8

            NTKundliPlanetX8[0] = (HKUNDLISpace + (x2 - x1)
                    / 2 + (x2 - x1) / 4 - (x2 - x1) / 32 + x1);
            NTKundliPlanetX8[1] = (HKUNDLISpace + (x2 - x1)
                    / 2 + (x2 - x1) / 8 + (x2 - x1) / 16 + (x2 - x1) / 64 + x1);

            NTKundliPlanetX8[2] = (HKUNDLISpace + (x2 - x1)
                    / 2 + (x2 - x1) / 4 + (x2 - x1) / 16 + (x2 - x1) / 128 + x1);
            NTKundliPlanetX8[3] = (HKUNDLISpace + (x2 - x1)
                    / 2 + (x2 - x1) / 4 + (x2 - x1) / 16 + (x2 - x1) / 128 + x1);

            NTKundliPlanetX8[4] = (HKUNDLISpace + (x2 - x1)
                    / 2 + (x2 - x1) / 8 + (x2 - x1) / 48 + x1);
            NTKundliPlanetX8[5] = (HKUNDLISpace + (x2 - x1)
                    / 2 + (x2 - x1) / 8 + (x2 - x1) / 48 + x1);

            NTKundliPlanetX8[6] = (HKUNDLISpace + (x2 - x1)
                    / 2 + (x2 - x1) / 8 + (x2 - x1) / 64 + x1);
            NTKundliPlanetX8[7] = (HKUNDLISpace + (x2 - x1)
                    / 2 + (x2 - x1) / 8 + x1);

            NTKundliPlanetX8[8] = (HKUNDLISpace + (x2 - x1)
                    / 2 + (x2 - x1) / 16 + (x2 - x1) / 32 + (x2 - x1) / 128 + x1);
            NTKundliPlanetX8[9] = (HKUNDLISpace + (x2 - x1)
                    / 2 + (x2 - x1) / 16 + x1);

            NTKundliPlanetX8[10] = (HKUNDLISpace + (x2 - x1)
                    / 2 + (x2 - x1) / 4 - (x2 - x1) / 128 + x1);
            NTKundliPlanetX8[11] = (HKUNDLISpace + (x2 - x1)
                    / 2 + (x2 - x1) / 4 - (x2 - x1) / 128 + x1);

            NTKundliPlanetY8[0] = (VKUNDLISpace + (y2 - y1)
                    / 2 + (y2 - y1) / 4 + (y2 - y1) / 32 + y1);
            NTKundliPlanetY8[1] = (VKUNDLISpace + (y2 - y1)
                    / 2 + (y2 - y1) / 4 + (y2 - y1) / 16 + y1);

            NTKundliPlanetY8[2] = (VKUNDLISpace + (y2 - y1)
                    / 2 + (y2 - y1) / 4 + (y2 - y1) / 8 + (y2 - y1) / 48 + y1);
            NTKundliPlanetY8[3] = (VKUNDLISpace + (y2 - y1)
                    / 2 + (y2 - y1) / 4 + (y2 - y1) / 8 + (y2 - y1) / 16 + y1);

            NTKundliPlanetY8[4] = (VKUNDLISpace + (y2 - y1)
                    / 2 + (y2 - y1) / 4 + (y2 - y1) / 8 + (y2 - y1) / 48 + y1);
            NTKundliPlanetY8[5] = (VKUNDLISpace + (y2 - y1)
                    / 2 + (y2 - y1) / 4 + (y2 - y1) / 8 + (y2 - y1) / 16 + y1);

            NTKundliPlanetY8[6] = (VKUNDLISpace + (y2 - y1)
                    / 2 + (y2 - y1) / 4 + (y2 - y1) / 16 + (y2 - y1) / 48 + y1);
            NTKundliPlanetY8[7] = (VKUNDLISpace + (y2 - y1)
                    / 2 + (y2 - y1) / 4 + (y2 - y1) / 8 - (y2 - y1) / 80 + y1);

            NTKundliPlanetY8[8] = (VKUNDLISpace + (y2 - y1)
                    / 2 + (y2 - y1) / 4 + (y2 - y1) / 8 + (y2 - y1) / 48 + y1);
            NTKundliPlanetY8[9] = (VKUNDLISpace + (y2 - y1)
                    / 2 + (y2 - y1) / 4 + (y2 - y1) / 8 + (y2 - y1) / 16 + y1);

            NTKundliPlanetY8[10] = (VKUNDLISpace + (y2 - y1)
                    / 2 + (y2 - y1) / 4 + (y2 - y1) / 8 + (y2 - y1) / 48
                    + (y2 - y1) / 256 + y1);
            NTKundliPlanetY8[11] = (VKUNDLISpace + (y2 - y1)
                    / 2 + (y2 - y1) / 4 + (y2 - y1) / 8 + (y2 - y1) / 16 + y1);

            // INIT X9,Y9

            NTKundliPlanetX9[0] = (HKUNDLISpace + (x2 - x1)
                    - (x2 - x1) / 16 - (x2 - x1) / 64 + x1);
            NTKundliPlanetX9[1] = (HKUNDLISpace + (x2 - x1)
                    - (x2 - x1) / 16 - (x2 - x1) / 32 - (x2 - x1) / 80 + x1);

            NTKundliPlanetX9[2] = (HKUNDLISpace + (x2 - x1)
                    - (x2 - x1) / 16 - (x2 - x1) / 64 + x1);
            NTKundliPlanetX9[3] = (HKUNDLISpace + (x2 - x1)
                    - (x2 - x1) / 16 - (x2 - x1) / 32 - (x2 - x1) / 80 + x1);

            NTKundliPlanetX9[4] = (HKUNDLISpace + (x2 - x1)
                    - (x2 - x1) / 16 - (x2 - x1) / 64 + x1);
            NTKundliPlanetX9[5] = (HKUNDLISpace + (x2 - x1)
                    - (x2 - x1) / 16 - (x2 - x1) / 32 - (x2 - x1) / 80 + x1);

            NTKundliPlanetX9[6] = (HKUNDLISpace + (x2 - x1)
                    / 2 + (x2 - x1) / 4 + (x2 - x1) / 32 + x1);
            NTKundliPlanetX9[7] = (HKUNDLISpace + (x2 - x1)
                    / 2 + (x2 - x1) / 4 + (x2 - x1) / 32 + x1);

            NTKundliPlanetX9[8] = (HKUNDLISpace + (x2 - x1)
                    - (x2 - x1) / 8 - (x2 - x1) / 128 + x1);
            NTKundliPlanetX9[9] = (HKUNDLISpace + (x2 - x1)
                    / 2 + (x2 - x1) / 4 + (x2 - x1) / 8 - (x2 - x1) / 64 + x1);

            NTKundliPlanetX9[10] = (HKUNDLISpace + (x2 - x1)
                    / 2 + (x2 - x1) / 4 + (x2 - x1) / 8 - (x2 - x1) / 40 + x1);
            NTKundliPlanetX9[11] = (HKUNDLISpace + (x2 - x1)
                    / 2 + (x2 - x1) / 4 + (x2 - x1) / 8 - (x2 - x1) / 16 + x1);

            NTKundliPlanetY9[0] = (VKUNDLISpace + (y2 - y1)
                    / 2 + (y2 - y1) / 16 + (y2 - y1) / 128 + y1);
            NTKundliPlanetY9[1] = (VKUNDLISpace + (y2 - y1)
                    / 2 + (y2 - y1) / 8 - (y2 - y1) / 64 + y1);

            NTKundliPlanetY9[2] = (VKUNDLISpace + (y2 - y1)
                    / 2 + (y2 - y1) / 4 + (y2 - y1) / 32 + (y2 - y1) / 128 + y1);
            NTKundliPlanetY9[3] = (VKUNDLISpace + (y2 - y1)
                    / 2 + (y2 - y1) / 4 + (y2 - y1) / 16 + (y2 - y1) / 128 + y1);

            NTKundliPlanetY9[4] = (VKUNDLISpace + (y2 - y1)
                    / 2 + (y2 - y1) / 8 + (y2 - y1) / 32 + y1);
            NTKundliPlanetY9[5] = (VKUNDLISpace + (y2 - y1)
                    / 2 + (y2 - y1) / 4 - (y2 - y1) / 16 + y1);

            NTKundliPlanetY9[6] = (VKUNDLISpace + (y2 - y1)
                    / 2 + (y2 - y1) / 4 - (y2 - y1) / 16 + (y2 - y1) / 128 + y1);
            NTKundliPlanetY9[7] = (VKUNDLISpace + (y2 - y1)
                    / 2 + (y2 - y1) / 4 - (y2 - y1) / 32 + (y2 - y1) / 128 + y1);

            NTKundliPlanetY9[8] = (VKUNDLISpace + (y2 - y1)
                    / 2 + (y2 - y1) / 4 - (y2 - y1) / 32 + (y2 - y1) / 128 + y1);
            NTKundliPlanetY9[9] = (VKUNDLISpace + (y2 - y1)
                    / 2 + (y2 - y1) / 4 + (y2 - y1) / 128 + y1);

            NTKundliPlanetY9[10] = (VKUNDLISpace + (y2 - y1)
                    / 2 + (y2 - y1) / 8 + (y2 - y1) / 96 + y1);
            NTKundliPlanetY9[11] = (VKUNDLISpace + (y2 - y1)
                    / 2 + (y2 - y1) / 8 + (y2 - y1) / 32 + (y2 - y1) / 64 + y1);

            // INIT X10,Y10
            NTKundliPlanetX10[0] = (HKUNDLISpace + (x2 - x1)
                    / 2 + (x2 - x1) / 4 - (x2 - x1) / 32 + x1);
            NTKundliPlanetX10[1] = (HKUNDLISpace + (x2 - x1)
                    / 2 + (x2 - x1) / 4 - (x2 - x1) / 32 + x1);

            NTKundliPlanetX10[2] = (HKUNDLISpace + (x2 - x1)
                    / 2 + (x2 - x1) / 16 + x1);
            NTKundliPlanetX10[3] = (HKUNDLISpace + (x2 - x1)
                    / 2 + (x2 - x1) / 16 + x1);

            NTKundliPlanetX10[4] = (HKUNDLISpace + (x2 - x1)
                    / 2 + (x2 - x1) / 8 + x1);
            NTKundliPlanetX10[5] = (HKUNDLISpace + (x2 - x1)
                    / 2 + (x2 - x1) / 8 + x1);

            NTKundliPlanetX10[6] = (HKUNDLISpace + (x2 - x1)
                    / 2 + (x2 - x1) / 4 + (x2 - x1) / 8 - (x2 - x1) / 32
                    - (x2 - x1) / 64 + x1);
            NTKundliPlanetX10[7] = (HKUNDLISpace + (x2 - x1)
                    / 2 + (x2 - x1) / 4 + (x2 - x1) / 8 - (x2 - x1) / 32
                    - (x2 - x1) / 64 + x1);

            NTKundliPlanetX10[8] = (HKUNDLISpace + (x2 - x1)
                    / 2 + (x2 - x1) / 4 - (x2 - x1) / 32 + x1);
            NTKundliPlanetX10[9] = (HKUNDLISpace + (x2 - x1)
                    / 2 + (x2 - x1) / 4 - (x2 - x1) / 32 - (x2 - x1) / 64 + x1);

            NTKundliPlanetX10[10] = (HKUNDLISpace + (x2 - x1)
                    / 2 + (x2 - x1) / 4 + (x2 - x1) / 64 + x1);
            NTKundliPlanetX10[11] = (HKUNDLISpace + (x2 - x1)
                    / 2 + (x2 - x1) / 4 + (x2 - x1) / 64 + x1);

            NTKundliPlanetY10[0] = (VKUNDLISpace + (y2 - y1)
                    / 4 + (y2 - y1) / 24 + y1);
            NTKundliPlanetY10[1] = (VKUNDLISpace + (y2 - y1)
                    / 4 + (y2 - y1) / 12 + y1);

            NTKundliPlanetY10[2] = (VKUNDLISpace + (y2 - y1)
                    / 4 + (y2 - y1) / 4 - (y2 - y1) / 24 + y1);
            NTKundliPlanetY10[3] = (VKUNDLISpace + (y2 - y1)
                    / 4 + (y2 - y1) / 4);

            NTKundliPlanetY10[4] = (VKUNDLISpace + (y2 - y1)
                    / 4 + (y2 - y1) / 8 + (y2 - y1) / 64 + y1);
            NTKundliPlanetY10[5] = (VKUNDLISpace + (y2 - y1)
                    / 4 + (y2 - y1) / 8 + (y2 - y1) / 16 + y1);

            NTKundliPlanetY10[6] = (VKUNDLISpace + (y2 - y1)
                    / 4 + (y2 - y1) / 8 + (y2 - y1) / 64 + y1);
            NTKundliPlanetY10[7] = (VKUNDLISpace + (y2 - y1)
                    / 4 + (y2 - y1) / 8 + (y2 - y1) / 16 + y1);

            NTKundliPlanetY10[8] = (VKUNDLISpace + (y2 - y1)
                    / 4 + (y2 - y1) / 4 + (y2 - y1) / 16 + y1);
            NTKundliPlanetY10[9] = (VKUNDLISpace + (y2 - y1)
                    / 4 + (y2 - y1) / 4 + (y2 - y1) / 8 + y1);

            NTKundliPlanetY10[10] = (VKUNDLISpace + (y2 - y1)
                    / 4 + (y2 - y1) / 4 - (y2 - y1) / 24 + y1);
            NTKundliPlanetY10[11] = (VKUNDLISpace + (y2 - y1)
                    / 4 + (y2 - y1) / 4);

            // INIT X11,Y11
            NTKundliPlanetX11[0] = (HKUNDLISpace + (x2 - x1)
                    - (x2 - x1) / 16 - (x2 - x1) / 64 + x1);
            NTKundliPlanetX11[1] = (HKUNDLISpace + (x2 - x1)
                    - (x2 - x1) / 16 - (x2 - x1) / 32 - (x2 - x1) / 80 + x1);

            NTKundliPlanetX11[2] = (HKUNDLISpace + (x2 - x1)
                    - (x2 - x1) / 16 - (x2 - x1) / 64 + x1);
            NTKundliPlanetX11[3] = (HKUNDLISpace + (x2 - x1)
                    - (x2 - x1) / 16 - (x2 - x1) / 32 - (x2 - x1) / 80 + x1);

            NTKundliPlanetX11[4] = (HKUNDLISpace + (x2 - x1)
                    - (x2 - x1) / 16 - (x2 - x1) / 64 + x1);
            NTKundliPlanetX11[5] = (HKUNDLISpace + (x2 - x1)
                    - (x2 - x1) / 16 - (x2 - x1) / 32 - (x2 - x1) / 80 + x1);

            NTKundliPlanetX11[6] = (HKUNDLISpace + (x2 - x1)
                    / 2 + (x2 - x1) / 4 + (x2 - x1) / 32 + x1);
            NTKundliPlanetX11[7] = (HKUNDLISpace + (x2 - x1)
                    / 2 + (x2 - x1) / 4 + (x2 - x1) / 32 + x1);

            NTKundliPlanetX11[8] = (HKUNDLISpace + (x2 - x1)
                    - (x2 - x1) / 8 - (x2 - x1) / 128 + x1);
            NTKundliPlanetX11[9] = (HKUNDLISpace + (x2 - x1)
                    / 2 + (x2 - x1) / 4 + (x2 - x1) / 8 - (x2 - x1) / 64 + x1);

            NTKundliPlanetX11[10] = (HKUNDLISpace + (x2 - x1)
                    / 2 + (x2 - x1) / 4 + (x2 - x1) / 8 - (x2 - x1) / 40 + x1);
            NTKundliPlanetX11[11] = (HKUNDLISpace + (x2 - x1)
                    / 2 + (x2 - x1) / 4 + (x2 - x1) / 8 - (x2 - x1) / 16 + x1);

            NTKundliPlanetY11[0] = (VKUNDLISpace + (y2 - y1)
                    / 16 + (y2 - y1) / 128 + y1);
            NTKundliPlanetY11[1] = (VKUNDLISpace + (y2 - y1)
                    / 8 - (y2 - y1) / 64 + y1);

            NTKundliPlanetY11[2] = (VKUNDLISpace + (y2 - y1)
                    / 4 + (y2 - y1) / 32 + (y2 - y1) / 128 + y1);
            NTKundliPlanetY11[3] = (VKUNDLISpace + (y2 - y1)
                    / 4 + (y2 - y1) / 16 + (y2 - y1) / 128 + y1);

            NTKundliPlanetY11[4] = (VKUNDLISpace + (y2 - y1)
                    / 8 + (y2 - y1) / 32 + y1);
            NTKundliPlanetY11[5] = (VKUNDLISpace + (y2 - y1)
                    / 4 - (y2 - y1) / 16 + y1);

            NTKundliPlanetY11[6] = (VKUNDLISpace + (y2 - y1)
                    / 4 - (y2 - y1) / 16 + (y2 - y1) / 128 + y1);
            NTKundliPlanetY11[7] = (VKUNDLISpace + (y2 - y1)
                    / 4 - (y2 - y1) / 32 + (y2 - y1) / 128 + y1);

            NTKundliPlanetY11[8] = (VKUNDLISpace + (y2 - y1)
                    / 4 - (y2 - y1) / 32 + (y2 - y1) / 128 + y1);
            NTKundliPlanetY11[9] = (VKUNDLISpace + (y2 - y1)
                    / 4 + (y2 - y1) / 128 + y1);

            NTKundliPlanetY11[10] = (VKUNDLISpace + (y2 - y1)
                    / 8 + (y2 - y1) / 96 + y1);
            NTKundliPlanetY11[11] = (VKUNDLISpace + (y2 - y1)
                    / 8 + (y2 - y1) / 32 + (y2 - y1) / 64 + y1);

            // INIT X12,Y12
            NTKundliPlanetX12[0] = (HKUNDLISpace + (x2 - x1)
                    / 2 + (x2 - x1) / 8 - (x2 - x1) / 28 + x1);
            NTKundliPlanetX12[1] = (HKUNDLISpace + (x2 - x1)
                    / 2 + (x2 - x1) / 8 - (x2 - x1) / 28 + x1);

            NTKundliPlanetX12[2] = (HKUNDLISpace + (x2 - x1)
                    / 2 + (x2 - x1) / 4 + (x2 - x1) / 8 - (x2 - x1) / 23 + x1);
            NTKundliPlanetX12[3] = (HKUNDLISpace + (x2 - x1)
                    / 2 + (x2 - x1) / 4 + (x2 - x1) / 8 - (x2 - x1) / 23 + x1);

            NTKundliPlanetX12[4] = (HKUNDLISpace + (x2 - x1)
                    / 2 + (x2 - x1) / 8 + (x2 - x1) / 16 - (x2 - x1) / 48 + x1);
            NTKundliPlanetX12[5] = (HKUNDLISpace + (x2 - x1)
                    / 2 + (x2 - x1) / 8 + (x2 - x1) / 16 - (x2 - x1) / 48 + x1);

            NTKundliPlanetX12[6] = (HKUNDLISpace + (x2 - x1)
                    / 2 + (x2 - x1) / 4 + x1);
            NTKundliPlanetX12[7] = (HKUNDLISpace + (x2 - x1)
                    / 2 + (x2 - x1) / 4 + x1);

            NTKundliPlanetX12[8] = (HKUNDLISpace + (x2 - x1)
                    / 2 + (x2 - x1) / 4 - (x2 - x1) / 12 + x1);
            NTKundliPlanetX12[9] = (HKUNDLISpace + (x2 - x1)
                    / 2 + (x2 - x1) / 4 - (x2 - x1) / 12 + x1);

            NTKundliPlanetX12[10] = (HKUNDLISpace + (x2 - x1)
                    / 2 + (x2 - x1) / 4 - (x2 - x1) / 128 + x1);
            NTKundliPlanetX12[11] = (HKUNDLISpace + (x2 - x1)
                    / 2 + (x2 - x1) / 4 - (x2 - x1) / 128 + x1);

            NTKundliPlanetY12[0] = (VKUNDLISpace + y1);
            NTKundliPlanetY12[1] = (VKUNDLISpace + (y2 - y1)
                    / 16 - (y2 - y1) / 64 + y1);

            NTKundliPlanetY12[2] = (VKUNDLISpace + y1);
            NTKundliPlanetY12[3] = (VKUNDLISpace + (y2 - y1)
                    / 16 - (y2 - y1) / 64 + y1);

            NTKundliPlanetY12[4] = (VKUNDLISpace + (y2 - y1)
                    / 8 - (x2 - x1) / 32 + y1);
            NTKundliPlanetY12[5] = (VKUNDLISpace + (y2 - y1)
                    / 8 + (x2 - x1) / 128 + y1);

            NTKundliPlanetY12[6] = (VKUNDLISpace + y1);
            NTKundliPlanetY12[7] = (VKUNDLISpace + (y2 - y1)
                    / 16 - (y2 - y1) / 64 + y1);

            NTKundliPlanetY12[8] = (VKUNDLISpace + y1);
            NTKundliPlanetY12[9] = (VKUNDLISpace + (y2 - y1)
                    / 16 - (y2 - y1) / 64 + y1);

            NTKundliPlanetY12[10] = (VKUNDLISpace + (y2 - y1)
                    / 8 - (x2 - x1) / 32 + y1);
            NTKundliPlanetY12[11] = (VKUNDLISpace + (y2 - y1)
                    / 8 + (x2 - x1) / 128 + y1);

            // SOUTH INDIAN COORDINATES TEJINDER MAKE COMMENT FOR TED

            HKUNDLISpace = HKUNDLISpace - 8;
            // X1,Y1

            STKundliPlanetX1[0] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 3 + x1);
            STKundliPlanetX1[1] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 3 + x1);
            STKundliPlanetX1[2] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 3 + x1);
            STKundliPlanetX1[3] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 3 + x1);
            STKundliPlanetX1[4] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 4 + x1);
            STKundliPlanetX1[5] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 4 + x1);
            STKundliPlanetX1[6] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 4 + x1);
            STKundliPlanetX1[7] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 4 + x1);
            STKundliPlanetX1[8] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 5 + x1);
            STKundliPlanetX1[9] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 5 + x1);
            STKundliPlanetX1[10] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 5 + x1);
            STKundliPlanetX1[11] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 5 + x1);

            STKundliPlanetY1[0] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 0 + y1);
            STKundliPlanetY1[1] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 1 + y1);
            STKundliPlanetY1[2] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 2 + y1);
            STKundliPlanetY1[3] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 3 + y1);
            STKundliPlanetY1[4] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 0 + y1);
            STKundliPlanetY1[5] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 1 + y1);
            STKundliPlanetY1[6] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 2 + y1);
            STKundliPlanetY1[7] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 3 + y1);
            STKundliPlanetY1[8] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 0 + y1);
            STKundliPlanetY1[9] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 1 + y1);
            STKundliPlanetY1[10] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 2 + y1);
            STKundliPlanetY1[11] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 3 + y1);

            // X2,Y2

            STKundliPlanetX2[0] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 6 + x1);
            STKundliPlanetX2[1] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 6 + x1);
            STKundliPlanetX2[2] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 6 + x1);
            STKundliPlanetX2[3] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 6 + x1);
            STKundliPlanetX2[4] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 7 + x1);
            STKundliPlanetX2[5] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 7 + x1);
            STKundliPlanetX2[6] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 7 + x1);
            STKundliPlanetX2[7] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 7 + x1);
            STKundliPlanetX2[8] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 8 + x1);
            STKundliPlanetX2[9] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 8 + x1);
            STKundliPlanetX2[10] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 8 + x1);
            STKundliPlanetX2[11] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 8 + x1);

            STKundliPlanetY2[0] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 0 + y1);
            STKundliPlanetY2[1] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 1 + y1);
            STKundliPlanetY2[2] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 2 + y1);
            STKundliPlanetY2[3] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 3 + y1);
            STKundliPlanetY2[4] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 0 + y1);
            STKundliPlanetY2[5] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 1 + y1);
            STKundliPlanetY2[6] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 2 + y1);
            STKundliPlanetY2[7] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 3 + y1);
            STKundliPlanetY2[8] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 0 + y1);
            STKundliPlanetY2[9] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 1 + y1);
            STKundliPlanetY2[10] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 2 + y1);
            STKundliPlanetY2[11] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 3 + y1);

            // X3,Y3

            STKundliPlanetX3[0] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 9 + x1);
            STKundliPlanetX3[1] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 9 + x1);
            STKundliPlanetX3[2] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 9 + x1);
            STKundliPlanetX3[3] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 9 + x1);
            STKundliPlanetX3[4] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 10 + x1);
            STKundliPlanetX3[5] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 10 + x1);
            STKundliPlanetX3[6] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 10 + x1);
            STKundliPlanetX3[7] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 10 + x1);
            STKundliPlanetX3[8] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 11 + x1);
            STKundliPlanetX3[9] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 11 + x1);
            STKundliPlanetX3[10] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 11 + x1);
            STKundliPlanetX3[11] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 11 + x1);

            STKundliPlanetY3[0] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 0 + y1);
            STKundliPlanetY3[1] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 1 + y1);
            STKundliPlanetY3[2] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 2 + y1);
            STKundliPlanetY3[3] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 3 + y1);
            STKundliPlanetY3[4] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 0 + y1);
            STKundliPlanetY3[5] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 1 + y1);
            STKundliPlanetY3[6] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 2 + y1);
            STKundliPlanetY3[7] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 3 + y1);
            STKundliPlanetY3[8] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 0 + y1);
            STKundliPlanetY3[9] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 1 + y1);
            STKundliPlanetY3[10] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 2 + y1);
            STKundliPlanetY3[11] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 3 + y1);

            // X4,Y4

            STKundliPlanetX4[0] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 9 + x1);
            STKundliPlanetX4[1] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 9 + x1);
            STKundliPlanetX4[2] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 9 + x1);
            STKundliPlanetX4[3] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 9 + x1);
            STKundliPlanetX4[4] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 10 + x1);
            STKundliPlanetX4[5] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 10 + x1);
            STKundliPlanetX4[6] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 10 + x1);
            STKundliPlanetX4[7] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 10 + x1);
            STKundliPlanetX4[8] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 11 + x1);
            STKundliPlanetX4[9] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 11 + x1);
            STKundliPlanetX4[10] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 11 + x1);
            STKundliPlanetX4[11] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 11 + x1);

            STKundliPlanetY4[0] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 4 + y1);
            STKundliPlanetY4[1] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 5 + y1);
            STKundliPlanetY4[2] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 6 + y1);
            STKundliPlanetY4[3] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 7 + y1);
            STKundliPlanetY4[4] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 4 + y1);
            STKundliPlanetY4[5] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 5 + y1);
            STKundliPlanetY4[6] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 6 + y1);
            STKundliPlanetY4[7] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 7 + y1);
            STKundliPlanetY4[8] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 4 + y1);
            STKundliPlanetY4[9] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 5 + y1);
            STKundliPlanetY4[10] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 6 + y1);
            STKundliPlanetY4[11] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 7 + y1);

            // X5,Y5

            STKundliPlanetX5[0] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 9 + x1);
            STKundliPlanetX5[1] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 9 + x1);
            STKundliPlanetX5[2] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 9 + x1);
            STKundliPlanetX5[3] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 9 + x1);
            STKundliPlanetX5[4] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 10 + x1);
            STKundliPlanetX5[5] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 10 + x1);
            STKundliPlanetX5[6] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 10 + x1);
            STKundliPlanetX5[7] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 10 + x1);
            STKundliPlanetX5[8] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 11 + x1);
            STKundliPlanetX5[9] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 11 + x1);
            STKundliPlanetX5[10] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 11 + x1);
            STKundliPlanetX9[11] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 11 + x1);

            STKundliPlanetY5[0] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 8 + y1);
            STKundliPlanetY5[1] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 9 + y1);
            STKundliPlanetY5[2] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 10 + y1);
            STKundliPlanetY5[3] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 11 + y1);
            STKundliPlanetY5[4] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 8 + y1);
            STKundliPlanetY5[5] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 9 + y1);
            STKundliPlanetY5[6] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 10 + y1);
            STKundliPlanetY5[7] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 11 + y1);
            STKundliPlanetY5[8] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 8 + y1);
            STKundliPlanetY5[9] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 9 + y1);
            STKundliPlanetY5[10] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 10 + y1);
            STKundliPlanetY5[11] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 11 + y1);

            // X6,Y6

            STKundliPlanetX6[0] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 9 + x1);
            STKundliPlanetX6[1] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 9 + x1);
            STKundliPlanetX6[2] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 9 + x1);
            STKundliPlanetX6[3] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 9 + x1);
            STKundliPlanetX6[4] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 10 + x1);
            STKundliPlanetX6[5] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 10 + x1);
            STKundliPlanetX6[6] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 10 + x1);
            STKundliPlanetX6[7] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 10 + x1);
            STKundliPlanetX6[8] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 11 + x1);
            STKundliPlanetX6[9] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 11 + x1);
            STKundliPlanetX6[10] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 11 + x1);
            STKundliPlanetX6[11] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 11 + x1);

            STKundliPlanetY6[0] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 12 + y1);
            STKundliPlanetY6[1] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 13 + y1);
            STKundliPlanetY6[2] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 14 + y1);
            STKundliPlanetY6[3] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 15 + y1);
            STKundliPlanetY6[4] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 12 + y1);
            STKundliPlanetY6[5] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 13 + y1);
            STKundliPlanetY6[6] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 14 + y1);
            STKundliPlanetY6[7] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 15 + y1);
            STKundliPlanetY6[8] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 12 + y1);
            STKundliPlanetY6[9] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 13 + y1);
            STKundliPlanetY6[10] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 14 + y1);
            STKundliPlanetY6[11] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 15 + y1);

            // X7,Y7
            STKundliPlanetX7[0] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 6 + x1);
            STKundliPlanetX7[1] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 6 + x1);
            STKundliPlanetX7[2] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 6 + x1);
            STKundliPlanetX7[3] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 6 + x1);
            STKundliPlanetX7[4] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 7 + x1);
            STKundliPlanetX7[5] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 7 + x1);
            STKundliPlanetX7[6] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 7 + x1);
            STKundliPlanetX7[7] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 7 + x1);
            STKundliPlanetX7[8] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 8 + x1);
            STKundliPlanetX7[9] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 8 + x1);
            STKundliPlanetX7[10] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 8 + x1);
            STKundliPlanetX7[11] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 8 + x1);

            STKundliPlanetY7[0] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 12 + y1);
            STKundliPlanetY7[1] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 13 + y1);
            STKundliPlanetY7[2] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 14 + y1);
            STKundliPlanetY7[3] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 15 + y1);
            STKundliPlanetY7[4] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 12 + y1);
            STKundliPlanetY7[5] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 13 + y1);
            STKundliPlanetY7[6] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 14 + y1);
            STKundliPlanetY7[7] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 15 + y1);
            STKundliPlanetY7[8] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 12 + y1);
            STKundliPlanetY7[9] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 13 + y1);
            STKundliPlanetY7[10] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 14 + y1);
            STKundliPlanetY7[11] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 15 + y1);

            // X8,Y8

            STKundliPlanetX8[0] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 3 + x1);
            STKundliPlanetX8[1] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 3 + x1);
            STKundliPlanetX8[2] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 3 + x1);
            STKundliPlanetX8[3] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 3 + x1);
            STKundliPlanetX8[4] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 4 + x1);
            STKundliPlanetX8[5] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 4 + x1);
            STKundliPlanetX8[6] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 4 + x1);
            STKundliPlanetX8[7] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 4 + x1);
            STKundliPlanetX8[8] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 5 + x1);
            STKundliPlanetX8[9] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 5 + x1);
            STKundliPlanetX8[10] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 5 + x1);
            STKundliPlanetX8[11] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 5 + x1);

            STKundliPlanetY8[0] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 12 + y1);
            STKundliPlanetY8[1] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 13 + y1);
            STKundliPlanetY8[2] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 14 + y1);
            STKundliPlanetY8[3] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 15 + y1);
            STKundliPlanetY8[4] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 12 + y1);
            STKundliPlanetY8[5] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 13 + y1);
            STKundliPlanetY8[6] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 14 + y1);
            STKundliPlanetY8[7] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 15 + y1);
            STKundliPlanetY8[8] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 12 + y1);
            STKundliPlanetY8[9] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 13 + y1);
            STKundliPlanetY8[10] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 14 + y1);
            STKundliPlanetY8[11] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 15 + y1);

            // X9,Y9

            STKundliPlanetX9[0] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 0 + x1);
            STKundliPlanetX9[1] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 0 + x1);
            STKundliPlanetX9[2] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 0 + x1);
            STKundliPlanetX9[3] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 0 + x1);
            STKundliPlanetX9[4] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 1 + x1);
            STKundliPlanetX9[5] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 1 + x1);
            STKundliPlanetX9[6] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 1 + x1);
            STKundliPlanetX9[7] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 1 + x1);
            STKundliPlanetX9[8] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 2 + x1);
            STKundliPlanetX9[9] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 2 + x1);
            STKundliPlanetX9[10] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 2 + x1);
            STKundliPlanetX9[11] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 2 + x1);

            STKundliPlanetY9[0] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 12 + y1);
            STKundliPlanetY9[1] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 13 + y1);
            STKundliPlanetY9[2] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 14 + y1);
            STKundliPlanetY9[3] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 15 + y1);
            STKundliPlanetY9[4] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 12 + y1);
            STKundliPlanetY9[5] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 13 + y1);
            STKundliPlanetY9[6] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 14 + y1);
            STKundliPlanetY9[7] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 15 + y1);
            STKundliPlanetY9[8] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 12 + y1);
            STKundliPlanetY9[9] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 13 + y1);
            STKundliPlanetY9[10] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 14 + y1);
            STKundliPlanetY9[11] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 15 + y1);

            // INIT X10,Y10
            STKundliPlanetX10[0] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 0 + x1);
            STKundliPlanetX10[1] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 0 + x1);
            STKundliPlanetX10[2] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 0 + x1);
            STKundliPlanetX10[3] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 0 + x1);
            STKundliPlanetX10[4] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 1 + x1);
            STKundliPlanetX10[5] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 1 + x1);
            STKundliPlanetX10[6] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 1 + x1);
            STKundliPlanetX10[7] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 1 + x1);
            STKundliPlanetX10[8] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 2 + x1);
            STKundliPlanetX10[9] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 2 + x1);
            STKundliPlanetX10[10] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 2 + x1);
            STKundliPlanetX10[11] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 2 + x1);

            STKundliPlanetY10[0] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 8 + y1);
            STKundliPlanetY10[1] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 9 + y1);
            STKundliPlanetY10[2] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 10 + y1);
            STKundliPlanetY10[3] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 11 + y1);
            STKundliPlanetY10[4] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 8 + y1);
            STKundliPlanetY10[5] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 9 + y1);
            STKundliPlanetY10[6] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 10 + y1);
            STKundliPlanetY10[7] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 11 + y1);
            STKundliPlanetY10[8] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 8 + y1);
            STKundliPlanetY10[9] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 9 + y1);
            STKundliPlanetY10[10] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 10 + y1);
            STKundliPlanetY10[11] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 11 + y1);

            // X11,Y11

            STKundliPlanetX11[0] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 0 + x1);
            STKundliPlanetX11[1] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 0 + x1);
            STKundliPlanetX11[2] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 0 + x1);
            STKundliPlanetX11[3] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 0 + x1);
            STKundliPlanetX11[4] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 1 + x1);
            STKundliPlanetX11[5] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 1 + x1);
            STKundliPlanetX11[6] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 1 + x1);
            STKundliPlanetX11[7] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 1 + x1);
            STKundliPlanetX11[8] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 2 + x1);
            STKundliPlanetX11[9] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 2 + x1);
            STKundliPlanetX11[10] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 2 + x1);
            STKundliPlanetX11[11] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 2 + x1);

            STKundliPlanetY11[0] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 4 + y1);
            STKundliPlanetY11[1] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 5 + y1);
            STKundliPlanetY11[2] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 6 + y1);
            STKundliPlanetY11[3] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 7 + y1);
            STKundliPlanetY11[4] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 4 + y1);
            STKundliPlanetY11[5] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 5 + y1);
            STKundliPlanetY11[6] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 6 + y1);
            STKundliPlanetY11[7] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 7 + y1);
            STKundliPlanetY11[8] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 4 + y1);
            STKundliPlanetY11[9] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 5 + y1);
            STKundliPlanetY11[10] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 6 + y1);
            STKundliPlanetY11[11] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 7 + y1);

            // INIT X12,Y12
            STKundliPlanetX12[0] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 0 + x1);
            STKundliPlanetX12[1] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 0 + x1);
            STKundliPlanetX12[2] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 0 + x1);
            STKundliPlanetX12[3] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 0 + x1);
            STKundliPlanetX12[4] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 1 + x1);
            STKundliPlanetX12[5] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 1 + x1);
            STKundliPlanetX12[6] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 1 + x1);
            STKundliPlanetX12[7] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 1 + x1);
            STKundliPlanetX12[8] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 2 + x1);
            STKundliPlanetX12[9] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 2 + x1);
            STKundliPlanetX12[10] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 2 + x1);
            STKundliPlanetX12[11] = (HKUNDLISpace
                    + ((x2 - x1) / 12) * 2 + x1);

            STKundliPlanetY12[0] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 0 + y1);
            STKundliPlanetY12[1] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 1 + y1);
            STKundliPlanetY12[2] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 2 + y1);
            STKundliPlanetY12[3] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 3 + y1);
            STKundliPlanetY12[4] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 0 + y1);
            STKundliPlanetY12[5] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 1 + y1);
            STKundliPlanetY12[6] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 2 + y1);
            STKundliPlanetY12[7] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 3 + y1);
            STKundliPlanetY12[8] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 0 + y1);
            STKundliPlanetY12[9] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 1 + y1);
            STKundliPlanetY12[10] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 2 + y1);
            STKundliPlanetY12[11] = (VKUNDLISpace
                    + ((y2 - y1) / 16) * 3 + y1);

            // East

         /* // X1,Y1

                  ESKundliPlanetX1[0] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 3 + x1+61);
                  ESKundliPlanetX1[1] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 3 + x1+61);
                  ESKundliPlanetX1[2] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 3 + x1+61);
                  ESKundliPlanetX1[3] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 3 + x1+61);
                  ESKundliPlanetX1[4] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 4 + x1+73);
                  ESKundliPlanetX1[5] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 4 + x1+73);
                  ESKundliPlanetX1[6] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 4 + x1+73);
                  ESKundliPlanetX1[7] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 4 + x1+73);
                  ESKundliPlanetX1[8] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 5 + x1+76);
                  ESKundliPlanetX1[9] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 5 + x1+76);
                  ESKundliPlanetX1[10] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 5 + x1+76);
                  ESKundliPlanetX1[11] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 5 + x1+76);

                  ESKundliPlanetY1[0] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 0 + y1+11);
                  ESKundliPlanetY1[1] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 1 + y1+11);
                  ESKundliPlanetY1[2] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 2 + y1+11);
                  ESKundliPlanetY1[3] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 3 + y1+11);
                  ESKundliPlanetY1[4] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 0 + y1+11);
                  ESKundliPlanetY1[5] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 1 + y1+11);
                  ESKundliPlanetY1[6] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 2 + y1+11);
                  ESKundliPlanetY1[7] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 3 + y1+11);
                  ESKundliPlanetY1[8] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 0 + y1+11);
                  ESKundliPlanetY1[9] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 1 + y1+11);
                  ESKundliPlanetY1[10] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 2 + y1+11);
                  ESKundliPlanetY1[11] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 3 + y1+11);



         ///x12
         ESKundliPlanetX2[0] = (HKUNDLISpace
               + ((x2 - x1) / 12) * 0 + x1+70);
         ESKundliPlanetX2[1] = (HKUNDLISpace
               + ((x2 - x1) / 12) * 0 + x1+70);
         ESKundliPlanetX2[2] = (HKUNDLISpace
               + ((x2 - x1) / 12) * 1 + x1+100);
         ESKundliPlanetX2[3] = (HKUNDLISpace
               + ((x2 - x1) / 12) * 1 + x1+100);
         ESKundliPlanetX2[4] = (HKUNDLISpace
               + ((x2 - x1) / 12) * 1 + x1+80);
         ESKundliPlanetX2[5] = (HKUNDLISpace
               + ((x2 - x1) / 12) * 1 + x1+80);
         ESKundliPlanetX2[6] = (HKUNDLISpace
               + ((x2 - x1) / 12) * 1 + x1+61);
         ESKundliPlanetX2[7] = (HKUNDLISpace
               + ((x2 - x1) / 12) * 1 + x1+61);
         ESKundliPlanetX2[8] = (HKUNDLISpace
               + ((x2 - x1) / 12) * 2 + x1+61);
         ESKundliPlanetX2[9] = (HKUNDLISpace
               + ((x2 - x1) / 12) * 2 + x1+61);
         ESKundliPlanetX2[10] = (HKUNDLISpace
               + ((x2 - x1) / 12) * 2 + x1+61);
         ESKundliPlanetX2[11] = (HKUNDLISpace
               + ((x2 - x1) / 12) * 2 + x1+61);

         ESKundliPlanetY2[0] = (VKUNDLISpace
               + ((y2 - y1) / 16) * 0 + y1 );
         ESKundliPlanetY2[1] = (VKUNDLISpace
               + ((y2 - y1) / 16) * 1 + y1 );
         ESKundliPlanetY2[2] = (VKUNDLISpace
               + ((y2 - y1) / 16) * 2 + y1 );
         ESKundliPlanetY2[3] = (VKUNDLISpace
               + ((y2 - y1) / 16) * 3 + y1 );
         ESKundliPlanetY2[4] = (VKUNDLISpace
               + ((y2 - y1) / 16) * 0 + y1 );
         ESKundliPlanetY2[5] = (VKUNDLISpace
               + ((y2 - y1) / 16) * 1 + y1 );
         ESKundliPlanetY2[6] = (VKUNDLISpace
               + ((y2 - y1) / 16) * 2 + y1 );
         ESKundliPlanetY2[7] = (VKUNDLISpace
               + ((y2 - y1) / 16) * 3 + y1 );
         ESKundliPlanetY2[8] = (VKUNDLISpace
               + ((y2 - y1) / 16) * 0 + y1 );
         ESKundliPlanetY2[9] = (VKUNDLISpace
               + ((y2 - y1) / 16) * 1 + y1 );
         ESKundliPlanetY2[10] = (VKUNDLISpace
               + ((y2 - y1) / 16) * 2 + y1 );
         ESKundliPlanetY2[11] = (VKUNDLISpace
               + ((y2 - y1) / 16) * 3 + y1 );

         //
         // INIT X10,Y10
                  ESKundliPlanetX4[0] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 0 + x1+11);
                  ESKundliPlanetX4[1] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 0 + x1+11);
                  ESKundliPlanetX4[2] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 0 + x1+11);
                  ESKundliPlanetX4[3] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 0 + x1+11);
                  ESKundliPlanetX4[4] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 1 + x1+31);
                  ESKundliPlanetX4[5] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 1 + x1+31);
                  ESKundliPlanetX4[6] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 1 + x1+31);
                  ESKundliPlanetX4[7] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 1 + x1+31);
                  ESKundliPlanetX4[8] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 2 + x1+61);
                  ESKundliPlanetX4[9] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 2 + x1+61);
                  ESKundliPlanetX4[10] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 2 + x1+61);
                  ESKundliPlanetX4[11] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 2 + x1+61);

                  ESKundliPlanetY4[0] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 8 + y1-81);
                  ESKundliPlanetY4[1] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 9 + y1-81);
                  ESKundliPlanetY4[2] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 10 + y1-81);
                  ESKundliPlanetY4[3] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 11 + y1-81);
                  ESKundliPlanetY4[4] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 8 + y1-81);
                  ESKundliPlanetY4[5] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 9 + y1-81);
                  ESKundliPlanetY4[6] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 10 + y1-81);
                  ESKundliPlanetY4[7] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 11 + y1-81);
                  ESKundliPlanetY4[8] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 8 + y1-81);
                  ESKundliPlanetY4[9] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 9 + y1-81);
                  ESKundliPlanetY4[10] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 10 + y1-81);
                  ESKundliPlanetY4[11] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 11 + y1-81);

                  ////x2


                  ESKundliPlanetX12[0] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 6 + x1+61);
                  ESKundliPlanetX12[1] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 6 + x1+61);
                  ESKundliPlanetX12[2] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 6 + x1+61);
                  ESKundliPlanetX12[3] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 6 + x1+61);
                  ESKundliPlanetX12[4] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 7 + x1+61);
                  ESKundliPlanetX12[5] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 7 + x1+61);
                  ESKundliPlanetX12[6] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 7 + x1+61);
                  ESKundliPlanetX12[7] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 7 + x1+61);
                  ESKundliPlanetX12[8] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 8 + x1+61);
                  ESKundliPlanetX12[9] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 8 + x1+61);
                  ESKundliPlanetX12[10] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 8 + x1+61);
                  ESKundliPlanetX12[11] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 8 + x1+61);

                  ESKundliPlanetY12[0] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 0 + y1+61);
                  ESKundliPlanetY12[1] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 1 + y1+61);
                  ESKundliPlanetY12[2] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 2 + y1+61);
                  ESKundliPlanetY12[3] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 3 + y1+61);
                  ESKundliPlanetY12[4] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 0 + y1+61);
                  ESKundliPlanetY12[5] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 1 + y1+61);
                  ESKundliPlanetY12[6] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 2 + y1+61);
                  ESKundliPlanetY12[7] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 3 + y1+61);
                  ESKundliPlanetY12[8] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 0 + y1+61);
                  ESKundliPlanetY12[9] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 1 + y1+61);
                  ESKundliPlanetY12[10] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 2 + y1+61);
                  ESKundliPlanetY12[11] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 3 + y1+61);

                  // X11,Y11

                  ESKundliPlanetX3[0] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 0 + x1 );
                  ESKundliPlanetX3[1] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 0 + x1 );
                  ESKundliPlanetX3[2] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 0 + x1);
                  ESKundliPlanetX3[3] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 0 + x1 );
                  ESKundliPlanetX3[4] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 1 + x1+15);
                  ESKundliPlanetX3[5] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 1 + x1+15 );
                  ESKundliPlanetX3[6] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 1 + x1 );
                  ESKundliPlanetX3[7] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 1 + x1 );
                  ESKundliPlanetX3[8] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 2 + x1 );
                  ESKundliPlanetX3[9] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 2 + x1 );
                  ESKundliPlanetX3[10] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 2 + x1+61);
                  ESKundliPlanetX3[11] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 2 + x1+61);


                  ESKundliPlanetY3[0] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 0 + y1+61);
                  ESKundliPlanetY3[1] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 1 + y1+61);
                  ESKundliPlanetY3[2] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 2 + y1+61);
                  ESKundliPlanetY3[3] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 3 + y1+61);
                  ESKundliPlanetY3[4] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 2 + y1+61);
                  ESKundliPlanetY3[5] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 3 + y1+61);
                  ESKundliPlanetY3[6] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 0 + y1+81);
                  ESKundliPlanetY3[7] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 1 + y1+81);
                  ESKundliPlanetY3[8] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 2 + y1+21);
                  ESKundliPlanetY3[9] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 3 + y1+21);
                  ESKundliPlanetY3[10] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 2 + y1+61);
                  ESKundliPlanetY3[11] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 3 + y1+61);



                  // X6,Y6

                  ESKundliPlanetX8[0] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 8 + x1 );
                  ESKundliPlanetX8[1] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 8 + x1 );
                  ESKundliPlanetX8[2] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 8 + x1 );
                  ESKundliPlanetX8[3] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 8 + x1 );
                  ESKundliPlanetX8[4] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 9 + x1 );
                  ESKundliPlanetX8[5] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 9 + x1 );
                  ESKundliPlanetX8[6] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 9 + x1 );
                  ESKundliPlanetX8[7] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 9 + x1 );
                  ESKundliPlanetX8[8] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 10 + x1 );
                  ESKundliPlanetX8[9] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 11 + x1 );
                  ESKundliPlanetX8[10] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 11 + x1 );
                  ESKundliPlanetX8[11] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 11 + x1 );

                  ESKundliPlanetY8[0] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 12 + y1 );
                  ESKundliPlanetY8[1] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 13 + y1 );
                  ESKundliPlanetY8[2] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 14 + y1 );
                  ESKundliPlanetY8[3] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 15 + y1 );
                  ESKundliPlanetY8[4] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 12 + y1 );
                  ESKundliPlanetY8[5] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 13 + y1 );
                  ESKundliPlanetY8[6] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 14 + y1 );
                  ESKundliPlanetY8[7] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 15 + y1 );
                  ESKundliPlanetY8[8] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 12 + y1 );
                  ESKundliPlanetY8[9] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 13 + y1 );
                  ESKundliPlanetY8[10] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 14 + y1 );
                  ESKundliPlanetY8[11] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 15 + y1 );

                  // X7,Y7
                  ESKundliPlanetX7[0] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 4 + x1 );
                  ESKundliPlanetX7[1] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 4 + x1 );
                  ESKundliPlanetX7[2] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 4 + x1 );
                  ESKundliPlanetX7[3] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 4 + x1 );
                  ESKundliPlanetX7[4] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 5+ x1+15 );
                  ESKundliPlanetX7[5] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 5 + x1+15 );
                  ESKundliPlanetX7[6] = (HKUNDLISpace
                        + ((x2 - x1) / 12) *5 + x1+15 );
                  ESKundliPlanetX7[7] = (HKUNDLISpace
                        + ((x2 - x1) / 12) *5 + x1+15 );
                  ESKundliPlanetX7[8] = (HKUNDLISpace
                        + ((x2 - x1) / 12) *6+ x1+25 );
                  ESKundliPlanetX7[9] = (HKUNDLISpace
                        + ((x2 - x1) / 12) *6 + x1 +25);
                  ESKundliPlanetX7[10] = (HKUNDLISpace
                        + ((x2 - x1) / 12) *6+ x1+25 );
                  ESKundliPlanetX7[11] = (HKUNDLISpace
                        + ((x2 - x1) / 12) *6+ x1+25 );

                  ESKundliPlanetY7[0] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 12 + y1-61);
                  ESKundliPlanetY7[1] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 13 + y1-61);
                  ESKundliPlanetY7[2] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 14 + y1-61);
                  ESKundliPlanetY7[3] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 15 + y1-61);
                  ESKundliPlanetY7[4] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 12 + y1-61);
                  ESKundliPlanetY7[5] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 13 + y1-61);
                  ESKundliPlanetY7[6] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 14 + y1-61);
                  ESKundliPlanetY7[7] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 15 + y1-61);
                  ESKundliPlanetY7[8] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 12 + y1-61);
                  ESKundliPlanetY7[9] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 13 + y1-61);
                  ESKundliPlanetY7[10] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 14 + y1-61);
                  ESKundliPlanetY7[11] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 15 + y1-61);

                  // X8,Y8

                  ESKundliPlanetX6[0] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 3 + x1+11);
                  ESKundliPlanetX6[1] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 3 + x1-31);
                  ESKundliPlanetX6[2] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 3 + x1-11 );
                  ESKundliPlanetX6[3] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 3 + x1 -11);
                  ESKundliPlanetX6[4] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 2 + x1-51 );
                  ESKundliPlanetX6[5] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 2 + x1-51 );
                  ESKundliPlanetX6[6] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 2 + x1 );
                  ESKundliPlanetX6[7] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 2 + x1 );
                  ESKundliPlanetX6[8] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 1 + x1-21 );
                  ESKundliPlanetX6[9] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 1 + x1-21 );
                  ESKundliPlanetX6[10] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 3 + x1-5 );
                  ESKundliPlanetX6[11] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 3 + x1-5 );

                  ESKundliPlanetY6[0] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 12 + y1-31 );
                  ESKundliPlanetY6[1] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 13 + y1-31 );
                  ESKundliPlanetY6[2] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 14 + y1 );
                  ESKundliPlanetY6[3] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 15 + y1 );
                  ESKundliPlanetY6[4] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 14 + y1 );
                  ESKundliPlanetY6[5] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 15 + y1 );
                  ESKundliPlanetY6[6] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 14 + y1-21 );
                  ESKundliPlanetY6[7] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 15 + y1-21 );
                  ESKundliPlanetY6[8] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 14+ y1+10 );
                  ESKundliPlanetY6[9] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 15 + y1+10 );
                  ESKundliPlanetY6[10] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 12 + y1+25 );
                  ESKundliPlanetY6[11] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 13 + y1+25 );

                  // X9,Y9

                  ESKundliPlanetX5[0] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 0 + x1+11);
                  ESKundliPlanetX5[1] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 0 + x1+11);
                  ESKundliPlanetX5[2] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 0 + x1+11);
                  ESKundliPlanetX5[3] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 0 + x1+11);
                  ESKundliPlanetX5[4] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 1 + x1+31);
                  ESKundliPlanetX5[5] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 1 + x1+31);
                  ESKundliPlanetX5[6] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 1 + x1+21);
                  ESKundliPlanetX5[7] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 1 + x1+21);
                  ESKundliPlanetX5[8] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 2 + x1+11);
                  ESKundliPlanetX5[9] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 2 + x1+11);
                  ESKundliPlanetX5[10] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 0 + x1 );
                  ESKundliPlanetX5[11] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 0 + x1 );

                  ESKundliPlanetY5[0] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 12 + y1-61);
                  ESKundliPlanetY5[1] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 13 + y1-61);
                  ESKundliPlanetY5[2] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 14 + y1-61);
                  ESKundliPlanetY5[3] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 15 + y1-61);
                  ESKundliPlanetY5[4] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 12 + y1-61);
                  ESKundliPlanetY5[5] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 13 + y1-61);
                  ESKundliPlanetY5[6] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 14 + y1-81);
                  ESKundliPlanetY5[7] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 15 + y1-101);
                  ESKundliPlanetY5[8] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 12 + y1-61);
                  ESKundliPlanetY5[9] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 13 + y1-61);
                  ESKundliPlanetY5[10] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 16 + y1-81);
                  ESKundliPlanetY5[11] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 17 + y1-81 );


                  // X3,Y3

                  ESKundliPlanetX11[0] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 9 + x1+61);
                  ESKundliPlanetX11[1] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 9 + x1+61);
                  ESKundliPlanetX11[2] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 9 + x1+61);
                  ESKundliPlanetX11[3] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 9 + x1+61);
                  ESKundliPlanetX11[4] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 10 + x1+61);
                  ESKundliPlanetX11[5] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 10 + x1+61);
                  ESKundliPlanetX11[6] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 10 + x1+61);
                  ESKundliPlanetX11[7] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 10 + x1+61);
                  ESKundliPlanetX11[8] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 11 + x1+61);
                  ESKundliPlanetX11[9] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 11 + x1+61);
                  ESKundliPlanetX11[10] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 11 + x1+61);
                  ESKundliPlanetX11[11] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 11 + x1+61);

                  ESKundliPlanetY11[0] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 0 + y1+61);
                  ESKundliPlanetY11[1] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 1 + y1+61);
                  ESKundliPlanetY11[2] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 2 + y1+61);
                  ESKundliPlanetY11[3] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 3 + y1+61);
                  ESKundliPlanetY11[4] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 0 + y1+61);
                  ESKundliPlanetY11[5] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 1 + y1+61);
                  ESKundliPlanetY11[6] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 2 + y1+61);
                  ESKundliPlanetY11[7] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 3 + y1+61);
                  ESKundliPlanetY11[8] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 0 + y1+61);
                  ESKundliPlanetY11[9] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 1 + y1+61);
                  ESKundliPlanetY11[10] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 2 + y1+61);
                  ESKundliPlanetY11[11] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 3 + y1+61);

                  // X4,Y4

                  ESKundliPlanetX10[0] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 9 + x1+61);
                  ESKundliPlanetX10[1] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 9 + x1+61);
                  ESKundliPlanetX10[2] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 9 + x1+61);
                  ESKundliPlanetX10[3] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 9 + x1+61);
                  ESKundliPlanetX10[4] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 10 + x1+61);
                  ESKundliPlanetX10[5] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 10 + x1+61);
                  ESKundliPlanetX10[6] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 10 + x1+61);
                  ESKundliPlanetX10[7] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 10 + x1+61);
                  ESKundliPlanetX10[8] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 11 + x1+61);
                  ESKundliPlanetX10[9] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 11 + x1+61);
                  ESKundliPlanetX10[10] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 11 + x1+61);
                  ESKundliPlanetX10[11] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 11 + x1+61);

                  ESKundliPlanetY10[0] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 4 + y1+61);
                  ESKundliPlanetY10[1] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 5 + y1+61);
                  ESKundliPlanetY10[2] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 6 + y1+61);
                  ESKundliPlanetY10[3] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 7 + y1+61);
                  ESKundliPlanetY10[4] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 4 + y1+61);
                  ESKundliPlanetY10[5] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 5 + y1+61);
                  ESKundliPlanetY10[6] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 6 + y1+61);
                  ESKundliPlanetY10[7] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 7 + y1+61);
                  ESKundliPlanetY10[8] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 4 + y1+61);
                  ESKundliPlanetY10[9] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 5 + y1+61);
                  ESKundliPlanetY10[10] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 6 + y1+61);
                  ESKundliPlanetY10[11] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 7 + y1+61);

                  // X5,Y5

                  ESKundliPlanetX9[0] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 9 + x1+61);
                  ESKundliPlanetX9[1] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 9 + x1+61);
                  ESKundliPlanetX9[2] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 9 + x1+61);
                  ESKundliPlanetX9[3] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 9 + x1+61);
                  ESKundliPlanetX9[4] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 10 + x1+61);
                  ESKundliPlanetX9[5] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 10 + x1+61);
                  ESKundliPlanetX9[6] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 10 + x1+61);
                  ESKundliPlanetX9[7] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 10 + x1+61);
                  ESKundliPlanetX9[8] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 11 + x1+61);
                  ESKundliPlanetX9[9] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 11 + x1+61);
                  ESKundliPlanetX9[10] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 11 + x1+61);
                  ESKundliPlanetX9[11] = (HKUNDLISpace
                        + ((x2 - x1) / 12) * 11 + x1+61);

                  ESKundliPlanetY9[0] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 8 + y1+61);
                  ESKundliPlanetY9[1] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 9 + y1+61);
                  ESKundliPlanetY9[2] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 10 + y1+61);
                  ESKundliPlanetY9[3] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 11 + y1+61);
                  ESKundliPlanetY9[4] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 8 + y1+61);
                  ESKundliPlanetY9[5] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 9 + y1+61);
                  ESKundliPlanetY9[6] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 10 + y1+61);
                  ESKundliPlanetY9[7] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 11 + y1+61);
                  ESKundliPlanetY9[8] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 8 + y1+61);
                  ESKundliPlanetY9[9] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 9 + y1+61);
                  ESKundliPlanetY9[10] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 10 + y1+61);
                  ESKundliPlanetY9[11] = (VKUNDLISpace
                        + ((y2 - y1) / 16) * 11 + y1+61);





*/
        } catch (Exception e) {

        }


    }

    private void initSouthKundliCoordinate() {


        // X1,Y1
        SKundliPlanetX1[0] = (HKUNDLISpace + 80.00f)
                * SPACE_FACTOR;
        SKundliPlanetX1[1] = (HKUNDLISpace + 72.00f)
                * SPACE_FACTOR;
        SKundliPlanetX1[2] = (HKUNDLISpace + 97.00f)
                * SPACE_FACTOR;
        SKundliPlanetX1[3] = (HKUNDLISpace + 90.00f)
                * SPACE_FACTOR;
        SKundliPlanetX1[4] = (HKUNDLISpace + 87.00f)
                * SPACE_FACTOR;

        SKundliPlanetX1[5] = (HKUNDLISpace + 70.00f)
                * SPACE_FACTOR;

        SKundliPlanetY1[0] = (VKUNDLISpace + 40.00f)
                * SPACE_FACTOR;
        SKundliPlanetY1[1] = (VKUNDLISpace + 29.00f)
                * SPACE_FACTOR;
        SKundliPlanetY1[2] = (VKUNDLISpace + 25.00f)
                * SPACE_FACTOR;
        SKundliPlanetY1[3] = (VKUNDLISpace + 12.00f)
                * SPACE_FACTOR;
        SKundliPlanetY1[4] = (VKUNDLISpace + 49.00f)
                * SPACE_FACTOR;
        SKundliPlanetY1[5] = (VKUNDLISpace + 12.00f)
                * SPACE_FACTOR;

        // X2,Y2
        SKundliPlanetX2[0] = (HKUNDLISpace + 138.00f)
                * SPACE_FACTOR;
        SKundliPlanetX2[1] = (HKUNDLISpace + 118.00f)
                * SPACE_FACTOR;
        SKundliPlanetX2[2] = (HKUNDLISpace + 155.00f)
                * SPACE_FACTOR;
        SKundliPlanetX2[3] = (HKUNDLISpace + 148.00f)
                * SPACE_FACTOR;
        SKundliPlanetX2[4] = (HKUNDLISpace + 145.00f)
                * SPACE_FACTOR;
        SKundliPlanetX2[5] = (HKUNDLISpace + 128.00f)
                * SPACE_FACTOR;

        SKundliPlanetY2[0] = (VKUNDLISpace + 35.00f)
                * SPACE_FACTOR;
        SKundliPlanetY2[1] = (VKUNDLISpace + 29.00f)
                * SPACE_FACTOR;
        SKundliPlanetY2[2] = (VKUNDLISpace + 25.00f)
                * SPACE_FACTOR;
        SKundliPlanetY2[3] = (VKUNDLISpace + 12.00f)
                * SPACE_FACTOR;
        SKundliPlanetY2[4] = (VKUNDLISpace + 49.00f)
                * SPACE_FACTOR;
        SKundliPlanetY2[5] = (VKUNDLISpace + 12.00f)
                * SPACE_FACTOR;

        // X3,Y3
        SKundliPlanetX3[0] = (HKUNDLISpace + 196.00f)
                * SPACE_FACTOR;
        SKundliPlanetX3[1] = (HKUNDLISpace + 180.00f)
                * SPACE_FACTOR;
        SKundliPlanetX3[2] = (HKUNDLISpace + 213.00f)
                * SPACE_FACTOR;
        SKundliPlanetX3[3] = (HKUNDLISpace + 210.00f)
                * SPACE_FACTOR;
        SKundliPlanetX3[4] = (HKUNDLISpace + 215.00f)
                * SPACE_FACTOR;
        SKundliPlanetX3[5] = (HKUNDLISpace + 182.00f)
                * SPACE_FACTOR;

        SKundliPlanetY3[0] = (VKUNDLISpace + 40.00f)
                * SPACE_FACTOR;
        SKundliPlanetY3[1] = (VKUNDLISpace + 29.00f)
                * SPACE_FACTOR;
        SKundliPlanetY3[2] = (VKUNDLISpace + 25.00f)
                * SPACE_FACTOR;
        SKundliPlanetY3[3] = (VKUNDLISpace + 12.00f)
                * SPACE_FACTOR;
        SKundliPlanetY3[4] = (VKUNDLISpace + 49.00f)
                * SPACE_FACTOR;
        SKundliPlanetY3[5] = (VKUNDLISpace + 12.00f)
                * SPACE_FACTOR;

        // X4,Y4
        SKundliPlanetX4[0] = (HKUNDLISpace + 190.00f)
                * SPACE_FACTOR;
        SKundliPlanetX4[1] = (HKUNDLISpace + 180.00f)
                * SPACE_FACTOR;
        SKundliPlanetX4[2] = (HKUNDLISpace + 213.00f)
                * SPACE_FACTOR;
        SKundliPlanetX4[3] = (HKUNDLISpace + 210.00f)
                * SPACE_FACTOR;
        SKundliPlanetX4[4] = (HKUNDLISpace + 215.00f)
                * SPACE_FACTOR;
        SKundliPlanetX4[5] = (HKUNDLISpace + 182.00f)
                * SPACE_FACTOR;

        SKundliPlanetY4[0] = (VKUNDLISpace + 105.00f)
                * SPACE_FACTOR;
        SKundliPlanetY4[1] = (VKUNDLISpace + 90.00f)
                * SPACE_FACTOR;
        SKundliPlanetY4[2] = (VKUNDLISpace + 90.00f)
                * SPACE_FACTOR;
        SKundliPlanetY4[3] = (VKUNDLISpace + 70.00f)
                * SPACE_FACTOR;
        SKundliPlanetY4[4] = (VKUNDLISpace + 110.00f)
                * SPACE_FACTOR;
        SKundliPlanetY4[5] = (VKUNDLISpace + 70.00f)
                * SPACE_FACTOR;

        // X5,Y5
        SKundliPlanetX5[0] = (HKUNDLISpace + 190.00f)
                * SPACE_FACTOR;
        SKundliPlanetX5[1] = (HKUNDLISpace + 180.00f)
                * SPACE_FACTOR;
        SKundliPlanetX5[2] = (HKUNDLISpace + 213.00f)
                * SPACE_FACTOR;
        SKundliPlanetX5[3] = (HKUNDLISpace + 210.00f)
                * SPACE_FACTOR;
        SKundliPlanetX5[4] = (HKUNDLISpace + 215.00f)
                * SPACE_FACTOR;
        SKundliPlanetX5[5] = (HKUNDLISpace + 182.00f)
                * SPACE_FACTOR;

        SKundliPlanetY5[0] = (VKUNDLISpace + 165.00f)
                * SPACE_FACTOR;
        SKundliPlanetY5[1] = (VKUNDLISpace + 150.00f)
                * SPACE_FACTOR;
        SKundliPlanetY5[2] = (VKUNDLISpace + 150.00f)
                * SPACE_FACTOR;
        SKundliPlanetY5[3] = (VKUNDLISpace + 130.00f)
                * SPACE_FACTOR;
        SKundliPlanetY5[4] = (VKUNDLISpace + 170.00f)
                * SPACE_FACTOR;
        SKundliPlanetY5[5] = (VKUNDLISpace + 130.00f)
                * SPACE_FACTOR;

        // X6,Y6
        SKundliPlanetX6[0] = (HKUNDLISpace + 195.00f)
                * SPACE_FACTOR;
        SKundliPlanetX6[1] = (HKUNDLISpace + 185.00f)
                * SPACE_FACTOR;
        SKundliPlanetX6[2] = (HKUNDLISpace + 218.00f)
                * SPACE_FACTOR;
        SKundliPlanetX6[3] = (HKUNDLISpace + 215.00f)
                * SPACE_FACTOR;
        SKundliPlanetX6[4] = (HKUNDLISpace + 220.00f)
                * SPACE_FACTOR;
        SKundliPlanetX6[5] = (HKUNDLISpace + 187.00f)
                * SPACE_FACTOR;

        SKundliPlanetY6[0] = (VKUNDLISpace + 220.00f)
                * SPACE_FACTOR;
        SKundliPlanetY6[1] = (VKUNDLISpace + 208.00f)
                * SPACE_FACTOR;
        SKundliPlanetY6[2] = (VKUNDLISpace + 202.00f)
                * SPACE_FACTOR;
        SKundliPlanetY6[3] = (VKUNDLISpace + 190.00f)
                * SPACE_FACTOR;
        SKundliPlanetY6[4] = (VKUNDLISpace + 220.00f)
                * SPACE_FACTOR;
        SKundliPlanetY6[5] = (VKUNDLISpace + 190.00f)
                * SPACE_FACTOR;

        // X7,Y7
        SKundliPlanetX7[0] = (HKUNDLISpace + 130.00f)
                * SPACE_FACTOR;
        SKundliPlanetX7[1] = (HKUNDLISpace + 122.00f)
                * SPACE_FACTOR;
        SKundliPlanetX7[2] = (HKUNDLISpace + 155.00f)
                * SPACE_FACTOR;
        SKundliPlanetX7[3] = (HKUNDLISpace + 152.00f)
                * SPACE_FACTOR;
        SKundliPlanetX7[4] = (HKUNDLISpace + 154.00f)
                * SPACE_FACTOR;
        SKundliPlanetX7[5] = (HKUNDLISpace + 124.00f)
                * SPACE_FACTOR;

        SKundliPlanetY7[0] = (VKUNDLISpace + 222.00f)
                * SPACE_FACTOR;
        SKundliPlanetY7[1] = (VKUNDLISpace + 208.00f)
                * SPACE_FACTOR;
        SKundliPlanetY7[2] = (VKUNDLISpace + 202.00f)
                * SPACE_FACTOR;
        SKundliPlanetY7[3] = (VKUNDLISpace + 190.00f)
                * SPACE_FACTOR;
        SKundliPlanetY7[4] = (VKUNDLISpace + 220.00f)
                * SPACE_FACTOR;
        SKundliPlanetY7[5] = (VKUNDLISpace + 190.00f)
                * SPACE_FACTOR;

        // X8,Y8
        SKundliPlanetX8[0] = (HKUNDLISpace + 72.00f)
                * SPACE_FACTOR;
        SKundliPlanetX8[1] = (HKUNDLISpace + 64.00f)
                * SPACE_FACTOR;
        SKundliPlanetX8[2] = (HKUNDLISpace + 97.00f)
                * SPACE_FACTOR;
        SKundliPlanetX8[3] = (HKUNDLISpace + 94.00f)
                * SPACE_FACTOR;
        SKundliPlanetX8[4] = (HKUNDLISpace + 96.00f)
                * SPACE_FACTOR;
        SKundliPlanetX8[5] = (HKUNDLISpace + 66.00f)
                * SPACE_FACTOR;

        SKundliPlanetY8[0] = (VKUNDLISpace + 222.00f)
                * SPACE_FACTOR;
        SKundliPlanetY8[1] = (VKUNDLISpace + 208.00f)
                * SPACE_FACTOR;
        SKundliPlanetY8[2] = (VKUNDLISpace + 202.00f)
                * SPACE_FACTOR;
        SKundliPlanetY8[3] = (VKUNDLISpace + 190.00f)
                * SPACE_FACTOR;
        SKundliPlanetY8[4] = (VKUNDLISpace + 220.00f)
                * SPACE_FACTOR;
        SKundliPlanetY8[5] = (VKUNDLISpace + 190.00f)
                * SPACE_FACTOR;

        // X9,Y9
        SKundliPlanetX9[0] = (HKUNDLISpace + 10.00f)
                * SPACE_FACTOR;
        SKundliPlanetX9[1] = (HKUNDLISpace + 6.00f)
                * SPACE_FACTOR;
        SKundliPlanetX9[2] = (HKUNDLISpace + 35.00f)
                * SPACE_FACTOR;
        SKundliPlanetX9[3] = (HKUNDLISpace + 36.00f)
                * SPACE_FACTOR;
        SKundliPlanetX9[4] = (HKUNDLISpace + 35.00f)
                * SPACE_FACTOR;
        SKundliPlanetX9[5] = (HKUNDLISpace + 8.00f)
                * SPACE_FACTOR;

        SKundliPlanetY9[0] = (VKUNDLISpace + 222.00f)
                * SPACE_FACTOR;
        SKundliPlanetY9[1] = (VKUNDLISpace + 208.00f)
                * SPACE_FACTOR;
        SKundliPlanetY9[2] = (VKUNDLISpace + 202.00f)
                * SPACE_FACTOR;
        SKundliPlanetY9[3] = (VKUNDLISpace + 190.00f)
                * SPACE_FACTOR;
        SKundliPlanetY9[4] = (VKUNDLISpace + 220.00f)
                * SPACE_FACTOR;
        SKundliPlanetY9[5] = (VKUNDLISpace + 190.00f)
                * SPACE_FACTOR;

        // X10,Y10
        SKundliPlanetX10[0] = (HKUNDLISpace + 10.00f)
                * SPACE_FACTOR;
        SKundliPlanetX10[1] = (HKUNDLISpace + 6.00f)
                * SPACE_FACTOR;
        SKundliPlanetX10[2] = (HKUNDLISpace + 35.00f)
                * SPACE_FACTOR;
        SKundliPlanetX10[3] = (HKUNDLISpace + 36.00f)
                * SPACE_FACTOR;
        SKundliPlanetX10[4] = (HKUNDLISpace + 35.00f)
                * SPACE_FACTOR;
        SKundliPlanetX10[5] = (HKUNDLISpace + 8.00f)
                * SPACE_FACTOR;

        SKundliPlanetY10[0] = (VKUNDLISpace + 164.00f)
                * SPACE_FACTOR;
        SKundliPlanetY10[1] = (VKUNDLISpace + 150.00f)
                * SPACE_FACTOR;
        SKundliPlanetY10[2] = (VKUNDLISpace + 144.00f)
                * SPACE_FACTOR;
        SKundliPlanetY10[3] = (VKUNDLISpace + 132.00f)
                * SPACE_FACTOR;
        SKundliPlanetY10[4] = (VKUNDLISpace + 162.00f)
                * SPACE_FACTOR;
        SKundliPlanetY10[5] = (VKUNDLISpace + 132.00f)
                * SPACE_FACTOR;

        // X11,Y11
        SKundliPlanetX11[0] = (HKUNDLISpace + 10.00f)
                * SPACE_FACTOR;
        SKundliPlanetX11[1] = (HKUNDLISpace + 6.00f)
                * SPACE_FACTOR;
        SKundliPlanetX11[2] = (HKUNDLISpace + 35.00f)
                * SPACE_FACTOR;
        SKundliPlanetX11[3] = (HKUNDLISpace + 36.00f)
                * SPACE_FACTOR;
        SKundliPlanetX11[4] = (HKUNDLISpace + 35.00f)
                * SPACE_FACTOR;
        SKundliPlanetX11[5] = (HKUNDLISpace + 8.00f)
                * SPACE_FACTOR;

        SKundliPlanetY11[0] = (VKUNDLISpace + 106.00f)
                * SPACE_FACTOR;
        SKundliPlanetY11[1] = (VKUNDLISpace + 92.00f)
                * SPACE_FACTOR;
        SKundliPlanetY11[2] = (VKUNDLISpace + 86.00f)
                * SPACE_FACTOR;
        SKundliPlanetY11[3] = (VKUNDLISpace + 74.00f)
                * SPACE_FACTOR;
        SKundliPlanetY11[4] = (VKUNDLISpace + 104.00f)
                * SPACE_FACTOR;
        SKundliPlanetY11[5] = (VKUNDLISpace + 74.00f)
                * SPACE_FACTOR;

        // X12,Y12
        SKundliPlanetX12[0] = (HKUNDLISpace + 10.00f)
                * SPACE_FACTOR;
        SKundliPlanetX12[1] = (HKUNDLISpace + 6.00f)
                * SPACE_FACTOR;
        SKundliPlanetX12[2] = (HKUNDLISpace + 35.00f)
                * SPACE_FACTOR;
        SKundliPlanetX12[3] = (HKUNDLISpace + 36.00f)
                * SPACE_FACTOR;
        SKundliPlanetX12[4] = (HKUNDLISpace + 35.00f)
                * SPACE_FACTOR;
        SKundliPlanetX12[5] = (HKUNDLISpace + 8.00f)
                * SPACE_FACTOR;

        SKundliPlanetY12[0] = (VKUNDLISpace + 48.00f)
                * SPACE_FACTOR;
        SKundliPlanetY12[1] = (VKUNDLISpace + 34.00f)
                * SPACE_FACTOR;
        SKundliPlanetY12[2] = (VKUNDLISpace + 28.00f)
                * SPACE_FACTOR;
        SKundliPlanetY12[3] = (VKUNDLISpace + 12.00f)
                * SPACE_FACTOR;
        SKundliPlanetY12[4] = (VKUNDLISpace + 40.00f)
                * SPACE_FACTOR;
        SKundliPlanetY12[5] = (VKUNDLISpace + 12.00f)
                * SPACE_FACTOR;


    }

    //@TEJINDER SINGh
    //this method initilize kundli corrdinates

    private void initEastKundliCoordinate() {


        // X1,Y1
        EKundliPlanetX1[0] = (HKUNDLISpace + 80.00f + 25.0f)
                * SPACE_FACTOR;
        EKundliPlanetX1[1] = (HKUNDLISpace + 60.00f + 29.0f)
                * SPACE_FACTOR;
        EKundliPlanetX1[2] = (HKUNDLISpace + 97.00f + 25.0f)
                * SPACE_FACTOR;
        EKundliPlanetX1[3] = (HKUNDLISpace + 90.00f + 28.0f)
                * SPACE_FACTOR;
        EKundliPlanetX1[4] = (HKUNDLISpace + 87.00f + 25.0f)
                * SPACE_FACTOR;

        EKundliPlanetX1[5] = (HKUNDLISpace + 70.00f + 25.0f)
                * SPACE_FACTOR;

        EKundliPlanetY1[0] = (VKUNDLISpace + 35.00f)
                * SPACE_FACTOR;
        EKundliPlanetY1[1] = (VKUNDLISpace + 29.00f)
                * SPACE_FACTOR;
        EKundliPlanetY1[2] = (VKUNDLISpace + 25.00f)
                * SPACE_FACTOR;
        EKundliPlanetY1[3] = (VKUNDLISpace + 12.00f)
                * SPACE_FACTOR;
        EKundliPlanetY1[4] = (VKUNDLISpace + 49.00f)
                * SPACE_FACTOR;
        EKundliPlanetY1[5] = (VKUNDLISpace + 12.00f)
                * SPACE_FACTOR;

        // X2,Y2
        EKundliPlanetX12[0] = (HKUNDLISpace + 138.00f + 36.0f)
                * SPACE_FACTOR;
        EKundliPlanetX12[1] = (HKUNDLISpace + 120.00f + 46.0f)
                * SPACE_FACTOR;
        EKundliPlanetX12[2] = (HKUNDLISpace + 155.00f + 32.0f)
                * SPACE_FACTOR;
        EKundliPlanetX12[3] = (HKUNDLISpace + 148.00f + 44.0f)
                * SPACE_FACTOR;
        EKundliPlanetX12[4] = (HKUNDLISpace + 145.00f + 19.0f)
                * SPACE_FACTOR;
        EKundliPlanetX12[5] = (HKUNDLISpace + 128.00f + 40.0f)
                * SPACE_FACTOR;

        EKundliPlanetY12[0] = (VKUNDLISpace + 32.00f)
                * SPACE_FACTOR;
        EKundliPlanetY12[1] = (VKUNDLISpace + 16.00f)
                * SPACE_FACTOR;
        EKundliPlanetY12[2] = (VKUNDLISpace + 20.00f)
                * SPACE_FACTOR;
        EKundliPlanetY12[3] = (VKUNDLISpace + 7.00f)
                * SPACE_FACTOR;
        EKundliPlanetY12[4] = (VKUNDLISpace + 43.00f)
                * SPACE_FACTOR;
        EKundliPlanetY12[5] = (VKUNDLISpace + 7.00f)
                * SPACE_FACTOR;

        // X3,Y3
        EKundliPlanetX11[0] = (HKUNDLISpace + 196.00f + 7.0f)
                * SPACE_FACTOR;
        EKundliPlanetX11[1] = (HKUNDLISpace + 180.00f + 2.0f)
                * SPACE_FACTOR;
        EKundliPlanetX11[2] = (HKUNDLISpace + 219.00f)
                * SPACE_FACTOR;
        EKundliPlanetX11[3] = (HKUNDLISpace + 210.00f + 9.0f)
                * SPACE_FACTOR;
        EKundliPlanetX11[4] = (HKUNDLISpace + 222.00f)
                * SPACE_FACTOR;
        EKundliPlanetX11[5] = (HKUNDLISpace + 182.00f + 19.0f)
                * SPACE_FACTOR;

        EKundliPlanetY11[0] = (VKUNDLISpace + 40.00f + 5.0f)
                * SPACE_FACTOR;
        EKundliPlanetY11[1] = (VKUNDLISpace + 29.00f + 34.0f)
                * SPACE_FACTOR;
        EKundliPlanetY11[2] = (VKUNDLISpace + 27.00f)
                * SPACE_FACTOR;
        EKundliPlanetY11[3] = (VKUNDLISpace + 12.00f + 48.0f)
                * SPACE_FACTOR;
        EKundliPlanetY11[4] = (VKUNDLISpace + 49.00f - 5.0f)
                * SPACE_FACTOR;
        EKundliPlanetY11[5] = (VKUNDLISpace + 12.00f + 48.0f)
                * SPACE_FACTOR;

        // X4,Y4
        EKundliPlanetX10[0] = (HKUNDLISpace + 190.00f - 10.0f)
                * SPACE_FACTOR;
        EKundliPlanetX10[1] = (HKUNDLISpace + 180.00f - 10.0f)
                * SPACE_FACTOR;
        EKundliPlanetX10[2] = (HKUNDLISpace + 213.00f - 10.0f)
                * SPACE_FACTOR;
        EKundliPlanetX10[3] = (HKUNDLISpace + 210.00f - 10.0f)
                * SPACE_FACTOR;
        EKundliPlanetX10[4] = (HKUNDLISpace + 215.00f - 10.0f)
                * SPACE_FACTOR;
        EKundliPlanetX10[5] = (HKUNDLISpace + 182.00f - 10.0f)
                * SPACE_FACTOR;

        EKundliPlanetY10[0] = (VKUNDLISpace + 105.00f + 27.0f)
                * SPACE_FACTOR;
        EKundliPlanetY10[1] = (VKUNDLISpace + 90.00f + 27.0f)
                * SPACE_FACTOR;
        EKundliPlanetY10[2] = (VKUNDLISpace + 90.00f + 27.0f)
                * SPACE_FACTOR;
        EKundliPlanetY10[3] = (VKUNDLISpace + 70.00f + 27.0f)
                * SPACE_FACTOR;
        EKundliPlanetY10[4] = (VKUNDLISpace + 110.00f + 27.0f)
                * SPACE_FACTOR;
        EKundliPlanetY10[5] = (VKUNDLISpace + 70.00f + 27.0f)
                * SPACE_FACTOR;

        // X5,Y5
        EKundliPlanetX9[0] = (HKUNDLISpace + 190.00f + 15.0f)
                * SPACE_FACTOR;
        EKundliPlanetX9[1] = (HKUNDLISpace + 180.00f + 13.0f)
                * SPACE_FACTOR;
        EKundliPlanetX9[2] = (HKUNDLISpace + 211.00f)
                * SPACE_FACTOR;
        EKundliPlanetX9[3] = (HKUNDLISpace + 210.00f)
                * SPACE_FACTOR;
        EKundliPlanetX9[4] = (HKUNDLISpace + 215.00f + 2.0f)
                * SPACE_FACTOR;
        EKundliPlanetX9[5] = (HKUNDLISpace + 182.00f)
                * SPACE_FACTOR;

        EKundliPlanetY9[0] = (VKUNDLISpace + 165.00f + 32.0f)
                * SPACE_FACTOR;
        EKundliPlanetY9[1] = (VKUNDLISpace + 150.00f + 33.0f)
                * SPACE_FACTOR;
        EKundliPlanetY9[2] = (VKUNDLISpace + 150.00f + 35.0f)
                * SPACE_FACTOR;
        EKundliPlanetY9[3] = (VKUNDLISpace + 130.00f + 42.0f)
                * SPACE_FACTOR;
        EKundliPlanetY9[4] = (VKUNDLISpace + 170.00f + 37.0f)
                * SPACE_FACTOR;
        EKundliPlanetY9[5] = (VKUNDLISpace + 130.00f + 42.0f)
                * SPACE_FACTOR;

        // X6,Y6
        EKundliPlanetX8[0] = (HKUNDLISpace + 190.00f - 15.0f)
                * SPACE_FACTOR;
        EKundliPlanetX8[1] = (HKUNDLISpace + 180.00f - 15.0f)
                * SPACE_FACTOR;
        EKundliPlanetX8[2] = (HKUNDLISpace + 213.00f - 27.0f)
                * SPACE_FACTOR;
        EKundliPlanetX8[3] = (HKUNDLISpace + 210.00f - 46.0f)
                * SPACE_FACTOR;
        EKundliPlanetX8[4] = (HKUNDLISpace + 215.00f - 17.0f)
                * SPACE_FACTOR;
        //mo
        EKundliPlanetX8[5] = (HKUNDLISpace + 182.00f - 13.0f)
                * SPACE_FACTOR;

        EKundliPlanetY8[0] = (VKUNDLISpace + 220.00f + 3.0f)
                * SPACE_FACTOR;

        EKundliPlanetY8[1] = (VKUNDLISpace + 208.00f + 3.0f)
                * SPACE_FACTOR;
        EKundliPlanetY8[2] = (VKUNDLISpace + 202.00f + 8.0f)
                * SPACE_FACTOR;
        EKundliPlanetY8[3] = (VKUNDLISpace + 180.00f + 7.0f)
                * SPACE_FACTOR;
        //mo
        EKundliPlanetY8[4] = (VKUNDLISpace + 220.00f + 4.0f)
                * SPACE_FACTOR;
        //ve
        EKundliPlanetY8[5] = (VKUNDLISpace + 190.00f + 8.0f)
                * SPACE_FACTOR;

        // X7,Y7
        EKundliPlanetX7[0] = (HKUNDLISpace + 130.00f - 25.0f)
                * SPACE_FACTOR;
        EKundliPlanetX7[1] = (HKUNDLISpace + 122.00f - 25.0f)
                * SPACE_FACTOR;
        EKundliPlanetX7[2] = (HKUNDLISpace + 155.00f - 25.0f)
                * SPACE_FACTOR;
        EKundliPlanetX7[3] = (HKUNDLISpace + 152.00f - 25.0f)
                * SPACE_FACTOR;
        EKundliPlanetX7[4] = (HKUNDLISpace + 154.00f - 25.0f)
                * SPACE_FACTOR;
        EKundliPlanetX7[5] = (HKUNDLISpace + 124.00f - 25.0f)
                * SPACE_FACTOR;

        EKundliPlanetY7[0] = (VKUNDLISpace + 222.00f - 10.0f)
                * SPACE_FACTOR;
        EKundliPlanetY7[1] = (VKUNDLISpace + 208.00f - 10.0f)
                * SPACE_FACTOR;
        EKundliPlanetY7[2] = (VKUNDLISpace + 202.00f - 10.0f)
                * SPACE_FACTOR;
        EKundliPlanetY7[3] = (VKUNDLISpace + 190.00f - 10.0f)
                * SPACE_FACTOR;
        EKundliPlanetY7[4] = (VKUNDLISpace + 220.00f - 10.0f)
                * SPACE_FACTOR;
        EKundliPlanetY7[5] = (VKUNDLISpace + 190.00f - 10.0f)
                * SPACE_FACTOR;

        // X8,Y8
        EKundliPlanetX6[0] = (HKUNDLISpace + 72.00f - 48.0f)
                * SPACE_FACTOR;
        EKundliPlanetX6[1] = (HKUNDLISpace + 64.00f - 31.0f)
                * SPACE_FACTOR;
        EKundliPlanetX6[2] = (HKUNDLISpace + 97.00f - 46.0f)
                * SPACE_FACTOR;
        EKundliPlanetX6[3] = (HKUNDLISpace + 94.00f - 40.0f)
                * SPACE_FACTOR;
        EKundliPlanetX6[4] = (HKUNDLISpace + 96.00f - 40.0f)
                * SPACE_FACTOR;
        EKundliPlanetX6[5] = (HKUNDLISpace + 66.00f - 20.0f)
                * SPACE_FACTOR;

        EKundliPlanetY6[0] = (VKUNDLISpace + 222.00f + 1.0f)
                * SPACE_FACTOR;
        EKundliPlanetY6[1] = (VKUNDLISpace + 208.00f + 4.0f)
                * SPACE_FACTOR;
        EKundliPlanetY6[2] = (VKUNDLISpace + 202.00f + 10.0f)
                * SPACE_FACTOR;
        EKundliPlanetY6[3] = (VKUNDLISpace + 190.00f)
                * SPACE_FACTOR;
        EKundliPlanetY6[4] = (VKUNDLISpace + 220.00f + 2.0f)
                * SPACE_FACTOR;
        EKundliPlanetY6[5] = (VKUNDLISpace + 190.00f + 10.0f)
                * SPACE_FACTOR;

        // X9,Y9
        EKundliPlanetX5[0] = (HKUNDLISpace + 1.00f)
                * SPACE_FACTOR;
        EKundliPlanetX5[1] = (HKUNDLISpace + 10.00f)
                * SPACE_FACTOR;
        EKundliPlanetX5[2] = (HKUNDLISpace + 35.00f)
                * SPACE_FACTOR;
        EKundliPlanetX5[3] = (HKUNDLISpace + 36.00f - 15.0f)
                * SPACE_FACTOR;
        EKundliPlanetX5[4] = (HKUNDLISpace + 35.00f - 32.0f)
                * SPACE_FACTOR;
        EKundliPlanetX5[5] = (HKUNDLISpace + 8.00f)
                * SPACE_FACTOR;

        EKundliPlanetY5[0] = (VKUNDLISpace + 222.00f - 17.0f)
                * SPACE_FACTOR;
        EKundliPlanetY5[1] = (VKUNDLISpace + 208.00f - 14.0f)
                * SPACE_FACTOR;
        EKundliPlanetY5[2] = (VKUNDLISpace + 202.00f - 33.0f)
                * SPACE_FACTOR;
        EKundliPlanetY5[3] = (VKUNDLISpace + 190.00f - 10.0f)
                * SPACE_FACTOR;
        EKundliPlanetY5[4] = (VKUNDLISpace + 220.00f - 40.0f)
                * SPACE_FACTOR;
        EKundliPlanetY5[5] = (VKUNDLISpace + 190.00f - 20.0f)
                * SPACE_FACTOR;

        // X10,Y10
        EKundliPlanetX4[0] = (HKUNDLISpace + 10.00f)
                * SPACE_FACTOR;
        EKundliPlanetX4[1] = (HKUNDLISpace + 6.00f + 10.0f)
                * SPACE_FACTOR;
        EKundliPlanetX4[2] = (HKUNDLISpace + 35.00f)
                * SPACE_FACTOR;
        EKundliPlanetX4[3] = (HKUNDLISpace + 36.00f + 5.0f)
                * SPACE_FACTOR;
        EKundliPlanetX4[4] = (HKUNDLISpace + 35.00f + 10.0f)
                * SPACE_FACTOR;
        EKundliPlanetX4[5] = (HKUNDLISpace + 8.00f + 10.0f)
                * SPACE_FACTOR;

        EKundliPlanetY4[0] = (VKUNDLISpace + 164.00f - 20.0f)
                * SPACE_FACTOR;
        EKundliPlanetY4[1] = (VKUNDLISpace + 150.00f - 30.0f)
                * SPACE_FACTOR;
        EKundliPlanetY4[2] = (VKUNDLISpace + 144.00f - 50.0f)
                * SPACE_FACTOR;
        EKundliPlanetY4[3] = (VKUNDLISpace + 132.00f - 20.0f)
                * SPACE_FACTOR;
        EKundliPlanetY4[4] = (VKUNDLISpace + 162.00f - 30.0f)
                * SPACE_FACTOR;
        EKundliPlanetY4[5] = (VKUNDLISpace + 132.00f - 30.0f)
                * SPACE_FACTOR;

        // X11,Y11
        EKundliPlanetX3[0] = (HKUNDLISpace + 6.00f)
                * SPACE_FACTOR;
        EKundliPlanetX3[1] = (HKUNDLISpace + 4.00f)
                * SPACE_FACTOR;
        EKundliPlanetX3[2] = (HKUNDLISpace + 35.00f - 14.0f)
                * SPACE_FACTOR;
        EKundliPlanetX3[3] = (HKUNDLISpace + 36.00f - 14.0f)
                * SPACE_FACTOR;
        EKundliPlanetX3[4] = (HKUNDLISpace + 35.00f + 5.0f)
                * SPACE_FACTOR;
        EKundliPlanetX3[5] = (HKUNDLISpace + 2.00f)
                * SPACE_FACTOR;

        EKundliPlanetY3[0] = (VKUNDLISpace + 106.00f - 45.0f)
                * SPACE_FACTOR;
        EKundliPlanetY3[1] = (VKUNDLISpace + 92.00f - 50.0f)
                * SPACE_FACTOR;
        //ma
        EKundliPlanetY3[2] = (VKUNDLISpace + 86.00f - 27.0f)
                * SPACE_FACTOR;
        //me
        EKundliPlanetY3[3] = (VKUNDLISpace + 74.00f - 29.0f)
                * SPACE_FACTOR;
        EKundliPlanetY3[4] = (VKUNDLISpace + 104.00f - 45.0f)
                * SPACE_FACTOR;
        EKundliPlanetY3[5] = (VKUNDLISpace + 74.00f - 47.0f)
                * SPACE_FACTOR;

        // X12,Y12
        EKundliPlanetX2[0] = (HKUNDLISpace + 10.00f + 35.0f)
                * SPACE_FACTOR;
        EKundliPlanetX2[1] = (HKUNDLISpace + 6.00f + 26.0f)
                * SPACE_FACTOR;
        EKundliPlanetX2[2] = (HKUNDLISpace + 35.00f + 14.0f)
                * SPACE_FACTOR;
        EKundliPlanetX2[3] = (HKUNDLISpace + 36.00f + 10.0f)
                * SPACE_FACTOR;
        EKundliPlanetX2[4] = (HKUNDLISpace + 35.00f + 16.0f)
                * SPACE_FACTOR;
        EKundliPlanetX2[5] = (HKUNDLISpace + 8.00f + 10.0f)
                * SPACE_FACTOR;

        EKundliPlanetY2[0] = (VKUNDLISpace + 48.00f - 16.0f)
                * SPACE_FACTOR;
        EKundliPlanetY2[1] = (VKUNDLISpace + 34.00f - 16.0f)
                * SPACE_FACTOR;
        EKundliPlanetY2[2] = (VKUNDLISpace + 28.00f - 7.0f)
                * SPACE_FACTOR;
        EKundliPlanetY2[3] = (VKUNDLISpace + 12.00f - 4.0f)
                * SPACE_FACTOR;
        EKundliPlanetY2[4] = (VKUNDLISpace + 40.00f + 3.0f)
                * SPACE_FACTOR;
        EKundliPlanetY2[5] = (VKUNDLISpace + 12.00f - 4.0f)
                * SPACE_FACTOR;


    }


    private void initNorthKundliCoordinate() {


        NRashiX[0] = (HKUNDLISpace + 117.00f) * SPACE_FACTOR;
        NRashiX[1] = (HKUNDLISpace + 58.00f) * SPACE_FACTOR;
        NRashiX[2] = (HKUNDLISpace + 38.00f) * SPACE_FACTOR;
        NRashiX[3] = (HKUNDLISpace + 58.00f) * SPACE_FACTOR;
        NRashiX[4] = (HKUNDLISpace + 40.00f) * SPACE_FACTOR;
        NRashiX[5] = (HKUNDLISpace + 58.00f) * SPACE_FACTOR;
        NRashiX[6] = (HKUNDLISpace + 117.00f) * SPACE_FACTOR;
        NRashiX[7] = (HKUNDLISpace + 175.00f) * SPACE_FACTOR;
        NRashiX[8] = (HKUNDLISpace + 193.00f) * SPACE_FACTOR;
        NRashiX[9] = (HKUNDLISpace + 175.00f) * SPACE_FACTOR;
        NRashiX[10] = (HKUNDLISpace + 193.00f) * SPACE_FACTOR;
        NRashiX[11] = (HKUNDLISpace + 175.00f) * SPACE_FACTOR;

        NRashiY[0] = (VKUNDLISpace + 58.00f)
                * SPACE_FACTOR;
        NRashiY[1] = (VKUNDLISpace + 40.00f)
                * SPACE_FACTOR;
        NRashiY[2] = (VKUNDLISpace + 60.00f)
                * SPACE_FACTOR;
        NRashiY[3] = (VKUNDLISpace + 117.00f)
                * SPACE_FACTOR;
        NRashiY[4] = (VKUNDLISpace + 179.00f)
                * SPACE_FACTOR;
        NRashiY[5] = (VKUNDLISpace + 193.00f)
                * SPACE_FACTOR;
        NRashiY[6] = (VKUNDLISpace + 175.00f)
                * SPACE_FACTOR;
        NRashiY[7] = (VKUNDLISpace + 195.00f)
                * SPACE_FACTOR;
        NRashiY[8] = (VKUNDLISpace + 175.00f)
                * SPACE_FACTOR;
        NRashiY[9] = (VKUNDLISpace + 117.00f)
                * SPACE_FACTOR;
        NRashiY[10] = (VKUNDLISpace + 58.00f)
                * SPACE_FACTOR;
        NRashiY[11] = (VKUNDLISpace + 40.00f)
                * SPACE_FACTOR;

        // INIT X1,Y1

        NKundliPlanetX1[0] = (HKUNDLISpace + 125.00f)
                * SPACE_FACTOR;
        NKundliPlanetX1[1] = (HKUNDLISpace + 87.00f)
                * SPACE_FACTOR;
        NKundliPlanetX1[2] = (HKUNDLISpace + 117.00f)
                * SPACE_FACTOR;
        NKundliPlanetX1[3] = (HKUNDLISpace + 135.00f)
                * SPACE_FACTOR;
        NKundliPlanetX1[4] = (HKUNDLISpace + 99.00f)
                * SPACE_FACTOR;
        NKundliPlanetX1[5] = (HKUNDLISpace + 99.00f)
                * SPACE_FACTOR;// ADDED BY BIJENDRA ON
        // 12-DEC-2012

        NKundliPlanetY1[0] = (VKUNDLISpace + 40.00f)
                * SPACE_FACTOR;
        NKundliPlanetY1[1] = (VKUNDLISpace + 58.00f)
                * SPACE_FACTOR;
        NKundliPlanetY1[2] = (VKUNDLISpace + 76.00f)
                * SPACE_FACTOR;
        NKundliPlanetY1[3] = (VKUNDLISpace + 58.00f)
                * SPACE_FACTOR;
        NKundliPlanetY1[4] = (VKUNDLISpace + 32.00f)
                * SPACE_FACTOR;
        NKundliPlanetY1[5] = (VKUNDLISpace + 94.00f)
                * SPACE_FACTOR;// ADDED BY BIJENDRA ON
        // 12-DEC-2012

        // X7,Y7
        NKundliPlanetX7[0] = (HKUNDLISpace + 125.00f)
                * SPACE_FACTOR;
        NKundliPlanetX7[1] = (HKUNDLISpace + 87.00f)
                * SPACE_FACTOR;
        NKundliPlanetX7[2] = (HKUNDLISpace + 117.00f)
                * SPACE_FACTOR;
        NKundliPlanetX7[3] = (HKUNDLISpace + 135.00f)
                * SPACE_FACTOR;
        NKundliPlanetX7[4] = (HKUNDLISpace + 99.00f)
                * SPACE_FACTOR;
        NKundliPlanetX7[5] = (HKUNDLISpace + 99.00f)
                * SPACE_FACTOR;// ADDED BY BIJENDRA ON
        // 12-DEC-2012

        NKundliPlanetY7[0] = (VKUNDLISpace + 157.00f)
                * SPACE_FACTOR;
        NKundliPlanetY7[1] = (VKUNDLISpace + 175.00f)
                * SPACE_FACTOR;
        NKundliPlanetY7[2] = (VKUNDLISpace + 193.00f)
                * SPACE_FACTOR;
        NKundliPlanetY7[3] = (VKUNDLISpace + 175.00f)
                * SPACE_FACTOR;
        NKundliPlanetY7[4] = (VKUNDLISpace + 149.00f)
                * SPACE_FACTOR;
        NKundliPlanetY7[5] = (VKUNDLISpace + 162.00f)
                * SPACE_FACTOR;// ADDED BY BIJENDRA ON
        // 12-DEC-2012

        // INIT X2,Y2

        NKundliPlanetX2[0] = (HKUNDLISpace + 30.00f)
                * SPACE_FACTOR;
        NKundliPlanetX2[1] = (HKUNDLISpace + 73.00f)
                * SPACE_FACTOR;
        NKundliPlanetX2[2] = (HKUNDLISpace + 28.00f)
                * SPACE_FACTOR;
        NKundliPlanetX2[3] = (HKUNDLISpace + 53.00f)
                * SPACE_FACTOR;
        NKundliPlanetX2[4] = (HKUNDLISpace + 60.00f)
                * SPACE_FACTOR;
        NKundliPlanetX2[5] = (HKUNDLISpace + 80.00f)
                * SPACE_FACTOR;// ADDED BY BIJENDRA ON
        // 12-DEC-2012

        NKundliPlanetY2[0] = (VKUNDLISpace + 24.00f)
                * SPACE_FACTOR;
        NKundliPlanetY2[1] = (VKUNDLISpace + 24.00f)
                * SPACE_FACTOR;
        NKundliPlanetY2[2] = (VKUNDLISpace + 12.00f)
                * SPACE_FACTOR;
        NKundliPlanetY2[3] = (VKUNDLISpace + 26.00f)
                * SPACE_FACTOR;
        NKundliPlanetY2[4] = (VKUNDLISpace + 12.00f)
                * SPACE_FACTOR;
        NKundliPlanetY2[5] = (VKUNDLISpace + 12.00f)
                * SPACE_FACTOR;// ADDED BY BIJENDRA ON
        // 12-DEC-2012

        // INIT X12,Y12
        // NKundliPlanetX12[0]=(HKUNDLISpace+147.00f)*SPACE_FACTOR;
        NKundliPlanetX12[0] = (HKUNDLISpace + 150.00f)
                * SPACE_FACTOR;
        NKundliPlanetX12[1] = (HKUNDLISpace + 190.00f)
                * SPACE_FACTOR;
        NKundliPlanetX12[2] = (HKUNDLISpace + 145.00f)
                * SPACE_FACTOR;
        NKundliPlanetX12[3] = (HKUNDLISpace + 170.00f)
                * SPACE_FACTOR;
        NKundliPlanetX12[4] = (HKUNDLISpace + 177.00f)
                * SPACE_FACTOR;
        NKundliPlanetX12[5] = (HKUNDLISpace + 200.00f)
                * SPACE_FACTOR;// ADDED BY BIJENDRA ON
        // 12-DEC-2012

        NKundliPlanetY12[0] = (VKUNDLISpace + 24.00f)
                * SPACE_FACTOR;
        NKundliPlanetY12[1] = (VKUNDLISpace + 24.00f)
                * SPACE_FACTOR;
        NKundliPlanetY12[2] = (VKUNDLISpace + 12.00f)
                * SPACE_FACTOR;
        NKundliPlanetY12[3] = (VKUNDLISpace + 26.00f)
                * SPACE_FACTOR;
        NKundliPlanetY12[4] = (VKUNDLISpace + 12.00f)
                * SPACE_FACTOR;
        NKundliPlanetY12[5] = (VKUNDLISpace + 12.00f)
                * SPACE_FACTOR;// ADDED BY BIJENDRA ON
        // 12-DEC-2012

        // INIT X3,Y3

        NKundliPlanetX3[0] = (HKUNDLISpace + 3.00f)
                * SPACE_FACTOR;
        NKundliPlanetX3[1] = (HKUNDLISpace + 8.00f)
                * SPACE_FACTOR;
        NKundliPlanetX3[2] = (HKUNDLISpace + 21.00f)
                * SPACE_FACTOR;
        NKundliPlanetX3[3] = (HKUNDLISpace + 4.00f)
                * SPACE_FACTOR;
        NKundliPlanetX3[4] = (HKUNDLISpace + 4.00f)
                * SPACE_FACTOR;
        NKundliPlanetX3[5] = (HKUNDLISpace + 21.00f)
                * SPACE_FACTOR;// ADDED BY BIJENDRA ON
        // 12-DEC-2012

        NKundliPlanetY3[0] = (VKUNDLISpace + 28.00f)
                * SPACE_FACTOR;
        NKundliPlanetY3[1] = (VKUNDLISpace + 85.00f)
                * SPACE_FACTOR;
        NKundliPlanetY3[2] = (VKUNDLISpace + 48.00f)
                * SPACE_FACTOR;
        NKundliPlanetY3[3] = (VKUNDLISpace + 60.00f)
                * SPACE_FACTOR;
        NKundliPlanetY3[4] = (VKUNDLISpace + 42.00f)
                * SPACE_FACTOR;
        NKundliPlanetY3[5] = (VKUNDLISpace + 75.00f)
                * SPACE_FACTOR;// ADDED BY BIJENDRA ON
        // 12-DEC-2012

        // INIT X4,Y4

        NKundliPlanetX4[0] = (HKUNDLISpace + 34.00f)
                * SPACE_FACTOR;
        NKundliPlanetX4[1] = (HKUNDLISpace + 58.00f)
                * SPACE_FACTOR;
        NKundliPlanetX4[2] = (HKUNDLISpace + 78.00f)
                * SPACE_FACTOR;
        NKundliPlanetX4[3] = (HKUNDLISpace + 58.00f)
                * SPACE_FACTOR;
        NKundliPlanetX4[4] = (HKUNDLISpace + 20.00f)
                * SPACE_FACTOR;
        NKundliPlanetX4[5] = (HKUNDLISpace + 58.00f)
                * SPACE_FACTOR;// ADDED BY BIJENDRA ON
        // 12-DEC-2012

        NKundliPlanetY4[0] = (VKUNDLISpace + 112.00f)
                * SPACE_FACTOR;
        NKundliPlanetY4[1] = (VKUNDLISpace + 97.00f)
                * SPACE_FACTOR;
        NKundliPlanetY4[2] = (VKUNDLISpace + 117.00f)
                * SPACE_FACTOR;
        NKundliPlanetY4[3] = (VKUNDLISpace + 137.00f)
                * SPACE_FACTOR;
        NKundliPlanetY4[4] = (VKUNDLISpace + 125.00f)
                * SPACE_FACTOR;
        NKundliPlanetY4[5] = (VKUNDLISpace + 150.00f)
                * SPACE_FACTOR;// ADDED BY BIJENDRA ON
        // 12-DEC-2012

        // INIT X10,Y10
        NKundliPlanetX10[0] = (HKUNDLISpace + 151.00f)
                * SPACE_FACTOR;
        NKundliPlanetX10[1] = (HKUNDLISpace + 175.00f)
                * SPACE_FACTOR;
        NKundliPlanetX10[2] = (HKUNDLISpace + 195.00f)
                * SPACE_FACTOR;
        NKundliPlanetX10[3] = (HKUNDLISpace + 175.00f)
                * SPACE_FACTOR;
        NKundliPlanetX10[4] = (HKUNDLISpace + 147.00f)
                * SPACE_FACTOR;
        NKundliPlanetX10[5] = (HKUNDLISpace + 175.00f)
                * SPACE_FACTOR;// ADDED BY BIJENDRA ON
        // 12-DEC-2012

        NKundliPlanetY10[0] = (VKUNDLISpace + 112.00f)
                * SPACE_FACTOR;
        NKundliPlanetY10[1] = (VKUNDLISpace + 97.00f)
                * SPACE_FACTOR;
        NKundliPlanetY10[2] = (VKUNDLISpace + 117.00f)
                * SPACE_FACTOR;
        NKundliPlanetY10[3] = (VKUNDLISpace + 137.00f)
                * SPACE_FACTOR;
        NKundliPlanetY10[4] = (VKUNDLISpace + 125.00f)
                * SPACE_FACTOR;
        NKundliPlanetY10[5] = (VKUNDLISpace + 150.00f)
                * SPACE_FACTOR;// ADDED BY BIJENDRA ON
        // 12-DEC-2012

        // INIT X5,Y5

        NKundliPlanetX5[0] = (HKUNDLISpace + 4.00f)
                * SPACE_FACTOR;
        NKundliPlanetX5[1] = (HKUNDLISpace + 20.00f)
                * SPACE_FACTOR;
        NKundliPlanetX5[2] = (HKUNDLISpace + 4.00f)
                * SPACE_FACTOR;
        NKundliPlanetX5[3] = (HKUNDLISpace + 4.00f)
                * SPACE_FACTOR;
        NKundliPlanetX5[4] = (HKUNDLISpace + 15.00f)
                * SPACE_FACTOR;
        NKundliPlanetX5[5] = (HKUNDLISpace + 18.00f)
                * SPACE_FACTOR;// ADDED BY BIJENDRA ON
        // 12-DEC-2012

        NKundliPlanetY5[0] = (VKUNDLISpace + 175.00f)
                * SPACE_FACTOR;
        NKundliPlanetY5[1] = (VKUNDLISpace + 193.00f)
                * SPACE_FACTOR;
        NKundliPlanetY5[2] = (VKUNDLISpace + 147.00f)
                * SPACE_FACTOR;
        NKundliPlanetY5[3] = (VKUNDLISpace + 209.00f)
                * SPACE_FACTOR;
        NKundliPlanetY5[4] = (VKUNDLISpace + 160.00f)
                * SPACE_FACTOR;
        NKundliPlanetY5[5] = (VKUNDLISpace + 175.00f)
                * SPACE_FACTOR;// ADDED BY BIJENDRA ON
        // 12-DEC-2012

        // INIT X9,Y9

        NKundliPlanetX9[0] = (HKUNDLISpace + 215.00f)
                * SPACE_FACTOR;
        NKundliPlanetX9[1] = (HKUNDLISpace + 200.00f)
                * SPACE_FACTOR;
        NKundliPlanetX9[2] = (HKUNDLISpace + 217.00f)
                * SPACE_FACTOR;
        NKundliPlanetX9[3] = (HKUNDLISpace + 215.00f)
                * SPACE_FACTOR;
        NKundliPlanetX9[4] = (HKUNDLISpace + 205.00f)
                * SPACE_FACTOR;
        NKundliPlanetX9[5] = (HKUNDLISpace + 222.00f)
                * SPACE_FACTOR;// ADDED BY BIJENDRA ON
        // 12-DEC-2012

        NKundliPlanetY9[0] = (VKUNDLISpace + 175.00f)
                * SPACE_FACTOR;
        NKundliPlanetY9[1] = (VKUNDLISpace + 193.00f)
                * SPACE_FACTOR;
        // NKundliPlanetY9[2]=(VKUNDLISpace+147.00f)*SPACE_FACTOR;
        NKundliPlanetY9[2] = (VKUNDLISpace + 150.00f)
                * SPACE_FACTOR;
        NKundliPlanetY9[3] = (VKUNDLISpace + 209.00f)
                * SPACE_FACTOR;
        // NKundliPlanetY9[4]=(VKUNDLISpace+160.00f)*SPACE_FACTOR;
        NKundliPlanetY9[4] = (VKUNDLISpace + 162.00f)
                * SPACE_FACTOR;
        NKundliPlanetY9[5] = (VKUNDLISpace + 193.00f)
                * SPACE_FACTOR;// ADDED BY BIJENDRA ON
        // 12-DEC-2012

        // INIT X11,Y11

        NKundliPlanetX11[0] = (HKUNDLISpace + 215.00f)
                * SPACE_FACTOR;
        // NKundliPlanetX11[1]=(HKUNDLISpace+200.00f)*SPACE_FACTOR;
        NKundliPlanetX11[1] = (HKUNDLISpace + 204.00f)
                * SPACE_FACTOR;
        NKundliPlanetX11[2] = (HKUNDLISpace + 215.00f)
                * SPACE_FACTOR;
        // NKundliPlanetX11[3]=(HKUNDLISpace+215.00f)*SPACE_FACTOR;
        NKundliPlanetX11[3] = (HKUNDLISpace + 219.00f)
                * SPACE_FACTOR;
        NKundliPlanetX11[4] = (HKUNDLISpace + 205.00f)
                * SPACE_FACTOR;
        NKundliPlanetX11[5] = (HKUNDLISpace + 224.00f)
                * SPACE_FACTOR;// ADDED BY BIJENDRA ON
        // 12-DEC-2012

        NKundliPlanetY11[0] = (VKUNDLISpace + 58.00f)
                * SPACE_FACTOR;
        NKundliPlanetY11[1] = (VKUNDLISpace + 72.00f)
                * SPACE_FACTOR;
        NKundliPlanetY11[2] = (VKUNDLISpace + 30.00f)
                * SPACE_FACTOR;
        NKundliPlanetY11[3] = (VKUNDLISpace + 92.00f)
                * SPACE_FACTOR;
        NKundliPlanetY11[4] = (VKUNDLISpace + 43.00f)
                * SPACE_FACTOR;
        NKundliPlanetY11[5] = (VKUNDLISpace + 76.00f)
                * SPACE_FACTOR;// ADDED BY BIJENDRA ON
        // 12-DEC-2012

        // INIT X6,Y6

        NKundliPlanetX6[0] = (HKUNDLISpace + 58.00f)
                * SPACE_FACTOR;
        NKundliPlanetX6[1] = (HKUNDLISpace + 40.00f)
                * SPACE_FACTOR;
        NKundliPlanetX6[2] = (HKUNDLISpace + 65.00f)
                * SPACE_FACTOR;
        NKundliPlanetX6[3] = (HKUNDLISpace + 82.00f)
                * SPACE_FACTOR;
        NKundliPlanetX6[4] = (HKUNDLISpace + 20.00f)
                * SPACE_FACTOR;
        NKundliPlanetX6[5] = (HKUNDLISpace + 40.00f)
                * SPACE_FACTOR;// ADDED BY BIJENDRA ON
        // 12-DEC-2012

        NKundliPlanetY6[0] = (VKUNDLISpace + 223.00f)
                * SPACE_FACTOR;
        NKundliPlanetY6[1] = (VKUNDLISpace + 210.00f)
                * SPACE_FACTOR;
        NKundliPlanetY6[2] = (VKUNDLISpace + 210.00f)
                * SPACE_FACTOR;
        NKundliPlanetY6[3] = (VKUNDLISpace + 230.00f)
                * SPACE_FACTOR;
        NKundliPlanetY6[4] = (VKUNDLISpace + 224.00f)
                * SPACE_FACTOR;
        NKundliPlanetY6[5] = (VKUNDLISpace + 224.00f)
                * SPACE_FACTOR;// ADDED BY BIJENDRA ON
        // 12-DEC-2012
        // INIT X8,Y8

        NKundliPlanetX8[0] = (HKUNDLISpace + 175.00f)
                * SPACE_FACTOR;
        NKundliPlanetX8[1] = (HKUNDLISpace + 157.00f)
                * SPACE_FACTOR;
        NKundliPlanetX8[2] = (HKUNDLISpace + 182.00f)
                * SPACE_FACTOR;
        NKundliPlanetX8[3] = (HKUNDLISpace + 199.00f)
                * SPACE_FACTOR;
        // NKundliPlanetX8[4]=(HKUNDLISpace+137.00f)*SPACE_FACTOR;
        NKundliPlanetX8[4] = (HKUNDLISpace + 142.00f)
                * SPACE_FACTOR;
        NKundliPlanetX8[5] = (HKUNDLISpace + 162.00f)
                * SPACE_FACTOR;// ADDED BY BIJENDRA ON
        // 12-DEC-2012

        NKundliPlanetY8[0] = (VKUNDLISpace + 223.00f)
                * SPACE_FACTOR;
        NKundliPlanetY8[1] = (VKUNDLISpace + 210.00f)
                * SPACE_FACTOR;
        NKundliPlanetY8[2] = (VKUNDLISpace + 210.00f)
                * SPACE_FACTOR;
        NKundliPlanetY8[3] = (VKUNDLISpace + 230.00f)
                * SPACE_FACTOR;
        NKundliPlanetY8[4] = (VKUNDLISpace + 224.00f)
                * SPACE_FACTOR;
        NKundliPlanetY8[5] = (VKUNDLISpace + 224.00f)
                * SPACE_FACTOR;// ADDED BY BIJENDRA ON
        // 12-DEC-2012

    }


    private void intiCustomFontTextSizeAccordingToScreen(Activity context) {
        int _fTextSize = 0;
        if (CUtils.isTablet(context)) {
            int deviceWidth = (int) DeviceScreenWidth;
            isDeviceCompatibleForADV = true;
            if (600 > deviceWidth) {
                _fTextSize = 18;
                GESTURE_THRESHOLD_DIP_OTHERS = 18.0f;
                SPACE_FACTOR = 2.35f;

            }// added by hukum 5 Oct 2012, coz code is not working for tablets
            // with width less than 600 px
            if (600 > deviceWidth) {
                _fTextSize = 18;
                GESTURE_THRESHOLD_DIP_OTHERS = 18.0f;
                SPACE_FACTOR = 2.35f;

            }
            if ((600 < deviceWidth) && (deviceWidth <= 720)) {
                _fTextSize = 18;
                GESTURE_THRESHOLD_DIP_OTHERS = 20.0f;
                SPACE_FACTOR = 3.00f;
                VKUNDLISpace = 4.0f;

            }
            if ((720 < deviceWidth) && (deviceWidth <= 800)) {
                _fTextSize = 20;
                GESTURE_THRESHOLD_DIP_OTHERS = 20.0f;
                GESTURE_THRESHOLD_DIP_KUNDLI = 20.0f;
                SPACE_FACTOR = 3.25f;
                VKUNDLISpace = 5.0f;

            }

            if ((800 < deviceWidth) && (deviceWidth <= 1000)) {
                _fTextSize = 20;
                GESTURE_THRESHOLD_DIP_OTHERS = 20.0f;
                GESTURE_THRESHOLD_DIP_KUNDLI = 20.0f;
                SPACE_FACTOR = 3.50f;
                VKUNDLISpace = 5.5f;

            }

            if ((1000 < deviceWidth) && (deviceWidth <= 1200)) {
                _fTextSize = 20;
                GESTURE_THRESHOLD_DIP_OTHERS = 20.0f;
                GESTURE_THRESHOLD_DIP_KUNDLI = 20.0f;
                SPACE_FACTOR = 4.50f;
                VKUNDLISpace = 6.0f;

            }
            //ADDED BY DEEPAK ON 04-11-2014
            else {
                _fTextSize = 20;
                GESTURE_THRESHOLD_DIP_OTHERS = 20.0f;
                GESTURE_THRESHOLD_DIP_KUNDLI = 20.0f;
                SPACE_FACTOR = DeviceScreenWidth / 240.00f;
                VKUNDLISpace = 6.0f;

            }
            //END ON 04-11-2014

        } else {
            isDeviceCompatibleForADV = false;
            if (DeviceScreenWidth <= 240) {
                _fTextSize = 12;
                GESTURE_THRESHOLD_DIP_OTHERS = 12.0f;
                SPACE_FACTOR = 1.0f;

            }
            if ((240 < DeviceScreenWidth)
                    && (DeviceScreenWidth <= 320)) {
                _fTextSize = 14;
                GESTURE_THRESHOLD_DIP_OTHERS = 14.0f;
                SPACE_FACTOR = 1.33f;

            }

            if ((320 < DeviceScreenWidth)
                    && (DeviceScreenWidth <= 480)) {
                _fTextSize = 16;
                GESTURE_THRESHOLD_DIP_OTHERS = 16.0f;
                SPACE_FACTOR = 2.00f;
                if ((680 < DeviceScreenHeight)
                        && (DeviceScreenHeight <= 800))
                    isDeviceCompatibleForADV = true;

            }
            if ((480 < DeviceScreenWidth)
                    && (DeviceScreenWidth <= 540)) {
                _fTextSize = 18;
                GESTURE_THRESHOLD_DIP_OTHERS = 18.0f;
                SPACE_FACTOR = 2.25f;

                isDeviceCompatibleForADV = true;
            }
            if ((540 < DeviceScreenWidth)
                    && (DeviceScreenWidth <= 600)) {
                _fTextSize = 18;
                GESTURE_THRESHOLD_DIP_OTHERS = 18.0f;
                SPACE_FACTOR = 2.35f;

                isDeviceCompatibleForADV = true;
            }

            if ((600 < DeviceScreenWidth)
                    && (DeviceScreenWidth <= 720)) {
                _fTextSize = 18;
                GESTURE_THRESHOLD_DIP_OTHERS = 20.0f;
                SPACE_FACTOR = 3.00f;
                isDeviceCompatibleForADV = true;
            }

            if ((720 < DeviceScreenWidth)
                    && (DeviceScreenWidth <= 800)) {
                _fTextSize = 20;
                GESTURE_THRESHOLD_DIP_OTHERS = 20.0f;
                GESTURE_THRESHOLD_DIP_KUNDLI = 20.0f;
                SPACE_FACTOR = 3.25f;
                VKUNDLISpace = 5.00f;
                isDeviceCompatibleForADV = true;
            }

            if ((800 < DeviceScreenWidth && (DeviceScreenWidth <= 1000))) {
                _fTextSize = 20;
                GESTURE_THRESHOLD_DIP_OTHERS = 20.0f;
                GESTURE_THRESHOLD_DIP_KUNDLI = 20.0f;
                SPACE_FACTOR = 4.25f;
                VKUNDLISpace = 5.75f;
                isDeviceCompatibleForADV = true;
            }

            if ((1000 < DeviceScreenWidth) && (DeviceScreenWidth <= 1200)) {
                _fTextSize = 20;
                GESTURE_THRESHOLD_DIP_OTHERS = 20.0f;
                GESTURE_THRESHOLD_DIP_KUNDLI = 20.0f;
                SPACE_FACTOR = 4.60f;
                VKUNDLISpace = 6.0f;

            }
            //ADDED BY DEEPAK ON 04-11-2014
            else {
                _fTextSize = 20;
                GESTURE_THRESHOLD_DIP_OTHERS = 20.0f;
                GESTURE_THRESHOLD_DIP_KUNDLI = 20.0f;
                SPACE_FACTOR = DeviceScreenWidth / 240.00f;
                VKUNDLISpace = 6.0f;

            }
            //END ON 04-11-2014

        }
        custom_Font_TextSize = _fTextSize;
        custom_Font_TextSize_List = _fTextSize + 3;
        Difference_between_planets_in_Kundli = _fTextSize;

    }

    private void initApplicationfontAndColors(Activity context) {
//    final float scale = context.getBaseContext().getResources().getDisplayMetrics().density;
//    int mGestureThreshold = (int) (GESTURE_THRESHOLD_DIP_KUNDLI * scale + 0.5f);


        float cotentTextSize = context.getResources().getDimension(R.dimen.body_text_size);
        float noteTextSize = context.getResources().getDimension(R.dimen.note_text_size);

        /*if(CUtils.isTablet(context))
        {
            mGestureThreshold = context.getResources().getDimension(R.dimen.heading_text_size);
        }else {
            mGestureThreshold = context.getResources().getDimension(R.dimen.heading_text_size_mobile);
        }*/

        //int mGestureThresholdOthers = (int) (GESTURE_THRESHOLD_DIP_OTHERS * scale + 0.5f);
//       KundliLineColor.setColor(Color.rgb(128, 0, 128));

        // SAN Color Change Black uncomment this line
        KundliLineColor.setColor(ContextCompat.getColor(context,R.color.ColorPrimary));
        //KundliLineColor.setColor(ContextCompat.getColor(context,R.color.black));
        KundliLineColor.setStyle(Paint.Style.STROKE);
        KundliLineColor.setTypeface(Typeface.DEFAULT_BOLD);
        KundliLineColor.setStrokeWidth(4f);


//       KundliRasiNumbersCustomFont.setColor(Color.rgb(210, 105, 30));
        // SAN Color Change Black
        //KundliRasiNumbersCustomFont.setColor(Color.rgb(71, 71, 71));
        KundliRasiNumbersCustomFont.setColor(ContextCompat.getColor(context,R.color.KundliRasiNumbersCustomFont));
        //KundliRasiNumbersCustomFont.setColor(Color.rgb(0, 0, 0));  // 0 for black
        KundliRasiNumbersCustomFont.setStyle(Paint.Style.FILL_AND_STROKE);
            /*KundliRasiNumbersCustomFont.setTypeface(Typeface.DEFAULT_BOLD);
            KundliRasiNumbersCustomFont.setStrokeWidth(1.0f);*/
        KundliRasiNumbersCustomFont.setAntiAlias(true);
        KundliRasiNumbersCustomFont.setTextSize(mGestureThreshold);

        // SAN Color Change Black uncomment this line

        planetsColorList.put(context.getResources().getString(R.string.La), ContextCompat.getColor(context,R.color.colorLa));
        planetsColorList.put(context.getResources().getString(R.string.Su), ContextCompat.getColor(context,R.color.colorSu));
        planetsColorList.put(context.getResources().getString(R.string.Mo), ContextCompat.getColor(context,R.color.colorMo));
        planetsColorList.put(context.getResources().getString(R.string.Ma), ContextCompat.getColor(context,R.color.colorMa));
        planetsColorList.put(context.getResources().getString(R.string.Me), ContextCompat.getColor(context,R.color.colorMe));
        planetsColorList.put(context.getResources().getString(R.string.Ju), ContextCompat.getColor(context,R.color.colorJu));
        planetsColorList.put(context.getResources().getString(R.string.Ve), ContextCompat.getColor(context,R.color.colorVe));
        planetsColorList.put(context.getResources().getString(R.string.Sa), ContextCompat.getColor(context,R.color.colorSa));
        planetsColorList.put(context.getResources().getString(R.string.Ra), ContextCompat.getColor(context,R.color.colorRa));
        planetsColorList.put(context.getResources().getString(R.string.Ke), ContextCompat.getColor(context,R.color.colorKe));
        planetsColorList.put(context.getResources().getString(R.string.Ur), ContextCompat.getColor(context,R.color.colorUr));
        planetsColorList.put(context.getResources().getString(R.string.Ne), ContextCompat.getColor(context,R.color.colorNe));
        planetsColorList.put(context.getResources().getString(R.string.Pl), ContextCompat.getColor(context,R.color.colorPl));

/*
        planetsColorList.put(context.getResources().getString(R.string.La), ContextCompat.getColor(context,R.color.black));
        planetsColorList.put(context.getResources().getString(R.string.Su), ContextCompat.getColor(context,R.color.black));
        planetsColorList.put(context.getResources().getString(R.string.Mo), ContextCompat.getColor(context,R.color.black));
        planetsColorList.put(context.getResources().getString(R.string.Ma), ContextCompat.getColor(context,R.color.black));
        planetsColorList.put(context.getResources().getString(R.string.Me), ContextCompat.getColor(context,R.color.black));
        planetsColorList.put(context.getResources().getString(R.string.Ju), ContextCompat.getColor(context,R.color.black));
        planetsColorList.put(context.getResources().getString(R.string.Ve), ContextCompat.getColor(context,R.color.black));
        planetsColorList.put(context.getResources().getString(R.string.Sa), ContextCompat.getColor(context,R.color.black));
        planetsColorList.put(context.getResources().getString(R.string.Ra), ContextCompat.getColor(context,R.color.black));
        planetsColorList.put(context.getResources().getString(R.string.Ke), ContextCompat.getColor(context,R.color.black));
        planetsColorList.put(context.getResources().getString(R.string.Ur), ContextCompat.getColor(context,R.color.black));
        planetsColorList.put(context.getResources().getString(R.string.Ne), ContextCompat.getColor(context,R.color.black));
        planetsColorList.put(context.getResources().getString(R.string.Pl), ContextCompat.getColor(context,R.color.black));
*/


        initPaint(context, mGestureThreshold);


        //ADDED BY BIJENDRA ON 17-06-14

        KundliRomanNumCustomFont.setStyle(Paint.Style.FILL_AND_STROKE);
        //KundliRomanNumCustomFont.setTypeface(_typeface);
        KundliRomanNumCustomFont.setAntiAlias(true);
        KundliRomanNumCustomFont.setTextSize(mGestureThreshold);
        //END


        //HEADING COLOR
        HeadingColor = new Paint();

//       HeadingColor.setColor(Color.rgb(210, 105, 30));
        HeadingColor.setColor(Color.WHITE);
        HeadingColor.setStyle(Paint.Style.FILL_AND_STROKE);
        HeadingColor.setTypeface(_typeface);
//       HeadingColor.setStrokeWidth(2.0f);
        HeadingColor.setAntiAlias(true);
        HeadingColor.setTextSize(mGestureThreshold + 2);

        //TEXT COLOR
        TextColor = new Paint();
//       TextColor.setColor(Color.rgb(30, 144, 255));
        TextColor.setColor(Color.rgb(00, 00, 00));
        TextColor.setStyle(Paint.Style.FILL_AND_STROKE);
//       TextColor.setStrokeWidth(1.0f);
        TextColor.setAntiAlias(true);
        TextColor.setTypeface(_typeface);
        TextColor.setTextSize(mGestureThreshold);

        TextColorEnglish = new Paint();
//       TextColor.setColor(Color.rgb(30, 144, 255));
        TextColorEnglish.setColor(Color.rgb(00, 00, 00));
        TextColorEnglish.setStyle(Paint.Style.FILL_AND_STROKE);
        TextColorEnglish.setAntiAlias(true);
        TextColorEnglish.setTextSize(mGestureThreshold);


        //
        CGlobalVariables.TOP = (int) (mGestureThreshold) + 4;
        CGlobalVariables.BOTTOM = (int) (mGestureThreshold / 2) - 2;
        //
        //ADDED BY BIJENDRA 17-01-14
        NewTextColor = new Paint();
        NewTextColor.setColor(ContextCompat.getColor(context,R.color.black));
        NewTextColor.setStyle(Paint.Style.FILL_AND_STROKE);
//       NewTextColor.setStrokeWidth(1.0f);
        NewTextColor.setTypeface(_typeface);
        NewTextColor.setAntiAlias(true);
        NewTextColor.setTextSize(mGestureThreshold);

        //HEADING COLOR
        NewHeadingColor = new Paint();
        NewHeadingColor.setColor(ContextCompat.getColor(context,R.color.black));
        NewHeadingColor.setAntiAlias(true);
        NewHeadingColor.setStyle(Paint.Style.FILL_AND_STROKE);
        NewHeadingColor.setTypeface(_typeface);
        NewHeadingColor.setStrokeWidth(1f);
        NewHeadingColor.setTextSize(mGestureThreshold);

        //HEADING COLOR
        headingPaint = new Paint();
        headingPaint.setColor(ContextCompat.getColor(context,R.color.black));
        headingPaint.setAntiAlias(true);
        // headingPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        headingPaint.setTypeface(_typeface);
        headingPaint.setStrokeWidth(1f);
        headingPaint.setTextSize(mGestureThreshold);
        //for Content
        contentPaint = new Paint();
        contentPaint.setColor(ContextCompat.getColor(context,R.color.black));
        contentPaint.setAntiAlias(true);
        // contentPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        contentPaint.setTypeface(regularTypeface);
        contentPaint.setStrokeWidth(1f);
        contentPaint.setTextSize(cotentTextSize);

        contentPaintWithouttypeface = new Paint();
        contentPaintWithouttypeface.setColor(ContextCompat.getColor(context,R.color.black));
        contentPaintWithouttypeface.setAntiAlias(true);
        // contentPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        // contentPaint.setTypeface(regularTypeface);
        contentPaintWithouttypeface.setStrokeWidth(1f);
        contentPaintWithouttypeface.setTextSize(cotentTextSize);

        notePaint = new Paint();
        notePaint.setColor(ContextCompat.getColor(context,R.color.black));
        notePaint.setAntiAlias(true);
        // contentPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        notePaint.setTypeface(regularTypeface);
        notePaint.setStrokeWidth(1f);
        notePaint.setTextSize(noteTextSize);

        CenterHeadingColor = new Paint();
        CenterHeadingColor.setColor(ContextCompat.getColor(context,R.color.black));
        CenterHeadingColor.setAntiAlias(true);
        CenterHeadingColor.setStyle(Paint.Style.FILL_AND_STROKE);
        CenterHeadingColor.setTypeface(_typeface);
        CenterHeadingColor.setStrokeWidth(1f);
        CenterHeadingColor.setTextSize(mGestureThreshold + 2);
        CenterHeadingColor.setTextAlign(Align.CENTER);

        TableRow_Background_Color.setColor(ContextCompat.getColor(context,R.color.backgroundColor));
        TableRow_Background_Color.setStyle(Paint.Style.FILL_AND_STROKE);
        TableHeading_Background_Color.setColor(ContextCompat.getColor(context,R.color.backgroundColor));
        TableHeading_Background_Color.setStyle(Paint.Style.FILL_AND_STROKE);


        retroRectBackgroundPaint = new Paint();
        retroRectBackgroundPaint.setColor(ContextCompat.getColor(context,R.color.bgColorSec1));
        retroRectBackgroundPaint.setAntiAlias(true);
        retroRectBackgroundPaint.setStrokeWidth(1f);
        retroRectBackgroundPaint.setTextSize(cotentTextSize);

        TextColorKundliHint = new Paint();
        TextColorKundliHint.setColor(ContextCompat.getColor(context,R.color.black));
        TextColorKundliHint.setStyle(Paint.Style.FILL_AND_STROKE);
        TextColorKundliHint.setAntiAlias(true);
        TextColorKundliHint.setTypeface(_typeface);
        TextColorKundliHint.setTextSize(mGestureThreshold);

        combustRectBackgroundPaint = new Paint();
        combustRectBackgroundPaint.setColor(ContextCompat.getColor(context,R.color.combustcolor));
        combustRectBackgroundPaint.setAntiAlias(true);
        combustRectBackgroundPaint.setStrokeWidth(1f);
        combustRectBackgroundPaint.setTextSize(cotentTextSize);

    }

    public int custom_Font_TextSize_List = 0;
    public int custom_Font_TextSize = 0;
    public float Difference_between_planets_in_Kundli = 0;

    public float HKUNDLISpace = 2.00f;
    public float VKUNDLISpace = 4.00f;

    public float NRashiX[] = new float[12];
    public float NRashiY[] = new float[12];

    public float GESTURE_THRESHOLD_DIP_KUNDLI = 16.0f;
    public float GESTURE_THRESHOLD_DIP_OTHERS = 20.0f;
    public float SPACE_FACTOR = 1.0f;// for width 480 pixel
    public boolean isDeviceCompatibleForADV = false;
    public float DeviceScreenWidth = 0;
    public float DeviceScreenHeight = 0;
    // NORTH AND SOUTH COORDINATE FOR TABLES
    public float NTRashiX[] = new float[12];
    public float NTRashiY[] = new float[12];

    public float NKundliPlanetX1[] = new float[6];
    public float NKundliPlanetY1[] = new float[6];

    public float NKundliPlanetX2[] = new float[6];
    public float NKundliPlanetY2[] = new float[6];

    public float NKundliPlanetX3[] = new float[6];
    public float NKundliPlanetY3[] = new float[6];

    public float NKundliPlanetX4[] = new float[6];
    public float NKundliPlanetY4[] = new float[6];

    public float NKundliPlanetX5[] = new float[6];
    public float NKundliPlanetY5[] = new float[6];

    public float NKundliPlanetX6[] = new float[6];
    public float NKundliPlanetY6[] = new float[6];

    public float NKundliPlanetX7[] = new float[6];
    public float NKundliPlanetY7[] = new float[6];

    public float NKundliPlanetX8[] = new float[6];
    public float NKundliPlanetY8[] = new float[6];

    public float NKundliPlanetX9[] = new float[6];
    public float NKundliPlanetY9[] = new float[6];

    public float NKundliPlanetX10[] = new float[6];
    public float NKundliPlanetY10[] = new float[6];

    public float NKundliPlanetX11[] = new float[6];
    public float NKundliPlanetY11[] = new float[6];

    public float NKundliPlanetX12[] = new float[6];
    public float NKundliPlanetY12[] = new float[6];

    // SOUTH KUNDLI COORDINATES

    public float SKundliPlanetX1[] = new float[6];
    public float SKundliPlanetY1[] = new float[6];

    public float SKundliPlanetX2[] = new float[6];
    public float SKundliPlanetY2[] = new float[6];

    public float SKundliPlanetX3[] = new float[6];
    public float SKundliPlanetY3[] = new float[6];

    public float SKundliPlanetX4[] = new float[6];
    public float SKundliPlanetY4[] = new float[6];

    public float EKundliPlanetX5[] = new float[6];
    public float EKundliPlanetY5[] = new float[6];

    public float EKundliPlanetX6[] = new float[6];
    public float EKundliPlanetY6[] = new float[6];

    public float EKundliPlanetX7[] = new float[6];
    public float EKundliPlanetY7[] = new float[6];

    public float EKundliPlanetX8[] = new float[6];
    public float EKundliPlanetY8[] = new float[6];

    public float EKundliPlanetX9[] = new float[6];
    public float EKundliPlanetY9[] = new float[6];

    public float EKundliPlanetX10[] = new float[6];
    public float EKundliPlanetY10[] = new float[6];

    public float EKundliPlanetX11[] = new float[6];
    public float EKundliPlanetY11[] = new float[6];

    public float EKundliPlanetX12[] = new float[6];
    public float EKundliPlanetY12[] = new float[6];

    //// EAST KUNDLI COORDINATES

    public float EKundliPlanetX1[] = new float[6];
    public float EKundliPlanetY1[] = new float[6];

    public float EKundliPlanetX2[] = new float[6];
    public float EKundliPlanetY2[] = new float[6];

    public float EKundliPlanetX3[] = new float[6];
    public float EKundliPlanetY3[] = new float[6];

    public float EKundliPlanetX4[] = new float[6];
    public float EKundliPlanetY4[] = new float[6];

    public float SKundliPlanetX5[] = new float[6];
    public float SKundliPlanetY5[] = new float[6];

    public float SKundliPlanetX6[] = new float[6];
    public float SKundliPlanetY6[] = new float[6];

    public float SKundliPlanetX7[] = new float[6];
    public float SKundliPlanetY7[] = new float[6];

    public float SKundliPlanetX8[] = new float[6];
    public float SKundliPlanetY8[] = new float[6];

    public float SKundliPlanetX9[] = new float[6];
    public float SKundliPlanetY9[] = new float[6];

    public float SKundliPlanetX10[] = new float[6];
    public float SKundliPlanetY10[] = new float[6];

    public float SKundliPlanetX11[] = new float[6];
    public float SKundliPlanetY11[] = new float[6];

    public float SKundliPlanetX12[] = new float[6];
    public float SKundliPlanetY12[] = new float[6];

    // END EAST KUNDLI COORDINATES

    public float NTKundliPlanetX1[] = new float[12];
    public float NTKundliPlanetY1[] = new float[12];

    public float NTKundliPlanetX2[] = new float[12];
    public float NTKundliPlanetY2[] = new float[12];

    public float NTKundliPlanetX3[] = new float[12];
    public float NTKundliPlanetY3[] = new float[12];

    public float NTKundliPlanetX4[] = new float[12];
    public float NTKundliPlanetY4[] = new float[12];

    public float NTKundliPlanetX5[] = new float[12];
    public float NTKundliPlanetY5[] = new float[12];

    public float NTKundliPlanetX6[] = new float[12];
    public float NTKundliPlanetY6[] = new float[12];

    public float NTKundliPlanetX7[] = new float[12];
    public float NTKundliPlanetY7[] = new float[12];

    public float NTKundliPlanetX8[] = new float[12];
    public float NTKundliPlanetY8[] = new float[12];

    public float NTKundliPlanetX9[] = new float[12];
    public float NTKundliPlanetY9[] = new float[12];

    public float NTKundliPlanetX10[] = new float[12];
    public float NTKundliPlanetY10[] = new float[12];

    public float NTKundliPlanetX11[] = new float[12];
    public float NTKundliPlanetY11[] = new float[12];

    public float NTKundliPlanetX12[] = new float[12];
    public float NTKundliPlanetY12[] = new float[12];

    // SOUTH KUNDLI COORDINATES

    public float STKundliPlanetX1[] = new float[12];
    public float STKundliPlanetY1[] = new float[12];

    public float STKundliPlanetX2[] = new float[12];
    public float STKundliPlanetY2[] = new float[12];

    public float STKundliPlanetX3[] = new float[12];
    public float STKundliPlanetY3[] = new float[12];

    public float STKundliPlanetX4[] = new float[12];
    public float STKundliPlanetY4[] = new float[12];

    public float STKundliPlanetX5[] = new float[12];
    public float STKundliPlanetY5[] = new float[12];

    public float STKundliPlanetX6[] = new float[12];
    public float STKundliPlanetY6[] = new float[12];

    public float STKundliPlanetX7[] = new float[12];
    public float STKundliPlanetY7[] = new float[12];

    public float STKundliPlanetX8[] = new float[12];
    public float STKundliPlanetY8[] = new float[12];

    public float STKundliPlanetX9[] = new float[12];
    public float STKundliPlanetY9[] = new float[12];

    public float STKundliPlanetX10[] = new float[12];
    public float STKundliPlanetY10[] = new float[12];

    public float STKundliPlanetX11[] = new float[12];
    public float STKundliPlanetY11[] = new float[12];

    public float STKundliPlanetX12[] = new float[12];
    public float STKundliPlanetY12[] = new float[12];


    //EAST KUNDLI COORDINATES


/* public  float ESKundliPlanetX1[] = new float[12];
    public  float ESKundliPlanetY1[] = new float[12];

   public  float ESKundliPlanetX2[] = new float[12];
   public  float ESKundliPlanetY2[] = new float[12];

   public  float ESKundliPlanetX3[] = new float[12];
   public  float ESKundliPlanetY3[] = new float[12];

   public  float ESKundliPlanetX4[] = new float[12];
   public  float ESKundliPlanetY4[] = new float[12];

   public  float ESKundliPlanetX5[] = new float[12];
   public  float ESKundliPlanetY5[] = new float[12];

   public  float ESKundliPlanetX6[] = new float[12];
   public  float ESKundliPlanetY6[] = new float[12];

   public  float ESKundliPlanetX7[] = new float[12];
   public  float ESKundliPlanetY7[] = new float[12];

   public  float ESKundliPlanetX8[] = new float[12];
   public  float ESKundliPlanetY8[] = new float[12];

   public  float ESKundliPlanetX9[] = new float[12];
   public  float ESKundliPlanetY9[] = new float[12];

   public  float ESKundliPlanetX10[] = new float[12];
   public  float ESKundliPlanetY10[] = new float[12];

   public  float ESKundliPlanetX11[] = new float[12];
   public  float ESKundliPlanetY11[] = new float[12];

   public  float ESKundliPlanetX12[] = new float[12];
   public  float ESKundliPlanetY12[] = new float[12];
*/
    //END


    public Paint HeadingColor = new Paint();
    public Paint TextColor = new Paint();
    public Paint TextColorEnglish = new Paint();

    public Paint InnerUnderLineColor = new Paint();
    public Paint KundliLineColor = new Paint();
    public Paint KundliRasiNumbersCustomFont = new Paint();
    public Paint[] KundliPlanetsCustomFont = new Paint[14];
    public int UnderLineColor = Color.rgb(65, 105, 225);
    public Paint KundliRomanNumCustomFont = new Paint();
    public boolean NotShowUranusNeptunePlutoInChart;


    public Paint TableRow_Background_Color = new Paint();
    public Paint NewTextColor = new Paint();
    public Paint NewHeadingColor = new Paint();
    public Paint TableHeading_Background_Color = new Paint();
    public float NewDeviceScreenWidth = 0;
    public Paint CenterHeadingColor = new Paint();

    public Paint headingPaint;
    public Paint contentPaint;
    public Paint notePaint;
    public Paint contentPaintWithouttypeface;
    public Paint retroRectBackgroundPaint;
    public Paint TextColorKundliHint;
    public Paint combustRectBackgroundPaint;

    public static HashMap<String, Integer> planetsColorList = new HashMap<>();

    /**
     * Initiate  paint array added by Amit sharma
     */
    private void initPaint(Context context, float mGestureThreshold) {
        int[] colorArr = context.getResources().getIntArray(R.array.planet_color);
        for (int i = 0; i < 14; i++) {
            KundliPlanetsCustomFont[i] = new Paint();

            // SAN Color Change Black uncomment this line
            KundliPlanetsCustomFont[i].setColor(colorArr[i]);
            //KundliPlanetsCustomFont[i].setColor(Color.BLACK);
            KundliPlanetsCustomFont[i].setStyle(Paint.Style.FILL_AND_STROKE);
            KundliPlanetsCustomFont[i].setTypeface(_typeface);
            KundliPlanetsCustomFont[i].setAntiAlias(true);
            KundliPlanetsCustomFont[i].setTextSize(mGestureThreshold);
        }




       /* KundliPlanetsCustomFont[1] = new Paint();
        KundliPlanetsCustomFont[1].setColor(Color.rgb(220, 20, 60));
//       KundliPlanetsCustomFont[1].setColor(Color.rgb(71, 71, 71));
        KundliPlanetsCustomFont[1].setStyle(Paint.Style.FILL_AND_STROKE);
        KundliPlanetsCustomFont[1].setTypeface(_typeface);
//        KundliPlanetsCustomFont[1].setStrokeWidth(1.0f);
        KundliPlanetsCustomFont[1].setAntiAlias(true);
        KundliPlanetsCustomFont[1].setTextSize(mGestureThreshold);


        KundliPlanetsCustomFont[2] = new Paint();
        KundliPlanetsCustomFont[2].setColor(Color.rgb(0, 100, 0));
//       KundliPlanetsCustomFont[2].setColor(Color.rgb(71, 71, 71));
        KundliPlanetsCustomFont[2].setStyle(Paint.Style.FILL_AND_STROKE);
        KundliPlanetsCustomFont[2].setTypeface(_typeface);
//       KundliPlanetsCustomFont[2].setStrokeWidth(1.0f);
        KundliPlanetsCustomFont[2].setAntiAlias(true);
        KundliPlanetsCustomFont[2].setTextSize(mGestureThreshold);

        KundliPlanetsCustomFont[3] = new Paint();
        KundliPlanetsCustomFont[3].setColor(Color.rgb(139, 0, 139));
//       KundliPlanetsCustomFont[3].setColor(Color.rgb(71, 71, 71));
        KundliPlanetsCustomFont[3].setStyle(Paint.Style.FILL_AND_STROKE);
        KundliPlanetsCustomFont[3].setTypeface(_typeface);
//       KundliPlanetsCustomFont[3].setStrokeWidth(1.0f);
        KundliPlanetsCustomFont[3].setAntiAlias(true);
        KundliPlanetsCustomFont[3].setTextSize(mGestureThreshold);

        KundliPlanetsCustomFont[4] = new Paint();
        KundliPlanetsCustomFont[4].setColor(Color.rgb(30, 144, 255));
//       KundliPlanetsCustomFont[4].setColor(Color.rgb(71, 71, 71));
        KundliPlanetsCustomFont[4].setStyle(Paint.Style.FILL_AND_STROKE);
        KundliPlanetsCustomFont[4].setTypeface(_typeface);
//       KundliPlanetsCustomFont[4].setStrokeWidth(1.0f);
        KundliPlanetsCustomFont[4].setAntiAlias(true);
        KundliPlanetsCustomFont[4].setTextSize(mGestureThreshold);

        KundliPlanetsCustomFont[5] = new Paint();
        KundliPlanetsCustomFont[5].setColor(Color.rgb(255, 0, 255));
//       KundliPlanetsCustomFont[5].setColor(Color.rgb(71, 71, 71));
        KundliPlanetsCustomFont[5].setStyle(Paint.Style.FILL_AND_STROKE);
        KundliPlanetsCustomFont[5].setTypeface(_typeface);
//       KundliPlanetsCustomFont[5].setStrokeWidth(1.0f);
        KundliPlanetsCustomFont[5].setAntiAlias(true);
        KundliPlanetsCustomFont[5].setTextSize(mGestureThreshold);

        KundliPlanetsCustomFont[6] = new Paint();
        KundliPlanetsCustomFont[6].setColor(Color.rgb(255, 0, 255));
//       KundliPlanetsCustomFont[5].setColor(Color.rgb(71, 71, 71));
        KundliPlanetsCustomFont[6].setStyle(Paint.Style.FILL_AND_STROKE);
        KundliPlanetsCustomFont[6].setTypeface(_typeface);
//       KundliPlanetsCustomFont[5].setStrokeWidth(1.0f);
        KundliPlanetsCustomFont[6].setAntiAlias(true);
        KundliPlanetsCustomFont[6].setTextSize(mGestureThreshold);

        KundliPlanetsCustomFont[7] = new Paint();
        KundliPlanetsCustomFont[7].setColor(Color.rgb(255, 0, 255));
//       KundliPlanetsCustomFont[5].setColor(Color.rgb(71, 71, 71));
        KundliPlanetsCustomFont[7].setStyle(Paint.Style.FILL_AND_STROKE);
        KundliPlanetsCustomFont[7].setTypeface(_typeface);
//       KundliPlanetsCustomFont[5].setStrokeWidth(1.0f);
        KundliPlanetsCustomFont[7].setAntiAlias(true);
        KundliPlanetsCustomFont[7].setTextSize(mGestureThreshold);

        KundliPlanetsCustomFont[8] = new Paint();
        KundliPlanetsCustomFont[8].setColor(Color.rgb(255, 0, 255));
//       KundliPlanetsCustomFont[5].setColor(Color.rgb(71, 71, 71));
        KundliPlanetsCustomFont[8].setStyle(Paint.Style.FILL_AND_STROKE);
        KundliPlanetsCustomFont[8].setTypeface(_typeface);
//       KundliPlanetsCustomFont[5].setStrokeWidth(1.0f);
        KundliPlanetsCustomFont[8].setAntiAlias(true);
        KundliPlanetsCustomFont[8].setTextSize(mGestureThreshold);

        KundliPlanetsCustomFont[8] = new Paint();
        KundliPlanetsCustomFont[8].setColor(Color.rgb(255, 0, 255));
//       KundliPlanetsCustomFont[5].setColor(Color.rgb(71, 71, 71));
        KundliPlanetsCustomFont[8].setStyle(Paint.Style.FILL_AND_STROKE);
        KundliPlanetsCustomFont[8].setTypeface(_typeface);
//       KundliPlanetsCustomFont[5].setStrokeWidth(1.0f);
        KundliPlanetsCustomFont[8].setAntiAlias(true);
        KundliPlanetsCustomFont[8].setTextSize(mGestureThreshold);*/
    }
}
