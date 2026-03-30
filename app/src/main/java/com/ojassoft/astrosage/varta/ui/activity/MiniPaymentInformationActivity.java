package com.ojassoft.astrosage.varta.ui.activity;

import static com.ojassoft.astrosage.utils.CGlobalVariables.COUNTRY_CODE_IND;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_DOMESTIC_DOLLAR_CR;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_DOMESTIC_GST_RATE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_INTERNATIONAL_DOLLAR_CR;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_INTERNATIONAL_GST_RATE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.MAX_UPI_AMOUNT_VAL;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.facebook.appevents.AppEventsConstants;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.utils.UPIAppChecker;
import com.ojassoft.astrosage.varta.dialog.PaymentFailDialog;
import com.ojassoft.astrosage.varta.dialog.PaymentProcessDialog;
import com.ojassoft.astrosage.varta.dialog.PaymentSucessfulDialog;
import com.ojassoft.astrosage.varta.model.UPIResponse;
import com.ojassoft.astrosage.varta.model.WalletAmountBean;
import com.ojassoft.astrosage.varta.service.Loginservice;
import com.ojassoft.astrosage.varta.service.PreFetchDataservice;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.ChatUtils;
import com.ojassoft.astrosage.varta.utils.CustomProgressDialog;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.varta.utils.GetCheckSum;
import com.ojassoft.astrosage.varta.utils.NumberUtils;
import com.ojassoft.astrosage.varta.utils.UPIResponseParser;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class MiniPaymentInformationActivity extends BaseActivity implements PaymentResultWithDataListener {
    private String callingActivity;
    private WalletAmountBean walletAmountBean;
    private String astrlogerUrlText;
    private String astrologerPhoneNumber;
    private String chatChannelId;
    private WalletAmountBean.Services services;
    private double rechargeAmount, paymentAmount, actualraters;
    private CustomProgressDialog pd;
    private String currency = CGlobalVariables.CURRENCY_INDIA;
    private List<PaymentView> paymentOptions;
    private LinearLayout upiPaymentOptionsContainer;
    private String orderId = "", paymentModeStr;
    ConstraintLayout containerLayout;
    RequestQueue queue;
    boolean isPaymentNetworkFailed = false;
    PaymentSucessfulDialog paymentSucessfulDialog;
    PaymentProcessDialog paymentProcessDialog;
    boolean getOrderIdReq;
    String amountToPay = "0";
    String payStatus = "0";
    private int selectedPosition;
    private ConstraintLayout other_methods_row;
    private TextView tvTotalAmount, tv_gst_rate, tvCashback, tvPayWithUpi, tvOtherMethods;
    private Activity activity;
    private double gst, dollorRate;
    private double totalDollar;
    private String selectedPayMode = "";
    private String od = "", phonepeid = "", phonepeOrderId = "";
    private ActivityResultLauncher<Intent> upiLauncher;
    private boolean intentFlowVisibilityForVartaWallet;
    private String chat_Id = "";

    public static String replaceEmailChar(String emailId) {

        try {
            int xCharacterAhead = 1;
            emailId = encodeWithAsciiValue(emailId, xCharacterAhead);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return emailId;
    }

    static String encodeWithAsciiValue(String stringToEncode, int XAheadCharacter) {
        // changed string
        String newString = "";
        // iterate for every characters
        for (int iterator = 0; iterator < stringToEncode.length(); ++iterator) {
            // ASCII value
            int val = stringToEncode.charAt(iterator);
            // store the duplicate
            newString += (char) (val + XAheadCharacter);
        }
        // return the new string
        return newString;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setContentView(R.layout.activity_mini_payment_information);
        this.setFinishOnTouchOutside(false);
        Window window = getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.BOTTOM;
        //wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
        window.setAttributes(wlp);
        CUtils.addFacebookAndFirebaseEvent(AppEventsConstants.EVENT_NAME_ADDED_TO_CART,"recharge_popup","MiniPaymentInformationActivity");
        getDataFromBundle();
        // Initialize UI elements
        activity = this;
        containerLayout = findViewById(R.id.containerLayout);
        ImageView closeIcon = findViewById(R.id.closeIcon);
        tvTotalAmount = findViewById(R.id.tv_total_amount);
        tv_gst_rate = findViewById(R.id.tv_gst_rate);
        FontUtils.changeFont(activity, tvTotalAmount, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(activity, tv_gst_rate, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        tvCashback = findViewById(R.id.tv_cashback);
        tvPayWithUpi = findViewById(R.id.tv_pay_with_upi);
        tvOtherMethods = findViewById(R.id.tv_other_methods);
        FontUtils.changeFont(MiniPaymentInformationActivity.this, tvOtherMethods, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(MiniPaymentInformationActivity.this, tvPayWithUpi, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        other_methods_row = findViewById(R.id.other_methods_row);
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        other_methods_row.setOnClickListener(v -> openSelectedPaymentMethods());
        setData();
        setPhonePePaymentGetwayData();
        // set on activity result for upi launcher
        setOnActivityResultForUpiLauncher();

        closeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTICS_MINI_PAYMENT_INFO_OPEN+"_"+callingActivity, CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");
    }

    private void setData() {
        // handle for intent flow visibility for varta wallet
        intentFlowVisibilityForVartaWallet = CUtils.getBooleanData(activity, CGlobalVariables.INTENT_FLOW_VISIBILITY_FOR_VARTA_WALLET, false);
        // handle for max upi amount
        if (paymentAmount > MAX_UPI_AMOUNT_VAL) {
            intentFlowVisibilityForVartaWallet = false;
        }
        String gstRate = walletAmountBean.getGstrate();
        String dollorConverstionRate = walletAmountBean.getDollorConverstionRate();
        if (TextUtils.isEmpty(gstRate)) {// in case of gstRate is not available then get from shared preferences(config api)
            if (CUtils.getCountryCode(activity).equalsIgnoreCase(COUNTRY_CODE_IND)) {
                gstRate = CUtils.getStringData(activity, KEY_DOMESTIC_GST_RATE, "0");
            } else {
                gstRate = CUtils.getStringData(activity, KEY_INTERNATIONAL_GST_RATE, "0");
            }
        }
        if (TextUtils.isEmpty(dollorConverstionRate)) {// in case of dollorConverstionRate is not available then get from shared preferences(config api)
            if (CUtils.getCountryCode(activity).equalsIgnoreCase(COUNTRY_CODE_IND)) {
                dollorConverstionRate = CUtils.getStringData(activity, KEY_DOMESTIC_DOLLAR_CR, "0");
            } else {
                dollorConverstionRate = CUtils.getStringData(activity, KEY_INTERNATIONAL_DOLLAR_CR, "0");
            }
        }
        gst = NumberUtils.safeParseDouble(gstRate);
        dollorRate = NumberUtils.safeParseDouble(dollorConverstionRate);
        double gstValAmt = Math.round(((rechargeAmount * gst) / 100) * 100.0) / 100.0;
        if (services != null) {
            paymentAmount = NumberUtils.safeParseDouble(services.getPaymentamount());
            actualraters = NumberUtils.safeParseDouble(services.getActualraters());
        } else {
            DecimalFormat df = new DecimalFormat("#.00");
            String payAmount = df.format(rechargeAmount + gstValAmt);
            paymentAmount = NumberUtils.safeParseDouble(payAmount);
            actualraters = rechargeAmount;
        }
        totalDollar = paymentAmount / dollorRate;
        totalDollar = (int) (Math.round(totalDollar * 100)) / 100.0;// totalDollar;

        String dollorText = " ($" + totalDollar + ")";
        String tAText = activity.getResources().getString(R.string.total_amount_dialog).replace("####", getResources().getString(R.string.rs_sign) + paymentAmount);
        tvTotalAmount.setText(tAText);
        String gstText = activity.getResources().getString(R.string.inc_gst).replace("####", gst + "");
        tv_gst_rate.setText(gstText);
        WalletAmountBean.Services rechargeService = walletAmountBean.getServiceList().get(selectedPosition);
        String walletPriceText = activity.getResources().getString(R.string.you_will_get_in_your_wallet).replace("####", getString(R.string.astroshop_rupees_sign) + getRupeetoShow(rechargeService.getRaters(), rechargeService.getOfferAmount(), "0"));
        tvCashback.setText(walletPriceText);

    }

    private void getDataFromBundle() {
        if (getIntent() != null) {
            Bundle bundle = getIntent().getExtras();
            callingActivity = bundle.getString(CGlobalVariables.CALLING_ACTIVITY);
            selectedPosition = bundle.getInt(CGlobalVariables.SELECTED_POSITION, 0);
            walletAmountBean = (WalletAmountBean) bundle.getSerializable(CGlobalVariables.WALLET_INFO);
            astrlogerUrlText = bundle.getString(CGlobalVariables.ASTROLGER_URL_TEXT);
            astrologerPhoneNumber = bundle.getString(CGlobalVariables.ASTROLOGER_MOB_NUMBER);
            if (bundle.containsKey(CGlobalVariables.CHANNEL_ID)) {
                chatChannelId = bundle.getString(CGlobalVariables.CHANNEL_ID);
            }
            services = walletAmountBean.getServiceList().get(selectedPosition);
            rechargeAmount = NumberUtils.safeParseDouble(services.getRaters());

        }

    }

    private String getRupeetoShow(String x, String y, String couponprice) {
        //Log.e(("SAN PFA ", "getRupeetoShow() x, y, couponprice => "+x + "<=>" + y + "<=>" + couponprice );
        double result = 0.0;
        result = NumberUtils.safeParseDouble(x) + NumberUtils.safeParseDouble(y) + NumberUtils.safeParseDouble(couponprice);
        return CUtils.convertAmtIntoIndianFormat(String.valueOf(result));
    }

    private void setPhonePePaymentGetwayData() {
        // Find all the views using the IDs from the XML
        upiPaymentOptionsContainer = findViewById(R.id.upiPaymentOptionsContainer);
        LinearLayout phonepeOption = findViewById(R.id.phonepe_option);
        LinearLayout gpayOption = findViewById(R.id.gpay_option);
        LinearLayout paytmOption = findViewById(R.id.paytm_option);
        LinearLayout bhimOption = findViewById(R.id.paytm_option);
        LinearLayout credOption = findViewById(R.id.paytm_option);
        // View divider_line = findViewById(R.id.divider_line);
        ImageView phonepeIcon = findViewById(R.id.phonepe_icon);
        ImageView gpayIcon = findViewById(R.id.gpay_icon);
        ImageView paytmIcon = findViewById(R.id.paytm_icon);

        TextView phonepeText = findViewById(R.id.phonepe_text);
        TextView gpayText = findViewById(R.id.gpay_text);
        TextView paytmText = findViewById(R.id.paytm_text);
        UPIAppChecker.showAppIcons(this, phonepeOption, gpayOption, paytmOption,bhimOption,credOption);

        if (!intentFlowVisibilityForVartaWallet || (phonepeOption.getVisibility() == View.GONE && paytmOption.getVisibility() == View.GONE && gpayOption.getVisibility() == View.GONE)) {
            tvPayWithUpi.setVisibility(View.GONE);
            upiPaymentOptionsContainer.setVisibility(View.GONE);
            tvOtherMethods.setText(getResources().getString(R.string.pay_with_razorpay));
        } else {
            tvOtherMethods.setText(getResources().getString(R.string.other_payment_methods));
            upiPaymentOptionsContainer.setVisibility(View.VISIBLE);
        }
        // Group the views for easier management
        paymentOptions = Arrays.asList(new PaymentView(phonepeOption, phonepeIcon, phonepeText, CGlobalVariables.UPI_PAYMENTS_PHONE), new PaymentView(gpayOption, gpayIcon, gpayText, CGlobalVariables.UPI_PAYMENTS_GPAY), new PaymentView(paytmOption, paytmIcon, paytmText, CGlobalVariables.UPI_PAYMENTS_PAYTM));

        // Set click listeners for each option
        for (PaymentView option : paymentOptions) {
            option.layout.setOnClickListener(v -> selectPaymentOption(option, paymentOptions));
        }
    }

    private void selectPaymentOption(PaymentView selectedOption, List<PaymentView> allOptions) {
        for (PaymentView option : allOptions) {
            if (option.equals(selectedOption)) {
                // This is the selected item
                option.layout.setSelected(true);
                animateIcon(option.icon, R.animator.icon_scale_up);
                animateTextSize(option.text, 14f); // Animate to a larger size
                getApiCallForPhonePayIntentUrl(option.paymentMethodName);
                selectedPayMode = option.paymentMethodName;
                paymentModeStr = option.paymentMethodName;
                //Toast.makeText(this, option.paymentMethodName, Toast.LENGTH_SHORT).show();
            } else {
                // These are the unselected items
                option.layout.setSelected(false);
                animateIcon(option.icon, R.animator.icon_scale_down);
                animateTextSize(option.text, 12f); // Animate back to original size
            }
        }
    }

    private void unSelectPaymentOption(List<PaymentView> allOptions) {
        for (PaymentView option : allOptions) {
            option.layout.setSelected(false);
            animateIcon(option.icon, R.animator.icon_scale_down);
            animateTextSize(option.text, 12f); // Animate back to original size
        }
    }

    private void animateIcon(ImageView icon, int animationResId) {
        Animator animator = AnimatorInflater.loadAnimator(activity, animationResId);
        animator.setTarget(icon);
        animator.start();
    }

    private void animateTextSize(TextView textView, float toSize) {
        ValueAnimator animator = ValueAnimator.ofFloat(textView.getTextSize() / getResources().getDisplayMetrics().scaledDensity, toSize);
        animator.setDuration(200);
        animator.addUpdateListener(animation -> {
            float animatedValue = (float) animation.getAnimatedValue();
            textView.setTextSize(animatedValue);
        });
        animator.start();
    }

    /**
     * This method is used to get PhonePay/Gpay/Paytm intent url from the server.
     *
     * @param paymentMethodName - The type of payment method (eg. PhonePay, Gpay or Paytm) which is selected by the user.
     *                          This parameter is used to identify the type of payment method and to get the corresponding
     *                          intent url from the server.
     *                          <p>
     *                          This method sends a POST request to the server with the necessary parameters to get the intent url. If the request
     *                          is successful, it extracts the intent url from the server response and starts an intent to initiate the payment.
     *                          <p>
     *                          If the request fails, it shows a progress dialog indicating that the payment is being processed.
     * @see RetrofitClient
     * @see ApiList
     * @see ResponseBody
     * @see CustomProgressDialog
     * @see UPIAppChecker
     */
    private void getApiCallForPhonePayIntentUrl(String paymentMethodName) {
        CUtils.addFacebookAndFirebaseEvent(AppEventsConstants.EVENT_NAME_INITIATED_CHECKOUT,paymentModeStr,"MiniPaymentInformationActivity");
        if (pd == null) pd = new CustomProgressDialog(activity);
        pd.show();
        pd.setCancelable(false);

        ApiList api = RetrofitClient.getInstance().create(ApiList.class);
        Map<String, String> mapNew = getParamsForGenerateOrderId();
        switch (paymentMethodName) {
            case CGlobalVariables.UPI_PAYMENTS_PHONE:
                CUtils.fcmAnalyticsEvents(CGlobalVariables.MINI_PAYMENT_INFO_VIA_PHONEPE_UPI, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                mapNew.put(CGlobalVariables.TARGET_APP, UPIAppChecker.PACKAGE_PHONEPE);
                mapNew.put(CGlobalVariables.PAY_MODE, CGlobalVariables.PHONEPE);
                break;
            case CGlobalVariables.UPI_PAYMENTS_GPAY:
                CUtils.fcmAnalyticsEvents(CGlobalVariables.MINI_PAYMENT_INFO_VIA_GPAY_UPI, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                mapNew.put(CGlobalVariables.TARGET_APP, UPIAppChecker.PACKAGE_GOOGLE_PAY);
                mapNew.put(CGlobalVariables.PAY_MODE, CGlobalVariables.GPAY);
                break;
            case CGlobalVariables.UPI_PAYMENTS_PAYTM:
                CUtils.fcmAnalyticsEvents(CGlobalVariables.MINI_PAYMENT_INFO_VIA_PAYTM_UPI, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                mapNew.put(CGlobalVariables.TARGET_APP, UPIAppChecker.PACKAGE_PAYTM);
                mapNew.put(CGlobalVariables.PAY_MODE, CGlobalVariables.PAYTM);
                break;
        }
        Call<ResponseBody> call = api.phonePayOrderCreation(mapNew);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                hideProgressBar();
                try {
                    String myResponse = response.body().string();
                    JSONObject obj = new JSONObject(myResponse);
                    JSONObject phonepeResponse = obj.getJSONObject("phonepeResponse");
                    JSONObject merchantResponse = obj.getJSONObject("merchantResponse");
                    String intentUrl = phonepeResponse.getString("intentUrl");
                    orderId = merchantResponse.getString("OrderId");
                    //open upi payment app(Phone Pe, G Pay, Paytm) via intent url
                    openPaymentApp(intentUrl);
                } catch (Exception e) {
                    CUtils.showSnackbar(containerLayout, e.toString(), activity);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideProgressBar();
                CUtils.showSnackbar(containerLayout, t.toString(), activity);
            }
        });
    }

    public Map<String, String> getParamsForGenerateOrderId() {
        currency = CGlobalVariables.CURRENCY_INDIA;


        Calendar calendar = Calendar.getInstance();
        HashMap<String, String> headers = new HashMap<String, String>();
        try {
            headers.put("key", CUtils.getApplicationSignatureHashCode(activity));
            headers.put("regName", CUtils.getCountryCode(activity) + "-" + CUtils.getUserID(activity));
            String emailId = "";
            if (TextUtils.isEmpty(com.ojassoft.astrosage.varta.utils.CUtils.getUserID(activity))) {
                emailId = com.ojassoft.astrosage.utils.CUtils.getUserName(activity);
            } else {
                emailId = com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(activity) + com.ojassoft.astrosage.varta.utils.CUtils.getUserID(activity);
            }
            headers.put(CGlobalVariables.KEY_EMAIL_ID, replaceEmailChar(emailId));
            headers.put("gender", "male");
            headers.put("dateOfBirth", String.valueOf(calendar.get(Calendar.DATE)));
            headers.put("monthOfBirth", String.valueOf(calendar.get(Calendar.MONTH) + 1));
            headers.put("yearOfBirth", String.valueOf(calendar.get(Calendar.YEAR)));
            headers.put("hourOfBirth", String.valueOf(calendar.get(Calendar.HOUR)));
            headers.put("minOfBirth", String.valueOf(calendar.get(Calendar.MINUTE)));
            headers.put("country", "India");
            headers.put("state", "Uttar Pradesh");
            headers.put("nearCity", "Agra");
            headers.put("place", "Agra");

            if (selectedPosition == -1) { //in case of user input recharge amount
                headers.put("problem", "Variable Amount Recharge");
                headers.put("serviceId", CGlobalVariables.SERVICE_ID_VARIABLE_AMOUNT);
                headers.put("rechargeamount", String.valueOf(paymentAmount));
            } else {
                headers.put("problem", services.getServicename());
                headers.put("serviceId", services.getServiceid());
                headers.put("rechargeamount", "");
                headers.put("price", String.valueOf(totalDollar));
                headers.put("priceRs", services.getPaymentamount());
            }

            headers.put("currency", currency);
            headers.put("payMode", paymentModeStr);
            headers.put("mobileNo", CUtils.getUserID(activity));
            headers.put("profileId", "");
            headers.put("KPHN", "0");
            headers.put("TimezoneOfBirth", "5.5");

            headers.put(CGlobalVariables.KEY_AS_USER_ID, replaceEmailChar(CUtils.getUserID(activity)));
            headers.put("asplanid", "1");

            headers.put("LongDegOfBirth", "78");
            headers.put("LongMinOfBirth", "1");
            headers.put("LongEWOfBirth", "E");
            headers.put("LatDegOfBirth", "27");
            headers.put("LatMinOfBirth", "10");
            headers.put("LatNSOfBirth", "N");
            headers.put("device_id", CUtils.getMyAndroidId(activity));//"407f6ad3c4ff24d4"
            headers.put("couponcode", "");
            headers.put("ordersource", CGlobalVariables.APP_SOURCE);
            headers.put(CGlobalVariables.PROPERTY_APP_VERSION, BuildConfig.VERSION_NAME + "(" + BuildConfig.VERSION_CODE + ")");
            headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
            headers.put("orderfromdomain", "varta.astrosage.com");

        } catch (Exception e) {

        }
        String encodedFburl = "";
        try {
            String fbUrl = AstrosageKundliApplication.facebookDeepLinkUrl;
            if (!TextUtils.isEmpty(fbUrl)) {
                encodedFburl = URLEncoder.encode(fbUrl, "UTF-8");
            }
        } catch (Exception e) {
        }
        headers.put(CGlobalVariables.KEY_FB_AD_LINK, encodedFburl);

        String firstInstallTime = CUtils.getStringData(activity, com.ojassoft.astrosage.utils.CGlobalVariables.FIRST_INSTALL_TIME_KEY, "");
        headers.put(com.ojassoft.astrosage.utils.CGlobalVariables.FIRST_INSTALL_TIME_KEY, firstInstallTime);
        //Log.e("RequesthitPaym", headers.toString());
        return CUtils.setRequiredParams(headers);
    }

    private void hideProgressBar() {
        try {
            if (pd != null && pd.isShowing()) pd.dismiss();
        } catch (Exception e) {
        }

    }

    //open upi payment app(Phone Pe, G Pay, Paytm) via intent url
    private void openPaymentApp(String intentUrl) {
        Uri uri = Uri.parse(intentUrl);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        if (selectedPayMode.equalsIgnoreCase(CGlobalVariables.UPI_PAYMENTS_PAYTM)) {
            intent.setPackage(UPIAppChecker.PACKAGE_PAYTM); // Force open in Paytm app
        } else if (selectedPayMode.equalsIgnoreCase(CGlobalVariables.UPI_PAYMENTS_GPAY)) {
            intent.setPackage(UPIAppChecker.PACKAGE_GOOGLE_PAY); // Force open in Google Pay app
        } else if (selectedPayMode.equalsIgnoreCase(CGlobalVariables.UPI_PAYMENTS_PHONE)) {
            intent.setPackage(UPIAppChecker.PACKAGE_PHONEPE); // Force open in PhonePe app
        } else if (selectedPayMode.equalsIgnoreCase(CGlobalVariables.UPI_PAYMENTS_BHIM)) {
            intent.setPackage(UPIAppChecker.PACKAGE_BHIM); // Force open in PhonePe app
        }
        upiLauncher.launch(intent);
    }

    private void setOnActivityResultForUpiLauncher() {
        //Notes - when user cancel the payment then
        //Status=SUBMITTED, TxnId=null, ResponseCode=02, TxnRef=OM2509250812412655389553 (Phone pe)
        //Status=FAILURE, TxnId=null, ResponseCode=null, TxnRef=null(G Pay) and (Paytm)
        //Notes - when Payment is success
        //Status=SUCCESS, TxnId=AXLe989334e471949a28a34feb10440883a, ResponseCode=00, TxnRef=OM2509250815499918508350(Phone Pe)
        //Status=SUCCESS, TxnId=AXIe040afe90df94424ae2109e840978c81, ResponseCode=0, TxnRef=OM2509250817333599356167(G Pay)
        //Status=SUCCESS, TxnId=PTM93342cb2475b43cab8214d8b1e477db8, ResponseCode=0, TxnRef=OM2509250818498691165143(Paytm)
        upiLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
            try {
                Intent data = result.getData();
                if (data != null) {
                    //Toast.makeText(activity, data + "", Toast.LENGTH_SHORT).show();
                    //Log.d("TestLogs", "data==>" + data);
                    //call order status api
                    getOrderStatusApi();
                    UPIResponse upiResp = UPIResponseParser.parse(result.getData());
                    // CGlobalVariables.PAYMENT_INFO_ACTIVITY_LOGS = upiResp.toString();
                    if (CGlobalVariables.SUCCESS.equalsIgnoreCase(upiResp.status)) {
                        setEcommercePurchaseEvent();
                        //  Payment success
                    } else if ("SUBMITTED".equals(upiResp.status)) {
                        // Payment pending
                    } else {
                        // Payment failed / cancelled
                    }
                } else {
                    com.paytm.pgsdk.Log.d("UPI", "UPI App closed without result");
                }
            } catch (Exception e) {
                //
            }

        });
    }

    /**
     * This method is used to get the order status of the UPI transaction.
     * It makes a network call to the server and retrieves the order status.
     * If the order status is "SUCCESS", then it shows a success dialog.
     * If the order status is "FAILURE", then it shows a failure dialog.
     * If the order status is "PENDING", then it retries the network call after a delay of 10 seconds.
     */
    private void getOrderStatusApi() {
        if (pd == null) pd = new CustomProgressDialog(activity);
        pd.show();
        pd.setCancelable(false);
        ApiList api = RetrofitClient.getInstance3().create(ApiList.class);
        //Map<String, String> mapNew = getParamsForOrderStatus();
        Call<ResponseBody> call = api.getUpiOrdersatusApi(getParamsForOrderStatus());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                hideProgressBar();
                try {
                    String myResponse = response.body().string();
                    JSONObject obj = new JSONObject(myResponse);
                    if (obj.getString("state").equals("COMPLETED")) {
                        phonepeOrderId = obj.getString("phonepeOrderId");
                        phonepeid = obj.getString("phonepeid");
                        od = obj.getString("OrderId");
                        sendDataToServer("1", "", true);
                    } else {
                        sendDataToServer("0", "", false);
                    }
                } catch (Exception e) {
                    //
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                hideProgressBar();
                openPendingDialog();
            }
        });

    }

    private void openPendingDialog() {
        Dialog dialog = new Dialog(this);
        View view = getLayoutInflater().inflate(R.layout.dialog_recharge_under_process, null);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE); //before
        dialog.setContentView(view);
        // Make background transparent
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            //dialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND); // optional: remove dim
        }

        //dialog.setContentView(R.layout.dialog_recharge_under_process);
        TextView tv_amount = dialog.findViewById(R.id.tv_amount);
        Button btn_ok = dialog.findViewById(R.id.btn_ok);

        tv_amount.setText("₹" + paymentAmount);
        dialog.setCancelable(false); // prevent dismiss on outside touch
        btn_ok.setOnClickListener(v -> dialog.dismiss());
        dialog.show();

    }
    private Map<String, String> getParamsForOrderStatus() {
        Map<String, String> mapNew = new HashMap<>();

        mapNew.put("orderId", orderId);
        mapNew.put("currency", CGlobalVariables.CURRENCY_INDIA);
        mapNew.put("PriceRs", String.valueOf(paymentAmount));
        mapNew.put("Price", String.valueOf(totalDollar));
        mapNew.put("key", CUtils.getApplicationSignatureHashCode(activity));
        return CUtils.setRequiredParams(mapNew);
    }

    private void openSelectedPaymentMethods() {
        unSelectPaymentOption(paymentOptions);
//        String defaultOrderPaymentMethod = CUtils.getStringData(activity, com.ojassoft.astrosage.utils.CGlobalVariables.DEFAULT_ORDER_PAYMENT_METHOD, CGlobalVariables.RAZORPAY);
//        if (defaultOrderPaymentMethod.equals(CGlobalVariables.PAYTM)) {
//            paymentModeStr = CGlobalVariables.PAYTM;
//        } else {
//            paymentModeStr = CGlobalVariables.RAZORPAY;
//        }
        CUtils.fcmAnalyticsEvents(CGlobalVariables.MINI_PAYMENT_INFO_VIA_RAZORPAY, CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");

        paymentModeStr = CGlobalVariables.RAZORPAY;
        selectedPayMode = paymentModeStr;
        getOrderId();

    }

    public void getOrderId() {
        CUtils.addFacebookAndFirebaseEvent(AppEventsConstants.EVENT_NAME_INITIATED_CHECKOUT,paymentModeStr,"MiniPaymentInformationActivity");
        if (selectedPosition != -1) {//-1 means user input recharge amount
            if (services == null) { //in case of service is null then reget service
                services = CUtils.getRechargeService(selectedPosition);
                if (services == null) {
                    Toast.makeText(activity, getString(R.string.please_select_recharge_amount) + " (" + selectedPosition + ")", Toast.LENGTH_LONG).show();
                    finish();
                    return;
                }
            }
        }
        getOrderIdReq = false;
        if (!CUtils.isConnectedWithInternet(activity)) {
            CUtils.showSnackbar(containerLayout, getResources().getString(R.string.no_internet), activity);
        } else {

            if (pd == null) pd = new CustomProgressDialog(activity);
            pd.show();
            pd.setCancelable(false);

            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.getOrderId(getParamsForGenerateOrderId());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    hideProgressBar();
                    try {
                        String myResponse = response.body().string();
                        JSONObject obj = new JSONObject(myResponse);
                        //Log.e("TestPayment=> ", obj.toString());
                        if (obj.has("status")) {
                            if (obj.optString("status").equalsIgnoreCase("100")) {
                                getOrderIdReq = true;
                                LocalBroadcastManager.getInstance(activity).registerReceiver(mReceiverBackgroundLoginService, new IntentFilter(CGlobalVariables.SEND_BROADCAST_BACK_GR_LOGIN));
                                startBackgroundLoginService();
                            }
                        } else {
                            orderId = obj.getString("OrderId");
                            amountToPay = obj.getString("PriceRsToPay");
                            currency = CGlobalVariables.CURRENCY_INDIA;

                            if (paymentModeStr.equalsIgnoreCase(CGlobalVariables.PAYTM)) {
                                new GetCheckSum(activity).getCheckSum(getchecksumparams(orderId), 0);
                            } else {
                                if (!CUtils.getCountryCode(activity).equals(COUNTRY_CODE_IND)) {
                                    if (actualraters != 1.0) {
                                        amountToPay = obj.getString("PriceToPay");
                                        currency = CGlobalVariables.CURRENCY_USD;
                                    }
                                }
                                JSONObject razorpayResponseObj = obj.getJSONObject("RazorpayResponse");
                                String razorpayOrderId = razorpayResponseObj.getString("razorpayOrderId");
                                startPayment(orderId, razorpayOrderId);
                            }
                        }
                    } catch (Exception e) {
                        com.paytm.pgsdk.Log.e("TestPayment=> ", e.toString());
                        CUtils.showSnackbar(containerLayout, e.toString(), activity);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    com.paytm.pgsdk.Log.e("TestPayment=> f", t.toString());
                    hideProgressBar();
                    CUtils.showSnackbar(containerLayout, t.toString(), activity);
                }
            });

        }
    }

//    public void getCallBack(String result, CUtils.callBack callback, String priceInDollor, String priceInRs) {
//
//        if (callback == CUtils.callBack.GET_CHECKSUM) {
//            String checksum = result;
//            if (!result.isEmpty()) {
//                startPaytmPayment(orderId, amountToPay, checksum);
//            } else {
//                CUtils.showSnackbar(containerLayout, getResources().getString(R.string.order_fail), activity);
//            }
//        }
//    }

    private void startPayment(String order_Id, String razorpayOrderId) {
        //Toast.makeText(this,"startPayment() = "+order_Id, Toast.LENGTH_SHORT).show();
        final Checkout co = new Checkout();
        co.setFullScreenDisable(true);

        try {
            JSONObject options = new JSONObject();
            options.put("name", "AstroSage");
            if (services == null) {
                options.put("description", "Wallet Recharge");
            } else {
                options.put("description", services.getServicename());
            }

            //You can omit the image option to fetch the image from dashboard
            // options.put("image", "http://astrosage.com/images/logo.png");

            Double amount = NumberUtils.safeParseDouble(amountToPay);
            amount = amount * 100;
            options.put("currency", currency);

            options.put("amount", amount);
            options.put("color", "#ff6f00");
            options.put("order_id", razorpayOrderId);

            JSONObject preFill = new JSONObject();
            preFill.put("email", "");
            if (selectedPayMode.equals(CGlobalVariables.CREDIT_DEBIT_CARD_RAZORPAY)) {
                preFill.put("method", "card");
            }
            preFill.put("contact", CUtils.getUserID(activity));
            options.put("prefill", preFill);


            JSONObject notes = new JSONObject();
            //added by Ankit on 3-7-2019 for Razorpay Webhook
            notes.put("orderId", order_Id);
            notes.put("chatId", "");
            notes.put("orderType", "service");
            notes.put("appVersion", BuildConfig.VERSION_NAME);
            notes.put("appName", BuildConfig.APPLICATION_ID);
            notes.put("orderFromDomain", "varta.astrosage.com");
            notes.put("firebaseinstanceid", CUtils.getFirebaseAnalyticsAppInstanceId(activity));
            notes.put("facebookinstanceid", CUtils.getFacebookAnalyticsAppInstanceId(activity));
            notes.put("isfirstpurchase", CUtils.getFirstTimePurchaseValue(activity));
            options.put("notes", notes);


            co.open(activity, options);
            //Log.e(("LoadMore razorpay ", options.toString());
        } catch (Exception e) {
            CUtils.showSnackbar(containerLayout, "Error in payment: " + e.getMessage(), activity);
            e.printStackTrace();
        }
    }

    Map<String, String> getchecksumparams(String order_Id) {
        String email = CUtils.getUserID(activity);
        Map<String, String> params = new HashMap<>();

        params.put("key", CUtils.getApplicationSignatureHashCode(activity));
        params.put("MID", "Ojasso36077880907527");
        params.put("ORDER_ID", order_Id);
        params.put("WEBSITE", "OjassoWAP");
        params.put("CALLBACK_URL", CGlobalVariables.CALLBACK_URL + order_Id);
        params.put("TXN_AMOUNT", amountToPay);
        params.put("CUST_ID", email);

        chat_Id = chat_Id.equalsIgnoreCase("") ? "0" : chat_Id;
        String extraData = "chatId_" + chat_Id + "_type_" + CGlobalVariables.PAYMENT_TYPE_SERVICE + "_appVersion_" + BuildConfig.VERSION_NAME + "_appName_" + BuildConfig.APPLICATION_ID + "_firebaseinstanceid_" + CUtils.getFirebaseAnalyticsAppInstanceId(activity) + "_facebookinstanceid_" + CUtils.getFacebookAnalyticsAppInstanceId(activity) + "_isfirstpurchase_" + CUtils.getFirstTimePurchaseValue(activity);

        params.put("MERC_UNQ_REF", extraData);

        return params;
    }

    private void startBackgroundLoginService() {
        try {
            if (CUtils.getUserLoginStatus(activity)) {
                CUtils.fcmAnalyticsEvents("bg_login_from_payment_info", CGlobalVariables.VARTA_BACKGROUND_LOGIN, "");

                Intent intent = new Intent(activity, Loginservice.class);
                activity.startService(intent);
            }
        } catch (Exception e) {
        }
    }

    public void sendDataToServer(String status, String razorpayid, boolean isEcommerceEventAdded) {
        try {
            //Log.d("sendDataToServer", status + razorpayid + isEcommerceEventAdded);
            if (status.equals("0")) {
               // Log.d("sendDataToServer", "status.equals(\"0\")" + paymentModeStr);
                //CUtils.saveWalletRechargeData("");
                if (paymentModeStr.equalsIgnoreCase(CGlobalVariables.PAYTM)) {
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.MINI_PAYMENT_INFO_PAYMENT_FAILED_PAYTM, CGlobalVariables.FIREBASE_EVENT_PAYMENT_FAILED, "");

                    CUtils.addFacebookEventPaymentFailed( CGlobalVariables.MINI_PAYMENT_INFO_PAYMENT_FAILED_PAYTM, actualraters, CGlobalVariables.CURRENCY_INDIA, "");

                } else if (paymentModeStr.equalsIgnoreCase(CGlobalVariables.RAZORPAY)) {
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.MINI_PAYMENT_INFO_PAYMENT_FAILED_RAZORPAY, CGlobalVariables.FIREBASE_EVENT_PAYMENT_FAILED, "");

                    CUtils.addFacebookEventPaymentFailed( CGlobalVariables.MINI_PAYMENT_INFO_PAYMENT_FAILED_RAZORPAY, actualraters, CGlobalVariables.CURRENCY_INDIA, "");

                } else if (paymentModeStr.equalsIgnoreCase(CGlobalVariables.UPI_PAYMENTS_PHONE)) {
                    //Log.d("sendDataToServer", "UPI_PAYMENTS_PHONE failed");
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.MINI_PAYMENT_INFO_PAYMENT_FAILED_UPI_PHONEPE, CGlobalVariables.FIREBASE_EVENT_PAYMENT_FAILED, "");

                    CUtils.addFacebookEventPaymentFailed( CGlobalVariables.MINI_PAYMENT_INFO_PAYMENT_FAILED_UPI_PHONEPE, actualraters, CGlobalVariables.CURRENCY_INDIA, "");

                } else if (paymentModeStr.equalsIgnoreCase(CGlobalVariables.UPI_PAYMENTS_GPAY)) {
                    //Log.d("sendDataToServer", "UPI_PAYMENTS_GPAY failed");
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.MINI_PAYMENT_INFO_PAYMENT_FAILED_UPI_GPAY, CGlobalVariables.FIREBASE_EVENT_PAYMENT_FAILED, "");

                    CUtils.addFacebookEventPaymentFailed( CGlobalVariables.MINI_PAYMENT_INFO_PAYMENT_FAILED_UPI_GPAY, actualraters, CGlobalVariables.CURRENCY_INDIA, "");

                } else if (paymentModeStr.equalsIgnoreCase(CGlobalVariables.UPI_PAYMENTS_PAYTM)) {
                   // Log.d("sendDataToServer", "UPI_PAYMENTS_PAYTM failed");
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.MINI_PAYMENT_INFO_PAYMENT_FAILED_UPI_PAYTM, CGlobalVariables.FIREBASE_EVENT_PAYMENT_FAILED, "");

                    CUtils.addFacebookEventPaymentFailed( CGlobalVariables.MINI_PAYMENT_INFO_PAYMENT_FAILED_UPI_PAYTM, actualraters, CGlobalVariables.CURRENCY_INDIA, "");

                }
                udatePaymentStatusOnserveronFailed("0", orderId, String.valueOf(rechargeAmount), paymentModeStr);
                return;
            }
            //isEcommerceEventAdded -> this variable is used to check if the e-commerce purchase event is already added or not.
            //If the payment is successful and the e-commerce purchase event is already added, then don't add it again.
            //If the payment is failed and the e-commerce purchase event is already added, then don't add it again.
            else if (status.equals("1") && !isEcommerceEventAdded) {
                //Log.d("sendDataToServer","setEcommercePurchaseEvent");
                        setEcommercePurchaseEvent();
            }
            if (callingActivity == null) {
                callingActivity = "";
            }
            if (callingActivity.equals("TopupRechargeDialog")) {
                udatePaymentStatusOnserver(containerLayout, status, orderId, String.valueOf(rechargeAmount), queue, paymentModeStr, razorpayid);
            } else {
                if (callingActivity.equals("AstrologerDescriptionActivity")) {
                    openCallingActivity(status, AstrologerDescriptionActivity.class, razorpayid);
                } else if (callingActivity.equals("ChatWindowActivity")) {
                    openCallingActivity(status, ChatWindowActivity.class, razorpayid);
                }  else if (callingActivity.equals("DashBoardActivity")) {
                    openCallingActivity(status, DashBoardActivity.class, razorpayid);
                   // openCallingActivity(status, razorpayid, DashBoardActivity.class);
                } else if (callingActivity.equals("AIChatWindowActivity")) {
                    openCallingActivity(status, AIChatWindowActivity.class, razorpayid);
                } else if (callingActivity.equals("QuickRechargeBottomSheet")) {
                    openCallingActivity(status, razorpayid, ChatWindowActivity.class);
                } else if (callingActivity.equals("AIQuickRechargeBottomSheet")) {
                    openCallingActivity(status, razorpayid, AIChatWindowActivity.class);
                } else if (callingActivity.equals(CGlobalVariables.AIVOICECALLINGACTIVITY)) {
                    openCallingActivity(status, AIVoiceCallingActivity.class, razorpayid);
                } else {
                    openCallingActivity(status, DashBoardActivity.class, razorpayid);
                }
                //Log.e(("LoadMore status ", "success 0" + status);
                finish();
            }
        } catch (Exception e) {
            //Log.e(("LoadMore status ", "exception success 0" + status);
            ////Log.e(("OnPaymentError", "Exception in onPaymentError", e);
        }
    }

    //to set the purchase event in firebase
    private void setEcommercePurchaseEvent() {
        try {
            //CUtils.saveWalletRechargeData("");
            com.ojassoft.astrosage.utils.CUtils.saveBooleanData(activity, com.ojassoft.astrosage.utils.CGlobalVariables.IS_PURCHASE, true);
            //Log.e("RazorPayPayment","sendDataToServer status="+status+" getActualraters="+services.getActualraters());
            if (actualraters > 0) {
                //Log.d("sendDataToServer","setEcommercePurchaseEvent paymentModeStr"+paymentModeStr);
                if (paymentModeStr.equalsIgnoreCase(CGlobalVariables.PAYTM)) {
                    CUtils.fcmAnalyticsEcommerceEvents(activity, CGlobalVariables.MINI_PAYMENT_INFO_PAYMENT_SUCCESS_PAYTM, FirebaseAnalytics.Event.PURCHASE, actualraters, CGlobalVariables.CURRENCY_INDIA, orderId,"");
                } else if (paymentModeStr.equalsIgnoreCase(CGlobalVariables.RAZORPAY)) {
                    CUtils.fcmAnalyticsEcommerceEvents(activity, CGlobalVariables.MINI_PAYMENT_INFO_PAYMENT_SUCCESS_RAZORPAY, FirebaseAnalytics.Event.PURCHASE, actualraters, CGlobalVariables.CURRENCY_INDIA, orderId,"");
                } else if (paymentModeStr.equalsIgnoreCase(CGlobalVariables.UPI_PAYMENTS_PHONE)) {
                    //Log.d("sendDataToServer","setEcommercePurchaseEvent success UPI_PAYMENTS_PHONE");
                    CUtils.fcmAnalyticsEcommerceEvents(activity, CGlobalVariables.MINI_PAYMENT_INFO_PAYMENT_SUCCESS_UPI_PHONEPE, FirebaseAnalytics.Event.PURCHASE, actualraters, CGlobalVariables.CURRENCY_INDIA, orderId,"");
                } else if (paymentModeStr.equalsIgnoreCase(CGlobalVariables.UPI_PAYMENTS_GPAY)) {
                    //Log.d("sendDataToServer","setEcommercePurchaseEvent success UPI_PAYMENTS_GPAY");
                    CUtils.fcmAnalyticsEcommerceEvents(activity, CGlobalVariables.MINI_PAYMENT_INFO_PAYMENT_SUCCESS_UPI_GPAY, FirebaseAnalytics.Event.PURCHASE, actualraters, CGlobalVariables.CURRENCY_INDIA, orderId,"");
                } else if (paymentModeStr.equalsIgnoreCase(CGlobalVariables.UPI_PAYMENTS_PAYTM)) {
                    //Log.d("sendDataToServer","setEcommercePurchaseEvent success PAYMENT_SUCCESS_UPI_PAYTM");
                    CUtils.fcmAnalyticsEcommerceEvents(activity, CGlobalVariables.MINI_PAYMENT_INFO_PAYMENT_SUCCESS_UPI_PAYTM, FirebaseAnalytics.Event.PURCHASE, actualraters, CGlobalVariables.CURRENCY_INDIA, orderId,"");
                }

                if (services != null) { //in case of service amount payment add purchase event service wise)
                    int serviceAmt = (int) actualraters;
                    if (paymentModeStr.equalsIgnoreCase(CGlobalVariables.PAYTM)) {
                        CUtils.ecommercePurchaseEventsServiceWise(this,CGlobalVariables.MINI_PAYMENT_INFO_PAYMENT_SUCCESS_PAYTM, FirebaseAnalytics.Event.PURCHASE + "_" + serviceAmt, actualraters, CGlobalVariables.CURRENCY_INDIA, orderId,"");
                    } else if (paymentModeStr.equalsIgnoreCase(CGlobalVariables.RAZORPAY)) {
                        CUtils.ecommercePurchaseEventsServiceWise(this,CGlobalVariables.MINI_PAYMENT_INFO_PAYMENT_SUCCESS_RAZORPAY, FirebaseAnalytics.Event.PURCHASE + "_" + serviceAmt, actualraters, CGlobalVariables.CURRENCY_INDIA, orderId,"");
                    } else if (paymentModeStr.equalsIgnoreCase(CGlobalVariables.UPI_PAYMENTS_PHONE)) {
                        //Log.d("sendDataToServer","setEcommercePurchaseEvent success 2 UPI_PAYMENTS_PHONE");
                        CUtils.ecommercePurchaseEventsServiceWise(this,CGlobalVariables.MINI_PAYMENT_INFO_PAYMENT_SUCCESS_UPI_PHONEPE, FirebaseAnalytics.Event.PURCHASE + "_" + serviceAmt, actualraters, CGlobalVariables.CURRENCY_INDIA, orderId,"");
                    } else if (paymentModeStr.equalsIgnoreCase(CGlobalVariables.UPI_PAYMENTS_GPAY)) {
                        //Log.d("sendDataToServer","setEcommercePurchaseEvent success 2 UPI_PAYMENTS_GPAY");
                        CUtils.ecommercePurchaseEventsServiceWise(this,CGlobalVariables.MINI_PAYMENT_INFO_PAYMENT_SUCCESS_UPI_GPAY, FirebaseAnalytics.Event.PURCHASE + "_" + serviceAmt, actualraters, CGlobalVariables.CURRENCY_INDIA, orderId,"");
                    } else if (paymentModeStr.equalsIgnoreCase(CGlobalVariables.UPI_PAYMENTS_PAYTM)) {
                       // Log.d("sendDataToServer","setEcommercePurchaseEvent success 2 PAYMENT_SUCCESS_UPI_PAYTM");
                        CUtils.ecommercePurchaseEventsServiceWise(this,CGlobalVariables.MINI_PAYMENT_INFO_PAYMENT_SUCCESS_UPI_PAYTM, FirebaseAnalytics.Event.PURCHASE + "_" + serviceAmt, actualraters, CGlobalVariables.CURRENCY_INDIA, orderId,"");
                    }

                }
            }
        } catch (Exception e) {
            // e.printStackTrace();
        }

    }
    private void openCallingActivity(String orderStatus, Class activity, String razorpayid) {
        Bundle bundle = new Bundle();
        Intent intent = new Intent(this.activity, activity);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        bundle.putBoolean(CGlobalVariables.IS_RECHARGED, true);
        bundle.putString(CGlobalVariables.ORDER_ID, orderId);
        bundle.putString(CGlobalVariables.ORDER_STATUS, orderStatus);
        bundle.putString(CGlobalVariables.RECHARGE_AMOUNT, String.valueOf(rechargeAmount));
        bundle.putString(CGlobalVariables.ASTROLOGER_MOB_NUMBER, astrologerPhoneNumber);
        bundle.putString(CGlobalVariables.ASTROLGER_URL_TEXT, astrlogerUrlText);
        bundle.putString(CGlobalVariables.PAYMENT_MODE, paymentModeStr);
        bundle.putString(CGlobalVariables.RAZORPAY_ID, razorpayid);
        intent.putExtras(bundle);
        startActivity(intent);
    }
    private void openCallingActivity(String orderStatus, String razorpayid, Class activity) {
        Bundle bundle = new Bundle();
        Intent intent = new Intent(this.activity, activity);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        bundle.putBoolean(CGlobalVariables.IS_RECHARGED, true);
        bundle.putString(CGlobalVariables.ORDER_ID, orderId);
        bundle.putString(CGlobalVariables.ORDER_STATUS, orderStatus);
        bundle.putString(CGlobalVariables.RECHARGE_AMOUNT, String.valueOf(rechargeAmount));
        bundle.putString(CGlobalVariables.ASTROLOGER_MOB_NUMBER, astrologerPhoneNumber);
        bundle.putString(CGlobalVariables.ASTROLGER_URL_TEXT, astrlogerUrlText);
        bundle.putString(CGlobalVariables.PAYMENT_MODE, paymentModeStr);
        bundle.putString(CGlobalVariables.RAZORPAY_ID, razorpayid);
        bundle.putString(CGlobalVariables.PHONEPE_ID, phonepeid);
        bundle.putString(CGlobalVariables.PHONEPE_ORDER_ID, phonepeOrderId);
        bundle.putString(CGlobalVariables.ORDER_ID_PHONEPE, od);
        intent.putExtras(bundle);
        this.activity.setResult(CGlobalVariables.REQUEST_FOR_QUICK_RECHARGE, intent);
    }private final BroadcastReceiver mReceiverBackgroundLoginService = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String status = intent.getStringExtra("status");
            if (status.equals(CGlobalVariables.SUCCESS)) {
                if (getOrderIdReq) {
                    getOrderIdReq = false;
                    getOrderId();
                }
            } else {
                CUtils.showSnackbar(containerLayout, getResources().getString(R.string.something_wrong_error), activity.getApplicationContext());
            }

            if (mReceiverBackgroundLoginService != null) {
                LocalBroadcastManager.getInstance(activity).unregisterReceiver(mReceiverBackgroundLoginService);
            }
        }
    };

//    private void startPaytmPayment(String oId, String amount, String tnxToken) {
//
//        String midString = CGlobalVariables.PAYTM_MID;
//        String callBackUrl = CGlobalVariables.CALLBACK_URL + oId;
//
//        PaytmOrder paytmOrder = new PaytmOrder(oId, midString, tnxToken, amount, callBackUrl);
//
//        TransactionManager transactionManager = new TransactionManager(paytmOrder, new PaytmPaymentTransactionCallback() {
//
//            @Override
//            public void onTransactionResponse(Bundle bundle) {
//                try {
//                    String status = bundle.getString("STATUS");
//                    processPaytmTransaction(status);
//                } catch (Exception e) {
//                    //
//                }
//            }
//
//            @Override
//            public void networkNotAvailable() {
//            }
//
//            @Override
//            public void onErrorProceed(String s) {
//            }
//
//            @Override
//            public void clientAuthenticationFailed(String s) {
//            }
//
//            @Override
//            public void someUIErrorOccurred(String s) {
//            }
//
//            @Override
//            public void onErrorLoadingWebPage(int i, String s, String s1) {
//            }
//
//            @Override
//            public void onBackPressedCancelTransaction() {
//            }
//
//            @Override
//            public void onTransactionCancel(String s, Bundle bundle) {
//            }
//
//        });
//        //transactionManager.setAppInvokeEnabled(false);
//        transactionManager.setShowPaymentUrl(CGlobalVariables.PAYTM_PAYMENT_URL);
//        transactionManager.startTransaction(activity, CGlobalVariables.REQUEST_CODE_PAYTM);
//    }

//    private void processPaytmTransaction(String status) {
//        if (status.equals(CGlobalVariables.TXN_SUCCESS)) {
//            payStatus = "1";
//        } else {
//            payStatus = "0";
//        }
//        sendDataToServer(payStatus, "", false);
//    }

    public void udatePaymentStatusOnserver(View containerLayout, String orderStatus, String orderID, String amount, RequestQueue queue, String paymentMode, String razorpayid) {
        //Log.d("PaymentStatus", "udatePaymentStatusOnserver()");

        if (!CUtils.isConnectedWithInternet(activity)) {
            CUtils.showSnackbar(containerLayout, getResources().getString(R.string.no_internet), activity);
        } else {
            if (pd == null) pd = new CustomProgressDialog(activity);
            pd.show();
            pd.setCancelable(false);

            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.walletRecharge(getParamsForWalletRecharge(orderStatus, orderID, razorpayid));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    hideProgressBar();
                    startPrefatchDataService();
                    //Log.e("TestFreeChat", "chatAstrologerDetailBeanAfterRecharge1="+AstrosageKundliApplication.chatAstrologerDetailBeanAfterRecharge);
                    if (AstrosageKundliApplication.chatAstrologerDetailBeanAfterRecharge != null) { //initiate chat automatically in case of insufficient balance dialog
                        AstrosageKundliApplication.selectedAstrologerDetailBean = AstrosageKundliApplication.chatAstrologerDetailBeanAfterRecharge;
                        ChatUtils.getInstance(activity).callStartChat(CUtils.isAiAstrologer(AstrosageKundliApplication.selectedAstrologerDetailBean));
                        AstrosageKundliApplication.chatAstrologerDetailBeanAfterRecharge = null;
                    } else {
                        try {
                            String myResponse = response.body().string();
                            handleWalletRechargeResponse(myResponse);
                        } catch (Exception e) {
                            //
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }
    }

    public void handleWalletRechargeResponse(String response) {
        com.paytm.pgsdk.Log.e("PaymentStatus", response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            String status = jsonObject.getString("status");
            if (status.equals("1") || status.equals("2")) {
                try {
                    //CUtils.clearWalletApiCache(mainQueue);
                    //getWalletPriceData();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (paymentSucessfulDialog != null && paymentSucessfulDialog.getDialog() != null && paymentSucessfulDialog.getDialog().isShowing() && !paymentSucessfulDialog.isRemoving()) {
                    //dialog is showing so do nothing
                } else {
                    //dialog is not showing
                    paymentSucessfulDialog = new PaymentSucessfulDialog(String.valueOf(rechargeAmount));
                    paymentSucessfulDialog.show(getSupportFragmentManager(), "PaymentSucessfulDialog");
                }

            } else {
                showPaymentProcessDialog();
            }
        } catch (Exception e) {
            //Log.e("PaymentStatus ", "exp="+e);
        }
    }

//    @Override
//    public void onPaymentSuccess(String razorpayPaymentID, PaymentData paymentData) {
//        if (razorpayPaymentID == null) {
//            razorpayPaymentID = "";
//        }
//        sendDataToServer("1", razorpayPaymentID, false);
//    }
//
//    @Override
//    public void onPaymentError(int i, String s, PaymentData paymentData) {
//        com.ojassoft.astrosage.utils.CUtils.saveBooleanData(activity, com.ojassoft.astrosage.utils.CGlobalVariables.IS_SUBSCRIPTION_ERROR, true);
//        sendDataToServer("0", "", false);
//    }

    private void showPaymentProcessDialog() {
        try {
            if (paymentProcessDialog != null && paymentProcessDialog.getDialog() != null && paymentProcessDialog.getDialog().isShowing() && !paymentProcessDialog.isRemoving()) {
                //dialog is showing so do nothing
            } else {
                //dialog is not showing
                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_OPEN_RECHARGE_PROCESS_DIALOG, CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");
                paymentProcessDialog = new PaymentProcessDialog(String.valueOf(rechargeAmount));
                paymentProcessDialog.show(getSupportFragmentManager(), "PaymentProcessDialog");
            }
        } catch (Exception e) {
            //
        }
    }

    private Map<String, String> getParamsForWalletRecharge(String orderStatus, String orderId, String razorpayid) {
        HashMap<String, String> headers = new HashMap<String, String>();
        try {
            headers.put("key", CUtils.getApplicationSignatureHashCode(activity));
            headers.put("od", orderId);
            headers.put("isSucess", orderStatus);
            headers.put("paycurr", "INR");
            headers.put("razorpayid", razorpayid);
            Log.e("testOnRespose", "getParamsForWalletRecharge==>>" + headers.toString());
        } catch (Exception e) {
            //
        }
        return CUtils.setRequiredParams(headers);

    }

    private void startPrefatchDataService() {
        try {
            Intent intentService = new Intent(activity, PreFetchDataservice.class);
            activity.startService(intentService);
        } catch (Exception e) {
        }

    }

    public void udatePaymentStatusOnserveronFailed(final String orderStatus, final String orderID, final String amount, final String paymentMode) {
        String orderUrl = "";

        if (paymentMode.equalsIgnoreCase(CGlobalVariables.PAYTM)) {
            orderUrl = CGlobalVariables.UPDATE_STATUS_RAZOR_PAY;
        } else {
            orderUrl = CGlobalVariables.UPDATE_PAY_STATUS_ASKQUE;
        }
        if (pd == null) pd = new CustomProgressDialog(activity);
        pd.show();
        pd.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, orderUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                hideProgressBar();
                //Log.e(("LoadMore recharge resp ", response);
                try {
                    JSONArray array = new JSONArray(response);
                    JSONObject obj = array.getJSONObject(0);
                    String result = obj.getString("Result");
                    //for paytm  //1 success or fail update & 5 when s2s update server api
                    //result = 1 for succesfully updated and result =2 for data updated from server to server
                    if (result.equalsIgnoreCase("1") || result.equalsIgnoreCase("2")) {
                        //callback.getCallBack("1", callBack.POST_RAZORPAYSTATUS, "", "");
                    } else {
                        //result = 5 for data not succesfully updated
                        //callback.getCallBack("0", callBack.POST_RAZORPAYSTATUS, "", "");
                    }
                    if (!isPaymentNetworkFailed) {
                        PaymentFailDialog dialog = new PaymentFailDialog();
                        dialog.show(getSupportFragmentManager(), "PaymentFailDialog");
                    }
                    isPaymentNetworkFailed = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressBar();
                //callback.getCallBack("0", callBack.POST_RAZORPAYSTATUS, "", "");
                VolleyLog.d("VolleyError: " + error.getMessage());
                try {
                    PaymentFailDialog dialog = new PaymentFailDialog();
                    dialog.show(getSupportFragmentManager(), "PaymentFailDialog");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded;charset=UTF-8";
            }

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("key", CUtils.getApplicationSignatureHashCode(activity));
                headers.put("amount", amount);
                headers.put("orderid", orderID);
                headers.put("paycurr", "INR");
                headers.put("status", orderStatus);
                headers.put("od", od);
                headers.put("phonepeid", phonepeid);
                headers.put("phonepeOrderId", phonepeOrderId);
                headers.put("profile_Id", CUtils.getUserID(activity));

                if (paymentMode.equalsIgnoreCase(CGlobalVariables.PAYTM)) {
                    headers.put("chatid", "");
                } else {
                    headers.put("paymentid", "");
                    headers.put("razorpayresponse", "0");
                }
                headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
                headers.put(CGlobalVariables.DEVICE_ID, CUtils.getMyAndroidId(activity));
                headers.put(CGlobalVariables.APP_VERSION, BuildConfig.VERSION_NAME);
                headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(activity));
                return headers;
            }

        };
        // Add the request to the RequestQueue.
        //com.google.analytics.tracking.android.////Log.e(("API HIT HERE");
        int socketTimeout = 60000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID, PaymentData paymentData) {
        //Log.e("RazorPayPayment","onPaymentSuccess razorpayPaymentID="+razorpayPaymentID+" getPaymentId="+paymentData.getPaymentId() +" getData="+paymentData.getData());
        if (razorpayPaymentID == null) {
            razorpayPaymentID = "";
        }
        sendDataToServer("1", razorpayPaymentID, false);
    }

    @Override
    public void onPaymentError(int code, String response, PaymentData paymentData) {//failed payment
        //Log.e("RazorPayPayment","onPaymentError code="+code+" response="+response +" getData="+paymentData.getData());

        if (code == Checkout.PAYMENT_CANCELED) {

            com.ojassoft.astrosage.utils.CUtils.saveBooleanData(this, com.ojassoft.astrosage.utils.CGlobalVariables.IS_SUBSCRIPTION_ERROR, true);
            sendDataToServer("0", "", false);
        } else {
            isPaymentNetworkFailed = true;
            udatePaymentStatusOnserveronFailed("0", orderId, String.valueOf(rechargeAmount), paymentModeStr);
            PaymentFailDialog dialog = new PaymentFailDialog();
            dialog.setTryAgainCallback(() -> {
                paymentModeStr = CGlobalVariables.PAYTM;
                getOrderId();
                CUtils.fcmAnalyticsEvents(CGlobalVariables.PAYMENT_FAILED_REDIRECT_TO_PAYTM, CGlobalVariables.FIREBASE_EVENT_PAYMENT_FAILED, "");

                CUtils.addFacebookEventPaymentFailed( CGlobalVariables.PAYMENT_FAILED_REDIRECT_TO_PAYTM, actualraters, CGlobalVariables.CURRENCY_INDIA, "");

            });
            dialog.show(getSupportFragmentManager(), "PaymentFailDialog");
            CUtils.fcmAnalyticsEvents(CGlobalVariables.PAYMENT_FAILED_RAZORPAY, CGlobalVariables.FIREBASE_EVENT_PAYMENT_FAILED, "");

            CUtils.addFacebookEventPaymentFailed( CGlobalVariables.PAYMENT_FAILED_RAZORPAY, actualraters, CGlobalVariables.CURRENCY_INDIA, "");

        }
    }

    // Helper class to group the views for each payment option
    static class PaymentView {
        final LinearLayout layout;
        final ImageView icon;
        final TextView text;
        final String paymentMethodName;

        PaymentView(LinearLayout layout, ImageView icon, TextView text, String paymentMethodName) {
            this.layout = layout;
            this.icon = icon;
            this.text = text;
            this.paymentMethodName = paymentMethodName;
        }
    }








}