package com.ojassoft.astrosage.varta.ui.activity;

import static com.ojassoft.astrosage.utils.CGlobalVariables.ALL_LIVE_BOTTOM_BAR_CALL_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.ALL_LIVE_BOTTOM_BAR_CHAT_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.ALL_LIVE_BOTTOM_BAR_LIVE_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_ALL_LIVE_BOTTOM_CALL;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_ALL_LIVE_BOTTOM_CHAT;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_ALL_LIVE_BOTTOM_LIVE;
import static com.ojassoft.astrosage.utils.CUtils.createSession;

import android.Manifest;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.act.ActAppModule;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.varta.adapters.ViewPagerLiveAdapter;
import com.ojassoft.astrosage.varta.model.LiveAstrologerModel;
import com.ojassoft.astrosage.varta.ui.fragments.AllLiveAstroFragment;
import com.ojassoft.astrosage.varta.ui.fragments.SchedulesLiveFragment;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.CustomProgressDialog;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AllLiveAstrologerActivity extends BaseActivity implements View.OnClickListener, VolleyResponse {


    private RecyclerView liveAstroRecyclerView;
    private ArrayList<LiveAstrologerModel> liveAstrologerModelArrayList;
    //private LiveAstrologerAdapter liveAstrologerAdapter;
    private int FETCH_LIVE_ASTROLOGER = 4;
    RequestQueue queue;
    ImageView backIV;
   // FloatingActionButton fabAllLive;
    TextView titleTV;
    SwipeRefreshLayout mSwipeRefreshLayout;
    CustomProgressDialog pd;
    LinearLayout navView;
    private BroadcastReceiver liveAstroReceiver;
    private static final int PERMISSION_REQ_CODE = 1;
    Dialog emptyListDialog;
    private String astroProfileUrl;
    private String[] PERMISSIONS = {
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA
    };
    private boolean spacingSet = false;
    ViewPager pager;
    ViewPagerLiveAdapter adapter;
    TabLayout tablayout;
    /*BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.navigation_home:
                            CUtils.openAstroSageHomeActivity(AllLiveAstrologerActivity.this);
                            return true;
                        case R.id.navigation_recharge:
                            CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_ALL_LIVE_BOTTOM_CALL, FIREBASE_EVENT_ITEM_CLICK, "");
                            createSession(AllLiveAstrologerActivity.this, ALL_LIVE_BOTTOM_BAR_CALL_PARTNER_ID);
                            switchToConsultTab(FILTER_TYPE_CALL, AllLiveAstrologerActivity.this);
                            return true;
                        case R.id.navigation_share:
                            joinLive();
                            return true;
                        case R.id.navigation_notification:
                            CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_ALL_LIVE_BOTTOM_CHAT, FIREBASE_EVENT_ITEM_CLICK, "");
                            createSession(AllLiveAstrologerActivity.this, ALL_LIVE_BOTTOM_BAR_CHAT_PARTNER_ID);
                            switchToConsultTab(FILTER_TYPE_CHAT,AllLiveAstrologerActivity.this);
                            return true;
                        case R.id.navigation_profile:
                            isUserLogin(new ProfileFragment(), CGlobalVariables.PROFILE_SCRREN);
                            return true;
                    }
                    return false;
                }
            };*/

    private void joinLive(){
        if(liveAstrologerModelArrayList != null && !liveAstrologerModelArrayList.isEmpty()){
            CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_ALL_LIVE_BOTTOM_LIVE, FIREBASE_EVENT_ITEM_CLICK, "");
            createSession(AllLiveAstrologerActivity.this, ALL_LIVE_BOTTOM_BAR_LIVE_PARTNER_ID);
            checkPermissions(liveAstrologerModelArrayList.get(0));
        } else {
            finish();
            startActivity(new Intent(AllLiveAstrologerActivity.this, AllLiveAstrologerActivity.class));
        }
    }

    public void isUserLogin(Fragment fragment, String whichScreen) {
        boolean isLogin = false;
        isLogin = CUtils.getUserLoginStatus(AllLiveAstrologerActivity.this);
        if (!isLogin) {
            CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_NAV_SIGNUP,
                    CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
            Intent intent = new Intent(AllLiveAstrologerActivity.this, LoginSignUpActivity.class);
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
                Intent intent = new Intent(AllLiveAstrologerActivity.this, MyAccountActivity.class);
                startActivity(intent);
            }
        }
    }

    public void openWalletScreen() {
        Intent intent = new Intent(AllLiveAstrologerActivity.this, WalletActivity.class);
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
                FontUtils.changeFont(AllLiveAstrologerActivity.this, ((TextView) v), CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
                ((TextView) v).setTextSize(11);
                ((TextView) v).setTextColor(getResources().getColor(R.color.black));
                // added by vishal
                ((TextView) v).setEllipsize(TextUtils.TruncateAt.END);
                ((TextView) v).setPadding(0, 0, 0, 0);
            }
        } catch (Exception e) { }
    }

    private void setBottomNavigationText() {
        ImageView navLiveImg = navView.findViewById(R.id.imgViewLive);
        TextView navHomeTxt = navView.findViewById(R.id.txtViewHome);
        ImageView navCallImg = navView.findViewById(R.id.imgViewCall);
        TextView navCallTxt = navView.findViewById(R.id.txtViewCall);
        TextView navLiveTxt = navView.findViewById(R.id.txtViewLive);
        TextView navChatTxt = navView.findViewById(R.id.txtViewChat);
        ImageView navHisImg = navView.findViewById(R.id.imgViewHistory);
        TextView navHisTxt = navView.findViewById(R.id.txtViewHistory);

        navHomeTxt.setText(getResources().getString(R.string.title_home));

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

        navLiveTxt.setText(getResources().getString(R.string.live));
        navLiveImg.setImageResource(R.drawable.live_icon_new_filled);
        // set new title to the MenuItem
        if (com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(this)) {
            navHisTxt.setText(getResources().getString(R.string.history));
            navHisImg.setImageResource(R.drawable.nav_more_icons);
       } else {
            navHisTxt.setText(getResources().getString(R.string.sign_up));
            navHisImg.setImageResource(R.drawable.nav_profile_icons);;
        }

        //setting Click listener
        navView.findViewById(R.id.bottom_nav_home).setOnClickListener(navbarItemSelectedListener);
        navView.findViewById(R.id.bottom_nav_call).setOnClickListener(navbarItemSelectedListener);
        navView.findViewById(R.id.bottom_nav_live).setOnClickListener(navbarItemSelectedListener);
        navView.findViewById(R.id.bottom_nav_chat).setOnClickListener(navbarItemSelectedListener);
        navView.findViewById(R.id.bottom_nav_history).setOnClickListener(navbarItemSelectedListener);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_live_astrologer);
        astroProfileUrl = getIntent().getStringExtra("astroProfileUrl");
        //manager = SplitInstallManagerFactory.create(this);
        tablayout = findViewById(R.id.tab_layout);
        pager = findViewById(R.id.view_pager);
        backIV = findViewById(R.id.ivBack);
       // fabAllLive = findViewById(R.id.fabAllLive);
        titleTV = findViewById(R.id.tvTitle);
        titleTV.setText(getResources().getString(R.string.astrosage_live_tv));
        navView = findViewById(R.id.nav_view);
        backIV.setOnClickListener(this);
       // fabAllLive.setOnClickListener(this);
        //navView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
       // navView.getMenu().setGroupCheckable(0,false,true);
        customBottomNavigationFont(navView);
        setBottomNavigationText();
        queue = VolleySingleton.getInstance(AllLiveAstrologerActivity.this).getRequestQueue();
        liveAstroRecyclerView = (RecyclerView)findViewById(R.id.liveAstroRecyclerView);
        liveAstroRecyclerView.setItemAnimator(null);
        liveAstrologerModelArrayList = new ArrayList<>();
       // liveAstrologerAdapter = new LiveAstrologerAdapter(AllLiveAstrologerActivity.this, liveAstrologerModelArrayList,1);
        // liveAstroRecyclerView.setLayoutManager(new LinearLayoutManager(AllLiveAstrologerActivity.this));
       // liveAstroRecyclerView.setAdapter(liveAstrologerAdapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout)findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getLiveAstrologerList();
                    }
                }, 500);
            }
        });
        initLiveAstroReceiver();
        getLiveAstrologerList();
        setPagerAdapter();
    }
    private void setPagerAdapter() {

        ArrayList<Fragment> fragList = new ArrayList<Fragment>();
        fragList.add(new AllLiveAstroFragment());
        fragList.add(new SchedulesLiveFragment());
        adapter = new ViewPagerLiveAdapter(this, getSupportFragmentManager(), fragList);
        pager.setAdapter(adapter);
        tablayout.setupWithViewPager(pager);
        if (tablayout.getTabAt(0).getCustomView() != null) {
            FontUtils.changeFont(this, (TextView) tablayout.getTabAt(0).getCustomView(), CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        }
        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }
    @Override
    protected void onStop() {
        super.onStop();
        //Log.e("SAN CI DA ", " onStop() inside ");
        /*if(manager != null) {
            manager.unregisterListener(listener);
        }*/
    }

    @Override
    public void onResume() {
        super.onResume();
        /*if(manager != null) {
            manager.registerListener(listener);
        }*/
        getLiveAstrologerDataFromServer();
    }

    private void getLiveAstrologerList() {

        String liveAstroData = CUtils.getLiveAstroList();
        if (!TextUtils.isEmpty(liveAstroData)) {
            parseLiveAstrologerList(liveAstroData);
        }
    }

    private void getLiveAstrologerDataFromServer() {
        if (!CUtils.isConnectedWithInternet(AllLiveAstrologerActivity.this)) {
            if (mSwipeRefreshLayout != null) {
                mSwipeRefreshLayout.setRefreshing(false);
                mSwipeRefreshLayout.setEnabled(true);
            }
            CUtils.showSnackbar(navView, getResources().getString(R.string.no_internet), AllLiveAstrologerActivity.this);
        } else {

            if ((CUtils.getCurrentTimeStamp() - CUtils.getApiLastHitTime()) > (60 * 1000)) {

//                if (liveAstrologerAdapter == null) {
//                    showProgressBar();
//                }
                // Log.e("SAN LiveAstro URL2=>", CGlobalVariables.GET_LIVE_ASTRO_URL);
//                StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.GET_LIVE_ASTRO_URL,
//                        AllLiveAstrologerActivity.this, false, CUtils.getLiveAstroParams(this,CUtils.getActivityName(AllLiveAstrologerActivity.this)), FETCH_LIVE_ASTROLOGER).getMyStringRequest();
//                queue.add(stringRequest);
                ApiList api = RetrofitClient.getInstance().create(ApiList.class);
                Call<ResponseBody> call = api.getLiveAstrologerList(CUtils.getLiveAstroParams(this, CUtils.getActivityName(AllLiveAstrologerActivity.this)));
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        try {
                            hideProgressBar();
                            if (response.body() != null) {
                                String myResponse = response.body().string();
                                if (mSwipeRefreshLayout != null) {
                                    mSwipeRefreshLayout.setRefreshing(false);
                                    mSwipeRefreshLayout.setEnabled(true);
                                }
                                CUtils.setApiLastHitTime();
                                CUtils.saveLiveAstroList(myResponse);
                                parseLiveAstrologerList(myResponse);
                            }
                        } catch (Exception e) {
                            //
                        }

                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        hideProgressBar();
                        if (mSwipeRefreshLayout != null) {
                            mSwipeRefreshLayout.setRefreshing(false);
                            mSwipeRefreshLayout.setEnabled(true);
                        }
                    }
                });
            }
        }
    }

    private void parseLiveAstrologerList(String liveAstroData){
        if(TextUtils.isEmpty(liveAstroData)){
            return;
        }
        try {

            JSONObject jsonObject = new JSONObject(liveAstroData);
            JSONArray jsonArray = jsonObject.getJSONArray("astrologers");
            liveAstrologerModelArrayList.clear();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                LiveAstrologerModel liveAstrologerModel = CUtils.parseLiveAstrologerObject(object);
                if(liveAstrologerModel == null) continue;
                liveAstrologerModelArrayList.add(liveAstrologerModel);
            }

//            if (liveAstrologerModelArrayList.isEmpty()){
//                showEmptyListDialog();
//            } else if(liveAstrologerModelArrayList.size()>4){
//                dismissEmptyListDialog();
//                liveAstrologerAdapter = new LiveAstrologerAdapter(AllLiveAstrologerActivity.this, liveAstrologerModelArrayList,1);
//                liveAstroRecyclerView.setLayoutManager(new GridLayoutManager(AllLiveAstrologerActivity.this,3));
//                liveAstroRecyclerView.setAdapter(liveAstrologerAdapter);
//                if(!spacingSet){
//                    liveAstroRecyclerView.addItemDecoration(new GridViewSpacing(20));
//                    spacingSet = true;
//                }
//            }else{
//                dismissEmptyListDialog();
//                liveAstrologerAdapter.notifyDataSetChanged();
//            }

            CUtils.parseGiftList(liveAstroData);
        }catch (Exception e){
            //Log.e("redirectToLink", e.toString());
            liveAstrologerModelArrayList.clear();
            //liveAstrologerAdapter.notifyDataSetChanged();
        }
        mSwipeRefreshLayout.setRefreshing(false);

        if(!TextUtils.isEmpty(astroProfileUrl)){
            LiveAstrologerModel liveAstrologerModel = getLiveAstrologerModelFromList(astroProfileUrl);
            if(liveAstrologerModel != null){
                checkPermissions(liveAstrologerModel);
            }else {
                Toast.makeText(AllLiveAstrologerActivity.this, getResources().getString(R.string.text_astrologer_not_live), Toast.LENGTH_SHORT).show();
                finish();
            }
        }else if (getIntent().hasExtra("fromLiveIcon")){
            joinLive();
        } else if (getIntent().hasExtra("upcoming")){
            int currPos=pager.getCurrentItem();
            new Handler().post(new Runnable() {
                @Override
                public void run() {
                    pager.setCurrentItem(currPos+1);
                }
            });
        }

    }

    public void showProgressBar() {
        try {
            if (pd == null) {
                pd = new CustomProgressDialog(AllLiveAstrologerActivity.this);
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
//        hideProgressBar();
//        if (mSwipeRefreshLayout != null) {
//            mSwipeRefreshLayout.setRefreshing(false);
//            mSwipeRefreshLayout.setEnabled(true);
//        }
//
//        if (response != null && response.length() > 0) {
//
//            if (method == FETCH_LIVE_ASTROLOGER) {
//                try {
//                    CUtils.setApiLastHitTime();
////                    Log.e("SAN HF AStro res ", " Time=> " + System.currentTimeMillis() + " res=> " + response);
//                    CUtils.saveLiveAstroList(response);
//                    parseLiveAstrologerList(response);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }
    }

    @Override
    public void onError(VolleyError error) {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.ivBack:
                finish();
                break;

        }
    }

    public void checkPermissions(LiveAstrologerModel liveAstrologerModel) {
        AstrosageKundliApplication.liveAstrologerModel = liveAstrologerModel;

        openLiveStramingScreen();

//        boolean granted = true;
//        for (String per : PERMISSIONS) {
//            if (!permissionGranted(per)) {
//                granted = false;
//                break;
//            }
//        }
//
//        if (granted) {
//            openLiveStramingScreen();
//        } else {
//            requestPermissions();
//        }
    }

//    private boolean permissionGranted(String permission) {
//        return ContextCompat.checkSelfPermission(
//                this, permission) == PackageManager.PERMISSION_GRANTED;
//    }
//
//    private void requestPermissions() {
//        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_REQ_CODE);
//    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (requestCode == PERMISSION_REQ_CODE) {
//            boolean granted = true;
//            for (int result : grantResults) {
//                granted = (result == PackageManager.PERMISSION_GRANTED);
//                if (!granted) break;
//            }
//
//            if (granted) {
//                openLiveStramingScreen();
//            } else {
//                toastNeedPermissions();
//            }
//        }
//    }

    private void openLiveStramingScreen(){
        try{
            if(AstrosageKundliApplication.liveAstrologerModel == null){
                return;
            }
            launchVartaLiveStreaming();
        }catch (Exception e){
        }
    }

    private void launchVartaLiveStreaming() {
        /*if(manager == null) return;
        if (manager.getInstalledModules().contains(CGlobalVariables.MODULE_VARTA_LIVE)) {
            Intent intent = new Intent(CGlobalVariables.INTENT_LIVE_ACTIVITY);
            intent.setPackage(BuildConfig.APPLICATION_ID);
            startActivity(intent);
            if(!TextUtils.isEmpty(astroProfileUrl)){
                finish();
            }
        } else {
            installVartaLiveStreaming();
        }*/

        CUtils.openLiveActivity(this);
        if(!TextUtils.isEmpty(astroProfileUrl)){
            finish();
        }
    }

/*    public void installVartaLiveStreaming() {
        if(manager == null) return;
        SplitInstallRequest request = SplitInstallRequest
                .newBuilder()
                .addModule(CGlobalVariables.MODULE_VARTA_LIVE)
                .build();

        manager.startInstall(request)
                .addOnSuccessListener(new com.google.android.play.core.tasks.OnSuccessListener<Integer>() {
                    @Override
                    public void onSuccess(Integer sessionId) {
                        showInstallLiveProgressBar();
                    }
                })
                .addOnFailureListener(new com.google.android.play.core.tasks.OnFailureListener() {
                    @Override
                    public void onFailure(Exception exception) {
                        CUtils.showSnackbar(liveAstroRecyclerView, "Live content installation failed", AllLiveAstrologerActivity.this);
                    }
                });
    }*/


    /*SplitInstallStateUpdatedListener listener =
            new SplitInstallStateUpdatedListener() {
                @Override
                public void onStateUpdate(SplitInstallSessionState state) {

                    switch (state.status()) {
                        case SplitInstallSessionStatus.DOWNLOADING:
                            int totalBytes = (int) state.totalBytesToDownload();
                            int progress = (int) state.bytesDownloaded();
                            updateInstallLiveProgressBar(CUtils.convertBytesToMB(totalBytes), CUtils.convertBytesToMB(progress));
                            break;
                        case SplitInstallSessionStatus.INSTALLING:
                            break;
                        case SplitInstallSessionStatus.DOWNLOADED:
                            break;

                        case SplitInstallSessionStatus.INSTALLED:
                            hideInstallLiveProgressBar();
                            openLiveStramingScreen();
                            break;

                        case SplitInstallSessionStatus.CANCELED:
                            hideInstallLiveProgressBar();
                            CUtils.showSnackbar(liveAstroRecyclerView, "Live content installation cancelled", AllLiveAstrologerActivity.this);
                            break;

                        case SplitInstallSessionStatus.PENDING:
                            break;

                        case SplitInstallSessionStatus.FAILED:
                            hideInstallLiveProgressBar();
                            CUtils.showSnackbar(liveAstroRecyclerView, "Live content installation failed. Error code: " + state.errorCode(), AllLiveAstrologerActivity.this);
                            break;
                    }
                }
            };*/

    private void toastNeedPermissions() {
        Toast.makeText(this, getString(R.string.need_necessary_permissions), Toast.LENGTH_LONG).show();
    }

    private void initLiveAstroReceiver(){
        try {
            liveAstroReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    getLiveAstrologerDataFromServer();
                }
            };
            LocalBroadcastManager.getInstance(this).registerReceiver((liveAstroReceiver),
                    new IntentFilter(CGlobalVariables.LIVE_ASTRO_BROAD_ACTION)
            );
        }catch (Exception e){
            //
        }
    }

    @Override
    public void onDestroy() {
        try {
            if (liveAstroReceiver != null) {
                LocalBroadcastManager.getInstance(this).unregisterReceiver(liveAstroReceiver);
            }
        }catch (Exception e){
            //
        }
        super.onDestroy();
    }

    private void showEmptyListDialog(){
        emptyListDialog = new Dialog(this);
        emptyListDialog.setContentView(R.layout.no_live_astrologer_dialog_layout);
        emptyListDialog.setCancelable(false);
        TextView textView = emptyListDialog.findViewById(R.id.tvNoLiveAstro);
        Button button = emptyListDialog.findViewById(R.id.btnNoLiveAstro);

        FontUtils.changeFont(this, textView, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(this, button, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);

        button.setOnClickListener(v->{
            finish();
            Intent dashboardIntent = new Intent(this, DashBoardActivity.class);
            startActivity(dashboardIntent);
        });

        emptyListDialog.show();

    }

    private void dismissEmptyListDialog(){
        if (emptyListDialog != null){
            emptyListDialog.dismiss();
        }
    }


    /**
     *
     * @param urlText
     * @return LiveAstrologerModel
     */
    private LiveAstrologerModel getLiveAstrologerModelFromList(String urlText){
        try{
            if(TextUtils.isEmpty(urlText)){
                return null;
            }
            if(liveAstrologerModelArrayList != null) {
                for (int i = 0; i < liveAstrologerModelArrayList.size(); i++) {
                    LiveAstrologerModel liveAstrologerModel = liveAstrologerModelArrayList.get(i);
                    if (liveAstrologerModel == null) continue;
                    if (urlText.equals(liveAstrologerModel.getUrltext())) {
                        return liveAstrologerModel;
                    }

                }
            }
        }catch (Exception e){
            //
        }
        return null;
    }

    @Override
    public void clickCall() {
        CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_ALL_LIVE_BOTTOM_CALL, FIREBASE_EVENT_ITEM_CLICK, "");
        createSession(AllLiveAstrologerActivity.this, ALL_LIVE_BOTTOM_BAR_CALL_PARTNER_ID);
        super.clickCall();
    }

    @Override
    public void clickChat() {
        CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_ALL_LIVE_BOTTOM_CHAT, FIREBASE_EVENT_ITEM_CLICK, "");
        createSession(AllLiveAstrologerActivity.this, ALL_LIVE_BOTTOM_BAR_CHAT_PARTNER_ID);
        super.clickChat();
    }

    @Override
    public void clickLive() {
        joinLive();
    }

}
