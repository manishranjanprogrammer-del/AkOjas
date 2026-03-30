package com.ojassoft.astrosage.ui.customviews.common;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.RectF;
import android.graphics.Region;
import android.text.Html;
import android.text.Layout;
import android.text.Spanned;
import android.text.StaticLayout;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;


import androidx.core.content.ContextCompat;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.Planet;
import com.ojassoft.astrosage.beans.PlanetsAndColor;
import com.ojassoft.astrosage.ui.act.OutputMasterActivity;
import com.ojassoft.astrosage.ui.customcontrols.FlowTextView;
import com.ojassoft.astrosage.utils.CGlobal;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CGlobalVariables.enuKundliType;
import com.ojassoft.astrosage.utils.CScreenConstants;
import com.ojassoft.astrosage.utils.CUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ViewDrawRotateKundli extends View {

    int indx1 = 0, indx2 = 0, indx3 = 0, indx4 = 0, indx5 = 0, indx6 = 0,
            indx7 = 0, indx8 = 0, indx9 = 0, indx10 = 0, indx11 = 0,
            indx12 = 0;
    String[] _planetsName;
    int[] _planetsInRashi;
    double[] _planetsDegree = null;
    private int iInxedColor = 0;
    CGlobalVariables.enuKundliType _kundliType;
    CScreenConstants SCREEN_CONSTANTS;
    Context _context = null;
    Canvas _canvas;
    float w = 0;
    float h = 0;
    float centerX,centerY,offset,eastKundliOffset;
    int laganKarksaOne;
    int lagna_position = -1,tappedPosition =-1;
    private int[] chartRashi = new int[12];
    float x, y;
    boolean isDrawFirstTime = false;
    //variable declare show karkas and swars
    //@Tejinder Singh on 14-jan-2016
    boolean isDrawKarkasSwars;
    int[] selectFirstRashi;
    int _lagna = 0;
    //end of variable declare
    private final Region[] cellList =  new Region[12];
    private final Path[] paths =  new Path[12];
    RectF resetButtonRect;
    float xDown = 0, yDown = 0, xUp = 0, yUp = 0;
    boolean isTapped = false;
    boolean isDeviceTablet = false;
    int _chartName = -1;
    int _languageCode = -1;
    boolean isShowDirectOrCumbust = false;
    private String unicodeBargotttam = "\u2e0b";
    private String unicodeExalted = "\ua71b";
    private String unicodeDebiliated = "\ua71c";
    private OnKundliCellClickListener cellClickListener;
    private boolean isForTransit = false;
    Paint paint = new Paint();
    private boolean isKundliRotated =false;

    public ViewDrawRotateKundli(Context context, String[] planetsName,
                                 int[] planetsInRashi, CGlobalVariables.enuKundliType kundliType,
                                 boolean isTablet, CScreenConstants _SCREEN_CONSTANTS,
                                 int chartName, int languageCode) {
        super(context);

        Log.e("SAN ", " VDRK ViewDrawRotateKundli() inside 1" );
        isForTransit = false;
        _context = context;
        _planetsName = planetsName;
        _planetsInRashi = planetsInRashi;
        _kundliType = kundliType;
        isDeviceTablet = isTablet;
        SCREEN_CONSTANTS = _SCREEN_CONSTANTS;
        _chartName = chartName;
        _languageCode = languageCode;
        initValues(false, 0, planetsInRashi);

    }

    /* @Tenjinder 14-jan-2016
     * Make constuctor make the chart for swamsa*/
    public ViewDrawRotateKundli(Context context, String[] planetsName,
                                 int[] planetsInRashi, CGlobalVariables.enuKundliType kundliType,
                                 boolean isTablet, CScreenConstants _SCREEN_CONSTANTS,
                                 int chartName, int languageCode, String args, double[] planetsDegree) {
        super(context);

        Log.e("SAN ", " VDRK ViewDrawRotateKundli() inside 2" );
        isForTransit = false;
        _context = context;
        _planetsName = planetsName;
        _planetsInRashi = planetsInRashi;
        _kundliType = kundliType;
        isDeviceTablet = isTablet;
        SCREEN_CONSTANTS = _SCREEN_CONSTANTS;
        _chartName = chartName;
        _languageCode = languageCode;
        _planetsDegree = planetsDegree;
        int laganKarksa = calculateHigestDegree(planetsDegree);
        selectFirstRashi = planetsInRashi;
        isDrawKarkasSwars = true;
        initValues(true, laganKarksa, planetsInRashi);
    }

    //By Mahtab for Gochar
    public ViewDrawRotateKundli(Context context, String[] planetsName,
                                 int[] planetsInRashi, double[] planetsDegree,
                                 CGlobalVariables.enuKundliType kundliType, boolean isTablet,
                                 CScreenConstants _SCREEN_CONSTANTS, int chartName, int languageCode, int[] _planetsInRashiTransitLagna, boolean isShowDirectOrCumbust) {
        super(context);

        Log.e("SAN ", " VDRK ViewDrawRotateKundli() inside 3" );
        isForTransit = true;
        _context = context;
        _planetsName = planetsName;
        _planetsInRashi = planetsInRashi;
        _kundliType = kundliType;
        _planetsDegree = planetsDegree;
        isDeviceTablet = isTablet;
        SCREEN_CONSTANTS = _SCREEN_CONSTANTS;
        _chartName = chartName;
        //_planetsInRashiTransitLagna=_planetsInRashiTransitLagna;
        _languageCode = languageCode;
        this.isShowDirectOrCumbust = isShowDirectOrCumbust;
        initValuesTransit(false, 0, _planetsInRashiTransitLagna);
        if(_context instanceof OutputMasterActivity) cellClickListener = (OutputMasterActivity) _context;

    }

    //end
    public ViewDrawRotateKundli(Context context, String[] planetsName,
                                 int[] planetsInRashi, double[] planetsDegree,
                                 CGlobalVariables.enuKundliType kundliType, boolean isTablet,
                                 CScreenConstants _SCREEN_CONSTANTS, int chartName, int languageCode, boolean isShowDirectOrCumbust) {
        super(context);

        Log.e("SAN ", " VDRK ViewDrawRotateKundli() inside 4 " );

        isForTransit = false;
        _context = context;
        _planetsName = planetsName;
        _planetsInRashi = planetsInRashi;
        _kundliType = kundliType;
        _planetsDegree = planetsDegree;
        isDeviceTablet = isTablet;
        SCREEN_CONSTANTS = _SCREEN_CONSTANTS;
        _chartName = chartName;
        _languageCode = languageCode;
        initValues(false, 0, planetsInRashi);
        this.isShowDirectOrCumbust = isShowDirectOrCumbust;

    }

    /* @Tenjinder 14-jan-2016
     * Make constuctor make the chart for Karakamsha*/
    public ViewDrawRotateKundli(Context context, String[] planetsName,
                                 int[] planetsInRashi, double[] planetsDegree,
                                 CGlobalVariables.enuKundliType kundliType, boolean isTablet,
                                 CScreenConstants _SCREEN_CONSTANTS, int chartName, int languageCode, String karksa, int calculateAtmkark[]) {
        super(context);

        Log.e("SAN ", " VDRK ViewDrawRotateKundli() inside 5" );

        isForTransit = false;
        _context = context;
        _planetsName = planetsName;
        _planetsInRashi = planetsInRashi;
        _kundliType = kundliType;
        _planetsDegree = planetsDegree;
        isDeviceTablet = isTablet;
        SCREEN_CONSTANTS = _SCREEN_CONSTANTS;
        _chartName = chartName;
        _languageCode = languageCode;

        int laganKarksa = calculateHigestDegree(planetsDegree);
        isDrawKarkasSwars = true;
        selectFirstRashi = calculateAtmkark;
        initValues(true, laganKarksa, selectFirstRashi);
    }

    //calculate the HigestDegree plant to show karakamsha and swamsa
    public int calculateHigestDegree(double[] planetsDegree) {
        double higestDegreeNumber = 0.0;
        double higestValue = 0.0;
        int position = 0;
        for (int i = 0; i <= 6; i++) {
            higestDegreeNumber = planetsDegree[i] % 30;
            if (higestDegreeNumber > higestValue) {
                position = i;
                higestValue = higestDegreeNumber;
            }
        }
        return position;
    }

    //End of Work

    private void fillChartRashi(int lagnaNumber) {

        for (int i = 0; i < 12; i++) {
            chartRashi[i] = lagnaNumber;
            ++lagnaNumber;
            if (lagnaNumber > 12)
                lagnaNumber = 1;
        }
    }

    private void initValues(boolean isDefaultValueTaken, int value, int selectFirstRashi[]) {
        Log.e("SAN ", " VDRK initValues() inside" );
        w = SCREEN_CONSTANTS.DeviceScreenWidth;
        h = w;
        centerX = w/2f;
        centerY = h/2f;
        offset = h/4;
        eastKundliOffset = w/3;
        Log.e("SAN ", " VDRK w => " + w );
        Log.e("SAN ", " VDRK h => " + h );
        x = y = 0;
        /*Tejinder Singh
         *  take _planet for lagna to draw diagram  */
        if (!isDefaultValueTaken)
            _lagna = _planetsInRashi[12];
        else if (isDefaultValueTaken) {
            _lagna = selectFirstRashi[value];
        }
        laganKarksaOne = _lagna;
        fillChartRashi(_lagna);
        initVariablesWithDefaultvalues();
        if(_context instanceof OutputMasterActivity) cellClickListener = (OutputMasterActivity) _context;

    }

    @Override
    protected void onDraw(Canvas canvas) {

        _canvas = canvas;

        initVariablesWithDefaultvalues();

        if (_kundliType == enuKundliType.NORTH) {
            if (!isDrawFirstTime) {
                isDrawFirstTime = true;
                Log.e("SAN ", " VDRK onDraw showNorthKundli() ");
                showNorthKundli();
            } else
                ReDrawNorthKundli();
        }
        if (_kundliType == enuKundliType.SOUTH)
            showSouthKundli();
        if (_kundliType == enuKundliType.EAST)
            showEastKundli();

        /* ADDED BY BIJENDRA TO SHOW PLANT DEGREE ON 07-05-15 */
        try {
            if (/*
             * _kundliType == enuKundliType.NORTH &&
             */(_chartName == CGlobalVariables.SUB_MODULE_BASIC_LAGNA_CHART)){
                 showPlanetsDegreeBelowKundli();
            }
        } catch (Exception e) {
            Log.i("Exception--", e.getMessage());
        }
        if(isTapped && tappedPosition != -1){
            paint.setColor(ContextCompat.getColor(_context,R.color.appBtnRippleEffectColor));
            paint.setAlpha(60);
            canvas.drawPath(paths[tappedPosition],paint);
        }

    }

    /**
     * This function is used to show planet degree below north kundli
     * <p/>
     * 07-May-2015
     */
    private void showPlanetsDegreeBelowKundli() {
        //int colorIndex = 0;
        // SAN might be this method for Degree

        float w1 = w / 3;
        float h1 = (SCREEN_CONSTANTS.VKUNDLISpace + h) / 12;

        float x1 = SCREEN_CONSTANTS.HKUNDLISpace + 10;
        float x2 = (x1 + w1 / 4) - 5;
        float x3 = x1 + w1;
        float x4 = (x3 + w1 / 4) - 5;
        float x5 = x3 + w1;
        float x6 = (x5 + w1 / 4) - 5;
        if (_languageCode == CGlobalVariables.HINDI) {
            x2 -= 15;
            x4 -= 15;
            x6 -= 15;
        }
        float[] x_axis1 = {x1, x3, x5};
        float[] x_axis2 = {x2, x4, x6};
        float y0 = SCREEN_CONSTANTS.VKUNDLISpace + h + h1;
        float y1 = y0 + h1 + 5;
        float y2 = y1 + h1;
        float y3 = y2 + h1;
        float y4 = y3 + h1;
        float y5 = y4 + h1;
        float[] y_axis;
        float[] y_axis_lagna = {y1, y2, y3, y4, y5};
        float[] y_axis_transit = {y0, y1, y2, y3, y4};
        int planetIndex = 0;

        if (isShowDirectOrCumbust) {
            y_axis = y_axis_lagna;
            // SAN draw Retrograde, Combust, Vagouttam heading
            drawRetrogradeAndCombustHintText(x1, y0, h1);

        } else {
            y_axis = y_axis_transit;
        }


        for (int row = 0; row < 5; row++) {
            Log.i("row>>", "" + row);
            for (int column = 0; column < 3; column++) {
                if (planetIndex < _planetsName.length - 1) {

                    String planetName;
                    String planetDegree;
                    Paint paint;

                    if (planetIndex == 0) {
                        planetName = _planetsName[12];
                        planetDegree = CUtils.FormatDegreeInDDMMSS(_planetsDegree[12], _languageCode);
                        paint = SCREEN_CONSTANTS.KundliPlanetsCustomFont[12];

                    } else {
                        planetName = _planetsName[planetIndex - 1];
                        planetDegree = CUtils.FormatDegreeInDDMMSS(_planetsDegree[planetIndex - 1], _languageCode);
                        paint = SCREEN_CONSTANTS.KundliPlanetsCustomFont[planetIndex - 1];

                    }
                    // SAN added for font text same
                    //paint.setTextSize(SCREEN_CONSTANTS.TextColor.getTextSize());
                    // SAN Bottom content write from here
                    float tmpHeight = isForTransit == true ? h1 : h1 * 6;
                    _canvas.drawText(planetName, x_axis1[column], y_axis[row] + tmpHeight, paint);
                    _canvas.drawText(planetDegree, x_axis2[column], y_axis[row] + tmpHeight, paint);
                    Log.i("planetIndex>>", "" + planetIndex);
                    planetIndex++;
                    //colorIndex++;
                }
            }
        }








      /*// FIRST COLUMN
      for (int i = 0; i < 4; i++) {

         if (colorIndex > 5)
            colorIndex = 0;

         // FIRST COLUMN
         _canvas.drawText(_planetsName[i], x1, SCREEN_CONSTANTS.VKUNDLISpace
               + h + ((i + 1) * h1),
               SCREEN_CONSTANTS.KundliPlanetsCustomFont[colorIndex]);

         // PRINT PLANET DEGREE FOR IST COLUMN PLANETS
         try {
            _canvas.drawText(CUtils.FormatDegreeInDDMMSS(_planetsDegree[i],
                  _languageCode), x2, SCREEN_CONSTANTS.VKUNDLISpace + h
                  + ((i + 1) * h1),
                  SCREEN_CONSTANTS.KundliPlanetsCustomFont[colorIndex]);
         } catch (Exception e) {
         }
         colorIndex++;

         // SECOND COLUMN
         if (colorIndex > 5)
            colorIndex = 0;

         _canvas.drawText(_planetsName[i + 4], x3,
               SCREEN_CONSTANTS.VKUNDLISpace + h + ((i + 1) * h1),
               SCREEN_CONSTANTS.KundliPlanetsCustomFont[colorIndex]);

         // PRINT PLANET DEGREE FOR IST COLUMN PLANETS
         try {
            _canvas.drawText(CUtils.FormatDegreeInDDMMSS(
                  _planetsDegree[i + 4], _languageCode), x4,
                  SCREEN_CONSTANTS.VKUNDLISpace + h + ((i + 1) * h1),
                  SCREEN_CONSTANTS.KundliPlanetsCustomFont[colorIndex]);
         } catch (Exception e) {
         }
         colorIndex++;

         // THIRD COLUMN
         if (colorIndex > 5)
            colorIndex = 0;
         _canvas.drawText(_planetsName[i + 8], x5,
               SCREEN_CONSTANTS.VKUNDLISpace + h + ((i + 1) * h1),
               SCREEN_CONSTANTS.KundliPlanetsCustomFont[colorIndex]);

         // PRINT PLANET DEGREE FOR IST COLUMN PLANETS
         try {
            _canvas.drawText(CUtils.FormatDegreeInDDMMSS(
                  _planetsDegree[i + 8], _languageCode), x6,
                  SCREEN_CONSTANTS.VKUNDLISpace + h + ((i + 1) * h1),
                  SCREEN_CONSTANTS.KundliPlanetsCustomFont[colorIndex]);
         } catch (Exception e) {
         }
         colorIndex++;

      }*/


    }

    private void ReDrawNorthKundli() {
        double tempPlntDegree = -1;
        int _lagna = 0;
        drawNorthKundli();
        ArrayList<PlanetsAndColor> planetName = getPlantNameWithDirectAndRedirect();

        // drawNorthKundli();
        if (planetName != null && planetName.size() > 0) {
            _lagna = chartRashi[0];
            initVariablesWithDefaultvalues();
            // _lagna=_planetsInRashi[12];

            showNorthRashiInKundli(_lagna);
            if(isDrawKarkasSwars){
                lagna_position = returnPlanetInBhavaInNorthKundli(_lagna,laganKarksaOne);
            }else {
                lagna_position = returnPlanetInBhavaInNorthKundli(_lagna,
                        _planetsInRashi[_planetsInRashi.length - 1]);
            }
            for (int i = 0; i < _planetsInRashi.length; i++) {
                int pntInBhava = returnPlanetInBhavaInNorthKundli(_lagna,
                        _planetsInRashi[i]);
                if (_planetsDegree != null)
                    tempPlntDegree = _planetsDegree[i];

                if ((SCREEN_CONSTANTS.NotShowUranusNeptunePlutoInChart)
                        && (i == 9 || i == 10 || i == 11)) {
                    continue;
                } else if (i == 12 && isDrawKarkasSwars) {

                } else {
                    // Log.d("Agra"+String.valueOf(i), _planetsName[i]);
                    try {
                        switch (pntInBhava) {
                            case 1:
                                printPlanetInNorthKundliBhav1(planetName.get(i),
                                        tempPlntDegree);
                                break;
                            case 2:
                                printPlanetInNorthKundliBhav2(planetName.get(i),
                                        tempPlntDegree);
                                break;
                            case 3:
                                printPlanetInNorthKundliBhav3(planetName.get(i),
                                        tempPlntDegree);
                                break;
                            case 4:
                                printPlanetInNorthKundliBhav4(planetName.get(i),
                                        tempPlntDegree);
                                break;
                            case 5:
                                printPlanetInNorthKundliBhav5(planetName.get(i),
                                        tempPlntDegree);
                                break;
                            case 6:
                                printPlanetInNorthKundliBhav6(planetName.get(i),
                                        tempPlntDegree);
                                break;
                            case 7:
                                printPlanetInNorthKundliBhav7(planetName.get(i),
                                        tempPlntDegree);
                                break;
                            case 8:
                                printPlanetInNorthKundliBhav8(planetName.get(i),
                                        tempPlntDegree);
                                break;
                            case 9:
                                printPlanetInNorthKundliBhav9(planetName.get(i),
                                        tempPlntDegree);
                                break;
                            case 10:
                                printPlanetInNorthKundliBhav10(planetName.get(i),
                                        tempPlntDegree);
                                break;
                            case 11:
                                printPlanetInNorthKundliBhav11(planetName.get(i),
                                        tempPlntDegree);
                                break;
                            case 12:
                                printPlanetInNorthKundliBhav12(planetName.get(i),
                                        tempPlntDegree);
                                break;

                        }
                    } catch (Exception e) {

                    }
                }
            }
        }
        if(isKundliRotated)
            drawResetButton();
    }
    int rotate_lagna;
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
                 rotate_lagna = chartRashi[i];
                fillChartRashi(rotate_lagna);
                isBoxBumber = true;
                break;
            }
        }

        return isBoxBumber;
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
                    isDrawFirstTime = false;
                    if(isDrawKarkasSwars){
                        int value = calculateHigestDegree(_planetsDegree);
                        _lagna  = selectFirstRashi[value];
                    }else {
                        _lagna = _planetsInRashi[12];
                    }
                    fillChartRashi(_lagna);
                    isKundliRotated = false;
                    invalidate();
                    return true;
                }
                if (isToRorateChart(x, xUp, y, yUp)) {
                    if (getTouchRashiNumber(x, y)) {
                        isTapped = false;
                        tappedPosition = -1;
                        isKundliRotated = rotate_lagna != _lagna;
                        invalidate();
                        return true;
                    }
                }
            }
            Log.d("DrawKundali","Lagna Position in onTouchEvent= " + lagna_position);
            for (int cellNumber = 0; cellNumber < cellList.length; cellNumber++) {
                if (cellList[cellNumber] != null && cellList[cellNumber].contains((int) x, (int) y)) {
                    //Log.d("Kundali Click", "Clicked on call = " + i);
                    if (cellClickListener != null) {
                            int houseNumber = cellNumber - lagna_position + 1;
                            if (houseNumber < 0) {
                                houseNumber += 12;
                            }
                        Log.d("DrawKundali","house number in onTouchEvent= " + houseNumber);
                         cellClickListener.onCellClicked(houseNumber);
                            tappedPosition = -1;
                            isTapped = false;
                            invalidate();
                    }
                }
            }
        }
        return true;
    }

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

    private void initVariablesWithDefaultvalues() {
        indx1 = 0;
        indx2 = 0;
        indx3 = 0;
        indx4 = 0;
        indx5 = 0;
        indx6 = 0;
        indx7 = 0;
        indx8 = 0;
        indx9 = 0;
        indx10 = 0;
        indx11 = 0;
        indx12 = 0;
        xDown = 0;
        yDown = 0;
        xUp = 0;
        yUp = 0;
    }

    /*
     * North Kundli functions
     */
    private void showNorthKundli() {
        double tempPlntDegree = -1;
        resetButtonRect = null;
        //int _lagna = 0;
        Log.e("SAN ", " VDRK onDraw showNorthKundli() inside ");
        drawNorthKundli();
        ArrayList<PlanetsAndColor> planetName = getPlantNameWithDirectAndRedirect();
        //Log.d("DarwNorthKundali","Planets Name = " + _planetsName[12]+ "Planets rashi = " + _planetsInRashi[12]);
        //_lagna = _planetsInRashi[12];
        if (planetName != null && planetName.size() > 0) {
            Log.e("SAN ", " VDRK onDraw showNorthKundli() planetName.size() => " + planetName.size() );
            showNorthRashiInKundli(_lagna);
            if(isDrawKarkasSwars){
                lagna_position = returnPlanetInBhavaInNorthKundli(_lagna,laganKarksaOne);

            }else {
                lagna_position = returnPlanetInBhavaInNorthKundli(_lagna,
                        _planetsInRashi[_planetsInRashi.length - 1]);
            }
            for (int i = 0; i < _planetsInRashi.length; i++) {
                int pntInBhava = returnPlanetInBhavaInNorthKundli(_lagna,
                        _planetsInRashi[i]);
                if (_planetsDegree != null)
                    tempPlntDegree = _planetsDegree[i];

                // if((CGlobal.getCGlobalObject().getAppPreferences().isNotShowPlUnNa())&&(i==9||i==10||i==11))
                if ((SCREEN_CONSTANTS.NotShowUranusNeptunePlutoInChart)
                        && (i == 9 || i == 10 || i == 11)) {
                    continue;
                } else if (i == 12 && isDrawKarkasSwars) {
                    // Toast.makeText(_context, "notfound:"+_planetsName[i], Toast.LENGTH_SHORT).show();
                } else {
                    switch (pntInBhava) {
                        case 1:
                            printPlanetInNorthKundliBhav1(planetName.get(i),
                                    tempPlntDegree);
                            break;
                        case 2:
                            printPlanetInNorthKundliBhav2(planetName.get(i),
                                    tempPlntDegree);
                            break;
                        case 3:
                            printPlanetInNorthKundliBhav3(planetName.get(i),
                                    tempPlntDegree);
                            break;
                        case 4:
                            printPlanetInNorthKundliBhav4(planetName.get(i),
                                    tempPlntDegree);
                            break;
                        case 5:
                            printPlanetInNorthKundliBhav5(planetName.get(i),
                                    tempPlntDegree);
                            break;
                        case 6:
                            printPlanetInNorthKundliBhav6(planetName.get(i),
                                    tempPlntDegree);
                            break;
                        case 7:
                            printPlanetInNorthKundliBhav7(planetName.get(i),
                                    tempPlntDegree);
                            break;
                        case 8:
                            printPlanetInNorthKundliBhav8(planetName.get(i),
                                    tempPlntDegree);
                            break;
                        case 9:
                            printPlanetInNorthKundliBhav9(planetName.get(i),
                                    tempPlntDegree);
                            break;
                        case 10:
                            printPlanetInNorthKundliBhav10(planetName.get(i),
                                    tempPlntDegree);
                            break;
                        case 11:
                            printPlanetInNorthKundliBhav11(planetName.get(i),
                                    tempPlntDegree);
                            break;
                        case 12:
                            printPlanetInNorthKundliBhav12(planetName.get(i),
                                    tempPlntDegree);
                            break;

                    }
                }
            }
        }
    }

    private int returnPlanetInBhavaInNorthKundli(int lagnaRashi, int plntRashi) {
        int _iTemo = 0;
        _iTemo = plntRashi - lagnaRashi;
        if (_iTemo < 0)
            _iTemo += 12;
        _iTemo += 1;
        return _iTemo;
    }

    private void drawNorthKundli() {

        Log.e("SAN ", " VDRK drawNorthKundli() " );

        /*_canvas.drawRect(SCREEN_CONSTANTS.HKUNDLISpace,
                SCREEN_CONSTANTS.VKUNDLISpace, SCREEN_CONSTANTS.HKUNDLISpace
                        + w, SCREEN_CONSTANTS.VKUNDLISpace + h,
                SCREEN_CONSTANTS.KundliLineColor);*/
        for(int i =0;i<12;i++){
            Path path = drawNorthCell(i);
            paths[i] = path;
            cellList[i] = createRegionFromPath(path);
            _canvas.drawPath(path,SCREEN_CONSTANTS.KundliLineColor);
        }
        /*_canvas.drawLine(SCREEN_CONSTANTS.HKUNDLISpace,
                SCREEN_CONSTANTS.VKUNDLISpace, SCREEN_CONSTANTS.HKUNDLISpace
                        + w, SCREEN_CONSTANTS.VKUNDLISpace + h,
                SCREEN_CONSTANTS.KundliLineColor);

        _canvas.drawLine(SCREEN_CONSTANTS.HKUNDLISpace + w,
                SCREEN_CONSTANTS.VKUNDLISpace, SCREEN_CONSTANTS.HKUNDLISpace,
                SCREEN_CONSTANTS.VKUNDLISpace + h,
                SCREEN_CONSTANTS.KundliLineColor);*/

        float[] lines = new float[]{(SCREEN_CONSTANTS.HKUNDLISpace + w) / 2,
                SCREEN_CONSTANTS.VKUNDLISpace,
                (SCREEN_CONSTANTS.HKUNDLISpace + w),
                (SCREEN_CONSTANTS.VKUNDLISpace + h) / 2,
                (SCREEN_CONSTANTS.HKUNDLISpace + w),
                (SCREEN_CONSTANTS.VKUNDLISpace + h) / 2,
                (SCREEN_CONSTANTS.HKUNDLISpace + w) / 2,
                SCREEN_CONSTANTS.VKUNDLISpace + h,
                (SCREEN_CONSTANTS.HKUNDLISpace + w) / 2,
                SCREEN_CONSTANTS.VKUNDLISpace + h,
                SCREEN_CONSTANTS.HKUNDLISpace,
                (SCREEN_CONSTANTS.VKUNDLISpace + h) / 2,
                SCREEN_CONSTANTS.HKUNDLISpace,
                (SCREEN_CONSTANTS.VKUNDLISpace + h) / 2,
                (SCREEN_CONSTANTS.HKUNDLISpace + w) / 2,
                SCREEN_CONSTANTS.VKUNDLISpace

        };

        //_canvas.drawLines(lines, SCREEN_CONSTANTS.KundliLineColor); //

    }

    private void showNorthRashiInKundli(int iRasiNumber) {
        int iRasi = 0;
        iRasi = iRasiNumber;
        if (iRasi > 12)
            iRasi = 1;
        try {

            for (int i = 0; i < 12; i++) {
                if (isDeviceTablet)
                    _canvas.drawText(String.valueOf(iRasi),
                            SCREEN_CONSTANTS.NTRashiX[i],
                            SCREEN_CONSTANTS.NTRashiY[i],
                            SCREEN_CONSTANTS.KundliRasiNumbersCustomFont);
                else
                    _canvas.drawText(String.valueOf(iRasi),
                            SCREEN_CONSTANTS.NRashiX[i],
                            SCREEN_CONSTANTS.NRashiY[i],
                            SCREEN_CONSTANTS.KundliRasiNumbersCustomFont);
                iRasi += 1;

                if (iRasi > 12)
                    iRasi = 1;
            }
        } catch (Exception e) {

        }

    }

    private void printPlanetInNorthKundliBhav1(PlanetsAndColor planetsAndColor, double plntDegree) {

        if (iInxedColor > 5)
            iInxedColor = 0;

        if (isDeviceTablet) {

            if (indx1 > 11)
                indx1 = 1;
            _canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.NTKundliPlanetX1[indx1],
                    SCREEN_CONSTANTS.NTKundliPlanetY1[indx1],
                    planetsAndColor.getPaint());
            indx1++;

            if (indx1 > 11)
                indx1 = 1;

            if (plntDegree > 0) {
                _canvas.drawText(CUtils.FormatDegreeInDDMM(plntDegree),
                        SCREEN_CONSTANTS.NTKundliPlanetX1[indx1],
                        SCREEN_CONSTANTS.NTKundliPlanetY1[indx1],
                        planetsAndColor.getPaint());
            }
            indx1++;

        } else {
            if (indx1 > 5)
                indx1 = 0;

            /*_canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.NKundliPlanetX1[indx1],
                    SCREEN_CONSTANTS.NKundliPlanetY1[indx1],
                    planetsAndColor.getPaint());*/

            // SAN changes for retroNcombust display on bottom
            if ( planetsAndColor.getPlanetName().contains("^") || planetsAndColor.getPlanetName().contains("*")
                    || planetsAndColor.getPlanetName().contains(unicodeBargotttam) ||
                    planetsAndColor.getPlanetName().contains(unicodeExalted) ||
                    planetsAndColor.getPlanetName().contains(unicodeDebiliated)  ) {

                String planetName = planetsAndColor.getPlanetName(); //.substring(0, 2);
                //String retroNcombust = planetsAndColor.getPlanetName().substring(2);

                _canvas.drawText(planetName, SCREEN_CONSTANTS.NKundliPlanetX1[indx1],
                        SCREEN_CONSTANTS.NKundliPlanetY1[indx1],
                        planetsAndColor.getPaint());
                /*_canvas.drawText(retroNcombust, SCREEN_CONSTANTS.NKundliPlanetX1[indx1]+(SCREEN_CONSTANTS.TextColor.getTextSize() + (planetName.contains("M") ? 15 : 5) ),
                        SCREEN_CONSTANTS.NKundliPlanetY1[indx1]+10,
                        planetsAndColor.getPaint());*/
            } else {
                _canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.NKundliPlanetX1[indx1],
                        SCREEN_CONSTANTS.NKundliPlanetY1[indx1],
                        planetsAndColor.getPaint());
            }

            if (plntDegree > 0) {

                _canvas.drawText(CUtils.FormatDegreeInDD(plntDegree),
                        (SCREEN_CONSTANTS.NKundliPlanetX1[indx1]+30),
                        (SCREEN_CONSTANTS.NKundliPlanetY1[indx1] - (SCREEN_CONSTANTS.TextColor.getTextSize() - 10) ),
                        getTempPaintObj(planetsAndColor));
            }

            ++indx1;
        }
        iInxedColor++;
    }

    private void printPlanetInNorthKundliBhav2(PlanetsAndColor planetsAndColor, double plntDegree) {
        if (iInxedColor > 5)
            iInxedColor = 0;
        if (isDeviceTablet) {
            if (indx2 > 11)
                indx2 = 1;

            _canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.NTKundliPlanetX2[indx2],
                    SCREEN_CONSTANTS.NTKundliPlanetY2[indx2],
                    planetsAndColor.getPaint());
            indx2++;

            if (indx2 > 11)
                indx2 = 1;
            if (plntDegree > 0) {
                _canvas.drawText(CUtils.FormatDegreeInDD(plntDegree),
                        SCREEN_CONSTANTS.NTKundliPlanetX2[indx2],
                        SCREEN_CONSTANTS.NTKundliPlanetY2[indx2],
                        planetsAndColor.getPaint());
            }
            indx2++;
        } else {
            if (indx2 > 5)
                indx2 = 0;

            /*_canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.NKundliPlanetX2[indx2],
                    SCREEN_CONSTANTS.NKundliPlanetY2[indx2],
                    planetsAndColor.getPaint());*/

            // SAN changes for retroNcombust display on bottom
            if ( planetsAndColor.getPlanetName().contains("^") || planetsAndColor.getPlanetName().contains("*")
                    || planetsAndColor.getPlanetName().contains(unicodeBargotttam) ||
                    planetsAndColor.getPlanetName().contains(unicodeExalted) ||
                    planetsAndColor.getPlanetName().contains(unicodeDebiliated)  ) {

                String planetName = planetsAndColor.getPlanetName(); //.substring(0, 2);
                //String retroNcombust = planetsAndColor.getPlanetName().substring(2);

                _canvas.drawText(planetName, SCREEN_CONSTANTS.NKundliPlanetX2[indx2],
                        SCREEN_CONSTANTS.NKundliPlanetY2[indx2],
                        planetsAndColor.getPaint());

                /*_canvas.drawText(retroNcombust, SCREEN_CONSTANTS.NKundliPlanetX2[indx2]+(SCREEN_CONSTANTS.TextColor.getTextSize() + (planetName.contains("M") ? 15 : 5) ),
                        SCREEN_CONSTANTS.NKundliPlanetY2[indx2]+10,
                        planetsAndColor.getPaint());*/
            } else {
                _canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.NKundliPlanetX2[indx2],
                        SCREEN_CONSTANTS.NKundliPlanetY2[indx2],
                        planetsAndColor.getPaint());
            }

            if (plntDegree > 0) {

                _canvas.drawText(CUtils.FormatDegreeInDD(plntDegree),
                        (SCREEN_CONSTANTS.NKundliPlanetX2[indx2]+30),
                        (SCREEN_CONSTANTS.NKundliPlanetY2[indx2] - (SCREEN_CONSTANTS.TextColor.getTextSize() - 10) ),
                        getTempPaintObj(planetsAndColor));
            }

            ++indx2;
        }
        iInxedColor++;

    }

    private void printPlanetInNorthKundliBhav3(PlanetsAndColor planetsAndColor, double plntDegree) {
        if (iInxedColor > 5)
            iInxedColor = 0;
        if (isDeviceTablet) {
            if (indx3 > 11)
                indx3 = 1;

            _canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.NTKundliPlanetX3[indx3],
                    SCREEN_CONSTANTS.NTKundliPlanetY3[indx3],
                    planetsAndColor.getPaint());
            indx3++;

            if (indx3 > 11)
                indx3 = 1;
            if (plntDegree > 0) {
                _canvas.drawText(CUtils.FormatDegreeInDD(plntDegree),
                        SCREEN_CONSTANTS.NTKundliPlanetX3[indx3],
                        SCREEN_CONSTANTS.NTKundliPlanetY3[indx3],
                        planetsAndColor.getPaint());
            }
            indx3++;
        } else {

            if (indx3 > 5)
                indx3 = 0;

            /*_canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.NKundliPlanetX3[indx3],
                    SCREEN_CONSTANTS.NKundliPlanetY3[indx3],
                    planetsAndColor.getPaint());*/

            // SAN changes for retroNcombust display on bottom
            if ( planetsAndColor.getPlanetName().contains("^") || planetsAndColor.getPlanetName().contains("*")
                    || planetsAndColor.getPlanetName().contains(unicodeBargotttam) ||
                    planetsAndColor.getPlanetName().contains(unicodeExalted) ||
                    planetsAndColor.getPlanetName().contains(unicodeDebiliated)  ) {

                String planetName = planetsAndColor.getPlanetName(); //.substring(0, 2);
                //String retroNcombust = planetsAndColor.getPlanetName().substring(2);

                _canvas.drawText(planetName, SCREEN_CONSTANTS.NKundliPlanetX3[indx3],
                        SCREEN_CONSTANTS.NKundliPlanetY3[indx3],
                        planetsAndColor.getPaint());

               /* _canvas.drawText(retroNcombust, SCREEN_CONSTANTS.NKundliPlanetX3[indx3]+(SCREEN_CONSTANTS.TextColor.getTextSize() + (planetName.contains("M") ? 15 : 5) ),
                        SCREEN_CONSTANTS.NKundliPlanetY3[indx3]+10,
                        planetsAndColor.getPaint());*/
            } else {
                _canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.NKundliPlanetX3[indx3],
                        SCREEN_CONSTANTS.NKundliPlanetY3[indx3],
                        planetsAndColor.getPaint());
            }


            if (plntDegree > 0) {

                _canvas.drawText(CUtils.FormatDegreeInDD(plntDegree),
                        (SCREEN_CONSTANTS.NKundliPlanetX3[indx3]+30),
                        (SCREEN_CONSTANTS.NKundliPlanetY3[indx3] - (SCREEN_CONSTANTS.TextColor.getTextSize() - 10) ),
                        getTempPaintObj(planetsAndColor));
            }

            ++indx3;

        }
        iInxedColor++;

    }

    private void printPlanetInNorthKundliBhav4(PlanetsAndColor planetsAndColor, double plntDegree) {
        if (iInxedColor > 5)
            iInxedColor = 0;

        if (isDeviceTablet) {
            if (indx4 > 11)
                indx4 = 1;
            _canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.NTKundliPlanetX4[indx4],
                    SCREEN_CONSTANTS.NTKundliPlanetY4[indx4],
                    planetsAndColor.getPaint());
            indx4++;

            if (indx4 > 11)
                indx4 = 1;
            if (plntDegree > 0) {
                _canvas.drawText(CUtils.FormatDegreeInDD(plntDegree),
                        SCREEN_CONSTANTS.NTKundliPlanetX4[indx4],
                        SCREEN_CONSTANTS.NTKundliPlanetY4[indx4]-10,
                        planetsAndColor.getPaint());
            }
            indx4++;
        } else {
            if (indx4 > 5)
                indx4 = 0;

            /*_canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.NKundliPlanetX4[indx4],
                    SCREEN_CONSTANTS.NKundliPlanetY4[indx4],
                    planetsAndColor.getPaint());*/

            // SAN changes for retroNcombust display on bottom
            if ( planetsAndColor.getPlanetName().contains("^") || planetsAndColor.getPlanetName().contains("*")
                    || planetsAndColor.getPlanetName().contains(unicodeBargotttam) ||
                    planetsAndColor.getPlanetName().contains(unicodeExalted) ||
                    planetsAndColor.getPlanetName().contains(unicodeDebiliated)  ) {

                String planetName = planetsAndColor.getPlanetName(); //.substring(0, 2);
                //String retroNcombust = planetsAndColor.getPlanetName().substring(2);

                _canvas.drawText(planetName, SCREEN_CONSTANTS.NKundliPlanetX4[indx4],
                        SCREEN_CONSTANTS.NKundliPlanetY4[indx4],
                        planetsAndColor.getPaint());

                /*_canvas.drawText(retroNcombust, SCREEN_CONSTANTS.NKundliPlanetX4[indx4]+(SCREEN_CONSTANTS.TextColor.getTextSize() + (planetName.contains("M") ? 15 : 5) ),
                        SCREEN_CONSTANTS.NKundliPlanetY4[indx4]+10,
                        planetsAndColor.getPaint());*/
            } else {
                _canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.NKundliPlanetX4[indx4],
                        SCREEN_CONSTANTS.NKundliPlanetY4[indx4],
                        planetsAndColor.getPaint());
            }

            if (plntDegree > 0) {

                _canvas.drawText(CUtils.FormatDegreeInDD(plntDegree),
                        (SCREEN_CONSTANTS.NKundliPlanetX4[indx4]+30),
                        (SCREEN_CONSTANTS.NKundliPlanetY4[indx4] - (SCREEN_CONSTANTS.TextColor.getTextSize() - 10) ),
                        getTempPaintObj(planetsAndColor));
            }

            ++indx4;
        }
        iInxedColor++;
    }

    private void printPlanetInNorthKundliBhav5(PlanetsAndColor planetsAndColor, double plntDegree) {
        if (iInxedColor > 5)
            iInxedColor = 0;

        if (isDeviceTablet) {
            if (indx5 > 11)
                indx5 = 1;

            _canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.NTKundliPlanetX5[indx5],
                    SCREEN_CONSTANTS.NTKundliPlanetY5[indx5],
                    planetsAndColor.getPaint());
            indx5++;

            if (indx5 > 11)
                indx5 = 1;
            if (plntDegree > 0) {
                _canvas.drawText(CUtils.FormatDegreeInDD(plntDegree),
                        SCREEN_CONSTANTS.NTKundliPlanetX5[indx5],
                        SCREEN_CONSTANTS.NTKundliPlanetY5[indx5],
                        planetsAndColor.getPaint());
            }
            indx5++;
        } else {

            if (indx5 > 5)
                indx5 = 0;

            /*_canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.NKundliPlanetX5[indx5],
                    SCREEN_CONSTANTS.NKundliPlanetY5[indx5],
                    planetsAndColor.getPaint());*/

            // SAN changes for retroNcombust display on bottom
            if ( planetsAndColor.getPlanetName().contains("^") || planetsAndColor.getPlanetName().contains("*")
                    || planetsAndColor.getPlanetName().contains(unicodeBargotttam) ||
                    planetsAndColor.getPlanetName().contains(unicodeExalted) ||
                    planetsAndColor.getPlanetName().contains(unicodeDebiliated)  ) {

                String planetName = planetsAndColor.getPlanetName(); //.substring(0, 2);
                //String retroNcombust = planetsAndColor.getPlanetName().substring(2);

                _canvas.drawText(planetName, SCREEN_CONSTANTS.NKundliPlanetX5[indx5],
                        SCREEN_CONSTANTS.NKundliPlanetY5[indx5],
                        planetsAndColor.getPaint());

               /* _canvas.drawText(retroNcombust, SCREEN_CONSTANTS.NKundliPlanetX5[indx5]+(SCREEN_CONSTANTS.TextColor.getTextSize() + (planetName.contains("M") ? 15 : 5) ),
                        SCREEN_CONSTANTS.NKundliPlanetY5[indx5]+10,
                        planetsAndColor.getPaint());*/
            } else {
                _canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.NKundliPlanetX5[indx5],
                        SCREEN_CONSTANTS.NKundliPlanetY5[indx5],
                        planetsAndColor.getPaint());
            }

            if (plntDegree > 0) {

                _canvas.drawText(CUtils.FormatDegreeInDD(plntDegree),
                        (SCREEN_CONSTANTS.NKundliPlanetX5[indx5]+30),
                        (SCREEN_CONSTANTS.NKundliPlanetY5[indx5] - (SCREEN_CONSTANTS.TextColor.getTextSize() - 10) ),
                        getTempPaintObj(planetsAndColor));
            }

            ++indx5;


        }
        iInxedColor++;
    }

    private void printPlanetInNorthKundliBhav6(PlanetsAndColor planetsAndColor, double plntDegree) {
        if (iInxedColor > 5)
            iInxedColor = 0;

        if (isDeviceTablet) {
            if (indx6 > 11)
                indx6 = 1;

            _canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.NTKundliPlanetX6[indx6],
                    SCREEN_CONSTANTS.NTKundliPlanetY6[indx6],
                    planetsAndColor.getPaint());
            indx6++;

            if (indx6 > 11)
                indx6 = 1;
            if (plntDegree > 0) {
                _canvas.drawText(CUtils.FormatDegreeInDD(plntDegree),
                        SCREEN_CONSTANTS.NTKundliPlanetX6[indx6],
                        SCREEN_CONSTANTS.NTKundliPlanetY6[indx6],
                        planetsAndColor.getPaint());
            }
            indx6++;
        } else {

            if (indx6 > 5)
                indx6 = 0;

            /*_canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.NKundliPlanetX6[indx6],
                    SCREEN_CONSTANTS.NKundliPlanetY6[indx6],
                    planetsAndColor.getPaint());*/

            // SAN changes for retroNcombust display on bottom
            if ( planetsAndColor.getPlanetName().contains("^") || planetsAndColor.getPlanetName().contains("*")
                    || planetsAndColor.getPlanetName().contains(unicodeBargotttam) ||
                    planetsAndColor.getPlanetName().contains(unicodeExalted) ||
                    planetsAndColor.getPlanetName().contains(unicodeDebiliated)  ) {

                String planetName = planetsAndColor.getPlanetName(); //.substring(0, 2);
                //String retroNcombust = planetsAndColor.getPlanetName().substring(2);

                _canvas.drawText(planetName, SCREEN_CONSTANTS.NKundliPlanetX6[indx6],
                        SCREEN_CONSTANTS.NKundliPlanetY6[indx6],
                        planetsAndColor.getPaint());

                /*_canvas.drawText(retroNcombust, SCREEN_CONSTANTS.NKundliPlanetX6[indx6]+(SCREEN_CONSTANTS.TextColor.getTextSize() + (planetName.contains("M") ? 15 : 5) ),
                        SCREEN_CONSTANTS.NKundliPlanetY6[indx6]+10,
                        planetsAndColor.getPaint());*/
            } else {
                _canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.NKundliPlanetX6[indx6],
                        SCREEN_CONSTANTS.NKundliPlanetY6[indx6],
                        planetsAndColor.getPaint());
            }

            if (plntDegree > 0) {

                _canvas.drawText(CUtils.FormatDegreeInDD(plntDegree),
                        (SCREEN_CONSTANTS.NKundliPlanetX6[indx6]+30),
                        (SCREEN_CONSTANTS.NKundliPlanetY6[indx6] - (SCREEN_CONSTANTS.TextColor.getTextSize() - 10) ),
                        getTempPaintObj(planetsAndColor));
            }

            ++indx6;

        }
        iInxedColor++;

    }

    private void printPlanetInNorthKundliBhav7(PlanetsAndColor planetsAndColor, double plntDegree) {
        if (iInxedColor > 5)
            iInxedColor = 0;

        if (isDeviceTablet) {
            if (indx7 > 11)
                indx7 = 1;

            _canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.NTKundliPlanetX7[indx7],
                    SCREEN_CONSTANTS.NTKundliPlanetY7[indx7],
                    planetsAndColor.getPaint());
            indx7++;

            if (indx7 > 11)
                indx7 = 1;
            if (plntDegree > 0) {
                _canvas.drawText(CUtils.FormatDegreeInDD(plntDegree),
                        SCREEN_CONSTANTS.NTKundliPlanetX7[indx7],
                        SCREEN_CONSTANTS.NTKundliPlanetY7[indx7],
                        planetsAndColor.getPaint());
            }
            indx7++;
        } else {
            if (indx7 > 5)
                indx7 = 0;

            /*_canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.NKundliPlanetX7[indx7],
                    SCREEN_CONSTANTS.NKundliPlanetY7[indx7],
                    planetsAndColor.getPaint());*/

            // SAN changes for retroNcombust display on bottom
            if ( planetsAndColor.getPlanetName().contains("^") || planetsAndColor.getPlanetName().contains("*")
                    || planetsAndColor.getPlanetName().contains(unicodeBargotttam) ||
                    planetsAndColor.getPlanetName().contains(unicodeExalted) ||
                    planetsAndColor.getPlanetName().contains(unicodeDebiliated)  ) {

                String planetName = planetsAndColor.getPlanetName(); //.substring(0, 2);
                //String retroNcombust = planetsAndColor.getPlanetName().substring(2);

                _canvas.drawText(planetName, SCREEN_CONSTANTS.NKundliPlanetX7[indx7],
                        SCREEN_CONSTANTS.NKundliPlanetY7[indx7],
                        planetsAndColor.getPaint());

                /*_canvas.drawText(retroNcombust, SCREEN_CONSTANTS.NKundliPlanetX7[indx7]+(SCREEN_CONSTANTS.TextColor.getTextSize() + (planetName.contains("M") ? 15 : 5) ),
                        SCREEN_CONSTANTS.NKundliPlanetY7[indx7]+10,
                        planetsAndColor.getPaint());*/
            } else {
                _canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.NKundliPlanetX7[indx7],
                        SCREEN_CONSTANTS.NKundliPlanetY7[indx7],
                        planetsAndColor.getPaint());
            }

            if (plntDegree > 0) {

                _canvas.drawText(CUtils.FormatDegreeInDD(plntDegree),
                        (SCREEN_CONSTANTS.NKundliPlanetX7[indx7]+30),
                        (SCREEN_CONSTANTS.NKundliPlanetY7[indx7] - (SCREEN_CONSTANTS.TextColor.getTextSize() - 10) ),
                        getTempPaintObj(planetsAndColor));
            }

            ++indx7;
        }
        iInxedColor++;
    }

    private void printPlanetInNorthKundliBhav8(PlanetsAndColor planetsAndColor, double plntDegree) {

        if (iInxedColor > 5)
            iInxedColor = 0;

        if (isDeviceTablet) {
            if (indx8 > 11)
                indx8 = 1;

            _canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.NTKundliPlanetX8[indx8],
                    SCREEN_CONSTANTS.NTKundliPlanetY8[indx8],
                    planetsAndColor.getPaint());
            indx8++;

            if (indx8 > 11)
                indx8 = 1;
            if (plntDegree > 0) {
                _canvas.drawText(CUtils.FormatDegreeInDD(plntDegree),
                        SCREEN_CONSTANTS.NTKundliPlanetX8[indx8],
                        SCREEN_CONSTANTS.NTKundliPlanetY8[indx8],
                        planetsAndColor.getPaint());
            }
            indx8++;
        } else {
            if (indx8 > 5)
                indx8 = 0;

            /*_canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.NKundliPlanetX8[indx8],
                    SCREEN_CONSTANTS.NKundliPlanetY8[indx8],
                    planetsAndColor.getPaint());*/

            // SAN changes for retroNcombust display on bottom
            if ( planetsAndColor.getPlanetName().contains("^") || planetsAndColor.getPlanetName().contains("*")
                    || planetsAndColor.getPlanetName().contains(unicodeBargotttam) ||
                    planetsAndColor.getPlanetName().contains(unicodeExalted) ||
                    planetsAndColor.getPlanetName().contains(unicodeDebiliated)  ) {

                String planetName = planetsAndColor.getPlanetName(); //.substring(0, 2);
                //String retroNcombust = planetsAndColor.getPlanetName().substring(2);

                _canvas.drawText(planetName, SCREEN_CONSTANTS.NKundliPlanetX8[indx8],
                        SCREEN_CONSTANTS.NKundliPlanetY8[indx8],
                        planetsAndColor.getPaint());

                /*_canvas.drawText(retroNcombust, SCREEN_CONSTANTS.NKundliPlanetX8[indx8]+(SCREEN_CONSTANTS.TextColor.getTextSize() + (planetName.contains("M") ? 15 : 5) ),
                        SCREEN_CONSTANTS.NKundliPlanetY8[indx8]+10,
                        planetsAndColor.getPaint());*/
            } else {
                _canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.NKundliPlanetX8[indx8],
                        SCREEN_CONSTANTS.NKundliPlanetY8[indx8],
                        planetsAndColor.getPaint());
            }

            if (plntDegree > 0) {

                _canvas.drawText(CUtils.FormatDegreeInDD(plntDegree),
                        (SCREEN_CONSTANTS.NKundliPlanetX8[indx8]+30),
                        (SCREEN_CONSTANTS.NKundliPlanetY8[indx8] - (SCREEN_CONSTANTS.TextColor.getTextSize() - 10) ),
                        getTempPaintObj(planetsAndColor));
            }

            ++indx8;
        }
        iInxedColor++;

    }

    private void printPlanetInNorthKundliBhav9(PlanetsAndColor planetsAndColor, double plntDegree) {
        if (iInxedColor > 5)
            iInxedColor = 0;

        if (isDeviceTablet) {
            if (indx9 > 11)
                indx9 = 1;

            _canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.NTKundliPlanetX9[indx9],
                    SCREEN_CONSTANTS.NTKundliPlanetY9[indx9],
                    planetsAndColor.getPaint());
            indx9++;

            if (indx9 > 11)
                indx9 = 1;
            if (plntDegree > 0) {
                _canvas.drawText(CUtils.FormatDegreeInDD(plntDegree),
                        SCREEN_CONSTANTS.NTKundliPlanetX9[indx9],
                        SCREEN_CONSTANTS.NTKundliPlanetY9[indx9],
                        planetsAndColor.getPaint());
            }
            indx9++;
        } else {
            if (indx9 > 5)
                indx9 = 0;

            /*_canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.NKundliPlanetX9[indx9],
                    SCREEN_CONSTANTS.NKundliPlanetY9[indx9],
                    planetsAndColor.getPaint());*/

            // SAN changes for retroNcombust display on bottom
            if ( planetsAndColor.getPlanetName().contains("^") || planetsAndColor.getPlanetName().contains("*")
                    || planetsAndColor.getPlanetName().contains(unicodeBargotttam) ||
                    planetsAndColor.getPlanetName().contains(unicodeExalted) ||
                    planetsAndColor.getPlanetName().contains(unicodeDebiliated)  ) {

                String planetName = planetsAndColor.getPlanetName(); //.substring(0, 2);
                //String retroNcombust = planetsAndColor.getPlanetName().substring(2);

                _canvas.drawText(planetName, SCREEN_CONSTANTS.NKundliPlanetX9[indx9],
                        SCREEN_CONSTANTS.NKundliPlanetY9[indx9],
                        planetsAndColor.getPaint());

                /*_canvas.drawText(retroNcombust, SCREEN_CONSTANTS.NKundliPlanetX9[indx9]+(SCREEN_CONSTANTS.TextColor.getTextSize() + (planetName.contains("M") ? 15 : 5) ),
                        SCREEN_CONSTANTS.NKundliPlanetY9[indx9]+10,
                        planetsAndColor.getPaint());*/
            } else {
                _canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.NKundliPlanetX9[indx9],
                        SCREEN_CONSTANTS.NKundliPlanetY9[indx9],
                        planetsAndColor.getPaint());
            }

            if (plntDegree > 0) {

                _canvas.drawText(CUtils.FormatDegreeInDD(plntDegree),
                        (SCREEN_CONSTANTS.NKundliPlanetX9[indx9]+30),
                        (SCREEN_CONSTANTS.NKundliPlanetY9[indx9] - (SCREEN_CONSTANTS.TextColor.getTextSize() - 10) ),
                        getTempPaintObj(planetsAndColor));
            }

            ++indx9;
        }
        iInxedColor++;
    }

    private void printPlanetInNorthKundliBhav10(PlanetsAndColor planetsAndColor, double plntDegree) {
        if (iInxedColor > 5)
            iInxedColor = 0;
        if (isDeviceTablet) {
            if (indx10 > 11)
                indx10 = 1;

            _canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.NTKundliPlanetX10[indx10],
                    SCREEN_CONSTANTS.NTKundliPlanetY10[indx10],
                    planetsAndColor.getPaint());
            indx10++;

            if (indx10 > 11)
                indx10 = 1;
            if (plntDegree > 0) {
                _canvas.drawText(CUtils.FormatDegreeInDD(plntDegree),
                        SCREEN_CONSTANTS.NTKundliPlanetX10[indx10],
                        SCREEN_CONSTANTS.NTKundliPlanetY10[indx10],
                        planetsAndColor.getPaint());
            }
            indx10++;
        } else {
            if (indx10 > 5)
                indx10 = 0;

            /*_canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.NKundliPlanetX10[indx10],
                    SCREEN_CONSTANTS.NKundliPlanetY10[indx10],
                    planetsAndColor.getPaint());*/

            // SAN changes for retroNcombust display on bottom
            if ( planetsAndColor.getPlanetName().contains("^") || planetsAndColor.getPlanetName().contains("*")
                    || planetsAndColor.getPlanetName().contains(unicodeBargotttam) ||
                    planetsAndColor.getPlanetName().contains(unicodeExalted) ||
                    planetsAndColor.getPlanetName().contains(unicodeDebiliated)  ) {

                String planetName = planetsAndColor.getPlanetName(); //.substring(0, 2);
                //String retroNcombust = planetsAndColor.getPlanetName().substring(2);

                _canvas.drawText(planetName, SCREEN_CONSTANTS.NKundliPlanetX10[indx10],
                        SCREEN_CONSTANTS.NKundliPlanetY10[indx10],
                        planetsAndColor.getPaint());

                /*_canvas.drawText(retroNcombust, SCREEN_CONSTANTS.NKundliPlanetX10[indx10]+(SCREEN_CONSTANTS.TextColor.getTextSize() + (planetName.contains("M") ? 15 : 5) ),
                        SCREEN_CONSTANTS.NKundliPlanetY10[indx10]+10,
                        planetsAndColor.getPaint());*/
            } else {
                _canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.NKundliPlanetX10[indx10],
                        SCREEN_CONSTANTS.NKundliPlanetY10[indx10],
                        planetsAndColor.getPaint());
            }

            if (plntDegree > 0) {

                _canvas.drawText(CUtils.FormatDegreeInDD(plntDegree),
                        (SCREEN_CONSTANTS.NKundliPlanetX10[indx10]+30),
                        (SCREEN_CONSTANTS.NKundliPlanetY10[indx10] - (SCREEN_CONSTANTS.TextColor.getTextSize() - 10) ),
                        getTempPaintObj(planetsAndColor));
            }

            ++indx10;
            iInxedColor++;
        }
    }

    private void printPlanetInNorthKundliBhav11(PlanetsAndColor planetsAndColor, double plntDegree) {
        if (iInxedColor > 5)
            iInxedColor = 0;

        if (isDeviceTablet) {
            if (indx11 > 11)
                indx11 = 1;

            _canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.NTKundliPlanetX11[indx11],
                    SCREEN_CONSTANTS.NTKundliPlanetY11[indx11],
                    planetsAndColor.getPaint());
            indx11++;

            if (indx11 > 11)
                indx11 = 1;

            if (plntDegree > 0) {
                _canvas.drawText(CUtils.FormatDegreeInDD(plntDegree),
                        SCREEN_CONSTANTS.NTKundliPlanetX11[indx11],
                        SCREEN_CONSTANTS.NTKundliPlanetY11[indx11],
                        planetsAndColor.getPaint());
            }
            indx11++;
        } else {
            if (indx11 > 5)
                indx11 = 0;

            /*_canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.NKundliPlanetX11[indx11],
                    SCREEN_CONSTANTS.NKundliPlanetY11[indx11],
                    planetsAndColor.getPaint());*/

            // SAN changes for retroNcombust display on bottom
            if ( planetsAndColor.getPlanetName().contains("^") || planetsAndColor.getPlanetName().contains("*")
                    || planetsAndColor.getPlanetName().contains(unicodeBargotttam) ||
                    planetsAndColor.getPlanetName().contains(unicodeExalted) ||
                    planetsAndColor.getPlanetName().contains(unicodeDebiliated)  ) {

                String planetName = planetsAndColor.getPlanetName(); //.substring(0, 2);
                //String retroNcombust = planetsAndColor.getPlanetName().substring(2);

                _canvas.drawText(planetName, SCREEN_CONSTANTS.NKundliPlanetX11[indx11],
                        SCREEN_CONSTANTS.NKundliPlanetY11[indx11],
                        planetsAndColor.getPaint());

                /*_canvas.drawText(retroNcombust, SCREEN_CONSTANTS.NKundliPlanetX11[indx11]+(SCREEN_CONSTANTS.TextColor.getTextSize() + (planetName.contains("M") ? 15 : 5) ),
                        SCREEN_CONSTANTS.NKundliPlanetY11[indx11]+10,
                        planetsAndColor.getPaint());*/
            } else {
                _canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.NKundliPlanetX11[indx11],
                        SCREEN_CONSTANTS.NKundliPlanetY11[indx11],
                        planetsAndColor.getPaint());
            }

            if (plntDegree > 0) {

                _canvas.drawText(CUtils.FormatDegreeInDD(plntDegree),
                        (SCREEN_CONSTANTS.NKundliPlanetX11[indx11]+30),
                        (SCREEN_CONSTANTS.NKundliPlanetY11[indx11] - (SCREEN_CONSTANTS.TextColor.getTextSize() - 10) ),
                        getTempPaintObj(planetsAndColor));
            }

            ++indx11;
        }
        iInxedColor++;
    }

    private void printPlanetInNorthKundliBhav12(PlanetsAndColor planetsAndColor, double plntDegree) {
        if (iInxedColor > 5)
            iInxedColor = 0;

        if (isDeviceTablet) {
            if (indx12 > 11)
                indx12 = 1;

            _canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.NTKundliPlanetX12[indx12],
                    SCREEN_CONSTANTS.NTKundliPlanetY12[indx12],
                    planetsAndColor.getPaint());
            indx12++;

            if (indx12 > 11)
                indx12 = 1;
            if (plntDegree > 0) {
                _canvas.drawText(CUtils.FormatDegreeInDD(plntDegree),
                        SCREEN_CONSTANTS.NTKundliPlanetX12[indx12],
                        SCREEN_CONSTANTS.NTKundliPlanetY12[indx12],
                        planetsAndColor.getPaint());
            }
            indx12++;
        } else {
            if (indx12 > 5)
                indx12 = 0;

            /*_canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.NKundliPlanetX12[indx12],
                    SCREEN_CONSTANTS.NKundliPlanetY12[indx12],
                    planetsAndColor.getPaint());*/

            // SAN changes for retroNcombust display on bottom
            if ( planetsAndColor.getPlanetName().contains("^") || planetsAndColor.getPlanetName().contains("*")
                    || planetsAndColor.getPlanetName().contains(unicodeBargotttam) ||
                    planetsAndColor.getPlanetName().contains(unicodeExalted) ||
                    planetsAndColor.getPlanetName().contains(unicodeDebiliated)  ) {

                String planetName = planetsAndColor.getPlanetName(); //.substring(0, 2);
                //String retroNcombust = planetsAndColor.getPlanetName().substring(2);

                _canvas.drawText(planetName, SCREEN_CONSTANTS.NKundliPlanetX12[indx12],
                        SCREEN_CONSTANTS.NKundliPlanetY12[indx12],
                        planetsAndColor.getPaint());

                /*_canvas.drawText(retroNcombust, SCREEN_CONSTANTS.NKundliPlanetX12[indx12]+(SCREEN_CONSTANTS.TextColor.getTextSize() + (planetName.contains("M") ? 15 : 5) ),
                        SCREEN_CONSTANTS.NKundliPlanetY12[indx12]+10,
                        planetsAndColor.getPaint());*/
            } else {
                _canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.NKundliPlanetX12[indx12],
                        SCREEN_CONSTANTS.NKundliPlanetY12[indx12],
                        planetsAndColor.getPaint());
            }

            if (plntDegree > 0) {

                _canvas.drawText(CUtils.FormatDegreeInDD(plntDegree),
                        (SCREEN_CONSTANTS.NKundliPlanetX12[indx12]+30),
                        (SCREEN_CONSTANTS.NKundliPlanetY12[indx12] - (SCREEN_CONSTANTS.TextColor.getTextSize() - 10) ),
                        getTempPaintObj(planetsAndColor));
            }

            ++indx12;
        }
        iInxedColor++;
    }

    // END


    //TEJINDER SING show East Kundli

    private void showEastKundli() {
        ArrayList<PlanetsAndColor> planetName = getPlantNameWithDirectAndRedirect();
        drawEastKundli();
        double tempPlntDegree = -1;
        if(lagna_position == -1){
            lagna_position = _planetsInRashi[12];
        }
        Log.d("DarwEastKundali","Lagna position = " + lagna_position);
        if (planetName != null && planetName.size() > 0) {
            for (int i = 0; i < _planetsInRashi.length; i++) {
                if (_planetsDegree != null)
                    tempPlntDegree = _planetsDegree[i];

                if (i == 12 && isDrawKarkasSwars)
                    _planetsInRashi[i] = laganKarksaOne;

                if ((SCREEN_CONSTANTS.NotShowUranusNeptunePlutoInChart)
                        && (i == 9 || i == 10 || i == 11)) {
                    continue;
                } else {
                    switch (_planetsInRashi[i]) {
                        case 0:
                            printPlanetsInEastKundliInRashi12(planetName.get(i),
                                    tempPlntDegree);
                            break;
                        case 1:
                            printPlanetsInEastKundliInRashi1(planetName.get(i),
                                    tempPlntDegree);
                            break;
                        case 2:
                            printPlanetsInEastKundliInRashi2(planetName.get(i),
                                    tempPlntDegree);
                            break;
                        case 3:
                            printPlanetsInEastKundliInRashi3(planetName.get(i),
                                    tempPlntDegree);
                            break;
                        case 4:
                            printPlanetsInEastKundliInRashi4(planetName.get(i),
                                    tempPlntDegree);
                            break;
                        case 5:
                            printPlanetsInEastKundliInRashi5(planetName.get(i),
                                    tempPlntDegree);
                            break;
                        case 6:
                            printPlanetsInEastKundliInRashi6(planetName.get(i),
                                    tempPlntDegree);
                            break;
                        case 7:
                            printPlanetsInEastKundliInRashi7(planetName.get(i),
                                    tempPlntDegree);
                            break;
                        case 8:
                            printPlanetsInEastKundliInRashi8(planetName.get(i),
                                    tempPlntDegree);
                            break;
                        case 9:
                            printPlanetsInEastKundliInRashi9(planetName.get(i),
                                    tempPlntDegree);
                            break;
                        case 10:
                            printPlanetsInEastKundliInRashi10(planetName.get(i),
                                    tempPlntDegree);
                            break;
                        case 11:
                            printPlanetsInEastKundliInRashi11(planetName.get(i),
                                    tempPlntDegree);
                            break;
                        case 12:
                            printPlanetsInEastKundliInRashi12(planetName.get(i),
                                    tempPlntDegree);
                            break;

                    }
                }
            }
        }
    }


    /*
     * South kundli functions
     */
    //commented by tejinder singh for testing
    private void showSouthKundli() {
        ArrayList<PlanetsAndColor> planetName = getPlantNameWithDirectAndRedirect();
        drawSouthKundli();
        if(lagna_position == -1) {
            lagna_position  = _planetsInRashi[12];
        }
        Log.d("DarwSouthKundali","Lagna Position = " + lagna_position);
        double tempPlntDegree = -1;
        if (planetName != null && planetName.size() > 0) {
            for (int i = 0; i < _planetsInRashi.length; i++) {
                if (_planetsDegree != null)
                    tempPlntDegree = _planetsDegree[i];

                if (i == 12 && isDrawKarkasSwars)
                    _planetsInRashi[i] = laganKarksaOne;

                if ((SCREEN_CONSTANTS.NotShowUranusNeptunePlutoInChart)
                        && (i == 9 || i == 10 || i == 11)) {
                    continue;
                } else {
                    switch (_planetsInRashi[i]) {
                        /*
                         * @author : Amit.R
                         * @date : 16 feb 2016
                         * @description : some time server gives 0 value instead of 12. so we have to call printPlanetsInSouthKundliInRashi12 method in case of getting 0 value.
                         */
                        case 0:
                            printPlanetsInSouthKundliInRashi12(planetName.get(i),
                                    tempPlntDegree);
                            break;
                        case 1:
                            printPlanetsInSouthKundliInRashi1(planetName.get(i),
                                    tempPlntDegree);
                            break;
                        case 2:
                            printPlanetsInSouthKundliInRashi2(planetName.get(i),
                                    tempPlntDegree);
                            break;
                        case 3:
                            printPlanetsInSouthKundliInRashi3(planetName.get(i),
                                    tempPlntDegree);
                            break;
                        case 4:
                            printPlanetsInSouthKundliInRashi4(planetName.get(i),
                                    tempPlntDegree);
                            break;
                        case 5:
                            printPlanetsInSouthKundliInRashi5(planetName.get(i),
                                    tempPlntDegree);
                            break;
                        case 6:
                            printPlanetsInSouthKundliInRashi6(planetName.get(i),
                                    tempPlntDegree);
                            break;
                        case 7:
                            printPlanetsInSouthKundliInRashi7(planetName.get(i),
                                    tempPlntDegree);
                            break;
                        case 8:
                            printPlanetsInSouthKundliInRashi8(planetName.get(i),
                                    tempPlntDegree);
                            break;
                        case 9:
                            printPlanetsInSouthKundliInRashi9(planetName.get(i),
                                    tempPlntDegree);
                            break;
                        case 10:
                            printPlanetsInSouthKundliInRashi10(planetName.get(i),
                                    tempPlntDegree);
                            break;
                        case 11:
                            printPlanetsInSouthKundliInRashi11(planetName.get(i),
                                    tempPlntDegree);
                            break;
                        case 12:
                            printPlanetsInSouthKundliInRashi12(planetName.get(i),
                                    tempPlntDegree);
                            break;

                    }
                }
            }
        }
    }

    //@Tejinder Singh for testing max 6 plant on per rashi
    //0-SU|| 1-MO|| 2-MA || 3-ME || 4-JU ||5-VE

    /* private void showEastKundli() {
      drawEastKundli();
      double tempPlntDegree = -1;

      for (int i = 0; i < _planetsInRashi.length; i++) {
         if (_planetsDegree != null)
            tempPlntDegree = _planetsDegree[i];

         if ((SCREEN_CONSTANTS.NotShowUranusNeptunePlutoInChart)
               && (i == 9 || i == 10 || i == 11)) {
            continue;
         }
         else if(i==12&&isDrawKarkasSwars)
            {

            }
            else {
                printPlanetsInEastKundliInRashi10(_planetsName[i],
                     tempPlntDegree);
                printPlanetsInEastKundliInRashi10(_planetsName[i],
                     tempPlntDegree);
                printPlanetsInEastKundliInRashi8(_planetsName[i],
                     tempPlntDegree);
                printPlanetsInEastKundliInRashi6(_planetsName[i],
                     tempPlntDegree);
               printPlanetsInEastKundliInRashi4(_planetsName[i],
                     tempPlntDegree);
                printPlanetsInEastKundliInRashi2(_planetsName[i],
                     tempPlntDegree);
               printPlanetsInEastKundliInRashi1(_planetsName[i],
                     tempPlntDegree);
               printPlanetsInEastKundliInRashi11(_planetsName[i],
                     tempPlntDegree);
               printPlanetsInEastKundliInRashi9(_planetsName[i],
                     tempPlntDegree);
               printPlanetsInEastKundliInRashi7(_planetsName[i],
                     tempPlntDegree);
               printPlanetsInEastKundliInRashi5(_planetsName[i],
                     tempPlntDegree);
               printPlanetsInEastKundliInRashi3(_planetsName[i],
                     tempPlntDegree);

         }
      }

   }*/


    private void drawSouthKundli() {

       /* _canvas.drawRect(SCREEN_CONSTANTS.HKUNDLISpace,
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
                SCREEN_CONSTANTS.KundliLineColor); // (9)*/

        for(int i=0;i<12;i++){
            Path path = drawSouthKundliCell(i);
            paths[i] = path;
            cellList[i] = createRegionFromPath(path);
            _canvas.drawPath(path,SCREEN_CONSTANTS.KundliLineColor);
        }

    }

    //Tejinder Show kundli East Kundli

    private void drawEastKundli() {

        /*_canvas.drawRect(SCREEN_CONSTANTS.HKUNDLISpace,
                SCREEN_CONSTANTS.VKUNDLISpace, SCREEN_CONSTANTS.HKUNDLISpace
                        + w, SCREEN_CONSTANTS.VKUNDLISpace + h,
                SCREEN_CONSTANTS.KundliLineColor); // (1)
        // first line horizontal
        _canvas.drawLine(SCREEN_CONSTANTS.HKUNDLISpace,
                ((SCREEN_CONSTANTS.VKUNDLISpace + h) / 4) + 61.0f,
                SCREEN_CONSTANTS.HKUNDLISpace + w,
                ((SCREEN_CONSTANTS.VKUNDLISpace + h) / 4) + 61.0f,
                SCREEN_CONSTANTS.KundliLineColor);// (2)

        //show diagnol line top Right
        _canvas.drawLine(SCREEN_CONSTANTS.HKUNDLISpace + w,
                SCREEN_CONSTANTS.HKUNDLISpace,
                (3 * (SCREEN_CONSTANTS.HKUNDLISpace + w) / 4) - 61.0f,
                ((SCREEN_CONSTANTS.VKUNDLISpace + h) / 4) + 61.0f,
                SCREEN_CONSTANTS.KundliLineColor);

        //show diagnol line top left
        _canvas.drawLine(SCREEN_CONSTANTS.HKUNDLISpace,
                SCREEN_CONSTANTS.HKUNDLISpace,
                ((SCREEN_CONSTANTS.HKUNDLISpace + w) / 4) + 61.0f,
                ((SCREEN_CONSTANTS.VKUNDLISpace + h) / 4) + 61.0f,
                SCREEN_CONSTANTS.KundliLineColor);

        //show diagnol line bottom left
        _canvas.drawLine(SCREEN_CONSTANTS.HKUNDLISpace,
                SCREEN_CONSTANTS.HKUNDLISpace + h,
                ((SCREEN_CONSTANTS.HKUNDLISpace + w) / 4) + 61.0f,
                (3 * (SCREEN_CONSTANTS.VKUNDLISpace + h) / 4) - 61.0f,
                SCREEN_CONSTANTS.KundliLineColor);

        //show diagnol line bottom Right
        _canvas.drawLine(SCREEN_CONSTANTS.HKUNDLISpace + w,
                SCREEN_CONSTANTS.HKUNDLISpace + h,
                (3 * (SCREEN_CONSTANTS.HKUNDLISpace + w) / 4) - 61.0f,
                (3 * (SCREEN_CONSTANTS.VKUNDLISpace + h) / 4) - 61.0f,
                SCREEN_CONSTANTS.KundliLineColor);


        //above than bottom line horizontal
        _canvas.drawLine(SCREEN_CONSTANTS.HKUNDLISpace,
                (3 * (SCREEN_CONSTANTS.VKUNDLISpace + h) / 4) - 61.0f,
                (SCREEN_CONSTANTS.HKUNDLISpace + w),
                (3 * (SCREEN_CONSTANTS.VKUNDLISpace + h) / 4) - 61.0f,
                SCREEN_CONSTANTS.KundliLineColor); // (5)
        //first vertical line
        _canvas.drawLine(((SCREEN_CONSTANTS.HKUNDLISpace + w) / 4) + 61.0f,
                SCREEN_CONSTANTS.VKUNDLISpace,
                ((SCREEN_CONSTANTS.HKUNDLISpace + w) / 4) + 61.0f,
                SCREEN_CONSTANTS.VKUNDLISpace + h,
                SCREEN_CONSTANTS.KundliLineColor); // (6)
        //second vertical line
        _canvas.drawLine((3 * (SCREEN_CONSTANTS.HKUNDLISpace + w) / 4) - 61.0f,
                SCREEN_CONSTANTS.VKUNDLISpace,
                (3 * (SCREEN_CONSTANTS.HKUNDLISpace + w) / 4) - 61.0f,
                SCREEN_CONSTANTS.VKUNDLISpace + h,
                SCREEN_CONSTANTS.KundliLineColor);*/ // (7)

        for(int i=0;i<12;i++){
            Path path = drawEastKundliCell(i);
            paths[i] = path;
            cellList[i] = createRegionFromPath(path);
            _canvas.drawPath(path,SCREEN_CONSTANTS.KundliLineColor);
        }

    }


    private void printPlanetsInSouthKundliInRashi1(PlanetsAndColor planetsAndColor,
                                                   double plntDegree) {
        if (iInxedColor > 5)
            iInxedColor = 0;
        if (indx1 < SCREEN_CONSTANTS.STKundliPlanetX1.length) {
            if (plntDegree > 0 && isDeviceTablet) {
                _canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.STKundliPlanetX1[indx1],
                        SCREEN_CONSTANTS.STKundliPlanetY1[indx1],
                        planetsAndColor.getPaint());
                indx1++;
                _canvas.drawText(CUtils.FormatDegreeInDDMM(plntDegree),
                        SCREEN_CONSTANTS.STKundliPlanetX1[indx1],
                        SCREEN_CONSTANTS.STKundliPlanetY1[indx1],
                        planetsAndColor.getPaint());
                indx1++;
            } else {
                if (indx1 > 5)
                    indx1 = 0;

                _canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.SKundliPlanetX1[indx1],
                        SCREEN_CONSTANTS.SKundliPlanetY1[indx1],
                        planetsAndColor.getPaint());

                if (plntDegree > 0) {

                    _canvas.drawText(CUtils.FormatDegreeInDD(plntDegree),
                            (SCREEN_CONSTANTS.SKundliPlanetX1[indx1]+30),
                            (SCREEN_CONSTANTS.SKundliPlanetY1[indx1] - (SCREEN_CONSTANTS.TextColor.getTextSize() - 10) ),
                            getTempPaintObj(planetsAndColor));
                }

                ++indx1;
            }
        }
        iInxedColor++;
    }

    private void printPlanetsInSouthKundliInRashi2(PlanetsAndColor planetsAndColor,
                                                   double plntDegree) {
        if (iInxedColor > 5)
            iInxedColor = 0;
        if (indx2 < SCREEN_CONSTANTS.STKundliPlanetX2.length) {
            if (plntDegree > 0 && isDeviceTablet) {
                _canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.STKundliPlanetX2[indx2],
                        SCREEN_CONSTANTS.STKundliPlanetY2[indx2],
                        planetsAndColor.getPaint());
                indx2++;
                _canvas.drawText(CUtils.FormatDegreeInDDMM(plntDegree),
                        SCREEN_CONSTANTS.STKundliPlanetX2[indx2],
                        SCREEN_CONSTANTS.STKundliPlanetY2[indx2],
                        planetsAndColor.getPaint());
                indx2++;
            } else {
                if (indx2 > 5)
                    indx2 = 0;

                _canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.SKundliPlanetX2[indx2],
                        SCREEN_CONSTANTS.SKundliPlanetY2[indx2],
                        planetsAndColor.getPaint());

                if (plntDegree > 0) {

                    _canvas.drawText(CUtils.FormatDegreeInDD(plntDegree),
                            (SCREEN_CONSTANTS.SKundliPlanetX2[indx2]+30),
                            (SCREEN_CONSTANTS.SKundliPlanetY2[indx2] - (SCREEN_CONSTANTS.TextColor.getTextSize() - 10) ),
                            getTempPaintObj(planetsAndColor));
                }

                ++indx2;
            }
        }
        iInxedColor++;
    }

    private void printPlanetsInSouthKundliInRashi3(PlanetsAndColor planetsAndColor,
                                                   double plntDegree) {
        if (iInxedColor > 5)
            iInxedColor = 0;
        if (indx3 < SCREEN_CONSTANTS.STKundliPlanetX3.length) {
            if (plntDegree > 0 && isDeviceTablet) {
                _canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.STKundliPlanetX3[indx3],
                        SCREEN_CONSTANTS.STKundliPlanetY3[indx3],
                        planetsAndColor.getPaint());
                indx3++;
                _canvas.drawText(CUtils.FormatDegreeInDDMM(plntDegree),
                        SCREEN_CONSTANTS.STKundliPlanetX3[indx3],
                        SCREEN_CONSTANTS.STKundliPlanetY3[indx3],
                        planetsAndColor.getPaint());
                indx3++;
            } else {
                if (indx3 > 5)
                    indx3 = 0;

                _canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.SKundliPlanetX3[indx3],
                        SCREEN_CONSTANTS.SKundliPlanetY3[indx3],
                        planetsAndColor.getPaint());

                if (plntDegree > 0) {

                    _canvas.drawText(CUtils.FormatDegreeInDD(plntDegree),
                            (SCREEN_CONSTANTS.SKundliPlanetX3[indx3]+30),
                            (SCREEN_CONSTANTS.SKundliPlanetY3[indx3] - (SCREEN_CONSTANTS.TextColor.getTextSize() - 10) ),
                            getTempPaintObj(planetsAndColor));
                }

                ++indx3;
            }
        }
        iInxedColor++;
    }

    private void printPlanetsInSouthKundliInRashi4(PlanetsAndColor planetsAndColor,
                                                   double plntDegree) {
        if (iInxedColor > 5)
            iInxedColor = 0;
        if (indx4 < SCREEN_CONSTANTS.STKundliPlanetX4.length) {
            if (plntDegree > 0 && isDeviceTablet) {
                _canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.STKundliPlanetX4[indx4],
                        SCREEN_CONSTANTS.STKundliPlanetY4[indx4],
                        planetsAndColor.getPaint());
                indx4++;
                _canvas.drawText(CUtils.FormatDegreeInDDMM(plntDegree),
                        SCREEN_CONSTANTS.STKundliPlanetX4[indx4],
                        SCREEN_CONSTANTS.STKundliPlanetY4[indx4],
                        planetsAndColor.getPaint());
                indx4++;
            } else {
                if (indx4 > 5)
                    indx4 = 0;

                _canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.SKundliPlanetX4[indx4],
                        SCREEN_CONSTANTS.SKundliPlanetY4[indx4],
                        planetsAndColor.getPaint());

                if (plntDegree > 0) {

                    _canvas.drawText(CUtils.FormatDegreeInDD(plntDegree),
                            (SCREEN_CONSTANTS.SKundliPlanetX4[indx4]+30),
                            (SCREEN_CONSTANTS.SKundliPlanetY4[indx4] - (SCREEN_CONSTANTS.TextColor.getTextSize() - 10) ),
                            getTempPaintObj(planetsAndColor));
                }

                ++indx4;
            }
        }
        iInxedColor++;
    }

    private void printPlanetsInSouthKundliInRashi5(PlanetsAndColor planetsAndColor,
                                                   double plntDegree) {
        if (iInxedColor > 5)
            iInxedColor = 0;
        if (indx5 < SCREEN_CONSTANTS.STKundliPlanetX5.length) {
            if (plntDegree > 0 && isDeviceTablet) {
                _canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.STKundliPlanetX5[indx5],
                        SCREEN_CONSTANTS.STKundliPlanetY5[indx5],
                        planetsAndColor.getPaint());
                indx5++;
                _canvas.drawText(CUtils.FormatDegreeInDDMM(plntDegree),
                        SCREEN_CONSTANTS.STKundliPlanetX5[indx5],
                        SCREEN_CONSTANTS.STKundliPlanetY5[indx5],
                        planetsAndColor.getPaint());
                indx5++;
            } else {
                if (indx5 > 5)
                    indx5 = 0;

                _canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.SKundliPlanetX5[indx5],
                        SCREEN_CONSTANTS.SKundliPlanetY5[indx5],
                        planetsAndColor.getPaint());

                if (plntDegree > 0) {

                    _canvas.drawText(CUtils.FormatDegreeInDD(plntDegree),
                            (SCREEN_CONSTANTS.SKundliPlanetX5[indx5]+30),
                            (SCREEN_CONSTANTS.SKundliPlanetY5[indx5] - (SCREEN_CONSTANTS.TextColor.getTextSize() - 10) ),
                            getTempPaintObj(planetsAndColor));
                }

                ++indx5;
            }
        }
        iInxedColor++;
    }

    private void printPlanetsInSouthKundliInRashi6(PlanetsAndColor planetsAndColor,
                                                   double plntDegree) {
        if (iInxedColor > 5)
            iInxedColor = 0;
        if (indx6 < SCREEN_CONSTANTS.STKundliPlanetX6.length) {
            if (plntDegree > 0 && isDeviceTablet) {
                _canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.STKundliPlanetX6[indx6],
                        SCREEN_CONSTANTS.STKundliPlanetY6[indx6],
                        planetsAndColor.getPaint());
                indx6++;
                _canvas.drawText(CUtils.FormatDegreeInDDMM(plntDegree),
                        SCREEN_CONSTANTS.STKundliPlanetX6[indx6],
                        SCREEN_CONSTANTS.STKundliPlanetY6[indx6],
                        planetsAndColor.getPaint());
                indx6++;
            } else {
                if (indx6 > 5)
                    indx6 = 0;

                _canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.SKundliPlanetX6[indx6],
                        SCREEN_CONSTANTS.SKundliPlanetY6[indx6],
                        planetsAndColor.getPaint());

                if (plntDegree > 0) {

                    _canvas.drawText(CUtils.FormatDegreeInDD(plntDegree),
                            (SCREEN_CONSTANTS.SKundliPlanetX6[indx6]+30),
                            (SCREEN_CONSTANTS.SKundliPlanetY6[indx6] - (SCREEN_CONSTANTS.TextColor.getTextSize() - 10) ),
                            getTempPaintObj(planetsAndColor));
                }

                ++indx6;
            }
        }
        iInxedColor++;
    }

    private void printPlanetsInSouthKundliInRashi7(PlanetsAndColor planetsAndColor,
                                                   double plntDegree) {
        if (iInxedColor > 5)
            iInxedColor = 0;
        if (indx7 < SCREEN_CONSTANTS.STKundliPlanetX7.length) {
            if (plntDegree > 0 && isDeviceTablet) {
                _canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.STKundliPlanetX7[indx7],
                        SCREEN_CONSTANTS.STKundliPlanetY7[indx7],
                        planetsAndColor.getPaint());
                indx7++;
                _canvas.drawText(CUtils.FormatDegreeInDDMM(plntDegree),
                        SCREEN_CONSTANTS.STKundliPlanetX7[indx7],
                        SCREEN_CONSTANTS.STKundliPlanetY7[indx7],
                        planetsAndColor.getPaint());
                indx7++;
            } else {
                if (indx7 > 5)
                    indx7 = 0;
                _canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.SKundliPlanetX7[indx7],
                        SCREEN_CONSTANTS.SKundliPlanetY7[indx7],
                        planetsAndColor.getPaint());

                if (plntDegree > 0) {

                    _canvas.drawText(CUtils.FormatDegreeInDD(plntDegree),
                            (SCREEN_CONSTANTS.SKundliPlanetX7[indx7]+30),
                            (SCREEN_CONSTANTS.SKundliPlanetY7[indx7] - (SCREEN_CONSTANTS.TextColor.getTextSize() - 10) ),
                            getTempPaintObj(planetsAndColor));
                }

                ++indx7;
            }
        }
        iInxedColor++;
    }

    private void printPlanetsInSouthKundliInRashi8(PlanetsAndColor planetsAndColor,
                                                   double plntDegree) {
        if (iInxedColor > 5)
            iInxedColor = 0;
        if (plntDegree > 0 && isDeviceTablet) {
            _canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.STKundliPlanetX8[indx8],
                    SCREEN_CONSTANTS.STKundliPlanetY8[indx8],
                    planetsAndColor.getPaint());
            indx8++;
            _canvas.drawText(CUtils.FormatDegreeInDDMM(plntDegree),
                    SCREEN_CONSTANTS.STKundliPlanetX8[indx8],
                    SCREEN_CONSTANTS.STKundliPlanetY8[indx8],
                    planetsAndColor.getPaint());
            indx8++;
        } else {
            if (indx8 > 5)
                indx8 = 0;

            _canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.SKundliPlanetX8[indx8],
                    SCREEN_CONSTANTS.SKundliPlanetY8[indx8],
                    planetsAndColor.getPaint());

            if (plntDegree > 0) {

                _canvas.drawText(CUtils.FormatDegreeInDD(plntDegree),
                        (SCREEN_CONSTANTS.SKundliPlanetX8[indx8]+30),
                        (SCREEN_CONSTANTS.SKundliPlanetY8[indx8] - (SCREEN_CONSTANTS.TextColor.getTextSize() - 10) ),
                        getTempPaintObj(planetsAndColor));
            }

            ++indx8;
        }
        iInxedColor++;
    }

    private void printPlanetsInSouthKundliInRashi9(PlanetsAndColor planetsAndColor,
                                                   double plntDegree) {
        if (iInxedColor > 5)
            iInxedColor = 0;
        if (indx9 < SCREEN_CONSTANTS.STKundliPlanetX9.length) {
            if (plntDegree > 0 && isDeviceTablet) {
                _canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.STKundliPlanetX9[indx9],
                        SCREEN_CONSTANTS.STKundliPlanetY9[indx9],
                        planetsAndColor.getPaint());
                indx9++;
                _canvas.drawText(CUtils.FormatDegreeInDDMM(plntDegree),
                        SCREEN_CONSTANTS.STKundliPlanetX9[indx9],
                        SCREEN_CONSTANTS.STKundliPlanetY9[indx9],
                        planetsAndColor.getPaint());
                indx9++;
            } else {
                if (indx9 > 5)
                    indx9 = 0;

                _canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.SKundliPlanetX9[indx9],
                        SCREEN_CONSTANTS.SKundliPlanetY9[indx9],
                        planetsAndColor.getPaint());

                if (plntDegree > 0) {

                    _canvas.drawText(CUtils.FormatDegreeInDD(plntDegree),
                            (SCREEN_CONSTANTS.SKundliPlanetX9[indx9]+30),
                            (SCREEN_CONSTANTS.SKundliPlanetY9[indx9] - (SCREEN_CONSTANTS.TextColor.getTextSize() - 10) ),
                            getTempPaintObj(planetsAndColor));
                }

                ++indx9;
            }
        }
        iInxedColor++;
    }

    private void printPlanetsInSouthKundliInRashi10(PlanetsAndColor planetsAndColor,
                                                    double plntDegree) {
        if (iInxedColor > 5)
            iInxedColor = 0;
        if (indx10 < SCREEN_CONSTANTS.STKundliPlanetY10.length) {
            if (plntDegree > 0 && isDeviceTablet) {
                _canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.STKundliPlanetX10[indx10],
                        SCREEN_CONSTANTS.STKundliPlanetY10[indx10],
                        planetsAndColor.getPaint());
                indx10++;
                _canvas.drawText(CUtils.FormatDegreeInDDMM(plntDegree),
                        SCREEN_CONSTANTS.STKundliPlanetX10[indx10],
                        SCREEN_CONSTANTS.STKundliPlanetY10[indx10],
                        planetsAndColor.getPaint());
                indx10++;
            } else {
                if (indx10 > 5)
                    indx10 = 0;

                _canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.SKundliPlanetX10[indx10],
                        SCREEN_CONSTANTS.SKundliPlanetY10[indx10],
                        planetsAndColor.getPaint());

                if (plntDegree > 0) {

                    _canvas.drawText(CUtils.FormatDegreeInDD(plntDegree),
                            (SCREEN_CONSTANTS.SKundliPlanetX10[indx10]+30),
                            (SCREEN_CONSTANTS.SKundliPlanetY10[indx10] - (SCREEN_CONSTANTS.TextColor.getTextSize() - 10) ),
                            getTempPaintObj(planetsAndColor));
                }

                ++indx10;
            }
        }
        iInxedColor++;
    }

    private void printPlanetsInSouthKundliInRashi11(PlanetsAndColor planetsAndColor,
                                                    double plntDegree) {
        if (iInxedColor > 5)
            iInxedColor = 0;
        if (indx11 < SCREEN_CONSTANTS.STKundliPlanetX11.length) {
            if (plntDegree > 0 && isDeviceTablet) {
                _canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.STKundliPlanetX11[indx11],
                        SCREEN_CONSTANTS.STKundliPlanetY11[indx11],
                        planetsAndColor.getPaint());
                indx11++;
                _canvas.drawText(CUtils.FormatDegreeInDDMM(plntDegree),
                        SCREEN_CONSTANTS.STKundliPlanetX11[indx11],
                        SCREEN_CONSTANTS.STKundliPlanetY11[indx11],
                        planetsAndColor.getPaint());
                indx11++;
            } else {
                if (indx11 > 5)
                    indx11 = 0;

                _canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.SKundliPlanetX11[indx11],
                        SCREEN_CONSTANTS.SKundliPlanetY11[indx11],
                        planetsAndColor.getPaint());

                if (plntDegree > 0) {

                    _canvas.drawText(CUtils.FormatDegreeInDD(plntDegree),
                            (SCREEN_CONSTANTS.SKundliPlanetX11[indx11]+30),
                            (SCREEN_CONSTANTS.SKundliPlanetY11[indx11] - (SCREEN_CONSTANTS.TextColor.getTextSize() - 10) ),
                            getTempPaintObj(planetsAndColor));
                }

                ++indx11;
            }
        }
        iInxedColor++;
    }

    private void printPlanetsInSouthKundliInRashi12(PlanetsAndColor planetsAndColor,
                                                    double plntDegree) {
        if (iInxedColor > 5)
            iInxedColor = 0;
        if (indx12 < SCREEN_CONSTANTS.STKundliPlanetX12.length) {
            if (plntDegree > 0 && isDeviceTablet) {
                _canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.STKundliPlanetX12[indx12],
                        SCREEN_CONSTANTS.STKundliPlanetY12[indx12],
                        planetsAndColor.getPaint());
                indx12++;
                _canvas.drawText(CUtils.FormatDegreeInDDMM(plntDegree),
                        SCREEN_CONSTANTS.STKundliPlanetX12[indx12],
                        SCREEN_CONSTANTS.STKundliPlanetY12[indx12],
                        planetsAndColor.getPaint());
                indx12++;
            } else {
                if (indx12 > 5)
                    indx12 = 0;

                _canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.SKundliPlanetX12[indx12],
                        SCREEN_CONSTANTS.SKundliPlanetY12[indx12],
                        planetsAndColor.getPaint());

                if (plntDegree > 0) {

                    _canvas.drawText(CUtils.FormatDegreeInDD(plntDegree),
                            (SCREEN_CONSTANTS.SKundliPlanetX12[indx12]+30),
                            (SCREEN_CONSTANTS.SKundliPlanetY12[indx12] - (SCREEN_CONSTANTS.TextColor.getTextSize() - 10) ),
                            getTempPaintObj(planetsAndColor));
                }

                ++indx12;
            }
        }
        iInxedColor++;
    }


    ///EAST INDIA KUNDLI


    private void printPlanetsInEastKundliInRashi1(PlanetsAndColor planetsAndColor,
                                                  double plntDegree) {
        if (iInxedColor > 5)
            iInxedColor = 0;

   /* if (plntDegree > 0 && isDeviceTablet) {
         if(indx1>11)
            indx1=1;
         _canvas.drawText(plnt, SCREEN_CONSTANTS.ESKundliPlanetX1[indx1],
               SCREEN_CONSTANTS.ESKundliPlanetY1[indx1],
               SCREEN_CONSTANTS.KundliPlanetsCustomFont[iInxedColor]);
         indx1++;
         _canvas.drawText(CUtils.FormatDegreeInDDMM(plntDegree),
               SCREEN_CONSTANTS.ESKundliPlanetX1[indx1],
               SCREEN_CONSTANTS.ESKundliPlanetY1[indx1],
               SCREEN_CONSTANTS.KundliPlanetsCustomFont[iInxedColor]);
         indx1++;
      } else {*/
        if (indx1 > 5)
            indx1 = 0;

        _canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.EKundliPlanetX1[indx1],
                SCREEN_CONSTANTS.EKundliPlanetY1[indx1],
                planetsAndColor.getPaint());

        ++indx1;
        //}
        iInxedColor++;
    }

    private void printPlanetsInEastKundliInRashi2(PlanetsAndColor planetsAndColor,
                                                  double plntDegree) {
        if (iInxedColor > 5)
            iInxedColor = 0;

      /*if (plntDegree > 0 && isDeviceTablet) {
         if (indx2 > 11)
            indx2 = 0;
         _canvas.drawText(plnt, SCREEN_CONSTANTS.ESKundliPlanetX2[indx2],
               SCREEN_CONSTANTS.ESKundliPlanetY2[indx2],
               SCREEN_CONSTANTS.KundliPlanetsCustomFont[iInxedColor]);
         indx2++;
         _canvas.drawText(CUtils.FormatDegreeInDDMM(plntDegree),
               SCREEN_CONSTANTS.ESKundliPlanetX2[indx2],
               SCREEN_CONSTANTS.ESKundliPlanetY2[indx2],
               SCREEN_CONSTANTS.KundliPlanetsCustomFont[iInxedColor]);
         indx2++;
      } else {*/
        if (indx2 > 5)
            indx2 = 0;

        _canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.EKundliPlanetX2[indx2],
                SCREEN_CONSTANTS.EKundliPlanetY2[indx2],
                planetsAndColor.getPaint());

        ++indx2;
        //}
        iInxedColor++;
    }

    private void printPlanetsInEastKundliInRashi3(PlanetsAndColor planetsAndColor,
                                                  double plntDegree) {
        if (iInxedColor > 5)
            iInxedColor = 0;

      /*if (plntDegree > 0 && isDeviceTablet) {
         if (indx3 > 11)
            indx3 = 0;
         _canvas.drawText(plnt, SCREEN_CONSTANTS.ESKundliPlanetX3[indx3],
               SCREEN_CONSTANTS.ESKundliPlanetY3[indx3],
               SCREEN_CONSTANTS.KundliPlanetsCustomFont[iInxedColor]);
         indx3++;
         _canvas.drawText(CUtils.FormatDegreeInDDMM(plntDegree),
               SCREEN_CONSTANTS.ESKundliPlanetX3[indx3],
               SCREEN_CONSTANTS.ESKundliPlanetY3[indx3],
               SCREEN_CONSTANTS.KundliPlanetsCustomFont[iInxedColor]);
         indx3++;
      } else {*/
        if (indx3 > 5)
            indx3 = 0;

        _canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.EKundliPlanetX3[indx3],
                SCREEN_CONSTANTS.EKundliPlanetY3[indx3],
                planetsAndColor.getPaint());

        ++indx3;
        //}
        iInxedColor++;
    }

    private void printPlanetsInEastKundliInRashi4(PlanetsAndColor planetsAndColor,
                                                  double plntDegree) {
        if (iInxedColor > 5)
            iInxedColor = 0;

      /*if (plntDegree > 0 && isDeviceTablet) {
         if (indx4 > 11)
            indx4 = 0;
         _canvas.drawText(plnt, SCREEN_CONSTANTS.ESKundliPlanetX4[indx4],
               SCREEN_CONSTANTS.ESKundliPlanetY4[indx4],
               SCREEN_CONSTANTS.KundliPlanetsCustomFont[iInxedColor]);
         indx4++;
         _canvas.drawText(CUtils.FormatDegreeInDDMM(plntDegree),
               SCREEN_CONSTANTS.ESKundliPlanetX4[indx4],
               SCREEN_CONSTANTS.ESKundliPlanetY4[indx4],
               SCREEN_CONSTANTS.KundliPlanetsCustomFont[iInxedColor]);
         indx4++;
      } else {*/
        if (indx4 > 5)
            indx4 = 0;

        _canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.EKundliPlanetX4[indx4],
                SCREEN_CONSTANTS.EKundliPlanetY4[indx4],
                planetsAndColor.getPaint());

        ++indx4;
        //}
        iInxedColor++;
    }

    private void printPlanetsInEastKundliInRashi5(PlanetsAndColor planetsAndColor,
                                                  double plntDegree) {
        if (iInxedColor > 5)
            iInxedColor = 0;

      /*if (plntDegree > 0 && isDeviceTablet) {
         if (indx5 > 11)
            indx5 = 0;
         _canvas.drawText(plnt, SCREEN_CONSTANTS.ESKundliPlanetX5[indx5],
               SCREEN_CONSTANTS.ESKundliPlanetY5[indx5],
               SCREEN_CONSTANTS.KundliPlanetsCustomFont[iInxedColor]);
         indx5++;
         _canvas.drawText(CUtils.FormatDegreeInDDMM(plntDegree),
               SCREEN_CONSTANTS.ESKundliPlanetX5[indx5],
               SCREEN_CONSTANTS.ESKundliPlanetY5[indx5],
               SCREEN_CONSTANTS.KundliPlanetsCustomFont[iInxedColor]);
         indx5++;
      } else {*/
        if (indx5 > 5)
            indx5 = 0;

        _canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.EKundliPlanetX5[indx5],
                SCREEN_CONSTANTS.EKundliPlanetY5[indx5],
                planetsAndColor.getPaint());

        ++indx5;
        //}
        iInxedColor++;
    }

    private void printPlanetsInEastKundliInRashi6(PlanetsAndColor planetsAndColor,
                                                  double plntDegree) {
        if (iInxedColor > 5)
            iInxedColor = 0;

   /* if (plntDegree > 0 && isDeviceTablet) {
         if (indx6 > 11)
            indx6 = 0;
         _canvas.drawText(plnt, SCREEN_CONSTANTS.ESKundliPlanetX6[indx6],
               SCREEN_CONSTANTS.ESKundliPlanetY6[indx6],
               SCREEN_CONSTANTS.KundliPlanetsCustomFont[iInxedColor]);
         indx6++;
         _canvas.drawText(CUtils.FormatDegreeInDDMM(plntDegree),
               SCREEN_CONSTANTS.ESKundliPlanetX6[indx6],
               SCREEN_CONSTANTS.ESKundliPlanetY6[indx6],
               SCREEN_CONSTANTS.KundliPlanetsCustomFont[iInxedColor]);
         indx6++;
      } else {*/
        if (indx6 > 5)
            indx6 = 0;

        _canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.EKundliPlanetX6[indx6],
                SCREEN_CONSTANTS.EKundliPlanetY6[indx6],
                planetsAndColor.getPaint());

        ++indx6;
        //}
        iInxedColor++;
    }

    private void printPlanetsInEastKundliInRashi7(PlanetsAndColor planetsAndColor,
                                                  double plntDegree) {
        if (iInxedColor > 5)
            iInxedColor = 0;

      /*if (plntDegree > 0 && isDeviceTablet) {
         if (indx7 > 11)
            indx7= 0;
         _canvas.drawText(plnt, SCREEN_CONSTANTS.ESKundliPlanetX7[indx7],
               SCREEN_CONSTANTS.ESKundliPlanetY7[indx7],
               SCREEN_CONSTANTS.KundliPlanetsCustomFont[iInxedColor]);
         indx7++;
         _canvas.drawText(CUtils.FormatDegreeInDDMM(plntDegree),
               SCREEN_CONSTANTS.ESKundliPlanetX7[indx7],
               SCREEN_CONSTANTS.ESKundliPlanetY7[indx7],
               SCREEN_CONSTANTS.KundliPlanetsCustomFont[iInxedColor]);
         indx7++;
      } else {*/
        if (indx7 > 5)
            indx7 = 0;
        _canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.EKundliPlanetX7[indx7],
                SCREEN_CONSTANTS.EKundliPlanetY7[indx7],
                planetsAndColor.getPaint());

        ++indx7;
        //}
        iInxedColor++;
    }

    private void printPlanetsInEastKundliInRashi8(PlanetsAndColor planetsAndColor,
                                                  double plntDegree) {
        if (iInxedColor > 5)
            iInxedColor = 0;
      /*if (plntDegree > 0 && isDeviceTablet) {
         if (indx8 > 11)
            indx8 = 0;
         _canvas.drawText(plnt, SCREEN_CONSTANTS.ESKundliPlanetX8[indx8],
               SCREEN_CONSTANTS.ESKundliPlanetY8[indx8],
               SCREEN_CONSTANTS.KundliPlanetsCustomFont[iInxedColor]);
         indx8++;
         _canvas.drawText(CUtils.FormatDegreeInDDMM(plntDegree),
               SCREEN_CONSTANTS.ESKundliPlanetX8[indx8],
               SCREEN_CONSTANTS.ESKundliPlanetY8[indx8],
               SCREEN_CONSTANTS.KundliPlanetsCustomFont[iInxedColor]);
         indx8++;
      } else {*/
        if (indx8 > 5)
            indx8 = 0;

        _canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.EKundliPlanetX8[indx8],
                SCREEN_CONSTANTS.EKundliPlanetY8[indx8],
                planetsAndColor.getPaint());

        ++indx8;
        //}
        iInxedColor++;
    }

    private void printPlanetsInEastKundliInRashi9(PlanetsAndColor planetsAndColor,
                                                  double plntDegree) {
        if (iInxedColor > 5)
            iInxedColor = 0;

      /*if (plntDegree > 0 && isDeviceTablet) {
         if (indx9 > 11)
            indx9= 0;
         _canvas.drawText(plnt, SCREEN_CONSTANTS.ESKundliPlanetX9[indx9],
               SCREEN_CONSTANTS.ESKundliPlanetY9[indx9],
               SCREEN_CONSTANTS.KundliPlanetsCustomFont[iInxedColor]);
         indx9++;
         _canvas.drawText(CUtils.FormatDegreeInDDMM(plntDegree),
               SCREEN_CONSTANTS.ESKundliPlanetX9[indx9],
               SCREEN_CONSTANTS.ESKundliPlanetY9[indx9],
               SCREEN_CONSTANTS.KundliPlanetsCustomFont[iInxedColor]);
         indx9++;
      } else {*/
        if (indx9 > 5)
            indx9 = 0;

        _canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.EKundliPlanetX9[indx9],
                SCREEN_CONSTANTS.EKundliPlanetY9[indx9],
                planetsAndColor.getPaint());

        ++indx9;
        //}
        iInxedColor++;
    }

    private void printPlanetsInEastKundliInRashi10(PlanetsAndColor planetsAndColor,
                                                   double plntDegree) {
        if (iInxedColor > 5)
            iInxedColor = 0;

      /*if (plntDegree > 0 && isDeviceTablet) {
         if (indx10 > 11)
            indx10 = 1;
         {
         _canvas.drawText(plnt, SCREEN_CONSTANTS.ESKundliPlanetX10[indx10],
               SCREEN_CONSTANTS.ESKundliPlanetY10[indx10],
               SCREEN_CONSTANTS.KundliPlanetsCustomFont[iInxedColor]);
         indx10++;
         _canvas.drawText(CUtils.FormatDegreeInDDMM(plntDegree),
               SCREEN_CONSTANTS.ESKundliPlanetX10[indx10],
               SCREEN_CONSTANTS.ESKundliPlanetY10[indx10],
               SCREEN_CONSTANTS.KundliPlanetsCustomFont[iInxedColor]);
         indx10++;
         }
      } else {*/
        if (indx10 > 5)
            indx10 = 0;

        _canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.EKundliPlanetX10[indx10],
                SCREEN_CONSTANTS.EKundliPlanetY10[indx10],
                planetsAndColor.getPaint());

        ++indx10;
        //}
        iInxedColor++;
    }

    private void printPlanetsInEastKundliInRashi11(PlanetsAndColor planetsAndColor,
                                                   double plntDegree) {
        if (iInxedColor > 5)
            iInxedColor = 0;

      /*if (plntDegree > 0 && isDeviceTablet) {
         if (indx11 > 11)
            indx11 = 0;
         _canvas.drawText(plnt, SCREEN_CONSTANTS.ESKundliPlanetX11[indx11],
               SCREEN_CONSTANTS.ESKundliPlanetY11[indx11],
               SCREEN_CONSTANTS.KundliPlanetsCustomFont[iInxedColor]);
         indx11++;
         _canvas.drawText(CUtils.FormatDegreeInDDMM(plntDegree),
               SCREEN_CONSTANTS.ESKundliPlanetX11[indx11],
               SCREEN_CONSTANTS.ESKundliPlanetY11[indx11],
               SCREEN_CONSTANTS.KundliPlanetsCustomFont[iInxedColor]);
         indx11++;
      } else {*/
        if (indx11 > 5)
            indx11 = 0;

        _canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.EKundliPlanetX11[indx11],
                SCREEN_CONSTANTS.EKundliPlanetY11[indx11],
                planetsAndColor.getPaint());

        ++indx11;
        //}
        iInxedColor++;
    }

    private void printPlanetsInEastKundliInRashi12(PlanetsAndColor planetsAndColor,
                                                   double plntDegree) {
        if (iInxedColor > 5)
            iInxedColor = 0;

      /*if (plntDegree > 0 && isDeviceTablet) {
         if (indx12 > 5)
            indx12 = 0;
         _canvas.drawText(plnt, SCREEN_CONSTANTS.ESKundliPlanetX12[indx12],
               SCREEN_CONSTANTS.ESKundliPlanetY12[indx12],
               SCREEN_CONSTANTS.KundliPlanetsCustomFont[iInxedColor]);
         indx12++;
         _canvas.drawText(CUtils.FormatDegreeInDDMM(plntDegree),
               SCREEN_CONSTANTS.ESKundliPlanetX12[indx12],
               SCREEN_CONSTANTS.ESKundliPlanetY12[indx12],
               SCREEN_CONSTANTS.KundliPlanetsCustomFont[iInxedColor]);
         indx12++;
      } else {*/
        if (indx12 > 5)
            indx12 = 0;

        _canvas.drawText(planetsAndColor.getPlanetName(), SCREEN_CONSTANTS.EKundliPlanetX12[indx12],
                SCREEN_CONSTANTS.EKundliPlanetY12[indx12],
                planetsAndColor.getPaint());

        ++indx12;
        //}
        iInxedColor++;
    }


    //END OF EAST KUNDLI
//mahtab for goacher
    private void initValuesTransit(boolean isDefaultValueTaken, int value, int selectFirstRashi[]) {
        w = SCREEN_CONSTANTS.DeviceScreenWidth;
        h = w;
        x = y = 0;
        centerX = w/2f;
        centerY = h/2f;
        offset = h/4;
        eastKundliOffset = w/3;
        if (!isDefaultValueTaken)
            _lagna = selectFirstRashi[12];
        else if (isDefaultValueTaken) {
            _lagna = selectFirstRashi[value];
        }
        laganKarksaOne = _lagna;
        fillChartRashi(_lagna);
        initVariablesWithDefaultvalues();

    }

    /**
     * Return arraylist of PlanetsAndColor with _directOrRect and planet color
     *
     * @return
     */

    ArrayList<PlanetsAndColor> getPlantNameWithDirectAndRedirect() {

        Paint[] planetColor = SCREEN_CONSTANTS.KundliPlanetsCustomFont;
        ArrayList<PlanetsAndColor> planetNameWithDirectAndRedirect = new ArrayList<PlanetsAndColor>();
        try {
            String[] direct = CUtils.getDirectArray(_context);
            String[] combust = CUtils.getCombustArray(_context);

            // SAN Testing
            String shodashvargaLagan[] = CGlobal.getCGlobalObject().getOutShodashvargaObject().get_lagna().split(",");
            String shodashvargaNavmasha[] = CGlobal.getCGlobalObject().getOutShodashvargaObject().get_navamsha().split(",");

            String planetRelationArr[] = CGlobal.getCGlobalObject().getPlanetDataObject().getRelation().split(",");
            if(isForTransit){
                planetRelationArr = CGlobal.getCGlobalObject().getPlanetDataObjectTransit().getRelation().split(",");
            }

           /* Log.e("TEST_KUNDLI","\n***********************\n");
            for(int i=0; i<planetRelationArr.length;i++){
                Log.e("TEST_KUNDLI",planetRelationArr[i]);
            }*/

            PlanetsAndColor planetsAndColor;
            String _directOrRect = "";
            for (int i = 0; i < _planetsName.length; i++) {
                _directOrRect = _planetsName[i];
                if (isShowDirectOrCumbust) {
                    try {

                        if (i < direct.length) {

                            if (direct[i].equals(_context.getResources().getString(R.string.retroget))) {
                                direct[i] = "*";
                            }

                            _directOrRect += direct[i];

                            if (i != 0) {

                                if (combust[i - 1].trim().length() > 0) {
                                    if (combust[i - 1].equals(_context.getResources().getString(R.string.combst))) {
                                        combust[i - 1] = "^";
                                    }
                                    _directOrRect += combust[i - 1];
                                }
                            }

                            // SAN for Vagottam
                            //Log.e("SAN ", " i shodashvargaLagan[i] <=> shodashvargaNavmasha[i] => " +  i + " <=> " + shodashvargaLagan[i] + "<=>" + shodashvargaNavmasha[i] );
                            if ( shodashvargaLagan[i+1].equals(shodashvargaNavmasha[i+1]) ) {
                                _directOrRect += unicodeBargotttam;//newstr;
                                //Log.e("SAN ", " i shodashvargaLagan[i] <=> shodashvargaNavmasha[i] => " +  i + " <=> " + shodashvargaLagan[i] + "<=>" + shodashvargaNavmasha[i] + " Matched " + _directOrRect  );
                            } else {
                                //Log.e("SAN ", " i shodashvargaLagan[i] <=> shodashvargaNavmasha[i] => " +  i + " <=> " + shodashvargaLagan[i] + "<=>" + shodashvargaNavmasha[i] + " Not Matched " + _directOrRect );
                            }

                            // SAN for Planet Relation
                            if ( planetRelationArr[i].equals("1") ){  // Exalted
                                _directOrRect += unicodeExalted ; //"\ua71b";  // Arrow UP
                            } else if ( planetRelationArr[i].equals("2") ){  // Debiliated
                                _directOrRect += unicodeDebiliated; // "\ua71c"; // Arrow DOWN
                            }

                            // SAN Test

                            //_directOrRect += "^" + "\u2e0b" + "\ua71b" + "\ua71c";

                                /*else if ( planetRelationArr[i].equals("3") ){   // Own
                                    _directOrRect += "\u142a"; // T
                                } else if ( planetRelationArr[i].equals("4") ){  // Friendly
                                    _directOrRect += "\u21ab"; //;  "\u21ab"; //  up
                                } else if ( planetRelationArr[i].equals("5") ){   // Enemy
                                    _directOrRect += "\u21ac"; //  down
                                } else if ( planetRelationArr[i].equals("6") ){ // Neutral
                                    _directOrRect += "\u1d17"; // upper cap
                                }*/

                        }

                    } catch (Exception e) {

                    }
                }
                planetsAndColor = new PlanetsAndColor();
                planetsAndColor.setPlanetName(_directOrRect);
                planetsAndColor.setPaint(planetColor[i]);
                //planetsAndColor.setPaintSmSize(planetColor[i]);
                planetNameWithDirectAndRedirect.add(planetsAndColor);
            }
        } catch (Exception e) {
            //
        }
        return planetNameWithDirectAndRedirect;
    }

    public static Spanned fromHtml(String html, int flags) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            return Html.fromHtml(html, flags);
        } else {
            return Html.fromHtml(html);
        }
    }

    /**
     * This method print retrograte and combust hint text.
     *
     * @param x
     * @param y
     */

    private void drawRetrogradeAndCombustHintText(float x, float y, float h1) {
        // _canvas.drawRect(x - 10, y - 40, x + w, y + 20, SCREEN_CONSTANTS.retroRectBackgroundPaint);
        // _canvas.drawText(getResources().getString(R.string.retrograde) + "     " + getResources().getString(R.string.combust), x + 10, y + 5, SCREEN_CONSTANTS.TextColor);
        // _canvas.drawRect(x + 250, y - 40, x + 450, y + 20, SCREEN_CONSTANTS.combustRectBackgroundPaint);
        // _canvas.drawText(getResources().getString(R.string.combust), x + 260, y, SCREEN_CONSTANTS.TextColor);

        /* SAN Experiment */
        //float textSize = 0.0f;
        float textSize = SCREEN_CONSTANTS.TextColor.getTextSize();
        float textSizeHalf = textSize / 2;

        //Log.e("SAN ", " drawRetrogradeAndCombustHintText x,y,h1" + x +","+ y +","+ h1 + "<= width  =>"+ SCREEN_CONSTANTS.DeviceScreenWidth);
        int ot = (int) (w / 3); // (int) (SCREEN_CONSTANTS.DeviceScreenWidth/3);
        float y1 = y + h1;

        // _canvas.drawRect(x - 10, y - (textSize + textSizeHalf), x + w, y + textSizeHalf, SCREEN_CONSTANTS.retroRectBackgroundPaint);
        _canvas.drawRect(x - 10, y - (textSize + textSizeHalf), x + w, y1 + textSizeHalf , SCREEN_CONSTANTS.retroRectBackgroundPaint);
        _canvas.drawText(_context.getResources().getString(R.string.retrograde) , x + 10, y, SCREEN_CONSTANTS.TextColorKundliHint);
        _canvas.drawText(_context.getResources().getString(R.string.combust) , x + ot, y, SCREEN_CONSTANTS.TextColorKundliHint);
        _canvas.drawText(_context.getResources().getString(R.string.vargottama) , x + ot+ot, y, SCREEN_CONSTANTS.TextColorKundliHint);

        //_canvas.drawRect(x - 10, y + textSizeHalf , x + w, y1 + textSizeHalf , SCREEN_CONSTANTS.retroRectBackgroundPaint);
        _canvas.drawText(_context.getResources().getString(R.string.exalted) , x+ 10, y1, SCREEN_CONSTANTS.TextColorKundliHint);
        _canvas.drawText(_context.getResources().getString(R.string.debilitated) , x + ot, y1 , SCREEN_CONSTANTS.TextColorKundliHint);

    }

    public Paint getTempPaintObj(PlanetsAndColor planetsAndColor) {

        /*PlanetsAndColor planetsAndColorTmp = new PlanetsAndColor();
        planetsAndColorTmp.setPaint( planetsAndColor.getPaint() );
        planetsAndColorTmp.getPaint().setTextSize((SCREEN_CONSTANTS.TextColor.getTextSize()/2));*/

        int colorCode =  planetsAndColor.getPaint().getColor();

        Paint paintTmp = new Paint();
        paintTmp.setColor( colorCode );
        paintTmp.setTextSize(SCREEN_CONSTANTS.TextColor.getTextSize()/2);

        return paintTmp;

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

    private Region createRegionFromPath(Path path) {
        // Define a bounding rectangle for the region
        RectF bounds = new RectF();
        path.computeBounds(bounds, true);
        Region region = new Region();
        region.setPath(path, new Region((int) bounds.left, (int) bounds.top, (int) bounds.right, (int) bounds.bottom));
        return region;
    }

    @FunctionalInterface
   public interface OnKundliCellClickListener{
         void onCellClicked(int cellNumber);
    }

    public void setKundliCellClickListener(OnKundliCellClickListener l){
        this.cellClickListener = l;
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

//end
}