package com.ojassoft.astrosage.ui.customviews.basic;


import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

import com.ojassoft.astrosage.misc.CGenerateAppViews;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CScreenConstants;
import com.ojassoft.astrosage.utils.CUtils;

public class ViewDrawBhavaBeginAndEnd extends View {
    private int startX = 10;
    private int xLength = 0;
    private Canvas _canvas = null;
    private float[] xPoNormal;
    int y1 = 0;
    int topY, bottomY;
    double[] _bhavaBegin = null;
    double[] _midBhava = null;
    CScreenConstants SCREEN_CONSTANTS;
    String[] rasi = null;
    String[] heading = null;
    int LANGUAGE_CODE = 0;

    public ViewDrawBhavaBeginAndEnd(Context context, double[] bhavaBig, double[] midBhava, CScreenConstants SCREEN_CONSTANTS, String[] rasi, String[] heading, int LANGUAGE_CODE) {
        super(context);
        this.SCREEN_CONSTANTS = SCREEN_CONSTANTS;
        xLength = ((int) SCREEN_CONSTANTS.DeviceScreenWidth) / 4;
        y1 = ((int) SCREEN_CONSTANTS.DeviceScreenHeight) / 17;
        initVariables();
        y1 += 1;
        _bhavaBegin = bhavaBig;
        _midBhava = midBhava;
        this.rasi = rasi;
        this.heading = heading;
        this.LANGUAGE_CODE = LANGUAGE_CODE;
    }

    private void initVariables() {
        xPoNormal = new float[]{10,
                startX + (0.4f) * xLength,
                startX + (1.0f) * xLength,
                startX + (2.2f) * xLength,
                startX + (2.8f) * xLength};
    }

    @Override
    protected void onDraw(Canvas canvas) {
        _canvas = canvas;
        DrawChalitTable();
    }


    private void drawTable() {
        int bhBegRashi = 0, midBhRashi = 0;
        double bhBegRem = 0, midBhRem = 0;
        String _directOrRect = "";
        for (int i = 0; i < 12; i++) {
            if ((i % 2 == 0)) {
                if (this.SCREEN_CONSTANTS.NewDeviceScreenWidth < CGlobalVariables.SMALL_DEVICE_WIDTH) {
                    _canvas.drawRect(3, y1 - CScreenConstants.pxToDp(CScreenConstants.margininline - CScreenConstants.rectHieghtMarginTop, getContext()), this.SCREEN_CONSTANTS.NewDeviceScreenWidth, y1 + CScreenConstants.pxToDp(-CScreenConstants.rectHieghtMarginBottom, getContext()), SCREEN_CONSTANTS.TableRow_Background_Color);
                } else {
                    _canvas.drawRect(3, y1 - CScreenConstants.pxToDp(CScreenConstants.margininline - CScreenConstants.rectHieghtMarginTop, getContext()), this.SCREEN_CONSTANTS.NewDeviceScreenWidth, y1 + CScreenConstants.pxToDp(CScreenConstants.rectHieghtMarginBottom, getContext()), SCREEN_CONSTANTS.TableRow_Background_Color);
                }
            }
            if (_bhavaBegin.length > i)
            bhBegRashi = (int) (_bhavaBegin[i] / 30.0);
            if (_midBhava.length > i)
            midBhRashi = (int) (_midBhava[i] / 30.0);
            if (_bhavaBegin.length > i)
            bhBegRem = _bhavaBegin[i] % 30.0;
            if (_midBhava.length > i)
            midBhRem = _midBhava[i] % 30.0;

            _canvas.drawText(String.valueOf(i + 1), xPoNormal[0], y1, SCREEN_CONSTANTS.contentPaint);
            if (rasi.length > bhBegRashi)
                _canvas.drawText(rasi[bhBegRashi], xPoNormal[1], y1, SCREEN_CONSTANTS.contentPaint);
            _canvas.drawText(CUtils.FormatDMSIn2DigitStringWithSign(bhBegRem, LANGUAGE_CODE), xPoNormal[2], y1, SCREEN_CONSTANTS.contentPaint);
            if (rasi.length > midBhRashi)
            _canvas.drawText(rasi[midBhRashi], xPoNormal[3], y1, SCREEN_CONSTANTS.contentPaint);
            _canvas.drawText(CUtils.FormatDMSIn2DigitStringWithSign(midBhRem, LANGUAGE_CODE), xPoNormal[4], y1, SCREEN_CONSTANTS.contentPaint);


            y1 = y1 + CScreenConstants.pxToDp(CScreenConstants.margininline, getContext());
        }
        CGenerateAppViews.setHeightParamChalitKundli(y1);
    }

    private void DrawChalitTable() {
        drawChalitTableHeading();
        drawTable();
    }


    private void drawChalitTableHeading() {
        xLength = ((int) SCREEN_CONSTANTS.DeviceScreenWidth) / 4;
        y1 = ((int) SCREEN_CONSTANTS.DeviceScreenHeight) / 18;
        for (int i = 0; i < heading.length; i++) {
            _canvas.drawText(heading[i], xPoNormal[i], y1, SCREEN_CONSTANTS.headingPaint);
        }
        y1 = y1 + CScreenConstants.pxToDp(CScreenConstants.firstLineTopMargin, getContext());
    }

}
