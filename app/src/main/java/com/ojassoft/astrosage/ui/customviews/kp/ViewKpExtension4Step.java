package com.ojassoft.astrosage.ui.customviews.kp;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CScreenConstants;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.kpextension.BeanKP4Step;
import com.ojassoft.kpextension.BeanKp4StepPlanet;
import com.ojassoft.kpextension.GlobalVariablesKPExtension;
import com.ojassoft.kpextension.KeyValue4Step;

public class ViewKpExtension4Step extends ScrollView {

    Context _context;

    BeanKP4Step[] _nbeanArray;
    String[] _plntNames;
    String[] _headingTitles;
    TableLayout.LayoutParams tableParams;
    TableLayout myTable = null, tabel2;

    String _leftBracket, _rightBracket, _comma, _colon, _conj, _asp, _cupal, _plusSign;
    LinearLayout layout;
    boolean isTablet = false;
    WindowManager wm;
    int width;

    //DISABLED BY BIJENDRA ON 30-OCT-13
    /*int STEP1_TEXT_COLOR=Color.rgb(255,69,0);
    int OTHER_STEP_TEXT_COLOR=Color.rgb(30, 144, 255);//Color.rgb(0,0,255);//139;69;19
     */
    //END

    //ADDED BY BIJENDRA ON 30-OCT-13
    int STEP1_TEXT_COLOR = 0;
    int OTHER_STEP_TEXT_COLOR = 0;
    //END

    int TABLE_COLOR = Color.rgb(65, 105, 225);//139;69;19

    String[] _noteTitles = null;
    String _starLordTitle;
    String _subLordTitle;
    String _starLordOfSubLordTitle;
    Typeface mediumTypeface = null;
    Typeface regularTypeface = null;
    CScreenConstants SCREEN_CONSTANTS;
    TableLayout tabel0;
    int languageCode;

    public ViewKpExtension4Step(Context context, BeanKP4Step[] nbeanArray,
                                String[] plntNames, String leftBracket,
                                String rightBracket, String comma, String ColonTitle, String ConjTitle, String AspectTitle, String CuspalTitle, String PlusSign,
                                String[] NoteTitles,
                                String StarLordTitle,
                                String SubLordTitle,
                                String StarLordOfSubLordTitle,
                                Typeface typeface, CScreenConstants _SCREEN_CONSTANTS, int languageCode) {
        super(context);
        _context = context;

        _nbeanArray = nbeanArray;
        _plntNames = plntNames;

        _leftBracket = leftBracket;
        _rightBracket = rightBracket;
        _comma = comma;
        _colon = ColonTitle;
        _conj = ConjTitle;
        _asp = AspectTitle;
        _cupal = CuspalTitle;
        _plusSign = PlusSign;
        _noteTitles = NoteTitles;
        _starLordTitle = StarLordTitle;
        _subLordTitle = SubLordTitle;
        _starLordOfSubLordTitle = StarLordOfSubLordTitle;
        this.languageCode = languageCode;
        mediumTypeface = CUtils.getRobotoFont(context, languageCode, CGlobalVariables.medium);
        regularTypeface = CUtils.getRobotoFont(context, languageCode, CGlobalVariables.regular);

        STEP1_TEXT_COLOR = _SCREEN_CONSTANTS.NewHeadingColor.getColor();
        OTHER_STEP_TEXT_COLOR = _SCREEN_CONSTANTS.TextColor.getColor();
        this.SCREEN_CONSTANTS = _SCREEN_CONSTANTS;
        tabel0 = new TableLayout(context);
        designThisViewNew();
    }

    private TableRow getHeadingTableRow(Context context) {
        TableRow tr = new TableRow(context);
        tr.addView(getCustomHeadingCell(getResources().getString(R.string.four_step_top_heading), _context, SCREEN_CONSTANTS.headingPaint, true));
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

    private void designThisViewNew() {
        try {
            this.removeAllViews();
        } catch (Exception e) {
        }
        tableParams = new TableLayout.LayoutParams(
                TableLayout.LayoutParams.MATCH_PARENT,
                TableLayout.LayoutParams.WRAP_CONTENT);
        layout = new LinearLayout(_context);
        layout.setLayoutParams(new LayoutParams(
                LayoutParams.MATCH_PARENT,
                LayoutParams.WRAP_CONTENT));
        layout.setOrientation(LinearLayout.VERTICAL);

        tabel0.setLayoutParams(this.tableParams);
        tabel0.setStretchAllColumns(true);
        tabel0.addView(getHeadingTableRow(_context));
        layout.addView(tabel0);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) tabel0.getLayoutParams();
        layoutParams.height = LinearLayout.LayoutParams.WRAP_CONTENT;
        layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT;
        layoutParams.topMargin = CScreenConstants.pxToDp(CScreenConstants.firstTableTopMargin, _context);

        for (int i = 0; i < 9; i++) {
            TableLayout linearLayout = getTableToAdd(_nbeanArray[i], i);
            layout.addView(linearLayout);
            LinearLayout.LayoutParams layoutParams1 = (LinearLayout.LayoutParams) linearLayout.getLayoutParams();
            layoutParams1.height = LinearLayout.LayoutParams.WRAP_CONTENT;
            layoutParams1.width = LinearLayout.LayoutParams.MATCH_PARENT;
            if (i == 0) {
                layoutParams1.topMargin = CScreenConstants.pxToDp(CScreenConstants.secondTableTopMargin - 1, _context);
            }
            linearLayout.setLayoutParams(layoutParams1);
        }
        layout.addView(getTableForNote());
        this.addView(layout);
    }

    private View getSeparatorView() {
        View vSeparator = new View(_context);
        vSeparator.setLayoutParams(new LayoutParams(android.view.ViewGroup.LayoutParams.MATCH_PARENT, 2));
        vSeparator.setBackgroundColor(getResources().getColor(R.color.theme_brown));
        return vSeparator;
    }

    private LinearLayout getTableForNote() {

        LinearLayout ll = new LinearLayout(_context);
        ll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        ll.setPadding(2, 2, 2, 2);
        //ll.setBackgroundColor(TABLE_COLOR);

        TableLayout tableLayout = new TableLayout(_context);
        tableLayout.setColumnStretchable(1, true);
        //tableLayout.setPadding(5, 5, 5, 5);
        tableLayout.setPadding(2, 0, 2, 0);
        tableLayout.setLayoutParams(this.tableParams);


        //tableLayout.setBackgroundColor(Color.WHITE);

        TableRow trNote1 = new TableRow(_context);
        trNote1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

//		TableRow trNote2=new TableRow(_context);
//		trNote2.setLayoutParams(new LayoutParams( LayoutParams.MATCH_PARENT,LayoutParams.WRAP_CONTENT));	

        trNote1.addView(getFormattedTextView(OTHER_STEP_TEXT_COLOR, _noteTitles[0], true));

//		trNote2.addView(getFormattedTextView(OTHER_STEP_TEXT_COLOR,_noteTitles[1],false));

        tableLayout.addView(trNote1);
//		tableLayout.addView(trNote2);
        //tableLayout.setBackgroundColor(getResources().getColor(R.color.white));

        ll.addView(tableLayout);

        return ll;

    }

    private TextView getFormattedTextView(int txtColor, String text, boolean isNoteText) {
        TextView plTextView = new TextView(_context);
        plTextView.setMaxLines(2);
        plTextView.setPadding(3, 5, 3, 5);
        plTextView.setSingleLine(false);

        plTextView.setTextColor(ContextCompat.getColor(_context,R.color.black));
        if (isNoteText) {
            plTextView.setTextSize(12);
        } else {
            plTextView.setTextSize(16);
        }

        plTextView.setLayoutParams(new TableRow.LayoutParams(0, android.view.ViewGroup.LayoutParams.WRAP_CONTENT, 1));

        plTextView.setText(text);
       /* if (isHeading) {
            plTextView.setTypeface(mediumTypeface, Typeface.BOLD);
        } else {*/
        plTextView.setTypeface(regularTypeface);
        // }


        return plTextView;
    }

    private TableLayout getTableToAdd(BeanKP4Step bean, int position) {

        /*LinearLayout ll = new LinearLayout(_context);
        ll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        ll.setPadding(2, 2, 2, 2);*/
        TableLayout tableLayout = new TableLayout(_context);
        tableLayout.setColumnStretchable(1, true);
        tableLayout.setPadding(10, 10, 10, 10);
        tableLayout.setLayoutParams(this.tableParams);
        if (position % 2 == 0) {
            tableLayout.setBackgroundColor(ContextCompat.getColor(_context,R.color.light_gray_tbl_bg));
        } else {
            //tableLayout.setBackgroundColor(getResources().getColor(R.color.white));
        }
        tableLayout.addView(printStep1(bean.getKp4Step1()));
        tableLayout.addView(printStep2(bean.getKp4Step2()));
        tableLayout.addView(printStep3(bean.getKp4Step3()));
        tableLayout.addView(printStep4(bean.getKp4Step4()));

        //ll.addView(tableLayout);

        return tableLayout;

    }

    private TableRow printStep1(BeanKp4StepPlanet beanKP4planet) {
        TableRow trStep1 = new TableRow(_context);
        trStep1.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        trStep1.addView(getFormattedTextView(STEP1_TEXT_COLOR, formatStep1(beanKP4planet), false));
        //trStep1.setBackgroundColor(getResources().getColor(R.color.white));
        return trStep1;
    }

    private String formatStep1(BeanKp4StepPlanet beanKP4planet) {
        StringBuilder sb = new StringBuilder();
        //change from krutidev to unicode
        if (languageCode == CGlobalVariables.HINDI)
            sb.append("1.");
        else
            sb.append("1.");

        if (beanKP4planet.isPlanetStrong()) {
            if (beanKP4planet.isPlanetInItsOwnStar())
                sb.append(_plntNames[beanKP4planet.getPlanet()] + _plusSign + _colon);
            else
                //change from krutidev to unicode
                sb.append(_plntNames[beanKP4planet.getPlanet()] + "." + _colon);
        } else
            sb.append(_plntNames[beanKP4planet.getPlanet()] + _colon);

        if (!beanKP4planet.isPlanetStrong())
            return sb.toString();


        if (beanKP4planet.getPlanetStepValues() != null) {
            int lSuSt1 = beanKP4planet.getPlanetStepValues().length;
            for (int i = 0; i < lSuSt1; i++) {
                sb.append(CGlobalVariables.UnicodeSpace + beanKP4planet.getPlanetStepValues()[i]);

            }


            if ((beanKP4planet.getPlanet() == GlobalVariablesKPExtension.RAHU) || (beanKP4planet.getPlanet() == GlobalVariablesKPExtension.KETU)) {
                KeyValue4Step obj = beanKP4planet.getRahuKetPlacedInPlanetRashi();
                if (obj.getValues() != null) {
                    sb.append(CGlobalVariables.UnicodeSpace + _leftBracket + CGlobalVariables.UnicodeSpace + _plntNames[obj.getKey()]);

                    for (int i = 0; i < obj.getValues().length; i++)
                        sb.append(CGlobalVariables.UnicodeSpace + obj.getValues()[i]);

                    sb.append(_rightBracket);

                }
            }
        }

        //CUSPAL CONJUNCTION
        if (beanKP4planet.getPlanetCuspConjunction() > 0)
            sb.append(CGlobalVariables.UnicodeSpace + beanKP4planet.getPlanetCuspConjunction() + _leftBracket + _cupal + _rightBracket);

        //PLANET CONJUNCTION

        if (beanKP4planet.getPlanetConjunction().size() > 0) {
            for (KeyValue4Step obj : beanKP4planet.getPlanetConjunction()) {
                if (obj.getValues() != null) {
                    sb.append(CGlobalVariables.UnicodeSpace + _leftBracket + _conj + CGlobalVariables.UnicodeSpace + _plntNames[obj.getKey()]);

                    for (int i = 0; i < obj.getValues().length; i++)
                        sb.append(CGlobalVariables.UnicodeSpace + obj.getValues()[i]);

                    if ((obj.getKey() == GlobalVariablesKPExtension.RAHU) || (obj.getKey() == GlobalVariablesKPExtension.KETU)) {
                        if (obj.gePlanetValuesInWhichRahuKetuPlaced() != null) {

                            sb.append(CGlobalVariables.UnicodeSpace + _leftBracket + _plntNames[obj.getPlanetInWhichRahuKetuPlaced()]);
                            for (int i = 0; i < obj.gePlanetValuesInWhichRahuKetuPlaced().length; i++)
                                sb.append(CGlobalVariables.UnicodeSpace + obj.gePlanetValuesInWhichRahuKetuPlaced()[i]);
                            sb.append(_rightBracket);
                        }
                    }

                    sb.append(_rightBracket);

                }
            }
        }

        //CUSP ASPECT

        if (beanKP4planet.isPlanetStrong()) {
            if (beanKP4planet.getCuspAspect() != null) {

                sb.append(CGlobalVariables.UnicodeSpace + _leftBracket + _asp);
                for (int i = 0; i < beanKP4planet.getCuspAspect().length; i++)
                    sb.append(CGlobalVariables.UnicodeSpace + beanKP4planet.getCuspAspect()[i]);

                sb.append(_rightBracket);

            }

        }

        //PLANET ASPECT

        if (beanKP4planet.getPlanetAspect().size() > 0) {
            for (KeyValue4Step obj : beanKP4planet.getPlanetAspect()) {
                if (obj.getValues() != null) {

                    sb.append(CGlobalVariables.UnicodeSpace + _leftBracket + _asp + CGlobalVariables.UnicodeSpace + _plntNames[obj.getKey()]);
                    for (int i = 0; i < obj.getValues().length; i++)
                        sb.append(CGlobalVariables.UnicodeSpace + obj.getValues()[i]);


                    if ((obj.getKey() == GlobalVariablesKPExtension.RAHU) || (obj.getKey() == GlobalVariablesKPExtension.KETU)) {
                        if (obj.gePlanetValuesInWhichRahuKetuPlaced() != null) {

                            sb.append(CGlobalVariables.UnicodeSpace + _plntNames[obj.getPlanetInWhichRahuKetuPlaced()]);
                            for (int i = 0; i < obj.gePlanetValuesInWhichRahuKetuPlaced().length; i++)
                                sb.append(CGlobalVariables.UnicodeSpace + obj.gePlanetValuesInWhichRahuKetuPlaced()[i]);

                        }
                    }
                    sb.append(_rightBracket);

                }
            }
        }


        return sb.toString();
    }

    private TableRow printStep2(BeanKp4StepPlanet beanKP4planet) {
        TableRow trStep2 = new TableRow(_context);

        trStep2.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        trStep2.addView(getFormattedTextView(OTHER_STEP_TEXT_COLOR, formatStep2(beanKP4planet), false));
        //trStep2.setBackgroundResource(R.drawable.textview_repeat_type_bg);

        return trStep2;
    }

    private String formatStep2(BeanKp4StepPlanet beanKP4planet) {
        StringBuilder sb = new StringBuilder();

        sb.append(_starLordTitle + CGlobalVariables.UnicodeSpace + _plntNames[beanKP4planet.getPlanet()] + _colon);

        if (beanKP4planet.getPlanetStepValues() != null) {
            int lSuSt1 = beanKP4planet.getPlanetStepValues().length;
            for (int i = 0; i < lSuSt1; i++)
                sb.append(CGlobalVariables.UnicodeSpace + beanKP4planet.getPlanetStepValues()[i]);


            if ((beanKP4planet.getPlanet() == GlobalVariablesKPExtension.RAHU) || (beanKP4planet.getPlanet() == GlobalVariablesKPExtension.KETU)) {
                KeyValue4Step obj = beanKP4planet.getRahuKetPlacedInPlanetRashi();
                if (obj.getValues() != null) {

                    sb.append(CGlobalVariables.UnicodeSpace + _leftBracket + CGlobalVariables.UnicodeSpace + _plntNames[obj.getKey()]);
                    for (int i = 0; i < obj.getValues().length; i++)
                        sb.append(CGlobalVariables.UnicodeSpace + obj.getValues()[i]);
                    sb.append(_rightBracket);
                }
            }
        }

        //CUSP CONJUNCTION
        if (beanKP4planet.getPlanetCuspConjunction() > 0) {
            sb.append(CGlobalVariables.UnicodeSpace + beanKP4planet.getPlanetCuspConjunction() + _leftBracket + _cupal + _rightBracket);

        }

        //PLANET CONJUNCTION

        if (beanKP4planet.getPlanetConjunction().size() > 0) {
            for (KeyValue4Step obj : beanKP4planet.getPlanetConjunction()) {
                if (obj.getValues() != null) {
                    sb.append(CGlobalVariables.UnicodeSpace + _leftBracket + _conj + CGlobalVariables.UnicodeSpace + _plntNames[obj.getKey()]);
                    for (int i = 0; i < obj.getValues().length; i++)
                        sb.append(CGlobalVariables.UnicodeSpace + obj.getValues()[i]);

                    if ((obj.getKey() == GlobalVariablesKPExtension.RAHU) || (obj.getKey() == GlobalVariablesKPExtension.KETU)) {
                        if (obj.gePlanetValuesInWhichRahuKetuPlaced() != null) {

                            sb.append(CGlobalVariables.UnicodeSpace + _leftBracket + CGlobalVariables.UnicodeSpace + _plntNames[obj.getPlanetInWhichRahuKetuPlaced()]);
                            for (int i = 0; i < obj.gePlanetValuesInWhichRahuKetuPlaced().length; i++)
                                sb.append(CGlobalVariables.UnicodeSpace + obj.gePlanetValuesInWhichRahuKetuPlaced()[i]);
                            sb.append(_rightBracket);
                        }
                    }
                    sb.append(_rightBracket);

                }
            }
        }

        //CUSPAL ASPECT
        if (beanKP4planet.getCuspAspect() != null) {

            sb.append(CGlobalVariables.UnicodeSpace + _leftBracket + _asp);
            for (int i = 0; i < beanKP4planet.getCuspAspect().length; i++)
                sb.append(CGlobalVariables.UnicodeSpace + beanKP4planet.getCuspAspect()[i]);

            sb.append(_rightBracket);
        }

        //PLANET ASPECT

        if (beanKP4planet.getPlanetAspect().size() > 0) {
            for (KeyValue4Step obj : beanKP4planet.getPlanetAspect()) {
                if (obj.getValues() != null) {
                    sb.append(CGlobalVariables.UnicodeSpace + _leftBracket + _asp + CGlobalVariables.UnicodeSpace + _plntNames[obj.getKey()]);
                    for (int i = 0; i < obj.getValues().length; i++)
                        sb.append(CGlobalVariables.UnicodeSpace + obj.getValues()[i]);

                    if ((obj.getKey() == GlobalVariablesKPExtension.RAHU) || (obj.getKey() == GlobalVariablesKPExtension.KETU)) {
                        if (obj.gePlanetValuesInWhichRahuKetuPlaced() != null) {

                            sb.append(CGlobalVariables.UnicodeSpace + _leftBracket + _plntNames[obj.getPlanetInWhichRahuKetuPlaced()]);
                            for (int i = 0; i < obj.gePlanetValuesInWhichRahuKetuPlaced().length; i++)
                                sb.append(CGlobalVariables.UnicodeSpace + obj.gePlanetValuesInWhichRahuKetuPlaced()[i]);
                            sb.append(_rightBracket);
                        }
                    }
                    sb.append(_rightBracket);

                }
            }
        }


        return sb.toString();

    }

    private TableRow printStep3(BeanKp4StepPlanet beanKP4planet) {
        TableRow trStep3 = new TableRow(_context);
        trStep3.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
        trStep3.addView(getFormattedTextView(OTHER_STEP_TEXT_COLOR, formatStep3(beanKP4planet), false));
        //trStep3.setBackgroundResource(R.drawable.textview_repeat_type_bg);
        return trStep3;
    }

    private String formatStep3(BeanKp4StepPlanet beanKP4planet) {
        StringBuilder sb = new StringBuilder();

        sb.append(_subLordTitle + CGlobalVariables.UnicodeSpace + _plntNames[beanKP4planet.getPlanet()] + _colon);

        if (beanKP4planet.getPlanetStepValues() != null) {
            int lSuSt1 = beanKP4planet.getPlanetStepValues().length;
            for (int i = 0; i < lSuSt1; i++)
                sb.append(CGlobalVariables.UnicodeSpace + beanKP4planet.getPlanetStepValues()[i]);


            if ((beanKP4planet.getPlanet() == GlobalVariablesKPExtension.RAHU) || (beanKP4planet.getPlanet() == GlobalVariablesKPExtension.KETU)) {
                KeyValue4Step obj = beanKP4planet.getRahuKetPlacedInPlanetRashi();
                if (obj.getValues() != null) {

                    sb.append(CGlobalVariables.UnicodeSpace + _leftBracket + CGlobalVariables.UnicodeSpace + _plntNames[obj.getKey()]);
                    for (int i = 0; i < obj.getValues().length; i++)
                        sb.append(CGlobalVariables.UnicodeSpace + obj.getValues()[i]);
                    sb.append(_rightBracket);
                }
            }
        }

        //CUSPAL CONJUNCTION

        if (beanKP4planet.getPlanetCuspConjunction() > 0) {
            sb.append(CGlobalVariables.UnicodeSpace + beanKP4planet.getPlanetCuspConjunction() + _leftBracket + _cupal + _rightBracket);

        }


        //PLANET CONJUNCTION

        if (beanKP4planet.getPlanetConjunction().size() > 0) {
            for (KeyValue4Step obj : beanKP4planet.getPlanetConjunction()) {
                if (obj.getValues() != null) {

                    sb.append(CGlobalVariables.UnicodeSpace + _leftBracket + CGlobalVariables.UnicodeSpace + _conj + CGlobalVariables.UnicodeSpace + _plntNames[obj.getKey()]);

                    for (int i = 0; i < obj.getValues().length; i++)
                        sb.append(CGlobalVariables.UnicodeSpace + obj.getValues()[i]);


                    if ((obj.getKey() == GlobalVariablesKPExtension.RAHU) || (obj.getKey() == GlobalVariablesKPExtension.KETU)) {
                        if (obj.gePlanetValuesInWhichRahuKetuPlaced() != null) {

                            sb.append(CGlobalVariables.UnicodeSpace + _leftBracket + CGlobalVariables.UnicodeSpace + _plntNames[obj.getPlanetInWhichRahuKetuPlaced()]);
                            for (int i = 0; i < obj.gePlanetValuesInWhichRahuKetuPlaced().length; i++)
                                sb.append(CGlobalVariables.UnicodeSpace + obj.gePlanetValuesInWhichRahuKetuPlaced()[i]);
                            sb.append(_rightBracket);

                        }
                    }
                    sb.append(_rightBracket);

                }
            }
        }


        //CUSPAL ASPECT

        if (beanKP4planet.getCuspAspect() != null) {
            sb.append(CGlobalVariables.UnicodeSpace + _leftBracket + _asp);
            for (int i = 0; i < beanKP4planet.getCuspAspect().length; i++)
                sb.append(CGlobalVariables.UnicodeSpace + beanKP4planet.getCuspAspect()[i]);

            sb.append(_rightBracket);

        }

        //PLANET ASPECT

        if (beanKP4planet.getPlanetAspect().size() > 0) {
            for (KeyValue4Step obj : beanKP4planet.getPlanetAspect()) {
                if (obj.getValues() != null) {

                    sb.append(CGlobalVariables.UnicodeSpace + _leftBracket + _asp + CGlobalVariables.UnicodeSpace + _plntNames[obj.getKey()]);

                    for (int i = 0; i < obj.getValues().length; i++)
                        sb.append(CGlobalVariables.UnicodeSpace + obj.getValues()[i]);

                    if ((obj.getKey() == GlobalVariablesKPExtension.RAHU) || (obj.getKey() == GlobalVariablesKPExtension.KETU)) {
                        if (obj.gePlanetValuesInWhichRahuKetuPlaced() != null) {

                            sb.append(CGlobalVariables.UnicodeSpace + _leftBracket + CGlobalVariables.UnicodeSpace + _plntNames[obj.getPlanetInWhichRahuKetuPlaced()]);

                            for (int i = 0; i < obj.gePlanetValuesInWhichRahuKetuPlaced().length; i++)
                                sb.append(CGlobalVariables.UnicodeSpace + obj.gePlanetValuesInWhichRahuKetuPlaced()[i]);
                            sb.append(_rightBracket);
                        }
                    }

                    sb.append(_rightBracket);
                }
            }
        }


        return sb.toString();

    }

    private TableRow printStep4(BeanKp4StepPlanet beanKP4planet) {
        TableRow trStep4 = new TableRow(_context);
        trStep4.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));

        trStep4.addView(getFormattedTextView(OTHER_STEP_TEXT_COLOR, formatStep4(beanKP4planet), false));
        //trStep4.setBackgroundResource(R.drawable.textview_repeat_type_bg);
        return trStep4;

    }

    private String formatStep4(BeanKp4StepPlanet beanKP4planet) {
        StringBuilder sb = new StringBuilder();


        sb.append(_starLordOfSubLordTitle + CGlobalVariables.UnicodeSpace + _plntNames[beanKP4planet.getPlanet()] + _colon);

        if (beanKP4planet.getPlanetStepValues() != null) {
            int lSuSt1 = beanKP4planet.getPlanetStepValues().length;
            for (int i = 0; i < lSuSt1; i++)
                sb.append(CGlobalVariables.UnicodeSpace + beanKP4planet.getPlanetStepValues()[i]);

            if ((beanKP4planet.getPlanet() == GlobalVariablesKPExtension.RAHU) || (beanKP4planet.getPlanet() == GlobalVariablesKPExtension.KETU)) {
                KeyValue4Step obj = beanKP4planet.getRahuKetPlacedInPlanetRashi();
                if (obj.getValues() != null) {

                    sb.append(CGlobalVariables.UnicodeSpace + _leftBracket + CGlobalVariables.UnicodeSpace + _plntNames[obj.getKey()]);

                    for (int i = 0; i < obj.getValues().length; i++)
                        sb.append(CGlobalVariables.UnicodeSpace + obj.getValues()[i]);
                    sb.append(_rightBracket);

                }
            }
        }


        //CUSPAL CONJUNCTION

        if (beanKP4planet.getPlanetCuspConjunction() > 0) {
            sb.append(CGlobalVariables.UnicodeSpace + beanKP4planet.getPlanetCuspConjunction() + _leftBracket + _cupal + _rightBracket);

        }


        //PLANET CONJUNCTION

        if (beanKP4planet.getPlanetConjunction().size() > 0) {
            for (KeyValue4Step obj : beanKP4planet.getPlanetConjunction()) {
                if (obj.getValues() != null) {
                    sb.append(CGlobalVariables.UnicodeSpace + _leftBracket + _conj + CGlobalVariables.UnicodeSpace + _plntNames[obj.getKey()]);

                    for (int i = 0; i < obj.getValues().length; i++)
                        sb.append(CGlobalVariables.UnicodeSpace + obj.getValues()[i]);


                    if ((obj.getKey() == GlobalVariablesKPExtension.RAHU) || (obj.getKey() == GlobalVariablesKPExtension.KETU)) {
                        if (obj.gePlanetValuesInWhichRahuKetuPlaced() != null) {

                            sb.append(CGlobalVariables.UnicodeSpace + _leftBracket + CGlobalVariables.UnicodeSpace + _plntNames[obj.getPlanetInWhichRahuKetuPlaced()]);
                            for (int i = 0; i < obj.gePlanetValuesInWhichRahuKetuPlaced().length; i++)
                                sb.append(CGlobalVariables.UnicodeSpace + obj.gePlanetValuesInWhichRahuKetuPlaced()[i]);
                            sb.append(_rightBracket);
                        }
                    }
                    sb.append(_rightBracket);
                }
            }
        }

        //CUSP ASPECT

        if (beanKP4planet.getCuspAspect() != null) {
            sb.append(CGlobalVariables.UnicodeSpace + _leftBracket + _asp);
            for (int i = 0; i < beanKP4planet.getCuspAspect().length; i++)
                sb.append(CGlobalVariables.UnicodeSpace + beanKP4planet.getCuspAspect()[i]);

            sb.append(_rightBracket);
        }

        //PLANET ASPECT

        if (beanKP4planet.getPlanetAspect().size() > 0) {
            for (KeyValue4Step obj : beanKP4planet.getPlanetAspect()) {
                if (obj.getValues() != null) {
                    sb.append(CGlobalVariables.UnicodeSpace + _leftBracket + _asp + CGlobalVariables.UnicodeSpace + _plntNames[obj.getKey()]);

                    for (int i = 0; i < obj.getValues().length; i++)
                        sb.append(CGlobalVariables.UnicodeSpace + obj.getValues()[i]);

                    if ((obj.getKey() == GlobalVariablesKPExtension.RAHU) || (obj.getKey() == GlobalVariablesKPExtension.KETU)) {
                        if (obj.gePlanetValuesInWhichRahuKetuPlaced() != null) {

                            sb.append(CGlobalVariables.UnicodeSpace + _leftBracket + CGlobalVariables.UnicodeSpace + _plntNames[obj.getPlanetInWhichRahuKetuPlaced()]);
                            for (int i = 0; i < obj.gePlanetValuesInWhichRahuKetuPlaced().length; i++)
                                sb.append(CGlobalVariables.UnicodeSpace + obj.gePlanetValuesInWhichRahuKetuPlaced()[i]);
                            sb.append(_rightBracket);

                        }
                    }
                    sb.append(_rightBracket);

                }
            }
        }


        return sb.toString();

    }


}

