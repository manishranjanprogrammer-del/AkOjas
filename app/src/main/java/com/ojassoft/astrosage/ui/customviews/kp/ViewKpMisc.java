package com.ojassoft.astrosage.ui.customviews.kp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.view.View;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.OutKpMisc;
import com.ojassoft.astrosage.utils.CGlobal;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CScreenConstants;
import com.ojassoft.astrosage.utils.CUtils;

public class ViewKpMisc extends View {

    private Canvas _canvas = null;
    private String[] _heading;

    private String _degSign, _minSign, _secSign;
    int y1 = 0;
    private OutKpMisc _outKpMisc = null;
    Typeface _typeface = null;
    CScreenConstants SCREEN_CONSTANTS;
    float topHeadingY;
    int xLength = 0;
    int yHeight = 0;
    private int startX = 10;
    float[] xcoor = {0, 0.9f, 2.2f, 2.7f, 3.2f, 3.6f};
    Context context;

    public ViewKpMisc(Context context, OutKpMisc outKpMisc, String[] Heading, Typeface typeface, String DegSign, String MinSign, String SecSign, CScreenConstants SCREEN_CONSTANTS) {
        super(context);
        _outKpMisc = outKpMisc;
        _heading = Heading;
        _typeface = typeface;
        _degSign = DegSign;
        _minSign = MinSign;
        _secSign = SecSign;
        this.context=context;
        this.SCREEN_CONSTANTS = SCREEN_CONSTANTS;
        y1 = ((int) SCREEN_CONSTANTS.DeviceScreenHeight) / 7;
        topHeadingY = SCREEN_CONSTANTS.DeviceScreenHeight / 17;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        _canvas = canvas;

        printTopHeading();
    }


    private void printKpMisc() {


        int _totalLines = _heading.length;
        int screenWidth = ((int) SCREEN_CONSTANTS.DeviceScreenWidth) / 2;
        int hSpace = 30;
        int x = 10;
        int iColourIndex = -1;

        for (int i = 0; i < _totalLines; i++) {
            iColourIndex++;
            if (iColourIndex > 5)
                iColourIndex = 0;

            if (i < _totalLines - 1){
                if((i%2==0)) {
                    if (this.SCREEN_CONSTANTS.NewDeviceScreenWidth < CGlobalVariables.SMALL_DEVICE_WIDTH) {
                        _canvas.drawRect(3, yHeight-CScreenConstants.pxToDp(CScreenConstants.margininline-CScreenConstants.rectHieghtMarginTop,getContext()), this.SCREEN_CONSTANTS.NewDeviceScreenWidth, yHeight+CScreenConstants.pxToDp(-CScreenConstants.rectHieghtMarginBottom,getContext()), SCREEN_CONSTANTS.TableRow_Background_Color);
                    } else {
                        _canvas.drawRect(3, yHeight-CScreenConstants.pxToDp(CScreenConstants.margininline-CScreenConstants.rectHieghtMarginTop,getContext()), this.SCREEN_CONSTANTS.NewDeviceScreenWidth, yHeight+CScreenConstants.pxToDp(CScreenConstants.rectHieghtMarginBottom,getContext()), SCREEN_CONSTANTS.TableRow_Background_Color);
                    }
                }
                _canvas.drawText(_heading[i], x, yHeight, SCREEN_CONSTANTS.contentPaint);}
            switch (i) {

                case 0:
                    _canvas.drawText(CUtils.FormatDMSInStringWithSign(_outKpMisc.getFortunaDeg(), _degSign, _minSign, _secSign), screenWidth + hSpace, yHeight, SCREEN_CONSTANTS.contentPaint);
                    break;
                case 1:
                    _canvas.drawText(_outKpMisc.getFortunaRasit(), screenWidth + hSpace, yHeight, SCREEN_CONSTANTS.contentPaint);
                    break;
                case 2:
                    _canvas.drawText(_outKpMisc.getFortunaSubSub(), screenWidth + hSpace, yHeight, SCREEN_CONSTANTS.contentPaint);
                    break;
                case 3:
                    _canvas.drawText(CUtils.FormatDMSInStringWithSign(_outKpMisc.getKpAyan(), _degSign, _minSign, _secSign), screenWidth + hSpace, yHeight, SCREEN_CONSTANTS.contentPaint);
                    break;
                case 4:
                    if (Integer.valueOf(CGlobal.getCGlobalObject().getHoroPersonalInfoObject().getHoraryNumber()) > 0) {
                        _canvas.drawText(_heading[i], x, yHeight, SCREEN_CONSTANTS.contentPaint);
                        _canvas.drawText(String.valueOf(CGlobal.getCGlobalObject().getHoroPersonalInfoObject().getHoraryNumber()),
                                screenWidth + hSpace, yHeight, SCREEN_CONSTANTS.contentPaint);
                    }
                    break;
            }
            yHeight = yHeight + CScreenConstants.pxToDp(CScreenConstants.margininline, getContext());

        }

    }

    private void printTopHeading() {
        xLength = ((int) SCREEN_CONSTANTS.DeviceScreenWidth) / 4;
        //yHeight = ((int) SCREEN_CONSTANTS.DeviceScreenHeight) / 18;
        yHeight = CScreenConstants.pxToDp(CScreenConstants.topMargin, context);
        _canvas.drawText(getResources().getString(R.string.fortuna_top_heading), startX + xcoor[0], yHeight, SCREEN_CONSTANTS.headingPaint);
        yHeight = yHeight + CScreenConstants.pxToDp(CScreenConstants.firstLineTopMargin, getContext());
        printKpMisc();
    }


}
