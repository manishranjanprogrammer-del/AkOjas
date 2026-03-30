package com.ojassoft.astrosage.varta.ui.activity;

import static com.ojassoft.astrosage.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_MY_ACCOUNT_BOTTOM_CALL;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_MY_ACCOUNT_BOTTOM_CHAT;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_MY_ACCOUNT_BOTTOM_KUNDLI;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_MY_ACCOUNT_BOTTOM_LIVE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_PROFILE_BOTTOM_CALL;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_PROFILE_BOTTOM_CHAT;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_PROFILE_BOTTOM_KUNDLI;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_PROFILE_BOTTOM_LIVE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.MY_ACCOUNT_BOTTOM_BAR_CALL_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.PROFILE_BOTTOM_BAR_CALL_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.PROFILE_BOTTOM_BAR_CHAT_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.PROFILE_BOTTOM_BAR_KUNDLI_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.PROFILE_BOTTOM_BAR_LIVE_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CUtils.createSession;
import static com.ojassoft.astrosage.utils.CUtils.openKundliActivity;
import static com.ojassoft.astrosage.varta.utils.CUtils.gotoAllLiveActivityFromLiveIcon;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.android.volley.RequestQueue;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ojassoft.astrosage.R;

import com.ojassoft.astrosage.networkcall.GlobalRetrofitResponse;
import com.ojassoft.astrosage.ui.act.ActAppModule;
import com.ojassoft.astrosage.ui.act.ActAstroShopCategories;
import com.ojassoft.astrosage.varta.interfacefile.IBirthDetailInputFragment;
import com.ojassoft.astrosage.varta.model.BeanDateTime;
import com.ojassoft.astrosage.varta.model.BeanPlace;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

public class ProfileActivity extends BaseActivity implements IBirthDetailInputFragment, GlobalRetrofitResponse {
    TextView titleTV;
    ImageView backIV;
    FloatingActionButton fabProfile;
    LinearLayout navView;
    RelativeLayout containerLayout;
    RequestQueue queue;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_layout);
        containerLayout=findViewById(R.id.container);
        titleTV = findViewById(R.id.tvTitle);
        backIV = findViewById(R.id.ivBack);
        fabProfile = findViewById(R.id.fabProfile);
        titleTV.setText(getResources().getString(R.string.title_profile));
        FontUtils.changeFont(this, titleTV, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        backIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        fabProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabActions();
            }
        });
        navView = findViewById(R.id.nav_view);
        //navView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        customBottomNavigationFont(navView);
        setBottomNavigationText(CUtils.getUserLoginStatus(this));
        //navView.setSelectedItemId(R.id.navigation_profile);
        //navView.getMenu().setGroupCheckable(0,false,true);
        queue = VolleySingleton.getInstance(this).getRequestQueue();

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * Bottom Navigation View
     */
    /*
    BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            Intent intent = new Intent(ProfileActivity.this, ActAppModule.class);
                            startActivity(intent);
                            return true;
                        case R.id.navigation_recharge:
                            CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_PROFILE_BOTTOM_CALL, FIREBASE_EVENT_ITEM_CLICK, "");
                            createSession(ProfileActivity.this, PROFILE_BOTTOM_BAR_CALL_PARTNER_ID);
                            switchToConsultTab(FILTER_TYPE_CALL, ProfileActivity.this);
                            return true;
                        case R.id.navigation_share:
                            fabActions();
                            return true;
                        case R.id.navigation_notification:
                            CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_PROFILE_BOTTOM_CHAT, FIREBASE_EVENT_ITEM_CLICK, "");
                            createSession(ProfileActivity.this, PROFILE_BOTTOM_BAR_CHAT_PARTNER_ID);
                            switchToConsultTab(FILTER_TYPE_CHAT, ProfileActivity.this);
                            return true;
                        case R.id.navigation_profile:
                            Intent cunsultationIntent = new Intent(ProfileActivity.this, ConsultantHistoryActivity.class);
                            cunsultationIntent.putExtra(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CALLING_ACTIVITY, "ProfileActivity");
                            startActivity(cunsultationIntent);
                            return true;
                    }
                    return false;
                }
            };
*/
    public void customBottomNavigationFont(final View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    customBottomNavigationFont(child);
                }
            } else if (v instanceof TextView) {
                FontUtils.changeFont(ProfileActivity.this, ((TextView) v), CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
                ((TextView) v).setTextSize(11);
                ((TextView) v).setTextColor(getResources().getColor(R.color.black));
                // added by vishal
                ((TextView) v).setEllipsize(TextUtils.TruncateAt.END);
                ((TextView) v).setPadding(0, 0, 0, 0);
            }
        } catch (Exception e) {
        }
    }

    /*private void setBottomNavigationText() {
        // get menu from navigationView
        Menu menu = navView.getMenu();
        // find MenuItem you want to change
        MenuItem navHome = menu.findItem(R.id.bottom_nav_home);
        MenuItem navRead = menu.findItem(R.id.bottom_nav_call);
        MenuItem navNotificaton = menu.findItem(R.id.bottom_nav_live);
        MenuItem navMyaccount = menu.findItem(R.id.bottom_nav_history);

        // set new title to the MenuItem
        //navHome.setTitle(getResources().getString(R.string.title_home));
        com.ojassoft.astrosage.utils.CUtils.handleFabOnActivities(this,fabProfile,navNotificaton);

        navMyaccount.setTitle(getResources().getString(R.string.account));
    }*/

    private void setBottomNavigationText(boolean isLoginData) {

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
        com.ojassoft.astrosage.utils.CUtils.handleFabOnActivities(this,fabProfile,navLive);

  //      navHisTxt.setText(getResources().getString(R.string.sign_up));
  //      navHisImg.setImageResource(R.drawable.nav_profile_icons);
        if (isLoginData) {
            navHisTxt.setText(getResources().getString(R.string.history));
            navHisImg.setImageResource(R.drawable.nav_more_icons);
        } else {
            navHisTxt.setText(getResources().getString(R.string.sign_up));
            navHisImg.setImageResource(R.drawable.nav_profile_icons);
        }
        //navView.getMenu().setGroupCheckable(0,false,true);
        navView.findViewById(R.id.bottom_nav_home).setOnClickListener(navbarItemSelectedListener);
        navView.findViewById(R.id.bottom_nav_call).setOnClickListener(navbarItemSelectedListener);
        navView.findViewById(R.id.bottom_nav_live).setOnClickListener(navbarItemSelectedListener);
        navView.findViewById(R.id.bottom_nav_chat).setOnClickListener(navbarItemSelectedListener);
        navView.findViewById(R.id.bottom_nav_history).setOnClickListener(navbarItemSelectedListener);
    }

    @Override
    public void openCalendar(BeanDateTime beanDateTime) {

    }

    @Override
    public void openTimePicker(BeanDateTime beanDateTime) {

    }

    @Override
    public void openSearchPlace(BeanPlace b) {

    }

    @Override
    public void birthDetailInputFragmentCreated() {

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
    public void fabActions(){
        try {
            //boolean liveStreamingEnabledForAstrosage = CUtils.getBooleanData(this, CGlobalVariables.liveStreamingEnabledForAstrosage, false);
            boolean liveStreamingEnabledForAstrosage = true;
            if(!liveStreamingEnabledForAstrosage){ //fetch data according to tagmanag
                CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_PROFILE_BOTTOM_KUNDLI, FIREBASE_EVENT_ITEM_CLICK, "");
                createSession(ProfileActivity.this, PROFILE_BOTTOM_BAR_KUNDLI_PARTNER_ID);
                openKundliActivity(this);
            }
            else {
                CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_PROFILE_BOTTOM_LIVE, FIREBASE_EVENT_ITEM_CLICK, "");
                createSession(ProfileActivity.this, PROFILE_BOTTOM_BAR_LIVE_PARTNER_ID);
                gotoAllLiveActivityFromLiveIcon(this);
            }
        }catch (Exception e){
            //
        }
    }

    @Override
    public void clickCall() {
        CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_PROFILE_BOTTOM_CALL, FIREBASE_EVENT_ITEM_CLICK, "");
        createSession(ProfileActivity.this, PROFILE_BOTTOM_BAR_CALL_PARTNER_ID);
        super.clickCall();
    }

    @Override
    public void clickChat() {
        CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_PROFILE_BOTTOM_CHAT, FIREBASE_EVENT_ITEM_CLICK, "");
        createSession(ProfileActivity.this, PROFILE_BOTTOM_BAR_CHAT_PARTNER_ID);
        super.clickChat();
    }

    @Override
    public void clickLive() {
        fabActions();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.e("SAN ", " PA Distroyed ");
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
