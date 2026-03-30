package com.ojassoft.astrosage.varta.ui.activity;

import static com.ojassoft.astrosage.utils.CGlobalVariables.CONSULT_HISTORY_BOTTOM_BAR_CALL_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.CONSULT_HISTORY_BOTTOM_BAR_CHAT_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.CONSULT_HISTORY_BOTTOM_BAR_KUNDLI_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.CONSULT_HISTORY_BOTTOM_BAR_LIVE_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_CONSULT_BOTTOM_CALL;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_CONSULT_BOTTOM_CHAT;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_CONSULT_BOTTOM_KUNDLI;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_CONSULT_BOTTOM_LIVE;
import static com.ojassoft.astrosage.utils.CUtils.createSession;
import static com.ojassoft.astrosage.utils.CUtils.openKundliActivity;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.CONSULT_HISTORY_LIST_ID;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.CONSULT_HISTORY_TYPE;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;
import static com.ojassoft.astrosage.varta.utils.CUtils.gotoAllLiveActivityFromLiveIcon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.KundliChatHistoryBean;
import com.ojassoft.astrosage.model.CDatabaseHelperOperations;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.varta.adapters.ViewPagerAdapter;
import com.ojassoft.astrosage.varta.model.CallHistoryBean;
import com.ojassoft.astrosage.varta.model.ChatHistoryBean;
import com.ojassoft.astrosage.varta.model.ConsultantHistoryBean;
import com.ojassoft.astrosage.varta.model.DeductionHistoryBean;
import com.ojassoft.astrosage.varta.model.RechargeHistoryBean;
import com.ojassoft.astrosage.varta.receiver.OngoingCallChatManager;
import com.ojassoft.astrosage.varta.service.Loginservice;
import com.ojassoft.astrosage.varta.service.OnGoingChatService;
import com.ojassoft.astrosage.varta.ui.fragments.CallHistoryFragment;
import com.ojassoft.astrosage.varta.ui.fragments.ChatHistoryFragment;
import com.ojassoft.astrosage.varta.ui.fragments.GiftHistoryFragment;
import com.ojassoft.astrosage.varta.ui.fragments.KundliAIChatHistoryFragment;
import com.ojassoft.astrosage.varta.ui.fragments.LiveHistoryFragment;
import com.ojassoft.astrosage.varta.ui.fragments.RechargeHistoryFragment;
import com.ojassoft.astrosage.varta.ui.fragments.VideoCallHistoryFragment;
import com.ojassoft.astrosage.varta.ui.fragments.WalletDeductionFragment;
import com.ojassoft.astrosage.varta.utils.CDatabaseHelper;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.CustomProgressDialog;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ConsultantHistoryActivity extends BaseActivity implements VolleyResponse , View.OnClickListener {
    LinearLayout navView;
    ViewPager pager;
    ViewPagerAdapter adapter;
    TabLayout tablayout;
    CustomProgressDialog pd;
    RequestQueue queue;
    RelativeLayout containerLayout;
    TextView walletAmountTV;
    TextView titleTV;
    ImageView backIV;
    FloatingActionButton fabConsult;
    public ConsultantHistoryBean consultantHistoryBean;
    LinearLayout walletLayout;
    public static int current_page = 0;
    View ongoingChatInfoLayout;
    private final Observer<Intent> ongoingChatObserver = new Observer<Intent>() {
        @Override
        public void onChanged(Intent intent) {
            //Log.d("test_ongoing_chat","intent ==>>"+intent);
            String remTime = intent.getStringExtra("rem_time");
            String CHANNEL_ID = intent.getStringExtra(CGlobalVariables.CHAT_USER_CHANNEL);
            String chatJsonObject = intent.getStringExtra("connect_chat_bean");
            String astrologerName = intent.getStringExtra("astrologer_name");
            String astrologerProfileUrl = intent.getStringExtra("astrologer_profile_url");
            String astrologerId = intent.getStringExtra("astrologer_id");
            String userChatTime = intent.getStringExtra("userChatTime");
            String chatinitiatetype = intent.getStringExtra(CGlobalVariables.CHATINITIATETYPE);
            // Log.d("test_ongoing_chat","remTime ==>>"+remTime);
//            Log.d("test_ongoing_chat","CHANNEL_ID ==>>"+CHANNEL_ID);
//            Log.d("test_ongoing_chat","chatJsonObject ==>>"+chatJsonObject);
//            Log.d("test_ongoing_chat","astrologerName ==>>"+astrologerName);
//            Log.d("test_ongoing_chat","astrologerProfileUrl ==>>"+astrologerProfileUrl);
//            Log.d("test_ongoing_chat","astrologerId ==>>"+astrologerId);

            if (!remTime.equals("00:00:00")) {
                if (AstrosageKundliApplication.selectedAstrologerDetailBean != null) {
                    CUtils.joinOngoingChatLayoutView(ConsultantHistoryActivity.this, remTime, CHANNEL_ID, chatJsonObject, astrologerName, astrologerProfileUrl, astrologerId, userChatTime,chatinitiatetype);
                    ongoingChatInfoLayout.setVisibility(View.VISIBLE);
                }
            } else {
                // OngoingChatManager.getOngoingChatLiveData().removeObserver(ongoingChatObserver);
                ongoingChatInfoLayout.setVisibility(View.GONE);
            }
        }
    };
    public void showOrHideOngoingChat() {
        if (CUtils.checkServiceRunning(OnGoingChatService.class)) {
            OngoingCallChatManager.getOngoingChatLiveData().observe(this, ongoingChatObserver);
        } else {
            if (ongoingChatObserver != null) {
                OngoingCallChatManager.getOngoingChatLiveData().removeObserver(ongoingChatObserver);
            }
            ongoingChatInfoLayout.setVisibility(View.GONE);
        }

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        consultantHistoryBean = new ConsultantHistoryBean();
        setContentView(R.layout.consultant_history_layout);
        backIV = findViewById(R.id.ivBack);
        fabConsult = findViewById(R.id.fabConsult);
        walletAmountTV = findViewById(R.id.wallet_price_txt);
        walletLayout = findViewById(R.id.wallet_layout);
        walletLayout.setVisibility(View.VISIBLE);
        titleTV = findViewById(R.id.tvTitle);
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        pager = (ViewPager) findViewById(R.id.viewPager);

        tablayout = (TabLayout) findViewById(R.id.tabs);
        tablayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        tablayout.setTabGravity(TabLayout.GRAVITY_CENTER);
        ongoingChatInfoLayout = findViewById(R.id.join_ongoing_chat_layout);
        containerLayout = (RelativeLayout) findViewById(R.id.container);
        titleTV.setText(getResources().getString(R.string.consult_history));
        titleTV.setMaxLines(1);
        FontUtils.changeFont(this, titleTV, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        //FontUtils.changeFont(this, walletAmountTV, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        fabConsult.setOnClickListener(v->{
            fabActions();
        });

        navView = findViewById(R.id.nav_view);
        //navView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        customBottomNavigationFont(navView);
        setBottomNavigationText();
        //navView.getMenu().setGroupCheckable(0,false,true);
        //navView.setSelectedItemId(R.id.bottom_nav_history);

        setPagerAdapter();

//        if (android.os.Build.VERSION.SDK_INT >= 21) {
//            getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
//        }

        //getCallAndRechargeHistory();
        setOnCLickListener();
//        if(!CUtils.getBooleanData(this, CGlobalVariables.IS_KUNDLI_SERVER_CHAT_HIST_LOADED, false)){
//            getKundliChatHistoryFromServer(CUtils.getIntData(this, CGlobalVariables.LOADED_HISTORY_PAGE_NO, 0)+1);
//       }

    }

    private final BroadcastReceiver mReceiverBackgroundLoginService = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //errorLogsConsultation = errorLogsConsultation + "CHActivity mReceiverBackgroundLoginService \n";
            String status = intent.getStringExtra("status");
            if (status.equals(CGlobalVariables.SUCCESS)) {
                //getConSulHisDataFromServer(CONSULT_HISTORY_CALL, "0");
            } else {
                CUtils.showSnackbar(containerLayout, getResources().getString(R.string.something_wrong_error), getApplicationContext());
            }

            if (mReceiverBackgroundLoginService != null) {
                LocalBroadcastManager.getInstance(ConsultantHistoryActivity.this).unregisterReceiver(mReceiverBackgroundLoginService);
            }

        }
    };

    private void setOnCLickListener() {
        walletLayout.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wallet_layout:
                CUtils.fcmAnalyticsEvents(CGlobalVariables.CONSULTATION_HISTORY_WALLET_CLICK,
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                createSession(ConsultantHistoryActivity.this,CGlobalVariables.CONSULTATION_HISTORY_WALLET_CLICK_PARTNER_ID);
                openWalletScreen(CGlobalVariables.RECHARGE_SCRREN);
                break;
        }

    }

    private void openWalletScreen(String whichScreen) {
        Intent intent = new Intent(ConsultantHistoryActivity.this, WalletActivity.class);
        intent.putExtra(CGlobalVariables.CALLING_ACTIVITY, "DashBoardActivity");
        startActivity(intent);
    }
    /**
     * Bottom Navigation View
     */
    /*
    SAN common code
    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            CUtils.openAstroSageHomeActivity(ConsultantHistoryActivity.this);
                            return true;
                        case R.id.navigation_recharge:
                            CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_CONSULT_BOTTOM_CALL, FIREBASE_EVENT_ITEM_CLICK, "");
                            createSession(ConsultantHistoryActivity.this, CONSULT_HISTORY_BOTTOM_BAR_CALL_PARTNER_ID);
                            switchToConsultTab(FILTER_TYPE_CALL, ConsultantHistoryActivity.this);
                            return true;
                        case R.id.navigation_share:
                            fabActions();
                            //CUtils.sendFeedbackActivity(ConsultantHistoryActivity.this);
                            return true;
                        case R.id.navigation_notification:
                            CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_CONSULT_BOTTOM_CHAT, FIREBASE_EVENT_ITEM_CLICK, "");
                            createSession(ConsultantHistoryActivity.this, CONSULT_HISTORY_BOTTOM_BAR_CHAT_PARTNER_ID);
                            switchToConsultTab(FILTER_TYPE_CHAT, ConsultantHistoryActivity.this);
                            return true;
                        case R.id.navigation_profile:
                            //Intent accountIntent = new Intent(ConsultantHistoryActivity.this, MyAccountActivity.class);
                            //startActivity(accountIntent);
                            return true;
                    }
                    return false;
                }
            };
*/
    public void isUserLogin(Fragment fragment, String whichScreen) {
        boolean isLogin = false;
        isLogin = CUtils.getUserLoginStatus(ConsultantHistoryActivity.this);
        if (!isLogin) {
            CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_NAV_SIGNUP,
                    CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
            Intent intent = new Intent(ConsultantHistoryActivity.this, LoginSignUpActivity.class);
            intent.putExtra(CGlobalVariables.IS_FROM_SCREEN, whichScreen);
            startActivity(intent);
        } else {
            if (whichScreen.equals(CGlobalVariables.RECHARGE_SCRREN)) {
                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_NAV_RECHARGE,
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                openWalletScreen(CGlobalVariables.RECHARGE_SCRREN);
            } else {
                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_NAV_PROFILE,
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                Intent intent = new Intent(ConsultantHistoryActivity.this, MyAccountActivity.class);
                startActivity(intent);
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
                FontUtils.changeFont(ConsultantHistoryActivity.this, ((TextView) v), CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
                ((TextView) v).setTextSize(11);
                ((TextView) v).setTextColor(getResources().getColor(R.color.black));
                // added by vishal
                ((TextView) v).setEllipsize(TextUtils.TruncateAt.END);
                ((TextView) v).setPadding(0, 0, 0, 0);
            }
        } catch (Exception e) {
        }
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    /*private void setBottomNavigationText() {
        // get menu from navigationView
        Menu menu = navView.getMenu();
        // find MenuItem you want to change
        MenuItem navHome = menu.findItem(R.id.navigation_home);
        MenuItem navRead = menu.findItem(R.id.navigation_recharge);
        MenuItem navNotificaton = menu.findItem(R.id.navigation_share);
        MenuItem navMyaccount = menu.findItem(R.id.navigation_profile);

        // set new title to the MenuItem
        navHome.setTitle(getResources().getString(R.string.title_home));
        com.ojassoft.astrosage.utils.CUtils.handleFabOnActivities(this,fabConsult,navNotificaton);
        navMyaccount.setTitle(getResources().getString(R.string.history));
    }*/

    private void setBottomNavigationText() {

        // find MenuItem you want to change
        TextView navLive = navView.findViewById(R.id.txtViewLive);
        TextView navHomeTxt = navView.findViewById(R.id.txtViewHome);
        ImageView navCallImg = navView.findViewById(R.id.imgViewCall);
        TextView navCallTxt = navView.findViewById(R.id.txtViewCall);
        TextView navChatTxt = navView.findViewById(R.id.txtViewChat);
        ImageView navHisImg = navView.findViewById(R.id.imgViewHistory);
        TextView navHisTxt = navView.findViewById(R.id.txtViewHistory);

        boolean isAIChatDisplayed = com.ojassoft.astrosage.utils.CUtils.isAIChatDisplayed(this);
        if(isAIChatDisplayed){
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
        com.ojassoft.astrosage.utils.CUtils.handleFabOnActivities(this,fabConsult,navLive);
        navHisTxt.setText(getResources().getString(R.string.history));
        navHisImg.setImageResource(R.drawable.history_icon);
        //setting Click listener
        navView.findViewById(R.id.bottom_nav_home).setOnClickListener(navbarItemSelectedListener);
        navView.findViewById(R.id.bottom_nav_call).setOnClickListener(navbarItemSelectedListener);
        navView.findViewById(R.id.bottom_nav_live).setOnClickListener(navbarItemSelectedListener);
        navView.findViewById(R.id.bottom_nav_chat).setOnClickListener(navbarItemSelectedListener);
        navView.findViewById(R.id.bottom_nav_history).setOnClickListener(navbarItemSelectedListener);
    }

    private void getCallAndRechargeHistory() {
        //Log.e("SAN ", "CHActivity getCallAndRechargeHistory() ");
        //errorLogsConsultation = errorLogsConsultation + "CHActivity getCallAndRechargeHistory()\n";
        String conHisData = CUtils.getConsultationHistoryList();
        if (!TextUtils.isEmpty(conHisData) ) {
            parseConsulList( conHisData );
        }else {
            //showProgressBar();
        }
        //getConSulHisDataFromServer(CONSULT_HISTORY_CALL, "0");
    }

    private void parseConsulList(String response){
        //Log.e("SAN ", "CHActivity parseConsulList() ");
        //errorLogsConsultation = errorLogsConsultation + "CHActivity parseConsulList()\n";
        try {
            if (!TextUtils.isEmpty(response)) {

                ArrayList<CallHistoryBean> callHistoryList = new ArrayList();
                ArrayList<CallHistoryBean> videoCallHistoryList = new ArrayList();
                ArrayList<RechargeHistoryBean> rechargeHistoryList = new ArrayList();
                ArrayList<ChatHistoryBean> chatHistoryBeanList = new ArrayList();
                ArrayList<CallHistoryBean> liveHistoryBeanList = new ArrayList();
                ArrayList<CallHistoryBean> giftHistoryBeanList  = new ArrayList();
                ArrayList<DeductionHistoryBean> deductionHistoryBeanArrayList  = new ArrayList();
                CallHistoryBean callHistoryBean;
                RechargeHistoryBean rechargeHistoryBean;
                ChatHistoryBean chatHistoryBean;
                CallHistoryBean liveHistoryBean;
                CallHistoryBean giftHistoryBean;
                DeductionHistoryBean deductionHistoryBean;
                JSONArray consultations = null;
                JSONArray deductions = null;
                JSONArray recharges = null;
                JSONArray chats = null;

                JSONObject jsonObject = new JSONObject(response);
                String walletBalance = jsonObject.getString("walletbalance");
                if(jsonObject.has("consultations")){
                    consultations = jsonObject.getJSONArray("consultations");

                    if(consultations != null && consultations.length()>0) {
                        for (int i = 0; i < consultations.length(); i++) {
                            callHistoryBean = new CallHistoryBean();
                         //   Log.d("parseConsulList","consultations"+consultations.getString(i));
                            String userPhoneNo = consultations.getJSONObject(i).getString("userPhoneNo");
                            String astrologerPhoneNo = consultations.getJSONObject(i).getString("astrologerPhoneNo");
                            String astrologerName = consultations.getJSONObject(i).getString("astrologerName");
                            String consultationTime = consultations.getJSONObject(i).getString("consultationTime");
                            String callDuration = consultations.getJSONObject(i).getString("callDuration");
                            String callAmount = consultations.getJSONObject(i).getString("callAmount");
                            String astrologerImageFile = consultations.getJSONObject(i).getString("astrologerImageFile");
                            String astrologerServiceRs = consultations.getJSONObject(i).getString("astrologerServiceRs");
                            String astroWalletId = consultations.getJSONObject(i).getString("astroWalletId");
                            String urlText = consultations.getJSONObject(i).getString("urlText");
                            String consultationMode = consultations.getJSONObject(i).getString("consultationMode");
                            String callChatId = consultations.getJSONObject(i).getString("callChatId");
                            String refundStatus = consultations.getJSONObject(i).getString("refundStatus");
                            String aiai = consultations.getJSONObject(i).optString("aiai");
                            String durationUnitType = consultations.getJSONObject(i).optString("durationUnitType");
                            String callDurationMin = consultations.getJSONObject(i).optString("calldurationmin");


                            callHistoryBean.setUserPhoneNo(userPhoneNo);
                            callHistoryBean.setAstrologerPhoneNo(astrologerPhoneNo);
                            callHistoryBean.setAstrologerName(astrologerName);
                            callHistoryBean.setConsultationTime(consultationTime);
                            callHistoryBean.setCallDuration(callDuration);
                            callHistoryBean.setCallAmount(callAmount);
                            callHistoryBean.setAstrologerImageFile(astrologerImageFile);
                            callHistoryBean.setAstrologerServiceRs(astrologerServiceRs);
                            callHistoryBean.setAstroWalletId(astroWalletId);
                            callHistoryBean.setUrlText(urlText);
                            callHistoryBean.setConsultationMode(consultationMode);
                            callHistoryBean.setCallChatId(callChatId);
                            callHistoryBean.setRefundStatus(refundStatus);
                            callHistoryBean.setAiAstroId(aiai);
                            callHistoryBean.setDurationUnitType(durationUnitType);
                            callHistoryBean.setCallDurationMin(callDurationMin);

                            callHistoryList.add(callHistoryBean);
                        }
                    }
                }
                if(jsonObject.has("videos")){
                    JSONArray videos = jsonObject.getJSONArray("videos");
                    if(videos != null && videos.length()>0) {
                        for (int i = 0; i < videos.length(); i++) {
                            callHistoryBean = new CallHistoryBean();
                            String userPhoneNo = videos.getJSONObject(i).getString("userPhoneNo");
                            String astrologerPhoneNo = videos.getJSONObject(i).getString("astrologerPhoneNo");
                            String astrologerName = videos.getJSONObject(i).getString("astrologerName");
                            String consultationTime = videos.getJSONObject(i).getString("consultationTime");
                            String callDuration = videos.getJSONObject(i).getString("callDuration");
                            String callAmount = videos.getJSONObject(i).getString("callAmount");
                            String astrologerImageFile = videos.getJSONObject(i).getString("astrologerImageFile");
                            String astrologerServiceRs = videos.getJSONObject(i).getString("astrologerServiceRs");
                            String astroWalletId = videos.getJSONObject(i).getString("astroWalletId");
                            String urlText = videos.getJSONObject(i).getString("urlText");
                            String consultationMode = videos.getJSONObject(i).getString("consultationMode");
                            String callChatId = videos.getJSONObject(i).getString("callChatId");
                            String refundStatus = videos.getJSONObject(i).getString("refundStatus");
                            String durationUnitType = videos.getJSONObject(i).optString("durationUnitType");
                            String callDurationMin = videos.getJSONObject(i).optString("calldurationmin");


                            callHistoryBean.setUserPhoneNo(userPhoneNo);
                            callHistoryBean.setAstrologerPhoneNo(astrologerPhoneNo);
                            callHistoryBean.setAstrologerName(astrologerName);
                            callHistoryBean.setConsultationTime(consultationTime);
                            callHistoryBean.setCallDuration(callDuration);
                            callHistoryBean.setCallAmount(callAmount);
                            callHistoryBean.setAstrologerImageFile(astrologerImageFile);
                            callHistoryBean.setAstrologerServiceRs(astrologerServiceRs);
                            callHistoryBean.setAstroWalletId(astroWalletId);
                            callHistoryBean.setUrlText(urlText);
                            callHistoryBean.setConsultationMode(consultationMode);
                            callHistoryBean.setCallChatId(callChatId);
                            callHistoryBean.setRefundStatus(refundStatus);
                            callHistoryBean.setDurationUnitType(durationUnitType);
                            callHistoryBean.setCallDurationMin(callDurationMin);

                            videoCallHistoryList.add(callHistoryBean);
                        }
                    }
                }
                if(jsonObject.has("recharges")) {
                    recharges = jsonObject.getJSONArray("recharges");
                    if(recharges != null && recharges.length()>0) {
                        CUtils.saveBooleanData(this,CGlobalVariables.HAS_WALLET_RECHARGE_HISTORY,true);
                        for (int i = 0; i < recharges.length(); i++) {
                            rechargeHistoryBean = new RechargeHistoryBean();
                            String rechargeType = recharges.getJSONObject(i).getString("rechargeType");
                            String rechargeAmount = recharges.getJSONObject(i).getString("rechargeAmount");
                            String rechargeDateTime = recharges.getJSONObject(i).getString("rechargeDateTime");
                            String displayMessage = recharges.getJSONObject(i).getString("displayMsg");
                            String orderId = recharges.getJSONObject(i).getString("orderId");
                            String message = recharges.getJSONObject(i).optString("message");
                            String referralMsg = recharges.getJSONObject(i).optString("referralMsg");
                            rechargeHistoryBean.setRechargeType(rechargeType);
                            rechargeHistoryBean.setRechargeAmount(rechargeAmount);
                            rechargeHistoryBean.setRechargeDateTime(rechargeDateTime);
                            rechargeHistoryBean.setDisplayMsg(displayMessage);
                            rechargeHistoryBean.setOrderId(orderId);
                            rechargeHistoryBean.setMessage(message);
                            rechargeHistoryBean.setReferralMsg(referralMsg);
                            rechargeHistoryList.add(rechargeHistoryBean);
                        }
                    }
                }

                if(jsonObject.has("chats")) {
                    chats = jsonObject.getJSONArray("chats");
                    if(chats != null && chats.length()>0) {
                        for (int i = 0; i < chats.length(); i++) {
                            chatHistoryBean = new ChatHistoryBean();
                            String userPhoneNo = chats.getJSONObject(i).getString("userPhoneNo");
                            String astrologerPhoneNo = chats.getJSONObject(i).getString("astrologerPhoneNo");
                            String astrologerName = chats.getJSONObject(i).getString("astrologerName");
                            String consultationTime = chats.getJSONObject(i).getString("consultationTime");
                            String callDuration = chats.getJSONObject(i).getString("callDuration");
                            String callAmount = chats.getJSONObject(i).getString("callAmount");
                            String astrologerImageFile = chats.getJSONObject(i).getString("astrologerImageFile");
                            String astrologerServiceRs = chats.getJSONObject(i).getString("astrologerServiceRs");
                            String astroWalletId = chats.getJSONObject(i).getString("astroWalletId");
                            String urlText = chats.getJSONObject(i).getString("urlText");
                            String consultationMode = chats.getJSONObject(i).getString("consultationMode");
                            String callChatId = chats.getJSONObject(i).getString("callChatId");
                            String refundStatus = chats.getJSONObject(i).getString("refundStatus");
                            String durationUnitType = chats.getJSONObject(i).optString("durationUnitType");
                            String callDurationMin = chats.getJSONObject(i).optString("calldurationmin");



                            chatHistoryBean.setUserPhoneNo(userPhoneNo);
                            chatHistoryBean.setAstrologerPhoneNo(astrologerPhoneNo);
                            chatHistoryBean.setAstrologerName(astrologerName);
                            chatHistoryBean.setConsultationTime(consultationTime);
                            chatHistoryBean.setCallDuration(callDuration);
                            chatHistoryBean.setCallAmount(callAmount);
                            chatHistoryBean.setAstrologerImageFile(astrologerImageFile);
                            chatHistoryBean.setAstrologerServiceRs(astrologerServiceRs);
                            chatHistoryBean.setAstroWalletId(astroWalletId);
                            chatHistoryBean.setUrlText(urlText);
                            chatHistoryBean.setConsultationMode(consultationMode);
                            chatHistoryBean.setCallChatId(callChatId);
                            chatHistoryBean.setRefundStatus(refundStatus);
                            chatHistoryBean.setDurationUnitType(durationUnitType);
                            chatHistoryBean.setCallDurationMin(callDurationMin);


                            chatHistoryBeanList.add(chatHistoryBean);
                        }
                    }
                }
                if(jsonObject.has("livesessions")) {
                    consultations = jsonObject.getJSONArray("livesessions");
                    if(consultations != null && consultations.length()>0) {
                        for (int i = 0; i < consultations.length(); i++) {
                            liveHistoryBean = new CallHistoryBean();
                            String userPhoneNo = consultations.getJSONObject(i).getString("userPhoneNo");
                            String astrologerPhoneNo = consultations.getJSONObject(i).getString("astrologerPhoneNo");
                            String astrologerName = consultations.getJSONObject(i).getString("astrologerName");
                            String consultationTime = consultations.getJSONObject(i).getString("consultationTime");
                            String callDuration = consultations.getJSONObject(i).getString("callDuration");
                            String callAmount = consultations.getJSONObject(i).getString("callAmount");
                            String astrologerImageFile = consultations.getJSONObject(i).getString("astrologerImageFile");
                            String astrologerServiceRs = consultations.getJSONObject(i).getString("astrologerServiceRs");
                            String astroWalletId = consultations.getJSONObject(i).getString("astroWalletId");
                            String urlText = consultations.getJSONObject(i).getString("urlText");
                            String consultationMode = consultations.getJSONObject(i).getString("consultationMode");
                            String callChatId = consultations.getJSONObject(i).getString("callChatId");
                            String refundStatus = consultations.getJSONObject(i).getString("refundStatus");
                            String durationUnitType = consultations.getJSONObject(i).optString("durationUnitType");
                            String callDurationMin = consultations.getJSONObject(i).optString("calldurationmin");



                            liveHistoryBean.setUserPhoneNo(userPhoneNo);
                            liveHistoryBean.setAstrologerPhoneNo(astrologerPhoneNo);
                            liveHistoryBean.setAstrologerName(astrologerName);
                            liveHistoryBean.setConsultationTime(consultationTime);
                            liveHistoryBean.setCallDuration(callDuration);
                            liveHistoryBean.setCallAmount(callAmount);
                            liveHistoryBean.setAstrologerImageFile(astrologerImageFile);
                            liveHistoryBean.setAstrologerServiceRs(astrologerServiceRs);
                            liveHistoryBean.setAstroWalletId(astroWalletId);
                            liveHistoryBean.setUrlText(urlText);
                            liveHistoryBean.setConsultationMode(consultationMode);
                            liveHistoryBean.setCallChatId(callChatId);
                            liveHistoryBean.setRefundStatus(refundStatus);
                            liveHistoryBean.setDurationUnitType(durationUnitType);
                            liveHistoryBean.setCallDurationMin(callDurationMin);

                            liveHistoryBeanList.add(liveHistoryBean);
                        }
                    }
                }
                if(jsonObject.has("gifts")) {
                    consultations = jsonObject.getJSONArray("gifts");
                    if(consultations != null && consultations.length()>0) {
                        for (int i = 0; i < consultations.length(); i++) {
                            giftHistoryBean = new CallHistoryBean();
                            String userPhoneNo = consultations.getJSONObject(i).getString("userPhoneNo");
                            String astrologerPhoneNo = consultations.getJSONObject(i).getString("astrologerPhoneNo");
                            String astrologerName = consultations.getJSONObject(i).getString("astrologerName");
                            String consultationTime = consultations.getJSONObject(i).getString("consultationTime");
                            String callDuration = consultations.getJSONObject(i).getString("callDuration");
                            String callAmount = consultations.getJSONObject(i).getString("callAmount");
                            String astrologerImageFile = consultations.getJSONObject(i).getString("astrologerImageFile");
                            String astrologerServiceRs = consultations.getJSONObject(i).getString("astrologerServiceRs");
                            String astroWalletId = consultations.getJSONObject(i).getString("astroWalletId");
                            String urlText = consultations.getJSONObject(i).getString("urlText");
                            String consultationMode = consultations.getJSONObject(i).getString("consultationMode");
                            String callChatId = consultations.getJSONObject(i).getString("callChatId");
                            String serviceIdId = consultations.getJSONObject(i).getString("serviceIdId");
                            String refundStatus = consultations.getJSONObject(i).getString("refundStatus");
                            String durationUnitType = consultations.getJSONObject(i).optString("durationUnitType");
                            String callDurationMin = consultations.getJSONObject(i).optString("calldurationmin");


                            giftHistoryBean.setUserPhoneNo(userPhoneNo);
                            giftHistoryBean.setAstrologerPhoneNo(astrologerPhoneNo);
                            giftHistoryBean.setAstrologerName(astrologerName);
                            giftHistoryBean.setConsultationTime(consultationTime);
                            giftHistoryBean.setCallDuration(callDuration);
                            giftHistoryBean.setCallAmount(callAmount);
                            giftHistoryBean.setAstrologerImageFile(astrologerImageFile);
                            giftHistoryBean.setAstrologerServiceRs(astrologerServiceRs);
                            giftHistoryBean.setAstroWalletId(astroWalletId);
                            giftHistoryBean.setUrlText(urlText);
                            giftHistoryBean.setConsultationMode(consultationMode);
                            giftHistoryBean.setCallChatId(callChatId);
                            giftHistoryBean.setServiceIdId(serviceIdId);
                            giftHistoryBean.setRefundStatus(refundStatus);
                            giftHistoryBean.setDurationUnitType(durationUnitType);
                            giftHistoryBean.setCallDurationMin(callDurationMin);


                            giftHistoryBeanList.add(giftHistoryBean);
                        }
                    }
                }
                if (jsonObject.has("deduction")) {
                    deductions = jsonObject.getJSONArray("deduction");
                    if (deductions != null && deductions.length() > 0) {
                        for (int i = 0; i < deductions.length(); i++) {
                            deductionHistoryBean = new DeductionHistoryBean();
                            String deductedAmount = deductions.getJSONObject(i).getString("deductedAmount");
                            String deductedTime = deductions.getJSONObject(i).getString("deductedTime");
                            String displayMessage = deductions.getJSONObject(i).getString("displayMsg");
                            String orderId = deductions.getJSONObject(i).getString("orderId");
                            String purchaseId = deductions.getJSONObject(i).getString("purchaseId");

                            //Log.e("SAN ", "CHA response parse consultationId " + rechargeId );


                            deductionHistoryBean.setDeductedAmount(deductedAmount);
                            deductionHistoryBean.setDeductedTime(deductedTime);
                            deductionHistoryBean.setDisplayMsg(displayMessage);
                            deductionHistoryBean.setDisplayMsg(displayMessage);
                            deductionHistoryBean.setOrderId(orderId);
                            deductionHistoryBean.setPurchaseId(purchaseId);
                            deductionHistoryBeanArrayList.add(deductionHistoryBean);

                        }
                    }



                }
                consultantHistoryBean.setWalletbalance(walletBalance);
                consultantHistoryBean.setCallHistoryList(callHistoryList);
                consultantHistoryBean.setVideoCallHistoryBeanList(videoCallHistoryList);
                consultantHistoryBean.setRechargeHistoryList(rechargeHistoryList);
                consultantHistoryBean.setChatHistoryBeanList(chatHistoryBeanList);
                consultantHistoryBean.setLiveHistoryBeanList(liveHistoryBeanList);
                consultantHistoryBean.setGiftHistoryBeanList(giftHistoryBeanList);
                consultantHistoryBean.setDeductionHistoryBeanArrayList(deductionHistoryBeanArrayList);
                walletAmountTV.setText(getResources().getString(R.string.rs_sign) + CUtils.convertAmtIntoIndianFormat(consultantHistoryBean.getWalletbalance()));

                setPagerAdapter();
            } else {
                if (getCurrentSelectedFrag() instanceof CallHistoryFragment) {
                    ((CallHistoryFragment) getCurrentSelectedFrag()).showErrorMsg(false);
                } else if (getCurrentSelectedFrag() instanceof ChatHistoryFragment) {
                    ((ChatHistoryFragment) getCurrentSelectedFrag()).showErrorMsg(false);
                } else {
                    ((RechargeHistoryFragment) getCurrentSelectedFrag()).showErrorMsg(false);
                }
            }

            hideProgressBar();

        } catch (Exception e) {
            //Log.e("SAN ", "CHActivity parseConsulList() exp " + e.toString() );
            //errorLogsConsultation = errorLogsConsultation + "CHActivity parseConsulList() exp " + e.toString()+"\n";
            String status="";
            try {
                JSONObject jsonObject = new JSONObject(response);
                 status = jsonObject.getString("status");
                if ( status.equals("100") ) {

                    LocalBroadcastManager.getInstance(this).registerReceiver(mReceiverBackgroundLoginService
                            , new IntentFilter(CGlobalVariables.SEND_BROADCAST_BACK_GR_LOGIN));

                    startBackgroundLoginService();
                }
            } catch (Exception exception){
                //Log.e("SAN CHA response ", " pasrse exp " + exception.toString() );
                //Log.e("SAN ", "CHActivity parseConsulList() exp 1 " + e.toString() );
                //errorLogsConsultation = errorLogsConsultation + "CHActivity parseConsulList() exp 1 " + e.toString()+"\n";
            }

            if ( !status.equals("100") ) {

                hideProgressBar();

                if (getCurrentSelectedFrag() instanceof CallHistoryFragment) {
                    ((CallHistoryFragment) getCurrentSelectedFrag()).showErrorMsg(false);
                }else if (getCurrentSelectedFrag() instanceof ChatHistoryFragment) {
                    ((ChatHistoryFragment) getCurrentSelectedFrag()).showErrorMsg(false);
                } else {
                    ((RechargeHistoryFragment) getCurrentSelectedFrag()).showErrorMsg(false);
                }
            }

        }
    }

    private void startBackgroundLoginService() {
        try {
            if (CUtils.getUserLoginStatus(this)) {
                CUtils.fcmAnalyticsEvents("bg_login_from_consult_history",CGlobalVariables.VARTA_BACKGROUND_LOGIN,"");

                Intent intent = new Intent(ConsultantHistoryActivity.this, Loginservice.class);
                startService(intent);
            }
        } catch (Exception e) {}
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mReceiverBackgroundLoginService != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(mReceiverBackgroundLoginService);
        }

    }

//    private void getConSulHisDataFromServer(String historyType, String lastId) {
//
//        if (!CUtils.isConnectedWithInternet(ConsultantHistoryActivity.this)) {
//            CUtils.showSnackbar(containerLayout, getResources().getString(R.string.no_internet), ConsultantHistoryActivity.this);
//            hideProgressBar();
//        } else {
//            String url = CGlobalVariables.CONSULTATIONHISTORY;
//            //Log.e("SAN ", "CHA Consultation History" + url);
//            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, url,
//                    ConsultantHistoryActivity.this, false, getParamsNew(historyType, lastId), 1).getMyStringRequest();
//            stringRequest.setShouldCache(false);
//            queue.add(stringRequest);
//        }
//
//    }


    public Map<String, String> getParamsNew(String historyType, String lastId) {

        HashMap<String, String> headers = new HashMap<String, String>();
        try {
            headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(ConsultantHistoryActivity.this));
            headers.put(CGlobalVariables.USER_PHONE_NO, CUtils.getUserID(ConsultantHistoryActivity.this));
            headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(ConsultantHistoryActivity.this));
            //Log.e("SAN CHA ", " getCallAndRechargeHistory params " + headers.toString() );
            headers.put(CGlobalVariables.LANG,CUtils.getLanguageKey(LANGUAGE_CODE));
            headers.put(CGlobalVariables.IGNORE_ASTRO,"true");
            headers.put(CONSULT_HISTORY_TYPE, historyType);
            headers.put(CONSULT_HISTORY_LIST_ID, lastId);
            //Log.e("SAN ", "CHA Consultation History params " + headers.toString() );
        } catch (Exception e) {

        }
        return headers;
    }

    private void setPagerAdapter() {

        ArrayList<Fragment> fragList = new ArrayList<Fragment>();

        fragList.add(new RechargeHistoryFragment());
        fragList.add(new CallHistoryFragment());
        fragList.add(new ChatHistoryFragment());
        fragList.add(new KundliAIChatHistoryFragment());
        fragList.add(new VideoCallHistoryFragment());
        fragList.add(new LiveHistoryFragment());
        fragList.add(new GiftHistoryFragment());
        fragList.add(new WalletDeductionFragment());

        String title[] = getResources().getStringArray(R.array.consultation_history_title);
        adapter = new ViewPagerAdapter(this, getSupportFragmentManager(), fragList);
        pager.setAdapter(adapter);
        tablayout.setupWithViewPager(pager);
        if (tablayout.getTabAt(0).getCustomView() != null) {
            FontUtils.changeFont(this, (TextView) tablayout.getTabAt(0).getCustomView(), CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        }

    }

    private void showProgressBar(){
        try {
            if (pd == null) {
                pd = new CustomProgressDialog(ConsultantHistoryActivity.this);
            }
            pd.show();
            pd.setCancelable(false);
        }catch (Exception e){
            //
        }
    }

    private void hideProgressBar(){
        try {
            if (pd != null && pd.isShowing())
                pd.dismiss();
        } catch (Exception e){}
    }

    @Override
    public void onResponse(String response, int method) {
        //Log.e("SAN CHA response ", response);
        int maxLogSize = 1000;
        for(int i = 0; i <= response.length() / maxLogSize; i++) {
            int start = i * maxLogSize;
            int end = (i+1) * maxLogSize;
            end = end > response.length() ? response.length() : end;
            //Log.v("kdjsfdsghfksdghfk", response.substring(start, end));
        }
        if (method == 1) {
            parseConsulList(response);
        }
    }

    @Override
    public void onError(VolleyError error) {
        hideProgressBar();
        //SAN CHA response ", " error " + error.toString());
        if (getCurrentSelectedFrag() instanceof CallHistoryFragment) {
            ((CallHistoryFragment) getCurrentSelectedFrag()).showErrorMsg(false);
        } else if (getCurrentSelectedFrag() instanceof ChatHistoryFragment) {
            ((ChatHistoryFragment) getCurrentSelectedFrag()).showErrorMsg(false);
        } else {
            ((RechargeHistoryFragment) getCurrentSelectedFrag()).showErrorMsg(false);
        }
    }

    private Fragment getCurrentSelectedFrag() {
        return adapter.getItem(pager.getCurrentItem());
    }

    public void fabActions(){
        try {
            //boolean liveStreamingEnabledForAstrosage = CUtils.getBooleanData(this, CGlobalVariables.liveStreamingEnabledForAstrosage, false);
            boolean liveStreamingEnabledForAstrosage = true;
            if(!liveStreamingEnabledForAstrosage){ //fetch data according to tagmanag
                CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_CONSULT_BOTTOM_KUNDLI, FIREBASE_EVENT_ITEM_CLICK, "");
                createSession(this, CONSULT_HISTORY_BOTTOM_BAR_KUNDLI_PARTNER_ID);
                openKundliActivity(ConsultantHistoryActivity.this);
            }
            else {
                CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_CONSULT_BOTTOM_LIVE, FIREBASE_EVENT_ITEM_CLICK, "");
                createSession(this, CONSULT_HISTORY_BOTTOM_BAR_LIVE_PARTNER_ID);
                gotoAllLiveActivityFromLiveIcon(this);
            }
        }catch (Exception e){
            //
        }
    }

    /*@Override
    protected void onResume() {
        super.onResume();
        navView.setSelectedItemId(R.id.bottom_nav_history);
    }*/

    @Override
    protected void onResume() {
        super.onResume();
        if (!CUtils.getUserLoginStatus(ConsultantHistoryActivity.this)){
            finish();
        }
        showOrHideOngoingChat();
    }

    @Override
    public void clickCall() {
        CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_CONSULT_BOTTOM_CALL, FIREBASE_EVENT_ITEM_CLICK, "");
        createSession(ConsultantHistoryActivity.this, CONSULT_HISTORY_BOTTOM_BAR_CALL_PARTNER_ID);
        super.clickCall();
    }

    @Override
    public void clickChat() {
        CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_CONSULT_BOTTOM_CHAT, FIREBASE_EVENT_ITEM_CLICK, "");
        createSession(ConsultantHistoryActivity.this, CONSULT_HISTORY_BOTTOM_BAR_CHAT_PARTNER_ID);
        super.clickChat();
    }

    @Override
    public void clickLive() {
        fabActions();
    }

    @Override
    public void clickHistory() {
        //do nothing
    }

    public void updateWalletBalance(){
        walletAmountTV.setText(getResources().getString(R.string.rs_sign) + CUtils.convertAmtIntoIndianFormat(consultantHistoryBean.getWalletbalance()));
    }

    /*public void getKundliChatHistoryFromServer(int pageNo) {

        Call<ResponseBody> call = RetrofitClient.getAIInstance().create(ApiList.class).getUserChatHistory(getUserChatHistoryParams(pageNo));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        String data = response.body().string();
                        Log.e("chatBackup", "response : "+data);
                        if(!data.isEmpty()){
                           // parseServerHistory(data, pageNo);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }*/

    /*private Map<String, String> getUserChatHistoryParams(int pageNo) {
        Map<String, String> params = new HashMap<>();
        params.put("packagename", com.ojassoft.astrosage.utils.CGlobalVariables.ASTROSAGE_AI_PACKAGE_NAME);
        params.put("methodname", "getuserchathistorytitles");
        params.put("userid", com.ojassoft.astrosage.utils.CUtils.getUserName(this));
        params.put("pageno", String.valueOf(pageNo));
        params.put(CGlobalVariables.DEVICE_ID, CUtils.getMyAndroidId(this));
        params.put(CGlobalVariables.APP_VERSION, "" + BuildConfig.VERSION_NAME);
        params.put(CGlobalVariables.KEY_API,CUtils.getApplicationSignatureHashCode(this));
        Log.e("chatBackup", "params : "+params);
        return params;
    }*/

    /*private void parseServerHistory(String data, int pageNo) {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(()->{
            try (CDatabaseHelper db = new CDatabaseHelper(ConsultantHistoryActivity.this)){
                JSONArray jsonArray = new JSONArray(data);
                CUtils.saveBooleanData(ConsultantHistoryActivity.this, CGlobalVariables.IS_KUNDLI_SERVER_CHAT_HIST_LOADED, true);
                if (jsonArray.length() > 0) {
                     CUtils.saveIntData(this, CGlobalVariables.LOADED_HISTORY_PAGE_NO, pageNo);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        int conversationId = jsonObject.getInt("CONVERSATIONID");
                        KundliChatHistoryBean kundliChatHistoryBean = com.ojassoft.astrosage.utils.CUtils.parseKundliChatHistory(jsonObject);

                        if (pageNo == 1 && i == 0) {
                            CUtils.saveIntData(this, CGlobalVariables.LAST_CONVERSATION_ID, conversationId);
                        }

                        //db.insertChatHistory(kundliChatHistoryBean);

                    }
                    if(jsonArray.length() <= 15 )
                        CUtils.saveBooleanData(this, CGlobalVariables.IS_NEED_TO_LOAD_MORE_PAGE, true);
                }else{
                    CUtils.saveBooleanData(this, CGlobalVariables.IS_NEED_TO_LOAD_MORE_PAGE, false);
                }
                try {

                    Intent intent = new Intent(CGlobalVariables.ACTION_DATA_LOADED);
                    LocalBroadcastManager.getInstance(ConsultantHistoryActivity.this).sendBroadcast(intent);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } catch (Exception e) {
                Log.e("chatBackup", "parseServerHistory: "+e);
                CUtils.saveBooleanData(this, CGlobalVariables.IS_NEED_TO_LOAD_MORE_PAGE, false);
            }
        });

    }*/

}
