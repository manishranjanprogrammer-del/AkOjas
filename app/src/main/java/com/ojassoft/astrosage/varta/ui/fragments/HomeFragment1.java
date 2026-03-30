package com.ojassoft.astrosage.varta.ui.fragments;

import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_PASSWORD;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.CALL_CLICK;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.FILTER_TYPE_ALL;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.USER_SPEED;
import static com.ojassoft.astrosage.varta.utils.CUtils.allAstrologersArrayList;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.RecyclerView;
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
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.varta.adapters.CallChatCategoryAdapter;
import com.ojassoft.astrosage.varta.adapters.CustomAdapter;
import com.ojassoft.astrosage.varta.dao.NotificationDBManager;
import com.ojassoft.astrosage.varta.dialog.CallInitiatedDialog;
import com.ojassoft.astrosage.varta.dialog.CallMsgDialog;
import com.ojassoft.astrosage.varta.dialog.FreeMinuteDialog;
import com.ojassoft.astrosage.varta.dialog.FreeMinuteMinimizeDialog;
import com.ojassoft.astrosage.varta.dialog.InSufficientBalanceDialog;
import com.ojassoft.astrosage.varta.dialog.NewFilterBottomDialog;
import com.ojassoft.astrosage.varta.interfacefile.LoadMoreList;
import com.ojassoft.astrosage.varta.model.AstrologerDetailBean;
import com.ojassoft.astrosage.varta.model.BookmarkModel;
import com.ojassoft.astrosage.varta.model.CategoryModel;
import com.ojassoft.astrosage.varta.service.CallStatusCheckService;
import com.ojassoft.astrosage.varta.ui.activity.AllLiveAstrologerActivity;
import com.ojassoft.astrosage.varta.ui.activity.DashBoardActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.ChatUtils;
import com.ojassoft.astrosage.varta.utils.CustomProgressDialog;
import com.ojassoft.astrosage.varta.volley_network.GZipRequest;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;
import com.ojassoft.astrosage.varta.volley_network.VolleyServiceHandler;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;
import com.ojassoft.astrosage.vartalive.widgets.WrapContentLinearLayoutManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * HomeFragment1 - Main fragment for displaying and managing the list of astrologers in the Varta module.
 *
 * <p>
 * Responsibilities:
 * <ul>
 *   <li>Displays astrologer list with filtering, sorting, and category selection</li>
 *   <li>Handles call/chat initiation with astrologers</li>
 *   <li>Manages offer types, free consultations, and analytics events</li>
 *   <li>Integrates with both Retrofit and Volley for network operations</li>
 *   <li>Handles UI state for loading, errors, and empty lists</li>
 *   <li>Implements filter dialogs and manages filter state</li>
 *   <li>Manages permissions for audio recording (calls)</li>
 *   <li>Handles broadcast events for chat refunds</li>
 * </ul>
 *
 * <p>
 * This fragment is central to the Varta experience, providing a rich set of features for users to discover, filter, and interact with astrologers.
 *
 * <p>
 * Note: This file contains extensive logic and UI management. Only documentation and comments have been added as per the request.
 */
public class HomeFragment1 extends Fragment implements VolleyResponse, LoadMoreList, CallChatCategoryAdapter.CategoryClickCallbacks {


    public static final String TAG = "HomeFragment1";
    String[] expertiseID = {"Expertise", "Vedic", "Kp", "Lal Kitab",
            "Vastu", "Numerology", "Tarot", "Reiki", "Feng Shui", "Horary"};

    private static final String ARG_PARAM1 = "filterType";
    public static boolean isBookMarkedCBChecked = false;
    private static HomeFragment1 instance;
    private final int CALL_METHOD = 22;
    private final int GET_FILTER_LIST = 23;

    private final String TYPE_ECONOMIC = "Economic";
    private final String TYPE_PREMIUM = "Premium";
    private final String TYPE_LOW_TO_HIGH = "P_Economic";
    private final String TYPE_HIGH_TO_LOW = "P_Premium";

    private final String TYPE_POPULAR = "Popular";
    private final String TYPE_MOSTRATED = "Most Rated";
    private final String MARRIAGE = "Marriage";

    private final String TYPE_CALL = "Call";
    private final String TYPE_CHAT = "Chat";
    private final String TYPE_LIVE = "Live";

    private String TYPE_TAB_FILTER = "";

    ArrayList<CategoryModel> categoryArr = new ArrayList<>();
    List<String> catIdList = Arrays.asList("0", "1", "2", "3", "4", "5", "6", "7", "8", "9");

    private CallChatCategoryAdapter filterAstrologerAdapter;
    private Activity activity;
    private FreeMinuteMinimizeDialog freeMinuteMinimizeDialog;
    private FreeMinuteDialog freeMinutedialog;
    private CallInitiatedDialog callInitiatedDialog;
    private TextView headingTxt;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView filterAstroRecyclerView;
    private LinearLayout containerLayout;
    private WrapContentLinearLayoutManager mLayoutManager;
    private RequestQueue queue;
    private CustomAdapter astrologerAdapter;
    private CustomProgressDialog pd;
    private ArrayList<AstrologerDetailBean> arrayListRecyclerViewAllAstrologer;
    private ArrayList<AstrologerDetailBean> arrayListRecyclerView;
    private ArrayList<AstrologerDetailBean> filteredarrayList;
    public static List<String> filterList;
    public static List<String> expertiseFilterList = new ArrayList<>();
    public static List<String> langFilterList = new ArrayList<>();
    private boolean isFilterApply = false;
    private ImageView logoGif;
    private LinearLayout llGifProgress;
    private boolean isFreeConsultation;
    private String callingAstroName = "", callingAstroProfileUrl = "";
    private String offerType = "";
    public int tempFilterType = 0;

    int randomNumber = 0;
    Boolean isTabRefresh = false;
    //private SplitInstallManager manager;
    private LinearLayout llNoAstroFound, llReloadData;
    public int categoryPosition = 0;
    String offerIntroType;
    private String isPriceFilter = "0";

    /**
     * Default constructor for HomeFragment1.
     */
    @SuppressLint("ValidFragment")
    public HomeFragment1() {

    }

    /**
     * Creates a new instance of HomeFragment1 with the specified filter type.
     *
     * @param param1 The filter type to apply
     * @return A new HomeFragment1 fragment instance
     */
    public static HomeFragment1 newInstance(String param1) {
       // Clear the static filterList to prevent unwanted filters
        // from persisting across fragment recreations.
        if (filterList != null) {
            filterList.clear();
        }

        HomeFragment1 fragment = new HomeFragment1();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Returns the singleton instance of HomeFragment1.
     *
     * @return The HomeFragment1 instance
     */
    public static HomeFragment1 getInstance() {
        if (instance == null) {
            instance = new HomeFragment1();
        }
        return instance;
    }

    /**
     * Fragment lifecycle: Called when the fragment is attached to its activity.
     *
     * @param activity The parent activity
     */
    @Override
    public void onAttach(@NonNull Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    /**
     * Fragment lifecycle: Called to do initial creation of the fragment.
     * Initializes request queue and registers broadcast receiver.
     *
     * @param savedInstanceState Saved state
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.d("TestStatus", "onCreate");
        //Log.e("SAN CHAT ", " HomeFragment1 onCreate() " );
        instance = this;
        queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
        registerReceiverRefundFreeChat();
        // Retrieve the price filter setting from preferences.
        isPriceFilter = com.ojassoft.astrosage.varta.utils.CUtils.getStringData(
                requireContext(),
                com.ojassoft.astrosage.utils.CGlobalVariables.IS_PRICE_FILTER,
                "0"
        );
        /*if(getActivity()!=null){
            manager = SplitInstallManagerFactory.create(getActivity());
        }*/
        //  fetchAllAstrologerDataWithoutGzip();
    }

    /**
     * Fragment lifecycle: Creates and returns the view hierarchy associated with the fragment.
     *
     * @param inflater LayoutInflater
     * @param container ViewGroup container
     * @param savedInstanceState Saved state
     * @return The root view for the fragment
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home1, container, false);
        return view;
    }

    /**
     * Fragment lifecycle: Called immediately after onCreateView().
     * Initializes UI components and data.
     *
     * @param view The root view returned by onCreateView
     * @param savedInstanceState Saved state
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AstrosageKundliApplication.astroListLogs = "";
        //Log.d("TestStatus", "onViewCreated");
        astrologerAdapter = null;

        if (filterList != null) {
            filterList.clear();
        }

        expertiseFilterList.clear();
        langFilterList.clear();
        TYPE_TAB_FILTER = "";
        isTabRefresh = false;
        initView(view);
        setAstrologerFilter();
        initData();
    }

    /**
     * Fragment lifecycle: Called when the fragment is visible to the user and actively running.
     * Handles UI refresh and triggers data loading as needed.
     */
    @Override
    public void onResume() {
        super.onResume();
        //Log.d("TestStatus", "onResume");
        isFilterApply = false;
        //TYPE_TAB_FILTER = "";
        try {
            if (activity != null && activity instanceof DashBoardActivity) {
                ((DashBoardActivity) activity).actionOnResume();
            }
        } catch (Exception e) {
            //
        }
        try {
            if (CUtils.astroListFilterType == FILTER_TYPE_ALL) {
                //Log.e("SAN ", " HomeFragment1 oResume() call dashboard openFragment ");
                ((DashBoardActivity) activity).openWantedFragment();
            } else {
                //Log.e("SAN CHAT ", " HomeFragment1 oResume() calling getData() if (filterList.isEmpty()) " );
                if (filterList.isEmpty())
                    getData(CUtils.astroListFilterType);
            }
        } catch (Exception e) {
            //
        }
    }

    /**
     * Loads astrologer data based on the current filter type.
     * Handles cache, refresh, and triggers network calls as needed.
     *
     * @param tempFilter The filter type to apply
     */
    public void getData(int tempFilter) {
        isTabRefresh = false;
        try {
            // isFilterApply = false;
            tempFilterType = tempFilter;
            arrayListRecyclerViewAllAstrologer.clear();
            arrayListRecyclerView.clear();
            //Log.d("sharepdftest", "isEndChatCompleted="+AstrosageKundliApplication.isEndChatCompleted);
            if (AstrosageKundliApplication.isEndChatCompleted) { //return after chat completed then reload list
                Log.d("sharepdftest", "getAstrologerListFromServer() called");
                AstrosageKundliApplication.isEndChatCompleted = false;
                if(CUtils.aiAstrologersArrayList != null){
                    CUtils.aiAstrologersArrayList.clear();
                }
                getAstrologerListFromServer(false);
                return;
            }

            Log.d("sharepdftest", "allAstrologersArrayList: " + allAstrologersArrayList);
            if (allAstrologersArrayList != null && !allAstrologersArrayList.isEmpty()) {
//                recyclerView.scrollToPosition(0);
                arrayListRecyclerViewAllAstrologer.addAll(allAstrologersArrayList);
                updateCallChatAvailableAstrologer();
                Log.d("sharepdftest", "AstrosageKundliApplication.astroGroup: " + AstrosageKundliApplication.astroGroup);
                if (TextUtils.isEmpty(AstrosageKundliApplication.astroGroup)) {
                    getAstrologerListFromServer(false);
                } else {
                    //Log.e("SAN CHAT ", " HomeFragment1 getData() calling getAstrologersStatusPrice()" );
                    getAstrologersStatusPrice();
                }
            } else {
                updateAstroListAdapterNew();
                //Log.e("SAN CHAT ", " HomeFragment1 getData() calling getAstrologerListFromServer()" );
                getAstrologerListFromServer(false);
            }
        } catch (Exception e) {
            try {
                Toast.makeText(activity, "Exception1=" + e, Toast.LENGTH_LONG).show();
            } catch (Exception e1) {
                //
            }
        }
    }

    /**
     * Updates the astrologer adapter with the latest data and manages UI state for reload/no data.
     */
    private void updateAstroListAdapterNew() {
        if (!arrayListRecyclerView.isEmpty()) {
            llReloadData.setVisibility(View.GONE);
            llNoAstroFound.setVisibility(View.GONE);
        }

        if (astrologerAdapter != null) {
          listTypeToShow(arrayListRecyclerViewAllAstrologer);
        }
    }


    /**
     * Updates the adapter with a filtered list of astrologers based on price and offer type.
     *
     * @param listToFilter The list of astrologers to filter
     */
    private void listTypeToShow(ArrayList<AstrologerDetailBean> listToFilter){
        if (isPriceFilter.equals("1")) {
            if (categoryPosition == 0 && !isFilterApply) {
                // Show astrologers with price above 20
                astrologerAdapter.updateListData(getListAbove20Price(listToFilter));
            } else {
                astrologerAdapter.updateListData(listToFilter);

            }
        }else {
            astrologerAdapter.updateListData(listToFilter);

        }
    }
    /**
     * Processes the list of astrologers by ensuring that the astrologer at position 0 is always included.
     * If the offer type is neither free nor reduced-price and the price filter is enabled, it further filters
     * the remaining astrologers, adding only those with an actual service price greater than 20.
     *
     * @param arrayListRecyclerViewAllAstrologer the complete list of astrologers
     */
    private ArrayList<AstrologerDetailBean> getListAbove20Price(ArrayList<AstrologerDetailBean> arrayListRecyclerViewAllAstrologer) {

        // Create a new list to hold the filtered astrologers.
        ArrayList<AstrologerDetailBean> arrayListAbove20 = new ArrayList<>();
        String userOfferType = CUtils.getUserIntroOfferType(getContext());
        // Only process further if the offerType is not free or reduced-price.
//        if (!userOfferType.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.INTRO_OFFER_TYPE_FREE) &&
//                !offerType.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.REDUCED_PRICE_OFFER)) {
                if(TextUtils.isEmpty(userOfferType)){
                boolean isLoggedIn = CUtils.getUserLoginStatus(requireContext());
                int startIndex = 0;

                if (isLoggedIn && !arrayListRecyclerViewAllAstrologer.isEmpty()) {
                    //add last consulted astrologer to the list on top
                    AstrologerDetailBean lastConsultedAstrologer = arrayListRecyclerViewAllAstrologer.get(0);
                    arrayListAbove20.add(lastConsultedAstrologer);
                    // set the start index to 1 to skip the first element
                    startIndex = 1;
                }

                for (int i = startIndex; i < arrayListRecyclerViewAllAstrologer.size(); i++) {
                    AstrologerDetailBean astrologerBean = arrayListRecyclerViewAllAstrologer.get(i);
                    int actualPrice = astrologerBean.getActualServicePriceInteger();
                    int servicePrice = astrologerBean.servicePriceInt();
                    if (actualPrice > 20 && servicePrice > 20) {
                        arrayListAbove20.add(astrologerBean);
                    }
                }
                // Further processing with arrayListAbove20 can be added here.
                arrayListRecyclerViewAllAstrologer.clear();
                arrayListRecyclerViewAllAstrologer.addAll(arrayListAbove20);

        }
        // If the offerType is free or reduced-price, or if the price filter is not enabled then return the original list.
        return arrayListRecyclerViewAllAstrologer;

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    ProgressBar progressBar;

    private void initView(View root) {
        progressBar = root.findViewById(R.id.progressBar);
        headingTxt = root.findViewById(R.id.heading_txt);
        filterAstroRecyclerView = root.findViewById(R.id.filterAstroRecyclerView);
        recyclerView = root.findViewById(R.id.recyclerview);
        recyclerView.setItemAnimator(null);
        containerLayout = root.findViewById(R.id.container_layout);
//        LinearLayout linLayoutUpdateFilter = root.findViewById(R.id.linLayoutUpdateFilter);
        mSwipeRefreshLayout = root.findViewById(R.id.swipeToRefresh);
        llGifProgress = root.findViewById(R.id.ll_gif_progress);
        logoGif = root.findViewById(R.id.logo_gif);
        llNoAstroFound = root.findViewById(R.id.llNoAstroFound);
        llReloadData = root.findViewById(R.id.llReloadData);
        TextView tvNoAstroFound = root.findViewById(R.id.tvNoAstroFound);
        TextView ivReload = root.findViewById(R.id.tv_reload);
        mSwipeRefreshLayout.setColorSchemeResources(R.color.colorAccent);

        mSwipeRefreshLayout.setOnRefreshListener(() ->
                new Handler().postDelayed(() -> {
                    //isFilterApply = false;
                    isTabRefresh = true;
                    getAstrologerListFromServer(true);
                }, 500));

        if (activity instanceof DashBoardActivity) {
            ((DashBoardActivity) activity).changeToolbar(CGlobalVariables.HOMEFRAGMENT);
        }

        SpannableString content = new SpannableString(activity.getString(R.string.Clear_All));
        content.setSpan(new UnderlineSpan(), 0, content.length(), 0);
        tvNoAstroFound.setText(content);
        tvNoAstroFound.setOnClickListener(v -> clearFilter());
        ivReload.setOnClickListener(v -> {
            llReloadData.setVisibility(View.GONE);
            isTabRefresh = true;
            getAstrologerListFromServer(false);
        });

//        linLayoutUpdateFilter.setOnClickListener(view -> {
//            com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents("update_filter_btn_click", com.ojassoft.astrosage.varta.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
//
//            openFilter();
//        });

    }

    private void initData() {
        try {
            arrayListRecyclerViewAllAstrologer = new ArrayList<>();
            arrayListRecyclerView = new ArrayList<>();
            filterList = new ArrayList<>();
            filteredarrayList = new ArrayList<>();
//            if (categoryPosition == 0 && !isFilterApply) {
//                // Show astrologers with price above 20
//                astrologerAdapter = new CustomAdapter(getContext(), HomeFragment1.this, getListAbove20Price(arrayListRecyclerView),this);
//
//            } else {
//                astrologerAdapter = new CustomAdapter(getContext(), HomeFragment1.this, arrayListRecyclerView,this);
//
//            }
            astrologerAdapter = new CustomAdapter(getContext(), HomeFragment1.this, getListAbove20Price(arrayListRecyclerView), this);

            mLayoutManager = new WrapContentLinearLayoutManager(getActivity());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.setAdapter(astrologerAdapter);
        } catch (Exception e) {
            //
        }
    }

    public void openFilter() {
        try {
            // Use getChildFragmentManager since you're calling this from a fragment.
            FragmentManager fm = getChildFragmentManager();
            // Check if the dialog fragment already exists
            Fragment prev = fm.findFragmentByTag(NewFilterBottomDialog.TAG);
            if (prev != null) {
                fm.beginTransaction().remove(prev).commit();
            }
            // Create and show a new dialog instance
            NewFilterBottomDialog filterDialog = NewFilterBottomDialog.getInstance();
            filterDialog.show(fm, NewFilterBottomDialog.TAG);
        } catch (Exception e) {
            //
        }

    }

    private void setAstrologerFilter() {
        try {
            categoryArr.clear();
            List<String> catSymbol = Arrays.asList("", "❤️", "💼", "🎀", "💓", "📶", "🤝", "📚", "🤰", "⚖️");
            final String[] itemList = getContext().getResources().getStringArray(R.array.filter_category);
            //ArrayList<LangAndExpertiseData> langAndExpertiseDataArrayList = new ArrayList<>();
            for (int i = 0; i < itemList.length; i++) {
                if (i == 0) {
                    categoryArr.add(new CategoryModel(itemList[i], catSymbol.get(i), catIdList.get(i), true));
                } else {
                    categoryArr.add(new CategoryModel(itemList[i], catSymbol.get(i), catIdList.get(i), false));
                }
            }
            filterAstrologerAdapter = new CallChatCategoryAdapter(getContext(), categoryArr, this);
            WrapContentLinearLayoutManager linearLayoutManager = new WrapContentLinearLayoutManager(getContext());
            linearLayoutManager.setOrientation(RecyclerView.HORIZONTAL);
            filterAstroRecyclerView.setLayoutManager(linearLayoutManager);
            filterAstroRecyclerView.setAdapter(filterAstrologerAdapter);
            getFilterCategoryList();
        } catch (Exception e) {
            //
        }
    }


    private void getAstrologerListFromServer(boolean isSwipeRefresh) {
        //Log.e("SAN TestList", "getAstrologerListFromServer()");
        if (activity == null) return;
        if (!CUtils.isConnectedWithInternet(activity)) {
            llNoAstroFound.setVisibility(View.GONE);
            if (astrologerAdapter.getItemCount() == 0) {
                llReloadData.setVisibility(View.VISIBLE);
            }
            if (isSwipeRefresh) {
                hideSwipeRefreshProgressBar();
            }
            CUtils.showSnackbar(containerLayout, getResources().getString(R.string.no_internet), getActivity());
        } else {
            if (!isSwipeRefresh) {
                if (arrayListRecyclerViewAllAstrologer == null || arrayListRecyclerViewAllAstrologer.isEmpty()) {
                    //showLogoProgressbar();
                    progressBar.setVisibility(View.VISIBLE);
                }
            }
            fetchAllAstrologerData();
        }

    }

    private void updateAstroListAdapter() {
        if (arrayListRecyclerView.isEmpty()) {
            llNoAstroFound.setVisibility(View.VISIBLE);
        } else {
            llReloadData.setVisibility(View.GONE);
            llNoAstroFound.setVisibility(View.GONE);
        }
        if (astrologerAdapter != null) {
          listTypeToShow(arrayListRecyclerView);
        }
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
        headers.put(CGlobalVariables.OFFER_TYPE, offerType);
        headers.put(CGlobalVariables.PREF_SECOND_FREE_CHAT, "" + CUtils.isSecondFreeChat(activity));
        headers.put(CGlobalVariables.FETCHALL, "1");
        headers.put(CGlobalVariables.PACKAGE_NAME, "" + BuildConfig.APPLICATION_ID);
        headers.put(CGlobalVariables.APP_VERSION, "" + BuildConfig.VERSION_NAME);
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
        headers.put(CGlobalVariables.DEVICE_ID, CUtils.getMyAndroidId(getActivity()));
        //Log.e("SAN ", " HF1 params= " + headers.toString());

        AstrosageKundliApplication.astroListLogs += "AstroList Params=" + headers + "\n";

        return headers;
    }

    @Override
    public void onResponse(String response, int method) {
        //Log.e("SAN HF onResponse ",  " res=> " + response );
        if (activity == null || getActivity() == null) return;

        if (response != null && response.length() > 0) {
            if (method == 1) {

//                hideProgressBar();
//                hideLogoProgressBar();
//                hideSwipeRefreshProgressBar();
//
//                CUtils.saveAstroList(response);
//                Log.e("sharepdftest", " HomeFragment1 onResponse() method == 1 ");
//                parseAstrologerList(response);
            } else if (method == CALL_METHOD) {
                hideProgressBar();
                hideLogoProgressBar();
                hideSwipeRefreshProgressBar();
                responseNetworkCall(response);
            } else if (method == GET_FILTER_LIST) {

                try {
                    JSONObject jsonObject = new JSONObject(response);
                    //JSONArray expertise = jsonObject.getJSONArray("expertise");
                    JSONArray category = jsonObject.getJSONArray("category");
                    for (int i = 0; i < category.length(); i++) {
                        JSONObject catObj = category.getJSONObject(i);
                        String catId = catObj.getString("id");
                        if (!catIdList.contains(catId)) {
                            categoryArr.add(new CategoryModel(catObj.getString("name"), catObj.getString("code"), catObj.getString("id"), false));
                        }
                    }
                    if (filterAstrologerAdapter != null) {
                        filterAstrologerAdapter.notifyDataSetChanged();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                hideProgressBar();
                hideLogoProgressBar();
                hideSwipeRefreshProgressBar();
            }
        } else {
            hideProgressBar();
            hideLogoProgressBar();
            hideSwipeRefreshProgressBar();
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
            if (astrologerAdapter.getItemCount() == 0) {
                llReloadData.setVisibility(View.VISIBLE);
            }
            CUtils.showVolleyErrorRespMessage(containerLayout, activity, error, "2");
        } catch (Exception e) {
            e.printStackTrace();
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
                    CUtils.fcmAnalyticsEvents("show_call_thank_you_dialog", AstrosageKundliApplication.currentEventType, "");
                    openFreeMinDialogBox();
                }
                CUtils.fcmAnalyticsEvents("show_call_initiate_dialog", AstrosageKundliApplication.currentEventType, "");

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

                callInitiatedDialog = new CallInitiatedDialog(callsId, talkTime, exophoneNo, CALL_CLICK, internationalCharges, callingAstroName, callingAstroProfileUrl);
                callInitiatedDialog.show(getChildFragmentManager(), "Dialog");

                try {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                CUtils.fcmAnalyticsEvents("start_call_status_service", AstrosageKundliApplication.currentEventType, "");

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
            } else if (status.equals(CGlobalVariables.INSUFFICIENT_BALANCE)) {
                String astrologerName = jsonObject.getString("astrologername");
                String userbalance = jsonObject.getString("userbalance");
                String minBalance = jsonObject.getString("minbalance");
                CUtils.fcmAnalyticsEvents("call_insufficient_bal_dialog", AstrosageKundliApplication.currentEventType, "");

                InSufficientBalanceDialog dialog = new InSufficientBalanceDialog(astrologerName, userbalance, minBalance, CALL_CLICK);
                dialog.show(getChildFragmentManager(), "Dialog");
            } else if (status.equals(CGlobalVariables.ASTROLOGER_BUSY_OFFLINE)) {
                String fcm_label = "call_astrologer_" + message;

                CUtils.fcmAnalyticsEvents(fcm_label, AstrosageKundliApplication.currentEventType, "");

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
                    dialogMsg = getActivity().getResources().getString(R.string.existing_call_msg);
                }
                callMsgDialogData(dialogMsg, "", true, CALL_CLICK);
                //CUtils.showSnackbar(containerLayout,getResources().getString(R.string.astrologer_busy_msg), getActivity());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadMoreAstrologerist(int listCount) {

    }

    @Override
    public void callAstrologer(AstrologerDetailBean astrologerDetailBean) {
        if (CUtils.isChatNotInitiated()) {
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
            if (astrologerDetailBean.isFreeForCall()) {
                isFreeConsultation = true;
            }
            offerType = astrologerDetailBean.getIntroOfferType();
            String aiai = astrologerDetailBean.getAiAstrologerId();
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
                        if (isAgoraCallEnabled && astrologerDetailBean.getAcceptInternetCall() != CGlobalVariables.NETWORK_CALL) {
                            //checkCallMedium(astrologerDetailBean);
                            checkPermission(astrologerDetailBean);
                        } else if(!TextUtils.isEmpty(aiai) && !aiai.equals("0")) {
                            checkPermission(astrologerDetailBean);
                        } else {
                            checkCallMedium(astrologerDetailBean);
                            //CUtils.openProfileOrKundliActFromFrag(activity, astrologerDetailBean.getUrlText(), "call", this);
                        }
                    } else {
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.ASTRO_BUSY_IN_ANOTHER_CALL,
                                AstrosageKundliApplication.currentEventType, "");
                        String msg = getResources().getString(R.string.astrologer_busy_msg);
                        callMsgDialogData(msg, "", true, CALL_CLICK);
                    }
                }
            } else {
                CUtils.fcmAnalyticsEvents(CGlobalVariables.ASTRO_BUSY_IN_ANOTHER_CALL,
                        AstrosageKundliApplication.currentEventType, "");
                if (astrologerBusyStatus.equalsIgnoreCase("true")) {
                    String msg = getResources().getString(R.string.astrologer_busy_msg);
                    callMsgDialogData(msg, "", true, CALL_CLICK);
                } else {
                    String stringMsg = "";
                    if ((astrologerChatStatus.equalsIgnoreCase("true")) && (astrologerCallStatus.equalsIgnoreCase("false"))) {
                        stringMsg = getResources().getString(R.string.astrologer_offline_call);
                    } else if ((astrologerChatStatus.equalsIgnoreCase("false")) && (astrologerCallStatus.equalsIgnoreCase("true"))) {
                        stringMsg = getResources().getString(R.string.astrologer_offline_chat);
                    } else {
                        stringMsg = getResources().getString(R.string.astrologer_offline_msg);
                    }
                    callMsgDialogData(stringMsg, "", true, CALL_CLICK);
                }
            }
        } else {
            CUtils.showSnackbar(containerLayout, getResources().getString(R.string.allready_in_chat), activity);
        }
    }

    private void checkCallMedium(AstrologerDetailBean astrologerDetailBean) {
        try {
            astrologerDetailBean.setCallSource(TAG);
            ChatUtils.getInstance(activity).initVoiceCall(astrologerDetailBean);
        } catch (Exception e) {
            //
        }
        /*if (CUtils.isConnectedMobile(activity)) {
            if (CUtils.isNetworkSpeedSlow(activity)) {
                CUtils.fcmAnalyticsEvents(CGlobalVariables.NETWORK_CALL_CONNECT_REASON_SPEED_SLOW,
                        AstrosageKundliApplication.currentEventType, "");
                CUtils.openProfileOrKundliActFromFrag(activity, astrologerDetailBean.getUrlText(), "call", this);
            } else {
                ChatUtils.getInstance(activity).initVoiceCall(astrologerDetailBean);
            }
        } else {
            ChatUtils.getInstance(activity).initVoiceCall(astrologerDetailBean);
        }*/
    }

    private static final int PERMISSION_REQ_ID = 22;
    private static final String[] REQUESTED_PERMISSIONS = {Manifest.permission.RECORD_AUDIO};
    private AstrologerDetailBean callAstrologerDetailBean;

    private void checkPermission(AstrologerDetailBean astrologerDetailBean) {
        boolean granted = true;
        for (String per : REQUESTED_PERMISSIONS) {
            if (!permissionGranted(per)) {
                granted = false;
                break;
            }
        }

        if (granted) {
            CUtils.setHavePermissionRecordAudio("1");
            checkCallMedium(astrologerDetailBean);
        } else {
            CUtils.showPreMicPermissionDialog(requireContext(),()->{
                requestPermissions(astrologerDetailBean);
            });

        }
    }

    private void requestPermissions(AstrologerDetailBean astrologerDetailBean) {
        callAstrologerDetailBean = astrologerDetailBean;
        requestPermissions(REQUESTED_PERMISSIONS, PERMISSION_REQ_ID);
    }

    private boolean permissionGranted(String permission) {
        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQ_ID) {
            boolean granted = true;
            for (int result : grantResults) {
                granted = (result == PackageManager.PERMISSION_GRANTED);
                if (!granted) break;
            }
            if (callAstrologerDetailBean != null) {
                if (granted) {
                    CUtils.setHavePermissionRecordAudio("1");
                    checkCallMedium(callAstrologerDetailBean);
                } else {
                    CUtils.setHavePermissionRecordAudio("0");
//                    if (callAstrologerDetailBean.getAcceptInternetCall() == CGlobalVariables.NETWORK_AND_INTERNET_CALL) {
//                        CUtils.openProfileOrKundliActFromFrag(activity, callAstrologerDetailBean.getUrlText(), "call", this);
//                    } else {
//                        CUtils.displayRequiredPermissionDialog(activity);
//                    }
                    String aiai = callAstrologerDetailBean.getAiAstrologerId();
                    if(TextUtils.isEmpty(aiai) || aiai.equals("0")) {
                        checkCallMedium(callAstrologerDetailBean);
                    }else {
                        CUtils.showPermissionDeniedDialog(requireContext());
                    }

                }
            }
        }
    }

    @Override
    public void chatAstrologer(AstrologerDetailBean astrologerDetailBean) {
        CUtils.isCallChatInitFromAstroList = true;
        if (astrologerDetailBean != null) {
            Log.e("TestChat", "chatAstrologer()2= " + astrologerDetailBean.getAiAstrologerId());
            if (astrologerDetailBean.getIsAvailableForChat().equalsIgnoreCase("true")) {
                if (astrologerDetailBean.getIsBusy().equalsIgnoreCase("true")) {
                    CUtils.fcmAnalyticsEvents("astro_busy_when_free_user_receive_notification",
                            AstrosageKundliApplication.currentEventType, "");
                    //String msg = getResources().getString(R.string.astrologer_busy_msg_chat);
                    //msg = msg.replace("#",astrologerDetailBean.getWaitTimeRemaining());
                    //callMsgDialogData(msg, "", true, CGlobalVariables.CHAT_CLICK);
                    //((DashBoardActivity) activity).sendNotification(astrologerDetailBean.getUrlText(),astrologerDetailBean);
                    CUtils.showNotificationAlertDialog(activity, astrologerDetailBean, getChildFragmentManager());
                } else {
                    astrologerDetailBean.setCallSource(TAG);
                    CUtils.fcmAnalyticsEvents("astro_chat_method_called",
                            AstrosageKundliApplication.currentEventType, "");
//                    if (getActivity() instanceof DashBoardActivity) {
//                        ((DashBoardActivity) getActivity()).initTwilioChat(astrologerDetailBean);
//                    } else if (getActivity() instanceof ActAppModule) {
//                        AstrosageKundliApplication.chatAstrologerDetailBean = astrologerDetailBean;
//                        Intent intent = new Intent(getActivity(), DashBoardActivity.class);
//                        startActivity(intent);
//                    }
                    if (CUtils.isChatNotInitiated()) {
                        if (CUtils.isAiAstrologer(astrologerDetailBean)) {
                            Log.e("TestChat", "chatAstrologer() isAiAstrologer = true");
                            ChatUtils.getInstance(getActivity()).initAIChat(astrologerDetailBean);
                        } else {
                            ChatUtils.getInstance(getActivity()).initChat(astrologerDetailBean);
                        }
                    } else {
                        CUtils.showSnackbar(containerLayout, getResources().getString(R.string.allready_in_chat), getActivity());
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


    private void parseAstrologerList(String responseData) {
        // Log.e("sharepdftest", "responseData: "+responseData);
        AstrosageKundliApplication.astroListLogs += "AstroList responseData=" + responseData + "\n";
        if (arrayListRecyclerViewAllAstrologer == null) {
            arrayListRecyclerViewAllAstrologer = new ArrayList();
        } else {
            arrayListRecyclerViewAllAstrologer.clear();
        }
        llReloadData.setVisibility(View.GONE);
        try {
            JSONObject jsonObject = new JSONObject(responseData);
            if (jsonObject.length() > 0) {

                if (jsonObject.has("verifiedicon")) {
                    try {
                        CUtils.setVerifiedAndOfferImage(URLDecoder.decode(jsonObject.getString("verifiedicon"), "UTF-8"), URLDecoder.decode(jsonObject.getString("verifiedicondetail"), "UTF-8"),
                                URLDecoder.decode(jsonObject.getString("offericon"), "UTF-8"), URLDecoder.decode(jsonObject.getString("offericondetail"), "UTF-8"));
                    } catch (Exception ex) {

                    }
                }
                AstrosageKundliApplication.connectinternetcall = jsonObject.optBoolean("cic");
                AstrosageKundliApplication.astroGroup = jsonObject.optString("astrogroup");
                String offerType = jsonObject.optString("offertype"); //"FIRSTSESSIONFREE";//
                boolean isSecondFreeChat = jsonObject.optBoolean("sfc");
                CUtils.setSecondFreeChat(activity, isSecondFreeChat);
                //Log.d("TestStatus", " offerType1= "+offerType);
                //Log.e("SAN CHAT ", " HomeFragment1 parseAstrologerList() calling handleOfferType() " );

                JSONArray jsonArray = jsonObject.getJSONArray("astrologers");
                if (jsonArray.length() > 0) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        AstrologerDetailBean astrologerDetailBean = CUtils.parseAstrologerObject(object);
                        if (astrologerDetailBean == null) continue;
                        arrayListRecyclerViewAllAstrologer.add(astrologerDetailBean);

                        //Log.d("TestStatus", astrologerDetailBean.getAstrologerId()+" FreeForCall "+astrologerDetailBean.isFreeForCall()+"FreeForChat "+astrologerDetailBean.isFreeForChat());

                    }
                }
                //Log.d("AstroListF", "arrayListRecyclerViewAllAstrologer size"+arrayListRecyclerViewAllAstrologer.size());
                handleOfferType(offerType);
                updateAstrologerList();

                if (AstrosageKundliApplication.connectinternetcall) {
                    // need request permission
                    requestPermissions(null);
                }

            } else {
                ((DashBoardActivity) activity).callSnakbar(getResources().getString(R.string.server_error));
            }
        } catch (JSONException e) {
            Log.d("sharepdftest", "ex: " + e);
            e.printStackTrace();
        }
    }

    private void handleOfferType(String offerType) {
        offerIntroType = offerType;
        //Log.e("SAN CHAT ", " HomeFragment1 handleOfferType() offerType => " + offerType );
        try {
            if (offerType.equals(CGlobalVariables.INTRO_OFFER_TYPE_FREE)) {
                if (activity instanceof DashBoardActivity && !AstrosageKundliApplication.isOpenVartaPopup) {
                    ((DashBoardActivity) activity).openDialogOnHomeScreen();
                }
            }

            //Log.d("TestOfferType", "offerType="+offerType);
            //Log.e("SAN CHAT ", " HomeFragment1 handleOfferType() save offerType  => " + offerType );
            CUtils.setUserIntroOfferType(activity, offerType);
            checkNextRechargeOffer(offerType);
        } catch (Exception e) {
            //
        }
    }

    private void updateAstrologerList() {
        progressBar.setVisibility(View.GONE);
        allAstrologersArrayList.clear();
        allAstrologersArrayList.addAll(arrayListRecyclerViewAllAstrologer);
        setbookMarkedAstrologerList();
        updateCallChatAvailableAstrologer();
    }

    public void chatWithRandomAstrologer() {
        try {
//            AstrologerDetailBean astrologerBean = null;
//            if ( arrayListRecyclerViewAllAstrologer != null) {
//                for (int i = 0; i < arrayListRecyclerViewAllAstrologer .size(); i++) {
//                    //Log.d("TestisOfferRemaining"," " +arrayListRecyclerViewAllAstrologer.get(i).isOfferRemaining());
//                    astrologerBean = arrayListRecyclerViewAllAstrologer .get(i);
//                    if (astrologerBean.getIsAvailableForChat().equals("true") && astrologerBean.getIsBusy().equals("false") && astrologerBean.isOfferRemaining()) {
//                        // Log.d("TestisOfferRemaining"," " +i);
//                        break;
//                    } else {
//                        astrologerBean = null;
//                    }
//                }
//            }
//            if (astrologerBean != null) {
//                astrologerBean.setFreeForChat(true);
//                chatAstrologer(astrologerBean);
//            }
        } catch (Exception e) {
            //
        }
    }

    public void callWithRandomAstrologer() {
        try {
            CUtils.popUpLoginFreeCallClicked = false;
            CUtils.popUpLoginFreeChatClicked = false;
            AstrologerDetailBean astrologerBean = null;
            if (arrayListRecyclerViewAllAstrologer != null) {
                for (int i = 0; i < arrayListRecyclerViewAllAstrologer.size(); i++) {
                    astrologerBean = arrayListRecyclerViewAllAstrologer.get(i);
                    // Log.d("TestExpertise",astrologerBean.getExpertise());
                    if (astrologerBean.getIsAvailableForCall().equals("true") && astrologerBean.getIsBusy().equals("false") && astrologerBean.getExpertise().contains("Vedic") && astrologerBean.isOfferRemaining()) {
                        break;
                    } else {
                        astrologerBean = null;
                    }
                }
            }
            if (astrologerBean != null) {
                isFreeConsultation = true;
                callAstrologer(astrologerBean);
            }
        } catch (Exception e) {
            //
        }
    }

    public void callToAstrologer(String phoneNo, String urlText, boolean isFreeConsultation) {
        showFreeOneMinDialog();
        callSelectedAstrologer(phoneNo, urlText, isFreeConsultation);
    }

    private void showFreeOneMinDialog() {
        try {
            String offerType = CUtils.getCallChatOfferType(getActivity());
            if (!TextUtils.isEmpty(offerType) && offerType.equals(CGlobalVariables.INTRO_OFFER_TYPE_FREE) && CUtils.getCountryCode(activity).equals("91")) {
                freeMinutedialog = new FreeMinuteDialog(CGlobalVariables.INTRO_OFFER_TYPE_FREE);
                freeMinutedialog.show(getChildFragmentManager(), "Dialog");
                CUtils.fcmAnalyticsEvents("first_min_free_dialog_free_call", AstrosageKundliApplication.currentEventType, "");

            }
            String wallet = CUtils.getWalletRs(getActivity());
            //Log.e("LoadMore wallet ", wallet);
            if ((wallet != null) && !wallet.equals("0.0")) {
                CUtils.fcmAnalyticsEvents("first_min_free_dialog", AstrosageKundliApplication.currentEventType, "");

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
                    callToAstrologer(phoneNo, urlText, isFreeConsultation);
                } else if (data.getExtras().containsKey("openKundliList")) {
                    CUtils.openSavedKundliListFromFrag(activity, data.getStringExtra("urlText"), "call", this);
                } else if (data.getExtras().containsKey("openProfileForChat")) {
                    boolean prefillData = true;
                    if (data.getExtras().containsKey("prefillData")) {
                        prefillData = data.getBooleanExtra("prefillData", true);
                    }
                    Bundle bundle = data.getExtras();
                    CUtils.openProfileForChatFromFrag(activity, data.getStringExtra("urlText"), "call", bundle, prefillData, this);
                }
            }
        }

    }


    private void getFilterCategoryList() {
        if (CUtils.isConnectedWithInternet(getActivity())) {
            //showProgressBar();
            CUtils.getFilterCategory(this, GET_FILTER_LIST);
        } else {
            CUtils.showSnackbar(containerLayout, getResources().getString(R.string.no_internet), getActivity());
        }
    }

    private void callSelectedAstrologer(String mobileNo, String urlTextt, boolean isFreeConsultation) {

        if (!CUtils.isConnectedWithInternet(getActivity())) {
            CUtils.showSnackbar(containerLayout, getResources().getString(R.string.no_internet), getActivity());
        } else {
            showProgressBar();
            //Log.e("SAN  ", " url " + CGlobalVariables.CALL_ASTROLOGER_URL);
            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.CALL_ASTROLOGER_URL,
                    HomeFragment1.this, false, getCallParams(mobileNo, urlTextt, isFreeConsultation), CALL_METHOD).getMyStringRequest();
            stringRequest.setShouldCache(true);
            queue.add(stringRequest);
            CUtils.fcmAnalyticsEvents("connect_call_api_called", AstrosageKundliApplication.currentEventType, "");

        }
    }

    public Map<String, String> getCallParams(String phoneNo, String urlText, boolean isFreeConsultation) {
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
        headers.put(CGlobalVariables.OFFER_TYPE, currentOfferType);
        headers.put(USER_SPEED, String.valueOf(CUtils.getNetworkSpeed(getActivity())));
        //Log.e("SAN ", " TestCall params1= "+headers.toString());
        return headers;
    }

    public void setbookMarkedAstrologerList() {
        if (getActivity() == null) return;
        NotificationDBManager dbManager = new NotificationDBManager(getActivity());
        List<BookmarkModel> bookmarkModelList = dbManager.getBookMarkList();
        for (int i = 0; i < arrayListRecyclerViewAllAstrologer.size(); i++) {
            for (int j = 0; j < bookmarkModelList.size(); j++) {
                if (arrayListRecyclerViewAllAstrologer.get(i).getAstrologerId().equals(bookmarkModelList.get(j).getAstrologerId())) {
                    arrayListRecyclerViewAllAstrologer.get(i).setAstrologerBookmarked(true);
                }
            }
        }
    }


    public void selectBookMarkedDataUpdate() {
        applyFilter(filterList, langFilterList, expertiseFilterList);
    }


    public ArrayList<AstrologerDetailBean> getCallChatAvailableAstrologer(int filterType) {
        //Log.e("SAN ", "HF1 filterType " + filterType );
        try {
            if (filterType == CGlobalVariables.FILTER_TYPE_AVAILABLE) {
                return getAvailableAstrologerList(arrayListRecyclerViewAllAstrologer);
            } else if (filterType == CGlobalVariables.FILTER_TYPE_CALL) {
                return getCallAvailableAstrologerList();
            } else if (filterType == CGlobalVariables.FILTER_TYPE_CHAT) {
                return getChatAvailableAstrologerList();
            } else {
                return arrayListRecyclerViewAllAstrologer;
            }
        } catch (Exception e) {
            //
        }
        return new ArrayList<>();
    }

    public void updateCallChatAvailableAstrologer() {
        try {

            arrayListRecyclerView.clear();
            arrayListRecyclerView.addAll(getCallChatAvailableAstrologer(CUtils.astroListFilterType));
            filteredarrayList.clear();
            filteredarrayList.addAll(arrayListRecyclerView);
            if (isFilterApply) {
                applyFilter(filterList, langFilterList, expertiseFilterList);
            }
            if (CUtils.expertiseFilterType != -1) {
                expertiseFilterList.add(expertiseID[CUtils.expertiseFilterType]);
                filterList.add(expertiseID[CUtils.expertiseFilterType]);
                applyFilter(filterList, langFilterList, expertiseFilterList);
            }

            if (CUtils.regionalFilterType != -1) {
                langFilterList.add(CUtils.getLanguage(CUtils.regionalFilterType));
                filterList.add(CUtils.getLanguage(CUtils.regionalFilterType));
                applyFilter(filterList, langFilterList, expertiseFilterList);
            }

            if (arrayListRecyclerView.size() == 0) {
                CUtils.showSnackbar(containerLayout, getResources().getString(R.string.no_astrologer_found), getActivity());
            }

            if (TYPE_TAB_FILTER.isEmpty()) {
                updateAstroListAdapter();
            } else {
                onTabClick(TYPE_TAB_FILTER);
            }

        } catch (Exception e) {
            //
        }
    }


    public ArrayList<AstrologerDetailBean> getAvailableAstrologerList(ArrayList<AstrologerDetailBean> astrologerDetailBeanArrayList) {
        ArrayList<AstrologerDetailBean> arrayList = new ArrayList<>();
        for (int i = 0; i < astrologerDetailBeanArrayList.size(); i++) {
            if (astrologerDetailBeanArrayList.get(i).getIsBusy().equals("false")) {
                if (astrologerDetailBeanArrayList.get(i).isAstrologerBookmarked()) {
                    arrayList.add(astrologerDetailBeanArrayList.get(i));
                } else {
                    arrayList.add(astrologerDetailBeanArrayList.get(i));
                }
            }
        }
        return arrayList;
    }

    public ArrayList<AstrologerDetailBean> getCallAvailableAstrologerList() {
        ArrayList<AstrologerDetailBean> arrayList = new ArrayList<>();
        if (arrayListRecyclerViewAllAstrologer != null) {
            for (int i = 0; i < arrayListRecyclerViewAllAstrologer.size(); i++) {
                if (arrayListRecyclerViewAllAstrologer.get(i).getIsAvailableForCall().equals("true")) {
                    arrayList.add(arrayListRecyclerViewAllAstrologer.get(i));
                }
            }
        }
        return arrayList;
    }

    public ArrayList<AstrologerDetailBean> getChatAvailableAstrologerList() {
        ArrayList<AstrologerDetailBean> arrayList = new ArrayList<>();
        if (arrayListRecyclerViewAllAstrologer != null) {
            for (int i = 0; i < arrayListRecyclerViewAllAstrologer.size(); i++) {
                // SAN added for call astrologer
                // arrayListRecyclerViewAllAstrologer.get(i).getIsAvailableForCall().equals("true")
                if (arrayListRecyclerViewAllAstrologer.get(i).getIsAvailableForChat().equals("true") &&
                        arrayListRecyclerViewAllAstrologer.get(i).getIsAvailableForCall().equals("true")) {
                    arrayList.add(arrayListRecyclerViewAllAstrologer.get(i));
                } else if (arrayListRecyclerViewAllAstrologer.get(i).getIsAvailableForChat().equals("true")) {
                    arrayList.add(arrayListRecyclerViewAllAstrologer.get(i));
                }

            }
        }
        return arrayList;
    }

    /*
    public ArrayList<AstrologerDetailBean> getChatAvailableNotBusy() {
        filteredarrayList.clear();
        if (arrayListRecyclerViewAllAstrologer != null) {
            for (int i = 0; i < arrayListRecyclerViewAllAstrologer.size(); i++) {
                AstrologerDetailBean bean = arrayListRecyclerViewAllAstrologer.get(i);
                if (bean.getIsAvailableForChat().equals("true") && bean.getIsBusy().equals("false")) {
                    filteredarrayList.add(arrayListRecyclerViewAllAstrologer.get(i));
                }
            }
        }
        return filteredarrayList;
    }*/

    /*public void openFilter(){
        try {
            FilterDialog filterDialog = FilterDialog.newInstance(this,filterList);
            filterDialog.show(getChildFragmentManager(), FilterDialog.TAG);
        }catch (Exception e){
            //
        }

    }*/

    public void clearFilter() {
        isFilterApply = false;
        filterList.clear();
        CUtils.astroListFilterType = tempFilterType;
        updateCallChatAvailableAstrologer();
        // applyFilter(filterList);
        // categoryItemClick(0);
    }

    private void onTabClick(String keyval) {
        recyclerView.scrollToPosition(0);
        if (arrayListRecyclerView != null)
            arrayListRecyclerView.clear();
        ArrayList<AstrologerDetailBean> astrologerListByCategory = new ArrayList<>();
        //ArrayList<AstrologerDetailBean> astrologerArrayList = getCallChatAvailableAstrologer(CUtils.astroListFilterType);
        if (filteredarrayList != null && !filteredarrayList.isEmpty()) {
            for (AstrologerDetailBean item : filteredarrayList) {
                if (item.getCatId().contains(keyval)) {
                    astrologerListByCategory.add(item);
                }
            }
            //Log.d("TestLog", "size1="+astrologerListByCategory.size());
            // randomize astrologers according to featured and non-ffeatured
            if (!astrologerListByCategory.isEmpty()) {
                if (isFilterApply) {
                    arrayListRecyclerView.addAll(astrologerListByCategory);
                } else {
                    ArrayList<AstrologerDetailBean> featuredAstrologerList = new ArrayList<>();
                    ArrayList<AstrologerDetailBean> nonFeaturedAstrologerList = new ArrayList<>();
                    if (isTabRefresh) {
                        randomNumber = CUtils.getRandomNumberByRange(0, (astrologerListByCategory.size() - 1));
                    }
                    //Log.d("TestLog", "randomNumber="+randomNumber);
                    for (int i = randomNumber; i < astrologerListByCategory.size(); i++) {
                        AstrologerDetailBean astrologerBean = astrologerListByCategory.get(i);
                        if (astrologerBean.isFeatured()) {
                            featuredAstrologerList.add(astrologerBean);
                        } else {
                            nonFeaturedAstrologerList.add(astrologerBean);
                        }
                    }
                    for (int i = 0; i < randomNumber; i++) {
                        AstrologerDetailBean astrologerBean = astrologerListByCategory.get(i);
                        if (astrologerBean.isFeatured()) {
                            featuredAstrologerList.add(astrologerBean);
                        } else {
                            nonFeaturedAstrologerList.add(astrologerBean);
                        }
                    }
                    //Log.d("TestLog", "size2="+featuredAstrologerList.size());
                    //Log.d("TestLog", "size3="+nonFeaturedAstrologerList.size());
                    arrayListRecyclerView.addAll(featuredAstrologerList);
                    arrayListRecyclerView.addAll(nonFeaturedAstrologerList);

                    ArrayList<AstrologerDetailBean> freeAstrologerList = new ArrayList<>(arrayListRecyclerView);
                    arrayListRecyclerView.clear();
                    for (AstrologerDetailBean bean : freeAstrologerList) {
                        String offerType = bean.getIntroOfferType();
                        if (bean.getUseIntroOffer() && !TextUtils.isEmpty(offerType)) {
                            arrayListRecyclerView.add(bean);
                        }
                    }

                    freeAstrologerList.removeAll(arrayListRecyclerView);
                    arrayListRecyclerView.addAll(freeAstrologerList);

                }

                //Log.d("TestLog", "size4="+arrayListRecyclerView.size());
            } else {
                CUtils.showSnackbar(containerLayout, getResources().getString(R.string.no_astrologer_found), getActivity());
            }
        } else {
            CUtils.showSnackbar(containerLayout, getResources().getString(R.string.no_astrologer_found), getActivity());
        }
        updateAstroListAdapter();
    }

    public void showLiveAstrologer() {
        activity.startActivity(new Intent(activity, AllLiveAstrologerActivity.class));
        /*if (activity instanceof DashBoardActivity) {
            ((DashBoardActivity) activity).fabActions();
        }*/
    }

    public void applyFilter(List<String> filterList, List<String> langFilterList, List<String> expertiseFilterList) {
        try {
            this.filterList = filterList;
            //filterAstroRecyclerView.scrollToPosition(0);
            recyclerView.scrollToPosition(0);
            isFilterApply = true;
            arrayListRecyclerView.clear();

            if (filterList.contains(TYPE_CALL)) {
                CUtils.astroListFilterType = CGlobalVariables.FILTER_TYPE_CALL;
            } else if (filterList.contains(TYPE_CHAT)) {
                CUtils.astroListFilterType = CGlobalVariables.FILTER_TYPE_CHAT;
            } else if (filterList.contains(TYPE_LIVE)) {
                CUtils.astroListFilterType = 0;
            }
            ((DashBoardActivity) activity).setBottomNavigationText(CUtils.getUserLoginStatus(activity));

            ArrayList<AstrologerDetailBean> astrologerArrayList = getCallChatAvailableAstrologer(CUtils.astroListFilterType);
        /*
        if (isBookMarkedCBChecked) {
            arrayListRecyclerView.addAll(getBookmardedAstrologerList(astrologerArrayList));
        }else {
            arrayListRecyclerView.addAll(getFilteredData(astrologerArrayList, selectedLangId, selectedExpertiseId));
        }*/

            if (astrologerArrayList != null && !astrologerArrayList.isEmpty()) {
                if (!filterList.isEmpty()) {
                    // notifyAdapter();
                    for (AstrologerDetailBean item : astrologerArrayList) {
                        boolean isLangExist = false;
                        boolean isExpertiseExist = false;
                        for (String data : langFilterList) {
                            if (item.getLanguage().contains(data)) {
                                isLangExist = true;
                            } else {
                                isLangExist = false;
                                break;
                            }
                        }
                        for (String data : expertiseFilterList) {
                            if (item.getExpertise().contains(data)) {
                                isExpertiseExist = true;
                            } else {
                                isExpertiseExist = false;
                                break;
                            }
                        }
                        if (!langFilterList.isEmpty() && !expertiseFilterList.isEmpty()) {
                            if (isExpertiseExist && isLangExist) {
                                arrayListRecyclerView.add(item);
                            }
                        } else if (!langFilterList.isEmpty()) {
                            if (isLangExist) {
                                arrayListRecyclerView.add(item);
                            }
                        } else if (!expertiseFilterList.isEmpty()) {
                            if (isExpertiseExist) {
                                arrayListRecyclerView.add(item);
                            }
                        }
                    }

                    if (!langFilterList.isEmpty() || !expertiseFilterList.isEmpty()) {
                        if (arrayListRecyclerView.isEmpty()) {
                            CUtils.showSnackbar(containerLayout, getResources().getString(R.string.no_astrologer_found), getActivity());
                            updateAstroListAdapter();
                            return;
                        } else {
                            //
                        }
                    }
                    if (filterList.contains(TYPE_ECONOMIC) || filterList.contains(TYPE_LOW_TO_HIGH)) {
                        if (arrayListRecyclerView.isEmpty()) {
                            arrayListRecyclerView.addAll(astrologerArrayList);
                        }
                        Comparator<AstrologerDetailBean> comparator = AstrologerDetailBean.servicePriceCamparatorAssending;
                        Collections.sort(arrayListRecyclerView, comparator);
                    } else if (filterList.contains(TYPE_PREMIUM) || filterList.contains(TYPE_HIGH_TO_LOW)) {
                        if (arrayListRecyclerView.isEmpty()) {
                            arrayListRecyclerView.addAll(astrologerArrayList);
                        }
                        Comparator<AstrologerDetailBean> comparator = AstrologerDetailBean.servicePriceCamparatorDesending;
                        Collections.sort(arrayListRecyclerView, comparator);
                    } else if (filterList.contains(TYPE_POPULAR)) {
                        if (arrayListRecyclerView.isEmpty()) {
                            arrayListRecyclerView.addAll(astrologerArrayList);
                        }
                        Comparator<AstrologerDetailBean> comparator = AstrologerDetailBean.followerCamparatorDesending;
                        Collections.sort(arrayListRecyclerView, comparator);
                        ArrayList<AstrologerDetailBean> tempAstrologerList = new ArrayList<>();
                        tempAstrologerList.addAll(arrayListRecyclerView);

                        for (int i = 0; i < tempAstrologerList.size(); i++) {
                            AstrologerDetailBean astrologerDetailBean = tempAstrologerList.get(i);
                            if (astrologerDetailBean.getManipulatedRank() > 0) {
                                break;
                            } else {
                                arrayListRecyclerView.remove(astrologerDetailBean);
                                arrayListRecyclerView.add(astrologerDetailBean);
                            }
                        }

                    } else if (filterList.contains(TYPE_MOSTRATED)) {
                        if (arrayListRecyclerView.isEmpty()) {
                            arrayListRecyclerView.addAll(astrologerArrayList);
                        }
                        Comparator<AstrologerDetailBean> comparator = AstrologerDetailBean.serviceRatingCamparatorAssending;
                        Collections.sort(arrayListRecyclerView, comparator);
                        ArrayList<AstrologerDetailBean> zeroList = new ArrayList<>();
                        for (AstrologerDetailBean item : arrayListRecyclerView) {
                            if (item.getManipulatedRank() == 0) {
                                zeroList.add(item);
                            }
                        }
                        arrayListRecyclerView.removeAll(zeroList);
                        arrayListRecyclerView.addAll(zeroList);
                    } else if (CUtils.astroListFilterType > 0) {
                        if (arrayListRecyclerView.isEmpty()) {
                            arrayListRecyclerView.addAll(astrologerArrayList);
                        }
                    }
                } else {
                    arrayListRecyclerView.addAll(astrologerArrayList);
                }
            }
            if (arrayListRecyclerView.isEmpty()) {
                CUtils.showSnackbar(containerLayout, getResources().getString(R.string.no_astrologer_found), getActivity());
            } else {
                filteredarrayList.clear();
                filteredarrayList.addAll(arrayListRecyclerView);
            }
            if (TYPE_TAB_FILTER.isEmpty()) {
                updateAstroListAdapter();
            } else {
                onTabClick(TYPE_TAB_FILTER);
            }
        } catch (Exception e) {
            //
        }
    }

    private void fetchAllAstrologerData() {
        //Log.e("SAN CHAT ", " HomeFragment1 fetchAllAstrologerData() " );
        fetchAllAstrologerDataWithoutGzip();
        /*if (DashBoardActivity.isGzipDesabled != null && DashBoardActivity.isGzipDesabled.equals("1")) {
            //Log.e("SAN CHAT ", " HomeFragment1 fetchAllAstrologerDataWithoutGzip() " );
            fetchAllAstrologerDataWithoutGzip();
        } else {
            //Log.e("SAN CHAT ", " HomeFragment1 sendGzipRequestAstro() " );
            sendGzipRequestAstro(CGlobalVariables.ASTROLOGER_LIST_URL, getParams());
        }*/
    }

    private void fetchAllAstrologerDataWithoutGzip() {
/*        if (!CUtils.isConnectedWithInternet(getActivity())) {
            CUtils.showSnackbar(containerLayout, getResources().getString(R.string.no_internet), getActivity());
        } else {
            //Log.e("SAN ", "HF1 url " + CGlobalVariables.ASTROLOGER_LIST_URL );
            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, CGlobalVariables.ASTROLOGER_LIST_URL,
                    HomeFragment1.this, false, getParams(), 1).getMyStringRequest();
            stringRequest.setShouldCache(true);
            queue.add(stringRequest);
        }*/

        ApiList api = RetrofitClient.getInstance().create(ApiList.class);
        Call<ResponseBody> call = api.getAstrologerList(getParams());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                progressBar.setVisibility(View.GONE);
                try {
                    if (response.body() != null) {
                        if (activity == null || getActivity() == null) return;
                        hideLogoProgressBar();
                        hideSwipeRefreshProgressBar();
                        String myResponse = response.body().string();
                        CUtils.saveAstroList(myResponse);
                        Log.e("SAN TestList", "Response " + myResponse);

                        parseAstrologerList(myResponse);
                    }
                } catch (Exception e) {
                    //
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (activity == null || getActivity() == null) return;
                try {
                    progressBar.setVisibility(View.GONE);
                    hideProgressBar();
                    hideLogoProgressBar();
                    hideSwipeRefreshProgressBar();
                    if (astrologerAdapter.getItemCount() == 0) {
                        llReloadData.setVisibility(View.VISIBLE);
                    }
                    CUtils.showSnackbar(containerLayout, getActivity().getResources().getString(R.string.something_wrong_error), getActivity());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
    }

    long timeDifference = 0;
//    private void sendGzipRequestAstro(String url, final Map<String, String> paramsHashMap) {
//
//        //Log.e("SAN TestList", "sendGzipRequestAstro()"+System.currentTimeMillis());
//        timeDifference = 0;
//        timeDifference = System.currentTimeMillis();
//        RequestQueue queue = VolleySingleton.getInstance(activity).getRequestQueue();
//        //Log.e("SAN AstroListF", " url = "+url);
//        //Log.e("SAN AstroListF", " paramsHashMap = "+paramsHashMap);
//        GZipRequest postRequest = new GZipRequest(Request.Method.POST, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        //Log.e("SAN TestList", "sendGzipRequestAstro() onResponse"+System.currentTimeMillis());
//                        timeDifference = System.currentTimeMillis() - timeDifference;
//                        //Toast.makeText(activity, "onResponse="+timeDifference, Toast.LENGTH_SHORT).show();
//                        //Log.e("SAN AstroListF", "onResponse = "+response);
//                        if (activity == null || getActivity() == null) return;
//                        hideLogoProgressBar();
//                        hideSwipeRefreshProgressBar();
//                        //Log.e("SAN AIA ", " Astro List Res Time=> " + respTime + " res=> " + response);
//                        if (response != null && response.length() > 0) {
//                            try {
//                                CUtils.saveAstroList(response);
//                                //Log.e("SAN CHAT ", " HomeFragment1 onResponse() sendGzipRequestAstro " );
//                                parseAstrologerList(response);
//                            } catch (Exception e) {
//                            }
//                        }
//                    }
//                },
//                new Response.ErrorListener() {
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        //Log.e("SAN TestList", "sendGzipRequestAstro() onErrorResponse"+System.currentTimeMillis());
//                        timeDifference = System.currentTimeMillis() - timeDifference;
//                        //Toast.makeText(activity, "onError="+timeDifference, Toast.LENGTH_SHORT).show();
//                        Log.e("sharepdftest", " Astro List Res Time=> " + error);
//                        if (activity == null || getActivity() == null) return;
//                        try {
//                            hideLogoProgressBar();
//                            hideSwipeRefreshProgressBar();
//                            if (astrologerAdapter.getItemCount() == 0) {
//                                llReloadData.setVisibility(View.VISIBLE);
//                            }
//                            CUtils.showVolleyErrorRespMessage(containerLayout, activity, error, "3");
//                        } catch (Exception e) {
//                            e.printStackTrace();
//                        }
//                    }
//                }
//        ) {
//            @Override
//            public String getBodyContentType() {
//                return "application/x-www-form-urlencoded; charset=UTF-8";
//            }
//
//            @Override
//            public Map<String, String> getParams() {
//                return paramsHashMap;
//            }
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                Map<String, String> params = new HashMap<>();
//                params.putAll(super.getHeaders());
//                params.put("Accept-Encoding", "gzip,deflate");
//                return params;
//            }
//        };
//
//        int socketTimeout = 60000;//30 seconds - change to what you want
//        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
//        postRequest.setRetryPolicy(policy);
//        postRequest.setShouldCache(true);
//        queue.add(postRequest);
//    }

    private void checkNextRechargeOffer(String offerType) {
        try {
            if (activity == null) return;
            if (CUtils.popUpLoginFreeCallClicked) {
                CUtils.popUpLoginFreeCallClicked = false;
                callWithRandomAstrologer();
                return;
            } else if (CUtils.popUpLoginFreeChatClicked) {
                CUtils.popUpLoginFreeChatClicked = false;
                boolean enabledAIFreeChatPopup = com.ojassoft.astrosage.utils.CUtils.isAIfreeChatPopupEnabled(activity);
                String firstFreeChatType = CUtils.getStringData(activity, com.ojassoft.astrosage.utils.CGlobalVariables.FIRST_FREE_CHAT_TYPE, "");
                String secondFreeChatType = CUtils.getStringData(activity, com.ojassoft.astrosage.utils.CGlobalVariables.SECOND_FREE_CHAT_TYPE, "");

                if (enabledAIFreeChatPopup) {
                    if (CUtils.isSecondFreeChat(activity) && offerType.equals(CGlobalVariables.INTRO_OFFER_TYPE_FREE)) {
                        if (secondFreeChatType.equalsIgnoreCase(com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_FREE_CHAT_AI)) {
                            CUtils.initiateRandomAiChat(activity, CGlobalVariables.AI_FREE_CHAT_POP_UP_OR_ASK_TAB,com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_FREE_CHAT_AI);
                        } else {
                            CUtils.initiateRandomChat(activity, CGlobalVariables.FREE_CHAT_POP_UP_OR_ASK_TAB, com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_FREE_CHAT_HUMAN);
                        }
                    } else {
                        if (firstFreeChatType.equalsIgnoreCase(com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_FREE_CHAT_AI)) {
                            CUtils.initiateRandomAiChat(activity, CGlobalVariables.AI_FREE_CHAT_POP_UP_OR_ASK_TAB,com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_FREE_CHAT_AI);
                        } else {
                            CUtils.initiateRandomChat(activity, CGlobalVariables.FREE_CHAT_POP_UP_OR_ASK_TAB, com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_FREE_CHAT_HUMAN);
                        }
                    }
                } else {
                    CUtils.initiateRandomChat(activity, CGlobalVariables.FREE_CHAT_POP_UP_OR_ASK_TAB, com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_FREE_CHAT_HUMAN);
                }
                return;
            }

            if (activity instanceof DashBoardActivity) {
                if (((DashBoardActivity) activity).showDialogOnce) {
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

    public void showLogoProgressbar() {
        //Log.d("TestList", "showLogoProgressbar()");
        try {
            llGifProgress.setVisibility(View.VISIBLE);
            llNoAstroFound.setVisibility(View.GONE);
            llReloadData.setVisibility(View.GONE);
            Glide.with(activity.getApplicationContext()).load(R.drawable.new_ai_loader).into(logoGif);
        } catch (Exception e) {
            //
        }

    }

    private void hideSwipeRefreshProgressBar() {
        if (mSwipeRefreshLayout != null) {
            mSwipeRefreshLayout.setRefreshing(false);
            mSwipeRefreshLayout.setEnabled(true);
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
    public void categoryItemClick(int position) {
        categoryPosition = position;
        //isFilterApply = false;
        if (categoryArr != null && !categoryArr.isEmpty()) {
            for (int i = 0; i < categoryArr.size(); i++) {
                if (i == position) {
                    categoryArr.get(position).setSelected(true);
                } else {
                    categoryArr.get(i).setSelected(false);
                }
            }
            if (filterAstrologerAdapter != null) {
                filterAstrologerAdapter.notifyDataSetChanged();
            }
            if (position > 0) {
                isTabRefresh = true;
                TYPE_TAB_FILTER = categoryArr.get(position).getID();
                onTabClick(TYPE_TAB_FILTER);
                //onFilterItemClick()
            } else {
                isTabRefresh = false;
                TYPE_TAB_FILTER = "";
                if (arrayListRecyclerView != null)
                    arrayListRecyclerView.clear();
                arrayListRecyclerView.addAll(filteredarrayList);
            }
            updateAstroListAdapter();
        }
    }


    private void notifyAdapter() {
        if (categoryArr != null && !categoryArr.isEmpty()) {
            for (int i = 0; i < categoryArr.size(); i++) {
                if (i == 0) {
                    categoryArr.get(0).setSelected(true);
                } else {
                    categoryArr.get(i).setSelected(false);
                }
            }
            filterAstrologerAdapter.notifyDataSetChanged();
        }
    }

    private void notificationAlertDialog() {
        // custom dialog
        final Dialog dialog = new Dialog(getContext());
        dialog.setContentView(R.layout.notification_alert_layout);
        Button dialogButton = (Button) dialog.findViewById(R.id.btnNotifAlertYes);
        Button btn_no = (Button) dialog.findViewById(R.id.btnNotifAlertNo);

        // if button is clicked, close the custom dialog
        dialogButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog.dismiss();
                // Toast.makeText(getApplicationContext(),"Dismissed..!!",Toast.LENGTH_SHORT).show();
            }
        });
        // if button is clicked, close the custom dialog
        btn_no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                // Toast.makeText(getApplicationContext(),"Dismissed..!!",Toast.LENGTH_SHORT).show();
            }
        });
        dialog.show();
    }

    private String getFreeAstrologersId() {
        String freeAstrologersId = "";
        try {
            if (arrayListRecyclerViewAllAstrologer != null && !arrayListRecyclerViewAllAstrologer.isEmpty()) {
                for (int i = 0; i < arrayListRecyclerViewAllAstrologer.size(); i++) {
                    AstrologerDetailBean astrologerDetailBean = arrayListRecyclerViewAllAstrologer.get(i);
                    if (astrologerDetailBean == null) continue;
                    if (astrologerDetailBean.isFreeForChat() || astrologerDetailBean.isFreeForCall()) {
                        freeAstrologersId = freeAstrologersId + astrologerDetailBean.getAstrologerId() + ",";
                    }
                }
                freeAstrologersId = freeAstrologersId.substring(0, freeAstrologersId.length() - 1);
            }
        } catch (Exception e) {
            //
        }
        return freeAstrologersId;
    }

    public void getAstrologersStatusPrice() {
        if (activity == null || getActivity() == null) return;

        String freeAstrologersId = getFreeAstrologersId();

        HashMap<String, String> params = getParams();
        String astroGroup = AstrosageKundliApplication.astroGroup;
        if (astroGroup == null) astroGroup = "";
        params.put("astrogroup", astroGroup);
        params.put("astrologerids", freeAstrologersId);

        AstrosageKundliApplication.astroListLogs += "\n\n\n AstroStatus Params=" + params + "\n";
        //Log.d("TestStatus", "ASTROLOGERS_STATUS_N_PRICE_URL params="+params);
        //Log.e("SAN CHAT ", " HomeFragment1 getAstrologersStatusPrice() calling sendGzipRequest()" );
        sendGzipRequest(CGlobalVariables.ASTROLOGERS_STATUS_N_PRICE_URL, params);
    }


    private void sendGzipRequest(String url, final HashMap<String, String> paramsHashMap) {
        final long apiHitTime = System.currentTimeMillis();
        RequestQueue queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
        GZipRequest postRequest = new GZipRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        long respTime = System.currentTimeMillis() - apiHitTime;
                        //Log.e("SAN ", " Astro List Res Time=> " + respTime + " res=> " + response);
                        if (response != null && !response.isEmpty()) {
                            try {
                                //Log.e("SAN CHAT ", " HomeFragment1 sendGzipRequest() calling parseAstrologerStatus()" );
                                parseAstrologerStatus(response);
                            } catch (Exception e) {

                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("ksdfksad", error.toString());
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

    private void parseAstrologerStatus(String responseData) {
        if (activity == null || getActivity() == null) return;
        try {
            AstrosageKundliApplication.astroListLogs += "AstroStatus responseData=" + responseData + "\n";
            JSONObject jsonObject = new JSONObject(responseData);
            JSONObject jsonObjectAstro = jsonObject.getJSONObject("astrologers");

            String offerType = jsonObject.optString("offertype");
            //boolean isSecondFreeChat  = jsonObject.optBoolean("sfc");
            //CUtils.setSecondFreeChat(activity, isSecondFreeChat);
            Log.e("TestStatus", " Astro List Res offerType2=> " + offerType);
            //Log.e("SAN CHAT ", " HomeFragment1 parseAstrologerStatus() calling handleOfferType()" );
            handleOfferType(offerType);
            //Log.d("TestStatus", jsonObjectAstro.length()+" arrayListRecyclerViewAllAstrologer= "+arrayListRecyclerViewAllAstrologer.size());

            Iterator<AstrologerDetailBean> iterator = arrayListRecyclerViewAllAstrologer.iterator();
            while (iterator.hasNext()) {
                AstrologerDetailBean astrologerDetailBean = iterator.next();
                if (astrologerDetailBean == null) continue;
                String astrologerId = astrologerDetailBean.getAstrologerId();
                if (jsonObjectAstro.has(astrologerId)) {
                    JSONObject object = jsonObjectAstro.getJSONObject(astrologerId);

                    //Log.e("TestStatus", " AstrologerId exists" + astrologerId);

                    astrologerDetailBean.setIsOnline(object.optString("io"));
                    astrologerDetailBean.setIsAvailableForCall(object.optString("iafc"));
                    astrologerDetailBean.setIsAvailableForChat(object.optString("iafch"));

                    astrologerDetailBean.setIsBusy(object.optString("ib"));
                    astrologerDetailBean.setServicePrice(object.optString("pr"));
                    astrologerDetailBean.setActualServicePriceInt(object.optString("apri"));
                    astrologerDetailBean.setUseIntroOffer(object.optBoolean("uif"));
                    astrologerDetailBean.setOfferRemaining(object.optBoolean("ofc"));
                    astrologerDetailBean.setFreeForCall(object.optBoolean("ioc"));
                    astrologerDetailBean.setFreeForChat(object.optBoolean("ioch"));
                    //Log.d("TestStatus", astrologerId +" isBusy "+object.optBoolean("ib")+ " FreeForCall "+object.optBoolean("ioc")+" FreeForChat "+object.optBoolean("ioch"));

                } else {
                    iterator.remove();
                    //Log.e("TestStatus", " AstrologerId does not exists" + astrologerId);
                }
            }
            //Log.d("TestStatus", " arrayListRecyclerViewAllAstrologer= "+arrayListRecyclerViewAllAstrologer.size());
            if (arrayListRecyclerViewAllAstrologer.isEmpty()) {
                isTabRefresh = false;
                getAstrologerListFromServer(false);
            } else {
                updateAstrologerList();
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void registerReceiverRefundFreeChat() {
        LocalBroadcastManager.getInstance(activity).registerReceiver(mReceiverRefundFreeChat
                , new IntentFilter(CGlobalVariables.SEND_BROADCAST_REFUND_FREE_CHAT));
    }

    private final BroadcastReceiver mReceiverRefundFreeChat = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Log.e("SAN HF1 ", "Refund Free Chat onRecieve()");
            onResume();
        }
    };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mReceiverRefundFreeChat != null) {
            LocalBroadcastManager.getInstance(activity).unregisterReceiver(mReceiverRefundFreeChat);
        }
    }

}