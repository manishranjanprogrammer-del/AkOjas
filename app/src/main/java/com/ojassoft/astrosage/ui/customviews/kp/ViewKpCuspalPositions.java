package com.ojassoft.astrosage.ui.customviews.kp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.view.View;

import com.ojassoft.astrosage.misc.CGenerateAppViews;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CScreenConstants;

public class ViewKpCuspalPositions extends View {
    Context context;
    private Canvas _canvas = null;
    private String[] _heading = null;
    private String[][] _kpCuspalPosition = null;
    private int startX = 10;
    CScreenConstants SCREEN_CONSTANTS;
    int yHeight = 0;
    int xLength = 0;
    String[] noteHeadings;
    float[] xcoor = {0, 0.7f, 2.0f, 2.5f, 3.0f, 3.5f};
    Typeface _typeface = null;

    public ViewKpCuspalPositions(Context context, String[][] kpCuspalPosition, String[] Heading, Typeface typeface, CScreenConstants _SCREEN_CONSTANTS, String[] noteHeadings) {
        super(context);
        this.context = context;
        _kpCuspalPosition = kpCuspalPosition;
        _heading = Heading;
        _typeface = typeface;
        SCREEN_CONSTANTS = _SCREEN_CONSTANTS;
        yHeight = ((int) SCREEN_CONSTANTS.DeviceScreenHeight) / 18;
        xLength = ((int) SCREEN_CONSTANTS.DeviceScreenWidth) / 4;
        this.noteHeadings = noteHeadings;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        _canvas = canvas;

        printHeading();
    }

    private void printHeading() {
        xLength = ((int) SCREEN_CONSTANTS.DeviceScreenWidth) / 4;
        // yHeight = ((int) SCREEN_CONSTANTS.DeviceScreenHeight) / 18;
        yHeight = CScreenConstants.pxToDp(CScreenConstants.topMargin, context);
        _canvas.drawText(_heading[0], startX + xcoor[0], yHeight, SCREEN_CONSTANTS.headingPaint);
        _canvas.drawText(_heading[1], (startX + xcoor[1] * xLength), yHeight, SCREEN_CONSTANTS.headingPaint);
        _canvas.drawText(_heading[2], startX + xcoor[2] * xLength, yHeight, SCREEN_CONSTANTS.headingPaint);
        _canvas.drawText(_heading[3], startX + xcoor[3] * xLength, yHeight, SCREEN_CONSTANTS.headingPaint);
        _canvas.drawText(_heading[4], startX + xcoor[4] * xLength, yHeight, SCREEN_CONSTANTS.headingPaint);
        _canvas.drawText(_heading[5], startX + xcoor[5] * xLength, yHeight, SCREEN_CONSTANTS.headingPaint);
        yHeight = yHeight + CScreenConstants.pxToDp(CScreenConstants.firstLineTopMargin, getContext());
        printPlanetDegreeAndSubsub();
    }

    private void printPlanetDegreeAndSubsub() {
        String[] naks = null;


        for (int i = 0; i < 12; i++) {
            if ((i % 2 == 0)) {
                if (this.SCREEN_CONSTANTS.NewDeviceScreenWidth < CGlobalVariables.SMALL_DEVICE_WIDTH) {
                    _canvas.drawRect(3, yHeight - CScreenConstants.pxToDp(CScreenConstants.margininline - CScreenConstants.rectHieghtMarginTop, getContext()), this.SCREEN_CONSTANTS.NewDeviceScreenWidth, yHeight + CScreenConstants.pxToDp(-CScreenConstants.rectHieghtMarginBottom, getContext()), SCREEN_CONSTANTS.TableRow_Background_Color);
                } else {
                    _canvas.drawRect(3, yHeight - CScreenConstants.pxToDp(CScreenConstants.margininline - CScreenConstants.rectHieghtMarginTop, getContext()), this.SCREEN_CONSTANTS.NewDeviceScreenWidth, yHeight + CScreenConstants.pxToDp(CScreenConstants.rectHieghtMarginBottom, getContext()), SCREEN_CONSTANTS.TableRow_Background_Color);
                }
            }
            naks = _kpCuspalPosition[i][1].split("-");
            _canvas.drawText(String.valueOf(i + 1),
                    startX + xcoor[0], yHeight, SCREEN_CONSTANTS.contentPaint);

            _canvas.drawText(_kpCuspalPosition[i][0], startX + xcoor[1]
                    * xLength, yHeight, SCREEN_CONSTANTS.contentPaint);
            _canvas.drawText(naks[0], startX + xcoor[2] * xLength, yHeight,
                    SCREEN_CONSTANTS.contentPaint);
            _canvas.drawText(naks[1], startX + xcoor[3] * xLength, yHeight,
                    SCREEN_CONSTANTS.contentPaint);
            _canvas.drawText(naks[2], startX + xcoor[4] * xLength, yHeight,
                    SCREEN_CONSTANTS.contentPaint);
            _canvas.drawText(naks[3], startX + xcoor[5] * xLength, yHeight,
                    SCREEN_CONSTANTS.contentPaint);
            // }
            yHeight = yHeight + CScreenConstants.pxToDp(CScreenConstants.margininline, getContext());
        }

       // yHeight = yHeight + CScreenConstants.pxToDp(CScreenConstants.noteTopMarign, getContext());
        _canvas.drawText(noteHeadings[0], 10, yHeight, SCREEN_CONSTANTS.headingPaint);
        yHeight = yHeight + CScreenConstants.pxToDp(CScreenConstants.noteFirstLineMarign, getContext());
        for (int i = 1; i < noteHeadings.length; i++) {
            _canvas.drawText(noteHeadings[i], 50, yHeight, SCREEN_CONSTANTS.notePaint);
            yHeight = yHeight + CScreenConstants.pxToDp(CScreenConstants.noteLineMargin, getContext());
        }
        CGenerateAppViews.setParamsOfViewKpCuspalPositions(yHeight);
    }

}
