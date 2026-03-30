package com.ojassoft.astrosage.varta.ui.activity;

import static com.ojassoft.astrosage.utils.CGlobalVariables.GET_USER_CHAT_CATEGORY_ENABLED;
import static com.ojassoft.astrosage.utils.CGlobalVariables.INTERNATIONAL_DEFAULT_ORDER_PAYMENT_METHOD;
import static com.ojassoft.astrosage.utils.CGlobalVariables.IS_STRIPE_VISIBLE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_DOMESTIC_DOLLAR_CR;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_DOMESTIC_GST_RATE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_INTERNATIONAL_DOLLAR_CR;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_INTERNATIONAL_GST_RATE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.MAX_UPI_AMOUNT_VAL;
import static com.ojassoft.astrosage.utils.CGlobalVariables.SPECIAL_RECHARGE_25_BTN_CLICK;
import static com.ojassoft.astrosage.utils.CGlobalVariables.SPECIAL_RECHARGE_25_DISMISS;
import static com.ojassoft.astrosage.utils.CGlobalVariables.SPECIAL_RECHARGE_25_SHOWN;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.PAYMENT_INFO_SCREEN;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.REQUEST_CODE_CCAVENUE;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.REQUEST_CODE_LOGIN;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.SHOW_DIALOG_EVENT;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.SHOW_INTERNATIONAL_PAY_IN_INR;
import static com.ojassoft.astrosage.varta.utils.CUtils.getProfileForChatFromPreference;
import static com.ojassoft.astrosage.varta.utils.CUtils.getReducedPriceRechargeDetails;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
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
import com.ojassoft.astrosage.networkcall.GlobalRetrofitResponse;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.utils.AvenuesParams;
import com.ojassoft.astrosage.utils.UPIAppChecker;
import com.ojassoft.astrosage.varta.dialog.CallMsgDialog;
import com.ojassoft.astrosage.varta.dialog.CoupanCodeDialog;
import com.ojassoft.astrosage.varta.dialog.PaymentFailDialog;
import com.ojassoft.astrosage.varta.dialog.PaymentProcessDialog;
import com.ojassoft.astrosage.varta.dialog.PaymentSucessfulDialog;
import com.ojassoft.astrosage.varta.interfacefile.IAskCallback;
import com.ojassoft.astrosage.varta.model.UPIResponse;
import com.ojassoft.astrosage.varta.model.UserProfileData;
import com.ojassoft.astrosage.varta.model.WalletAmountBean;
import com.ojassoft.astrosage.varta.service.Loginservice;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.ChatUtils;
import com.ojassoft.astrosage.varta.utils.CustomProgressDialog;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.varta.utils.GetCheckSum;
import com.ojassoft.astrosage.varta.utils.NumberUtils;
import com.ojassoft.astrosage.varta.utils.UPIResponseParser;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.paytm.pgsdk.TransactionManager;
import com.phonepe.intent.sdk.api.PhonePeKt;
import com.phonepe.intent.sdk.api.models.PhonePeEnvironment;
import com.razorpay.Checkout;
import com.razorpay.PaymentData;
import com.razorpay.PaymentResultWithDataListener;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class PaymentInformationActivity extends BaseActivity implements View.OnClickListener,
        VolleyResponse, IAskCallback, PaymentResultWithDataListener, GlobalRetrofitResponse {
    ImageView backIV;
    TextView titleTV;
    TextView totalAmountKeyTV;
    TextView totalAmountValTV;
    TextView gstKeyTV;
    TextView gstValTV;
    TextView totalPayableAmountKeyTV;
    TextView totalPayableAmountValTV;
    TextView coupanTV;
    Button payNowBtn;
    double rechargeAmount, paymentAmount, actualraters;
    RequestQueue queue;
    boolean result;
    LinearLayout  upiPaymentOptionsContainer;
    TextView other_upi_text;
    WalletAmountBean walletAmountBean;
    int selectedPosition;
    String userInputRechargeAmount,frompopunder="0",screenOpenFrom="PaymentInformationActivity";
    double gst, dollorRate;
    WalletAmountBean.Services services;
    LinearLayout containerLayout;
    LinearLayout applyCoupanLinearLayout;
    CustomProgressDialog pd;
    String COUNTRY_CODE_IND = "91";
    String currency = CGlobalVariables.CURRENCY_INDIA;
    String orderId = "";
    String amountToPay = "0";
    String callingActivity;
    String astrologerPhoneNumber;
    String astrlogerUrlText, chatChannelId;
    String coupanCode = "", paymentModeStr = "";
    private String chat_Id = "";
    String payStatus = "0";
    TextView payNowWithPaytmBtn, or_txt1, orTxt;
    TextView payNowWithCCAvenueBtn;
    LinearLayout bonusRechargeLL, pilLlpayinindianrupee;
    TextView bonusTv;
    TextView balanceTv;
    private EditText coupanCodeEt;
    private Button applyCodeBtn;
    private ImageView couponRemove;
    private TextView coupanTxtSuccess,upiPaymentOptionsHeading;
    private CheckBox payInRsCheckBox;
    boolean getOrderIdReq;
    double totalDollar;
    int upiAppNo = 0 ;
   // String upiOPrderId;
    private boolean isAutoselected;
    RadioButton radioButtonRazorpay,radioButtonPhonePeCheckout;
    RadioButton radioButtonBankTransferOther;
    RadioButton radioButtonWallet;
    RadioButton radioButtonCreditDebitCardRazorpay;
    RadioButton radioButtonPaytm;
    RadioButton radioButtonOther;
    RadioButton radioButtonAlternativeGateway;
    private String nearCity="Agra";
    private String state="Uttar Pradesh";
//    PayPalButton payPalButton;
private static final String TAG = "PaymentInformationActivity";

    //public static final int PAYPAL_REQUEST_CODE = 123;
/*    private static PayPalConfiguration config = new PayPalConfiguration()
            // Start with mock environment.  When ready, switch to sandbox (ENVIRONMENT_SANDBOX)
            // or live (ENVIRONMENT_PRODUCTION)
            .environment(PayPalConfiguration.ENVIRONMENT_SANDBOX)
            .clientId(PayPalConfig.PAYPAL_CLIENT_ID);*/
    private String selectedPayMode = "";
    List<PaymentView> paymentOptions;
    private  ActivityResultLauncher<Intent> upiLauncher,activityResultLauncherStandard;
    private String od = "", phonepeid = "", phonepeOrderId = "";
    private  boolean intentFlowVisibilityForVartaWallet;
    RelativeLayout upiPaymentOptionsLayout;

    private PaymentSheet paymentSheet;
    private PaymentSheet.CustomerConfiguration customerConfig;
    private String paymentIntentClientSecret;
    private String intentID;
    private String currencyUSD = "USD";
    private String priceToPay;
    private boolean isStripeVisible = false,isPhonePeCheckoutVisible;
    private String internationalDefaultOrderPaymentMethod = CGlobalVariables.RAZORPAY;
    boolean isRetryPayment;

    public  static String errorMsg;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LANGUAGE_CODE = AstrosageKundliApplication.getAppContext().getLanguageCode();
        CUtils.getRobotoFont(PaymentInformationActivity.this, LANGUAGE_CODE, CGlobalVariables.regular);
        CUtils.addFacebookAndFirebaseEvent(AppEventsConstants.EVENT_NAME_ADDED_TO_CART,"wallet_screen","PaymentInformationActivity");
        setContentView(R.layout.payment_information_layout);
        queue = VolleySingleton.getInstance(this).getRequestQueue();

        getDataFromIntent();
        //get ids of views
        applyCoupanLinearLayout = findViewById(R.id.apply_coupan_container);
        containerLayout = findViewById(R.id.container);
        payNowWithPaytmBtn = findViewById(R.id.pay_now_with_paytm_btn);
        payNowWithCCAvenueBtn = findViewById(R.id.pay_now_with_ccavenue_btn);
        or_txt1 = findViewById(R.id.or_txt1);
        orTxt = findViewById(R.id.or_txt);
        backIV = findViewById(R.id.ivBack);
        titleTV = findViewById(R.id.tvTitle);
        totalAmountKeyTV = findViewById(R.id.total_amount_tv);
        totalAmountValTV = findViewById(R.id.total_amount_val_tv);
        gstKeyTV = findViewById(R.id.gst_key_tv);
        gstValTV = findViewById(R.id.gst_val_tv);
        totalPayableAmountKeyTV = findViewById(R.id.total_payable_amount_key_tv);
        totalPayableAmountValTV = findViewById(R.id.total_payable_amount_val_tv);
        coupanTV = findViewById(R.id.coupan_tv);
        payNowBtn = findViewById(R.id.pay_now_btn);
        bonusRechargeLL = findViewById(R.id.bonus_recharge_ll);
        pilLlpayinindianrupee = findViewById(R.id.pil_llpayinindianrupee);
        bonusTv = findViewById(R.id.bonus_tv);
        balanceTv = findViewById(R.id.balance_tv);
        coupanCodeEt = findViewById(R.id.coupan_code_et);
        applyCodeBtn = findViewById(R.id.apply_code_btn);
        couponRemove = findViewById(R.id.coupon_remove);
        coupanTxtSuccess = findViewById(R.id.coupan_txt_success);
        TextView creditDebitCardTitleTV = findViewById(R.id.font_auto_payment_information_layout_17);
        TextView walletSubtitleTV = findViewById(R.id.font_auto_payment_information_layout_20);
        TextView paytmGatewayTitleTV = findViewById(R.id.font_auto_payment_information_layout_21);
        TextView alternateGatewayTitleTV = findViewById(R.id.font_auto_payment_information_layout_22);
        TextView otherGatewayTitleTV = findViewById(R.id.font_auto_payment_information_layout_23);
        TextView otherGatewaySubtitleTV = findViewById(R.id.font_auto_payment_information_layout_24);
        FontUtils.changeFont(this, creditDebitCardTitleTV, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(this, walletSubtitleTV, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(this, paytmGatewayTitleTV, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(this, alternateGatewayTitleTV, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(this, otherGatewayTitleTV, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(this, otherGatewaySubtitleTV, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        payInRsCheckBox = findViewById(R.id.payInRsCheckBox);
        payInRsCheckBox.setOnClickListener(this);
        applyCodeBtn.setOnClickListener(this);
        couponRemove.setOnClickListener(this);
        backIV.setOnClickListener(this);
        payNowBtn.setOnClickListener(this);
        payNowWithPaytmBtn.setOnClickListener(this);
        payNowWithCCAvenueBtn.setOnClickListener(this);
        applyCoupanLinearLayout.setOnClickListener(this);
        payNowWithPaytmBtn.setText(Html.fromHtml(getResources().getString(R.string.paytm_payment_gateway)));
        payNowWithCCAvenueBtn.setText(Html.fromHtml(getResources().getString(R.string.alternate_gateway)));

        setData();
        setTypeface();
        setBonusView("0", false);
        CardView upiPaymentOptions = findViewById(R.id.upiPaymentOptions);
         upiPaymentOptionsLayout = findViewById(R.id.rlRazorpayPayments);
        upiPaymentOptionsHeading = findViewById(R.id.upiPaymentOptionsHeading);
         upiPaymentOptionsContainer = findViewById(R.id.upiPaymentOptionsContainer);
         other_upi_text = findViewById(R.id.other_upi_text);
        RelativeLayout rlPhonePeCheckout = findViewById(R.id.rlPhonePeCheckout);
        RelativeLayout rlRazorpay = findViewById(R.id.rlRazorpay);
        RelativeLayout rlCreditDebitCardRazorpay = findViewById(R.id.rlCreditDebitCardRazorpay);
        RelativeLayout rlBankTransferOther = findViewById(R.id.rlBankTransferOther);
        RelativeLayout rlPaytmPaymentGetWay = findViewById(R.id.rlPaytmPaymentGetWay);
        RelativeLayout rlOther = findViewById(R.id.rlOther);
        RelativeLayout rlAlternativeGetway = findViewById(R.id.rlAlternativeGetway);
        RelativeLayout rlWallet = findViewById(R.id.rlWallet);
        CardView cardViewOtherGateway = findViewById(R.id.cardViewOtherGateway);
        Button proceedToPayButton = findViewById(R.id.proceed_to_pay_button);
        proceedToPayButton.setText(getResources().getString(R.string.paynow_amount));
        TextView otherPaymentMethod = findViewById(R.id.otherPaymentMethod);
        TextView tvRazorpay = findViewById(R.id.tvRazorpay);
        TextView txtViewBankTransfer = findViewById(R.id.txtViewBankTransfer);
        ImageView imgViewRazorpay = findViewById(R.id.imgViewRazorpay);
         radioButtonRazorpay = findViewById(R.id.radioButtonRazorpay);

        TextView tvPhonePeCheckout = findViewById(R.id.tvPhonePeCheckout);
        ImageView imgViewPhonePeCheckout = findViewById(R.id.imgViewPhonePeCheckout);
        radioButtonPhonePeCheckout = findViewById(R.id.radioButtonPhonePeCheckout);

        radioButtonBankTransferOther = findViewById(R.id.radioButtonBankTransferOther);
        radioButtonWallet = findViewById(R.id.radioButtonWallet);
         radioButtonCreditDebitCardRazorpay = findViewById(R.id.radioButtonCreditDebitCardRazorpay);
         radioButtonPaytm = findViewById(R.id.radioButtonPaytm);
        radioButtonOther = findViewById(R.id.radioButtonOther);
         radioButtonAlternativeGateway = findViewById(R.id.radioButtonAlternativeGateway);

        // handle for IND country and international country payment options
        if (CUtils.getCountryCode(PaymentInformationActivity.this).equals(COUNTRY_CODE_IND)) {
            orTxt.setVisibility(View.GONE);
            payNowWithPaytmBtn.setVisibility(View.GONE);
            rlRazorpay.setVisibility(View.GONE);
            cardViewOtherGateway.setVisibility(View.VISIBLE);
            rlWallet.setVisibility(View.VISIBLE);
            txtViewBankTransfer.setText(getResources().getString(R.string.net_banking));
            otherPaymentMethod.setText(getResources().getString(R.string.pay_via_cards_banking_wallets));

            //show upi payment options
            upiPaymentOptions.setVisibility(View.VISIBLE);
            //default selected pay mode is razorpay or paytam upi for indian users
            selectedPayMode = CGlobalVariables.RAZORPAY_UPI;
            imgViewRazorpay.setImageResource(R.drawable.ic_razor_pay);
            tvRazorpay.setText(getResources().getString(R.string.pay_with_razorpay));
           // rlBankTransferOther.setVisibility(View.GONE);
            isPhonePeCheckoutVisible = CUtils.getBooleanData(this, com.ojassoft.astrosage.utils.CGlobalVariables.IS_PHONEPE_CHECKOUT_VISIBLE, false);
            String defaultOrderPaymentMethod = CUtils.getStringData(PaymentInformationActivity.this, com.ojassoft.astrosage.utils.CGlobalVariables.DEFAULT_ORDER_PAYMENT_METHOD, CGlobalVariables.RAZORPAY);
            if (defaultOrderPaymentMethod.equals(CGlobalVariables.TAG_MANGER_PHONEPE_CHECKOUT ) || isPhonePeCheckoutVisible){//get data from calling activity
                result = PhonePeKt.init(
                        this,                       // context
                        CGlobalVariables.CHECKOUT_MERCHANT_ID,                      // merchantId
                        CUtils.getUserIdForBlock(PaymentInformationActivity.this),                  // flowId
                        PhonePeEnvironment.RELEASE, // environment
                        true,                      // enableLogging
                        null                        // appId
                );

                if (result) {
                    //Toast.makeText(this,result+"",Toast.LENGTH_LONG).show();
                    // Good to go
                }
            }
        } else {
            orTxt.setVisibility(View.GONE);
            payNowWithPaytmBtn.setVisibility(View.GONE);
            cardViewOtherGateway.setVisibility(View.GONE);
            rlWallet.setVisibility(View.GONE);
            //set text of other payment method
            otherPaymentMethod.setText(getResources().getString(R.string.payment_method));
            //not show upi payment options
            upiPaymentOptionsHeading.setVisibility(View.GONE);
            upiPaymentOptions.setVisibility(View.GONE);
            isStripeVisible = CUtils.getBooleanData(this,IS_STRIPE_VISIBLE, false);
            internationalDefaultOrderPaymentMethod = CUtils.getStringData(this,INTERNATIONAL_DEFAULT_ORDER_PAYMENT_METHOD, CGlobalVariables.RAZORPAY);

            if (internationalDefaultOrderPaymentMethod.equals(CGlobalVariables.STRIPE ) || isStripeVisible){
                PaymentConfiguration.init(this, CGlobalVariables.STRIPE_PUBLISHABLE_KEY);
                paymentSheet = new PaymentSheet(this, this::onPaymentSheetResult);
            }
            // selectedPayMode sirf tab change ho jab gateway actually STRIPE ho
            if (internationalDefaultOrderPaymentMethod.equals(CGlobalVariables.STRIPE)) {
                selectedPayMode = CGlobalVariables.STRIPE;
            } else {
                selectedPayMode = CGlobalVariables.RAZORPAY;
            }
            rlBankTransferOther.setVisibility(View.VISIBLE);

            // rlCreditDebitCardRazorpay.setVisibility(View.GONE);
            rlPaytmPaymentGetWay.setVisibility(View.GONE);
            //radioButtonRazorpay.setChecked(true);
            //default selected pay mode is razorpay upi for international users
            imgViewRazorpay.setImageResource(R.drawable.ic_paypal_square);
            tvRazorpay.setText(getResources().getString(R.string.title_paypal));
        }


        boolean ccavenueVisibilityForVartaWallet = CUtils.getBooleanData(this, CGlobalVariables.ccavenueVisibilityForVartaWallet, false);
        if (ccavenueVisibilityForVartaWallet) {
            rlAlternativeGetway.setVisibility(View.VISIBLE);
            payNowWithCCAvenueBtn.setVisibility(View.GONE);
            or_txt1.setVisibility(View.GONE);
        } else {
            payNowWithCCAvenueBtn.setVisibility(View.GONE);
            or_txt1.setVisibility(View.GONE);
            rlAlternativeGetway.setVisibility(View.GONE);
        }
        // handle for intent flow visibility for varta wallet
         intentFlowVisibilityForVartaWallet = CUtils.getBooleanData(this, CGlobalVariables.INTENT_FLOW_VISIBILITY_FOR_VARTA_WALLET, false);
        // handle for max upi amount
        if (paymentAmount > MAX_UPI_AMOUNT_VAL) {
            intentFlowVisibilityForVartaWallet = false;
        }

        if (intentFlowVisibilityForVartaWallet) {
            upiPaymentOptionsContainer.setVisibility(View.VISIBLE);
            other_upi_text.setText(getResources().getString(R.string.pay_with_other_upi_apps));
        } else {
            selectedPayMode = CGlobalVariables.RAZORPAY;
            //radioButtonRazorpay.performClick();
            upiPaymentOptionsHeading.setVisibility(View.GONE);
            upiPaymentOptionsContainer.setVisibility(View.GONE);
            other_upi_text.setText(getResources().getString(R.string.pay_with_upi_apps));
        }
        // handle for phonepe payment gateway
        setPhonePePaymentGetwayData();
        upiPaymentOptionsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelectPaymentOption(paymentOptions);
                selectedPayMode = CGlobalVariables.RAZORPAY_UPI;
                CUtils.fcmAnalyticsEvents(CGlobalVariables.PAY_RAZORPAY_UPI, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                String defaultOrderPaymentMethod = CUtils.getStringData(PaymentInformationActivity.this, com.ojassoft.astrosage.utils.CGlobalVariables.DEFAULT_ORDER_PAYMENT_METHOD, CGlobalVariables.RAZORPAY);
                if (defaultOrderPaymentMethod.equals(CGlobalVariables.PAYTM)) {
                    paymentModeStr = CGlobalVariables.PAYTM;
                    getOrderId();
                } else if(defaultOrderPaymentMethod.equals(CGlobalVariables.TAG_MANGER_PHONEPE_CHECKOUT)){
                    selectedPayMode = CGlobalVariables.PAYMENTS_PHONEPE_CHECKOUT;
                    paymentModeStr = CGlobalVariables.PAYMENTS_PHONEPE_CHECKOUT;
                    getApiCallForPhonePayIntentUrl(CGlobalVariables.PAYMENTS_PHONEPE_CHECKOUT);

                }else {
                    paymentModeStr = CGlobalVariables.RAZORPAY;
                    getOrderId();
                }

            }
        });
        radioButtonRazorpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelectPaymentOption(paymentOptions);
                selectedPayMode = CGlobalVariables.RAZORPAY;
                radioButtonRazorpay.setChecked(true);
                radioButtonPhonePeCheckout.setChecked(false);
                radioButtonBankTransferOther.setChecked(false);
                radioButtonWallet.setChecked(false);
                radioButtonCreditDebitCardRazorpay.setChecked(false);
                radioButtonPaytm.setChecked(false);
                radioButtonOther.setChecked(false);
                radioButtonAlternativeGateway.setChecked(false);
            }
        });
        radioButtonPhonePeCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelectPaymentOption(paymentOptions);
                selectedPayMode = CGlobalVariables.PAYMENTS_PHONEPE_CHECKOUT;
                paymentModeStr = CGlobalVariables.PAYMENTS_PHONEPE_CHECKOUT;
                radioButtonRazorpay.setChecked(false);
                radioButtonPhonePeCheckout.setChecked(true);
                radioButtonBankTransferOther.setChecked(false);
                radioButtonWallet.setChecked(false);
                radioButtonCreditDebitCardRazorpay.setChecked(false);
                radioButtonPaytm.setChecked(false);
                radioButtonOther.setChecked(false);
                radioButtonAlternativeGateway.setChecked(false);
            }
        });
        radioButtonBankTransferOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelectPaymentOption(paymentOptions);
                selectedPayMode = CGlobalVariables.NET_BANKING_RAZORPAY;
                radioButtonRazorpay.setChecked(false);
                radioButtonPhonePeCheckout.setChecked(false);
                radioButtonBankTransferOther.setChecked(true);
                radioButtonWallet.setChecked(false);
                radioButtonCreditDebitCardRazorpay.setChecked(false);
                radioButtonPaytm.setChecked(false);
                radioButtonOther.setChecked(false);
                radioButtonAlternativeGateway.setChecked(false);
            }
        });
        radioButtonWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelectPaymentOption(paymentOptions);
                selectedPayMode = CGlobalVariables.WALLET_RAZORPAY;
                radioButtonRazorpay.setChecked(false);
                radioButtonPhonePeCheckout.setChecked(false);
                radioButtonBankTransferOther.setChecked(false);
                radioButtonWallet.setChecked(true);
                radioButtonCreditDebitCardRazorpay.setChecked(false);
                radioButtonPaytm.setChecked(false);
                radioButtonOther.setChecked(false);
                radioButtonAlternativeGateway.setChecked(false);
            }
        });
        radioButtonCreditDebitCardRazorpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelectPaymentOption(paymentOptions);
                if (CUtils.getCountryCode(PaymentInformationActivity.this).equals(COUNTRY_CODE_IND)) {
                    selectedPayMode = CGlobalVariables.CREDIT_DEBIT_CARD_RAZORPAY;

                } else {
                    if (isStripeVisible){
                        selectedPayMode = CGlobalVariables.STRIPE;
                    }else {
                        selectedPayMode = CGlobalVariables.RAZORPAY;
                    }
                }
                radioButtonCreditDebitCardRazorpay.setActivated(true);
                radioButtonRazorpay.setChecked(false);
                radioButtonPhonePeCheckout.setChecked(false);
                radioButtonBankTransferOther.setChecked(false);
                radioButtonWallet.setChecked(false);
                radioButtonPaytm.setChecked(false);
                radioButtonOther.setChecked(false);
                radioButtonAlternativeGateway.setChecked(false);
            }
        });

        radioButtonPaytm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelectPaymentOption(paymentOptions);
                selectedPayMode = CGlobalVariables.PAYTM_PAYMENT_GETWAY;
                radioButtonPaytm.setChecked(true);
                radioButtonOther.setChecked(false);
                radioButtonPhonePeCheckout.setChecked(false);
                radioButtonRazorpay.setChecked(false);
                radioButtonBankTransferOther.setChecked(false);
                radioButtonWallet.setChecked(false);
                radioButtonCreditDebitCardRazorpay.setChecked(false);
                radioButtonAlternativeGateway.setChecked(false);
            }
        });
        radioButtonOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelectPaymentOption(paymentOptions);
                selectedPayMode = CGlobalVariables.ALTERNATIVE_GETWAY;
                radioButtonPaytm.setChecked(false);
                radioButtonOther.setChecked(true);
                radioButtonPhonePeCheckout.setChecked(false);
                radioButtonRazorpay.setChecked(false);
                radioButtonBankTransferOther.setChecked(false);
                radioButtonWallet.setChecked(false);
                radioButtonCreditDebitCardRazorpay.setChecked(false);
                radioButtonAlternativeGateway.setChecked(false);
            }
        });

        radioButtonAlternativeGateway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unSelectPaymentOption(paymentOptions);
                selectedPayMode = CGlobalVariables.ALTERNATIVE_GETWAY;
                radioButtonAlternativeGateway.setChecked(true);
                radioButtonRazorpay.setChecked(false);
                radioButtonPhonePeCheckout.setChecked(false);
                radioButtonBankTransferOther.setChecked(false);
                radioButtonWallet.setChecked(false);
                radioButtonPaytm.setChecked(false);
                radioButtonOther.setChecked(false);
                radioButtonCreditDebitCardRazorpay.setChecked(false);
            }
        });
        rlRazorpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPayMode = CGlobalVariables.RAZORPAY;
                radioButtonRazorpay.performClick();
            }
        });
        rlPhonePeCheckout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPayMode = CGlobalVariables.PAYMENTS_PHONEPE_CHECKOUT;
                paymentModeStr = CGlobalVariables.PAYMENTS_PHONEPE_CHECKOUT;
                radioButtonPhonePeCheckout.performClick();
            }
        });

        rlBankTransferOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPayMode = CGlobalVariables.NET_BANKING_RAZORPAY;
                radioButtonBankTransferOther.performClick();
            }
        });
        rlWallet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPayMode = CGlobalVariables.WALLET_RAZORPAY;
                radioButtonWallet.performClick();
            }
        });


        rlCreditDebitCardRazorpay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (CUtils.getCountryCode(PaymentInformationActivity.this).equals(COUNTRY_CODE_IND)) {
                    selectedPayMode = CGlobalVariables.CREDIT_DEBIT_CARD_RAZORPAY;

                } else {
                    if (isStripeVisible){
                        selectedPayMode = CGlobalVariables.STRIPE;
                    }else {
                        selectedPayMode = CGlobalVariables.RAZORPAY;
                    }
                }
                radioButtonCreditDebitCardRazorpay.performClick();
            }
        });
        rlPaytmPaymentGetWay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPayMode = CGlobalVariables.PAYTM_PAYMENT_GETWAY;
                radioButtonPaytm.performClick();

            }
        });
        rlOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPayMode = CGlobalVariables.ALTERNATIVE_GETWAY;
                radioButtonOther.performClick();

            }
        });
        rlAlternativeGetway.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedPayMode = CGlobalVariables.ALTERNATIVE_GETWAY;
                radioButtonAlternativeGateway.performClick();
            }
        });
        //This method is used to click on the alternative gateway layout
        //when this layout is clicked then the selectedPayMode will be set to alternative gateway
        //and the radio button of alternative gateway will be checked
        //and the radio button for other payment mode will be unchecked
        proceedToPayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (selectedPayMode) {
                    case CGlobalVariables.RAZORPAY_UPI:
                        upiPaymentOptionsLayout.performClick();
                        break;
                    case CGlobalVariables.UPI_PAYMENTS_PHONE:
                        paymentModeStr = CGlobalVariables.UPI_PAYMENTS_PHONE;
                        getApiCallForPhonePayIntentUrl(CGlobalVariables.UPI_PAYMENTS_PHONE);
                        break;
                    case CGlobalVariables.UPI_PAYMENTS_BHIM:
                        paymentModeStr = CGlobalVariables.UPI_PAYMENTS_BHIM;
                        getApiCallForPhonePayIntentUrl(CGlobalVariables.UPI_PAYMENTS_BHIM);
                        break;
                    case CGlobalVariables.UPI_PAYMENTS_GPAY:
                        paymentModeStr = CGlobalVariables.UPI_PAYMENTS_GPAY;
                        getApiCallForPhonePayIntentUrl(CGlobalVariables.UPI_PAYMENTS_GPAY);
                        break;
                    case CGlobalVariables.UPI_PAYMENTS_PAYTM:
                        paymentModeStr = CGlobalVariables.UPI_PAYMENTS_PAYTM;
                        getApiCallForPhonePayIntentUrl(CGlobalVariables.UPI_PAYMENTS_PAYTM);
                        break;
                    case CGlobalVariables.UPI_PAYMENTS_CRED:
                        paymentModeStr = CGlobalVariables.UPI_PAYMENTS_CRED;
                        getApiCallForPhonePayIntentUrl(CGlobalVariables.UPI_PAYMENTS_CRED);
                        break;
                    case CGlobalVariables.RAZORPAY:
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.VIA_RAZORPAY, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                        openRazorPay();
                        break;
                    case CGlobalVariables.CREDIT_DEBIT_CARD_RAZORPAY:
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.VIA_RAZORPAY_CARDS, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                        openRazorPay();
                        break;
                        case CGlobalVariables.NET_BANKING_RAZORPAY:
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.VIA_NET_BANKING_RAZORPAY, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                        openRazorPay();
                        break;
                    case CGlobalVariables.PAYTM_PAYMENT_GETWAY:
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.VIA_PAYTM_GATEWAY, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                        paymentModeStr = CGlobalVariables.PAYTM;
                        getOrderId();
                        break;
                    case CGlobalVariables.ALTERNATIVE_GETWAY:
                        paymentModeStr = CGlobalVariables.RAZORPAY;
                       // getOrderId();
                        openAlternatePaymentOption();
                        break;
                    case CGlobalVariables.STRIPE:
                        paymentModeStr = CGlobalVariables.STRIPE;
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.VIA_STRIPE_CARDS, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                        generateOrderForStripe();
                        break;
                    case CGlobalVariables.PAYMENTS_PHONEPE_CHECKOUT:
                        selectedPayMode = CGlobalVariables.PAYMENTS_PHONEPE_CHECKOUT;
                        paymentModeStr = CGlobalVariables.PAYMENTS_PHONEPE_CHECKOUT;
                        getApiCallForPhonePayIntentUrl(CGlobalVariables.PAYMENTS_PHONEPE_CHECKOUT);
                        break;
                    case CGlobalVariables.WALLET_RAZORPAY:
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.VIA_WALLET_RAZORPAY, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                        openRazorPay();
                        break;
                }
            }
        });

/*        Intent intent = new Intent(this, PayPalService.class);
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);
        startService(intent);*/

/*        payPalButton = (PayPalButton)findViewById(R.id.payPalButton);

        if (CUtils.getCountryCode(PaymentInformationActivity.this).equals("91")) {
            payPalButton.setVisibility(View.GONE);
            or_txt1.setVisibility(View.GONE);
            orTxt.setVisibility(View.VISIBLE);
            payNowWithPaytmBtn.setVisibility(View.VISIBLE);

        } else {
            orTxt.setVisibility(View.GONE);
            payNowWithPaytmBtn.setVisibility(View.GONE);
            payPalButton.setVisibility(View.VISIBLE);
            or_txt1.setVisibility(View.VISIBLE);

        }
/*
        CheckoutConfig config = new CheckoutConfig(getApplication(),
                PayPalConfig.PAYPAL_CLIENT_ID,
                Environment.LIVE,
                String.format("%s://paypalpay", BuildConfig.APPLICATION_ID),
                CurrencyCode.USD,
                UserAction.PAY_NOW,
                new SettingsConfig(
                        true,
                        false
                )
        );
        PayPalCheckout.setConfig(config);*/

        //paymentModeStr = CGlobalVariables.PAYPAL;
        //getOrderId();

/*        payPalButton.setup(
                new CreateOrder() {
                    @Override
                    public void create(@NotNull CreateOrderActions createOrderActions) {
                        paymentModeStr = CGlobalVariables.PAYPAL;
                        getOrderId();
                    }
                },
                new OnApprove() {
                    @Override
                    public void onApprove(@NotNull Approval approval) {
                        approval.getOrderActions().capture(new OnCaptureComplete() {
                            @Override
                            public void onCaptureComplete(@NotNull CaptureOrderResult result) {
                            }
                        });
                    }
                });*/

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {

                if(getCallingActivity() != null && !getCallingActivity().getClassName().equals(WalletActivity.class.getName())) {
                    checkSpacialRecharge();
                }else{
                    setResult(Activity.RESULT_CANCELED);
                    finish();
                }
            }
        });

        // set on activity result for upi launcher
        setOnActivityResultForUpiLauncher();
        setOnActivityResultForStandardCheckoutLauncher();
    }



    private void openRazorPay() {
//        String defaultOrderPaymentMethod = CUtils.getStringData(PaymentInformationActivity.this, com.ojassoft.astrosage.utils.CGlobalVariables.DEFAULT_ORDER_PAYMENT_METHOD, CGlobalVariables.RAZORPAY);
//        if (defaultOrderPaymentMethod.equals(CGlobalVariables.PAYTM)) {
//            paymentModeStr = CGlobalVariables.PAYTM;
//        } else {
//            paymentModeStr = CGlobalVariables.RAZORPAY;
//        }
        paymentModeStr = CGlobalVariables.RAZORPAY;
        getOrderId();
    }



    /*
        private void getPayPalPayment(String amount)
        {
            payPalButton.setup(
                    new CreateOrder() {
                        @Override
                        public void create(@NotNull CreateOrderActions createOrderActions) {
                            ArrayList purchaseUnits = new ArrayList<>();
                            purchaseUnits.add(new PurchaseUnit.Builder().amount(new Amount.Builder().currencyCode(CurrencyCode.USD).value(amount).build()).build());
                            Order order = new Order(OrderIntent.CAPTURE, new AppContext.Builder().userAction(UserAction.PAY_NOW).build(), purchaseUnits);
                            createOrderActions.create(order, (CreateOrderActions.OnOrderCreated) null);
                        }
                    },
                    new OnApprove() {
                        @Override
                        public void onApprove(@NotNull Approval approval) {
                            approval.getOrderActions().capture(new OnCaptureComplete() {
                                @Override
                                public void onCaptureComplete(@NotNull CaptureOrderResult result) {
                                    Log.e("CaptureOrder", String.format("CaptureOrderResult: %s", result));
                                    sendDataToServer("1");
                                }
                            });
                        }
                    },
                    new OnCancel() {
                        @Override
                        public void onCancel()
                        {
                            sendDataToServer("0");
                            Log.e("OnCancel", "Buyer cancelled");
                        }
                    },new OnError() {
                        @Override
                        public void onError(@NotNull ErrorInfo errorInfo) {
                            sendDataToServer("0");
                            Log.e("OnError", String.format("Error: %s", errorInfo));
                        }
                    });

       //Creating a paypalpayment
        PayPalPayment payment = new PayPalPayment(new BigDecimal(String.valueOf(amount)), "USD", "Ojas Paypal",
                PayPalPayment.PAYMENT_INTENT_SALE);

        //Creating Paypal Payment activity intent
        Intent intent = new Intent(this, PaymentActivity.class);

        //putting the paypal configuration to the intent
        intent.putExtra(PayPalService.EXTRA_PAYPAL_CONFIGURATION, config);

        //Puting paypal payment to the intent
        intent.putExtra(PaymentActivity.EXTRA_PAYMENT, payment);

        //Starting the intent activity for result
        //the request code will be used on the method onActivityResult
        startActivityForResult(intent, PAYPAL_REQUEST_CODE);
    }
    */

    private final BroadcastReceiver mReceiverBackgroundLoginService = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String status = intent.getStringExtra("status");
            if (status.equals(CGlobalVariables.SUCCESS)) {
                if (getOrderIdReq) {
                    getOrderIdReq = false;
                    getOrderId();
                } else {
                    applyCouponCode();
                }
            } else {
                CUtils.showSnackbar(containerLayout, getResources().getString(R.string.something_wrong_error), getApplicationContext());
            }

            if (mReceiverBackgroundLoginService != null) {
                LocalBroadcastManager.getInstance(PaymentInformationActivity.this).unregisterReceiver(mReceiverBackgroundLoginService);
            }
        }
    };

    private void getDataFromIntent() {

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        callingActivity = bundle.getString(CGlobalVariables.CALLING_ACTIVITY);
        selectedPosition = bundle.getInt(CGlobalVariables.SELECTED_POSITION, 0);
        userInputRechargeAmount = bundle.getString(CGlobalVariables.RECHARGE_AMOUNT, "");
        walletAmountBean = (WalletAmountBean) bundle.getSerializable(CGlobalVariables.WALLET_INFO);
        astrlogerUrlText = bundle.getString(CGlobalVariables.ASTROLGER_URL_TEXT);
        astrologerPhoneNumber = bundle.getString(CGlobalVariables.ASTROLOGER_MOB_NUMBER);
        if (intent.hasExtra(CGlobalVariables.CHANNEL_ID)) {
            chatChannelId = bundle.getString(CGlobalVariables.CHANNEL_ID);
        }
        if (intent.hasExtra("frompopunder")) {
            frompopunder = bundle.getString("frompopunder");
        }
        if (intent.hasExtra("screen_open_from")) {
            screenOpenFrom = bundle.getString("screen_open_from");
        }

        if (selectedPosition == -1) { //in case of user input recharge amount
            rechargeAmount = NumberUtils.safeParseDouble(userInputRechargeAmount);
        } else {
            services = walletAmountBean.getServiceList().get(selectedPosition);
            rechargeAmount = NumberUtils.safeParseDouble(services.getRaters());
        }
    }

    private void setBonusView(String couponAmt, boolean isCouponCodeApply) {
        try {
            WalletAmountBean.Services rechargeService = walletAmountBean.getServiceList().get(selectedPosition);
            String offerMsg = rechargeService.getOffermessage();
            //Log.e("SAN PFA setBonusView ", offerMsg);
            if (isCouponCodeApply || (walletAmountBean != null && (!TextUtils.isEmpty(offerMsg) && (!offerMsg.equalsIgnoreCase("null"))))) {

                bonusRechargeLL.setVisibility(View.VISIBLE);
                if (isCouponCodeApply || (!TextUtils.isEmpty(offerMsg) && NumberUtils.safeParseDouble(couponAmt) > 0)) {
                    bonusTv.setText(getString(R.string.final_amount) + " " + getString(R.string.astroshop_rupees_sign) + couponAmt);
                    balanceTv.setText(getString(R.string.astroshop_rupees_sign) + getRupeetoShow(rechargeService.getRaters(), "0", couponAmt) + " " + getString(R.string.bonus));
                } else {
                    bonusTv.setText(getString(R.string.final_amount) + " " + getString(R.string.astroshop_rupees_sign) + rechargeService.getOfferAmount());
                    balanceTv.setText(getString(R.string.astroshop_rupees_sign) + getRupeetoShow(rechargeService.getRaters(), rechargeService.getOfferAmount(), "0") + " " + getString(R.string.bonus));
                }
            } else {
                bonusRechargeLL.setVisibility(View.GONE);
                bonusTv.setText("");
                balanceTv.setText("");
            }
        } catch (Exception e) {
            e.printStackTrace();
            bonusRechargeLL.setVisibility(View.GONE);
            bonusTv.setText("");
            balanceTv.setText("");
            //Log.e(("SAN PFA exc ", e.toString());
        }
    }


    private String getRupeetoShow(String x, String y, String couponprice) {
        //Log.e(("SAN PFA ", "getRupeetoShow() x, y, couponprice => "+x + "<=>" + y + "<=>" + couponprice );
        double result = 0.0;
        result = NumberUtils.safeParseDouble(x) + NumberUtils.safeParseDouble(y) + NumberUtils.safeParseDouble(couponprice);
        return CUtils.convertAmtIntoIndianFormat(String.valueOf(result));
    }

    private void setTypeface() {
        FontUtils.changeFont(this, titleTV, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);

        FontUtils.changeFont(this, totalAmountKeyTV, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(this, gstKeyTV, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(this, totalPayableAmountKeyTV, CGlobalVariables.FONTS_OPEN_SANS_BOLD);

        FontUtils.changeFont(this, totalAmountValTV, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(this, gstValTV, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(this, totalPayableAmountValTV, CGlobalVariables.FONTS_OPEN_SANS_BOLD);

        FontUtils.changeFont(this, payNowBtn, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(this, payNowWithPaytmBtn, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(this, payNowWithCCAvenueBtn, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(this, coupanTV, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);

        FontUtils.changeFont(this, bonusTv, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(this, balanceTv, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);

        FontUtils.changeFont(this, coupanCodeEt, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(this, applyCodeBtn, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(this, coupanTxtSuccess, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);

    }

    private void setData() {
        String gstRate = walletAmountBean.getGstrate();
        String dollorConverstionRate = walletAmountBean.getDollorConverstionRate();
        if (TextUtils.isEmpty(gstRate)) {// in case of gstRate is not available then get from shared preferences(config api)
            if (CUtils.getCountryCode(PaymentInformationActivity.this).equalsIgnoreCase(COUNTRY_CODE_IND)) {
                gstRate = CUtils.getStringData(this, KEY_DOMESTIC_GST_RATE, "0");
            } else {
                gstRate = CUtils.getStringData(this, KEY_INTERNATIONAL_GST_RATE, "0");
            }
        }
        if (TextUtils.isEmpty(dollorConverstionRate)) {// in case of dollorConverstionRate is not available then get from shared preferences(config api)
            if (CUtils.getCountryCode(PaymentInformationActivity.this).equalsIgnoreCase(COUNTRY_CODE_IND)) {
                dollorConverstionRate = CUtils.getStringData(this, KEY_DOMESTIC_DOLLAR_CR, "0");
            } else {
                dollorConverstionRate = CUtils.getStringData(this, KEY_INTERNATIONAL_DOLLAR_CR, "0");
            }
        }
        gst = NumberUtils.safeParseDouble(gstRate);
        dollorRate = NumberUtils.safeParseDouble(dollorConverstionRate);

        double gstValAmt = Math.round(((rechargeAmount * gst) / 100) * 100.0) / 100.0;
        titleTV.setText(getResources().getString(R.string.payment_information));
        totalAmountValTV.setText(getResources().getString(R.string.rs_sign) + rechargeAmount);
        gstValTV.setText(getResources().getString(R.string.rs_sign) + gstValAmt);

        if (services != null) {
            paymentAmount = NumberUtils.safeParseDouble(services.getPaymentamount());
            actualraters = NumberUtils.safeParseDouble(services.getActualraters());
        } else {
            DecimalFormat df = (DecimalFormat) NumberFormat.getNumberInstance(Locale.ENGLISH);
            df.applyPattern("#.00");
           // DecimalFormat df = new DecimalFormat("#.00");
            String payAmount = df.format(rechargeAmount + gstValAmt);
            paymentAmount = NumberUtils.safeParseDouble(payAmount);
            actualraters = rechargeAmount;
        }
        totalDollar = paymentAmount / dollorRate;
        totalDollar = (int) (Math.round(totalDollar * 100)) / 100.0;// totalDollar;

        String dollorText = " ($" + totalDollar + ")";

        if (CUtils.getCountryCode(PaymentInformationActivity.this).equalsIgnoreCase(COUNTRY_CODE_IND)) {
            String gstLbl = (getResources().getString(R.string.gst).replace("#", String.valueOf((int) gst)));
            gstKeyTV.setText(gstLbl);
            totalPayableAmountValTV.setText(getResources().getString(R.string.rs_sign) + paymentAmount);
            pilLlpayinindianrupee.setVisibility(View.GONE);
        } else {
            boolean showInternationalPayInINR = CUtils.getBooleanData(this, SHOW_INTERNATIONAL_PAY_IN_INR, false);
            if (showInternationalPayInINR) {
                pilLlpayinindianrupee.setVisibility(View.VISIBLE);
            } else {
                pilLlpayinindianrupee.setVisibility(View.GONE);
            }
            gstKeyTV.setText(R.string.international_charges);

            if (actualraters == 1.0) {
                totalPayableAmountValTV.setText(getResources().getString(R.string.rs_sign) + paymentAmount);
                pilLlpayinindianrupee.setVisibility(View.GONE);
            } else {
                totalPayableAmountValTV.setText(getResources().getString(R.string.rs_sign) + (paymentAmount) + dollorText);
            }
        }

        coupanCodeEt.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

                if (coupanCodeEt.getText().toString().trim().length() == 0) {
                    hideCouponTxt();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivBack:
                getOnBackPressedDispatcher().onBackPressed();
                break;
            case R.id.pay_now_btn:
                String defaultOrderPaymentMethod = CUtils.getStringData(PaymentInformationActivity.this, com.ojassoft.astrosage.utils.CGlobalVariables.DEFAULT_ORDER_PAYMENT_METHOD, CGlobalVariables.RAZORPAY);
                if (defaultOrderPaymentMethod.equals(CGlobalVariables.PAYTM)) {
                    paymentModeStr = CGlobalVariables.PAYTM;
                } else {
                    paymentModeStr = CGlobalVariables.RAZORPAY;
                }
                getOrderId();
                break;
            case R.id.pay_now_with_paytm_btn:
                paymentModeStr = CGlobalVariables.PAYTM;
                getOrderId();
                break;
/*            case R.id.pay_now_with_paypal_btn:
                paymentModeStr = CGlobalVariables.PAYPAL;
                getOrderId();
                break;*/
            case R.id.pay_now_with_ccavenue_btn: {
                paymentModeStr = CGlobalVariables.CCAVENUE;
                getOrderId();
                break;
            }
            case R.id.apply_coupan_container:
                CoupanCodeDialog dialog = new CoupanCodeDialog();
                dialog.show(getSupportFragmentManager(), "PaymentSucessfulDialog");
                break;
            case R.id.apply_code_btn:
                applyCouponCode();
                break;
            case R.id.coupon_remove:
                removeCouponCode();
                break;
            case R.id.payInRsCheckBox: {
                break;
            }
        }
    }

    private void removeCouponCode() {
        this.coupanCode = "";
        coupanCodeEt.setText("");
        coupanCodeEt.setEnabled(true);
        applyCodeBtn.setVisibility(View.VISIBLE);
        couponRemove.setVisibility(View.GONE);
        coupanTxtSuccess.setVisibility(View.GONE);
        setBonusView("0", false);
    }

    private void hideCouponTxt() {
        this.coupanCode = "";
        coupanTxtSuccess.setVisibility(View.GONE);
    }

    private void applyCouponCode() {

        String couponCode = coupanCodeEt.getText().toString().trim();

        if (!TextUtils.isEmpty(couponCode)) {
            CUtils.fcmAnalyticsEvents(this.coupanCode, CGlobalVariables.COUPON_CLICK, "");
            applyCoupanCode(couponCode);
        } else {
            CUtils.showSnackbar(containerLayout, getResources().getString(R.string.enter_coupon_code), PaymentInformationActivity.this);
        }

    }


    private void startPayment(String order_Id, String razorpayOrderId) {
        //Toast.makeText(this,"startPayment() = "+order_Id, Toast.LENGTH_SHORT).show();
        final Checkout co = new Checkout();
        co.setFullScreenDisable(true);
        String phoneNo = com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(this) + com.ojassoft.astrosage.varta.utils.CUtils.getUserID(this);
        String astrosageId = com.ojassoft.astrosage.utils.CUtils.getUserName(this);
        String email = "";
        if (astrosageId.contains("@")) {
            email = astrosageId;
        }
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
            if(!TextUtils.isEmpty(email)){
                preFill.put("email", email);
            }
            if(selectedPayMode.equals(CGlobalVariables.CREDIT_DEBIT_CARD_RAZORPAY)){
                preFill.put("method", "card");
            }
            if(selectedPayMode.equals(CGlobalVariables.NET_BANKING_RAZORPAY)){
                preFill.put("method", "netbanking");
            }
            if(selectedPayMode.equals(CGlobalVariables.WALLET_RAZORPAY)){
                preFill.put("method", "wallet");
            }
             preFill.put("contact", phoneNo);
            options.put("prefill", preFill);


            JSONObject notes = new JSONObject();
            //added by Ankit on 3-7-2019 for Razorpay Webhook
            notes.put("orderId", order_Id);
            notes.put("chatId", "");
            notes.put("orderType", "service");
            notes.put("appVersion", BuildConfig.VERSION_NAME);
            notes.put("appName", BuildConfig.APPLICATION_ID);
            notes.put("orderFromDomain", "varta.astrosage.com");
            notes.put("firebaseinstanceid", CUtils.getFirebaseAnalyticsAppInstanceId(this));
            notes.put("facebookinstanceid", CUtils.getFacebookAnalyticsAppInstanceId(this));
            notes.put("isfirstpurchase", CUtils.getFirstTimePurchaseValue(this));
            options.put("notes", notes);


            co.open(this, options);
            //Log.e("LoadMore razorpay ", options.toString());
        } catch (Exception e) {
            CUtils.showSnackbar(containerLayout, "Error in payment: " + e.getMessage(), PaymentInformationActivity.this);
            e.printStackTrace();
        }
    }


    @Override
    public void onPaymentSuccess(String razorpayPaymentID, PaymentData paymentData) {//sucess payment
        //Log.e("RazorPayPayment","onPaymentSuccess razorpayPaymentID="+razorpayPaymentID+" getPaymentId="+paymentData.getPaymentId() +" getData="+paymentData.getData());
        if (razorpayPaymentID == null) {
            razorpayPaymentID = "";
        }
        sendDataToServer("1", razorpayPaymentID,false);
    }
    //boolean isPaymentNetworkFailed = false;
    @Override
    public void onPaymentError(int code, String response, PaymentData paymentData) {//failed payment
        //Log.e("RazorPayPayment","onPaymentError code="+code+" response="+response +" getData="+paymentData.getData());

        //if(code == Checkout.PAYMENT_CANCELED) {

            com.ojassoft.astrosage.utils.CUtils.saveBooleanData(this, com.ojassoft.astrosage.utils.CGlobalVariables.IS_SUBSCRIPTION_ERROR, true);
            sendDataToServer("0", "",false);
        //}

//        else {
//            isPaymentNetworkFailed = true;
//            udatePaymentStatusOnserveronFailed("0", orderId, String.valueOf(rechargeAmount), paymentModeStr);
//                PaymentFailDialog dialog = new PaymentFailDialog();
//                dialog.setTryAgainCallback(()->{
//                    paymentModeStr = CGlobalVariables.PAYTM;
//                    getOrderId();
//                    CUtils.fcmAnalyticsEvents(CGlobalVariables.PAYMENT_FAILED_REDIRECT_TO_PAYTM,
//                            CGlobalVariables.FIREBASE_EVENT_PAYMENT_FAILED, "");
//
//                    CUtils.addFacebookEventPaymentFailed( CGlobalVariables.PAYMENT_FAILED_REDIRECT_TO_PAYTM, actualraters, CGlobalVariables.CURRENCY_INDIA, "");
//
//                });
//                dialog.show(getSupportFragmentManager(), "PaymentFailDialog");
//            CUtils.fcmAnalyticsEvents(CGlobalVariables.PAYMENT_FAILED_RAZORPAY,
//                    CGlobalVariables.FIREBASE_EVENT_PAYMENT_FAILED, "");
//
//            CUtils.addFacebookEventPaymentFailed( CGlobalVariables.PAYMENT_FAILED_RAZORPAY, actualraters, CGlobalVariables.CURRENCY_INDIA, "");
//
//
//        }
    }

    private void openCallingActivity(String orderStatus, Class activity, String razorpayid) {
        Bundle bundle = new Bundle();
        Intent intent = new Intent(PaymentInformationActivity.this, activity);
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
        Intent intent = new Intent(PaymentInformationActivity.this, activity);
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
        setResult(CGlobalVariables.REQUEST_FOR_QUICK_RECHARGE, intent);
    }

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

    private void hideProgressBar() {
        try {
            if (pd != null && pd.isShowing())
                pd.dismiss();
        } catch (Exception e) {
        }

    }

    @Override
    public void onResponse(String response, int method) {
        //Toast.makeText(this, "response==>"+response, Toast.LENGTH_SHORT).show();

        //Log.e("SAN PFA response ", response);
        try {
            response = new String(response.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
        } catch (Exception e) {
        }
        //        Log.e("profile_data", " : "+VolleyServiceHandler.isUTF8Format(response));
        if (method == 2) {
            hideProgressBar();
            try {
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.getString("status");
                String msg = jsonObject.getString("msg");
                if (status.equals("1")) {
                    PaymentSucessfulDialog dialog = new PaymentSucessfulDialog("" + actualraters);
                    dialog.show(getSupportFragmentManager(), "PaymentSucessfulDialog");
                    CUtils.saveStringData(PaymentInformationActivity.this, com.ojassoft.astrosage.utils.CGlobalVariables.RECHARGE_SERVICE_OFFER_RESPONSE_KEY, "");
                } else {
                    showFailedPaymentDialog();
                }


            } catch (Exception e) {
                //Log.e(("Exception>>", e.getMessage());
            }
        } else if (method == 3) {
            //{"status":"1", "msg":"Get INR 20 extra on recharges"}
            try {
                //Log.e("SAN Coupon Res => ",  response );
                JSONObject jsonObject = new JSONObject(response);
                if (jsonObject.has("status")) {
                    String status = jsonObject.getString("status");
                    if (status.equals("1")) {
                        setBonusView(jsonObject.getString("offeramount"), true);
                        applyCodeBtn.setVisibility(View.GONE);
                        couponRemove.setVisibility(View.VISIBLE);
                        coupanTxtSuccess.setVisibility(View.VISIBLE);
                        coupanTxtSuccess.setText(getResources().getString(R.string.coupon_applied));
                        coupanTxtSuccess.setTextColor(getResources().getColor(R.color.green_color));
                        coupanCodeEt.setEnabled(false);

                        CUtils.fcmAnalyticsEvents(this.coupanCode, CGlobalVariables.COUPON_APPLY, "");

                    } else if (status.equals("100")) {
                        this.coupanCode = "";
                        LocalBroadcastManager.getInstance(this).registerReceiver(mReceiverBackgroundLoginService
                                , new IntentFilter(CGlobalVariables.SEND_BROADCAST_BACK_GR_LOGIN));

                        startBackgroundLoginService();

                    } else if (status.equals("0")) {

                        String tmpCpnCd = this.coupanCode;
                        this.coupanCode = "";
                        coupanTxtSuccess.setVisibility(View.VISIBLE);
                        coupanTxtSuccess.setText(jsonObject.getString("msg"));
                        coupanTxtSuccess.setTextColor(getResources().getColor(R.color.red));
                        CUtils.fcmAnalyticsEvents(tmpCpnCd, CGlobalVariables.COUPON_INVALID, "");

                    } else {
                        this.coupanCode = "";
                    }

                    if (!status.equals("100")) {
                        hideProgressBar();
                        String msg = jsonObject.getString("msg");
//                        String msg = new String(jsonObject.getString("msg").getBytes("ISO-8859-1"), "UTF-8");

                        CUtils.showSnackbar(containerLayout, msg, this);
                    }

                }
            } catch (Exception e) {
                hideProgressBar();
                this.coupanCode = "";
            }
        } else if (method == 4) {
            //{"status":"1", "msg":"Get INR 20 extra on recharges"}
            hideProgressBar();
            try {
                JSONObject object = new JSONObject(response);
                String walletBal = object.getString("userbalancce");
                if (walletBal != null && walletBal.length() > 0) {
                    CUtils.setWalletRs(PaymentInformationActivity.this, walletBal);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onError(VolleyError error) {
        //Toast.makeText(this,"onError= "+error, Toast.LENGTH_SHORT).show();
        //PaymentFailDialog dialog = new PaymentFailDialog();
        //dialog.show(getSupportFragmentManager(), "PaymentFailDialog");
        hideProgressBar();
        try {
            CUtils.showSnackbar(containerLayout, getResources().getString(R.string.something_wrong_error), PaymentInformationActivity.this);
        } catch (Exception e) {
            //
        }
    }


    private void startBackgroundLoginService() {
        try {
            if (CUtils.getUserLoginStatus(this)) {
                CUtils.fcmAnalyticsEvents("bg_login_from_payment_info", CGlobalVariables.VARTA_BACKGROUND_LOGIN, "");

                Intent intent = new Intent(PaymentInformationActivity.this, Loginservice.class);
                startService(intent);
            }
        } catch (Exception e) {
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mReceiverBackgroundLoginService != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiverBackgroundLoginService);
        }

    }


    public void applyCoupanCode(String coupanCodeL) {
        // CUtils.showSnackbar(containerLayout, "Invalid coupan code", this);
        coupanCode = coupanCodeL;
        if (!CUtils.isConnectedWithInternet(PaymentInformationActivity.this)) {
            CUtils.showSnackbar(containerLayout, getResources().getString(R.string.no_internet), PaymentInformationActivity.this);
        } else {

            if (pd == null)
                pd = new CustomProgressDialog(PaymentInformationActivity.this);
            pd.show();
            pd.setCancelable(false);
//            String url = CGlobalVariables.VALIDATE_COUPAN_CODE;
            //Log.e(("SAN Coupon URL => ",  url );
//            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, url,
//                    PaymentInformationActivity.this, false, getParamsForCoupanApplied(coupanCode), 3).getMyStringRequest();
//            stringRequest.setShouldCache(false);
//            queue.add(stringRequest);
            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.validateCouponCode(getParamsForCoupanApplied(coupanCode));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    hideProgressBar();
                    try {
                        //Log.e("SAN Coupon Res => ",  response );
                        String myResponse = response.body().string();
                        JSONObject jsonObject = new JSONObject(myResponse);
                        if (jsonObject.has("status")) {
                            String status = jsonObject.getString("status");
                            if (status.equals("1")) {
                                setBonusView(jsonObject.getString("offeramount"), true);
                                applyCodeBtn.setVisibility(View.GONE);
                                couponRemove.setVisibility(View.VISIBLE);
                                coupanTxtSuccess.setVisibility(View.VISIBLE);
                                coupanTxtSuccess.setText(getResources().getString(R.string.coupon_applied));
                                coupanTxtSuccess.setTextColor(getResources().getColor(R.color.green_color));
                                coupanCodeEt.setEnabled(false);

                                CUtils.fcmAnalyticsEvents(coupanCode, CGlobalVariables.COUPON_APPLY, "");

                            } else if (status.equals("100")) {
                                coupanCode = "";
                                LocalBroadcastManager.getInstance(PaymentInformationActivity.this).registerReceiver(mReceiverBackgroundLoginService
                                        , new IntentFilter(CGlobalVariables.SEND_BROADCAST_BACK_GR_LOGIN));

                                startBackgroundLoginService();

                            } else if (status.equals("0")) {

                                String tmpCpnCd = coupanCode;
                                coupanCode = "";
                                coupanTxtSuccess.setVisibility(View.VISIBLE);
                                coupanTxtSuccess.setText(jsonObject.getString("msg"));
                                coupanTxtSuccess.setTextColor(getResources().getColor(R.color.red));
                                CUtils.fcmAnalyticsEvents(tmpCpnCd, CGlobalVariables.COUPON_INVALID, "");

                            } else {
                                coupanCode = "";
                            }

                            if (!status.equals("100")) {
                                hideProgressBar();
                                String msg = jsonObject.getString("msg");
//                        String msg = new String(jsonObject.getString("msg").getBytes("ISO-8859-1"), "UTF-8");

                                CUtils.showSnackbar(containerLayout, msg, PaymentInformationActivity.this);
                            }

                        }
                    } catch (Exception e) {
                        hideProgressBar();
                        coupanCode = "";
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    hideProgressBar(pd);
                    CUtils.showSnackbar(containerLayout, t.getMessage(), PaymentInformationActivity.this);
                    showFailedPaymentDialog();
                }
            });


        }
    }

    private Map<String, String> getParamsForCoupanApplied(String coupanCode) {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("couponCode", coupanCode);
        if (selectedPosition == -1) { //in case of user input recharge amount
            headers.put("serviceId", CGlobalVariables.SERVICE_ID_VARIABLE_AMOUNT);
        } else {
            headers.put("serviceId", services.getServiceid());
        }
        headers.put("countryCode", CUtils.getCountryCode(PaymentInformationActivity.this));
        headers.put("userPh", CUtils.getUserID(PaymentInformationActivity.this));
        headers.put("key", CUtils.getApplicationSignatureHashCode(PaymentInformationActivity.this));
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
        //Log.e(("SAN Coupon URL => ",  headers.toString() );
        return CUtils.setRequiredParams(headers);
    }

    public void getWalletPriceData() {

        if (!CUtils.isConnectedWithInternet(PaymentInformationActivity.this)) {
            //CUtils.showSnackbar(containerLayout, getResources().getString(R.string.no_internet), DashBoardActivity.this);
        } else {
            if (pd == null)
                pd = new CustomProgressDialog(PaymentInformationActivity.this);
            pd.show();
            pd.setCancelable(false);
//            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.GET_WALLET_PRICE_URL,
//                    PaymentInformationActivity.this, false, getParamsNew(), 4).getMyStringRequest();
//            queue.add(stringRequest);
            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.getWalletBalance(getParamsNew());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    //{"status":"1", "msg":"Get INR 20 extra on recharges"}
                    hideProgressBar();
                    try {
                        String myResponse = response.body().string();
                        JSONObject object = new JSONObject(myResponse);
                        String walletBal = object.getString("userbalancce");
                        if (walletBal != null && walletBal.length() > 0) {
                            CUtils.setWalletRs(PaymentInformationActivity.this, walletBal);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    hideProgressBar();
                }
            });
        }
    }

    public Map<String, String> getParamsNew() {

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(PaymentInformationActivity.this));
        headers.put(CGlobalVariables.USER_PHONE_NO, CUtils.getUserID(PaymentInformationActivity.this));
        headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(PaymentInformationActivity.this));
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
        headers.put(CGlobalVariables.DEVICE_ID, CUtils.getMyAndroidId(PaymentInformationActivity.this));
        headers.put(CGlobalVariables.APP_VERSION, BuildConfig.VERSION_NAME);
        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(PaymentInformationActivity.this));
        return headers;
    }

    @Override
    public void getCallBack(String result, CUtils.callBack callback, String priceInDollor, String priceInRs) {

        if (callback == CUtils.callBack.GET_CHECKSUM) {
            String checksum = result;
            if (!result.isEmpty()) {
                startPaytmPayment(orderId, amountToPay, checksum);
            } else {
                CUtils.showSnackbar(containerLayout, getResources().getString(R.string.order_fail), PaymentInformationActivity.this);
            }
        }
    }

    @Override
    public void getCallBackForChat(String[] result, CUtils.callBack callback, String priceInDollor, String priceInRs) {

    }

    public void sendDataToServer(String status, String razorpayid,boolean isEcommerceEventAdded ) {
        android.util.Log.d("StripePaymentActivity", "status: " + status);

        try {
            if (status.equals("0")) {
                //CUtils.saveWalletRechargeData("");
                if (paymentModeStr.equalsIgnoreCase(CGlobalVariables.PAYTM)) {
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.PAYMENT_FAILED_PAYTM,
                            CGlobalVariables.FIREBASE_EVENT_PAYMENT_FAILED, "");

                    CUtils.addFacebookEventPaymentFailed( CGlobalVariables.PAYMENT_FAILED_PAYTM, actualraters, CGlobalVariables.CURRENCY_INDIA, "");

                } else if (paymentModeStr.equalsIgnoreCase(CGlobalVariables.CCAVENUE)) {
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.PAYMENT_FAILED_CCAVENUE,
                            CGlobalVariables.FIREBASE_EVENT_PAYMENT_FAILED, "");

                    CUtils.addFacebookEventPaymentFailed( CGlobalVariables.PAYMENT_FAILED_CCAVENUE, actualraters, CGlobalVariables.CURRENCY_INDIA, "");

                } else if(paymentModeStr.equalsIgnoreCase(CGlobalVariables.RAZORPAY)){
                    if (selectedPayMode.equals(CGlobalVariables.RAZORPAY_UPI)) {
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.PAYMENT_FAILED_RAZORPAY_UPI,
                                CGlobalVariables.FIREBASE_EVENT_PAYMENT_FAILED, "");

                        CUtils.addFacebookEventPaymentFailed( CGlobalVariables.PAYMENT_FAILED_RAZORPAY_UPI, actualraters, CGlobalVariables.CURRENCY_INDIA, "");

                    } else if(selectedPayMode.equals(CGlobalVariables.CREDIT_DEBIT_CARD_RAZORPAY)) {
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.PAYMENT_FAILED_RAZORPAY_CARDS,
                                CGlobalVariables.FIREBASE_EVENT_PAYMENT_FAILED, "");

                        CUtils.addFacebookEventPaymentFailed( CGlobalVariables.PAYMENT_FAILED_RAZORPAY_CARDS, actualraters, CGlobalVariables.CURRENCY_INDIA, "");

                    }else {
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.PAYMENT_FAILED_RAZORPAY,
                                CGlobalVariables.FIREBASE_EVENT_PAYMENT_FAILED, "");

                        CUtils.addFacebookEventPaymentFailed( CGlobalVariables.PAYMENT_FAILED_RAZORPAY, actualraters, CGlobalVariables.CURRENCY_INDIA, "");

                    }
                } else if (paymentModeStr.equalsIgnoreCase(CGlobalVariables.UPI_PAYMENTS_PHONE)) {
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.PAYMENT_FAILED_UPI_PHONEPE,
                            CGlobalVariables.FIREBASE_EVENT_PAYMENT_FAILED, "");

                    CUtils.addFacebookEventPaymentFailed( CGlobalVariables.PAYMENT_FAILED_UPI_PHONEPE, actualraters, CGlobalVariables.CURRENCY_INDIA, "");

                }else if (paymentModeStr.equalsIgnoreCase(CGlobalVariables.UPI_PAYMENTS_GPAY)) {
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.PAYMENT_FAILED_UPI_GPAY,
                            CGlobalVariables.FIREBASE_EVENT_PAYMENT_FAILED, "");

                    CUtils.addFacebookEventPaymentFailed( CGlobalVariables.PAYMENT_FAILED_UPI_GPAY, actualraters, CGlobalVariables.CURRENCY_INDIA, "");

                }else if (paymentModeStr.equalsIgnoreCase(CGlobalVariables.UPI_PAYMENTS_PAYTM)) {
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.PAYMENT_FAILED_UPI_PAYTM,
                            CGlobalVariables.FIREBASE_EVENT_PAYMENT_FAILED, "");

                    CUtils.addFacebookEventPaymentFailed( CGlobalVariables.PAYMENT_FAILED_UPI_PAYTM, actualraters, CGlobalVariables.CURRENCY_INDIA, "");

                }else if (paymentModeStr.equalsIgnoreCase(CGlobalVariables.UPI_PAYMENTS_CRED)) {
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.PAYMENT_FAILED_UPI_CRED,
                            CGlobalVariables.FIREBASE_EVENT_PAYMENT_FAILED, "");

                    CUtils.addFacebookEventPaymentFailed( CGlobalVariables.PAYMENT_FAILED_UPI_CRED, actualraters, CGlobalVariables.CURRENCY_INDIA, "");

                }else if (paymentModeStr.equalsIgnoreCase(CGlobalVariables.UPI_PAYMENTS_BHIM)) {
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.PAYMENT_FAILED_UPI_BHIM,
                            CGlobalVariables.FIREBASE_EVENT_PAYMENT_FAILED, "");

                    CUtils.addFacebookEventPaymentFailed( CGlobalVariables.PAYMENT_FAILED_UPI_BHIM, actualraters, CGlobalVariables.CURRENCY_INDIA, "");

                }else if (paymentModeStr.equalsIgnoreCase(CGlobalVariables.STRIPE)) {
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.PAYMENT_FAILED_STRIPE,
                            CGlobalVariables.FIREBASE_EVENT_PAYMENT_FAILED, "");

                    CUtils.addFacebookEventPaymentFailed( CGlobalVariables.PAYMENT_FAILED_STRIPE, actualraters, CGlobalVariables.CURRENCY_USD, "");
                }
                else if (paymentModeStr.equalsIgnoreCase(CGlobalVariables.PAYMENTS_PHONEPE_CHECKOUT)) {
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.PAYMENT_FAILED_PHONEPE_CHECKOUT,
                            CGlobalVariables.FIREBASE_EVENT_PAYMENT_FAILED, "");

                    CUtils.addFacebookEventPaymentFailed( CGlobalVariables.PAYMENT_FAILED_PHONEPE_CHECKOUT, actualraters, CGlobalVariables.CURRENCY_INDIA, "");
                }
                udatePaymentStatusOnserveronFailed("0", orderId, String.valueOf(rechargeAmount), paymentModeStr);
                return;
            }
            //isEcommerceEventAdded -> this variable is used to check if the e-commerce purchase event is already added or not.
            //If the payment is successful and the e-commerce purchase event is already added, then don't add it again.
            //If the payment is failed and the e-commerce purchase event is already added, then don't add it again.
            else if (status.equals("1") && !isEcommerceEventAdded) {
               setEcommercePurchaseEvent();
            }
            if (callingActivity == null) {
                callingActivity = "";
            }
            if (callingActivity.equals("TopupRechargeDialog")) {
                udatePaymentStatusOnserver(this, containerLayout, status, orderId, String.valueOf(rechargeAmount), queue, paymentModeStr, razorpayid);
            } else {
                if (callingActivity.equals("AstrologerDescriptionActivity")) {
                    openCallingActivity(status, AstrologerDescriptionActivity.class, razorpayid);
                } else if (callingActivity.equals("ChatWindowActivity")) {
                    openCallingActivity(status, ChatWindowActivity.class, razorpayid);
                } else if (callingActivity.equals("AIChatWindowActivity")) {
                    openCallingActivity(status, AIChatWindowActivity.class, razorpayid);
                } else if (callingActivity.equals("QuickRechargeBottomSheet")) {
                    openCallingActivity(status, razorpayid, ChatWindowActivity.class);
                } else if (callingActivity.equals("AIQuickRechargeBottomSheet")) {
                    openCallingActivity(status, razorpayid, AIChatWindowActivity.class);
                }  else if (callingActivity.equals(CGlobalVariables.AIVOICECALLINGACTIVITY)) {
                    openCallingActivity(status, AIVoiceCallingActivity.class, razorpayid);
                } else if (callingActivity.equals("DashBoardActivity")) {
                    openCallingActivity(status, DashBoardActivity.class, razorpayid);
                }else {
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
            com.ojassoft.astrosage.utils.CUtils.saveBooleanData(this, com.ojassoft.astrosage.utils.CGlobalVariables.IS_PURCHASE, true);
            if (actualraters > 0) {
                if (paymentModeStr.equalsIgnoreCase(CGlobalVariables.PAYTM)) {
                    CUtils.fcmAnalyticsEcommerceEvents(this, CGlobalVariables.PAYMENT_SUCCESS_PAYTM,
                            FirebaseAnalytics.Event.PURCHASE, actualraters, CGlobalVariables.CURRENCY_INDIA, orderId,"");
                } else if (paymentModeStr.equalsIgnoreCase(CGlobalVariables.CCAVENUE)) {
                    CUtils.fcmAnalyticsEcommerceEvents(this, CGlobalVariables.PAYMENT_SUCCESS_CCAVENUE,
                            FirebaseAnalytics.Event.PURCHASE, actualraters,
                            CGlobalVariables.CURRENCY_INDIA, orderId,"");
                }
                else if (paymentModeStr.equalsIgnoreCase(CGlobalVariables.RAZORPAY)) {
                    if (selectedPayMode.equals(CGlobalVariables.RAZORPAY_UPI)) {
                        CUtils.fcmAnalyticsEcommerceEvents(this, CGlobalVariables.PAYMENT_SUCCESS_RAZORPAY_UPI,
                                FirebaseAnalytics.Event.PURCHASE, actualraters, CGlobalVariables.CURRENCY_INDIA, orderId,"");
                    } else if (selectedPayMode.equals(CGlobalVariables.CREDIT_DEBIT_CARD_RAZORPAY)) {
                        CUtils.fcmAnalyticsEcommerceEvents(this, CGlobalVariables.PAYMENT_SUCCESS_RAZORPAY_CARDS,
                                FirebaseAnalytics.Event.PURCHASE, actualraters, CGlobalVariables.CURRENCY_INDIA, orderId,"");
                    } else {
                        CUtils.fcmAnalyticsEcommerceEvents(this, CGlobalVariables.PAYMENT_SUCCESS_RAZORPAY,
                                FirebaseAnalytics.Event.PURCHASE, actualraters, CGlobalVariables.CURRENCY_INDIA, orderId,"");

                    }

                } else if (paymentModeStr.equalsIgnoreCase(CGlobalVariables.UPI_PAYMENTS_PHONE)) {
                    CUtils.fcmAnalyticsEcommerceEvents(this, CGlobalVariables.PAYMENT_SUCCESS_UPI_PHONEPE,
                            FirebaseAnalytics.Event.PURCHASE, actualraters,
                            CGlobalVariables.CURRENCY_INDIA, orderId,"");
                }
                else if (paymentModeStr.equalsIgnoreCase(CGlobalVariables.UPI_PAYMENTS_GPAY)) {
                    CUtils.fcmAnalyticsEcommerceEvents(this, CGlobalVariables.PAYMENT_SUCCESS_UPI_GPAY,
                            FirebaseAnalytics.Event.PURCHASE, actualraters,
                            CGlobalVariables.CURRENCY_INDIA, orderId,"");
                }
                else if (paymentModeStr.equalsIgnoreCase(CGlobalVariables.UPI_PAYMENTS_PAYTM)) {
                    CUtils.fcmAnalyticsEcommerceEvents(this, CGlobalVariables.PAYMENT_SUCCESS_UPI_PAYTM,
                            FirebaseAnalytics.Event.PURCHASE, actualraters,
                            CGlobalVariables.CURRENCY_INDIA, orderId,"");
                }
                else if (paymentModeStr.equalsIgnoreCase(CGlobalVariables.UPI_PAYMENTS_BHIM)) {
                    CUtils.fcmAnalyticsEcommerceEvents(this, CGlobalVariables.PAYMENT_SUCCESS_UPI_BHIM,
                            FirebaseAnalytics.Event.PURCHASE, actualraters,
                            CGlobalVariables.CURRENCY_INDIA, orderId,"");
                }
                else if (paymentModeStr.equalsIgnoreCase(CGlobalVariables.UPI_PAYMENTS_CRED)) {
                    CUtils.fcmAnalyticsEcommerceEvents(this, CGlobalVariables.PAYMENT_SUCCESS_UPI_CRED,
                            FirebaseAnalytics.Event.PURCHASE, actualraters,
                            CGlobalVariables.CURRENCY_INDIA, orderId,"");
                }
                else if (paymentModeStr.equalsIgnoreCase(CGlobalVariables.STRIPE)) {
                    CUtils.fcmAnalyticsEcommerceEvents(this, CGlobalVariables.PAYMENT_SUCCESS_STRIPE,
                            FirebaseAnalytics.Event.PURCHASE, actualraters,
                            currency, orderId,"");
                }
                else if (paymentModeStr.equalsIgnoreCase(CGlobalVariables.PAYMENTS_PHONEPE_CHECKOUT)) {
                    CUtils.fcmAnalyticsEcommerceEvents(this, CGlobalVariables.PAYMENT_SUCCESS_PHONEPE_CHECKOUT,
                            FirebaseAnalytics.Event.PURCHASE, actualraters,
                            CGlobalVariables.CURRENCY_INDIA, orderId,"");
                }

                if (services != null) { //in case of service amount payment add purchase event service wise)
                    int serviceAmt = (int) actualraters;
                    if (paymentModeStr.equalsIgnoreCase(CGlobalVariables.PAYTM)) {
                        CUtils.ecommercePurchaseEventsServiceWise(this,CGlobalVariables.PAYMENT_SUCCESS_PAYTM,
                                FirebaseAnalytics.Event.PURCHASE + "_" + serviceAmt, actualraters, CGlobalVariables.CURRENCY_INDIA, orderId,"");
                    } else if (paymentModeStr.equalsIgnoreCase(CGlobalVariables.CCAVENUE)) {
                        CUtils.ecommercePurchaseEventsServiceWise(this,CGlobalVariables.PAYMENT_SUCCESS_CCAVENUE,
                                FirebaseAnalytics.Event.PURCHASE + "_" + serviceAmt, actualraters,
                                CGlobalVariables.CURRENCY_INDIA, orderId,"");
                    } else if (paymentModeStr.equalsIgnoreCase(CGlobalVariables.RAZORPAY)) {
                        if (selectedPayMode.equals(CGlobalVariables.RAZORPAY_UPI)) {
                            CUtils.ecommercePurchaseEventsServiceWise(this,CGlobalVariables.PAYMENT_SUCCESS_RAZORPAY_UPI,
                                    FirebaseAnalytics.Event.PURCHASE + "_" + serviceAmt, actualraters, CGlobalVariables.CURRENCY_INDIA, orderId,"");
                        } else if (selectedPayMode.equals(CGlobalVariables.CREDIT_DEBIT_CARD_RAZORPAY)) {
                            CUtils.ecommercePurchaseEventsServiceWise(this,CGlobalVariables.PAYMENT_SUCCESS_RAZORPAY_CARDS,
                                    FirebaseAnalytics.Event.PURCHASE + "_" + serviceAmt, actualraters, CGlobalVariables.CURRENCY_INDIA, orderId,"");
                        } else {
                            CUtils.ecommercePurchaseEventsServiceWise(this,CGlobalVariables.PAYMENT_SUCCESS_RAZORPAY,
                                    FirebaseAnalytics.Event.PURCHASE + "_" + serviceAmt, actualraters, CGlobalVariables.CURRENCY_INDIA, orderId,"");
                        }

                    } else if (paymentModeStr.equalsIgnoreCase(CGlobalVariables.UPI_PAYMENTS_PHONE)) {
                        CUtils.ecommercePurchaseEventsServiceWise(this,CGlobalVariables.PAYMENT_SUCCESS_UPI_PHONEPE,
                                FirebaseAnalytics.Event.PURCHASE + "_" + serviceAmt, actualraters, CGlobalVariables.CURRENCY_INDIA, orderId,"");
                    } else if (paymentModeStr.equalsIgnoreCase(CGlobalVariables.UPI_PAYMENTS_GPAY)) {
                        CUtils.ecommercePurchaseEventsServiceWise(this,CGlobalVariables.PAYMENT_SUCCESS_UPI_GPAY,
                                FirebaseAnalytics.Event.PURCHASE + "_" + serviceAmt, actualraters, CGlobalVariables.CURRENCY_INDIA, orderId,"");
                    } else if (paymentModeStr.equalsIgnoreCase(CGlobalVariables.UPI_PAYMENTS_PAYTM)) {
                        CUtils.ecommercePurchaseEventsServiceWise(this,CGlobalVariables.PAYMENT_SUCCESS_UPI_PAYTM,
                                FirebaseAnalytics.Event.PURCHASE + "_" + serviceAmt, actualraters, CGlobalVariables.CURRENCY_INDIA, orderId,"");
                    }else if (paymentModeStr.equalsIgnoreCase(CGlobalVariables.UPI_PAYMENTS_CRED)) {
                        CUtils.ecommercePurchaseEventsServiceWise(this,CGlobalVariables.PAYMENT_SUCCESS_UPI_CRED,
                                FirebaseAnalytics.Event.PURCHASE + "_" + serviceAmt, actualraters, CGlobalVariables.CURRENCY_INDIA, orderId,"");
                    }else if (paymentModeStr.equalsIgnoreCase(CGlobalVariables.UPI_PAYMENTS_BHIM)) {
                        CUtils.ecommercePurchaseEventsServiceWise(this,CGlobalVariables.PAYMENT_SUCCESS_UPI_BHIM,
                                FirebaseAnalytics.Event.PURCHASE + "_" + serviceAmt, actualraters, CGlobalVariables.CURRENCY_INDIA, orderId,"");
                    }else if (paymentModeStr.equalsIgnoreCase(CGlobalVariables.STRIPE)) {
                        CUtils.ecommercePurchaseEventsServiceWise(this,CGlobalVariables.PAYMENT_SUCCESS_STRIPE,
                                FirebaseAnalytics.Event.PURCHASE + "_" + serviceAmt, actualraters, CGlobalVariables.CURRENCY_USD, orderId,"");
                    }else if (paymentModeStr.equalsIgnoreCase(CGlobalVariables.PAYMENTS_PHONEPE_CHECKOUT)) {
                        CUtils.ecommercePurchaseEventsServiceWise(this,CGlobalVariables.PAYMENT_SUCCESS_PHONEPE_CHECKOUT,
                                FirebaseAnalytics.Event.PURCHASE + "_" + serviceAmt, actualraters, CGlobalVariables.CURRENCY_INDIA, orderId,"");
                    }

                }
            }
        }catch (Exception e){
           // e.printStackTrace();
        }

    }



    public void udatePaymentStatusOnserveronFailed(final String orderStatus, final String orderID, final String amount, final String paymentMode) {
        String orderUrl = "";

        if (paymentMode.equalsIgnoreCase(CGlobalVariables.PAYTM)) {
            orderUrl = CGlobalVariables.UPDATE_STATUS_RAZOR_PAY;
        } else {
            orderUrl = CGlobalVariables.UPDATE_PAY_STATUS_ASKQUE;
        }
        if (pd == null)
            pd = new CustomProgressDialog(PaymentInformationActivity.this);
        pd.show();
        pd.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, orderUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideProgressBar(pd);
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
                               showFailedPaymentDialog();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressBar(pd);
                //callback.getCallBack("0", callBack.POST_RAZORPAYSTATUS, "", "");
                VolleyLog.d("VolleyError: " + error.getMessage());
                showFailedPaymentDialog();
            }
        }
        ) {
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
                headers.put("key", CUtils.getApplicationSignatureHashCode(PaymentInformationActivity.this));
                headers.put("amount", amount);
                headers.put("orderid", orderID);
                headers.put("paycurr", "INR");
                headers.put("status", orderStatus);
                headers.put("od", od);
                headers.put("phonepeid", phonepeid);
                headers.put("phonepeOrderId", phonepeOrderId);
                headers.put("profile_Id", CUtils.getUserID(PaymentInformationActivity.this));

                if (paymentMode.equalsIgnoreCase(CGlobalVariables.PAYTM)) {
                    headers.put("chatid", "");
                } else {
                    headers.put("paymentid", "");
                    headers.put("razorpayresponse", "0");
                }
                headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
                headers.put(CGlobalVariables.DEVICE_ID, CUtils.getMyAndroidId(PaymentInformationActivity.this));
                headers.put(CGlobalVariables.APP_VERSION, BuildConfig.VERSION_NAME);
                headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(PaymentInformationActivity.this));
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

    /**
     *
     */
    /**
     * This method is used to open alternate payment option when the payment fails.
     * If the default payment method is PayTM, it redirects to PayTM and vice versa.
     */
    private void openAlternatePaymentOption() {
        isRetryPayment = true;
        String defaultOrderPaymentMethod = CUtils.getStringData(PaymentInformationActivity.this, com.ojassoft.astrosage.utils.CGlobalVariables.DEFAULT_ORDER_PAYMENT_METHOD, CGlobalVariables.RAZORPAY);
        if (defaultOrderPaymentMethod.equals(CGlobalVariables.PAYTM)&&CUtils.getCountryCode(PaymentInformationActivity.this).equals(COUNTRY_CODE_IND)) {
            paymentModeStr = CGlobalVariables.PAYTM;
            CUtils.fcmAnalyticsEvents(CGlobalVariables.PAYMENT_FAILED_REDIRECT_TO_PAYTM,
                    CGlobalVariables.FIREBASE_EVENT_PAYMENT_FAILED, "");
            getOrderId();
        } else if (defaultOrderPaymentMethod.equals(CGlobalVariables.TAG_MANGER_PHONEPE_CHECKOUT)&& isPhonePeCheckoutVisible &&CUtils.getCountryCode(PaymentInformationActivity.this).equals(COUNTRY_CODE_IND)) {
            CUtils.fcmAnalyticsEvents(CGlobalVariables.PAYMENT_FAILED_REDIRECT_TO_PHONEPE_CHECKOUT,
                    CGlobalVariables.FIREBASE_EVENT_PAYMENT_FAILED, "");
            selectedPayMode = CGlobalVariables.PAYMENTS_PHONEPE_CHECKOUT;
            paymentModeStr = CGlobalVariables.PAYMENTS_PHONEPE_CHECKOUT;
            getApiCallForPhonePayIntentUrl(CGlobalVariables.PAYMENTS_PHONEPE_CHECKOUT);
        } else if(paymentModeStr.equals(CGlobalVariables.STRIPE)){
            CUtils.fcmAnalyticsEvents(CGlobalVariables.PAYMENT_FAILED_REDIRECT_TO_RAZORPAY,
                    CGlobalVariables.FIREBASE_EVENT_PAYMENT_FAILED, "");
            paymentModeStr = CGlobalVariables.RAZORPAY;
            getOrderId();
        }else if(paymentModeStr.equals(CGlobalVariables.RAZORPAY) && isStripeVisible && !CUtils.getCountryCode(PaymentInformationActivity.this).equals(COUNTRY_CODE_IND)){
            CUtils.fcmAnalyticsEvents(CGlobalVariables.PAYMENT_FAILED_REDIRECT_TO_STRIPE,
                    CGlobalVariables.FIREBASE_EVENT_PAYMENT_FAILED, "");
            paymentModeStr = CGlobalVariables.STRIPE;
            generateOrderForStripe();

        }else{
            if(paymentModeStr.equals(CGlobalVariables.RAZORPAY) && isPhonePeCheckoutVisible && CUtils.getCountryCode(PaymentInformationActivity.this).equals(COUNTRY_CODE_IND)){
                CUtils.fcmAnalyticsEvents(CGlobalVariables.PAYMENT_FAILED_REDIRECT_TO_PHONEPE_CHECKOUT,
                        CGlobalVariables.FIREBASE_EVENT_PAYMENT_FAILED, "");
                selectedPayMode = CGlobalVariables.PAYMENTS_PHONEPE_CHECKOUT;
                paymentModeStr = CGlobalVariables.PAYMENTS_PHONEPE_CHECKOUT;
                getApiCallForPhonePayIntentUrl(CGlobalVariables.PAYMENTS_PHONEPE_CHECKOUT);
            }else{
                CUtils.fcmAnalyticsEvents(CGlobalVariables.PAYMENT_FAILED_REDIRECT_TO_RAZORPAY,
                        CGlobalVariables.FIREBASE_EVENT_PAYMENT_FAILED, "");
                paymentModeStr = CGlobalVariables.RAZORPAY;
                getOrderId();
            }
        }
//
//
//
//        if (paymentModeStr.equalsIgnoreCase(CGlobalVariables.PAYTM)) {
//            paymentModeStr = CGlobalVariables.RAZORPAY;
//            getOrderId();
//            CUtils.addFacebookEventPaymentFailed(CGlobalVariables.PAYMENT_FAILED_REDIRECT_TO_RAZORPAY, actualraters, CGlobalVariables.CURRENCY_INDIA, "");
//
//        } else if (paymentModeStr.equalsIgnoreCase(CGlobalVariables.RAZORPAY)) {
//            paymentModeStr = CGlobalVariables.PAYTM;
//            getOrderId();
//            CUtils.fcmAnalyticsEvents(CGlobalVariables.PAYMENT_FAILED_REDIRECT_TO_PAYTM,
//                    CGlobalVariables.FIREBASE_EVENT_PAYMENT_FAILED, "");
//
//            CUtils.addFacebookEventPaymentFailed(CGlobalVariables.PAYMENT_FAILED_REDIRECT_TO_PAYTM, actualraters, CGlobalVariables.CURRENCY_INDIA, "");
//        } else if (paymentModeStr.equalsIgnoreCase(CGlobalVariables.UPI_PAYMENTS_PHONE)) {
//            paymentModeStr = CGlobalVariables.RAZORPAY;
//            getOrderId();
//            CUtils.addFacebookEventPaymentFailed(CGlobalVariables.PAYMENT_FAILED_REDIRECT_TO_RAZORPAY, actualraters, CGlobalVariables.CURRENCY_INDIA, "");
//        } else if (paymentModeStr.equalsIgnoreCase(CGlobalVariables.UPI_PAYMENTS_GPAY)) {
//            paymentModeStr = CGlobalVariables.RAZORPAY;
//            getOrderId();
//            CUtils.addFacebookEventPaymentFailed(CGlobalVariables.PAYMENT_FAILED_REDIRECT_TO_RAZORPAY, actualraters, CGlobalVariables.CURRENCY_INDIA, "");
//        } else if (paymentModeStr.equalsIgnoreCase(CGlobalVariables.UPI_PAYMENTS_PAYTM)) {
//            paymentModeStr = CGlobalVariables.RAZORPAY;
//            getOrderId();
//            CUtils.addFacebookEventPaymentFailed(CGlobalVariables.PAYMENT_FAILED_REDIRECT_TO_RAZORPAY, actualraters, CGlobalVariables.CURRENCY_INDIA, "");
//        }
    }



    private void hideProgressBar(CustomProgressDialog pd) {
        try {
            if (pd != null && pd.isShowing())
                pd.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*************************************** Paytm Transaction ************************************************************/

    Map<String, String> getchecksumparams(String order_Id) {
        String email = CUtils.getUserID(PaymentInformationActivity.this);
        Map<String, String> params = new HashMap<>();

        params.put("key", CUtils.getApplicationSignatureHashCode(this));
        params.put("MID", "Ojasso36077880907527");
        params.put("ORDER_ID", order_Id);
        params.put("WEBSITE", "OjassoWAP");
        params.put("CALLBACK_URL", CGlobalVariables.CALLBACK_URL + order_Id);
        params.put("TXN_AMOUNT", amountToPay);
        params.put("CUST_ID", email);

        chat_Id = chat_Id.equalsIgnoreCase("") ? "0" : chat_Id;
        String extraData = "chatId_" + chat_Id + "_type_" + CGlobalVariables.PAYMENT_TYPE_SERVICE
                + "_appVersion_" + BuildConfig.VERSION_NAME + "_appName_" + BuildConfig.APPLICATION_ID+
                "_firebaseinstanceid_"+CUtils.getFirebaseAnalyticsAppInstanceId(this)+
                "_facebookinstanceid_"+CUtils.getFacebookAnalyticsAppInstanceId(this)+
                "_isfirstpurchase_"+CUtils.getFirstTimePurchaseValue(this);

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
        transactionManager.setAppInvokeEnabled(false);
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
                if (TextUtils.isEmpty(responseData)) {
                    processPaytmTransaction("TXN_FAILED");
                } else {
                    JSONObject respObj = new JSONObject(responseData);
                    String status = respObj.getString("STATUS");
                    processPaytmTransaction(status);
                }
            } catch (Exception e) {
                //
            }
        }
        /*
        else if (requestCode == PAYPAL_REQUEST_CODE) {

            if (resultCode == Activity.RESULT_OK) {

                PaymentConfirmation confirm = data.getParcelableExtra(PaymentActivity.EXTRA_RESULT_CONFIRMATION);
               // Log.e("paypal confirm", confirm.toString());
                if (confirm != null) {
                    try {
                        String paymentDetails = confirm.toJSONObject().toString(4);
                        //Log.e("paypal payDetails", paymentDetails);

                        JSONObject jsonDetails = new JSONObject(paymentDetails);
                        //Log.e("paypal payDetails jo", jsonDetails.toString());
                        JSONObject jsonData = jsonDetails.getJSONObject("response");

                        String id = jsonData.getString("id");
                        String state = jsonData.getString("state");
                        //Toast.makeText(this, "PayPal Payment : "+state+ " , Id "+id, Toast.LENGTH_SHORT).show();
                        sendDataToServer("1");
                    } catch (JSONException e) {
                        //Log.e("paypal", "paypal error : "+e);
                    }
                }
            } else if (resultCode == Activity.RESULT_CANCELED) {
                Log.i("paypal", "User canceled");
                sendDataToServer("0");
            } else if (resultCode == PaymentActivity.RESULT_EXTRAS_INVALID) {
                Log.i("paypal", "Invalid Payment or PayPalConfiguration was submitted");
                sendDataToServer("0");
            }
        }
        */
        else if (requestCode == REQUEST_CODE_CCAVENUE && data != null) {
            try {
                payStatus = data.getStringExtra("payStatus");
                sendDataToServer(payStatus, "",false);
            } catch (Exception e) {
                //
            }
        } else if (requestCode == REQUEST_CODE_LOGIN) {
            try {
                String userPhoneNumber = CUtils.getUserID(PaymentInformationActivity.this);
                if (!TextUtils.isEmpty(userPhoneNumber)) {
                    getOrderId();
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
        sendDataToServer(payStatus, "",false);
    }

    private void postDataToAvenue(String orderId) {

        Intent intent = new Intent(PaymentInformationActivity.this, ActCCAvenueServicePaymentActivity.class);

        intent.putExtra(AvenuesParams.ACCESS_CODE, getResources().getString(R.string.access_code).trim());
        intent.putExtra(AvenuesParams.ORDER_ID, orderId.trim());
        intent.putExtra(AvenuesParams.BILLING_TEL, CUtils.getUserID(PaymentInformationActivity.this));
        intent.putExtra(AvenuesParams.BILLING_EMAIL, "");

        intent.putExtra(AvenuesParams.REDIRECT_URL,
                com.ojassoft.astrosage.utils.CGlobalVariables.avenueRedirectUrl);
        intent.putExtra(AvenuesParams.CANCEL_URL,
                com.ojassoft.astrosage.utils.CGlobalVariables.avenueRedirectUrl);
        intent.putExtra(AvenuesParams.RSA_KEY_URL, com.ojassoft.astrosage.utils.CGlobalVariables.avenueRsaUrl);

        intent.putExtra(AvenuesParams.CUSTOMER_IDENTIFIER, "".trim());
        intent.putExtra(AvenuesParams.CURRENCY, currency);
        Double d = NumberUtils.safeParseDouble(amountToPay);
        String amount = String.valueOf(d);
        intent.putExtra(AvenuesParams.AMOUNT, amount);
        startActivityForResult(intent, REQUEST_CODE_CCAVENUE);
    }

    private void openLoginActivity() {
        try {
            Intent intent = new Intent(PaymentInformationActivity.this, LoginSignUpActivity.class);
            intent.putExtra(CGlobalVariables.IS_FROM_SCREEN, PAYMENT_INFO_SCREEN);
            startActivityForResult(intent, REQUEST_CODE_LOGIN);
        } catch (Exception e) {
            //
        }
    }

    @Override
    public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response, int requestCode) {
        hideProgressBar();
        if (response.body() != null) {
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
        Log.e("PaymentStatus", "onFailure=" + t);
        hideProgressBar();
        showPaymentProcessDialog();
    }

    public void handleWalletRechargeResponse(String response) {
        Log.e("PaymentStatus", response);
        try {
            JSONObject jsonObject = new JSONObject(response);
            String status = jsonObject.getString("status");
            status = "1";
            if (status.equals("1") || status.equals("2")) {
                try {
                    //CUtils.clearWalletApiCache(mainQueue);
                    //getWalletPriceData();
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (paymentSucessfulDialog != null
                        && paymentSucessfulDialog.getDialog() != null
                        && paymentSucessfulDialog.getDialog().isShowing()
                        && !paymentSucessfulDialog.isRemoving()) {
                    //dialog is showing so do nothing
                } else {
                    //dialog is not showing
                    paymentSucessfulDialog = new PaymentSucessfulDialog(amount);
                    paymentSucessfulDialog.show(getSupportFragmentManager(), "PaymentSucessfulDialog");
                }

            } else {
                showPaymentProcessDialog();
            }
        } catch (Exception e) {
            //Log.e("PaymentStatus ", "exp="+e);
        }
    }

    private void showPaymentProcessDialog() {
        try {
            if (paymentProcessDialog != null
                    && paymentProcessDialog.getDialog() != null
                    && paymentProcessDialog.getDialog().isShowing()
                    && !paymentProcessDialog.isRemoving()) {
                //dialog is showing so do nothing
            } else {
                //dialog is not showing
                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_OPEN_RECHARGE_PROCESS_DIALOG,
                        CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");
                paymentProcessDialog = new PaymentProcessDialog(amount);
                paymentProcessDialog.show(getSupportFragmentManager(), "PaymentProcessDialog");
            }
        } catch (Exception e) {
            //
        }
    }

    public void closeActivity() {
        setResult(RESULT_OK);
        finish();
    }


    public void getOrderId() {
        CUtils.addFacebookAndFirebaseEvent(AppEventsConstants.EVENT_NAME_INITIATED_CHECKOUT,paymentModeStr,"PaymentInformationActivity");
        if(selectedPosition != -1){//-1 means user input recharge amount
            if(services == null) { //in case of service is null then reget service
                services = CUtils.getRechargeService(selectedPosition);
                if(services == null) {
                    Toast.makeText(this, getString(R.string.please_select_recharge_amount) + " ("+selectedPosition + ")", Toast.LENGTH_LONG).show();
                    finish();
                    return;
                }
            }
        }
        getOrderIdReq = false;
        if (!CUtils.isConnectedWithInternet(PaymentInformationActivity.this)) {
            CUtils.showSnackbar(containerLayout, getResources().getString(R.string.no_internet), PaymentInformationActivity.this);
        } else {

            if (pd == null)
                pd = new CustomProgressDialog(PaymentInformationActivity.this);
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
                                LocalBroadcastManager.getInstance(PaymentInformationActivity.this).registerReceiver(mReceiverBackgroundLoginService
                                        , new IntentFilter(CGlobalVariables.SEND_BROADCAST_BACK_GR_LOGIN));

                                startBackgroundLoginService();

                            }
                        } else {
                            String result = obj.getString("Result");
                            if(result.equalsIgnoreCase("1")) {
                                orderId = obj.getString("OrderId");
                                amountToPay = obj.getString("PriceRsToPay");
                                currency = CGlobalVariables.CURRENCY_INDIA;

                                if (paymentModeStr.equalsIgnoreCase(CGlobalVariables.PAYTM)) {
                                    new GetCheckSum(PaymentInformationActivity.this).getCheckSum(getchecksumparams(orderId), 0);
                                } else if (paymentModeStr.equalsIgnoreCase(CGlobalVariables.CCAVENUE)) {
                                    postDataToAvenue(orderId);
                                } else {
                                    if (!CUtils.getCountryCode(PaymentInformationActivity.this).equals(COUNTRY_CODE_IND)) {
                                        if (actualraters != 1.0 && !payInRsCheckBox.isChecked()) {
                                            amountToPay = obj.getString("PriceToPay");
                                            currency = CGlobalVariables.CURRENCY_USD;
                                        }
                                    }
                                    JSONObject razorpayResponseObj = obj.getJSONObject("RazorpayResponse");
                                    String razorpayOrderId = razorpayResponseObj.getString("razorpayOrderId");
                                    startPayment(orderId, razorpayOrderId);
                                }
                            } else {
                                String errorMsg = obj.getString("Message");
                                CUtils.showSnackbar(containerLayout, errorMsg+" ("+result+")", PaymentInformationActivity.this);
                            }
                        }
                    } catch (Exception e) {
                        Log.e("TestPayment=> ", e.toString());
                        CUtils.showSnackbar(containerLayout, e.toString(), PaymentInformationActivity.this);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("TestPayment=> f", t.toString());
                    hideProgressBar(pd);
                    CUtils.showSnackbar(containerLayout, t.toString(), PaymentInformationActivity.this);
                }
            });

        }
    }

    /**
     * UPI Payment Handling Starts
     * This method is used to set the phonepe payment gateway data.
     */
    private void setPhonePePaymentGetwayData() {
        LinearLayout phonepeOption = findViewById(R.id.phonepe_option);
        LinearLayout gpayOption = findViewById(R.id.gpay_option);
        LinearLayout paytmOption = findViewById(R.id.paytm_option);
        LinearLayout bhimOption = findViewById(R.id.bhim_option);
        LinearLayout credOption = findViewById(R.id.cred_option);

        ImageView phonepeIcon = findViewById(R.id.phonepe_icon);
        ImageView gpayIcon = findViewById(R.id.gpay_icon);
        ImageView paytmIcon = findViewById(R.id.paytm_icon);
        ImageView bhimIcon = findViewById(R.id.bhim_icon);
        ImageView credIcon = findViewById(R.id.cred_icon);

        TextView phonepeText = findViewById(R.id.phonepe_text);
        TextView gpayText = findViewById(R.id.gpay_text);
        TextView paytmText = findViewById(R.id.paytm_text);
        TextView bhimText = findViewById(R.id.bhim_text);
        TextView credText = findViewById(R.id.cred_text);
        View divider2 = findViewById(R.id.divider2);
        UPIAppChecker.showAppIcons(this, phonepeOption, gpayOption, paytmOption,bhimOption,credOption);
        if (!intentFlowVisibilityForVartaWallet || (phonepeOption.getVisibility() == View.GONE && paytmOption.getVisibility() == View.GONE && gpayOption.getVisibility() == View.GONE && bhimOption.getVisibility() == View.GONE && credOption.getVisibility() == View.GONE)) {
            upiPaymentOptionsContainer.setVisibility(View.GONE);
            upiPaymentOptionsHeading.setVisibility(View.GONE);
            divider2.setVisibility(View.GONE);
            other_upi_text.setText(getResources().getString(R.string.pay_with_upi_apps));
        } else {
            upiPaymentOptionsContainer.setVisibility(View.VISIBLE);
            other_upi_text.setText(getResources().getString(R.string.pay_with_other_upi_apps));
        }
        // Group the views for easier management
        paymentOptions = Arrays.asList(
                new PaymentView(phonepeOption, phonepeIcon, phonepeText,CGlobalVariables.UPI_PAYMENTS_PHONE),
                new PaymentView(gpayOption, gpayIcon, gpayText,CGlobalVariables.UPI_PAYMENTS_GPAY),
                new PaymentView(paytmOption, paytmIcon, paytmText,CGlobalVariables.UPI_PAYMENTS_PAYTM),
                new PaymentView(bhimOption, bhimIcon, bhimText,CGlobalVariables.UPI_PAYMENTS_BHIM),
                new PaymentView(credOption, credIcon, credText,CGlobalVariables.UPI_PAYMENTS_CRED)
        );
        boolean isShowCredAndBhim = CUtils.getBooleanData(this, com.ojassoft.astrosage.utils.CGlobalVariables.ISSHOWCREDANDBHIM, false);
        if(!isShowCredAndBhim){
            credOption.setVisibility(View.GONE);
            bhimOption.setVisibility(View.GONE);
        }
        // if all apps are visible then hide cred option
        if (phonepeOption.getVisibility() == View.VISIBLE && paytmOption.getVisibility() == View.VISIBLE && gpayOption.getVisibility() == View.VISIBLE && bhimOption.getVisibility() == View.VISIBLE && credOption.getVisibility() == View.VISIBLE) {
            bhimOption.setVisibility(View.GONE);
        }
        // Set the initial state (e.g., PhonePe selected) now have stopped that's why it is commented
        //selectPaymentOption(paymentOptions.get(0), paymentOptions);

        // Set click listeners for each option
        for (PaymentView option : paymentOptions) {
            option.layout.setOnClickListener(v -> selectPaymentOption(option, paymentOptions));
        }
    }
    /**
     * This method is used to get PhonePay/Gpay/Paytm intent url from the server.
     *
     * @param paymentMethodName - The type of payment method (eg. PhonePay, Gpay or Paytm) which is selected by the user.
     *                          This parameter is used to identify the type of payment method and to get the corresponding
     *                          intent url from the server.
     *
     * This method sends a POST request to the server with the necessary parameters to get the intent url. If the request
     * is successful, it extracts the intent url from the server response and starts an intent to initiate the payment.
     *
     * If the request fails, it shows a progress dialog indicating that the payment is being processed.
     *
     * @see RetrofitClient
     * @see ApiList
     * @see ResponseBody
     * @see CustomProgressDialog
     * @see UPIAppChecker
     */
    private void getApiCallForPhonePayIntentUrl(String paymentMethodName) {
        CUtils.addFacebookAndFirebaseEvent(AppEventsConstants.EVENT_NAME_INITIATED_CHECKOUT,paymentModeStr,"PaymentInformationActivity");
        if (pd == null)
            pd = new CustomProgressDialog(PaymentInformationActivity.this);
        pd.show();
        pd.setCancelable(false);

        ApiList api = RetrofitClient.getInstance().create(ApiList.class);
        Map<String, String> mapNew = getParamsForGenerateOrderId();
        switch (paymentMethodName) {
            case CGlobalVariables.UPI_PAYMENTS_PHONE:
                CUtils.fcmAnalyticsEvents(CGlobalVariables.VIA_PHONEPE_UPI, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                mapNew.put(CGlobalVariables.TARGET_APP, UPIAppChecker.PACKAGE_PHONEPE);
                mapNew.put(CGlobalVariables.PAY_MODE, CGlobalVariables.PHONEPE);
                break;
            case CGlobalVariables.UPI_PAYMENTS_GPAY:
                CUtils.fcmAnalyticsEvents(CGlobalVariables.VIA_GPAY_UPI, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                mapNew.put(CGlobalVariables.TARGET_APP, UPIAppChecker.PACKAGE_GOOGLE_PAY);
                mapNew.put(CGlobalVariables.PAY_MODE, CGlobalVariables.GPAY);
                break;
            case CGlobalVariables.UPI_PAYMENTS_BHIM:
                CUtils.fcmAnalyticsEvents(CGlobalVariables.VIA_CRED_UPI, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                mapNew.put(CGlobalVariables.TARGET_APP, UPIAppChecker.PACKAGE_BHIM);
                mapNew.put(CGlobalVariables.PAY_MODE, CGlobalVariables.BHIM);
                break;
            case CGlobalVariables.UPI_PAYMENTS_CRED:
                CUtils.fcmAnalyticsEvents(CGlobalVariables.VIA_CRED_UPI, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                mapNew.put(CGlobalVariables.TARGET_APP, UPIAppChecker.PACKAGE_CRED);
                mapNew.put(CGlobalVariables.PAY_MODE, CGlobalVariables.CRED);
                break;
            case CGlobalVariables.UPI_PAYMENTS_PAYTM:
                CUtils.fcmAnalyticsEvents(CGlobalVariables.VIA_PAYTM_UPI, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                mapNew.put(CGlobalVariables.TARGET_APP, UPIAppChecker.PACKAGE_PAYTM);
                mapNew.put(CGlobalVariables.PAY_MODE, CGlobalVariables.PAYTM);
                break;
            case CGlobalVariables.PAYMENTS_PHONEPE_CHECKOUT:
                CUtils.fcmAnalyticsEvents(CGlobalVariables.VIA_PHONEPE_STANDARD_CHECKOUT, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                mapNew.put(CGlobalVariables.TARGET_APP, "");
                mapNew.put(CGlobalVariables.PAY_MODE, CGlobalVariables.PHONEPE_CHECKOUT);
                mapNew.put(CGlobalVariables.IS_PAYMENT_CHECKOUT, "true");
                break;

        }

        Call<ResponseBody> call = api.phonePayOrderCreation(mapNew);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                hideProgressBar();
                try {
                    if(response.body() == null){
                        //CUtils.showSnackbar(containerLayout, "onResponse() Server Response is null", PaymentInformationActivity.this);
                        errorMsg = "Server Response is null";
                        showFailedPaymentDialog();
                        return;
                    }
                    String myResponse = response.body().string();
                    JSONObject obj = new JSONObject(myResponse);
                    if(paymentMethodName.equals(CGlobalVariables.PAYMENTS_PHONEPE_CHECKOUT)){
                        // merchantResponse
                        JSONObject merchantResponse = obj.getJSONObject("merchantResponse");
                         orderId = merchantResponse.optString("OrderId");

                        // phonepeResponse
                        JSONObject phonepeResponse = obj.getJSONObject("phonepeResponse");
                        String phonePeOrderId = phonepeResponse.optString("orderId");
                        String phonePeToken = phonepeResponse.optString("token");


                        //AstrosageKundliApplication.paymentScreenLogs = "orderId = " + orderId+"/n"+"phonePeOrderId = " + phonePeOrderId+"/n"+"phonePeToken = " + phonePeToken+"/n";
                        openPhonePeCheckout(phonePeOrderId, phonePeToken);


                    }else {
                        JSONObject phonepeResponse = obj.getJSONObject("phonepeResponse");
                        JSONObject merchantResponse = obj.getJSONObject("merchantResponse");
                        String intentUrl = phonepeResponse.getString("intentUrl");
                        orderId = merchantResponse.getString("OrderId");
                        //open upi payment app(Phone Pe, G Pay, Paytm) via intent url
                        openPaymentApp(intentUrl);
                    }

                } catch (Exception e) {
                    errorMsg = "Exception = "+e.getMessage();
                    showFailedPaymentDialog();
                    //Utils.showSnackbar(containerLayout, "onResponse() = "+e, PaymentInformationActivity.this);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                hideProgressBar();
                errorMsg = "Failure Exception = "+t;
                showFailedPaymentDialog();
                //CUtils.showSnackbar(containerLayout, t, PaymentInformationActivity.this);
            }
        });
    }

    private void openPhonePeCheckout(String phonePeOrderId, String phonePeToken) {
        try {
            PhonePeKt.startCheckoutPage(
                    this,                     // context
                    phonePeToken,                    // token
                    phonePeOrderId,                    // orderId
                    activityResultLauncherStandard    // ActivityResultLauncher<Intent>
            );
            phonePeStandardCheckoutStarted = true;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    //open upi payment app(Phone Pe, G Pay, Paytm) via intent url
    private void openPaymentApp(String intentUrl) {
        Uri uri = Uri.parse(
                intentUrl);
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(uri);
        if (selectedPayMode.equalsIgnoreCase(CGlobalVariables.UPI_PAYMENTS_PAYTM)) {
            intent.setPackage(UPIAppChecker.PACKAGE_PAYTM); // Force open in Paytm app
        } else if (selectedPayMode.equalsIgnoreCase(CGlobalVariables.UPI_PAYMENTS_GPAY)) {
            intent.setPackage(UPIAppChecker.PACKAGE_GOOGLE_PAY); // Force open in Google Pay app
        } else if (selectedPayMode.equalsIgnoreCase(CGlobalVariables.UPI_PAYMENTS_PHONE)) {
            intent.setPackage(UPIAppChecker.PACKAGE_PHONEPE); // Force open in PhonePe app
        }else if (selectedPayMode.equalsIgnoreCase(CGlobalVariables.UPI_PAYMENTS_BHIM)) {
            intent.setPackage(UPIAppChecker.PACKAGE_BHIM); // Force open in PhonePe app
        }else if (selectedPayMode.equalsIgnoreCase(CGlobalVariables.UPI_PAYMENTS_CRED)) {
            intent.setPackage(UPIAppChecker.PACKAGE_CRED); // Force open in PhonePe app
        }
        upiLauncher.launch(intent);
    }
        boolean phonePeStandardCheckoutStarted;
    @Override
    protected void onResume() {
        super.onResume();
        if(phonePeStandardCheckoutStarted){
            getOrderStatusApi();
            phonePeStandardCheckoutStarted = false;
        }
    }

    private void setOnActivityResultForStandardCheckoutLauncher() {
        activityResultLauncherStandard = registerForActivityResult( new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    try {
                        Intent data = result.getData();
                        if (data != null) {
                            //AstrosageKundliApplication.paymentScreenLogs = AstrosageKundliApplication.paymentScreenLogs +"/n    "+"data = " + data+"/n";
                            //call order status api
                            //getOrderStatusApi();
                        }
                    }catch (Exception e){
                        sendDataToServer("0", "",false);

                    }

                });  }
    private void setOnActivityResultForUpiLauncher() {
        //Notes - when user cancel the payment then
        //Status=SUBMITTED, TxnId=null, ResponseCode=02, TxnRef=OM2509250812412655389553 (Phone pe)
        //Status=FAILURE, TxnId=null, ResponseCode=null, TxnRef=null(G Pay) and (Paytm)
        //Notes - when Payment is success
        //Status=SUCCESS, TxnId=AXLe989334e471949a28a34feb10440883a, ResponseCode=00, TxnRef=OM2509250815499918508350(Phone Pe)
        //Status=SUCCESS, TxnId=AXIe040afe90df94424ae2109e840978c81, ResponseCode=0, TxnRef=OM2509250817333599356167(G Pay)
        //Status=SUCCESS, TxnId=PTM93342cb2475b43cab8214d8b1e477db8, ResponseCode=0, TxnRef=OM2509250818498691165143(Paytm)
        upiLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    try {
                        Intent data = result.getData();
                        if (data != null) {
                            //call order status api
                            getOrderStatusApi();
                            UPIResponse upiResp = UPIResponseParser.parse(result.getData());
                           // CGlobalVariables.PAYMENT_INFO_ACTIVITY_LOGS = upiResp.toString();
                            if (CGlobalVariables.SUCCESS.equalsIgnoreCase(upiResp.status)) {
                                //setEcommercePurchaseEvent();
                                //  Payment success
                            } else if ("SUBMITTED".equals(upiResp.status)) {
                                // Payment pending
                            } else {
                                // Payment failed / cancelled
                            }
                        } else {
                            Log.d("UPI", "UPI App closed without result");
                        }
                    }catch (Exception e){
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
        if (pd == null)
            pd = new CustomProgressDialog(PaymentInformationActivity.this);
        pd.show();
        pd.setCancelable(false);
        ApiList api = RetrofitClient.getInstance3().create(ApiList.class);
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
                        sendDataToServer("1", "",false);
                    } else  {
                        sendDataToServer("0", "",false);
                    }
                } catch (Exception e) {
                    sendDataToServer("0", "",false);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                sendDataToServer("0", "",false);
                hideProgressBar();
            }
        });

    }
    // generate params for upi payments order status
    private Map<String, String> getParamsForOrderStatus() {
        Map<String, String> mapNew = new HashMap<>();
        mapNew.put("orderId", orderId);
        mapNew.put("currency", CGlobalVariables.CURRENCY_INDIA);
        mapNew.put("PriceRs", String.valueOf(paymentAmount));
        mapNew.put("Price", String.valueOf(totalDollar));
        if(paymentModeStr.equals(CGlobalVariables.PAYMENTS_PHONEPE_CHECKOUT)){
            mapNew.put("ispaymentcheckout", "true");
        }
        mapNew.put("key", CUtils.getApplicationSignatureHashCode(PaymentInformationActivity.this));
        return CUtils.setRequiredParams(mapNew);
    }

    // Helper class to group the views for each payment option
    static class PaymentView {
        final LinearLayout layout;
        final ImageView icon;
        final TextView text;
        final String paymentMethodName;
        PaymentView(LinearLayout layout, ImageView icon, TextView text,String paymentMethodName) {
            this.layout = layout;
            this.icon = icon;
            this.text = text;
            this.paymentMethodName = paymentMethodName;
        }
    }
    private void selectPaymentOption(PaymentView selectedOption, List<PaymentView> allOptions) {
        radioButtonRazorpay.setChecked(false);
        radioButtonBankTransferOther.setChecked(false);
        radioButtonWallet.setChecked(false);
        radioButtonCreditDebitCardRazorpay.setChecked(false);
        radioButtonPaytm.setChecked(false);
        radioButtonOther.setChecked(false);
        radioButtonAlternativeGateway.setChecked(false);
        for (PaymentView option : allOptions) {
            if (option.equals(selectedOption)) {
                // This is the selected item
                option.layout.setSelected(true);
                animateIcon(option.icon, R.animator.icon_scale_up);
                animateTextSize(option.text, 14f); // Animate to a larger size

                selectedPayMode = option.paymentMethodName;
                paymentModeStr = option.paymentMethodName;
                getApiCallForPhonePayIntentUrl(option.paymentMethodName);
//                if(isAutoselected){
//                    selectedPayMode = option.paymentMethodName;
//                    //CUtils.fcmAnalyticsEvents(CGlobalVariables.VIA_PHONEPE_UPI, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
//
//                }
//                isAutoselected = true;
                //Toast.makeText(this, option.paymentMethodName, Toast.LENGTH_SHORT).show();
            } else {
                // These are the unselected items
                option.layout.setSelected(false);
                animateIcon(option.icon, R.animator.icon_scale_down);
                animateTextSize(option.text, 12f); // Animate back to original size
            }
        }
    }

    private void unSelectPaymentOption( List<PaymentView> allOptions) {
        for (PaymentView option : allOptions) {
                option.layout.setSelected(false);
                animateIcon(option.icon, R.animator.icon_scale_down);
                animateTextSize(option.text, 12f); // Animate back to original size
        }
    }

    private void animateIcon(ImageView icon, int animationResId) {
        Animator animator = AnimatorInflater.loadAnimator(this, animationResId);
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

    // ------------------------UPI Payment Handling Ends--------------------//

    // generate params for generate order id
    public Map<String, String> getParamsForGenerateOrderId() {
        String countryCode = CUtils.getCountryCode(PaymentInformationActivity.this);
        currency = CGlobalVariables.CURRENCY_INDIA;
        if (!CUtils.getCountryCode(PaymentInformationActivity.this).equals(COUNTRY_CODE_IND)){
            if(paymentModeStr.equalsIgnoreCase(CGlobalVariables.RAZORPAY)) { //in case of razorpay payment
                if (actualraters != 1.0 && !payInRsCheckBox.isChecked()) {
                    currency = CGlobalVariables.CURRENCY_USD;
                }
            } else if(paymentModeStr.equalsIgnoreCase(CGlobalVariables.STRIPE)) { //in case of stripe payment
                currency = CGlobalVariables.CURRENCY_USD;
            }
        }

        Calendar calendar = Calendar.getInstance();
        HashMap<String, String> headers = new HashMap<String, String>();
        try {
            headers.put("key", CUtils.getApplicationSignatureHashCode(PaymentInformationActivity.this));
            headers.put("regName", CUtils.getCountryCode(PaymentInformationActivity.this) + "-" + CUtils.getUserID(PaymentInformationActivity.this));
            String emailId = "";
            if(TextUtils.isEmpty(com.ojassoft.astrosage.varta.utils.CUtils.getUserID(PaymentInformationActivity.this))){
                emailId =com.ojassoft.astrosage.utils.CUtils.getUserName(PaymentInformationActivity.this);
            }else {
                emailId = com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(PaymentInformationActivity.this) + com.ojassoft.astrosage.varta.utils.CUtils.getUserID(PaymentInformationActivity.this);
            }
            headers.put(CGlobalVariables.KEY_EMAIL_ID, replaceEmailChar(emailId));
            headers.put("gender", "male");
            headers.put("dateOfBirth", String.valueOf(calendar.get(Calendar.DATE)));
            headers.put("monthOfBirth", String.valueOf(calendar.get(Calendar.MONTH) + 1));
            headers.put("yearOfBirth", String.valueOf(calendar.get(Calendar.YEAR)));
            headers.put("hourOfBirth", String.valueOf(calendar.get(Calendar.HOUR)));
            headers.put("minOfBirth", String.valueOf(calendar.get(Calendar.MINUTE)));

            UserProfileData userProfileData = com.ojassoft.astrosage.varta.utils.CUtils.getUserSelectedProfileFromPreference(this);

            if (userProfileData != null && !TextUtils.isEmpty(userProfileData.getPlace())) {
                // Extracts and safely assigns city and state values from the user's place address string.
                getCityStateFromPlaceAddress(userProfileData.getPlace());
                headers.put("place", userProfileData.getPlace());
            } else {
                headers.put("place", "Agra");
            }
            headers.put("state", state);
            headers.put("nearCity", nearCity);
            if (!countryCode.equals(COUNTRY_CODE_IND)) {
                headers.put("country", countryCode);
            }else {
                headers.put("country", "India");
            }

            headers.put(CGlobalVariables.COUNTRY_CODE, countryCode);
            headers.put("currency", currency);
            if (selectedPosition == -1) { //in case of user input recharge amount
                headers.put("problem", "Variable Amount Recharge");
                headers.put("serviceId", CGlobalVariables.SERVICE_ID_VARIABLE_AMOUNT);
                headers.put("rechargeamount", String.valueOf(paymentAmount));
                headers.put("frompopunder", frompopunder);
            } else {
                headers.put("problem", services.getServicename());
                headers.put("serviceId", services.getServiceid());
                headers.put("rechargeamount", "");
                headers.put("price", String.valueOf(totalDollar));
                headers.put("priceRs", services.getPaymentamount());
            }

            headers.put("payMode", paymentModeStr);
            headers.put("countrycode", CUtils.getCountryCode(PaymentInformationActivity.this));
            headers.put("mobileNo", CUtils.getUserID(PaymentInformationActivity.this));
            headers.put("profileId", "");
            headers.put("KPHN", "0");
            headers.put("TimezoneOfBirth", "5.5");

            headers.put(CGlobalVariables.KEY_AS_USER_ID, replaceEmailChar(CUtils.getUserID(PaymentInformationActivity.this)));
            headers.put("asplanid", "1");

            headers.put("LongDegOfBirth", "78");
            headers.put("LongMinOfBirth", "1");
            headers.put("LongEWOfBirth", "E");
            headers.put("LatDegOfBirth", "27");
            headers.put("LatMinOfBirth", "10");
            headers.put("LatNSOfBirth", "N");
            headers.put("device_id", CUtils.getMyAndroidId(PaymentInformationActivity.this));//"407f6ad3c4ff24d4"
            headers.put("couponcode", this.coupanCode);
            headers.put("ordersource", CGlobalVariables.APP_SOURCE);
            headers.put(CGlobalVariables.PROPERTY_APP_VERSION, BuildConfig.VERSION_NAME + "(" + BuildConfig.VERSION_CODE + ")");
            headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
            headers.put("orderfromdomain", "varta.astrosage.com");
            headers.put("activitysource", isRetryPayment ? "TryAgain_"+screenOpenFrom : screenOpenFrom);
            isRetryPayment = false;
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

        String firstInstallTime = CUtils.getStringData(this, com.ojassoft.astrosage.utils.CGlobalVariables.FIRST_INSTALL_TIME_KEY, "");
        headers.put(com.ojassoft.astrosage.utils.CGlobalVariables.FIRST_INSTALL_TIME_KEY, firstInstallTime);
        //Log.e("RequesthitPaym", headers.toString());
        return CUtils.setRequiredParams(headers);
    }

    /**
     * Safely extracts and assigns city and state values from a place address string.
     * Handles null, empty, and malformed inputs without causing application crashes.
     * Example input: "Ramanakkapeta, Andhra Pradesh"
     */
    public void getCityStateFromPlaceAddress(String location) {
        try {
            String[] parts = location.split(",");

            if (parts.length >= 1) {
                String city = parts[0];
                if(!TextUtils.isEmpty(city)){
                    nearCity = city;
                }
            }

            if (parts.length >= 2) {
                String state = parts[1];
                if(!TextUtils.isEmpty(state)){
                    this.state = state;
                }
            }
        } catch (Exception e) {
            //
        }
    }


    public void callMsgDialogData(String message, String title, boolean isShowOkBtn, String fromWhich) {
        try {

            CallMsgDialog dialog = new CallMsgDialog(message, title, isShowOkBtn, fromWhich);
            FragmentManager ft = getSupportFragmentManager();

            dialog.show(ft, "Dialog");
        } catch (Exception e) {
            CUtils.showSnackbar(containerLayout, "MsgDialog= "+e, PaymentInformationActivity.this);
        }
    }

    private void generateOrderForStripe(){
        CUtils.addFacebookAndFirebaseEvent(AppEventsConstants.EVENT_NAME_INITIATED_CHECKOUT,paymentModeStr,"PaymentInformationActivity");
        if(selectedPosition != -1){//-1 means user input recharge amount
            if(services == null) { //in case of service is null then reget service
                services = CUtils.getRechargeService(selectedPosition);
                if(services == null) {
                    Toast.makeText(this, getString(R.string.please_select_recharge_amount) + " ("+selectedPosition + ")", Toast.LENGTH_LONG).show();
                    finish();
                    return;
                }
            }
        }
        getOrderIdReq = false;

        if (!CUtils.isConnectedWithInternet(PaymentInformationActivity.this)) {
            CUtils.showSnackbar(containerLayout, getResources().getString(R.string.no_internet), PaymentInformationActivity.this);
        } else {

            if (pd == null)
                pd = new CustomProgressDialog(PaymentInformationActivity.this);
            pd.show();
            pd.setCancelable(false);
            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            retrofit2.Call<ResponseBody> call = api.generateOrderForStripe(getParamsForGenerateOrderId());
            android.util.Log.d("StripePaymentActivity", "URL: " + call.request().url().toString());

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {

                    hideProgressBar();
                    getOrderIdReq = true;

                    try {
                        String myResponse = response.body().string();

                        JSONObject jsonObject = new JSONObject(myResponse);
                        android.util.Log.d("StripePaymentActivity", "customerId: " + jsonObject);

                        //Get stripe object
                        JSONObject stripeObj = jsonObject.getJSONObject("stripe");
                        JSONObject order = jsonObject.getJSONObject("order");
                        JSONObject metadataObj = stripeObj.getJSONObject("metadata");
                        orderId = order.getString("OrderId");
                        //Get customer id
                        String customerId = stripeObj.getString("customerid");

                        //Get payment intent client secret
                        paymentIntentClientSecret = stripeObj.optString("clientsecret");

                        //Get payment intent id
                        intentID = stripeObj.getString("paymentintentid");

                        //Get currency and price to pay
                        currency = stripeObj.getString("currency");
                        priceToPay = order.getString("PriceToPay");

                        // ephemeralKey object
                        JSONObject ephKeyObj = stripeObj.getJSONObject("ephemeralkey");
                        String ephemeralKeySecret = ephKeyObj.getString("secret");

                        // Create the CustomerConfiguration if we have the necessary details
                        if (!customerId.isEmpty() && !ephemeralKeySecret.isEmpty()) {
                            customerConfig = new PaymentSheet.CustomerConfiguration(customerId, ephemeralKeySecret);
                        } else {
                            android.util.Log.w(TAG, "CustomerId or EphemeralKeySecret is missing. Cannot set customerConfig for saved payment methods.");
                        }

                        //open stripe payment sheet
                        openStripePaymentSheet();

                    } catch (Exception e) {
                       // android.util.Log.d("StripePaymentActivity", "exception: " + e.getMessage().toString());
                        CUtils.showSnackbar(containerLayout, e.toString(), PaymentInformationActivity.this);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                   // android.util.Log.d("StripePaymentActivity", "failed: " + t.getMessage().toString());
                    CUtils.showSnackbar(containerLayout, t.toString(), PaymentInformationActivity.this);

                }
            });
        }
    }

    private void getStripePaymentStatusApi(){
        ApiList api = RetrofitClient.getInstance().create(ApiList.class);
        retrofit2.Call<ResponseBody> call = api.getStripeOrderStatus(getOrderStatusParams());
        android.util.Log.d("StripePaymentActivity", "URL: " + call.request().url().toString());

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(retrofit2.Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {


                try {
                    String myResponse = response.body().string();

                    JSONObject jsonObject = new JSONObject(myResponse);

                    String stripeStatus = jsonObject.getString("stripestatus");
                    android.util.Log.d("StripePaymentActivity", "stripeStatus: " + stripeStatus);


                    android.util.Log.d("StripePaymentActivity", "myResponse: " + jsonObject);
                    if (stripeStatus.equals("1")){
                        sendDataToServer("1", "", true);
                    }else {
                        sendDataToServer("0", "",false);
                    }
                } catch (Exception e) {
                    android.util.Log.d("StripePaymentActivity", "exception: " + e.getMessage().toString());
                    sendDataToServer("0", "",false);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                android.util.Log.d("StripePaymentActivity", "failed: " + t.getMessage().toString());
                sendDataToServer("0", "",false);
            }
        });
    }

    public Map<String,String> getOrderStatusParams(){
        HashMap<String, String> headers = new HashMap<String, String>();
        try {
            headers.put("key", CUtils.getApplicationSignatureHashCode(this));
            headers.put("paymentintentid", intentID);
            headers.put("currency", currencyUSD);
            headers.put("price", priceToPay);

        } catch (Exception e) {

        }
        return headers;
    }

    /**
     * Configures and presents the PaymentSheet to the user.
     */
    private void openStripePaymentSheet() {

        String countryDialCiode = CUtils.getCountryCode(PaymentInformationActivity.this);
        String countryCode  = com.ojassoft.astrosage.utils.CUtils.getCountryFromDialCode(Integer.parseInt(countryDialCiode));

        //android.util.Log.d("StripePaymentActivity", "countryDialCiode: " + countryDialCiode+", countryCode: "+countryCode);

        PaymentSheet.Address address =
                new PaymentSheet.Address(
                        null,   // city
                        countryCode,   // country ✅ default country
                        null,   // line1
                        null,   // line2
                        null,   // postalCode
                        null    // state
                );

        PaymentSheet.BillingDetails defaultBillingDetails =
                new PaymentSheet.BillingDetails(
                        address,
                        null,
                        null,
                        null
                );

        // 1️⃣ Google Pay Configuration (US)
        PaymentSheet.GooglePayConfiguration googlePayConfig =
                new PaymentSheet.GooglePayConfiguration(
                        PaymentSheet.GooglePayConfiguration.Environment.Test,
                        "US" // country code
                );


        // 2️⃣ Billing Details Collection Configuration
        PaymentSheet.BillingDetailsCollectionConfiguration billingConfig =
                new PaymentSheet.BillingDetailsCollectionConfiguration(
                        PaymentSheet.BillingDetailsCollectionConfiguration.CollectionMode.Always, // name
                        PaymentSheet.BillingDetailsCollectionConfiguration.CollectionMode.Never, // email
                        PaymentSheet.BillingDetailsCollectionConfiguration.CollectionMode.Never, // phone
                        PaymentSheet.BillingDetailsCollectionConfiguration.AddressCollectionMode.Full, // address
                        true // attachDefaultsToPaymentMethod
                );



        // 3️⃣ PaymentSheet Configuration
        PaymentSheet.Configuration config =
                new PaymentSheet.Configuration.Builder("AstroSage AI")
                        .customer(customerConfig)
                        .defaultBillingDetails(defaultBillingDetails)
                        .billingDetailsCollectionConfiguration(billingConfig)
                        .googlePay(googlePayConfig)
                        .allowsDelayedPaymentMethods(true)
                        .build();

        paymentSheet.presentWithPaymentIntent(paymentIntentClientSecret, config);

    }


    private void onPaymentSheetResult(
            final PaymentSheetResult paymentSheetResult
    ) {
        if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            setEcommercePurchaseEvent();
            //call payment verify status api
            getStripePaymentStatusApi();
        } else if (paymentSheetResult instanceof PaymentSheetResult.Canceled) {
            sendDataToServer("0", "",false);

        } else if (paymentSheetResult instanceof PaymentSheetResult.Failed) {
            sendDataToServer("0", "",false);
        }
    }

    private void showFailedPaymentDialog(){
        try {
            PaymentFailDialog dialog = new PaymentFailDialog();
            dialog.setTryAgainCallback(this::openAlternatePaymentOption);
            dialog.show(getSupportFragmentManager(), "PaymentFailDialog");
        } catch (Exception e) {
            //
        }
    }

    boolean isSpacialRechargePopShown = false;
    void checkSpacialRecharge(){
        String offerType = com.ojassoft.astrosage.varta.utils.CUtils.getUserIntroOfferType(this);
        boolean isShow25offer = CUtils.getBooleanData(this, com.ojassoft.astrosage.utils.CGlobalVariables.IS_SHOW_25_RUPEE_RECHARGE_OFFER, true);

        String cachedOfferResponse = CUtils.getStringData(this, com.ojassoft.astrosage.utils.CGlobalVariables.RECHARGE_SERVICE_OFFER_RESPONSE_KEY, "");
        if(isSpacialRechargePopShown || TextUtils.isEmpty(cachedOfferResponse)){
            finish();
            return;
        }

        if(offerType.contains(CGlobalVariables.REDUCED_PRICE_OFFER) && isShow25offer ) {
            parseData(cachedOfferResponse);

        }else{
            setResult(Activity.RESULT_CANCELED);
            finish();
        }
    }

    Dialog spacialRechargeDialog;
    void showSpacialRechargePopUp(WalletAmountBean walletAmountBean, int selectedPosition, int amount){
        if(spacialRechargeDialog != null && spacialRechargeDialog.isShowing()){
            return;
        }
        spacialRechargeDialog = new Dialog(this);
        spacialRechargeDialog.setContentView(R.layout.spacial_recharege_dialog_layout);
        Window window = spacialRechargeDialog.getWindow();
        if(window != null) {
            window.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.top_round_corner));
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.BOTTOM;
            window.setAttributes(lp);
        }
        spacialRechargeDialog.setCancelable(false);
        ImageView ivCancel = spacialRechargeDialog.findViewById(R.id.ivCancel);
        TextView tvTitle = spacialRechargeDialog.findViewById(R.id.tvHeading);
        FontUtils.changeFont(this, tvTitle, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        TextView tvSubTitle = spacialRechargeDialog.findViewById(R.id.tvSubText);
        tvSubTitle.setText(getResources().getString(R.string.continue_your_chat,amount));
        FontUtils.changeFont(this, tvSubTitle, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        TextView tvValidity = spacialRechargeDialog.findViewById(R.id.tvValidity);
        Typeface typeface = Typeface.createFromAsset(getAssets(),CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        tvValidity.setTypeface(typeface,Typeface.ITALIC);
        Button btnRecharge= spacialRechargeDialog.findViewById(R.id.btnRecharge);
        FontUtils.changeFont(this, btnRecharge, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        String msg = getResources().getString(R.string.continue_your_chat,amount);
        String subStr = getResources().getString(R.string.spacial_price,amount);
        ivCancel.setOnClickListener((v)->{
            CUtils.fcmAnalyticsEvents(SPECIAL_RECHARGE_25_DISMISS,  CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
            spacialRechargeDialog.dismiss();
        });
        btnRecharge.setOnClickListener((v)->{
            CUtils.fcmAnalyticsEvents(SPECIAL_RECHARGE_25_BTN_CLICK,  CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
            Bundle bundle = new Bundle();
            bundle.putSerializable(CGlobalVariables.WALLET_INFO, walletAmountBean);
            bundle.putInt(CGlobalVariables.SELECTED_POSITION, selectedPosition);
            bundle.putString(CGlobalVariables.CALLING_ACTIVITY, callingActivity);
            bundle.putString(CGlobalVariables.ASTROLGER_URL_TEXT, astrlogerUrlText);
            bundle.putString(CGlobalVariables.ASTROLOGER_MOB_NUMBER, astrologerPhoneNumber);
            Intent intent = new Intent(this, PaymentInformationActivity.class);
            intent.putExtras(bundle);
            startActivityForResult(intent, 1);
            spacialRechargeDialog.dismiss();
        });
        try {
            SpannableString spannable = new SpannableString(msg);
            int rateStart = msg.indexOf(subStr);
            int rateEnd = rateStart + subStr.length();

            spannable.setSpan(
                    new ForegroundColorSpan(ContextCompat.getColor(this, R.color.colorPrimary_day_night)),
                    rateStart,
                    rateEnd,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );
            spannable.setSpan(
                    new StyleSpan(Typeface.BOLD),
                    rateStart,
                    rateEnd,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            );
            tvSubTitle.setText(spannable);
        }catch(Exception e){
            e.printStackTrace();
        }
        isSpacialRechargePopShown = true;
        spacialRechargeDialog.show();

    }


    public void parseData(String response) {
        try {
            //android.util.Log.e("fiveRupeeTest", "onResponse: " + response);
            JSONObject jsonObject = new JSONObject(response);
            try {
                WalletAmountBean walletBean = new WalletAmountBean();
                WalletAmountBean.Services services;
                String gstRate = jsonObject.getString("gstrate");
                String dollarconversionrate = jsonObject.getString("dollarconversionrate");
                JSONArray jsonArray = jsonObject.getJSONArray("services");
                JSONObject innerJsonObject;
                boolean isServiceFound = false;
                int selectedPosition = -1;
                String amount = "";
                ArrayList<WalletAmountBean.Services> servicesArrayList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    innerJsonObject = jsonArray.getJSONObject(i);

                    String serviceid = innerJsonObject.getString("serviceid");
                    String servicename = innerJsonObject.getString("servicename");
                    String smalliconfile = innerJsonObject.getString("smalliconfile");
                    String categoryid = innerJsonObject.getString("categoryid");
                    String rate = innerJsonObject.getString("rate");
                    String raters = innerJsonObject.getString("raters");
                    String actualrate = innerJsonObject.getString("actualrate");
                    String actualraters = innerJsonObject.getString("actualraters");
                    String paymentamount = innerJsonObject.getString("paymentamount");
                    String offermessage = innerJsonObject.getString("offermessage");
                    String offerAmount = innerJsonObject.getString("offeramout");
                    if (serviceid.equals("249")) {
                        isServiceFound = true;
                        selectedPosition = i;
                        amount = raters;
                    }
                    services = walletBean.new Services();
                    services.setServiceid(serviceid);
                    services.setServicename(servicename);
                    services.setSmalliconfile(smalliconfile);
                    services.setCategoryid(categoryid);
                    services.setRate(rate);
                    services.setRaters(raters);
                    services.setActualrate(actualrate);
                    services.setActualraters(actualraters);
                    services.setPaymentamount(paymentamount);
                    services.setOffermessage(offermessage);
                    services.setOfferAmount(offerAmount);
                    servicesArrayList.add(services);
                }
                walletBean.setGstrate(gstRate);
                walletBean.setDollorConverstionRate(dollarconversionrate);
                walletBean.setServiceList(servicesArrayList);
                if (isServiceFound) {
                    CUtils.fcmAnalyticsEvents(SPECIAL_RECHARGE_25_SHOWN, SHOW_DIALOG_EVENT, "");
                    showSpacialRechargePopUp(walletBean, selectedPosition, (int) Double.parseDouble(amount));
                } else {
                    setResult(Activity.RESULT_CANCELED);
                    finish();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
