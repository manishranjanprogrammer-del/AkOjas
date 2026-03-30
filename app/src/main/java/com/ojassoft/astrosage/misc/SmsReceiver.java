package com.ojassoft.astrosage.misc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import com.ojassoft.astrosage.jinterface.SmsListener;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SmsReceiver extends BroadcastReceiver {

    private static SmsListener mListener;

    @Override
    public void onReceive(Context context, Intent intent) {
           String OTP_REGEX = "[0-9]{1,4}";
        Bundle data = intent.getExtras();

        Object[] pdus = (Object[]) data.get("pdus");

        for (int i = 0; i < pdus.length; i++) {
            SmsMessage smsMessage = SmsMessage.createFromPdu((byte[]) pdus[i]);

            String sender = smsMessage.getDisplayOriginatingAddress();
            //You must check here if the sender is your provider and not another one with same text.

            String messageBody = smsMessage.getMessageBody();
            String[] messsages=messageBody.split("\\.");
            Pattern pattern = Pattern.compile(OTP_REGEX);
            Matcher matcher = pattern.matcher(messsages[0]);
            String otp = "";
            while (matcher.find())
            {
                otp = matcher.group();
            }
            //Pass on the text to our listener.
            if (mListener != null) {
                mListener.messageReceived(otp);
            }

        }

    }

    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
}
