package com.ojassoft.astrosage.ui.customviews.basic;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.view.View;

import com.ojassoft.astrosage.beans.KundliPlanetArray;
import com.ojassoft.astrosage.misc.CGenerateAppViews;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CScreenConstants;
import com.ojassoft.astrosage.utils.CUtils;

public class viewMiscDrawable extends View {

    private int startX = 10;

    private int xLength = 0;
    private Canvas _canvas = null;
    private KundliPlanetArray _obj = null;
    // START POSITION NORMAl
    private int[] arrPoNormalSign = new int[13];
    private String[] arrPoNormalDegree = new String[13];
    private int[] arrPoNormalNaks = new int[13];
    private int[] arrPoNormalC = new int[13];
    private float[] xPoNormal;
    int bottomY;
    int topY;
    // END POSITION NORMAl
    // START POSITION SUB

    private String[] arrPoSubNaks = new String[13];

    private float[] xPoSub;

    // END POSITION SUB

    private float yAvasthaHeading = 15;

    int y1 = 0;
    int _index = 0;
    CScreenConstants SCREEN_CONSTANTS;
    String[] _arrDirect = new String[12];
    String[] _arrCombust = new String[11];
    int languageCode = CGlobalVariables.ENGLISH;
    String[] planetLagna = null;
    String[] rasi = null;
    String[] nakName = null;
    String[] rasiLord = null;
    String[] nakLord = null;
    String degSign = "";
    String minSign = "";
    String secSign = "";
    String[] strNormalHeading = null;
    String[] strSubHeading = null;
    String[] noteHeadings;

    public viewMiscDrawable(Context context, KundliPlanetArray objKundliPlanetArray, int index, CScreenConstants SCREEN_CONSTANTS, String[] arrDirect, String[] arrCombust, int languageCode, String[] planetLagna, String[] rasi, String[] nakName,
                            String[] nakLord, String[] rasiLord, String degSign, String minSign, String secSign, String[] strNormalHeading, String[] strSubHeading, String[] noteHeadings) {
        super(context);
        _obj = objKundliPlanetArray;
        _index = index;
        // y1 = ((int) SCREEN_CONSTANTS.DeviceScreenHeight) / 18;
//		y1 +=1;
        this.SCREEN_CONSTANTS = SCREEN_CONSTANTS;


        xLength = ((int) SCREEN_CONSTANTS.DeviceScreenWidth) / 4;
        initVariables();
        _arrDirect = arrDirect;
        _arrCombust = arrCombust;
        this.planetLagna = planetLagna;
        this.rasi = rasi;
        this.nakName = nakName;
        this.rasiLord = rasiLord;
        this.nakLord = nakLord;
        this.degSign = degSign;
        this.minSign = minSign;
        this.secSign = secSign;
        this.strNormalHeading = strNormalHeading;
        this.strSubHeading = strSubHeading;
        this.languageCode = languageCode;
        this.noteHeadings = noteHeadings;
    }

    private void initVariables() {
        xPoNormal = new float[]{10, (startX + (0.4f) * xLength),
                startX + (0.9f) * xLength,// 2
                startX + (1.5f) * xLength,// 3
                startX + (2.7f) * xLength,// 4
                startX + (3.6f) * xLength,// 5
                startX + (3.4f) * xLength};// 6

        xPoSub = new float[]{10,// 0
                (startX + (.4f) * xLength),// 1
                (startX + (0.9f) * xLength),// 2
                startX + (2.3f) * xLength,// 3
                startX + (2.7f) * xLength,// 4
                startX + (3.1f) * xLength,// 5
                startX + (3.5f) * xLength,// 6
                startX + (3.9f) * xLength};// 7
    }

    @Override
    protected void onDraw(Canvas canvas) {
        _canvas = canvas;

        switch (_index) {
            case 0:
                ShowPositionNormalHeading();

                break;
            case 1:
                ShoawPositionSubHeading();
                break;
            default:
                break;

        }

    }

    private void DrawPositionSub() {
        String[] naks = null;
        String _directOrRect = "";

        float y = yAvasthaHeading;
        CalculatePositionSub();



        for (int i = 0; i < planetLagna.length - 1; i++) {
            naks = arrPoSubNaks[i].split("-");
            if((i%2==0)) {
                if (this.SCREEN_CONSTANTS.NewDeviceScreenWidth < CGlobalVariables.SMALL_DEVICE_WIDTH) {
                    _canvas.drawRect(3, y1-CScreenConstants.pxToDp(CScreenConstants.margininline-CScreenConstants.rectHieghtMarginTop,getContext()), this.SCREEN_CONSTANTS.NewDeviceScreenWidth, y1+CScreenConstants.pxToDp(-CScreenConstants.rectHieghtMarginBottom,getContext()), SCREEN_CONSTANTS.TableRow_Background_Color);
                } else {
                    _canvas.drawRect(3, y1-CScreenConstants.pxToDp(CScreenConstants.margininline-CScreenConstants.rectHieghtMarginTop,getContext()), this.SCREEN_CONSTANTS.NewDeviceScreenWidth, y1+CScreenConstants.pxToDp(CScreenConstants.rectHieghtMarginBottom,getContext()), SCREEN_CONSTANTS.TableRow_Background_Color);
                }
            }
            if (i == 0) {
                _canvas.drawText(planetLagna[i], xPoSub[0], y1, SCREEN_CONSTANTS.contentPaint);
                _canvas.drawText(arrPoNormalDegree[i], xPoSub[2],y1,SCREEN_CONSTANTS.contentPaint);
                _canvas.drawText(naks[0], xPoSub[3],y1,SCREEN_CONSTANTS.contentPaint);
                _canvas.drawText(naks[1], xPoSub[4],y1,SCREEN_CONSTANTS.contentPaint);
                _canvas.drawText(naks[2], xPoSub[5],y1,SCREEN_CONSTANTS.contentPaint);
                _canvas.drawText(naks[3], xPoSub[6],y1,SCREEN_CONSTANTS.contentPaint);
            }
            else {


              /*  if (!(i % 2 == 0)) { if (this.SCREEN_CONSTANTS.NewDeviceScreenWidth < CGlobalVariables.SMALL_DEVICE_WIDTH) {
                    _canvas.drawRect(3, (2 + i) * y1 - CGlobalVariables.SMALL_DEVICE_TOP, this.SCREEN_CONSTANTS.NewDeviceScreenWidth, (2 + i) * y1 + CGlobalVariables.SMALL_DEVICE_BOTTOM, SCREEN_CONSTANTS.TableRow_Background_Color);
                } else {
                    bottomY = topY + CScreenConstants.rectHieght;
                    _canvas.drawRect(3, topY, this.SCREEN_CONSTANTS.NewDeviceScreenWidth, bottomY, SCREEN_CONSTANTS.TableRow_Background_Color);
                }
                } else {
                    bottomY = topY + CScreenConstants.rectHieght;
                    _canvas.drawRect(3, topY, this.SCREEN_CONSTANTS.NewDeviceScreenWidth, bottomY, SCREEN_CONSTANTS.TableHeading_Background_Color);
                }*/


                _directOrRect = "";
                _directOrRect = _arrDirect[i - 1];
                if (i != 1) {
                    if (_arrCombust[i - 2].trim().length() > 0)
                        _directOrRect += _arrCombust[i - 2];  //
                }
                if (_directOrRect.trim().length() > 0) {
                    // FORMAT
                    _directOrRect = CUtils.getLeftBracketSign(languageCode) + _directOrRect.trim() + CUtils.getRightBracketSign(languageCode);
                    // END
                }
                // FORMAT




                _canvas.drawText(planetLagna[i], xPoSub[0],y1, SCREEN_CONSTANTS.contentPaint);
                _canvas.drawText(_directOrRect, xPoSub[1],y1,SCREEN_CONSTANTS.contentPaint);
                _canvas.drawText(arrPoNormalDegree[i], xPoSub[2], y1, SCREEN_CONSTANTS.contentPaint);

                _canvas.drawText(naks[0], xPoSub[3], y1, SCREEN_CONSTANTS.contentPaint);
                _canvas.drawText(naks[1], xPoSub[4], y1, SCREEN_CONSTANTS.contentPaint);
                _canvas.drawText(naks[2], xPoSub[5], y1, SCREEN_CONSTANTS.contentPaint);
                _canvas.drawText(naks[3], xPoSub[6], y1, SCREEN_CONSTANTS.contentPaint);
            }
            y1 =  y1+ CScreenConstants.pxToDp(CScreenConstants.margininline,getContext());
        }

        _canvas.drawText(noteHeadings[0], 10,y1, SCREEN_CONSTANTS.headingPaint);
        y1=y1+ CScreenConstants.pxToDp(CScreenConstants.noteTopMarign,getContext());
        _canvas.drawText(noteHeadings[1], startX + CScreenConstants.notetextLeftMargin,  y1 , SCREEN_CONSTANTS.notePaint);
        y1=y1+CScreenConstants.pxToDp(CScreenConstants.noteLineMargin,getContext());
        _canvas.drawText(noteHeadings[2], startX +CScreenConstants.notetextLeftMargin,y1, SCREEN_CONSTANTS.notePaint);
        y1=y1+CScreenConstants.pxToDp(CScreenConstants.noteLineMargin,getContext());
        _canvas.drawText(noteHeadings[3], startX + CScreenConstants.notetextLeftMargin,y1, SCREEN_CONSTANTS.notePaint);
        y1=y1+CScreenConstants.pxToDp(CScreenConstants.noteLineMargin,getContext());
        _canvas.drawText(noteHeadings[4], startX + CScreenConstants.notetextLeftMargin,y1, SCREEN_CONSTANTS.notePaint);
        y1=y1+CScreenConstants.pxToDp(CScreenConstants.noteLineMargin,getContext());
        CGenerateAppViews.setParamsOfViewMiscDrawableSubPlanet(y1);

    }

    public static int pxToDp(int px)
    {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    private void CalculatePositionSub() {

        arrPoNormalDegree[0] = CUtils.FormatDMSInStringWithSign(_obj.getLagna().getPlanetDeg(), degSign, minSign, secSign);
        arrPoNormalDegree[1] = CUtils.FormatDMSInStringWithSign(_obj.getSun().getPlanetDeg(), degSign, minSign, secSign);
        arrPoNormalDegree[2] = CUtils.FormatDMSInStringWithSign(_obj.getMoon().getPlanetDeg(), degSign, minSign, secSign);
        arrPoNormalDegree[3] = CUtils.FormatDMSInStringWithSign(_obj.getMarsh().getPlanetDeg(), degSign, minSign, secSign);
        arrPoNormalDegree[4] = CUtils.FormatDMSInStringWithSign(_obj.getMercury().getPlanetDeg(), degSign, minSign, secSign);
        arrPoNormalDegree[5] = CUtils.FormatDMSInStringWithSign(_obj.getJupiter().getPlanetDeg(), degSign, minSign, secSign);
        arrPoNormalDegree[6] = CUtils.FormatDMSInStringWithSign(_obj.getVenus().getPlanetDeg(), degSign, minSign, secSign);
        arrPoNormalDegree[7] = CUtils.FormatDMSInStringWithSign(_obj.getSaturn().getPlanetDeg(), degSign, minSign, secSign);
        arrPoNormalDegree[8] = CUtils.FormatDMSInStringWithSign(_obj.getRahu().getPlanetDeg(), degSign, minSign, secSign);
        arrPoNormalDegree[9] = CUtils.FormatDMSInStringWithSign(_obj.getKetu().getPlanetDeg(), degSign, minSign, secSign);
        arrPoNormalDegree[10] = CUtils.FormatDMSInStringWithSign(_obj.getUranus().getPlanetDeg(), degSign, minSign, secSign);
        arrPoNormalDegree[11] = CUtils.FormatDMSInStringWithSign(_obj.getNeptune().getPlanetDeg(), degSign, minSign, secSign);
        arrPoNormalDegree[12] = CUtils.FormatDMSInStringWithSign(_obj.getPluto().getPlanetDeg(), degSign, minSign, secSign);

        arrPoSubNaks[0] = CUtils.getRasiNakSubSub(_obj.getLagna().getPlanetDeg(), rasiLord, nakLord);
        arrPoSubNaks[1] = CUtils.getRasiNakSubSub(_obj.getSun().getPlanetDeg(), rasiLord, nakLord);
        arrPoSubNaks[2] = CUtils.getRasiNakSubSub(_obj.getMoon().getPlanetDeg(), rasiLord, nakLord);
        arrPoSubNaks[3] = CUtils.getRasiNakSubSub(_obj.getMarsh().getPlanetDeg(), rasiLord, nakLord);
        arrPoSubNaks[4] = CUtils.getRasiNakSubSub(_obj.getMercury().getPlanetDeg(), rasiLord, nakLord);
        arrPoSubNaks[5] = CUtils.getRasiNakSubSub(_obj.getJupiter().getPlanetDeg(), rasiLord, nakLord);
        arrPoSubNaks[6] = CUtils.getRasiNakSubSub(_obj.getVenus().getPlanetDeg(), rasiLord, nakLord);
        arrPoSubNaks[7] = CUtils.getRasiNakSubSub(_obj.getSaturn().getPlanetDeg(), rasiLord, nakLord);
        arrPoSubNaks[8] = CUtils.getRasiNakSubSub(_obj.getRahu().getPlanetDeg(), rasiLord, nakLord);
        arrPoSubNaks[9] = CUtils.getRasiNakSubSub(_obj.getKetu().getPlanetDeg(), rasiLord, nakLord);
        arrPoSubNaks[10] = CUtils.getRasiNakSubSub(_obj.getUranus().getPlanetDeg(), rasiLord, nakLord);
        arrPoSubNaks[11] = CUtils.getRasiNakSubSub(_obj.getNeptune().getPlanetDeg(), rasiLord, nakLord);
        arrPoSubNaks[12] = CUtils.getRasiNakSubSub(_obj.getPluto().getPlanetDeg(), rasiLord, nakLord);
    }

    private void ShoawPositionSubHeading() {
       /* if (this.SCREEN_CONSTANTS.NewDeviceScreenWidth < CGlobalVariables.SMALL_DEVICE_WIDTH) {
            _canvas.drawRect(3, y1 - CGlobalVariables.SMALL_DEVICE_TOP, this.SCREEN_CONSTANTS.NewDeviceScreenWidth, y1 + CGlobalVariables.SMALL_DEVICE_BOTTOM, SCREEN_CONSTANTS.TableHeading_Background_Color);
           // _canvas.drawLine(3, y1 + CGlobalVariables.SMALL_DEVICE_BOTTOM, SCREEN_CONSTANTS.NewDeviceScreenWidth, y1 + CGlobalVariables.SMALL_DEVICE_BOTTOM, SCREEN_CONSTANTS.KundliLineColor);
        } else {
            _canvas.drawRect(3, y1 - CGlobalVariables.TOP, this.SCREEN_CONSTANTS.NewDeviceScreenWidth, y1 + CGlobalVariables.BOTTOM, SCREEN_CONSTANTS.TableHeading_Background_Color);
           // _canvas.drawLine(3, y1 + CGlobalVariables.BOTTOM, SCREEN_CONSTANTS.NewDeviceScreenWidth, y1 + CGlobalVariables.BOTTOM, SCREEN_CONSTANTS.KundliLineColor);
        }*/
        xLength = ((int) SCREEN_CONSTANTS.DeviceScreenWidth) / 4;
        y1 = ((int) SCREEN_CONSTANTS.DeviceScreenHeight) / 18;
        int j = 0;
        for (int i = 0; i < strSubHeading.length - 1; i++) {
            if (i > 0)
                j = i + 1;
            _canvas.drawText(strSubHeading[i], xPoSub[j], y1, SCREEN_CONSTANTS.headingPaint);
        }
        y1 =y1+ CScreenConstants.pxToDp(CScreenConstants.firstLineTopMargin,getContext());
        DrawPositionSub();
    }

    private void DrawPositionNormal() {

        String _directOrRect = "", _nakCharan = "";

        CalculatePositionNormal();

        for (int i = 0; i < planetLagna.length - 1; i++) {

            _nakCharan = String.valueOf(arrPoNormalC[i]);
            _nakCharan = CUtils.getLeftBracketSign(languageCode) + _nakCharan + CUtils.getRightBracketSign(languageCode);


            if((i%2==0)) {
                if (this.SCREEN_CONSTANTS.NewDeviceScreenWidth < CGlobalVariables.SMALL_DEVICE_WIDTH) {
                    _canvas.drawRect(3, y1-CScreenConstants.pxToDp(CScreenConstants.margininline-CScreenConstants.rectHieghtMarginTop,getContext()), this.SCREEN_CONSTANTS.NewDeviceScreenWidth, y1+CScreenConstants.pxToDp(-CScreenConstants.rectHieghtMarginBottom,getContext()), SCREEN_CONSTANTS.TableRow_Background_Color);
                } else {
                    _canvas.drawRect(3, y1-CScreenConstants.pxToDp(CScreenConstants.margininline-CScreenConstants.rectHieghtMarginTop,getContext()), this.SCREEN_CONSTANTS.NewDeviceScreenWidth, y1+CScreenConstants.pxToDp(CScreenConstants.rectHieghtMarginBottom,getContext()), SCREEN_CONSTANTS.TableRow_Background_Color);
                }
            }


            if (i == 0) {

                _canvas.drawText(planetLagna[i], xPoNormal[0]
                        , y1, SCREEN_CONSTANTS.contentPaint);
                _canvas.drawText(rasi[arrPoNormalSign[i]], xPoNormal[2],  y1, SCREEN_CONSTANTS.contentPaint);
                _canvas.drawText(arrPoNormalDegree[i], xPoNormal[3], y1, SCREEN_CONSTANTS.contentPaint);
                _canvas.drawText(nakName[arrPoNormalNaks[i]], xPoNormal[4],  y1,
                        SCREEN_CONSTANTS.contentPaint);
                _canvas.drawText(_nakCharan, xPoNormal[5],  y1, SCREEN_CONSTANTS.contentPaint);
            } else {
                _directOrRect = _arrDirect[i - 1];
                if (i != 1) {
                    if (_arrCombust[i - 2].trim().length() > 0)
                        _directOrRect += _arrCombust[i - 2];
                }

                if (_directOrRect.trim().length() > 0) {
                    // FORMAT
                    _directOrRect = CUtils.getLeftBracketSign(languageCode) + _directOrRect.trim()
                            + CUtils.getRightBracketSign(languageCode);
                }
                _canvas.drawText(planetLagna[i], xPoNormal[0]  , y1, SCREEN_CONSTANTS.contentPaint);
                _canvas.drawText(_directOrRect, xPoNormal[1]  , y1, SCREEN_CONSTANTS.contentPaint);
                _canvas.drawText(rasi[arrPoNormalSign[i]], xPoNormal[2]  , y1, SCREEN_CONSTANTS.contentPaint);
                _canvas.drawText(arrPoNormalDegree[i], xPoNormal[3]  , y1, SCREEN_CONSTANTS.contentPaint);
                _canvas.drawText(nakName[arrPoNormalNaks[i]], xPoNormal[4]  , y1, SCREEN_CONSTANTS.contentPaint);
                _canvas.drawText(_nakCharan, xPoNormal[5]  , y1, SCREEN_CONSTANTS.contentPaint);
            }
            y1 =  y1+ CScreenConstants.pxToDp(CScreenConstants.margininline,getContext());;
        }
        CGenerateAppViews.setParamsOfViewMiscDrawable(y1);

    }

    private void CalculatePositionNormal() {

        arrPoNormalDegree[0] = CUtils.FormatDMSIn2DigitStringWithSign(_obj.getLagna().getPlanetDeg() % 30, languageCode);
        arrPoNormalDegree[1] = CUtils.FormatDMSIn2DigitStringWithSign(_obj.getSun().getPlanetDeg() % 30, languageCode);
        arrPoNormalDegree[2] = CUtils.FormatDMSIn2DigitStringWithSign(_obj.getMoon().getPlanetDeg() % 30, languageCode);
        arrPoNormalDegree[3] = CUtils.FormatDMSIn2DigitStringWithSign(_obj.getMarsh().getPlanetDeg() % 30, languageCode);
        arrPoNormalDegree[4] = CUtils.FormatDMSIn2DigitStringWithSign(_obj.getMercury().getPlanetDeg() % 30, languageCode);
        arrPoNormalDegree[5] = CUtils.FormatDMSIn2DigitStringWithSign(_obj.getJupiter().getPlanetDeg() % 30, languageCode);
        arrPoNormalDegree[6] = CUtils.FormatDMSIn2DigitStringWithSign(_obj.getVenus().getPlanetDeg() % 30, languageCode);
        arrPoNormalDegree[7] = CUtils.FormatDMSIn2DigitStringWithSign(_obj.getSaturn().getPlanetDeg() % 30, languageCode);
        arrPoNormalDegree[8] = CUtils.FormatDMSIn2DigitStringWithSign(_obj.getRahu().getPlanetDeg() % 30, languageCode);
        arrPoNormalDegree[9] = CUtils.FormatDMSIn2DigitStringWithSign(_obj.getKetu().getPlanetDeg() % 30, languageCode);

        arrPoNormalDegree[10] = CUtils.FormatDMSIn2DigitStringWithSign(_obj.getUranus().getPlanetDeg() % 30, languageCode);
        arrPoNormalDegree[11] = CUtils.FormatDMSIn2DigitStringWithSign(_obj.getNeptune().getPlanetDeg() % 30, languageCode);
        arrPoNormalDegree[12] = CUtils.FormatDMSIn2DigitStringWithSign(_obj.getPluto().getPlanetDeg() % 30, languageCode);

        // CALCULATE RASI FOR EVERY PLANET
        arrPoNormalSign[0] = CUtils.getRasiNumber(_obj.getLagna().getPlanetDeg());
        arrPoNormalSign[1] = CUtils.getRasiNumber(_obj.getSun().getPlanetDeg());
        arrPoNormalSign[2] = CUtils.getRasiNumber(_obj.getMoon().getPlanetDeg());
        arrPoNormalSign[3] = CUtils.getRasiNumber(_obj.getMarsh().getPlanetDeg());
        arrPoNormalSign[4] = CUtils.getRasiNumber(_obj.getMercury().getPlanetDeg());
        arrPoNormalSign[5] = CUtils.getRasiNumber(_obj.getJupiter().getPlanetDeg());
        arrPoNormalSign[6] = CUtils.getRasiNumber(_obj.getVenus().getPlanetDeg());
        arrPoNormalSign[7] = CUtils.getRasiNumber(_obj.getSaturn().getPlanetDeg());
        arrPoNormalSign[8] = CUtils.getRasiNumber(_obj.getRahu().getPlanetDeg());
        arrPoNormalSign[9] = CUtils.getRasiNumber(_obj.getKetu().getPlanetDeg());
        arrPoNormalSign[10] = CUtils.getRasiNumber(_obj.getUranus().getPlanetDeg());
        arrPoNormalSign[11] = CUtils.getRasiNumber(_obj.getNeptune().getPlanetDeg());
        arrPoNormalSign[12] = CUtils.getRasiNumber(_obj.getPluto().getPlanetDeg());

        // CALCULATE NAK FOR EVERY PLANET
        arrPoNormalNaks[0] = CUtils.getNakshatraNumber(_obj.getLagna().getPlanetDeg());
        arrPoNormalNaks[1] = CUtils.getNakshatraNumber(_obj.getSun().getPlanetDeg());
        arrPoNormalNaks[2] = CUtils.getNakshatraNumber(_obj.getMoon().getPlanetDeg());
        arrPoNormalNaks[3] = CUtils.getNakshatraNumber(_obj.getMarsh().getPlanetDeg());
        arrPoNormalNaks[4] = CUtils.getNakshatraNumber(_obj.getMercury().getPlanetDeg());
        arrPoNormalNaks[5] = CUtils.getNakshatraNumber(_obj.getJupiter().getPlanetDeg());
        arrPoNormalNaks[6] = CUtils.getNakshatraNumber(_obj.getVenus().getPlanetDeg());
        arrPoNormalNaks[7] = CUtils.getNakshatraNumber(_obj.getSaturn().getPlanetDeg());
        arrPoNormalNaks[8] = CUtils.getNakshatraNumber(_obj.getRahu().getPlanetDeg());
        arrPoNormalNaks[9] = CUtils.getNakshatraNumber(_obj.getKetu().getPlanetDeg());
        arrPoNormalNaks[10] = CUtils.getNakshatraNumber(_obj.getUranus().getPlanetDeg());
        arrPoNormalNaks[11] = CUtils.getNakshatraNumber(_obj.getNeptune().getPlanetDeg());
        arrPoNormalNaks[12] = CUtils.getNakshatraNumber(_obj.getPluto().getPlanetDeg());

        // CALCULATE NAK CHARAN FOR EVERY PLANET
        arrPoNormalC[0] = CUtils.getPlntCharan(_obj.getLagna().getPlanetDeg());
        arrPoNormalC[1] = CUtils.getPlntCharan(_obj.getSun().getPlanetDeg());
        arrPoNormalC[2] = CUtils.getPlntCharan(_obj.getMoon().getPlanetDeg());
        arrPoNormalC[3] = CUtils.getPlntCharan(_obj.getMarsh().getPlanetDeg());
        arrPoNormalC[4] = CUtils.getPlntCharan(_obj.getMercury().getPlanetDeg());
        arrPoNormalC[5] = CUtils.getPlntCharan(_obj.getJupiter().getPlanetDeg());
        arrPoNormalC[6] = CUtils.getPlntCharan(_obj.getVenus().getPlanetDeg());
        arrPoNormalC[7] = CUtils.getPlntCharan(_obj.getSaturn().getPlanetDeg());
        arrPoNormalC[8] = CUtils.getPlntCharan(_obj.getRahu().getPlanetDeg());
        arrPoNormalC[9] = CUtils.getPlntCharan(_obj.getKetu().getPlanetDeg());
        arrPoNormalC[11] = CUtils.getPlntCharan(_obj.getNeptune().getPlanetDeg());
        arrPoNormalC[10] = CUtils.getPlntCharan(_obj.getUranus().getPlanetDeg());
        arrPoNormalC[12] = CUtils.getPlntCharan(_obj.getPluto().getPlanetDeg());

    }

    private void ShowPositionNormalHeading() {
        xLength = ((int) SCREEN_CONSTANTS.DeviceScreenWidth) / 4;
        y1 = ((int) SCREEN_CONSTANTS.DeviceScreenHeight) / 18;
        int i = 0, j = 0;
        for (i = 0; i < strNormalHeading.length; i++) {
            if (i > 0)
                j = i + 1;
            _canvas.drawText(strNormalHeading[i], xPoNormal[j], y1, SCREEN_CONSTANTS.headingPaint);

        }
        y1 =y1+ CScreenConstants.pxToDp(CScreenConstants.firstLineTopMargin,getContext());
        DrawPositionNormal();
    }

}
