package com.ojassoft.astrosage.varta.ui.activity;

import static com.ojassoft.astrosage.utils.CGlobalVariables.CLICKED_CATEGORY_ENUM_KEY;
import static com.ojassoft.astrosage.utils.CGlobalVariables.IS_OPENED_FROM_K_AI_CHAT_BTN;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_KUNDALI_DETAILS;
import static com.ojassoft.astrosage.utils.CUtils.getUserBirthDetailBean;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.CALL_BTN_CLICKED;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHAT_BTN_CLICKED;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.REQUEST_CODE_SELECT_KUNDALI;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.SELECTED_KUNDLI_PROFILE;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.SUB_FRAGMENT_ADD_PROFILE;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.UnderlineSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.misc.CustomDatePicker;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.act.HomeInputScreen;
import com.ojassoft.astrosage.ui.fragments.ReportsFragment;
import com.ojassoft.astrosage.utils.CGlobal;
import com.ojassoft.astrosage.varta.interfacefile.IBirthDetailInputFragment;
import com.ojassoft.astrosage.varta.model.BeanDateTime;
import com.ojassoft.astrosage.varta.model.BeanPlace;
import com.ojassoft.astrosage.varta.model.UserProfileData;
import com.ojassoft.astrosage.varta.service.Loginservice;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.CustomProgressDialog;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.varta.utils.MyTimePickerDialog;
import com.ojassoft.astrosage.varta.utils.TimePicker;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import org.json.JSONObject;
import org.shadow.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * This activity is responsible for collecting and managing the user's profile information,
 * which is essential for astrological consultations. It allows users to input their name, gender,
 * date and time of birth, place of birth, marital status, and occupation.
 * <p>
 * The activity can be launched in different contexts, such as before starting a chat or call
 * with an astrologer, or when the user wants to edit their profile. It pre-fills the fields
 * with existing data if available and provides options to select from saved Kundlis or
 * enter new details.
 * <p>
 * The collected data is validated and then saved locally and sent to the server to ensure
 * that the user's profile is always up-to-date.
 */
public class ProfileForChat extends AppCompatActivity implements View.OnClickListener, VolleyResponse {

    EditText etFullName;
    RelativeLayout rlGender, rlPlacePicker, rlDatePicker, rlTimePicker;
    TextView tvPlacePicker, tvDatePicker, tvTimePicker, tvFullName, tvGender,
            tvPlaceOfBirth, tvDateOfBirth, tvTimeOfBirth, headingTxt, subheadingTxt, tvMaritalStatus, tvOccupation;
    Button proceed_btn;
    TextView cancel_btn;
    View view;
    ScrollView scroll_view;
    private CustomDatePicker datePickerDialog;
    private TimePickerDialog mTimePicker;
    private boolean isTimeSelected = false;
    private boolean isDateSelected = false;
    private int year;
    private int month;
    private int dayOfMonth;
    private Calendar calendar;
    UserProfileData userProfileData = null;
    private BeanPlace mBeanPlace = null;
    IBirthDetailInputFragment _iBirthDetailInputFragment;
    BeanDateTime beanDateTime = new BeanDateTime(false,false);
    BeanDateTime beanTimeIfIssue;
    Spinner tvGenderSpinner, tvMaritalStatusSpinner, tvOccupationSpinner;
    LinearLayout mainlayout;
    String[] genderOptions;
    String[] maritalStatusOptions;
    String[] occupationOptions;
    String[] genderOptionsKey = new String[]{"NotSpecified", "M", "F"};
    String[] maritalStatusOptionsKey = new String[]{"NotSpecified","Single","Married","Divorced","In a Relationship", "Complicated", "Widowed"};
    String[] occupationOptionsKey = new String[]{"NotSpecified", "Student", "Businessperson", "Employee", "Retired", "Housewife"};

    RequestQueue queue;
    CustomProgressDialog pd;
    LinearLayout selectKundliLL;
    TextView selectKundliTV;
    TextView skip_text;
    LinearLayout rlSkip;
    Activity activity;
    String phoneNo = "";
    String urlText = "", fromWhereData = "";
    private int METHOD_GET_PROFILE = 2;
    int selectedGender = -1, selectedMaritalStatus = -1, selectedOccupation = -1;
    private Bundle bundle;
    private boolean prefillData;
    private String clicked_category_enum;
    private boolean is_opened_from_kundli_ai_chat_btn = false;
    String iName = "", iDob = "", iTime = "", iPlace = "";
    int iGenderPos = 0, iMaritalPos = 0, iOccupationPos = 0;
    boolean isFromAiHoroscope = false;

    /**
     * Initializes the activity, setting up the user interface and initial state.
     * This method is called when the activity is first created. It sets the content view,
     * initializes all the UI components, and sets up the initial configuration for the activity.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     *                           Otherwise, it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        LANGUAGE_CODE = AstrosageKundliApplication.getAppContext().getLanguageCode();
        CUtils.getRobotoFont(ProfileForChat.this, LANGUAGE_CODE, CGlobalVariables.regular);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setContentView(R.layout.activity_profile_for_chat);
        //setFinishOnTouchOutside(false);
        activity = ProfileForChat.this;
        initView();
    }

    /**
     * Initializes all the UI components of the activity.
     * This method finds and assigns all the views from the layout file to their corresponding variables.
     * It also sets up the initial state of the views, such as fonts, click listeners, and visibility,
     * and retrieves any data passed to the activity through the intent.
     */
    public void initView() {

        //Log.e("PROFILE DASH", "PROFILE FRAG ");
        rlGender = findViewById(R.id.rl_gender);
        rlPlacePicker = findViewById(R.id.rl_place_picker);
        rlDatePicker = findViewById(R.id.rl_date_picker);
        rlTimePicker = findViewById(R.id.rl_time_picker);
        etFullName = findViewById(R.id.et_full_name);
//        InputFilter[] existingFilters = etFullName.getFilters();
//        if (existingFilters == null) {
//            existingFilters = new InputFilter[0];
//        }
//        InputFilter[] combinedFilters = Arrays.copyOf(existingFilters, existingFilters.length + 1);
//        combinedFilters[existingFilters.length] = new EmojiInputFilter();
//        etFullName.setFilters(combinedFilters);
        tvFullName = findViewById(R.id.tv_full_name);
        headingTxt = findViewById(R.id.heading_txt);
        subheadingTxt = findViewById(R.id.subheading_txt);
        tvGenderSpinner = findViewById(R.id.tv_gender_txt);
        tvDatePicker = findViewById(R.id.tv_date_picker);
        tvGender = findViewById(R.id.tv_gender);

        tvDatePicker = findViewById(R.id.tv_date_picker);
        tvTimePicker = findViewById(R.id.tv_time_picker);
        tvDateOfBirth = findViewById(R.id.tv_date_of_birth);
        tvTimeOfBirth = findViewById(R.id.tv_time_of_birth);

        tvMaritalStatusSpinner = findViewById(R.id.tv_marital_status_txt);
        tvOccupationSpinner = findViewById(R.id.tv_occupation_txt);
        tvOccupation = findViewById(R.id.tv_occupation);
        tvMaritalStatus = findViewById(R.id.tv_marital_status);

        tvPlaceOfBirth = findViewById(R.id.tv_place_of_birth);
        tvPlacePicker = findViewById(R.id.tv_place_picker);
        mainlayout = findViewById(R.id.mainlayout);

        scroll_view = findViewById(R.id.scroll_view);
        cancel_btn = findViewById(R.id.cancel_btn);
        proceed_btn = findViewById(R.id.proceed_btn);
        skip_text = findViewById(R.id.skip_text);
        selectKundliTV = findViewById(R.id.selectKundliTV);
        selectKundliLL = findViewById(R.id.selectKundliLL);
        rlSkip = findViewById(R.id.skip_rl);

        String proceedText = getResources().getString(R.string.proceed);
        String cancelText = getResources().getString(R.string.cancel);

        FontUtils.changeFont(ProfileForChat.this, selectKundliTV, CGlobalVariables.FONTS_POPPINS_SEMI_BOLD);
        FontUtils.changeFont(ProfileForChat.this, skip_text, CGlobalVariables.FONTS_POPPINS_SEMI_BOLD);
        FontUtils.changeFont(ProfileForChat.this, etFullName, CGlobalVariables.FONTS_POPPINS_SEMI_BOLD);
        FontUtils.changeFont(ProfileForChat.this, tvDatePicker, CGlobalVariables.FONTS_POPPINS_SEMI_BOLD);
        FontUtils.changeFont(ProfileForChat.this, tvTimePicker, CGlobalVariables.FONTS_POPPINS_SEMI_BOLD);
        FontUtils.changeFont(ProfileForChat.this, tvPlacePicker, CGlobalVariables.FONTS_POPPINS_SEMI_BOLD);
        FontUtils.changeFont(ProfileForChat.this, tvFullName, CGlobalVariables.FONTS_POPPINS_LIGHT);
        FontUtils.changeFont(ProfileForChat.this, tvGender, CGlobalVariables.FONTS_POPPINS_LIGHT);
        FontUtils.changeFont(ProfileForChat.this, tvPlaceOfBirth, CGlobalVariables.FONTS_POPPINS_LIGHT);
        FontUtils.changeFont(ProfileForChat.this, tvDateOfBirth, CGlobalVariables.FONTS_POPPINS_LIGHT);
        FontUtils.changeFont(ProfileForChat.this, tvTimeOfBirth, CGlobalVariables.FONTS_POPPINS_LIGHT);
        FontUtils.changeFont(ProfileForChat.this, tvMaritalStatus, CGlobalVariables.FONTS_POPPINS_LIGHT);
        FontUtils.changeFont(ProfileForChat.this, tvOccupation, CGlobalVariables.FONTS_POPPINS_LIGHT);

        FontUtils.changeFont(ProfileForChat.this, headingTxt, CGlobalVariables.FONTS_POPPINS_BOLD);
        FontUtils.changeFont(ProfileForChat.this, subheadingTxt, CGlobalVariables.FONTS_POPPINS_LIGHT);
        FontUtils.changeFont(ProfileForChat.this, proceed_btn, CGlobalVariables.FONTS_POPPINS_SEMI_BOLD);
        FontUtils.changeFont(ProfileForChat.this, cancel_btn, CGlobalVariables.FONTS_POPPINS_SEMI_BOLD);

        tvPlacePicker.setOnClickListener(this);
        tvDatePicker.setOnClickListener(this);
        tvTimePicker.setOnClickListener(this);
        proceed_btn.setOnClickListener(this);
        cancel_btn.setOnClickListener(this);
        rlSkip.setOnClickListener(this);
        selectKundliLL.setOnClickListener(this);
        tvPlacePicker.setPadding(20, 0, 0, 0);
        etFullName.setPadding(15, 0, 0, 0);
        tvDatePicker.setPadding(15, 0, 0, 0);
        tvTimePicker.setPadding(15, 0, 0, 0);
        queue = VolleySingleton.getInstance(ProfileForChat.this).getRequestQueue();
        Calendar mcurrentTime = Calendar.getInstance();
        genderOptions = getResources().getStringArray(R.array.gender_list);
        maritalStatusOptions = activity.getResources().getStringArray(R.array.marital_status_list);
        occupationOptions = activity.getResources().getStringArray(R.array.occupation_list);
        userProfileData = CUtils.getUserSelectedProfileFromPreference(ProfileForChat.this);

        if (getIntent().getExtras() != null) {
            bundle = getIntent().getExtras();
            if (bundle.containsKey("phoneNo")) {
                phoneNo = getIntent().getStringExtra("phoneNo");
            }
            if (bundle.containsKey("urlText")) {
                urlText = getIntent().getStringExtra("urlText");
            }
            if (bundle.containsKey("fromWhere")) {
                fromWhereData = getIntent().getStringExtra("fromWhere");
            }
            if (bundle.containsKey("prefillData")) {
                prefillData = getIntent().getBooleanExtra("prefillData", false);
            }
            if (bundle.containsKey("isFromAiHoroscope")) {
                isFromAiHoroscope = getIntent().getBooleanExtra("isFromAiHoroscope", false);
            }
            if (bundle.containsKey(CLICKED_CATEGORY_ENUM_KEY)) {
                clicked_category_enum = getIntent().getStringExtra(CLICKED_CATEGORY_ENUM_KEY);
            }
            if (bundle.containsKey(IS_OPENED_FROM_K_AI_CHAT_BTN)) {
                is_opened_from_kundli_ai_chat_btn = getIntent().getBooleanExtra(IS_OPENED_FROM_K_AI_CHAT_BTN,false);
            }

        }
        rlSkip.setVisibility(View.VISIBLE);
        if (urlText != null && urlText.equals(CGlobalVariables.TYPE_AI_CHAT_RANDOM)) {
            rlSkip.setVisibility(View.VISIBLE);
        } else if (!TextUtils.isEmpty(fromWhereData) && (fromWhereData.equals("ServiceOrder"))) {
            rlSkip.setVisibility(View.GONE);
        }else if(AstrosageKundliApplication.selectedAstrologerDetailBean != null && !CUtils.isAiAstrologer(AstrosageKundliApplication.selectedAstrologerDetailBean)){
            rlSkip.setVisibility(View.VISIBLE);//for human case
        }
        else if (!CUtils.isAstroAITarot(this)) {
            rlSkip.setVisibility(View.GONE);
        }

        try {
            if (AstrosageKundliApplication.currentEventType.equals(CHAT_BTN_CLICKED)) {
                if (fromWhereData.equals("profile_send")) {
                    proceedText = getResources().getString(R.string.send);
                    cancelText = getResources().getString(R.string.cancel);
                } else {
                    proceedText = getResources().getString(R.string.proceed_chat);
                    cancelText = getResources().getString(R.string.cancel_chat);
                }
            } else if (AstrosageKundliApplication.currentEventType.equals(CALL_BTN_CLICKED)) {
                proceedText = getResources().getString(R.string.proceed_call);
                cancelText = getResources().getString(R.string.cancel_call1);
            }else{
                if (fromWhereData.equals("profile_send")) {
                    proceedText = getResources().getString(R.string.send);
                    cancelText = getResources().getString(R.string.cancel);
                }
            }
        } catch (Exception e) {
            //
        }

        proceed_btn.setText(proceedText);

        SpannableString spannableString = new SpannableString(cancelText);
        spannableString.setSpan(
                new UnderlineSpan(),
                0, spannableString.length(),
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        );
        cancel_btn.setText(spannableString);

        initUserData();

        if (bundle != null && bundle.containsKey(KEY_KUNDALI_DETAILS)) {
            proceedAfterPlaceSelect(bundle);
        } else if (prefillData) {
            if (userProfileData != null) {
                setProfileData(userProfileData);
            } else {
                userProfileData = new UserProfileData();
            }
            if(!ReportsFragment.OPEN_FROM_KUNDLI){
                getUserProfileDetails();
            }
        }

        if(ReportsFragment.OPEN_FROM_KUNDLI){
            setUserProfileData(CGlobal.getCGlobalObject().getHoroPersonalInfoObject());
            setProfileData(userProfileData);
        }


        if (isFromAiHoroscope){
          //  selectKundliLL.setVisibility(View.GONE);
            captureInitialValues();

            etFullName.addTextChangedListener(new TextWatcher() {
                @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
                @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                    checkProceedVisibility();
                }
                @Override public void afterTextChanged(Editable s) {}
            });
        }


    }

    private void captureInitialValues() {

        iName = etFullName.getText().toString().trim();
        iDob = tvDatePicker.getText().toString().trim();
        iTime = tvTimePicker.getText().toString().trim();
        iPlace = tvPlacePicker.getText().toString().trim();

        iGenderPos = tvGenderSpinner.getSelectedItemPosition();
        iMaritalPos = tvMaritalStatusSpinner.getSelectedItemPosition();
        iOccupationPos = tvOccupationSpinner.getSelectedItemPosition();

        checkProceedVisibility();
    }
    private void checkProceedVisibility() {

        try {
            if (isFromAiHoroscope){
                String name  = etFullName.getText().toString().trim();
                String dob   = tvDatePicker.getText().toString().trim();
                String time  = tvTimePicker.getText().toString().trim();
                String place = tvPlacePicker.getText().toString().trim();

                int genderPos     = tvGenderSpinner.getSelectedItemPosition();
                int maritalPos    = tvMaritalStatusSpinner.getSelectedItemPosition();
                int occupationPos = tvOccupationSpinner.getSelectedItemPosition();

                boolean isAnyEmpty =
                        name.isEmpty() ||
                                genderPos == 0 ||
                                maritalPos == 0 ||
                                occupationPos == 0 ||
                                dob.equals(getString(R.string.date_of_birth)) ||
                                time.equals(getString(R.string.time_of_birth)) ||
                                place.equals(getString(R.string.birth_place));

                boolean isChanged =
                        !name.equals(iName) ||
                                !dob.equals(iDob) ||
                                !time.equals(iTime) ||
                                !place.equals(iPlace) ||
                                genderPos != iGenderPos ||
                                maritalPos != iMaritalPos ||
                                occupationPos != iOccupationPos;

                if (isAnyEmpty || isChanged) {
                    //proceed_btn.setVisibility(View.VISIBLE);
                    proceed_btn.setEnabled(true);
                    proceed_btn.setAlpha(1.0f);
                } else {
                    //proceed_btn.setVisibility(View.GONE);
                    proceed_btn.setEnabled(false);
                    proceed_btn.setAlpha(0.4f);
                    
                }
            }
        }catch (Exception e){
         //
        }

    }


    /**
     * Populates the user profile data from a {@link BeanHoroPersonalInfo} object.
     * This method is used to pre-fill the profile form with data from a saved Kundli.
     *
     * @param beanHoroPersonalInfo The object containing the user's birth details.
     */
    private void setUserProfileData(BeanHoroPersonalInfo beanHoroPersonalInfo) {
        try {
            com.ojassoft.astrosage.beans.BeanDateTime beanDateTime = beanHoroPersonalInfo.getDateTime();
            com.ojassoft.astrosage.beans.BeanPlace beanPlace = beanHoroPersonalInfo.getPlace();

            userProfileData.setName(beanHoroPersonalInfo.getName());
            userProfileData.setUserPhoneNo(com.ojassoft.astrosage.varta.utils.CUtils.getUserID(this));

            userProfileData.setGender(beanHoroPersonalInfo.getGender());

            userProfileData.setMaritalStatus(com.ojassoft.astrosage.varta.utils.CGlobalVariables.NOT_SPECIFIED);

            userProfileData.setOccupation(com.ojassoft.astrosage.varta.utils.CGlobalVariables.NOT_SPECIFIED);

            if (beanDateTime != null) {
                userProfileData.setDay("" + beanDateTime.getDay());
                userProfileData.setMonth("" + (beanDateTime.getMonth() + 1));
                userProfileData.setYear("" + beanDateTime.getYear());
                userProfileData.setHour("" + beanDateTime.getHour());
                userProfileData.setMinute("" + beanDateTime.getMin());
                userProfileData.setSecond("" + beanDateTime.getSecond());
            }
            if (beanPlace != null) {
                String place = beanPlace.getCityName();
                if (beanPlace.getState() != null && !beanPlace.getState().trim().isEmpty()) {
                    if (!place.contains(",")) {
                        place = place + ", " + beanPlace.getState();
                    }
                }
                userProfileData.setPlace(place);
                userProfileData.setLatdeg(beanPlace.getLatDeg());
                userProfileData.setLongdeg(beanPlace.getLongDeg());
                userProfileData.setLongmin(beanPlace.getLongMin());
                userProfileData.setLatmin(beanPlace.getLatMin());
                userProfileData.setLongew(beanPlace.getLongDir());
                userProfileData.setLatns(beanPlace.getLatDir());
                userProfileData.setTimezone(beanPlace.getTimeZoneValue() + "");
            }

        } catch (Exception e) {
            Log.d("joinLiveAud", e.toString());
        }
    }

    /**
     * Initializes the user data input fields, such as spinners for gender, marital status, and occupation.
     * This method sets up the adapters for the spinners and defines their behavior, including how they are displayed
     * and how the selected values are handled.
     */
    private void initUserData() {
        ArrayAdapter<CharSequence> genderAdapter = new ArrayAdapter<CharSequence>(activity, R.layout.spinner_list_item2,
                genderOptions) {

            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                // ((TextView) v).setTextColor(Color.WHITE);
                //((TextView) v).setTypeface(((DashBoardActivity) activity).regularTypeface);
                v.setBackgroundColor(activity.getResources().getColor(R.color.bg_card_view_color));
                FontUtils.changeFont(ProfileForChat.this, ((TextView) v), CGlobalVariables.FONTS_POPPINS_SEMI_BOLD);
                ((TextView) v).setTextColor(activity.getResources().getColor(R.color.black));
                ((TextView) v).setTextSize(14);
                v.setPadding(10, 0, 10, 0);
                return v;
            }

            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                // ((TextView) v).setTextColor(Color.WHITE);
                //((TextView) v).setTypeface(((DashBoardActivity) activity).regularTypeface);
                if (selectedGender == position) {
                    v.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary_day_night));
                    ((TextView) v).setTextColor(activity.getResources().getColor(R.color.white));
                }
                FontUtils.changeFont(ProfileForChat.this, ((TextView) v), CGlobalVariables.FONTS_POPPINS_LIGHT);

                return v;
            }
        };
        tvGenderSpinner.setAdapter(genderAdapter);

        tvGenderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedGender = position;
                checkProceedVisibility();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<CharSequence> maritalStatusAdapter = new ArrayAdapter<CharSequence>(activity, R.layout.spinner_list_item2,
                maritalStatusOptions) {

            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                // ((TextView) v).setTextColor(Color.WHITE);
                //((TextView) v).setTypeface(((DashBoardActivity) activity).regularTypeface);
                v.setBackgroundColor(activity.getResources().getColor(R.color.bg_card_view_color));
                FontUtils.changeFont(ProfileForChat.this, ((TextView) v), CGlobalVariables.FONTS_POPPINS_SEMI_BOLD);
                ((TextView) v).setTextColor(activity.getResources().getColor(R.color.black));
                ((TextView) v).setTextSize(14);
                v.setPadding(10, 0, 10, 0);
                return v;
            }

            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                // ((TextView) v).setTextColor(Color.WHITE);
                //((TextView) v).setTypeface(((DashBoardActivity) activity).regularTypeface);
                if (selectedMaritalStatus == position) {
                    v.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary_day_night));
                    ((TextView) v).setTextColor(activity.getResources().getColor(R.color.white));
                }
                FontUtils.changeFont(ProfileForChat.this, ((TextView) v), CGlobalVariables.FONTS_POPPINS_LIGHT);

                return v;
            }
        };

        tvMaritalStatusSpinner.setAdapter(maritalStatusAdapter);

        tvMaritalStatusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedMaritalStatus = position;
                checkProceedVisibility();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        ArrayAdapter<CharSequence> occupationAdapter = new ArrayAdapter<CharSequence>(activity, R.layout.spinner_list_item2,
                occupationOptions) {

            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                // ((TextView) v).setTextColor(Color.WHITE);
                //((TextView) v).setTypeface(((DashBoardActivity) activity).regularTypeface);
                v.setBackgroundColor(activity.getResources().getColor(R.color.bg_card_view_color));
                FontUtils.changeFont(ProfileForChat.this, ((TextView) v), CGlobalVariables.FONTS_POPPINS_SEMI_BOLD);
                ((TextView) v).setTextColor(activity.getResources().getColor(R.color.black));
                ((TextView) v).setTextSize(14);
                v.setPadding(10, 0, 10, 0);
                return v;
            }

            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                // ((TextView) v).setTextColor(Color.WHITE);
                //((TextView) v).setTypeface(((DashBoardActivity) activity).regularTypeface);
                if (selectedOccupation == position) {
                    v.setBackgroundColor(activity.getResources().getColor(R.color.colorPrimary_day_night));
                    ((TextView) v).setTextColor(activity.getResources().getColor(R.color.white));
                }
                FontUtils.changeFont(ProfileForChat.this, ((TextView) v), CGlobalVariables.FONTS_POPPINS_LIGHT);

                return v;
            }
        };

        tvOccupationSpinner.setAdapter(occupationAdapter);

        tvOccupationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedOccupation = position;
                checkProceedVisibility();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /**
     * Fetches the user's profile details from the server.
     * This method makes an API call to retrieve the user's profile data and then calls
     * {@link #parseJsonAndUpdateProfile(String)} to update the UI with the fetched data.
     */
    private void getUserProfileDetails() {
        //Log.e("LoadMore Url ",CGlobalVariables.GET_PROFILE_URL);
        if (CUtils.isConnectedWithInternet(activity)) {
//            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.GET_PROFILE_URL,
//                    ProfileForChat.this, false, getProfileParams(), METHOD_GET_PROFILE).getMyStringRequest();
//            queue.add(stringRequest);
            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.refreshUserProfile(getProfileParams());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        hideProgressBar();
                        String myResponse = response.body().string();
                        JSONObject jsonObject = new JSONObject(myResponse);
                        String status = jsonObject.getString("status");
                        if (status.equals(CGlobalVariables.OTP_VERIFIED_AND_RETURN_USERDATA)) {
                            parseJsonAndUpdateProfile(myResponse);
                        }
                    } catch (Exception e) {
                        //
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });

        }
    }

    /**
     * Creates a map of parameters for the get user profile API call.
     * This method gathers the necessary user and device information and puts it into a HashMap,
     * which is then used as the body of the API request to fetch the user's profile.
     *
     * @return A map of parameters for the get user profile API call.
     */
    public Map<String, String> getProfileParams() {
        HashMap<String, String> headers = new HashMap<String, String>();
        if (activity == null) return headers;
        String mobileNo = CUtils.getUserID(activity);
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(activity));
        headers.put(CGlobalVariables.USER_PHONE_NO, mobileNo);
        headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(activity));
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
        return CUtils.setRequiredParams(headers);
    }

    /**
     * Populates the profile form with the user's data.
     * This method takes a {@link UserProfileData} object and sets the values of the input fields
     * in the profile form, such as name, gender, date of birth, etc.
     *
     * @param userProfileData1 The object containing the user's profile data.
     */
    private void setProfileData(UserProfileData userProfileData1) {

        try {
            etFullName.setText(userProfileData1.getName());

            validateUserName();
            validateUserNameCheckAstrosage();
            String cityName = userProfileData1.getPlace() == null ? "" : userProfileData1.getPlace();
            if (cityName.equalsIgnoreCase("")) {
                tvPlacePicker.setText(getResources().getString(R.string.birth_place));
            } else {
                tvPlacePicker.setText(cityName);
            }

            int birthMonth = 0;
            if (!TextUtils.isEmpty(userProfileData1.getMonth())) {
                birthMonth = Integer.parseInt(userProfileData1.getMonth());
            }
            if (birthMonth > 0) {
                if (!(userProfileData1.getDay().isEmpty() &&
                        userProfileData1.getMonth().isEmpty() &&
                        userProfileData1.getYear().isEmpty())) {

                    tvDatePicker.setText(CUtils.appendZeroOnSingleDigit(Integer.parseInt(userProfileData1.getDay())) + "/"
                            + CUtils.appendZeroOnSingleDigit(Integer.parseInt(userProfileData1.getMonth()))
                            + "/" + Integer.parseInt(userProfileData1.getYear()));
                    isDateSelected = true;
                }
            }

          //  Log.e("profiledataCheck", "setProfileData: " + CUtils.convertTimeToHrMtScAmPm(userProfileData1.getHour() + ":" +
        //            userProfileData1.getMinute() + ":" + userProfileData1.getSecond()));



            int genderPos = 0;
            if (!userProfileData1.getGender().trim().isEmpty()) {
                if (userProfileData1.getGender().startsWith("N")) {
                    genderPos = 0;
                } else if (userProfileData1.getGender().startsWith("M")) {
                    genderPos = 1;
                } else {
                    genderPos = 2;
                }
            }

            tvGenderSpinner.setSelection(genderPos);

            if (userProfileData1.getMaritalStatus() != null && !userProfileData1.getMaritalStatus().isEmpty()) {
                tvMaritalStatusSpinner.setSelection(getMaritalStatusPos(userProfileData1.getMaritalStatus()));
            } else {
                tvMaritalStatusSpinner.setSelection(0);
            }
            if (userProfileData1.getOccupation() != null && !userProfileData1.getOccupation().isEmpty()) {
                tvOccupationSpinner.setSelection(getOccupationStatusPos(userProfileData1.getOccupation()));
            } else {
                tvOccupationSpinner.setSelection(0);
            }

            if (!(userProfileData1.getHour().isEmpty() &&
                    userProfileData1.getMinute().isEmpty() &&
                    userProfileData1.getSecond().isEmpty())) {
                tvTimePicker.setText(CUtils.convertTimeToHrMtScAmPm(userProfileData1.getHour() + ":" +
                        userProfileData1.getMinute() + ":" + userProfileData1.getSecond()));

                if (beanDateTime == null)
                    beanDateTime = new BeanDateTime(false, false);
                beanDateTime.setHour(Integer.parseInt(userProfileData1.getHour()));
                beanDateTime.setMin(Integer.parseInt(userProfileData1.getMinute()));
                beanDateTime.setSecond(Integer.parseInt(userProfileData1.getSecond()));
                isTimeSelected = true;
            }
            if (!(userProfileData1.getYear().isEmpty() &&
                    userProfileData1.getMonth().isEmpty() &&
                    userProfileData1.getDay().isEmpty())) {
                if (beanDateTime == null)
                    beanDateTime = new BeanDateTime(false, false);
                beanDateTime.setDay(Integer.parseInt(userProfileData1.getDay()));
                int month = Integer.parseInt(userProfileData1.getMonth());
                beanDateTime.setMonth(month);
                beanDateTime.setYear(Integer.parseInt(userProfileData1.getYear()));
            }
        }catch(Exception e){
            //
        }
    }

    /**
     * Gets the position of a given marital status in the options array.
     *
     * @param maritalStatus The marital status string.
     * @return The position of the marital status in the array.
     */
    private int getMaritalStatusPos(String maritalStatus) {
        int maritalPos = 0;
        for (int i = 0; i < maritalStatusOptionsKey.length; i++) {
            if (maritalStatus.equalsIgnoreCase(maritalStatusOptionsKey[i])) {
                maritalPos = i;
            }
        }
        return maritalPos;
    }

    /**
     * Gets the position of a given occupation status in the options array.
     *
     * @param occupationStatus The occupation status string.
     * @return The position of the occupation status in the array.
     */
    private int getOccupationStatusPos(String occupationStatus) {
        int occupationPos = 0;
        for (int i = 0; i < occupationOptionsKey.length; i++) {
            if (occupationStatus.equalsIgnoreCase(occupationOptionsKey[i])) {
                occupationPos = i;
            }
        }
        return occupationPos;
    }

    /**
     * Handles the click event on the date of birth field.
     * This method opens a date picker dialog, allowing the user to select their date of birth.
     */
    private void onDateOfBirthClick() {
        calendar = Calendar.getInstance();//Calendar.getInstance(Locale.ENGLISH);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);
        //Log.e("profileCheck", "onDateOfBirthClick: year="+year);

        Locale locale = Locale.ENGLISH;
        Locale.setDefault(locale);
        if (beanDateTime == null) {
            beanDateTime = new BeanDateTime(false,true);
        }
        datePickerDialog = new CustomDatePicker(ProfileForChat.this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        tvDatePicker.setPadding(15, 0, 0, 0);
                        tvDatePicker.setText(CUtils.appendZeroOnSingleDigit(day) + "/" +
                                CUtils.appendZeroOnSingleDigit((month) + 1) + "/" + year);
                        beanDateTime.setDay(day);
                        beanDateTime.setMonth(month + 1);
                        beanDateTime.setYear(year);
                        isDateSelected = true;
                        checkProceedVisibility();
                    }
                }, year, month, dayOfMonth);

        datePickerDialog.show();
        if (beanDateTime != null && beanDateTime.getMonth() >= 1) {
            int monTH = 1;
            monTH = beanDateTime.getMonth() - 1;
            datePickerDialog.updateDate(beanDateTime.getYear(), monTH, beanDateTime.getDay());
        }
    }

    /**
     * Handles the click event on the time of birth field.
     * This method opens a time picker dialog, allowing the user to select their time of birth.
     */
    private void onTimeOfBirthClick() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            if(beanDateTime==null){
                beanDateTime = new BeanDateTime(true,false);
            }
            final MyTimePickerDialog.OnTimeSetListener myTimeSetListener = new MyTimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute, int seconds) {
                    BeanDateTime beanDateTime = new BeanDateTime(false,false);
                    beanDateTime.setHour(hourOfDay);
                    beanDateTime.setMin(minute);
                    beanDateTime.setSecond(seconds);
                    updateBirthTime(beanDateTime);
                    beanTimeIfIssue = beanDateTime;
                    checkProceedVisibility();
                }
            };

            final MyTimePickerDialog mTimePicker = new MyTimePickerDialog(ProfileForChat.this,
                    R.style.AppCompatAlertDialogStyle, myTimeSetListener, beanDateTime.getHour(),
                    (beanDateTime.getMin()), (beanDateTime.getSecond()));
            mTimePicker.setCanceledOnTouchOutside(false);
            Drawable timerIcon = ResourcesCompat.getDrawable(getResources(),R.drawable.timer_title,null);
            timerIcon.setTint(getResources().getColor(R.color.black));
            mTimePicker.setIcon(timerIcon);
            boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
            if (!tabletSize) {
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(mTimePicker.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                mTimePicker.show();
                mTimePicker.getWindow().setAttributes(lp);
                mTimePicker.show();
            } else {
                mTimePicker.show();
            }

            Button butOK = mTimePicker.findViewById(android.R.id.button1);
            Button butCancel = mTimePicker.findViewById(android.R.id.button2);
            try {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    mTimePicker.getWindow().setBackgroundDrawableResource(android.R.color.white);
                }
                int divierId = mTimePicker.getContext().getResources()
                        .getIdentifier("android:id/titleDivider", null, null);
                View divider = mTimePicker.findViewById(divierId);
                divider.setVisibility(View.GONE);

            } catch (Exception e) {
                //android.util.//Log.e("EXCEPTION", "openTimePicker: " + e.getMessage());
            }

//            butOK.setTypeface(this.);
//            butCancel.setTypeface(regularTypeface);
            butOK.setText(R.string.set);
            butOK.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary_day_night));
            FontUtils.changeFont(this, butOK, CGlobalVariables.FONTS_POPPINS_SEMI_BOLD);
            butCancel.setText(R.string.cancel);
            FontUtils.changeFont(this, butCancel, CGlobalVariables.FONTS_POPPINS_SEMI_BOLD);

            butCancel.setTextColor(getResources().getColor(R.color.black));


            FontUtils.changeFont(ProfileForChat.this, butOK, CGlobalVariables.FONTS_POPPINS_LIGHT);
            FontUtils.changeFont(ProfileForChat.this, butCancel, CGlobalVariables.FONTS_POPPINS_LIGHT);
        } else {
            showCustomTimePickerDialog(beanDateTime);
        }

    }


    /**
     * Shows a custom time picker dialog.
     * This method is used on older Android versions that do not support the standard time picker dialog.
     *
     * @param beanDateTime The object containing the initial time to be displayed in the picker.
     */
    private void showCustomTimePickerDialog(BeanDateTime beanDateTime) {
        Intent intent = new Intent(ProfileForChat.this, MyNewCustomTimePicker.class);
        intent.putExtra("H", beanDateTime.getHour());
        intent.putExtra("M", beanDateTime.getMin());
        intent.putExtra("S", beanDateTime.getSecond());
        intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, CGlobalVariables.MODULE_PROFILE);
        startActivityForResult(intent, CGlobalVariables.SUB_ACTIVITY_TIME_PICKER);
    }

    /**
     * Updates the birth time with the selected values.
     *
     * @param beanDateTime The object containing the selected time.
     */
    private void updateBirthTime(BeanDateTime beanDateTime) {
        if(this.beanDateTime==null){
            this.beanDateTime = new BeanDateTime(false,false);
        }
        this.beanDateTime.setHour(beanDateTime.getHour());
        this.beanDateTime.setMin(beanDateTime.getMin());
        this.beanDateTime.setSecond(beanDateTime.getSecond());
        isTimeSelected = true;
        tvTimePicker.setPadding(15, 0, 0, 0);
        tvTimePicker.setText(CUtils.convertTimeToHrMtScAmPm(beanDateTime.getHour() + ":" + beanDateTime.getMin() + ":" + beanDateTime.getSecond()));
    }


    /**
     * Handles click events on various views in the activity.
     * This method is called when the user clicks on any view that has a click listener set.
     * It determines which view was clicked and performs the corresponding action, such as opening a picker,
     * proceeding to the next step, or canceling the operation.
     *
     * @param v The view that was clicked.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_date_picker:
                onDateOfBirthClick();
                break;
            case R.id.tv_time_picker:
                onTimeOfBirthClick();
                break;

            case R.id.tv_place_picker:
                openSearchPlace();
                break;

            case R.id.proceed_btn:
                if (!CUtils.isConnectedWithInternet(ProfileForChat.this)) {
                    // CUtils.showSnackbar(mainlayout, getResources().getString(R.string.no_internet), ProfileForChat.this);
                    showToast(getResources().getString(R.string.no_internet));
                } else {
                    if (validation()) {
                        CUtils.fcmAnalyticsEvents("user_profile_dialog_proceed", AstrosageKundliApplication.currentEventType, "");
                        profileDataSendToServer(false);
                    }
                }
                break;

            case R.id.skip_rl:
                // Disable the skip button immediately to prevent multiple clicks while processing.
                rlSkip.setEnabled(false);
                //Determine if the interaction is AI-driven or human-driven.
                boolean isAIInteraction = CUtils.isAstroAITarot(this) ||
                        (urlText != null && urlText.equals(CGlobalVariables.TYPE_AI_CHAT_RANDOM));


                //Validate the full name upfront. If it fails, show toast, re-enable button, and exit.
                if (isAIInteraction && TextUtils.isEmpty(etFullName.getText().toString())) {
                    showToast(getString(R.string.please_enter_name_v));
                    rlSkip.setEnabled(true); // Re-enable button if validation fails
                    return;
                }

                //Perform a more detailed validation if the field is not empty.
                if (!validateFullNameWithToast()) {
                    rlSkip.setEnabled(true); // Re-enable button if detailed validation fails
                    return;
                }


                //when user click on skip button then always profile will not send
                profileDataSendToServer(true);

                //Log the analytics event after the action has been initiated.
                CUtils.fcmAnalyticsEvents("user_profile_dialog_skip", AstrosageKundliApplication.currentEventType, "");
                break;

            case R.id.cancel_btn:
                CUtils.fcmAnalyticsEvents("user_profile_dialog_cancel", AstrosageKundliApplication.currentEventType, "");
//                Intent intent2 = new Intent();
//                intent2.putExtra("IS_PROCEED", false);
//                intent2.putExtra("fromWhere", fromWhereData);
//                setResult(DashBoardActivity.BACK_FROM_PROFILECHATDIALOG, intent2);
                finish();
                break;
            case R.id.selectKundliLL: {
                try {
                    Log.e("clickCheck", "onClick: open profile" );
                    CUtils.fcmAnalyticsEvents("open_profile_btn_click", AstrosageKundliApplication.currentEventType, "");

                    int isKundliAvailable = com.ojassoft.astrosage.utils.CUtils.isLocalKundliAvailable(this);
                    Log.e("clickCheck", "onClick: isKundliAvailable:"+isKundliAvailable );
                    if (isKundliAvailable == 1) {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(com.ojassoft.astrosage.utils.CGlobalVariables.VARTA_PROFILE_QUERY_DATA, true);
                    bundle.putInt("PAGER_INDEX", isKundliAvailable);
                    Intent intent = new Intent(ProfileForChat.this, HomeInputScreen.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, REQUEST_CODE_SELECT_KUNDALI);
                    } else if (isKundliAvailable == 0) {
                        //CUtils.saveProfileForChatInPreference(ProfileForChat.this, userProfileData);
                        Intent intent = new Intent();
                        intent.putExtra("IS_PROCEED", false);
                        intent.putExtra("openKundliList", false);
                        intent.putExtra("phoneNo", phoneNo);
                        intent.putExtra("urlText", urlText);
                        intent.putExtra("fromWhere", fromWhereData);
                        setResult(DashBoardActivity.BACK_FROM_PROFILECHATDIALOG, intent);
                        finish();
                    }
                } catch (Exception e) {
                    Log.e("clickCheck", "onClick: exxception: "+e );

                }
                break;
            }
        }
    }

    /**
     * Gathers the user's profile data and sends it to the server.
     * This method collects all the information entered by the user, saves it locally, and then
     * initiates the process of sending it to the server. It also finishes the activity and
     * returns the result to the calling activity.
     *
     * @param isSkip A boolean indicating whether the user has skipped the profile creation process.
     */
    public void profileDataSendToServer(boolean isSkip) {
        String userName = etFullName.getText().toString();

        userProfileData.setName(userName);
        userProfileData.setUserPhoneNo(CUtils.getUserID(ProfileForChat.this));

        int genderPos = tvGenderSpinner.getSelectedItemPosition();
        String genderStr = genderOptionsKey[genderPos];
        //Log.e("GENDER ", genderStr + " , " + genderPos);
        userProfileData.setGender(genderStr);

        int maritalPos = tvMaritalStatusSpinner.getSelectedItemPosition();
        String maritalStr = maritalStatusOptionsKey[maritalPos];
        userProfileData.setMaritalStatus(maritalStr);

        int occupationPos = tvOccupationSpinner.getSelectedItemPosition();
        String occupationStr = occupationOptionsKey[occupationPos];
        userProfileData.setOccupation(occupationStr);

        if (beanDateTime != null) {
            userProfileData.setDay("" + beanDateTime.getDay());
            userProfileData.setMonth("" + (beanDateTime.getMonth()));
            userProfileData.setYear("" + beanDateTime.getYear());
            userProfileData.setHour("" + beanDateTime.getHour());
            userProfileData.setMinute("" + beanDateTime.getMin());
            userProfileData.setSecond("" + beanDateTime.getSecond());
        }
        if (mBeanPlace != null) {
            String place = mBeanPlace.getCityName();
            if (mBeanPlace.getState() != null && mBeanPlace.getState().trim().length() > 0) {
                if(!place.contains(",")) {
                    place = place + ", " + mBeanPlace.getState();
                }
            }
            userProfileData.setPlace(place);
            userProfileData.setLatdeg(mBeanPlace.getLatDeg());
            userProfileData.setLongdeg(mBeanPlace.getLongDeg());
            userProfileData.setLongmin(mBeanPlace.getLongMin());
            userProfileData.setLatmin(mBeanPlace.getLatMin());
            userProfileData.setLongew(mBeanPlace.getLongDir());
            userProfileData.setLatns(mBeanPlace.getLatDir());
            userProfileData.setTimezone(mBeanPlace.getTimeZoneValue() + "");
        }

        //isSkip true meansprofile will not send to server
        userProfileData.setProfileSendToAstrologer(!isSkip); // isSkip = true only in case of AI tarot
        if (com.ojassoft.astrosage.utils.CUtils.isCompleteUserData(userProfileData)) {
            int isKundliAvailable = com.ojassoft.astrosage.utils.CUtils.isLocalKundliAvailable(this);
            //Log.d("testLogs123", "isKundliAvailable=" + isKundliAvailable);
            if (isKundliAvailable == 1) {//1 means local kundli not available then create the kundli
                com.ojassoft.astrosage.utils.CUtils.calculateKundli(this, getUserBirthDetailBean(userProfileData));
            }
        }


        Intent intent = new Intent();
        intent.putExtra("IS_PROCEED", true);
        intent.putExtra("IS_SKIP", isSkip);
        intent.putExtra("USER_DETAIL", userProfileData);
        if (phoneNo != null && phoneNo.length() > 0 && urlText != null && urlText.length() > 0) {
            intent.putExtra("phoneNo", phoneNo);
            intent.putExtra("urlText", urlText);
        }
        if (!TextUtils.isEmpty(urlText) && urlText.equals(CGlobalVariables.OPEN_DUMMY_CHAT_WINDOW)) {
            intent.putExtra(CGlobalVariables.OPEN_DUMMY_CHAT_WINDOW,true);
        }
        intent.putExtra("fromWhere", fromWhereData);

        if(fromWhereData.equals(CGlobalVariables.HOME_KUNDLI_CHAT)){
            intent.putExtra(CLICKED_CATEGORY_ENUM_KEY, clicked_category_enum);
            intent.putExtra(IS_OPENED_FROM_K_AI_CHAT_BTN,is_opened_from_kundli_ai_chat_btn);
        }
        setResult(DashBoardActivity.BACK_FROM_PROFILECHATDIALOG, intent);

        finish();

       //{longdeg=, occupation=NotSpecified, gender=NotSpecified, year=0, timezone=, countrycode=91,
        // latmin=, regsource=AK_Varta user app, userphoneno=1111100116, minute=0, second=0, month=0, hour=0,
        // longew=, name=, place=, latns=, lang=en, day=0, key=-1489918760, maritalStatus=NotSpecified, longmin=, latdeg=}

        // if any data is present update server and local with that only data
        if(!TextUtils.isEmpty(userProfileData.getName()) || !userProfileData.getGender().equals("NotSpecified") ||
                !userProfileData.getYear().equals("0") || !userProfileData.getHour().equals("0") || !TextUtils.isEmpty(userProfileData.getPlace())) {
            Log.e("profileCheck", "profileDataSendToServer: year="+userProfileData.getYear());
            CUtils.saveUserSelectedProfileInPreference(activity, userProfileData);
            CUtils.saveProfileForChatInPreference(activity,userProfileData);
            sendToServer(userProfileData);
        }

    }

    /**
     * Sends the user's profile data to the server.
     * This method makes an API call to the server to update the user's profile with the newly entered information.
     *
     * @param userProfileData1 The object containing the user's profile data.
     */
    private void sendToServer(UserProfileData userProfileData1) {

        try {
            int year = Integer.parseInt(userProfileData1.getYear());
            int month = Integer.parseInt(userProfileData1.getMonth());
            int day = Integer.parseInt(userProfileData1.getDay());
            CUtils.setFcmAnalyticsByAge(year, month, day);
        } catch (Exception e) {
            //
        }
        if (!CUtils.isConnectedWithInternet(ProfileForChat.this)) {
            // CUtils.showSnackbar(mainlayout, getResources().getString(R.string.no_internet), ProfileForChat.this);
            showToast(getResources().getString(R.string.no_internet));
        } else {
            /*if (pd == null) {
                pd = new CustomProgressDialog(ProfileForChat.this);
                pd.show();
                pd.setCancelable(false);
            }*/
//            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.PROFILE_UPDATE_SUBMIT_URL,
//                    this, false, getParams(userProfileData1), 1).getMyStringRequest();
//            queue.add(stringRequest);

            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.updateUserProfile(getParams(userProfileData1));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        hideProgressBar();
                        String myResponse = response.body().string();
                        JSONObject jsonObject = new JSONObject(myResponse);
                        //Log.d("testLogs123", "onResponse: "+jsonObject);
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("1")) {
                            CUtils.saveUserSelectedProfileInPreference(ProfileForChat.this, userProfileData);
                            //CUtils.showSnackbar(mainlayout, getResources().getString(R.string.update_successfully), ProfileForChat.this);
                            //showToast(getResources().getString(R.string.update_successfully));
                        } else if (status.equals("100")) {
                            CUtils.fcmAnalyticsEvents("bg_login_from_profile_for_chat", CGlobalVariables.VARTA_BACKGROUND_LOGIN, "");

                            LocalBroadcastManager.getInstance(ProfileForChat.this).registerReceiver(mReceiverBackgroundLoginService, new IntentFilter(CGlobalVariables.SEND_BROADCAST_BACK_GR_LOGIN));
                            startBackgroundLoginService();
                        } else {
                            //CUtils.showSnackbar(mainlayout, getResources().getString(R.string.update_not_successfully), ProfileForChat.this);
                            showToast(getResources().getString(R.string.update_not_successfully));
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    hideProgressBar();
                }
            });
        }
    }

    /**
     * Creates a map of parameters for the user profile update API call.
     * This method gathers all the user's profile data and puts it into a HashMap,
     * which is then used as the body of the API request to update the user's profile.
     *
     * @param userProfileDataBean The object containing the user's profile data.
     * @return A map of user profile parameters.
     */
    public Map<String, String> getParams(UserProfileData userProfileDataBean) {

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(ProfileForChat.this));
        headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(ProfileForChat.this));
        headers.put(CGlobalVariables.USER_PHONE_NO, userProfileDataBean.getUserPhoneNo());
        headers.put(CGlobalVariables.REG_SOURCE, CGlobalVariables.APP_SOURCE);
        headers.put("name", userProfileDataBean.getName());
        headers.put("gender", userProfileDataBean.getGender());
        headers.put("place", userProfileDataBean.getPlace());
        headers.put("day", userProfileDataBean.getDay());
        headers.put("month", userProfileDataBean.getMonth());
        headers.put("year", userProfileDataBean.getYear());
        headers.put("hour", userProfileDataBean.getHour());
        headers.put("minute", userProfileDataBean.getMinute());
        headers.put("second", userProfileDataBean.getSecond());
        headers.put("longdeg", userProfileDataBean.getLongdeg());
        headers.put("longmin", userProfileDataBean.getLongmin());
        headers.put("longew", userProfileDataBean.getLongew());
        headers.put("latmin", userProfileDataBean.getLatmin());
        headers.put("latdeg", userProfileDataBean.getLatdeg());
        headers.put("latns", userProfileDataBean.getLatns());
        headers.put("timezone", userProfileDataBean.getTimezone());
        headers.put("maritalStatus", userProfileDataBean.getMaritalStatus());
        headers.put("occupation", userProfileDataBean.getOccupation());
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
        //Log.d("testLogs123", "headers="+headers);
        return CUtils.setRequiredParams(headers);
    }

    /**
     * Opens the place search activity.
     * This method launches the {@link ActPlaceSearch} activity, allowing the user to search for and select their place of birth.
     * The selected place is then returned to this activity to be included in the user's profile.
     */
    public void openSearchPlace() {
        BeanPlace beanPlace = CUtils.getUserDefaultCity(ProfileForChat.this);
        int SELECTED_MODULE = CGlobalVariables.MODULE_PROFILE;
        Intent intent = new Intent(ProfileForChat.this, ActPlaceSearch.class);
        intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, SELECTED_MODULE);
        intent.putExtra(CGlobalVariables.PLACE_BEAN_KEY, beanPlace);
        startActivityForResult(intent, SUB_FRAGMENT_ADD_PROFILE);
    }

    /**
     * Handles the result from a previously launched activity.
     * This method is called when an activity that was launched for a result (e.g., the place search activity)
     * returns. It processes the returned data, such as the selected place of birth, and updates the UI accordingly.
     *
     * @param requestCode The integer request code originally supplied to startActivityForResult(),
     *                    allowing you to identify who this result came from.
     * @param resultCode  The integer result code returned by the child activity through its setResult().
     * @param data        An Intent, which can return result data to the caller.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SUB_FRAGMENT_ADD_PROFILE:
                if (resultCode == RESULT_OK) {
                    if (data == null) return;
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        final BeanPlace place = CUtils.getPlaceObjectFromBundle(bundle);
                        tvPlacePicker.setPadding(40, 0, 0, 0);

                        String cityName = place.getCityName() == null ? "" : place.getCityName();
                        String stateName = place.getState() == null ? "" : place.getState();

                        if (stateName.equalsIgnoreCase("")) {
                            tvPlacePicker.setText(cityName);
                        } else {
                            tvPlacePicker.setText(cityName + ", " + stateName);
                        }
                        mBeanPlace = place;
                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                        CUtils.hideMyKeyboard(ProfileForChat.this);
                    }
                    checkProceedVisibility();
                }
                break;
            case REQUEST_CODE_SELECT_KUNDALI: {
                if (resultCode == RESULT_OK) {
                    if (data == null) return;
                    Bundle bundle = data.getExtras();
                    if (bundle == null) return;
                    proceedAfterPlaceSelect(bundle);
                }
                break;
            }
        }
    }

    /**
     * Proceeds with the profile creation process after a place has been selected.
     * This method is called when the user has selected a place of birth from the search activity.
     * It populates the profile form with the data from the selected Kundli.
     *
     * @param bundle The bundle containing the selected Kundli details.
     */
    private void proceedAfterPlaceSelect(Bundle bundle) {
        try {
            BeanHoroPersonalInfo beanHoroPersonalInfo = (BeanHoroPersonalInfo) bundle.getSerializable(KEY_KUNDALI_DETAILS);
            CUtils.saveStringData(this, SELECTED_KUNDLI_PROFILE, String.valueOf(beanHoroPersonalInfo.getLocalChartId()));
            if (beanHoroPersonalInfo == null) return;
            com.ojassoft.astrosage.beans.BeanDateTime beanDateTime = beanHoroPersonalInfo.getDateTime();
            com.ojassoft.astrosage.beans.BeanPlace beanPlace = beanHoroPersonalInfo.getPlace();
            if (beanDateTime == null || beanPlace == null) return;
            if (userProfileData == null) {
                userProfileData = new UserProfileData();
            }

            userProfileData.setName(beanHoroPersonalInfo.getName());
            userProfileData.setGender(beanHoroPersonalInfo.getGender());

            userProfileData.setDay(String.valueOf(beanDateTime.getDay()));
            userProfileData.setMonth(String.valueOf(beanDateTime.getMonth() + 1));
            userProfileData.setYear(String.valueOf(beanDateTime.getYear()));
            userProfileData.setHour(String.valueOf(beanDateTime.getHour()));
            userProfileData.setMinute(String.valueOf(beanDateTime.getMin()));
            userProfileData.setSecond(String.valueOf(beanDateTime.getSecond()));
            userProfileData.setOccupation("");
            userProfileData.setMaritalStatus("");

            String place = beanPlace.getCityName();
            if (beanPlace.getState() != null && beanPlace.getState().trim().length() > 0) {
                if(!place.contains(",")) {
                    place = place + ", " + beanPlace.getState();
                }
            }

            userProfileData.setPlace(place);
            userProfileData.setLongdeg(beanPlace.getLongDeg());
            userProfileData.setLongmin(beanPlace.getLongMin());
            userProfileData.setLongew(beanPlace.getLongDir());
            userProfileData.setLatdeg(beanPlace.getLatDeg());
            userProfileData.setLatmin(beanPlace.getLatMin());
            userProfileData.setLatns(beanPlace.getLatDir());
            userProfileData.setTimezone(beanPlace.getTimeZoneValue() + "");

            mBeanPlace = null; //forcefully set to null bcz here place is set in userProfileData

            if(!ReportsFragment.OPEN_FROM_KUNDLI){
                setProfileData(userProfileData);
            }
        } catch (Exception e) {
            //Log.d("selectedProfile",e.toString());
        }
    }

    /**
     * Handles the response from a Volley network request.
     * This method is called when a network request made with Volley returns a successful response.
     * It processes the response and updates the UI accordingly.
     *
     * @param response The response from the network request.
     * @param method   The method ID of the network request.
     */
    @Override
    public void onResponse(String response, int method) {

        hideProgressBar();
        //response = "{\"status\":\"2\", \"msg\":\"\"}";
        // Log.e("LoadMore response ", response);
        if (response != null && response.length() > 0) {
            if (method == METHOD_GET_PROFILE) {
//                try {
//                    //Log.e("SAN response ", " METHOD_GET_PROFILE " + response);
//                    JSONObject jsonObject = new JSONObject(response);
//                    String status = jsonObject.getString("status");
//                    if (status.equals(CGlobalVariables.OTP_VERIFIED_AND_RETURN_USERDATA)) {
//                        parseJsonAndUpdateProfile(response);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
            } else {
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    String status = jsonObject.getString("status");
//                    if (status.equalsIgnoreCase("1")) {
//                        CUtils.saveUserSelectedProfileInPreference(ProfileForChat.this, userProfileData);
//                        //CUtils.showSnackbar(mainlayout, getResources().getString(R.string.update_successfully), ProfileForChat.this);
//                        //showToast(getResources().getString(R.string.update_successfully));
//                    } else if (status.equals("100")) {
//                        CUtils.fcmAnalyticsEvents("bg_login_from_profile_for_chat",CGlobalVariables.VARTA_BACKGROUND_LOGIN,"");
//
//                        LocalBroadcastManager.getInstance(ProfileForChat.this).registerReceiver(mReceiverBackgroundLoginService, new IntentFilter(CGlobalVariables.SEND_BROADCAST_BACK_GR_LOGIN));
//                        startBackgroundLoginService();
//                    } else {
//                        //CUtils.showSnackbar(mainlayout, getResources().getString(R.string.update_not_successfully), ProfileForChat.this);
//                        showToast(getResources().getString(R.string.update_not_successfully));
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        } else {
            //CUtils.showSnackbar(mainlayout, getResources().getString(R.string.server_error), ProfileForChat.this);
            showToast(getResources().getString(R.string.server_error));
        }
    }

    /**
     * Handles errors from a Volley network request.
     * This method is called when a network request made with Volley fails.
     *
     * @param error The error that occurred during the network request.
     */
    @Override
    public void onError(VolleyError error) {
        hideProgressBar();
    }

    /**
     * Hides the progress bar if it is currently showing.
     */
    public void hideProgressBar() {
        try {
            if (pd != null && pd.isShowing())
                pd.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Parses the JSON response and updates the user profile data.
     *
     * @param responseResult The JSON string containing the user profile data.
     */
    private void parseJsonAndUpdateProfile(String responseResult) {
        try {
            JSONObject jsonObject = new JSONObject(responseResult);
            Gson gson = new Gson();
            userProfileData = gson.fromJson(jsonObject.toString(), UserProfileData.class);
            CUtils.saveUserSelectedProfileInPreference(activity, userProfileData);
            if (userProfileData != null) {
                setProfileData(userProfileData);
            } else {
                userProfileData = new UserProfileData();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    /**
     * BroadcastReceiver to handle background login service responses.
     * This receiver listens for broadcasts from the background login service and processes the status of the login.
     */
    private final BroadcastReceiver mReceiverBackgroundLoginService = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String status = intent.getStringExtra("status");
            if (status.equals(CGlobalVariables.SUCCESS)) {
                profileDataSendToServer(false);
            } else {
                CUtils.showSnackbar(mainlayout, getResources().getString(R.string.something_wrong_error), getApplicationContext());
            }

            if (mReceiverBackgroundLoginService != null) {
                LocalBroadcastManager.getInstance(ProfileForChat.this).unregisterReceiver(mReceiverBackgroundLoginService);
            }
        }
    };

    /**
     * Starts the background login service.
     * This method initiates the {@link Loginservice} to perform a background login operation.
     */
    private void startBackgroundLoginService() {
        try {
            if (CUtils.getUserLoginStatus(ProfileForChat.this)) {
                Intent intent = new Intent(ProfileForChat.this, Loginservice.class);
                startService(intent);
            }
        } catch (Exception e) {
        }
    }

    /**
     * Validates the user's entered name by removing digits.
     * This method ensures that the username does not contain any numerical digits.
     */
    public void validateUserName() {
        String username = etFullName.getText().toString();
        if (!TextUtils.isEmpty(username)) {
            if (hasDigit(username)) {
                username = username.replaceAll("[0-9]", "");
                etFullName.setText(username);
            }
        }
    }

    /**
     * Validates the user's entered name for specific keywords like "Astrosage" and replaces them.
     * This method helps in preventing the use of restricted keywords in the username.
     */
    public void validateUserNameCheckAstrosage() {
        String username = etFullName.getText().toString();
        if (!TextUtils.isEmpty(username)) {
            username = username.replaceAll("  ", " ");
            if (StringUtils.containsIgnoreCase(username, "Astrosage")) {
                username = username.replaceAll("(?i)" + "Astrosage", "Axxxxxxxx");
            } else if (StringUtils.containsIgnoreCase(username, "Astro sage")) {
                username = username.replaceAll("(?i)" + "Astro sage", "Axxxxxxxxx");
            } else if (StringUtils.containsIgnoreCase(username, CGlobalVariables.ASTROSAGE_HINDI_1)) {
                char strTmp = username.charAt(0);
                username = username.replaceAll("(?i)" + CGlobalVariables.ASTROSAGE_HINDI_1, strTmp + "xxxxxxxxx");
            } else if (StringUtils.containsIgnoreCase(username, CGlobalVariables.ASTROSAGE_HINDI_2)) {
                char strTmp = username.charAt(0);
                username = username.replaceAll("(?i)" + CGlobalVariables.ASTROSAGE_HINDI_1, strTmp + "xxxxxxxxx");
            } else if (isContain(username, CGlobalVariables.astosageKeyword)) {
                username = CUtils.removeAstrosageFromRegional(username);
            }

            etFullName.setText(username);
        }
    }

    /**
     * Performs validation on all the input fields in the profile form.
     * This method checks if all the required fields are filled and if the data is in a valid format.
     *
     * @return {@code true} if all validations pass, {@code false} otherwise.
     */
    private boolean validation() {
        boolean flag = true;

        //validateUserName();
        String fullNameTmp = etFullName.getText().toString().trim();

        fullNameTmp = fullNameTmp.replaceAll("  ", "");
        fullNameTmp = fullNameTmp.replaceAll("[\n\r]", "");

        etFullName.setText(fullNameTmp);
        etFullName.setSelection(etFullName.getText().length());

        if (etFullName.getText().toString().trim().isEmpty()) {
            showToast(getString(R.string.please_enter_name_v));
            flag = false;
        } else if(!validateFullNameWithToast()){
            flag = false;
        } else if (!isDateSelected) {
            showToast(getString(R.string.enter_date));
            flag = false;
        } else if (!isTimeSelected) {
            showToast(getString(R.string.enter_time));
            flag = false;
        } else if (tvPlacePicker.getText().toString().equals(getResources().getString(R.string.birth_place))) {
            showToast(getString(R.string.enter_place));
            flag = false;
        }
        return flag;
    }

    /**
     * Validates the full name with toast messages for invalid input.
     * This method performs various checks on the entered full name, such as:
     * - Checking for special characters.
     * - Filtering out abusive or restricted keywords.
     * - Verifying that the name does not contain digits.
     * - Checking for emojis.
     * - Enforcing a maximum length.
     * If any validation fails, a toast message is displayed to the user.
     *
     * @return {@code true} if the name is valid, {@code false} otherwise.
     */
    private boolean validateFullNameWithToast() {
        boolean flag = true;
        String fullNameTmp = etFullName.getText().toString().trim();
        if (CUtils.isSpecialCharFound(etFullName.getText().toString().trim())) {
            showToast(getString(R.string.please_enter_valid_name_v));
            flag = false;
        } else if (isContain(etFullName.getText().toString().trim(), CGlobalVariables.abuseKeyword)) {
            showToast(getString(R.string.text_name_validation));
            flag = false;
        } else if (StringUtils.containsIgnoreCase(fullNameTmp, "AstroSage") ||
                StringUtils.containsIgnoreCase(fullNameTmp, "Astro Sage")) {
            showToast(getString(R.string.error_message_name));
            flag = false;
        } else if (StringUtils.containsIgnoreCase(fullNameTmp, CGlobalVariables.ASTROSAGE_HINDI_1) ||
                StringUtils.containsIgnoreCase(fullNameTmp, CGlobalVariables.ASTROSAGE_HINDI_2)) {
            showToast(getString(R.string.error_message_name));
            flag = false;
        } else if (isContain(etFullName.getText().toString().trim(), CGlobalVariables.astosageKeyword)) {
            showToast(getString(R.string.error_message_name));
            flag = false;
        } else if (hasDigit(etFullName.getText().toString().trim())) {
            showToast(getString(R.string.text_name_validation_number));
            flag = false;
        } else if (CUtils.checkIsEmojiInString(etFullName.getText().toString().trim())) {
            showToast(getString(R.string.text_name_validation_number));
            flag = false;
        } else if (etFullName.getText().toString().trim().length() > 49) {
            showToast(getString(R.string.text_max_length));
            flag = false;
        }
        return flag;
    }

    private boolean isContain(String inputString, String[] items) {
        String lowerCase = inputString.toLowerCase();
        boolean found = false;
        for (String item : items) {
            String pattern = "\\b" + item + "\\b";
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(lowerCase);
            if (m.find()) {
                found = true;
                break;
            }

        }
        return found;
    }

    /**
     * Handles the back button press event.
     * This method is overridden to handle the back button press, typically to cancel the current operation
     * and set the chat status to canceled.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        AstrosageKundliApplication.currentChatStatus = com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHAT_CANCELED;
        /*userProfileData.setProfileSendToAstrologer(false);
        CUtils.saveProfileForChatInPreference(ProfileForChat.this, userProfileData);
        Intent intent=new Intent();
        intent.putExtra("IS_PROCEED", false);
        setResult(DashBoardActivity.BACK_FROM_PROFILECHATDIALOG,intent);
        finish();*/
    }


    /**
     * Called when the activity is resumed.
     * This method is overridden to perform actions when the activity comes to the foreground,
     * such as clearing temporary data and adjusting the window size.
     */
    @Override
    protected void onResume() {
        super.onResume();
        CUtils.saveStringData(this, CGlobalVariables.TEMP_NAME_FOR_AI_TAROT, "");
        Window window = getWindow();
        if (window != null) {
            WindowManager.LayoutParams params = window.getAttributes();

            // Set the height to 70% of the screen height
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            params.width = (int) (displayMetrics.widthPixels * 0.92);
            params.height = WindowManager.LayoutParams.WRAP_CONTENT;
            window.setAttributes(params);
            window.setBackgroundDrawableResource(android.R.color.transparent);
        }

    }

    /**
     * Called when the activity is being destroyed.
     * This method is overridden to perform cleanup actions when the activity is finished,
     * such as setting the chat status to canceled.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        AstrosageKundliApplication.currentChatStatus = com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHAT_CANCELED;
    }

    /**
     * Displays a short-lived notification (a toast) to the user.
     * This is a convenience method for showing feedback or error messages to the user.
     *
     * @param msg The message to be displayed in the toast.
     */
    private void showToast(String msg) {
        Toast.makeText(ProfileForChat.this, msg, Toast.LENGTH_SHORT).show();
    }

    /**
     * Checks if a given string contains any digits.
     * This method is used for validation to ensure that certain fields, like names, do not contain numbers.
     *
     * @param str The string to be checked.
     * @return {@code true} if the string contains at least one digit, {@code false} otherwise.
     */
    private boolean hasDigit(String str) {
        String regex = ".*[0-9].*";
        return str.matches(regex);
    }


}
