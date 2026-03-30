package com.ojassoft.astrosage.ui.customviews.kp;

import android.content.Context;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanKpKhullarCIL;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CScreenConstants;
import com.ojassoft.astrosage.utils.CUtils;

public class ViewKpExtensionKhullarCIL extends ScrollView {
    Context _context;
    BeanKpKhullarCIL[] _beanArray;
    String[] _plntNames;
    String[] _leveltitles;
    //TableLayout.LayoutParams tableParams;
    TableLayout[] tableLayout = new TableLayout[9];
    LinearLayout linearLayout = null;
    TableLayout tabel0;
    int headingColor = 0;
    int labelColor = 0;
    String _comma;

    int width;
    Typeface mediumTypeface = null;
    Typeface regularTypeface = null;
    boolean isTablet = false;
    CScreenConstants SCREEN_CONSTANTS;

    public ViewKpExtensionKhullarCIL(Context context, BeanKpKhullarCIL[] beanArray,
                                     String[] plntNames, String[] leveltitles, String comma, Typeface typeface, CScreenConstants SCREEN_CONSTANTS) {
        super(context);
        _context = context;
        _beanArray = beanArray;
        _leveltitles = leveltitles;
        _plntNames = plntNames;
        _comma = comma;
        mediumTypeface = CUtils.getRobotoFont(context, ((AstrosageKundliApplication) context.getApplicationContext()).getLanguageCode(), CGlobalVariables.medium);
        regularTypeface = CUtils.getRobotoFont(context, ((AstrosageKundliApplication) context.getApplicationContext()).getLanguageCode(), CGlobalVariables.regular);
        headingColor = SCREEN_CONSTANTS.NewHeadingColor.getColor();
        labelColor = SCREEN_CONSTANTS.TextColor.getColor();
        width = (int) SCREEN_CONSTANTS.DeviceScreenWidth;
        this.SCREEN_CONSTANTS = SCREEN_CONSTANTS;
        isTablet = CUtils.isTablet(_context);
        tabel0 = new TableLayout(context);
        designThisView();

    }

    private TableRow getHeadingTableRow(Context context) {
        TableRow tr = new TableRow(context);
        tr.addView(getCustomHeadingCell(getResources().getString(R.string.kcil_top_heading), _context, SCREEN_CONSTANTS.HeadingColor, true));
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
        try {
            this.removeAllViews();
        } catch (Exception e) {

        }

      /*  tableParams = new LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT);*/
        linearLayout = new LinearLayout(_context);
        linearLayout.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        linearLayout.setOrientation(LinearLayout.VERTICAL);


        //tabel0.setLayoutParams(this.tableParams);
        tabel0.setStretchAllColumns(true);
        tabel0.addView(getHeadingTableRow(_context));

        linearLayout.addView(tabel0);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tabel0.getLayoutParams();
        layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
        layoutParams.topMargin = CScreenConstants.pxToDp(CScreenConstants.firstTableTopMargin, _context);
        //layoutParams.bottomMargin= CScreenConstants.pxToDp(CScreenConstants.secondTableTopMargin, _context);
        tabel0.setLayoutParams(layoutParams);

        for (int i = 0; i < _beanArray.length; i++) {
            TableLayout tableLayout = getTabletoAdd(i, _context);
            linearLayout.addView(tableLayout);
            LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) tableLayout.getLayoutParams();
            layoutParams1.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            layoutParams1.width = LinearLayout.LayoutParams.MATCH_PARENT;
            if (i == 0) {
                layoutParams1.topMargin = CScreenConstants.pxToDp(CScreenConstants.secondTableTopMargin-1, _context);
            }
            tableLayout.setLayoutParams(layoutParams1);
        }

        this.addView(linearLayout);
    }

    private TableLayout getTabletoAdd(int index, Context context) {
        tableLayout[index] = new TableLayout(context);
        tableLayout[index].setColumnStretchable(1, true);
        tableLayout[index].setPadding(10, 10, 10, 10);
        //tableLayout[index].setLayoutParams(this.tableParams);
        // tableLayout[index].setStretchAllColumns(true);
        tableLayout[index].addView(getTableRow(_beanArray[index], context, 0));
        tableLayout[index].addView(getTableRow(_beanArray[index], context, 1));
        tableLayout[index].addView(getTableRow(_beanArray[index], context, 2));
        tableLayout[index].addView(getTableRow(_beanArray[index], context, 3));
        tableLayout[index].addView(getTableRow(_beanArray[index], context, 4));
        if ((index % 2 == 0)) {
            tableLayout[index].setBackgroundColor(ContextCompat.getColor(_context,R.color.light_gray_tbl_bg));
        } else {
           // tableLayout[index].setBackgroundColor(getResources().getColor(R.color.white));
            //tableLayout[index].setBackgroundResource(R.drawable.textview_repeat_type_bg);
        }
        /*TableLayout.LayoutParams tableParams = (TableLayout.LayoutParams) tableLayout[index].getLayoutParams();
        tableParams.width = TableLayout.LayoutParams.MATCH_PARENT;
        tableParams.height = TableLayout.LayoutParams.WRAP_CONTENT;
        tableParams.topMargin = 35;
        tableLayout[index].setLayoutParams(tableParams);*/
        return tableLayout[index];

    }

    private TableRow getTableRow(BeanKpKhullarCIL beanArray, Context context,
                                 int cellIndex) {
        TableRow tr = new TableRow(context);

        // FOR CELL 0
        if (cellIndex == 0) {
            tr.addView(getCellToAdd(_leveltitles[cellIndex], context, headingColor, true));
            tr.addView(getCellToAdd(_plntNames[beanArray.getPlanet()], context, labelColor, true));
        }
        // FOR CELL 1
        if (cellIndex == 1) {
            tr.addView(getCellToAdd(_leveltitles[cellIndex], context, headingColor, true));
            tr.addView(getCellToAdd(getFormattedLevelValues(beanArray.getKCILType1()), context, labelColor, false));
        }
        // FOR CELL 2
        if (cellIndex == 2) {
            tr.addView(getCellToAdd(_leveltitles[cellIndex], context,
                    headingColor, true));
            tr.addView(getCellToAdd(
                    getFormattedLevelValues(beanArray.getKCILType2()), context,
                    labelColor, false));
        }

        // FOR CELL 3
        if (cellIndex == 3) {
            tr.addView(getCellToAdd(_leveltitles[cellIndex], context,
                    headingColor, true));
            tr.addView(getCellToAdd(
                    getFormattedLevelValues(beanArray.getKCILType3()), context,
                    labelColor, false));
        }
        // FOR CELL 4
        if (cellIndex == 4) {
            tr.addView(getCellToAdd(_leveltitles[cellIndex], context,
                    headingColor, true));
            tr.addView(getCellToAdd(
                    getFormattedLevelValues(beanArray.getKCILType4()), context,
                    labelColor, false));
        }
        return tr;
    }

    private TextView getCellToAdd(String text, Context context,
                                  final int txtColor, boolean isHeader) {
        /*TextView cell = new TextView(context) {
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
        cell.setTextColor(ContextCompat.getColor(_context,R.color.black));
        cell.setTextSize(16);
        //DISABLED BY BIJENDRA ON 30-OCT-13
        if (isHeader) {
            cell.setWidth((int) (width / 3));
            //cell.setBackgroundResource(R.drawable.heading_textview_repeat_type_bg);
            cell.setTypeface(regularTypeface, Typeface.BOLD);
        } else {
            //cell.setBackgroundResource(R.drawable.textview_repeat_type_bg);
            cell.setTypeface(regularTypeface);
        }
        //END
        cell.setText(text);
        cell.setPadding(4, 2, 4, 2);

        //cell.setTextSize(CGlobalVariables.custom_Font_TextSize);
        //ADDED BY BIJENDRA ON 30-OCT-13
        /*if(isTablet){
            if(CGlobalVariables.LANGUAGE_CODE == CGlobalVariables.LANGUAGE_HINDI)
				cell.setTextSize(CGlobalVariables.custom_Font_TextSize+4);
			else
				cell.setTextSize(CGlobalVariables.custom_Font_TextSize);
		}else {
			if(CGlobalVariables.LANGUAGE_CODE == CGlobalVariables.LANGUAGE_HINDI)
				cell.setTextSize(17);
			else 
				cell.setTextSize(14);
		}
		//END
		*/


        cell.setWidth(width / 2);//ADDED BY BIJENDRA ON 30-OCT-13
        cell.setGravity(Gravity.CENTER_VERTICAL);
        return cell;
    }

    private String getFormattedLevelValues(int[] levelValues) {
        String value = "";

        if (levelValues != null) {
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
