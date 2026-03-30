package com.ojassoft.astrosage.varta.twiliochat.chat.messages;

import android.content.Context;
import android.util.Log;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;

public class LeftStatusMessage extends StatusMessage {

    Context context;

    public LeftStatusMessage(String author, Context context) {
        super(author);
        this.context = context;
    }

    @Override
    public String getMessageBody(Context context) {

        String auther = this.getAuthor();
        if(auther == null ) auther = "";
        String msgStatus = "";

        if(!auther.contains(context.getResources().getString(R.string.follow_astrologer)))
                msgStatus = auther + " left the channel";

        if(auther.equals(CGlobalVariables.ASTROLOGER)){
            msgStatus = context.getResources().getString(R.string.chat_end_by_astrologer);
        }else if(auther.equals(CGlobalVariables.CHAT_INTERNET_DISCONNECTION)){
            msgStatus = context.getResources().getString(R.string.chat_end_due_to_internet);
        }else if(auther.equals(CGlobalVariables.USER)){
            msgStatus = context.getResources().getString(R.string.chat_end_by_you_msg);
        }else if(auther.contains(context.getResources().getString(R.string.follow_astrologer))){
            msgStatus = auther;
        }else {
            if (CGlobalVariables.CHAT_END_STATUS.equalsIgnoreCase(CGlobalVariables.COMPLETED) ||
                    CGlobalVariables.CHAT_END_STATUS.equalsIgnoreCase(CGlobalVariables.USER_BUSY)) {
                msgStatus = context.getResources().getString(R.string.chat_end_by_you_msg);
            } else {
                if (!CUtils.isConnectedWithInternet(context)) {
                    msgStatus = context.getResources().getString(R.string.chat_end_due_to_internet);
                } else {
                    if (CUtils.getUserLoginStatus(context)) {
                        try {
                            String walletBalance = CUtils.getWalletRs(context);
                            double walletBalanceRs = Double.parseDouble(walletBalance);
                            if (walletBalanceRs <= 0) {
                                msgStatus = context.getResources().getString(R.string.chat_end_due_to_low_balance);
                            }
                        } catch (Exception e) {
                            //
                        }
                    } else {
                        msgStatus = context.getResources().getString(R.string.chat_end_by_astrologer);
                    }
                }
            }
        }
        return msgStatus;
    }

    @Override
    public void setMessageBody(String body) {

    }
}