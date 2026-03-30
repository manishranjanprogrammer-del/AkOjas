package com.ojassoft.astrosage.utils;

import android.view.View;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.misc.PersonalizedCategoryENUM;

import java.util.ArrayList;
import java.util.Arrays;

public class CGlobalVariables {

    public static final String AI_BASE_URL = "https://ai.astrosage.com";
    public static final String KUNDLI_AI_BASE_URL = "https://ai2.astrosage.com";
    public static final String KUNDLI_AI_TEST_BASE_URL = "https://aitest.astrosage.com";
    public static final String PDF_DOWNLOAD_COUNT_KEY = "pdf_download_count_key";
    public static final String IS_SUBSCRIPTION_ERROR = "subscription_error";
    public static final String SERVICE_LIST_CACHING_DATE_KEY = "service_list_caching_date_key";
    public static final String UPGRADE_AFTER_TEN_CHART_DIALOG_IS_SHOWN = "upgrade_after_ten_chart_dialog_is_shown";
    public static final String SOURCE_FROM_HOME_SCREEN_POPUP = "source_from_home_screen_popup";
    public static final String KUNDLI_AI_FREE_TRIAL_BUTTON_CLICK = "kundli_ai_free_trial_button_click";
    public static final String KUNDLI_AI_FREE_TRIAL_FOR_3_MONTH_BUTTON_CLICK = "kundli_ai_free_trial_for_3_months_button_click";
    public static final String KUNDLI_AI_FREE_TRIAL__AFTER_TEN_CHART_BUTTON_CLICK = "kundli_ai_free_trial_after_ten_charts_button_click";
    public static final String KUNDLI_AI_FREE_TRIAL_UPGRADE_BUTTON_CLICK = "kundli_ai_free_trial_upgrade_button_click";
    public static final String AI_HOROSCOPE_SCREEN_OPEN = "ai_horoscope_screen_open";
    public static final String CUSTOMER_BILLING_ID = "customer_billing_address_id";
    public static final int REQUEST_CODE_BILLING_ADDRESS = 897;

    public static String BASE_URL_API2 = "BASE_URL_API2";
    public static String BASE_URL_APP2 = "BASE_URL_APP2";
    public static String BASE_URL_PDF = "BASE_URL_PDF";
    public static String BASE_URL_PANCHANG = "BASE_URL_PANCHANG";
    public static String BASE_URL_M_ASTROSAGE = "BASE_URL_M_ASTROSAGE";
    public static String BASE_URL_ASTROCAMP_COM = "BASE_URL_ASTROCAMP_COM";
    public static String BASE_URL_MARRIAGE = "BASE_URL_MARRIAGE";
    public static String BASE_URL_JAPP = "BASE_URL_JAPP";
    public static String BASE_URL_INAPPVERFY = "BASE_URL_INAPPVERFY";
    public static String BASE_URL_ASTROSAGE = "BASE_URL_ASTROSAGE";
    public static String BASE_URL_VARTA = "BASE_URL_VARTA";
    public static String BASE_URL_GCM = "BASE_URL_GCM";
    public static String BASE_URL_CHART = "BASE_URL_CHART";
    public static String BASE_URL_PLAY = "BASE_URL_PLAY";
    public static String BASE_URL_K_ASTROSAGE = "BASE_URL_K_ASTROSAGE";
    public static String BASE_URL_DHRUV = "BASE_URL_DHRUV";


    public static String API2_BASE_URL = CUtils.getDomainName(BASE_URL_API2, "https://api2.astrosage.com/");
    public static String APP2_BASE_URL = CUtils.getDomainName(BASE_URL_APP2, "https://app2.astrosage.com/");
    public static String PDF_BASE_URL = CUtils.getDomainName(BASE_URL_PDF, "https://pdf.astrosage.com/");
    public static String PANCHANG_BASE_URL = CUtils.getDomainName(BASE_URL_PANCHANG, "https://panchang.astrosage.com/");
    public static String hostName = CUtils.getDomainName(BASE_URL_M_ASTROSAGE, "https://m.astrosage.com");
    public static String ASTROCAMP_BASE_URL = CUtils.getDomainName(BASE_URL_ASTROCAMP_COM, "https://astrocamp.com/");
    public static String MARRIAGE_BASE_URL = CUtils.getDomainName(BASE_URL_MARRIAGE, "https://marriage.astrosage.com/");
    public static String JAPI_BASE_URL = CUtils.getDomainName(BASE_URL_JAPP, "https://japp.astrosage.com/");
    public static String INAPPVERFY_BASE_URL = CUtils.getDomainName(BASE_URL_INAPPVERFY, "https://inappverify.appspot.com/");
    public static String ASTROSAGE_BASE_URL = CUtils.getDomainName(BASE_URL_ASTROSAGE, "https://www.astrosage.com/");
    public static String PLAY_URL = CUtils.getDomainName(BASE_URL_PLAY, "https://play.google.com");
    public static String openShareChartDeepLinks = CUtils.getDomainName(BASE_URL_K_ASTROSAGE, "https://k.astrosage.com");
    public static String CHART_BASE_URL = CUtils.getDomainName(BASE_URL_CHART, "https://akxml.astrosage.com/");
    //public static String CHART_BASE_URL = CUtils.getDomainName(BASE_URL_CHART, "https://6-dot-astrosagexml.appspot.com/");
    public static String GCM_BASE_URL = CUtils.getDomainName(BASE_URL_GCM, "https://gcm2.astrosage.com/");
    public static String VARTA_BASE_URL = CUtils.getDomainName(BASE_URL_VARTA, "https://vartaapi.astrosage.com/"); //  talk  vartaapi ;
    public static String DHRUV_BASE_URL = CUtils.getDomainName(BASE_URL_DHRUV, "https://ascloud.astrosage.com/");

    public static String PAYTM_MID = "Ojasso36077880907527";


   /* public final static String API2_BASE_URL = "https://api2.astrosage.com/";
    public final static String APP2_BASE_URL = "https://app2.astrosage.com/";
    public static String PDF_BASE_URL = "https://pdf.astrosage.com/";
    public final static String PANCHANG_BASE_URL = "https://panchang.astrosage.com/";
    public static String hostName = "https://m.astrosage.com";
    public static String ASTROCAMP_BASE_URL = "https://astrocamp.com/";
    public static String MARRIAGE_BASE_URL = "https://marriage.astrosage.com/";
    public static String JAPI_BASE_URL = "https://japp.astrosage.com/";
    public static final String INAPPVERFY_BASE_URL = "https://inappverify.appspot.com/";
    public static String ASTROSAGE_BASE_URL = "https://www.astrosage.com/";*/

    //public static String GCM_BASE_URL = "https://gcm2.astrosage.com/";

    // public static String CHART_BASE_URL = "https://akxml.astrosage.com/";
   /* public static String PLAY_URL = "https://play.google.com";

    public static String openShareChartDeepLinks = "https://k.astrosage.com";*/

    public static String GET_ASTROLOGER_PROFILE_LISTING = API2_BASE_URL + "astrologers/astrologer-profile.asp";

    public static String MAKE_CHART_DEFAULT = API2_BASE_URL + "as/mark-chart-default.asp";
    public static String DHRUV_DIRECTOTY_LISTINGOTP_VERIFY_URL = DHRUV_BASE_URL + "dhruvapi/directory-verify-otp";
    public static String LISTING_REQUEST_URL = DHRUV_BASE_URL + "dhruvapi/directory-join-request";
    public static String SEARCH_PLACE_URL = DHRUV_BASE_URL + "dhruvapi/directory-search-place";
    public static String ASTROLOGER_INFO_EDIT_URL = DHRUV_BASE_URL + "dhruvapi/directory-info-edit";
    public static String DIRECTORY_HOME_URL = DHRUV_BASE_URL + "dhruvapi/directory-home";
    public static String VARTA_TERMS_URL = ASTROCAMP_BASE_URL + "astro/terms-and-conditions.asp";
    public static String BOOK_APPOINTMENT_URL = DHRUV_BASE_URL + "dhruv/appointment-details.jsp";
    public static String MY_APPOINTMENT_URL = DHRUV_BASE_URL + "dhruv/my-appointment.jsp";
    public static String GET_TOPUP_DETAILS_URL = API2_BASE_URL + "ac/astroshop/dhruv-topup-recharge-desc.asp";
    public static String GET_PLAN_DETAILS_URL = JAPI_BASE_URL + "user-plan-pdf-details";
    public static String SEND_JOIN_REQUEST = VARTA_BASE_URL + "join-request";
    public static String ASTROSAGE_TERMS_CONDITIONS_URL = ASTROSAGE_BASE_URL + "controls/i_terms.asp";
    public static String ASTROSAGE_DISCLAIMER_URL = ASTROSAGE_BASE_URL + "controls/i_disclaimer.asp";
    public static String ASTROSAGE_PRIVACY_POLICY_URL = ASTROSAGE_BASE_URL + "controls/i_privacy-policy.asp";
    public static String GCM_REGISTRATION_URL = GCM_BASE_URL + "GCM/GCMServices.aspx";
    public static String OPEN_CHART_URL = API2_BASE_URL + "as/astrosage-xml/openchart-xml-v4.asp";
    public static String CANCEL_CLOUD_PLAN_URL = API2_BASE_URL + "ac/cloud-plan-verification/cancel-cloud-plan.asp";
    public static String DUMP_DATA_CLOUD_PLAN_URL = API2_BASE_URL + "ac/cloud-plan-verification/dump-data-buy-cloud-plan-v2.asp";
    public static String VERIFYING_LOGIN = API2_BASE_URL + "as/userlogincheckV11.asp";
    public static String VERIFYING_LOGIN_FIRST_TIME_AFTER_PURCHASE_PLAN = API2_BASE_URL + "as/userlogincheckV10.asp";
    public static String REGISTRATION_USER_FIRST_TIME_AFTER_PURCHASE_PLAN = API2_BASE_URL + "as/usersignupV10.asp";
    public static String dailyRemediesURL = API2_BASE_URL + "as/horoscope/horoscope-rss-v6.asp";
    public static String tomorrowRemediesURL = API2_BASE_URL + "as/horoscope/horoscope-rss-tomorrow-v6.asp";
    public static String REGISTRATION_USER = API2_BASE_URL + "as/usersignupV7.asp";
    public static String REGISTRATION_USER_WITHOUT_PASSWORD = API2_BASE_URL + "as/usersignupV8.asp";
    public static String ONLINE_CHART_DELETE_URL = API2_BASE_URL + "as/astrosage-xml/deletechart-xml-v1.asp?";
    public static String ONLINE_CHART_SAVE_URL = API2_BASE_URL + "as/astrosage-xml/savechart-xml-v1.asp?";
    public static String dailyHoroscopeRssFeedURL = API2_BASE_URL + "as/horoscope/horoscope-rss-v5.asp";
    public static String dailyHoroscopeRssFeedURLinUnicodeHindi = API2_BASE_URL + "as/horoscope/horoscope-rss-v5.asp?Language=hi";
    public static String monthlyHoroscopeRssFeedURL = API2_BASE_URL + "as/horoscope/horoscope-prediction-rss-v10.asp?type=monthly";
    public static String monthlyHoroscopeRssFeedURLinUnicodeHindi = API2_BASE_URL + "as/horoscope/horoscope-prediction-rss-v10.asp?type=monthly&language=HI";
    public static String monthlyHoroscopeRssFeedURLinUnicodeTamil = API2_BASE_URL + "as/horoscope/horoscope-prediction-rss-v10.asp?type=monthly&language=TA";
    public static String monthlyHoroscopeRssFeedURLinUnicodeMarathi = API2_BASE_URL + "as/horoscope/horoscope-prediction-rss-v10.asp?type=monthly&language=MR";
    public static String monthlyHoroscopeRssFeedURLinUnicodeBengali = API2_BASE_URL + "as/horoscope/horoscope-prediction-rss-v10.asp?type=monthly&language=BN";
    public static String monthlyHoroscopeRssFeedURLinUnicodeKannad = API2_BASE_URL + "as/horoscope/horoscope-prediction-rss-v10.asp?type=monthly&language=KA";
    public static String monthlyHoroscopeRssFeedURLinUnicodeTelgu = API2_BASE_URL + "as/horoscope/horoscope-prediction-rss-v10.asp?type=monthly&language=TE";
    public static String monthlyHoroscopeRssFeedURLinUnicodeGujarati = API2_BASE_URL + "as/horoscope/horoscope-prediction-rss-v10.asp?type=monthly&language=GU";
    public static String monthlyHoroscopeRssFeedURLinUnicodeMalayalam = API2_BASE_URL + "as/horoscope/horoscope-prediction-rss-v10.asp?type=monthly&language=ML";
    public static String monthlyHoroscopeRssFeedURLinUnicodeASAMMESE = API2_BASE_URL + "as/horoscope/horoscope-prediction-rss-v10.asp?type=monthly&language=AS";
    public static String monthlyHoroscopeRssFeedURLinUnicodeODIA = API2_BASE_URL + "as/horoscope/horoscope-prediction-rss-v10.asp?type=monthly&language=OR";

    public static final String UPDATE_STATUS_PRODUCT_RAZOR_PAY = API2_BASE_URL + "ac/astroshop/product-razorpay-paymentupdate.asp";
    public static String yearlyHoroscopeRssFeedURLEnglish = API2_BASE_URL + "as/horoscope/horoscope-prediction-rss-v9.asp?type=yearly&language=&RequestedYear";
    public static String yearlyHoroscopeRssFeedURLHindi = API2_BASE_URL + "as/horoscope/horoscope-prediction-rss-v9.asp?type=yearly&language=hindi&RequestedYear";
    public static String yearlyHoroscopeRssFeedURLTamil = API2_BASE_URL + "as/horoscope/horoscope-prediction-rss-v9.asp?type=yearly&language=tamil&RequestedYear";
    public static String yearlyHoroscopeRssFeedURLBangla = API2_BASE_URL + "as/horoscope/horoscope-prediction-rss-v9.asp?type=yearly&language=bengali&RequestedYear";
    public static String yearlyHoroscopeRssFeedURLMarathi = API2_BASE_URL + "as/horoscope/horoscope-prediction-rss-v9.asp?type=yearly&language=marathi&RequestedYear";
    public static String yearlyHoroscopeRssFeedURLTelugu = API2_BASE_URL + "as/horoscope/horoscope-prediction-rss-v9.asp?type=yearly&language=telugu&RequestedYear";
    public static String yearlyHoroscopeRssFeedURLKannad = API2_BASE_URL + "as/horoscope/horoscope-prediction-rss-v9.asp?type=yearly&language=kannada&RequestedYear";
    public static String yearlyHoroscopeRssFeedURLGujrati = API2_BASE_URL + "as/horoscope/horoscope-prediction-rss-v9.asp?type=yearly&language=gujarati&RequestedYear";
    public static String yearlyHoroscopeRssFeedURLMalayalam = API2_BASE_URL + "as/horoscope/horoscope-prediction-rss-v9.asp?type=yearly&language=malayalam&RequestedYear";
    public static String yearlyHoroscopeRssFeedURLASAMMESE = API2_BASE_URL + "as/horoscope/horoscope-prediction-rss-v9.asp?type=yearly&language=assammese&RequestedYear";
    public static String yearlyHoroscopeRssFeedURLOdia = API2_BASE_URL + "as/horoscope/horoscope-prediction-rss-v9.asp?type=yearly&language=odia&RequestedYear";

    public static String weeklyHoroscopeRssFeedURL = API2_BASE_URL + "as/horoscope/horoscope-prediction-rss-v10.asp?type=weekly";
    public static String weeklyHoroscopeRssFeedURLinUnicodeHindi = API2_BASE_URL + "as/horoscope/horoscope-prediction-rss-v10.asp?type=weekly&language=HI";
    public static String weeklyHoroscopeRssFeedURLinUnicodeTamil = API2_BASE_URL + "as/horoscope/horoscope-prediction-rss-v10.asp?type=weekly&language=TA";
    public static String weeklyHoroscopeRssFeedURLinUnicodeMarathi = API2_BASE_URL + "as/horoscope/horoscope-prediction-rss-v10.asp?type=weekly&language=MR";
    public static String weeklyHoroscopeRssFeedURLinUnicodeBengali = API2_BASE_URL + "as/horoscope/horoscope-prediction-rss-v10.asp?type=weekly&language=BN";
    public static String weeklyHoroscopeRssFeedURLinUnicodeKannad = API2_BASE_URL + "as/horoscope/horoscope-prediction-rss-v10.asp?type=weekly&language=KA";
    public static String weeklyHoroscopeRssFeedURLinUnicodeTelgu = API2_BASE_URL + "as/horoscope/horoscope-prediction-rss-v10.asp?type=weekly&language=TE";
    public static String weeklyHoroscopeRssFeedURLinUnicodeGujarati = API2_BASE_URL + "as/horoscope/horoscope-prediction-rss-v10.asp?type=weekly&language=GU";
    public static String weeklyHoroscopeRssFeedURLinUnicodeMalayalam = API2_BASE_URL + "as/horoscope/horoscope-prediction-rss-v10.asp?type=weekly&language=ML";
    public static String weeklyHoroscopeRssFeedURLinUnicodeOdia = API2_BASE_URL + "as/horoscope/horoscope-prediction-rss-v10.asp?type=weekly&language=OR";
    public static String weeklyHoroscopeRssFeedURLinUnicodeAssammese = API2_BASE_URL + "as/horoscope/horoscope-prediction-rss-v10.asp?type=weekly&language=AS";

    public static String weeklyLoveHoroscopeRssFeedURL = API2_BASE_URL + "as/horoscope/horoscope-prediction-rss-v10.asp?type=weeklylove";
    public static String weeklyLoveHoroscopeRssFeedURLinUnicodeHindi = API2_BASE_URL + "as/horoscope/horoscope-prediction-rss-v10.asp?type=weeklylove&language=HI";
    public static String weeklyLoveHoroscopeRssFeedURLinUnicodeTamil = API2_BASE_URL + "as/horoscope/horoscope-prediction-rss-v10.asp?type=weeklylove&language=TA";
    public static String weeklyLoveHoroscopeRssFeedURLinUnicodeMarathi = API2_BASE_URL + "as/horoscope/horoscope-prediction-rss-v10.asp?type=weeklylove&language=MR";
    public static String weeklyLoveHoroscopeRssFeedURLinUnicodeBengali = API2_BASE_URL + "as/horoscope/horoscope-prediction-rss-v10.asp?type=weeklylove&language=BN";
    public static String weeklyLoveHoroscopeRssFeedURLinUnicodeKannad = API2_BASE_URL + "as/horoscope/horoscope-prediction-rss-v10.asp?type=weeklylove&language=KA";
    public static String weeklyLoveHoroscopeRssFeedURLinUnicodeTelgu = API2_BASE_URL + "as/horoscope/horoscope-prediction-rss-v10.asp?type=weeklylove&language=TE";
    public static String weeklyLoveHoroscopeRssFeedURLinUnicodeGujarati = API2_BASE_URL + "as/horoscope/horoscope-prediction-rss-v10.asp?type=weeklylove&language=GU";
    public static String weeklyLoveHoroscopeRssFeedURLinUnicodeMalayalam = API2_BASE_URL + "as/horoscope/horoscope-prediction-rss-v10.asp?type=weeklylove&language=ML";
    public static String weeklyLoveHoroscopeRssFeedURLinUnicodeOdia = API2_BASE_URL + "as/horoscope/horoscope-prediction-rss-v10.asp?type=weeklylove&language=OR";
    public static String weeklyLoveHoroscopeRssFeedURLinUnicodeAssammese = API2_BASE_URL + "as/horoscope/horoscope-prediction-rss-v10.asp?type=weeklylove&language=AS";

    public static String dailyHoroscopeRssFeedURLinUnicodeTamil = API2_BASE_URL + "as/horoscope/horoscope-rss-v5.asp?Language=ta";
    public static String dailyHoroscopeTomorrowRssFeedURLinUnicodeHindi = API2_BASE_URL + "as/horoscope/horoscope-rss-tomorrow-v5.asp?day=1&language=hi";
    public static String dailyHoroscopeTomorrowRssFeedURL = API2_BASE_URL + "as/horoscope/horoscope-rss-tomorrow-v5.asp?day=1";
    public static String dailyHoroscopeTomorrowRssFeedURLinUnicodeMarathi = API2_BASE_URL + "as/horoscope/horoscope-rss-tomorrow-v5.asp?day=1&language=mr";
    public static String dailyHoroscopeRssFeedURLinUnicodeMarathi = API2_BASE_URL + "as/horoscope/horoscope-rss-v5.asp?Language=mr";
    public final static String CHANGE_PASSWORD_USERNAME = API2_BASE_URL + "as/reset-password-v6.asp";
    public final static String GET_BRANDING_DETAIL_URL = JAPI_BASE_URL + "user-branding-details";
    //public final static String GET_BRANDING_DETAIL_URL ="https://japp2.astrosage.com/user-branding-details";

    //public final static String CHANGE_PASSWORD_USERNAME = "http://192.168.1.177/as/reset-password-v6.asp";

    public static final String CHANGE_PASSWORD_URL = API2_BASE_URL + "as/reset-password-v5.asp";
    public static final String GET_FREE_QUESTION_AT_USER_FIRST_INSTALL_URL = API2_BASE_URL + "ac/astrologerchat/reward-free-question-v1.asp";
    public static String dailyHoroscopeTomorrowRssFeedURLinUnicodeGujarati = API2_BASE_URL + "as/horoscope/horoscope-rss-tomorrow-v5.asp?day=1&language=gu";
    public static String dailyHoroscopeTomorrowRssFeedURLinUnicodekannad = API2_BASE_URL + "as/horoscope/horoscope-rss-tomorrow-v5.asp?day=1&language=ka";
    public static String dailyHoroscopeRssFeedURLinUnicodekannad = API2_BASE_URL + "as/horoscope/horoscope-rss-v5.asp?Language=ka";
    public static String dailyHoroscopeTomorrowRssFeedURLinUnicodeBengali = API2_BASE_URL + "as/horoscope/horoscope-rss-tomorrow-v5.asp?day=1&language=bn";
    public static String dailyHoroscopeRssFeedURLinUnicodeBengali = API2_BASE_URL + "as/horoscope/horoscope-rss-v5.asp?Language=bn";
    public static String dailyHoroscopeTomorrowRssFeedURLinUnicodeTelgu = API2_BASE_URL + "as/horoscope/horoscope-rss-tomorrow-v5.asp?day=1&language=te";
    public static String dailyHoroscopeRssFeedURLinUnicodeTelgu = API2_BASE_URL + "as/horoscope/horoscope-rss-v5.asp?Language=te";
    public static String dailyHoroscopeTomorrowRssFeedURLinUnicodeMalayalam = API2_BASE_URL + "as/horoscope/horoscope-rss-tomorrow-v5.asp?day=1&language=ml";
    public static String dailyHoroscopeRssFeedURLinUnicodeMalayalam = API2_BASE_URL + "as/horoscope/horoscope-rss-v5.asp?Language=ml";
  public static String dailyHoroscopeTomorrowRssFeedURLinUnicodeOdia = API2_BASE_URL + "as/horoscope/horoscope-rss-tomorrow-v5.asp?day=1&language=or";
    public static String dailyHoroscopeRssFeedURLinUnicodeOdia = API2_BASE_URL + "as/horoscope/horoscope-rss-v5.asp?Language=or";
  public static String dailyHoroscopeTomorrowRssFeedURLinUnicodeAssammese = API2_BASE_URL + "as/horoscope/horoscope-rss-tomorrow-v5.asp?day=1&language=as";
    public static String dailyHoroscopeRssFeedURLinUnicodeAssammese = API2_BASE_URL + "as/horoscope/horoscope-rss-v5.asp?Language=as";
    public static String astroShopLogInsLive = API2_BASE_URL + "ac/astroshop/register-user-v3.asp";
    public static String genrateOrder = API2_BASE_URL + "ac/astroshop/product-order-v8.asp";
    public static String sendPayStatus = API2_BASE_URL + "ac/astroshop/product-order-paymentupdate-v2.asp";
    public static String astroshopCehequeDdUrl = API2_BASE_URL + "ac/astroshop/cheque-details.asp";
    public static String forgetPasswordUrl = API2_BASE_URL + "as/astrosage-xml/forget-password-v1.asp";
    public static String astroShoppincodecheckApi = API2_BASE_URL + "ac/astroshop/COD-pincode-check.asp";
    public static String astrologerLive = API2_BASE_URL + "ac/astroshop/astrologer-list.asp";
    public static String astroShopServiceList = API2_BASE_URL + "ac/astroshop/service-list-v12.asp";
    public static String astroShopHistory = API2_BASE_URL + "ac/astroshop/products-order-history-v1.asp";
    public static String astroShopSaveAddress = API2_BASE_URL + "ac/astroshop/saveuserAddress-v1.asp";
    public static String genrateServiceOrder = API2_BASE_URL + "ac/astroshop/service-order-v1.asp";
    public static String postservicepaystatus = API2_BASE_URL + "ac/astroshop/service-ccavenue-paymentupdate.asp";
    public static String astroShopServiceAskaQuestion = API2_BASE_URL + "ac/astroshop/service-list-customized-v7.asp?";
    public final static String numerologyApiUrl = API2_BASE_URL + "as/numerology-calculator.asp";
    public static String dailyHoroscopeRssFeedURLinUnicodeGujarati = API2_BASE_URL + "as/horoscope/horoscope-rss-v5.asp?Language=gu";
    public static final String LEARN_ASTROLOGY = API2_BASE_URL + "as/learnastrology/video-details.asp?";
    public static final String GET_VIDEO_DESCRIPTION = API2_BASE_URL + "as/learnastrology/videos-description.asp";
    public static String genrateOrderForCart = API2_BASE_URL + "ac/astroshop/product-cart-order-v7.asp";
    public static final String CUSTOM_ADDS = API2_BASE_URL + "ac/astroshop/all-banner-image-urls-v2.asp";
    public static final String AVAILABLEQUESTION = API2_BASE_URL + "ac/astrologerchat/get-user-question-detailsv3.asp";
    public static final String CHAT_HISTORY = API2_BASE_URL + "ac/astrologerchat/register-chat-historyv4.asp";
    public static final String SENDQUESTION = API2_BASE_URL + "ac/astrologerchat/api-update-order-v1.asp";
    public static final String SENDANSWERRATING = API2_BASE_URL + "ac/astrologerchat/update-answer-rating.asp";
    public static final String GENRATE_ORDER_ASKQUE = API2_BASE_URL + "ac/astroshop/service-order-paytm-v6.asp";
    public static final String UPDATE_PAY_STATUS_ASKQUE = API2_BASE_URL + "ac/astroshop/service-paytm-paymentupdate-with-verification-v2.asp";
    public static final String GET_ASTROCHAT_ORDERID_PAYTM = API2_BASE_URL + "ac/astrologerchat/api-update-order-paytmv6.asp";
    public static final String UPDATE_RAZORPAY_STATUS_CHAT = API2_BASE_URL + "ac/astrologerchat/order-razorpay-paymentupdate-v2.asp";
    public static final String UPDATE_PAY_STATUS_CHAT = API2_BASE_URL + "ac/astrologerchat/order-paytm-paymentupdate-v3.asp";
    public static final String UPDATE_STATUS_RAZOR_PAY = API2_BASE_URL + "ac/astroshop/service-razorpay-paymentupdate-v2.asp";
    public static final String PAYTM_EMAIL_PDF = API2_BASE_URL + "ac/astroshop/service-bhpdf-deliver-paytm.asp";
    public static final String RAZORPAY_EMAIL_PDF = API2_BASE_URL + "ac/astroshop/service-bhpdf-deliver-razorpay.asp";
    public static final String POST_PARTNER_ID = API2_BASE_URL + "ac/astroshop/partnerid-session.asp";
    public static String astroShopItemsLive = API2_BASE_URL + "ac/astroshop/product-listv12.asp";
    public static String astroShopcountryListLive = API2_BASE_URL + "ac/astroshop/country-shipping-price-v7.asp";
    //public static String astroShopcountryListLive = "https://api3.astrosage.com/ac/payment/test-country-shipping-price-v7.asp";
    public static String bigHorscopeWebURl = API2_BASE_URL + "ac/astroshop/big-horoscope-v7.asp";
    public static String brihatHorscopeWebURl = API2_BASE_URL + "ac/astroshop/Brihat-horoscope-service-product-desc-v3.asp";
    //public static String cogniAstroWebURl = "http://192.168.0.100:8080/" + "cogni-astro-service-desc-v1";
    public static String cogniAstroWebURl = JAPI_BASE_URL + "cogni-astro-service-desc-v1";

    public static String saveAndShareChart = API2_BASE_URL + "as/sharechart/saveandsharechart-v1.asp";
    public static String SAVE_NOTES_URL = API2_BASE_URL + "as/save-user-comment-v1.asp";
    public static final String astrosage_offer_details_url = API2_BASE_URL + "ac/astroshop/offer-description.asp";
    public static final String GET_FULL_DESCRIPTION = API2_BASE_URL + "ac/astroshop/product-detailsv2.asp";
    public static final String DELETE_ASK_A_QUES_CHAT_HISTORY = API2_BASE_URL + "ac/astrologerchat/deactivate-chat.asp";
    public static final String All_Astrologer_Url = API2_BASE_URL + "ac/astrologerchat/astrologer-list.asp";
    public static String SYNCCHARTURL = API2_BASE_URL + "as/chart-synch-v1.asp";
    public static String openShareChart = API2_BASE_URL + "as/sharechart/openchart.asp";
    public static String GET_ONLINE_KUNDLI_URL = API2_BASE_URL + "as/user-charts-v1.asp?";
    public static String dailyHoroscopeTomorrowRssFeedURLinUnicodeTamil = API2_BASE_URL + "as/horoscope/horoscope-rss-tomorrow-v5.asp?day=1&language=ta";
    public static String saveMatchingChart = API2_BASE_URL + "as/savechart-matchmaking.asp?";


   /* public final static String nameMatchingApiUrl = APP2_BASE_URL + "name-horoscope-matching.asp";
    public static String USER_DASHA_DETAILS = APP2_BASE_URL + "as/personalized-notifications/user-personlized-notifications-v2.asp";
    public static String MM_MATCH_MAKING_URL_NORTH = APP2_BASE_URL + "match-making-xml/MatchMakingReportV5.asp";// ADDED
    public static String TAJIK_VARSHAPHAL_URL = APP2_BASE_URL + "astrosage-xml/varshphal-xml.asp?";
    public static String URI_get_moon_sign_by_name = APP2_BASE_URL + "astrosage-xml/namrashi.asp?Type=1&name=";*/


    public final static String nameMatchingApiUrl = JAPI_BASE_URL + "name-horoscope-matching.asp";
    public static String USER_DASHA_DETAILS = JAPI_BASE_URL + "as/personalized-notifications/user-personlized-notifications-v2.asp";
    public static String MM_MATCH_MAKING_URL_NORTH = JAPI_BASE_URL + "match-making-xml/MatchMakingReportV5.asp";
    public static String TAJIK_VARSHAPHAL_URL = JAPI_BASE_URL + "astrosage-xml/varshphal-xml.asp";
    public static String URI_get_moon_sign_by_name = JAPI_BASE_URL + "astrosage-xml/namrashi.asp?Type=1&name=";

    public static String URI_DOWNLOAD_PDF = PDF_BASE_URL + "HindipdfNew.aspx?TypePdf=0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,17,0";
    public static String URI_DOWNLOAD_PDF_MATCHING = PDF_BASE_URL + "MatchMakingPdf.aspx?";


    public static String purnimaVratUrl = PANCHANG_BASE_URL + "festival/purnima?lang=";
    public static String ekadashiVratUrl = PANCHANG_BASE_URL + "festival/ekadashi?lang=";
    public static String pradoshVratUrl = PANCHANG_BASE_URL + "festival/pradosh-vrat?lang=";
    public static String masikShivratriVratUrl = PANCHANG_BASE_URL + "festival/shivratri/masik-shivratri?lang=";
    public static String sankashtiVratUrl = PANCHANG_BASE_URL + "festival/sankashti-chaturthi?lang=";
    public static String amavasyaVratUrl = PANCHANG_BASE_URL + "festival/amavasya?lang=";
    public static String sankrantiVratUrl = PANCHANG_BASE_URL + "festival/sankranti?lang=";
    public final static String panchakApiUrl = PANCHANG_BASE_URL + "muhurat/panchak";
    public final static String badhraApiUrl = PANCHANG_BASE_URL + "muhurat/bhadra";
    public final static String lagnaTableApiUrl = PANCHANG_BASE_URL + "panchang/lagna-table";
    public static String indianCalenderUrl = PANCHANG_BASE_URL + "calendars/indiancalendarapi?lang=";
    public static String hinduCalenderApiUrl = PANCHANG_BASE_URL + "calendars/hindu-calendarapi?lang=";
    public static String DAILY_FEST_URL = PANCHANG_BASE_URL + "festival/festivalapi?lang=";


    public static String URL_DAILY_PREDICTION = hostName + "/DailyPrediction.asp?isasktable=1&";
    public static String URL_MONTHLY_PREDICTION = hostName + "/MonthlyPredictions.asp?isasktable=1&";
    public static String URL_LIFE_PREDICTION = hostName + "/LifePredictions.asp?isasktable=1&";
    public static String URL_MANGAL_DOSH_PREDICTION = hostName + "/mangal-dosh.asp?isasktable=1&";
    public static String URL_SHANI_SADE_SATI_PREDICTION = hostName + "/shani-sade-sati.asp?isasktable=1&";
    public static String URL_KALSARPA_YOGA_PREDICTION = hostName + "/kalsarpa-yoga.asp?isasktable=1&";
    public static String URL_LALKITABDEBT_PREDICTION = hostName + "/lalkitab-debts.asp?isasktable=1&";
    public static String URL_LALKITAB_TEVA_PREDICTION = hostName + "/lalkitab-teva.asp?isasktable=1&";
    public static String URL_LALKITAB_DASHA_PREDICTION = hostName + "/lalkitab-dashanew.asp?isasktable=1&";
    public static String URL_LALKITAB_PLANET_PREDICTION = hostName + "/lalkitab-planet.asp?isasktable=1&";
    public static String URL_LALKITAB_HOUSE_PREDICTION = hostName + "/lalkitab-house.asp?isasktable=1&";

    public static String URL_LALKITAB_REMIDIES_PREDICTION = hostName + "/lalkitab-prediction.asp?isasktable=1&";
    public static String URL_ASCENDANT_PREDICTION = hostName + "/ascendant-prediction.asp?isasktable=1&";
    public static String URL_NAKSHATRA_REPORT = hostName + "/nakshatra-report.asp?isasktable=1&";
    public static String URL_GEMSTONES_REPORT = hostName + "/gemstones-report.asp?isasktable=1&";
    public static String URL_TRANSIT_TODAY = hostName + "/transit-today.asp?isasktable=1&";
    public static String URL_MAHADASHA_PHALA = hostName + "/mahadasha-phala.asp?isasktable=1&";
    public static String URL_PLANET_CONSIDERATION = hostName + "/planets-consideration.asp?isasktable=1&";
    public final static String KNOW_YOUR_MOON_SIGN_BY_DOB_URL = hostName + "/rashifal/moonsign.asp";
    public static String CREATE_SESSION = hostName + "/createsession.asp?currentchartid=&";
    public static String TAJIK_PREDICTION_URL = hostName + "/MonthlyPredictions.asp?isasktable=1&currentchartid=&";
    //tejinder added url for prediction module
    public static String BABY_NAME_URL = hostName + "/babyname.asp?isasktable=1&";
    public static String MOON_WESTREN_URL = hostName + "/moon-sign.asp?isasktable=1&";
    public static String MOON_URL = hostName + "/rasi-calculator.asp?isasktable=1&";
    //tejinder added url for basic module
    public static String SHADBALA_URL = hostName + "/ShadAndBhav.asp?isasktable=1&";
    public static String PRASTHARASHTAKVARGA_URL = hostName + "/Prastharashtakvarga.asp?isasktable=1&";
    public static String BHAV_MADHYA_URL = hostName + "/BhavMadhya.asp?isasktable=1&";
    public static String KP_CUSP_URL = hostName + "/KpCusp.asp?isasktable=1&";
    public static String SHODASHVARGA_URL = hostName + "/ShodashvargaTable.asp?isasktable=1&";
    public static String FRIENDSHIP_URL = hostName + "/friendship.asp?isasktable=1&";
    public static String CHARDASHA_URL = hostName + "/CharAntardasha.asp?isasktable=1&";
    public static String YOGINIDASHA_URL = hostName + "/YoginiDasha.asp?isasktable=1&";
    public static String JAMINIKARAKAMSA_URL = hostName + "/JaminiSystemKarakamsaSwamsa.asp?isasktable=1&";
    public static String AVKAHADA_CHAKRA_URL = hostName + "/avkahadachakra.asp?isasktable=1&";
    public static String PERSONAL_DETAIL_URL = hostName + "/person-details.asp?isasktable=1&";
    public static String GATHAK_FAVOURABLE_POINTS_URL = hostName + "/gathak-favourablepoints.asp?isasktable=1&";
    public final static String CALENDAR_URL = hostName + "/calendar2013/all-calendars.asp";
    public final static String PORUTHAM_URL_EN = hostName + "/tamil/default.asp?languageCode=0&utm_source=app&utm_medium=icon&utm_campaign=ak-home-icons";
    public final static String PORUTHAM_URL_HI = hostName + "/tamil/default.asp?languageCode=1&utm_source=app&utm_medium=icon&utm_campaign=ak-home-icons";
    public static String JADI_URL = hostName + "/jadi-report.asp?";
    public static String YANTRA_URL = hostName + "/yantra-report.asp?";
    public static String RUDRAKSHA_URL = hostName + "/rudraksha-report.asp?";
    public static String ISHT_DEVTA_URL = hostName + "/rudraksha-report.asp?";
    public final static String KNOW_YOUR_MOON_SIGN_BY_DOB_URL_HI = hostName + "/rashifal/moonsign.asp?userlang=1";

    public static String URL_MISC_Panchadhikari = hostName + "/panchadhikari.jsp?isasktable=1&";
    public static String URL_MISC_Yoga_Dosha_Summary = hostName + "/yoga-dosha.jsp?isasktable=1&";
    public static String URL_MISC_Remedies_Recommendations = hostName + "/recommendation.jsp?isasktable=1&";
    public static String URL_MISC_Karak = hostName + "/karak.jsp?isasktable=1&";
    public static String URL_MISC_Avastha = hostName + "/avastha.jsp?isasktable=1&";
    public static String URL_MISC_Navatara = hostName + "/navtara.jsp?isasktable=1&";
    public static String URL_MISC_Upgraha_Table = hostName + "/upgrah-table.jsp?isasktable=1&";
    public static String URL_MISC_Upgraha_Chart = hostName + "/upgrahchart.jsp?isasktable=1&";
    public static String URL_MISC_Arudha_Chart = hostName + "/arudha.jsp?isasktable=1&";
    public static String URL_MISC_Current_Ruling_Planets = hostName + "/current-ruling-planet.jsp?isasktable=1&";
    public static String URL_MISC_Shodashvarta_Table_Rashi = hostName + "/shodashvargatable.jsp?isasktable=1&";
    public static String URL_MISC_Shodashvarga_Bhava = hostName + "/shodashvargabhava.jsp?isasktable=1&";

    public final static String ASTRO_SHOP_URL = ASTROCAMP_BASE_URL + "mobile/?src=8";
    public final static String ASTRO_SHOP_SERVICES = ASTROCAMP_BASE_URL + "mobile/allservices.asp?src=8";
    public final static String ASTRO_SHOP_ASTROLOGER = ASTROCAMP_BASE_URL + "mobile/astrologers.asp?src=8";
    public final static String ASTRO_ASK_ASTROLOGER_URL = ASTROCAMP_BASE_URL + "mobile/astrologers.asp?src=8";

    public static String avenueRedirectUrl = MARRIAGE_BASE_URL + "ccavenue/ccavresponsehandler.aspx";
    public static String avenueRsaUrl = MARRIAGE_BASE_URL + "ccavenue/GetRSA.aspx";
    public final static String ASTROSAGE_MARRIAGE_URL_HI = MARRIAGE_BASE_URL + "Default.aspx?lang=1&utm_source=app&utm_medium=icon&utm_campaign=ak-home-icons";
    public final static String ASTROSAGE_MARRIAGE_URL_EN = MARRIAGE_BASE_URL + "Default.aspx?lang=2&utm_source=app&utm_medium=icon&utm_campaign=ak-home-icons";

    //public static final String GET_CHECKSUM_FOR_PAYTM_URL = "https://japp2.astrosage.com/paytmchecksumgenerator";
    public static final String GET_CHECKSUM_FOR_PAYTM_URL = JAPI_BASE_URL + "paytmchecksumgenerator";


    public static final String cricketTopicUrls = ASTROSAGE_BASE_URL + "virtual/notification/cricket";
    public static final String shareMarketTopicUrls = ASTROSAGE_BASE_URL + "virtual/notification/sharemarket";
    public static final String bollywoodTopicUrls = ASTROSAGE_BASE_URL + "virtual/notification/bollywood";
    public static final String politicsTopicUrls = ASTROSAGE_BASE_URL + "virtual/notification/politics";
    public static final String newMagazineTopicUrls = ASTROSAGE_BASE_URL + "virtual/notification/newmagazine";
    public static String ENGLISH_SAMPLE_PDF_URL = ASTROSAGE_BASE_URL + "pdf/poojasharmaE.pdf";
    public static String HINDI_SAMPLE_PDF_URL = ASTROSAGE_BASE_URL + "pdf/poojasharmaH.pdf";
    public static final String astrosage_offers_urls = ASTROSAGE_BASE_URL + "offer";
    public static String URL_NAVAGRAH_YANTRA_IMG = ASTROSAGE_BASE_URL + "numerology/images/yantras/";
    public static final String astrosage_shareapp_urls = ASTROSAGE_BASE_URL + "virtual/shareapp";
    public static final String OPEN_KUNDALI_VIRTUAL_URLS = ASTROSAGE_BASE_URL + "virtual/openkundali";
    public static final String OPEN_KUNDALI_VIRTUAL_URL = "http://www.astrosage.com/virtual/openkundali";


    public static final String defalutAdvertisementLink = PLAY_URL + "/store/apps/details?id=com.ojassoft.news&referrer=utm_source%3Dakad%26utm_medium%3Dbanner%26utm_campaign%3Dyohoapp";
    public static String OUR_OTHER_SOFTWARE_URL = "https://go.astrosage.com/ourapps";
    public static final String gPlusUrl = PLAY_URL + "/store/apps/details?id=com.ojassoft.astrosage";
    public static String SHARE_APP_URL = PLAY_URL + "/store/apps/details?id=com.ojassoft.astrosage";// GOOGLE

    public static String PAYTM_PAYMENT_URL = "https://secure.paytmpayments.com/theia/api/v1/showPaymentPage";
    public static String CALLBACK_URL = "https://secure.paytmpayments.com/theia/paytmCallback?ORDER_ID=";
    public static final String youtubeRssFeedUrl = "https://www.youtube.com/feeds/videos.xml?channel_id=UCHBM-lmpDCFzu8hpo8HjW-A";
    public static final String playListUrl = "https://www.googleapis.com/youtube/v3/playlistItems?&maxResults=10&part=snippet&key=#key&channelId=UCHBM-lmpDCFzu8hpo8HjW-A";
    public static final String youTubeSearchUrl = "https://www.googleapis.com/youtube/v3/search?&q=#&maxResults=5&part=snippet&key=#key&channelId=UCHBM-lmpDCFzu8hpo8HjW-A";
    public static final String youTubePopularUrl = "https://www.googleapis.com/youtube/v3/search?key=#key&channelId=UCHBM-lmpDCFzu8hpo8HjW-A&part=snippet,id&maxResults=10";
    public static final String youtubeUploadsId = "UUHBM-lmpDCFzu8hpo8HjW-A";
    public static final String astrosage_youtube_urls = "https://www.youtube.com/";
    public static final String astrosage_topics_httpsurl = "https://horoscope.astrosage.com/";
    public static final String astrology_topics_httpsurl = "https://astrology.astrosage.com/";
    public static final String jyotish_topics_httpsurl = "https://jyotish.astrosage.com/";
    public static String VARIFY_Payment_from_Paytm = "https://secure.paytm.in/oltp/HANDLER_INTERNAL/TXNSTATUS";
    public static String TRACK_SHIPWAY_ORDER = "https://shipway.in/api/getOrderShipmentDetails";
    public final static String youTubeBaseUrl = "https://img.youtube.com/vi/";
    public static final String buy_astrosage_urls = "https://buy.astrosage.com";
    public static final String astrocamp_service_url = "astrocamp.com/mobile/serviceorder.asp";
    public static final String astrocamp_product_url = "astrocamp.com/mobile/productlist.asp";
    public static final String Astrosage_Url_For_Twitter = "https://twitter.com/AstroSageSays";
    public static final String Astrosage_Url_For_Instagram = "https://www.instagram.com/astrosagedotcom/";
    public static final String Astrosage_Url_For_LinkedIn = "https://www.linkedin.com/showcase/astrosage/about/";
    //public static final String Astrosage_Url_For_Facebook = "https://www.facebook.com/astrology.horoscope";
    public static final String Astrosage_Url_For_Facebook = "https://www.facebook.com/astrology.horoscope";
    public static final String Astrosage_Url_For_GooglePlus = "https://plus.google.com/+astrosage";

    public static final String TERMS_AND_CONDITION_URL = "https://www.astrosage.com/terms-conditions.asp";
    public static final String PRIVACY_POLICY_URL = "https://www.astrosage.com/privacy-policy.asp";
    public static String CALCULATION_WEB_SERVICE_URL = CHART_BASE_URL + "freekphorary/chartxmlv2.asp";// ADDED
    //public static String CALCULATION_WEB_SERVICE_URL = "https://akxml2.astrosage.com/freekphorary/chartxmlv2.asp";// ADDED by Sandeep according to neha
    //public static String CALCULATION_WEB_SERVICE_URL = "https://6-dot-astrosagexml.appspot.com/freekphorary/chartxmlv2.asp";// ADDED by Sandeep according to neha
    public final static String regular = "Regular";
    public final static String medium = "Medium";

    public static String KEY_CLASS_NAME = "activityName";


    public final static int ASTROSHOP_SERVICES = 0;
    public final static int ASTROSHOP_ASTROLOGER = 1;
    public final static int MAXCARDSAVE = 5;

    public final static String SAVEPURCHASEDATA = "savePurchaseData";

    public final static String ASTROSHOP_CART = "astroshopCart";

    public final static String IS_PURCHASE = "is_purchase";

    // Cards Positioning
    public final static int dailyRashiphal = 1001;
    public final static int dailyPanchang = 1002;
    public final static int sunAndMoonCalculation = 1003;
    public final static int hinduMonthAndYear = 1004;
    public final static int auspiciousInauspiciousTimings = 1005;
    public final static int horaTable = 1006;
    public final static int chogadhiya = 1007;
    public final static int doGhati = 1008;
    public final static int rahuKaal = 1009;
    public final static int birthday = 1010;
    public final static int mahadasha = 1011;
    public final static int anterdasha = 1012;
    public final static int pratyantradasha = 1013;
    public final static int abhijit = 1014;
    public final static int questionAnswer = 1015;
    public final static int youtubeChannel = 1016;
    public final static int preQuestionAnswer = 1017;
    public final static int remediesCard = 1018;
    public final static int todaysRating = 1019;
    public final static int goodTime = 1020;
    public final static int daily_auspicious_muhurat = 1021;


    public final static String SHIPWAY_USERNAME = "punit";
    public final static String SHIPWAY_LICENSE = "87e0c0155c0894819b2e6ebd4e5bd7f0";

    // FINISH
    public final static int BASIC_PLAN_ID = 1;// ADDED BY BIJENDRA ON 05-06-15
    public final static int SILVER_PLAN_ID = 2;//Silver Yearly
    public final static int SILVER_PLAN_ID_4 = 4;//Silver Monthly
    public final static int SILVER_PLAN_ID_5 = 5;//Silver Yearly

    public final static int GOLD_PLAN_ID = 3;//Gold Yearly
    public final static int GOLD_PLAN_ID_6 = 6;//Gold Monthly
    public final static int GOLD_PLAN_ID_7 = 7;//Gold Yearly

    public final static int PLATINUM_PLAN_ID = 8;//Platinum Yearly
    public final static int PLATINUM_PLAN_ID_9 = 9;//Platinum Monthly
    public final static int PLATINUM_PLAN_ID_10 = 10;//Platinum Yearly
    public final static int KUNDLI_AI_PRIMIUM_PLAN_ID_11 = 11;//KUNDLI AI PRIMIUM PLAN

    public final static int MODULE_BASIC = 0;
    public final static int MODULE_DASA = 1;
    public final static int MODULE_PREDICTION = 2;
    public final static int MODULE_KP = 3;
    public final static int MODULE_SHODASHVARGA = 4;
    public final static int MODULE_LALKITAB = 5;
    public final static int MODULE_VARSHAPHAL = 6;
    public final static int MODULE_MISC = 7;
    public final static int MODULE_NUMEROLOGY = 8;
    public final static int MODULE_MATCHING = 7;
    public final static int MODULE_ASTROSAGE_ARTICLES = 8;
    public final static int MODULE_ASTRO_SHOP = 9;
    public final static int MODULE_ASK_OUR_ASTROLOGER = 10;
    public final static int MODULE_HOROSCOPE = 11;
    public final static int MODULE_HOROSCOPE_KNOW_YOUR_MOON_SIGN_BY_DOB = 12;
    public final static int MODULE_CALENDAR = 13;

    // ADDED BY BIJENDRA ON 18-02-15
    public final static int MODULE_PORUTHAM = 14;
    public final static int MODULE_ASTROSAGE_MARRIAGE = 15;

    // END
    // ADDED ON 14 SEP 2015
    public final static int MODULE_ASTROSAGE_PANCHANG = 16;
    public final static int MODULE_ASTROSAGE_HORA = 17;
    public final static int MODULE_ASTROSAGE_CHOGADIA = 18;
    public final static int MODULE_DO_GHATI_MUHURT = 19;
    public final static int MODULE_MY_CHART = 20;
    public final static int MODULE_RAHUKAAL = 31;
    public final static int MODULE_RASHIFAL = 32;
    public final static int MODULE_ASTROTV = 32;
    public final static int MODULE_PANCHAK = 181;
    public final static int MODULE_BHADRA = 182;
    public final static int MODULE_ASTROSAGE_DASHBOARD = 183;
    public final static int MODULE_MUHURAT = 184;
    public final static int MODULE_ASTROSAGE_NUMROLOGY = 185;
    public final static int MODULE_LAGNA = 186;
    public final static int MODULE_DAILY_NOTES = 187;
    public final static int MODULE_COGNIASTRO = 188;
    public final static int MODULE_DHRUV = 189;
    public final static int MODULE_BRIHAT_HOROSCOPE = 190;

    // ADDED FOR ASTRO SHOP
    public final static int MODULE_ASTROSHOP_GEMSTONE = 21;
    public final static int MODULE_ASTROSHOP_YANTRAS = 22;
    public final static int MODULE_ASTROSHOP_RUDRAKASHA = 23;
    public final static int MODULE_ASTROSHOP_MALA = 24;
    public final static int MODULE_ASTROSHOP_NAVAGRAH = 25;
    public final static int MODULE_ASTROSHOP_JADI = 26;
    public final static int MODULE_ASTROSHOP_SERVICE = 27;
    public final static int MODULE_ASTROSHOP_ASTROLOGER = 28;
    public final static int MODULE_ASTROSHOP_MISC = 29;

    public final static int MODULE_ASTROSAGE_ASKQUESTION = 29;
    public final static int MODULE_LEARN_ASTRO = 30;
    public final static int MODULE_ASTROSAGE_OFFERS = 31;
    public final static int MODULE_ASTROSAGE_HINDU_CALENDER = 32;
    public final static int MODULE_ASTROSAGE_YEARLY_VART = 33;
    public final static int MODULE_ASTROSAGE_INDIAN_CALENDER = 34;
    public final static int MODULE_ASTROSAGE_MONTH_VIEW = 35;
    public final static int MODULE_ASTROSAGE_INDIAN_CALENDAR = 36;
    public final static int MODULE_ABOUT_US = 37;

    public static final int MODULE_NUMERLOGY_CALCULATOR = 38;
    // public static final int MODULE_DAILY_NOTES = 39;
    public static final int MODULE_BHRIGOO = 39;
    public static final int MODULE_FREE_PDF = 40;

    //ADDED BY BIJENDRA ON 18-5-15
    public final static int SUB_MODULE_BASIC_CATEGORY = 0;
    public final static int SUB_MODULE_BASIC_LAGNA_CHART = 1;
    public final static int SUB_MODULE_BASIC_NAVAMSHA_CHART = 2;
    public final static int SUB_MODULE_BASIC_MOON_CHART = 3;
    public final static int SUB_MODULE_BASIC_CHALIT_CHART = 4;
    public final static int SUB_MODULE_BASIC_PLANETS = 5;
    public final static int SUB_MODULE_BASIC_PLANETS_SUB = 6;
    public final static int SUB_MODULE_BASIC_CHALIT_TABLE = 7;
    public final static int SUB_MODULE_BASIC_BIRTH_DETAILS = 8;
    public final static int SUB_MODULE_BASIC_PANCHANG = 9;
    public final static int SUB_MODULE_BASIC_ASHTAKVARGA = 10;
    //Tejinder Singh added these fields to add new webview page
    public final static int SUB_MODULE_BASIC_JAIMINI_KARAKAMSA = 11;
    public final static int SUB_MODULE_BASIC_JAIMINI_SWAMSA = 12;

    public final static int SUB_MODULE_BASIC_GOACHER = 13;
    public final static int SUB_MODULE_BASIC_SHADBALA = 14;
    public final static int SUB_MODULE_BASIC_PRASTHARAHTAKBARGA = 15;
    public final static int SUB_MODULE_BASIC_BHAV_MADHYA = 16;
    public final static int SUB_MODULE_BASIC_FRIENDSHIP = 17;
    public final static int SUB_MODULE_BASIC_PERSONAL_DETAIL = 18;
    public final static int SUB_MODULE_BASIC_AVAKAHADACHAKRA = 19;
    public final static int SUB_MODULE_BASIC_GATHAK_FAVOURABLE_POINTS = 20;

    public final static int PDF_DOWNLOAD_COUNT = 101;


    static public final String gemstone = "gemstone";
    static public final String yantra = "yantra";
    static public final String navagrah_yantra = "navagrah-yantra";
    static public final String rudraksha = "rudraksha";
    static public final String mala = "mala";
    static public final String jadi_tree_roots = "jadi-tree-roots";
    static public final String miscellaneous = "miscellaneous";
    static public final String gold_plan = "gold-plan";
    static public final String silver_plan = "silver-plan";

    static public final String static_horscope_measurment = "fromstatichorscope";
    static public final String dynamic_horscope_measurment = "fromdynamichorscope";
    //WE HAVE ADDED CATEGORY PREDICTION ,STATED INDEX FOR IT IS 0
    public static final int SUB_MODULE_PREDICTION_LIFE_PREDICTIONS = 1;
    public static final int SUB_MODULE_PREDICTION_MONTHLY_PREDICTIONS = 2;
    public static final int SUB_MODULE_PREDICTION_DAILY_PREDICTIONS = 3;
    public static final int SUB_MODULE_PREDICTION_MANGAL_DOSH = 4;
    public static final int SUB_MODULE_PREDICTION_SADE_SATI = 5;
    public static final int SUB_MODULE_PREDICTION_KAAL_SARP_DOSHA = 6;
    public static final int SUB_MODULE_PREDICTION_LALKITAB_DEBT = 7;
    public static final int SUB_MODULE_PREDICTION_LALKITAB_TEVA_TYPE = 8;
    public static final int SUB_MODULE_PREDICTION_LAL_KITAB_REMEDIES = 9;
    public static final int SUB_MODULE_PREDICTION_ASCENDANT_PREDICTION = 10;
    public static final int SUB_MODULE_PREDICTION_PLANET_CONSIDERATION = 11;
    public static final int SUB_MODULE_PREDICTION_GEMSTONE_REPORT = 12;
    public static final int SUB_MODULE_PREDICTION_TRANSIT_TODAY = 13;
    public static final int SUB_MODULE_PREDICTION_MAHADASHA_PHALA = 14;
    public static final int SUB_MODULE_PREDICTION_NAKSHATRA_REPORT = 15;
    public static final int SUB_MODULE_PREDICTION_VARSHPHAL = 16;
    public static final int SUB_MODULE_PREDICTION_BABYNAME = 17;
    public static final int SUB_MODULE_PREDICTION_MOONWESTERN = 18;
    public static final int SUB_MODULE_PREDICTION_MOON = 19;


    public final static int SUB_MODULE_PREDICTION_RUDRAKSHA = 20;
    public final static int SUB_MODULE_PREDICTION_JADI = 21;
    public final static int SUB_MODULE_PREDICTION_YANTRA = 22;
    public final static int SUB_MODULE_PREDICTION_ISHT_DEVTA = 23;
    public final static int SUB_MODULE_PREDICTION_CharAntardasha = 24;
    public final static int SUB_MODULE_PREDICTION_YoginiDasha = 25;
    //END
    public static final int SUB_MODULE_KP_CATEGORY = 0;
    public static final int SUB_MODULE_KP_KP_CHART = 1;
    public static final int SUB_MODULE_KP_RASHI_CHART = 2;
    public static final int SUB_MODULE_KP_PLANETS = 3;
    public static final int SUB_MODULE_KP_CUSPS = 4;
    public static final int SUB_MODULE_KP_PLANET_SIG = 5;
    public static final int SUB_MODULE_KP_HOUSE_SIG = 6;
    public static final int SUB_MODULE_KP_PLANET_SIG_VIEW_2 = 7;
    public static final int SUB_MODULE_KP_NAKSHATRA_NADI = 8;
    public static final int SUB_MODULE_KP_KCIL = 9;
    public static final int SUB_MODULE_KP_4_STEP = 10;
    public static final int SUB_MODULE_KP_CIL = 11;
    public static final int SUB_MODULE_KP_RULING_PLANET = 12;
    public static final int SUB_MODULE_KP_CURRENT_RULING_PLANET = 13;
    public static final int SUB_MODULE_KP_MISC = 14;
    public static final int SUB_MODULE_BASIC_KPCUSP = 15;

    //MODIFIED BY BIJENDRA ON 18-5-15
    public static final int SUB_MODULE_LALKITAB_CATEGORY = 0;
    public static final int SUB_MODULE_LALKITAB_LAGNA = 1;
    public static final int SUB_MODULE_LALKITAB_REMEDIES = 2;
    public static final int SUB_MODULE_LALKITAB_VARSHA_CHART = 3;
    public static final int SUB_MODULE_LALKITAB_DEBTS = 4;
    public static final int SUB_MODULE_LALKITAB_TEVA_TYPE = 5;
    public static final int SUB_MODULE_LALKITAB_DASHA_TYPE = 6;
    public static final int SUB_MODULE_LALKITAB_PLANET_TYPE = 7;
    public static final int SUB_MODULE_LALKITAB_HOUSE_TYPE = 8;
    //END
    // ADDED BY BIJENDRA ON 18-5-15
    public static final int SUB_MODULE_SHODASHVARGA_CATEGORY = 0,
            SUB_MODULE_SHODASHVARGA_LAGNA = 1,
            SUB_MODULE_SHODASHVARGA_HORA = 2,
            SUB_MODULE_SHODASHVARGA_DREKKANA = 3,
            SUB_MODULE_SHODASHVARGA_CHATURTHAMSHA = 4,
            SUB_MODULE_SHODASHVARGA_SAPTAMSHA = 5,
            SUB_MODULE_SHODASHVARGA_NAVAMSHA = 6,
            SUB_MODULE_SHODASHVARGA_DASHAMSHA = 7,
            SUB_MODULE_SHODASHVARGA_DWADASHMSHA = 8,
            SUB_MODULE_SHODASHVARGA_SHODASHAMSHA = 9,
            SUB_MODULE_SHODASHVARGA_VISHAMSHA = 10,
            SUB_MODULE_SHODASHVARGA_CHATURVISHAMSHA = 11,
            SUB_MODULE_SHODASHVARGA_SAPTAVISHMASHA = 12,
            SUB_MODULE_SHODASHVARGA_TRISAHAMSHA = 13,
            SUB_MODULE_SHODASHVARGA_KHAVEDAMSHA = 14,
            SUB_MODULE_SHODASHVARGA_AKSHAVEDAMSHA = 15,
            SUB_MODULE_SHODASHVARGA_SHASHTIMSHA = 16;
    public final static int SUB_MODULE_BASIC_SHODASHVARGATABLE = 17;
    // END

    public static final int SUB_MODULE_TAJIK_VARSHPHAL_CATEGORY = 0,
            SUB_MODULE_TAJIK_VARSHPHAL_CHART = 1,
            SUB_MODULE_TAJIK_VARSHPHAL_PLANETS = 2,
            SUB_MODULE_TAJIK_VARSHPHAL_PREDICTIONS = 3,
            SUB_MODULE_TAJIK_VARSHPHAL_Panchadhikari = 4;

    public final static int SUB_MODULE_MISC_Panchadhikari = 11111111,
            SUB_MODULE_MISC_Yoga_Dosha_Summary = 1,
            SUB_MODULE_MISC_Remedies_Recommendations = 2,
            SUB_MODULE_MISC_Karak = 3,
            SUB_MODULE_MISC_Avastha = 4,
            SUB_MODULE_MISC_Navatara = 5,
            SUB_MODULE_MISC_Upgraha_Table = 6,
            SUB_MODULE_MISC_Upgraha_Chart = 7,
            SUB_MODULE_MISC_Arudha_Chart = 8,
            SUB_MODULE_MISC_Current_Ruling_Planets = 11,
            SUB_MODULE_MISC_Shodashvarta_Table_Rashi = 9,
            SUB_MODULE_MISC_Shodashvarga_Bhava = 10;


    public final static String PANCHANG_TYPE_KEY = "PanchangType";
    public final static String MODULE_TYPE_KEY = "ModuleType";
    public final static String SUB_MODULE_TYPE_KEY = "SubModuleType";
    public final static String SUB_MODULE_SCREEN_NAME = "SubModuleScreenName";
    public final static String IS_NORTH_INDIAN = "IsNorthIndian";
    public final static String LANGUAGE_CODE = "LANGUAGE_CODE";
    public final static String DONT_SHOW_UR_NE_PL = "Dont_show_ur_ne_pl";
    public final static String AYANAMSHA_KEY = "Ayanamsha";
    public final static String PLACE_BEAN_KEY = "Place_ben_key";
    public final static String PAYMENTDONE_KEY_FOR_ASTRO_SERVICES_ISPAYMENTDONE = "payment_key_for_service_ispayment";
    public final static String PAYMENTDONE_KEY_FOR_ASTRO_SERVICES_JSONOBJ = "payment_key_for_service_obj";
    public final static String PAYMENTDONE_KEY_FOR_ASTRO_SERVICES_CHATID = "payment_key_for_service_chat_id";
    public final static String KEY_FOR_ASTRO_SERVICES_ASKAQUESTION = "key_for__service_ask_a_question";


    public enum enuShodashvarga {
        SHODASHVARGA_LAGNA, SHODASHVARGA_HORA, SHODASHVARGA_DREKKANA, SHODASHVARGA_CHATURTHAMSHA, SHODASHVARGA_SAPTAMAMSHA, SHODASHVARGA_NAVAMSHA, SHODASHVARGA_DASHAMAMSHA, SHODASHVARGA_DWADASHAMAMSHA, SHODASHVARGA_SHODASHAMSHA, SHODASHVARGA_VIMSHAMSHA, SHODASHVARGA_CHATURVIMSHAMSHA, SHODASHVARGA_SAPTAVIMSHAMSHA, SHODASHVARGA_TRIMSHAMSHA, SHODASHVARGA_KHAVEDAMSHA, SHODASHVARGA_AKSHVEDAMSHA, SHODASHVARGA_SHASHTIAMSHA, SHODASHVARGA_TABLE
    }

    /**********
     * Calculation Constants
     *********/

    public final static int ASCEDENT = 0;
    public final static int KPCUSP1 = 0;
    public final static int KPCUSP2 = 1;
    public final static int KPCUSP3 = 2;
    public final static int KPCUSP4 = 3;
    public final static int KPCUSP5 = 4;
    public final static int KPCUSP6 = 5;
    public final static int KPCUSP7 = 6;
    public final static int KPCUSP8 = 7;
    public final static int KPCUSP9 = 8;
    public final static int KPCUSP10 = 9;
    public final static int KPCUSP11 = 10;
    public final static int KPCUSP12 = 11;

    public final static int SUN_SIG = 0;
    public final static int Moon_SIG = 1;
    public final static int MARS_SIG = 2;
    public final static int MERCURY_SIG = 3;
    public final static int JUPITER_SIG = 4;
    public final static int VENUS_SIG = 5;
    public final static int SATURN_SIG = 6;
    public final static int RAHU_SIG = 7;
    public final static int KETU_SIG = 8;

    public final static int LAGNA = 0;
    public final static int HORA = 1;
    public final static int DREKKANA = 2;
    public final static int CHATURTHAMSHA = 3;
    public final static int SAPTAMAMSHA = 4;
    public final static int NAVAMSHA = 5;
    public final static int DASHAMAMSHA = 6;
    public final static int DWADASHAMAMSHA = 7;
    public final static int SHODASHAMSHA = 8;
    public final static int VIMSHAMSHA = 9;
    public final static int CHATURVIMSHAMSHA = 10;
    public final static int SAPTAVIMSHAMSHA = 11;
    public final static int TRIMSHAMSHA = 12;
    public final static int KHAVEDAMSHA = 13;
    public final static int AKSHVEDAMSHA = 14;
    public final static int SHASTIAMSHA = 15;


    public final static int SUN_ASTAK = 0;
    public final static int Moon_ASTAK = 1;
    public final static int MARS_ASTAK = 2;
    public final static int MERCURY_ASTAK = 3;
    public final static int JUPITER_ASTAK = 4;
    public final static int VENUS_ASTAK = 5;
    public final static int SATURN_ASTAK = 6;

    public static final int ENGLISH = 0;
    public static final int HINDI = 1;
    public static final int TAMIL = 2;// ADDED BY BIJENDRA 0N 03-07-15
    public static final int MARATHI = 9;// ADDED BY SHELENDRA 0N 24-08-15
    public static final int BANGALI = 6;// ADDED BY SHELENDRA 0N 23-12-15
    public static final int KANNADA = 4;
    public static final int TELUGU = 5;
    public static final int MALAYALAM = 8;
    public static final int GUJARATI = 7;
    public static final int URDU = 10; //--URDU not implemeted in app but given u for ref
    public static final int Nepali = 11; //--Nepali not implemeted in app but given u for ref\
    public static final int ODIA = 13;
    public static final int ASAMMESSE = 14;
    public static final int PUNJABHI = 15;//--punjabi not implemeted in app but given u for ref
    public static final int SPANISH = 16;
    public static final int CHINESE = 17;
    public static final int JAPANESE = 18;
    public static final int PORTUGUESE = 19;
    public static final int GERMAN = 20;
    public static final int ITALIAN = 21;
    public static final int FRENCH = 22;

    public enum enuKundliType {
        NORTH, SOUTH, EAST
    }

    ;

    public static String UnicodeSpace = "\u0020";// \u2254"


    public static String[] ROMAN_NUMBERS = {"I", "II", "III", "IV", "V", "VI",
            "VII", "VIII", "IX", "X", "XI", "XII"};

    public static int HOUSE_1 = 0;
    public static int HOUSE_4 = 3;
    public static int HOUSE_7 = 6;
    public static int HOUSE_8 = 7;
    public static int HOUSE_12 = 11;

    public static int LalKitabVarshaPhalTable[][] = {
            {1, 9, 10, 3, 5, 2, 11, 7, 6, 12, 4, 8},
            {4, 1, 12, 9, 3, 7, 5, 6, 2, 8, 10, 11},
            {9, 4, 1, 2, 8, 3, 10, 5, 7, 11, 12, 6},
            {3, 8, 4, 1, 10, 9, 6, 11, 5, 7, 2, 12},
            {11, 3, 8, 4, 1, 5, 9, 2, 12, 6, 7, 10},
            {5, 12, 3, 8, 4, 11, 2, 9, 1, 10, 6, 7},
            {7, 6, 9, 5, 12, 4, 1, 10, 11, 2, 8, 3},
            {2, 7, 6, 12, 9, 10, 3, 1, 8, 5, 11, 4},
            {12, 2, 7, 6, 11, 1, 8, 4, 10, 3, 5, 9},
            {10, 11, 2, 7, 6, 12, 4, 8, 3, 1, 9, 5},
            {8, 5, 11, 10, 7, 6, 12, 3, 9, 4, 1, 2},
            {6, 10, 5, 11, 2, 8, 7, 12, 4, 9, 3, 1},
            {1, 5, 10, 8, 11, 6, 7, 2, 12, 3, 9, 4},
            {4, 1, 3, 2, 5, 7, 8, 11, 6, 12, 10, 9},
            {9, 4, 1, 6, 8, 5, 2, 7, 11, 10, 12, 3},
            {3, 9, 4, 1, 12, 8, 6, 5, 2, 7, 11, 10},
            {11, 3, 9, 4, 1, 10, 5, 6, 7, 8, 2, 12},
            {5, 11, 6, 9, 4, 1, 12, 8, 10, 2, 3, 7},
            {7, 10, 11, 3, 9, 4, 1, 12, 8, 5, 6, 2},
            {2, 7, 5, 12, 3, 9, 10, 1, 4, 6, 8, 11},
            {12, 2, 8, 5, 10, 3, 9, 4, 1, 11, 7, 6},
            {10, 12, 2, 7, 6, 11, 3, 9, 5, 1, 4, 8},
            {8, 6, 12, 10, 7, 2, 11, 3, 9, 4, 1, 5},
            {6, 8, 7, 11, 2, 12, 4, 10, 3, 9, 5, 1},
            {1, 6, 10, 3, 2, 8, 7, 4, 11, 5, 12, 9},
            {4, 1, 3, 8, 6, 7, 2, 11, 12, 9, 5, 10},
            {9, 4, 1, 5, 10, 11, 12, 7, 6, 8, 2, 3},
            {3, 9, 4, 1, 11, 5, 6, 8, 7, 2, 10, 12},
            {11, 3, 9, 4, 1, 6, 8, 2, 10, 12, 7, 5},
            {5, 11, 8, 9, 4, 1, 3, 12, 2, 10, 6, 7},
            {7, 5, 11, 12, 9, 4, 1, 10, 8, 6, 3, 2},
            {2, 7, 5, 11, 3, 12, 10, 6, 4, 1, 9, 8},
            {12, 2, 6, 10, 8, 3, 9, 1, 5, 7, 4, 11},
            {10, 12, 2, 7, 5, 9, 11, 3, 1, 4, 8, 6},
            {8, 10, 12, 6, 7, 2, 4, 5, 9, 3, 11, 1},
            {6, 8, 7, 2, 12, 10, 5, 9, 3, 11, 1, 4},
            {1, 3, 10, 6, 9, 12, 7, 5, 11, 2, 4, 8},
            {4, 1, 3, 8, 6, 5, 2, 7, 12, 10, 11, 9},
            {9, 4, 1, 12, 8, 2, 10, 11, 6, 3, 5, 7},
            {3, 9, 4, 1, 11, 8, 6, 12, 2, 5, 7, 10},
            {11, 7, 9, 4, 1, 6, 8, 2, 10, 12, 3, 5},
            {5, 11, 8, 9, 12, 1, 3, 4, 7, 6, 10, 2},
            {7, 5, 11, 2, 3, 4, 1, 10, 8, 9, 12, 6},
            {2, 10, 5, 3, 4, 9, 12, 8, 1, 7, 6, 11},
            {12, 2, 6, 5, 10, 7, 9, 1, 3, 11, 8, 4},
            {10, 12, 2, 7, 5, 3, 11, 6, 4, 8, 9, 1},
            {8, 6, 12, 10, 7, 11, 4, 9, 5, 1, 2, 3},
            {6, 8, 7, 11, 2, 10, 5, 3, 9, 4, 1, 12},
            {1, 7, 10, 6, 12, 2, 8, 4, 11, 9, 3, 5},
            {4, 1, 8, 3, 6, 12, 5, 11, 2, 7, 10, 9},
            {9, 4, 1, 2, 8, 3, 12, 6, 7, 10, 5, 11},
            {3, 9, 4, 1, 11, 7, 2, 12, 5, 8, 6, 10},
            {11, 10, 7, 4, 1, 6, 3, 9, 12, 5, 8, 2},
            {5, 11, 3, 9, 4, 1, 6, 2, 10, 12, 7, 8},
            {7, 5, 11, 8, 3, 9, 1, 10, 6, 4, 2, 12},
            {2, 3, 5, 11, 9, 4, 10, 1, 8, 6, 12, 7},
            {12, 2, 6, 5, 10, 8, 9, 7, 4, 11, 1, 3},
            {10, 12, 2, 7, 5, 11, 4, 8, 3, 1, 9, 6},
            {8, 6, 12, 10, 7, 5, 11, 3, 9, 2, 4, 1},
            {6, 8, 9, 12, 2, 10, 7, 5, 1, 3, 11, 4},
            {1, 11, 10, 6, 12, 2, 4, 7, 8, 9, 5, 3},
            {4, 1, 6, 8, 3, 12, 2, 10, 9, 5, 7, 11},
            {9, 4, 1, 2, 8, 6, 12, 11, 7, 3, 10, 5},
            {3, 9, 4, 1, 6, 8, 7, 12, 5, 2, 11, 10},
            {11, 2, 9, 4, 1, 5, 8, 3, 10, 12, 6, 7},
            {5, 10, 3, 9, 2, 1, 6, 8, 11, 7, 12, 4},
            {7, 5, 11, 3, 10, 4, 1, 9, 12, 6, 8, 2},
            {2, 3, 5, 11, 9, 7, 10, 1, 6, 8, 4, 12},
            {12, 8, 7, 5, 11, 3, 9, 4, 1, 10, 2, 6},
            {10, 12, 2, 7, 5, 11, 3, 6, 4, 1, 9, 8},
            {8, 6, 12, 10, 7, 9, 11, 5, 2, 4, 3, 1},
            {6, 7, 8, 12, 4, 10, 5, 2, 3, 11, 1, 9},
            {1, 4, 10, 6, 12, 11, 7, 8, 2, 5, 9, 3},
            {4, 2, 3, 8, 6, 12, 1, 11, 7, 10, 5, 9},
            {9, 10, 1, 3, 8, 6, 2, 7, 5, 4, 12, 11},
            {3, 9, 6, 1, 2, 8, 5, 12, 11, 7, 10, 4},
            {11, 3, 9, 4, 1, 2, 8, 10, 12, 6, 7, 5},
            {5, 11, 4, 9, 7, 1, 6, 2, 10, 12, 3, 8},
            {7, 5, 11, 2, 9, 4, 12, 6, 3, 1, 8, 10},
            {2, 8, 5, 11, 4, 7, 10, 3, 1, 9, 6, 12},
            {12, 1, 7, 5, 11, 10, 9, 4, 8, 3, 2, 6},
            {10, 12, 2, 7, 5, 3, 4, 9, 6, 8, 11, 1},
            {8, 6, 12, 10, 3, 5, 11, 1, 9, 2, 4, 7},
            {6, 7, 8, 12, 10, 9, 3, 5, 4, 11, 1, 2},
            {1, 3, 10, 6, 12, 2, 8, 11, 5, 4, 9, 7},
            {4, 1, 8, 3, 6, 12, 11, 2, 7, 9, 10, 5},
            {9, 4, 1, 7, 3, 8, 12, 5, 2, 6, 11, 10},
            {3, 9, 4, 1, 8, 10, 2, 7, 12, 5, 6, 11},
            {11, 10, 9, 4, 1, 6, 7, 12, 3, 8, 5, 2},
            {5, 11, 6, 9, 4, 1, 3, 8, 10, 2, 7, 12},
            {7, 5, 11, 2, 10, 4, 6, 9, 8, 3, 12, 1},
            {2, 7, 5, 11, 9, 3, 10, 4, 1, 12, 8, 6},
            {12, 8, 7, 5, 2, 11, 9, 1, 6, 10, 3, 4},
            {10, 12, 2, 8, 11, 5, 4, 6, 9, 7, 1, 3},
            {8, 6, 12, 10, 5, 7, 1, 3, 4, 11, 2, 9},
            {6, 2, 3, 12, 7, 9, 5, 10, 11, 1, 4, 8},
            {1, 9, 10, 6, 12, 2, 7, 5, 3, 4, 8, 11},
            {4, 1, 6, 8, 10, 12, 11, 2, 9, 7, 3, 5},
            {9, 4, 1, 2, 6, 8, 12, 11, 5, 3, 10, 7},
            {3, 10, 8, 1, 5, 7, 6, 12, 2, 9, 11, 4},
            {11, 3, 9, 4, 1, 6, 8, 10, 7, 5, 12, 2},
            {5, 11, 3, 9, 4, 1, 2, 6, 8, 12, 7, 10},
            {7, 5, 11, 3, 9, 4, 1, 8, 12, 10, 2, 6},
            {2, 7, 5, 11, 3, 9, 10, 1, 6, 8, 4, 12},
            {12, 2, 4, 5, 11, 3, 9, 7, 10, 6, 1, 8},
            {10, 12, 2, 7, 8, 5, 3, 9, 4, 11, 6, 1},
            {8, 6, 12, 10, 7, 11, 4, 3, 1, 2, 5, 9},
            {6, 8, 7, 12, 2, 10, 5, 4, 11, 1, 9, 3},
            {1, 9, 10, 6, 12, 2, 7, 11, 5, 3, 4, 8},
            {4, 1, 6, 8, 10, 12, 3, 5, 7, 2, 11, 9},
            {9, 4, 1, 2, 5, 8, 12, 10, 6, 7, 3, 11},
            {3, 10, 8, 9, 11, 7, 4, 1, 2, 12, 6, 5},
            {11, 3, 9, 4, 1, 6, 2, 7, 10, 5, 8, 12},
            {5, 11, 3, 1, 4, 10, 6, 8, 12, 9, 7, 2},
            {7, 5, 11, 3, 9, 4, 1, 12, 8, 10, 2, 6},
            {2, 7, 5, 11, 3, 9, 10, 6, 4, 8, 12, 1},
            {12, 2, 4, 5, 6, 1, 8, 9, 3, 11, 10, 7},
            {10, 12, 2, 7, 8, 11, 9, 3, 1, 6, 5, 4},
            {8, 6, 12, 10, 7, 5, 11, 2, 9, 4, 1, 3},
            {6, 8, 7, 12, 2, 3, 5, 4, 11, 1, 9, 10}};
    //Added by Amit
    public static String versionCode = "versioncode";
    public static String _unicodeSpace = "\u0020";

    public static String[] predcition_pageName_Array = {"LifePredictions.asp",
            "MonthlyPredictions.asp", "DailyPrediction.asp", "mangal-dosh.asp",
            "shani-sade-sati.asp", "kalsarpa-yoga.asp", "lalkitab-debts.asp",
            "lalkitab-teva.asp", "lalkitab-prediction.asp",
            "ascendant-prediction.asp", "planets-consideration.asp",
            "gemstones-report.asp", "transit-today.asp", "mahadasha-phala.asp",
            "nakshatra-report.asp"};
    public static int TOTAL_PLANETS_ON_NOT_SHOW_NEP_PLU_URA = 9;

    public static int GENDER_BOY = 0;
    public static int GENDER_GIRL = 1;
    public static int BOTH_GENDER = 2;
    public static String APP_USER_ID = "11000000000";

    /*
     * public static String LoginName=""; public static String LoginPwd="";
     * public static boolean isLogin=false;
     */
    public static final String APP_PREFS_ASTROSHOP_USER_ADDRSS = "useraddress_new";
    public static final String APP_PREFS_ASTROSHOP_USER_EMAIL = "user_email";

    public static final String APP_PREFS_NAME = "KundliPref_new";
    public static final String APP_PREFS_NAME_HELP = "KundliPref_Help";
    public static final String APP_PREFS_WIZARD = "KundliPref_Wizard";// ADDED
    public static final String APP_PREFS_NAME_FOR_SERVICE = "KundliPref_new_forservice";
    public static final String APP_PREFS_SAVED_VIDEOS = "SAVEDVIDEOS";
    public static final String PANCHANG_PREF = "panchang_pref";
    public static final String APP_PREFS_NAME_FREE_DHRUV = "FreeDhruvPref";

    // BY
    // BIJENDRA
    // ON
    // 24-04-15
    public static final String APP_PREFS_MainModule = "MainModule";
    public static final String APP_PREFS_SubModule = "SubModule";
    public static final String APP_PREFS_ChartStyle = "ChartStyle";
    public static final String APP_PREFS_MonthType = "MonthType";
    public static final String APP_PREFS_Plan = "planname";
    public static final String APP_PREFS_Plan_for_Popup = "popup";
    public static final String APP_PREFS_Plan_for_Shareapp = "shareapp";
    public static final String APP_PREFS_Plan_for_service = "plannameforservice";

    public static final String APP_PREFS_Ayanmasha = "Ayanmasha";
    public static final String APP_PREFS_NotShowPlUnNa = "NotShowPlUnNa";
    public static final String APP_PREFS_UserWantCustomDatePicker = "UserWantCustomDatePickerNewVersion";
    public static final String APP_PREFS_AppWizardScreenIndex = "AppWizardScreenIndex";
    public static final String APP_PREFS_User_Id = "UserId";
    public static final String APP_PREFS_SAVED_CARDS = "SAVEDCARDS";

    public static final String APP_PREFS_User_Password = "UserPassword";
    public static final String APP_PREFS_User_name = "UserName";
    public static final String APP_PREFS_IsLogin = "IsLogin";
    public static final String APP_PREFS_isautogenerated_password = "AutoGeneratedPassword";
    public static final String APP_PREFS_Is_auto_generated_pass = "AutoGeneratedPassword";
    public static final String APP_PREFS_NotShowLoginScreen = "doNotShowLoginScreen";
    public static final String APP_PREFS_NotShowUpgradePlanPopUp = "NotShowUpgradePlanPopUp";
    public static final String APP_PREFS_oldPreferencesCopied = "OldPreferencesHaveSavedInNewPreferences";
    public static final String APP_PREFS_allowedHindiSupportPopup = "allowedHindiSupportPopup";
    public static final String APP_PREFS_help = "0";
    public static final String APP_PREFS_KundliAI_help = "app_pref_kundli_ai_help";
    public static final String APP_PREFS_KundliAI_help_new = "app_pref_kundli_ai_help_new";
    public static final String APP_PREFS_Option_help = "0";
    public static final String APP_PRESS_Horoscope_want = "wantHoroscope";
    public static final String APP_PRESS_Moon_Sign = "Aries";
    public static final String APP_PREFS_IsUserINShipping = "IsShipping";

    // START old app pref file names
    public static final String MYKUNDLI_PREFS_NAME = "KundliPref";
    public static final String MYKUNDLI_LANGUAGE_PREFS_NAME = "KundliLanguagePref2";
    public static final String MYKUNDLI_PAY_PREF = "KundliPayPref";

    public static final String APP_PREFS_AppLanguage = "LT";

    public static final String SELECTED_PAY_OPTION = "selected_pay_option";

    public static final String PREF_USER_ID = "UserID";
    public static final String PREF_USERR_PWD = "UserPwd";
    public static final String PREF_USER_PURCHASE_PLAN = "PurchasePlan";
    public static final String PREF_USER_PLAN_PURCHASE_DATE = "PlanPurchaseDate";
    public static final String PREF_USER_PLAN_Expiry_DATE = "PlanExpiryDate";
    // End old app pref file names
    public static final int CHART_NORTH_STYLE = 0;
    public static final int CHART_SOUTH_STYLE = 1;
    public static final int CHART_EAST_STYLE = 2;

    public static final int MONTH_AMANTA = 0;
    public static final int MONTH_PURNIMANT = 1;

    public static final String SILVER_PLAN_VALUE_YEAR = "silver_year";
    public static final String GOLD_PLAN_VALUE_YEAR = "gold_year";
    public static final String SILVER_PLAN_VALUE_MONTH = "silver_month";
    public static final String GOLD_PLAN_VALUE_MONTH = "gold_month";
    public static final String PLATINUM_PLAN_VALUE_YEAR = "platinum_year";
    public static final String PLATINUM_PLAN_VALUE_MONTH = "platinum_month";
    public static final String PLATINUM_PLAN_VALUE_MONTH_OMF = "platinum_month_omf";
    public static final String PLATINUM_PLAN_VALUE_YEAR_OMF = "platinum_year_omf";
    public static final String KUNDLI_AI_PLAN_VALUE_MONTH = "kundli_ai_plan_month";

    public static final String ASK_QUESTION_PLAN_VALUE = "ask_a_question";

    public static final String BOOKMARK_COLLECTION_FILE_NAME = "BookMarkScreen_File";
    public static final String SCREEN_HISTORY_COLLECTION_FILE_NAME = "RecentScreen_File";
    public static final String SHOWDIALOGFORPERSONALKUNDALI = "DialogForPersonalKundali";

    public final static String SUBSCRIBE_RAHUKAAL = "subscriberahukal";

    public final static String SUBSCRIBE_PANCHANG = "subscribepanchang";


    public static final String ITEM_IN_SUBSCRIPTION = "subs";

    public static String myKundliDirectoryInSDCard = "MyKundliScreen";

    public enum enuFileType {
        IMAGE, TEXT
    }

    ;

    // public static String MY_AD_ID = "a151a47e2cd3142";
    public static String MY_AD_ID = "ca-app-pub-7494543079410386/3658074422";
    public static String MM_PREDICTION_VARNA = "VA";
    public static String MM_PREDICTION_VASYA = "VAS";
    public static String MM_PREDICTION_TARA = "TA";
    public static String MM_PREDICTION_YONI = "YON";
    public static String MM_PREDICTION_GANA = "GAN";
    public static String MM_PREDICTION_NADI = "NA";
    public static String MM_PREDICTION_MAITRI = "RAS";
    public static String MM_PREDICTION_BHAKUT = "BH";
    //public static String APP_EXIT_AD_ID = "ca-app-pub-7494543079410386/7345246024";// ADDED
    public static String APP_EXIT_AD_ID = "ca-app-pub-7494543079410386/7711216028";// Native Express Ad key
    // BY
    // BIJENDRA
    // ON
    // 01-JULY-2015

    // END
    public static int MM_ENGLISH = 0;
    public static int MM_HINDI = 1;

    public static String eol = System.getProperty("line.separator");

    public final static String APP_PACKAGE_NAME = "com.ojassoft.astrosage";

    public static final int SMALL_DEVICE_WIDTH = 380;

    public static final int SMALL_DEVICE_TOP = 10;
    public static final int SMALL_DEVICE_BOTTOM = 3;

    public static int TOP = 10;
    public static int BOTTOM = 13;

    public static final String outPutMasterActSavedBundleKey = "outPutMasterActSavedBundleKey";
    public static final String outPutMatchingMasterActSavedBundleKey = "outPutMatchingMasterActSavedBundleKey";

    public static final String ASTROSAGEPURCHASEPLANFORSERVICE = "astrosagePurchasePlanforservice";
    public static final String ASTROSAGEPURCHASEPLANFORSERVICEOBJECT = "astrosagePurchasePlanforserviceobject";
    public static final String ASTROASKAQUESTIONPRICE = "astroaskaquestionprice";

    public static final String ASTROASKAQUESTIONUSERPHONENUMBER = "astroaskaquestionuserphonenumber";
    public static final String ASTROASKAQUESTIONUSEREMAIL = "astroaskaquestionuseremail";


    public static final String ASTROSAGEPURCHASEPLAN = "astrosagePurchasePlan";
    public static final String ASTROSAGEPURCHASEPLANOBJECT = "astrosagePurchasePlanObject";
    public static String RATE_APP_URL = "market://details?id=com.ojassoft.astrosage";// GOOGLE


    public static String shareToFriendBody = "Try this amazing app on your Android phone- App Name: Kundli by AstroSage. Download URL:"
            + SHARE_APP_URL + "<" + SHARE_APP_URL;

    // END
    public static final int APP_MODULE_SCREEN = 1;
    public static final int HOME_INPUT_SCREEN = 2;
    public static final int Thanks_Product_Purchase_Screen = 3;
    public static String INTERNET_IS_NOT_WORKING = "Internet is not working";

    // ===============Google Analytics=====================
    public static String GOOGLE_ANALYTIC_NOTIFICATION_STATIC_RECEIVE = "STATIC_HOROSCOPE_RECEIVE";
    public static String GOOGLE_ANALYTIC_NOTIFICATION_STATIC_CLICK = "STATIC_HOROSCOPE_CLICK";


    public static String GOOGLE_ANALYTIC_NOTIFICATION_DYNAMIC_RECEIVE = "DYNAMIC_HOROSCOPE_RECEIVE";
    public static String GOOGLE_ANALYTIC_NOTIFICATION_DYNAMIC_CLICK = "DYNAMIC_HOROSCOPE_CLICK";

    public static String GOOGLE_ANALYTIC_HOROSCOPE_LABEL_DAILY = "Daily";
    public static String GOOGLE_ANALYTIC_HOROSCOPE_LABEL_WEEKLY = "Weekly";
    public static String GOOGLE_ANALYTIC_HOROSCOPE_LABEL_WEEKLY_LOVE = "Weekly_Love";
    public static String GOOGLE_ANALYTIC_HOROSCOPE_LABEL_MONTHLY = "Monthly";

    public final static String SAVEHOROSCOPECOUNT = "saveHoroscopeCount";


    public static String GOOGLE_ANALYTIC_SCREEN = "SCREEN";
    public static String GOOGLE_ANALYTIC_DOWNLOAD = "DOWNLOAD";

    public static String GOOGLE_ANALYTIC_LAUNCH = "LAUNCH";
    public static String GOOGLE_ANALYTIC_EVENT = "EVENT";
    public static String GOOGLE_ANALYTIC_LANGUAGE_CONFIG_SCREEN = "_LANGUAGE CONFIG SCREEN";
    public static String GOOGLE_ANALYTIC_EVENT_FOR_ASTROSHOP_VIEW = "VIEW";
    public static String GOOGLE_ANALYTIC_EVENT_FOR_ASTROSHOP_BUY = "BUY";
    public static String GOOGLE_ANALYTIC_EVENT_FOR_ASTROSHOP_VIEW_AND_CONTINUE = "VIEW AND CONTINUE";
    public static String GOOGLE_ANALYTIC_EVENT_FOR_ASTROSHOP_LOGIN = "LOGIN";
    public static String GOOGLE_ANALYTIC_EVENT_FOR_ASTROSHOP_SHIPPING = "SHIPPING";
    public static String GOOGLE_ANALYTIC_EVENT_FOR_ASTROSHOP_PAYMENT = "PAYMENT";
    public static String GOOGLE_ANALYTIC_INAPP_SEARCH = "In_App_Search";
    public static String GOOGLE_ANALYTIC_OUTPUTMASTER_SEARCH = "Output_Master_Search";

    public static String GOOGLE_ANALYTIC_LABEL_FOR_ASTROSHOP_PAYMENT_SUCCESS = "PRODUCT_PURCHASED";
    public static String GOOGLE_ANALYTIC_LABEL_FOR_ASTROSHOP_PAYMENT_FAILED = "PRODUCT_PURCHASE_FAILED";


    /* ====================================================== */
    public static String GOOGLE_ANALYTIC_ACTION_ASTROSAGE_LOGIN = "AstroSage Cloud login";
    public static String GOOGLE_ANALYTIC_ACTION_ASTROSAGE_LOGIN_SKIP = "AstroSage Cloud login skip";
    public static String GOOGLE_ANALYTIC_ACTION_ASTROSAGE_LOGIN_CANCEL = "AstroSage Cloud login Cancel";
    public static String GOOGLE_ANALYTIC_ACTION_ASTROSAGE_LOGIN_FORGET_PWD = "AstroSage Cloud Forget password";
    public static String GOOGLE_ANALYTIC_ACTION_ASTROSAGE_CLOUD_REGISTRATION = "AstroSage Cloud Registration";
    public static String GOOGLE_ANALYTIC_ACTION_ASTROSAGE_CLOUD_KUNDLI_DELETE = "AstroSage Cloud Kundli delete";
    public static String GOOGLE_ANALYTIC_ACTION_ASTROSAGE_CLOUD_KUNDLI_SEARCH = "AstroSage Cloud Kundlisearch";
    public static String GOOGLE_ANALYTIC_ACTION_SHARE = "Share";
    public static String GOOGLE_ANALYTIC_ACTION_RATE_APP = "rate_app";
    public static String GOOGLE_ANALYTIC_ACTION_FEED_BACK = "Feed back";
    public static String GOOGLE_ANALYTIC_ACTION_CALL = "Call";
    public static String GOOGLE_ANALYTIC_ACTION_SHOW_KUNDLI = "Show Kundli";
    public static String GOOGLE_ANALYTIC_ACTION_CALCULATE_KUNDLI = "Calculate Kundli";
    public static String GOOGLE_ANALYTIC_ACTION_SEARCH_KUNDLI = "Search Kundli";
    public static String GOOGLE_ANALYTIC_ACTION_VIEW_KUNDLI = "View Kundli";
    public static String GOOGLE_ANALYTIC_ACTION_SHOW_PRDICTION = "Show_Prediction";
    public static String GOOGLE_ANALYTIC_ACTION_ASTRO_SHOP = "Astro Shop";
    public static String GOOGLE_ANALYTIC_ACTION_ASK_OUR_ASTROLOGER = "Ask Our Astrologer";
    public static String GOOGLE_ANALYTIC_ACTION_OUR_OTHER_PRODUCTS = "Our Other Products";
    public static String GOOGLE_ANALYTIC_DASHA = "Dasha";
    public static String GOOGLE_ANALYTIC_HORSCOPE = "Open_Horoscope";
    public static String GOOGLE_ANALYTIC_CALENDAR = "Open_Calendar";
    public static String GOOGLE_ANALYTIC_HINDU_CALENDAR = "Open_Hindu_Calendar";
    public static String GOOGLE_ANALYTIC_YEARLY_VART = "Open_Yearly_Vart";
    public static String GOOGLE_ANALYTIC_MONTH_VIEW = "Open_Month_View";
    //public static String GOOGLE_ANALYTIC_DAILY_NOTES = "Open_Daily_Notes";
    public static String GOOGLE_ANALYTIC_BHRIGOO = "Open_Bhrigoo";
    public static String GOOGLE_ANALYTIC_PORUTHAM = "Open_Porutham";
    public static String GOOGLE_ANALYTIC_INDIAN_CALENDAR = "Open_Indian_Calendar";
    public static String GOOGLE_ANALYTIC_ASTROTV = "Astrosage_tv";// ADDED BY
    // BIJENDRA
    // ON
    // 14-04-15
    public static String GOOGLE_ANALYTIC_NUMEROLOGY_CALCULATOR = "Numerology calculator";// ADDED
    public static String GOOGLE_ANALYTIC_FREE_PDF = "Free 50+ pages";// ADDED
    public static String GOOGLE_ANALYTIC_OCCULT_DIRECTORY = "occult_directory";// ADDED


    public static String GOOGLE_ANALYTIC_ASTROSAHE_MARRIAGE = "Open_AstrosagMarriage";// ADDED
    public static String GOOGLE_ANALYTIC_ASTROSAHE_ASKAQUESTION = "Open_AskaQuestion";// ADDED
    public static String GOOGLE_ANALYTIC_ASTROSAHE_LEARNASTROLOGY = "Open_LearnAstrology";// ADDED
    public static String GOOGLE_ANALYTIC_ASTROSAHE_ASKAQUESTION_MARRIAGE = "Open_AskaQuestion_marriage";
    public static String GOOGLE_ANALYTIC_TRACK_ORDER = "Track_Order";// ADDED

    // BY
    // BIJENDRA
    // ON
    // 14-04-15
    public static String signin_screen_from_output_activity = "Sign in screen";
    public static String signin_screen_from_output_activity_yes = "yes sign in";
    public static String signin_screen_from_output_activity_no = "not sign in";
    public static String GOOGLE_ANALYTIC_ACTION_SHARE_PREDICTION_ON_WHATSAPP = "Share Prediction on WhatsApp";// ADDED
    // BY
    // BIJENDRA
    // ON
    // 14-04-15
    public static String GOOGLE_ANALYTIC_PANCHANG = "Open_Panchang";// ADDED ON
    public static String GOOGLE_ANALYTIC_PANCHANG_FROM_CARD = "Open_Panchang_From_Card";
    // 14 SEP
    // 2015

    public static String GOOGLE_ANALYTIC_MYCHART = "Open_MYCHART";// ADDED ON 24
    // SEP
    // 2015


    //@Tejinder Singh Reports for googleAnalytics name
    public static final String GOOGLE_ANALYTIC_LIFE_REPORTS = "Open_LifeReport";

    public static final String GOOGLE_ANALYTIC_MONTHLY_REPORTS = "Open_MonthlyReport";
    public static final String GOOGLE_ANALYTIC_DAILY_REPORTS = "Open_DailyReport";
    public static final String GOOGLE_ANALYTIC_SADESATI_REPORTS = "Open_SadesatiReport";
    public static final String GOOGLE_ANALYTIC_ASCENDANT_PREDICTION = "Open_AscendantPrediction";
    public static final String GOOGLE_ANALYTIC_ANNUAL_PREDICTION = "Open_AnnualPrediction";
    public static final String GOOGLE_ANALYTIC_MANGAL_DOSH = "Open_MangalDosh";
    public static final String GOOGLE_ANALYTIC_KALSARPL_DOSH = "Open_KalSarpDosh";
    public static final String GOOGLE_ANALYTIC_MOON_SIGN = "Open_MoonSign";
    public static final String GOOGLE_ANALYTIC_LALKITAB_DEBT = "Open_LalKitabDebt";
    public static final String GOOGLE_ANALYTIC_LALKITAB_TEVA = "Open_LalKitabTeva";
    public static final String GOOGLE_ANALYTIC_BABY_NAME = "Open_Baby_Name";
    public static final String GOOGLE_ANALYTIC_LALKITAB_REMEDIES = "Open_LalKitabRemedies";
    public static final String GOOGLE_ANALYTIC_PLANET_CONSIDERATION = "Open_PlanetConsideration";
    public static final String GOOGLE_ANALYTIC_GEMSTONES_REPORT = "Open_Gemstones_Report";
    public static final String GOOGLE_ANALYTIC_TRANSIT_TODAY = "Open_TransitToday";
    public static final String GOOGLE_ANALYTIC_MAHADASHA_PHALA = "Open_MahadashaPhala";
    public static final String GOOGLE_ANALYTIC_NAKSHATRA_REPORT = "Open_NakshatraReport";

    public static final String GOOGLE_ANALYTIC_Yearly_Horoscope = "Open_Yearly_Horoscope";
    public static final String GOOGLE_ANALYTIC_Yearly_Jupiter_Transits = "Open_Yearly_Jupiter_Transits";
    public static final String GOOGLE_ANALYTIC_Yearly_Numerology = "Open_Yearly_Numerology";
    public static final String GOOGLE_ANALYTIC_Yearly_Love_Horoscope = "Open_Yearly_Love_Horoscope";
    public static final String GOOGLE_ANALYTIC_Yearly_Career_Horoscope = "Open_Yearly_Career_Horoscope";
    public static final String GOOGLE_ANALYTIC_Yearly_Chinese_Horoscope = "Open_Yearly_Chinese_Horoscope";
    public static final String GOOGLE_ANALYTIC_Yearly_Education_Horoscope = "Open_Yearly_Education_Horoscope";
    public static final String GOOGLE_ANALYTIC_Yearly_Finance_Horoscope = "Open_Yearly_Finance_Horoscope";
    public static final String GOOGLE_ANALYTIC_Yearly_Lalkitab_Horoscope = "Open_Yearly_Lalkitab_Horoscope";
    public static final String GOOGLE_ANALYTIC_Yearly_Calendar = "Open_Yearly_Calendar";
    public static final String GOOGLE_ANALYTIC_Yearly_Muhurat = "Open_Yearly_Muhurat";
    public static final String GOOGLE_ANALYTIC_Yearly_Sports = "Open_Yearly_Sports";
    public static final String GOOGLE_ANALYTIC_Yearly_Movies = "Open_Yearly_Movies";
    public static final String GOOGLE_ANALYTIC_Yearly_Elections = "Open_Yearly_Elections";
    public static final String GOOGLE_ANALYTIC_Yearly_IPL = "Open_Yearly_IPL";
    public static final String GOOGLE_ANALYTIC_Yearly_Saturn_Transits = "Open_Yearly_Saturn_Transits";
    public static final String GOOGLE_ANALYTIC_Yearly_Festivals = "Open_Yearly_Festivals";
    public static final String GOOGLE_ANALYTIC_Yearly_Grahan = "Open_Yearly_Grahan";

    public static final String GOOGLE_ANALYTIC_Yearly_Viah_Muhurat = "Open_Yearly_Viah_Muhurat";
    public static final String GOOGLE_ANALYTIC_Yearly_Mundan_Muhurat = "Open_Yearly_Mundan_Muhurat";
    public static final String GOOGLE_ANALYTIC_Yearly_Griha_Muhurat = "Open_Yearly_Griha_Muhurat";
    public static final String GOOGLE_ANALYTIC_Yearly_Namkaran_Muhurat = "Open_Yearly_Namkaran_Muhurat";
    public static final String GOOGLE_ANALYTIC_Yearly_Annaprashan_Muhurat = "Open_Yearly_Annaprashan_Muhurat";
    public static final String GOOGLE_ANALYTIC_Yearly_Karnavedha_Muhurat = "Open_Yearly_Karnavedha_Muhurat";
    public static final String GOOGLE_ANALYTIC_Yearly_Vidyarambh_Muhurat = "Open_Yearly_Vidyarambh_Muhurat";

    public static final String GOOGLE_ANALYTIC_Yearly_Ketu_Transit = "Open_Yearly_Ketu_Transit";
    public static final String GOOGLE_ANALYTIC_Yearly_Rahu_Transit = "Open_Yearly_Rahu_Transit";
    public static final String GOOGLE_ANALYTIC_Yearly_Lunar_Eclipse = "Open_Yearly_Lunar_Eclipse";
    public static final String GOOGLE_ANALYTIC_Yearly_Solar_Eclipse = "Open_Yearly_Solar_Eclipse";
    public static final String GOOGLE_ANALYTIC_Yearly_PlanetsInRetrograde = "Open_Yearly_PlanetsInRetrograde";
    public static final String GOOGLE_ANALYTIC_Yearly_MercuryRetrograde = "Open_Yearly_MercuryRetrograde";

    //Panchag section
    public static final String GOOGLE_ANALYTIC_PANCHANG_PANCHANG = "Open_NPANCHAG_PANCHANG";
    public static String GOOGLE_ANALYTIC_HORA = "Open_HORA";
    public static String GOOGLE_ANALYTIC_CHOGADIA = "Open_chogadia";
    public static String GOOGLE_ANALYTIC_DOGHATI = "Open_DoGhati";
    public static String GOOGLE_ANALYTIC_VIDEO = "Open_Video";
    public static String GOOGLE_ANALYTIC_PLAY_VIDEO = "Play_Video";
    public static String GOOGLE_ANALYTIC_SEARCH_VIDEO = "Search_Video";
    public static String GOOGLE_ANALYTIC_PANCHAK = "Open_Panchak";
    public static String GOOGLE_ANALYTIC_BHADRA = "Open_Bhadra";
    public static String GOOGLE_ANALYTIC_MUHURAT = "Open_Muhurat";
    public static String GOOGLE_ANALYTIC_DAILY_NOTES = "Open_Daily_Notes";
    public static String GOOGLE_ANALYTIC_LAGNA = "Open_Lagna";
    public static String GOOGLE_ANALYTIC_COGNI_ASTRO = "Open_Cogni_Astro";
    public static String GOOGLE_ANALYTIC_DHRUV = "Open_Dhruv";
    public static String GOOGLE_ANALYTIC_BRIHAT = "Open_Brihat_Horoscope";
    public static String GOOGLE_ANALYTIC_VARTA = "Open_Varta";
    public static String GOOGLE_ANALYTIC_HOME_TALK_BTN = "home_bottom_talk_to_astrologer_btn";
    public static String GOOGLE_ANALYTIC_HOME_CHAT_BTN = "home_bottom_chat_btn";

    public static String GOOGLE_ANALYTIC_VARTA_TAB_CALL_BTN = "varta_tab_call_btn";
    public static String GOOGLE_ANALYTIC_VARTA_TAB_CHAT_BTN = "varta_tab_chat_btn";
    public static String GOOGLE_ANALYTIC_VARTA_HOME_CALL_BTN = "varta_home_call_btn";
    public static String GOOGLE_ANALYTIC_VARTA_HOME_CHAT_BTN = "varta_home_chat_btn";
    public static String GOOGLE_ANALYTIC_VARTA_TAB_LIVE_JOIN = "varta_tab_live_join";
    public static String GOOGLE_ANALYTIC_VARTA_HOME_LIVE_JOIN = "varta_home_live_join";

    public static String GOOGLE_ANALYTIC_RAHUKAAL = "Open_RahuKaal";
    public static String GOOGLE_ANALYTIC_HOROSCOPE_DAILY = "Open_HoroScope_DAILY";
    public static String GOOGLE_ANALYTIC_HOROSCOPE_WEEKLY = "Open_HoroScope_Weekly";
    public static String GOOGLE_ANALYTIC_HOROSCOPE_WEEKLY_LOVE = "Open_HoroScope_Weekly_Love";
    public static String GOOGLE_ANALYTIC_HOROSCOPE_MONTHLY = "Open_HoroScope_Monthly";
    public static String GOOGLE_ANALYTIC_HOROSCOPE_YEARLY = "Open_HoroScope_Yearly";

    public static String GOOGLE_ANALYTICS_AK_BOTTOM_BAR_CALL = "ak_bottom_bar_call";
    public static String GOOGLE_ANALYTICS_AK_BOTTOM_BAR_CHAT = "ak_bottom_bar_chat";
    public static String GOOGLE_ANALYTICS_AK_BOTTOM_BAR_LIVE = "ak_bottom_bar_live";
    public static String GOOGLE_ANALYTICS_AK_BOTTOM_BAR_KUNDLI = "ak_bottom_bar_kundli";
    public static String GOOGLE_ANALYTICS_VARTA_HOME_BOTTOM_BAR_CALL = "varta_home_bottom_call";
    public static String GOOGLE_ANALYTICS_VARTA_HOME_BOTTOM_BAR_CHAT = "varta_home_bottom_chat";
    public static String GOOGLE_ANALYTICS_VARTA_HOME_BOTTOM_BAR_KUNDLI = "varta_home_bottom_kundli";
    public static String GOOGLE_ANALYTICS_VARTA_HOME_BOTTOM_BAR_LIVE = "varta_home_bottom_kundli";
    public static String GOOGLE_ANALYTICS_ALL_LIVE_BOTTOM_CALL = "all_live_bottom_call";
    public static String GOOGLE_ANALYTICS_ALL_LIVE_BOTTOM_CHAT = "all_live_bottom_chat";
    public static String GOOGLE_ANALYTICS_ALL_LIVE_BOTTOM_LIVE = "all_live_bottom_live";
    public static String GOOGLE_ANALYTICS_CONSULT_BOTTOM_CALL = "consult_bottom_call";
    public static String GOOGLE_ANALYTICS_CONSULT_BOTTOM_CHAT = "consult_bottom_chat";
    public static String GOOGLE_ANALYTICS_CONSULT_BOTTOM_LIVE = "consult_bottom_live";
    public static String GOOGLE_ANALYTICS_CONSULT_BOTTOM_KUNDLI = "consult_bottom_kundli";
    public static String GOOGLE_ANALYTICS_FOLLOWING_BOTTOM_CALL = "following_bottom_call";
    public static String GOOGLE_ANALYTICS_FOLLOWING_BOTTOM_CHAT = "following_bottom_chat";
    public static String GOOGLE_ANALYTICS_FOLLOWING_BOTTOM_LIVE = "following_bottom_live";
    public static String GOOGLE_ANALYTICS_FOLLOWING_BOTTOM_KUNDLI = "following_bottom_kundli";
    public static String GOOGLE_ANALYTICS_GENERATE_TICKET_BOTTOM_CALL = "generate_ticket_bottom_call";
    public static String GOOGLE_ANALYTICS_GENERATE_TICKET_BOTTOM_CHAT = "generate_ticket_bottom_chat";
    public static String GOOGLE_ANALYTICS_GENERATE_TICKET_BOTTOM_LIVE = "generate_ticket_bottom_live";
    public static String GOOGLE_ANALYTICS_GENERATE_TICKET_BOTTOM_KUNDLI = "generate_ticket_bottom_kundli";
    public static String GOOGLE_ANALYTICS_MY_ACCOUNT_BOTTOM_CALL = "my_account_bottom_call";
    public static String GOOGLE_ANALYTICS_MY_ACCOUNT_BOTTOM_CHAT = "my_account_bottom_chat";
    public static String GOOGLE_ANALYTICS_MY_ACCOUNT_BOTTOM_LIVE = "my_account_bottom_live";
    public static String GOOGLE_ANALYTICS_MY_ACCOUNT_BOTTOM_KUNDLI = "my_account_bottom_kundli";
    public static String GOOGLE_ANALYTICS_PROFILE_BOTTOM_CALL = "profile_bottom_call";
    public static String GOOGLE_ANALYTICS_PROFILE_BOTTOM_CHAT = "profile_bottom_chat";
    public static String GOOGLE_ANALYTICS_PROFILE_BOTTOM_LIVE = "profile_bottom_live";
    public static String GOOGLE_ANALYTICS_PROFILE_BOTTOM_KUNDLI = "profile_bottom_kundli";
    public static String GOOGLE_ANALYTICS_WALLET_BOTTOM_CALL = "wallet_bottom_call";
    public static String GOOGLE_ANALYTICS_WALLET_BOTTOM_CHAT = "wallet_bottom_chat";
    public static String GOOGLE_ANALYTICS_WALLET_BOTTOM_LIVE = "wallet_bottom_live";
    public static String GOOGLE_ANALYTICS_WALLET_BOTTOM_KUNDLI = "wallet_bottom_kundli";
    public static String GOOGLE_ANALYTICS_ASTROLOGER_DESCRIPTION_BOTTOM_CALL = "astrologer_description_bottom_call";
    public static String GOOGLE_ANALYTICS_ASTROLOGER_DESCRIPTION_BOTTOM_CHAT = "astrologer_description_bottom_chat";
    public static String GOOGLE_ANALYTICS_ASTROLOGER_DESCRIPTION_BOTTOM_LIVE = "astrologer_description_bottom_live";
    public static String GOOGLE_ANALYTICS_ASTROLOGER_DESCRIPTION_BOTTOM_KUNDLI = "astrologer_description_bottom_kundli";
    public static String GOOGLE_ANALYTICS_OUTPUT_MASTER_BOTTOM_CALL = "output_master_bottom_call";
    public static String GOOGLE_ANALYTICS_OUTPUT_MASTER_BOTTOM_CHAT = "output_master_bottom_chat";
    public static String GOOGLE_ANALYTICS_OUTPUT_MASTER_BOTTOM_LIVE = "output_master_bottom_live";
    public static String GOOGLE_ANALYTICS_OUTPUT_MASTER_BOTTOM_KUNDLI = "output_master_bottom_kundli";
    public static String GOOGLE_ANALYTICS_OUTPUT_MATCHING_MASTER_BOTTOM_CALL = "omatching_master_bottom_call";
    public static String GOOGLE_ANALYTICS_OUTPUT_MATCHING_MASTER_BOTTOM_CHAT = "omatching_master_bottom_chat";
    public static String GOOGLE_ANALYTICS_OUTPUT_MATCHING_MASTER_BOTTOM_LIVE = "omatching_master_bottom_live";
    public static String GOOGLE_ANALYTICS_OUTPUT_MATCHING_MASTER_BOTTOM_KUNDLI = "omatching_master_bottom_kundli";
    public static String GOOGLE_ANALYTICS_INPUT_PANCHANG_BOTTOM_CALL = "input_panchang_bottom_call";
    public static String GOOGLE_ANALYTICS_INPUT_PANCHANG_BOTTOM_CHAT = "input_panchang_bottom_chat";
    public static String GOOGLE_ANALYTICS_INPUT_PANCHANG_BOTTOM_LIVE = "input_panchang_bottom_live";
    public static String GOOGLE_ANALYTICS_INPUT_PANCHANG_BOTTOM_KUNDLI = "input_panchang_bottom_kundli";
    public static String GOOGLE_ANALYTICS_NUMEROLOGY_CALC_O_BOTTOM_CALL = "numero_calc_o_bottom_call";
    public static String GOOGLE_ANALYTICS_NUMEROLOGY_CALC_O_BOTTOM_CHAT = "numero_calc_o_bottom_chat";
    public static String GOOGLE_ANALYTICS_NUMEROLOGY_CALC_O_BOTTOM_LIVE = "numero_calc_o_bottom_live";
    public static String GOOGLE_ANALYTICS_NUMEROLOGY_CALC_O_BOTTOM_KUNDLI = "numero_calc_o_bottom_kundli";
    public static String GOOGLE_ANALYTICS_DETAILED_HOROSCOPE_BOTTOM_CALL = "detailed_horoscope_bottom_call";
    public static String GOOGLE_ANALYTICS_DETAILED_HOROSCOPE_BOTTOM_CHAT = "detailed_horoscope_bottom_chat";
    public static String GOOGLE_ANALYTICS_DETAILED_HOROSCOPE_BOTTOM_LIVE = "detailed_horoscope_bottom_live";
    public static String GOOGLE_ANALYTICS_DETAILED_HOROSCOPE_BOTTOM_KUNDLI = "detailed_horoscope_bottom_kundli";
    public static String GOOGLE_ANALYTICS_LIVE_ACTIVITY_SHARE = "live_activity_share";
    public static String GOOGLE_ANALYTICS_ASTROLOGER_DESCRIPTION_SHARE = "astrologer_description_share";
    public static String GOOGLE_ANALYTICS_SUCCESSFUL_FREE_CHAT_CALL = "success_free_chat_call";
    public static String GOOGLE_ANALYTICS_SUCCESSFUL_FIRST_FREE_CHAT_CALL = "success_first_free_chat_call";
    public static String GOOGLE_ANALYTICS_SUCCESSFUL_SECOND_FREE_CHAT_CALL = "success_second_free_chat_call";
    public static String GOOGLE_ANALYTICS_ASTROLOGER_DESCRIPTION_SEND_GIFT = "astrologer_description_send_gift";
    public static String GOOGLE_ANALYTICS_DAILY_HOROSCOPE_CALL = "daily_horoscope_call";
    public static String GOOGLE_ANALYTICS_DAILY_HOROSCOPE_CHAT = "daily_horoscope_chat";
    public static String GOOGLE_ANALYTICS_WEEKLY_HOROSCOPE_CALL = "weekly_horoscope_call";
    public static String GOOGLE_ANALYTICS_WEEKLY_HOROSCOPE_CHAT = "weekly_horoscope_chat";
    public static String GOOGLE_ANALYTICS_WEEKLY_LOVE_HOROSCOPE_CALL = "weekly_love_horoscope_call";
    public static String GOOGLE_ANALYTICS_WEEKLY_LOVE_HOROSCOPE_CHAT = "weekly_love_horoscope_chat";
    public static String GOOGLE_ANALYTICS_MONTHLY_HOROSCOPE_CALL = "monthly_horoscope_call";
    public static String GOOGLE_ANALYTICS_MONTHLY_HOROSCOPE_CHAT = "monthly_horoscope_chat";
    public static String GOOGLE_ANALYTICS_YEARLY_HOROSCOPE_CALL = "yearly_horoscope_call";
    public static String GOOGLE_ANALYTICS_YEARLY_HOROSCOPE_CHAT = "yearly_horoscope_chat";

    public static String GOOGLE_ANALYTICS_VARTA_HOME_KUNDLI = "varta_home_kundli";
    public static String GOOGLE_ANALYTICS_VARTA_HOME_HOROSCOPE = "varta_home_horoscope";
    public static String GOOGLE_ANALYTICS_VARTA_HOME_MATCHING = "varta_home_matching";
    public static String GOOGLE_ANALYTICS_VARTA_HOME_FREE_CHAT = "varta_home_free_chat";

    public static String GOOGLE_ANALYTICS_KUNDLI_HOME_ONLINE_ASTRO_CARD = "kundli_home_online_astro_card";

    public static String[] arrLang = {"USER_LANGUAGE_ENGLISH",
            "USER_LANGUAGE_HINDI", "USER_LANGUAGE_TAMIL", "", "USER_LANGUAGE_KANNADA", "USER_LANGUAGE_TELUGU", "USER_LANGUAGE_BANGALI", "USER_LANGUAGE_GUJARATI", "USER_LANGUAGE_MALAYALAM", "USER_LANGUAGE_MARATHI","","","", "USER_LANGUAGE_ODIA","USER_LANGUAGE_ASAMMESSE", "","USER_LANGUAGE_SPANISH","USER_LANGUAGE_CHINESE","USER_LANGUAGE_JAPANESE","USER_LANGUAGE_PORTUGUESE","USER_LANGUAGE_GERMAN","USER_LANGUAGE_ITALIAN","USER_LANGUAGE_FRENCH"};


    public static String GOOGLE_ANALYTIC_LALKITAB = "Lalkitab";
    public static String[] GOOGLE_ANALYTIC_PAGES_LALKITAB = {
            "Lalkitab_Category", "Lalkitab_Kundli", "Lalkitab_Remedies",
            "Lalkitab_Varsha_Kundli", "Lalkitab_Debt", "Lalkitab_KundliType", "LalKitab_Dasha", "LalKitab_Planetary_Positions", "LalKitab_House_Positions", "LalKitab_Ask a Question"};
    public static String GOOGLE_ANALYTIC_VARSHPHAL = "Varshaphal";
    public static String[] GOOGLE_ANALYTIC_PAGES_VARSHPHAL = {
            "Varshaphal_Category", "Varshaphal_Chart", "Varshaphal_Planets",
            "Varshaphal_Prediction", "Varshaphal_Panchadhikari", "Varshaphal_Ask a Question"};
    public static String GOOGLE_ANALYTIC_KUNDLI_MATCH = "KundliMatch";
    public static String GOOGLE_ANALYTIC_KUNDLI_BASIC = "Open_Basic";
    public static String GOOGLE_ANALYTIC_KUNDLI_KP = "Open_KP";
    public static String GOOGLE_ANALYTIC_KUNDLI_LALKITAB = "Open_LalKitab";
    public static String GOOGLE_ANALYTIC_KUNDLI_Varshphal = "Open_Varshphal";
    public static String GOOGLE_ANALYTIC_KUNDLI_ARTICLES = "Open_AstroSage_Articles";
    public static String GOOGLE_ANALYTIC_KUNDLI_SHARE_APP = "Share_App";
    public static String GOOGLE_ANALYTIC_KUNDLI_SHARE_APP_WITH_FRIENDS = "Share_App_With_Friends";
    public static String GOOGLE_ANALYTIC_KUNDLI_SEND_FEEDBACK = "Send_Feedback";
    public static String GOOGLE_ANALYTIC_DOWNLOAD_PDF = "Download_Pdf";
    public static String GOOGLE_ANALYTIC_ORDER_NOW_DOWNLOAD_PDF = "order_now_download_pdf_link";
    public static String GOOGLE_ANALYTIC_PRINT_PDF = "Print_Pdf";
    public static String GOOGLE_ANALYTIC_SPINNER_KUNDALI = "Switched Tab from spinner";
    public static String GOOGLE_ANALYTIC_DOWNLOAD_PDF_KUNDLI_SIDEMENU = "Kundli_SideMenu";
    public static String GOOGLE_ANALYTIC_DOWNLOAD_PDF_KUNDLI_ACTIONBAR = "Kundli_ActionBar";
    public static String GOOGLE_ANALYTIC_DOWNLOAD_PDF_KUNDLI_LASTSCREEN = "Kundli_LastScreen";
    public static String GOOGLE_ANALYTIC_DOWNLOAD_PDF_MATCHING_SIDEMENU = "Match_SideMenu";
    public static String GOOGLE_ANALYTIC_DOWNLOAD_PDF_MATCHING_ACTIONBAR = "Match_ActionBar";
    public static String GOOGLE_ANALYTIC_DOWNLOAD_PDF_MATCHING_LASTSCREEN = "Match_LastScreen";
    public static String FIREBASE_EVENT_ORDER_NOW = "order_now_download_pdf";


    public static String GOOGLE_ANALYTIC_KUNDLI_SHARE_PDF = "Share_Pdf";
    public static String GOOGLE_ANALYTIC_KUNDLI_SHARE_URL = "Share_Url";
    public static String GOOGLE_ANALYTIC_KUNDLI_BOOKMARK = "Kundali_Bookmark";
    public static String GOOGLE_ANALYTIC_KUNDLI_SHARE_DHRUV_PDF = "Share_Dhruv_Pdf";
    public static String GOOGLE_ANALYTIC_KUNDLI_PRINT_DHRUV_PDF = "Print_Dhruv_Pdf";
    public static String GOOGLE_ANALYTIC_KUNDLI_CLOSE_DHRUV_PDF = "Close_Dhruv_Pdf";

    public static String[] GOOGLE_ANALYTIC_PAGES_KUNDLI_MATCH = {
            "KundliMatch_MatchDetails", "KundliMatch_Varna",
            "KundliMatch_Vasya", "KundliMatch_Tara", "KundliMatch_Yoni",
            "KundliMatch_Matri", "KundliMatch_Gana", "KundliMatch_Bhakoot",
            "KundliMatch_Nadi"
    };
    public static String GOOGLE_ANALYTIC_EVENT_AUTO_SYNC = "AUTO_SYNC";
    public static String GOOGLE_ANALYTIC_AUTO_SYNC_CHART = "AUTO_SYNC_CHART";
    public static String GOOGLE_ANALYTIC_SYNC_CHART = "SYNC_CHART";
    public static String GOOGLE_ANALYTIC_SAVE_NOTES = "SAVE_NOTES";

    public static String GOOGLE_ANALYTIC_ASTROSHOP_GEMSTONE = "GEMSTONE";
    public static String GOOGLE_ANALYTIC_ASTROSHOP_YANTRAS = "YANTRAS";
    public static String GOOGLE_ANALYTIC_ASTROSHOP_RUDRAKSHA = "RUDRAKSHA";
    public static String GOOGLE_ANALYTIC_ASTROSHOP_MALA = "MALA";
    public static String GOOGLE_ANALYTIC_ASTROSHOP_NAVAGRAH = "NAVAGRAH";
    public static String GOOGLE_ANALYTIC_ASTROSHOP_JADI = "JADI";
    public static String GOOGLE_ANALYTIC_ASTROSHOP_MISC = "MISC";
    public static String GOOGLE_ANALYTIC_ASTROSHOP_SERVICE = "SERVICE";
    public static String GOOGLE_ANALYTIC_ASTROSHOP_ASTROLOGER = "ASTROLOGER";
    public static String GOOGLE_ANALYTIC_ASTROSHOP_PRODUCTS = "PRODUCTS";

    public static String GOOGLE_ANALYTIC_KUNDALI_BOTTOM_BUTTON_PLANET = "kundli_bottom_button_planet";
    public static String GOOGLE_ANALYTIC_KUNDALI_BOTTOM_BUTTON_DASHA = "kundli_bottom_button_dasha";
    public static String GOOGLE_ANALYTIC_KUNDALI_BOTTOM_BUTTON_PREDICTIONS = "kundli_bottom_button_predictions";
    public static String GOOGLE_ANALYTIC_KUNDALI_BOTTOM_BUTTON_KPS_SYSTEM = "kundli_bottom_button_KP_SYSTEM";
    public static String GOOGLE_ANALYTIC_KUNDALI_BOTTOM_BUTTON_SHODASHVARGA = "kundli_bottom_button_shodashvarga";
    public static String GOOGLE_ANALYTIC_KUNDALI_BOTTOM_BUTTON_LAL_KITAB = "kundli_bottom_button_lal_kitab";
    public static String GOOGLE_ANALYTIC_KUNDALI_BOTTOM_BUTTON_VARSHPHAL = "kundli_bottom_button_varshphal";
    public static String GOOGLE_ANALYTIC_KUNDALI_BOTTOM_BUTTON_RAJ_YOGA = "kundli_bottom_button_raj_yoga";
    public static String GOOGLE_ANALYTIC_KUNDALI_BOTTOM_BUTTON_GOCHAR = "kundli_bottom_button_gochar";

    public static String[] GOOGLE_ANALYTIC_PAGES_BASIC = {"Basic_Category", "Basic_Lagna",
            "Basic_Navamsha", "Basic_Moon", "Basic_Chalit_Chart",
            "Basic_Planets", "Basic_Planet_sub", "Basic_Chalit_table",
            "Birth_detail", "Basic_Panchang", "Basic_Ashtakvarga",
            "Basic_Jaimini_Karakamsa", "Basic_Jaimini_Swamsa", "Basic_Goacher", "Basic_ShadBala", "Basic_Prastharashtakvarga",
            "Basic_Bhav_Madhya", "Basic_Friendship", "Basic_Personal_Detail", "Basic_Avakahada_chakra", "Basic_Gathak-favourable_points", "Download PDF", "Ask a Question"};   //added by tejinder for new web page
    // public static String GOOGLE_ANALYTIC_APPLICATION_NAME="AstroSageKundli ";
    public static String GOOGLE_ANALYTIC_BASIC = "Basic";

    public static String GOOGLE_ANALYTIC_KP = "KP";
    public static String[] GOOGLE_ANALYTIC_PAGES_KP = {"KP_Category", "KP_Chart", "KP_Rashi",
            "KP_Planets", "KP_Cusps", "KP_Planet_Sig", "KP_House_Sig",
            "KP_Planet_Sig_View2", "KP_Nakshtra_nadi", "KP_KCIL", "KP_4Step",
            "KP_CIL", "KP_RP", "KP_CRP", "KP_Misc"/*added by tejinder*/, "Basic_KpCusp", "Ask a Question"};
    public static String GOOGLE_ANALYTIC_PREDICTION = "Prediction";

    public static String[] GOOGLE_ANALYTIC_PAGES_PREDICTION = {"Predictions"/* ADDED BY BIJENDRA FOR PREDICTION CATEGORY ON 07-05-15*/,
            "Prediction_Life", "Prediction_Monthly_Annual", "Prediction_Daily",
            "Prediction_Mangal_Dosh", "Prediction_Sade_sati",
            "Prediction_Kaal_Sarp_dosh", "Prediction_Lalkitab",
            "Prediction_Lalkitab_Kundli_Type", "Prediction_Lalkitab_Remedies",
            "Prediction_Ascendent", "Prediction_Planet_Consideration",
            "Prediction_Gemstone_Report", "Prediction_Transit",
            "Prediction_Mahadasha_Phal", "Prediction_Nakshtra_Report", "Prediction_Varshphal",
            "Prediction_Babyname", "Prediction_moonwestren", "Prediction_Moon", "Prediction_Rudraksh", "Prediction_Jadi", "Prediction_Yantra", "Prediction_isht_devta", "Ask a Question"
    };

    public static String GOOGLE_ANALYTIC_SHODASHVARGA = "Shodashvarga";
    public static String[] GOOGLE_ANALYTIC_PAGES_SHODASHVARGA = {"Shodashvarga_Category",
            "Shodashvarga_D1", "Shodashvarga_D2", "Shodashvarga_D3",
            "Shodashvarga_D4", "Shodashvarga_D7", "Shodashvarga_D9",
            "Shodashvarga_D10", "Shodashvarga_D12", "Shodashvarga_D16",
            "Shodashvarga_D20", "Shodashvarga_D24", "Shodashvarga_D27",
            "Shodashvarga_D30", "Shodashvarga_D40", "Shodashvarga_D45",
            "Shodashvarga_D60", "Shodashvarga_Table", "Ask a Question"};

    public static String GOOGLE_ANALYTIC_MISCELLANEOUS = "Miscellaneous";
    public static String[] GOOGLE_ANALYTIC_PAGES_MISC = {"Miscellaneous",
            "Panchadhikari", "Yoga_Dosha", "Remedies", "Karak",
            "Avastha", "Navatara", "Upgraha_Table", "Upgraha_Chart",
            "Arudha_Chart", /*"Current_Ruling_Planets",*/ "Shodashvarga_Table_Rashi"
            /*,"Shodashvarga_Bhava"*/
    };

    public static final String GOOGLE_ANALYTIC_PAYMENT_FAILED = "ASK_A_QUESTION_PAYMENT_FAILED";
    public static final String GOOGLE_ANALYTIC_PAYMENT_SUCCESS = "ASK_A_QUESTION_PURCHASED";

    public static final String GOOGLE_ANALYTIC_ASK_A_QUESTION_FREE = "FREE_ASK_A_QUESTION_PURCHASED";

    public static final String GOOGLE_ANALYTIC_SERVICE_PAYMENT_SUCCESS = "SERVICE_PURCHASED";
    public static final String GOOGLE_ANALYTIC_SERVICE_PAYMENT_FAILED = "SERVICE_PAYMENT_FAILED";
    public static final String GOOGLE_ANALYTIC_TOPUP_PAYTM_PURCHASED_FAILED = "TOPUP_PURCHASED_FAILED_PAYTM";

    public static final String GOOGLE_ANALYTIC_SERVICE_RAZOR_PAYMENT_SUCCESS = "SERVICE_PURCHASED_RAZORPAY";
    public static final String GOOGLE_ANALYTIC_SERVICE_RAZOR_PAYMENT_FAILED = "SERVICE_PAYMENT_FAILED_RAZORPAY";
    public static final String GOOGLE_ANALYTIC_TOPUP_RAZOR_PAYMENT_FAILED = "TOPUP_PAYMENT_FAILED_RAZORPAY";

    public static final String GOOGLE_ANALYTIC_PAYMENT_FAILED_PAYTM = "ASK_A_QUESTION_PAYMENT_FAILED_PAYTM";
    public static final String GOOGLE_ANALYTIC_PAYMENT_FAILED_RAZORPAY = "ASK_A_QUESTION_PAYMENT_FAILED_RAZORPAY";

    public static final String GOOGLE_ANALYTIC_PAYMENT_SUCCESS_PAYTM = "ASK_A_QUESTION_PURCHASED_PAYTM";
    public static final String GOOGLE_ANALYTIC_PAYMENT_SUCCESS_RAZORPAY = "ASK_A_QUESTION_PURCHASED_RAZORPAY";

    public static final String GOOGLE_ANALYTIC_PAYMENT_FAILED_POPUP_CLOSE = "ASK_A_QUESTION_DIALOG_CLOSE";
    public static final String GOOGLE_ANALYTIC_PAYMENT_FAILED_POPUP_CALL = "ASK_A_QUESTION_DIALOG_CALL_US";
    public static final String GOOGLE_ANALYTIC_PAYMENT_FAILED_POPUP_RETRY = "ASK_A_QUESTION_DIALOG_RETRY";
    public static String GOOGLE_ANALYTIC_ASKAQUESTION_FROM_MENU = "ASK_A_QUESTION_TITLE_ICON";

    public static String GOOGLE_ANALYTIC_ASKAQUESTION_PAYTM = "ASK_A_QUESTION_PAYTM";
    public static String GOOGLE_ANALYTIC_ASKAQUESTION_GOOGLE_WALLET = "ASK_A_QUESTION_GOOGLE_WALLET";


    public static String GOOGLE_ANALYTIC_SERVICE_PAYTM = "SERVICE_PAYTM_SELECTED";
    public static String GOOGLE_ANALYTIC_SERVICE_RAZORPAY = "SERVICE_RAZORPAY_SELECTED";
    public static String GOOGLE_ANALYTIC_TOPUP_PAYTM = "TOPUP_PAYTM_SELECTED";
    public static String GOOGLE_ANALYTIC_TOPUP_RAZORPAY = "TOPUP_RAZORPAY_SELECTED";

    public static String GOOGLE_ANALYTIC_HOME_SCREEN_ADD = "Open_Home_screen_ad";// ADDED
    public static String GOOGLE_ANALYTIC_ASTROSHOP_SCREEN_ADD = "Open_AstroShop_screen_ad";// ADDED

    public static String GOOGLE_ANALYTIC_SLOT_0_Add = "Open_Ad_slot_0";// ADDED
    public static String GOOGLE_ANALYTIC_SLOT_1_Add = "Open_Ad_slot_1";// ADDED
    public static String GOOGLE_ANALYTIC_SLOT_2_Add = "Open_Ad_slot_2";// ADDED
    public static String GOOGLE_ANALYTIC_SLOT_3_Add = "Open_Ad_slot_3";// ADDED
    public static String GOOGLE_ANALYTIC_SLOT_4_Add = "Open_Ad_slot_4";// ADDED
    public static String GOOGLE_ANALYTIC_SLOT_5_Add = "Open_Ad_slot_5";// ADDED
    public static String GOOGLE_ANALYTIC_SLOT_6_Add = "Open_Ad_slot_6";// ADDED
    public static String GOOGLE_ANALYTIC_SLOT_7_Add = "Open_Ad_slot_7";// ADDED
    public static String GOOGLE_ANALYTIC_SLOT_8_Add = "Open_Ad_slot_8";// ADDED
    public static String GOOGLE_ANALYTIC_SLOT_9_Add = "Open_Ad_slot_9";// ADDED

    public static String GOOGLE_ANALYTIC_SLOT_10_Add = "Open_Ad_slot_10";// ADDED
    public static String GOOGLE_ANALYTIC_SLOT_11_Add = "Open_Ad_slot_11";// ADDED
    public static String GOOGLE_ANALYTIC_SLOT_12_Add = "Open_Ad_slot_12";// ADDED
    public static String GOOGLE_ANALYTIC_SLOT_13_Add = "Open_Ad_slot_13";// ADDED
    public static String GOOGLE_ANALYTIC_SLOT_14_Add = "Open_Ad_slot_14";// ADDED
    public static String GOOGLE_ANALYTIC_SLOT_15_Add = "Open_Ad_slot_15";// ADDED
    public static String GOOGLE_ANALYTIC_SLOT_16_Add = "Open_Ad_slot_16";// ADDED
    public static String GOOGLE_ANALYTIC_SLOT_17_Add = "Open_Ad_slot_17";// ADDED
    public static String GOOGLE_ANALYTIC_SLOT_18_Add = "Open_Ad_slot_18";// ADDED

    public static String GOOGLE_ANALYTIC_SLOT_19_Add = "Open_Ad_slot_19";// ADDED
    public static String GOOGLE_ANALYTIC_SLOT_20_Add = "Open_Ad_slot_20";// ADDED
    public static String GOOGLE_ANALYTIC_SLOT_21_Add = "Open_Ad_slot_21";// ADDED
    public static String GOOGLE_ANALYTIC_SLOT_22_Add = "Open_Ad_slot_22";// ADDED
    public static String GOOGLE_ANALYTIC_SLOT_23_Add = "Open_Ad_slot_23";// ADDED
    public static String GOOGLE_ANALYTIC_SLOT_25_Add = "Open_Ad_slot_25";
    public static String GOOGLE_ANALYTIC_SLOT_28_Add = "Open_Ad_slot_28";// ADDED
    public static String GOOGLE_ANALYTIC_SLOT_29_Add = "Open_Ad_slot_29";// ADDED ON VIDEO SEARCH
    public static String GOOGLE_ANALYTIC_SLOT_30_Add = "Open_Ad_slot_30";// ADDED
    public static String GOOGLE_ANALYTIC_SLOT_31_Add = "Open_Ad_slot_31";// ADDED
    public static String GOOGLE_ANALYTIC_SLOT_32_Add = "Open_Ad_slot_32";// ADDED
    public static String GOOGLE_ANALYTIC_SLOT_33_Add = "Open_Ad_slot_33";// ADDED
    public static String GOOGLE_ANALYTIC_SLOT_34_Add = "Open_Ad_slot_34";// ADDED
    public static String GOOGLE_ANALYTIC_SLOT_35_Add = "Open_Ad_slot_35";// ADDED
    public static String GOOGLE_ANALYTIC_SLOT_36_Add = "Open_Ad_slot_36";// ADDED
    public static String GOOGLE_ANALYTIC_SLOT_37_Add = "Open_Ad_slot_37";// ADDED
    public static String GOOGLE_ANALYTIC_SLOT_38_Add = "Open_Ad_slot_38";// ADDED
    public static String GOOGLE_ANALYTIC_SLOT_39_Add = "Open_Ad_slot_39";// ADDED
    public static String GOOGLE_ANALYTIC_SLOT_40_Add = "Open_Ad_slot_40";// ADDED

    //Numerology
    public static String GOOGLE_ANALYTIC_SLOT_41_Add = "Open_Ad_slot_41";
    public static String GOOGLE_ANALYTIC_SLOT_42_Add = "Open_Ad_slot_42";
    public static String GOOGLE_ANALYTIC_SLOT_43_Add = "Open_Ad_slot_43";
    public static String GOOGLE_ANALYTIC_SLOT_44_Add = "Open_Ad_slot_44";
    public static String GOOGLE_ANALYTIC_SLOT_45_Add = "Open_Ad_slot_45";
    public static String GOOGLE_ANALYTIC_SLOT_46_Add = "Open_Ad_slot_46";
    public static String GOOGLE_ANALYTIC_SLOT_47_Add = "Open_Ad_slot_47";
    public static String GOOGLE_ANALYTIC_SLOT_48_Add = "Open_Ad_slot_48";
    public static String GOOGLE_ANALYTIC_SLOT_49_Add = "Open_Ad_slot_49";
    public static String GOOGLE_ANALYTIC_SLOT_50_Add = "Open_Ad_slot_50";
    public static String GOOGLE_ANALYTIC_SLOT_51_Add = "Open_Ad_slot_51";

    //hindu calendar
    public static String GOOGLE_ANALYTIC_SLOT_52_Add = "Open_Ad_slot_52";
    public static String GOOGLE_ANALYTIC_SLOT_53_Add = "Open_Ad_slot_53";
    public static String GOOGLE_ANALYTIC_SLOT_54_Add = "Open_Ad_slot_54";
    public static String GOOGLE_ANALYTIC_SLOT_55_Add = "Open_Ad_slot_55";
    public static String GOOGLE_ANALYTIC_SLOT_56_Add = "Open_Ad_slot_56";
    public static String GOOGLE_ANALYTIC_SLOT_57_Add = "Open_Ad_slot_57";
    public static String GOOGLE_ANALYTIC_SLOT_58_Add = "Open_Ad_slot_58";
    public static String GOOGLE_ANALYTIC_SLOT_59_Add = "Open_Ad_slot_59";
    public static String GOOGLE_ANALYTIC_SLOT_60_Add = "Open_Ad_slot_60";

    //Festival 2021
    public static String GOOGLE_ANALYTIC_SLOT_61_Add = "Open_Ad_slot_61";

    public static String GOOGLE_ANALYTIC_SLOT_62_Add = "Open_Ad_slot_62"; // Post Kundli(OutputMasterActivity)
    public static String GOOGLE_ANALYTIC_SLOT_63_Add = "Open_Ad_slot_63";
    public static String GOOGLE_ANALYTIC_SLOT_64_Add = "Open_Ad_slot_64";
    public static String GOOGLE_ANALYTIC_SLOT_65_Add = "Open_Ad_slot_65";
    public static String GOOGLE_ANALYTIC_SLOT_66_Add = "Open_Ad_slot_66";
    public static String GOOGLE_ANALYTIC_SLOT_67_Add = "Open_Ad_slot_67";
    public static String GOOGLE_ANALYTIC_SLOT_68_Add = "Open_Ad_slot_68";
    public static String GOOGLE_ANALYTIC_SLOT_69_Add = "Open_Ad_slot_69";
    public static String GOOGLE_ANALYTIC_SLOT_70_Add = "Open_Ad_slot_70";
    public static String GOOGLE_ANALYTIC_SLOT_71_Add = "Open_Ad_slot_71";
    public static String GOOGLE_ANALYTIC_SLOT_72_Add = "Open_Ad_slot_72";


    public static String GOOGLE_ANALYTIC_SLOT_25_Add_Load = "Load_Open_Ad_slot_25";
    public static String GOOGLE_ANALYTIC_SHARE_BOTTOM_BUTTON = "Share matching pdf with bottom button";
    public static String GOOGLE_ANALYTIC_SHARE_TOP_BUTTON = "Share matching pdf with top button";
    public static String GOOGLE_ANALYTIC_MATCHING_MUHURAT = "matching_upcoming_marriage_muhurat_clicked ";


    public static String GOOGLE_ANALYTIC_ARTICLE_SCREEN_ADD = "Open_Article_screen_ad";// ADDED

    public static String GOOGLE_ANALYTIC_FOOTER_ADD = "Open_Footer_ad";// ADDED
    public static String GOOGLE_ANALYTICS_VARTA_PROMO_NOTIFICATION_DISPLAYED = "varta_promotional_notification_displayed";
    public static String GOOGLE_ANALYTICS_VARTA_FOLLOW_NOTIFICATION_DISPLAYED = "varta_follow_notification_displayed";
    public static String GOOGLE_ANALYTICS_VARTA_PROMO_NOTIFICATION_CLICKED = "varta_promotional_notification_clicked";
    public static String GOOGLE_ANALYTICS_VARTA_FOLLOW_NOTIFICATION_CLICKED = "varta_follow_notification_clicked";

    public static String GOOGLE_ANALYTICS_AK_INFO_NOTIFICATION_DISPLAYED = "ak_informational_notification_displayed";
    public static String GOOGLE_ANALYTICS_AK_INFO_NOTIFICATION_CLICKED = "ak_informational_notification_clicked";


    public static String CHAR_PIPE = "||";
    public static String CHAR_AMPERSENT = "&";

    public static String CHAR_LESS_THEN = "<";
    public static String CHAR_NINE = "9";
    /**
     * This is drawable images ID's Array to draw rashi icon (large) on
     * ChooseRashi Screen..
     */
    public static Integer[] rashiImageWithoutName = {R.drawable.aries,
            R.drawable.taurus, R.drawable.gemini, R.drawable.cancer,
            R.drawable.leo, R.drawable.virgo, R.drawable.libra,
            R.drawable.scorpio, R.drawable.sagittarius, R.drawable.capricorn,
            R.drawable.aquarius, R.drawable.pisces};
    public static final int ARIES = 0;
    public static final int TAURUS = 1;
    public static final int GEMINI = 2;
    public static final int CANCER = 3;
    public static final int LEO = 4;
    public static final int VIRGO = 5;
    public static final int LIBRA = 6;
    public static final int SCORPIO = 7;
    public static final int SAGITTARIUS = 8;
    public static final int CAPRICORN = 9;
    public static final int AQUARIUS = 10;
    public static final int PISCES = 11;
    //start mahtab


    public static final String YEARLY_HOROSCEOPE_PREF_KEY = "YEARLYHOROSCOPEKEY";

    //end
    public static String dailyHoroscopePrefName = "DailyHoroscopePref";
    public static String dailyHoroscopePrefHindiName = "DailyHoroscopePrefInHindi";
    public static String nextMonthlyHoroscopePrefName = "NextMonthlyHoroscopePref";
    public static String nextMonthlyHoroscopePrefHindiName = "NextMonthlyHoroscopePrefInHindi";

    public static String monthlyHoroscopePrefName = "MonthlyHoroscopePref";
    public static String monthlyHoroscopePrefHindiName = "MonthlyHoroscopePrefInHindi";
    public static String monthlyHoroscopePrefTamilName = "MonthlyHoroscopePrefInTamil";
    public static String monthlyHoroscopePrefMarathiName = "MonthlyHoroscopePrefInMarathi";
    public static String monthlyHoroscopePrefBangaliName = "MonthlyHoroscopePrefInBangali";
    public static String monthlyHoroscopePrefKannadName = "MonthlyHoroscopePrefInKannad";
    public static String monthlyHoroscopePrefTelguName = "MonthlyHoroscopePrefInTelgu";
    public static String monthlyHoroscopePrefGujaratiName = "MonthlyHoroscopePrefInGujarati";
    public static String monthlyHoroscopePrefMalayalamName = "MonthlyHoroscopePrefInMalayalam";
    public static String monthlyHoroscopePrefAssammeseName = "MonthlyHoroscopePrefInAssammese";
    public static String monthlyHoroscopePrefOdiaName = "MonthlyHoroscopePrefInOdia";

    public static String weeklyHoroscopePrefName = "WeeklyHoroscopePref";
    public static String weeklyHoroscopePrefHindiName = "WeeklyHoroscopePrefInHindi";
    public static String weeklyHoroscopePrefTamilName = "WeeklyHoroscopePrefInTamil";
    public static String weeklyHoroscopePrefMarathiName = "WeeklyHoroscopePrefInMarathi";
    public static String weeklyHoroscopePrefBangaliName = "WeeklyHoroscopePrefInBangali";
    public static String weeklyHoroscopePrefKannadName = "WeeklyHoroscopePrefInKannad";
    public static String weeklyHoroscopePrefTelguName = "WeeklyHoroscopePrefInTelgu";
    public static String weeklyHoroscopePrefGujaratiName = "WeeklyHoroscopePrefInGujarati";
    public static String weeklyHoroscopePrefMalayalamName = "WeeklyHoroscopePrefInMalayalam";
    public static String weeklyHoroscopePrefOdiaName = "WeeklyHoroscopePrefInMalayalam";
    public static String weeklyHoroscopePrefAssammeseName = "WeeklyHoroscopePrefInMalayalam";


    public static String weeklyLoveHoroscopePrefName = "WeeklyLoveHoroscopePref";
    public static String weeklyLoveHoroscopePrefHindiName = "WeeklyLoveHoroscopePrefInHindi";
    public static String weeklyLoveHoroscopePrefTamilName = "WeeklyLoveHoroscopePrefInTamil";
    public static String weeklyLoveHoroscopePrefMarathiName = "WeeklyLoveHoroscopePrefInMarathi";
    public static String weeklyLoveHoroscopePrefBangaliName = "WeeklyLoveHoroscopePrefInBangali";
    public static String weeklyLoveHoroscopePrefKannadName = "WeeklyLoveHoroscopePrefInKannad";
    public static String weeklyLoveHoroscopePrefTelguName = "WeeklyLoveHoroscopePrefInTelgu";
    public static String weeklyLoveHoroscopePrefGujaratiName = "WeeklyLoveHoroscopePrefInGujarati";
    public static String weeklyLoveHoroscopePrefMalayalamName = "WeeklyLoveHoroscopePrefInMalayalam";
    public static String weeklyLoveHoroscopePrefAssammeseName = "WeeklyLoveHoroscopePrefInAssammese";
    public static String weeklyLoveHoroscopePrefOdiaName = "WeeklyLoveHoroscopePrefInOdia";

    //commented by ankit on 17/4/2019 added other language pref.
//    public static String dailyRemediesPrefName = "DailyRemediesPref";
    public static String dailyRemediesPrefEnglishName = "DailyRemediesPrefEnglish";
    public static String dailyRemediesPrefHindiName = "DailyRemediesPrefInHindi";
    public static String dailyRemediesPrefTamilName = "DailyRemediesPrefInTamil";
    public static String dailyRemediesPrefMarathiName = "DailyRemediesPrefInMarathi";
    public static String dailyRemediesPrefBangaliName = "DailyRemediesPrefInBangali";
    public static String dailyRemediesPrefKannadName = "DailyRemediesPrefInKannad";
    public static String dailyRemediesPrefTelguName = "DailyRemediesPrefInTelgu";
    public static String dailyRemediesPrefGujaratiName = "DailyRemediesPrefInGujarati";
    public static String dailyRemediesPrefMalayalamName = "DailyRemediesPrefInMalayalam";
    public static String dailyRemediesPrefAssammeseName = "DailyRemediesPrefInAssammese";
    public static String dailyRemediesPrefOdiaName = "DailyRemediesPrefInOdia";

    //commented by ankit on 17/4/2019 added other language pref.
//    public static String tomorrowRemediesPrefName = "TomorrowRemediesPref";
    public static String tomorrowRemediesPrefEnglishName = "TomorrowRemediesPrefEnglish";
    public static String tomorrowRemediesPrefHindiName = "TomorrowRemediesPrefInHindi";
    public static String tomorrowRemediesPrefTamilName = "TomorrowRemediesPrefInTamil";
    public static String tomorrowRemediesPrefMarathiName = "TomorrowRemediesPrefInMarathi";
    public static String tomorrowRemediesPrefBangaliName = "TomorrowRemediesPrefInBangali";
    public static String tomorrowRemediesPrefKannadName = "TomorrowRemediesPrefInKannad";
    public static String tomorrowRemediesPrefTelguName = "TomorrowRemediesPrefInTelgu";
    public static String tomorrowRemediesPrefGujaratiName = "TomorrowRemediesPrefInGujarati";
    public static String tomorrowRemediesPrefMalayalamName = "TomorrowRemediesPrefInMalayalam";
    public static String tomorrowRemediesPrefAssammeseName = "TomorrowRemediesPrefInAssammese";
    public static String tomorrowRemediesPrefOdiaName = "TomorrowRemediesPrefInOdia";


    public static final String DAILY_REMEDIES_PREF_KEY = "TODAYSREMEDIESKEY";
    public static final String TOMORROW_REMEDIES_PREF_KEY = "TOMORROWREMEDIESKEY";

    // ADDED BY BIJENDRA ON 16-02-15
    public static String dailyHoroscopeTomorrowPrefHindiName = "DailyHoroscopeTomorrowPrefInHindi";
    public static String dailyHoroscopeTomorrowPrefName = "DailyHoroscopeTomorrowPref";
    // END
    public static String dailyHoroscopePrefTamilName = "DailyHoroscopePrefInTamil";
    public static final String DAILY_HOROSCEOPE_PREF_KEY = "TODAYSHOROSCOPEKEY";
    public static final String WEEKLY_HOROSCEOPE_PREF_KEY = "WEEKSHOROSCOPEKEY";
    public static final String WEEKLY_LOVE_HOROSCEOPE_PREF_KEY = "WEEKLOVESHOROSCOPEKEY";
    public static final String MONTHLY_HOROSCEOPE_PREF_KEY = "MONTHLYHOROSCOPEKEY";
    public static final String NEXT_MONTH_HOROSCEOPE_PREF_KEY = "NEXTMONTHHOROSCOPEKEY";
    public static String dailyHoroscopeTomorrowPrefTamilName = "DailyHoroscopeTomorrowPrefInTamil";
    public static String dailyHoroscopeTomorrowPrefMarathiName = "DailyHoroscopeTomorrowPrefInMarathi";
    public static String dailyHoroscopePrefMarathiName = "DailyHoroscopePrefInMarathi";
    // for personal horoscope

    public static final int DAILY = 0;
    public static final int WEEKLY = 1;
    public static final int WEEKLY_LOVE = 2;
    public static final int MONTHLY = 3;
    public static final int YEARLY = 4;

    public static final int DAILY_TYPE = 0;
    public static final int PANCHANG_TYPE = 1;
    public static final int WEEKLY_TYPE = 2;
    public static final int WEEKLY_LOVE_TYPE = 3;
    public static final int MONTHLY_TYPE = 4;
    public static final int YEARLY_TYPE = 5;
    public static final int HINDI_TYPE = 5;
    public static final int SADE_SATI_TYPE = 6;
    public static final int MAHA_DASHA_TYPE = 7;
    public static final int ANTER_DASHA_TYPE = 8;

    public static String[] pageViewDailyWeeklyMonthlyHoroscope = {"Daily",
            "Daily Panchang Horoscope", "Weekly", "Weekly Love", "Monthly", "Yearly", "Ask Questions"};
    public static final String[] RashiType = {"ARIES", "TAURUS", "GEMINI",
            "CANCER", "LEO", "VIRGO", "LIBRA", "SCORPIO", "SAGITTARIUS",
            "CAPRICORN", "AQUARIUS", "PISCES"};
    public static String[] pagePanchangHoraChogdiaDoghatiRahukaal = {"Daily Panchang Dashboard", "Daily Panchang",
            "Daily Horoscope", "Hora", "Chogdia", "Do Ghati", "Rahukaal", "Panchak", "Bhadra", "Muhurat", "Lagna", "Ask Questions"};


    public static final int HELP_LIMIT = 1;


    public static final String BIRTHDAY_INTENT_ACTION = "MY_BIRTHDAY_NOTIFICATION";
    public static final String MY_MAHADASHA_NOTIFICATION = "MY_MAHADASHA_NOTIFICATION";
    public static final String MY_ANTERDASHA_NOTIFICATION = "MY_ANTERDASHA_NOTIFICATION";
    public static final String MY_SADESATI_NOTIFICATION = "MY_SADESATI_NOTIFICATION";
    public static final String DAILY_PRIDICTION_INTENT_ACTION = "MY_HOROSCOPE_DAILY_PRIDICTION_OJASSOFT";
    public static final String WEEKLY_PRIDICTION_INTENT_ACTION = "MY_HOROSCOPE_WEEKLY_PRIDICTION_OJASSOFT";
    public static final String WEEKLYLOVE_PRIDICTION_INTENT_ACTION = "MY_HOROSCOPE_WEEKLYLOVE_PRIDICTION_OJASSOFT";
    public static final String MONTHLY_PRIDICTION_INTENT_ACTION = "MY_HOROSCOPE_MONTHLY_PRIDICTION_OJASSOFT";
    public static final String YEARLY_PRIDICTION_INTENT_ACTION = "MY_HOROSCOPE_YEARLY_PRIDICTION_OJASSOFT";
    public static final String HINDI_PREDICTION_INTENT_ACTION = "MY_HOROSCOPE_HINDI_PRIDICTION_OJASSOFT";
    public final static String ASTRO_WEBVIEW_TITLE_KEY = "Astro_webview_title_key";


    public static String YearlyHoroscopePrefName = "YearlyHoroscopePref";
    public static String YearlyHoroscopePrefNameHindi = "YearlyHoroscopePrefHindi";
    public static String YearlyHoroscopePrefNameTamil = "YearlyHoroscopePrefTamil";
    public static String YearlyHoroscopePrefNameBangla = "YearlyHoroscopePrefBangla";
    public static String YearlyHoroscopePrefNameMarathi = "YearlyHoroscopePrefMarathi";
    public static String YearlyHoroscopePrefNameTelegu = "YearlyHoroscopePrefTelegu";
    public static String YearlyHoroscopePrefNameKannad = "YearlyHoroscopePrefKannad";
    public static String YearlyHoroscopePrefNameGujrati = "YearlyHoroscopePrefGujrati";
    public static String YearlyHoroscopePrefNameMalayalam = "YearlyHoroscopePrefMalayalam";
    public static String YearlyHoroscopePrefNameAssam = "YearlyHoroscopePrefAssam";
    public static String YearlyHoroscopePrefNameOdia = "YearlyHoroscopePrefOdia";
    public static final String YEARLY_HOROSCEOPE_PREF_KEYFOR_NEXTYEAR = "YEARLYHOROSCOPEKEYFORNEXTYEAR";
    public static String YearlyHoroscopePrefNameNextYear = "YearlyHoroscopePrefnextyear";
    public static String YearlyHoroscopePrefNameNextYearHindi = "YearlyHoroscopePrefnextyearHindi";
    public static String YearlyHoroscopePrefNameNextYearTamil = "YearlyHoroscopePrefnextyearTamil";
    public static String YearlyHoroscopePrefNameNextYearBangla = "YearlyHoroscopePrefnextyearBangla";
    public static String YearlyHoroscopePrefNameNextYearMarathi = "YearlyHoroscopePrefnextyearMarathi";
    public static String YearlyHoroscopePrefNameNextYearTelugu = "YearlyHoroscopePrefnextyearTelegu";
    public static String YearlyHoroscopePrefNameNextYearKannad = "YearlyHoroscopePrefnextyearKannad";
    public static String YearlyHoroscopePrefNameNextYearGujrati = "YearlyHoroscopePrefnextyearGujrati";
    public static String YearlyHoroscopePrefNameNextYearMalayalam = "YearlyHoroscopePrefnextyearMalayalam";
    public static String YearlyHoroscopePrefNameNextYearAssammese = "YearlyHoroscopePrefnextyearAssammese";
    public static String YearlyHoroscopePrefNameNextYearOdia = "YearlyHoroscopePrefnextyearOdia";
    public static final String YEARLY_HOROSCEOPE_PREF_KEYFOR_NEXTYEAR_FOR_BOOLEAN = "YEARLYHOROSCOPEKEYFORNEXTYEARBOOLEAN";

    public static final String YEARLY_HOROSCOPE_PREF_CHECK_VALUE = "YEARLYHOROSCOPEKEYFORNEXTYEARBOOLEANCHECKVALUE";
    public static final String YEARLY_HOROSCOPE_PREF_CHECK_VALUE_TAMIL = "YEARLYHOROSCOPEKEYFORNEXTYEARBOOLEANCHECKVALUETAMIL";
    public static final String YEARLY_HOROSCOPE_PREF_CHECK_VALUE_BANGLA = "YEARLYHOROSCOPEKEYFORNEXTYEARBOOLEANCHECKVALUEBANGLA";
    public static final String YEARLY_HOROSCOPE_PREF_CHECK_VALUE_HINDI = "YEARLYHOROSCOPEKEYFORNEXTYEARBOOLEANCHECKVALUEHINDI";
    public static final String YEARLY_HOROSCOPE_PREF_CHECK_VALUE_MARATHI = "YEARLYHOROSCOPEKEYFORNEXTYEARBOOLEANCHECKVALUEMARATHI";
    public static final String YEARLY_HOROSCOPE_PREF_CHECK_VALUE_TELEGU = "YEARLYHOROSCOPEKEYFORNEXTYEARBOOLEANCHECKVALUETELEGU";
    public static final String YEARLY_HOROSCOPE_PREF_CHECK_VALUE_KANNAD = "YEARLYHOROSCOPEKEYFORNEXTYEARBOOLEANCHECKVALUEKANNAD";
    public static final String YEARLY_HOROSCOPE_PREF_CHECK_VALUE_GUJRATI = "YEARLYHOROSCOPEKEYFORNEXTYEARBOOLEANCHECKVALUEGUJRATI";
    public static final String YEARLY_HOROSCOPE_PREF_CHECK_VALUE_MALAYALAM = "YEARLYHOROSCOPEKEYFORNEXTYEARBOOLEANCHECKVALUEMALAYALAM";
    public static final String YEARLY_HOROSCOPE_PREF_CHECK_VALUE_ODIA = "YEARLYHOROSCOPEKEYFORNEXTYEARBOOLEANCHECKVALUEODIA";
    public static final String YEARLY_HOROSCOPE_PREF_CHECK_VALUE_ASSAMMESE = "YEARLYHOROSCOPEKEYFORNEXTYEARBOOLEANCHECKVALUEASSAMMESE";


    //ADDED BY BIJENDRA ON 18-02-15

    //ADDED BY BIJENDRA ON 31-10-14
    public final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    public static final String PREF_PUSH_NOTIFICATION_TOKEN = "AstroSageKundliPN";
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    public static final String PROPERTY_APP_VERSION = "appVersion";
    //public static LinearLayout layoutWithAdv;
    public static boolean SHOW_INTERSTITIAL_AD = true;
    public static boolean isGrid = false;

    public static String INTERSTITIAL_AD_ID = "ca-app-pub-7494543079410386/5134807623"; //interstitial ad id
    public static String INTERSTITIAL_INTENT_FILTER = "SHOW_INTERSTETIAL_ADV";
    //ADDED BY BIJENDRA ON 03-06-15
    public static final String ASTROSAGE_SILVER_PLAN_ID = "silver";
    public static final String ASTROSAGE_GOLD_PLAN_ID = "gold";
    public static final String ASTROSAGE_PLATINUM_PLAN_ID = "platinum";
    public static final String ASTROSAGE_ASK_QUESTION_PLAN_ID = "ask_a_question";
    //Added on 10-dec-2015
    public static final String customAdvertisementImage = "customAdvertisementImage";
    public static final String customAdvertisementLink = "customAdvertisementLink";


    public static String imageDataForAdvertisement = "imageDataForAdvertisement";
    public static View previousAdvertiseView = null;


    public static final String doNotShowMessageAgainForUpdatePlan = "doNotShowMessageAgainForUpdatePlan";
    public static final String isNeedToRunServiceToGetPurchasePlanDetails = "isNeedToRunServiceToGetPurchasePlanDetails";
    public static final String helpNumberFirst = "+91-9560267006";
    public static final String helpNumberSecond = "+91-1204138503";
    public static final String helpNumberKrutidev = "$91&9911840126";

    public static String dailyHoroscopePrefBangaliName = "DailyHoroscopePrefInBangali";
    public static String dailyHoroscopeTomorrowPrefBangaliName = "DailyHoroscopeTomorrowPrefInBengali";

    public static String dailyHoroscopePrefkannadName = "DailyHoroscopePrefInkannad";
    public static String dailyHoroscopeTomorrowPrefkannadName = "DailyHoroscopeTomorrowPrefInkannad";


    public static String dailyHoroscopePrefTelguName = "DailyHoroscopePrefInTelgu";
    public static String dailyHoroscopeTomorrowPrefTelguName = "DailyHoroscopeTomorrowPrefInTelgu";


    public static String dailyHoroscopePrefMalayalamName = "DailyHoroscopePrefInMalayalam";
    public static String dailyHoroscopeTomorrowPrefMalayalamName = "DailyHoroscopeTomorrowPrefInMalayalam";
 public static String dailyHoroscopePrefAssammeseName = "DailyHoroscopePrefInAssammese";
    public static String dailyHoroscopeTomorrowPrefAssammeseName = "DailyHoroscopeTomorrowPrefInAssammese";
 public static String dailyHoroscopePrefOdiaName = "DailyHoroscopePrefInOdia";
    public static String dailyHoroscopeTomorrowPrefOdiaName = "DailyHoroscopeTomorrowPrefInOdia";

    public static String dailyHoroscopePrefGujaratiName = "DailyHoroscopePrefInGujarati";
    public static String dailyHoroscopeTomorrowPrefGujaratiName = "DailyHoroscopeTomorrowPrefInGujarati";


    public static String call_on_first_number_9560267006 = "+91-9560267006";
    public static String call_on_second_number_120_4138503 = "+911204138503";

    public static final String needToSendDeviceIdForLogin = "NeedToSendDeviceIdForLogin";
    public static final String email_to_care = "customercare@astrosage.com";

    public final static String ASK_QUESTION_QUERY_DATA_TAB = "ASK_QUESTION_QUERY_DATA_TAB";
    public final static String ASK_QUESTION_QUERY_DATA = "ASK_QUESTION_QUERY_DATA";
    public final static String ASTRO_SERVICE_QUERY_DATA = "ASTRO_SERVICE_QUERY_DATA";
    public final static String NUMROLOGY_QUERY_DATA = "NUMROLOGY_QUERY_DATA";
    public final static String BHRIGOO_QUERY_DATA = "BHRIGOO_QUERY_DATA";
    public final static String ASTROSAGE_CHAT_QUERY_DATA = "ASTROSAGE_CHAT_QUERY_DATA";
    public final static String CHECK_INAPP_PURCHASE = "CHECK_INAPP_PURCHASE";
    public final static String VARTA_PROFILE_QUERY_DATA = "VARTA_PROFILE_QUERY_DATA";

    //question left
    public static String Type_PAYMENT = "typePayment";
    //testing
    static final public String COPA_RESULT = "com.ojassoft.astrologerchat.custompushnotification.GcmIntentService";
    static final public String COPA_MESSAGE = "com.ojassoft.astrologerchat.msg";
    public static String prefs_key_for_five_star = "isFiveStarClickedAlready";
    public static String IS_USER_PROFILE_FILLED = "isUserProfileFilled";
    public static String UPDATE_PLAN_STATUS = "updatePlanStatus";
    public static final String CHATWITHASTROLOGER = "chatWithAstrologer";
    public static final String ISINSERT = "isInsert";
    public static final String REGISTATIONSUCESSONOJASSERVER = "registationSucessOnOjasServer";
    public static final String ISWELCOMEMESSAGESUCESS = "isWelcomeMessageSucess";
    public static final String NOOFQUESTIONAVAILABLE = "noOFQuestionAvailable";
    //sendQuestion
    public static final String USERPROFILEASTROCHAT = "UserProfileAstroChat";
    public static final String ASTROSHOP_CARTSHIPPING_COST = API2_BASE_URL + "ac/astroshop/product-cart-shipping-price-v6.asp";
    public static final String CHAT_ANSWER_REPLY_MESSAGE = "CHAT_ANSWER_MESSAGE";
    public static final String KEY_USER_PICTURE = "MyProfilePicture";
    public static final String HAS_PICTURE_SAVED = "isPictureSaved";


    //GTM variable
    public static final long TIMEOUT_FOR_CONTAINER_OPEN_MILLISECONDS = 2000;
    public static final String CONTAINER_ID = "GTM-5QWRNB3";
    public static final String key_MainModuleCustomAdsImageClickListenerUrl = "MainModuleCustomAdsImageClickListenerUrl";
    public static final String key_MainModuleCustomAdsImageUrl = "MainModuleCustomAdsImageUrl";
    public static final String key_MainModuleCustomAdsVisibility = "MainModuleCustomAdsVisibility";
    public static final String key_UniversalCustomAdsImageClickListenerUrl = "UniversalCustomAdsImageClickListenerUrl";
    public static final String key_UniversalCustomAdsImageUrl = "UniversalCustomAdsImageUrl";
    public static final String key_UniversalCustomAdsVisibility = "UniversalCustomAdsVisibility";

    public static final String key_BasicPlanAstrologicalFeaturesEnglish = "BasicPlanAstrologicalFeaturesEnglishV2";
    public static final String key_BasicPlanAstrologicalFeaturesHindi = "BasicPlanAstrologicalFeaturesHindiV2";
    public static final String key_BasicPlanAstrologicalFeaturesTamil = "BasicPlanAstrologicalFeaturesTamilV2";
    public static final String key_BasicPlanAstrologicalFeaturesBangali = "BasicPlanAstrologicalFeaturesBangaliV2";
    public static final String key_BasicPlanAstrologicalFeaturesMarathi = "BasicPlanAstrologicalFeaturesMarathiV2";
    public static final String key_BasicPlanAstrologicalFeaturesTelugu = "BasicPlanAstrologicalFeaturesTeluguV2";
    public static final String key_BasicPlanAstrologicalFeaturesKannad = "BasicPlanAstrologicalFeaturesKannadaV2";
    public static final String key_BasicPlanAstrologicalFeaturesGujrati = "BasicPlanAstrologicalFeaturesGujaratiV2";
    public static final String key_BasicPlanAstrologicalFeaturesMalayalam = "BasicPlanAstrologicalFeaturesMalayalamV2";
    public static final String key_BasicPlanAstrologicalFeaturesOdia = "BasicPlanAstrologicalFeaturesOdiaV2";
    public static final String key_BasicPlanAstrologicalFeaturesAssamese = "BasicPlanAstrologicalFeaturesAssameseV2";

    public static final String data_BasicPlanAstrologicalFeaturesEnglish = "{ \t\"plans\": [{ \t\t\"heading\": \"Create Kundli\", \t\t\"desc\": \"Create Your Personalized Kundli\", \t\t\"icon\": \"plan_kundli_icon\" \t}, { \t\t\"heading\": \"Horoscope Matching\", \t\t\"desc\": \"Find Your Ideal Match Now\", \t\t\"icon\": \"plan_horoscope_icon\" \t}, { \t\t\"heading\": \"Save Unlimited Charts on Your Device\", \t\t\"desc\": \"Save Multiple Birth Charts on Local Device\", \t\t\"icon\": \"plan_mobile_icon\" \t}, { \t\t\"heading\": \"Write Your Notes and Comments\", \t\t\"desc\": \"Save Your Notes and Reminders\", \t\t\"icon\": \"plan_notes_icon\" \t}, { \t\t\"heading\": \"Save 10 Charts on Cloud\", \t\t\"desc\": \"Now Save upto 10 Charts on Cloud Storage\", \t\t\"icon\": \"plan_cloud_icon\" \t}] }";
    public static final String data_BasicPlanAstrologicalFeaturesHindi = "{\"plans\":[  {\"heading\":\"कुंडली बनाएँ\",\"desc\":\"अपनी व्यक्तिगत कुंडली बनाएँ। \",\"icon\":\"plan_kundli_icon\"},  {\"heading\":\"कुंडली मिलान\",\"desc\":\" अब अपने आदर्श मैच का पता लगाएँ। \",\"icon\":\"plan_horoscope_icon\"},  {\"heading\":\"अपने डिवाइस पर असीमित कुंडली सहेजें\",\"desc\":\"लोकल डिवाइस पर एक से अधिक जन्म कुंडली को सुरक्षित रख सकते हैं। \",\"icon\":\"plan_mobile_icon\"}, {\"heading\":\"अपने नोट्स और टिप्पणियाँ लिखें\",\"desc\":\"अपने नोट्स और अनुस्मारक को सहेजें।\",\"icon\":\"plan_notes_icon\"},  {\"heading\":\"क्लाउड पर 10 चार्ट सहेजें\",\"desc\":\"अब क्लाउड स्टोरेज पर 10 चार्ट तक सहेज सकते हैं। \",\"icon\":\"plan_cloud_icon\"} ]}";
    public static final String data_BasicPlanAstrologicalFeaturesTamil = "{\"plans\":[  {\"heading\":\"ஜாதகம் உருவாக்குங்கள்\",\"desc\":\"உங்கள் தனிப்பட்ட ஜாதகம் \",\"icon\":\"plan_kundli_icon\"},  {\"heading\":\"ஜாதக பொருத்தம்\",\"desc\":\"உங்களுக்கு ஏற்றமான பொருத்தம் காணுங்கள் \",\"icon\":\"plan_horoscope_icon\"},  {\"heading\":\"10 வரைபட கட்டங்களை கிளவுட் இல் சேமிக்கவும்\",\"desc\":\"இப்பொழுது 10வரைப்பட கட்டங்கள் வரை கிளவுட் சேமிப்பகத்தில் சேமிக்கலாம்.\",\"icon\":\"plan_mobile_icon\"}, {\"heading\":\"உங்கள் குறிப்புகள் மற்றும் கருத்துக்கள் எழுதுங்கள்\",\"desc\":\"உங்கள் குறிப்புகள் மற்றும் ஞாபகத்தை சேமிக்கவும்.\",\"icon\":\"plan_notes_icon\"},  {\"heading\":\"உங்கள் சாதனங்களில் வரைப்பட கட்டங்களை சேமிக்கவும்\",\"desc\":\"பல பிறந்த வரைப்பட கட்டங்களை உள்ளுக்குறி சாதனைகளில் சேமிக்கவும்.\",\"icon\":\"plan_cloud_icon\"} ]}";
    public static final String data_BasicPlanAstrologicalFeaturesBangali = "{\"plans\":[  {\"heading\":\"কুণ্ডলী তৈরি করুন\",\"desc\":\"আপনার ব্যক্তিগতকৃত কুণ্ডলী তৈরি করুন\",\"icon\":\"plan_kundli_icon\"},  {\"heading\":\"কুণ্ডলী মিলন\",\"desc\":\"এখন আপনার আদর্শ ম্যাচ খুঁজুন\",\"icon\":\"plan_horoscope_icon\"},  {\"heading\":\"আপনার ডিভাইসে আনলিমিটেড চার্ট সংরক্ষণ করুন\",\"desc\":\"স্থানীয় ডিভাইসে একাধিক জন্মের চার্ট সংরক্ষণ করুন\",\"icon\":\"plan_mobile_icon\"}, {\"heading\":\"আপনার নোট এবং মন্তব্য লিখুন\",\"desc\":\"আপনার নোট এবং অনুস্মারক সংরক্ষণ করুন\",\"icon\":\"plan_notes_icon\"},  {\"heading\":\"ক্লাউডে 10 চার্ট সংরক্ষণ করুন\",\"desc\":\"এখন ক্লাউড স্টোরেজে 10 টি চার্ট সংরক্ষণ করুন\",\"icon\":\"plan_cloud_icon\"} ]}";
    public static final String data_BasicPlanAstrologicalFeaturesMarathi = "{\"plans\":[  {\"heading\":\"कुंडली तयार करा\",\"desc\":\"आपली व्यक्तिगत कुंडली तयार करा\",\"icon\":\"plan_kundli_icon\"},  {\"heading\":\"कुंडली जुळत आहे\",\"desc\":\"आत्ताच आपली आदर्श जुळवणी पहा. \",\"icon\":\"plan_horoscope_icon\"},  {\"heading\":\"आपल्या डिव्हाइसवर अमर्यादित चार्ट जतन करा\",\"desc\":\"स्थानिक डिव्हाइसवर एकाधिक जन्म चार्ट जतन करा\",\"icon\":\"plan_mobile_icon\"}, {\"heading\":\"आपल्या नोट्स आणि टिपण्या लिहा\",\"desc\":\"आपल्या नोट्स आणि स्मरणपत्रे जतन करा\",\"icon\":\"plan_notes_icon\"},  {\"heading\":\"क्लाउडमध्ये 10 चार्ट्स जतन करा\",\"desc\":\"आता क्लाउड स्टोरेजवर 10 पर्यंत चार्ट जतन करा\",\"icon\":\"plan_cloud_icon\"} ]}";
    public static final String data_BasicPlanAstrologicalFeaturesTelugu = "{ \"plans\": [{\"heading\": \"కుండలిని సృష్టించండి\",\"desc\": \"మీ వ్యక్తిగతీకరించిన కుండలిని సృష్టించండి\",\"icon\": \"plan_kundli_icon\"}, {\"heading\": \"జాతక సరిపోలిక\",\"desc\": \"మీ ఆదర్శ మ్యాచ్ ని ఇప్పుడే కనుగొనండి\",\"icon\": \"plan_horoscope_icon\"}, {\"heading\": \"మీ పరికరంలో అపరిమిత చార్ట్\u200Cలను సేవ్ చేయండి\",\"desc\": \"స్థానిక పరికరంలో బహుళ బర్త్ చార్ట్\u200Cలను సేవ్ చేయండి\", \"icon\": \"plan_mobile_icon\"}, {\"heading\": \"మీ గమనికలు మరియు వ్యాఖ్యలను వ్రాయండి\",\"desc\": \"మీ గమనికలు మరియు రిమైండర్\u200Cలను సేవ్ చేయండి\",\"icon\": \"plan_notes_icon\"}, {\"heading\": \"క్లౌడ్\u200Cలో 10 చార్ట్\u200Cలను సేవ్ చేయండి\",\"desc\": \"ఇప్పుడు క్లౌడ్ స్టోరేజ్\u200Cలో 10 చార్ట్\u200Cలను సేవ్ చేయండి\",\"icon\": \"plan_cloud_icon\"}] }";    public static final String data_BasicPlanAstrologicalFeaturesKannad = "{\"plans\":[  {\"heading\":\"ಜಾತಕ  ರಚಿಸಿ\",\"desc\":\"ನಿಮ್ಮ ವೈಯಕ್ತಿಕಗೊಳಿಸಿದ ಜಾರಕವನ್ನು ರಚಿಸಿ\",\"icon\":\"plan_kundli_icon\"},   {\"heading\":\"ಜಾತಕ ಹೊಂದಾಣಿಕೆ \",\"desc\":\"ನಿಮ್ಮ ಆದರ್ಶ ಹೊಂದಾಣಿಕೆಯನ್ನು ಈಗ ಹುಡುಕಿ\",\"icon\":\"plan_horoscope_icon\"}, {\"heading\":\"ನಿಮ್ಮ ಸಾಧನದಲ್ಲಿ ಅನಿಯಮಿತ ಚಾರ್ಟ್\u200Cಗಳನ್ನು ಉಳಿಸಿ\",\"desc\":\"ಸ್ಥಳೀಯವಾಗಿ ಬಹು ಜನನ ಪಟ್ಟಿಯಲ್ಲಿ ಉಳಿಸಿ ಯಂತ್ರ\",\"icon\":\"plan_mobile_icon\"},  {\"heading\":\"ನಿಮ್ಮ ಟಿಪ್ಪಣಿಗಳು ಮತ್ತು ಪ್ರತಿಕ್ರಿಯೆಗಳನ್ನು ಬರೆಯಿರಿ\",\"desc\":\"ನಿಮ್ಮ ಟಿಪ್ಪಣಿಗಳು ಮತ್ತು ಜ್ಞಾಪನೆಗಳನ್ನು ಉಳಿಸಿ\",\"icon\":\"plan_notes_icon\"},  {\"heading\":\"ಮೇಘದಲ್ಲಿ 10 ಚಾರ್ಟ್\u200Cಗಳನ್ನು ಉಳಿಸಿ\",\"desc\":\"ಈಗ ಮೇಘ ಸಂಗ್ರಹಣೆಯಲ್ಲಿ 10 ಚಾರ್ಟ್\u200Cಗಳವರೆಗೆ ಉಳಿಸಿ\",\"icon\":\"plan_cloud_icon\"} ]}";
    public static final String data_BasicPlanAstrologicalFeaturesGujrati = "{\"plans\":[  {\"heading\":\"કુંડળી બનાવો\",\"desc\":\"તમારી વ્યક્તિગત કુંડળી બનાવો\",\"icon\":\"plan_kundli_icon\"},  {\"heading\":\"કુંડળી મિલાન\",\"desc\":\"હવે તમારૂ આદર્શ મિલાન શોધો\",\"icon\":\"plan_horoscope_icon\"},  {\"heading\":\"તમારા ડિવાઇસ પર અનલિમિટેડ કુંડળી સાચવો\",\"desc\":\"સ્થાનિક ડિવાઇસ પર વધારે જન્મ પત્રિકા સાચવો\",\"icon\":\"plan_mobile_icon\"}, {\"heading\":\"તમારી નોંધો અને ટિપ્પણીઓ લખો\",\"desc\":\"તમારી નોંધો અને યાદીઓ\",\"icon\":\"plan_notes_icon\"},  {\"heading\":\"સાચવો કલાઉડ પર 10 કુંડળીઓ\",\"desc\":\"હવે ક્લાઉડ સ્ટોરેજ પર 10 કુંડળીઓ સુધી સાચવો\",\"icon\":\"plan_cloud_icon\"} ]}";
    public static final String data_BasicPlanAstrologicalFeaturesMalayalam = "{\"plans\":[  {\"heading\":\"ജാതകം ഉണ്ടാക്കുക\",\"desc\":\"നിങ്ങളുടെ വ്യക്തിഗത ജാതകം നിർമ്മിക്കുക\",\"icon\":\"plan_kundli_icon\"},  {\"heading\":\"ജാതകം പൊരുത്തം\",\"desc\":\"ഇപ്പോൾനിങ്ങളുടെ മാതൃക പൊരുത്തം കണ്ടെത്തുക \",\"icon\":\"plan_horoscope_icon\"},  {\"heading\":\"10 ചാർട്ടുകൾക്\u200Cളൗഡിൽ സൂക്ഷിക്കുക\",\"desc\":\"10 ചാർട്ടുകൾ വരെ ക്ലൗഡ് സ്റ്റോറേജിൽ ഇപ്പോൾ സൂക്ഷിക്കാം.\",\"icon\":\"plan_mobile_icon\"}, {\"heading\":\"നിങ്ങളുടെ കുറിപ്പുകളും അഭിപ്രായങ്ങളും എഴുതുക\",\"desc\":\"നിങ്ങളുടെ കുറിപ്പുകളും ഓർമ്മപ്പെടുത്തലുകളും സൂക്ഷിക്കുക \",\"icon\":\"plan_notes_icon\"},  {\"heading\":\"നിങ്ങളുടെ ഉപകരണത്തിൽ അപരിമിതമായ ചാർട്ടുകൾ സൂക്ഷിക്കുക\",\"desc\":\"ലോക്കൽ ഉപകരണത്തിൽ ഒന്നിലധികം ജനന ചാർട്ടുകൾ സൂക്ഷിക്കുക\",\"icon\":\"plan_cloud_icon\"} ]}";
    public static final String data_BasicPlanAstrologicalFeaturesOdia = "{\"plans\":[{\"heading\":\"କୁଣ୍ଡଳୀ ସୃଷ୍ଟି କରନ୍ତୁ\",\"desc\":\"ଆପଣଙ୍କ ବ୍ୟକ୍ତିଗତ କୁଣ୍ଡଳୀ ସୃଷ୍ଟି କରନ୍ତୁ\",\"icon\":\"plan_kundli_icon\"},{\"heading\":\"କୁଣ୍ଡଳୀ ମିଳାନ\",\"desc\":\"ଏବେ ଆପଣଙ୍କ ଉପଯୁକ୍ତ ମେଳ ଖୋଜନ୍ତୁ\",\"icon\":\"plan_horoscope_icon\"},{\"heading\":\"ଆପଣଙ୍କ ଡିଭାଇସରେ ଅସୀମିତ ଚାର୍ଟ ସେଭ୍ କରନ୍ତୁ\",\"desc\":\"ଲୋକାଲ୍ ଡିଭାଇସରେ ଅନେକ ଜନ୍ମ ଚାର୍ଟ ସେଭ୍ କରନ୍ତୁ\",\"icon\":\"plan_mobile_icon\"},{\"heading\":\"ଆପଣଙ୍କ ଟିପ୍ପଣୀ ଏବଂ ମନ୍ତବ୍ୟ ଲେଖନ୍ତୁ\",\"desc\":\"ଆପଣଙ୍କ ଟିପ୍ପଣୀ ଏବଂ ସ୍ମରଣିକା ସେଭ୍ କରନ୍ତୁ\",\"icon\":\"plan_notes_icon\"},{\"heading\":\"କ୍ଲାଉଡରେ 10ଟି ଚାର୍ଟ ସେଭ୍ କରନ୍ତୁ\",\"desc\":\"ଏବେ କ୍ଲାଉଡ୍ ଷ୍ଟୋରେଜରେ 10ଟି ପର୍ଯ୍ୟନ୍ତ ଚାର୍ଟ ସେଭ୍ କରନ୍ତୁ\",\"icon\":\"plan_cloud_icon\"}]}";
    public static final String data_BasicPlanAstrologicalFeaturesAssamese = "{\"plans\":[{\"heading\":\"কুণ্ডলী তৈয়াৰ কৰক\",\"desc\":\"আপোনাৰ ব্যক্তিগত কুণ্ডলী তৈয়াৰ কৰক\",\"icon\":\"plan_kundli_icon\"},{\"heading\":\"কুণ্ডলী মিলান\",\"desc\":\"এতিয়াই আপোনাৰ উপযুক্ত মিল বিচাৰি উলিয়াওক\",\"icon\":\"plan_horoscope_icon\"},{\"heading\":\"আপোনাৰ ডিভাইচত অসীমিত চাৰ্ট সংৰক্ষণ কৰক\",\"desc\":\"লোকেল ডিভাইচত একাধিক জন্ম চাৰ্ট সংৰক্ষণ কৰক\",\"icon\":\"plan_mobile_icon\"},{\"heading\":\"আপোনাৰ টোকা আৰু মন্তব্য লিখক\",\"desc\":\"আপোনাৰ টোকা আৰু সোঁৱৰাই দিয়া বিষয়সমূহ সংৰক্ষণ কৰক\",\"icon\":\"plan_notes_icon\"},{\"heading\":\"ক্লাউডত 10টা চাৰ্ট সংৰক্ষণ কৰক\",\"desc\":\"এতিয়া ক্লাউড ষ্ট'ৰেজত 10টা পর্যন্ত চাৰ্ট সংৰক্ষণ কৰক\",\"icon\":\"plan_cloud_icon\"}]}";
    public static final String data_BasicPlanAstrologicalFeaturesSpanish = "{\"plans\":[{\"heading\":\"Crear Kundli\",\"desc\":\"Crea tu Kundli personalizada\",\"icon\":\"plan_kundli_icon\"},{\"heading\":\"Compatibilidad de horóscopos\",\"desc\":\"Encuentra ahora tu pareja ideal\",\"icon\":\"plan_horoscope_icon\"},{\"heading\":\"Guarda gráficos ilimitados en tu dispositivo\",\"desc\":\"Guarda múltiples cartas natales en el dispositivo local\",\"icon\":\"plan_mobile_icon\"},{\"heading\":\"Escribe tus notas y comentarios\",\"desc\":\"Guarda tus notas y recordatorios\",\"icon\":\"plan_notes_icon\"},{\"heading\":\"Guarda 10 gráficos en la nube\",\"desc\":\"Ahora guarda hasta 10 gráficos en el almacenamiento en la nube\",\"icon\":\"plan_cloud_icon\"}]}";
    public static final String data_BasicPlanAstrologicalFeaturesJapanese = "{\"plans\":[{\"heading\":\"クンドリを作成\",\"desc\":\"あなた専用のクンドリを作成\",\"icon\":\"plan_kundli_icon\"},{\"heading\":\"相性診断\",\"desc\":\"理想の相手を今すぐ見つけましょう\",\"icon\":\"plan_horoscope_icon\"},{\"heading\":\"端末に無制限でチャートを保存\",\"desc\":\"複数の出生チャートを端末に保存できます\",\"icon\":\"plan_mobile_icon\"},{\"heading\":\"メモとコメントを記録\",\"desc\":\"メモやリマインダーを保存できます\",\"icon\":\"plan_notes_icon\"},{\"heading\":\"クラウドに10件のチャートを保存\",\"desc\":\"クラウドストレージに最大10件のチャートを保存できます\",\"icon\":\"plan_cloud_icon\"}]}";
    public static final String data_BasicPlanAstrologicalFeaturesGerman = "{\"plans\":[{\"heading\":\"Kundli erstellen\",\"desc\":\"Erstellen Sie Ihre personalisierte Kundli\",\"icon\":\"plan_kundli_icon\"},{\"heading\":\"Horoskop-Abgleich\",\"desc\":\"Finden Sie jetzt Ihren idealen Partner\",\"icon\":\"plan_horoscope_icon\"},{\"heading\":\"Unbegrenzt Charts auf Ihrem Gerät speichern\",\"desc\":\"Speichern Sie mehrere Geburtshoroskope lokal auf Ihrem Gerät\",\"icon\":\"plan_mobile_icon\"},{\"heading\":\"Notizen und Kommentare schreiben\",\"desc\":\"Speichern Sie Ihre Notizen und Erinnerungen\",\"icon\":\"plan_notes_icon\"},{\"heading\":\"10 Charts in der Cloud speichern\",\"desc\":\"Speichern Sie jetzt bis zu 10 Charts im Cloud-Speicher\",\"icon\":\"plan_cloud_icon\"}]}";
    public static final String data_BasicPlanAstrologicalFeaturesFrench = "{\"plans\":[{\"heading\":\"Créer une kundli\",\"desc\":\"Créez votre kundli personnalisée\",\"icon\":\"plan_kundli_icon\"},{\"heading\":\"Compatibilité des horoscopes\",\"desc\":\"Trouvez dès maintenant votre compatibilité idéale\",\"icon\":\"plan_horoscope_icon\"},{\"heading\":\"Enregistrez un nombre illimité de thèmes sur votre appareil\",\"desc\":\"Conservez plusieurs thèmes de naissance sur l'appareil local\",\"icon\":\"plan_mobile_icon\"},{\"heading\":\"Écrivez vos notes et commentaires\",\"desc\":\"Enregistrez vos notes et rappels\",\"icon\":\"plan_notes_icon\"},{\"heading\":\"Enregistrez 10 thèmes sur le cloud\",\"desc\":\"Enregistrez désormais jusqu'à 10 thèmes sur le stockage cloud\",\"icon\":\"plan_cloud_icon\"}]}";


    public static final String key_GoogleWalletPaymentVisibility = "GoogleWalletPaymentVisibility";
    public static final String key_PaytmPaymentVisibility = "PaytmPaymentVisibility";

    public static final String key_PaytmWalletVisibilityServices = "PaytmVisibilityForServices";
    public static final String key_RazorPayVisibilityServices = "RazorPayVisibilityForServices";

    public static final String key_Phone_One = "PhoneNo_new";
    public static final String key_AskAQuestionHeaderAndFooterVisibility = "AskAQuestionHeaderAndFooterVisibility";
    // Shared Perference
    public static final String customAdvertisementAdsVisibility = "customAdvertisementAdsVisibility";
    public static final String gold_plan_ads = "astrosage-cloud-paid-plan-gold";
    public static final String silver_plan_ads = "astrosage-cloud-paid-plan-silver-new";
    public static final String platinum_plan_ads = "astrosage-cloud-paid-plan-platinum";
    public static final String platinum_plan_one_month = "astrosage-cloud-paid-plan-platinum-one-month";

    public static final String ask_A_Question_Android = "ask-a-question-android";
    public static final String chat_with_astrologer_paid = "chat-with-astrologer-paid";
    public static final String ask_A_Question_For_Marriage_Android = "virtual-ask-a-question-marriage";
    public static final String ask_A_Question_For_Career_Android = "virtual-ask-a-question-career";
    public static final String ask_A_Question_For_Health_Android = "virtual-ask-a-question-health";
    public static final String ask_A_Question_For_House_Android = "virtual-ask-a-question-house";
    public static final String ask_A_Question_For_Telephonic_Consultation_Android = "virtual-ask-a-question-telephonic-consultation";
    public static final String ask_A_Question_Data = "ask_a_question_data";
    public static final String astrosage_tv = "virtual-astrosagetv";
    public static final String astrosage_learn_astrology = "virtual-learn-astrology";
    public static final String live_astrologers = "live-astrologers";


    public static final String RedirectUrlFromAstroShopKey = "RedirectUrlFromAstroShop";
    public static final String RedirectUrlFromAstroShopServicesKey = "RedirectUrlFromAstroShopServices";
    public static final String RedirectUrlFromAstroShopAstrologerKey = "RedirectUrlFromAstroShopAstrologer";
    public static final String SOUCRE_ACTIVITY = "sourceActivity";
    public static final String InterstitialAdId = "InterstitialAdId";
    public static final String BannerAdId = "BannerAdId";
    public static final String ExitScreenBannerAdId = "ExitAdId";
    public static final String KEY_MOBILE_NUMBER = "keyMobileNumber";

    //mahtab

    public static final String ASTROSHOP_SERVICES_DEEP_LINKING = "service";
    public static final String ASTROSHOP_BRIHAT_HOROSCOPE = "astrosage-brihat-horoscope";
    public static final String ASTROSHOP_SERVICES_BRIHAT_HOROSCOPE_DEEP_LINKING = "/service/astrosage-brihat-horoscope";
    public static final String ASTROSHOP_ASTROLOGER_DEEP_LINKING = "astrologer";

    public static final String Ecommerce_Paytm_Purchase = "Paytm Purchase";


    public static final String PAYTM_MARCHENT_ID = "Ojasso36077880907527";

    public static final String ExitScreenCustomImageUrl = "ExitScreenCustomImageUrl";
    public static final String ExitScreenCustomImageClickListener = "ExitScreenCustomImageClickListener";
    public static final String DataComingFromAskAQuesAdvertisementView = "DataComingFromAskAQuesAdvertisementView";
    public static final String Astroshop_Data = "Astroshop_data";
    public static final String Astroshop_Service_Data = "Astroservice_data";
    public static final String Astroshop_Astrologer_Data = "Astrologer_data";


    public static String GOOGLE_ANALYTIC_DEEPLINK_EVENT = "DEEP_LINKS";
    public static String GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_GOLD_PLAN = "Gold_Plan";
    public static String GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_SILVER_PLAN = "Sliver_Plan";
    public static String GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_PLATINUM_PLAN = "Platinum_Plan";

    public static String GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_ASK_A_QUESTION = "Ask_A_Question";
    public static String GOOGLE_ANALYTIC_LABEL_FOR_ASK_A_QUESTION_CHAT = "Ask_A_Question_Chat";
    public static String GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_CHAT_WITH_ASTROLOGER = "Chat_With_Astrologer";
    public static String GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_ASTROSAGE_TV = "Astrosage_Tv";
    public static String GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_LEARN_ASTROLOGY = "Learn_Astrology";
    public static String GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_LIVE_ASTROLGERS = "Live_Astrologers";

    public static String GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_ASK_A_QUESTION_MARRIAGE = "Ask_A_Question_Marriage";
    public static String GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_ASK_A_QUESTION_CAREER = "Ask_A_Question_Career";
    public static String GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_ASK_A_QUESTION_HEALTH = "Ask_A_Question_Health";
    public static String GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_ASK_A_QUESTION_HOUSE = "Ask_A_Question_House";
    public static String GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_ASK_A_QUESTION_TELEPHONIC_CONSULTATION = "Ask_A_Question_Telephonic_Consultation";
    public static String GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_SERVICES = "Astrosage_Services";
    public static String GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_ASTROLOGERS = "Astrologers";
    public static String GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_PRODUCTS = "Astrosage_Products";
    public static String GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_SHAREAPP = "Astrosage_ShareApp";
    public static String GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_YOUTUBE_VIDEO = "Astrosage_Youtube_Video";

    //mahtab start


    public static final String astrosage_big_horscope = "astrosage-big-horoscope-url";
    public static final String astrosage_brihat_horscope = "astrosage-brihat-horoscope-url";

    public static String GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_BIG_HORSCOPE_ACTIVITY = "Big_Horscope_Home";
    public static String GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_BRIHAT_HORSCOPE_ACTIVITY = "Brihat_Horscope_Home";

    public static String GOOGLE_ANALYTIC_ENGISH_SAMPLE_PDF_DOWNLOAD = "Download_Sample_English";
    public static String GOOGLE_ANALYTIC_HINDI_SAMPLE_PDF_DOWNLOAD = "Download_Sample_Hindi";
    public static String GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_BIG_HORSCOPE_SERVICE = "Big_Horscope_Service";
    public static String GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_BIG_HORSCOPE_PRODUCT = "Big_Horscope_Service";
    public static String GOOGLE_ANALYTIC_ASK_A_QUESTION_FOR_MARRIAGE_MATCH_MAKING = "Ask_a_question_for_marriage_match_making";
    public static final String DataComingFromDownloadPdf = "commingfromdownloadpdf";

    public static String GOOGLE_ANALYTIC_BRIHAT_ENGISH_SAMPLE_PDF_DOWNLOAD = "Download_Sample_Brihat_English";
    public static String GOOGLE_ANALYTIC_BRIHAT_HINDI_SAMPLE_PDF_DOWNLOAD = "Download_Sample_Brihat_Hindi";


    public static final String PERMISSION_KEY_STRORAGE = "STORAGE_PERMISSTION_SETTING";
    public static final String PERMISSION_KEY_LOCATION = "LOCATION_PERMISSTION_SETTING";
    public static final String PERMISSION_KEY_CONTACTS = "LOCATION_PERMISSTION_CONTACTS";
    public final static String ASTRO_PRODUCT_DATA = "ASTRO_PRODUCT_DATA";
    public static final String KEY_LATITUDE = "latitude";
    public static final String KEY_LONGITUDE = "longitude";
    public static final String KEY_CITY = "city";
    public static final String KEY_STATE = "state";
    //Topics to registered
    public static final String TOPIC_DOMESTIC = "AK_IND";
    public static final String TOPIC_INTERNATIONAL = "AK_ISD";
    public static final String TOPIC_TEST = "AK_TEST_V1";
    public static final String TOPIC_ALL = "AK_ALL_NEW";
    public static final String TOPIC_UPDATE_LIVE_ASTRO = "AV_UPDATE_LIVE_ASTRO_LIST";
    public static final String TOPIC_ENGLISH = "AK_ENGLISH_NEW";
    public static final String TOPIC_HINDI = "AK_HINDI_NEW";
    public static final String TOPIC_TAMIL = "AK_TAMIL_NEW";
    public static final String TOPIC_MARATHI = "AK_MARATHI_NEW";
    public static final String TOPIC_BANGALI = "AK_BANGALI_NEW";
    public static final String TOPIC_KANNADA = "AK_KANNADA_NEW";
    public static final String TOPIC_TELUGU = "AK_TELUGU_NEW";
    public static final String TOPIC_MALAYALAM = "AK_MALAYALAM_NEW";
    public static final String TOPIC_GUJARATI = "AK_GUJARATI_NEW";
    public static final String TOPIC_ODIA = "AK_ODIA_NEW";
    public static final String TOPIC_SPANISH = "AK_SPANISH_NEW";
    public static final String TOPIC_ASAMMESSE = "AK_ASAMMESSE_NEW";
    public static final String TOPIC_CHINESE = "AK_CHINESE_NEW";
    public static final String TOPIC_JAPANESE = "AK_JAPANESE_NEW";
    public static final String TOPIC_PORTUGUESE = "AK_PORTUGUESE_NEW";
    public static final String TOPIC_GERMAN = "AK_GERMAN_NEW";
    public static final String TOPIC_ITALIAN = "AK_ITALIAN_NEW";
    public static final String TOPIC_FRENCH = "AK_FRENCH_NEW";
    public static final String TOPIC_ASKAQUESTION = "AK_ASKAQUESTION_NEW";
    public static final String TOPIC_SERVICES = "AK_SERVICES_NEW";
    public static final String TOPIC_PRODUCTS = "AK_PRODUCTS_NEW";
    public static final String TOPIC_CLOUDPLAN = "AK_CLOUDPLAN_NEW";
    public static final String TOPIC_GOLDPLAN = "AK_GOLDPLAN_NEW";
    public static final String TOPIC_DHRUVPLAN = "AK_DHRUVPLAN_NEW";
    public static final String TOPIC_ALL_NEED_TO_SEND_TO_GOOGLE_SERVER = "TOPIC_ALL_NEED_TO_SEND_TO_GOOGLE_SERVER"; // default value should be ALL.
    public static final String LANGUAGE_TOPIC_NEED_TO_SEND_TO_GOOGLE_SERVER = "LANGUAGE_TOPIC_NEED_TO_SEND_TO_GOOGLE_SERVER"; // value should be language name like - ENGLISH,HINDI etc.
    public static final String EXTRA_TOPIC_NEED_TO_SEND_TO_GOOGLE_SERVER = "EXTRA_TOPIC_NEED_TO_SEND_TO_GOOGLE_SERVER"; // value should be language name like - ENGLISH,HINDI etc.
    public static final String VERSION_TOPIC_NEED_TO_SEND_TO_GOOGLE_SERVER = "VERSION_TOPIC_NEED_TO_SEND_TO_GOOGLE_SERVER"; // value should be V+Version Code
    public static final String OTHER_TOPIC_NEED_TO_SEND_TO_GOOGLE_SERVER = "OTHER_TOPIC_NEED_TO_SEND_TO_GOOGLE_SERVER";
    public static final String TOPIC_VERSION = "AK_V";
    public static final String TOPIC_IS_ASTROLOGER_5 = "AK_ASTROLOGER_5";
    public static final String TOPIC_IS_ASTROLOGER_10 = "AK_ASTROLOGER_10";
    public static final String TOPIC_CRICKET = "AK_CRICKET_NEW";
    public static final String TOPIC_SHARE_MARKET = "AK_SHARE_MARKET_NEW";
    public static final String TOPIC_BOLLYWOOD = "AK_BOLLYWOOD_NEW";
    public static final String TOPIC_NEW_MAGAZINE = "AK_NEW_MAGAZINE_NEW";
    public static final String TOPIC_POLITICS = "AK_POLITICS_NEW";
    public static final String TOPIC_NOTIFICATION_NOT_CLICK_7 = "AK_NOTIFICATION_NOT_CLICK_7";
    public static final String TOPIC_NOTIFICATION_NOT_CLICK_15 = "AK_NOTIFICATION_NOT_CLICK_15";
    public static final String TOPIC_REGISTERED_USER = "AK_REGISTER";
    public static final String TOPIC_FREE_CHAT_TAKEN_USER = "AK_FREE_CHAT_TAKEN";
    public static final String TOPIC_PAID_CHAT_TAKEN_USER = "AK_PAID_CHAT_TAKEN";
    public static final String TOPIC_USER_CATEGORY = "AK_USER_CATEGORY_";
    public static final String VIEW_ASTROSAGE = "view_astrosage";
    public static final String VIEW_VARTA = "view_varta";
    public static final String VIEW_ANDROID_PHONE = "view_android_phone";

    public static String USER_MAPPING_DATA = "user_mapping_data";
    public static String ISSHOWASTROLOGERDIALOG = "isShow";

    public static String isShowAstrologerDirectoryInMainMenu = "isShowAstrologerDirectoryInMainMenu";
    public static String COUNTNOTIFICATIONCENTER = "CountNotificationCenter";
    public static String USERREGISTEREMAILID = "UserRegisterEmailID";
    public static String SAVEMOBILENUMBER = "saveMobileNumber";
    public static String ISInstanceID = "ISInstanceID";
    public static String showStaticHoroscope = "ShowStaticHoroscope";
    public static String staticHoroscopeDaily = "StaticHoroscopeDaily";
    public static String staticHoroscopeMonthly = "StaticHoroscopeMonthly";
    public static String staticHoroscopeMonthlyV2 = "StaticHoroscopeMonthlyV2";
    public static String staticHoroscopeWeekly = "StaticHoroscopeWeekly";
    public static String staticHoroscopeWeeklyV2 = "StaticHoroscopeWeeklyV2";
    public static String staticHoroscopeWeeklyLove = "StaticHoroscopeWeeklyLove";
    public static String staticHoroscopeWeeklyLoveV2 = "StaticHoroscopeWeeklyLoveV2";
    public static String staticHoroscopeYearly = "StaticHoroscopeYearly";
    public static String paytmVisibilityForProduct = "PaytmVisibilityForProduct";
    public static String ccavenueVisibilityForProducts = "CcavenueVisibilityForProducts";
    public static String razorPayVisibilityForProduct = "RazorPayVisibilityForProduct";
    public static String videoCardVisibilityForHoroscope = "VideoCardVisibilityForHoroscope";
    public static String oldMagazineEnabled = "OldMagazineEnabled";
    public static String isShowPremimumDialog = "IsShowPremimumDialog";
    public static String enableMonthlySubscription = "EnableMonthlySubscription";
    public static String youtubeApiKey = "AstroSage_New_Key";
    public static final String showFreeCallChatPopupType = "showFreeCallChatPopupType";

    public static String currentSession = "Astro";
    public static long currentSessionTime;

    public static final String APPLY_COUPON = API2_BASE_URL + "ac/astroshop/service-coupon-code-v5.asp";


    public static final String GOOGLE_ANALYTIC_GOLD_PLAN_YEARLY_SUCCESS = "GOLD_PLAN_YEARLY";
    public static final String GOOGLE_ANALYTIC_GOLD_PLAN_MONTHLY_SUCCESS = "GOLD_PLAN_MONTHLY";
    public static final String GOOGLE_ANALYTIC_SILVER_PLAN_YEARLY_SUCCESS = "SILVER_PLAN_YEARLY";
    public static final String GOOGLE_ANALYTIC_SILVER_PLAN_MONTHLY_SUCCESS = "SILVER_PLAN_MONTHLY";
    public static final String GOOGLE_ANALYTIC_PLATINUM_PLAN_YEARLY_SUCCESS = "PLATINUM_PLAN_YEARLY";
    public static final String GOOGLE_ANALYTIC_PLATINUM_PLAN_MONTHLY_SUCCESS = "PLATINUM_PLAN_MONTHLY";
    public static final String GOOGLE_ANALYTIC_PLATINUM_PLAN_MONTHLY_OMF_SUCCESS = "PLATINUM_PLAN_MONTHLY_OMF";
    public static final String GOOGLE_ANALYTIC_PLATINUM_PLAN_YEARLY_OMF_SUCCESS = "PLATINUM_PLAN_YEARLY_OMF";

    //end panchang all url

    public final static String HINDU_MONTH_KEY = "hindumonthkey";

    public static final String Astroshop_Data_First_Time = "Astroshop_data_First_Time1";
    public static String GOOGLE_ANALYTIC_Coupon_Applied_Success = "Coupon_Applied_Success";// ADDED
    public static String GOOGLE_ANALYTIC_Coupon_Applied_Failed = "Coupon_Applied_Fail";// ADDED
    public static final String GOOGLE_ANALYTIC_PRODUCT = "PRODUCT";
    public static final String GOOGLE_ANALYTIC_CLOUD = "CLOUD";
    public static final String GOOGLE_ANALYTIC_CHEQUEDD = "PRODUCT_PURCHASED_CHEQUEDD";
    public static final String GOOGLE_ANALYTIC_COD = "PRODUCT_PURCHASED_COD";
    public static final String EmailId_Permission = "EmailId_Permission";


    public static final String PREFS_NAME = "TOPICSPREF";
    public static final String topicListKey = "topiclist";

    public static final String key_messageChatID = "MESSAGECHATID";
    public static final String key_layoutPosition = "ChatLayoutPosition";

    public static final String All_Astrologer_List = "Key_All_Astrologer_List";


    public static final String DAILY_TYPE_COUNT = "0";
    public static final String WEEKLY_TYPE_COUNT = "1";
    public static final String WEEKLY_LOVE_TYPE_COUNT = "2";
    public static final String MONTHLY_TYPE_COUNT = "3";

    public static final String DAILY_TYPE_COUNT_DYNAMIC = "4";
    public static final String WEEKLY_TYPE_COUNT_DYNAMIC = "5";
    public static final String WEEKLY_LOVE_TYPE_COUNT_DYNAMIC = "6";
    public static final String MONTHLY_TYPE_COUNT_DYNAMIC = "7";

    public static final String MAHA_DASHA_TYPE_COUNT = "8";
    public static final String ANTER_DASHA_TYPE_COUNT = "9";
    public static final String SADE_SATI_TYPE_COUNT = "10";


    public static String GOOGLE_ANALYTIC_NOTIFICATION_DASHA_RECEIVE = "DYNAMIC_SADESATI_RECEIVE";
    public static String GOOGLE_ANALYTIC_NOTIFICATION_DASHA_CLICK = "DYNAMIC_SADESATI_CLICK";
    public static String GOOGLE_ANALYTIC_SADE_SATI = "Sadeshati";
    public static String GOOGLE_ANALYTIC_ANTER_DASHA = "AnterDasha";
    public static String GOOGLE_ANALYTIC_MAHA_DASHA = "MahaDasha";

    public static final String GET_FREE_QUESTION_AT_USER_FIRST_INSTALL_KEY = "GET_FREE_QUESTION_AT_USER_FIRST_INSTALL";
    public static final String IS_FIRST_INSTALL_KEY = "isFirstInstall";
    public static final String FIRST_INSTALL_TIME_KEY = "firstInstallTime";

    public static final String LAST_NOTICICATION_CLICK_TIME_KEY = "lastnotificationclicktime";
    public static final String IS_LAST_NOTICICATION_SUBSCRIBE_SENT = "islastnotisubsent";
    public static final String NOTICICATION_CLICK_COUNT = "notificationClickCount";

    public static String YANTRA_DEEP_LINK = buy_astrosage_urls + "/navagrah-yantra/";


    public static String currentLalitude = "currentLalitude";
    public static String currentLongitude = "currentLongitude";
    public static String timeZone = "timeZone";
    public static String timeZoneId = "timeZoneId";
    public static String locality = "locality";
    public static String calenderCurrentTime = "calenderCurrentTime";
    public static String defaultPlace = "New Delhi (India)";
    public static String defaultTimeZoneString = "Asia/Kolkata";
    public static String defaultLatitude = "28.6139";
    public static String defaultLongitude = "77.2090";
    public static String defaultTimeZone = "+5.5";

    public static String PDF_NAME_MATCHING = "Matching#.pdf";
    public static String PDF_NAME_KUNDALI = "AstroSageKundli#.pdf";
    public static String PDF_NAME_PREMIUM_KUNDALI = "PremiumKundli#.pdf";
    public static int PAYMENT_TYPE_SERVICE = 1;
    public static int PAYMENT_TYPE_PRODUCT = 2;

    /*Daily Horoscope*/
    public static String GOOGLE_ANALYTIC_ACTION_PLAY_AUDIO_DAILY = "Daily_Horoscope_Play_Audio";
    public static String GOOGLE_ANALYTIC_ACTION_SHARE_TEXT_DAILY = "Daily_Horoscope_Share_Text";
    public static String GOOGLE_ANALYTIC_ACTION_COPY_TEXT_DAILY = "Daily_Horoscope_Copy_Text";

    /*Weekly Horoscope*/
    public static String GOOGLE_ANALYTIC_ACTION_PLAY_AUDIO_WEEKLY = "Weekly_Horoscope_Play_Audio";
    public static String GOOGLE_ANALYTIC_ACTION_SHARE_TEXT_WEEKLY = "Weekly_Horoscope_Share_Text";
    public static String GOOGLE_ANALYTIC_ACTION_COPY_TEXT_WEEKLY = "Weekly_Horoscope_Copy_Text";

    /*Weekly Love Horoscope*/
    public static String GOOGLE_ANALYTIC_ACTION_PLAY_AUDIO_WEEKLY_LOVE = "Weekly_Love_Horoscope_Play_Audio";
    public static String GOOGLE_ANALYTIC_ACTION_SHARE_TEXT_WEEKLY_LOVE = "Weekly_Love_Horoscope_Share_Text";
    public static String GOOGLE_ANALYTIC_ACTION_COPY_TEXT_WEEKLY_LOVE = "Weekly_Love_Horoscope_Copy_Text";

    /*Monthly Love Horoscope*/
    public static String GOOGLE_ANALYTIC_ACTION_PLAY_AUDIO_MONTHLY = "Monthly_Horoscope_Play_Audio";
    public static String GOOGLE_ANALYTIC_ACTION_SHARE_TEXT_MONTHLY = "Monthly_Love_Horoscope_Share_Text";
    public static String GOOGLE_ANALYTIC_ACTION_COPY_TEXT_MONTHLY = "Monthly_Love_Horoscope_Copy_Text";

    /*Yearly Love Horoscope*/
    public static String GOOGLE_ANALYTIC_ACTION_PLAY_AUDIO_YEARLY = "Yearly_Horoscope_Play_Audio";
    public static String GOOGLE_ANALYTIC_ACTION_SHARE_TEXT_YEARLY = "Yearly_Love_Horoscope_Share_Text";
    public static String GOOGLE_ANALYTIC_ACTION_COPY_TEXT_YEARLY = "Yearly_Love_Horoscope_Copy_Text";

    /*WhatsApp Share Horoscope*/
    public static String GOOGLE_ANALYTIC_ACTION_WHATSAPP_SHARE_DAILY = "Share_Prediction_on_WhatsApp_Daily";
    public static String GOOGLE_ANALYTIC_ACTION_WHATSAPP_SHARE_WEEKLY = "Share_Prediction_on_WhatsApp_Weekly";
    public static String GOOGLE_ANALYTIC_ACTION_WHATSAPP_SHARE_WEEKLY_LOVE = "Share_Prediction_on_WhatsApp_Weekly_Love";
    public static String GOOGLE_ANALYTIC_ACTION_WHATSAPP_SHARE_MONTHLY = "Share_Prediction_on_WhatsApp_Monthly";
    public static String GOOGLE_ANALYTIC_ACTION_WHATSAPP_SHARE_YEARLY = "Share_Prediction_on_WhatsApp_Yearly";
    public static String GOOGLE_ANALYTIC_ACTION_WHATSAPP_SHARE_TOOLBAR = "Share_Prediction_on_WhatsApp_Toolbar";


    public static int PROGRESS_COMPLETE = 100;
    public static int PDF_LIMIT_COMPLETE = 1;
    public static String FOLDER_ASTROSAGE = "Astrosage";
    public static String KEY_PROGRESS = "progress";
    public static String KEY_FNAME = "fname";
    public static String KEY_PDF_SHARE = "isPdfShare";
    public static String KEY_NORTH_CHART = "isNorthChart";
    public static String KEY_CHART_STYLE = "chartStyle";
    public static String KEY_RECEIVER = "receiver";
    public static String KEY_URL = "url";
    public static String KEY_PARAMS = "params";
    public static String KEY_NOTIFICATION_COUNT = "notificationCount";
    public static String KEY_PDF_DOWNLOAD_COUNT = "pdf_download_count";
    public static String KEY_REPORT_TYPE = "key_report_type";

    public static final String REPORT_ERROR_PREF = "REPORT_ERROR_PREF";
    public static int FROM_BABY_NAMES = 0;
    public static String KEY_NAME = "name";
    public static String KEY_GIRL_NAME = "girlName";
    public static String KEY_BOY_SWAR = "boySwar";
    public static String KEY_GIRL_SWAR = "girlSwar";
    public static String KEY_SWAR_LIST = "swarlist";
    public static String KEY_MATCHING_DATA = "matching_data";
    public static String KEY_DOB = "dob";
    public static String KEY_TYPE = "type";
    public static String KEY_NUMROLOGY_MODEL = "numerologyOutputModel";
    public static String KEY_RESULT = "Result";
    public static int ALL_FIELD_REQUIRED = 0;
    public static int NAME_MATCHING_RESULT_NOT_FOUND = 4;
    public static String PDF_FILE = "pdf_path";
    public static String IS_FROM_MATCH_MAKING = "is_From_Match_Making";
    public static String PDF_KUNDLI_TOP_TITLE = "pdf_kundli_topTitle";

    public static String constant_change_language_to_hindi_EN = "Change language to Hindi";
    public static String constant_change_language_to_english_HI_text = "चेंज लैंग्वेज टू इंग्लिश";
    public static String constant_change_language_to_english_HI = "भाषा अंग्रेजी में बदलें";
    public static String constant_change_language_english = "Change language";
    public static String constant_change_language_hindi = "भाषा बदलें";
    public static String app_language_key = "AppLanguagePerf";
    public static String IMAGEKEY = "imagekey";
    public static String GOOGLE_ANALYTIC_ACTION_OPEN_LANGUAGE_SETTING = "OPEN_LANGUAGE_SETTING";
    public static String GOOGLE_ANALYTIC_OPEN_NOTIFICATION = "Open_Notification_Center";
    public static String GOOGLE_ANALYTIC_READ_NOTIFICATION = "Read_Notification";
    public static String GOOGLE_ANALYTIC_BUY_NUMROLOGY_YANTRA = "Buy_Numerology_Yantra";
    public static String GOOGLE_ANALYTIC_NUMROLOGY_CALCULATION = "Numerology_Calculation";


    /*
     * Added by Monika
     * */
    public static String RESULT_CODE_SEVEN = "7";
    public static String RESULT_CODE_THREE = "3";
    public static String RESULT_CODE_ONE = "1";
    public static String RESULT_CODE_TWO = "2";
    public static String RESULT_CODE_SIX = "6";
    public static String RESULT_CODE_FIVE = "5";
    public static String FREE_SHIPPING = "0";

    public static String FIREBASE_EVENT_ITEM_CLICK = "item_click";
    public static String FIREBASE_EVENT_API_CALLED = "api_called";
    public static String FIREBASE_EVENT_PAYMENT_FAILED = "payment_failed";
    public static String FIREBASE_EVENT_WALLET_PAYMENT_FAILED = "wallet_payment_failed";
    public static String FIREBASE_EVENT_WALLTE_PAYMENT = "repot_wallet_payment";
    public static String FIREBASE_EVENT_REPORT_PURCHASE_WITH_WALLET = "report_purchase_with_wallet";
    public static String FIREBASE_EVENT_REPORT_PURCHASE_WITH_REZORPAY = "report_purchase_with_razorpay";
    public static String ORDER_ID_GENERATED = "razorpay_order_id_generated";
    public static String REPORT_PURCHASE_PAYMENT_INTITATE = "razorpay_payment_intitiate";
    public static String FIREBASE_EVENT_OPEN_SCREEN = "open_screen";
    public static String FIREBASE_EVENT_DEEPLINK_CLICK = "deeplink_click";
    public static String FIREBASE_EVENT_NOTIFICATION = "notification";
    public static String FIREBASE_EVENT_SIGN_IN_OUT_SUCESS = "sign_in_out_sucess";
    public static String LOGIN_STATUS = "login_status";

    public static String ANALYTICS_ASTROSHOP_CATEGORY = "ASTROSHOP";
    public static String FIREBASE_LABEL_PARAMS = "label";
    public static String FIREBASE_PURCHASE_SOURCE_PARAMS = "purchase_source";
    public static String FIREBASE_FREE_CHAT_CALL_SUCCESS_EVENT = "free_chat_call_success";
    public static String FIREBASE_FIRST_FREE_CHAT_CALL_SUCCESS_EVENT = "first_free_chat_call_success";
    public static String FIREBASE_SECOND_FREE_CHAT_CALL_SUCCESS_EVENT = "second_free_chat_call_success";
    public static String FIREBASE_NOTIFICATION_DELIVERED_EVENT = "notification_delivered";
    public static String FIREBASE_NOTIFICATION_CLICKED_EVENT = "notification_clicked";

    public static String GOOGLE_ANALYTIC_OPEN_FREE_PDF = "open_free_fifty_plus_pdf_pages";// ADDED
    public static String GOOGLE_ANALYTIC_SPINNER_KUNDALI_ACTIONBAR = "kundli_switch_tab_from_spinner";
    public static String GOOGLE_ANALYTIC_KUNDALI_DOWNLOAD_PDF_ACTIONBAR = "kundli_download_pdf_actionbar";
    public static String GOOGLE_ANALYTIC_KUNDALI_PRINT_PDF_ACTIONBAR = "kundli_download_print_actionbar";
    public static String GOOGLE_ANALYTIC_KUNDALI_ASK_A_QUESTION_ACTIONBAR = "kundli_ask_a_question_actionbar";
    public static String GOOGLE_ANALYTIC_LAUNCH_SHARE_SCREEN = "launch_share_screen";
    public static String GOOGLE_ANALYTIC_KNOW_YOUR_MOON_BY_NAME = "choose_rashi_screen_know_your_mooon_by_name";
    public static String GOOGLE_ANALYTIC_NOTES_DIALOG = "open_notes_dialog";
    public static String GOOGLE_ANALYTIC_YOUTUBE_PLAY_VIDEO_CLICK = "play_video_from_video_tab";
    public static String GOOGLE_ANALYTIC_OPEN_YOUTUBE_PLAYER = "open_youtube_player_video";
    public static String GOOGLE_ANALYTIC_ASKAQUESTION_RAZORPAY = "ASK_A_QUESTION_RAZORPAY";
    public static String GOOGLE_ANALYTIC_APP_ICON_FREE_KUNDLI = "app_icon_shortcut_free_kundli";
    public static String GOOGLE_ANALYTIC_APP_ICON_FREE_MATCH_MAKING = "app_icon_shortcut_free_match_making";
    public static String GOOGLE_ANALYTIC_APP_ICON_FREE_CHAT = "app_icon_shortcut_free_chat";
    public static String GOOGLE_ANALYTIC_APP_ICON_TODAYS_HOROSCOPE = "app_icon_shortcut_today's_horoscope";
    public static String GOOGLE_ANALYTIC_KUNDLI_AI_CHAT_BUTTON_CLICK = "kundli_ai_chat_btn_click";
    public static String GOOGLE_ANALYTIC_KUNDLI_AI_HOME_CHAT_BUTTON_CLICK = "kundli_ai_home_chat_btn_click";
    public static String GOOGLE_ANALYTIC_KUNDLI_AI_HOME_ICON = "clicked_kundli_ai_home_icon";
    public static String GOOGLE_ANALYTIC_KUNDLI_AI_CHAT_SCREEN_OPEN = "opened_kundli_ai_chat_screen";


    public static final String key_SliverPlanAstrologicalFeaturesEnglishUpdate = "SliverPlanAstrologicalFeaturesEnglishV2";
    public static final String key_SliverPlanAstrologicalFeaturesHindiUpdate = "SliverPlanAstrologicalFeaturesHindiV2";
    public static final String key_SliverPlanAstrologicalFeaturesTamilUpdate = "SliverPlanAstrologicalFeaturesTamilV2";
    public static final String key_SliverPlanAstrologicalFeaturesBangaliUpdate = "SliverPlanAstrologicalFeaturesBangaliV2";
    public static final String key_SliverPlanAstrologicalFeaturesMarathiUpdate = "SliverPlanAstrologicalFeaturesMarathiV2";
    public static final String key_SliverPlanAstrologicalFeaturesTeluguUpdate = "SliverPlanAstrologicalFeaturesTeluguV2";
    public static final String key_SliverPlanAstrologicalFeaturesKannadUpdate = "SliverPlanAstrologicalFeaturesKannadaV2";
    public static final String key_SliverPlanAstrologicalFeaturesGujratiUpdate = "SliverPlanAstrologicalFeaturesGujaratiV2";
    public static final String key_SliverPlanAstrologicalFeaturesMalayalamUpdate = "SliverPlanAstrologicalFeaturesMalayalamV2";
    public static final String key_SliverPlanQuestionCount = "SilverFreeChatCount";


    public static final String key_GoldPlanAstrologicalFeaturesEnglishUpdate = "GoldPlanAstrologicalFeaturesEnglishV2";
    public static final String key_GoldPlanAstrologicalFeaturesHindiUpdate = "GoldPlanAstrologicalFeaturesHindiV2";
    public static final String key_GoldPlanAstrologicalFeaturesTamilUpdate = "GoldPlanAstrologicalFeaturesTamilV2";
    public static final String key_GoldPlanAstrologicalFeaturesBangaliUpdate = "GoldPlanAstrologicalFeaturesBangaliV2";
    public static final String key_GoldPlanAstrologicalFeaturesMarathiUpdate = "GoldPlanAstrologicalFeaturesMarathiV2";
    public static final String key_GoldPlanAstrologicalFeaturesTeluguUpdate = "GoldPlanAstrologicalFeaturesTeluguV2";
    public static final String key_GoldPlanAstrologicalFeaturesKannadUpdate = "GoldPlanAstrologicalFeaturesKannadaV2";
    public static final String key_GoldPlanAstrologicalFeaturesGujratiUpdate = "GoldPlanAstrologicalFeaturesGujaratiV2";
    public static final String key_GoldPlanAstrologicalFeaturesMalayalamUpdate = "GoldPlanAstrologicalFeaturesMalayalamV2";
    public static final String key_GoldPlanQuestionCount = "GoldFreeChatCount";
    public static final String key_DhruvPlanQuestionCount = "DhruvFreeChatCount";


    /*public static final String key_PlatinumPlanAstrologicalFeaturesEnglish = "PlatinumPlanAstrologicalFeaturesEnglish";
    public static final String key_PlatinumPlanAstrologicalFeaturesHindi = "PlatinumPlanAstrologicalFeaturesHindi";
    public static final String key_PlatinumPlanAstrologicalFeaturesTamil = "PlatinumPlanAstrologicalFeaturesTamil";
    public static final String key_PlatinumPlanAstrologicalFeaturesBangali = "PlatinumPlanAstrologicalFeaturesBangali";
    public static final String key_PlatinumPlanAstrologicalFeaturesMarathi = "PlatinumPlanAstrologicalFeaturesMarathi";
    public static final String key_PlatinumPlanAstrologicalFeaturesTelugu = "PlatinumPlanAstrologicalFeaturesTelugu";
    public static final String key_PlatinumPlanAstrologicalFeaturesKannad = "PlatinumPlanAstrologicalFeaturesKannada";
    public static final String key_PlatinumPlanAstrologicalFeaturesGujrati = "PlatinumPlanAstrologicalFeaturesGujarati";
    public static final String key_PlatinumPlanAstrologicalFeaturesMalayalam = "PlatinumPlanAstrologicalFeaturesMalayalam";
*/
    public static final String key_PlatinumPlanAstrologicalFeaturesEnglish = "PlatinumPlanAstrologicalFeaturesEnglishV1";
    public static final String key_PlatinumPlanAstrologicalFeaturesHindi = "PlatinumPlanAstrologicalFeaturesHindiV1";
    public static final String key_PlatinumPlanAstrologicalFeaturesTamil = "PlatinumPlanAstrologicalFeaturesTamilV1";
    public static final String key_PlatinumPlanAstrologicalFeaturesBangali = "PlatinumPlanAstrologicalFeaturesBangaliV1";
    public static final String key_PlatinumPlanAstrologicalFeaturesMarathi = "PlatinumPlanAstrologicalFeaturesMarathiV1";
    public static final String key_PlatinumPlanAstrologicalFeaturesTelugu = "PlatinumPlanAstrologicalFeaturesTeluguV1";
    public static final String key_PlatinumPlanAstrologicalFeaturesKannad = "PlatinumPlanAstrologicalFeaturesKannadaV1";
    public static final String key_PlatinumPlanAstrologicalFeaturesGujrati = "PlatinumPlanAstrologicalFeaturesGujaratiV1";
    public static final String key_PlatinumPlanAstrologicalFeaturesMalayalam = "PlatinumPlanAstrologicalFeaturesMalayalamV1";
    public static String DHRUV_PLAN_QUES_COUNT_KEY = "dhruv_plan_question_count_key";
    public static String SILVER_PLAN_QUES_COUNT_KEY = "silver_plan_question_count_key";
    public static String GOLD_PLAN_QUES_COUNT_KEY = "gold_plan_question_count_key";
    public static final String dataPlatinumPlanAstrologicalFeaturesEnglish = "{\"plans\": [{\"heading\":\"1 Premium Kundli Every Month \n(Worth ₹499)\",\"desc\":\"Unlock detailed life predictions with a comprehensive premium report.\",\"icon\": \"plan_download_icon\"},{\"heading\":\"Save Unlimited Charts (Kundli) on Cloud\",\"desc\": \"Gain Access to Unlimited Personalized Charts in Cloud Storage\",\"icon\":\"plan_cloud_icon\"},{\"heading\":\"Ads-Free Experience\",\"desc\":\"Explore the App without Interruption\",\"icon\": \"plan_free_ads_icon\"},{\"heading\": \"All the Features of Basic Membership\",\"desc\":\"Get Access to all the Specifications of the Basic Membership\",\"icon\": \"plan_all_features_icon\"}]}";
    public static final String dataPlatinumPlanAstrologicalFeaturesHindi = "{\"plans\": [{\"heading\":\"1 प्रीमियम कुंडली हर महीने \n(मूल्य ₹499)\",\"desc\":\"एक व्यापक प्रीमियम रिपोर्ट के साथ विस्तृत जीवन भविष्यवाणियों को अनलॉक करें।\",\"icon\": \"plan_download_icon\"},{\"heading\":\"क्लाउड पर असीमित चार्ट (कुंडली) सहेजें\",\"desc\": \"क्लाउड स्टोरेज में असीमित व्यक्तिगत चार्ट तक पहुंच प्राप्त करें।\",\"icon\":\"plan_cloud_icon\"},{\"heading\":\"विज्ञापन-मुक्त अनुभव\",\"desc\":\"बिना किसी रुकावट के ऐप को एक्सप्लोर करें।\",\"icon\": \"plan_free_ads_icon\"},{\"heading\": \"बेसिक सदस्यता की सभी सुविधाएँ\",\"desc\":\"बेसिक सदस्यता की सभी विशिष्टताओं तक पहुंच प्राप्त करें।\",\"icon\": \"plan_all_features_icon\"}]}";
    public static final String dataPlatinumPlanAstrologicalFeaturesTamil = "{\"plans\": [{\"heading\":\"1 பிரீமியம் குண்டலி ஒவ்வொரு மாதமும் \n(மதிப்பு ₹499)\",\"desc\":\"ஒரு விரிவான பிரீமியம் அறிக்கையுடன் விரிவான வாழ்க்கை கணிப்புகளைத் திறக்கவும்.\",\"icon\": \"plan_download_icon\"},{\"heading\":\"மேகக்கணியில் அன்லிமிடெட் ஜாதகங்களை (குண்டலிகளை) சேமிக்கவும்\",\"desc\": \"மேகக்கணி சேமிப்பகத்தில் அன்லிமிடெட் தனிப்பயனாக்கப்பட்ட ஜாதகங்களை அணுகலாம்.\",\"icon\":\"plan_cloud_icon\"},{\"heading\":\"விளம்பரமில்லா அனுபவம்\",\"desc\":\"எந்த இடையூறும் இல்லாமல் பயன்பாட்டை ஆராயவும்.\",\"icon\": \"plan_free_ads_icon\"},{\"heading\": \"அடிப்படை உறுப்பினர் திட்டத்தின் அனைத்து அம்சங்களும்\",\"desc\":\"அடிப்படை உறுப்பினர் திட்டத்தின் அனைத்து விவரக்குறிப்புகளையும் அணுகவும்.\",\"icon\": \"plan_all_features_icon\"}]}";
    public static final String dataPlatinumPlanAstrologicalFeaturesBangali = "{\"plans\": [{\"heading\":\"1 প্রিমিয়াম কুন্ডলী প্রতি মাসে \n(মূল্য ₹499)\",\"desc\":\"একটি ব্যাপক প্রিমিয়াম রিপোর্টের মাধ্যমে বিস্তারিত জীবন ভবিষ্যদ্বাণীগুলি আনলক করুন।\",\"icon\": \"plan_download_icon\"},{\"heading\":\"ক্লাউডে সীমাহীন চার্ট (কুন্ডলী) সংরক্ষণ করুন\",\"desc\": \"ক্লাউড স্টোরেজে সীমাহীন ব্যক্তিগতকৃত চার্ট অ্যাক্সেস করুন\",\"icon\":\"plan_cloud_icon\"},{\"heading\":\"বিজ্ঞাপন-মুক্ত অভিজ্ঞতা\",\"desc\":\"কোনও বাধা ছাড়াই অ্যাপটি এক্সপ্লোর করুন\",\"icon\": \"plan_free_ads_icon\"},{\"heading\": \"বেসিক মেম্বারশিপের সমস্ত বৈশিষ্ট্য\",\"desc\":\"বেসিক মেম্বারশিপের সমস্ত স্পেসিফিকেশন অ্যাক্সেস করুন\",\"icon\": \"plan_all_features_icon\"}]}";
    public static final String dataPlatinumPlanAstrologicalFeaturesMarathi = "{\"plans\": [{\"heading\":\"1 प्रीमियम कुंडली दर महिन्याला \n(किंमत ₹499)\",\"desc\":\"सर्वसमावेशक प्रीमियम अहवालासह विस्तृत जीवन भविष्यवाण्या अनलॉक करा.\",\"icon\": \"plan_download_icon\"},{\"heading\":\"क्लाउडवर अमर्यादित चार्ट (कुंडली) जतन करा\",\"desc\": \"क्लाउड स्टोरेजमध्ये अमर्यादित वैयक्तिकृत चार्टमध्ये प्रवेश मिळवा\",\"icon\":\"plan_cloud_icon\"},{\"heading\":\"जाहिरात-मुक्त अनुभव\",\"desc\":\"कोणत्याही व्यत्ययाशिवाय अॅप एक्सप्लोर करा\",\"icon\": \"plan_free_ads_icon\"},{\"heading\": \"मूळ सदस्यत्वाचे सर्व वैशिष्ट्ये\",\"desc\":\"मूळ सदस्यत्वाच्या सर्व वैशिष्ट्यांमध्ये प्रवेश मिळवा\",\"icon\": \"plan_all_features_icon\"}]}";
    public static final String dataPlatinumPlanAstrologicalFeaturesTelugu = "{\"plans\": [{\"heading\":\"1 ప్రీమియం కుండలి ప్రతి నెల \n(విలువ ₹499)\",\"desc\":\"సమగ్ర ప్రీమియం నివేదికతో వివరణాత్మక జీవిత అంచనాలను అన్‌లాక్ చేయండి.\",\"icon\": \"plan_download_icon\"},{\"heading\":\"క్లౌడ్‌లో అపరిమిత చార్ట్‌లను (కుండలి) సేవ్ చేయండి\",\"desc\": \"క్లౌడ్ నిల్వలో అపరిమిత వ్యక్తిగతీకరించిన చార్ట్‌లకు ప్రాప్యత పొందండి\",\"icon\":\"plan_cloud_icon\"},{\"heading\":\"ప్రకటనలు లేని అనుభవం\",\"desc\":\"ఎటువంటి అంతరాయం లేకుండా యాప్‌ను అన్వేషించండి\",\"icon\": \"plan_free_ads_icon\"},{\"heading\": \"ప్రాథమిక సభ్యత్వం యొక్క అన్ని ఫీచర్లు\",\"desc\":\"ప్రాథమిక సభ్యత్వం యొక్క అన్ని లక్షణాలను పొందండి\",\"icon\": \"plan_all_features_icon\"}]}";
    public static final String dataPlatinumPlanAstrologicalFeaturesKannad = "{\"plans\": [{\"heading\":\"1 ಪ್ರೀಮಿಯಂ ಕುಂಡಲಿ ಪ್ರತಿ ತಿಂಗಳು \n(ಮೌಲ್ಯ ₹499)\",\"desc\":\"ಸಮಗ್ರ ಪ್ರೀಮಿಯಂ ವರದಿಯೊಂದಿಗೆ ವಿವರವಾದ ಜೀವನ ಭವಿಷ್ಯವಾಣಿಗಳನ್ನು ಅನ್‌ಲಾಕ್ ಮಾಡಿ.\",\"icon\": \"plan_download_icon\"},{\"heading\":\"ಕ್ಲೌಡ್‌ನಲ್ಲಿ ಅನಿಯಮಿತ ಚಾರ್ಟ್‌ಗಳನ್ನು (ಕುಂಡಲಿ) ಉಳಿಸಿ\",\"desc\": \"ಕ್ಲೌಡ್ ಸಂಗ್ರಹಣೆಯಲ್ಲಿ ಅನಿಯಮಿತ ವೈಯಕ್ತೀಕರಿಸಿದ ಚಾರ್ಟ್‌ಗಳಿಗೆ ಪ್ರವೇಶ ಪಡೆಯಿರಿ\",\"icon\":\"plan_cloud_icon\"},{\"heading\":\"ಜಾಹೀರಾತು-ಮುಕ್ತ ಅನುಭವ\",\"desc\":\"ಯಾವುದೇ ಅಡೆತಡೆಗಳಿಲ್ಲದೆ ಅಪ್ಲಿಕೇಶನ್ ಅನ್ನು ಅನ್ವೇಷಿಸಿ\",\"icon\": \"plan_free_ads_icon\"},{\"heading\": \"ಮೂಲ ಸದಸ್ಯತ್ವದ ಎಲ್ಲಾ ವೈಶಿಷ್ಟ್ಯಗಳು\",\"desc\":\"ಮೂಲ ಸದಸ್ಯತ್ವದ ಎಲ್ಲಾ ವೈಶಿಷ್ಟ್ಯಗಳಿಗೆ ಪ್ರವೇಶ ಪಡೆಯಿರಿ.\",\"icon\": \"plan_all_features_icon\"}]}";
    public static final String dataPlatinumPlanAstrologicalFeaturesGujrati = "{\"plans\": [{\"heading\":\"1 પ્રીમિયમ કુંડળી દર મહિને \n(કિંમત ₹499)\",\"desc\":\"એક વ્યાપક પ્રીમિયમ રિપોર્ટ સાથે વિગતવાર જીવન ભવિષ્યવાણીઓને અનલૉક કરો.\",\"icon\": \"plan_download_icon\"},{\"heading\":\"ક્લાઉડ પર અમર્યાદિત ચાર્ટ (કુંડળી) સાચવો\",\"desc\": \"ક્લાઉડ સ્ટોરેજમાં અમર્યાદિત વ્યક્તિગત કરેલા ચાર્ટ્સની ઍક્સેસ મેળવો\",\"icon\":\"plan_cloud_icon\"},{\"heading\":\"જાહેરાત-મુક્ત અનુભવ\",\"desc\":\"કોઈપણ અવરોધ વિના એપ્લિકેશનનું અન્વેષણ કરો\",\"icon\": \"plan_free_ads_icon\"},{\"heading\": \"મૂળભૂત સભ્યપદની તમામ સુવિધાઓ\",\"desc\":\"મૂળભૂત સભ્યપદની તમામ વિશિષ્ટતાઓની ઍક્સેસ મેળવો\",\"icon\": \"plan_all_features_icon\"}]}";
    public static final String dataPlatinumPlanAstrologicalFeaturesMalayalam = "{\"plans\": [{\"heading\":\"1 പ്രീമിയം കുണ്ഡലി എല്ലാ മാസവും \n(മൂല്യം ₹499)\",\"desc\":\"സമഗ്രമായ പ്രീമിയം റിപ്പോർട്ട് ഉപയോഗിച്ച് വിശദമായ ജീവിത പ്രവചനങ്ങൾ അൺലോക്ക് ചെയ്യുക.\",\"icon\": \"plan_download_icon\"},{\"heading\":\"ക്ലൗഡിൽ അൺലിമിറ്റഡ് ചാർട്ടുകൾ (ജാതകം) സംരക്ഷിക്കുക\",\"desc\": \"ക്ലൗഡ് സ്റ്റോറേജിൽ അൺലിമിറ്റഡ് വ്യക്തിഗതമാക്കിയ ചാർട്ടുകളിലേക്ക് പ്രവേശനം നേടുക\",\"icon\":\"plan_cloud_icon\"},{\"heading\":\"പരസ്യരഹിത അനുഭവം\",\"desc\":\"തടസ്സങ്ങളില്ലാതെ ആപ്പ് പര്യവേക്ഷണം ചെയ്യുക\",\"icon\": \"plan_free_ads_icon\"},{\"heading\": \"അടിസ്ഥാന അംഗത്വത്തിന്റെ എല്ലാ സവിശേഷതകളും\",\"desc\":\"അടിസ്ഥാന അംഗത്വത്തിന്റെ എല്ലാ പ്രത്യേകതകളിലേക്കും പ്രവേശനം നേടുക\",\"icon\": \"plan_all_features_icon\"}]}";
    public static final String dataPlatinumPlanAstrologicalFeaturesOdia = "{\"plans\":[{\"heading\":\"ପ୍ରତି ମାସ 1 ପ୍ରିମିୟମ୍ କୁଣ୍ଡଳୀ \n(ମୂଲ୍ୟ ₹499)\",\"desc\":\"ଏକ ସମ୍ପୂର୍ଣ୍ଣ ପ୍ରିମିୟମ୍ ରିପୋର୍ଟ ସହିତ ବିସ୍ତୃତ ଜୀବନ ପୂର୍ବାନୁମାନ ଅନଲକ୍ କରନ୍ତୁ\",\"icon\":\"plan_download_icon\"},{\"heading\":\"କ୍ଲାଉଡରେ ଅସୀମିତ ଚାର୍ଟ (କୁଣ୍ଡଳୀ) ସେଭ୍ କରନ୍ତୁ\",\"desc\":\"କ୍ଲାଉଡ୍ ଷ୍ଟୋରେଜରେ ଅସୀମିତ ବ୍ୟକ୍ତିଗତ ଚାର୍ଟର ଆକ୍ସେସ୍ ପାଆନ୍ତୁ\",\"icon\":\"plan_cloud_icon\"},{\"heading\":\"ବିଜ୍ଞାପନମୁକ୍ତ ଅନୁଭବ\",\"desc\":\"କୌଣସି ବାଧା ବିନା ଆପ୍‌କୁ ଅନୁସନ୍ଧାନ କରନ୍ତୁ\",\"icon\":\"plan_free_ads_icon\"},{\"heading\":\"ବେସିକ୍ ସଦସ୍ୟତାର ସମସ୍ତ ବୈଶିଷ୍ଟ୍ୟ\",\"desc\":\"ବେସିକ୍ ସଦସ୍ୟତାର ସମସ୍ତ ବିବରଣୀର ଆକ୍ସେସ୍ ପାଆନ୍ତୁ\",\"icon\":\"plan_all_features_icon\"}]}";
    public static final String dataPlatinumPlanAstrologicalFeaturesAssamese = "{\"plans\":[{\"heading\":\"প্ৰতি মাহে 1 টা প্ৰিমিয়াম কুণ্ডলী \n(মূল্য ₹499)\",\"desc\":\"এখন বিস্তৃত জীৱন ভৱিষ্যদ্বাণীসমূহ এটা সমগ্ৰ প্ৰিমিয়াম ৰিপ'ৰ্টৰ সহায়ত আনলক কৰক\",\"icon\":\"plan_download_icon\"},{\"heading\":\"ক্লাউডত অসীমিত চাৰ্ট (কুণ্ডলী) সংৰক্ষণ কৰক\",\"desc\":\"ক্লাউড ষ্ট'ৰেজত অসীমিত ব্যক্তিগতকৃত চাৰ্টৰ এক্সেছ লাভ কৰক\",\"icon\":\"plan_cloud_icon\"},{\"heading\":\"বিজ্ঞাপন-মুক্ত অভিজ্ঞতা\",\"desc\":\"কোনো বাধা নোহোৱাকৈ এপটো অন্বেষণ কৰক\",\"icon\":\"plan_free_ads_icon\"},{\"heading\":\"বেচিক সদস্যতাৰ সকলো সুবিধা\",\"desc\":\"বেচিক সদস্যতাৰ সকলো বিশেষত্বৰ এক্সেছ লাভ কৰক\",\"icon\":\"plan_all_features_icon\"}]}";
    public static final String dataPlatinumPlanAstrologicalFeaturesSpanish = "{\"plans\":[{\"heading\":\"1 Kundli premium cada mes \\n(valor ₹499)\",\"desc\":\"Desbloquea predicciones detalladas de vida con un informe premium completo.\",\"icon\":\"plan_download_icon\"},{\"heading\":\"Guarda gráficos (Kundli) ilimitados en la nube\",\"desc\":\"Obtén acceso a gráficos personalizados ilimitados en el almacenamiento en la nube\",\"icon\":\"plan_cloud_icon\"},{\"heading\":\"Experiencia sin anuncios\",\"desc\":\"Explora la app sin interrupciones\",\"icon\":\"plan_free_ads_icon\"},{\"heading\":\"Todas las funciones de la membresía básica\",\"desc\":\"Obtén acceso a todas las funciones de la membresía básica\",\"icon\":\"plan_all_features_icon\"}]}";
    public static final String dataPlatinumPlanAstrologicalFeaturesJapanese = "{\"plans\":[{\"heading\":\"毎月プレミアムクンドリ1件 \\n(価値 ₹499)\",\"desc\":\"充実したプレミアムレポートで、詳細な人生予測を解放します。\",\"icon\":\"plan_download_icon\"},{\"heading\":\"クラウドに無制限のチャート（クンドリ）を保存\",\"desc\":\"クラウドストレージ上の無制限のパーソナライズドチャートにアクセスできます\",\"icon\":\"plan_cloud_icon\"},{\"heading\":\"広告なしの快適な体験\",\"desc\":\"中断なくアプリを利用できます\",\"icon\":\"plan_free_ads_icon\"},{\"heading\":\"ベーシック会員の全機能\",\"desc\":\"ベーシック会員のすべての機能を利用できます\",\"icon\":\"plan_all_features_icon\"}]}";
    public static final String dataPlatinumPlanAstrologicalFeaturesGerman = "{\"plans\":[{\"heading\":\"1 Premium-Kundli pro Monat \\n(Wert ₹499)\",\"desc\":\"Schalten Sie detaillierte Lebensprognosen mit einem umfassenden Premium-Bericht frei.\",\"icon\":\"plan_download_icon\"},{\"heading\":\"Unbegrenzt Charts (Kundli) in der Cloud speichern\",\"desc\":\"Erhalten Sie Zugriff auf unbegrenzt personalisierte Charts im Cloud-Speicher\",\"icon\":\"plan_cloud_icon\"},{\"heading\":\"Werbefreies Erlebnis\",\"desc\":\"Entdecken Sie die App ohne Unterbrechungen\",\"icon\":\"plan_free_ads_icon\"},{\"heading\":\"Alle Funktionen der Basic-Mitgliedschaft\",\"desc\":\"Erhalten Sie Zugriff auf alle Funktionen der Basic-Mitgliedschaft\",\"icon\":\"plan_all_features_icon\"}]}";
    public static final String dataPlatinumPlanAstrologicalFeaturesFrench = "{\"plans\":[{\"heading\":\"1 kundli premium chaque mois \\n(valeur 499 ₹)\",\"desc\":\"Débloquez des prévisions de vie détaillées grâce à un rapport premium complet.\",\"icon\":\"plan_download_icon\"},{\"heading\":\"Enregistrez un nombre illimité de thèmes (kundli) sur le cloud\",\"desc\":\"Accédez à un nombre illimité de thèmes personnalisés sur le stockage cloud\",\"icon\":\"plan_cloud_icon\"},{\"heading\":\"Expérience sans publicité\",\"desc\":\"Explorez l'application sans interruption\",\"icon\":\"plan_free_ads_icon\"},{\"heading\":\"Toutes les fonctionnalités de l'abonnement Basique\",\"desc\":\"Accédez à toutes les fonctionnalités de l'abonnement Basique\",\"icon\":\"plan_all_features_icon\"}]}";

    public static String FIREBASE_EVENT_DAILY_HOROSCOPE_PLAY_VIDEO = "daily_horoscope_play_video";
    public static final String ASTROSAGE_ASK_QUESTION_TAG = "Ask a Question";

    public final static String IS_USER_HAS_PLAN = "is_user_has_plan";
    public final static String GOLD_PLAN_TEXT = "gold";
    public final static String SILVER_PLAN_TEXT = "silver";
    public final static String PLATINUM_PLAN_TEXT = "platinum";
    public final static String FROM_SERVICE_TEXT = "service";
    public final static String FROM_PRODUCT_TEXT = "product";
    public final static String KEY_KUNDALI_DETAILS = "beanHoroPersonalInfo";
    public final static String KEY_NOTIFICATION_LINK = "link";
    public final static String KEY_NOTIFICATION_IMG_URL = "imgurl";
    public final static String KEY_NOTIFICATION_EXTRA = "extradata";

    public final static int NUMEROLOGY_MASTER11 = 11;
    public final static int NUMEROLOGY_MASTER22 = 22;
    public final static int NUMEROLOGY_KARMIC13 = 13;
    public final static int NUMEROLOGY_KARMIC14 = 14;
    public final static int NUMEROLOGY_KARMIC16 = 16;
    public final static int NUMEROLOGY_KARMIC19 = 19;

    public static String FIREBASE_SIGNUP_VIA = "email_id";
    public static final String CONSULT_PREMIMUM_DIALOG_PREF_NAME = "CONSULT_PREMIMUM_DIALOG_PREF";
    public static final String CONSULT_PREMIMUM_DIALOG_OPEN_DATE = "consult_premimum_dialog_open_date";
    public static final String AUTO_OPEN_LOGIN_SCREEN_PREF_NAME = "auto_open_login_screen_pref";
    public static final String AUTO_OPEN_LOGIN_SCREEN = "auto_open_login_screen";
    public static final String FOLOW_TOPIC_SUBSCRIBE_DATE = "FOLOW_TOPIC_SUBSCRIBE_DATE";

    public static final String SILVER_PLAN_DIALOG_PREF_NAME = "SILVER_PLAN_DIALOG_PREF";
    public static final String SILVER_PLAN_DIALOG_IS_OPEN = "silver_plan_dialog_is_open";
    public static final String SILVER_PLAN_DIALOG_IS_OPEN_DATE_SAME = "silver_plan_dialog_is_open_date_same";
    public static final String SILVER_PLAN_DIALOG_OPEN_DATE = "silver_plan_dialog_open_date";

    public static final String FROM_CURRENT_DATE = "currentDate";
    public static final int WEEK_DAYS = 7;
    public static final int ONE_DAY = 1;

    public static String LAGNA_NATURE_MOVABLE = "1";
    public static String LAGNA_NATURE_FIXED = "2";
    public static String LAGNA_NATURE_DUAL = "3";
    public static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    public static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = 5000;
    public static final String SKU_SILVER_PLAN_YEAR = "silver_plan_year";
    public static final String SKU_SILVER_PLAN_MONTH = "silver_plan_month";
    public static final String SKU_GOLD_PLAN_YEAR = "gold_plan_year";
    public static final String SKU_GOLD_PLAN_MONTH = "gold_plan_month";
    public static final String SKU_PLATINUM_PLAN_YEAR = "platinum_plan_year";
    public static final String SKU_PLATINUM_PLAN_MONTH = "platinum_plan_month";
    public static final String SKU_PLATINUM_PLAN_MONTH_OMF = "platinum_plan_month_omf";
    public static final String SKU_PLATINUM_PLAN_YEAR_OMF = "platinum_plan_year_omf";

    public static String CHANGE_PASSWORD_BTN_CLICK = "change_password_btn";
    public static String CHANGE_PASSWORD_SUBMIT_BTN_CLICK = "change_password_submit_btn";
    public static String CONTACT_DETAIL_SUBMIT_BTN_CLICK = "contact_detail_submit_btn";
    public static String SUBSCRIPTION_DIALOG_OPEN = "subscription_dialog_open";
    public static String SUBSCRIPTION_DIALOG_BUYNOW_BTN_CLICK = "subscription_dialog_buy_now_btn";
    public static String SUBSCRIPTION_DIALOG_CROSS_BTN_CLICK = "subscription_dialog_cross_btn";
    public static String CONSULT_PREMIMUM_DIALOG_CROSS_BTN_CLICK = "consult_premimum_dialog_cross_btn";
    public static String CONSULT_PREMIMUM_DIALOG_CROSS_BTN_CLICK_AI = "consult_premimum_dialog_cross_btn_ai";
    public static String CONSULT_PREMIMUM_DIALOG_CALL_BTN_CLICK = "consult_premimum_dialog_call_btn";
    public static String CONSULT_PREMIMUM_DIALOG_CALL_BTN_CLICK_AI = "consult_premimum_dialog_call_btn_ai";
    public static String AI_CHAT_BTN_CLICK_AI_RAMAN = "ai_chat_btn_click_ai_raman";
    public static String AI_CHAT_BTN_CLICK_AI_ANITA_JHA = "ai_chat_btn_click_ai_anita_jha";
    public static String AI_CHAT_BTN_CLICK_AI_KRISHNAMURTI = "ai_chat_btn_click_ai_krishnamurti";
    public static String AI_CHAT_BTN_CLICK_AI_MOM_THE_ASTROLOGER = "ai_chat_btn_click_ai_mom_the_astrologer";
    public static String AI_CHAT_BTN_CLICK_AI_ACHARYA_JOSI = "ai_chat_btn_click_ai_acharya_josi";

    public static String CONSULT_PREMIMUM_GET_RANDOM_AI_ASTROLOGER = "consult_premimum_get_random_ai_astrologer";
    public static String CONSULT_PREMIMUM_DIALOG_OPEN = "consult_premimum_dialog_open";
    public static String CONSULT_PREMIMUM_DIALOG_OPEN_AI = "consult_premimum_dialog_open_ai";

    public static String NUMEROLOGY_EVENT = "numerology";
    public static String[] GOOGLE_ANALYTIC_NUMEROLOGY = {"Key Points", "Radical Number", "Destiny Number"
            , "Name Number", "Auspicious Place", "Health"
            , "Auspicious Time", "Career Choices", "Fasts & Remedies", "Yantra"};

    public static String OPEN_APP_RATE_DIALOG = "open_app_rate_dialog";
    public static String DIALOG_RATE_NOW_CLICK = "dialog_rate_now_click";
    public static String DIALOG_RATE_NOW_CANCEL_CLICK = "dialog_rate_now_cancel_click";

    public static String IN_APP_freeTrialPeriod = "freeTrialPeriod";

    public static final String CHANGE_LANGUAGE_FOR_DRAWER = "change_language_drawer";

    public static final String OPEN_MY_APPOINTMENT_FROM_BOOKING_CONFIRMATION = "open_my_appointment_from_booking_confirmation";
    public static final String OPEN_TOPUP_RECHARGE_AFTER_LIMIT_EXCEED = "open_topup_recharge_after_limit_exceed";
    public static final String CANCEL_TOPUP_RECHARGE_AFTER_LIMIT_EXCEED = "cancel_topup_recharge_after_limit_exceed";
    public static final String UPDATE_PLAN_AFTER_TEN_CHARTS_BUYNOW = "update_plan_after_ten_charts_buynow";
    public static final String UPDATE_PLAN_AFTER_TEN_CHARTS_BUYNOW_FOR_PLUS_PLAN = "update_plan_after_ten_charts_for_plus_plan";
    public static final String UPDATE_PLAN_AFTER_TEN_CHARTS_BUYNOW_FOR_DHRUV_PLAN = "update_plan_after_ten_charts_for_dhruv_plan";
    public static final String UPDATE_PLAN_AFTER_TEN_CHARTS_PROCEED = "update_plan_after_ten_charts_proceed";
    public static final String KUNDLI_CLOUD_PLAN = "kundli_cloud_plan";
    public static final String KUNDLI_CLOUD_PLAN_CLICK = "kundli_cloud_plan_click";
    public static final String PLAN_UPGRADE_FOR_SILVER = "plan_upgrade_for_silver";
    public static final String PLAN_UPGRADE_FOR_GOLD = "plan_upgrade_for_gold";
    public static final String PLAN_UPGRADE_FOR_BASIC = "plan_upgrade_for_basic";
    public static final String NAVIGATION_DRAWER_UPGRADE_PLAN = "navigation_drawer_upgrade_plan";
    public static final String CURRENCY_INDIA = "INR";
    public static final String FIREBASE_VIEW_ITEM_LIST = "view_item_list";
    public static final String FIREBASE_VIEW_ITEM = "view_item";
    public static final String FIREBASE_ADD_TO_CART = "add_to_cart";
    public static final String LAB_CERTIFIED_REMOVE = "-  Lab Certified";
    public static final String LAB_CERTIFIED_BRAC_REMOVE = "- Lab Certified";
    public static final String ACTIVITYUPDATEAFTERPLANPURCHASE = "activityupdateafterplanpurchase";
    public static int PLAN_PURCHASE_ID = 1;
    public static boolean isFromPlanPurchaseActivity = false;
    public static final String BROADCAST_PLAN_PURCHASE = "GET_PLAN_VERIFICATION_DETAIL";
    public static final String PLAN_VERIFY = "verify";
    public static final String PLAN_NOT_VERIFY = "not_verify";
    public static final String KEY_PLAN_DATA = "plan_data";
    public static final String KEY_USER_ID = "us";
    public static final String KEY_EMAIL_ID = "ma";
    public static final String KEY_PASSWORD = "pw";
    public static final String KEY_AS_USER_ID = "asus";
    public static final String KEY_OLD_PASSWORD = "opw";
    public static final String KEY_NEW_PASSWORD = "npw";


    public static final String astrosage_services_urls = astrosage_offers_urls + "/reports-pdf/";


    public static final String astrosage_shop_url = "https://astrosage.shop/";
    public static final String learn_astrology_url = "https://www.astrosage.com/learn-astrology ";
    public static final String numrology_url = "https://www.astrosage.com/numerology";
    public static final String horoscope_url = "https://www.astrosage.com/horoscope";
    public static final String match_making_url = "https://www.astrosage.com/match-making";
    public static final String astrosage_youtube_url = "http://www.youtube.com/";
    public static final String astrosage_topics_httpurl = "http://horoscope.astrosage.com/";
    public static final String astrology_topics_httpurl = "http://astrology.astrosage.com/";
    public static final String jyotish_topics_httpurl = "http://jyotish.astrosage.com/";
    public static final String politicsTopicUrl = "http://www.astrosage.com/virtual/notification/politics";
    public static final String bollywoodTopicUrl = "http://www.astrosage.com/virtual/notification/bollywood";
    public static final String shareMarketTopicUrl = "http://www.astrosage.com/virtual/notification/sharemarket";
    public static final String cricketTopicUrl = "http://www.astrosage.com/virtual/notification/cricket";
    public static final String newMagazineTopicUrl = "http://www.astrosage.com/virtual/notification/newmagazine";
    public static final String astrosage_offers_url = "http://www.astrosage.com/offer";
    public static String openShareChartDeepLink = "http://k.astrosage.com";
    public static final String buy_astrosage_url = "http://buy.astrosage.com";
    public static final String astrosage_shareapp_url = "http://www.astrosage.com/virtual/shareapp";
    public static final String astrosage_services_url = astrosage_offers_url + "/reports-pdf/";
    public static final String astrosage_varta_join_url = "https://vartaapi.astrosage.com/join-request";
    public static final String gPlusUrlLatest = "https://go.astrosage.com/akwa";

    public static final String KEY_PLAN_ID = "pd";
    public static final String SHOW_SILVER_PLAN_TAB = "show_silver_plan";
    public static final String SHOW_GOLD_PLAN_TAB = "show_gold_plan";
    public static boolean isShowSilverPlanTab = false;
    public static boolean isShowGoldPlanTab = false;


    public static String purchaseSource = "";
    public static final String VARSHFAL_START_DATE = "startingAPYear";
    public static final String VARSHFAL_YEAR_NO = "yearOfAP";
    //public static String astroShopServiceList = "http://192.168.1.177/ac/astroshop/service-list-v11.asp";//checked
    //public static String astroShopServiceAskaQuestion ="http://192.168.1.177/ac/astroshop/service-list-customized-v6.asp?";//checked
    //public static String VERIFYING_LOGIN_FIRST_TIME_AFTER_PURCHASE_PLAN ="http://192.168.1.177/as/userlogincheckV10.asp";
    //public static String VERIFYING_LOGIN ="http://192.168.1.177/as/userlogincheckV11.asp";
    //public static final String GENRATE_ORDER_ASKQUE ="http://192.168.1.177/ac/astroshop/service-order-paytm-v6.asp";//checked
    //public static final String APPLY_COUPON ="http://192.168.1.177/ac/astroshop/service-coupon-code-v5.asp";//checked
    //public static String REGISTRATION_USER_FIRST_TIME_AFTER_PURCHASE_PLAN ="http://192.168.1.177//as/usersignupV10.asp";
    //public static String bigHorscopeWebURl ="http://192.168.1.177/ac/astroshop/big-horoscope-v6.asp";//checked
    //public static String genrateOrder ="http://192.168.1.177/ac/astroshop/product-order-v8.asp";//unchecked
    //public static String genrateOrderForCart ="http://192.168.1.177/ac/astroshop/product-cart-order-v7.asp";//checked
    //public static String astroShopItemsLive ="http://192.168.1.177/ac/astroshop/product-listv11.asp";//checked
    //public static final String ASTROSHOP_CARTSHIPPING_COST ="http://192.168.1.177/ac/astroshop/product-cart-shipping-price-v6.asp";//checked
    //public static String astroShopcountryListLive ="http://192.168.1.177/ac/astroshop/country-shipping-price-v7.asp";//checked
    //public static String brihatHorscopeWebURl ="http://192.168.1.177/ac/astroshop/Brihat-horoscope-service-product-desc-v2.asp";//checked
    public final static int MUHURAT_TOTAL_MONTH_COUNT = 14;
    public final static int MONTH_IN_YEAR = 12;
    public static String brihatForFreePdfHoroscope = ASTROSAGE_BASE_URL + "pdf/premium-kundli.pdf";
    public static String brihatForFreePdfHoroscopeHi = ASTROSAGE_BASE_URL + "pdf/premium-kundli-hi.pdf";
    public static String brihatForFreePdfHoroscopeGu = ASTROSAGE_BASE_URL + "pdf/premium-kundli-gu.pdf";
    public static String brihatForFreePdfHoroscopeBn = ASTROSAGE_BASE_URL + "pdf/premium-kundli-bn.pdf";
    public static String brihatForFreePdfHoroscopeMr = ASTROSAGE_BASE_URL + "pdf/premium-kundli-mr.pdf";
    public static String brihatForFreePdfHoroscopeMl = ASTROSAGE_BASE_URL + "pdf/premium-kundli-ml.pdf";
    public static String brihatForFreePdfHoroscopeTa = ASTROSAGE_BASE_URL + "pdf/premium-kundli-ta.pdf";
    public static String brihatForFreePdfHoroscopeTe = ASTROSAGE_BASE_URL + "pdf/premium-kundli-te.pdf";
    public static String brihatForFreePdfHoroscopeKn = ASTROSAGE_BASE_URL + "pdf/premium-kundli-kn.pdf";


    public final static String PLATINUM_SERVICE_ID = "153";
    public final static int VALUE_EIGHT_COUNT = 8;

    public static final int ARIES_RASHI = 1;
    public static final int TAURUS_RASHI = 2;
    public static final int GEMINI_RASHI = 3;
    public static final int CANCER_RASHI = 4;
    public static final int LEO_RASHI = 5;
    public static final int VIRGO_RASHI = 6;
    public static final int LIBRA_RASHI = 7;
    public static final int SCORPIO_RASHI = 8;
    public static final int SAGITTARIUS_RASHI = 9;
    public static final int CAPRICORN_RASHI = 10;
    public static final int AQUARIUS_RASHI = 11;
    public static final int PISCES_RASHI = 12;

    //public static String printCategoryUrl = API2_BASE_URL + "as/module-names-to-print-pdf.asp?";
    public static String printCategoryUrl = JAPI_BASE_URL + "module-names-to-print-pdf.asp?";
    //public static String printCategoryUrl = "http://192.168.1.198:8080/module-names-to-print-pdf.asp?";

    public static final String BASIC_PLAN_PDF_PAGE_COUNT = "50";
    public static final String SILVER_PLAN_PDF_PAGE_COUNT = "100";
    public static final String GOLD_PLAN_PDF_PAGE_COUNT = "100";
    public static final String DHRUV_PLAN_PDF_PAGE_COUNT = "200";

    public static final String FREE_FIFTY = "free_fifty";
    public static final String HUNDRED = "hundred";
    public static final String TWO_HUNDRED = "two_hundred";
    public static final String SCREEN_ID_DHRUV = "screen_id_dhruv";
    public static final String RAZORPAY_ORDERID_URL = buy_astrosage_urls + "/razorpay/generaterazorpayorderid";
    public static final String RAZORPAY_SUBSCRIPTION_ID_URL = buy_astrosage_urls + "/RazorpayCreateSubscriptionServlet";

    public static final int COGNI_ASTRO_GRADE10 = 0;
    public static final int COGNI_ASTRO_GRADE12 = 1;
    public static final int COGNI_ASTRO_PROFESSIONAL = 2;
    public static final int TOTAL_RASHI_COUNT = 12;

    public static String GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_DHRUV_ACTIVITY = "Open_Dhruv_Virtual_Url";
    public static String GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_COGNI_ASTRO_ACTIVITY = "Cogni_Astro_Home";
    public static String GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_VARTA_USER_ACTIVITY = "Varta_User_Home_Call";
    public static String GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_VARTA_USER_ACTIVITY_CHAT = "Varta_User_Home_Chat";
    public static String GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_VARTA_USER_ACTIVITY_AI_CHAT = "Varta_User_Home_AI_Chat";
    public static String GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_JOIN_VARTA_ACTIVITY = "Open_Join_Varta_Virtual_Url";
    public static String GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_ACT_LEARN_ACTIVITY = "open_act_learn_astrology";
    public static String GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_NUMOROLOGY_ACTIVITY = "open_numorology";
    public static String GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_HOME_MATCH_MAKING_ACTIVITY = "open_home_match_making";
    public static String GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_HOROSCOPE_HOME_ACTIVITY = "open_horoscope_home";


    public static final String astrosage_cogni_astro = "astrosage-cogni-astro-url";
    public static final String talk_to_astrologers = "talk-to-astrologers";
    public static final String chat_with_astrologers = "chat-with-astrologers";
    public static final String open_ai_astrologers = "open-ai-astrologers";
    public static final String chat_with_kundli_ai = "chat-with-kundli-ai";

    public static final String astrosage_dhruv_virtual_url = "astrosage-dhruv-url";
    public static final String astrosage_dhruv_pop_virtual_url = "astrosage-dhruv-popup-url";
    public static final String astrosage_join_varta_virtual_url = "astrosage-join-varta-url";
    public static String GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_COGNI_ASTRO_SERVICE_GRADE10 = "cogni_astro_service_grade10";
    public static String GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_COGNI_ASTRO_SERVICE_GRADE12 = "cogni_astro_service_grade12";
    public static String GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_COGNI_ASTRO_SERVICE_PROF = "cogni_astro_service_professionals";
    public static final String APP_PREFS_UserWantArudhException = "arudhexception";

    public static final String FIRST_LOGIN_AFTER_PLAN_PURCHASED = "firstLoginAfterPurchased";
    public static final String OPERATION_NAME_LOGIN = "login";
    public static final String OPERATION_NAME_SIGNUP = "signup";
    public static final String OPERATION_NAME_SOCIALMEDIA = "socialmedia";
    public static final String REG_SOURCE_ANDROID = "android";
    public static final String REG_SOURCE_ANDROID_GOOGLE = "android_google";
    public static final String REG_SOURCE_ANDROID_FACEBOOK = "android_fb";

    public static final String APP_THEME = "app_theme";
    public static final String MSGCODE = "msgcode";
    public static final String MSGSTR = "msgStr";
    public static final String USERID = "userid";
    public static final String USERPLAN_ID = "userplanid";
    public static final String USER_PLAN_EXPIRY_DATE = "userplanexpirydate";
    public static final String USER_PLAN_PURCHASE_DATE = "userplanpurchasedate";
    public static final String MOBILE = "mobile";
    public static final String EMAIL = "email";
    public static final String OCCUPATION = "occupation";
    public static final String MARITALSTATUS = "maritalstatus";
    public static final String ADDRESS1 = "addressLine1";
    public static final String ADDRESS2 = "addressLine2";
    public static final String USER_FIRSTNAME = "userfirstname";
    public static final String COMPANY_NAME = "companyName";
    public static final String USER_PASSWORD = "userpassword";
    public static final String IS_DHRUV_PLAN_AVAIL = "isdhruvavailed";
    public static final String IS_LOGOUT = "isLogout";

    public static final String FACEBOOK_LOGIN = "facebook_login";
    public static final String GOOGLE_LOGIN = "google_login";
    public static final String FACEBOOK_SIGNUP = "facebook_signup";
    public static final String GOOGLE_SIGNUP = "google_signup";
    public static final String EMAIL_LOGIN = "email_login";
    public static final String EMAIL_SIGNUP = "email_signup";
    public static final String LOGIN_SIGNUP_TEXT = "email_login";
    public static final String SIGNUP_SIGNIN_TEXT = "email_signup";
    public static final String TERMS_OF_USE_CLICK = "terms_of_use";


    public static String loginSignupNewApi = CGlobalVariables.JAPI_BASE_URL + "social-signup-login";
    public static String ASTROSAGE_VARTA_URL = VARTA_BASE_URL + "talk-to-astrologers?prtnr_id=AKICN";
    public static String terms_of_use_text = "Terms of Use";


    public final static String ASK_QUESTION_PRICE = "ask_question_price";
    public static String SAVEASKAQUESTION = "saveAskQuestion";
    public static String SEARCH_ITEM = "search_item";
    public static String SEARCH_BUTTON_CLICK = "search_button_click";

    public static String STATE_UTTAR_PRADESH = "Uttar Pradesh";
    public static String STATE_BIHAR = "Bihar";
    public static final String AstrosageKundliGoogleBottomAds = "AstrosageKundliGoogleBottomAds";
    public static final String AstrosageKundliGoogleIntertitialAds = "AstrosageKundliGoogleIntertitialAds";
    public static String liveAstrologerEnabledForAstrosageHomeScreen = "LiveStreamingEnabledForAstrosageHome";
    public static String IS_VARTA_ENABLED = "isVartaEnabled";
    public static String KEY_KUNDALI_VIRTUAL_URL = "kundaliVirtualUrl";
    public static String VARTA_ASTROSAGE = "varta.astrosage.com";
    public static String TALK_ASTROSAGE = "talk.astrosage.com";
    public static final String CALLING_ACTIVITY = "calling_activity";
    public static String KEY_IS_BROWSER = "isBrowser";
    public static String ASTRO_PARTNER_ID = "Astro";
    public static String NOTIFICATION_PARTNER_ID = "NOCAK";
    public static String CONSULT_PREMIMUM_DIALOG_CALL_BTN_PARTNER_ID = "AKPOP";
    public static String HOME_PAGE_BOTTOM_CALL_BTN_PARTNER_ID = "AKBTN";
    public static String HOME_PAGE_BOTTOM_CHAT_BTN_PARTNER_ID = "AKCHT";

    public static String AK_HOME_BOTTOM_BAR_CALL_PARTNER_ID = "AKBCL";
    public static String AK_HOME_BOTTOM_BAR_CHAT_PARTNER_ID = "AKBCH";
    public static String AK_HOME_BOTTOM_BAR_LIVE_PARTNER_ID = "AKBLI";
    public static String AK_HOME_BOTTOM_BAR_KUNDLI_PARTNER_ID = "AKBKU";
    public static String AK_HOME_BOTTOM_BAR_HISTORY_PARTNER_ID = "AKBHI";
    public static String VARTA_HOME_BOTTOM_BAR_CALL_PARTNER_ID = "VHBCL";
    public static String VARTA_HOME_BOTTOM_BAR_CHAT_PARTNER_ID = "VHBCH";
    public static String VARTA_HOME_BOTTOM_BAR_LIVE_PARTNER_ID = "VHBLI";
    public static String VARTA_HOME_BOTTOM_BAR_KUNDLI_PARTNER_ID = "VHBKU";
    public static String ALL_LIVE_BOTTOM_BAR_CALL_PARTNER_ID = "ALBCL";
    public static String ALL_LIVE_BOTTOM_BAR_CHAT_PARTNER_ID = "ALBCH";
    public static String ALL_LIVE_BOTTOM_BAR_LIVE_PARTNER_ID = "ALBLI";
    public static String CONSULT_HISTORY_BOTTOM_BAR_CALL_PARTNER_ID = "CHBCL";
    public static String CONSULT_HISTORY_BOTTOM_BAR_CHAT_PARTNER_ID = "CHBCH";
    public static String CONSULT_HISTORY_BOTTOM_BAR_LIVE_PARTNER_ID = "CHBLI";
    public static String CONSULT_HISTORY_BOTTOM_BAR_KUNDLI_PARTNER_ID = "CHBKU";
    public static String FOLLOWING_BOTTOM_BAR_CALL_PARTNER_ID = "FBCAL";
    public static String FOLLOWING_BOTTOM_BAR_CHAT_PARTNER_ID = "FBCHT";
    public static String FOLLOWING_BOTTOM_BAR_LIVE_PARTNER_ID = "FBLIV";
    public static String FOLLOWING_BOTTOM_BAR_KUNDLI_PARTNER_ID = "FBKUN";
    public static String GENERATE_TICKET_BOTTOM_BAR_CALL_PARTNER_ID = "GTBCL";
    public static String GENERATE_TICKET_BOTTOM_BAR_CHAT_PARTNER_ID = "GTBCH";
    public static String GENERATE_TICKET_BOTTOM_BAR_LIVE_PARTNER_ID = "GTBLI";
    public static String GENERATE_TICKET_BOTTOM_BAR_KUNDLI_PARTNER_ID = "GTBKU";
    public static String MY_ACCOUNT_BOTTOM_BAR_CALL_PARTNER_ID = "MYBCL";
    public static String MY_ACCOUNT_BOTTOM_BAR_CHAT_PARTNER_ID = "MYBCH";
    public static String MY_ACCOUNT_BOTTOM_BAR_LIVE_PARTNER_ID = "MYBLI";
    public static String MY_ACCOUNT_BOTTOM_BAR_KUNDLI_PARTNER_ID = "MYBKU";
    public static String PROFILE_BOTTOM_BAR_CALL_PARTNER_ID = "PBCAL";
    public static String PROFILE_BOTTOM_BAR_CHAT_PARTNER_ID = "PBCHT";
    public static String PROFILE_BOTTOM_BAR_LIVE_PARTNER_ID = "PBLIV";
    public static String PROFILE_BOTTOM_BAR_KUNDLI_PARTNER_ID = "PBKUN";
    public static String WALLET_BOTTOM_BAR_CALL_PARTNER_ID = "WBCAL";
    public static String WALLET_BOTTOM_BAR_CHAT_PARTNER_ID = "WBCHT";
    public static String WALLET_BOTTOM_BAR_LIVE_PARTNER_ID = "WBLIV";
    public static String WALLET_BOTTOM_BAR_KUNDLI_PARTNER_ID = "WBKUN";
    public static String ASTROLOGER_DESCRIPTION_BOTTOM_BAR_CALL_PARTNER_ID = "ADBCL";
    public static String ASTROLOGER_DESCRIPTION_BOTTOM_BAR_CHAT_PARTNER_ID = "ADBCH";
    public static String ASTROLOGER_DESCRIPTION_BOTTOM_BAR_LIVE_PARTNER_ID = "ADBLI";
    public static String ASTROLOGER_DESCRIPTION_BOTTOM_BAR_KUNDLI_PARTNER_ID = "ADBKU";
    public static String OUTPUT_MASTER_BOTTOM_BAR_CALL_PARTNER_ID = "CALL1";
    public static String OUTPUT_MASTER_BOTTOM_BAR_CHAT_PARTNER_ID = "CHAT1";
    public static String OUTPUT_MASTER_BOTTOM_BAR_LIVE_PARTNER_ID = "LIVE1";
    public static String OUTPUT_MASTER_BOTTOM_BAR_KUNDLI_PARTNER_ID = "KUND1";
    public static String OUTPUT_MATCHING_MASTER_BOTTOM_BAR_CALL_PARTNER_ID = "CALL2";
    public static String OUTPUT_MATCHING_MASTER_BOTTOM_BAR_CHAT_PARTNER_ID = "CHAT2";
    public static String OUTPUT_MATCHING_MASTER_BOTTOM_BAR_LIVE_PARTNER_ID = "LIVE2";
    public static String OUTPUT_MATCHING_MASTER_BOTTOM_BAR_KUNDLI_PARTNER_ID = "KUND2";
    public static String INPUT_PANCHANG_BOTTOM_BAR_CALL_PARTNER_ID = "CALL3";
    public static String INPUT_PANCHANG_BOTTOM_BAR_CHAT_PARTNER_ID = "CHAT3";
    public static String INPUT_PANCHANG_BOTTOM_BAR_LIVE_PARTNER_ID = "LIVE3";
    public static String INPUT_PANCHANG_BOTTOM_BAR_KUNDLI_PARTNER_ID = "KUND3";
    public static String DETAILED_HOROSCOPE_BOTTOM_BAR_CALL_PARTNER_ID = "CALL4";
    public static String DETAILED_HOROSCOPE_BOTTOM_BAR_CHAT_PARTNER_ID = "CHAT4";
    public static String DAILY_HOROSCOPE_CALL_PARTNER_ID = "CALL5";
    public static String DAILY_HOROSCOPE_CHAT_PARTNER_ID = "CHAT5";
    public static String WEEKLY_HOROSCOPE_CALL_PARTNER_ID = "CALL6";
    public static String WEEKLY_HOROSCOPE_CHAT_PARTNER_ID = "CHAT6";
    public static String WEEKLY_LOVE_HOROSCOPE_CALL_PARTNER_ID = "CALL7";
    public static String WEEKLY_LOVE_HOROSCOPE_CHAT_PARTNER_ID = "CHAT7";
    public static String MONTHLY_HOROSCOPE_CALL_PARTNER_ID = "CALL8";
    public static String MONTHLY_HOROSCOPE_CHAT_PARTNER_ID = "CHAT8";
    public static String YEARLY_HOROSCOPE_CALL_PARTNER_ID = "CALL9";
    public static String YEARLY_HOROSCOPE_CHAT_PARTNER_ID = "CHAT9";
    public static String DETAILED_HOROSCOPE_BOTTOM_BAR_LIVE_PARTNER_ID = "LIVE4";
    public static String DETAILED_HOROSCOPE_BOTTOM_BAR_KUNDLI_PARTNER_ID = "KUND4";
    public static String NUMEROLOGY_CALC_O_BOTTOM_BAR_CALL_PARTNER_ID = "CALL5";
    public static String NUMEROLOGY_CALC_O_BOTTOM_BAR_CHAT_PARTNER_ID = "CHAT5";
    public static String NUMEROLOGY_CALC_O_BOTTOM_BAR_LIVE_PARTNER_ID = "LIVE5";
    public static String NUMEROLOGY_CALC_O_BOTTOM_BAR_KUNDLI_PARTNER_ID = "KUND5";
    public static String LIVE_ACTIVITY_SHARE_PARTNER_ID = "LASHR";
    public static String ASTROLOGER_DESCRIPTION_SHARE_PARTNER_ID = "ADSHR";
    public static String KUNDLI_HOME_ONLINE_ASTRO_CARD_PARTNER_ID = "KHOAC";


    // it is verify
    public static String HOME_PAGE_ASTRO_VIEW_ALL_PARTNER_ID = "AKHVL";

    public static String VARTA_TAB_CALL_BTN_PARTNER_ID = "VTCAL";
    public static String VARTA_TAB_CHAT_BTN_PARTNER_ID = "VTCHT";
    public static String VARTA_HOME_CALL_BTN_PARTNER_ID = "VHCAL";
    public static String VARTA_HOME_CHAT_BTN_PARTNER_ID = "VHCHT";
    public static String VARTA_TAB_LIVE_JOIN_PARTNER_ID = "VTLIV";
    public static String VARTA_HOME_LIVE_JOIN_PARTNER_ID = "VHLIV";
    public static String ASTRO_DETAIL_CHAT_BTN_PARTNER_ID = "ADCHT";
    public static String ASTRO_DETAIL_CALL_BTN_PARTNER_ID = "ADCAL";
    public static String CONSULT_HISTORY_CHAT_BTN_PARTNER_ID = "CHCHT";
    public static String CONSULT_HISTORY_CALL_BTN_PARTNER_ID = "CHCAL";

    public static String DIALOG_HM_FREE_CALL_CHAT_PARTNER_ID = "DHMFC";
    public static String DIALOG_K_FREE_CALL_CHAT_PARTNER_ID = "DKFCC";
    public static String DIALOG_N_FREE_CALL_CHAT_PARTNER_ID = "DNFCC";
    public static String DIALOG_P_FREE_CALL_CHAT_PARTNER_ID = "DPFCC";
    public static String DIALOG_H_FREE_CALL_CHAT_PARTNER_ID = "DHFCC";
    public static int[] langArr = new int[]{CGlobalVariables.ENGLISH,
            CGlobalVariables.HINDI,
            CGlobalVariables.TAMIL,
            CGlobalVariables.BANGALI,
            CGlobalVariables.MARATHI,
            CGlobalVariables.KANNADA,
            CGlobalVariables.TELUGU,
            CGlobalVariables.MALAYALAM,
            CGlobalVariables.GUJARATI,
            CGlobalVariables.ODIA,
            CGlobalVariables.ASAMMESSE,
            CGlobalVariables.SPANISH,
            CGlobalVariables.CHINESE,
            CGlobalVariables.JAPANESE,
            CGlobalVariables.PORTUGUESE,
            CGlobalVariables.GERMAN,
            CGlobalVariables.ITALIAN,
            CGlobalVariables.FRENCH};
    public static final String KEY_LANGUAGES = "selectedLanguages";
    public static final String KEY_API = "key";
    public static final String COUNTRY_CODE = "country_code";
    public static final int WIZARD_ACTIVITY_TIME_PICKER = 1002;
    public static String GOOGLE_ANALYTIC_ACTION_ASTROSAGE_VARTA_JOIN_REQ = "AstroSage Varta Astrologer Join Request";
    public static final String LANG_ENGLISH = "English";
    public static final String LANG_HINDI = "Hindi";
    public static final String LANG_TAMIL = "Tamil";
    public static final String LANG_MARATHI = "Marathi";
    public static final String LANG_BANGALI = "Bengali";
    public static final String LANG_KANNADA = "Kannada";
    public static final String LANG_TELUGU = "Telugu";
    public static final String LANG_MALAYALAM = "Malayalam";
    public static final String LANG_GUJARATI = "Gujarati";

    public static final String LANG_MAITHILI = "Maithili";
    public static final String LANG_BHOJPURI = "Bhojpuri";
    public static final String LANG_URDU = "Urdu";
    public static final String LANG_SINDHI = "Sindhi";
    public static final String LANG_PUNJABI = "Punjabi";
    public static final String LANG_ODIA = "Odia";
    public static final String LANG_NEPALI = "Nepali";
    public static final String LANG_KONKANI = "Konkani";
    public static final String LANG_KASHMIRI = "Kashmiri";
    public static final String LANG_DOGRI = "Dogri";
    public static final String LANG_ASSAMESE = "Assamese";

    public static final String LANG_HARYANVI = "Haryanvi";
    public static final String LANG_RAJASTHANI = "Rajasthani";
    public static final String LANG_MANIPURI = "Manipuri";
    public static final String LANG_SANSKRI = "Sanskrit";
    public static final String LANG_KUMAONI = "Kumaoni";
    public static final String LANG_TULU = "Tulu";
    public static final String LANG_SANTALI = "Santali";
    public static final String LANG_SPANISH = "Spanish";
    public static final String LANG_FRENCH = "French";
    public static final String LANG_ARABIC = "Arabic";
    public static final String LANG_CHINESE = "Chinese";
    public static final String LANG_RUSSIAN = "Russian";
    public static final String LANG_PORTUGUESE = "Portuguese";
    public static final String LANG_INDONESIAN = "Indonesian";
    public static final String LANG_JAPANESE = "Japanese";
    //public static String dhruvDomain = "https://ascloud.astrosage.com/";

    public static String affiliateHome = DHRUV_BASE_URL + "dhruv/affiliate/home.jsp";
    public static String orderDetailUrl = DHRUV_BASE_URL + "dhruv/affiliate/affiliate-order-detail.jsp";
    public static String buyProductUrl = DHRUV_BASE_URL + "dhruv/affiliate/buy-product.jsp";
    public static String revenueReportUrl = DHRUV_BASE_URL + "dhruv/affiliate/affiliate-revenue-report.jsp";

    public static String GOOGLE_ANALYTIC_AFFILIATE_AGREEMENT = "Open_sign_up_for_affiliate";
    public static String GOOGLE_ANALYTIC_NEW_APPOINTMENT = "open_new_appointment";
    public static String GOOGLE_ANALYTIC_MY_APPOINTMENT = "open_my_appointment";
    public static String GOOGLE_ANALYTIC_VARTA_JOIN = "open_varta_join";
    public static String GOOGLE_ANALYTIC_AFFILIATE = "open_affiliate_program";
    public static String GOOGLE_ANALYTIC_TOP_UP_RECHARGE = "open_top_up_recharge";
    public static String GOOGLE_ANALYTIC_FOOTER_DETAIL = "open_footer_detail";
    public static String GOOGLE_ANALYTIC_AFFILIATE_BUY_SERVICES = "open_affilate_buy_services";
    public static String GOOGLE_ANALYTIC_AFFILIATE_BUY_PRODUCT = "open_affilate_buy_products";
    public static final String GOOGLE_ANALYTIC_OPEN_MANTRA_HOME_ACTIVITY = "open_mantra_home_activity";
    public static final String GOOGLE_ANALYTIC_OPEN_MANTRA_ACTIVITY = "open_mantra_activity";
    public static final String GOOGLE_ANALYTIC_OPEN_CHALISA_ACTIVITY = "open_chalisa_activity";
    public static final String GET_MANTRA_CHALISA = API2_BASE_URL + "as/chalisa-mantra/audio-video.asp";
    public static final String GOOGLE_ANALYTIC_MANTRA_CHALISA_VIDEO = "Open_Mantra_Chalisa_Video";
    public static final String GOOGLE_ANALYTIC_MANTRA_CHALISA_AUDIO = "Open_Mantra_Chalisa_Audio";
    public static String GOOGLE_ANALYTIC_OPEN_YOUTUBE_PLAYER_MANTRA = "open_youtube_player_mantra_video";
    public static final String FBA_ASTROSAGE_HOME_WALLET_LOGIN = "ak_home_wallet_login";
    public static final String FBA_ASTROSAGE_HOME_WALLET_CLICK = "ak_home_wallet_open";
    public static final String FBA_MAGAZINE_SHARE = "magazine_share";

    public static final String EXPERT_VEDIC = "Vedic";
    public static final String EXPERT_KP_SYSTEM = "KP System";
    public static final String EXPERT_LAL_KITAB = "Lal Kitab";
    public static final String EXPERT_VASTU = "Vastu";
    public static final String EXPERT_TAROT_READING = "Tarot Reading";
    public static final String EXPERT_NADI = "Nadi";
    public static final String EXPERT_NUMEROLOGY = "Numerology";
    public static final String EXPERT_ASHTAKVARGA = "Ashtakvarga";
    public static final String EXPERT_PALMISTRY = "Palmistry";
    public static final String EXPERT_RAMAL = "Ramal";
    public static final String EXPERT_JAIMINI = "Jaimini";
    public static final String EXPERT_TAJIK = "Tajik";
    public static final String EXPERT_WESTERN = "Western";
    public static final String EXPERT_KERALA = "Kerala";
    public static final String EXPERT_SWAR_SHASTRA = "Swar Shastra";
    public static final String EXPERT_REIKI = "Reiki";
    public static final String EXPERT_CRYSTAL_HEALING = "Crystal Healing";
    public static final String EXPERT_ANGEL_READING = "Angel Reading";
    public static final String EXPERT_PENDULUM_DOWSING = "Pendulum Dowsing";
    public static final String EXPERT_FENG_SHUI = "Feng Shui";
    public static final String EXPERT_PRASHNA = "Prashna / Horary";
    public static final String EXPERT_PSYCHIC_READING = "Psychic Reading";
    public static final String EXPERT_FACE_READING = "Face Reading";
    public static final String EXPERT_MUHURTA = "Muhurta";
    public static final String KEY_EXPERTS = "sectedExperts";
    static final public String UPDATE_TAG_MANAGER_DATA = "UpdateTagManagerData";
    public static final String FONTS_OPEN_SANS_SEMIBOLD = "fonts/OpenSans-Semibold.ttf";
    public static final String FONTS_ROBOTO_BOLD = "fonts/Roboto-Bold.ttf";
    public static final String FONTS_ROBOTO_BLACK = "fonts/Roboto-Black.ttf";
    public static final String FONTS_ROBOTO_MEDIUM = "fonts/Roboto-Medium.ttf";
    public static final int VARTA_TAB_POS = 2;

    public static final String BIRTH_DETAILS_KEY = "OpenKundliBirthDetail";
    public static final String PACKAGE_NAME = "pkgname";

    public static final String DHRUV_CITY_URL = API2_BASE_URL + "astrologers/city-list.asp";
    public static final String DHRUV_CITY_ASTROLOGER_LIST_URL = API2_BASE_URL + "astrologers/astrologers-in-city.asp";
    public static String DHRUV_SEND_OTP_URL = JAPI_BASE_URL + "dhruvapi/send-otp";
    public static String DHRUV_OTP_VERIFY_URL = JAPI_BASE_URL + "dhruvapi/verify-otp";

    public static final String KEY_PREF_COUNTRY_CODE = "country_code";
    public static final String KEY_PREF_MOBILE_NO = "mobile_no";
    public static final String KEY_PREF_IS_MOBILE_VERIFY = "is_mobile_verify";
    public static final int MAX_FREE_KUNDLI_COUNT = 10;
    static final public String CHECK_FREE_DHRUV_PLAN_AVAIL_INTENT = "CHECK_FREE_DHRUV_PLAN_AVAIL_INTENT";
    static final public String CHECK_LOGIN_INTENT = "CHECK_LOGIN_INTENT";
    static final public String SERVICE_DETAILS_BROADCAST = "SERVICE_DETAILS_BROADCAST";
    public static String DHRUV_GET_CONSULTATION_URL = API2_BASE_URL + "astrologers/user-registration.asp";
    public static String DHRUV_LEAD_GENERATION_URL = API2_BASE_URL + "astrologers/send-lead-msg-to-astrologer.asp";
    public static String ASTROLOGER_OTP_SEND_URL = API2_BASE_URL + "astrologers/send-otp.asp";
    public static String ASTROLOGER_OTP_VERIFICATION_URL = API2_BASE_URL + "astrologers/sms-verification.asp";

    public static String DHRUV_OMF_DIALOG_BUYNOW_BTN_CLICK = "omf_dialog_buy_now_btn";
    public static String DHRUV_OMF_DIALOG_CROSS_BTN_CLICK = "omf_dialog_cross_btn";
    public static String DHRUV_OMF_DIALOG_SHOW = "omf_dialog_show";


    public static final String KEY_PREF_DHRUV_FREE_CONSULTATION_COUNTRY_CODE = "countrycode";
    public static final String KEY_PREF_DHRUV_FREE_CONSULTATION_USERNAME = "username";
    public static final String KEY_PREF_DHRUV_FREE_CONSULTATION_MOBILENO = "phoneno";
    public static final String KEY_PREF_DHRUV_FREE_CONSULTATION_EMAILID = "emailid";
    public static final String KEY_PREF_DHRUV_FREE_CONSULTATION_ASTROLOGERID = "astrologerid";
    public static final String SHIW_OTP_DIALOG = "showOTP";
    public static final String SHIW_LEAD_SUCCESS_DIALOG = "showLeadSuccess";
    public static String FIREBASE_LOGIN_VIA = "email_id";
    public static String FIREBASE_LOGIN_VIA_FACEBOOK = "facebook";
    public static String FIREBASE_LOGIN_VIA_GOOGLE = "google";
    public static String FIREBASE_LOGIN_VIA_OTP = "otp";
    public static final double PRICE_IN_ONE_UNIT = 1000000.0;
    public static final int REQUEST_CODE_PAYTM = 10002;
    public static final int REQUEST_CODE_CCAVENUE = 10003;
    public static final String TXN_SUCCESS = "TXN_SUCCESS";
    public static final String IS_FOLLOW_NOTIF_SETTING_CHECKED = "is_follow_notif_setting_checked";
    //public static final String VARTA_ASTROLOGER_BASE_URL = "varta.astrosage.com/astrologer/";
    public static final String VARTA_PANEL_PACKAGE = "com.ojassoft.astrologer";
    public static final String VARTA_PANEL_ACTION_NAME = "SEND_CURRENT_SCREEN_NAME_TO_PANEL";

    public static ArrayList<String> getBroadcastList(int adPosition, String listName) {
        ArrayList<String> arrayList = new ArrayList<>();
        switch (listName) {
            case "kundliScreenNames":
                arrayList.addAll(Arrays.asList(kundliScreenNames));
                break;
            case "predictionsScreenNames":
                arrayList.addAll(Arrays.asList(predictionsScreenNames));
                break;
            case "dashaScreenNames":
                arrayList.addAll(Arrays.asList(dashaScreenNames));
                break;
            case "kpSystemScreenNames":
                arrayList.addAll(Arrays.asList(kpSystemScreenNames));
                break;
            case "shodashvargaScreenNames":
                arrayList.addAll(Arrays.asList(shodashvargaScreenNames));
                break;
            case "lalKitabScreenNames":
                arrayList.addAll(Arrays.asList(lalKitabScreenNames));
                break;
            case "varshphalScreenNames":
                arrayList.addAll(Arrays.asList(varshphalScreenNames));
                break;
            case "miscScreenNames":
                arrayList.addAll(Arrays.asList(miscScreenNames));
                break;
        }

        if (adPosition != 0) {
            arrayList.add(adPosition, "");
        } else {
            arrayList.add("");
        }

        return arrayList;
    }

    public static String[] kundliScreenNames = {"Basic", "Lagna Chart", "Navmansh Chart", "Moon Chart", "Chalit Chart", "Planetary Positions",
            "Planets Sub Positions", "Chalit Table", "Birth Details", "Panchang at Birth", "Ashtakvarga", "Karakamsha Chart",
            "Swamsa Chart", "Transit", "Shad Bala", "Prastharashtakvarga", "Bhav Madhya Chakra", "Planetary Friendship",
            "Details", "Avkahada Chakra", "Ghatak and Favourable", "Download PDF", "Ask a question"};

    public static String[] predictionsScreenNames = {"Predictions", "Life Predictions", "Monthly Predictions",
            "Daily Predictions", "Mangal Dosh", "Sade Sati Life Report", "Kaal Sarp Dosh", "Lal Kitab Debt",
            "Lal Kitab Teva Type", "Lal Kitab Remedies", "Ascendant Prediction", "Planet Consideration",
            "Gemstones Report", "Transit Today", "Mahadasha Phala", "Nakshatra Report", "Prediction", "Baby Names",
            "Moon Sign", "Moon Sign (Classical)", "Rudraksha", "Jadi", "Yantra", "Ask a question"};

    public static String[] dashaScreenNames = {"Dasha", "Vimshottari Dasha", "Mahadasha Phala",
            "Char Dasha", "Yogini Dasha"};

    public static String[] miscScreenNames = {"Miscellaneous", "Yoga & Dosha",
            "Remedies", "Karak", "Avastha", "Navatara", "Upgraha Table", "Upgraha Chart",
            "Arudha Chart", /*"Current Ruling Planets",*/ "Shodashvarga Table" /*, "Shodashvarga Bhava"*/};

    public static String[] kpSystemScreenNames = {"KP System", "KP Chart", "Rasi Chart", "KP Planets",
            "KP Cusps", "KP Planet Signification", "KP House Signification", "KP Planet Signification",
            "Nakshatra Nadi", "Cuspal Interlinks", "4-Step Method", "Cuspal Interlinks", "Ruling Planets", "Current Ruling Planets", "Misc KP", "KP Aspects", "Ask a question"};

    public static String[] shodashvargaScreenNames = {"Shodashvarga", "Lagna - D1 Chart", "Hora - D2 Chart", "Drekkana - D3 Chart",
            "Chaturthamsha - D4 Chart", "Saptamamsha - D7 Chart", "Navamsha - D9 Chart", "Dashamamsha - D10 Chart", "Dwadashamamsha - D12 Chart",
            "Shadashamsha - D16 Chart", "Vimshamsha - D20 Chart", "Saptavimshamsha - D27 Chart", "Chaturvimshamsha - D24 Chart",
            "Trimshamsha - D30 Chart", "Khavedamsha - D40 Chart", "Akshvedamsha - D45 Chart", "Shashtiamsha - D60 Chart", "Shodashvarga", "Ask a question"};

    public static String[] lalKitabScreenNames = {"Lal Kitab", "Lal Kitab Kundli", "Lal Kitab Remedies",
            "Lal Kitab Varsha Kundli", "Lal Kitab Debts", "Lal Kitab Teva", "Ask a question"};

    public static String[] varshphalScreenNames = {"Tajik Varshphal", "Annual Chart", "Annual Planets", "Tajik Poitions", "Panchadhikari", "Ask a question"};
    public static final String ASTROSAGE_BACKGROUND_LOGIN = "astrosage_background_login";

    public static final String FIREBASE_EVENT_INSTALL_REFERRER = "install_referrer";
    public static final String INSTALL_REF_ASPOPUPBTM_OPEN_VARTA_CHAT = "install_ref_aspopupbtm_open_varta_chat";
    public static final String INSTALL_REF_GOOGLE_OPEN_VARTA_CHAT = "install_ref_google_open_varta_chat";
    public static final String INSTALL_REF_GOOGLE_ORGANIC_OPEN_VARTA_CHAT = "install_ref_google_organic_open_varta_chat";
    public static final String INSTALL_REF_FB_OPEN_VARTA_CHAT = "install_ref_fb_open_varta_chat";
    public static final String FB_DEEPLINK_OPEN_VARTA_CHAT = "fb_deeplink_open_varta_chat";
    public static final String FB_DEEPLINK_OPEN_KUNDLI_AI_CHAT = "fb_deeplink_open_kundli_ai_chat";
    public static final String FB_DEEPLINK_OPEN_VARTA_LIVE = "fb_deeplink_open_varta_live";
    public static final String INSTALL_FREE_NOTIFICATION = "install_free_notification";
    public static final String NOTIFICATION_TYPE = "notification_type";
    public static final String NOTIFICATION_RECEIVED = "notification_received";
    public static final String RECEIVED_FREE_CHAT_NOTIFICATION = "received_free_chat_notification";
    public static final String TYPE_CALL = "call";
    public static final String TYPE_CHAT = "chat";
    public static final String COUNTRY_CODE_IND = "91";
    public static final String COUNTRY_CODE_IN = "in";

    public static final String NEW_USER_RECHARGE_AFTER_FREE_CHAT_CALL = "new_user_recharge";
    public static final String NEW_USER_RECHARGE_NOTIFICATION_RECEIVED = "new_user_recharge_notification_received";

    public static final String NEW_USER_RECHARGE_NOTIFICATION = "new_user_recharge_notification";
    public static final String ASTROSAGE_HOME_WALLET_PARTNER_ID = "AHWPI";

    public static final String RELATION_EXALTED = "Exalted";
    public static final String RELATION_DEBILITATED = "Debilitated";
    public static final String RELATION_FRIENDLY = "Friendly";
    public static final String RELATION_OWN = "OWN";
    public static final String RELATION_ENEMY = "Enemy";
    public static final String RELATION_NEUTRAL = "Neutral";
    public static String ISAGORACALLENABLED = "isagoracallenabled";

    // SAN For SCALE CHANGE
    public static boolean isFontSizeChange = false;
    public static float fontSizeChange = 0.0f;
    public static String displayliveinhoroscope = "displayliveinhoroscope";
    public static String displayLiveOnAstroSageHome = "displayLiveOnAstroSageHome";

    public static String CHART_ID = "chartid";

    public static final String AIChatSupportedLanguages = "AIChatSupportedLanguages";
    public static final String AIFreeChatPopupSupportedLanguages = "AIFreeChatPopupSupportedLanguages";
    public static final String enabledAIFreeChatPopup = "EnabledAIFreeChatPopup";
    public static final String DEFAULT_ORDER_PAYMENT_METHOD = "defaultOrderPaymentMethod";
    public static final String CAMP_INSTALL = "DLINST";

    public static final String FIRST_FREE_CHAT_TYPE = "firstchattype";
    public static final String FIRST_FREE_CALL_TYPE = "firstcalltype";
    public static final String FIRST_CONSULT_TYPE = "firstconsulttype";
    public static final String SECOND_FREE_CHAT_TYPE = "secondchattype";

    public static final String GOOGLE_FREE_CHAT_TYPE = "googleconsulttype";
    public static final String FACEBOOK_FREE_CHAT_TYPE = "fbconsulttype";
    public static final String FACEBOOK_INSTALL_FREE_CHAT_TYPE = "fbinstallconsulttype";
    public static final String ORGANIC_INSTALL_FREE_CHAT_TYPE = "organicconsulttype";

    public static final String TYPE_FREE_CHAT_HUMAN = "human";
    public static final String TYPE_FREE_CHAT_AI = "ai";
    public static final String TYPE_AI_CALL = "aicall";
    public static final String TYPE_AI_CHAT = "aichat";
    public static final String TYPE_HUMAN_CALL = "humancall";
    public static final String TYPE_HUMAN_CHAT = "humanchat";

    public static final String SHOW_NEW_BRIHAT_KUNDLI_PAGE = "show_new_brihat_horoscope_page";
    public static final String CAMP_KUNDLI_AI = "KAI";


    public static final int CATEGORY_SCREEN_ID = 100;


    public static final int BASIC_LAGNA_SCREEN_ID = 0;
    public static final int BASIC_NAVAMSHA_SCREEN_ID = 1;
    public static final int BASIC_MOON_SCREEN_ID = 2;
    public static final int BASIC_CHALIT_SCREEN_ID = 3;
    public static final int BASIC_PLANETS_SCREEN_ID = 4;
    public static final int BASIC_PLANETS_SUB_SCREEN_ID = 5;
    public static final int BASIC_CHALIT_TABLE_SCREEN_ID = 6;
    public static final int BASIC_BIRTH_DETAILS_SCREEN_ID = 7;
    public static final int BASIC_PANCHANG_SCREEN_ID = 8;
    public static final int BASIC_ASHTAKVARGA_SCREEN_ID = 9;
    public static final int BASIC_KARAKAMSHA_SCREEN_ID = 10;
    public static final int BASIC_SWAMSA_SCREEN_ID = 11;
    public static final int BASIC_TRANSIT_SCREEN_ID = 12;
    public static final int BASIC_SHAD_BALA_SCREEN_ID = 13;
    public static final int BASIC_PRASTHARASHTAKVARGA_SCREEN_ID = 14;
    public static final int BASIC_BHAV_MADHYA_SCREEN_ID = 15;
    public static final int BASIC_FRIENDSHIP_SCREEN_ID = 16;
    public static final int BASIC_PERSONAL_DETAILS_SCREEN_ID = 17;
    public static final int BASIC_AVKAHADACHAKRA_SCREEN_ID = 18;
    public static final int BASIC_GHATAK_AND_FAVOURABLE_SCREEN_ID = 19;
    public static final int BASIC_DOWNLOAD_PDG_SCREEN_ID = 20;

    public static final int ASK_QUESTION_SCREEN_ID = 21;
    public static final int CLOUD_SCREEN_ID = 22;

    public static final int DASHA_VIMSHOTTARI_SCREEN_ID = 23;
    public static final int DASHA_MAHADASHA_PHALA_SCREEN_ID = 24;
    public static final int DASHA_CHAR_SCREEN_ID = 25;
    public static final int DASHA_YOGINI_SCREEN_ID = 26;

    public static final int PREDICTION_LIFE_SCREEN_ID = 27;
    public static final int PREDICTION_MONTHLY_SCREEN_ID = 28;
    public static final int PREDICTION_DAILY_SCREEN_ID = 29;
    public static final int PREDICTION_MANGAL_DOSH_SCREEN_ID = 30;
    public static final int PREDICTION_SADE_SATI_LIFE_REPORT_SCREEN_ID = 31;
    public static final int PREDICTION_KAAL_SARP_DOSH_SCREEN_ID = 32;

    public static final int LAL_KITAB_DEBT_SCREEN_ID = 33;
    public static final int LAL_KITAB_TEVA_TYPE_SCREEN_ID = 34;
    public static final int LAL_KITAB_REMEDIES_SCREEN_ID = 35;
    public static final int LAL_KITAB_DASHA_SCREEN_ID = 172;
    public static final int LAL_KITAB_PLANETARY_POS_SCREEN_ID = 173;
    public static final int LAL_KITAB_HOUSE_POS_SCREEN_ID = 174;

    public static final int PREDICTION_ASCENDENT_SCREEN_ID = 36;
    public static final int PREDICTION_PLANET_CONSIDERATION_SCREEN_ID = 37;
    public static final int PREDICTION_GEMSTONE_REPORT_SCREEN_ID = 38;
    public static final int PREDICTION_TRANSIT_TODAY_SCREEN_ID = 39;
    public static final int PREDICTION_MAHADASHA_PHALA_SCREEN_ID = 40;
    public static final int PREDICTION_NAKSHATRA_REPORT_SCREEN_ID = 41;
    public static final int PREDICTION_SCREEN_ID = 42;
    public static final int PREDICTION_BABY_NAMES_SCREEN_ID = 43;
    public static final int PREDICTION_MOON_SIGN_SCREEN_ID = 44;
    public static final int PREDICTION_MOON_SIGN_CLASSICAL_SCREEN_ID = 94;
    public static final int PREDICTION_RUDRAKSH_SCREEN_ID = 45;
    public static final int PREDICTION_JADI_SCREEN_ID = 46;
    public static final int PREDICTION_YANTRA_SCREEN_ID = 47;

    public static final int KP_SYSTEM_CHART_SCREEN_ID = 48;
    public static final int KP_SYSTEM_RASI_CHART_SCREEN_ID = 49;
    public static final int KP_SYSTEM_PLANETS_SCREEN_ID = 50;
    public static final int KP_SYSTEM_CUSPS_SCREEN_ID = 51;
    public static final int KP_SYSTEM_PLANET_SIGNIFICATION_SCREEN_ID = 52;
    public static final int KP_SYSTEM_HOUSE_SIGNIFICANCE_SCREEN_ID = 53;
    public static final int KP_SYSTEM_PLANET_SIGNIFICATION_V2_SCREEN_ID = 54;
    public static final int KP_SYSTEM_NAKSHATRA_NADI_SCREEN_ID = 55;
    public static final int KP_SYSTEM_CIL_SUB_SUB_SCREEN_ID = 56;
    public static final int KP_SYSTEM_4_STEP_SCREEN_ID = 57;
    public static final int KP_SYSTEM_CIL_SUB_SCREEN_ID = 93;
    public static final int KP_SYSTEM_RULING_PLANETS_SCREEN_ID = 58;
    public static final int KP_SYSTEM_CURRENT_RULING_PLANETS_SCREEN_ID = 59;
    public static final int KP_SYSTEM_MISC_SCREEN_ID = 60;
    public static final int KP_SYSTEM_KP_CUSPS_SCREEN_ID = 61;

    public static final int SHODASHVARGA_LAGNA_D1_SCREEN_ID = 62;
    public static final int SHODASHVARGA_HORA_D2_SCREEN_ID = 63;
    public static final int SHODASHVARGA_DREKKANA_D3_SCREEN_ID = 64;
    public static final int SHODASHVARGA_CHATURTHAMSHA_D4_SCREEN_ID = 65;
    public static final int SHODASHVARGA_SAPTAMAMSHA_D7_SCREEN_ID = 66;
    public static final int SHODASHVARGA_NAVAMSHA_D9_SCREEN_ID = 67;
    public static final int SHODASHVARGA_DASHAMAMSHA_D10_SCREEN_ID = 68;
    public static final int SHODASHVARGA_DWADASHAMAMSHA_D12_SCREEN_ID = 69;
    public static final int SHODASHVARGA_SHODASHAMSHA_D16_SCREEN_ID = 70;
    public static final int SHODASHVARGA_VIMSHAMSHA_D20_SCREEN_ID = 71;
    public static final int SHODASHVARGA_SAPTAVIMSHAMSHA_D27_SCREEN_ID = 72;
    public static final int SHODASHVARGA_CHATURVIMSHAMSHA_D24_SCREEN_ID = 73;
    public static final int SHODASHVARGA_TRIMSHAMSHA_D30_SCREEN_ID = 74;
    public static final int SHODASHVARGA_KHAVEDAMSHA_D40_SCREEN_ID = 75;
    public static final int SHODASHVARGA_AKSHVEDAMSHA_D45_SCREEN_ID = 76;
    public static final int SHODASHVARGA_SHASHTIAMSHA_D60_SCREEN_ID = 77;
    public static final int SHODASHVARGA_SHODASHVARGA_SCREEN_ID = 78;

    public static final int LAL_KITAB_KUNDALI_SCREEN_ID = 79;
    public static final int LAL_KITAB_VARSHA_KUNDLI_SCREEN_ID = 81;


    public static final int VARSHPHAL_PANCHADHIKARI_SCREEN_ID = 82;
    public static final int VARSHPHAL_PLANETS_SCREEN_ID = 83;
    public static final int VARSHPHAL_CHART_SCREEN_ID = 95;

    public static final int MISC_YOGA_AND_DASHA_SCREEN_ID = 84;
    public static final int MISC_REMEDIES_SCREEN_ID = 85;
    public static final int MISC_KARAK_SCREEN_ID = 86;
    public static final int MISC_AVASTHA_SCREEN_ID = 87;
    public static final int MISC_NAVATARA_SCREEN_ID = 88;
    public static final int MISC_UPGRAHA_CHART_SCREEN_ID = 89;
    public static final int MISC_UPGRAHA_TABLE_SCREEN_ID = 90;
    public static final int MISC_ARUDHA_CHART_SCREEN_ID = 91;
    public static final int MISC_SHODASHVARGA_TABLE_SCREEN_ID = 92;


    public static final int[] BasicIdsList = {
            CATEGORY_SCREEN_ID,
            BASIC_LAGNA_SCREEN_ID,
            BASIC_NAVAMSHA_SCREEN_ID,
            CLOUD_SCREEN_ID,
            BASIC_MOON_SCREEN_ID,
            BASIC_CHALIT_SCREEN_ID,
            BASIC_PLANETS_SCREEN_ID,
            BASIC_PLANETS_SUB_SCREEN_ID,
            BASIC_CHALIT_TABLE_SCREEN_ID,
            BASIC_BIRTH_DETAILS_SCREEN_ID,
            BASIC_PANCHANG_SCREEN_ID,
            BASIC_ASHTAKVARGA_SCREEN_ID,
            BASIC_KARAKAMSHA_SCREEN_ID,
            BASIC_SWAMSA_SCREEN_ID,
            BASIC_TRANSIT_SCREEN_ID,
            BASIC_SHAD_BALA_SCREEN_ID,
            BASIC_PRASTHARASHTAKVARGA_SCREEN_ID,
            BASIC_BHAV_MADHYA_SCREEN_ID,
            BASIC_FRIENDSHIP_SCREEN_ID,
            BASIC_PERSONAL_DETAILS_SCREEN_ID,
            BASIC_AVKAHADACHAKRA_SCREEN_ID,
            BASIC_GHATAK_AND_FAVOURABLE_SCREEN_ID,
            BASIC_DOWNLOAD_PDG_SCREEN_ID,
            ASK_QUESTION_SCREEN_ID,};

    public static final int[] DashaScreenIdsArray = {
            CATEGORY_SCREEN_ID,
            DASHA_VIMSHOTTARI_SCREEN_ID,
            DASHA_MAHADASHA_PHALA_SCREEN_ID,
            CLOUD_SCREEN_ID,
            DASHA_CHAR_SCREEN_ID,
            DASHA_YOGINI_SCREEN_ID
    };


    public static final int[] AllPredictionIds = {
            CATEGORY_SCREEN_ID,
            PREDICTION_LIFE_SCREEN_ID,
            PREDICTION_MONTHLY_SCREEN_ID,
            CLOUD_SCREEN_ID,
            PREDICTION_DAILY_SCREEN_ID,
            PREDICTION_MANGAL_DOSH_SCREEN_ID,
            PREDICTION_SADE_SATI_LIFE_REPORT_SCREEN_ID,
            PREDICTION_KAAL_SARP_DOSH_SCREEN_ID,
            LAL_KITAB_DEBT_SCREEN_ID,
            LAL_KITAB_TEVA_TYPE_SCREEN_ID,
            LAL_KITAB_REMEDIES_SCREEN_ID,
            PREDICTION_ASCENDENT_SCREEN_ID,
            PREDICTION_PLANET_CONSIDERATION_SCREEN_ID,
            PREDICTION_GEMSTONE_REPORT_SCREEN_ID,
            PREDICTION_TRANSIT_TODAY_SCREEN_ID,
            PREDICTION_MAHADASHA_PHALA_SCREEN_ID,
            PREDICTION_NAKSHATRA_REPORT_SCREEN_ID,
            PREDICTION_SCREEN_ID,
            PREDICTION_BABY_NAMES_SCREEN_ID,
            PREDICTION_MOON_SIGN_SCREEN_ID,
            PREDICTION_MOON_SIGN_CLASSICAL_SCREEN_ID,
            PREDICTION_RUDRAKSH_SCREEN_ID,
            PREDICTION_JADI_SCREEN_ID,
            PREDICTION_YANTRA_SCREEN_ID,
            ASK_QUESTION_SCREEN_ID
    };


    public static final int[] KPSystemScreenIds = {
            CATEGORY_SCREEN_ID,
            KP_SYSTEM_CHART_SCREEN_ID,
            KP_SYSTEM_RASI_CHART_SCREEN_ID,
            CLOUD_SCREEN_ID,
            KP_SYSTEM_PLANETS_SCREEN_ID,
            KP_SYSTEM_CUSPS_SCREEN_ID,
            KP_SYSTEM_PLANET_SIGNIFICATION_SCREEN_ID,
            KP_SYSTEM_HOUSE_SIGNIFICANCE_SCREEN_ID,
            KP_SYSTEM_PLANET_SIGNIFICATION_V2_SCREEN_ID,
            KP_SYSTEM_NAKSHATRA_NADI_SCREEN_ID,
            KP_SYSTEM_CIL_SUB_SUB_SCREEN_ID,
            KP_SYSTEM_4_STEP_SCREEN_ID,
            KP_SYSTEM_CIL_SUB_SCREEN_ID,
            KP_SYSTEM_RULING_PLANETS_SCREEN_ID,
            KP_SYSTEM_CURRENT_RULING_PLANETS_SCREEN_ID,
            KP_SYSTEM_MISC_SCREEN_ID,
            KP_SYSTEM_KP_CUSPS_SCREEN_ID,
            ASK_QUESTION_SCREEN_ID
    };

    public static final int[] ShodashvargaScreenIds = {
            CATEGORY_SCREEN_ID,
            SHODASHVARGA_LAGNA_D1_SCREEN_ID,
            SHODASHVARGA_HORA_D2_SCREEN_ID,
            CLOUD_SCREEN_ID,
            SHODASHVARGA_DREKKANA_D3_SCREEN_ID,
            SHODASHVARGA_CHATURTHAMSHA_D4_SCREEN_ID,
            SHODASHVARGA_SAPTAMAMSHA_D7_SCREEN_ID,
            SHODASHVARGA_NAVAMSHA_D9_SCREEN_ID,
            SHODASHVARGA_DASHAMAMSHA_D10_SCREEN_ID,
            SHODASHVARGA_DWADASHAMAMSHA_D12_SCREEN_ID,
            SHODASHVARGA_SHODASHAMSHA_D16_SCREEN_ID,
            SHODASHVARGA_VIMSHAMSHA_D20_SCREEN_ID,
            SHODASHVARGA_SAPTAVIMSHAMSHA_D27_SCREEN_ID,
            SHODASHVARGA_CHATURVIMSHAMSHA_D24_SCREEN_ID,
            SHODASHVARGA_TRIMSHAMSHA_D30_SCREEN_ID,
            SHODASHVARGA_KHAVEDAMSHA_D40_SCREEN_ID,
            SHODASHVARGA_AKSHVEDAMSHA_D45_SCREEN_ID,
            SHODASHVARGA_SHASHTIAMSHA_D60_SCREEN_ID,
            SHODASHVARGA_SHODASHVARGA_SCREEN_ID,
            ASK_QUESTION_SCREEN_ID
    };

    public static final int[] LalKitabScreenIds = {
            CATEGORY_SCREEN_ID,
            LAL_KITAB_KUNDALI_SCREEN_ID,
            LAL_KITAB_REMEDIES_SCREEN_ID,
            CLOUD_SCREEN_ID,
            LAL_KITAB_VARSHA_KUNDLI_SCREEN_ID,
            LAL_KITAB_DEBT_SCREEN_ID,
            LAL_KITAB_TEVA_TYPE_SCREEN_ID,
            LAL_KITAB_DASHA_SCREEN_ID,
            LAL_KITAB_PLANETARY_POS_SCREEN_ID,
            LAL_KITAB_HOUSE_POS_SCREEN_ID,
            ASK_QUESTION_SCREEN_ID
    };

    public static final int[] MiscScreenIds = {
            CATEGORY_SCREEN_ID,
            MISC_YOGA_AND_DASHA_SCREEN_ID,
            MISC_REMEDIES_SCREEN_ID,
            CLOUD_SCREEN_ID,
            MISC_KARAK_SCREEN_ID,
            MISC_AVASTHA_SCREEN_ID,
            MISC_NAVATARA_SCREEN_ID,
            MISC_UPGRAHA_CHART_SCREEN_ID,
            MISC_UPGRAHA_TABLE_SCREEN_ID,
            MISC_ARUDHA_CHART_SCREEN_ID,
            MISC_SHODASHVARGA_TABLE_SCREEN_ID
    };


    public static final int[] VarshphalScreenIds = {
            CATEGORY_SCREEN_ID,
            VARSHPHAL_CHART_SCREEN_ID,
            VARSHPHAL_PLANETS_SCREEN_ID,
            CLOUD_SCREEN_ID,
            PREDICTION_SCREEN_ID,
            VARSHPHAL_PANCHADHIKARI_SCREEN_ID,
            ASK_QUESTION_SCREEN_ID
    };


    public static final String PURCHASE_EVENT_ADDED_BY_SERVER = "purchaseEventAddFromServer";
    public static final String DOMESTIC_PAY_MODE = "domesticpaymode";
    public static final String INTERNATIONAL_PAY_MODE = "internationalpaymode";
    public static final String REFER_AND_EARN = "referandearn";
    public static final String REFER_AND_EARN_AMOUNT = "referamount";
    public static final String UPDATE_DIALOG_SHOWN = "updatesialogshown";
    public static final String AI_SHOW_END_CHAT_BUTTON_AFTER = "aiEndChatBtnDuration";
    public static final String IS_PRICE_FILTER = "ispricefilterastrolist";
    public static final String AI_SHOW_PAID_END_CHAT_BUTTON_AFTER = "aiPaidEndChatBtnDuration";
    public static final String HUMAN_SHOW_END_CHAT_BUTTON_AFTER = "humanEndChatBtnDuration";
    public static String EXIT_SCREEN_CALL_BTN_CLICK_AI = "exit_screen_call_btn_ai";
    public static String DIRECT_SECOND_FREE_CHAT = "directsecondfreechat";
    public static String IS_3_MONTH_SUBS_PLAN_SHOWN = "isThreeMonthSubPlanVisible";
    public static String NEW_INSTALL_DIRECT_SECOND_FREE_CHAT = "newinstallrandomsecondchat";
    public static String SHOW_VAR_RECHARGE = "showvariableRechargeNew";
    public static String MIN_VAR_RECHARGE_AMOUNT = "variableRechargeMin";
    public static String MAX_VAR_RECHARGE_AMOUNT = "variableRechargeMax";
    public static String INTERNATIONAL_MIN_VAR_RECHARGE_AMOUNT = "variableRechargeMinInt";
    public static String INTERNATIONAL_MAX_VAR_RECHARGE_AMOUNT = "variableRechargeMaxInt";
    public static int MIN_VAR_RECHARGE_AMOUNT_VAL = 1;
    public static int MAX_VAR_RECHARGE_AMOUNT_VAL = 100000;
    public static double MAX_UPI_AMOUNT_VAL = 100000.0;
    //dark mode
    public static final String DARK_THEME = "dark";
    public static final String LIGHT_THEME = "light";
    public static final String SYSTEM_THEME = "system_theme";
    public static final String IS_DARK_MODE_SELECTED = "is_dark_mode_selected";
    public static final String APP_THEME_CHANGE_EVENT = "app_theme_change_event";

    public static final String DARK_MODE_THEME = "dark_mode_theme_type";
    public static final String CONNECT_CHAT_BEAN = "connect_chat_bean";
    public static final String ASTROLOGER_NAME = "astrologer_name";
    public static final String ASTROLOGER_PROFILE_URL = "astrologer_profile_url";
    public static final String ASTROLOGER_ID = "astrologer_id";
    public static final String USERCHATTIME = "userChatTime";
    public static final String ACCEPTED = "Accepted";

    public static final String chat_with_ai_astrologers = "chat-with-ai-astrologers";
    public static final String OPEN_FOR_AI_CHAT = "open_for_ai_chat";
    public static final String FREE_LIMT_EXCEED = "free_limit_exceed";
    public static final String IS_OPEN_AS_AD = "is_open_as_ad";
    public static final String IS_AD_FREE_BOTTOM = "is_ad_free_bottom";
    public static final String AI_KUNDLI_PLUS = "kundli-ai-plus";
    public static final String AI_KUNDLI_PLUS_DEEP_LINK = "https://www.astrosage.com/kundli-ai-plus";
    public static final String CREATE_KUNDLI_LINK = "https://www.astrosage.com/create-kundli";
    // public static final String OPEN_FOR_CHART_LIMIT_EXCEED = "open_for_chart_limit_exceed";
    public static final String ASTROSAGE_AI_PACKAGE_NAME = "com.ojassoft.astrosage_ai";
    public static final String IS_CALL_RECORDING_ENABLED_KEY = "is_call_recording_enabled_key";
    public static final String FREE_QUESTIONS_RECEIVED_KEY = "free_questions_received_key";
    public static final String ENABLED = "1";
    public static final String MODULE_SUGGESTED_QUESTIONS_KEY = "module_suggested_questions_key";
    public static final String MODULE_SUGGESTED_QUESTIONS_DATE_KEY = "module_suggested_questions_date_key";
    public static final String LAGNA_SUGGESTED_QUESTIONS_KEY = "module_suggested_questions_key";
    public static final String LAGNA_SUGGESTED_QUESTIONS_DATE_KEY = "module_suggested_questions_date_key";
    public static final String LAST_LANG_CODE_KEY = "last_lang_code_key";

    public static final String KUNDLI_AI_OPEN_FIRST_TIME_KEY = "kundli_ai_open_first_time1";

    public static final int NUMERO_KEY_POINTS = 101;
    public static final int NUMERO_RADICAL_NUMBER = 102;
    public static final int NUMERO_DESTINY_NUMBER = 103;
    public static final int NUMERO_NAME_NUMBER = 104;
    public static final int NUMERO_AUSPICIOUS_PLACE = 105;
    public static final int NUMERO_HEALTH = 106;
    public static final int NUMERO_AUSPICIOUS_TIME = 107;
    public static final int NUMERO_CAREER_CHOICES = 108;
    public static final int NUMERO_FASTS_AND_REMEDIES = 109;
    public static final int NUMERO_YANTRA = 110;
    public static final int NUMERO_LO_SHU_GRID = 111;
    public static final int NUMERO_EXCLUSIVE_FACTS = 112;
    public static final int NUMERO_ASK_QUESTIONS = 113;

    public static final int[] numerologyScreenIds = {
            NUMERO_KEY_POINTS,
            NUMERO_RADICAL_NUMBER,
            NUMERO_DESTINY_NUMBER,
            NUMERO_NAME_NUMBER,
            NUMERO_AUSPICIOUS_PLACE,
            NUMERO_HEALTH,
            NUMERO_AUSPICIOUS_TIME,
            NUMERO_CAREER_CHOICES,
            NUMERO_FASTS_AND_REMEDIES,
            NUMERO_YANTRA,
            NUMERO_LO_SHU_GRID,
            NUMERO_EXCLUSIVE_FACTS,
            NUMERO_ASK_QUESTIONS
    };

    public static String KEY_PERSONALIZED_NOTIFICATION_STATUS = "PERSONALIZED_NOTIFICATION_STATUS";

    public static final int AI_MATCHING_MODULE = 9;
    public static final int AI_HOROSCOPE_MODULE = 10;
    public static final int AI_PANCHANG_MODULE = 11;
    public static final int AI_LEARNING_MODULE = 12;
    public static final int AI_HOME_SCREEN_MODULE = 13;

    public static final int MATCHING_RESULTS = 114;
    public static final int MATCHING_DETAILS = 115;
    public static final int MATCHING_VARNA = 116;
    public static final int MATCHING_VASYA = 117;
    public static final int MATCHING_TARA = 118;
    public static final int MATCHING_YONI = 119;

    private static final int MATCHING_MAITRI = 120;
    private static final int MATCHING_GANA = 121;
    private static final int MATCHING_BHAKOOT = 122;
    private static final int MATCHING_NADI = 123;
    private static final int MATCHING_DOWNLOAD_PDF = 124;
    private static final int MATCHING_BIRTH_DETAILS = 125;
    private static final int MATCHING_ASK_QUESTIONS = 126;

    public static final String CONVERS_START = "CLDCHT";

    public static final int[] matchingScreenIds = {
            MATCHING_RESULTS,
            MATCHING_DETAILS,
            MATCHING_VARNA,
            MATCHING_VASYA,
            MATCHING_TARA,
            MATCHING_YONI,
            MATCHING_MAITRI,
            MATCHING_GANA,
            MATCHING_BHAKOOT,
            MATCHING_NADI,
            MATCHING_DOWNLOAD_PDF,
            MATCHING_BIRTH_DETAILS,
            MATCHING_ASK_QUESTIONS
    };

    public static final String IS_NEW_APP_INSTALL = "is_new_install";
    public static final String APP_INSTALL_TOPIC_TEMPLATE = "APP_INSTALL_";
    public static final String LAST_APP_USED_TOPIC_KEY = "last_app_used_topic_key";
    public static final String LAST_USED_HOUR_TOPIC_TEMPLATE = "LAST_USED_";
    public static final String TYPE_PASTE_BIRTH_DETAILS = "type_paste_birth_details";
    public static final String TYPE_PASTE_BIRTH_DETAILS_MATCHING = "type_paste_birth_details_matching";

    private static final int HOROSCOPE_DAILY = 127;
    private static final int HOROSCOPE_PANCHANG = 128;
    private static final int HOROSCOPE_WEEKLY = 129;
    private static final int HOROSCOPE_WEEKLY_LOVE = 130;
    private static final int HOROSCOPE_MOTHLY = 131;
    private static final int HOROSCOPE_YEARLY = 132;
    private static final int HOROSCOPE_ASK_QUESTIONS = 133;

    public static final int[] horoscopeScreenIds = {
            HOROSCOPE_DAILY,
            HOROSCOPE_PANCHANG,
            HOROSCOPE_WEEKLY,
            HOROSCOPE_WEEKLY_LOVE,
            HOROSCOPE_MOTHLY,
            HOROSCOPE_YEARLY,
            HOROSCOPE_ASK_QUESTIONS
    };

    private static final int PANCHANG_DASHBOARD = 134;
    private static final int PANCHANG_DAILY_PANCHANG = 135;
    public static final int PANCHANG_HOROSCOPE = 136;
    private static final int PANCHANG_HORA = 137;
    private static final int PANCHANG_CHOGADIA = 138;
    private static final int PANCHANG_DO_GHATI = 139;
    private static final int PANCHANG_RAHU_KAAL = 140;
    private static final int PANCHANG_PANCHAK = 141;
    private static final int PANCHANG_BHADRA = 142;
    public static final int PANCHANG_MUHURAT = 143;
    private static final int PANCHANG_LAGNA_TABLE = 144;
    public static final int PANCHANG_ASK_QUESTIONS = 145;

    public static final int[] panchangScreenIds = {
            PANCHANG_DASHBOARD,
            PANCHANG_DAILY_PANCHANG,
            PANCHANG_HOROSCOPE,
            PANCHANG_HORA,
            PANCHANG_CHOGADIA,
            PANCHANG_DO_GHATI,
            PANCHANG_RAHU_KAAL,
            PANCHANG_PANCHAK,
            PANCHANG_BHADRA,
            PANCHANG_MUHURAT,
            PANCHANG_LAGNA_TABLE,
            PANCHANG_ASK_QUESTIONS
    };

    private static final int LEARN_PLANETS = 146;
    private static final int LEARN_BENEFIC_AND_MALEFIC_PLANETS = 147;
    private static final int LEARN_NATURE_AND_SIGNIFICANCE_OF_PLANETS_1 = 148;
    private static final int LEARN_NATURE_AND_SIGNIFICANCE_OF_PLANETS_2 = 149;
    private static final int LEARN_ZODIAC_AND_ZODIAC_SIGN_EXPLAINED = 150;
    private static final int LEARN_BIRTH_CHART_EXPLAINED = 151;
    private static final int LEARN_SIGNIFICANCE_OF_HOUSES = 152;
    private static final int LEARN_EXALTATION_AND_DEBILITATION_OF_PLANETS = 153;
    private static final int LEARN_FRIENDSHIP_END_ENMITY_OF_PLANETS = 154;
    private static final int LEARN_PLANETARY_CONJUNCTION_DEMYSTIFIED = 155;
    private static final int LEARN_PLANETARY_ASPECT_EXPLAINED = 156;
    private static final int LEARN_15_RULES_FOR_PREDICTION = 157;
    private static final int LEARN_15_RULES_FOR_PREDICTION_EXPLAINED_WITH_EXAMPLE = 158;
    private static final int LEARN_YOGAS_FOR_SUCCESS_AND_PROSPERITY = 159;
    private static final int LEARN_SECRET_OF_RAJYOGA_REVEALED = 160;
    private static final int LEARN_CANCELLATION_OF_RAJYOGA = 161;
    private static final int LEARN_CONSTELLATION = 162;
    private static final int LEARN_SECRET_OF_TIMING_EVENTS = 163;
    private static final int LEARN_DASHAFAL_ANALYSIS_EXAMPLE = 164;
    private static final int LEARN_7_SECRETS_OF_TRANSIT_ANALYSIS = 165;
    private static final int LEARN_SHODASHVARGA_EXPLAINED = 166;
    private static final int LEARN_CHALIT_CHAKRA = 167;
    private static final int LEARN_HOW_TO_PREDICT_USING_BIRTH_CHART = 168;
    private static final int LEARN_AN_IMPORTANT_PRINCIPLE_FOR_PREDICTION = 169;
    private static final int LEARN_DISPOSITOR_THEORY_EXPLAINED = 170;

    public static final int[] learningScreenIds = {
            LEARN_PLANETS,
            LEARN_BENEFIC_AND_MALEFIC_PLANETS,
            LEARN_NATURE_AND_SIGNIFICANCE_OF_PLANETS_1,
            LEARN_NATURE_AND_SIGNIFICANCE_OF_PLANETS_2,
            LEARN_ZODIAC_AND_ZODIAC_SIGN_EXPLAINED,
            LEARN_BIRTH_CHART_EXPLAINED,
            LEARN_SIGNIFICANCE_OF_HOUSES,
            LEARN_EXALTATION_AND_DEBILITATION_OF_PLANETS,
            LEARN_FRIENDSHIP_END_ENMITY_OF_PLANETS,
            LEARN_PLANETARY_CONJUNCTION_DEMYSTIFIED,
            LEARN_PLANETARY_ASPECT_EXPLAINED,
            LEARN_15_RULES_FOR_PREDICTION,
            LEARN_15_RULES_FOR_PREDICTION_EXPLAINED_WITH_EXAMPLE,
            LEARN_YOGAS_FOR_SUCCESS_AND_PROSPERITY,
            LEARN_SECRET_OF_RAJYOGA_REVEALED,
            LEARN_CANCELLATION_OF_RAJYOGA,
            LEARN_CONSTELLATION,
            LEARN_SECRET_OF_TIMING_EVENTS,
            LEARN_DASHAFAL_ANALYSIS_EXAMPLE,
            LEARN_7_SECRETS_OF_TRANSIT_ANALYSIS,
            LEARN_SHODASHVARGA_EXPLAINED,
            LEARN_CHALIT_CHAKRA,
            LEARN_HOW_TO_PREDICT_USING_BIRTH_CHART,
            LEARN_AN_IMPORTANT_PRINCIPLE_FOR_PREDICTION,
            LEARN_DISPOSITOR_THEORY_EXPLAINED
    };

    public static final int LOVE_CATEGORY_SCREEN_ID = 175;
    private static final int CAREER_CATEGORY_SCREEN_ID = 176;
    private static final int MARRIAGE_CATEGORY_SCREEN_ID = 177;
    private static final int HEALTH_CATEGORY_SCREEN_ID = 178;
    private static final int EDUCATION_CATEGORY_SCREEN_ID = 179;
    private static final int BUSINESS_CATEGORY_SCREEN_ID = 180;
    private static final int STOCK_MARKET_CATEGORY_SCREEN_ID = 181;
    private static final int LEGAL_CATEGORY_SCREEN_ID = 182;

    public static final int[] homeScreenScreenIds = {
            LOVE_CATEGORY_SCREEN_ID,
            CAREER_CATEGORY_SCREEN_ID,
            MARRIAGE_CATEGORY_SCREEN_ID,
            HEALTH_CATEGORY_SCREEN_ID,
            EDUCATION_CATEGORY_SCREEN_ID,
            BUSINESS_CATEGORY_SCREEN_ID,
            STOCK_MARKET_CATEGORY_SCREEN_ID,
            LEGAL_CATEGORY_SCREEN_ID
    };

    public static final String[] below19UnmarriedCategoryList = {
            PersonalizedCategoryENUM.EDUCATION.name(),
            PersonalizedCategoryENUM.CAREER.name(),
            PersonalizedCategoryENUM.TODAY.name(),
            PersonalizedCategoryENUM.COUNSELLOR.name(),
            PersonalizedCategoryENUM.LOVE.name(),
            PersonalizedCategoryENUM.MENTAL_COACH.name(),
            PersonalizedCategoryENUM.HEALTH.name(),
            PersonalizedCategoryENUM.STOCK_MARKET.name(),
            PersonalizedCategoryENUM.MUHURAT.name(),
            PersonalizedCategoryENUM.WEIGHT_LOSS.name(),
//      PersonalizedCategoryENUM.POLITICS.name()
    };
    public static final String[] f19_25UnmarriedCategoryList = {
            PersonalizedCategoryENUM.LOVE.name(),
            PersonalizedCategoryENUM.MARRIAGE.name(),
            PersonalizedCategoryENUM.MUHURAT.name(),
            PersonalizedCategoryENUM.TODAY.name(),
            PersonalizedCategoryENUM.CAREER.name(),
            PersonalizedCategoryENUM.EDUCATION.name(),
            PersonalizedCategoryENUM.HEALTH.name(),
            PersonalizedCategoryENUM.WEIGHT_LOSS.name(),
            PersonalizedCategoryENUM.MENTAL_COACH.name(),
            PersonalizedCategoryENUM.STOCK_MARKET.name(),
            PersonalizedCategoryENUM.BUSINESS.name(),
            PersonalizedCategoryENUM.COUNSELLOR.name()
//            PersonalizedCategoryENUM.POLITICS.name(),
    };

    public static final String[] f19_25MarriedCategoryList = {
            PersonalizedCategoryENUM.LOVE.name(),
            PersonalizedCategoryENUM.MARRIAGE.name(),
            PersonalizedCategoryENUM.MUHURAT.name(),
            PersonalizedCategoryENUM.MOTHERHOOD.name(),
            PersonalizedCategoryENUM.TODAY.name(),
            PersonalizedCategoryENUM.CAREER.name(),
            PersonalizedCategoryENUM.EDUCATION.name(),
            PersonalizedCategoryENUM.HEALTH.name(),
            PersonalizedCategoryENUM.WEIGHT_LOSS.name(),
            PersonalizedCategoryENUM.STOCK_MARKET.name(),
            PersonalizedCategoryENUM.MENTAL_COACH.name(),
            PersonalizedCategoryENUM.BUSINESS.name(),
            PersonalizedCategoryENUM.COUNSELLOR.name(),
//            PersonalizedCategoryENUM.POLITICS.name(),
    };

    public static final String[] m19_25CategoryList = {
            PersonalizedCategoryENUM.CAREER.name(),
            PersonalizedCategoryENUM.MENTAL_COACH.name(),
            PersonalizedCategoryENUM.TODAY.name(),
            PersonalizedCategoryENUM.LOVE.name(),
            PersonalizedCategoryENUM.EDUCATION.name(),
            PersonalizedCategoryENUM.MARRIAGE.name(),
            PersonalizedCategoryENUM.COUNSELLOR.name(),
            PersonalizedCategoryENUM.MUHURAT.name(),
            PersonalizedCategoryENUM.WEIGHT_LOSS.name(),
            PersonalizedCategoryENUM.HEALTH.name(),
//            PersonalizedCategoryENUM.POLITICS.name(),
            PersonalizedCategoryENUM.STOCK_MARKET.name(),
            PersonalizedCategoryENUM.BUSINESS.name(),
            PersonalizedCategoryENUM.LEGAL.name()
    };

    public static final String[] f25_35MarriedCategoryList = {
            PersonalizedCategoryENUM.MOTHERHOOD.name(),
            PersonalizedCategoryENUM.MARRIAGE.name(),
            PersonalizedCategoryENUM.WEIGHT_LOSS.name(),
            PersonalizedCategoryENUM.HEALTH.name(),
            PersonalizedCategoryENUM.TODAY.name(),
            PersonalizedCategoryENUM.LOVE.name(),
            PersonalizedCategoryENUM.MUHURAT.name(),
            PersonalizedCategoryENUM.CAREER.name(),
            PersonalizedCategoryENUM.COUNSELLOR.name(),
            PersonalizedCategoryENUM.MENTAL_COACH.name(),
//            PersonalizedCategoryENUM.POLITICS.name(),
            PersonalizedCategoryENUM.STOCK_MARKET.name(),
            PersonalizedCategoryENUM.BUSINESS.name(),
            PersonalizedCategoryENUM.EDUCATION.name(),
            PersonalizedCategoryENUM.LEGAL.name()
    };

    public static final String[] f25_35UnmarriedList = {
            PersonalizedCategoryENUM.MARRIAGE.name(),
            PersonalizedCategoryENUM.WEIGHT_LOSS.name(),
            PersonalizedCategoryENUM.MUHURAT.name(),
            PersonalizedCategoryENUM.TODAY.name(),
            PersonalizedCategoryENUM.LOVE.name(),
            PersonalizedCategoryENUM.HEALTH.name(),
            PersonalizedCategoryENUM.CAREER.name(),
            PersonalizedCategoryENUM.COUNSELLOR.name(),
            PersonalizedCategoryENUM.STOCK_MARKET.name(),
            PersonalizedCategoryENUM.MENTAL_COACH.name(),
//            PersonalizedCategoryENUM.POLITICS.name(),
            PersonalizedCategoryENUM.BUSINESS.name(),
            PersonalizedCategoryENUM.EDUCATION.name(),
            PersonalizedCategoryENUM.LEGAL.name()
    };

    public static final String[] m25_35CategoryList = {
            PersonalizedCategoryENUM.CAREER.name(),
            PersonalizedCategoryENUM.MARRIAGE.name(),
            PersonalizedCategoryENUM.STOCK_MARKET.name(),
            PersonalizedCategoryENUM.TODAY.name(),
            PersonalizedCategoryENUM.BUSINESS.name(),
            PersonalizedCategoryENUM.MUHURAT.name(),
            PersonalizedCategoryENUM.LOVE.name(),
            PersonalizedCategoryENUM.MENTAL_COACH.name(),
            PersonalizedCategoryENUM.COUNSELLOR.name(),
//            PersonalizedCategoryENUM.POLITICS.name(),
            PersonalizedCategoryENUM.WEIGHT_LOSS.name(),
            PersonalizedCategoryENUM.HEALTH.name(),
            PersonalizedCategoryENUM.EDUCATION.name(),
            PersonalizedCategoryENUM.LEGAL.name()
    };
    public static final String[] mAfter40CategoryList = {
            PersonalizedCategoryENUM.HEALTH.name(),
            PersonalizedCategoryENUM.STOCK_MARKET.name(),
//            PersonalizedCategoryENUM.POLITICS.name(),
            PersonalizedCategoryENUM.BUSINESS.name(),
            PersonalizedCategoryENUM.MUHURAT.name(),
            PersonalizedCategoryENUM.LEGAL.name(),
            PersonalizedCategoryENUM.TODAY.name(),
            PersonalizedCategoryENUM.MENTAL_COACH.name(),
            PersonalizedCategoryENUM.COUNSELLOR.name(),
            PersonalizedCategoryENUM.WEIGHT_LOSS.name(),
            PersonalizedCategoryENUM.CAREER.name(),
            PersonalizedCategoryENUM.LOVE.name()
    };
    public static final String[] fAfter40CategoryList = {
            PersonalizedCategoryENUM.WEIGHT_LOSS.name(),
            PersonalizedCategoryENUM.HEALTH.name(),
            PersonalizedCategoryENUM.MUHURAT.name(),
            PersonalizedCategoryENUM.TODAY.name(),
            PersonalizedCategoryENUM.LEGAL.name(),
//            PersonalizedCategoryENUM.POLITICS.name(),
            PersonalizedCategoryENUM.MENTAL_COACH.name(),
            PersonalizedCategoryENUM.COUNSELLOR.name(),
            PersonalizedCategoryENUM.STOCK_MARKET.name(),
            PersonalizedCategoryENUM.BUSINESS.name(),
            PersonalizedCategoryENUM.LOVE.name()
    };
    public static String GOOGLE_ANALYTIC_KUNDLI_AI_MATCHING_CHAT_BUTTON_CLICK = "kundli_ai_matching_btn_click";
    public static final String UPDATE_PLAN_AFTER_TEN_CHARTS = "update_plan_after_ten_charts";
    public static final String UPDATE_PLAN_FROM_STATUS_MESSAGE = "update_plan_from_status_message";
    public static final String AI_CHAT_EXTEND_CHAT_RECHARGE = "ACECR";
    public static final String PERSONALIZED_AI_NOTIFICATION = "personalized_ai_notification";
    public static final String IS_FREE_CALL_ASTRO_AVAIL = "isFreeCallsAstroAvail";
    public static String BRIHAT_FROM_KUNDLI_DOWNLOADED_NOTIFICATION_PARTNER_ID = "BFKDN";
    public static String BRIHAT_FROM_KUNDLI_DOWNLOADED_NOTIFICATION_CLICKED = "get_brihat_notification_clicked";

    public static String NEW_BRIHAT_SCREEN_OPEN_EVENT = "new_brihat_screen_open";
    public static String NEW_BRIHAT_SCREEN_PDF_KUNDLI_BTN_CLICK = "new_brihat_pdf_kundli_click";
    public static String NEW_BRIHAT_SCREEN_PRINTED_KUNDLI_BTN_CLICK = "new_brihat_printed_kundli_click";
    public static String ANALYTICS_ARTICLE_CLICK_EVENT = "article_click_event_analytics";
    public static String HOME_PRODUCT_CATEGORY_CLICK = "home_product_category";
    public static String HOME_ASTROLOGY_LESSON_CLICK = "homehome_product_category_astrology_lesson_category";
    public static String HOME_REPORTS_CLICK = "home_report_click";
    public static String HOME_YOUTUBE_VIDEO_CLICK = "home_youtube_video_item_click";
    public static String HOME_ASTROLOGER_LIST_ITEM_CLICK = "home_astrologer_list_item_click";
    public static String HOME_AI_ASTROLOGER_LIST_ITEM_CLICK = "home_ai_astrologer_list_item_click";

    public static final String PLAN_UPGRADE_FOR_DHRUV_FROM_TAB = "plan_upgrade_for_dhruv_from_tab";
    public static final String PLAN_UPGRADE_FOR_KUNDLI_AI_PLUS_FROM_TAB = "plan_upgrade_for_kundli_ai_plus_from_tab";
    public static final String PLAN_UPGRADE_FOR_KUNDLI_AI_PLUS_FROM_KUNDLI = "plan_upgrade_for_kundli_ai_plus_from_kundli";
    public static final String PLAN_UPGRADE_FOR_DHRUV_FROM_KUNDLI = "plan_upgrade_for_dhruv_from_kundli";
    public static final String FREE_TRAIL_INFO_SCREEN_SHOWN = "free_trial_info_screen_shown";
    public static final String PLAN_UPGRADE_FOR_ASTROSAGE_AI_PLUS_FROM_DIALOG = "plan_upgrade_for_astrosage_ai_plus_from_dialog";
    public static final String PLAN_UPGRADE_FOR_ASTROSAGE_AI_PLUS_RAZORPAY = "plan_upgrade_for_astrosage_ai_plus_razorpay";
    public static final String PLAN_UPGRADE_FOR_ASTROSAGE_AI_PLUS_INTENT_FLOW = "plan_upgrade_for_astrosage_ai_plus_intent_flow";
    public static final String PLAN_UPGRADE_FOR_ASTROSAGE_AI_PLUS_INTENT_FLOW_APP_LUNCHED = "plan_upgrade_for_astrosage_ai_plus_intent_flow_app_lunched";
    public static final String PLAN_UPGRADE_FOR_ASTROSAGE_AI_PLUS_RAZORPAY_SUCCESS = "plan_upgrade_for_astrosage_ai_plus_razorpay_success";
    public static final String PLAN_UPGRADE_FOR_KUNDLI_AI_PLUS_RAZORPAY_SUCCESS = "plan_upgrade_for_kundli_ai_plus_razorpay_success";
    public static final String PLAN_UPGRADE_FOR_DHRUV_PLAN_RAZORPAY_SUCCESS = "plan_upgrade_for_dhruv_plan_razorpay_success";
    public static final String PLAN_UPGRADE_FOR_DHRUV_PLAN_RAZORPAY_SUCCESS_FRIM_AI_PASS = "plan_upgrade_for_dhruv_plan_razorpay_success_AI_PASS";
    public static final String PLAN_UPGRADE_FOR_ASTROSAGE_AI_PLUS_RAZORPAY_FAILURE = "plan_upgrade_for_astrosage_ai_plus_razorpay_failure";
    public static final String PLAN_UPGRADE_INTENT_FLOW_FAILURE = "plan_upgrade_intent_flow_failure";
    public static final String PLAN_UPGRADE_INTENT_FLOW_SUCCESS = "plan_upgrade_intent_flow_success";
    public static final String PLAN_UPGRADE_FOR_ASTROSAGE_AI_PLUS_GOOGLE = "plan_upgrade_for_astrosage_ai_plus_google";
    public static final String PLAN_UPGRADE_FOR_ASTROSAGE_AI_PLUS_SUCCESS_DIALOG_SHOW = "plan_upgrade_for_astrosage_ai_plus_success_dialog_show";
    public static final String PLAN_UPGRADE_FOR_ASTROSAGE_AI_PLUS_SUCCESS_DIALOG_SHOW_OLD = "plan_upgrade_for_astrosage_ai_plus_success_dialog_show";
    public static final String PLAN_UPGRADE_FOR_ASTROSAGE_AI_PLUS_NOT_LOGGED_IN = "plan_upgrade_for_astrosage_ai_plus_not_logged_in";
    public static final String PLAN_UPGRADE_FOR_ASTROSAGE_AI_DHRUV_NOT_LOGGED_IN = "plan_upgrade_for_astrosage_ai_dhruv_not_logged_in";
    public static final String EMAIL_IS_VERIFIED = "is_email_verified";
    public static final String BRIHAT_KUNDLI_FROM_SHORTCUT_EVENT = "brihat_kundli_from_shortcut_event_key";
    public static final String FREE_BRIHAT_KUNDLI_DOWNLOAD_BTN_CLICK_EVENT = "free_brihat_kundli_download_btn_click_event";
    public static final String FREE_BRIHAT_KUNDLI_Thanks_PAGE_OPEN_EVENT = "free_brihat_kundli_thanks_page_open_event";
    public static final String SHARE_APP_BTN_FROM_BRIHAT_THANKS_PAGE = "share_app_btn_event_from_brihat_thanks_page";
    public static final String HOME_BTN_FROM_BRIHAT_THANKS_PAGE = "home_btn_event_from_brihat_thanks_page";
    public static final String BRIHAT_KUNDLI_EMAIL_INPUT_SCREEN_EVENT = "brihat_kundli_email_input_screen_open";
    public static final String BRIHAT_KUNDLI_EMAIL_SUBMIT_BTN_CLICK_EVENT = "brihat_kundli_email_submit_btn_click_event";
    public static final String BRIHAT_KUNDLI_OTP_VERIFY_SCREEN_OPEN_EVENT = "brihat_kundli_otp_verify_screen_open_event";
    public static final String BRIHAT_KUNDLI_OTP_VERIFY_BTN_CLICK_EVENT = "brihat_kundli_otp_verify_btn_click_event";
    public static final String BK_EMAIL_INPUT_SCREEN_BACK_BTN_CLICK = "bk_email_input_back_btn_click_event";
    public static final String BK_EMAIL_Verify_SCREEN_BACK_BTN_CLICK = "bk_email_verify_back_btn_click_event";
    public static final String OTHERS_SERVICES_CLICK_EVENT = "home_others_service_item_click_event";
    public static final String APP_SHARE_AND_EARN_BUTTON_CLICK = "app_share_and_earn_button_click";
    public static final String APP_SHARE_APP_WITH_FRIEND = "app_share_app_with_friend";
    public static final String APP_SHARE_AND_EARN_VIA_WHATSAPP = "app_share_and_earn_via_whatsapp";
    public static final String APP_SHARE_AND_EARN_VIA_APPLINK = "app_share_and_earn_via_applink";
    public static final String APP_SHARE_AND_EARN_VIA_WHATSAPP_NOT_LOGGED_IN = "app_share_and_earn_via_whatsapp_not_logged_in";
    public static final String APP_SHARE_AND_EARN_VIA_APPLINK_NOT_LOGGED_IN = "app_share_and_earn_via_applink_not_logged_in";

    public static final String BRIHAT_SERVICE_DEEPLINK_URL = "https://buy.astrosage.com/service/astrosage-brihat-horoscope";
    public static final String BRIHAT_PRODUCT_DEEPLINK_URL = "http://buy.astrosage.com/miscellaneous/astrosage-brihat-horoscope";
    public static final String PLAN_AI_PASS_RAZORPAY_FAILURE = "plan_ai_pass_razorpay_failure";
    public static final String PLAN_AI_PASS_RAZORPAY_SUCCESS = "plan_ai_pass_razorpay_success";
    public static final String PURCHASE_AI_CONSULTATION_CLOSE_BUTTON_CLICK = "purchase_ai_consultation_close_button_click";
    public static final String PURCHASE_AI_CONSULTATION_SUBSCRIBE_BUTTON_CLICK = "PURCHASE_AI_CONSULTATION_SUBSCRIBE_BUTTON_CLICK";
    public static final String AI_PASS_PURCHASE = "AI_PASS_PURCHASE";
    public static final String SOURCE_FROM_KUNDLI_CLOUD_FRAG = "source_from_kundli_cloud_frag";
    public static final String PDF_DOWNLOAD_COUNT_UPDATED = "pdf_download_count_updated";
    public static final String REPORT_ITEM_CLICK_IN_KUNDLI = "reports_item_click_in_kundli";
    public static final String REPORT_ITEM_CLICK_BUY_NOW = "reports_item_click_buy_now";
    public static final String SHOW_AI_PASS_PLAN_KEY = "showaipassplan";
    public static final String SHOW_UNLIMITED_AI_CHAT_PLAN_VIEW = "show_unlimited_ai_chat_plan_view";
    public static final String UNLIMITED_AI_CHAT_PLAN_WALLET_SCREEN = "unlimited_ai_chat_plan_wallet_screen";
    public static final String UNLIMITED_AI_CHAT_PLAN_LOW_BALANCE = "unlimited_ai_chat_plan_low_balance";
    public static final String REMAINING_REPORT_COUNT_URL = "https://aspdf.astrosage.com/api/getRemainingPdfCount";
    public static final String KEY_DOMESTIC_GST_RATE = "domesticGSTRate";
    public static final String KEY_INTERNATIONAL_GST_RATE = "internationalGSTRate";
    public static final String KEY_DOMESTIC_DOLLAR_CR = "domesticDollerCR";
    public static final String KEY_MIN_RECHARGE_AMOUNT = "popunderminrechargeamt";
    public static final String KEY_INTERNATIONAL_DOLLAR_CR = "internationalDollerCR";
    public static final String KEY_FOR_CATEGORY_ID = "key_for_category_id";
    public static final String KEY_CATEGORY_ID = "category_id";
    public static final String KEY_UTM_CAMPAIGN = "utm_campaign";
    public static final String CALL_NOW_BTN_FROM_DIALOG_CLICK = "call_now_btn_from_dialog_click";
    public static final String KEY_AK_RTDB_LONG_POLLING = "AK_RTDB_Long_Polling";
    public static final String GOOGLE_ANALYTICS_HUMAN_CHAT_BACK_PRESS = "analytics_on_human_chat_back_press";
    public static final String GOOGLE_ANALYTICS_AI_CHAT_BACK_PRESS = "analytics_on_ai_chat_back_press";
    public static final String CHAT_COMPLETED_KEY = "chat_completed";
    public static final String DURING_CHAT_KEY = "during_chat";
    public static final int BACK_FROM_PROFILE_CHAT_DIALOG = 2001;
    public static final String RECHARGE_SERVICE_OFFER_RESPONSE_KEY = "rcharge_service_offer_response_key";
    public static final String EXTEND_CHAT = "extend-chat";
    public static final String RECHARGE_AFTER_FREE_CHAT = "Recharge-After-Free-Chat";
    public static final String CONTINUE_CHAT = "continue-chat";
    public static final String BOTTOMSERVICELISTUSEDFOR = "bottomServiceListUsedFor";
    public static final String INTERSTITIAL_SESSION_TIME = "intersticial_session_time";
    public static final String PLAN_SCREEN_OPEN_FOR_AD = "plan_screen_open_for_intersticial_ad";

    public static final String DHRUV_UPGRADE_SUBSCRIBE_BUTTON_CLICK = "dhruv_upgrade_subscribe_btn_click";
    public static final String IS_AI_CALL_DEFAULT_SPEAKER_ON = "is_ai_call_on_speaker";
    public static final String CLICKED_CATEGORY_ENUM_KEY = "clickedCategoryEnumKey";
    public static final String IS_OPENED_FROM_K_AI_CHAT_BTN = "is_opened_from_kundli_ai_chat_btn";
    public static final String  SHOPPING_CART_VIA_WEBVIEW = "ShoppingCartViaWebView";

    public static final String  ASTROSAGE_SHOP_URL_RUDRAKSHA = "https://astrosage.shop/collections/rudraksha";
    public static final String  ASTROSAGE_SHOP_URL_YANTRA = "https://astrosage.shop/collections/yantra";
    public static final String  ASTROSAGE_SHOP_URL_GEMSTONES = "https://astrosage.shop/collections/gemstones";
    public static final String  ASTROSAGE_SHOP_URL_MALA = "https://astrosage.shop/collections/mala";
    public static final String  ASTROSAGE_SHOP_URL_BRACELET = "https://astrosage.shop/collections/bracelet";
    public static final String  ASTROSAGE_SHOP_URL_PENDANT = "https://astrosage.shop/collections/pendant";

    public static final String  ASTROSAGE_SHOP_CAT_RUDRAKSHA = "cat_rudraksha";
    public static final String  ASTROSAGE_SHOP_CAT_YANTRA = "cat_yantra";
    public static final String  ASTROSAGE_SHOP_CAT_GEMSTONES = "cat_gemstones";
    public static final String  ASTROSAGE_SHOP_CAT_MALA = "cat_mala";
    public static final String  ASTROSAGE_SHOP_CAT_BRACELET = "cat_bracelet";
    public static final String  ASTROSAGE_SHOP_CAT_PENDANT = "cat_pendant";

    public static final String  AI_Pass_Daily_Chat_Count = "AIPassDailyChatCount";
    public static final String  PURCHASE_SOURCE_FROM_DHRUV_DIALOG_AFTER_LIMIT_EXCEED = "dhruvPurchaseAfterLimitExceed";
    public static final String  AKANDROID = "akandroid";
    public static final String  AKHOMELIST = "akhomelist";
    public static final String  AKHOMETAB = "akhometab";
    public static final String  AKNOTIFICTIONCLICK = "aknotificationclick";
    public static final String  COMPLETE_URL = "complete_url";

    public static final String  IS_AUTO_SYNC_CHART_ENABLED = "isAutoSyncChartEnabled";
    public static final String  IS_LOGIN_MANDATORY= "isLoginMandatory";
    public static final String  PRODUCT_CATEGORY_NOTIFICATION= "product_category_notification";
    public static final String  SHOW_NEW_CONNECT_CHAT_DIALOG= "showNewConnectChatDialog";

    public static final String IS_HOME_SCREEN_KUNDLI_AI_POPUP_SHOWN = "showHomescreenKundliAIPopup";
    public static final String IS_INTERSTICIAL_ENABLED = "isIntersticialEnabled";
    public static final String IS_SHOW_FIVE_RUPEE_OFFER = "isShownFiveRupeeOffer";
    public static final String GET_USER_CHAT_CATEGORY_ENABLED = "getUserChatCategoryEnabled";
    public static final String IS_STRIPE_VISIBLE = "stripeVisibilityForVartaWallet";
    public static final String IS_PHONEPE_CHECKOUT_VISIBLE = "phonePeCheckoutVisibilityForVartaWallet";
    public static final String INTERNATIONAL_DEFAULT_ORDER_PAYMENT_METHOD = "internationalDefaultOrderPaymentMethod";
    public static final String SPECIAL_RECHARGE_25_SHOWN = "special_recharge_25_dialog_shown";
    public static final String SPECIAL_RECHARGE_25_BTN_CLICK = "special_recharge_25_button_click";
    public static final String SPECIAL_RECHARGE_25_DISMISS = "special_25_dismiss_click";
    public static final String AI_CALL_EXTEND_BTN_CALICK = "ai_call_extend_call_btn_click";
    public static final String IS_SHOW_25_RUPEE_RECHARGE_OFFER = "isShown25RupeeRecharge";
    public static final String ISSHOWCREDANDBHIM = "isShowCredAndBhim";
    public static final String IS_INSUFFICIENT_SUGGESTED_RECHARGE_DIALOG_SHOW = "isInsufficientRechargePopUnderShow";
    public static final String IS_FIREBASE_TOKEN_REFRESH = "isFirebaseTokenRefresh";
    public static final String SHOW_NEW_STYLE_NOTIFICATION = "isShowNewStyleNotification";
}
