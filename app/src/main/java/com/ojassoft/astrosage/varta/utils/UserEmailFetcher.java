package com.ojassoft.astrosage.varta.utils;

import android.content.Context;
import android.text.TextUtils;

import com.ojassoft.astrosage.varta.model.GmailAccountInfo;


public class UserEmailFetcher {
    public static String getEmail(Context context) {

        String emailId = "";
        try {
            GmailAccountInfo gmailAccountInfo = CUtils.getGmailAccountInfo(context);
            if (gmailAccountInfo != null) {
                emailId = gmailAccountInfo.getEmailId();
            }
            if(TextUtils.isEmpty(emailId )) {
                emailId = gmailAccountInfo.getId();
            }
        } catch (Exception ex) {
            //
        }
        if(emailId == null) emailId = "";
        return emailId;
    }
    public static String getPhone(Context context) {

        String phone = "";
        try {
            GmailAccountInfo gmailAccountInfo = CUtils.getGmailAccountInfo(context);
            if (gmailAccountInfo != null) {
                phone = gmailAccountInfo.getMobileNo();
            }
        } catch (Exception ex) {
            //
        }
        if(phone == null) phone = "";
        return phone;
    }

}
