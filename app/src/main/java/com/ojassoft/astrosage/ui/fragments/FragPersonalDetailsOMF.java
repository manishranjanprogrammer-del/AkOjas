package com.ojassoft.astrosage.ui.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Cache;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputLayout;
import com.libojassoft.android.misc.VolleyResponse;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.jinterface.IPersonalDetails;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.act.PurchasePlanHomeActivity;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.varta.adapters.CountryListAdapter;
import com.ojassoft.astrosage.varta.interfacefile.RecyclerClickListner;
import com.ojassoft.astrosage.varta.model.CountryBean;
import com.ojassoft.astrosage.varta.ui.activity.LoginSignUpActivity;
import com.ojassoft.astrosage.varta.volley_network.VolleyServiceHandler;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_AS_USER_ID;

/**
 * Created by ojas-20 on 17/5/18.
 */

public class FragPersonalDetailsOMF extends DialogFragment implements VolleyResponse {

    private EditText edtMailId,edtPhone;
    private Button btnCancel,btnOk;
    private Context context;
    private IPersonalDetails iPersonalDetails;
    private int planIndex = -1;
    private boolean isEmailIdVisible,isPhnNumbVisisble;
    private LinearLayout llEmail,llPhone;
    private TextInputLayout input_layout_email,input_layout_phone;
    private TextView textViewHeading, sendOtpBtn, resendOtpViaSmsBtn;
    private RequestQueue queue;
    private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    Typeface typeFace;
    String countryCode="91";
    String mobileNo="";
    Dialog oTPdialog;
    EditText edtCountryCode;
    ArrayList<CountryBean> countryBeanList = null;
    CountryListAdapter adapter;
    private CustomProgressDialog pd;
    String url = CGlobalVariables.DHRUV_SEND_OTP_URL;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        iPersonalDetails = (IPersonalDetails)context;
        planIndex = getArguments().getInt("planIndex");
        isPhnNumbVisisble =  getArguments().getBoolean("isPhnNumbVisisble");
        isEmailIdVisible =  getArguments().getBoolean("isEmailIdVisible");
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.lay_personal_details_omf, container);

        setCancelable(false);
        setLayRef(view);

        setListeners();

        return view;
    }

    /**
     * This method is used to set layout view
     * @param view
     */
    private void setLayRef(View view){
        LANGUAGE_CODE = ((AstrosageKundliApplication) context.getApplicationContext())
                .getLanguageCode();

        typeFace = CUtils.getRobotoFont(getApplicationContext(), LANGUAGE_CODE, CGlobalVariables.regular);

        edtMailId = (EditText)view.findViewById(R.id.edtMailId);
        edtPhone = (EditText)view.findViewById(R.id.edtPhone);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnOk = (Button)view.findViewById(R.id.btnOk);
        edtCountryCode = (EditText)view.findViewById(R.id.edtCountryCode);
        sendOtpBtn = (TextView)view.findViewById(R.id.send_otp_btn);
        resendOtpViaSmsBtn = (TextView)view.findViewById(R.id.resend_otp_via_sms_btn);
        //cursorMoveOtpAutomatically();
        //verifyPhoneBtn.setTypeface(((BaseInputActivity) getActivity()).robotRegularTypeface);
        edtMailId.setTypeface(((BaseInputActivity) getActivity()).robotRegularTypeface);
        edtPhone.setTypeface(((BaseInputActivity) getActivity()).robotRegularTypeface);
        btnOk.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);
        btnCancel.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);

        sendOtpBtn.setText(Html.fromHtml(context.getResources().getString(R.string.send_otp)));

        if (((AstrosageKundliApplication) getActivity().getApplication())
                .getLanguageCode() == CGlobalVariables.ENGLISH) {
            btnOk.setText(getResources().getString(R.string.send_otp).toUpperCase());
            btnCancel.setText(getResources().getString(R.string.cancel).toUpperCase());
        }

        //countryCode = "91";
        checkCacheCountryListData();
        llEmail = (LinearLayout)view.findViewById(R.id.llEmail);
        llPhone = (LinearLayout)view.findViewById(R.id.llPhone);

        input_layout_email = (TextInputLayout)view.findViewById(R.id.input_layout_email);
        input_layout_phone = (TextInputLayout)view.findViewById(R.id.input_layout_phone);
        textViewHeading = (TextView)view.findViewById(R.id.textViewHeading);
        textViewHeading.setTypeface(((BaseInputActivity) getActivity()).robotMediumTypeface);

        String phoneNum = CUtils.getStringData(context, CGlobalVariables.KEY_PREF_MOBILE_NO, "");
        String countryCodee = CUtils.getStringData(context, CGlobalVariables.KEY_PREF_COUNTRY_CODE, "");

        if(!TextUtils.isEmpty(phoneNum)){
            edtPhone.setText(phoneNum);
        }

        if(!TextUtils.isEmpty(countryCodee)){
            edtCountryCode.setText("+ " + countryCodee + "");
            countryCode = countryCodee;
        }

        if(isEmailIdVisible)
            llEmail.setVisibility(View.VISIBLE);
        else
            llEmail.setVisibility(View.GONE);

        if(isPhnNumbVisisble)
            llPhone.setVisibility(View.VISIBLE);
        else
            llPhone.setVisibility(View.GONE);

    }

    private void setListeners(){
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* String otpNumberStr = otpEdt1.getText().toString().trim() + otpEdt2.getText().toString().trim() +
                        otpEdt3.getText().toString().trim() + otpEdt4.getText().toString().trim();*/
                /*if(validateData()) {
                    iPersonalDetails.onYesClick(planIndex);
                    FragPersonalDetailsOMF.this.dismiss();
                }*/
                if(validateData()){
                    if (!CUtils.isConnectedWithInternet(context)) {
                        showCustomisedToastMessage(getResources().getString(R.string.no_internet));
                    } else {
                        mobileNo = edtPhone.getText().toString();
                        CUtils.hideMyKeyboard(getActivity());
                        showProgressBar();
                        CUtils.vollyPostRequestPhoneVerification(FragPersonalDetailsOMF.this, url,
                                getParams(mobileNo, "0", false, ""), 0);
                    }
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCancelDialog();
                //FragPersonalDetailsOMF.this.dismiss();
            }
        });

        edtCountryCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    Map<String, String> getParams(String mobileNumber, String isResend, boolean isFromVerify, String otp) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("countrycode", countryCode);
        params.put("phoneno", mobileNumber);
        if(isFromVerify){
            params.put("userid", CUtils.getUserName(context));
            params.put("otp", otp);
        }else {
            params.put("isresend", isResend);
        }
        params.put("key", CUtils.getApplicationSignatureHashCode(context));

        Log.e("DATAA params ", params.toString());
        return params;
    }
    private void checkCacheCountryListData() {
        String url = com.ojassoft.astrosage.varta.utils.CGlobalVariables.COUNTRY_LIST_URL;
        Cache cache = VolleySingleton.getInstance(context).getRequestQueue().getCache();
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
        if (!com.ojassoft.astrosage.varta.utils.CUtils.isConnectedWithInternet(context)) {
            showCustomisedToastMessage(context.getResources().getString(R.string.no_internet));
        } else {
            CUtils.vollyPostRequestPhoneVerification(FragPersonalDetailsOMF.this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.COUNTRY_LIST_URL,
                    getParamsNew(),3);
        }
    }

    public Map<String, String> getParamsNew() {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.APP_KEY, com.ojassoft.astrosage.varta.utils.CUtils.getApplicationSignatureHashCode(context));
        //Log.e("LoadMore params ", headers.toString());
        return headers;
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
        String strFilter;
        final EditText inputSearch;
        final Dialog dialog = new Dialog(context);
        dialog.setCanceledOnTouchOutside(true);
        View view = getLayoutInflater().inflate(R.layout.lay_varta_city_custompopup, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(view);
        inputSearch = (EditText) view.findViewById(R.id.edtcountry);
        strFilter = inputSearch.getText().toString();
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.listview);
        recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        adapter = new CountryListAdapter(context, countryBeanList);
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
                        edtCountryCode.setText("+ " + countryCodeValue + "");
                        countryCode = countryCodeValue;
                        //com.ojassoft.astrosage.varta.utils.CUtils.setCountryCode(context, countryCode);
                        if (edtCountryCode.getText().toString().contains("+91")) {
                            edtPhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
                        } else {
                            edtPhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
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

    private boolean validateData() {
        boolean flag = false;
        if(isEmailIdVisible && isPhnNumbVisisble) {
            if (validateName(edtMailId, input_layout_email, getString(R.string.email_one_v))
                    && validateName(edtPhone, input_layout_phone, getString(R.string.astro_shop_User_mob_no))) {
                    CUtils.saveStringData(context, CGlobalVariables.ASTROASKAQUESTIONUSERPHONENUMBER, edtPhone.getText().toString());
                    CUtils.saveStringData(context, CGlobalVariables.ASTROASKAQUESTIONUSEREMAIL, edtMailId.getText().toString());

                flag = true;
            }
        }else if(isEmailIdVisible){
            if (validateName(edtMailId, input_layout_email, getString(R.string.email_one_v))) {
                CUtils.saveStringData(context, CGlobalVariables.ASTROASKAQUESTIONUSEREMAIL, edtMailId.getText().toString());
                flag = true;
            }
        }else if(isPhnNumbVisisble){
            if (validateName(edtPhone, input_layout_phone, getString(R.string.astro_shop_User_mob_no))) {
                CUtils.saveStringData(context, CGlobalVariables.ASTROASKAQUESTIONUSERPHONENUMBER, edtPhone.getText().toString());
                flag = true;
            }
        }

        return flag;
    }

    private boolean validateName(EditText name, TextInputLayout inputLayout, String message) {
        boolean value = true;

        if (name == edtMailId) {

            if (edtMailId.getText().toString().trim().isEmpty()) {
                input_layout_email.setError(getResources().getString(R.string.email_one_v));
                inputLayout.setErrorEnabled(true);
                requestFocus(edtMailId);
                name.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                value = false;
                // etUseremail.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
            }
            if (!(CUtils.isValidEmail(edtMailId.getText().toString().trim()))) {
                input_layout_email.setError(getResources().getString(R.string.email_one_v_astro_service));
                inputLayout.setErrorEnabled(true);
                requestFocus(edtMailId);
                name.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                value = false;
                // etUseremail.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
            } else {
                input_layout_email.setErrorEnabled(false);
                input_layout_email.setError(null);
                edtMailId.getBackground().setColorFilter(getResources().getColor(R.color.ColorPrimary), PorterDuff.Mode.SRC_ATOP);
            }
        }

        if (name == edtPhone) {
            if (name.getText().toString().trim().isEmpty() || name.getText().toString().trim().length() < 9) {
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

        return value;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            ((Activity)context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onResponse(String response, int method) {

        Log.e("DATAA ", response);
        hideProgressBar();
        //response = "{\"status\":\"1\", \"msg\":\"Success\"}";
        if(response != null && response.length()>0){
            //{"status":"2", "msg":"WRONG OTP"}

            if (method == 3) {
                parseCountryList(response);
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            }else if(method == 2){
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.has("status")){
                        if(jsonObject.getString("status").equalsIgnoreCase("1")){
                            CUtils.saveFreeDhruvPurchase(context,  countryCode,
                                     mobileNo, true);

                            ((PlatinumPlanFrag) getParentFragment()).openSelectPlan();

                            oTPdialog.dismiss();
                            FragPersonalDetailsOMF.this.dismiss();
                        }else {
                            showCustomisedToastMessage(jsonObject.getString("msg"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if(method == 0){
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(jsonObject.has("status")){
                        if(jsonObject.getString("status").equalsIgnoreCase("1")){
                            showOTPDialog(edtPhone.getText().toString());
                        }else{
                            showCustomisedToastMessage(jsonObject.getString("msg"));
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if(method == 1){
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
        }else{
            showCustomisedToastMessage(getResources().getString(R.string.something_wrong_error));
        }

    }

    private void showCustomisedToastMessage(String msg) {
        try {
            MyCustomToast mct = new MyCustomToast(context, getLayoutInflater(), ((BaseInputActivity) getActivity()), typeFace);
            mct.show(msg);
        }catch (Exception e){
            //
        }
    }

    @Override
    public void onError(VolleyError error) {
        showCustomisedToastMessage(context.getString(R.string.something_wrong_error));
    }

    public void showCancelDialog(){
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.cancel_dialog);
        Button btnback = (Button) dialog.findViewById(R.id.btnback);
        Button btnconfirm = (Button) dialog.findViewById(R.id.btnconfirm);
        // if button is clicked, close the custom dialog
        btnback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                //Toast.makeText(context,"Dismissed..!! btnback",Toast.LENGTH_SHORT).show();
            }
        });

        btnconfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                FragPersonalDetailsOMF.this.dismiss();
                //Toast.makeText(context,"Dismissed..!! btnconfirm",Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }


    public void showOTPDialog(final String mobileNo){
        oTPdialog = new Dialog(context);
        oTPdialog.setContentView(R.layout.otp_dialog_for_omf);
        final EditText otpEdt1, otpEdt2, otpEdt3, otpEdt4;
        TextView resendOtpViaSmsBtn, trying_txt2, edit_btn;
        otpEdt1 = (EditText) oTPdialog.findViewById(R.id.otp_edt1);
        otpEdt2 = (EditText) oTPdialog.findViewById(R.id.otp_edt2);
        otpEdt3 = (EditText) oTPdialog.findViewById(R.id.otp_edt3);
        otpEdt4 = (EditText) oTPdialog.findViewById(R.id.otp_edt4);

        otpEdt1.setTypeface(((BaseInputActivity) getActivity()).robotRegularTypeface);
        otpEdt2.setTypeface(((BaseInputActivity) getActivity()).robotRegularTypeface);
        otpEdt3.setTypeface(((BaseInputActivity) getActivity()).robotRegularTypeface);
        otpEdt4.setTypeface(((BaseInputActivity) getActivity()).robotRegularTypeface);

        resendOtpViaSmsBtn = (TextView)oTPdialog.findViewById(R.id.resend_otp_via_sms_btn);
        trying_txt2 = (TextView)oTPdialog.findViewById(R.id.trying_txt2);
        edit_btn = (TextView)oTPdialog.findViewById(R.id.edit_btn);
        Button btnback = (Button) oTPdialog.findViewById(R.id.btnback);
        Button btnconfirm = (Button) oTPdialog.findViewById(R.id.btnconfirm);

        String str = context.getResources().getString(R.string.trying_msg2);
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
                    if (!CUtils.isConnectedWithInternet(context)) {
                        showCustomisedToastMessage(getResources().getString(R.string.no_internet));
                    } else {
                        showProgressBar();
                        CUtils.vollyPostRequestPhoneVerification(FragPersonalDetailsOMF.this, CGlobalVariables.DHRUV_OTP_VERIFY_URL,
                                getParams(mobileNo, "0", true, otpNumberStr), 2);
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
                showProgressBar();
                CUtils.vollyPostRequestPhoneVerification(FragPersonalDetailsOMF.this, url,
                        getParams(mobileNo,"1", false, ""),1);
            }
        });
        oTPdialog.show();
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

    private void showProgressBar() {
        try {
            pd = new CustomProgressDialog(context, typeFace);
            pd.setCanceledOnTouchOutside(false);
            pd.show();
        }catch (Exception e){

        }
    }

    private void hideProgressBar() {
        try {
            if (pd != null & pd.isShowing())
                pd.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
