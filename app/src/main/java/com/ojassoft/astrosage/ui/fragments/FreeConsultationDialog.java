package com.ojassoft.astrosage.ui.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Cache;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.google.android.material.textfield.TextInputLayout;
import com.libojassoft.android.misc.VolleyResponse;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.interfaces.DiaglogDismiss;
import com.ojassoft.astrosage.jinterface.IPersonalDetails;
import com.ojassoft.astrosage.ui.DhruvAstrologerList;
import com.ojassoft.astrosage.ui.act.AstrologerListingDetailsActivity;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.varta.adapters.CountryListAdapter;
import com.ojassoft.astrosage.varta.interfacefile.RecyclerClickListner;
import com.ojassoft.astrosage.varta.model.CountryBean;
import com.ojassoft.astrosage.varta.ui.activity.DashBoardActivity;
import com.ojassoft.astrosage.varta.utils.CustomProgressDialog;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by ojas-20 on 17/5/18.
 */

public class FreeConsultationDialog extends DialogFragment implements VolleyResponse  {

    private EditText etEmailId, etPhone, etName, edtCountryCode, etMsg;
    private Context context;
    private TextView ok_button;
    private RequestQueue queue;
    private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    Typeface typeFace;
    String countryCode="91";
    String mobileNo="";
    Dialog oTPdialog;
    Activity activity;

    String fromWhichEvent="";
    ArrayList<CountryBean> countryBeanList = null;
    CountryListAdapter adapter;

    ImageView cross_btn;
    CustomProgressDialog pd;

    String astrologerProfileID="";

    public FreeConsultationDialog(String astrologerProfileID){
        this.astrologerProfileID = astrologerProfileID;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        this.activity = activity;
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
        View view = inflater.inflate(R.layout.free_consultation_dialog_layout, container);
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

        etEmailId = (EditText)view.findViewById(R.id.etEmailId);
        etMsg = (EditText)view.findViewById(R.id.etMsg);
        etPhone = (EditText)view.findViewById(R.id.etPhone);
        edtCountryCode = (EditText)view.findViewById(R.id.edtCountryCode);
        etName = (EditText)view.findViewById(R.id.etName);
        ok_button = (TextView) view.findViewById(R.id.ok_button);
        cross_btn = (ImageView) view.findViewById(R.id.cross_btn);

        etEmailId.setTypeface(((BaseInputActivity) getActivity()).robotRegularTypeface);
        etPhone.setTypeface(((BaseInputActivity) getActivity()).robotRegularTypeface);
        ok_button.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);

        edtCountryCode.setText("+ "+countryCode);
        //countryCode = "91";
        checkCacheCountryListData();

    }

    private void setListeners(){
        cross_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fromWhichEvent = "";
                FreeConsultationDialog.this.dismiss();
            }
        });

        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateData()) {
                    if (!CUtils.isConnectedWithInternet(context)) {
                        showCustomisedToastMessage(getResources().getString(R.string.no_internet));
                    } else {
                        showProgressBar();
                        CUtils.vollyDhruvUserRegister(FreeConsultationDialog.this,
                                CGlobalVariables.DHRUV_GET_CONSULTATION_URL,
                                getParams(""), 0);
                    }
                }
            }
        });

        edtCountryCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
    }

    private void showProgressBar() {
        if (pd == null) {
            pd = new CustomProgressDialog(context);
        }
        pd.setCanceledOnTouchOutside(false);
        if (!pd.isShowing()) {
            pd.show();
        }
    }

    /**
     * hide Progress Bar
     */
    private void hideProgressBar() {
        try {
            if (pd != null & pd.isShowing())
                pd.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    Map<String, String> getParams(String value) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("countrycode", countryCode);
        params.put("mobileno", etPhone.getText().toString());
        params.put("name", etName.getText().toString());
        params.put("deviceid", CUtils.getMyAndroidId(context));
        params.put("source", "astrosage_kundli");
        params.put("key", CUtils.getApplicationSignatureHashCode(context));

        Log.e("DATAA params ", params.toString());
        return params;
    }


    Map<String, String> getParamsForLead() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("countrycode", countryCode);
        params.put("mobileno", etPhone.getText().toString());
        params.put("name", etName.getText().toString());
        params.put("deviceid", CUtils.getMyAndroidId(context));
        params.put("email", etEmailId.getText().toString());
        params.put("astroProfileId",astrologerProfileID);
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
            CUtils.vollyPostRequestPhoneVerification(FreeConsultationDialog.this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.COUNTRY_LIST_URL,
                    getParamsNew(),2);
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
                            etPhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
                        } else {
                            etPhone.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
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
        boolean flag = true;
        if(TextUtils.isEmpty(etName.getText().toString())) {
            showCustomisedToastMessage(context.getResources().getString(R.string.please_enter_name_v));
            flag = false;
        }else if(TextUtils.isEmpty(etEmailId.getText().toString()) ||
                !(CUtils.isValidEmail(etEmailId.getText().toString().trim()))){
            showCustomisedToastMessage(context.getResources().getString(R.string.email_one_v));
            flag = false;
        } else if(TextUtils.isEmpty(etPhone.getText().toString())){
            showCustomisedToastMessage(context.getResources().getString(R.string.astro_shop_User_mob_no));
            flag = false;
        }
        return flag;
    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            ((Activity)context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onResponse(String response, int method) {

        Log.e("DATAA ", response);

        //response = "{\"status\":\"1\", \"msg\":\"Success\"}";
        hideProgressBar();
        if(response != null && response.length()>0){
            //{"status":"2", "msg":"WRONG OTP"}

            if (method == 2) {
                parseCountryList(response);
                if (adapter != null) {
                    adapter.notifyDataSetChanged();
                }
            }else if(method == 0){
                try {
                    //[{"Result": "1","IsMobileVerified": "False","SendOTPStatus": "1"}]

                    //[{""Result"": ""0""}]" ' All fields required
                    //[{""Result"": ""3""}]" ' Authentication failed
                    //[{""Result"": ""4""}]" ' Enter valid data field
                    //[{""Result"": ""1"",""IsMobileVerified"": ""0"",""SendOTPStatus"": ""1""}]" 'success
                    //jsonResult = jsonResult&"[{""Result"": ""1"",""IsMobileVerified"": ""1"",""SendOTPStatus"": ""0""}]" 'there is no need to send sms because number is alredy verified
                    JSONArray jsonArray = new JSONArray(response);
                    if(jsonArray != null && jsonArray.length()>0){
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        if(jsonObject.has("Result")){
                            if(jsonObject.getString("Result").equalsIgnoreCase("1")){
                                if(jsonObject.has("IsMobileVerified")){
                                    if(jsonObject.getString("IsMobileVerified").equalsIgnoreCase("False")||
                                            jsonObject.getString("IsMobileVerified").equalsIgnoreCase("0")){

                                        fromWhichEvent = CGlobalVariables.SHIW_OTP_DIALOG;
                                        CUtils.saveStringData(context, CGlobalVariables.KEY_PREF_DHRUV_FREE_CONSULTATION_ASTROLOGERID, astrologerProfileID);
                                        CUtils.saveStringData(context, CGlobalVariables.KEY_PREF_DHRUV_FREE_CONSULTATION_USERNAME, etName.getText().toString());
                                        CUtils.saveStringData(context, CGlobalVariables.KEY_PREF_DHRUV_FREE_CONSULTATION_EMAILID, etEmailId.getText().toString());
                                        CUtils.saveStringData(context, CGlobalVariables.KEY_PREF_DHRUV_FREE_CONSULTATION_COUNTRY_CODE, countryCode);
                                        CUtils.saveStringData(context, CGlobalVariables.KEY_PREF_DHRUV_FREE_CONSULTATION_MOBILENO, etPhone.getText().toString());

                                        //((DhruvAstrologerList) context).showOtpDialog(fromWhichEvent);

                                        dismiss();
                                        /*FreeConsultationOTPDialog dialog = new FreeConsultationOTPDialog(etName.getText().toString(),
                                                etEmailId.getText().toString(), countryCode, etPhone.getText().toString(),
                                                astrologerProfileID, FreeConsultationDialog.this);
                                        dialog.show(getChildFragmentManager(), "Dialog");*/

                                    }else{
                                        if (!CUtils.isConnectedWithInternet(context)) {
                                            showCustomisedToastMessage(getResources().getString(R.string.no_internet));
                                        } else {
                                            showProgressBar();
                                            CUtils.vollyDhruvUserRegister(FreeConsultationDialog.this,
                                                    CGlobalVariables.DHRUV_LEAD_GENERATION_URL,
                                                    getParamsForLead(), 1);
                                        }
                                    }
                                }
                            }else if(jsonObject.getString("Result").equalsIgnoreCase("3")){
                                showCustomisedToastMessage(getResources().getString(R.string.sign_up_validation_authentication_failed));
                            }else if(jsonObject.getString("Result").equalsIgnoreCase("4")){
                                showCustomisedToastMessage(getResources().getString(R.string.enter_valid_data_fields));
                            }else{
                                showCustomisedToastMessage(getResources().getString(R.string.all_fields_required));
                            }
                        }
                    }else{
                        showCustomisedToastMessage(getResources().getString(R.string.something_wrong_error));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else if(method == 1){
                try {
                    //[{""Result"": ""0""}]" ' All fields required
                    //[{""Result"": ""3""}]" ' Authentication failed
                    //[{""Result"": ""4""}]" ' mobile number is not correct
                    //[{""Result"": ""5""}]" ' mobile is not registered
                    //[{""Result"": ""6""}]" 'Mobile no is not verified
                    //[{""Result"": ""7""}]" 'astrologer profile id is disable or not valid
                    //[{""result"":""1"",""SMSStatus"":""0""}]" 'mail send to admin ther is no need to send sms because this is not a dhruv listing astrologer
                    //[{""result"":""1"",""SMSStatus"":"""&strOTPResult&"""}]"  sms and mail send to verified astrologer and strOTPResult =1 means send sms successfull else case is failed to send sms
                    JSONArray jsonArray = new JSONArray(response);
                    if(jsonArray != null && jsonArray.length()>0) {
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        if (jsonObject.has("result")) {
                            if (jsonObject.getString("result").equalsIgnoreCase("1")) {

                                fromWhichEvent = CGlobalVariables.SHIW_LEAD_SUCCESS_DIALOG;
                                dismiss();
                                /*DhruvLeadSucessfulDialog dialog = new DhruvLeadSucessfulDialog(FreeConsultationDialog.this);
                                dialog.show(getChildFragmentManager(), "Dialog");*/
                                //FreeConsultationDialog.this.dismiss();

                            }else if(jsonObject.getString("result").equalsIgnoreCase("2")){
                                showCustomisedToastMessage(getResources().getString(R.string.otp_is_wrong));
                            }else if(jsonObject.getString("result").equalsIgnoreCase("3")){
                                showCustomisedToastMessage(getResources().getString(R.string.sign_up_validation_authentication_failed));
                            }else if(jsonObject.getString("result").equalsIgnoreCase("4")){
                                showCustomisedToastMessage(getResources().getString(R.string.mobile_no_is_incorrect));
                            }else if(jsonObject.getString("result").equalsIgnoreCase("5")){
                                showCustomisedToastMessage(getResources().getString(R.string.mobile_is_not_regitered));
                            }else if(jsonObject.getString("result").equalsIgnoreCase("6")){
                                showCustomisedToastMessage(getResources().getString(R.string.mobile_is_not_verified));
                            }else if(jsonObject.getString("result").equalsIgnoreCase("7")){
                                showCustomisedToastMessage(getResources().getString(R.string.astrologer_id_disable));
                            }else{
                                showCustomisedToastMessage(getResources().getString(R.string.all_fields_required));
                            }
                        }
                    }else{
                        showCustomisedToastMessage(getResources().getString(R.string.something_wrong_error));
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
        MyCustomToast mct = new MyCustomToast(context, getLayoutInflater(), ((BaseInputActivity) getActivity()), typeFace);
        mct.show(msg);
    }

    @Override
    public void onError(VolleyError error) {

    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Log.e("DATAA ", "ONDISMISS");
        if(fromWhichEvent.length()>0) {
            if (fromWhichEvent.equalsIgnoreCase(CGlobalVariables.SHIW_OTP_DIALOG) ||
                    fromWhichEvent.equalsIgnoreCase(CGlobalVariables.SHIW_LEAD_SUCCESS_DIALOG)) {
                if (context instanceof DhruvAstrologerList) {
                    ((DhruvAstrologerList) context).showOtpDialog(fromWhichEvent);
                }else if(context instanceof AstrologerListingDetailsActivity){
                    ((AstrologerListingDetailsActivity) context).showOtpDialog(fromWhichEvent);
                }
            }
        }
    }

}
