package com.ojassoft.astrosage.ui.customviews.kp;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.view.View;

import com.ojassoft.astrosage.misc.CGenerateAppViews;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CScreenConstants;

public class ViewKpPlanetSignification extends View {

    private Canvas _canvas = null;
    private String[] _heading = null;
    private String[] _kpPlanetSig = null;
    private String[] _chartPlanets = null;
    Typeface _typeface = null;
    CScreenConstants SCREEN_CONSTANTS;
    String[] note;
    int languageCode;
    float[] xcoor = {0, 0.9f, 2.2f, 2.7f, 3.2f, 3.6f};
    private int startX = 10;
    int yHeight;
    int xLength;
    Context context;

    public ViewKpPlanetSignification(Context context, String[] kpPlanetSig, String[] Heading, String[] ChartPlanets, Typeface typeface, CScreenConstants SCREEN_CONSTANTS, String[] note, int languageCode) {
        super(context);
        _kpPlanetSig = kpPlanetSig;
        _heading = Heading;
        _chartPlanets = ChartPlanets;
        _typeface = typeface;
        this.SCREEN_CONSTANTS = SCREEN_CONSTANTS;
        this.context=context;
        this.note = note;
        this.languageCode = languageCode;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        _canvas = canvas;
        printHeading();

    }

    private void printHeading() {
        xLength = ((int) SCREEN_CONSTANTS.DeviceScreenWidth) / 4;
        //yHeight = ((int) SCREEN_CONSTANTS.DeviceScreenHeight) / 18;
        yHeight = CScreenConstants.pxToDp(CScreenConstants.topMargin, context);
        _canvas.drawText(_heading[0], startX + xcoor[0], yHeight, SCREEN_CONSTANTS.headingPaint);
        _canvas.drawText(_heading[1], (startX + xcoor[1] * xLength), yHeight, SCREEN_CONSTANTS.headingPaint);
        yHeight = yHeight + CScreenConstants.pxToDp(CScreenConstants.firstLineTopMargin, getContext());
        printPlanetSignification();
    }

    private void printPlanetSignification() {
        String temp = null;

        for (int j = 0; j < 9; j++) {
            if ((j % 2 == 0)) {
                if (this.SCREEN_CONSTANTS.NewDeviceScreenWidth < CGlobalVariables.SMALL_DEVICE_WIDTH) {
                    _canvas.drawRect(3, yHeight - CScreenConstants.pxToDp(CScreenConstants.margininline - CScreenConstants.rectHieghtMarginTop, getContext()), this.SCREEN_CONSTANTS.NewDeviceScreenWidth, yHeight + CScreenConstants.pxToDp(-CScreenConstants.rectHieghtMarginBottom, getContext()), SCREEN_CONSTANTS.TableRow_Background_Color);
                } else {
                    _canvas.drawRect(3, yHeight - CScreenConstants.pxToDp(CScreenConstants.margininline - CScreenConstants.rectHieghtMarginTop, getContext()), this.SCREEN_CONSTANTS.NewDeviceScreenWidth, yHeight + CScreenConstants.pxToDp(CScreenConstants.rectHieghtMarginBottom, getContext()), SCREEN_CONSTANTS.TableRow_Background_Color);
                }
            }
            temp = _kpPlanetSig[j];
            temp = (temp.replace(" ", "|")).replace("|", "  ");

            _canvas.drawText(_chartPlanets[j],
                    startX + xcoor[0], yHeight, SCREEN_CONSTANTS.contentPaint);

            _canvas.drawText(temp, startX + xcoor[1]
                    * xLength, yHeight, SCREEN_CONSTANTS.contentPaint);

            temp = null;
            yHeight = yHeight + CScreenConstants.pxToDp(CScreenConstants.margininline, getContext());
        }
        yHeight = yHeight + CScreenConstants.pxToDp(CScreenConstants.noteTopMarign, getContext());
        /*if (languageCode != CGlobalVariables.HINDI && languageCode != CGlobalVariables.ENGLISH) {
            //Draw Note below the table.
            //_canvas.drawText(note, 10, yHeight, SCREEN_CONSTANTS.notePaint);

            _canvas.drawText(note[0], 10, yHeight, SCREEN_CONSTANTS.headingPaint);
            yHeight = yHeight + CScreenConstants.pxToDp(CScreenConstants.noteFirstLineMarign, getContext());
            for (int i = 1; i < note.length; i++) {
                _canvas.drawText(note[i], 50, yHeight, SCREEN_CONSTANTS.notePaint);
                yHeight = yHeight + CScreenConstants.pxToDp(CScreenConstants.noteLineMargin, getContext());
            }
            //End note
        }*/ //Note Text Removed from layout.
        CGenerateAppViews.setParamsOfViewPlanetSignification(yHeight);
    }
}
