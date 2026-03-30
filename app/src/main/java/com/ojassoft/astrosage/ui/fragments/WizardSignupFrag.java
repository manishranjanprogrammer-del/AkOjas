package com.ojassoft.astrosage.ui.fragments;

import static com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.LinkMovementMethod;
import android.text.method.PasswordTransformationMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.CustomCredential;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.common.SignInButton;
import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.libojassoft.android.misc.VolleyResponse;
import com.libojassoft.android.misc.VolleyServiceHandler;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.controller.ControllerManager;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.model.GmailAccountInfo;
import com.ojassoft.astrosage.notification.ActShowOjasSoftArticles;
import com.ojassoft.astrosage.ui.act.ActDisclaimerAndPrivacyPolicy;
import com.ojassoft.astrosage.ui.act.ActivityLoginAndSignin;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;

/**
 * Created by ojas-08 on 14/6/16.
 */
public class WizardSignupFrag extends Fragment implements
        VolleyResponse, View.OnClickListener {
    Activity activity;
    EditText edtMailId;
    EditText edtPassword;
    Button btnSignUp;
    TextInputLayout textInputEmail, textInputPass;
    String emailId = "";
    String password = "";
    CustomProgressDialog pd = null;
    CheckBox showPasswordCheckbox;
    Spinner spinnerScreenList;
    TextView textViewDisclaimer, textViewPrivacyPolicy, headingTextView, agreeTextView, andTextView, disclaimerTextView, privacyTextView;
    LinearLayout layout1, layout2, mainContainer;
    //AsyncTaskRegistrationOfUser asyncTaskRegistrationOfUser;
    private IUserAlreadyRegistered iUserAlreadyRegistered;
    private String key;
    private RequestQueue queue;
    int USER_REGISTRATION = 1;
    int SIGNUP_WITH_ONLY_EMAILID = 0;
    private CredentialManager credentialManager;
    private FirebaseAuth mAuth;
    private boolean isEmailDialogueOpened;
    private String selectedEmailId, userName = "", operationName = "";

    SignInButton googleSignInButton;
    LoginButton facebookLoginButton;
    CallbackManager callbackManager;
    RelativeLayout fbGoogleLayout;
    LinearLayout signUpLayout, facebookButton, googleButton, emailButton;
    TextView welcomeText, chartSignupText, signinText, privacyText, haveAccountText;
    WizardLogingRegister.IWizardLoginRegisterFragmentInterface iWizardLoginRegisterFragmentInterface;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        iWizardLoginRegisterFragmentInterface = (WizardLogingRegister.IWizardLoginRegisterFragmentInterface) activity;
        iUserAlreadyRegistered = (IUserAlreadyRegistered) activity;
        this.activity = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wizard_signup_frag_layout, null);
        getIdOfAllView(view);
        queue = VolleySingleton.getInstance(activity).getRequestQueue();
        setTypefaceOfView(activity);

        layout1.setVisibility(View.GONE);
        layout2.setVisibility(View.VISIBLE);

        key = CUtils.getApplicationSignatureHashCode(activity);

        showPasswordCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    if (!isChecked) {
                        // show password
                        edtPassword.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        edtPassword.setSelection(edtPassword.getText().length());
                    } else {
                        // hide password
                        edtPassword.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        edtPassword.setSelection(edtPassword.getText().length());
                    }
                } catch (Exception ex) {
                    //
                }
            }
        });

        btnSignUp.setOnClickListener(this);
        edtMailId.setOnClickListener(this);
        textViewDisclaimer.setOnClickListener(this);
        textViewPrivacyPolicy.setOnClickListener(this);
        setSpannableString();

        fbGoogleLayout = view.findViewById(R.id.fb_google_layout);
        signUpLayout = view.findViewById(R.id.signup_layout);

        fbGoogleLayout.setVisibility(View.VISIBLE);
        signUpLayout.setVisibility(View.GONE);

        facebookButton = view.findViewById(R.id.facebook_button);
        googleButton = view.findViewById(R.id.google_button);
        emailButton = view.findViewById(R.id.email_button);
        welcomeText = view.findViewById(R.id.welcome_text);
        chartSignupText = view.findViewById(R.id.chart_signup_text);
        signinText = view.findViewById(R.id.signin_text);
        privacyText = view.findViewById(R.id.privacy_text);
        haveAccountText = view.findViewById(R.id.have_account_text);

        haveAccountText.setText(getActivity().getResources().getString(R.string.already_account));
        welcomeText.setVisibility(View.GONE);
        chartSignupText.setVisibility(View.VISIBLE);
        privacyText.setVisibility(View.VISIBLE);
        signinText.setText(getActivity().getResources().getString(R.string.button_sign_in));

        signinText.setOnClickListener(this);
        facebookButton.setOnClickListener(this);
        googleButton.setOnClickListener(this);
        emailButton.setOnClickListener(this);
        setSpannableStringPrivacyText();

        boolean loggedOut = AccessToken.getCurrentAccessToken() == null;
        if (!loggedOut) {
            try {
                LoginManager.getInstance().logOut();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //Using Graph API
            //getUserProfile(AccessToken.getCurrentAccessToken());
        }
        callbackManager = CallbackManager.Factory.create();
        facebookLoginButton = view.findViewById(R.id.facebook_login_button);
        facebookLoginButton.setFragment(this);
        //facebookLoginButton.setReadPermissions(Arrays.asList("email", "public_profile"));
        facebookLoginButton.setPermissions(Arrays.asList("email", "public_profile", "user_friends"));

        // Callback registration
        facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                boolean loggedIn = AccessToken.getCurrentAccessToken() == null;
                Log.d("API123", loggedIn + " ??");
                getUserProfile(AccessToken.getCurrentAccessToken());
            }

            @Override
            public void onCancel() {
            }

            @Override
            public void onError(FacebookException exception) {
            }
        });

        googleSignInButton = view.findViewById(R.id.google_sign_in_button);
        credentialManager = CredentialManager.create(getActivity());
        mAuth = FirebaseAuth.getInstance();
        return view;
    }

    private void callViewsLayout() {
        setValueOfViews();
        setSpinnerData();

        if (getUserEmailId() != null && getUserEmailId().size() > 0) {
            layout1.setVisibility(View.VISIBLE);
            layout2.setVisibility(View.GONE);
        } else {
            layout1.setVisibility(View.GONE);
            layout2.setVisibility(View.VISIBLE);
        }
    }

    private void getIdOfAllView(View view) {
        if (activity == null || view == null) return;
        headingTextView = view.findViewById(R.id.signup_heading);
        agreeTextView = view.findViewById(R.id.agree_text);
        andTextView = view.findViewById(R.id.and_text);
        edtMailId = view.findViewById(R.id.edtMailId);
        edtPassword = view.findViewById(R.id.edtPassword);
        btnSignUp = view.findViewById(R.id.btnSignUp);
        textInputEmail = view.findViewById(R.id.input_layout_email);
        textInputPass = view.findViewById(R.id.input_layout_pass);
        showPasswordCheckbox = view.findViewById(R.id.show_password);
        spinnerScreenList = view.findViewById(R.id.spinnerScreenList);
        textViewPrivacyPolicy = view.findViewById(R.id.textViewPrivacyPolicy);
        textViewDisclaimer = view.findViewById(R.id.textViewDisclaimer);
        disclaimerTextView = view.findViewById(R.id.textViewDisclaimer);
        privacyTextView = view.findViewById(R.id.textViewPrivacyPolicy);

        mainContainer = view.findViewById(R.id.main_container);
        layout1 = view.findViewById(R.id.layout1);
        layout2 = view.findViewById(R.id.layout2);
    }

    private void setValueOfViews() {
        if (((BaseInputActivity) activity).LANGUAGE_CODE == CGlobalVariables.ENGLISH) {
            //btnSignUp.setText(getResources().getString(R.string.sign_up).toUpperCase());
            btnSignUp.setText("START USING ASTROSAGE".toUpperCase());
        }
        ArrayList<GmailAccountInfo> emailIdArrayList = getUserEmailId();
        if (emailIdArrayList != null && emailIdArrayList.size() > 0) {
            edtMailId.setText(emailIdArrayList.get(0).getId());
        }

    }

    private void setTypefaceOfView(Activity activity) {

        btnSignUp.setTypeface(((BaseInputActivity) activity).mediumTypeface);
        edtMailId.setTypeface(((BaseInputActivity) activity).robotRegularTypeface);
        edtPassword.setTypeface(((BaseInputActivity) activity).robotRegularTypeface);
        headingTextView.setTypeface(((BaseInputActivity) activity).regularTypeface);
        andTextView.setTypeface(((BaseInputActivity) activity).regularTypeface);
        disclaimerTextView.setTypeface(((BaseInputActivity) activity).regularTypeface);
        privacyTextView.setTypeface(((BaseInputActivity) activity).regularTypeface);
        agreeTextView.setTypeface(((BaseInputActivity) activity).regularTypeface);
        showPasswordCheckbox.setTypeface(((BaseInputActivity) activity).regularTypeface);
    }

    private void doActionOnSignupButton() {

        CUtils.googleAnalyticSendWitPlayServie(
                activity,
                CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                CGlobalVariables.GOOGLE_ANALYTIC_ACTION_ASTROSAGE_CLOUD_REGISTRATION,
                null);
        CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_ACTION_ASTROSAGE_CLOUD_REGISTRATION, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

        registerUser();
        CUtils.hideMyKeyboard(activity);
    }

    private void registerUser() {
        if (layout1.getVisibility() == View.VISIBLE) {
            emailId = ((GmailAccountInfo) spinnerScreenList.getSelectedItem()).getId();
            if (CUtils.isConnectedWithInternet(activity)) {
                //new signUpUsingOnlyEmailId().execute();

                /*emailId = "mom32@gmail.com";
                password="";*/
                // signUpUsingOnlyEmailId();
                operationName = CGlobalVariables.OPERATION_NAME_SIGNUP;
                userName = CUtils.getUserNameFromEmailID(emailId);
                CUtils.verifyLoginWithUserPurchasedPlan(WizardSignupFrag.this, emailId,
                        password, userName, CGlobalVariables.OPERATION_NAME_SIGNUP,
                        CGlobalVariables.REG_SOURCE_ANDROID, SIGNUP_WITH_ONLY_EMAILID);

            } else {
                MyCustomToast mct = new MyCustomToast(activity, activity.getLayoutInflater(), activity, ((BaseInputActivity) activity).regularTypeface);
                mct.show(getResources().getString(R.string.no_internet));
            }
        } else {
            emailId = edtMailId.getText().toString().trim();
            password = edtPassword.getText().toString().trim();
            if (validateData()) {
               /* asyncTaskRegistrationOfUser = new AsyncTaskRegistrationOfUser();
                asyncTaskRegistrationOfUser.execute();*/
                showProgressBar();
                // CUtils.userSignUp(WizardSignupFrag.this, emailId, password, USER_REGISTRATION);
                operationName = CGlobalVariables.OPERATION_NAME_SIGNUP;
                userName = CUtils.getUserNameFromEmailID(emailId);
                CUtils.verifyLoginWithUserPurchasedPlan(WizardSignupFrag.this, emailId,
                        password, userName, CGlobalVariables.OPERATION_NAME_SIGNUP,
                        CGlobalVariables.REG_SOURCE_ANDROID, USER_REGISTRATION);
            }
        }
    }

    private boolean validateData() {
        boolean flag = validateName(edtMailId, textInputEmail, getString(R.string.email_one_v)) && validateName(edtPassword, textInputPass, getString(R.string.enter_password));

        return flag;
    }

    private boolean validateName(EditText name, TextInputLayout inputLayout, String message) {
        //String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (name == edtMailId) {
            if (name.getText().toString().trim().length() < 1) {
                inputLayout.setError(message);
                inputLayout.setErrorEnabled(true);
                requestFocus(name);
                name.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                return false;
            } else if (name.getText().toString().trim().length() > 70) {
                inputLayout.setError(getResources().getString(R.string.email_two_v));
                inputLayout.setErrorEnabled(true);
                requestFocus(name);
                name.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                return false;
            } else if (!CUtils.isValidEmail(name.getText().toString().trim())) {
                inputLayout.setError(getResources().getString(R.string.email_three_v));
                inputLayout.setErrorEnabled(true);
                requestFocus(name);
                name.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                return false;
            } else {
                inputLayout.setErrorEnabled(false);
                inputLayout.setError(null);
                name.getBackground().setColorFilter(getResources().getColor(R.color.ColorPrimary), PorterDuff.Mode.SRC_ATOP);
            }
        } else if (name.getText().toString().trim().isEmpty()) {
            inputLayout.setError(message);
            inputLayout.setErrorEnabled(true);
            requestFocus(name);
            name.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
            return false;
        } else {
            inputLayout.setErrorEnabled(false);
            inputLayout.setError(null);
            name.getBackground().setColorFilter(getResources().getColor(R.color.ColorPrimary), PorterDuff.Mode.SRC_ATOP);
        }

        if (!CUtils.isConnectedWithInternet(activity)) {
            MyCustomToast mct = new MyCustomToast(activity, activity.getLayoutInflater(), activity, ((BaseInputActivity) activity).regularTypeface);
            mct.show(getResources().getString(R.string.no_internet));
            return false;
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        }
    }

    /******************************************* EmailId dialogue start***************************/
    public void requestCredentials() {
        if (!isEmailDialogueOpened) {
            showGmailAccountPicker();
        }
    }

    public void showGmailAccountPicker() {
        // Configure Google ID option
        GetGoogleIdOption googleIdOption = new GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false) // Show all accounts
                .setServerClientId(getString(R.string.default_web_client_id)) // Required
                .setAutoSelectEnabled(false) // Show account picker
                .build();

        // Build the request
        GetCredentialRequest request = new GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build();

        // Launch Credential Manager UI
        credentialManager.getCredentialAsync(
                getActivity(),
                request,
                new CancellationSignal(),
                Executors.newSingleThreadExecutor(),
                new CredentialManagerCallback() {
                    @Override
                    public void onResult(Object responseObj) {
                        isEmailDialogueOpened = true;
                        setFocusOnEmail();
                        handleSignIn((GetCredentialResponse) responseObj);
                    }

                    @Override
                    public void onError(@NonNull Object o) {
                        isEmailDialogueOpened = true;
                        setFocusOnEmail();
                    }
                }
        );
    }

    private void handleSignIn(GetCredentialResponse credentialResponse) {
        try {
            Credential credential = credentialResponse.getCredential();
            if (credential instanceof CustomCredential && credential.getType().equals(TYPE_GOOGLE_ID_TOKEN_CREDENTIAL)) {
                // Create Google ID Token
                Bundle credentialData = credential.getData();
                GoogleIdTokenCredential googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credentialData);
                selectedEmailId = googleIdTokenCredential.getId();
                activity.runOnUiThread(() -> {
                    try {
                        CUtils.saveAstroshopUserEmail(activity, selectedEmailId);
                        callViewsLayout();
                        startGcmService();
                    } catch (Exception e) {
                        //
                    }
                });
            }
        }catch (Exception e){
            //
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

       /* if (requestCode == RC_HINT_EMAIL) {
            try {
                isEmailDialogueOpened = true;
                setFocusOnEmail();
                if (resultCode == RESULT_OK) {
                    Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                    selectedEmailId = credential.getId();
                    CUtils.saveAstroshopUserEmail(activity, selectedEmailId);
                    callViewsLayout();
                    startGcmService();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else
        if (requestCode == RC_SIGN_IN) {       // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
            // The Task returned from this call is always completed, no need to attach a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }else {
            callbackManager.onActivityResult(requestCode, resultCode, data);
        }*/
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void setFocusOnEmail() {
        try {
            activity.runOnUiThread(() -> {
                edtMailId.setFocusableInTouchMode(true);
                edtMailId.setFocusable(true);
                edtMailId.requestFocus();
            });
        } catch (Exception e){
            //
        }
    }

    private void startGcmService() {
        if (getActivity() == null) return;
        getActivity().startService(new Intent(getActivity(), com.ojassoft.astrosage.custompushnotification.MyCloudRegistrationService.class));//ADDED BY DEEPAK ON 21-11-2014
    }

    /******************************************* EmailId dialogue end***************************/
    private void showCustomMsg(String msg) {
        try {
            if (activity != null) {
                MyCustomToast mct = new MyCustomToast(activity,
                        activity.getLayoutInflater(), activity,
                        ((BaseInputActivity) activity).regularTypeface);
                mct.show(msg);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void saveSignUpInformation(Activity activity, String userLogin, String pwd,
                                       HashMap<String, String> profileInfo, String operatnName) {
        String userName = "", userPlanExpiryDate = "",
                userPlanPurchaseDate = "", userPlanId = "1", mobile = "", occupation = "", maritalStatus = "",
                companyName = "", address1 = "", address2 = "";

        if (profileInfo.containsKey(CGlobalVariables.USERID)) {
            userLogin = profileInfo.get(CGlobalVariables.USERID);
        }

        if (profileInfo.containsKey(CGlobalVariables.USER_FIRSTNAME)) {
            userName = profileInfo.get(CGlobalVariables.USER_FIRSTNAME);
        }

        if (profileInfo.containsKey(CGlobalVariables.USERPLAN_ID)) ;
        {
            if (profileInfo.get(CGlobalVariables.USERPLAN_ID).length() > 0) {
                userPlanId = profileInfo.get(CGlobalVariables.USERPLAN_ID);
            }
        }
        if (profileInfo.containsKey(CGlobalVariables.USER_PLAN_EXPIRY_DATE)) ;
        {
            userPlanExpiryDate = profileInfo.get(CGlobalVariables.USER_PLAN_EXPIRY_DATE);
        }
        if (profileInfo.containsKey(CGlobalVariables.USER_PLAN_PURCHASE_DATE)) ;
        {
            userPlanPurchaseDate = profileInfo.get(CGlobalVariables.USER_PLAN_PURCHASE_DATE);
        }

        GmailAccountInfo gmailAccountInfo = new GmailAccountInfo();
        if (operatnName.equalsIgnoreCase(CGlobalVariables.OPERATION_NAME_SOCIALMEDIA)) {
            if (profileInfo.containsKey(CGlobalVariables.MOBILE)) {
                mobile = profileInfo.get(CGlobalVariables.MOBILE);
            }
            if (profileInfo.containsKey(CGlobalVariables.OCCUPATION)) {
                occupation = profileInfo.get(CGlobalVariables.OCCUPATION);
            }
            if (profileInfo.containsKey(CGlobalVariables.MARITALSTATUS)) {
                maritalStatus = profileInfo.get(CGlobalVariables.MARITALSTATUS);
            }
            if (profileInfo.containsKey(CGlobalVariables.COMPANY_NAME)) ;
            {
                companyName = profileInfo.get(CGlobalVariables.COMPANY_NAME);
            }
            if (profileInfo.containsKey(CGlobalVariables.ADDRESS1)) ;
            {
                address1 = profileInfo.get(CGlobalVariables.ADDRESS1);
            }
            if (profileInfo.containsKey(CGlobalVariables.ADDRESS2)) ;
            {
                address2 = profileInfo.get(CGlobalVariables.ADDRESS2);
            }

            gmailAccountInfo.setMobileNo(mobile);
            if (!TextUtils.isEmpty(occupation)) {
                gmailAccountInfo.setOccupation(Integer.parseInt(occupation));
            } else {
                gmailAccountInfo.setOccupation(0);
            }
            if (!TextUtils.isEmpty(maritalStatus)) {
                gmailAccountInfo.setMaritalStatus(Integer.parseInt(maritalStatus));
            } else {
                gmailAccountInfo.setMaritalStatus(0);
            }

            gmailAccountInfo.setHeading(companyName);
            gmailAccountInfo.setAddress1(address1);
            gmailAccountInfo.setAddress2(address2);
        }

        CUtils.saveLoginDetailInPrefs(activity, userLogin, pwd, userName, true, false);
        // Clear old userid and password from old app
        SharedPreferences settings = activity.getSharedPreferences(
                CGlobalVariables.MYKUNDLI_PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(CGlobalVariables.PREF_USER_ID, "");
        editor.putString(CGlobalVariables.PREF_USERR_PWD, "");
        editor.commit();

        gmailAccountInfo.setId(userLogin);
        if (userName.equals("")) {

            gmailAccountInfo.setUserName(userLogin.split("@")[0]);
        } else {
            gmailAccountInfo.setUserName(userName);
        }
        CUtils.saveGmailAccountInfo(activity, gmailAccountInfo);


        CUtils.storeUserPurchasedPlanInPreference(activity, Integer.parseInt(userPlanId));

        CUtils.saveStringData(activity, CGlobalVariables.PREF_USER_PLAN_PURCHASE_DATE, userPlanPurchaseDate);//Purchase plan Date
        CUtils.saveStringData(activity, CGlobalVariables.PREF_USER_PLAN_Expiry_DATE, userPlanExpiryDate);//Expiry plan  Date
       /* iWizardLoginRegisterFragmentInterface.clickedSignInButton(userLogin,
                pwd);*/
        ((ActivityLoginAndSignin) activity).goToHome();
        // ADDED BY BIJENDRA ON 26-12-14 TO SAVE GCM REGISTRATION INFORMATION ON
        // SERVER
        int languageCode = ((AstrosageKundliApplication) activity
                .getApplication()).getLanguageCode();
        String regid = CUtils.getRegistrationId(activity
                .getApplicationContext());
        new ControllerManager().saveUserGCMRegistrationInformationOnOjasServer(
                activity.getApplicationContext(), regid, languageCode,
                userLogin);

    }

    private void saveSignUpInformation(Activity activity, String[] iReturn, String userLogin, String pwd, String userName) {

        CUtils.saveLoginDetailInPrefs(activity, userLogin, pwd, userName, true, false);
        GmailAccountInfo gmailAccountInfo = new GmailAccountInfo();
        gmailAccountInfo.setId(userLogin);
        gmailAccountInfo.setUserName(userLogin.split("@")[0]);
        CUtils.saveGmailAccountInfo(activity, gmailAccountInfo);
        // Clear old userid and password from old app
        SharedPreferences settings = activity.getSharedPreferences(
                CGlobalVariables.MYKUNDLI_PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(CGlobalVariables.PREF_USER_ID, "");
        editor.putString(CGlobalVariables.PREF_USERR_PWD, "");
        editor.commit();

        CUtils.storeUserPurchasedPlanInPreference(activity, Integer.parseInt(iReturn[1]));

        CUtils.saveStringData(activity, CGlobalVariables.PREF_USER_PLAN_PURCHASE_DATE, iReturn[2]);//Purchase plan Date
        CUtils.saveStringData(activity, CGlobalVariables.PREF_USER_PLAN_Expiry_DATE, iReturn[3]);//Expiry plan  Date
       /* iWizardLoginRegisterFragmentInterface.clickedSignInButton(userLogin,
                pwd);*/
        ((ActivityLoginAndSignin) activity).goToHome();
        // ADDED BY BIJENDRA ON 26-12-14 TO SAVE GCM REGISTRATION INFORMATION ON
        // SERVER
        int languageCode = ((AstrosageKundliApplication) activity
                .getApplication()).getLanguageCode();
        String regid = CUtils.getRegistrationId(activity
                .getApplicationContext());
        new ControllerManager().saveUserGCMRegistrationInformationOnOjasServer(
                activity.getApplicationContext(), regid, languageCode,
                userLogin);

    }

    private ArrayList<GmailAccountInfo> getUserEmailId() {
        ArrayList<GmailAccountInfo> emailId = new ArrayList<GmailAccountInfo>();
        if (!TextUtils.isEmpty(selectedEmailId)) {
            GmailAccountInfo gmailAccountInfo = new GmailAccountInfo();
            gmailAccountInfo.setId(selectedEmailId);
            emailId.add(gmailAccountInfo);
        }

        return emailId;
    }

    @Override
    public void onResume() {
        super.onResume();
        textInputEmail.setHintEnabled(false);
        textInputPass.setHintEnabled(false);

    }

    private void setSpinnerData() {
        final ArrayList<GmailAccountInfo> pageTitles = getUserEmailId();
        ArrayAdapter<GmailAccountInfo> topDropDownListAadapter = new ArrayAdapter<GmailAccountInfo>(
                activity, R.layout.signup_spinner_layout,  //android.R.layout.simple_spinner_item,
                pageTitles) {

            public View getView(int position, View convertView, ViewGroup parent) {

                LayoutInflater inflater = activity.getLayoutInflater();
                View row = inflater.inflate(R.layout.signup_spinner_layout, parent,
                        false);
                TextView make = row.findViewById(R.id.email_text);
                make.setText(pageTitles.get(position).getId());
                return row;
            }


            public View getDropDownView(int position, View convertView,
                                        ViewGroup parent) {
                LayoutInflater inflater = activity.getLayoutInflater();
                View row = inflater.inflate(R.layout.signup_spinner_layout, parent,
                        false);
                TextView make = row.findViewById(R.id.email_text);
                TextView addTextview = row.findViewById(R.id.add_new_email);
                make.setText(pageTitles.get(position).getId());
                if (position == pageTitles.size() - 1) {
                    addTextview.setVisibility(View.VISIBLE);
                } else {
                    addTextview.setVisibility(View.GONE);
                }
                addTextview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        hideSpinnerDropDown(spinnerScreenList);
                        layout1.setVisibility(View.GONE);
                        layout2.setVisibility(View.VISIBLE);

                    }
                });
                return row;
            }
        };
        spinnerScreenList.setAdapter(topDropDownListAadapter);

    }

    public static void hideSpinnerDropDown(Spinner spinner) {
        try {
            Method method = Spinner.class.getDeclaredMethod("onDetachedFromWindow");
            method.setAccessible(true);
            method.invoke(spinner);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setSpannableStringPrivacyText() {
        try {

            String s = getActivity().getResources().getString(R.string.creating_account);
            int start = 0, end = 0;

            start = s.indexOf("#");
            end = s.lastIndexOf("#") - 1;
            SpannableString ss = new SpannableString(s.replace("#", ""));

            ClickableSpan span1 = new ClickableSpan() {
                @Override
                public void onClick(View textView) {

                    CUtils.googleAnalyticSendWitPlayServie(
                            getActivity(),
                            CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK,
                            CGlobalVariables.TERMS_OF_USE_CLICK,
                            null);
                    Intent intent = new Intent(getActivity(), ActShowOjasSoftArticles.class);
                    intent.putExtra(CGlobalVariables.ASTRO_WEBVIEW_TITLE_KEY, CGlobalVariables.MODULE_ABOUT_US);
                    // intent.putExtra("URL", "https://www.astrosage.com/controls/i_privacy-policy.asp");
                    intent.putExtra("URL", CGlobalVariables.ASTROSAGE_TERMS_CONDITIONS_URL);
                    intent.putExtra("TITLE_TO_SHOW", CGlobalVariables.terms_of_use_text);
                    startActivity(intent);
                }
            };
            ss.setSpan(span1, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            privacyText.setText(ss);
            privacyText.setLinkTextColor(getActivity().getResources().getColor(R.color.color_footer_grey));
            privacyText.setMovementMethod(LinkMovementMethod.getInstance());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setSpannableString() {
        SpannableString ss = new SpannableString(getResources().getString(R.string.agree_text));
        ClickableSpan span1 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent intent = new Intent(getActivity(), ActShowOjasSoftArticles.class);
                intent.putExtra(CGlobalVariables.ASTRO_WEBVIEW_TITLE_KEY, CGlobalVariables.MODULE_ABOUT_US);
                intent.putExtra("URL", CGlobalVariables.ASTROSAGE_DISCLAIMER_URL);
                intent.putExtra("TITLE_TO_SHOW", "Disclaimer");
                startActivity(intent);
            }
        };

        ClickableSpan span2 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                Intent intent = new Intent(getActivity(), ActShowOjasSoftArticles.class);
                intent.putExtra(CGlobalVariables.ASTRO_WEBVIEW_TITLE_KEY, CGlobalVariables.MODULE_ABOUT_US);
                intent.putExtra("URL", CGlobalVariables.ASTROSAGE_PRIVACY_POLICY_URL);
                intent.putExtra("TITLE_TO_SHOW", "Privacy Policy");
                startActivity(intent);
            }
        };
        int languageCode = ((AstrosageKundliApplication) activity
                .getApplication()).getLanguageCode();
        if (languageCode == CGlobalVariables.ENGLISH) {
            ss.setSpan(span1, 50, 60, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(span2, 65, 79, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (languageCode == CGlobalVariables.HINDI) {
            ss.setSpan(span1, 37, 50, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(span2, 54, 63, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (languageCode == CGlobalVariables.BANGALI) {
            ss.setSpan(span1, 33, 43, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(span2, 48, 64, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (languageCode == CGlobalVariables.TAMIL) {
            ss.setSpan(span1, 27, 40, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(span2, 48, 62, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (languageCode == CGlobalVariables.TELUGU) {
            ss.setSpan(span1, 33, 45, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(span2, 51, 67, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (languageCode == CGlobalVariables.MARATHI) {
            ss.setSpan(span1, 34, 43, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(span2, 46, 62, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (languageCode == CGlobalVariables.KANNADA) {
            ss.setSpan(span1, 31, 46, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(span2, 53, 70, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (languageCode == CGlobalVariables.MALAYALAM) {
            ss.setSpan(span1, 58, 72, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(span2, 82, 112, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } else if (languageCode == CGlobalVariables.GUJARATI) {
            ss.setSpan(span1, 29, 38, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(span2, 43, 54, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        }

        agreeTextView.setText(ss);
        agreeTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    public void gotoSelectedScreen(View textView) {
        Intent intent = new Intent(activity, ActDisclaimerAndPrivacyPolicy.class);
        switch (textView.getId()) {
            case R.id.textViewDisclaimer:
                intent.putExtra("screenType", 0);
                break;
            case R.id.textViewPrivacyPolicy:
                intent.putExtra("screenType", 1);
                break;
        }
        startActivity(intent);
    }

    private void returnToMasterActivityAfterLogin(String userLogin, String pwd, String userName) {
        //Log.i("Passwprd--" + pwd);
        CUtils.saveLoginDetailInPrefs(activity, userLogin, pwd, userName, true, true);
        // Clear old userid and password from old app
        SharedPreferences settings = activity.getSharedPreferences(
                CGlobalVariables.MYKUNDLI_PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(CGlobalVariables.PREF_USER_ID, "");
        editor.putString(CGlobalVariables.PREF_USERR_PWD, "");
        editor.commit();
        GmailAccountInfo gmailAccountInfo = new GmailAccountInfo();
        gmailAccountInfo.setId(userLogin);
        if (userName.equals("")) {

            gmailAccountInfo.setUserName(userLogin.split("@")[0]);
        } else {
            gmailAccountInfo.setUserName(userName);
        }
        CUtils.saveGmailAccountInfo(activity, gmailAccountInfo);

        // ADDED BY BIJENDRA ON 26-12-14 TO SAVE GCM REGISTRATION INFORMATION ON
        // SERVER
        int languageCode = ((AstrosageKundliApplication) activity
                .getApplication()).getLanguageCode();
        String regid = CUtils.getRegistrationId(activity
                .getApplicationContext());
        new ControllerManager().saveUserGCMRegistrationInformationOnOjasServer(
                activity.getApplicationContext(), regid, languageCode,
                userLogin);
        ((ActivityLoginAndSignin) activity).goToHome();

    }

    @Override
    public void onDetach() {
        super.onDetach();
        iWizardLoginRegisterFragmentInterface = null;
        activity = null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.facebook_button:
                CUtils.googleAnalyticSendWitPlayServie(
                        getActivity(),
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK,
                        CGlobalVariables.FACEBOOK_SIGNUP,
                        null);
                facebookLoginButton.performClick();
                break;

            case R.id.google_sign_in_button:
            case R.id.google_button:
                if (CUtils.isConnectedWithInternet(activity)) {
                    CUtils.googleAnalyticSendWitPlayServie(
                            getActivity(),
                            CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK,
                            CGlobalVariables.GOOGLE_SIGNUP,
                            null);
                    signIn();
                } else {
                    showCustomMsg(getResources().getString(R.string.no_internet));
                }
                break;

            case R.id.email_button:
                CUtils.googleAnalyticSendWitPlayServie(
                        getActivity(),
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK,
                        CGlobalVariables.EMAIL_SIGNUP,
                        null);
                fbGoogleLayout.setVisibility(View.GONE);
                signUpLayout.setVisibility(View.VISIBLE);
                break;

            case R.id.btnSignUp:
                doActionOnSignupButton();
                break;

            case R.id.textViewPrivacyPolicy:
                gotoSelectedScreen(textViewPrivacyPolicy);
                break;

            case R.id.textViewDisclaimer:
                gotoSelectedScreen(textViewDisclaimer);
                break;

            case R.id.edtMailId:
                /*if (isGoogleApiClientConnected) {
                    requestCredentials();
                } else {
                    setFocusOnEmail();
                }*/
                requestCredentials();
                break;

            case R.id.signin_text:
                CUtils.googleAnalyticSendWitPlayServie(
                        getActivity(),
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK,
                        CGlobalVariables.SIGNUP_SIGNIN_TEXT,
                        null);
                iWizardLoginRegisterFragmentInterface.clickedSignUpButton(1);
                break;

            default:
                break;
        }
    }

    public interface IUserAlreadyRegistered {
        void setEmailId(ActivityLoginAndSignin.EnumFrag enumFrag, String emailId);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void showSkipButton() {
        if (activity != null) {
            ((ActivityLoginAndSignin) activity).showSkipButton();
        }
    }

    private void signUpUsingOnlyEmailId() {
        showProgressBar();
        String url = CGlobalVariables.REGISTRATION_USER_WITHOUT_PASSWORD;
        StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, url, this, true, getParamsNew(), SIGNUP_WITH_ONLY_EMAILID).getMyStringRequest();
        queue.add(stringRequest);
    }

    public Map<String, String> getParamsNew() {
        Context context = AstrosageKundliApplication.getAppContext();
        String latitude = CUtils.getStringData(context, CGlobalVariables.KEY_LATITUDE, "");
        String longitude = CUtils.getStringData(context, CGlobalVariables.KEY_LONGITUDE, "");
        String city = CUtils.getStringData(context, CGlobalVariables.KEY_CITY, "");

        HashMap<String, String> params = new HashMap<String, String>();
        params.put(CGlobalVariables.KEY_EMAIL_ID, CUtils.replaceEmailChar(emailId));
        params.put("key", key);
        params.put("latitude", latitude);
        params.put("longitude", longitude);
        params.put("city", city);

        return params;
    }

    @Override
    public void onResponse(String response, int method) {
        if (activity == null || !isAdded()) {
            return;
        }
        Log.e("LOGIN DATA SIGNUP ", response);

        String[] status;
        if (method == SIGNUP_WITH_ONLY_EMAILID) {

            String respCode = "";
            if (response != null && response.length() > 0) {
                try {
                    JSONObject respObj = new JSONObject(response);
                    respCode = respObj.getString("msgcode");

                    if ((activity != null) && (respCode.equals("10") || respCode.equals("20") ||
                            respCode.equals("22"))) {

                        CUtils.signUpFirebaseEvent();
                        showCustomMsg(getResources().getString(R.string.sign_up_success));
                        CUtils.saveBooleanData(AstrosageKundliApplication.getAppContext(), CGlobalVariables.needToSendDeviceIdForLogin, false);

                        HashMap<String, String> jsonObjHash = CUtils.parseLoginSignupJson(respObj);
                        if (jsonObjHash != null && jsonObjHash.size() > 0) {

                            String userName = "", pwd = "";

                            if (jsonObjHash.containsKey(CGlobalVariables.USER_FIRSTNAME)) {
                                userName = jsonObjHash.get(CGlobalVariables.USER_FIRSTNAME);
                            }

                            if (jsonObjHash.containsKey(CGlobalVariables.USER_PASSWORD)) ;
                            {
                                pwd = jsonObjHash.get(CGlobalVariables.USER_PASSWORD);
                            }

                            //Log.e("LOGIN DATA PASS ",pwd);
                            returnToMasterActivityAfterLogin(emailId, pwd, userName);
                        }
                    } else {

                        if (respCode.equals("2") || respCode.equals("60") ||
                                respCode.equals("50") || respCode.equals("1") ||
                                respCode.equals("3") || respCode.equals("5")) {
                            showCustomMsg(getResources().getString(R.string.sign_up_validation_user_already_exists));
                            iUserAlreadyRegistered.setEmailId(ActivityLoginAndSignin.EnumFrag.WizardLoginFrag, emailId);
                            showSkipButton();
                        } else if (respCode.equals("4")) {
                            showCustomMsg(getResources().getString(R.string.sign_up_validation_authentication_failed));
                            showSkipButton();
                        } else {
                            showCustomMsg(getResources().getString(R.string.sign_up_validation_failed));
                            showSkipButton();
                        }
                    }
                    hideProgressBar();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                showCustomMsg(getResources().getString(R.string.server_error_msg));
                hideProgressBar();
            }

        } else if (method == USER_REGISTRATION) {

            String respCode = "";
            if (response != null && response.length() > 0) {
                try {
                    JSONObject respObj = new JSONObject(response);
                    respCode = respObj.getString("msgcode");

                    if ((activity != null) && (respCode.equals("10") || respCode.equals("20") ||
                            respCode.equals("22"))) {
                        signUpUser(respObj, "");
                    } else {

                        if (respCode.equals("2") || respCode.equals("60") ||
                                respCode.equals("50") || respCode.equals("1") ||
                                respCode.equals("3") || respCode.equals("5")) {

                            if (operationName.equalsIgnoreCase(CGlobalVariables.OPERATION_NAME_SOCIALMEDIA)) {
                                signUpUser(respObj, CGlobalVariables.OPERATION_NAME_SOCIALMEDIA);
                            } else {
                                showCustomMsg(getResources().getString(R.string.sign_up_validation_user_already_exists));
                                iUserAlreadyRegistered.setEmailId(ActivityLoginAndSignin.EnumFrag.WizardLoginFrag, emailId);
                                showSkipButton();
                            }

                        } else if (respCode.equals("4")) {
                            showCustomMsg(getResources().getString(R.string.sign_up_validation_authentication_failed));
                            showSkipButton();
                        } else {
                            showCustomMsg(getResources().getString(R.string.sign_up_validation_failed));
                            showSkipButton();
                        }
                    }
                    hideProgressBar();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                hideProgressBar();
                showCustomMsg(getResources().getString(R.string.server_error_msg));
            }
        }

    }


    private void signUpUser(JSONObject responseObj, String operatnName) {
        CUtils.saveBooleanData(AstrosageKundliApplication.getAppContext(), CGlobalVariables.needToSendDeviceIdForLogin, false);
        CUtils.signUpFirebaseEvent();
        showCustomMsg(getResources().getString(R.string.sign_up_success));

        HashMap<String, String> jsonObjHash = CUtils.parseLoginSignupJson(responseObj);
        if (jsonObjHash != null && jsonObjHash.size() > 0) {

            if (emailId == null || emailId.length() == 0) {
                if (jsonObjHash.containsKey(CGlobalVariables.USERID)) {
                    emailId = jsonObjHash.get(CGlobalVariables.USERID);
                }
            }
            if (password == null || password.length() == 0) {
                if (jsonObjHash.containsKey(CGlobalVariables.USER_PASSWORD)) {
                    password = jsonObjHash.get(CGlobalVariables.USER_PASSWORD);
                }
            }
            saveSignUpInformation(activity, emailId, password, jsonObjHash, operatnName);
        }
    }

    @Override
    public void onError(VolleyError error) {
        if (activity != null && isAdded()) {
            hideProgressBar();
            showCustomMsg(getResources().getString(R.string.sign_up_validation_failed));
            showSkipButton();
        }

    }

    private String[] parseSignupResult(String jsonString) {
        String[] resultArray = new String[2];
        try {
            JSONArray jsonArray = new JSONArray(jsonString);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String resultString = jsonObject.get("Result").toString();
            resultArray[0] = resultString;
            if (resultString.equals("-1") || resultString.equals("0") || resultString.equals("4") || resultString.equals("5")) {
                resultArray[1] = "";
            } else if (resultString.equals("1")) {
                resultArray[1] = jsonObject.get("userpassword").toString();
            }
        } catch (Exception e) {
            resultArray[0] = "1001"; //Incorrect Json Format Error
        }
        return resultArray;
    }

    /**
     * show Progress Bar
     */
    private void showProgressBar() {
        if (activity != null) {
            if (pd == null) {
                pd = new CustomProgressDialog(activity, ((BaseInputActivity) activity).regularTypeface);
            }
            pd.setCanceledOnTouchOutside(false);
            if (!pd.isShowing()) {
                pd.show();
            }
        }
    }

    /**
     * hide Progress Bar
     */
    private void hideProgressBar() {
        if (activity != null) {
            try {
                if (pd != null && pd.isShowing())
                    pd.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void signIn() {
        launchCredentialManager();
    }

    private static final int MAX_RETRY_ATTEMPTS = 3;
    private int retryCount = 0;

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
                getActivity(),
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

            // Sign in to Firebase with using the token
            firebaseAuthWithGoogle(googleIdTokenCredential.getIdToken());
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        try {
            AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(getActivity(), task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            try {
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
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
        try {
            if (accountDataObj != null) {

                userName = accountDataObj.getDisplayName();
                emailId = accountDataObj.getEmail();

                operationName = CGlobalVariables.OPERATION_NAME_SOCIALMEDIA;
                showProgressBar();
                CUtils.verifyLoginWithUserPurchasedPlan(WizardSignupFrag.this, emailId,
                        "", userName, operationName,
                        CGlobalVariables.REG_SOURCE_ANDROID_GOOGLE, USER_REGISTRATION);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getUserProfile(AccessToken currentAccessToken) {
        GraphRequest request = GraphRequest.newMeRequest(
                currentAccessToken, new GraphRequest.GraphJSONObjectCallback() {
                    @Override
                    public void onCompleted(JSONObject object, GraphResponse response) {
                        //Log.d("TAG", object.toString());
                        try {
                            if (object == null) return;
                            String first_name = object.getString("first_name");
                            String last_name = object.getString("last_name");
                            String email = object.getString("email");
                            String id = object.getString("id");
                            String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";
                            if (TextUtils.isEmpty(email)) return;
                            /*Log.e("FACEBOOK ","first_name"+ first_name);
                            Log.e("FACEBOOK ","last_name"+ last_name);
                            Log.e("FACEBOOK ","email"+ email);
                            Log.e("FACEBOOK ","id"+ id);
                            Log.e("FACEBOOK ","image_url"+ image_url);*/

                            userName = first_name + " " + last_name;
                            operationName = CGlobalVariables.OPERATION_NAME_SOCIALMEDIA;
                            showProgressBar();
                            CUtils.verifyLoginWithUserPurchasedPlan(WizardSignupFrag.this, email,
                                    "", userName, operationName,
                                    CGlobalVariables.REG_SOURCE_ANDROID_FACEBOOK, USER_REGISTRATION);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email,id");
        request.setParameters(parameters);
        request.executeAsync();

    }
}
