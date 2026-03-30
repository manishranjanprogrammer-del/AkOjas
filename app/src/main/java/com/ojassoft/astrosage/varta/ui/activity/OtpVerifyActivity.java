package com.ojassoft.astrosage.varta.ui.activity;

import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_PASSWORD;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_USER_ID;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_FILTER_TYPE;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.NEW_USER_OTP_SEND;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.appevents.AppEventsConstants;
import com.google.android.gms.auth.api.phone.SmsRetriever;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.Status;
import com.google.gson.Gson;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.Receiver.IncomingSmsReceiver;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.act.ActAppModule;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.varta.customwidgets.CustomEditText;
import com.ojassoft.astrosage.varta.interfacefile.CancellingResendOtpThread;
import com.ojassoft.astrosage.varta.interfacefile.CustomEditTextListener;
import com.ojassoft.astrosage.varta.model.UserProfileData;
import com.ojassoft.astrosage.varta.service.PreFetchDataservice;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.CreateCustomLocalNotification;
import com.ojassoft.astrosage.varta.utils.CustomProgressDialog;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;
import com.ojassoft.astrosage.varta.volley_network.VolleyServiceHandler;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class OtpVerifyActivity extends BaseActivity implements View.OnClickListener, VolleyResponse, CancellingResendOtpThread {

    public static final long timeafterwhichRecieverUnregister = 900000l;
    private static final String FORMAT = "%02d:%02d";
    private static final long longTimeAfterWhichButtonEnable = 91000;
    private static final long longTotalVerificationTime = 91000;
    private static final long longOneSecond = 1000;
    public static Runnable r;
    static int resendOtpCount = 1;
    private static Runnable rButtonVisibility;
    private static Handler handlerButtonVisibility;
    Button verifyBtn;
    TextView resendOtp, otpHeadingTxt, otpMsgTxt;
    CustomEditText otpEdt1, otpEdt2, otpEdt3, otpEdt4;
    String mobileNumber = "";
    CustomProgressDialog pd;
    ScrollView containerLayout;
    RequestQueue queue;
    int RESEND_OTP_METHOD = 33;
    int RESEND_OTP_VIA_PHONE_METHOD = 44;
    String isFromScreen = "";
    Activity activity;
    TextView astrosageText;
    private TextView textViewTimer,txtViewResendOtp;
    private int intNumberOfTimesResendPress = 0;
    private CountDownTimer countDownTimer;
    private CountDownTimer timerForUnregisteringBroadcastReciever;
    private boolean istimerForUnregisteringBroadcastRecieverrunning;
    private BroadcastReceiver smsReciever;
    private Boolean recieverRegistered = false;
    private TextView getOtpViaPhone;
    private String newUser = "0";
    private String astroSageUserId = "";
    private String _userId = "", _pwd = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp_verify);
        initView();
    }

    private void initView() {
        activity = OtpVerifyActivity.this;
        containerLayout = findViewById(R.id.container_layout);
        verifyBtn = findViewById(R.id.verify_btn);
        resendOtp = findViewById(R.id.resend_otp);
        getOtpViaPhone = findViewById(R.id.get_otp_via_phone);
        otpEdt1 = findViewById(R.id.otp_edt1);
        otpEdt2 = findViewById(R.id.otp_edt2);
        otpEdt3 = findViewById(R.id.otp_edt3);
        otpEdt4 = findViewById(R.id.otp_edt4);
        otpHeadingTxt = findViewById(R.id.otp_heading_txt);
        otpMsgTxt = findViewById(R.id.otp_msg_txt);

        textViewTimer = findViewById(R.id.textViewTimer);
        txtViewResendOtp = findViewById(R.id.txtViewResendOtp);
        astrosageText = findViewById(R.id.astrosage_txt);
        cursorMoveOtpAutomatically();
        initEditTextPasteListner();
        resendOtp.setText(Html.fromHtml(getResources().getString(R.string.resend_otp).trim()));
        getOtpViaPhone.setText(Html.fromHtml(getResources().getString(R.string.get_otp_via_phone).trim()));

        FontUtils.changeFont(OtpVerifyActivity.this, astrosageText, CGlobalVariables.FONTS_DIAVLO_BOLD);
        FontUtils.changeFont(OtpVerifyActivity.this, verifyBtn, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(OtpVerifyActivity.this, otpHeadingTxt, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(OtpVerifyActivity.this, otpMsgTxt, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(OtpVerifyActivity.this, txtViewResendOtp, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(OtpVerifyActivity.this, resendOtp, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(OtpVerifyActivity.this, getOtpViaPhone, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(OtpVerifyActivity.this, textViewTimer, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(OtpVerifyActivity.this, otpEdt1, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(OtpVerifyActivity.this, otpEdt2, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(OtpVerifyActivity.this, otpEdt3, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(OtpVerifyActivity.this, otpEdt4, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);

        initSmsReceiver();
        setOtpTimer();

        verifyBtn.setOnClickListener(this);
        queue = VolleySingleton.getInstance(OtpVerifyActivity.this).getRequestQueue();
        if (getIntent().getExtras() != null) {
            mobileNumber = getIntent().getExtras().getString(CGlobalVariables.PHONE_NO);
            isFromScreen = getIntent().getExtras().getString(CGlobalVariables.IS_FROM_SCREEN);
            newUser = getIntent().getExtras().getString(CGlobalVariables.NEW_USER);
            astroSageUserId = getIntent().getExtras().getString(KEY_USER_ID);
        }
        String msg = getIntent().getStringExtra(CGlobalVariables.MESSAGES_FBD_KEY);
        //Log.e("TestOtp", "msg="+msg);
        if(!TextUtils.isEmpty(msg)) {
            //otpMsgTxt.setText(Html.fromHtml(msg));
            String newText = "<html><body><img src='whatsapp_small'></body></html>";
            msg = msg.replace("##",newText);
            otpMsgTxt.setText(Html.fromHtml(msg, source -> {
                int id;

                id = getResources().getIdentifier(source, "drawable", getPackageName());

                if (id == 0) {
                    // the drawable resource wasn't found in our package, maybe it is a stock android drawable?
                    id = getResources().getIdentifier(source, "drawable", "android");
                }

                if (id == 0) {
                    // prevent a crash if the resource still can't be found
                    return null;
                }
                else {
                    Drawable d = getResources().getDrawable(id);
                    d.setBounds(0,0,d.getIntrinsicWidth(),d.getIntrinsicHeight());
                    return d;
                }
            }, null));

            /*if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                otpMsgTxt.setText(Html.fromHtml(msg, Html.FROM_HTML_MODE_LEGACY));
            } else {
                otpMsgTxt.setText(Html.fromHtml(msg));
            }*/
            //CUtils.showSnackbar(containerLayout, msg, OtpVerifyActivity.this);
        }

        // Create a SpannableString for the text
       // SpannableString spannableString = new SpannableString(getString(R.string.we_have_sent_a_verification_code_to,"+"+CUtils.getCountryCode(activity)+"-"+mobileNumber));
// Create a SpannableString for the text
        String text = getString(R.string.we_have_sent_a_verification_code_to, "+" + CUtils.getCountryCode(activity) + "-" + mobileNumber);
        SpannableString spannableString = new SpannableString(text + " "); // Add space for the icon
// Get the position of the mobile number in the string
        int mobileNumberStart = text.indexOf(mobileNumber);
        int mobileNumberEnd = mobileNumberStart + mobileNumber.length(); // End position of the mobile number

        // Get the drawable resource from mipmap
        Drawable drawable = ContextCompat.getDrawable(this, R.mipmap.edit); // Replace 'ic_launcher' with your mipmap image

        if (drawable != null) {

            // Set custom size for the image (width and height)
            int width = 30;  // desired width in pixels
            int height = 30; // desired height in pixels
            int marginStart = com.ojassoft.astrosage.utils.CUtils.convertDpToPx(this,15); // Convert 15dp to pixels for margin
            drawable.setBounds(marginStart, 0, marginStart + width, height); // Add margin to the image
        }

        // Create ImageSpan to insert the image at the end of the text
        ImageSpan imageSpan = new ImageSpan(drawable, ImageSpan.ALIGN_BASELINE);
        //spannableString.setSpan(imageSpan, spannableString.length() - 1, spannableString.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        // Add the ImageSpan right after the mobile number
        spannableString.setSpan(imageSpan, mobileNumberEnd, mobileNumberEnd + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE); // Insert icon after the mobile number

        // Add ClickableSpan for the image
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                //Toast.makeText(OtpVerifyActivity.this, "Image clicked", Toast.LENGTH_SHORT).show();
                onBackPressed();
            }
        };
        //spannableString.setSpan(clickableSpan, spannableString.length() - 1, spannableString.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
// Make the icon (ImageSpan) clickable by setting ClickableSpan
        spannableString.setSpan(clickableSpan, mobileNumberEnd, mobileNumberEnd + 1, Spannable.SPAN_INCLUSIVE_EXCLUSIVE);

        // Set the SpannableString to TextView
        otpMsgTxt.setText(spannableString);
        otpMsgTxt.setMovementMethod(LinkMovementMethod.getInstance()); // To handle clicks

       // otpMsgTxt.setText(getString(R.string.we_have_sent_a_verification_code_to,"+"+CUtils.getCountryCode(activity)+"-"+mobileNumber));
        SmsRetriever.getClient(this).startSmsUserConsent( null );
    }
    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(SmsRetriever.SMS_RETRIEVED_ACTION);
        ContextCompat.registerReceiver(this, smsVerificationReceiver, intentFilter, ContextCompat.RECEIVER_EXPORTED);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(smsVerificationReceiver != null){
            unregisterReceiver(smsVerificationReceiver);
        }
    }

    private void initSmsReceiver() {
        smsReciever = new IncomingSmsReceiver(OtpVerifyActivity.this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(CGlobalVariables.EVENT_SMS_RECEIVED);
        intentFilter.setPriority(CGlobalVariables.PRIORITY_HIGH);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.registerReceiver(this,smsReciever, intentFilter,ContextCompat.RECEIVER_EXPORTED);
        }else {
            registerReceiver(smsReciever, intentFilter);
        }
        recieverRegistered = true;
        functionForUnregisteringBroadcastReceiever();
    }

    private void setOtpTimer() {
        resendOtp.setOnClickListener(null);
        resendOtp.setClickable(false);
        resendOtp.setTextColor(ContextCompat.getColor(this, R.color.new_theme_darker_gray));
        //resendOtp.setAlpha(0.5f);
        resendOtp.setVisibility(View.GONE);
        getOtpViaPhone.setOnClickListener(null);
        getOtpViaPhone.setClickable(false);
        getOtpViaPhone.setTextColor(ContextCompat.getColor(this, R.color.new_theme_darker_gray));

        //getOtpViaPhone.setAlpha(0.5f);

        // Remove underline
        getOtpViaPhone.setPaintFlags(getOtpViaPhone.getPaintFlags() & (~Paint.UNDERLINE_TEXT_FLAG));
        functionInitializingCountDownTimer();
    }

    /*private void cursorMoveOtpAutomatically() {

        otpEdt1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() == 1) {
                    otpEdt2.requestFocus();
                }
            }
        });

        otpEdt1.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_DEL) {
                    if (otpEdt1.getText().toString().length() == 1) {
                        otpEdt1.requestFocus();
                    }
                }

                return false;
            }
        });

        otpEdt2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() == 1) {
                    otpEdt3.requestFocus();
                }
                if (editable.toString().isEmpty()) {
                    otpEdt1.requestFocus();
                }
            }
        });

        otpEdt2.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if(keyCode == KeyEvent.KEYCODE_DEL) {
                    if (otpEdt2.getText().toString().length() == 1) {
                        otpEdt2.requestFocus();
                    }
                    if (otpEdt2.getText().toString().isEmpty()) {
                        otpEdt1.requestFocus();
                    }
                }

                return false;
            }
        });

        otpEdt3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                //Log.e("SAN ", " otpEdt3 afterTextChanged editable.toString() " + editable.toString() );

                if (editable.toString().length() == 1) {
                    otpEdt4.requestFocus();
                }
                if (editable.toString().isEmpty()) {
                    otpEdt2.requestFocus();
                }
            }
        });

        otpEdt3.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                //Log.e("SAN ", " otpEdt3.setOnKeyListener " + keyCode +" <=> " + KeyEvent.KEYCODE_DEL);

                if(keyCode == KeyEvent.KEYCODE_DEL) {
                    if (otpEdt3.getText().toString().length() == 1) {
                        otpEdt3.requestFocus();
                    }
                    if (otpEdt3.getText().toString().isEmpty()) {
                        otpEdt2.requestFocus();
                    }
                }

                return false;
            }
        });

        otpEdt4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                //Log.e("SAN ", " otpEdt4 afterTextChanged editable.toString() " + editable.toString() );

                if (editable.toString().isEmpty()) {
                    otpEdt3.requestFocus();
                }
            }
        });

        otpEdt4.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                //Log.e("SAN ", " otpEdt4.setOnKeyListener " + keyCode +" <=> " + KeyEvent.KEYCODE_DEL);

                if(keyCode == KeyEvent.KEYCODE_DEL) {
                    if (otpEdt4.getText().toString().isEmpty()) {
                        otpEdt3.requestFocus();
                    }
                }

                return false;
            }
        });

    }
    */

    private void cursorMoveOtpAutomatically(){
        otpEdt1.addTextChangedListener(new GenericTextWatcher(otpEdt1, otpEdt2));
        otpEdt2.addTextChangedListener(new GenericTextWatcher(otpEdt2, otpEdt3));
        otpEdt3.addTextChangedListener(new GenericTextWatcher(otpEdt3, otpEdt4));
        otpEdt4.addTextChangedListener(new GenericTextWatcher(otpEdt4, null));

        otpEdt2.setOnKeyListener( new GenericKeyEvent ( otpEdt2 , otpEdt1 ) );
        otpEdt3.setOnKeyListener( new GenericKeyEvent ( otpEdt3 , otpEdt2 ) );
        otpEdt4.setOnKeyListener( new GenericKeyEvent ( otpEdt4 , otpEdt3 ) );
    }


    private void initEditTextPasteListner() {
        otpEdt1.addListener(new CustomEditTextListener() {
            @Override
            public void onUpdate() {
                onOtpPaste();
            }
        });
        otpEdt2.addListener(new CustomEditTextListener() {
            @Override
            public void onUpdate() {
                onOtpPaste();
            }
        });
        otpEdt3.addListener(new CustomEditTextListener() {
            @Override
            public void onUpdate() {
                onOtpPaste();
            }
        });
        otpEdt4.addListener(new CustomEditTextListener() {
            @Override
            public void onUpdate() {
                onOtpPaste();
            }
        });
    }

    private void onOtpPaste() {
        try {
            String otpStr = getCopiedOtpFromClipboard();
            autoFillOtp(otpStr);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.verify_btn:
                String otpNumberStr = otpEdt1.getText().toString().trim() + otpEdt2.getText().toString().trim() +
                        otpEdt3.getText().toString().trim() + otpEdt4.getText().toString().trim();

                if (isValidData(otpNumberStr)) {
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_OTP_VERIFY_BTN,
                            CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                    if (recieverRegistered == true) {
                        unregisterReceiver(smsReciever);
                        recieverRegistered = false;
                    }
                    if (istimerForUnregisteringBroadcastRecieverrunning) {
                        timerForUnregisteringBroadcastReciever.cancel();
                        istimerForUnregisteringBroadcastRecieverrunning = false;
                    }
                    verifyOtp(otpNumberStr);
                } else {
                    CUtils.showSnackbar(containerLayout, getResources().getString(R.string.enter_valid_otp_no), OtpVerifyActivity.this);
                }
                break;

            case R.id.resend_otp:
                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_RESEND_OTP_BTN,
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                intNumberOfTimesResendPress = intNumberOfTimesResendPress + 1;
                if (intNumberOfTimesResendPress <= CGlobalVariables.RESET_LIMIT) {
                    if (istimerForUnregisteringBroadcastRecieverrunning) {
                        timerForUnregisteringBroadcastReciever.cancel();
                        istimerForUnregisteringBroadcastRecieverrunning = false;
                    }
                    functionForUnregisteringBroadcastReceiever();
                    setOtpTimer();

                    callLoginReSendOtpApi();
                  // resendOtp(CGlobalVariables.REGISTRATION_URL, RESEND_OTP_METHOD);
                } else {
                    CUtils.showSnackbar(containerLayout, getResources().getString(R.string.resend_otp_max_limit), OtpVerifyActivity.this);
                }
                break;

            case R.id.get_otp_via_phone:
                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_RESEND_OTP_VIA_PHONE_BTN,
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                intNumberOfTimesResendPress = intNumberOfTimesResendPress + 1;
                if (intNumberOfTimesResendPress <= CGlobalVariables.RESET_LIMIT) {
                    setOtpTimer();
                    callLoginReSendViaCallOtpApi();
                   // resendOtp(CGlobalVariables.CALL_OVER_OTP_URL, RESEND_OTP_VIA_PHONE_METHOD);
                } else {
                    CUtils.showSnackbar(containerLayout, getResources().getString(R.string.resend_otp_max_limit), OtpVerifyActivity.this);
                }
                break;
        }
    }

    private boolean isValidData(String otpNo) {
        boolean isValid = otpNo != null || otpNo.trim().length() != 0;
        if (otpNo.trim().length() < 4) {
            isValid = false;
        }
        return isValid;
    }

    private void verifyOtp(String otpData) {
       // Log.e("broad_cast", "start timer: "+System.currentTimeMillis() );
        CUtils.hideMyKeyboard(this);
        if (!CUtils.isConnectedWithInternet(OtpVerifyActivity.this)) {
            CUtils.showSnackbar(containerLayout, getResources().getString(R.string.no_internet), OtpVerifyActivity.this);
        } else {
            if (pd == null)
                pd = new CustomProgressDialog(OtpVerifyActivity.this);
            pd.show();
            pd.setCancelable(false);
            callVerifyOtp(otpData);
//            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.VERIFY_OTP_URL,
//                    OtpVerifyActivity.this, false, getParams(otpData), 1).getMyStringRequest();
//            stringRequest.setShouldCache(false);
//            queue.add(stringRequest);
           // Log.e("broad_cast", "end timer: "+System.currentTimeMillis() );

        }
    }

    private void callVerifyOtp(String otpData) {
//        Context context = AstrosageKundliApplication.getAppContext();
//        int kundliCount = com.ojassoft.astrosage.utils.CUtils.getKundliCount(this);
//        String strFirstLoginAfterPlanPurchase = "";
//        if (com.ojassoft.astrosage.utils.CUtils.getBooleanData(this, com.ojassoft.astrosage.utils.CGlobalVariables.needToSendDeviceIdForLogin, false)) {
//            strFirstLoginAfterPlanPurchase = com.ojassoft.astrosage.utils.CGlobalVariables.FIRST_LOGIN_AFTER_PLAN_PURCHASED;
//        }
//        if (astroSageUserId == null) astroSageUserId = "";

        ApiList api = RetrofitClient.getInstance().create(ApiList.class);
        Call<ResponseBody> call = api.verifyOtp(getParams(otpData));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    if (response.body() != null) {
                        try {
                            hideProgressBar();
                            String myResponse = response.body().string();
                            //Log.d("otpVerify", "onResponse response: "+myResponse);
                            Log.e("TestLogin", "myResponse" + myResponse);
                            JSONObject jsonObject = new JSONObject(myResponse);
                            try {
                                CUtils.saveAstrologerIDAndChannelID(OtpVerifyActivity.this,"","");
                                String status = jsonObject.getString("status");
                                String errorCode = getResources().getString(R.string.error_code).replace("#", status);
                                if (status.equals(CGlobalVariables.OTP_VERIFIED_AND_RETURN_USERDATA)) {
                                    if(newUser != null && newUser.equals(NEW_USER_OTP_SEND)){//new registration
                                        CUtils.addFacebookAndFirebaseEvent(AppEventsConstants.EVENT_NAME_COMPLETED_REGISTRATION, "new_register", "OtpVerifyActivity");
                                    }
                                    CUtils.saveBannerData("");
                                    startPrefatchDataService();
                                    CUtils.startFollowerSubscriptionService(OtpVerifyActivity.this);

                                    com.ojassoft.astrosage.varta.utils.CUtils.unSubscribeTopics("", com.ojassoft.astrosage.varta.utils.CGlobalVariables.TOPIC_NOT_LOGGEDIN, OtpVerifyActivity.this);
                                    com.ojassoft.astrosage.varta.utils.CUtils.subscribeTopics("", com.ojassoft.astrosage.varta.utils.CGlobalVariables.TOPIC_LOGGEDIN, OtpVerifyActivity.this);

                                    BeanHoroPersonalInfo beanHoroPersonalInfo = com.ojassoft.astrosage.utils.CUtils.getBeanHoroscopeInfoObject(jsonObject);
                                    com.ojassoft.astrosage.utils.CUtils.saveDefaultKundliIfAvailable(activity,beanHoroPersonalInfo);

                                    UserProfileData userProfileDataBean = parseJsonObject(myResponse);
                                    CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_SIGNUP_LOGIN_SUCCESS, CGlobalVariables.FIREBASE_EVENT_SIGNUP_LOGIN, "");
                                    //save password in preference
                                    CUtils.setUserLoginPassword(OtpVerifyActivity.this, jsonObject.getString(KEY_PASSWORD));
                                    CUtils.userOfferAfterLogin = userProfileDataBean.getPrivateintrooffertype();
                                    CUtils.setUserOffers(OtpVerifyActivity.this, userProfileDataBean.isLiveintrooffer(), userProfileDataBean.getPrivateintrooffertype());
                                    // get and set user unique refer code
                                    CUtils.setSecondFreeChat(OtpVerifyActivity.this, userProfileDataBean.isSecondFreeChat());

                                    String bonusStatus = "0", isShowOneRsPopup = "0", amountOnPopup = "0", freeSessionPopUp = "0";
                                    if (jsonObject.has("eligibleforsignupbonus")) {
                                        bonusStatus = jsonObject.getString("eligibleforsignupbonus");
                                    }
                                    if (jsonObject.has("showonerepopup")) {
                                        isShowOneRsPopup = jsonObject.getString("showonerepopup");
                                        CUtils.setDataForOneRsDialog(OtpVerifyActivity.this, isShowOneRsPopup);
                                    }
                                    if (jsonObject.has("showfreesessionpopup")) {
                                        freeSessionPopUp = jsonObject.getString("showfreesessionpopup");
                                        CUtils.setDataForFreeSessionDialog(OtpVerifyActivity.this, freeSessionPopUp);
                                    }
                                    if (jsonObject.has("amountonpopup")) {
                                        amountOnPopup = jsonObject.getString("amountonpopup");
                                        CUtils.setAmountOnDialog(OtpVerifyActivity.this, amountOnPopup);
                                    }

                                    String bonustitle = "", bonusdescription = "";
                                    if (jsonObject.has("bonustitle")) {
                                        bonustitle = jsonObject.getString("bonustitle");
                                    }
                                    if (jsonObject.has("bonusdescription")) {
                                        bonusdescription = jsonObject.getString("bonusdescription");
                                    }

                                    if (!TextUtils.isEmpty(bonustitle) && !TextUtils.isEmpty(bonusdescription)) {
                                        boolean isNotificationShown = CUtils.getBooleanData(OtpVerifyActivity.this,CGlobalVariables.IS_49_ADDED_NOTIF_SHOWN,false);
                                        if (!isNotificationShown) {
                                            CUtils.saveBooleanData(OtpVerifyActivity.this, CGlobalVariables.IS_49_ADDED_NOTIF_SHOWN, true);
                                            CreateCustomLocalNotification notification = new CreateCustomLocalNotification(OtpVerifyActivity.this);
                                            notification.showLocalNotification(bonustitle, bonusdescription, true);
                                        }
                                    }

                                    try {
                                        CUtils.setUserIdForBlock(OtpVerifyActivity.this, jsonObject.getString("userid"));
                                        String blockedby = jsonObject.getString("blockedby");
                                        String[] blockByAstrologer = blockedby.split("\\s*,\\s*");
                                        CUtils.setblockByAstrologerList(blockByAstrologer);
                                    } catch (Exception e) {
                                        //Log.d("otpVerify", "onFailure e4: "+e);
                                    }

                                    CUtils.saveLoginDetailInPrefs(OtpVerifyActivity.this, mobileNumber, true, userProfileDataBean.getWalletbalance(), bonusStatus);
                                    // clear AiRandom Chat Astrologer Details and fetch new Astro a/c to login cred.
//                                    if(!TextUtils.isEmpty(userProfileDataBean.getPrivateintrooffertype()) && userProfileDataBean.getPrivateintrooffertype().equals(CGlobalVariables.INTRO_OFFER_TYPE_FREE)){
//                                        CUtils.saveAiRandomChatAstroDeatils("");
//                                        CUtils.getAiRandomChatAstroDetail(OtpVerifyActivity.this);
//                                    }

                                    handleAstroSageLogin(jsonObject);
                                    redirectToNextActivity(userProfileDataBean);
                                } else if (status.equals(CGlobalVariables.OTP_EXPIRED)) {
                                    String msg = jsonObject.getString("msg");

                                    if (msg.equalsIgnoreCase(CGlobalVariables.FBA_OTP_EXPIRED)) {
                                        CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_OTP_EXPIRED,
                                                CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                                        CUtils.showSnackbar(containerLayout, getResources().getString(R.string.otp_expired) + " " + errorCode, OtpVerifyActivity.this);
                                    } else {
                                        CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_OTP_INCORRECT,
                                                CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                                        CUtils.showSnackbar(containerLayout, getResources().getString(R.string.otp_is_wrong) + " " + errorCode, OtpVerifyActivity.this);
                                    }
                                } else if (status.equals(CGlobalVariables.USER_OTP_MAX_LIMIT)) {
                                    String msg = jsonObject.getString("msg");
                                    CUtils.showSnackbar(containerLayout, msg + " " + errorCode, OtpVerifyActivity.this);
                                } else {
                                    CUtils.showSnackbar(containerLayout, getResources().getString(R.string.something_wrong_error), OtpVerifyActivity.this);
                                }
                                com.ojassoft.astrosage.varta.utils.CUtils.startToGetAiPassSubService(OtpVerifyActivity.this);
                                CUtils.saveAstroList("");
                            } catch (Exception e) {
                                //Log.d("otpVerify", "onFailure e3: "+e);
                                e.printStackTrace();
                                CUtils.showSnackbar(containerLayout, getResources().getString(R.string.something_wrong_error), OtpVerifyActivity.this);
                            }
                        } catch (Exception e) {
                            //
                        }
                    }
                }catch (Exception e){
                    //Log.d("otpVerify", "onFailure e1: "+e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideProgressBar();
                //Log.d("otpVerify", "onFailure: "+t);
                pd.dismiss();
                CUtils.showSnackbar(containerLayout, getResources().getString(R.string.something_wrong_error), OtpVerifyActivity.this);

            }
        });
    }

    public Map<String, String> getParams(String otpNumber) {

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(OtpVerifyActivity.this));
        headers.put(CGlobalVariables.PHONE_NO, mobileNumber);
        headers.put(CGlobalVariables.OTP_NO, otpNumber);
        headers.put(CGlobalVariables.NEW_USER, newUser);
        headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(OtpVerifyActivity.this));
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
        headers.put(CGlobalVariables.OPERATION_NAME, com.ojassoft.astrosage.utils.CGlobalVariables.OPERATION_NAME_LOGIN);

        int kundliCount = com.ojassoft.astrosage.utils.CUtils.getKundliCount(this);
        String strFirstLoginAfterPlanPurchase = "";
        if (com.ojassoft.astrosage.utils.CUtils.getBooleanData(this, com.ojassoft.astrosage.utils.CGlobalVariables.needToSendDeviceIdForLogin, false)) {
            strFirstLoginAfterPlanPurchase = com.ojassoft.astrosage.utils.CGlobalVariables.FIRST_LOGIN_AFTER_PLAN_PURCHASED;
        }
        if (astroSageUserId == null) astroSageUserId = "";

        headers.put(KEY_USER_ID, astroSageUserId);
        headers.put("firstloginafterplanpurchase", strFirstLoginAfterPlanPurchase);
        headers.put("isverified", "1");
        headers.put("nocharts", kundliCount + "");
        if(newUser.equals("1")){
            String installedReferredCode = CUtils.getStringData(OtpVerifyActivity.this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_REFERRER_URL, "");
            Log.d("InstallRef","installedReferredCode: uniquereferredcode==>>  "+installedReferredCode);
            if(CUtils.isReferralCodeValid(installedReferredCode)){
                headers.put("uniquereferredcode", installedReferredCode);
            }
        }
        //Log.d("LoginFlow", " otp: " + headers);
        return CUtils.setRequiredParams(headers);
    }

    @Override
    public void onResponse(String response, int method) {
//       /* try {
//            response = new String(response.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
//        } catch (Exception e) {
//
//        }*/
//
//        hideProgressBar();
//        //Log.d("LoginFlow", " otp response: " + response);
//        if (response != null && response.length() > 0) {
//
//            if (method == RESEND_OTP_VIA_PHONE_METHOD) {
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    String status = jsonObject.getString("status");
//                    String errorCode = getResources().getString(R.string.error_code).replace("#", status);
//
//                    if (status.equals(CGlobalVariables.OTP_RESEND_SUCCESSFULL_NEW_USER)) {
//                        //CUtils.showSnackbar(containerLayout, getResources().getString(R.string.resend_otp_success_msg), OtpVerifyActivity.this);
//                    } else {
//                        CUtils.showSnackbar(containerLayout, getResources().getString(R.string.resend_otp_failed_msg) + " " + errorCode, OtpVerifyActivity.this);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    CUtils.showSnackbar(containerLayout, getResources().getString(R.string.something_wrong_error), OtpVerifyActivity.this);
//                }
//            } else if (method == RESEND_OTP_METHOD) {
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    String status = jsonObject.getString("status");
//                    String msg = jsonObject.optString("msg");
//                    String errorCode = getResources().getString(R.string.error_code).replace("#", status);
//                    if (status.equals(CGlobalVariables.OTP_RESEND_SUCCESSFULL_EXISTING_USER) || status.equals(CGlobalVariables.OTP_RESEND_SUCCESSFULL_NEW_USER)) {
//                        if(TextUtils.isEmpty(msg)) {
//                            CUtils.showSnackbar(containerLayout, getResources().getString(R.string.resend_otp_success_msg), OtpVerifyActivity.this);
//                        } else {
//                            CUtils.showSnackbar(containerLayout, msg, OtpVerifyActivity.this);
//                        }
//                    } else {
//                        CUtils.showSnackbar(containerLayout, getResources().getString(R.string.something_wrong_error) + " " + errorCode, OtpVerifyActivity.this);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                    CUtils.showSnackbar(containerLayout, getResources().getString(R.string.something_wrong_error), OtpVerifyActivity.this);
//                }
//            } else {
//                try {
//                    CUtils.saveAstrologerIDAndChannelID(OtpVerifyActivity.this,"","");
//                    JSONObject jsonObject = new JSONObject(response);
//                    String status = jsonObject.getString("status");
//                    String errorCode = getResources().getString(R.string.error_code).replace("#", status);
//                    if (status.equals(CGlobalVariables.OTP_VERIFIED_AND_RETURN_USERDATA)) {
//                        CUtils.saveBannerData("");
//                        startPrefatchDataService();
//                        CUtils.startFollowerSubscriptionService(this);
//
//                        com.ojassoft.astrosage.varta.utils.CUtils.unSubscribeTopics("", com.ojassoft.astrosage.varta.utils.CGlobalVariables.TOPIC_NOT_LOGGEDIN, this);
//                        com.ojassoft.astrosage.varta.utils.CUtils.subscribeTopics("", com.ojassoft.astrosage.varta.utils.CGlobalVariables.TOPIC_LOGGEDIN, this);
//
//                        UserProfileData userProfileDataBean = parseJsonObject(response);
//                        CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_SIGNUP_LOGIN_SUCCESS, CGlobalVariables.FIREBASE_EVENT_SIGNUP_LOGIN, "");
//                        //save password in preference
//                        CUtils.setUserLoginPassword(OtpVerifyActivity.this, jsonObject.getString(KEY_PASSWORD));
//                        CUtils.setUserOffers(this, userProfileDataBean.isLiveintrooffer(), userProfileDataBean.getPrivateintrooffertype());
//
//                        String bonusStatus = "0", isShowOneRsPopup = "0", amountOnPopup = "0", freeSessionPopUp = "0";
//                        if (jsonObject.has("eligibleforsignupbonus")) {
//                            bonusStatus = jsonObject.getString("eligibleforsignupbonus");
//                        }
//                        if (jsonObject.has("showonerepopup")) {
//                            isShowOneRsPopup = jsonObject.getString("showonerepopup");
//                            CUtils.setDataForOneRsDialog(OtpVerifyActivity.this, isShowOneRsPopup);
//                        }
//                        if (jsonObject.has("showfreesessionpopup")) {
//                            freeSessionPopUp = jsonObject.getString("showfreesessionpopup");
//                            CUtils.setDataForFreeSessionDialog(OtpVerifyActivity.this, freeSessionPopUp);
//                        }
//                        if (jsonObject.has("amountonpopup")) {
//                            amountOnPopup = jsonObject.getString("amountonpopup");
//                            CUtils.setAmountOnDialog(OtpVerifyActivity.this, amountOnPopup);
//                        }
//
//                        String bonustitle = "", bonusdescription = "";
//                        if (jsonObject.has("bonustitle")) {
//                            bonustitle = jsonObject.getString("bonustitle");
//                        }
//                        if (jsonObject.has("bonusdescription")) {
//                            bonusdescription = jsonObject.getString("bonusdescription");
//                        }
//
//                        if (!TextUtils.isEmpty(bonustitle) && !TextUtils.isEmpty(bonusdescription)) {
//                            boolean isNotificationShown = CUtils.getBooleanData(OtpVerifyActivity.this,CGlobalVariables.IS_49_ADDED_NOTIF_SHOWN,false);
//                            if (!isNotificationShown) {
//                                CUtils.saveBooleanData(OtpVerifyActivity.this, CGlobalVariables.IS_49_ADDED_NOTIF_SHOWN, true);
//                                CreateCustomLocalNotification notification = new CreateCustomLocalNotification(this);
//                                notification.showLocalNotification(bonustitle, bonusdescription, true);
//                            }
//                        }
//
//                        try {
//                            CUtils.setUserIdForBlock(this, jsonObject.getString("userid"));
//                            String blockedby = jsonObject.getString("blockedby");
//                            String[] blockByAstrologer = blockedby.split("\\s*,\\s*");
//                            CUtils.setblockByAstrologerList(blockByAstrologer);
//                        } catch (Exception e) {
//                        }
//
//                        CUtils.saveLoginDetailInPrefs(OtpVerifyActivity.this, mobileNumber, true, userProfileDataBean.getWalletbalance(), bonusStatus);
//
//                        handleAstroSageLogin(jsonObject);
//                        redirectToNextActivity(userProfileDataBean);
//                    } else if (status.equals(CGlobalVariables.OTP_EXPIRED)) {
//                        String msg = jsonObject.getString("msg");
//
//                        if (msg.equalsIgnoreCase(CGlobalVariables.FBA_OTP_EXPIRED)) {
//                            CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_OTP_EXPIRED,
//                                    CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
//                            CUtils.showSnackbar(containerLayout, getResources().getString(R.string.otp_expired) + " " + errorCode, OtpVerifyActivity.this);
//                        } else {
//                            CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_OTP_INCORRECT,
//                                    CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
//                            CUtils.showSnackbar(containerLayout, getResources().getString(R.string.otp_is_wrong) + " " + errorCode, OtpVerifyActivity.this);
//                        }
//                    } else {
//                        CUtils.showSnackbar(containerLayout, getResources().getString(R.string.something_wrong_error), OtpVerifyActivity.this);
//                    }
//                    CUtils.saveAstroList("");
//                } catch (Exception e) {
//                    e.printStackTrace();
//                    CUtils.showSnackbar(containerLayout, getResources().getString(R.string.something_wrong_error), OtpVerifyActivity.this);
//                }
//            }
//        } else {
//            CUtils.showSnackbar(containerLayout, getResources().getString(R.string.server_error), OtpVerifyActivity.this);
//        }
    }

    @Override
    public void onError(VolleyError error) {
        hideProgressBar();
        CUtils.showSnackbar(containerLayout, getResources().getString(R.string.something_wrong_error), OtpVerifyActivity.this);
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

    private UserProfileData parseJsonObject(String responseResult) {
        UserProfileData userProfileDataBean;
        try {
            JSONObject jsonObject = new JSONObject(responseResult);
            Gson gson = new Gson();
            userProfileDataBean = gson.fromJson(jsonObject.toString(), UserProfileData.class);

            CUtils.saveUserSelectedProfileInPreference(OtpVerifyActivity.this, userProfileDataBean);
            CUtils.saveProfileForChatInPreference(OtpVerifyActivity.this, userProfileDataBean);

        } catch (JSONException e) {
            e.printStackTrace();
            userProfileDataBean = null;
        }

        return userProfileDataBean;
    }

    private void resendOtp(String otpUrl, int method) {
        if (!CUtils.isConnectedWithInternet(OtpVerifyActivity.this)) {
            CUtils.showSnackbar(containerLayout, getResources().getString(R.string.no_internet), OtpVerifyActivity.this);
        } else {
            if (pd == null)
                pd = new CustomProgressDialog(OtpVerifyActivity.this);
            pd.show();
            pd.setCancelable(false);

            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, otpUrl,
                    OtpVerifyActivity.this, false, getParamsForResend(), method).getMyStringRequest();
            stringRequest.setShouldCache(false);
            queue.add(stringRequest);
        }
    }

    private void callLoginReSendOtpApi() {
        if (!CUtils.isConnectedWithInternet(OtpVerifyActivity.this)) {
            CUtils.showSnackbar(containerLayout, getResources().getString(R.string.no_internet), OtpVerifyActivity.this);
        } else {
            if (pd == null)
                pd = new CustomProgressDialog(OtpVerifyActivity.this);
            pd.show();
            pd.setCancelable(false);

            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.reSendOtp(getParamsForResend());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    try {
                        if (response.body() != null) {
                            try {
                                String myResponse = response.body().string();
                                Log.d("testRetrofitApi", "reSendOtp myResponse" + myResponse);
                                JSONObject jsonObject = new JSONObject(myResponse);
                                hideProgressBar();
                                String status = jsonObject.getString("status");
                                String msg = jsonObject.optString("msg");
                                String errorCode = getResources().getString(R.string.error_code).replace("#", status);
                                if (status.equals(CGlobalVariables.OTP_RESEND_SUCCESSFULL_EXISTING_USER) || status.equals(CGlobalVariables.OTP_RESEND_SUCCESSFULL_NEW_USER)) {
                                    if (TextUtils.isEmpty(msg)) {
                                        CUtils.showSnackbar(containerLayout, getResources().getString(R.string.resend_otp_success_msg), OtpVerifyActivity.this);
                                    } else {
                                        CUtils.showSnackbar(containerLayout, msg, OtpVerifyActivity.this);
                                    }
                                } else {
                                    CUtils.showSnackbar(containerLayout, getResources().getString(R.string.something_wrong_error) + " " + errorCode, OtpVerifyActivity.this);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                CUtils.showSnackbar(containerLayout, getResources().getString(R.string.something_wrong_error), OtpVerifyActivity.this);
                            }
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

    private void callLoginReSendViaCallOtpApi() {
        if (!CUtils.isConnectedWithInternet(OtpVerifyActivity.this)) {
            CUtils.showSnackbar(containerLayout, getResources().getString(R.string.no_internet), OtpVerifyActivity.this);
        } else {
            if (pd == null)
                pd = new CustomProgressDialog(OtpVerifyActivity.this);
            pd.show();
            pd.setCancelable(false);
            Context context = OtpVerifyActivity.this;
            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.reSendViaCallOtp(getParamsForResend());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    try {
                        if (response.body() != null) {
                            try {
                                String myResponse = response.body().string();
                                Log.d("testRetrofitApi", "reSendViaCallOtp myResponse" + myResponse);
                                JSONObject jsonObject = new JSONObject(myResponse);
                                hideProgressBar();
                                String status = jsonObject.getString("status");
                                String errorCode = getResources().getString(R.string.error_code).replace("#", status);

                                if (status.equals(CGlobalVariables.OTP_RESEND_SUCCESSFULL_NEW_USER)) {
                                    //CUtils.showSnackbar(containerLayout, getResources().getString(R.string.resend_otp_success_msg), OtpVerifyActivity.this);
                                } else {
                                    CUtils.showSnackbar(containerLayout, getResources().getString(R.string.resend_otp_failed_msg) + " " + errorCode, OtpVerifyActivity.this);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                                CUtils.showSnackbar(containerLayout, getResources().getString(R.string.something_wrong_error), OtpVerifyActivity.this);
                            }
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

    public Map<String, String> getParamsForResend() {

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(OtpVerifyActivity.this));
        headers.put(CGlobalVariables.PHONE_NO, mobileNumber);
        headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(OtpVerifyActivity.this));
        headers.put(CGlobalVariables.FROM_RESEND, "" + resendOtpCount);
        headers.put(CGlobalVariables.REG_SOURCE, CGlobalVariables.APP_SOURCE);
        headers.put(CGlobalVariables.APP_VERSION, "" + BuildConfig.VERSION_NAME);
        headers.put(CGlobalVariables.DEVICE_ID, CUtils.getMyAndroidId(OtpVerifyActivity.this));
        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(OtpVerifyActivity.this));
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
        headers.put(CGlobalVariables.OPERATION_NAME, com.ojassoft.astrosage.utils.CGlobalVariables.OPERATION_NAME_SIGNUP);
        //Log.d("LoginFlow", " otp headers: " + headers);
        return headers;
    }


    @Override
    public void cancelHandler(boolean isCancel) {
        handlerButtonVisibility.removeCallbacks(rButtonVisibility);
        countDownTimer.cancel();
        timerForUnregisteringBroadcastReciever.cancel();
    }

    public void functionEnablingButtons() {

        try{
            resendOtp.setOnClickListener(OtpVerifyActivity.this);
            resendOtp.setClickable(true);
            resendOtp.setTextColor(ContextCompat.getColor(this, R.color.black));

            //resendOtp.setAlpha(1);
            resendOtp.setVisibility(View.VISIBLE);
            getOtpViaPhone.setOnClickListener(OtpVerifyActivity.this);
            getOtpViaPhone.setClickable(true);
            getOtpViaPhone.setTextColor(ContextCompat.getColor(this, R.color.black));

            //getOtpViaPhone.setAlpha(1);
            // Add underline
            getOtpViaPhone.setPaintFlags(getOtpViaPhone.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);

        }catch (Exception e){}

    }

    public void functionInitializingCountDownTimer() {
        //textViewTimer.setVisibility(View.VISIBLE);
        txtViewResendOtp.setVisibility(View.VISIBLE);
        resendOtp.setVisibility(View.GONE);

        countDownTimer = new CountDownTimer(longTotalVerificationTime, longOneSecond) {
            @SuppressLint("StringFormatInvalid")
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onTick(long millisUntilFinished) {
                //textViewTimer.setVisibility(View.VISIBLE);

                String text = String.format(Locale.US, FORMAT,
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(
                                TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(
                                TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished)));
                textViewTimer.setText(text.trim());
                //txtViewResendOtp.setText(getString(R.string.resend_otp_in,text.toString() ).trim());
                String otp = text.toString().trim(); // The value to be black
                String formattedText = getString(R.string.resend_otp_in, otp); // "Resend OTP in 30s"

                // Find start and end of the black part (the %s value)
                int startBlack = formattedText.indexOf(otp);
                int endBlack = startBlack + otp.length();

                // Create SpannableString
                SpannableString spannable = new SpannableString(formattedText);

                // Colors from resources
                int greyColor = ContextCompat.getColor(OtpVerifyActivity.this, R.color.new_theme_darker_gray); // for "Resend OTP in"
                int blackColor = ContextCompat.getColor(OtpVerifyActivity.this, R.color.black); // for otp

                // Set black color for OTP part
                spannable.setSpan(
                        new ForegroundColorSpan(blackColor),
                        startBlack,
                        endBlack,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                );

               // Set grey color for the rest
                spannable.setSpan(
                        new ForegroundColorSpan(greyColor),
                        0,
                        startBlack,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                );

                // Set to TextView
                txtViewResendOtp.setText(spannable);
            }

            @Override
            public void onFinish() {
                functionEnablingButtons();
                textViewTimer.setVisibility(View.GONE);
                txtViewResendOtp.setVisibility(View.GONE);
                resendOtp.setVisibility(View.VISIBLE);
                otpEdt1.setFocusable(true);
                otpEdt1.requestFocus();
                otpEdt1.setFocusableInTouchMode(true);
                otpEdt1.invalidate();
            }
        }.start();
    }

    public void functionForUnregisteringBroadcastReceiever() {
        istimerForUnregisteringBroadcastRecieverrunning = true;
        timerForUnregisteringBroadcastReciever = new CountDownTimer(timeafterwhichRecieverUnregister, 120000) {

            @Override
            public void onTick(long millisUntilFinished) {
            }

            @Override
            public void onFinish() {
                if (recieverRegistered) {
                    unregisterReceiver(smsReciever);
                    recieverRegistered = false;
                }
            }
        };
    }

    public void autoFillOtp(String otp) {
        try {
            otpEdt1.setText(String.valueOf(otp.charAt(0)));
            otpEdt2.setText(String.valueOf(otp.charAt(1)));
            otpEdt3.setText(String.valueOf(otp.charAt(2)));
            otpEdt4.setText(String.valueOf(otp.charAt(3)));
            verifyBtn.performClick();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        if (smsReciever != null) {
            if (recieverRegistered == true) {
                unregisterReceiver(smsReciever);
                recieverRegistered = false;
            }
        }
        super.onDestroy();
    }

    private String getCopiedOtpFromClipboard() {
        try {
            ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            if (clipboard != null && clipboard.hasPrimaryClip()) {
                ClipData.Item item = clipboard.getPrimaryClip().getItemAt(0);
                String pasteOtp = item.getText().toString();
                if (!TextUtils.isEmpty(pasteOtp) && pasteOtp.length() == 4) {
                    return pasteOtp;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    private void startPrefatchDataService() {
        try {
            AstrosageKundliApplication.isPrefetchServiceRunning = true;
            Intent intentService = new Intent(OtpVerifyActivity.this, PreFetchDataservice.class);
            startService(intentService);
        } catch (Exception e) {
        }
    }

    private void handleAstroSageLogin(JSONObject jsonObject) {
        try {
            //Log.d("LoginFlow", " handleAstroSageLogin()1");
            String astroSageUserId = com.ojassoft.astrosage.utils.CUtils.getUserName(this);
            if (com.ojassoft.astrosage.utils.CUtils.isUserLogedIn(this) || !TextUtils.isEmpty(astroSageUserId)) {
                return; //if already loggedin with astrosage-id then return
            }
            JSONArray jsonArray = jsonObject.getJSONArray("as");
            JSONObject respObj = jsonArray.getJSONObject(0);

            String respCode = respObj.getString("msgcode");
            if (respCode.equals("22")) {               //if plan activated successfully
                com.ojassoft.astrosage.utils.CUtils.saveBooleanData(activity, com.ojassoft.astrosage.utils.CGlobalVariables.needToSendDeviceIdForLogin, false);
                CUtils.showSnackbar(containerLayout, getResources().getString(R.string.plan_activate_success), OtpVerifyActivity.this);
            } else if (respCode.equals("3")) {
                com.ojassoft.astrosage.utils.CUtils.saveBooleanData(activity, com.ojassoft.astrosage.utils.CGlobalVariables.needToSendDeviceIdForLogin, false);
                CUtils.showSnackbar(containerLayout, getResources().getString(R.string.user_exist_record_not_found), OtpVerifyActivity.this);
            } else {
                CUtils.showSnackbar(containerLayout, getResources().getString(R.string.sign_in_success), OtpVerifyActivity.this);
            }

            HashMap<String, String> jsonObjHash = com.ojassoft.astrosage.utils.CUtils.parseLoginSignupJson(respObj);
            if (jsonObjHash.size() > 0) {
                if (jsonObjHash.containsKey(com.ojassoft.astrosage.utils.CGlobalVariables.USERID)) {
                    _userId = jsonObjHash.get(com.ojassoft.astrosage.utils.CGlobalVariables.USERID);
                }
                if (jsonObjHash.containsKey(com.ojassoft.astrosage.utils.CGlobalVariables.USER_PASSWORD)) {
                    _pwd = jsonObjHash.get(com.ojassoft.astrosage.utils.CGlobalVariables.USER_PASSWORD);
                }
                CUtils.saveInformation(activity, _userId, _pwd, jsonObjHash);
            }
            //Log.d("LoginFlow", " handleAstroSageLogin()2");
        } catch (Exception e) {
            //
        }
    }

    private void redirectToNextActivity(UserProfileData userProfileDataBean) {
        try {

            if (isFromScreen == null) isFromScreen = "";
            if (isFromScreen.equals(CGlobalVariables.PAYMENT_INFO_SCREEN)) {
                setResult(RESULT_CANCELED);
            } else if (isFromScreen.equals(CGlobalVariables.LANGUAGE_SELECTION_SCRREN)) {
                com.ojassoft.astrosage.utils.CUtils.saveWizardShownFirstTime(OtpVerifyActivity.this);
                startActivity(new Intent(OtpVerifyActivity.this, ActAppModule.class));
            } else if (isFromScreen.equals(CGlobalVariables.BASE_INPUT_SCREEN) ||
                    isFromScreen.equals(CGlobalVariables.LOGOUT_BTN)) {
                Intent intent = new Intent();
                Bundle bundle=new Bundle();
                bundle.putString("LOGIN_NAME",_userId);
                bundle.putString("LOGIN_PWD",_pwd);
                intent.putExtras(bundle);
                setResult(RESULT_OK,intent);
            } else if(isFromScreen.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.RECHARGE_SCRREN)){
                Intent walletIntent = new Intent(OtpVerifyActivity.this, WalletActivity.class);
                startActivity(walletIntent);
            } else {
                Intent i = new Intent(OtpVerifyActivity.this, DashBoardActivity.class);
                i.putExtra(CGlobalVariables.USER_DATA, userProfileDataBean);
                i.putExtra(CGlobalVariables.IS_FROM_SCREEN, isFromScreen);
                i.putExtra(KEY_FILTER_TYPE, CGlobalVariables.FILTER_TYPE_CALL);
                startActivity(i);
            }
            finish();
            if (LoginSignUpActivity.loginSignupAct != null) {
                LoginSignUpActivity.loginSignupAct.finish();
            }
        } catch (Exception e) {
            //
        }
    }

    private class GenericTextWatcher implements TextWatcher {
        private final EditText currentView;
        private final EditText nextView;

        private GenericTextWatcher(EditText currentView, EditText nextView) {
            this.currentView = currentView;
            this.nextView = nextView;
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // TODO Auto-generated method stub
            String text = editable.toString();
            switch (currentView.getId ()) {
                case (R.id.otp_edt1) :

                case (R.id.otp_edt3) :

                case (R.id.otp_edt2) :
                    if (text.length () == 1) nextView.requestFocus();
                    break;

                case (R.id.otp_edt4) :
                    if (text.length () == 1) checkForAutoClick();
                    break;

                //You can use EditText4 same as above to hide the keyboard
            }

        }

        @Override
        public void beforeTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onTextChanged(CharSequence arg0, int arg1, int arg2, int arg3) {
            // TODO Auto-generated method stub
        }
    }

    private  class GenericKeyEvent implements View.OnKeyListener {

        private final EditText currentView;
        private final EditText previousView;

        public GenericKeyEvent(EditText currentView, EditText previousView) {
            this.currentView = currentView;
            this.previousView = previousView;
        }

        @Override
        public boolean onKey(View v, int keyCode, KeyEvent event) {
            if(event.getAction () == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_DEL && currentView.getId () != R.id.otp_edt1 && currentView.getText ().toString ().isEmpty())
            {
                //If current is empty then previous EditText's number will also be deleted
                previousView.setText (null);
                previousView.requestFocus();
                return true;
            }
            return false;
        }
    }

    private void checkForAutoClick(){

        String otpNumberStr = otpEdt1.getText().toString().trim() + otpEdt2.getText().toString().trim() +
                otpEdt3.getText().toString().trim() + otpEdt4.getText().toString().trim();

        if (isValidData(otpNumberStr)){
            verifyBtn.performClick();
        }

    }
    private static final int SMS_CONSENT_REQUEST = 2;
    private final BroadcastReceiver smsVerificationReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (SmsRetriever.SMS_RETRIEVED_ACTION.equals(intent.getAction())) {
                Bundle extras = intent.getExtras();
                if(extras != null) {
                    Status smsRetrieverStatus = (Status) extras.get(SmsRetriever.EXTRA_STATUS);
                    if (smsRetrieverStatus == null) return;
                    if (smsRetrieverStatus.getStatusCode() == CommonStatusCodes.SUCCESS) {
                        try {
                            Intent consentIntent = extras.getParcelable(SmsRetriever.EXTRA_CONSENT_INTENT);

                            startActivityForResult(consentIntent, SMS_CONSENT_REQUEST);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }  // Time out occurred, handle the error.

                }
            }
        }
    };
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SMS_CONSENT_REQUEST) {
            if (resultCode == RESULT_OK) {

                String message = data.getStringExtra(SmsRetriever.EXTRA_SMS_MESSAGE);
                if(message != null){
                    int startIndex = message.indexOf("#") + 1;
                    String otp = message.substring(startIndex,startIndex+4);
                    autoFillOtp(otp);
                }
//                Log.d("OTP message ", message);
                // send one time code to the server
            }
        }
    }

}