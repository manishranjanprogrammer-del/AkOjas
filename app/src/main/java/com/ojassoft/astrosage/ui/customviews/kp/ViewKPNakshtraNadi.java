package com.ojassoft.astrosage.ui.customviews.kp;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanNakshtraNadi;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.act.OutputMasterActivity;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CScreenConstants;
import com.ojassoft.astrosage.utils.CUtils;

public class    ViewKPNakshtraNadi extends ScrollView {

    private Canvas _canvas = null;
    int xLength = 0;
    int yHeight = 0;
    private Context _context;
    private BeanNakshtraNadi[] nakBeans;
    String[] _plntNames;
    String[] _leveltitles;
    TableLayout.LayoutParams tableParams;
    final int PLANET = 0, STAR_LORD = 1, SUB_LORD = 2;
    final int BOLD = 1, NORMAL = 2;
    ArrayList<String[][]> arr = new ArrayList<String[][]>();

    boolean _isTablet = false;
    TableLayout tabel0, tabel1, tabel2;
    int width;
    private int startX = 10;
    float[] xcoor = {0, 0.9f, 2.2f, 2.7f, 3.2f, 3.6f};
    LinearLayout layout;
    String[][] arr2;
    String[] _kPNakNadiHeadingDetail = null;
    Typeface mediumTypeface = null;
    Typeface regularTypeface = null;
    int _languageCode = CGlobalVariables.ENGLISH;
    CScreenConstants SCREEN_CONSTANTS;

    public ViewKPNakshtraNadi(Context context, BeanNakshtraNadi[] objNakBean, String[] plntNames, String[] leveltitles, String[] KPNakNadiHeadingDetail, boolean isTablet, Typeface typeface, int LanguageCode, CScreenConstants SCREEN_CONSTANTS) {
        super(context);
        _context = context;
        nakBeans = objNakBean;
        _plntNames = plntNames;
        _leveltitles = leveltitles;
        _kPNakNadiHeadingDetail = KPNakNadiHeadingDetail;
        _isTablet = isTablet;
        mediumTypeface = CUtils.getRobotoFont(context, ((AstrosageKundliApplication) context.getApplicationContext()).getLanguageCode(), CGlobalVariables.medium);
        regularTypeface = CUtils.getRobotoFont(context, ((AstrosageKundliApplication) context.getApplicationContext()).getLanguageCode(), CGlobalVariables.regular);
        _languageCode = LanguageCode;
        this.SCREEN_CONSTANTS = SCREEN_CONSTANTS;
        width = (int) SCREEN_CONSTANTS.DeviceScreenWidth;
        tabel0 = new TableLayout(context);
        tabel1 = new TableLayout(context);
        tabel2 = new TableLayout(context);

        layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));

        if (objNakBean != null) {
            initView();
            designThisView();
        }
    }

    private TableRow getHeadingTableRow(Context context) {
        TableRow tr = new TableRow(context);
        tr.addView(getCustomHeadingCell(getResources().getString(R.string.nakshatra_nadi_top_heading), _context, SCREEN_CONSTANTS.HeadingColor, true));
        return tr;
    }

    private TextView getCustomHeadingCell(String text, Context context, Paint pTextcolor, boolean isMainHeading) {
        TextView cell = new TextView(context);
        cell.setTextColor(getResources().getColor(R.color.black));

        cell.setText(text);
        //cell.setPadding(2, 2, 2, 2);
        cell.setWidth(width);
        cell.setSingleLine(false);
        cell.setTextSize(18);
//        if (isMainHeading) {
            cell.setBackgroundColor(getResources().getColor(R.color.backgroundColor));
            cell.setTypeface(mediumTypeface);
//        } else {
//            cell.setTypeface(mediumTypeface);
//        }
        return cell;
    }


    private void designThisView() {
        tableParams = new TableLayout.LayoutParams(width, TableLayout.LayoutParams.MATCH_PARENT);
        //tabel0.setLayoutParams(this.tableParams);
        //tabel0.setBackgroundColor(getResources().getColor(R.color.blue));
        tabel0.setStretchAllColumns(true);
        tabel0.addView(getHeadingTableRow(_context));
        layout.addView(tabel0);
        LinearLayout.LayoutParams tableParams0 = (LinearLayout.LayoutParams) tabel0.getLayoutParams();
        tableParams0.width = TableLayout.LayoutParams.MATCH_PARENT;
        tableParams0.height = TableLayout.LayoutParams.WRAP_CONTENT;
        tableParams0.topMargin = CScreenConstants.pxToDp(CScreenConstants.firstTableTopMargin, _context);
        tabel0.setLayoutParams(tableParams0);

        tabel1.setColumnStretchable(1, true);
        //tabel1.setShowDividers(LinearLayout.SHOW_DIVIDER_NONE);
        for (int j = 0; j < nakBeans.length; j++) {
            tabel1.addView(getValueTableRow(j, _context));
        }
        layout.addView(tabel1);
        LinearLayout.LayoutParams tableParams1 = (LinearLayout.LayoutParams) tabel1.getLayoutParams();
        tableParams1.width = TableLayout.LayoutParams.MATCH_PARENT;
        tableParams1.height = TableLayout.LayoutParams.WRAP_CONTENT;
        tableParams1.topMargin = CScreenConstants.pxToDp(CScreenConstants.secondTableTopMargin, getContext());
        tabel1.setLayoutParams(tableParams1);
        String note0 = _kPNakNadiHeadingDetail[0];
        String note1 = _leveltitles[0] + _kPNakNadiHeadingDetail[1];
        String note2 = _leveltitles[1] + _kPNakNadiHeadingDetail[2];
        String note3 = _leveltitles[2] + _kPNakNadiHeadingDetail[3];
        tabel2.setLayoutParams(this.tableParams);
        tabel2.setStretchAllColumns(true);
        tabel2.addView(getNoteTableRow(note0, true));
        tabel2.addView(getNoteTableRow(note1, false));
        tabel2.addView(getNoteTableRow(note2, false));
        tabel2.addView(getNoteTableRow(note3, false));
        layout.addView(tabel2);

        this.addView(layout);

    }

    private TableRow getNoteTableRow(String note, boolean isHeading) {
        TableRow tr = new TableRow(_context);
        //int txtColor=Color.RED;
        int txtColor = SCREEN_CONSTANTS.NewHeadingColor.getColor();
        if (note != null) {
            if (isHeading) {
                tr.addView(getCustomCellwithBorder(note, _context, txtColor, true));
            } else {
                tr.addView(getCustomCellwithBorder(note, _context, txtColor, false));
            }

        }

        tr.setBackgroundColor(getResources().getColor(R.color.white));
        return tr;
    }

    private TextView getCustomCellwithBorder(String text, Context context, final int txtColor, boolean isHeading) {
        TextView cell = new TextView(context);
        cell.setTextColor(txtColor);
        cell.setText(text);
        if (isHeading) {
            cell.setTextSize(18);
            cell.setTypeface(mediumTypeface);
            cell.setPadding(10, 16, 0, 0);
        } else {
            cell.setTextSize(12);
            cell.setTypeface(regularTypeface);
            cell.setPadding(30, 5, 0, 0);
        }
        cell.setMaxWidth(width);
        cell.setGravity(Gravity.CENTER_VERTICAL);
        return cell;
    }

    private TableRow getValueTableRow(int rowNo, Context context) {
        TableRow tr = new TableRow(context);

        if (rowNo == 0 || rowNo == 3 || rowNo == 6) {
            tr.addView(getCustomCell(_leveltitles[0], _context, 0));
        } else if (rowNo == 1 || rowNo == 4 || rowNo == 7) {
            tr.addView(getCustomCell(_leveltitles[1], _context, 0));
        } else if (rowNo == 2 || rowNo == 5 || rowNo == 8) {
            tr.addView(getCustomCell(_leveltitles[2], _context, 0));
        }

        if (rowNo == 0) {
            tr.addView(getCustomCell(getFormattedLevelValues(0, PLANET), _context, 1));
            tr.addView(getCustomCell(getFormattedLevelValues(1, PLANET), _context, 2));
            tr.addView(getCustomCell(getFormattedLevelValues(2, PLANET), _context, 3));
        }

        if (rowNo == 1) {
            tr.addView(getCustomCell(getFormattedLevelValues(0, STAR_LORD), _context, 1));
            tr.addView(getCustomCell(getFormattedLevelValues(1, STAR_LORD), _context, 2));
            tr.addView(getCustomCell(getFormattedLevelValues(2, STAR_LORD), _context, 3));
        }

        if (rowNo == 2) {
            tr.addView(getCustomCell(getFormattedLevelValues(0, SUB_LORD), _context, 1));
            tr.addView(getCustomCell(getFormattedLevelValues(1, SUB_LORD), _context, 2));
            tr.addView(getCustomCell(getFormattedLevelValues(2, SUB_LORD), _context, 3));
        }

        if (rowNo == 3) {
            tr.addView(getCustomCell(getFormattedLevelValues(3, PLANET), _context, 1));
            tr.addView(getCustomCell(getFormattedLevelValues(4, PLANET), _context, 2));
            tr.addView(getCustomCell(getFormattedLevelValues(5, PLANET), _context, 3));
        }

        if (rowNo == 4) {
            tr.addView(getCustomCell(getFormattedLevelValues(3, STAR_LORD), _context, 1));
            tr.addView(getCustomCell(getFormattedLevelValues(4, STAR_LORD), _context, 2));
            tr.addView(getCustomCell(getFormattedLevelValues(5, STAR_LORD), _context, 3));
        }

        if (rowNo == 5) {
            tr.addView(getCustomCell(getFormattedLevelValues(3, SUB_LORD), _context, 1));
            tr.addView(getCustomCell(getFormattedLevelValues(4, SUB_LORD), _context, 2));
            tr.addView(getCustomCell(getFormattedLevelValues(5, SUB_LORD), _context, 3));
        }

        if (rowNo == 6) {
            tr.addView(getCustomCell(getFormattedLevelValues(6, PLANET), _context, 1));
            tr.addView(getCustomCell(getFormattedLevelValues(7, PLANET), _context, 2));
            tr.addView(getCustomCell(getFormattedLevelValues(8, PLANET), _context, 3));
        }

        if (rowNo == 7) {
            tr.addView(getCustomCell(getFormattedLevelValues(6, STAR_LORD), _context, 1));
            tr.addView(getCustomCell(getFormattedLevelValues(7, STAR_LORD), _context, 2));
            tr.addView(getCustomCell(getFormattedLevelValues(8, STAR_LORD), _context, 3));
        }

        if (rowNo == 8) {
            tr.addView(getCustomCell(getFormattedLevelValues(6, SUB_LORD), _context, 1));
            tr.addView(getCustomCell(getFormattedLevelValues(7, SUB_LORD), _context, 2));
            tr.addView(getCustomCell(getFormattedLevelValues(8, SUB_LORD), _context, 3));
        }

        if (rowNo == 0 || rowNo == 1 || rowNo == 2 || rowNo == 6 || rowNo == 7 || rowNo == 8) {
            tr.setBackgroundColor(ContextCompat.getColor(_context,R.color.light_gray_tbl_bg));
        } else {
            //tr.setBackgroundColor(getResources().getColor(R.color.white));
        }
        if (rowNo == 0 || rowNo == 3 || rowNo == 6) {
            tr.setPadding(10, 10, 10, 10);
        } else if (rowNo == 2 || rowNo == 5 || rowNo == 8) {
            tr.setPadding(10, 0, 10, 10);
        } else {
            tr.setPadding(10, 0, 0, 10);
        }

        return tr;
    }

    private String getFormattedLevelValues(int beanIndex, int rowType) {
        String value = "";

        StringBuilder sb = new StringBuilder();
        if (rowType == PLANET) {
            sb.append(arr.get(beanIndex)[0][0].trim());
            sb.append("-");
            sb.append(arr.get(beanIndex)[0][1].trim());
        } else if (rowType == STAR_LORD) {
            sb.append(arr.get(beanIndex)[1][0].trim());
            sb.append("-");
            sb.append(arr.get(beanIndex)[1][1].trim());
        } else if (rowType == SUB_LORD) {
            sb.append(arr.get(beanIndex)[2][0].trim());
            sb.append("-");
            sb.append(arr.get(beanIndex)[2][1].trim());
        }

        value = sb.toString();
        /*
        CGlobalVariables.LANGUAGE_CODE = CUtils.getApplicationLanguagePreference(_context);
		if(CGlobalVariables.LANGUAGE_CODE == CGlobalVariables.LANGUAGE_HINDI){
			value = value.replace(',', ']');
			value = value.replace('-', '&');
		}
		*/
        if (CGlobalVariables.HINDI == _languageCode) {
            //change from krutidev to unicode
            value = value.replace(',', ',');
            value = value.replace('-', '-');
        }
        return value;

    }

    private TextView getCustomCell(String text, Context context, int cellIndex) {
        TextView cell = new TextView(context);
        cell.setTextColor(getResources().getColor(R.color.black));

        cell.setText(text);
        cell.setTextSize(16);

        if (cellIndex == 0) {
            cell.setWidth(CScreenConstants.pxToDp(50, _context));
        } else {
            cell.setWidth((width - CScreenConstants.pxToDp(50, _context)) / 3);
        }

        cell.setSingleLine(false);
        cell.setTypeface(regularTypeface);
        return cell;
    }

    private void initView() {


        for (int i = 0; i < nakBeans.length; i++) {
            arr2 = new String[3][2];


            arr2[0][0] = _plntNames[nakBeans[i].getPlanetNakstraNadi().getPlanet()];
            int[] pl = nakBeans[i].getPlanetNakstraNadi().getNadi();
            //ADDED BY BIJENDRA ON 19-MARCH-2013
            if (pl != null)
                pl = CUtils.removeUpdatedDuplicates(pl);
            //END
            StringBuilder sb = new StringBuilder();
            for (int j = 0; j < pl.length; j++) {
                if (j > 0) {
                    sb.append("," + String.valueOf(pl[j]));
                } else {
                    sb.append(String.valueOf(pl[j]));
                }
            }
            arr2[0][1] = sb.toString();

            arr2[1][0] = _plntNames[nakBeans[i].getPlanetStarLordNadi().getPlanet()];
            int[] pll = nakBeans[i].getPlanetStarLordNadi().getNadi();
            //ADDED BY BIJENDRA ON 19-MARCH-2013
            if (pll != null)
                pll = CUtils.removeUpdatedDuplicates(pll);
            //pll=CUtils.sortIntArray(pll);
            //END
            StringBuilder sbb = new StringBuilder();
            for (int j = 0; j < pll.length; j++) {
                if (j > 0) {
                    sbb.append("," + String.valueOf(pll[j]));
                } else {
                    sbb.append(String.valueOf(pll[j]));
                }
            }

            arr2[1][1] = sbb.toString();

            arr2[2][0] = _plntNames[nakBeans[i].getPlanetSubLordNadi().getPlanet()];
            int[] plll = nakBeans[i].getPlanetSubLordNadi().getNadi();
            //ADDED BY BIJENDRA ON 19-MARCH-2013
            if (plll != null)
                plll = CUtils.removeUpdatedDuplicates(plll);
            //END
            StringBuilder sbs = new StringBuilder();
            for (int j = 0; j < plll.length; j++) {
                if (j > 0) {
                    sbs.append("," + String.valueOf(plll[j]));
                } else {
                    sbs.append(String.valueOf(plll[j]));
                }
            }

            arr2[2][1] = sbs.toString();


            arr.add(arr2);

        }
    }

}

