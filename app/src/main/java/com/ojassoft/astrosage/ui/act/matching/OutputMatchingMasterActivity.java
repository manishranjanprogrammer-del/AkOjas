package com.ojassoft.astrosage.ui.act.matching;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.analytics.tracking.android.EasyTracker;
//import com.google.analytics.tracking.android.Log;
import com.google.firebase.perf.metrics.AddTrace;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AdData;
import com.ojassoft.astrosage.beans.BeanDateTime;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.controller.ControllerManager;
import com.ojassoft.astrosage.customexceptions.NoInternetException;
import com.ojassoft.astrosage.customexceptions.UICOnlineChartOperationException;
import com.ojassoft.astrosage.jinterface.IChooseLanguageFragment;
import com.ojassoft.astrosage.misc.CalculateKundli;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.misc.DownloadReceiver;
import com.ojassoft.astrosage.misc.DownloadService;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.act.ActAppModule;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.act.FlashLoginActivity;
import com.ojassoft.astrosage.ui.act.HomeInputScreen;
import com.ojassoft.astrosage.ui.act.OutputMasterActivity;
import com.ojassoft.astrosage.ui.customcontrols.AppRater;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.ui.fragments.AskQuestionsFragment;
import com.ojassoft.astrosage.ui.fragments.DownloadMarriagePDF;
import com.ojassoft.astrosage.ui.fragments.HomeNavigationDrawerFragment;
import com.ojassoft.astrosage.ui.fragments.KundliAIFirstTimeMsgFragment;
import com.ojassoft.astrosage.ui.fragments.SearchBirthDetailsFragment;
import com.ojassoft.astrosage.ui.fragments.matching.MatchingOutputBoyAndGirlDetailFragment;
import com.ojassoft.astrosage.ui.fragments.matching.MatchingOutputConclusionFragment;
import com.ojassoft.astrosage.ui.fragments.matching.MatchingOutputDetailResultNorthFragment;
import com.ojassoft.astrosage.ui.fragments.matching.MatchingOutputNorthInterpretationFragment;
import com.ojassoft.astrosage.utils.CGlobal;
import com.ojassoft.astrosage.utils.CGlobalMatching;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.PopUpLogin;
import com.ojassoft.astrosage.varta.dao.KundliHistoryDao;
import com.ojassoft.astrosage.varta.ui.MiniChatWindow;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.varta.utils.TypeWriter;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static android.os.Build.VERSION.SDK_INT;
import static com.ojassoft.astrosage.ui.act.horoscope.HoroscopeHomeActivity.BACK_FROM_PLAN_PURCHASE_AD_SCREEN;
import static com.ojassoft.astrosage.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_OUTPUT_MATCHING_MASTER_BOTTOM_CALL;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_OUTPUT_MATCHING_MASTER_BOTTOM_CHAT;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_OUTPUT_MATCHING_MASTER_BOTTOM_KUNDLI;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_OUTPUT_MATCHING_MASTER_BOTTOM_LIVE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTIC_DOWNLOAD_PDF;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTIC_DOWNLOAD_PDF_MATCHING_ACTIONBAR;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTIC_DOWNLOAD_PDF_MATCHING_SIDEMENU;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_PDF_SHARE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_RECEIVER;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_URL;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KUNDLI_AI_OPEN_FIRST_TIME_KEY;
import static com.ojassoft.astrosage.utils.CGlobalVariables.MODULE_SUGGESTED_QUESTIONS_KEY;
import static com.ojassoft.astrosage.utils.CGlobalVariables.OUTPUT_MATCHING_MASTER_BOTTOM_BAR_CALL_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.OUTPUT_MATCHING_MASTER_BOTTOM_BAR_CHAT_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.OUTPUT_MATCHING_MASTER_BOTTOM_BAR_KUNDLI_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.OUTPUT_MATCHING_MASTER_BOTTOM_BAR_LIVE_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.matchingScreenIds;
import static com.ojassoft.astrosage.utils.CUtils.createSession;
import static com.ojassoft.astrosage.utils.CUtils.isPopupLoginShown;
import static com.ojassoft.astrosage.utils.CUtils.openKundliActivity;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.APP_KEY;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.CURRENT_SCREEN_ID_KEY;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_CONVERSATION_ID;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_SCREEN_NAME;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.MATCHING_KEY;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.SOURCE_OF_SCREEN;
import static com.ojassoft.astrosage.varta.utils.CUtils.getApplicationSignatureHashCode;
import static com.ojassoft.astrosage.varta.utils.CUtils.getLocaleResources;
import static com.ojassoft.astrosage.varta.utils.CUtils.gotoAllLiveActivityFromLiveIcon;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/*import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.ActionBar.Tab;*/
//import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

public class OutputMatchingMasterActivity extends BaseInputActivity
        implements IChooseLanguageFragment {

   /* public static final int FILE = 0;
    public static final int HOME = 1;
    public static final int NEW_MATCHING = 2;
    public static final int EDIT_INPUT_DETAIL = 3;
    //UPDATED BY BIJENDRA ON 19-MAY-14

    public static final int VIEW_BOY_KUNDLI = 4;
    public static final int VIEW_GIRL_KUNDLI = 5;
    public static final int DOWNLOAD_PDF = 6;
    public static final int EMAIL_SCREEN = 7;
    public static final int SETTINGS = 8;
    public static final int CHANGE_LANGUAGE = 9;
    public static final int MISC = 10;
    public static final int FEEDBACK = 11;
    public static final int RATE_KUNDLI_APP = 12;

    public static final int PRODUCT_PLAN_LIST = 13;
    public static final int SHARE_APP = 14;
    public static final int OUR_OTHER_APPS = 15;
    public static final int CALL_US = 16;
    public static final int ASTRO_SHOP = 17;
    public static final int ASK_OUR_ASTROLOGER = 18;
    public static final int ABOUT_US = 19;
    public static final int CLOUD_SIGN_OUT = 20;*/


    String[] pageTitles;
    // PagerTabStrip myPagerTabStrip;
    ViewPager viewPager;

    BeanDateTime beanDateTimeInput;
    SearchBirthDetailsFragment searchBirthDetailsFragment;

    Toolbar toolBar_InputKundli;
    // private SlidingTabLayoutInputKundli tabs_input_kundli;
    // BirthDetailInputFragment birthDetailInputFragment;
    // MatchingInputDetailFragment matchingInputDetailFragment;
    MatchingOutputConclusionFragment matchingOutputConclusionFragment;
    MatchingOutputDetailResultNorthFragment matchingOutputDetailResultNorthFragment;
    MatchingOutputNorthInterpretationFragment matchingOutputNorthInterpretationFragmentVARNA;
    MatchingOutputNorthInterpretationFragment matchingOutputNorthInterpretationFragmentVASYA;
    MatchingOutputNorthInterpretationFragment matchingOutputNorthInterpretationFragmentTARA;
    MatchingOutputNorthInterpretationFragment matchingOutputNorthInterpretationFragmentYONI;
    MatchingOutputNorthInterpretationFragment matchingOutputNorthInterpretationFragmentMAITRI;
    MatchingOutputNorthInterpretationFragment matchingOutputNorthInterpretationFragmentGANA;
    MatchingOutputNorthInterpretationFragment matchingOutputNorthInterpretationFragmentBHAKOOT;
    MatchingOutputNorthInterpretationFragment matchingOutputNorthInterpretationFragmentNADI;
    MatchingOutputBoyAndGirlDetailFragment matchingOutputBoyAndGirlDetailFragment;
    DownloadMarriagePDF downloadMarriagePDF;

    public static int MATCHING_CONCLUSION = 0;
    public static int VARNA = 1;
    public static int VASYA = 2;
    public static int TARA = 3;
    public static int YONI = 4;
    public static int MAITRI = 5;
    public static int GANA = 6;
    public static int BHAKOOT = 7;
    public static int NADI = 8;
    public static int DOWNLOADPDF = 9;
    int LANGUAGE_CODE2;

    //MatchingOutputMenuFrag matchingOutputMenuFrag;

    private TextView tvToolBarTitle;

    public OutputMatchingMasterActivity() {
        super(R.string.app_name);
    }

    private CountDownTimer myCountDownTimer;

    ImageView ivToggleImage, imgHome, imgMoreItem, imgShare, icDownload;

    private TabLayout tabLayout;

    HomeNavigationDrawerFragment drawerFragment;
    ProgressBar progressBar;
    boolean isPdfShare;
    LinearLayout navView;
    FloatingActionButton fabOutputMaster;

    String heading = "";
    String subHeading = "";

    RelativeLayout ask_question_layout;
    TypeWriter tv_ask_quest;

    HashMap<String, ArrayList<String>> questionMap;
    String conversationId;
    KundliHistoryDao chatDao;
    private final String GET_SUGGESTED_QUESTION_DATE = "matching_suggested_questions_date";
    private final String SUGGESTED_QUESTION_KEY = "matching_suggested_questions";

    private boolean isKundliChatWindowShowing = false;

    @AddTrace(name = "onOutputMatchingMasterActivityCreateTrace", enabled = true /* optional */)
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {

            if (savedInstanceState != null) {
                try {
                    CGlobalMatching cGlobal = (CGlobalMatching) savedInstanceState.getSerializable(CGlobalVariables.outPutMatchingMasterActSavedBundleKey);
                    CGlobalMatching.setCGlobalMatching(cGlobal);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            setContentView(R.layout.lay_output_matching_screen);
            /*typeface = CUtils.getUserSelectedLanguageFontType(
                    getApplicationContext(),
                    CUtils.getLanguageCodeFromPreference(getApplicationContext()));*/

            isPopupLoginShown = false;

            heading = getResources().getString(R.string.pop_up_heading_kundli_matching);
            subHeading = getResources().getString(R.string.pop_up_sub_heading_kundli_matching);


            toolBar_InputKundli = (Toolbar) findViewById(R.id.tool_barAppModule);
            tvToolBarTitle = (TextView) toolBar_InputKundli.findViewById(R.id.tvTitle);
            ivToggleImage = (ImageView) toolBar_InputKundli.findViewById(R.id.ivToggleImage);
            imgHome = (ImageView) toolBar_InputKundli.findViewById(R.id.imgHome);
            imgMoreItem = (ImageView) toolBar_InputKundli.findViewById(R.id.imgMoreItem);
            imgShare = (ImageView) toolBar_InputKundli.findViewById(R.id.share);
            icDownload = toolBar_InputKundli.findViewById(R.id.icDownload);
            ask_question_layout = findViewById(R.id.ask_question_layout);
            tv_ask_quest = findViewById(R.id.tv_ask_que);
            ivToggleImage.setVisibility(View.VISIBLE);
            imgHome.setVisibility(View.VISIBLE);
            imgMoreItem.setVisibility(View.VISIBLE);
            imgShare.setVisibility(View.VISIBLE);
            icDownload.setVisibility(View.VISIBLE);

            getSuggestQuestionForMatchingScreens();

            setVisibilityOfMoreIconImage(imgMoreItem, getResources().getStringArray(
                    R.array.matching_output_menu_item_list), getResources().obtainTypedArray(
                    R.array.matching_output_menu_item_list_icon), matching_output_menu_item_list_index);

            imgHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    CUtils.gotoHomeScreen(OutputMatchingMasterActivity.this);
                }
            });
            imgShare.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    sharePDF(true);
                    CUtils.googleAnalyticSendWitPlayServie(OutputMatchingMasterActivity.this,
                            CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                            CGlobalVariables.GOOGLE_ANALYTIC_SHARE_TOP_BUTTON, null);
                    com.ojassoft.astrosage.varta.utils.CUtils.addFacebookAndFirebaseEvent(CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK,GOOGLE_ANALYTIC_DOWNLOAD_PDF,CGlobalVariables.GOOGLE_ANALYTIC_SHARE_TOP_BUTTON);
                }
            });

            icDownload.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CUtils.googleAnalyticSendWitPlayServie(OutputMatchingMasterActivity.this,
                            CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                            GOOGLE_ANALYTIC_DOWNLOAD_PDF,
                            GOOGLE_ANALYTIC_DOWNLOAD_PDF_MATCHING_ACTIONBAR);

                    com.ojassoft.astrosage.varta.utils.CUtils.addFacebookAndFirebaseEvent(CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK,GOOGLE_ANALYTIC_DOWNLOAD_PDF,GOOGLE_ANALYTIC_DOWNLOAD_PDF_MATCHING_ACTIONBAR);
                    sharePDF(false);
                }
            });

            //tabs_input_kundli = (SlidingTabLayoutInputKundli) findViewById(R.id.tabs_input_kundli);
            // tabs_input_kundli.initTextTypeface(typeface);
            // tabs_input_kundli.setCustomTabView(R.layout.lay_input_kundli_tab_title,
            // R.id.tabtext);
            setSupportActionBar(toolBar_InputKundli);

            drawerFragment = (HomeNavigationDrawerFragment) getSupportFragmentManager().
                    findFragmentById(R.id.myDrawerFrag);

            drawerFragment.setup(R.id.myDrawerFrag, (DrawerLayout) findViewById(R.id.drawerLayout), toolBar_InputKundli, getDrawerListItem(), getDrawerListItemIcon(), getDrawerListItemIndex());


            viewPager = (ViewPager) findViewById(R.id.pager);
            tabLayout = (TabLayout) findViewById(R.id.tabs);
            try {
                configureActionBarTabStyle();
            } catch (Exception e) {
                e.printStackTrace();
            }
            setViewPagerAdapter();
            //		setViewPagerListeners();

            // Menu
            //  getSlidingMenu().setMode(SlidingMenu.LEFT_RIGHT);

            // Primary menu
            // setBehindContentView(R.layout.menu_frame);
            /*
             * getSupportFragmentManager() .beginTransaction()
             * .replace(R.id.menu_frame, new
             * MatchingOutputRightMenuFragment()).commit();
             */

            //   getSlidingMenu().setSecondaryMenu(R.layout.menu_frame);
            //  getSlidingMenu().setSecondaryShadowDrawable(R.drawable.shadowright);


           /* getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.menu_frame,
                            new MatchingOutputRightMenuFragment()).commit();*/

            // Secondary menu
            //  getSlidingMenu().setSecondaryMenu(R.layout.menu_frame_two);
            //   getSlidingMenu().setSecondaryShadowDrawable(R.drawable.shadowright);
            /*
             * getSupportFragmentManager() .beginTransaction()
             * .replace(R.id.menu_frame_two, new
             * MatchingOutpuLeftMenuFragment()) .commit();
             */
            /*matchingOutputMenuFrag = new MatchingOutputMenuFrag();
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.menu_frame_two,
                            matchingOutputMenuFrag).commit();*/
            // customize the SlidingMenu
            // getSlidingMenu().setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
            //	getSupportActionBar().setDisplayShowTitleEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            //viewPager.setCurrentItem(0);
            setCurrentView(0, false);


            // tabs_input_kundli.setDistributeEvenly(true); // To make the Tabs Fixed set this true, This makes the tabs Space Evenly in Available width

            // Setting Custom Color for the Scroll bar indicator of the Tab View
           /* tabs_input_kundli.setCustomTabColorizer(new SlidingTabLayoutInputKundli.TabColorizer() {
                @Override
                public int getIndicatorColor(int position) {
                    return getResources().getColor(R.color.tabsScrollColor);
                }
            });*/


            // tabs_input_kundli.setViewPager(viewPager);
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            //CUtils.showAdvertisement(this,(LinearLayout) findViewById(R.id.advLayout));
//			getSupportActionBar().setLogo(getResources().getDrawable(R.drawable.astrosage_logo));

            if (CUtils.isUserAllowingToShowHindiTextMessage(OutputMatchingMasterActivity.this)) {
                if ((CUtils.getLanguageCodeFromPreference(getApplicationContext()) == CGlobalVariables.HINDI) && (!SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE)) {
                    showHindiSupportMessage();
                }
            }

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
//		hide actionbar up indicator
		/*try {
			int upId = Resources.getSystem().getIdentifier("up", "id", "android");
			if(upId > 0){
			    ImageView upImage = (ImageView)findViewById(upId);
			    upImage.setImageResource(R.drawable.indicator);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}*/
//		end

      /*  ((ImageView) toolBar_InputKundli.findViewById(R.id.ivLeft)).setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                toggle();
                ;
            }
        });*/
        chatDao = KundliHistoryDao.getInstance(this);
        String onlineBoyChartId =  CGlobalMatching.getCGlobalMatching().getBoyPersonalDetail().getOnlineChartId();
        String onlineGirlChatId = CGlobalMatching.getCGlobalMatching().getGirlPersonalDetail().getOnlineChartId();
        //Log.e("KundliChatAi","Output = boy online_chart_id = "+onlineBoyChartId + " girl online_chat_id = " + onlineGirlChatId);

        if(onlineBoyChartId.isEmpty()|| onlineBoyChartId.equals("-1")  || onlineGirlChatId.isEmpty() || onlineGirlChatId.equals("-1")){
            conversationId = CGlobalMatching.getCGlobalMatching().getBoyPersonalDetail().toString()+"_"+CGlobalMatching.getCGlobalMatching().getGirlPersonalDetail().toString();
        }else{
            conversationId = onlineBoyChartId+"_"+onlineGirlChatId;
        }
        conversationId = chatDao.getConversationId(conversationId,getString(R.string.matching));
        Log.e("KundliChatAi","Output = "+conversationId);
        fabOutputMaster = findViewById(R.id.fabHome);
        navView = findViewById(R.id.nav_view);
        //navView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        customBottomNavigationFont(navView);
        setBottomNavigationText();
        fabOutputMaster.setOnClickListener(v->{
            fabActions();
        });
        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                        PopUpLogin popUpLogin = new PopUpLogin
                                (com.ojassoft.astrosage.varta.utils.CGlobalVariables.HOROSCOPE_MATCHING,
                                        heading,
                                        subHeading,
                                        R.drawable.astrologer_icon_2);
                        popUpLogin.show(getSupportFragmentManager(), "PopUpFreeCall");

                }catch (Exception e){
                    //
                }
            }
        }, 15000);*/

        ask_question_layout.setOnClickListener(v-> {

            if (isKundliChatWindowShowing) {
                return; //prevent to show chat window multiple times
            }

            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_KUNDLI_AI_MATCHING_CHAT_BUTTON_CLICK, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

            if (com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(this)) {
                boolean isFirstTime = CUtils.getBooleanData(this, KUNDLI_AI_OPEN_FIRST_TIME_KEY, true);
                if (isFirstTime) {
                    KundliAIFirstTimeMsgFragment kundliAIFirstTimeMsgFragment = new KundliAIFirstTimeMsgFragment(() -> {
                        openKundliAIChatWindow();
                        CUtils.saveBooleanData(this,KUNDLI_AI_OPEN_FIRST_TIME_KEY,false);
                    });
                    kundliAIFirstTimeMsgFragment.show(getSupportFragmentManager(), "kundliAIWelcomeMsg");

                    return;
                }
                openKundliAIChatWindow();
            } else {
                isPopupLoginShown = true;
                AstrosageKundliApplication.isOpenVartaPopup = true;
                isKundliChatWindowShowing = true;
                Intent intent1 = new Intent(this, FlashLoginActivity.class);
                startActivity(intent1);
                /*PopUpLogin popUpLogin = new PopUpLogin
                        (com.ojassoft.astrosage.varta.utils.CGlobalVariables.KUNDALI,
                                "ONLY_LOGIN");
                popUpLogin.show(getSupportFragmentManager(), "PopUpLogin");
                isPopupLoginShown = true;
                AstrosageKundliApplication.isOpenVartaPopup = true;
                isKundliChatWindowShowing = true;*/
                //startActivity(new Intent(OutputMasterActivity.this, LoginSignUpActivity.class));
            }
        });
        // Check if the interstitial/home popup feature is enabled from tagmanager
        boolean isPopupEnabled = com.ojassoft.astrosage.varta.utils.CUtils.getBooleanData(this, CGlobalVariables.IS_INTERSTICIAL_ENABLED, false);
        // Check if the user is already a premium subscriber (Kundli AI+ or Dhruv Plan)
        boolean isPremiumUser = CUtils.isKundliAIPlusPlan(this) || CUtils.isDhruvPlan(this);
        // Check if the "Upgrade after 10 charts" dialog has already been shown
        boolean isUpgradeDialogShown = CUtils.getBooleanData(this, CGlobalVariables.UPGRADE_AFTER_TEN_CHART_DIALOG_IS_SHOWN, false);
        // Logic: Show Ad if feature enabled + user NOT premium + upgrade dialog NOT shown + time interval allows
        if (isPopupEnabled && !isUpgradeDialogShown && !isPremiumUser && CUtils.canShowInterstitial(this)) {
            // Redirect to the Plan Purchase screen (acting as an Ad)
            com.ojassoft.astrosage.varta.utils.CUtils.openPurchasePlanScreenForAd(
                    OutputMatchingMasterActivity.this,
                    false,
                    false,
                    com.ojassoft.astrosage.varta.utils.CGlobalVariables.SOURCE_FROM_KUNDLI_MATCHING_AD,
                    true,
                    BACK_FROM_PLAN_PURCHASE_AD_SCREEN
            );
        }



    }

    private void openKundliAIChatWindow() {
        int screenId = matchingScreenIds[viewPager.getCurrentItem()];
        ArrayList<String> suggestedQuestions = getSuggestedQuestionsForScreenId(screenId);
        Intent intent = new Intent(OutputMatchingMasterActivity.this, MiniChatWindow.class);
        intent.putStringArrayListExtra(MODULE_SUGGESTED_QUESTIONS_KEY, suggestedQuestions);
        intent.putExtra(MATCHING_KEY, true);
        intent.putExtra(SOURCE_OF_SCREEN, com.ojassoft.astrosage.varta.utils.CGlobalVariables.SOURCE_FROM_MATCHING_SCREEN);
        intent.putExtra(CURRENT_SCREEN_ID_KEY, screenId);
        if (LANGUAGE_CODE == 1) {
            intent.putExtra(KEY_SCREEN_NAME, getRomanScreenName(viewPager.getCurrentItem()));
        } else {
            intent.putExtra(KEY_SCREEN_NAME, pageTitles[(viewPager.getCurrentItem())]);
        }
        intent.putExtra(KEY_CONVERSATION_ID,conversationId);
        startActivity(intent);
        isKundliChatWindowShowing = true;
    }

    @Override
    public void setVisibilityOfMoreIconImage(View view, final String[] subMenuItems, final TypedArray subMenuItemsIcon, final Integer[] menuIndex) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingForpopUpMenu(view, subMenuItems, subMenuItemsIcon, menuIndex);
            }
        });
    }

    private void popupToRateApp() {
        myCountDownTimer = new CountDownTimer(RATE_DIALOG_THREAD_SLEEP_TIME,
                1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onFinish() {
                // TODO Auto-generated method stub
                if(!com.ojassoft.astrosage.varta.utils.CUtils.isPopUpLoginShowing){
                    String Headingtext = OutputMatchingMasterActivity.this
                            .getString(R.string.app_mainheading_text_matching);
                    String SubHeadingtext = OutputMatchingMasterActivity.this
                            .getString(R.string.app_subheading_text_matching);
                    String Subchildheading = OutputMatchingMasterActivity.this
                            .getString(R.string.app_subchild_text_matching);
                    AppRater.app_launched(OutputMatchingMasterActivity.this,
                            Headingtext, SubHeadingtext, Subchildheading);
                }

            }
        };
        myCountDownTimer.start();
    }

    private void configureActionBarTabStyle() {

        pageTitles = getResources().getStringArray(R.array.output_matching_tab_list);
		/*ActionBar actionBar = getSupportActionBar();
	    actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
	    ActionBar.TabListener tabListener = new ActionBar.TabListener() {
	        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
	            // show the given tab
	        	viewPager.setCurrentItem(tab.getPosition());
	        }

	        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
	            // hide the given tab
	        }

	        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
	            // probably ignore this event
	        }
	    };
	    
	    Tab tab = actionBar.newTab().setTabListener(tabListener);
	    actionBar.addTab(tab);
	    tab = actionBar.newTab().setTabListener(tabListener);
	    actionBar.addTab(tab);
	    tab = actionBar.newTab().setTabListener(tabListener);
	    actionBar.addTab(tab);
	    tab = actionBar.newTab().setTabListener(tabListener);
	    actionBar.addTab(tab);
	    tab = actionBar.newTab().setTabListener(tabListener);
	    actionBar.addTab(tab);
	    tab = actionBar.newTab().setTabListener(tabListener);
	    actionBar.addTab(tab);
	    tab = actionBar.newTab().setTabListener(tabListener);
	    actionBar.addTab(tab);
	    tab = actionBar.newTab().setTabListener(tabListener);
	    actionBar.addTab(tab);
	    tab = actionBar.newTab().setTabListener(tabListener);
	    actionBar.addTab(tab);
	    tab = actionBar.newTab().setTabListener(tabListener);
	    actionBar.addTab(tab);
	    
	    for(int i = 0; i<actionBar.getTabCount(); i++){
	        LayoutInflater inflater = LayoutInflater.from(this);
	        View customView = inflater.inflate(R.layout.tab_title, null);
	        TextView titleTV = (TextView) customView.findViewById(R.id.action_custom_title);
	        titleTV.setText(pageTitles[i]);
	        //Here you can also add any other styling you want.
	        titleTV.setTypeface(typeface,Typeface.BOLD);
	        if(LANGUAGE_CODE == CGlobalVariables.HINDI){
	        	 titleTV.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
	        }
	        actionBar.getTabAt(i).setCustomView(customView);
	    }*/
    }


    protected void showHindiSupportMessage() {
        Dialog dialog = null;
        ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(this, R.color.bg_card_view_color));
        AlertDialog.Builder builder = new AlertDialog.Builder(OutputMatchingMasterActivity.this);
        builder.setMessage(getResources().getString(R.string.this_device_does_not_support_hindi))
                .setTitle("")
                .setCancelable(false)
                .setPositiveButton(getResources().getString(R.string.ok),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                                dialog = null;
                            }
                        }
                );
        builder.setNegativeButton(getResources().getString(R.string.donot_show_again),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        CUtils.setDoNotShowHindiLanguageSupportPopup(OutputMatchingMasterActivity.this, false);
                        dialog = null;
                    }
                }
        );
        dialog = builder.create();
        Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(colorDrawable);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.show();
        TextView textView = (TextView) dialog.findViewById(android.R.id.message);
        textView.setTypeface(mediumTypeface);
        Button btnYes = (Button) dialog.findViewById(android.R.id.button1);
        Button btnNo = (Button) dialog.findViewById(android.R.id.button2);
        btnYes.setTypeface(mediumTypeface);
        btnNo.setTypeface(mediumTypeface);
    }


    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onStart()
     */
    @Override
    protected void onStart() {
        super.onStart();

        popupToRateApp();

        //EasyTracker.getInstance().activityStart(this);
		/*CUtils.googleAnalyticSend(null,CGlobalVariables.GOOGLE_ANALYTIC_SCREEN,
				CGlobalVariables.GOOGLE_ANALYTIC_KUNDLI_MATCH,"Match Result", 0L);*/
        CUtils.googleAnalyticSendWitPlayServie(this, CGlobalVariables.GOOGLE_ANALYTIC_SCREEN,
                CGlobalVariables.GOOGLE_ANALYTIC_KUNDLI_MATCH, "Match Result");
    }

    /* (non-Javadoc)
     * @see com.actionbarsherlock.app.SherlockFragmentActivity#onStop()
     */
    @Override
    protected void onStop() {
        super.onStop();
        //EasyTracker.getInstance().activityStop(this);

        try {

            if (myCountDownTimer != null) {
                myCountDownTimer.cancel();
            }

            if (AppRater.myCountDownTimer != null) {
                AppRater.myCountDownTimer.cancel();
            }


        } catch (Exception e) {
            //Log.i(e.getMessage().toString());
        }
    }

    /* (non-Javadoc)
     * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(CGlobalVariables.outPutMatchingMasterActSavedBundleKey, CGlobalMatching.getCGlobalMatching());
        super.onSaveInstanceState(outState);
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.FragmentActivity#onResume()
     */
    @Override
    protected void onResume() {
        super.onResume();
        setBottomNavigationText();
        isKundliChatWindowShowing = false;
        if ((!CUtils.isDhruvPlan(OutputMatchingMasterActivity.this) || CUtils.getUserPurchasedPlanFromPreference(this) != CGlobalVariables.KUNDLI_AI_PRIMIUM_PLAN_ID_11)&& !isPopupLoginShown) {
            //Log.e("SAN ", " openConfuseAboutKundli() ");
            openConfuseAboutKundli();
        }
        //  typeface = CUtils.getUserSelectedLanguageFontType(getApplicationContext(), CUtils.getLanguageCodeFromPreference(getApplicationContext()));
        //CUtils.applyTypeFaceOnActionBarTitle(OutputMatchingMasterActivity.this,typeface, getResources().getString(R.string.matching_result_title));
        tvToolBarTitle.setTypeface(mediumTypeface);
        tvToolBarTitle.setText(getResources().getString(R.string.matching_result_title));

        /*CUtils.showAdvertisement(this, (LinearLayout) findViewById(R.id.advLayout));*/

        updateMenus();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                CUtils.hideMyKeyboard(OutputMatchingMasterActivity.this);
            }
        }, 500);

        if (LANGUAGE_CODE == 1) {
            tv_ask_quest.animateTextInfinite(getString(R.string.ask_anything_to_kundli_hi,pageTitles[viewPager.getCurrentItem()]));
        } else {
            tv_ask_quest.animateTextInfinite(getString(R.string.ask_anything_to_kundli,pageTitles[viewPager.getCurrentItem()]));
        }


    }

    Handler handler;
    Runnable runnable;
    private void openConfuseAboutKundli() {
        try {
            if (handler != null && runnable != null) {
                handler.removeCallbacks(runnable);
            }
        } catch (Exception e) {
            //
        }
        handler = new Handler();
        runnable = () -> {
            try {
                if (!isKundliChatWindowShowing) {
                    LANGUAGE_CODE = CUtils.getLanguageCodeFromPreference(OutputMatchingMasterActivity.this);
                    CUtils.getRobotoFont(getApplicationContext(), LANGUAGE_CODE, CGlobalVariables.regular);
                    int planId = CUtils.getUserPurchasedPlanFromPreference(OutputMatchingMasterActivity.this);
                    if (planId != CGlobalVariables.PLATINUM_PLAN_ID && planId != CGlobalVariables.PLATINUM_PLAN_ID_9 && planId != CGlobalVariables.PLATINUM_PLAN_ID_10 && planId != CGlobalVariables.KUNDLI_AI_PRIMIUM_PLAN_ID_11) {
                        try {
                            if (AstrosageKundliApplication.popUpLogin != null) {
                                AstrosageKundliApplication.popUpLogin.dismiss();
                            }
                        } catch (Exception e) {
                            //
                        }
                        AstrosageKundliApplication.popUpLogin = new PopUpLogin
                                (com.ojassoft.astrosage.varta.utils.CGlobalVariables.HOROSCOPE_MATCHING,
                                        heading,
                                        subHeading,
                                        R.drawable.astrologer_icon_2);
                        AstrosageKundliApplication.popUpLogin.show(getSupportFragmentManager(), "PopUpFreeCall");
                        isPopupLoginShown = true;
                    }

                }
            } catch (Exception e) {
                //
            }
        };
        handler.postDelayed(runnable, 15000);
    }

    public void updateMenus() {

        try {
            drawerFragment.updateLayout(getDrawerListItem(), getDrawerListItemIcon(), getDrawerListItemIndex());
        } catch (Exception ex) {
            //Log.i(ex.getMessage().toString());
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        /*CUtils.removeAdvertisement(this, (LinearLayout) findViewById(R.id.advLayout));*/
    }

    private void setViewPagerListeners() {
        viewPager.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int arg0) {

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageSelected(int position) {

            }

        });
    }

    private void setViewPagerAdapter() {
        try {
            ModulePagerAdapter modulePagerAdapter = new ModulePagerAdapter(
                    getSupportFragmentManager());
            /*ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), OutputMatchingMasterActivity.this);*/
            viewPager.setAdapter(modulePagerAdapter);

            tabLayout.setupWithViewPager(viewPager);

            // Iterate over all tabs and set the custom view
            for (int i = 0; i < tabLayout.getTabCount(); i++) {
                TabLayout.Tab tab = tabLayout.getTabAt(i);
                tab.setCustomView(modulePagerAdapter.getTabView(i));
            }

            viewPager.addOnPageChangeListener(new OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    setCurrentView(position, false);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }


    public class ModulePagerAdapter extends FragmentStatePagerAdapter {

        private ArrayList<Fragment> mFragments;
        TypedArray imgs = null;
        int[] resId = new int[5];

        public ModulePagerAdapter(FragmentManager fm) {
            super(fm);

            AdData bottomAdDataVARNA = null;
            AdData bottomAdDataVASYA = null;
            AdData bottomAdDataTARA = null;
            AdData bottomAdDataYONI = null;
            AdData bottomAdDataMAITRI = null;
            AdData bottomAdDataGANA = null;
            AdData bottomAdDataBHAKOOT = null;
            AdData bottomAdDataNADI = null;
            ;

            try {
                String result = CUtils.getStringData(OutputMatchingMasterActivity.this, "CUSTOMADDS", "");
                if (result != null && !result.equals("")) {
                    ArrayList<AdData> adList = new Gson().fromJson(result, new TypeToken<ArrayList<AdData>>() {
                    }.getType());
                    bottomAdDataVARNA = CUtils.getSlotData(adList, "11");
                    bottomAdDataVASYA = CUtils.getSlotData(adList, "12");
                    bottomAdDataTARA = CUtils.getSlotData(adList, "13");
                    bottomAdDataYONI = CUtils.getSlotData(adList, "14");
                    bottomAdDataMAITRI = CUtils.getSlotData(adList, "15");
                    bottomAdDataGANA = CUtils.getSlotData(adList, "16");
                    bottomAdDataBHAKOOT = CUtils.getSlotData(adList, "17");
                    bottomAdDataNADI = CUtils.getSlotData(adList, "18");

                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            mFragments = new ArrayList<Fragment>();
            matchingOutputConclusionFragment = new MatchingOutputConclusionFragment();
            matchingOutputDetailResultNorthFragment = new MatchingOutputDetailResultNorthFragment();
            matchingOutputNorthInterpretationFragmentVARNA = MatchingOutputNorthInterpretationFragment.newInstance(VARNA, bottomAdDataVARNA);
            matchingOutputNorthInterpretationFragmentVASYA = MatchingOutputNorthInterpretationFragment.newInstance(VASYA, bottomAdDataVASYA);
            matchingOutputNorthInterpretationFragmentTARA = MatchingOutputNorthInterpretationFragment.newInstance(TARA, bottomAdDataTARA);
            matchingOutputNorthInterpretationFragmentYONI = MatchingOutputNorthInterpretationFragment.newInstance(YONI, bottomAdDataYONI);
            matchingOutputNorthInterpretationFragmentMAITRI = MatchingOutputNorthInterpretationFragment.newInstance(MAITRI, bottomAdDataMAITRI);
            matchingOutputNorthInterpretationFragmentGANA = MatchingOutputNorthInterpretationFragment.newInstance(GANA, bottomAdDataGANA);
            matchingOutputNorthInterpretationFragmentBHAKOOT = MatchingOutputNorthInterpretationFragment.newInstance(BHAKOOT, bottomAdDataBHAKOOT);
            matchingOutputNorthInterpretationFragmentNADI = MatchingOutputNorthInterpretationFragment.newInstance(NADI, bottomAdDataNADI);
            downloadMarriagePDF = DownloadMarriagePDF.newInstance();
            matchingOutputBoyAndGirlDetailFragment = MatchingOutputBoyAndGirlDetailFragment.newInstance();

            mFragments.add(matchingOutputConclusionFragment);
            mFragments.add(matchingOutputDetailResultNorthFragment);
            mFragments.add(matchingOutputNorthInterpretationFragmentVARNA);
            mFragments.add(matchingOutputNorthInterpretationFragmentVASYA);
            mFragments.add(matchingOutputNorthInterpretationFragmentTARA);
            mFragments.add(matchingOutputNorthInterpretationFragmentYONI);
            mFragments.add(matchingOutputNorthInterpretationFragmentMAITRI);
            mFragments.add(matchingOutputNorthInterpretationFragmentGANA);
            mFragments.add(matchingOutputNorthInterpretationFragmentBHAKOOT);
            mFragments.add(matchingOutputNorthInterpretationFragmentNADI);
            mFragments.add(downloadMarriagePDF);
            mFragments.add(matchingOutputBoyAndGirlDetailFragment);
            mFragments.add(AskQuestionsFragment.newInstance(com.ojassoft.astrosage.varta.utils.CGlobalVariables.HOROSCOPE_MATCHING));

        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        /* (non-Javadoc)
         * @see android.support.v4.view.PagerAdapter#getPageTitle(int)
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return pageTitles[position];
        }

        public View getTabView(int position) {

            View view = LayoutInflater.from(OutputMatchingMasterActivity.this).inflate(R.layout.lay_input_kundli_tab_title, null);
            TextView tv = (TextView) view.findViewById(R.id.tabtext);
            // View separater = (View) view.findViewById(R.id.view);
            tv.setTypeface(regularTypeface);
            tv.setText(pageTitles[position]);

            if (LANGUAGE_CODE == CGlobalVariables.ENGLISH) {
                tv.setText(pageTitles[position].toUpperCase());
            } else {
                tv.setText(pageTitles[position]);
            }

           /* if (position == 0) {
                separater.setVisibility(View.GONE);
            } else {
                separater.setVisibility(View.VISIBLE);
            }*/

            return view;

        }

    }

    @Override
    public void matchingOutputMainMenuSelected(int menuItemPosition) {
        // TODO Auto-generated method stub
        // getSlidingMenu().showContent();
        if (menuItemPosition == BaseInputActivity.TAG_MATCH_RESULT) {
            //viewPager.setCurrentItem(0);
            setCurrentView(0, false);
        } else if (menuItemPosition == BaseInputActivity.TAG_MATCH_RESULT_IN_DETAILS) {
            // viewPager.setCurrentItem(1);
            setCurrentView(1, false);
        } else if (menuItemPosition == BaseInputActivity.TAG_VAMA_WORK) {
            // viewPager.setCurrentItem(2);
            setCurrentView(2, false);
        } else if (menuItemPosition == BaseInputActivity.TAG_VASYA_DOMINANCE) {
            // viewPager.setCurrentItem(3);
            setCurrentView(3, false);
        } else if (menuItemPosition == BaseInputActivity.TAG_TARA_DESTINY) {
            // viewPager.setCurrentItem(4);
            setCurrentView(4, false);
        } else if (menuItemPosition == BaseInputActivity.TAG_YONI_MENTALITY) {
            // viewPager.setCurrentItem(5);
            setCurrentView(5, false);
        } else if (menuItemPosition == BaseInputActivity.TAG_MAITRI_COMPATIBILITY) {
            // viewPager.setCurrentItem(6);
            setCurrentView(6, false);
        } else if (menuItemPosition == BaseInputActivity.TAG_GANA) {
            // viewPager.setCurrentItem(7);
            setCurrentView(7, false);
        } else if (menuItemPosition == BaseInputActivity.TAG_BHAKOOT) {
            // viewPager.setCurrentItem(8);
            setCurrentView(8, false);
        } else if (menuItemPosition == BaseInputActivity.TAG_NADI) {
            // viewPager.setCurrentItem(9);
            setCurrentView(9, false);
        }

    }

    //ADDED BY DEEPAK ON 06-11-14
    private void goToViewBoyKundli() {

        BeanHoroPersonalInfo beanHoroPersonalInfoBoy = CGlobalMatching.getCGlobalMatching().getBoyPersonalDetail();
        calculateKundli(beanHoroPersonalInfoBoy);
    }

    private void goToViewGirlKundli() {
        BeanHoroPersonalInfo beanHoroPersonalInfoGirl = CGlobalMatching.getCGlobalMatching().getGirlPersonalDetail();
        calculateKundli(beanHoroPersonalInfoGirl);
    }

    //END ON 06-11-14
    @Override
    public void goToEmailScreen() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                /*CUtils.removeAdvertisement(OutputMatchingMasterActivity.this, (LinearLayout) findViewById(R.id.advLayout));*/
                emailScreenToUser();
            }
        }, 500);

    }

    private void emailScreenToUser() {
        View rootView = findViewById(android.R.id.content).getRootView();
        String savedImgUrl = CUtils.saveScreenImageInSD(this, rootView, this
                .getTitle().toString().trim(), false);
        /*CUtils.showAdvertisement(OutputMatchingMasterActivity.this, (LinearLayout) findViewById(R.id.advLayout));*/
        CUtils.sendMailOfUserScreen(this, savedImgUrl,
                CGlobalVariables.enuFileType.IMAGE);
    }

    private void goToEditMatchingInputDetail(boolean isEdit) {
		/*Intent intent = new Intent(getApplicationContext(),
				HomeMatchMakingInputScreen.class);
		intent.putExtra("IS_EDIT_INPUT_DETAIL", isEdit);
		startActivity(intent);
		this.finish();*/

        if (!isEdit) {
            Intent intent = new Intent(getApplicationContext(),
                    HomeMatchMakingInputScreen.class).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            intent.putExtra("IS_EDIT_INPUT_DETAIL", isEdit);
            startActivity(intent);
            this.finish();
        } else {
//			simply finish activity in case of edit kundli
            this.finish();
        }

    }

    /*private void openLanguageSelectDialog() {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        FragmentManager fm = getSupportFragmentManager();
        Fragment prev = fm.findFragmentByTag("MATCH_INPUT_LANGUAGE_OUT");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        ChooseLanguageFragmentDailog clfd = new ChooseLanguageFragmentDailog();
        clfd.show(fm, "MATCH_INPUT_LANGUAGE_OUT");
        ft.commit();

    }*/

    @Override
    public void onSelectedLanguage(int languageIndex) {
        if (LANGUAGE_CODE != languageIndex) {
            LANGUAGE_CODE = languageIndex;
			/*Intent intent = new Intent(getApplicationContext(), OutputMatchingMasterActivity.class);
			// intent.putExtra("LANGUAGE_CODE", LANGUAGE_CODE);
			startActivity(intent);*/
            startActivity(new Intent(getApplicationContext(), ActAppModule.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
            this.finish();
        }
    }

   /* @Override
    public void switchContent(int menuItemPosition) {
        switch (menuItemPosition) {
            case FILE:
//			its separator do nothing
                break;
            case HOME:
                CUtils.gotoHomeScreen(OutputMatchingMasterActivity.this);
                break;
            case NEW_MATCHING:
                //DISABLED BY BIJENDRA ON 13-02-15
	    	*//*if(CUtils.isInterstitialAdReady()){
				CUtils.displayInterstitialAd(new AdListener() {
					@Override
					public void onAdClosed() {
						super.onAdClosed();
						if(AstrosageKundliApplication.interstitialAd != null)
							AstrosageKundliApplication.interstitialAd.setAdListener(null);
						goToEditMatchingInputDetail(false);
					}
				});
			}else{
				goToEditMatchingInputDetail(false);
			}*//*
                goToEditMatchingInputDetail(false);
                break;
            case EDIT_INPUT_DETAIL:
                goToEditMatchingInputDetail(true);
                break;
            case DOWNLOAD_PDF:
                //Toast.makeText(this,"DOWNLOAD_PDF", Toast.LENGTH_LONG).show();
                //BOY ,GIRL,BOY RASI,GIRL RASI,LANGUAGE CODE
                CUtils.downloadKundliPdfForMatching(OutputMatchingMasterActivity.this, LANGUAGE_CODE);
                break;
            //ADDED BY DEEPAK ON 06-11-14
            case VIEW_BOY_KUNDLI:
                goToViewBoyKundli();
                break;
            case VIEW_GIRL_KUNDLI:
                goToViewGirlKundli();
                break;
            //END ON 06-11-14
            case EMAIL_SCREEN:
                //CUtils.googleAnalyticSend(null, CGlobalVariables.GOOGLE_ANALYTIC_LAUNCH, "Share_Screen", "Share_Screen", 0L);
                CUtils.googleAnalyticSendWitPlayServie(this, CGlobalVariables.GOOGLE_ANALYTIC_LAUNCH, "Share_Screen", "Share_Screen");
                goToEmailScreen();
                break;

            case SETTINGS:
//	    	its separator do nothing
                break;
            case CHANGE_LANGUAGE:
                if (LibCUtils.isSupportUnicodeHindi())
                    openLanguageSelectDialog();
                else
                    Toast.makeText(this, "Device does not support Unicode", Toast.LENGTH_LONG).show();
                break;
            case MISC:
//	    	its separator do nothing
                break;
            case FEEDBACK:
                CUtils.sendFeedBackViaApi(this, mediumTypeface, CUtils.getUserName(getApplicationContext()));
                break;
            case RATE_KUNDLI_APP:
                CUtils.rateAppication(this);
                break;
            case SHARE_APP:
                CUtils.shareToFriendMail(this);
                break;
            case OUR_OTHER_APPS:
                Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse(CGlobalVariables.OUR_OTHER_SOFTWARE_URL));
                startActivity(browserIntent);
                break;
            case CALL_US:
                CUtils.gotoCallUsScreen(this, LANGUAGE_CODE);
                break;
            case ASTRO_SHOP:
                CUtils.gotoAstroShopScreen(this);
                break;
            case ASK_OUR_ASTROLOGER:
                CUtils.gotoAskOurAstrologerScreen(this);
                break;
            case ABOUT_US:
                CUtils.gotoAboutUsScreen(this, LANGUAGE_CODE);
                break;
            case CLOUD_SIGN_OUT:
                logoutFromAstroSageCloud();
                break;
            case PRODUCT_PLAN_LIST:
                CUtils.gotopProductPlanList(OutputMatchingMasterActivity.this,
                        LANGUAGE_CODE,
                        HomeInputScreen.SUB_ACTIVITY_UPGRADE_PLAN_UPDTAE);
                break;
        }
       // getSlidingMenu().showContent();
    }*/

    @Override
    public void newKundli() {
        goToEditMatchingInputDetail(false);
    }

    @Override
    public void setEditKundli() {
        goToEditMatchingInputDetail(true);
    }

    @Override
    public void openKundli(int position) {
        if (position == BaseInputActivity.TAG_VIEW_BOYS_KUNDLI) {
            goToViewBoyKundli();
        } else if (position == BaseInputActivity.TAG_VIEW_GIRL_KUNDLI) {
            goToViewGirlKundli();
        }
    }

    @Override
    public void downloadPDF() {
        CUtils.googleAnalyticSendWitPlayServie(OutputMatchingMasterActivity.this,
                CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                GOOGLE_ANALYTIC_DOWNLOAD_PDF,
                GOOGLE_ANALYTIC_DOWNLOAD_PDF_MATCHING_SIDEMENU);

        com.ojassoft.astrosage.varta.utils.CUtils.addFacebookAndFirebaseEvent(CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK,GOOGLE_ANALYTIC_DOWNLOAD_PDF,GOOGLE_ANALYTIC_DOWNLOAD_PDF_MATCHING_SIDEMENU);
        sharePDF(false);
    }

    public void sharePDF(boolean isPdfShare) {
        this.isPdfShare = isPdfShare;

        if (!CUtils.isConnectedWithInternet(this)) {
            MyCustomToast mct = new MyCustomToast(this, this
                    .getLayoutInflater(), this, regularTypeface);
            mct.show(getResources().getString(R.string.no_internet));
            return;
        }
        progressBar = findViewById(R.id.progressbar);
        progressBar.setVisibility(View.VISIBLE);
        Intent intent = new Intent(this, DownloadService.class);
        intent.putExtra(KEY_PDF_SHARE, isPdfShare);
        intent.putExtra(KEY_URL, CUtils.getMatchingPdfUrl(this,LANGUAGE_CODE));
        intent.putExtra(KEY_RECEIVER, new DownloadReceiver(OutputMatchingMasterActivity.this, new Handler()));
        startService(intent);
    }



    private void requestExternalStoragePermission(int requestCode) {
        if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            CUtils.requestForExternalStorageNew(this, this, requestCode);
        } else {
            CUtils.requestForExternalStorage(this, this, requestCode);
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults != null && grantResults.length > 0) {
            if (requestCode == PERMISSION_EXTERNAL_STORAGE) {
                boolean isPermissionGranted = false;
                if (grantResults.length == 1) {
                    boolean READ_EXTERNAL_STORAGE = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (READ_EXTERNAL_STORAGE) {
                        isPermissionGranted = true;
                    }
                } else if (grantResults.length == 2) {
                    boolean READ_EXTERNAL_STORAGE = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean WRITE_EXTERNAL_STORAGE = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (READ_EXTERNAL_STORAGE && WRITE_EXTERNAL_STORAGE) {
                        isPermissionGranted = true;
                    }
                }

                if (isPermissionGranted) {
                    sharePDF(isPdfShare);
                } else {
                    Toast.makeText(this, getResources().getString(R.string.permission_allow), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    public void cancelProgressDialog() {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
        }
    }

    public void cancelDownloadProgressDialog() {
        if (downloadMarriagePDF != null) {
            downloadMarriagePDF.cancelProgressDialog();
        }
    }

    @Override
    public void logoutFromAstroSageCloud(boolean isShowToast) {
        // matchingOutputMenuFrag.updateLoginDetials(false, "", "");
        MyCustomToast mct = new MyCustomToast(OutputMatchingMasterActivity.this,
                OutputMatchingMasterActivity.this.getLayoutInflater(), OutputMatchingMasterActivity.this,
                mediumTypeface);
        mct.show(getResources().getString(R.string.sign_out_success));

    }

    /*public void goToLogin() {

        if (!CUtils.isUserLogedIn(getApplicationContext())) {
			*//*
     * Intent intent = new
     * Intent(HomeMatchMakingInputScreen.this,ActWizardScreens.class);
     * intent.putExtra("callerActivity",
     * CGlobalVariables.HOME_INPUT_SCREEN);
     * startActivityForResult(intent, SUB_ACTIVITY_USER_LOGIN);
     *//*
            Intent intent = new Intent(OutputMatchingMasterActivity.this,
                    ActLogin.class);
            intent.putExtra("callerActivity",
                    CGlobalVariables.HOME_INPUT_SCREEN);
            startActivityForResult(intent, HomeMatchMakingInputScreen.SUB_ACTIVITY_USER_LOGIN);
        } else {
           // matchingOutputMenuFrag.updateLoginDetials(false, "", "");
            MyCustomToast mct = new MyCustomToast(
                    OutputMatchingMasterActivity.this,
                    OutputMatchingMasterActivity.this.getLayoutInflater(),
                    OutputMatchingMasterActivity.this, mediumTypeface);
            mct.show(getResources().getString(R.string.sign_out_success));

        }

      //  getSlidingMenu().showContent();

    }*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {


            case HomeMatchMakingInputScreen.SUB_ACTIVITY_USER_LOGIN: {
                if (resultCode == RESULT_OK) {
                    Bundle b = data.getExtras();
                    String loginName = b.getString("LOGIN_NAME");
                    String loginPwd = b.getString("LOGIN_PWD");
                    setUserLoginDetails(loginName, loginPwd);

                }
            }
            break;
            case PERMISSION_EXTERNAL_STORAGE:
                /*if (SDK_INT >= Build.VERSION_CODES.R) {
                    if (Environment.isExternalStorageManager()) {
                        sharePDF(isPdfShare);
                    } else {
                        Toast.makeText(this, getResources().getString(R.string.permission_allow), Toast.LENGTH_SHORT).show();
                    }
                }*/
                sharePDF(isPdfShare);
                break;
            default:
                break;
            case HomeInputScreen.SUB_ACTIVITY_UPGRADE_PLAN_DIALOG: {

                // Added by shelendra on 01.06.2015
                if (resultCode == RESULT_OK) {
                    CalculateKundli kundli = new CalculateKundli(CGlobal.getCGlobalObject()
                            .getHoroPersonalInfoObject(), false, OutputMatchingMasterActivity.this, regularTypeface, SELECTED_MODULE, CGlobalVariables.HOME_INPUT_SCREEN, false, 0, 0, SELECTED_SUB_SCREEN);
                    kundli.new CCalculateOnlineDataSync(
                            CGlobal.getCGlobalObject()
                                    .getHoroPersonalInfoObject(),
                            CUtils.isConnectedWithInternet(getApplicationContext()))
                            .execute();
                }
            }
        }


    }

    private void setUserLoginDetails(String loginName, String loginPwd) {
        // matchingOutputMenuFrag.updateLoginDetials(true, loginName, loginPwd);
    }

    //ADDED BY DEEPAK ON 06-11-14
    public void calculateKundli(BeanHoroPersonalInfo beanHoroPersonalInfo) {
        CGlobal.getCGlobalObject().setHoroPersonalInfoObject(
                beanHoroPersonalInfo);
		/*CUtils.googleAnalyticSend(null, CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
				CGlobalVariables.GOOGLE_ANALYTIC_ACTION_SHOW_KUNDLI, null, 0l);*/
        CUtils.googleAnalyticSendWitPlayServie(this, CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                CGlobalVariables.GOOGLE_ANALYTIC_ACTION_SHOW_KUNDLI, null);

        // new CCalculateOnlineDataSync(beanHoroPersonalInfo, CUtils.isConnectedWithInternet(getApplicationContext())).execute();
        CalculateKundli kundli = new CalculateKundli(beanHoroPersonalInfo, false, OutputMatchingMasterActivity.this, regularTypeface, SELECTED_MODULE, CGlobalVariables.HOME_INPUT_SCREEN, false, 0, 0, SELECTED_SUB_SCREEN);
        kundli.calculate();
    }

    private class CCalculateOnlineDataSync extends AsyncTask<String, Long, Void> {
        CustomProgressDialog processDialog = null;
        BeanHoroPersonalInfo beanHoroPersonalInfo;
        boolean internetStatus, isSuccessCalculation = true;
        String exceptionMessage = "";

        public CCalculateOnlineDataSync(
                BeanHoroPersonalInfo beanHoroPersonalInfo,
                boolean internetStatus) {
            this.beanHoroPersonalInfo = beanHoroPersonalInfo;
            this.internetStatus = internetStatus;
        }

        @Override
        protected Void doInBackground(String... arg0) {
            // calculate chart code
            ControllerManager _controllerManager = new ControllerManager();
            try {
                _controllerManager.calculateKundliData(beanHoroPersonalInfo,
                        true, internetStatus);
            } catch (UICOnlineChartOperationException e) {
                e.printStackTrace();
                isSuccessCalculation = false;
                exceptionMessage = e.getMessage();

            } catch (NoInternetException nIntrntExc) {
                isSuccessCalculation = false;
                nIntrntExc.printStackTrace();
            }
            return null;
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            try {
                if (processDialog != null & processDialog.isShowing())
                    processDialog.dismiss();
            } catch (Exception e) {

            }
            if (isSuccessCalculation) {
                Intent intent = new Intent(OutputMatchingMasterActivity.this,
                        OutputMasterActivity.class);
                intent.putExtra(CGlobalVariables.LANGUAGE_CODE, LANGUAGE_CODE);
                SharedPreferences sharedPreferences = getSharedPreferences(
                        CGlobalVariables.APP_PREFS_NAME, Context.MODE_PRIVATE);
                intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY,
                        CGlobalVariables.MODULE_BASIC);
                intent.putExtra(CGlobalVariables.SUB_MODULE_TYPE_KEY,
                        sharedPreferences.getInt(
                                CGlobalVariables.APP_PREFS_SubModule, 0));
                intent.putExtra(CGlobalVariables.IS_NORTH_INDIAN,
                        sharedPreferences.getInt(
                                CGlobalVariables.APP_PREFS_ChartStyle,
                                CGlobalVariables.CHART_NORTH_STYLE));
                startActivity(intent);
            } else {
                if ((exceptionMessage != null)
                        && (exceptionMessage.length() > 0)) {
                    MyCustomToast mct = new MyCustomToast(OutputMatchingMasterActivity.this,
                            OutputMatchingMasterActivity.this.getLayoutInflater(),
                            OutputMatchingMasterActivity.this, Typeface.DEFAULT);
                    mct.show(exceptionMessage);
                } else if ((exceptionMessage == null)) {
                    MyCustomToast mct1 = new MyCustomToast(
                            OutputMatchingMasterActivity.this,
                            OutputMatchingMasterActivity.this.getLayoutInflater(),
                            OutputMatchingMasterActivity.this, Typeface.DEFAULT);
                    mct1.show("Error on server");
                } else {
                    MyCustomToast mct2 = new MyCustomToast(
                            OutputMatchingMasterActivity.this,
                            OutputMatchingMasterActivity.this.getLayoutInflater(),
                            OutputMatchingMasterActivity.this, mediumTypeface);
                    mct2.show(getResources().getString(R.string.no_internet));
                }
            }
        }

        /*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPreExecute()
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            exceptionMessage = "";
			/*processDialog = ProgressDialog.show(OutputMatchingMasterActivity.this, null,
					getResources().getString(R.string.msg_please_wait), true,
					false);
			TextView tvMsg = (TextView) processDialog
					.findViewById(android.R.id.message);
			tvMsg.setTypeface(typeface);
			tvMsg.setTextSize(20);*/

            processDialog = new CustomProgressDialog(OutputMatchingMasterActivity.this, regularTypeface);
            processDialog.show();

        }
    }
    //END ON 06-11-14

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
    }

    List<String> getDrawerListItem() {

        try {
            String[] menuItems2 = getResources().getStringArray(R.array.main_menu_output_matching);
            return Arrays.asList(menuItems2);
        } catch (Exception ex) {
            //Log.i(ex.getMessage().toString());
            return null;
        }

    }

    List<Drawable> getDrawerListItemIcon() {

        try {
            TypedArray itemsIcon2 = getResources().obtainTypedArray(R.array.main_menu_output_matching_icon);
            return CUtils.convertTypedArrayToArrayList(OutputMatchingMasterActivity.this, itemsIcon2, main_menu_output_matching_index);
        } catch (Exception ex) {
            //Log.i(ex.getMessage().toString());
            return null;
        }

    }

    List<Integer> getDrawerListItemIndex() {
        try {
            //return CUtils.getDrawerListItemIndex(OutputMasterActivity.this, app_home_menu_item_list_index, module_list_index);
            return Arrays.asList(main_menu_output_matching_index);
        } catch (Exception ex) {
            //Log.i(ex.getMessage().toString());
            return null;
        }
    }

    @Override
    public void onBackPressed() {

        try {
            if (drawerFragment.isDrawerOpen) {
                drawerFragment.closeDrawer();
            } else {
                super.onBackPressed();
            }
        } catch (Exception ex) {
            //Log.i(ex.getMessage().toString());
            super.onBackPressed();
        }
    }

    public void setDataAfterPurchasePlan(boolean purchaseSilverPlan, String screenId) {

        if (purchaseSilverPlan) {

            //08-feb-2016 work for which screen will be open
            //String ScreenId = data.getStringExtra("ScreenId");
            CUtils.gotoProductPlanListUpdated(
                    OutputMatchingMasterActivity.this,
                    LANGUAGE_CODE,
                    HomeInputScreen.SUB_ACTIVITY_UPGRADE_PLAN_DIALOG_PURCHSE, screenId,"output_matching_master_activity");

        }// END ON 22-12-2014
        else {

            CalculateKundli kundli = new CalculateKundli(CGlobal.getCGlobalObject()
                    .getHoroPersonalInfoObject(), false, OutputMatchingMasterActivity.this, regularTypeface, SELECTED_MODULE, CGlobalVariables.HOME_INPUT_SCREEN, false, 0, 0, SELECTED_SUB_SCREEN);
            kundli.new CCalculateOnlineDataSync(
                    CGlobal.getCGlobalObject()
                            .getHoroPersonalInfoObject(),
                    CUtils.isConnectedWithInternet(getApplicationContext()))
                    .execute();
        }
    }

    private void setCurrentView(int index, boolean smoothScroll) {
        viewPager.setCurrentItem(index, smoothScroll);
        if (tabLayout != null) {
            setAlpha(index, tabLayout);
        }
        if (LANGUAGE_CODE == 1) {
            tv_ask_quest.animateTextInfinite(getString(R.string.ask_anything_to_kundli_hi,pageTitles[index]));
        } else {
            tv_ask_quest.animateTextInfinite(getString(R.string.ask_anything_to_kundli,pageTitles[index]));
        }
    }

    private void setAlpha(int position, TabLayout tabLayout) {
        try {
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
                for (int i = 0; i < tabLayout.getTabCount(); i++) {
                    View view = tabLayout.getTabAt(i).getCustomView();
                    TextView textView = (TextView) view.findViewById(R.id.tabtext);
                    if (position == i) {
                        textView.setAlpha(1F);
                    } else {
                        textView.setAlpha(0.5F);
                    }
                }
            }
        } catch (Exception ex) {
            //android.util.//Log.i("Exception",ex.getMessage());
        }
    }

    /*BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.actapp_nav_home:
                            Intent intent = new Intent(OutputMatchingMasterActivity.this, ActAppModule.class);
                            startActivity(intent);
                            return true;
                        case R.id.actapp_nav_call:
                            com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_OUTPUT_MATCHING_MASTER_BOTTOM_CALL, FIREBASE_EVENT_ITEM_CLICK, "");
                            createSession(OutputMatchingMasterActivity.this, OUTPUT_MATCHING_MASTER_BOTTOM_BAR_CALL_PARTNER_ID);
                            switchToConsultTab(FILTER_TYPE_CALL, OutputMatchingMasterActivity.this);
                            return true;
                        case R.id.actapp_nav_live:
                            fabActions();
                            return true;
                        case R.id.actapp_nav_chat:
                            com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_OUTPUT_MATCHING_MASTER_BOTTOM_CHAT, FIREBASE_EVENT_ITEM_CLICK, "");
                            createSession(OutputMatchingMasterActivity.this, OUTPUT_MATCHING_MASTER_BOTTOM_BAR_CHAT_PARTNER_ID);
                            switchToConsultTab(FILTER_TYPE_CHAT, OutputMatchingMasterActivity.this);
                            return true;
                        case R.id.actapp_nav_account:
                            com.ojassoft.astrosage.varta.utils.CUtils.openAccountScreen(OutputMatchingMasterActivity.this);
                            return true;
                    }
                    return false;
                }
            };*/

    private void fabActions(){
        try {
            //boolean liveStreamingEnabledForAstrosage = com.ojassoft.astrosage.varta.utils.CUtils.getBooleanData(this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.liveStreamingEnabledForAstrosage, false);
            boolean liveStreamingEnabledForAstrosage = true;
            if(!liveStreamingEnabledForAstrosage){ //fetch data according to tagmanag
                com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_OUTPUT_MATCHING_MASTER_BOTTOM_KUNDLI, FIREBASE_EVENT_ITEM_CLICK, "");
                createSession(OutputMatchingMasterActivity.this, OUTPUT_MATCHING_MASTER_BOTTOM_BAR_KUNDLI_PARTNER_ID);
                openKundliActivity(this);
            }
            else {
                com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_OUTPUT_MATCHING_MASTER_BOTTOM_LIVE, FIREBASE_EVENT_ITEM_CLICK, "");
                createSession(OutputMatchingMasterActivity.this, OUTPUT_MATCHING_MASTER_BOTTOM_BAR_LIVE_PARTNER_ID);
                gotoAllLiveActivityFromLiveIcon(OutputMatchingMasterActivity.this);
            }
        }catch (Exception e){
            //
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
                FontUtils.changeFont(OutputMatchingMasterActivity.this, ((TextView) v), com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
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
        CUtils.handleFabOnActivities(this,fabOutputMaster,navLive);

        if (com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(OutputMatchingMasterActivity.this)) {
            navHisTxt.setText(getResources().getString(R.string.history));
            navHisImg.setImageResource(R.drawable.nav_more_icons);
        } else {
            navHisTxt.setText(getResources().getString(R.string.sign_up));
            navHisImg.setImageResource(R.drawable.nav_profile_icons);
        }
        //navView.getMenu().setGroupCheckable(0,false,true);
        //setting Click listener
        navView.findViewById(R.id.bottom_nav_home).setOnClickListener(navbarItemSelectedListener);
        navView.findViewById(R.id.bottom_nav_call).setOnClickListener(navbarItemSelectedListener);
        navView.findViewById(R.id.bottom_nav_live).setOnClickListener(navbarItemSelectedListener);
        navView.findViewById(R.id.bottom_nav_chat).setOnClickListener(navbarItemSelectedListener);
        navView.findViewById(R.id.bottom_nav_history).setOnClickListener(navbarItemSelectedListener);
    }

    @Override
    public void clickCall() {
        com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_OUTPUT_MATCHING_MASTER_BOTTOM_CALL, FIREBASE_EVENT_ITEM_CLICK, "");
        createSession(OutputMatchingMasterActivity.this, OUTPUT_MATCHING_MASTER_BOTTOM_BAR_CALL_PARTNER_ID);
        super.clickCall();
    }

    @Override
    public void clickChat() {
        com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_OUTPUT_MATCHING_MASTER_BOTTOM_CHAT, FIREBASE_EVENT_ITEM_CLICK, "");
        createSession(OutputMatchingMasterActivity.this, OUTPUT_MATCHING_MASTER_BOTTOM_BAR_CHAT_PARTNER_ID);
        super.clickChat();
    }

    @Override
    public void clickLive() {
        fabActions();
    }

    private void getSuggestQuestionForMatchingScreens() {
        try {
            // one day caching
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            Date curentDate = new Date();
            String todayDate = formatter.format(curentDate);
            String getSuggestedQuestionDate = CUtils.getStringData(this,GET_SUGGESTED_QUESTION_DATE,"");
            int lang = LANGUAGE_CODE;
            String suggestedQuestionLangKey = SUGGESTED_QUESTION_KEY+"_"+lang;


            if(getSuggestedQuestionDate.equalsIgnoreCase(todayDate)){
                String suggestedQuestion = CUtils.getStringData(this,suggestedQuestionLangKey,"");
                if(!TextUtils.isEmpty(suggestedQuestion)){ //check in local
                    parseSuggestedQuestion(suggestedQuestion, suggestedQuestionLangKey);
                    return;
                }
            }

            Call<ResponseBody> call = RetrofitClient.getAIInstance().create(ApiList.class).getSuggestedQuestionModule(getQuestionListParams(lang));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        if (response.isSuccessful() && response.body() != null) {
                            String responseString = response.body().string();
                            //Log.e("TestQuestion", "onResponse: " + responseString);
                            parseSuggestedQuestion(responseString, suggestedQuestionLangKey);
                            CUtils.saveStringData(OutputMatchingMasterActivity.this,GET_SUGGESTED_QUESTION_DATE,todayDate);
                        }

                    } catch (Exception e) {
                        //
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable throwable) {

                }
            });
        } catch (Exception e) {
            //
        }
    }

    private void parseSuggestedQuestion(String responseString, String suggestedQuestionLangKey){
        try {
            if (questionMap == null) {
                questionMap = new HashMap<>();
            }
            JSONObject jsonObject = new JSONObject(responseString);
            CUtils.saveStringData(this,suggestedQuestionLangKey,jsonObject.toString());
            Iterator<String> keys = jsonObject.keys();

            Gson gson = new Gson();
            // Iterate over the keys
            while (keys.hasNext()) {
                String key = keys.next();
                JSONArray jsonArray = jsonObject.getJSONArray(key);
                Type listType = new TypeToken<ArrayList<String>>() {
                }.getType();
                ArrayList<String> questionList = (gson.fromJson(jsonArray.toString(), listType));

                questionMap.put(key, questionList);
            }
        } catch (Exception e){
            //
        }
    }

    private Map<String, String> getQuestionListParams(int lang) {
        Map<String, String> headers = new HashMap<>();

        headers.put("lang",String.valueOf(lang));
        headers.put("moduleid", String.valueOf(CGlobalVariables.AI_MATCHING_MODULE));
        headers.put(APP_KEY, getApplicationSignatureHashCode(this));
        headers.put("methodname", "suggestedquesakmodules");

        return headers;
    }

    private ArrayList<String> getSuggestedQuestionsForScreenId(int screenId) {
        try {
            return new ArrayList<>(questionMap.get(String.valueOf(screenId)));
        } catch (Exception e) {
            return null;
        }
    }

    private String getRomanScreenName(int position) {
        Resources resources = getLocaleResources(this, "en");
        String[] titles = resources.getStringArray(R.array.output_matching_tab_list);
        return titles[position];
    }

}
