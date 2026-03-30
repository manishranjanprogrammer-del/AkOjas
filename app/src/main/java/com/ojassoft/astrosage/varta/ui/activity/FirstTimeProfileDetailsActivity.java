package com.ojassoft.astrosage.varta.ui.activity;

import static android.widget.Toast.LENGTH_LONG;
import static com.ojassoft.astrosage.utils.CUtils.getUserBirthDetailBean;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.CALL_CLICK;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.SUB_FRAGMENT_ADD_PROFILE;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.misc.CustomDatePicker;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.utils.NumberPicker;
import com.ojassoft.astrosage.varta.adapters.CustomDropDownAdapter;
import com.ojassoft.astrosage.varta.adapters.DropDownInterface;
import com.ojassoft.astrosage.varta.dialog.CallInitiatedDialog;
import com.ojassoft.astrosage.varta.model.BeanDateTime;
import com.ojassoft.astrosage.varta.model.BeanPlace;
import com.ojassoft.astrosage.varta.model.UserDateTimeTempSingleton;
import com.ojassoft.astrosage.varta.model.UserProfileData;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.ChatUtils;
import com.ojassoft.astrosage.varta.utils.FontUtils;

import org.json.JSONException;
import org.json.JSONObject;
import org.shadow.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * An activity that guides the user through a multi-step process to enter their profile details for the first time.
 * This activity is crucial for gathering essential user information like name, gender, date and time of birth,
 * place of birth, and marital status, which is then used for astrological calculations and consultations.
 *
 * <p>The activity uses a wizard-like interface, presenting one piece of information to be entered at a time.
 * It validates user input at each step and ensures a smooth and guided user experience.
 * The collected data is then saved locally and sent to the server to create or update the user's profile.</p>
 *
 * <p>This activity is typically launched when a new user attempts to use a feature that requires their profile,
 * such as starting a chat or call with an astrologer.</p>
 */
public class FirstTimeProfileDetailsActivity extends BaseInputActivity implements DropDownInterface {
    TextView titleTV, btnNextProfile, btnNextGender, btnNextDate, btnNextTime, btnStartCallWithAstrologer, btnStartChatWithAstrologer, txtViewCancel, txtSkip;
    ImageView ivBack, imgViewMale, imgViewFeMale, ivSingle, ivMarried, ivDivorced;
    int page = 1;
    View includeLayoutName, includeLayoutGender, includeLayoutDate, includeLayoutTime, includeLayoutMarital, includeLayoutOccupation, includeLayoutBornPlace;
    EditText edtTextViewName, edtTextSearchPlace;
    NumberPicker pickerDay, pickerMonth, pickerYear;
    private int genderPos = 0, maritalPos = 0, occupationPos = 0;
    private int year;
    private int month;
    private int dayOfMonth;
    private Calendar calendar;
    private CustomDatePicker datePickerDialog;
    private BeanDateTime beanDateTime;
    private BeanPlace mBeanPlace;
    private UserProfileData userProfileData;
    String[] genderOptionsKey = new String[]{"NotSpecified", "M", "F"};
    String[] maritalStatusOptionsKey = new String[]{"NotSpecified","Single","Married","Divorced","In a Relationship", "Complicated", "Widowed"};
    String[] occupationOptionsKey = new String[]{"NotSpecified", "Student", "Businessperson", "Employee", "Retired", "Housewife"};
    String callSource;
    String phoneNo = "";
    String configType = "";//, consultationType = "";
    private TextView tvSingle, tvMarried, tvDivorced, tvNextMarital, tvStudent, tvBusiness, tvEmployee, tvRetired, tvHousewife, tvNextOccupation, tvNextPlace, tvOccupation, tvMaritalStatus;
    private Dialog dialog;
    CheckBox dontKnowTimeCB, dontKnowDateCB;
    private Activity activity;
    private long serverTimeMs;
    public FirstTimeProfileDetailsActivity() {
        super(R.string.astrosage_name);
    }
    private final String[] PERMISSION = {
            Manifest.permission.RECORD_AUDIO
    };
    private static final int PERMISSION_REQ_CODE_AUDIO = 200011;
    public void checkPermissionsAudio() {
        boolean granted = true;
        for (String per : PERMISSION) {
            if (!permissionGranted(per)) {
                granted = false;
                break;
            }
        }

        if (granted) {
            ChatUtils.getInstance(activity).connectAIVoiceCallRandom(userProfileData,AstrosageKundliApplication.apiCallingSource+"_FTP");
        } else {
            CUtils.showPreMicPermissionDialog(this, this::requestPermission);
        }
    }

    private boolean permissionGranted(String permission) {
        return ContextCompat.checkSelfPermission(
                this, permission) == PackageManager.PERMISSION_GRANTED;
    }


    private void requestPermission() {
        ActivityCompat.requestPermissions(this, PERMISSION, PERMISSION_REQ_CODE_AUDIO);
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_REQ_CODE_AUDIO ) {
            boolean granted = true;
            for (int result : grantResults) {
                granted = (result == PackageManager.PERMISSION_GRANTED);
                if (!granted) break;
            }

            if (granted) {
                ChatUtils.getInstance(activity).connectAIVoiceCallRandom(userProfileData,AstrosageKundliApplication.apiCallingSource+"_FTP");
            } else {
                openAlertDialogForOpenSetting();
            }
        }
    }

    // to show the dialog to open setting
    public void openAlertDialogForOpenSetting() {
        AlertDialog.Builder builder = new AlertDialog.Builder(FirstTimeProfileDetailsActivity.this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R
                .layout.dialog_mic_permission, null);
        builder.setView(dialogView);

        // Get references to the views
        TextView notNowButton = dialogView.findViewById(R.id.btn_not_now);

        ImageView close_btn = dialogView.findViewById(R.id.close_btn);
        TextView txtTitle = dialogView.findViewById(R.id.txtTitle);
        TextView txtSubText = dialogView.findViewById(R.id.txtSubText);
        TextView txtSubText1 = dialogView.findViewById(R.id.txtSubText1);
        Button settingsButton = dialogView.findViewById(R.id.btn_settings);
        FontUtils.changeFont(this, notNowButton, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(this, settingsButton, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(this, txtTitle, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(this, txtSubText, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(this, txtSubText1, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);

        // Create and show the dialog
        dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.show();

        // Set click listeners
        notNowButton.setOnClickListener(v -> {
            // Close the dialog if "Not now" is clicked
            dialog.dismiss();
        });
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Close the dialog if "Not now" is clicked
                dialog.dismiss();
            }
        });
        settingsButton.setOnClickListener(v -> {
            // Open app settings if "Settings" is clicked
            dialog.dismiss();
            openAppSettings();

        });
    }
    boolean permissionSettingClicked = false;
    private void openAppSettings() {
        permissionSettingClicked = true;
        Intent intent = new Intent();
        intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivity(intent);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(permissionSettingClicked){
            permissionSettingClicked = false;
            checkPermissionsAudio();
        }
    }

    /**
     * Initializes the activity, setting up the user interface and event listeners.
     * This method is called when the activity is first created. It sets the content view,
     * initializes all the UI components, sets up click listeners for various buttons and views,
     * and handles the back press functionality to navigate through the profile creation steps.
     *
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down,
     *                           this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle).
     *                           Otherwise, it is null.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_time_profile_details);
        activity = FirstTimeProfileDetailsActivity.this;
        // Override the edge-to-edge inset handling to prevent the layout from adjusting with the keyboard
        ViewCompat.setOnApplyWindowInsetsListener(getWindow().getDecorView(), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            // Only apply padding for system bars (status/nav bars), ignoring the keyboard (IME)
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        CUtils.fcmAnalyticsEvents(CGlobalVariables.NEW_FIRST_TIME_PROFILE_WINDOW_OPEN, CGlobalVariables.NEW_FIRST_TIME_PROFILE_FLOW_EVENT, "");
        getIntentData(getIntent());
        inItIds();
        onClickListener();
        datepickerLisner();
        timePickerListener();
        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (page == 2) {
                    page = 1;
                    includeLayoutName.setVisibility(View.VISIBLE);
                    includeLayoutGender.setVisibility(View.GONE);
                } else if (page == 3) {
                    page = 2;
                    includeLayoutGender.setVisibility(View.VISIBLE);
                    includeLayoutDate.setVisibility(View.GONE);
                } else if (page == 4) {
                    page = 3;
                    includeLayoutDate.setVisibility(View.VISIBLE);
                    includeLayoutTime.setVisibility(View.GONE);
                } else if (page == 5) {
                    page = 4;
                    includeLayoutTime.setVisibility(View.VISIBLE);
                    includeLayoutBornPlace.setVisibility(View.GONE);
                } else if (page == 6) {
                    page = 5;
                    includeLayoutBornPlace.setVisibility(View.VISIBLE);
                    includeLayoutMarital.setVisibility(View.GONE);
                } else if (page == 7) {
                    page = 6;
                    includeLayoutOccupation.setVisibility(View.VISIBLE);
                    includeLayoutMarital.setVisibility(View.GONE);
                } else {
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.BACK_BUTTON_CLICKED, CGlobalVariables.NEW_FIRST_TIME_PROFILE_FLOW_EVENT, "");
                    finish();
                }
            }
        });
        getCurrentTimeFromFirebase();
    }

    /**
     * Initializes all the UI components of the activity.
     * This method finds and assigns all the views from the layout file to their corresponding variables.
     * It also sets up the initial state of some views, such as the title text, visibility of buttons,
     * and initializes the data objects required for storing user profile information.
     */
    private void inItIds() {
        ivBack = findViewById(R.id.ivBack);
        includeLayoutName = findViewById(R.id.includeLayoutName);
        includeLayoutGender = findViewById(R.id.includeLayoutGender);
        includeLayoutDate = findViewById(R.id.includeLayoutDate);
        includeLayoutTime = findViewById(R.id.includeLayoutTime);
        includeLayoutMarital = findViewById(R.id.includeLayoutMarital);
        //includeLayoutOccupation = findViewById(R.id.includeLayoutOccupation);
        includeLayoutBornPlace = findViewById(R.id.includeLayoutBornPlace);
        titleTV = findViewById(R.id.tvTitle);
        // Get text size from dimens and set it
        float textSize = getResources().getDimension(R.dimen.tv_size_22);
        titleTV.setTextSize(TypedValue.COMPLEX_UNIT_PX, textSize);
        titleTV.setText(R.string.enter_your_details);
        titleTV.setTextColor(getColor(R.color.black));
        btnNextProfile = findViewById(R.id.btnNextProfile);
        imgViewMale = findViewById(R.id.imgViewMale);
        imgViewFeMale = findViewById(R.id.imgViewFeMale);
        TextView genderTitleTV = findViewById(R.id.txtViewGendetTitle);
        TextView maleLabelTV = findViewById(R.id.font_auto_layout_gender_2);
        TextView femaleLabelTV = findViewById(R.id.font_auto_layout_gender_3);
        btnNextGender = findViewById(R.id.btnNextGender);
        btnNextDate = findViewById(R.id.btnNextDate);
        tvNextPlace = findViewById(R.id.tvNextPlace);
        //btnNextTime = findViewById(R.id.btnNextTime);
        edtTextViewName = findViewById(R.id.edtTextViewName);
        edtTextSearchPlace = findViewById(R.id.edtTextSearchPlace);
        btnStartChatWithAstrologer = findViewById(R.id.btnStatChatWithAstrologer);
        btnStartCallWithAstrologer = findViewById(R.id.btnStatCallWithAstrologer);
        txtViewCancel = findViewById(R.id.txtViewCancel);
        txtSkip = findViewById(R.id.txtSkip);
        dontKnowTimeCB = findViewById(R.id.checkBoxDontKnowMyExactTimeOfBirth);
        dontKnowDateCB = findViewById(R.id.checkBoxDontKnowMyExactDateOfBirth);

        tvNextOccupation = findViewById(R.id.tvNextOccupation);
        tvStudent = findViewById(R.id.tvStudent);
        tvBusiness = findViewById(R.id.tvBusiness);
        tvEmployee = findViewById(R.id.tvEmployee);
        tvRetired = findViewById(R.id.tvRetired);
        tvHousewife = findViewById(R.id.tvHousewife);

        tvOccupation = findViewById(R.id.tvOccupation);
        TextView maritalTitleTV = findViewById(R.id.tvMaritalTitle);
        tvMaritalStatus = findViewById(R.id.tvMaritalStatus);

        FontUtils.changeFont(this, genderTitleTV, CGlobalVariables.FONTS_POPPINS_LIGHT);
        FontUtils.changeFont(this, maleLabelTV, CGlobalVariables.FONTS_POPPINS_LIGHT);
        FontUtils.changeFont(this, femaleLabelTV, CGlobalVariables.FONTS_POPPINS_LIGHT);
        FontUtils.changeFont(this, btnNextGender, CGlobalVariables.FONTS_POPPINS_LIGHT);
        FontUtils.changeFont(this, maritalTitleTV, CGlobalVariables.FONTS_POPPINS_LIGHT);
        FontUtils.changeFont(this, tvMaritalStatus, CGlobalVariables.FONTS_POPPINS_LIGHT);

        btnNextGender.setVisibility(View.GONE);
        txtSkip.setVisibility(View.GONE);
        txtSkip.setText(getString(R.string.skip).toLowerCase());
        txtViewCancel.setPaintFlags(txtViewCancel.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        pickerDay = findViewById(R.id.picker_day);
        pickerMonth = findViewById(R.id.picker_month);
        pickerYear = findViewById(R.id.picker_year);
        beanDateTime = new BeanDateTime(false, false);
        userProfileData = new UserProfileData();


        tvNextPlace.setVisibility(View.GONE);

        //boolean isFreeCallsAstroAvail = CUtils.getBooleanData(this, IS_FREE_CALL_ASTRO_AVAIL, false);
        //btnStartCallWithAstrologer.setVisibility(isFreeCallsAstroAvail ? View.VISIBLE : View.GONE);
    }

    /**
     * Retrieves data passed to the activity through the intent.
     * This method checks for and extracts any extra data that was passed when the activity was started,
     * such as the call source, phone number, and other relevant information needed for the profile creation process.
     *
     * @param intent The intent that started the activity.
     */
    private void getIntentData(Intent intent) {
        if (intent.hasExtra("callSource")) {
            callSource = intent.getStringExtra("callSource");
        }
        if (getIntent().getExtras().containsKey("phoneNo")) {
            phoneNo = getIntent().getStringExtra("phoneNo");
        }
        if (getIntent().getExtras().containsKey("configType")) {
            configType = getIntent().getStringExtra("configType");
        }
        /*if (getIntent().getExtras().containsKey("consultationType")) {
            consultationType = getIntent().getStringExtra("consultationType");
        }*/
        if(configType == null){
            configType = com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_AI_CHAT;
        }
    }

    /**
     * Sets up the time picker and its listener.
     * This method initializes the number pickers for hour, minute, and AM/PM, sets their ranges and default values,
     * and defines the behavior for when the user confirms their time selection.
     * It captures the selected time and advances the user to the next step in the profile creation process.
     */
    private void timePickerListener() {
        NumberPicker npHour = findViewById(R.id.np_hour);
        NumberPicker npMinute = findViewById(R.id.np_minute);
        NumberPicker npAmPm = findViewById(R.id.np_ampm);
        TextView btnNext = findViewById(R.id.btnNextTime);

        Calendar calendar1 = Calendar.getInstance();
        int currentHour = calendar1.get(Calendar.HOUR);
        int currentMin = calendar1.get(Calendar.MINUTE);
        int currentAMPM = calendar1.get(Calendar.AM_PM);

        // Set up Hour NumberPicker
        npHour.setMinValue(1);
        npHour.setMaxValue(12);
        npHour.setValue(currentHour);

        // Set up Minute NumberPicker
        npMinute.setMinValue(0);
        npMinute.setMaxValue(59);
        npMinute.setValue(currentMin);
        npMinute.setFormatter(i -> String.format("%02d", i)); // Display minutes in 2 digits

        // Set up AM/PM NumberPicker
        String[] ampmValues = {"AM", "PM"};
        npAmPm.setMinValue(0);
        npAmPm.setMaxValue(1);
        npAmPm.setValue(currentAMPM);
        npAmPm.setDisplayedValues(ampmValues);

        btnNext.setOnClickListener(v -> {

            //if (!dontKnowTimeCB.isChecked()) {
                // clear focus to detect value change in every case
                npHour.clearFocus();
                npMinute.clearFocus();
                npAmPm.clearFocus();

                int selectedHour = npHour.getValue();
                int selectedMinute = npMinute.getValue();
                String selectedAmPm = ampmValues[npAmPm.getValue()];

                int thFormat;
                if (selectedAmPm.equals("AM")) {
                    if (selectedHour == 12) {
                        thFormat = 0;
                    } else {
                        thFormat = selectedHour;
                    }
                } else {
                    if (selectedHour == 12) {
                        selectedHour = 0;
                    }
                    thFormat = 12 + selectedHour;
                }

                beanDateTime.setMin(selectedMinute);
                beanDateTime.setHour(thFormat);
                beanDateTime.setSecond(0);

            /*} else { // case where user don't know time
                beanDateTime.setMin(-1);
                beanDateTime.setHour(-1);
                beanDateTime.setSecond(-1);
            }*/

            CUtils.fcmAnalyticsEvents(CGlobalVariables.TIME_NEXT, CGlobalVariables.NEW_FIRST_TIME_PROFILE_FLOW_EVENT, "");
            CUtils.hideMyKeyboard(activity);
            page = 5;
            includeLayoutTime.setVisibility(View.GONE);
            includeLayoutBornPlace.setVisibility(View.VISIBLE);
        });
    }

    /**
     * Sets up the date picker and its listener.
     * This method initializes the number pickers for day, month, and year, sets their ranges and default values,
     * and defines the behavior for when the user confirms their date of birth selection.
     * It captures the selected date and advances the user to the next step in the profile creation process.
     */
    private void datepickerLisner() {
        NumberPicker npMonth = findViewById(R.id.np_month);
        NumberPicker npDay = findViewById(R.id.np_day);
        NumberPicker npYear = findViewById(R.id.np_year);

        // Increase text size for each NumberPicker
        setNumberPickerTextSize(npMonth, 20); // Set text size to 20sp
        setNumberPickerTextSize(npDay, 20);   // Set text size to 20sp
        setNumberPickerTextSize(npYear, 20);  // Set text size to 20sp

        // Get the current date
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        int currentMonth = calendar.get(Calendar.MONTH); // 0-based index
        int currentDay = calendar.get(Calendar.DAY_OF_MONTH);

        // Set month values
        //String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        String[] months = getResources().getStringArray(R.array.month_short_name_list);
        npMonth.setMinValue(0);
        npMonth.setMaxValue(months.length - 1);
        npMonth.setDisplayedValues(months);

        // Set day values
        npDay.setMinValue(1);
        npDay.setMaxValue(31);

        // Set year values between 1950 and current year
        npYear.setMinValue(1950);
        npYear.setMaxValue(currentYear);

        // Default values
        npYear.setValue(currentYear);
        npMonth.setValue(currentMonth);
        npDay.setValue(currentDay);

        // Add listeners to dynamically adjust the limits
        npYear.setOnValueChangedListener((picker, oldVal, newVal) -> {
            if (newVal == currentYear) {
                npMonth.setMaxValue(currentMonth); // Restrict to current month
                if (npMonth.getValue() == currentMonth) {
                    npDay.setMaxValue(currentDay); // Restrict to current day
                }
            } else {
                npMonth.setMaxValue(months.length - 1); // Reset to full months
                npDay.setMaxValue(31); // Reset to full days
            }
        });

        npMonth.setOnValueChangedListener((picker, oldVal, newVal) -> {
            if (npYear.getValue() == currentYear && newVal == currentMonth) {
                npDay.setMaxValue(currentDay); // Restrict to current day
            } else {
                npDay.setMaxValue(31); // Reset to full days
            }
        });

        // Next Button Click
        findViewById(R.id.btnNextDate).setOnClickListener(view -> {

            if (!dontKnowDateCB.isChecked()) {
                // clear focus to detect change in value in every case
                npMonth.clearFocus();
                npDay.clearFocus();
                npYear.clearFocus();

                int month = npMonth.getValue() + 1; // Adjust for array index
                int day = npDay.getValue();
                int year = npYear.getValue();

                beanDateTime.setDay(day);
                beanDateTime.setMonth(month);
                beanDateTime.setYear(year);
            } else {
                beanDateTime.setDay(-1);
                beanDateTime.setMonth(-1);
                beanDateTime.setYear(-1);
            }
            CUtils.fcmAnalyticsEvents(CGlobalVariables.DOB_NEXT, CGlobalVariables.NEW_FIRST_TIME_PROFILE_FLOW_EVENT, "");
            CUtils.hideMyKeyboard(activity);
            page = 4;
            includeLayoutDate.setVisibility(View.GONE);
            includeLayoutTime.setVisibility(View.VISIBLE);
        });
    }


    /**
     * Sets the text size for a NumberPicker.
     * This utility method iterates through the child views of a NumberPicker to find the EditText
     * and sets its text size, font, and gravity to customize the appearance of the picker.
     *
     * @param numberPicker The NumberPicker to modify.
     * @param textSize     The desired text size in scaled pixels (sp).
     */
    private void setNumberPickerTextSize(NumberPicker numberPicker, float textSize) {
        try {
            // Iterate through all the child views of the NumberPicker
            for (int i = 0; i < numberPicker.getChildCount(); i++) {
                View child = numberPicker.getChildAt(i);
                if (child instanceof EditText) {
                    EditText editText = (EditText) child;
                    editText.setTextSize(textSize); // Set the desired text size
                    editText.setTypeface(Typeface.createFromAsset(getAssets(), CGlobalVariables.FONTS_POPPINS_LIGHT)); // Optional: Set text style to bold
                    editText.setGravity(Gravity.CENTER); // Optional: Align text to center
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Sets up click listeners for all the interactive UI elements in the activity.
     * This method defines the actions to be taken when the user clicks on buttons like 'Next',
     * gender selection images, and other interactive views. It handles the navigation between
     * different steps of the profile creation process and triggers the final data submission.
     */
    private void onClickListener() {
        btnNextProfile.setOnClickListener(v -> {
            if (isValidName()) {
                CUtils.fcmAnalyticsEvents(CGlobalVariables.NAME_NEXT, CGlobalVariables.NEW_FIRST_TIME_PROFILE_FLOW_EVENT, "");
                CUtils.hideMyKeyboard(activity);
                page = 2;
                includeLayoutName.setVisibility(View.GONE);
                includeLayoutGender.setVisibility(View.VISIBLE);
            }
        });

        ivBack.setOnClickListener(v -> {
            getOnBackPressedDispatcher().onBackPressed();
        });

        imgViewMale.setOnClickListener(v -> {
            btnNextGender.setVisibility(View.VISIBLE);
            genderPos = 1;
            imgViewMale.clearColorFilter();
            imgViewMale.setBackgroundResource(R.drawable.circle_yellow);
            imgViewMale.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white)));

            imgViewFeMale.clearColorFilter();
            imgViewFeMale.setBackgroundResource(R.drawable.circle_female_bg);
            imgViewFeMale.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.black)));
        });

        imgViewFeMale.setOnClickListener(v -> {
            btnNextGender.setVisibility(View.VISIBLE);
            genderPos = 2;
            imgViewFeMale.clearColorFilter();
            imgViewFeMale.setBackgroundResource(R.drawable.circle_yellow);
            imgViewFeMale.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.white)));

            imgViewMale.clearColorFilter();
            imgViewMale.setBackgroundResource(R.drawable.circle_female_bg);
            imgViewMale.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.black)));
        });

        btnNextGender.setOnClickListener(v -> {
            CUtils.fcmAnalyticsEvents(CGlobalVariables.GENDER_NEXT, CGlobalVariables.NEW_FIRST_TIME_PROFILE_FLOW_EVENT, "");
            page = 3;
            includeLayoutGender.setVisibility(View.GONE);
            includeLayoutDate.setVisibility(View.VISIBLE);
        });

        tvNextPlace.setOnClickListener(v -> {
            if (!TextUtils.isEmpty(edtTextSearchPlace.getText())) {
                CUtils.fcmAnalyticsEvents(CGlobalVariables.PLACE_NEXT, CGlobalVariables.NEW_FIRST_TIME_PROFILE_FLOW_EVENT, "");
                page = 6;
                includeLayoutBornPlace.setVisibility(View.GONE);
                includeLayoutMarital.setVisibility(View.VISIBLE);
                showCallChatButton();
            } else {
                showToast(getString(R.string.enter_place));
            }
        });

        tvMaritalStatus.setOnClickListener(v -> {
            openCustomDropDown(2);
        });

        tvOccupation.setOnClickListener(v -> {
            openCustomDropDown(1);
        });

        btnStartChatWithAstrologer.setOnClickListener(v -> {
            CUtils.fcmAnalyticsEvents(CGlobalVariables.START_CHAT_BUTTON_CLICKED, CGlobalVariables.NEW_FIRST_TIME_PROFILE_FLOW_EVENT, "");
            profileDataSendToServer();
            if(configType.contains(com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_HUMAN_CHAT) || configType.equals(com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_FREE_CHAT_HUMAN)) {
                //connect human chat
                ChatUtils.getInstance(activity).startChatRandom(userProfileData, AstrosageKundliApplication.apiCallingSource);
            } else {
                // connect ai chat
                ChatUtils.getInstance(activity).startAIChatRandom(userProfileData, AstrosageKundliApplication.apiCallingSource);
            }

        });

        btnStartCallWithAstrologer.setOnClickListener(v -> {
            CUtils.fcmAnalyticsEvents(CGlobalVariables.START_CALL_BUTTON_CLICKED, CGlobalVariables.NEW_FIRST_TIME_PROFILE_FLOW_EVENT, "");
            profileDataSendToServer();
            if(configType.contains(com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_HUMAN_CALL)) {
                //connect human call
                ChatUtils.getInstance(activity).initCallRandomAstrologer(AstrosageKundliApplication.apiCallingSource);
            } else {

                // connect ai call
                checkPermissionsAudio();
                //ChatUtils.getInstance(activity).connectAIVoiceCallRandom(userProfileData,AstrosageKundliApplication.apiCallingSource+"_FTP");
            }
        });

        /*txtSkip.setOnClickListener(v -> {
            if (CUtils.isAstroAITarot(this) || (configType != null && configType.equals(CGlobalVariables.TYPE_AI_CHAT_RANDOM))) {
                if (TextUtils.isEmpty(edtTextViewName.getText().toString())) {
                    showToast(getString(R.string.please_enter_name_v));
                    return;
                } else {
                    CUtils.saveStringData(this, CGlobalVariables.TEMP_NAME_FOR_AI_TAROT, edtTextViewName.getText().toString());
                }
            }
            //CUtils.fcmAnalyticsEvents("user_profile_dialog_skip", AstrosageKundliApplication.currentEventType, "");
            Intent intent1 = new Intent();
            intent1.putExtra("IS_PROCEED", true);
            intent1.putExtra("IS_SKIP", true);
            userProfileData.setProfileSendToAstrologer(false);
            CUtils.saveProfileForChatInPreference(activity, userProfileData);
            intent1.putExtra("USER_DETAIL", userProfileData);
            if (phoneNo != null && phoneNo.length() > 0 && configType != null && configType.length() > 0) {
                intent1.putExtra("phoneNo", phoneNo);
                intent1.putExtra("configType", configType);
            }
            intent1.putExtra("fromWhere", consultationType);
            setResult(RESULT_OK, intent1);
            finish();
        });*/

        /*txtViewCancel.setOnClickListener(v -> {
            //CUtils.fcmAnalyticsEvents(CGlobalVariables.CANCEL_BUTTON_CLICKED, CGlobalVariables.NEW_FIRST_TIME_PROFILE_FLOW_EVENT, "");
            Intent intent1 = new Intent();
            intent1.putExtra("IS_PROCEED", false);
            setResult(RESULT_OK, intent1);
            finish();
        });*/

        edtTextSearchPlace.setFocusable(false);
        edtTextSearchPlace.setClickable(true);
        edtTextSearchPlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openSearchPlace();
            }
        });
    }

    /**
     * Validates the user's entered name.
     * This method checks if the entered name is valid by performing several checks, such as:
     * - Ensuring the name is not empty.
     * - Checking for special characters.
     * - Filtering out abusive or restricted keywords.
     * - Verifying that the name does not contain digits.
     * - Checking for emojis.
     * - Enforcing a maximum length.
     *
     * @return {@code true} if the name is valid, {@code false} otherwise.
     */
    private boolean isValidName() {
        boolean flag = true;

        String fullNameTmp = edtTextViewName.getText().toString().trim();

        fullNameTmp = fullNameTmp.replaceAll("  ", "");
        fullNameTmp = fullNameTmp.replaceAll("[\n\r]", "");

        edtTextViewName.setText(fullNameTmp);
        edtTextViewName.setSelection(edtTextViewName.getText().length());

        if (edtTextViewName.getText().toString().trim().isEmpty()) {
            showToast(getString(R.string.please_enter_name_v));
            flag = false;
        } else if (CUtils.isSpecialCharFound(edtTextViewName.getText().toString().trim())) {
            showToast(getString(R.string.please_enter_valid_name_v));
            flag = false;
        } else if (isContain(edtTextViewName.getText().toString().trim(), CGlobalVariables.abuseKeyword)) {
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
        } else if (isContain(edtTextViewName.getText().toString().trim(), CGlobalVariables.astosageKeyword)) {
            showToast(getString(R.string.error_message_name));
            flag = false;
        } else if (hasDigit(edtTextViewName.getText().toString().trim())) {
            showToast(getString(R.string.text_name_validation_number));
            flag = false;
        } else if (CUtils.checkIsEmojiInString(edtTextViewName.getText().toString().trim())) {
            showToast(getString(R.string.text_name_validation_number));
            flag = false;
        } else if (edtTextViewName.getText().toString().trim().length() > 49) {
            showToast(getString(R.string.text_max_length));
            flag = false;
        }
        return flag;
    }

    /**
     * Displays a short-lived notification (a toast) to the user.
     * This is a convenience method for showing feedback or error messages to the user.
     *
     * @param msg The message to be displayed in the toast.
     */
    private void showToast(String msg) {
        CUtils.showSnackbar(includeLayoutName, msg, this);
    }

    /**
     * Checks if a given string contains any of the specified keywords.
     * This method is used for validation purposes, such as checking for abusive or restricted words in user input.
     *
     * @param inputString The string to be checked.
     * @param items       An array of keywords to search for.
     * @return {@code true} if any of the keywords are found in the input string, {@code false} otherwise.
     */
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

    /**
     * Opens the place search activity.
     * This method launches the {@link ActPlaceSearch} activity, allowing the user to search for and select their place of birth.
     * The selected place is then returned to this activity to be included in the user's profile.
     */
    public void openSearchPlace() {
        BeanPlace beanPlace = CUtils.getUserDefaultCity(this);
        int SELECTED_MODULE = CGlobalVariables.MODULE_PROFILE;
        Intent intent = new Intent(this, ActPlaceSearch.class);
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
                        //tvPlacePicker.setPadding(40, 0, 0, 0);

                        String cityName = place.getCityName() == null ? "" : place.getCityName();
                        String stateName = place.getState() == null ? "" : place.getState();

                        if (stateName.equalsIgnoreCase("")) {
                            edtTextSearchPlace.setText(cityName);
                        } else {
                            edtTextSearchPlace.setText(cityName + ", " + stateName);
                        }
                        tvNextPlace.setVisibility(View.VISIBLE);
                        mBeanPlace = place;
                        CUtils.hideMyKeyboard(this);
                    }
                }
                break;
        }
    }

    /**
     * Gathers all the collected profile data and sends it to the server.
     * This method compiles all the user-entered information into a {@link UserProfileData} object,
     * saves it locally, and then initiates the process of sending it to the server.
     * It also finishes the activity and returns the result to the calling activity.
     *
     * @param currentTimeMillis The current time in milliseconds from the server and match with user input date and time.
     *                          If user birth details are matching with current time(within 5 minutes) then date time will not be send to server. This is done to avoid wrong date time entry by user.
     */
    public void profileDataSendToServer() {
        try {
           // Log.e("mytagss", "profileDataSendToServer: started" );
            if(serverTimeMs == 0){
                serverTimeMs = System.currentTimeMillis();
            }
            if (userProfileData == null) {
                userProfileData = new UserProfileData();
            }
            String userName = edtTextViewName.getText().toString();
            userProfileData.setName(userName);
            userProfileData.setUserPhoneNo(CUtils.getUserID(this));

            String genderStr = genderOptionsKey[genderPos];
            userProfileData.setGender(genderStr);

            userProfileData.setMaritalStatus(maritalStatusOptionsKey[maritalPos]);
            userProfileData.setOccupation(occupationOptionsKey[occupationPos]);

            if (beanDateTime != null) {
                setDateAndTime(serverTimeMs);
            }

            if (mBeanPlace != null) {
                String place = mBeanPlace.getCityName();
                if (mBeanPlace.getState() != null && !mBeanPlace.getState().trim().isEmpty()) {
                    if (!place.contains(",")) {
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

            userProfileData.setProfileSendToAstrologer(true);
            CUtils.saveUserSelectedProfileInPreference(this, userProfileData);
            CUtils.saveProfileForChatInPreference(this, userProfileData);

        /*Intent intent = new Intent();
        intent.putExtra("IS_PROCEED", isChat);
        intent.putExtra("USER_DETAIL", userProfileData);
        if (phoneNo != null && phoneNo.length() > 0 && configType != null && configType.length() > 0) {
            intent.putExtra("phoneNo", phoneNo);
            intent.putExtra("configType", configType);
        }
        if (!TextUtils.isEmpty(configType) && configType.equals(CGlobalVariables.OPEN_DUMMY_CHAT_WINDOW)) {
            intent.putExtra(CGlobalVariables.OPEN_DUMMY_CHAT_WINDOW, true);
        }
        intent.putExtra("fromWhere", consultationType);
        setResult(RESULT_OK, intent);*/
            //finish();
           // Log.e("mytagss", "profileDataSendToServer: send to server called" );
            sendToServer();
        }catch (Exception e){
          //  Log.e("mytagss", "profileDataSendToServer: exception occurred "+e.getMessage() );
        }

    }

    /**
     * Sets the date and time in the user profile data based on the user's input and the current time.
     * This method checks if the user's entered date and time are close to the current date and time (within 5 minutes).
     * If they are, it saves empty values for date and time in the server to avoid incorrect entries. If the user has indicated
     * that they don't know the time but the date matches, it saves the date but leaves time empty. Otherwise, it saves both date and time.
     *
     * @param currentTimeMillis The current time in milliseconds from the server to compare with user input.
     */
    private void setDateAndTime(long currentTimeMillis) {
        try {
            // check if user input date time is matching with current date time (considering 5 minutes) then don't send date time to server and save empty value in server for date and time.
            // If user don't know time but date is matching with current date then also save empty value for time in server.
   /*         if ((isSameDate(currentTimeMillis, getUserInputTimeInMillis(beanDateTime)) && dontKnowTimeCB.isChecked())
                    || isDateTimeWithinFiveMinutesFromCurrentTime(currentTimeMillis, beanDateTime)) {
                //date
                userProfileData.setDay("");
                userProfileData.setMonth("");
                userProfileData.setYear("");
                //time
                userProfileData.setHour("");
                userProfileData.setMinute("");
                userProfileData.setSecond("");
            } else if (!isSameDate(currentTimeMillis, getUserInputTimeInMillis(beanDateTime)) && dontKnowTimeCB.isChecked()) {
                // case where user don't know time but date is not matching with current date then also save date in server but time will be empty in server because user don't know time.
                //date
                userProfileData.setDay("" + beanDateTime.getDay());
                userProfileData.setMonth("" + beanDateTime.getMonth());
                userProfileData.setYear("" + beanDateTime.getYear());
                //time
                userProfileData.setHour("");
                userProfileData.setMinute("");
                userProfileData.setSecond("");
            } else {*/
                // case where user know time and date is matching with current date time then  save date and time in server.
                //date
                userProfileData.setDay("" + beanDateTime.getDay());
                userProfileData.setMonth("" + beanDateTime.getMonth());
                userProfileData.setYear("" + beanDateTime.getYear());
                //time
                userProfileData.setHour("" + beanDateTime.getHour());
                userProfileData.setMinute("" + beanDateTime.getMin());
                userProfileData.setSecond("" + beanDateTime.getSecond());
            //}

            // save date and time in user profile data singleton class to use in current chat/call session
            //Valid for 1 hour only
            UserDateTimeTempSingleton userDateTimeTempSingleton = UserDateTimeTempSingleton.getInstance();
            userDateTimeTempSingleton.setDay(beanDateTime.getDay()+"");
            userDateTimeTempSingleton.setMonth(beanDateTime.getMonth()+"");
            userDateTimeTempSingleton.setYear(beanDateTime.getYear()+"");
            userDateTimeTempSingleton.setHour(beanDateTime.getHour()+"");
            userDateTimeTempSingleton.setMinute(beanDateTime.getMin()+"");
            userDateTimeTempSingleton.setSecond(beanDateTime.getSecond()+"");
        } catch (Exception e) {
            //
        }
    }
    /**
     * Sends the user's profile data to the server.
     * This method makes an API call to the server to update the user's profile with the newly entered information.
     * It also triggers the calculation of the user's Kundli if sufficient birth details are available.
     */
    private void sendToServer() {
        try {
          //  Log.e("mytagss", "profileDataSendToServer: sendto server start" );
            if (!CUtils.isConnectedWithInternet(this)) {
                CUtils.showSnackbar(btnNextDate, getResources().getString(R.string.no_internet), this);
                showToast(getResources().getString(R.string.no_internet));
            } else {
                if (!TextUtils.isEmpty(userProfileData.getYear())&& !TextUtils.isEmpty(userProfileData.getHour()) ) {
                    //only need date and time mandatory field
                    int year = Integer.parseInt(userProfileData.getYear());
                    int month = Integer.parseInt(userProfileData.getMonth());
                    int day = Integer.parseInt(userProfileData.getDay());
                    CUtils.setFcmAnalyticsByAge(year, month, day);

                    int isKundliAvailable = com.ojassoft.astrosage.utils.CUtils.isLocalKundliAvailable(this);
                    Log.e("CreateKundli", "isKundliAvailable=" + isKundliAvailable);
                    if (isKundliAvailable == 1) {//1 means local kundli not available then create the kundli
                        com.ojassoft.astrosage.utils.CUtils.calculateKundli(this, getUserBirthDetailBean(userProfileData));
                    }
                }

                ApiList api = RetrofitClient.getInstance().create(ApiList.class);
                Call<ResponseBody> call = api.updateUserProfile(getUserParams());
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            String myResponse = response.body().string();
                            JSONObject jsonObject = new JSONObject(myResponse);
                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase("1")) {
                                CUtils.saveUserSelectedProfileInPreference(activity, userProfileData);
                            } else {
                                showToast(getResources().getString(R.string.update_not_successfully));
                            }
                        } catch (Exception e) {
                            //
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        //
                    }
                });
            }
        } catch (Exception e) {

        }
    }

    /**
     * Creates a map of parameters for the user profile update API call.
     * This method gathers all the user's profile data and puts it into a HashMap,
     * which is then used as the body of the API request to update the user's profile.
     *
     * @return A map of user profile parameters.
     */
    public Map<String, String> getUserParams() {

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(this));
        headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(this));
        headers.put(CGlobalVariables.USER_PHONE_NO, userProfileData.getUserPhoneNo());
        headers.put(CGlobalVariables.REG_SOURCE, CGlobalVariables.APP_SOURCE);
        headers.put("name", userProfileData.getName());
        headers.put("gender", userProfileData.getGender());
        headers.put("place", userProfileData.getPlace());

        headers.put("day", userProfileData.getDay());
        headers.put("month", userProfileData.getMonth());
        headers.put("year", userProfileData.getYear());

        headers.put("hour", userProfileData.getHour());
        headers.put("minute", userProfileData.getMinute());
        headers.put("second", userProfileData.getSecond());

        headers.put("longdeg", userProfileData.getLongdeg());
        headers.put("longmin", userProfileData.getLongmin());
        headers.put("longew", userProfileData.getLongew());
        headers.put("latmin", userProfileData.getLatmin());
        headers.put("latdeg", userProfileData.getLatdeg());
        headers.put("latns", userProfileData.getLatns());
        headers.put("timezone", userProfileData.getTimezone());
        headers.put("maritalStatus", userProfileData.getMaritalStatus());
        headers.put("occupation", userProfileData.getOccupation());
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
        headers.put("dontknowtime", dontKnowTimeCB.isChecked() ? "1" : "0"); // 1 means user don't know time otherwise 0
        //Log.d("TestLog", "headers=" + headers);
        return CUtils.setRequiredParams(headers);
    }

    /**
     * Displays a dialog indicating that a call has been initiated.
     * This method is called after a successful random call initiation and shows a dialog
     * with details about the call, such as the astrologer's name and profile picture.
     *
     * @param myResponse The JSON response from the call initiation API.
     */
    public void showCallInitiatedDialog(String myResponse) {
        Log.d("testCallRandApiResReq", "Call Initiated==>>>>>" + myResponse);
        Toast.makeText(this, "Call Initiated ==", LENGTH_LONG).show();
        try {
            JSONObject jsonObject = new JSONObject(myResponse);
            String status = jsonObject.getString("status");
            if (status.equals(CGlobalVariables.CALL_INITIATED)) {
                final String callsId = jsonObject.getString("callsid");
                String talkTime = jsonObject.getString("talktime");
                final String exophoneNo = jsonObject.getString("exophone");
                String internationalCharges = jsonObject.getString("callcharge");
                JSONObject astrologer = jsonObject.getJSONObject("astrologer");
                String astroName = astrologer.getString("name");
                String profileUrl = astrologer.getString("imagefile");

                CallInitiatedDialog dialog = new CallInitiatedDialog(callsId, talkTime, exophoneNo, CALL_CLICK, internationalCharges, astroName, profileUrl);
                dialog.show(getSupportFragmentManager(), "Dialog");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    /**
     * Clears the visual selection state of a list of ImageViews.
     * This method is used to reset the appearance of a group of ImageViews, typically used for single-choice selections.
     *
     * @param views A list of ImageViews to be cleared.
     */
    private void clearIVFilters(List<ImageView> views) {
        for (ImageView view : views) {
            view.clearColorFilter();
            view.setBackgroundResource(R.drawable.circle_female_bg);
            view.setImageTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.black)));
        }
    }

    /**
     * Clears the visual selection state of a list of TextViews.
     * This method is used to reset the appearance of a group of TextViews, typically used for single-choice selections.
     *
     * @param views A list of TextViews to be cleared.
     */
    private void clearTvFilter(List<TextView> views) {
        for (TextView view : views) {
            view.setBackgroundTintList(AppCompatResources.getColorStateList(this, R.color.edit_profile_color));
            view.setTextColor(ContextCompat.getColor(this, R.color.black));
        }
    }

    /**
     * Handles the selection of an occupation.
     * This method is called when the user selects an occupation from the list. It updates the UI to reflect the selection.
     *
     * @param view The TextView that was clicked.
     */

    public void onOccupationSelected(View view) {
        occupationPos = Integer.parseInt(String.valueOf(view.getTag()));
        TextView tv = (TextView) view;
        clearTvFilter(Arrays.asList(tvStudent, tvBusiness, tvEmployee, tvRetired, tvHousewife));
        tv.setBackgroundTintList(AppCompatResources.getColorStateList(this, R.color.colorPrimary_day_night));
        tv.setTextColor(ContextCompat.getColor(this, R.color.white));
    }

    /**
     * Opens a custom dropdown dialog for selecting occupation or marital status.
     *
     * @param type An integer indicating the type of dropdown to open (1 for occupation, 2 for marital status).
     */
    private void openCustomDropDown(int type) {
        dialog = new Dialog(this);
        dialog.setContentView(R.layout.custom_dropdown_layout);
        dialog.setCancelable(true);

        String[] list;
        if (type == 1) {
            list = getResources().getStringArray(R.array.occupation_list);
        } else {
            list = getResources().getStringArray(R.array.marital_status_list);
        }

        RecyclerView recyclerView = dialog.findViewById(R.id.rvDropDown);
        CustomDropDownAdapter adapter = new CustomDropDownAdapter(list, this, type);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dialog.show();

    }

    /**
     * Handles the selection of an item from the custom dropdown.
     * This method is called when the user selects an item from the occupation or marital status dropdown.
     * It updates the UI and the user's profile data with the selected value.
     *
     * @param id   The position of the selected item.
     * @param type The type of dropdown (1 for occupation, 2 for marital status).
     */

    @Override
    public void onItemSelected(int id, int type) {
        if (id > 0) {
            if (type == 1) {
                CUtils.fcmAnalyticsEvents(CGlobalVariables.OCCUPATION_SELECTED, CGlobalVariables.NEW_FIRST_TIME_PROFILE_FLOW_EVENT, "");
            } else {
                CUtils.fcmAnalyticsEvents(CGlobalVariables.MARITAL_SELECTED, CGlobalVariables.NEW_FIRST_TIME_PROFILE_FLOW_EVENT, "");
            }
        }
        dialog.dismiss();
        if (type == 1) {
            occupationPos = id;
            tvOccupation.setText(getResources().getStringArray(R.array.occupation_list)[id]);
        } else {
            maritalPos = id;
            tvMaritalStatus.setText(getResources().getStringArray(R.array.marital_status_list)[id]);
        }
    }

    /**
     * Displays or hides the call and chat buttons based on the configuration type and user selections.
     * This method checks the configuration type to determine whether to show the chat and call buttons.
     * It also considers user selections, such as whether they have indicated they do not know their date or time of birth,
     * in which case only the chat button is shown.
     * configType is aicall, aichat,humanchat,humancall human(for human chat), ai (for ai chat)
     */
    private void showCallChatButton() {
        if(configType.contains(CGlobalVariables.TYPE_CHAT) || configType.equals(com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_FREE_CHAT_AI) ||
                configType.equals(com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_FREE_CHAT_HUMAN)) {
            btnStartChatWithAstrologer.setVisibility(View.VISIBLE);
        } else {
            btnStartChatWithAstrologer.setVisibility(View.GONE);
        }

        if(configType.contains(CGlobalVariables.TYPE_CALL)) {
            btnStartCallWithAstrologer.setVisibility(View.VISIBLE);
        } else {
            btnStartCallWithAstrologer.setVisibility(View.GONE);
        }

        // in case of aicall if dont know date or time then only show chat option. no need to show call option
        /*if(configType.contains(com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_AI_CALL)){
            if(dontKnowDateCB.isChecked() || dontKnowTimeCB.isChecked()){
                btnStartCallWithAstrologer.setVisibility(View.GONE);
                btnStartChatWithAstrologer.setVisibility(View.VISIBLE);
            }
        }*/
    }

    /**
     * Retrieves the current time from Firebase and updates the user's profile data.
     */
    private void getCurrentTimeFromFirebase() {
        DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
        DatabaseReference timeRef = ref.child("server_time");
        // Server par timestamp write karo
        timeRef.setValue(ServerValue.TIMESTAMP);

        // Server se time read karo
        timeRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    try {
                         serverTimeMs = snapshot.getValue(Long.class);
                    } catch (Exception e) {
                       // serverTimeMs = System.currentTimeMillis();// IN case of error getting time from firebase then send current time from device. this is done to avoid blocking of user action due to firebase time fetch failure.
                    }
                    //Date date = new Date(serverTimeMillis);
                    //Log.d("FirebaseServerTime", "Server Time = " + date);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("ServerTime", "Error: " + error.getMessage());
            }
        });

    }

    /**
     * Checks if the user's entered date and time of birth is greater than five minutes from the current time.
     * This method compares the user's entered date and time with the current time to determine if it is a future date that is not realistic.
     *
     * @param currentTimeMillis The current time in milliseconds from the server.
     * @param beanDateTime      The user's entered date and time of birth encapsulated in a BeanDateTime object.
     * @return {@code true} if the user's entered date and time of birth is greater than five minutes from the current time, {@code false} otherwise.
     */
    private boolean isDateTimeWithinFiveMinutesFromCurrentTime(long currentTimeMillis, BeanDateTime beanDateTime) {


        long userTimeMillis = getUserInputTimeInMillis(beanDateTime);

        long FIVE_MINUTES = 5 * 60 * 1000; //5 MINUTES in milliseconds

        return currentTimeMillis < (userTimeMillis + FIVE_MINUTES);
    }

    /**
     * Checks if the user's entered date of birth is the same as the current date.
     * This method compares the user's entered date of birth with the current date to determine if they are the same.
     * This is used to validate the user's input and ensure that they have not entered a future date that is not realistic.
     *
     * @param currentTimeMillis    The current time in milliseconds from the server.
     * @param userInputTimeMillis The user's entered date of birth in milliseconds.
     * @return {@code true} if the user's entered date of birth is the same as the current date, {@code false} otherwise.
     */

    public static boolean isSameDate(long currentTimeMillis, long userInputTimeMillis) {

        try {
            Calendar c1 = Calendar.getInstance();
            Calendar c2 = Calendar.getInstance();

            c1.setTimeInMillis(currentTimeMillis);
            c2.setTimeInMillis(userInputTimeMillis);

            return c1.get(Calendar.YEAR) == c2.get(Calendar.YEAR)
                    && c1.get(Calendar.DAY_OF_YEAR) == c2.get(Calendar.DAY_OF_YEAR);
        } catch (Exception e) {
            return false;
        }
    }


    /**
     * Converts the user's entered date and time of birth into milliseconds.
     * This method takes the user's entered date and time, encapsulated in a BeanDateTime object, and converts it into milliseconds since the epoch.
     * This is used for comparing the user's input with the current time to validate the input.
     *
     * @param beanDateTime The user's entered date and time of birth encapsulated in a BeanDateTime object.
     * @return The user's entered date and time in milliseconds since the epoch.
     */
    private long getUserInputTimeInMillis(BeanDateTime beanDateTime) {
        int year = beanDateTime.getYear();
        int month = beanDateTime.getMonth();
        int day = beanDateTime.getDay();
        int hour = beanDateTime.getHour();
        int minute = beanDateTime.getMin();
        int second = beanDateTime.getSecond();
        Calendar userCal = Calendar.getInstance();
        userCal.set(Calendar.YEAR, year);
        userCal.set(Calendar.MONTH, month - 1); // 0-based
        userCal.set(Calendar.DAY_OF_MONTH, day);
        userCal.set(Calendar.HOUR_OF_DAY, hour);
        userCal.set(Calendar.MINUTE, minute);
        userCal.set(Calendar.SECOND, second);
        userCal.set(Calendar.MILLISECOND, 0);

        return userCal.getTimeInMillis();
    }

}
