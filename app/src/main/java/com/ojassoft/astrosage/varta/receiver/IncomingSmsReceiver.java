package com.ojassoft.astrosage.Receiver;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ojassoft.astrosage.varta.interfacefile.CancellingResendOtpThread;
import com.ojassoft.astrosage.varta.ui.activity.OtpVerifyActivity;


public class IncomingSmsReceiver extends BroadcastReceiver implements CancellingResendOtpThread {

    private static String message;
    Context myContext;
    String phone;
    Activity callingActivityObj;

    public IncomingSmsReceiver() {

    }

    public IncomingSmsReceiver(Activity callingActivityObj) {

        this.callingActivityObj = callingActivityObj;
        //Log.e("SmsReceiver registered", "");
    }

    public void onReceive(Context context, Intent intent) {
        //Log.e("SmsReceiver", " sms recieved");
        // Retrieves a map of extended data from the intent.
        final Bundle bundle = intent.getExtras();
        myContext = context;

        try {

            if (bundle != null) {

                final Object[] pdusObj = (Object[]) bundle.get("pdus");

                for (int i = 0; i < pdusObj.length; i++) {

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                    String senderNum = currentMessage.getDisplayOriginatingAddress();
                    message = currentMessage.getDisplayMessageBody();
                    //Log.e("SmsReceiver", "senderNum: " + senderNum + "; message: " + message);
                    //phone = preferences.getString(AppConstants.keyMobileNo, AppConstants.entityNotPresent);
                    int startIndex = message.length()-4;
                    int endIndex = message.length();
                    String receivedOtp = message.substring(startIndex, endIndex).trim();
                    //Log.e("SmsReceiver", "receivedOtp = " + receivedOtp);
                    if (receivedOtp.length() == 4) {
                        LocalBroadcastManager.getInstance(context).unregisterReceiver(this);
                        cancelHandler(true);
                        if (myContext instanceof OtpVerifyActivity) {
                            ((OtpVerifyActivity) myContext).autoFillOtp(receivedOtp);
                        }
                    }
                }
            }

        } catch (Exception e) {
            //Log.e("SmsReceiver", "Exception smsReceiver" + e);
        }
    }

    @Override
    public void cancelHandler(boolean iscancel) {

    }
}






