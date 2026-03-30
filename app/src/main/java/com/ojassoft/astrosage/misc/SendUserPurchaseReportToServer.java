package com.ojassoft.astrosage.misc;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.util.Log;

import androidx.annotation.NonNull;

import com.android.billingclient.api.AcknowledgePurchaseParams;
import com.android.billingclient.api.AcknowledgePurchaseResponseListener;
import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ConsumeParams;
import com.android.billingclient.api.ConsumeResponseListener;
import com.android.billingclient.api.Purchase;
import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.libojassoft.android.misc.VolleyResponse;
import com.ojassoft.astrosage.model.AppPurchaseDataSaveModelClass;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.PurchasePlanHomeActivity;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class SendUserPurchaseReportToServer implements VolleyResponse {
    //String requestURL = "https://inappverify.astrosage.com/inapppurchaseverificationv9",
    //String requestURL = "http://192.168.1.151/ac/cloud-plan-verification/dump-data-buy-cloud-plan.asp",
    String requestURL = CGlobalVariables.DUMP_DATA_CLOUD_PLAN_URL,

    // String requestURL = "https://inappverify.astrosage.com/inapppurchaseverificationv5",
    //String requestURL ="http://192.168.1.45:8088/inapppurchaseverificationv5",
    /*"http://www.astrocamp.com/app/json/api-buy-cloud-plan.asp",*/
    key = "jsonInput", value, purchaseData, emailId, userId, misc, price = "0", priceCurrencycode = "INR", formattedPrice="", key4 = "apkversion",
            keyOnly = "key", planExpiryTime = "planExpiryTime", jsonInputOther = "jsonInputOther";
    Context context;

    PackageInfo pInfo;
    String app_version = "", freetrialperiod = "", source;
    boolean openForAIChat;

    public SendUserPurchaseReportToServer(Context context, String purchaseData, String emailId, String userId, String price, String priceCurrencycode, String freetrialperiod, boolean openForAIChat, String formattedPrice, String source) {
        this.context = context;
        this.purchaseData = purchaseData;
        this.emailId = emailId;
        this.userId = userId;
        this.price = price;
        this.priceCurrencycode = priceCurrencycode;
        this.freetrialperiod = freetrialperiod;
        this.openForAIChat = openForAIChat;
        this.source = source;
        try {

            pInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            app_version = pInfo.versionName;

        } catch (Exception e) {
            e.printStackTrace();
        }
        this.formattedPrice = formattedPrice;
    }

    public void sendReportToServer() {
        value = getPurchaseReoprt();
        if (value.length() > 0) {
            try {
                misc = this.context.getSharedPreferences("MISC_PUR", Context.MODE_PRIVATE).getString("VALUE", "");//ADDED BY BIJENDRA ON 16-06-15
                JSONObject objectPurchaseData = null;
                try {
                    if (misc != null && misc.trim().length() > 0) {
                        if (freetrialperiod != null && freetrialperiod.trim().length() > 0) {
                            objectPurchaseData = new JSONObject(purchaseData);
                            objectPurchaseData.accumulate(CGlobalVariables.IN_APP_freeTrialPeriod, freetrialperiod);

                            misc = objectPurchaseData.toString();
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                sendPostRequest();

            } catch (Exception e) {
                // TODO Auto-generated catch block
                sendBroadcasttothanksActivity();
                e.printStackTrace();
            }
        } else {
            sendBroadcasttothanksActivity();
        }

    }

    private String getPurchaseReoprt() {
        String sendStringToServer = "";
        try {

            //If permission of user id is not available then use
            if (emailId.equals("")) {
                String astroShopEmail = CUtils.getAstroshopUserEmail(context);
                String askAQuesEmail = CUtils.getStringData(context, CGlobalVariables.ASTROASKAQUESTIONUSEREMAIL, "");

                if (!askAQuesEmail.equals("")) {
                    emailId = askAQuesEmail;
                } else if (!astroShopEmail.isEmpty()) {
                    emailId = astroShopEmail;
                }
            }

            JSONObject objectPurchaseData = new JSONObject(purchaseData);
            String orderId = objectPurchaseData.getString("orderId");
            String productId = objectPurchaseData.getString("productId");
            String purchaseToken = objectPurchaseData.getString("purchaseToken");
            JSONObject object = new JSONObject();
            object.accumulate("orderId", orderId);
            object.accumulate("productId", productId);
            object.accumulate(CGlobalVariables.KEY_EMAIL_ID, CUtils.replaceEmailChar(this.emailId));
            /*if(this.userId.equals("")){
                this.userId = UserEmailFetcher.getEmail(context);
            }*/
            object.accumulate(CGlobalVariables.KEY_USER_ID, CUtils.replaceEmailChar(this.userId));
            object.accumulate("platform", "mobile");
            object.accumulate("purchaseToken", purchaseToken);
            object.accumulate("key", CUtils.getProductPurchaseSecurityKey(orderId));
            object.accumulate("signed_key", CUtils.getApplicationSignatureHashCode(context));
            object.accumulate("device_id", CUtils.getMyAndroidId(context));

            String askAQuesMobNo = CUtils.getStringData(context, CGlobalVariables.ASTROASKAQUESTIONUSERPHONENUMBER, "");
            object.accumulate("phoneNo", askAQuesMobNo);

            try {
                SharedPreferences sharedPreferencesForLang = context.getSharedPreferences(
                        CGlobalVariables.MYKUNDLI_LANGUAGE_PREFS_NAME,
                        Context.MODE_PRIVATE);
                int langaugeCode = sharedPreferencesForLang.getInt(
                        CGlobalVariables.APP_PREFS_AppLanguage, 0);
                object.accumulate("languageCode", langaugeCode);
            } catch (Exception ex) {
                object.accumulate("languageCode", 0);
            }
            object.accumulate("apkversion", app_version);
            sendStringToServer = object.toString();
        } catch (Exception e) {

        }
        //Log.e("verifyPurchase params1",sendStringToServer);
        return sendStringToServer;
    }

    /*private String getProductIdSendAstroSageServer(String productId) {
        String astroSageProductId = "";

        if (productId.equalsIgnoreCase(DialogPlanUpgradeNew.SKU_SILVER_PLAN))
            astroSageProductId = CGlobalVariables.ASTROSAGE_SILVER_PLAN_ID*//*ASTROSAGE SERVICE ID*//*;
        if (productId.equalsIgnoreCase(DialogPlanUpgradeNew.SKU_GOLD_PLAN))
            astroSageProductId = CGlobalVariables.ASTROSAGE_GOLD_PLAN_ID*//*ASTROSAGE SERVICE ID*//*;

        //productId
        return astroSageProductId;
    }*/

    /*private String sendPostRequest()
            throws IOException {
        InputStream inputStream = null;
        String result = "", key1 = "jsonInputOther";


        try {
            String appVerName = LibCUtils.getApplicationVersionToShow(context);
            URL url = new URL(requestURL);

            httpConn = (HttpURLConnection) url.openConnection();

            httpConn.setConnectTimeout(15000);
            httpConn.setRequestMethod("POST");
            httpConn.setReadTimeout(15000);

            httpConn.setUseCaches(false);

            httpConn.setDoInput(true); // true indicates the server returns			 					// response

            StringBuffer requestParams = new StringBuffer();
            httpConn.setDoOutput(true); // true indicates POST request

            requestParams.append(URLEncoder.encode(key, "UTF-8"));
            requestParams.append("=").append(URLEncoder.encode(value, "UTF-8"));
            requestParams.append("&");
            requestParams.append(URLEncoder.encode(key1, "UTF-8"));
            requestParams.append("=").append(URLEncoder.encode(misc, "UTF-8"));

            requestParams.append("&");
            requestParams.append(URLEncoder.encode(key4, "UTF-8"));
            requestParams.append("=").append(URLEncoder.encode(app_version, "UTF-8"));

            // sends POST data
            OutputStreamWriter writer = new OutputStreamWriter(
                    httpConn.getOutputStream());
            writer.write(requestParams.toString());
            writer.flush();
            writer.close();

            inputStream = httpConn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);//iso-8859-1
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            inputStream.close();
            result = sb.toString();
            JSONArray jsonArray = new JSONArray(result);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String successResult = jsonObject.getString("Result");
            if (successResult.equalsIgnoreCase("1"))//1 for successful and 5 if order id already exists
            {
                // if(CUtils.getBooleanData(context, CGlobalVariables.ISGOOLGEVERIFY,true)) {
                //Track User
                //consumePurchase(purchaseData);
                sendGoogleAnalytics(purchaseData);
                clearDataInPreferences();
                callPurchasePlanDetails();
                // }
            } else if (successResult.equalsIgnoreCase("0")) {
                clearDataInPreferences(); // by abhishek -- data clear while inappverify failed
            } else if (successResult.equalsIgnoreCase("InvalidOperation")) {
                //CUtils.saveBooleanData(context,CGlobalVariables.ISGOOLGEVERIFY,false);
                clearDataInPreferences();
            } else {
                clearDataInPreferences();
            }

        } catch (Exception ex) {
            //Log.i("Tag","1 - "+ex.getMessage().toString());
        } finally {
            if (httpConn != null) {
                httpConn.disconnect();
            }
        }

        return result;
    }*/
    private void sendBroadcasttothanksActivity() {
        try {
            if (CGlobalVariables.isFromPlanPurchaseActivity) {
                sendDataToActivity(CGlobalVariables.PLAN_NOT_VERIFY);
                CGlobalVariables.isFromPlanPurchaseActivity = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void sendDataToActivity(String verifyTxt) {
        Intent sendLevel = new Intent();
        sendLevel.setAction(CGlobalVariables.BROADCAST_PLAN_PURCHASE);
        sendLevel.putExtra(CGlobalVariables.KEY_PLAN_DATA, verifyTxt);
        this.context.sendBroadcast(sendLevel);
    }
    /*public void consumePurchase(String purchaseData) {

        try {
            JSONObject objectPurchaseData = new JSONObject(purchaseData);
            String purchaseToken = objectPurchaseData.getString("purchaseToken");
            // boolean purchaseConsume = false;
            try {
                int response = AstrosageKundliApplication.mservice.consumePurchase(3, context.getPackageName(), purchaseToken);
                ////Log.e("RESPONSE_CONSUME_PURCHASE",String.valueOf(response));
                if (response == 0) {
                    //purchaseConsume = true;
                    //CUtils.saveBooleanData(context, CGlobalVariables.ISGOOLGEVERIFY, false);
                    clearDataInPreferences();
                }


            } catch (Exception e) {
                // TODO Auto-generated catch block
                //Log.e("RESPONSE_ERROR_CONSUME1",e.getMessage());
            }
            //stopSelf();
            // if (purchaseConsume)
            //new SendReportToServerAsyn().execute();

        } catch (Exception e) {
            //Log.e("RESPONSE_ERROR_CONSUME2",e.getMessage());
        }
    }*/

    //Clear Purchase plan info
    private void clearDataInPreferences() {

        SharedPreferences purchasageDataPlan = context.getSharedPreferences(CGlobalVariables.ASTROSAGEPURCHASEPLAN, Context.MODE_PRIVATE);
        SharedPreferences.Editor purchasePlanEditor = purchasageDataPlan.edit();
        AppPurchaseDataSaveModelClass appPurchaseDataSaveModelClass = CUtils.getObjectSavePref("", "", "", "", "", "");
        Gson gson = new Gson();
        String purchaseDataJSONObject = gson.toJson(appPurchaseDataSaveModelClass); // myObject - instance of MyObject
        purchasePlanEditor.putString(CGlobalVariables.ASTROSAGEPURCHASEPLANOBJECT, purchaseDataJSONObject);
        purchasePlanEditor.putBoolean(CGlobalVariables.ACTIVITYUPDATEAFTERPLANPURCHASE, true);

        purchasePlanEditor.commit();


        context.getSharedPreferences("MISC_PUR", Context.MODE_PRIVATE).edit()
                .putString("VALUE", "").commit();

    }

    private void sendGoogleAnalytics(String data) {

        try {

            JSONObject objectPurchaseData = new JSONObject(data);
            String orderId = objectPurchaseData.getString("orderId");
            String productId = objectPurchaseData.getString("productId");

            String productName = "";
            //String price = "0";
            //String priceCurrencycode = "INR";
            if (productId.equalsIgnoreCase(PurchasePlanHomeActivity.SKU_GOLD_PLAN_YEAR)) {
                //price = arayGoldPlan.get(price_amount_micros);// 2
                // priceCurrencycode = arayGoldPlan.get(price_currency_code);
                productName = CGlobalVariables.GOLD_PLAN_VALUE_YEAR;
            } else if (productId.equalsIgnoreCase(PurchasePlanHomeActivity.SKU_GOLD_PLAN_MONTH)) {
                // price = araySilverPlan.get(price_amount_micros);// 2
                // priceCurrencycode = araySilverPlan.get(price_currency_code);
                CUtils.subscribeTopics("", CGlobalVariables.TOPIC_GOLDPLAN, context);
                productName = CGlobalVariables.GOLD_PLAN_VALUE_MONTH;
            } else if (productId.equalsIgnoreCase(PurchasePlanHomeActivity.SKU_SILVER_PLAN_YEAR)) {
                // price = araySilverPlan.get(price_amount_micros);// 2
                // priceCurrencycode = araySilverPlan.get(price_currency_code);
                CUtils.subscribeTopics("", CGlobalVariables.TOPIC_GOLDPLAN, context);
                productName = CGlobalVariables.SILVER_PLAN_VALUE_YEAR;
            } else if (productId.equalsIgnoreCase(PurchasePlanHomeActivity.SKU_SILVER_PLAN_MONTH)) {
                // price = araySilverPlan.get(price_amount_micros);// 2
                // priceCurrencycode = araySilverPlan.get(price_currency_code);
                productName = CGlobalVariables.SILVER_PLAN_VALUE_MONTH;
            } else if (productId.equalsIgnoreCase(PurchasePlanHomeActivity.SKU_PLATINUM_PLAN_YEAR)) {
                // price = araySilverPlan.get(price_amount_micros);// 2
                // priceCurrencycode = araySilverPlan.get(price_currency_code);
                productName = CGlobalVariables.PLATINUM_PLAN_VALUE_YEAR;
                CUtils.subscribeTopics("", CGlobalVariables.TOPIC_DHRUVPLAN, context);
            } else if (productId.equalsIgnoreCase(PurchasePlanHomeActivity.SKU_PLATINUM_PLAN_MONTH)) {
                // price = araySilverPlan.get(price_amount_micros);// 2
                // priceCurrencycode = araySilverPlan.get(price_currency_code);
                productName = CGlobalVariables.PLATINUM_PLAN_VALUE_MONTH;
                CUtils.subscribeTopics("", CGlobalVariables.TOPIC_DHRUVPLAN, context);
            }


            onPurchaseCompleted(productId, productName, price, orderId, priceCurrencycode);
        } catch (Exception ex) {
            //Log.i("Error : ",ex.getMessage().toString());
        }

    }

    public void onPurchaseCompleted(String productId, String ProductName, String productCost, String order_id, String priceCurrencycode) {
        //Log.e("Purchase plan Start","");

       /* Transaction myTrans = new Transaction.Builder(
                order_id, // (String) Transaction Id, should be unique.
                (long) (Integer.valueOf(productCost))) // (long) Order total (in micros)
                .setAffiliation("In-App Store") // (String) Affiliation
                .setTotalTaxInMicros((long) (0)) // (long) Total tax (in micros)
                .setShippingCostInMicros((long)(0)) // (long) Total shipping cost (in micros)
                .setCurrencyCode(priceCurrencycode)
                .build();

        myTrans.addItem(new Transaction.Item.Builder(
                productId, // (String) Product SKU
                ProductName, // (String) Product name
                (long) (Integer.valueOf(productCost)), // (long) Product price (in micros)
                (long) 1) // (long) Product quantity
                .setProductCategory("In-App Purchase") // (String) Product category
                .build());

        Tracker myTracker = EasyTracker.getTracker(); // Get reference to tracker.
        myTracker.sendTransaction(myTrans); // Send the transaction.*/

        //double price = (Double.valueOf(productCost) / 1000000);
        CUtils.trackEcommerceProduct(context, productId, ProductName, productCost, order_id, "INR", "In-App Purchase", "In-App Store", "0", CGlobalVariables.TOPIC_CLOUDPLAN);

        //Log.e("Purchase plan End","");

        //Log.e("Purchase plan End","");
    }

    private void callPurchasePlanDetails() {
        if (CUtils.isUserLogedIn(context)
                && CUtils.isConnectedWithInternet(context)) {
            Intent intent = new Intent(context, ServiceToGetPurchasePlanDetails.class);
            context.startService(intent);
           /* Intent intent = new Intent(ActAppModule.this,
                    ServiceToGetPurchasePlanDetails.class);
            startService(intent);*/
        }
    }

    private void sendPostRequest() {
        Log.d("BillingClient", "sendPostRequest() 1");
        CUtils.vollyPostRequest(this, requestURL, getParamsNew(), 0);
    }

    private Map<String, String> getParamsNew() {
        String phoneNum = CUtils.getStringData(context, CGlobalVariables.KEY_PREF_MOBILE_NO, "");
        String countryCode = CUtils.getStringData(context, CGlobalVariables.KEY_PREF_COUNTRY_CODE, "");
        boolean isPhoneVerified = CUtils.getBooleanData(context, CGlobalVariables.KEY_PREF_IS_MOBILE_VERIFY, false);
        if(source == null) source ="";
        HashMap<String, String> params = new HashMap<>();
        params.put(key, value);
        params.put(jsonInputOther, misc);
        params.put(key4, app_version);
        params.put(planExpiryTime, "");
        params.put(keyOnly, /*"897584714"*/CUtils.getApplicationSignatureHashCode(context));
        params.put("mobileno", phoneNum);
        params.put("countrycode", countryCode);
        params.put("mobileverified", isPhoneVerified?"1":"0");
        params.put("sourcce", source);
        params.put("priceAmountMicros", this.price);
        params.put("priceCurrencyCode", this.priceCurrencycode);
        params.put("formattedPrice", this.formattedPrice);
        //Log.e("BillingClient params2",params.toString());
        return params;
    }


    @Override
    public void onResponse(String response, int method) {
        Log.d("BillingClient", "onResponse="+response);
        try {
            JSONArray jsonArray = new JSONArray(response);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String successResult = jsonObject.getString("Result");
            if (successResult.equalsIgnoreCase("1") || successResult.equalsIgnoreCase("2"))//1 for successful and 2 if user not logged-in but sucessful
            {
                // if(CUtils.getBooleanData(context, CGlobalVariables.ISGOOLGEVERIFY,true)) {
                //Track User
                acknowledgePurchase(purchaseData);

                try {

                    if (CUtils.isUserLogedIn(this.context)) {
                        if (CGlobalVariables.isFromPlanPurchaseActivity) {
                            //Log.e("PLANIDD ",""+CGlobalVariables.PLAN_PURCHASE_ID);
                            CUtils.storeUserPurchasedPlanInPreference(this.context, CGlobalVariables.PLAN_PURCHASE_ID);
                            sendDataToActivity(CGlobalVariables.PLAN_VERIFY);
                            CGlobalVariables.isFromPlanPurchaseActivity = false;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                sendGoogleAnalytics(purchaseData);
                clearDataInPreferences();
                callPurchasePlanDetails();
                // }
            } else if (successResult.equalsIgnoreCase("0")) {
                clearDataInPreferences(); // by abhishek -- data clear while inappverify failed
            } else if (successResult.equalsIgnoreCase("InvalidOperation")) {
                //CUtils.saveBooleanData(context,CGlobalVariables.ISGOOLGEVERIFY,false);
                clearDataInPreferences();
            } else {
                clearDataInPreferences();
            }
        } catch (Exception e) {

        }
    }

    @Override
    public void onError(VolleyError error) {
        //Log.e("verifyPurchase error", error.toString());
    }


    public void acknowledgePurchase(String purchaseData) {

        try {
            Log.d("BillingClient", "acknowledgePurchase() purchaseData="+purchaseData);

            JSONObject objectPurchaseData = new JSONObject(purchaseData);
            String purchaseToken = objectPurchaseData.getString("purchaseToken");
            boolean isAcknowledged = objectPurchaseData.getBoolean("acknowledged");

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
                            clearDataInPreferences();
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

}