package com.ojassoft.astrosage.misc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.ojassoft.astrosage.model.AppPurchaseDataSaveModelClass;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.fragments.astrochatfragment.model.MessageDecode;
import com.ojassoft.astrosage.ui.fragments.astrochatfragment.savedata.SaveDataInternalStorage;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.libojassoft.android.misc.VolleyResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ojas on १२/७/१६.
 */
public class SendUserPurchaseReportForServiceToServer implements VolleyResponse {

    String requestURL = "https://inappverify.astrosage.com/inapppurchaseverification",

    // String requestURL ="http://192.168.1.70:8090/inapppurchaseverification",
    /*"http://www.astrocamp.com/app/json/api-buy-cloud-plan.asp",*/
    key = "jsonInput", value, purchaseData, emailId, userId, misc, price = "0", priceCurrencycode = "INR", fullJsonDataObj = "", key2 = "jsonInputForService", key3 = "isPaymentDonestatus",
            key4 = "appVerName", layoutPosition = "", key5 = "type", questionOfMessage = "problem", chatid = "chatid";
    Context context;
    String appVerName, messageText, messageChatID;

    public SendUserPurchaseReportForServiceToServer(Context context, String purchaseData, String emailId, String userId, String price, String priceCurrencycode, String fullJsonDataObj, String layoutPosition, String messageText, String messageChatID) {
        this.context = context;
        this.purchaseData = purchaseData;
        this.emailId = emailId;
        this.userId = userId;
        this.price = price;
        this.priceCurrencycode = priceCurrencycode;
        this.fullJsonDataObj = fullJsonDataObj;
        this.layoutPosition = layoutPosition;
        this.messageText = messageText;
        this.messageChatID = messageChatID;
    }

    public void sendReportToServer() {
        value = getPurchaseReoprt();
        if (value.length() > 0)
            try {
                misc = this.context.getSharedPreferences("MISC_PUR_SERVICE", Context.MODE_PRIVATE).getString("VALUE_SERVICE", "");
                //sendPostRequest(false);
                sendPostRequest();
            } catch (Exception e) {
                e.printStackTrace();
            }

    }

    private String getPurchaseReoprt() {
        String sendStringToServer = "";
        try {
            JSONObject objectPurchaseData = new JSONObject(purchaseData);
            String orderId = objectPurchaseData.getString("orderId");
            String productId = getProductIdSendAstroSageServer(objectPurchaseData.getString("productId"));
            String purchaseToken = objectPurchaseData.getString("purchaseToken");
            JSONObject object = new JSONObject();
            object.accumulate("orderId", orderId);
            object.accumulate("productId", productId);
            //object.accumulate("emailId", this.emailId);
            /*if(this.userId.equals("")){
                this.userId = UserEmailFetcher.getEmail(context);
            }*/
            //object.accumulate("userId", this.userId);
            object.accumulate("platform", "mobile");
            object.accumulate("purchaseToken", purchaseToken);
            object.accumulate("key", CUtils.getProductPurchaseSecurityKey(orderId));
            object.accumulate("signed_key", CUtils.getApplicationSignatureHashCode(context));
            object.accumulate("device_id", CUtils.getMyAndroidId(context));
            object.accumulate("price", price);
            object.accumulate("priceCurrencycode", priceCurrencycode);
            object.accumulate("source_of_code", "AstroShop");

            sendStringToServer = object.toString();
        } catch (Exception e) {
            //Log.e("ex", "fdf");
        }
        ////Log.e("sendStringToServer",sendStringToServer);
        return sendStringToServer;
    }

    private String getProductIdSendAstroSageServer(String productId) {
        String astroSageProductId = "";

    /*    if (productId.equalsIgnoreCase(FragAstrologerQuery.SKU_SILVER_PLAN))
            astroSageProductId = CGlobalVariables.ASTROSAGE_SILVER_PLAN_ID*//*ASTROSAGE SERVICE ID*//*;
        if (productId.equalsIgnoreCase(FragAstrologerQuery.SKU_GOLD_PLAN))
            astroSageProductId = CGlobalVariables.ASTROSAGE_GOLD_PLAN_ID*//*ASTROSAGE SERVICE ID*//*;*/
        if (productId.equalsIgnoreCase(CGlobalVariables.ASK_QUESTION_PLAN_VALUE))
            astroSageProductId = CGlobalVariables.ASTROSAGE_ASK_QUESTION_PLAN_ID/*ASTROSAGE SERVICE ID*/;

        //productId
        return astroSageProductId;
    }

    private void updateQuestion(String paymentStatus) {
        Intent intent = new Intent((context), SaveDataInternalStorage.class);
        //intent.putParcelableArrayListExtra(CGlobalVariables.CHATWITHASTROLOGER, chatMessageArrayList);
        intent.putExtra(CGlobalVariables.ISINSERT, false);
        intent.putExtra("layoutPositon", "" + layoutPosition);
        intent.putExtra("chatid", chatid);
        intent.putExtra("paymentStatus", "" + paymentStatus);
        (context).startService(intent);
    }

    public void consumePurchase(String purchaseData) {

        try {
            //Log.e("Going to Consume Data", "!!!!!!!!!!!!!!!!");

            JSONObject objectPurchaseData = new JSONObject(purchaseData);
            String purchaseToken = objectPurchaseData.getString("purchaseToken");
            // boolean purchaseConsume = false;
            ConsumeParams consumeParams =
                    ConsumeParams.newBuilder()
                            .setPurchaseToken(purchaseToken)
                            .build();

            ConsumeResponseListener listener = new ConsumeResponseListener() {
                @Override
                public void onConsumeResponse(BillingResult billingResult, String purchaseToken) {
                    if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
                        Log.e("BillingClient", "onBillingSetupFinished() OK");
                        clearDataInPreferences();
                    } else {
                        Log.e("BillingClient", "onBillingSetupFinished() FAIL");
                    }
                }
            };

            AstrosageKundliApplication.billingClient.consumeAsync(consumeParams, listener);
        } catch (Exception e) {

        }
    }

    //Clear Purchase plan info
    private void clearDataInPreferences() {
        SharedPreferences purchasageDataPlan = context.getSharedPreferences(CGlobalVariables.ASTROSAGEPURCHASEPLANFORSERVICE, Context.MODE_PRIVATE);
        SharedPreferences.Editor purchasePlanEditor = purchasageDataPlan.edit();
        AppPurchaseDataSaveModelClass appPurchaseDataSaveModelClass = CUtils.getObjectSavePref("", "", "", "", "", "", "", "", "", "");
        Gson gson = new Gson();
        String purchaseDataJSONObject = gson.toJson(appPurchaseDataSaveModelClass); // myObject - instance of MyObject
        purchasePlanEditor.putString(CGlobalVariables.ASTROSAGEPURCHASEPLANFORSERVICEOBJECT, purchaseDataJSONObject);
        purchasePlanEditor.commit();
        context.getSharedPreferences("MISC_PUR_SERVICE", Context.MODE_PRIVATE).edit()
                .putString("VALUE_SERVICE", "").commit();
    }

    private void sendGoogleAnalytics(String data) {
        try {
            JSONObject objectPurchaseData = new JSONObject(data);
            String orderId = objectPurchaseData.getString("orderId");
            String productId = objectPurchaseData.getString("productId");
            String productName = "";
            if (productId.equalsIgnoreCase(CGlobalVariables.ASK_QUESTION_PLAN_VALUE)) {
                productName = CGlobalVariables.ASK_QUESTION_PLAN_VALUE;
            }
            onPurchaseCompleted(productId, productName, price, orderId, priceCurrencycode);
        } catch (Exception ex) {
            //Log.i("Error : ", ex.getMessage());
        }

    }

    public void onPurchaseCompleted(String productId, String ProductName, String productCost, String order_id, String priceCurrencycode) {


        //double price = (Double.valueOf(productCost) / 1000000);
        CUtils.trackEcommerceProduct(context, productId, ProductName, productCost, order_id, priceCurrencycode, "In-App Purchase", "In-App Store", "0", CGlobalVariables.TOPIC_ASKAQUESTION);

    }

    private void sendPostRequest() {
        CUtils.vollyPostRequest(this, requestURL, getParamsNew(), 0);
    }

    private Map<String, String> getParamsNew() {
        String paymentstatus = "" + CUtils.getBooleanData(context, CGlobalVariables.PAYMENTDONE_KEY_FOR_ASTRO_SERVICES_ISPAYMENTDONE, true);
        boolean isInsert = CUtils.getBooleanData(context, CGlobalVariables.Type_PAYMENT, false);
        String paidNotPaid = "update";
        if (!isInsert) {
            paidNotPaid = "insert";
        }
        if (messageChatID == null) {
            messageChatID = "";
        }
        HashMap<String, String> params = new HashMap<>();
        params.put(key, value);
        params.put("jsonInputOther", misc);
        params.put(key2, fullJsonDataObj);
        params.put(key3, paymentstatus);
        params.put(key4, appVerName);
        params.put(key5, paidNotPaid);
        params.put(questionOfMessage, messageText);
        params.put(chatid, messageChatID);

        return params;
    }


    @Override
    public void onResponse(String response, int method) {
        try {
            JSONArray jsonArray = new JSONArray(response);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String successResult = jsonObject.getString("Result");
            if (successResult.equalsIgnoreCase("1")) {
                // if(CUtils.getBooleanData(context, CGlobalVariables.ISGOOLGEVERIFY,true)) {
                //Track User
                sendGoogleAnalytics(purchaseData);
                consumePurchase(purchaseData);
                updateQuestion("False");
                // }
            } else if (successResult.equalsIgnoreCase("0")) {
                //check 3 times
                int status = Integer.parseInt(CUtils.getStringData(context, CGlobalVariables.CHECK_INAPP_PURCHASE, "1"));
                if (status > 3) {
                    sendGoogleAnalytics(purchaseData);
                    consumePurchase(purchaseData);
                    CUtils.saveStringData(context, CGlobalVariables.CHECK_INAPP_PURCHASE, "1");
                } else {
                    status = status + 1;
                    CUtils.saveStringData(context, CGlobalVariables.CHECK_INAPP_PURCHASE, String.valueOf(status));
                }
                updateQuestion("False");
            } else {
                //for result 2 and 3
                //CUtils.saveBooleanData(context,CGlobalVariables.ISGOOLGEVERIFY,false);
                //if(successResult.equalsIgnoreCase("InvalidOperation")
                clearDataInPreferences();
                updateQuestion("True");
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onError(VolleyError error) {
        Log.i("", "");
    }

}
