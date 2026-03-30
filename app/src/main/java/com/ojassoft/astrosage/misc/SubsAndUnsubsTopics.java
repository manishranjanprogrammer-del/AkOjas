package com.ojassoft.astrosage.misc;

import android.app.IntentService;
import android.content.Intent;

import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

/**
 * Created by ojas on 11/4/17.
 */

public class SubsAndUnsubsTopics extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public SubsAndUnsubsTopics() {
        super(SubsAndUnsubsTopics.class.getName());
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        try {
            if(CUtils.isConnectedWithInternet(getApplicationContext())){
                String regid = CUtils.getRegistrationId(getApplicationContext());
                //Do Action For Topic ALL
                String topicAll =  CUtils.getStringData(getApplicationContext(), CGlobalVariables.TOPIC_ALL_NEED_TO_SEND_TO_GOOGLE_SERVER,"");
                CUtils.saveStringData(getApplicationContext(), CGlobalVariables.TOPIC_ALL_NEED_TO_SEND_TO_GOOGLE_SERVER,"");
                if(!topicAll.equals("") && regid.trim().length() != 0){
                    CUtils.subscribeTopics(regid, CGlobalVariables.TOPIC_ALL,getApplicationContext());
                }

                String topicExtra=  CUtils.getStringData(getApplicationContext(), CGlobalVariables.EXTRA_TOPIC_NEED_TO_SEND_TO_GOOGLE_SERVER,"");
                CUtils.saveStringData(getApplicationContext(), CGlobalVariables.EXTRA_TOPIC_NEED_TO_SEND_TO_GOOGLE_SERVER,"");
                if(!topicExtra.equals("") && regid.trim().length() != 0){
                    CUtils.subscribeTopics(regid, topicExtra,getApplicationContext());
                }

                //Do Action for Topic Version Code
                String topicVersion =  CUtils.getStringData(getApplicationContext(), CGlobalVariables.VERSION_TOPIC_NEED_TO_SEND_TO_GOOGLE_SERVER,"");
                CUtils.saveStringData(getApplicationContext(), CGlobalVariables.VERSION_TOPIC_NEED_TO_SEND_TO_GOOGLE_SERVER,"");
                if(!topicVersion.equals("") && regid.trim().length() != 0){
                    CUtils.subscribeTopics(regid,topicVersion,getApplicationContext());
                }

                //Do Action For Language Topics
                String languageTopic =  CUtils.getStringData(getApplicationContext(), CGlobalVariables.LANGUAGE_TOPIC_NEED_TO_SEND_TO_GOOGLE_SERVER,"");

                if(!languageTopic.equals("") && regid.trim().length() != 0) {

                    //First unsubscrebe topic
                    for (int i = 0; i < CGlobalVariables.langArr.length; i++) {
                        CUtils.unSubscribeTopics(regid, CUtils.getTopicName(CGlobalVariables.langArr[i]), getApplicationContext());
                    }
                    //subscribe topic
                    CUtils.saveStringData(getApplicationContext(), CGlobalVariables.LANGUAGE_TOPIC_NEED_TO_SEND_TO_GOOGLE_SERVER,"");
                    CUtils.subscribeTopics(regid,languageTopic,getApplicationContext());
                }

                //Do Action For Others Topics
                String otherTopic =  CUtils.getStringData(getApplicationContext(), CGlobalVariables.OTHER_TOPIC_NEED_TO_SEND_TO_GOOGLE_SERVER,"");
                CUtils.saveStringData(getApplicationContext(), CGlobalVariables.OTHER_TOPIC_NEED_TO_SEND_TO_GOOGLE_SERVER,"");
                if(!otherTopic.equals("") && regid.trim().length() != 0){
                    CUtils.subscribeTopics(regid,otherTopic,getApplicationContext());
                }

                int kundliCount =  CUtils.getKundliCount(this);
                boolean isAstrologer5 =  CUtils.getBooleanData(this,CGlobalVariables.TOPIC_IS_ASTROLOGER_5,false);
                if(kundliCount > 5 && regid.trim().length() != 0 && !isAstrologer5){
                    CUtils.subscribeTopics(regid, CGlobalVariables.TOPIC_IS_ASTROLOGER_5, getApplicationContext());
                    CUtils.saveBooleanData(this,CGlobalVariables.TOPIC_IS_ASTROLOGER_5,true);
                }

                boolean isAstrologer10 =  CUtils.getBooleanData(this,CGlobalVariables.TOPIC_IS_ASTROLOGER_10,false);
                if(kundliCount > 10 && regid.trim().length() != 0 && !isAstrologer10){
                    CUtils.unSubscribeTopics(regid, CGlobalVariables.TOPIC_IS_ASTROLOGER_5, getApplicationContext());
                    CUtils.subscribeTopics(regid, CGlobalVariables.TOPIC_IS_ASTROLOGER_10, getApplicationContext());
                    CUtils.saveBooleanData(this,CGlobalVariables.TOPIC_IS_ASTROLOGER_10,true);
                }
            }
        }catch (Exception ex){
            //Log.e("Exception",ex.getMessage().toString());
        }
    }

}
