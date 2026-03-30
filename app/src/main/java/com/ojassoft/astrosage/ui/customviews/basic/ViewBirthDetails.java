package com.ojassoft.astrosage.ui.customviews.basic;


import java.util.Calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;
import android.widget.Toast;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.OutBirthDetail;
import com.ojassoft.astrosage.controller.ControllerManager;
import com.ojassoft.astrosage.misc.CGenerateAppViews;
import com.ojassoft.astrosage.utils.CGlobal;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CScreenConstants;
import com.ojassoft.astrosage.utils.CUtils;

public class ViewBirthDetails extends View {
    private String[] headingBirthDetails;
    private Canvas _canvas = null;
    private OutBirthDetail _outBirthDetail;
    int languageCode;
    String degSign = "";
    String minSign = "";
    String secSign = "";
    CScreenConstants SCREEN_CONSTANTS;
    String[] shortYMDArray = null;

    String[] varMangalDosh = null;
    String[] arrayRashiName = null;
    String[] varVimDasaPlanets = null;
    String[] ayanArray = null;
    float topHeadingY;
    private String topHeading;
    int y1;
    int x = 10;
    private Context mContext;//ADDED BY BIJENDRA ON 17-04-15

    public ViewBirthDetails(Context context, OutBirthDetail outBirthDetail, String[] headingBirthDetails, int languageCode, String degSign, String minSign, String secSign,
                            CScreenConstants SCREEN_CONSTANTS, String[] shortYMDArray, String[] varMangalDosh, String[] arrayRashiName, String[] varVimDasaPlanets, String[] ayanArray, String topHeading) {
        super(context);
        _outBirthDetail = outBirthDetail;
        this.headingBirthDetails = headingBirthDetails;
        this.languageCode = languageCode;
        this.degSign = degSign;
        this.minSign = minSign;
        this.secSign = secSign;
        this.SCREEN_CONSTANTS = SCREEN_CONSTANTS;
        this.varMangalDosh = varMangalDosh;
        this.arrayRashiName = arrayRashiName;
        this.varVimDasaPlanets = varVimDasaPlanets;
        this.shortYMDArray = shortYMDArray;
        this.ayanArray = ayanArray;
        topHeadingY = SCREEN_CONSTANTS.DeviceScreenHeight / 17;
        this.topHeading = topHeading;
        this.mContext = context;//ADDED BY BIJENDRA ON 17-04-15
    }

    @Override
    protected void onDraw(Canvas canvas) {
        _canvas = canvas;
        printBirthDetail();
    }


    private String getFormattedAyan() {
        String _ayan = ayanArray[Integer.parseInt(_outBirthDetail.getAyanType())] + CGlobalVariables._unicodeSpace + CUtils.getLeftBracketSign(languageCode) + CUtils.FormatDMSInStringWithSign(CGlobal.getCGlobalObject().getPlanetDataObject().getAyan(), degSign, minSign, secSign) + CUtils.getRightBracketSign(languageCode);
        return _ayan;
    }

    private void printBirthDetail() {

        printTopHeading();

        int _totalLines = headingBirthDetails.length;
        //int screenWidth = ((int) SCREEN_CONSTANTS.DeviceScreenWidth) / 4;//DISABLED BY SHELENDRA ON 07-07-15
        int screenWidth = (int) (((double) SCREEN_CONSTANTS.DeviceScreenWidth) / 2.4);//ADDED BY SHELENDRA ON 07-07-15

        int y = 24;
        int hSpace = 15;

        int iColourIndex = -1;

        int day = 0;
        int month = 0;
        int year = 0;


        String[] s = _outBirthDetail.getDateOfBirth().split("/");

        Calendar birthDate = Calendar.getInstance();
        int cDay = birthDate.get(Calendar.DAY_OF_MONTH);
        int cMonth = birthDate.get(Calendar.MONTH) + 1;
        int cYear = birthDate.get(Calendar.YEAR);

        int bDay = Integer.valueOf(s[0]);
        int bMonth = Integer.valueOf(s[1]);
        int bYear = Integer.valueOf(s[2]);

        double calDateDiff = CUtils.getDateDifferencInDays(bYear, bMonth, bDay, cYear, cMonth, cDay);
        try {
            if (calDateDiff > -1) {
                int[] ymd = CUtils.getDateFromDoubleToYMD(calDateDiff);
                day = ymd[0];
                month = ymd[1];
                year = ymd[2];
            }
        } catch (Exception e) {

        }

        for (int i = 0; i < _totalLines; i++) {
            iColourIndex++;
            if (iColourIndex > 5)
                iColourIndex = 0;
            if((i%2==0)) {
                if (this.SCREEN_CONSTANTS.NewDeviceScreenWidth < CGlobalVariables.SMALL_DEVICE_WIDTH) {
                    _canvas.drawRect(3, y1-CScreenConstants.pxToDp(CScreenConstants.margininline-CScreenConstants.rectHieghtMarginTop,getContext()), this.SCREEN_CONSTANTS.NewDeviceScreenWidth, y1+CScreenConstants.pxToDp(-CScreenConstants.rectHieghtMarginBottom,getContext()), SCREEN_CONSTANTS.TableRow_Background_Color);
                } else {
                    _canvas.drawRect(3, y1-CScreenConstants.pxToDp(CScreenConstants.margininline-CScreenConstants.rectHieghtMarginTop,getContext()), this.SCREEN_CONSTANTS.NewDeviceScreenWidth, y1+CScreenConstants.pxToDp(CScreenConstants.rectHieghtMarginBottom,getContext()), SCREEN_CONSTANTS.TableRow_Background_Color);
                }
            }

            if(i<11)
                _canvas.drawText(headingBirthDetails[i], x,  y1,SCREEN_CONSTANTS.contentPaint);
            else if(Integer.valueOf(CGlobal.getCGlobalObject().getHoroPersonalInfoObject().getHoraryNumber()) > 0)
                _canvas.drawText(headingBirthDetails[i], x,  y1,SCREEN_CONSTANTS.contentPaint);

            switch (i) {
                case 0:
                    _canvas.drawText(_outBirthDetail.getName(), screenWidth + hSpace,  y1, SCREEN_CONSTANTS.contentPaintWithouttypeface);
                   // Toast.makeText(getContext(),"name:- "+_outBirthDetail.getName(),Toast.LENGTH_SHORT).show();
                    break;
                case 1:
                    _canvas.drawText(_outBirthDetail.getDateOfBirth(), screenWidth
                            + hSpace, y1, SCREEN_CONSTANTS.contentPaintWithouttypeface);
                    break;
                case 2:
                    _canvas.drawText(_outBirthDetail.getTimeOfBirth(), screenWidth
                            + hSpace, y1, SCREEN_CONSTANTS.contentPaintWithouttypeface);
                    break;
                case 3:
                    _canvas.drawText(_outBirthDetail.getPlace(), screenWidth
                            + hSpace, y1, SCREEN_CONSTANTS.contentPaintWithouttypeface);
                    break;
                case 4:
                    //DISABLED BY BIJENDRA ON 17-04-15
				/*_canvas.drawText(_outBirthDetail.getGender(), screenWidth
						+ hSpace, y1, SCREEN_CONSTANTS.TextColorEnglish);*/
                    //ADDED BY BIJENDRA ON 17-04-15
                    if(_outBirthDetail.getGender().equalsIgnoreCase("Male"))
                        _canvas.drawText(mContext.getResources().getString(R.string.male), screenWidth
                                + hSpace, y1, SCREEN_CONSTANTS.contentPaint);
                    else
                        _canvas.drawText(mContext.getResources().getString(R.string.female), screenWidth
                                + hSpace, y1, SCREEN_CONSTANTS.contentPaint);
                    //END
                    break;
                case 5:
                    _canvas.drawText(getFormattedAyan(), screenWidth
                            + hSpace,  y1, SCREEN_CONSTANTS.contentPaint);
                    break;
                case 6:
                    _canvas.drawText(_outBirthDetail.getDst(),
                            screenWidth + hSpace,  y1, SCREEN_CONSTANTS.contentPaint);
                    break;
                case 7:
                    _canvas.drawText(varMangalDosh[_outBirthDetail.getMangldoshPoint()],
                            screenWidth + hSpace,  y1, SCREEN_CONSTANTS.contentPaint);
                    break;
                case 8:
                    _canvas.drawText(arrayRashiName[_outBirthDetail.getRashi()-1],
                            screenWidth + hSpace,  y1, SCREEN_CONSTANTS.contentPaint);
                    break;
                case 9:
                    _canvas.drawText(year+" "+shortYMDArray[0]+" "+month+" "+shortYMDArray[1]+" "+day+" "+shortYMDArray[2],
                            screenWidth + hSpace,  y1, SCREEN_CONSTANTS.contentPaint);
                    break;
                case 10:
                    try
                    {
                        ControllerManager cm=new ControllerManager();
                        int planetNumber=cm.getPlanetForBalanceOfDasa(CGlobal.getCGlobalObject().getPlanetDataObject().getMoon());
                        double balDasa=cm.getBalanceOfDasa(CGlobal.getCGlobalObject().getPlanetDataObject().getMoon());
                        int[]balOfDasaymd=CUtils.getDateFromDoubleToYMD(balDasa);

                        String strPrintBalDasa=varVimDasaPlanets[planetNumber]+" "+balOfDasaymd[2]+" "+shortYMDArray[0]+" "+balOfDasaymd[1]+" "+shortYMDArray[1]+" "+balOfDasaymd[0]+" "+shortYMDArray[2];
                        _canvas.drawText(strPrintBalDasa,screenWidth + hSpace,  y1, SCREEN_CONSTANTS.contentPaint);
                    }
                    catch(Exception e)
                    {

                    }
                    break;
                case 11:
                    if(Integer.valueOf(CGlobal.getCGlobalObject().getHoroPersonalInfoObject().getHoraryNumber()) > 0)
                        _canvas.drawText(String.valueOf(CGlobal.getCGlobalObject().getHoroPersonalInfoObject().getHoraryNumber()),
                                screenWidth + hSpace,  y1, SCREEN_CONSTANTS.contentPaint);
                    break;
            }
            y1 =  y1+ CScreenConstants.pxToDp(CScreenConstants.margininline,getContext());
  }
        CGenerateAppViews.setHeightParamBirthDetail(y1);
    }

    private void printTopHeading() {
        x = 10;
        y1 = ((int) SCREEN_CONSTANTS.DeviceScreenHeight) / 18;
        int xPos = (int) ((_canvas.getWidth() / 2));
        _canvas.drawText(topHeading,x, topHeadingY, SCREEN_CONSTANTS.headingPaint);
        y1 =y1+ CScreenConstants.pxToDp(CScreenConstants.firstLineTopMargin,getContext());

    }
}
