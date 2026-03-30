package com.ojassoft.astrosage.ui.fragments;

import static com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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

import com.android.volley.VolleyError;
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
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.controller.ControllerManager;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.model.GmailAccountInfo;
import com.ojassoft.astrosage.ui.act.ActForgetPassword;
import com.ojassoft.astrosage.ui.act.ActivityLoginAndSignin;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.Executors;

/**
 * Created by ojas-08 on 14/6/16.
 */
public class WizardLoginFrag extends Fragment implements VolleyResponse, View.OnClickListener {
    EditText idEditText;
    EditText passwordEditText;
    Button signButton;
    TextView forgetPasswordTextView;
    TextInputLayout textInputEmail;
    TextInputLayout textInputPass;
    LinearLayout usernamePassword_container;
    CheckBox showPasswordCheckbox;
    Activity activity;
    //LoginVerificationOnline loginVerificationOnline;
    CustomProgressDialog pd = null;
    String _userId = "", _pwd = "", userName = "";
    SignInButton googleSignInButton;
    private static final int RC_SIGN_IN = 9001;
    LoginButton facebookLoginButton;
    private static final String EMAIL = "email";
    CallbackManager callbackManager;
    RelativeLayout fbGoogleLayout;
    LinearLayout loginLayout, facebookButton, googleButton, emailButton;
    TextView welcomeText, chartSignupText, signinText, privacyText, haveAccountText;
    WizardLogingRegister.IWizardLoginRegisterFragmentInterface iWizardLoginRegisterFragmentInterface;
    private CredentialManager credentialManager;
    private FirebaseAuth mAuth;

    private static final int MAX_RETRY_ATTEMPTS = 3;
    private int retryCount = 0;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        iWizardLoginRegisterFragmentInterface = (WizardLogingRegister.IWizardLoginRegisterFragmentInterface) context;
        activity = context;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.wizard_login_frag_layout, null);
        getIdOfAllView(view);

        signButton.setOnClickListener(this);
        forgetPasswordTextView.setOnClickListener(this);
        showPasswordCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    if (!isChecked) {
                        // show password
                        passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                        passwordEditText.setSelection(passwordEditText.getText().length());
                    } else {
                        // hide password
                        passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                        passwordEditText.setSelection(passwordEditText.getText().length());
                    }
                } catch (Exception ex) {
                    //
                }
            }
        });

        fbGoogleLayout = view.findViewById(R.id.fb_google_layout);
        loginLayout = view.findViewById(R.id.login_layout);

        fbGoogleLayout.setVisibility(View.VISIBLE);
        loginLayout.setVisibility(View.GONE);

        facebookButton = view.findViewById(R.id.facebook_button);
        googleButton = view.findViewById(R.id.google_button);
        emailButton = view.findViewById(R.id.email_button);
        welcomeText = view.findViewById(R.id.welcome_text);
        chartSignupText = view.findViewById(R.id.chart_signup_text);
        signinText = view.findViewById(R.id.signin_text);
        privacyText = view.findViewById(R.id.privacy_text);
        haveAccountText = view.findViewById(R.id.have_account_text);

        haveAccountText.setText(getActivity().getResources().getString(R.string.dont_have_account));
        welcomeText.setVisibility(View.VISIBLE);
        chartSignupText.setVisibility(View.GONE);
        privacyText.setVisibility(View.INVISIBLE);
        signinText.setText(getActivity().getResources().getString(R.string.sign_up));

        signinText.setOnClickListener(this);
        facebookButton.setOnClickListener(this);
        googleButton.setOnClickListener(this);
        emailButton.setOnClickListener(this);

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
        try {
            //facebookLoginButton.setReadPermissions(Arrays.asList("email", "public_profile"));
            //facebookLoginButton.setReadPermissions("email", "public_profile", "user_friends");
            facebookLoginButton.setPermissions(Arrays.asList("email", "public_profile", "user_friends"));

        } catch (Exception e) {
            //Log.e("SAN API123",  " setReadPermissions expection=>" + e.toString());
        }

        // Callback registration
        facebookLoginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                // App code
                Log.e("SAN FB", " loginResult " + loginResult.getAccessToken());
                boolean loggedIn = AccessToken.getCurrentAccessToken() == null;
                Log.e("SAN FB", " loggedIn  " + loggedIn);
                getUserProfile(AccessToken.getCurrentAccessToken());
            }

            @Override
            public void onCancel() {
                Log.e("SAN FB", "onCancel ");
            }

            @Override
            public void onError(FacebookException exception) {
                Log.e("SAN FB", "onError " + exception.toString());
            }
        });

        googleSignInButton = view.findViewById(R.id.google_sign_in_button);
        credentialManager = CredentialManager.create(getActivity());
        mAuth = FirebaseAuth.getInstance();

        return view;
    }

    private void getIdOfAllView(View view) {
        passwordEditText = view.findViewById(R.id.password_edittext);
        idEditText = view.findViewById(R.id.etUserName);

        signButton = view.findViewById(R.id.buttonSignIn);
        forgetPasswordTextView = view.findViewById(R.id.textViewForgetPass);
        showPasswordCheckbox = view.findViewById(R.id.show_password);

        textInputEmail = view.findViewById(R.id.input_layout_email);
        textInputPass = view.findViewById(R.id.input_layout_pass);
        usernamePassword_container = view
                .findViewById(R.id.usernamePassword_container);
        if (((BaseInputActivity) activity).LANGUAGE_CODE == CGlobalVariables.ENGLISH) {
            signButton.setText(getResources().getString(R.string.button_sign_in).toUpperCase());
        }
        setTypefaceOfView();
    }

    private void setTypefaceOfView() {
        idEditText.setTypeface(((BaseInputActivity) activity).robotRegularTypeface);
        passwordEditText.setTypeface(((BaseInputActivity) activity).robotRegularTypeface);
        signButton.setTypeface(((BaseInputActivity) activity).mediumTypeface);
        forgetPasswordTextView.setTypeface(((BaseInputActivity) activity).regularTypeface);
        showPasswordCheckbox.setTypeface(((BaseInputActivity) activity).regularTypeface);
    }

    private void doActionOnSignOnClick() {
        if (validateData()) {
            CUtils.googleAnalyticSendWitPlayServie(
                    activity,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                    CGlobalVariables.GOOGLE_ANALYTIC_ACTION_ASTROSAGE_LOGIN,
                    null);
            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_ACTION_ASTROSAGE_LOGIN, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

            goToSignIn();
            CUtils.hideMyKeyboard(activity);
        }
    }

    private void doActionOnForgetPassword() {
        CUtils.googleAnalyticSendWitPlayServie(
                activity,
                CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                CGlobalVariables.GOOGLE_ANALYTIC_ACTION_ASTROSAGE_LOGIN_FORGET_PWD,
                null);
        CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_ACTION_ASTROSAGE_LOGIN_FORGET_PWD, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

        goToForgotPassword();
    }


    private boolean validateData() {
        boolean flag = validateName(idEditText, textInputEmail, getString(R.string.email_login)) && validateName(passwordEditText, textInputPass, getString(R.string.enter_password));

        return flag;
    }

    private boolean validateName(EditText name, TextInputLayout inputLayout, String message) {
        if (name.getText().toString().trim().isEmpty()) {
            inputLayout.setErrorEnabled(true);
            requestFocus(name);
            inputLayout.setError(message);
            name.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
            return false;
        } else {
            inputLayout.setErrorEnabled(false);
            inputLayout.setError(null);
            name.getBackground().setColorFilter(getResources().getColor(R.color.ColorPrimary), PorterDuff.Mode.SRC_ATOP);
        }
        if (!CUtils.isConnectedWithInternet(activity)) {
            MyCustomToast mct = new MyCustomToast(activity, activity
                    .getLayoutInflater(), activity, ((BaseInputActivity) activity).regularTypeface);
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

    public void goToSignIn() {

        _userId = idEditText.getText().toString();
        _pwd = passwordEditText.getText().toString();
       /* loginVerificationOnline = new LoginVerificationOnline(_userId, _pwd);
        loginVerificationOnline.execute();*/
        if (CUtils.isConnectedWithInternet(activity)) {
            showProgressBar();
            // CUtils.verifyLoginWithUserPurchasedPlan(WizardLoginFrag.this, _userId, _pwd, 0);
            CUtils.verifyLoginWithUserPurchasedPlan(WizardLoginFrag.this, _userId,
                    _pwd, userName, CGlobalVariables.OPERATION_NAME_LOGIN,
                    CGlobalVariables.REG_SOURCE_ANDROID, 3);

        } else {
            MyCustomToast mct = new MyCustomToast(activity,
                    activity.getLayoutInflater(), activity,
                    ((BaseInputActivity) activity).regularTypeface);
            mct.show(getResources().getString(R.string.no_internet));

        }
    }

    private void shakeMyViewOnSignFailed(View viewToAnimate) {
        Animation shake = AnimationUtils.loadAnimation(activity,
                R.anim.shake);
        viewToAnimate.startAnimation(shake);
    }


    public void goToForgotPassword() {
        Intent intent = new Intent(activity, ActForgetPassword.class);
        startActivity(intent);
    }

    private void saveInformation(Activity activity, String userId, String password, HashMap<String, String> profileInfo) {

        String userName = "", mobile = "", occupation = "", maritalStatus = "",
                companyName = "", address1 = "", address2 = "", userPlanExpiryDate = "",
                userPlanPurchaseDate = "", userPlanId = "1";

        if (profileInfo.containsKey(CGlobalVariables.USERID)) {
            userId = profileInfo.get(CGlobalVariables.USERID);
        }

        if (profileInfo.containsKey(CGlobalVariables.USER_FIRSTNAME)) {
            userName = profileInfo.get(CGlobalVariables.USER_FIRSTNAME);
        }
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

        CUtils.saveLoginDetailInPrefs(activity, userId, password, userName, true, false);
        // Clear old userid and password from old app
        SharedPreferences settings = activity.getSharedPreferences(
                CGlobalVariables.MYKUNDLI_PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(CGlobalVariables.PREF_USER_ID, "");
        editor.putString(CGlobalVariables.PREF_USERR_PWD, "");
        editor.commit();
        GmailAccountInfo gmailAccountInfo = new GmailAccountInfo();
        gmailAccountInfo.setId(userId);
        if (userName.equals("")) {
            gmailAccountInfo.setUserName(userId.split("@")[0]);
        } else {
            gmailAccountInfo.setUserName(userName);
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
        CUtils.saveGmailAccountInfo(activity, gmailAccountInfo);
        CUtils.storeUserPurchasedPlanInPreference(activity, Integer.parseInt(userPlanId));

        CUtils.saveStringData(activity, CGlobalVariables.PREF_USER_PLAN_PURCHASE_DATE, userPlanPurchaseDate);//Purchase plan Date
        CUtils.saveStringData(activity, CGlobalVariables.PREF_USER_PLAN_Expiry_DATE, userPlanExpiryDate);//Expiry plan  Date
          /*  iWizardLoginRegisterFragmentInterface.clickedSignInButton(userLogin,
                    pwd);*/

        // ADDED BY BIJENDRA ON 26-12-14 TO SAVE GCM REGISTRATION INFORMATION ON
        // SERVER
        int languageCode = ((AstrosageKundliApplication) activity
                .getApplication()).getLanguageCode();
        String regid = CUtils.getRegistrationId(activity
                .getApplicationContext());
        new ControllerManager().saveUserGCMRegistrationInformationOnOjasServer(
                activity.getApplicationContext(), regid, languageCode,
                userId);
        ((ActivityLoginAndSignin) activity).goToHome();
    }

    private void saveInformation(Activity activity, String[] result, String userId, String password, String userName) {

        CUtils.saveLoginDetailInPrefs(activity, userId, password, userName, true, false);
        // Clear old userid and password from old app
        SharedPreferences settings = activity.getSharedPreferences(
                CGlobalVariables.MYKUNDLI_PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(CGlobalVariables.PREF_USER_ID, "");
        editor.putString(CGlobalVariables.PREF_USERR_PWD, "");
        editor.commit();
        GmailAccountInfo gmailAccountInfo = new GmailAccountInfo();
        gmailAccountInfo.setId(userId);
        if (userName.equals("")) {
            gmailAccountInfo.setUserName(userId.split("@")[0]);
        } else {
            gmailAccountInfo.setUserName(userName);
        }

        gmailAccountInfo.setMobileNo(result[26]);
        if (!TextUtils.isEmpty(result[27])) {
            gmailAccountInfo.setOccupation(Integer.parseInt(result[27]));
        } else {
            gmailAccountInfo.setOccupation(0);
        }
        if (!TextUtils.isEmpty(result[28])) {
            gmailAccountInfo.setMaritalStatus(Integer.parseInt(result[28]));
        } else {
            gmailAccountInfo.setMaritalStatus(0);
        }


        gmailAccountInfo.setHeading(result[29]);
        gmailAccountInfo.setAddress1(result[30]);
        gmailAccountInfo.setAddress2(result[31]);
        CUtils.saveGmailAccountInfo(activity, gmailAccountInfo);
        CUtils.storeUserPurchasedPlanInPreference(activity, Integer.parseInt(result[1]));

        CUtils.saveStringData(activity, CGlobalVariables.PREF_USER_PLAN_PURCHASE_DATE, result[2]);//Purchase plan Date
        CUtils.saveStringData(activity, CGlobalVariables.PREF_USER_PLAN_Expiry_DATE, result[3]);//Expiry plan  Date
          /*  iWizardLoginRegisterFragmentInterface.clickedSignInButton(userLogin,
                    pwd);*/

        // ADDED BY BIJENDRA ON 26-12-14 TO SAVE GCM REGISTRATION INFORMATION ON
        // SERVER
        int languageCode = ((AstrosageKundliApplication) activity
                .getApplication()).getLanguageCode();
        String regid = CUtils.getRegistrationId(activity
                .getApplicationContext());
        new ControllerManager().saveUserGCMRegistrationInformationOnOjasServer(
                activity.getApplicationContext(), regid, languageCode,
                userId);
        ((ActivityLoginAndSignin) activity).goToHome();


    }

    @Override
    public void onDetach() {
        super.onDetach();
        iWizardLoginRegisterFragmentInterface = null;
        activity = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        textInputEmail.setHintEnabled(false);
        textInputPass.setHintEnabled(false);

    }

    public void updateEmailIdInEditTextView(String emailId) {
        if (idEditText != null) {
            idEditText.setText(emailId);
        }
    }

    @Override
    public void onResponse(String response, int method) {
        if (activity == null || !isAdded()) {
            return;
        }
        //Log.e("LOGIN DATA ",response);

        String respCode = "";
        if (response != null && response.length() > 0) {
            try {
                JSONObject respObj = new JSONObject(response);
                respCode = respObj.getString("msgcode");

                hideProgressBar();
                if (activity != null) {

                    /*10 for new signup, 2 and 60 for simple login
                  1 and 5 for first time login after plan purchased
                  22 for plan activated successfully
                  3 for use id exist but record not found in corresponding to passed device id*/

                    if (respCode.equals("10") || respCode.equals("2") || respCode.equals("60") ||
                            respCode.equals("1") || respCode.equals("5") ||
                            respCode.equals("22") || respCode.equals("3")) {

                        if (respCode.equals("22"))                //if plan activated successfully
                        {
                            CUtils.saveBooleanData(AstrosageKundliApplication.getAppContext(), CGlobalVariables.needToSendDeviceIdForLogin, false);
                            MyCustomToast mct = new MyCustomToast(activity,
                                    activity.getLayoutInflater(), activity,
                                    ((BaseInputActivity) activity).regularTypeface);
                            mct.show(getResources().getString(R.string.plan_activate_success));

                        } else if (respCode.equals("3")) {
                            CUtils.saveBooleanData(AstrosageKundliApplication.getAppContext(), CGlobalVariables.needToSendDeviceIdForLogin, false);
                            MyCustomToast mct = new MyCustomToast(activity,
                                    activity.getLayoutInflater(), activity,
                                    ((BaseInputActivity) activity).regularTypeface);
                            mct.show(getResources().getString(R.string.user_exist_record_not_found));
                        } else {
                            MyCustomToast mct = new MyCustomToast(activity,
                                    activity.getLayoutInflater(), activity,
                                    ((BaseInputActivity) activity).regularTypeface);
                            mct.show(getResources().getString(R.string.sign_in_success));
                        }

                        if (method == 1) {
                            CUtils.logInFirebaseEvent(CGlobalVariables.FIREBASE_LOGIN_VIA_FACEBOOK);
                        } else if (method == 2) {
                            CUtils.logInFirebaseEvent(CGlobalVariables.FIREBASE_LOGIN_VIA_GOOGLE);
                        } else if (method == 3) {
                            CUtils.logInFirebaseEvent(CGlobalVariables.FIREBASE_LOGIN_VIA);
                        }
                        //Checking is there any unconsumed data in google account
                        CUtils.startServiceToConsumeProduct(activity);

                        HashMap<String, String> jsonObjHash = CUtils.parseLoginSignupJson(respObj);
                        if (jsonObjHash != null && jsonObjHash.size() > 0) {

                            if (_userId == null || _userId.length() == 0) {
                                if (jsonObjHash.containsKey(CGlobalVariables.USERID)) {
                                    _userId = jsonObjHash.get(CGlobalVariables.USERID);
                                }
                            }
                            if (_pwd == null || _pwd.length() == 0) {
                                if (jsonObjHash.containsKey(CGlobalVariables.USER_PASSWORD)) {
                                    _pwd = jsonObjHash.get(CGlobalVariables.USER_PASSWORD);
                                }
                            }
                            saveInformation(activity, _userId, _pwd, jsonObjHash);
                        }
                    } else {
                        if (respCode.equals("40")) {
                            MyCustomToast mct = new MyCustomToast(activity,
                                    activity.getLayoutInflater(), activity,
                                    ((BaseInputActivity) activity).regularTypeface);
                            mct.show(getResources().getString(R.string.email_doesnot_exist));
                        } else if (respCode.equals("30")) {
                            MyCustomToast mct = new MyCustomToast(activity,
                                    activity.getLayoutInflater(), activity,
                                    ((BaseInputActivity) activity).regularTypeface);
                            mct.show(getResources().getString(R.string.password_not_match));
                        } else if (respCode.equals("4")) {
                            MyCustomToast mct = new MyCustomToast(activity,
                                    activity.getLayoutInflater(), activity,
                                    ((BaseInputActivity) activity).regularTypeface);
                            mct.show(getResources().getString(R.string.sign_up_validation_authentication_failed));
                        } else {
                            MyCustomToast mct = new MyCustomToast(activity,
                                    activity.getLayoutInflater(), activity,
                                    ((BaseInputActivity) activity).regularTypeface);
                            mct.show(getResources().getString(R.string.sign_in_failed));
                        }
                        shakeMyViewOnSignFailed(usernamePassword_container);
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        } else {
            hideProgressBar();
            MyCustomToast mct = new MyCustomToast(activity,
                    activity.getLayoutInflater(), activity,
                    ((BaseInputActivity) activity).regularTypeface);
            mct.show(getResources().getString(R.string.server_error_msg));

        }
    }

    @Override
    public void onError(VolleyError error) {
        if (activity != null && isAdded()) {
            hideProgressBar();
            if (error != null && error.getMessage() != null) {
                MyCustomToast mct = new MyCustomToast(activity, activity.getLayoutInflater(), activity,
                        ((BaseInputActivity) activity).regularTypeface);
                mct.show(error.getMessage());
            }
        }
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
                if (pd != null & pd.isShowing())
                    pd.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
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
                getActivity(),
                request,
                new CancellationSignal(),
                Executors.newSingleThreadExecutor(),
                new CredentialManagerCallback() {
                    @Override
                    public void onResult(Object o) {
                        Log.e("CredentialManager", "Credential retrieved successfully="+o.toString());
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
        Log.e("CredentialManager", "handleSignInResult credential="+credential.toString());
        // Check if credential is of type Google ID
        if (credential instanceof CustomCredential && credential.getType().equals(TYPE_GOOGLE_ID_TOKEN_CREDENTIAL)) {
            // Create Google ID Token
            Bundle credentialData = credential.getData();
            GoogleIdTokenCredential googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credentialData);
            Log.e("CredentialManager", "handleSignInResult googleIdTokenCredential="+googleIdTokenCredential.toString());

            // Sign in to Firebase with using the token
            firebaseAuthWithGoogle(googleIdTokenCredential.getIdToken());
        } else {
            Log.e("CredentialManager", "handleSignInResult else condition");
        }
    }

    private void firebaseAuthWithGoogle(String idToken) {
        try {
            Log.e("CredentialManager", "handleSignInResult firebaseAuthWithGoogle=" + idToken.toString());

            AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);
            mAuth.signInWithCredential(credential)
                    .addOnCompleteListener(getActivity(), task -> {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.e("CredentialManager", "handleSignInResult task.isSuccessful()=" + task.isSuccessful());

                            try {
                                FirebaseUser user = mAuth.getCurrentUser();
                                Log.e("CredentialManager", "handleSignInResult user=" + user);
                                updateUI(user);
                            } catch (Exception e) {
                                Log.e("CredentialManager", "handleSignInResult Exception=" + e.toString());

                            }
                        } else {
                            Log.e("CredentialManager", "handleSignInResult task.isSuccessful() not success");
                            Exception e = task.getException();
                            Log.e("CredentialManager", "handleSignInResult Exception=" + e);
                        }
                    });
        } catch (Exception e){
            //
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void updateUI(FirebaseUser accountDataObj) {
        Log.e("CredentialManager", "updateUI accountDataObj="+accountDataObj);
        try {
            if (accountDataObj != null) {

                userName = accountDataObj.getDisplayName();
                _userId = accountDataObj.getEmail();
                Log.e("CredentialManager", "updateUI userName="+userName);
                Log.e("CredentialManager", "updateUI _userId="+_userId);
                showProgressBar();
                CUtils.verifyLoginWithUserPurchasedPlan(WizardLoginFrag.this, _userId,
                        "", userName, CGlobalVariables.OPERATION_NAME_SOCIALMEDIA,
                        CGlobalVariables.REG_SOURCE_ANDROID_GOOGLE, 2);

            }
        } catch (Exception e) {
            Log.e("CredentialManager", "handleSignInResult Exception2="+e);
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

                            showProgressBar();
                            CUtils.verifyLoginWithUserPurchasedPlan(WizardLoginFrag.this, email,
                                    "", userName, CGlobalVariables.OPERATION_NAME_SOCIALMEDIA,
                                    CGlobalVariables.REG_SOURCE_ANDROID_FACEBOOK, 1);

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.facebook_button:
                CUtils.googleAnalyticSendWitPlayServie(
                        getActivity(),
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK,
                        CGlobalVariables.FACEBOOK_LOGIN,
                        null);
                facebookLoginButton.performClick();
                break;


            case R.id.google_sign_in_button:
            case R.id.google_button:
                if (CUtils.isConnectedWithInternet(activity)) {
                    CUtils.googleAnalyticSendWitPlayServie(
                            getActivity(),
                            CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK,
                            CGlobalVariables.GOOGLE_LOGIN,
                            null);
                    signIn();
                } else {
                    MyCustomToast mct = new MyCustomToast(activity,
                            activity.getLayoutInflater(), activity,
                            ((BaseInputActivity) activity).regularTypeface);
                    mct.show(getResources().getString(R.string.no_internet));
                }
                break;


            case R.id.email_button:
                CUtils.googleAnalyticSendWitPlayServie(
                        getActivity(),
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK,
                        CGlobalVariables.EMAIL_LOGIN,
                        null);
                fbGoogleLayout.setVisibility(View.GONE);
                loginLayout.setVisibility(View.VISIBLE);
                break;

            case R.id.buttonSignIn:
                doActionOnSignOnClick();
                break;

            case R.id.textViewForgetPass:
                doActionOnForgetPassword();
                break;

            case R.id.signin_text:
                CUtils.googleAnalyticSendWitPlayServie(
                        getActivity(),
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK,
                        CGlobalVariables.LOGIN_SIGNUP_TEXT,
                        null);
                iWizardLoginRegisterFragmentInterface.clickedSignUpButton(0);
                break;

            default:
                break;
        }
    }
}