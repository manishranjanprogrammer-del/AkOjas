package com.ojassoft.astrosage.varta.ui.activity;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.COUNTRY_CODE_IND;
import static com.ojassoft.astrosage.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_WALLET_BOTTOM_CALL;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_WALLET_BOTTOM_CHAT;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_WALLET_BOTTOM_KUNDLI;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_WALLET_BOTTOM_LIVE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.INTERNATIONAL_MAX_VAR_RECHARGE_AMOUNT;
import static com.ojassoft.astrosage.utils.CGlobalVariables.INTERNATIONAL_MIN_VAR_RECHARGE_AMOUNT;
import static com.ojassoft.astrosage.utils.CGlobalVariables.MAX_VAR_RECHARGE_AMOUNT;
import static com.ojassoft.astrosage.utils.CGlobalVariables.MAX_VAR_RECHARGE_AMOUNT_VAL;
import static com.ojassoft.astrosage.utils.CGlobalVariables.MIN_VAR_RECHARGE_AMOUNT;
import static com.ojassoft.astrosage.utils.CGlobalVariables.MIN_VAR_RECHARGE_AMOUNT_VAL;
import static com.ojassoft.astrosage.utils.CGlobalVariables.SPECIAL_RECHARGE_25_BTN_CLICK;
import static com.ojassoft.astrosage.utils.CGlobalVariables.SPECIAL_RECHARGE_25_DISMISS;
import static com.ojassoft.astrosage.utils.CGlobalVariables.SPECIAL_RECHARGE_25_SHOWN;
import static com.ojassoft.astrosage.utils.CGlobalVariables.WALLET_BOTTOM_BAR_CALL_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.WALLET_BOTTOM_BAR_CHAT_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.WALLET_BOTTOM_BAR_KUNDLI_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.WALLET_BOTTOM_BAR_LIVE_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CUtils.createSession;
import static com.ojassoft.astrosage.utils.CUtils.openKundliActivity;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.SHOW_DIALOG_EVENT;
import static com.ojassoft.astrosage.varta.utils.CUtils.getReducedPriceRechargeDetails;
import static com.ojassoft.astrosage.varta.utils.CUtils.gotoAllLiveActivityFromLiveIcon;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.Editable;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.PurchaseAiConsultationPlan;
import com.ojassoft.astrosage.varta.adapters.WalletRecyclerViewAdapter;
import com.ojassoft.astrosage.varta.model.WalletAmountBean;
import com.ojassoft.astrosage.varta.utils.AppDataSingleton;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.CustomProgressDialog;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.varta.utils.NumberUtils;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class WalletActivity extends BaseActivity implements View.OnClickListener, VolleyResponse {

    LinearLayout navView;
    Button proceedBtn, proceedRechargeBtn;
    ImageView backIV;
    TextView titleTV;
    TextView headingTV;
    TextView balanceTV;
    TextView rsSignTV;
    EditText editTextAmunt;
    RequestQueue queue;
    ImageView ivArrowCustomAmount;
    LinearLayout custom_amount_layout;
    TextView text_unlimited_ai_chats,text_unlimited_ai_chats_price,enterOrChooseTV;
    TextView descTV;
    LinearLayout enter_amount_layout,llCustomRecharge;
    RecyclerView recyclerView;
    CustomProgressDialog pd;
    int[] rechargePriceArr = {1, 100, 200, 500, 1000, 2000, 5000, 10000, 20000};
    int selectedPosition = 4;
    RelativeLayout containerLayout;
    WalletAmountBean walletAmountBean;
    String callingActivity;
    String astrologerPhoneNumber;
    String astrlogerUrlText;
    String url, fromOneRsDialog;
    String rechargeServiceId;
    private final int FETCH_RECHARGE_DATA = 1;
    private final int FETCH_CONST_HIST = 2;
    FloatingActionButton fabWallet;
    int variableRechargeMin;
    int variableRechargeMax;
    ConstraintLayout unlimitedAiChatsLayout;
    StringRequest callHistoryRequest;
    ImageView ivRechargeHistory;
    TextView paidPriceTV, freePriceTV;
    LinearLayout paidFreePriceLayout , walletBalLayout;
    boolean isPaidFreeBalShowing = false;
    /**
     * Bottom Navigation View
     */

    /*BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            CUtils.openAstroSageHomeActivity(WalletActivity.this);
                            return true;
                        case R.id.navigation_recharge:
                            CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_WALLET_BOTTOM_CALL, FIREBASE_EVENT_ITEM_CLICK, "");
                            createSession(WalletActivity.this, WALLET_BOTTOM_BAR_CALL_PARTNER_ID);
                            switchToConsultTab(FILTER_TYPE_CALL, WalletActivity.this);
                            return true;
                        case R.id.navigation_share:
                            fabActions();
                            return true;
                        case R.id.navigation_notification:
                            CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_WALLET_BOTTOM_CHAT, FIREBASE_EVENT_ITEM_CLICK, "");
                            createSession(WalletActivity.this, WALLET_BOTTOM_BAR_CHAT_PARTNER_ID);
                            switchToConsultTab(FILTER_TYPE_CHAT, WalletActivity.this);
                            return true;
                        case R.id.navigation_profile:
                            Intent intent1 = new Intent(WalletActivity.this, MyAccountActivity.class);
                            startActivity(intent1);
                            return true;
                    }
                    return false;
                }
            };*/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        walletAmountBean = new WalletAmountBean();
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        CGlobalVariables.walletRequestQueue = queue;
        setContentView(R.layout.wallet_layout);
        initIds();

//        Typeface typeface = CUtils.getRobotoFont(WalletActivity.this, 0, CGlobalVariables.regular);
//        balanceTV.setTypeface(typeface);
        setTypeface();
        backIV.setOnClickListener(this);
        fabWallet.setOnClickListener(this);
        ivRechargeHistory.setOnClickListener(this);
        proceedBtn.setOnClickListener(this);
        editTextAmunt.setOnClickListener(this);
        proceedRechargeBtn.setOnClickListener(this);
        walletBalLayout.setOnClickListener(this);
        /*String mobNo = "9015469060";*/
        //getRechargeAmounts(CUtils.getUserID(this));
        Calendar calendar = Calendar.getInstance();
        //  url = CGlobalVariables.RECHARGESERVICES+"date="+calendar.get(Calendar.DATE)+"-"+calendar.get(Calendar.MONTH)+"-"+calendar.get(Calendar.YEAR);
        getDataFromIntent();
        checkCachedData();

        //navView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        customBottomNavigationFont(navView);
        setBottomNavigationText();
        //navView.setSelectedItemId(R.id.navigation_recharge);

        boolean isShowvariableRecharge = CUtils.getBooleanData(this, com.ojassoft.astrosage.utils.CGlobalVariables.SHOW_VAR_RECHARGE, false);
        if (isShowvariableRecharge) {
            descTV.setVisibility(GONE);
            llCustomRecharge.setVisibility(VISIBLE);
            if (CUtils.getCountryCode(WalletActivity.this).equals(COUNTRY_CODE_IND)) {
                variableRechargeMin = CUtils.getIntData(this, MIN_VAR_RECHARGE_AMOUNT, MIN_VAR_RECHARGE_AMOUNT_VAL);
                variableRechargeMax = CUtils.getIntData(this, MAX_VAR_RECHARGE_AMOUNT, MAX_VAR_RECHARGE_AMOUNT_VAL);
            } else {//international user
                variableRechargeMin = CUtils.getIntData(this, INTERNATIONAL_MIN_VAR_RECHARGE_AMOUNT, MIN_VAR_RECHARGE_AMOUNT_VAL);
                variableRechargeMax = CUtils.getIntData(this, INTERNATIONAL_MAX_VAR_RECHARGE_AMOUNT, MAX_VAR_RECHARGE_AMOUNT_VAL);
            }
            editTextAmunt.setHint(getString(R.string.note_for_min_amt, variableRechargeMin + ""));
        } else {
            descTV.setVisibility(VISIBLE);
            llCustomRecharge.setVisibility(GONE);
        }


        // Add TextWatcher to allow only English digits (0-9)
        editTextAmunt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // Not needed
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // Not needed
            }

            @Override
            public void afterTextChanged(Editable s) {
                String text = s.toString();
                if (!text.isEmpty()) {
                    // Check if the text contains any non-digit characters
                    if (!text.matches("[0-9]+")) {
                        // Show toast message for non-English digits
                        Toast.makeText(WalletActivity.this, R.string.please_enter_only_english_digits, Toast.LENGTH_SHORT).show();
                        // Remove non-digit characters
                        String filteredText = text.replaceAll("[^0-9]", "");
                        // Update the EditText without triggering the TextWatcher
                        editTextAmunt.removeTextChangedListener(this);
                        editTextAmunt.setText(filteredText);
                        editTextAmunt.setSelection(filteredText.length());
                        editTextAmunt.addTextChangedListener(this);
                    }
                }
            }
        });
        enter_amount_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (custom_amount_layout.getVisibility() == View.VISIBLE) {
                    custom_amount_layout.setVisibility(View.GONE);
                    enterOrChooseTV.setText(getString(R.string.choose_your_own_amount));
                    ivArrowCustomAmount.setImageDrawable(ContextCompat.getDrawable(WalletActivity.this, R.drawable.ic_arrow_down_24px));
                } else {
                    custom_amount_layout.setVisibility(View.VISIBLE);
                    enterOrChooseTV.setText(getString(R.string.enter_your_amount));
                    ivArrowCustomAmount.setImageDrawable(ContextCompat.getDrawable(WalletActivity.this, R.drawable.ic_arrow_up_24px));

                }
            }
        });
        unlimitedAiChatsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Track subscribe button click with Firebase analytics
                com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents(com.ojassoft.astrosage.varta.utils.CGlobalVariables.FBA_WALLET_SCREEN_UNLIMITED_AI_PASS_SUBSCRIBE_BTN, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                com.ojassoft.astrosage.utils.CUtils.createSession(WalletActivity.this, CGlobalVariables.WALLET_SCREEN_UNLIMITED_AI_PASS);

                Intent intent = new Intent(WalletActivity.this, PurchaseAiConsultationPlan.class);
                intent.putExtra(com.ojassoft.astrosage.varta.utils.CGlobalVariables.SOURCE_PARAMS, "wallet_screen");
                startActivity(intent);
            }
        });

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                checkSpacialRecharge(backIV);
            }
        });
        // Check to show 25 rupees spacial offer
        if(CUtils.getBooleanData(this, com.ojassoft.astrosage.utils.CGlobalVariables.IS_SHOW_25_RUPEE_RECHARGE_OFFER, true)) {
            new Handler().postDelayed(() -> getReducedPriceRechargeDetails(WalletActivity.this, null), 500);
        }

    }

    private void initIds() {
        unlimitedAiChatsLayout = findViewById(R.id.unlimitedAiChatsLayout);
        backIV = findViewById(R.id.ivBack);
        proceedBtn = findViewById(R.id.proceed_btn);
        titleTV = findViewById(R.id.tvTitle);
        ivRechargeHistory = findViewById(R.id.ivRechargeHistory);
        headingTV = findViewById(R.id.balance_heading_tv);
        proceedRechargeBtn = findViewById(R.id.proceed_recharge_btn);
        editTextAmunt = findViewById(R.id.editTextAmunt);
        balanceTV = findViewById(R.id.balance_val_tv);
        enter_amount_layout = findViewById(R.id.enter_amount_layout);
        llCustomRecharge = findViewById(R.id.llCustomRecharge);
        containerLayout = findViewById(R.id.container);
        text_unlimited_ai_chats = findViewById(R.id.text_unlimited_ai_chats);
        text_unlimited_ai_chats_price = findViewById(R.id.text_unlimited_ai_chats_price);
        enterOrChooseTV = findViewById(R.id.enterOrChooseTV);
        custom_amount_layout = findViewById(R.id.custom_amount_layout);
        ivArrowCustomAmount = findViewById(R.id.ivArrowCustomAmount);
        descTV = findViewById(R.id.desc_tv);
        rsSignTV = findViewById(R.id.rs_sign_tv);
        fabWallet = findViewById(R.id.fabWallet);
        String title = getResources().getString(R.string.select_recharge_pack);
//        if (title.length() > 0) {
//            title = title.substring(0, 1).toUpperCase() + title.substring(1);
//        }
        titleTV.setText(title);
        ivRechargeHistory.setVisibility(VISIBLE);
        recyclerView = findViewById(R.id.recyclerview);
        navView = findViewById(R.id.nav_view);
        paidPriceTV = findViewById(R.id.paid_price_tv);
        freePriceTV = findViewById(R.id.free_price_tv);
        paidFreePriceLayout = findViewById(R.id.paid_free_price_layout);
        walletBalLayout = findViewById(R.id.llAmountView);
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            showWalletBalance();
            getWalletBalanceDataFromServer(false);
            //Log.d("TestLogsData","TestLogsData==>>"+ AstrosageKundliApplication.paymentScreenLogs);
            //AstrosageKundliApplication.paymentScreenLogs = "";
        } catch (Exception e) {
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        checkCachedData();
    }

    private void setPaidAndFreePrice() {
        String paidAmt = CUtils.getWalletPaidAmt(WalletActivity.this);
        String freeAmt = CUtils.getWalletFreeAmt(WalletActivity.this);
        if (!TextUtils.isEmpty(paidAmt) && !TextUtils.isEmpty(freeAmt)) {
            paidPriceTV.setText(" ₹" + paidAmt);
            freePriceTV.setText(" ₹" + freeAmt);
        }
    }
    private void showWalletBalance() {
        balanceTV.setText(CUtils.convertAmtIntoIndianDecimalFormat(CUtils.getWalletRs(WalletActivity.this)));
    }

    private void getDataFromIntent() {

        if (getIntent() != null && getIntent().getExtras() != null) {
            callingActivity = getIntent().getStringExtra(CGlobalVariables.CALLING_ACTIVITY);
            astrologerPhoneNumber = getIntent().getStringExtra(CGlobalVariables.ASTROLOGER_MOB_NUMBER);
            astrlogerUrlText = getIntent().getStringExtra(CGlobalVariables.ASTROLGER_URL_TEXT);
            if (getIntent().getExtras().containsKey(CGlobalVariables.FROM_ONE_RS_DIALOG)) {
                fromOneRsDialog = getIntent().getExtras().getString(CGlobalVariables.FROM_ONE_RS_DIALOG);
            }
            if (getIntent().getExtras().containsKey(CGlobalVariables.RECHARGE_SERVICE_ID)) {
                rechargeServiceId = getIntent().getExtras().getString(CGlobalVariables.RECHARGE_SERVICE_ID);
            }
        }
    }

    public void customBottomNavigationFont(final View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    customBottomNavigationFont(child);
                }
            } else if (v instanceof TextView) {
                FontUtils.changeFont(WalletActivity.this, ((TextView) v), CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
                ((TextView) v).setTextSize(11);
                ((TextView) v).setTextColor(getResources().getColor(R.color.black));
                // added by vishal
                ((TextView) v).setEllipsize(TextUtils.TruncateAt.END);
                v.setPadding(0, 0, 0, 0);
            }
        } catch (Exception e) {
        }
    }

    private void setBottomNavigationText() {
        // find MenuItem you want to change
        TextView navHomeTxt = navView.findViewById(R.id.txtViewHome);
        ImageView navCallImg = navView.findViewById(R.id.imgViewCall);
        TextView navCallTxt = navView.findViewById(R.id.txtViewCall);
        TextView navChatTxt = navView.findViewById(R.id.txtViewChat);
        ImageView navHisImg = navView.findViewById(R.id.imgViewHistory);
        TextView navHisTxt = navView.findViewById(R.id.txtViewHistory);
        TextView navLive = navView.findViewById(R.id.txtViewLive);

        boolean isAIChatDisplayed = com.ojassoft.astrosage.utils.CUtils.isAIChatDisplayed(this);
        if (isAIChatDisplayed) {
            navChatTxt.setText(getResources().getString(R.string.text_ask));
            navCallTxt.setText(getResources().getString(R.string.ai_astrologer));
            Glide.with(navCallImg).load(R.drawable.ic_ai_astrologer_unselected).into(navCallImg);
        } else {
            navChatTxt.setText(getResources().getString(R.string.chat_now));
            navCallTxt.setText(getResources().getString(R.string.call));
            navCallImg.setImageResource(R.drawable.nav_call_icons);
        }


        // set new title to the MenuItem
        navHomeTxt.setText(getResources().getString(R.string.title_home));
        com.ojassoft.astrosage.utils.CUtils.handleFabOnActivities(this, fabWallet, navLive);

        navHisTxt.setText(getResources().getString(R.string.history));
        navHisImg.setImageResource(R.drawable.nav_more_icons);

        View.OnClickListener bottomNavClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkSpacialRecharge(v);
            }

        };

        //setting Click listener
        navView.findViewById(R.id.bottom_nav_home).setOnClickListener(bottomNavClickListener);
        navView.findViewById(R.id.bottom_nav_call).setOnClickListener(bottomNavClickListener);
        navView.findViewById(R.id.bottom_nav_live).setOnClickListener(bottomNavClickListener);
        navView.findViewById(R.id.bottom_nav_chat).setOnClickListener(bottomNavClickListener);
        navView.findViewById(R.id.bottom_nav_history).setOnClickListener(bottomNavClickListener);
    }

    private void setRVAdapter(WalletAmountBean walletAmountBean) {
        WalletRecyclerViewAdapter walletRecyclerViewAdapter = new WalletRecyclerViewAdapter(this, walletAmountBean);
        RecyclerView.LayoutManager manager = new GridLayoutManager(this, 3);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(walletRecyclerViewAdapter);

        if (fromOneRsDialog != null && fromOneRsDialog.equalsIgnoreCase("1")) {
            int pos = -1;
            ArrayList<WalletAmountBean.Services> list = walletAmountBean.getServiceList();
            if (list != null && list.size() > 0) {
                if (!TextUtils.isEmpty(rechargeServiceId)) {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getServiceid().equalsIgnoreCase(rechargeServiceId)) {
                            pos = i;
                            break;
                        }
                    }
                } else {
                    for (int i = 0; i < list.size(); i++) {
                        if (list.get(i).getActualraters().equalsIgnoreCase("1.0")) {
                            pos = i;
                            break;
                        }
                    }
                }
                if (pos != -1) {
                    selectedPosition = pos;
                    redirectToPaymentActivity();
                }

            }
            fromOneRsDialog = "0";
        }
    }

    private void setTypeface() {
        FontUtils.changeFont(this, headingTV, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
//        FontUtils.changeFont(this, balanceTV, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
//        FontUtils.changeFont(this, balanceTV, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(this, text_unlimited_ai_chats, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(this, enterOrChooseTV, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(this, text_unlimited_ai_chats_price, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(this, descTV, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(this, rsSignTV, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(this, proceedBtn, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(this, titleTV, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.proceed_btn:
                if (CUtils.isConnectedWithInternet(WalletActivity.this)) {
                    if (walletAmountBean.getServiceList() == null || walletAmountBean.getServiceList().isEmpty()) {
                        CUtils.showSnackbar(containerLayout, getResources().getString(R.string.server_error), WalletActivity.this);
                        return;
                    }
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(CGlobalVariables.WALLET_INFO, walletAmountBean);
                    bundle.putInt(CGlobalVariables.SELECTED_POSITION, selectedPosition);
                    bundle.putString(CGlobalVariables.CALLING_ACTIVITY, callingActivity);
                    bundle.putString(CGlobalVariables.ASTROLGER_URL_TEXT, astrlogerUrlText);
                    bundle.putString(CGlobalVariables.ASTROLOGER_MOB_NUMBER, astrologerPhoneNumber);
                    bundle.putString("screen_open_from", "WalletActivity");
                    Intent intent = new Intent(WalletActivity.this, PaymentInformationActivity.class);
                    intent.putExtras(bundle);
                    startActivityForResult(intent, 1);
                } else {
                    CUtils.showSnackbar(containerLayout, getResources().getString(R.string.no_internet), WalletActivity.this);
                }
                break;
            case R.id.proceed_recharge_btn:
                String rechargeAmount = editTextAmunt.getText().toString();
                if (isValidRechargeAmount(rechargeAmount)) {
                    processVariableRecharge(rechargeAmount);
                }
                break;
            case R.id.ivBack:
                getOnBackPressedDispatcher().onBackPressed();
                break;
            case R.id.fabWallet:
                fabActions();
                break;
            case R.id.ivRechargeHistory:
                try {
                    if (callHistoryRequest != null) {
                        callHistoryRequest.cancel();
                    }
                } catch (Exception e) {
                }


                Intent cunsultationIntent = new Intent(WalletActivity.this, ConsultantHistoryActivity.class);
                startActivity(cunsultationIntent);
                break;

            case R.id.llAmountView:
                if (isPaidFreeBalShowing) {
                    paidFreePriceLayout.setVisibility(View.GONE);
                    isPaidFreeBalShowing = false;
                } else {
                    isPaidFreeBalShowing = true;
                    paidFreePriceLayout.setVisibility(VISIBLE);
                    setPaidAndFreePrice();//first show from local if available
                    getWalletBalanceDataFromServer(true); // then call API for latest data to refresh
                }
        }
    }

    public void setSelectedPosition(int selectedPosition) {
        this.selectedPosition = selectedPosition;
        proceedBtn.performClick();
    }

    private void getRechargeAmounts(String mobNo) {
        if (!CUtils.isConnectedWithInternet(WalletActivity.this)) {
            CUtils.showSnackbar(containerLayout, getResources().getString(R.string.no_internet), WalletActivity.this);
        } else {

            ApiList apiList = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> callApi = apiList.getRechargeServices(CUtils.getRechargeParams(this,mobNo));
            callApi.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    hideProgressBar();
                    if (response.body() != null) {
                        try {

                            String myResponse = response.body().string();
                            Calendar calendar = Calendar.getInstance();
                            CUtils.saveStringData(WalletActivity.this, CGlobalVariables.RECHARGE_SERVICES_DATE, String.valueOf(calendar.get(Calendar.DATE)));
                            CUtils.saveWalletRechargeData(myResponse);

                            parseResponse(myResponse);
                        } catch (Exception e) {
                            //
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    hideProgressBar();
                    CUtils.showSnackbar(containerLayout, getResources().getString(R.string.something_wrong_error), WalletActivity.this);
                }
            });
        }
    }

    /**
     * Check and load cache data
     */
    private void checkCachedData() {
        if (!TextUtils.isEmpty(CUtils.getWalletRechargeData())) {
            parseResponse(CUtils.getWalletRechargeData());
        } else {
            getRechargeAmounts(CUtils.getUserID(this));
        }
        try {
            if (!TextUtils.isEmpty(CUtils.getIsAiPassData())) {
                if (getShowAiPassPlan(CUtils.getIsAiPassData())) {
                    setViewForUnlimitedAIChatPlan();
                }
            } else {
                getAiPassPlan(CUtils.getUserID(this));
            }

        }catch (Exception e){
            //
        }

        /*Calendar calendar = Calendar.getInstance();
        String currentDate = String.valueOf(calendar.get(Calendar.DATE));
        String savedDate = CUtils.getStringData(WalletActivity.this, CGlobalVariables.RECHARGE_SERVICES_DATE, "");
        if (!currentDate.equals(savedDate)) {
            getRechargeAmounts(CUtils.getUserID(this));
        } else {
            if (!TextUtils.isEmpty(CUtils.getWalletRechargeData())) {
                parseResponse(CUtils.getWalletRechargeData());
            } else {
                getRechargeAmounts(CUtils.getUserID(this));
            }

        }*/
    }
    // to get isAiPass is active for this user or build version or all ready taken
    private void getAiPassPlan(String userID) {
        if (!CUtils.isConnectedWithInternet(WalletActivity.this)) {
            CUtils.showSnackbar(containerLayout, getResources().getString(R.string.no_internet), WalletActivity.this);
        } else {

            ApiList apiList = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> callApi = apiList.getAIPassPlan(CUtils.getRechargeParams(this,userID));
            callApi.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    hideProgressBar();
                    if (response.body() != null) {
                        try {
                            String myResponse = response.body().string();
                            CUtils.saveIsAiPassData(myResponse);
                            // We have to show unlimited AI chat plan behalf of below key showaipassplan
                            if (getShowAiPassPlan(myResponse)) {
                                // it will set all the view for unlimited AI chat plan
                                setViewForUnlimitedAIChatPlan();
                            }
                        } catch (Exception e) {
                            //
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    hideProgressBar();
                    CUtils.showSnackbar(containerLayout, getResources().getString(R.string.something_wrong_error), WalletActivity.this);
                }
            });
        }

    }

    public boolean getShowAiPassPlan(String json) {
        try {
            JSONObject obj = new JSONObject(json);
            return obj.getBoolean("showaipassplan");
        } catch (Exception e) {
            e.printStackTrace();
            return false;   // default value if parsing fails
        }
    }
    @Override
    public void onResponse(String response, int method) {
        //Log.d("TestLog", "response1="+response);
        //Log.e("LoginResponse1 RES ", response);
        hideProgressBar();
        if (method == FETCH_RECHARGE_DATA) {
//            CUtils.saveWalletRechargeData(response);
//            parseResponse(response);
        } else if (method == FETCH_CONST_HIST) {
//            try {
//                //Log.e("SAN WA RES ", " getCallAndRechargeHistory " + response);
//                JSONObject jsonObject = new JSONObject(response);
//                if ( jsonObject.has("consultations") && jsonObject.getJSONArray("consultations").length() > 0  ) {
//                        CUtils.saveConsultationHistoryData(response);
//
//                }
//
//                String walletBal = jsonObject.getString("walletbalance");
//                if (!TextUtils.isEmpty(walletBal)) {
//                    CUtils.setWalletRs(WalletActivity.this, walletBal);
//                    showWalletBalance();
//                }
//            }catch (Exception e){}
        }

    }

    @Override
    public void onError(VolleyError error) {
        hideProgressBar();
    }

    private void hideProgressBar() {
        try {
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }
        } catch (Exception e) {
            //
        }
    }

    private boolean parseResponse(String response) {
        //Log.d("TestLog", "response="+response);

        boolean boolaval = true;
        if (response != null && response.length() > 0) {
            try {
                WalletAmountBean.Services services;
                JSONObject jsonObject = new JSONObject(response);
                String gstRate = jsonObject.getString("gstrate");
                String dollarconversionrate = jsonObject.getString("dollarconversionrate");

                JSONArray jsonArray = jsonObject.getJSONArray("services");
                JSONObject innerJsonObject;
                ArrayList<WalletAmountBean.Services> servicesArrayList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    innerJsonObject = jsonArray.getJSONObject(i);
                    services = walletAmountBean.new Services();
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
                walletAmountBean.setGstrate(gstRate);
                walletAmountBean.setDollorConverstionRate(dollarconversionrate);
                walletAmountBean.setServiceList(servicesArrayList);
                setRVAdapter(walletAmountBean);

//                // We have to show unlimited AI chat plan behalf of below key showaipassplan
//                String showUnlimitedAiChatPlan = jsonObject.optString("showaipassplan");
//
//                if(showUnlimitedAiChatPlan.equals("true")){
//                    // it will set all the view for unlimited AI chat plan
//                    setViewForUnlimitedAIChatPlan();
//                }
            } catch (Exception e) {
                boolaval = false;
                e.printStackTrace();
            }
        } else {
            boolaval = false;
            CUtils.showSnackbar(containerLayout, getResources().getString(R.string.server_error), WalletActivity.this);
        }
        return boolaval;
    }
    /**
     * Show the unlimited AI Chat plan details on the top of the recyclerview in the wallet screen.
     * It will fetch the details from the server and set the views accordingly.
     */
    private void setViewForUnlimitedAIChatPlan() {
        CUtils.fcmAnalyticsEvents(com.ojassoft.astrosage.utils.CGlobalVariables.UNLIMITED_AI_CHAT_PLAN_WALLET_SCREEN, com.ojassoft.astrosage.utils.CGlobalVariables.SHOW_UNLIMITED_AI_CHAT_PLAN_VIEW, "");
        if (!TextUtils.isEmpty(com.ojassoft.astrosage.varta.utils.CUtils.getAiPassPlanServiceDetail())) {
            String ojb = com.ojassoft.astrosage.varta.utils.CUtils.getAiPassPlanServiceDetail();
            getServiceDetails(ojb);
        } else {
            getUserRechargeAmount();
        }
    }

    public void redirectToPaymentActivity() {
        if (CUtils.isConnectedWithInternet(WalletActivity.this)) {
            if (walletAmountBean.getServiceList() == null || walletAmountBean.getServiceList().isEmpty()) {
                CUtils.showSnackbar(containerLayout, getResources().getString(R.string.server_error), WalletActivity.this);
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putSerializable(CGlobalVariables.WALLET_INFO, walletAmountBean);
            bundle.putInt(CGlobalVariables.SELECTED_POSITION, selectedPosition);
            bundle.putString(CGlobalVariables.CALLING_ACTIVITY, callingActivity);
            bundle.putString(CGlobalVariables.ASTROLGER_URL_TEXT, astrlogerUrlText);
            bundle.putString(CGlobalVariables.ASTROLOGER_MOB_NUMBER, astrologerPhoneNumber);
            bundle.putString("screen_open_from", "WalletActivity");
            Intent intent = new Intent(WalletActivity.this, PaymentInformationActivity.class);
            intent.putExtras(bundle);
            startActivityForResult(intent, 1);
        } else {
            CUtils.showSnackbar(containerLayout, getResources().getString(R.string.no_internet), WalletActivity.this);
        }
    }




    private void getWalletBalanceDataFromServer(boolean isWalletBalClicked) {

        if (CUtils.isConnectedWithInternet(WalletActivity.this)) {
            AppDataSingleton.getInstance().setConsultHistoryData("");

            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.getWalletBalance(getParamsNew());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    try {
                        if (response.body() != null) {
                            try {
//                                Log.e("balcheck", " getCallAndRechargeHistory " + response);
                                String myResponse = response.body().string();
                                JSONObject jsonObject = new JSONObject(myResponse);
                                String walletBal = jsonObject.getString("userbalancce");
                                if (!TextUtils.isEmpty(walletBal)) {
                                    CUtils.setWalletRs(WalletActivity.this, walletBal);
                                    showWalletBalance();
                                }
                                if (jsonObject.has("paidbalance") && jsonObject.has("freebalance")) {
                                    String paidBal = jsonObject.getString("paidbalance");
                                    String freeBal = jsonObject.getString("freebalance");
//                                    Log.e("balcheck", "onResponse: paid" +paidBal);
//                                    Log.e("balcheck", "onResponse: free" +freeBal);
                                    if(!TextUtils.isEmpty(paidBal)){
                                        com.ojassoft.astrosage.varta.utils.CUtils.setWalletPaidAmt(WalletActivity.this, paidBal);
                                    }
                                    if(!TextUtils.isEmpty(freeBal)){
                                        com.ojassoft.astrosage.varta.utils.CUtils.setWalletFreeAmt(WalletActivity.this, freeBal);
                                    }
                                    if(isWalletBalClicked)
                                      setPaidAndFreePrice();
                                }else{
                                    isPaidFreeBalShowing = false;
                                    paidFreePriceLayout.setVisibility(View.GONE);
                                }

                            } catch (Exception e) {
                                Log.e("balcheck", "exception: " +e.getMessage());
                            }
                        }
                    } catch (Exception e) {
                        //
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    try {
                        //
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }

    }



    public Map<String, String> getParamsNew() {
        HashMap<String, String> headers = new HashMap<String, String>();
        try {
            headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(WalletActivity.this));
            headers.put(CGlobalVariables.USER_PHONE_NO, CUtils.getUserID(WalletActivity.this));
            headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(WalletActivity.this));
            headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
            headers.put(CGlobalVariables.IGNORE_ASTRO, "true");
            headers.put(CGlobalVariables.DEVICE_ID, CUtils.getMyAndroidId(this));
            headers.put(CGlobalVariables.APP_VERSION, BuildConfig.VERSION_NAME);
            headers.put("balancetype","freeandpaid");
            headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(this));

            //Log.e("SAN ", " getCallAndRechargeHistory params " + headers.toString() );
        } catch (Exception e) {
        }
        return headers;
    }

    private void fabActions() {
        try {
            //boolean liveStreamingEnabledForAstrosage = CUtils.getBooleanData(this, CGlobalVariables.liveStreamingEnabledForAstrosage, false);
            boolean liveStreamingEnabledForAstrosage = true;
            if (!liveStreamingEnabledForAstrosage) { //fetch data according to tagmanag
                CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_WALLET_BOTTOM_KUNDLI, FIREBASE_EVENT_ITEM_CLICK, "");
                createSession(WalletActivity.this, WALLET_BOTTOM_BAR_KUNDLI_PARTNER_ID);
                openKundliActivity(this);
            } else {
                CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_WALLET_BOTTOM_LIVE, FIREBASE_EVENT_ITEM_CLICK, "");
                createSession(WalletActivity.this, WALLET_BOTTOM_BAR_LIVE_PARTNER_ID);
                gotoAllLiveActivityFromLiveIcon(this);
            }
        } catch (Exception e) {
            //
        }
    }

    @Override
    public void clickCall() {
        CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_WALLET_BOTTOM_CALL, FIREBASE_EVENT_ITEM_CLICK, "");
        createSession(WalletActivity.this, WALLET_BOTTOM_BAR_CALL_PARTNER_ID);
        super.clickCall();
    }

    @Override
    public void clickChat() {
        CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_WALLET_BOTTOM_CHAT, FIREBASE_EVENT_ITEM_CLICK, "");
        createSession(WalletActivity.this, WALLET_BOTTOM_BAR_CHAT_PARTNER_ID);
        super.clickChat();
    }

    @Override
    public void clickLive() {
        fabActions();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            if (resultCode == RESULT_OK) {
                setResult(RESULT_OK);
                finish();
            }
        }
    }

    private boolean isValidRechargeAmount(String rechargeAmount) {
        if (TextUtils.isEmpty(rechargeAmount)) {
            CUtils.showSnackbar(containerLayout, getString(R.string.error_enter_recharge_amount), WalletActivity.this);
            return false;
        } else {
            double rechargeAmt = NumberUtils.safeParseDouble(rechargeAmount);
            if (rechargeAmt < variableRechargeMin) {
                CUtils.showSnackbar(containerLayout, getString(R.string.error_enter_min_recharge_amount, variableRechargeMin + ""), WalletActivity.this);
                return false;
            }
            if (rechargeAmt > variableRechargeMax) {
                CUtils.showSnackbar(containerLayout, getString(R.string.error_enter_max_recharge_amount, variableRechargeMax + ""), WalletActivity.this);
                return false;
            }
        }
        return true;
    }

    private void processVariableRecharge(String rechargeAmount) {
        if (CUtils.isConnectedWithInternet(WalletActivity.this)) {
            ArrayList<WalletAmountBean.Services> rechargeList = walletAmountBean.getServiceList();
            int selectedPos = -1;
            // check weather recharge amount exist in recharge list
            /*if (rechargeList != null && !rechargeList.isEmpty()) {
                for (int i = 0; i < rechargeList.size(); i++) {
                    WalletAmountBean.Services services = rechargeList.get(i);
                    if (services == null) continue;
                    if (Double.parseDouble(services.getRaters()) == Double.parseDouble(rechargeAmount)) {
                        selectedPos = i;
                        break;
                    }
                }
            }*/

            Bundle bundle = new Bundle();
            bundle.putSerializable(CGlobalVariables.WALLET_INFO, walletAmountBean);
            bundle.putString(CGlobalVariables.CALLING_ACTIVITY, callingActivity);
            bundle.putString(CGlobalVariables.ASTROLGER_URL_TEXT, astrlogerUrlText);
            bundle.putString(CGlobalVariables.ASTROLOGER_MOB_NUMBER, astrologerPhoneNumber);

            if (selectedPos == -1) {//if recharge amount didn't match then pass recharge amount
                bundle.putString(CGlobalVariables.RECHARGE_AMOUNT, rechargeAmount);
            } else {
                bundle.putString(CGlobalVariables.RECHARGE_AMOUNT, "");
            }
            bundle.putInt(CGlobalVariables.SELECTED_POSITION, selectedPos);
            bundle.putString("screen_open_from", "WalletActivity");
            Intent intent = new Intent(WalletActivity.this, PaymentInformationActivity.class);
            intent.putExtras(bundle);
            startActivityForResult(intent, 1);
        } else {
            CUtils.showSnackbar(containerLayout, getResources().getString(R.string.no_internet), WalletActivity.this);
        }
    }
    /**
     * Parse and process AI service details from JSON response
     *
     * @param JObject JSON response containing service details
     */
    private void getServiceDetails(String JObject) {
        try {
            JSONObject obj = new JSONObject(JObject);
            //Log.d("testshjdvfghsd","amsdgjasgjdas"+obj);
            //serviceInDoller = obj.getString("rateindollar");
           // servicePriceInRs = obj.getString("rateinrs");
            String  firstvisitdatetimeValue = CUtils.getStringData(WalletActivity.this,CGlobalVariables.FIRSTVISITDATETIME,"");
            if(TextUtils.isEmpty(firstvisitdatetimeValue)){
                CUtils.saveStringData(WalletActivity.this,CGlobalVariables.FIRSTVISITDATETIME,obj.optString("firstvisittimemillis"));
                CUtils.saveBooleanData(WalletActivity.this,CGlobalVariables.FIRSTVISITSTATUS,true);
            }
            int actualPrice=0;
            if(obj.has("strikerateinrs")){
                 actualPrice = obj.optInt("strikerateinrs");
            }
            int servicePrice = obj.optInt("rateinrs");
            TextView tv_only_for_today = findViewById(R.id.tv_only_for_today);
            TextView striprateinrs = findViewById(R.id.striprateinrs);
            if(actualPrice==servicePrice){
                striprateinrs.setVisibility(GONE);
                tv_only_for_today.setVisibility(GONE);
            }else {
                striprateinrs.setVisibility(VISIBLE);
                tv_only_for_today.setVisibility(VISIBLE);
            }
            FontUtils.changeFont(WalletActivity.this,text_unlimited_ai_chats_price,CGlobalVariables.FONTS_OPEN_SANS_BOLD);
            FontUtils.changeFont(WalletActivity.this,striprateinrs,CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
            FontUtils.changeFont(WalletActivity.this,tv_only_for_today,CGlobalVariables.FONTS_OPEN_SANS_REGULAR);

            striprateinrs.setText("\u20B9"+actualPrice);
            striprateinrs.setPaintFlags(striprateinrs.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);


            text_unlimited_ai_chats_price.setText("\u20B9"+servicePrice);
            unlimitedAiChatsLayout.setVisibility(View.VISIBLE);
//            // Set currency based on user's country
//            if (!com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(this).equals(COUNTRY_CODE_IND)) {
//                currency = com.ojassoft.astrosage.varta.utils.CGlobalVariables.CURRENCY_USD;
//            }

        } catch (Exception e) {
            // Ignore exceptions
        }
    }
    /**
     * Fetch AI pass details from server
     */
    public void getUserRechargeAmount() {
        if (!com.ojassoft.astrosage.varta.utils.CUtils.isConnectedWithInternet(this)) {
            Toast.makeText(this, getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        } else {
            //showProgressBar();
            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.getAiPassDetails(getUserRechargeParams());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    //hideProgressBar();
                    try {
                        String myResponse = response.body().string();
                        JSONObject obj = new JSONObject(myResponse);
                        String status = obj.getString(com.ojassoft.astrosage.varta.utils.CGlobalVariables.STATUS);
                        if (status.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.STATUS_SUCESS)) {
                            // Cache response for future use
                            com.ojassoft.astrosage.varta.utils.CUtils.setAiPassPlanServiceDetail(myResponse);
                            getServiceDetails(myResponse);
                        } else {
                            com.ojassoft.astrosage.utils.CUtils.showSnackbar(containerLayout, getResources().getString(R.string.something_went_wrong) + "(" + status + ")", WalletActivity.this);
                        }
                    } catch (Exception e) {
                        // Ignore exceptions
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    //hideProgressBar();
                }
            });
        }
    }

    /**
     * Prepare parameters for API request
     *
     * @return Map containing request parameters
     */
    private Map<String, String> getUserRechargeParams() {
        boolean firstvisitstatusValue = CUtils.getBooleanData(WalletActivity.this,CGlobalVariables.FIRSTVISITSTATUS,false);
        String  firstvisitdatetimeValue = CUtils.getStringData(WalletActivity.this,CGlobalVariables.FIRSTVISITDATETIME,"");
        Map<String, String> map = new HashMap<>();
        map.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.PHONE_NO, com.ojassoft.astrosage.varta.utils.CUtils.getUserID(WalletActivity.this));
        map.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.APP_KEY, com.ojassoft.astrosage.varta.utils.CUtils.getApplicationSignatureHashCode(WalletActivity.this));
        map.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.COUNTRY_CODE, com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(WalletActivity.this));
        map.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_USER_ID, com.ojassoft.astrosage.varta.utils.CUtils.getUserIdForBlock(WalletActivity.this));
        map.put(CGlobalVariables.FIRSTVISITSTATUS, firstvisitstatusValue+"");
        map.put(CGlobalVariables.FIRSTVISITDATETIME, firstvisitdatetimeValue);
        return com.ojassoft.astrosage.varta.utils.CUtils.setRequiredParams(map);
    }

    boolean isSpacialRechargePopShown = false;
    void checkSpacialRecharge(View from){
        // Get the specific offer type assigned to this user
        String offerType = com.ojassoft.astrosage.varta.utils.CUtils.getUserIntroOfferType(this);
        boolean isShow25offer = CUtils.getBooleanData(this, com.ojassoft.astrosage.utils.CGlobalVariables.IS_SHOW_25_RUPEE_RECHARGE_OFFER, true);

        String cachedOfferResponse = CUtils.getStringData(this, com.ojassoft.astrosage.utils.CGlobalVariables.RECHARGE_SERVICE_OFFER_RESPONSE_KEY, "");
        if(isSpacialRechargePopShown || TextUtils.isEmpty(cachedOfferResponse)){
            sendToDestination(from);
            return;
        }

        if(offerType.contains(CGlobalVariables.REDUCED_PRICE_OFFER) && isShow25offer ) {
            parseData(cachedOfferResponse,from);

        }else{
            sendToDestination(from);
        }
    }

    void sendToDestination(View v){
        switch (v.getId()) {
            case R.id.bottom_nav_home:
                clickHome();
                break;
            case R.id.bottom_nav_call:
                clickCall();
                break;
            case R.id.bottom_nav_live:
                clickLive();
                break;
            case R.id.bottom_nav_chat:
                clickChat();
                break;
            case R.id.bottom_nav_history:
                clickHistory();
                break;
        }
        finish();
    }



    Dialog spacialRechargeDialog;
    void showSpacialRechargePopUp(WalletAmountBean walletAmountBean, int selectedPosition, int amount,View from){
        if(spacialRechargeDialog != null && spacialRechargeDialog.isShowing()){
            return;
        }
        spacialRechargeDialog = new Dialog(this);
        spacialRechargeDialog.setContentView(R.layout.spacial_recharege_dialog_layout);
        spacialRechargeDialog.setCancelable(false);
        Window window = spacialRechargeDialog.getWindow();
        if(window != null) {
            window.setBackgroundDrawable(ContextCompat.getDrawable(this,R.drawable.top_round_corner));
            WindowManager.LayoutParams lp = window.getAttributes();
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            lp.gravity = Gravity.BOTTOM;
            window.setAttributes(lp);
        }
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
            spacialRechargeDialog.dismiss();
            sendToDestination(from);
            CUtils.fcmAnalyticsEvents(SPECIAL_RECHARGE_25_DISMISS,  CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
        });
        btnRecharge.setOnClickListener((v)->{
            CUtils.fcmAnalyticsEvents(SPECIAL_RECHARGE_25_BTN_CLICK,  CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

            Bundle bundle = new Bundle();
            bundle.putSerializable(CGlobalVariables.WALLET_INFO, walletAmountBean);
            bundle.putInt(CGlobalVariables.SELECTED_POSITION, selectedPosition);
            bundle.putString(CGlobalVariables.CALLING_ACTIVITY, callingActivity);
            bundle.putString(CGlobalVariables.ASTROLGER_URL_TEXT, astrlogerUrlText);
            bundle.putString(CGlobalVariables.ASTROLOGER_MOB_NUMBER, astrologerPhoneNumber);
            bundle.putString("screen_open_from", "WalletActivity");
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

    public void parseData(String response,View v) {
        try {
            Log.e("fiveRupeeTest", "onResponse: " + response);
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
                    showSpacialRechargePopUp(walletBean, selectedPosition, (int) Double.parseDouble(amount),v);
                } else {
                    sendToDestination(v);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
