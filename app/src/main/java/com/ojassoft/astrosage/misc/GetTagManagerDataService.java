package com.ojassoft.astrosage.misc;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tagmanager.Container;
import com.google.android.gms.tagmanager.ContainerHolder;
import com.google.android.gms.tagmanager.TagManager;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.model.CDatabaseHelperOperations;
import com.ojassoft.astrosage.model.YoutubeKeySingleton;
import com.ojassoft.astrosage.tagmanager.ContainerHolderSingleton;
import com.ojassoft.astrosage.tagmanager.ContainerLoadedCallback;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.ojassoft.astrosage.utils.CGlobalVariables.GET_USER_CHAT_CATEGORY_ENABLED;
import static com.ojassoft.astrosage.utils.CGlobalVariables.INTERNATIONAL_DEFAULT_ORDER_PAYMENT_METHOD;

import static com.ojassoft.astrosage.utils.CGlobalVariables.IS_STRIPE_VISIBLE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.SHOW_NEW_CONNECT_CHAT_DIALOG;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.IS_AI_CALL_SHOW_IN_AI_CHAT;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.IS_AI_CALL_SHOW_IN_NOTIFICATION;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.NEW_USER_AI_CHAT_5MIN;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.SHOW_INTERNATIONAL_PAY_IN_INR;

import org.apache.commons.logging.LogFactory;

public class GetTagManagerDataService extends IntentService {

    private static final org.apache.commons.logging.Log log = LogFactory.getLog(GetTagManagerDataService.class);
    private TagManager tagManager;
    private PendingResult<ContainerHolder> pending;
    private int LANGUAGE_CODE;

    public GetTagManagerDataService() {
        super("GetTagManagerDataService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //Log.e("GetTagManager","onHandleIntent");
        try{
           int kundaliCount =  new CDatabaseHelperOperations(this).getKundliCount();
            //Log.e("GetTagManager","kundali count = "+kundliCount);
        }catch(Exception ignore){
        }
        LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication()).getLanguageCode();
        tagManager = TagManager.getInstance(this);
        if (tagManager != null) {
            tagManager.setVerboseLoggingEnabled(true);
        }
        try {
            pending = tagManager.loadContainerPreferNonDefault(CGlobalVariables.CONTAINER_ID, R.raw.binary_file);
            if (pending != null) {
                pending.setResultCallback(new ResultCallback<ContainerHolder>() {
                    @Override
                    public void onResult(ContainerHolder containerHolder) {
                        try {
                            ContainerHolderSingleton.setContainerHolder(containerHolder);
                            Container container = containerHolder.getContainer();
                            if (!containerHolder.getStatus().isSuccess()) {
                                return;
                            }
                            ContainerHolderSingleton.setContainerHolder(containerHolder);
                            ContainerLoadedCallback.registerCallbacksForContainer(container);
                            containerHolder.setContainerAvailableListener(new ContainerLoadedCallback());

                            new TagManagerDataAsync().executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

                            ExecutorService executor = Executors.newSingleThreadExecutor();
                            executor.execute(new Runnable() {
                                @Override
                                public void run() {
                                    getDataFromGTMMainCointainer();
                                    Log.e("gtmcheck", " AT SPLASH run:gtm " );
                                }
                            });

                        } catch (Exception ex) {
                            //
                        }
                    }
                }, CGlobalVariables.TIMEOUT_FOR_CONTAINER_OPEN_MILLISECONDS, TimeUnit.MILLISECONDS);

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    class TagManagerDataAsync extends AsyncTask<Void, Integer, String> {

        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected String doInBackground(Void...arg0) {
           getDataFromGTMMainCointainer();
           return "";
        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
        }
    }

    public void getDataFromGTMMainCointainer() {
        try {
            //Log.d("GetTagManager", "getDataFromGTMMainCointainer1");
            if (ContainerHolderSingleton.getContainerHolder() != null) {
                ContainerHolderSingleton.getContainerHolder().refresh();
                Container container = ContainerHolderSingleton.getContainerHolder().getContainer();
                if (container != null) {
                    CUtils.saveBooleanData(this,CGlobalVariables.KEY_AK_RTDB_LONG_POLLING,container.getBoolean(CGlobalVariables.KEY_AK_RTDB_LONG_POLLING));

                    CUtils.saveStringData(this,CGlobalVariables.GOLD_PLAN_QUES_COUNT_KEY,container.getString(CGlobalVariables.key_GoldPlanQuestionCount));
                    CUtils.saveStringData(this,CGlobalVariables.DHRUV_PLAN_QUES_COUNT_KEY,container.getString(CGlobalVariables.key_DhruvPlanQuestionCount));
                    CUtils.saveStringData(this,CGlobalVariables.SILVER_PLAN_QUES_COUNT_KEY,container.getString(CGlobalVariables.key_SliverPlanQuestionCount));

                    //Log.e("gtmcheck", "getDataFromGTMMainCointainer: gold :"+container.getString(CGlobalVariables.key_GoldPlanQuestionCount)+"\n"+"silver:"+container.getString(CGlobalVariables.key_SliverPlanQuestionCount)+"\n"+"dhruv"+container.getString(CGlobalVariables.key_DhruvPlanQuestionCount) );


                    /*String aIChatSupportedLanguages = container.getString(CGlobalVariables.AIChatSupportedLanguages);
                    CUtils.saveStringData(this, CGlobalVariables.AIChatSupportedLanguages, aIChatSupportedLanguages);

                    String aIFreeChatPopupSupportedLanguages = container.getString(CGlobalVariables.AIFreeChatPopupSupportedLanguages);
                    CUtils.saveStringData(this, CGlobalVariables.AIFreeChatPopupSupportedLanguages, aIFreeChatPopupSupportedLanguages);
*/
                    //boolean enabledAIFreeChatPopup = container.getBoolean(CGlobalVariables.enabledAIFreeChatPopup);
                    //CUtils.saveBooleanData(this, CGlobalVariables.enabledAIFreeChatPopup, enabledAIFreeChatPopup);

                    boolean mainModuleCustomAdsVisibility = container.getBoolean(CGlobalVariables.key_MainModuleCustomAdsVisibility);
                    CUtils.saveBooleanData(this, CGlobalVariables.key_MainModuleCustomAdsVisibility, mainModuleCustomAdsVisibility);
                    if(mainModuleCustomAdsVisibility) {
                        String mainModuleCustomAdsImageData = container.getString(CGlobalVariables.key_MainModuleCustomAdsImageUrl);
                        CUtils.saveStringData(this, CGlobalVariables.key_MainModuleCustomAdsImageUrl, mainModuleCustomAdsImageData);
                        String mainModuleCustomAdsImageClickListenerUrl = container.getString(CGlobalVariables.key_MainModuleCustomAdsImageClickListenerUrl);
                        CUtils.saveStringData(this, CGlobalVariables.key_MainModuleCustomAdsImageClickListenerUrl, mainModuleCustomAdsImageClickListenerUrl);

                    }
                    //Added for free call/chat popup
                    String showFreeCallChatPopupType = container.getString(CGlobalVariables.showFreeCallChatPopupType);
                    CUtils.saveStringData(this, CGlobalVariables.showFreeCallChatPopupType, showFreeCallChatPopupType);
                    //Log.d("TestLog", "showFreeCallChatPopupType1="+showFreeCallChatPopupType);

                    //Getting Ads id from Cointainer
                    //String interstitialAdId = container.getString(CGlobalVariables.InterstitialAdId);
                    //String bannerAdId = container.getString(CGlobalVariables.BannerAdId);
                    //String exitScreenBannerAdId = container.getString(CGlobalVariables.ExitScreenBannerAdId);
                    //Log.e("GetTagManager","bannerAdId"+bannerAdId);
                    //CUtils.saveStringData(this, CGlobalVariables.InterstitialAdId, interstitialAdId);
                    //CUtils.saveStringData(this, CGlobalVariables.BannerAdId, bannerAdId);
                    //CUtils.saveStringData(this, CGlobalVariables.ExitScreenBannerAdId, exitScreenBannerAdId);

                    //String exitScreenCustomImageUrl = container.getString(CGlobalVariables.ExitScreenCustomImageUrl);
                    //String exitScreenCustomImageClickListener = container.getString(CGlobalVariables.ExitScreenCustomImageClickListener);

                    //CUtils.saveStringData(this, CGlobalVariables.ExitScreenCustomImageUrl, exitScreenCustomImageUrl);
                    //CUtils.saveStringData(this, CGlobalVariables.ExitScreenCustomImageClickListener, exitScreenCustomImageClickListener);

                    //Getting Variables for UniversalCustomAds
                    String universalCustomAdsImageClickListenerUrl = container.getString(CGlobalVariables.key_UniversalCustomAdsImageClickListenerUrl);
                    String universalCustomAdsImageData = container.getString(CGlobalVariables.key_UniversalCustomAdsImageUrl);
                    String key = CUtils.getLanguageKey(LANGUAGE_CODE);
                    String universalCustomAdsImageUrl = CUtils.getLanguageBasedUrl(universalCustomAdsImageData, key);
                    boolean universalCustomAdsVisibility = container.getBoolean(CGlobalVariables.key_UniversalCustomAdsVisibility);

                    if (!universalCustomAdsImageUrl.equals("")) {
                        CUtils.saveStringData(this, CGlobalVariables.customAdvertisementImage, universalCustomAdsImageUrl);
                        CUtils.saveStringData(this, CGlobalVariables.customAdvertisementLink, universalCustomAdsImageClickListenerUrl);
                        CUtils.saveBooleanData(this, CGlobalVariables.customAdvertisementAdsVisibility, universalCustomAdsVisibility);
                    } else {
                        //due to some error
                        CUtils.saveBooleanData(this, CGlobalVariables.customAdvertisementAdsVisibility, false);
                    }

                    boolean googleWalletPaymentVisibility = container.getBoolean(CGlobalVariables.key_GoogleWalletPaymentVisibility);
                    boolean paytmPaymentVisibility = container.getBoolean(CGlobalVariables.key_PaytmPaymentVisibility);

                    String phoneNoOne = container.getString(CGlobalVariables.key_Phone_One);

                    CUtils.saveStringData(this, CGlobalVariables.key_Phone_One, phoneNoOne);


                    CUtils.saveBooleanData(this, CGlobalVariables.key_GoogleWalletPaymentVisibility, googleWalletPaymentVisibility);
                    CUtils.saveBooleanData(this, CGlobalVariables.key_PaytmPaymentVisibility, paytmPaymentVisibility);

                    boolean askAQuestionHeaderImagesVisibility = container.getBoolean(CGlobalVariables.key_AskAQuestionHeaderAndFooterVisibility);
                    CUtils.saveBooleanData(this, CGlobalVariables.key_AskAQuestionHeaderAndFooterVisibility, askAQuestionHeaderImagesVisibility);


                    boolean razorPayVisibilityForServices = container.getBoolean(CGlobalVariables.key_RazorPayVisibilityServices);
                    boolean paytmVisibilityforServices = container.getBoolean(CGlobalVariables.key_PaytmWalletVisibilityServices);
                    CUtils.saveBooleanData(this, CGlobalVariables.key_RazorPayVisibilityServices, razorPayVisibilityForServices);
                    CUtils.saveBooleanData(this, CGlobalVariables.key_PaytmWalletVisibilityServices, paytmVisibilityforServices);

                    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {

                        razorPayVisibilityForServices = false;
                        CUtils.saveBooleanData(this, CGlobalVariables.key_RazorPayVisibilityServices, razorPayVisibilityForServices);

                    }

                    //showStaticHoroscope variable to show static Horoscope notification or not
                    boolean showStaticHoroscope = container.getBoolean(CGlobalVariables.showStaticHoroscope);
                    CUtils.saveBooleanData(this, CGlobalVariables.showStaticHoroscope, showStaticHoroscope);

                    //Getting data of static Horoscope notification
                    String staticHoroscopeDaily = container.getString(CGlobalVariables.staticHoroscopeDaily);
                    CUtils.saveStringData(this, CGlobalVariables.staticHoroscopeDaily, staticHoroscopeDaily);

                    String staticHoroscopeMonthly = container.getString(CGlobalVariables.staticHoroscopeMonthly);
                    CUtils.saveStringData(this, CGlobalVariables.staticHoroscopeMonthly, staticHoroscopeMonthly);

                    //Added for notification in multi language by Ankit on 13/8/2019
                    String staticHoroscopeMonthlyV2 = container.getString(CGlobalVariables.staticHoroscopeMonthlyV2);
                    CUtils.saveStringData(this, CGlobalVariables.staticHoroscopeMonthlyV2, staticHoroscopeMonthlyV2);

                    String staticHoroscopeWeekly = container.getString(CGlobalVariables.staticHoroscopeWeekly);
                    CUtils.saveStringData(this, CGlobalVariables.staticHoroscopeWeekly, staticHoroscopeWeekly);

                    //Added for notification in multi language by Ankit on 13/8/2019
                    String staticHoroscopeWeeklyV2 = container.getString(CGlobalVariables.staticHoroscopeWeeklyV2);
                    CUtils.saveStringData(this, CGlobalVariables.staticHoroscopeWeeklyV2, staticHoroscopeWeeklyV2);


                    String staticHoroscopeWeeklyLove = container.getString(CGlobalVariables.staticHoroscopeWeeklyLove);
                    CUtils.saveStringData(this, CGlobalVariables.staticHoroscopeWeeklyLove, staticHoroscopeWeeklyLove);

                    //Added for notification in multi language by Ankit on 13/8/2019
                    String staticHoroscopeWeeklyLoveV2 = container.getString(CGlobalVariables.staticHoroscopeWeeklyLoveV2);
                    CUtils.saveStringData(this, CGlobalVariables.staticHoroscopeWeeklyLoveV2, staticHoroscopeWeeklyLoveV2);


                    String staticHoroscopeYearly = container.getString(CGlobalVariables.staticHoroscopeYearly);
                    CUtils.saveStringData(this, CGlobalVariables.staticHoroscopeYearly, staticHoroscopeYearly);

                    boolean paytmVisibilityForProduct = container.getBoolean(CGlobalVariables.paytmVisibilityForProduct);
                    CUtils.saveBooleanData(this, CGlobalVariables.paytmVisibilityForProduct, paytmVisibilityForProduct);

                    boolean ccavenueVisibilityForProducts = container.getBoolean(CGlobalVariables.ccavenueVisibilityForProducts);
                    CUtils.saveBooleanData(this, CGlobalVariables.ccavenueVisibilityForProducts, ccavenueVisibilityForProducts);

                    boolean razorPayVisibilityForProduct = container.getBoolean(CGlobalVariables.razorPayVisibilityForProduct);
                    CUtils.saveBooleanData(this, CGlobalVariables.razorPayVisibilityForProduct, razorPayVisibilityForProduct);

                    boolean videoCardVisibilityForHoroscope = container.getBoolean(CGlobalVariables.videoCardVisibilityForHoroscope);
                    CUtils.saveBooleanData(this, CGlobalVariables.videoCardVisibilityForHoroscope, videoCardVisibilityForHoroscope);

                    boolean enableMonthlySubscription = container.getBoolean(CGlobalVariables.enableMonthlySubscription);
                    //boolean enableMonthlySubscription = true;
                    CUtils.saveBooleanData(this, CGlobalVariables.enableMonthlySubscription, enableMonthlySubscription);

                    //boolean isVartaEnabled = container.getBoolean(CGlobalVariables.IS_VARTA_ENABLED);
                    //CUtils.saveBooleanData(this, CGlobalVariables.IS_VARTA_ENABLED, isVartaEnabled);
                    //Log.e("GetTagManager","isVartaEnabled"+isVartaEnabled);
                    //CGlobalVariables.isShowSilverPlanTab = container.getBoolean(CGlobalVariables.SHOW_SILVER_PLAN_TAB);
                    //CGlobalVariables.isShowGoldPlanTab = container.getBoolean(CGlobalVariables.SHOW_GOLD_PLAN_TAB);
                    String youTubeApiKey = container.getString(CGlobalVariables.youtubeApiKey);
                    YoutubeKeySingleton.getInstance().setApiKey(youTubeApiKey);

                    boolean oldMagazineEnabled = container.getBoolean(CGlobalVariables.oldMagazineEnabled);
                    CUtils.saveBooleanData(this, CGlobalVariables.oldMagazineEnabled, oldMagazineEnabled);

                    boolean isShowConsultPremimumDialog = container.getBoolean(CGlobalVariables.isShowPremimumDialog);
                    CUtils.saveBooleanData(this, CGlobalVariables.isShowPremimumDialog, isShowConsultPremimumDialog);
                    boolean AstrosageKundliGoogleBottomAds = container.getBoolean(CGlobalVariables.AstrosageKundliGoogleBottomAds);
                    CUtils.saveBooleanData(this, CGlobalVariables.AstrosageKundliGoogleBottomAds, AstrosageKundliGoogleBottomAds);
                    boolean AstrosageKundliGoogleIntertitialAds = container.getBoolean(CGlobalVariables.AstrosageKundliGoogleIntertitialAds);
                    CUtils.saveBooleanData(this, CGlobalVariables.AstrosageKundliGoogleIntertitialAds, AstrosageKundliGoogleIntertitialAds);

                    boolean ccavenueVisibilityForVarta = container.getBoolean(com.ojassoft.astrosage.varta.utils.CGlobalVariables.ccavenueVisibilityForVartaWallet);
                    CUtils.saveBooleanData(this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.ccavenueVisibilityForVartaWallet, ccavenueVisibilityForVarta);
                    // Whether the intent flow for Varta wallet should be visible or not
                    boolean intentFlowVisibilityForVartaWallet = container.getBoolean(com.ojassoft.astrosage.varta.utils.CGlobalVariables.INTENT_FLOW_VISIBILITY_FOR_VARTA_WALLET);
                    CUtils.saveBooleanData(this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.INTENT_FLOW_VISIBILITY_FOR_VARTA_WALLET, intentFlowVisibilityForVartaWallet);

                    boolean isInsufficientDialogueHide = container.getBoolean(com.ojassoft.astrosage.varta.utils.CGlobalVariables.ISINSUFFICIENTDIALOGUEHIDE);
                    CUtils.saveBooleanData(this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.ISINSUFFICIENTDIALOGUEHIDE, isInsufficientDialogueHide);


                    boolean liveStreamingEnabledForAstrosage = container.getBoolean(com.ojassoft.astrosage.varta.utils.CGlobalVariables.liveStreamingEnabledForAstrosage);
                    CUtils.saveBooleanData(this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.liveStreamingEnabledForAstrosage, liveStreamingEnabledForAstrosage);
                    boolean liveAstroEnabledForAstrosageHomeScreen = container.getBoolean(com.ojassoft.astrosage.varta.utils.CGlobalVariables.liveStreamingEnabledForAstrosage);
                    CUtils.saveBooleanData(this, CGlobalVariables.liveAstrologerEnabledForAstrosageHomeScreen, liveAstroEnabledForAstrosageHomeScreen);

                    boolean isAgoraCallEnabled = container.getBoolean(com.ojassoft.astrosage.varta.utils.CGlobalVariables.IS_AGORA_CALL_ENABLED);
                    CUtils.saveBooleanData(this, CGlobalVariables.ISAGORACALLENABLED, isAgoraCallEnabled);

                    boolean displayLiveInHoroscope = container.getBoolean(com.ojassoft.astrosage.varta.utils.CGlobalVariables.DISPLAY_LIVE_IN_HOROSCOPE);
                    CUtils.saveBooleanData(this, CGlobalVariables.displayliveinhoroscope, displayLiveInHoroscope);

                    boolean isPhonePeSubscriptionEnabled = container.getBoolean(com.ojassoft.astrosage.varta.utils.CGlobalVariables.ISPHONEPESUBSCRIPTIONENABLED);
                    CUtils.saveBooleanData(this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.ISPHONEPESUBSCRIPTIONENABLED, false);


                    String defaultOrderPaymentMethod = container.getString(CGlobalVariables.DEFAULT_ORDER_PAYMENT_METHOD);
                    //Log.e("TagManagerData","defaultOrderPaymentMethod="+defaultOrderPaymentMethod);
                    CUtils.saveStringData(this, CGlobalVariables.DEFAULT_ORDER_PAYMENT_METHOD, defaultOrderPaymentMethod);

                    boolean showNewBrihatKundliPage = container.getBoolean(CGlobalVariables.SHOW_NEW_BRIHAT_KUNDLI_PAGE);
                    CUtils.saveBooleanData(this,CGlobalVariables.SHOW_NEW_BRIHAT_KUNDLI_PAGE,showNewBrihatKundliPage);

                    String BASE_URL_API2 = container.getString(CGlobalVariables.BASE_URL_API2);
                    String BASE_URL_APP2 = container.getString(CGlobalVariables.BASE_URL_APP2);
                    String BASE_URL_PDF = container.getString(CGlobalVariables.BASE_URL_PDF);
                    String BASE_URL_PANCHANG = container.getString(CGlobalVariables.BASE_URL_PANCHANG);
                    String BASE_URL_M_ASTROSAGE = container.getString(CGlobalVariables.BASE_URL_M_ASTROSAGE);
                    String BASE_URL_ASTROCAMP_COM = container.getString(CGlobalVariables.BASE_URL_ASTROCAMP_COM);
                    String BASE_URL_MARRIAGE = container.getString(CGlobalVariables.BASE_URL_MARRIAGE);
                    String BASE_URL_JAPP = container.getString(CGlobalVariables.BASE_URL_JAPP);
                    String BASE_URL_INAPPVERFY = container.getString(CGlobalVariables.BASE_URL_INAPPVERFY);
                    String BASE_URL_ASTROSAGE = container.getString(CGlobalVariables.BASE_URL_ASTROSAGE);
                    String BASE_URL_VARTA = container.getString(CGlobalVariables.BASE_URL_VARTA);
                    String BASE_URL_GCM = container.getString(CGlobalVariables.BASE_URL_GCM);
                    String BASE_URL_CHART = container.getString(CGlobalVariables.BASE_URL_CHART);
                    String BASE_URL_PLAY = container.getString(CGlobalVariables.BASE_URL_PLAY);


                    CUtils.saveStringData(this, CGlobalVariables.BASE_URL_API2, BASE_URL_API2);
                    CUtils.saveStringData(this, CGlobalVariables.BASE_URL_APP2, BASE_URL_APP2);
                    CUtils.saveStringData(this, CGlobalVariables.BASE_URL_PDF, BASE_URL_PDF);
                    CUtils.saveStringData(this, CGlobalVariables.BASE_URL_PANCHANG, BASE_URL_PANCHANG);
                    CUtils.saveStringData(this, CGlobalVariables.BASE_URL_M_ASTROSAGE, BASE_URL_M_ASTROSAGE);
                    CUtils.saveStringData(this, CGlobalVariables.BASE_URL_ASTROCAMP_COM, BASE_URL_ASTROCAMP_COM);
                    CUtils.saveStringData(this, CGlobalVariables.BASE_URL_MARRIAGE, BASE_URL_MARRIAGE);
                    CUtils.saveStringData(this, CGlobalVariables.BASE_URL_JAPP, BASE_URL_JAPP);
                    CUtils.saveStringData(this, CGlobalVariables.BASE_URL_INAPPVERFY, BASE_URL_INAPPVERFY);
                    CUtils.saveStringData(this, CGlobalVariables.BASE_URL_ASTROSAGE, BASE_URL_ASTROSAGE);
                    CUtils.saveStringData(this, CGlobalVariables.BASE_URL_VARTA, BASE_URL_VARTA);
                    CUtils.saveStringData(this, CGlobalVariables.BASE_URL_GCM, BASE_URL_GCM);
                    CUtils.saveStringData(this, CGlobalVariables.BASE_URL_CHART, BASE_URL_CHART);
                    CUtils.saveStringData(this, CGlobalVariables.BASE_URL_PLAY, BASE_URL_PLAY);

                    // enable/disable personalized notification
                    boolean isPersonalizedNotificationEnabled = container.getBoolean(CGlobalVariables.KEY_PERSONALIZED_NOTIFICATION_STATUS);
                    CUtils.saveBooleanData(this, CGlobalVariables.KEY_PERSONALIZED_NOTIFICATION_STATUS, isPersonalizedNotificationEnabled);
                    boolean showInternationalPayInINR = container.getBoolean(SHOW_INTERNATIONAL_PAY_IN_INR);
                    CUtils.saveBooleanData(this, SHOW_INTERNATIONAL_PAY_IN_INR, showInternationalPayInINR);
                    boolean newUserAiChat5Min = container.getBoolean(NEW_USER_AI_CHAT_5MIN);
                    CUtils.saveBooleanData(this, NEW_USER_AI_CHAT_5MIN, newUserAiChat5Min);
                    boolean isAICallShowInNotification = container.getBoolean(IS_AI_CALL_SHOW_IN_NOTIFICATION);
                    CUtils.saveBooleanData(this, IS_AI_CALL_SHOW_IN_NOTIFICATION, isAICallShowInNotification);
                    boolean isAICallShowInAiChat = container.getBoolean(IS_AI_CALL_SHOW_IN_AI_CHAT);
                    CUtils.saveBooleanData(this, IS_AI_CALL_SHOW_IN_AI_CHAT, isAICallShowInAiChat);

                    //AI call default speaker on,off TAG
                    boolean isDefaultSpeakerOn = container.getBoolean(CGlobalVariables.IS_AI_CALL_DEFAULT_SPEAKER_ON);
                    CUtils.saveBooleanData(this, CGlobalVariables.IS_AI_CALL_DEFAULT_SPEAKER_ON, isDefaultSpeakerOn);

                    //Intersticial on,off TAG
                    boolean isIntersticialShown = container.getBoolean(CGlobalVariables.IS_INTERSTICIAL_ENABLED);
                    CUtils.saveBooleanData(this, CGlobalVariables.IS_INTERSTICIAL_ENABLED, isIntersticialShown);

                    // Home Screen Popup on,off
                    boolean showHomeScreeonKundliAIPopup = container.getBoolean(CGlobalVariables.IS_HOME_SCREEN_KUNDLI_AI_POPUP_SHOWN);
                    CUtils.saveBooleanData(this, CGlobalVariables.IS_HOME_SCREEN_KUNDLI_AI_POPUP_SHOWN, showHomeScreeonKundliAIPopup);

                    // 5 rupee offer on Exit Screen
                    boolean isShow5RupeeOffer = container.getBoolean(CGlobalVariables.IS_SHOW_FIVE_RUPEE_OFFER);
                    CUtils.saveBooleanData(this, CGlobalVariables.IS_SHOW_FIVE_RUPEE_OFFER, isShow5RupeeOffer);

                    // 25 rupee special offer on Wallet Screen
                    boolean isShow25RupeeRechargeOffer = container.getBoolean(CGlobalVariables.IS_SHOW_25_RUPEE_RECHARGE_OFFER);
                    CUtils.saveBooleanData(this, CGlobalVariables.IS_SHOW_25_RUPEE_RECHARGE_OFFER, isShow25RupeeRechargeOffer);

                    //this key is used to decide weather I have to open in native app or web view
                    boolean shoppingcartviawebview = container.getBoolean(CGlobalVariables.SHOPPING_CART_VIA_WEBVIEW);
                    AstrosageKundliApplication.webViewIssue = AstrosageKundliApplication.webViewIssue+"\n"+"Tagmanger shoppingcartviawebview  ==>>"+shoppingcartviawebview;

                    CUtils.saveBooleanData(this, CGlobalVariables.SHOPPING_CART_VIA_WEBVIEW, shoppingcartviawebview);

                    //AI Pass Plan Daily Chat Count
                    String aiPassPrice = container.getString(CGlobalVariables.AI_Pass_Daily_Chat_Count);
                    CUtils.saveStringData(this, CGlobalVariables.AI_Pass_Daily_Chat_Count, aiPassPrice);

                    boolean isAutoSyncChart = container.getBoolean(CGlobalVariables.IS_AUTO_SYNC_CHART_ENABLED);
                    CUtils.saveBooleanData(this, CGlobalVariables.IS_AUTO_SYNC_CHART_ENABLED, isAutoSyncChart);

                    boolean showNewConnectPaidChatDialog = container.getBoolean(SHOW_NEW_CONNECT_CHAT_DIALOG);
                    CUtils.saveBooleanData(this, SHOW_NEW_CONNECT_CHAT_DIALOG, showNewConnectPaidChatDialog);

                    boolean getUserChatCategoryEnabled = container.getBoolean(GET_USER_CHAT_CATEGORY_ENABLED);
                    CUtils.saveBooleanData(this, GET_USER_CHAT_CATEGORY_ENABLED, getUserChatCategoryEnabled);

                    boolean isShowCredAndBhim = container.getBoolean(CGlobalVariables.ISSHOWCREDANDBHIM);
                    CUtils.saveBooleanData(this, CGlobalVariables.ISSHOWCREDANDBHIM, true);


                    boolean is_stripe_visible = container.getBoolean(IS_STRIPE_VISIBLE);
                    CUtils.saveBooleanData(this, IS_STRIPE_VISIBLE, is_stripe_visible);

                    boolean isPhonePeCheckoutVisible = container.getBoolean(CGlobalVariables.IS_PHONEPE_CHECKOUT_VISIBLE);
                    CUtils.saveBooleanData(this, CGlobalVariables.IS_PHONEPE_CHECKOUT_VISIBLE, isPhonePeCheckoutVisible);


                    String internationalDefaultOrderPaymentMethod = container.getString(INTERNATIONAL_DEFAULT_ORDER_PAYMENT_METHOD);
                    CUtils.saveStringData(this, INTERNATIONAL_DEFAULT_ORDER_PAYMENT_METHOD, internationalDefaultOrderPaymentMethod);

                    boolean isInsufficientSuggestedRechargeDialogShow = container.getBoolean(CGlobalVariables.IS_INSUFFICIENT_SUGGESTED_RECHARGE_DIALOG_SHOW);
                    CUtils.saveBooleanData(this, CGlobalVariables.IS_INSUFFICIENT_SUGGESTED_RECHARGE_DIALOG_SHOW, isInsufficientSuggestedRechargeDialogShow);

                    boolean showNewStyleNotification = container.getBoolean(CGlobalVariables.SHOW_NEW_STYLE_NOTIFICATION);
                    CUtils.saveBooleanData(this, CGlobalVariables.SHOW_NEW_STYLE_NOTIFICATION, showNewStyleNotification);

                    //Log.d("GetTagManager","end");
                }
            }
            //Log.d("TestLog", "getDataFromGTMMainCointainer2");
        } catch (Exception ex) {
            Log.e("GetTagManager", "getDataFromGTMMainCointainer3"+ex);
        }
    }

}

