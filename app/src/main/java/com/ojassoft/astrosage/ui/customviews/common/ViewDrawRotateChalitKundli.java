package com.ojassoft.astrosage.ui.customviews.common;

import java.util.Arrays;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.Region;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.core.content.ContextCompat;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.OutputMasterActivity;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CScreenConstants;
import com.ojassoft.astrosage.utils.CGlobalVariables.enuKundliType;
import com.ojassoft.astrosage.utils.CUtils;

/**
 * This is a custom view to draw chalit kundli
 *
 * @author Bijendra(23-oct-13)
 */
public class ViewDrawRotateChalitKundli extends View {

    String[] _planetsName;
    double[] _plntDegree;
    double[] _bhavaDegree = new double[12];
    double[] org_bhavaDegree;
    Context _context;
    Canvas _canvas;
    float w = 0;
    float h = w;
    float centerX,centerY,offset,eastKundliOffset;
    CGlobalVariables.enuKundliType _kundliType;
    int lagna_position = -1,tappedPosition = -1;
    int clickedBhavaIndex = 0;
    private final Region[] cellList =  new Region[12];
    private final Path[] paths = new Path[12];
    float xDown = 0, yDown = 0, xUp = 0, yUp = 0;
    float x, y;
    boolean isTapped = false;
    private double _lagnaDegree = 0.0;
    String _lagnaName;

    int[] _planetsInRashi = new int[12];

    static int[] _southRashiNumber = {1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12};

    double[] _passedBhavaDegree = new double[12];
    double[] _southBhava_PlntDegree = new double[24];
    double[] _bhavaMidDegree = new double[12];
    double[] org_bhavaMidDegree;
    boolean _isTablet = false;
    float _xDistence = 1;
    CScreenConstants SCREEN_CONSTANTS;
    ViewDrawRotateKundli.OnKundliCellClickListener cellClickListener;
    Paint paint = new Paint();
    RectF resetButtonRect;
    public ViewDrawRotateChalitKundli(Context context, String[] planetsName,
                                      double[] plntDegree, double[] bhavaDegree, double[] bhavaMidDegree,
                                      CGlobalVariables.enuKundliType kundliType, String lagnaName, boolean isTablet, CScreenConstants SCREEN_CONSTANTS) {
        super(context);
        // TODO Auto-generated constructor stub
        _context = context;
        org_bhavaMidDegree = bhavaMidDegree;
        _planetsName = planetsName;
        _plntDegree = plntDegree;
        _lagnaName = lagnaName;
        _kundliType = kundliType;
        _isTablet = isTablet;
        org_bhavaDegree = bhavaDegree;
        _lagnaDegree = bhavaDegree[0] + 1.00;
        if (_lagnaDegree > 360.00)
            _lagnaDegree -= 360.00;

        this.SCREEN_CONSTANTS = SCREEN_CONSTANTS;
        w = SCREEN_CONSTANTS.DeviceScreenWidth;
        h = w;
        centerX = w/2f;
        centerY = h/2f;
        offset = h/4;
        eastKundliOffset = w/3;
        cellClickListener = _context instanceof OutputMasterActivity ? (OutputMasterActivity)_context : null;
        initValues(bhavaDegree,bhavaMidDegree);

    }

    /**
     * This function is used to init values
     *
     * @param bhavaDegree
     * @author Bijendra(24-oct-13)
     */
    private void initValues(double[] bhavaDegree,double[] bhavaMidDegree) {

        for (int i = 0; i < 12; i++) {
            _planetsInRashi[i] = ((int) (_plntDegree[i] / 30.0)) + 1;
            _bhavaDegree[i] = bhavaDegree[i];
            _bhavaMidDegree[i] = bhavaMidDegree[i];
            _passedBhavaDegree[i] = bhavaDegree[i];

            //_southBhava_PlntDegree[i]=bhavaDegree[i];
            _southBhava_PlntDegree[i] = _bhavaMidDegree[i];
            _southBhava_PlntDegree[12 + i] = _plntDegree[i];
        }

        Arrays.sort(_southBhava_PlntDegree);

		/* for (int i = 0; i < _southBhava_PlntDegree.length; i++)
			 //Log.e("SINGH", String.valueOf(i+1)+" "+String.valueOf(_southBhava_PlntDegree[i]));*/
    }

    @Override
    protected void onDraw(Canvas canvas) {

        _canvas = canvas;
        if (_kundliType == enuKundliType.NORTH) {
            drawNorthKundli();
            drawNorthRashiInBhava();
            drawNorthBhavaKundli();
            if(lagna_position != 1)
                drawResetButton();
        }
        if (_kundliType == enuKundliType.SOUTH)
            showSouthKundli();
        else if (_kundliType == enuKundliType.EAST)
            showEastKundli();

        if(isTapped && tappedPosition != -1){
            paint.setColor(ContextCompat.getColor(_context, R.color.appBtnRippleEffectColor));
            paint.setAlpha(60);
            canvas.drawPath(paths[tappedPosition],paint);
        }

    }


    /**
     * This function is used to print north kundli for phone
     *
     * @author Bijendra(24-oct-13)
     */
    private void printNorthKundliPlanetPhone() {
        x = y = 0;

        int index = 0, iInxedColor = 0;
        String s = "";
        double bhava1 = 0.0, bhava2 = 0.0;
        float[] arrXY = null;
        boolean isLagnaPrinted = false;
        for (int bhavaIndex = 0; bhavaIndex < 12; bhavaIndex++) {
            index = 0;
            iInxedColor = 0;
            bhava1 = _bhavaDegree[bhavaIndex];

            if (bhavaIndex < 11)
                bhava2 = _bhavaDegree[bhavaIndex + 1];
            else
                bhava2 = _bhavaDegree[0];
            for (int plntIndex = 0; plntIndex < 12; plntIndex++)

            {

                if ((SCREEN_CONSTANTS.NotShowUranusNeptunePlutoInChart) && (plntIndex == 9 || plntIndex == 10 || plntIndex == 11)) {
                    continue;
                } else {

                    // PHONE
                    if (hasInHouse(bhava2, bhava1, _plntDegree[plntIndex])) {
                        arrXY = getXYCoordinateForNorthKundliPlanet(bhavaIndex,
                                index);
                        _canvas.drawText(
                                _planetsName[plntIndex],
                                arrXY[0],
                                arrXY[1],
                                SCREEN_CONSTANTS.KundliPlanetsCustomFont[plntIndex]);
                        ++iInxedColor;
                        ++index;

                    }
                    if (iInxedColor > 5)
                        iInxedColor = 0;
                    if (index > 5)
                        index = 0;

                    // FHONE
                    if (!isLagnaPrinted) {
                        if (hasInHouse(bhava2, bhava1, _lagnaDegree)) {
                            isLagnaPrinted = true;
                            arrXY = getXYCoordinateForNorthKundliPlanet(
                                    bhavaIndex, index);
                            _canvas.drawText(
                                    _lagnaName,
                                    arrXY[0],
                                    arrXY[1],
                                    SCREEN_CONSTANTS.KundliPlanetsCustomFont[plntIndex]);
                            for(int i= 0;i<cellList.length;i++){
                                if(cellList[i] != null && cellList[i].contains((int) arrXY[0],(int)arrXY[1])){
                                    lagna_position = i+1;
                                    break;
                                }
                            }
                            ++iInxedColor;
                            ++index;

                        }
                        if (iInxedColor > 5)
                            iInxedColor = 0;
                        if (index > 5)
                            index = 0;
                    }

                }

            }

        }

    }


    /**
     * This function is used to print north kundli for tablet
     *
     * @author Bijendra(24-oct-13)
     */
    private void printNorthKundliPlanetTablet() {
        x = y = 0;

        int index = 0, iInxedColor = 0;
        String s = "";
        double bhava1 = 0.0, bhava2 = 0.0;
        float[] arrXY = null;
        boolean isLagnaPrinted = false;
        for (int bhavaIndex = 0; bhavaIndex < 12; bhavaIndex++) {
            index = 0;
            iInxedColor = 0;
            bhava1 = _bhavaDegree[bhavaIndex];

            if (bhavaIndex < 11)
                bhava2 = _bhavaDegree[bhavaIndex + 1];
            else
                bhava2 = _bhavaDegree[0];
            for (int plntIndex = 0; plntIndex < 12; plntIndex++)

            {

                if ((SCREEN_CONSTANTS.NotShowUranusNeptunePlutoInChart) && (plntIndex == 9 || plntIndex == 10 || plntIndex == 11)) {
                    continue;
                } else {

                    // TABLET
                    if (hasInHouse(bhava2, bhava1, _plntDegree[plntIndex])) {
                        arrXY = getXYCoordinateForNorthKundliPlanet(bhavaIndex,
                                index);
                        _canvas.drawText(
                                _planetsName[plntIndex],
                                arrXY[0],
                                arrXY[1],
                                SCREEN_CONSTANTS.KundliPlanetsCustomFont[iInxedColor]);

                        //++iInxedColor;
                        ++index;

						/*if (iInxedColor > 5)
							iInxedColor = 0;*/
                        if (index > 11)
                            index = 0;

                        arrXY = getXYCoordinateForNorthKundliPlanet(bhavaIndex,
                                index);
                        _canvas.drawText(
                                CUtils.FormatDegreeInDDMM(_plntDegree[plntIndex]),
                                arrXY[0],
                                arrXY[1],
                                SCREEN_CONSTANTS.KundliPlanetsCustomFont[iInxedColor]);

                        ++iInxedColor;
                        ++index;

                        if (iInxedColor > 5)
                            iInxedColor = 0;
                        if (index > 11)
                            index = 0;
                    }

                    // TABLET
                    if (!isLagnaPrinted) {
                        if (hasInHouse(bhava2, bhava1, _lagnaDegree)) {
                            isLagnaPrinted = true;
                            arrXY = getXYCoordinateForNorthKundliPlanet(
                                    bhavaIndex, index);
                            _canvas.drawText(
                                    _lagnaName,
                                    arrXY[0],
                                    arrXY[1],
                                    SCREEN_CONSTANTS.KundliPlanetsCustomFont[iInxedColor]);

                            //++iInxedColor;
                            ++index;

						/*	if (iInxedColor > 5)
								iInxedColor = 0;*/
                            if (index > 11)
                                index = 0;

                            arrXY = getXYCoordinateForNorthKundliPlanet(
                                    bhavaIndex, index);
                            _canvas.drawText(
                                    CUtils.FormatDegreeInDDMM(_lagnaDegree),
                                    arrXY[0],
                                    arrXY[1],
                                    SCREEN_CONSTANTS.KundliPlanetsCustomFont[iInxedColor]);

                            ++iInxedColor;
                            ++index;

                            if (iInxedColor > 5)
                                iInxedColor = 0;
                            if (index > 11)
                                index = 0;
                            for(int i= 0;i<cellList.length;i++){
                                if(cellList[i] != null && cellList[i].contains((int) arrXY[0],(int)arrXY[1])){
                                    lagna_position = i+1;
                                    break;
                                }
                            }
                        }

                    }

                }

            }

        }

    }

    /**
     * This function is used to decide to draw north kundli for phone/tablet
     *
     * @author Bijendra(24-oct-13)
     */
    private void drawNorthBhavaKundli() {

        if (_isTablet)
            printNorthKundliPlanetTablet();
        else
            printNorthKundliPlanetPhone();
    }

    /**
     * This function is used to return xy coordinate to print for north kundli
     *
     * @param rashiNumber
     * @param index
     * @return float[]
     */
    float[] getXYCoordinateForNorthKundliPlanet(int bhavaNumber, int index) {
        float[] arrXY = new float[2];
        switch (bhavaNumber) {
            case 0:
                if (_isTablet) {
                    arrXY[0] = SCREEN_CONSTANTS.NTKundliPlanetX1[index];
                    arrXY[1] = SCREEN_CONSTANTS.NTKundliPlanetY1[index];
                } else {
                    arrXY[0] = SCREEN_CONSTANTS.NKundliPlanetX1[index];
                    arrXY[1] = SCREEN_CONSTANTS.NKundliPlanetY1[index];
                }
                break;
            case 1:
                if (_isTablet) {
                    arrXY[0] = SCREEN_CONSTANTS.NTKundliPlanetX2[index];
                    arrXY[1] = SCREEN_CONSTANTS.NTKundliPlanetY2[index];
                } else {
                    arrXY[0] = SCREEN_CONSTANTS.NKundliPlanetX2[index];
                    arrXY[1] = SCREEN_CONSTANTS.NKundliPlanetY2[index];
                }
                break;
            case 2:
                if (_isTablet) {
                    arrXY[0] = SCREEN_CONSTANTS.NTKundliPlanetX3[index];
                    arrXY[1] = SCREEN_CONSTANTS.NTKundliPlanetY3[index];
                } else {
                    arrXY[0] = SCREEN_CONSTANTS.NKundliPlanetX3[index];
                    arrXY[1] = SCREEN_CONSTANTS.NKundliPlanetY3[index];
                }
                break;
            case 3:
                if (_isTablet) {
                    arrXY[0] = SCREEN_CONSTANTS.NTKundliPlanetX4[index];
                    arrXY[1] = SCREEN_CONSTANTS.NTKundliPlanetY4[index];
                } else {
                    arrXY[0] = SCREEN_CONSTANTS.NKundliPlanetX4[index];
                    arrXY[1] = SCREEN_CONSTANTS.NKundliPlanetY4[index];
                }
                break;
            case 4:
                if (_isTablet) {
                    arrXY[0] = SCREEN_CONSTANTS.NTKundliPlanetX5[index];
                    arrXY[1] = SCREEN_CONSTANTS.NTKundliPlanetY5[index];
                } else {
                    arrXY[0] = SCREEN_CONSTANTS.NKundliPlanetX5[index];
                    arrXY[1] = SCREEN_CONSTANTS.NKundliPlanetY5[index];
                }
                break;
            case 5:
                if (_isTablet) {
                    arrXY[0] = SCREEN_CONSTANTS.NTKundliPlanetX6[index];
                    arrXY[1] = SCREEN_CONSTANTS.NTKundliPlanetY6[index];
                } else {
                    arrXY[0] = SCREEN_CONSTANTS.NKundliPlanetX6[index];
                    arrXY[1] = SCREEN_CONSTANTS.NKundliPlanetY6[index];
                }
                break;
            case 6:
                if (_isTablet) {
                    arrXY[0] = SCREEN_CONSTANTS.NTKundliPlanetX7[index];
                    arrXY[1] = SCREEN_CONSTANTS.NTKundliPlanetY7[index];
                } else {
                    arrXY[0] = SCREEN_CONSTANTS.NKundliPlanetX7[index];
                    arrXY[1] = SCREEN_CONSTANTS.NKundliPlanetY7[index];
                }
                break;
            case 7:
                if (_isTablet) {
                    arrXY[0] = SCREEN_CONSTANTS.NTKundliPlanetX8[index];
                    arrXY[1] = SCREEN_CONSTANTS.NTKundliPlanetY8[index];
                } else {
                    arrXY[0] = SCREEN_CONSTANTS.NKundliPlanetX8[index];
                    arrXY[1] = SCREEN_CONSTANTS.NKundliPlanetY8[index];
                }
                break;
            case 8:
                if (_isTablet) {
                    arrXY[0] = SCREEN_CONSTANTS.NTKundliPlanetX9[index];
                    arrXY[1] = SCREEN_CONSTANTS.NTKundliPlanetY9[index];
                } else {
                    arrXY[0] = SCREEN_CONSTANTS.NKundliPlanetX9[index];
                    arrXY[1] = SCREEN_CONSTANTS.NKundliPlanetY9[index];
                }
                break;
            case 9:
                if (_isTablet) {
                    arrXY[0] = SCREEN_CONSTANTS.NTKundliPlanetX10[index];
                    arrXY[1] = SCREEN_CONSTANTS.NTKundliPlanetY10[index];
                } else {
                    arrXY[0] = SCREEN_CONSTANTS.NKundliPlanetX10[index];
                    arrXY[1] = SCREEN_CONSTANTS.NKundliPlanetY10[index];
                }
                break;
            case 10:
                if (_isTablet) {
                    arrXY[0] = SCREEN_CONSTANTS.NTKundliPlanetX11[index];
                    arrXY[1] = SCREEN_CONSTANTS.NTKundliPlanetY11[index];
                } else {
                    arrXY[0] = SCREEN_CONSTANTS.NKundliPlanetX11[index];
                    arrXY[1] = SCREEN_CONSTANTS.NKundliPlanetY11[index];
                }
                break;
            case 11:
                if (_isTablet) {
                    arrXY[0] = SCREEN_CONSTANTS.NTKundliPlanetX12[index];
                    arrXY[1] = SCREEN_CONSTANTS.NTKundliPlanetY12[index];
                } else {
                    arrXY[0] = SCREEN_CONSTANTS.NKundliPlanetX12[index];
                    arrXY[1] = SCREEN_CONSTANTS.NKundliPlanetY12[index];
                }
                break;

        }
        return arrXY;
    }

    /**
     * This function is used to draw rashi number in north kundli
     *
     * @author Bijendra(24-oct-13)
     */
    private void drawNorthRashiInBhava() {

        int iRasi = 0;

        for (int i = 0; i < 12; i++) {
            //iRasi = (int) (_bhavaDegree[i] / 30.0);
            iRasi = (int) (_bhavaMidDegree[i] / 30.0);
            iRasi += 1;
            if (_isTablet)
                _canvas.drawText(String.valueOf(iRasi),
                        SCREEN_CONSTANTS.NTRashiX[i],
                        SCREEN_CONSTANTS.NTRashiY[i],
                        SCREEN_CONSTANTS.KundliRasiNumbersCustomFont);
            else
                _canvas.drawText(String.valueOf(iRasi),
                        SCREEN_CONSTANTS.NRashiX[i],
                        SCREEN_CONSTANTS.NRashiY[i],
                        SCREEN_CONSTANTS.KundliRasiNumbersCustomFont);

        }

    }

    /**
     * This function is used to draw structure for north kundli
     *
     * @author Bijendra(24-oct-13)
     */
    private void drawNorthKundli() {

//        _canvas.drawRect(SCREEN_CONSTANTS.HKUNDLISpace,
//                SCREEN_CONSTANTS.VKUNDLISpace, SCREEN_CONSTANTS.HKUNDLISpace
//                        + w, SCREEN_CONSTANTS.VKUNDLISpace + h,
//                SCREEN_CONSTANTS.KundliLineColor); // (1)
//        _canvas.drawLine(SCREEN_CONSTANTS.HKUNDLISpace,
//                SCREEN_CONSTANTS.VKUNDLISpace, SCREEN_CONSTANTS.HKUNDLISpace
//                        + w, SCREEN_CONSTANTS.VKUNDLISpace + h,
//                SCREEN_CONSTANTS.KundliLineColor);// (2)
//        _canvas.drawLine(SCREEN_CONSTANTS.HKUNDLISpace + w,
//                SCREEN_CONSTANTS.VKUNDLISpace, SCREEN_CONSTANTS.HKUNDLISpace,
//                SCREEN_CONSTANTS.VKUNDLISpace + h,
//                SCREEN_CONSTANTS.KundliLineColor);// (3)
//
//        float[] lines = new float[]{(SCREEN_CONSTANTS.HKUNDLISpace + w) / 2,
//                SCREEN_CONSTANTS.VKUNDLISpace,
//                (SCREEN_CONSTANTS.HKUNDLISpace + w),
//                (SCREEN_CONSTANTS.VKUNDLISpace + h) / 2,
//                (SCREEN_CONSTANTS.HKUNDLISpace + w),
//                (SCREEN_CONSTANTS.VKUNDLISpace + h) / 2,
//                (SCREEN_CONSTANTS.HKUNDLISpace + w) / 2,
//                SCREEN_CONSTANTS.VKUNDLISpace + h,
//                (SCREEN_CONSTANTS.HKUNDLISpace + w) / 2,
//                SCREEN_CONSTANTS.VKUNDLISpace + h,
//                SCREEN_CONSTANTS.HKUNDLISpace,
//                (SCREEN_CONSTANTS.VKUNDLISpace + h) / 2,
//                SCREEN_CONSTANTS.HKUNDLISpace,
//                (SCREEN_CONSTANTS.VKUNDLISpace + h) / 2,
//                (SCREEN_CONSTANTS.HKUNDLISpace + w) / 2,
//                SCREEN_CONSTANTS.VKUNDLISpace
//
//        };

//        _canvas.drawLines(lines, SCREEN_CONSTANTS.KundliLineColor); //

        for(int i =0;i<12;i++){
            Path path = drawNorthCell(i);
            paths[i] = path;
            cellList[i] = createRegionFromPath(path);
            _canvas.drawPath(path,SCREEN_CONSTANTS.KundliLineColor);
        }


    }

    /**
     * This function is used to check that passed planet deg is in passed bhava degree
     *
     * @param cusp2
     * @param cusp1
     * @param plntDegree
     * @return boolean
     * @author Bijendra(24-oct-13)
     */
    public boolean hasInHouse(double cusp2, double cusp1, double plntDegree) {
        double temp2 = cusp2;
        double temp1 = cusp1;
        String s = "";

        if ((temp2 - temp1) < 0)
            temp2 += 360.00;

        if ((temp1 < (plntDegree + 360.0)) && ((plntDegree + 360.0) < temp2)) {
			/*
			 * s=String.valueOf(cusp2)+" <> "+String.valueOf(cusp1)+" <> "+String
			 * .valueOf(plntDegree); //Log.e("BIJENDRA", s);
			 */
            return true;
        }

        if ((temp1 < plntDegree) && (plntDegree < temp2)) {
			/*
			 * s=String.valueOf(cusp2)+" <> "+String.valueOf(cusp1)+" <> "+String
			 * .valueOf(plntDegree); //Log.e("SINGH", s);
			 */
            return true;
        }

        return false;
    }

    /**
     * This function is used to decide   north kundli to rotate or not
     *
     * @param x1
     * @param x2
     * @param y1
     * @param y2
     * @return boolean
     * @author Bijendra(24-oct-13)
     */
    private boolean isToRorateChart(float x1, float x2, float y1, float y2) {
        float[] diffxy = new float[2];
        diffxy[0] = x1 - x2;
        diffxy[1] = y1 - y2;
        int compare1 = Float.compare(diffxy[0], 0.0f);
        // if (diffxy[0] < 0.0)
        if (compare1 < 0) {
            diffxy[0] *= -1;
        }
        // if (diffxy[1] < 0)
        int compare2 = Float.compare(diffxy[1], 0.0f);
        if (compare2 < 0) {
            diffxy[1] *= -1;
        }
        // if (diffxy[0] < 5.0 && diffxy[1] < 5.0) {
        int compare3 = Float.compare(diffxy[0], 5.0f);
        int compare4 = Float.compare(diffxy[1], 5.0f);

        if (compare3 < 0 && compare4 < 0) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent me) {

        boolean isTouchEventCompleted = false;
        int action = me.getAction();
        switch (action) {
            case MotionEvent.ACTION_MOVE:
                return false;

            case MotionEvent.ACTION_DOWN:
                x = me.getX();
                y = me.getY();
                xDown = x;
                yDown = y;
                if(resetButtonRect != null && resetButtonRect.contains(x,y)){
                    return true;
                }
                for(int i=0;i<cellList.length;i++){
                    if(cellList[i] != null && cellList[i].contains((int)x,(int)y)){
                        isTapped = true;
                        tappedPosition = i;
                        invalidate();
                        break;
                    }
                }
                break;
            case MotionEvent.ACTION_UP:
                x = me.getX();
                y = me.getY();
                xUp = x;
                yUp = y;
                isTouchEventCompleted = true;
                break;
            case MotionEvent.ACTION_CANCEL:
                isTapped = false;
                tappedPosition = -1;
                invalidate();
                return true;
        }
        if (isTouchEventCompleted) {

            if (_kundliType == enuKundliType.NORTH) {
                if(resetButtonRect != null && resetButtonRect.contains(x,y)){
                    initValues(org_bhavaDegree,org_bhavaMidDegree);
                    invalidate();
                    return true;
                }
                if (isToRorateChart(x, xUp, y, yUp)) {
                    if (getTouchRashiNumber(x, y)) {
                        isTapped = false;
                        tappedPosition = -1;
                        invalidate();
                        return true;
                    }
                }
            }
            for (int cellNumber = 0; cellNumber < cellList.length; cellNumber++) {
                if (cellList[cellNumber] != null && cellList[cellNumber].contains((int) x, (int) y)) {
                    Log.d("DrawKundali","Lagna Position in onTouchEvent= " + lagna_position);
                    if (cellClickListener != null) {
                        int houseNumber = cellNumber - lagna_position + 1;
                        // Adjust for circular zodiac
                        if (houseNumber < 0) {
                            houseNumber += 12;
                        }
                        Log.d("DrawKundali","house number in onTouchEvent= " + houseNumber);
                        cellClickListener.onCellClicked(houseNumber);

                        tappedPosition = -1;
                        isTapped = false;
                        invalidate();
                        break;
                    }
                }
            }
        }
        return true;
    }

    /**
     * This function return touched rashi number to rotate
     *
     * @param x
     * @param y
     * @return boolean
     * @author Bijendra(24-oct-13)
     */
    private boolean getTouchRashiNumber(float x, float y) {
        boolean isBoxBumber = false;
        float diffX = 0, diffY = 0;

        for (int i = 0; i < 12; i++) {

            if (CUtils.isTablet(_context)) {
                diffX = x - SCREEN_CONSTANTS.NTRashiX[i];
                diffY = y - SCREEN_CONSTANTS.NTRashiY[i];
            } else {
                diffX = x - SCREEN_CONSTANTS.NRashiX[i];
                diffY = y - SCREEN_CONSTANTS.NRashiY[i];
            }

            int compare1 = Float.compare(diffX, 0.0f);
            if (compare1 < 0) {
                diffX *= -1;
            }
            int compare2 = Float.compare(diffY, 0.0f);
            if (compare2 < 0) {
                diffY *= -1;
            }

            // Hukum [ on 16 April ] changed (50.0f); from 20.0f to 50.0f for
            // making larger clickable area.
            int compare3 = Float.compare(diffX, 50.0f);
            int compare4 = Float.compare(diffY, 50.0f);
            if ((compare3 < 0) & (compare4 < 0)) {
                reInitBhavaArray(i);
                isBoxBumber = true;
                break;
            }
        }
        return isBoxBumber;
    }

    /**
     * This function is used to re initialize bhava array to rotate north kundli
     *
     * @param index
     * @author Bijendra(24-oct-13)
     * @author Hukum (16 - April - 2014) Rotated _bhavaMidDegree Array to rotate Rashi number.
     */
    private void reInitBhavaArray(int index) {
        int tempRound = 0, tempIndex = 0;
        double[] tempSort = new double[12];
        double[] tempSortForMidDegree = new double[12];
        for (int i = 0; i < 12; i++) {
            tempSort[i] = _bhavaDegree[i];
            tempSortForMidDegree[i] = _bhavaMidDegree[i];
        }
        tempIndex = index;
        do {
            _bhavaDegree[tempRound] = tempSort[tempIndex];
            _bhavaMidDegree[tempRound] = tempSortForMidDegree[tempIndex];
            ++tempRound;
            ++tempIndex;
            if (tempIndex > 11)
                tempIndex = 0;
        } while (tempRound < 12);

    }

    // SOUTH KUINDLI

    /*
     * South kundli functions
     */
    private void showSouthKundli() {
        if(lagna_position == -1) {
            lagna_position  = _planetsInRashi[11];
        }
        drawSouthKundli();
        drawSouthBhavaKundli();
    }

    public void showEastKundli() {
        if(lagna_position == -1) {
            lagna_position  = _planetsInRashi[11];
        }
        drawEastKundli();
        printEastKundliPlanet();
    }

    //Tejinder Show kundli East Kundli

    private void drawEastKundli() {

        /*_canvas.drawRect(SCREEN_CONSTANTS.HKUNDLISpace,
                SCREEN_CONSTANTS.VKUNDLISpace, SCREEN_CONSTANTS.HKUNDLISpace
                        + w, SCREEN_CONSTANTS.VKUNDLISpace + h,
                SCREEN_CONSTANTS.KundliLineColor); // (1)
        // first line horizontal
        _canvas.drawLine(SCREEN_CONSTANTS.HKUNDLISpace,
                ((SCREEN_CONSTANTS.VKUNDLISpace + h) / 4) + 68.0f,
                SCREEN_CONSTANTS.HKUNDLISpace + w,
                ((SCREEN_CONSTANTS.VKUNDLISpace + h) / 4) + 68.0f,
                SCREEN_CONSTANTS.KundliLineColor);// (2)

        //show diagnol line top Right
        _canvas.drawLine(SCREEN_CONSTANTS.HKUNDLISpace + w,
                SCREEN_CONSTANTS.HKUNDLISpace,
                (3 * (SCREEN_CONSTANTS.HKUNDLISpace + w) / 4) - 68.0f,
                ((SCREEN_CONSTANTS.VKUNDLISpace + h) / 4) + 68.0f,
                SCREEN_CONSTANTS.KundliLineColor);

        //show diagnol line top left
        _canvas.drawLine(SCREEN_CONSTANTS.HKUNDLISpace,
                SCREEN_CONSTANTS.HKUNDLISpace,
                ((SCREEN_CONSTANTS.HKUNDLISpace + w) / 4) + 68.0f,
                ((SCREEN_CONSTANTS.VKUNDLISpace + h) / 4) + 68.0f,
                SCREEN_CONSTANTS.KundliLineColor);

        //show diagnol line bottom left
        _canvas.drawLine(SCREEN_CONSTANTS.HKUNDLISpace,
                SCREEN_CONSTANTS.HKUNDLISpace + h,
                ((SCREEN_CONSTANTS.HKUNDLISpace + w) / 4) + 68.0f,
                (3 * (SCREEN_CONSTANTS.VKUNDLISpace + h) / 4) - 68.0f,
                SCREEN_CONSTANTS.KundliLineColor);

        //show diagnol line bottom Right
        _canvas.drawLine(SCREEN_CONSTANTS.HKUNDLISpace + w,
                SCREEN_CONSTANTS.HKUNDLISpace + h,
                (3 * (SCREEN_CONSTANTS.HKUNDLISpace + w) / 4) - 68.0f,
                (3 * (SCREEN_CONSTANTS.VKUNDLISpace + h) / 4) - 68.0f,
                SCREEN_CONSTANTS.KundliLineColor);


        //above than bottom line horizontal
        _canvas.drawLine(SCREEN_CONSTANTS.HKUNDLISpace,
                (3 * (SCREEN_CONSTANTS.VKUNDLISpace + h) / 4) - 68.0f,
                (SCREEN_CONSTANTS.HKUNDLISpace + w),
                (3 * (SCREEN_CONSTANTS.VKUNDLISpace + h) / 4) - 68.0f,
                SCREEN_CONSTANTS.KundliLineColor); // (5)
        //first vertical line
        _canvas.drawLine(((SCREEN_CONSTANTS.HKUNDLISpace + w) / 4) + 68.0f,
                SCREEN_CONSTANTS.VKUNDLISpace,
                ((SCREEN_CONSTANTS.HKUNDLISpace + w) / 4) + 68.0f,
                SCREEN_CONSTANTS.VKUNDLISpace + h,
                SCREEN_CONSTANTS.KundliLineColor); // (6)
        //second vertical line
        _canvas.drawLine((3 * (SCREEN_CONSTANTS.HKUNDLISpace + w) / 4) - 68.0f,
                SCREEN_CONSTANTS.VKUNDLISpace,
                (3 * (SCREEN_CONSTANTS.HKUNDLISpace + w) / 4) - 68.0f,
                SCREEN_CONSTANTS.VKUNDLISpace + h,
                SCREEN_CONSTANTS.KundliLineColor); // (7)*/
        for(int i=0;i<12;i++){
            Path path = drawEastKundliCell(i);
            cellList[i] = createRegionFromPath(path);
            paths[i] = path;
            _canvas.drawPath(path,SCREEN_CONSTANTS.KundliLineColor);
        }
    }


    /**
     * This function is used to return xy coordinate to print for south kundloi
     * @param rashiNumber
     * @param index
     * @return float[]
     */
	/*float[] getXYCoordinateForSouthKundliPlanet(int rashiNumber, int index) {
		float[] arrXY = new float[2];
		switch (rashiNumber) {
		case 0:
			if (_isTablet) {
				arrXY[0] = CGlobalVariables.STKundliPlanetX1[index];
				arrXY[1] = CGlobalVariables.STKundliPlanetY1[index];
			} else {
				arrXY[0] = CGlobalVariables.SKundliPlanetX1[index];
				arrXY[1] = CGlobalVariables.SKundliPlanetY1[index];
			}
			break;
		case 1:
			if (_isTablet) {
				arrXY[0] = CGlobalVariables.STKundliPlanetX2[index];
				arrXY[1] = CGlobalVariables.STKundliPlanetY2[index];
			} else {
				arrXY[0] = CGlobalVariables.SKundliPlanetX2[index];
				arrXY[1] = CGlobalVariables.SKundliPlanetY2[index];
			}
			break;
		case 2:
			if (_isTablet) {
				arrXY[0] = CGlobalVariables.STKundliPlanetX3[index];
				arrXY[1] = CGlobalVariables.STKundliPlanetY3[index];
			} else {
				arrXY[0] = CGlobalVariables.SKundliPlanetX3[index];
				arrXY[1] = CGlobalVariables.SKundliPlanetY3[index];
			}
			break;
		case 3:
			if (_isTablet) {
				arrXY[0] = CGlobalVariables.STKundliPlanetX4[index];
				arrXY[1] = CGlobalVariables.STKundliPlanetY4[index];
			} else {
				arrXY[0] = CGlobalVariables.SKundliPlanetX4[index];
				arrXY[1] = CGlobalVariables.SKundliPlanetY4[index];
			}
			break;
		case 4:
			if (_isTablet) {
				arrXY[0] = CGlobalVariables.STKundliPlanetX5[index];
				arrXY[1] = CGlobalVariables.STKundliPlanetY5[index];
			} else {
				arrXY[0] = CGlobalVariables.SKundliPlanetX5[index];
				arrXY[1] = CGlobalVariables.SKundliPlanetY5[index];
			}
			break;
		case 5:
			if (_isTablet) {
				arrXY[0] = CGlobalVariables.STKundliPlanetX6[index];
				arrXY[1] = CGlobalVariables.STKundliPlanetY6[index];
			} else {
				arrXY[0] = CGlobalVariables.SKundliPlanetX6[index];
				arrXY[1] = CGlobalVariables.SKundliPlanetY6[index];
			}
			break;
		case 6:
			if (_isTablet) {
				arrXY[0] = CGlobalVariables.STKundliPlanetX7[index];
				arrXY[1] = CGlobalVariables.STKundliPlanetY7[index];
			} else {
				arrXY[0] = CGlobalVariables.SKundliPlanetX7[index];
				arrXY[1] = CGlobalVariables.SKundliPlanetY7[index];
			}
			break;
		case 7:
			if (_isTablet) {
				arrXY[0] = CGlobalVariables.STKundliPlanetX8[index];
				arrXY[1] = CGlobalVariables.STKundliPlanetY8[index];
			} else {
				arrXY[0] = CGlobalVariables.SKundliPlanetX8[index];
				arrXY[1] = CGlobalVariables.SKundliPlanetY8[index];
			}
			break;
		case 8:
			if (_isTablet) {
				arrXY[0] = CGlobalVariables.STKundliPlanetX9[index];
				arrXY[1] = CGlobalVariables.STKundliPlanetY9[index];
			} else {
				arrXY[0] = CGlobalVariables.SKundliPlanetX9[index];
				arrXY[1] = CGlobalVariables.SKundliPlanetY9[index];
			}
			break;
		case 9:
			if (_isTablet) {
				arrXY[0] = CGlobalVariables.STKundliPlanetX10[index];
				arrXY[1] = CGlobalVariables.STKundliPlanetY10[index];
			} else {
				arrXY[0] = CGlobalVariables.SKundliPlanetX10[index];
				arrXY[1] = CGlobalVariables.SKundliPlanetY10[index];
			}
			break;
		case 10:
			if (_isTablet) {
				arrXY[0] = CGlobalVariables.STKundliPlanetX11[index];
				arrXY[1] = CGlobalVariables.STKundliPlanetY11[index];
			} else {
				arrXY[0] = CGlobalVariables.SKundliPlanetX11[index];
				arrXY[1] = CGlobalVariables.SKundliPlanetY11[index];
			}
			break;
		case 11:
			if (_isTablet) {
				arrXY[0] = CGlobalVariables.STKundliPlanetX12[index];
				arrXY[1] = CGlobalVariables.STKundliPlanetY12[index];
			} else {
				arrXY[0] = CGlobalVariables.SKundliPlanetX12[index];
				arrXY[1] = CGlobalVariables.SKundliPlanetY12[index];
			}
			break;

		}
		return arrXY;
	}*/

    /**
     * This function is used to draw kundli structure
     *
     * @author Bijendra(24-oct-13)
     */
    private void drawSouthKundli() {

/*
        _canvas.drawRect(SCREEN_CONSTANTS.HKUNDLISpace,
                SCREEN_CONSTANTS.VKUNDLISpace, SCREEN_CONSTANTS.HKUNDLISpace
                        + w, SCREEN_CONSTANTS.VKUNDLISpace + h,
                SCREEN_CONSTANTS.KundliLineColor); // (1)
        _canvas.drawLine(SCREEN_CONSTANTS.HKUNDLISpace,
                (SCREEN_CONSTANTS.VKUNDLISpace + h) / 4,
                SCREEN_CONSTANTS.HKUNDLISpace + w,
                (SCREEN_CONSTANTS.VKUNDLISpace + h) / 4,
                SCREEN_CONSTANTS.KundliLineColor); // (2)
        _canvas.drawLine(SCREEN_CONSTANTS.HKUNDLISpace,
                (SCREEN_CONSTANTS.VKUNDLISpace + h) / 2,
                (SCREEN_CONSTANTS.HKUNDLISpace + w) / 4,
                (SCREEN_CONSTANTS.VKUNDLISpace + h) / 2,
                SCREEN_CONSTANTS.KundliLineColor); // (3)
        _canvas.drawLine(3 * (SCREEN_CONSTANTS.HKUNDLISpace + w) / 4,
                (SCREEN_CONSTANTS.VKUNDLISpace + h) / 2,
                (SCREEN_CONSTANTS.HKUNDLISpace + w),
                (SCREEN_CONSTANTS.VKUNDLISpace + h) / 2,
                SCREEN_CONSTANTS.KundliLineColor); // (4)
        _canvas.drawLine(SCREEN_CONSTANTS.HKUNDLISpace,
                3 * (SCREEN_CONSTANTS.VKUNDLISpace + h) / 4,
                (SCREEN_CONSTANTS.HKUNDLISpace + w),
                3 * (SCREEN_CONSTANTS.VKUNDLISpace + h) / 4,
                SCREEN_CONSTANTS.KundliLineColor); // (5)
        _canvas.drawLine((SCREEN_CONSTANTS.HKUNDLISpace + w) / 4,
                SCREEN_CONSTANTS.VKUNDLISpace,
                (SCREEN_CONSTANTS.HKUNDLISpace + w) / 4,
                SCREEN_CONSTANTS.VKUNDLISpace + h,
                SCREEN_CONSTANTS.KundliLineColor); // (6)
        _canvas.drawLine(3 * (SCREEN_CONSTANTS.HKUNDLISpace + w) / 4,
                SCREEN_CONSTANTS.VKUNDLISpace,
                3 * (SCREEN_CONSTANTS.HKUNDLISpace + w) / 4,
                SCREEN_CONSTANTS.VKUNDLISpace + h,
                SCREEN_CONSTANTS.KundliLineColor); // (7)
        _canvas.drawLine((SCREEN_CONSTANTS.HKUNDLISpace + w) / 2,
                SCREEN_CONSTANTS.VKUNDLISpace,
                (SCREEN_CONSTANTS.HKUNDLISpace + w) / 2,
                (SCREEN_CONSTANTS.VKUNDLISpace + h) / 4,
                SCREEN_CONSTANTS.KundliLineColor); // (8)
        _canvas.drawLine((SCREEN_CONSTANTS.HKUNDLISpace + w) / 2,
                3 * (SCREEN_CONSTANTS.VKUNDLISpace + h) / 4,
                (SCREEN_CONSTANTS.HKUNDLISpace + w) / 2,
                SCREEN_CONSTANTS.VKUNDLISpace + h,
                SCREEN_CONSTANTS.KundliLineColor); // (9)
*/
        for(int i=0;i<12;i++){
            Path path = drawSouthKundliCell(i);
            cellList[i] = createRegionFromPath(path);
            paths[i] = path;
            _canvas.drawPath(path,SCREEN_CONSTANTS.KundliLineColor);
        }
    }

/**
 * This function is used to print south kundli for phone
 * @author Bijendra(24-oct-13)
 */
/*private void printSouthKundliPlanetPhone()
 {
		int rashi = 0, index = 0, iInxedColor = 0,returnNumber=0;
		float[] arrXY = null;
		for (int rashiIndex = 0; rashiIndex < 12; rashiIndex++) {
			index = 0;
			iInxedColor = 0;
			for (int j = 0; j < _southBhava_PlntDegree.length; j++) {
				rashi = (int) (_southBhava_PlntDegree[j] / 30.0);
				rashi +=1;
				if(rashi>12)
					rashi=1;
				if (_southRashiNumber[rashiIndex] == rashi) {

					if (isBhava(_southBhava_PlntDegree[j])) {
						
						returnNumber=getBhavaNumber(_southBhava_PlntDegree[j]);
						////Log.e("My BHAVA ", String.valueOf(returnNumber) + "  "+ String.valueOf(_southBhava_PlntDegree[j]));
						if(returnNumber>-1)
						{
							arrXY = getXYCoordinateForSouthKundliPlanet(rashiIndex,
									index);
							printSouhtPlanets(
									CGlobalVariables.ROMAN_NUMBERS[returnNumber],
									arrXY[0], arrXY[1], iInxedColor, false);
							++index;
							if (index > 5)
								index = 0;
		
							arrXY = getXYCoordinateForSouthKundliPlanet(rashiIndex,
									index);
							printSouhtPlanets(
									CUtils.FormatDegreeInDDMM(_southBhava_PlntDegree[j]),
									arrXY[0], arrXY[1], iInxedColor, true);
		
							
							}
					}

					if (isPlanet(_southBhava_PlntDegree[j])) {
						
						
						returnNumber=getPlanetnumber(_southBhava_PlntDegree[j]);
					
						if(returnNumber>-1)
						{
							arrXY = getXYCoordinateForSouthKundliPlanet(rashiIndex,
									index);
							printSouhtPlanets(_planetsName[returnNumber],
									arrXY[0], arrXY[1], iInxedColor, true);
						}
					}

					++iInxedColor;
					++index;

					if (iInxedColor > 5)
						iInxedColor = 0;
					if (index > 5)
						index = 0;
				}

			}
		}
		
}

*/

    /**
     * This function is used to print south kundli for tablet
     *
     * @author Bijendra(24-oct-13)
     * @modified Bijendra(28-oct-13)
     */
    private void printSouthKundliPlanet() {
        int iInxedColor = 0, returnNumber = 0;

        float[][] arrXYTab = null;
        double[] plntsInRashi = null;
        String concateString = "";
        for (int rashiIndex = 0; rashiIndex < 12; rashiIndex++) {
            plntsInRashi = totalPlanetsInRashiSouth(_southRashiNumber[rashiIndex], _southBhava_PlntDegree);
            if (plntsInRashi != null) {
                arrXYTab = getXYCoordinatesSouth(SCREEN_CONSTANTS.HKUNDLISpace, SCREEN_CONSTANTS.VKUNDLISpace, w, h, plntsInRashi.length, _southRashiNumber[rashiIndex]);
                for (int plntIndex = 0; plntIndex < plntsInRashi.length; plntIndex++) {
                    if (isBhava(plntsInRashi[plntIndex])) {
                        returnNumber = getBhavaNumber(plntsInRashi[plntIndex]);
                        if (returnNumber > -1) {
                            concateString = CGlobalVariables.ROMAN_NUMBERS[returnNumber] + CGlobalVariables.UnicodeSpace + CUtils.FormatDegreeInDDMM(plntsInRashi[plntIndex]);
                            printSouhtPlanets(concateString,
                                    arrXYTab[plntIndex][0], arrXYTab[plntIndex][1], iInxedColor, false);
                        }
                    }
                    if (isPlanet(plntsInRashi[plntIndex])) {
                        returnNumber = getPlanetnumber(plntsInRashi[plntIndex]);
                        if (returnNumber > -1) {
                            concateString = _planetsName[returnNumber] + CGlobalVariables.UnicodeSpace + CUtils.FormatDegreeInDDMM(plntsInRashi[plntIndex]);
                            printSouhtPlanets(concateString,
                                    arrXYTab[plntIndex][0], arrXYTab[plntIndex][1], iInxedColor, true);

                            ++iInxedColor;

                            if (iInxedColor > 5)
                                iInxedColor = 0;
                        }
                    }
                }
            }
        }


    }


////START


    private void printEastKundliPlanet() {
        int iInxedColor = 0, returnNumber = 0;

        float[][] arrXYTab = null;
        double[] plntsInRashi = null;
        String concateString = "";
        //tejinder
        for (int rashiIndex = 0; rashiIndex < 12; rashiIndex++) {
            plntsInRashi = totalPlanetsInRashiSouth(_southRashiNumber[rashiIndex], _southBhava_PlntDegree);
            if (plntsInRashi != null) {
                arrXYTab = getXYCoordinatesEast(SCREEN_CONSTANTS.HKUNDLISpace, SCREEN_CONSTANTS.VKUNDLISpace, w, h, plntsInRashi.length, _southRashiNumber[rashiIndex]);
                for (int plntIndex = 0; plntIndex < plntsInRashi.length; plntIndex++) {
                    if (isBhava(plntsInRashi[plntIndex])) {
                        returnNumber = getBhavaNumber(plntsInRashi[plntIndex]);
                        if (returnNumber > -1) {
                            concateString = CGlobalVariables.ROMAN_NUMBERS[returnNumber] + " " + CUtils.FormatDegreeInDDMMEast(plntsInRashi[plntIndex]);
                            printSouhtPlanets(concateString,
                                    arrXYTab[plntIndex][0], arrXYTab[plntIndex][1], iInxedColor, false);
                        }
                    }
                    if (isPlanet(plntsInRashi[plntIndex])) {
                        returnNumber = getPlanetnumber(plntsInRashi[plntIndex]);
                        if (returnNumber > -1) {
                            concateString = _planetsName[returnNumber] + " " + CUtils.FormatDegreeInDDMMEast(plntsInRashi[plntIndex]);
                            printSouhtPlanets(concateString,
                                    arrXYTab[plntIndex][0], arrXYTab[plntIndex][1], iInxedColor, true);

                            ++iInxedColor;

                            if (iInxedColor > 5)
                                iInxedColor = 0;
                        }
                    }
                }
            }
        }


    }


////END

    /**
     * This function is used to print string on canvas
     *
     * @param prBh
     * @param xx
     * @param yy
     * @param indexColor
     * @param isPlanet
     * @author Bijendra(24-oct-13)
     */
    private void printSouhtPlanets(String prBh, float xx, float yy, int indexColor, boolean isPlanet) {
        // PLANET
        if (isPlanet)
            _canvas.drawText(prBh, xx, yy, SCREEN_CONSTANTS.KundliPlanetsCustomFont[indexColor]);
        else//BHAVA
        {
            prBh = prBh.replace("-", ".");
            _canvas.drawText(prBh, xx, yy, SCREEN_CONSTANTS.KundliRomanNumCustomFont);
        }


    }

    private void drawSouthBhavaKundli() {

        printSouthKundliPlanet();

    }

    /**
     * This function is used to check that passed degree is bhava or not
     *
     * @param bhavaDeg
     * @return boolean
     * @author Bijendra(24-oct-13)
     */
    private boolean isBhava(double bhavaDeg) {
        for (int i = 0; i < 12; i++)
            if (Double.compare(bhavaDeg, _bhavaMidDegree[i]) == 0)
                return true;

        return false;
    }

    /**
     * This function is used to return bhava number(0-11)
     *
     * @param bhavaDeg
     * @return int
     * @author Bijendra(24-oct-13)
     */
    private int getBhavaNumber(double bhavaDeg) {
        int bhavaIndex = -1;
        for (int i = 0; i < 12; i++)
            if (Double.compare(bhavaDeg, _bhavaMidDegree[i]) == 0)
                bhavaIndex = i;

        return bhavaIndex;
    }

    /**
     * This function is used to check that passed degree is planet or not
     *
     * @param plntDeg
     * @return boolean
     * @author Bijendra(24-oct-13)
     */
    private boolean isPlanet(double plntDeg) {
        for (int plntIndex = 0; plntIndex < 12; plntIndex++) {
		/*if ((CGlobal.getCGlobalObject().getAppPreferences()
							.isNotShowPlUnNa())
							&& (plntIndex == 9 || plntIndex == 10 || plntIndex == 11)) {
						continue;
		}
		else
		{
			if(Double.compare(plntDeg, _plntDegree[plntIndex])==0)
				return true;
		}*/
            if ((SCREEN_CONSTANTS.NotShowUranusNeptunePlutoInChart) && (plntIndex == 9 || plntIndex == 10 || plntIndex == 11)) {
                continue;
            }else {
                if (Double.compare(plntDeg, _plntDegree[plntIndex]) == 0)
                    return true;
            }
        }

        return false;
    }

    /**
     * This function is ued to return planet number for passed degree
     *
     * @param plntDeg
     * @return int
     * @author Bijendra(24-oct-13)
     */
    private int getPlanetnumber(double plntDeg) {
        int plntIndex = -1;
        for (int i = 0; i < 12; i++) {
		/*if ((CGlobal.getCGlobalObject().getAppPreferences()
				.isNotShowPlUnNa())
				&& (i == 9 || i == 10 || i == 11))
		
			continue;		
		else
		{
			if(Double.compare(plntDeg, _plntDegree[i])==0)
				plntIndex=i;
		}*/
            if ((SCREEN_CONSTANTS.NotShowUranusNeptunePlutoInChart) && (plntIndex == 9 || plntIndex == 10 || plntIndex == 11)) {
                continue;
            }else {
                if (Double.compare(plntDeg, _plntDegree[i]) == 0)
                    plntIndex = i;
            }
        }
        //Log.e("RAM", String.valueOf(plntIndex));

        return plntIndex;
    }

    private void testSouth() {
        int index = 0, iInxedColor = 0;
        //float[] arrXY = null;

        //for (int rashiIndex = 0; rashiIndex < 12; rashiIndex++)
        //{
		
		/*for (int j = 0; j < CGlobalVariables.STKundliPlanetX1.length; j++) 
		{
			
			_canvas.drawText(String.valueOf(j+1), CGlobalVariables.STKundliPlanetX1[j],CGlobalVariables.STKundliPlanetY1[j],CGlobalVariables.kundliPlanetsCustomFont[iInxedColor]);		
			++index;
			++iInxedColor;
			if (index > 11)
				index = 0;
			if (iInxedColor > 5)
				iInxedColor = 0;
			//_canvas.drawText(String.valueOf(1), CGlobalVariables.STKundliPlanetX1[1],CGlobalVariables.STKundliPlanetY1[1],CGlobalVariables.kundliPlanetsCustomFont[iInxedColor]);		
		}*/
        //}
        float[][] arrXY = null;

        int[] boxArray = {1, 2, 3, 6, 7, 8, 9, 12};
        for (int k = 0; k < boxArray.length; k++) {
            arrXY = getXYCoordinatesSouth(SCREEN_CONSTANTS.HKUNDLISpace, SCREEN_CONSTANTS.VKUNDLISpace, w, h, 6, boxArray[k]);
            for (int i = 0; i < arrXY.length; i++) {
                try {
                    _canvas.drawText(String.valueOf(i + 1), arrXY[i][0], arrXY[i][1], SCREEN_CONSTANTS.KundliPlanetsCustomFont[iInxedColor]);
                    ++iInxedColor;
                    if (iInxedColor > 5)
                        iInxedColor = 0;
                } catch (Exception e) {

                }
            }
        }
        int[] boxArray1 = {4, 5, 10, 11};
        for (int k = 0; k < boxArray1.length; k++) {
            arrXY = getXYCoordinatesSouth(SCREEN_CONSTANTS.HKUNDLISpace, SCREEN_CONSTANTS.VKUNDLISpace, w, h, 6, boxArray1[k]);
            for (int i = 0; i < arrXY.length; i++) {
                try {
                    _canvas.drawText(String.valueOf(i + 1), arrXY[i][0], arrXY[i][1], SCREEN_CONSTANTS.KundliPlanetsCustomFont[iInxedColor]);
                    ++iInxedColor;
                    if (iInxedColor > 5)
                        iInxedColor = 0;
                } catch (Exception e) {

                }
            }
        }


    }

    /**
     * This function returns array of planets degree in the passed rashi number
     *
     * @param rashiNumber
     * @param deg
     * @return double []
     * @author Bijendra(28-oct-13)
     */
    private double[] totalPlanetsInRashiSouth(int rashiNumber, double[] deg) {
        int index = 0, temp = 0, indexDeg = 0;
        double[] _deg = null;
        for (int i = 0; i < deg.length; i++) {
            temp = (int) (deg[i] / 30.0) + 1;
            if (temp > 12)
                temp = 1;

            if (rashiNumber == temp)
                ++index;
        }

        if (index > 0) {
            _deg = new double[index];

            for (int i = 0; i < deg.length; i++) {
                temp = (int) (deg[i] / 30.0) + 1;
                if (temp > 12)
                    temp = 1;

                if (rashiNumber == temp) {
                    _deg[indexDeg] = deg[i];
                    ++indexDeg;
                }
            }
        }

        return _deg;
    }

    /**
     * This function is used to get south coordinates
     *
     * @param startX
     * @param startY
     * @param w
     * @param h
     * @param plntCount
     * @param boxNumber
     * @return float[][]
     * @author Bijendra(28-oct-13)
     */
    private float[][] getXYCoordinatesEast(float startX, float startY, float w, float h, int plntCount, int rashiNumber) {
        float xL = (float) (w / 3.0) + 61;
        float yL = (float) (h / 3.0);
        float yL1 = (float) (h / 3.0);
        float[][] arrXY = new float[plntCount][2];
        yL = (float) (yL * .75);


        float diffY = yL / (plntCount + 1);

        float _startX = 0;
        float _startY = startY;
        boolean upToDown = true;


        if (rashiNumber == 1 || rashiNumber == 2 || rashiNumber == 3 || rashiNumber == 5 || rashiNumber == 6 || rashiNumber == 7 || rashiNumber == 8 || rashiNumber == 9 || rashiNumber == 12) {
            switch (rashiNumber) {
                case 1:
                    if (_isTablet)
                        _startX = startX + 10 + xL;
                    else
                        _startX = startX + xL + 2;

                    _startY = startY + diffY;
                    break;
                case 2:
                    if (_isTablet)
                        _startX = startX + xL / 2 + 2 - 20;
                    else
                        _startX = startX + xL / 2 + 2 - 20;

                    _startY = startY + diffY;
                    break;
                case 3:
                    if (_isTablet)
                        _startX = startX + 2;
                    else
                        _startX = startX + 2;
                    _startY = startY + diffY + yL / 2 - 15;
                    break;

                case 5:
                    if (_isTablet)
                        _startX = startX + 2;
                    else
                        _startX = startX + 2;

                    _startY = startY + 2 * yL1 + diffY + 5;
                    break;

                case 6:
                    if (_isTablet)
                        _startX = startX + xL / 2 + 2 - 20;
                    else
                        _startX = startX + xL / 2 + 2 - 20;


                    _startY = startY + 2 * yL1 + diffY + 100;
                    break;
                case 7:
                    if (_isTablet)
                        _startX = startX + xL + 2;
                    else
                        _startX = startX + xL + 2;

                    _startY = startY + h;
                    break;
                case 8:
                    if (_isTablet)
                        _startX = startX + xL * 2 + 2 - 100;
                    else
                        _startX = startX + xL * 2 + 2 - 100;

                    _startY = startY + h + 41;
                    break;
                case 9:
                    if (_isTablet)
                        _startX = startX + xL * 2 + (xL / 2 - 64 * 2);
                    else
                        _startX = startX + xL * 2 + (xL / 2 - 64 * 2);

                    _startY = startY + h - 66;
                    break;
                case 12:
                    if (_isTablet)
                        _startX = startX + xL * 2 + 2 - 95;
                    else
                        _startX = startX + xL * 2 + 2 - 95;

                    _startY = startY + yL1 - 70;

                    break;

            }
            if (rashiNumber == 7 || rashiNumber == 8 || rashiNumber == 9 || rashiNumber == 12)
                upToDown = false;

            for (int x = 0; x < plntCount; x++)
                arrXY[x][0] = _startX;

            for (int y = 0; y < plntCount; y++) {
                if (upToDown)
                    arrXY[y][1] = _startY + diffY * y;
                else
                    arrXY[y][1] = _startY - diffY * (y + 1);
            }
        }

        if (rashiNumber == 4 || rashiNumber == 10 || rashiNumber == 11) {
            switch (rashiNumber) {
                case 4:
                    if (_isTablet)
                        _startX = startX + 61;
                    else
                        _startX = startX + 61;

                    _startY = startY + yL + diffY + 120;
                    break;

                case 10:
                    if (_isTablet)
                        _startX = startX + xL * 2;
                    else
                        _startX = startX + xL * 2;

                    _startY = startY + 2 * yL1;
                    break;
                case 11:
                    if (_isTablet)
                        _startX = startX + xL * 2 + (xL / 2 - 64 * 2);
                    else
                        _startX = startX + xL * 2 + (xL / 2 - 64 * 2);

                    _startY = startY + yL1 + 10;


            }

            if (rashiNumber == 10 || rashiNumber == 11 || rashiNumber == 9)
                upToDown = false;

            for (int x = 0; x < plntCount; x++)
                arrXY[x][0] = _startX;

            for (int y = 0; y < plntCount; y++) {
                if (upToDown)
                    arrXY[y][1] = _startY + diffY * y;
                else
                    arrXY[y][1] = _startY - diffY * (y + 1);
            }
        }

        return arrXY;
    }

//////East show

    private float[][] getXYCoordinatesSouth(float startX, float startY, float w, float h, int plntCount, int rashiNumber) {
        float xL = (float) (w / 4.0);
        float yL = (float) (h / 4.0);
        float[][] arrXY = new float[plntCount][2];

        float diffY = yL / (plntCount + 1);

        float _startX = 0;
        float _startY = startY;
        boolean upToDown = true;


        if (rashiNumber == 1 || rashiNumber == 2 || rashiNumber == 3 || rashiNumber == 6 || rashiNumber == 7 || rashiNumber == 8 || rashiNumber == 9 || rashiNumber == 12) {
            switch (rashiNumber) {
                case 1:
                    if (_isTablet)
                        _startX = startX + 10 + xL;
                    else
                        _startX = startX + xL + 2;

                    _startY = startY + diffY;
                    break;
                case 2:
                    if (_isTablet)
                        _startX = startX + 10 + 2 * xL;
                    else
                        _startX = startX + 2 * xL + 2;

                    _startY = startY + diffY;
                    break;
                case 3:
                    if (_isTablet)
                        _startX = startX + 10 + 3 * xL;
                    else
                        _startX = startX + 3 * xL + 2;

                    _startY = startY + diffY;
                    break;
                case 6:
                    if (_isTablet)
                        _startX = startX + 10 + 3 * xL;
                    else
                        _startX = startX + 3 * xL + 2;

                    _startY = startY + 3 * yL + diffY;
                    break;
                case 7:
                    if (_isTablet)
                        _startX = startX + 10 + 2 * xL;
                    else
                        _startX = startX + 2 * xL + 2;

                    _startY = startY + h;
                    break;
                case 8:
                    if (_isTablet)
                        _startX = startX + 10 + xL;
                    else
                        _startX = startX + xL + 2;

                    _startY = startY + h;
                    break;
                case 9:
                    if (_isTablet)
                        _startX = startX + 10;
                    else
                        _startX = startX + 2;

                    _startY = startY + h;
                    break;
                case 12:
                    if (_isTablet)
                        _startX = startX + 10;
                    else
                        _startX = startX + 2;

                    _startY = startY + yL;
                    break;

            }
            if (rashiNumber == 7 || rashiNumber == 8 || rashiNumber == 9 || rashiNumber == 12)
                upToDown = false;

            for (int x = 0; x < plntCount; x++)
                arrXY[x][0] = _startX;

            for (int y = 0; y < plntCount; y++) {
                if (upToDown)
                    arrXY[y][1] = _startY + diffY * y;
                else
                    arrXY[y][1] = _startY - diffY * (y + 1);
            }
        }

        if (rashiNumber == 4 || rashiNumber == 5 || rashiNumber == 10 || rashiNumber == 11) {
            switch (rashiNumber) {
                case 4:
                    if (_isTablet)
                        _startX = startX + 10 + 3 * xL;
                    else
                        _startX = startX + 3 * xL + 2;

                    _startY = startY + yL + diffY;
                    break;
                case 5:
                    if (_isTablet)
                        _startX = startX + 10 + 3 * xL;
                    else
                        _startX = startX + 3 * xL + 2;

                    _startY = startY + 2 * yL + diffY;
                    break;
                case 10:
                    if (_isTablet)
                        _startX = startX + 10;
                    else
                        _startX = startX + 2;

                    _startY = startY + 3 * yL;
                    break;
                case 11:
                    if (_isTablet)
                        _startX = startX + 10;
                    else
                        _startX = startX + 2;

                    _startY = startY + 2 * yL;


            }

            if (rashiNumber == 10 || rashiNumber == 11)
                upToDown = false;

            for (int x = 0; x < plntCount; x++)
                arrXY[x][0] = _startX;

            for (int y = 0; y < plntCount; y++) {
                if (upToDown)
                    arrXY[y][1] = _startY + diffY * y;
                else
                    arrXY[y][1] = _startY - diffY * (y + 1);
            }
        }

        return arrXY;
    }

    private Path drawNorthCell(int cellNumber){
        Path path = new Path();
        switch (cellNumber){
            case 0 :
                path.moveTo(centerX,SCREEN_CONSTANTS.HKUNDLISpace);
                path.lineTo(offset,offset);
                path.lineTo(centerX,centerY);
                path.lineTo(centerX+offset,offset);
                break;
            case 1 :
                path.moveTo(SCREEN_CONSTANTS.HKUNDLISpace,SCREEN_CONSTANTS.HKUNDLISpace);
                path.lineTo(offset,offset);
                path.lineTo(centerX,SCREEN_CONSTANTS.HKUNDLISpace);
                path.lineTo(SCREEN_CONSTANTS.HKUNDLISpace,SCREEN_CONSTANTS.HKUNDLISpace);
                break;
            case 2 :
                path.moveTo(SCREEN_CONSTANTS.HKUNDLISpace,SCREEN_CONSTANTS.HKUNDLISpace);
                path.lineTo(SCREEN_CONSTANTS.HKUNDLISpace,centerY);
                path.lineTo(offset,offset);
                break;
            case 3 :
                path.moveTo(SCREEN_CONSTANTS.HKUNDLISpace,centerY);
                path.lineTo(offset,offset);
                path.lineTo(centerX,centerY);
                path.lineTo(offset,centerY+offset);
                break;
            case 4 :
                path.moveTo(SCREEN_CONSTANTS.HKUNDLISpace,centerY);
                path.lineTo(offset,centerY+offset);
                path.lineTo(SCREEN_CONSTANTS.HKUNDLISpace,centerY*2);
                break;
            case 5 :
                path.moveTo(SCREEN_CONSTANTS.HKUNDLISpace,centerY*2);
                path.lineTo(centerX,centerY*2);
                path.lineTo(offset,centerY+offset);
                break;
            case 6 :
                path.moveTo(centerX,centerY*2);
                path.lineTo(centerX+offset,centerY+offset);
                path.lineTo(centerX,centerY);
                path.lineTo(offset,centerY+offset);
                break;
            case 7 :
                path.moveTo(centerX+offset,centerY+offset);
                path.lineTo(centerX,centerY*2);
                path.lineTo(centerX*2,centerY*2);
                break;
            case 8 :
                path.moveTo(centerX*2,centerY*2);
                path.lineTo(centerX*2,centerY);
                path.lineTo(centerX+offset,centerY+offset);
                break;
            case 9 :
                path.moveTo(centerX*2,centerY);
                path.lineTo(centerX+offset,offset);
                path.lineTo(centerX,centerY);
                path.lineTo(centerX+offset,centerY+offset);
                break;
            case 10 :
                path.moveTo(centerX*2,SCREEN_CONSTANTS.HKUNDLISpace);
                path.lineTo(centerX+offset,offset);
                path.lineTo(centerX*2,centerY);
                break;
            case  11 :
                path.moveTo(centerX,SCREEN_CONSTANTS.HKUNDLISpace);
                path.lineTo(centerX+offset,offset);
                path.lineTo(centerX*2,SCREEN_CONSTANTS.HKUNDLISpace);
        }
        path.close();
        return path;
    }

    private Path drawSouthKundliCell(int cellNumber){
        Path path = new Path();
        switch (cellNumber){
            case 0 :
                path.moveTo(offset,SCREEN_CONSTANTS.HKUNDLISpace);
                path.lineTo(centerX,SCREEN_CONSTANTS.HKUNDLISpace);
                path.lineTo(centerX,offset);
                path.lineTo(offset,offset);
                break;
            case 1 :
                path.moveTo(centerX,SCREEN_CONSTANTS.HKUNDLISpace);
                path.lineTo(centerX+offset,SCREEN_CONSTANTS.HKUNDLISpace);
                path.lineTo(centerX+offset,offset);
                path.lineTo(centerX,offset);
                break;
            case 2 :
                path.moveTo(centerX+offset,SCREEN_CONSTANTS.HKUNDLISpace);
                path.lineTo(centerX*2,SCREEN_CONSTANTS.HKUNDLISpace);
                path.lineTo(centerX*2,offset);
                path.lineTo(centerX+offset,offset);
                break;
            case 3 :
                path.moveTo(centerX+offset,offset);
                path.lineTo(centerX*2,offset);
                path.lineTo(centerX*2,centerY);
                path.lineTo(centerX+offset,centerY);
                break;
            case 4 :
                path.moveTo(centerX+offset,centerY);
                path.lineTo(centerX*2,centerY);
                path.lineTo(centerX*2,centerY+offset);
                path.lineTo(centerX+offset,centerY+offset);
                break;
            case 5 :
                path.moveTo(centerX+offset,centerY+offset);
                path.lineTo(centerX*2,centerY+offset);
                path.lineTo(centerX*2,centerY*2);
                path.lineTo(centerX+offset,centerY*2);
                break;
            case 6 :
                path.moveTo(centerX,centerY+offset);
                path.lineTo(centerX+offset,centerY+offset);
                path.lineTo(centerX+offset,centerY*2);
                path.lineTo(centerX,centerY*2);
                break;
            case 7 :
                path.moveTo(offset,centerY+offset);
                path.lineTo(centerX,centerY+offset);
                path.lineTo(centerX,centerY*2);
                path.lineTo(offset,centerY*2);
                break;
            case 8 :
                path.moveTo(SCREEN_CONSTANTS.HKUNDLISpace,centerY+offset);
                path.lineTo(offset,centerY+offset);
                path.lineTo(offset,centerY*2);
                path.lineTo(SCREEN_CONSTANTS.HKUNDLISpace,centerY*2);
                break;
            case 9 :
                path.moveTo(SCREEN_CONSTANTS.HKUNDLISpace,centerY);
                path.lineTo(offset,centerY);
                path.lineTo(offset,centerY+offset);
                path.lineTo(SCREEN_CONSTANTS.HKUNDLISpace,centerY+offset);
                break;
            case 10 :
                path.moveTo(SCREEN_CONSTANTS.HKUNDLISpace,offset);
                path.lineTo(offset,offset);
                path.lineTo(offset,centerY);
                path.lineTo(SCREEN_CONSTANTS.HKUNDLISpace,centerY);
                break;
            case 11 :
                path.moveTo(SCREEN_CONSTANTS.HKUNDLISpace,SCREEN_CONSTANTS.HKUNDLISpace);
                path.lineTo(offset,SCREEN_CONSTANTS.HKUNDLISpace);
                path.lineTo(offset,offset);
                path.lineTo(SCREEN_CONSTANTS.HKUNDLISpace,offset);
                break;

        }
        path.close();
        return path;
    }

    private Region createRegionFromPath(Path path) {
        // Define a bounding rectangle for the region
        RectF bounds = new RectF();
        path.computeBounds(bounds, true);
        Region region = new Region();
        region.setPath(path, new Region((int) bounds.left, (int) bounds.top, (int) bounds.right, (int) bounds.bottom));
        return region;
    }

    private Path drawEastKundliCell(int cellNumber){
        Path path = new Path();
        switch (cellNumber){
            case  0 :
                path.moveTo(eastKundliOffset,SCREEN_CONSTANTS.HKUNDLISpace);
                path.lineTo(eastKundliOffset*2,SCREEN_CONSTANTS.HKUNDLISpace);
                path.lineTo(eastKundliOffset*2,eastKundliOffset);
                path.lineTo(eastKundliOffset,eastKundliOffset);
                break;
            case 1 :
                path.moveTo(SCREEN_CONSTANTS.HKUNDLISpace,SCREEN_CONSTANTS.HKUNDLISpace);
                path.lineTo(eastKundliOffset,SCREEN_CONSTANTS.HKUNDLISpace);
                path.lineTo(eastKundliOffset,eastKundliOffset);
                break;
            case 2 :
                path.moveTo(SCREEN_CONSTANTS.HKUNDLISpace,SCREEN_CONSTANTS.HKUNDLISpace);
                path.lineTo(eastKundliOffset,eastKundliOffset);
                path.lineTo(SCREEN_CONSTANTS.HKUNDLISpace,eastKundliOffset);
                break;
            case 3 :
                path.moveTo(SCREEN_CONSTANTS.HKUNDLISpace,eastKundliOffset);
                path.lineTo(eastKundliOffset,eastKundliOffset);
                path.lineTo(eastKundliOffset,eastKundliOffset*2);
                path.lineTo(SCREEN_CONSTANTS.HKUNDLISpace,eastKundliOffset*2);
                break;
            case 4 :
                path.moveTo(SCREEN_CONSTANTS.HKUNDLISpace,eastKundliOffset*2);
                path.lineTo(eastKundliOffset,eastKundliOffset*2);
                path.lineTo(SCREEN_CONSTANTS.HKUNDLISpace,h);
                break;
            case 5 :
                path.moveTo(SCREEN_CONSTANTS.HKUNDLISpace,h);
                path.lineTo(eastKundliOffset,eastKundliOffset*2);
                path.lineTo(eastKundliOffset,h);
                break;
            case 6 :
                path.moveTo(eastKundliOffset,eastKundliOffset*2);
                path.lineTo(eastKundliOffset*2,eastKundliOffset*2);
                path.lineTo(eastKundliOffset*2,h);
                path.lineTo(eastKundliOffset,h);
                break;
            case 7 :
                path.moveTo(eastKundliOffset*2,eastKundliOffset*2);
                path.lineTo(w,h);
                path.lineTo(eastKundliOffset*2,h);
                break;
            case 8 :
                path.moveTo(eastKundliOffset*2,eastKundliOffset*2);
                path.lineTo(w,eastKundliOffset*2);
                path.lineTo(w,h);
                break;
            case 9 :
                path.moveTo(eastKundliOffset*2,eastKundliOffset);
                path.lineTo(w,eastKundliOffset);
                path.lineTo(w,eastKundliOffset*2);
                path.lineTo(eastKundliOffset*2,eastKundliOffset*2);
                break;
            case 10 :
                path.moveTo(eastKundliOffset*2,eastKundliOffset);
                path.lineTo(w,SCREEN_CONSTANTS.HKUNDLISpace);
                path.lineTo(w,eastKundliOffset);
                break;
            case 11 :
                path.moveTo(eastKundliOffset*2,SCREEN_CONSTANTS.HKUNDLISpace);
                path.lineTo(w,SCREEN_CONSTANTS.HKUNDLISpace);
                path.lineTo(eastKundliOffset*2,eastKundliOffset);
                break;
        }
        path.close();
        return path;
    }

    Paint buttonPaint = new Paint();

    private void drawResetButton(){
        buttonPaint.reset();
        float size = CUtils.convertDpToPx(_context,40);
        float margin = CUtils.convertDpToPx(_context,10);
        Paint shadowPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        shadowPaint.setColor(ContextCompat.getColor(_context,R.color.no_change_black));
        shadowPaint.setAlpha(25); // Semi-transparent
        shadowPaint.setStyle(Paint.Style.FILL);
        RectF shadowRectF = new RectF(w-(size+CUtils.convertDpToPx(_context,11)),h-(size+CUtils.convertDpToPx(_context,11)),w-CUtils.convertDpToPx(_context,6),h-CUtils.convertDpToPx(_context,6));
        _canvas.drawOval(shadowRectF,shadowPaint);
        resetButtonRect = new RectF(w-(size+margin),h-(size+margin),w-margin,h-margin);
        buttonPaint.setColor(ContextCompat.getColor(_context,R.color.colorPrimary_day_night));
        _canvas.drawOval(resetButtonRect,buttonPaint);
        Bitmap bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.ic_sync),(int) (size-CUtils.convertDpToPx(_context,10)),(int) (size-CUtils.convertDpToPx(_context,10)),true);
        ColorFilter filter = new PorterDuffColorFilter(ContextCompat.getColor(_context,R.color.white), PorterDuff.Mode.SRC_IN);
        buttonPaint.setColorFilter(filter);
        _canvas.drawBitmap(bitmap,w-(size+CUtils.convertDpToPx(_context,6)),h-(size+CUtils.convertDpToPx(_context,6)),buttonPaint);
    }
//END //////

}

