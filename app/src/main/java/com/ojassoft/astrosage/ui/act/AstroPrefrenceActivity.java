package com.ojassoft.astrosage.ui.act;

import static com.ojassoft.astrosage.utils.CGlobalVariables.IS_CALL_RECORDING_ENABLED_KEY;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.core.content.res.TypedArrayUtils;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

//import com.google.analytics.tracking.android.EasyTracker;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.CustomTypefaces;
import com.ojassoft.astrosage.varta.utils.FontUtils;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import com.actionbarsherlock.app.SherlockActivity;
//import com.actionbarsherlock.internal.widget.IcsSpinner;


public class AstroPrefrenceActivity extends BaseInputActivity {

    /*IcsSpinner ayanamsaOptions,mainModuleOptions,subModuleOptions,languageOptions;*/
    Spinner ayanamsaOptions, languageOptions;
    RadioButton radioNorth, radioSouth, radioEast;
    RadioButton radioAmanta, radioPurnimant;
    CheckBox checkBoxUrNePl, checkBoxCalendar;
    public Typeface mediumTypeface, regularTypeface, robotRegularTypeface;
    public int old_LANGUAGE_CODE;
    public int new_LANGUAGE_CODE;
    TextView tvMonthType, tvChartStyle, tvLanguage, tvAyanamsa, tvChooseOutput;
    Button buttonCancel, buttonSave, buttonDefault;
    TabLayout tabs;
    ImageView imgBackButton;
    String[] lang_option2, ayan_list2;
    Activity activity;
    private int[] displayedLanguageCodes;
    private TextView tvToolBarTitle;
    private Toolbar toolBar;
    RadioGroup rgSelectDarkMode;
    RadioGroup rgCallDisable;
    String appCurrentTheme;
    RadioButton rbCallRecordingDisableYes;
    RadioButton rbCallRecordingDisableNO;
    boolean isDisableCallSelectionChanged = false;

    public AstroPrefrenceActivity() {
        super(R.string.app_name);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lay_prefrence_screen);
        activity = this;
        regularTypeface = CUtils.getRobotoFont(this, CUtils.getLanguageCodeFromPreference(this), CGlobalVariables.regular);
        mediumTypeface = CUtils.getRobotoFont(this, CUtils.getLanguageCodeFromPreference(this), CGlobalVariables.medium);
        robotRegularTypeface = CUtils.getRobotoRegular(getApplicationContext());

        buttonCancel = (Button) findViewById(R.id.buttonCancel);
        buttonSave = (Button) findViewById(R.id.buttonSave);
        buttonDefault = (Button) findViewById(R.id.buttonDefault);

        buttonCancel.setOnClickListener(new ButtonClickListner());
        buttonSave.setOnClickListener(new ButtonClickListner());
        buttonDefault.setOnClickListener(new ButtonClickListner());


        tvChartStyle = (TextView) findViewById(R.id.tvChartStyle);
        tvMonthType = findViewById(R.id.tvMonthType);
        tvLanguage = (TextView) findViewById(R.id.tvLanguage);
        tvAyanamsa = (TextView) findViewById(R.id.tvAyanamsa);
        tvChooseOutput = (TextView) findViewById(R.id.tvChooseOutput);
        tvMonthType.setOnClickListener(new ButtonClickListner());
        radioNorth = (RadioButton) findViewById(R.id.radioNorth);
        radioSouth = (RadioButton) findViewById(R.id.radioSouth);
        radioEast = (RadioButton) findViewById(R.id.radioEast);
        rgSelectDarkMode = findViewById(R.id.rg_select_dark_mode);
        rgCallDisable = findViewById(R.id.diable_call_recording_options);
        TextView darkMode = findViewById(R.id.tvDarkMode);
        darkMode.setTypeface(regularTypeface);
        RadioButton darkOff = findViewById(R.id.rb_dark_mode_off);
        RadioButton darkOn = findViewById(R.id.rb_dark_mode_on);
        RadioButton systemDefault = findViewById(R.id.rb_dark_mode_system_default);
        rbCallRecordingDisableYes = findViewById(R.id.rb_disable_call_yes);
        rbCallRecordingDisableNO = findViewById(R.id.rb_disable_call_no);
        darkMode.setTypeface(regularTypeface);
        darkOff.setTypeface(regularTypeface);
        darkOn.setTypeface(regularTypeface);
        systemDefault.setTypeface(regularTypeface);
        radioAmanta = findViewById(R.id.radioAmanta);
        radioPurnimant = findViewById(R.id.radioPurnimant);

        checkBoxUrNePl = (CheckBox) findViewById(R.id.checkBoxUrNePl);
        checkBoxCalendar = (CheckBox) findViewById(R.id.checkBoxCalendar);

        ayanamsaOptions = (Spinner) findViewById(R.id.ics_spinner_ayanOptions);
        //mainModuleOptions = (IcsSpinner)findViewById(R.id.ics_spinner_main);
        //subModuleOptions = (IcsSpinner)findViewById(R.id.ics_spinner_sub);
        languageOptions = (Spinner) findViewById(R.id.ics_spinner_lang);


        // languageOptions.setAdapter(getSpinnerAdapter(getResources().getStringArray(R.array.language_options)));

        appCurrentTheme = CUtils.getAppDarkMode(this);
        boolean isCallRecordingEnabled = CUtils.getBooleanData(this, IS_CALL_RECORDING_ENABLED_KEY, true);

//added by neeraj
        toolBar = (Toolbar) findViewById(R.id.tool_barAppModule);
        tvToolBarTitle = (TextView) toolBar.findViewById(R.id.tvTitle);
        tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setVisibility(View.GONE);
        toolBar.setNavigationIcon(R.drawable.ic_back_arrow);
        setSupportActionBar(toolBar);
        //Log.d("AppTheme", "current theme mode = " + appCurrentTheme);
        switch (appCurrentTheme) {
            case CGlobalVariables.SYSTEM_THEME:
                systemDefault.setChecked(true);
                break;
            case CGlobalVariables.DARK_THEME:
                darkOn.setChecked(true);
                break;
            case CGlobalVariables.LIGHT_THEME:
                darkOff.setChecked(true);
                break;
        }


        if (isCallRecordingEnabled) {
            rbCallRecordingDisableNO.setChecked(true);
        } else {
            rbCallRecordingDisableYes.setChecked(true);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);

        rgSelectDarkMode.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_dark_mode_off) {
                    appCurrentTheme = CGlobalVariables.LIGHT_THEME;
                } else if (checkedId == R.id.rb_dark_mode_on) {
                    appCurrentTheme = CGlobalVariables.DARK_THEME;
                } else if (checkedId == R.id.rb_dark_mode_system_default) {
                    appCurrentTheme = CGlobalVariables.SYSTEM_THEME;
                }
                CUtils.fcmAnalyticsEvents(appCurrentTheme,CGlobalVariables.APP_THEME_CHANGE_EVENT,"");
            }
        });
        rgCallDisable.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.rb_disable_call_yes) {
                    if (isCallRecordingEnabled)
                        showAlertDialogOKCancel(1);
                } else if (checkedId == R.id.rb_disable_call_no) {
                    if (!isCallRecordingEnabled)
                        setEnableDisableToServer(0);
                }
            }
        });

        displayedLanguageCodes = getDisplayedLanguageCodes();
        languageOptions.setAdapter(getSpinnerAdapterLanguage(getDisplayedLanguageLabels()));
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT)//addded by neeraj to display correct spinner in higher virsion 29/4/16
        {
            languageOptions.setPopupBackgroundResource(R.drawable.spinner_dropdown);
            ayanamsaOptions.setPopupBackgroundResource(R.drawable.spinner_dropdown);
        }

        ayanamsaOptions.setAdapter(getSpinnerAdapter(getResources().getStringArray(R.array.ayan_list)));
        // ayanamsaOptions.setAdapter(ayanamsaAdapter);
        //mainModuleOptions.setAdapter(getSpinnerAdapter(getResources().getStringArray(R.array.module_names)));

		/*mainModuleOptions.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(IcsAdapterView<?> parent, View view,
					int position, long id) {
				subModuleOptions.setAdapter(getSpinnerAdapter(getSubScreenSpinnerItems(position)));
				SharedPreferences sharedPreferences = getSharedPreferences(CGlobalVariables.APP_PREFS_NAME, MODE_PRIVATE);
				if((getSubScreenSpinnerItems(position).length-1) < sharedPreferences.getInt(CGlobalVariables.APP_PREFS_SubModule, 0)) {
					subModuleOptions.setSelection(getSubScreenSpinnerItems(position).length-1);
				}else {
					subModuleOptions.setSelection(sharedPreferences.getInt(CGlobalVariables.APP_PREFS_SubModule, 0));
				}
				
			}

			@Override
			public void onNothingSelected(IcsAdapterView<?> parent) {
				
			}
		});*/


        //getting preference of Language to set the value of languageOptions (1-Dec-2015)
        SharedPreferences sharedPreferencesForLang = getSharedPreferences(CGlobalVariables.MYKUNDLI_LANGUAGE_PREFS_NAME, Context.MODE_PRIVATE);
        old_LANGUAGE_CODE = sharedPreferencesForLang.getInt(CGlobalVariables.APP_PREFS_AppLanguage, 0);

        initUserPreference(old_LANGUAGE_CODE);

        if (old_LANGUAGE_CODE == 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                buttonSave.setAllCaps(true);
                buttonCancel.setAllCaps(true);
                buttonDefault.setAllCaps(true);
            }
        }
        //CUtils.showAdvertisement(AstroPrefrenceActivity.this,(LinearLayout)findViewById(R.id.advLayout));
        setCustomFontAndText();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onStart()
     */
    @Override
    protected void onStart() {
        super.onStart();
        //EasyTracker.getInstance().activityStart(this);
    }

    /* (non-Javadoc)
     * @see com.actionbarsherlock.app.SherlockFragmentActivity#onStop()
     */
    @Override
    protected void onStop() {
        super.onStop();
        //EasyTracker.getInstance().activityStop(this);
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onResume()
     */
    @Override
    protected void onResume() {
        super.onResume();
        //typeface = CUtils.getRobotoFont(getApplicationContext(), CUtils.getLanguageCodeFromPreference(getApplicationContext()), CGlobalVariables.regular);
        //CUtils.applyTypeFaceOnActionBarTitle(AstroPrefrenceActivity.this, typeface,null);
        /*CUtils.showAdvertisement(AstroPrefrenceActivity.this, (LinearLayout) findViewById(R.id.advLayout));*/
        showToolBarTitle(mediumTypeface, AstroPrefrenceActivity.this.getResources()
                .getString(R.string.set_pref_title));
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*CUtils.removeAdvertisement(AstroPrefrenceActivity.this, (LinearLayout) findViewById(R.id.advLayout));*/
    }

    private void setCustomFontAndText() {
        tvChooseOutput.setText(R.string.choose_output);//
        tvChartStyle.setText(R.string.chart_style);//tvChooseOutput
        tvLanguage.setText(R.string.language);
        tvAyanamsa.setText(R.string.ayanamsa);
        radioNorth.setText(R.string.north);
        radioSouth.setText(R.string.south);
        radioEast.setText(R.string.east);
        checkBoxUrNePl.setText(R.string.dont_show_ur_np_pl);
        checkBoxCalendar.setText(R.string.use_custome_calendar);
        buttonCancel.setText(R.string.cancel);
        buttonSave.setText(R.string.save);
        buttonDefault.setText(R.string.set_default_setting1);

        tvChooseOutput.setTypeface(regularTypeface);
        tvChartStyle.setTypeface(regularTypeface);
        tvMonthType.setTypeface(regularTypeface);
        tvLanguage.setTypeface(regularTypeface);
        tvAyanamsa.setTypeface(regularTypeface);
        radioNorth.setTypeface(regularTypeface);
        radioSouth.setTypeface(regularTypeface);
        radioEast.setTypeface(regularTypeface);
        radioAmanta.setTypeface(regularTypeface);
        radioPurnimant.setTypeface(regularTypeface);
        checkBoxUrNePl.setTypeface(regularTypeface);
        checkBoxCalendar.setTypeface(regularTypeface);
        buttonCancel.setTypeface(regularTypeface);
        buttonSave.setTypeface(regularTypeface);
        buttonDefault.setTypeface(regularTypeface);//tvAyanamsa
        if (((AstrosageKundliApplication) this.getApplication())
                .getLanguageCode() == CGlobalVariables.ENGLISH) {
            buttonCancel.setText(getResources().getString(R.string.cancel).toUpperCase());
            buttonSave.setText(getResources().getString(R.string.save).toUpperCase());
            buttonDefault.setText(getResources().getString(R.string.set_default_setting1).toUpperCase());
        }

    }

    private void showToolBarTitle(Typeface typeface, String titleToshow) {
        tvToolBarTitle.setTypeface(typeface);
        if (titleToshow != null)
            tvToolBarTitle.setText(titleToshow);
        else
            // ADDED BY BIJENDRA ON 17-06-14
            tvToolBarTitle.setText("");

    }


    /* (non-Javadoc)
     * @see android.app.Activity#onResume()
     */
/*	@Override
    protected void onResume() {
		super.onResume();
		initUserPreference();		
	}

*/

    private String[] getSubScreenSpinnerItems(int moduleType) {
        String[] elements = null;
        switch (moduleType) {
            case 0:
                elements = getResources().getStringArray(R.array.basic_module_list);
                break;
            case 1:
                elements = getResources().getStringArray(R.array.dasha_sub);
                break;
            case 2:
                String[] elementsPrediction = getResources().getStringArray(R.array.predictions_module_list);
                elements = new String[]{elementsPrediction[0]};
                break;
            case 3:
                elements = getResources().getStringArray(R.array.kpsystem_module_list);
                break;
            case 4:
                elements = getResources().getStringArray(R.array.shodasvarga_module_list);
                break;
            case 5:
                elements = getResources().getStringArray(R.array.lalkitab_module_list);
                break;
            case 6:
                elements = getResources().getStringArray(R.array.tajik_module_list);
                break;

        }
        return elements;
    }

    private ArrayAdapter<CharSequence> getSpinnerAdapterLanguage(String[] spinnerOptions) {
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(AstroPrefrenceActivity.this, R.layout.spinner_list_item, spinnerOptions) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                if (position == CGlobalVariables.HINDI) {
                    ((TextView) v).setTypeface(CustomTypefaces.get(AstroPrefrenceActivity.this, "hi")); ///cmnt by neeraj 5/5/16
                } else {
                    ((TextView) v).setTypeface(robotRegularTypeface); //cmnt by neeraj 5/5/16
                }
                ((TextView) v).setTextSize(16);
                ((TextView) v).setPadding(10, 5, 10, 0);
                ((TextView) v).setBackgroundColor(getResources().getColor(R.color.white));
                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);

                if (position == CGlobalVariables.HINDI) {
                    ((TextView) v).setTypeface(CustomTypefaces.get(AstroPrefrenceActivity.this, "hi")); //cmnt by neeraj 5/5/16

                } else {
                    ((TextView) v).setTypeface(robotRegularTypeface); //cmnt by neeraj 5/5/16
                }
                ((TextView) v).setTextSize(16);

                return v;
            }
        };
        return adapter;
    }

    private ArrayAdapter<CharSequence> getSpinnerAdapter(String[] spinnerOptions) {
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(AstroPrefrenceActivity.this, R.layout.spinner_list_item, spinnerOptions) {

            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextSize(16);
                ((TextView) v).setTypeface(regularTypeface);
                ((TextView) v).setPadding(10, 0, 10, 0);
                ((TextView) v).setBackgroundColor(getResources().getColor(R.color.white));
                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setTypeface(regularTypeface);
                ((TextView) v).setTextSize(16);

                return v;
            }
        };
        return adapter;
    }

    public void gotoCancel() {
        this.finish();
    }

    public void gotoSave() {
        SharedPreferences sharedPreferences = getSharedPreferences(CGlobalVariables.APP_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        //sharedPrefEditor.putInt(CGlobalVariables.APP_PREFS_MainModule, mainModuleOptions.getSelectedItemPosition());
        // sharedPrefEditor.putInt(CGlobalVariables.APP_PREFS_SubModule, subModuleOptions.getSelectedItemPosition());
        sharedPrefEditor.putInt(CGlobalVariables.APP_PREFS_ChartStyle, getChartStyle());
        sharedPrefEditor.putInt(CGlobalVariables.APP_PREFS_MonthType, getMonthType());
        new_LANGUAGE_CODE = getSelectedLanguageCode();


        sharedPrefEditor.putInt(CGlobalVariables.APP_PREFS_Ayanmasha, ayanamsaOptions.getSelectedItemPosition());
        sharedPrefEditor.putBoolean(CGlobalVariables.APP_PREFS_NotShowPlUnNa, checkBoxUrNePl.isChecked());
        sharedPrefEditor.putBoolean(CGlobalVariables.APP_PREFS_UserWantCustomDatePicker, checkBoxCalendar.isChecked());
        sharedPrefEditor.commit();
        SharedPreferences sharedPreferencesForLang = getSharedPreferences(CGlobalVariables.MYKUNDLI_LANGUAGE_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditorForLang = sharedPreferencesForLang.edit();
        sharedPrefEditorForLang.putInt(CGlobalVariables.APP_PREFS_AppLanguage, new_LANGUAGE_CODE);
        ((AstrosageKundliApplication) getApplication()).setLanguageCode(new_LANGUAGE_CODE); //Save Changed Language Code to variable in Application Object As well. ADDED BY DEEPAK ON 25-12-2014
        sharedPrefEditorForLang.commit();
        ((AstrosageKundliApplication) getApplication()).changeAppTheme(appCurrentTheme);
        //15-oct-2015
        startActivity(new Intent(getApplicationContext(), ActAppModule.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        this.finish();
          /*if (old_LANGUAGE_CODE == new_LANGUAGE_CODE) {
            Intent intent = new Intent();
			intent.putExtra(CGlobalVariables.AYANAMSHA_KEY,ayanamsaOptions.getSelectedItemPosition());
			setResult(Activity.RESULT_OK, intent);
			this.finish();
		}else {
			startActivity(new Intent(getApplicationContext(),ActAppModule.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
			this.finish();
		}*/
    }

    /**
     * Restores persisted preference values and safely maps language codes to the current spinner rows.
     */
    private void initUserPreference(int selection_code) {
        SharedPreferences sharedPreferences = getSharedPreferences(CGlobalVariables.APP_PREFS_NAME, MODE_PRIVATE);

        //mainModuleOptions.setSelection(sharedPreferences.getInt(CGlobalVariables.APP_PREFS_MainModule, 0));
        //subModuleOptions.setSelection(sharedPreferences.getInt(CGlobalVariables.APP_PREFS_SubModule, 0));
        setChartStyle(sharedPreferences.getInt(CGlobalVariables.APP_PREFS_ChartStyle, CGlobalVariables.CHART_NORTH_STYLE));
        int monthType;
        int languageCode = ((AstrosageKundliApplication) getApplication()).getLanguageCode();
        if (languageCode == CGlobalVariables.HINDI) {
            monthType = sharedPreferences.getInt(CGlobalVariables.APP_PREFS_MonthType, CGlobalVariables.MONTH_PURNIMANT);
        } else {
            monthType = sharedPreferences.getInt(CGlobalVariables.APP_PREFS_MonthType, CGlobalVariables.MONTH_AMANTA);
        }
        setMonthType(monthType);

        languageOptions.setSelection(getSpinnerPositionForLanguageCode(selection_code));

        ayanamsaOptions.setSelection(sharedPreferences.getInt(CGlobalVariables.APP_PREFS_Ayanmasha, 0));

        checkBoxUrNePl.setChecked(sharedPreferences.getBoolean(CGlobalVariables.APP_PREFS_NotShowPlUnNa, false));
        checkBoxCalendar.setChecked(sharedPreferences.getBoolean(CGlobalVariables.APP_PREFS_UserWantCustomDatePicker, false));
    }

    private int getChartStyle() {
        if (radioNorth.isChecked()) {
            return CGlobalVariables.CHART_NORTH_STYLE;
        } else if (radioSouth.isChecked()) {
            return CGlobalVariables.CHART_SOUTH_STYLE;
        } else {
            return CGlobalVariables.CHART_EAST_STYLE;
        }
    }

    private void setChartStyle(int chartStyle) {
        if (chartStyle == CGlobalVariables.CHART_NORTH_STYLE) {
            radioNorth.setChecked(true);
        } else if (chartStyle == CGlobalVariables.CHART_SOUTH_STYLE) {
            radioSouth.setChecked(true);
        } else if (chartStyle == CGlobalVariables.CHART_EAST_STYLE) {
            radioEast.setChecked(true);
        }
    }

    private int getMonthType() {
        if (radioAmanta.isChecked()) {
            return CGlobalVariables.MONTH_AMANTA;
        } else if (radioPurnimant.isChecked()) {
            return CGlobalVariables.MONTH_PURNIMANT;
        } else {
            return CGlobalVariables.MONTH_AMANTA;
        }
    }

    private void setMonthType(int monthType) {
        if (monthType == CGlobalVariables.MONTH_AMANTA) {
            radioAmanta.setChecked(true);
        } else if (monthType == CGlobalVariables.MONTH_PURNIMANT) {
            radioPurnimant.setChecked(true);
        }
    }

    public void setDefaultSetting() {
        SharedPreferences sharedPreferences = getSharedPreferences(CGlobalVariables.APP_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.putInt(CGlobalVariables.APP_PREFS_MainModule, 0);
        sharedPrefEditor.putInt(CGlobalVariables.APP_PREFS_SubModule, 0);
        sharedPrefEditor.putInt(CGlobalVariables.APP_PREFS_ChartStyle, CGlobalVariables.CHART_NORTH_STYLE);

        int monthType = CGlobalVariables.MONTH_AMANTA;
       /* String state = CUtils.getStringData(this, CGlobalVariables.KEY_STATE, "");
        if (state.equalsIgnoreCase(CGlobalVariables.STATE_BIHAR) || state.equalsIgnoreCase(CGlobalVariables.STATE_UTTAR_PRADESH)) {
            monthType = CGlobalVariables.MONTH_PURNIMANT;
        }*/
        int languageCode = ((AstrosageKundliApplication) getApplication()).getLanguageCode();
        if (languageCode == CGlobalVariables.HINDI) {
            monthType = CGlobalVariables.MONTH_PURNIMANT;
        }
        sharedPrefEditor.putInt(CGlobalVariables.APP_PREFS_MonthType, monthType);

        sharedPrefEditor.putInt(CGlobalVariables.APP_PREFS_Ayanmasha, 0);
        sharedPrefEditor.putBoolean(CGlobalVariables.APP_PREFS_NotShowPlUnNa, false);
        sharedPrefEditor.putBoolean(CGlobalVariables.APP_PREFS_UserWantCustomDatePicker, false);
        sharedPrefEditor.commit();
          /*SharedPreferences sharedPreferencesForLang = getSharedPreferences(CGlobalVariables.MYKUNDLI_LANGUAGE_PREFS_NAME, Context.MODE_PRIVATE);
          SharedPreferences.Editor sharedPrefEditorForLang = sharedPreferencesForLang.edit();
          sharedPrefEditorForLang.putInt(CGlobalVariables.APP_PREFS_AppLanguage, CGlobalVariables.ENGLISH);
          sharedPrefEditorForLang.commit();*/
        //Default value of language is CGlobalVariables.ENGLISH =0, means English
        initUserPreference(CGlobalVariables.ENGLISH);
        //this.finish();//added by neeraj 20/4/16
    }

    private void finishActivity() {
        this.finish();
    }

    class ButtonClickListner implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.buttonSave:
                    gotoSave();
                    break;
                case R.id.buttonCancel:
                    gotoCancel();
                    break;
                case R.id.buttonDefault:
                    setDefaultSetting();
                    break;
                case R.id.tvMonthType:
                    CUtils.openLanguageSelectDialog(getSupportFragmentManager());
                    break;
                default:
                    break;
            }
        }
    }

    /**
     * Returns the localized language labels shown in the legacy preferences spinner.
     */
    private String[] getDisplayedLanguageLabels() {
        int[] languageCodes = getDisplayedLanguageCodes();
        String[] languageLabels = new String[languageCodes.length];

        for (int i = 0; i < languageCodes.length; i++) {
            languageLabels[i] = getLanguageLabelForCode(languageCodes[i]);
        }

        return languageLabels;
    }

    /**
     * Returns the shared app language ids in the same order as the visible preferences rows.
     */
    private int[] getDisplayedLanguageCodes() {
        List<Integer> languageCodes = new ArrayList<>();
        List<com.ojassoft.astrosage.varta.model.Language> languageList = CUtils.getLanguageList(this);

        for (com.ojassoft.astrosage.varta.model.Language language : languageList) {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M
                    && language.getLanguageId() == CGlobalVariables.GUJARATI) {
                continue;
            }
            languageCodes.add(language.getLanguageId());
        }

        int[] mappedCodes = new int[languageCodes.size()];
        for (int i = 0; i < languageCodes.size(); i++) {
            mappedCodes[i] = languageCodes.get(i);
        }
        return mappedCodes;
    }

    /**
     * Resolves the user-facing language label for a persisted language id.
     */
    private String getLanguageLabelForCode(int languageCode) {
        for (com.ojassoft.astrosage.varta.model.Language language : CUtils.getLanguageList(this)) {
            if (language.getLanguageId() == languageCode) {
                return language.getName();
            }
        }
        return getString(R.string.english);
    }

    /**
     * Maps the selected spinner row to the persisted language code, defaulting to English if unavailable.
     */
    private int getSelectedLanguageCode() {
        int selectedPosition = languageOptions.getSelectedItemPosition();
        if (displayedLanguageCodes == null || selectedPosition < 0 || selectedPosition >= displayedLanguageCodes.length) {
            return CGlobalVariables.ENGLISH;
        }
        return displayedLanguageCodes[selectedPosition];
    }

    /**
     * Finds the spinner row for a persisted language code and falls back to English when unsupported.
     */
    private int getSpinnerPositionForLanguageCode(int languageCode) {
        if (displayedLanguageCodes == null || displayedLanguageCodes.length == 0) {
            return 0;
        }
        for (int i = 0; i < displayedLanguageCodes.length; i++) {
            if (displayedLanguageCodes[i] == languageCode) {
                return i;
            }
        }
        return 0;
    }

//    private void setSwitchListeners() {
//        swRecordingEnableDisable.setOnCheckedChangeListener(null);
//        swRecordingEnableDisable.setChecked(isRecordingEnableOrDisable);
//        swRecordingEnableDisable.setOnCheckedChangeListener(swRecordingEnableDisableListeners);
//    }


    private void setEnableDisableToServer(int isRecordingEnableOrDisable) {
        ApiList api = RetrofitClient.getInstance().create(ApiList.class);
        Call<ResponseBody> call = api.callDisableUserCallRecord(getParams(isRecordingEnableOrDisable));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.body() != null) {
                        String myResponse = response.body().string();
                        myResponse = new String(myResponse.getBytes("ISO-8859-1"), "UTF-8");
                        //Log.d("TestHistoryResponse","myResponse==>>"+myResponse);
                        setSwitchBtn(myResponse);
                    }
                } catch (Exception e) {
                    //
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

//                setSwitchListeners();
            }
        });
    }


    private void setSwitchBtn(String myResponse) {
        try {
            JSONObject jsonObject = new JSONObject(myResponse);
            String status = jsonObject.getString("status");
            if(status.equals("2")){
                rbCallRecordingDisableYes.setChecked(true);
            }else{
                rbCallRecordingDisableNO.setChecked(true);
            }
            Log.e("mytag", "setSwitchBtn: "+jsonObject );
            String msg = jsonObject.getString("msg");
            if (TextUtils.isEmpty(msg)) {
                msg = "";
            }
            com.ojassoft.astrosage.varta.utils.CUtils.showSnackbar(rgCallDisable, msg, activity);
        } catch (Exception e) {
            //
        }
    }



    private void showAlertDialogOKCancel(int disablerecording) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.alert_pop_up_msg, null);
        builder.setView(dialogView);

        TextView textView = dialogView.findViewById(R.id.txtInfo);
        FontUtils.changeFont(this, textView, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);


        TextView _yes = dialogView.findViewById(R.id._yes);
        TextView _no = dialogView.findViewById(R.id._no);
        final AlertDialog alert = builder.create();
        alert.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.transparent)));

        _yes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                setEnableDisableToServer(disablerecording);
            }
        });

        _no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alert.dismiss();
                rbCallRecordingDisableNO.setChecked(true);
            }
        });
        alert.show();
    }


    public Map<String, String> getParams(int isRecordingEnableOrDisable) {

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.APP_KEY, com.ojassoft.astrosage.varta.utils.CUtils.getApplicationSignatureHashCode(activity));
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANG, com.ojassoft.astrosage.varta.utils.CUtils.getLanguageKey(LANGUAGE_CODE));

        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.PACKAGE_NAME, com.ojassoft.astrosage.varta.utils.CUtils.getAppPackageName(activity));
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.APP_VERSION, "" + BuildConfig.VERSION_NAME);
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.DEVICE_ID, com.ojassoft.astrosage.varta.utils.CUtils.getMyAndroidId(activity));
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.DISABLERECORDING, String.valueOf(isRecordingEnableOrDisable));
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_USER_ID, com.ojassoft.astrosage.varta.utils.CUtils.getUserIdForBlock(activity));

        boolean isLogin = com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(activity);
        try {
            if (isLogin) {
                headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.USER_PHONE_NO, com.ojassoft.astrosage.varta.utils.CUtils.getUserID(activity));
                headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.COUNTRY_CODE, com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(activity));
            } else {
                headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.PHONE_NO, "");
                headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.COUNTRY_CODE, "");
            }
        } catch (Exception e) {
            //
        }
        //Log.d("TestHistoryResponse","headers==>>"+headers);
        return headers;
    }
}
