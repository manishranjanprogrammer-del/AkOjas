package com.ojassoft.astrosage.ui.customviews.shodashvarga;


import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

import com.ojassoft.astrosage.utils.CGlobal;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CScreenConstants;

public class ViewShodashvargaNorthHora extends View {
    public String[] _planetLagna = null;
    private int iInxedColor = 0;
    private Canvas canvas1;
    CScreenConstants SCREEN_CONSTANTS;
    private float w = 0;
    private float h = w;

    public ViewShodashvargaNorthHora(Context context, CGlobalVariables.enuShodashvarga enumsho, CScreenConstants SCREEN_CONSTANTS, String[] planetLagna) {
        super(context);
        this.SCREEN_CONSTANTS = SCREEN_CONSTANTS;
        w = SCREEN_CONSTANTS.DeviceScreenWidth;
        h = w;
        _planetLagna = planetLagna;
        CGlobal.getCGlobalObject().getOutShodashvargaObject().calculatePlanetsInBhav(enumsho);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        canvas1 = canvas;
        drawHeading();
        drawView();
    }

    public void drawView() {
        showRasiInChart(CGlobal.getCGlobalObject().getOutShodashvargaObject().get_lagnaRashi());
        printPlanetsBhav1();
        try {
            printPlanetsBhav2();
        }catch (Exception e){

        }
    }


    public void drawHeading() {
        drawNorthKundli(canvas1);
    }

    private void drawNorthKundli(Canvas canvas) {

        canvas1.drawRect(SCREEN_CONSTANTS.HKUNDLISpace, SCREEN_CONSTANTS.VKUNDLISpace, SCREEN_CONSTANTS.HKUNDLISpace + w, SCREEN_CONSTANTS.VKUNDLISpace + h, SCREEN_CONSTANTS.KundliLineColor);

        canvas1.drawLine(SCREEN_CONSTANTS.HKUNDLISpace, SCREEN_CONSTANTS.VKUNDLISpace + h / 2, SCREEN_CONSTANTS.HKUNDLISpace + w, SCREEN_CONSTANTS.VKUNDLISpace + h / 2, SCREEN_CONSTANTS.KundliLineColor);


    }


    private void showRasiInChart(int iRasiNumber) {

        canvas1.drawText(String.valueOf(CGlobal.getCGlobalObject().getOutShodashvargaObject().get_horaRashi1()), SCREEN_CONSTANTS.NRashiX[0], SCREEN_CONSTANTS.NRashiY[0], SCREEN_CONSTANTS.KundliRasiNumbersCustomFont);

        canvas1.drawText(String.valueOf(CGlobal.getCGlobalObject().getOutShodashvargaObject().get_horaRashi2()), SCREEN_CONSTANTS.NRashiX[6], SCREEN_CONSTANTS.NRashiY[6], SCREEN_CONSTANTS.KundliRasiNumbersCustomFont);


    }


    private void printPlanetsBhav1() {
        int _temp = 0;
        ++iInxedColor;

        for (int i = 0; i < CGlobal.getCGlobalObject().getOutShodashvargaObject().getBhav1().size(); i++) {
            _temp = CGlobal.getCGlobalObject().getOutShodashvargaObject().getBhav1().get(i);
            if (iInxedColor > 5)
                iInxedColor = 0;
            if (SCREEN_CONSTANTS.NotShowUranusNeptunePlutoInChart) {
                if (_temp < CGlobalVariables.TOTAL_PLANETS_ON_NOT_SHOW_NEP_PLU_URA) {
                    if (i < 5)
                        canvas1.drawText(_planetLagna[_temp], SCREEN_CONSTANTS.NKundliPlanetX1[i], SCREEN_CONSTANTS.NKundliPlanetY1[i], SCREEN_CONSTANTS.KundliPlanetsCustomFont[iInxedColor]);
                    else
                        canvas1.drawText(_planetLagna[_temp], SCREEN_CONSTANTS.NKundliPlanetX2[i - 5], SCREEN_CONSTANTS.NKundliPlanetY2[i - 5], SCREEN_CONSTANTS.KundliPlanetsCustomFont[iInxedColor]);
                }

            } else {

                if (i < 5)
                    canvas1.drawText(_planetLagna[_temp], SCREEN_CONSTANTS.NKundliPlanetX1[i], SCREEN_CONSTANTS.NKundliPlanetY1[i], SCREEN_CONSTANTS.KundliPlanetsCustomFont[iInxedColor]);
                else
                    canvas1.drawText(_planetLagna[_temp], SCREEN_CONSTANTS.NKundliPlanetX2[i - 5], SCREEN_CONSTANTS.NKundliPlanetY2[i - 5], SCREEN_CONSTANTS.KundliPlanetsCustomFont[iInxedColor]);
            }

            ++iInxedColor;
        }

    }

    private void printPlanetsBhav2() {
        int _temp = 0;

        for (int i = 0; i < CGlobal.getCGlobalObject().getOutShodashvargaObject().getBhav2().size(); i++) {
            try {
                _temp = CGlobal.getCGlobalObject().getOutShodashvargaObject().getBhav2().get(i);
            }catch (Exception e){

            }
            if (iInxedColor > 5)
                iInxedColor = 0;
            if (SCREEN_CONSTANTS.NotShowUranusNeptunePlutoInChart) {
                if (_temp < CGlobalVariables.TOTAL_PLANETS_ON_NOT_SHOW_NEP_PLU_URA) {
                    if (i < 5)
                        canvas1.drawText(_planetLagna[_temp], SCREEN_CONSTANTS.NKundliPlanetX7[i], SCREEN_CONSTANTS.NKundliPlanetY7[i], SCREEN_CONSTANTS.KundliPlanetsCustomFont[iInxedColor]);
                    else
                        canvas1.drawText(_planetLagna[_temp], SCREEN_CONSTANTS.NKundliPlanetX8[i - 5], SCREEN_CONSTANTS.NKundliPlanetY8[i - 5], SCREEN_CONSTANTS.KundliPlanetsCustomFont[iInxedColor]);

                }

            } else {
                if (i < 5)
                    canvas1.drawText(_planetLagna[_temp], SCREEN_CONSTANTS.NKundliPlanetX7[i], SCREEN_CONSTANTS.NKundliPlanetY7[i], SCREEN_CONSTANTS.KundliPlanetsCustomFont[iInxedColor]);
                else
                    canvas1.drawText(_planetLagna[_temp], SCREEN_CONSTANTS.NKundliPlanetX8[i - 5], SCREEN_CONSTANTS.NKundliPlanetY8[i - 5], SCREEN_CONSTANTS.KundliPlanetsCustomFont[iInxedColor]);

            }

            ++iInxedColor;
        }

    }
}
