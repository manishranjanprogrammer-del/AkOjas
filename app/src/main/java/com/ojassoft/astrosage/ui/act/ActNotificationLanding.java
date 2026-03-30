package com.ojassoft.astrosage.ui.act;

import static com.ojassoft.astrosage.utils.CGlobalVariables.PAYMENT_TYPE_SERVICE;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.android.volley.AuthFailureError;
import com.android.volley.Cache;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.tabs.TabLayout;
import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.libojassoft.android.misc.SendDataBackToComponent;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanDateTime;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.beans.BeanPlace;
import com.ojassoft.astrosage.billing.BillingEventHandler;
import com.ojassoft.astrosage.jinterface.IAskCallback;
import com.ojassoft.astrosage.jinterface.IChoosePayOption;
import com.ojassoft.astrosage.jinterface.IExtraActionForAskAQuestionChat;
import com.ojassoft.astrosage.misc.AskAQueDataUpdateService;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.misc.GetCheckSum;
import com.ojassoft.astrosage.misc.NotificationLandingRecyclerView;
import com.ojassoft.astrosage.misc.SendUserPurchaseReportForServiceToServerForAstroChat;
import com.ojassoft.astrosage.misc.VerificationServiceForInAppBillingChat;
import com.ojassoft.astrosage.model.AppPurchaseDataSaveModelClass;
import com.ojassoft.astrosage.model.AstrologerServiceInfo;
import com.ojassoft.astrosage.model.ServicelistModal;
import com.ojassoft.astrosage.networkcall.OnVolleyResultListener;
import com.ojassoft.astrosage.ui.customcontrols.AppRater;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.ui.fragments.ChoosePayOptionFragmentDailog;
import com.ojassoft.astrosage.ui.fragments.ChooseServicePayOPtionDialog;
import com.ojassoft.astrosage.ui.fragments.astrochatfragment.model.MessageDecode;
import com.ojassoft.astrosage.ui.fragments.astrochatfragment.savedata.InternalStorage;
import com.ojassoft.astrosage.ui.fragments.astrochatfragment.savedata.SaveDataInternalStorage;
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

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ActNotificationLanding extends BaseInputActivity implements IChoosePayOption,
        IAskCallback, IExtraActionForAskAQuestionChat, PaymentResultListener, SendDataBackToComponent, OnVolleyResultListener, BillingEventHandler {

    public static final String SKU_ASKQUESTION_PLAN = "ask_a_question";
    static final int SUB_RC_REQUEST_ASK_QUESTION_PLAN = 20006;
    public static int userImageResource = R.drawable.ic_male;
    public static boolean isMessagesModified = false;
    public static int ASK_QUESTION_PLAN = 0;
    public Toolbar toolBar_InputKundli;
    public View appbarAppModule;
    public ServicelistModal itemdetails;
    NotificationLandingRecyclerView notificationLandingRecyclerView;
    ArrayList<String> arayaskquestionPlan = new ArrayList<String>(5);
    ProductDetails askAQSkuDetails;
    int layoutPosition;
    RecyclerView astrologerRecyclerView;
    ArrayList<MessageDecode> chatMessageArrayList;
    private final BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            CUtils.saveIntData(ActNotificationLanding.this, CGlobalVariables.COUNTNOTIFICATIONCENTER, 0);
            updateRecyclerViewMessages();
        }
    };
    boolean isQuestionSet;
    CustomProgressDialog pd = null;
    EditText questionAskAstrologer;
    String emailid;
    MessageDecode messageDecodeSend;
    //public static final String SKU_ASKQUESTION_PLAN = "test";
    String developerPayload = "", purchaseData = "", signature = "";
    int price_amount_micros = 5, price_currency_code = 6;
    int price_amount = 2;
    String pricedl;
    String pricers;
    // public static boolean isMessageNeedToSend=true;
    //PaytmPGService Service;
    String mob;
    String orderId;
    String query_for_question;
    String chatID;
    String mobile_num_for_question;
    Button askAQuestionNow;
    String order_Id = "";
    String chat_Id = "";
    String payStatus = "0";
    String payId = "";
    ImageView imgHome, imgRefresh;
    String[] errorResponse = {"Success",
            "Billing response result user canceled",
            "Network connection is down",
            "Billing API version is not supported for the type requested",
            "Requested product is not available for purchase",
            "Invalid arguments provided to the API",
            "Fatal error during the API action",
            "Failure to purchase since item is already owned",
            "Failure to consume since item is not owned"};
    String priceValue = "";
    String dumpDataString = "";
    private TextView tvTitle;
    private TabLayout tabs_input_kundli;
    private boolean isPaymentDone = true;
    private AstrologerServiceInfo astrologerServiceInfo;
    private String fullJsonDataObj = "";
    private RequestQueue queue;
    private boolean isNotification, isUserHAsPlanBoolean = false;
    private String askAQuestionDataPage = "";


    public ActNotificationLanding() {
        super(R.string.app_name);
    }

    public static AstrologerServiceInfo setDataModel(Context context, BeanHoroPersonalInfo beanHoroPersonalInfo, String payMode, ServicelistModal itemdetails) {
        AstrologerServiceInfo astrologerServiceInfo = new AstrologerServiceInfo();

        if (beanHoroPersonalInfo != null) {
            astrologerServiceInfo.setKey(CUtils.getApplicationSignatureHashCode(context));
            astrologerServiceInfo.setPayMode(payMode);

            String gender = beanHoroPersonalInfo.getGender();
            astrologerServiceInfo.setGender(gender);
            String email_id_for_question = UserEmailFetcher.getEmail(context);
            // email_id_for_question="tejatestliveapi@gmail.com";
            astrologerServiceInfo.setEmailID(email_id_for_question);

            String username = beanHoroPersonalInfo.getName();
            astrologerServiceInfo.setRegName(username); //Temporarily changes by chandan
            //astrologerServiceInfo.setRegName(email_id_for_question);

            BeanDateTime beanDateTime = beanHoroPersonalInfo.getDateTime();
            astrologerServiceInfo.setDateOfBirth(String.valueOf(beanDateTime.getDay()));
            astrologerServiceInfo.setMonthOfBirth(String.valueOf(beanDateTime.getMonth() + 1));
            astrologerServiceInfo.setYearOfBirth(String.valueOf(beanDateTime.getYear()));
            astrologerServiceInfo.setHourOfBirth(String.valueOf(beanDateTime.getHour()));
            astrologerServiceInfo.setMinOfBirth(String.valueOf(beanDateTime.getMin()));
            astrologerServiceInfo.setSecOfBirth(String.valueOf(beanDateTime.getSecond()));

            BeanPlace place = beanHoroPersonalInfo.getPlace();
            String city = place.getCityName();
            String state = place.getState();
            String country = place.getCountryName();
            astrologerServiceInfo.setPlace(city);
            astrologerServiceInfo.setNearCity(city);
            if (country.trim().equals("not define")) {
                astrologerServiceInfo.setState("");
                astrologerServiceInfo.setCountry("");
            } else {
                astrologerServiceInfo.setState(state);
                astrologerServiceInfo.setCountry(country);
            }
            if (place.getTimeZone() != null && !place.getTimeZone().isEmpty()) {
                astrologerServiceInfo.setTimezone(place.getTimeZone());
            } else {
                astrologerServiceInfo.setTimezone("5.5");
            }
            astrologerServiceInfo.setLatitude(place.getLatDeg() + place.getLatDir() + place.getLatMin());
            astrologerServiceInfo.setLongitude(place.getLongDeg() + place.getLongDir() + place.getLongMin());
            if (itemdetails != null)
                astrologerServiceInfo.setPriceRs(itemdetails.getPriceInRS());
            else
                astrologerServiceInfo.setPriceRs("");
        }
        return astrologerServiceInfo;
    }

    private static String getFormattedMessageText(String text) {
        String actualString = "";
        if (text.contains("(Girl")) {
            String[] arr = text.split("\\(Girl");
            actualString = arr[0];
        } else if (text.contains("^^")) {
            String[] arr = text.split("\\^\\^");
            actualString = arr[0];
        } else if (text.contains("##")) {
            String[] arr = text.split("##");
            actualString = arr[0];
        } else {
            actualString = text;
        }
        return actualString;
    }

    /*@author Tejinder Singh
      on 27-march-17 */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_landing);
        setBillingEventHandler(this);
        startAsyncTaskToGetProductDetail();
        initLayoutValues();
        initValues(getIntent());
        enableToolBar();
        initiallizeOblect();
        setClickListener();
        isNotification = getIntent().getBooleanExtra("isNotification", false);
        if (isNotification) {
            CUtils.googleAnalyticSendWitPlayServie(this, CGlobalVariables.GOOGLE_ANALYTIC_EVENT, CGlobalVariables.GOOGLE_ANALYTIC_LABEL_FOR_ASK_A_QUESTION_CHAT, null);
            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_LABEL_FOR_ASK_A_QUESTION_CHAT, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
        }

    }

    @Override
    protected void onNewIntent(Intent intent) {
        startAsyncTaskToGetProductDetail();
        initLayoutValues();
        initValues(intent);
        enableToolBar();
        initiallizeOblect();
        setClickListener();
        isNotification = intent.getBooleanExtra("isNotification", false);
        if (isNotification) {
            CUtils.googleAnalyticSendWitPlayServie(this, CGlobalVariables.GOOGLE_ANALYTIC_EVENT, CGlobalVariables.GOOGLE_ANALYTIC_LABEL_FOR_ASK_A_QUESTION_CHAT, null);
            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_LABEL_FOR_ASK_A_QUESTION_CHAT, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

        }
    }

    private void setClickListener() {
        askAQuestionNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //set Partner id
                CUtils.createSession(ActNotificationLanding.this, "SAQN");

                if (isNotification) {
                    Intent intent = new Intent(ActNotificationLanding.this, ActAskQuestion.class);
                    intent.putExtra("isNotification", true);
                    startActivity(intent);
                }
                ActNotificationLanding.this.finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (isNotification) {
            CUtils.gotoHomeScreen(ActNotificationLanding.this);
        } else {
            ActNotificationLanding.this.finish();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver,
                new IntentFilter(CGlobalVariables.COPA_RESULT));
    }

    @Override
    protected void onStop() {
        super.onStop();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);

    }

    public void saveDataOnInternalStorage(ArrayList<MessageDecode> chatMessageArrayList1) {
        Collections.reverse(chatMessageArrayList1);
        Intent intent = new Intent(ActNotificationLanding.this, SaveDataInternalStorage.class);
        intent.putParcelableArrayListExtra(CGlobalVariables.CHATWITHASTROLOGER, chatMessageArrayList1);
        intent.putExtra(CGlobalVariables.ISINSERT, false);
        (ActNotificationLanding.this).startService(intent);
    }

    private void startAsyncTaskToGetProductDetail() {
        fetchProductFromGoogleServer();
    }

    private void initLayoutValues() {
        toolBar_InputKundli = findViewById(R.id.tool_barAppModule);
        tabs_input_kundli = findViewById(R.id.tabs);
        astrologerRecyclerView = findViewById(R.id.astrologer_list);
        tvTitle = findViewById(R.id.tvTitle);
        askAQuestionNow = findViewById(R.id.askquestionnow);
        imgRefresh = findViewById(R.id.imgRefresh);
        // Added by Ankit on 6/3/2019
        /*LANGUAGE_CODE = ((AstrosageKundliApplication) ActNotificationLanding.this.getApplication())
                .getLanguageCode();*/
        imgRefresh.setVisibility(View.VISIBLE);
        imgRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (CUtils.isConnectedWithInternet(ActNotificationLanding.this)) {
                    getChatHistory();
                } else {
                    MyCustomToast mct = new MyCustomToast(ActNotificationLanding.this, ActNotificationLanding.this
                            .getLayoutInflater(), ActNotificationLanding.this, regularTypeface);
                    mct.show(getResources().getString(R.string.no_internet));
                }
            }
        });

        imgHome = findViewById(R.id.imgHome);
        imgHome.setVisibility(View.VISIBLE);
        imgHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CUtils.gotoHomeScreen(ActNotificationLanding.this);
            }
        });
    }

    private void initValues(Intent intent) {
        chatMessageArrayList = CUtils.getDataFromPrefrence(ActNotificationLanding.this);
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        Typeface typeface = CUtils.getRobotoFont(
                ActNotificationLanding.this, LANGUAGE_CODE, CGlobalVariables.regular);
        pd = new CustomProgressDialog(ActNotificationLanding.this, typeface);

        int planId = 1;

        planId = CUtils.getUserPurchasedPlanFromPreference(ActNotificationLanding.this);

        isUserHAsPlanBoolean = planId == CGlobalVariables.SILVER_PLAN_ID ||
                planId == CGlobalVariables.SILVER_PLAN_ID_5 ||
                planId == CGlobalVariables.SILVER_PLAN_ID_4 ||
                planId == CGlobalVariables.GOLD_PLAN_ID ||
                planId == CGlobalVariables.GOLD_PLAN_ID_7 ||
                planId == CGlobalVariables.GOLD_PLAN_ID_6 ||
                planId == CGlobalVariables.PLATINUM_PLAN_ID ||
                planId == CGlobalVariables.PLATINUM_PLAN_ID_9 ||
                planId == CGlobalVariables.PLATINUM_PLAN_ID_10 ||
                planId == CGlobalVariables.KUNDLI_AI_PRIMIUM_PLAN_ID_11;

        setAdapter();

        CUtils.saveIntData(ActNotificationLanding.this, CGlobalVariables.COUNTNOTIFICATIONCENTER, 0);
        itemdetails = (ServicelistModal) intent.getSerializableExtra("keyItemDetails");
        if (itemdetails == null) {
            if (!CUtils.isConnectedWithInternet(ActNotificationLanding.this)) {
                MyCustomToast mct = new MyCustomToast(ActNotificationLanding.this, ActNotificationLanding.this
                        .getLayoutInflater(), ActNotificationLanding.this, typeface);
                mct.show(getResources().getString(R.string.no_internet));
            } else {
                if (askAQuestionDataPage == null || askAQuestionDataPage.equals("")) {
                    askAQuestionDataPage = CGlobalVariables.ask_A_Question_Android;
                }
                //String cacheResult = CUtils.getStringData(ActAskQuestion.this, "ASK" + String.valueOf(LANGUAGE_CODE), "");
                String cacheResult = CUtils.getStringData(ActNotificationLanding.this, askAQuestionDataPage + LANGUAGE_CODE, "");
                if (cacheResult != null && !cacheResult.equals("")) {
                    Intent i = new Intent(ActNotificationLanding.this, AskAQueDataUpdateService.class);
                    i.putExtra(CGlobalVariables.ask_A_Question_Data, askAQuestionDataPage);
                    startService(i);
                    parseGsonData(cacheResult);
                } else {
                    //new ActAskQuestion.GetData().execute();
                }

                //  loadAstroShopData();
            }
        }
        tvTitle.setTypeface(this.regularTypeface);
        String titleText = getResources().getString(R.string.askaquestion);
        tvTitle.setText(titleText);
        askAQuestionNow.setTypeface(robotRegularTypeface);
        askAQuestionNow.setText(getResources().getString(R.string.asknow));
    }

    private void parseGsonData(String saveData) {

        try {
            List<ServicelistModal> data;
            Gson gson = new Gson();
            JsonElement element = gson.fromJson(saveData, JsonElement.class);
            data = gson.fromJson(saveData, new TypeToken<ArrayList<ServicelistModal>>() {
            }.getType());
            itemdetails = data.get(0);

        } catch (Exception ex) {

        }

    }

    private void setAdapter() {
        if (chatMessageArrayList != null) {
            notificationLandingRecyclerView = new NotificationLandingRecyclerView(chatMessageArrayList, ActNotificationLanding.this, false, ActNotificationLanding.this);
            astrologerRecyclerView.setAdapter(notificationLandingRecyclerView);
        } else {
            astrologerRecyclerView.setAdapter(null);
            chatMessageArrayList = new ArrayList<>();
            getChatHistory();
        }
        astrologerRecyclerView.setLayoutManager(new LinearLayoutManager(ActNotificationLanding.this));
    }

    private void enableToolBar() {
        setSupportActionBar(toolBar_InputKundli);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        tabs_input_kundli.setVisibility(View.GONE);
    }
    //Payment Code

    public void selectOptionPayment(int layoutPosition, String messageText, String chatID, MessageDecode messageDecodeSend) {
        this.messageDecodeSend = messageDecodeSend;
        query_for_question = messageDecodeSend.getMessageText();
        this.chatID = messageDecodeSend.getChatId();
        this.layoutPosition = layoutPosition;
        chatMessageArrayList = CUtils.getDataFromPrefrence(ActNotificationLanding.this);

       /* if(messageDecodeSend.getAstrologerServiceInfo() != null){
            astrologerServiceInfo = messageDecodeSend.getAstrologerServiceInfo();
            convertInJsonObj(astrologerServiceInfo);
        }*/

        // setAdapter();
        // Toast.makeText(this, "layoutpositon : "+layoutPosition+chatMessageArrayList.get(layoutPosition).getMessageText(), Toast.LENGTH_SHORT).show();
        boolean googleWalletPaymentVisibility = CUtils.getBooleanData(ActNotificationLanding.this, CGlobalVariables.key_GoogleWalletPaymentVisibility, true);
        boolean paytmPaymentVisibility = CUtils.getBooleanData(ActNotificationLanding.this, CGlobalVariables.key_PaytmPaymentVisibility, true);
        boolean razorPaymentVisibility = CUtils.getBooleanData(ActNotificationLanding.this, CGlobalVariables.key_RazorPayVisibilityServices, true);
        boolean paytmPaymentVisibilityService = CUtils.getBooleanData(ActNotificationLanding.this, CGlobalVariables.key_PaytmWalletVisibilityServices, true);

        int noOfQuestion = CUtils.getIntData(ActNotificationLanding.this, CGlobalVariables.NOOFQUESTIONAVAILABLE, 0);
        //int noOfQuestion=1;
        if (noOfQuestion > 0) {
            /*final CustomProgressDialog pd = new CustomProgressDialog(this, regularTypeface);
            pd.setCancelable(false);
            pd.show();*/
            showProgressBar();

            if (!CUtils.isConnectedWithInternet(ActNotificationLanding.this)) {
                MyCustomToast mct = new MyCustomToast(ActNotificationLanding.this, ActNotificationLanding.this
                        .getLayoutInflater(), ActNotificationLanding.this, regularTypeface);
                mct.show(getResources().getString(R.string.no_internet));
            } else {

                if (messageDecodeSend.getAstrologerServiceInfo() != null) {
                    astrologerServiceInfo = messageDecodeSend.getAstrologerServiceInfo();
                    if (astrologerServiceInfo != null) {
                        astrologerServiceInfo.setPayMode("Free");
                    }
                    convertInJsonObj(astrologerServiceInfo);
                }
                //  setDataModel(beanHoroPersonalInfo,"Google");
                //  convertInJsonObj(astrologerServiceInfo);
                // AstrologerServiceInfo serviceInfo = new Gson().fromJson(fullJsonDataObj, AstrologerServiceInfo.class);
                // serviceInfo.setPayMode("Free");
                //  if (serviceInfo.getEmailID() != null)
                //      emailid = serviceInfo.getEmailID();
                //  fullJsonDataObj = new Gson().toJson(serviceInfo);
                CUtils.saveBooleanData(ActNotificationLanding.this, CGlobalVariables.Type_PAYMENT, true);
                // saveDataOnInternalStorage();
                //SendUserPurchaseReportForServiceToServer(Context context, String purchaseData, String emailId, String userId, String price, String priceCurrencycode, String fullJsonDataObj, String layoutPosition,String messageText,String messageChatID)

                String price = "0";
                if (arayaskquestionPlan.get(price_amount_micros) != null)
                    price = arayaskquestionPlan.get(price_amount_micros);

                priceValue = price;
                SendUserPurchaseReportForServiceToServerForAstroChat sendObj = new SendUserPurchaseReportForServiceToServerForAstroChat(this, "", UserEmailFetcher.getEmail(this), CUtils.getUserName(this), price, "", fullJsonDataObj, "" + layoutPosition, query_for_question, chatID, "Order Free Question");
                sendObj.sendReportToServerForFreeQue();
            }


        } else {

            CUtils.saveBooleanData(ActNotificationLanding.this, CGlobalVariables.Type_PAYMENT, false);

            if (isUserHAsPlanBoolean) {
                //astrologerServiceInfo = CUtils.setDataModel(this, beanHoroPersonalInfo, "Paytm", servicelistModal);

                if (razorPaymentVisibility && !paytmPaymentVisibilityService) {
                    onSelectedOption(R.id.radioRazor, "");
                } else if (!razorPaymentVisibility && paytmPaymentVisibilityService) {
                    onSelectedOption(R.id.radioPaytm, "");
                } else {
                    Fragment diog = getSupportFragmentManager().findFragmentByTag("Dialog");
                    if (diog == null) {
                        ChooseServicePayOPtionDialog dialog = new ChooseServicePayOPtionDialog();
                        dialog.show(getSupportFragmentManager(), "Dialog");
                    }
                }
            } else {

                if (googleWalletPaymentVisibility && !razorPaymentVisibility && !paytmPaymentVisibility) {
                    onSelectedOption(R.id.radioGoogle, "");
                } else if (razorPaymentVisibility && !googleWalletPaymentVisibility && !paytmPaymentVisibility) {
                    onSelectedOption(R.id.radioRazor, "");
                } else if (paytmPaymentVisibility && !googleWalletPaymentVisibility && !razorPaymentVisibility) {
                    onSelectedOption(R.id.radioPaytm, "");
                } else {
                    Fragment diog = getSupportFragmentManager().findFragmentByTag("Dialog");
                    if (diog == null) {
                        ChoosePayOptionFragmentDailog dialog = new ChoosePayOptionFragmentDailog();
                        dialog.show(getSupportFragmentManager(), "Dialog");
                    }
                }
            }
        }

    }

    private void updateRecyclerViewMessages() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getMessageList();
                //astrologerRecyclerView.scrollToPosition(notificationLandingRecyclerView.getItemCount() - 1);
            }
        }, 1000);
    }

    private void getMessageList() {
        chatMessageArrayList = CUtils.getDataFromPrefrence(this);
        if (chatMessageArrayList != null) {
            notificationLandingRecyclerView = new NotificationLandingRecyclerView(chatMessageArrayList, this, false, ActNotificationLanding.this);
            astrologerRecyclerView.setAdapter(notificationLandingRecyclerView);
        } else {
            astrologerRecyclerView.setAdapter(null);
            chatMessageArrayList = new ArrayList<>();
        }
        astrologerRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    private void convertInJsonObj(AstrologerServiceInfo localOrderModal) {
        localOrderModal.setPrtnr_id(CGlobalVariables.currentSession);
        Gson gson = new Gson();
        fullJsonDataObj = gson.toJson(localOrderModal);


        System.out.println("obj" + fullJsonDataObj);

    }

    @Override
    public void onSelectedOption(int opt, String mob) {
        //fullJsonDataObj=getFullJSONData();
       /* if(messageDecodeSend.getAstrologerServiceInfo() != null){
            astrologerServiceInfo = messageDecodeSend.getAstrologerServiceInfo();
            convertInJsonObj(astrologerServiceInfo);
        }*/

        //   setDataModel(beanAskAQue);
        // convertInJsonObj(astrologerServiceInfo);
        //String jsonFullAstrosageData=convertInJsonObj(astrologerServiceInfo);
        //  setMessageDataAskByUser();
        //  mob=  CUtils.getStringData(ActNotificationLanding.this, CGlobalVariables.SAVEMOBILENUMBER,"");

        if (messageDecodeSend.getAstrologerServiceInfo() != null) {
            astrologerServiceInfo = messageDecodeSend.getAstrologerServiceInfo();
            if (opt == R.id.radioGoogle) {
                if (astrologerServiceInfo != null) {
                    astrologerServiceInfo.setPayMode("Google");
                }
            } else if (opt == R.id.radioRazor) {
                if (astrologerServiceInfo != null) {
                    astrologerServiceInfo.setPayMode("RazorPay");
                }
            } else {
                if (astrologerServiceInfo != null) {
                    astrologerServiceInfo.setPayMode("Paytm");
                }
            }
            convertInJsonObj(astrologerServiceInfo);
        }
        
        if (opt == R.id.radioGoogle) {

            isPaymentDone = false;
            CUtils.saveBooleanData(ActNotificationLanding.this, CGlobalVariables.PAYMENTDONE_KEY_FOR_ASTRO_SERVICES_ISPAYMENTDONE, isPaymentDone);
            CUtils.saveStringData(ActNotificationLanding.this, CGlobalVariables.PAYMENTDONE_KEY_FOR_ASTRO_SERVICES_JSONOBJ, fullJsonDataObj);
            //purchaseServiceByInApp();

            Typeface typeface = CUtils.getRobotoFont(
                    ActNotificationLanding.this, LANGUAGE_CODE, CGlobalVariables.regular);
            RequestQueue queue = VolleySingleton.getInstance(ActNotificationLanding.this).getRequestQueue();

            if (!CUtils.isConnectedWithInternet(ActNotificationLanding.this)) {
                MyCustomToast mct = new MyCustomToast(ActNotificationLanding.this, ActNotificationLanding.this
                        .getLayoutInflater(), ActNotificationLanding.this, typeface);
                mct.show(getResources().getString(R.string.no_internet));
            } else {
                CUtils.getOrderIDAndChatId(ActNotificationLanding.this, typeface, queue, itemdetails, fullJsonDataObj, astrologerServiceInfo, chatID);
            }

        } else if (opt == R.id.radioRazor) {
            CUtils.googleAnalyticSendWitPlayServie(ActNotificationLanding.this,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                    CGlobalVariables.GOOGLE_ANALYTIC_ASKAQUESTION_RAZORPAY, null);
            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_ASKAQUESTION_RAZORPAY, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

            Typeface typeface = CUtils.getRobotoFont(
                    ActNotificationLanding.this, LANGUAGE_CODE, CGlobalVariables.regular);
            RequestQueue queue = VolleySingleton.getInstance(ActNotificationLanding.this).getRequestQueue();

            if (!CUtils.isConnectedWithInternet(ActNotificationLanding.this)) {
                MyCustomToast mct = new MyCustomToast(ActNotificationLanding.this, ActNotificationLanding.this
                        .getLayoutInflater(), ActNotificationLanding.this, typeface);
                mct.show(getResources().getString(R.string.no_internet));
            } else {

                if (messageDecodeSend.getChatId() == null) {
                    messageDecodeSend.setChatId("");
                }
                CUtils.getOrderIDAndChatId(ActNotificationLanding.this, typeface, queue, itemdetails, fullJsonDataObj, astrologerServiceInfo, messageDecodeSend.getChatId());
                // CUtils.getOrderID(ActNotificationLanding.this, typeface, queue, astrologerServiceInfo);
            }
        } else {
            LANGUAGE_CODE = ((AstrosageKundliApplication) ActNotificationLanding.this.getApplication())
                    .getLanguageCode();

            Typeface typeface = CUtils.getRobotoFont(
                    ActNotificationLanding.this, LANGUAGE_CODE, CGlobalVariables.regular);
            RequestQueue queue = VolleySingleton.getInstance(ActNotificationLanding.this).getRequestQueue();

            CUtils.googleAnalyticSendWitPlayServie(this,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                    CGlobalVariables.GOOGLE_ANALYTIC_ASKAQUESTION_PAYTM, null);
            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_ASKAQUESTION_PAYTM, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

            if (!CUtils.isConnectedWithInternet(ActNotificationLanding.this)) {
                MyCustomToast mct = new MyCustomToast(ActNotificationLanding.this, ActNotificationLanding.this
                        .getLayoutInflater(), ActNotificationLanding.this, typeface);
                mct.show(getResources().getString(R.string.no_internet));
            } else {
                //   astrologerServiceInfo.setPayMode("Paytm");
                //  CUtils.getOrderID(ActNotificationLanding.this, typeface, queue, astrologerServiceInfo);
                // getAstrochatPrice();
                if (messageDecodeSend.getChatId() == null) {
                    messageDecodeSend.setChatId("");
                }
                CUtils.getOrderIDAndChatId(ActNotificationLanding.this, typeface, queue, itemdetails, fullJsonDataObj, astrologerServiceInfo, messageDecodeSend.getChatId());
            }
        }

    }

    @Override
    public void getCallBack(String result, CUtils.callBack callback, String priceInDollor, String priceInRs) {
        if (callback == CUtils.callBack.GET_ORDER_ID) {
            order_Id = result;
            //com.google.analytics.tracking.android.//Log.e("order" + order_Id);
            if (order_Id != null && !order_Id.isEmpty()) {

                if (priceInRs != null && priceInRs.length() > 0) {
                    astrologerServiceInfo.setPriceRs(priceInRs);
                }

                if (priceInDollor != null && priceInDollor.length() > 0) {
                    astrologerServiceInfo.setPrice(priceInDollor);
                }
                if (astrologerServiceInfo.getPayMode().equalsIgnoreCase("Google")) {
                    purchaseServiceByInApp();
                } else if (astrologerServiceInfo.getPayMode().equalsIgnoreCase("RazorPay")) {
                    Double amount = Double.parseDouble(astrologerServiceInfo.getPriceRs());
                    Double paiseAmount = amount * 100;
                    // API calling
                    //showProgressBar();
                    //new VolleyServerRequest(this, (OnVolleyResultListener) ActNotificationLanding.this, CGlobalVariables.RAZORPAY_ORDERID_URL, CUtils.getRazorOrderIdParams(paiseAmount,"INR",order_Id));
                    goToRazorPayFlow();
                } else {
                    new GetCheckSum(ActNotificationLanding.this, regularTypeface).getCheckSum(getchecksumparams(), 0);

                    //CUtils.getCheckSum(ActNotificationLanding.this, getchecksumparams(), regularTypeface);
                }
            } else {
                MyCustomToast mct = new MyCustomToast(ActNotificationLanding.this,
                        ActNotificationLanding.this.getLayoutInflater(),
                        ActNotificationLanding.this, regularTypeface);
                mct.show(getResources().getString(R.string.order_fail));
            }
        } else if (callback == CUtils.callBack.GET_CHECKSUM) {
            String checksum = result;
            if (!result.isEmpty()) {
                startPaytmPayment(order_Id, astrologerServiceInfo.getPriceRs(), checksum);
            } else {
                MyCustomToast mct = new MyCustomToast(ActNotificationLanding.this,
                        ActNotificationLanding.this.getLayoutInflater(),
                        ActNotificationLanding.this, regularTypeface);
                mct.show(getResources().getString(R.string.order_fail));
            }
        } else if (callback == CUtils.callBack.POST_STATUS) {
            String postStatus = result;
            if (postStatus != null && postStatus.equalsIgnoreCase("1")) {

                if (payStatus.equalsIgnoreCase("1")) {
                    updateRecyclerViewMessages();

                    notifyNotificationAdapter();

                    onPurchaseCompleted(itemdetails, order_Id);

                } else {
                    CUtils.googleAnalyticSendWitPlayServie(this,
                            CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                            CGlobalVariables.GOOGLE_ANALYTIC_PAYMENT_FAILED_PAYTM, null);
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_PAYMENT_FAILED_PAYTM, CGlobalVariables.FIREBASE_EVENT_PAYMENT_FAILED, "");

                    // openPaymentFailedDialog();

                    MyCustomToast mct = new MyCustomToast(ActNotificationLanding.this,
                            ActNotificationLanding.this.getLayoutInflater(),
                            ActNotificationLanding.this, regularTypeface);
                    mct.show(getResources().getString(R.string.payment_failure_dialog_title));
                }

            } else {
                MyCustomToast mct = new MyCustomToast(ActNotificationLanding.this,
                        ActNotificationLanding.this.getLayoutInflater(),
                        ActNotificationLanding.this, regularTypeface);
                mct.show(getResources().getString(R.string.order_fail));


            }
        } else if (callback == CUtils.callBack.POST_RAZORPAYSTATUS) {
            String postStatus = result;
            if (postStatus != null && postStatus.equalsIgnoreCase("1")) {
                if (payStatus.equalsIgnoreCase("1")) {

                    //CUtils.emailPDF(ActNotificationLanding.this, CGlobalVariables.RAZORPAY_EMAIL_PDF, mediumTypeface, queue, order_Id, payStatus, astrologerServiceInfo);
                    updateRecyclerViewMessages();
                    notifyNotificationAdapter();
                    onPurchaseCompleted(itemdetails, order_Id);

                } else {
                    //openPaymentFailedDialog();
                    CUtils.googleAnalyticSendWitPlayServie(this,
                            CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                            CGlobalVariables.GOOGLE_ANALYTIC_SERVICE_RAZOR_PAYMENT_FAILED, null);
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SERVICE_RAZOR_PAYMENT_FAILED, CGlobalVariables.FIREBASE_EVENT_PAYMENT_FAILED, "");

                    MyCustomToast mct = new MyCustomToast(ActNotificationLanding.this,
                            ActNotificationLanding.this.getLayoutInflater(),
                            ActNotificationLanding.this, regularTypeface);
                    mct.show(getResources().getString(R.string.payment_failure_dialog_title));
                }

            } else {
                MyCustomToast mct = new MyCustomToast(ActNotificationLanding.this,
                        ActNotificationLanding.this.getLayoutInflater(),
                        ActNotificationLanding.this, regularTypeface);
                mct.show(getResources().getString(R.string.order_fail));
            }
        }
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
            options.put("description", getResources().getString(R.string.askaquestion));
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
            preFill.put("email", astrologerServiceInfo.getEmailID().trim());
            preFill.put("contact", astrologerServiceInfo.getMobileNo().trim());
            options.put("prefill", preFill);


            JSONObject notes = new JSONObject();
            //added by Ankit on 17-5-2019 for Razorpay Webhook
            notes.put("orderId", order_Id);
            notes.put("chatId", "0");
            notes.put("orderType", CGlobalVariables.PAYMENT_TYPE_PRODUCT);
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
    public void getCallBackForChat(String[] result, CUtils.callBack callback, String priceInDollor, String priceInRs) {
        if (callback == CUtils.callBack.GET_ORDER_ID) {
            order_Id = result[0];
            chat_Id = result[1];
            //com.google.analytics.tracking.android.//Log.e("order" + order_Id);
            if (order_Id != null && !order_Id.isEmpty() && chat_Id != null && !chat_Id.isEmpty()) {

                if (priceInRs != null && priceInRs.length() > 0) {
                    astrologerServiceInfo.setPriceRs(priceInRs);
                }

                if (priceInDollor != null && priceInDollor.length() > 0) {
                    astrologerServiceInfo.setPrice(priceInDollor);
                }
                CUtils.updatePaidStatus(ActNotificationLanding.this, layoutPosition, order_Id, "0", chat_Id, astrologerServiceInfo);
                if (astrologerServiceInfo.getPayMode().equalsIgnoreCase("Google")) {
                    purchaseServiceByInApp();
                } else if (astrologerServiceInfo.getPayMode().equalsIgnoreCase("RazorPay")) {
                    Double amount = Double.parseDouble(astrologerServiceInfo.getPriceRs());
                    //Need to send amount to razor pay in paise
                    Double paiseAmount = amount * 100;
                    // API calling
                    //showProgressBar();
                    //new VolleyServerRequest(this, (OnVolleyResultListener) ActNotificationLanding.this, CGlobalVariables.RAZORPAY_ORDERID_URL, CUtils.getRazorOrderIdParams(paiseAmount,"INR",order_Id));
                    goToRazorPayFlow();
                } else {
                    new GetCheckSum(ActNotificationLanding.this, regularTypeface).getCheckSum(getchecksumparams(), 1);
                    //CUtils.getCheckSum(ActNotificationLanding.this, getchecksumparams(), regularTypeface);
                }
               /* PaytmPGService Service = PaytmPGService.getProductionService();
                CUtils.startPaytmPayment(Service, ActNotificationLanding.this, astrologerServiceInfo.getEmailID(), astrologerServiceInfo.getMobileNo(), order_Id, itemdetails.getPriceInRS(),"checksumhere");
*/
            } else {
                MyCustomToast mct = new MyCustomToast(ActNotificationLanding.this,
                        ActNotificationLanding.this.getLayoutInflater(),
                        ActNotificationLanding.this, regularTypeface);
                mct.show(getResources().getString(R.string.order_fail));
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Log.e("Bijendra", String.valueOf(requestCode));
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case CGlobalVariables.REQUEST_CODE_PAYTM: {
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
                break;
            }

            default:
                break;
        }
    }

    @Override
    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {
        // Logic from onActivityResult should be moved here.
        String astroSageUserId = CUtils.getUserName(this);
        dumpDataString = dumpDataString + " onPurchasesUpdated() responseCode=" + billingResult.getResponseCode() + " astroSageUserId=" + astroSageUserId;
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
            dumpDataString = dumpDataString + " ResponseOK";
            Log.e("BillingClient", "onPurchasesUpdated() OK");
            if (purchases != null && !purchases.isEmpty()) {
                Purchase purchase = purchases.get(0);
                dumpDataString = dumpDataString + " signature=" + purchase.getSignature() + " purchaseData=" + purchase.getOriginalJson();
                processGooglePaymentAfterSuccess(purchase);
            }
        } else {
            dumpDataString = dumpDataString + " ResponseFAIL";
            Log.e("BillingClient", "onPurchasesUpdated() FAIL = " + purchases);

            CUtils.googleAnalyticSendWitPlayServie(this,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                    CGlobalVariables.GOOGLE_ANALYTIC_PAYMENT_FAILED, null);
            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_PAYMENT_FAILED, CGlobalVariables.FIREBASE_EVENT_PAYMENT_FAILED, "");

            String price = "0";
            if (arayaskquestionPlan.get(price_amount_micros) != null)
                price = arayaskquestionPlan.get(price_amount_micros);

            if(!TextUtils.isEmpty(chat_Id)){
                chatID = chat_Id;
            }

            SendUserPurchaseReportForServiceToServerForAstroChat sendObj = new SendUserPurchaseReportForServiceToServerForAstroChat(this, "", CUtils.getStringData(this, CGlobalVariables.USERREGISTEREMAILID, UserEmailFetcher.getEmail(this)), CUtils.getUserName(this), price, "", fullJsonDataObj, "" + layoutPosition, query_for_question, chatID, "",order_Id);
            sendObj.sendReportToServer();
            CUtils.saveBooleanData(this,CGlobalVariables.IS_SUBSCRIPTION_ERROR,true);

        }
        CUtils.postDumpDataToServer(ActNotificationLanding.this, dumpDataString);
        //Log.e("BillingClient", "onPurchasesUpdated() FAIL purchases size="+purchases.size());
    }

    private void processGooglePaymentAfterSuccess(Purchase purchase) {
        try {
            String price = "0";
            String priceCurrencycode = "INR";
            SavePlaninPreference(ASK_QUESTION_PLAN);
            String plan = CGlobalVariables.ASK_QUESTION_PLAN_VALUE;
            price = arayaskquestionPlan.get(price_amount_micros);// 2
            priceCurrencycode = arayaskquestionPlan.get(price_currency_code);
            isPaymentDone = true;
            CUtils.saveBooleanData(ActNotificationLanding.this, CGlobalVariables.PAYMENTDONE_KEY_FOR_ASTRO_SERVICES_ISPAYMENTDONE, isPaymentDone);

            double dPrice = 0.0;
            try {
                if (price != null && price.length() > 0) {
                    dPrice = Double.valueOf(price);
                    dPrice = (dPrice / CGlobalVariables.PRICE_IN_ONE_UNIT);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            CUtils.googleAnalyticSendWitPlayServieForPurchased(this,
                    CGlobalVariables.GOOGLE_ANALYTIC_ASTROSHOP_SERVICE,
                    CGlobalVariables.GOOGLE_ANALYTIC_PAYMENT_SUCCESS, null, dPrice, "");

            gotoThanksPage(purchase, plan, price, priceCurrencycode);
        } catch (Exception e) {
            Log.e("BillingClient", "AfterSuccess exp=" + e);
        }
    }

    private void gotoThanksPage(Purchase purchase, String plan, String price, String priceCurrencycode) {
        signature = purchase.getSignature();
        purchaseData = purchase.getOriginalJson();
        //
        ActNotificationLanding.this.getSharedPreferences("MISC_PUR_SERVICE", Context.MODE_PRIVATE).edit()
                .putString("VALUE_SERVICE", purchaseData).commit();
        String astroSageUserId = CUtils.getUserName(ActNotificationLanding.this);
        saveDataInPreferences(astroSageUserId, price, priceCurrencycode);

        verifyPurchaseFromService(astroSageUserId, price, priceCurrencycode, fullJsonDataObj);
        // update status
        CUtils.updatePaidStatus(ActNotificationLanding.this, layoutPosition, "", "1", chatID, astrologerServiceInfo);
        notifyNotificationAdapter();
    }

    private void verifyPurchaseFromService(String astroSageUserId, String price, String priceCurrencycode, String fullJsonDataObj) {

        this.getSharedPreferences("MISC_PUR_SERVICE", Context.MODE_PRIVATE).edit()
                .putString("VALUE_SERVICE", purchaseData).commit();

        saveDataInPreferences(astroSageUserId, price, priceCurrencycode);
        Intent pvsIntent = new Intent(this,
                VerificationServiceForInAppBillingChat.class);
        pvsIntent.putExtra("SIGNATURE", signature);
        pvsIntent.putExtra("PURCHASE_DATA", purchaseData);
        pvsIntent.putExtra("DEVELOPER_PAYLOAD", developerPayload);
        pvsIntent.putExtra("ASTRO_USERID", astroSageUserId);
        String textMsg = query_for_question;
        /*if (spouseFullDetail != null&&) {
            textMsg = textMsg + "<>" + CUtils.convertInJsonObj(beanHoroPersonalInfo2);
        }*/

        if(!TextUtils.isEmpty(chat_Id)){
            chatID = chat_Id;
        }

        pvsIntent.putExtra("MESSAGE_TEXT", textMsg);
        pvsIntent.putExtra("MESSAGE_CHAT_ID", chatID);
        pvsIntent.putExtra("ORDER_ID", order_Id);
        pvsIntent.putExtra("price", price);
        pvsIntent.putExtra("priceCurrencycode", priceCurrencycode);
        pvsIntent.putExtra("messageTitle", "Order Update");
        pvsIntent.putExtra("FullJsonDataObj", fullJsonDataObj);
        pvsIntent.putExtra("layoutPostion", "" + layoutPosition);
        startService(pvsIntent);

        CUtils.saveStringData(getApplicationContext(), "MISC_PUR_SERVICE_LAYOUT_POSITION", "" + layoutPosition);
        CUtils.saveStringData(getApplicationContext(), "MISC_PUR_SERVICE_MSG_TEXT", "" + textMsg);
        CUtils.saveStringData(getApplicationContext(), "MISC_PUR_SERVICE_MSG_CHAT_ID", chatID);
    }

    private void saveDataInPreferences(String astroSageUserId, String price, String priceCurrencycode) {
        SharedPreferences purchasageDataPlan = getSharedPreferences(CGlobalVariables.ASTROSAGEPURCHASEPLANFORSERVICE, Context.MODE_PRIVATE);
        SharedPreferences.Editor purchasePlanEditor = purchasageDataPlan.edit();
        AppPurchaseDataSaveModelClass appPurchaseDataSaveModelClass = CUtils.getObjectSavePref(signature, purchaseData, developerPayload, astroSageUserId, price, priceCurrencycode);
        Gson gson = new Gson();
        String purchaseDataJSONObject = gson.toJson(appPurchaseDataSaveModelClass); // myObject - instance of MyObject
        purchasePlanEditor.putString(CGlobalVariables.ASTROSAGEPURCHASEPLANFORSERVICEOBJECT, purchaseDataJSONObject);
        purchasePlanEditor.commit();
    }

    private void purchaseServiceByInApp() {
        dumpDataString = "ActNotificationLanding purchaseServiceByInApp() fullJsonDataObj=" + fullJsonDataObj;
        gotBuyAskQuestionPlan();
    }

    public void gotBuyAskQuestionPlan() {
        try {
            dumpDataString = dumpDataString + " gotBuyAskQuestionPlan()";
            CUtils.googleAnalyticSendWitPlayServie(ActNotificationLanding.this,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                    CGlobalVariables.GOOGLE_ANALYTIC_ASKAQUESTION_GOOGLE_WALLET, null);
            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_ASKAQUESTION_GOOGLE_WALLET, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

            if (askAQSkuDetails != null) {
                ImmutableList productDetailsParamsList =
                        ImmutableList.of(
                                BillingFlowParams.ProductDetailsParams.newBuilder()
                                        // retrieve a value for "productDetails" by calling queryProductDetailsAsync()
                                        .setProductDetails(askAQSkuDetails)
                                        .build()
                        );

                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                        .setProductDetailsParamsList(productDetailsParamsList)
                        .build();

                // Launch the billing flow
                int responseCode = AstrosageKundliApplication.billingClient.launchBillingFlow(this, billingFlowParams).getResponseCode();
                dumpDataString = dumpDataString + " gotBuyAskQuestionPlan() prepayment responseCode=" + responseCode;
                Log.e("BillingClient", "dumpDataString=" + dumpDataString);
                if (responseCode != BillingClient.BillingResponseCode.OK) {
                    Toast.makeText(ActNotificationLanding.this, "Fail to purchase", Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            //com.google.analytics.tracking.android.Log.e(e.getMessage());
        }
        CUtils.postDumpDataToServer(ActNotificationLanding.this, dumpDataString);
    }

    private void SavePlaninPreference(int newPlanIndex) {
        SharedPreferences sharedPreferences = ActNotificationLanding.this
                .getSharedPreferences(CGlobalVariables.APP_PREFS_NAME_FOR_SERVICE,
                        Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        if (newPlanIndex == ASK_QUESTION_PLAN) {
            sharedPrefEditor.putString(CGlobalVariables.APP_PREFS_Plan_for_service,
                    CGlobalVariables.ASK_QUESTION_PLAN_VALUE);
        }
        sharedPrefEditor.commit();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        notifyNotificationAdapter();
    }

    private String initiallizeOblect() {
        CUtils.getStringData(ActNotificationLanding.this, CGlobalVariables.USERPROFILEASTROCHAT, fullJsonDataObj);
        return fullJsonDataObj;
    }

    @Override
    public void showDialog(final int pos, final String chatId) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.lay_ask_delete_chat);

        TextView tvDelete = dialog.findViewById(R.id.tvDelete);
        tvDelete.setTypeface(mediumTypeface);

        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                conformationDialog(pos, chatId);
            }
        });

        dialog.show();
    }

    private void conformationDialog(final int pos, final String chatId) {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(true);
        dialog.setContentView(R.layout.lay_confirmation_delete_chat);

        Button btnDelete = dialog.findViewById(R.id.btnDelete);
        Button btnCancel = dialog.findViewById(R.id.btnCancel);
        TextView textViewHeading = dialog.findViewById(R.id.textViewHeading);
        TextView tvContent = dialog.findViewById(R.id.tvContent);

        btnDelete.setTypeface(mediumTypeface);
        btnCancel.setTypeface(mediumTypeface);
        textViewHeading.setTypeface(robotMediumTypeface);
        tvContent.setTypeface(robotRegularTypeface);
        //

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteChat(pos, chatId);
                dialog.dismiss();
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            double dPrice = 0.0;
            String pricee = "0";
            try {
                if (astrologerServiceInfo != null) {
                    if (astrologerServiceInfo.getPriceRs() != null && astrologerServiceInfo.getPriceRs().length() > 0) {
                        pricee = astrologerServiceInfo.getPriceRs();
                        dPrice = Double.valueOf(pricee);
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
            RequestQueue queue = VolleySingleton.getInstance(ActNotificationLanding.this).getRequestQueue();

            CUtils.updatePaidStatus(ActNotificationLanding.this, layoutPosition, order_Id, payStatus, chat_Id, astrologerServiceInfo);
            CUtils.postAskQuestionRazorpayDataToServer(ActNotificationLanding.this, mediumTypeface, queue, order_Id, chat_Id, payStatus, astrologerServiceInfo, CGlobalVariables.UPDATE_RAZORPAY_STATUS_CHAT, payId, "");

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
            RequestQueue queue = VolleySingleton.getInstance(ActNotificationLanding.this).getRequestQueue();
            CUtils.saveBooleanData(this,CGlobalVariables.IS_SUBSCRIPTION_ERROR,true);
            CUtils.updatePaidStatus(ActNotificationLanding.this, layoutPosition, order_Id, payStatus, chat_Id, astrologerServiceInfo);
            CUtils.postAskQuestionRazorpayDataToServer(ActNotificationLanding.this, mediumTypeface, queue, order_Id, chat_Id, payStatus, astrologerServiceInfo, CGlobalVariables.UPDATE_RAZORPAY_STATUS_CHAT, "", status);

            // Toast.makeText(this, "Payment failed: " + i + " " + response, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            //Log.e("Service pay", "Exception in onPaymentError", e);
        }
    }

    private void fetchProductFromGoogleServer() {
        Log.e("BillingClient", "fetchProductFromGoogleServer()");
        QueryProductDetailsParams queryProductDetailsParams =
                QueryProductDetailsParams.newBuilder()
                        .setProductList(ImmutableList.of(QueryProductDetailsParams.Product.newBuilder()
                                .setProductId(SKU_ASKQUESTION_PLAN)
                                .setProductType(BillingClient.ProductType.INAPP)
                                .build())).build();

        AstrosageKundliApplication.billingClient.queryProductDetailsAsync(
                queryProductDetailsParams, (billingResult, productDetailsList) -> {
                    int response = billingResult.getResponseCode();
                    Log.e("BillingClient", "onProductDetailsResponse() response=" + response);
                    if (response == BillingClient.BillingResponseCode.OK) {
                        for (ProductDetails productDetails : productDetailsList) {
                            intiProductPlan(productDetails);
                        }
                    } else {
                        showMsg(response);
                    }
                }
        );
    }

    private void showMsg(final int response) {
        ActNotificationLanding.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (response >= 0) {
                    //Toast.makeText(ActNotificationLanding.this, errorResponse[response], Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void intiProductPlan(ProductDetails productDetails) {
        Log.e("BillingClient", "intiProductPlan() productDetails=" + productDetails.getName());
        try {
            if (productDetails.getProductId().equalsIgnoreCase(SKU_ASKQUESTION_PLAN)) {
                arayaskquestionPlan.add(productDetails.getProductId());// 0
                arayaskquestionPlan.add(productDetails.getTitle());// 1
                arayaskquestionPlan.add(productDetails.getOneTimePurchaseOfferDetails().getFormattedPrice()); // 2
                arayaskquestionPlan.add(productDetails.getDescription());// 3
                arayaskquestionPlan.add(productDetails.getProductType());// 4
                arayaskquestionPlan.add(productDetails.getOneTimePurchaseOfferDetails().getPriceAmountMicros() + "");//5
                arayaskquestionPlan.add(productDetails.getOneTimePurchaseOfferDetails().getPriceCurrencyCode());
                askAQSkuDetails = productDetails;
            }

        } catch (Exception e) {
            System.out.print(e.getMessage());
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

    public void onPurchaseCompleted(ServicelistModal itemdetails, String order_id) {

        CUtils.trackEcommerceProduct(ActNotificationLanding.this, itemdetails.getServiceId(), itemdetails.getTitle(), itemdetails.getPriceInRS(), order_id, "INR", CGlobalVariables.Ecommerce_Paytm_Purchase, "In-App Store", "0", CGlobalVariables.TOPIC_ASKAQUESTION);
        Log.e("Purchase plan End", "");
    }

    public void displayRateDialog() {
        String Headingtext = ActNotificationLanding.this
                .getString(R.string.app_mainheading_text_kundali);
        String SubHeadingtext = getResources().getString(R.string.rate_app_chat_text);//ActNotificationLanding.this.getString(R.string.app_subheading_text_kundali);
        String Subchildheading = ActNotificationLanding.this
                .getString(R.string.app_subchild_text_ask_a_question);
        AppRater.app_launched(ActNotificationLanding.this, Headingtext,
                SubHeadingtext, Subchildheading);
    }

    private void notifyNotificationAdapter() {
        if (notificationLandingRecyclerView != null) {
            notificationLandingRecyclerView.notifyDataSetChanged();
        }
    }

    private void deleteChat(int pos, String chatId) {
        if (chatId == null) {
            deleteChatFromLocal(pos);
        } else {
            if (CUtils.isConnectedWithInternet(ActNotificationLanding.this)) {
                deleteChatFromServer(pos, chatId);
            } else {
                MyCustomToast mct = new MyCustomToast(ActNotificationLanding.this, ActNotificationLanding.this
                        .getLayoutInflater(), ActNotificationLanding.this, regularTypeface);
                mct.show(getResources().getString(R.string.no_internet));
            }

        }
    }

    private void deleteChatFromLocal(int pos) {
        if (chatMessageArrayList != null) {
            chatMessageArrayList.remove(pos);
            ArrayList<MessageDecode> chatMessageArrayList1 = new ArrayList<>(chatMessageArrayList);
            saveDataOnInternalStorage(chatMessageArrayList1);
            notifyNotificationAdapter();
        }
    }

    private void deleteChatFromServer(final int pos, final String chatId) {
        if (this.pd != null && !this.pd.isShowing()) {
            this.pd.show();
            this.pd.setCancelable(false);
        }
        StringRequest stringRequest = new StringRequest(Request.Method.POST, CGlobalVariables.DELETE_ASK_A_QUES_CHAT_HISTORY, new Response.Listener<String>() {
            public void onResponse(String response) {
                hideProgressBar();
                Log.e("order id response", response);
                try {
                    JSONObject obj = new JSONArray(response).getJSONObject(0);
                    String status = obj.getString("Result");

                    //1= sucessfully deleted, 4= Chatid not found on server
                    if (status.equals("1") || status.equals("4")) {
                        deleteChatFromLocal(pos);
                        MyCustomToast mct = new MyCustomToast(ActNotificationLanding.this, ActNotificationLanding.this
                                .getLayoutInflater(), ActNotificationLanding.this, robotRegularTypeface);
                        mct.show(getResources().getString(R.string.sucessfully_deleted));
                    } else {

                        MyCustomToast mct = new MyCustomToast(ActNotificationLanding.this, ActNotificationLanding.this
                                .getLayoutInflater(), ActNotificationLanding.this, robotRegularTypeface);
                        mct.show(getResources().getString(R.string.server_error));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                // Log.e("Error Through" + error.getMessage());
                VolleyLog.d("VolleyError: " + error.getMessage());
                //   mTextView.setText("That didn't work!");
                if (error instanceof TimeoutError) {
                    VolleyLog.d("TimeoutError: " + error.getMessage());
                } else if (error instanceof NoConnectionError) {
                    VolleyLog.d("NoConnectionError: " + error.getMessage());
                } else if (error instanceof AuthFailureError) {
                    VolleyLog.d("AuthFailureError: " + error.getMessage());
                    //TODO
                } else if (error instanceof ServerError) {
                    //TODO
                    VolleyLog.d("ServerError: " + error.getMessage());
                } else if (error instanceof NetworkError) {
                    //TODO
                    VolleyLog.d("NetworkError: " + error.getMessage());
                } else if (error instanceof ParseError) {
                    //TODO
                    VolleyLog.d("ParseError: " + error.getMessage());

                }
                hideProgressBar();
            }
        }
        ) {
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            public Map<String, String> getParamsNew() {

                HashMap<String, String> headers = new HashMap();
                headers.put("Key", CUtils.getApplicationSignatureHashCode(ActNotificationLanding.this));
                headers.put("chatid", chatId);
                return headers;
            }

        };
        // com.google.analytics.tracking.android.Log.e("API HIT HERE");
        int socketTimeout = 60000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        stringRequest.setShouldCache(true);
        this.queue.add(stringRequest);
    }

    private void getChatHistory() {
        if (this.pd != null && !this.pd.isShowing()) {
            this.pd.show();
            this.pd.setCancelable(false);
        }


        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                StringRequest stringRequest = new StringRequest(Request.Method.POST, CGlobalVariables.CHAT_HISTORY,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                Log.e("Response+", response);
                                if (response != null && !response.isEmpty()) {
                                    try {
                                        String str = new String(response.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                                        JSONArray jsonArray = new JSONArray(str);
                                        JSONObject obj = jsonArray.getJSONObject(0);
                                        if (obj.getString("Result") != null && (obj.getString("Result").equalsIgnoreCase("1"))) {
                                            String chatHistory = obj.getString("GotChatHistory");
                                            chatMessageArrayList.clear();
                                            chatMessageArrayList = new Gson().fromJson(chatHistory.trim(), new TypeToken<ArrayList<MessageDecode>>() {
                                            }.getType());
                                            if (chatMessageArrayList.size() > 0) {
                                                Log.e("Count is: ", "" + chatMessageArrayList.size());
                                                //int count=chatMessageArrayList.size();
                                                // CUtils.saveIntData(ActNotificationLanding.this,CGlobalVariables.COUNTNOTIFICATIONCENTER,count);
                                                for (MessageDecode msgObj : chatMessageArrayList) {
                                                    if ((msgObj.getOrderId() != null && !msgObj.getOrderId().isEmpty() && msgObj.getOrderId().equalsIgnoreCase("0"))) {
                                                        msgObj.setNotPaidLayoutShow("True");
                                                    } else {
                                                        msgObj.setNotPaidLayoutShow("False");
                                                    }
                                                }
                                                InternalStorage.writeObject(ActNotificationLanding.this, chatMessageArrayList);
                                                setAdapter();
                                            }
                                        }
                                    } catch (Exception e) {

                                        //e.printStackTrace();
                                    }

                                }

                                hideProgressBar();
                                if ((chatMessageArrayList == null) || (chatMessageArrayList != null && chatMessageArrayList.size() <= 0)) {
                                    MyCustomToast mct2 = new MyCustomToast(ActNotificationLanding.this, ActNotificationLanding.this.getLayoutInflater(), ActNotificationLanding.this, regularTypeface);
                                    mct2.show(getResources().getString(R.string.history_not_available));
                                }
                            }
                        }
                        , new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("tag", "Error Through" + error.getMessage());
                        hideProgressBar();
                    }
                }

                ) {
                    @Override
                    public String getBodyContentType() {
                        return "application/x-www-form-urlencoded; charset=UTF-8";
                    }


                    /**
                     * Passing some request headers
                     * */
                    @Override
                    public Map<String, String> getParams() {

                        String emailId = UserEmailFetcher.getEmail(ActNotificationLanding.this);
                        String androidId = CUtils.getMyAndroidId(ActNotificationLanding.this);
                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("key", CUtils.getApplicationSignatureHashCode(ActNotificationLanding.this));
                        headers.put(CGlobalVariables.KEY_EMAIL_ID, CUtils.replaceEmailChar(emailId));
                        headers.put("androidid", androidId);
                        String astrosageUserID = CUtils.getUserName(ActNotificationLanding.this);
                        if (astrosageUserID == null) {
                            astrosageUserID = "";
                        }
                        headers.put(CGlobalVariables.KEY_AS_USER_ID, CUtils.replaceEmailChar(astrosageUserID));
                        //Log.e("", headers.toString());
                        return headers;
                    }

                };
                Log.e("tag", "API HIT HERE");
                int socketTimeout = 60000;//30 seconds - change to what you want
                RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
                stringRequest.setRetryPolicy(policy);
                stringRequest.setShouldCache(true);
                queue.add(stringRequest);

            }
        }, 2000);
    }

    /**
     * @param messageDecode This method is used to call ask a question Act.
     * @author: Amit Rautela
     */
    public void callToAskQuestion(MessageDecode messageDecode, int layPos) {
        try {
            Intent intent = new Intent(this, ActAskQuestion.class);
            String cId = "";
            String prob = "";
            if (messageDecode != null) {
                if (messageDecode.getChatId() != null) {
                    cId = messageDecode.getChatId();
                }

                if (messageDecode.getMessageText() != null) {
                    prob = messageDecode.getMessageText();
                }
            }
            intent.putExtra(CGlobalVariables.key_messageChatID, cId);
            intent.putExtra(CGlobalVariables.key_layoutPosition, layPos);
            intent.putExtra("PROBLEM", getFormattedMessageText(prob).trim());
            startActivity(intent);
        } catch (Exception ex) {
            //
        }
    }

    @Override
    public void doActionAfterGetResult(String response, int method) {
        try {
            hideProgressBar();
            JSONArray jsonArray = new JSONArray(response);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String successResult = jsonObject.getString("Result");

            if (successResult.equalsIgnoreCase("1") || successResult.equalsIgnoreCase("3")) {

                double dPrice = 0.0;
                try {
                    if (priceValue != null && priceValue.length() > 0) {
                        dPrice = Double.valueOf(priceValue);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                CUtils.googleAnalyticSendWitPlayServieForPurchased(ActNotificationLanding.this,
                        CGlobalVariables.GOOGLE_ANALYTIC_ASTROSHOP_SERVICE,
                        CGlobalVariables.GOOGLE_ANALYTIC_ASK_A_QUESTION_FREE, null, dPrice, "");
                notifyNotificationAdapter();

            } else if (successResult.equalsIgnoreCase("8")) {
                //8 = If Free question is deactivated or expired
                CUtils.openFreeQuestionDeactivateFrag(ActNotificationLanding.this);
            } else {
                Toast.makeText(ActNotificationLanding.this, getResources().getString(R.string.server_error_msg), Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex) {
            Toast.makeText(ActNotificationLanding.this, getResources().getString(R.string.server_error_msg), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void doActionOnError(String response) {
        hideProgressBar();
        Toast.makeText(ActNotificationLanding.this, getResources().getString(R.string.server_error_msg), Toast.LENGTH_LONG).show();
    }

    /**
     * show Progress Bar
     */
    private void showProgressBar() {
        if (pd == null) {
            pd = new CustomProgressDialog(ActNotificationLanding.this, regularTypeface);
        }

        if (!pd.isShowing()) {
            pd.setCanceledOnTouchOutside(false);
            pd.show();
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
     * hide Progress Bar
     */
    private void hideProgressBar() {
        try {
            if (pd != null && pd.isShowing())
                pd.dismiss();
        } catch (final IllegalArgumentException e) {
            // Do nothing.
        } catch (final Exception e) {
            // Do nothing.
        } finally {
            pd = null;
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
            String pricee = "";
            try {
                if (astrologerServiceInfo != null) {
                    if (astrologerServiceInfo.getPriceRs() != null && astrologerServiceInfo.getPriceRs().length() > 0) {
                        pricee = astrologerServiceInfo.getPriceRs();
                        dPrice = Double.valueOf(pricee);
                    }
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            // in case of purchase event not added by server then add event
            if(!com.ojassoft.astrosage.utils.CUtils.isPurchaseEventAddedByServer(this)) {
                CUtils.googleAnalyticSendWitPlayServieForPurchased(this,
                        CGlobalVariables.GOOGLE_ANALYTIC_ASTROSHOP_SERVICE,
                        CGlobalVariables.GOOGLE_ANALYTIC_PAYMENT_SUCCESS_PAYTM, null, dPrice, "");
            }
        }
        CUtils.updatePaidStatus(ActNotificationLanding.this, layoutPosition, order_Id, payStatus, chat_Id, astrologerServiceInfo);
        CUtils.postDataToServer(ActNotificationLanding.this, mediumTypeface, queue, order_Id, chat_Id, payStatus, astrologerServiceInfo, CGlobalVariables.UPDATE_PAY_STATUS_CHAT);
    }
}
