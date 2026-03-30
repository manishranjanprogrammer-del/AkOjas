package com.ojassoft.astrosage.custompushnotification;

import android.app.IntentService;
import android.content.Intent;
import android.os.AsyncTask;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.beans.TopicDetail;
import com.ojassoft.astrosage.controller.ControllerManager;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.util.ArrayList;

public class MyCloudRegistrationService extends IntentService {

    private String regid;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public MyCloudRegistrationService() {
        super("MyCloudRegistrationServiceOld");
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.e("MyCloudRegistrationSer", "onHandleIntent");
        getFcmToken();
    }

    private void getFcmToken() {
        regid = CUtils.getRegistrationId(getApplicationContext());
        if (TextUtils.isEmpty(regid)) {
            FirebaseMessaging.getInstance().getToken()
                    .addOnCompleteListener(new OnCompleteListener<String>() {
                        @Override
                        public void onComplete(@NonNull Task<String> task) {
                            if (!task.isSuccessful()) {
                                return;
                            }
                            // Get new FCM registration token
                            String token = task.getResult();
                            CUtils.storeRegistrationId(getApplicationContext(), token);
                            regid = token;
                            getRegistrationIfFromGCM();
                        }
                    });
        } else {
            getRegistrationIfFromGCM();
        }
    }

    private void getRegistrationIfFromGCM() {
        try {
            Log.e("MyCloudRegistrationSer", "getRegistrationIfFromGCM");
            new AsyncTaskRegisterInBackground().execute(null, null, null);
        } catch (Exception e) {
            //Log.e("REG ID-ERROR", e.getMessage());
        }
        MyCloudRegistrationService.this.stopSelf();
    }

    class AsyncTaskRegisterInBackground extends AsyncTask<String, Long, Void> {
        boolean isSuccess = true;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            Log.e("MyCloudRegistrationSer", "onPreExecute");
        }

        @Override
        protected Void doInBackground(String... paramArrayOfParams) {
            // TODO Auto-generated method stub

            try {
                Log.e("MyCloudRegistrationSer", "doInBackground");
                try {
                    CUtils.subscribeTopics(regid, CGlobalVariables.TOPIC_ALL, MyCloudRegistrationService.this);
                    CUtils.subscribeTopics(regid, CGlobalVariables.TOPIC_VERSION + BuildConfig.VERSION_CODE, MyCloudRegistrationService.this);
                    CUtils.subscribeTopics(regid, CGlobalVariables.TOPIC_UPDATE_LIVE_ASTRO, MyCloudRegistrationService.this);

                    //Check for previous topics
                    ArrayList<TopicDetail> topicList = CUtils.loadTopics(MyCloudRegistrationService.this);
                    if (topicList != null) {
                        for (int i = 0; i < topicList.size(); i++) {
                            if (topicList.get(i).isSubscribed()) {
                                CUtils.subscribeTopics(regid, topicList.get(i).getTopicId(), MyCloudRegistrationService.this);
                            }
                        }
                    }

                } catch (Exception ex) {
                    Log.e("Exception", ex.getMessage());
                }

                CUtils.saveIntData(getApplicationContext(), CGlobalVariables.ISInstanceID, 2);

            } catch (Exception ex) {

                isSuccess = false;
                //For lower veresion services
                try {
                    isSuccess = true;
                    CUtils.saveIntData(getApplicationContext(), CGlobalVariables.ISInstanceID, 2);

                } catch (Exception ex1) {
                    isSuccess = false;
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            if (isSuccess) {
                Log.e("MyCloudRegistrationSer", "onPostExecute");
                String loginId = CUtils.getUserName(getApplicationContext());
                int languageCode = ((AstrosageKundliApplication) getApplication())
                        .getLanguageCode();
                new ControllerManager()
                        .saveUserGCMRegistrationInformationOnOjasServer(
                                getApplicationContext(), regid, languageCode,
                                loginId);

                new AsyncTask<Integer, Void, String>() {
                    @Override
                    protected String doInBackground(Integer... params) {
                        //UnSubscribed Old Topic
                        try {
                            for (int i = 0; i < CGlobalVariables.langArr.length; i++) {
                                CUtils.unSubscribeTopics(regid, CUtils.getTopicName(CGlobalVariables.langArr[i]), getApplicationContext());
                            }
                        } catch (Exception ex) {
                            Log.e("Exception", ex.getMessage());
                        }
                        //Subscribed New Topic
                        try {
                            String topicName = CUtils.getTopicName(params[0]);
                            CUtils.subscribeTopics(regid, topicName, getApplicationContext());
                        } catch (Exception ex) {
                            Log.e("Exception", ex.getMessage());
                        }

                        try {

                            int appVersionPref = CUtils.getIntData(getApplicationContext(), "AppVersion", 0);
                            int currentAppVersion = BuildConfig.VERSION_CODE;
                            //Log.e("TestLang", "appVersionPref="+appVersionPref+"  currentAppVersion="+currentAppVersion);
                            if(appVersionPref < currentAppVersion){ // check if app update
                                CUtils.saveIntData(getApplicationContext(), "AppVersion", currentAppVersion);
                                String labell = "launch_langauge_change_" + CUtils.getLangForAnalytics(params[0]);
                                CUtils.fcmAnalyticsEvents(labell, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                                //Log.e("TestLang", "labell="+labell);
                            }

                            String countryCode = com.ojassoft.astrosage.varta.utils.CUtils.getDeviceCountryCode(getApplicationContext());
                            //Log.d("TestCountry", "countryCode="+countryCode);

                            String countryTopicName = CGlobalVariables.TOPIC_INTERNATIONAL;
                            if(TextUtils.isEmpty(countryCode) || countryCode.equalsIgnoreCase(CGlobalVariables.COUNTRY_CODE_IN)){
                                countryTopicName = CGlobalVariables.TOPIC_DOMESTIC;
                            }

                            // unsubscribe previous topics
                            CUtils.unSubscribeTopics(regid, CGlobalVariables.TOPIC_INTERNATIONAL, getApplicationContext());
                            CUtils.unSubscribeTopics(regid, CGlobalVariables.TOPIC_DOMESTIC, getApplicationContext());
                            // subscribe new
                            CUtils.subscribeTopics(regid, countryTopicName, getApplicationContext());

                        }catch (Exception e){
                            //
                        }

                        return null;
                    }
                }.execute(languageCode, null, null);

            }

        }


    }

}