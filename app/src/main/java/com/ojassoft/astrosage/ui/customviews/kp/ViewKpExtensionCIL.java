package com.ojassoft.astrosage.ui.customviews.kp;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanKpCIL;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CScreenConstants;
import com.ojassoft.astrosage.utils.CUtils;

public class ViewKpExtensionCIL extends ScrollView {
    Context _context;
    BeanKpCIL[] _beanArray;
    String[] _plntNames;
    String[] _leveltitles;
    //TableLayout.LayoutParams tableParams;
    String _comma;
    TableLayout myTable1, tabel2, myTable0;
    LinearLayout layout;
    boolean _isTablet = false;
    int width;
    String[] _cILNotes = null;
    Typeface mediumTypeface = null;
    Typeface regularTypeface = null;
    CScreenConstants SCREEN_CONSTANTS;

    public ViewKpExtensionCIL(Context context, BeanKpCIL[] beanArray, String[] leveltitles, String comma, String[] CILNotes, boolean isTablet, Typeface typeface, CScreenConstants _SCREEN_CONSTANTS) {
        super(context);
        _context = context;
        _beanArray = beanArray;
        _comma = comma;
        _leveltitles = leveltitles;
        _cILNotes = CILNotes;
        mediumTypeface = CUtils.getRobotoFont(context, ((AstrosageKundliApplication) context.getApplicationContext()).getLanguageCode(), CGlobalVariables.medium);
        regularTypeface = CUtils.getRobotoFont(context, ((AstrosageKundliApplication) context.getApplicationContext()).getLanguageCode(), CGlobalVariables.regular);
        SCREEN_CONSTANTS = _SCREEN_CONSTANTS;
        tabel2 = new TableLayout(context);
        width = (int) SCREEN_CONSTANTS.NewDeviceScreenWidth;
        layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        _isTablet = isTablet;
        designThisView();
    }

    private TableRow getHeadingTableRow() {
        TableRow tr = new TableRow(_context);
        tr.addView(getCustomHeadingCell(getResources().getString(R.string.cil_top_heading), _context, SCREEN_CONSTANTS.NewHeadingColor, true));
        return tr;
    }

    private TextView getCustomHeadingCell(String text, Context context, Paint pTextcolor, boolean isMainHeading) {
        TextView cell = new TextView(context);
        cell.setTextColor(getResources().getColor(R.color.black));

        cell.setText(text);
        cell.setPadding(2, 2, 2, 2);
        cell.setWidth(width);
        cell.setSingleLine(false);
        cell.setTextSize(18);
        if (isMainHeading) {
            //cell.setBackgroundColor(getResources().getColor(R.color.white));
            cell.setTypeface(mediumTypeface);
        } else {
            cell.setTypeface(mediumTypeface);
        }
        return cell;
    }

    private void designThisView() {

        // tableParams = new TableLayout.LayoutParams(TableLayout.LayoutParams.MATCH_PARENT, TableLayout.LayoutParams.WRAP_CONTENT);
       /* tableParams = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT);*/
        myTable0 = new TableLayout(_context);
        myTable0.addView(getHeadingTableRow());
        layout.addView(myTable0);
        this.addView(layout);
        LinearLayout.LayoutParams tableParams = (LinearLayout.LayoutParams) myTable0.getLayoutParams();
        tableParams.width = TableLayout.LayoutParams.MATCH_PARENT;
        tableParams.height = TableLayout.LayoutParams.WRAP_CONTENT;
        tableParams.topMargin = CScreenConstants.pxToDp(CScreenConstants.firstTableTopMargin, getContext());
        myTable0.setLayoutParams(tableParams);


        myTable1 = new TableLayout(_context);
        myTable1.setColumnStretchable(1, true);
        myTable1.setColumnStretchable(2, true);
        myTable1.setColumnStretchable(3, true);
        myTable1.setColumnStretchable(4, true);
        // myTable.setLayoutParams(this.tableParams);
        myTable1.addView(getHeadingTableRow(_leveltitles, _context));
        //NUMBER OF HOUSE
        for (int j = 0; j < _beanArray.length; j++)
            myTable1.addView(getValueTableRow(j + 1/*HOUSE NUMBER*/, _beanArray[j], _context, j));
        layout.addView(myTable1);
        LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) myTable1.getLayoutParams();
        layoutParams1.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        layoutParams1.width = LinearLayout.LayoutParams.MATCH_PARENT;
        layoutParams1.topMargin = CScreenConstants.pxToDp(CScreenConstants.secondTableTopMargin - 1, _context);
        myTable1.setLayoutParams(layoutParams1);
        String note0 = _cILNotes[0];
        String note1 = _leveltitles[0] + _cILNotes[1];
        String note2 = _leveltitles[1] + _cILNotes[2];
        String note3 = _leveltitles[2] + _cILNotes[3];
        //tabel2.setLayoutParams(this.tableParams);
        tabel2.setStretchAllColumns(true);
        tabel2.addView(getNoteTableRow(note0, true));
        tabel2.addView(getNoteTableRow(note1, false));
        tabel2.addView(getNoteTableRow(note2, false));
        tabel2.addView(getNoteTableRow(note3, false));
        note1 = _leveltitles[3] + _cILNotes[4];
        note2 = _leveltitles[4] + _cILNotes[5];
        note3 = " ";
        tabel2.addView(getNoteTableRow(note1, false));
        tabel2.addView(getNoteTableRow(note2, false));
        layout.addView(tabel2);


    }


    private TableRow getNoteTableRow(String note, boolean isHeading) {
        TableRow tr = new TableRow(_context);
        /*int txtColor=Color.RED;*///DISABLED BY BIJENDRA ON 30-OCT-13
        int txtColor = SCREEN_CONSTANTS.NewHeadingColor.getColor();//ADDED BY BIJENDRA ON 30-OCT-13
        if (note != null) {
            if (isHeading) {
                tr.addView(getCustomCellwithBorder(note, _context, txtColor, true));
            } else {
                tr.addView(getCustomCellwithBorder(note, _context, txtColor, false));
            }
        }

        //tr.setBackgroundColor(getResources().getColor(R.color.white));
        return tr;
    }

    private TextView getCustomCellwithBorder(String text, Context context, final int txtColor, boolean isHeading) {


        TextView cell = new TextView(context);
        cell.setTextColor(txtColor);
        cell.setText(text);
        cell.setTextSize(12);
        if (isHeading) {
            cell.setTextSize(18);
            cell.setTypeface(mediumTypeface);
            cell.setPadding(10, 16, 0, 0);
        } else {
            cell.setTextSize(12);
            cell.setTypeface(regularTypeface);
            cell.setPadding(30, 5, 0, 0);
        }
        /*if(isTablet){
            cell.setTextSize(SCREEN_CONSTANTS.custom_Font_TextSize);
		}
		cell.setTypeface(SCREEN_CONSTANTS.tfFoApplication);//CHANGE THE TEXT STYLE ACCORDING TO SELECTED LANGUAGE
			*/
        cell.setTypeface(regularTypeface);
        cell.setMaxWidth(width);
        cell.setGravity(Gravity.CENTER_VERTICAL);
        return cell;
    }

    private TableRow getHeadingTableRow(String[] lavalTitles, Context context) {
        TableRow tr = new TableRow(context);
        //tr.setLayoutParams(this.tableParams);
        int txtColor = SCREEN_CONSTANTS.NewHeadingColor.getColor();
        for (int i = 0; i < lavalTitles.length; i++) {
            tr.addView(getCustomCell(lavalTitles[i], _context, txtColor, i, true));
        }

        //tr.setBackgroundColor(_context.getResources().getColor(R.color.LightGray));
        return tr;

    }

    private TableRow getValueTableRow(int houseNumber, BeanKpCIL bean, Context context, int j) {

		/*int txtColorHeading=Color.RED;
        int txtColor=Color.BLUE;*/
        int txtColorHeading = SCREEN_CONSTANTS.NewHeadingColor.getColor();
        int txtColor = ContextCompat.getColor(_context,R.color.black);

        TableRow tr = new TableRow(context);

        tr.addView(getCustomCell(String.valueOf(bean.getCusp() + 1), _context, txtColorHeading, 0, false));
//		tr.addView(getSingleLine());
        tr.addView(getCustomCell(getFormattedLevelValues(bean.getKpCILType1()), _context, txtColor, 1, false));
//		tr.addView(getSingleLine());
        tr.addView(getCustomCell(getFormattedLevelValues(bean.getKpCILType2()), _context, txtColor, 2, false));
//		tr.addView(getSingleLine());
        tr.addView(getCustomCell(getFormattedLevelValues(bean.getKpCILType3()), _context, txtColor, 3, false));
//		tr.addView(getSingleLine());
        tr.addView(getCustomCell(getFormattedSingleValue(bean.getKpCILType4()), _context, txtColor, 4, false));
        if (!(j % 2 == 0)) {
            //tr.setBackgroundColor(getResources().getColor(R.color.white));
        } else {
            tr.setBackgroundColor(ContextCompat.getColor(_context,R.color.light_gray_tbl_bg));
        }

        return tr;

    }
	
	/*private View getSingleLine(){
		View v = new View(_context);
		v.setLayoutParams(new LayoutParams(1, LayoutParams.MATCH_PARENT));
		v.setBackgroundColor(Color.BLUE);
		return v;
	}*/

    private String getFormattedSingleValue(int val) {
        if (val < 1)
            return "";
        else
            return String.valueOf(val);


    }

    private TextView getCustomCell(String text, Context context, final int txtColor, int cellIndex, boolean isHeading) {
		
		
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
        cell.setPadding(2, CScreenConstants.pxToDp(7, _context), 2, CScreenConstants.pxToDp(7, _context));
        if (isHeading) {
            cell.setTypeface(mediumTypeface);
            cell.setTextSize(18);
        } else {
            cell.setTypeface(regularTypeface);
            cell.setTextSize(16);
        }
        if (cellIndex == 0) {
            cell.setWidth((int) (width / 10));
        } else {
            cell.setMaxWidth((int) (width / 3) - (int) (width / 10));
        }
        cell.setSingleLine(false);
//		cell.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.line, 0);
        return cell;
    }

    private String getFormattedLevelValues(int[] levelValues) {
        String value = "";

        if (levelValues != null) {
            levelValues = CUtils.removeUpdatedDuplicates(levelValues);
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < levelValues.length; i++)
                if (levelValues[i] > 0) {
                    if (i > 0)
                        sb.append(_comma + String.valueOf(levelValues[i]));
                    else
                        sb.append(String.valueOf(levelValues[i]));
                }
            value = sb.toString();


        }
        return value;

    }


}

