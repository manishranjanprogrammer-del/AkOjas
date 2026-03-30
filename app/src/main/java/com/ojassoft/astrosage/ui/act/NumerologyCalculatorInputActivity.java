package com.ojassoft.astrosage.ui.act;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanDateTime;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.controller.ControllerManager;
import com.ojassoft.astrosage.customexceptions.UIDataOperationException;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.model.NumerologyOutputModel;
import com.ojassoft.astrosage.ui.act.matching.HomeMatchMakingInputScreen;
import com.ojassoft.astrosage.ui.customcontrols.DateTimePicker;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.utils.CGlobal;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.MyDatePicker;
import com.ojassoft.astrosage.utils.MyDatePickerDialog;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_DOB;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_KUNDALI_DETAILS;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_NAME;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_NUMROLOGY_MODEL;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_TYPE;

/**
 * Created By Abhishek Raj
 */
public class NumerologyCalculatorInputActivity extends BaseInputActivity implements DateTimePicker.DateWatcher {

    private final int REQUEST_CODE_NUMROLOGY_DATA = 1001;
    Activity currentActivity;
    Context context;
    private Toolbar tool_barAppModule;
    private TabLayout tabLayout;
    private TextView tvTitle;
    private EditText etUserName;
    private TextInputLayout inputLayoutName;
    private Spinner typeSpinner;
    private TextView textViewDob;
    private TextView textViewType;
    private Button buttonCalendar;
    private Button buttonSubmit;
    private TextView textViewUserName;
    private BeanDateTime beanDateTime;
    private DateFormat mDateFormat;
    private CustomProgressDialog pd;
    private RequestQueue queue;
    private String dob = "";

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            beanDateTime.setYear(year);
            beanDateTime.setMonth(monthOfYear);
            beanDateTime.setDay(dayOfMonth);
            updateBirthDate();
        }

    };

    public NumerologyCalculatorInputActivity() {
        super(R.string.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numerology_calculator_input);
        initContext();
        initViews();
        initListener();

        updateUIIfFromOMA();

    }

    private void initViews() {
        queue = VolleySingleton.getInstance(currentActivity).getRequestQueue();
        tool_barAppModule = findViewById(R.id.tool_barAppModule);
        // Get the navigation icon drawable
        Drawable navIcon = ContextCompat.getDrawable(this, R.drawable.ic_back_arrow);

// Check if the drawable is not null
        if (navIcon != null) {
            // Tint the drawable with the desired color
            navIcon.setTint(ContextCompat.getColor(this, R.color.black));

            // Set the tinted drawable as the navigation icon
            tool_barAppModule.setNavigationIcon(navIcon);
        }
        setSupportActionBar(tool_barAppModule);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDateFormat = android.text.format.DateFormat.getMediumDateFormat(currentActivity);

        tvTitle = findViewById(R.id.tvTitle);
        etUserName = findViewById(R.id.etUserName);
        inputLayoutName = findViewById(R.id.input_layout_name);
        textViewDob = findViewById(R.id.textViewDob);
        textViewType = findViewById(R.id.textViewType);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setVisibility(View.GONE);
        tvTitle.setText(getString(R.string.title_numrology_input));
        typeSpinner = findViewById(R.id.ics_spinner_type);
        buttonSubmit = findViewById(R.id.buttonSubmit);
        buttonCalendar = findViewById(R.id.buttonCalendar);
        textViewUserName = findViewById(R.id.textViewUserName);

        tvTitle.setTypeface(regularTypeface);
        textViewDob.setTypeface(mediumTypeface);
        textViewType.setTypeface(mediumTypeface);
        etUserName.addTextChangedListener(new MyTextWatcher(etUserName));

        initDobButton();
        initTypeSpinner();



    }

    private void updateUIIfFromOMA() {

        String from = "";
        Bundle extra = getIntent().getExtras();
        if ( extra != null ){

            try {
                if ( extra.containsKey("FROM") ) {
                    from = extra.getString("FROM");
                    if ( from.equalsIgnoreCase("OMA") ) {
                        BeanHoroPersonalInfo horoPersonalInfo = CGlobal.getCGlobalObject().getHoroPersonalInfoObject();
                        if ( horoPersonalInfo != null ) {
                            String userName = horoPersonalInfo.getName();
                            etUserName.setText(userName);

                            if (this.beanDateTime == null) {
                                this.beanDateTime = new BeanDateTime();
                            }
                            this.beanDateTime.setYear(horoPersonalInfo.getDateTime().getYear());
                            this.beanDateTime.setMonth(horoPersonalInfo.getDateTime().getMonth());
                            this.beanDateTime.setDay(horoPersonalInfo.getDateTime().getDay());
                            buttonCalendar.setText(getFormatedTextToShowDate(beanDateTime));
                            if(userName.matches("[a-zA-Z]+")){
                                etUserName.setClickable(false);
                                etUserName.setEnabled(false);
                            }

                            buttonCalendar.setClickable(false);
                            buttonCalendar.setEnabled(false);

                            buttonSubmit.setText(getResources().getString(R.string.modify));

                            buttonSubmit.performClick();

                        }
                    }
                } else {
                    //Log.e("SAN ", " NCIA not from OMA ");
                }
            } catch (Exception e) {
                //Log.e("SAN ", " NCIA exp " + e.toString() );
            }
        }

    }

    private void initContext() {
        currentActivity = NumerologyCalculatorInputActivity.this;
        context = NumerologyCalculatorInputActivity.this;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initDobButton() {
        Calendar calendar = Calendar.getInstance();
        if (this.beanDateTime == null) {
            this.beanDateTime = new BeanDateTime();
        }
        this.beanDateTime.setYear(calendar.get(Calendar.YEAR));
        this.beanDateTime.setMonth(calendar.get(Calendar.MONTH));
        this.beanDateTime.setDay(calendar.get(Calendar.DAY_OF_MONTH));
        updateBirthDate();
    }

    private void initTypeSpinner() {
        String[] typeOptions = getResources().getStringArray(
                R.array.type_list);
        ArrayAdapter<CharSequence> typeAdapter = new ArrayAdapter<CharSequence>(currentActivity, R.layout.spinner_list_item,
                typeOptions) {

            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTypeface(regularTypeface);
                ((TextView) v).setTextSize(16);
                ((TextView) v).setTextColor(getResources().getColor(R.color.black));
                ((TextView) v).setPadding(10, 0, 10, 0);
                ((TextView) v).setBackgroundColor(getResources().getColor(R.color.backgroundColor));
                return v;
            }

            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setTypeface(regularTypeface);
                ((TextView) v).setTextColor(getResources().getColor(R.color.black));
                ((TextView) v).setBackgroundColor(getResources().getColor(R.color.backgroundColor));
                return v;
            }
        };
        typeSpinner.setAdapter(typeAdapter);
    }

    private void initListener() {

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CUtils.hideMyKeyboard(currentActivity);
                if (submitForm()) {
                    CUtils.googleAnalyticSendWitPlayServie(currentActivity,
                            CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                            CGlobalVariables.GOOGLE_ANALYTIC_NUMROLOGY_CALCULATION, null);
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_NUMROLOGY_CALCULATION, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                    if (CUtils.isConnectedWithInternet(currentActivity)) {
                        calculateValues();
                    } else {
                        MyCustomToast mct = new MyCustomToast(currentActivity, getLayoutInflater(), currentActivity, ((BaseInputActivity) currentActivity).regularTypeface);
                        mct.show(getResources().getString(R.string.no_internet));
                    }

                }
            }
        });

        buttonCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCalendar();
            }
        });
        textViewUserName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();

                bundle.putBoolean(CGlobalVariables.NUMROLOGY_QUERY_DATA, true);
                bundle.putInt("PAGER_INDEX", isLocalKundliAvailable());
                Intent intent = new Intent(currentActivity, HomeInputScreen.class);
                intent.putExtras(bundle);

                startActivityForResult(intent, REQUEST_CODE_NUMROLOGY_DATA);
            }
        });
    }

    private int isLocalKundliAvailable() {
        int screenId = 1;
        try {
            Map<String, String> mapHoroID = new ControllerManager().searchLocalKundliListOperation(
                    this.getApplicationContext(), "", CGlobalVariables.BOTH_GENDER, -1);
            if (mapHoroID != null) {
                screenId = 0;
            } else {
                screenId = 1;
            }

        } catch (UIDataOperationException e) {
            e.printStackTrace();
        }
        return screenId;
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
    /**
     * Opens the calendar to select a date.
     * It checks if the user wants a custom calendar or the default Android calendar.
     * For Android N and N_MR1, it always uses the custom calendar due to a bug in the Nougat Date time picker.
     */
    public void openCalendar() {
        if (beanDateTime == null) return;
        if (CUtils.isUserWantsCustomCalender(currentActivity)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                showCustomDatePickerDialogAboveHoneyComb(beanDateTime);
            } else {
                showCustomDatePickerDialog(beanDateTime);
            }
        } else {
            // Use Custom Date time picker for 7.0 and 7.1. due to eror in Nouget Date time picker (It only uses datePickerMode = Calender)
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N || Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1) {
                showCustomDatePickerDialogAboveHoneyComb(beanDateTime);
            } else {
                showAndroidDatePicker(beanDateTime);
            }
        }
    }

    public void showCustomDatePickerDialogAboveHoneyComb(final BeanDateTime beanDateTime) {
        final MyDatePickerDialog.OnDateChangedListener myDateSetListener = new MyDatePickerDialog.OnDateChangedListener() {

            @Override
            public void onDateSet(MyDatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {

                beanDateTime.setYear(year);
                beanDateTime.setMonth(monthOfYear);
                beanDateTime.setDay(dayOfMonth);
                updateBirthDate();
            }

        };


        final MyDatePickerDialog myDatePickerDialog = new MyDatePickerDialog(currentActivity, R.style.AppCompatAlertDialogStyle, myDateSetListener, beanDateTime.getMonth(), (beanDateTime.getDay()), (beanDateTime.getYear()), false);
        myDatePickerDialog.setCanceledOnTouchOutside(false);
        //mTimePicker.setTitle("hello");
        myDatePickerDialog.setIcon(getResources().getDrawable(R.drawable.ic_today_black_icon));
        //if device is tablet than i do not need to set DatePicker and Time Picker to be match Parent
        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        if (!tabletSize) {
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(myDatePickerDialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            myDatePickerDialog.show();
            myDatePickerDialog.getWindow().setAttributes(lp);
        } else {
            myDatePickerDialog.show();
        }

        try {
            //   mTimePicker.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                myDatePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.white);
            }
            int divierId = myDatePickerDialog.getContext().getResources()
                    .getIdentifier("android:id/titleDivider", null, null);
            View divider = myDatePickerDialog.findViewById(divierId);
            divider.setVisibility(View.GONE);

        } catch (Exception e) {
            //android.util.//Log.e("EXCEPTION", "openTimePicker: " + e.getMessage());
        }

        Button butOK = (Button) myDatePickerDialog.findViewById(android.R.id.button1);
        Button butCancel = (Button) myDatePickerDialog.findViewById(android.R.id.button2);
        butOK.setText(R.string.set);
        butCancel.setText(R.string.cancel);

        butOK.setTypeface(regularTypeface);
        butCancel.setTypeface(regularTypeface);

    }

    private void showCustomDatePickerDialog(BeanDateTime beanDateTime) {
        // Create the dialog
        final Dialog mDateTimeDialog = new Dialog(this);
        // Inflate the root layout
        final RelativeLayout mDateTimeDialogView = (RelativeLayout) getLayoutInflater()
                .inflate(R.layout.date_time_dialog, null);
        // Grab widget instance
        final DateTimePicker mDateTimePicker = (DateTimePicker) mDateTimeDialogView
                .findViewById(R.id.DateTimePicker);
        mDateTimePicker.setDateChangedListener(this);
        // Added by hukum to init date picker
        mDateTimePicker.initDateElements(beanDateTime.getYear(),
                beanDateTime.getMonth(), beanDateTime.getDay(),
                beanDateTime.getHour(), beanDateTime.getMin(),
                beanDateTime.getSecond());
        mDateTimePicker.initData();
        // end
        Button setDateBtn = (Button) mDateTimeDialogView
                .findViewById(R.id.SetDateTime);
        setDateBtn.setTypeface(regularTypeface);
        // Update demo TextViews when the "OK" button is clicked
        setDateBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // birthDetailInputFragment.isDstOrNot();
                mDateTimePicker.clearFocus();
                mDateTimeDialog.dismiss();
            }
        });

        Button cancelBtn = (Button) mDateTimeDialogView
                .findViewById(R.id.CancelDialog);
        cancelBtn.setTypeface(regularTypeface);
        // Cancel the dialog when the "Cancel" button is clicked
        cancelBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                mDateTimePicker.reset();
                mDateTimeDialog.cancel();
            }
        });

        // Reset Date and Time pickers when the "Reset" button is clicked

        Button resetBtn = (Button) mDateTimeDialogView
                .findViewById(R.id.ResetDateTime);
        resetBtn.setTypeface(regularTypeface);
        resetBtn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                mDateTimePicker.reset();
            }
        });

        // Setup TimePicker
        // No title on the dialog window
        mDateTimeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Set the dialog content view
        mDateTimeDialog.setContentView(mDateTimeDialogView);
        // Display the dialog
        mDateTimeDialog.show();
    }

    /**
     * Displays the Android native DatePickerDialog.
     * <p>
     * This method initializes and shows a {@link DatePickerDialog} allowing the user to select a date.
     * It pre-fills the dialog with the date provided in the {@code beanDateTime} parameter.
     * <p>
     * Key functionalities include:
     * <ul>
     *     <li>Setting an initial date from {@code beanDateTime}.</li>
     *     <li>Preventing dismissal of the dialog when touching outside its bounds.</li>
     *     <li>Adding padding to the DatePicker view.</li>
     *     <li>Handling differences in title and icon display for Android versions above and below Nougat (API 23).</li>
     *     <li>Adjusting the dialog's width to 85% of the screen width and height to wrap content.</li>
     *     <li>Setting a custom background color for the dialog window.</li>
     *     <li>Scaling the DatePicker view slightly for better visual appearance.</li>
     *     <li>Applying custom styling (e.g., for API 6 to ensure a non-Holo look).</li>
     *     <li>Hiding the title divider line, especially for Holo-themed dialogs on pre-Lollipop devices.</li>
     *     <li>Customizing the text and typeface of the "Set" and "Cancel" buttons.</li>
     *     <li>Implementing a custom positive button click listener to ensure the selected date is correctly captured,
     *         even when the date is manually entered into an EditText within the DatePicker (by clearing focus).</li>
     * </ul>
     *
     * @param beanDateTime A {@link BeanDateTime} object containing the initial year, month, and day to display in the DatePicker.
     */
    private void showAndroidDatePicker(BeanDateTime beanDateTime) {
        final DatePickerDialog datePickerDialog = new DatePickerDialog(this, R.style.DatePickerDialogTheme, mDateSetListener,
                beanDateTime.getYear(), beanDateTime.getMonth(),
                beanDateTime.getDay());
        datePickerDialog.setCanceledOnTouchOutside(false);

        DatePicker datePicker = datePickerDialog.getDatePicker();
        // Add padding to the DatePicker
        int padding = (int) (16 * getResources().getDisplayMetrics().density); // 16dp padding
        datePicker.setPadding(padding, padding, padding, padding);



        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year = datePicker.getYear();
        Calendar mCalendar = Calendar.getInstance();
        mCalendar.set(year, month, day);

        //Due to Nought issue these line added
        if (Build.VERSION.SDK_INT > 23) {
            datePickerDialog.setTitle("");
        } else {
            datePickerDialog.setIcon(getResources().getDrawable(R.drawable.ic_today_black_icon));
            datePickerDialog.setTitle(mDateFormat.format(mCalendar.getTime()));
        }


        datePickerDialog.onDateChanged(datePickerDialog.getDatePicker(), year, month, day);
        /*This Code for set DatePicker Width full*/
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(datePickerDialog.getWindow().getAttributes());
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        lp.width = (int) (displayMetrics.widthPixels * 0.85);
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        datePickerDialog.show();
        datePickerDialog.getWindow().setAttributes(lp);
        datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(ContextCompat.getColor(this, R.color.backgroundColorView)));
        datePicker.setScaleX(1.2f);


        //This code for API 6 date Picker Color not like holo
        CUtils.applyStyLing(datePickerDialog, currentActivity);

        //hide divider line in title for holo
        try {
            //this condition because setBacgroundDrawable Resource does not work well lollipop and above
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                datePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.white);
            }
            //dg.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND‌​);
            int divierId = datePickerDialog.getContext().getResources()
                    .getIdentifier("android:id/titleDivider", null, null);
            View divider = datePickerDialog.findViewById(divierId);
            divider.setVisibility(View.GONE);

        } catch (Exception e) {
            //android.util.//Log.e("EXCEPTION", "openTimePicker: " + e.getMessage());
        }

        //dg.show();


        Button butOK = (Button) datePickerDialog.findViewById(android.R.id.button1);
        Button butCancel = (Button) datePickerDialog.findViewById(android.R.id.button2);
        butOK.setText(R.string.set);
        butCancel.setText(R.string.cancel);

        butOK.setTypeface(regularTypeface);
        butCancel.setTypeface(regularTypeface);

        CUtils.setCustomDatePickerEdittext(datePickerDialog);
        datePickerDialog.setButton(DialogInterface.BUTTON_POSITIVE, getString(R.string.set), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                // DatePicker datePicker = dg.getDatePicker();

                try {
                    DatePicker datePicker = null;
                    try {
                        Field mDatePickerField = datePickerDialog.getClass().getDeclaredField("mDatePicker");
                        mDatePickerField.setAccessible(true);
                        datePicker = (DatePicker) mDatePickerField.get(datePickerDialog);
                    } catch (Exception ex) {
                        //
                    }
                    // The following clear focus did the trick of saving the date while the date is put manually by the edit text.
                    if (datePicker == null) return;
                    datePicker.clearFocus();
                    mDateSetListener.onDateSet(datePicker, datePicker.getYear(), datePicker.getMonth(), datePicker.getDayOfMonth());

                } catch (Exception ex) {
                    //
                }
            }
        });
    }

    private void updateBirthDate() {
        buttonCalendar.setText(getFormatedTextToShowDate(beanDateTime));
    }

    private void calculateValues() {

        int month = (beanDateTime.getMonth() + 1);
        int day = beanDateTime.getDay();
        String monthStr = "" + month;
        if (month < 10) {
            monthStr = "0" + month;
        }
        String dayStr = "" + day;
        if (day < 10) {
            dayStr = "0" + day;
        }

        dob = dayStr + "-" + monthStr + "-" + beanDateTime.getYear();

        getNumrologyDetails(day, month, beanDateTime.getYear());
    }

    private boolean submitForm() {
        boolean result = false;
        if (validateName(etUserName)) {
            result = true;
        }

        return result;
    }

    private boolean validateName(EditText text) {
        boolean flag = true;

        if (text == etUserName) {
            if (text.getText().toString().trim().isEmpty()) {
                inputLayoutName.setErrorEnabled(true);
                inputLayoutName.setError(getString(R.string.please_enter_name_v));
                etUserName.requestFocus();
                text.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                flag = false;
            } else {
                inputLayoutName.setErrorEnabled(false);
                inputLayoutName.setError(null);
                text.getBackground().setColorFilter(getResources().getColor(R.color.ColorPrimary), PorterDuff.Mode.SRC_ATOP);
            }
        }
        return flag;
    }

    @Override
    public void onDateChanged(Calendar c) {
        beanDateTime.setYear(c.get(Calendar.YEAR));
        beanDateTime.setMonth(c.get(Calendar.MONTH));
        beanDateTime.setDay(c.get(Calendar.DAY_OF_MONTH));
        updateBirthDate();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE_NUMROLOGY_DATA) {

            if (resultCode == RESULT_OK) {
                if (data == null) return;
                Bundle bundle = data.getExtras();
                if (bundle == null) return;
                BeanHoroPersonalInfo beanHoroPersonalInfo = (BeanHoroPersonalInfo) bundle.getSerializable(KEY_KUNDALI_DETAILS);
                if (beanHoroPersonalInfo == null) return;
                etUserName.setText(beanHoroPersonalInfo.getName());
                BeanDateTime beanDateTime = beanHoroPersonalInfo.getDateTime();
                if (beanDateTime == null) return;
                this.beanDateTime.setYear(beanDateTime.getYear());
                this.beanDateTime.setMonth(beanDateTime.getMonth());
                this.beanDateTime.setDay(beanDateTime.getDay());
                updateBirthDate();
            }
        }
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
                    if (HomeMatchMakingInputScreen.isMenuItemClicked) {
                        inputLayoutName.setErrorEnabled(false);
                        inputLayoutName.setError(null);
                        etUserName.getBackground().setColorFilter(null);
                    } else {
                        validateName(etUserName);
                    }
                    break;
            }
        }
    }


    /**
     * get numerology detail
     */
    public void getNumrologyDetails(final int day, final int month, final int year) {

        showProgressBar();
        final String url = CGlobalVariables.numerologyApiUrl;

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @TargetApi(Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(String response) {
                        hideProgressBar();
                        if (response != null && !response.isEmpty()) {
                            try {
                                try {
                                    response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
                                } catch (UnsupportedEncodingException e) {
                                    e.printStackTrace();
                                }
                                //Log.e("response = ", response);
                                Gson gson = new Gson();
                                NumerologyOutputModel numerologyOutputModel = gson.fromJson(response, NumerologyOutputModel.class);

                                Intent intent = new Intent(NumerologyCalculatorInputActivity.this, NumerologyCalculatorOutputActivity.class);
                                intent.putExtra(KEY_NAME, etUserName.getText().toString());
                                intent.putExtra(KEY_DOB, dob);
                                intent.putExtra(KEY_TYPE, typeSpinner.getSelectedItemPosition());
                                intent.putExtra(KEY_NUMROLOGY_MODEL, numerologyOutputModel);
                                startActivity(intent);

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("getNumrologyDetails E= ", error.getMessage() + "");
                hideProgressBar();
                if (error instanceof TimeoutError) {
                    VolleyLog.d("TimeoutError: " + error.getMessage());
                    //If timeout occur it will again hit the request for get the product list
                    //      loadAstroShopData();
                } else if (error instanceof NoConnectionError) {
                    VolleyLog.d("NoConnectionError: " + error.getMessage());
                } else if (error instanceof AuthFailureError) {
                    VolleyLog.d("AuthFailureError: " + error.getMessage());
                    //TODO
                } else if (error instanceof ServerError) {
                    //TODO
                    VolleyLog.d("ServerError: " + error.getMessage());
                } else if (error instanceof NetworkError) {
                    //TODO
                    VolleyLog.d("NetworkError: " + error.getMessage());
                } else if (error instanceof ParseError) {
                    //TODO
                    VolleyLog.d("ParseError: " + error.getMessage());

                }
            }


        }


        ) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            public Map<String, String> getParams() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("key", CUtils.getApplicationSignatureHashCode(context));
                headers.put("languageCode", String.valueOf(LANGUAGE_CODE));
                headers.put("userName", etUserName.getText().toString());
                headers.put("day", String.valueOf(day));
                headers.put("month", String.valueOf(month));
                headers.put("year", String.valueOf(year));
                headers.put("Ctype", String.valueOf(typeSpinner.getSelectedItemPosition()));

                //Log.e("getNumrologyDetails H= ", headers.toString());

                return headers;
            }

        };

        int socketTimeout = 60000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        //stringRequest.setShouldCache(true);
        queue.add(stringRequest);
    }

    private void showProgressBar() {
        pd = new CustomProgressDialog(this, regularTypeface);
        pd.setCanceledOnTouchOutside(false);
        pd.show();
    }

    private void hideProgressBar() {
        try {
            if (pd != null & pd.isShowing())
                pd.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
