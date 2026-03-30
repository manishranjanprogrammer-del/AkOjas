package com.ojassoft.astrosage.misc;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.ojassoft.astrosage.utils.UserEmailFetcher;

/**
 * Created by ojas-20 on 10/2/17.
 */

public class VerificationServiceForInAppBillingChat extends IntentService {
    //1.VERIFY PURCHASE
    //2.CONSUME PURCHASE

    String purchaseData = "", signature = "",  astroSageUserId = "",price="0",priceCurrencycode="INR",
            fullJsonDataObj="",layoutPosition="",messageText="",chatid="";
    String emailId ;
    String orderId="";
    Context context;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public VerificationServiceForInAppBillingChat() {
        super("VerificationServiceForInAppBilling");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        //Log.e("BillingClient", "VerificationServiceForInAppBillingChat onHandleIntent enter");

        signature = intent.getExtras().getString("SIGNATURE");
        purchaseData = intent.getExtras().getString("PURCHASE_DATA");
        astroSageUserId = intent.getExtras().getString("ASTRO_USERID");
        price = intent.getExtras().getString("price");
        priceCurrencycode = intent.getExtras().getString("priceCurrencycode");
        fullJsonDataObj= intent.getExtras().getString("FullJsonDataObj");

        layoutPosition= intent.getExtras().getString("layoutPostion");
        if(layoutPosition == null){
            layoutPosition = "";
        }

        messageText = intent.getExtras().getString("MESSAGE_TEXT");
        chatid = intent.getExtras().getString("MESSAGE_CHAT_ID");

        orderId = intent.getExtras().getString("ORDER_ID");

        if (signature.length() > 0 && purchaseData.length() > 0) {
            SendReportToServerAsyn();
        }

    }

    private void  SendReportToServerAsyn() {
        //Log.e("BillingClient", "VerificationService SendReportToServerAsyn() enter");
        emailId = UserEmailFetcher.getEmail(getApplicationContext());
        context = getApplicationContext();
        try {
            new SendUserPurchaseReportForServiceToServerChat(context, purchaseData, emailId, astroSageUserId, price, priceCurrencycode, fullJsonDataObj, layoutPosition, messageText, chatid, orderId).sendReportToServer();
        } catch (Exception e) {
            //
        }
    }

}