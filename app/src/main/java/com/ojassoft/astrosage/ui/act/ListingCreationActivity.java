package com.ojassoft.astrosage.ui.act;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
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
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.libojassoft.android.misc.VolleyResponse;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.CityBean;
import com.ojassoft.astrosage.beans.ListingProfileBean;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.ui.fragments.ChooseExpertFragmentDailog;
import com.ojassoft.astrosage.ui.fragments.ChooseVartaLanguageFragmentDailog;
import com.ojassoft.astrosage.ui.fragments.FragPersonalDetailsOMF;
import com.ojassoft.astrosage.ui.fragments.SetChoosePhotoDialoge;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.varta.adapters.CountryListAdapter;
import com.ojassoft.astrosage.varta.interfacefile.RecyclerClickListner;
import com.ojassoft.astrosage.varta.model.CountryBean;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.io.IOException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static android.view.View.GONE;

public class ListingCreationActivity extends BaseInputActivity implements View.OnClickListener, VolleyResponse {
    TextView tvTitle;
    private CustomProgressDialog pd;
    private TabLayout tabs_input_kundli;
    public Toolbar toolBar_InputKundli;
    private TextView agreeTextView;
    EditText firstNameEt;
    //EditText lastNameEt;
    EditText countryCodeEt;
    EditText mobileNoEt;
    EditText mailIdEt;
    EditText langEt;
    Button signUp;
    CheckBox agreeCheckBox;
    EditText experienceEt;
    AutoCompleteTextView cityEt;
    EditText countryEt;
    EditText shortBioEt;
    EditText expertEt;
    TextView headingTV;
    LinearLayout profileVisibility;
    private String sectedLanguages = "";
    private String sectedExperts = "";
    LinearLayout linearLayout;
    private String fName = "", lName = "", emailId = "", mobileNumber = "";

    private final int LISTING_CREATE = 1;
    private final int GET_PROFILE_TO_EDIT = 3;
    private final int GET_CITY = 2;
    private final int RESEND_OTP = 4;
    private final int VERIFY_OTP = 5;
    private final int GET_COUNTRY_CODE = 6;
    ArrayList<CityBean> cityBeanArrayList = new ArrayList<>();
    String selectedCityId;
    private ListingProfileBean mListingProfileBean;
    private String profileIdToBeEdited;
    private String profileImageUrl;
    private Dialog oTPdialog;
    ArrayList<CountryBean> countryBeanList = null;
    CountryListAdapter adapter;
    String countryCode="91";
    private RadioGroup radioGroup;
    private boolean isProfileVisible;
    RadioButton activeRadioButton, inActiveRadioButton;

    public ListingCreationActivity() {
        super(R.string.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listing_creation_act_layout);
        toolBar_InputKundli = findViewById(R.id.tool_barAppModule);
        tabs_input_kundli = findViewById(R.id.tabs);
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(getString(R.string.directory_listing));
        linearLayout = findViewById(R.id.container_layout);
        agreeTextView = findViewById(R.id.agree_text);
        headingTV = findViewById(R.id.heading_tv);
        firstNameEt = findViewById(R.id.first_name_et);
        //lastNameEt = findViewById(R.id.last_name_et);
        countryCodeEt = findViewById(R.id.code_et);
        mobileNoEt = findViewById(R.id.phone_et);
        mailIdEt = findViewById(R.id.email_et);
        expertEt = findViewById(R.id.system_known_et);
        cityEt = findViewById(R.id.city_et);
        countryEt = findViewById(R.id.country_et);
        langEt = findViewById(R.id.select_languages_et);
        agreeCheckBox = findViewById(R.id.agreeCheckBox);
        experienceEt = findViewById(R.id.experience_et);
        shortBioEt = findViewById(R.id.short_bio_et);
        radioGroup= findViewById(R.id.radioGroup);
        profileVisibility = findViewById(R.id.profile_visiblity);
        profileVisibility.setVisibility(GONE);

        signUp = findViewById(R.id.btnSignUp);
        enableToolBar();
        setSpannableString();
        setTypeface();
        setListener();
        cityEt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(ListingCreationActivity.this, "id==" + cityBeanArrayList.get(position).getCityId(), Toast.LENGTH_SHORT).show();
                selectedCityId = cityBeanArrayList.get(position).getCityId();
            }
        });
        cityEt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() == 3) {
                    getCityFromServer(editable.toString());
                }
            }
        });
        countryCodeEt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        if(getIntent().getBooleanExtra("iseditprofile",false)){
            getEditProfile();
        }
        checkCacheCountryListData();
        addListenerButton();
    }

    private void addListenerButton() {
        activeRadioButton = findViewById(R.id.radio_active);
        inActiveRadioButton = findViewById(R.id.radio_inactivate);
        activeRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedID = radioGroup.getCheckedRadioButtonId();
                activeRadioButton = findViewById(selectedID);
                isProfileVisible = true;
            }
        });

        inActiveRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedID = radioGroup.getCheckedRadioButtonId();
                inActiveRadioButton = findViewById(selectedID);
                isProfileVisible = false;
            }
        });
    }

    private void setListener() {
        langEt.setOnClickListener(this);
        expertEt.setOnClickListener(this);
        agreeTextView.setOnClickListener(this);
        agreeCheckBox.setOnClickListener(this);
        signUp.setOnClickListener(this);
    }

    private void setTypeface() {
        headingTV.setTypeface(mediumTypeface);
        firstNameEt.setTypeface(regularTypeface);
        //lastNameEt.setTypeface(regularTypeface);
        countryCodeEt.setTypeface(regularTypeface);
        mobileNoEt.setTypeface(regularTypeface);
        mailIdEt.setTypeface(regularTypeface);
        expertEt.setTypeface(regularTypeface);
        cityEt.setTypeface(regularTypeface);

        countryEt.setTypeface(regularTypeface);
        langEt.setTypeface(regularTypeface);
        agreeCheckBox.setTypeface(regularTypeface);
        experienceEt.setTypeface(regularTypeface);
        shortBioEt.setTypeface(regularTypeface);
        agreeTextView.setTypeface(regularTypeface);
        signUp.setTypeface(mediumTypeface);
    }

    private void enableToolBar() {
        setSupportActionBar(toolBar_InputKundli);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tabs_input_kundli.setVisibility(GONE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
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
        CUtils.openWebBrowser(ListingCreationActivity.this, Uri.parse(url));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.select_languages_et:
                openLanguageSelectDialog();
                break;
            case R.id.system_known_et:
                openExpertSelectDialog();
                break;
            case R.id.agree_text:
                openTermsUseUrl();
                break;
            case R.id.agreeCheckBox: {
                if (agreeCheckBox.isChecked()) {
                    signUp.setEnabled(true);
                    signUp.setAlpha(1);
                } else {
                    signUp.setEnabled(false);
                    signUp.setAlpha(0.6f);
                }
                break;
            }
            case R.id.btnSignUp:
                summitRequest();
                break;

        }
    }

    private void onChooseButtonClicked() {
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        SetChoosePhotoDialoge profilePictureDialoge = new SetChoosePhotoDialoge();
        profilePictureDialoge.show(fm, "setProfilePic");
        ft.commit();
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
        langEt.setText(sectedLanguagesToDisplay);
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
        expertEt.setText(sectedExpertssToDisplay);
    }

    private void summitRequest() {
        CUtils.hideMyKeyboard(ListingCreationActivity.this);
        if (validateData()) {
            CUtils.googleAnalyticSendWitPlayServie(ListingCreationActivity.this, CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                    CGlobalVariables.GOOGLE_ANALYTIC_ACTION_ASTROSAGE_VARTA_JOIN_REQ, null);
            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_ACTION_ASTROSAGE_VARTA_JOIN_REQ, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
            registerUser();
        }
    }

    private boolean validateData() {
        boolean flag = false;
        if (validateName(firstNameEt, getString(R.string.enter_first_name)) && validateName(mobileNoEt, getString(R.string.enter_mobile_number)) && validateName(mailIdEt, getString(R.string.email_one_v)) && validateName(experienceEt, getString(R.string.enter_experience))
                && validateName(cityEt, getString(R.string.enter_city)) && validateName(countryEt, getString(R.string.enter_country)) && validateName(expertEt, getString(R.string.select_system_known)) && validateName(langEt, getString(R.string.msg_choose_lang_title)) && validateName(shortBioEt, getString(R.string.enter_bio)))
            flag = true;

        return flag;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private boolean validateName(EditText nameETV, String message) {
        if (nameETV == mailIdEt) {
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
        if (nameETV == mobileNoEt) {
            if (nameETV.getText().toString().trim().length() < 1) {
                requestFocus(nameETV);
                showSnackbar(nameETV, message);
                return false;
            } else if (nameETV.getText().toString().trim().length() < 10) {
                requestFocus(nameETV);
                showSnackbar(nameETV, getResources().getString(R.string.phone_one_v_astro_service));
                return false;
            }
        } else if (nameETV.getText().toString().trim().isEmpty()) {
            requestFocus(nameETV);
            showSnackbar(nameETV, message);
            return false;
        }
        if (!CUtils.isConnectedWithInternet(ListingCreationActivity.this)) {
            showSnackbar(linearLayout, getResources().getString(R.string.no_internet));
            return false;
        }
        return true;
    }

    public void registerUser() {
        fName = firstNameEt.getText().toString();
        //lName = lastNameEt.getText().toString();
        mobileNumber = mobileNoEt.getText().toString();
        emailId = mailIdEt.getText().toString();

        if (CUtils.isConnectedWithInternet(ListingCreationActivity.this)) {
            showProgressBar();
            CUtils.sendRequestForListing(ListingCreationActivity.this, getJoinRequestParams(), LISTING_CREATE);
        } else {
            showSnackbar(linearLayout, getResources().getString(R.string.no_internet));
        }
    }

    private void showProgressBar() {
        try {
            pd = new CustomProgressDialog(this, regularTypeface);
            pd.setCanceledOnTouchOutside(false);
            pd.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void hideProgressBar() {
        try {
            if (pd != null && pd.isShowing())
                pd.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Map<String, String> getJoinRequestParams() {


        HashMap<String, String> params = new HashMap<String, String>();

        if(mListingProfileBean != null && !TextUtils.isEmpty(mListingProfileBean.getProfileId())){
            params.put("profileid", mListingProfileBean.getProfileId());
            params.put("disable", isProfileVisible ? "0":"1");
            profileIdToBeEdited = mListingProfileBean.getProfileId();
        }else {
            profileIdToBeEdited = "";
        }
        params.put("name", firstNameEt.getText().toString());
        //params.put("lname", lastNameEt.getText().toString());
        params.put("countrycode", countryCode);
        params.put("phone", mobileNoEt.getText().toString());
        params.put("email", mailIdEt.getText().toString());
        params.put("experience", experienceEt.getText().toString());
        params.put("address", countryEt.getText().toString());
        params.put("cityid", selectedCityId);
        params.put("lang", sectedLanguages);
        params.put("expert", sectedExperts);
        params.put("description", shortBioEt.getText().toString());
        params.put("userid", CUtils.getUserName(ListingCreationActivity.this));
        params.put("isapi", "1");
        params.put("key", CUtils.getApplicationSignatureHashCode(ListingCreationActivity.this));

        //Log.e("JoinReqResponse  params", params.toString());
        return params;
    }

    @Override
    public void onResponse(String response, int method) {
        Log.e("DirectoryListing resp", response);
        try {
            hideProgressBar();
            if (method == LISTING_CREATE) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String responsecode = jsonObject.getString("status");
                    if(responsecode.equalsIgnoreCase("1")) {
                        showSnackbar(mobileNoEt, jsonObject.optString("msg"));
                        if(TextUtils.isEmpty(profileIdToBeEdited)) { //listing creation
                            showOTPDialog(mobileNoEt.getText().toString());
                        }else {
                            Intent intent = new Intent(ListingCreationActivity.this, DirectoryListingProfileImageActivity.class);
                            intent.putExtra("profileImageUrl",profileImageUrl);
                            startActivity(intent);
                            finish();
                        }

                    }else{
                        showSnackbar(mobileNoEt, jsonObject.optString("msg"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else if (method == GET_CITY) {
                if (!TextUtils.isEmpty(response)) {
                    cityBeanArrayList.clear();
                    JSONArray jsonArray = new JSONArray(response);
                    JSONObject jsonObject;
                    CityBean cityBean;
                    for (int i = 0; i < jsonArray.length(); i++) {
                        jsonObject = jsonArray.getJSONObject(i);
                        cityBean = new CityBean();
                        cityBean.setCountry(jsonObject.getString("country"));
                        cityBean.setCity(jsonObject.getString("city"));
                        cityBean.setCityId(jsonObject.getString("cityId"));
                        cityBean.setState(jsonObject.getString("state"));
                        cityBeanArrayList.add(cityBean);
                    }


                }
                ArrayList<String> list = new ArrayList();
                for (int i = 0; i < cityBeanArrayList.size(); i++) {
                    list.add(cityBeanArrayList.get(i).getCity()+", "+ cityBeanArrayList.get(i).getState());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>
                        (this, R.layout.autocompletetextview_dropdown_latout, list);

                cityEt.setThreshold(1);
                cityEt.setAdapter(adapter);
                adapter.setNotifyOnChange(true);
                cityEt.showDropDown();

            } else if(method == GET_PROFILE_TO_EDIT){
                if(!TextUtils.isEmpty(response)) {
                    Gson gson = new Gson();
                    mListingProfileBean = gson.fromJson(response.toString(), ListingProfileBean.class);
                    setupUI(mListingProfileBean);
                }
            }else if(method == RESEND_OTP){
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.has("status")){
                        if(jsonObject.getString("status").equalsIgnoreCase("1")){
                            showCustomisedToastMessage(getResources().getString(R.string.resend_otp_success_msg));
                        }else{
                            showCustomisedToastMessage(jsonObject.getString("msg"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            else if(method == VERIFY_OTP){
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.has("status")){
                        if(jsonObject.getString("status").equalsIgnoreCase("1")){
                            CUtils.hideMyKeyboard(this);
                            showCustomisedToastMessage(getString(R.string.otp_verified));
                            oTPdialog.dismiss();
                            Intent intent = new Intent(ListingCreationActivity.this, DirectoryListingProfileImageActivity.class);
                            startActivity(intent);
                            finish();
                        }else{
                            showCustomisedToastMessage(jsonObject.getString("msg"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if (method == GET_COUNTRY_CODE) {
                parseCountryList(response);
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            }
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
    }

    @Override
    public void onError(VolleyError error) {
        hideProgressBar();
        showSnackbar(mobileNoEt, getResources().getString(R.string.something_wrong_error));
    }


    public void getCityFromServer(String str) {
        if (CUtils.isConnectedWithInternet(ListingCreationActivity.this)) {
            showProgressBar();
            CUtils.getCityFromServer(ListingCreationActivity.this, getCityRequestParams(str), GET_CITY);
        } else {
            showSnackbar(linearLayout, getResources().getString(R.string.no_internet));
        }
    }

    public Map<String, String> getCityRequestParams(String str) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("keyword", str);
        params.put("isapi", "1");
        params.put("key", CUtils.getApplicationSignatureHashCode(ListingCreationActivity.this));
        //Log.e("JoinReqResponse  params", params.toString());
        return params;
    }

    public void getEditProfile(){
        if (CUtils.isConnectedWithInternet(ListingCreationActivity.this)) {
            showProgressBar();
            CUtils.sendRequestForEditListingProfile(ListingCreationActivity.this, getEditProfileParams(), GET_PROFILE_TO_EDIT);
        } else {
            showSnackbar(linearLayout, getResources().getString(R.string.no_internet));
        }
    }

    public Map<String, String> getEditProfileParams() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("userid", CUtils.getUserName(ListingCreationActivity.this));
        params.put("isapi", "1");
        params.put("key", CUtils.getApplicationSignatureHashCode(ListingCreationActivity.this));
        //Log.e("JoinReqResponse  params", params.toString());
        return params;
    }

    private void setupUI(ListingProfileBean mListingProfileBean) {
        if(mListingProfileBean != null) {
            try {
                profileIdToBeEdited = mListingProfileBean.getProfileId();
                profileImageUrl = mListingProfileBean.getImageURl();
                firstNameEt.setText(mListingProfileBean.getName());
                countryCodeEt.setText("+"+mListingProfileBean.getCountryCode());
                countryCode = mListingProfileBean.getCountryCode();
                mobileNoEt.setText(mListingProfileBean.getPhone());
                mailIdEt.setText(mListingProfileBean.getEmail());
                experienceEt.setText(mListingProfileBean.getExperience());
                countryEt.setText(mListingProfileBean.getAddress());
                selectedCityId = mListingProfileBean.getCityId().toString();
                cityEt.setText(mListingProfileBean.getCity());
                sectedLanguages = mListingProfileBean.getLanguage();
                langEt.setText(sectedLanguages);
                sectedExperts = mListingProfileBean.getExpertise();
                expertEt.setText(sectedExperts);
                shortBioEt.setText(mListingProfileBean.getShortBio());

                profileVisibility.setVisibility(View.VISIBLE);
                if (mListingProfileBean.getDisable().equalsIgnoreCase("0")) {
                    activeRadioButton.performClick();
                } else {
                    inActiveRadioButton.performClick();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void showOTPDialog(final String mobileNo){
        oTPdialog = new Dialog(this);
        oTPdialog.setContentView(R.layout.otp_dialog_for_omf);
        final EditText otpEdt1, otpEdt2, otpEdt3, otpEdt4;
        TextView resendOtpViaSmsBtn, trying_txt2, edit_btn;
        otpEdt1 = (EditText) oTPdialog.findViewById(R.id.otp_edt1);
        otpEdt2 = (EditText) oTPdialog.findViewById(R.id.otp_edt2);
        otpEdt3 = (EditText) oTPdialog.findViewById(R.id.otp_edt3);
        otpEdt4 = (EditText) oTPdialog.findViewById(R.id.otp_edt4);

        otpEdt1.setTypeface(regularTypeface);
        otpEdt2.setTypeface(regularTypeface);
        otpEdt3.setTypeface(regularTypeface);
        otpEdt4.setTypeface(regularTypeface);

        resendOtpViaSmsBtn = (TextView)oTPdialog.findViewById(R.id.resend_otp_via_sms_btn);
        trying_txt2 = (TextView)oTPdialog.findViewById(R.id.trying_txt2);
        edit_btn = (TextView)oTPdialog.findViewById(R.id.edit_btn);
        edit_btn.setVisibility(GONE);
        Button btnback = (Button) oTPdialog.findViewById(R.id.btnback);
        Button btnconfirm = (Button) oTPdialog.findViewById(R.id.btnconfirm);

        String str = getResources().getString(R.string.trying_msg2);
        str = str.replace("#", mobileNo);

        trying_txt2.setText(str);

        cursorMoveOtpAutomatically(otpEdt1, otpEdt2, otpEdt3, otpEdt4);
        // if button is clicked, close the custom dialog
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oTPdialog.dismiss();
                //Toast.makeText(context,"Dismissed..!! btnback",Toast.LENGTH_SHORT).show();
            }
        });

        edit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oTPdialog.dismiss();
            }
        });

        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //dialog.dismiss();
                //FragPersonalDetailsOMF.this.dismiss();
                String otpNumberStr = otpEdt1.getText().toString().trim() + otpEdt2.getText().toString().trim() +
                        otpEdt3.getText().toString().trim() + otpEdt4.getText().toString().trim() ;
                if(isValidData(otpNumberStr)) {
                    if (CUtils.isConnectedWithInternet(ListingCreationActivity.this)) {
                        showProgressBar();
                        CUtils.vollyPostRequestPhoneVerification(ListingCreationActivity.this, CGlobalVariables.DHRUV_DIRECTOTY_LISTINGOTP_VERIFY_URL,
                                getParams(mobileNo, "0", true, otpNumberStr), VERIFY_OTP);
                    } else {
                        showSnackbar(linearLayout, getResources().getString(R.string.no_internet));
                    }
                }else {
                    showCustomisedToastMessage(getResources().getString(R.string.invalid_otp));
                }
                //Toast.makeText(context,"Dismissed..!! btnconfirm",Toast.LENGTH_SHORT).show();
            }
        });

        resendOtpViaSmsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CUtils.isConnectedWithInternet(ListingCreationActivity.this)) {
                    showProgressBar();
                    CUtils.vollyPostRequestPhoneVerification(ListingCreationActivity.this, CGlobalVariables.DHRUV_SEND_OTP_URL,
                            getParams(mobileNo,"1", false, ""),RESEND_OTP);
                } else {
                    showSnackbar(linearLayout, getResources().getString(R.string.no_internet));
                }
            }
        });
        oTPdialog.show();
    }

    Map<String, String> getParams(String mobileNumber, String isResend, boolean isFromVerify, String otp) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("countrycode", countryCode);
        params.put("phoneno", mobileNumber);
        if(isFromVerify){
            params.put("userid", CUtils.getUserName(this));
            params.put("otp", otp);
        }else {
            params.put("isresend", isResend);
        }
        params.put("isapi", "1");
        params.put("key", CUtils.getApplicationSignatureHashCode(this));

        Log.e("DATAA params ", params.toString());
        return params;
    }

    private void showCustomisedToastMessage(String msg) {
        MyCustomToast mct = new MyCustomToast(this, getLayoutInflater(),  this, regularTypeface);
        mct.show(msg);
    }

    private boolean isValidData(String otpNo) {
        boolean isValid = true;
        if (otpNo == null && otpNo.trim().length() == 0) {
            isValid = false;
        }
        if (otpNo.trim().length() != 4) {
            isValid = false;
        }
        return isValid;
    }

    private void cursorMoveOtpAutomatically(final EditText otpEdt1, final EditText otpEdt2, final EditText otpEdt3, final EditText otpEdt4) {

        otpEdt1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() == 1) {
                    otpEdt2.requestFocus();
                }
            }
        });
        otpEdt2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() == 1) {
                    otpEdt3.requestFocus();
                }
                if (editable.toString().isEmpty()) {
                    otpEdt1.requestFocus();
                }
            }
        });
        otpEdt3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() == 1) {
                    otpEdt4.requestFocus();
                }
                if (editable.toString().isEmpty()) {
                    otpEdt2.requestFocus();
                }
            }
        });
        otpEdt4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().isEmpty()) {
                    otpEdt3.requestFocus();
                }
            }
        });
    }

    private void showDialog() {
        String strFilter;
        final EditText inputSearch;
        final Dialog dialog = new Dialog(this);
        dialog.setCanceledOnTouchOutside(true);
        View view = getLayoutInflater().inflate(R.layout.lay_varta_city_custompopup, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(view);
        inputSearch = (EditText) view.findViewById(R.id.edtcountry);
        strFilter = inputSearch.getText().toString();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.listview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        adapter = new CountryListAdapter(this, countryBeanList);
        recyclerView.setAdapter(adapter);
        adapter.setOnClickListener(new RecyclerClickListner() {
            @Override
            public void onItemClick(int position, View v) {
                if (position != -1) {
                    CountryBean countryBean = countryBeanList.get(position);
                    Log.e("COUNTRY ", "" + countryBean.getCountryName());
                    if (countryBean != null) {
                        String countryCodeValue = countryBean.getCountryCode();
                        Log.e("COUNTRY CODE ", "" + countryCodeValue);
                        //edtCountryCode.setText(countryBean.getCountryName() + " (+" + countryCodeValue + ")");
                        countryCodeEt.setText("+" + countryCodeValue + "");
                        countryCode = countryCodeValue;
                        //com.ojassoft.astrosage.varta.utils.CUtils.setCountryCode(context, countryCode);
                        if (countryCodeEt.getText().toString().contains("+91")) {
                            mobileNoEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
                        } else {
                            mobileNoEt.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
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

    private void checkCacheCountryListData() {
        String url = com.ojassoft.astrosage.varta.utils.CGlobalVariables.COUNTRY_LIST_URL;
        Cache cache = VolleySingleton.getInstance(this).getRequestQueue().getCache();
        Cache.Entry entry = cache.get("1-" + url);
        String saveData = "";
        // cache data
        try {
            if (entry != null) {
                saveData = new String(entry.data, "UTF-8");
                Log.e("SAVE DATA = ", saveData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (TextUtils.isEmpty(saveData)) {
            saveData = "{\"countries\":[{\"name\":\"India\",\"code\":\"91\"},{\"name\":\"USA\",\"code\":\"1\"},{\"name\":\"UK\",\"code\":\"44\"},{\"name\":\"Australia\",\"code\":\"61\"},{\"name\":\"Brazil\",\"code\":\"55\"},{\"name\":\"Canada\",\"code\":\"1\"},{\"name\":\"France\",\"code\":\"33\"},{\"name\":\"Germany\",\"code\":\"49\"},{\"name\":\"Israel\",\"code\":\"972\"},{\"name\":\"Japan\",\"code\":\"81\"},{\"name\":\"New Zealand\",\"code\":\"64\"},{\"name\":\"Singapore\",\"code\":\"65\"},{\"name\":\"Saudi Arabia\",\"code\":\"966\"},{\"name\":\"United Arab Emirates\",\"code\":\"971\"}]}";
        }
        parseCountryList(saveData);
        if (!com.ojassoft.astrosage.varta.utils.CUtils.isConnectedWithInternet(this)) {
            showCustomisedToastMessage(this.getResources().getString(R.string.no_internet));
        } else {
            CUtils.vollyPostRequestPhoneVerification(ListingCreationActivity.this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.COUNTRY_LIST_URL,
                    getParamsNew(),GET_COUNTRY_CODE);
        }
    }

    public Map<String, String> getParamsNew() {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.APP_KEY, com.ojassoft.astrosage.varta.utils.CUtils.getApplicationSignatureHashCode(this));
        //Log.e("LoadMore params ", headers.toString());
        return headers;
    }



}
