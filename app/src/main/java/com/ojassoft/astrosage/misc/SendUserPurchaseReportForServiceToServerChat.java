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
import com.libojassoft.android.misc.VolleyResponse;
import com.libojassoft.android.utils.LibCUtils;
import com.ojassoft.astrosage.model.AppPurchaseDataSaveModelClass;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.fragments.astrochatfragment.savedata.SaveDataInternalStorage;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ojas-20 on 10/2/17.
 */

public class SendUserPurchaseReportForServiceToServerChat implements VolleyResponse {


    //String requestURL = "https://inappverify.astrosage.com/inapppurchaseverificationv6",
    String requestURL = "https://inappverify.astrosage.com/inapppurchaseverificationv11",
    // String requestURL ="http://192.168.1.128:8880/inapppurchaseverification",

    key = "jsonInput", value, purchaseData, emailId, userId, misc, price = "0", priceCurrencycode = "INR", fullJsonDataObj = "", key2 = "jsonInputForService", key3 = "isPaymentDonestatus",
            key4 = "appVerName", layoutPosition = "", key5 = "type", questionOfMessage = "problem", chatid = "chatid", orderId = "";
    Context context;
    String appVerName, messageText, messageChatID;
    String orderIdNew="";
    boolean isFree;

    public SendUserPurchaseReportForServiceToServerChat(Context context, String purchaseData, String emailId, String userId, String price, String priceCurrencycode, String fullJsonDataObj, String layoutPosition, String messageText, String messageChatID, String orderId) {
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
        this.orderIdNew = orderId;
    }

    public void sendReportToServer() {
        Log.e("BillingClient", "SendPurchaseReport SendReportToServerAsyn() enter");
        value = getPurchaseReoprt();
        if (value.length() > 0)
            try {
                misc = this.context.getSharedPreferences("MISC_PUR_SERVICE", Context.MODE_PRIVATE).getString("VALUE_SERVICE", "");
                sendPostRequest(false);
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
            object.accumulate(CGlobalVariables.KEY_EMAIL_ID, CUtils.replaceEmailChar(this.emailId));
            object.accumulate(CGlobalVariables.KEY_USER_ID, CUtils.replaceEmailChar(this.userId));
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
        //added by Ankit on 7/3/2019
        intent.putExtra("orderid", orderId);
        intent.putExtra("paymentStatus", "" + paymentStatus);
        (context).startService(intent);
    }

    public void consumePurchase(String purchaseData) {

        try {

            JSONObject objectPurchaseData = new JSONObject(purchaseData);
            String purchaseToken = objectPurchaseData.getString("purchaseToken");

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

    private void sendPostRequest(Boolean isFree) {
        this.isFree = isFree;
        CUtils.vollyPostRequest(this, requestURL, getParamsNew(), 0);
    }

    private Map<String, String> getParamsNew() {
        appVerName = LibCUtils.getApplicationVersionToShow(context);
        String paymentstatus = "" + CUtils.getBooleanData(context, CGlobalVariables.PAYMENTDONE_KEY_FOR_ASTRO_SERVICES_ISPAYMENTDONE, true);
        boolean isInsert = CUtils.getBooleanData(context, CGlobalVariables.Type_PAYMENT, false);
        String paidNotPaid = "update";
        if (!isInsert) {
            paidNotPaid = "insert";
        }
        if (messageChatID == null) {
            messageChatID = "";
        }
        try {
            int priceNow = 0;
            priceNow = Integer.parseInt(price);
            if (priceNow != 0) {
                priceNow = priceNow / 1000000;
                price = Integer.toString(priceNow);
            } else {
                price = "0";
            }
        } catch (Exception e) {
            Log.e("Prcie Conv:", "");
        }

        if(orderIdNew == null) orderIdNew = "";
        if(messageChatID == null) messageChatID = "";

        HashMap<String, String> params = new HashMap<>();
        params.put(key, value);
        params.put("jsonInputOther", misc);
        params.put(key2, fullJsonDataObj);
        params.put(key3, paymentstatus);
        params.put(key4, appVerName);
        params.put(key5, paidNotPaid);
        params.put(questionOfMessage, messageText);
        params.put(chatid, messageChatID);
        params.put("orderid", orderIdNew);
        params.put("payMode", "Google");
        params.put("price", price);
        params.put("androidid", CUtils.getMyAndroidId(context));
        params.put("ordersource", "astroSage_Kundli");

        Log.e("BillingClient", "params =" + params);

        return params;
    }

    @Override
    public void onResponse(String response, int method) {
        try {
            Log.e("BillingClient", "response =" + response);
            JSONArray jsonArray = new JSONArray(response);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String successResult = jsonObject.getString("Result");
            if (successResult.equalsIgnoreCase("1")) {
                if (jsonObject.has("ChatId")) {
                    chatid = jsonObject.getString("ChatId");
                    orderId = jsonObject.getString("orderId");
                }
                if (isFree) {
                    updateQuestion("False");
                    return;
                }
                sendGoogleAnalytics(purchaseData);
                updateQuestion("False");
                consumePurchase(purchaseData);
                clearDataInPreferences();
            } else if (successResult.equalsIgnoreCase("0") || successResult.equalsIgnoreCase("2")) {
                if (jsonObject.has("ChatId")) {
                    chatid = jsonObject.getString("ChatId");
                }
                updateQuestion("True");
                consumePurchase(purchaseData);
                clearDataInPreferences();
            } else if (successResult.equalsIgnoreCase("3")) {
                if (jsonObject.has("ChatId")) {
                    chatid = jsonObject.getString("ChatId");
                }
                updateQuestion("True");
                consumePurchase(purchaseData);
                clearDataInPreferences();
            } else {
                //for result 2 and 3
                //CUtils.saveBooleanData(context,CGlobalVariables.ISGOOLGEVERIFY,false);
                //if(successResult.equalsIgnoreCase("InvalidOperation")
                //sendGoogleAnalytics(purchaseData);
                consumePurchase(purchaseData);
                clearDataInPreferences();
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onError(VolleyError error) {
        if (error == null) {
            Log.e("BillingClient", "error is null");
        } else {
            Log.e("BillingClient", "error =" + error.toString());
        }
    }
}