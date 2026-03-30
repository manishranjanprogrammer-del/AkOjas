package com.ojassoft.astrosage.ui.customviews.kp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.view.View;

import com.ojassoft.astrosage.misc.CGenerateAppViews;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CScreenConstants;

public class ViewKpPlanetaryPositions extends View {

    private Canvas _canvas = null;
    private String[] _heading = null;
    private String[][] _kpPlanetPositions = null;

    private String[] _planetNames = null;
    private String[] _direct = null;
    private String[] _combust = null;


    int xLength = 0;
    int yHeight = 0;

    Context _context = null;
    private int startX = 10;
    float[] xcoor = {0, 0.9f, 2.2f, 2.7f, 3.2f, 3.6f};
    Typeface _typeface = null;
    String _leftBracket = "(";
    String _rightBracket = ")";
    CScreenConstants SCREEN_CONSTANTS;
    String[] noteHeadings;

    public ViewKpPlanetaryPositions(Context context,
                                    String[][] kpPlanetaryPositions, String[] Heading, String[] PlanetNames, String[] Direct, String[] Combust, Typeface typeface, String LeftBracket, String RightBracket, CScreenConstants SCREEN_CONSTANTS, String[] noteHeadings) {
        super(context);
        _context = context;
        _kpPlanetPositions = kpPlanetaryPositions;
        _heading = Heading;
        _planetNames = PlanetNames;
        _direct = Direct;
        _combust = Combust;
        _typeface = typeface;
        _leftBracket = LeftBracket;
        _rightBracket = RightBracket;
        this.SCREEN_CONSTANTS = SCREEN_CONSTANTS;

        this.noteHeadings = noteHeadings;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        _canvas = canvas;
        printHeading();

    }

    private void printHeading() {
        xLength = ((int) SCREEN_CONSTANTS.DeviceScreenWidth) / 4;
        yHeight =CScreenConstants.pxToDp(CScreenConstants.topMargin, _context);

        _canvas.drawText(_heading[0], startX + xcoor[0], yHeight, SCREEN_CONSTANTS.headingPaint);
        _canvas.drawText(_heading[1], (startX + xcoor[1] * xLength), yHeight, SCREEN_CONSTANTS.headingPaint);
        _canvas.drawText(_heading[2], startX + xcoor[2] * xLength, yHeight, SCREEN_CONSTANTS.headingPaint);
        _canvas.drawText(_heading[3], startX + xcoor[3] * xLength, yHeight, SCREEN_CONSTANTS.headingPaint);
        _canvas.drawText(_heading[4], startX + xcoor[4] * xLength, yHeight, SCREEN_CONSTANTS.headingPaint);
        _canvas.drawText(_heading[5], startX + xcoor[5] * xLength, yHeight, SCREEN_CONSTANTS.headingPaint);
        yHeight = yHeight + CScreenConstants.pxToDp(CScreenConstants.firstLineTopMargin, getContext());
        printPlanetDegreeAndSubsub();

    }

    private void printPlanetDegreeAndSubsub() {
        String[] naks = null;
        String _directOrRect = "";

        // TO SHOW PLANETS AND ITS DEGREE
        for (int i = 0; i < 13; i++) {
            naks = _kpPlanetPositions[i][1].split("-");
            // ADDED BY BIJENDRA ON 14-DEC-2012

            if ((i % 2 == 0)) {
                if (this.SCREEN_CONSTANTS.NewDeviceScreenWidth < CGlobalVariables.SMALL_DEVICE_WIDTH) {
                    _canvas.drawRect(3, yHeight - CScreenConstants.pxToDp(CScreenConstants.margininline - CScreenConstants.rectHieghtMarginTop, getContext()), this.SCREEN_CONSTANTS.NewDeviceScreenWidth, yHeight + CScreenConstants.pxToDp(-CScreenConstants.rectHieghtMarginBottom, getContext()), SCREEN_CONSTANTS.TableRow_Background_Color);
                } else {
                    _canvas.drawRect(3, yHeight - CScreenConstants.pxToDp(CScreenConstants.margininline - CScreenConstants.rectHieghtMarginTop, getContext()), this.SCREEN_CONSTANTS.NewDeviceScreenWidth, yHeight + CScreenConstants.pxToDp(CScreenConstants.rectHieghtMarginBottom, getContext()), SCREEN_CONSTANTS.TableRow_Background_Color);
                }
            }

            if (i == 0) {
                //FOR ASCEDENT
                _canvas.drawText(_planetNames[13], startX + xcoor[0], yHeight, SCREEN_CONSTANTS.contentPaint);
                _canvas.drawText(_kpPlanetPositions[0][0], startX + xcoor[1] * xLength, yHeight, SCREEN_CONSTANTS.contentPaint);

                if (naks.length > 0) {
                    _canvas.drawText(naks[0], startX + xcoor[2] * xLength, yHeight, SCREEN_CONSTANTS.contentPaint);
                    _canvas.drawText(naks[1], startX + xcoor[3] * xLength, yHeight, SCREEN_CONSTANTS.contentPaint);
                    _canvas.drawText(naks[2], startX + xcoor[4] * xLength, yHeight, SCREEN_CONSTANTS.contentPaint);
                    _canvas.drawText(naks[3], startX + xcoor[5] * xLength, yHeight, SCREEN_CONSTANTS.contentPaint);

                }

            } else {
                _directOrRect = _direct[i - 1];
                if (i != 1) {
                    if (_combust[i - 2].trim().length() > 0)
                        _directOrRect += CGlobalVariables.UnicodeSpace + _combust[i - 2];
                }
                if (_directOrRect.trim().length() > 0) {
                    // FORMAT
                    _directOrRect = _leftBracket + _directOrRect.trim() + _rightBracket;
                    // END
                }
                _canvas.drawText(_planetNames[i] + _directOrRect,
                        startX + xcoor[0], yHeight, SCREEN_CONSTANTS.contentPaint);

                _canvas.drawText(_kpPlanetPositions[i][0], startX + xcoor[1]
                        * xLength, yHeight, SCREEN_CONSTANTS.contentPaint);
                _canvas.drawText(naks[0], startX + xcoor[2] * xLength, yHeight,
                        SCREEN_CONSTANTS.contentPaint);
                _canvas.drawText(naks[1], startX + xcoor[3] * xLength, yHeight,
                        SCREEN_CONSTANTS.contentPaint);
                _canvas.drawText(naks[2], startX + xcoor[4] * xLength, yHeight,
                        SCREEN_CONSTANTS.contentPaint);
                _canvas.drawText(naks[3], startX + xcoor[5] * xLength, yHeight,
                        SCREEN_CONSTANTS.contentPaint);
            }
            yHeight = yHeight + CScreenConstants.pxToDp(CScreenConstants.margininline, getContext());
        }
        // END

        //Draw Note below the table.
       // yHeight = yHeight + CScreenConstants.pxToDp(CScreenConstants.noteTopMarign, getContext());
        _canvas.drawText(noteHeadings[0], 10, yHeight, SCREEN_CONSTANTS.headingPaint);
        yHeight = yHeight + CScreenConstants.pxToDp(CScreenConstants.noteFirstLineMarign, getContext());
        for (int i = 1; i < noteHeadings.length; i++) {
            _canvas.drawText(noteHeadings[i], 50, yHeight, SCREEN_CONSTANTS.notePaint);
            yHeight = yHeight + CScreenConstants.pxToDp(CScreenConstants.noteLineMargin, getContext());
        }
        //End note
        CGenerateAppViews.setParamsOfViewKpPlanetaryPositions(yHeight);
    }


}

