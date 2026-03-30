package com.ojassoft.astrosage.misc;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesResponseListener;
import com.google.gson.Gson;
import com.ojassoft.astrosage.model.AppPurchaseDataSaveModelClass;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import org.json.JSONObject;

import java.util.List;

/**
 * Created by ojas on 2/5/16.
 */
public class ServiceToGetUnConsumedProductFromGoogleAccount extends IntentService {

    String local_signature = "";
    String local_purchase_data = "";
    String local_astro_userid = "";
    String local_price = "";
    String local_priceCurrencycode = "";
    String misc = "";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public ServiceToGetUnConsumedProductFromGoogleAccount() {
        super("ServiceToGetUnConsumedProductFromGoogleAccount");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        try {
            verifyPurchase();
        } catch (Exception e) {
            //Log.e("Output", "onHandleIntent: " + e.getMessage());
        }
    }


    public void verifyPurchase() {
        Log.e("BillingClient", "UnConsumed verifyPurchase enter");
        try {
            misc = getSharedPreferences("MISC_PUR_SERVICE", Context.MODE_PRIVATE).getString("VALUE_SERVICE", "");
            if (AstrosageKundliApplication.billingClient != null) {
                AstrosageKundliApplication.billingClient.queryPurchasesAsync(BillingClient.SkuType.INAPP, new PurchasesResponseListener() {

                    @Override
                    public void onQueryPurchasesResponse(@NonNull BillingResult billingResult, @NonNull List<Purchase> list) {
                        if (billingResult.getResponseCode() !=  BillingClient.BillingResponseCode.OK) {
                            Log.e("BillingClient", "onBillingSetupFinished() FAIL");
                            return;
                        }
                        for (int i = 0; i < list.size(); i++) {
                            Purchase purchase = list.get(i);
                            if (misc.equals("")) {
                                String purchaseToken = purchase.getPurchaseToken();
                                consumePurchase(purchaseToken);
                            } else {
                                //If data is available then send it to server
                                try {
                                    JSONObject purchaseObject = new JSONObject(misc);
                                    if (purchaseObject.getString("purchaseToken").endsWith(purchase.getPurchaseToken())) {
                                        saveDataIntoVariables();
                                        verifyPurchaseFromService();
                                    } else {
                                        // consume product
                                        String purchaseToken = purchase.getPurchaseToken();
                                        consumePurchase(purchaseToken);
                                    }
                                }catch (Exception e){
                                    //
                                }
                            }
                        }

                    }
                });


                AstrosageKundliApplication.billingClient.queryPurchasesAsync(BillingClient.SkuType.SUBS, new PurchasesResponseListener() {

                    @Override
                    public void onQueryPurchasesResponse(@NonNull BillingResult billingResult, @NonNull List<Purchase> list) {
                        if (billingResult.getResponseCode() !=  BillingClient.BillingResponseCode.OK) {
                            Log.e("BillingClient", "onBillingSetupFinished() SUBS FAIL");
                            return;
                        }
                        Log.e("BillingClient", "onBillingSetupFinished() SUBS list.size()="+list.size());
                        for (int i = 0; i < list.size(); i++) {
                            Purchase purchase = list.get(i);
                            Log.e("BillingClient", "onBillingSetupFinished() purchase="+purchase);
                            acknowledgePurchase(purchase);
                        }

                    }
                });
            }
        } catch (Exception e) {
            //
        }
    }


    public void acknowledgePurchase(Purchase purchase) {

        try {
            String purchaseToken = purchase.getPurchaseToken();
            boolean isAcknowledged = purchase.isAcknowledged();

            //Log.d("BillingClient", "acknowledgePurchase() purchaseToken="+purchaseToken);
            //Log.d("BillingClient", "acknowledgePurchase() isAcknowledged="+isAcknowledged);

            if (!isAcknowledged) {
                AcknowledgePurchaseParams acknowledgePurchaseParams =
                        AcknowledgePurchaseParams.newBuilder()
                                .setPurchaseToken(purchaseToken)
                                .build();

                AcknowledgePurchaseResponseListener listener = new AcknowledgePurchaseResponseListener() {
                    @Override
                    public void onAcknowledgePurchaseResponse(@NonNull BillingResult billingResult) {
                        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                            Log.d("BillingClient", "onAcknowledgePurchaseResponse() OK");
                        } else {
                            Log.d("BillingClient", "onAcknowledgePurchaseResponse() FAIL");
                        }
                    }
                };

                AstrosageKundliApplication.billingClient.acknowledgePurchase(acknowledgePurchaseParams, listener);
            }

        } catch (Exception e) {
            Log.d("BillingClient", "consumePurchase() Exception="+e);
        }
    }


    public void consumePurchase(String purchaseToken) {
        Log.e("BillingClient", "onBillingSetupFinished() purchaseToken="+purchaseToken);
        try {
            ConsumeParams consumeParams =
                    ConsumeParams.newBuilder()
                            .setPurchaseToken(purchaseToken)
                            .build();

            ConsumeResponseListener listener = new ConsumeResponseListener() {
                @Override
                public void onConsumeResponse(BillingResult billingResult, String purchaseToken) {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        Log.e("BillingClient", "onConsumeResponse() OK "+purchaseToken);
                    } else {
                        Log.e("BillingClient", "onConsumeResponse() FAIL "+purchaseToken);
                    }
                }
            };

            AstrosageKundliApplication.billingClient.consumeAsync(consumeParams, listener);
        } catch (Exception ex) {
            Log.i("BillingClient", "-" + ex.getMessage());
        }
    }

    private void saveDataIntoVariables() {

        try {
            SharedPreferences purchasageDataPlanSharedPreferences = getSharedPreferences(CGlobalVariables.ASTROSAGEPURCHASEPLANFORSERVICE, Context.MODE_PRIVATE);
            Gson gson = new Gson();
            String json = purchasageDataPlanSharedPreferences.getString(CGlobalVariables.ASTROSAGEPURCHASEPLANFORSERVICEOBJECT, "");
            AppPurchaseDataSaveModelClass appPurchaseDataSaveModelClassArrayList = gson.fromJson(json, AppPurchaseDataSaveModelClass.class);

            local_signature = appPurchaseDataSaveModelClassArrayList.getSignature();
            local_purchase_data = appPurchaseDataSaveModelClassArrayList.getPurchaseData();
            local_astro_userid = appPurchaseDataSaveModelClassArrayList.getPurchaseUserID();
            local_price = appPurchaseDataSaveModelClassArrayList.getPrice();
            local_priceCurrencycode = appPurchaseDataSaveModelClassArrayList.getPriceCurrencycode();

        } catch (Exception ex) {
            //Log.i("Exception", "-"+ex.getMessage());
        }

    }

    private void verifyPurchaseFromService() {

        if (!local_signature.equals("") && !local_purchase_data.equals("")) {

            if (local_astro_userid.equals("")) {
                local_astro_userid = CUtils.getUserName(ServiceToGetUnConsumedProductFromGoogleAccount.this);
            }

            String layoutPosition = CUtils.getStringData(getApplicationContext(), "MISC_PUR_SERVICE_LAYOUT_POSITION", "");
            String textMsg = CUtils.getStringData(getApplicationContext(), "MISC_PUR_SERVICE_MSG_TEXT", "");
            String chatId = CUtils.getStringData(getApplicationContext(), "MISC_PUR_SERVICE_MSG_CHAT_ID", "");

            Intent pvsIntent = new Intent(getApplicationContext(),
                    VerificationServiceForInAppBillingChat.class);
            pvsIntent.putExtra("SIGNATURE", local_signature);
            pvsIntent.putExtra("PURCHASE_DATA", local_purchase_data);
            pvsIntent.putExtra("ASTRO_USERID", local_astro_userid);

            pvsIntent.putExtra("MESSAGE_TEXT", textMsg);
            pvsIntent.putExtra("MESSAGE_CHAT_ID", chatId);

            pvsIntent.putExtra("price", local_price);
            pvsIntent.putExtra("priceCurrencycode", local_priceCurrencycode);
            pvsIntent.putExtra("FullJsonDataObj", CUtils.getStringData(getApplicationContext(), CGlobalVariables.PAYMENTDONE_KEY_FOR_ASTRO_SERVICES_JSONOBJ, ""));

            pvsIntent.putExtra("layoutPostion", "" + layoutPosition);
            pvsIntent.putExtra("messageTitle", "Order Insert");

            startService(pvsIntent);
        }

    }
}
