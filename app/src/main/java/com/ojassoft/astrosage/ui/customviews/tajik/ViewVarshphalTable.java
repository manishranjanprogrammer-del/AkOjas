package com.ojassoft.astrosage.ui.customviews.tajik;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

import com.ojassoft.astrosage.misc.CGenerateAppViews;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CScreenConstants;
import com.ojassoft.astrosage.utils.CUtils;

public class ViewVarshphalTable extends View {
    Context context;
    private double[] plntDegree;
    private int[] plntInRasi;
    private String[] planetName;
    private String[] rasiName;
    private String[] heading;
    private String degSign;
    private String minSign;
    private String secSign;
    private CScreenConstants SCREEN_CONSTANTS;
    private int startX = 10;
    private int xLength = 0;
    int y1 = 0;
    int _index = 0;
    private float[] xCoordinate;

    int topY, bottomY;

    public ViewVarshphalTable(Context context, double[] plntDegree,
                              int[] plntInRasi, String[] planetName, String[] rasiName,
                              String[] heading, String degSign, String minSign, String secSign,
                              CScreenConstants SCREEN_CONSTANTS, int languageCode, String munthaBhavHeading, String muntha) {
        super(context);
        this.context = context;
        this.plntDegree = plntDegree;
        this.plntInRasi = plntInRasi;
        this.planetName = planetName;
        this.rasiName = rasiName;
        this.heading = heading;
        this.degSign = degSign;
        this.minSign = minSign;
        this.secSign = secSign;
        this.SCREEN_CONSTANTS = SCREEN_CONSTANTS;
//		this.languageCode = languageCode;

        initVariables();

    }

    private void initVariables() {

        xLength = ((int) SCREEN_CONSTANTS.DeviceScreenWidth) / 4;
        xCoordinate = new float[]{startX, (startX + (1.4f) * xLength), startX + (2.65f) * xLength};
        for (int i = 0; i < plntDegree.length; i++) {
            plntDegree[i] = plntDegree[i] % 30;
        }
    }

    private void showHeading(Canvas canvas) {
        xLength = ((int) SCREEN_CONSTANTS.DeviceScreenWidth) / 4;
        y1 = CScreenConstants.pxToDp(CScreenConstants.topMargin, context) - CScreenConstants.pxToDp(20, getContext());
        // y1 = ((int) SCREEN_CONSTANTS.DeviceScreenHeight) / 18-CScreenConstants.pxToDp(CScreenConstants.noteTopMarign,getContext());
        for (int i = 0; i < heading.length; i++) {
            canvas.drawText(heading[i], xCoordinate[i], y1, SCREEN_CONSTANTS.headingPaint);
        }
        y1 = y1 + CScreenConstants.pxToDp(CScreenConstants.firstLineTopMargin, getContext());
        showPlanetTable(canvas);
    }

    private void showPlanetTable(Canvas canvas) {
        String formattedPlanetDeg = "";


        for (int i = 0; i < planetName.length - 1; i++) {
            if ((i % 2 == 0)) {
                if (this.SCREEN_CONSTANTS.NewDeviceScreenWidth < CGlobalVariables.SMALL_DEVICE_WIDTH) {
                    canvas.drawRect(3, y1 - CScreenConstants.pxToDp(CScreenConstants.margininline - CScreenConstants.rectHieghtMarginTop, getContext()), this.SCREEN_CONSTANTS.NewDeviceScreenWidth, y1 + CScreenConstants.pxToDp(-CScreenConstants.rectHieghtMarginBottom, getContext()), SCREEN_CONSTANTS.TableRow_Background_Color);
                } else {
                    canvas.drawRect(3, y1 - CScreenConstants.pxToDp(CScreenConstants.margininline - CScreenConstants.rectHieghtMarginTop, getContext()), this.SCREEN_CONSTANTS.NewDeviceScreenWidth, y1 + CScreenConstants.pxToDp(CScreenConstants.rectHieghtMarginBottom, getContext()), SCREEN_CONSTANTS.TableRow_Background_Color);
                }
            }
            if (i == 0) {
                //FOR LAGNA
                canvas.drawText(planetName[planetName.length - 2],
                        xCoordinate[0], y1, SCREEN_CONSTANTS.contentPaint);
                // planet in rasi
                canvas.drawText(
                        rasiName[plntInRasi[planetName.length - 2] - 1],
                        xCoordinate[1], y1, SCREEN_CONSTANTS.contentPaint);
                // planet degree
                formattedPlanetDeg = CUtils.FormatDMSInStringWithSign(
                        plntDegree[planetName.length - 2], degSign, minSign,
                        secSign);
                canvas.drawText(formattedPlanetDeg, xCoordinate[2], y1,
                        SCREEN_CONSTANTS.contentPaint);
            } else {
                // planet name
                canvas.drawText(planetName[i - 1], xCoordinate[0], y1, SCREEN_CONSTANTS.contentPaint);
                // planet in rasi
                canvas.drawText(rasiName[plntInRasi[i - 1] - 1], xCoordinate[1], y1, SCREEN_CONSTANTS.contentPaint);
                // planet degree
                formattedPlanetDeg = CUtils.FormatDMSInStringWithSign(plntDegree[i - 1], degSign, minSign, secSign);
                canvas.drawText(formattedPlanetDeg, xCoordinate[2], y1, SCREEN_CONSTANTS.contentPaint);
            }
            y1 = y1 + CScreenConstants.pxToDp(CScreenConstants.margininline, getContext());
            ;
        }
        CGenerateAppViews.setParamsOfViewVarshphalTable(y1);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // TODO Auto-generated method stub
        super.onDraw(canvas);
        showHeading(canvas);

    }
}
