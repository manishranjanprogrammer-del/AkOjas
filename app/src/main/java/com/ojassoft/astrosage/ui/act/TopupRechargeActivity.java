package com.ojassoft.astrosage.ui.act;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.libojassoft.android.misc.VolleyResponse;
import com.libojassoft.android.misc.VolleySingleton;
import com.libojassoft.android.utils.LibCUtils;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.jinterface.IAskCallback;
import com.ojassoft.astrosage.jinterface.IChoosePayOption;
import com.ojassoft.astrosage.jinterface.IPaymentFailed;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.misc.GetCheckSum;
import com.ojassoft.astrosage.model.AstrologerServiceInfo;
import com.ojassoft.astrosage.model.ServicelistModal;
import com.ojassoft.astrosage.ui.fragments.ChooseServicePayOPtionDialog;
import com.ojassoft.astrosage.ui.fragments.PaymentFailedDailogFrag;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.UserEmailFetcher;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.paytm.pgsdk.TransactionManager;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.ojassoft.astrosage.utils.CGlobalVariables.PAYMENT_TYPE_SERVICE;

/**
 * Created By Abhishek Raj
 */
public class TopupRechargeActivity extends BaseInputActivity implements View.OnClickListener, IAskCallback, IChoosePayOption,
        VolleyResponse, PaymentResultListener, IPaymentFailed {

    private Activity currentActivity;
    private Toolbar tool_barAppModule;
    private TabLayout tabLayout;
    private TextView tvTitle;
    private CustomProgressDialog pd;
    private TextView titleTopupTV;
    private TextView descTopupTV;
    private TextView amountTV;
    private Button btnBuyNow;

    private String payStatus = "0";
    private String payId = "";
    private String order_Id = "";
    private RequestQueue queue;
    private AstrologerServiceInfo astrologerServiceInfo;
    private ServicelistModal servicelistModal;
    private String chat_Id = "";
    private String emailID;
    private String payMode = "Paytm";

    public TopupRechargeActivity() {
        super(R.string.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topup_recharge);
        initContext();
        initViews();
        initListener();
    }

    private void initViews() {
        LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication()).getLanguageCode();
        tool_barAppModule = findViewById(R.id.tool_barAppModule);
        tool_barAppModule.setNavigationIcon(R.drawable.ic_back_arrow);
        setSupportActionBar(tool_barAppModule);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvTitle = findViewById(R.id.tvTitle);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setVisibility(View.GONE);
        titleTopupTV = findViewById(R.id.titleTopupTV);
        descTopupTV = findViewById(R.id.descTopupTV);
        amountTV = findViewById(R.id.amountTV);
        btnBuyNow = findViewById(R.id.btnBuyNow);

        tvTitle.setText(getString(R.string.topup_rechage));
        tvTitle.setTypeface(regularTypeface);
        titleTopupTV.setTypeface(mediumTypeface);
        btnBuyNow.setTypeface(mediumTypeface);
        descTopupTV.setTypeface(regularTypeface);
        amountTV.setTypeface(mediumTypeface);

        emailID = UserEmailFetcher.getEmail(this);
        astrologerServiceInfo = new AstrologerServiceInfo();
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        getTopupDescription();
    }

    private void initContext() {
        currentActivity = TopupRechargeActivity.this;
    }

    private void initListener() {
        btnBuyNow.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBuyNow: {
                if (!CUtils.isConnectedWithInternet(currentActivity)) {
                    showSnackbar(btnBuyNow, getResources().getString(R.string.no_internet));
                } else {
                    if (servicelistModal != null) {
                        Fragment diog = getSupportFragmentManager().findFragmentByTag("Dialog");
                        if (diog == null) {
                            ChooseServicePayOPtionDialog dialog = new ChooseServicePayOPtionDialog();
                            dialog.show(getSupportFragmentManager(), "Dialog");
                        }
                    } else {
                        showSnackbar(btnBuyNow, getResources().getString(R.string.something_wrong_error));
                    }
                }
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

    private void getTopupDescription() {
        if (com.ojassoft.astrosage.utils.CUtils.isConnectedWithInternet(currentActivity)) {
            showProgressBar();
            CUtils.getTopupDetailsRequest(TopupRechargeActivity.this, getTopupDetailsRequestParams(), 1);
        } else {
            showSnackbar(btnBuyNow, getResources().getString(R.string.no_internet));
        }
    }

    public Map<String, String> getTopupDetailsRequestParams() {

        String key = CUtils.getApplicationSignatureHashCode(currentActivity);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put(CGlobalVariables.KEY_API, key);
        params.put("languagecode", LANGUAGE_CODE + "");

        //Log.e("JoinReqResponse  params", params.toString());
        return params;
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

    @Override
    public void getCallBack(String result, CUtils.callBack callback, String priceInDollor, String priceInRs) {
        hideProgressBar();
        if (callback == CUtils.callBack.GET_ORDER_ID) {

            if (result.equalsIgnoreCase(CGlobalVariables.RESULT_CODE_THREE)) {
                showSnackbar(btnBuyNow, getString(R.string.coupon_not_valid));
            } else {
                order_Id = result;
                VolleyLog.d("Dhruv Top Up Recharge order_Id " + order_Id);
                if (order_Id != null && !order_Id.isEmpty()) {
                    if (priceInRs != null && priceInRs.length() > 0) {
                        astrologerServiceInfo.setPriceRs(priceInRs);
                    }
                    if (priceInDollor != null && priceInDollor.length() > 0) {
                        astrologerServiceInfo.setPrice(priceInDollor);
                    }
                    if (astrologerServiceInfo.getPayMode().equalsIgnoreCase("RazorPay")) {
                        goToRazorPayFlow();
                    } else {
                        new GetCheckSum(currentActivity, regularTypeface).getCheckSum(getchecksumparams(), 0);
                    }
                } else {
                    showSnackbar(btnBuyNow, getResources().getString(R.string.order_fail));
                }
            }
        } else if (callback == CUtils.callBack.GET_CHECKSUM) {
            String checksum = result;
            if (!result.isEmpty()) {
                startPaytmPayment(order_Id, astrologerServiceInfo.getPriceRs(), checksum);
            } else {
                showSnackbar(btnBuyNow, getResources().getString(R.string.order_fail));
            }
        } else if (callback == CUtils.callBack.POST_STATUS) {
            String postStatus = result;
            if (postStatus != null && postStatus.equalsIgnoreCase("1")) {
                if (payStatus.equalsIgnoreCase("1")) {
                    CUtils.emailPDF(currentActivity, CGlobalVariables.PAYTM_EMAIL_PDF, mediumTypeface, queue, order_Id, payStatus, astrologerServiceInfo);
                    Intent tppsIntent = new Intent(currentActivity, DhruvPlanDetailsActivity.class);
                    startActivity(tppsIntent);
                    onPurchaseCompleted(servicelistModal, order_Id);
                } else {
                    openPaymentFailedDialog();
                    CUtils.googleAnalyticSendWitPlayServie(this,
                            CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                            CGlobalVariables.GOOGLE_ANALYTIC_TOPUP_PAYTM_PURCHASED_FAILED, null);
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_TOPUP_PAYTM_PURCHASED_FAILED, CGlobalVariables.FIREBASE_EVENT_PAYMENT_FAILED, "");
                }

            } else {
                openPaymentFailedDialog();
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_TOPUP_PAYTM_PURCHASED_FAILED, CGlobalVariables.FIREBASE_EVENT_PAYMENT_FAILED, "");
            }

        } else if (callback == CUtils.callBack.POST_RAZORPAYSTATUS) {
            String postStatus = result;
            if (postStatus != null && postStatus.equalsIgnoreCase("1")) {
                if (payStatus.equalsIgnoreCase("1")) {

                    CUtils.emailPDF(currentActivity, CGlobalVariables.RAZORPAY_EMAIL_PDF, mediumTypeface, queue, order_Id, payStatus, astrologerServiceInfo);
                    Intent tppsIntent = new Intent(currentActivity, DhruvPlanDetailsActivity.class);
                    startActivity(tppsIntent);
                    onPurchaseCompleted(servicelistModal, order_Id);
                } else {
                    openPaymentFailedDialog();
                    CUtils.googleAnalyticSendWitPlayServie(this,
                            CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                            CGlobalVariables.GOOGLE_ANALYTIC_TOPUP_RAZOR_PAYMENT_FAILED, null);
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_TOPUP_RAZOR_PAYMENT_FAILED, CGlobalVariables.FIREBASE_EVENT_PAYMENT_FAILED, "");
                }

            } else {
                openPaymentFailedDialog();
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_TOPUP_RAZOR_PAYMENT_FAILED, CGlobalVariables.FIREBASE_EVENT_PAYMENT_FAILED, "");
            }
        }
    }

    @Override
    public void getCallBackForChat(String[] result, CUtils.callBack callback, String priceInDollor, String priceInRs) {

    }

    @Override
    public void onSelectedOption(int opt, String mob) {
        if (opt == R.id.radioPaytm) {
            CUtils.googleAnalyticSendWitPlayServie(this,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                    CGlobalVariables.GOOGLE_ANALYTIC_TOPUP_PAYTM, null);
            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_TOPUP_PAYTM, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
            payMode = "Paytm";
            setDataModel();
            startPayment();
        } else if (opt == R.id.radioRazor) {
            CUtils.googleAnalyticSendWitPlayServie(this,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                    CGlobalVariables.GOOGLE_ANALYTIC_TOPUP_RAZORPAY, null);
            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_TOPUP_RAZORPAY, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
            payMode = "RazorPay";
            setDataModel();
            startPayment();
        }
    }

    private void setDataModel() {
        String appVerName = LibCUtils.getApplicationVersionToShow(currentActivity);
        String problemStr = "Dhruv Pdf Top Up (lang: " + CUtils.getLanguageKey(LANGUAGE_CODE) + ", " + "appversion: " + appVerName + ")";
        astrologerServiceInfo.setKey(CUtils.getApplicationSignatureHashCode(this));
        astrologerServiceInfo.setCcAvenueType(payMode);
        astrologerServiceInfo.setPayMode(payMode);
        astrologerServiceInfo.setProblem(problemStr);
        astrologerServiceInfo.setKnowDOB("0");
        astrologerServiceInfo.setMobileNo("");
        astrologerServiceInfo.setPriceRs(servicelistModal.getPriceInRS());
        astrologerServiceInfo.setPrice(servicelistModal.getPriceInDollor());
        astrologerServiceInfo.setServiceId(servicelistModal.getServiceId());
        astrologerServiceInfo.setProfileId("");
        astrologerServiceInfo.setBillingCountry("");
        if (TextUtils.isEmpty(emailID)) {
            astrologerServiceInfo.setEmailID(CUtils.getUserName(this));
        } else {
            astrologerServiceInfo.setEmailID(emailID);
        }
    }

    private void startPayment() {
        showProgressBar();
        CUtils.getOrderID(currentActivity, regularTypeface, queue, astrologerServiceInfo);
    }

    @Override
    public void onResponse(String response, int method) {
        Log.e("JoinReqResponse", "method = " + method + response);
        hideProgressBar();
        try {
            JSONArray respObjArr = new JSONArray(response);
            parseJsonDataAndUpdateUi(respObjArr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(VolleyError error) {
        hideProgressBar();
        if (error != null) {
            showSnackbar(btnBuyNow, error.toString());
        }
    }

    private void parseJsonDataAndUpdateUi(JSONArray respObjArr) {
        try {
            JSONObject respObj = respObjArr.getJSONObject(0);
            servicelistModal = new Gson().fromJson(respObj.toString(), new TypeToken<ServicelistModal>() {
            }.getType());
            if (servicelistModal != null) {
                tvTitle.setText(servicelistModal.getTitle());
                titleTopupTV.setText(servicelistModal.getTitle());
                descTopupTV.setText(servicelistModal.getDetailDesc());
                String amtDesc = getResources().getString(R.string.price_of_pdf) + " " + getResources().getString(R.string.astroshop_dollar_sign) + servicelistModal.getPriceInDollor()
                        + "/ " + getResources().getString(R.string.astroshop_rupees_sign) + servicelistModal.getPriceInRS();
                amountTV.setText(amtDesc);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void goToRazorPayFlow() {
        final Checkout co = new Checkout();
        co.setFullScreenDisable(true);
        try {
            JSONObject options = new JSONObject();
            options.put("name", "AstroSage");
            options.put("description", servicelistModal.getTitle());
            options.put("currency", "INR");
            Double amount = Double.parseDouble(astrologerServiceInfo.getPriceRs());
            //Need to send amount to razor pay in paise
            Double paiseAmount = amount * 100;
            options.put("amount", paiseAmount);
            options.put("color", "#ff6f00");

            JSONObject preFill = new JSONObject();
            preFill.put("email", emailID);
            preFill.put("contact", "");
            options.put("prefill", preFill);


            JSONObject notes = new JSONObject();
            notes.put("orderId", order_Id);
            notes.put("chatId", chat_Id);
            notes.put("orderType", PAYMENT_TYPE_SERVICE);
            notes.put("appVersion", BuildConfig.VERSION_NAME);
            notes.put("appName", BuildConfig.APPLICATION_ID);
            notes.put("name", astrologerServiceInfo.getRegName());
            notes.put("firebaseinstanceid", com.ojassoft.astrosage.varta.utils.CUtils.getFirebaseAnalyticsAppInstanceId(this));
            notes.put("facebookinstanceid", com.ojassoft.astrosage.varta.utils.CUtils.getFacebookAnalyticsAppInstanceId(this));
            options.put("notes", notes);

            co.open(this, options);
        } catch (Exception e) {
            showSnackbar(btnBuyNow, "Error in payment: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        //Log.e("postRazorPayDetail suc", razorpayPaymentID);
        try {
            payStatus = "1";
            payId = razorpayPaymentID;
            double dPrice = 0.0;
            String price = "";
            try {
                if (astrologerServiceInfo != null) {
                    if (astrologerServiceInfo.getPriceRs() != null && astrologerServiceInfo.getPriceRs().length() > 0) {
                        price = astrologerServiceInfo.getPriceRs();
                        dPrice = Double.valueOf(price);
                    }
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            //Log.e("postRazorPayDetail dPrice", dPrice+"");
            CUtils.googleAnalyticSendWitPlayServieForPurchased(this,
                    CGlobalVariables.GOOGLE_ANALYTIC_ASTROSHOP_SERVICE,
                    CGlobalVariables.GOOGLE_ANALYTIC_SERVICE_RAZOR_PAYMENT_SUCCESS, null, dPrice, "");

            payStatus = "1";
            payId = razorpayPaymentID;
            CUtils.postRazorPayDetail(currentActivity, mediumTypeface, queue, order_Id, payStatus, astrologerServiceInfo, razorpayPaymentID, "");
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
            CUtils.postRazorPayDetail(currentActivity, mediumTypeface, queue, order_Id, payStatus, astrologerServiceInfo, "", status);
        } catch (Exception e) {
            //Log.e("Service pay", "Exception in onPaymentError", e);
        }
    }

    public void onPurchaseCompleted(ServicelistModal itemdetails, String order_id) {
        showSnackbar(btnBuyNow, getResources().getString(R.string.topup_purchase_suc));
        CUtils.trackEcommerceProduct(currentActivity, itemdetails.getServiceId(), itemdetails.getTitle(), itemdetails.getPriceInRS(), order_id, "INR", "Dhruv Pdf Top-Up ", "In-App Store", "0", CGlobalVariables.TOPIC_SERVICES);
    }

    private void openPaymentFailedDialog() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        PaymentFailedDailogFrag pfdf = new PaymentFailedDailogFrag();
        ft.add(pfdf, null);
        ft.commitAllowingStateLoss();
    }

    @Override
    public void onRetryClick() {
        btnBuyNow.performClick();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
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
            CUtils.googleAnalyticSendWitPlayServieForPurchased(this,
                    CGlobalVariables.GOOGLE_ANALYTIC_ASTROSHOP_SERVICE,
                    CGlobalVariables.GOOGLE_ANALYTIC_SERVICE_PAYMENT_SUCCESS, null, dPrice, "");
        }
        CUtils.postDataToServer(currentActivity, mediumTypeface, queue, order_Id, "", payStatus, astrologerServiceInfo, CGlobalVariables.UPDATE_PAY_STATUS_ASKQUE);
    }
}
