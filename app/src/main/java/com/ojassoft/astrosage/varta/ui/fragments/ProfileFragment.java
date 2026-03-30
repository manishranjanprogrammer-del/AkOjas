package com.ojassoft.astrosage.varta.ui.fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.act.HomeInputScreen;
import com.ojassoft.astrosage.varta.interfacefile.IBirthDetailInputFragment;
import com.ojassoft.astrosage.varta.model.BeanDateTime;
import com.ojassoft.astrosage.varta.model.BeanPlace;
import com.ojassoft.astrosage.varta.model.UserProfileData;
import com.ojassoft.astrosage.varta.service.Loginservice;
import com.ojassoft.astrosage.varta.ui.activity.ActPlaceSearch;
import com.ojassoft.astrosage.varta.ui.activity.MyNewCustomTimePicker;
import com.ojassoft.astrosage.varta.ui.activity.ProfileForChat;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.CustomProgressDialog;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.varta.utils.MyTimePickerDialog;
import com.ojassoft.astrosage.varta.utils.TimePicker;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;
import com.ojassoft.astrosage.varta.volley_network.VolleyServiceHandler;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import org.json.JSONException;
import org.json.JSONObject;
import org.shadow.apache.commons.lang3.StringUtils;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.app.Activity.RESULT_OK;
import static com.facebook.FacebookSdk.getApplicationContext;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_KUNDALI_DETAILS;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.ASTROSAGE_HINDI_1;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.REQUEST_CODE_SELECT_KUNDALI;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.SUB_FRAGMENT_ADD_PROFILE;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileFragment extends Fragment implements View.OnClickListener, VolleyResponse {

    EditText etFullName;
    RelativeLayout rlGender, rlPlacePicker, rlDatePicker, rlTimePicker;
    TextView tvPlacePicker, tvDatePicker, tvTimePicker, tvFullName, tvGender,
            tvPlaceOfBirth, tvDateOfBirth, tvTimeOfBirth, headingTxt, subheadingTxt, tvMaritalStatus, tvOccupation;
    Button submitBtn;
    View view;
    ScrollView scroll_view;
    UserProfileData userProfileData = null;
    IBirthDetailInputFragment _iBirthDetailInputFragment;
    Activity activity;
    BeanDateTime beanDateTime = new BeanDateTime(false,false);
    BeanDateTime beanTimeIfIssue;
    Spinner tvGenderSpinner, tvMaritalStatusSpinner, tvOccupationSpinner;
    LinearLayout mainlayout;
    String[] genderOptions;
    String[] maritalStatusOptions;
    String[] occupationOptions;
    String[] genderOptionsKey = new String[] {"NotSpecified","M","F"};
    String[] maritalStatusOptionsKey = new String[] {"NotSpecified","Single","Married","Divorced","In a Relationship", "Complicated", "Widowed"};
    String[] occupationOptionsKey = new String[] {"NotSpecified","Student","Businessperson","Employee","Retired","Housewife"};

    RequestQueue queue;
    CustomProgressDialog pd;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog mTimePicker;
    private boolean isTimeSelected = false;
    private boolean isDateSelected = false;
    private int year;
    private int month;
    private int dayOfMonth;
    private int mHour, mMinute;
    private Calendar calendar;
    private BeanPlace mBeanPlace = null;
    private int METHOD_GET_PROFILE = 2;
    TextView selectKundliTV;

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

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        LANGUAGE_CODE = AstrosageKundliApplication.getAppContext().getLanguageCode();
//        CUtils.getRobotoFont(getActivity(), LANGUAGE_CODE, CGlobalVariables.regular);
        view = inflater.inflate(R.layout.fragment_add_profile, container, false);
        initView();
        return view;
    }

    public void initView() {
        if(activity == null){
            activity = getActivity();
        }
        //Log.e("PROFILE DASH", "PROFILE FRAG ");
        selectKundliTV = view.findViewById(R.id.selectKundliTV);
        rlGender = view.findViewById(R.id.rl_gender);
        rlPlacePicker = view.findViewById(R.id.rl_place_picker);
        rlDatePicker = view.findViewById(R.id.rl_date_picker);
        rlTimePicker = view.findViewById(R.id.rl_time_picker);
        etFullName = view.findViewById(R.id.et_full_name);
        tvFullName = view.findViewById(R.id.tv_full_name);
        headingTxt = view.findViewById(R.id.heading_txt);
        subheadingTxt = view.findViewById(R.id.subheading_txt);
        tvGenderSpinner = view.findViewById(R.id.tv_gender_txt);
        tvMaritalStatusSpinner = view.findViewById(R.id.tv_marital_status_txt);
        tvOccupationSpinner = view.findViewById(R.id.tv_occupation_txt);
        tvGender = view.findViewById(R.id.tv_gender);

        tvDatePicker = view.findViewById(R.id.tv_date_picker);
        tvTimePicker = view.findViewById(R.id.tv_time_picker);
        tvDateOfBirth = view.findViewById(R.id.tv_date_of_birth);
        tvTimeOfBirth = view.findViewById(R.id.tv_time_of_birth);

        tvPlaceOfBirth = view.findViewById(R.id.tv_place_of_birth);
        tvPlacePicker = view.findViewById(R.id.tv_place_picker);
        tvOccupation = view.findViewById(R.id.tv_occupation);
        tvMaritalStatus = view.findViewById(R.id.tv_marital_status);
        //tvDatePicker = view.findViewById(R.id.tv_date_picker);
        mainlayout = view.findViewById(R.id.mainlayout);

        scroll_view = view.findViewById(R.id.scroll_view);
        submitBtn = view.findViewById(R.id.submit_btn);
        //tvTitle = view.findViewById(R.id.tvTitle);

        //FontUtils.changeFont(getActivity(), tvTitle, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(getActivity(), selectKundliTV, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(getActivity(), etFullName, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(getActivity(), tvDatePicker, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(getActivity(), tvTimePicker, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(getActivity(), tvPlacePicker, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(getActivity(), tvFullName, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(getActivity(), tvGender, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(getActivity(), tvPlaceOfBirth, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(getActivity(), tvDateOfBirth, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(getActivity(), tvTimeOfBirth, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(getActivity(), tvMaritalStatus, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(getActivity(), tvOccupation, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);

        FontUtils.changeFont(getActivity(), headingTxt, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(getActivity(), subheadingTxt, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(getActivity(), submitBtn, CGlobalVariables.FONTS_OPEN_SANS_BOLD);

        selectKundliTV.setOnClickListener(this);
        tvPlacePicker.setOnClickListener(this);
        tvDatePicker.setOnClickListener(this);
        tvTimePicker.setOnClickListener(this);
        submitBtn.setOnClickListener(this);
        tvPlacePicker.setPadding(40, 0, 0, 0);
        etFullName.setPadding(40, 0, 0, 0);
        tvDatePicker.setPadding(40, 0, 0, 0);
        tvTimePicker.setPadding(40, 0, 0, 0);
        queue = VolleySingleton.getInstance(activity).getRequestQueue();
        //Calendar mcurrentTime = Calendar.getInstance();
        genderOptions = activity.getResources().getStringArray(R.array.gender_list);
        maritalStatusOptions = activity.getResources().getStringArray(R.array.marital_status_list);
        occupationOptions = activity.getResources().getStringArray(R.array.occupation_list);
        userProfileData = CUtils.getUserSelectedProfileFromPreference(activity);

        ArrayAdapter<CharSequence> genderAdapter = new ArrayAdapter<CharSequence>(activity, R.layout.spinner_list_item,
                genderOptions) {

            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                // ((TextView) v).setTextColor(Color.WHITE);
                //((TextView) v).setTypeface(((DashBoardActivity) activity).regularTypeface);
                FontUtils.changeFont(getActivity(), ((TextView) v), CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
                ((TextView) v).setTextColor(ContextCompat.getColor(activity,R.color.black));
                ((TextView) v).setBackgroundColor(ContextCompat.getColor(activity,R.color.backgroundColorView));
                //((TextView) v).setTextSize(activity.getResources().getDimension(R.dimen.spinner_text_size));
                v.setPadding(10, 0, 10, 0);
                return v;
            }

            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setBackgroundColor(ContextCompat.getColor(activity,R.color.bg_card_view_color));
                // ((TextView) v).setTextColor(Color.WHITE);
                //((TextView) v).setTypeface(((DashBoardActivity) activity).regularTypeface);
                FontUtils.changeFont(getActivity(), ((TextView) v), CGlobalVariables.FONTS_OPEN_SANS_REGULAR);

                return v;
            }
        };

        tvGenderSpinner.setAdapter(genderAdapter);

        ArrayAdapter<CharSequence> maritalStatusAdapter = new ArrayAdapter<CharSequence>(activity, R.layout.spinner_list_item,
                maritalStatusOptions) {

            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                // ((TextView) v).setTextColor(Color.WHITE);
                //((TextView) v).setTypeface(((DashBoardActivity) activity).regularTypeface);
                FontUtils.changeFont(getActivity(), ((TextView) v), CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
                ((TextView) v).setTextColor(ContextCompat.getColor(activity,R.color.black));
                ((TextView) v).setBackgroundColor(ContextCompat.getColor(activity,R.color.backgroundColorView));
                //((TextView) v).setTextSize(activity.getResources().getDimension(R.dimen.spinner_text_size));
                v.setPadding(10, 0, 10, 0);
                return v;
            }

            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setBackgroundColor(ContextCompat.getColor(activity,R.color.bg_card_view_color));
                // ((TextView) v).setTextColor(Color.WHITE);
                //((TextView) v).setTypeface(((DashBoardActivity) activity).regularTypeface);
                FontUtils.changeFont(getActivity(), ((TextView) v), CGlobalVariables.FONTS_OPEN_SANS_REGULAR);

                return v;
            }
        };

        tvMaritalStatusSpinner.setAdapter(maritalStatusAdapter);

        ArrayAdapter<CharSequence> occupationAdapter = new ArrayAdapter<CharSequence>(activity, R.layout.spinner_list_item,
                occupationOptions) {

            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                // ((TextView) v).setTextColor(Color.WHITE);
                //((TextView) v).setTypeface(((DashBoardActivity) activity).regularTypeface);
                FontUtils.changeFont(getActivity(), ((TextView) v), CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
                ((TextView) v).setTextColor(ContextCompat.getColor(activity,R.color.black));
                ((TextView) v).setBackgroundColor(ContextCompat.getColor(activity,R.color.backgroundColorView));
               // ((TextView) v).setTextSize(activity.getResources().getDimension(R.dimen.spinner_text_size));
                v.setPadding(10, 0, 10, 0);
                return v;
            }

            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setBackgroundColor(ContextCompat.getColor(activity,R.color.bg_card_view_color));
                //((TextView) v).setTypeface(((DashBoardActivity) activity).regularTypeface);
               // ((TextView) v).setTextSize(activity.getResources().getDimension(R.dimen.spinner_text_size));
                FontUtils.changeFont(getActivity(), ((TextView) v), CGlobalVariables.FONTS_OPEN_SANS_REGULAR);

                return v;
            }
        };

        tvOccupationSpinner.setAdapter(occupationAdapter);

        if (userProfileData != null) {
            setProfileData(userProfileData);
        } else {
            userProfileData = new UserProfileData();
        }
        getUserProfileDetails();
    }

    private void setProfileData(UserProfileData userProfileData1)
    {
        etFullName.setText(userProfileData1.getName());
        String cityName = userProfileData1.getPlace() == null ? "" : userProfileData1.getPlace();
        if (cityName.equalsIgnoreCase("")) {
            tvPlacePicker.setText(activity.getResources().getString(R.string.birth_place));
        } else {
            tvPlacePicker.setText(cityName);
        }

        int birthMonth = 0;
        if(!TextUtils.isEmpty(userProfileData1.getMonth())){
            birthMonth = Integer.parseInt(userProfileData1.getMonth());
        }
        if(birthMonth > 0) {
            if (!(userProfileData1.getDay().isEmpty() &&
                    userProfileData1.getMonth().isEmpty() &&
                    userProfileData1.getYear().isEmpty())) {

                tvDatePicker.setText(CUtils.appendZeroOnSingleDigit(Integer.parseInt(userProfileData1.getDay())) + "/"
                        + CUtils.appendZeroOnSingleDigit(Integer.parseInt(userProfileData1.getMonth()))
                        + "/" + Integer.parseInt(userProfileData1.getYear()));
                isDateSelected = true;
            }
        }

        if (!(userProfileData1.getHour().isEmpty() &&
                userProfileData1.getMinute().isEmpty() &&
                userProfileData1.getSecond().isEmpty())) {
            tvTimePicker.setText(CUtils.convertTimeToHrMtScAmPm(userProfileData1.getHour() + ":" +
                    userProfileData1.getMinute() + ":" + userProfileData1.getSecond()));

            beanDateTime = new BeanDateTime(false,false);
            if (!TextUtils.isEmpty(userProfileData1.getHour().trim())) {
                beanDateTime.setHour(Integer.parseInt(userProfileData1.getHour().trim()));}
            if (!TextUtils.isEmpty(userProfileData1.getMinute().trim())) {
                beanDateTime.setMin(Integer.parseInt(userProfileData1.getMinute().trim()));
            }
            if (!TextUtils.isEmpty(userProfileData1.getSecond().trim())) {
                beanDateTime.setSecond(Integer.parseInt(userProfileData1.getSecond().trim()));
            }
            if (!TextUtils.isEmpty(userProfileData1.getDay().trim())) {
                beanDateTime.setDay(Integer.parseInt(userProfileData1.getDay().trim()));
            }
            if (!TextUtils.isEmpty(userProfileData1.getMonth().trim())) {
                beanDateTime.setMonth(Integer.parseInt(userProfileData1.getMonth().trim()));
            }
            if (!TextUtils.isEmpty(userProfileData1.getYear().trim())) {
                beanDateTime.setYear(Integer.parseInt(userProfileData1.getYear().trim()));
            }
            isTimeSelected = true;
        }

        int genderPos = 0;
        if (userProfileData1.getGender().trim().length() > 0) {
/*            if (userProfileData1.getGender().startsWith("F") || userProfileData1.getGender().equalsIgnoreCase(activity.getResources().getString(R.string.female))
                    || userProfileData1.getGender().equalsIgnoreCase("F")) {
                genderPos = 1;
            }*/
            if(userProfileData1.getGender().startsWith("N"))
            {
                genderPos = 0;
            }
            else if(userProfileData1.getGender().startsWith("M"))
            {
                genderPos = 1;
            }
            else
            {
                genderPos = 2;
            }
        }

        tvGenderSpinner.setSelection(genderPos);
        //Log.e("LoadMore GENDER 1 ", userProfileData1.getGender());
        //Log.e("LoadMore GENDER 1 ", genderOptions[genderPos] + " , " + genderPos);

        if(userProfileData1.getMaritalStatus() != null && userProfileData1.getMaritalStatus().length()>0) {
            tvMaritalStatusSpinner.setSelection(getMaritalStatusPos(userProfileData1.getMaritalStatus()));
        }else {
            tvMaritalStatusSpinner.setSelection(0);
        }
        if(userProfileData1.getOccupation() != null && userProfileData1.getOccupation().length()>0) {
            tvOccupationSpinner.setSelection(getOccupationStatusPos(userProfileData1.getOccupation()));
        }else{
            tvOccupationSpinner.setSelection(0);
        }
        //Log.e("LoadMore Marital 1 ", userProfileData1.getMaritalStatus() + " , " + getMaritalStatusPos(userProfileData1.getMaritalStatus()));
        //Log.e("LoadMore Occupation 1 ", userProfileData1.getOccupation() + " , " + getOccupationStatusPos(userProfileData1.getOccupation()));
    }

    private int  getMaritalStatusPos(String maritalStatus){
        int maritalPos=0;
        for(int i=0; i<maritalStatusOptionsKey.length; i++) {
            if (maritalStatus.equalsIgnoreCase(maritalStatusOptionsKey[i])) {
                maritalPos = i;
            }
        }
        return maritalPos;
    }

    private int getOccupationStatusPos(String occupationStatus){
        int occupationPos=0;
        for(int i=0; i<occupationOptionsKey.length; i++) {
            if (occupationStatus.equalsIgnoreCase(occupationOptionsKey[i])) {
                occupationPos = i;
            }
        }
        return occupationPos;
    }

    /**
     * onDateOfBirthClick
     */

    private void onDateOfBirthClick() {
        calendar = Calendar.getInstance();//Calendar.getInstance(Locale.ENGLISH);
        year = calendar.get(Calendar.YEAR);
        month = calendar.get(Calendar.MONTH);
        dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH);

        Locale locale = Locale.ENGLISH;
        Locale.setDefault(locale);

        datePickerDialog = new DatePickerDialog(requireContext(), R.style.DatePickerDialogTheme,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        tvDatePicker.setPadding(40, 0, 0, 0);
                        tvDatePicker.setText(CUtils.appendZeroOnSingleDigit(day) + "/" +
                                CUtils.appendZeroOnSingleDigit((month) + 1) + "/" + year);
                        beanDateTime.setDay(day);
                        beanDateTime.setMonth(month + 1);
                        beanDateTime.setYear(year);
                        isDateSelected = true;
                    }
                }, year, month, dayOfMonth);
        //datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        if(beanDateTime.getMonth() >= 1) {
            int monTH=1;
            monTH = beanDateTime.getMonth() - 1;
            datePickerDialog.updateDate(beanDateTime.getYear(), monTH, beanDateTime.getDay());
        }
        DatePicker datePicker = datePickerDialog.getDatePicker();
        // Add padding to the DatePicker
        int padding = (int) (16 * getResources().getDisplayMetrics().density); // 16dp padding
        datePicker.setPadding(padding, padding, padding, padding);


        datePicker.setMaxDate(System.currentTimeMillis());
        /*This Code for set DatePicker Width full*/
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(datePickerDialog.getWindow().getAttributes());
        // Set the height to 70% of the screen height
        DisplayMetrics displayMetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        lp.width = (int) (displayMetrics.widthPixels * 0.85);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(requireContext(), R.color.bg_card_view_color)));
        datePickerDialog.show();
        datePickerDialog.getWindow().setAttributes(lp);
        datePicker.setScaleX(1.2f);
        datePickerDialog.show();
    }

    /**
     * onTimeOfBirthClick
     */

    private void onTimeOfBirthClick() {
        final MyTimePickerDialog.OnTimeSetListener myTimeSetListener = (view, hourOfDay, minute, seconds) -> {
            BeanDateTime beanDateTime = new BeanDateTime(false,false);
            beanDateTime.setHour(hourOfDay);
            beanDateTime.setMin(minute);
            beanDateTime.setSecond(seconds);
            updateBirthTime(beanDateTime);
            beanTimeIfIssue = beanDateTime;
        };

        final MyTimePickerDialog mTimePicker = new MyTimePickerDialog(getActivity(),
                R.style.AppCompatAlertDialogStyle, myTimeSetListener, beanDateTime.getHour(),
                (beanDateTime.getMin()), (beanDateTime.getSecond()));
        mTimePicker.setCanceledOnTouchOutside(false);
        mTimePicker.setIcon(ContextCompat.getDrawable(requireContext(),R.drawable.timer_title));

        mTimePicker.show();

        Button butOK = (Button) mTimePicker.findViewById(android.R.id.button1);
        Button butCancel = (Button) mTimePicker.findViewById(android.R.id.button2);
        try {
            int divierId = mTimePicker.getContext().getResources()
                    .getIdentifier("android:id/titleDivider", null, null);
            View divider = mTimePicker.findViewById(divierId);
            divider.setVisibility(View.GONE);

        } catch (Exception e) {
            //android.util.//Log.e("EXCEPTION", "openTimePicker: " + e.getMessage());
        }
        butOK.setText(R.string.set);
        FontUtils.changeFont(getContext(),butOK, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        butCancel.setText(R.string.cancel);
        FontUtils.changeFont(getContext(),butCancel, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        butOK.setTextColor(ContextCompat.getColor(requireContext(),R.color.colorPrimary_day_night));
        butCancel.setTextColor(getResources().getColor(R.color.black));

 }


    private void showCustomTimePickerDialog(BeanDateTime beanDateTime) {
        Intent intent = new Intent(getActivity(), MyNewCustomTimePicker.class);
        intent.putExtra("H", beanDateTime.getHour());
        intent.putExtra("M", beanDateTime.getMin());
        intent.putExtra("S", beanDateTime.getSecond());
        intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, CGlobalVariables.MODULE_PROFILE);
        startActivityForResult(intent, CGlobalVariables.SUB_ACTIVITY_TIME_PICKER);
    }

    private void updateBirthTime(BeanDateTime beanDateTime) {
        this.beanDateTime.setHour(beanDateTime.getHour());
        this.beanDateTime.setMin(beanDateTime.getMin());
        this.beanDateTime.setSecond(beanDateTime.getSecond());
        isTimeSelected = true;
        tvTimePicker.setPadding(40, 0, 0, 0);
        tvTimePicker.setText(CUtils.convertTimeToHrMtScAmPm(beanDateTime.getHour() + ":" + beanDateTime.getMin() + ":" + beanDateTime.getSecond()));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_date_picker:
                try {
                    onDateOfBirthClick();
                }catch (Exception e){}
                break;
            case R.id.tv_time_picker:
                onTimeOfBirthClick();
                break;

            case R.id.tv_place_picker:
                openSearchPlace();
                break;

            case R.id.submit_btn:
                if (!CUtils.isConnectedWithInternet(getActivity())) {
                    CUtils.showSnackbar(mainlayout, getResources().getString(R.string.no_internet), getActivity());
                } else {
                    if (validation()) {
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_SUBMIT_PROFILE,
                                CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                        profileDataSendToServer();
                    }
                }
                break;
            case R.id.selectKundliTV:{
                try {
                    Bundle bundle = new Bundle();
                    bundle.putBoolean(com.ojassoft.astrosage.utils.CGlobalVariables.VARTA_PROFILE_QUERY_DATA, true);
                    bundle.putInt("PAGER_INDEX", com.ojassoft.astrosage.utils.CUtils.isLocalKundliAvailable(getActivity()));
                    Intent intent = new Intent(getActivity(), HomeInputScreen.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, REQUEST_CODE_SELECT_KUNDALI);
                }catch (Exception e){
                    //
                }
                break;
            }
        }
    }

    public void profileDataSendToServer() {
        if(getActivity() == null) return;
        String userName = etFullName.getText().toString();
        userProfileData.setName(userName);
        userProfileData.setUserPhoneNo(CUtils.getUserID(getActivity()));

        int genderPos = tvGenderSpinner.getSelectedItemPosition();
        String genderStr = genderOptionsKey[genderPos];
        userProfileData.setGender(""+genderStr.charAt(0));

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

        sendToServer(userProfileData);
    }

    private void sendToServer(UserProfileData userProfileData1) {
        if(activity == null) return;
        try {
            CUtils.fcmAnalyticsEvents(userProfileData1.getMaritalStatus(), CGlobalVariables.EVENT_MARITAL_STATUS,"");
            CUtils.fcmAnalyticsEvents(userProfileData1.getOccupation(), CGlobalVariables.EVENT_OCCUPATION,"");

            int year = Integer.parseInt(userProfileData1.getYear());
            int month = Integer.parseInt(userProfileData1.getMonth());
            int day = Integer.parseInt(userProfileData1.getDay());
            CUtils.setFcmAnalyticsByAge(year, month, day);
        }catch (Exception e){
            //
        }
        if (!CUtils.isConnectedWithInternet(activity)) {
            CUtils.showSnackbar(mainlayout, getResources().getString(R.string.no_internet), activity);
        } else {
            if (pd == null)
                pd = new CustomProgressDialog(activity);
            pd.show();
            pd.setCancelable(false);
            //Log.e("LoadMore Url ",CGlobalVariables.PROFILE_UPDATE_SUBMIT_URL);
//            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.PROFILE_UPDATE_SUBMIT_URL,
//                    ProfileFragment.this, false, getParams(userProfileData1), 1).getMyStringRequest();
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
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("1")) {
                            CUtils.saveUserSelectedProfileInPreference(activity, userProfileData);
                            CUtils.saveProfileForChatInPreference(activity, userProfileData);
                            com.ojassoft.astrosage.utils.CUtils.saveStringData(activity, com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_AI_HOROSCOPE, "");
                            CUtils.showSnackbar(mainlayout, getResources().getString(R.string.update_successfully), activity);
                        } else if ( status.equals("100") )
                        {
                            LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReceiverBackgroundLoginService, new IntentFilter(CGlobalVariables.SEND_BROADCAST_BACK_GR_LOGIN));
                            startBackgroundLoginService();
                        }
                        else
                        {
                            CUtils.showSnackbar(mainlayout, getResources().getString(R.string.update_not_successfully), activity);
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

    public Map<String, String> getParams(UserProfileData userProfileDataBean) {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(activity));
        headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(activity));
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
        headers.put(CGlobalVariables.LANG,CUtils.getLanguageKey(LANGUAGE_CODE));
        //Log.e("profile_data1"," : "+CUtils.getApplicationSignatureHashCode(activity)+""+CUtils.getCountryCode(activity) +""+userProfileDataBean.getUserPhoneNo() +""+userProfileDataBean.getName() +""+userProfileDataBean.getGender() +""+userProfileDataBean.getPlace() +""+userProfileDataBean.getDay() +""+userProfileDataBean.getMonth() +""+userProfileDataBean.getYear() +""+userProfileDataBean.getHour() +""+userProfileDataBean.getMinute() +""+userProfileDataBean.getSecond() +""+userProfileDataBean.getLongdeg() +""+userProfileDataBean.getLongmin() +""+userProfileDataBean.getLongew() +""+userProfileDataBean.getLatmin() +""+userProfileDataBean.getLatdeg() +""+userProfileDataBean.getLatns() +""+userProfileDataBean.getTimezone() +""+userProfileDataBean.getMaritalStatus() +""+userProfileDataBean.getOccupation() +""+CUtils.getLanguageKey(LANGUAGE_CODE));
        return CUtils.setRequiredParams(headers);
    }

    public void openSearchPlace() {
        if(getActivity() == null) return;
        BeanPlace beanPlace = CUtils.getUserDefaultCity(getActivity());
        int SELECTED_MODULE = CGlobalVariables.MODULE_PROFILE;
        Intent intent = new Intent(getActivity(), ActPlaceSearch.class);
        intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, SELECTED_MODULE);
        intent.putExtra(CGlobalVariables.PLACE_BEAN_KEY, beanPlace);
        startActivityForResult(intent, SUB_FRAGMENT_ADD_PROFILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SUB_FRAGMENT_ADD_PROFILE:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        final BeanPlace place = CUtils.getPlaceObjectFromBundle(bundle);
                        /*CGlobal.getCGlobalObject().getHoroPersonalInfoObject()
                                .setPlace(place);*/
                        tvPlacePicker.setPadding(40, 0, 0, 0);

                        String cityName = place.getCityName() == null ? "" : place.getCityName();
                        String stateName = place.getState() == null ? "" : place.getState();

                        if (stateName.equalsIgnoreCase("")) {
                            tvPlacePicker.setText(cityName);
                        } else {
                            if(cityName.contains(",")) {
                                tvPlacePicker.setText(cityName);
                            } else {
                                tvPlacePicker.setText(cityName + ", " + stateName);
                            }
                        }
                        mBeanPlace = new BeanPlace();
                        mBeanPlace = place;
                        //Log.e("CITY IDD ", "" + mBeanPlace.getCityId());
                        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                        CUtils.hideMyKeyboard(getActivity());
                    }
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

    private void proceedAfterPlaceSelect(Bundle bundle){
        try {
            BeanHoroPersonalInfo beanHoroPersonalInfo = (BeanHoroPersonalInfo) bundle.getSerializable(KEY_KUNDALI_DETAILS);
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

            setProfileData(userProfileData);
        }catch (Exception e){

        }
    }

    @Override
    public void onResponse(String response, int method)
    {
        hideProgressBar();
        //Log.e("profile_data2", " : "+response);
        //response = "{\"status\":\"2\", \"msg\":\"\"}";
        //Log.e("LoadMore response ", response);
        if (response != null && response.length() > 0) {
            if (method == METHOD_GET_PROFILE) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equals(CGlobalVariables.OTP_VERIFIED_AND_RETURN_USERDATA)) {
                        parseJsonAndUpdateProfile(response);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    if (status.equalsIgnoreCase("1")) {
                        CUtils.saveUserSelectedProfileInPreference(activity, userProfileData);
                        CUtils.saveProfileForChatInPreference(activity, userProfileData);
                        CUtils.showSnackbar(mainlayout, getResources().getString(R.string.update_successfully), activity);
                    } else if ( status.equals("100") )
                    {
                        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mReceiverBackgroundLoginService, new IntentFilter(CGlobalVariables.SEND_BROADCAST_BACK_GR_LOGIN));
                        startBackgroundLoginService();
                    }
                    else
                    {
                        CUtils.showSnackbar(mainlayout, getResources().getString(R.string.update_not_successfully), activity);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            CUtils.showSnackbar(mainlayout, getResources().getString(R.string.server_error), activity);
        }
    }

    @Override
    public void onError(VolleyError error) {
        hideProgressBar();
    }

    private final BroadcastReceiver mReceiverBackgroundLoginService = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String status = intent.getStringExtra("status");
            if (status.equals(CGlobalVariables.SUCCESS)) {
                profileDataSendToServer();
            } else {
                CUtils.showSnackbar(mainlayout, getResources().getString(R.string.something_wrong_error), getApplicationContext());
            }

            if (mReceiverBackgroundLoginService != null) {
                LocalBroadcastManager.getInstance(getActivity()).unregisterReceiver(mReceiverBackgroundLoginService);
            }
        }
    };

    private void startBackgroundLoginService() {
        try {
            if (CUtils.getUserLoginStatus(getActivity())) {
                CUtils.fcmAnalyticsEvents("bg_login_from_profile_fragment",CGlobalVariables.VARTA_BACKGROUND_LOGIN,"");

                Intent intent = new Intent(getActivity(), Loginservice.class);
                getActivity().startService(intent);
            }
        } catch (Exception e) {}
    }

    private boolean validation() {

        String fullNameTmp = etFullName.getText().toString().trim();
        fullNameTmp = fullNameTmp.replaceAll("  ", "");
        fullNameTmp = fullNameTmp.replaceAll("[\n\r]", "");

        etFullName.setText(fullNameTmp);
        etFullName.setSelection(etFullName.getText().length());


        boolean flag = true;
        if (etFullName.getText().toString().trim().isEmpty()) {
            CUtils.showSnackbar(mainlayout, getString(R.string.please_enter_name_v), activity);
            flag = false;
        } else if (isContain(etFullName.getText().toString().trim(), CGlobalVariables.abuseKeyword)) {
            CUtils.showSnackbar(mainlayout, getString(R.string.text_name_validation), activity);
            flag = false;
        }/* else if (!etFullName.getText().toString().trim().matches("^[a-zA-Z0-9]*$")) {
            CUtils.showSnackbar(mainlayout, getString(R.string.text_name_validation), activity);
            flag = false;
        }*/ else if ( StringUtils.containsIgnoreCase(fullNameTmp, "AstroSage") ||
                StringUtils.containsIgnoreCase(fullNameTmp, "Astro Sage") ) {
            CUtils.showSnackbar(mainlayout, getString(R.string.error_message_name), activity);
            flag = false;
        } else if ( StringUtils.containsIgnoreCase(fullNameTmp, CGlobalVariables.ASTROSAGE_HINDI_1) ||
                StringUtils.containsIgnoreCase(fullNameTmp, CGlobalVariables.ASTROSAGE_HINDI_2) ) {
            CUtils.showSnackbar(mainlayout, getString(R.string.error_message_name), activity);
            flag = false;
        } else if (isContain(etFullName.getText().toString().trim(), CGlobalVariables.astosageKeyword)) {
            CUtils.showSnackbar(mainlayout, getString(R.string.error_message_name), activity);
            flag = false;
        } else if (CUtils.isSpecialCharFound(etFullName.getText().toString().trim())) {
            CUtils.showSnackbar(mainlayout, getString(R.string.please_enter_valid_name_v), activity);
            flag = false;
        } else if (hasDigit(etFullName.getText().toString().trim())) {
            CUtils.showSnackbar(mainlayout, getString(R.string.text_name_validation_number), activity);
            flag = false;
        } else if (!isDateSelected) {
            CUtils.showSnackbar(mainlayout, getString(R.string.enter_date), activity);
            flag = false;
        } else if (!isTimeSelected) {
            CUtils.showSnackbar(mainlayout, getString(R.string.enter_time), activity);
            flag = false;
        } else if (tvPlacePicker.getText().toString().equals(activity.getResources().getString(R.string.birth_place))) {
            CUtils.showSnackbar(mainlayout, getString(R.string.enter_place), activity);
            flag = false;
        }

        return flag;
    }
    private boolean hasDigit(String  str){
        String regex = ".*[0-9].*";
        return str.matches( regex );
    }
    private  boolean isContain(String inputString, String[] items){
        String lowerCase = inputString.toLowerCase();
        boolean found = false;
        for (String item : items) {
            String pattern = "\\b"+item+"\\b";
            Pattern p=Pattern.compile(pattern);
            Matcher m=p.matcher(lowerCase);
            if(m.find()){
                found = true;
                break;
            }

        }
        return found;
    }
    private void getUserProfileDetails() {
        //Log.e("LoadMore Url ",CGlobalVariables.GET_PROFILE_URL);
        if (CUtils.isConnectedWithInternet(activity)) {
            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.GET_PROFILE_URL,
                    ProfileFragment.this, false, getProfileParams(), METHOD_GET_PROFILE).getMyStringRequest();
            queue.add(stringRequest);
        }
    }

    public Map<String, String> getProfileParams() {
        HashMap<String, String> headers = new HashMap<String, String>();
        if (activity == null) return headers;
        String mobileNo = CUtils.getUserID(activity);
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(activity));
        headers.put(CGlobalVariables.USER_PHONE_NO, mobileNo);
        headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(activity));
        headers.put(CGlobalVariables.LANG,CUtils.getLanguageKey(LANGUAGE_CODE));
        return headers;
    }

    /**
     * hide Progress Bar
     */
    public void hideProgressBar() {
        try {
            if (pd != null & pd.isShowing())
                pd.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

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
}