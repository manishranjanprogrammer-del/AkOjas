package com.ojassoft.astrosage.fcmservice;

import static com.libojassoft.android.utils.LibCGlobalVariables.NOTIFICATION_PUSH;
import static com.libojassoft.android.utils.LibCGlobalVariables.UPDATE_NOTIFICATION_COUNT;
import static com.libojassoft.android.utils.LibCGlobalVariables.context;
import static com.ojassoft.astrosage.ui.act.AstrosageKundliApplication.getAppContext;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_NOTIFICATION_COUNT;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_NOTIFICATION_LINK;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.AI_NOTIFICATION_SHOWN;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.CALL_CLICK;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.INTRO_OFFER_TYPE_FREE;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_CONSULTATION_TYPE;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.REFUND_FREE_CHAT;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.SEND_BROADCAST_LIVE_OPEN_KUNDLI;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.SEND_BROADCAST_REFUND_FREE_CHAT;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.libojassoft.android.dao.NotificationDBManager;
import com.libojassoft.android.misc.VolleySingleton;
import com.libojassoft.android.models.NotificationModel;
import com.libojassoft.android.utils.LibCGlobalVariables;
import com.libojassoft.android.utils.LibCUtils;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.custompushnotification.CustomAINotification;
import com.ojassoft.astrosage.custompushnotification.MyCloudRegistrationService;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.act.ActAppModule;
import com.ojassoft.astrosage.ui.act.ActNotificationLanding;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.fragments.astrochatfragment.model.MessageDecode;
import com.ojassoft.astrosage.ui.fragments.astrochatfragment.savedata.SaveDataInternalStorage;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.varta.service.Loginservice;
import com.ojassoft.astrosage.varta.service.PreFetchLiveAstroDataservice;
import com.ojassoft.astrosage.varta.ui.activity.AstrologerDescriptionActivity;
import com.ojassoft.astrosage.varta.ui.activity.ChatWindowActivity;
import com.ojassoft.astrosage.varta.ui.activity.DashBoardActivity;
import com.ojassoft.astrosage.varta.ui.activity.LoginSignUpActivity;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;
import com.ojassoft.astrosage.varta.volley_network.VolleyServiceHandler;
import com.ojassoft.astrosage.vartalive.activities.VideoCallActivity;
import com.ojassoft.astrosage.vartalive.activities.VoiceCallActivity;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;


/**
 * Created by admin on 1-2-2017.
 */
public class OjasFirebaseMessagingService extends FirebaseMessagingService implements VolleyResponse {

    private static final String TAG = "FirebaseMessagService";
    public static final int NOTIFICATION_ID = 1;
    public static final int Chat_Notification_Id = 2;
    public static final String SAVED_NOTIFICATION_ID = "saved_notification_id";
    public static int messageCounter = 0;
    private final String playUrl = CGlobalVariables.PLAY_URL;

    static final public String COPA_MESSAGE = "com.controlj.copame.backend.COPAService.ASTRO_CHAT_ANSWER_MSG";
    private LocalBroadcastManager broadcast;
    String mss = "", tit = "", link = "", ntID = "", extra = "", imgurl = "", consultationType = "", refundfreechat = "";

    String firstName = "", fullName = "";

    private final String userDisabled = "userdisabled";
    private final String userBlock = "userblocked";
    private final String updateLiveAstroList = "UpdateLiveAstroList";
    private final String notiNoDisplay = "noti_no_display";
    private String keyLive = "";
    private boolean followNotification;
    private String astrologerChatAccepted = "";
    private final int FETCH_FOLLOWED_ASTROLOGERS = 1;
    private final int ASTROLOGER_DETAIL_METHOD = 2;
    private RequestQueue queue;
    private String urlText;
    int notificationId;
    String notificationTag;
    BeanHoroPersonalInfo beanHoroPersonalInfo;

    //private static final Set<String> processedMessageIds = new HashSet<>();

    @Override
    public void onNewToken(String token) {
        super.onNewToken(token);
//        Log.i(TAG, "FCMRegistrationToken: " + token);

        try {
            CUtils.saveStringData(this, CGlobalVariables.TOPIC_ALL_NEED_TO_SEND_TO_GOOGLE_SERVER, "");
            CUtils.saveStringData(this, CGlobalVariables.LANGUAGE_TOPIC_NEED_TO_SEND_TO_GOOGLE_SERVER, "");
            CUtils.saveStringData(this, CGlobalVariables.VERSION_TOPIC_NEED_TO_SEND_TO_GOOGLE_SERVER, "");
            CUtils.saveStringData(this, CGlobalVariables.OTHER_TOPIC_NEED_TO_SEND_TO_GOOGLE_SERVER, "");
            CUtils.saveStringData(this, CGlobalVariables.EXTRA_TOPIC_NEED_TO_SEND_TO_GOOGLE_SERVER, "");
            CUtils.saveBooleanData(this, CGlobalVariables.TOPIC_IS_ASTROLOGER_5, false);
            CUtils.saveBooleanData(this, CGlobalVariables.TOPIC_IS_ASTROLOGER_10, false);
            CUtils.storeRegistrationId(getApplicationContext(), token);
            // Fetch updated Instance ID token and notify our app's server of any changes (if applicable).
            Intent intent = new Intent(this, MyCloudRegistrationService.class);
            startService(intent);
        } catch (Exception ex) {
            Log.e("Exception", ex.getMessage());
        }
    }

    private void saveLog(String msg) {
        try {
            AstrosageKundliApplication.notificationIssueLogs = "\n" + AstrosageKundliApplication.notificationIssueLogs + "\n" + msg;
            //String logData = CUtils.getStringData(this,"TestNotif", "");
            //logData = logData+"\n"+msg+"\n";
            //CUtils.saveStringData(this,"TestNotif", logData);
        } catch (Exception e) {
            Log.e("testNotification", "Exception :- " + e);
        }
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e("testNotification", "remoteMessages :- " + remoteMessage);
        saveLog("onMessageReceived() remoteMessage=" + remoteMessage);

        if(remoteMessage == null){
            return;
        }

        beanHoroPersonalInfo = (BeanHoroPersonalInfo) CUtils.getCustomObject(OjasFirebaseMessagingService.this);
        try {
            if (beanHoroPersonalInfo != null) {
                if (!beanHoroPersonalInfo.getName().isEmpty() && !Objects.equals(beanHoroPersonalInfo.getName(), "")) {
                    String[] name = beanHoroPersonalInfo.getName().split(" ");
                    firstName = name[0];
                    fullName = beanHoroPersonalInfo.getName();
                }
            }
        } catch (Exception e) {
            //
        }

        // Log.d("test_new_f","remoteMessage called "+remoteMessage);
//        Log.d(TAG, "onMessageReceived() enter");
        //  Log.e("testNotification", "onMessageReceived() From: " + remoteMessage.getFrom());
        saveLog("onMessageReceived() remoteMessage getData() =" + remoteMessage.getData());
        //handle push notification from fcm console
        Log.e("testNotification", "getNotification() :- " + remoteMessage.getNotification());
        //  Log.e("testNotification","getData() :- "+remoteMessage.getData());
        if (remoteMessage.getNotification() != null) {

            String title = remoteMessage.getNotification().getTitle();
            String body = remoteMessage.getNotification().getBody();
            //Log.d("testNotification","title :- "+title+""+"body :- "+body);
            if (!TextUtils.isEmpty(title) && !TextUtils.isEmpty(body)) {
                String link1 = "";
                if (remoteMessage.getData().size() > 0) {
                    Map<String, String> params = remoteMessage.getData();
                    link1 = params.get(KEY_NOTIFICATION_LINK);
                    imgurl = params.get(CGlobalVariables.KEY_NOTIFICATION_IMG_URL);
                    extra = params.get(CGlobalVariables.KEY_NOTIFICATION_EXTRA);
                }
                mss = body;
                tit = title;
                link = link1;

                if (tit.contains("{firstname}")) {
                    tit = tit.replace("{firstname}", firstName);
                }

                if (mss.contains("{firstname}")) {
                    mss = mss.replace("{firstname}", firstName);

                }
                if (tit.contains("{username}")) {
                    tit = tit.replace("{username}", fullName);
                }

                if (mss.contains("{username}")) {
                    mss = mss.replace("{username}", fullName);
                }

                //Log.d("testNotification","title :- "+title+""+"link :- "+link);
                //sendCustomPushNotification(title, body, link);

                /*if (!TextUtils.isEmpty(imgurl)) {
                    showNotification(tit, imgurl, mss, link);
                    showCustomNotification(tit, imgurl, mss, link);
                } else {
                    sendCustomPushNotification(tit, mss, link);
                }*/
                showCustomNotification(tit, imgurl, mss, link, extra);

                return;
            }
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Map<String, String> params = remoteMessage.getData();
            JSONObject object = new JSONObject(params);
            Log.e("testNotification", "object :- " + object);
            extractValuesToSendPushNotification(object);
        }
    }

    private void extractValuesToSendPushNotification(JSONObject extras) {
        try {
            mss = extras.getString("message");
            tit = extras.getString("title");

            Log.e("testNotification", "tit :- " + tit);
            Log.e("testNotification", "mss :- " + mss);

            if (tit.contains("{firstname}")) {
                tit = tit.replace("{firstname}", firstName);
            }

            if (mss.contains("{firstname}")) {
                mss = mss.replace("{firstname}", firstName);

            }
            if (tit.contains("{username}")) {
                tit = tit.replace("{username}", fullName);
            }

            if (mss.contains("{username}")) {
                mss = mss.replace("{username}", fullName);
            }

            link = extras.getString("link");
            extra = extras.getString("extradata");

            Log.e("testNotification", "link :- " + link);
            Log.e("testNotification", "extra :- " + extra);
            boolean isAIAstrologerOnline = false;
            String question = "";
            String astroId = "";

            if (extra != null && !extra.isEmpty()) {
                JSONObject obj = new JSONObject(extra);
                ///Log.d("testNotification","object is :- "+obj.toString());
                if (obj.has("imgurl")) {
                    imgurl = obj.getString("imgurl");
                }
                if (obj.has("key_live")) {
                    keyLive = obj.getString("key_live");
                }
                if (obj.has("follownotification")) {
                    followNotification = obj.getBoolean("follownotification");
                }
                if (obj.has("Type")) {
                    astrologerChatAccepted = obj.getString("Type");
                }
                if (obj.has(KEY_CONSULTATION_TYPE)) {
                    consultationType = obj.getString(KEY_CONSULTATION_TYPE);
                    //Log.e("testNotification", "consultationType="+consultationType);
                }

                if (obj.has(REFUND_FREE_CHAT)) {
                    refundfreechat = obj.getString(REFUND_FREE_CHAT);
                    //Log.e("testNotification", "refundfreechat="+refundfreechat);
                }

                //question = obj.optString(com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_AI_QUESTION);
                //isAIAstrologerOnline = obj.optBoolean(com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_AI_ASTROLOGER_ONLINE);
                //astroId = obj.optString(com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_AI_ASTROLOGER_ID);

            }
            //Log.e("output::",extras.getString("extradata")+"s : "+extras.getString("link"));
            if (extras.getString("ntid") != null && !extras.getString("ntid").trim().equalsIgnoreCase("0")) {
                ntID = extras.getString("ntid");
                //Log.e("msg",ntID);
            }

            if (link != null && link.trim().equalsIgnoreCase("CHAT_WITH_ASTROLOGER_KUNDLI".trim())) {
                //Log.e("!!!!...Extra Data..!!!!",""+extra);
                JSONObject object = new JSONObject(extra);
                String msgUserType = object.getString("PropertyUserType");
                String msgColor = object.getString("PropertyColorOfMsg");
                String msgRatingOption = object.getString("PropertyRatingToShow");
                String msgShareOption = object.getString("PropertyShareLinkToShow");
                String msgNotificationOption = object.getString("PropertyShowOnNotificationBar");
                String msgOrderId = object.getString("OrderId");
                String msgAstrologerName = object.getString("AstrologerName");
                String msgAstrologerImagePath = object.getString("AstrologerImagePath");
                String msgChatId = object.getString("ChatId");

                MessageDecode messageDecode = new MessageDecode();
                messageDecode.setMessageText(mss);
                messageDecode.setUserType(msgUserType);
                messageDecode.setDateTimeShow(CUtils.getCurrentDateTime());
                messageDecode.setAstrologerName(msgAstrologerName);
                messageDecode.setOrderId(msgOrderId);
                messageDecode.setAstrologerImagePath(msgAstrologerImagePath);
                messageDecode.setColorOfMessage(msgColor);
                messageDecode.setMessageTextTitle(tit);
                messageDecode.setChatId(msgChatId);
                messageCounter += 1;

                if (msgRatingOption.equalsIgnoreCase("true")) {
                    messageDecode.setRateShow("true");
                } else {
                    messageDecode.setRateShow("false");
                }
                if (msgShareOption.equalsIgnoreCase("true")) {
                    messageDecode.setShareLinkShow("true");
                } else {
                    messageDecode.setShareLinkShow("false");
                }
                saveMessage(messageDecode);
                sendResult(messageDecode);


                //Send Notification Here.::
                if (msgNotificationOption.equalsIgnoreCase("true")) {
                    if (mss != null) {
                        mss = mss.trim();
                    }
                    sendCustomPushNotiAstroChat(tit, mss, link);
                }

            } else if (link != null && link.trim().equalsIgnoreCase("CHAT_WITH_ASTROLOGER_FREE_QUESTION".trim())) {
                //Log.e("!!!!...Extra Data..!!!!",""+extra);
                JSONObject object = new JSONObject(extra);
                String msgUserType = object.getString("PropertyUserType");
                String msgColor = object.getString("PropertyColorOfMsg");
                String msgRatingOption = object.getString("PropertyRatingToShow");
                String msgShareOption = object.getString("PropertyShareLinkToShow");
                String msgNotificationOption = object.getString("PropertyShowOnNotificationBar");
                String msgOrderId = object.getString("OrderId");
                String msgAstrologerName = object.getString("AstrologerName");
                String msgAstrologerImagePath = object.getString("AstrologerImagePath");
                String msgChatId = object.getString("ChatId");

                MessageDecode messageDecode = new MessageDecode();
                messageDecode.setMessageText(mss);
                messageDecode.setUserType(msgUserType);
                messageDecode.setDateTimeShow(CUtils.getCurrentDateTime());
                messageDecode.setAstrologerName(msgAstrologerName);
                messageDecode.setOrderId(msgOrderId);
                messageDecode.setAstrologerImagePath(msgAstrologerImagePath);
                messageDecode.setColorOfMessage(msgColor);
                messageDecode.setMessageTextTitle(tit);
                messageDecode.setChatId(msgChatId);
                messageCounter += 1;

                if (msgRatingOption.equalsIgnoreCase("true")) {
                    messageDecode.setRateShow("true");
                } else {
                    messageDecode.setRateShow("false");
                }
                if (msgShareOption.equalsIgnoreCase("true")) {
                    messageDecode.setShareLinkShow("true");
                } else {
                    messageDecode.setShareLinkShow("false");
                }

                //Send Notification Here.::
                if (msgNotificationOption.equalsIgnoreCase("true")) {
                    if (mss != null) {
                        mss = mss.trim();
                    }
                    sendCustomPushNotiAstroChat(tit, mss, link);
                }

            } else {
                if (!ntID.trim().isEmpty()) {
                    if (ntID.equalsIgnoreCase(CUtils.getSavedNotificationID(this, SAVED_NOTIFICATION_ID))) {
                    } else {
                        /*if (imgurl != null && !imgurl.isEmpty()) {
                            //sendImageNotification(tit, imgurl, mss, link);
                            showNotification(tit, imgurl, mss, link);
                        } else {
                            sendCustomPushNotification(tit, mss, link);

                        }*/

                        showCustomNotification(tit, imgurl, mss, link, extra);

                        CUtils.setSavedNotificationID(this, ntID, SAVED_NOTIFICATION_ID);
                    }
                } else if (astrologerChatAccepted.equals("Notification_Type_1")) {
                    //Log.d("testNotification","Called from this  ==>> "+tit+mss+link);
                    astrologerChatAccepted = "";
                    initiateChatChatRequestAccepted(tit, mss, link);

                } else {
                    /*if (imgurl != null && !imgurl.isEmpty()) {
                        // sendImageNotification(tit, imgurl, mss, link);
                        showNotification(tit, imgurl, mss, link);
                    } else {
                        //Log.d("testNotification","Called sendCustomNoti");
                        sendCustomPushNotification(tit, mss, link);

                    }*/

                    showCustomNotification(tit, imgurl, mss, link, extra);
                }
            }

        } catch (Exception e) {
            Log.e("testNotification", "Exception ==>>" + e);
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    private void saveMessage(MessageDecode message) {
        ArrayList<MessageDecode> chatMessageArrayList = CUtils.getDataFromPrefrence(OjasFirebaseMessagingService.this);
        if (chatMessageArrayList == null)
            chatMessageArrayList = new ArrayList<>();
        chatMessageArrayList.add(message);
        saveDataOnInternalStorage(chatMessageArrayList);
        //InternalStorage.writeObject(this, chatMessageArrayList);// used temporary for testing...
    }

    public void saveDataOnInternalStorage(ArrayList<MessageDecode> chatMessageArrayList) {
        Intent intent = new Intent(OjasFirebaseMessagingService.this, SaveDataInternalStorage.class);
        intent.putParcelableArrayListExtra(CGlobalVariables.CHATWITHASTROLOGER, chatMessageArrayList);
        intent.putExtra(CGlobalVariables.ISINSERT, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //(OjasFirebaseMessagingService.this).startForegroundService(intent);
            (OjasFirebaseMessagingService.this).startService(intent);
        } else {
            (OjasFirebaseMessagingService.this).startService(intent);
        }
    }

    // Put the message into a notification and post it.
    // This is just one simple example of what you might choose to do with
    // a GCM message.
    private void sendNotification(String msg) {
        //Log.e("tag", msg);
    }


    private void sendCustomPushNotification(String title, String msg, String link) {
        //Log.d("testNotification","String title, String msg, String link"+title+msg+link);
        com.ojassoft.astrosage.varta.utils.CUtils.cancelNotification(AstrosageKundliApplication.getAppContext());
        boolean isDataSaveInDB = true;
        try {
            if (!TextUtils.isEmpty(extra)) {
                JSONObject obj = new JSONObject(extra);
                if (obj.has("showinnotifcenter")) {
                    isDataSaveInDB = obj.getBoolean("showinnotifcenter");
                }
            }
        } catch (Exception e) {
            //
        }
        boolean isVartaPromoNotification = false;
        boolean isVartaFollowNotification = false;
        if (link != null && (link.contains(com.ojassoft.astrosage.varta.utils.CGlobalVariables.varta_astrosage_urls) ||
                link.contains(com.ojassoft.astrosage.varta.utils.CGlobalVariables.varta_astrosage_url))) {
            isVartaPromoNotification = true;
        }

        // String CHANNEL_ID = "my_gcm_notification01";
        Bitmap icon = BitmapFactory.decodeResource(OjasFirebaseMessagingService.this.getResources(), com.libojassoft.android.R.mipmap.icon);
        int notificationId = LibCUtils.getRandomNumber();
        // pending intent is redirection using the deep-link
        //If url contains Play store then use Action view else open the App
        Intent resultIntent;
        if (link != null && link.contains(playUrl)) {
            resultIntent = new Intent(Intent.ACTION_VIEW);
            resultIntent.setData(Uri.parse(link));
        } else if (
                title.equals(OjasFirebaseMessagingService.this.getResources().getString(R.string.chat_failed_user)) ||
                        title.equalsIgnoreCase(OjasFirebaseMessagingService.this.getResources().getString(R.string.chat_not_completed)) ||
                        title.equalsIgnoreCase(OjasFirebaseMessagingService.this.getResources().getString(R.string.chat_request_accepted))) {
            isVartaPromoNotification = false;
            resultIntent = new Intent(this, DashBoardActivity.class);
            //resultIntent.setAction(Intent.ACTION_VIEW);
            resultIntent.putExtra("title", title);
            resultIntent.putExtra("msg", msg);
            try {
                sendResult(mss, tit, link, "");
            } catch (Exception e) {
                e.printStackTrace();
            }
            isDataSaveInDB = false;
        } else if (title.equals(OjasFirebaseMessagingService.this.getResources().getString(R.string.call_completed)) ||
                title.equals(OjasFirebaseMessagingService.this.getResources().getString(R.string.chat_completed))) {
            isVartaPromoNotification = false;
            //Log.d("testNotification","Called from this 0");
            com.ojassoft.astrosage.varta.utils.CUtils.updateChatCallOfferType(this, false, CALL_CLICK);
            //Log.e("astrologerUrl", "urlText2="+link);
            // to stop the chat related services if running
            com.ojassoft.astrosage.varta.utils.CUtils.stopChatServices(this);
            String consultType = "";
            if (title.equals(OjasFirebaseMessagingService.this.getResources().getString(R.string.call_completed))) {
                //Log.d("testNotification","Called from this 3");
                try {
                    if (!TextUtils.isEmpty(consultationType)) {
                        consultType = consultationType;
                        consultationType = "";
                    } else {
                        consultType = com.ojassoft.astrosage.varta.utils.CGlobalVariables.CON_TYPE_CALL;
                    }
                    CUtils.fcmAnalyticsEvents("call_completed_success_notification", com.ojassoft.astrosage.varta.utils.CGlobalVariables.CALL_BTN_CLICKED, "");
                    sendResult(mss, tit, link, consultType);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (title.equals(OjasFirebaseMessagingService.this.getResources().getString(R.string.chat_completed))) {
                //Log.d("testNotification","Called from this 4");
                try {
                    consultType = com.ojassoft.astrosage.varta.utils.CGlobalVariables.CON_TYPE_CHAT;
                    CUtils.fcmAnalyticsEvents("chat_completed_success_notification", com.ojassoft.astrosage.varta.utils.CGlobalVariables.CALL_BTN_CLICKED, "");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (!TextUtils.isEmpty(link)) {
                //Log.d("testNotification","Called from this 1");
                resultIntent = new Intent(this, AstrologerDescriptionActivity.class);
                resultIntent.putExtra("title", title);
                resultIntent.putExtra("msg", msg);
                resultIntent.putExtra("astrologerUrl", link);
                resultIntent.putExtra(KEY_CONSULTATION_TYPE, consultType);
            } else {
                //Log.d("testNotification","Called from this 2");
                resultIntent = new Intent(this, DashBoardActivity.class);
                resultIntent.putExtra("title", title);
                resultIntent.putExtra("msg", msg);
            }
        } /*else if(!TextUtils.isEmpty(aiAstroId) && !TextUtils.isEmpty(question)) {
            saveLog("onMessageReceived() aiAstroId= "+aiAstroId+" question="+question);
            resultIntent = new Intent(this, ActAppModule.class);
            resultIntent.setAction(Intent.ACTION_VIEW);
            resultIntent.setData(Uri.parse(link));
            resultIntent.putExtra(com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_AI_QUESTION, question);
            resultIntent.putExtra(com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_REVERT_QUESTION_COUNT, revertQCount);
            resultIntent.putExtra(com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_AI_ASTROLOGER_ID, aiAstroId);
        } */ else {
            if (link.contains(com.ojassoft.astrosage.varta.utils.CGlobalVariables.varta_astrosage_urls) ||
                    link.contains(com.ojassoft.astrosage.varta.utils.CGlobalVariables.varta_astrosage_url)) {

                //  Log.e("testNotification", "sendCustomPushNotification() link: " + link);
                if (link.contains(CGlobalVariables.open_ai_astrologers) || link.contains(CGlobalVariables.chat_with_kundli_ai)) {//open ai astrologer list
                    resultIntent = new Intent(this, ActAppModule.class);
                    if (link.contains(CGlobalVariables.chat_with_kundli_ai)) {
                        try {
                            if (extra != null && !extra.isEmpty()) {
                                // Log.e(TAG, "sendCustomPushNotification: extra 593 = " +extra );
                                JSONObject obj = new JSONObject(extra);
                                String categoryId = obj.getString("category");
                                // Log.e("testNotification", "sendCustomPushNotification() categoryId: " + categoryId);
                                resultIntent.putExtra(CGlobalVariables.KEY_FOR_CATEGORY_ID, categoryId);
                            }
                        } catch (Exception e) {
//
                        }
                    }
                } else {
                    resultIntent = new Intent(this, DashBoardActivity.class);
                }
                if (followNotification) {
                    link = link + "&isfollow=1";
                    notificationTag = String.valueOf(System.currentTimeMillis());
                    try {
                        getFollowedAstros(link, notificationId);
                    } catch (Exception e) {
                        Log.d(TAG, "getFollowedAstros e: " + e);
                    }
                } else if (refundfreechat != null && refundfreechat.equalsIgnoreCase(INTRO_OFFER_TYPE_FREE)) {
                    com.ojassoft.astrosage.varta.utils.CUtils.setUserIntroOfferType(getApplicationContext(), refundfreechat);
                    sendBroadcastRefundFreeChat();
                    refundfreechat = "";
                }
            } else {
                resultIntent = new Intent(this, ActAppModule.class);
            }
            resultIntent.setAction(Intent.ACTION_VIEW);
            resultIntent.setData(Uri.parse(link));
        }
        resultIntent.putExtra("isNotification", true);

        PendingIntent pending = PendingIntent.getActivity(this, notificationId, resultIntent, PendingIntent.FLAG_IMMUTABLE);


        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, LibCGlobalVariables.NOTIFICATION_CHANNEL_ID)

                        .setContentTitle(title)
                        .setContentText(msg)
                        .setSmallIcon(com.libojassoft.android.R.drawable.ic_notification)
                        .setLargeIcon(icon)
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setContentIntent(pending)
                        .setChannelId(LibCGlobalVariables.NOTIFICATION_CHANNEL_ID)
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg));


        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(LibCGlobalVariables.NOTIFICATION_CHANNEL_ID, "horoscopenotification", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            //notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }

        if (keyLive != null && keyLive.contains(updateLiveAstroList)) {
            keyLive = "";
            boolean liveStreamingEnabledForAstrosage = com.ojassoft.astrosage.varta.utils.CUtils.getBooleanData(this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.liveStreamingEnabledForAstrosage, false);
            if (!liveStreamingEnabledForAstrosage) { //updateLiveAstroList according to tagmanager
                return;
            }

            isDataSaveInDB = false;
            try {
                if (!isPrefetchLiveAstroServiceRunning() && AstrosageKundliApplication.wasInBackground) {
                    Intent intentService = new Intent(this, PreFetchLiveAstroDataservice.class);
                    intentService.putExtra(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CALL_SOURCE, com.ojassoft.astrosage.varta.utils.CUtils.getActivityName(this));
                    startService(intentService);
                }
            } catch (Exception e) {
                //
            }
        }

        if (link.contains(userDisabled)) {
            //logout from the app
            if (com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(this)) {
                logoutFromVartaSection();
            }
        } else if (link.contains(userBlock)) {
            //Log.e("blockuserDisabled", "link="+link);
            sendBroadcastLiveOpenKundlI(link);
        } else {
            if (!link.contains(notiNoDisplay)) { //silent notification will not be show on notification tray
                // using the same tag and Id causes the new notification to replace an existing one
                if (followNotification) {
                    isVartaPromoNotification = false;
                    isVartaFollowNotification = true;
                    followNotification = false;
                    boolean isFollowChecked = CUtils.getBooleanData(this, CGlobalVariables.IS_FOLLOW_NOTIF_SETTING_CHECKED, true);
                    if (isFollowChecked) {
                        AstrosageKundliApplication.notificationIssueLogs = "\n" + AstrosageKundliApplication.notificationIssueLogs + "\n" + "mNotificationManager.notify() 1";
                        mNotificationManager.notify( notificationId, notificationBuilder.build());
                    } /*else {
                        //don't show follow notification if desabled from setting
                    }*/
                } else if (CUtils.isDisplayNotification(this)) {
                    AstrosageKundliApplication.notificationIssueLogs = "\n" + AstrosageKundliApplication.notificationIssueLogs + "\n" + "mNotificationManager.notify() 2";
                    mNotificationManager.notify( notificationId, notificationBuilder.build());
                }
                if (isDataSaveInDB) {
                    saveNotificationInLocalDb();
                }

                vartaPromotionNotificationEvent(isVartaPromoNotification, isVartaFollowNotification);

            }
        }
    }

    private void initiateChatChatRequestAccepted(String title, String msg, String link) {
        int notificationId = LibCUtils.getRandomNumber();
        Bitmap icon = BitmapFactory.decodeResource(OjasFirebaseMessagingService.this.getResources(), com.libojassoft.android.R.mipmap.icon);
        Intent resultIntent = null;
        String currentChannelId = "";
        String[] dataStringArr = link.split("-");
        currentChannelId = dataStringArr[1];
        if (currentChannelId.startsWith("IVC") || currentChannelId.startsWith("IAC") && !AstrosageKundliApplication.IS_AGORA_CALL_RUNNING) {
            //Log.d("testNotification","isAgoraCallScreenOpened");

            String channelIdLink = "";
            try {
                sendResult("", "", link, "");
                channelIdLink = dataStringArr[1];
                if (channelIdLink.startsWith("IVC")) {
                    resultIntent = new Intent(this, VideoCallActivity.class);
                    resultIntent.putExtras(Objects.requireNonNull(agoraCallInitiateBundle()));
                    resultIntent.setPackage(BuildConfig.APPLICATION_ID);
                } else if (channelIdLink.startsWith("IAC")) {
                    resultIntent = new Intent(this, VoiceCallActivity.class);
                    resultIntent.putExtras(Objects.requireNonNull(agoraCallInitiateBundle()));
                    resultIntent.setPackage(BuildConfig.APPLICATION_ID);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (AstrosageKundliApplication.currentChatStatus.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHAT_STARTED)) {
            //resultIntent.putExtra("ongoing_notification", true);
            //resultIntent.setAction(Intent.ACTION_VIEW);
            //resultIntent.putExtra("title", title);
            // resultIntent.putExtra("msg", msg);
            String channelIdLink = "";
            try {
                sendResult("", "", link, "");
                channelIdLink = dataStringArr[1];
                if (channelIdLink.startsWith("FCH")) {
                    resultIntent = new Intent(this, ChatWindowActivity.class);
                }
                if (com.ojassoft.astrosage.varta.utils.CUtils.getSelectedChannelID(getApplicationContext()).equals(channelIdLink)) {
                    Bundle bundle = new Bundle();
                    bundle.putString(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHAT_USER_CHANNEL, channelIdLink);
                    bundle.putString(CGlobalVariables.CONNECT_CHAT_BEAN, AstrosageKundliApplication.chatJsonObject);
                    bundle.putString(CGlobalVariables.ASTROLOGER_NAME, AstrosageKundliApplication.selectedAstrologerDetailBean.getName());
                    bundle.putString(CGlobalVariables.ASTROLOGER_PROFILE_URL, AstrosageKundliApplication.selectedAstrologerDetailBean.getImageFile());
                    bundle.putString(CGlobalVariables.ASTROLOGER_ID, AstrosageKundliApplication.selectedAstrologerDetailBean.getAstrologerId());
                    bundle.putString(CGlobalVariables.USERCHATTIME, com.ojassoft.astrosage.varta.utils.CGlobalVariables.userChatTime);
                    if (resultIntent != null) {
                        resultIntent.putExtras(bundle);
                    }
                    // Launch activity
                    if (AstrosageKundliApplication.isActivityVisible()) {
                        com.ojassoft.astrosage.varta.utils.CUtils.chatStartWhenAstrologerAccept(this, bundle);
                    }

                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            // resultIntent.putExtra("isNotification", true);
        } else {
            String channelIdLink = "";
            channelIdLink = dataStringArr[1];
            if (channelIdLink.startsWith("IVC")) {
                resultIntent = new Intent(this, VideoCallActivity.class);
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                resultIntent.putExtra("ongoing_notification", true);
                resultIntent.setPackage(BuildConfig.APPLICATION_ID);
            } else if (channelIdLink.startsWith("IAC")) {
                resultIntent = new Intent(this, VoiceCallActivity.class);
                resultIntent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                resultIntent.putExtra("ongoing_notification", true);
                resultIntent.setPackage(BuildConfig.APPLICATION_ID);
            } else if (channelIdLink.startsWith("FCH")) {
                resultIntent = new Intent(this, ChatWindowActivity.class);
                resultIntent.putExtra("ongoing_notification", true);
                resultIntent.setAction(Intent.ACTION_VIEW);
            }
            //resultIntent.putExtra("title", title);
            //resultIntent.putExtra("msg", msg);
        }
        PendingIntent pending = PendingIntent.getActivity(this, notificationId, resultIntent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, LibCGlobalVariables.NOTIFICATION_CHANNEL_ID)

                        .setContentTitle(title)
                        .setContentText(msg)
                        .setSmallIcon(com.libojassoft.android.R.drawable.ic_notification)
                        .setLargeIcon(icon)
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setContentIntent(pending)
                        .setChannelId(LibCGlobalVariables.NOTIFICATION_CHANNEL_ID)
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg));


        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(LibCGlobalVariables.NOTIFICATION_CHANNEL_ID, "horoscopenotification", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            //notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        String tag = String.valueOf(System.currentTimeMillis());
        AstrosageKundliApplication.currentDisplayedNotificationId = notificationId;
        AstrosageKundliApplication.currentDisplayedNotificationTag = tag;
        //Log.d("testNotification","Called 123");
        mNotificationManager.notify(tag, notificationId, notificationBuilder.build());
        //mNotificationManager.notify( notificationId, notificationBuilder.build());
        // mNotificationManager.notify("Notification_Type_1", notificationId, notificationBuilder.build());
        //mNotificationManager.notify(System.currentTimeMillis(), notificationBuilder.build());


    }

    private Bundle agoraCallInitiateBundle() {
        Bundle bundle = new Bundle();
        if (com.ojassoft.astrosage.varta.utils.CUtils.connectAgoraCallBean != null && AstrosageKundliApplication.selectedAstrologerDetailBean != null) {
            String agoraCallSId = com.ojassoft.astrosage.varta.utils.CUtils.connectAgoraCallBean.getCallsid();
            String agoraToken = com.ojassoft.astrosage.varta.utils.CUtils.connectAgoraCallBean.getTokenid();
            String agoraTokenId = com.ojassoft.astrosage.varta.utils.CUtils.connectAgoraCallBean.getAgoratokenid();
            String astrologerName = AstrosageKundliApplication.selectedAstrologerDetailBean.getName();
            String astrologerProfileUrl = AstrosageKundliApplication.selectedAstrologerDetailBean.getImageFile();
            String agoraCallDuration = com.ojassoft.astrosage.varta.utils.CUtils.connectAgoraCallBean.getTalktime();
            String astrologerId = AstrosageKundliApplication.selectedAstrologerDetailBean.getAstrologerId();
            bundle.putString(com.ojassoft.astrosage.varta.utils.CGlobalVariables.AGORA_CALLS_ID, agoraCallSId);
            bundle.putString(com.ojassoft.astrosage.varta.utils.CGlobalVariables.AGORA_TOKEN, agoraToken);
            bundle.putString(com.ojassoft.astrosage.varta.utils.CGlobalVariables.AGORA_TOKEN_ID, agoraTokenId);
            bundle.putString(com.ojassoft.astrosage.varta.utils.CGlobalVariables.ASTROLOGER_NAME, astrologerName);
            bundle.putString(com.ojassoft.astrosage.varta.utils.CGlobalVariables.ASTROLOGER_PROFILE_URL, astrologerProfileUrl);
            bundle.putString(com.ojassoft.astrosage.varta.utils.CGlobalVariables.AGORA_CALL_DURATION, agoraCallDuration);
            bundle.putString(com.ojassoft.astrosage.varta.utils.CGlobalVariables.ASTROLOGER_ID, astrologerId);
        }

        return bundle;
    }

    private void sendCustomPushNotiAstroChat(String title, String msg, String link) {
        // String CHANNEL_ID = "my_gcm_notification02";
        Bitmap icon = BitmapFactory.decodeResource(OjasFirebaseMessagingService.this.getResources(), com.libojassoft.android.R.mipmap.icon);
        int notificationId = LibCUtils.getRandomNumber();
        // pending intent is redirection using the deep-link
        Intent resultIntent = new Intent(this, ActNotificationLanding.class);
        resultIntent.putExtra("isNotification", true);

        PendingIntent pending = PendingIntent.getActivity(this, notificationId, resultIntent, PendingIntent.FLAG_IMMUTABLE);

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, LibCGlobalVariables.NOTIFICATION_CHANNEL_ID)
                        .setContentTitle(title)
                        .setContentText(msg)
                        .setSmallIcon(com.libojassoft.android.R.drawable.ic_notification)
                        .setLargeIcon(icon)
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setContentIntent(pending)
                        .setChannelId(LibCGlobalVariables.NOTIFICATION_CHANNEL_ID)
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC);
        //  if (messageCounter==1) {
        // notificationBuilder.setContentText(msg);
       /* } else {

                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg));
            notificationBuilder.setContentText(messageCounter+" New messages");
        }*/

        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(LibCGlobalVariables.NOTIFICATION_CHANNEL_ID, "horoscopenotification", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            //notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        // using the same tag and Id causes the new notification to replace an existing one
        mNotificationManager.notify( notificationId, notificationBuilder.build());
        //store notification into local db
        saveNotificationInLocalDb();
    }

    public void sendResult(MessageDecode message) {
        broadcast = LocalBroadcastManager.getInstance(OjasFirebaseMessagingService.this);
        Intent intent = new Intent(CGlobalVariables.COPA_RESULT);
        if (message != null) {
            intent.putExtra(COPA_MESSAGE, message);
            intent.putExtra(CGlobalVariables.CHAT_ANSWER_REPLY_MESSAGE, true);
        }
        broadcast.sendBroadcast(intent);
    }

    public void sendResult(String message, String title, String link, String consultationType) {
        broadcast = LocalBroadcastManager.getInstance(OjasFirebaseMessagingService.this);
        AstrosageKundliApplication.IS_CALL_BROAD_ACTION_SEEN = false;
        Intent intent = new Intent(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CALL_BROAD_ACTION);
        if (message != null) {
            intent.putExtra(com.ojassoft.astrosage.varta.utils.CGlobalVariables.BROAD_MSG_RESULT, message);
        }
        if (title != null) {
            intent.putExtra(com.ojassoft.astrosage.varta.utils.CGlobalVariables.BROAD_TITLE_RESULT, title);
        }
        if (link != null && link.length() > 0) {
            String[] urlText = link.split("\\/");
            String astrologerUrl = urlText[urlText.length - 1];
            intent.putExtra(com.ojassoft.astrosage.varta.utils.CGlobalVariables.BROAD_LINK_RESULT, astrologerUrl);
        }
        intent.putExtra(KEY_CONSULTATION_TYPE, consultationType);
        broadcast.sendBroadcast(intent);
    }

    private void sendImageNotification(String title, Bitmap bitmap, String msg, String link) {
        //String CHANNEL_ID = "my_gcm_notification03";
        Bitmap icon = BitmapFactory.decodeResource(OjasFirebaseMessagingService.this.getResources(), com.libojassoft.android.R.mipmap.icon);
        int notificationId = LibCUtils.getRandomNumber();
        // pending intent is redirection using the deep-link
        //If url contains Play store then use Action view else open the App
        Intent resultIntent;
        if (link.contains(playUrl)) {
            resultIntent = new Intent(Intent.ACTION_VIEW);
            resultIntent.setData(Uri.parse(link));
        } else {
            resultIntent = new Intent(this, ActAppModule.class);
            resultIntent.setAction(Intent.ACTION_VIEW);
            resultIntent.setData(Uri.parse(link));
            if (link.contains(CGlobalVariables.chat_with_kundli_ai)) {
                try {
                    if (extra != null && !extra.isEmpty()) {
                        JSONObject obj = new JSONObject(extra);
                        String categoryId = obj.getString("category");
                        resultIntent.putExtra(CGlobalVariables.KEY_FOR_CATEGORY_ID, categoryId);
                    }
                } catch (Exception e) {
//
                }
            }
        }

        PendingIntent pending = PendingIntent.getActivity(this, notificationId, resultIntent, PendingIntent.FLAG_IMMUTABLE);


        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(this, LibCGlobalVariables.NOTIFICATION_CHANNEL_ID)
                        .setContentTitle(title)
                        .setContentText(msg)
                        .setSmallIcon(com.libojassoft.android.R.drawable.ic_notification)
                        .setLargeIcon(icon)
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setContentIntent(pending)
                        .setChannelId(LibCGlobalVariables.NOTIFICATION_CHANNEL_ID)
                        .setAutoCancel(true)
                        .setPriority(NotificationCompat.PRIORITY_HIGH)
                        .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
                        .setStyle(new NotificationCompat.BigPictureStyle()
                                .bigPicture(bitmap).setBigContentTitle(title).setSummaryText(msg));

        NotificationManager mNotificationManager =
                (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(LibCGlobalVariables.NOTIFICATION_CHANNEL_ID, "horoscopenotification", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            //notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        // using the same tag and Id causes the new notification to replace an existing one
        if (CUtils.isDisplayNotification(this)) {
            mNotificationManager.notify( notificationId, notificationBuilder.build());
        }
        //store notification into local db
        saveNotificationInLocalDb();
    }

   /* Bitmap getImageFromUrl(String imgurl) {
        Bitmap bitmap = null;
        URL url = null;
        try {
            url = new URL(imgurl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream in = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(in);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return bitmap;
    }*/

    private void saveNotificationInLocalDb() {

        Context context = this;
        if (context == null) return;
        NotificationModel notificationModel = new NotificationModel();
        notificationModel.setMessage(mss);
        notificationModel.setTitle(tit);
        notificationModel.setLink(link);
        notificationModel.setNtId(ntID);
        notificationModel.setExtra(extra);
        notificationModel.setImgUrl(imgurl);
        notificationModel.setBlogId("");
        notificationModel.setNotificationType(NOTIFICATION_PUSH);
        notificationModel.setTimestamp(System.currentTimeMillis() + "");

        NotificationDBManager dbManager = new NotificationDBManager(context);
        dbManager.addNotification(notificationModel);
        //update notification count
        int count = CUtils.getIntData(context, KEY_NOTIFICATION_COUNT, 0);
        CUtils.saveIntData(context, KEY_NOTIFICATION_COUNT, ++count);
        Intent intent = new Intent(UPDATE_NOTIFICATION_COUNT);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
    }

    private void showNotification(final String title, final String imgUrl, final String msg, final String link) {
        ImageRequest request = new ImageRequest(imgUrl,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap bitmap) {
                        sendImageNotification(title, bitmap, msg, link);
                    }
                }, 0, 0, null,
                new Response.ErrorListener() {
                    public void onErrorResponse(VolleyError error) {
                    }
                });
// Access the RequestQueue through your singleton class.
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();
        queue.add(request);
    }

    private boolean isPrefetchLiveAstroServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (service.service.getClassName().contains("PreFetchLiveAstroDataservice")) {
                return true;
            }
        }
        return false;
    }

    private void logoutFromVartaSection() {
        //Log.e("userDisabled", "logoutFromApp1");
        try {
            Context context = this;
            com.ojassoft.astrosage.varta.utils.CUtils.clearAllSharedPreferences(context);
            com.ojassoft.astrosage.varta.utils.CUtils.saveLoginDetailInPrefs(context, "", false, "", "0");
            com.ojassoft.astrosage.varta.utils.CUtils.setUserLoginPassword(this, "");
            FirebaseAuth.getInstance().signOut();
            if (com.ojassoft.astrosage.varta.utils.CUtils.isAppRunning(context)) {
                //Log.e("userDisabled", "logoutFromApp2");
                Intent intent = new Intent(context, LoginSignUpActivity.class);
                //intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra(com.ojassoft.astrosage.varta.utils.CGlobalVariables.IS_FROM_SCREEN, "");
                context.startActivity(intent);
            }
            //Log.e("userDisabled", "logoutFromApp3");
        } catch (Exception e) {
            e.printStackTrace();
            //Log.e("userDisabled", "logoutFromApp exp="+e.toString());
        }
    }

    private void sendBroadcastLiveOpenKundlI(String link) {
        try {
            //Log.e("blockuserDisabled", "linksendbroadcast");
            Context context = this;
            Intent intent = new Intent(SEND_BROADCAST_LIVE_OPEN_KUNDLI);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendBroadcastRefundFreeChat() {
        try {
            //Log.e("SAN OFMS ", "sendBroadcastRefundFreeChat()");
            Context context = this;
            Intent intent = new Intent(SEND_BROADCAST_REFUND_FREE_CHAT);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void callBackgroundLogin() {
        try {
            boolean isLogin = com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(this);
            if (isLogin) {
                Intent intentService = new Intent(this, Loginservice.class);
                startService(intentService);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void getFollowedAstros(String link, int nId) {
        queue = com.ojassoft.astrosage.varta.volley_network.VolleySingleton.getInstance(getAppContext()).getRequestQueue();
        Uri linkData = Uri.parse(link);
        urlText = linkData.getLastPathSegment();
        notificationId = nId;

        if (com.ojassoft.astrosage.varta.utils.CUtils.isConnectedWithInternet(this)) {
//            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, com.ojassoft.astrosage.varta.utils.CGlobalVariables.GET_FOLLOWING_ASTRO_URL,
//                    OjasFirebaseMessagingService.this, false, com.ojassoft.astrosage.varta.utils.CUtils.getFollowingAstroParams(this), FETCH_FOLLOWED_ASTROLOGERS).getMyStringRequest();
//            queue.add(stringRequest);


            ApiList apiList = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> apiCall = apiList.getFollowedAstrologers(com.ojassoft.astrosage.varta.utils.CUtils.getFollowingAstroParams(this));
            apiCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    try {
                        String responses = response.body().string();
                        JSONObject jsonObject = new JSONObject(responses);
                        JSONArray followedList = jsonObject.getJSONArray("astrologerslist");
                        boolean isFollowing = true;
                        for (int i = 0; i < followedList.length(); i++) {
                            JSONObject object = followedList.getJSONObject(i);

                            if (urlText.equals(object.getString("urlText"))) {
                                isFollowing = true;
                                break;
                            } else {
                                isFollowing = false;
                            }
                        }

                        if (!isFollowing) {
                            try {
                                NotificationManager nMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                                nMgr.cancel(notificationTag, notificationId);
                            } catch (Exception e) {
                                //
                            }
                            getAstrologerDetails(urlText);
                        }

                    } catch (Exception e) {
                        Log.d(TAG, "onResponse 1: " + e);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }
    }

    private void getAstrologerDetails(String urlText) {
        StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, com.ojassoft.astrosage.varta.utils.CGlobalVariables.ASTROLOGER_DESCRIPTION_URL,
                OjasFirebaseMessagingService.this, false, getAstroDetailsParams(urlText), ASTROLOGER_DETAIL_METHOD).getMyStringRequest();
        stringRequest.setShouldCache(true);
        queue.add(stringRequest);
    }

    public Map<String, String> getAstroDetailsParams(String urlText) {

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.APP_KEY, com.ojassoft.astrosage.varta.utils.CUtils.getApplicationSignatureHashCode(context));
        boolean isLogin = com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(context);
        try {
            if (isLogin) {
                headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.PHONE_NO, com.ojassoft.astrosage.varta.utils.CUtils.getUserID(context));
                headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.COUNTRY_CODE, com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(context));
            } else {
                headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.PHONE_NO, "");
                headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.COUNTRY_CODE, "");
            }
        } catch (Exception e) {
        }
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.URL_TEXT, urlText);
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.PACKAGE_NAME, com.ojassoft.astrosage.varta.utils.CUtils.getAppPackageName(context));
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.APP_VERSION, BuildConfig.VERSION_NAME);
        //Log.e("SAN ADA ", "Astro URL Status N Price params " + headers.toString() );
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANG, com.ojassoft.astrosage.varta.utils.CUtils.getLanguageKey(LANGUAGE_CODE));
        String offerType = com.ojassoft.astrosage.varta.utils.CUtils.getCallChatOfferType(this);
        if (offerType == null) offerType = "";
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.OFFER_TYPE, offerType);
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.USE_INTRO_OFFER, "0");

        //Log.d(TAG, "getAstroDetailsParams: "+headers);

        return headers;
    }

    @Override
    public void onResponse(String response, int method) {
        //Log.d(TAG, "onResponse method->"+method+"--response->"+response);
        if (response != null && response.length() > 0) {
            if (method == FETCH_FOLLOWED_ASTROLOGERS) {
//                try {
//                    JSONObject jsonObject = new JSONObject(response);
//                    JSONArray followedList = jsonObject.getJSONArray("astrologerslist");
//                    boolean isFollowing = true;
//                    for (int i = 0; i < followedList.length(); i++) {
//                        JSONObject object = followedList.getJSONObject(i);
//
//                        if (urlText.equals(object.getString("urlText"))) {
//                            isFollowing = true;
//                            break;
//                        } else {
//                            isFollowing = false;
//                        }
//                    }
//
//                    if (!isFollowing) {
//                        try {
//                            NotificationManager nMgr = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
//                            nMgr.cancel(notificationTag, notificationId);
//                        } catch (Exception e) {
//                            //
//                        }
//                        getAstrologerDetails(urlText);
//                    }
//
//                } catch (Exception e) {
//                    Log.d(TAG, "onResponse 1: "+e);
//                }
            } else if (method == ASTROLOGER_DETAIL_METHOD) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String astroID = jsonObject.getString("ai");
                    com.ojassoft.astrosage.varta.utils.CUtils.unSubscribeFollowTopic(OjasFirebaseMessagingService.this, astroID);
                } catch (Exception e) {
                    Log.d(TAG, "onResponse 2: " + e);
                }

            }
        }
    }

    @Override
    public void onError(VolleyError error) {
        Log.d(TAG, "onError: " + error);
    }

    private void showCustomNotification(String tit, String imgurl, String mss, String link, String extras) {
        if (NotificationDeduplicationHelper.shouldSkipRecentlyShownNotification(this, tit, mss)) {
            saveLog("showCustomNotification() duplicate title/message ignored within 2 minutes");
            return;
        }

        if (link != null && link.contains(CGlobalVariables.chat_with_ai_astrologers)) {// ai astrologer chat notification

            boolean isVartaPromoNotification = false;
            boolean isVartaFollowNotification = false;

            if ((link.contains(com.ojassoft.astrosage.varta.utils.CGlobalVariables.varta_astrosage_urls) ||
                    link.contains(com.ojassoft.astrosage.varta.utils.CGlobalVariables.varta_astrosage_url))) {
                isVartaPromoNotification = true;
            }

            if (followNotification) {
                isVartaPromoNotification = false;
                isVartaFollowNotification = true;
                followNotification = false;
                boolean isFollowChecked = CUtils.getBooleanData(this, CGlobalVariables.IS_FOLLOW_NOTIF_SETTING_CHECKED, true);
                if (!isFollowChecked) {
                    return;
                }

            }

            vartaPromotionNotificationEvent(isVartaPromoNotification, isVartaFollowNotification);

            // Track notification shown event
            CUtils.fcmAnalyticsEvents(AI_NOTIFICATION_SHOWN, CGlobalVariables.PERSONALIZED_AI_NOTIFICATION, "");


            CustomAINotification customAINotification = new CustomAINotification(this, extras, link);
            customAINotification.loadNotification();
        } else {
            if (!TextUtils.isEmpty(imgurl)) {
                showNotification(tit, imgurl, mss, link);
            } else {
                sendCustomPushNotification(tit, mss, link);
            }
        }
    }

    private void vartaPromotionNotificationEvent(boolean isVartaPromoNotification, boolean isVartaFollowNotification) {


        if (isVartaPromoNotification) {
            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTICS_VARTA_PROMO_NOTIFICATION_DISPLAYED, CGlobalVariables.FIREBASE_NOTIFICATION_DELIVERED_EVENT, "");
        } else if (isVartaFollowNotification) {
            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTICS_VARTA_FOLLOW_NOTIFICATION_DISPLAYED, CGlobalVariables.FIREBASE_NOTIFICATION_DELIVERED_EVENT, "");
        }

    }

}
