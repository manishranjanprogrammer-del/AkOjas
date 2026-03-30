package com.ojassoft.astrosage.ui.customviews.kp;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.view.View;

import com.ojassoft.astrosage.beans.OutKpHouseSignificators;
import com.ojassoft.astrosage.misc.CGenerateAppViews;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CScreenConstants;

public class ViewKpHouseSignificators extends View {

    private Canvas _canvas = null;
    private OutKpHouseSignificators _outKpHouseSignificators;
    private String[] _chartPlanets = null;
    private String[] _heading = null;
    Typeface _typeface = null;
    CScreenConstants SCREEN_CONSTANTS;
    int xLength = 0;
    int yHeight = 0;
    private int startX = 10;
    float[] xcoor = {0, 0.9f, 2.2f, 2.7f, 3.2f, 3.6f};
    Context context;

    public ViewKpHouseSignificators(Context context, OutKpHouseSignificators outKpHouseSignificators, String[] Heading, String[] ChartPlanets, Typeface typeface, CScreenConstants SCREEN_CONSTANTS) {
        super(context);
        _outKpHouseSignificators = outKpHouseSignificators;
        _heading = Heading;
        _chartPlanets = ChartPlanets;
        _typeface = typeface;
        this.SCREEN_CONSTANTS = SCREEN_CONSTANTS;
        this.context = context;

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
        printHouseSignificators();

    }

    private void printHouseSignificators() {
        ArrayList<String> tempHousePlanets = null;

        int iPlanetNumber = 0;

        for (int j = 0; j < 12; j++) {
            if ((j % 2 == 0)) {
                if (this.SCREEN_CONSTANTS.NewDeviceScreenWidth < CGlobalVariables.SMALL_DEVICE_WIDTH) {
                    _canvas.drawRect(3, yHeight - CScreenConstants.pxToDp(CScreenConstants.margininline - CScreenConstants.rectHieghtMarginTop, getContext()), this.SCREEN_CONSTANTS.NewDeviceScreenWidth, yHeight + CScreenConstants.pxToDp(-CScreenConstants.rectHieghtMarginBottom, getContext()), SCREEN_CONSTANTS.TableRow_Background_Color);
                } else {
                    _canvas.drawRect(3, yHeight - CScreenConstants.pxToDp(CScreenConstants.margininline - CScreenConstants.rectHieghtMarginTop, getContext()), this.SCREEN_CONSTANTS.NewDeviceScreenWidth, yHeight + CScreenConstants.pxToDp(CScreenConstants.rectHieghtMarginBottom, getContext()), SCREEN_CONSTANTS.TableRow_Background_Color);
                }
            }
            float tx = startX + xcoor[1] * xLength;
            _canvas.drawText(String.valueOf(j + 1),
                    startX + xcoor[0], yHeight, SCREEN_CONSTANTS.contentPaint);
            tempHousePlanets = getHouseSignificators(j);

            for (int k = 0; k < tempHousePlanets.size(); k++) {
                iPlanetNumber = Integer.valueOf(tempHousePlanets.get(k));
                _canvas.drawText(_chartPlanets[iPlanetNumber],
                        tx, yHeight, SCREEN_CONSTANTS.contentPaint);
                tx = tx + CScreenConstants.pxToDp(30, context);
            }
            yHeight = yHeight + CScreenConstants.pxToDp(CScreenConstants.margininline, getContext());

        }

        CGenerateAppViews.setParamsOfViewKpHouseSignificators(yHeight);
    }

    private ArrayList<String> getHouseSignificators(int index) {
        ArrayList<String> hPlanets = null;

        switch (index + 1) {
            case 1:
                hPlanets = _outKpHouseSignificators.getBhav1();
                break;
            case 2:
                hPlanets = _outKpHouseSignificators.getBhav2();
                break;
            case 3:
                hPlanets = _outKpHouseSignificators.getBhav3();
                break;
            case 4:
                hPlanets = _outKpHouseSignificators.getBhav4();
                break;
            case 5:
                hPlanets = _outKpHouseSignificators.getBhav5();
                break;
            case 6:
                hPlanets = _outKpHouseSignificators.getBhav6();
                break;
            case 7:
                hPlanets = _outKpHouseSignificators.getBhav7();
                break;
            case 8:
                hPlanets = _outKpHouseSignificators.getBhav8();
                break;
            case 9:
                hPlanets = _outKpHouseSignificators.getBhav9();
                break;
            case 10:
                hPlanets = _outKpHouseSignificators.getBhav10();
                break;
            case 11:
                hPlanets = _outKpHouseSignificators.getBhav11();
                break;
            case 12:
                hPlanets = _outKpHouseSignificators.getBhav12();
                break;
        }
        return hPlanets;
    }

}
