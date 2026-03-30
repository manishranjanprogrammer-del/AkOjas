package com.ojassoft.astrosage.ui.fragments;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.ASTROSAGE_AI_PACKAGE_NAME;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.APP_PREFS_AppLanguage;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.libojassoft.android.beans.LibOutPlace;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanDateTime;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.beans.BeanPlace;
import com.ojassoft.astrosage.jinterface.IBirthDetailInputFragment;
import com.ojassoft.astrosage.jinterface.IDialogFragmentClick;
import com.ojassoft.astrosage.misc.CalculateKundli;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.FlashLoginActivity;
import com.ojassoft.astrosage.ui.act.HomeInputScreen;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.utils.CGlobal;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.GetDefaultKundliDataService;
import com.ojassoft.astrosage.utils.PanchangUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BirthDetailInputFragment extends Fragment implements IDialogFragmentClick {
    // boolean selfKundali;
    Activity activity;
    IBirthDetailInputFragment _iBirthDetailInputFragment;
    Button buttonTime, buttonCalendar;
    Button buttonCalculateKundli;
    BeanHoroPersonalInfo _beanHoroPersonalInfo;
    BeanDateTime beanDateTime;
    BeanPlace beanPlace;
    boolean isOpenKundliPlaceEmpty;
    LinearLayout otherOptions;
    ScrollView wholeScreen;
    Spinner ayanamsaOptions, dstOptions;
    EditText etName, etKpHorary;
    RadioButton rbMale, rbFemale;
    CheckBox saveChart;
    TextView showHideButton;
    BeanHoroPersonalInfo beanHoroPersonalInfo = null;
    LinearLayout layPlaceHolder = null;
    // txtPlacedstDetail to update dsttime or standard time
    TextView txtPlaceName, txtPlaceDetail;
    TextView txtPlacedstDetail;
    LinearLayout txtPlacedstDetailContainer;
    int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    TextInputLayout textInputLayout;
    //Typeface typeface;

    boolean fragmentRestorde = false;
    private Dialog dialog;

    int KP_AYAN_NEW = 1;
    private TextInputLayout inputLayoutName, inputLayoutKpHorary;
    private CoordinatorLayout main_container;
    boolean isVisibleToUser;
    private EditText etBirthDetails;
    private View viewDummy;
    private TextView btnShowInputBirthDet;
    private LinearLayout llEnterBirthDetails;
    private String prevBirthDetails;
    public BirthDetailInputFragment() {
        setRetainInstance(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        beanDateTime = new BeanDateTime();
        beanPlace = CUtils.getUserDefaultCity(activity);
        beanHoroPersonalInfo = new BeanHoroPersonalInfo();
        beanHoroPersonalInfo.setPlace(beanPlace);
        if (savedInstanceState != null) {
            fragmentRestorde = true;
        }


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.birth_detail_input, container,
                false);
        layPlaceHolder = (LinearLayout) view.findViewById(R.id.layPlaceHolder);
        txtPlaceName = (TextView) view.findViewById(R.id.textViewPlaceName);
        // txtPlacedstDetail take refrence to set value
        txtPlaceDetail = (TextView) view
                .findViewById(R.id.textViewPlaceDetails);
        txtPlacedstDetail = (TextView) view.findViewById(R.id.textDST);
        txtPlacedstDetailContainer = (LinearLayout) view.findViewById(R.id.textDSTCotainer);
        txtPlaceName.setText(beanPlace.getCityName().trim());
        txtPlaceDetail.setText(CUtils.getPlaceDetailInSingleString(beanPlace));
        txtPlacedstDetail.setText("");

        buttonCalculateKundli = (Button) view
                .findViewById(R.id.buttonCalculateKundli);
        TextView textViewUserName = (TextView) view
                .findViewById(R.id.textViewUserName);

        TextView textViewCalendar = (TextView) view
                .findViewById(R.id.textViewCalendar);
        //TextView textViewTime = (TextView) view.findViewById(R.id.textViewTime);
        buttonCalendar = (Button) view.findViewById(R.id.buttonCalendar);
        buttonCalendar.setText(getFormatedTextToShowDate(beanDateTime));
        /*set kundli date in newKundliSelectedDate*/
        if (buttonCalendar.toString().contains("-")) {
            int monthInCount = 0;
            String[] getYear = buttonCalendar.getText().toString().split("-");
            CUtils.setNewKundliSelectedDate(Integer.parseInt(getYear[2].trim()));
            String[] shortMonth = getResources().getStringArray(R.array.month_short_name_list);
            for (int i = 0; i < shortMonth.length; i++) {
                if (shortMonth[i].equalsIgnoreCase(getYear[1].trim())) {
                    monthInCount = i;
                    break;
                }
            }
            CUtils.setNewKundliSelectedMonth(monthInCount);
        }
        buttonTime = (Button) view.findViewById(R.id.buttonTime);
        buttonTime.setText(CUtils.getFormatedTextToShowTime(beanDateTime));
        wholeScreen = (ScrollView) view.findViewById(R.id.scrollView);
        etName = (EditText) view.findViewById(R.id.etUserName);

        if (CGlobalVariables.FROM_BABY_NAMES == CGlobalVariables.SUB_MODULE_PREDICTION_BABYNAME) {
            etName.setHint(getString(R.string.enter_father_s_name));
        } else {
            etName.setHint(getString(R.string.enter_name));
        }

        etName.addTextChangedListener(new MyTextWatcher(etName));
        //etName.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
        etKpHorary = (EditText) view.findViewById(R.id.etKPHorary);
        etKpHorary.addTextChangedListener(new MyTextWatcher(etKpHorary));

        rbMale = (RadioButton) view.findViewById(R.id.radioMale);
        rbFemale = (RadioButton) view.findViewById(R.id.radioFemale);
        rbMale.setOnClickListener(v -> {
            rbMale.setTextColor(getResources().getColor(R.color.black_color));
            rbFemale.setTextColor(getResources().getColor(R.color.text_color_black));
        });
        rbFemale.setOnClickListener(v -> {
            rbFemale.setTextColor(getResources().getColor(R.color.black_color));
            rbMale.setTextColor(getResources().getColor(R.color.text_color_black));
        });
        inputLayoutName = (TextInputLayout) view.findViewById(R.id.input_layout_name);
        inputLayoutKpHorary = (TextInputLayout) view.findViewById(R.id.input_layout_kphorary);

        etName.setFocusable(false);

        saveChart = (CheckBox) view.findViewById(R.id.checkBoxSaveKundli);
        preventSpecialCharInName();
        // Advance Options Code
        ayanamsaOptions = (Spinner) view.findViewById(R.id.ics_spinner_ayan);
        String[] ayanOptions = activity.getResources().getStringArray(
                R.array.ayan_list);
        ArrayAdapter<CharSequence> ayanamsaAdapter = new ArrayAdapter<CharSequence>(activity, R.layout.spinner_list_item,
                ayanOptions) {

            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                // ((TextView) v).setTextColor(Color.WHITE);
                ((TextView) v)
                        .setTypeface(((HomeInputScreen) activity).regularTypeface);
                ((TextView) v).setTextSize(16);
                ((TextView) v).setPadding(10, 0, 10, 0);
                ((TextView) v).setBackgroundColor(getResources().getColor(R.color.white));
                return v;
            }

            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                // ((TextView) v).setTextColor(Color.WHITE);
                ((TextView) v)
                        .setTypeface(((HomeInputScreen) activity).regularTypeface);
                return v;
            }
        };

        etName.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                v.setFocusable(true);
                v.setFocusableInTouchMode(true);
                return false;
            }
        });

        /*
         * R.layout.sherlock_spinner_item);
         * list.setDropDownViewResource(R.layout
         * .sherlock_spinner_dropdown_item);
         */
        ayanamsaOptions.setAdapter(ayanamsaAdapter);

        String[] dstOption = activity.getResources().getStringArray(
                R.array.dst_list);
        dstOptions = (Spinner) view.findViewById(R.id.ics_spinner_dst);
        ArrayAdapter<CharSequence> dstAdapter = new ArrayAdapter<CharSequence>(
                activity, R.layout.spinner_list_item,
                dstOption) {

            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                // ((TextView) v).setTextColor(Color.WHITE);
                ((TextView) v)
                        .setTypeface(((HomeInputScreen) activity).regularTypeface);
                ((TextView) v).setTextSize(16);
                ((TextView) v).setPadding(10, 0, 10, 0);
                ((TextView) v).setBackgroundColor(getResources().getColor(R.color.white));
                return v;
            }

            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                // ((TextView) v).setTextColor(Color.WHITE);
                ((TextView) v)
                        .setTypeface(((HomeInputScreen) activity).regularTypeface);
                return v;
            }
        };
        dstOptions.setAdapter(dstAdapter);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT)//added by neeraj 29/4/16 for show spinner higher virsion
        {
            dstOptions.setPopupBackgroundResource(R.drawable.spinner_dropdown);
            ayanamsaOptions.setPopupBackgroundResource(R.drawable.spinner_dropdown);


        }

        /*
         * //@ Tejinder Singh this listener add for chage the value standard or
         * dst time when manually change by user
         */
        dstOptions.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                if (position == 0) {
                    txtPlacedstDetailContainer.setVisibility(View.GONE);
                    txtPlacedstDetail.setText(activity.getResources().getString(R.string.standard));
                } else {
                    txtPlacedstDetailContainer.setVisibility(View.VISIBLE);
                    txtPlacedstDetail.setTextColor(getResources().getColor(R.color.textcolorchange));
                    txtPlacedstDetail.setText(activity.getResources()
                            .getString(R.string.dsttime));
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub

            }
        });

        otherOptions = (LinearLayout) view
                .findViewById(R.id.layOtherCalcOptions);
        showHideButton = (TextView) view
                .findViewById(R.id.buttonCalculationOptions);
        showHideButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                if (otherOptions.getVisibility() == View.GONE) {
                    otherOptions.setVisibility(View.VISIBLE);
                    showHideButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_birthdetailsetting_minus, 0);
                    /*wholeScreen.post(new Runnable() {
                        public void run() {
                            wholeScreen.scrollTo(0, wholeScreen.getBottom());
                        }
                    });
                    Animation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
                    Animation ScaleAnimation = new ScaleAnimation(0.3f, 1.0f,
                            0.3f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f,
                            Animation.RELATIVE_TO_SELF, 0.5f);
                    ScaleAnimation.setDuration(350);
                    alphaAnimation.setDuration(350);
                    otherOptions.startAnimation(alphaAnimation);
                    otherOptions.startAnimation(ScaleAnimation);*/
                } else {
                    otherOptions.setVisibility(View.GONE);
                    showHideButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_birthdetailsetting_plus, 0);

                    /*Animation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
                    Animation ScaleAnimation = new ScaleAnimation(1.0f, 0.3f,
                            1.0f, 0.3f, Animation.RELATIVE_TO_SELF, 0.5f,
                            Animation.RELATIVE_TO_SELF, 0.5f);
                    ScaleAnimation.setDuration(350);
                    alphaAnimation.setDuration(350);
                    otherOptions.startAnimation(alphaAnimation);
                    otherOptions.startAnimation(ScaleAnimation);
                    // ADDED BY BIJENDRA ON 19-04-14
                    ScaleAnimation
                            .setAnimationListener(new AnimationListener() {

                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationRepeat(
                                        Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    otherOptions.setVisibility(View.GONE);
                                }
                            });

                    // END*/
                }
            }
        });
        // End Advance options code

        TextView textViewPlace = (TextView) view
                .findViewById(R.id.textViewPlace);

        textViewUserName.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                _iBirthDetailInputFragment.openSavedKundli();
            }
        });
        textViewUserName.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int[] posXY = new int[2];
                v.getLocationOnScreen(posXY);
                int x = posXY[0];
                int y = posXY[1];
                float densityConstant = getResources().getDisplayMetrics().density;
                showToolTipInToast("Name", x + ((int) (densityConstant * 30)),
                        y - ((int) (densityConstant * 75)));
                return true;
            }
        });

        /*textViewCalendar.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                _iBirthDetailInputFragment.openCalendar(beanDateTime);

            }
        });*/
        textViewCalendar.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int[] posXY = new int[2];
                v.getLocationOnScreen(posXY);
                int x = posXY[0];
                int y = posXY[1];
                float densityConstant = getResources().getDisplayMetrics().density;
                showToolTipInToast("Birth Date", x
                        + ((int) (densityConstant * 30)), y
                        - ((int) (densityConstant * 75)));
                return true;
            }
        });
        buttonCalendar.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                _iBirthDetailInputFragment.openCalendar(beanDateTime);
            }
        });
        /*textViewTime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                _iBirthDetailInputFragment.openTimePicker(beanDateTime);

            }
        });*/
        /*textViewTime.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int[] posXY = new int[2];
                v.getLocationOnScreen(posXY);
                int x = posXY[0];
                int y = posXY[1];
                //Log.e("tag", x + " " + y);
                float densityConstant = getResources().getDisplayMetrics().density;
                showToolTipInToast("Birth Time", x
                        + ((int) (densityConstant * 30)), y
                        - ((int) (densityConstant * 75)));
                return true;
            }
        });*/

        buttonTime.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                _iBirthDetailInputFragment.openTimePicker(beanDateTime);

            }
        });

        textViewPlace.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                BeanPlace beanPlace = beanHoroPersonalInfo.getPlace();
                if(beanPlace == null){
                    beanPlace = CUtils.getDefaultPlace();
                }
                _iBirthDetailInputFragment.openSearchPlace(beanPlace);
            }
        });
        textViewPlace.setOnLongClickListener(new OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                int[] posXY = new int[2];
                v.getLocationOnScreen(posXY);
                int x = posXY[0];
                int y = posXY[1];
                float densityConstant = getResources().getDisplayMetrics().density;
                showToolTipInToast("Birth Place", x
                        + ((int) (densityConstant * 30)), y
                        - ((int) (densityConstant * 75)));
                return true;
            }
        });
        layPlaceHolder.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                BeanPlace beanPlace = beanHoroPersonalInfo.getPlace();
                if(beanPlace == null){
                    beanPlace = CUtils.getDefaultPlace();
                }
                _iBirthDetailInputFragment.openSearchPlace(beanPlace);
            }
        });

        //applyLanguageFont(view, ((HomeInputScreen) activity).regularTypeface);
        setTypefaceOfView(view);

        buttonCalculateKundli.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                //added by Neeraj 29/03/16

                // String str=CUtils.getApplicationSignatureHashCode(AstrosageKundliApplication.getAppContext());
                BeanHoroPersonalInfo birthDetails = null;
                if (submitForm()) {
                    {
                        // ADDED BY BIJENDRA ON 19-06-14

                        SharedPreferences sharedPreferences = activity
                                .getSharedPreferences(
                                        CGlobalVariables.APP_PREFS_NAME,
                                        Context.MODE_PRIVATE);
                        SharedPreferences.Editor sharedPrefEditor = sharedPreferences
                                .edit();
                        /*
                           This Condition added because KP System ayanams setting option value do not need
                           to apply other section (KP mddule this calculation is diffrent)
                         */
                        if (((HomeInputScreen) activity).SELECTED_MODULE != CGlobalVariables.MODULE_KP) {
                            sharedPrefEditor.putInt(
                                    CGlobalVariables.APP_PREFS_Ayanmasha,
                                    ayanamsaOptions.getSelectedItemPosition());
                        }
                        sharedPrefEditor.commit();
                        // ENd

                        birthDetails = getUserBirthDetailBean();
                    }
                    //if default kundli is updated then dasha detail is get from server
                    BeanHoroPersonalInfo defaultKundli = (BeanHoroPersonalInfo) CUtils.getCustomObject(activity);
                    if (defaultKundli != null && birthDetails != null) {
                        if (birthDetails.getLocalChartId() == -1 && birthDetails.getOnlineChartId().equals("")) {

                        } else {
                            if (!birthDetails.getOnlineChartId().equals("")) {
                                if (birthDetails.getOnlineChartId().equals(defaultKundli.getOnlineChartId())) {
                                    getDashaData(birthDetails);
                                }
                            } else if (birthDetails.getLocalChartId() != -1) {
                                if (birthDetails.getLocalChartId() == defaultKundli.getLocalChartId()) {
                                    getDashaData(birthDetails);
                                }

                            }
                        }
                    }


                    // Added by Amit sharma
                    // checke current Activity
                    if (((HomeInputScreen) activity).SELECTED_MODULE == CGlobalVariables.MODULE_BASIC) {
                        // check dialog to be show or not
//                        if (CUtils.getStringData(activity,
//                                CGlobalVariables.SHOWDIALOGFORPERSONALKUNDALI,
//                                "show").equals("show")) {
//                            selectPersonalKundali(activity);
//
//
//                        } else {
                        //@Gaurav removed dialog for setting default kundli
                            setAnalyticsOnFirstKundli();
                            _iBirthDetailInputFragment.calculateKundli(
                                    birthDetails, saveChart.isChecked());
//                        }
                    } else {
                        setAnalyticsOnFirstKundli();
                        _iBirthDetailInputFragment.calculateKundli(
                                birthDetails, saveChart.isChecked());
                    }


                    CUtils.googleAnalyticSendWitPlayServie(activity,
                            CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                            CGlobalVariables.GOOGLE_ANALYTIC_ACTION_CALCULATE_KUNDLI, null);
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_ACTION_CALCULATE_KUNDLI, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");


                }
            }
        });

        initAynamsha();

        if (isVisibleToUser) {
            if (Integer.valueOf(CUtils.getHelpOptionPref(activity)) < CGlobalVariables.HELP_LIMIT) {
                wholeScreen.fullScroll(View.FOCUS_DOWN);

                new Handler().postDelayed(new Runnable() {

                    @Override
                    public void run() {
                        wholeScreen.scrollTo(0, wholeScreen.getBottom());
                        customeDialogHelp();

                    }
                }, 1000L);

            }
        }
        main_container =  view.findViewById(R.id.main_container);
        etBirthDetails = view.findViewById(R.id.etBirthDetails);
        btnShowInputBirthDet = view.findViewById(R.id.btnShowInputBirthDet);
        btnShowInputBirthDet.setTypeface(((HomeInputScreen) activity).mediumTypeface);
        RelativeLayout getFillDetails = view.findViewById(R.id.getFillDetails);
         llEnterBirthDetails = view.findViewById(R.id.llEnterBirthDetails);
        btnShowInputBirthDet.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if(llEnterBirthDetails.getVisibility()==View.VISIBLE){
                    btnShowInputBirthDet.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_birthdetailsetting_plus, 0);
                    llEnterBirthDetails.setVisibility(View.GONE);
                }else {
                    btnShowInputBirthDet.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_birthdetailsetting_minus, 0);
                    llEnterBirthDetails.setVisibility(View.VISIBLE);
                }
            }
        });
        getFillDetails.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                closeKeyBoard();
                if(com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(activity)){
                    String etBirthDetailsText = etBirthDetails.getText().toString().trim();
                    if (!TextUtils.isEmpty(etBirthDetailsText)) {
                        if(TextUtils.isEmpty(prevBirthDetails)|| !prevBirthDetails.equals(etBirthDetailsText)){
                            prevBirthDetails = etBirthDetailsText;
                            CUtils.fcmAnalyticsEvents(CGlobalVariables.TYPE_PASTE_BIRTH_DETAILS, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                            getBirthDetails();
                        }else {
                            CUtils.showSnackbar(main_container, getResources().getString(R.string.prev_birth_details_are_same), getContext());
                        }
                    } else {
                        CUtils.showSnackbar(main_container, getResources().getString(R.string.plz_enter_birth_details), getContext());
                    }
                }else {
                    displayLoginDialog();
                }

            }
        });
        // call to set DST value on screen load first itme
        setDST();
        viewDummy = view.findViewById(R.id.viewDummy);
        etBirthDetails.setOnFocusChangeListener((view1, hasFocus) -> {
            if (hasFocus) {
                // Keyboard is likely opened
                viewDummy.setVisibility(View.VISIBLE);
            } else {
                viewDummy.setVisibility(View.GONE);
                // Keyboard is likely closed
            }
        });

        return view;
    }

    /**
     *
     */
    private void getBirthDetails() {
        try {
            if (getActivity() instanceof HomeInputScreen) {
                ((HomeInputScreen) getActivity()).showProgressBar();

            }
        } catch (Exception e){
            //
        }


        Call<ResponseBody> call = RetrofitClient.getAIInstance().create(ApiList.class).getBirthDetails(getMessageParams());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try{
                    ((HomeInputScreen)getActivity()).hideProgressBar();
                } catch (Exception e){
                    //
                }
                try {
                    if (response.body() != null) {
                        String myResponse = response.body().string();
                        Log.e("TestUIDetails", "myResponse==>>" + myResponse);
                        //etBirthDetails.setText("Res:- "+myResponse);
                        JSONObject jsonObject = new JSONObject(myResponse);
                        if (jsonObject.getInt("status") == 1) {
                            llEnterBirthDetails.setVisibility(View.GONE);
                            btnShowInputBirthDet.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_birthdetailsetting_plus, 0);
                            setDataInInputField(jsonObject);
                            CUtils.showSnackbar(main_container, getResources().getString(R.string.details_provided_by_you_has_been_filled), getContext());
                        } else if (jsonObject.getInt("status") == -1) {
                            setDataInInputField(jsonObject);
                            //String missingKeys = jsonObject.getString("missingKeys").replace(" ", ",");
                            CUtils.showSnackbar(main_container, getResources().getString(R.string.details_provided_by_you_has_been_filled), getContext());
                        } else if (jsonObject.getInt("status") == -4 || jsonObject.getInt("status") == 0 || jsonObject.getInt("status") == 5) {
                            String message = jsonObject.getString("message");
                            CUtils.showSnackbar(main_container, message, getContext());

                        } else if (jsonObject.getInt("status") == 6) {
                            // String message = jsonObject.getString("message");
                            prevBirthDetails = "";
                            displayLoginDialog();
                        }
                    }
                } catch (Exception e) {
                    //Log.d("TestData", "Ex1--::" + e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                try{
                    ((HomeInputScreen)getActivity()).hideProgressBar();
                } catch (Exception e){
                    //
                }
            }
        });
    }
    private void closeKeyBoard(){
        try {
            //clear focus in BirthDetails Input
            etBirthDetails.clearFocus();
            // Close keyboard
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(main_container.getWindowToken(), 0);
        }catch (Exception e){
            //
        }

    }
    private void displayLoginDialog() {
        CUtils.showSnackbar(main_container, getString(R.string.to_use_this_feature_please_log_in), getContext());
        try {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        Intent intent1 = new Intent(getActivity(), FlashLoginActivity.class);
                        startActivity(intent1);
                    } catch (Exception e){
                        //
                    }
                }
            }, 1000);
        } catch (Exception e) {
            //
        }
    }
    private void setDataInInputField(JSONObject jsonObject) {
        try {
            if(jsonObject.has("presentKeys")) {
                String str = jsonObject.getString("presentKeys");
                ArrayList<String> dataList = new ArrayList<>(Arrays.asList(str.split(",")));
                HashMap<String, String> map = new HashMap<String, String>();
                for (int i = 0; i < dataList.size(); i++) {
                    String singleData = dataList.get(i);
                    ArrayList<String> tempAaryList = new ArrayList<>(Arrays.asList(singleData.split(":")));
                    if (tempAaryList.size() == 2) {
                        map.put(tempAaryList.get(0).trim(), tempAaryList.get(1).trim());
                    }
                }
                if (map.containsKey("name")) {
                    String name = map.get("name");
                    if (!TextUtils.isEmpty(name)) {
                        etName.setText(name.replace("/", "_"));
                    }
                }
                if (map.containsKey("gender")){
                    String gender = map.get("gender");
                    if ((!TextUtils.isEmpty(gender))
                            && gender.trim()
                            .equalsIgnoreCase("male"))
                        rbMale.setChecked(true);
                    else
                        rbFemale.setChecked(true);
                }

                if (map.containsKey("day") && map.containsKey("month")&& map.containsKey("year")) {
                    int day = Integer.parseInt(map.get("day"));
                    int month = Integer.parseInt(map.get("month"));
                    int year = Integer.parseInt(map.get("year"));
                    if (day != 0 && month != 0 && year != 0) {
                        BeanDateTime beanDateTime = new BeanDateTime();
                        beanDateTime.setYear(year);
                        beanDateTime.setMonth(month - 1);
                        beanDateTime.setDay(day);
                        updateBirthDate(beanDateTime);
                    }
                }
                if (map.containsKey("hour") && map.containsKey("minute")) {
                    int hour = Integer.parseInt(map.get("hour"));
                    int minute = Integer.parseInt(map.get("minute"));
                        beanDateTime.setHour(hour);
                        beanDateTime.setMin(minute);
                        beanDateTime.setSecond(0);
                        updateBirthTime(beanDateTime);
                    
                }
            }
            if(jsonObject.has("cityDetails")){
                String[] cityDetails = jsonObject.getString("cityDetails").split("\\|");
                //Log.d("TestUIDetails","cityDetails.length:---"+cityDetails.length);
                if(cityDetails.length > 10){
                    BeanPlace place = new BeanPlace();
                    place.setCityName(cityDetails[0]);
                    place.setCountryName(cityDetails[2]);
                    place.setState(cityDetails[1]);
                    place.setLatDeg(cityDetails[3]);  // Corrected parsing
                    place.setLatMin(cityDetails[4]);  // Fixed latitude minutes
                    //place.setLatNS(cityDetails[5]);
                    place.setLongDeg(cityDetails[6]);
                    place.setLongMin(cityDetails[7]);
                    //place.setLongEW(cityDetails[8]);
                    place.setTimeZone(cityDetails[9]);
                    try {
                        place.setTimeZoneValue(Float.parseFloat(cityDetails[9]));
                    } catch (Exception e){
                        //
                    }
                    updateBirthPlace(place);
                    beanHoroPersonalInfo.setPlace(place);
                }
            }

        } catch (Exception e) {
            //
        }

    }

    private Map<String, String> getMessageParams() {

        Context mContext = AstrosageKundliApplication.getAppContext();
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.APP_KEY, com.ojassoft.astrosage.varta.utils.CUtils.getApplicationSignatureHashCode(mContext));
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.PHONE_NO, com.ojassoft.astrosage.varta.utils.CUtils.getUserID(getActivity()));
        //headers.put("message", "Paritosh (birth details)DoB 26.10.1993ToB 20:53 pmPoB: Bareilly");
        headers.put("message", etBirthDetails.getText().toString().trim());
        headers.put("userid", CUtils.getUserName(mContext));
        headers.put("pw",CUtils.getUserPassword(mContext) );
        headers.put("extradata", "");
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.COUNTRY_CODE, com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(mContext));
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANG, com.ojassoft.astrosage.varta.utils.CUtils.getLanguageKey(LANGUAGE_CODE));
        return com.ojassoft.astrosage.varta.utils.CUtils.setRequiredParams(headers);
    }

    private void getDashaData(BeanHoroPersonalInfo birthDetails) {
        CUtils.saveCustomObject(activity, birthDetails);
        //this service get dasha data for default kundli
        Intent intent = new Intent(activity, GetDefaultKundliDataService.class);
        intent.putExtra("beanHoroPersonalInfo", birthDetails);
        activity.startService(intent);
    }

    // ADDED BY BIJENDRA ON 17-06-14
    private void initKPAyan_OnKpModule_Selection() {
        if (!((HomeInputScreen) activity).isEditKundli) {
            if (((HomeInputScreen) activity).SELECTED_MODULE == CGlobalVariables.MODULE_KP)
                ayanamsaOptions.setSelection(KP_AYAN_NEW);
            else {
                SharedPreferences sharedPreferences = activity
                        .getSharedPreferences(CGlobalVariables.APP_PREFS_NAME,
                                Context.MODE_PRIVATE);
                ayanamsaOptions.setSelection(sharedPreferences.getInt(
                        CGlobalVariables.APP_PREFS_Ayanmasha, 0));
            }

        }
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.Fragment#onStop()
     */
    @Override
    public void onStop() {

        super.onStop();
        // when user leave the activity then it should save user input in bean.
        // CGlobal.getCGlobalObject().setHoroPersonalInfoObject(getUserBirthDetailBean());
    }

    protected void showToolTipInToast(String tips, int x, int y) {
        try {
            if (activity != null) {
                Toast toast = Toast.makeText(activity, tips, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP | Gravity.LEFT, x, y);
                toast.show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void preventSpecialCharInName() {
        // final char[] nonAcceptedChars = new char[]{'_','^','*','-',',', '?',
        // '&', '@',
        // '%','<','>','`','~','!','#','{','}','[',']','\'','\\',';','"','|','=','+'};
        final char[] nonAcceptedChars = new char[]{'?', '&', '<', '>', '\'', '%', ';'};

        InputFilter[] filterArray = new InputFilter[2];
        filterArray[0] = new InputFilter() {
            String s = new String(nonAcceptedChars);

            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                if (end > start) {
                    for (int index = start; index < end; index++) {
                        if (s.contains(String.valueOf(source.charAt(index)))) {
                            return "";
                        }
                    }
                }
                return null;
            }

        };

        filterArray[1] = new InputFilter.LengthFilter(50);

        etName.setFilters(new InputFilter[]{filterArray[0], filterArray[1]});

    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.Fragment#onResume()
     */
    @Override
    public void onResume() {
        super.onResume();
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        CUtils.hideMyKeyboard(getActivity());
        if (fragmentRestorde) {
            updateBirthDetail(CGlobal.getCGlobalObject()
                    .getHoroPersonalInfoObject());
            fragmentRestorde = false;
        }
        //etName.getBackground().setColorFilter(null);//getResources().getColor(R.color.ColorPrimary), PorterDuff.Mode.SRC_ATOP);
        inputLayoutName.setHintEnabled(false);
        //inputLayoutName.setErrorEnabled(false);
        //etKpHorary.getBackground().setColorFilter(null);//getResources().getColor(R.color.ColorPrimary), PorterDuff.Mode.SRC_ATOP);
        inputLayoutKpHorary.setHintEnabled(false);
        //inputLayoutKpHorary.setErrorEnabled(false);

    }

    private void initAynamsha() {
        SharedPreferences sharedPreferences = activity
                .getSharedPreferences(CGlobalVariables.APP_PREFS_NAME,
                        Context.MODE_PRIVATE);
        ayanamsaOptions.setSelection(sharedPreferences.getInt(
                CGlobalVariables.APP_PREFS_Ayanmasha, 0));

        initKPAyan_OnKpModule_Selection();// ADDED BY BIJENDRA ON 17-06-14
    }

    /*private void applyLanguageFont(View view, Typeface typeface) {

    }
*/
    protected boolean validForm() {
        boolean valid = true;
        if (etName.getText().toString().trim().length() == 0) {
            valid = false;


            etName.setError(getResources().getString(R.string.please_enter_name_v));
            MyCustomToast mct = new MyCustomToast(activity, activity
                    .getLayoutInflater(), activity,
                    ((HomeInputScreen) activity).regularTypeface);
            mct.show(getResources().getString(R.string.please_enter_name));
            wholeScreen.post(new Runnable() {
                public void run() {
                    wholeScreen.scrollTo(0, 0);
                }
            });
        }
        if (etKpHorary.getText().toString().trim().length() > 0) {
            if (Integer.parseInt(etKpHorary.getText().toString().trim()) > 249
                    || Integer.parseInt(etKpHorary.getText().toString().trim()) < 1) {
                valid = false;
                if (otherOptions.getVisibility() == View.GONE)
                    otherOptions.setVisibility(View.VISIBLE);
                MyCustomToast mct = new MyCustomToast(activity,
                        activity.getLayoutInflater(), activity,
                        ((HomeInputScreen) activity).regularTypeface);
                mct.show(getResources().getString(
                        R.string.please_enter_number_between_1_249));
                etKpHorary.setError(getResources().getString(
                        R.string.please_enter_number_between_1_249_v1));
            }
        }
        return valid;
    }

    protected BeanHoroPersonalInfo getUserBirthDetailBean() {

        if (this.beanHoroPersonalInfo == null)
            this.beanHoroPersonalInfo = new BeanHoroPersonalInfo();
        this.beanHoroPersonalInfo.setName(etName.getText().toString().trim());
        this.beanHoroPersonalInfo.setPlace(beanPlace);
        this.beanHoroPersonalInfo.setDateTime(beanDateTime);
        if (rbMale.isChecked())
            this.beanHoroPersonalInfo.setGender("M");
        else
            this.beanHoroPersonalInfo.setGender("F");
        /*
         * if (rbSelf.isChecked()) selfKundali = true; else selfKundali = false;
         */
        int KpHorarayNumver = 0;
        if (etKpHorary.getText().toString().trim().length() > 0) {
            KpHorarayNumver = Integer.parseInt(etKpHorary.getText().toString()
                    .trim());
        }
        this.beanHoroPersonalInfo.setHoraryNumber(KpHorarayNumver);
        this.beanHoroPersonalInfo.setAyanIndex(ayanamsaOptions
                .getSelectedItemPosition());
        this.beanHoroPersonalInfo.setDST(dstOptions.getSelectedItemPosition());
        return this.beanHoroPersonalInfo;
    }

    protected void setUserBirthDetailBean(BeanHoroPersonalInfo beanHoroPersonalInfo) {
        this.beanHoroPersonalInfo = beanHoroPersonalInfo;
        String name = beanHoroPersonalInfo.getName();
        if(!TextUtils.isEmpty(name)) {
            etName.setText(name.replace("/", "_"));
        }
        beanPlace = beanHoroPersonalInfo.getPlace();
        if(beanPlace != null) {
            txtPlaceName.setText(beanPlace.getCityName().trim());
            txtPlaceDetail.setText(CUtils.getPlaceDetailInSingleString(beanPlace));
        }else {
            beanPlace = CUtils.getDefaultPlace();
            isOpenKundliPlaceEmpty = true;
            txtPlaceName.setText(activity.getString(R.string.city_not_found));
            txtPlaceDetail.setText(activity.getString(R.string.select_city));
        }

        beanDateTime = beanHoroPersonalInfo.getDateTime();
        buttonCalendar.setText(getFormatedTextToShowDate(beanDateTime));
        /*set kundli date in newKundliSelectedDate*/
        if (buttonCalendar.toString().contains("-")) {
            int monthInCount = 0;
            String[] getYear = buttonCalendar.getText().toString().split("-");
            CUtils.setNewKundliSelectedDate(Integer.parseInt(getYear[2].trim()));
            String[] shortMonth = getResources().getStringArray(R.array.month_short_name_list);
            for (int i = 0; i < shortMonth.length; i++) {
                if (shortMonth[i].equalsIgnoreCase(getYear[1].trim())) {
                    monthInCount = i;
                    break;
                }
            }
            CUtils.setNewKundliSelectedMonth(monthInCount);
        }
        buttonTime.setText(CUtils.getFormatedTextToShowTime(beanDateTime));
        if ((beanHoroPersonalInfo.getGender().trim().equalsIgnoreCase("M"))
                || beanHoroPersonalInfo.getGender().trim()
                .equalsIgnoreCase("Male"))
            rbMale.setChecked(true);
        else
            rbFemale.setChecked(true);

        if ((beanHoroPersonalInfo.getHoraryNumber() > 0)
                && (beanHoroPersonalInfo.getHoraryNumber() <= 249)) {
            etKpHorary.setText(String.valueOf(beanHoroPersonalInfo
                    .getHoraryNumber()));
        } else {
            etKpHorary.setText("");
        }
        ayanamsaOptions.setSelection(beanHoroPersonalInfo.getAyanIndex());
        dstOptions.setSelection(beanHoroPersonalInfo.getDST());

        if (beanHoroPersonalInfo.getDST() >= 1) {
            txtPlacedstDetailContainer.setVisibility(View.VISIBLE);
            txtPlacedstDetail.setTextColor(getResources().getColor(R.color.textcolorchange));
            txtPlacedstDetail.setText(activity.getResources().getString(
                    R.string.dsttime));
        } else {
            txtPlacedstDetailContainer.setVisibility(View.GONE);
            txtPlacedstDetail.setText(activity.getResources().getString(R.string.standard));
        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        _iBirthDetailInputFragment = (IBirthDetailInputFragment) activity;
        this.activity = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        _iBirthDetailInputFragment = null;
        activity = null;
    }

    public Context getACT() {
        return activity;
    }

    public void updateBirthPlace(BeanPlace beanPlace) {
        isOpenKundliPlaceEmpty = false;
        this.beanPlace = beanPlace;
        this.beanHoroPersonalInfo.setPlace(beanPlace);
        txtPlaceName.setText(beanPlace.getCityName().trim());
        txtPlaceDetail.setText(CUtils
                .getPlaceDetailInSingleString(beanHoroPersonalInfo.getPlace()));

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        CUtils.hideMyKeyboard(getActivity());
        // when time change set again dst or standard Enter city name
        setDST();
    }

    /*
     * take Date object from String and pass it to Pachang util class
     *
     * @ Tejinder Singh
     *
     * @ take parameter date as String
     *
     * @ Return Date object
     */
    public Date getStringtoDateObject(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy",
                Locale.US);
        String dateInString = date;
        Date dateReturn = null;

        try {
            dateReturn = formatter.parse(dateInString);
            System.out.println(dateReturn);
            System.out.println(formatter.format(dateReturn));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateReturn;
    }

    /*
     * @param timeZoneName takename of timezone(TIMEZONESTRING)
     *
     * @param dateobj date object taken
     *
     * @return return is it dst or not
     */
    public boolean isDSTTimeEligible(String timeZoneName, Date dateobj) {
        boolean dstValue = false;
        PanchangUtil objPanchangUtil;
        objPanchangUtil = new PanchangUtil();
        dstValue = objPanchangUtil.isDst(timeZoneName, dateobj);
        return dstValue;
    }

    public void updateAynamshaFromPrfrenceScreen(int ayanamshaIndex) {
        ayanamsaOptions.setSelection(ayanamshaIndex);
    }

    public void updateBirthDetail(BeanHoroPersonalInfo beanHoroPersonalInfo) {

        setUserBirthDetailBean(beanHoroPersonalInfo);
    }

    public void updateBirthDate(BeanDateTime beanDateTime) {
        try {
            if (this.beanDateTime == null) {
                this.beanDateTime = new BeanDateTime();
            }
            this.beanDateTime.setYear(beanDateTime.getYear());
            this.beanDateTime.setMonth(beanDateTime.getMonth());
            this.beanDateTime.setDay(beanDateTime.getDay());
            buttonCalendar.setText(getFormatedTextToShowDate(beanDateTime));
            /*set kundli date in newKundliSelectedDate*/
            if (buttonCalendar.toString().contains("-")) {
                int monthInCount = 0;
                String[] getYear = buttonCalendar.getText().toString().split("-");
                CUtils.setNewKundliSelectedDate(Integer.parseInt(getYear[2].trim()));
                String[] shortMonth = getResources().getStringArray(R.array.month_short_name_list);
                for (int i = 0; i < shortMonth.length; i++) {
                    if (shortMonth[i].equalsIgnoreCase(getYear[1].trim())) {
                        monthInCount = i;
                        break;
                    }
                }
                CUtils.setNewKundliSelectedMonth(monthInCount + 1);
            }
            // when time is updated dst is set
            setDST();
            updateLocationTimeZone();
        }catch (Exception e){
            //
        }
    }

    private void updateLocationTimeZone() {

        if (beanPlace != null && beanPlace.getCountryName() != null && beanPlace.getCountryName().trim().equalsIgnoreCase("Nepal")) {

            if (CUtils.getNewKundliSelectedDate() <= 1985) {
                beanPlace.setTimeZoneName("GMT+5.5");
                beanPlace.setTimeZone("5.5");
                beanPlace.setTimeZoneValue(Float.parseFloat("5.5"));
            } else {
                beanPlace.setTimeZoneName("GMT+5.75");
                beanPlace.setTimeZone("5.75");
                beanPlace.setTimeZoneValue(Float.parseFloat("5.75"));
            }
        }
        if (beanPlace != null && beanPlace.getCountryName() != null && beanPlace.getCountryName().trim().equalsIgnoreCase("Suriname")) {
            if (CUtils.getNewKundliSelectedDate() <= 1984) {
                if (CUtils.getNewKundliSelectedDate() == 1984 &&
                        CUtils.getNewKundliSelectedMonth() > 9) {
                    beanPlace.setTimeZoneName("GMT-3.0");
                    beanPlace.setTimeZone("-3.0");
                    beanPlace.setTimeZoneValue(Float.parseFloat("-3.0"));
                } else {
                    beanPlace.setTimeZoneName("GMT-3.5");
                    beanPlace.setTimeZone("-3.5");
                    beanPlace.setTimeZoneValue(Float.parseFloat("-3.5"));
                }
            } else {
                beanPlace.setTimeZoneName("GMT-3.0");
                beanPlace.setTimeZone("-3.0");
                beanPlace.setTimeZoneValue(Float.parseFloat("-3.0"));
            }
        }
        txtPlaceName.setText(beanPlace.getCityName().trim());
        txtPlaceDetail.setText(CUtils.getPlaceDetailInSingleString(beanPlace));
    }

    // @Tejinder Singh
    // this method to set value for dst
    public void setDST() {
        String dateString = getFormatedTextToShowDateToParse(beanDateTime);
        Date date = getStringtoDateObject(dateString);
        if (beanPlace.getTimeZoneString() != null) {
            boolean isDST = isDSTTimeEligible(beanPlace.getTimeZoneString(),
                    date);
            if (isDST) {
                dstOptions.setSelection(1);
                txtPlacedstDetailContainer.setVisibility(View.VISIBLE);

                txtPlacedstDetail.setTextColor(getResources().getColor(R.color.textcolorchange));
                txtPlacedstDetail.setText(activity.getResources()
                        .getString(R.string.dsttime));
            } else {
                dstOptions.setSelection(0);
                txtPlacedstDetailContainer.setVisibility(View.GONE);
                txtPlacedstDetail.setText(activity.getResources().getString(R.string.standard));
            }
        }
    }

    public void updateBirthTime(BeanDateTime beanDateTime) {
        this.beanDateTime.setHour(beanDateTime.getHour());
        this.beanDateTime.setMin(beanDateTime.getMin());
        this.beanDateTime.setSecond(beanDateTime.getSecond());

        buttonTime.setText(CUtils.getFormatedTextToShowTime(beanDateTime));
    }

    public void resetBirthDetailForm() {

        BeanHoroPersonalInfo personInfo = new BeanHoroPersonalInfo();
        personInfo.setPlace(CUtils.getUserDefaultCity(activity));
        setUserBirthDetailBean(personInfo);
        initAynamsha();// ADDED BY BIJENDRA ON 17-06-14
        setDST();  //Added By Tejinder 01-02-16
    }

    private String getFormatedTextToShowDate(BeanDateTime beanDateTime) {
        String strDateTime = null;
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
                "Aug", "Sep", "Oct", "Nov", "Dec"};
        strDateTime = CUtils.pad(beanDateTime.getDay()) + " - "
                + months[beanDateTime.getMonth()] + " - "
                + beanDateTime.getYear();
        return strDateTime;
    }

    // take string type date to convet Date object
    // @Tejinder Singh
    private String getFormatedTextToShowDateToParse(BeanDateTime beanDateTime) {
        String strDateTime = null;
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
                "Aug", "Sep", "Oct", "Nov", "Dec"};
        strDateTime = CUtils.pad(beanDateTime.getDay()) + "-"
                + months[beanDateTime.getMonth()] + "-"
                + beanDateTime.getYear();
        return strDateTime;
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onActivityCreated(savedInstanceState);
        _iBirthDetailInputFragment.birthDetailInputFragmentCreated();
    }

    public void oneChartDeleted_IfThatIsCurrentOneThenDeleteChartIdFromIt(
            long chartId, boolean isOnline) {
        String deletedChartId = String.valueOf(chartId);
        if (isOnline) {
            if (this.beanHoroPersonalInfo.getOnlineChartId().trim()
                    .equalsIgnoreCase(deletedChartId)) {
                this.beanHoroPersonalInfo.setOnlineChartId("");
            }
        } else {
            if (this.beanHoroPersonalInfo.getLocalChartId() == chartId) {
                this.beanHoroPersonalInfo.setLocalChartId(-1);
            }
        }
    }

    /**
     * This method open a dialog for helping user
     */

    private void customeDialogHelp() {

        Animation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
        Animation ScaleAnimation = new ScaleAnimation(0.1f, 1.0f, 0.1f, 1.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        ScaleAnimation.setDuration(500);
        alphaAnimation.setDuration(500);

        dialog = new Dialog(activity);
        LayoutInflater inflator = (LayoutInflater) activity
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflator.inflate(R.layout.lay_option_help, null, false);

        TextView textHelp;
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        dialog.setContentView(view);
        dialog.setCancelable(true);
        view.startAnimation(alphaAnimation);
        view.startAnimation(ScaleAnimation);
        textHelp = (TextView) dialog.findViewById(R.id.texthelp);
        textHelp.setTypeface(((HomeInputScreen) activity).regularTypeface);
        textHelp.setText(getResources().getString(R.string.option_help));
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        ((RelativeLayout) dialog.findViewById(R.id.layoutHelpParent))
                .setOnTouchListener(new OnTouchListener() {

                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        dialog.dismiss();
                        return true;
                    }
                });
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.MATCH_PARENT;
        dialog.show();
        dialog.getWindow().setAttributes(lp);

        addOptionHelp(view);

        String number = CUtils.getHelpOptionPref(activity);
        CUtils.saveHelpOptionPref(activity,
                String.valueOf(Integer.valueOf(number) + 1));
    }

    private void addOptionHelp(View view) {

        int[] posXY = new int[2];
        int rightMargin = 25;
        showHideButton.getLocationOnScreen(posXY);
        int left = showHideButton.getLeft();
        int right = showHideButton.getRight();

        int y = posXY[1];
        float x = (left + right) / 2;

        int additionalValue = 0;

        double density = getResources().getDisplayMetrics().density;
        if (density >= 4.0) {
            additionalValue = additionalValue + 25;
            //xxxhdpi
        } else if (density >= 3.5 && density < 4.0) {
            additionalValue = additionalValue + 25;
        } else if (density >= 3.0 && density < 3.5) {
            additionalValue = additionalValue + 15;
        } else if (density >= 2.0 && density < 3.0) {
            additionalValue = additionalValue - 5;
            //xhdpi
        } else if (density >= 1.5 && density < 2.0) {
            additionalValue = additionalValue - 5;
            //hdpi
        } else if (density >= 1.0 && density < 1.5) {
            additionalValue = additionalValue - 5;
            //mdpi
        }
        //additionalValue=(int)getResources().getDimension(R.dimen.helP_top_marging);

        // int width = (int)
        // ((HomeInputScreen)getActivity()).SCREEN_CONSTANTS.DeviceScreenWidth;

        LinearLayout llhelp = (LinearLayout) view
                .findViewById(R.id.layoutOptionHelp);
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) llhelp
                .getLayoutParams();
        layoutParams
                .setMargins(
                        0,
                        y + additionalValue,
                        rightMargin,
                        0);

        llhelp.setLayoutParams(layoutParams);

    }

    public void selectPersonalKundali(final Context context) {
        FragmentManager fm = getActivity().getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();
        Fragment prev = fm.findFragmentByTag("SelectPersonalKundliDialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        SelectPersonalKundliDialog selectPersonalKundliDialog = SelectPersonalKundliDialog.getInstance();
        selectPersonalKundliDialog.setTargetFragment(BirthDetailInputFragment.this, 0);
        selectPersonalKundliDialog.show(fm, "SelectPersonalKundliDialog");
        ft.commit();

    }

    private void setTypefaceOfView(View view) {
        // etName.setTypeface(((HomeInputScreen) activity).mediumTypeface);
        buttonCalendar.setTypeface(((HomeInputScreen) activity).robotMediumTypeface);
        buttonTime.setTypeface(((HomeInputScreen) activity).robotMediumTypeface);
        txtPlaceName.setTypeface(((HomeInputScreen) activity).robotMediumTypeface);
        txtPlaceDetail.setTypeface(((HomeInputScreen) activity).robotMediumTypeface);
        txtPlacedstDetail.setTypeface(((HomeInputScreen) activity).mediumTypeface);
        rbMale.setTypeface(((HomeInputScreen) activity).mediumTypeface);
        buttonCalculateKundli.setText(activity.getResources().getString(R.string.get_horoscope));
        buttonCalculateKundli.setTypeface(((HomeInputScreen) activity).mediumTypeface);
        if ((activity instanceof HomeInputScreen) && ((((HomeInputScreen) activity).ASK_QUESTION_QUERY_DATA) || (((HomeInputScreen) activity).ASTROSAGE_CHAT_QUERY_DATA)) || (((HomeInputScreen) activity).ASTRO_SERVICE_QUERY_DATA) || (((HomeInputScreen) activity).ASTRO_PRODUCT_DATA) || (((HomeInputScreen) activity).NUMROLOGY_QUERY_DATA) || (((HomeInputScreen) activity).VARTA_PROFILE_QUERY_DATA) || (((HomeInputScreen) activity).BHRIGOO_QUERY_DATA)) {
            buttonCalculateKundli.setText(activity.getResources().getString(R.string.select_profile_text));
            if (((AstrosageKundliApplication) activity.getApplication())
                    .getLanguageCode() == CGlobalVariables.ENGLISH) {
                buttonCalculateKundli.setText(activity.getResources().getString(R.string.select_profile_text).toUpperCase());
            }
        } else if (((AstrosageKundliApplication) activity.getApplication())
                .getLanguageCode() == CGlobalVariables.ENGLISH) {
            buttonCalculateKundli.setText(getResources().getString(R.string.get_horoscope).toUpperCase());
        }

        rbFemale.setTypeface(((HomeInputScreen) activity).mediumTypeface);
        saveChart.setTypeface(((HomeInputScreen) activity).mediumTypeface);
        showHideButton.setTypeface(((HomeInputScreen) activity).mediumTypeface);

        ((TextView) view.findViewById(R.id.textViewHeadingOtherCalc))
                .setTypeface(((HomeInputScreen) activity).mediumTypeface);
        ((TextView) view.findViewById(R.id.textViewKPHorary))
                .setTypeface(((HomeInputScreen) activity).mediumTypeface);
        ((TextView) view.findViewById(R.id.textViewAyan)).setTypeface(((HomeInputScreen) activity).mediumTypeface);
        ((TextView) view.findViewById(R.id.textViewDst)).setTypeface(((HomeInputScreen) activity).mediumTypeface);


    }

    @Override
    public void onYesClick() {

        setAnalyticsByAge();

        _iBirthDetailInputFragment.calculateKundli(
                getUserBirthDetailBean(), saveChart.isChecked());
        CUtils.saveStringData(getActivity(),
                CGlobalVariables.SHOWDIALOGFORPERSONALKUNDALI,
                "notshow");
        // CUtils.saveCustomObject(context, getUserBirthDetailBean());
        CalculateKundli.isPersonalKundli = true;
        Intent intent = new Intent(getActivity(), GetDefaultKundliDataService.class);
        intent.putExtra("beanHoroPersonalInfo", beanHoroPersonalInfo);
        getActivity().startService(intent);
        Log.e("trackCall", "on yes click" );
//        CUtils.BirthdayNotifications(activity,
//                CGlobalVariables.BIRTHDAY_INTENT_ACTION, 50);
    }



    @Override
    public void onNoClick() {
        setAnalyticsOnFirstKundli();
        _iBirthDetailInputFragment.calculateKundli(
                getUserBirthDetailBean(), saveChart.isChecked());
        CUtils.saveStringData(getActivity(),
                CGlobalVariables.SHOWDIALOGFORPERSONALKUNDALI,
                "notshow");
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.etUserName:
                    if (HomeInputScreen.drawerNewKundliOptionClicked) {
                        HomeInputScreen.drawerNewKundliOptionClicked = false;
                        inputLayoutName.setError(null);
                        inputLayoutName.setErrorEnabled(false);
                    } else {
                        validateName(etName);
                    }
                    break;
                case R.id.etKPHorary:
                    validateName(etKpHorary);

            }
        }
    }

    private boolean validateName(EditText text) {
        boolean flag = true;

        if (text == etName) {
            if (text.getText().toString().trim().isEmpty()) {
                // inputLayoutName.setHintEnabled(true);
                inputLayoutName.setErrorEnabled(true);
                inputLayoutName.setError(getString(R.string.please_enter_name_v));
                requestFocus(text);
                text.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                wholeScreen.post(new Runnable() {
                    public void run() {
                        wholeScreen.scrollTo(0, 0);
                    }
                });
                flag = false;
            } else if (isSpecialCharFound(text.getText().toString().trim())) {
                // inputLayoutName.setHintEnabled(true);
                inputLayoutName.setErrorEnabled(true);
                inputLayoutName.setError(getString(R.string.please_enter_valid_name_v));
                requestFocus(text);
                text.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                wholeScreen.post(new Runnable() {
                    public void run() {
                        wholeScreen.scrollTo(0, 0);
                    }
                });
                flag = false;
            } else {
                // inputLayoutName.setError(null);
                // etName.getBackground().setColorFilter(getResources().getColor(R.color.ColorPrimary), PorterDuff.Mode.SRC_ATOP);
                inputLayoutName.setErrorEnabled(false);
                inputLayoutName.setError(null);
                text.getBackground().setColorFilter(getResources().getColor(R.color.ColorPrimary), PorterDuff.Mode.SRC_ATOP);
            }
        }
        if (text == etKpHorary) {
            try {
                if ((text.getText().toString().trim().length() > 0) && (Integer.parseInt(text.getText().toString().trim()) > 249 || Integer.parseInt(text.getText().toString().trim()) < 1)) {
                    if (otherOptions.getVisibility() == View.GONE)
                        otherOptions.setVisibility(View.VISIBLE);
                    inputLayoutKpHorary.setErrorEnabled(true);
                    inputLayoutKpHorary.setError(getString(R.string.please_enter_number_between_1_249_v1));
                    requestFocus(text);
                    text.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);

                    flag = false;
                } else {
                    inputLayoutKpHorary.setErrorEnabled(false);
                    inputLayoutKpHorary.setError(null);
                    text.getBackground().setColorFilter(getResources().getColor(R.color.ColorPrimary), PorterDuff.Mode.SRC_ATOP);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return flag;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        }
    }

    private boolean submitForm() {
        boolean flag = false;
        if (validateName(etName) && validateName(etKpHorary)) {
            flag = true;
        }
        if(isOpenKundliPlaceEmpty){
            MyCustomToast mct = new MyCustomToast(activity, activity.getLayoutInflater(), activity, ((HomeInputScreen) activity).regularTypeface);
            mct.show(getResources().getString(R.string.select_city));
            flag = false;
        }

        return flag;

        // Toast.makeText(getApplicationContext(), "Thank You!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (activity != null && isVisibleToUser) {
            this.isVisibleToUser = isVisibleToUser;
            CUtils.hideMyKeyboard(getActivity());
            View appbarAppModule = getActivity().findViewById(R.id.appbarAppModule);
            appbarAppModule.setVisibility(View.VISIBLE);
            getActivity().getWindow().clearFlags(
                    WindowManager.LayoutParams.FLAG_FULLSCREEN);

        }
    }

    /**
     * This method check the special symbol exist or not
     *
     * @param name
     * @return true if special symbol exist otherwise false
     */

    private boolean isSpecialCharFound(String name) {
        Pattern regex = Pattern.compile("[$&+~,:;=\\\\?@#|/'<>.^*()%!-]");

        if (regex.matcher(name).find()) {
            return true;
        }
        return false;
    }

    private void setAnalyticsOnFirstKundli(){
        int isLocalKundliAvailable = CUtils.isLocalKundliAvailable(getActivity());
        if(isLocalKundliAvailable == 1){ //i.e, local kundli not available
            setAnalyticsByAge();
        }
    }

    private void setAnalyticsByAge(){
        try {
            BeanHoroPersonalInfo beanHoroPersonalInfo = getUserBirthDetailBean();
            BeanDateTime beanDateTime = beanHoroPersonalInfo.getDateTime();
            int year = beanDateTime.getYear();
            int month = beanDateTime.getMonth();
            int day = beanDateTime.getDay();
            com.ojassoft.astrosage.varta.utils.CUtils.setFcmAnalyticsByAge(year, month, day);
        }catch (Exception e){
            //
        }
    }
}