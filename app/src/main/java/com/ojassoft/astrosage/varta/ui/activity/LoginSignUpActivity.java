package com.ojassoft.astrosage.varta.ui.activity;

import static com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL;
import static com.ojassoft.astrosage.ui.act.BaseInputActivity.SUB_ACTIVITY_USER_LOGIN;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_PASSWORD;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_USER_ID;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.AUTO_OPEN_LOGIN;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.IS_GOOGLE_FACEBOOK_VISIBLE;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_FILTER_TYPE;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.CancellationSignal;
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
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.IntentSenderRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.CustomCredential;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Cache;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.google.android.gms.auth.api.identity.GetPhoneNumberHintIntentRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.gson.Gson;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.controller.ControllerManager;
import com.ojassoft.astrosage.model.GmailAccountInfo;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.notification.ActShowOjasSoftArticles;
import com.ojassoft.astrosage.ui.act.ActAppModule;
import com.ojassoft.astrosage.ui.act.ActLogin;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.varta.adapters.CountryListAdapter;
import com.ojassoft.astrosage.varta.interfacefile.RecyclerClickListner;
import com.ojassoft.astrosage.varta.model.CountryBean;
import com.ojassoft.astrosage.varta.model.UserProfileData;
import com.ojassoft.astrosage.varta.service.PreFetchDataservice;
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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginSignUpActivity extends BaseActivity implements View.OnClickListener, VolleyResponse {

    private static final String TAG = LoginSignUpActivity.class.getSimpleName();
    private static final int RC_HINT_EMAIL = 2;
    private static final int TRUECALLERSDKAPI = 101;
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
    private boolean isGoogleApiClientConnected;
    private boolean isEmailDialogueOpened;
    private String selectedMobNum;
    private CheckBox agreeCheckBox;
    public static Activity loginSignupAct;
    Spinner tvCountryCodeTxt;
    ArrayList<CountryBean> countryBeanList = null;
    boolean isShowProgressBar = false;
    ImageView dropdown_img;
    CountryListAdapter adapter;
    LinearLayout country_code_layout;
    TextView countryCodeText;
    boolean isClickGetOtpBtn = false;
    TextView orWithTxt;
    LinearLayout truecaller_button;
    Button astrosageIdBtn;
    LinearLayout linLayoutEmail,linLayoutGoogle,linLayoutFB;
    TextView astrosageText;
    LinearLayout dontHaveAccountLL,linLayoutSocialLogin;
    private String astroSageUserId = "";
    private String _userId = "", _pwd = "";

    long timeDifference = 0;
    String codeVerifier;
    boolean isTrueCallerUsable;

    TcSdkOptions tcSdkOptions;
    GetPhoneNumberHintIntentRequest request;
    ActivityResultLauncher<IntentSenderRequest> phoneNumberHintIntentResultLauncher;
    private CredentialManager credentialManager;
    private FirebaseAuth mAuth;
    private static final int MAX_RETRY_ATTEMPTS = 3;
    private int retryCount = 0;
    private CallbackManager callbackManager;
    private Typeface regularTypeface;
    boolean isGoogleFacebookVisible = false;
    String image_url = "";
    boolean isLoginMandatory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_signup);
        credentialManager = CredentialManager.create(this);
        mAuth = FirebaseAuth.getInstance();


        getResources().getConfiguration().setLocale(Locale.getDefault());
        loginSignupAct = this;
        CUtils.getRobotoFont(getApplicationContext(), CGlobalVariables.LANGUAGE_CODE, CGlobalVariables.regular);

        initView();
        trueCallerInitialize();
        setupRequestPhoneNumber();

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                // Back is pressed...
                redirectToNextActivity();
            }
        });
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
                            new TcSdkOptions.Builder(LoginSignUpActivity.this, new TcOAuthCallback() {
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
                        String stateRequested = (new BigInteger(130, (Random) (new SecureRandom()))).toString(32);
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
                        TcSdk.getInstance().getAuthorizationCode(LoginSignUpActivity.this);
                        //boolean isTrueCallerInstalled = CUtils.isAppInstalled(LoginSignUpActivity.this, "com.truecaller");
                        //TcSdk.clear();
                    }
                    // UI process
                    handler.post(() -> {
                        if (isTrueCallerUsable) {
                            truecaller_button.setVisibility(View.VISIBLE);
                            orWithTxt.setVisibility(View.VISIBLE);

                        } else {
                            truecaller_button.setVisibility(View.GONE);
                            orWithTxt.setVisibility(View.GONE);

                        }

                        if (com.ojassoft.astrosage.utils.CUtils.isUserLogedIn(LoginSignUpActivity.this) && !isTrueCallerUsable) {
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
        String url = "";
        switch (textView.getId()) {

            case R.id.textViewPrivacyPolicy:
                url = com.ojassoft.astrosage.utils.CGlobalVariables.ASTROSAGE_PRIVACY_POLICY_URL;
                intent.putExtra("TITLE_TO_SHOW", "Privacy Policy");
                break;
            case R.id.textViewTermsAndConditions:
                url =  com.ojassoft.astrosage.utils.CGlobalVariables.ASTROSAGE_TERMS_CONDITIONS_URL;
                intent.putExtra("TITLE_TO_SHOW", "Terms and Conditions");
                break;
        }
        url = com.ojassoft.astrosage.utils.CUtils.addParamsForDarkInURL(getApplicationContext(),url);
        intent.putExtra(com.ojassoft.astrosage.utils.CGlobalVariables.ASTRO_WEBVIEW_TITLE_KEY, com.ojassoft.astrosage.utils.CGlobalVariables.MODULE_ABOUT_US);
        intent.putExtra("URL", url);

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
        linLayoutEmail = findViewById(R.id.linLayoutEmail);
        linLayoutFB = findViewById(R.id.linLayoutFB);
        linLayoutGoogle = findViewById(R.id.linLayoutGoogle);
        astrosageText = findViewById(R.id.astrosage_txt);
        truecaller_button = findViewById(R.id.truecaller_button);
        dontHaveAccountLL = findViewById(R.id.dont_have_account_ll);
        linLayoutSocialLogin = findViewById(R.id.linLayoutSocialLogin);
        regularTypeface = com.ojassoft.astrosage.utils.CUtils.getRobotoFont(
                getApplicationContext(), LANGUAGE_CODE, com.ojassoft.astrosage.utils.CGlobalVariables.regular);
        FontUtils.changeFont(LoginSignUpActivity.this, astrosageText, CGlobalVariables.FONTS_DIAVLO_BOLD);
        FontUtils.changeFont(LoginSignUpActivity.this, astrosageIdBtn, CGlobalVariables.FONTS_DIAVLO_BOLD);

        FontUtils.changeFont(LoginSignUpActivity.this, countryCodeText, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(LoginSignUpActivity.this, loginHeadingTxt, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(LoginSignUpActivity.this, getOtpBtn, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(LoginSignUpActivity.this, mobileNumberTxt, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(LoginSignUpActivity.this, dontHaveAccountTxt, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        //FontUtils.changeFont(LoginSignUpActivity.this, signUpTxt, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(LoginSignUpActivity.this, skipCloseButton, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(LoginSignUpActivity.this, language_button, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(LoginSignUpActivity.this, agreeText, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(LoginSignUpActivity.this, sendOtpMsgTV, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(LoginSignUpActivity.this, orWithTxt, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(LoginSignUpActivity.this, (TextView) findViewById(R.id.textViewPrivacyPolicy), CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(LoginSignUpActivity.this, (TextView) findViewById(R.id.textViewTermsAndConditions), CGlobalVariables.FONTS_OPEN_SANS_REGULAR);

        CUtils.startGcmService(LoginSignUpActivity.this);
        initListners();
        queue = VolleySingleton.getInstance(LoginSignUpActivity.this).getRequestQueue();
        checkCacheCountryListData();
         isGoogleFacebookVisible = getIntent().getBooleanExtra(IS_GOOGLE_FACEBOOK_VISIBLE, false);

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

            // hide skip button when open login screen firsttime after install in case of login mandatory from configration
            isLoginMandatory = CUtils.getBooleanData(this, com.ojassoft.astrosage.utils.CGlobalVariables.IS_LOGIN_MANDATORY, false);
            if(isLoginMandatory && (isFromScreen != null && isFromScreen.equals(CGlobalVariables.LANGUAGE_SELECTION_SCRREN))){
                skipCloseButton.setVisibility(View.GONE);
            }
        }


//        boolean isUserFirstTimeInstallTheApp = CUtils.getBooleanData(this, com.ojassoft.astrosage.utils.CGlobalVariables.IS_FIRST_INSTALL_KEY, true);
//
//        if (isUserFirstTimeInstallTheApp){
//            IsFacebookGoogleLoginVisible(true);
//        }

        if (countryCodeText.getText().toString().contains("+91")) {
            mobileNumberTxt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
        }


        if (com.ojassoft.astrosage.utils.CUtils.isUserLogedIn(LoginSignUpActivity.this)) {
           // astrosageIdBtn.setVisibility(View.INVISIBLE);
           // linLayoutEmail.setVisibility(View.INVISIBLE);
            //IsFacebookGoogleLoginVisible(false);

            if (truecaller_button.getVisibility() != View.VISIBLE) {
                orWithTxt.setVisibility(View.GONE);
            }
           // dontHaveAccountLL.setVisibility(View.GONE);
            loginHeadingTxt.setText(getResources().getString(R.string.text_enter_phone));
        } else {
           // astrosageIdBtn.setVisibility(View.VISIBLE);
          //  linLayoutEmail.setVisibility(View.VISIBLE);
           // IsFacebookGoogleLoginVisible(true);
            if (truecaller_button.getVisibility() != View.VISIBLE) {
                orWithTxt.setVisibility(View.GONE);
            }else {
                orWithTxt.setVisibility(View.VISIBLE);
            }



            //  dontHaveAccountLL.setVisibility(View.VISIBLE);
        }
        IsFacebookGoogleLoginVisible(isGoogleFacebookVisible);


        SpannableString textSpan = new SpannableString(getString(R.string.terms_and_con_text));

        String termsText = getString(R.string.terms_and_con_text);
        String terms = getString(R.string.terms);
        String privacy = getString(R.string.privacy_policy);
        int termsStartIndex = termsText.indexOf(terms);
        int privacyStartIndex = termsText.indexOf(privacy);
        if(termsStartIndex != -1 && privacyStartIndex != -1) {
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

    private void IsFacebookGoogleLoginVisible( boolean isVisible) {

        if (!isVisible){

            linLayoutSocialLogin.setVisibility(View.GONE);
        }else {
            if (com.ojassoft.astrosage.utils.CUtils.isUserLogedIn(LoginSignUpActivity.this)) {

                linLayoutSocialLogin.setVisibility(View.GONE);
            }else {

                linLayoutSocialLogin.setVisibility(View.VISIBLE);
            }

        }



    }

    private void getPreferencesCountryCode() {
        // Loop through the list using a for-each loop
        for (CountryBean countryBean : countryBeanList) {
            String countryCode = countryBean.getCountryCode();

            if (countryCode.equals(CUtils.getCountryCode(this))) {
               // countryCodeText.setText(countryBean.getCountryName() + " (+" + countryCode + ")");
                countryCodeText.setText( "+" +countryCode );
            }
        }
    }
public static String logMsgForFacebookLogin = "";
    private void initListners() {
        getOtpBtn.setOnClickListener(this);
        astrosageIdBtn.setOnClickListener(this);
        linLayoutEmail.setOnClickListener(this);
        linLayoutFB.setOnClickListener(this);
        linLayoutGoogle.setOnClickListener(this);
        signUpTxt.setOnClickListener(this);
        skipCloseButton.setOnClickListener(this);
        language_button.setOnClickListener(this);
        agreeCheckBox.setOnClickListener(this);
        country_code_layout.setOnClickListener(this);

        ivback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getOnBackPressedDispatcher().onBackPressed();
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

        boolean loggedOut = AccessToken.getCurrentAccessToken() == null;
        if (!loggedOut) {
            try {
                logMsgForFacebookLogin = logMsgForFacebookLogin+"logging out because already loggeIn";
                LoginManager.getInstance().logOut();
            }catch (Exception e)
            {
                e.printStackTrace();
            }
            // Picasso.with(this).load(Profile.getCurrentProfile().getProfilePictureUri(200, 200)).into(imageView);
            //Log.d("TAG", "Username is: " + Profile.getCurrentProfile().getName());
            //Using Graph API
            //getUserProfile(AccessToken.getCurrentAccessToken());
        }

        callbackManager = CallbackManager.Factory.create();
        // Register callback
        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                logMsgForFacebookLogin = logMsgForFacebookLogin+"\n loginManager onSuccess loginResult : "+loginResult;
                getUserProfile(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {
                logMsgForFacebookLogin = logMsgForFacebookLogin+"\n loginManager onCancel";
            }

            @Override
            public void onError(FacebookException exception) {
                CUtils.showSnackbar(containerLayout, getResources().getString(R.string.facebook_login_error_try_another_way_msg), LoginSignUpActivity.this);
                logMsgForFacebookLogin = logMsgForFacebookLogin+"\nloginManager onError: "+exception;
            }
        });
    }
    String  userName="";

    private void getUserProfile(AccessToken currentAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                currentAccessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        //Log.d("TAG", object.toString());
                        logMsgForFacebookLogin = logMsgForFacebookLogin+"\n getUserProfile onCompleted object : "+object;
                        try {
                            if(object == null) return;
                            String first_name = object.getString("first_name");
                            String last_name = object.getString("last_name");
                            String email = object.getString("email");
                            String id = object.getString("id");
                             image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";

                            if(TextUtils.isEmpty(email)) return;


                            /*Log.e("FACEBOOK ","first_name"+ first_name);
                            Log.e("FACEBOOK ","last_name"+ last_name);
                            Log.e("FACEBOOK ","email"+ email);
                            Log.e("FACEBOOK ","id"+ id);
                            Log.e("FACEBOOK ","image_url"+ image_url);*/


                            userName = first_name + " "+ last_name;

                            showProgressBar();
                            com.ojassoft.astrosage.utils.CUtils.verifyLoginWithUserPurchasedPlan(new com.libojassoft.android.misc.VolleyResponse() {
                                                                                                     @Override
                                                                                                     public void onResponse(String response, int method) {
                                                                                                         logMsgForFacebookLogin = logMsgForFacebookLogin+"\n verifyLoginWithUserPurchasedPlan onResponse :"+response;

                                                                                                         CUtils.saveGoogleFacebookProfile(LoginSignUpActivity.this,image_url);
                                                                                                         getFacebookLoginResponse(response);

                                                                                                     }

                                                                                                     @Override
                                                                                                     public void onError(VolleyError error) {
                                                                                                         logMsgForFacebookLogin = logMsgForFacebookLogin+"\n verifyLoginWithUserPurchasedPlan onError :"+error;
                                                                                                      failFacebookLogin(error);
                                                                                                     }
                                                                                                 }, email,
                                    "", userName, com.ojassoft.astrosage.utils.CGlobalVariables.OPERATION_NAME_SOCIALMEDIA,
                                    com.ojassoft.astrosage.utils.CGlobalVariables.REG_SOURCE_ANDROID_FACEBOOK,0);

                        } catch (Exception e) {
                            CUtils.showSnackbar(containerLayout, getResources().getString(R.string.facebook_login_error_try_another_way_msg), LoginSignUpActivity.this);
                            logMsgForFacebookLogin = logMsgForFacebookLogin+"\n getUserProfile Exception occured:  : "+e.getMessage();
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();

    }

    private void showProgressBar(){
        if (pd == null) {
            pd = new CustomProgressDialog(this);
        }
        pd.setCanceledOnTouchOutside(false);
        if (!pd.isShowing()) {
            pd.show();
        }
    }

    public void getFacebookLoginResponse(String response){
        String respCode="";
        Log.d("FB_LOGIN & Google","response: "+response);
        logMsgForFacebookLogin = logMsgForFacebookLogin+"\n getFacebookLoginResponse reponse: :"+response;

        try {
            JSONObject respObj = new JSONObject(response);
            respCode = respObj.getString("msgcode");
            hideProgressBar();

                /*10 for new signup, 2 and 60 for simple login
                  1 and 5 for first time login after plan purchased
                  22 for plan activated successfully
                  3 for use id exist but record not found in corresponding to passed device id*/

            if(respCode.equals("10") || respCode.equals("2") || respCode.equals("60") ||
                    respCode.equals("1") || respCode.equals("5") ||
                    respCode.equals("22") || respCode.equals("3"))
            {
                if(respCode.equals("22"))                //if plan activated successfully
                {
                    com.ojassoft.astrosage.utils.CUtils.saveBooleanData(AstrosageKundliApplication.getAppContext(), com.ojassoft.astrosage.utils.CGlobalVariables.needToSendDeviceIdForLogin, false);
                    MyCustomToast mct = new MyCustomToast(this,
                            getLayoutInflater(), this,
                            regularTypeface);
                    mct.show(getResources().getString(R.string.plan_activate_success));

                }else if(respCode.equals("3")){
                    com.ojassoft.astrosage.utils.CUtils.saveBooleanData(AstrosageKundliApplication.getAppContext(), com.ojassoft.astrosage.utils.CGlobalVariables.needToSendDeviceIdForLogin, false);
                    MyCustomToast mct = new MyCustomToast(this,
                            getLayoutInflater(), this,
                            regularTypeface);
                    mct.show(getResources().getString(R.string.user_exist_record_not_found));
                }else {
                    MyCustomToast mct = new MyCustomToast(this,
                            getLayoutInflater(), this,
                            regularTypeface);
                    mct.show(getResources().getString(R.string.sign_in_success));
                }

                HashMap<String, String> jsonObjHash = com.ojassoft.astrosage.utils.CUtils.parseLoginSignupJson(respObj);
                if(jsonObjHash != null && jsonObjHash.size()>0) {

                    if(_userId == null || _userId.length()==0) {
                        if (jsonObjHash.containsKey(com.ojassoft.astrosage.utils.CGlobalVariables.USERID)) {
                            _userId = jsonObjHash.get(com.ojassoft.astrosage.utils.CGlobalVariables.USERID);
                        }
                    }
                    if(_pwd == null || _pwd.length()==0) {
                        if (jsonObjHash.containsKey(com.ojassoft.astrosage.utils.CGlobalVariables.USER_PASSWORD)) {
                            _pwd = jsonObjHash.get(com.ojassoft.astrosage.utils.CGlobalVariables.USER_PASSWORD);
                        }
                    }

                    returnToMasterActivityAfterLogin(_userId, _pwd, jsonObjHash);
                }
            }else {
                if(respCode.equals("40"))
                {
                    MyCustomToast mct = new MyCustomToast(this,
                            this.getLayoutInflater(), this,
                            regularTypeface);
                    mct.show(getResources().getString(R.string.email_doesnot_exist));
                }else if(respCode.equals("30"))
                {
                    MyCustomToast mct = new MyCustomToast(this,
                            this.getLayoutInflater(), this,
                            regularTypeface);
                    mct.show(getResources().getString(R.string.password_not_match));
                }else if(respCode.equals("4"))
                {
                    MyCustomToast mct = new MyCustomToast(this,
                            this.getLayoutInflater(), this,
                            regularTypeface);
                    mct.show(getResources().getString(R.string.sign_up_validation_authentication_failed));
                }
                else {
                    MyCustomToast mct = new MyCustomToast(this,
                            this.getLayoutInflater(), this,
                            regularTypeface);
                    mct.show(getResources().getString(R.string.sign_in_failed));
                }
                //shakeMyViewOnSignFailed(usernamePassword_container);
            }

        } catch (JSONException e) {
            logMsgForFacebookLogin = logMsgForFacebookLogin+"\n getFacebookLoginResponse JSONException :"+e;
            e.printStackTrace();
        }
    }

    private void shakeMyViewOnSignFailed(View viewToAnimate) {
        Animation shake = AnimationUtils.loadAnimation(this,
                R.anim.shake);
        viewToAnimate.startAnimation(shake);
    }

    private void returnToMasterActivityAfterLogin(String userLogin, String pwd, HashMap<String, String> profileInfo) {
        try {
            Log.d("FB_LOGIN", "returnToMasterActivityAfterLogin");
            logMsgForFacebookLogin = logMsgForFacebookLogin+"\n returnToMasterActivityAfterLogin ";

            String userName="", mobile="", occupation="", maritalStatus="",
                    companyName="", address1="", address2="", userPlanExpiryDate="",
                    userPlanPurchaseDate="", userPlanId="1";

            if(profileInfo.containsKey(com.ojassoft.astrosage.utils.CGlobalVariables.USERID))
            {
                userLogin = profileInfo.get(com.ojassoft.astrosage.utils.CGlobalVariables.USERID);
            }
            if(profileInfo.containsKey(com.ojassoft.astrosage.utils.CGlobalVariables.USER_FIRSTNAME))
            {
                userName = profileInfo.get(com.ojassoft.astrosage.utils.CGlobalVariables.USER_FIRSTNAME);
            }
            if(profileInfo.containsKey(com.ojassoft.astrosage.utils.CGlobalVariables.MOBILE))
            {
                mobile = profileInfo.get(com.ojassoft.astrosage.utils.CGlobalVariables.MOBILE);
            }
            if(profileInfo.containsKey(com.ojassoft.astrosage.utils.CGlobalVariables.OCCUPATION))
            {
                occupation = profileInfo.get(com.ojassoft.astrosage.utils.CGlobalVariables.OCCUPATION);
            }
            if(profileInfo.containsKey(com.ojassoft.astrosage.utils.CGlobalVariables.MARITALSTATUS))
            {
                maritalStatus = profileInfo.get(com.ojassoft.astrosage.utils.CGlobalVariables.MARITALSTATUS);
            }
            if(profileInfo.containsKey(com.ojassoft.astrosage.utils.CGlobalVariables.COMPANY_NAME));
            {
                companyName = profileInfo.get(com.ojassoft.astrosage.utils.CGlobalVariables.COMPANY_NAME);
            }
            if(profileInfo.containsKey(com.ojassoft.astrosage.utils.CGlobalVariables.ADDRESS1));
            {
                address1 = profileInfo.get(com.ojassoft.astrosage.utils.CGlobalVariables.ADDRESS1);
            }
            if(profileInfo.containsKey(com.ojassoft.astrosage.utils.CGlobalVariables.ADDRESS2));
            {
                address2 = profileInfo.get(com.ojassoft.astrosage.utils.CGlobalVariables.ADDRESS2);
            }
            if(profileInfo.containsKey(com.ojassoft.astrosage.utils.CGlobalVariables.USERPLAN_ID));
            {
                if(profileInfo.get(com.ojassoft.astrosage.utils.CGlobalVariables.USERPLAN_ID).length()>0) {
                    userPlanId = profileInfo.get(com.ojassoft.astrosage.utils.CGlobalVariables.USERPLAN_ID);
                }
            }
            if(profileInfo.containsKey(com.ojassoft.astrosage.utils.CGlobalVariables.USER_PLAN_EXPIRY_DATE));
            {
                userPlanExpiryDate = profileInfo.get(com.ojassoft.astrosage.utils.CGlobalVariables.USER_PLAN_EXPIRY_DATE);
            }
            if(profileInfo.containsKey(com.ojassoft.astrosage.utils.CGlobalVariables.USER_PLAN_PURCHASE_DATE));
            {
                userPlanPurchaseDate = profileInfo.get(com.ojassoft.astrosage.utils.CGlobalVariables.USER_PLAN_PURCHASE_DATE);
            }

            com.ojassoft.astrosage.utils.CUtils.saveLoginDetailInPrefs(this, userLogin, pwd, userName, true, false);

            // Clear old userid and password from old app
            SharedPreferences settings = this.getSharedPreferences(
                    com.ojassoft.astrosage.utils.CGlobalVariables.MYKUNDLI_PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(com.ojassoft.astrosage.utils.CGlobalVariables.PREF_USER_ID, "");
            editor.putString(com.ojassoft.astrosage.utils.CGlobalVariables.PREF_USERR_PWD, "");
            editor.commit();
            GmailAccountInfo gmailAccountInfo = new GmailAccountInfo();
            gmailAccountInfo.setId(userLogin);
            if (userName.equals("")) {

                gmailAccountInfo.setUserName(userLogin.split("@")[0]);
            } else {
                gmailAccountInfo.setUserName(userName);
            }
            gmailAccountInfo.setMobileNo(mobile);
            if(!TextUtils.isEmpty(occupation)){
                gmailAccountInfo.setOccupation(Integer.parseInt(occupation));
            }else{
                gmailAccountInfo.setOccupation(0);
            }
            if(!TextUtils.isEmpty(maritalStatus)){
                gmailAccountInfo.setMaritalStatus(Integer.parseInt(maritalStatus));
            }else{
                gmailAccountInfo.setMaritalStatus(0);
            }

            gmailAccountInfo.setHeading(companyName);
            gmailAccountInfo.setAddress1(address1);
            gmailAccountInfo.setAddress2(address2);
            com.ojassoft.astrosage.utils.CUtils.saveGmailAccountInfo(this, gmailAccountInfo);
            com.ojassoft.astrosage.utils.CUtils.storeUserPurchasedPlanInPreference(this, Integer.parseInt(userPlanId));

            com.ojassoft.astrosage.utils.CUtils.saveStringData(this, com.ojassoft.astrosage.utils.CGlobalVariables.PREF_USER_PLAN_PURCHASE_DATE, userPlanPurchaseDate);//Purchase plan Date
            com.ojassoft.astrosage.utils.CUtils.saveStringData(this, com.ojassoft.astrosage.utils.CGlobalVariables.PREF_USER_PLAN_Expiry_DATE, userPlanExpiryDate);//Expiry plan  Date
            //iWizardLoginRegisterFragmentInterface.clickedSignInButton(userLogin, pwd);

            // ADDED BY BIJENDRA ON 26-12-14 TO SAVE GCM REGISTRATION INFORMATION ON
            // SERVER
            int languageCode = ((AstrosageKundliApplication) this.getApplication()).getLanguageCode();
            String regid = com.ojassoft.astrosage.utils.CUtils.getRegistrationId(this.getApplicationContext());
            new ControllerManager().saveUserGCMRegistrationInformationOnOjasServer(
                    this.getApplicationContext(), regid, languageCode,
                    userLogin);

            Log.d("FB_LOGIN", "returnToMasterActivityAfterLogin - before redirectToNextActivity");

            redirectToNextActivity();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void failFacebookLogin(VolleyError error) {
        hideProgressBar();
        if (error != null && error.getMessage() != null) {
            MyCustomToast mct = new MyCustomToast(this,getLayoutInflater(), this, regularTypeface);
            mct.show(error.getMessage());
        }
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
                        CUtils.showSnackbar(containerLayout, getResources().getString(R.string.enter_valid_mobile_no), LoginSignUpActivity.this);
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
                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_SKIP, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                CUtils.saveBooleanData(LoginSignUpActivity.this, CGlobalVariables.PREF_FIRST_INSTALL_APP_KEY, true);
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
                Intent intent = new Intent(LoginSignUpActivity.this, ActLogin.class);
                startActivityForResult(intent, SUB_ACTIVITY_USER_LOGIN);
                break;
            }
            case R.id.linLayoutEmail: {
                Intent intent = new Intent(LoginSignUpActivity.this, ActLogin.class);
                startActivityForResult(intent, SUB_ACTIVITY_USER_LOGIN);
                break;
            }
            case R.id.linLayoutFB: {
                com.ojassoft.astrosage.utils.CUtils.googleAnalyticSendWitPlayServie(
                        this,
                        com.ojassoft.astrosage.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK,
                        com.ojassoft.astrosage.utils.CGlobalVariables.FACEBOOK_SIGNUP,
                        null);
               // facebookLoginButton.performClick();
                LoginManager.getInstance().logInWithReadPermissions(
                        this,
                        Arrays.asList("email", "public_profile", "user_friends")
                );
                break;
            }

            case R.id.linLayoutGoogle: {
                if (com.ojassoft.astrosage.utils.CUtils.isConnectedWithInternet(this)) {
                    com.ojassoft.astrosage.utils.CUtils.googleAnalyticSendWitPlayServie(
                            this,
                            com.ojassoft.astrosage.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK,
                            com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_LOGIN,
                            null);
                    signIn();
                } else {
//                    MyCustomToast mct = new MyCustomToast(this,
//                            this.getLayoutInflater(), this,
//                            typeface);
//                    mct.show(getResources().getString(R.string.no_internet));
                }
            }
        }
    }

    private void signIn() {

        launchCredentialManager();
    }
    private void launchCredentialManager() {
        // [START create_credential_manager_request]
        // Instantiate a Google sign-in request
        GetGoogleIdOption googleIdOption = new GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false)
                .setServerClientId(getString(R.string.default_web_client_id))
                .build();

        // Create the Credential Manager request
        GetCredentialRequest request = new GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build();
        // [END create_credential_manager_request]

        // Launch Credential Manager UI
        credentialManager.getCredentialAsync(
                this,
                request,
                new CancellationSignal(),
                Executors.newSingleThreadExecutor(),
                new CredentialManagerCallback() {
                    @Override
                    public void onResult(Object o) {
                        Log.d("CredentialManager", "Credential retrieved successfully");
                        retryCount = 0; // Reset retry count on success
                        handleSignInResult(((GetCredentialResponse) o).getCredential());
                    }

                    @Override
                    public void onError(@NonNull Object o) {
                        Log.e("CredentialManager", "Error during credential retrieval: " + o.toString());
                        if (o.toString().contains("NoCredentialException")) {
                            if (retryCount < MAX_RETRY_ATTEMPTS) {
                                retryCount++;
                                Log.e("CredentialManager", "No credentials found, retrying... Attempt " + retryCount);
                                launchCredentialManager();
                            } else {
                                Log.e("CredentialManager", "Max retry attempts reached. Aborting.");
                                // Handle max retry reached scenario
                            }
                        } else {
                            // Handle other errors
                            Log.e("CredentialManager", "Unhandled error: " + o.toString());
                        }
                    }
                }
        );
    }

    private void handleSignInResult(Credential credential) {
        // Check if credential is of type Google ID
        if (credential instanceof CustomCredential && credential.getType().equals(TYPE_GOOGLE_ID_TOKEN_CREDENTIAL)) {
            // Create Google ID Token
            Bundle credentialData = credential.getData();
            GoogleIdTokenCredential googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credentialData);
            Log.d("FB_LOGIN", "handleSignInResult -  DashBoardActivity");

            // Sign in to Firebase with using the token
            firebaseAuthWithGoogle(googleIdTokenCredential.getIdToken());
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        try {
            AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(this, task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            try {
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                                Log.d("FB_LOGIN", "firebaseAuthWithGoogle -  DashBoardActivity");

                            } catch (Exception e) {
                                //
                            }
                        }
                    });
        } catch (Exception e){
            //
        }
    }

    private void updateUI(FirebaseUser accountDataObj) {
        try{
            if (accountDataObj != null) {

                userName = accountDataObj.getDisplayName();
                _userId = accountDataObj.getEmail();

                if (accountDataObj.getPhotoUrl() != null){
                    image_url = accountDataObj.getPhotoUrl().toString();

                }

                logMsgForFacebookLogin = logMsgForFacebookLogin+"\n updateUI onSuccess accountDataObj : "+accountDataObj.toString();

                    com.ojassoft.astrosage.utils.CUtils.saveAstroshopUserEmail(this,_userId);
                com.ojassoft.astrosage.utils.CUtils.saveBooleanData(this, com.ojassoft.astrosage.utils.CGlobalVariables.EMAIL_IS_VERIFIED,true);
//                Log.e(TAG, "updateUI: ", );
                showProgressBar();
                com.ojassoft.astrosage.utils.CUtils.verifyLoginWithUserPurchasedPlan(new com.libojassoft.android.misc.VolleyResponse() {
                                                                                         @Override
                                                                                         public void onResponse(String response, int method) {

                                                                                             CUtils.saveGoogleFacebookProfile(LoginSignUpActivity.this,image_url);

                                                                                             getFacebookLoginResponse(response);
                                                                                         }

                                                                                         @Override
                                                                                         public void onError(VolleyError error) {
                                                                                             failFacebookLogin(error);

                                                                                         }
                                                                                     }, _userId,
                        "", userName, com.ojassoft.astrosage.utils.CGlobalVariables.OPERATION_NAME_SOCIALMEDIA,
                        com.ojassoft.astrosage.utils.CGlobalVariables.REG_SOURCE_ANDROID_GOOGLE, 0);

            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private void saveToSharedPreferences(HashMap idPic) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    SharedPreferences myPrefrence = getSharedPreferences("MyProfilePicture", MODE_PRIVATE);
                    SharedPreferences.Editor editor = myPrefrence.edit();
                    Set mapSet = idPic.entrySet();
                    //Create iterator on Set
                    Iterator mapIterator = mapSet.iterator();
                    System.out.println("Display the key/value of HashMap.");
                    while (mapIterator.hasNext()) {
                        Map.Entry mapEntry = (Map.Entry) mapIterator.next();
                        // getKey Method of HashMap access a key of map
                        String email = (String) mapEntry.getKey();
                        //getValue method returns corresponding key's value
                        try {
                            Bitmap photo = (Bitmap) mapEntry.getValue();
                            editor.putString(email, encodeToBase64(photo));//added by neeraj 23/06/2016 use method to encode and then store to shared preference
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        editor.commit();
                    }
                } catch (Exception e) {
                    //
                }
            }
        });
    }
    private String encodeToBase64(Bitmap photo) {
        Bitmap immage = photo;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        immage.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] b = baos.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);

        android.util.Log.d("Image Log:", imageEncoded);
        return imageEncoded;
    }

    private void redirectToNextActivity() {
        try {
            if (isFromScreen == null) isFromScreen = "";
            if (isFromScreen.equals(CGlobalVariables.PAYMENT_INFO_SCREEN) ||
                    isFromScreen.equals(CGlobalVariables.BASE_INPUT_SCREEN) ||
                    isFromScreen.equals(CGlobalVariables.LOGOUT_BTN)) {
                setResult(RESULT_CANCELED);
                finish();
            } else if (isFromScreen.equals(CGlobalVariables.LANGUAGE_SELECTION_SCRREN)) {
                
                AstrosageKundliApplication.connectAiChatAfterLogin = false;
                AstrosageKundliApplication.connectHumanChatAfterLogin = false;
                CUtils.saveBooleanData(LoginSignUpActivity.this, CGlobalVariables.PREF_FIRST_INSTALL_APP_KEY, true);


                //if user skip login after notification click send back to ActAppModule
                com.ojassoft.astrosage.utils.CUtils.AINotificationQuestion = "";
                com.ojassoft.astrosage.utils.CUtils.AIRevertQCount = "";
                com.ojassoft.astrosage.utils.CUtils.AINotificationAstroId = "";
                com.ojassoft.astrosage.utils.CUtils.isAIAstrologerOnline = false;
                // do nothing while back press in case of first time login showing language selection screen because of login mandatory from config
                if(!isLoginMandatory) {
                    com.ojassoft.astrosage.utils.CUtils.saveWizardShownFirstTime(LoginSignUpActivity.this);
                    startActivity(new Intent(LoginSignUpActivity.this, ActAppModule.class));
                }
            } else {
                Intent i = new Intent(LoginSignUpActivity.this, DashBoardActivity.class);
                i.putExtra(KEY_FILTER_TYPE, CGlobalVariables.FILTER_TYPE_CALL);
                startActivity(i);
                finish();
            }

        } catch (Exception e) {
            //
        }
    }

    private boolean isValidData(String mobileNo) {
        boolean isValid = mobileNo != null || mobileNo.trim().length() != 0;

        if (CUtils.getCountryCode(LoginSignUpActivity.this).equals("91")) {
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
        if (!CUtils.isConnectedWithInternet(LoginSignUpActivity.this)) {
            isClickGetOtpBtn = false;
            CUtils.showSnackbar(containerLayout, getResources().getString(R.string.no_internet), LoginSignUpActivity.this);
        } else {
            if (pd == null)
                pd = new CustomProgressDialog(LoginSignUpActivity.this);
            pd.show();
            pd.setCancelable(false);
            //Log.e("SAN ", "URL Login " +CGlobalVariables.REGISTRATION_URL);
//            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.REGISTRATION_URL,
//                    LoginSignUpActivity.this, false, getParams(mobNo), 1).getMyStringRequest();
//            stringRequest.setShouldCache(false);
//            queue.add(stringRequest);
            callLoginSendOtpApi(mobNo);
           // Log.e("SAN ", "getUTCTimeFromMillis " +CUtils.getTimeFromMillis());

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
                                    Log.d("InstallRef ", "newUSer  value" + newUSer);
                                    String msg = jsonObject.optString("msg");
                                    Intent i = new Intent(LoginSignUpActivity.this, OtpVerifyActivity.class);
                                    i.putExtra(CGlobalVariables.IS_FROM_SCREEN, isFromScreen);
                                    i.putExtra(CGlobalVariables.NEW_USER, newUSer);
                                    i.putExtra(CGlobalVariables.PHONE_NO, mobileNumber);
                                    i.putExtra(KEY_USER_ID, astroSageUserId);
                                    i.putExtra(CGlobalVariables.MESSAGES_FBD_KEY, msg);
                                    startActivity(i);
                                } else if (status.equals(CGlobalVariables.USER_OTP_MAX_LIMIT)) {
                                    String msg = jsonObject.getString("msg");
                                    CUtils.showSnackbar(containerLayout, msg + " " + errorCode, LoginSignUpActivity.this);
                                } else if (status.equals("5")) {
                                    String message = getString(R.string.your_number_has_been_blocked);
                                    CUtils.showSnackbar(containerLayout, message + " " + errorCode, LoginSignUpActivity.this);
                                } else {
                                    CUtils.showSnackbar(containerLayout, getResources().getString(R.string.invalid_mobie_no) + " " + errorCode, LoginSignUpActivity.this);
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
                CUtils.showSnackbar(containerLayout, getString(R.string.server_error), LoginSignUpActivity.this);
            }
        });
    }

    public Map<String, String> getParams(String mobileNo) {

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(LoginSignUpActivity.this));
        headers.put(CGlobalVariables.PHONE_NO, mobileNo);
        headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(LoginSignUpActivity.this));
        headers.put(CGlobalVariables.DEVICE_ID, CUtils.getMyAndroidId(LoginSignUpActivity.this));
        headers.put(CGlobalVariables.REG_SOURCE, CGlobalVariables.APP_SOURCE);
        headers.put(CGlobalVariables.APP_VERSION, "" + BuildConfig.VERSION_NAME);
        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(LoginSignUpActivity.this));
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
        headers.put(CGlobalVariables.OPERATION_NAME, com.ojassoft.astrosage.utils.CGlobalVariables.OPERATION_NAME_SIGNUP);
        headers.put("rt", String.valueOf(CUtils.getTimeFromMillis()));

        //Log.d("LoginFlow", " login: " + headers);
        return headers;
    }

    private void checkCacheCountryListData() {
        String url = CGlobalVariables.COUNTRY_LIST_URL;
        Cache cache = VolleySingleton.getInstance(LoginSignUpActivity.this).getRequestQueue().getCache();
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

        if (!CUtils.isConnectedWithInternet(LoginSignUpActivity.this)) {
            CUtils.showSnackbar(containerLayout, getResources().getString(R.string.no_internet), LoginSignUpActivity.this);
        } else {
            if (isShowProgressBar) {
                if (pd == null)
                    pd = new CustomProgressDialog(LoginSignUpActivity.this);
                pd.show();
                pd.setCancelable(false);
                isShowProgressBar = false;
            }
//            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.COUNTRY_LIST_URL,
//                    LoginSignUpActivity.this, false, getParamsCountryList(), 2).getMyStringRequest();
//            stringRequest.setShouldCache(true);
//            queue.add(stringRequest);
            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.getCountryList(getParamsCountryList());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    hideProgressBar();
                    if(response.body()!=null){
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
            AstrosageKundliApplication.isPrefetchServiceRunning = true;
            Intent intentService = new Intent(LoginSignUpActivity.this, PreFetchDataservice.class);
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

            CUtils.saveUserSelectedProfileInPreference(LoginSignUpActivity.this, userProfileDataBean);
            CUtils.saveProfileForChatInPreference(LoginSignUpActivity.this, userProfileDataBean);

        } catch (JSONException e) {
            e.printStackTrace();
            userProfileDataBean = null;
        }

        return userProfileDataBean;
    }

    public Map<String, String> getParamsCountryList() {

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(LoginSignUpActivity.this));
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

//            if (method == TRUECALLERSDKAPI) {
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    String status = jsonObject.getString("status");
//
//                    if (status.equals(CGlobalVariables.OTP_VERIFIED_AND_RETURN_USERDATA)) {
//                        CUtils.saveBannerData("");
//                        startPrefatchDataService();
//                        CUtils.startFollowerSubscriptionService(this);
//
//                        com.ojassoft.astrosage.varta.utils.CUtils.unSubscribeTopics("", com.ojassoft.astrosage.varta.utils.CGlobalVariables.TOPIC_NOT_LOGGEDIN, this);
//                        com.ojassoft.astrosage.varta.utils.CUtils.subscribeTopics("", com.ojassoft.astrosage.varta.utils.CGlobalVariables.TOPIC_LOGGEDIN, this);
//
//                        UserProfileData userProfileDataBean = parseJsonObject(response);
//                        CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_SIGNUP_LOGIN_SUCCESS_TRUE_CALLER, CGlobalVariables.FIREBASE_EVENT_SIGNUP_LOGIN, "");
//                        //save password in preference
//                        CUtils.setUserLoginPassword(LoginSignUpActivity.this, jsonObject.getString(KEY_PASSWORD));
//                        CUtils.setUserOffers(this, userProfileDataBean.isLiveintrooffer(), userProfileDataBean.getPrivateintrooffertype());
//
//                        String bonusStatus = "0", isShowOneRsPopup = "0", amountOnPopup = "0", countryCodeTrueCaller = "", phoneno = "";
//                        if (jsonObject.has("eligibleforsignupbonus")) {
//                            bonusStatus = jsonObject.getString("eligibleforsignupbonus");
//                        }
//                        if (jsonObject.has("showonerepopup")) {
//                            isShowOneRsPopup = jsonObject.getString("showonerepopup");
//                            CUtils.setDataForOneRsDialog(LoginSignUpActivity.this, isShowOneRsPopup);
//                        }
//                        if (jsonObject.has("amountonpopup")) {
//                            amountOnPopup = jsonObject.getString("amountonpopup");
//                            CUtils.setAmountOnDialog(LoginSignUpActivity.this, amountOnPopup);
//                        }
//                        if (jsonObject.has("countrycode")) {
//                            countryCodeTrueCaller = jsonObject.getString("countrycode");
//                            CUtils.setCountryCode(LoginSignUpActivity.this, countryCodeTrueCaller);
//                        }
//                        if (jsonObject.has("phoneno")) {
//                            phoneno = jsonObject.getString("phoneno");
//                        }
//                        mobileNumber = phoneno;
//                        CUtils.saveLoginDetailInPrefs(LoginSignUpActivity.this, phoneno, true, userProfileDataBean.getWalletbalance(), bonusStatus);
//
//                        if (jsonObject.has("blockedby")) {
//                            try {
//                                String blockedby = jsonObject.getString("blockedby");
//                                String[] blockByAstrologer = blockedby.split("\\s*,\\s*");
//                                CUtils.setblockByAstrologerList(blockByAstrologer);
//                            } catch (Exception e) {
//                            }
//                        }
//                        if (jsonObject.has("userid")) {
//                            CUtils.setUserIdForBlock(this, jsonObject.getString("userid"));
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
//                            boolean isNotificationShown = CUtils.getBooleanData(LoginSignUpActivity.this, CGlobalVariables.IS_49_ADDED_NOTIF_SHOWN, false);
//                            if (!isNotificationShown) {
//                                CUtils.saveBooleanData(LoginSignUpActivity.this, CGlobalVariables.IS_49_ADDED_NOTIF_SHOWN, true);
//                                CreateCustomLocalNotification notification = new CreateCustomLocalNotification(this);
//                                notification.showLocalNotification(bonustitle, bonusdescription, true);
//                            }
//                        }
//
//                        handleAstroSageLogin(jsonObject);
//                        redirectToNextActivity(userProfileDataBean);
//                    } else {
//                        CUtils.showSnackbar(containerLayout, getResources().getString(R.string.something_wrong_error), LoginSignUpActivity.this);
//                    }
//                    CUtils.saveAstroList("");
//                } catch (Exception e) {
//                    Log.i("mTrueCallerLogs", "response method 3 exception-->" + e);
//                    e.printStackTrace();
//                    CUtils.showSnackbar(containerLayout, getResources().getString(R.string.something_wrong_error), LoginSignUpActivity.this);
//                }
//            } else {
//                if (method == 1) {
//                    timeDifference = System.currentTimeMillis() - timeDifference;
//                    //Toast.makeText(loginSignupAct, "onResponse="+timeDifference, Toast.LENGTH_SHORT).show();
//                }
//                try {
//                    isClickGetOtpBtn = false;
//                    JSONObject jsonObject = new JSONObject(response);
//                    String status = jsonObject.getString("status");
//
//                    String errorCode = getResources().getString(R.string.error_code).replace("#", status);
//
//                    if (status.equals(CGlobalVariables.USER_ALREADY_EXIST_OTP_SEND) ||
//                            status.equals(CGlobalVariables.NEW_USER_OTP_SEND)) {
//                        String newUSer = "0";
//                        if (status.equals(CGlobalVariables.NEW_USER_OTP_SEND)) {
//                            newUSer = status;
//                        }
//                        try {
//                            JSONArray jsonArray = jsonObject.getJSONArray("as");
//                            JSONObject jsonObjectAS = jsonArray.getJSONObject(0);
//                            astroSageUserId = jsonObjectAS.getString("userid");
//                        } catch (Exception e) {
//                            //
//                        }
//                        String msg = jsonObject.optString("msg");
//                        Intent i = new Intent(LoginSignUpActivity.this, OtpVerifyActivity.class);
//                        i.putExtra(CGlobalVariables.IS_FROM_SCREEN, isFromScreen);
//                        i.putExtra(CGlobalVariables.NEW_USER, newUSer);
//                        i.putExtra(CGlobalVariables.PHONE_NO, mobileNumber);
//                        i.putExtra(KEY_USER_ID, astroSageUserId);
//                        i.putExtra(CGlobalVariables.MESSAGES_FBD_KEY, msg);
//                        startActivity(i);
//                    } else if (status.equals(CGlobalVariables.USER_OTP_MAX_LIMIT)) {
//                        String msg = jsonObject.getString("msg");
//                        CUtils.showSnackbar(containerLayout, msg + " " + errorCode, LoginSignUpActivity.this);
//                    } else if (status.equals("5")) {
//                        String message = getString(R.string.your_number_has_been_blocked);
//                        CUtils.showSnackbar(containerLayout, message + " " + errorCode, LoginSignUpActivity.this);
//                    } else {
//                        CUtils.showSnackbar(containerLayout, getResources().getString(R.string.invalid_mobie_no) + " " + errorCode, LoginSignUpActivity.this);
//                    }
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
        } else {
            CUtils.showSnackbar(containerLayout, getResources().getString(R.string.server_error), LoginSignUpActivity.this);
        }

        Log.e("LOGIN DATA ",response);


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
            //Auth.CredentialsApi.request(mGoogleApiClient, mCredentialRequest).setResultCallback(this);
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
                                    selectedMobNum = Identity.getSignInClient(LoginSignUpActivity.this).getPhoneNumberFromIntent(result.getData());
                                    if (selectedMobNum.length() > 10) {
                                        selectedMobNum = selectedMobNum.substring(selectedMobNum.length() - 10);
                                    }
                                    CUtils.saveAstroshopUserEmail(LoginSignUpActivity.this, selectedMobNum);
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
        callbackManager.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TcSdk.SHARE_PROFILE_REQUEST_CODE) {
            try {
                TcSdk.getInstance().onActivityResultObtained(this, requestCode, resultCode, data);
            } catch (Exception e) {
                try {
                    TcSdk.init(tcSdkOptions);
                    TcSdk.getInstance().onActivityResultObtained(this, requestCode, resultCode, data);
                } catch (Exception e1) {
                    CUtils.showSnackbar(containerLayout, e1.toString(), LoginSignUpActivity.this);
                }
            }

        } else if (requestCode == RC_HINT_EMAIL) {
            isEmailDialogueOpened = true;
            setFocusOnEmail();
            if (resultCode == RESULT_OK) {
                //Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
               // selectedMobNum = credential.getId();
                if (selectedMobNum.length() > 10) {
                    selectedMobNum = selectedMobNum.substring(selectedMobNum.length() - 10);
                }
                CUtils.saveAstroshopUserEmail(LoginSignUpActivity.this, selectedMobNum);
                mobileNumberTxt.setText(selectedMobNum);
            }
        } else if (requestCode == SUB_ACTIVITY_USER_LOGIN && resultCode == RESULT_OK) {
            CUtils.saveBooleanData(LoginSignUpActivity.this, CGlobalVariables.PREF_FIRST_INSTALL_APP_KEY, true);
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
        final Dialog dialog = new Dialog(LoginSignUpActivity.this);
        dialog.setCanceledOnTouchOutside(true);

        View view = getLayoutInflater().inflate(R.layout.lay_city_custompopup, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(view);
        ImageView imgViewSearch = view.findViewById(R.id.imgViewSearch);
        inputSearch = view.findViewById(R.id.edtcountry);
        strFilter = inputSearch.getText().toString();
        RecyclerView recyclerView = view.findViewById(R.id.listview);
        recyclerView.setLayoutManager(new LinearLayoutManager(LoginSignUpActivity.this, LinearLayoutManager.VERTICAL, false));
        adapter = new CountryListAdapter(LoginSignUpActivity.this, countryBeanList);
        recyclerView.setAdapter(adapter);

        imgViewSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                inputSearch.requestFocus();
            }
        });
        adapter.setOnClickListener(new RecyclerClickListner() {
            @Override
            public void onItemClick(int position, View v) {
                if (position != -1) {
                    CountryBean countryBean = countryBeanList.get(position);
                    //Log.e("COUNTRY ", "" + countryBean.getCountryName());
                    if (countryBean != null) {
                        String countryCode = countryBean.getCountryCode();
                        //Log.e("COUNTRY CODE ", "" + countryCode);
                      //  countryCodeText.setText(countryBean.getCountryName() + " (+" + countryCode + ")");
                        countryCodeText.setText("+" +countryCode);
                        CUtils.setCountryCode(LoginSignUpActivity.this, countryCode);
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
                CUtils.showSnackbar(containerLayout, getResources().getString(R.string.sign_in_success), LoginSignUpActivity.this);
            }

            HashMap<String, String> jsonObjHash = com.ojassoft.astrosage.utils.CUtils.parseLoginSignupJson(respObj);
            if (jsonObjHash.size() > 0) {
                if (jsonObjHash.containsKey(com.ojassoft.astrosage.utils.CGlobalVariables.USERID)) {
                    _userId = jsonObjHash.get(com.ojassoft.astrosage.utils.CGlobalVariables.USERID);
                }
                if (jsonObjHash.containsKey(com.ojassoft.astrosage.utils.CGlobalVariables.USER_PASSWORD)) {
                    _pwd = jsonObjHash.get(com.ojassoft.astrosage.utils.CGlobalVariables.USER_PASSWORD);
                }
                CUtils.saveInformation(LoginSignUpActivity.this, _userId, _pwd, jsonObjHash);
            }
            //Log.d("LoginFlow", " handleAstroSageLogin()2");
        } catch (Exception e) {
            Log.i("mTrueCallerLogs", "handleAstroSageLogin exception-->" + e);
        }
    }

    private void redirectToNextActivity(UserProfileData userProfileDataBean) {
        try {

            if (isFromScreen == null) isFromScreen = "";
            if (isFromScreen.equals(CGlobalVariables.PAYMENT_INFO_SCREEN)) {
                Log.i("mTrueCallerLogs", "redirectToNextActivity from PAYMENT_INFO_SCREEN");
                setResult(RESULT_CANCELED);
                finish();
            } else if (isFromScreen.equals(CGlobalVariables.LANGUAGE_SELECTION_SCRREN)) {
                Log.i("mTrueCallerLogs", "redirectToNextActivity from LANGUAGE_SELECTION_SCRREN");
                com.ojassoft.astrosage.utils.CUtils.saveWizardShownFirstTime(LoginSignUpActivity.this);
                startActivity(new Intent(LoginSignUpActivity.this, ActAppModule.class));
                finish();
            } else if (isFromScreen.equals(CGlobalVariables.BASE_INPUT_SCREEN) ||
                    isFromScreen.equals(CGlobalVariables.LOGOUT_BTN)) {
                if (isFromScreen.equals(CGlobalVariables.BASE_INPUT_SCREEN)) {
                    Log.i("mTrueCallerLogs", "redirectToNextActivity from BASE_INPUT_SCREEN");
                } else {
                    Log.i("mTrueCallerLogs", "redirectToNextActivity from LOGOUT_BTN");
                }
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putString("LOGIN_NAME", _userId);
                bundle.putString("LOGIN_PWD", _pwd);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                finish();
            } else {
                Log.i("mTrueCallerLogs", "redirectToNextActivity from else");
                Intent i = new Intent(LoginSignUpActivity.this, DashBoardActivity.class);
                i.putExtra(CGlobalVariables.USER_DATA, userProfileDataBean);
                i.putExtra(CGlobalVariables.IS_FROM_SCREEN, isFromScreen);
                i.putExtra(KEY_FILTER_TYPE, CGlobalVariables.FILTER_TYPE_CHAT);
                startActivity(i);
                finish();
            }
        } catch (Exception e) {
            Log.i("mTrueCallerLogs", "redirectToNextActivity exception-->" + e);
        }
    }

//    @Override
//    public void onFailure(@NonNull TcOAuthError tcOAuthError) {
////        Log.i("testTrueCaller", "tcOAuthError.getErrorMessage==>>" + tcOAuthError.getErrorMessage());
////        Log.i("testTrueCaller", "tcOAuthError.getErrorCode==>>" + tcOAuthError.getErrorCode());
//       // finish();
//
//        //Toast.makeText(this, "Login failed. Please try another method.", Toast.LENGTH_LONG).show();
//        TcSdk.clear();
//       //onBackPressed();
//        //getOnBackPressedDispatcher().onBackPressed(); alternative of on Backpress
//    }
//
//    @Override
//    public void onSuccess(@NonNull TcOAuthData tcOAuthData) {
//       // Log.i("testTrueCaller","tcOAuthData.getAuthorizationCode==>>"+tcOAuthData.getAuthorizationCode());
//       // Log.i("testTrueCaller","tcOAuthData.getState==>>"+tcOAuthData.getState());
//       // Log.i("testTrueCaller","tcOAuthData.getScopesGranted==>>"+tcOAuthData.getScopesGranted().size());
////        for (int i = 0; i < tcOAuthData.getScopesGranted().size(); i++) {
////            Log.i("testTrueCaller","tcOAuthData.getScopesGranted==>>"+tcOAuthData.getScopesGranted().get(i));
////        }
//        sendRequestToTrueCaller(tcOAuthData);
//    }
//
//    @Override
//    public void onVerificationRequired(@Nullable TcOAuthError tcOAuthError) {
//        //Log.i("testTrueCaller","called onVerificationRequired"+tcOAuthError);
//    }

    private void sendRequestToTrueCaller(TcOAuthData tcOAuthData) {
        CUtils.hideMyKeyboard(this);
        if (!CUtils.isConnectedWithInternet(LoginSignUpActivity.this)) {
            CUtils.showSnackbar(containerLayout, getResources().getString(R.string.no_internet), LoginSignUpActivity.this);
        } else {
            if (pd == null)
                pd = new CustomProgressDialog(LoginSignUpActivity.this);
            pd.show();

//            StringRequest stpd.setCancelable(false);ringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.TrueCallerRegisterV3,
//                    LoginSignUpActivity.this, false, getParamsNumberVerify(tcOAuthData), TRUECALLERSDKAPI).getMyStringRequest();
//            stringRequest.setShouldCache(false);
//            queue.add(stringRequest);
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
                    Log.e("testTrueCaller","jsonObject==>>"+jsonObject);
                    String status = jsonObject.getString("status");
                  //  Log.i("testTrueCaller","status==>>"+status);
                    if (status.equals(CGlobalVariables.OTP_VERIFIED_AND_RETURN_USERDATA)) {
                        CUtils.saveBannerData("");
                        startPrefatchDataService();
                        CUtils.startFollowerSubscriptionService(LoginSignUpActivity.this);

                        com.ojassoft.astrosage.varta.utils.CUtils.unSubscribeTopics("", com.ojassoft.astrosage.varta.utils.CGlobalVariables.TOPIC_NOT_LOGGEDIN, LoginSignUpActivity.this);
                        com.ojassoft.astrosage.varta.utils.CUtils.subscribeTopics("", com.ojassoft.astrosage.varta.utils.CGlobalVariables.TOPIC_LOGGEDIN, LoginSignUpActivity.this);

                        BeanHoroPersonalInfo beanHoroPersonalInfo = com.ojassoft.astrosage.utils.CUtils.getBeanHoroscopeInfoObject(jsonObject);
                        com.ojassoft.astrosage.utils.CUtils.saveDefaultKundliIfAvailable(LoginSignUpActivity.this,beanHoroPersonalInfo);


                        UserProfileData userProfileDataBean = parseJsonObject(myResponse);
                        if(userProfileDataBean.isIsnewuser()){
                            CUtils.addFacebookAndFirebaseEvent(AppEventsConstants.EVENT_NAME_COMPLETED_REGISTRATION, "new_register", "TrueCaller");
                        }
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_SIGNUP_LOGIN_SUCCESS_TRUE_CALLER, CGlobalVariables.FIREBASE_EVENT_SIGNUP_LOGIN, "");
                        //save password in preference
                        CUtils.setUserLoginPassword(LoginSignUpActivity.this, jsonObject.getString(KEY_PASSWORD));
                        CUtils.userOfferAfterLogin = userProfileDataBean.getPrivateintrooffertype();
                        CUtils.setUserOffers(LoginSignUpActivity.this, userProfileDataBean.isLiveintrooffer(), userProfileDataBean.getPrivateintrooffertype());
                        CUtils.setSecondFreeChat(LoginSignUpActivity.this, userProfileDataBean.isSecondFreeChat());
                        // get and set user unique refer code
                        String bonusStatus = "0", isShowOneRsPopup = "0", amountOnPopup = "0", countryCodeTrueCaller = "", phoneno = "";
                        if (jsonObject.has("eligibleforsignupbonus")) {
                            bonusStatus = jsonObject.getString("eligibleforsignupbonus");
                        }
                        if (jsonObject.has("showonerepopup")) {
                            isShowOneRsPopup = jsonObject.getString("showonerepopup");
                            CUtils.setDataForOneRsDialog(LoginSignUpActivity.this, isShowOneRsPopup);
                        }
                        if (jsonObject.has("amountonpopup")) {
                            amountOnPopup = jsonObject.getString("amountonpopup");
                            CUtils.setAmountOnDialog(LoginSignUpActivity.this, amountOnPopup);
                        }
                        if (jsonObject.has("countrycode")) {
                            countryCodeTrueCaller = jsonObject.getString("countrycode");
                            CUtils.setCountryCode(LoginSignUpActivity.this, countryCodeTrueCaller);
                        }
                        if (jsonObject.has("phoneno")) {
                            phoneno = jsonObject.getString("phoneno");
                        }
                        mobileNumber = phoneno;
                        CUtils.saveLoginDetailInPrefs(LoginSignUpActivity.this, phoneno, true, userProfileDataBean.getWalletbalance(), bonusStatus);
//                        if(!TextUtils.isEmpty(userProfileDataBean.getPrivateintrooffertype()) && userProfileDataBean.getPrivateintrooffertype().equals(CGlobalVariables.INTRO_OFFER_TYPE_FREE)){
//                            CUtils.saveAiRandomChatAstroDeatils("");
//                            CUtils.getAiRandomChatAstroDetail(LoginSignUpActivity.this);
//                        }

                        if (jsonObject.has("blockedby")) {
                            try {
                                String blockedby = jsonObject.getString("blockedby");
                                String[] blockByAstrologer = blockedby.split("\\s*,\\s*");
                                CUtils.setblockByAstrologerList(blockByAstrologer);
                            } catch (Exception e) {
                            }
                        }
                        if (jsonObject.has("userid")) {
                            CUtils.setUserIdForBlock(LoginSignUpActivity.this, jsonObject.getString("userid"));
                        }

                        String bonustitle = "", bonusdescription = "";
                        if (jsonObject.has("bonustitle")) {
                            bonustitle = jsonObject.getString("bonustitle");
                        }
                        if (jsonObject.has("bonusdescription")) {
                            bonusdescription = jsonObject.getString("bonusdescription");
                        }

                        if (!TextUtils.isEmpty(bonustitle) && !TextUtils.isEmpty(bonusdescription)) {
                            boolean isNotificationShown = CUtils.getBooleanData(LoginSignUpActivity.this, CGlobalVariables.IS_49_ADDED_NOTIF_SHOWN, false);
                            if (!isNotificationShown) {
                                CUtils.saveBooleanData(LoginSignUpActivity.this, CGlobalVariables.IS_49_ADDED_NOTIF_SHOWN, true);
                                CreateCustomLocalNotification notification = new CreateCustomLocalNotification(LoginSignUpActivity.this);
                                notification.showLocalNotification(bonustitle, bonusdescription, true);
                            }
                        }

                        handleAstroSageLogin(jsonObject);
                        if (pd != null) {
                            pd.dismiss();
                        }
                        redirectToNextActivity(userProfileDataBean);
                    } else {
                        CUtils.showSnackbar(containerLayout, getResources().getString(R.string.truecaller_login_error), LoginSignUpActivity.this);
                        if (pd != null) {
                            pd.dismiss();
                        }
                    }
                    CUtils.startToGetAiPassSubService(LoginSignUpActivity.this);
                    CUtils.saveAstroList("");
                } catch (Exception e) {
                    Log.i("testTrueCaller", "response method 3 exception-->" + e);
                    e.printStackTrace();
                    CUtils.showSnackbar(containerLayout, getResources().getString(R.string.truecaller_login_error), LoginSignUpActivity.this);
                    if (pd != null){
                        pd.dismiss();
                    }
                }            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
               // Log.i("testTrueCaller","t==>>"+t);
              //onBackPressed();
                if (pd != null){
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
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(LoginSignUpActivity.this));

        headers.put(CGlobalVariables.DEVICE_ID, CUtils.getMyAndroidId(LoginSignUpActivity.this));
        headers.put(CGlobalVariables.REG_SOURCE, CGlobalVariables.APP_SOURCE);
        headers.put(CGlobalVariables.APP_VERSION, "" + BuildConfig.VERSION_NAME);
        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(LoginSignUpActivity.this));


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

        String installedReferredCode = CUtils.getStringData(LoginSignUpActivity.this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_REFERRER_URL, "");
        Log.d("InstallRef","installedReferredCode: uniquereferredcode==>>  "+installedReferredCode);
        if(CUtils.isReferralCodeValid(installedReferredCode)){
            headers.put("uniquereferredcode", installedReferredCode);
        }
        //Log.d("testTrueCaller","headers==>"+headers);
        //Log.d("LoginFlow", " truecaller: " + headers);

        return headers;
    }
}
