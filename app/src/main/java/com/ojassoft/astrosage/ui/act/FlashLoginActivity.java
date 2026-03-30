package com.ojassoft.astrosage.ui.act;

import static com.ojassoft.astrosage.ui.act.BaseInputActivity.SUB_ACTIVITY_USER_LOGIN;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_PASSWORD;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_USER_ID;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.AUTO_OPEN_LOGIN;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.credentials.CredentialManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Cache;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.google.android.gms.auth.api.identity.GetPhoneNumberHintIntentRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.gson.Gson;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.notification.ActShowOjasSoftArticles;
import com.ojassoft.astrosage.varta.adapters.CountryListAdapter;
import com.ojassoft.astrosage.varta.interfacefile.RecyclerClickListner;
import com.ojassoft.astrosage.varta.model.CountryBean;
import com.ojassoft.astrosage.varta.model.UserProfileData;
import com.ojassoft.astrosage.varta.service.PreFetchDataservice;
import com.ojassoft.astrosage.varta.ui.activity.BaseActivity;

import com.ojassoft.astrosage.varta.ui.activity.LoginSignUpActivity;
import com.ojassoft.astrosage.varta.ui.activity.OtpVerifyActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.CreateCustomLocalNotification;
import com.ojassoft.astrosage.varta.utils.CustomProgressDialog;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;
import com.truecaller.android.sdk.oAuth.CodeVerifierUtil;
import com.truecaller.android.sdk.oAuth.TcOAuthCallback;
import com.truecaller.android.sdk.oAuth.TcOAuthData;
import com.truecaller.android.sdk.oAuth.TcOAuthError;
import com.truecaller.android.sdk.oAuth.TcSdk;
import com.truecaller.android.sdk.oAuth.TcSdkOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FlashLoginActivity extends BaseActivity implements View.OnClickListener, VolleyResponse {

    private static final String TAG = FlashLoginActivity.class.getSimpleName();
    private static final int RC_HINT_EMAIL = 2;
    ImageView ivback;
    EditText mobileNumberTxt;
    Button getOtpBtn;
    TextView signUpTxt, loginHeadingTxt, dontHaveAccountTxt, agreeText, sendOtpMsgTV;
    RequestQueue queue;
    CustomProgressDialog pd;
    ScrollView containerLayout;
    String mobileNumber = "";
    TextView skipCloseButton;
    TextView language_button;
    String isFromScreen;
    boolean autoOpenLogin;
    private CredentialManager credentialManager;
    private ActivityResultLauncher<IntentSenderRequest> phoneNumberHintLauncher;
    private boolean isGoogleApiClientConnected;
    private boolean isEmailDialogueOpened;
    private String selectedMobNum;
    private CheckBox agreeCheckBox;
    public static Activity flashLoginSignupAct;
    Spinner tvCountryCodeTxt;
    ArrayList<CountryBean> countryBeanList = null;
    boolean isShowProgressBar = false;
    ImageView dropdown_img;
    CountryListAdapter adapter;
    RelativeLayout country_code_layout;
    TextView countryCodeText;
    boolean isClickGetOtpBtn = false;
    TextView orWithTxt;
    LinearLayout truecaller_button;
    Button astrosageIdBtn;
    TextView astrosageText;
    LinearLayout dontHaveAccountLL;
    private String astroSageUserId = "";
    private String _userId = "", _pwd = "";

    long timeDifference = 0;
    String codeVerifier;
    boolean isTrueCallerUsable;

    TcSdkOptions tcSdkOptions;
    GetPhoneNumberHintIntentRequest request;
    ActivityResultLauncher<IntentSenderRequest> phoneNumberHintIntentResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        LANGUAGE_CODE = AstrosageKundliApplication.getAppContext().getLanguageCode();
        CUtils.getRobotoFont(FlashLoginActivity.this, LANGUAGE_CODE, CGlobalVariables.regular);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setContentView(R.layout.activity_flash_login);
        this.setFinishOnTouchOutside(false);
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        //wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        initView();
        //trueCallerInitialize();
        ImageView dismissDialog = findViewById(R.id.dismissDialog);
        dismissDialog.setOnClickListener(view1 -> {
            finish();
        });
        flashLoginSignupAct = this;
        setupRequestPhoneNumber();
    }

    private void trueCallerInitialize() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //Background thread
                    TcSdk.clear();
                    tcSdkOptions =
                            new TcSdkOptions.Builder(FlashLoginActivity.this, new TcOAuthCallback() {
                                @Override
                                public void onSuccess(@NonNull TcOAuthData tcOAuthData) {
                                    sendRequestToTrueCaller(tcOAuthData);
                                }

                                @Override
                                public void onFailure(@NonNull TcOAuthError tcOAuthError) {
                                    TcSdk.clear();
                                }

                                @Override
                                public void onVerificationRequired(@Nullable TcOAuthError tcOAuthError) {

                                }
                            })
                                    .sdkOptions(TcSdkOptions.OPTION_VERIFY_ONLY_TC_USERS)
                                    .buttonColor(Color.parseColor("#e65100"))
                                    .buttonTextColor(Color.parseColor("#FFFFFF"))
                                    .loginTextPrefix(TcSdkOptions.LOGIN_TEXT_PREFIX_TO_GET_STARTED)
                                    .ctaText(TcSdkOptions.CTA_TEXT_CONTINUE).
                                    buttonShapeOptions(TcSdkOptions.BUTTON_SHAPE_ROUNDED).
                                    footerType(TcSdkOptions.FOOTER_TYPE_ANOTHER_METHOD).
                                    consentTitleOption(TcSdkOptions.SDK_CONSENT_HEADING_LOG_IN_TO).
                                    build();
                    TcSdk.init(tcSdkOptions);
                    isTrueCallerUsable = TcSdk.getInstance().isOAuthFlowUsable();
                    if (isTrueCallerUsable) {
                        Locale locale = new Locale(CUtils.getLanguageKey(LANGUAGE_CODE));
                        TcSdk.getInstance().setLocale(locale);
                        String stateRequested = (new BigInteger(130, new SecureRandom())).toString(32);
                        TcSdk.getInstance().setOAuthState(stateRequested);
                        TcSdk.getInstance().setOAuthScopes(new String[]{"profile", "phone"});

                        codeVerifier = CodeVerifierUtil.Companion.generateRandomCodeVerifier();
                        String codeChallenge = CodeVerifierUtil.Companion.getCodeChallenge(codeVerifier);
                        if (codeChallenge != null) {
                            TcSdk.getInstance().setCodeChallenge(codeChallenge);
                        } else {
                            String message = "Code challenge is Null. Can't proceed further";
                            System.out.print(message);
                        }
                        TcSdk.getInstance().getAuthorizationCode(FlashLoginActivity.this);
                        //boolean isTrueCallerInstalled = CUtils.isAppInstalled(LoginSignUpActivity.this, "com.truecaller");
                        //TcSdk.clear();
                    }
                    // UI process
                    handler.post(() -> {
                        if (isTrueCallerUsable) {
                            truecaller_button.setVisibility(View.VISIBLE);
                        } else {
                            truecaller_button.setVisibility(View.GONE);
                        }

                        if (com.ojassoft.astrosage.utils.CUtils.isUserLogedIn(FlashLoginActivity.this) && !isTrueCallerUsable) {
                            orWithTxt.setVisibility(View.GONE);
                        }
                    });
                } catch (Exception e) {
                    //
                }
            }
        });
    }

    public void gotoSelectedScreen(View textView) {
        Intent intent = new Intent(this, ActShowOjasSoftArticles.class);
        switch (textView.getId()) {

            case R.id.textViewPrivacyPolicy:
                intent.putExtra(com.ojassoft.astrosage.utils.CGlobalVariables.ASTRO_WEBVIEW_TITLE_KEY, com.ojassoft.astrosage.utils.CGlobalVariables.MODULE_ABOUT_US);
                intent.putExtra("URL", com.ojassoft.astrosage.utils.CGlobalVariables.ASTROSAGE_PRIVACY_POLICY_URL);
                intent.putExtra("TITLE_TO_SHOW", "Privacy Policy");
                break;
            case R.id.textViewTermsAndConditions:
                intent.putExtra(com.ojassoft.astrosage.utils.CGlobalVariables.ASTRO_WEBVIEW_TITLE_KEY, com.ojassoft.astrosage.utils.CGlobalVariables.MODULE_ABOUT_US);
                intent.putExtra("URL", com.ojassoft.astrosage.utils.CGlobalVariables.ASTROSAGE_TERMS_CONDITIONS_URL);
                intent.putExtra("TITLE_TO_SHOW", "Terms and Conditions");
                break;
        }
        startActivity(intent);
    }

    public String getCountry(String[] argStringArray, String argText) {
        String country = "";
        for (int i = 0; i < argStringArray.length; i++) {
            String[] g = argStringArray[i].split(",");
            if (g[1].equals(getLastTwo(argText))) {
                country = g[0];
                break;
            }
        }
        return country;
    }

    public String getLastTwo(String argText) {
        String threeChar;
        threeChar = argText.substring(argText.length() - 2);
        return threeChar;
    }

    private void initView() {
        ivback = findViewById(R.id.ivback);
        countryCodeText = findViewById(R.id.country_code);
        country_code_layout = findViewById(R.id.country_code_layout);
        dropdown_img = findViewById(R.id.dropdown_img);
        mobileNumberTxt = findViewById(R.id.mobile_number_txt);
        getOtpBtn = findViewById(R.id.get_otp_btn);
        signUpTxt = findViewById(R.id.sign_up_txt);
        signUpTxt.setPaintFlags(signUpTxt.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        loginHeadingTxt = findViewById(R.id.login_heading_txt);
        dontHaveAccountTxt = findViewById(R.id.dont_have_account_txt);
        containerLayout = findViewById(R.id.container_layout);
        skipCloseButton = findViewById(R.id.skip_close_button);
        language_button = findViewById(R.id.language_button);
        agreeText = findViewById(R.id.agree_text);
        sendOtpMsgTV = findViewById(R.id.sendOtpMsgTV);
        agreeCheckBox = findViewById(R.id.agreeCheckBox);
        tvCountryCodeTxt = findViewById(R.id.tv_country_code_txt);
        orWithTxt = findViewById(R.id.orWithTxt);
        astrosageIdBtn = findViewById(R.id.astrosage_id_btn);
        astrosageText = findViewById(R.id.astrosage_txt);
        truecaller_button = findViewById(R.id.truecaller_button);
        dontHaveAccountLL = findViewById(R.id.dont_have_account_ll);

        FontUtils.changeFont(FlashLoginActivity.this, astrosageText, CGlobalVariables.FONTS_DIAVLO_BOLD);
        FontUtils.changeFont(FlashLoginActivity.this, astrosageIdBtn, CGlobalVariables.FONTS_DIAVLO_BOLD);

        FontUtils.changeFont(FlashLoginActivity.this, countryCodeText, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(FlashLoginActivity.this, loginHeadingTxt, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(FlashLoginActivity.this, getOtpBtn, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(FlashLoginActivity.this, mobileNumberTxt, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(FlashLoginActivity.this, dontHaveAccountTxt, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        //FontUtils.changeFont(FlashLoginActivity.this, signUpTxt, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(FlashLoginActivity.this, skipCloseButton, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(FlashLoginActivity.this, language_button, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(FlashLoginActivity.this, agreeText, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(FlashLoginActivity.this, sendOtpMsgTV, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(FlashLoginActivity.this, orWithTxt, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(FlashLoginActivity.this, (TextView) findViewById(R.id.textViewPrivacyPolicy), CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(FlashLoginActivity.this, (TextView) findViewById(R.id.textViewTermsAndConditions), CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);

        CUtils.startGcmService(FlashLoginActivity.this);
        initListners();
        queue = VolleySingleton.getInstance(FlashLoginActivity.this).getRequestQueue();
        checkCacheCountryListData();

        if (getIntent().getExtras() != null) {
            autoOpenLogin = getIntent().getBooleanExtra(AUTO_OPEN_LOGIN, false);
            isFromScreen = getIntent().getExtras().getString(CGlobalVariables.IS_FROM_SCREEN);
            if (isFromScreen != null && (isFromScreen.equalsIgnoreCase(CGlobalVariables.PROFILE_SCRREN) ||
                    isFromScreen.equalsIgnoreCase(CGlobalVariables.RECHARGE_SCRREN) ||
                    isFromScreen.equalsIgnoreCase(CGlobalVariables.DASHBOARD_CALL_NOW_SCRREN) ||
                    isFromScreen.equalsIgnoreCase(CGlobalVariables.ASTROLOGER_DETAIL_CALL_NOW_SCREEN))) {
                loginHeadingTxt.setText(getResources().getString(R.string.sign_up));
                mobileNumberTxt.setText("");
                signUpTxt.setText(getResources().getString(R.string.login));
                dontHaveAccountTxt.setText(getResources().getString(R.string.already_have_account));
                skipCloseButton.setVisibility(View.GONE);
            } else {
                loginHeadingTxt.setText(getResources().getString(R.string.login));
                mobileNumberTxt.setText("");
                signUpTxt.setText(getResources().getString(R.string.text_sign_up));
                dontHaveAccountTxt.setText(getResources().getString(R.string.dont_have_account));
                skipCloseButton.setVisibility(View.VISIBLE);
            }
        }

        if (countryCodeText.getText().toString().contains("+91")) {
            mobileNumberTxt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        }


        if (com.ojassoft.astrosage.utils.CUtils.isUserLogedIn(FlashLoginActivity.this)) {
            astrosageIdBtn.setVisibility(View.INVISIBLE);
            if (truecaller_button.getVisibility() != View.VISIBLE) {
                orWithTxt.setVisibility(View.GONE);
            }
            dontHaveAccountLL.setVisibility(View.GONE);
            loginHeadingTxt.setText(getResources().getString(R.string.text_enter_phone));
        } else {
            //** done on Flash
            astrosageIdBtn.setVisibility(View.GONE);
            orWithTxt.setVisibility(View.VISIBLE);
            dontHaveAccountLL.setVisibility(View.GONE);
        }

        SpannableString textSpan = new SpannableString(getString(R.string.terms_and_con_text));

        String termsText = getString(R.string.terms_and_con_text);
        String terms = getString(R.string.terms);
        String privacy = getString(R.string.privacy_policy);
        int termsStartIndex = termsText.indexOf(terms);
        int privacyStartIndex = termsText.indexOf(privacy);
        if (termsStartIndex != -1 && privacyStartIndex != -1) {
            ClickableSpan clickableSpan = new ClickableSpan() {
                @Override
                public void onClick(@NonNull View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(com.ojassoft.astrosage.utils.CGlobalVariables.PRIVACY_POLICY_URL));
                    startActivity(intent);
                }
            };

            textSpan.setSpan(new ClickableSpan() {
                @Override
                public void onClick(@NonNull View view) {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(com.ojassoft.astrosage.utils.CGlobalVariables.TERMS_AND_CONDITION_URL));
                    startActivity(intent);
                }
            }, termsStartIndex, termsStartIndex + terms.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            textSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#0070E0")), termsStartIndex, termsStartIndex + terms.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            textSpan.setSpan(clickableSpan, privacyStartIndex, privacyStartIndex + privacy.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
            textSpan.setSpan(new ForegroundColorSpan(Color.parseColor("#0070E0")), privacyStartIndex, privacyStartIndex + privacy.length(), Spannable.SPAN_EXCLUSIVE_INCLUSIVE);

            agreeText.setMovementMethod(LinkMovementMethod.getInstance());
            agreeText.setText(textSpan);
        }

        getPreferencesCountryCode();

    }

    private void getPreferencesCountryCode() {
        // Loop through the list using a for-each loop
        for (CountryBean countryBean : countryBeanList) {
            String countryCode = countryBean.getCountryCode();

            if (countryCode.equals(CUtils.getCountryCode(this))) {
                countryCodeText.setText(countryBean.getCountryName() + " (+" + countryCode + ")");
            }
        }
    }

    private void initListners() {
        getOtpBtn.setOnClickListener(this);
        astrosageIdBtn.setOnClickListener(this);
        signUpTxt.setOnClickListener(this);
        skipCloseButton.setOnClickListener(this);
        language_button.setOnClickListener(this);
        agreeCheckBox.setOnClickListener(this);
        country_code_layout.setOnClickListener(this);

        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        mobileNumberTxt.setFocusable(false);
        mobileNumberTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestCredentials();
            }
        });

        mobileNumberTxt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                isClickGetOtpBtn = false;
                if (s.toString().length() == 1 && s.toString().startsWith("0")) {
                    mobileNumberTxt.setText("");
                }
            }
        });

        truecaller_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String labell = "truecaller_login_signup";
                    CUtils.fcmAnalyticsEvents(labell, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                    trueCallerInitialize();
                } catch (Exception e) {
                    //
                }
            }
        });
    }

    private void setFocusOnEmail() {
        mobileNumberTxt.setFocusableInTouchMode(true);
        mobileNumberTxt.setFocusable(true);
        mobileNumberTxt.requestFocus();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.country_code_layout:
                isClickGetOtpBtn = false;
                showDialog();
                break;

            case R.id.get_otp_btn:
                mobileNumber = mobileNumberTxt.getText().toString().trim();
                if (!isClickGetOtpBtn) {
                    if (isValidData(mobileNumber)) {

                        CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_GET_OTP_BTN, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                        if (!CUtils.checkReceiveSmsPermission(this)) {
                            CUtils.requestReceiveSmsPermission(this);
                            isClickGetOtpBtn = false;
                        } else {
                            isClickGetOtpBtn = true;
                            sendOtpToMobile(mobileNumber);
                        }
                    } else {
                        isClickGetOtpBtn = false;
                        CUtils.showSnackbar(containerLayout, getResources().getString(R.string.enter_valid_mobile_no), FlashLoginActivity.this);
                    }
                }
                break;

            case R.id.sign_up_txt:
                isClickGetOtpBtn = false;
                if (loginHeadingTxt.getText().toString().equalsIgnoreCase(getResources().getString(R.string.login))) {
                    loginHeadingTxt.setText(getResources().getString(R.string.sign_up));
                    mobileNumberTxt.setText("");
                    signUpTxt.setText(getResources().getString(R.string.login));
                    dontHaveAccountTxt.setText(getResources().getString(R.string.already_have_account));
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_LOGIN_TXT,
                            CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                } else {
                    loginHeadingTxt.setText(getResources().getString(R.string.login));
                    mobileNumberTxt.setText("");
                    signUpTxt.setText(getResources().getString(R.string.text_sign_up));
                    dontHaveAccountTxt.setText(getResources().getString(R.string.dont_have_account));
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_SIGNUP_TXT,
                            CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                }
                break;

            case R.id.skip_close_button:
                isClickGetOtpBtn = false;
                AstrosageKundliApplication.connectAiChatAfterLogin = false;
                AstrosageKundliApplication.connectHumanChatAfterLogin = false;
                AstrosageKundliApplication.connectAiCallAfterLogin = false;
                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_SKIP, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                CUtils.saveBooleanData(FlashLoginActivity.this, CGlobalVariables.PREF_FIRST_INSTALL_APP_KEY, true);
                redirectToNextActivity();
                break;

            case R.id.language_button:
                break;

            case R.id.agreeCheckBox: {
                isClickGetOtpBtn = false;
                if (agreeCheckBox.isChecked()) {
                    getOtpBtn.setEnabled(true);
                    getOtpBtn.setAlpha(1);
                    truecaller_button.setEnabled(true);
                    truecaller_button.setAlpha(1);
                } else {
                    getOtpBtn.setEnabled(false);
                    getOtpBtn.setAlpha(0.6f);
                    truecaller_button.setEnabled(false);
                    truecaller_button.setAlpha(0.6f);
                }
                break;
            }
            case R.id.astrosage_id_btn: {
                Intent intent = new Intent(FlashLoginActivity.this, ActLogin.class);
                startActivityForResult(intent, SUB_ACTIVITY_USER_LOGIN);
                break;
            }
        }
    }

    private void redirectToNextActivity() {
        try {
            finish();
        } catch (Exception e) {
            //
        }
    }

    private boolean isValidData(String mobileNo) {
        boolean isValid = mobileNo != null || mobileNo.trim().length() != 0;

        if (CUtils.getCountryCode(FlashLoginActivity.this).equals("91")) {
            if (mobileNo.trim().length() < 10) {
                isValid = false;
            }
        }
        return isValid;
    }

    private void sendOtpToMobile(String mobNo) {
        timeDifference = 0;
        timeDifference = System.currentTimeMillis();
        //Toast.makeText(loginSignupAct, "sendOtpToMobile="+timeDifference, Toast.LENGTH_SHORT).show();
        CUtils.hideMyKeyboard(this);
        if (!CUtils.isConnectedWithInternet(FlashLoginActivity.this)) {
            isClickGetOtpBtn = false;
            CUtils.showSnackbar(containerLayout, getResources().getString(R.string.no_internet), FlashLoginActivity.this);
        } else {
            if (pd == null)
                pd = new CustomProgressDialog(FlashLoginActivity.this);
            pd.show();
            pd.setCancelable(false);
            callLoginSendOtpApi(mobNo);
        }
    }

    private void callLoginSendOtpApi(String mobNo) {
        ApiList api = RetrofitClient.getInstance().create(ApiList.class);
        Call<ResponseBody> call = api.sendOtp(getParams(mobNo));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    hideProgressBar();
                    if (response.body() != null) {
                        try {
                            String myResponse = response.body().string();
                            JSONObject jsonObject = new JSONObject(myResponse);
                            //hideProgressBar();
                            timeDifference = System.currentTimeMillis() - timeDifference;
                            //Toast.makeText(loginSignupAct, "onResponse="+timeDifference, Toast.LENGTH_SHORT).show();

                            try {
                                isClickGetOtpBtn = false;
                                String status = jsonObject.getString("status");

                                String errorCode = getResources().getString(R.string.error_code).replace("#", status);
                                if (status.equals(CGlobalVariables.USER_ALREADY_EXIST_OTP_SEND) ||
                                        status.equals(CGlobalVariables.NEW_USER_OTP_SEND)) {
                                    String newUSer = "0";
                                    if (status.equals(CGlobalVariables.NEW_USER_OTP_SEND)) {
                                        newUSer = status;
                                    }
                                    try {
                                        JSONArray jsonArray = jsonObject.getJSONArray("as");
                                        JSONObject jsonObjectAS = jsonArray.getJSONObject(0);
                                        astroSageUserId = jsonObjectAS.getString("userid");
                                    } catch (Exception e) {
                                        //
                                    }
                                    String msg = jsonObject.optString("msg");
                                    Intent myIntent = new Intent(FlashLoginActivity.this, FlashOtpVerifyActivity.class);
                                    myIntent.putExtra(CGlobalVariables.IS_FROM_SCREEN, isFromScreen);
                                    myIntent.putExtra(CGlobalVariables.NEW_USER, newUSer);
                                    myIntent.putExtra(CGlobalVariables.PHONE_NO, mobileNumber);
                                    myIntent.putExtra(KEY_USER_ID, astroSageUserId);
                                    myIntent.putExtra(CGlobalVariables.MESSAGES_FBD_KEY, msg);
                                    startActivity(myIntent);
                                } else if (status.equals(CGlobalVariables.USER_OTP_MAX_LIMIT)) {
                                    String msg = jsonObject.getString("msg");
                                    CUtils.showSnackbar(containerLayout, msg + " " + errorCode, FlashLoginActivity.this);
                                } else if (status.equals("5")) {
                                    String message = getString(R.string.your_number_has_been_blocked);
                                    CUtils.showSnackbar(containerLayout, message + " " + errorCode, FlashLoginActivity.this);
                                } else {
                                    CUtils.showSnackbar(containerLayout, getResources().getString(R.string.invalid_mobie_no) + " " + errorCode, FlashLoginActivity.this);
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        } catch (Exception e) {
                            //
                        }
                    }
                } catch (Exception e) {
                    //
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideProgressBar();
                CUtils.showSnackbar(containerLayout, getString(R.string.server_error), FlashLoginActivity.this);
            }
        });
    }

    public Map<String, String> getParams(String mobileNo) {

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(FlashLoginActivity.this));
        headers.put(CGlobalVariables.PHONE_NO, mobileNo);
        headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(FlashLoginActivity.this));
        headers.put(CGlobalVariables.DEVICE_ID, CUtils.getMyAndroidId(FlashLoginActivity.this));
        headers.put(CGlobalVariables.REG_SOURCE, CGlobalVariables.APP_SOURCE);
        headers.put(CGlobalVariables.APP_VERSION, BuildConfig.VERSION_NAME);
        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(FlashLoginActivity.this));
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
        headers.put(CGlobalVariables.OPERATION_NAME, com.ojassoft.astrosage.utils.CGlobalVariables.OPERATION_NAME_SIGNUP);
        headers.put("rt", String.valueOf(CUtils.getTimeFromMillis()));

        //Log.d("LoginFlow", " login: " + headers);
        return headers;
    }

    private void checkCacheCountryListData() {
        String url = CGlobalVariables.COUNTRY_LIST_URL;
        Cache cache = VolleySingleton.getInstance(FlashLoginActivity.this).getRequestQueue().getCache();
        Cache.Entry entry = cache.get("1-" + url);
        String saveData = "";
        // cache data
        try {
            if (entry != null) {
                saveData = new String(entry.data, StandardCharsets.UTF_8);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(saveData)) {
            saveData = "{\"countries\":[{\"name\":\"India\",\"code\":\"91\"},{\"name\":\"USA\",\"code\":\"1\"},{\"name\":\"UK\",\"code\":\"44\"},{\"name\":\"Australia\",\"code\":\"61\"},{\"name\":\"Brazil\",\"code\":\"55\"},{\"name\":\"Canada\",\"code\":\"1\"},{\"name\":\"France\",\"code\":\"33\"},{\"name\":\"Germany\",\"code\":\"49\"},{\"name\":\"Israel\",\"code\":\"972\"},{\"name\":\"Japan\",\"code\":\"81\"},{\"name\":\"New Zealand\",\"code\":\"64\"},{\"name\":\"Singapore\",\"code\":\"65\"},{\"name\":\"Saudi Arabia\",\"code\":\"966\"},{\"name\":\"United Arab Emirates\",\"code\":\"971\"}]}";
        }
        isShowProgressBar = false;
        parseCountryList(saveData);

        if (!CUtils.isConnectedWithInternet(FlashLoginActivity.this)) {
            CUtils.showSnackbar(containerLayout, getResources().getString(R.string.no_internet), FlashLoginActivity.this);
        } else {
            if (isShowProgressBar) {
                if (pd == null)
                    pd = new CustomProgressDialog(FlashLoginActivity.this);
                pd.show();
                pd.setCancelable(false);
                isShowProgressBar = false;
            }

            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.getCountryList(getParamsCountryList());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    hideProgressBar();
                    if (response.body() != null) {
                        try {
                            String myResponse = response.body().string();
                            parseCountryList(myResponse);
                            if (adapter != null) {
                                adapter.notifyDataSetChanged();
                            }
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    hideProgressBar();
                }
            });
        }
    }

    private void parseCountryList(String saveData) {
        if (saveData != null && saveData.length() > 0) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(saveData);
                JSONArray jsonArray = jsonObject.getJSONArray("countries");
                if (jsonArray != null && jsonArray.length() > 0) {
                    countryBeanList = new ArrayList<>();
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        CountryBean countryBean = new CountryBean();
                        countryBean.setCountryName(object.getString("name"));
                        countryBean.setCountryCode(object.getString("code"));
                        countryBeanList.add(countryBean);
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void startPrefatchDataService() {
        try {
            Intent intentService = new Intent(FlashLoginActivity.this, PreFetchDataservice.class);
            startService(intentService);
        } catch (Exception e) {
        }

    }

    private UserProfileData parseJsonObject(String responseResult) {
        UserProfileData userProfileDataBean;
        try {
            JSONObject jsonObject = new JSONObject(responseResult);
            Gson gson = new Gson();
            userProfileDataBean = gson.fromJson(jsonObject.toString(), UserProfileData.class);

            CUtils.saveUserSelectedProfileInPreference(FlashLoginActivity.this, userProfileDataBean);
            CUtils.saveProfileForChatInPreference(FlashLoginActivity.this, userProfileDataBean);

        } catch (JSONException e) {
            e.printStackTrace();
            userProfileDataBean = null;
        }

        return userProfileDataBean;
    }

    public Map<String, String> getParamsCountryList() {

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(FlashLoginActivity.this));
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
        return CUtils.setRequiredParams(headers);
    }

    @Override
    public void onResponse(String response, int method) {

        hideProgressBar();
        if (response != null && response.length() > 0) {
            Log.e("TestOtp", "response " + response);
            if (method == 2) {
                parseCountryList(response);
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            }
        } else {
            CUtils.showSnackbar(containerLayout, getResources().getString(R.string.server_error), FlashLoginActivity.this);
        }
    }

    @Override
    public void onError(VolleyError error) {
        //Log.d("LoginFlow", " error: " + error);
        timeDifference = System.currentTimeMillis() - timeDifference;
        //Toast.makeText(loginSignupAct, "onError="+timeDifference, Toast.LENGTH_SHORT).show();
        isClickGetOtpBtn = false;
        hideProgressBar();
    }

    /**
     * hide Progress Bar
     */
    public void hideProgressBar() {
        try {
            if (pd != null && pd.isShowing())
                pd.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /***************************** Gmail choose dialogue start **********************/
    public void requestCredentials() {
        if (!isEmailDialogueOpened) {
            requestPhoneNumber();
        }
    }


    private void setupRequestPhoneNumber() {
        request = GetPhoneNumberHintIntentRequest.builder().build();
        phoneNumberHintIntentResultLauncher =
                registerForActivityResult(
                        new ActivityResultContracts.StartIntentSenderForResult(),
                        new ActivityResultCallback() {
                            @Override
                            public void onActivityResult(Object resultObj) {
                                isEmailDialogueOpened = true;
                                setFocusOnEmail();
                                try {
                                    ActivityResult result = ((ActivityResult) resultObj);
                                    selectedMobNum = Identity.getSignInClient(FlashLoginActivity.this).getPhoneNumberFromIntent(result.getData());
                                    if (selectedMobNum.length() > 10) {
                                        selectedMobNum = selectedMobNum.substring(selectedMobNum.length() - 10);
                                    }
                                    CUtils.saveAstroshopUserEmail(FlashLoginActivity.this, selectedMobNum);
                                    mobileNumberTxt.setText(selectedMobNum);
                                } catch (Exception e) {
                                    Log.e(TAG, "Phone Number Hint failed", e);
                                }
                            }
                        });
    }

    private void requestPhoneNumber() {
        Identity.getSignInClient(this)
                .getPhoneNumberHintIntent(request)
                .addOnSuccessListener( result -> {
                    try {
                        isEmailDialogueOpened = true;
                        setFocusOnEmail();
                        phoneNumberHintIntentResultLauncher.launch(new IntentSenderRequest.Builder(result.getIntentSender()).build());
                    } catch(Exception e) {
                        Log.e(TAG, "Launching the PendingIntent failed", e);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e(TAG, "Phone Number Hint failed", e);
                    isEmailDialogueOpened = true;
                    setFocusOnEmail();
                });

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TcSdk.SHARE_PROFILE_REQUEST_CODE) {
            try {
                TcSdk.getInstance().onActivityResultObtained(this, requestCode, resultCode, data);
            } catch (Exception e) {
                try {
                    TcSdk.init(tcSdkOptions);
                    TcSdk.getInstance().onActivityResultObtained(this, requestCode, resultCode, data);
                } catch (Exception e1) {
                    CUtils.showSnackbar(containerLayout, e1.toString(), FlashLoginActivity.this);
                }
            }

        } /*else if (requestCode == RC_HINT_EMAIL) {
            isEmailDialogueOpened = true;
            setFocusOnEmail();
            if (resultCode == RESULT_OK) {
                //Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                //selectedMobNum = credential.getId();
                if (selectedMobNum.length() > 10) {
                    selectedMobNum = selectedMobNum.substring(selectedMobNum.length() - 10);
                }
                CUtils.saveAstroshopUserEmail(FlashLoginActivity.this, selectedMobNum);
                mobileNumberTxt.setText(selectedMobNum);
            }
        }*/ else if (requestCode == SUB_ACTIVITY_USER_LOGIN && resultCode == RESULT_OK) {
            CUtils.saveBooleanData(FlashLoginActivity.this, CGlobalVariables.PREF_FIRST_INSTALL_APP_KEY, true);
            Bundle b = data.getExtras();
            _userId = b.getString("LOGIN_NAME");
            _pwd = b.getString("LOGIN_PWD");
            redirectToNextActivity(null);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case CGlobalVariables.RECEIVE_SMS_PERMISSION_REQUEST:
                sendOtpToMobile(mobileNumber);
                break;
        }
    }

    private void showDialog() {
        String strFilter;
        final EditText inputSearch;
        final Dialog dialog = new Dialog(FlashLoginActivity.this);
        dialog.setCanceledOnTouchOutside(true);

        View view = getLayoutInflater().inflate(R.layout.lay_city_custompopup, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(view);

        inputSearch = view.findViewById(R.id.edtcountry);
        strFilter = inputSearch.getText().toString();
        RecyclerView recyclerView = view.findViewById(R.id.listview);
        recyclerView.setLayoutManager(new LinearLayoutManager(FlashLoginActivity.this, LinearLayoutManager.VERTICAL, false));
        adapter = new CountryListAdapter(FlashLoginActivity.this, countryBeanList);
        recyclerView.setAdapter(adapter);

        adapter.setOnClickListener(new RecyclerClickListner() {
            @Override
            public void onItemClick(int position, View v) {
                if (position != -1) {
                    CountryBean countryBean = countryBeanList.get(position);
                    //Log.e("COUNTRY ", "" + countryBean.getCountryName());
                    if (countryBean != null) {
                        String countryCode = countryBean.getCountryCode();
                        //Log.e("COUNTRY CODE ", "" + countryCode);
                        countryCodeText.setText(countryBean.getCountryName() + " (+" + countryCode + ")");
                        CUtils.setCountryCode(FlashLoginActivity.this, countryCode);
                        if (countryCodeText.getText().toString().contains("+91")) {
                            mobileNumberTxt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
                        } else {
                            mobileNumberTxt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
                        }
                    }
                    dialog.dismiss();
                    try {
                        if (adapter != null) {
                            countryBeanList = (ArrayList<CountryBean>) adapter.filter("");
                            adapter.notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onItemLongClick(int position, View v) {

            }
        });

        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                String text = inputSearch.getText().toString().toLowerCase(Locale.getDefault());
                countryBeanList = (ArrayList<CountryBean>) adapter.filter(text);
                adapter.notifyDataSetChanged();
            }
        });
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        TcSdk.clear();
    }


    private void handleAstroSageLogin(JSONObject jsonObject) {
        try {
            String astroSageUserId = com.ojassoft.astrosage.utils.CUtils.getUserName(this);
            if (com.ojassoft.astrosage.utils.CUtils.isUserLogedIn(this) || !TextUtils.isEmpty(astroSageUserId)) {
                return; //if already loggedin with astrosage-id then return
            }
            //Log.d("LoginFlow", " handleAstroSageLogin()1");
            JSONArray jsonArray = jsonObject.getJSONArray("as");
            JSONObject respObj = jsonArray.getJSONObject(0);

            String respCode = respObj.getString("msgcode");
            if (respCode.equals("22")) {               //if plan activated successfully
                com.ojassoft.astrosage.utils.CUtils.saveBooleanData(this, com.ojassoft.astrosage.utils.CGlobalVariables.needToSendDeviceIdForLogin, false);
                CUtils.showSnackbar(containerLayout, getResources().getString(R.string.plan_activate_success), this);
            } else if (respCode.equals("3")) {
                com.ojassoft.astrosage.utils.CUtils.saveBooleanData(this, com.ojassoft.astrosage.utils.CGlobalVariables.needToSendDeviceIdForLogin, false);
                CUtils.showSnackbar(containerLayout, getResources().getString(R.string.user_exist_record_not_found), this);
            } else {
                CUtils.showSnackbar(containerLayout, getResources().getString(R.string.sign_in_success), FlashLoginActivity.this);
            }

            HashMap<String, String> jsonObjHash = com.ojassoft.astrosage.utils.CUtils.parseLoginSignupJson(respObj);
            if (jsonObjHash.size() > 0) {
                if (jsonObjHash.containsKey(com.ojassoft.astrosage.utils.CGlobalVariables.USERID)) {
                    _userId = jsonObjHash.get(com.ojassoft.astrosage.utils.CGlobalVariables.USERID);
                }
                if (jsonObjHash.containsKey(com.ojassoft.astrosage.utils.CGlobalVariables.USER_PASSWORD)) {
                    _pwd = jsonObjHash.get(com.ojassoft.astrosage.utils.CGlobalVariables.USER_PASSWORD);
                }
                CUtils.saveInformation(FlashLoginActivity.this, _userId, _pwd, jsonObjHash);
            }
            //Log.d("LoginFlow", " handleAstroSageLogin()2");
        } catch (Exception e) {
            Log.i("mTrueCallerLogs", "handleAstroSageLogin exception-->" + e);
        }
    }

    private void redirectToNextActivity(UserProfileData userProfileDataBean) {
        try {
            finish();
        } catch (Exception e) {
            Log.i("mTrueCallerLogs", "redirectToNextActivity exception-->" + e);
        }
    }

    private void sendRequestToTrueCaller(TcOAuthData tcOAuthData) {
        CUtils.hideMyKeyboard(this);
        if (!CUtils.isConnectedWithInternet(FlashLoginActivity.this)) {
            CUtils.showSnackbar(containerLayout, getResources().getString(R.string.no_internet), FlashLoginActivity.this);
        } else {
            if (pd == null)
                pd = new CustomProgressDialog(FlashLoginActivity.this);
            pd.show();
            callApiTrueCaller(tcOAuthData);
        }
    }

    private void callApiTrueCaller(TcOAuthData tcOAuthData) {
        ApiList api = RetrofitClient.getInstance().create(ApiList.class);
        Call<ResponseBody> call = api.registerWithTrueCaller(getParamsNumberVerify(tcOAuthData));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    String myResponse = response.body().string();
                    // Log.i("testTrueCaller","myResponse==>>"+myResponse);
                    JSONObject jsonObject = new JSONObject(myResponse);
                    // Log.i("testTrueCaller","jsonObject==>>"+jsonObject);
                    String status = jsonObject.getString("status");
                    //  Log.i("testTrueCaller","status==>>"+status);
                    if (status.equals(CGlobalVariables.OTP_VERIFIED_AND_RETURN_USERDATA)) {
                        CUtils.saveBannerData("");
                        startPrefatchDataService();
                        CUtils.startFollowerSubscriptionService(FlashLoginActivity.this);

                        com.ojassoft.astrosage.varta.utils.CUtils.unSubscribeTopics("", com.ojassoft.astrosage.varta.utils.CGlobalVariables.TOPIC_NOT_LOGGEDIN, FlashLoginActivity.this);
                        com.ojassoft.astrosage.varta.utils.CUtils.subscribeTopics("", com.ojassoft.astrosage.varta.utils.CGlobalVariables.TOPIC_LOGGEDIN, FlashLoginActivity.this);

                        BeanHoroPersonalInfo beanHoroPersonalInfo = com.ojassoft.astrosage.utils.CUtils.getBeanHoroscopeInfoObject(jsonObject);
                        com.ojassoft.astrosage.utils.CUtils.saveDefaultKundliIfAvailable(FlashLoginActivity.this,beanHoroPersonalInfo);

                        UserProfileData userProfileDataBean = parseJsonObject(myResponse);
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_SIGNUP_LOGIN_SUCCESS_TRUE_CALLER, CGlobalVariables.FIREBASE_EVENT_SIGNUP_LOGIN, "");
                        //save password in preference
                        CUtils.setUserLoginPassword(FlashLoginActivity.this, jsonObject.getString(KEY_PASSWORD));
                        CUtils.userOfferAfterLogin = userProfileDataBean.getPrivateintrooffertype();
                        CUtils.setUserOffers(FlashLoginActivity.this, userProfileDataBean.isLiveintrooffer(), userProfileDataBean.getPrivateintrooffertype());
                        CUtils.setSecondFreeChat(FlashLoginActivity.this, userProfileDataBean.isSecondFreeChat());

                        String bonusStatus = "0", isShowOneRsPopup = "0", amountOnPopup = "0", countryCodeTrueCaller = "", phoneno = "";
                        if (jsonObject.has("eligibleforsignupbonus")) {
                            bonusStatus = jsonObject.getString("eligibleforsignupbonus");
                        }
                        if (jsonObject.has("showonerepopup")) {
                            isShowOneRsPopup = jsonObject.getString("showonerepopup");
                            CUtils.setDataForOneRsDialog(FlashLoginActivity.this, isShowOneRsPopup);
                        }
                        if (jsonObject.has("amountonpopup")) {
                            amountOnPopup = jsonObject.getString("amountonpopup");
                            CUtils.setAmountOnDialog(FlashLoginActivity.this, amountOnPopup);
                        }
                        if (jsonObject.has("countrycode")) {
                            countryCodeTrueCaller = jsonObject.getString("countrycode");
                            CUtils.setCountryCode(FlashLoginActivity.this, countryCodeTrueCaller);
                        }
                        if (jsonObject.has("phoneno")) {
                            phoneno = jsonObject.getString("phoneno");
                        }
                        mobileNumber = phoneno;
                        CUtils.saveLoginDetailInPrefs(FlashLoginActivity.this, phoneno, true, userProfileDataBean.getWalletbalance(), bonusStatus);

                        if (jsonObject.has("blockedby")) {
                            try {
                                String blockedby = jsonObject.getString("blockedby");
                                String[] blockByAstrologer = blockedby.split("\\s*,\\s*");
                                CUtils.setblockByAstrologerList(blockByAstrologer);
                            } catch (Exception e) {
                            }
                        }
                        if (jsonObject.has("userid")) {
                            CUtils.setUserIdForBlock(FlashLoginActivity.this, jsonObject.getString("userid"));
                        }

                        String bonustitle = "", bonusdescription = "";
                        if (jsonObject.has("bonustitle")) {
                            bonustitle = jsonObject.getString("bonustitle");
                        }
                        if (jsonObject.has("bonusdescription")) {
                            bonusdescription = jsonObject.getString("bonusdescription");
                        }

                        if (!TextUtils.isEmpty(bonustitle) && !TextUtils.isEmpty(bonusdescription)) {
                            boolean isNotificationShown = CUtils.getBooleanData(FlashLoginActivity.this, CGlobalVariables.IS_49_ADDED_NOTIF_SHOWN, false);
                            if (!isNotificationShown) {
                                CUtils.saveBooleanData(FlashLoginActivity.this, CGlobalVariables.IS_49_ADDED_NOTIF_SHOWN, true);
                                CreateCustomLocalNotification notification = new CreateCustomLocalNotification(FlashLoginActivity.this);
                                notification.showLocalNotification(bonustitle, bonusdescription, true);
                            }
                        }

                        handleAstroSageLogin(jsonObject);
                        if (pd != null) {
                            pd.dismiss();
                        }
                        redirectToNextActivity(userProfileDataBean);
                    } else {
                        CUtils.showSnackbar(containerLayout, getResources().getString(R.string.truecaller_login_error), FlashLoginActivity.this);
                        if (pd != null) {
                            pd.dismiss();
                        }

                    }
                    CUtils.saveAstroList("");
                    com.ojassoft.astrosage.varta.utils.CUtils.startToGetAiPassSubService(FlashLoginActivity.this);
                } catch (Exception e) {
                    Log.i("testTrueCaller", "response method 3 exception-->" + e);
                    e.printStackTrace();
                    CUtils.showSnackbar(containerLayout, getResources().getString(R.string.truecaller_login_error), FlashLoginActivity.this);
                    if (pd != null) {
                        pd.dismiss();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Log.i("testTrueCaller","t==>>"+t);
                //onBackPressed();
                if (pd != null) {
                    pd.dismiss();
                }
            }
        });


    }


    public Map<String, String> getParamsNumberVerify(TcOAuthData tcOAuthData) {
        // Need to update newUser Value for trueCaller
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("clientid", getString(R.string.clientID));
        headers.put("code", tcOAuthData.getAuthorizationCode());
        headers.put("codeverifier", codeVerifier);
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(FlashLoginActivity.this));

        headers.put(CGlobalVariables.DEVICE_ID, CUtils.getMyAndroidId(FlashLoginActivity.this));
        headers.put(CGlobalVariables.REG_SOURCE, CGlobalVariables.APP_SOURCE);
        headers.put(CGlobalVariables.APP_VERSION, BuildConfig.VERSION_NAME);
        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(FlashLoginActivity.this));


        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
        headers.put(CGlobalVariables.OPERATION_NAME, com.ojassoft.astrosage.utils.CGlobalVariables.OPERATION_NAME_LOGIN);

        int kundliCount = com.ojassoft.astrosage.utils.CUtils.getKundliCount(this);
        String strFirstLoginAfterPlanPurchase = "";
        if (com.ojassoft.astrosage.utils.CUtils.getBooleanData(this, com.ojassoft.astrosage.utils.CGlobalVariables.needToSendDeviceIdForLogin, false)) {
            strFirstLoginAfterPlanPurchase = com.ojassoft.astrosage.utils.CGlobalVariables.FIRST_LOGIN_AFTER_PLAN_PURCHASED;
        }
        String userId = com.ojassoft.astrosage.utils.CUtils.getUserName(this);
        headers.put(CGlobalVariables.ASTROSAGE_USERID, com.ojassoft.astrosage.utils.CUtils.replaceEmailChar(userId));
        headers.put("firstloginafterplanpurchase", strFirstLoginAfterPlanPurchase);
        headers.put("isverified", "1");
        headers.put("nocharts", kundliCount + "");
        String installedReferredCode = CUtils.getStringData(FlashLoginActivity.this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_REFERRER_URL, "");
        Log.d("InstallRef","installedReferredCode: uniquereferredcode==>>  "+installedReferredCode);
        if(CUtils.isReferralCodeValid(installedReferredCode)){
            headers.put("uniquereferredcode", installedReferredCode);
        }
        return headers;
    }
}
