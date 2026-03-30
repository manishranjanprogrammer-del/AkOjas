package com.ojassoft.astrosage.ui.act;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.tabs.TabLayout;
import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.billing.BillingEventHandler;
import com.ojassoft.astrosage.controller.ControllerManager;
import com.ojassoft.astrosage.customadapters.ViewPagerAdapter;
import com.ojassoft.astrosage.customexceptions.UIDataOperationException;
import com.ojassoft.astrosage.fcmservice.OjasFirebaseMessagingService;
import com.ojassoft.astrosage.jinterface.IPaymentFailed;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.misc.SendUserPurchaseReportForServiceToServerForAstroChat;
import com.ojassoft.astrosage.misc.VerificationServiceForInAppBillingChat;
import com.ojassoft.astrosage.model.AppPurchaseDataSaveModelClass;
import com.ojassoft.astrosage.model.AstrologerServiceInfo;
import com.ojassoft.astrosage.model.ServicelistModal;
import com.ojassoft.astrosage.ui.customcontrols.AppRater;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.ui.fragments.PaymentFailedDailogFrag;
import com.ojassoft.astrosage.ui.fragments.astrochatfragment.adapter.RecyclerViewAdapterShowMessage;
import com.ojassoft.astrosage.ui.fragments.astrochatfragment.fragment.ChangeBirthProfileDialog;
import com.ojassoft.astrosage.ui.fragments.astrochatfragment.model.MessageDecode;
import com.ojassoft.astrosage.ui.fragments.astrochatfragment.networkdataload.NetworkQuestionDetail;
import com.ojassoft.astrosage.ui.fragments.astrochatfragment.savedata.InternalStorage;
import com.ojassoft.astrosage.ui.fragments.astrochatfragment.savedata.SaveDataInternalStorage;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.UserEmailFetcher;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by ojas on ८/६/१६.
 */
public class ActAstroPaymentOptions extends BaseInputActivity implements IPaymentFailed, BillingEventHandler {


    public static final int RESPONSE_CODE_SELECT_PROFILE = 101;
    public static final int RESPONSE_CODE_UPDATE_PROFILE = 102;
    public static final String SKU_ASKQUESTION_PLAN = "ask_a_question";
    static final int SUB_RC_REQUEST_ASK_QUESTION_PLAN = 20006;
    public static int userImageResource = R.drawable.ic_male;
    public static boolean isMessagesModified = false;
    public static boolean isMessageNeedToSend = true;
    public String[] pageTitles;
    public ViewPager viewPager;
    public Toolbar toolBar_InputKundli;
    public View appbarAppModule;
    public int SELECTED_MODULE;
    public ServicelistModal itemdetails;
    TextView titleTextView;
    ImageView homeImageView;
    ImageView moreImageView;
    ImageView toggleImageView;
    ViewPagerAdapter adapter;
    FrameLayout frameLayoutShowQuestion;
    int layoutPosition;
    ImageView updateBirthProfile;
    //MergeActAstroPaymentOption
    RecyclerView astrologerRecyclerView;
    ArrayList<MessageDecode> chatMessageArrayList;
    //Toolbar toolBar;
    boolean isQuestionSet;
    ImageView sendButton;
    CustomProgressDialog pd = null;
    Typeface typeface;
    int i = 0;
    EditText questionAskAstrologer;
    RecyclerViewAdapterShowMessage myRecyclerAdapter;
    MessageDecode messageDecodeSend;
    ArrayList<String> arayaskquestionPlan = new ArrayList<String>(5);
    ProductDetails askAQSkuDetails;
    String[] errorResponse = {"Success",
            "Billing response result user canceled",
            "Network connection is down",
            "Billing API version is not supported for the type requested",
            "Requested product is not available for purchase",
            "Invalid arguments provided to the API",
            "Fatal error during the API action",
            "Failure to purchase since item is already owned",
            "Failure to consume since item is not owned"};
    String purchaseData = "", signature = "";
    int price_amount_micros = 5, price_currency_code = 6;
    String spouseFullDetail;
    String callingActivity;
    private TabLayout tabs_input_kundli;
    private RequestQueue queue;
    private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    private String birthProfileName;
    private final String email_id_for_question = "";
    private boolean isPaymentDone = true;
    private String titleText;
    private BroadcastReceiver receiver;
    private final BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            if (intent.getBooleanExtra(CGlobalVariables.UPDATE_PLAN_STATUS, false) || intent.getBooleanExtra(CGlobalVariables.CHAT_ANSWER_REPLY_MESSAGE, false)) {
                updateRecyclerViewMessages();

            } else {
                MessageDecode message = intent.getParcelableExtra(CGlobalVariables.COPA_MESSAGE);
                message.setNotPaidLayoutShow("False");
                myRecyclerAdapter.notifyDataSetChanged();
                astrologerRecyclerView.scrollToPosition(myRecyclerAdapter.getItemCount() - 1);
                //Toast.makeText(ActAstroPaymentOptions.this, "call GCM Result" + message.getMessageText(), Toast.LENGTH_SHORT).show();
                Log.d("receiver", "Got message: " + message.getMessageText() + message.getMessageText());
                addToList(message);
            }
        }
    };

    public ActAstroPaymentOptions() {
        super(R.string.askaquestion);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dismissActiveMessageNotification();
        setBillingEventHandler(this);
        LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication())
                .getLanguageCode();

        typeface = CUtils.getRobotoFont(
                getApplicationContext(), LANGUAGE_CODE, CGlobalVariables.regular);
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        setContentView(R.layout.lay_input_ask_a_question_screen);
        if (getIntent().getExtras().containsKey("callingActivity")) {
            callingActivity = getIntent().getExtras().get("callingActivity").toString();
            if (callingActivity.equals("OutputMasterActivity")) {
                BeanHoroPersonalInfo beanHoroPersonalInfo1 = (BeanHoroPersonalInfo) getIntent().getExtras().getSerializable("BeanHoroPersonalInfo1");
                String data = convertObjectInJsonString(beanHoroPersonalInfo1);
                CUtils.saveStringData(ActAstroPaymentOptions.this, CGlobalVariables.USERPROFILEASTROCHAT, data);
                CUtils.saveBooleanData(ActAstroPaymentOptions.this, CGlobalVariables.IS_USER_PROFILE_FILLED, true);
            }
        }

        //updateAvailableQuestion();
        initLayoutXML();
        //testNotificationSetting();
        setVisibilityToolbarMenuButton();
        getAvailableQuestion();
        enableToolBar();
        setClickListener();
        setToolBarTitleText(getTitleText());
        fetchProductFromGoogleServer();
        //new AsyncProductDetailFromGoogleServer().execute();
        //showFragmentAskQuestion();
        chatMessageArrayList = CUtils.getDataFromPrefrence(ActAstroPaymentOptions.this);
        if (chatMessageArrayList == null || chatMessageArrayList.size() <= 0) {
            getChatHistory();
        }
        receiveServerMessage();

    }

    private void dismissActiveMessageNotification() {
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.cancel("CHAT_WITH_ASTROLOGER", OjasFirebaseMessagingService.Chat_Notification_Id);
        OjasFirebaseMessagingService.messageCounter = 0;
    }

    private void receiveServerMessage() {
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                MessageDecode messageDecode;
                messageDecode = (MessageDecode) intent.getSerializableExtra(OjasFirebaseMessagingService.COPA_MESSAGE);
                if (messageDecode != null) {
                    addToList(messageDecode);
                }
            }
        };
    }

    @Override
    public void onResume() {
        super.onResume();
        setOurAstrologerRecyclerView();
        getMessageList();
        if (chatMessageArrayList == null || chatMessageArrayList.size() <= 0) {
            //getChatHistory();
        }
        setClickListener();
        if (myRecyclerAdapter != null) {
            //Log.w("TotalMessageCount", "" + myRecyclerAdapter.getItemCount());
            astrologerRecyclerView.scrollToPosition(myRecyclerAdapter.getItemCount() - 1);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        //EasyTracker.getInstance().activityStart(this);
        LocalBroadcastManager.getInstance(ActAstroPaymentOptions.this).registerReceiver(mMessageReceiver,
                new IntentFilter(CGlobalVariables.COPA_RESULT));
    }

    // save message each time when screen change even when partially.
    @Override
    public void onPause() {
        super.onPause();
        // saveDataOnInternalStorage();
    }

    @Override
    public void onStop() {
        super.onStop();
        //EasyTracker.getInstance().activityStop(this);
        LocalBroadcastManager.getInstance(ActAstroPaymentOptions.this).unregisterReceiver(mMessageReceiver);
        if (isMessagesModified) {
            saveDataOnInternalStorage();
        }
    }

    private void updateRecyclerViewMessages() {
        android.os.Handler handler = new android.os.Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                getMessageList();
                astrologerRecyclerView.scrollToPosition(myRecyclerAdapter.getItemCount() - 1);
            }
        }, 1000);
    }

    private void setToolBarTitleText(String title) {
        if (title.contains("$")) {
            title = title.replace("$", "");
        }
        titleTextView.setText(title);
    }

    private void enableToolBar() {
        setSupportActionBar(toolBar_InputKundli);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setVisibilityToolbarMenuButton() {
        toggleImageView.setVisibility(View.GONE);
        homeImageView.setVisibility(View.GONE);
        moreImageView.setVisibility(View.GONE);
        // titleTextView.setTypeface(mediumTypeface);
        tabs_input_kundli.setVisibility(View.GONE);
        if (callingActivity != null && callingActivity.equals("ActAppModule")) {
            updateBirthProfile.setVisibility(View.VISIBLE);
        } else if (callingActivity != null && (callingActivity.equals("OutputMasterActivity") || callingActivity.equals("OutputMatchingMasterActivity"))) {
            updateBirthProfile.setVisibility(View.GONE);
        } else {
            updateBirthProfile.setVisibility(View.VISIBLE);
        }
    }

    private void initLayoutXML() {
        appbarAppModule = findViewById(R.id.appbarAppModule);
        toolBar_InputKundli = findViewById(R.id.tool_barAppModule);
        toggleImageView = findViewById(R.id.ivToggleImage);
        titleTextView = findViewById(R.id.tvTitle);
        homeImageView = findViewById(R.id.imgHome);
        moreImageView = findViewById(R.id.imgMoreItem);
        tabs_input_kundli = findViewById(R.id.tabs);
        frameLayoutShowQuestion = findViewById(R.id.fragmentchange);
        astrologerRecyclerView = findViewById(R.id.astrologer_list);
        sendButton = findViewById(R.id.sendbutton);
        questionAskAstrologer = findViewById(R.id.userQuestion);
        updateBirthProfile = findViewById(R.id.changeProfileAskQuestion);

    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                CUtils.hideMyKeyboard(this);
                this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void displayRateDialog() {
        String Headingtext = ActAstroPaymentOptions.this
                .getString(R.string.app_mainheading_text_kundali);
        String SubHeadingtext = ActAstroPaymentOptions.this
                .getString(R.string.app_subheading_text_kundali);
        String Subchildheading = ActAstroPaymentOptions.this
                .getString(R.string.app_subchild_text_kundali);
        AppRater.app_launched(ActAstroPaymentOptions.this, Headingtext,
                SubHeadingtext, Subchildheading);
    }

    public String getTitleText() {
        if (CUtils.getBooleanData(ActAstroPaymentOptions.this, CGlobalVariables.IS_USER_PROFILE_FILLED, false)) {
            String userProfileChat = initiallizeOblect();
            titleTextView.setTypeface(null);
            return getProfileName(userProfileChat);
        } else {
            titleTextView.setTypeface(typeface);
            return getResources().getString(R.string.show_chat_message_fragment);
        }
    }

    public void getAvailableQuestion() {
        new NetworkQuestionDetail(ActAstroPaymentOptions.this, UserEmailFetcher.getEmail(this)).getAvailableQuestion();
        if (CUtils.getBooleanData(ActAstroPaymentOptions.this, CGlobalVariables.IS_USER_PROFILE_FILLED, false)) {
            String userProfileChat = initiallizeOblect();
            AstrologerServiceInfo serviceInfo = new Gson().fromJson(userProfileChat, new TypeToken<AstrologerServiceInfo>() {
            }.getType());
        }
    }

    @Override
    public void onRetryClick() {
        gotBuyAskQuestionPlan(messageDecodeSend, layoutPosition);
    }

    private void fetchProductFromGoogleServer() {
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

        ActAstroPaymentOptions.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (response >= 0) {
                    //Toast.makeText(ActAstroPaymentOptions.this, errorResponse[response], Toast.LENGTH_LONG).show();
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

    public void gotBuyAskQuestionPlan(MessageDecode messageDecode, int layoutPosition) {
        try {
            this.layoutPosition = layoutPosition;
            messageDecodeSend = messageDecode;

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

                if (responseCode != BillingClient.BillingResponseCode.OK) {
                    Toast.makeText(ActAstroPaymentOptions.this, "Fail to purchase", Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            //Log.e("RESPONSE_ERROR", e.getMessage());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                //case ShowMessageChat.REQUEST_CODE_SELECT_PROFILE:
                case RESPONSE_CODE_SELECT_PROFILE:
                    String userProfileChat = data.getStringExtra("JSONFULLASTROSAGEDATA");
                    CUtils.saveStringData(ActAstroPaymentOptions.this, CGlobalVariables.USERPROFILEASTROCHAT, userProfileChat);
                    titleTextView.setTypeface(null);
                    setToolBarTitleText(getProfileName(userProfileChat));
                    //Toast.makeText(ActAstroPaymentOptions.this, "Profile Selected Successfully", Toast.LENGTH_SHORT).show();
                    if (isQuestionSet) {
                        // saveDataOnInternalStorage();
                        sendDataToServer();
                    }
                    break;
                case RESPONSE_CODE_UPDATE_PROFILE:
                    String newBirthProfile = data.getStringExtra("JSONFULLASTROSAGEDATA");
                    CUtils.saveStringData(ActAstroPaymentOptions.this, CGlobalVariables.USERPROFILEASTROCHAT, newBirthProfile);
                    titleTextView.setTypeface(null);
                    setToolBarTitleText(getProfileName(newBirthProfile));
                    //Toast.makeText(ActAstroPaymentOptions.this, "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    // Toast.makeText(ActAstroPaymentOptions.this, "Condition cannot fulfill", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }

    @Override
    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {
        // Logic from onActivityResult should be moved here.
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
            Log.e("BillingClient", "onPurchasesUpdated() OK");
            if (purchases != null && !purchases.isEmpty()) {
                Purchase purchase = purchases.get(0);
                processGooglePaymentAfterSuccess(purchase);
            }
        } else {
            Log.e("BillingClient", "onPurchasesUpdated() FAIL = " + purchases);
            if (isMessageNeedToSend) {
                String fullJsonobj = initiallizeOblect();
                SendUserPurchaseReportForServiceToServerForAstroChat sendObj = new SendUserPurchaseReportForServiceToServerForAstroChat(this, "", UserEmailFetcher.getEmail(this), CUtils.getUserName(this), "", "", fullJsonobj, "" + layoutPosition, messageDecodeSend.getMessageText(), messageDecodeSend.getChatId(), "MessageTitle");
                sendObj.sendReportToServerForFreeQue();
                isMessageNeedToSend = false;
            }
            openPaymentFailedDialog();
        }
        //Log.e("BillingClient", "onPurchasesUpdated() FAIL purchases size="+purchases.size());
    }

    private void processGooglePaymentAfterSuccess(Purchase purchase) {
        try {
            String price = "0";
            String priceCurrencycode = "INR";
            price = arayaskquestionPlan.get(price_amount_micros);// 2
            priceCurrencycode = arayaskquestionPlan.get(price_currency_code);
            String astroSageUserId = CUtils.getUserName(ActAstroPaymentOptions.this);
            isPaymentDone = true;
            CUtils.saveBooleanData(ActAstroPaymentOptions.this, CGlobalVariables.PAYMENTDONE_KEY_FOR_ASTRO_SERVICES_ISPAYMENTDONE, isPaymentDone);
            String fullJsonDataObj = initiallizeOblect();
            verifyPurchaseFromService(purchase, astroSageUserId, price, priceCurrencycode, fullJsonDataObj);
        } catch (Exception e) {
            Log.e("BillingClient", "AfterSuccess exp=" + e);
        }
    }

    private void openPaymentFailedDialog() {
        CUtils.saveBooleanData(this,CGlobalVariables.IS_SUBSCRIPTION_ERROR,true);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        PaymentFailedDailogFrag pfdf = new PaymentFailedDailogFrag();
        ft.add(pfdf, null);
        ft.commitAllowingStateLoss();
    }

    private String getProfileName(String userProfileChat) {
        AstrologerServiceInfo serviceInfo = new Gson().fromJson(userProfileChat, new TypeToken<AstrologerServiceInfo>() {
        }.getType());
        if ((serviceInfo != null && serviceInfo.getGender() != null) && (serviceInfo.getGender().equalsIgnoreCase("male") || serviceInfo.getGender().equals("M"))) {
            userImageResource = R.drawable.ic_male;
        } else {
            userImageResource = R.drawable.ic_female;
        }
        if (serviceInfo != null)
            return serviceInfo.getRegName();
        else {
            userImageResource = R.drawable.ic_male;
            return "";
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void setClickListener() {
        astrologerRecyclerView.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
                if (myRecyclerAdapter != null) {
                    //           astrologerRecyclerView.scrollToPosition(myRecyclerAdapter.getItemCount() - 1);
                }

                homeImageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ActAstroPaymentOptions.this.finish();
                    }
                });
            }
        });

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Check question is empty or not.
                if (!questionAskAstrologer.getEditableText().toString().trim().isEmpty()) {
                    isPaymentDone = false;
                    CUtils.saveBooleanData(ActAstroPaymentOptions.this, CGlobalVariables.PAYMENTDONE_KEY_FOR_ASTRO_SERVICES_ISPAYMENTDONE, isPaymentDone);
                    if (CUtils.getBooleanData(ActAstroPaymentOptions.this, CGlobalVariables.IS_USER_PROFILE_FILLED, false)) {
                        sendDataToServer();
                    } else {
                        isQuestionSet = true;
                        openBirthProfileUpdatorDialog(RESPONSE_CODE_SELECT_PROFILE);
                    }
                } else {
                    //Toast.makeText(ActAstroPaymentOptions.this, " Can't be Empty ", Toast.LENGTH_SHORT).show();
                    MyCustomToast mct2 = new MyCustomToast(ActAstroPaymentOptions.this, ActAstroPaymentOptions.this.getLayoutInflater(), ActAstroPaymentOptions.this, ActAstroPaymentOptions.this.regularTypeface);
                    mct2.show(getResources().getString(R.string.questioncannotempty));
                }
            }
        });

        updateBirthProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openBirthProfileUpdatorDialog(RESPONSE_CODE_UPDATE_PROFILE);
            }
        });
    }

    public void openBirthProfileUpdatorDialog(Integer requestKey) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        FragmentManager fm = getSupportFragmentManager();
        Fragment prev = fm.findFragmentByTag("CHANGE_BIRTH_PROFILE");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        ChangeBirthProfileDialog changeBirthProfileDialog = new ChangeBirthProfileDialog();
        Bundle bundle = new Bundle();
        bundle.putInt("requestCode", requestKey);
        changeBirthProfileDialog.setArguments(bundle);
        changeBirthProfileDialog.show(fm, "CHANGE_BIRTH_PROFILE");
        ft.commit();
    }

    private void sendDataToServer() {
        int noOfQuestion = CUtils.getIntData(ActAstroPaymentOptions.this, CGlobalVariables.NOOFQUESTIONAVAILABLE, 0);
        if (noOfQuestion > 0) {
            MessageDecode messageDecode = setMessageDataAskByUser();
            CUtils.saveBooleanData(ActAstroPaymentOptions.this, CGlobalVariables.Type_PAYMENT, true);
            saveDataOnInternalStorage();
            //SendUserPurchaseReportForServiceToServer(Context context, String purchaseData, String emailId, String userId, String price, String priceCurrencycode, String fullJsonDataObj, String layoutPosition,String messageText,String messageChatID)
            String data = initiallizeOblect();
            if (!data.isEmpty()) {
                AstrologerServiceInfo serviceInfo = new Gson().fromJson(data, AstrologerServiceInfo.class);
                serviceInfo.setPayMode("Free");
                String fullJsonobj = new Gson().toJson(serviceInfo);
                SendUserPurchaseReportForServiceToServerForAstroChat sendObj = new SendUserPurchaseReportForServiceToServerForAstroChat(this, "", UserEmailFetcher.getEmail(this), CUtils.getUserName(this), "", "", fullJsonobj, String.valueOf(chatMessageArrayList.size() - 1), messageDecode.getMessageText(), messageDecode.getChatId(), "");
                sendObj.sendReportToServerForFreeQue();
               /* new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void[] params) {
                        sendObj.sendReportToServerForFreeQue();
                        return null;
                    }
                }.execute();*/
            }

        } else {
            MessageDecode messageDecode = setMessageDataAskByUser();
            CUtils.saveBooleanData(ActAstroPaymentOptions.this, CGlobalVariables.Type_PAYMENT, false);
            saveDataOnInternalStorage();
            gotBuyAskQuestionPlan(messageDecode, chatMessageArrayList.size() - 1);
        }
    }

    private MessageDecode setMessageDataAskByUser() {
        MessageDecode messageDecode = new MessageDecode();
        messageDecode.setUserType("user");
        messageDecode.setNotificationShow("False");
        String currentDateTime = CUtils.getCurrentDateTime();
        messageDecode.setDateTimeShow(currentDateTime);
        messageDecode.setMessageText(questionAskAstrologer.getEditableText().toString());
        messageDecode.setColorOfMessage("#ffffff");
        messageDecode.setRateShow("False");
        messageDecode.setShareLinkShow("False");
        messageDecode.setNoOfQuestion(CUtils.getIntData(ActAstroPaymentOptions.this, CGlobalVariables.NOOFQUESTIONAVAILABLE, 0));
        int noOfQuestion = CUtils.getIntData(ActAstroPaymentOptions.this, CGlobalVariables.NOOFQUESTIONAVAILABLE, 0);
        if (noOfQuestion > 0) {
            messageDecode.setNotPaidLayoutShow("False");
        } else {
            messageDecode.setNotPaidLayoutShow("True");
            messageDecode.setOrderId("0");
        }
        addToList(messageDecode);
        questionAskAstrologer.setText(" ");
        questionAskAstrologer.setHint(getResources().getString(R.string.type_text));
        return messageDecode;
    }

    private int isLocalKundliAvailable() {
        int screenId = 1;
        try {
            Map<String, String> mapHoroID = new ControllerManager().searchLocalKundliListOperation(
                    ActAstroPaymentOptions.this.getApplicationContext(), "",
                    CGlobalVariables.BOTH_GENDER, -1);
            if (mapHoroID != null) {
                screenId = 0;
            } else {
                screenId = 1;
            }

        } catch (UIDataOperationException e) {
            e.printStackTrace();
        }
        return screenId;
    }


    // Add new message on the list and update adapter each time.
    private void addToList(MessageDecode messageDecode) {
        if (chatMessageArrayList == null)
            chatMessageArrayList = new ArrayList<MessageDecode>();
        chatMessageArrayList.add(messageDecode);
        if (myRecyclerAdapter == null) {
            myRecyclerAdapter = new RecyclerViewAdapterShowMessage(chatMessageArrayList, ActAstroPaymentOptions.this, false);
            astrologerRecyclerView.setAdapter(myRecyclerAdapter);
        }
        myRecyclerAdapter.notifyDataSetChanged();
        astrologerRecyclerView.scrollToPosition(myRecyclerAdapter.getItemCount() - 1);
    }

    // Get previous message from storage.
    // called once when app start/resume.
    private void getMessageList() {
        chatMessageArrayList = CUtils.getDataFromPrefrence((ActAstroPaymentOptions.this));
        if (chatMessageArrayList != null) {
            myRecyclerAdapter = new RecyclerViewAdapterShowMessage(chatMessageArrayList, ActAstroPaymentOptions.this, false);
            astrologerRecyclerView.setAdapter(myRecyclerAdapter);
        } else {
            astrologerRecyclerView.setAdapter(null);
            chatMessageArrayList = new ArrayList<>();
        }
        astrologerRecyclerView.setLayoutManager(new LinearLayoutManager(ActAstroPaymentOptions.this));
    }

    //  Save/Update complete message list on storage.
    //  Call each time when screen changed.]
    public void saveDataOnInternalStorage() {
        Intent intent = new Intent(ActAstroPaymentOptions.this, SaveDataInternalStorage.class);
        intent.putParcelableArrayListExtra(CGlobalVariables.CHATWITHASTROLOGER, chatMessageArrayList);
        intent.putExtra(CGlobalVariables.ISINSERT, false);
        (ActAstroPaymentOptions.this).startService(intent);
    }

    public void setOurAstrologerRecyclerView() {

    }

    private void verifyPurchaseFromService(Purchase purchase, String astroSageUserId, String price, String priceCurrencycode, String fullJsonDataObj) {
        signature = purchase.getSignature();
        purchaseData = purchase.getOriginalJson();
        ActAstroPaymentOptions.this.getSharedPreferences("MISC_PUR_SERVICE", Context.MODE_PRIVATE).edit()
                .putString("VALUE_SERVICE", purchaseData).commit();

        saveDataInPreferences(astroSageUserId, price, priceCurrencycode);
        Intent pvsIntent = new Intent(getApplicationContext(),
                VerificationServiceForInAppBillingChat.class);
        pvsIntent.putExtra("SIGNATURE", signature);
        pvsIntent.putExtra("PURCHASE_DATA", purchaseData);
        pvsIntent.putExtra("ASTRO_USERID", astroSageUserId);
        String textMsg = messageDecodeSend.getMessageText();
        /*if (spouseFullDetail != null&&) {
            textMsg = textMsg + "<>" + CUtils.convertInJsonObj(beanHoroPersonalInfo2);
        }*/
        pvsIntent.putExtra("MESSAGE_TEXT", textMsg);
        String chatID = "";
        if (messageDecodeSend.getChatId() != null) {
            chatID = messageDecodeSend.getChatId();
        }
        pvsIntent.putExtra("MESSAGE_CHAT_ID", chatID);
        pvsIntent.putExtra("price", price);
        pvsIntent.putExtra("priceCurrencycode", priceCurrencycode);

        pvsIntent.putExtra("FullJsonDataObj", fullJsonDataObj);
        pvsIntent.putExtra("layoutPostion", "" + layoutPosition);
        startService(pvsIntent);
    }

    private void saveDataInPreferences(String astroSageUserId, String price, String priceCurrencycode) {
        boolean isInsert = CUtils.getBooleanData(ActAstroPaymentOptions.this, CGlobalVariables.Type_PAYMENT, false);
        String isInsertPre = "" + isInsert;
        SharedPreferences purchasageDataPlan = getSharedPreferences(CGlobalVariables.ASTROSAGEPURCHASEPLANFORSERVICE, Context.MODE_PRIVATE);
        SharedPreferences.Editor purchasePlanEditor = purchasageDataPlan.edit();
        AppPurchaseDataSaveModelClass appPurchaseDataSaveModelClass = CUtils.getObjectSavePref(signature, purchaseData, "", astroSageUserId, price, priceCurrencycode, messageDecodeSend.getMessageText(), messageDecodeSend.getChatId(), isInsertPre, "" + layoutPosition);
        Gson gson = new Gson();
        String purchaseDataJSONObject = gson.toJson(appPurchaseDataSaveModelClass); // myObject - instance of MyObject
        purchasePlanEditor.putString(CGlobalVariables.ASTROSAGEPURCHASEPLANFORSERVICEOBJECT, purchaseDataJSONObject);
        purchasePlanEditor.commit();
    }


    private void getChatHistory() {
        pd = new CustomProgressDialog(ActAstroPaymentOptions.this, typeface);
        pd.show();
        pd.setCancelable(false);
        //Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, CGlobalVariables.CHAT_HISTORY,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.e("Response+", response.toString());
                        if (response != null && !response.isEmpty()) {

                            try {

                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject obj = jsonArray.getJSONObject(0);
                                if (obj.getString("Result") != null && obj.getString("Result").equalsIgnoreCase("2")) {
                                    String chatHistory = obj.getString("GotChatHistory");
                                    chatMessageArrayList = new Gson().fromJson(chatHistory.trim(), new TypeToken<ArrayList<MessageDecode>>() {
                                    }.getType());

                                    if (chatMessageArrayList.size() > 0) {
                                        for (MessageDecode msgObj : chatMessageArrayList) {
                                            if ((msgObj.getOrderId() != null && !msgObj.getOrderId().isEmpty() && msgObj.getOrderId().equalsIgnoreCase("0"))) {
                                                msgObj.setNotPaidLayoutShow("True");
                                            } else {
                                                msgObj.setNotPaidLayoutShow("False");
                                            }
                                        }

                                        InternalStorage.writeObject(ActAstroPaymentOptions.this, chatMessageArrayList);
                                        //   saveDataOnInternalStorage();
                                        myRecyclerAdapter = new RecyclerViewAdapterShowMessage(chatMessageArrayList, ActAstroPaymentOptions.this, true);
                                        astrologerRecyclerView.setAdapter(myRecyclerAdapter);
                                        myRecyclerAdapter.notifyDataSetChanged();
                                        astrologerRecyclerView.scrollToPosition(myRecyclerAdapter.getItemCount() - 1);
                                        if (myRecyclerAdapter != null) {
                                            astrologerRecyclerView.scrollToPosition(myRecyclerAdapter.getItemCount() - 1);
                                        }

                                    }
                                } else if (obj.getString("Result") != null && obj.getString("Result").equalsIgnoreCase("1")) {

                                    MessageDecode ms = new Gson().fromJson(obj.toString(), MessageDecode.class);
                                    addToList(ms);
                                    myRecyclerAdapter = new RecyclerViewAdapterShowMessage(chatMessageArrayList, ActAstroPaymentOptions.this, true);
                                    astrologerRecyclerView.setAdapter(myRecyclerAdapter);
                                    myRecyclerAdapter.notifyDataSetChanged();
                                    astrologerRecyclerView.scrollToPosition(myRecyclerAdapter.getItemCount() - 1);
                                    if (myRecyclerAdapter != null) {
                                        //   Log.w("TotalMessageCount", "" + myRecyclerAdapter.getItemCount());
                                        astrologerRecyclerView.scrollToPosition(myRecyclerAdapter.getItemCount() - 1);
                                    }
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

//                        data = gson.fromJson(response, new TypeToken<ArrayList<AstroShopMaindata>>() {
//                        }.getType());
                        //    //Log.e("Size returned" + data.get(0).getGemStones().size());
                        pd.dismiss();
                    }

                }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e("tag", "Error Through" + error.getMessage());
                pd.dismiss();
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

               /* TelephonyManager telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                String deviceId = telephonyManager.getDeviceId();*/
                //useremailid, key
                String emailId = UserEmailFetcher.getEmail(ActAstroPaymentOptions.this);
                //String emailId="chandantestliveapi@gmail.com";
                String androidId = Settings.Secure.getString(getApplicationContext().getContentResolver(), Settings.Secure.ANDROID_ID);

                //String emailId = "testchatnewid@gmail.com";
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Key", CUtils.getApplicationSignatureHashCode(ActAstroPaymentOptions.this));
                headers.put(CGlobalVariables.KEY_EMAIL_ID, CUtils.replaceEmailChar(emailId));
                headers.put("androidid", androidId);
                //Log.e("", headers.toString());
                return headers;
            }

        };


        // Add the request to the RequestQueue.
        //Log.e("tag", "API HIT HERE");
        int socketTimeout = 60000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        stringRequest.setShouldCache(true);
        queue.add(stringRequest);

    }

    private String initiallizeOblect() {
        String fullJsonDataObj = "";

        if (callingActivity != null && callingActivity.equals("ActAppModule")) {
            fullJsonDataObj = CUtils.getStringData(ActAstroPaymentOptions.this, CGlobalVariables.USERPROFILEASTROCHAT, "");
        } else if (callingActivity != null && callingActivity.equals("OutputMasterActivity")) {
            BeanHoroPersonalInfo beanHoroPersonalInfo1 = (BeanHoroPersonalInfo) getIntent().getExtras().getSerializable("BeanHoroPersonalInfo1");
            fullJsonDataObj = convertObjectInJsonString(beanHoroPersonalInfo1);
            CUtils.saveStringData(ActAstroPaymentOptions.this, CGlobalVariables.USERPROFILEASTROCHAT, fullJsonDataObj);
        } else if (callingActivity != null && callingActivity.equals("OutputMatchingMasterActivity")) {
            BeanHoroPersonalInfo beanHoroPersonalInfo1 = (BeanHoroPersonalInfo) getIntent().getExtras().getSerializable("BeanHoroPersonalInfo1");
            fullJsonDataObj = convertObjectInJsonString(beanHoroPersonalInfo1);
            BeanHoroPersonalInfo beanHoroPersonalInfo2 = (BeanHoroPersonalInfo) getIntent().getExtras().getSerializable("BeanHoroPersonalInfo2");
            spouseFullDetail = convertObjectInJsonString(beanHoroPersonalInfo2);
        } else {
            fullJsonDataObj = CUtils.getStringData(ActAstroPaymentOptions.this, CGlobalVariables.USERPROFILEASTROCHAT, "");
        }
        return fullJsonDataObj;
    }

    private String convertObjectInJsonString(BeanHoroPersonalInfo beanHoroPersonalInfo) {
        AstrologerServiceInfo astrologerServiceInfo = CUtils.setDataModel(ActAstroPaymentOptions.this, beanHoroPersonalInfo, "google", itemdetails);
        String fullJsonDataObj = CUtils.convertInJsonObj(astrologerServiceInfo);
        return fullJsonDataObj;
    }
}