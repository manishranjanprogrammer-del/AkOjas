package com.ojassoft.astrosage.ui.act;

import static com.ojassoft.astrosage.misc.SyncKundliService.ACTION_SYNC_SERVICE;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.BASE_INPUT_SCREEN;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.FILTER_TYPE_CHAT;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.IS_FROM_SCREEN;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.IS_GOOGLE_FACEBOOK_VISIBLE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingClientStateListener;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.Purchase;
import com.google.android.material.snackbar.Snackbar;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.appcompat.widget.ListPopupWindow;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.beans.BeanUserMapping;
import com.ojassoft.astrosage.beans.CScreenHistoryItemCollection;
import com.ojassoft.astrosage.beans.SerializeAndDeserializeBeans;
import com.ojassoft.astrosage.billing.BillingEventHandler;
import com.ojassoft.astrosage.billing.PurchaseBillingEventHandler;
import com.ojassoft.astrosage.jinterface.IHomeNavigationDrawerFragment;
import com.ojassoft.astrosage.jinterface.IOpenInputYearCalendar;
import com.ojassoft.astrosage.jinterface.IYearInputBoxPopupFragmentDialog;
import com.ojassoft.astrosage.misc.CalculateKundli;
import com.ojassoft.astrosage.misc.SyncKundliService;
import com.ojassoft.astrosage.notification.ActShowOjasSoftArticles;
import com.ojassoft.astrosage.ui.SuperBaseActivity;
import com.ojassoft.astrosage.ui.customcontrols.ActBookmarkAndHistory;
import com.ojassoft.astrosage.ui.fragments.AppRateFrag;
import com.ojassoft.astrosage.ui.fragments.CallUsDialogFragment;
import com.ojassoft.astrosage.ui.fragments.ChooseLanguageFragmentDailog;
import com.ojassoft.astrosage.ui.fragments.CustomerSupportDialog;
import com.ojassoft.astrosage.ui.fragments.FragAskForAstrologer;
import com.ojassoft.astrosage.ui.fragments.NotificationSettingFragment;
import com.ojassoft.astrosage.ui.fragments.SignOutDialogFragment;
import com.ojassoft.astrosage.ui.fragments.YearInputBoxPopupFragmentDialog;
import com.ojassoft.astrosage.utils.CGlobal;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CScreenConstants;
import com.ojassoft.astrosage.utils.CUtils;


import com.ojassoft.astrosage.varta.dialog.AstroBusyAlertDialog;
import com.ojassoft.astrosage.varta.ui.activity.LoginSignUpActivity;
//import com.ojassoft.astrosage.varta.ui.activity.ManageSubscriptionActivity;
import com.ojassoft.astrosage.varta.ui.activity.MyAccountActivity;


import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


public class BaseInputActivity extends SuperBaseActivity implements IHomeNavigationDrawerFragment, IYearInputBoxPopupFragmentDialog, IOpenInputYearCalendar, BillingEventHandler {

    private MyBroadcastReceiver myBroadcastReceiver;
    /**
     * Menues Items
     */
    final int MODULE_BASIC = CGlobalVariables.MODULE_BASIC;
    final int MODULE_DASA = CGlobalVariables.MODULE_DASA;
    final int MODULE_PREDICTION = CGlobalVariables.MODULE_PREDICTION;
    final int MODULE_KP = CGlobalVariables.MODULE_KP;
    final int MODULE_SHODASHVARGA = CGlobalVariables.MODULE_SHODASHVARGA;
    final int MODULE_LALKITAB = CGlobalVariables.MODULE_LALKITAB;
    final int MODULE_VARSHAPHAL = CGlobalVariables.MODULE_VARSHAPHAL;
    final int MODULE_MISC = CGlobalVariables.MODULE_MISC;

    final int MODULE_NUMEROLOGY = CGlobalVariables.MODULE_NUMEROLOGY;

    public static final int TAG_SIGN_IN = 101;
    public static final int TAG_SIGN_UP = 102;
    public static final int TAG_APP_SUBSCRIPTION = 183;
    final int TAG_SET_PREFERENCES = 103;
    final int TAG_NOTIFICATION_SETTING = 104;
    final int TAG_CHANGE_LANGUAGE = 105;
    final int TAG_UPGRADE_PRODUCT_PLAN_LIST = 106;
    public static final int TAG_REMOVE_ADS = 107;
    //final int TAG_SHARE_APP = 108;
    final int TAG_FEEDBACK = 109;
    //final int TAG_CALL_US = 110;
    final int TAG_RATE_APP = 111;
    final int TAG_SHARE_APP = 200;
    final int TAG_ABOUT_US = 112;
    final int TAG_Share_CHART = 1101;
    final int TAG_MY_KUNDLI = 113;
    public static final int TAG_CLOUD_SIGN_OUT = 114;
    final int TAG_HOME = 115;
    final int TAG_BOOKMARK = 116;
    final int TAG_NEW_KUNDLI = 117;
    final int TAG_OPEN_KUNDLI = 118;
    final int TAG_ASTROSAGE_ARTICLES = 119;

    final int TAG_EDIT_KUNDLI = 120;
    final int TAG_DOWNLOAD_PDF = 121;
    final int TAG_SHARE_SCREEN = 122;
    final int TAG_CHART_STYLE = 123;
    final int TAG_CHANGE_AYANAMSA = 124;
    final int TAG_HELP = 125;
    final int TAG_OUR_OTHER_APPS = 126;
    final int TAG_ASTRO_SHOP = 127;
    final int TAG_ASK_OUR_ASTROLOGER = 128;
    final int TAG_BY_NAME = 129;
    final int TAG_BY_BIRTH = 130;
    final int TAG_HOROSCOPE_NOTIFICATION = 131;


    public static final int TAG_TODAYS_HOROSCOPE = 132;
    public static final int TAG_HOROSCOPE_PANCHANG = 180;
    public static final int TAG_WEEKLY_HOROSCOPE = 133;
    public static final int TAG_WEEKLY_LOVE_HOROSCOPE = 134;
    public static final int TAG_MONTHLY_HOROSCOPE = 135;
    public static final int TAG_YEARLY_HOROSCOPE = 136;

    final int TAG_SHARE_PANCHANG = 137;

    public static final int TAG_VIEW_BOYS_KUNDLI = 138;
    public static final int TAG_VIEW_GIRL_KUNDLI = 139;

    public static final int TAG_MATCH_RESULT = 140;
    public static final int TAG_MATCH_RESULT_IN_DETAILS = 141;
    public static final int TAG_VAMA_WORK = 142;
    public static final int TAG_VASYA_DOMINANCE = 143;
    public static final int TAG_TARA_DESTINY = 144;
    public static final int TAG_YONI_MENTALITY = 145;
    public static final int TAG_MAITRI_COMPATIBILITY = 146;
    public static final int TAG_GANA = 147;
    public static final int TAG_BHAKOOT = 148;
    public static final int TAG_NADI = 149;
    public static final int TAG_SET_PASSWORD = 150;
    public static final int TAG_ASTROLOGER_DIRECTORY = 151;
    public static final int TAG_ASTROLOGER = 152;
    public static final int TAG_ASK_A_QUES_CHAT_HISTORY = 153;
    public static final int TAG_CUSTOMER_SUPPORT = 154;
    public static final int TAG_TERMS_AND_PRIVACY = 155;
    final int TAG_NOTES = 156;
    public static final int TAG_SYNC_CHART = 157;
    final int TAG_NEW_KUNDLI_DHRUV = 158;
    final int TAG_NEW_KUNDLI_DHRUV_MATCH_MAKING = 159;
    final int TAG_NEW_KUNDLI_DHRUV_PRINT_KUNDLI = 160;
    final int TAG_NEW_KUNDLI_DHRUV_DASHA = 161;
    final int TAG_NEW_KUNDLI_DHRUV_PREDICTION = 162;
    final int TAG_NEW_KUNDLI_DHRUV_NUMEROLOGY = 163;
    final int TAG_NEW_KUNDLI_DHRUV_KP = 164;
    final int TAG_NEW_KUNDLI_DHRUV_SHODASHVARGA = 165;
    final int TAG_NEW_KUNDLI_DHRUV_LAL_KITAB = 166;
    final int TAG_NEW_KUNDLI_DHRUV_VARSHFAL = 167;
    final int TAG_JOIN_VARTA = 168;
    public static final int TAG_MY_ACCOUNT = 169;
    public static final int TAG_CALL_US = 175;

    public static final int TAG_REFER_AND_EARN = 170;
    public static final int TAG_MY_KUNDLI_DOWNLOADS = 192;


    public static final int PERMISSION_EXTERNAL_STORAGE = 2501;
    public static final int PERMISSION_LOCATION = 2502;
    public static final int PERMISSION_CONTACTS = 2503;
    public static final int PERMISSION_MANAGE_STORAGE = 2504;
    public static final int PERMISSION_STORAGE_FOR_SHARE_SCREEN = 2505;
    public static final int PERMISSION_STORAGE_FOR_SHARE_PDF = 2506;

    //index for panchang start


    public static final int TAG_MONTHLY_PANCHANG = 171;
    public static final int TAG_HINDU_CALENDAR = 172;
    public static final int TAG_YEARLY_VART = 173;
    public static final int TAG_FESTIVAL = 174;
    public static final int TAG_OTHER_CALENDARS = 179;


    //index for panchang end

    //public static final int TAG_APP_INVITE = 180;

    //Index array
    final Integer[] app_home_menu_item_list_index = {
            TAG_ASTROLOGER_DIRECTORY,
            TAG_SYNC_CHART,
            TAG_SET_PREFERENCES,
            TAG_NOTIFICATION_SETTING,
            TAG_CHANGE_LANGUAGE,
            /*TAG_APP_INVITE,*/
            TAG_UPGRADE_PRODUCT_PLAN_LIST,
            TAG_REMOVE_ADS,
            // TAG_SHARE_APP,
            TAG_FEEDBACK,
            //TAG_CALL_US,
            TAG_RATE_APP,
            //    TAG_SHARE_APP,
            TAG_ABOUT_US,
            TAG_JOIN_VARTA,
            TAG_MY_KUNDLI,
            TAG_MY_KUNDLI_DOWNLOADS,
            TAG_SET_PASSWORD,
            TAG_REFER_AND_EARN,
            TAG_MY_ACCOUNT,
            TAG_CALL_US,
            TAG_CLOUD_SIGN_OUT,
            TAG_APP_SUBSCRIPTION
    };


    final Integer[] module_list_index_for_panchang = {TAG_HOME, CGlobalVariables.MODULE_ASTROSAGE_PANCHANG, TAG_MONTHLY_PANCHANG, TAG_HINDU_CALENDAR, TAG_YEARLY_VART, TAG_FESTIVAL, CGlobalVariables.MODULE_ASTROSAGE_HORA,
            CGlobalVariables.MODULE_ASTROSAGE_CHOGADIA, CGlobalVariables.MODULE_DO_GHATI_MUHURT, CGlobalVariables.MODULE_RAHUKAAL, CGlobalVariables.MODULE_PANCHAK, CGlobalVariables.MODULE_BHADRA, CGlobalVariables.MODULE_MUHURAT, CGlobalVariables.MODULE_LAGNA, TAG_OTHER_CALENDARS};


    final Integer[] module_list_index = {TAG_HOME, TAG_BOOKMARK, MODULE_BASIC, MODULE_DASA, MODULE_PREDICTION, MODULE_KP,
            MODULE_SHODASHVARGA, MODULE_LALKITAB, MODULE_VARSHAPHAL, MODULE_MISC, MODULE_NUMEROLOGY};

    final Integer[] kundli_home_menu_item_list_index = {TAG_NEW_KUNDLI, TAG_OPEN_KUNDLI, TAG_ASTROSAGE_ARTICLES, TAG_UPGRADE_PRODUCT_PLAN_LIST,
            //TAG_SHARE_APP,
            TAG_FEEDBACK, TAG_SET_PREFERENCES, TAG_NOTIFICATION_SETTING, TAG_CHANGE_LANGUAGE,
            // TAG_CALL_US,
            TAG_RATE_APP, TAG_ABOUT_US, TAG_JOIN_VARTA, TAG_MY_ACCOUNT,TAG_MY_KUNDLI_DOWNLOADS, TAG_CLOUD_SIGN_OUT
    };

    final Integer[] kundli_output_menu_item_list_index = {TAG_HOME, TAG_NOTES, TAG_NEW_KUNDLI, TAG_OPEN_KUNDLI, TAG_EDIT_KUNDLI, TAG_DOWNLOAD_PDF, TAG_UPGRADE_PRODUCT_PLAN_LIST,
            TAG_SHARE_SCREEN, TAG_CHART_STYLE, TAG_CHANGE_LANGUAGE, TAG_CHANGE_AYANAMSA, TAG_HELP, TAG_FEEDBACK,
            TAG_RATE_APP,
            //TAG_SHARE_APP,
            TAG_OUR_OTHER_APPS,
            //TAG_CALL_US,
            TAG_ASTRO_SHOP, TAG_ASK_OUR_ASTROLOGER, TAG_Share_CHART, TAG_ABOUT_US, TAG_JOIN_VARTA, TAG_CLOUD_SIGN_OUT};

    public final Integer[] horoscope_menu_item_list_index = {TAG_BY_NAME, TAG_BY_BIRTH, TAG_HOROSCOPE_NOTIFICATION,
            TAG_FEEDBACK, TAG_RATE_APP,
            //TAG_SHARE_APP,
            TAG_OUR_OTHER_APPS,
            //TAG_CALL_US,
            TAG_ASTRO_SHOP, TAG_ASK_OUR_ASTROLOGER, TAG_ABOUT_US, TAG_JOIN_VARTA, TAG_CLOUD_SIGN_OUT};


    public final Integer[] detailed_horoscope_menu_titles_list_index = {TAG_HOME, TAG_TODAYS_HOROSCOPE, TAG_HOROSCOPE_PANCHANG, TAG_WEEKLY_HOROSCOPE, TAG_WEEKLY_LOVE_HOROSCOPE, TAG_MONTHLY_HOROSCOPE, TAG_YEARLY_HOROSCOPE,
            TAG_HOROSCOPE_NOTIFICATION, TAG_FEEDBACK, TAG_RATE_APP,
            // TAG_SHARE_APP,
            TAG_OUR_OTHER_APPS,
            // TAG_CALL_US,
            TAG_ASTRO_SHOP, TAG_ASK_OUR_ASTROLOGER, TAG_ABOUT_US, TAG_JOIN_VARTA, TAG_CLOUD_SIGN_OUT};


    public final Integer[] panchang_menu_item_list_index = {TAG_SHARE_PANCHANG, TAG_FEEDBACK, TAG_RATE_APP,
            //TAG_SHARE_APP,
            TAG_OUR_OTHER_APPS,
            //TAG_CALL_US,
            TAG_ASTRO_SHOP, TAG_ASK_OUR_ASTROLOGER, TAG_ABOUT_US, TAG_JOIN_VARTA, TAG_CLOUD_SIGN_OUT};

    public final Integer[] panchang_menu_item_list_index_without_share = {TAG_FEEDBACK, TAG_RATE_APP,
            //TAG_SHARE_APP,
            TAG_OUR_OTHER_APPS,
            //TAG_CALL_US,
            TAG_ASTRO_SHOP, TAG_ASK_OUR_ASTROLOGER, TAG_ABOUT_US, TAG_JOIN_VARTA, TAG_CLOUD_SIGN_OUT};

    public final Integer[] matching_home_menu_item_list_index = {TAG_NEW_KUNDLI, TAG_OPEN_KUNDLI, TAG_ASTROSAGE_ARTICLES, TAG_UPGRADE_PRODUCT_PLAN_LIST,
            //TAG_SHARE_APP,
            TAG_FEEDBACK, TAG_NOTIFICATION_SETTING, TAG_CHANGE_LANGUAGE,
            // TAG_CALL_US,
            TAG_RATE_APP, TAG_ABOUT_US, TAG_JOIN_VARTA
            , TAG_CLOUD_SIGN_OUT
    };

    public final Integer[] matching_output_menu_item_list_index = {TAG_HOME, TAG_NEW_KUNDLI, TAG_EDIT_KUNDLI, TAG_VIEW_BOYS_KUNDLI, TAG_VIEW_GIRL_KUNDLI, TAG_DOWNLOAD_PDF,
            TAG_SHARE_SCREEN, TAG_CHANGE_LANGUAGE, TAG_FEEDBACK, TAG_RATE_APP, TAG_UPGRADE_PRODUCT_PLAN_LIST,
            // TAG_SHARE_APP,
            TAG_OUR_OTHER_APPS,
            // TAG_CALL_US,
            TAG_ASTRO_SHOP, TAG_ASK_OUR_ASTROLOGER, TAG_ABOUT_US, TAG_JOIN_VARTA, TAG_CLOUD_SIGN_OUT};

    public final Integer[] main_menu_output_matching_index = {TAG_HOME, TAG_MATCH_RESULT, TAG_MATCH_RESULT_IN_DETAILS, TAG_VAMA_WORK, TAG_VASYA_DOMINANCE, TAG_TARA_DESTINY,
            TAG_YONI_MENTALITY, TAG_MAITRI_COMPATIBILITY, TAG_GANA, TAG_BHAKOOT, TAG_NADI};

    final Integer[] ask_a_question_list_index = {TAG_ASTROLOGER, TAG_ASK_A_QUES_CHAT_HISTORY, TAG_CUSTOMER_SUPPORT, TAG_TERMS_AND_PRIVACY};


    public final Integer[] dhruv_menu_index = {TAG_HOME, TAG_NEW_KUNDLI_DHRUV, TAG_NEW_KUNDLI_DHRUV_MATCH_MAKING, TAG_NEW_KUNDLI_DHRUV_PRINT_KUNDLI,
            TAG_NEW_KUNDLI_DHRUV_DASHA, TAG_NEW_KUNDLI_DHRUV_PREDICTION, TAG_NEW_KUNDLI_DHRUV_NUMEROLOGY, TAG_NEW_KUNDLI_DHRUV_KP,
            TAG_NEW_KUNDLI_DHRUV_SHODASHVARGA, TAG_NEW_KUNDLI_DHRUV_LAL_KITAB, TAG_NEW_KUNDLI_DHRUV_VARSHFAL};


    public int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    // public Typeface typeface;
    public CScreenConstants SCREEN_CONSTANTS = null;
    private int mTitleRes;
    protected Fragment mFrag;
    public static final int SUB_ACTIVITY_USER_LOGIN_UPLOAD_KUNDLI = 1000;
    public static final int SUB_ACTIVITY_PLACE_SEARCH = 1001;
    public static final int SUB_ACTIVITY_TIME_PICKER = 1002;
    public static final int SUB_ACTIVITY_USER_LOGIN = 1003;
    public static final int SUB_ACTIVITY_USER_PREFERENCE = 1004;
    public static final int SUB_ACTIVITY_UPGRADE_PLAN_DIALOG = 1005;
    public static final int SUB_ACTIVITY_UPGRADE_PLAN_DIALOG_PURCHSE = 1006;
    public static final int SUB_ACTIVITY_UPGRADE_PLAN_UPDTAE = 1007;
    public static final int SUB_ACTIVITY_EXIT_PLAN = 2005;
    public static final int BOOK_MARK_LIST_ACTIVITY_CODE = 10001;
    public static final int SUB_ACTIVITY_USER_LOGIN_FOR_SHARE = 1010;
    public static final int SUB_ACTIVITY_BHRIGOO = 1011;
    public static final int BRIHAT_KUNDLI_PROFILE_CODE = 1012;

    int LALKITAB_INPUT_YEAR = -1;
    int VARSHPHAL_INPUT_YEAR = -1;
    public int SELECTED_MODULE = MODULE_BASIC;
    public int SELECTED_SUB_SCREEN = 0;
    public static final int RATE_DIALOG_THREAD_SLEEP_TIME = 60000;// ADDED BY
    // SHELENDRA
    // ON
    // 12-05-15

    public boolean calculateKundli = false;// It will true only when user call Get Kundli

    public Typeface regularTypeface;
    public Typeface mediumTypeface;
    public Typeface robotMediumTypeface;


    public Typeface robotRegularTypeface;
    protected Activity currentActivity;
    private final PurchaseBillingEventHandler purchaseBillingEventHandler = new PurchaseBillingEventHandler();

    public BaseInputActivity(int titleRes) {
        mTitleRes = titleRes;
    }

    protected AstrosageKundliApplication mVartaAstroApp;

    private void initValues() {
        //typeface = CUtils.getUserSelectedLanguageFontType(getApplicationContext(), LANGUAGE_CODE);
        regularTypeface = CUtils.getRobotoFont(
                getApplicationContext(), LANGUAGE_CODE, CGlobalVariables.regular);

        mediumTypeface = CUtils.getRobotoFont(
                getApplicationContext(), LANGUAGE_CODE, CGlobalVariables.medium);
        robotMediumTypeface = CUtils.getRobotoMedium(getApplicationContext());
        robotRegularTypeface = CUtils.getRobotoRegular(getApplicationContext());
        SCREEN_CONSTANTS = new CScreenConstants(this, regularTypeface);

    }


    /**
     * Called when the activity is first created.
     * Initializes the activity, sets up the UI, registers broadcast receivers,
     * initializes in-app purchases, and performs other setup tasks.
     *
     * @param savedInstanceState If the activity is being re-initialized after
     *                           previously being shut down then this Bundle contains the data it most
     *                           recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mVartaAstroApp = (AstrosageKundliApplication) this.getApplicationContext();

        // Enable edge-to-edge mode with sensible defaults (from AndroidX EdgeToEdge library)
        EdgeToEdge.enable(this);

        // Apply custom handling of insets (status bar, nav bar, keyboard padding)
        // so views don’t overlap with system bars or the keyboard
        CUtils.applyEdgeToEdgeInsets(this);

        if (myBroadcastReceiver == null) {
            myBroadcastReceiver = new MyBroadcastReceiver();
            LocalBroadcastManager.getInstance(this).registerReceiver(myBroadcastReceiver, new IntentFilter(ACTION_SYNC_SERVICE));
        }
        LANGUAGE_CODE = CUtils.getLanguageCodeFromPreference(this);

        SELECTED_MODULE = getIntent().getIntExtra(
                CGlobalVariables.MODULE_TYPE_KEY, SELECTED_MODULE);
        initInAppPurchase();
        initValues();
        setTitle(mTitleRes);
        String[] activityNames = {"HomeInputScreen", "OutputMasterActivity", "OutputMatchingMasterActivity"};
        String activityName = this.getClass().getSimpleName();
        ArrayList screenName = new ArrayList(Arrays.asList(activityNames));
        if (screenName != null && screenName.contains(activityName)) {
            boolean isShowDialog = CUtils.getBooleanData(BaseInputActivity.this, CGlobalVariables.ISSHOWASTROLOGERDIALOG, true);
            if (isShowDialog && CUtils.getKundliCount(BaseInputActivity.this) > 5) {
                //showDialogAfterSomeDelay();
            }

        }
    }




    @Override
    protected void onNewIntent(Intent intent) {
        try {
            LANGUAGE_CODE = CUtils.getLanguageCodeFromPreference(this);
            initValues();
        } catch (Exception ex) {
            //Log.i("Exception", ex.getMessage().toString());
        }
        super.onNewIntent(intent);
    }

    @Override
    protected void onResume() {

        try {
            mVartaAstroApp.setCurrentActivity(this);
        } catch (Exception e) {
        }


        try {
            LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication()).getLanguageCode();
            initValues();
        } catch (Exception ex) {
            //Log.i("Exception", ex.getMessage().toString());
        }
        super.onResume();
    }

    private void clearReferences() {
        Activity currActivity = mVartaAstroApp.getCurrentActivity();
        if (this.equals(currActivity))
            mVartaAstroApp.setCurrentActivity(null);
    }

    @Override
    public void switchContent(int position) {

        //Log.e("SAN ", "BIA switchContent() position => " + position);

        switch (position) {

            case TAG_SET_PREFERENCES:
                startActivity(new Intent(this, AstroPrefrenceActivity.class));
                break;

            case TAG_NOTIFICATION_SETTING:
                openNotificationSettingDialog();
                //CUtils.gotoNotificationSettingScreen(this);
                break;

            case TAG_CHANGE_LANGUAGE:
                CUtils.fcmAnalyticsEvents(CGlobalVariables.CHANGE_LANGUAGE_FOR_DRAWER, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
//                openLanguageSelectDialog();
                Intent langIntent = new Intent(this, ActWizardScreens.class);
                langIntent.putExtra("callerActivity", CGlobalVariables.APP_MODULE_SCREEN);
                langIntent.putExtra("isFromSplash", false);
                startActivity(langIntent);
                break;

            /*case TAG_SHARE_APP:
                CUtils.shareToFriendMail(this);
                break;*/

            case TAG_FEEDBACK:
                String activityName = this.getLocalClassName();
                CUtils.sendFeedBackViaApi(this, regularTypeface, CUtils.getUserName(this), activityName);
                break;

            /*case TAG_CALL_US:
                CUtils.gotoCallUsScreen(this, LANGUAGE_CODE);
                break;*/

            case TAG_RATE_APP:
                CUtils.rateAppication(this, true);
                //rateApplication();
                break;
            case TAG_SHARE_APP:
                CUtils.shareWithFriends(this);
                break;
            case TAG_ABOUT_US:
                CUtils.gotoAboutUsScreen(this, LANGUAGE_CODE);
                break;

            case TAG_JOIN_VARTA:
                CUtils.gotoJoinVartaPanel(this, LANGUAGE_CODE);
                break;

            case TAG_MY_KUNDLI:
                CUtils.gotoSearchKundliScreen(this);
                break;

            case TAG_CLOUD_SIGN_OUT:
                logoutFromAstroSageCloudSetPref();
                break;

            case TAG_UPGRADE_PRODUCT_PLAN_LIST:
                CUtils.fcmAnalyticsEvents(CGlobalVariables.NAVIGATION_DRAWER_UPGRADE_PLAN, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                CUtils.gotoProductPlanListUpdated(BaseInputActivity.this, LANGUAGE_CODE, SUB_ACTIVITY_UPGRADE_PLAN_UPDTAE, "", com.ojassoft.astrosage.varta.utils.CGlobalVariables.SOURCE_FROM_DRAWER_MENU);
                break;

            case TAG_REMOVE_ADS:
                CUtils.gotoProductPlanListUpdated(BaseInputActivity.this, LANGUAGE_CODE, SUB_ACTIVITY_UPGRADE_PLAN_UPDTAE, "", "remove_ads");
                break;

            case TAG_SIGN_IN:
                goToLogin(TAG_SIGN_IN);
                break;

            case TAG_SIGN_UP:
                goToLogin(TAG_SIGN_UP);
                break;
            case TAG_APP_SUBSCRIPTION:
                com.ojassoft.astrosage.varta.utils.CUtils.openWebBrowser(this, Uri.parse("https://play.google.com/store/account/subscriptions"));
                //startActivity(new Intent(this, ManageSubscriptionActivity.class));
                break;

            case TAG_NEW_KUNDLI:
                newKundli();
                break;
            case TAG_NOTES:
                openNotesDialog();
                break;

            case TAG_OPEN_KUNDLI:
                openKundli();
                break;

            case TAG_ASTROSAGE_ARTICLES:
                showAstrosageArticles();
                break;

            case TAG_HOME:
                //gotoHomeScreen(0);//Home menu
                gotoHomeScreen(1);
                break;

            case TAG_BOOKMARK:
                openBookMarkList();
                break;

            case MODULE_BASIC:
            case MODULE_DASA:
            case MODULE_PREDICTION:
            case MODULE_KP:
            case MODULE_SHODASHVARGA:
            case MODULE_LALKITAB:
            case MODULE_VARSHAPHAL:
            case MODULE_MISC:
                moduleNavigate(position);
                break;

            case MODULE_NUMEROLOGY:
                moduleNavigateNumerology(position);
                break;

            case TAG_EDIT_KUNDLI:
                setEditKundli();
                break;

            case TAG_DOWNLOAD_PDF:
                downloadPDF();
                break;

            case TAG_SHARE_SCREEN:
                goToEmailScreen();
                break;

            case TAG_CHART_STYLE:
                openChartStyleSelectDialog();
                break;

            case TAG_CHANGE_AYANAMSA:
                openAyanSelectDialog();
                break;

            case TAG_HELP:
                customeDialogHelp();
                break;

            case TAG_OUR_OTHER_APPS:
                ourOtherApps();
                break;

            case TAG_ASTRO_SHOP:
                //CUtils.gotoAstroShopScreen(this);
                //    gotoHomeScreen(4);//astro shop

                Intent i = new Intent(this, ActAstroShopCategories.class);
                startActivity(i);
                break;

            case TAG_ASK_OUR_ASTROLOGER:
                CUtils.openVartaTabActivity(this, FILTER_TYPE_CHAT);
                break;

            case TAG_BY_NAME:
                askUserToEnterName();
                break;

            case TAG_BY_BIRTH:
                moonSignByDateOfBirth();
                break;

            case TAG_HOROSCOPE_NOTIFICATION:
                openNotificationCategaryDialog();
                break;

            case TAG_TODAYS_HOROSCOPE:
            case TAG_WEEKLY_HOROSCOPE:
            case TAG_WEEKLY_LOVE_HOROSCOPE:
            case TAG_MONTHLY_HOROSCOPE:
            case TAG_YEARLY_HOROSCOPE:
            case TAG_HOROSCOPE_PANCHANG:
                callHorscope(position);
                break;

            case TAG_SHARE_PANCHANG:
                shareContentData("");
                break;

            case TAG_VIEW_BOYS_KUNDLI:
            case TAG_VIEW_GIRL_KUNDLI:
                openKundli(position);
                break;

            case TAG_MATCH_RESULT:
            case TAG_MATCH_RESULT_IN_DETAILS:
            case TAG_VAMA_WORK:
            case TAG_VASYA_DOMINANCE:
            case TAG_TARA_DESTINY:
            case TAG_YONI_MENTALITY:
            case TAG_MAITRI_COMPATIBILITY:
            case TAG_GANA:
            case TAG_BHAKOOT:
            case TAG_NADI:
                matchingOutputMainMenuSelected(position);
                break;
            case TAG_SET_PASSWORD:
                Intent intent = new Intent(BaseInputActivity.this, SetPasswordActivity.class);
                intent.putExtra("dologout", false);
                startActivityForResult(intent, 101);
            case TAG_ASTROLOGER_DIRECTORY:
                FragAskForAstrologer dialogFragment = new FragAskForAstrologer();
                showDialogFrag(dialogFragment, "FragAskForAstrologer");
                break;
            case TAG_SYNC_CHART:
                CUtils.syncChartWithCloud(this,false);
                break;

            case TAG_MONTHLY_PANCHANG:
                sendToMonthlyPanchang();
                break;
            case TAG_HINDU_CALENDAR:
                sendToHinduCalendar();
                break;
            case TAG_YEARLY_VART:
                sendToYearlyVart();
                break;
            case TAG_FESTIVAL:
                sendToFestival();
                break;
            case CGlobalVariables.MODULE_ASTROSAGE_PANCHANG:
                sendToHora(CGlobalVariables.MODULE_ASTROSAGE_PANCHANG);
                break;
            case CGlobalVariables.MODULE_ASTROSAGE_HORA:
                sendToHora(CGlobalVariables.MODULE_ASTROSAGE_HORA);
                break;
            case CGlobalVariables.MODULE_ASTROSAGE_CHOGADIA:
                sendToHora(CGlobalVariables.MODULE_ASTROSAGE_CHOGADIA);
                break;
            case CGlobalVariables.MODULE_DO_GHATI_MUHURT:
                sendToHora(CGlobalVariables.MODULE_DO_GHATI_MUHURT);
                break;
            case CGlobalVariables.MODULE_RAHUKAAL:
                sendToHora(CGlobalVariables.MODULE_RAHUKAAL);
                break;
            case CGlobalVariables.MODULE_BHADRA:
                sendToHora(CGlobalVariables.MODULE_BHADRA);
                break;
            case CGlobalVariables.MODULE_PANCHAK:
                sendToHora(CGlobalVariables.MODULE_PANCHAK);
                break;
            case CGlobalVariables.MODULE_MUHURAT:
                sendToHora(CGlobalVariables.MODULE_MUHURAT);
                break;
            case CGlobalVariables.MODULE_LAGNA:
                sendToHora(CGlobalVariables.MODULE_LAGNA);
                break;
            case TAG_OTHER_CALENDARS:
                sendToOtherCale();
                break;
            case TAG_ASK_A_QUES_CHAT_HISTORY:
                callAskAQuestionChatHistory();
                break;
            case TAG_ASTROLOGER:
                callAstrologersAct();
                break;
            case TAG_CUSTOMER_SUPPORT:
                openCustomerSupportDialog();
                break;
            case TAG_TERMS_AND_PRIVACY:
                callTermsAndPrivacyAct();
                break;
            /*case TAG_APP_INVITE:
                callAppInvite();
                break;*/

            case TAG_Share_CHART:
                shareChart();
                break;


            case TAG_NEW_KUNDLI_DHRUV:
                newKundli(TAG_NEW_KUNDLI_DHRUV);
                break;
            case TAG_NEW_KUNDLI_DHRUV_DASHA:
                newKundli(TAG_NEW_KUNDLI_DHRUV_DASHA);
                break;
            case TAG_NEW_KUNDLI_DHRUV_KP:
                newKundli(TAG_NEW_KUNDLI_DHRUV_KP);
                break;
            case TAG_NEW_KUNDLI_DHRUV_LAL_KITAB:
                newKundli(TAG_NEW_KUNDLI_DHRUV_LAL_KITAB);
                break;
            case TAG_NEW_KUNDLI_DHRUV_NUMEROLOGY:
                newKundli(TAG_NEW_KUNDLI_DHRUV_NUMEROLOGY);
                break;
            case TAG_NEW_KUNDLI_DHRUV_PREDICTION:
                newKundli(TAG_NEW_KUNDLI_DHRUV_PREDICTION);
                break;
            case TAG_NEW_KUNDLI_DHRUV_SHODASHVARGA:
                newKundli(TAG_NEW_KUNDLI_DHRUV_SHODASHVARGA);
                break;
            case TAG_NEW_KUNDLI_DHRUV_VARSHFAL:
                newKundli(TAG_NEW_KUNDLI_DHRUV_VARSHFAL);
                break;
            case TAG_NEW_KUNDLI_DHRUV_PRINT_KUNDLI:
                newKundli(TAG_NEW_KUNDLI_DHRUV_PRINT_KUNDLI);
                break;
            case TAG_NEW_KUNDLI_DHRUV_MATCH_MAKING:
                newKundli(TAG_NEW_KUNDLI_DHRUV_MATCH_MAKING);
                break;
            case TAG_REFER_AND_EARN:
                CUtils.fcmAnalyticsEvents(CGlobalVariables.APP_SHARE_AND_EARN_BUTTON_CLICK, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                Intent inviteApp = new Intent(this, InviteAppActivity.class);
                startActivity(inviteApp);
                break;
            case TAG_MY_ACCOUNT:

                Intent myAccount = new Intent(this, MyAccountActivity.class);
                startActivity(myAccount);
                break;
            case TAG_CALL_US:
                CallUsDialogFragment callUsDialogFragment = new CallUsDialogFragment();
                callUsDialogFragment.show(this.getSupportFragmentManager(), "call_us_dialog");
                break;
            case TAG_MY_KUNDLI_DOWNLOADS:
                com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents("Downloads_pdf_center_btn_click_event", AstrosageKundliApplication.currentEventType, "");

                Intent totalDownloadsIntent = new Intent(this, TotalDownloadActivity.class);
                startActivity(totalDownloadsIntent);
                break;


        }
    }

    /**
     * @author Amit Rautela
     * This method is used to call ActNotificationLanding Act to show chat history
     */
    private void callAskAQuestionChatHistory() {
        Intent intent = new Intent(this, ActNotificationLanding.class);
        startActivity(intent);
    }

    /**
     * @author Amit Rautela
     * This method is used to call ActAllAstrologers Act to show Astrologers list
     */
    public void callAstrologersAct() {
        Intent intent = new Intent(this, ActAllAstrologers.class);
        startActivity(intent);
        //
    }

    /**
     * @author Amit Rautela
     * This method is used to call TermsAndPrivacy Act
     */
    private void callTermsAndPrivacyAct() {
        Intent intent = new Intent(this, TermsAndPrivacy.class);
        startActivity(intent);
    }

    /**
     * @author Amit Rautela
     * This method is used to open CustomerSupportDialog
     */
    private void openCustomerSupportDialog() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        FragmentManager fm = getSupportFragmentManager();
        Fragment prev = fm.findFragmentByTag("CustomerSupportDialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        CustomerSupportDialog clfd = new CustomerSupportDialog();
        clfd.show(fm, "CustomerSupportDialog");
        ft.commit();
    }

    // call shared chart method in child class....
    public void shareChart() {
    }

    public void sendToOtherCale() {
       /* Intent intent=new Intent(this,ActCalendar.class);
        startActivity(intent);*/

        CUtils.googleAnalyticSendWitPlayServie(this,
                CGlobalVariables.GOOGLE_ANALYTIC_LAUNCH,
                CGlobalVariables.GOOGLE_ANALYTIC_CALENDAR, null);
        Intent intent = new Intent(this, ActCalendar.class);
        intent.putExtra(CGlobalVariables.ASTRO_WEBVIEW_TITLE_KEY,
                CGlobalVariables.MODULE_CALENDAR);
        startActivity(intent);
    }

    public void sendToHora(int tagHoraPanchang) {
        Intent intent = new Intent(this, InputPanchangActivity.class);
        intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, tagHoraPanchang);
        intent.putExtra("date", Calendar.getInstance());
        intent.putExtra("place", "");
        startActivity(intent);
    }

    public void sendToDailyPanchang() {
        Intent intent = new Intent(this, InputPanchangActivity.class);
        //intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, moduleType);
        intent.putExtra("date", Calendar.getInstance());
        intent.putExtra("place", "");
        startActivity(intent);
    }

    public void sendToMonthlyPanchang() {
        Intent intent = new Intent(this, ActMontlyCalendar.class);
        startActivity(intent);
    }

    public void sendToHinduCalendar() {
        Intent intent = new Intent(this, ActHinduCalender.class);
        startActivity(intent);
    }

    public void sendToYearlyVart() {
        Intent intent = new Intent(this, ActYearlyVrat.class);
        startActivity(intent);
    }

    public void sendToFestival() {
        Intent intent = new Intent(this, ActIndianCalender.class);
        startActivity(intent);
    }

    public void matchingOutputMainMenuSelected(int menuItemPosition) {
    }

    ;

    /**
     * call sub class method for open Kundli in OutputMatchingMasterActivity
     */
    public void openKundli(int position) {
    }

    void shareContentData(String packageName) {
    }

    ;

    public void callHorscope(int position) {
    }

    ;

    void openBookMarkList() {
        startActivityForResult(new Intent(BaseInputActivity.this,
                ActBookmarkAndHistory.class), BOOK_MARK_LIST_ACTIVITY_CODE);
    }

    public void openNotificationCategaryDialog() {
    }

    ;

    public void askUserToEnterName() {
    }

    ;

    void customeDialogHelp() {
    }

    ;

    void openAyanSelectDialog() {
    }

    ;

    void openChartStyleSelectDialog() {
    }

    ;

    public void goToEmailScreen() {
    }

    ;

    public void downloadPDF() {
    }

    public void setEditKundli() {
    }

    /**
     * call sub class method for New Kundli
     */
    public void newKundli() {
    }

    public void newKundli(int pos) {
    }

    /**
     * call sub class method for open Kundli
     */
    public void openKundli() {
    }

    /**
     * call sub class method for
     */
    public void logoutFromAstroSageCloud(boolean isShowToast) {
    }

    void moduleNavigate(int moduleIndex) {
        callCalculateKundli(moduleIndex, false, 0, 0);
    }

    ;

    public void openNotesDialog() {

    }

    void gotoHomeScreen(int id) {
        CUtils.gotoHomeScreenWithTabId(BaseInputActivity.this, id);
    }

    private void ourOtherApps() {

        Intent browserIntent = new Intent(Intent.ACTION_VIEW,
                Uri.parse(CGlobalVariables.OUR_OTHER_SOFTWARE_URL));
        startActivity(browserIntent);

    }

    void moduleNavigateNumerology(int position) {
        try {

            Intent intent = new Intent(BaseInputActivity.this, NumerologyCalculatorInputActivity.class);
            //intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, CGlobalVariables.MODULE_NUMERLOGY_CALCULATOR);
            intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, position);
            intent.putExtra("FROM", "OMA");
            startActivity(intent);

        } catch (Exception e) {
        }

    }

    void callCalculateKundli(int position, boolean is_bookMarkSelectFromAppModule, int group_id, int child_id) {

        Object obj = CUtils.getCustomObject(BaseInputActivity.this);
        BeanHoroPersonalInfo info = null;
        if (obj != null) {
            info = (BeanHoroPersonalInfo) obj;
            CalculateKundli kundli = new CalculateKundli(info, false, BaseInputActivity.this, regularTypeface, position, CGlobalVariables.APP_MODULE_SCREEN, is_bookMarkSelectFromAppModule, group_id, child_id, 0);
            kundli.calculate();
        }
    }

    private void logoutFromAstroSageCloudSetPref() {

        boolean isAutoGeneratedPassword = CUtils.isAutoGeneratedPassword(BaseInputActivity.this);
        if (isAutoGeneratedPassword) {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Fragment prev = fm.findFragmentByTag("SignOutDialogFragment");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);
            SignOutDialogFragment signOutDialogFragment = SignOutDialogFragment.getInstance();
            // confirmDeleteKundliDialog.setTargetFragment(SearchBirthDetailsFragment.this, 0);
            signOutDialogFragment.show(fm, "SignOutDialogFragment");
            ft.commit();
        } else {
            CUtils.logoutFromApp(this);
            logoutFromAstroSageCloud(true);
        }
    }

    public ArrayList<BeanHoroPersonalInfo> deleteOnlineKundliFromList(ArrayList<BeanHoroPersonalInfo> kundliList) {
        //int size = kundliList.size();
        if (kundliList != null) {
            for (int i = 0; i < kundliList.size(); ) {
                if (!kundliList.get(i).getOnlineChartId().equals("") && !kundliList.get(i).getOnlineChartId().equals("-1")) {
                    kundliList.remove(i);
                    i = 0;
                } else {
                    i++;
                }
            }
        }
        return kundliList;
    }

    private void showAstrosageArticles() {

        CUtils.googleAnalyticSendWitPlayServie(this,
                CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                CGlobalVariables.GOOGLE_ANALYTIC_KUNDLI_ARTICLES, null);

        Intent intent = new Intent(this, ActShowOjasSoftArticlesWithTabs.class);
        intent.putExtra(CGlobalVariables.ASTRO_WEBVIEW_TITLE_KEY,
                CGlobalVariables.MODULE_ASTROSAGE_ARTICLES);
        startActivity(intent);

        //startActivity(new Intent(this, ActShowOjasSoftArticles.class));

    }

    private void openLanguageSelectDialog() {
        try {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            FragmentManager fm = getSupportFragmentManager();
            Fragment prev = fm.findFragmentByTag("HOME_INPUT_LANGUAGE");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);
            ChooseLanguageFragmentDailog clfd = new ChooseLanguageFragmentDailog();
            clfd.show(fm, "HOME_INPUT_LANGUAGE");
            ft.commit();
        } catch (Exception e) {
            //
        }
    }

    private void openNotificationSettingDialog() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        FragmentManager fm = getSupportFragmentManager();
        Fragment prev = fm.findFragmentByTag("USER_NOTIFICATION_SETTING");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        NotificationSettingFragment nsf = new NotificationSettingFragment();
        nsf.show(fm, "USER_NOTIFICATION_SETTING");
        ft.commit();
    }

    ListPopupWindow popupWindow = null;

    public void settingForpopUpMenu(View view, String[] subMenuItems, TypedArray subMenuItemsIcon, Integer[] menuIndex) {

       /* String[] subMenuItems = getResources().getStringArray(
                R.array.app_home_menu_item_list);
        TypedArray subMenuItemsIcon = getResources().obtainTypedArray(
                R.array.app_home_menu_item_list_icon);*/


        final ArrayList<HashMap<String, Object>> data = new ArrayList<HashMap<String, Object>>();
        for (int i = 0; i < subMenuItems.length; i++) {
            if (!CUtils.isVisisbilityGoneForSignInAndRemoveAddsMenu(menuIndex[i], BaseInputActivity.this)) {

                HashMap<String, Object> map = new HashMap<String, Object>();
                map.put("TITLE", subMenuItems[i]);
                map.put("ICON", subMenuItemsIcon.getResourceId(i, -1));
                map.put("INDEX", menuIndex[i]);//app_home_menu_item_list_index[i]
                data.add(map);
            }
        }

        popupWindow = new ListPopupWindow(BaseInputActivity.this);
        CustomSimpleAdapter adapter = new CustomSimpleAdapter(BaseInputActivity.this, data, R.layout.custom_popup_menu_items, null, null, regularTypeface);

        popupWindow.setAnchorView(view);
        popupWindow.setAdapter(adapter);

        int valueInPixelsVertical = getResources().getDimensionPixelSize(R.dimen.popup_window_vertical_offset);
        int valueInPixelsHorizontal = getResources().getDimensionPixelSize(R.dimen.popup_window_horizontal_offset);
        /*popupWindow.setPromptPosition(-200);*/
        popupWindow.setHorizontalOffset(-valueInPixelsHorizontal);
        popupWindow.setVerticalOffset(-valueInPixelsVertical);
        int width = getResources().getDimensionPixelSize(R.dimen.overflow_width_popup_window);
        popupWindow.setWidth(width); // note: don't use pixels, use a dimen resource
        //popupWindow.setHeight(ViewGroup.LayoutParams.MATCH_PARENT);
        popupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                int index = (int) data.get(i).get("INDEX");
                switchContent(index);
                popupWindow.dismiss();
            }
        }); // the callback for when a list item is selected
        popupWindow.setHorizontalOffset(400);
        popupWindow.show();


    }

    private class CustomSimpleAdapter extends SimpleAdapter {

        private ArrayList<HashMap<String, Object>> results;
        Typeface typeface;

        public CustomSimpleAdapter(Context context, ArrayList<HashMap<String, Object>> data, int resource, String[] from, int[] to, Typeface typeface) {
            super(context, data, resource, from, to);
            this.results = data;
            this.typeface = typeface;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View v = convertView;
            if (v == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                v = vi.inflate(R.layout.custom_popup_menu_items, null);
            }

            ImageView listIcon = (ImageView) v.findViewById(R.id.listIcon);
            TextView listText = (TextView) v.findViewById(R.id.listText);
            listText.setTypeface(typeface);

            listText.setText(results.get(position).get("TITLE").toString());

            String mDrawableName = results.get(position).get("ICON").toString();
            int resID = getResources().getIdentifier(mDrawableName, "drawable", getPackageName());
            Drawable drawable = ResourcesCompat.getDrawable(getResources(), resID, null);
            listIcon.setImageDrawable(drawable);

            return v;
        }
    }

    public void goToLogin(int position) {

        if (!CUtils.isUserLogedIn(BaseInputActivity.this)) {

            //int pos = 0;
            //Intent intent = new Intent(BaseInputActivity.this, ActLogin.class);
            //intent.putExtra("callerActivity", CGlobalVariables.HOME_INPUT_SCREEN);
            /*if (position == TAG_SIGN_IN) {
                pos = 0;//sign in
            } else {
                pos = 1;//sign up
            }*/

            //intent.putExtra("screenId", pos);
            Intent intent = new Intent(BaseInputActivity.this, LoginSignUpActivity.class);
            intent.putExtra(IS_FROM_SCREEN, BASE_INPUT_SCREEN);
            intent.putExtra(IS_GOOGLE_FACEBOOK_VISIBLE, true);
            startActivityForResult(intent, SUB_ACTIVITY_USER_LOGIN);
        }
    }

    public void setVisibilityOfMoreIconImage(View view, final String[] subMenuItems, final TypedArray subMenuItemsIcon, final Integer[] menuIndex) {
        Object obj = CUtils.getCustomObject(BaseInputActivity.this);
        if (obj != null) {
            view.setVisibility(View.VISIBLE);
            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    settingForpopUpMenu(view, subMenuItems, subMenuItemsIcon, menuIndex);
                }
            });

        } else {
            view.setVisibility(View.GONE);
            view.setOnClickListener(null);
        }
    }

    protected void setBookMarkIcon(int subMenuItemPosition) {
        try {
            if (CUtils.isScreenBookMarked(BaseInputActivity.this, SELECTED_MODULE,
                    subMenuItemPosition))
                ((ImageView) findViewById(R.id.action_bookmark_Menu)).setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_action_bookmarked));
            else
                ((ImageView) findViewById(R.id.action_bookmark_Menu)).setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_action_unbookmarked));
                /*findViewById(R.id.action_bookmark_Menu).setIcon(
                        R.drawable.ic_action_unbookmarked);*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void setHistoryScreen(int subMenuItemPosition) {
        try {

            if (!CUtils.isScreenInHistoryQueue(BaseInputActivity.this,
                    SELECTED_MODULE, subMenuItemPosition)) {
                CScreenHistoryItemCollection.getScreenHistoryItemCollection(
                        BaseInputActivity.this).addScreenInHistory(SELECTED_MODULE,
                        subMenuItemPosition);
                // save Bookmark Collection in file system
                SerializeAndDeserializeBeans
                        .saveSerializedBeanObject(
                                BaseInputActivity.this,
                                CGlobalVariables.SCREEN_HISTORY_COLLECTION_FILE_NAME,
                                CScreenHistoryItemCollection
                                        .getScreenHistoryItemCollection(BaseInputActivity.this));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void goToBookMark(int module, int sub_module) {
        try {
            boolean addedScreenInBookmarks = CUtils.bookMarkOrUnBookMarkScreen(
                    BaseInputActivity.this, module, sub_module);
            // BaseInputActivity.this, SELECTED_MODULE, SELECTED_SUB_SCREEN);
            changeBookMarkOptionIcon(addedScreenInBookmarks);

            CUtils.googleAnalyticSendWitPlayServie(this,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                    CGlobalVariables.GOOGLE_ANALYTIC_KUNDLI_BOOKMARK, null);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void changeBookMarkOptionIcon(boolean addedScreenInBookmarks) {
        if (addedScreenInBookmarks) {
            ((ImageView) findViewById(R.id.action_bookmark_Menu)).setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_action_bookmarked));
            /*actionMenu.findItem(R.id.action_bookmark_Menu).setIcon(
                    R.drawable.ic_action_bookmarked);*/
        } else {
            ((ImageView) findViewById(R.id.action_bookmark_Menu)).setImageDrawable(ContextCompat.getDrawable(this, R.drawable.ic_action_unbookmarked));
            /*actionMenu.findItem(R.id.action_bookmark_Menu).setIcon(
                    R.drawable.ic_action_unbookmarked);*/
        }
    }

    private void showChangeYearPopUp() {
        int inputYear = 0;
        if (SELECTED_MODULE == MODULE_LALKITAB)
            inputYear = LALKITAB_INPUT_YEAR;
        if ((SELECTED_MODULE == MODULE_VARSHAPHAL)
                || (SELECTED_MODULE == MODULE_PREDICTION))
            inputYear = VARSHPHAL_INPUT_YEAR;

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        FragmentManager fm = getSupportFragmentManager();
        Fragment prev = fm.findFragmentByTag("FRAG_CAL");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        YearInputBoxPopupFragmentDialog yipf = new YearInputBoxPopupFragmentDialog();
        Bundle bundle = new Bundle();
        bundle.putInt(CGlobalVariables.LANGUAGE_CODE, LANGUAGE_CODE);
        bundle.putInt("INPUT_YEAR", inputYear);
        bundle.putInt("BIRTH_YEAR", CGlobal.getCGlobalObject()
                .getHoroPersonalInfoObject().getDateTime().getYear());

        yipf.setArguments(bundle);
        yipf.show(fm, "FRAG_CAL");
        ft.commit();
    }


    public int getPresentYearNumber() {

        int presentYearNumber;
        int mYear;
        int mMonth;
        int mDay;

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        presentYearNumber = mYear
                - CGlobal.getCGlobalObject().getHoroPersonalInfoObject()
                .getDateTime().getYear();
        try {
            if ((CGlobal.getCGlobalObject().getHoroPersonalInfoObject()
                    .getDateTime().getMonth() - mMonth) == 0) {
                if ((CGlobal.getCGlobalObject().getHoroPersonalInfoObject()
                        .getDateTime().getDay() - mDay) > 0)
                    presentYearNumber -= 1;

            } else if ((CGlobal.getCGlobalObject().getHoroPersonalInfoObject()
                    .getDateTime().getMonth() - mMonth) > 0) {
                presentYearNumber -= 1;
            }
        } catch (Exception e) {
            //Log.e("Init Varshfal year", e.getMessage());
        }
        if (presentYearNumber < 0) {
            presentYearNumber = 0;
        }
        return presentYearNumber;
    }

    @Override
    public void onSelectedInputYear(int inputyear) {

        this.selectedInputYear(inputyear); // ADDED BY BIJENDRA ON 05-06-14
    }

    @Override
    public void openInputYearCalendar() {
        showChangeYearPopUp();

    }

    void selectedInputYear(int inputyear) {
    }

    private void moonSignByDateOfBirth() {
        Intent intent = new Intent(this, ActShowOjasSoftArticles.class);
        intent.putExtra(
                CGlobalVariables.ASTRO_WEBVIEW_TITLE_KEY,
                CGlobalVariables.MODULE_HOROSCOPE_KNOW_YOUR_MOON_SIGN_BY_DOB);
        startActivity(intent);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        try {
            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
                if ((popupWindow != null) && (popupWindow.isShowing())) {
                    popupWindow.dismiss();
                    return false;
                }
            }
        } catch (Exception ex) {
            //Log.i("Exception", ex.getMessage().toString());
        }

        return super.dispatchKeyEvent(event);
    }

    private void rateApplication() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        FragmentManager fm = getSupportFragmentManager();
        Fragment prev = fm.findFragmentByTag("Rate_Application");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        AppRateFrag afd = new AppRateFrag();
        afd.show(fm, "Rate_Application");
        ft.commit();
    }


    private void showFragmentDialog() {

        //if (getKundliCount() > 5) {
        FragAskForAstrologer dialogFragment = new FragAskForAstrologer();
        showDialogFrag(dialogFragment, "FragAskForAstrologer");
        CUtils.saveBooleanData(BaseInputActivity.this, CGlobalVariables.ISSHOWASTROLOGERDIALOG, false);
        //}


    }

    public void isUserAstrologer() {
        BeanUserMapping beanUserMapping = CUtils.getUserMappingData(BaseInputActivity.this);
        if (beanUserMapping == null) {
            beanUserMapping = new BeanUserMapping();
        }
        if (CUtils.getKundliCount(BaseInputActivity.this) >= 5 && beanUserMapping.getIsAstrologer() != 1) {
            beanUserMapping.setIsAstrologer(1);
            beanUserMapping.setDeviceId(CUtils.getMyAndroidId(BaseInputActivity.this));
            beanUserMapping.setStatus(1);
            CUtils.saveUserMappingData(BaseInputActivity.this, beanUserMapping);
            CUtils.saveBooleanData(BaseInputActivity.this, CGlobalVariables.ISSHOWASTROLOGERDIALOG, true);
        }
    }


    public void showDialogFrag(DialogFragment dialogFragment, String tag) {
        try {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Fragment prev = fm.findFragmentByTag(tag);
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);
            dialogFragment.show(fm, tag);
            ft.commitAllowingStateLoss();
        } catch (IllegalStateException e) {
            //Log.e("Error>>", "" + e.getMessage());
        }

    }

    private void showDialogAfterSomeDelay() {
        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            public void run() {
                if (this != null && !isFinishing()) {
                    showFragmentDialog();
                }
            }
        }, 5000);
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            Bundle result = intent.getBundleExtra("result");
            int resultCode = result.getInt("resultcode");
            String msg = result.getString("resultstr");

//            Log.e("SAN", "onReceive: msg "+msg );
  //          Log.e("SAN", "onReceive: resulcode "+msg );
            if (resultCode == 0) {
                if (msg.equalsIgnoreCase(getString(R.string.server_error))) {
                    CUtils.gotoshowAlertActivity(
                            BaseInputActivity.this, LANGUAGE_CODE,
                            HomeInputScreen.SUB_ACTIVITY_UPGRADE_PLAN_DIALOG, 6);
                    //startActivity(new Intent(BaseInputActivity.this, PlanAlertActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                } else {
                    Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
                }
            } else if (resultCode == 1) {
                CUtils.gotoshowAlertActivity(
                        BaseInputActivity.this, LANGUAGE_CODE,
                        HomeInputScreen.SUB_ACTIVITY_UPGRADE_PLAN_DIALOG, 6);
                //startActivity(new Intent(BaseInputActivity.this, PlanAlertActivity.class).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
            } /*else if (resultCode == -5) {

                Toast.makeText(context, getResources().getString(R.string.chart_upoloaded), Toast.LENGTH_SHORT).show();
            } else if (resultCode == -4) {
                Toast.makeText(context, getResources().getString(R.string.server_error), Toast.LENGTH_SHORT).show();
            } else if (resultCode == 11) {
                Toast.makeText(context, getResources().getString(R.string.chart_upoload_error), Toast.LENGTH_SHORT).show();
            }*/

        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(myBroadcastReceiver);
        } catch (Exception e) {

        }
    }


    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

   /* public void showAlertDialog() {
        try {
            FragmentManager fm = getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();

            Fragment prev = fm.findFragmentByTag("PlanAlertDialog");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);

            PlanAlertDialog horaMeaning = PlanAlertDialog.newInstance();
            horaMeaning.show(fm, "PlanAlertDialog");
            ft.commit();
            //ft.commitAllowingStateLoss();
        } catch (Exception e) {
            Log.i("Error msg>>", "" + e.getMessage());
        }


    }*/

    /**
     * Snack Bar to Show Error Messages to user
     *
     * @param view
     * @param text
     */
    public void showSnackbar(View view, String text) {
        try {
            Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT);
            snackbar.getView().setBackgroundColor(getColor(R.color.colorPrimary_day_night));
            TextView tv = snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text); //snackbar_text
            tv.setTextColor(getColor(R.color.white));
            tv.setTextSize(16);
            snackbar.show();
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public void setBillingEventHandler(BillingEventHandler handler) {
        if (purchaseBillingEventHandler != null) {
            purchaseBillingEventHandler.setBillingEventHandler(handler);
        }
    }

    public void initInAppPurchase() {
        if (AstrosageKundliApplication.billingClient == null) {
            AstrosageKundliApplication.billingClient = BillingClient.newBuilder(getApplicationContext())
                    .enablePendingPurchases()
                    .setListener(purchaseBillingEventHandler)
                    .build();

            AstrosageKundliApplication.billingClient.startConnection(new BillingClientStateListener() {
                @Override
                public void onBillingSetupFinished(BillingResult billingResult) {
                    // Logic from ServiceConnection.onServiceConnected should be moved here.
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        Log.e("BillingClient", "onBillingSetupFinished() OK");
                        CUtils.startServiceToConsumeProduct(BaseInputActivity.this);
                    } else {
                        Log.e("BillingClient", "onBillingSetupFinished() FAIL");
                    }
                }

                @Override
                public void onBillingServiceDisconnected() {
                    //Log.e("BillingClient", "onBillingServiceDisconnected()");
                    // Logic from ServiceConnection.onServiceDisconnected should be moved here.
                }
            });
        }
    }

    @Override
    public void onPurchasesUpdated(@NonNull BillingResult var1, @Nullable List<Purchase> var2) {

    }
}
