package com.ojassoft.astrosage.ui.customviews.kp;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanKpPlanetSigWithStrenght;
import com.ojassoft.astrosage.misc.CGenerateAppViews;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CScreenConstants;

public class ViewKpPlanetSigWithStrenght extends ScrollView {
    private Canvas _canvas = null;
    Context _context;
    BeanKpPlanetSigWithStrenght[] _beanArray;
    String[] _plntNames;
    String[] _leveltitles;
    TableLayout.LayoutParams tableParams;
    TableLayout tabel, tabel2;
    String _strengthSign, _comma;
    LinearLayout layout;
    int width;
    //int lineColor=Color.rgb(65, 105,225);
    boolean _isTablet = false;
    String[] _headingNotes = null;
    Typeface _typeface = null;
    CScreenConstants SCREEN_CONSTANTS;
    int xLength = 0;
    int yHeight = 0;
    private int startX = 10;
    float[] xcoor = {0, 1.7f, 2.2f, 2.7f, 3.2f, 3.7f};

    public ViewKpPlanetSigWithStrenght(Context context, BeanKpPlanetSigWithStrenght[] beanArray, String[] plntNames, String[] leveltitles, String comma, String strengthSign, boolean isTablet, String[] HeadingNotes, Typeface typeface, CScreenConstants SCREEN_CONSTANTS) {
        super(context);
        _context = context;
        _beanArray = beanArray;
        _plntNames = plntNames;
        _leveltitles = leveltitles;
        _comma = comma;
        _strengthSign = strengthSign;
        _headingNotes = HeadingNotes;
        this.SCREEN_CONSTANTS = SCREEN_CONSTANTS;
        tabel = new TableLayout(context);
        tabel2 = new TableLayout(context);

        _isTablet = isTablet;//MODIFIED  BY BIJENDRA ON 12-SEP-13
        _typeface = typeface;
        width = (int) SCREEN_CONSTANTS.DeviceScreenWidth;

        layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        // designThisView();
    }

    private void designThisView() {
        tabel.setPadding(0, (int) ((SCREEN_CONSTANTS.DeviceScreenHeight / 18) ), 0, 0);
        tableParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
        tabel.setLayoutParams(this.tableParams);
        tabel.setStretchAllColumns(true);
        tabel.addView(getHeadingTableRow(_leveltitles, _context));
        View v = new View(_context);
        v.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, 2));
        v.setBackgroundColor(getResources().getColor(R.color.theme_brown));
        tabel.addView(v);

        for (int j = 0; j <= 8; j++)
            tabel.addView(getValueTableRow(_beanArray[j].getPlanet(), _beanArray[j], _context, j), tableParams);
        //this.addView(getValueTableRow(j, _beanArray[j],_context),tableParams);

        layout.addView(tabel);

        //Added Code by hukum to show full form of l1,l2,l3.....t1,t2.t2...
        tabel2.setLayoutParams(this.tableParams);
        tabel2.setStretchAllColumns(true);

        String note1 = _leveltitles[0] + _headingNotes[0];
        String note2 = _leveltitles[1] + _headingNotes[1];
        String note3 = _leveltitles[2] + _headingNotes[2];
        //hard coded because there are less space to write "Note", below is Adv.
//		tabel2.addView(getNoteTableRow(note1,note2,note3));
        View v2 = new View(_context);
        v2.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, 2));
        v2.setBackgroundColor(getResources().getColor(R.color.theme_brown));

        tabel2.addView(v2);
        tabel2.addView(getNoteTableRow(note1));
        tabel2.addView(getNoteTableRow(note2));
        tabel2.addView(getNoteTableRow(note3));


        note1 = _leveltitles[3] + _headingNotes[3];
        note2 = _leveltitles[4] + _headingNotes[4];
        note3 = _headingNotes[5];
        //hard coded because there are less space to write "Note", below is Adv.
        //tabel2.addView(getNoteTableRow(note1,note2,note3));
        tabel2.addView(getNoteTableRow(note1));
        tabel2.addView(getNoteTableRow(note2));
        tabel2.addView(getNoteTableRow(note3));
        layout.addView(tabel2);
        this.addView(layout);

    }

	/*private TableRow getNoteTableRow(String note1, String note2, String note3){
        TableRow tr=new TableRow(_context);
		int txtColor=Color.RED;
		if(note1!=null)
			tr.addView(getCustomCellwithBorder(note1,_context,txtColor,true));
		if(note2!=null)
			tr.addView(getCustomCellwithBorder(note2,_context,txtColor,true));
		if(note3!=null)
			tr.addView(getCustomCellwithBorder(note3,_context,txtColor,true));
		return tr;
	}*/

    private TableRow getNoteTableRow(String note) {
        TableRow tr = new TableRow(_context);

        int txtColor = SCREEN_CONSTANTS.NewHeadingColor.getColor();
        if (note != null)
            tr.addView(getCustomCellwithBorder(note, _context, txtColor, true));
        tr.setBackgroundColor(getResources().getColor(R.color.table_heading_bg));
        return tr;
    }

    private TableRow getHeadingTableRow(String[] lavalTitles, Context context) {
        int txtColor = SCREEN_CONSTANTS.headingPaint.getColor();
        TableRow tr = new TableRow(context);
        tr.setLayoutParams(this.tableParams);

        for (int i = 0; i < lavalTitles.length; i++)
            tr.addView(getCustomCell(lavalTitles[i], _context, txtColor, true));

//		tr.setBackgroundResource(R.drawable.heading_textview_repeat_type_bg);
        return tr;

    }

    private TableRow getValueTableRow(int plntNumber, BeanKpPlanetSigWithStrenght bean, Context context, int j) {
        //ADDED BY BIJENDRA ON 30-OCT-13
        int txtColorHeading = SCREEN_CONSTANTS.NewHeadingColor.getColor();
        int txtColor = SCREEN_CONSTANTS.TextColor.getColor();
        //END


        TableRow tr = new TableRow(context);


        String strL = "";

        //added by bijendra on 16 march 2013
        if (bean.getPlanetStrength() == 1)
            strL = _strengthSign;
        else
            strL = "";
        //Log.d("Bijendra>"+String.valueOf(plntNumber),String.valueOf( bean.getPlanetStrength()));

        tr.addView(getCustomCell(_plntNames[plntNumber] + strL, _context, txtColorHeading, true));
        tr.addView(getCustomCell(getFormattedSingleValue(bean.getLevel1()), _context, txtColor, false));
        tr.addView(getCustomCell(getFormattedSingleValue(bean.getLevel2()), _context, txtColor, false));
        tr.addView(getCustomCell(getFormattedLevelValues(bean.getLevel3()), _context, txtColor, false));
        tr.addView(getCustomCell(getFormattedLevelValues(bean.getLevel4()), _context, txtColor, false));

        if (!(j % 2 == 0)) {
            tr.setBackgroundColor(getResources().getColor(R.color.table_text_bg));
        }

        return tr;

    }

    private String getFormattedSingleValue(int val) {
        if (val == 0)
            return "";
        else
            return String.valueOf(val);

    }

    private TextView getCustomCellwithBorder(String text, Context context, final int txtColor, boolean isHeading) {

		
		/*TextView cell=new TextView(context){
            @Override
            protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);
                Rect rect = new Rect();
                Paint paint = new Paint();
                paint.setStyle(Paint.Style.STROKE);
                paint.setStrokeWidth(2);
                getLocalVisibleRect(rect);
                canvas.drawRect(rect, paint);     
            }
			
		};*/
        TextView cell = new TextView(context);
        cell.setTextColor(txtColor);
        cell.setText(text);
        cell.setPadding(4, 2, 4, 2);
        /*if(_isTablet){
            cell.setTextSize(CGlobalVariables.custom_Font_TextSize);
		}
		cell.setTypeface(CGlobalVariables.tfFoApplication);//CHANGE THE TEXT STYLE ACCORDING TO SELECTED LANGUAGE
*/
        //cell.setMinLines(2);
        cell.setTypeface(_typeface);
        cell.setMaxWidth(width);
        cell.setGravity(Gravity.CENTER_VERTICAL);
        return cell;
    }

    private TextView getCustomCell(String text, Context context, final int txtColor, boolean isHeading) {

		
		/*TextView cell=new TextView(context){
            @Override
            protected void onDraw(Canvas canvas) {
                super.onDraw(canvas);
                Rect rect = new Rect();
                Paint paint = new Paint();
                paint.setStyle(Paint.Style.STROKE);
             
                paint.setStrokeWidth(2);
                getLocalVisibleRect(rect);
                canvas.drawRect(rect, paint);     
            }
			
		};*/
        TextView cell = new TextView(context);
        cell.setTextColor(txtColor);
        cell.setText(text);
        cell.setPadding(10, 10, 10, 10);
        if (isHeading) {
            cell.setBackgroundColor(getResources().getColor(R.color.white));
            cell.setTypeface(_typeface, Typeface.BOLD);
        } else {
            cell.setTypeface(_typeface);
        }

        cell.setSingleLine(false);

        return cell;
    }

    private String getFormattedLevelValues(int[] levelValues) {
        StringBuilder sb = new StringBuilder();

        if (levelValues != null) {
            for (int i = 0; i < levelValues.length; i++)
                if (levelValues[i] > 0) {
                    if (i > 0)
                        sb.append(_comma + String.valueOf(levelValues[i]));
                    else
                        sb.append(String.valueOf(levelValues[i]));
                }
        }
        return sb.toString();

    }

    @Override
    protected void onDraw(Canvas canvas) {
        _canvas = canvas;
        printHeading();
    }

    private void printHeading() {
        xLength = ((int) SCREEN_CONSTANTS.DeviceScreenWidth) / 5;
        //yHeight = ((int) SCREEN_CONSTANTS.DeviceScreenHeight) / 18;
        yHeight = CScreenConstants.pxToDp(CScreenConstants.topMargin, _context);
        _canvas.drawText(_leveltitles[0], startX + xcoor[0], yHeight, SCREEN_CONSTANTS.headingPaint);
        _canvas.drawText(_leveltitles[1], startX + 1 * xLength, yHeight, SCREEN_CONSTANTS.headingPaint);
        _canvas.drawText(_leveltitles[2], startX + 2 * xLength, yHeight, SCREEN_CONSTANTS.headingPaint);
        _canvas.drawText(_leveltitles[3], startX + 3 * xLength, yHeight, SCREEN_CONSTANTS.headingPaint);
        _canvas.drawText(_leveltitles[4], startX + 4 * xLength, yHeight, SCREEN_CONSTANTS.headingPaint);
//        _canvas.drawText(_leveltitles[5], startX + xcoor[5] * xLength, yHeight, SCREEN_CONSTANTS.headingPaint);
        yHeight = yHeight + CScreenConstants.pxToDp(CScreenConstants.firstLineTopMargin,getContext());
        printPlanetDegreeAndSubsub();

    }


    private void printPlanetDegreeAndSubsub() {


        // TO SHOW PLANETS AND ITS DEGREE
        for (int i = 0; i <= 8; i++) {
            if((i%2==0)) {
                if (this.SCREEN_CONSTANTS.NewDeviceScreenWidth < CGlobalVariables.SMALL_DEVICE_WIDTH) {
                    _canvas.drawRect(3, yHeight-CScreenConstants.pxToDp(CScreenConstants.margininline-CScreenConstants.rectHieghtMarginTop,getContext()), this.SCREEN_CONSTANTS.NewDeviceScreenWidth, yHeight+CScreenConstants.pxToDp(-CScreenConstants.rectHieghtMarginBottom,getContext()), SCREEN_CONSTANTS.TableRow_Background_Color);
                } else {
                    _canvas.drawRect(3, yHeight-CScreenConstants.pxToDp(CScreenConstants.margininline-CScreenConstants.rectHieghtMarginTop,getContext()), this.SCREEN_CONSTANTS.NewDeviceScreenWidth, yHeight+CScreenConstants.pxToDp(CScreenConstants.rectHieghtMarginBottom,getContext()), SCREEN_CONSTANTS.TableRow_Background_Color);
                }
            }
            int planet = _beanArray[i].getPlanet();
            BeanKpPlanetSigWithStrenght beanKpPlanetSigWithStrenght = _beanArray[i];
            String strL = "";
            if (beanKpPlanetSigWithStrenght.getPlanetStrength() == 1)
                strL = _strengthSign;
            else
                strL = "";
            _canvas.drawText(_plntNames[planet] + strL,
                    startX + xcoor[0], yHeight, SCREEN_CONSTANTS.contentPaint);


            _canvas.drawText(getFormattedSingleValue(beanKpPlanetSigWithStrenght.getLevel1()), startX + 1
                    * xLength, yHeight, SCREEN_CONSTANTS.contentPaint);
            _canvas.drawText(getFormattedSingleValue(beanKpPlanetSigWithStrenght.getLevel2()), startX + 2 * xLength, yHeight,
                    SCREEN_CONSTANTS.contentPaint);
            _canvas.drawText(getFormattedLevelValues(beanKpPlanetSigWithStrenght.getLevel3()), startX + 3 * xLength, yHeight,
                    SCREEN_CONSTANTS.contentPaint);
            _canvas.drawText(getFormattedLevelValues(beanKpPlanetSigWithStrenght.getLevel4()), startX + 4 * xLength, yHeight,
                    SCREEN_CONSTANTS.contentPaint);
            yHeight = yHeight + CScreenConstants.pxToDp(CScreenConstants.margininline,getContext());
        }


        // END
        _canvas.drawText(_headingNotes[0], 10, yHeight, SCREEN_CONSTANTS.headingPaint);
        yHeight = yHeight + CScreenConstants.pxToDp(CScreenConstants.noteFirstLineMarign, getContext());
        for (int i = 1; i < _headingNotes.length; i++) {
            if(i<=5){
                _canvas.drawText(_leveltitles[i-1]+_headingNotes[i], 40, yHeight, SCREEN_CONSTANTS.notePaint);
            }else{
                _canvas.drawText(_headingNotes[i], 40, yHeight, SCREEN_CONSTANTS.notePaint);
            }

            yHeight = yHeight + CScreenConstants.pxToDp(CScreenConstants.noteLineMargin, getContext());
        }
        //Draw Note below the table.
       /* yHeight = yHeight + CScreenConstants.pxToDp(CScreenConstants.noteTopMarign,getContext());
        // _canvas.drawText(noteHeadings[0], 10, yHeight, SCREEN_CONSTANTS.headingPaint);
        //yHeight = yHeight + CScreenConstants.pxToDp(CScreenConstants.noteFirstLineMarign,getContext());
        String note1 = _leveltitles[0] + _headingNotes[0];
        for (int i = 0; i < 6; i++) {
            if (i == 5) {
                _canvas.drawText(_headingNotes[i], 50, yHeight, SCREEN_CONSTANTS.notePaint);
            } else {
                _canvas.drawText(_leveltitles[i] + _headingNotes[i], 50, yHeight, SCREEN_CONSTANTS.notePaint);
            }

            yHeight = yHeight +  CScreenConstants.pxToDp(CScreenConstants.noteLineMargin,getContext());
        }*/
    }
    //End note
    //CGenerateAppViews.setParamsOfViewKpPlanetaryPositions(yHeight);


}

