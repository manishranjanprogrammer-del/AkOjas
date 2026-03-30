package com.ojassoft.astrosage.ui.act;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;

import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;
import androidx.core.app.ActivityCompat;
import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.CustomCredential;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.Toolbar;

import android.os.CancellationSignal;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Cache;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.google.gson.Gson;
import com.libojassoft.android.misc.VolleySingleton;
import com.libojassoft.android.utils.LibCUtils;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanDateTime;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.constants.PaymentOptions;
import com.ojassoft.astrosage.controller.ControllerManager;
import com.ojassoft.astrosage.customexceptions.UIDataOperationException;
import com.ojassoft.astrosage.jinterface.IAskCallback;
import com.ojassoft.astrosage.jinterface.IChoosePayOption;
import com.ojassoft.astrosage.jinterface.IPaymentFailed;
import com.ojassoft.astrosage.jinterface.IPermissionCallback;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.misc.GetCheckSum;
import com.ojassoft.astrosage.model.AstrologerServiceInfo;
import com.ojassoft.astrosage.model.Payoption;
import com.ojassoft.astrosage.model.ServicelistModal;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.OnVolleyResultListener;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.BrihatPaymentInformationActivity;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.ui.customcontrols.MyNewCustomTimePicker;
import com.ojassoft.astrosage.ui.fragments.ChooseServicePayOPtionDialog;
import com.ojassoft.astrosage.ui.fragments.PaymentFailedDailogFrag;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.CustomTypefaces;
import com.ojassoft.astrosage.utils.UserEmailFetcher;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.paytm.pgsdk.TransactionManager;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

import static com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_API;
import static com.ojassoft.astrosage.utils.CGlobalVariables.PACKAGE_NAME;
import static com.ojassoft.astrosage.utils.CGlobalVariables.PAYMENT_TYPE_SERVICE;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.APP_VERSION;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.COUNTRY_CODE;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.DEVICE_ID;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.USER_PHONE_NO;
import static com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode;
import static com.ojassoft.astrosage.varta.utils.CUtils.getUserID;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

//import com.google.analytics.tracking.android.EasyTracker;
//import com.google.analytics.tracking.android.Tracker;
//import com.google.analytics.tracking.android.Transaction;

/**
 * Created by ojas-02 on 1/3/17.
 */

public class ActAstroServicePayment extends BaseInputActivity implements View.OnClickListener, IChoosePayOption, IAskCallback, IPaymentFailed, PaymentResultListener, IPermissionCallback, OnVolleyResultListener {
    String payStatus = "0";
    String payId = "";
    String order_Id = "";
    private static CustomProgressDialog pd = null;
    private final int REQUEST_KEY_SELECT_PROFILE = 1004;
    public Payoption globalPayOptions;
    public BeanHoroPersonalInfo beanHoroPersonalInfo;
    boolean isDataComingFromDownloadPdf;
    String[] availLangArray;
    private TextView tvTitle, tvLimit, tvLanguage;
    private Toolbar tool_barAppModule;
    private TabLayout tabLayout;
    private ProgressDialog pDialog;
    private Typeface typeface;
    private RequestQueue queue;
    private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    private Cache cache;
    private String data;
    private String country_name, billingCountry_name = "";
    private EditText etUseremail, etUsermobno, etUserquestion;
    // private String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
    private Button buy_now;
    private TextInputLayout input_layout_email, input_layout_mobno, input_layout_question/*, edt_State_layout, edt_city_layout, edt_address_layout, edt_pincode_layout*/;
    // private IBirthDetailInputFragment _iBirthDetailInputFragment;
    private MyCustomToast mct;
    private AstrologerServiceInfo astrologerServiceInfo;
    private ServicelistModal servicelistModal;
    private String astrologerId;
    private String reportAvailableInLang;
    private Bundle bundle;
    private FragmentTransaction ft;
    private String title;
    private JSONObject fullAddress;
    private String fullJsonDataObj = "";
    private boolean isPaymentDone = true;
    private String emailID = "";
    private String payMode = "Paytm";
    private AlertDialog langOptionSelectAlert;
    //private int selectedLang = CGlobalVariables.ENGLISH;
    private Spinner languageOptions;
    private String chat_Id = "";

    private static final int RC_READ = 1;
    private static final int RC_HINT_EMAIL = 2;
    private boolean isGoogleApiClientConnected;
    private boolean isEmailDialogueOpened;
    private String selectedEmailId;
    private ActAstroServicePayment activity;
    private CredentialManager credentialManager;
    ActivityResultLauncher<Intent> resultLauncher;

    public ActAstroServicePayment() {
        super(R.string.app_name);
    }

    public static final String TAG = "pppppp";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LANGUAGE_CODE = ((AstrosageKundliApplication) this.getApplication())
                .getLanguageCode();
        //selectedLang = LANGUAGE_CODE;
        typeface = CUtils.getRobotoFont(
                this, LANGUAGE_CODE, CGlobalVariables.regular);


        setContentView(R.layout.lay_astro_service_payment);
        activity = ActAstroServicePayment.this;

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//            Checkout.preload(getApplicationContext());
//        }


        init();
    }

    @Override
    public void onClick(View v) {
        if (v != null) {
            switch (v.getId()) {
                case R.id.buy_now:
                    if (validateData()) {

                        if (isDataComingFromDownloadPdf) {
                            boolean razorPaymentVisibility = CUtils.getBooleanData(ActAstroServicePayment.this, CGlobalVariables.key_RazorPayVisibilityServices, true);
                            boolean paytmPaymentVisibility = CUtils.getBooleanData(ActAstroServicePayment.this, CGlobalVariables.key_PaytmWalletVisibilityServices, true);
                            astrologerServiceInfo = CUtils.setDataModel(this, beanHoroPersonalInfo, "Paytm", servicelistModal);

                            if (razorPaymentVisibility && !paytmPaymentVisibility) {
                                onSelectedOption(R.id.radioRazor, "");
                            } else if (!razorPaymentVisibility && paytmPaymentVisibility) {
                                onSelectedOption(R.id.radioPaytm, "");
                            } else {
                                launchBrihatPaymentScreen();
                                /*Fragment diog = getSupportFragmentManager().findFragmentByTag("Dialog");
                                if (diog == null) {
                                    ChooseServicePayOPtionDialog dialog = new ChooseServicePayOPtionDialog();
                                    dialog.show(getSupportFragmentManager(), "Dialog");

                                }*/
                            }

          /*              astrologerServiceInfo.setMobileNo(mobile);
                        astrologerServiceInfo.setEmailID(email);*/


                        } else {
                            if(servicelistModal == null || servicelistModal.getIsShowProfile() == null){
                                callToGetNewBirthDetails(isLocalKundliAvailable());
                            } else if (servicelistModal.getIsShowProfile().equalsIgnoreCase("0")) {
                                setmodalData("");
                            } else {
                                callToGetNewBirthDetails(isLocalKundliAvailable());
                            }
                        }

                /*    setDataModel();

                    convertInJsonObj(astrologerServiceInfo);*/

                        //     astrologerServiceInfo.setPriceRs("1");
                        //   servicelistModal.setPriceInRS("1");

                    }
            }
        }
    }
    /***************************** Gmail choose dialogue start **********************/

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
                        etUseremail.setText(selectedEmailId);
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
            etUseremail.setFocusableInTouchMode(true);
            etUseremail.setFocusable(true);
            etUseremail.requestFocus();
        });
    }

    /***************************** Gmail choose dialogue end **********************/

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

    private void callToGetNewBirthDetails(int screenId) {
        Bundle bundle = new Bundle();
        Intent intent = new Intent(this, HomeInputScreen.class);
        intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, CGlobalVariables.MODULE_BASIC);
        intent.putExtra(CGlobalVariables.ASTRO_SERVICE_QUERY_DATA, true);
        bundle.putString("QUERY_EMAIL_ID", etUseremail.getText().toString().trim());
        bundle.putString("QUERY_PHONE_NUM", etUsermobno.getText().toString().trim());
        bundle.putString("QUERY_QUESTION", etUserquestion.getText().toString().trim());
        bundle.putSerializable("keyItemDetails", servicelistModal);
        intent.putExtra("PAGER_INDEX", screenId);
        intent.putExtras(bundle);
        startActivityForResult(intent, REQUEST_KEY_SELECT_PROFILE);
    }

    private void convertInJsonObj(AstrologerServiceInfo localOrderModal) {
        Gson gson = new Gson();
        fullJsonDataObj = gson.toJson(localOrderModal);


        System.out.println("obj" + fullJsonDataObj.toString());
        // System.out.println("obj" + gson.fromJson(fullJsonDataObj.toString(), JsonElement.class));

    }

    private boolean validateData() {
        boolean flag = false;
        if (validateName(etUseremail, input_layout_email, getString(R.string.email_one_v))
                && validateName(etUsermobno, input_layout_mobno, getString(R.string.astro_shop_User_mob_no))
                && validateName(etUserquestion, input_layout_question, getString(R.string.astrologer_user_query))) {
            CUtils.saveStringData(this, CGlobalVariables.ASTROASKAQUESTIONUSERPHONENUMBER, etUsermobno.getText().toString());
            CUtils.saveStringData(this, CGlobalVariables.ASTROASKAQUESTIONUSEREMAIL, etUseremail.getText().toString());

            flag = true;
        }

        return flag;
    }

    private void init() {
        credentialManager = CredentialManager.create(this);
        bundle = new Bundle();
        LANGUAGE_CODE = ((AstrosageKundliApplication) this.getApplication())
                .getLanguageCode();
        tool_barAppModule = (Toolbar) findViewById(R.id.tool_barAppModule);
        tool_barAppModule.setNavigationIcon(R.drawable.ic_back_arrow);
        setSupportActionBar(tool_barAppModule);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvLimit = (TextView) findViewById(R.id.textquestiontextlimit);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setVisibility(View.GONE);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        typeface = CUtils.getRobotoFont(
                this, LANGUAGE_CODE, CGlobalVariables.regular);
        mct = new MyCustomToast(this, this
                .getLayoutInflater(), this, typeface);
        astrologerServiceInfo = new AstrologerServiceInfo();
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        pd = new CustomProgressDialog(this, typeface);
        servicelistModal = (ServicelistModal) getIntent().getSerializableExtra("key");
        astrologerId = getIntent().getStringExtra("astrologerId");
        reportAvailableInLang = getIntent().getStringExtra("ReportAvailableInLang");
        Log.d("service model data", new Gson().toJson(servicelistModal));
        beanHoroPersonalInfo = ((BeanHoroPersonalInfo) getIntent().getSerializableExtra("BeanHoroPersonalInfo"));
        isDataComingFromDownloadPdf = getIntent().getBooleanExtra(CGlobalVariables.DataComingFromDownloadPdf, false);

        buy_now = (Button) findViewById(R.id.buy_now);
        buy_now.setOnClickListener(this);

        etUseremail = (EditText) findViewById(R.id.etUseremail);
        etUseremail.addTextChangedListener(new MyTextWatcher(etUseremail));

        etUsermobno = (EditText) findViewById(R.id.etUsermobno);
        etUsermobno.addTextChangedListener(new MyTextWatcher(etUsermobno));

        etUserquestion = (EditText) findViewById(R.id.etUserquestion);
        etUserquestion.addTextChangedListener(new MyTextWatcher(etUserquestion));

        input_layout_email = (TextInputLayout) findViewById(R.id.input_layout_email);
        input_layout_mobno = (TextInputLayout) findViewById(R.id.input_layout_mobno);
        input_layout_question = (TextInputLayout) findViewById(R.id.input_layout_question);

        languageOptions = (Spinner) findViewById(R.id.ics_spinner_lang);
        tvLanguage = (TextView) findViewById(R.id.tvLanguage);

        boolean gotEmail = false;
        String isShowProblem = servicelistModal.getIsShowProblem();
        if(isShowProblem == null){
            isShowProblem = "1";
        }
        String email_id = CUtils.getAstroshopUserEmail(this);
        if (servicelistModal != null && servicelistModal.getServiceId() != null && servicelistModal.getServiceId().equalsIgnoreCase("114") ||
                servicelistModal.getServiceId().equalsIgnoreCase("148") ||
                (servicelistModal.getIsShowProblem() != null && isShowProblem.equalsIgnoreCase("0"))) {
            etUserquestion.setVisibility(View.GONE);
            tvLimit.setVisibility(View.GONE);
            tvTitle.setTypeface(((BaseInputActivity) this).robotRegularTypeface);
            tvTitle.setText(servicelistModal.getTitle());

            //For selection of language
            languageOptions.setVisibility(View.VISIBLE);
            tvLanguage.setVisibility(View.VISIBLE);
            tvLanguage.setTypeface(robotRegularTypeface);
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT) {
                languageOptions.setPopupBackgroundResource(R.drawable.spinner_dropdown);
            }
            //languageOptions.setAdapter(getSpinnerAdapterLanguage(getResources().getStringArray(R.array.language_options_hi_eng)));
            availLangArray = getLangForAvailableReport(reportAvailableInLang);
            languageOptions.setAdapter(getSpinnerAdapterLanguage(availLangArray));
            if (availLangArray != null && availLangArray.length>1 && LANGUAGE_CODE == CGlobalVariables.HINDI) {
                languageOptions.setSelection(1);
            } else {
                languageOptions.setSelection(0);
            }

            languageOptions.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    CUtils.hideMyKeyboard(ActAstroServicePayment.this);
                    return false;
                }
            });
            // tvLanguage.setVisibility(View.VISIBLE);
            //setDeliveryText();

        } else {
            etUserquestion.setVisibility(View.VISIBLE);
            tvLimit.setVisibility(View.VISIBLE);
            tvTitle.setTypeface(((BaseInputActivity) this).regularTypeface);
            tvTitle.setText(getResources().getString(R.string.service_question_text));

            languageOptions.setVisibility(View.GONE);
            tvLanguage.setVisibility(View.GONE);
        }

        String preferenceEmailId = CUtils.getStringData(this, CGlobalVariables.ASTROASKAQUESTIONUSEREMAIL, "");
        if (!preferenceEmailId.equals("")) {
            etUseremail.setText(preferenceEmailId);
            gotEmail = true;
        } else if (CUtils.isUserLogedIn(this)) {
            String user_Name = CUtils.getUserName(this);
            String user_Password = CUtils.getUserPassword(this);
            if (user_Name != null && user_Name != "" && CUtils.isValidEmail(user_Name)) {
                etUseremail.setText(user_Name);
                gotEmail = true;
            }
        }

        if (!email_id.isEmpty() && !gotEmail) {
            etUseremail.setText(email_id);
        } else if (!gotEmail) {
            /*boolean hasEmailIdPermission = CUtils.getBooleanData(this, CGlobalVariables.EmailId_Permission, false);
            if (hasEmailIdPermission) {
                checkForContactPermission(false);
            } else {
                CUtils.showEmailIdPermissionDialog(this);
            }*/
            //etUseremail.setFocusable(false);
            etUseremail.setOnClickListener(new View.OnClickListener() {
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

        String preferenceMobNo = CUtils.getStringData(this, CGlobalVariables.ASTROASKAQUESTIONUSERPHONENUMBER, "");
        if(preferenceMobNo.isEmpty()){
            preferenceMobNo = getUserID(this);
        }
        if (!preferenceMobNo.equals("")) {
            etUsermobno.setText(preferenceMobNo);
        }

        resultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    try {
                        int payViaOptions = result.getResultCode();
                        switch (payViaOptions) {
                            case PaymentOptions.PAY_VIA_PAYTM:
                                setDataForPayment("Paytm");
                                break;
                            case PaymentOptions.PAY_VIA_RAZORPAY:
                                setDataForPayment("RazorPay");
                                break;
                            case PaymentOptions.PAY_VIA_WALLET:
                                setDataForPayment("Wallet");
                                break;
                        }
                    } catch (Exception e) {
                        Log.d(TAG, "resultLauncher e: "+e);
                    }
                }
        );

    }

    private void setDataForPayment(String paymentMode) {
        setDataModel();
        astrologerServiceInfo.setPayMode(paymentMode);
        startPayment(astrologerServiceInfo);
    }

    private void checkForContactPermission(boolean isUSerPermissionDialogOpen) {
        if (CUtils.isContactsPermissionGranted(this, this, PERMISSION_CONTACTS, isUSerPermissionDialogOpen)) {
            setEmailIdFromUserAccount();
        }
    }

    private void setEmailIdFromUserAccount() {
        String email = UserEmailFetcher.getEmail(this);
        if (email != null) {
            etUseremail.setText(email);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {


        super.onSaveInstanceState(outState);
    }


    private void showCustomTimePickerDialog(BeanDateTime beanDateTime) {
        Intent intent = new Intent(this, MyNewCustomTimePicker.class);
        intent.putExtra("H", beanDateTime.getHour());
        intent.putExtra("M", beanDateTime.getMin());
        intent.putExtra("S", beanDateTime.getSecond());
        intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, SELECTED_MODULE);
        startActivityForResult(intent, SUB_ACTIVITY_TIME_PICKER);
    }

    void startPayment(AstrologerServiceInfo astrologerServiceInfo) {
        if (!CUtils.isConnectedWithInternet(ActAstroServicePayment.this)) {
            MyCustomToast mct = new MyCustomToast(ActAstroServicePayment.this, ActAstroServicePayment.this
                    .getLayoutInflater(), ActAstroServicePayment.this, typeface);
            mct.show(getResources().getString(R.string.no_internet));
        } else {
            if (astrologerServiceInfo != null) {
                //   astrologerServiceInfo.setPayMode("Paytm");
                String appVerName = LibCUtils.getApplicationVersionToShow(ActAstroServicePayment.this);

                String problem = "";
                if (servicelistModal != null && servicelistModal.getServiceId() != null && servicelistModal.getServiceId().equalsIgnoreCase("114") ||
                        servicelistModal.getServiceId().equalsIgnoreCase("148")
                        || (servicelistModal.getIsShowProblem() != null && servicelistModal.getIsShowProblem().equalsIgnoreCase("0"))) {

                    int language = languageOptions.getSelectedItemPosition();
                    if (availLangArray[language].equals(getResources().getString(R.string.english))) {
                        language = CGlobalVariables.ENGLISH;
                    } else if (availLangArray[language].equals(getResources().getString(R.string.bangali))) {
                        language = CGlobalVariables.BANGALI;
                    } else if (availLangArray[language].equals(getResources().getString(R.string.hindi))) {
                        language = CGlobalVariables.HINDI;
                    } else if (availLangArray[language].equals(getResources().getString(R.string.tamil))) {
                        language = CGlobalVariables.TAMIL;
                    } else if (availLangArray[language].equals(getResources().getString(R.string.telugu))) {
                        language = CGlobalVariables.TELUGU;
                    } else if (availLangArray[language].equals(getResources().getString(R.string.gujarati))) {
                        language = CGlobalVariables.GUJARATI;
                    } else if (availLangArray[language].equals(getResources().getString(R.string.kannad))) {
                        language = CGlobalVariables.KANNADA;
                    } else if (availLangArray[language].equals(getResources().getString(R.string.marathi))) {
                        language = CGlobalVariables.MARATHI;
                    } else if (availLangArray[language].equals(getResources().getString(R.string.malayalam))) {
                        language = CGlobalVariables.MALAYALAM;
                    }else if (availLangArray[language].equals(getResources().getString(R.string.odia))) {
                        language = CGlobalVariables.ODIA;
                    }else if (availLangArray[language].equals(getResources().getString(R.string.assamese))) {
                        language = CGlobalVariables.ASAMMESSE;
                    }
                    //international languages
                    else if (availLangArray[language].equals(getResources().getString(R.string.spanish))) {
                        language = CGlobalVariables.SPANISH;
                    }else if (availLangArray[language].equals(getResources().getString(R.string.chinese))) {
                        language = CGlobalVariables.CHINESE;
                    }else if (availLangArray[language].equals(getResources().getString(R.string.japanese))) {
                        language = CGlobalVariables.JAPANESE;
                    }else if (availLangArray[language].equals(getResources().getString(R.string.portugese))) {
                        language = CGlobalVariables.PORTUGUESE;
                    }else if (availLangArray[language].equals(getResources().getString(R.string.german))) {
                        language = CGlobalVariables.GERMAN;
                    }else if (availLangArray[language].equals(getResources().getString(R.string.italian))) {
                        language = CGlobalVariables.ITALIAN;
                    }

                    problem = servicelistModal.getTitle() + "-" + CUtils.getLanguageKey(language) + " " + "## " + "(lang: " + CUtils.getLanguageKey(language) + ", " + "appversion: " + appVerName + ")";

                } else {
                    problem = astrologerServiceInfo.getProblem() + " " + "## " + "(lang: " + CUtils.getLanguageKey(LANGUAGE_CODE) + ", " + "appversion: " + appVerName + ")";

                }
                astrologerServiceInfo.setProblem(problem);
                if (servicelistModal != null) {
                    astrologerServiceInfo.setCouponCode(servicelistModal.getCouponCode());
                }
                CUtils.getOrderID(ActAstroServicePayment.this, typeface, queue, astrologerServiceInfo);

            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //EasyTracker.getInstance().activityStart(this);

    }

    @Override
    protected void onStop() {
        super.onStop();
        //EasyTracker.getInstance().activityStop(this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if (requestCode == RC_HINT_EMAIL) {
            isEmailDialogueOpened = true;
            setFocusOnEmail();
            if (resultCode == RESULT_OK) {
                Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                selectedEmailId = credential.getId();
                CUtils.saveAstroshopUserEmail(activity, selectedEmailId);
                etUseremail.setText(selectedEmailId);
            }
        } else */
        if (requestCode == CGlobalVariables.REQUEST_CODE_PAYTM && data != null) {
            try {
                String responseData = data.getStringExtra("response");
                //Log.e("PaytmOrder", "resp data=" + responseData);
                if(TextUtils.isEmpty(responseData)){
                    processPaytmTransaction("TXN_FAILED");
                }else {
                    JSONObject respObj = new JSONObject(responseData);
                    String status = respObj.getString("STATUS");
                    processPaytmTransaction(status);
                }
            } catch (Exception e) {
                //
            }
        }

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_KEY_SELECT_PROFILE:
                    try {
                        if (data != null && data.getStringExtra("JSONFULLASTROSAGEDATA") != null) {
                            String allBirthDetail = data.getStringExtra("JSONFULLASTROSAGEDATA");
                            setmodalData(allBirthDetail);
                        }
                    } catch (Exception ex) {
                        //Log.e("Exception",ex.getMessage());
                    }
                    break;
                case PERMISSION_CONTACTS:
                    checkForContactPermission(false);
                    break;
            }
        }

    }

    @Override
    public void getCallBack(String result, CUtils.callBack callback, String priceInDollor, String priceInRs) {
        if (callback == CUtils.callBack.GET_ORDER_ID) {

            if (result != null && result.equalsIgnoreCase(CGlobalVariables.RESULT_CODE_THREE)) {
                mct.show(getString(R.string.coupon_not_valid));
            }/*else if(result.equalsIgnoreCase(CGlobalVariables.RESULT_CODE_SEVEN)) // price mismatch with server
            {
                mct.show(getString(R.string.order_fail));
            }*/ else {
                order_Id = result;
                VolleyLog.d("Ask from server order_Id " + order_Id);
                //com.google.analytics.tracking.android.//Log.e("order" + order_Id);
                if (order_Id != null && !order_Id.isEmpty()) {

                    if (priceInRs != null && priceInRs.length() > 0) {
                        astrologerServiceInfo.setPriceRs(priceInRs);
                    }

                    if (priceInDollor != null && priceInDollor.length() > 0) {
                        astrologerServiceInfo.setPrice(priceInDollor);
                    }

                    if (astrologerServiceInfo.getPayMode() != null && astrologerServiceInfo.getPayMode().equalsIgnoreCase("RazorPay")) {
                        Double paise = Double.parseDouble(astrologerServiceInfo.getPriceRs()) * 100;
                        // API calling
                        //showProgressBar();
                        //new VolleyServerRequest(this, (OnVolleyResultListener) ActAstroServicePayment.this, CGlobalVariables.RAZORPAY_ORDERID_URL, CUtils.getRazorOrderIdParams(paise,"INR",order_Id));
                        goToRazorPayFlow();
                    } else if (astrologerServiceInfo.getPayMode() != null && astrologerServiceInfo.getPayMode().equalsIgnoreCase("Wallet")) {
                        payViaWallet();
                    } else {
                        new GetCheckSum(ActAstroServicePayment.this, typeface).getCheckSum(getchecksumparams(), 0);
                        // CUtils.getCheckSum(ActAstroServicePayment.this, getchecksumparams(), typeface);
                    }

                } else {
                    MyCustomToast mct = new MyCustomToast(ActAstroServicePayment.this,
                            ActAstroServicePayment.this.getLayoutInflater(),
                            ActAstroServicePayment.this, regularTypeface);
                    mct.show(getResources().getString(R.string.order_fail));
                }
            }
        } else if (callback == CUtils.callBack.GET_CHECKSUM) {
            String checksum = result;
            if (!result.isEmpty()) {
                startPaytmPayment(order_Id, astrologerServiceInfo.getPriceRs(), checksum);
            } else {
                MyCustomToast mct = new MyCustomToast(ActAstroServicePayment.this,
                        ActAstroServicePayment.this.getLayoutInflater(),
                        ActAstroServicePayment.this, regularTypeface);
                mct.show(getResources().getString(R.string.order_fail));
            }
        } else if (callback == CUtils.callBack.POST_STATUS) {
            String postStatus = result;
            if (postStatus != null && postStatus.equalsIgnoreCase("1")) {
                if (payStatus != null && payStatus.equalsIgnoreCase("1")) {
                    CUtils.emailPDF(ActAstroServicePayment.this, CGlobalVariables.PAYTM_EMAIL_PDF, mediumTypeface, queue, order_Id, payStatus, astrologerServiceInfo);
                    Intent tppsIntent = new Intent(ActAstroServicePayment.this,
                            ActServicePaymentStatus.class);
                    tppsIntent.putExtra("Key", servicelistModal);
                    tppsIntent.putExtra("Status", "success");
                    tppsIntent.putExtra("emailID", astrologerServiceInfo.getEmailID());
                    startActivity(tppsIntent);
                    onPurchaseCompleted(servicelistModal, order_Id);
                } else {
                    openPaymentFailedDialog();
                    CUtils.googleAnalyticSendWitPlayServie(this,
                            CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                            CGlobalVariables.GOOGLE_ANALYTIC_SERVICE_PAYMENT_FAILED, null);
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SERVICE_PAYMENT_FAILED, CGlobalVariables.FIREBASE_EVENT_PAYMENT_FAILED, "");

                    /*MyCustomToast mct = new MyCustomToast(ActAskQuestion.this,
                            ActAskQuestion.this.getLayoutInflater(),
                            ActAskQuestion.this, regularTypeface);
                    mct.show(getResources().getString(R.string.payment_failure_dialog_title));*/
                }

            } else {
                openPaymentFailedDialog();
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SERVICE_PAYMENT_FAILED, CGlobalVariables.FIREBASE_EVENT_PAYMENT_FAILED, "");
            }

        } else if (callback == CUtils.callBack.POST_RAZORPAYSTATUS) {
            String postStatus = result;
            if (postStatus != null && postStatus.equalsIgnoreCase("1")) {
                if (payStatus != null && payStatus.equalsIgnoreCase("1")) {

                    CUtils.emailPDF(ActAstroServicePayment.this, CGlobalVariables.RAZORPAY_EMAIL_PDF, mediumTypeface, queue, order_Id, payStatus, astrologerServiceInfo);
                    Intent tppsIntent = new Intent(ActAstroServicePayment.this,
                            ActServicePaymentStatus.class);
                    tppsIntent.putExtra("Key", servicelistModal);
                    tppsIntent.putExtra("Status", "success");
                    tppsIntent.putExtra("emailID", astrologerServiceInfo.getEmailID());
                    startActivity(tppsIntent);

                    onPurchaseCompleted(servicelistModal, order_Id);
                } else {
                    openPaymentFailedDialog();
                    CUtils.googleAnalyticSendWitPlayServie(this,
                            CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                            CGlobalVariables.GOOGLE_ANALYTIC_SERVICE_RAZOR_PAYMENT_FAILED, null);
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SERVICE_RAZOR_PAYMENT_FAILED, CGlobalVariables.FIREBASE_EVENT_PAYMENT_FAILED, "");

                    /*MyCustomToast mct = new MyCustomToast(ActAskQuestion.this,
                            ActAskQuestion.this.getLayoutInflater(),
                            ActAskQuestion.this, regularTypeface);
                    mct.show(getResources().getString(R.string.payment_failure_dialog_title));*/
                }

            } else {
                openPaymentFailedDialog();
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SERVICE_RAZOR_PAYMENT_FAILED, CGlobalVariables.FIREBASE_EVENT_PAYMENT_FAILED, "");

            }
        }

    }

    @Override
    public void getCallBackForChat(String[] result, CUtils.callBack callback, String priceInDollor, String priceInRs) {

    }

    private void openPaymentFailedDialog() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        PaymentFailedDailogFrag pfdf = new PaymentFailedDailogFrag();
        ft.add(pfdf, null);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onRetryClick() {
        if (astrologerServiceInfo != null) {

            boolean razorPaymentVisibility = CUtils.getBooleanData(ActAstroServicePayment.this, CGlobalVariables.key_RazorPayVisibilityServices, true);
            boolean paytmPaymentVisibility = CUtils.getBooleanData(ActAstroServicePayment.this, CGlobalVariables.key_PaytmWalletVisibilityServices, true);
            //     astrologerServiceInfo = CUtils.setDataModel(this, beanHoroPersonalInfo, "Paytm", servicelistModal);

            if (razorPaymentVisibility && !paytmPaymentVisibility) {
                onSelectedOption(R.id.radioRazor, "");
            } else if (!razorPaymentVisibility && paytmPaymentVisibility) {
                onSelectedOption(R.id.radioPaytm, "");
            } else {
                launchBrihatPaymentScreen();
                /*Fragment diog = getSupportFragmentManager().findFragmentByTag("Dialog");
                if (diog == null) {
                    ChooseServicePayOPtionDialog dialog = new ChooseServicePayOPtionDialog();
                    dialog.show(getSupportFragmentManager(), "Dialog");

                }*/
            }
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);


    }

    @Override
    public void onSelectedOption(int opt, String mob) {
        if (opt == R.id.radioPaytm) {
            CUtils.googleAnalyticSendWitPlayServie(this,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                    CGlobalVariables.GOOGLE_ANALYTIC_SERVICE_PAYTM, null);
            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SERVICE_PAYTM, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

            setDataModel();
            astrologerServiceInfo.setPayMode("Paytm");
            //convertInJsonObj(astrologerServiceInfo);
            startPayment(astrologerServiceInfo);

        } else if (opt == R.id.radioRazor) {
            CUtils.googleAnalyticSendWitPlayServie(this,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                    CGlobalVariables.GOOGLE_ANALYTIC_SERVICE_RAZORPAY, null);
            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SERVICE_RAZORPAY, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
            setDataModel();
            astrologerServiceInfo.setPayMode("RazorPay");
            startPayment(astrologerServiceInfo);

        }

    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            payStatus = "1";
            payId = razorpayPaymentID;

            double dPrice = 0.0;
            String price = "";
            try {
                if(astrologerServiceInfo != null)
                {
                    if(astrologerServiceInfo.getPriceRs() != null && astrologerServiceInfo.getPriceRs().length()>0)
                    {
                        price = astrologerServiceInfo.getPriceRs();
                        dPrice = Double.valueOf(price);
                    }
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            // in case of purchase event not added by server then add event
            if(!com.ojassoft.astrosage.utils.CUtils.isPurchaseEventAddedByServer(this)) {
                CUtils.googleAnalyticSendWitPlayServieForPurchased(this,
                        CGlobalVariables.GOOGLE_ANALYTIC_ASTROSHOP_SERVICE,
                        CGlobalVariables.GOOGLE_ANALYTIC_SERVICE_RAZOR_PAYMENT_SUCCESS, null, dPrice, "");
            }
            payStatus = "1";
            payId = razorpayPaymentID;

            CUtils.postRazorPayDetail(ActAstroServicePayment.this, mediumTypeface, queue, order_Id, payStatus, astrologerServiceInfo, razorpayPaymentID, "");

            //Log.e("ServicePay", razorpayPaymentID);

            //  Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            //Log.e("ServicePay", "Exception in onPaymentSuccess", e);
        }
    }

    @Override
    public void onPaymentError(int i, String response) {
        try {
            String status = "Code-" + i + " " + "Message-" + response;
            payStatus = "0";
            CUtils.saveBooleanData(this,CGlobalVariables.IS_SUBSCRIPTION_ERROR,true);
            CUtils.postRazorPayDetail(ActAstroServicePayment.this, mediumTypeface, queue, order_Id, payStatus, astrologerServiceInfo, "", status);

            // Toast.makeText(this, "Payment failed: " + i + " " + response, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            //Log.e("Service pay", "Exception in onPaymentError", e);
        }
    }

    @Override
    public void isEmailIdPermissionGranted(boolean isPermissionGranted) {
        checkForContactPermission(true);
    }

    private boolean validateName(EditText name, TextInputLayout inputLayout, String message) {
        boolean value = true;

        if (name == etUseremail) {

            if (etUseremail.getText().toString().trim().isEmpty()) {
                input_layout_email.setError(getResources().getString(R.string.email_one_v));
                inputLayout.setErrorEnabled(true);
                requestFocus(etUseremail);
                name.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                value = false;
                // etUseremail.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
            } else if (!(CUtils.isValidEmail(etUseremail.getText().toString().trim()))) {
                input_layout_email.setError(getResources().getString(R.string.email_one_v_astro_service));
                inputLayout.setErrorEnabled(true);
                requestFocus(etUseremail);
                name.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                value = false;
            } else {
                input_layout_email.setErrorEnabled(false);
                input_layout_email.setError(null);
                etUseremail.getBackground().setColorFilter(getResources().getColor(R.color.ColorPrimary), PorterDuff.Mode.SRC_ATOP);
            }


        }


        if (name == etUserquestion) {
            if (etUserquestion.getVisibility() == View.VISIBLE && (name.getText().toString().trim().isEmpty() || name.getText().toString().trim().length() < 1)) {
                inputLayout.setError(message);
                inputLayout.setErrorEnabled(true);
                requestFocus(name);
                name.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                value = false;
            } else {
                inputLayout.setErrorEnabled(false);
                inputLayout.setError(null);
                name.getBackground().setColorFilter(getResources().getColor(R.color.ColorPrimary), PorterDuff.Mode.SRC_ATOP);
            }
        }
        if (name == etUsermobno) {
            if (name.getText().toString().trim().isEmpty()) {
                inputLayout.setError(message);
                inputLayout.setErrorEnabled(true);
                requestFocus(name);
                name.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                value = false;
            } else if (name.getText().toString().trim().length() < 9) {
                inputLayout.setError(getResources().getString(R.string.phone_one_v_astro_service));
                inputLayout.setErrorEnabled(true);
                requestFocus(name);
                name.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                value = false;
            } else {
                inputLayout.setErrorEnabled(false);
                inputLayout.setError(null);
                name.getBackground().setColorFilter(getResources().getColor(R.color.ColorPrimary), PorterDuff.Mode.SRC_ATOP);
            }

        }
        return value;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private AstrologerServiceInfo setDataModel() {


        if (this instanceof ActAstroServicePayment) {
            astrologerServiceInfo.setKey(CUtils.getApplicationSignatureHashCode(this));
            astrologerServiceInfo.setCcAvenueType("Paytm");
            astrologerServiceInfo.setPayMode(payMode);

            astrologerServiceInfo.setEmailID(etUseremail.getText().toString().trim());

            //           astrologerServiceInfo.setRegName(username);
            emailID = etUseremail.getText().toString().trim();
            //  astrologerServiceInfo.setRegName(emailID);


            astrologerServiceInfo.setProblem(etUserquestion.getText().toString().trim());
                /*if (checkBoxbirth.isChecked()) {
                    astrologerServiceInfo.setKnowDOB("1");
                } else {*/
            astrologerServiceInfo.setKnowDOB("0");
            //}
                /*if (checkBoxtimeofbirth.isChecked()) {
                    astrologerServiceInfo.setKnowTOB("1");
                } else {*/
            astrologerServiceInfo.setKnowTOB("0");
            //}
            astrologerServiceInfo.setMobileNo(etUsermobno.getText().toString().trim());
            astrologerServiceInfo.setPriceRs(servicelistModal.getPriceInRS());
            astrologerServiceInfo.setPrice(servicelistModal.getPriceInDollor());
            astrologerServiceInfo.setServiceId(servicelistModal.getServiceId());
            if (astrologerId == null) {
                astrologerServiceInfo.setProfileId(" ");

            } else {
                astrologerServiceInfo.setProfileId(astrologerId);

            }

            astrologerServiceInfo.setBillingCountry(billingCountry_name);

        }
        return astrologerServiceInfo;
    }

    public void onPurchaseCompleted(ServicelistModal itemdetails, String order_id) {
        //Log.e("Purchase plan Start", "");

        //double price = (Double.valueOf(itemdetails.getPriceInRS()));
        //String priceCurrencycode, String category, String affiliation
        CUtils.trackEcommerceProduct(ActAstroServicePayment.this, itemdetails.getServiceId(), itemdetails.getTitle(), itemdetails.getPriceInRS(), order_id, "INR", "Astrologer Services", "In-App Store", "0", CGlobalVariables.TOPIC_SERVICES);


        //Log.e("Purchase plan End", "");


    }

    private void goToRazorPayFlow() {
/*

JSONObject notes = new JSONObject();
notes.put("key1", "value1");
notes.put("key2, "value2");
.
.
.
options.put("notes", notes);
 */
        final Checkout co = new Checkout();
        co.setFullScreenDisable(true);


        try {
            JSONObject options = new JSONObject();
            options.put("name", "AstroSage");
            options.put("description", servicelistModal.getTitle());
            //You can omit the image option to fetch the image from dashboard
            //    options.put("image", "http://astrosage.com/images/logo.png");
            options.put("currency", "INR");
            Double amount = Double.parseDouble(astrologerServiceInfo.getPriceRs());
            //Need to send amount to razor pay in paise
            Double paiseAmount = amount * 100;
            options.put("amount", paiseAmount);
            options.put("color", "#ff6f00");
            //options.put("order_id", razorpayOrderId);


            JSONObject preFill = new JSONObject();
            preFill.put("email", etUseremail.getText().toString().trim());
            preFill.put("contact", etUsermobno.getText().toString().trim());
            options.put("prefill", preFill);


            JSONObject notes = new JSONObject();
            //added by Ankit on 17-5-2019 for Razorpay Webhook
            notes.put("orderId", order_Id);
            notes.put("chatId", chat_Id);
            notes.put("orderType", CGlobalVariables.PAYMENT_TYPE_SERVICE);
            notes.put("appVersion", BuildConfig.VERSION_NAME);
            notes.put("appName", BuildConfig.APPLICATION_ID);
            notes.put("name", astrologerServiceInfo.getRegName());
            notes.put("firebaseinstanceid", com.ojassoft.astrosage.varta.utils.CUtils.getFirebaseAnalyticsAppInstanceId(this));
            notes.put("facebookinstanceid", com.ojassoft.astrosage.varta.utils.CUtils.getFacebookAnalyticsAppInstanceId(this));
            options.put("notes", notes);


            co.open(this, options);
        } catch (Exception e) {
            Toast.makeText(this, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults != null && grantResults.length > 0) {
            if (requestCode == PERMISSION_CONTACTS && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setEmailIdFromUserAccount();
            } else {
                boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0]);
                if (!showRationale) {
                    CUtils.saveBooleanData(this, CGlobalVariables.PERMISSION_KEY_CONTACTS, true);
                }
            }
        }
    }

    private void setmodalData(String allBirthDetail) {
        Log.d("all kundali data", allBirthDetail);
        Log.d("service data again", new Gson().toJson(servicelistModal));
        astrologerServiceInfo = new Gson().fromJson(allBirthDetail, AstrologerServiceInfo.class);
        if (astrologerServiceInfo == null)
            astrologerServiceInfo = new AstrologerServiceInfo();
        astrologerServiceInfo.setMobileNo(etUsermobno.getText().toString());
        astrologerServiceInfo.setEmailID(etUseremail.getText().toString());
        astrologerServiceInfo.setProblem(etUserquestion.getText().toString());
        astrologerServiceInfo = setDataModel();

        boolean razorPaymentVisibility = CUtils.getBooleanData(ActAstroServicePayment.this, CGlobalVariables.key_RazorPayVisibilityServices, true);
        boolean paytmPaymentVisibility = CUtils.getBooleanData(ActAstroServicePayment.this, CGlobalVariables.key_PaytmWalletVisibilityServices, true);

        if (razorPaymentVisibility && !paytmPaymentVisibility) {
            onSelectedOption(R.id.radioRazor, "");
        } else if (!razorPaymentVisibility && paytmPaymentVisibility) {
            //   astrologerServiceInfo = CUtils.setDataModel(this, beanHoroPersonalInfo, "Paytm", servicelistModal);
            onSelectedOption(R.id.radioPaytm, "");
        } else {
            launchBrihatPaymentScreen();
            /*Fragment diog = getSupportFragmentManager().findFragmentByTag("Dialog");
            if (diog == null) {
                ChooseServicePayOPtionDialog dialog = new ChooseServicePayOPtionDialog();
                dialog.show(getSupportFragmentManager(), "Dialog");

            }*/
        }
    }

    private ArrayAdapter<CharSequence> getSpinnerAdapterLanguage(final String[] spinnerOptions) {
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(ActAstroServicePayment.this, R.layout.spinner_list_item, spinnerOptions) {
            public View getView(int position, View convertView, ViewGroup parent) {
                if(spinnerOptions != null && spinnerOptions.length == 1 && position == 1){
                    position = 0;
                }
                View v = super.getView(position, convertView, parent);
                if (position == CGlobalVariables.HINDI) {
                    ((TextView) v).setTypeface(CustomTypefaces.get(ActAstroServicePayment.this, "hi")); ///cmnt by neeraj 5/5/16
                } else {
                    ((TextView) v).setTypeface(robotRegularTypeface); //cmnt by neeraj 5/5/16
                }
                ((TextView) v).setTextSize(16);
                ((TextView) v).setPadding(5, 10, 10, 0);
                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);

                if (position == CGlobalVariables.HINDI) {
                    ((TextView) v).setTypeface(CustomTypefaces.get(ActAstroServicePayment.this, "hi")); //cmnt by neeraj 5/5/16

                } else {
                    ((TextView) v).setTypeface(robotRegularTypeface); //cmnt by neeraj 5/5/16
                }
                ((TextView) v).setTextSize(16);

                return v;
            }
        };
        return adapter;
    }


    private String[] getLangForAvailableReport(String langString) {

        List<String> availableLang = new ArrayList<>();
        HashMap<String, String> langOption = new HashMap<String, String>();
        langOption.put("0", getResources().getString(R.string.english));
        langOption.put("1", getResources().getString(R.string.hindi));
        langOption.put("2", getResources().getString(R.string.tamil));
        langOption.put("4", getResources().getString(R.string.kannad));
        langOption.put("5", getResources().getString(R.string.telugu));
        langOption.put("6", getResources().getString(R.string.bangali));
        langOption.put("7", getResources().getString(R.string.gujarati));
        langOption.put("8", getResources().getString(R.string.malayalam));
        langOption.put("9", getResources().getString(R.string.marathi));
        langOption.put("13", getResources().getString(R.string.odia));
        langOption.put("14", getResources().getString(R.string.assamese));
        //international
//        langOption.put("16", getResources().getString(R.string.spanish));
//        langOption.put("17", getResources().getString(R.string.chinese));
//        langOption.put("18", getResources().getString(R.string.japanese));
//        langOption.put("19", getResources().getString(R.string.portugese));
//        langOption.put("20", getResources().getString(R.string.german));
//        langOption.put("21", getResources().getString(R.string.italian));
        if (langString != null && langString.length() > 0) {
            String[] langArray = langString.split(",");
            for (int i = 0; i < langArray.length; i++) {
                //This condition is for gujrati language:Amit Sharma
                if (langArray[i].equals("7") && android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
                    continue;
                }
                availableLang.add(langOption.get(langArray[i]));
            }
        } else {

            availableLang.add(langOption.get("0"));
            availableLang.add(langOption.get("1"));
        }

        String[] availableLangArray = availableLang.toArray(new String[availableLang.size()]);
        return availableLangArray;
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            if (charSequence.toString().length() == 1) {
                if (!charSequence.toString().matches("[a-zA-Z ]+") && !charSequence.toString().matches("[0-9]") && !charSequence.toString().matches("'+'")) {
                    //   mct.show(getResources().getString(R.string.astro_shop_valid_text));
                }
            }
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {

                case R.id.etUseremail:
                    validateName(etUseremail, input_layout_email, getString(R.string.email_one_v));

                    if (etUseremail.getText().toString().trim().isEmpty()) {
                        input_layout_email.setError(getResources().getString(R.string.email_one_v));
                        requestFocus(etUseremail);
                        etUseremail.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);

                    } else if (!(CUtils.isValidEmail(etUseremail.getText().toString().trim()))) {
                        input_layout_email.setError(getResources().getString(R.string.email_one_v_astro_service));
                        requestFocus(etUseremail);
                        etUseremail.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);

                    } else {
                        input_layout_email.setErrorEnabled(false);
                        etUseremail.getBackground().setColorFilter(null);
                        etUseremail.getBackground().setColorFilter(getResources().getColor(R.color.ColorPrimary), PorterDuff.Mode.SRC_ATOP);
                    }

                    String charSequence1 = etUseremail.getText().toString().trim();
                    if (charSequence1.toString().length() == 1) {
                        if (!charSequence1.toString().matches("[a-zA-Z ]+") && !charSequence1.toString().matches("[0-9]")) {
                            mct.show(getResources().getString(R.string.astro_shop_valid_text));
                            etUseremail.setText("");
                        }
                    }

                    break;

                case R.id.etUserquestion:
                    validateName(etUserquestion, input_layout_question, getString(R.string.astrologer_user_query));


                    String charSequence2 = etUserquestion.getText().toString().trim();
                    if (charSequence2.toString().length() == 1) {
//                        if (!charSequence2.toString().matches("[a-zA-Z ]+") && !charSequence2.toString().matches("[0-9]")) {
//                            mct.show(getResources().getString(R.string.astro_shop_valid_text));
//                            edt_address.setText("");
//                        }
                    }

                    break;
                case R.id.etUsermobno:
                    validateName(etUsermobno, input_layout_mobno, getString(R.string.astro_shop_User_mob_no));
                    break;


            }
        }
    }

    @Override
    public void onVolleySuccess(String result, Cache cache) {

        try {
            hideProgressBar();
            //JSONObject jsonObject = new JSONObject(result);
            //goToRazorPayFlow(jsonObject.optString("id"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onVolleyError(VolleyError result) {
        hideProgressBar();
    }
    /**
     * show Progress Bar
     */
    private void showProgressBar() {
        if (pd == null) {
            pd = new CustomProgressDialog(ActAstroServicePayment.this, regularTypeface);
        }

        if (!pd.isShowing()) {
            pd.setCanceledOnTouchOutside(false);
            pd.show();
        }
    }

    /**
     * hide Progress Bar
     */
    private void hideProgressBar() {
        try {
            if (pd != null && pd.isShowing())
                pd.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*************************************** Paytm Transaction ************************************************************/

    Map<String, String> getchecksumparams() {
        String email = astrologerServiceInfo.getEmailID();
        Map<String, String> params = new HashMap<>();

        params.put("key", CUtils.getApplicationSignatureHashCode(this));
        params.put("MID", "Ojasso36077880907527");
        params.put("ORDER_ID", order_Id);
        params.put("WEBSITE", "OjassoWAP");
        params.put("CALLBACK_URL", CGlobalVariables.CALLBACK_URL + order_Id);
        params.put("TXN_AMOUNT", astrologerServiceInfo.getPriceRs());
        params.put("CUST_ID", email);

        chat_Id = chat_Id.equalsIgnoreCase("") ? "0" : chat_Id;
        String extraData = "chatId_" + chat_Id + "_type_" + PAYMENT_TYPE_SERVICE
                + "_appVersion_" + BuildConfig.VERSION_NAME + "_appName_" + BuildConfig.APPLICATION_ID+
                "_firebaseinstanceid_"+ com.ojassoft.astrosage.varta.utils.CUtils.getFirebaseAnalyticsAppInstanceId(this)+
                "_facebookinstanceid_"+ com.ojassoft.astrosage.varta.utils.CUtils.getFacebookAnalyticsAppInstanceId(this);

        params.put("MERC_UNQ_REF", extraData);
        Log.e("TestData",extraData);
        return params;
    }

    private void startPaytmPayment(String oId, String amount, String tnxToken) {

        String midString = CGlobalVariables.PAYTM_MID;
        String callBackUrl = CGlobalVariables.CALLBACK_URL + oId;

        PaytmOrder paytmOrder = new PaytmOrder(oId, midString, tnxToken, amount, callBackUrl);

        TransactionManager transactionManager = new TransactionManager(paytmOrder, new PaytmPaymentTransactionCallback() {

            @Override
            public void onTransactionResponse(Bundle bundle) {
                try {
                    String status = bundle.getString("STATUS");
                    processPaytmTransaction(status);
                } catch (Exception e) {
                    //
                }
            }

            @Override
            public void networkNotAvailable() {

            }

            @Override
            public void onErrorProceed(String s) {

            }

            @Override
            public void clientAuthenticationFailed(String s) {

            }

            @Override
            public void someUIErrorOccurred(String s) {

            }

            @Override
            public void onErrorLoadingWebPage(int i, String s, String s1) {

            }

            @Override
            public void onBackPressedCancelTransaction() {

            }

            @Override
            public void onTransactionCancel(String s, Bundle bundle) {

            }
        });
        //transactionManager.setAppInvokeEnabled(false);
        transactionManager.setShowPaymentUrl(CGlobalVariables.PAYTM_PAYMENT_URL);
        transactionManager.startTransaction(this, CGlobalVariables.REQUEST_CODE_PAYTM);
    }

    private void processPaytmTransaction(String status) {
        if (status.equals(CGlobalVariables.TXN_SUCCESS)) {
            payStatus = "1";
        } else {
            payStatus = "0";
        }

        if (!payStatus.equals("0")) { //sucess

            double dPrice = 0.0;
            String price = "";
            try {
                if (astrologerServiceInfo != null) {
                    if (astrologerServiceInfo.getPriceRs() != null && astrologerServiceInfo.getPriceRs().trim().length() > 0) {
                        price = astrologerServiceInfo.getPriceRs();
                        dPrice = Double.valueOf(price);
                    }
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            // in case of purchase event not added by server then add event
            if (!com.ojassoft.astrosage.utils.CUtils.isPurchaseEventAddedByServer(this)) {
                CUtils.googleAnalyticSendWitPlayServieForPurchased(this,
                        CGlobalVariables.GOOGLE_ANALYTIC_ASTROSHOP_SERVICE,
                        CGlobalVariables.GOOGLE_ANALYTIC_SERVICE_PAYMENT_SUCCESS, null, dPrice, "");
            }
        }
        CUtils.postDataToServer(ActAstroServicePayment.this, mediumTypeface, queue, order_Id, "", payStatus, astrologerServiceInfo, CGlobalVariables.UPDATE_PAY_STATUS_ASKQUE);

    }

    private void payViaWallet() {
        if (!CUtils.isConnectedWithInternet(this)) {
            MyCustomToast mct = new MyCustomToast(ActAstroServicePayment.this, ActAstroServicePayment.this
                    .getLayoutInflater(), ActAstroServicePayment.this, typeface);
            mct.show(getResources().getString(R.string.no_internet));
        } else {
            Call<ResponseBody> call = RetrofitClient
                    .getInstance().create(ApiList.class)
                    .purchaseReportFromWallet(getPurchaseFromWalletParams());

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if (response.isSuccessful()) {
                            String rBody = response.body().string();
                            JSONObject jsonObject = new JSONObject(rBody);
                            String status = jsonObject.optString("status");
                            if (!TextUtils.isEmpty(status)) {
                                if (status.equals("1")) {

                                } else {

                                }
                            }
                            Log.d(TAG, "onResponse: "+rBody);
                        }
                    } catch (Exception e) {
                        Log.d(TAG, "onResponse e: "+e);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                    Log.d(TAG, "onFailure: "+throwable);
                }
            });
        }
    }

    private Map<String, String> getPurchaseFromWalletParams() {
        Map<String, String> map = new HashMap<>();
        map.put(KEY_API,CUtils.getApplicationSignatureHashCode(this));
        map.put(COUNTRY_CODE, getCountryCode(this));
        map.put(USER_PHONE_NO, getUserID(this));
        map.put("orderid", order_Id);
        map.put(PACKAGE_NAME, BuildConfig.APPLICATION_ID);
        map.put(APP_VERSION, BuildConfig.VERSION_NAME);
        map.put(DEVICE_ID, CUtils.getMyAndroidId(this));
        return map;
    }

    private void launchBrihatPaymentScreen() {
        //old method
        Fragment diog = getSupportFragmentManager().findFragmentByTag("Dialog");
        if (diog == null) {
            ChooseServicePayOPtionDialog dialog = new ChooseServicePayOPtionDialog();
            dialog.show(getSupportFragmentManager(), "Dialog");
        }

        //new method
        /*Intent myIntent = new Intent(this, BrihatPaymentInformationActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putSerializable("key", servicelistModal);
        myIntent.putExtras(mBundle);
        resultLauncher.launch(myIntent);*/

    }

}
