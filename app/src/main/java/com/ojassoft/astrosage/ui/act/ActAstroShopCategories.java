package com.ojassoft.astrosage.ui.act;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.widget.Toolbar;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AdData;
import com.ojassoft.astrosage.customadapters.AstroShopCategoryAdapter;
import com.ojassoft.astrosage.misc.AstroShopDataDownloadService;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.model.CustomAddModel;
import com.ojassoft.astrosage.model.ProductCategory;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.VolleySingleton;

import java.util.ArrayList;
import java.util.List;

import static com.ojassoft.astrosage.utils.CGlobalVariables.SCREEN_ID_DHRUV;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

//import com.google.analytics.tracking.android.EasyTracker;
//import com.google.analytics.tracking.android.Log;

/**
 * Created by ojas on १३/५/१६.
 */
public class ActAstroShopCategories extends BaseInputActivity {

    private TextView tvTitle;
    private Toolbar tool_barAppModule;
    Typeface typeface;
    private static int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    private RecyclerView gridView;
    private TabLayout tabLayout;
    AdData bottomAdData;
    private ArrayList<AdData> adList;
    private String[] moduleNameList;
    static Activity activity;
    private String IsShowBanner1 = "False";

    public ActAstroShopCategories() {
        super(R.string.app_name);
    }

    public Integer[] moduleIconList = {
            R.drawable.brihat_horoscope_icon,
            R.drawable.ic_gemstone,
            R.drawable.ic_yantra,
            R.drawable.ic_rudraskh,
            R.drawable.ic_mala,
            R.drawable.ic_navgrah,
            R.drawable.ic_jadi,
            R.drawable.ic_service,
            //R.drawable.ic_astro,
            R.drawable.icon_kundli_ai_plus,
            R.drawable.icon_cogni_astro,
            R.drawable.ic_fengshui,
            R.drawable.ic_misc};

    private static String gemStoneDeeplink = "https://buy.astrosage.com/gemstone";
    private static String yantraDeeplink = "https://buy.astrosage.com/yantra";
    private static String rudrakshDeeplink = "https://buy.astrosage.com/rudraksha";
    private static String malaDeeplink = "https://buy.astrosage.com/mala";
    private static String navgrahDeeplink = "https://buy.astrosage.com/navagrah-yantra";
    private static String jadiDeeplink = "https://buy.astrosage.com/jadi-tree-roots";
    private static String brihatHorscopeDeepLinkUrl = "https://buy.astrosage.com/virtual/astrosage-brihat-horoscope-url";
    private static String fengshuiDeeplink = "https://buy.astrosage.com/fengshui";
    private static String miscDeeplink = "https://buy.astrosage.com/miscellaneous";


    private static final int BRIHATHOROSCOPE = 0;
    private static final int GEMSTONE = 1;
    private static final int YANTRAS = 2;
    private static final int RUDRAKSHA = 3;
    private static final int MALA = 4;
    private static final int NAVAGRAH = 5;
    private static final int JADI = 6;
    private static final int SERVICE = 7;
    //static final int ASTROLOGER = 8;
    private static final int DHRUV = 8;
    private static final int COGNI_ASTRO = 9;
    private static final int FENGSHUI = 10;
    private static final int MISC = 11;


    private static final String BigHorscopeId = "Bighorscope_Main_Module";
    private static final String GemstoneId = "Gemstone_Main_Module";
    private static final String YantrasId = "Yantra_Main_Module";
    private static final String RudrakshaId = "Rudraksha_Main_Module";
    private static final String MalaId = "Mala_Main_Module";
    private static final String NavagrahId = "Navagrah_Yantra_Main_Module";
    private static final String JadiId = "Jadi_Main_Module";
    static final String BrihatHorscopeId = "Brihathorscope_Main_Module";
    static final String fengshuiId = "Fengshui_Main_Module";
    static final String miscId = "Misc_Main_Module";
    private NetworkImageView bottomoAdImage;
    private List<ProductCategory> categoryList;
    private CustomProgressDialog pd = null;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        LANGUAGE_CODE = ((AstrosageKundliApplication) ActAstroShopCategories.this.getApplication())
                .getLanguageCode();
        typeface = CUtils.getRobotoFont(
                ActAstroShopCategories.this.getApplicationContext(), LANGUAGE_CODE, CGlobalVariables.regular);
        setContentView(R.layout.lay_astro_shop_categories);
        tool_barAppModule = (Toolbar) findViewById(R.id.tool_barAppModule);
        setSupportActionBar(tool_barAppModule);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(getResources().getString(R.string.home_astro_shop));
        tvTitle.setTypeface(typeface);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setVisibility(View.GONE);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        init();
        getData();
        bottomoAdImage = (NetworkImageView) findViewById(R.id.bottomoAdImage);
        if (bottomAdData != null && bottomAdData.getImageObj() != null && bottomAdData.getImageObj().size() > 0) {
            setBottomAd(bottomAdData);
        }

        bottomoAdImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CUtils.googleAnalyticSendWitPlayServie(ActAstroShopCategories.this,
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_SLOT_9_Add, null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SLOT_9_Add, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                CUtils.createSession(ActAstroShopCategories.this, "S9");
                CustomAddModel modal = bottomAdData.getImageObj().get(0);
                CUtils.divertToScreen(ActAstroShopCategories.this, modal.getImgthumbnailurl(), LANGUAGE_CODE);


            }
        });
    }

    private void init() {
        gridView = findViewById(R.id.gridView);
        gridView.setLayoutManager(new GridLayoutManager(this, 3)); // 3 Columns

        moduleNameList = getResources().getStringArray(R.array.astroshopModulesList);
        setAstroShopAdapter();
    }

    private BroadcastReceiver copaReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction() != null && intent.getAction().equals(AstroShopDataDownloadService.COPA_RESULT)) {
                //String id = intent.getStringExtra(COPA_MESSAGE);
                //Log.e("CopaReceiver", "Received ID: " + id);
                setAstroShopAdapter();
            }
        }
    };

    private void getData() {

        try {
            String result = CUtils.getStringData(this, "CUSTOMADDS", "");
            if (result != null && !result.equals("")) {
                adList = new Gson().fromJson(result, new TypeToken<ArrayList<AdData>>() {
                }.getType());
                bottomAdData = CUtils.getSlotData(adList, "9");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void setBottomAd(AdData bottomData) {
        getData();
        if (bottomData != null) {
            IsShowBanner1 = bottomData.getIsShowBanner();
            IsShowBanner1 = IsShowBanner1 == null ? "" : IsShowBanner1;

        }
        if (bottomAdData == null || bottomData.getImageObj() == null || bottomData.getImageObj().size() <= 0 || IsShowBanner1.equalsIgnoreCase("False")) {
            if (bottomoAdImage != null) {
                bottomoAdImage.setVisibility(View.GONE);
            }
        } else {
            if (bottomoAdImage != null) {
                bottomoAdImage.setVisibility(View.VISIBLE);
                bottomoAdImage.setImageUrl(bottomData.getImageObj().get(0).getImgurl(), VolleySingleton.getInstance(this).getImageLoader());
            }
        }
        if (CUtils.getUserPurchasedPlanFromPreference(activity) != 1) {
            if (bottomoAdImage != null) {
                bottomoAdImage.setVisibility(View.GONE);
            }
        }


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * @param position
     */
    public static void callActivity(int position) {
        if (position == BRIHATHOROSCOPE) {

            CUtils.getUrlLink(brihatHorscopeDeepLinkUrl, activity, LANGUAGE_CODE, 0);

            CUtils.googleAnalyticSendWitPlayServie(activity,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT, BrihatHorscopeId, null);
            CUtils.fcmAnalyticsEvents(BrihatHorscopeId, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
            // moduleType = checkModule(GemstoneId);
        }else if (position == GEMSTONE) {

            CUtils.getUrlLink(gemStoneDeeplink, activity, LANGUAGE_CODE, 0);
            CUtils.googleAnalyticSendWitPlayServie(activity,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT, GemstoneId, null);
            CUtils.fcmAnalyticsEvents(GemstoneId, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

            // moduleType = checkModule(GemstoneId);
        } else if (position == YANTRAS) {
            CUtils.getUrlLink(yantraDeeplink, activity, LANGUAGE_CODE, 0);

            CUtils.googleAnalyticSendWitPlayServie(activity,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT, YantrasId, null);
            CUtils.fcmAnalyticsEvents(YantrasId, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

            //  moduleType = checkModule(YantrasId);
        } else if (position == RUDRAKSHA) {
            CUtils.getUrlLink(rudrakshDeeplink, activity, LANGUAGE_CODE, 0);
            //  moduleType = checkModule(RudrakshaId);

            CUtils.googleAnalyticSendWitPlayServie(activity,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT, RudrakshaId, null);
            CUtils.fcmAnalyticsEvents(RudrakshaId, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

        } else if (position == MALA) {
            CUtils.getUrlLink(malaDeeplink, activity, LANGUAGE_CODE, 0);
            // moduleType = checkModule(MalaId);

            CUtils.googleAnalyticSendWitPlayServie(activity,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT, MalaId, null);
            CUtils.fcmAnalyticsEvents(MalaId, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");


        } else if (position == NAVAGRAH) {
            CUtils.getUrlLink(navgrahDeeplink, activity, LANGUAGE_CODE, 0);
            //moduleType = checkModule(NavagrahId);

            CUtils.googleAnalyticSendWitPlayServie(activity,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT, NavagrahId, null);
            CUtils.fcmAnalyticsEvents(NavagrahId, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

        } else if (position == JADI) {
            CUtils.getUrlLink(jadiDeeplink, activity, LANGUAGE_CODE, 0);
            //moduleType = checkModule(JadiId);

            CUtils.googleAnalyticSendWitPlayServie(activity,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT, JadiId, null);
            CUtils.fcmAnalyticsEvents(JadiId, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

        } else if (position == SERVICE) {
            Intent i = new Intent(activity, ActAstroShopServices.class);
            activity.startActivity(i);
            CUtils.googleAnalyticSendWitPlayServie(activity,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                    CGlobalVariables.GOOGLE_ANALYTIC_ASTROSHOP_SERVICE, null);
            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_ASTROSHOP_SERVICE, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

            return;
        } /*else if (position == ASTROLOGER) {
            Intent intent = new Intent(activity, ActAstrologer.class);
            activity.startActivity(intent);

            CUtils.googleAnalyticSendWitPlayServie(activity,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                    CGlobalVariables.GOOGLE_ANALYTIC_ASTROSHOP_ASTROLOGER, null);
            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_ASTROSHOP_ASTROLOGER, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

            return;
        }*/
        else if (position == COGNI_ASTRO) {
            CUtils.googleAnalyticSendWitPlayServie(activity,
                    CGlobalVariables.GOOGLE_ANALYTIC_LAUNCH,
                    CGlobalVariables.GOOGLE_ANALYTIC_COGNI_ASTRO, null);
            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_COGNI_ASTRO, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

            Intent intent = new Intent(activity, CogniAstroActivity.class);
            activity.startActivity(intent);

        }
        else if (position == DHRUV) {
            CUtils.googleAnalyticSendWitPlayServie(activity,
                    CGlobalVariables.GOOGLE_ANALYTIC_LAUNCH,
                    CGlobalVariables.GOOGLE_ANALYTIC_DHRUV, null);
            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_DHRUV, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

            if (CUtils.isDhruvPlan(activity)) {
                Intent intent = new Intent(activity, ActUserPlanDetails.class);
                activity.startActivity(intent);
                return;
            }
            CUtils.gotoProductPlanListUpdated(activity, ActAppModule.LANGUAGE_CODE, SUB_ACTIVITY_UPGRADE_PLAN_UPDTAE, SCREEN_ID_DHRUV, "upgrade_plan");
        }

        else if (position == FENGSHUI) {
            CUtils.getUrlLink(fengshuiDeeplink, activity, LANGUAGE_CODE, 0);
            //moduleType = checkModule(JadiId);

            CUtils.googleAnalyticSendWitPlayServie(activity,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT, fengshuiId, null);
            CUtils.fcmAnalyticsEvents(fengshuiId, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

        }
        else if (position == MISC) {
            CUtils.getUrlLink(miscDeeplink, activity, LANGUAGE_CODE, 0);
            //moduleType = checkModule(JadiId);

            CUtils.googleAnalyticSendWitPlayServie(activity,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT, miscId, null);
            CUtils.fcmAnalyticsEvents(miscId, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

        }

    }
    private void setAstroShopAdapter(){

        //Log.d("TestAdd", "setAstroShopAdapter called");
        String entry = CUtils.getStringData(this, CGlobalVariables.Astroshop_Data + LANGUAGE_CODE, "");
        //Log.d("wiConvert", "getData() -"+entry);
        //Log.d("TestAdd", "LANGUAGE_CODE -"+LANGUAGE_CODE);

        if (!TextUtils.isEmpty(entry)) {
            hideProgressDialog();
            categoryList = parseCategories(entry);
            AstroShopCategoryAdapter adapter = new AstroShopCategoryAdapter(activity, categoryList, this.mediumTypeface);
            gridView.setAdapter(adapter);
        }else{
            CUtils.startAstroShopeDataDownloadService(this);
            showProgressDialog();
        }


    }

    @Override
    protected void onResume() {
        super.onResume();
        IntentFilter intentFilter = new IntentFilter(AstroShopDataDownloadService.COPA_RESULT);
        LocalBroadcastManager.getInstance(this).registerReceiver(copaReceiver, intentFilter);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //EasyTracker.getInstance().activityStart(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(copaReceiver);
    }

    private List<ProductCategory> parseCategories(String json) {
        List<ProductCategory> categories = new ArrayList<>();
        // Adding static category at position 0
        categories.add(new ProductCategory(
                getString(R.string.module_brihat),
                getString(R.string.module_brihat),
                "Brihat Description",
                "brihat",
                null,
                String.valueOf(R.drawable.ic_brihat_kundli_round) // Set from drawable resource
        ));

        try {
            JSONArray jsonArray = new JSONArray(json).getJSONObject(0).getJSONArray("ProductsCategoryName");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = jsonArray.getJSONObject(i);
                Log.d("TestAdd", "CategoryFullName -"+obj.getString("CategoryShortName"));


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
        }
        // Adding static category at position 6
        if (categories.size() >= 6) {
            categories.add(6,new ProductCategory(
                    getString(R.string.astro_services),
                    getString(R.string.astro_services),
                    "Service Description",
                    "services",
                    null,
                    String.valueOf(R.drawable.ic_dollers_round) // Set from drawable resource
            ));
        }
        // Adding static category at position 8
        if (categories.size() >= 7) {
            categories.add(7, new ProductCategory(
                    getString(R.string.platinum_plan),
                    getString(R.string.platinum_plan),
                    "Kundli AI+ Description",
                    "kundli ai+",
                    null,
                    String.valueOf(R.drawable.ic_kundli_ai_round) // Set from drawable resource
            ));
        }
        // Adding static category at position 8
        if (categories.size() >= 8) {
            categories.add(8, new ProductCategory(
                    getString(R.string.module_cogni_astro),
                    getString(R.string.module_cogni_astro),
                    "CogniAstro Description",
                    "cogniastro",
                    null,
                    String.valueOf(R.drawable.ic_cogniastro_round) // Set from drawable resource
            ));
        }


        return categories;
    }

    private void showProgressDialog() {
        try {
            if (pd == null) {
                pd = new CustomProgressDialog(this, typeface);
                pd.show();
            }
        }catch(Exception e){
            //
        }

    }
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
