package com.libojassoft.android.utils;


import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Environment;

/*
 * This is a global variables class
 */
public class LibCGlobalVariables {

    //	public static MyAppNotification myAppNotificationObjectForDownloadApplication=null;
    public static final int OPEN_METHOD_TYPE_DOWNLOAD_APPLICATION = 0;
    public static final int OPEN_METHOD_TYPE_WEB = 1;
    public static String APP_NOTIFICATION_PREFERENCE = "ojasappnotify";
    public static String URL_NOTIFICATION = "http://old.astrosage.com/astrosage-xml/popupnotification_xml.xml";

    //public static String rssFeedURL = "http://feeds.feedburner.com/astrosage/GiaE?format=xml";
    //public static String rssFeedURLHindi = "http://feeds.feedburner.com/astrosage/YtFw?format=xml";
    //MODIFIED BY BIJENDRA ON 8-AUG-13
    public static String rssFeedURL = "https://feeds.feedburner.com/astrosage/wIqS?format=xml";
    //public static String rssFeedURL = "http://feeds.feedburner.com/AmberThemeBlog?format=xml";
    public static String rssFeedURLHindi = "https://feeds.feedburner.com/astrosage/rKyc?format=xml";
    //END
    public final static String API2_BASE_URL = "https://api2.astrosage.com/";
    public static String ASTROCAMP_BASE_URL = "https://astrocamp.com/";
    public static String ATLAS_BASE_URL = "https://japi.astrosage.com/";

    public static String PREFIX_PREF_KEY = "OJAS_";

    public static String PACKAGE_NAME = "PACKAGE_NAME";
    public static final int OJAS_ACT_NOTIFICATION_CODE = 100;

    public static final int DEVICE_SMALL = 0;
    public static final int DEVICE_NORMAL = 1;
    public static final int DEVICE_LARGE = 2;
    public static final int DEVICE_XLARGE = 3;

    //FOR MAGAZINE NOTIFICATION
    //BIJENDRA ON 3-APRL-2013
    //public static final String MAGAZINE_NOTIFICATION_FILE_NAME= "/mazno.txt";
    public static final String MAGAZINE_NOTIFICATION_FILE_NAME = "/jmazno.txt";
    public static final String MAGAZINE_NOTIFICATION_FILE_NAME_HINDI = "/jmaznohi.txt";

    public static final String MAGAZINE_NOTIFICATION_CONFIG_FILE_NAME = "/jconfig.txt";
    public static final String USER_CONFIG_FILE_NAME = "/uconfig.txt";

    public static final String SETTING_DIRECTORY_NAME = Environment.getExternalStorageDirectory().getAbsolutePath() + "/.astronot";
    //END
    //MISC APPLICATION INNER URL ON 5-APRL-2013

    public static String CONST_JCHINESEASTROLOGY_URI = "http://m2.astrosage.com/chineseastrology/";
    public static String CONST_JRASHIPHAL_URI = "http://m2.astrosage.com/rashifal/";
    public static String CONST_JRASHIPHAL_ENG_URI = "http://m2.astrosage.com/horoscope/";
    public static String CONST_JKUNDLIMATCHING_URI = "http://m2.astrosage.com/horoscopematching/";
    public static String CONST_JLOVEMATCH_URI = "http://m2.astrosage.com/compatibility/";
    public static String CONST_JLOVEMATCH_HINDI_URI = "http://m2.astrosage.com/compatibility/default.asp?languageCode=1";
    public static String CONST_JLOVEMATCH_ENGLISH_URI = "http://m2.astrosage.com/compatibility/default.asp?languageCode=0";
    public static String CONST_JNUMEROLOGY_URI = "http://m2.astrosage.com/numerology/";
    public static String CONST_JPORUTHAM_TAMIL_URI = "http://m2.astrosage.com/tamil/";
    public static String CONST_JTODAYSHOROSCOPE_URI = "http://m2.astrosage.com/horoscope/";
    //END

    public static String MAGZINE_NOTIFICATION_SERVICE_NAME = "com.libojassoft.android.custom.services.AstroSageMagazineNotificationService";
    public static boolean IS_MAGZINE_NOTIFICATION_SERVICE_RUNNING = false;
    //added by bijendra on 13-april-2013
    public static String CALLER_APP_AD_ID = "CALLER_APP_AD_ID";
    
/*    public static final int CONST_ASTROSAGE_KUNDLI_SERVICE_START_DELAY = 2000;
    public static final int CONST_JASTROLOGY_SERVICE_START_DELAY = 5000;
    public static final int CONST_HOROSCOPE_MATCHING_SERVICE_START_DELAY = 70000;
    public static final int CONST_JCHINESEASTROLOGY_SERVICE_START_DELAY = 10000;
    public static final int CONST_LEARN_ASTROLOGY_SERVICE_START_DELAY = 12000;
    public static final int CONST_JHINDIRASHIFAL_SERVICE_START_DELAY = 15000;
    public static final int CONST_JKUNDLIMATCHING_SERVICE_START_DELAY = 20000;
    public static final int CONST_JLOVEMATCH_SERVICE_START_DELAY = 25000;
    public static final int CONST_JNUMEROLOGY_SERVICE_START_DELAY = 30000;
    public static final int CONST_JPORUTHAM_SERVICE_START_DELAY = 35000;
    public static final int CONST_JTODAYSHOROSCOPE_SERVICE_START_DELAY = 40000;
    public static final int CONST_BABY_NAMES_SERVICE_START_DELAY = 43000;
    public static final int CONST_RAHUKALAM_SERVICE_START_DELAY = 46000;
    public static final int CONST_HOROSCOPE_2013_SERVICE_START_DELAY = 49000;    
    public static final int CONST_HOROSCOPE_2012_13_SERVICE_START_DELAY = 52000;
    public static final int CONST_HORA_MUHURT_SERVICE_START_DELAY = 55000;
    public static final int CONST_CALENDAR_SERVICE_START_DELAY = 58000;
    public static final int CONST_RAM_SHALAKA_SERVICE_START_DELAY = 61000;
    public static final int CONST_DAILY_HOROSCOPE_SERVICE_START_DELAY = 64000;*/

    public static final int CONST_ASTROSAGE_KUNDLI_SERVICE_START_DELAY = 20000;//10000;  //MODIFIED BY BIJENDRA ON 9-JULY-13
    public static final int CONST_ASTROSAGE_BOOTSTRAP_KUNDLI_SERVICE_START_DELAY = 30000;
    public static final int CONST_HOROSCOPE_MATCHING_SERVICE_START_DELAY = 120000;
    public static final int CONST_HOROSCOPE_2012_13_SERVICE_START_DELAY = 180000;
    public static final int CONST_DAILY_HOROSCOPE_SERVICE_START_DELAY = 240000;
    public static final int CONST_JASTROLOGY_SERVICE_START_DELAY = 240000;
    public static final int CONST_JCHINESEASTROLOGY_SERVICE_START_DELAY = 300000;
    public static final int CONST_LEARN_ASTROLOGY_SERVICE_START_DELAY = 360000;
    public static final int CONST_JHINDIRASHIFAL_SERVICE_START_DELAY = 420000;
    public static final int CONST_JKUNDLIMATCHING_SERVICE_START_DELAY = 480000;
    public static final int CONST_JLOVEMATCH_SERVICE_START_DELAY = 540000;
    public static final int CONST_JNUMEROLOGY_SERVICE_START_DELAY = 600000;
    public static final int CONST_JPORUTHAM_SERVICE_START_DELAY = 660000;
    public static final int CONST_JTODAYSHOROSCOPE_SERVICE_START_DELAY = 720000;
    public static final int CONST_BABY_NAMES_SERVICE_START_DELAY = 780000;
    public static final int CONST_RAHUKALAM_SERVICE_START_DELAY = 840000;
    public static final int CONST_HOROSCOPE_2013_SERVICE_START_DELAY = 180000;

    public static final int CONST_HORA_MUHURT_SERVICE_START_DELAY = 960000;
    public static final int CONST_CALENDAR_SERVICE_START_DELAY = 1020000;
    public static final int CONST_RAM_SHALAKA_SERVICE_START_DELAY = 1080000;

    public static final int CONST_HOROSCOPE_2013_SERVICE_START_DELAY_SECOND = 1200000;


    public static String NEW_JASTROLOGY_ARTICLE_INTENT = "MY_JASTROLOGY_ARTICLE_UPDATE_OJASSOFT";
    public static String NEW_JCHINESEASTROLOGY_ARTICLE_INTENT = "MY_JCHINESEASTROLOGY_ARTICLE_UPDATE_OJASSOFT";
    public static String NEW_JHINDIRASHIPHAL_ARTICLE_INTENT = "MY_JHINDIRASHIPHAL_ARTICLE_UPDATE_OJASSOFT";
    public static String NEW_JKUNDLIMATCHING_ARTICLE_INTENT = "MY_JASHTAKOOT_ARTICLE_UPDATE_OJASSOFT";
    public static String NEW_JLOVE_MATCH_ARTICLE_INTENT = "MY_JCOMPATIBILITY_ARTICLE_UPDATE_OJASSOFT";
    public static String NEW_JNUMEROLOGY_ARTICLE_INTENT = "MY_JNUMEROLOGY_ARTICLE_UPDATE_OJASSOFT";
    public static String NEW_JPORUTHAM_ARTICLE_INTENT = "MY_JPORUTHAM_ARTICLE_UPDATE_OJASSOFT";
    public static String NEW_JTODAYSHOROSCOPE_ARTICLE_INTENT = "MY_JTODAYSHOROSCOPE_ARTICLE_UPDATE_OJASSOFT";
    public static String NEW_BABYNAMES_ARTICLE_INTENT = "MY_BABYNAMES_ARTICLE_UPDATE_OJASSOFT";
    public static String NEW_RAHUKALAM_ARTICLE_INTENT = "MY_RAHUKALAM_ARTICLE_UPDATE_OJASSOFT";
    public static String NEW_JCALENDER_ARTICLE_INTENT = "MY_JCALENDER_ARTICLE_UPDATE_OJASSOFT";
    public static String NEW_JRAMSHALAKA_ARTICLE_INTENT = "MY_JRAMSHALAKA_ARTICLE_UPDATE_OJASSOFT";
    public static String NEW_HORO_ARTICLE_INTENT = "MY_HORO_ARTICLE_UPDATE_OJASSOFT";
    public static String NEW_DAILY_HOROSCOPE_ARTICLE_INTENT = "MY_DAILY_HOROSCOPE_ARTICLE_UPDATE_OJASSOFT";
    public static String NEW_HOROSCOPE_2013_ARTICLE_INTENT = "MY_HOROSCOPE_2013_ARTICLE_UPDATE_OJASSOFT";
    public static String NEW_HOROSCOPE_2013_ARTICLE_INTENT_SECOND = "MY_HOROSCOPE_2013_ARTICLE_UPDATE_OJASSOFT_SECOND";
    public static String NEW_HOROSCOPE_MATCHING_ARTICLE_INTENT = "MY_HOROSCOPE_MATCHING_ARTICLE_UPDATE_OJASSOFT";
    public static String NEW_LEARN_ASTROLOGY_ARTICLE_INTENT = "MY_LEARN_ASTROLOGY_ARTICLE_UPDATE_OJASSOFT";
    public static String NEW_ASTROSAGE_KUNDLI_ARTICLE_INTENT = "MY_ASTROSAGE_KUNDLI_ARTICLE_UPDATE_OJASSOFT";
    public static String NEW_ASTROSAGE_BOOTSTRAP_KUNDLI_ARTICLE_INTENT = "MY_ASTROSAGE_BOOTSTRAP_KUNDLI_ARTICLE_UPDATE_OJASSOFT";

    public static String NEW_UNIVERSAL_ARTICLE_INTENT_ACTION = "MY_UNIVERSAL_ARTICLE_UPDATE_OJASSOFT_ON_REBOOT";

    //THESE VARIABLES ARE WRITTEN FOR USER DISCLAIMER
    //BIJENDRA:17-APRIL-2013
    public static final int CODE_DISCLAIMER_ACT = 512;
    public static final int CODE_CONFIG_ARTICLE_NOTIFICATION_ACT = 513;

    public static final int CONST_DISCLAIMER_AGREE = 0;
    public static final int CONST_DISCLAIMER_DISAGREE = 1;

    public static String DISCLAIMER_AGREE_RESULT_CODE = "AG_RESULT";
    public static final String DISCLAIMER_AGREE_PREFS_NAME = "OjasUserDisclamierPref";
    //END DISCLAIMER VARIABLES

    //BIJENDRA:22-APRIL-2013
    public static int MAGZINE_FIRST_CYCLE_FLAG = 0;
    public static int MAGZINE_SECOND_CYCLE_FLAG = 0;
    public static final int MAGZINE_FIRST_CYCLE_TIME = 9;//9;
    public static final int MAGZINE_SECOND_CYCLE_TIME = 17;//17;
    public static final int MAGZINE_CYCLE_TIME_INTERVAL = 2;//2;

    public static final String JSON_MAGAZINE_TODAY = "today";
    public static final String JSON_MAGAZINE_BOLG_ID = "blogid";
    public static final String JSON_MAGZINE_FIRST_CYCLE_FLAG = "round_first";
    public static final String JSON_MAGZINE_SECOND_CYCLE_FLAG = "round_second";
    public static String TODAY_DATE_ID = "";
    public static String MAGAZINE_BOLG_ID = "";

    //END DISCLAIMER VARIABLES
    public static String EXTRA_INTERNET_ON = "NET_CON";
    public static boolean IS_ACTIVITY_ON = false;

    //PLACE SEARCH URL #ADDED BY BIJENDRA ON 30-MAY-13
    /* public static final String ATRROSAGE_ATLAS_PLACE_SEARCH="http://app.astrosage.com/astrosage-xml/placedetailsv2.asp?placename=";*/ //DISABLED BY BIJENDRA (04-04-14)
    // public static final String ASTROSAGE_ATLAS_PLACE_SEARCH="http://app.astrosage.com/astrosage-xml/placedetailsv2.asp";//ADDED BY BIJENDRA (04-04-14)
    /* public static final String ATRROSAGE_ATLAS_GET_PLACE_DETAIL="http://app.astrosage.com/astrosage-xml/placedetails.asp?id=";*/
    // public static final String ASTROSAGEE_ATLAS_GET_PLACE_DETAIL="http://app.astrosage.com/astrosage-xml/placedetails.asp?id=";
    //END

    /* public static final String ASTROSAGE_ATLAS_PLACE_SEARCH="http://findplaces-970.appspot.com/findPlaces?placename=";
     public static final String ASTROSAGEE_ATLAS_GET_PLACE_DETAIL="http://findplaces-970.appspot.com/findPlaces?id=";*/
    public static final String ASTROSAGE_ATLAS_PLACE_SEARCH = ATLAS_BASE_URL + "astrosage-xml/placedetailsv3.asp?placename=";
    public static final String ASTROSAGEE_ATLAS_GET_PLACE_DETAIL = ATLAS_BASE_URL + "astrosage-xml/placedetailsv3.asp?id=";

    /**
     * THIS ARRAY IS FOR ASTRO SHOW
     * BIJENDRA(29-JUNE-13)
     */
    public static String[] LIB_ASTRO_SHOP_PAGES = {ASTROCAMP_BASE_URL+"mobile/products.asp?src=",
            ASTROCAMP_BASE_URL+"mobile/default.asp?src="};

    public static Typeface SHOW_FONT_TYPE = Typeface.DEFAULT;
    public static int LIB_ENGLISH = 0;
    public static int LIB_HINDI = 1;
    public static int LIB_LANGUAGE_CODE = 0;

    public static String COMPAIGN_URL_NOTIFICATION = "src=app_src&utm_source=android&utm_medium=notification&utm_campaign=app_name";

    public static String GOOGLE_ANALYTIC_SRC_ASTROSAGEKUNDLI = "8";
    public static String GOOGLE_ANALYTIC_SRC_HOROSCOPEMATCHING = "10";
    public static String GOOGLE_ANALYTIC_SRC_HOROSCOPE2013 = "9";


    public static String GOOGLE_ANALYTIC_ASTROSAGEKUNDLI_NAME = "astro-Kundli";
    public static String GOOGLE_ANALYTIC_HOROSCOPEMATCHING_NAME = "horo-matching";

    public static String GOOGLE_ANALYTIC_JASTROLOGY_NAME = "j-astrology";
    public static String GOOGLE_ANALYTIC_SHARE_MARKET_NAME = "sensex-nifty-astrology";
    public static String GOOGLE_ANALYTIC_CRICKET_NAME = "cricket-astrology";

    public static String GOOGLE_ANALYTIC_HOROSCOPE2012_NAME = "horo-2012";
    public static String GOOGLE_ANALYTIC_HOROSCOPE2013_NAME = "horo-2013";
    public static String GOOGLE_ANALYTIC_DAILYHOROSCOPE_NAME = "daily-horo";
    public static String GOOGLE_ANALYTIC_ZODIAC_SIGNS = "zodiac-signs";

    public static String ASTROSAGE_BLOG_HOME_PAGE = "https://astrology.astrosage.com";//ADDED BY BIJENRA ON 6-JULY-13
    public static String ASTROSAGE_BLOG_HOME_PAGE_HINDI = "https://jyotish.astrosage.com";


    //ADDED BY BIJENRA ON 9-JULY-13
    public static int ASTRO_SHOP_PAGE_INDEX = 0;
    public static int ASK_OUR_ASTROLOGER_PAGE_INDEX = 1;
    //END 
    //ADDED BY BIJENRA ON 18-JULY-13
    public static Activity activity;
    public static Context context;


    public static final int NEVER = 0;
    public static final int ONCE_IN_A_DAY = 1;
    public static final int TWICE_IN_A_DAY = 2;
    public static final int AS_MANY_TIME_IT_COMES = 3;
    public static final String JSON_USER_CHOICE = "userChoice";
    //For Hindi blog notification
    public static final String HINDI_BLOG_SETTING_JSON_OBJECT_NAME = "HindiBlogSettings";
    public static final String HINDI_LOCALE_CODE = "hi";
    //ADDED BY BIJENDRA ON 12-NOV-13
    public static final String TAMIL_LOCALE_CODE = "ta";
    public static final String MARATHI_LOCALE_CODE = "mr";
    public static final String TELUGU_LOCALE_CODE = "te";
    public static final String GUJARATI_LOCALE_CODE = "gu";
    public static final String BENGALI_LOCALE_CODE = "bn";
    public static final String URDU_LOCALE_CODE = "ur";
    //END

    public static final String JSON_USER_LANGUAGE = "json_ulan";
    public static final String JSON_USER_LOGIN = "json_ulog";
    public static final String JSON_USER_PWD = "json_upwd";
    public static final String JSON_USER_AUTO_LOGIN = "json_alog";
    public static final String JSON_USER_NOTIFICATION_CHOICE = "json_unco";

    public static final String CAT_SCREEN = "SCREEN";
    public static final String ANALYITICS_ACTION_NOTIFICATION_CONFIG_SCREEN = "Open-Notification-Config-Screen";

    public static final String ONCE_IN_A_DAY_txt = "Once-in-a-day";
    public static final String TWICE_IN_A_DAY_txt = "Twice-in-a-day";
    public static final String AS_MANY_TIME_IT_COMES_txt = "As-many-time-as-it-comes";
    public static final String NEVER_txt = "Never";
    //tejinder
    public static String FEED_BACK = API2_BASE_URL + "as/astrosage-xml/feedback-xml-v3.asp";//ADDED BY BIJENDRA ON 2-NOV-13 //UPDATED 29-DEC_2015

    public static boolean userDetailAvailble = false;


    public static String useridsession = "";
    public static String name = "";
    public static String day = "";
    public static String month = "";
    public static String year = "";
    public static String hour = "";
    public static String min = "";
    public static String sec = "";
    public static String place = "";
    public static String timezone = "";
    public static String longdeg = "";
    public static String longmin = "";
    public static String longew = "";
    public static String latdeg = "";
    public static String latmin = "";
    public static String latns = "";
    public static String dst = "";
    public static String ayanamsa = "";


    public static enum LANGUAGE_TYPE {
        HINDI, ENGLISH, TAMIL
    }


    public static enum RASHI_TYPE {
        ARIES, TAURUS, GEMINI, CANCER, LEO, VIRGO, LIBRA, SCORPIO, SAGGITTARIUS, CAPRICORN, AQUARIUS, PISCES
    }

    public static String NOTIFICATION_CHANNEL_ID = "notification_channelid";
    static final public String NOTIFICATION_TYPE = "NotificationType";
    public static int NOTIFICATION_PUSH = 1;
    public static int NOTIFICATION_ARTICLE = 2;
    static final public String UPDATE_NOTIFICATION_COUNT = "UpdateNotificationCount";
    public static String KEY_NOTIFICATION_COUNT = "notificationCount";

    static final String LINK = "link";
    static final String TITLE = "title";
    static final String ITEM = "item";//
    static final String CHANNEL = "channel";//
    static final String GUID = "guid";
    static final String PUB_DATE = "pubDate";
    static final String DESCRIPTION = "description";
    static final String ENTRY = "entry";
    static final String THUMBNAIL = "thumbnail";
    static final String URL = "url";
    static final String VIDEO_ID = "videoId";
    static final String PUBLISHED_DATE = "published";

    static final String HEALTH = "Health";
    static final String CAREER = "Career";
    static final String Love_Marriage_PersonalRelations = "Love_Marriage_PersonalRelations";
    static final String Advice = "Advice";
    static final String General = "General";
    static final String Finance = "Finance";
    static final String Trade_Finance = "Trade_Finance";
    static final String Family_Friends = "Family_Friends";

    public static final String NOTIFICATION_PREF_KEY = "NotificationPref";
    public static final String EN_MAGAZINE_NOTIFICATION_PREF_KEY = "en_magazine_noti_key";
    public static final String HI_MAGAZINE_NOTIFICATION_PREF_KEY = "hi_magazine_noti_key";
    public static final String NOTIFICATION_CHOICE_PREF_KEY = "nbotification_user_choice_key";
    public static final String KEY_USER_ID = "us";
    public static final String KEY_EMAIL_ID = "ma";
    public static final String KEY_PASSWORD = "pw";
    public static final String KEY_AS_USER_ID = "asus";
    public static final String FACEBOOK_ADS_KEY = "a82bd88994b6cc499f9758f1a2d0beaff8913efbf4b7f2158fddac6251bce14d";
}
