package com.ojassoft.astrosage.constants;


public interface AppConstants {


    /*alert type*/
    public static final int ALERT_TYPE_NO_NETWORK = 0x01;
    public static final int ALERT_TYPE_DELETE_NOTE = 0x02;
    public static final int ALERT_TYPE_DELETE_REMINDAR = 0x03;

    /* animation type*/
    public static final int ANIMATION_SLIDE_UP = 0x01;
    public static final int ANIMATION_SLIDE_LEFT = 0x02;


    /* splash screen*/
    public static final int SPLASH_TIME = 2000;
    public static final int APP_RATE_TIME = 90 * 1000;

    /* App Tag*/
    public static final String APP_NAME = "IndNotes";
    public static final String DEVICE_TYPE = "Android";
    public static final int CALENDER_YEAR = 2019;
    public static final int CALENDER_PREV_YEAR = 2018;
    /* Request Tag*/
    public static final int REQUEST_TAG_NO_RESULT = 0x01;
    public static final int REQUEST_TAG_NOTES = 0x02;

    /* DashboardActivity*/
    public static final int APP_EXIT_TIME = 2000;

    public static final int HOME = 0;
    public static final int CALENDAR = 1;
    public static final int HOLIDAYS = 2;
    public static final int DATEWISE_NOTES = 3;
    public static final int GENERAL_NOTES = 4;
    public static final int CHANGE_LANGUAGE = 5;
    public static final int REMINDER = 6;
    public static final int SHARE_IT = 7;
    public static final int ABOUT_US = 8;

    /*Font*/
    public static final String FONT_ROBOTO_REGULAR = "fonts/Roboto-Regular.ttf";
    public static final String FONT_ROBOTO_MEDIUM = "fonts/Roboto-Medium.ttf";
    public static final String FONT_DIGITAL_MONO = "font/digital-7-mono.ttf";

    public static final String app_language_key = "AppLanguagePerf";
    public static final String IS_LANGUAGE_SELECTED = "isLanguageSelected";
    public static final String IS_APP_RATED = "IS_APP_RATED";
    public static final String APP_PREFS_NAME = "CalendarPref";

    public static final int ENGLISH = 1;
    public static final int HINDI = 2;

    public static final String KEY_REMIND_TEXT = "remindText";
    public static final String KEY_REMIND_DATE = "remindDate";
    public static final String KEY_MONTH = "month";
    public static final String KEY_YEAR = "year";
    public static final String KEY_SEARCHED_STR = "searchedString";
    public static final String KEY_NOTES_MODEL = "notesModel";
    public static final String DIR_PDF = "/pdf/";
    public static final String DIR_IMAGES = "/images/";
    public static final String KEY_CATEGORY_ID = "selectedCategoryId";
    public static final String HISTORY_LIST = "historyList";
    public static String NOTIFICATION_CHANNEL_ID = "CHID_NOTIFICATION";
    public static String REMINDAR_CHANNEL_ID = "CHID_REMINDAR";

    public static final int December18 = 0;
    public static final int January = 1;
    public static final int February = 2;
    public static final int March = 3;
    public static final int April = 4;
    public static final int May = 5;
    public static final int June = 6;
    public static final int July = 7;
    public static final int August = 8;
    public static final int September = 9;
    public static final int October = 10;
    public static final int November = 11;
    public static final int December = 12;

    public static final int CAL_DAY_WIDTH = 116;
    public static final int CAL_DAY_HEIGHT = 105;
    public static final int CAL_Y_MARGIN = 135;
    public static final int CAL_X_MARGIN = 10;

/*    public static final int CAL_DAY_WIDTH = 304;
    public static final int CAL_DAY_HEIGHT = 292;
    public static final int CAL_Y_MARGIN = 350;
    public static final int CAL_X_MARGIN = 30;*/

  /*  public static final int PI_CAL_DAY_WIDTH = 63;
    public static final int PI_CAL_DAY_HEIGHT = 64;
    public static final int PI_CAL_Y_MARGIN = 84;
    public static final int PI_CAL_X_MARGIN = 6;*/

    public static final int PI_CAL_DAY_WIDTH = 68;
    public static final int PI_CAL_DAY_HEIGHT = 66;
    public static final int PI_CAL_Y_MARGIN = 85;
    public static final int PI_CAL_X_MARGIN = 8;

    public static String call_on_first_number_9560267006 = "+91-9560267006";
    public static String call_on_second_number_120_4138503 = "+911204138503";
    public final static String ASTRO_WEBVIEW_TITLE_KEY = "Astro_webview_title_key";
    public final static int MODULE_ABOUT_US = 37;

    public static final String ISInstanceID = "ISInstanceID";
    public static final String PROPERTY_REG_ID = "registration_id";
    public static final String PROPERTY_APP_VERSION = "appVersion";
    public static final String PREF_PUSH_NOTIFICATION_TOKEN = "AstroSageKundliPN";
    public static final String TOPIC_ALL_NEED_TO_SEND_TO_GOOGLE_SERVER = "TOPIC_ALL_NEED_TO_SEND_TO_GOOGLE_SERVER"; // default value should be ALL.
    public static final String LANGUAGE_TOPIC_NEED_TO_SEND_TO_GOOGLE_SERVER = "LANGUAGE_TOPIC_NEED_TO_SEND_TO_GOOGLE_SERVER"; // value should be language name like - ENGLISH,HINDI etc.
    public static final String VERSION_TOPIC_NEED_TO_SEND_TO_GOOGLE_SERVER = "VERSION_TOPIC_NEED_TO_SEND_TO_GOOGLE_SERVER"; // value should be V+Version Code

    public static final String HELP_SCREEN_CALENDAR_CLICK = "HELP_SCREEN_CALENDAR_CLICK";
    public static final String HELP_SCREEN_CALENDAR_ZOOM = "HELP_SCREEN_CALENDAR_ZOOM";
    public static final String HELP_SCREEN_CATEGORY_CLICK = "HELP_SCREEN_CATEGORY_CLICK";

    //Topics to registered
    public static final String TOPIC_ALL = "CA_CA_ALL";
    public static final String TOPIC_VERSION = "CA_CA_V";
    public static final String TOPIC_ENGLISH = "CA_CA_ENG";
    public static final String TOPIC_HINDI = "CA_CA_HIN";
}
