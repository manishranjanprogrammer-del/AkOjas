package com.ojassoft.astrosage.varta.ui.fragments;

import static com.ojassoft.astrosage.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_VARTA_HOME_HOROSCOPE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_VARTA_HOME_KUNDLI;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_VARTA_HOME_MATCHING;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_PASSWORD;
import static com.ojassoft.astrosage.utils.CGlobalVariables.MODULE_HOROSCOPE;
import static com.ojassoft.astrosage.utils.CUtils.callHoroscopeActivity;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.FILTER_TYPE_AVAILABLE;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.FILTER_TYPE_CALL;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.FILTER_TYPE_CHAT;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.USER_SPEED;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.isFromConsultTab;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.ActAppModule;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.HomeInputScreen;
import com.ojassoft.astrosage.ui.act.matching.HomeMatchMakingInputScreen;
import com.ojassoft.astrosage.utils.OnlineAstrologerAdapter;
import com.ojassoft.astrosage.varta.adapters.LastConsultAdapter;
import com.ojassoft.astrosage.varta.adapters.LiveAstrologerAdapter;
import com.ojassoft.astrosage.varta.adapters.NewsAdapter;
import com.ojassoft.astrosage.varta.adapters.VartaHomeBannerAdapter;
import com.ojassoft.astrosage.varta.adapters.VartaHomeExpertiseAdapter;
import com.ojassoft.astrosage.varta.dao.NotificationDBManager;
import com.ojassoft.astrosage.varta.dialog.CallInitiatedDialog;
import com.ojassoft.astrosage.varta.dialog.CallMsgDialog;
import com.ojassoft.astrosage.varta.dialog.FreeMinuteDialog;
import com.ojassoft.astrosage.varta.dialog.FreeMinuteMinimizeDialog;
import com.ojassoft.astrosage.varta.dialog.InSufficientBalanceDialog;
import com.ojassoft.astrosage.varta.interfacefile.LoadMoreList;
import com.ojassoft.astrosage.varta.model.AstrologerDetailBean;
import com.ojassoft.astrosage.varta.model.BannerLinkModel;
import com.ojassoft.astrosage.varta.model.BookmarkModel;
import com.ojassoft.astrosage.varta.model.CallHistoryBean;
import com.ojassoft.astrosage.varta.model.LiveAstrologerModel;
import com.ojassoft.astrosage.varta.service.CallStatusCheckService;
import com.ojassoft.astrosage.varta.ui.activity.AllLiveAstrologerActivity;
import com.ojassoft.astrosage.varta.ui.activity.AstrologerDescriptionActivity;
import com.ojassoft.astrosage.varta.ui.activity.ConsultantHistoryActivity;
import com.ojassoft.astrosage.varta.ui.activity.DashBoardActivity;
import com.ojassoft.astrosage.varta.ui.activity.LoginSignUpActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.ChatUtils;
import com.ojassoft.astrosage.varta.utils.CustomProgressDialog;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.varta.volley_network.GZipRequest;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;
import com.ojassoft.astrosage.varta.volley_network.VolleyServiceHandler;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class VartaHomeFragment extends Fragment implements VolleyResponse, LoadMoreList, View.OnClickListener {

    private static final String TAG = "VartaHomeFragment";
    private static final String ARG_PARAM1 = "param1";
    private static VartaHomeFragment instance;
    private final int FETCH_BANNER = 2;
    private final int FETCH_LIVE_ASTROLOGER = 4;
    private final int FETCH_LAST_CONSULTATIONS= 10;
    private final int CALL_METHOD = 22;
    int[] newsImageList = new int[]{
            R.drawable.news18,
            R.drawable.hindustan,
            R.drawable.ht_lucknow,
            R.drawable.imbd
    };
    String[] newsImageUrl = new String[]{
            "https://varta.astrosage.com/images/news18-big.jpg",
            "https://varta.astrosage.com/images/hindustan-big.jpg",
            "https://varta.astrosage.com/images/ht-lucknow-big.jpg",
            "https://varta.astrosage.com/images/imbd-big.jpg"
    };
    String[] expertiseArray = new String[]{
            "Vedic",
            "KP System",
            "Lal Kitab",
            "Vastu",
            "Numerology",
            "Tarot Reading",
            "Reiki",
            "Feng Shui",
            "Horary"
    };
    ArrayList<ArrayList<AstrologerDetailBean>> expertiseNamesList;
    ArrayList<AstrologerDetailBean> regionalAstrologerList;
    private int expertiseFilterTye = -1;
    private ArrayList<String> bannerArrayList;
    private ArrayList<BannerLinkModel> bannerLinkArrayList;
    private ArrayList<AstrologerDetailBean> allAstrologersArrayList;
    private ArrayList<AstrologerDetailBean> callAstrologersArrayList;
    private ArrayList<AstrologerDetailBean> chatAstrologersArrayList;
    private ArrayList<CallHistoryBean> callHistoryList;
    public static ArrayList<LiveAstrologerModel> liveAstrologersArrayList;
    private Activity activity;
    private RequestQueue queue;
    private CustomProgressDialog pd;
    private FreeMinuteMinimizeDialog freeMinuteMinimizeDialog;
    private FreeMinuteDialog freeMinutedialog;
    private CallInitiatedDialog callInitiatedDialog;
    //private ConstraintLayout containerLayout;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private BroadcastReceiver liveAstroReceiver;
    private ImageView logoGif, ivVartaHomeKundliLogo;
    private LinearLayout llGifProgress;
    private LinearLayout llVartaHomeKundli, llVartaHomeMatching, llVartaHomeHoroscope, llVartaHomeFreeChat;
    private TextView tvVartaHomeKundliTxt, tvVartaHomeMatchingTxt, tvVartaHomeHoroscopeTxt, tvVartaHomeFreeChatTxt,
            tvVartaHomeLiveHeading, tvVartaHomeCallHeading, tvVartaHomeChatHeading, tvVartaHomeLastConsultHeading,
            tvVartaHomeLoveHeading, tvVartaHomeMarriageHeading, tvVartaHomeKPHeading, tvVartaHomeTarotHeading,
            tvVartaHomeNewsHeading, tvVartaHomeLiveSeeMore, tvVartaHomeCallSeeMore, tvVartaHomeChatSeeMore,
            tvVartaHomeLastConsultSeeMore, tvVartaHomeLoveSeeMore, tvVartaHomeMarriageSeeMore, tvVartaHomeKPSeeMore,
            tvVartaHomeTarotSeeMore,tvVartaHomeRegionalHeading,tvVartaHomeRegionalSeeMore;
    private RecyclerView rvVartaHomeBanner, rvVartaHomeLive, rvVartaHomeCall, rvVartaHomeChat, rvVartaHomeLastConsult,rvVartaHomeRegional,
            rvVartaHomeLove, rvVartaHomeMarriage, rvVartaHomeKP, rvVartaHomeTarot, rvVartaHomeNews, rvVartaHomeExpertiseLists;
    private CardView cardVartaHomeBanner;
    private String astroUrlForLiveJoin,callingAstroName,callingAstroProfileUrl="";
    private LiveAstrologerAdapter liveAstrologerAdapter;
    private VartaHomeBannerAdapter vartaHomeBannerAdapter;
    private OnlineAstrologerAdapter astrologerCallAdapter, astrologerChatAdapter, kpAstrologerAdapter, tarotAstrologerAdapter;
    private VartaHomeExpertiseAdapter expertiseAdapter;
    LastConsultAdapter lastConsultAdapter;
    private OnlineAstrologerAdapter regionalAdapter;
    private boolean isFreeConsultation;
    private String offerType = "";
    private boolean IS_LIVE_SESSION = false;

    @SuppressLint("ValidFragment")
    public VartaHomeFragment() {

    }

    public static VartaHomeFragment newInstance(String param1) {
        VartaHomeFragment fragment = new VartaHomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    public static VartaHomeFragment GetInstance() {
        if(instance == null) {
            instance = new VartaHomeFragment();
        }
        return instance;
    }

    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;
        allAstrologersArrayList = new ArrayList<>();
        callAstrologersArrayList = new ArrayList<>();
        chatAstrologersArrayList = new ArrayList<>();
        liveAstrologersArrayList = new ArrayList<>();
        callHistoryList = new ArrayList<>();
        bannerArrayList = new ArrayList<>();
        bannerLinkArrayList = new ArrayList<>();
        expertiseNamesList = new ArrayList<>();
        regionalAstrologerList = new ArrayList<>();
        vartaHomeBannerAdapter = null;
        liveAstrologerAdapter = null;
        astrologerCallAdapter = null;
        astrologerChatAdapter = null;
        kpAstrologerAdapter = null;
        tarotAstrologerAdapter = null;
        expertiseAdapter = null;
        lastConsultAdapter = null;
        queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LANGUAGE_CODE =  AstrosageKundliApplication.getAppContext().getLanguageCode();
        //manager = SplitInstallManagerFactory.create(getActivity());
        com.ojassoft.astrosage.varta.utils.CUtils.getRobotoFont(getActivity(), LANGUAGE_CODE, com.ojassoft.astrosage.varta.utils.CGlobalVariables.regular);
        View view = inflater.inflate(R.layout.fragment_varta_home, container, false);
        IS_LIVE_SESSION = false;
        initView(view);
        initData();
        initListeners();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            if (activity != null && activity instanceof DashBoardActivity) {
                ((DashBoardActivity) activity).actionOnResume();
            }
        }catch (Exception e){
            //
        }
        try {
            isFromConsultTab = true;
            getAstrologerListFromServer(false);
            getLiveAstrologerDataFromServer();
            if (CUtils.getUserLoginStatus(activity)) {
                getLastConsultDataFromServer();
            } else {
                hideShowLastConsultation(new ArrayList<CallHistoryBean>());
            }
            if (CUtils.astroListFilterType > 0 && activity instanceof DashBoardActivity) {
                openAstrologerListFragment(CUtils.astroListFilterType, activity);
            }
        }catch (Exception e){
            //
        }
    }


    private void initView(View root) {
        llVartaHomeKundli = root.findViewById(R.id.llVartaHomeKundli);
        llVartaHomeMatching = root.findViewById(R.id.llVartaHomeMatching);
        llVartaHomeHoroscope = root.findViewById(R.id.llVartaHomeHoroscope);
        llVartaHomeFreeChat = root.findViewById(R.id.llVartaHomeFreeChat);
        rvVartaHomeBanner = root.findViewById(R.id.rvVartaHomeBanner);
        rvVartaHomeLive = root.findViewById(R.id.rvVartaHomeLive);
        rvVartaHomeLastConsult = root.findViewById(R.id.rvVartaHomeLastConsult);
        //containerLayout = root.findViewById(R.id.container_layout);
        mSwipeRefreshLayout = root.findViewById(R.id.swipeToRefresh);
        llGifProgress = root.findViewById(R.id.ll_gif_progress);
        logoGif = root.findViewById(R.id.logo_gif);
        cardVartaHomeBanner = root.findViewById(R.id.cardVartaHomeBanner);
        tvVartaHomeLiveSeeMore = root.findViewById(R.id.tvVartaHomeLiveSeeMore);
        tvVartaHomeCallSeeMore = root.findViewById(R.id.tvVartaHomeCallSeeMore);
        tvVartaHomeChatSeeMore = root.findViewById(R.id.tvVartaHomeChatSeeMore);
        tvVartaHomeLastConsultSeeMore = root.findViewById(R.id.tvVartaHomeLastConsultSeeMore);
        tvVartaHomeLiveHeading = root.findViewById(R.id.tvVartaHomeLiveHeading);
        tvVartaHomeCallHeading = root.findViewById(R.id.tvVartaHomeCallHeading);
        tvVartaHomeChatHeading = root.findViewById(R.id.tvVartaHomeChatHeading);
        tvVartaHomeLastConsultHeading = root.findViewById(R.id.tvVartaHomeLastConsultHeading);
        tvVartaHomeNewsHeading = root.findViewById(R.id.tvVartaHomeNewsHeading);
        rvVartaHomeCall = root.findViewById(R.id.rvVartaHomeCall);
        rvVartaHomeChat = root.findViewById(R.id.rvVartaHomeChat);
        rvVartaHomeNews = root.findViewById(R.id.rvVartaHomeNews);
        tvVartaHomeKundliTxt = root.findViewById(R.id.tvVartaHomeKundliTxt);
        ivVartaHomeKundliLogo = root.findViewById(R.id.ivVartaHomeKundliLogo);
        tvVartaHomeFreeChatTxt = root.findViewById(R.id.tvVartaHomeFreeChatTxt);
        rvVartaHomeExpertiseLists = root.findViewById(R.id.rvVartaHomeExpertiseLists);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        tvVartaHomeRegionalHeading = root.findViewById(R.id.tvVartaHomeRegionalHeading);
        tvVartaHomeRegionalSeeMore = root.findViewById(R.id.tvVartaHomeRegionalSeeMore);
        rvVartaHomeRegional = root.findViewById(R.id.rvVartaHomeRegional);

        FontUtils.changeFont(activity, tvVartaHomeLiveHeading, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(activity, tvVartaHomeCallHeading, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(activity, tvVartaHomeChatHeading, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(activity, tvVartaHomeLastConsultHeading, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(activity, tvVartaHomeNewsHeading, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(activity, tvVartaHomeRegionalHeading, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);

        tvVartaHomeKundliTxt.setText(getResources().getStringArray(R.array.kundliModulesList)[0]);

        if(LANGUAGE_CODE == CGlobalVariables.TELUGU || LANGUAGE_CODE == CGlobalVariables.TAMIL ||
                LANGUAGE_CODE == CGlobalVariables.KANNADA || LANGUAGE_CODE == CGlobalVariables.MALAYALAM){
            ivVartaHomeKundliLogo.setImageResource(R.drawable.ic_kundali);
        } else if( LANGUAGE_CODE == CGlobalVariables.BANGALI ){
            ivVartaHomeKundliLogo.setImageResource(R.drawable.ic_kundali_east);
        } else {
            ivVartaHomeKundliLogo.setImageResource(R.drawable.ic_kundali_north);
        }

        if (CUtils.getUserLoginStatus(activity)) {
            if (CUtils.getUserIntroOfferType(activity) != null && CUtils.getUserIntroOfferType(activity).equals(CGlobalVariables.INTRO_OFFER_TYPE_FREE)) {
                tvVartaHomeFreeChatTxt.setText(activity.getResources().getString(R.string.free_chat));
            } else {
                tvVartaHomeFreeChatTxt.setText(activity.getResources().getString(R.string.chat_now));
            }
        } else {
            tvVartaHomeFreeChatTxt.setText(activity.getResources().getString(R.string.free_chat));
        }

        if (activity instanceof DashBoardActivity) {
            ((DashBoardActivity) activity).changeToolbar(CGlobalVariables.VARTA_HOME_FRAGMENT);
        }

    }

    private void initData() {
        initLiveAstroReceiver();
        getBannerImgData();
        getLiveAstrologerListFromCache();
        getAstrologerListFromCache();
    }

    private void initNewsRecyclerView() {
        tvVartaHomeNewsHeading.setVisibility(View.VISIBLE);
        rvVartaHomeNews.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
        rvVartaHomeNews.setNestedScrollingEnabled(false);
        NewsAdapter newsAdapter = new NewsAdapter(activity, newsImageList, newsImageUrl);
        rvVartaHomeNews.setAdapter(newsAdapter);
    }

    private void initListeners() {
        mSwipeRefreshLayout.setOnRefreshListener(() ->
                new Handler().postDelayed(() -> {
                    getAstrologerListFromServer(true);
                    getLiveAstrologerDataFromServer();
                    getLastConsultDataFromServer();
                }, 200));

        llVartaHomeKundli.setOnClickListener(this);
        llVartaHomeMatching.setOnClickListener(this);
        llVartaHomeHoroscope.setOnClickListener(this);
        llVartaHomeFreeChat.setOnClickListener(this);
        tvVartaHomeLiveSeeMore.setOnClickListener(this);
        tvVartaHomeCallSeeMore.setOnClickListener(this);
        tvVartaHomeLastConsultSeeMore.setOnClickListener(this);
        tvVartaHomeChatSeeMore.setOnClickListener(this);
        tvVartaHomeRegionalSeeMore.setOnClickListener(this);
    }

    private void initLiveAstroReceiver() {
        try {
            liveAstroReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    // Log.e("SAN PF LiveAstro Res=>", "3");
                    getLiveAstrologerListFromCache();
                }
            };
            LocalBroadcastManager.getInstance(activity).registerReceiver((liveAstroReceiver),
                    new IntentFilter(CGlobalVariables.LIVE_ASTRO_BROAD_ACTION)
            );
        } catch (Exception e) {
            //
        }
    }

    private void setAllAstrologersListAdapter(ArrayList<AstrologerDetailBean> list, int type) {
        try {
            if (type == FILTER_TYPE_CALL) {
                if (astrologerCallAdapter == null) {
                    astrologerCallAdapter = new OnlineAstrologerAdapter(activity, list, FILTER_TYPE_CALL, VartaHomeFragment.this, this);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity,
                            RecyclerView.HORIZONTAL, false);
                    rvVartaHomeCall.setAdapter(astrologerCallAdapter);
                    rvVartaHomeCall.setLayoutManager(layoutManager);
                    rvVartaHomeCall.setNestedScrollingEnabled(false);
                    tvVartaHomeCallHeading.setVisibility(View.VISIBLE);
                    tvVartaHomeCallSeeMore.setVisibility(View.VISIBLE);
                } else {
                    astrologerCallAdapter.notifyDataSetChanged();
                }
            } else if (type == FILTER_TYPE_CHAT) {
                if (astrologerChatAdapter == null) {
                    astrologerChatAdapter = new OnlineAstrologerAdapter(activity, list, FILTER_TYPE_CHAT, this,this);
                    RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity,
                            RecyclerView.HORIZONTAL, false);
                    rvVartaHomeChat.setAdapter(astrologerChatAdapter);
                    rvVartaHomeChat.setLayoutManager(layoutManager);
                    rvVartaHomeChat.setNestedScrollingEnabled(false);
                    tvVartaHomeChatHeading.setVisibility(View.VISIBLE);
                    tvVartaHomeChatSeeMore.setVisibility(View.VISIBLE);
                } else {
                    astrologerChatAdapter.notifyDataSetChanged();
                }
            }
        } catch (Exception e) {
            //
        }
    }


    private void getAstrologerListFromCache() {
        if (activity == null || getActivity() == null) return;
        String astroList = CUtils.getAstroList();
        if (!TextUtils.isEmpty(astroList)) {
            parseAstrologerList(astroList);
        }
    }

    private void getLiveAstrologerListFromCache() {
        String liveAstroData = CUtils.getLiveAstroList();
        if (!TextUtils.isEmpty(liveAstroData)) {
            parseLiveAstrologerList(liveAstroData);
        }
    }

    private void getBannerImgData() {
        String bannerData = CUtils.getBannerList();
        if (!TextUtils.isEmpty(bannerData)) {
            parseBannerList(bannerData);
        } else {
            getBannerDataFromServer();
        }
    }

    private void getBannerDataFromServer() {
        if (CUtils.isConnectedWithInternet(getActivity())) {
            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.GET_BANNER_URL, VartaHomeFragment.this, false, getBannerParams(), FETCH_BANNER).getMyStringRequest();
            queue.add(stringRequest);
        }
    }

    private void getAstrologerListFromServer(boolean isSwipeRefresh) {
        if (activity == null) return;
        if (!CUtils.isConnectedWithInternet(activity)) {
            if(isSwipeRefresh) {
                hideSwipeRefreshProgressBar();
            }
            CUtils.showSnackbar(mSwipeRefreshLayout, getResources().getString(R.string.no_internet), getActivity());
        } else {
            if(!isSwipeRefresh) {
                if (allAstrologersArrayList == null || allAstrologersArrayList.isEmpty()) {
                    showLogoProgressbar();
                }
            }
            fetchAllAstrologerData();
        }
    }

    private void getLiveAstrologerDataFromServer() {
        if (activity == null) return;
        boolean liveStreamingEnabledForAstrosage = CUtils.getBooleanData(activity, CGlobalVariables.liveStreamingEnabledForAstrosage, false);
        if (!liveStreamingEnabledForAstrosage) { //fetch data according to tagmanager
            return;
        }
        if (CUtils.isConnectedWithInternet(activity)) {

            if ((CUtils.getCurrentTimeStamp() - CUtils.getApiLastHitTime()) > (60 * 1000)) {
                //Log.e("SAN LiveAstro URL2=>", CGlobalVariables.GET_LIVE_ASTRO_URL);
                StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.GET_LIVE_ASTRO_URL,
                        VartaHomeFragment.this, false, CUtils.getLiveAstroParams(activity, CUtils.getActivityName(activity)), FETCH_LIVE_ASTROLOGER).getMyStringRequest();
                queue.add(stringRequest);
            }
        } else {
            getLiveAstrologerListFromCache();
        }
    }

    private void parseBannerList(String bannerData) {
        try {
            if (bannerArrayList == null) {
                bannerArrayList = new ArrayList<>();
            } else {
                bannerArrayList.clear();
            }

            if (bannerLinkArrayList == null) {
                bannerLinkArrayList = new ArrayList<>();
            } else {
                bannerLinkArrayList.clear();
            }

            JSONObject jsonObject = new JSONObject(bannerData);
            JSONArray imgUrlArr = jsonObject.getJSONArray("bannerimage");
            JSONArray linkArray = jsonObject.getJSONArray("links");
            for (int i = 0; i < imgUrlArr.length(); i++) {
                String imgUrl = imgUrlArr.getString(i);
                imgUrl = imgUrl.replace("%3A", ":");
                imgUrl = imgUrl.replace("%2F", "/");
                //Log.e("LoadMore imgUrl ", imgUrl);
                bannerArrayList.add(imgUrl);
                //for banner link
                BannerLinkModel bannerLinkModel = new BannerLinkModel();
                bannerLinkModel.setLink(linkArray.getJSONObject(i).getString("link"));
                bannerLinkModel.setLoginrequired(linkArray.getJSONObject(i).getBoolean("loginrequired"));
                bannerLinkArrayList.add(bannerLinkModel);
            }
            if (!bannerArrayList.isEmpty()) {
                setBannerAdapter();
                cardVartaHomeBanner.setVisibility(View.GONE);
            } else {
                rvVartaHomeBanner.setVisibility(View.GONE);
                cardVartaHomeBanner.setVisibility(View.VISIBLE);
            }
        } catch (JSONException e) {
            //Log.e("redirectToLink", e.toString());
            e.printStackTrace();
        } catch (Exception e) {
            //Log.e("redirectToLink", e.toString());
        }
    }

    private void setBannerAdapter() {
        try {
            if (vartaHomeBannerAdapter == null) {
                SnapHelper snapHelper = new LinearSnapHelper();
                snapHelper.attachToRecyclerView(rvVartaHomeBanner);
                vartaHomeBannerAdapter = new VartaHomeBannerAdapter(activity, bannerArrayList, bannerLinkArrayList);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false);
                rvVartaHomeBanner.setLayoutManager(mLayoutManager);
                rvVartaHomeBanner.setAdapter(vartaHomeBannerAdapter);
                rvVartaHomeBanner.setNestedScrollingEnabled(false);
                setAutoRotateBanner();
            } else {
                vartaHomeBannerAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            Log.d("mException", String.valueOf(e));
        }
    }

    private void parseAstrologerList(String responseData) {

        if (allAstrologersArrayList == null) {
            allAstrologersArrayList = new ArrayList();
        } else {
            allAstrologersArrayList.clear();
        }

        try {
            JSONObject jsonObject = new JSONObject(responseData);
            if (jsonObject.length() > 0) {

                if (jsonObject.has("verifiedicon")) {
                    try {
                        CUtils.setVerifiedAndOfferImage(URLDecoder.decode(jsonObject.getString("verifiedicon"), "UTF-8"), URLDecoder.decode(jsonObject.getString("verifiedicondetail"), "UTF-8"),
                                URLDecoder.decode(jsonObject.getString("offericon"), "UTF-8"), URLDecoder.decode(jsonObject.getString("offericondetail"), "UTF-8"));
                    } catch (Exception ex) {
                        //
                    }
                }
                AstrosageKundliApplication.astroGroup = jsonObject.optString("astrogroup");
                String offerType = jsonObject.optString("offertype"); //"FIRSTSESSIONFREE";//
                CUtils.setUserIntroOfferType(activity,offerType);
                checkNextRechargeOffer(offerType);
                JSONArray jsonArray = jsonObject.getJSONArray("astrologers");
                if (jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        AstrologerDetailBean astrologerDetailBean = CUtils.parseAstrologerObject(object);
                        if (astrologerDetailBean == null) continue;
                        allAstrologersArrayList.add(astrologerDetailBean);
                    }
                }
                CUtils.allAstrologersArrayList.clear();
                CUtils.allAstrologersArrayList.addAll(allAstrologersArrayList);
                //Log.d("AstroListF", "allAstrologersArrayList size"+allAstrologersArrayList.size());
                setbookMarkedAstrologerList();

                setAllAstrologersListAdapter(getCallAvailableAstrologerList(), FILTER_TYPE_CALL);
                setAllAstrologersListAdapter(getChatAvailableAstrologerList(), FILTER_TYPE_CHAT);

                if (CUtils.popUnderFreeChatClicked && !allAstrologersArrayList.isEmpty()) {
                    CUtils.popUnderFreeChatClicked = false;
                    chatWithRandomAstrologer();
                }
                if (CUtils.popUpLoginFreeCallClicked && !allAstrologersArrayList.isEmpty()) {
                    CUtils.popUpLoginFreeCallClicked = false;
                    callWithRandomAstrologer();
                }
                if (CUtils.popUpLoginFreeChatClicked && !allAstrologersArrayList.isEmpty()) {
                    CUtils.popUpLoginFreeChatClicked = false;
                    chatWithRandomAstrologer();
                }

                displayExpertiseAstrologers();
                initNewsRecyclerView();
            } else {
                ((DashBoardActivity) activity).callSnakbar(getResources().getString(R.string.server_error));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void displayExpertiseAstrologers() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(new Runnable() {
            @Override
            public void run() {

                try {
                    String lang = CUtils.getLanguage(LANGUAGE_CODE);
                    if (lang != null && !lang.isEmpty()) {
                        getRegionalList(lang);
                    }

                    expertiseNamesList = new ArrayList<>();
                    for (String name : CGlobalVariables.astroExpertiseNames) {
                        expertiseNamesList.add(getExpertsList(name));
                    }

                    handler.post(() -> {
                        setExpertiseAdapter(expertiseNamesList);

                        if (lang != null && !lang.isEmpty()){
                            tvVartaHomeRegionalHeading.setVisibility(View.VISIBLE);
                            tvVartaHomeRegionalSeeMore.setVisibility(View.VISIBLE);
                            rvVartaHomeRegional.setVisibility(View.VISIBLE);
                            setRegionalAdapter();
                        }else {
                            tvVartaHomeRegionalHeading.setVisibility(View.GONE);
                            tvVartaHomeRegionalSeeMore.setVisibility(View.GONE);
                            rvVartaHomeRegional.setVisibility(View.GONE);
                        }
                    });
                }catch (Exception e){
                    //
                }
            }
        });

        /*new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {

                setExpertiseAdapter(expertiseNamesList);
            }
        },500);*/
    }

    private void getRegionalList(String lang){
        regionalAstrologerList.clear();
        ArrayList<AstrologerDetailBean> tempList = new ArrayList<>();
        for (int i = 0; i < allAstrologersArrayList.size(); i++){
            if (allAstrologersArrayList.get(i).getLanguage().contains(lang)) {
                tempList.add(allAstrologersArrayList.get(i));
            }
        }
        if (tempList.size() < 6){
            regionalAstrologerList.addAll(tempList);
        } else {
            while (regionalAstrologerList.size() < 6) {
                int rnd = new Random().nextInt(allAstrologersArrayList.size());
                AstrologerDetailBean bean = allAstrologersArrayList.get(rnd);
                if (!regionalAstrologerList.contains(bean) && bean.getLanguage().contains(lang)) {
                    regionalAstrologerList.add(bean);
                }
            }
        }
    }
    private ArrayList<AstrologerDetailBean> getExpertsList(String key){
        ArrayList<AstrologerDetailBean> tempList = new ArrayList<>();
        for (int i = 0; i < allAstrologersArrayList.size(); i++){
            if (allAstrologersArrayList.get(i).getExpertise().contains(key)) {
                tempList.add(allAstrologersArrayList.get(i));
            }
        }
        ArrayList<AstrologerDetailBean> expertiseAstrosList = new ArrayList<>();
        if (tempList.size() < 6){
            expertiseAstrosList.addAll(tempList);
        } else {
            while (expertiseAstrosList.size() < 6) {
                int rnd = new Random().nextInt(allAstrologersArrayList.size());
                AstrologerDetailBean bean = allAstrologersArrayList.get(rnd);
                if (!expertiseAstrosList.contains(bean) && bean.getExpertise().contains(key)) {
                    expertiseAstrosList.add(bean);
                }
            }
        }
        return expertiseAstrosList;
    }

    private void setExpertiseAdapter(ArrayList<ArrayList<AstrologerDetailBean>> filteredList) {
        try {
            if (expertiseAdapter == null) {
                ArrayList<String> astroExpertiseNames = new ArrayList<>(Arrays.asList(CGlobalVariables.astroExpertiseNames));
                expertiseAdapter = new VartaHomeExpertiseAdapter(filteredList, astroExpertiseNames, activity, this);
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity);
                rvVartaHomeExpertiseLists.setAdapter(expertiseAdapter);
                rvVartaHomeExpertiseLists.setLayoutManager(layoutManager);
                rvVartaHomeExpertiseLists.setNestedScrollingEnabled(false);
            } else {
                expertiseAdapter.notifyDataSetChanged();
            }
        } catch (Exception e){
            //
        }
    }

    private void setRegionalAdapter() {
        try {
            if (regionalAdapter == null) {
                RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false);
                rvVartaHomeRegional.setLayoutManager(layoutManager);
                regionalAdapter = new OnlineAstrologerAdapter(activity, regionalAstrologerList);
                rvVartaHomeRegional.setAdapter(regionalAdapter);
            } else {
                regionalAdapter.notifyDataSetChanged();
            }
        } catch (Exception e){
            //
        }
    }

    private void parseLiveAstrologerList(String liveAstroData) {
        if (TextUtils.isEmpty(liveAstroData)) {
            return;
        }
        if (liveAstrologersArrayList == null) {
            liveAstrologersArrayList = new ArrayList<>();
        } else {
            liveAstrologersArrayList.clear();
        }
        try {
            JSONObject jsonObject = new JSONObject(liveAstroData);
            JSONArray jsonArray = jsonObject.getJSONArray("astrologers");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                LiveAstrologerModel liveAstrologerModel = CUtils.parseLiveAstrologerObject(object);
                if (liveAstrologerModel == null) continue;
                liveAstrologersArrayList.add(liveAstrologerModel);
            }
            if (activity instanceof DashBoardActivity) {
                ((DashBoardActivity) activity).liveAstrologerModelArrayList = liveAstrologersArrayList;
            }

            openLiveStramingScreen();

            CUtils.parseGiftList(liveAstroData);

            if (!liveAstrologersArrayList.isEmpty()){
                setLiveAstrologersAdapter();
            }

        } catch (Exception e) {
            //Log.e("redirectToLink", e.toString());
        }
    }

    private void setLiveAstrologersAdapter() {
        try {
            if (liveAstrologerAdapter == null) {
                liveAstrologerAdapter = new LiveAstrologerAdapter(getContext(), liveAstrologersArrayList, 0);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity, RecyclerView.HORIZONTAL, false);
                rvVartaHomeLive.setLayoutManager(mLayoutManager);
                rvVartaHomeLive.setAdapter(liveAstrologerAdapter);
                rvVartaHomeLive.setNestedScrollingEnabled(false);
                tvVartaHomeLiveHeading.setVisibility(View.VISIBLE);
                tvVartaHomeLiveSeeMore.setVisibility(View.VISIBLE);
            } else {
                liveAstrologerAdapter.notifyDataSetChanged();
            }
        } catch (Exception e) {
            Log.d("mException", String.valueOf(e));
        }
    }

    public Map<String, String> getBannerParams() {

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(getActivity()));

        String wallet = CUtils.getWalletRs(getActivity());
        String codeValue = CGlobalVariables.BANNER_ONE_RUPEES;
        //Log.e("LoadMore wallet ", wallet);
        if (wallet == null || !(wallet.equals("0.0"))) {
            codeValue = CGlobalVariables.BANNER_WITHOUT_RUPEES;
        }
        headers.put(CGlobalVariables.WALLET_CODE, codeValue);
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));

        //Log.e("LoadMore params ", headers.toString());
        return headers;
    }

    public HashMap<String, String> getParams() {

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(getActivity()));
        boolean isLogin = CUtils.getUserLoginStatus(getActivity());
        String offerType = CUtils.getCallChatOfferType(getActivity());
        /*if(TextUtils.isEmpty(offerType)){
            offerType ="NA";
        }*/
        try {
            if (isLogin) {
                headers.put(CGlobalVariables.PHONE_NO, CUtils.getUserID(getActivity()));
                headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(getActivity()));
            } else {
                headers.put(CGlobalVariables.PHONE_NO, "");
                headers.put(CGlobalVariables.COUNTRY_CODE, "");
                //offerType = "";
            }
        } catch (Exception e) {
        }
        headers.put(CGlobalVariables.OFFER_TYPE,offerType);
        headers.put(CGlobalVariables.FETCHALL, "1");
        headers.put(CGlobalVariables.PACKAGE_NAME, "" + BuildConfig.APPLICATION_ID);
        headers.put(CGlobalVariables.APP_VERSION, "" + BuildConfig.VERSION_NAME);
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
        headers.put(CGlobalVariables.DEVICE_ID, CUtils.getMyAndroidId(getActivity()));
        //Log.d("TestOfferType ", headers.toString());
        return headers;
    }


    private void fetchAllAstrologerData() {
        if (DashBoardActivity.isGzipDesabled != null && DashBoardActivity.isGzipDesabled.equals("1")) {
            fetchAllAstrologerDataWithoutGzip();
        } else {
            sendGzipRequestAstro(CGlobalVariables.ASTROLOGER_LIST_URL, getParams());
        }
    }

    private void fetchAllAstrologerDataWithoutGzip() {
        if (!CUtils.isConnectedWithInternet(getActivity())) {
            CUtils.showSnackbar(mSwipeRefreshLayout, getResources().getString(R.string.no_internet), getActivity());
        } else {
            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.ASTROLOGER_LIST_URL,
                    VartaHomeFragment.this, false, getParams(), 1).getMyStringRequest();
            stringRequest.setShouldCache(true);
            queue.add(stringRequest);
        }
    }

    private void sendGzipRequestAstro(String url, final Map<String, String> paramsHashMap) {
        RequestQueue queue = VolleySingleton.getInstance(activity).getRequestQueue();
        //Log.d("AstroListF", "paramsHashMap = "+paramsHashMap);
        GZipRequest postRequest = new GZipRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.d("LoginResponse", "gzip resp="+response);
                        if (activity == null || getActivity() == null) return;
                        hideLogoProgressBar();
                        hideSwipeRefreshProgressBar();
                        //Log.e("SAN AIA ", " Astro List Res Time=> " + respTime + " res=> " + response);
                        if (response != null && response.length() > 0) {
                            try {
                                CUtils.saveAstroList(response);
                                parseAstrologerList(response);
                            } catch (Exception e) {
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //Log.e("SAN AIA ", " Astro List Res Time=> " + error);
                        if (activity == null || getActivity() == null) return;
                        try {
                            hideLogoProgressBar();
                            hideSwipeRefreshProgressBar();
                            CUtils.showVolleyErrorRespMessage(mSwipeRefreshLayout, activity, error, "4");
                        } catch (Exception e) {
                            e.printStackTrace();
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
                return paramsHashMap;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.putAll(super.getHeaders());
                params.put("Accept-Encoding", "gzip,deflate");
                return params;
            }
        };

        int socketTimeout = 60000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        postRequest.setRetryPolicy(policy);
        postRequest.setShouldCache(true);
        queue.add(postRequest);

    }

    @Override
    public void onResponse(String response, int method) {
        //Log.d("xyzAbc",  " res=> " + response );
        if (activity == null || getActivity() == null) return;
        hideProgressBar();
        hideLogoProgressBar();
        hideSwipeRefreshProgressBar();

        if (response != null && response.length() > 0) {
            if (method == 1) {
                CUtils.saveAstroList(response);
                parseAstrologerList(response);
            } else if (method == FETCH_LIVE_ASTROLOGER) {
                try {
                    CUtils.setApiLastHitTime();
                    CUtils.saveLiveAstroList(response);
                    parseLiveAstrologerList(response);
                    // call activity
                    if(IS_LIVE_SESSION){
                        Intent intent = new Intent(getActivity(), AllLiveAstrologerActivity.class);
                        intent.putExtra("fromLiveIcon", true);
                        startActivity(intent);
                        IS_LIVE_SESSION = false;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (method == FETCH_BANNER) {
                CUtils.saveBannerData(response);
                parseBannerList(response);
            } else if (method == CALL_METHOD) {
                responseNetworkCall(response);
            } else if(method == FETCH_LAST_CONSULTATIONS){
                parseLastConsultList(response);
            }
        }
    }

    private void openFreeMinDialogBox() {
        try {
            freeMinutedialog.dismiss();
            if (freeMinuteMinimizeDialog == null) {
                freeMinuteMinimizeDialog = new FreeMinuteMinimizeDialog();
            }
            freeMinuteMinimizeDialog.show(getChildFragmentManager(), "MiniDialog");
        } catch (Exception e) {
            //Log.e("SAN ADA", " openFreeMinDialogBox exp " + e.toString());
        }
    }

    @Override
    public void onError(VolleyError error) {
        if (activity == null || getActivity() == null) return;
        try {
            hideProgressBar();
            hideLogoProgressBar();
            hideSwipeRefreshProgressBar();
            CUtils.showVolleyErrorRespMessage(mSwipeRefreshLayout, activity, error,"5");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadMoreAstrologerist(int listCount) {

    }

    @Override
    public void callAstrologer(AstrologerDetailBean astrologerDetailBean) {
        if(CUtils.isChatNotInitiated()) {
            CUtils.isCallChatInitFromAstroList = true;
            if (astrologerDetailBean == null) return;
            String phoneNo = "";
            callingAstroName = astrologerDetailBean.getName();
            callingAstroProfileUrl = astrologerDetailBean.getImageFile();
            String urlText = astrologerDetailBean.getUrlText();
            String astrologerOnlineStatus = astrologerDetailBean.getIsOnline();
            String astrologerBusyStatus = astrologerDetailBean.getIsBusy();
            String astrologerCallStatus = astrologerDetailBean.getIsAvailableForCall();
            String astrologerChatStatus = astrologerDetailBean.getIsAvailableForChat();
            //isFreeConsultation = astrologerDetailBean.getUseIntroOffer();

            if(astrologerDetailBean.isFreeForCall()){
                isFreeConsultation = true;
            }

            offerType = astrologerDetailBean.getIntroOfferType();
            //Log.e("LoadMore ", urlText);
            if (astrologerOnlineStatus.equalsIgnoreCase("true")) {
                phoneNo = CUtils.getUserID(getActivity());
                if (astrologerBusyStatus.equalsIgnoreCase("true")) {
                    //String msg = getResources().getString(R.string.astrologer_busy_msg);
                    //msg = msg.replace("#",waitingTime);
                    //callMsgDialogData(msg, "", true, CGlobalVariables.CALL_CLICK);
                    //notificationAlertDialog();
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.ASTRO_BUSY_NOTI_FREE,
                            AstrosageKundliApplication.currentEventType, "");
                    CUtils.showNotificationAlertDialog(activity, astrologerDetailBean, getChildFragmentManager());
                } else {
                    if (phoneNo.length() > 0 && urlText.length() > 0) {
                        boolean isAgoraCallEnabled = com.ojassoft.astrosage.utils.CUtils.getBooleanData(getContext(), com.ojassoft.astrosage.utils.CGlobalVariables.ISAGORACALLENABLED, false);
                        if(isAgoraCallEnabled){
                            checkCallMedium(astrologerDetailBean);
                        }else {
                            CUtils.openProfileOrKundliActFromFrag(activity, astrologerDetailBean.getUrlText(), "call", this);
                        }

                    /*Intent i = new Intent(getActivity(), SavedKundliListActivity.class);
                    i.putExtra("phoneNo", phoneNo);
                    i.putExtra("urlText", urlText);
                    i.putExtra("fromWhere", "call");
                    startActivityForResult(i, DashBoardActivity.BACK_FROM_PROFILECHATDIALOG);*/

                    } else {
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.ASTRO_BUSY_IN_ANOTHER_CALL,
                                AstrosageKundliApplication.currentEventType, "");
                        String msg = getResources().getString(R.string.astrologer_busy_msg);
                        callMsgDialogData(msg, "", true, CGlobalVariables.CALL_CLICK);
                    }
                }
            } else {
                CUtils.fcmAnalyticsEvents(CGlobalVariables.ASTRO_BUSY_IN_ANOTHER_CALL,
                        AstrosageKundliApplication.currentEventType, "");
                if (astrologerBusyStatus.equalsIgnoreCase("true")) {
                    String msg = getResources().getString(R.string.astrologer_busy_msg);
                    callMsgDialogData(msg, "", true, CGlobalVariables.CALL_CLICK);
                } else {
                    String stringMsg = "";
                    if ((astrologerChatStatus.equalsIgnoreCase("true")) && (astrologerCallStatus.equalsIgnoreCase("false"))) {
                        stringMsg = getResources().getString(R.string.astrologer_offline_call);
                    } else if ((astrologerChatStatus.equalsIgnoreCase("false")) && (astrologerCallStatus.equalsIgnoreCase("true"))) {
                        stringMsg = getResources().getString(R.string.astrologer_offline_chat);
                    } else {
                        stringMsg = getResources().getString(R.string.astrologer_offline_msg);
                    }
                    callMsgDialogData(stringMsg, "", true, CGlobalVariables.CALL_CLICK);
                }
            }
        }else {
            CUtils.showSnackbar(mSwipeRefreshLayout, getResources().getString(R.string.allready_in_chat), activity);
        }
    }

    @Override
    public void chatAstrologer(AstrologerDetailBean astrologerDetailBean) {

        if (astrologerDetailBean != null) {
            if (astrologerDetailBean.getIsAvailableForChat().equalsIgnoreCase("true")) {
                if (astrologerDetailBean.getIsBusy().equalsIgnoreCase("true")) {
                    String msg = getResources().getString(R.string.astrologer_busy_msg_chat);
                    //msg = msg.replace("#",astrologerDetailBean.getWaitTimeRemaining());
                    //callMsgDialogData(msg, "", true, CGlobalVariables.CHAT_CLICK);
                    CUtils.fcmAnalyticsEvents("astro_busy_chat_when_free_user_receive_notification",
                            AstrosageKundliApplication.currentEventType, "");
                    CUtils.showNotificationAlertDialog(activity,astrologerDetailBean,getChildFragmentManager());
                } else {
                    astrologerDetailBean.setCallSource(TAG);
                    CUtils.fcmAnalyticsEvents("astro_chat_method_called",
                            AstrosageKundliApplication.currentEventType, "");
                    if (getActivity() instanceof DashBoardActivity) {
                        if(CUtils.isChatNotInitiated()){
                            ChatUtils.getInstance(getActivity()).initChat(astrologerDetailBean);
                        }else {
                            CUtils.showSnackbar(mSwipeRefreshLayout, getResources().getString(R.string.allready_in_chat), getActivity());
                        }
                        //((DashBoardActivity) getActivity()).initTwilioChat(astrologerDetailBean);
                    } else if (getActivity() instanceof ActAppModule) {
                        if(CUtils.isChatNotInitiated()){
                            ChatUtils.getInstance(activity).initChat(astrologerDetailBean);
                        }else {
                            CUtils.showSnackbar(mSwipeRefreshLayout, getResources().getString(R.string.allready_in_chat), getActivity());
                        }
//                        AstrosageKundliApplication.chatAstrologerDetailBean = astrologerDetailBean;
//                        Intent intent = new Intent(getActivity(), DashBoardActivity.class);
//                        startActivity(intent);
                    }
                }
            } else {
                CUtils.fcmAnalyticsEvents("astro_busy_in_another_chat",
                        AstrosageKundliApplication.currentEventType, "");
                if (astrologerDetailBean.getIsBusy().equalsIgnoreCase("true")) {
                    String msg = getResources().getString(R.string.astrologer_busy_msg_chat);
                    callMsgDialogData(msg, "", true, CGlobalVariables.CHAT_CLICK);
                } else {
                    String stringMsg = "";
                    if ((astrologerDetailBean.getIsAvailableForChat().equalsIgnoreCase("true")) && (astrologerDetailBean.getIsAvailableForCall().equalsIgnoreCase("false"))) {
                        stringMsg = getResources().getString(R.string.astrologer_offline_call);
                    } else if ((astrologerDetailBean.getIsAvailableForChat().equalsIgnoreCase("false")) && (astrologerDetailBean.getIsAvailableForCall().equalsIgnoreCase("true"))) {
                        stringMsg = getResources().getString(R.string.astrologer_offline_chat);
                    } else {
                        stringMsg = getResources().getString(R.string.astrologer_offline_msg);
                    }
                    callMsgDialogData(stringMsg, "", true, CGlobalVariables.CHAT_CLICK);
                }
            }
        } else {
            CUtils.fcmAnalyticsEvents("astro_busy_in_another_chat",
                    AstrosageKundliApplication.currentEventType, "");
            String msg = getResources().getString(R.string.astrologer_busy_msg_chat);
            callMsgDialogData(msg, "", true, CGlobalVariables.CHAT_CLICK);
        }
    }

    public void callMsgDialogData(String message, String title, boolean isShowOkBtn, String fromWhich) {
        try {
            CallMsgDialog dialog = new CallMsgDialog(message, title, isShowOkBtn, fromWhich);
            dialog.show(getChildFragmentManager(), "Dialog");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void chatWithRandomAstrologer() {
        try {
            CUtils.popUnderFreeChatClicked = false;
            AstrologerDetailBean astrologerBean = null;
            if (allAstrologersArrayList  != null) {
                for (int i = CGlobalVariables.RANDOM_ASTROLOGER_START_COUNT; i < allAstrologersArrayList .size(); i++) {
                    //Log.d("TestisOfferRemaining"," " +arrayListRecyclerViewAllAstrologer.get(i).isOfferRemaining());
                    astrologerBean = allAstrologersArrayList .get(i);
                    if (astrologerBean.getIsAvailableForChat().equals("true") && astrologerBean.getIsBusy().equals("false") && astrologerBean.isOfferRemaining()) {
                        // Log.d("TestisOfferRemaining"," " +i);
                        break;
                    } else {
                        astrologerBean = null;
                    }
                }
            }
            if (astrologerBean != null) {
                astrologerBean.setFreeForChat(true);
                chatAstrologer(astrologerBean);
            }
        } catch (Exception e) {
            //
        }
    }

    public void callWithRandomAstrologer() {
        try {
            CUtils.popUpLoginFreeCallClicked = false;
            CUtils.popUpLoginFreeChatClicked = false;
            AstrologerDetailBean astrologerBean = null;
            if (allAstrologersArrayList != null) {
                for (int i = CGlobalVariables.RANDOM_ASTROLOGER_START_COUNT; i < allAstrologersArrayList.size(); i++) {
                    astrologerBean = allAstrologersArrayList.get(i);
                   // Log.d("TestExpertise",astrologerBean.getExpertise());
                    if (astrologerBean.getIsAvailableForCall().equals("true") && astrologerBean.getIsBusy().equals("false")&& astrologerBean.getExpertise().contains("Vedic")&& astrologerBean.isOfferRemaining()) {
                        break;
                    } else {
                        astrologerBean = null;
                    }
                }
            }
            if (astrologerBean != null) {
                astrologerBean.setFreeForCall(true);
                callAstrologer(astrologerBean);
            }
        } catch (Exception e) {
            //
        }
    }


    public void callToAstrologer(String phoneNo, String urlText) {
        showFreeOneMinDialog();
        callSelectedAstrologer(phoneNo, urlText);
    }
    private void checkCallMedium(AstrologerDetailBean astrologerDetailBean) {
        astrologerDetailBean.setCallSource(TAG);
        //if (manager == null) return;
        //if (manager.getInstalledModules().contains(com.ojassoft.astrosage.varta.utils.CGlobalVariables.MODULE_VARTA_LIVE)) {
            //init agora call
            if(CUtils.isConnectedMobile(activity)){
                if(CUtils.isNetworkSpeedSlow(activity)){
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.NETWORK_CALL_CONNECT_REASON_SPEED_SLOW,
                            AstrosageKundliApplication.currentEventType, "");
                    CUtils.openProfileOrKundliActFromFrag(activity, astrologerDetailBean.getUrlText(), "call", this);
                }else {
                    ChatUtils.getInstance(activity).initVoiceCall(astrologerDetailBean);
                }
            }else {
                ChatUtils.getInstance(activity).initVoiceCall(astrologerDetailBean);
            }
        /*} else {
            CUtils.fcmAnalyticsEvents(CGlobalVariables.NETWORK_CALL_CONNECT_REASON_PACKAGE_NOT_INSTALL,
                    AstrosageKundliApplication.currentEventType, "");
            CUtils.openProfileOrKundliActFromFrag(activity, astrologerDetailBean.getUrlText(), "call", this);

        }*/

    }
    private void showFreeOneMinDialog() {
        try {
            String offerType = CUtils.getCallChatOfferType(getActivity());
            if (!TextUtils.isEmpty(offerType) && offerType.equals(CGlobalVariables.INTRO_OFFER_TYPE_FREE)&& CUtils.getCountryCode(activity).equals("91")) {
                CUtils.fcmAnalyticsEvents("first_min_free_dialog_free_call", AstrosageKundliApplication.currentEventType,"");

                freeMinutedialog = new FreeMinuteDialog(CGlobalVariables.INTRO_OFFER_TYPE_FREE);
                freeMinutedialog.show(getChildFragmentManager(), "Dialog");
            }
            String wallet = CUtils.getWalletRs(getActivity());
            //Log.e("LoadMore wallet ", wallet);
            if ((wallet != null) && !wallet.equals("0.0")) {
                CUtils.fcmAnalyticsEvents("first_min_free_dialog", AstrosageKundliApplication.currentEventType,"");
                freeMinutedialog = new FreeMinuteDialog("");
                freeMinutedialog.show(getChildFragmentManager(), "Dialog");
            } else {

            }
        } catch (Exception e) {
            //
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DashBoardActivity.BACK_FROM_PROFILECHATDIALOG) {
            if (data != null) {
                boolean isProceed = data.getExtras().getBoolean("IS_PROCEED");
                if (isProceed) {
                    String phoneNo = data.getStringExtra("phoneNo");
                    String urlText = data.getStringExtra("urlText");
                    callToAstrologer(phoneNo, urlText);
                } else if (data.getExtras().containsKey("openKundliList")){
                    CUtils.openSavedKundliListFromFrag(activity, data.getStringExtra("urlText"),"call", this);
                } else if (data.getExtras().containsKey("openProfileForChat")){
                    boolean prefillData = true;
                    if (data.getExtras().containsKey("prefillData")){
                        prefillData = data.getBooleanExtra("prefillData", true);
                    }
                    Bundle bundle = data.getExtras();
                    CUtils.openProfileForChatFromFrag(activity, data.getStringExtra("urlText"),"call", bundle, prefillData, this);
                }
            }
        }
    }

    private void callSelectedAstrologer(String mobileNo, String urlTextt) {

        if (!CUtils.isConnectedWithInternet(getActivity())) {
            CUtils.showSnackbar(mSwipeRefreshLayout, getResources().getString(R.string.no_internet), getActivity());
        } else {
            showProgressBar();
            //Log.e("SAN LoadMore url ", CGlobalVariables.CALL_ASTROLOGER_URL);
            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.CALL_ASTROLOGER_URL,
                    VartaHomeFragment.this, false, getCallParams(mobileNo, urlTextt), CALL_METHOD).getMyStringRequest();
            stringRequest.setShouldCache(true);
            queue.add(stringRequest);
            CUtils.fcmAnalyticsEvents("connect_call_api_called", AstrosageKundliApplication.currentEventType,"");

        }
    }

    public Map<String, String> getCallParams(String phoneNo, String urlText) {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(getActivity()));
        headers.put(CGlobalVariables.USER_PHONE_NO, phoneNo);
        headers.put(CGlobalVariables.URL_TEXT, urlText);
        headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(getActivity()));
        headers.put(KEY_PASSWORD, CUtils.getUserLoginPassword(activity));
        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(activity));
        headers.put(CGlobalVariables.APP_VERSION, "" + BuildConfig.VERSION_NAME);
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
        headers.put(CGlobalVariables.CALL_SOURCE, TAG);
        String currentOfferType = CUtils.getUserIntroOfferType(activity);
        headers.put(CGlobalVariables.ISFREE_CONSULTATION, String.valueOf(isFreeConsultation));
        headers.put(CGlobalVariables.OFFER_TYPE,currentOfferType);
        isFreeConsultation = false;
        headers.put(USER_SPEED, String.valueOf(CUtils.getNetworkSpeed(activity)));
        //Log.e("SAN LoadMore params ", headers.toString());
        return headers;
    }

    public void setbookMarkedAstrologerList() {
        if (getActivity() == null) return;
        NotificationDBManager dbManager = new NotificationDBManager(getActivity());
        List<BookmarkModel> bookmarkModelList = dbManager.getBookMarkList();
        for (int i = 0; i < allAstrologersArrayList.size(); i++) {
            for (int j = 0; j < bookmarkModelList.size(); j++) {
                if (allAstrologersArrayList.get(i).getAstrologerId().equals(bookmarkModelList.get(j).getAstrologerId())) {
                    allAstrologersArrayList.get(i).setAstrologerBookmarked(true);
                }
            }
        }
    }

    public ArrayList<AstrologerDetailBean> getCallAvailableAstrologerList() {
        if (callAstrologersArrayList == null) {
            callAstrologersArrayList = new ArrayList<>();
        } else {
            callAstrologersArrayList.clear();
        }
        if (allAstrologersArrayList != null) {
            for (int i = 0; i < allAstrologersArrayList.size(); i++) {
                if (allAstrologersArrayList.get(i).getIsAvailableForCall().equals("true")) {
                    if(callAstrologersArrayList.size() < CGlobalVariables.CALL_CHAT_ASTRO_GRID_LIMIT) {
                        callAstrologersArrayList.add(allAstrologersArrayList.get(i));
                    }else {
                        break;
                    }
                }
            }
        }
        return callAstrologersArrayList;
    }

    public ArrayList<AstrologerDetailBean> getChatAvailableAstrologerList() {
        if (chatAstrologersArrayList == null) {
            chatAstrologersArrayList = new ArrayList<>();
        } else {
            chatAstrologersArrayList.clear();
        }
        if (allAstrologersArrayList != null) {
            for (int i = 0; i < allAstrologersArrayList.size(); i++) {
                if (allAstrologersArrayList.get(i).getIsAvailableForChat().equals("true")) {
                    if(chatAstrologersArrayList.size() < CGlobalVariables.CALL_CHAT_ASTRO_GRID_LIMIT) {
                        chatAstrologersArrayList.add(allAstrologersArrayList.get(i));
                    }else {
                        break;
                    }
                }
            }
        }
        return chatAstrologersArrayList;
    }

    public ArrayList<AstrologerDetailBean> getChatAvailableNotBusy() {
        ArrayList<AstrologerDetailBean> arrayList = new ArrayList<>();
        try {
            if (allAstrologersArrayList != null) {
                for (int i = 0; i < allAstrologersArrayList.size(); i++) {
                    AstrologerDetailBean bean = allAstrologersArrayList.get(i);
                    if (bean.getIsAvailableForChat().equals("true") && bean.getIsBusy().equals("false")) {
                        arrayList.add(allAstrologersArrayList.get(i));
                    }
                }
            }
        } catch (Exception e) {
            //
        }
        return arrayList;
    }

    private void checkNextRechargeOffer(String offerType) {
        try {

            if (activity == null) return;
            if (activity instanceof DashBoardActivity){
                if(((DashBoardActivity) activity).showDialogOnce){
                    return;
                }
            }

            if (!CUtils.getUserLoginStatus(activity)) {
                return;
            }
            ((DashBoardActivity) activity).showDialogOnce = true;
            if (TextUtils.isEmpty(CUtils.getPrefNextOffer(activity)) || !CUtils.isSameDay(CUtils.getPrefNextOffer(activity), CUtils.getCurrentDateInString())) {
                if (!TextUtils.isEmpty(offerType) && offerType.equals(CGlobalVariables.INTRO_OFFER_TYPE_FREE)) {
                    if (activity instanceof DashBoardActivity) {
                        boolean isDontShowBottomPopUnderDialog = ((DashBoardActivity) activity).isDontShowBottomPopUnderDialog;
                        if (!isDontShowBottomPopUnderDialog)
                            ((DashBoardActivity) activity).callFreeChatPopUnderDialog();
                    }
                } else {
                    if (activity instanceof DashBoardActivity) {
                        ((DashBoardActivity) activity).getNextRechargeFromApi();
                    }
                }
            }
        } catch (Exception e) {

        }
    }

    public void getLiveAstrologerModelAndJoinLiveSession(String urlText) {
        try {
            astroUrlForLiveJoin = urlText;
            LiveAstrologerModel liveAstrologerModel = getLiveAstrologerModelFromList(urlText);
            if (liveAstrologerModel != null) {
                IS_LIVE_SESSION = false;
                openLiveStramingScreen();
            } else {
                IS_LIVE_SESSION = true;
                StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.GET_LIVE_ASTRO_URL,
                        VartaHomeFragment.this, false, CUtils.getLiveAstroParams(activity, CUtils.getActivityName(activity)), FETCH_LIVE_ASTROLOGER).getMyStringRequest();
                queue.add(stringRequest);
            }
        } catch (Exception e) {

        }
    }

    /**
     * match url-text in live astrologer list, if available then join live otherwise open astrologer details page.
     */
    private void openLiveStramingScreen() {
        if (TextUtils.isEmpty(astroUrlForLiveJoin)) {
            return;
        }
        try {
            LiveAstrologerModel liveAstrologerModel = getLiveAstrologerModelFromList(astroUrlForLiveJoin);
            if (liveAstrologerModel != null) {
                //Log.d("MyTestingData"," AstrosageKundliApplication.liveActivityVisible"+  AstrosageKundliApplication.liveActivityVisible);

                if(AstrosageKundliApplication.liveActivityVisible){
                   // Log.d("MyTestingData","openLiveStramingScreen Varta Frag Called");

                    Intent intent = new Intent(CGlobalVariables.SEND_BROADCAST_OPEN_LIVE_SCREEN_FROM_NOTIFICATION);
                    intent.putExtra(CGlobalVariables.ASTROLOGER_DATA,liveAstrologerModel);
                    LocalBroadcastManager.getInstance(activity).sendBroadcast(intent);
                }else {
                    if (activity instanceof DashBoardActivity) {
                        ((DashBoardActivity) activity).checkPermissions(liveAstrologerModel);
                    } else if (activity instanceof ActAppModule) {
                        ((ActAppModule) activity).checkPermissions(liveAstrologerModel);
                    }
                }

            } else {
                CUtils.openAstrologerDetail(activity, astroUrlForLiveJoin, true, false,"");
            }

        } catch (Exception e) {
            //
        }
        IS_LIVE_SESSION = false;
        astroUrlForLiveJoin = "";
    }

    /**
     * @param urlText
     * @return LiveAstrologerModel
     */
    private LiveAstrologerModel getLiveAstrologerModelFromList(String urlText) {
        try {
            if (TextUtils.isEmpty(urlText)) {
                return null;
            }
            if (liveAstrologersArrayList != null) {
                for (int i = 0; i < liveAstrologersArrayList.size(); i++) {
                    LiveAstrologerModel liveAstrologerModel = liveAstrologersArrayList.get(i);
                    if (liveAstrologerModel == null) continue;
                    if (urlText.equals(liveAstrologerModel.getUrltext())) {
                        return liveAstrologerModel;
                    }

                }
            }
        } catch (Exception e) {
            //
        }
        return null;
    }

    private void hideSwipeRefreshProgressBar(){
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
            mSwipeRefreshLayout.setEnabled(true);
        }
    }

    public void showLogoProgressbar() {
        try {
            llGifProgress.setVisibility(View.VISIBLE);
            Glide.with(getActivity().getApplicationContext()).load(R.drawable.new_ai_loader).into(logoGif);
        } catch (Exception e) {
            //
        }

    }

    public void hideLogoProgressBar() {
        try {
            llGifProgress.setVisibility(View.GONE);
        } catch (Exception e) {
            //
        }
    }

    public void showProgressBar() {
        try {
            if (pd == null) {
                pd = new CustomProgressDialog(activity);
                pd.setCancelable(false);
            }
            pd.show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void hideProgressBar() {
        try {
            if (pd != null && pd.isShowing())
                pd.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onDestroy() {
        try {
            if (liveAstroReceiver != null) {
                LocalBroadcastManager.getInstance(activity).unregisterReceiver(liveAstroReceiver);
            }
        } catch (Exception e) {
            //
        }
        super.onDestroy();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llVartaHomeKundli:
                CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_VARTA_HOME_KUNDLI, FIREBASE_EVENT_ITEM_CLICK, "");
                Intent intent = new Intent(activity, HomeInputScreen.class);
                intent.putExtra(com.ojassoft.astrosage.utils.CGlobalVariables.MODULE_TYPE_KEY,
                        com.ojassoft.astrosage.utils.CGlobalVariables.MODULE_BASIC);
                activity.startActivity(intent);
                break;
            case R.id.llVartaHomeMatching:
                CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_VARTA_HOME_MATCHING, FIREBASE_EVENT_ITEM_CLICK, "");
                activity.startActivity(new Intent(activity, HomeMatchMakingInputScreen.class));
                break;
            case R.id.llVartaHomeHoroscope:
                CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_VARTA_HOME_HOROSCOPE, FIREBASE_EVENT_ITEM_CLICK, "");
                callHoroscopeActivity(activity, MODULE_HOROSCOPE, 0, 0);
                break;
            case R.id.llVartaHomeFreeChat:

                // commet by vishal
                /*
                if (CUtils.getUserLoginStatus(activity)) {
                    if (CUtils.getUserIntroOfferType(activity) != null && CUtils.getUserIntroOfferType(activity).equals(CGlobalVariables.INTRO_OFFER_TYPE_FREE)) {
                        AstrosageKundliApplication.currentEventType = CGlobalVariables.CHAT_BTN_CLICKED;
                        CUtils.fcmAnalyticsEvents("varta_home_free_chat_icon", AstrosageKundliApplication.currentEventType, "");

                        chatWithRandomAstrologer();
                    } else {
                        openCallChatAstrologers(FILTER_TYPE_CHAT);
                    }
                } else {
                    Intent loginIntent = new Intent(getContext(), LoginSignUpActivity.class);
                    loginIntent.putExtra(CGlobalVariables.IS_FROM_SCREEN, CGlobalVariables.DASHBOARD_CALL_NOW_SCRREN);
                    startActivity(loginIntent);
                }*/

                openCallChatAstrologers(FILTER_TYPE_CHAT);

                break;
            case R.id.tvVartaHomeLiveSeeMore:
                activity.startActivity(new Intent(activity, AllLiveAstrologerActivity.class));
                break;
            case R.id.tvVartaHomeCallSeeMore:
                openCallChatAstrologers(FILTER_TYPE_CALL);
                break;
            case R.id.tvVartaHomeChatSeeMore:
                openCallChatAstrologers(FILTER_TYPE_CHAT);
                break;
            case R.id.tvVartaHomeRegionalSeeMore:
                CUtils.expertiseFilterType = -1;
                CUtils.regionalFilterType = LANGUAGE_CODE;
                openCallChatAstrologers(FILTER_TYPE_AVAILABLE);
                break;
            case R.id.tvVartaHomeLastConsultSeeMore:
                activity.startActivity(new Intent(activity, ConsultantHistoryActivity.class));
                break;
            /*case R.id.tvVartaHomeKPSeeMore:
                CUtils.expertiseFilterType = 2;
                openCallChatAstrologers(FILTER_TYPE_AVAILABLE);
                break;
            case R.id.tvVartaHomeTarotSeeMore:
                CUtils.expertiseFilterType = 6;
                openCallChatAstrologers(FILTER_TYPE_AVAILABLE);
                break;*/
        }
    }

    private void setAutoRotateBanner() {
        final int speedScroll = 3000;
        final Handler handler = new Handler();
        final Runnable runnable = new Runnable() {
            int count = 0;
            boolean flag = true;

            @Override
            public void run() {
                if (count < vartaHomeBannerAdapter.getItemCount()) {
                    if (count == vartaHomeBannerAdapter.getItemCount() - 1) {
                        flag = false;
                    } else if (count == 0) {
                        flag = true;
                    }
                    if (flag) count++;
                    else count--;

                    rvVartaHomeBanner.smoothScrollToPosition(count);
                    handler.postDelayed(this, speedScroll);
                }
            }
        };

        handler.postDelayed(runnable, speedScroll);
    }

    public void openCallChatAstrologers(int filterType) {
        if (activity instanceof ActAppModule) {
            openVartaDashboardActivity(filterType);
        } else {
            openAstrologerListFragment(filterType, activity);
        }
    }

    public void openAstrologerListFragment(int filterType, Activity activity) {
        try {
            CUtils.astroListFilterType = filterType;
            if (activity == null || (activity instanceof ActAppModule)){
                activity = DashBoardActivity.getMainActivity();
            }
            ((DashBoardActivity)activity).openWantedFragment();
            /*((DashBoardActivity)activity).setBottomNavigationText(CUtils.getUserLoginStatus(activity));
            HomeFragment1 fragment = HomeFragment1.newInstance();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.nav_host_fragment, fragment, "HOMEFRAGMENT");
            transaction.addToBackStack(null);
            transaction.commit();

            HomeFragment1 fragment2 = (HomeFragment1) getActivity().getSupportFragmentManager().findFragmentByTag("HOMEFRAGMENT");
            if (fragment2 != null && fragment2.isAdded()){
                fragment2.getData(CUtils.astroListFilterType);
            }*/

        } catch (Exception e) {
            //Log.d("testingLog","varta ex-->"+e);
        }
    }

    private void openVartaDashboardActivity(int filterType) {
        try {
            CUtils.switchToConsultTab(filterType,activity);
            /*Intent intent = new Intent(activity, DashBoardActivity.class);
            intent.putExtra(KEY_FILTER_TYPE, filterType);
            startActivity(intent);*/
        } catch (Exception e) {
            //Log.d("testingLog","2. activity --> "+ activity +" === Exception --> "+ e);
        }
    }

    private void getLastConsultDataFromServer() {
        if (CUtils.isConnectedWithInternet(activity)) {
            String url = CGlobalVariables.CONSULTATIONHISTORY;
            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, url,
                    VartaHomeFragment.this, false, getParamsNew(), FETCH_LAST_CONSULTATIONS).getMyStringRequest();
            stringRequest.setShouldCache(false);
            queue.add(stringRequest);
        }
    }

    public Map<String, String> getParamsNew() {
        HashMap<String, String> headers = new HashMap<String, String>();
        try {
            headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(activity));
            headers.put(CGlobalVariables.USER_PHONE_NO, CUtils.getUserID(activity));
            headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(activity));
            headers.put(CGlobalVariables.LANG,CUtils.getLanguageKey(LANGUAGE_CODE));
            headers.put(CGlobalVariables.IGNORE_ASTRO,"true");

        } catch (Exception e) {

        }
        return headers;
    }

    private void parseLastConsultList(String lastConsultData) {
           Log.d("testhistory", lastConsultData);
        if (TextUtils.isEmpty(lastConsultData)) {
            return;
        }

        if(callHistoryList != null){
            callHistoryList.clear();
        }

        try {
            JSONArray consultations = null;
            CallHistoryBean callHistoryBean;
            JSONArray chats = null;
            JSONObject jsonObject = new JSONObject(lastConsultData);

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
                        callHistoryBean.setType("Call");
                        callHistoryBean.setDurationUnitType(durationUnitType);
                        callHistoryBean.setCallDurationMin(callDurationMin);


                        callHistoryList.add(callHistoryBean);
                    }
                }
            }
            if(jsonObject.has("consultations")){
                consultations = jsonObject.getJSONArray("consultations");

                if(consultations != null && consultations.length()>0) {
                    for (int i = 0; i < consultations.length(); i++) {
                        callHistoryBean = new CallHistoryBean();
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
                        callHistoryBean.setType("Call");
                        callHistoryBean.setDurationUnitType(durationUnitType);
                        callHistoryBean.setCallDurationMin(callDurationMin);


                        callHistoryList.add(callHistoryBean);
                    }
                }
            }
            if(jsonObject.has("chats")) {
                Log.d("testhistory","Called w8574532fdt");
                chats = jsonObject.getJSONArray("chats");
                if(chats != null && chats.length()>0) {
                    for (int i = 0; i < chats.length(); i++) {

                        callHistoryBean = new CallHistoryBean();
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
                        String durationUnitType = chats.getJSONObject(i).optString("durationUnitType");
                        String callDurationMin = chats.getJSONObject(i).optString("calldurationmin");



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
                        callHistoryBean.setType("Chat");
                        callHistoryBean.setDurationUnitType(durationUnitType);
                        callHistoryBean.setCallDurationMin(callDurationMin);


                        callHistoryList.add(callHistoryBean);
                    }
                }
            }
            if(jsonObject.has("livesessions")) {
                consultations = jsonObject.getJSONArray("livesessions");
                if(consultations != null && consultations.length()>0) {
                    for (int i = 0; i < consultations.length(); i++) {
                        callHistoryBean = new CallHistoryBean();
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
                        callHistoryBean.setType("Call");
                        callHistoryBean.setDurationUnitType(durationUnitType);
                        callHistoryBean.setCallDurationMin(callDurationMin);


                        callHistoryList.add(callHistoryBean);
                    }
                }
            }
            if (callHistoryList.size()>0) {
                Collections.sort(callHistoryList);
                if (lastConsultAdapter == null) {
                    lastConsultAdapter = new LastConsultAdapter(getActivity(), callHistoryList, 2);
                    rvVartaHomeLastConsult.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false));
                    rvVartaHomeLastConsult.setAdapter(lastConsultAdapter);
                } else {
                    lastConsultAdapter.notifyDataSetChanged();
                }
            }
            hideShowLastConsultation(callHistoryList);
        } catch (Exception e) {
            //Log.e("redirectToLink", e.toString());
        }
    }

    public void hideShowLastConsultation(ArrayList<CallHistoryBean> list) {
        try {
            if (list != null && !list.isEmpty()) {
                tvVartaHomeLastConsultHeading.setVisibility(View.VISIBLE);
                tvVartaHomeLastConsultSeeMore.setVisibility(View.VISIBLE);
                rvVartaHomeLastConsult.setVisibility(View.VISIBLE);
            } else {
                tvVartaHomeLastConsultHeading.setVisibility(View.GONE);
                tvVartaHomeLastConsultSeeMore.setVisibility(View.GONE);
                rvVartaHomeLastConsult.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            //
        }
    }


    public void responseNetworkCall(String response) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            String status = jsonObject.getString("status");
            String message = jsonObject.getString("msg");
            if (status.equals(CGlobalVariables.CALL_INITIATED)) {
                // {"status":"1", "msg":"Call has been initiated.", "callsid" : "86b9a4fa6c4c4be1f9dae402274d1479", "talktime":"4"}
                final String callsId = jsonObject.getString("callsid");
                String talkTime = jsonObject.getString("talktime");
                final String exophoneNo = jsonObject.getString("exophone");
                final String internationalCharges = jsonObject.getString("callcharge");

                if (freeMinutedialog != null && freeMinutedialog.isVisible()) {
                    CUtils.fcmAnalyticsEvents("show_call_thank_you_dialog",AstrosageKundliApplication.currentEventType,"");
                    openFreeMinDialogBox();
                }
                CUtils.fcmAnalyticsEvents("show_call_initiate_dialog",AstrosageKundliApplication.currentEventType,"");

                // Free offer taken event
                        /*
                        if (isFreeConsultation) {
                            if(!TextUtils.isEmpty(offerType) && offerType.equals(CGlobalVariables.INTRO_OFFER_TYPE_FREE)){
                                CUtils.fcmAnalyticsEvents(FREE_OFFER_TAKEN,CALL_CLICK,"");
                                CUtils.saveFirstFreeOfferTakenDate(getActivity(), DateTimeUtils.currentDate());
                            }else if(!TextUtils.isEmpty(offerType) && offerType.equals(CGlobalVariables.REDUCED_PRICE_OFFER)){
                                CUtils.fcmAnalyticsEvents(REDUCE_PRICE_OFFER_TAKEN,CALL_CLICK,"");
                                CUtils.saveReducePriceOfferTakenDate(getActivity(), DateTimeUtils.currentDate());
                            }
                        }*/
                callInitiatedDialog = new CallInitiatedDialog(callsId, talkTime, exophoneNo, CGlobalVariables.CALL_CLICK, internationalCharges,callingAstroName,callingAstroProfileUrl);
                callInitiatedDialog.show(getChildFragmentManager(), "Dialog");

                try {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                CUtils.fcmAnalyticsEvents("start_call_status_service",AstrosageKundliApplication.currentEventType,"");
                                Intent intent = new Intent(getActivity(), CallStatusCheckService.class);
                                intent.putExtra(CGlobalVariables.CALLS_ID, callsId);
                                getActivity().startService(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }, 60000);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (status.equals(CGlobalVariables.FREE_CONSULTATION_NOT_AVAILABLE)) {
                CUtils.fcmAnalyticsEvents("call_api_res_free_consultation_not_available", AstrosageKundliApplication.currentEventType, "");
                String dialogMsg = activity.getResources().getString(R.string.unable_to_connect_free_consultation);
                callMsgDialogData(dialogMsg, "", true, CGlobalVariables.CHAT_CLICK);
            } else if (status.equals(CGlobalVariables.INSUFFICIENT_BALANCE)) {
                String astrologerName = jsonObject.getString("astrologername");
                String userbalance = jsonObject.getString("userbalance");
                String minBalance = jsonObject.getString("minbalance");
                CUtils.fcmAnalyticsEvents("call_insufficient_bal_dialog",AstrosageKundliApplication.currentEventType,"");

                InSufficientBalanceDialog dialog = new InSufficientBalanceDialog(astrologerName, userbalance, minBalance, CGlobalVariables.CALL_CLICK);
                dialog.show(getChildFragmentManager(), "Dialog");
            } else if (status.equals(CGlobalVariables.ASTROLOGER_BUSY_OFFLINE)) {
                String  fcm_label = "call_astrologer_"+message;
                CUtils.fcmAnalyticsEvents(fcm_label,AstrosageKundliApplication.currentEventType,"");
                String dialogMsg = "";
                if (message.equalsIgnoreCase(CGlobalVariables.USER_BLOCKED_MSG)) {
                    dialogMsg = getActivity().getResources().getString(R.string.user_blocked_msg);
                } else if (message.equalsIgnoreCase(CGlobalVariables.ASTROLOGER_STATUS_BUSY_MSG)) {
                    dialogMsg = getActivity().getResources().getString(R.string.astrologer_busy_msg);
                } else if (message.equalsIgnoreCase(CGlobalVariables.ASTROLOGER_STATUS_OFFLINE_MSG)) {
                    dialogMsg = getActivity().getResources().getString(R.string.astrologer_offline_msg);

                } else if (message.equalsIgnoreCase(CGlobalVariables.ASTROLOGER_STATUS_DISABLED)) {
                    dialogMsg = getResources().getString(R.string.astrologer_status_disable);
                } else if (message.equalsIgnoreCase(CGlobalVariables.CALL_API_FAILED)) {
                    dialogMsg = getResources().getString(R.string.call_api_failed_msg);
                } else if (message.equalsIgnoreCase(CGlobalVariables.USE_ANOTHER_CALL_MSG)) {
                    dialogMsg = getActivity().getResources().getString(R.string.existing_call_msg);
                } else {
                    dialogMsg = getActivity().getResources().getString(R.string.something_wrong_error);
                }
                callMsgDialogData(dialogMsg, "", true, CGlobalVariables.CALL_CLICK);
                //CUtils.showSnackbar(containerLayout,getResources().getString(R.string.astrologer_busy_msg), getActivity());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}