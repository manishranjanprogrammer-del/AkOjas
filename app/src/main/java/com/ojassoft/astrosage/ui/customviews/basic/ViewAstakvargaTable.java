package com.ojassoft.astrosage.ui.customviews.basic;


import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

import com.ojassoft.astrosage.misc.CGenerateAppViews;
import com.ojassoft.astrosage.utils.CGlobal;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CScreenConstants;

public class ViewAstakvargaTable extends View {

    private Canvas _canvas=null;
    private String [] heading;
    private int _totalLines;

    private int screenWidth = 0;
    int yHeight = 0;
    private int y = 30;
    private int vSpace = 0;
    private int hSpace = 0;
    private int x = 10;
    private int _hDifference = 0;
    CScreenConstants SCREEN_CONSTANTS;

    int y1 = 0;

    public ViewAstakvargaTable(Context context,CScreenConstants SCREEN_CONTANTS,String [] heading) {
        super(context);
        this.SCREEN_CONSTANTS = SCREEN_CONTANTS;
        this.heading=heading;
        screenWidth = ((int) SCREEN_CONTANTS.DeviceScreenWidth);
        yHeight=((int)SCREEN_CONTANTS.DeviceScreenHeight)/18;
        _hDifference = screenWidth/9;
        _totalLines = heading.length;
        y1=((int)SCREEN_CONTANTS.DeviceScreenHeight)/17;
        hSpace=screenWidth/(_totalLines+1);
        vSpace=((int) SCREEN_CONTANTS.DeviceScreenHeight)/14 ;
    }
    @Override
    protected void onDraw(Canvas canvas) {
        _canvas=canvas;
        drawHeading();
        drawView();

    }

    public void drawView() {

        int x1=x;
        String []plnNum=null;
        // FOR SUN
        plnNum=CGlobal.getCGlobalObject().getOutAstakvargaTableObject().getSun().split(",");
        x1 +=  _hDifference;
        printAstakvarga(plnNum,x1);

        // FOR MOON
        plnNum=CGlobal.getCGlobalObject().getOutAstakvargaTableObject().getMoon().split(",");
        x1 +=  _hDifference;
        printAstakvarga(plnNum,x1);

        // FOR MAR
        plnNum=CGlobal.getCGlobalObject().getOutAstakvargaTableObject().getMars().split(",");
        x1 +=  _hDifference;
        printAstakvarga(plnNum,x1);

        // FOR MERCURY
        plnNum=CGlobal.getCGlobalObject().getOutAstakvargaTableObject().getMercury().split(",");
        x1 +=  _hDifference;
        printAstakvarga(plnNum,x1);

        // FOR JUPITER
        plnNum=CGlobal.getCGlobalObject().getOutAstakvargaTableObject().getJupiter().split(",");
        x1 +=  _hDifference;
        printAstakvarga(plnNum,x1);

        // FOR VENUS
        plnNum=CGlobal.getCGlobalObject().getOutAstakvargaTableObject().getVenus().split(",");
        x1 +=  _hDifference;
        printAstakvarga(plnNum,x1);


        // FOR SAT
        plnNum=CGlobal.getCGlobalObject().getOutAstakvargaTableObject().getSat().split(",");
        x1 +=  _hDifference;
        printAstakvarga(plnNum,x1);


        // FOR TOTAL
        plnNum=CGlobal.getCGlobalObject().getOutAstakvargaTableObject().getTotal().split(",");
        x1 +=  _hDifference;
        printAstakvarga(plnNum, x1);

        CGenerateAppViews.setParamsOfViewAstakVarga(yHeight);

    }

    private void printAstakvarga(String [] plnnum,int xCoordinate)
    {
        yHeight = ((int) SCREEN_CONSTANTS.DeviceScreenHeight) / 18;
        yHeight = yHeight + CScreenConstants.pxToDp(CScreenConstants.firstLineTopMargin,getContext());
        for(int i=0;i<12;i++) {
            _canvas.drawText(plnnum[i], xCoordinate, yHeight, SCREEN_CONSTANTS.contentPaint);
            yHeight = yHeight + CScreenConstants.pxToDp(CScreenConstants.margininline,getContext());
        }

    }


    public void drawHeading() {
        int x1=x;
        yHeight = ((int) SCREEN_CONSTANTS.DeviceScreenHeight) / 18;
        //draw top heading
        for (int i = 0; i < _totalLines; i++) {
            _canvas.drawText(heading[i], x1, yHeight, SCREEN_CONSTANTS.headingPaint);
            x1 +=  _hDifference;
        }
        // draw side heading
        yHeight = yHeight + CScreenConstants.pxToDp(CScreenConstants.firstLineTopMargin,getContext());
        for(int j=0;j<12;j++) {
            if((j%2==0)) {
                if (this.SCREEN_CONSTANTS.NewDeviceScreenWidth < CGlobalVariables.SMALL_DEVICE_WIDTH) {
                    _canvas.drawRect(3, yHeight-CScreenConstants.pxToDp(CScreenConstants.margininline-CScreenConstants.rectHieghtMarginTop,getContext()), this.SCREEN_CONSTANTS.NewDeviceScreenWidth, yHeight+CScreenConstants.pxToDp(-CScreenConstants.rectHieghtMarginBottom,getContext()), SCREEN_CONSTANTS.TableRow_Background_Color);
                } else {
                    _canvas.drawRect(3, yHeight-CScreenConstants.pxToDp(CScreenConstants.margininline-CScreenConstants.rectHieghtMarginTop,getContext()), this.SCREEN_CONSTANTS.NewDeviceScreenWidth, yHeight+CScreenConstants.pxToDp(CScreenConstants.rectHieghtMarginBottom,getContext()), SCREEN_CONSTANTS.TableRow_Background_Color);
                }
            }
            _canvas.drawText(String.valueOf(j + 1), x, yHeight, SCREEN_CONSTANTS.contentPaint);
            yHeight = yHeight + CScreenConstants.pxToDp(CScreenConstants.margininline,getContext());
        }

    }

}