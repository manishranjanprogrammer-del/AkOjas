package com.ojassoft.astrosage.ui.act;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.Nullable;
import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;

import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.libojassoft.android.misc.VolleyResponse;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanDateTime;
import com.ojassoft.astrosage.beans.BeanPlace;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.ui.customcontrols.MyNewCustomTimePicker;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.MyTimePickerDialog;
import com.ojassoft.astrosage.utils.TimePicker;
import com.ojassoft.astrosage.varta.utils.FontUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

/**
 * Created By Abhishek Raj
 */
public class NewAppointmentActivity extends BaseInputActivity implements View.OnClickListener, VolleyResponse {

    private final int BOOK_APPOINTMENT = 1;
    private String[] arrStartTime;
    private Activity currentActivity;
    private Toolbar tool_barAppModule;
    private TabLayout tabLayout;
    private TextView tvTitle;
    private CustomProgressDialog pd;
    private TextView lblNameTV;
    private TextView lblEmailTV;
    private TextView lblMobileNoTV;
    private TextView lbldobTV;
    private TextView lblTobTV;
    private TextView lblPobTV;
    private TextView dobTV;
    private TextView tobTV;
    private TextView pobTV;
    private TextView lblPaymentReceivedTV;
    private TextView lblAppointmentDateTV;
    private TextView appointmentDateTV;
    private TextView lblAppointmentTimeTV;
    private TextView lblRemarksTV;
    private TextView lblGenderTV;
    private EditText edtName;
    private EditText edtEmail;
    private EditText edtMobileNo;
    private EditText edtPaymentReceived;
    private EditText edtRemarks;
    private Button btnBook;
    private Spinner genderSpinner;
    private TextView notifyTV;
    private CheckBox notifyCheckBox;
    private TimePickerDialog mTimePicker;
    private Spinner apmtStartTimeSpinner;
    private Spinner apmtEndTimeSpinner;
    private BeanDateTime mBeanDateTime;
    private int dobYear;
    private int dobMonth;
    private int dobDay;
    private Calendar dobCalender;
    private int bookingDateYear;
    private int bookingDateMonth;
    private int bookingDateDay;
    private Calendar bookingDateCalender;
    private BeanPlace mBeanPlace;
    private String tobStr = "";

    public NewAppointmentActivity() {
        super(R.string.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_appointment);
        initContext();
        initViews();
        initListener();
    }

    private void initViews() {
        tool_barAppModule = findViewById(R.id.tool_barAppModule);
        tool_barAppModule.setNavigationIcon(R.drawable.ic_back_arrow);
        setSupportActionBar(tool_barAppModule);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvTitle = findViewById(R.id.tvTitle);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setVisibility(View.GONE);

        lblNameTV = findViewById(R.id.lblNameTV);
        lblEmailTV = findViewById(R.id.lblEmailTV);
        lblMobileNoTV = findViewById(R.id.lblMobileNoTV);
        lbldobTV = findViewById(R.id.lbldobTV);
        lblTobTV = findViewById(R.id.lblTobTV);
        lblPobTV = findViewById(R.id.lblPobTV);
        dobTV = findViewById(R.id.dobTV);
        tobTV = findViewById(R.id.tobTV);
        pobTV = findViewById(R.id.pobTV);
        lblPaymentReceivedTV = findViewById(R.id.lblPaymentReceivedTV);
        lblAppointmentDateTV = findViewById(R.id.lblAppointmentDateTV);
        appointmentDateTV = findViewById(R.id.appointmentDateTV);
        lblAppointmentTimeTV = findViewById(R.id.lblAppointmentTimeTV);
        lblRemarksTV = findViewById(R.id.lblRemarksTV);
        lblGenderTV = findViewById(R.id.lblGenderTV);
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtMobileNo = findViewById(R.id.edtMobileNo);
        edtPaymentReceived = findViewById(R.id.edtPaymentReceived);
        edtRemarks = findViewById(R.id.edtRemarks);
        btnBook = findViewById(R.id.btnBook);
        notifyCheckBox = findViewById(R.id.notifyCheckBox);
        notifyTV = findViewById(R.id.notifyTV);
        apmtStartTimeSpinner = findViewById(R.id.startTimeSpinner);
        apmtEndTimeSpinner = findViewById(R.id.endTimeSpinner);
        genderSpinner = findViewById(R.id.genderSpinner);

        tvTitle.setText(getString(R.string.title_new_appointment));
        tvTitle.setTypeface(regularTypeface);
        lblNameTV.setTypeface(mediumTypeface);
        lblEmailTV.setTypeface(mediumTypeface);
        lblMobileNoTV.setTypeface(mediumTypeface);
        lbldobTV.setTypeface(mediumTypeface);
        lblTobTV.setTypeface(mediumTypeface);
        lblPobTV.setTypeface(mediumTypeface);
        dobTV.setTypeface(regularTypeface);
        tobTV.setTypeface(regularTypeface);
        pobTV.setTypeface(regularTypeface);
        lblPaymentReceivedTV.setTypeface(mediumTypeface);
        lblAppointmentDateTV.setTypeface(mediumTypeface);
        appointmentDateTV.setTypeface(regularTypeface);
        lblAppointmentTimeTV.setTypeface(mediumTypeface);
        lblRemarksTV.setTypeface(mediumTypeface);
        lblGenderTV.setTypeface(mediumTypeface);
        edtName.setTypeface(regularTypeface);
        edtEmail.setTypeface(regularTypeface);
        edtMobileNo.setTypeface(regularTypeface);
        edtPaymentReceived.setTypeface(regularTypeface);
        edtRemarks.setTypeface(regularTypeface);
        btnBook.setTypeface(mediumTypeface);

        mBeanDateTime = new BeanDateTime();
        initGenderSpinner();
        initStartTimeSpinner();
        initEndTimeSpinner();
        setApmtTime();
    }

    private void initContext() {
        currentActivity = NewAppointmentActivity.this;
    }

    private void initListener() {
        pobTV.setOnClickListener(this);
        dobTV.setOnClickListener(this);
        tobTV.setOnClickListener(this);
        appointmentDateTV.setOnClickListener(this);
        btnBook.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.pobTV: {
                openSearchPlace();
                break;
            }
            case R.id.tobTV: {
                openTimePicker(mBeanDateTime);
                break;
            }
            case R.id.dobTV: {
                onDateOfBirthClick();
                break;
            }
            case R.id.appointmentDateTV: {
                onAppointmentDateClick();
                break;
            }
            case R.id.btnBook: {
                if (validateData()) {
                    bookAppointment();
                }
                break;
            }
        }
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


    @Override
    public void onResponse(String response, int method) {
        hideProgressBar();
        Log.e("JoinReqResponse", "response=" + response);
        if (method == BOOK_APPOINTMENT) {
            try {
                JSONObject respObj = new JSONObject(response);
                String status = respObj.getString("responsecode");
                /* 1 for success*/
                if (status.equalsIgnoreCase("1")) {
                    Intent intent = new Intent(this, ApmtBookingConfirmationActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    showSnackbar(btnBook, getResources().getString(R.string.failed_book_appointment));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                showSnackbar(btnBook, e.toString());
            }
        }
    }

    @Override
    public void onError(VolleyError error) {
        hideProgressBar();
        Log.e("JoinReqResponse error", error.toString());
        showSnackbar(btnBook, error.getMessage());
    }

    private boolean validateData() {

        if (edtName.getText().toString().trim().isEmpty()) {
            requestFocus(edtName);
            showSnackbar(btnBook, getResources().getString(R.string.please_enter_name_v));
            return false;
        }
        if (edtEmail.getText().toString().trim().isEmpty() && edtMobileNo.getText().toString().trim().isEmpty()) {
            requestFocus(edtEmail);
            showSnackbar(btnBook, getResources().getString(R.string.please_enter_email_or_mobile));
            return false;
        }
        if (!edtEmail.getText().toString().trim().isEmpty()) {
            if (edtEmail.getText().toString().trim().length() > 70) {
                requestFocus(edtEmail);
                showSnackbar(btnBook, getResources().getString(R.string.email_two_v));
                return false;
            } else if (!CUtils.isValidEmail(edtEmail.getText().toString().trim())) {
                requestFocus(edtEmail);
                showSnackbar(btnBook, getResources().getString(R.string.email_three_v));
                return false;
            }
        }
        if (!edtMobileNo.getText().toString().trim().isEmpty()) {
            if (edtMobileNo.getText().toString().trim().length() < 10) {
                requestFocus(edtMobileNo);
                showSnackbar(btnBook, getResources().getString(R.string.resend_otp_failed_msg));
                return false;
            }
        }
        if (appointmentDateTV.getText().toString().trim().isEmpty()) {
            showSnackbar(btnBook, getResources().getString(R.string.please_select_apmt_date));
            return false;
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
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

    public void openSearchPlace() {
        if (mBeanPlace == null) {
            mBeanPlace = CUtils.getUserDefaultCity(this);
        }
        Intent intent = new Intent(this, ActPlaceSearch.class);
        intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, true);
        intent.putExtra(CGlobalVariables.PLACE_BEAN_KEY, mBeanPlace);
        startActivityForResult(intent, SUB_ACTIVITY_PLACE_SEARCH);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SUB_ACTIVITY_PLACE_SEARCH:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        mBeanPlace = CUtils.getPlaceObjectFromBundle(bundle);
                        if (mBeanPlace != null) {
                            pobTV.setText(mBeanPlace.getCityName() + ", " + mBeanPlace.getState());
                        }
                    }
                }
                break;
        }
    }

    private void initStartTimeSpinner() {
        arrStartTime = getResources().getStringArray(R.array.arr_apmt_time);
        ArrayAdapter<CharSequence> startTimeAdapter = new ArrayAdapter<CharSequence>(currentActivity, R.layout.spinner_list_item, arrStartTime) {

            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                setSpinnerView(v);
                if (position < arrStartTime.length-1) {
                    apmtEndTimeSpinner.setSelection(position + 1);
                }
                if (position < arrStartTime.length-1) {
                    apmtEndTimeSpinner.setSelection(position + 1);
                }else if (position == arrStartTime.length-1) {
                    apmtEndTimeSpinner.setSelection(0);
                }
                return v;
            }

            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                if (v != null) {
                    ((TextView) v).setTypeface(regularTypeface);
                    ((TextView) v).setTextSize(16);
                }
                return v;
            }
        };
        apmtStartTimeSpinner.setAdapter(startTimeAdapter);
    }

    private void initEndTimeSpinner() {
        String[] arrEndTime = getResources().getStringArray(R.array.arr_apmt_time);
        ArrayAdapter<CharSequence> endTimeAdapter = new ArrayAdapter<CharSequence>(currentActivity, R.layout.spinner_list_item, arrEndTime) {

            @Override
            public boolean isEnabled(int position) {
                return position > apmtStartTimeSpinner.getSelectedItemPosition();
            }

            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                setSpinnerView(v);
                return v;
            }

            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                if (v != null) {
                    ((TextView) v).setTypeface(regularTypeface);
                    ((TextView) v).setTextSize(16);
                    if (isEnabled(position)) {
                        v.setAlpha(1);
                    } else {
                        v.setAlpha(0.6f);
                    }
                }
                return v;
            }
        };
        apmtEndTimeSpinner.setAdapter(endTimeAdapter);
    }

    private void setApmtTime() {
        if (getIntent() != null) {
            String apmtStartTime = getIntent().getStringExtra("apmtStartTime");
            if (!TextUtils.isEmpty(apmtStartTime)) {
                int timePos = 0;
                for (int i = 0; i < arrStartTime.length; i++) {
                    String time = arrStartTime[i];
                    if (time == null) continue;
                    if (time.equalsIgnoreCase(apmtStartTime)) {
                        timePos = i;
                        break;
                    }
                }
                final int pos = timePos;
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        apmtStartTimeSpinner.setSelection(pos);
                        if (pos < arrStartTime.length-1) {
                            apmtEndTimeSpinner.setSelection(pos + 1);
                        }else if (pos == arrStartTime.length-1) {
                            apmtEndTimeSpinner.setSelection(0);
                        }
                    }
                }, 500);
            }
        }
    }

    private void setSpinnerView(View v) {
        if (v != null) {
            ((TextView) v).setTypeface(regularTypeface);
            ((TextView) v).setTextSize(16);
            v.setPadding(CUtils.convertDpToPx(currentActivity, 16), 0, 0, 0);
        }
    }

    private void initGenderSpinner() {
        String[] arrEndTime = getResources().getStringArray(R.array.gender_list_new);
        ArrayAdapter<CharSequence> endTimeAdapter = new ArrayAdapter<CharSequence>(currentActivity, R.layout.spinner_list_item, arrEndTime) {

            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                setSpinnerView(v);
                return v;
            }

            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                if (v != null) {
                    ((TextView) v).setTypeface(regularTypeface);
                    ((TextView) v).setTextSize(16);
                }
                return v;
            }
        };
        genderSpinner.setAdapter(endTimeAdapter);
    }

    public void openTimePicker(final BeanDateTime beanDateTime) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            final MyTimePickerDialog.OnTimeSetListener myTimeSetListener = new MyTimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute, int seconds) {
                    beanDateTime.setHour(hourOfDay);
                    beanDateTime.setMin(minute);
                    beanDateTime.setSecond(seconds);
                    tobStr = hourOfDay + ":" + minute + ":" + seconds;
                    tobTV.setText(CUtils.convertTimeToHrMtScAmPm(hourOfDay + ":" + minute + ":" + seconds));
                }
            };


            final MyTimePickerDialog mTimePicker = new MyTimePickerDialog(currentActivity, R.style.AppCompatAlertDialogStyle, myTimeSetListener, beanDateTime.getHour(), (beanDateTime.getMin()), (beanDateTime.getSecond()));
            mTimePicker.setCanceledOnTouchOutside(false);
            mTimePicker.setIcon(getResources().getDrawable(R.drawable.timer_title));
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

            }
            butOK.setText(R.string.set);
            butOK.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary_day_night));
            FontUtils.changeFont(this,butOK, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
            butCancel.setText(R.string.cancel);
            FontUtils.changeFont(this,butCancel, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);

            butCancel.setTextColor(getResources().getColor(R.color.black));


        } else {
            showCustomTimePickerDialog(beanDateTime);
        }

    }

    private void showCustomTimePickerDialog(BeanDateTime beanDateTime) {
        Intent intent = new Intent(this, MyNewCustomTimePicker.class);
        intent.putExtra("H", beanDateTime.getHour());
        intent.putExtra("M", beanDateTime.getMin());
        intent.putExtra("S", beanDateTime.getSecond());
        intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, SELECTED_MODULE);
        startActivityForResult(intent, CGlobalVariables.WIZARD_ACTIVITY_TIME_PICKER);
    }

    /**
     * onDateOfBirthClick
     */
    private void onDateOfBirthClick() {
        if (dobCalender == null) {
            dobCalender = Calendar.getInstance();
        }
        dobYear = dobCalender.get(Calendar.YEAR);
        dobMonth = dobCalender.get(Calendar.MONTH);
        dobDay = dobCalender.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(currentActivity,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        dobTV.setText(CUtils.appendZeroOnSingleDigit(day) + "/" + CUtils.appendZeroOnSingleDigit((month + 1)) + "/" + year);

                        dobCalender.set(Calendar.YEAR, year);
                        dobCalender.set(Calendar.MONTH, month);
                        dobCalender.set(Calendar.DAY_OF_MONTH, day);
                    }
                }, dobYear, dobMonth, dobDay);
        datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis());
        datePickerDialog.show();
    }

    /**
     * onAppointmentDateClick
     */
    private void onAppointmentDateClick() {
        if (bookingDateCalender == null) {
            bookingDateCalender = Calendar.getInstance();
        }
        bookingDateYear = bookingDateCalender.get(Calendar.YEAR);
        bookingDateMonth = bookingDateCalender.get(Calendar.MONTH);
        bookingDateDay = bookingDateCalender.get(Calendar.DAY_OF_MONTH);
        DatePickerDialog datePickerDialog = new DatePickerDialog(currentActivity,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        appointmentDateTV.setText(CUtils.appendZeroOnSingleDigit(day) + "/" + CUtils.appendZeroOnSingleDigit((month + 1)) + "/" + year);
                        bookingDateCalender.set(Calendar.YEAR, year);
                        bookingDateCalender.set(Calendar.MONTH, month);
                        bookingDateCalender.set(Calendar.DAY_OF_MONTH, day);
                    }
                }, bookingDateYear, bookingDateMonth, bookingDateDay);
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis());
        datePickerDialog.show();
    }


    public void bookAppointment() {
        CUtils.hideMyKeyboard(currentActivity);
        if (CUtils.isConnectedWithInternet(currentActivity)) {
            showProgressBar();
            CUtils.bookAppointmentRequest(NewAppointmentActivity.this, getBookAppointmentRequestParams(), BOOK_APPOINTMENT);
        } else {
            showSnackbar(btnBook, getResources().getString(R.string.no_internet));
        }
    }

    public Map<String, String> getBookAppointmentRequestParams() {

        if (mBeanPlace == null) {
            mBeanPlace = new BeanPlace();
        }
        String key = CUtils.getApplicationSignatureHashCode(currentActivity);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put(CGlobalVariables.KEY_API, key);
        params.put("userid", CUtils.getUserName(currentActivity));
        params.put("isapi", "1");
        params.put("name", edtName.getText().toString());
        params.put("sex", genderSpinner.getSelectedItemPosition() == 0?"male":"female");
        params.put("email", edtEmail.getText().toString());
        params.put("mobile", edtMobileNo.getText().toString());
        params.put("dob", dobTV.getText().toString());
        params.put("tob", tobStr);
        params.put("apntdate", appointmentDateTV.getText().toString());
        params.put("apntstarttime", apmtStartTimeSpinner.getSelectedItem().toString());
        params.put("apntendtime", apmtEndTimeSpinner.getSelectedItem().toString());
        params.put("apntpayment", edtPaymentReceived.getText().toString());
        params.put("remarks", edtRemarks.getText().toString());
        params.put("isnotify", notifyCheckBox.isChecked() ? "on" : "off");
        //params.put("apntid", "");

        params.put("place", mBeanPlace.getCityName() == null ? "" : mBeanPlace.getCityName());
        params.put("state", mBeanPlace.getState() == null ? "" : mBeanPlace.getState());
        params.put("country", mBeanPlace.getCountryName() == null ? "" : mBeanPlace.getCountryName());
        params.put("latdeg", mBeanPlace.getLatDeg() == null ? "" : mBeanPlace.getLatDeg());
        params.put("latmin", mBeanPlace.getLatMin() == null ? "" : mBeanPlace.getLatMin());
        params.put("latns", mBeanPlace.getLatDir() == null ? "" : mBeanPlace.getLatDir());
        params.put("longdeg", mBeanPlace.getLongDeg() == null ? "" : mBeanPlace.getLongDeg());
        params.put("longmin", mBeanPlace.getLongMin() == null ? "" : mBeanPlace.getLongMin());
        params.put("longew", mBeanPlace.getLongDir() == null ? "" : mBeanPlace.getLongDir());
        params.put("timezone", mBeanPlace.getTimeZoneValue() + "");
        params.put("timezoneid", mBeanPlace.getTimeZoneString() == null ? "" : mBeanPlace.getTimeZoneString());
        params.put("dst", "0");


        //Log.e("JoinReqResponse", "params="+params.toString());
        return params;
    }
}
