package com.ojassoft.astrosage.varta.ui.activity;

import static com.ojassoft.astrosage.utils.CGlobalVariables.DETAILED_HOROSCOPE_BOTTOM_BAR_CALL_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.DETAILED_HOROSCOPE_BOTTOM_BAR_CHAT_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK;
import static com.ojassoft.astrosage.utils.CGlobalVariables.FOLLOWING_BOTTOM_BAR_CALL_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.FOLLOWING_BOTTOM_BAR_CHAT_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.FOLLOWING_BOTTOM_BAR_KUNDLI_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.FOLLOWING_BOTTOM_BAR_LIVE_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_DETAILED_HOROSCOPE_BOTTOM_CALL;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_DETAILED_HOROSCOPE_BOTTOM_CHAT;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_FOLLOWING_BOTTOM_CALL;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_FOLLOWING_BOTTOM_CHAT;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_FOLLOWING_BOTTOM_KUNDLI;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_FOLLOWING_BOTTOM_LIVE;
import static com.ojassoft.astrosage.utils.CUtils.createSession;
import static com.ojassoft.astrosage.utils.CUtils.openKundliActivity;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.FILTER_TYPE_CALL;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.FILTER_TYPE_CHAT;
import static com.ojassoft.astrosage.varta.utils.CUtils.followAstrologerModelArrayList;
import static com.ojassoft.astrosage.varta.utils.CUtils.gotoAllLiveActivityFromLiveIcon;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.act.horoscope.DetailedHoroscope;
import com.ojassoft.astrosage.varta.adapters.FollowingAstrologerAdapter;

import com.ojassoft.astrosage.varta.model.FollowAstrologerModel;
import com.ojassoft.astrosage.varta.service.Loginservice;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.CircularNetworkImageView;
import com.ojassoft.astrosage.varta.utils.CustomProgressDialog;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;
import com.ojassoft.astrosage.varta.volley_network.VolleyServiceHandler;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FollowingAstrologerActivity extends BaseActivity implements View.OnClickListener, VolleyResponse {

    private RecyclerView followAstroRecyclerView;
    private FollowingAstrologerAdapter followAstrologerAdapter;
    RequestQueue queue;
    ImageView backIV;
    FloatingActionButton fabFollowing;
    TextView titleTV;
    TextView walletPriceTxt;
    LinearLayout walletLayout;
    RelativeLayout walletBoxLayout;
    private LinearLayout mUnFollowLayout;
    private final int FOLLOW_ASTROLOGER_REQ = 6;
    public static int currentPosition=0;
    CustomProgressDialog pd;
    LinearLayout navView;

    private final BroadcastReceiver mReceiverBackgroundLoginService = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            String status = intent.getStringExtra("status");
            if (status.equals(CGlobalVariables.SUCCESS)) {
                followAstrologerRequest("0",followAstrologerModelArrayList.get(currentPosition));
            }

            if (mReceiverBackgroundLoginService != null) {
                LocalBroadcastManager.getInstance(FollowingAstrologerActivity.this).unregisterReceiver(mReceiverBackgroundLoginService);
            }
        }
    };

    /*BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener = new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            CUtils.openAstroSageHomeActivity(FollowingAstrologerActivity.this);
                            return true;
                        case R.id.navigation_recharge:
                            CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_FOLLOWING_BOTTOM_CALL, FIREBASE_EVENT_ITEM_CLICK, "");
                            createSession(FollowingAstrologerActivity.this, FOLLOWING_BOTTOM_BAR_CALL_PARTNER_ID);
                            switchToConsultTab(FILTER_TYPE_CALL, FollowingAstrologerActivity.this);
                            return true;
                        case R.id.navigation_share:
                            fabActions();
                            //CUtils.sendFeedbackActivity(FollowingAstrologerActivity.this);
                            //openFragment(new NotificationsFragment());
                            return true;
                        case R.id.navigation_notification:
                            CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_FOLLOWING_BOTTOM_CHAT, FIREBASE_EVENT_ITEM_CLICK, "");
                            createSession(FollowingAstrologerActivity.this, FOLLOWING_BOTTOM_BAR_CHAT_PARTNER_ID);
                            switchToConsultTab(FILTER_TYPE_CHAT, FollowingAstrologerActivity.this);
                            return true;
                        case R.id.navigation_profile:
                            //Intent intent1 = new Intent(FollowingAstrologerActivity.this, MyAccountActivity.class);
//                            startActivity(intent1);
                            return true;
                    }
                    return false;
                }
            };*/

    public void isUserLogin(Fragment fragment, String whichScreen) {
        boolean isLogin = false;
        isLogin = CUtils.getUserLoginStatus(FollowingAstrologerActivity.this);
        if (!isLogin) {
            CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_NAV_SIGNUP,
                    CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
            Intent intent = new Intent(FollowingAstrologerActivity.this, LoginSignUpActivity.class);
            intent.putExtra(CGlobalVariables.IS_FROM_SCREEN, whichScreen);
            startActivity(intent);
        } else {
            if (whichScreen.equals(CGlobalVariables.RECHARGE_SCRREN)) {
                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_NAV_RECHARGE,
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                openWalletScreen();
            } else {
                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_NAV_PROFILE,
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                Intent intent = new Intent(FollowingAstrologerActivity.this, MyAccountActivity.class);
                startActivity(intent);
            }
        }
    }

    public void openWalletScreen() {
        CUtils.fcmAnalyticsEvents(CGlobalVariables.FOLLOWING_ASTROLOGERS_WALLET_CLICK,
                CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
        createSession(FollowingAstrologerActivity.this,CGlobalVariables.FOLLOWING_ASTROLOGERS_WALLET_CLICK_PARTNER_ID);
        Intent intent = new Intent(FollowingAstrologerActivity.this, WalletActivity.class);
        intent.putExtra(CGlobalVariables.CALLING_ACTIVITY, "DashBoardActivity");
        startActivity(intent);
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
                FontUtils.changeFont(FollowingAstrologerActivity.this, ((TextView) v), CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
                ((TextView) v).setTextSize(11);
                ((TextView) v).setTextColor(getResources().getColor(R.color.black));
                // added by vishal
                ((TextView) v).setEllipsize(TextUtils.TruncateAt.END);
                ((TextView) v).setPadding(0, 0, 0, 0);
            }
        } catch (Exception e) { }
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
        com.ojassoft.astrosage.utils.CUtils.handleFabOnActivities(this,fabFollowing,navLive);

        navHisTxt.setText(getResources().getString(R.string.history));
        navHisImg.setImageResource(R.drawable.nav_more_icons);
        //navView.getMenu().setGroupCheckable(0,false,true);
        navView.findViewById(R.id.bottom_nav_home).setOnClickListener(navbarItemSelectedListener);
        navView.findViewById(R.id.bottom_nav_call).setOnClickListener(navbarItemSelectedListener);
        navView.findViewById(R.id.bottom_nav_live).setOnClickListener(navbarItemSelectedListener);
        navView.findViewById(R.id.bottom_nav_chat).setOnClickListener(navbarItemSelectedListener);
        navView.findViewById(R.id.bottom_nav_history).setOnClickListener(navbarItemSelectedListener);
    }

    private void setToolbarWallet(){
        walletLayout = findViewById(R.id.wallet_layout);
        walletPriceTxt = findViewById(R.id.wallet_price_txt);
        walletBoxLayout = findViewById(R.id.wallet_box_layout);
        walletBoxLayout.setVisibility(View.VISIBLE);
        walletLayout.setVisibility(View.VISIBLE);
        walletPriceTxt.setText(getResources().getString(R.string.astroshop_rupees_sign) + CUtils.convertAmtIntoIndianFormat( CUtils.getWalletRs(FollowingAstrologerActivity.this) ));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_following_astrologer);
        setToolbarWallet();

        backIV = findViewById(R.id.ivBack);
        fabFollowing = findViewById(R.id.fabFollowing);
        titleTV = findViewById(R.id.tvTitle);
        titleTV.setText(getResources().getString(R.string.following));
        navView = findViewById(R.id.nav_view);
        backIV.setOnClickListener(this);
        //navView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        customBottomNavigationFont(navView);
        setBottomNavigationText();
        queue = VolleySingleton.getInstance(FollowingAstrologerActivity.this).getRequestQueue();

        followAstroRecyclerView = (RecyclerView)findViewById(R.id.followAstroRecyclerView);
        /*followAstroRecyclerView.setItemAnimator(null);
        followAstrologerAdapter = new FollowingAstrologerAdapter(FollowingAstrologerActivity.this, followAstrologerModelArrayList);
        followAstroRecyclerView.setLayoutManager(new LinearLayoutManager(FollowingAstrologerActivity.this));
        followAstroRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                followAstroRecyclerView, new ClickListener() {
            @Override
            public void onClick(View view, final int position) {
                currentPosition=position;
                openUnFollowDialog(followAstrologerModelArrayList.get(position));
            }

            @Override
            public void onLongClick(View view, int position) {
            }
        }));*/

        fabFollowing.setOnClickListener(v->{
            fabActions();
        });

        walletLayout.setOnClickListener(v->{
            openWalletScreen();
        });

        showProgressBar();
        setFollowingAdapter();

        //followAstroRecyclerView.setAdapter(followAstrologerAdapter);

        mUnFollowLayout = findViewById(R.id.layout_live_unfollow);
        mUnFollowLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mUnFollowLayout.getVisibility() == View.VISIBLE) {
                    close_layout(mUnFollowLayout);
                }
            }
        });

    }

    public void openUnFollowDialog(FollowAstrologerModel followAstrologerModel) {
        initAstrologerUnFollowDialog(followAstrologerModel);
        if (mUnFollowLayout.getVisibility() == View.VISIBLE) {
            close_layout(mUnFollowLayout);
        }
        else 
        {
            open_layout(mUnFollowLayout);
        }
    }

    private void initAstrologerUnFollowDialog(FollowAstrologerModel followAstrologerModel) {

        TextView live_follow_textTv = mUnFollowLayout.findViewById(R.id.live_login_text);
        FontUtils.changeFont(FollowingAstrologerActivity.this, live_follow_textTv, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);

        TextView live_astrologer_name_follow = mUnFollowLayout.findViewById(R.id.live_astrologer_name_follow);
        FontUtils.changeFont(FollowingAstrologerActivity.this, live_astrologer_name_follow, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        live_astrologer_name_follow.setText(followAstrologerModel.getAstrologerName());

        CircularNetworkImageView live_astrologer_image_follow = findViewById(R.id.live_astrologer_image_unfollow);
        String astrologerProfileUrl = "";
        if (followAstrologerModel.getAstrologerImage() != null && followAstrologerModel.getAstrologerImage().length() > 0) {
            astrologerProfileUrl = CGlobalVariables.IMAGE_DOMAIN + followAstrologerModel.getAstrologerImage();
           // live_astrologer_image_follow.setImageUrl(astrologerProfileUrl, VolleySingleton.getInstance(FollowingAstrologerActivity.this).getImageLoader());
            Glide.with(getApplicationContext()).load(astrologerProfileUrl).circleCrop().placeholder(R.drawable.ic_profile_view).into(live_astrologer_image_follow);

        }


        Button live_follow_ok_btn = mUnFollowLayout.findViewById(R.id.live_follow_ok_btn);
        FontUtils.changeFont(FollowingAstrologerActivity.this, live_follow_ok_btn, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);

        live_follow_ok_btn.setOnClickListener(view -> {
            close_layout(mUnFollowLayout);
            if (CUtils.getUserLoginStatus(FollowingAstrologerActivity.this)) {
                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_LIVE_UNFOLLOW_ASTROLOGER_BTN_CLICK,
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                followAstrologerRequest("0",followAstrologerModel);
            }
        });

    }

    public void followAstrologerRequest(String followVal, FollowAstrologerModel followAstrologerModel) {
        if (CUtils.isConnectedWithInternet(FollowingAstrologerActivity.this)) {
            if (pd == null)
                pd = new CustomProgressDialog(FollowingAstrologerActivity.this);
            pd.show();
            pd.setCancelable(false);

//            String url = CGlobalVariables.FOLLOW_ASTROLOGER_URL;
//            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, url,
//                    FollowingAstrologerActivity.this, false, getfollowAstrologerParams(followVal,followAstrologerModel), FOLLOW_ASTROLOGER_REQ).getMyStringRequest();
//            stringRequest.setShouldCache(true);
//            if (queue == null)
//                queue = VolleySingleton.getInstance(FollowingAstrologerActivity.this).getRequestQueue();

           // queue.add(stringRequest);


            ApiList apiList = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> apiCall = apiList.newFollowAstrologer(getfollowAstrologerParams(followVal,followAstrologerModel));
            apiCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        hideProgressBar();
                        String responses = response.body().string();
                        JSONObject jsonObject = new JSONObject(responses);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("msg");
                        if (status.equals("1")) {
                            if (followAstrologerModelArrayList != null && currentPosition < followAstrologerModelArrayList.size())
                                CUtils.unSubscribeFollowTopic(FollowingAstrologerActivity.this, followAstrologerModelArrayList.get(currentPosition).getAstrologerId());
                            followAstrologerModelArrayList.remove(currentPosition);
                            followAstrologerAdapter.notifyDataSetChanged();
                            if (followAstrologerModelArrayList.size() == 0) {
                                finish();
                            }
                        } else if (status.equals("100")){
                            LocalBroadcastManager.getInstance(FollowingAstrologerActivity.this).registerReceiver(mReceiverBackgroundLoginService, new IntentFilter(CGlobalVariables.SEND_BROADCAST_BACK_GR_LOGIN));
                            startBackgroundLoginService();
                        } else {
                            CUtils.showSnackbar(followAstroRecyclerView, message, FollowingAstrologerActivity.this);
                        }
                    }catch (Exception e){

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    hideProgressBar();
                }
            });
        } else {
            //CUtils.showSnackbar(recyclerViewLiveStream, getResources().getString(R.string.no_internet), FollowingAstrologerActivity.this);
            Toast.makeText(getApplicationContext(), getResources().getString(R.string.no_internet), Toast.LENGTH_LONG).show();
        }
    }

    public Map<String, String> getfollowAstrologerParams(String followVal,FollowAstrologerModel followAstrologerModel) {
        HashMap<String, String> headers = new HashMap<String, String>();

        headers.put(CGlobalVariables.KEY_API, CUtils.getApplicationSignatureHashCode(FollowingAstrologerActivity.this));
        headers.put(CGlobalVariables.ASTROLOGER_ID, followAstrologerModel.getAstrologerId());
        headers.put(CGlobalVariables.KEY_USER_ID, CUtils.getUserIdForBlock(FollowingAstrologerActivity.this));
        headers.put(CGlobalVariables.KEY_FOLLOW, followVal);
        headers.put(CGlobalVariables.ACTIONSOURCE, CUtils.getActivityName(FollowingAstrologerActivity.this));

        headers.put(CGlobalVariables.KEY_IAPI, "1");
        headers.put(CGlobalVariables.DEVICE_ID, CUtils.getMyAndroidId(FollowingAstrologerActivity.this));
        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(FollowingAstrologerActivity.this));
        headers.put(CGlobalVariables.APP_VERSION, "" + BuildConfig.VERSION_NAME);
        return headers;
    }

    public void open_layout(View linearLayout) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(linearLayout, "translationY", linearLayout.getHeight(), 0);
        animator.setDuration(200);
        animator.addListener(new Animator.AnimatorListener() {

            @Override
            public void onAnimationStart(Animator animation) {
                linearLayout.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
            }

            @Override
            public void onAnimationEnd(Animator animation) {
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }
        });
        animator.start();
    }

    public void close_layout(View linearLayout) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(linearLayout, "translationY", 0, linearLayout.getHeight());
        animator.setDuration(200L);
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {

            }

            @Override
            public void onAnimationEnd(Animator animator) {
                linearLayout.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationCancel(Animator animator) {
                linearLayout.setVisibility(View.GONE);

            }

            @Override
            public void onAnimationRepeat(Animator animator) {

            }
        });
        animator.start();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    public void showProgressBar() {
        try {
            if (pd == null) {
                pd = new CustomProgressDialog(FollowingAstrologerActivity.this);
                pd.setCancelable(false);
            }
            pd.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideProgressBar() {
        try {
            if (pd != null & pd.isShowing())
                pd.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResponse(String response, int method) {
        hideProgressBar();
        if (response != null && response.length() > 0) {

            try {
                if (method == 42){
                    JSONObject jsonObject = new JSONObject(response);
                    JSONObject result = jsonObject.getJSONObject("result");
                    String status = result.getString("status");
                    if(status.equals("1")) {
                        parseFollowingAstrologerList(response);
                    }
                } else {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("msg");
                    if (status.equals("1")) {
                        if (followAstrologerModelArrayList != null && currentPosition < followAstrologerModelArrayList.size())
                            CUtils.unSubscribeFollowTopic(FollowingAstrologerActivity.this, followAstrologerModelArrayList.get(currentPosition).getAstrologerId());
                        followAstrologerModelArrayList.remove(currentPosition);
                        followAstrologerAdapter.notifyDataSetChanged();
                        if (followAstrologerModelArrayList.size() == 0) {
                            finish();
                        }
                    } else if (status.equals("100")){
                        LocalBroadcastManager.getInstance(FollowingAstrologerActivity.this).registerReceiver(mReceiverBackgroundLoginService, new IntentFilter(CGlobalVariables.SEND_BROADCAST_BACK_GR_LOGIN));
                        startBackgroundLoginService();
                    } else {
                        CUtils.showSnackbar(followAstroRecyclerView, message, FollowingAstrologerActivity.this);
                    }
                }

            } catch (Exception e) { }

        }
    }

    @Override
    public void onError(VolleyError error) {

    }

    private void startBackgroundLoginService() {
        try {
            if (CUtils.getUserLoginStatus(FollowingAstrologerActivity.this)) {
                Intent intent = new Intent(FollowingAstrologerActivity.this, Loginservice.class);
                startService(intent);
            }
        } catch (Exception e) {
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.ivBack:
                finish();
                break;
        }
    }

    public static interface ClickListener{
        public void onClick(View view,int position);
        public void onLongClick(View view,int position);
    }

    class RecyclerTouchListener implements RecyclerView.OnItemTouchListener{

        private ClickListener clicklistener;
        private GestureDetector gestureDetector;

        public RecyclerTouchListener(Context context, final RecyclerView recycleView, final ClickListener clicklistener){

            this.clicklistener=clicklistener;
            gestureDetector=new GestureDetector(context,new GestureDetector.SimpleOnGestureListener(){
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child=recycleView.findChildViewUnder(e.getX(),e.getY());
                    if(child!=null && clicklistener!=null){
                        clicklistener.onLongClick(child,recycleView.getChildAdapterPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {
            View child=rv.findChildViewUnder(e.getX(),e.getY());
            if(child!=null && clicklistener!=null && gestureDetector.onTouchEvent(e)){
                clicklistener.onClick(child,rv.getChildAdapterPosition(child));
            }

            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {

        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

        }
    }

    @Override
    public void onBackPressed() {
        if (mUnFollowLayout.getVisibility() == View.VISIBLE) {
            close_layout(mUnFollowLayout);
        }else
        {
            super.onBackPressed();
        }
    }

    private void fabActions(){
        try {
            //boolean liveStreamingEnabledForAstrosage = CUtils.getBooleanData(this, CGlobalVariables.liveStreamingEnabledForAstrosage, false);
            boolean liveStreamingEnabledForAstrosage = true;
            if(!liveStreamingEnabledForAstrosage){ //fetch data according to tagmanag
                CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_FOLLOWING_BOTTOM_KUNDLI, FIREBASE_EVENT_ITEM_CLICK, "");
                createSession(this, FOLLOWING_BOTTOM_BAR_KUNDLI_PARTNER_ID);
                openKundliActivity(this);
            }
            else {
                CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_FOLLOWING_BOTTOM_LIVE, FIREBASE_EVENT_ITEM_CLICK, "");
                createSession(this, FOLLOWING_BOTTOM_BAR_LIVE_PARTNER_ID);
                gotoAllLiveActivityFromLiveIcon(this);
            }
        }catch (Exception e){
            //
        }
    }

    private void parseFollowingAstrologerList(String liveAstroData){
        if(TextUtils.isEmpty(liveAstroData)){
            return;
        }
        try {
            if (followAstrologerModelArrayList == null) followAstrologerModelArrayList = new ArrayList<>();
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
            setFollowingAdapter();
        }catch (Exception e){
            followAstrologerModelArrayList.clear();
        }
    }

    private void setFollowingAdapter(){
        if (followAstrologerModelArrayList != null && followAstrologerModelArrayList.size() > 0) {
            followAstroRecyclerView.setItemAnimator(null);
            followAstrologerAdapter = new FollowingAstrologerAdapter(FollowingAstrologerActivity.this, followAstrologerModelArrayList);
            followAstroRecyclerView.setLayoutManager(new LinearLayoutManager(FollowingAstrologerActivity.this));
            followAstroRecyclerView.addOnItemTouchListener(new RecyclerTouchListener(this,
                    followAstroRecyclerView, new ClickListener() {
                @Override
                public void onClick(View view, final int position) {
                    currentPosition = position;
                    openUnFollowDialog(followAstrologerModelArrayList.get(position));
                }

                @Override
                public void onLongClick(View view, int position) {
                }
            }));
            followAstroRecyclerView.setAdapter(followAstrologerAdapter);
            hideProgressBar();
        } else {
            getFollowingAstrologerDataFromServer();
        }
    }

    private void getFollowingAstrologerDataFromServer(){
        showProgressBar();
        if (!CUtils.isConnectedWithInternet(FollowingAstrologerActivity.this)) {
            CUtils.showSnackbar(navView, getResources().getString(R.string.no_internet), FollowingAstrologerActivity.this);
        } else {
//            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.GET_FOLLOWING_ASTRO_URL,
//                    FollowingAstrologerActivity.this, false, CUtils.getFollowingAstroParams(this), 42).getMyStringRequest();
//            queue.add(stringRequest);
            ApiList apiList = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> apiCall = apiList.getFollowedAstrologers(CUtils.getFollowingAstroParams(this));
            apiCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        hideProgressBar();
                        String responses = response.body().string();
                        JSONObject jsonObject = new JSONObject(responses);
                        JSONObject result = jsonObject.getJSONObject("result");
                        String status = result.getString("status");
                        if(status.equals("1")) {
                            parseFollowingAstrologerList(responses);
                        }
                    }catch (Exception e){

                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    hideProgressBar();
                }
            });

        }
    }

    @Override
    public void clickCall() {
        CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_FOLLOWING_BOTTOM_CALL, FIREBASE_EVENT_ITEM_CLICK, "");
        createSession(FollowingAstrologerActivity.this, FOLLOWING_BOTTOM_BAR_CALL_PARTNER_ID);
        super.clickCall();
    }

    @Override
    public void clickChat() {
        CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_FOLLOWING_BOTTOM_CHAT, FIREBASE_EVENT_ITEM_CLICK, "");
        createSession(FollowingAstrologerActivity.this, FOLLOWING_BOTTOM_BAR_CHAT_PARTNER_ID);
        super.clickChat();
    }

    @Override
    public void clickLive() {
        fabActions();
    }

}