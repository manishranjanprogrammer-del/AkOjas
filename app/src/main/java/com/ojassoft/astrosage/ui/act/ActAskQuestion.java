package com.ojassoft.astrosage.ui.act;

import static com.google.android.libraries.identity.googleid.GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_AS_USER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.PAYMENT_TYPE_SERVICE;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.credentials.Credential;
import androidx.credentials.CredentialManager;
import androidx.credentials.CredentialManagerCallback;
import androidx.credentials.CustomCredential;
import androidx.credentials.GetCredentialRequest;
import androidx.credentials.GetCredentialResponse;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
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
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.auth.api.identity.BeginSignInRequest;
import com.google.android.gms.auth.api.identity.Identity;
import com.google.android.gms.auth.api.identity.SignInClient;
import com.google.android.libraries.identity.googleid.GetGoogleIdOption;
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.libojassoft.android.misc.SendDataBackToComponent;
import com.libojassoft.android.misc.VolleySingleton;
import com.libojassoft.android.utils.LibCUtils;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AskAQuestionTestimonials;
import com.ojassoft.astrosage.beans.BeanDateTime;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.beans.BeanPlace;
import com.ojassoft.astrosage.beans.CircularNetworkImageView;
import com.ojassoft.astrosage.billing.BillingEventHandler;
import com.ojassoft.astrosage.controller.ControllerManager;
import com.ojassoft.astrosage.customadapters.AskAQuestionTestimonialsAdapter;
import com.ojassoft.astrosage.customexceptions.UIDataOperationException;
import com.ojassoft.astrosage.jinterface.IAskCallback;
import com.ojassoft.astrosage.jinterface.IChoosePayOption;
import com.ojassoft.astrosage.jinterface.IPaymentFailed;
import com.ojassoft.astrosage.jinterface.IPermissionCallback;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.misc.GetCheckSum;
import com.ojassoft.astrosage.misc.SendUserPurchaseReportForServiceToServerForAstroChat;
import com.ojassoft.astrosage.misc.VerificationServiceForInAppBillingChat;
import com.ojassoft.astrosage.model.AllAstrologerInfo;
import com.ojassoft.astrosage.model.AppPurchaseDataSaveModelClass;
import com.ojassoft.astrosage.model.AstrologerServiceInfo;
import com.ojassoft.astrosage.model.Payoption;
import com.ojassoft.astrosage.model.ServicelistModal;
import com.ojassoft.astrosage.networkcall.OnVolleyResultListener;
import com.ojassoft.astrosage.ui.customcontrols.FlowTextView;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.ui.fragments.ChoosePayOptionFragmentDailog;
import com.ojassoft.astrosage.ui.fragments.ChooseServicePayOPtionDialog;
import com.ojassoft.astrosage.ui.fragments.HomeNavigationDrawerFragment;
import com.ojassoft.astrosage.ui.fragments.PaymentFailedDailogFrag;
import com.ojassoft.astrosage.ui.fragments.astrochatfragment.model.MessageDecode;
import com.ojassoft.astrosage.ui.fragments.astrochatfragment.networkdataload.NetworkQuestionDetail;
import com.ojassoft.astrosage.ui.fragments.astrochatfragment.savedata.InternalStorage;
import com.ojassoft.astrosage.ui.fragments.astrochatfragment.savedata.SaveDataInternalStorage;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.CustomTypefaces;
import com.ojassoft.astrosage.utils.UserEmailFetcher;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.paytm.pgsdk.TransactionManager;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executors;

/**
 * Created by ojas on ८/६/१६.
 */
public class ActAskQuestion extends BaseInputActivity implements View.OnClickListener, IPaymentFailed, IChoosePayOption, IAskCallback, IPermissionCallback, PaymentResultListener,
	        SendDataBackToComponent, OnVolleyResultListener, BillingEventHandler {
    private static final String TAG = "ActAskQuestionTag";
    public static final String SKU_ASKQUESTION_PLAN = "ask_a_question";
    private static final int RC_HINT_EMAIL = 2;
    // ADD FOR INAPP BILLING ON 08/07/2016
    public static int ASK_QUESTION_PLAN = 0;
    public BeanHoroPersonalInfo beanHoroPersonalInfo, beanHoroPersonalInfo1;//for girl
    public Payoption globalPayOptions;
    public HomeNavigationDrawerFragment drawerFragment;
    Activity activity;
    String payStatus = "0";
    String order_Id = "";
    String chat_Id = "";
    String priceValue = "";
    int price_amount_micros = 5, price_currency_code = 6;
    String developerPayload = "", purchaseData = "", signature = "";
    boolean isDataComingFromFragAskAQuesAdvertisementView;
    String[] errorResponse = {"Success",
            "Billing response result user canceled",
            "Network connection is down",
            "Billing API version is not supported for the type requested",
            "Requested product is not available for purchase",
            "Invalid arguments provided to the API",
            "Fatal error during the API action",
            "Failure to purchase since item is already owned",
            "Failure to consume since item is not owned"};
    ArrayList<String> arayaskquestionPlan = new ArrayList<String>(5);
    ProductDetails askAQSkuDetails;
    ArrayList<MessageDecode> chatMessageArrayList;
    TextView countTextViewNotificationCenter;
    RelativeLayout relativeLayoutNotificationCenter;
    int layoutPosition;
    Spinner reportLanguageOptions;
    String payId = "";
    LinearLayout basicPlanUserLayout;
    String dumpDataString = "";
    private TextView tvTitle;
    private Toolbar tool_barAppModule;
    private TabLayout tabLayout;
    private CustomProgressDialog pd = null;
    private Typeface typeface;
    private RequestQueue queue;
    private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    private EditText etUseremail, etUsermobno, etUserquestion;
    private Button buy_now;
    private TextInputLayout input_layout_email, input_layout_mobno, input_layout_question;
    private MyCustomToast mct;
    private AstrologerServiceInfo astrologerServiceInfo;
    private String fullJsonDataObj = "";
    private boolean isPaymentDone = true;
    //private String cacheResult;
    private ServicelistModal itemdetails;
    private TextView textrs, msgForBasicPlanText, msgForBasicPlanPrice, unlockPlanText;
    private FlowTextView textquestiondes;
    private String askAQuestionDataPage = "";
    private CircularNetworkImageView imgAstrologer;
    private ImageLoader mImageLoader;
    private RecyclerView recycler_view;
    private AskAQuestionTestimonialsAdapter askAQuestionTestimonialsAdapter;
    private List<AskAQuestionTestimonials> askAQuestionTestimonialList = new ArrayList<>();
    private TextView tvCustomerTestimonials;
    private LinearLayout llHeaderImagesContainer, llCustomerTestimonials;
    private TextView freeQuestionText, tvQuestionPriceText, tvFreeQuestionText, textDiscountPlan;
    private ImageView ivToggleImage;
    private String freeQues = "0", quesPrice = "";
    private String messageChatID = "", userProblem = "";
    private int countNotification;
    private final BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent
            countNotification = CUtils.getIntData(ActAskQuestion.this, CGlobalVariables.COUNTNOTIFICATIONCENTER, 0);
            setCountNotificationCenter();
        }
    };
    private boolean isNotification, isUserHAsPlanBoolean = false;
    private CredentialManager credentialManager;
    private boolean isEmailDialogueOpened;
    private String selectedEmailId;
    private static final int REQ_ONE_TAP = 1001;

    public ActAskQuestion() {
        super(R.string.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lay_astro_payment);
        activity = ActAskQuestion.this;
        queue = VolleySingleton.getInstance(ActAskQuestion.this).getRequestQueue();
        setBillingEventHandler(this);
        chatMessageArrayList = CUtils.getDataFromPrefrence(ActAskQuestion.this);
        countNotification = CUtils.getIntData(ActAskQuestion.this, CGlobalVariables.COUNTNOTIFICATIONCENTER, 0);
        if (chatMessageArrayList == null || chatMessageArrayList.size() <= 0) {
            getChatHistory();
        }
        init(true);
        if (chatMessageArrayList != null && chatMessageArrayList.size() > 0) {
            getAvailableQuestion();
            relativeLayoutNotificationCenter.setVisibility(View.VISIBLE);
        }

        //Getting 'true' from ActNotificationLanding Screen
        isNotification = getIntent().getBooleanExtra("isNotification", false);
        setProblemFieldIfAvailable(getIntent());
        credentialManager = CredentialManager.create(this);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        askAQuestionTestimonialList = new ArrayList<>();
        queue = VolleySingleton.getInstance(ActAskQuestion.this).getRequestQueue();
        chatMessageArrayList = CUtils.getDataFromPrefrence(ActAskQuestion.this);
        countNotification = CUtils.getIntData(ActAskQuestion.this, CGlobalVariables.COUNTNOTIFICATIONCENTER, 0);
        if (chatMessageArrayList == null || chatMessageArrayList.size() <= 0) {
            getChatHistory();
        }
        init(false);

        if (chatMessageArrayList != null && chatMessageArrayList.size() > 0) {
            getAvailableQuestion();
            relativeLayoutNotificationCenter.setVisibility(View.VISIBLE);
        }

        //Getting 'true' from ActNotificationLanding Screen
        isNotification = intent.getBooleanExtra("isNotification", false);
        setProblemFieldIfAvailable(intent);
    }

    /**
     * @author: Amit Rautela
     * This method is used to set the problem field, if available
     */
    private void setProblemFieldIfAvailable(Intent intent) {
        messageChatID = intent.getStringExtra(CGlobalVariables.key_messageChatID);
        userProblem = intent.getStringExtra("PROBLEM");
        layoutPosition = intent.getIntExtra(CGlobalVariables.key_layoutPosition, 0);
        if (userProblem != null && !userProblem.equals("")) {
            etUserquestion.setText(userProblem);
        }
    }

    public void getAvailableQuestion() {
        new NetworkQuestionDetail(ActAskQuestion.this, UserEmailFetcher.getEmail(this)).getAvailableQuestion();
    }

    public void showFreeQuestion(int numberOfQuestion) {
        try {
            String textFreeQuestion = getResources().getString(R.string.haveafreequestion);
            textFreeQuestion = textFreeQuestion.replace("#", "" + numberOfQuestion);
            freeQuestionText.setText(textFreeQuestion);
            freeQuestionText.setVisibility(View.VISIBLE);
            freeQues = "" + numberOfQuestion;
            updateAskAQuesPriceOnDrawer();
        } catch (Exception ex) {
            //
        }
    }

    /**
     * @author: Amit Rautela
     * This method is used to update the question price and free question on drawer
     */
    private void updateAskAQuesPriceOnDrawer() {
        if (drawerFragment != null) {
            drawerFragment.updateAskAQuestionPrice(freeQues, quesPrice);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }


    public void requestCredentials() {
        if (!isEmailDialogueOpened) {
            showGmailAccountPicker();
            //requestGoogleEmailHint();
        }
    }


    public void showGmailAccountPicker() {
        // Configure Google ID option
        GetGoogleIdOption googleIdOption = new GetGoogleIdOption.Builder()
                .setFilterByAuthorizedAccounts(false) // Show all accounts
                .setServerClientId(getString(R.string.default_web_client_id)) // Required
                .build();

        // Build the request
        GetCredentialRequest request = new GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOption)
                .build();

        // Launch Credential Manager UI
        credentialManager.getCredentialAsync(
                this,
                request,
                new CancellationSignal(),
                Executors.newSingleThreadExecutor(),
                new CredentialManagerCallback() {
                    @Override
                    public void onResult(Object responseObj) {
                        isEmailDialogueOpened = true;
                        setFocusOnEmail();
                        Log.e("TestGmailList"," onResult="+responseObj);
                        handleSignIn((GetCredentialResponse) responseObj);
                    }

                    @Override
                    public void onError(@NonNull Object o) {
                        Log.e("TestGmailList"," onError="+o);
                        isEmailDialogueOpened = true;
                        setFocusOnEmail();
                    }
                }
        );
    }

    private void handleSignIn(GetCredentialResponse credentialResponse) {
        try {
            Credential credential = credentialResponse.getCredential();
            Log.e("TestGmailList","handleSignIn credential="+credential);
            if (credential instanceof CustomCredential && credential.getType().equals(TYPE_GOOGLE_ID_TOKEN_CREDENTIAL)) {
                // Create Google ID Token
                Bundle credentialData = credential.getData();
                GoogleIdTokenCredential googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credentialData);
                selectedEmailId = googleIdTokenCredential.getId();
                Log.e("TestGmailList","handleSignIn selectedEmailId="+selectedEmailId);
                runOnUiThread(() -> {
                    try {
                        CUtils.saveAstroshopUserEmail(activity, selectedEmailId);
                        etUseremail.setText(selectedEmailId);
                    } catch (Exception e) {
                        //
                    }
                });
            }
        }catch (Exception e){
            Log.e("TestGmailList","handleSignIn Exception="+e);
        }
    }

    private void setFocusOnEmail() {
        runOnUiThread(() -> {
            etUseremail.setFocusableInTouchMode(true);
            etUseremail.setFocusable(true);
            etUseremail.requestFocus();
        });
    }


    private void requestGoogleEmailHint() {
        SignInClient oneTapClient = Identity.getSignInClient(this);

        BeginSignInRequest signInRequest = BeginSignInRequest.builder()
                .setGoogleIdTokenRequestOptions(
                        BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                                .setSupported(true)
                                .setServerClientId(getString(R.string.default_web_client_id))
                                .setFilterByAuthorizedAccounts(true) // Only show authorized accounts
                                //.setPrompt("select_account") // Force account selection
                                .build())
                .setAutoSelectEnabled(false) // Disable auto-selection
                .build();

        oneTapClient.beginSignIn(signInRequest)
                .addOnSuccessListener(this, result -> {
                    try {
                        startIntentSenderForResult(
                                result.getPendingIntent().getIntentSender(),
                                REQ_ONE_TAP,
                                null, 0, 0, 0);
                    } catch (Exception e) {
                        Log.e("EmailHint", "Couldn't start One Tap UI", e);
                    }
                })
                .addOnFailureListener(this, e -> {
                    Log.e("EmailHint", "Email hint failed", e);
                    // Fallback to manual input
                });
    }

    /***************************** Gmail choose dialogue end **********************/

    private void parseGsonData(String saveData) {

        try {
            List<ServicelistModal> data;
            Gson gson = new Gson();
            JsonElement element = gson.fromJson(saveData, JsonElement.class);
            data = gson.fromJson(saveData, new TypeToken<ArrayList<ServicelistModal>>() {
            }.getType());
            itemdetails = data.get(0);
            if (itemdetails != null) {
                setViewItem();
            }
        } catch (Exception ex) {

        }

    }

    private void setViewItem() {
        try {
            // String[] separated = null;
            if (itemdetails != null) {

                if (itemdetails.getAstrologerImageURL() != null && !itemdetails.getAstrologerImageURL().equals("")) {
                    imgAstrologer.setVisibility(View.VISIBLE);
                    mImageLoader = VolleySingleton.getInstance(this).getImageLoader();
                    imgAstrologer.setImageUrl(itemdetails.getAstrologerImageURL(), mImageLoader);
                } else {
                    imgAstrologer.setVisibility(View.GONE);
                }

                tvTitle.setText(itemdetails.getTitle());
                tvTitle.setTypeface(this.robotRegularTypeface);

                //  textquestiondes=(TextView)findViewById(R.id.textquestiondes);
                int planId = 1;
                planId = CUtils.getUserPurchasedPlanFromPreference(ActAskQuestion.this);

                /*
                 * For basic and Cloud plan user subscription discount
                 * If plan id = basic plan, silver monthly or gold monthly
                 * */
                if (planId == CGlobalVariables.BASIC_PLAN_ID /*|| planId == CGlobalVariables.SILVER_PLAN_ID_4 ||
                        planId == CGlobalVariables.GOLD_PLAN_ID_6*/) {
                    basicPlanUserLayout.setVisibility(View.VISIBLE);
                    msgForBasicPlanText.setText(getResources().getString(R.string.basic_user_text_dhruv));
                    msgForBasicPlanPrice.setText(getResources().getString(R.string.astroshop_dollar_sign) +
                            roundFunction(Double.parseDouble(itemdetails.getNonCloudPriceInDollor()), 2) +
                            " / " + "\n" + getResources().getString(R.string.astroshop_rupees_sign) + " " + roundFunction(Double.parseDouble(itemdetails.getNonCloudPriceInRS()), 2));

                    textrs.setText(itemdetails.getTitle() + ": " + getResources().getString(R.string.astroshop_dollar_sign) +
                            roundFunction(Double.parseDouble(itemdetails.getPriceInDollor()), 2) + " / " +
                            getResources().getString(R.string.astroshop_rupees_sign) +
                            roundFunction(Double.parseDouble(itemdetails.getPriceInRS()), 2));

                } else {
                    basicPlanUserLayout.setVisibility(View.GONE);
                    CUtils.showServiceProductDiscountedText(ActAskQuestion.this, textDiscountPlan, itemdetails.getMessageOfCloudPlanText1(),
                            itemdetails.getMessageOfCloudPlanText2(), CGlobalVariables.FROM_SERVICE_TEXT);
                    textrs.setText(itemdetails.getTitle() + ": " + getResources().getString(R.string.astroshop_dollar_sign) +
                            roundFunction(Double.parseDouble(itemdetails.getPriceInDollorBeforeCloudPlanDiscount()), 2) + " / " +
                            getResources().getString(R.string.astroshop_rupees_sign) +
                            roundFunction(Double.parseDouble(itemdetails.getPriceInRSBeforeCloudPlanDiscount()), 2));

                }

                textquestiondes.setText(itemdetails.getSmallDesc());
                quesPrice = getResources().getString(R.string.astroshop_dollar_sign) + roundFunction(Double.parseDouble(itemdetails.getPriceInDollor()), 2) + " / " + getResources().getString(R.string.astroshop_rupees_sign) + roundFunction(Double.parseDouble(itemdetails.getPriceInRS()), 2);
                updateAskAQuesPriceOnDrawer();
            }
        } catch (Exception ex) {
            //Log.i("Exp", ex.getMessage().toString());
        }
    }

    private double roundFunction(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.buy_now:
                if (itemdetails != null && validateData()) {
                    // isMessageNeedToSend = true;
                    if (isDataComingFromFragAskAQuesAdvertisementView) {

                        int noOfQuestion = CUtils.getIntData(ActAskQuestion.this, CGlobalVariables.NOOFQUESTIONAVAILABLE, 0);
                        //If number of question available
                        if (noOfQuestion > 0) {

                            final CustomProgressDialog pd = new CustomProgressDialog(this, typeface);
                            pd.setCancelable(false);
                            pd.show();

                            if (!CUtils.isConnectedWithInternet(ActAskQuestion.this)) {
                                MyCustomToast mct = new MyCustomToast(ActAskQuestion.this, ActAskQuestion.this
                                        .getLayoutInflater(), ActAskQuestion.this, typeface);
                                mct.show(getResources().getString(R.string.no_internet));
                            } else {

                                CUtils.saveIntData(ActAskQuestion.this, CGlobalVariables.NOOFQUESTIONAVAILABLE, --noOfQuestion);
                                setMessageDataAskByUser();
                                CUtils.saveBooleanData(ActAskQuestion.this, CGlobalVariables.Type_PAYMENT, true);
                                int count = CUtils.getIntData(ActAskQuestion.this, CGlobalVariables.COUNTNOTIFICATIONCENTER, 0);
                                CUtils.saveIntData(ActAskQuestion.this, CGlobalVariables.COUNTNOTIFICATIONCENTER, ++count);
                                saveDataOnInternalStorage();
                                setDataModel(beanHoroPersonalInfo, "Free");
                                convertInJsonObj(astrologerServiceInfo);
                                String problem = "";
                                if (astrologerServiceInfo != null && astrologerServiceInfo.getProblem() != null) {
                                    problem = astrologerServiceInfo.getProblem();
                                } else {
                                    problem = etUserquestion.getText().toString().trim();
                                }

                                boolean isAnswerUpdate = etUserquestion.getText().toString().trim().equals(userProblem);
                                //Checking if chat id is available or not
                                if (messageChatID != null && !messageChatID.equals("") && isAnswerUpdate) {
                                    //
                                } else {
                                    layoutPosition = chatMessageArrayList.size() - 1;
                                }

                                String price = "";
                                if (arayaskquestionPlan.size() > 0 && arayaskquestionPlan.get(price_amount_micros) != null)
                                    price = arayaskquestionPlan.get(price_amount_micros);

                                priceValue = price;
                                SendUserPurchaseReportForServiceToServerForAstroChat sendObj = new SendUserPurchaseReportForServiceToServerForAstroChat(this, "", UserEmailFetcher.getEmail(this), CUtils.getUserName(this), price, "", fullJsonDataObj, String.valueOf(layoutPosition), problem, "", "Order Free Question");
                                sendObj.sendReportToServerForFreeQue();
                            }


                        } else {

                            CUtils.saveBooleanData(ActAskQuestion.this, CGlobalVariables.Type_PAYMENT, false);

                            boolean googleWalletPaymentVisibility = CUtils.getBooleanData(ActAskQuestion.this, CGlobalVariables.key_GoogleWalletPaymentVisibility, true);
                            boolean paytmPaymentVisibility = CUtils.getBooleanData(ActAskQuestion.this, CGlobalVariables.key_PaytmPaymentVisibility, true);
                            boolean razorPaymentVisibility = CUtils.getBooleanData(ActAskQuestion.this, CGlobalVariables.key_RazorPayVisibilityServices, true);
                            boolean paytmPaymentVisibilityService = CUtils.getBooleanData(ActAskQuestion.this, CGlobalVariables.key_PaytmWalletVisibilityServices, true);

                            /*
                             * Added by Monika
                             * */
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

                    } else {
                        int screenId = isLocalKundliAvailable();
                        callToGetDetailsFromHomeInputModule(screenId);

                    }

                }

                break;

            case R.id.notificationcenter:
                openActNotificationLandingActivity();
                break;

            /*
             * Added by Monika
             * */
            case R.id.basic_plan_user_layout:

                CUtils.gotoProductPlanListUpdated(ActAskQuestion.this,
                        LANGUAGE_CODE, SUB_ACTIVITY_UPGRADE_PLAN_UPDTAE, CGlobalVariables.SCREEN_ID_DHRUV, "act_ask_a_question");

                break;

        }
    }

    private void init(boolean checkPermission) {
        LANGUAGE_CODE = ((AstrosageKundliApplication) ActAskQuestion.this.getApplication())
                .getLanguageCode();

        typeface = CUtils.getRobotoFont(
                ActAskQuestion.this, LANGUAGE_CODE, CGlobalVariables.regular);
        mct = new MyCustomToast(ActAskQuestion.this, ActAskQuestion.this
                .getLayoutInflater(), ActAskQuestion.this, typeface);

        beanHoroPersonalInfo = ((BeanHoroPersonalInfo) getIntent().getSerializableExtra("BeanHoroPersonalInfo"));
        beanHoroPersonalInfo1 = ((BeanHoroPersonalInfo) getIntent().getSerializableExtra("BeanHoroPersonalInfo1"));
        askAQuestionDataPage = getIntent().getStringExtra(CGlobalVariables.ask_A_Question_Data);

        isDataComingFromFragAskAQuesAdvertisementView = getIntent().getBooleanExtra(CGlobalVariables.DataComingFromAskAQuesAdvertisementView, false);
        if (getIntent().getExtras().containsKey(CGlobalVariables.IS_USER_HAS_PLAN)) {
            isUserHAsPlanBoolean = getIntent().getBooleanExtra(CGlobalVariables.IS_USER_HAS_PLAN, false);
        }
        if (isDataComingFromFragAskAQuesAdvertisementView) {
            fetchProductFromGoogleServer();
        }
        tool_barAppModule = findViewById(R.id.tool_barAppModule);
        setSupportActionBar(tool_barAppModule);

        ivToggleImage = findViewById(R.id.ivToggleImage);
        ivToggleImage.setVisibility(View.VISIBLE);

        drawerFragment = (HomeNavigationDrawerFragment) getSupportFragmentManager().
                findFragmentById(R.id.myDrawerFrag);

        drawerFragment.setup(R.id.myDrawerFrag, findViewById(R.id.drawerLayout), tool_barAppModule, getDrawerListItem(), getDrawerListItemIcon(), getDrawerListItemIndex());

        textquestiondes = findViewById(R.id.textquestiondes);
        textquestiondes.setTypeface(robotRegularTypeface);
        textquestiondes.setTextSize(getResources().getDimension(R.dimen.text_size));
        textquestiondes.setScrollBarStyle(ScrollView.SCROLLBARS_INSIDE_OVERLAY);

        imgAstrologer = findViewById(R.id.imgAstrologer);

        tvCustomerTestimonials = findViewById(R.id.tvCustomerTestimonials);
        tvCustomerTestimonials.setTypeface(mediumTypeface);

        llHeaderImagesContainer = findViewById(R.id.llHeaderImagesContainer);
        llCustomerTestimonials = findViewById(R.id.llCustomerTestimonials);

        reportLanguageOptions = findViewById(R.id.ics_spinner_lang);

        textDiscountPlan = findViewById(R.id.text_discount_plan);
        msgForBasicPlanText = findViewById(R.id.msg_for_basic_plan_text);
        msgForBasicPlanPrice = findViewById(R.id.msg_for_basic_plan_price);
        unlockPlanText = findViewById(R.id.unlock_plan_text);
        basicPlanUserLayout = findViewById(R.id.basic_plan_user_layout);

        basicPlanUserLayout.setOnClickListener(this);

        reportLanguageOptions.setPopupBackgroundResource(R.drawable.spinner_dropdown);
        reportLanguageOptions.setAdapter(getSpinnerAdapterLanguage(getResources().getStringArray(R.array.language_options_hi_eng)));
        if (LANGUAGE_CODE == CGlobalVariables.HINDI) {
            reportLanguageOptions.setSelection(1);
        } else {
            reportLanguageOptions.setSelection(0);
        }

        reportLanguageOptions.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                CUtils.hideMyKeyboard(ActAskQuestion.this);
                return false;
            }
        });


        if (!CUtils.getBooleanData(this, CGlobalVariables.key_AskAQuestionHeaderAndFooterVisibility, false)) {
            llHeaderImagesContainer.setVisibility(View.GONE);
            llCustomerTestimonials.setVisibility(View.GONE);
        } else {
            llHeaderImagesContainer.setVisibility(View.VISIBLE);
            llCustomerTestimonials.setVisibility(View.VISIBLE);
            recycler_view = findViewById(R.id.recycler_view);
            askAQuestionTestimonialsAdapter = new AskAQuestionTestimonialsAdapter(askAQuestionTestimonialList, robotRegularTypeface, this);
            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
            recycler_view.setHasFixedSize(true);
            recycler_view.setLayoutManager(mLayoutManager);
            recycler_view.setItemAnimator(new DefaultItemAnimator());
            recycler_view.setAdapter(askAQuestionTestimonialsAdapter);
            recycler_view.setNestedScrollingEnabled(false);

            getTestimonialData();
        }

        tvTitle = findViewById(R.id.tvTitle);
        try {
            DisplayMetrics displaymetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int height = displaymetrics.heightPixels;
            int width = displaymetrics.widthPixels;
            int tvWidth = (int) (width * 0.6);
            tvTitle.setWidth(tvWidth);
        } catch (Exception ex) {

        }

        textrs = findViewById(R.id.textrs);

        if (!CUtils.isConnectedWithInternet(ActAskQuestion.this)) {
            MyCustomToast mct = new MyCustomToast(ActAskQuestion.this, ActAskQuestion.this
                    .getLayoutInflater(), ActAskQuestion.this, typeface);
            mct.show(getResources().getString(R.string.no_internet));
            ActAskQuestion.this.finish();
        } else {
            if (askAQuestionDataPage == null || askAQuestionDataPage.equals("")) {
                askAQuestionDataPage = CGlobalVariables.ask_A_Question_Android;
            }
            String url = CGlobalVariables.astroShopServiceAskaQuestion + "service=" + CGlobalVariables.ask_A_Question_Android + "&languagecode=" + LANGUAGE_CODE;
//            String url = buildAskQuestionServiceUrl(LANGUAGE_CODE);

            checkCachedData(url);
        }


        tabLayout = findViewById(R.id.tabs);
        tabLayout.setVisibility(View.GONE);
        freeQuestionText = findViewById(R.id.freequestion);
        tvQuestionPriceText = findViewById(R.id.tvQuestionPriceText);
        tvFreeQuestionText = findViewById(R.id.tvQuestionPriceText);

        tvQuestionPriceText.setTypeface(robotRegularTypeface);
        tvFreeQuestionText.setTypeface(robotRegularTypeface);
        freeQuestionText.setTypeface(robotRegularTypeface);

        astrologerServiceInfo = new AstrologerServiceInfo();
        queue = VolleySingleton.getInstance(ActAskQuestion.this).getRequestQueue();
        buy_now = findViewById(R.id.buy_now);
        if (isDataComingFromFragAskAQuesAdvertisementView) {
            buy_now.setText(getResources().getString(R.string.astroshop_buy_now));
        } else {
            buy_now.setText(getResources().getString(R.string.button_next_ask_question));
        }

        buy_now.setTypeface(robotMediumTypeface);

        buy_now.setOnClickListener(this);

        etUseremail = findViewById(R.id.etUseremail);
        etUseremail.addTextChangedListener(new MyTextWatcher(etUseremail));

        etUsermobno = findViewById(R.id.etUsermobno);
        etUsermobno.addTextChangedListener(new MyTextWatcher(etUsermobno));

        etUserquestion = findViewById(R.id.etUserquestion);
        etUserquestion.addTextChangedListener(new MyTextWatcher(etUserquestion));

        input_layout_email = findViewById(R.id.input_layout_email);
        input_layout_mobno = findViewById(R.id.input_layout_mobno);
        input_layout_question = findViewById(R.id.input_layout_question);

        relativeLayoutNotificationCenter = findViewById(R.id.notificationcenter);
        relativeLayoutNotificationCenter.setVisibility(View.GONE);
        relativeLayoutNotificationCenter.setOnClickListener(this);
        countTextViewNotificationCenter = findViewById(R.id.notificationcount);
        countTextViewNotificationCenter.setVisibility(View.GONE);
        setCountNotificationCenter();

        boolean gotEmail = false;
        String email_id = CUtils.getAstroshopUserEmail(ActAskQuestion.this);

        if (!email_id.isEmpty() && !gotEmail) {
            etUseremail.setText(email_id);
        } else if (!gotEmail) {
            //etUseremail.setFocusable(false);
            etUseremail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /*if (isGoogleApiClientConnected) {
                        requestCredentials();
                    } else {
                        setFocusOnEmail();
                    }*/

                    //requestCredentials();
                }
            });
        }

        String preferenceMobNo = CUtils.getStringData(ActAskQuestion.this, CGlobalVariables.ASTROASKAQUESTIONUSERPHONENUMBER, "");
        if (!preferenceMobNo.equals("")) {
            etUsermobno.setText(preferenceMobNo);
        }
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);

        TextView txtMoneyBack = findViewById(R.id.txtMoneyBack);
        txtMoneyBack.setTypeface(mediumTypeface);
        TextView txtSecurePayment = findViewById(R.id.txtSecurePayment);
        txtSecurePayment.setTypeface(mediumTypeface);
        TextView txtVerifiedAstrologers = findViewById(R.id.txtVerifiedAstrologers);
        txtVerifiedAstrologers.setTypeface(mediumTypeface);

    }

    /**
     * Builds the Ask-a-Question service-list URL with the signature key as a query param.
     * This matches the backend expectation where callers use:
     * `...service-list-customized-v7.asp?service=...&lang=...&key=...`
     */
    private String buildAskQuestionServiceUrl(int languageCode) {
        String base = CGlobalVariables.astroShopServiceAskaQuestion;
        if (TextUtils.isEmpty(base)) {
            base = "";
        }

        StringBuilder url = new StringBuilder(base);
        if (!base.contains("?")) {
            url.append("?");
        } else if (!base.endsWith("?") && !base.endsWith("&")) {
            url.append("&");
        }

        url.append("service=").append(CGlobalVariables.ask_A_Question_Android);
        url.append("&lang=").append(languageCode);

        String signatureKey = CUtils.getApplicationSignatureHashCode(ActAskQuestion.this);
        if (!TextUtils.isEmpty(signatureKey)) {
            url.append("&key=").append(signatureKey);
        }
        return url.toString();
    }

    /*
     * Set the Report language spinner adapter
     * */
    private ArrayAdapter<CharSequence> getSpinnerAdapterLanguage(String[] spinnerOptions) {
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(ActAskQuestion.this, R.layout.spinner_list_item, spinnerOptions) {
            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                if (position == CGlobalVariables.HINDI) {
                    ((TextView) v).setTypeface(CustomTypefaces.get(ActAskQuestion.this, "hi")); ///cmnt by neeraj 5/5/16
                } else {
                    ((TextView) v).setTypeface(robotRegularTypeface);
                }
                ((TextView) v).setTextSize(16);
                v.setPadding(5, 10, 10, 0);
                v.setBackgroundColor(getResources().getColor(R.color.backgroundColorView));
                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);

                if (position == CGlobalVariables.HINDI) {
                    ((TextView) v).setTypeface(CustomTypefaces.get(ActAskQuestion.this, "hi")); //cmnt by neeraj 5/5/16
                } else {
                    ((TextView) v).setTypeface(robotRegularTypeface);
                }
                ((TextView) v).setTextSize(16);

                return v;
            }
        };
        return adapter;
    }

    /**
     * This method is used to parse astrologer data
     *
     * @param cache
     * @return
     */
    private List<AllAstrologerInfo> getAstrologerParsingData(String cache) {

        try {
            List<AllAstrologerInfo> astroListData = null;
            Gson gson = new Gson();
            astroListData = gson.fromJson(cache, new TypeToken<ArrayList<AllAstrologerInfo>>() {
            }.getType());
            return astroListData;
        } catch (Exception ex) {
            CUtils.saveStringData(ActAskQuestion.this, CGlobalVariables.All_Astrologer_List, "");
            return null;
        }
    }

    private void setRunTimePermission(boolean isUSerPermissionDialogOpen) {
        if (CUtils.isContactsPermissionGranted(this, this, PERMISSION_CONTACTS, isUSerPermissionDialogOpen)) {
            setEmailIdFromUserAccount();
        }
    }

    private void setEmailIdFromUserAccount() {
        String email = UserEmailFetcher.getEmail(ActAskQuestion.this);
        if (email != null) {
            etUseremail.setText(email);
        }
    }

    private void getTestimonialData() {

        AskAQuestionTestimonials bean = new AskAQuestionTestimonials(R.drawable.ic_testimonial_1, getResources().getString(R.string.customer_testimonials_1), getResources().getString(R.string.testimonial_user1));
        AskAQuestionTestimonials bean1 = new AskAQuestionTestimonials(R.drawable.ic_testimonial_2, getResources().getString(R.string.customer_testimonials_2), getResources().getString(R.string.testimonial_user2));
        AskAQuestionTestimonials bean2 = new AskAQuestionTestimonials(R.drawable.ic_testimonial_4, getResources().getString(R.string.customer_testimonials_3), getResources().getString(R.string.testimonial_user3));

        askAQuestionTestimonialList.add(bean);
        askAQuestionTestimonialList.add(bean1);
        askAQuestionTestimonialList.add(bean2);
        //askAQuestionTestimonialList.add(bean3);

        askAQuestionTestimonialsAdapter.notifyDataSetChanged();
    }

    private boolean validateData() {
        boolean flag = false;
        if (validateName(etUseremail, input_layout_email, getString(R.string.email_one_v))
                && validateName(etUsermobno, input_layout_mobno, getString(R.string.astro_shop_User_mob_no))
                && validateName(etUserquestion, input_layout_question, getString(R.string.astrologer_user_query))) {
            CUtils.saveStringData(ActAskQuestion.this, CGlobalVariables.ASTROASKAQUESTIONUSERPHONENUMBER, etUsermobno.getText().toString());
            CUtils.saveStringData(ActAskQuestion.this, CGlobalVariables.ASTROASKAQUESTIONUSEREMAIL, etUseremail.getText().toString());

            flag = true;
        }

        return flag;
    }

    private boolean validateName(EditText name, TextInputLayout inputLayout, String message) {
        boolean value = true;


        if (name == etUseremail) {

            if (etUseremail.getText().toString().trim().isEmpty()) {
                input_layout_email.setError(null);
                input_layout_email.setErrorEnabled(false);
                input_layout_email.setError(getResources().getString(R.string.email_one_v));
                inputLayout.setErrorEnabled(true);
                requestFocus(etUseremail);
                name.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                value = false;
                // etUseremail.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
            }
            if (!(CUtils.isValidEmail(etUseremail.getText().toString().trim()))) {
                input_layout_email.setError(null);
                input_layout_email.setErrorEnabled(false);
                input_layout_email.setError(getResources().getString(R.string.email_one_v_astro_service));
                inputLayout.setErrorEnabled(true);
                requestFocus(etUseremail);
                name.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                value = false;
                // etUseremail.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
            } else {
                input_layout_email.setErrorEnabled(false);
                input_layout_email.setError(null);
                etUseremail.getBackground().setColorFilter(getResources().getColor(R.color.ColorPrimary), PorterDuff.Mode.SRC_ATOP);
            }


        }


        if (name == etUserquestion) {
            if (name.getText().toString().trim().isEmpty() || name.getText().toString().trim().length() < 1) {
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

        if (name == etUsermobno) {
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
            ActAskQuestion.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    @Override
    public void onRetryClick() {

        boolean googleWalletPaymentVisibility = CUtils.getBooleanData(ActAskQuestion.this, CGlobalVariables.key_GoogleWalletPaymentVisibility, true);
        boolean paytmPaymentVisibility = CUtils.getBooleanData(ActAskQuestion.this, CGlobalVariables.key_PaytmPaymentVisibility, true);
        boolean razorPaymentVisibility = CUtils.getBooleanData(ActAskQuestion.this, CGlobalVariables.key_RazorPayVisibilityServices, true);
        boolean paytmPaymentVisibilityService = CUtils.getBooleanData(ActAskQuestion.this, CGlobalVariables.key_PaytmWalletVisibilityServices, true);


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
        //gotBuyAskQuestionPlan();

    }

    @Override
    public void onSelectedOption(int opt, String mob) {

        if (opt == R.id.radioGoogle) {
            setDataModel(beanHoroPersonalInfo, "Google");

        } else if (opt == R.id.radioRazor)  // Added by Monika
        {
            setDataModel(beanHoroPersonalInfo, "RazorPay");
        } else {
            setDataModel(beanHoroPersonalInfo, "Paytm");
        }

        // setDataModel(beanHoroPersonalInfo);
        convertInJsonObj(astrologerServiceInfo);
        setMessageDataAskByUser();
        saveDataOnInternalStorage();

        //
        boolean isAnswerUpdate = etUserquestion.getText().toString().trim().equals(userProblem);
        //Checking if chat id is available or not
        if (messageChatID != null && !messageChatID.equals("") && isAnswerUpdate) {
            //
        } else {
            layoutPosition = chatMessageArrayList.size() - 1;
        }


        int count = CUtils.getIntData(ActAskQuestion.this, CGlobalVariables.COUNTNOTIFICATIONCENTER, 0);
        CUtils.saveIntData(ActAskQuestion.this, CGlobalVariables.COUNTNOTIFICATIONCENTER, ++count);
        countNotification = CUtils.getIntData(ActAskQuestion.this, CGlobalVariables.COUNTNOTIFICATIONCENTER, 0);
        setCountNotificationCenter();

        if (opt == R.id.radioGoogle) {

            if (validateData()) {
                //astrologerServiceInfo.setPayMode(payMode);
                int screenId = isLocalKundliAvailable();
                callToGetDetailsFromHomeInputModule(screenId);
            }

        } else if (opt == R.id.radioRazor) {
            if (validateData()) {
                CUtils.googleAnalyticSendWitPlayServie(ActAskQuestion.this,
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_ASKAQUESTION_RAZORPAY, null);

                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_ASKAQUESTION_RAZORPAY, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                Typeface typeface = CUtils.getRobotoFont(
                        ActAskQuestion.this, LANGUAGE_CODE, CGlobalVariables.regular);
                RequestQueue queue = VolleySingleton.getInstance(ActAskQuestion.this).getRequestQueue();

                if (!CUtils.isConnectedWithInternet(ActAskQuestion.this)) {
                    MyCustomToast mct = new MyCustomToast(ActAskQuestion.this, ActAskQuestion.this
                            .getLayoutInflater(), ActAskQuestion.this, typeface);
                    mct.show(getResources().getString(R.string.no_internet));
                } else {

                    CUtils.getOrderIDAndChatId(ActAskQuestion.this, typeface, queue, itemdetails, fullJsonDataObj, astrologerServiceInfo, "");
                    //CUtils.getOrderID(ActAskQuestion.this, typeface, queue, astrologerServiceInfo);
                }
            }

        } else if (validateData()) {

            CUtils.googleAnalyticSendWitPlayServie(this,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                    CGlobalVariables.GOOGLE_ANALYTIC_ASKAQUESTION_PAYTM, null);
            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_ASKAQUESTION_PAYTM, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

            if (!CUtils.isConnectedWithInternet(ActAskQuestion.this)) {
                MyCustomToast mct = new MyCustomToast(ActAskQuestion.this, ActAskQuestion.this
                        .getLayoutInflater(), ActAskQuestion.this, typeface);
                mct.show(getResources().getString(R.string.no_internet));
            } else {
                CUtils.getOrderIDAndChatId(ActAskQuestion.this, typeface, queue, itemdetails, fullJsonDataObj, astrologerServiceInfo, "");
            }
        }

    }

    @Override
    public void getCallBack(String result, CUtils.callBack callback, String priceInDollor, String priceInRs) {
        if (callback == CUtils.callBack.GET_ORDER_ID) {
            order_Id = result;
            //com.google.analytics.tracking.android.
            // Log.e("order" + order_Id);
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
                    //Need to send amount to razor pay in paise
                    Double paiseAmount = amount * 100;
                    // API calling
                    //showProgressBar();
                    //new VolleyServerRequest(this, (OnVolleyResultListener) ActAskQuestion.this, CGlobalVariables.RAZORPAY_ORDERID_URL, CUtils.getRazorOrderIdParams(paiseAmount,"INR",order_Id));
                    goToRazorPayFlow();
                } else {
                    new GetCheckSum(ActAskQuestion.this, typeface).getCheckSum(getchecksumparams(), 0);
                    //CUtils.getCheckSum(ActAskQuestion.this, getchecksumparams(), typeface);
                }

            } else {
                MyCustomToast mct = new MyCustomToast(ActAskQuestion.this,
                        ActAskQuestion.this.getLayoutInflater(),
                        ActAskQuestion.this, regularTypeface);
                mct.show(getResources().getString(R.string.order_fail));
            }
        } else if (callback == CUtils.callBack.GET_CHECKSUM) {
            String checksum = result;
            if (!result.isEmpty()) {
                startPaytmPayment(order_Id, astrologerServiceInfo.getPriceRs(), checksum);
            } else {
                MyCustomToast mct = new MyCustomToast(ActAskQuestion.this,
                        ActAskQuestion.this.getLayoutInflater(),
                        ActAskQuestion.this, regularTypeface);
                mct.show(getResources().getString(R.string.order_fail));
            }
        } else if (callback == CUtils.callBack.POST_STATUS) {
            String postStatus = result;
            if (postStatus != null && postStatus.equalsIgnoreCase("1")) {

                if (payStatus.equalsIgnoreCase("1")) {

                    Intent intent = new Intent(ActAskQuestion.this, ActNotificationLanding.class);
                    startActivity(intent);

                    onPurchaseCompleted(itemdetails, order_Id);

                } else {
                    CUtils.googleAnalyticSendWitPlayServie(this,
                            CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                            CGlobalVariables.GOOGLE_ANALYTIC_PAYMENT_FAILED_PAYTM, null);
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_PAYMENT_FAILED_PAYTM, CGlobalVariables.FIREBASE_EVENT_PAYMENT_FAILED, "");
                    openPaymentFailedDialog();
                }

            } else {
                MyCustomToast mct = new MyCustomToast(ActAskQuestion.this,
                        ActAskQuestion.this.getLayoutInflater(),
                        ActAskQuestion.this, regularTypeface);
                mct.show(getResources().getString(R.string.order_fail));
            }
            //After Razorpay success payment
        } else if (callback == CUtils.callBack.POST_RAZORPAYSTATUS) {
            String postStatus = result;
            if (postStatus != null && postStatus.equalsIgnoreCase("1")) {
                if (payStatus.equalsIgnoreCase("1")) {

                    // CUtils.emailPDF(ActAskQuestion.this, CGlobalVariables.RAZORPAY_EMAIL_PDF, mediumTypeface, queue, order_Id, payStatus, astrologerServiceInfo);
                    onPurchaseCompleted(itemdetails, order_Id);

                    Intent intent = new Intent(ActAskQuestion.this, ActNotificationLanding.class);
                    startActivity(intent);

                } else {
                    openPaymentFailedDialog();
                    CUtils.googleAnalyticSendWitPlayServie(this,
                            CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                            CGlobalVariables.GOOGLE_ANALYTIC_SERVICE_RAZOR_PAYMENT_FAILED, null);
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SERVICE_RAZOR_PAYMENT_FAILED, CGlobalVariables.FIREBASE_EVENT_PAYMENT_FAILED, "");

                }

            } else {
                MyCustomToast mct = new MyCustomToast(ActAskQuestion.this,
                        ActAskQuestion.this.getLayoutInflater(),
                        ActAskQuestion.this, regularTypeface);
                // mct.show(getResources().getString(R.string.order_fail));
                openPaymentFailedDialog();
            }
        }
    }

    private void goToRazorPayFlow() {

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
            //added by Ankit on 3-7-2019 for Razorpay Webhook
            notes.put("orderId", order_Id);
            notes.put("chatId", chat_Id);
            notes.put("orderType", CGlobalVariables.PAYMENT_TYPE_SERVICE);
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

                CUtils.updatePaidStatus(ActAskQuestion.this, layoutPosition, order_Id, "0", chat_Id, astrologerServiceInfo);

                if (astrologerServiceInfo.getPayMode().equalsIgnoreCase("Google")) {
                    purchaseServiceByInApp();
                } else if (astrologerServiceInfo.getPayMode().equalsIgnoreCase("RazorPay")) {
                    Double amount = Double.parseDouble(astrologerServiceInfo.getPriceRs());
                    //Need to send amount to razor pay in paise
                    Double paiseAmount = amount * 100;
                    // API calling
                    //showProgressBar();
                    //new VolleyServerRequest(this, (OnVolleyResultListener) ActAskQuestion.this, CGlobalVariables.RAZORPAY_ORDERID_URL, CUtils.getRazorOrderIdParams(paiseAmount,"INR",order_Id));
                    goToRazorPayFlow();
                } else {
                    new GetCheckSum(ActAskQuestion.this, typeface).getCheckSum(getchecksumparams(), 0);
                    //CUtils.getCheckSum(ActAskQuestion.this, getchecksumparams(), typeface);
                }

                /*PaytmPGService Service = PaytmPGService.getProductionService();
                CUtils.startPaytmPayment(Service, ActAskQuestion.this, astrologerServiceInfo.getEmailID(), astrologerServiceInfo.getMobileNo(), order_Id, itemdetails.getPriceInRS(),"checksum");
                */
            } else {
                MyCustomToast mct = new MyCustomToast(ActAskQuestion.this,
                        ActAskQuestion.this.getLayoutInflater(),
                        ActAskQuestion.this, regularTypeface);
                mct.show(getResources().getString(R.string.order_fail));
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    public void isEmailIdPermissionGranted(boolean isPermissionGranted) {
        setRunTimePermission(true);
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {

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
                        CGlobalVariables.GOOGLE_ANALYTIC_SERVICE_RAZOR_PAYMENT_SUCCESS, null, dPrice, "");
            }
            payStatus = "1";
            payId = razorpayPaymentID;
            RequestQueue queue = VolleySingleton.getInstance(ActAskQuestion.this).getRequestQueue();

            CUtils.updatePaidStatus(ActAskQuestion.this, layoutPosition, order_Id, payStatus, chat_Id, astrologerServiceInfo);
            CUtils.postAskQuestionRazorpayDataToServer(ActAskQuestion.this, mediumTypeface, queue, order_Id, chat_Id, payStatus, astrologerServiceInfo, CGlobalVariables.UPDATE_RAZORPAY_STATUS_CHAT, payId, "");

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
            RequestQueue queue = VolleySingleton.getInstance(ActAskQuestion.this).getRequestQueue();
            CUtils.saveBooleanData(this,CGlobalVariables.IS_SUBSCRIPTION_ERROR,true);
            CUtils.updatePaidStatus(ActAskQuestion.this, layoutPosition, order_Id, payStatus, chat_Id, astrologerServiceInfo);
            CUtils.postAskQuestionRazorpayDataToServer(ActAskQuestion.this, mediumTypeface, queue, order_Id, chat_Id, payStatus, astrologerServiceInfo, CGlobalVariables.UPDATE_RAZORPAY_STATUS_CHAT, "", status);
            // Toast.makeText(this, "Payment failed: " + i + " " + response, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            //Log.e("Service pay", "Exception in onPaymentError", e);
        }
    }

    private void setDataModel(BeanHoroPersonalInfo beanHoroPersonalInfo, String paymode) {

        if (beanHoroPersonalInfo != null) {
            astrologerServiceInfo.setKey(CUtils.getApplicationSignatureHashCode(this));
            astrologerServiceInfo.setPayMode(paymode);
            astrologerServiceInfo.setKphn("" + beanHoroPersonalInfo.getHoraryNumber());

            String gender = beanHoroPersonalInfo.getGender();
            astrologerServiceInfo.setGender(gender);
            // email_id_for_question= UserEmailFetcher.getEmail(ActAstroPaymentOptions.this);
            astrologerServiceInfo.setEmailID(etUseremail.getText().toString().trim());

            String username = beanHoroPersonalInfo.getName();
            astrologerServiceInfo.setRegName(username); //Temporarily changes by chandan
            //astrologerServiceInfo.setRegName(email_id_for_question);

            BeanDateTime beanDateTime = beanHoroPersonalInfo.getDateTime();
            astrologerServiceInfo.setDateOfBirth(String.valueOf(beanDateTime.getDay()));
            astrologerServiceInfo.setMonthOfBirth(String.valueOf(beanDateTime.getMonth() + 1));
            astrologerServiceInfo.setYearOfBirth(String.valueOf(beanDateTime.getYear()));
            astrologerServiceInfo.setHourOfBirth(String.valueOf(beanDateTime.getHour()));
            astrologerServiceInfo.setMinOfBirth(String.valueOf(beanDateTime.getMin()));
            astrologerServiceInfo.setDst("" + beanHoroPersonalInfo.getDST());

            BeanPlace place = beanHoroPersonalInfo.getPlace();
            String city = place.getCityName();
            String state = place.getState();
            String country = place.getCountryName();
            astrologerServiceInfo.setPlace(city);
            astrologerServiceInfo.setNearCity(city);

            astrologerServiceInfo.setLongMinOfBirth(place.getLongMin());
            astrologerServiceInfo.setLongEWOfBirth(place.getLongDir());
            astrologerServiceInfo.setLongDegOfBirth(place.getLongDeg());
            astrologerServiceInfo.setLatDegOfBirth(place.getLatDeg());
            astrologerServiceInfo.setLatMinOfBirth(place.getLatMin());
            astrologerServiceInfo.setLatNSOfBirth(place.getLatDir());
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
            float tz = CUtils.getAdjustedTimezone(astrologerServiceInfo.getDst(), astrologerServiceInfo.getTimezone());
            astrologerServiceInfo.setTimezone("" + tz);


            //Give the user report language code (0 for Hindi and 1 for English)
            String reportLanguageCode = "hi";
            int language = reportLanguageOptions.getSelectedItemPosition();
            if (language != CGlobalVariables.HINDI) {
                reportLanguageCode = "en";
            }

            String problemStr = etUserquestion.getText().toString().trim();
            problemStr = problemStr + "^^Report lang: " + reportLanguageCode;

            if (beanHoroPersonalInfo1 != null) {
                //girl details if exits
                problemStr = problemStr + getGirlDetailInString(beanHoroPersonalInfo1);
            }
            problemStr = problemStr + " ## ";

            //To know from where Ask a Question calls
            if (askAQuestionDataPage == null || askAQuestionDataPage.equals("")) {
                problemStr = problemStr + " - (" + CGlobalVariables.ask_A_Question_Android + ")";
            } else {
                problemStr = problemStr + " - (" + askAQuestionDataPage + ")";
            }

            Log.e("Problem setModel ", problemStr);
            String appVerName = LibCUtils.getApplicationVersionToShow(ActAskQuestion.this);


            /*(lang: Eng, appversion: 7.0)
           ask a que - (ask-a-question-android)(lang: Hi, appversion: 7.0)*/

            problemStr = " " + problemStr + " " + "(lang: " + CUtils.getLanguageKey(LANGUAGE_CODE) + ", " + "appversion: " + appVerName + ")";

            astrologerServiceInfo.setProblem(problemStr);
                /*if (checkBoxbirth.isChecked()) {
                    astrologerServiceInfo.setKnowDOB("1");
                } else {*/
            astrologerServiceInfo.setKnowDOB("0");
            //}
                /*if (checkBoxtimeofbirth.isChecked()) {
                    astrologerServiceInfo.setKnowTOB("1");
                } else {*/
            astrologerServiceInfo.setKnowTOB("0");
            //}
            astrologerServiceInfo.setMobileNo(etUsermobno.getText().toString().trim());
            astrologerServiceInfo.setPriceRs(itemdetails.getPriceInRS());
            astrologerServiceInfo.setPrice(itemdetails.getPriceInDollor());
            astrologerServiceInfo.setServiceId(itemdetails.getServiceId());
            astrologerServiceInfo.setProfileId("");
            astrologerServiceInfo.setBillingCountry("");

            astrologerServiceInfo.setLatitude(place.getLatDeg() + place.getLatDir() + place.getLatMin());
            astrologerServiceInfo.setLongitude(place.getLongDeg() + place.getLongDir() + place.getLongMin());
            if (itemdetails != null)
                astrologerServiceInfo.setPriceRs(itemdetails.getPriceInRS());
            else
                astrologerServiceInfo.setPriceRs("");


        }
        // }
    }

    private void convertInJsonObj(AstrologerServiceInfo localOrderModal) {
        localOrderModal.setPrtnr_id(CGlobalVariables.currentSession);
        Gson gson = new Gson();
        fullJsonDataObj = gson.toJson(localOrderModal);


        System.out.println("obj" + fullJsonDataObj);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*if (requestCode == RC_HINT_EMAIL) {
            isEmailDialogueOpened = true;
            setFocusOnEmail();
            if (resultCode == RESULT_OK) {
                Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                selectedEmailId = credential.getId();
                CUtils.saveAstroshopUserEmail(activity, selectedEmailId);
                etUseremail.setText(selectedEmailId);
            }
        } else */
        if (requestCode == HomeInputScreen.requestCodePaymentFailureScreen) {
            if (resultCode == HomeInputScreen.resultCodeTryAgain) {
                // isMessageNeedToSend=false;
                boolean googleWalletPaymentVisibility = CUtils.getBooleanData(ActAskQuestion.this, CGlobalVariables.key_GoogleWalletPaymentVisibility, true);
                boolean paytmPaymentVisibility = CUtils.getBooleanData(ActAskQuestion.this, CGlobalVariables.key_PaytmPaymentVisibility, true);
                boolean razorPaymentVisibility = CUtils.getBooleanData(ActAskQuestion.this, CGlobalVariables.key_RazorPayVisibilityServices, true);
                boolean paytmPaymentVisibilityService = CUtils.getBooleanData(ActAskQuestion.this, CGlobalVariables.key_PaytmWalletVisibilityServices, true);

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
                //  gotBuyAskQuestionPlan();
            } else if (requestCode == HomeInputScreen.resultCodeCancel) {
                //Do nothing
            }
        } else if (requestCode == PERMISSION_CONTACTS) {
            setRunTimePermission(false);
        } else if (requestCode == SUB_ACTIVITY_USER_LOGIN) {
            if (resultCode == RESULT_OK) {
                Bundle b = data.getExtras();
                String loginName = b.getString("LOGIN_NAME");
                String loginPwd = b.getString("LOGIN_PWD");
                setUserLoginDetails(loginName, loginPwd);
            }
        } else if (requestCode == CGlobalVariables.REQUEST_CODE_PAYTM) {
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

    }

    @Override
    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {
        // Logic from onActivityResult should be moved here.
        String astroSageUserId = CUtils.getUserName(this);
        dumpDataString = dumpDataString + " onPurchasesUpdated() responseCode=" + billingResult.getResponseCode() + " astroSageUserId=" + astroSageUserId;
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
            Log.e("BillingClient", "onPurchasesUpdated() OK");
            dumpDataString = dumpDataString + " ResponseOK";
            if (purchases != null && !purchases.isEmpty()) {
                Purchase purchase = purchases.get(0);
                dumpDataString = dumpDataString + " signature=" + purchase.getSignature() + " purchaseData=" + purchase.getOriginalJson();
                processGooglePaymentAfterSuccess(purchase);
            }
        } else {
            Log.e("BillingClient", "onPurchasesUpdated() FAIL = " + purchases);
            dumpDataString = dumpDataString + " ResponseFAIL";
            String resgisteredEmail = UserEmailFetcher.getEmail(this);
            String userRegisterEmail = CUtils.getStringData(this, CGlobalVariables.USERREGISTEREMAILID, resgisteredEmail);
            String userName = CUtils.getUserName(this);
            String problem = "";
            if (astrologerServiceInfo.getProblem() != null) {
                problem = astrologerServiceInfo.getProblem();
            } else {
                problem = etUserquestion.getText().toString().trim();
            }

            String price = "0";
            if (arayaskquestionPlan.get(price_amount_micros) != null)
                price = arayaskquestionPlan.get(price_amount_micros);

            if (!TextUtils.isEmpty(chat_Id)) {
                messageChatID = chat_Id;
            }

            SendUserPurchaseReportForServiceToServerForAstroChat sendObj = new SendUserPurchaseReportForServiceToServerForAstroChat(this, "", userRegisterEmail, userName, price, "", fullJsonDataObj, "" + layoutPosition, problem, messageChatID, "", order_Id);
            sendObj.sendReportToServer();
            openPaymentFailedDialog();
        }
        CUtils.postDumpDataToServer(ActAskQuestion.this, dumpDataString);
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
            CUtils.saveBooleanData(ActAskQuestion.this, CGlobalVariables.PAYMENTDONE_KEY_FOR_ASTRO_SERVICES_ISPAYMENTDONE, isPaymentDone);

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

    private void setUserLoginDetails(String loginName, String loginPwd) {
        if (drawerFragment != null)
            drawerFragment.updateLoginDetials(true, loginName, loginPwd, getDrawerListItem(), getDrawerListItemIcon(), getDrawerListItemIndex());
    }

    @Override
    public void logoutFromAstroSageCloud(boolean isShowToast) {
        if (drawerFragment != null)
            drawerFragment.updateLoginDetials(false, "", "", getDrawerListItem(), getDrawerListItemIcon(), getDrawerListItemIndex());

        if (isShowToast) {
            MyCustomToast mct = new MyCustomToast(ActAskQuestion.this,
                    ActAskQuestion.this.getLayoutInflater(), ActAskQuestion.this,
                    regularTypeface);
            mct.show(getResources().getString(R.string.sign_out_success));
        }
    }

    private void openPaymentFailedDialog() {
        CUtils.saveBooleanData(this,CGlobalVariables.IS_SUBSCRIPTION_ERROR,true);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        PaymentFailedDailogFrag pfdf = new PaymentFailedDailogFrag();
        ft.add(pfdf, null);
        ft.commitAllowingStateLoss();
    }

    private void gotoThanksPage(Purchase purchase, String plan, String price, String priceCurrencycode) {

        signature = purchase.getSignature();
        purchaseData = purchase.getOriginalJson();

        ActAskQuestion.this.getSharedPreferences("MISC_PUR_SERVICE", Context.MODE_PRIVATE).edit()
                .putString("VALUE_SERVICE", purchaseData).commit();
        String astroSageUserId = CUtils.getUserName(ActAskQuestion.this);
        saveDataInPreferences(astroSageUserId, price, priceCurrencycode);
        // FOR START A SERVICE
        verifyPurchaseFromService(astroSageUserId, price, priceCurrencycode);

        // update status
        CUtils.updatePaidStatus(ActAskQuestion.this, layoutPosition, "", "1", messageChatID, astrologerServiceInfo);


        Intent intent = new Intent(ActAskQuestion.this, ActNotificationLanding.class);
        startActivity(intent);
    }

    private void verifyPurchaseFromService(String astroSageUserId, String price, String priceCurrencycode) {
        Intent pvsIntent = new Intent(getApplicationContext(),
                VerificationServiceForInAppBillingChat.class);
        pvsIntent.putExtra("SIGNATURE", signature);
        pvsIntent.putExtra("PURCHASE_DATA", purchaseData);
        pvsIntent.putExtra("DEVELOPER_PAYLOAD", developerPayload);
        pvsIntent.putExtra("ASTRO_USERID", astroSageUserId);

        if (!TextUtils.isEmpty(chat_Id)) {
            messageChatID = chat_Id;
        }

        String textMsg = "";
        if (astrologerServiceInfo != null && astrologerServiceInfo.getProblem() != null && astrologerServiceInfo.getProblem().trim().length() > 0) {
            textMsg = astrologerServiceInfo.getProblem();
        } else {
            textMsg = etUserquestion.getText().toString().trim();
        }
        pvsIntent.putExtra("MESSAGE_TEXT", textMsg);
        pvsIntent.putExtra("MESSAGE_CHAT_ID", messageChatID);
        pvsIntent.putExtra("ORDER_ID", order_Id);
        pvsIntent.putExtra("price", price);
        pvsIntent.putExtra("priceCurrencycode", priceCurrencycode);
        pvsIntent.putExtra("FullJsonDataObj", fullJsonDataObj);
        pvsIntent.putExtra("layoutPostion", "" + layoutPosition);
        pvsIntent.putExtra("messageTitle", "Order Insert");

        startService(pvsIntent);

        CUtils.saveStringData(getApplicationContext(), "MISC_PUR_SERVICE_LAYOUT_POSITION", "" + layoutPosition);
        CUtils.saveStringData(getApplicationContext(), "MISC_PUR_SERVICE_MSG_TEXT", textMsg);
        CUtils.saveStringData(getApplicationContext(), "MISC_PUR_SERVICE_MSG_CHAT_ID", "");
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

    private void SavePlaninPreference(int newPlanIndex) {
        SharedPreferences sharedPreferences = ActAskQuestion.this
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                CUtils.hideMyKeyboard(this);
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (drawerFragment != null && drawerFragment.isDrawerOpen) {
            drawerFragment.closeDrawer();
        } /*else if(homeNavigationDrawerFragmentAstrologer != null && homeNavigationDrawerFragmentAstrologer.isDrawerOpen){
            homeNavigationDrawerFragmentAstrologer.closeDrawer();
        }*/ else {
            if (isNotification) {
                CUtils.gotoHomeScreen(this);
            } else {
                this.finish();
            }
        }
    }

    /**
     * @author : Amit RAutela
     * This method is used to call HomeInputScreen to get the details of user
     */
    private int isLocalKundliAvailable() {
        int screenId = 1;
        try {
            Map<String, String> mapHoroID = new ControllerManager().searchLocalKundliListOperation(
                    ActAskQuestion.this.getApplicationContext(), "",
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


    private void callToGetDetailsFromHomeInputModule(int screenId) {

        int planId = 1;

        planId = CUtils.getUserPurchasedPlanFromPreference(ActAskQuestion.this);

        if (isDataComingFromFragAskAQuesAdvertisementView) {
            //setDataModel(beanHoroPersonalInfo);
            // convertInJsonObj(astrologerServiceInfo);
            isPaymentDone = false;
            CUtils.saveBooleanData(ActAskQuestion.this, CGlobalVariables.PAYMENTDONE_KEY_FOR_ASTRO_SERVICES_ISPAYMENTDONE, isPaymentDone);
            CUtils.saveStringData(ActAskQuestion.this, CGlobalVariables.PAYMENTDONE_KEY_FOR_ASTRO_SERVICES_JSONOBJ, fullJsonDataObj);
            //purchaseServiceByInApp();

            Typeface typeface = CUtils.getRobotoFont(
                    ActAskQuestion.this, LANGUAGE_CODE, CGlobalVariables.regular);
            RequestQueue queue = VolleySingleton.getInstance(ActAskQuestion.this).getRequestQueue();

            if (!CUtils.isConnectedWithInternet(ActAskQuestion.this)) {
                MyCustomToast mct = new MyCustomToast(ActAskQuestion.this, ActAskQuestion.this
                        .getLayoutInflater(), ActAskQuestion.this, typeface);
                mct.show(getResources().getString(R.string.no_internet));
            } else {
                CUtils.getOrderIDAndChatId(ActAskQuestion.this, typeface, queue, itemdetails, fullJsonDataObj, astrologerServiceInfo, messageChatID);
            }
        } else {

            Bundle bundle = new Bundle();
            bundle.putSerializable("keyItemDetails", itemdetails);
            bundle.putInt(CGlobalVariables.MODULE_TYPE_KEY, CGlobalVariables.MODULE_BASIC);
            bundle.putBoolean(CGlobalVariables.ASK_QUESTION_QUERY_DATA, true);
            bundle.putInt("PAGER_INDEX", screenId);
            bundle.putString("QUERY_EMAIL_ID", etUseremail.getText().toString().trim());
            bundle.putString("QUERY_PHONE_NUM", etUsermobno.getText().toString().trim());

            bundle.putBoolean(CGlobalVariables.IS_USER_HAS_PLAN, planId == CGlobalVariables.SILVER_PLAN_ID ||
                    planId == CGlobalVariables.SILVER_PLAN_ID_5 ||
                    planId == CGlobalVariables.SILVER_PLAN_ID_4 ||
                    planId == CGlobalVariables.GOLD_PLAN_ID ||
                    planId == CGlobalVariables.GOLD_PLAN_ID_7 ||
                    planId == CGlobalVariables.GOLD_PLAN_ID_6 ||
                    planId == CGlobalVariables.PLATINUM_PLAN_ID ||
                    planId == CGlobalVariables.PLATINUM_PLAN_ID_9 ||
                    planId == CGlobalVariables.PLATINUM_PLAN_ID_10 ||
                    planId == CGlobalVariables.KUNDLI_AI_PRIMIUM_PLAN_ID_11);


            //Give the user report language code (0 for Hindi and 1 for English)
            String reportLanguageCode = "hi";
            int language = reportLanguageOptions.getSelectedItemPosition();
            if (language != CGlobalVariables.HINDI) {
                reportLanguageCode = "en";
            }

            String problemStr = etUserquestion.getText().toString().trim();

            problemStr = problemStr + "^^Report lang: " + reportLanguageCode;

            problemStr = problemStr + " ## ";

            //To know from where Ask a Question calls
            if (askAQuestionDataPage == null || askAQuestionDataPage.equals("")) {
                problemStr = problemStr + " - (" + CGlobalVariables.ask_A_Question_Android + ")";
            } else {
                problemStr = problemStr + " - (" + askAQuestionDataPage + ")";
            }

            Log.e("Problem call to ", problemStr);
            String appVerName = LibCUtils.getApplicationVersionToShow(ActAskQuestion.this);

            problemStr = " " + problemStr + " " + "(lang: " + CUtils.getLanguageKey(LANGUAGE_CODE) + ", " + "appversion: " + appVerName + ")";

            bundle.putString("QUERY_QUESTION", problemStr);

            System.out.println("language code " + problemStr);

            //Check if question is same or not, if same then pass messageChatID
            boolean isAnswerUpdate = etUserquestion.getText().toString().trim().equals(userProblem);
            //Checking if chat id is available or not
            if (messageChatID != null && !messageChatID.equals("") && isAnswerUpdate) {
                bundle.putString(CGlobalVariables.key_messageChatID, messageChatID);
                bundle.putInt(CGlobalVariables.key_layoutPosition, layoutPosition);
            } else {
                bundle.putString(CGlobalVariables.key_messageChatID, "");
                bundle.putInt(CGlobalVariables.key_layoutPosition, 0);
            }

            Intent intent = new Intent(this, HomeInputScreen.class);
            intent.putExtras(bundle);

            startActivity(intent);
        }
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
        ActAskQuestion.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (response >= 0) {
                    //Toast.makeText(ActAskQuestion.this, errorResponse[response], Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void intiProductPlan(ProductDetails productDetails) {
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

    private void purchaseServiceByInApp() {
        dumpDataString = "ActAskQuestion purchaseServiceByInApp() fullJsonDataObj=" + fullJsonDataObj;
        gotBuyAskQuestionPlan();
    }

    public void gotBuyAskQuestionPlan() {
        try {
            dumpDataString = dumpDataString + " gotBuyAskQuestionPlan()";
            CUtils.googleAnalyticSendWitPlayServie(ActAskQuestion.this,
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
                //Log.e("BillingClient", "dumpDataString=" + dumpDataString);
                if (responseCode != BillingClient.BillingResponseCode.OK) {
                    Toast.makeText(ActAskQuestion.this, "Fail to purchase", Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            //Log.e(e.getMessage());
        }
        CUtils.postDumpDataToServer(ActAskQuestion.this, dumpDataString);
    }


    public void onPurchaseCompleted(ServicelistModal itemdetails, String order_id) {
        CUtils.trackEcommerceProduct(ActAskQuestion.this, itemdetails.getServiceId(), itemdetails.getTitle(), itemdetails.getPriceInRS(), order_id, "INR", CGlobalVariables.Ecommerce_Paytm_Purchase, "In-App Store", "0", CGlobalVariables.TOPIC_ASKAQUESTION);
    }

    private String getGirlDetailInString(BeanHoroPersonalInfo beanHoroPersonalInfo) {
        String girlDetail = "Girl's Birth Detail:";
        BeanDateTime beanDateTime = beanHoroPersonalInfo.getDateTime();
        BeanPlace beanPlace = beanHoroPersonalInfo.getPlace();
        girlDetail = girlDetail + " {" + "Name:" + beanHoroPersonalInfo.getName() + ",Day:" +
                beanDateTime.getDay() + ",Month:" + (beanDateTime.getMonth() + 1) + ",Year:" + beanDateTime.getYear() +
                ",hour:" + beanDateTime.getHour()
                + ",minute:" + beanDateTime.getMin() + ",Second:" + beanDateTime.getSecond() +
                ",City:" + beanPlace.getCityName() + ",LongDeg:" + beanPlace.getLongDeg() +
                ",LongMin:" + beanPlace.getLongMin() + ",LongEW:" + beanPlace.getLongDir() +
                ",LatDeg:" + beanPlace.getLatDeg() + ",LatMin:" + beanPlace.getLatMin() +
                ",LatNS:" + beanPlace.getLatDir() + ",TimeZone:" + beanPlace.getTimeZoneName() +
                ",CountryName:" + beanPlace.getCountryName() + ",StateName:" + beanPlace.getState() + "}";
        return "(" + girlDetail + ")";
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults != null && grantResults.length > 0) {
            if (requestCode == PERMISSION_CONTACTS && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setEmailIdFromUserAccount();
            } else {
                boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0]);
                if (!showRationale) {
                    CUtils.saveBooleanData(this, CGlobalVariables.PERMISSION_KEY_CONTACTS, true);
                }
            }
        }
    }

    private void getChatHistory() {
        // pd = new CustomProgressDialog(ActAskQuestion.this, typeface);
        //Request a string response from the provided URL.
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
                                    chatMessageArrayList = new Gson().fromJson(chatHistory.trim(), new TypeToken<ArrayList<MessageDecode>>() {
                                    }.getType());
                                    if (chatMessageArrayList.size() > 0) {
                                        Log.e("Count is: ", "" + chatMessageArrayList.size());
                                        int count = chatMessageArrayList.size();
                                        countTextViewNotificationCenter.setVisibility(View.VISIBLE);
                                        countTextViewNotificationCenter.setText("" + count);
                                        CUtils.saveIntData(ActAskQuestion.this, CGlobalVariables.COUNTNOTIFICATIONCENTER, count);
                                        for (MessageDecode msgObj : chatMessageArrayList) {
                                            if ((msgObj.getOrderId() != null && !msgObj.getOrderId().isEmpty() && msgObj.getOrderId().equalsIgnoreCase("0"))) {
                                                msgObj.setNotPaidLayoutShow("True");
                                            } else {
                                                msgObj.setNotPaidLayoutShow("False");
                                            }
                                        }
                                        InternalStorage.writeObject(ActAskQuestion.this, chatMessageArrayList);
                                        relativeLayoutNotificationCenter.setVisibility(View.VISIBLE);
                                    }
                                }
                                getAvailableQuestion();
                            } catch (Exception e) {

                                //e.printStackTrace();
                            }

                        }
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("tag", "Error Through" + error.getMessage());
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
                String emailId = UserEmailFetcher.getEmail(ActAskQuestion.this);
                //String emailId="chandantestliveapi@gmail.com";
                String androidId = CUtils.getMyAndroidId(ActAskQuestion.this);

                //String emailId = "testchatnewid@gmail.com";
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("key", CUtils.getApplicationSignatureHashCode(ActAskQuestion.this));
                headers.put(CGlobalVariables.KEY_EMAIL_ID, emailId == null ? "" : CUtils.replaceEmailChar(emailId));
                headers.put("androidid", androidId);
                headers.put("language", "" + LANGUAGE_CODE);
                String astrosageUserID = CUtils.getUserName(ActAskQuestion.this);
                if (astrosageUserID == null) {
                    astrosageUserID = "";
                }
                headers.put(CGlobalVariables.KEY_AS_USER_ID, CUtils.replaceEmailChar(astrosageUserID));
                //Log.e("", headers.toString());
                return headers;
            }

        };


        // Add the request to the RequestQueue.
        Log.e("tag", "API HIT HERE");
        int socketTimeout = 60000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        stringRequest.setShouldCache(true);
        queue.add(stringRequest);

    }

    private void setCountNotificationCenter() {
        if (countNotification > 0) {
            countTextViewNotificationCenter.setVisibility(View.VISIBLE);
            countTextViewNotificationCenter.setText("" + countNotification);
        } else {
            countTextViewNotificationCenter.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        chatMessageArrayList = CUtils.getDataFromPrefrence(ActAskQuestion.this);
        countNotification = CUtils.getIntData(ActAskQuestion.this, CGlobalVariables.COUNTNOTIFICATIONCENTER, 0);
        setCountNotificationCenter();
        getAvailableQuestion();
        freeQuestionText.setVisibility(View.GONE);

        if (chatMessageArrayList != null && chatMessageArrayList.size() > 0) {
            relativeLayoutNotificationCenter.setVisibility(View.VISIBLE);
        } else {
            relativeLayoutNotificationCenter.setVisibility(View.GONE);
        }
    }

    public void saveDataOnInternalStorage() {
        Intent intent = new Intent(ActAskQuestion.this, SaveDataInternalStorage.class);
        intent.putParcelableArrayListExtra(CGlobalVariables.CHATWITHASTROLOGER, chatMessageArrayList);
        intent.putExtra(CGlobalVariables.ISINSERT, false);
        (ActAskQuestion.this).startService(intent);
    }

    private MessageDecode setMessageDataAskByUser() {
        MessageDecode messageDecode = new MessageDecode();
        messageDecode.setUserType("user");
        messageDecode.setNotificationShow("False");
        String currentDateTime = CUtils.getCurrentDateTime();
        messageDecode.setDateTimeShow(currentDateTime);
        messageDecode.setMessageText(etUserquestion.getText().toString().trim());
        messageDecode.setColorOfMessage("#ffffff");
        messageDecode.setRateShow("False");
        messageDecode.setShareLinkShow("False");
        messageDecode.setNoOfQuestion(CUtils.getIntData(ActAskQuestion.this, CGlobalVariables.NOOFQUESTIONAVAILABLE, 0));
        int noOfQuestion = CUtils.getIntData(ActAskQuestion.this, CGlobalVariables.NOOFQUESTIONAVAILABLE, 0);
        if (noOfQuestion > 0) {
            messageDecode.setNotPaidLayoutShow("False");
        } else {
            messageDecode.setNotPaidLayoutShow("True");
            messageDecode.setOrderId("0");
        }
        if (astrologerServiceInfo != null) {
            messageDecode.setAstrologerServiceInfo(astrologerServiceInfo);
        }
        addToList(messageDecode);
        return messageDecode;
    }

    // Add new message on the list and update adapter each time.
    private void addToList(MessageDecode messageDecode) {
        if (chatMessageArrayList == null)
            chatMessageArrayList = new ArrayList<MessageDecode>();
        chatMessageArrayList.add(messageDecode);

    }

    /**
     * This method is used to open the notification center Activity
     */
    private void openActNotificationLandingActivity() {
        Intent intentAskQuestion = new Intent(ActAskQuestion.this, ActNotificationLanding.class);
        intentAskQuestion.putExtra("keyItemDetails", itemdetails);
        startActivity(intentAskQuestion);
    }

    List<String> getDrawerListItem() {

        try {
            String[] menuItems1 = getResources().getStringArray(R.array.ask_a_question_list);
            return CUtils.getDrawerListItem(ActAskQuestion.this, menuItems1, null, ask_a_question_list_index);
        } catch (Exception ex) {
            //Log.i("TAG", ex.getMessage());
            return null;
        }

    }

    List<Drawable> getDrawerListItemIcon() {

        try {
            TypedArray itemsIcon1 = getResources().obtainTypedArray(R.array.ask_a_question_list_icon);

            return CUtils.getDrawerListItemIcon(ActAskQuestion.this, itemsIcon1, null, ask_a_question_list_index);
        } catch (Exception ex) {
            //Log.i("TAG", ex.getMessage());
            return null;
        }

    }

    List<Integer> getDrawerListItemIndex() {
        try {
            return CUtils.getDrawerListItemIndex(ActAskQuestion.this, ask_a_question_list_index, null);
        } catch (Exception ex) {
            //Log.i("TAG", ex.getMessage());
            return null;
        }
    }

    /**
     * Check and load cache data
     */
    private void checkCachedData(String url) {
        // logDebug("AskQuestion URL=" + url);
        Cache cache = VolleySingleton.getInstance(ActAskQuestion.this).getRequestQueue().getCache();
        Cache.Entry entry = cache.get(url);
        // cache data
        if (entry != null) {
            String saveData = new String(entry.data, StandardCharsets.UTF_8);
            if (!TextUtils.isEmpty(saveData)) {
                // logDebug("AskQuestion cache hit bytes=" + entry.data.length);
                // logDebug("AskQuestion cache response preview=" + previewForLog(saveData));
                parseGsonData(saveData);
            }

        }
        if (!CUtils.isConnectedWithInternet(ActAskQuestion.this)) {
            MyCustomToast mct = new MyCustomToast(ActAskQuestion.this, ActAskQuestion.this
                    .getLayoutInflater(), ActAskQuestion.this, typeface);
            mct.show(getResources().getString(R.string.no_internet));
        } else {
            getAskQuestionDesc(url);
        }
    }

    /**
     * get Ask a question desc data
     */
    public void getAskQuestionDesc(String url) {
        showProgressBar();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideProgressBar();
                        if (response != null && !response.isEmpty()) {
                            try {
                                // logDebug("AskQuestion network response chars=" + response.length());
                                // logDebug("AskQuestion network response preview=" + previewForLog(response));
                                response = new String(response.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                                // logDebug("AskQuestion network response utf8 preview=" + previewForLog(response));
                                parseGsonData(response);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressBar();
                // logDebug("AskQuestion network error=" + error);
                if (error instanceof TimeoutError) {
                    VolleyLog.d("TimeoutError: " + error.getMessage());
                } else if (error instanceof NoConnectionError) {
                    VolleyLog.d("NoConnectionError: " + error.getMessage());
                } else if (error instanceof AuthFailureError) {
                    VolleyLog.d("AuthFailureError: " + error.getMessage());
                } else if (error instanceof ServerError) {
                    VolleyLog.d("ServerError: " + error.getMessage());
                } else if (error instanceof NetworkError) {
                    VolleyLog.d("NetworkError: " + error.getMessage());
                } else if (error instanceof ParseError) {
                    VolleyLog.d("ParseError: " + error.getMessage());
                }
            }


        }


        ) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }


            @Override
            public Map<String, String> getParams() {

                HashMap<String, String> headers = new HashMap<String, String>();
                if (TextUtils.isEmpty(askAQuestionDataPage)) {
                    askAQuestionDataPage = CGlobalVariables.ask_A_Question_Android;
                }
                headers.put("servicetype", CUtils.getAskAQuestionServiceTypeCodeFromPage(askAQuestionDataPage));
                headers.put("languageCode", String.valueOf(LANGUAGE_CODE));
                headers.put("Key", CUtils.getApplicationSignatureHashCode(ActAskQuestion.this));
                headers.put(KEY_AS_USER_ID, CUtils.replaceEmailChar(CUtils.getUserName(ActAskQuestion.this)));
                headers.put("asuserplanid", String.valueOf(CUtils.getUserPurchasedPlanFromPreference(ActAskQuestion.this)));

                return headers;
            }

        };


        int socketTimeout = 60000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        stringRequest.setShouldCache(true);
        queue.add(stringRequest);
    }

    /**
     * show Progress Bar
     */
    private void showProgressBar() {
        if (pd == null) {
            pd = new CustomProgressDialog(ActAskQuestion.this, regularTypeface);
        }

        if (!pd.isShowing()) {
            pd.setCanceledOnTouchOutside(false);
            pd.show();
        }
    }

    /**
     * hide Progress Bar
     */
    private void hideProgressBar() {
        try {
            if (pd != null && pd.isShowing())
                pd.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Logs URL/response debug info only for debug builds.
     */
    private void logDebug(String message) {
        if (BuildConfig.DEBUG) {
            Log.d(TAG, message);
        }
    }

    /**
     * Returns a short preview of a large response for Logcat readability.
     */
    private static String previewForLog(@Nullable String value) {
        if (value == null) {
            return "null";
        }
        String normalized = value.replace("\n", "\\n").replace("\r", "\\r");
        int max = 800;
        if (normalized.length() <= max) {
            return normalized;
        }
        return normalized.substring(0, max) + "...(truncated, len=" + normalized.length() + ")";
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
                CUtils.googleAnalyticSendWitPlayServieForPurchased(ActAskQuestion.this,
                        CGlobalVariables.GOOGLE_ANALYTIC_ASTROSHOP_SERVICE,
                        CGlobalVariables.GOOGLE_ANALYTIC_ASK_A_QUESTION_FREE, null, dPrice, "");

                Intent intent = new Intent(ActAskQuestion.this, ActNotificationLanding.class);
                startActivity(intent);

            } else if (successResult.equalsIgnoreCase("8")) {
                //8 = If Free question is deactivated or expired
                CUtils.openFreeQuestionDeactivateFrag(ActAskQuestion.this);
                getAvailableQuestion();
                freeQuestionText.setVisibility(View.GONE);
            } else {
                Toast.makeText(ActAskQuestion.this, getResources().getString(R.string.server_error_msg), Toast.LENGTH_LONG).show();
            }


        } catch (Exception ex) {
            Toast.makeText(ActAskQuestion.this, getResources().getString(R.string.server_error_msg), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void doActionOnError(String response) {
        Toast.makeText(ActAskQuestion.this, getResources().getString(R.string.server_error_msg), Toast.LENGTH_LONG).show();
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
        CUtils.updatePaidStatus(ActAskQuestion.this, layoutPosition, order_Id, payStatus, chat_Id, astrologerServiceInfo);
        CUtils.postDataToServer(ActAskQuestion.this, mediumTypeface, queue, order_Id, chat_Id, payStatus, astrologerServiceInfo, CGlobalVariables.UPDATE_PAY_STATUS_CHAT);

    }

    private class MyTextWatcher implements TextWatcher {

        private final View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            if (charSequence.toString().length() == 1) {
                if (!charSequence.toString().matches("[a-zA-Z ]+") && !charSequence.toString().matches("[0-9]") && !charSequence.toString().matches("'+'")) {
                    //   mct.show(getResources().getString(R.string.astro_shop_valid_text));
                }
            }
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {

                case R.id.etUseremail:
                    validateName(etUseremail, input_layout_email, getString(R.string.email_one_v));

                    if (etUseremail.getText().toString().trim().isEmpty()) {
                        input_layout_email.setError(null);
                        input_layout_email.setErrorEnabled(false);
                        input_layout_email.setError(getResources().getString(R.string.email_one_v));
                        requestFocus(etUseremail);
                        etUseremail.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);

                    } else if (!(CUtils.isValidEmail(etUseremail.getText().toString().trim()))) {
                        input_layout_email.setError(null);
                        input_layout_email.setErrorEnabled(false);
                        input_layout_email.setError(getResources().getString(R.string.email_one_v_astro_service));
                        requestFocus(etUseremail);
                        etUseremail.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                    } else {
                        input_layout_email.setErrorEnabled(false);
                        etUseremail.getBackground().setColorFilter(null);
                        etUseremail.getBackground().setColorFilter(getResources().getColor(R.color.ColorPrimary), PorterDuff.Mode.SRC_ATOP);
                    }

                    String charSequence1 = etUseremail.getText().toString().trim();
                    if (charSequence1.length() == 1) {
                        if (!charSequence1.matches("[a-zA-Z ]+") && !charSequence1.matches("[0-9]")) {
                            mct.show(getResources().getString(R.string.astro_shop_valid_text));
                            etUseremail.setText("");
                        }
                    }

                    break;

                case R.id.etUserquestion:
                    validateName(etUserquestion, input_layout_question, getString(R.string.astrologer_user_query));


                    String charSequence2 = etUserquestion.getText().toString().trim();
                    if (charSequence2.length() == 1) {

                    }

                    break;
                case R.id.etUsermobno:
                    validateName(etUsermobno, input_layout_mobno, getString(R.string.astro_shop_User_mob_no));


                    break;


            }
        }
    }
}
