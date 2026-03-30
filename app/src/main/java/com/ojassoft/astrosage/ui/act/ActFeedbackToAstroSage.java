package com.ojassoft.astrosage.ui.act;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;

import androidx.core.app.ActivityCompat;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.CustomCredential;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.LayoutInflater.Factory;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.libojassoft.android.misc.SendDataBackToComponent;
import com.libojassoft.android.utils.LibCGlobalVariables;
import com.libojassoft.android.utils.LibCUtils;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.jinterface.IPermissionCallback;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.misc.SendFeedback;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.UserEmailFetcher;
import com.ojassoft.astrosage.varta.utils.FontUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;

import static com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_CLASS_NAME;

//import com.google.analytics.tracking.android.EasyTracker;

//import com.actionbarsherlock.app.SherlockActivity;


/**
 * This activity is used to send user feed back to AstroSage server
 *
 * @author Hukum
 */
public class ActFeedbackToAstroSage extends BaseInputActivity implements IPermissionCallback, SendDataBackToComponent {

    public final Pattern EMAIL_ADDRESS_PATTERN = Pattern.compile("[a-zA-Z0-9\\+\\.\\_\\%\\-\\+]{1,256}" + "\\@" + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" + "(" + "\\." + "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" + ")+");
    public int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    public Typeface typeface;
    /**
     * Application variables
     */
    EditText _etName, _etEmailId, _etPhone, _etMsg;
    TabLayout tabLayout;
    Toolbar toolbar;
    TextInputLayout inputLayoutName, inputLayoutEmail, inputLayoutPhone, inputLayoutMessage;
    private TextView counter, tvHeading, tvName, tvEmail, tvPhone, tvMessage,txtViewErrorFullName,txtViewErrorEmail,txtViewErrorPhoneNumber,txtViewErrorMessage;
    int SEND_FEEDBACK = 0;
    private final TextWatcher mTextEditorWatcher = new TextWatcher() {
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        public void onTextChanged(CharSequence s, int start, int before, int count) {
            counter.setText(getResources().getString(R.string.startbracket) + String.valueOf(500 - s.length()) + " " + getResources().getString(R.string.charater) + getResources().getString(R.string.endbracket));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };
    private Button btnSend, btnCancel;
    private String activityName;
    private CustomProgressDialog pd;
    private static final int RC_READ = 1;
    private static final int RC_HINT_EMAIL = 2;
    private boolean isGoogleApiClientConnected;
    private boolean isEmailDialogueOpened;
    private String selectedEmailId;
    private ActFeedbackToAstroSage activity;
    private CredentialManager credentialManager;
    /*public ActFeedbackToAstroSage() {
        super(R.string.app_name);
    }
*/
    public ActFeedbackToAstroSage() {
        super(R.string.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_NO_TITLE); email
        //LANGUAGE_CODE = CUtils.getLanguageCodeFromPreference(this);
        LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication()).getLanguageCode();//ADDED BY HEVENDRA ON 24-12-2014
        typeface = CUtils.getRobotoFont(getApplicationContext(), LANGUAGE_CODE, CGlobalVariables.regular);
        setContentView(R.layout.lay_feedback);
        activity = ActFeedbackToAstroSage.this;

        toolbar = (Toolbar) findViewById(R.id.tool_barAppModule);
// Get the navigation icon drawable
        Drawable navIcon = ContextCompat.getDrawable(this, R.drawable.ic_back_arrow);

// Check if the drawable is not null
        if (navIcon != null) {
            // Tint the drawable with the desired color
            navIcon.setTint(ContextCompat.getColor(this, R.color.black));

            // Set the tinted drawable as the navigation icon
            toolbar.setNavigationIcon(navIcon);
        }
        tvHeading = (TextView) findViewById(R.id.feedbackToolbar).findViewById(R.id.tvTitle);
        tabLayout = (TabLayout) findViewById(R.id.feedbackToolbar).findViewById(R.id.tabs);
        tabLayout.setVisibility(View.GONE);

        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputLayoutPhone = (TextInputLayout) findViewById(R.id.input_layout_phone);
        inputLayoutMessage = (TextInputLayout) findViewById(R.id.input_layout_message);

        tvName = (TextView) findViewById(R.id.tvName);
        tvEmail = (TextView) findViewById(R.id.tvEmailId);
        tvPhone = (TextView) findViewById(R.id.tvPhone);
        tvMessage = (TextView) findViewById(R.id.tvMsg);
        TextView nameLabelTV = (TextView) findViewById(R.id.font_auto_lay_feedback_1);
        TextView emailLabelTV = (TextView) findViewById(R.id.font_auto_lay_feedback_2);
        TextView phoneLabelTV = (TextView) findViewById(R.id.font_auto_lay_feedback_3);
        TextView messageLabelTV = (TextView) findViewById(R.id.font_auto_lay_feedback_4);
        counter = (TextView) findViewById(R.id.textViewCharCount);
        btnSend = (Button) findViewById(R.id.butSend);
        btnCancel = (Button) findViewById(R.id.butCancel);

        txtViewErrorFullName = (TextView) findViewById(R.id.txtViewErrorFullName);
        txtViewErrorEmail = (TextView) findViewById(R.id.txtViewErrorEmail);
        txtViewErrorPhoneNumber = (TextView) findViewById(R.id.txtViewErrorPhoneNumber);
        txtViewErrorMessage = (TextView) findViewById(R.id.txtViewErrorMessage);
        FontUtils.changeFont(this, nameLabelTV, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(this, emailLabelTV, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(this, phoneLabelTV, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(this, messageLabelTV, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        setTypefaceOnScreenItems();
        this.setTitle("");

        _etName = (EditText) findViewById(R.id.etName);
        _etEmailId = (EditText) findViewById(R.id.etEmailId);


       /* boolean hasEmailIdPermission = CUtils.getBooleanData(this, CGlobalVariables.EmailId_Permission, false);
        if (hasEmailIdPermission) {
            setEmailIdFromUserAccount(false);
        } else {
            CUtils.showEmailIdPermissionDialog(this);
        }*/
        if (getIntent().getExtras() != null) {
            activityName = getIntent().getStringExtra(KEY_CLASS_NAME);
        }
        credentialManager = CredentialManager.create(this);
        setEmailIdFromUserAccount();

        tvHeading.setText(getString(R.string.feedback_form));

        //End of task
        _etPhone = (EditText) findViewById(R.id.etPhone);
        _etMsg = (EditText) findViewById(R.id.etMsg);

        /*if(LANGUAGE_CODE==0)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                btnSend.setAllCaps(true);
            }*/

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToSend(v);

            }
        });

//        _etMsg.addTextChangedListener(mTextEditorWatcher);

        _etName.addTextChangedListener(new MyTextWatcher(_etName));
        _etEmailId.addTextChangedListener(new MyTextWatcher(_etEmailId));
        _etPhone.addTextChangedListener(new MyTextWatcher(_etPhone));
        _etMsg.addTextChangedListener(new MyTextWatcher(_etMsg));

        counter.setText(getResources().getString(R.string.startbracket) + "500" + " " + getResources().getString(R.string.charater) + getResources().getString(R.string.endbracket));
        //setTextViewFact();
        setEditTextFontToEnglishOnly();

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);

    }

    private void setEmailIdFromUserAccount() {
        String email = UserEmailFetcher.getEmail(this);
        if (!TextUtils.isEmpty(email)) {
            _etEmailId.setText(email);
        } else {
            //_etEmailId.setFocusable(false);
            _etEmailId.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*if (isGoogleApiClientConnected) {
                        requestCredentials();
                    } else {
                        setFocusOnEmail();
                    }*/

                    //requestCredentials();
                }
            });
        }
    }

    /***************************** Gmail choose dialogue start **********************/
    public void requestCredentials() {
        if (!isEmailDialogueOpened) {
            showGmailAccountPicker();
            //Auth.CredentialsApi.request(mGoogleApiClient, mCredentialRequest).setResultCallback(this);
        }
    }

    public void showGmailAccountPicker() {
        // Configure Google ID option
        GetGoogleIdOption googleIdOption = new GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false) // Show all accounts
                .setServerClientId(getString(R.string.default_web_client_id)) // Required
                .build();

        // Build the request
        GetCredentialRequest request = new GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build();

        // Launch Credential Manager UI
        credentialManager.getCredentialAsync(
                this,
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
                runOnUiThread(() -> {
                    try {
                        CUtils.saveAstroshopUserEmail(activity, selectedEmailId);
                        _etEmailId.setText(selectedEmailId);
                    } catch (Exception e) {
                        //
                    }
                });
            }
        }catch (Exception e){
            //
        }
    }

    private void setFocusOnEmail() {
        runOnUiThread(() -> {
            _etEmailId.setFocusableInTouchMode(true);
            _etEmailId.setFocusable(true);
            _etEmailId.requestFocus();
        });
    }
    /***************************** Gmail choose dialogue end **********************/


    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onStart()
     */
    @Override
    protected void onStart() {
        super.onStart();
        //EasyTracker.getInstance().activityStart(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        typeface = CUtils.getRobotoFont(getApplicationContext(), CUtils.getLanguageCodeFromPreference(getApplicationContext()), CGlobalVariables.regular);
        desableColorFilter(_etName);
        desableColorFilter(_etEmailId);
        desableColorFilter(_etPhone);
        desableColorFilter(_etMsg);
        desableLayoutPro(inputLayoutName);
        desableLayoutPro(inputLayoutEmail);
        desableLayoutPro(inputLayoutPhone);
        desableLayoutPro(inputLayoutMessage);

    }

    private void desableLayoutPro(TextInputLayout inputLayout) {
        inputLayout.setHintEnabled(false);
        inputLayout.setErrorEnabled(false);
    }

    private void desableColorFilter(EditText editText) {
        editText.getBackground().setColorFilter(null);
    }

    private boolean validateData() {
        boolean flag = false;
        if (validateName(_etName, inputLayoutName, getString(R.string.please_enter_name_v)) && validateName(_etEmailId, inputLayoutEmail, getString(R.string.email_one_v)) && validateName(_etMsg, inputLayoutMessage, getString(R.string.feedback_message_one_v)) && validateName(_etPhone, inputLayoutPhone, getString(R.string.mandatory_fields)))
            flag = true;

        return flag;
    }

    @Override
    public void isEmailIdPermissionGranted(boolean isPermissionGranted) {
        //setEmailIdFromUserAccount(true);
    }


    private boolean validateName(EditText name, TextInputLayout inputLayout, String message) {
        boolean value = true;
        if (name == _etName) {
            if (name.getText().toString().trim().length() < 1) {
                //inputLayout.setError(message);
                //inputLayout.setErrorEnabled(true);
                //requestFocus(name);
                //name.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                txtViewErrorFullName.setText(message);
                txtViewErrorFullName.setVisibility(View.VISIBLE);
                value = false;
            } else if (name.getText().toString().trim().length() > 50) {
                //inputLayout.setError(getResources().getString(R.string.name_limit_v));
                //inputLayout.setErrorEnabled(true);
                //requestFocus(name);
               // name.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                txtViewErrorFullName.setText(getResources().getString(R.string.name_limit_v));
                txtViewErrorFullName.setVisibility(View.VISIBLE);
                value = false;
            } else {
                //inputLayout.setErrorEnabled(false);
                //inputLayout.setError(null);
                //name.getBackground().setColorFilter(getResources().getColor(R.color.ColorPrimary), PorterDuff.Mode.SRC_ATOP);
                txtViewErrorFullName.setVisibility(View.GONE);

            }
        }

        if (name == _etEmailId) {
            if (name.getText().toString().trim().length() < 1) {
                //inputLayout.setError(message);
                //inputLayout.setErrorEnabled(true);
                //requestFocus(name);
                //name.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                txtViewErrorEmail.setText(message);
                txtViewErrorEmail.setVisibility(View.VISIBLE);
                value = false;
            } else if (name.getText().toString().trim().length() > 70) {
                //inputLayout.setError(getResources().getString(R.string.email_two_v));
                //inputLayout.setErrorEnabled(true);
                //requestFocus(name);
                //name.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                txtViewErrorEmail.setText(getResources().getString(R.string.email_two_v));
                txtViewErrorEmail.setVisibility(View.VISIBLE);
                value = false;
            } else if (!checkEmail(name.getText().toString().trim())) {
                //inputLayout.setError(getResources().getString(R.string.email_three_v));
                //inputLayout.setErrorEnabled(true);
                //requestFocus(name);
                //name.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                txtViewErrorEmail.setText(getResources().getString(R.string.email_three_v));
                txtViewErrorEmail.setVisibility(View.VISIBLE);
                value = false;
            } else {
                //inputLayout.setErrorEnabled(false);
                //inputLayout.setError(null);
               // name.getBackground().setColorFilter(getResources().getColor(R.color.ColorPrimary), PorterDuff.Mode.SRC_ATOP);
                txtViewErrorEmail.setVisibility(View.GONE);
            }
        }

        if (name == _etPhone) {
            if (name.getText().toString().trim().isEmpty()) {
                //inputLayout.setError(getResources().getString(R.string.phone_validation_v));
                //inputLayout.setErrorEnabled(true);
                //requestFocus(name);
                //name.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                txtViewErrorPhoneNumber.setText(getResources().getString(R.string.phone_validation_v));
                txtViewErrorPhoneNumber.setVisibility(View.VISIBLE);
                value = false;
            }else if(name.getText().toString().trim().length() < 10){
                txtViewErrorPhoneNumber.setText(getResources().getString(R.string.phone_one_v_astro_service));
                txtViewErrorPhoneNumber.setVisibility(View.VISIBLE);
            }else {
                // inputLayout.setErrorEnabled(false);
                //  inputLayout.setError(null);
                //  name.getBackground().setColorFilter(getResources().getColor(R.color.ColorPrimary), PorterDuff.Mode.SRC_ATOP);
                txtViewErrorPhoneNumber.setVisibility(View.GONE);
            }
        }

        if (name == _etMsg) {
            if (name.getText().toString().trim().length() < 1) {
                //inputLayout.setError(message);
                //inputLayout.setErrorEnabled(true);
                //requestFocus(name);
                //name.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                txtViewErrorMessage.setText(message);
                txtViewErrorMessage.setVisibility(View.VISIBLE);
                value = false;
            } else if (name.getText().toString().trim().length() > 500) {
                //inputLayout.setError(getResources().getString(R.string.feedback_message_two_v));
                //inputLayout.setErrorEnabled(true);
                //requestFocus(name);
                //name.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                txtViewErrorMessage.setText(getResources().getString(R.string.feedback_message_two_v));
                txtViewErrorMessage.setVisibility(View.VISIBLE);
                value = false;
            } else {
                //inputLayout.setErrorEnabled(false);
                //inputLayout.setError(null);
               // name.getBackground().setColorFilter(getResources().getColor(R.color.ColorPrimary), PorterDuff.Mode.SRC_ATOP);
                txtViewErrorMessage.setVisibility(View.GONE);
            }
        }


        return value;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    /* (non-Javadoc)
     * @see com.actionbarsherlock.app.SherlockFragmentActivity#onStop()
     */
    @Override
    protected void onStop() {
        super.onStop();
        //EasyTracker.getInstance().activityStop(this);
    }

    private void setTypefaceOnScreenItems() {
        tvHeading.setTypeface(typeface);
        tvName.setTypeface(typeface);
        tvEmail.setTypeface(typeface);
        tvPhone.setTypeface(typeface);
        tvMessage.setTypeface(typeface);
        counter.setTypeface(typeface);
        btnSend.setTypeface(typeface);
        if (((AstrosageKundliApplication) this.getApplication()).getLanguageCode() == CGlobalVariables.ENGLISH) {
            btnSend.setText(getResources().getString(R.string.send).toUpperCase());
        }
        btnCancel.setTypeface(typeface);
    }

    private void setEditTextFontToEnglishOnly() {
        _etName.setTypeface(Typeface.DEFAULT);
        _etEmailId.setTypeface(Typeface.DEFAULT);
        _etPhone.setTypeface(Typeface.DEFAULT);
        _etMsg.setTypeface(Typeface.DEFAULT);
    }

    private void setTextViewFact() {
        getLayoutInflater().setFactory(new Factory() {
            @Override
            public View onCreateView(String name, Context context, AttributeSet attrs) {
                if (name.equalsIgnoreCase("TextView")) {
                    try {
                        LayoutInflater li = LayoutInflater.from(context);
                        final View view = li.createView(name, null, attrs);
                        new Handler().post(new Runnable() {
                            public void run() {
                                ((TextView) view).setTextSize(18);
                                ((TextView) view).setTypeface(typeface);
//	                                ((TextView) view).setTextColor(Color.BLACK);
                            }
                        });
                        return view;
                    } catch (InflateException e) {

                    } catch (ClassNotFoundException e) {

                    }
                }
                return null;
            }
        });
    }

    public void goToSend(View v) {
        btnSend.setEnabled(false);
        if (validateData()) {
            if (LibCUtils.isConnectedWithInternet(ActFeedbackToAstroSage.this)) {
                pd = new CustomProgressDialog(ActFeedbackToAstroSage.this, typeface);
                pd.show();
                //new SendFeedBackAsync().execute();
                new SendFeedback(ActFeedbackToAstroSage.this).sendFeedbackToServer(getparams(this), SEND_FEEDBACK);
                CUtils.hideMyKeyboard(ActFeedbackToAstroSage.this);
            } else {
                showCustomisedToastMessage(getResources().getString(R.string.internet_is_not_working));
            }
        }
        btnSend.setEnabled(true);
    }

    public void goToCancel(View v) {
        this.finish();
        ;
    }

    private void showCustomisedToastMessage(String msg) {
        MyCustomToast mct = new MyCustomToast(this, getLayoutInflater(), this, typeface);
        mct.show(msg);
    }

    /**
     * This function is used to validate the feedback form
     *
     * @return boolean
     * @author Bijendra(02 - Nov - 13)
     */
    private boolean validateForm() {
        boolean isValid = true;
        if (_etName.getText().toString().trim().length() < 1) {
            isValid = false;
            _etName.setError(getResources().getString(R.string.please_enter_name_v));
        } else if (_etName.getText().toString().trim().length() > 50) {
            isValid = false;
            _etName.setError(getResources().getString(R.string.name_limit_v));
//			_etName.setError("Name should be with in 50 characters...");
        }
        if (_etEmailId.getText().toString().trim().length() < 1) {
            isValid = false;
            _etEmailId.setError(getResources().getString(R.string.email_one_v));
//			_etEmailId.setError("Please Enter e-mail Id...");
        } else if (_etEmailId.getText().toString().trim().length() > 70) {
            isValid = false;
            _etEmailId.setError(getResources().getString(R.string.email_two_v));
            //_etEmailId.setError("e-mail should be with in 70 characters...");
        } else if (!checkEmail(_etEmailId.getText().toString().trim())) {
            _etEmailId.setError(getResources().getString(R.string.email_three_v));
//			_etEmailId.setError("Enter valid e-mail Id...");
            isValid = false;
        }
        if (_etMsg.getText().toString().trim().length() < 1) {
            isValid = false;
            _etMsg.setError(getResources().getString(R.string.feedback_message_one_v));
//			_etMsg.setError("Please write feedback message...");
        } else if (_etMsg.getText().toString().trim().length() > 500) {
            isValid = false;
            _etMsg.setError(getResources().getString(R.string.feedback_message_two_v));
//			_etMsg.setError("Message should be with in 500 characters...");
        }
        if (_etPhone.getText().toString().trim().length() > 1 && _etPhone.getText().toString().trim().length() < 10) {
            isValid = false;
            _etPhone.setError(getResources().getString(R.string.phone_validation_v));
//			_etPhone.setError("Enter 10 digit number...");
        }

        return isValid;
    }

    private boolean checkEmail(String email) {
        if (email == null) {
            return false;
        } else if (email.equals("")) {
            return true;
        } else {
            return EMAIL_ADDRESS_PATTERN.matcher(email).matches();
        }
    }

    /**
     * This function return the application name from which this activity is called
     *
     * @return String
     * @author Bijendra(02 - nov - 13)
     */
    private String getMyApplicationName() {
        ApplicationInfo info = getApplicationInfo();
        PackageManager p = this.getPackageManager();
        String appName = p.getApplicationLabel(info).toString();
        getApplicationContext().getPackageName();
        return appName + " ( " + getApplicationContext().getPackageName() + " )";
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       /* switch (requestCode) {
            case PERMISSION_CONTACTS:
                setEmailIdFromUserAccount(false);
                break;
        }*/
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults != null && grantResults.length > 0) {
            if (requestCode == PERMISSION_CONTACTS && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //setEmailIdFromUserAccount(false);
            } else {
                boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0]);
                if (!showRationale) {
                    CUtils.saveBooleanData(this, CGlobalVariables.PERMISSION_KEY_CONTACTS, true);
                }
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

        public void onTextChanged(CharSequence s, int i, int i1, int i2) {
            counter.setText(getResources().getString(R.string.startbracket) + String.valueOf(500 - s.length()) + " " + getResources().getString(R.string.charater) + getResources().getString(R.string.endbracket));

        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.etName:
                    validateName(_etName, inputLayoutName, getString(R.string.please_enter_name_v));
                    break;
                case R.id.etEmailId:
                    validateName(_etEmailId, inputLayoutEmail, getString(R.string.email_one_v));
                    break;
                case R.id.etPhone:
                    validateName(_etPhone, inputLayoutPhone, getString(R.string.mandatory_fields));
                    break;
                case R.id.etMsg:
                    validateName(_etMsg, inputLayoutMessage, getString(R.string.feedback_message_one_v));
                    break;

            }
        }
    }

    private Map<String, String> getparams(Context ctx) {
        SharedPreferences sharedPreferencesForLang = (ActFeedbackToAstroSage.this).getSharedPreferences(CGlobalVariables.MYKUNDLI_LANGUAGE_PREFS_NAME, Context.MODE_PRIVATE);
        int selectedLanguage = sharedPreferencesForLang.getInt(CGlobalVariables.APP_PREFS_AppLanguage, 0);
        String language = "";
        if (selectedLanguage == 0) {
            language = "ENGLISH";
        } else if (selectedLanguage == 1) {
            language = "HINDI";
        } else if (selectedLanguage == 2) {
            language = "TAMIL";
        } else {
            language = "";
        }
        String appVerName = LibCUtils.getApplicationVersionToShow(ActFeedbackToAstroSage.this);
        String name = _etName.getText().toString();
        String emailId = _etEmailId.getText().toString();
        String phone = _etPhone.getText().toString();
        String message = _etMsg.getText().toString();
        String apppName = getMyApplicationName();

        Map<String, String> params = new HashMap<>();
        params.put("feedbackfrom", apppName);
        ;
        params.put("appvesrion", appVerName);//ADDED BY BIJENDRA ON 01-MAY-15nameValuePairs.add(new BasicNameValuePair("appvesrion", appVerName));//ADDED BY BIJENDRA ON 01-MAY-15
        params.put("key", CUtils.getApplicationSignatureHashCode(ctx));
        params.put("feedbackpersonname", name);
        params.put(CGlobalVariables.KEY_EMAIL_ID, CUtils.replaceEmailChar(emailId));
        params.put("phoneno", phone);
        params.put("message", message);
        params.put("modelname", android.os.Build.MODEL);//ADDED BY TEJINDER ON 28-DEC-2015
        params.put("brandname", android.os.Build.BRAND);//ADDED BY TEJINDER ON 28-DEC-2015
        params.put("osversion", android.os.Build.VERSION.RELEASE);//ADDED BY TEJINDER ON 28-DEC-2015
        params.put("sdkversion", "" + android.os.Build.VERSION.SDK_INT);//ADDED BY TEJINDER ON 28-DEC-2015
        params.put("languagecode", language);//ADDED BY TEJINDER ON 28-DEC-2015
        params.put("activityname", activityName);

        if (LibCGlobalVariables.userDetailAvailble) {
            params.put(CGlobalVariables.KEY_AS_USER_ID, LibCGlobalVariables.useridsession);
            params.put("name", LibCGlobalVariables.name);
            params.put("day", LibCGlobalVariables.day);
            params.put("month", LibCGlobalVariables.month);
            params.put("year", LibCGlobalVariables.year);
            params.put("hour", LibCGlobalVariables.hour);
            params.put("min", LibCGlobalVariables.min);
            params.put("sec", LibCGlobalVariables.sec);
            params.put("place", LibCGlobalVariables.place);
            params.put("timezone", LibCGlobalVariables.timezone);
            params.put("longdeg", LibCGlobalVariables.longdeg);
            params.put("longmin", LibCGlobalVariables.longmin);
            params.put("longew", LibCGlobalVariables.longew);
            params.put("latdeg", LibCGlobalVariables.latdeg);
            params.put("latmin", LibCGlobalVariables.latmin);
            params.put("latns", LibCGlobalVariables.latns);
            params.put("dst", LibCGlobalVariables.dst);
            params.put("ayanamsa", LibCGlobalVariables.ayanamsa);
        }
        return params;
    }

    /*private class SendFeedBackAsync extends AsyncTask<String, Long, Void> {

        String etName = "", etEmailId = "", etPhone = "", etMsg = "";
        String _exceptionMessage = "", appVerName = "";
        boolean isSuccess = true;
        int resultCode = -1;

        @Override
        protected Void doInBackground(String... arg0) {
            try {
                //resultCode = LibCUtils.sendFeedbackToAstroSage(getMyApplicationName(), _etName.getText().toString(), _etEmailId.getText().toString(),_etPhone.getText().toString(), 	_etMsg.getText().toString());
                //THIS IS ADDED BY BIJENDRA ON 1-MAY-15 TP SEND APP VERSION WIHT FEEDBACK
                //tejinder This take to Language to send with feedback
                SharedPreferences sharedPreferencesForLang =
                        (ActFeedbackToAstroSage.this).getSharedPreferences(
                                CGlobalVariables.MYKUNDLI_LANGUAGE_PREFS_NAME,
                                Context.MODE_PRIVATE);
                int selectedLanguage = sharedPreferencesForLang.getInt(
                        CGlobalVariables.APP_PREFS_AppLanguage, 0);
                String language = "";
                if (selectedLanguage == 0) {
                    language = "ENGLISH";
                } else if (selectedLanguage == 1) {
                    language = "HINDI";
                } else if (selectedLanguage == 2) {
                    language = "TAMIL";
                } else {
                    language = "";
                }
                // Toast.makeText(ActFeedbackToAstroSage.this,"choose language :"+i,Toast.LENGTH_LONG).show();


                //resultCode = LibCUtils.sendFeedbackToAstroSage(getMyApplicationName(), _etName.getText().toString(), _etEmailId.getText().toString(),_etPhone.getText().toString(), 	_etMsg.getText().toString());
                //THIS IS ADDED BY BIJENDRA ON 1-MAY-15 TP SEND APP VERSION WIHT FEEDBACK
                resultCode = LibCUtils.sendFeedbackToAstroSageWithVesrion(ActFeedbackToAstroSage.this, getMyApplicationName(), etName,
                        etEmailId, etPhone, etMsg, appVerName, language, activityName);
            } catch (Exception e) {
                isSuccess = false;
                _exceptionMessage = e.getMessage();
            }
            return null;
        }


        protected void onPreExecute() {
            appVerName = LibCUtils.getApplicationVersionToShow(ActFeedbackToAstroSage.this);
            ////Log.e("APP_VERSION", appVerName);
        *//*	pd = ProgressDialog.show(ActFeedbackToAstroSage.this, null,getResources().getString(R.string.msg_please_wait), true, false);
			TextView tvMsg = (TextView) pd.findViewById(android.R.id.message);
			tvMsg.setTypeface(typeface);
			tvMsg.setTextSize(20);*//*
            etName = _etName.getText().toString();
            etEmailId = _etEmailId.getText().toString();
            etPhone = _etPhone.getText().toString();
            etMsg = _etMsg.getText().toString();

            pd = new CustomProgressDialog(ActFeedbackToAstroSage.this, typeface);
            pd.show();
        }

        protected void onPostExecute(final Void unused) {
            try {
                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                    pd = null;
                }

                if (isSuccess) {
                    // resultcode = 11 (key mismatch)
                    switch (resultCode) {
                        case 0:
                            showCustomisedToastMessage(getResources().getString(R.string.feedback_sent));
                            ActFeedbackToAstroSage.this.finish();
                            break;
                        case 1:
                            showCustomisedToastMessage(getResources().getString(R.string.please_enter_name));
                            showCustomisedToastMessage(getResources().getString(R.string.please_try_again));
                            break;
                        case 2:
                            showCustomisedToastMessage(getResources().getString(R.string.name_limit));
                            showCustomisedToastMessage(getResources().getString(R.string.please_try_again));
                            break;
                        case 3:
                            showCustomisedToastMessage(getResources().getString(R.string.email_one));
                            showCustomisedToastMessage(getResources().getString(R.string.please_try_again));
                            break;
                        case 4:
                            showCustomisedToastMessage(getResources().getString(R.string.email_two));
                            showCustomisedToastMessage(getResources().getString(R.string.please_try_again));
                            break;
                        case 5:
                            showCustomisedToastMessage(getResources().getString(R.string.email_three));
                            showCustomisedToastMessage(getResources().getString(R.string.please_try_again));
                            break;
                        case 6:
                            showCustomisedToastMessage(getResources().getString(R.string.phone_validation));
                            showCustomisedToastMessage(getResources().getString(R.string.please_try_again));
                            break;
                        case 7:
                            showCustomisedToastMessage(getResources().getString(R.string.phone_should_be_numeric));
                            showCustomisedToastMessage(getResources().getString(R.string.please_try_again));
                            break;
                        case 8:
                            showCustomisedToastMessage(getResources().getString(R.string.feedback_message_one));
                            showCustomisedToastMessage(getResources().getString(R.string.please_try_again));
                            break;
                        case 9:
                            showCustomisedToastMessage(getResources().getString(R.string.feedback_message_two));
                            showCustomisedToastMessage(getResources().getString(R.string.please_try_again));
                            break;
                    }
                }

                if (_exceptionMessage.trim().length() > 0) {
                    showCustomisedToastMessage(_exceptionMessage);
                }
            } catch (Exception e) {
                //showCustomisedToastMessage(e.getMessage());
            }

        }
    }*/

    @Override
    public void doActionAfterGetResult(String response, int method) {
        try {
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
                pd = null;
            }

            if (!TextUtils.isEmpty(response)) {
                // resultcode = 11 (key mismatch)
                int resultCode = LibCUtils.parseResponse(response);
                switch (resultCode) {
                    case 0:
                        showCustomisedToastMessage(getResources().getString(R.string.feedback_sent));
                        ActFeedbackToAstroSage.this.finish();
                        break;
                    case 1:
                        showCustomisedToastMessage(getResources().getString(R.string.please_enter_name));
                        showCustomisedToastMessage(getResources().getString(R.string.please_try_again));
                        break;
                    case 2:
                        showCustomisedToastMessage(getResources().getString(R.string.name_limit));
                        showCustomisedToastMessage(getResources().getString(R.string.please_try_again));
                        break;
                    case 3:
                        showCustomisedToastMessage(getResources().getString(R.string.email_one));
                        showCustomisedToastMessage(getResources().getString(R.string.please_try_again));
                        break;
                    case 4:
                        showCustomisedToastMessage(getResources().getString(R.string.email_two));
                        showCustomisedToastMessage(getResources().getString(R.string.please_try_again));
                        break;
                    case 5:
                        showCustomisedToastMessage(getResources().getString(R.string.email_three));
                        showCustomisedToastMessage(getResources().getString(R.string.please_try_again));
                        break;
                    case 6:
                        showCustomisedToastMessage(getResources().getString(R.string.phone_validation));
                        showCustomisedToastMessage(getResources().getString(R.string.please_try_again));
                        break;
                    case 7:
                        showCustomisedToastMessage(getResources().getString(R.string.phone_should_be_numeric));
                        showCustomisedToastMessage(getResources().getString(R.string.please_try_again));
                        break;
                    case 8:
                        showCustomisedToastMessage(getResources().getString(R.string.feedback_message_one));
                        showCustomisedToastMessage(getResources().getString(R.string.please_try_again));
                        break;
                    case 9:
                        showCustomisedToastMessage(getResources().getString(R.string.feedback_message_two));
                        showCustomisedToastMessage(getResources().getString(R.string.please_try_again));
                        break;
                }
            }

        } catch (Exception e) {
            if (e != null && e.getMessage() != null) {
                showCustomisedToastMessage(e.getMessage());
            }
        }
    }

    @Override
    public void doActionOnError(String error) {
        if (!TextUtils.isEmpty(error)) {
            showCustomisedToastMessage(error);
        }
    }
}
