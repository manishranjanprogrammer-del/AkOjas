package com.ojassoft.astrosage.misc;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.RemoteException;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.PurchasesResponseListener;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.common.collect.ImmutableList;
import com.libojassoft.android.misc.VolleyResponse;
import com.ojassoft.astrosage.model.GmailAccountInfo;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.UserEmailFetcher;
import com.ojassoft.astrosage.varta.volley_network.VolleyServiceHandler;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

////import com.google.analytics.tracking.android.Log;

public class ServiceToGetPurchasePlanDetails extends IntentService implements VolleyResponse, com.ojassoft.astrosage.varta.volley_network.VolleyResponse {

    Intent intent;
    String signature = "signatue", purchaseData;
    String orderID = "";
    String deviceID = "";
    int SEND_REPORT_TO_SERVER = 0;
    int VERIFY_LOGIN = 1;
    ArrayList<ProductDetails> productList;
    private String astroSageUserId;
    private String price;
    private String priceCurrencycode;
    private String formattedPrice;
    public static int GET_SERVICE_DETAILS = 3001;
    public ServiceToGetPurchasePlanDetails() {
        super("ServiceToGetPurchasePlanDetails");
    }

    public ServiceToGetPurchasePlanDetails(Context context) {
        super("ServiceToGetPurchasePlanDetails");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // TODO Auto-generated method stub
        try {
            fetchProductFromGoogleServer();

        } catch (RemoteException e) {
            e.printStackTrace();
        }
        this.intent = intent;

        if (!CUtils.isUserLogedIn(this)) {
            return;
        }

        String _userId = CUtils.getUserName(ServiceToGetPurchasePlanDetails.this);
        String _pwd = CUtils.getUserPassword(ServiceToGetPurchasePlanDetails.this);
        String userName = CUtils.getUserFirstName(ServiceToGetPurchasePlanDetails.this);

        //Log.e("LOGIN DETAIL ", _userId + "  "+ _pwd + "  "+ CUtils.getUserFirstName(ServiceToGetPurchasePlanDetails.this));

        try {

            CUtils.verifyLoginWithUserPurchasedPlan(ServiceToGetPurchasePlanDetails.this, _userId,
                    _pwd, userName, CGlobalVariables.OPERATION_NAME_LOGIN,
                    CGlobalVariables.REG_SOURCE_ANDROID, VERIFY_LOGIN);

        } catch (Exception e) {

        }
    }

    private void saveRecords(String planId, String expiryDate, String purchasedDate) {

        CUtils.storeUserPurchasedPlanInPreference(ServiceToGetPurchasePlanDetails.this, Integer.parseInt(planId));
        CUtils.saveStringData(ServiceToGetPurchasePlanDetails.this, CGlobalVariables.PREF_USER_PLAN_PURCHASE_DATE, purchasedDate);//Purchase plan Date
        CUtils.saveStringData(ServiceToGetPurchasePlanDetails.this, CGlobalVariables.PREF_USER_PLAN_Expiry_DATE, expiryDate);//Expiry plan  Date
        checkExpireDate();
    }

    private void checkExpireDate() {
        try {

            if (AstrosageKundliApplication.billingClient != null) {
                AstrosageKundliApplication.billingClient.queryPurchasesAsync(BillingClient.SkuType.SUBS, new PurchasesResponseListener() {

                    @Override
                    public void onQueryPurchasesResponse(@NonNull BillingResult billingResult, @NonNull List<Purchase> list) {
                        if (billingResult.getResponseCode() != BillingClient.BillingResponseCode.OK) {
                            //Log.e("BillingClient", "onBillingSetupFinished() FAIL");
                            return;
                        }

                        Purchase purchase = list.get(0);
                        String thisResponse = purchase.getOriginalJson();
                        String purchaseState = "";
                        JSONObject object = null;
                        try {
                            object = new JSONObject(thisResponse);
                            int purchasePlanId = CUtils.getUserPurchasedPlanFromPreference(ServiceToGetPurchasePlanDetails.this);
                            purchaseState = object.getString("purchaseState");
                            if (purchaseState.trim().equalsIgnoreCase("2")) {
                                //here api to refund ammount we should cancel his/her subscription
                                // startService(new Intent(ServiceToGetPurchasePlanDetails.this,CancelOrderPlan.class));

                                if (object.has("orderId")) {
                                    orderID = object.getString("orderId");
                                }
                                deviceID = CUtils.getMyAndroidId(getApplicationContext());
                                //new SendReportToServerAsyn().execute();
                                String url = CGlobalVariables.CANCEL_CLOUD_PLAN_URL;
                                CUtils.vollyPostRequest(ServiceToGetPurchasePlanDetails.this, url, getParamsNew(), SEND_REPORT_TO_SERVER);

                            } else if (purchasePlanId == CGlobalVariables.BASIC_PLAN_ID) {
                                String product_ID = object.getString("productId");
                                if ((product_ID.equalsIgnoreCase(CGlobalVariables.SKU_GOLD_PLAN_MONTH) && (product_ID.equalsIgnoreCase(CGlobalVariables.SKU_GOLD_PLAN_YEAR)))) {
                                    CUtils.storeUserPurchasedPlanInPreference(ServiceToGetPurchasePlanDetails.this, CGlobalVariables.GOLD_PLAN_ID);
                                } else if ((product_ID.equalsIgnoreCase(CGlobalVariables.SKU_SILVER_PLAN_YEAR) && (product_ID.equalsIgnoreCase(CGlobalVariables.SKU_SILVER_PLAN_MONTH)))) {
                                    CUtils.storeUserPurchasedPlanInPreference(ServiceToGetPurchasePlanDetails.this, CGlobalVariables.SILVER_PLAN_ID);
                                } else if ((product_ID.equalsIgnoreCase(CGlobalVariables.SKU_PLATINUM_PLAN_YEAR) && (product_ID.equalsIgnoreCase(CGlobalVariables.SKU_PLATINUM_PLAN_MONTH)))) {
                                    CUtils.storeUserPurchasedPlanInPreference(ServiceToGetPurchasePlanDetails.this, CGlobalVariables.PLATINUM_PLAN_ID);
                                }
                                try {
                                    signature = purchase.getSignature();
                                } catch (Exception e) {
                                    //android.util.//Log.e("EXCEPTION", "checkExpireDate: " + e.getMessage());
                                }

                                try {
                                    purchaseData = thisResponse;
                                    astroSageUserId = CUtils.getUserName(ServiceToGetPurchasePlanDetails.this);
                                    verifyPurchaseFromService(product_ID);
                                } catch (Exception e) {
                                    //android.util.//Log.e("Exception", "checkExpireDate: " + e.getMessage());
                                }

                            }
                        } catch (Exception e) {
                            //
                        }
                    }
                });
            }

        } catch (Exception e) {
            //android.util.//Log.e("Exception", "checkExpireDate: " + e.getMessage());
        }
    }

    private void verifyPurchaseFromService(String productID) {

        //   astroSageUserId = CUtils.getUserName(ThanksProductPurchaseScreen.this);
        getSharedPreferences("MISC_PUR", Context.MODE_PRIVATE).edit()
                .putString("VALUE", purchaseData).commit();// ADDED BY BIJENDRA
        Intent pvsIntent = new Intent(getApplicationContext(),
                PurchaseVerificationService.class);
        pvsIntent.putExtra("SIGNATURE", signature);
        pvsIntent.putExtra("PURCHASE_DATA", purchaseData);
        pvsIntent.putExtra("ASTRO_USERID", astroSageUserId);
        if (productList != null) {
            for (int i = 0; i < productList.size(); i++) {
                ProductDetails skuDetails = productList.get(i);
                if (skuDetails.getProductId().equalsIgnoreCase(productID)) {
                    // //Log.e("SKU_GOLD_PLAN_YEAR",object.toString() );
                    ProductDetails.PricingPhase priceDetails = skuDetails.getSubscriptionOfferDetails().get(0).getPricingPhases().getPricingPhaseList().get(0);
                    price = priceDetails.getPriceAmountMicros() + "";
                    priceCurrencycode = priceDetails.getPriceCurrencyCode();
                    formattedPrice = priceDetails.getFormattedPrice();
                }
            }
        }
        pvsIntent.putExtra("price", price);
        pvsIntent.putExtra("priceCurrencycode", priceCurrencycode);
        pvsIntent.putExtra("formattedPrice", formattedPrice);
        pvsIntent.putExtra("source", "CheckExpireDateService");
        startService(pvsIntent);

    }

    /**
     * @description This method is used to save preference, that describes this service already done its work, and no need to launch again, until another plan is not activate
     */
    private void stopThisServiceToLuanchUntilAnotherplanIsNotActive() {
        CUtils.saveBooleanData(ServiceToGetPurchasePlanDetails.this, CGlobalVariables.isNeedToRunServiceToGetPurchasePlanDetails, false);
    }

    private void cleanSharedPreference() {
        CUtils.saveLoginDetailInPrefs(this, "", "", "", false, false);
        CUtils.saveStringData(this, CGlobalVariables.PREF_USER_PLAN_PURCHASE_DATE, "");//Purchase plan Date
        CUtils.saveStringData(this, CGlobalVariables.PREF_USER_PLAN_Expiry_DATE, "");
        CUtils.storeUserPurchasedPlanInPreference(ServiceToGetPurchasePlanDetails.this, 0);
        GmailAccountInfo gmailAccountInfo = new GmailAccountInfo();
        gmailAccountInfo.setId("");
        gmailAccountInfo.setUserName("");
        gmailAccountInfo.setMobileNo("");
        gmailAccountInfo.setHeading("");
        gmailAccountInfo.setAddress1("");
        gmailAccountInfo.setAddress2("");
        gmailAccountInfo.setMaritalStatus(0);
        gmailAccountInfo.setOccupation(0);
        CUtils.saveGmailAccountInfo(this, gmailAccountInfo);
        //   ((ActAppModule) context).drawerFragment.usersignoutFuntionality();
        sendLogoutResult(true);
    }

    /*@author Tejinder Singh
     * This method added here for more better useexperience
     * So when User click on Purchase Plan go direct to
     * purchase instead of take data first from console; */
    private void fetchProductFromGoogleServer() throws RemoteException {
        try {
            if (productList != null && !productList.isEmpty()) {
                return;
            }

            QueryProductDetailsParams queryProductDetailsParams =
                    QueryProductDetailsParams.newBuilder().setProductList(
                            ImmutableList.of(

                                    QueryProductDetailsParams.Product.newBuilder()
                                            .setProductId(CGlobalVariables.SKU_SILVER_PLAN_YEAR)
                                            .setProductType(BillingClient.ProductType.SUBS)
                                            .build(),

                                    QueryProductDetailsParams.Product.newBuilder()
                                            .setProductId(CGlobalVariables.SKU_GOLD_PLAN_YEAR)
                                            .setProductType(BillingClient.ProductType.SUBS)
                                            .build(),

                                    QueryProductDetailsParams.Product.newBuilder()
                                            .setProductId(CGlobalVariables.SKU_SILVER_PLAN_MONTH)
                                            .setProductType(BillingClient.ProductType.SUBS)
                                            .build(),

                                    QueryProductDetailsParams.Product.newBuilder()
                                            .setProductId(CGlobalVariables.SKU_GOLD_PLAN_MONTH)
                                            .setProductType(BillingClient.ProductType.SUBS)
                                            .build(),

                                    QueryProductDetailsParams.Product.newBuilder()
                                            .setProductId(CGlobalVariables.SKU_PLATINUM_PLAN_YEAR)
                                            .setProductType(BillingClient.ProductType.SUBS)
                                            .build(),

                                    QueryProductDetailsParams.Product.newBuilder()
                                            .setProductId(CGlobalVariables.SKU_PLATINUM_PLAN_MONTH)
                                            .setProductType(BillingClient.ProductType.SUBS)
                                            .build(),

                                    QueryProductDetailsParams.Product.newBuilder()
                                            .setProductId(CGlobalVariables.SKU_PLATINUM_PLAN_MONTH_OMF)
                                            .setProductType(BillingClient.ProductType.SUBS)
                                            .build(),

                                    QueryProductDetailsParams.Product.newBuilder()
                                            .setProductId(CGlobalVariables.SKU_PLATINUM_PLAN_YEAR_OMF)
                                            .setProductType(BillingClient.ProductType.SUBS)
                                            .build()
                            )).build();

            AstrosageKundliApplication.billingClient.queryProductDetailsAsync(
                    queryProductDetailsParams, (billingResult, productDetailsList) -> {
                        int response = billingResult.getResponseCode();
                        Log.e("BillingClient", "onProductDetailsResponse() response=" + response);
                        if (response == BillingClient.BillingResponseCode.OK) {
                            productList = (ArrayList<ProductDetails>) productDetailsList;
                            CUtils.setCloudPlanList(productList);
                            CUtils.setDhruvPlanList(productList);
                            Log.e("BillingClient", "onProductDetailsResponse() OK" + productList.size());
                        }
                    }
            );
            getAIKundliPlusPlanServices();
        } catch (Exception e) {
            //android.util.//Log.e("Excption", "fetchProductFromGoogleServer: "+e.getMessage() );
        }
    }
    /**
     * Fetches the available Kundli AI Plus plan services from the server.
     * Shows a progress bar and makes a network request to retrieve plan data.
     * Handles no-internet scenario with a Snackbar.
     */
    RequestQueue queue;
    private void getAIKundliPlusPlanServices() {
        if (!com.ojassoft.astrosage.varta.utils.CUtils.isConnectedWithInternet(this)) {
            //
        } else {
            queue = VolleySingleton.getInstance(this).getRequestQueue();
            String url = com.ojassoft.astrosage.varta.utils.CGlobalVariables.SERVICE_LIST_KUNDLI_AI_PLANS;
            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, url,
                    ServiceToGetPurchasePlanDetails.this, false, CUtils.planDetailsRequestParams(this), GET_SERVICE_DETAILS).getMyStringRequest();
            stringRequest.setShouldCache(false);
            queue.add(stringRequest);
        }
    }
    @Override
    public void onResponse(String response, int method) {
        Log.d("LoginFlow123", " onResponse2: " + response);
        if (method == VERIFY_LOGIN) {

            String respCode = "";
            if (response != null && response.length() > 0) {
                try {
                    JSONObject respObj = new JSONObject(response);
                    respCode = respObj.getString("msgcode");


                /*2 and 60 for simple login
                  1 and 5 for first time login after plan purchased
                  22 for plan activated successfully
                  3 for use id exist but record not found in corresponding to passed device id*/

                    if (respCode.equals("2") || respCode.equals("60") ||
                            respCode.equals("1") || respCode.equals("5") ||
                            respCode.equals("22")) {
                        HashMap<String, String> jsonObjHash = CUtils.parseLoginSignupJson(respObj);
                        if (jsonObjHash != null && jsonObjHash.size() > 0) {
                            com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents("bg_astrosage_login_success", CGlobalVariables.ASTROSAGE_BACKGROUND_LOGIN, "");
                            String userPlanExpiryDate = "",
                                    userPlanPurchaseDate = "", userPlanId = "";

                            if (jsonObjHash.containsKey(CGlobalVariables.USERPLAN_ID)) ;
                            {
                                userPlanId = jsonObjHash.get(CGlobalVariables.USERPLAN_ID);
                            }
                            if (jsonObjHash.containsKey(CGlobalVariables.USER_PLAN_EXPIRY_DATE)) ;
                            {
                                userPlanExpiryDate = jsonObjHash.get(CGlobalVariables.USER_PLAN_EXPIRY_DATE);
                            }
                            if (jsonObjHash.containsKey(CGlobalVariables.USER_PLAN_PURCHASE_DATE)) ;
                            {
                                userPlanPurchaseDate = jsonObjHash.get(CGlobalVariables.USER_PLAN_PURCHASE_DATE);
                            }

                            saveRecords(userPlanId, userPlanExpiryDate, userPlanPurchaseDate);
                            CUtils.userPlanTopicsSubscribe(this);
                            if (jsonObjHash.containsKey(CGlobalVariables.IS_DHRUV_PLAN_AVAIL)) {
                                String isDhruvPlanAvail = jsonObjHash.get(CGlobalVariables.IS_DHRUV_PLAN_AVAIL);
                                sendFreeDhruvPlanAvailResult(isDhruvPlanAvail);
                            } else {
                                sendFreeDhruvPlanAvailResult("");
                            }

                        }
                    } else if (respCode.equals("3")) {
                        com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents("bg_astrosage_login_fail", CGlobalVariables.ASTROSAGE_BACKGROUND_LOGIN, "");

                        CUtils.saveBooleanData(AstrosageKundliApplication.getAppContext(), CGlobalVariables.needToSendDeviceIdForLogin, false);
                    } else {
                        com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents("bg_astrosage_login_fail", CGlobalVariables.ASTROSAGE_BACKGROUND_LOGIN, "");

                        cleanSharedPreference();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }
        if (method == GET_SERVICE_DETAILS) {
            try {
                JSONArray array = new JSONArray(response);
                JSONObject plusService = array.getJSONObject(0);//Kundli AI+ plan service
                JSONObject premiumService = array.getJSONObject(1); // Dhruv plan service
                com.ojassoft.astrosage.varta.utils.CUtils.setPlusPlanServiceDetail(plusService.toString());
                com.ojassoft.astrosage.varta.utils.CUtils.setPremiumPlanServiceDetail(premiumService.toString());

                Intent intent = new Intent(CGlobalVariables.SERVICE_DETAILS_BROADCAST);
                intent.putExtra("premiumPlanServiceDetails", premiumService.toString());
                intent.putExtra("plusPlanServiceDetails", plusService.toString());
                LocalBroadcastManager.getInstance(this).sendBroadcast(intent);

            } catch (Exception e) {
                Toast.makeText(this, "", Toast.LENGTH_LONG).show();
            }
        }

    }

    @Override
    public void onError(VolleyError error) {

    }

    public Map<String, String> getParamsNew() {
        Context context = AstrosageKundliApplication.getAppContext();
        String key = CUtils.getApplicationSignatureHashCode(context);
        String userid = UserEmailFetcher.getEmail(context);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("useridentity", userid);
        params.put("key", key);
        params.put("orderid", orderID);
        params.put("deviceid", deviceID);
        return params;

    }

    public void sendFreeDhruvPlanAvailResult(String isDhruvPlanAvail) {
        try {
            Context context = AstrosageKundliApplication.getAppContext();
            CUtils.saveStringData(context, CGlobalVariables.IS_DHRUV_PLAN_AVAIL, isDhruvPlanAvail);
            Intent intent = new Intent(CGlobalVariables.CHECK_FREE_DHRUV_PLAN_AVAIL_INTENT);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        } catch (Exception e) {
            //
        }
    }

    public void sendLogoutResult(boolean isLogout) {
        try {
            Context context = AstrosageKundliApplication.getAppContext();
            Intent intent = new Intent(CGlobalVariables.CHECK_LOGIN_INTENT);
            intent.putExtra(CGlobalVariables.IS_LOGOUT, isLogout);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        } catch (Exception e) {
            //
        }
    }
}
