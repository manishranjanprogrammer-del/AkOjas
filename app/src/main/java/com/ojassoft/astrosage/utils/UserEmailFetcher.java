package com.ojassoft.astrosage.utils;

import android.content.Context;
import android.text.TextUtils;

import com.ojassoft.astrosage.model.GmailAccountInfo;


public class UserEmailFetcher {
    public static String getEmail(Context context) {

        String emailId = CUtils.getAstroshopUserEmail(context);

        GmailAccountInfo gmailAccountInfo = CUtils.getGmailAccountInfo(context);

        if (gmailAccountInfo != null && !TextUtils.isEmpty(gmailAccountInfo.getId())) {
            emailId = gmailAccountInfo.getId();
        }

        if(!CUtils.isValidEmail(emailId)){
            emailId = CUtils.getAstroshopUserEmail(context);
        }

        if (emailId == null || emailId.equalsIgnoreCase("null")) {
            emailId = "";
        }
        return emailId;


        //boolean hasEmailIdPermission = CUtils.hasContactsPermission(context);
        /*boolean hasUserPermission = CUtils.getBooleanData(context,CGlobalVariables.EmailId_Permission,false);
        if(hasUserPermission && hasEmailIdPermission) {
            AccountManager accountManager = AccountManager.get(context);
            Account account = getAccount(accountManager);

            if (account == null) {
                return null;
            } else {
                return account.name;
            }
        }else{
            return "";
        }*/
    }

    /*private static Account getAccount(AccountManager accountManager) {
        Account[] accounts = accountManager.getAccountsByType("com.google");
        Account account;
        if (accounts.length > 0) {
            account = accounts[0];
        } else {
            account = null;
        }
        return account;
    }*/

}
