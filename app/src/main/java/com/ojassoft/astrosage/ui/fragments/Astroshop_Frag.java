package com.ojassoft.astrosage.ui.fragments;

import static com.ojassoft.astrosage.ui.act.BaseInputActivity.SUB_ACTIVITY_UPGRADE_PLAN_UPDTAE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.SCREEN_ID_DHRUV;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.IS_AI_CALL_SHOW_IN_AI_CHAT;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AdData;
import com.ojassoft.astrosage.customadapters.AstroShopCategoryAdapter;
import com.ojassoft.astrosage.misc.AstroShopDataDownloadService;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.model.CustomAddModel;
import com.ojassoft.astrosage.model.ProductCategory;
import com.ojassoft.astrosage.model.UrlTitleModel;
import com.ojassoft.astrosage.ui.act.ActAppModule;
import com.ojassoft.astrosage.ui.act.ActAstroShopServices;
import com.ojassoft.astrosage.ui.act.ActYearly;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.act.CogniAstroActivity;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amit Rautela on 23/2/16.
 *
 * Renders Astroshop modules and promotional banners, and wires user selections
 * to the appropriate deep links or native flows.
 */
public class Astroshop_Frag extends Fragment {

    public Integer[] moduleIconList = {
            R.drawable.brihat_horoscope_icon,
            R.drawable.ic_gemstone,
            R.drawable.ic_yantra,
            R.drawable.ic_rudraskh,
            R.drawable.ic_mala,
            //R.drawable.icon_vastu_painting,
            R.drawable.ic_jadi,
            R.drawable.ic_service,
            //R.drawable.ic_astro,
            R.drawable.icon_kundli_ai_plus,
            R.drawable.icon_cogni_astro,
            R.drawable.icon_aroma,
            R.drawable.ic_misc};

    private String[] moduleNameList;

    private static final String gemStoneDeeplink = "https://buy.astrosage.com/gemstone";
    private static final String yantraDeeplink = "https://buy.astrosage.com/yantra";
    private static final String rudrakshDeeplink = "https://buy.astrosage.com/rudraksha";
    private static final String malaDeeplink = "https://buy.astrosage.com/mala";
    private static final String navgrahDeeplink = "https://buy.astrosage.com/navagrah-yantra";
    private static final String jadiDeeplink = "https://buy.astrosage.com/jadi-tree-roots";
    private static final String brihatHorscopeDeepLinkUrl = "https://buy.astrosage.com/virtual/astrosage-brihat-horoscope-url";
    private static final String fengshuiDeeplink = "https://buy.astrosage.com/fengshui";
    private static final String miscDeeplink = "https://buy.astrosage.com/miscellaneous";
    //private static String miscDeeplink = "https://buy.astrosage.com/miscellaneous";
    //private static String miscDeeplink = "https://buy.astrosage.com/miscellaneous";

    // private static String miscDeeplink = "https://buy.astrosage.com/miscellaneous";


    RecyclerView gridView;
    NetworkImageView networkImageView;

    static Activity activity;
    static final int BRIHATHOROSCOPE = 0;
    static final int GEMSTONE = 1;
    static final int YANTRAS = 2;
    static final int RUDRAKSHA = 3;
    static final int MALA = 4;
    //static final int NAVAGRAH = 5;
    static final int JADI = 5;
    static final int SERVICE = 6;
    //static final int ASTROLOGER = 8;
    static final int DHRUV = 7;
    static final int COGNI_ASTRO = 8;
    static final int FENGSHUI = 9;
    static final int MISC = 10;

    //static final int MISC = 9;
    static final String BigHorscopeId = "Bighorscope_Main_Module";
    public static final String GemstoneId = "Gemstone_Main_Module";
    static final String YantrasId = "Yantra_Main_Module";
    static final String RudrakshaId = "Rudraksha_Main_Module";
    static final String MalaId = "Mala_Main_Module";
    static final String NavagrahId = "Navagrah_Yantra_Main_Module";
    static final String JadiId = "Jadi_Main_Module";
    public static final String BrihatHorscopeId = "Brihathorscope_Main_Module";
    static final String fengshuiId = "Fengshui_Main_Module";
    static final String miscId = "Misc_Main_Module";
    // static final String MiscId = "Miscellaneous";

    Typeface typeface;
    private static int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    // Progress dialog shown while the Astroshop category payload is loading.
    CustomProgressDialog pd = null;
    ArrayList<CustomAddModel> sliderList;

    private String IsShowBanner = "False";
    private String IsShowBanner1 = "False";
    private ArrayList<AdData> adList;
    AdData topAdData, bottomAdData;
    private NetworkImageView topAdImage, bottomoAdImage;
    private List<ProductCategory> categoryList;
    // Tracks whether we've already triggered the data download to avoid duplicate calls.
    private boolean hasRequestedData = false;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Astroshop_Frag.activity = activity;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    /**
     * Loads cached ad metadata and resolves the top/bottom ad slots.
     */
    private void getData() {
        Log.d("TestAdd", "getData()");
        try {
            String result = CUtils.getStringData(getActivity(), "CUSTOMADDS", "");
            if (result != null && !result.equals("")) {
                adList = new Gson().fromJson(result, new TypeToken<ArrayList<AdData>>() {
                }.getType());
                topAdData = CUtils.getSlotData(adList, "8");
                bottomAdData = CUtils.getSlotData(adList, "9");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.modules_frag, container, false);
        LANGUAGE_CODE = ((AstrosageKundliApplication) getActivity().getApplicationContext())
                .getLanguageCode();
        typeface = CUtils.getRobotoFont(getActivity(), LANGUAGE_CODE, CGlobalVariables.regular);

        setLayRef(rootView);

        bottomoAdImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CUtils.googleAnalyticSendWitPlayServie(getActivity(),
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_SLOT_9_Add, null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SLOT_9_Add, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                CUtils.createSession(getActivity(), "S9");

                CustomAddModel modal = bottomAdData.getImageObj().get(0);

                CUtils.divertToScreen(getActivity(), modal.getImgthumbnailurl(), LANGUAGE_CODE);
            }
        });

        topAdImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CUtils.googleAnalyticSendWitPlayServie(getActivity(),
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_SLOT_8_Add, null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SLOT_8_Add, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                CUtils.createSession(getActivity(), "S8");

                CustomAddModel modal = topAdData.getImageObj().get(0);

                CUtils.divertToScreen(getActivity(), modal.getImgthumbnailurl(), LANGUAGE_CODE);


            }
        });
        //   frameLay.setVisibility(View.VISIBLE);
        return rootView;
    }

    /**
     * @created by : Amit Rautela
     * @created on : 23/2/16.
     * @desc : This method is used to look up the layout reference
     */
    private void setLayRef(View rootView) {
        networkImageView = rootView.findViewById(R.id.imgBanner);
        ((ActAppModule) getActivity()).getDataFromGTMCointainer(networkImageView);
        moduleNameList = getResources().getStringArray(R.array.astroshopModulesList);
        gridView = rootView.findViewById(R.id.gridView2);
        gridView.setLayoutManager(new GridLayoutManager(requireContext(), 3)); // 3 Columns
        //gridView.setAdapter(new AstroShopCategoryAdapter(activity, categoryList, ((BaseInputActivity) getActivity()).mediumTypeface, Astroshop_Frag.this));
        gridView.setVisibility(View.VISIBLE);
       // gridView.setExpanded(true);
        gridView.setFocusable(false);

        topAdImage = rootView.findViewById(R.id.topAdImage);
        bottomoAdImage = rootView.findViewById(R.id.bottomoAdImage);

        setAstroShopAdapter();


        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getData();
                if (topAdData != null && topAdData.getImageObj() != null && topAdData.getImageObj().size() > 0) {
                    setTopAdd(topAdData);
                }
                if (bottomAdData != null && bottomAdData.getImageObj() != null && bottomAdData.getImageObj().size() > 0) {
                    setBottomAd(bottomAdData);
                }


            }
        }, 1000);//1 secons delay
    }
    private BroadcastReceiver copaReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null && intent.getAction().equals(AstroShopDataDownloadService.COPA_RESULT)) {
                String status = intent.getStringExtra(AstroShopDataDownloadService.COPA_MESSAGE);
                if ("1".equals(status)) {
                    hasRequestedData = false;
                    setAstroShopAdapter();
                } else {
                    hideProgressDialog();
                }
            }
        }
    };

    /**
     * Hydrates the grid from cached data if available; otherwise triggers
     * a background download and shows the loader.
     */
    private void setAstroShopAdapter(){

        //Log.d("TestAdd", "setAstroShopAdapter called");
        String entry = CUtils.getStringData(requireContext(), CGlobalVariables.Astroshop_Data + LANGUAGE_CODE, "");
        //Log.d("wiConvert", "getData() -"+entry);
        //Log.d("TestAdd", "LANGUAGE_CODE -"+LANGUAGE_CODE);

        if (!TextUtils.isEmpty(entry)) {
            hideProgressDialog();
            hasRequestedData = false;
            categoryList = parseCategories(entry);
            if (categoryList == null){
                CUtils.saveStringData(requireContext(), CGlobalVariables.Astroshop_Data + LANGUAGE_CODE, "");
                hasRequestedData = true;
                CUtils.startAstroShopeDataDownloadService(requireContext());
                showProgressDialog();
                return;
            }
            AstroShopCategoryAdapter adapter = new AstroShopCategoryAdapter(activity, categoryList, ((BaseInputActivity) getActivity()).mediumTypeface);
            gridView.setAdapter(adapter);
        }else{
            if (!hasRequestedData) {
                hasRequestedData = true;
                CUtils.startAstroShopeDataDownloadService(requireContext());
                showProgressDialog();
            } else {
                hideProgressDialog();
            }
        }


    }

    /**
     * Parses the Astroshop category payload and injects static categories
     * at fixed positions to support legacy layout expectations.
     */
    private List<ProductCategory> parseCategories(String json) {
        List<ProductCategory> categories = new ArrayList<>();
        // Adding static category at position 0
        categories.add(new ProductCategory(
                getString(R.string.astro_shop_module_brihat),
                getString(R.string.astro_shop_module_brihat),
                "Brihat Description",
                "brihat",
                null,
                String.valueOf(R.drawable.ic_brihat_kundli_round) // Set from drawable resource
        ));

        try {
            JSONArray jsonArray = new JSONArray(json).getJSONObject(0).getJSONArray("ProductsCategoryName");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
               // Log.d("TestAdd", "CategoryFullName -"+obj.getString("CategoryShortName"));


                categories.add(new ProductCategory(
                        obj.getString("CategoryFullName"),
                        obj.getString("CategoryShortName"),
                        obj.getString("CategorySmallDescription"),
                        obj.getString("CategoryUrl"),
                        obj.getString("CategoryImagePath"),
                        null
                ));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            com.ojassoft.astrosage.varta.utils.CUtils.sendLogDataRequest("", "Astroshop_Frag", "Astro shop categories parsing error");
            //FirebaseCrashlytics.getInstance().recordException(e,new CustomKeysAndValues.Builder().putString("Astroshop_Frag","AstroShopDataDownloadService").build());
            return null;
        }

        // Adding static category at position 6
        if (categories.size() >= 6) {
            categories.add(6,new ProductCategory(
                    getString(R.string.astro_shop_astro_services),
                    getString(R.string.astro_shop_astro_services),
                    "Service Description",
                    "services",
                    null,
                    String.valueOf(R.drawable.ic_dollers_round) // Set from drawable resource
            ));
        }
        // Adding static category at position 8

        String planNameToShow = ((CUtils.getUserPurchasedPlanFromPreference(activity))>=8)?getString(R.string.astro_shop_dhruv):getString(R.string.astro_shop_platinum_plan);
        int planIconToShow = ((CUtils.getUserPurchasedPlanFromPreference(activity))>=8)?R.drawable.ic_dhruv_plan_round:R.drawable.ic_kundli_ai_round;

        if (categories.size() >= 7) {
            categories.add(7, new ProductCategory(
                    planNameToShow,
                    planNameToShow,
                    "Kundli AI+ Description",
                    "kundli ai+",
                    null,
                    String.valueOf(planIconToShow) // Set as per plan
            ));
        }
        // Adding static category at position 8
        if (categories.size() >= 8) {
            categories.add(8, new ProductCategory(
                    getString(R.string.astro_shop_module_cogni_astro),
                    getString(R.string.astro_shop_module_cogni_astro),
                    "CogniAstro Description",
                    "cogniastro",
                    null,
                    String.valueOf(R.drawable.ic_cogniastro_round) // Set from drawable resource
            ));
        }


        return categories;
    }

    /**
     * @param position
     * @param mainModuleId
     * @created by : Amit Rautela
     * @created on : 23/2/16.
     */
    public static void callActivity(Activity activity ,int position, String deepUrl, String mainModuleId,String titleName) {
        AstrosageKundliApplication.webViewIssue = AstrosageKundliApplication.webViewIssue+"\n"+"Fragment callActivity called  ==>>"+activity+" ==>>>>"+position+" ==>>>>"+deepUrl+" ==>>>>"+mainModuleId+" ==>>>>"+titleName;

        String deepLinkUrl = "https://buy.astrosage.com/"+deepUrl;

        int moduleType = 0;
//        if (position == BRIHATHOROSCOPE) {
//
//            CUtils.getUrlLink(brihatHorscopeDeepLinkUrl, activity, LANGUAGE_CODE, 0);
//
//            CUtils.googleAnalyticSendWitPlayServie(activity,
//                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT, BrihatHorscopeId, null);
//            CUtils.fcmAnalyticsEvents(BrihatHorscopeId, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
//
//
//            //CUtils.getUrlLink(bigHorscopeDeepLinkUrl, activity, LANGUAGE_CODE, 0);
//
//        } else if (position == GEMSTONE) {
//
//            CUtils.getUrlLink(gemStoneDeeplink, activity, LANGUAGE_CODE, 0);
//            CUtils.googleAnalyticSendWitPlayServie(activity,
//                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT, GemstoneId, null);
//            CUtils.fcmAnalyticsEvents(GemstoneId, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
//
//            // moduleType = checkModule(GemstoneId);
//        } else if (position == YANTRAS) {
//            CUtils.getUrlLink(yantraDeeplink, activity, LANGUAGE_CODE, 0);
//
//            CUtils.googleAnalyticSendWitPlayServie(activity,
//                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT, YantrasId, null);
//            CUtils.fcmAnalyticsEvents(YantrasId, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
//
//            //  moduleType = checkModule(YantrasId);
//        } else if (position == RUDRAKSHA) {
//            CUtils.getUrlLink(rudrakshDeeplink, activity, LANGUAGE_CODE, 0);
//            //  moduleType = checkModule(RudrakshaId);
//
//            CUtils.googleAnalyticSendWitPlayServie(activity,
//                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT, RudrakshaId, null);
//            CUtils.fcmAnalyticsEvents(RudrakshaId, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
//
//        } else if (position == MALA) {
//            CUtils.getUrlLink(malaDeeplink, activity, LANGUAGE_CODE, 0);
//            // moduleType = checkModule(MalaId);
//
//            CUtils.googleAnalyticSendWitPlayServie(activity,
//                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT, MalaId, null);
//            CUtils.fcmAnalyticsEvents(MalaId, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
//
//
//        } /*else if (position == NAVAGRAH) {
//            CUtils.getUrlLink(navgrahDeeplink, activity, LANGUAGE_CODE, 0);
//            //moduleType = checkModule(NavagrahId);
//
//            CUtils.googleAnalyticSendWitPlayServie(activity,
//                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT, NavagrahId, null);
//            CUtils.fcmAnalyticsEvents(NavagrahId, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
//
//        } */else if (position == JADI) {
//            CUtils.getUrlLink(jadiDeeplink, activity, LANGUAGE_CODE, 0);
//            //moduleType = checkModule(JadiId);
//
//            CUtils.googleAnalyticSendWitPlayServie(activity,
//                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT, JadiId, null);
//            CUtils.fcmAnalyticsEvents(JadiId, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
//
//        } /*else if (position == MISC) {
//            CUtils.getUrlLink(miscDeeplink, activity, LANGUAGE_CODE, 0);
//            //moduleType = checkModule(MiscId);
//        }*/ else if (position == SERVICE) {
//            moduleType = CGlobalVariables.MODULE_ASTROSHOP_SERVICE;
//            Intent i = new Intent(activity, ActAstroShopServices.class);
//            activity.startActivity(i);
//            CUtils.googleAnalyticSendWitPlayServie(activity,
//                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
//                    CGlobalVariables.GOOGLE_ANALYTIC_ASTROSHOP_SERVICE, null);
//            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_ASTROSHOP_SERVICE, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
//
//            return;
//        } else if (position == COGNI_ASTRO) {
//            CUtils.googleAnalyticSendWitPlayServie(activity,
//                    CGlobalVariables.GOOGLE_ANALYTIC_LAUNCH,
//                    CGlobalVariables.GOOGLE_ANALYTIC_COGNI_ASTRO, null);
//            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_COGNI_ASTRO, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
//
//            Intent intent = new Intent(activity, CogniAstroActivity.class);
//            activity.startActivity(intent);
//
//        } else if (position == DHRUV) {
//            CUtils.googleAnalyticSendWitPlayServie(activity,
//                    CGlobalVariables.GOOGLE_ANALYTIC_LAUNCH,
//                    CGlobalVariables.GOOGLE_ANALYTIC_DHRUV, null);
//            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_DHRUV, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
//
//            if (CUtils.isDhruvPlan(activity)) {
//                Intent intent = new Intent(activity, ActUserPlanDetails.class);
//                activity.startActivity(intent);
//                return;
//            }
//            CUtils.gotoProductPlanListUpdated(activity, ActAppModule.LANGUAGE_CODE, SUB_ACTIVITY_UPGRADE_PLAN_UPDTAE, SCREEN_ID_DHRUV, "upgrade_plan");
//        } else if (position == FENGSHUI) {
//            CUtils.getUrlLink(fengshuiDeeplink, activity, LANGUAGE_CODE, 0);
//            //moduleType = checkModule(JadiId);
//
//            CUtils.googleAnalyticSendWitPlayServie(activity,
//                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT, fengshuiId, null);
//            CUtils.fcmAnalyticsEvents(fengshuiId, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
//
//        } else if (position == MISC) {
//            CUtils.getUrlLink(miscDeeplink, activity, LANGUAGE_CODE, 0);
//            //moduleType = checkModule(JadiId);
//
//            CUtils.googleAnalyticSendWitPlayServie(activity,
//                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT, miscId, null);
//            CUtils.fcmAnalyticsEvents(miscId, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
//
//        }
        if (position == BRIHATHOROSCOPE) {

            CUtils.getUrlLink(brihatHorscopeDeepLinkUrl, activity, LANGUAGE_CODE, 0);

            CUtils.googleAnalyticSendWitPlayServie(activity,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT, BrihatHorscopeId, null);
            CUtils.fcmAnalyticsEvents(BrihatHorscopeId, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");


            //CUtils.getUrlLink(bigHorscopeDeepLinkUrl, activity, LANGUAGE_CODE, 0);

        } else if (position == SERVICE) {
            moduleType = CGlobalVariables.MODULE_ASTROSHOP_SERVICE;
            Intent i = new Intent(activity, ActAstroShopServices.class);
            i.putExtra(CGlobalVariables.SOUCRE_ACTIVITY, "AstroShop_Fragment");
            activity.startActivity(i);
            CUtils.googleAnalyticSendWitPlayServie(activity,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                    CGlobalVariables.GOOGLE_ANALYTIC_ASTROSHOP_SERVICE, null);
            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_ASTROSHOP_SERVICE, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

            return;
        } else if (position == COGNI_ASTRO) {
            CUtils.googleAnalyticSendWitPlayServie(activity,
                    CGlobalVariables.GOOGLE_ANALYTIC_LAUNCH,
                    CGlobalVariables.GOOGLE_ANALYTIC_COGNI_ASTRO, null);
            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_COGNI_ASTRO, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

            Intent intent = new Intent(activity, CogniAstroActivity.class);
            activity.startActivity(intent);

        } else if (position == DHRUV) {
            CUtils.googleAnalyticSendWitPlayServie(activity,
                    CGlobalVariables.GOOGLE_ANALYTIC_LAUNCH,
                    CGlobalVariables.GOOGLE_ANALYTIC_DHRUV, null);
            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_DHRUV, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");


            //in both plans redirect to Product Plan list
//            if (CUtils.isDhruvPlan(activity)) {
//                Intent intent = new Intent(activity, ActUserPlanDetails.class);
//                activity.startActivity(intent);
//                return;
//            }
            CUtils.gotoProductPlanListUpdated(activity, ActAppModule.LANGUAGE_CODE, SUB_ACTIVITY_UPGRADE_PLAN_UPDTAE, SCREEN_ID_DHRUV, "purchase_from_astro_shop_frag");
        } else {
            boolean shoppingcartviawebview = com.ojassoft.astrosage.varta.utils.CUtils.getBooleanData(activity, CGlobalVariables.SHOPPING_CART_VIA_WEBVIEW, true);
            AstrosageKundliApplication.webViewIssue = AstrosageKundliApplication.webViewIssue+"\n"+" Fragment shoppingcartviawebview  ==>>"+shoppingcartviawebview;
            AstrosageKundliApplication.webViewIssue = AstrosageKundliApplication.webViewIssue+"\n"+"Fragment position  ==>>"+position;

            if(shoppingcartviawebview &&( position==1 ||position==2 ||position==3||position == 4||position==11||position==13)){
                UrlTitleModel urlTitleModel = CUtils.getUrlForWebView(position,activity);

                com.ojassoft.astrosage.varta.utils.CUtils.addFacebookAndFirebaseEvent(CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK,urlTitleModel.getCategory(),"Astroshop_Frag");
                CUtils.openAstroSageShopWebView(activity, urlTitleModel.getUrl(),CGlobalVariables.AKHOMETAB, urlTitleModel.getCategory());
                AstrosageKundliApplication.webViewIssue = AstrosageKundliApplication.webViewIssue+"\n"+"Fragment urlTitleModel.getUrl()  ==>>"+urlTitleModel.getUrl();
                if(!CUtils.getBooleanData(activity,CGlobalVariables.PRODUCT_CATEGORY_NOTIFICATION,false)){
                    com.ojassoft.astrosage.varta.utils.CUtils.sendNotificationWithLink(activity, activity.getString(R.string.coupon_code_for_10_off_on_your_next_product_order),activity.getString(R.string.your_discount_coupon_code_for_your_first_product_order_welcome10),urlTitleModel.getUrl());
                    CUtils.saveBooleanData(activity,CGlobalVariables.PRODUCT_CATEGORY_NOTIFICATION,true);
                }

            }else {
                AstrosageKundliApplication.webViewIssue = AstrosageKundliApplication.webViewIssue+"\n"+"Fragment else condition for native  ==>>";

                // Deep link for non-webview product categories.
                CUtils.getUrlLink(deepUrl, activity, LANGUAGE_CODE, 0);
                CUtils.googleAnalyticSendWitPlayServie(activity,
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT, mainModuleId, null);
                CUtils.fcmAnalyticsEvents(mainModuleId, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
            }
            // moduleType = checkModule(GemstoneId);
        }
    }



    /**
     * Extracts display names for analytics or secondary UI rendering.
     */
    private static ArrayList<String> parseAstroShopData(String saveData) {
        ArrayList<String> categoryFullName = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(saveData);
            JSONObject productCategoryNames = jsonArray.getJSONObject(0);
            JSONArray jsonArrayProductCategoryName = productCategoryNames.optJSONArray("ProductsCategoryName");
            for (int i = 0; i < jsonArrayProductCategoryName.length(); i++) {
                categoryFullName.add(jsonArrayProductCategoryName.getJSONObject(i).getString("CategoryFullName"));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return categoryFullName;
    }


    /**
     * Binds the top banner, honoring per-slot flags and plan restrictions.
     */
    public void setTopAdd(AdData topData) {
        Log.d("TestAdd", "setTopAdd()");
        getData();
        if (topData != null) {
            IsShowBanner = topData.getIsShowBanner();
            IsShowBanner = IsShowBanner == null ? "" : IsShowBanner;

        }
        if (topData == null || topData.getImageObj() == null || topData.getImageObj().size() <= 0 || IsShowBanner.equalsIgnoreCase("False")) {
            if (topAdImage != null) {

                topAdImage.setVisibility(View.GONE);
            }
        } else {
            if (topAdImage != null) {
                topAdImage.setVisibility(View.VISIBLE);
                topAdImage.setImageUrl(topData.getImageObj().get(0).getImgurl(), VolleySingleton.getInstance(getActivity()).getImageLoader());
            }
        }

        if (CUtils.getUserPurchasedPlanFromPreference(activity) != 1) {
            if (topAdImage != null) {
                topAdImage.setVisibility(View.GONE);
            }
        }
    }


    /**
     * Binds the bottom banner, honoring per-slot flags and plan restrictions.
     */
    public void setBottomAd(AdData bottomData) {
        Log.d("TestAdd", "setBottomAd() called");
        if (bottomData != null) {
            IsShowBanner1 = bottomData.getIsShowBanner();
            IsShowBanner1 = IsShowBanner1 == null ? "" : IsShowBanner1;

        }
        if (bottomData == null || bottomData.getImageObj() == null || bottomData.getImageObj().size() <= 0 || IsShowBanner1.equalsIgnoreCase("False")) {
            if (bottomoAdImage != null) {
                bottomoAdImage.setVisibility(View.GONE);
            }
        } else {
            if (bottomoAdImage != null) {
                bottomoAdImage.setVisibility(View.VISIBLE);
                bottomoAdImage.setImageUrl(bottomData.getImageObj().get(0).getImgurl(), VolleySingleton.getInstance(getActivity()).getImageLoader());
            }
        }
        if (CUtils.getUserPurchasedPlanFromPreference(activity) != 1) {
            if (bottomoAdImage != null) {
                bottomoAdImage.setVisibility(View.GONE);
            }
        }

    }

    @Override
    public void onPause() {
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(copaReceiver);
        super.onPause();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (pd != null){
            pd.dismiss();
            pd = null;
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(AstroShopDataDownloadService.COPA_RESULT);
        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(copaReceiver, intentFilter);

    }

    /**
     * Displays the blocking loader while category data is fetched.
     */
    private void showProgressDialog() {
        try {
            if (pd == null) {
                pd = new CustomProgressDialog(requireContext(), typeface);
                pd.show();
            }
        }catch(Exception e){
            //
        }

    }
    /**
     * Hides the blocking loader if it is currently visible.
     */
    private void hideProgressDialog() {
        try {
            if (pd != null) {
                pd.dismiss();
            }
        } catch (Exception e) {
            //
        }
    }

}
