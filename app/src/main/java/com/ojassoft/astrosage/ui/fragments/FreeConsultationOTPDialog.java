package com.ojassoft.astrosage.ui.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Editable;
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
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Cache;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.libojassoft.android.misc.VolleyResponse;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.interfaces.DiaglogDismiss;
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

public class FreeConsultationOTPDialog extends DialogFragment implements VolleyResponse{

    private EditText etPhone, etName, etOtp;
    private Context context;
    private TextView ok_button, resend_otp_via_sms_btn;
    private RequestQueue queue;
    private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    Typeface typeFace;
    String mobileNo="";
    Dialog oTPdialog;
    Activity activity;

    boolean isShowOTPDialog=false;
    ArrayList<CountryBean> countryBeanList = null;
    CountryListAdapter adapter;
    CustomProgressDialog pd;
    ImageView cross_btn;
    String userName="",  userEmail="",  countryCode="",  userPhoneNo="",  astrologerProfileID="";
    //DiaglogDismiss diaglogDismiss;
    String fromWhichEvent="";

    public FreeConsultationOTPDialog(String userName, String userEmail, String countryCode,
                                     String userPhoneNo, String astrologerProfileID){
        this.userName = userName;
        this.userEmail = userEmail;
        this.countryCode = countryCode;
        this.userPhoneNo = userPhoneNo;
        this.astrologerProfileID = astrologerProfileID;
        //this.diaglogDismiss = diaglogDismiss;
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
        View view = inflater.inflate(R.layout.otp_layout_free_consultation, container);

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

        etOtp = (EditText)view.findViewById(R.id.etOtp);
        etPhone = (EditText)view.findViewById(R.id.etPhone);
        etName = (EditText)view.findViewById(R.id.etName);
        ok_button = (TextView) view.findViewById(R.id.ok_button);
        resend_otp_via_sms_btn = (TextView) view.findViewById(R.id.resend_otp_via_sms_btn);
        cross_btn = (ImageView) view.findViewById(R.id.cross_btn);

        etPhone.setTypeface(((BaseInputActivity) getActivity()).robotRegularTypeface);
        ok_button.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);

        etName.setText(userName);
        String phoneStr = "+ "+countryCode+ "-"+userPhoneNo;
        etPhone.setText(phoneStr);
    }

    private void setListeners(){
        cross_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //diaglogDismiss.onDialogDismiss();
                fromWhichEvent="";
                FreeConsultationOTPDialog.this.dismiss();
            }
        });

        ok_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String otpNo = etOtp.getText().toString();
                if(isValidData(otpNo)){
                    //DhruvLeadSucessfulDialog dialog = new DhruvLeadSucessfulDialog();
                    //dialog.show(getChildFragmentManager(), "Dialog");
                    if (!CUtils.isConnectedWithInternet(context)) {
                        showCustomisedToastMessage(getResources().getString(R.string.no_internet));
                    } else {
                        showProgressBar();
                        CUtils.vollyDhruvUserRegister(FreeConsultationOTPDialog.this,
                                CGlobalVariables.ASTROLOGER_OTP_VERIFICATION_URL,
                                getParamsForOtpVerification(), 0);
                    }
                }else{
                    showCustomisedToastMessage(getResources().getString(R.string.invalid_otp));
                }
            }
        });

        resend_otp_via_sms_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgressBar();
                CUtils.vollyDhruvUserRegister(FreeConsultationOTPDialog.this,
                        CGlobalVariables.ASTROLOGER_OTP_SEND_URL,
                        getParamsForResendOtp(),1);
            }
        });
    }



    Map<String, String> getParamsForResendOtp() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("countrycode", countryCode);
        params.put("mobileno", userPhoneNo);
        params.put("deviceid", CUtils.getMyAndroidId(context));
        params.put("key", CUtils.getApplicationSignatureHashCode(context));

        Log.e("DATAA params ", params.toString());
        return params;
    }


    Map<String, String> getParamsForOtpVerification() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("countrycode", countryCode);
        params.put("mobileno", userPhoneNo);
        params.put("otp", etOtp.getText().toString());
        params.put("key", CUtils.getApplicationSignatureHashCode(context));

        Log.e("DATAA params ", params.toString());
        return params;
    }


    Map<String, String> getParamsForLead() {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("countrycode", countryCode);
        params.put("mobileno", userPhoneNo);
        params.put("name", userName);
        params.put("deviceid", CUtils.getMyAndroidId(context));
        params.put("email", userEmail);
        params.put("astroProfileId",astrologerProfileID);
        params.put("key", CUtils.getApplicationSignatureHashCode(context));

        Log.e("DATAA params ", params.toString());
        return params;
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


    public Map<String, String> getParamsNew() {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.APP_KEY, com.ojassoft.astrosage.varta.utils.CUtils.getApplicationSignatureHashCode(context));
        //Log.e("LoadMore params ", headers.toString());
        return headers;
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

            if(method == 2){
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
                                /*DhruvLeadSucessfulDialog dialog = new DhruvLeadSucessfulDialog();
                                dialog.show(getChildFragmentManager(), "Dialog");*/

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
            }else if(method == 0){
                try {

                    JSONArray jsonArray = new JSONArray(response);
                    if(jsonArray != null && jsonArray.length()>0) {
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        if (jsonObject.has("result")) {
                            if (jsonObject.getString("result").equalsIgnoreCase("1")) {
                                if (!CUtils.isConnectedWithInternet(context)) {
                                    showCustomisedToastMessage(getResources().getString(R.string.no_internet));
                                } else {
                                    showProgressBar();
                                    CUtils.vollyDhruvUserRegister(FreeConsultationOTPDialog.this,
                                            CGlobalVariables.DHRUV_LEAD_GENERATION_URL,
                                            getParamsForLead(), 2);
                                }
                            }else if(jsonObject.getString("result").equalsIgnoreCase("2")){
                                showCustomisedToastMessage(getResources().getString(R.string.otp_is_wrong));
                            }else if(jsonObject.getString("result").equalsIgnoreCase("3")){
                                showCustomisedToastMessage(getResources().getString(R.string.sign_up_validation_authentication_failed));
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

                    JSONArray jsonArray = new JSONArray(response);
                    if(jsonArray != null && jsonArray.length()>0) {
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        if (jsonObject.has("Result")) {
                            if (jsonObject.getString("Result").equalsIgnoreCase("1")) {
                                showCustomisedToastMessage(getResources().getString(R.string.resend_otp_success_msg));
                            }else if(jsonObject.getString("Result").equalsIgnoreCase("2")){
                                showCustomisedToastMessage(getResources().getString(R.string.something_wrong_error));
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

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        if(fromWhichEvent.length()>0) {
            if (fromWhichEvent.equalsIgnoreCase(CGlobalVariables.SHIW_LEAD_SUCCESS_DIALOG)) {
                if (context instanceof DhruvAstrologerList) {
                    ((DhruvAstrologerList) context).showOtpDialog(fromWhichEvent);
                }else if(context instanceof AstrologerListingDetailsActivity){
                    ((AstrologerListingDetailsActivity) context).showOtpDialog(fromWhichEvent);
                }
            }
        }
    }
}
