package com.ojassoft.astrosage.ui.fragments.astrochatfragment.savedata;

import android.app.IntentService;
import android.content.Intent;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ojassoft.astrosage.ui.fragments.astrochatfragment.model.MessageDecode;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.util.ArrayList;

/**
 * Created by ojas-10 on 8/3/16.
 */
public class SaveDataInternalStorage extends IntentService {
    public SaveDataInternalStorage() {
        super("SaveDataInternalStorage");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //startForeground(1,new Notification());
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //Log.e("@@@@", "!!!!!go");
        String layoutPosition="";
        try {
            ArrayList<MessageDecode> messageDecodeArrayList1 = (ArrayList<MessageDecode>) InternalStorage.readObject(this, CGlobalVariables.CHATWITHASTROLOGER);
            // if(intent.hasExtra("layoutPositon"))
            layoutPosition = intent.getStringExtra("layoutPositon");
            Boolean isInsert = intent.getBooleanExtra(CGlobalVariables.ISINSERT, true);
            if (layoutPosition != null && !layoutPosition.isEmpty()) {
                ArrayList<MessageDecode> chatMessageArrayList = CUtils.getDataFromPrefrence(this);
                chatMessageArrayList.get(Integer.parseInt(layoutPosition)).setNotPaidLayoutShow(intent.getStringExtra("paymentStatus"));
                chatMessageArrayList.get(Integer.parseInt(layoutPosition)).setChatId(intent.getStringExtra("chatid"));
                //added by Ankit on 7/3/2019
                chatMessageArrayList.get(Integer.parseInt(layoutPosition)).setOrderId(intent.getStringExtra("orderid"));
                InternalStorage.writeObject(this, chatMessageArrayList);
                sendResult(null);
            } else if (isInsert) {
                MessageDecode messageDecode = intent.getParcelableExtra(CGlobalVariables.CHATWITHASTROLOGER);
                if (messageDecodeArrayList1 != null) {
                    messageDecodeArrayList1.add(messageDecode);
                } else {
                    messageDecodeArrayList1 = new ArrayList<>();
                    messageDecodeArrayList1.add(messageDecode);
                }
                InternalStorage.writeObject(this, messageDecodeArrayList1);
            } else {
                messageDecodeArrayList1 = intent.getParcelableArrayListExtra(CGlobalVariables.CHATWITHASTROLOGER);
                InternalStorage.writeObject(this, messageDecodeArrayList1);
            }
        } catch (Exception e) {
            //Log.e("Exception", "!!" + e.getMessage());
        }
    }

    public void sendResult(MessageDecode messageDecode) {
        Intent intent = new Intent(CGlobalVariables.COPA_RESULT);
        if (messageDecode != null) {
            intent.putExtra(CGlobalVariables.COPA_MESSAGE, messageDecode);
        }
        intent.putExtra(CGlobalVariables.UPDATE_PLAN_STATUS, true);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
    }
}
