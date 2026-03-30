package com.ojassoft.astrosage.ui.act;

import android.app.Activity;
import android.app.Application;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.NotificationCompat;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.billingclient.api.BillingClient;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.libojassoft.android.utils.LibCUtils;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.utils.PersistentCookieStore;
import com.ojassoft.astrosage.utils.PopUpLogin;
import com.ojassoft.astrosage.varta.model.AstrologerDetailBean;
import com.ojassoft.astrosage.varta.model.LiveAstrologerModel;

import com.ojassoft.astrosage.varta.receiver.AstroLiveDataManager;

import com.ojassoft.astrosage.varta.service.AstroAcceptRejectService;
import com.ojassoft.astrosage.varta.service.ChatRunningBackgroundTimer;

import com.ojassoft.astrosage.varta.service.Loginservice;
import com.ojassoft.astrosage.varta.twiliochat.chat.messages.ChatMessage;
import com.ojassoft.astrosage.varta.ui.activity.ChatWindowActivity;
import com.ojassoft.astrosage.varta.ui.activity.DashBoardActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.vartalive.activities.VideoCallActivity;
import com.ojassoft.astrosage.vartalive.activities.VoiceCallActivity;


import org.json.JSONArray;
import org.json.JSONObject;

import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.util.List;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


public class AstrosageKundliApplication extends Application implements LifecycleObserver {

    public static final String TAG = "TwilioChat";
    public static boolean IS_CALL_BROAD_ACTION_SEEN  = false;
    //public static TruecallerSdkScope trueScope;
    public static BillingClient billingClient;
    //public static Activity currentActivity;
    public static boolean wasInBackground;
    public static boolean isDasboardActivityVisible;
    public static FirebaseDatabase mFirebaseInstance;
    public static DatabaseReference mFirebaseDatabase;
    public static LiveAstrologerModel liveAstrologerModel;
    public static String liveRemoteAudioState;
    public static String liveLocalAudioState;
    public static String runningCallsIdLog = "";
    public static boolean leaveChannelState;
    private static AstrosageKundliApplication astrosageKundliApplication;
   // public InterstitialAd interstitialAd;
    public boolean IsRateing_dialogChecked;
    public boolean IsShare_dialogChecked;
    //Current time in millis for showing InterstitialAd in the gap of 2 min.
    public long currentTimeMillis = 0;
    private boolean isInterstitialAdRequested = false;
    private int LANGUAGE_CODE = com.ojassoft.astrosage.utils.CGlobalVariables.ENGLISH;
    private Activity mCurrentActivity = null;
    public static String facebookDeepLinkUrl;
    public static boolean isUserFirstTimeInstallTheApp;
    //catching app global unhandled exceptions for TagManager
    public static String currentEventType = "";

    public static long chatTimerRemainingTime;
    public static AstrologerDetailBean lastChatAIAstrologerDetailBean;
    public static AstrologerDetailBean selectedAstrologerDetailBean;
    public static AstrologerDetailBean astrologerDetailBeanForBusyDialog;
    public static AstrologerDetailBean chatAgainAstrologerDetailBean;
    public static AstrologerDetailBean chatAstrologerDetailBeanAfterRecharge;
    public static String checkWhichConsultationScreen = "";

    public static boolean isOpenVartaPopup;

    public static String chatJsonObject = "";
    public static String channelIdTempStore = "";
    public static boolean isChatScreenOpes;
    public static boolean isAgoraCallScreenOpened;
    public static String ASTROLOGER_ID = "", AI_ASTROLOGER_ID = "";
    public static boolean isChatAgainButtonClick = false;
    public static List<ChatMessage> chatMessagesHistory;
    public static List<ChatMessage> allChatMessagesHistory;
    public static List<ChatMessage> kundliChatMessages;
    public static String KUNDLI_CHAT_CONVERSATION_ID="";
    public static BeanHoroPersonalInfo lastKundliDetails = new BeanHoroPersonalInfo();
    public static TreeSet<Integer> statusMessageSetHistory;
    public static String currentChatStatus ="";

    public static String currentConsultType ="";

    public static boolean isEndChatReqOnGoing = false;
    public static boolean isEndChatCompleted;
    public static int currentDisplayedNotificationId;
    public static int backgroundLoginCountForChat,status100BgLoginCount;
    public static String currentDisplayedNotificationTag="";
    public static  boolean isBackFromChat,isBackFromCall;
    //public static  long endChatTimeShowMilliSeconds  = TimeUnit.MINUTES.toMillis(1);
    public static  long endChatTimeShowMilliSeconds  = TimeUnit.SECONDS.toMillis(60);
    public static String astroGroup = "";
    public static boolean connectinternetcall;
    //public static boolean initialiseChatView;
    //public static boolean initialiseChatRunningView;
    public static boolean IS_AGORA_CALL_RUNNING;
    public static String aIChatSupportedLanguages;
    public static boolean connectAiChatAfterLogin;
    public static boolean connectAiCallAfterLogin;
    public static boolean connectHumanChatAfterLogin;
    public static String apiCallingSource ="";
    public static String chatCallconfigType ="";
    public static String astroListLogs = "";
    //public static boolean showAIFreePopup;
    public static String notificationIssueLogs = "";
    public static String aINotificationIssueLogs = "";
    public static String paymentScreenLogs = "";
    public static String webViewIssue = "";
    public static String brokenMessageLogs = "";
    public static PopUpLogin popUpLogin;
    private Thread.UncaughtExceptionHandler mDefaultUEH;
    private final Thread.UncaughtExceptionHandler mCaughtExceptionHandler = new Thread.UncaughtExceptionHandler() {
        @Override
        public void uncaughtException(Thread thread, Throwable ex) {
            try {
                String classpath = null;
                if (ex != null && ex.getStackTrace().length > 0)
                    classpath = ex.getStackTrace()[0].toString();
                if (classpath != null && ex.getMessage().contains("Results have already been set") && classpath.contains("com.google.android.gms.tagmanager")) {
                    // ignore
                    // Log.e("Exception","Exception");
                } else {
                    // run your default handler
                    mDefaultUEH.uncaughtException(thread, ex);
                }
            } catch (Exception e) {
                //
            }
        }
    };

    public static AstrosageKundliApplication getAppContext() {
        return astrosageKundliApplication;
    }

    public static DatabaseReference getmFirebaseDatabase(String channelIDD) {
        //channelIDD = "FCH9911764722P137A1620053834631";
        if (channelIDD == null || channelIDD.isEmpty()) {
            channelIDD = "";
        }
        if (mFirebaseInstance == null) {
            mFirebaseInstance = FirebaseDatabase.getInstance();
        }
        mFirebaseDatabase = mFirebaseInstance.getReference(CGlobalVariables.CHANNELS).child(channelIDD);
        return mFirebaseDatabase;
    }

    public boolean isIsShare_dialogChecked() {
        return IsShare_dialogChecked;
    }

    public void setIsShare_dialogChecked(boolean isShare_dialogChecked) {
        IsShare_dialogChecked = isShare_dialogChecked;
    }

    public void setIsRateing_dialogChecked(boolean isRateing_dialogChecked) {
        IsRateing_dialogChecked = isRateing_dialogChecked;
    }

    private String appThemeFromPref = com.ojassoft.astrosage.utils.CGlobalVariables.LIGHT_THEME;

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        boolean isRTDBLongPolling = CUtils.getBooleanData(this, com.ojassoft.astrosage.utils.CGlobalVariables.KEY_AK_RTDB_LONG_POLLING,true);
        if(isRTDBLongPolling){
            System.setProperty("firebase.database.forceLongPolling", "true");
            FirebaseDatabase.getInstance().setPersistenceEnabled(true);
        }
        super.onCreate();
        astrosageKundliApplication = this;

        try {
            ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
        } catch (Exception e) {
            e.printStackTrace();
        }
        setAppThemeAccordingToUserPref();

        new Thread(() -> {
            try {
                boolean isUserFirstTimeInstallTheApp = CUtils.getBooleanData(astrosageKundliApplication, com.ojassoft.astrosage.utils.CGlobalVariables.IS_FIRST_INSTALL_KEY, true);
                AstrosageKundliApplication.isUserFirstTimeInstallTheApp = isUserFirstTimeInstallTheApp;
                if (isUserFirstTimeInstallTheApp) {
                    CUtils.saveBooleanData(astrosageKundliApplication, com.ojassoft.astrosage.utils.CGlobalVariables.IS_FIRST_INSTALL_KEY, false);
                    CUtils.saveStringData(astrosageKundliApplication, com.ojassoft.astrosage.utils.CGlobalVariables.FIRST_INSTALL_TIME_KEY, String.valueOf(CUtils.getCurrentTimeMillisecAccordingToUTC()));
                    // not logged-in topic subscribe
                    com.ojassoft.astrosage.varta.utils.CUtils.unSubscribeTopics("", com.ojassoft.astrosage.varta.utils.CGlobalVariables.TOPIC_LOGGEDIN, this);
                    com.ojassoft.astrosage.varta.utils.CUtils.subscribeTopics("", com.ojassoft.astrosage.varta.utils.CGlobalVariables.TOPIC_NOT_LOGGEDIN, this);
                }
                SharedPreferences sharedPreferencesForLang = getSharedPreferences(com.ojassoft.astrosage.utils.CGlobalVariables.MYKUNDLI_LANGUAGE_PREFS_NAME, Context.MODE_PRIVATE);
                LANGUAGE_CODE = sharedPreferencesForLang.getInt(com.ojassoft.astrosage.utils.CGlobalVariables.APP_PREFS_AppLanguage, com.ojassoft.astrosage.utils.CGlobalVariables.ENGLISH);
            } catch (Exception e) {
                //
            }

            try {
                CookieManager cookieManager = new CookieManager(new PersistentCookieStore(AstrosageKundliApplication.this), CookiePolicy.ACCEPT_ORIGINAL_SERVER);
                CookieHandler.setDefault(cookieManager);
            } catch (Exception e) {
                //
            }

            try {
                mDefaultUEH = Thread.getDefaultUncaughtExceptionHandler();
                Thread.setDefaultUncaughtExceptionHandler(mCaughtExceptionHandler);
            } catch (Exception e) {
                //
            }

        }).start();
        AstrosageKundliApplication.isOpenVartaPopup = false;
        AstrosageKundliApplication.backgroundLoginCountForChat = 0;
        AstrosageKundliApplication.status100BgLoginCount = 0;

    }

    public int getLanguageCode() {
        return LANGUAGE_CODE;
    }

    public void setLanguageCode(int langaugeCode) {
        LANGUAGE_CODE = langaugeCode;
    }

    private void setAppThemeAccordingToUserPref(){
        appThemeFromPref = com.ojassoft.astrosage.utils.CUtils.getAppDarkMode(AstrosageKundliApplication.this);
        setAppTheme(appThemeFromPref);
        /*ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    //Background thread
                    try {
                        appThemeFromPref = com.ojassoft.astrosage.utils.CUtils.getAppDarkMode(AstrosageKundliApplication.this);
                    } catch (Exception e) {
                        //
                    }

                    //UI process
                    handler.post(() -> {
                        setAppTheme(appThemeFromPref);
                    });
                }catch (Exception e){
                    //
                }
            }
        });*/
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void onMoveToForeground() {
        // app moved to foreground
        wasInBackground = true;
        if (CGlobalVariables.chatTimerTime != 0) {
            ChatRunningBackgroundTimer.getInstance(this).cancelTimer();
        }
        CUtils.cancelChatNotification(astrosageKundliApplication);
        callBackgroundLogin();
    }

    private void sendCallCustomPushNotification() {
            Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.call_color_logo_large);
            String title = "";
            if(AstrosageKundliApplication.selectedAstrologerDetailBean!=null){
                title =  AstrosageKundliApplication.selectedAstrologerDetailBean.getName();
            }
            int notificationId = (int) System.currentTimeMillis();
            String consultType="";
            String msg ="";
            Intent resultIntent ;
        if(AstrosageKundliApplication.currentConsultType!=null){
            if(AstrosageKundliApplication.currentConsultType.equals("audio_call")){
                consultType = CGlobalVariables.TYPE_VOICE_CALL;
                 msg = getResources().getString(R.string.title_ongoing_voice_call);
            }else  if(AstrosageKundliApplication.currentConsultType.equals("video_call")){
                consultType = CGlobalVariables.TYPE_VIDEO_CALL;
                 msg = getResources().getString(R.string.title_ongoing_video_call);
            }
        }
        if(consultType.equals(CGlobalVariables.TYPE_VIDEO_CALL)){
            resultIntent = new Intent(this, VideoCallActivity.class);
        }else{
            resultIntent = new Intent(this, VoiceCallActivity.class);
        }
        resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
        resultIntent.putExtra("ongoing_notification", true);
            PendingIntent pending = PendingIntent.getActivity(this, notificationId, resultIntent, PendingIntent.FLAG_IMMUTABLE);

            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,CGlobalVariables.NOTIFICATION_CHANNEL_ID)
                    .setOngoing(true)
                    .setContentTitle(title)
                    .setContentText(msg)
                    .setSmallIcon(R.drawable.chat_logo)
                    .setColor(getResources().getColor(R.color.Orangecolor))
                    .setLargeIcon(icon).setDefaults(Notification.DEFAULT_SOUND)
                    .setContentIntent(pending).setChannelId(CGlobalVariables.NOTIFICATION_CHANNEL_ID)
                    .setAutoCancel(false)
                    .setPriority(NotificationCompat.PRIORITY_HIGH)
                    .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                    .setStyle(new NotificationCompat.BigTextStyle().bigText(msg));


            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel notificationChannel = new NotificationChannel(CGlobalVariables.NOTIFICATION_CHANNEL_ID, "agora_call_notification", NotificationManager.IMPORTANCE_HIGH);
                notificationChannel.enableLights(true);
                notificationChannel.setLightColor(Color.RED);
                //notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
                notificationChannel.enableVibration(true);
                mNotificationManager.createNotificationChannel(notificationChannel);
            }

            // using the same tag and Id causes the new notification to replace an existing one
            mNotificationManager.notify(CGlobalVariables.ONGOING_CALL_NOTIFICATION, CGlobalVariables.ONGOING_CALL_NOTIFICATION_ID, notificationBuilder.build());
        }


    @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
    public void onMoveToBackground() {
        // app moved to background
        wasInBackground = false;
        //Toast.makeText(vartaUserApplication, "app in background", Toast.LENGTH_SHORT).show();
        if (!TextUtils.isEmpty(CUtils.getSelectedAstrologerID(this)) && !TextUtils.isEmpty(CUtils.getSelectedChannelID(this))) {
            if (CGlobalVariables.chatTimerTime != 0) {
                ChatRunningBackgroundTimer.getInstance(this).startTimer();
            }
        }
    }
    public static DatabaseReference astroDbRef;
    public static ValueEventListener valueEventListener;
    public static Runnable runnable;
    public static String ASTROLOGER_ACCEPTED_DATA = "";
    public static boolean isPrefetchServiceRunning;

    public static void startChatServiceTimer(String channelId, String chatJsonObject, String name, String imageFile, String astrologerId) {
        try {
            runnable = new Runnable() {
                @Override
                public void run() {
                    if (!CUtils.checkServiceRunning(AstroAcceptRejectService.class)) {
                        getDataFromChannel(channelId,chatJsonObject,name,imageFile,astrologerId);
                    }
                }
            };
            new Handler().postDelayed(runnable,15000);
        }catch (Exception e){
        //
        }


    }
    public static void getDataFromChannel(String channelIDD, String chatJsonObject, String name, String imageFile, String astrologerId) {
        /**
         *
         */
        AstrosageKundliApplication.getAppContext().getEndChatData(channelIDD);

        valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {
                    ASTROLOGER_ACCEPTED_DATA = (String) dataSnapshot.getValue();
                    if (ASTROLOGER_ACCEPTED_DATA.equalsIgnoreCase("true")) {
                        Log.d("chat_test_new", "Called Accept chat from Firebase Listener");

                        performOperations(channelIDD, this, CGlobalVariables.ASTROLOGER_ACCEPTED,chatJsonObject,name,imageFile,astrologerId);
                    } else if (ASTROLOGER_ACCEPTED_DATA.equalsIgnoreCase("false")) {
                        try {
                            CUtils.cancelOnDisconnentEvent(channelIDD);
                            Log.d("chat_test_new", "Called Reject chat from Firebase Listener");

                            //performOperations(channelIDD, this, CGlobalVariables.ASTROLOGER_REJECTED);
                            CGlobalVariables.CHAT_END_STATUS = CGlobalVariables.ASTROLOGER_NO_ANSWER;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {



            }
        };
        astroDbRef = AstrosageKundliApplication.getmFirebaseDatabase(channelIDD).child(CGlobalVariables.ASTROLOGER_ACCEPTED_FBD_KEY);
        astroDbRef.addValueEventListener(valueEventListener);

    }
    public static void performOperations(String channelIDD, ValueEventListener valueEventListener, String actionType, String chatJsonObject, String name, String imageFile, String astrologerId) {
        if (actionType.equals(CGlobalVariables.ASTROLOGER_ACCEPTED)) {
            AstrosageKundliApplication.currentChatStatus = CGlobalVariables.CHAT_ACCEPTED;
            AstrosageKundliApplication.channelIdTempStore = "";
            CUtils.fcmAnalyticsEvents("astrologer_accept_chat_request_firebase", AstrosageKundliApplication.currentEventType, "");

            if (AstrosageKundliApplication.isActivityVisible()) {
                Log.d("chat_test_new", "App is in forground ");
                Log.d("chat_test_new", "  ");
                sendBroadcastMesage(CGlobalVariables.ASTROLOGER_ACCEPTED,channelIDD,chatJsonObject,name,imageFile,astrologerId);

                //sendBroadcastViewTimer(0);
                Log.d("chat_test_new", "stopService  AstroAcceptRejectService  ");

            }
        } else {
            AstrosageKundliApplication.currentChatStatus = CGlobalVariables.CHAT_REJECTED;
            sendBroadcastMesage(CGlobalVariables.ASTROLOGER_REJECTED,channelIDD,chatJsonObject,name,imageFile,astrologerId);
            CGlobalVariables.chatTimerTime = 0;
            AstrosageKundliApplication.chatTimerRemainingTime = 0;
            AstrosageKundliApplication.selectedAstrologerDetailBean = null;
            AstrosageKundliApplication.chatJsonObject = "";
            AstrosageKundliApplication.channelIdTempStore = "";
            CUtils.saveAstrologerIDAndChannelID(getAppContext(), "", "");
            AstroLiveDataManager.updateViews("0");
            //sendBroadcastViewTimer(0);
            CUtils.fcmAnalyticsEvents("astrologer_reject_chat_request", AstrosageKundliApplication.currentEventType, "");
            Log.d("testChatNew", "stopService  AstroAcceptRejectService  ");
        }
        AstrosageKundliApplication.getmFirebaseDatabase(channelIDD).child(CGlobalVariables.ASTROLOGER_ACCEPTED_FBD_KEY).removeEventListener(valueEventListener);

    }
    public static void sendBroadcastMesage(String status,String channelIDD,  String chatJsonObject, String name, String imageFile, String astrologerId) {
        try {
            Context context = AstrosageKundliApplication.getAppContext();
            Intent intent = new Intent(CGlobalVariables.SEND_BROADCAST_CHAT_ACCEPTED_OR_REJCET);
            intent.putExtra(CGlobalVariables.STATUS, status);
            intent.putExtra(CGlobalVariables.CHAT_USER_CHANNEL, channelIDD);
            intent.putExtra(CGlobalVariables.CONNECT_CHAT_BEAN, chatJsonObject);
            intent.putExtra(CGlobalVariables.ASTROLOGER_NAME, name);
            intent.putExtra(CGlobalVariables.ASTROLOGER_PROFILE_URL, imageFile);
            intent.putExtra(CGlobalVariables.CHAT_ASTROLOGER_ID, astrologerId);
            intent.putExtra(CGlobalVariables.USER_CHAT_TIME, CGlobalVariables.userChatTime);
            //intent.putExtra("chatInitiateAstrologerDetailBean",astrologerDetailBean);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callBackgroundLogin() {
        try {
            boolean isLogin = CUtils.getUserLoginStatus(this);
            if (isLogin) {
                Intent intentService = new Intent(this, Loginservice.class);
                startService(intentService);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Activity getCurrentActivity() {
        return mCurrentActivity;
    }

    public void setCurrentActivity(Activity mCurrentActivity) {
        this.mCurrentActivity = mCurrentActivity;
    }

    public void getEndChatData(final String channelIDD) {

        if (TextUtils.isEmpty(channelIDD)) return;
        AstrosageKundliApplication.getmFirebaseDatabase(channelIDD).child(CGlobalVariables.END_CHAT_OVER_FBD_KEY).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {
                    if (dataSnapshot == null) return;
                    Log.d("TestChat", dataSnapshot.getValue() + "");
                    boolean isEndChat = (boolean) dataSnapshot.getValue();

                    if (isEndChat && !isDasboardActivityVisible) {
                        try {
                            CUtils.cancelOnDisconnentEvent(DashBoardActivity.CHANNEL_ID);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }catch (Exception e){
                    //
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onTerminate() {
        super.onTerminate();
    }

    public enum TrackerName {
        APP_TRACKER, // Tracker used only in this app.
        GLOBAL_TRACKER, // Tracker used by all the apps from a company.
        // eg:roll-up tracking.
        ECOMMERCE_TRACKER, // Tracker used by all ecommerce transactions from a
        // company.
    }

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static boolean liveActivityVisible;


    public static void activityResumed() {
        activityVisible = true;
    }


    public static void activityPaused() {
        activityVisible = false;
    }

    private static boolean activityVisible;
    private void sendCustomPushNotification( ) {
        String msg = getString(R.string.ongoing_call);
        Bitmap icon = BitmapFactory.decodeResource(getResources(), R.drawable.call_color_logo_large);

        int notificationId = LibCUtils.getRandomNumber();
        // pending intent is redirection using the deep-link
        //If url contains Play store then use Action view else open the App
        Intent resultIntent = new Intent(this, ChatWindowActivity.class);
        resultIntent.putExtra("ongoing_notification", true);
        resultIntent.setAction(Intent.ACTION_VIEW);
        //resultIntent.setData(Uri.parse(link));

        PendingIntent pending = PendingIntent.getActivity(this, notificationId, resultIntent, PendingIntent.FLAG_IMMUTABLE);


        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this,CGlobalVariables.NOTIFICATION_CHANNEL_ID)
                        .setOngoing(true)
                        .setContentTitle(CUtils.getStringData(this, CGlobalVariables.TITLE_ONGOING_CHAT, ""))
                        .setContentText(msg)
                        .setSmallIcon(R.drawable.chat_logo)
                        .setColor(getResources().getColor(R.color.Orangecolor))
                        .setLargeIcon(icon)
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setContentIntent(pending)
                        .setChannelId(CGlobalVariables.NOTIFICATION_CHANNEL_ID)
                        .setAutoCancel(false)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg));


        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CGlobalVariables.NOTIFICATION_CHANNEL_ID, "horoscopenotification", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            //notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }

        // using the same tag and Id causes the new notification to replace an existing one
        mNotificationManager.notify(CGlobalVariables.ONGOING_NOTIFICATION, 1001, notificationBuilder.build());
    }
//    private void startAgoraBGService() {
//        try {
//            Log.d("ServiceException","Called startAgoraBGService");
//            Intent serviceIntent = new Intent(this, AgoraCallAppBackgroundService.class);
//            serviceIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            serviceIntent.putExtra("ongoing_notification", true);
//            serviceIntent.setAction(Intent.ACTION_VIEW);
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//                Log.d("ServiceException","Called startAgoraBGService 1");
//                startForegroundService(serviceIntent);
//            } else {
//                startService(serviceIntent);
//            }
//        } catch (Exception e) {
//            Log.d("ServiceException","Called Error"+e.toString());
//        }
//    }

    public void changeAppTheme(String newTheme){
        setAppTheme(newTheme);
        com.ojassoft.astrosage.utils.CUtils.setAppDarkMode(this,newTheme);
    }

    private void setAppTheme(String appCurrentTheme){
        //Log.e("SetDarkTheme", "setAppTheme="+appCurrentTheme);
        try {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            switch (appCurrentTheme) {
                case com.ojassoft.astrosage.utils.CGlobalVariables.SYSTEM_THEME:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM);
                    break;
                case com.ojassoft.astrosage.utils.CGlobalVariables.DARK_THEME:
                    //Log.e("SetDarkTheme", "DARK_THEME1");
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                    //Log.e("SetDarkTheme", "DARK_THEME2");
                    break;
                case com.ojassoft.astrosage.utils.CGlobalVariables.LIGHT_THEME:
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                    break;
            }
        }catch (Exception e){
            //Log.e("SetDarkTheme", "Exception="+e);
        }
    }
}