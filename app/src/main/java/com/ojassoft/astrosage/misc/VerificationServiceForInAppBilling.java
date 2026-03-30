package com.ojassoft.astrosage.misc;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.Nullable;

import com.ojassoft.astrosage.ui.fragments.astrochatfragment.model.MessageDecode;
import com.ojassoft.astrosage.utils.UserEmailFetcher;

/**
 * Created by ojas on १२/७/१६.
 */
public class VerificationServiceForInAppBilling extends IntentService {
    //1.VERIFY PURCHASE
//2.CONSUME PURCHASE


    String developerPayload = "", purchaseData = "", signature = "",  astroSageUserId = "",price="0",priceCurrencycode="INR",
            fullJsonDataObj="",layoutPosition="",messageText="",chatid="";
    String emailId ;
    Context context;
    MessageDecode messageDecode;
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     *
     */
    public VerificationServiceForInAppBilling() {
        super("VerificationServiceForInAppBilling");
    }

/*
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        signature = intent.getExtras().getString("SIGNATURE");
        purchaseData = intent.getExtras().getString("PURCHASE_DATA");
        developerPayload = intent.getExtras().getString("DEVELOPER_PAYLOAD");
        astroSageUserId = intent.getExtras().getString("ASTRO_USERID");
        price = intent.getExtras().getString("price");
        priceCurrencycode = intent.getExtras().getString("priceCurrencycode");
        fullJsonDataObj= intent.getExtras().getString("FullJsonDataObj");
        if (signature.length() > 0 && purchaseData.length() > 0 && developerPayload.length() > 0)
            new SendReportToServerAsyn().execute();
        else
            stopSelf();

        return START_STICKY;
    }*/

    @Override
    protected void onHandleIntent(Intent intent) {
        signature = intent.getExtras().getString("SIGNATURE");
        purchaseData = intent.getExtras().getString("PURCHASE_DATA");
        developerPayload = intent.getExtras().getString("DEVELOPER_PAYLOAD");
        astroSageUserId = intent.getExtras().getString("ASTRO_USERID");
        price = intent.getExtras().getString("price");
        priceCurrencycode = intent.getExtras().getString("priceCurrencycode");
        fullJsonDataObj= intent.getExtras().getString("FullJsonDataObj");
        if (signature.length() > 0 && purchaseData.length() > 0 && developerPayload.length() > 0) {
         //   new SendReportToServerAsyn().execute();
            SendReportToServerAsyn();

        }

    }

   private void  SendReportToServerAsyn(){

       emailId = UserEmailFetcher.getEmail(getApplicationContext());
       context = getApplicationContext();
       try {
           new SendUserPurchaseReportForServiceToServer(context, purchaseData, emailId, astroSageUserId,price,priceCurrencycode,fullJsonDataObj,layoutPosition,messageText,chatid).sendReportToServer();
       } catch (Exception e) {

       }
   }
   /* class SendReportToServerAsyn extends AsyncTask<String, Long, Void> {
        String emailId, userId = "";
        Context context;

        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            emailId = UserEmailFetcher.getEmail(getApplicationContext());
            context = getApplicationContext();
        }

        @Override
        protected Void doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {
                new SendUserPurchaseReportForServiceToServer(context, purchaseData, emailId, astroSageUserId,price,priceCurrencycode,fullJsonDataObj).sendReportToServer();
            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            VerificationServiceForInAppBilling.this.stopSelf();
        }

    }*/

}
