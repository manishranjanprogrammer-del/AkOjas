package com.ojassoft.astrosage.ui.act;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Cache;
import com.android.volley.VolleyError;
import com.google.android.material.tabs.TabLayout;
import com.libojassoft.android.misc.VolleyResponse;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.ui.fragments.ChooseExpertFragmentDailog;
import com.ojassoft.astrosage.ui.fragments.ChooseVartaLanguageFragmentDailog;
import com.ojassoft.astrosage.ui.fragments.JoinConfirmationDailogFrag;
import com.ojassoft.astrosage.ui.fragments.VerifyOtpDailogFrag;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.varta.adapters.CountryListAdapter;
import com.ojassoft.astrosage.varta.interfacefile.RecyclerClickListner;
import com.ojassoft.astrosage.varta.model.CountryBean;
import com.ojassoft.astrosage.varta.ui.activity.DashBoardActivity;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.REG_SOURCE;

/**
 * Created By Abhishek Raj
 */
public class VartaReqJoinActivity extends BaseInputActivity implements View.OnClickListener, VolleyResponse {

    private final int USER_REGISTRATION = 1;
    private final int OTP_VERIFICATION = 2;
    private final int OTP_RESEND = 3;
    private final int GET_COUNTRY_CODE = 4;
    RelativeLayout country_code_layout;
    private Activity currentActivity;
    private Toolbar tool_barAppModule;
    private TabLayout tabLayout;
    private TextView tvTitle;
    private CustomProgressDialog pd;
    private TextView heading3TV;
    private EditText edtFirstName;
    private EditText edtLastName;
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
    private String fName = "", lName = "", emailId = "", mobileNumber = "";
    private String sectedLanguages = "";
    private String sectedExperts = "";
    private String countryCode = "91";
    private CountryListAdapter adapter;
    private ArrayList<CountryBean> countryBeanList = null;
    private TextView countryCodeText;
    private VerifyOtpDailogFrag otpDailogFrag;

    private Spinner spinnerGender;
    private ArrayAdapter arrayAdapterGender;


    public VartaReqJoinActivity() {
        super(R.string.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_varta_req_join);
        initContext();
        initViews();
        initListener();
        checkCacheCountryListData();

        /*Intent intent = new Intent(VartaReqJoinActivity.this, JoinVartaProfileImageActivity.class);
        intent.putExtra("countryCode", "91");
        intent.putExtra("mobileNo", "9999283024");
        startActivityForResult(intent, 1);
*/
    }

    private void initViews() {
        tool_barAppModule = findViewById(R.id.tool_barAppModule);
        // Get the navigation icon drawable
        Drawable navIcon = ContextCompat.getDrawable(this, R.drawable.ic_back_arrow);

// Check if the drawable is not null
        if (navIcon != null) {
            // Tint the drawable with the desired color
            navIcon.setTint(ContextCompat.getColor(this, R.color.black));

            // Set the tinted drawable as the navigation icon
            tool_barAppModule.setNavigationIcon(navIcon);
        }
        setSupportActionBar(tool_barAppModule);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvTitle = findViewById(R.id.tvTitle);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setVisibility(View.GONE);
        heading3TV = findViewById(R.id.heading3TV);
        edtFirstName = findViewById(R.id.edtFirstName);
        edtLastName = findViewById(R.id.edtLastName);
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
        country_code_layout = findViewById(R.id.country_code_layout);
        countryCodeText = findViewById(R.id.country_code);
        spinnerGender = findViewById(R.id.spinner_gender);


        tvTitle.setText(getString(R.string.title_join_varta));
        tvTitle.setTypeface(regularTypeface);

        setSpannableString();

        heading3TV.setTypeface(mediumTypeface);
        edtFirstName.setTypeface(regularTypeface);
        edtLastName.setTypeface(regularTypeface);
        countryCodeText.setTypeface(regularTypeface);
        edtMobileNo.setTypeface(regularTypeface);
        edtMailId.setTypeface(regularTypeface);
        edtLang.setTypeface(regularTypeface);
        agreeTextView.setTypeface(regularTypeface);
        btnSignUp.setTypeface(mediumTypeface);
        edtExpert.setTypeface(regularTypeface);
        edtExperience.setTypeface(regularTypeface);
        edtCity.setTypeface(regularTypeface);
        edtCountry.setTypeface(regularTypeface);
        edtShortBio.setTypeface(regularTypeface);

        arrayAdapterGender = new ArrayAdapter<String>(VartaReqJoinActivity.this, android.R.layout.simple_list_item_1,
                new String[]{getResources().getString(R.string.male), getResources().getString(R.string.female)});
        spinnerGender.setAdapter(arrayAdapterGender);


    }

    private void initContext() {
        currentActivity = VartaReqJoinActivity.this;
    }

    private void initListener() {
        btnSignUp.setOnClickListener(this);
        agreeCheckBox.setOnClickListener(this);
        edtLang.setOnClickListener(this);
        agreeTextView.setOnClickListener(this);
        edtExpert.setOnClickListener(this);
        edtLang.setOnClickListener(this);
        country_code_layout.setOnClickListener(this);

        spinnerGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSignUp: {
                doActionOnSignupButton();
                /*Intent intent = new Intent(VartaReqJoinActivity.this, JoinVartaProfileImageActivity.class);
                intent.putExtra("countryCode", "91");
                intent.putExtra("mobileNo", "8010722646");
                startActivityForResult(intent, 1);*/
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
            case R.id.country_code_layout:
                showDialog();
                break;
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

        int languageCode = ((AstrosageKundliApplication) this.getApplication()).getLanguageCode();
        if (languageCode == CGlobalVariables.ENGLISH) {
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
        //Log.e("JoinReqResponse", "method = " + method + response);
        if (method == USER_REGISTRATION) {
            try {
                JSONObject respObj = new JSONObject(response);
                String status = respObj.getString("status");
                /* 1 for success, 2 for mobile already exits, 3 for mobile otp not verified, 4 for email-id already exits*/
                if (status.equalsIgnoreCase("1") || status.equalsIgnoreCase("3")) {
                    openVerifyOtpDialog();
                } else if (status.equalsIgnoreCase("2")) {
                    showSnackbar(btnSignUp, getString(R.string.mobile_already_reg));
                } else if (status.equalsIgnoreCase("4")) {
                    showSnackbar(btnSignUp, getString(R.string.email_already_reg));
                } else {
                    showSnackbar(btnSignUp, getResources().getString(R.string.server_error_msg));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                showSnackbar(btnSignUp, e.toString());
            }
        } else if (method == OTP_VERIFICATION) {
            try {
                JSONObject respObj = new JSONObject(response);
                String status = respObj.getString("status");
                if (status.equalsIgnoreCase("1")) { //success
                    //Intent intent = new Intent(VartaReqJoinActivity.this, JoinVartaProfileImageActivity.class);
                    //intent.putExtra("countryCode", countryCode);
                    //intent.putExtra("mobileNo", edtMobileNo.getText().toString());
                    //startActivityForResult(intent, 1);
                    if (otpDailogFrag != null) {
                        otpDailogFrag.dismiss();
                    }
                    openConfirmationDialog();
                } else if (status.equalsIgnoreCase("2")) { //failed
                    showSnackbar(btnSignUp, getString(R.string.incorrect_otp));
                } else if (status.equalsIgnoreCase("3")) { //failed
                    showSnackbar(btnSignUp, getString(R.string.expired_otp));
                } else {
                    showSnackbar(btnSignUp, getResources().getString(R.string.server_error_msg));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                showSnackbar(btnSignUp, e.toString());
            }
        } else if (method == OTP_RESEND) {
            try {
                JSONObject respObj = new JSONObject(response);
                String status = respObj.getString("status");
                if (status.equalsIgnoreCase("1")) { //success
                    showSnackbar(btnSignUp, getString(R.string.recend_otp));
                } else if (status.equalsIgnoreCase("2")) { //already verified
                    showSnackbar(btnSignUp, getString(R.string.mobile_already_verified));
                } else if (status.equalsIgnoreCase("3")) { //not valid
                    showSnackbar(btnSignUp, getString(R.string.mobile_not_valid));
                } else {
                    showSnackbar(btnSignUp, getResources().getString(R.string.server_error_msg));
                }
            } catch (JSONException e) {
                e.printStackTrace();
                showSnackbar(btnSignUp, e.toString());
            }
        } else if (method == GET_COUNTRY_CODE) {
            parseCountryList(response);
            if (adapter != null) {
                adapter.notifyDataSetChanged();
            }
        }
    }

    @Override
    public void onError(VolleyError error) {
        hideProgressBar();
        //Log.e("JoinReqResponse error", error.toString());
        showSnackbar(btnSignUp, error.getMessage());
    }

    private void doActionOnSignupButton() {
        CUtils.hideMyKeyboard(currentActivity);
        if (validateData()) {
            CUtils.googleAnalyticSendWitPlayServie(currentActivity, CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                    CGlobalVariables.GOOGLE_ANALYTIC_ACTION_ASTROSAGE_VARTA_JOIN_REQ, null);
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
                showSnackbar(nameETV, message);
                return false;
            } else if (nameETV.getText().toString().trim().length() > 70) {
                requestFocus(nameETV);
                showSnackbar(nameETV, getResources().getString(R.string.email_two_v));
                return false;
            } else if (!CUtils.isValidEmail(nameETV.getText().toString().trim())) {
                requestFocus(nameETV);
                showSnackbar(nameETV, getResources().getString(R.string.email_three_v));
                return false;
            }
        }
        if (nameETV == edtMobileNo) {
            if (nameETV.getText().toString().trim().length() < 1) {
                requestFocus(nameETV);
                showSnackbar(nameETV, message);
                return false;
            } else if (nameETV.getText().toString().trim().length() < 9) {
                requestFocus(nameETV);
                showSnackbar(nameETV, getResources().getString(R.string.phone_one_v_astro_service));
                return false;
            }
        } else if (nameETV.getText().toString().trim().isEmpty()) {
            requestFocus(nameETV);
            showSnackbar(nameETV, message);
            return false;
        }
        if (!CUtils.isConnectedWithInternet(currentActivity)) {
            showSnackbar(btnSignUp, getResources().getString(R.string.no_internet));
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
            showSnackbar(btnSignUp, getResources().getString(R.string.no_internet));
        }
    }

    public Map<String, String> getJoinRequestParams() {

        int genderPosition = spinnerGender.getSelectedItemPosition();
        String genderStr;
        if (genderPosition == 0) {
            genderStr = "male";
        } else {
            genderStr = "female";
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
        params.put("countrycode", countryCode);
        params.put("mobileno", mobileNumber);
        params.put("lang", sectedLanguages);
        params.put("experience", edtExperience.getText().toString());
        params.put("city", edtCity.getText().toString());
        params.put("country", edtCountry.getText().toString());
        params.put("expert", sectedExperts);
        params.put("description", edtShortBio.getText().toString());
        params.put(CGlobalVariables.PACKAGE_NAME, BuildConfig.APPLICATION_ID);
        params.put(REG_SOURCE, "AstroSage Kundli");
        params.put("astrosageid", CUtils.getUserName(this));
        params.put("gender", genderStr);

        //Log.e("JoinReqResponse  params", params.toString());
        return params;
    }

    public void openLanguageSelectDialog() {
        try {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            FragmentManager fm = getSupportFragmentManager();
            Fragment prev = fm.findFragmentByTag("ChooseVartaLanguageFragmentDailog");
            if (prev != null) {
                ft.remove(prev);
            }
            //ft.addToBackStack(null);
            ChooseVartaLanguageFragmentDailog fragmentDailog = new ChooseVartaLanguageFragmentDailog();
            Bundle bundle = new Bundle();
            bundle.putString(CGlobalVariables.KEY_LANGUAGES, sectedLanguages);
            fragmentDailog.setArguments(bundle);
            fragmentDailog.show(fm, "ChooseVartaLanguageFragmentDailog");
            ft.commit();
        } catch (Exception e) {
            //
        }
    }

    public void setSelectedLanguage(String sectedLanguages, String sectedLanguagesToDisplay) {
        this.sectedLanguages = sectedLanguages;
        edtLang.setText(sectedLanguagesToDisplay);
    }

    private void openVerifyOtpDialog() {
        try {
            Bundle bundle = new Bundle();
            bundle.putString(CGlobalVariables.MOBILE, edtMobileNo.getText().toString());
            bundle.putString(CGlobalVariables.COUNTRY_CODE, countryCode);
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            otpDailogFrag = new VerifyOtpDailogFrag();
            otpDailogFrag.setArguments(bundle);
            ft.add(otpDailogFrag, null);
            ft.commitAllowingStateLoss();
        } catch (Exception e) {
            //
        }
    }

    public void submitOtp(String otp) {
        if (CUtils.isConnectedWithInternet(currentActivity)) {
            showProgressBar();
            CUtils.verifyOtpRequest(VartaReqJoinActivity.this, mobileNumber, otp, countryCode, OTP_VERIFICATION);
        } else {
            showSnackbar(btnSignUp, getResources().getString(R.string.no_internet));
        }
    }

    public void resendOtp() {
        if (CUtils.isConnectedWithInternet(currentActivity)) {
            showProgressBar();
            CUtils.resendOtpRequest(VartaReqJoinActivity.this, mobileNumber, countryCode, OTP_RESEND);
        } else {
            showSnackbar(btnSignUp, getResources().getString(R.string.no_internet));
        }
    }

    private void openConfirmationDialog() {
        try {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            JoinConfirmationDailogFrag pfdf = new JoinConfirmationDailogFrag();
            ft.add(pfdf, null);
            ft.commitAllowingStateLoss();
        } catch (Exception e) {
            //
        }
    }

    private void showProgressBar() {
        pd = new CustomProgressDialog(this, regularTypeface);
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
        Intent intent = new Intent(this, DashBoardActivity.class);
        startActivity(intent);
    }

    public void openExpertSelectDialog() {
        try {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            FragmentManager fm = getSupportFragmentManager();
            Fragment prev = fm.findFragmentByTag("ChooseExpertFragmentDailog");
            if (prev != null) {
                ft.remove(prev);
            }
            //ft.addToBackStack(null);
            ChooseExpertFragmentDailog fragmentDailog = new ChooseExpertFragmentDailog();
            Bundle bundle = new Bundle();
            bundle.putString(CGlobalVariables.KEY_EXPERTS, sectedExperts);
            fragmentDailog.setArguments(bundle);
            fragmentDailog.show(fm, "ChooseExpertFragmentDailog");
            ft.commit();
        } catch (Exception e) {
            //
        }
    }

    public void setSelectedExperts(String sectedExperts, String sectedExpertssToDisplay) {
        this.sectedExperts = sectedExperts;
        edtExpert.setText(sectedExpertssToDisplay);
    }

    private void checkCacheCountryListData() {
        String url = com.ojassoft.astrosage.varta.utils.CGlobalVariables.COUNTRY_LIST_URL;
        Cache cache = VolleySingleton.getInstance(VartaReqJoinActivity.this).getRequestQueue().getCache();
        Cache.Entry entry = cache.get("1-" + url);
        String saveData = "";
        // cache data
        try {
            if (entry != null) {
                saveData = new String(entry.data, StandardCharsets.UTF_8);
                //Log.e("SAVE DATA = ", saveData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(saveData)) {
            saveData = "{\"countries\":[{\"name\":\"India\",\"code\":\"91\"},{\"name\":\"USA\",\"code\":\"1\"},{\"name\":\"UK\",\"code\":\"44\"},{\"name\":\"Australia\",\"code\":\"61\"},{\"name\":\"Brazil\",\"code\":\"55\"},{\"name\":\"Canada\",\"code\":\"1\"},{\"name\":\"France\",\"code\":\"33\"},{\"name\":\"Germany\",\"code\":\"49\"},{\"name\":\"Israel\",\"code\":\"972\"},{\"name\":\"Japan\",\"code\":\"81\"},{\"name\":\"New Zealand\",\"code\":\"64\"},{\"name\":\"Singapore\",\"code\":\"65\"},{\"name\":\"Saudi Arabia\",\"code\":\"966\"},{\"name\":\"United Arab Emirates\",\"code\":\"971\"}]}";
        }

        parseCountryList(saveData);
        if (!com.ojassoft.astrosage.varta.utils.CUtils.isConnectedWithInternet(VartaReqJoinActivity.this)) {
            com.ojassoft.astrosage.varta.utils.CUtils.showSnackbar(btnSignUp, getResources().getString(R.string.no_internet), VartaReqJoinActivity.this);
        } else {
            CUtils.getCountryCodeList(VartaReqJoinActivity.this, GET_COUNTRY_CODE);
        }
    }

    private void parseCountryList(String saveData) {
        //String saveData = "{\"countries\":[{\"name\":\"India\",\"code\":\"91\"},{\"name\":\"USA\",\"code\":\"1\"},{\"name\":\"UK\",\"code\":\"44\"},{\"name\":\"Australia\",\"code\":\"61\"},{\"name\":\"Brazil\",\"code\":\"55\"},{\"name\":\"Canada\",\"code\":\"1\"},{\"name\":\"France\",\"code\":\"33\"},{\"name\":\"Germany\",\"code\":\"49\"},{\"name\":\"Israel\",\"code\":\"972\"},{\"name\":\"Japan\",\"code\":\"81\"},{\"name\":\"New Zealand\",\"code\":\"64\"},{\"name\":\"Singapore\",\"code\":\"65\"},{\"name\":\"Saudi Arabia\",\"code\":\"966\"},{\"name\":\"United Arab Emirates\",\"code\":\"971\"}]}";
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

    private void showDialog() {

        final EditText inputSearch;
        final Dialog dialog = new Dialog(VartaReqJoinActivity.this);
        dialog.setCanceledOnTouchOutside(true);
        View view = getLayoutInflater().inflate(R.layout.lay_varta_city_custompopup, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(view);
        inputSearch = view.findViewById(R.id.edtcountry);
        RecyclerView recyclerView = view.findViewById(R.id.listview);
        recyclerView.setLayoutManager(new LinearLayoutManager(VartaReqJoinActivity.this, LinearLayoutManager.VERTICAL, false));
        adapter = new CountryListAdapter(VartaReqJoinActivity.this, countryBeanList);
        recyclerView.setAdapter(adapter);
        adapter.setOnClickListener(new RecyclerClickListner() {
            @Override
            public void onItemClick(int position, View v) {
                if (position != -1) {
                    CountryBean countryBean = countryBeanList.get(position);
                    //Log.e("COUNTRY ", "" + countryBean.getCountryName());
                    if (countryBean != null) {
                        countryCode = countryBean.getCountryCode();
                        //Log.e("COUNTRY CODE ", "" + countryCode);
                        countryCodeText.setText(countryBean.getCountryName() + " (+" + countryCode + ")");
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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            //openConfirmationDialog();
        }
    }

}
