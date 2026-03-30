package com.ojassoft.astrosage.varta.ui.activity;

import android.app.Activity;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.VolleyError;
import com.google.android.material.tabs.TabLayout;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.ui.fragments.ChooseExpertFragmentDailog;
import com.ojassoft.astrosage.varta.ui.fragments.ChooseVartaLanguageFragmentDailog;
import com.ojassoft.astrosage.varta.ui.fragments.JoinConfirmationDailogFrag;
import com.ojassoft.astrosage.varta.ui.fragments.VerifyOtpDailogFrag;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.CustomProgressDialog;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;
import static com.ojassoft.astrosage.varta.utils.CUtils.showSnackbar;

/**
 * Created By Abhishek Raj
 */
public class VartaReqJoinActivity extends BaseActivity implements View.OnClickListener, VolleyResponse {

    private final int USER_REGISTRATION = 1;
    private final int OTP_VERIFICATION = 2;
    private final int OTP_RESEND = 3;

    private Activity currentActivity;
    private Toolbar tool_barAppModule;
    private TextView tvTitle;
    private CustomProgressDialog pd;
    private TextView heading3TV;
    private EditText edtFirstName;
    private EditText edtLastName;
    private EditText edtCountryCode;
    private EditText edtMobileNo;
    private EditText edtMailId;
    private EditText edtLang;
    private EditText edtExpert;
    private EditText edtExperience;
    private EditText edtCity;
    private EditText edtCountry;
    private EditText edtShortBio;
    private Button btnSignUp;
    private TextView agreeTextView;
    private CheckBox agreeCheckBox;
    private ImageView backIV;
    private String fName = "", lName = "", emailId = "", mobileNumber = "";
    private boolean isChartExits;
    private boolean isGetChartCalled;
    private String respCode = "";
    private String sectedLanguages = "";
    private String sectedExperts = "";
    private String countryCode = "+91";
    private Spinner spinnerGender;
    private ArrayAdapter arrayAdapterGender;


    /*public VartaReqJoinActivity() {
        super(R.string.app_name);
    }*/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_varta_req_join);
        initContext();
        initViews();
        initListener();
    }

    private void initViews() {
        tool_barAppModule = findViewById(R.id.tool_barAppModule);
        setSupportActionBar(tool_barAppModule);

        backIV = findViewById(R.id.ivBack);
        tvTitle = findViewById(R.id.tvTitle);
        heading3TV = findViewById(R.id.heading3TV);
        edtFirstName = findViewById(R.id.edtFirstName);
        edtLastName = findViewById(R.id.edtLastName);
        edtCountryCode = findViewById(R.id.edtCountryCode);
        edtMobileNo = findViewById(R.id.edtMobileNo);
        edtMailId = findViewById(R.id.edtMailId);
        edtLang = findViewById(R.id.edtLang);
        btnSignUp = findViewById(R.id.btnSignUp);
        agreeTextView = findViewById(R.id.agree_text);
        agreeCheckBox = findViewById(R.id.agreeCheckBox);
        edtExperience = findViewById(R.id.edtExperience);
        edtCity = findViewById(R.id.edtCity);
        edtCountry = findViewById(R.id.edtCountry);
        edtShortBio = findViewById(R.id.edtShortBio);
        edtExpert = findViewById(R.id.edtExpert);
        spinnerGender = findViewById(R.id.spinner_gender);


        tvTitle.setText(getString(R.string.sign_up));
        FontUtils.changeFont(VartaReqJoinActivity.this, tvTitle, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);

        setSpannableString();
        FontUtils.changeFont(VartaReqJoinActivity.this, heading3TV, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(VartaReqJoinActivity.this, btnSignUp, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);

        FontUtils.changeFont(VartaReqJoinActivity.this, edtFirstName, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(VartaReqJoinActivity.this, edtLastName, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(VartaReqJoinActivity.this, edtCountryCode, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(VartaReqJoinActivity.this, edtMobileNo, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(VartaReqJoinActivity.this, edtMailId, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(VartaReqJoinActivity.this, edtLang, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(VartaReqJoinActivity.this, agreeTextView, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(VartaReqJoinActivity.this, edtExpert, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(VartaReqJoinActivity.this, edtExperience, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(VartaReqJoinActivity.this, edtCity, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(VartaReqJoinActivity.this, edtCountry, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(VartaReqJoinActivity.this, edtShortBio, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        arrayAdapterGender=new ArrayAdapter<String>(VartaReqJoinActivity.this, android.R.layout.simple_list_item_1,
                new String[]{getResources().getString(R.string.male), getResources().getString(R.string.female)});
        spinnerGender.setAdapter(arrayAdapterGender);

    }

    private void initContext() {
        currentActivity = VartaReqJoinActivity.this;
    }

    private void initListener() {
        btnSignUp.setOnClickListener(this);
        agreeCheckBox.setOnClickListener(this);
        agreeTextView.setOnClickListener(this);
        edtExpert.setOnClickListener(this);
        edtLang.setOnClickListener(this);

        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) { }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignUp: {
                doActionOnSignupButton();
                break;
            }
            case R.id.agreeCheckBox: {
                if (agreeCheckBox.isChecked()) {
                    btnSignUp.setEnabled(true);
                    btnSignUp.setAlpha(1);
                } else {
                    btnSignUp.setEnabled(false);
                    btnSignUp.setAlpha(0.6f);
                }
                break;
            }
            case R.id.agree_text: {
                openTermsUseUrl();
                break;
            }
            case R.id.edtLang: {
                openLanguageSelectDialog();
                break;
            }
            case R.id.edtExpert: {
                openExpertSelectDialog();
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

    private void setSpannableString() {
        SpannableString ss = new SpannableString(getResources().getString(R.string.agree_text_varta));
        ClickableSpan span1 = new ClickableSpan() {
            @Override
            public void onClick(View textView) {
                openTermsUseUrl();
            }
        };

        final ForegroundColorSpan fcs = new ForegroundColorSpan(Color.rgb(105, 105, 105));
        final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD);

        //int languageCode = ((AstrosageKundliApplication) this.getApplication()).getLanguageCode();
        //int languageCode = CGlobalVariables.ENGLISH;
        LANGUAGE_CODE = AstrosageKundliApplication.getAppContext().getLanguageCode();

        if (LANGUAGE_CODE == CGlobalVariables.ENGLISH) {
            ss.setSpan(bss, 43, 55, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            ss.setSpan(span1, 43, 55, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            ss.setSpan(fcs, 43, 55, Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        }

        agreeTextView.setText(ss);
        agreeTextView.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void openTermsUseUrl() {
        String url = CGlobalVariables.VARTA_TERMS_URL;
        CUtils.openWebBrowser(currentActivity, Uri.parse(url));
    }

    @Override
    public void onResponse(String response, int method) {
        hideProgressBar();
        //Log.e("JoinReqResponse", "method = "+method+response);
        if (method == USER_REGISTRATION) {
            try {
                JSONObject respObj = new JSONObject(response);
                String status = respObj.getString("status");
                /* 1 for success, 2 for mobile already exits, 3 for mobile otp not verified, 4 for email-id already exits*/
                if (status.equalsIgnoreCase("1")) {
                    openVerifyOtpDialog();
                } else if (status.equalsIgnoreCase("2") || status.equalsIgnoreCase("3")) {
                    showSnackbar(btnSignUp, getString(R.string.mobile_already_reg),VartaReqJoinActivity.this);
                } else if (status.equalsIgnoreCase("4")) {
                    showSnackbar(btnSignUp, getString(R.string.email_already_reg),VartaReqJoinActivity.this);
                } else {
                    showSnackbar(btnSignUp, getResources().getString(R.string.server_error_msg),VartaReqJoinActivity.this);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                showSnackbar(btnSignUp, e.toString(),VartaReqJoinActivity.this);
            }
        } else if (method == OTP_VERIFICATION) {
            try {
                JSONObject respObj = new JSONObject(response);
                String status = respObj.getString("status");
                if (status.equalsIgnoreCase("1")) { //success
                    openConfirmationDialog();
                    try {
                        //CUtils.subscribeTopics("", CGlobalVariables.TOPIC_JOIN_REQ_ASTROLOGER, currentActivity);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else if (status.equalsIgnoreCase("2")) { //failed
                    showSnackbar(btnSignUp, getString(R.string.incorrect_otp),VartaReqJoinActivity.this);
                } else if (status.equalsIgnoreCase("3")) { //failed
                    showSnackbar(btnSignUp, getString(R.string.expired_otp),VartaReqJoinActivity.this);
                } else {
                    showSnackbar(btnSignUp, getResources().getString(R.string.server_error_msg),VartaReqJoinActivity.this);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                showSnackbar(btnSignUp, e.toString(),VartaReqJoinActivity.this);
            }
        } else if (method == OTP_RESEND) {
            try {
                JSONObject respObj = new JSONObject(response);
                String status = respObj.getString("status");
                if (status.equalsIgnoreCase("1")) { //success
                    showSnackbar(btnSignUp, getString(R.string.recend_otp),VartaReqJoinActivity.this);
                } else if (status.equalsIgnoreCase("2")) { //already verified
                    showSnackbar(btnSignUp, getString(R.string.mobile_already_verified),VartaReqJoinActivity.this);
                } else if (status.equalsIgnoreCase("3")) { //not valid
                    showSnackbar(btnSignUp, getString(R.string.mobile_not_valid),VartaReqJoinActivity.this);
                } else {
                    showSnackbar(btnSignUp, getResources().getString(R.string.server_error_msg),VartaReqJoinActivity.this);
                }
            } catch (JSONException e) {
                e.printStackTrace();
                showSnackbar(btnSignUp, e.toString(),VartaReqJoinActivity.this);
            }
        }
    }

    @Override
    public void onError(VolleyError error) {
        hideProgressBar();
        //Log.e("JoinReqResponse error", error.toString());
        showSnackbar(btnSignUp, error.getMessage(),VartaReqJoinActivity.this);
    }

    private void doActionOnSignupButton() {
        CUtils.hideMyKeyboard(currentActivity);
        if (validateData()) {
            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_ACTION_ASTROSAGE_VARTA_JOIN_REQ, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
            registerUser();
        }
    }

    private boolean validateData() {
        boolean flag = false;
        if (validateName(edtFirstName, getString(R.string.enter_first_name)) && validateName(edtMobileNo, getString(R.string.enter_mobile_number)) && validateName(edtMailId, getString(R.string.email_one_v)) && validateName(edtExperience, getString(R.string.enter_experience))
                && validateName(edtCity, getString(R.string.enter_city)) && validateName(edtCountry, getString(R.string.enter_country)) && validateName(edtExpert, getString(R.string.select_system_known)) && validateName(edtLang, getString(R.string.msg_choose_lang_title)) && validateName(edtShortBio, getString(R.string.enter_bio)))
            flag = true;

        return flag;
    }

    private boolean validateName(EditText nameETV, String message) {
        if (nameETV == edtMailId) {
            if (nameETV.getText().toString().trim().length() < 1) {
                requestFocus(nameETV);
                showSnackbar(nameETV, message,VartaReqJoinActivity.this);
                return false;
            } else if (nameETV.getText().toString().trim().length() > 70) {
                requestFocus(nameETV);
                showSnackbar(nameETV, getResources().getString(R.string.email_two_v),VartaReqJoinActivity.this);
                return false;
            } else if (!CUtils.isValidEmail(nameETV.getText().toString().trim())) {
                requestFocus(nameETV);
                showSnackbar(nameETV, getResources().getString(R.string.email_three_v),VartaReqJoinActivity.this);
                return false;
            }
        }
        if (nameETV == edtMobileNo) {
            if (nameETV.getText().toString().trim().length() < 1) {
                requestFocus(nameETV);
                showSnackbar(nameETV, message,VartaReqJoinActivity.this);
                return false;
            } else if (nameETV.getText().toString().trim().length() < 10) {
                requestFocus(nameETV);
                showSnackbar(nameETV, getResources().getString(R.string.phone_one_v_astro_service),VartaReqJoinActivity.this);
                return false;
            }
        } else if (nameETV.getText().toString().trim().isEmpty()) {
            requestFocus(nameETV);
            showSnackbar(nameETV, message,VartaReqJoinActivity.this);
            return false;
        }
        if (!CUtils.isConnectedWithInternet(currentActivity)) {
            showSnackbar(btnSignUp, getResources().getString(R.string.no_internet),VartaReqJoinActivity.this);
            return false;
        }
        return true;
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    public void registerUser() {
        fName = edtFirstName.getText().toString();
        lName = edtLastName.getText().toString();
        mobileNumber = edtMobileNo.getText().toString();
        emailId = edtMailId.getText().toString();

        if (CUtils.isConnectedWithInternet(currentActivity)) {
            showProgressBar();
            CUtils.sendJoinRequest(VartaReqJoinActivity.this, getJoinRequestParams(), USER_REGISTRATION);
        } else {
            showSnackbar(btnSignUp, getResources().getString(R.string.no_internet),VartaReqJoinActivity.this);
        }
    }

    public Map<String, String> getJoinRequestParams() {

        int genderPosition = spinnerGender.getSelectedItemPosition();
        String genderStr;
        if(genderPosition == 0){
            genderStr="male";
        } else {
            genderStr="female";
        }

        fName = edtFirstName.getText().toString();
        lName = edtLastName.getText().toString();
        mobileNumber = edtMobileNo.getText().toString();
        emailId = edtMailId.getText().toString();

        String deviceId = CUtils.getMyAndroidId(currentActivity);
        String key = CUtils.getApplicationSignatureHashCode(currentActivity);
        String regid = CUtils.getRegistrationId(currentActivity);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put(CGlobalVariables.KEY_API, key);
        params.put("device_id", deviceId);
        params.put("token_id", regid);
        params.put("methodName", "registerShort2");
        params.put("isapi", "1");
        params.put("name", fName);
        params.put("lname", lName);
        params.put("email", emailId);
        params.put("mobileno", mobileNumber);
        params.put("lang", sectedLanguages);
        params.put("experience", edtExperience.getText().toString());
        params.put("city", edtCity.getText().toString());
        params.put("country", edtCountry.getText().toString());
        params.put("expert", sectedExperts);
        params.put("description", edtShortBio.getText().toString());
        params.put(CGlobalVariables.REG_SOURCE, CGlobalVariables.APP_SOURCE);

        params.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(VartaReqJoinActivity.this));
        params.put("gender", genderStr);
        //params.put(CGlobalVariables.LANG,CUtils.getLanguageKey(LANGUAGE_CODE));
        //Log.e("JoinReqResponse", "params="+params.toString());
        return params;
    }
    public void openLanguageSelectDialog() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        FragmentManager fm = getSupportFragmentManager();
        Fragment prev = fm.findFragmentByTag("ChooseVartaLanguageFragmentDailog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        ChooseVartaLanguageFragmentDailog fragmentDailog = new ChooseVartaLanguageFragmentDailog();
        Bundle bundle = new Bundle();
        bundle.putString(CGlobalVariables.KEY_LANGUAGES, sectedLanguages);
        fragmentDailog.setArguments(bundle);
        fragmentDailog.show(fm, "ChooseVartaLanguageFragmentDailog");
        ft.commit();
    }

    public void setSelectedLanguage(String sectedLanguages, String sectedLanguagesToDisplay) {
        this.sectedLanguages = sectedLanguages;
        edtLang.setText(sectedLanguagesToDisplay);
    }

    private void openVerifyOtpDialog() {
        Bundle bundle = new Bundle();
        bundle.putString(CGlobalVariables.MOBILE, edtMobileNo.getText().toString());
        bundle.putString(CGlobalVariables.COUNTRY_CODE, countryCode);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        VerifyOtpDailogFrag pfdf = new VerifyOtpDailogFrag();
        pfdf.setArguments(bundle);
        ft.add(pfdf, null);
        ft.commitAllowingStateLoss();
    }

    public void submitOtp(String otp) {
        if (CUtils.isConnectedWithInternet(currentActivity)) {
            showProgressBar();
            CUtils.verifyOtpRequest(VartaReqJoinActivity.this, mobileNumber, otp, OTP_VERIFICATION);
        } else {
            showSnackbar(btnSignUp, getResources().getString(R.string.no_internet),VartaReqJoinActivity.this);
        }
    }

    public void resendOtp() {
        if (CUtils.isConnectedWithInternet(currentActivity)) {
            showProgressBar();
            CUtils.resendOtpRequest(VartaReqJoinActivity.this, mobileNumber, OTP_RESEND);
        } else {
            showSnackbar(btnSignUp, getResources().getString(R.string.no_internet),VartaReqJoinActivity.this);
        }
    }

    private void openConfirmationDialog() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        JoinConfirmationDailogFrag pfdf = new JoinConfirmationDailogFrag();
        ft.add(pfdf, null);
        ft.commitAllowingStateLoss();
    }

    private void showProgressBar() {
        pd = new CustomProgressDialog(this);
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

    public void goToHome() {
        finish();
    }

    public void openExpertSelectDialog() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        FragmentManager fm = getSupportFragmentManager();
        Fragment prev = fm.findFragmentByTag("ChooseExpertFragmentDailog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        ChooseExpertFragmentDailog fragmentDailog = new ChooseExpertFragmentDailog();
        Bundle bundle = new Bundle();
        bundle.putString(CGlobalVariables.KEY_EXPERTS, sectedExperts);
        fragmentDailog.setArguments(bundle);
        fragmentDailog.show(fm, "ChooseExpertFragmentDailog");
        ft.commit();
    }

    public void setSelectedExperts(String sectedExperts, String sectedExpertssToDisplay) {
        this.sectedExperts = sectedExperts;
        edtExpert.setText(sectedExpertssToDisplay);
    }

}
