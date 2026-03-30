package com.ojassoft.astrosage.ui.fragments;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.google.android.material.textfield.TextInputLayout;
import androidx.core.app.ActivityCompat;
import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.CustomCredential;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.fragment.app.Fragment;

import android.os.CancellationSignal;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

//import com.google.analytics.tracking.android.Log;
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
import com.google.android.gms.common.api.ApiException;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.controller.ControllerManager;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.libojassoft.android.misc.VolleyResponse;
import com.ojassoft.astrosage.model.GmailAccountInfo;
import com.ojassoft.astrosage.ui.act.ActLogin;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.ui.fragments.WizardLogingRegister.IWizardLoginRegisterFragmentInterface;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.UserEmailFetcher;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tasks.Task;
import com.ojassoft.astrosage.notification.ActShowOjasSoftArticles;
import com.ojassoft.astrosage.varta.ui.activity.LoginSignUpActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.Executors;

import static android.app.Activity.RESULT_OK;
import static com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL;

/*
 * @date : 11 jan 2016
 * @description : This class is used to register the user in server
 */
public class FragSignUp extends Fragment implements VolleyResponse, OnClickListener {

    EditText edtMailId, edtPassword;
    Button btnSignUp, btnBackPressed;
    TextView tvEmailId, tvPassword;
    TextInputLayout textInputEmail, textInputPass;
    TextView agreeTextView;
    private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    //public Typeface typeface;

    String emailId = "";
    String password = "";
    String[] iReturn;

    IWizardLoginRegisterFragmentInterface iWizardLoginRegisterFragmentInterface;
    Activity activity;
    private TextView textViewNote1, textViewNote2;
    int SIGN_UP = 0;
    int LOGIN_VERIFICATION = 1;
    //AsyncTaskRegistrationOfUser asyncTaskRegistrationOfUser;

    CallbackManager callbackManager;
    private boolean isEmailDialogueOpened;
    private String selectedEmailId;
    public FragSignUp() {
        setRetainInstance(true);
    }

    String userName="", operationName="";
    SignInButton googleSignInButton;
    LoginButton facebookLoginButton;
    private static final String EMAIL = "email";
    RelativeLayout fbGoogleLayout;
    LinearLayout signUpLayout, facebookButton, googleButton, emailButton,signUpWithMobile;
    TextView welcomeText, chartSignupText, signinText, privacyText, haveAccountText;
    private FirebaseAuth mAuth;
    private CredentialManager credentialManager;
    private boolean isPasswordHiden = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.lay_wizard_signup, container,
                false);

        LANGUAGE_CODE = ((AstrosageKundliApplication) activity.getApplication()).getLanguageCode();


        //typeface = CUtils.getUserSelectedLanguageFontType(getActivity(), LANGUAGE_CODE);

        setLayRef(view);

        return view;
    }

    /*
     * @date : 11 jan 2016
     * @description : This method is used to look up the XMl fields
     */
    private void setLayRef(View view) {
        edtMailId = (EditText) view.findViewById(R.id.edtMailId);
        edtMailId.setTypeface(((BaseInputActivity) activity).robotRegularTypeface);
        edtPassword = (EditText) view.findViewById(R.id.edtPassword);
        edtPassword.setTypeface(((BaseInputActivity) activity).robotRegularTypeface);
        edtMailId.addTextChangedListener(new MyTextWatcher(edtMailId));
        edtPassword.addTextChangedListener(new MyTextWatcher(edtPassword));

        btnSignUp = (Button) view.findViewById(R.id.btnSignUp);
        btnBackPressed = (Button) view.findViewById(R.id.btnBackPressed);
        TextView signUpTitleTV = (TextView) view.findViewById(R.id.font_auto_lay_wizard_signup_1);
        tvEmailId = (TextView) view.findViewById(R.id.tvEmailId);
        tvPassword = (TextView) view.findViewById(R.id.tvPassword);
        agreeTextView = (TextView) view.findViewById(R.id.agree_text);
        //tvHeading = (TextView)view.findViewById(R.id.tvHeading);
        //tvNotes = (TextView)view.findViewById(R.id.tvNotes);

        textViewNote1 = (TextView) view.findViewById(R.id.tvNote1);
        textViewNote1.setTypeface(((ActLogin) activity).regularTypeface);
        CUtils.setClickableSpan(textViewNote1,
                Html.fromHtml(getString(R.string.login_note1)), activity, ((ActLogin) activity).regularTypeface);
        textViewNote2 = (TextView) view.findViewById(R.id.tvNote2);
        textViewNote2.setTypeface(((ActLogin) activity).regularTypeface);
        CUtils.setClickableSpan(textViewNote2,
                Html.fromHtml(getString(R.string.login_note2)), activity, ((ActLogin) activity).regularTypeface);

        textInputEmail = (TextInputLayout) view.findViewById(R.id.input_layout_email);
        textInputPass = (TextInputLayout) view.findViewById(R.id.input_layout_pass);

        //set type face of all fields
        FontUtils.changeFont(requireContext(), signUpTitleTV, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        tvEmailId.setTypeface(((ActLogin) activity).regularTypeface);
        tvPassword.setTypeface(((ActLogin) activity).regularTypeface);
        //tvHeading.setTypeface(typeface);
        btnSignUp.setTypeface(((ActLogin) activity).regularTypeface);
        if (LANGUAGE_CODE == 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                btnSignUp.setAllCaps(true);
            }
        }
        btnBackPressed.setTypeface(((ActLogin) activity).regularTypeface);

        if (((AstrosageKundliApplication) activity.getApplication())
                .getLanguageCode() == CGlobalVariables.ENGLISH) {
            btnBackPressed.setText(getResources().getString(R.string.back_button).toUpperCase());
            btnSignUp.setText(getResources().getString(R.string.sign_up).toUpperCase());
        }
        btnSignUp.setOnClickListener(this);
        btnBackPressed.setOnClickListener(this);

        setEmailIdFromUserAccount();

        fbGoogleLayout = (RelativeLayout)view.findViewById(R.id.fb_google_layout);
        signUpLayout = (LinearLayout)view.findViewById(R.id.signup_layout);

       // fbGoogleLayout.setVisibility(View.VISIBLE);
        //signUpLayout.setVisibility(View.GONE);

        facebookButton = (LinearLayout)view.findViewById(R.id.facebook_button);
        googleButton = (LinearLayout)view.findViewById(R.id.google_button);
        emailButton = (LinearLayout)view.findViewById(R.id.email_button);
        signUpWithMobile = (LinearLayout)view.findViewById(R.id.loginWithMobile_button);
        welcomeText = (TextView) view.findViewById(R.id.welcome_text);
        chartSignupText = (TextView)view.findViewById(R.id.chart_signup_text);
        signinText = (TextView)view.findViewById(R.id.signin_text);
        privacyText = (TextView)view.findViewById(R.id.privacy_text);
        haveAccountText = (TextView)view.findViewById(R.id.have_account_text);


        haveAccountText.setText(getActivity().getResources().getString(R.string.already_account));

        welcomeText.setVisibility(View.GONE);
        chartSignupText.setVisibility(View.VISIBLE);
        privacyText.setVisibility(View.VISIBLE);
        signinText.setText(getActivity().getResources().getString(R.string.log_in));

        signinText.setOnClickListener(this);
        facebookButton.setOnClickListener(this);
        googleButton.setOnClickListener(this);
        emailButton.setOnClickListener(this);

        ImageView ivEye = view.findViewById(R.id.ivEye);
        ivEye.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPasswordHiden) {
                    edtPassword.setTransformationMethod(null);
                    ivEye.setImageResource(R.drawable.ic_show_eye);
                    isPasswordHiden = false;
                } else {
                    edtPassword.setTransformationMethod(new android.text.method.PasswordTransformationMethod());
                    ivEye.setImageResource(R.drawable.ic_hide_eye);
                    isPasswordHiden = true;
                }
                edtPassword.setSelection(edtPassword.getText().length());
            }
        });
        setSpannableStringPrivacyText();


        boolean loggedOut = AccessToken.getCurrentAccessToken() == null;
        if (!loggedOut) {
            try {
                LoginManager.getInstance().logOut();
            }catch (Exception e)
            {
                e.printStackTrace();
            }
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
    }

    private void setEmailIdFromUserAccount() {
        String email = UserEmailFetcher.getEmail(activity);
        if (!TextUtils.isEmpty(email)) {
            edtMailId.setText(email);
            setFocusOnEmail();
            edtPassword.setFocusableInTouchMode(true);
            edtPassword.setFocusable(true);
        } else {
            edtMailId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    requestCredentials();
                }
            });
        }
    }

    private void setSpannableStringPrivacyText() {

        try {

            String s = getActivity().getResources().getString(R.string.creating_account);
            int start=0, end=0;

            start = s.indexOf("#",0);
            end = s.lastIndexOf("#")-1;
            SpannableString ss = new SpannableString(s.replace("#",""));

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
                    intent.putExtra("URL", CGlobalVariables.ASTROSAGE_TERMS_CONDITIONS_URL);
                    intent.putExtra("TITLE_TO_SHOW", CGlobalVariables.terms_of_use_text);
                    startActivity(intent);
                }
            };
            ss.setSpan(span1, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            privacyText.setText(ss);
            privacyText.setLinkTextColor(getActivity().getResources().getColor(R.color.color_footer_grey));
            privacyText.setMovementMethod(LinkMovementMethod.getInstance());

        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private void setSpannableString() {
        SpannableString ss = new SpannableString(getResources().getString(R.string.agree_text));
        ClickableSpan span1 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
               /* Intent intent = new Intent(activity, ActDisclaimerAndPrivacyPolicy.class);
                intent.putExtra("screenType", 0);
                startActivity(intent);*/
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
                /*Intent intent = new Intent(activity, ActDisclaimerAndPrivacyPolicy.class);
                intent.putExtra("screenType", 1);
                startActivity(intent);*/
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

    /******************************************* EmailId dialogue start***************************/

    public void requestCredentials() {
        if (!isEmailDialogueOpened) {
            showGmailAccountPicker();
            //Auth.CredentialsApi.request(mGoogleApiClient, mCredentialRequest).setResultCallback(this);
        }
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
                        edtMailId.setText(selectedEmailId);
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

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
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
               // fbGoogleLayout.setVisibility(View.GONE);
               // signUpLayout.setVisibility(View.VISIBLE);
                break;

            case R.id.loginWithMobile_button:
                CUtils.googleAnalyticSendWitPlayServie(
                        getActivity(),
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK,
                        "Sign in with Mobile",
                        null);
                Intent intent1 = new Intent(requireActivity(), LoginSignUpActivity.class);
                intent1.putExtra(com.ojassoft.astrosage.varta.utils.CGlobalVariables.IS_FROM_SCREEN, com.ojassoft.astrosage.varta.utils.CGlobalVariables.SOCIAL_MEDIA_SIGN_IN_SCREEN);
                requireActivity().startActivity(intent1);
                break;
            case R.id.btnSignUp:
                registerUser();
                CUtils.hideMyKeyboard(activity);
                break;

            case R.id.signin_text:
            case R.id.btnBackPressed:
                CUtils.googleAnalyticSendWitPlayServie(
                        getActivity(),
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK,
                        CGlobalVariables.SIGNUP_SIGNIN_TEXT,
                        null);
                onBackPressedClick();
                break;

            default:
                break;
        }
    }

    /******************************************* EmailId dialogue end***************************/


    /*
     * @date : 11 jan 2016
     * @description : This method is used to register the user into server
     */
    private void registerUser() {

        if (validateData()) {

            emailId = edtMailId.getText().toString().trim();
            password = edtPassword.getText().toString().trim();

          /*  asyncTaskRegistrationOfUser = new AsyncTaskRegistrationOfUser();
            asyncTaskRegistrationOfUser.execute();*/
            if (CUtils.isConnectedWithInternet(activity)) {
                showProgressBar();
                //CUtils.userSignUp(FragSignUp.this, emailId, password, SIGN_UP);

                userName = CUtils.getUserNameFromEmailID(emailId);
                operationName = CGlobalVariables.OPERATION_NAME_SIGNUP;
                CUtils.verifyLoginWithUserPurchasedPlan(FragSignUp.this, emailId,
                        password, userName, operationName,
                        CGlobalVariables.REG_SOURCE_ANDROID,SIGN_UP);

            } else {

            }

        }
    }

    /*
     * @date : 11 jan 2016
     * @description : This method is used to validate user credentials
     */
    protected boolean validateUserCredentials() {
        boolean valid = true;
        emailId = edtMailId.getText().toString().trim();
        password = edtPassword.getText().toString().trim();

        if (TextUtils.isEmpty(emailId)) {
            valid = false;
            edtMailId.setError(getResources().getString(R.string.email_one_v));
            requestFocus(edtMailId);
        } else if (TextUtils.isEmpty(password)) {
            valid = false;
            edtPassword.setError(getResources().getString(R.string.enter_password));
            requestFocus(edtPassword);
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailId).matches()) {
            valid = false;
            edtMailId.setError(getResources().getString(R.string.email_three_v));
        }

        if (!CUtils.isConnectedWithInternet(activity)) {
            valid = false;
            MyCustomToast mct = new MyCustomToast(activity, activity.getLayoutInflater(), activity, ((ActLogin) activity).regularTypeface);
            mct.show(getResources().getString(R.string.no_internet));
        }
        return valid;
    }

    private boolean validateData() {
        boolean flag = false;
        if (validateName(edtMailId, edtMailId, getString(R.string.email_one_v)) && validateName(edtPassword, edtPassword, getString(R.string.enter_password)))
            flag = true;

        return flag;
    }

    /*
     * @date : 11 jan 2016
     * @description : This method is used to finish the current activity
     */
    private void onBackPressedClick() {
        //this.finish();
        iWizardLoginRegisterFragmentInterface.clickedSignUpButton(0);//0 for login
    }

    /*
     * @date : 11 jan 2016
     * @description : This Async task class is used to register the user into server
     */
    CustomProgressDialog pd = null;

   /* private class AsyncTaskRegistrationOfUser extends AsyncTask<String, String, String> {

        String[] status = new String[2];

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            pd = new CustomProgressDialog(activity, ((ActLogin) activity).regularTypeface);
            pd.show();
        }

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {
                String key = CUtils.getApplicationSignatureHashCode(activity);
                status = new ControllerManager()
                        .userSignUp(emailId, password, key);
            } catch (UICOnlineChartOperationException e) {
                //Log.i(e.getMessage().toString());
                status[0] = "0";
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            //Sign Up Successfully.
            // 1 FOR LOGIN SUCCESSFUL AND 8 FOR PLAN ACTIVATED
            if (status[0].equals("1") || status[0].equals("8")) {
                new LoginVerificationOnline().execute();
            } else {
                try {
                    if (pd != null & pd.isShowing())
                        pd.dismiss();
                } catch (Exception e) {
                    //Log.i(e.getMessage().toString());
                }

                //All Fields of sign up form are mandatory.
                if (status[0].equals("2")) {
                    showCustomMsg(getResources().getString(R.string.sign_up_validation_all_fields));
                }
                //User Id already exist.
                else if (status[0].equals("3")) {
                    showCustomMsg(getResources().getString(R.string.sign_up_validation_user_already_exists));
                    onBackPressedClick();
                }
                //Authentication failed.
                else if (status[0].equals("4")) {
                    showCustomMsg(getResources().getString(R.string.sign_up_validation_authentication_failed));
                } else {
                    //0 or other condition
                    //Sign Up Failed.
                    showCustomMsg(getResources().getString(R.string.sign_up_validation_failed));
                }

            }
        }
    }*/

   /* class LoginVerificationOnline extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {
                String key = CUtils.getApplicationSignatureHashCode(activity);
                iReturn = new ControllerManager()
                        .verifyLoginWithUserPurchasedPlan(emailId, password, key);
            } catch (UICOnlineChartOperationException e) {
                //Log.i(e.getMessage().toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            try {
                if (pd != null & pd.isShowing())
                    pd.dismiss();
            } catch (Exception e) {
                iReturn[0] = "-1";
                //Log.i(e.getMessage().toString());
            }
            CUtils.signUpFirebaseEvent();
            showCustomMsg(getResources().getString(R.string.sign_up_success));
            if (iReturn[0].equals("1")) {
                returnToMasterActivityAfterLogin(emailId, password, iReturn[25]);
            } else {
                onBackPressedClick();
            }
        }

    }*/

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
                case R.id.edtMailId:
                    validateName(edtMailId, edtMailId, getString(R.string.email_one_v));
                    break;
                case R.id.edtPassword:
                    validateName(edtPassword, edtPassword, getString(R.string.enter_password));

            }
        }
    }

    private boolean validateName(EditText name, EditText inputLayout, String message) {
        //String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (name == edtMailId) {
            if (name.getText().toString().trim().length() < 1) {
                inputLayout.setError(message);
                name.requestFocus();
             //   name.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                return false;
            } else if (name.getText().toString().trim().length() > 70) {
                inputLayout.setError(getResources().getString(R.string.email_two_v));
                name.requestFocus();
//                name.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                return false;
            } else if (!CUtils.isValidEmail(name.getText().toString().trim())) {
                inputLayout.setError(getResources().getString(R.string.email_three_v));
                name.requestFocus();
              //  name.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                return false;
            } else {
                inputLayout.setError(null);
              //  name.getBackground().setColorFilter(getResources().getColor(R.color.ColorPrimary), PorterDuff.Mode.SRC_ATOP);
            }
        } else if (name.getText().toString().trim().isEmpty()) {
            inputLayout.setError(message);
            name.requestFocus();
         //   name.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
            return false;
        } else {
            inputLayout.setError(null);
          //  name.getBackground().setColorFilter(getResources().getColor(R.color.ColorPrimary), PorterDuff.Mode.SRC_ATOP);
        }

        if (!CUtils.isConnectedWithInternet(activity)) {
            MyCustomToast mct = new MyCustomToast(activity, activity.getLayoutInflater(), activity, ((ActLogin) activity).regularTypeface);
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

    private void showCustomMsg(String msg) {

        MyCustomToast mct = new MyCustomToast(activity,
                activity.getLayoutInflater(), activity,
                ((ActLogin) activity).regularTypeface);
        mct.show(msg);
    }

    @Override
    public void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
        /*edtMailId.getBackground().setColorFilter(null);
        edtPassword.getBackground().setColorFilter(null);
        textInputEmail.setErrorEnabled(false);
        textInputPass.setErrorEnabled(false);*/
        //textInputEmail.setHintEnabled(false);
        //textInputPass.setHintEnabled(false);
        //typeface = CUtils.getUserSelectedLanguageFontType(getActivity(), LANGUAGE_CODE);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        iWizardLoginRegisterFragmentInterface = (IWizardLoginRegisterFragmentInterface) activity;
        this.activity = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        /*if (asyncTaskRegistrationOfUser != null && asyncTaskRegistrationOfUser.getStatus() == AsyncTask.Status.RUNNING) {
            asyncTaskRegistrationOfUser.cancel(true);
        }*/
        iWizardLoginRegisterFragmentInterface = null;
        activity = null;
    }

    private void returnToMasterActivityAfterLogin(String userLogin, String pwd, HashMap<String, String> profileInfo, String operatnName)
    {
        try {

            String userName = "", userPlanExpiryDate = "",
                    userPlanPurchaseDate = "", userPlanId = "1", mobile="", occupation="", maritalStatus="",
                    companyName="", address1="", address2="";

            if(profileInfo.containsKey(CGlobalVariables.USERID))
            {
                userLogin = profileInfo.get(CGlobalVariables.USERID);
            }

            if(profileInfo.containsKey(CGlobalVariables.USER_FIRSTNAME))
            {
                userName = profileInfo.get(CGlobalVariables.USER_FIRSTNAME);
            }

            if(profileInfo.containsKey(CGlobalVariables.USERPLAN_ID));
            {
                if(profileInfo.get(CGlobalVariables.USERPLAN_ID).length()>0) {
                    userPlanId = profileInfo.get(CGlobalVariables.USERPLAN_ID);
                }
            }
            if(profileInfo.containsKey(CGlobalVariables.USER_PLAN_EXPIRY_DATE));
            {
                userPlanExpiryDate = profileInfo.get(CGlobalVariables.USER_PLAN_EXPIRY_DATE);
            }
            if(profileInfo.containsKey(CGlobalVariables.USER_PLAN_PURCHASE_DATE));
            {
                userPlanPurchaseDate = profileInfo.get(CGlobalVariables.USER_PLAN_PURCHASE_DATE);
            }
            GmailAccountInfo gmailAccountInfo = new GmailAccountInfo();
            if(operatnName.equalsIgnoreCase(CGlobalVariables.OPERATION_NAME_SOCIALMEDIA))
            {
                if(profileInfo.containsKey(CGlobalVariables.MOBILE))
                {
                    mobile = profileInfo.get(CGlobalVariables.MOBILE);
                }
                if(profileInfo.containsKey(CGlobalVariables.OCCUPATION))
                {
                    occupation = profileInfo.get(CGlobalVariables.OCCUPATION);
                }
                if(profileInfo.containsKey(CGlobalVariables.MARITALSTATUS))
                {
                    maritalStatus = profileInfo.get(CGlobalVariables.MARITALSTATUS);
                }
                if(profileInfo.containsKey(CGlobalVariables.COMPANY_NAME));
                {
                    companyName = profileInfo.get(CGlobalVariables.COMPANY_NAME);
                }
                if(profileInfo.containsKey(CGlobalVariables.ADDRESS1));
                {
                    address1 = profileInfo.get(CGlobalVariables.ADDRESS1);
                }
                if(profileInfo.containsKey(CGlobalVariables.ADDRESS2));
                {
                    address2 = profileInfo.get(CGlobalVariables.ADDRESS2);
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
            iWizardLoginRegisterFragmentInterface.clickedSignInButton(userLogin,
                    pwd);

            // ADDED BY BIJENDRA ON 26-12-14 TO SAVE GCM REGISTRATION INFORMATION ON
            // SERVER
            int languageCode = ((AstrosageKundliApplication) activity
                    .getApplication()).getLanguageCode();
            String regid = CUtils.getRegistrationId(activity
                    .getApplicationContext());
            new ControllerManager().saveUserGCMRegistrationInformationOnOjasServer(
                    activity.getApplicationContext(), regid, languageCode,
                    userLogin);

        }catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    private void returnToMasterActivityAfterLogin(String userLogin, String pwd, String userName) {
        CUtils.saveLoginDetailInPrefs(activity, userLogin, pwd, userName, true, false);
        // Clear old userid and password from old app
        SharedPreferences settings = activity.getSharedPreferences(
                CGlobalVariables.MYKUNDLI_PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(CGlobalVariables.PREF_USER_ID, "");
        editor.putString(CGlobalVariables.PREF_USERR_PWD, "");
        editor.commit();
        GmailAccountInfo gmailAccountInfo = new GmailAccountInfo();
        gmailAccountInfo.setId(userLogin);
        gmailAccountInfo.setUserName(userLogin.split("@")[0]);
        CUtils.saveGmailAccountInfo(activity, gmailAccountInfo);
        CUtils.storeUserPurchasedPlanInPreference(activity, Integer.parseInt(iReturn[1]));

        CUtils.saveStringData(activity, CGlobalVariables.PREF_USER_PLAN_PURCHASE_DATE, iReturn[2]);//Purchase plan Date
        CUtils.saveStringData(activity, CGlobalVariables.PREF_USER_PLAN_Expiry_DATE, iReturn[3]);//Expiry plan  Date
        iWizardLoginRegisterFragmentInterface.clickedSignInButton(userLogin,
                pwd);

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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        /*if (activity != null && activity instanceof BaseInputActivity && grantResults != null && grantResults.length > 0) {
            if (requestCode == BaseInputActivity.PERMISSION_CONTACTS && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setEmailIdFromUserAccount();
            } else {
                boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(activity, permissions[0]);
                if (!showRationale) {
                    CUtils.saveBooleanData(activity, CGlobalVariables.PERMISSION_KEY_CONTACTS, true);
                }
            }
        }*/
    }

   /* public void checkForPermission(boolean isUSerPermissionDialogOpen) {
        boolean hasEmailIdPermission = CUtils.isContactsPermissionGranted(activity, this, BaseInputActivity.PERMISSION_CONTACTS, isUSerPermissionDialogOpen);
        if (hasEmailIdPermission) {
            setEmailIdFromUserAccount();
        }
    }*/

    @Override
    public void onResponse(String response, int method) {
        if (activity == null || !isAdded()) {
            return;
        }
        Log.e("LOGIN DATA SIGNUP ",response);

        String respCode="";
        if(response != null && response.length()>0)
        {
            try {
                JSONObject respObj = new JSONObject(response);
                respCode = respObj.getString("msgcode");
                hideProgressBar();

                if(respCode.equals("10") || respCode.equals("20") || respCode.equals("22"))
                {
                    signUpUser(respObj, "");
                }else {

                    if(respCode.equals("2") || respCode.equals("60") ||
                            respCode.equals("50") || respCode.equals("1") ||
                            respCode.equals("3") || respCode.equals("5"))
                    {
                        if(operationName.equalsIgnoreCase(CGlobalVariables.OPERATION_NAME_SOCIALMEDIA))
                        {
                            signUpUser(respObj, CGlobalVariables.OPERATION_NAME_SOCIALMEDIA);
                        }else {
                            showCustomMsg(getResources().getString(R.string.sign_up_validation_user_already_exists));
                            onBackPressedClick();
                        }
                    }
                    else if(respCode.equals("4"))
                    {
                        showCustomMsg(getResources().getString(R.string.sign_up_validation_authentication_failed));
                    }
                    else
                    {
                        showCustomMsg(getResources().getString(R.string.sign_up_validation_failed));
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else{
            hideProgressBar();
            showCustomMsg(getResources().getString(R.string.server_error_msg));
        }
    }


    private void signUpUser(JSONObject responseObj, String operatnName)
    {
        CUtils.saveBooleanData(AstrosageKundliApplication.getAppContext(), CGlobalVariables.needToSendDeviceIdForLogin, false);
        CUtils.signUpFirebaseEvent();
        showCustomMsg(getResources().getString(R.string.sign_up_success));

        HashMap<String, String> jsonObjHash = CUtils.parseLoginSignupJson(responseObj);
        if(jsonObjHash != null && jsonObjHash.size()>0) {

            if(emailId == null || emailId.length()==0) {
                if (jsonObjHash.containsKey(CGlobalVariables.USERID)) {
                    emailId = jsonObjHash.get(CGlobalVariables.USERID);
                }
            }
            if(password == null || password.length()==0) {
                if (jsonObjHash.containsKey(CGlobalVariables.USER_PASSWORD)) {
                    password = jsonObjHash.get(CGlobalVariables.USER_PASSWORD);
                }
            }
            returnToMasterActivityAfterLogin(emailId, password, jsonObjHash, operatnName);
        }else
        {
            onBackPressedClick();
        }
    }


    @Override
    public void onError(VolleyError error) {
        if (activity != null && isAdded()) {
            hideProgressBar();
            if (error != null && error.getMessage() != null) {
                showCustomMsg(error.getMessage());
            }
        }
    }

    /**
     * show Progress Bar
     */
    private void showProgressBar() {
        if (activity != null) {
            if (pd == null) {
                pd = new CustomProgressDialog(activity, ((BaseInputActivity) activity).robotRegularTypeface);
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
        try {
            if (activity != null) {
                if (pd != null & pd.isShowing())
                    pd.dismiss();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void signIn() {
        launchCredentialManager();
    }

    private void updateUI(FirebaseUser accountDataObj) {
        Log.e("CredentialManager", "updateUI accountDataObj="+accountDataObj);
        try {

            if (accountDataObj != null) {

                userName = accountDataObj.getDisplayName();
                emailId = accountDataObj.getEmail();
                Log.e("CredentialManager", "updateUI userName="+userName);
                Log.e("CredentialManager", "updateUI emailId="+emailId);
                operationName = CGlobalVariables.OPERATION_NAME_SOCIALMEDIA;
                showProgressBar();
                CUtils.verifyLoginWithUserPurchasedPlan(FragSignUp.this, emailId,
                        "", userName, operationName,
                        CGlobalVariables.REG_SOURCE_ANDROID_GOOGLE,SIGN_UP);



            }
        }catch (Exception e) {
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
                            if(object == null) return;
                            String first_name = object.getString("first_name");
                            String last_name = object.getString("last_name");
                            String email = object.getString("email");
                            String id = object.getString("id");
                            String image_url = "https://graph.facebook.com/" + id + "/picture?type=normal";
                            if(TextUtils.isEmpty(email)) return;
                           /* Log.e("FACEBOOK ","first_name"+ first_name);
                            Log.e("FACEBOOK ","last_name"+ last_name);
                            Log.e("FACEBOOK ","email"+ email);
                            Log.e("FACEBOOK ","id"+ id);
                            Log.e("FACEBOOK ","image_url"+ image_url);*/

                            userName = first_name + " "+ last_name;
                            operationName = CGlobalVariables.OPERATION_NAME_SOCIALMEDIA;
                            showProgressBar();
                            CUtils.verifyLoginWithUserPurchasedPlan(FragSignUp.this, email,
                                    "", userName, operationName,
                                    CGlobalVariables.REG_SOURCE_ANDROID_FACEBOOK,SIGN_UP);

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
