package com.ojassoft.astrosage.ui.customviews.basic;


import java.util.Calendar;

import android.content.Context;
import android.graphics.Canvas;
import android.view.View;

import com.ojassoft.astrosage.beans.OutPanchang;
import com.ojassoft.astrosage.misc.CGenerateAppViews;
import com.ojassoft.astrosage.utils.CGlobal;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CScreenConstants;
import com.ojassoft.astrosage.utils.CUtils;

public class ViewPanchang extends View {

    private OutPanchang _outPanchang = null;
    private Canvas _canvas = null;
    private String[] heading;
    private String[] NewWeekDaysInEnglish;
    private String[] HinduWeekDays;
    int languageCode;
    private int _totalLines;
    private Context _context;
    CScreenConstants SCREEN_CONSTANTS;
    private int screenWidth = 0;
    private int y = 30;
    private int hSpace = 40;
    private int x = 10;
    String[] nakName = null;
    float topHeadingY;
    private String topHeading;

    private String pakshaName;
    private String TithiName;
    private String nakshatraName;
    private String yogName;
    private String karanName;
    int y1=0;

    public ViewPanchang(Context context, OutPanchang outPanchang, String[] heading, String[] NewWeekDaysInEnglish, String[] HinduWeekDays,
                        int languageCode, CScreenConstants SCREEN_CONSTANTS, String[] nakName, String topHeading, String pakshaName,
                        String TithiName,
                        String nakshatraName,
                        String yogName,
                        String karanName) {
        super(context);
        _context = context;
        _outPanchang = outPanchang;
        this.heading = heading;
        this.NewWeekDaysInEnglish = NewWeekDaysInEnglish;
        this.HinduWeekDays = HinduWeekDays;
        this.languageCode = languageCode;
        this.SCREEN_CONSTANTS = SCREEN_CONSTANTS;
        this.nakName = nakName;
        /*screenWidth = ((int) SCREEN_CONSTANTS.DeviceScreenWidth) / 3;*/ //DISABLED BY SHELENDRA ON 07-07-15
        screenWidth = ((int) SCREEN_CONSTANTS.DeviceScreenWidth) / 2;//ADDED BY SHELENDRA ON 07-07-15
        _totalLines = heading.length;
        calculateNakshatraIndexAndCharan();
        topHeadingY = SCREEN_CONSTANTS.DeviceScreenHeight / 17;
        this.topHeading = topHeading;

        this.pakshaName = pakshaName;
        this.TithiName = TithiName;
        this.nakshatraName = nakshatraName;
        this.yogName = yogName;
        this.karanName = karanName;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        _canvas = canvas;
        printTopHeading();
        drawHeading();
        drawView();
    }

    private void calculateNakshatraIndexAndCharan() {
        _outPanchang.setNakshatraIndex(CUtils.getNakshatraNumber(CGlobal.getCGlobalObject().getPlanetDataObject().getMoon()));
        _outPanchang.setNakshatraCharan(CUtils.getPlntCharan(CGlobal.getCGlobalObject().getPlanetDataObject().getMoon()));
        CGlobal.getCGlobalObject().getOutPanchangObject().setNakshatraIndex(CUtils.getNakshatraNumber(CGlobal.getCGlobalObject().getPlanetDataObject().getMoon()));
        CGlobal.getCGlobalObject().getOutPanchangObject().setNakshatraCharan(CUtils.getPlntCharan(CGlobal.getCGlobalObject().getPlanetDataObject().getMoon()));
    }


    public void drawView() {

        y1 = ((int) SCREEN_CONSTANTS.DeviceScreenHeight) / 18;
        y1 =y1+ CScreenConstants.pxToDp(CScreenConstants.firstLineTopMargin,getContext());

        for (int i = 0; i < _totalLines; i++) {

            switch (i) {
                case 0:
                    _canvas.drawText(pakshaName, screenWidth + hSpace ,   y1, SCREEN_CONSTANTS.contentPaint);
                    break;
                case 1:
                    _canvas.drawText(TithiName, screenWidth + hSpace ,   y1, SCREEN_CONSTANTS.contentPaint);
                    break;
                case 2:
                    _canvas.drawText(nakName[CGlobal.getCGlobalObject().getOutPanchangObject().getNakshatraIndex()] + nakshatraName, screenWidth
                            + hSpace ,   y1, SCREEN_CONSTANTS.contentPaint);
                    break;
                case 3:
                    String str;
                    str = HinduWeekDays[Integer.valueOf(CGlobal.getCGlobalObject().getOutPanchangObject().get_HinduWeekDay())];
                    if (languageCode == CGlobalVariables.ENGLISH)
                        _canvas.drawText(CUtils.makeFirstLatterInCapitalInString(str, languageCode), screenWidth
                                + hSpace ,   y1, SCREEN_CONSTANTS.contentPaint);
                    if (languageCode == CGlobalVariables.HINDI || languageCode == CGlobalVariables.TAMIL || languageCode == CGlobalVariables.BANGALI || languageCode == CGlobalVariables.MARATHI|| languageCode == CGlobalVariables.KANNADA|| languageCode == CGlobalVariables.TELUGU|| languageCode == CGlobalVariables.GUJARATI|| languageCode == CGlobalVariables.MALAYALAM)
                        _canvas.drawText(str, screenWidth + hSpace ,   y1, SCREEN_CONSTANTS.contentPaint);

                    break;
                case 4:
                    String strEng;
                    strEng = NewWeekDaysInEnglish[getEnglishWeekDay()];
                    _canvas.drawText(strEng, screenWidth
                            + hSpace ,   y1, SCREEN_CONSTANTS.contentPaint);

                    break;
                case 5:
                    _canvas.drawText(yogName, screenWidth + hSpace ,   y1, SCREEN_CONSTANTS.contentPaint);
                    break;
                case 6:
                    _canvas.drawText(karanName, screenWidth + hSpace ,   y1, SCREEN_CONSTANTS.contentPaint);
                    break;
                case 7:
                    String sunRise = CGlobal.getCGlobalObject().getOutPanchangObject().get_SunRise();
                    if (languageCode == CGlobalVariables.HINDI)
                        sunRise = sunRise.replace(".", "-");
                    _canvas.drawText(/*CGlobal.getCGlobalObject().getOutPanchangObject().get_SunRise()*/sunRise, screenWidth
                            + hSpace ,   y1, /*SCREEN_CONSTANTS.TextColorEnglish*/SCREEN_CONSTANTS.contentPaint);
                    break;
                case 8:
                    String sunSet = CGlobal.getCGlobalObject().getOutPanchangObject().get_SunSet();
                    if (languageCode == CGlobalVariables.HINDI)
                        sunSet = sunSet.replace(".", "-");
                    _canvas.drawText(/*CGlobal.getCGlobalObject().getOutPanchangObject().get_SunSet()*/sunSet,
                            screenWidth + hSpace ,   y1, /*SCREEN_CONSTANTS.TextColorEnglish */SCREEN_CONSTANTS.contentPaint);
                    break;

            }
            y1 =  y1+ CScreenConstants.pxToDp(CScreenConstants.margininline,getContext());
        }
        CGenerateAppViews.setHeightParamPanchangDetail(y1);
 }


    public void drawHeading() {
        y1 = ((int) SCREEN_CONSTANTS.DeviceScreenHeight) / 18;
        y1 =y1+ CScreenConstants.pxToDp(CScreenConstants.firstLineTopMargin,getContext());
        for (int i = 0; i < _totalLines; i++) {
            if((i%2==0)) {
                if (this.SCREEN_CONSTANTS.NewDeviceScreenWidth < CGlobalVariables.SMALL_DEVICE_WIDTH) {
                    _canvas.drawRect(3, y1-CScreenConstants.pxToDp(CScreenConstants.margininline-CScreenConstants.rectHieghtMarginTop,getContext()), this.SCREEN_CONSTANTS.NewDeviceScreenWidth, y1+CScreenConstants.pxToDp(-CScreenConstants.rectHieghtMarginBottom,getContext()), SCREEN_CONSTANTS.TableRow_Background_Color);
                } else {
                    _canvas.drawRect(3, y1-CScreenConstants.pxToDp(CScreenConstants.margininline-CScreenConstants.rectHieghtMarginTop,getContext()), this.SCREEN_CONSTANTS.NewDeviceScreenWidth, y1+CScreenConstants.pxToDp(CScreenConstants.rectHieghtMarginBottom,getContext()), SCREEN_CONSTANTS.TableRow_Background_Color);
                }
            }
            _canvas.drawText(heading[i], x+CScreenConstants.textPaddingLeft,   y1, SCREEN_CONSTANTS.contentPaint);
            y1 =  y1+ CScreenConstants.pxToDp(CScreenConstants.margininline,getContext());
        }

    }

    /**
     * This function is used to calculate english week day
     *
     * @return int
     * @author Bijendra(7-sep-13)
     */
    private int getEnglishWeekDay() {
        int engWeekDay = 0;
        Calendar c = Calendar.getInstance();

        c.set(CGlobal.getCGlobalObject().getHoroPersonalInfoObject().getDateTime().getYear()
                , CGlobal.getCGlobalObject().getHoroPersonalInfoObject().getDateTime().getMonth(),
                CGlobal.getCGlobalObject().getHoroPersonalInfoObject().getDateTime().getDay(),
                CGlobal.getCGlobalObject().getHoroPersonalInfoObject().getDateTime().getHour(),
                CGlobal.getCGlobalObject().getHoroPersonalInfoObject().getDateTime().getMin());

        engWeekDay = c.get(Calendar.DAY_OF_WEEK);
        engWeekDay -= 1;

        if (engWeekDay < 0)
            engWeekDay = 0;
        return engWeekDay;
    }

    private void printTopHeading() {
        x = 10;
        y1 = ((int) SCREEN_CONSTANTS.DeviceScreenHeight) / 18;
        int xPos = (int) ((_canvas.getWidth() / 2));
        _canvas.drawText(topHeading,x+CScreenConstants.textPaddingLeft, topHeadingY, SCREEN_CONSTANTS.headingPaint);
        y1 =y1+ CScreenConstants.pxToDp(CScreenConstants.firstLineTopMargin,getContext());
    }
}
