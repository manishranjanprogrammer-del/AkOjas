package com.ojassoft.astrosage.varta.ui.activity;

import static com.ojassoft.astrosage.utils.CGlobalVariables.DETAILED_HOROSCOPE_BOTTOM_BAR_CALL_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.DETAILED_HOROSCOPE_BOTTOM_BAR_CHAT_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_DETAILED_HOROSCOPE_BOTTOM_CALL;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_DETAILED_HOROSCOPE_BOTTOM_CHAT;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_MY_ACCOUNT_BOTTOM_CALL;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_MY_ACCOUNT_BOTTOM_CHAT;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_MY_ACCOUNT_BOTTOM_KUNDLI;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_MY_ACCOUNT_BOTTOM_LIVE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.MY_ACCOUNT_BOTTOM_BAR_CALL_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.MY_ACCOUNT_BOTTOM_BAR_CHAT_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.MY_ACCOUNT_BOTTOM_BAR_KUNDLI_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.MY_ACCOUNT_BOTTOM_BAR_LIVE_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CUtils.createSession;
import static com.ojassoft.astrosage.utils.CUtils.openKundliActivity;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.IS_GOOGLE_FACEBOOK_VISIBLE;
import static com.ojassoft.astrosage.varta.utils.CUtils.followAstrologerModelArrayList;
import static com.ojassoft.astrosage.varta.utils.CUtils.gotoAllLiveActivityFromLiveIcon;
import static com.ojassoft.astrosage.varta.utils.CUtils.showSnackbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.GlobalRetrofitResponse;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.act.AstroPrefrenceActivity;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.horoscope.DetailedHoroscope;
import com.ojassoft.astrosage.ui.fragments.NotificationSettingFragment;
import com.ojassoft.astrosage.varta.model.FollowAstrologerModel;
import com.ojassoft.astrosage.varta.ui.fragments.ReadFragment;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;
import com.ojassoft.astrosage.varta.volley_network.VolleyServiceHandler;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MyAccountActivity extends BaseActivity implements View.OnClickListener, VolleyResponse, GlobalRetrofitResponse {
    TextView titleTV;
    ImageView backIV;
    FloatingActionButton fabAccount;
    LinearLayout navView;
    RelativeLayout containerLayout;
    RequestQueue queue;
    TextView walletPriceTxt;
    LinearLayout walletLayout;
    RelativeLayout walletBoxLayout;
    LinearLayout myProfileLL,my_language, myWalletLL, myRechargeLL, shareLL, aboutusLL,following_layout, my_preferences, my_notofication, my_notification_setting, supportSetting;
    TextView userNumberHTv, userNumberTv, myProfileTv, myWalletTv, myRechargeTv, my_notification_setting_tv, support_setting_tv, my_preferences_tv;
    TextView shareWithFriendTv, aboutUsTv, joinVartaTv,my_followingCount;
    private Button logoutBtn;
    LinearLayout joinVartaLL;
    private int FETCH_FOLLOW_ASTROLOGER = 4;
    //public static ArrayList<FollowAstrologerModel> followAstrologerModelArrayList;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.myaccount_layout);
        setToolbarWallet();
        containerLayout=findViewById(R.id.container);
        titleTV = findViewById(R.id.tvTitle);
        backIV = findViewById(R.id.ivBack);
        fabAccount = findViewById(R.id.fabAccount);
        myProfileLL = findViewById(R.id.my_profilell);
        my_language = findViewById(R.id.my_language);
        following_layout = findViewById(R.id.following_layout);
        myWalletLL = findViewById(R.id.my_walletll);
        myRechargeLL = findViewById(R.id.my_rechargell);
        userNumberHTv = findViewById(R.id.logged_in_with);
        userNumberTv = findViewById(R.id.user_number);
        myProfileTv = findViewById(R.id.my_profiletv);
        myWalletTv = findViewById(R.id.my_wallettv);
        myRechargeTv = findViewById(R.id.my_rechargetv);
        logoutBtn = findViewById(R.id.logout_btn);
        shareWithFriendTv = findViewById(R.id.sharetv);
        aboutUsTv = findViewById(R.id.aboutustv);
        shareLL = findViewById(R.id.share);
        aboutusLL = findViewById(R.id.about_us);
        joinVartaTv = findViewById(R.id.join_varta_tv);
        joinVartaLL = findViewById(R.id.join_varta_ll);
        my_followingCount = findViewById(R.id.my_followingCount);
        my_preferences = findViewById(R.id.my_preferences);
        my_notification_setting = findViewById(R.id.my_notification_setting);
        supportSetting = findViewById(R.id.support_setting);
        my_preferences_tv = findViewById(R.id.my_preferences_tv);
        my_notification_setting_tv = findViewById(R.id.my_notification_setting_tv);
        support_setting_tv = findViewById(R.id.support_setting_tv);
        titleTV.setText(getResources().getString(R.string.account));
        userNumberTv.setText("+" + CUtils.getCountryCode(MyAccountActivity.this)+ " " +CUtils.getUserID(MyAccountActivity.this));
        FontUtils.changeFont(this, userNumberHTv, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(this, titleTV, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(this, userNumberTv, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(this, myProfileTv, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(this, myWalletTv, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(this, myRechargeTv, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(this, logoutBtn, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(this, shareWithFriendTv, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(this, aboutUsTv, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(this, joinVartaTv, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(this, my_preferences_tv, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(this, my_notification_setting_tv, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(this, support_setting_tv, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);

        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        navView = findViewById(R.id.nav_view);
        //navView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        customBottomNavigationFont(navView);
        setBottomNavigationText();
        //navView.getMenu().setGroupCheckable(0,false,true);
        queue = VolleySingleton.getInstance(this).getRequestQueue();

        if (followAstrologerModelArrayList == null) followAstrologerModelArrayList = new ArrayList<>();

        if(followAstrologerModelArrayList.size()>0) {
            my_followingCount.setVisibility(View.VISIBLE);
            my_followingCount.setText(getResources().getString(R.string.followingCountVal).replace("0",""+followAstrologerModelArrayList.size()));
        }

        setOnCLickListener();

    }

    private void setToolbarWallet(){
        walletLayout = findViewById(R.id.wallet_layout);
        walletPriceTxt = findViewById(R.id.wallet_price_txt);
        walletBoxLayout = findViewById(R.id.wallet_box_layout);
        walletBoxLayout.setVisibility(View.VISIBLE);
        walletLayout.setVisibility(View.VISIBLE);
        walletPriceTxt.setText(getResources().getString(R.string.astroshop_rupees_sign) + CUtils.convertAmtIntoIndianFormat( CUtils.getWalletRs(MyAccountActivity.this) ));
    }

    private void setOnCLickListener() {
        walletLayout.setOnClickListener(this);
        myProfileLL.setOnClickListener(this);
        my_language.setOnClickListener(this);
        following_layout.setOnClickListener(this);
        myWalletLL.setOnClickListener(this);
        myRechargeLL.setOnClickListener(this);
        logoutBtn.setOnClickListener(this);
        shareLL.setOnClickListener(this);
        aboutusLL.setOnClickListener(this);
        joinVartaLL.setOnClickListener(this);
        fabAccount.setOnClickListener(this);
        my_notification_setting.setOnClickListener(this);
        supportSetting.setOnClickListener(this);
        my_preferences.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        getFollowingAstrologerDataFromServer();
    }

    /**
     * Bottom Navigation View
     */
    /*BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            CUtils.openAstroSageHomeActivity(MyAccountActivity.this);
                            return true;
                        case R.id.navigation_recharge:
                            CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_MY_ACCOUNT_BOTTOM_CALL, FIREBASE_EVENT_ITEM_CLICK, "");
                            createSession(MyAccountActivity.this, MY_ACCOUNT_BOTTOM_BAR_CALL_PARTNER_ID);
                            switchToConsultTab(FILTER_TYPE_CALL, MyAccountActivity.this);
                            return true;
                        case R.id.navigation_share:
                            fabActions();
                            return true;
                        case R.id.navigation_notification:
                            CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_MY_ACCOUNT_BOTTOM_CHAT, FIREBASE_EVENT_ITEM_CLICK, "");
                            createSession(MyAccountActivity.this, MY_ACCOUNT_BOTTOM_BAR_CHAT_PARTNER_ID);
                            switchToConsultTab(FILTER_TYPE_CHAT, MyAccountActivity.this);
                            return true;
                        case R.id.navigation_profile:

                            return true;
                    }
                    return false;
                }
            };*/

    public void customBottomNavigationFont(final View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    customBottomNavigationFont(child);
                }
            } else if (v instanceof TextView) {
                FontUtils.changeFont(MyAccountActivity.this, ((TextView) v), CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
                ((TextView) v).setTextSize(11);
                ((TextView) v).setTextColor(getResources().getColor(R.color.black));
                // added by vishal
                ((TextView) v).setEllipsize(TextUtils.TruncateAt.END);
                ((TextView) v).setPadding(0, 0, 0, 0);
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
        com.ojassoft.astrosage.utils.CUtils.handleFabOnActivities(this,fabAccount,navLive);

        navHisTxt.setText(getResources().getString(R.string.history));
        navHisImg.setImageResource(R.drawable.nav_more_icons);
        //setting click listeners
        navView.findViewById(R.id.bottom_nav_home).setOnClickListener(navbarItemSelectedListener);
        navView.findViewById(R.id.bottom_nav_call).setOnClickListener(navbarItemSelectedListener);
        navView.findViewById(R.id.bottom_nav_live).setOnClickListener(navbarItemSelectedListener);
        navView.findViewById(R.id.bottom_nav_chat).setOnClickListener(navbarItemSelectedListener);
        navView.findViewById(R.id.bottom_nav_history).setOnClickListener(navbarItemSelectedListener);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            boolean isRecharged = bundle.getBoolean(CGlobalVariables.IS_RECHARGED);
            if (isRecharged) {
                String orderID = bundle.getString(CGlobalVariables.ORDER_ID);
                String orderStatus = bundle.getString(CGlobalVariables.ORDER_STATUS);
                String rechargeAmount = bundle.getString(CGlobalVariables.RECHARGE_AMOUNT);
                String paymentMode = bundle.getString(CGlobalVariables.PAYMENT_MODE);
                String razorpayid = bundle.getString("razorpayid");
                //udatePaymentStatusOnserver(containerLayout, orderStatus, orderID, rechargeAmount, queue);
                if(orderStatus.equals("0")){
                    udatePaymentStatusOnserveronFailed(containerLayout, orderStatus, orderID, rechargeAmount, queue, paymentMode);
                }else {
                    udatePaymentStatusOnserver(this,containerLayout, orderStatus, orderID, rechargeAmount, queue, paymentMode,razorpayid);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wallet_layout:
            case R.id.my_walletll:
                CUtils.fcmAnalyticsEvents(CGlobalVariables.MY_ACCOUNT_WALLET_CLICK,
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                createSession(MyAccountActivity.this,CGlobalVariables.MY_ACCOUNT_WALLET_CLICK_PARTNER_ID);
                isUserLogin(new ReadFragment(), CGlobalVariables.RECHARGE_SCRREN);
                break;
            case R.id.my_profilell:
                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_MY_ACCOUNT,
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                Intent intent = new Intent(MyAccountActivity.this, ProfileActivity.class);
                startActivity(intent);
                break;
            case R.id.my_rechargell:
                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_MY_ACCOUNT,
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                Intent cunsultationIntent = new Intent(MyAccountActivity.this, ConsultantHistoryActivity.class);
                startActivity(cunsultationIntent);
                break;

            case R.id.my_language:
                CGlobalVariables.fromSetting=1;
//                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_MY_ACCOUNT,CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                Intent languageIntent = new Intent(MyAccountActivity.this, LanguageSelectionActivity.class);
                startActivity(languageIntent);
                break;

            case R.id.following_layout:
                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_FOLLING_ASTROLOGERS,
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                /*Intent followingIntent = new Intent(MyAccountActivity.this, FollowingAstrologerActivity.class);
                startActivity(followingIntent);*/
                if(followAstrologerModelArrayList.size()>0) {
                    Intent followingIntent = new Intent(MyAccountActivity.this, FollowingAstrologerActivity.class);
                    startActivity(followingIntent);
                } else {
                    showSnackbar(findViewById(android.R.id.content),getString(R.string.no_astrologer_followed_by_you),this);
                }
                break;

            case R.id.logout_btn:
                if(CUtils.isChatNotInitiated()){
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_LOGOUT,
                            CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                    onLogoutClicked();
                }else {
                    CUtils.showSnackbar(navView, getResources().getString(R.string.already_in_chat), MyAccountActivity.this);
                }
                break;
            case R.id.fabAccount:
                fabActions();
                break;
            case R.id.share:
                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_NAV_SHARE,
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                //CUtils.shareWithFriends(MyAccountActivity.this);
                com.ojassoft.astrosage.utils.CUtils.shareToFriendMail(this);
                break;
            case R.id.about_us:
                //Intent intentAboutUs = new Intent(MyAccountActivity.this, ActAboutUs.class);
                //MyAccountActivity.this.startActivity(intentAboutUs);
                com.ojassoft.astrosage.utils.CUtils.gotoAboutUsScreen(this, AstrosageKundliApplication.getAppContext().getLanguageCode());
                break;
            case R.id.join_varta_ll:
                Intent intentVartaReq = new Intent(MyAccountActivity.this, com.ojassoft.astrosage.ui.act.VartaReqJoinActivity.class);
                MyAccountActivity.this.startActivity(intentVartaReq);
                break;
            case R.id.my_notification_setting:
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                FragmentManager fm = getSupportFragmentManager();
                NotificationSettingFragment nsf = new NotificationSettingFragment();
                nsf.show(fm, "USER_NOTIFICATION_SETTING");
                ft.commit();
                break;
            case R.id.my_preferences:
                startActivity(new Intent(this, AstroPrefrenceActivity.class));
                break;
            case R.id.support_setting:{
                Intent supportIntent = new Intent(MyAccountActivity.this, GenerateTicketActivity.class);
                startActivity(supportIntent);
                break;
            }
        }
    }

    public void isUserLogin(Fragment fragment, String whichScreen) {
        boolean isLogin = false;
        isLogin = CUtils.getUserLoginStatus(MyAccountActivity.this);
        if (!isLogin) {
            Intent intent = new Intent(MyAccountActivity.this, LoginSignUpActivity.class);
            intent.putExtra(CGlobalVariables.IS_FROM_SCREEN, whichScreen);
            startActivity(intent);
        } else {
            if (whichScreen.equals(CGlobalVariables.RECHARGE_SCRREN)) {
                openWalletScreen();
            } else {
                //changeToolbar(CGlobalVariables.PROFILE_FRAGMENT);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_NAV_PROFILE,
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                //openFragment(fragment, "PROFILEFRAGMENT");
                Intent intent = new Intent(MyAccountActivity.this, MyAccountActivity.class);
                startActivity(intent);
            }
        }
    }

    private void getFollowingAstrologerDataFromServer(){
        if (!CUtils.isConnectedWithInternet(MyAccountActivity.this)) {
            CUtils.showSnackbar(navView, getResources().getString(R.string.no_internet), MyAccountActivity.this);
        } else {
//            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.GET_FOLLOWING_ASTRO_URL,
//                    MyAccountActivity.this, false, CUtils.getFollowingAstroParams(this), FETCH_FOLLOW_ASTROLOGER).getMyStringRequest();
//            queue.add(stringRequest);

            ApiList apiList = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> apiCall = apiList.getFollowedAstrologers(CUtils.getFollowingAstroParams(this));
            apiCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if(response.body()!=null){
                        try {
                            String responses = response.body().string();
                            try {
                                JSONObject jsonObject = new JSONObject(responses);
                                JSONObject result = jsonObject.getJSONObject("result");
                                String status = result.getString("status");
                                if(status.equals("1"))
                                {
                                    parseFollowingAstrologerList(responses);
                                }
                                else {
                                    my_followingCount.setText(getResources().getString(R.string.followingCountVal).replace("0",""+followAstrologerModelArrayList.size()));
                                }
//                    Log.e("SAN HF AStro res ", " Time=> " + System.currentTimeMillis() + " res=> " + response+" : "+status);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
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

    private void parseFollowingAstrologerList(String liveAstroData){
        if(TextUtils.isEmpty(liveAstroData)){
            return;
        }
        try {
            JSONObject jsonObject = new JSONObject(liveAstroData);
            JSONArray jsonArray = jsonObject.getJSONArray("astrologerslist");
            followAstrologerModelArrayList.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);

                FollowAstrologerModel followAstrologerModel = new FollowAstrologerModel();
                followAstrologerModel.setAstrologerName(object.getString("nickName"));
                followAstrologerModel.setAstrologerImage(object.getString("imageFile"));
                followAstrologerModel.setFollowingStatus(object.getString("followValue"));
                followAstrologerModel.setAstrologerId(object.getString("astrologerId"));
                followAstrologerModel.setUserId(object.getString("userId"));
                followAstrologerModelArrayList.add(followAstrologerModel);

            }

            if(followAstrologerModelArrayList.size()>0)
            {
                my_followingCount.setVisibility(View.VISIBLE);
                my_followingCount.setText(getResources().getString(R.string.followingCountVal).replace("0",""+followAstrologerModelArrayList.size()));
            }
        }catch (Exception e){
            followAstrologerModelArrayList.clear();
        }
    }

    @Override
    public void onResponse(String response, int method) {

        if (response != null && response.length() > 0) {

            if (method == FETCH_FOLLOW_ASTROLOGER) {
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    JSONObject result = jsonObject.getJSONObject("result");
//                    String status = result.getString("status");
//                    if(status.equals("1"))
//                    {
//                        parseFollowingAstrologerList(response);
//                    }
//                    else {
//                        my_followingCount.setText(getResources().getString(R.string.followingCountVal).replace("0",""+followAstrologerModelArrayList.size()));
//                    }
////                    Log.e("SAN HF AStro res ", " Time=> " + System.currentTimeMillis() + " res=> " + response+" : "+status);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
            }
        }
    }

    @Override
    public void onError(VolleyError error) {

    }

    private void openWalletScreen() {
        Intent intent = new Intent(MyAccountActivity.this, WalletActivity.class);
        intent.putExtra(CGlobalVariables.CALLING_ACTIVITY, "DashBoardActivity");
        startActivity(intent);
    }

    private void onLogoutClicked() {
        try {
            com.ojassoft.astrosage.utils.CUtils.logoutFromApp(this);

            Intent intent = new Intent(MyAccountActivity.this, LoginSignUpActivity.class);
            //intent.putExtra(CGlobalVariables.IS_FROM_SCREEN, CGlobalVariables.LOGOUT_BTN);
            intent.putExtra(IS_GOOGLE_FACEBOOK_VISIBLE, true);
            startActivity(intent);
            finish();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void fabActions(){
        try {
            //boolean liveStreamingEnabledForAstrosage = CUtils.getBooleanData(this, CGlobalVariables.liveStreamingEnabledForAstrosage, false);
            boolean liveStreamingEnabledForAstrosage = true;
            if(!liveStreamingEnabledForAstrosage){ //fetch data according to tagmanag
                CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_MY_ACCOUNT_BOTTOM_KUNDLI, FIREBASE_EVENT_ITEM_CLICK, "");
                createSession(MyAccountActivity.this, MY_ACCOUNT_BOTTOM_BAR_KUNDLI_PARTNER_ID);
                openKundliActivity(this);
            }
            else {
                CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_MY_ACCOUNT_BOTTOM_LIVE, FIREBASE_EVENT_ITEM_CLICK, "");
                createSession(MyAccountActivity.this, MY_ACCOUNT_BOTTOM_BAR_LIVE_PARTNER_ID);
                gotoAllLiveActivityFromLiveIcon(this);
            }
        }catch (Exception e){
            //
        }
    }

    @Override
    public void clickCall() {
        CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_MY_ACCOUNT_BOTTOM_CALL, FIREBASE_EVENT_ITEM_CLICK, "");
        createSession(MyAccountActivity.this, MY_ACCOUNT_BOTTOM_BAR_CALL_PARTNER_ID);
        super.clickCall();
    }

    @Override
    public void clickChat() {
        CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_MY_ACCOUNT_BOTTOM_CHAT, FIREBASE_EVENT_ITEM_CLICK, "");
        createSession(MyAccountActivity.this, MY_ACCOUNT_BOTTOM_BAR_CHAT_PARTNER_ID);
        super.clickChat();
    }

    @Override
    public void clickLive() {
        fabActions();
    }
    @Override
    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response, int requestCode) {
       //
    }

    @Override
    public void onFailure(Call<ResponseBody> call, Throwable t) {
        //
    }
}
