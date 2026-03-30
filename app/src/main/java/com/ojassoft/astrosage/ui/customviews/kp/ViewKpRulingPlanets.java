package com.ojassoft.astrosage.ui.customviews.kp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.view.View;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.OutKpRulingPlanets;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CScreenConstants;

public class ViewKpRulingPlanets extends View {


    private Canvas _canvas = null;
    //private String [] headingRulingPlanets;
    private OutKpRulingPlanets _outKpRulingPlanets = null;

    int y1 = 0;
    Context _context;
    private int startX = 10;
    float[] xcoor = {0, 0.9f, 2.2f, 2.7f, 3.2f, 3.6f};
    int xLength = 0;
    int yHeight = 0;
    String[] _heading = null;
    String _dayLordName;
    Typeface _typeface = null;
    CScreenConstants SCREEN_CONSTANTS;
    float topHeadingY;

    public ViewKpRulingPlanets(Context context, OutKpRulingPlanets outKpRulingPlanets, String[] Heading, String DayLordName, Typeface typeface, CScreenConstants SCREEN_CONSTANTS) {
        super(context);
        _context = context;
        _outKpRulingPlanets = outKpRulingPlanets;
        _heading = Heading;
        _dayLordName = DayLordName;
        _typeface = typeface;
        this.SCREEN_CONSTANTS = SCREEN_CONSTANTS;
        y1 = ((int) SCREEN_CONSTANTS.DeviceScreenHeight) / 10;
        topHeadingY = SCREEN_CONSTANTS.DeviceScreenHeight / 18;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        _canvas = canvas;
        printTopHeading();
        //printRulingPlanets();
    }


    private void printRulingPlanets() {
        int _totalLines = _heading.length;
        int screenWidth = (int) (((int) SCREEN_CONSTANTS.DeviceScreenWidth) / 1.5);
        int hSpace = 25;
        int x = 10;
        screenWidth = screenWidth + 10;

        for (int i = 0; i < _totalLines; i++) {
            if((i%2==0)) {
                if (this.SCREEN_CONSTANTS.NewDeviceScreenWidth < CGlobalVariables.SMALL_DEVICE_WIDTH) {
                    _canvas.drawRect(3, yHeight-CScreenConstants.pxToDp(CScreenConstants.margininline-CScreenConstants.rectHieghtMarginTop,getContext()), this.SCREEN_CONSTANTS.NewDeviceScreenWidth, yHeight+CScreenConstants.pxToDp(-CScreenConstants.rectHieghtMarginBottom,getContext()), SCREEN_CONSTANTS.TableRow_Background_Color);
                } else {
                    _canvas.drawRect(3, yHeight-CScreenConstants.pxToDp(CScreenConstants.margininline-CScreenConstants.rectHieghtMarginTop,getContext()), this.SCREEN_CONSTANTS.NewDeviceScreenWidth, yHeight+CScreenConstants.pxToDp(CScreenConstants.rectHieghtMarginBottom,getContext()), SCREEN_CONSTANTS.TableRow_Background_Color);
                }
            }
            _canvas.drawText(_heading[i], x, yHeight, SCREEN_CONSTANTS.contentPaint);

            switch (i) {
                case 0:
                    _canvas.drawText(_dayLordName, screenWidth + hSpace, yHeight, SCREEN_CONSTANTS.contentPaint);
                    break;
                case 1:
                    _canvas.drawText(_outKpRulingPlanets.getAscSignLord(), screenWidth + hSpace, yHeight, SCREEN_CONSTANTS.contentPaint);
                    break;
                case 2:
                    _canvas.drawText(_outKpRulingPlanets.getAscNakLord(), screenWidth + hSpace, yHeight, SCREEN_CONSTANTS.contentPaint);
                    break;
                case 3:
                    _canvas.drawText(_outKpRulingPlanets.getAscSubLord(), screenWidth + hSpace, yHeight, SCREEN_CONSTANTS.contentPaint);
                    break;
                case 4:
                    _canvas.drawText(_outKpRulingPlanets.getMoonSignLord(), screenWidth + hSpace, yHeight, SCREEN_CONSTANTS.contentPaint);
                    break;
                case 5:
                    _canvas.drawText(_outKpRulingPlanets.getMoonNakLord(), screenWidth + hSpace, yHeight, SCREEN_CONSTANTS.contentPaint);
                    break;
                case 6:
                    _canvas.drawText(_outKpRulingPlanets.getMoonSubLord(), screenWidth + hSpace, yHeight, SCREEN_CONSTANTS.contentPaint);

                    break;

            }
            yHeight = yHeight + CScreenConstants.pxToDp(CScreenConstants.margininline,getContext());
        }


    }

    private void printTopHeading() {
        xLength = ((int) SCREEN_CONSTANTS.DeviceScreenWidth) / 4;
       // yHeight = ((int) SCREEN_CONSTANTS.DeviceScreenHeight) / 18;
        yHeight = CScreenConstants.pxToDp(CScreenConstants.topMargin, _context);
        _canvas.drawText(getResources().getString(R.string.ruling_planet_top_heading), startX + xcoor[0], yHeight, SCREEN_CONSTANTS.headingPaint);
        yHeight = yHeight + CScreenConstants.pxToDp(CScreenConstants.firstLineTopMargin, getContext());
        printRulingPlanets();

    }
}
