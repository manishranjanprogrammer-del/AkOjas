package com.ojassoft.astrosage.ui.fragments;

import static com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;

import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.NonNull;
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
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

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
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.Task;
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
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.varta.ui.activity.LoginSignUpActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.Executors;

public class WizardLogingRegister extends Fragment implements VolleyResponse, OnClickListener {

    IWizardLoginRegisterFragmentInterface iWizardLoginRegisterFragmentInterface;
    EditText etLogin, etPass;
    // int iReturn = -1;
    String[] iReturn;
    LinearLayout llAstroSageLoginCreateAccount = null;
    ScrollView llAstroSageLogin = null;
    Button signUp, backButton, signIn;
    //RelativeLayout topLine;
    ProgressBar progressBar;
    RelativeLayout.LayoutParams params;
    RelativeLayout relativeView;
    TextView textViewNote1, textViewNote2;

    Button textShowStatus, buttonSkipSignInForEver;
    LinearLayout usernamePassword_container;
    TextInputLayout textInputEmail, textInputPass;
    String url;
    public int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    public Typeface typeface;
    //	private TextView heading;
    //private TextView textViewNotes;
    private Activity activity;
    CustomProgressDialog pd;
    String _userId = "", _pwd = "", userName="";
    Button skipSignIn;
    TextView textViewForgetPass, textViewSkip;
    SignInButton googleSignInButton;
    private static final int RC_SIGN_IN = 9001;
    LoginButton facebookLoginButton;
    private static final String EMAIL = "email";
    CallbackManager callbackManager;
    RelativeLayout fbGoogleLayout;
    LinearLayout loginLayout, facebookButton, googleButton, emailButton,signUpWithMobile;
    TextView welcomeText, chartSignupText, signinText, privacyText, haveAccountText;
    private CredentialManager credentialManager;
    private FirebaseAuth mAuth;

    private static final int MAX_RETRY_ATTEMPTS = 3;
    private int retryCount = 0;
    private boolean isPasswordHiden = true;

    public WizardLogingRegister() {
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // LANGUAGE_CODE = CUtils.getLanguageCodeFromPreference(getActivity());
        LANGUAGE_CODE = ((AstrosageKundliApplication) getActivity()
                .getApplication()).getLanguageCode();// ADDED BY HEVENDRA ON
        // 24-12-2014
        typeface = CUtils.getRobotoFont(getActivity(),
                LANGUAGE_CODE, CGlobalVariables.regular);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.lay_wizard_login, container,
                false);

        printHashKey(getActivity());

        textViewNote1 = (TextView) view.findViewById(R.id.tvNote1);
        textViewNote1.setTypeface(typeface);
        CUtils.setClickableSpan(textViewNote1,
                Html.fromHtml(getString(R.string.login_note1)), getActivity(), typeface);
        textViewNote2 = (TextView) view.findViewById(R.id.tvNote2);
        textViewNote2.setTypeface(typeface);
        CUtils.setClickableSpan(textViewNote2,
                Html.fromHtml(getString(R.string.login_note2)), getActivity(), typeface);

        usernamePassword_container = (LinearLayout) view
                .findViewById(R.id.usernamePassword_container);
        llAstroSageLogin = (ScrollView) view.findViewById(R.id.scrollViewLogin);
        llAstroSageLoginCreateAccount = (LinearLayout) view
                .findViewById(R.id.llAstroSageLoginCreateAccount);
        // topLine = (RelativeLayout) view.findViewById(R.id.topLine);
        backButton = (Button) view.findViewById(R.id.buttonBack);
        backButton.setTypeface(typeface);
        TextView loginTitleTV = view.findViewById(R.id.font_auto_lay_wizard_login_1);
        TextView tvFacebook = view.findViewById(R.id.tv_facebook);
        TextView tvGmail = view.findViewById(R.id.tv_gmail);
        TextView tvEmail = view.findViewById(R.id.tv_email);
        FontUtils.changeFont(requireContext(), loginTitleTV, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        tvFacebook.setTypeface(CUtils.getRobotoMedium(requireContext()));
        tvGmail.setTypeface(CUtils.getRobotoMedium(requireContext()));
        tvEmail.setTypeface(CUtils.getRobotoMedium(requireContext()));
        textInputEmail = (TextInputLayout) view.findViewById(R.id.input_layout_email);
        textInputPass = (TextInputLayout) view.findViewById(R.id.input_layout_pass);

        ImageView ivEye = view.findViewById(R.id.ivEye);
        ivEye.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isPasswordHiden) {
                    etPass.setTransformationMethod(null);
                    ivEye.setImageResource(R.drawable.ic_show_eye);
                    isPasswordHiden = false;
                } else {
                    etPass.setTransformationMethod(new android.text.method.PasswordTransformationMethod());
                    ivEye.setImageResource(R.drawable.ic_hide_eye);
                    isPasswordHiden = true;
                }
                etPass.setSelection(etPass.getText().length());
            }
        });
        backButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                gotoBackScreen();
            }
        });

		/*heading = (TextView) view.findViewById(R.id.textViewHeading);
        heading.setTypeface(typeface);*/
        TextView textViewUserName = (TextView) view
                .findViewById(R.id.textViewUserName);
        textViewUserName.setTypeface(typeface);
        TextView textViewPass = (TextView) view.findViewById(R.id.textViewPass);
        textViewPass.setTypeface(typeface);
        textViewForgetPass = (TextView) view
                .findViewById(R.id.textViewForgetPass);
        textViewForgetPass.setText(getString(R.string.forget_pass_tag)+"?");
        textViewForgetPass.setTypeface(typeface);

        // ADDED BY BIJENDRA ON 17-04-15
        textViewSkip = (TextView) view.findViewById(R.id.textViewSkip);
        textViewSkip.setTypeface(typeface);
        // END

		/*textViewNotes = (TextView) view.findViewById(R.id.textViewNotes);
        textViewNotes.setTypeface(typeface);
		// textViewNotes.setMovementMethod(LinkMovementMethod.getInstance());//DISABLED
		// BY DEEPAK ON 2-12-2014
		CUtils.setClickableSpan(textViewNotes,
				Html.fromHtml(getString(R.string.login_note)),activity,typeface);*/
        signIn = (Button) view.findViewById(R.id.buttonSignIn);
        signIn.setTypeface(typeface);

       /* if (LANGUAGE_CODE==0){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                signIn.setAllCaps(true);
                backButton.setAllCaps(true);
            }
            }*/

        signUp = (Button) view.findViewById(R.id.buttonSignUp);
        signUp.setTypeface(typeface);
        skipSignIn = (Button) view.findViewById(R.id.buttonSkipSignIn);
        skipSignIn.setTypeface(typeface);
        buttonSkipSignInForEver = (Button) view
                .findViewById(R.id.buttonSkipSignInForEver);
        buttonSkipSignInForEver.setTypeface(typeface);

       /*if (iWizardLoginRegisterFragmentInterface.hideSkipButton()) {
            buttonSkipSignInForEver.setVisibility(View.GONE);

        } else {buttonSkipSignInForEver
            buttonSkipSignInForEver.setVisibility(View.VISIBLE);
        }*/

        etLogin = (EditText) view.findViewById(R.id.etUserName);
        etLogin.setTypeface(((BaseInputActivity) getActivity()).robotRegularTypeface);
        etPass = (EditText) view.findViewById(R.id.etPass);
        etPass.setTypeface(((BaseInputActivity) getActivity()).robotRegularTypeface);

        etPass.addTextChangedListener(new MyTextWatcher(etPass));
        etLogin.addTextChangedListener(new MyTextWatcher(etLogin));

        signIn.setOnClickListener(this);
        signUp.setOnClickListener(this);
        skipSignIn.setOnClickListener(this);
        buttonSkipSignInForEver.setOnClickListener(this);
        textViewSkip.setOnClickListener(this);
        textViewForgetPass.setOnClickListener(this);

        if (((AstrosageKundliApplication) activity.getApplication())
                .getLanguageCode() == CGlobalVariables.ENGLISH) {
            backButton.setText(getResources().getString(R.string.back_button).toUpperCase());
            buttonSkipSignInForEver.setText(getResources().getString(R.string.do_not_show_this_screen).toUpperCase());
            signUp.setText(getResources().getString(R.string.button_new_user).toUpperCase());
            signIn.setText(getResources().getString(R.string.button_sign_in).toUpperCase());
            skipSignIn.setText(getResources().getString(R.string.use_app_without_sign_in).toUpperCase());
        }

        fbGoogleLayout = (RelativeLayout)view.findViewById(R.id.fb_google_layout);
        loginLayout = (LinearLayout)view.findViewById(R.id.login_layout);

        //fbGoogleLayout.setVisibility(View.VISIBLE);
        //loginLayout.setVisibility(View.GONE);

        facebookButton = (LinearLayout)view.findViewById(R.id.facebook_button);
        googleButton = (LinearLayout)view.findViewById(R.id.google_button);
        emailButton = (LinearLayout)view.findViewById(R.id.email_button);
        signUpWithMobile = (LinearLayout)view.findViewById(R.id.loginWithMobile_button);
        welcomeText = (TextView) view.findViewById(R.id.welcome_text);
        chartSignupText = (TextView)view.findViewById(R.id.chart_signup_text);
        signinText = (TextView)view.findViewById(R.id.signin_text);
        privacyText = (TextView)view.findViewById(R.id.privacy_text);
        haveAccountText = (TextView)view.findViewById(R.id.have_account_text);

        haveAccountText.setText(getActivity().getResources().getString(R.string.dont_have_account));
        welcomeText.setVisibility(View.VISIBLE);
        chartSignupText.setVisibility(View.GONE);
        privacyText.setVisibility(View.INVISIBLE);
        signinText.setText(getActivity().getResources().getString(R.string.sign_up));
        welcomeText.setTypeface(CUtils.getRobotoMedium(requireContext()));
        signinText.setOnClickListener(this);
        facebookButton.setOnClickListener(this);
        googleButton.setOnClickListener(this);
        emailButton.setOnClickListener(this);
        signUpWithMobile.setOnClickListener(this);

        boolean loggedOut = AccessToken.getCurrentAccessToken() == null;
        if (!loggedOut) {
            try {
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

    @Override
    public void onStop() {
        super.onStop();

    }

    private boolean validateData() {
        boolean flag = false;
        if (validateName(etLogin, textInputEmail, getString(R.string.email_login)) && validateName(etPass, textInputPass, getString(R.string.enter_password)))
            flag = true;

        return flag;
    }
    // ADDED BY DEEPAK ON 2-12-2014
	/*private void setClickableSpan(TextView textView, CharSequence charSequence) {

		SpannableStringBuilder strBuilder = new SpannableStringBuilder(
				charSequence);

		// Get an array of URLSpan from SpannableStringBuilder object
		URLSpan[] urlSpans = strBuilder.getSpans(0, strBuilder.length(),
				URLSpan.class);

		// Add onClick listener for each of URLSpan object
		for (final URLSpan span : urlSpans) {
			int start = strBuilder.getSpanStart(span);
			int end = strBuilder.getSpanEnd(span);
			strBuilder.removeSpan(span);
			strBuilder.setSpan(new ClickableSpan() {
				@Override
				public void onClick(View widget) {
					if (CUtils.isConnectedWithInternet(getActivity())) {
						Intent intent = new Intent(Intent.ACTION_VIEW, Uri
								.parse(span.getURL()));
						startActivity(intent);
					} else {
						MyCustomToast mct2 = new MyCustomToast(getActivity(),
								getActivity().getLayoutInflater(),
								getActivity(), typeface);
						mct2.show(getResources()
								.getString(R.string.no_internet));
					}
				}
			}, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
		}
		textView.setText(strBuilder);
		// No action if this is not set
		textView.setMovementMethod(LinkMovementMethod.getInstance());
	}*/

    // END ON 2-12-2014

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        // When fragment become visible we will fetch user id and password from
        // old preference.
        // and after setting old user id and password in new preference user get
        // logged in and clear old
        // preferences user id password.
        // I enclosed following code in try catch,
        try {
            if (activity != null && isVisibleToUser) {
                getUserIdAndPasswordFromOldApp();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getUserIdAndPasswordFromOldApp() {
        SharedPreferences settings = getActivity().getSharedPreferences(
                CGlobalVariables.MYKUNDLI_PREFS_NAME, 0);
        final String oldUserId = settings.getString(
                CGlobalVariables.PREF_USER_ID, "");
        final String oldPassWord = settings.getString(
                CGlobalVariables.PREF_USERR_PWD, "");
        if (oldUserId.trim().length() > 0 && oldPassWord.trim().length() > 0) {
            etLogin.setText(oldUserId);
            etPass.setText(oldPassWord);
            etPass.postDelayed(new Runnable() {
                @Override
                public void run() {
                    loginVerification(oldUserId, oldPassWord);
                    /*new LoginVerificationOnline(oldUserId, oldPassWord)
                            .execute();*/
                }
            }, 1000);
        }
    }

    protected void gotoBackScreen() {
        llAstroSageLogin.setVisibility(View.VISIBLE);
        // topLine.setVisibility(View.VISIBLE);
        llAstroSageLoginCreateAccount.setVisibility(View.GONE);
        // iWizardLoginRegisterFragmentInterface.applyBackButon(false);

        //heading.setVisibility(View.VISIBLE);
    }

    public void hideWebLayout() {
        gotoBackScreen();
    }

    private void shakeMyViewOnSignFailed(View viewToAnimate) {
        Animation shake = AnimationUtils.loadAnimation(getActivity(),
                R.anim.shake);
        viewToAnimate.startAnimation(shake);
    }

    public void goToCreateAccount() {
		/*llAstroSageLogin.setVisibility(View.GONE);
		topLine.setVisibility(View.GONE);
		heading.setVisibility(View.GONE);
		llAstroSageLoginCreateAccount.setVisibility(View.VISIBLE);
		iWizardLoginRegisterFragmentInterface.applyBackButon(true);

		showWebView(CGlobalVariables.REGISTRATION_URL);*/
        iWizardLoginRegisterFragmentInterface.clickedSignUpButton(1);//1 for sign up
    }

    public void goToForgotPassword() {
       /* llAstroSageLogin.setVisibility(View.GONE);
        topLine.setVisibility(View.GONE);
        //heading.setVisibility(View.GONE);
        llAstroSageLoginCreateAccount.setVisibility(View.VISIBLE);
        iWizardLoginRegisterFragmentInterface.applyBackButon(true);
        showWebView(CGlobalVariables.FORGOT_PASSWORD_URL);*/

        Intent intent = new Intent(getActivity(), ActForgetPassword.class);
        startActivity(intent);

    }

    private void initProgressBar() {
        progressBar = new ProgressBar(getActivity(), null,
                android.R.attr.progressBarStyle);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);

        textShowStatus = new Button(getActivity(), null,
                android.R.attr.buttonStyle);

        params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.MATCH_PARENT,
                RelativeLayout.LayoutParams.MATCH_PARENT);
        relativeView = new RelativeLayout(getActivity());
        relativeView.setLayoutParams(params);
        relativeView.setGravity(Gravity.CENTER);
    }

    private void initWebView() {

    }

    protected boolean validateUsernamePass() {
        boolean valid = true;
        if (!(etLogin.getText().toString().trim().length() > 0)) {
            valid = false;
            etLogin.setError(getResources().getString(R.string.email_one_v));
            requestFocus(etLogin);
        } else if (!(etPass.getText().toString().trim().length() > 0)) {
            valid = false;
            etPass.setError(getResources().getString(R.string.enter_password));
            requestFocus(etPass);
        }
        if (!CUtils.isConnectedWithInternet(getActivity())) {
            valid = false;
            MyCustomToast mct = new MyCustomToast(getActivity(), getActivity()
                    .getLayoutInflater(), getActivity(), typeface);
            mct.show(getResources().getString(R.string.no_internet));
        }
        return valid;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            case R.id.buttonSignIn:
                if (validateData()) {
                    CUtils.googleAnalyticSendWitPlayServie(
                            getActivity(),
                            CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                            CGlobalVariables.GOOGLE_ANALYTIC_ACTION_ASTROSAGE_LOGIN,
                            null);
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_ACTION_ASTROSAGE_LOGIN, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                    goToSignIn();
                    CUtils.hideMyKeyboard(getActivity());
                }
                break;


            case R.id.buttonSignUp:
                CUtils.googleAnalyticSendWitPlayServie(
                        getActivity(),
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_ACTION_ASTROSAGE_CLOUD_REGISTRATION,
                        null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_ACTION_ASTROSAGE_CLOUD_REGISTRATION, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                goToCreateAccount();
                CUtils.hideMyKeyboard(getActivity());
                break;

            case R.id.buttonSkipSignInForEver:
                CUtils.saveLoginScreenHideInPrefs(getActivity(), true);
                CUtils.googleAnalyticSendWitPlayServie(
                        getActivity(),
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_ACTION_ASTROSAGE_LOGIN_CANCEL,
                        null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_ACTION_ASTROSAGE_LOGIN_CANCEL, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                iWizardLoginRegisterFragmentInterface.clickedSkipSignInButton();
                break;

            case R.id.buttonSkipSignIn:
                CUtils.googleAnalyticSendWitPlayServie(
                        getActivity(),
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_ACTION_ASTROSAGE_LOGIN_SKIP,
                        null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_ACTION_ASTROSAGE_LOGIN_SKIP, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                iWizardLoginRegisterFragmentInterface.clickedSkipSignInButton();
                break;

            case R.id.textViewForgetPass:
                CUtils.googleAnalyticSendWitPlayServie(
                        getActivity(),
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_ACTION_ASTROSAGE_LOGIN_FORGET_PWD,
                        null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_ACTION_ASTROSAGE_LOGIN_FORGET_PWD, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                goToForgotPassword();
                break;


            case R.id.textViewSkip:
                CUtils.googleAnalyticSendWitPlayServie(
                        getActivity(),
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_ACTION_ASTROSAGE_LOGIN_CANCEL,
                        null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_ACTION_ASTROSAGE_LOGIN_CANCEL, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                iWizardLoginRegisterFragmentInterface.clickedSkipSignInButton();
                break;

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
                    MyCustomToast mct = new MyCustomToast(getActivity(),
                            getActivity().getLayoutInflater(), getActivity(),
                            typeface);
                    mct.show(getResources().getString(R.string.no_internet));
                }
                break;

            case R.id.email_button:
                CUtils.googleAnalyticSendWitPlayServie(
                        getActivity(),
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK,
                        CGlobalVariables.EMAIL_LOGIN,
                        null);
               // fbGoogleLayout.setVisibility(View.GONE);
                loginLayout.setVisibility(View.VISIBLE);
                break;

            case R.id.loginWithMobile_button:
                CUtils.googleAnalyticSendWitPlayServie(
                        getActivity(),
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK,
                        "Sign Up with Mobile",
                        null);
                Intent intent1 = new Intent(requireActivity(), LoginSignUpActivity.class);
                intent1.putExtra(com.ojassoft.astrosage.varta.utils.CGlobalVariables.IS_FROM_SCREEN, com.ojassoft.astrosage.varta.utils.CGlobalVariables.SOCIAL_MEDIA_SIGN_UP_SCREEN);
                requireActivity().startActivity(intent1);
                break;

            case R.id.signin_text:
                CUtils.googleAnalyticSendWitPlayServie(
                        getActivity(),
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK,
                        CGlobalVariables.LOGIN_SIGNUP_TEXT,
                        null);
                goToCreateAccount();
                break;

            default:
                break;


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
                    validateName(etLogin, textInputEmail, getString(R.string.email_login));
                    break;
                case R.id.etPass:
                    validateName(etPass, textInputPass, getString(R.string.enter_password));
                    break;

            }
        }
    }

    private boolean validateName(EditText name, TextInputLayout inputLayout, String message) {
        if (name.getText().toString().trim().isEmpty()) {
            name.setError(message);
            requestFocus(name);
            //inputLayout.setError(message);
            //name.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
            return false;
        } else {
            //inputLayout.setErrorEnabled(false);
            //inputLayout.setError(null);
            //name.getBackground().setColorFilter(getResources().getColor(R.color.ColorPrimary), PorterDuff.Mode.SRC_ATOP);
        }
        if (!CUtils.isConnectedWithInternet(getActivity())) {
            MyCustomToast mct = new MyCustomToast(getActivity(), getActivity()
                    .getLayoutInflater(), getActivity(), typeface);
            mct.show(getResources().getString(R.string.no_internet));
            return false;
        }
        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        }
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.Fragment#onResume()
     */
    @Override
    public void onResume() {
        super.onResume();
        typeface = CUtils.getRobotoFont(getActivity(),
                LANGUAGE_CODE, CGlobalVariables.regular);
        /*etLogin.getBackground().setColorFilter(null);
        etPass.getBackground().setColorFilter(null);*/
       // textInputEmail.setHintEnabled(false);
       // textInputPass.setHintEnabled(false);
        /*textInputEmail.setErrorEnabled(false);
        textInputPass.setErrorEnabled(false);*/

    }

    public void goToSignIn() {
        String _userId = "", _pwd = "";
        _userId = etLogin.getText().toString();
        _pwd = etPass.getText().toString();
        loginVerification(_userId, _pwd);
        //new LoginVerificationOnline(_userId, _pwd).execute();
    }

    private void loginVerification(String oldUserId, String oldPassWord) {
        /*new LoginVerificationOnline(oldUserId, oldPassWord)
                .execute();*/
        _userId = oldUserId;
        _pwd = oldPassWord;
        if (CUtils.isConnectedWithInternet(activity)) {
            showProgressBar();
            CUtils.verifyLoginWithUserPurchasedPlan(WizardLogingRegister.this, oldUserId,
                    oldPassWord, userName, CGlobalVariables.OPERATION_NAME_LOGIN,
                    CGlobalVariables.REG_SOURCE_ANDROID,0);

        } else {
            MyCustomToast mct = new MyCustomToast(getActivity(),
                    getActivity().getLayoutInflater(), getActivity(),
                    typeface);
            mct.show(getResources().getString(R.string.no_internet));

        }

    }

   /* class LoginVerificationOnline extends AsyncTask<String, Long, Void> {
        //ProgressDialog pd = null;
        CustomProgressDialog pd = null;
        String _userId = "", _pwd = "", _msg = "";

        public LoginVerificationOnline(String userId, String pwd) {
            _userId = userId;
            _pwd = pwd;

        }

        @Override
        protected void onPreExecute() {
			*//*pd = ProgressDialog.show(getActivity(), null, getResources()
					.getString(R.string.msg_please_wait), true, false);
			TextView tvMsg = (TextView) pd.findViewById(android.R.id.message);
			tvMsg.setTypeface(typeface);
			tvMsg.setTextSize(20);*//*

     *//*pd = new ProgressDialog(getActivity());

			pd.requestWindowFeature(Window.FEATURE_NO_TITLE);
			pd.requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);
			pd.getWindow().requestFeature(Window.FEATURE_PROGRESS);
			pd.getWindow().requestFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

			View view = getActivity().getLayoutInflater().inflate(R.layout.progress_dialog_layout, null);
			TextView tvMessage = (TextView)view.findViewById(R.id.tvMessage);
			tvMessage.setText(getResources().getString(R.string.msg_please_wait));
			tvMessage.setTypeface(typeface);

			pd.setIndeterminate(true);
			pd.show();

			pd.setContentView(view);*//*

            pd = new CustomProgressDialog(getActivity(), typeface);
            pd.show();


			*//*if(android.os.Build.VERSION.SDK_INT >=android.os.Build.VERSION_CODES.LOLLIPOP && LANGUAGE_CODE==1){
				pd = ProgressDialog.show(getActivity(), null,
						getResources().getString(R.string.msg_please_wait_hindi), true,
						false);
				TextView tvMsg = (TextView) pd
						.findViewById(android.R.id.message);
			//	tvMsg.setTypeface(typeface);
				tvMsg.setTextSize(20);
			}else{
				pd = ProgressDialog.show(getActivity(), null,
					getResources().getString(R.string.msg_please_wait), true,
					false);
			TextView tvMsgs = (TextView) pd
					.findViewById(android.R.id.message);
			tvMsgs.setTypeface(typeface);
			tvMsgs.setTextSize(20);
			}*//*
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                String key = CUtils.getApplicationSignatureHashCode(getActivity());
                iReturn = new ControllerManager()
                        .verifyLoginWithUserPurchasedPlan(_userId, _pwd, key);
            } catch (UICOnlineChartOperationException e) {
                iReturn = new String[1];
                iReturn[0] = "-1";
                _msg = e.getMessage();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            try {
                if (pd != null & pd.isShowing())
                    pd.dismiss();

                MyCustomToast mct1 = new MyCustomToast(getActivity(),
                        getActivity().getLayoutInflater(), getActivity(), typeface);

                //mct1.show("response = "+iReturn[0]);
                // 1 FOR LOGIN SUCCESSFUL AND 2 FOR PLAN ACTIVATED
                if (iReturn[0].equals("1") || iReturn[0].equals("2")) {
                    MyCustomToast mct = new MyCustomToast(getActivity(),
                            getActivity().getLayoutInflater(), getActivity(),
                            typeface);
                    mct.show(getResources().getString(R.string.sign_in_success));

                    //Checking is there any unconsumed data in google account
                    //CUtils.startServiceToConsumeProduct(getActivity());

                    returnToMasterActivityAfterLogin(_userId, _pwd, iReturn[25]);
                } else if (iReturn[0].equals("-1")) {
                    MyCustomToast mct = new MyCustomToast(getActivity(),
                            getActivity().getLayoutInflater(), getActivity(),
                            typeface);
                    mct.show(getResources().getString(R.string.no_internet));
                } else {
                    MyCustomToast mct = new MyCustomToast(getActivity(),
                            getActivity().getLayoutInflater(), getActivity(),
                            typeface);
                    mct.show(getResources().getString(R.string.sign_in_failed));
                    shakeMyViewOnSignFailed(usernamePassword_container);
                    //Password should be blank if user fill incorrect credencials
                    //etPass.setText("");
                }
            } catch (Exception e) {
                Log.i("", e.getMessage());
            }
        }

    }*/


    private void returnToMasterActivityAfterLogin(String userLogin, String pwd, HashMap<String, String> profileInfo) {
        try {

            String userName="", mobile="", occupation="", maritalStatus="",
                    companyName="", address1="", address2="", userPlanExpiryDate="",
                    userPlanPurchaseDate="", userPlanId="1";

            if(profileInfo.containsKey(CGlobalVariables.USERID))
            {
                userLogin = profileInfo.get(CGlobalVariables.USERID);
            }
            if(profileInfo.containsKey(CGlobalVariables.USER_FIRSTNAME))
            {
                userName = profileInfo.get(CGlobalVariables.USER_FIRSTNAME);
            }
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

            CUtils.saveLoginDetailInPrefs(getActivity(), userLogin, pwd, userName, true, false);

            // Clear old userid and password from old app
            SharedPreferences settings = getActivity().getSharedPreferences(
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
            CUtils.saveGmailAccountInfo(getActivity(), gmailAccountInfo);
            CUtils.storeUserPurchasedPlanInPreference(getActivity(), Integer.parseInt(userPlanId));

            CUtils.saveStringData(activity, CGlobalVariables.PREF_USER_PLAN_PURCHASE_DATE, userPlanPurchaseDate);//Purchase plan Date
            CUtils.saveStringData(activity, CGlobalVariables.PREF_USER_PLAN_Expiry_DATE, userPlanExpiryDate);//Expiry plan  Date
            iWizardLoginRegisterFragmentInterface.clickedSignInButton(userLogin,
                    pwd);

            // ADDED BY BIJENDRA ON 26-12-14 TO SAVE GCM REGISTRATION INFORMATION ON
            // SERVER
            int languageCode = ((AstrosageKundliApplication) getActivity()
                    .getApplication()).getLanguageCode();
            String regid = CUtils.getRegistrationId(getActivity()
                    .getApplicationContext());
            new ControllerManager().saveUserGCMRegistrationInformationOnOjasServer(
                    getActivity().getApplicationContext(), regid, languageCode,
                    userLogin);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void returnToMasterActivityAfterLogin(String userLogin, String pwd, String[] profileInfo) {
        CUtils.saveLoginDetailInPrefs(getActivity(), userLogin, pwd, profileInfo[0], true, false);
        // Clear old userid and password from old app
        SharedPreferences settings = getActivity().getSharedPreferences(
                CGlobalVariables.MYKUNDLI_PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(CGlobalVariables.PREF_USER_ID, "");
        editor.putString(CGlobalVariables.PREF_USERR_PWD, "");
        editor.commit();
        GmailAccountInfo gmailAccountInfo = new GmailAccountInfo();
        gmailAccountInfo.setId(userLogin);
        if (profileInfo[0].equals("")) {

            gmailAccountInfo.setUserName(userLogin.split("@")[0]);
        } else {
            gmailAccountInfo.setUserName(profileInfo[0]);
        }
        gmailAccountInfo.setMobileNo(profileInfo[1]);
        if(!TextUtils.isEmpty(profileInfo[2])){
            gmailAccountInfo.setOccupation(Integer.parseInt(profileInfo[2]));
        }else{
            gmailAccountInfo.setOccupation(0);
        }
        if(!TextUtils.isEmpty(profileInfo[3])){
            gmailAccountInfo.setMaritalStatus(Integer.parseInt(profileInfo[3]));
        }else{
            gmailAccountInfo.setMaritalStatus(0);
        }

        gmailAccountInfo.setHeading(profileInfo[4]);
        gmailAccountInfo.setAddress1(profileInfo[5]);
        gmailAccountInfo.setAddress2(profileInfo[6]);
        CUtils.saveGmailAccountInfo(getActivity(), gmailAccountInfo);
        CUtils.storeUserPurchasedPlanInPreference(getActivity(), Integer.parseInt(iReturn[1]));

        CUtils.saveStringData(activity, CGlobalVariables.PREF_USER_PLAN_PURCHASE_DATE, iReturn[2]);//Purchase plan Date
        CUtils.saveStringData(activity, CGlobalVariables.PREF_USER_PLAN_Expiry_DATE, iReturn[3]);//Expiry plan  Date
        iWizardLoginRegisterFragmentInterface.clickedSignInButton(userLogin,
                pwd);

        // ADDED BY BIJENDRA ON 26-12-14 TO SAVE GCM REGISTRATION INFORMATION ON
        // SERVER
        int languageCode = ((AstrosageKundliApplication) getActivity()
                .getApplication()).getLanguageCode();
        String regid = CUtils.getRegistrationId(getActivity()
                .getApplicationContext());
        new ControllerManager().saveUserGCMRegistrationInformationOnOjasServer(
                getActivity().getApplicationContext(), regid, languageCode,
                userLogin);

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
        iWizardLoginRegisterFragmentInterface = null;
        activity = null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public interface IWizardLoginRegisterFragmentInterface {
        public void clickedSignInButton(String userId, String password);

        public void clickedSignUpButton(int pos);

        public void clickedSkipSignInButton();

        public void clickedForgetPasswordButton();

        public boolean hideSkipButton();

        public void applyBackButon(boolean b);

    }

    @Override
    public void onResponse(String response, int method) {
        if (activity == null || !isAdded()) {
            return;
        }
        Log.e("LOGIN DATA ",response);

        String respCode="";
        if(response != null && response.length()>0)
        {
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
                        CUtils.saveBooleanData(AstrosageKundliApplication.getAppContext(), CGlobalVariables.needToSendDeviceIdForLogin, false);
                        MyCustomToast mct = new MyCustomToast(getActivity(),
                                getActivity().getLayoutInflater(), getActivity(),
                                typeface);
                        mct.show(getResources().getString(R.string.plan_activate_success));

                    }else if(respCode.equals("3")){
                        CUtils.saveBooleanData(AstrosageKundliApplication.getAppContext(), CGlobalVariables.needToSendDeviceIdForLogin, false);
                        MyCustomToast mct = new MyCustomToast(getActivity(),
                                getActivity().getLayoutInflater(), getActivity(),
                                typeface);
                        mct.show(getResources().getString(R.string.user_exist_record_not_found));
                    }else {
                        MyCustomToast mct = new MyCustomToast(getActivity(),
                                getActivity().getLayoutInflater(), getActivity(),
                                typeface);
                        mct.show(getResources().getString(R.string.sign_in_success));
                    }

                    HashMap<String, String> jsonObjHash = CUtils.parseLoginSignupJson(respObj);
                    if(jsonObjHash != null && jsonObjHash.size()>0) {

                        if(_userId == null || _userId.length()==0) {
                            if (jsonObjHash.containsKey(CGlobalVariables.USERID)) {
                                _userId = jsonObjHash.get(CGlobalVariables.USERID);
                            }
                        }
                        if(_pwd == null || _pwd.length()==0) {
                            if (jsonObjHash.containsKey(CGlobalVariables.USER_PASSWORD)) {
                                _pwd = jsonObjHash.get(CGlobalVariables.USER_PASSWORD);
                            }
                        }

                        returnToMasterActivityAfterLogin(_userId, _pwd, jsonObjHash);
                    }
                }else {
                    if(respCode.equals("40"))
                    {
                        MyCustomToast mct = new MyCustomToast(activity,
                                activity.getLayoutInflater(), activity,
                                ((BaseInputActivity) activity).regularTypeface);
                        mct.show(getResources().getString(R.string.email_doesnot_exist));
                    }else if(respCode.equals("30"))
                    {
                        MyCustomToast mct = new MyCustomToast(activity,
                                activity.getLayoutInflater(), activity,
                                ((BaseInputActivity) activity).regularTypeface);
                        mct.show(getResources().getString(R.string.password_not_match));
                    }else if(respCode.equals("4"))
                    {
                        MyCustomToast mct = new MyCustomToast(activity,
                                activity.getLayoutInflater(), activity,
                                ((BaseInputActivity) activity).regularTypeface);
                        mct.show(getResources().getString(R.string.sign_up_validation_authentication_failed));
                    }
                    else {
                        MyCustomToast mct = new MyCustomToast(getActivity(),
                                getActivity().getLayoutInflater(), getActivity(),
                                typeface);
                        mct.show(getResources().getString(R.string.sign_in_failed));
                    }
                    shakeMyViewOnSignFailed(usernamePassword_container);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else
        {
            hideProgressBar();
            MyCustomToast mct = new MyCustomToast(getActivity(),
                    getActivity().getLayoutInflater(), getActivity(),
                    typeface);
            mct.show(getResources().getString(R.string.server_error_msg));
        }


      /*  iReturn = CUtils.setVerifyingLoginValue(response);
        try {
            hideProgressBar();
            // 1 FOR LOGIN SUCCESSFUL AND 2 FOR PLAN ACTIVATED
            //5 plan is activated from some where else
            if (iReturn[0].equals("1") || iReturn[0].equals("2") || iReturn[0].equals("5")) {
                MyCustomToast mct = new MyCustomToast(getActivity(),
                        getActivity().getLayoutInflater(), getActivity(),
                        typeface);
                mct.show(getResources().getString(R.string.sign_in_success));

                //Checking is there any unconsumed data in google account
                //CUtils.startServiceToConsumeProduct(getActivity());
                String[] profileInfo={iReturn[25],iReturn[26],iReturn[27],iReturn[28],iReturn[29],iReturn[30],iReturn[31]};
                returnToMasterActivityAfterLogin(_userId, _pwd, profileInfo);
            } else {
                MyCustomToast mct = new MyCustomToast(getActivity(),
                        getActivity().getLayoutInflater(), getActivity(),
                        typeface);
                mct.show(getResources().getString(R.string.sign_in_failed));
                shakeMyViewOnSignFailed(usernamePassword_container);
                //Password should be blank if user fill incorrect credencials
                //etPass.setText("");
            }
        } catch (Exception e) {
            Log.i("", e.getMessage());
        }*/

    }

    @Override
    public void onError(VolleyError error) {
        if (activity != null && isAdded()) {
            hideProgressBar();
            if (error != null && error.getMessage() != null) {
                MyCustomToast mct = new MyCustomToast(activity, activity.getLayoutInflater(), activity, typeface);
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void updateUI(FirebaseUser accountDataObj) {
        try{
            if (accountDataObj != null) {

                userName = accountDataObj.getDisplayName();
                _userId = accountDataObj.getEmail();
                if(getContext()!=null)
                CUtils.saveAstroshopUserEmail(getContext(),_userId);
                CUtils.saveBooleanData(getContext(),CGlobalVariables.EMAIL_IS_VERIFIED,true);
//                Log.e(TAG, "updateUI: ", );
                showProgressBar();
                CUtils.verifyLoginWithUserPurchasedPlan(WizardLogingRegister.this, _userId,
                        "", userName, CGlobalVariables.OPERATION_NAME_SOCIALMEDIA,
                        CGlobalVariables.REG_SOURCE_ANDROID_GOOGLE,0);

            }
        }catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public static void printHashKey(Context pContext) {
        try {
            PackageInfo info = pContext.getPackageManager().getPackageInfo(pContext.getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String hashKey = new String(Base64.encode(md.digest(), 0));
                Log.i("USERIDD ", "printHashKey() Hash Key: " + hashKey);
            }
        } catch (NoSuchAlgorithmException e) {
            Log.e("USERIDD ", "printHashKey()", e);
        } catch (Exception e) {
            Log.e("USERIDD ", "printHashKey()", e);
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
                            /*Log.e("FACEBOOK ","first_name"+ first_name);
                            Log.e("FACEBOOK ","last_name"+ last_name);
                            Log.e("FACEBOOK ","email"+ email);
                            Log.e("FACEBOOK ","id"+ id);
                            Log.e("FACEBOOK ","image_url"+ image_url);*/

                            userName = first_name + " "+ last_name;

                            showProgressBar();
                            CUtils.verifyLoginWithUserPurchasedPlan(WizardLogingRegister.this, email,
                                    "", userName, CGlobalVariables.OPERATION_NAME_SOCIALMEDIA,
                                    CGlobalVariables.REG_SOURCE_ANDROID_FACEBOOK,0);

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
