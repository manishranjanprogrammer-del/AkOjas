package com.ojassoft.astrosage.ui.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.controller.ControllerManager;
import com.ojassoft.astrosage.jinterface.IChooseLanguageFragment;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CScreenConstants;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.CustomTypefaces;

/**
 * This fragment is used to select language
 *
 * @author Bijendra
 * @date 17-dec-13
 * @copyrigth Ojassoft
 */
public class ChooseLanguageFragmentDailog extends DialogFragment {

    /**
     * Variables
     */

    // private IcsSpinner languageOptions;
    private Button butChooseLanguageOk, butChooseLanguageCancel;
    private int oldLanguageIndex = 0, checkedRadioButtonId = 0;
    //private Typeface typeface;
    private IChooseLanguageFragment iChooseLanguageFragment;
    RadioGroup langOptions;
    /* TextView rbEnglishtxt, rbHinditxt, rbTamiltxt, radioMarathitxt, radiobanglatxt,
             radiotelgutxt, radiokannadtxt, radiomalyalamtxt, radiogujratitxt;
 */
    // LinearLayout llEnglish, llHindi, llTamil, llbangla;

    RadioButton rbEnglish, rbHindi, rbTamil, radioMarathi, radiobangla,
            radiotelgu, radiokannad, radiomalyalam, radiogujrati;
    int radioBtnId[] = {R.id.radioEnglish, R.id.radioHindi, R.id.radioTamil, R.id.radiobangla, R.id.radioTelgu, R.id.radioMarathi, R.id.radiokannad, R.id.radiogujrati, R.id.radiomalyalam};

    public ChooseLanguageFragmentDailog() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences sharedPreferencesForLang = getActivity()
                .getSharedPreferences(
                        CGlobalVariables.MYKUNDLI_LANGUAGE_PREFS_NAME,
                        Context.MODE_PRIVATE);
        oldLanguageIndex = sharedPreferencesForLang.getInt(
                CGlobalVariables.APP_PREFS_AppLanguage, 0);

        //Toast.makeText(getActivity(),"oncreateOldIndex "+oldLanguageIndex,Toast.LENGTH_LONG).show();
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.chooselanguagedialog, container);

        langOptions = (RadioGroup) view.findViewById(R.id.radioGroupLanguage);
        rbEnglish = (RadioButton) view.findViewById(R.id.radioEnglish);
        rbHindi = (RadioButton) view.findViewById(R.id.radioHindi);
        rbTamil = (RadioButton) view.findViewById(R.id.radioTamil);
        radiobangla = (RadioButton) view.findViewById(R.id.radiobangla);
        radiotelgu = (RadioButton) view.findViewById(R.id.radioTelgu);
        radioMarathi = (RadioButton) view.findViewById(R.id.radioMarathi);
        radiokannad = (RadioButton) view.findViewById(R.id.radiokannad);
        radiogujrati = (RadioButton) view.findViewById(R.id.radiogujrati);
        radiomalyalam = (RadioButton) view.findViewById(R.id.radiomalyalam);

        rbEnglish.setTypeface(((BaseInputActivity) getActivity()).robotRegularTypeface);
        rbHindi.setTypeface(CustomTypefaces.get(getActivity(), "hi"));
        /*rbTamil.setTypeface(((BaseInputActivity) getActivity()).regularTypeface);
        radiobangla.setTypeface(((BaseInputActivity) getActivity()).regularTypeface);
        radioMarathi.setTypeface(((BaseInputActivity) getActivity()).regularTypeface);
        radiokannad.setTypeface(((BaseInputActivity) getActivity()).regularTypeface);
        radiogujrati.setTypeface(((BaseInputActivity) getActivity()).regularTypeface);*/
        ((TextView) view.findViewById(R.id.textViewHeading))
                .setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);
        rbEnglish.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                checkedRadioButtonId = radioBtnId[0];
            }
        });
        rbHindi.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                checkedRadioButtonId = radioBtnId[1];
            }
        });
        rbTamil.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                checkedRadioButtonId = radioBtnId[2];
            }
        });
        radiobangla.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                checkedRadioButtonId = radioBtnId[3];
            }
        });
        radiotelgu.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                checkedRadioButtonId = radioBtnId[4];
            }
        });
        radioMarathi.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                checkedRadioButtonId = radioBtnId[5];
            }
        });
        radiokannad.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                checkedRadioButtonId = radioBtnId[6];
            }
        });
        radiogujrati.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                checkedRadioButtonId = radioBtnId[7];
            }
        });
        radiomalyalam.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                checkedRadioButtonId = radioBtnId[8];
            }
        });
        // ADDED BY SHELENDRA ON 03.07.2015

        ;//(RadioButton) view.findViewById(R.id.radioTamil);
        //radioMarathi = (RadioButton) view.findViewById(R.id.radioMarathi).findViewById(R.id.radioBtn);//(RadioButton) view.findViewById(R.id.radioMarathi);
        //(RadioButton) view.findViewById(R.id.radiobangla);
        //radiotelgu = (RadioButton) view.findViewById(R.id.radiotelgu).findViewById(R.id.radioBtn);//(RadioButton) view.findViewById(R.id.radiotelgu);
        //radiokannad = (RadioButton) view.findViewById(R.id.radiokannad).findViewById(R.id.radioBtn);//(RadioButton) view.findViewById(R.id.radiokannad);
        //radiomalyalam= (RadioButton) view.findViewById(R.id.radiomalyalam).findViewById(R.id.radioBtn);//(RadioButton) view.findViewById(R.id.radiomalyalam);
        //radiogujrati = (RadioButton) view.findViewById(R.id.radiogujrati).findViewById(R.id.radioBtn);//(RadioButton) view.findViewById(R.id.radiogujrati);

		/*radiobangla.setVisibility(view.GONE);
        radiotelgu.setVisibility(view.GONE);
		radiomalyalam.setVisibility(view.GONE);
		radiokannad.setVisibility(view.GONE);
		radioMarathi.setVisibility(view.GONE);*/


        // radiogujratitxt = (TextView) view.findViewById(R.id.radiogujrati).findViewById(R.id.txt);


        //radioMarathitxt = (TextView) view.findViewById(R.id.radioMarathi).findViewById(R.id.txt);
        //radiokannadtxt = (TextView) view.findViewById(R.id.radiokannad).findViewById(R.id.txt);
        //radiotelgutxt = (TextView) view.findViewById(R.id.radiotelgu).findViewById(R.id.txt);
        //radiomalyalamtxt = (TextView) view.findViewById(R.id.radiomalyalam).findViewById(R.id.txt);

		/*radiobanglatxt.setVisibility(view.GONE);
        radiotelgutxt.setVisibility(view.GONE);
		radiomalyalamtxt.setVisibility(view.GONE);
		radiokannadtxt.setVisibility(view.GONE);
		radioMarathitxt.setVisibility(view.GONE);*/

		/*rbEnglish.setTypeface(typeface);
        rbHinditxt.setTypeface(typeface);
		rbTamiltxt.setTypeface(typeface);
		radiogujratitxt.setTypeface(typeface);*/

        //llEnglish = (LinearLayout) view.findViewById(R.id.radioEnglish).findViewById(R.id.llContainer);
        //llHindi = (LinearLayout) view.findViewById(R.id.radioHindi).findViewById(R.id.llContainer);
        //llTamil = (LinearLayout) view.findViewById(R.id.radioTamil).findViewById(R.id.llContainer);
        //llbangla = (LinearLayout) view.findViewById(R.id.radiobangla).findViewById(R.id.llContainer);



		/*LinearLayout.LayoutParams layoutParams = new RadioGroup.LayoutParams(
                RadioGroup.LayoutParams.WRAP_CONTENT,
				RadioGroup.LayoutParams.WRAP_CONTENT);*/

		/*langOptions.addView(rbEnglish);
        langOptions.addView(rbHindi);
		langOptions.addView(rbTamil);
		langOptions.addView(radiogujrati);*/

        //setClickListener(view, rbEnglish, R.id.radioEnglish);
        // setClickListener(view, rbHindi, R.id.radioHindi);
        // setClickListener(view, rbTamil, R.id.radioTamil);
        //setClickListener(view, radiogujrati, R.id.radiogujrati);
        // setClickListener(view, radiobangla, R.id.radiobangla);


        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            // call something for API Level 14+
            //rbTamil.setVisibility(View.VISIBLE);
            //		radioMarathi.setVisibility(View.VISIBLE);
            //radiobangla.setVisibility(View.VISIBLE);
            //          disable below Four lines to Enable All Languages
            //		radiotelgu.setVisibility(View.VISIBLE);
            //		radiokannad.setVisibility(View.VISIBLE);
            //		radiomalyalam.setVisibility(View.VISIBLE);
            // radiogujrati.setVisibility(View.VISIBLE);


            rbTamil.setVisibility(View.GONE);//added by Neeraj 22/4/16
            radiobangla.setVisibility(View.GONE);
            radiotelgu.setVisibility(View.GONE);
            radioMarathi.setVisibility(View.GONE);
            radiokannad.setVisibility(View.GONE);
            radiogujrati.setVisibility(View.GONE);
            radiomalyalam.setVisibility(View.GONE);

            /*
             * /// Device model String PhoneModel = android.os.Build.MODEL;
             *
             * // Android version String AndroidVersion =
             * android.os.Build.VERSION.RELEASE;
             *
             *
             * System.out.println(android.os.Build.MODEL
             * +"      "+android.os.Build.VERSION.RELEASE);
             */

            //llTamil.setVisibility(View.GONE);
            //llbangla.setVisibility(View.GONE);

        }
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
            radiogujrati.setVisibility(View.GONE);
        }/*else {
            rbTamil.setVisibility(View.GONE);
           // radioMarathi.setVisibility(View.GONE);
            radiobangla.setVisibility(View.GONE);
           // radiotelgu.setVisibility(View.GONE);
           // radiokannad.setVisibility(View.GONE);
           // radiomalyalam.setVisibility(View.GONE);
           // radiogujrati.setVisibility(View.GONE);
        }*/
        // radioMarathi.setVisibility(View.GONE);

        // rbEnglish.setTypeface(typeface);

        /*radiogujratitxt.setTypeface(CustomTypefacesForGujrati.get(getActivity(), "gu"));
        ((TextView) view.findViewById(R.id.textViewHeading))
                .setTypeface(typeface);*/

        // languageOptions =
        // (IcsSpinner)view.findViewById(R.id.ics_spinner_choose_language_Options);
        // languageOptions.setAdapter(getSpinnerAdapter(getResources().getStringArray(R.array.language_options)));
        butChooseLanguageOk = (Button) view
                .findViewById(R.id.butChooseLanguageOk);
        butChooseLanguageOk.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);
        butChooseLanguageCancel = (Button) view
                .findViewById(R.id.butChooseLanguageCancel);
        butChooseLanguageCancel.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);
        if (((AstrosageKundliApplication) getActivity().getApplication())
                .getLanguageCode() == CGlobalVariables.ENGLISH) {
            butChooseLanguageCancel.setText(getResources().getString(R.string.cancel).toUpperCase());
            butChooseLanguageOk.setText(getResources().getString(R.string.ok).toUpperCase());
        }
        butChooseLanguageOk.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                goToChooseLanguageOK();
            }
        });
        butChooseLanguageCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                goToChooseLanguageCancel();
            }
        });
        initValues();

        return view;

    }

    private void setClickListener(final View parentView, View view, final int id) {
        if (view instanceof TextView) {
            ((TextView) view).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    doActionOnRadioBtnSelect(parentView, id);
                }
            });
        } else if (view instanceof RadioButton) {
            ((RadioButton) view).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    doActionOnRadioBtnSelect(parentView, id);
                }
            });
        }
    }

    private void doActionOnRadioBtnSelect(View view, int selected) {
        for (int i = 0; i < radioBtnId.length; i++) {
            if (selected == radioBtnId[i]) {
                ((RadioButton) view.findViewById(radioBtnId[i]).findViewById(R.id.radioBtn)).setChecked(true);
                checkedRadioButtonId = radioBtnId[i];

            } else {
                ((RadioButton) view.findViewById(radioBtnId[i]).findViewById(R.id.radioBtn)).setChecked(false);
            }
        }

    }

    private int getCheckedRadioButtonId() {
        return checkedRadioButtonId;
    }


    /**
     * This function is used to init variables and dialog values
     *
     * @date 17-dec-13
     */
    private void initValues() {
        SharedPreferences sharedPreferencesForLang = getActivity()
                .getSharedPreferences(
                        CGlobalVariables.MYKUNDLI_LANGUAGE_PREFS_NAME,
                        Context.MODE_PRIVATE);
        oldLanguageIndex = sharedPreferencesForLang.getInt(
                CGlobalVariables.APP_PREFS_AppLanguage, 0);
        //Toast.makeText(getActivity(),"initValues= "+oldLanguageIndex,Toast.LENGTH_LONG).show();
        switch (oldLanguageIndex) {
            //default: setCheckedRadioButtonId(radioBtnId[oldLanguageIndex]);
            case 0:
                //langOptions.check(R.id.radioEnglish);
                rbEnglish.setChecked(true);
                break;
            case 1:
                //langOptions.check(R.id.radioHindi);
                rbHindi.setChecked(true);
                break;
            // ADDED BY SHELENDRA ON 03.07.2015
            case 2:
                //langOptions.check(R.id.radioTamil);
                rbTamil.setChecked(true);
                break;
            case 4:
                //langOptions.check(R.id.radiokannad);
                radiokannad.setChecked(true);
                break;
            case 5:
                langOptions.check(R.id.radioTelgu);
                radiotelgu.setChecked(true);
                break;
            case 6:
                //langOptions.check(R.id.radiobangla);
                radiobangla.setChecked(true);
                break;
            case 9:
                //langOptions.check(R.id.radioMarathi);
                radioMarathi.setChecked(true);

                break;
            case 7:
                //langOptions.check(R.id.radiogujrati);
                radiogujrati.setChecked(true);
                break;
            case 8:
                //langOptions.check(R.id.radiomalyalam);
                radiomalyalam.setChecked(true);
                break;
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        iChooseLanguageFragment = (IChooseLanguageFragment) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        iChooseLanguageFragment = null;
    }

    /**
     * This function is used to fire event after language selection
     *
     * @date 17-dec-13
     */
    int newLanguageIndexVar = 0;
    String regid = "";
    private void goToChooseLanguageOK() {
        com.ojassoft.astrosage.varta.utils.CUtils.saveWalletRechargeData("");
        newLanguageIndexVar = getSelectedLanguageIndex();
        //Toast.makeText(getActivity(),"choose language OK "+newLanguageIndex+"\n buttonID "+getCheckedRadioButtonId(),Toast.LENGTH_LONG).show();
        if (newLanguageIndexVar != oldLanguageIndex) {
            saveLanguageInPreference(newLanguageIndexVar);
            //Toast.makeText(getActivity(),"language index saved "+newLanguageIndex,Toast.LENGTH_LONG).show();
            // Added on 11-sep-2015
            saveChartInPreference(newLanguageIndexVar);
            //
            ((AstrosageKundliApplication) getActivity().getApplication())
                    .setLanguageCode(newLanguageIndexVar);

            changePlanetListColor(getActivity());

            // ADDED BY BIJENDRA ON 26-12-14 TO SAVE GCM REGISTRATION
            // INFORMATION ON SERVER
            regid = CUtils.getRegistrationId(getActivity()
                    .getApplicationContext());
            new ControllerManager()
                    .saveUserGCMRegistrationInformationOnOjasServer(
                            getActivity().getApplicationContext(), regid,
                            newLanguageIndexVar, CUtils.getUserName(getActivity()
                                    .getApplicationContext()));

            // Topic Registered and Unregistered
            CUtils.saveStringData(getActivity(),CGlobalVariables.LANGUAGE_TOPIC_NEED_TO_SEND_TO_GOOGLE_SERVER,"");
            new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {
                    //Unregistered Old Topic
                    try {
                        if (!regid.equals("")) {
                            String topicName = CUtils.getTopicName(oldLanguageIndex);
                            CUtils.unSubscribeTopics(regid, topicName,getActivity());
                        }
                    } catch (Exception ex) {
                        Log.e("Exception", ex.getMessage().toString());
                    }

                    //Registered New Topic
                    try {
                        if (!regid.equals("")) {
                            String topicName = CUtils.getTopicName(newLanguageIndexVar);
                            CUtils.subscribeTopics(regid, topicName,getActivity());
                        }
                    } catch (Exception ex) {
                        Log.e("Exception", ex.getMessage().toString());
                    }
                    return null;
                }
            }.execute(null, null, null);
            com.ojassoft.astrosage.varta.utils.CUtils.allAstrologersArrayList.clear();
            com.ojassoft.astrosage.varta.utils.CUtils.aiAstrologersArrayList.clear();
        }

        // CUtils.googleAnalyticSend(null,
        // CGlobalVariables.GOOGLE_ANALYTIC_LANGUAGE_CONFIG_SCREEN,
        // CGlobalVariables.arrLang[newLanguageIndex],null, 0l);
        CUtils.googleAnalyticSendWitPlayServie(getActivity(),
                CGlobalVariables.GOOGLE_ANALYTIC_LANGUAGE_CONFIG_SCREEN,
                CGlobalVariables.arrLang[newLanguageIndexVar], null);
        String labell = "language_change_" + CGlobalVariables.arrLang[newLanguageIndexVar];
        CUtils.fcmAnalyticsEvents(labell,CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK,"");

        iChooseLanguageFragment.onSelectedLanguage(newLanguageIndexVar);
        this.dismiss();
    }

    private int getSelectedLanguageIndex() {
        int langIndex = 0;
        switch (getCheckedRadioButtonId()) {
            case R.id.radioEnglish:
                langIndex = 0;
                break;
            case R.id.radioHindi:
                langIndex = 1;
                break;
            case R.id.radioTamil:
                langIndex = 2;
                break;
            case R.id.radiokannad:
                langIndex = 4;
                break;
            case R.id.radioTelgu:
                langIndex = 5;
                break;
            case R.id.radiobangla:
                langIndex = 6;
                break;
            case R.id.radioMarathi:
                langIndex = 9;
                break;
            case R.id.radiogujrati:
                langIndex = 7;
                break;
            case R.id.radiomalyalam:
                langIndex = 8;
                break;

            default:
                langIndex = oldLanguageIndex;
                break;


        }
        return langIndex;
    }

    private void saveLanguageInPreference(int newLanguageIndex) {

        SharedPreferences sharedPreferencesForLang = getActivity()
                .getSharedPreferences(
                        CGlobalVariables.MYKUNDLI_LANGUAGE_PREFS_NAME,
                        Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferencesForLang
                .edit();
        sharedPrefEditor.putInt(CGlobalVariables.APP_PREFS_AppLanguage,
                newLanguageIndex);
        sharedPrefEditor.commit();
    }

    private void saveChartInPreference(int newLanguageIndex) {
        SharedPreferences sharedPreferences = getActivity()
                .getSharedPreferences(CGlobalVariables.APP_PREFS_NAME,
                        Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        if (newLanguageIndex == CGlobalVariables.BANGALI) {//For East India Chart
            sharedPrefEditor.putInt(CGlobalVariables.APP_PREFS_ChartStyle,
                    CGlobalVariables.CHART_EAST_STYLE);
        } else if (newLanguageIndex == 2) {//For South India Chart
            sharedPrefEditor.putInt(CGlobalVariables.APP_PREFS_ChartStyle,
                    CGlobalVariables.CHART_SOUTH_STYLE);
        } else {
            sharedPrefEditor.putInt(CGlobalVariables.APP_PREFS_ChartStyle,
                    CGlobalVariables.CHART_NORTH_STYLE);
        }
       /* int monthType = CGlobalVariables.MONTH_AMANTA;
       *//* String state = CUtils.getStringData(getActivity(), CGlobalVariables.KEY_STATE,"");
        if(state.equalsIgnoreCase(CGlobalVariables.STATE_BIHAR) || state.equalsIgnoreCase(CGlobalVariables.STATE_UTTAR_PRADESH)){
            monthType = CGlobalVariables.MONTH_PURNIMANT;
        }*//*
        if (newLanguageIndex == CGlobalVariables.HINDI) {
            monthType = CGlobalVariables.MONTH_PURNIMANT;
        }
        sharedPrefEditor.putInt(CGlobalVariables.APP_PREFS_MonthType, monthType);*/
        sharedPrefEditor.commit();
    }

    /**
     * This function is to return on called activity
     *
     * @date 17-dec-13
     */
    private void goToChooseLanguageCancel() {
        this.dismiss();

    }

    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        Dialog dialog = getDialog();
        if (dialog != null) {
            WindowManager.LayoutParams wmlp = dialog.getWindow()
                    .getAttributes();
            wmlp.width = (int) width - 40;
            wmlp.height = android.view.WindowManager.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(wmlp);
            //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    private static void changePlanetListColor(Context context)
    {
        CScreenConstants.planetsColorList.put(context.getResources().getString(R.string.La),context.getResources().getColor(R.color.colorLa));
        CScreenConstants.planetsColorList.put(context.getResources().getString(R.string.Su),context.getResources().getColor(R.color.colorSu));
        CScreenConstants.planetsColorList.put(context.getResources().getString(R.string.Mo),context.getResources().getColor(R.color.colorMo));
        CScreenConstants.planetsColorList.put(context.getResources().getString(R.string.Ma),context.getResources().getColor(R.color.colorMa));
        CScreenConstants.planetsColorList.put(context.getResources().getString(R.string.Me),context.getResources().getColor(R.color.colorMe));
        CScreenConstants.planetsColorList.put(context.getResources().getString(R.string.Ju),context.getResources().getColor(R.color.colorJu));
        CScreenConstants.planetsColorList.put(context.getResources().getString(R.string.Ve),context.getResources().getColor(R.color.colorVe));
        CScreenConstants.planetsColorList.put(context.getResources().getString(R.string.Sa),context.getResources().getColor(R.color.colorSa));
        CScreenConstants.planetsColorList.put(context.getResources().getString(R.string.Ra),context.getResources().getColor(R.color.colorRa));
        CScreenConstants.planetsColorList.put(context.getResources().getString(R.string.Ke),context.getResources().getColor(R.color.colorKe));
        CScreenConstants.planetsColorList.put(context.getResources().getString(R.string.Ur),context.getResources().getColor(R.color.colorUr));
        CScreenConstants.planetsColorList.put(context.getResources().getString(R.string.Ne),context.getResources().getColor(R.color.colorNe));
        CScreenConstants.planetsColorList.put(context.getResources().getString(R.string.Pl),context.getResources().getColor(R.color.colorPl));
    }

    /*
     * private ArrayAdapter<CharSequence> getSpinnerAdapter(String[]
	 * spinnerOptions) { ArrayAdapter<CharSequence> adapter = new
	 * ArrayAdapter<CharSequence>(getActivity(),
	 * android.R.layout.simple_list_item_checked, spinnerOptions){
	 * 
	 * public View getView(int position, View convertView, ViewGroup parent) {
	 * View v = super.getView(position, convertView, parent); ((TextView)
	 * v).setTypeface(typeface); return v; } public View getDropDownView(int
	 * position, View convertView, ViewGroup parent) { View v =
	 * super.getDropDownView(position, convertView, parent); ((TextView)
	 * v).setTypeface(typeface); return v; } }; return adapter; }
	 */

}
