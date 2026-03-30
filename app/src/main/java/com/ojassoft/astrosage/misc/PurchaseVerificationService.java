package com.ojassoft.astrosage.misc;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.ojassoft.astrosage.utils.UserEmailFetcher;

public class PurchaseVerificationService extends IntentService {
//1.VERIFY PURCHASE 
//2.CONSUME PURCHASE	


    String source, purchaseData = "", signature = "", PURCHASE_TOKEN = "", astroSageUserId = "",price="0",priceCurrencycode="INR",formattedPrice="", freetrialperiod="";
    boolean openForAIChat = false;

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     */
    public PurchaseVerificationService() {
        super("PurchaseVerificationService");
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub

        return null;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d("BillingClient", "onHandleIntent() 1");
        signature = intent.getExtras().getString("SIGNATURE");
        purchaseData = intent.getExtras().getString("PURCHASE_DATA");
        astroSageUserId = intent.getExtras().getString("ASTRO_USERID");
        price = intent.getExtras().getString("price");
        priceCurrencycode = intent.getExtras().getString("priceCurrencycode");
        formattedPrice = intent.getExtras().getString("formattedPrice");
        freetrialperiod = intent.getExtras().getString("freetrialperiod");
        openForAIChat = intent.getBooleanExtra("openForAIChat", false);
        source = intent.getExtras().getString("source");
        if (signature.length() > 0 && purchaseData.length() > 0) {
            SendReportToServerAsyn();
        }

    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
    }

    /* private void verifyPurchase() {
        try {
            boolean isPurchaseVerify = false;
            String purchaseToken = "";
            ////Log.e("Total data", data.getExtras().toString());
            ////Log.e("INAPP_DATA_SIGNATURE", signature);
            // //Log.e("INAPP_PURCHASE_DATA-INSERVICE", purchaseData);
            JSONObject purchaseObject = new JSONObject(purchaseData);
            Bundle skuDetails = AstrosageKundliApplication.mservice.getPurchases(3, getPackageName(), "inapp", null);

            int response = skuDetails.getInt(IabHelper.RESPONSE_CODE);
            if (response == 0&&CUtils.getBooleanData(PurchaseVerificationService.this, CGlobalVariables.ISGOOLGEVERIFY,true)) {
                ArrayList<String> responseList = skuDetails
                        .getStringArrayList(IabHelper.RESPONSE_INAPP_PURCHASE_DATA_LIST);
                breakVerifyPurchaseLoop:
                for (String thisResponse : responseList) {
                    if (!isPurchaseVerify) {
                        JSONObject object = new JSONObject(thisResponse);

                        if (purchaseObject.getString("purchaseToken").endsWith(object.getString("purchaseToken"))
                                && developerPayload.endsWith(object.getString("developerPayload"))) {
                            purchaseToken = purchaseObject.getString("purchaseToken");
                            PURCHASE_TOKEN = purchaseToken;
                            // //Log.e("INAPP_PURCHASE_TOKEN", PURCHASE_TOKEN);
                              isPurchaseVerify = true;
                              break breakVerifyPurchaseLoop;
                            int responseOne = AstrosageKundliApplication.mservice.consumePurchase(3, getPackageName(), purchaseToken);
                            ////Log.e("RESPONSE_CONSUME_PURCHASE",String.valueOf(response));
                            if (response == 0) {
                                purchaseConsume = true;
                                CUtils.saveBooleanData(context, CGlobalVariables.ISGOOLGEVERIFY, false);
                            }
                        }
                    }
                }
            }
            else if(CUtils.getBooleanData(PurchaseVerificationService.this, CGlobalVariables.ISSERVERVERIFY,true))
            {
                new SendReportToServerAsyn().execute();
            }

            if (isPurchaseVerify) {
                if (Security.verifyPurchase(encodedPublicKey, purchaseData, signature))
                    //consumePurchase(purchaseToken);
                else
                    stopSelf();
            } else
                stopSelf();
        } catch (Exception e) {
                   //Log.e("Exception","Exception"+e.getMessage());
        }

    }
*/
   /* public void consumePurchase(String purchaseToken) {
        boolean purchaseConsume = false;
        try {
            int response = AstrosageKundliApplication.mservice.consumePurchase(3, getPackageName(), purchaseToken);
            ////Log.e("RESPONSE_CONSUME_PURCHASE",String.valueOf(response));
            if (response == 0) {
                purchaseConsume = true;
                CUtils.saveBooleanData(PurchaseVerificationService.this, CGlobalVariables.ISGOOLGEVERIFY, false);
            }


        } catch (Exception e) {
            // TODO Auto-generated catch block
            ////Log.e("RESPONSE_ERROR_CONSUME_PURCHASE",e.getMessage());
        }
        //stopSelf();
        if (purchaseConsume)
            new SendReportToServerAsyn().execute();

    }
*/
    private void SendReportToServerAsyn(){
        String emailId, userId = "";
        Context context;

        emailId = UserEmailFetcher.getEmail(getApplicationContext());
        context = getApplicationContext();

        try {
            new SendUserPurchaseReportToServer(context, purchaseData, emailId, astroSageUserId,price,priceCurrencycode,freetrialperiod,openForAIChat, formattedPrice,source).sendReportToServer();
        } catch (Exception e) {

        }
    }

    /*class SendReportToServerAsyn extends AsyncTask<String, Long, Void> {
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
                new SendUserPurchaseReportToServer(context, purchaseData, emailId, astroSageUserId,price,priceCurrencycode).sendReportToServer();
            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
            PurchaseVerificationService.this.stopSelf();
        }

    }*/


}
