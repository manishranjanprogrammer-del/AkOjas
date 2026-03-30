package com.ojassoft.astrosage.ui.act;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AstroShopItemDetails;
import com.ojassoft.astrosage.beans.ItemOrderModel;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.utils.AvenuesParams;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.Constants;
import com.ojassoft.astrosage.utils.RSAUtility;
import com.ojassoft.astrosage.utils.ServiceUtility;

import org.apache.http.util.EncodingUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

//import com.google.analytics.tracking.android.EasyTracker;
//import com.google.analytics.tracking.android.Tracker;
//import com.google.analytics.tracking.android.Transaction;


public class ActWebViewActivity extends Activity {
    Intent mainIntent;
    String html, encVal;
    public AstroShopItemDetails itemdetails;
    public int noOfItem = 1;
    private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    private Typeface typeface;
    //  private static CustomProgressDialog pd = null;
    private ItemOrderModel orderModel;
    String currency = "INR";
    String amount = "0";
    private RequestQueue queue;
    MyCustomToast mct;
    private String payStatus = "";
    private String order_id = "";
    private String status = null;
    private Boolean isFromCart = false;
    private CustomProgressDialog pd = null;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_webview);
        LANGUAGE_CODE = ((AstrosageKundliApplication) ActWebViewActivity.this.getApplication())
                .getLanguageCode();

        typeface = CUtils.getRobotoFont(
                ActWebViewActivity.this, LANGUAGE_CODE, CGlobalVariables.regular);
        mainIntent = getIntent();
        //  pd = new CustomProgressDialog(ActWebViewActivity.this, typeface);
        init();
        // new RenderView().execute();
        getData();
    }

    /**
     * Async task class to get json by making HTTP call
     */

    /*private class RenderView extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
*//*if(pd!=null && pd.isShowing())
            pd.show();*//*
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Creating service handler class instance
            ServiceHandler sh = new ServiceHandler();
            // Making a request to url and getting response
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair(AvenuesParams.ACCESS_CODE, getResources().getString(R.string.access_code)));
            params.add(new BasicNameValuePair(AvenuesParams.ORDER_ID, mainIntent.getStringExtra(AvenuesParams.ORDER_ID)));

            String vResponse = sh.makeServiceCall(mainIntent.getStringExtra(AvenuesParams.RSA_KEY_URL), ServiceHandler.POST, params);
            Log.wtf("Response from RSA", vResponse);
            if (!ServiceUtility.chkNull(vResponse).equals("")
                    && ServiceUtility.chkNull(vResponse).toString().indexOf("ERROR") == -1) {
                StringBuffer vEncVal = new StringBuffer("");
                vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.CVV, mainIntent.getStringExtra(AvenuesParams.CVV)));
                vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.AMOUNT, mainIntent.getStringExtra(AvenuesParams.AMOUNT)));
                //  vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.AMOUNT,"1"));

                //Log.e("Total Amount is@#@#@#@",mainIntent.getStringExtra(AvenuesParams.AMOUNT));
                vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.CURRENCY, mainIntent.getStringExtra(AvenuesParams.CURRENCY)));
                vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.CARD_NUMBER, mainIntent.getStringExtra(AvenuesParams.CARD_NUMBER)));
                vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.CUSTOMER_IDENTIFIER, mainIntent.getStringExtra(AvenuesParams.CUSTOMER_IDENTIFIER)));
                vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.EXPIRY_YEAR, mainIntent.getStringExtra(AvenuesParams.EXPIRY_YEAR)));
                vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.EXPIRY_MONTH, mainIntent.getStringExtra(AvenuesParams.EXPIRY_MONTH)));
                encVal = RSAUtility.encrypt(vEncVal.substring(0, vEncVal.length() - 1), vResponse);
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
           *//* if (pd!=null && pd.isShowing())
                pd.dismiss();*//*
            @SuppressWarnings("unused")
            class MyJavaScriptInterface {
                @JavascriptInterface
                public void processHTML(String html) {
                    Log.wtf("Html returned from handler", html);

                    // process the html as needed by the app
                    if (html.indexOf("Failure") != -1) {
                        CUtils.googleAnalyticSendWitPlayServie(ActWebViewActivity.this, CGlobalVariables.GOOGLE_ANALYTIC_EVENT, CGlobalVariables.GOOGLE_ANALYTIC_LABEL_FOR_ASTROSHOP_PAYMENT_FAILED, null);
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_LABEL_FOR_ASTROSHOP_PAYMENT_FAILED, CGlobalVariables.FIREBASE_EVENT_PAYMENT_FAILED, "");

                        status = "Transaction Declined!";
                        payStatus = "0";
                    } else if (html.indexOf("Success") != -1) {
                        status = "Transaction Successful!";
                        payStatus = "1";
                        onPurchaseCompleted(itemdetails, orderModel, order_id);
                        double dPrice = 0.0;
                        String pricee = "";
                        try {
                            if(orderModel != null)
                            {
                                if(orderModel.getProductcost_inrs() != null && orderModel.getProductcost_inrs().length()>0)
                                {
                                    pricee = orderModel.getProductcost_inrs();
                                    dPrice = Double.valueOf(pricee);
                                }
                            }
                        } catch (NumberFormatException e) {
                            e.printStackTrace();
                        }
                        CUtils.googleAnalyticSendWitPlayServieForPurchased(ActWebViewActivity.this, CGlobalVariables.GOOGLE_ANALYTIC_PRODUCT,
                                CGlobalVariables.GOOGLE_ANALYTIC_LABEL_FOR_ASTROSHOP_PAYMENT_SUCCESS, null,dPrice);
                        if (isFromCart) {
                            CUtils.setCartProduct(ActWebViewActivity.this, "");

                        }
                    } else if (html.indexOf("Aborted") != -1) {
                        CUtils.googleAnalyticSendWitPlayServie(ActWebViewActivity.this, CGlobalVariables.GOOGLE_ANALYTIC_EVENT, CGlobalVariables.GOOGLE_ANALYTIC_LABEL_FOR_ASTROSHOP_PAYMENT_FAILED, null);
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_LABEL_FOR_ASTROSHOP_PAYMENT_FAILED, CGlobalVariables.FIREBASE_EVENT_PAYMENT_FAILED, "");

                        status = "Transaction Cancelled!";
                        payStatus = "2";

                    } else {
                        CUtils.googleAnalyticSendWitPlayServie(ActWebViewActivity.this, CGlobalVariables.GOOGLE_ANALYTIC_EVENT, CGlobalVariables.GOOGLE_ANALYTIC_LABEL_FOR_ASTROSHOP_PAYMENT_FAILED, null);
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_LABEL_FOR_ASTROSHOP_PAYMENT_FAILED, CGlobalVariables.FIREBASE_EVENT_PAYMENT_FAILED, "");

                        payStatus = "0";
                        //    status = "Status Not Known!";
                        status = "Transaction Declined!";

                    }

                    postPayStatus();
                    //Log.e("Status avenue", status);
                }
            }

            final WebView webview = (WebView) findViewById(R.id.webview);
            webview.getSettings().setJavaScriptEnabled(true);
            webview.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
            webview.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(webview, url);
                    Log.wtf("Url on finish", url);
                    if (pd != null && pd.isShowing()) {
                        pd.dismiss();
                    }
                    // if(url.indexOf("/ccavResponseHandler.jsp")!=-1){
                    if (url.indexOf("/ccavresponsehandler.aspx") != -1) {
                        webview.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
                        Log.wtf("Url after", "javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>')");
                        webview.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                    Toast.makeText(getApplicationContext(), "Oh no! " + description, Toast.LENGTH_SHORT).show();
                }
            });
            *//* An instance of this class will be registered as a JavaScript interface *//*
            StringBuffer params = new StringBuffer();
            params.append(ServiceUtility.addToPostParams(AvenuesParams.ACCESS_CODE, mainIntent.getStringExtra(AvenuesParams.ACCESS_CODE)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.MERCHANT_ID, getResources().getString(R.string.merchant_id)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.ORDER_ID, mainIntent.getStringExtra(AvenuesParams.ORDER_ID)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.REDIRECT_URL, mainIntent.getStringExtra(AvenuesParams.REDIRECT_URL)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.CANCEL_URL, mainIntent.getStringExtra(AvenuesParams.CANCEL_URL)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.LANGUAGE, "EN"));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_NAME, mainIntent.getStringExtra(AvenuesParams.BILLING_NAME)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_ADDRESS, mainIntent.getStringExtra(AvenuesParams.BILLING_ADDRESS)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_CITY, mainIntent.getStringExtra(AvenuesParams.BILLING_CITY)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_STATE, mainIntent.getStringExtra(AvenuesParams.BILLING_STATE)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_ZIP, mainIntent.getStringExtra(AvenuesParams.BILLING_ZIP)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_COUNTRY, mainIntent.getStringExtra(AvenuesParams.BILLING_COUNTRY)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_TEL, mainIntent.getStringExtra(AvenuesParams.BILLING_TEL)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_EMAIL, mainIntent.getStringExtra(AvenuesParams.BILLING_EMAIL)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.DELIVERY_NAME, mainIntent.getStringExtra(AvenuesParams.DELIVERY_NAME)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.DELIVERY_ADDRESS, mainIntent.getStringExtra(AvenuesParams.DELIVERY_ADDRESS)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.DELIVERY_CITY, mainIntent.getStringExtra(AvenuesParams.DELIVERY_CITY)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.DELIVERY_STATE, mainIntent.getStringExtra(AvenuesParams.DELIVERY_STATE)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.DELIVERY_ZIP, mainIntent.getStringExtra(AvenuesParams.DELIVERY_ZIP)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.DELIVERY_COUNTRY, mainIntent.getStringExtra(AvenuesParams.DELIVERY_COUNTRY)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.DELIVERY_TEL, mainIntent.getStringExtra(AvenuesParams.DELIVERY_TEL)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.MERCHANT_PARAM1, "additional Info."));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.MERCHANT_PARAM2, "additional Info."));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.MERCHANT_PARAM3, "additional Info."));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.MERCHANT_PARAM4, "additional Info."));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.PAYMENT_OPTION, mainIntent.getStringExtra(AvenuesParams.PAYMENT_OPTION)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.CARD_TYPE, mainIntent.getStringExtra(AvenuesParams.CARD_TYPE)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.CARD_NAME, mainIntent.getStringExtra(AvenuesParams.CARD_NAME)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.DATA_ACCEPTED_AT,*//*mainIntent.getStringExtra(AvenuesParams.DATA_ACCEPTED_AT)*//*"N"));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.ISSUING_BANK, mainIntent.getStringExtra(AvenuesParams.ISSUING_BANK)));
            if (encVal != null)
                params.append(ServiceUtility.addToPostParams(AvenuesParams.ENC_VAL, URLEncoder.encode(encVal)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.EMI_PLAN_ID, mainIntent.getStringExtra(AvenuesParams.EMI_PLAN_ID)));
            params.append(ServiceUtility.addToPostParams(AvenuesParams.EMI_TENURE_ID, mainIntent.getStringExtra(AvenuesParams.EMI_TENURE_ID)));

            ////////For Testing////////
            //	params.append(ServiceUtility.addToPostParams("data_accept","Y"));

            //data_accept
            if (mainIntent.getStringExtra(AvenuesParams.SAVE_CARD) != null)
                params.append(ServiceUtility.addToPostParams(AvenuesParams.SAVE_CARD, mainIntent.getStringExtra(AvenuesParams.SAVE_CARD)));
            String vPostParams = params.substring(0, params.length() - 1);
            try {
                webview.postUrl(Constants.TRANS_URL, EncodingUtils.getBytes(vPostParams, "UTF-8"));
            } catch (Exception e) {
                showToast("Exception occured while opening webview.");
            }
        }
    }*/
    public void showToast(String msg) {
        Toast.makeText(this, "Toast: " + msg, Toast.LENGTH_LONG).show();
    }

    private void init() {


        pd = new CustomProgressDialog(this, typeface);
        pd.setCancelable(false);
        pd.show();

        isFromCart = mainIntent.getBooleanExtra("fromCart", false);
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        mct = new MyCustomToast(this, this
                .getLayoutInflater(), this, typeface);
        if (!isFromCart) {
            itemdetails = new AstroShopItemDetails();
            noOfItem = mainIntent.getIntExtra("ItemNo", 0);
            itemdetails = (AstroShopItemDetails) mainIntent.getSerializableExtra("Key");
        }

        orderModel = (ItemOrderModel) mainIntent.getSerializableExtra("Order_Model");
        currency = mainIntent.getStringExtra(AvenuesParams.CURRENCY);
        amount = mainIntent.getStringExtra(AvenuesParams.AMOUNT);
        order_id = mainIntent.getStringExtra(AvenuesParams.ORDER_ID);

    }


    private void postPayStatus() {
        pd = new CustomProgressDialog(this, typeface);
        pd.setCancelable(false);
        pd.show();
// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, CGlobalVariables.sendPayStatus,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //com.google.analytics.tracking.android.//Log.e("PayPostResponse  +" + response.toString());
                        if (pd != null && pd.isShowing())
                            pd.dismiss();
                        try {
                            JSONArray array = new JSONArray(response);
                            JSONObject obj = array.getJSONObject(0);
                            String result = obj.getString("Result");
                            if (result.equalsIgnoreCase("1")) {

                                Intent intent = new Intent(ActWebViewActivity.this, ActPaymentStatus.class);
                                if (!isFromCart) {
                                    intent.putExtra("Key", itemdetails);

                                }
                                intent.putExtra(AvenuesParams.ORDER_ID, order_id);
                                intent.putExtra("Status", status);
                                intent.putExtra("Order_Model", orderModel);
                                intent.putExtra("fromCart", isFromCart);
                                startActivityForResult(intent, 1);

                                //Clear cart Product on successful payment
                                //  startActivity(intent);
                                // finish();
                            } else {
                                mct.show(getResources().getString(R.string.order_fail));

                            }
                        } catch (Exception e) {
                        }

                    }

                }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // //Log.e("Error Through" + error.getMessage());
                VolleyLog.d("VolleyError: " + error.getMessage());
                //   mTextView.setText("That didn't work!");
                if (error instanceof TimeoutError) {
                    VolleyLog.d("TimeoutError: " + error.getMessage());
                } else if (error instanceof NoConnectionError) {
                    VolleyLog.d("NoConnectionError: " + error.getMessage());
                } else if (error instanceof AuthFailureError) {
                    VolleyLog.d("AuthFailureError: " + error.getMessage());
                    //TODO
                } else if (error instanceof ServerError) {
                    //TODO
                    VolleyLog.d("ServerError: " + error.getMessage());
                } else if (error instanceof NetworkError) {
                    //TODO
                    VolleyLog.d("NetworkError: " + error.getMessage());
                } else if (error instanceof ParseError) {
                    //TODO
                    VolleyLog.d("ParseError: " + error.getMessage());

                }
                if (pd != null && pd.isShowing())
                    pd.dismiss();
            }

        }
        ) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded;charset=UTF-8";
            }

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                /*key,amount,orderid,paycurr,status*/
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("key", CUtils.getApplicationSignatureHashCode(ActWebViewActivity.this));
                headers.put("amount", amount);
                headers.put("orderid", order_id);
                headers.put("paycurr", currency);
                headers.put("status", payStatus);
                //com.google.analytics.tracking.android.//Log.e("Pay status Body==" + headers.toString());
                return headers;
            }

        };
        ;
// Add the request to the RequestQueue.
        //com.google.analytics.tracking.android.//Log.e("API HIT HERE");
        int socketTimeout = 30000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //Log.e("Result in webActivity", "" + requestCode + "," + resultCode);
        if (requestCode == 1 && resultCode == 1) {
            this.setResult(1);
            this.finish();
        } else if (requestCode == 1 && resultCode == 2) {
            this.setResult(2);
            this.finish();
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.setResult(2);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void onPurchaseCompleted(AstroShopItemDetails itemdetails, ItemOrderModel orderModel, String order_id) {
        //Log.e("Purchase plan Start","");
       /* Transaction myTrans = new Transaction.Builder(
                order_id,                                           // (String) Transaction Id, should be unique.
                (long) (Double.valueOf(orderModel.getProductcost_inrs()) * 1000000))                              // (long) Order total (in micros)
                .setAffiliation("In-App Store")                       // (String) Affiliation
                .setTotalTaxInMicros((long) (0))         // (long) Total tax (in micros)
                .setShippingCostInMicros((long)(Double.valueOf(orderModel.getShippingcost_in_rs())*1000000))
                .setCurrencyCode("INR")// (long) Total shipping cost (in micros)
                .build();

        if(isFromCart)
        {
            myTrans.addItem(new Transaction.Item.Builder(
                    "Astro Shop",                                              // (String) Product SKU
                    "Cart",                                  // (String) Product name
                    (long) (Double.valueOf(orderModel.getProductcost_inrs()) * 1000000),                              // (long) Product price (in micros)
                    (long) 1)                                             // (long) Product quantity
                    .setProductCategory("Astro Shop")
                    // (String) Product category
                    .build());
        }

        else
        {

            myTrans.addItem(new Transaction.Item.Builder(
                    itemdetails.getPId(),                                              // (String) Product SKU
                    itemdetails.getPName(),                                  // (String) Product name
                    (long) (Double.valueOf(orderModel.getProductcost_inrs()) * 1000000),                              // (long) Product price (in micros)
                    (long) 1)                                             // (long) Product quantity
                    .setProductCategory("Astro Shop")
                    // (String) Product category
                    .build());
        }

        Tracker myTracker = EasyTracker.getTracker(); // Get reference to tracker.
        myTracker.sendTransaction(myTrans); // Send the transaction.*/

        ////////////////////////////////// New

        //double price = (Double.valueOf(orderModel.getProductcost_inrs()));
        //double shipingCost = Double.valueOf(orderModel.getShippingcost_in_rs());

        if (isFromCart) {
            CUtils.trackEcommerceProduct(ActWebViewActivity.this, "Astro Shop", "Cart", orderModel.getProductcost_inrs(), order_id, "INR", "Astro Shop", "In-App Store", orderModel.getShippingcost_in_rs(), CGlobalVariables.TOPIC_PRODUCTS);
        } else {
            CUtils.trackEcommerceProduct(ActWebViewActivity.this, itemdetails.getPId(), itemdetails.getPName(), orderModel.getProductcost_inrs(), order_id, "INR", "Astro Shop", "In-App Store", orderModel.getShippingcost_in_rs(), CGlobalVariables.TOPIC_PRODUCTS);
        }


        //Log.e("Purchase plan End","");
    }

    @Override
    protected void onStart() {
        super.onStart();
        //EasyTracker.getInstance().activityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //EasyTracker.getInstance().activityStop(this);
    }

    /**
     * Render view
     */
    public void getData() {


        StringRequest stringRequest = new StringRequest(Request.Method.POST, mainIntent.getStringExtra(AvenuesParams.RSA_KEY_URL),
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        renderView(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // hideProgressBar();
                if (error instanceof TimeoutError) {
                    VolleyLog.d("TimeoutError: " + error.getMessage());
                } else if (error instanceof NoConnectionError) {
                    VolleyLog.d("NoConnectionError: " + error.getMessage());
                } else if (error instanceof AuthFailureError) {
                    VolleyLog.d("AuthFailureError: " + error.getMessage());
                } else if (error instanceof ServerError) {
                    VolleyLog.d("ServerError: " + error.getMessage());
                } else if (error instanceof NetworkError) {
                    VolleyLog.d("NetworkError: " + error.getMessage());
                } else if (error instanceof ParseError) {
                    VolleyLog.d("ParseError: " + error.getMessage());
                }
            }


        }


        ) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }


            @Override
            public Map<String, String> getParams() {

                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put(AvenuesParams.ACCESS_CODE, getResources().getString(R.string.access_code));
                headers.put(AvenuesParams.ORDER_ID, mainIntent.getStringExtra(AvenuesParams.ORDER_ID));

                return headers;
            }

            @Override
            public Map<String, String> getHeaders() {
                Map<String, String> headers = new HashMap<String, String>();
                headers.put("User-agent", "Mozilla/5.0 (Linux; U; Android-4.0.3; en-us; Galaxy Nexus Build/IML74K) AppleWebKit/535.7 (KHTML, like Gecko) CrMo/16.0.912.75 Mobile Safari/535.7");
                return headers;
            }

        };


        int socketTimeout = 60000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        stringRequest.setShouldCache(true);
        queue.add(stringRequest);
    }

    private void renderView(String response) {
        if (!ServiceUtility.chkNull(response).equals("")
                && ServiceUtility.chkNull(response).toString().indexOf("ERROR") == -1) {
            StringBuffer vEncVal = new StringBuffer("");
            vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.CVV, mainIntent.getStringExtra(AvenuesParams.CVV)));
            vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.AMOUNT, mainIntent.getStringExtra(AvenuesParams.AMOUNT)));
            //  vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.AMOUNT,"1"));

            //Log.e("Total Amount is@#@#@#@",mainIntent.getStringExtra(AvenuesParams.AMOUNT));
            vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.CURRENCY, mainIntent.getStringExtra(AvenuesParams.CURRENCY)));
            vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.CARD_NUMBER, mainIntent.getStringExtra(AvenuesParams.CARD_NUMBER)));
            vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.CUSTOMER_IDENTIFIER, mainIntent.getStringExtra(AvenuesParams.CUSTOMER_IDENTIFIER)));
            vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.EXPIRY_YEAR, mainIntent.getStringExtra(AvenuesParams.EXPIRY_YEAR)));
            vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.EXPIRY_MONTH, mainIntent.getStringExtra(AvenuesParams.EXPIRY_MONTH)));
            encVal = RSAUtility.encrypt(vEncVal.substring(0, vEncVal.length() - 1), response);
        }
        @SuppressWarnings("unused")
        class MyJavaScriptInterface {
            @JavascriptInterface
            public void processHTML(String html) {
                Log.wtf("Html returned from handler", html);

                // process the html as needed by the app
                if (html.indexOf("Failure") != -1) {
                    CUtils.googleAnalyticSendWitPlayServie(ActWebViewActivity.this, CGlobalVariables.GOOGLE_ANALYTIC_EVENT, CGlobalVariables.GOOGLE_ANALYTIC_LABEL_FOR_ASTROSHOP_PAYMENT_FAILED, null);
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_LABEL_FOR_ASTROSHOP_PAYMENT_FAILED, CGlobalVariables.FIREBASE_EVENT_PAYMENT_FAILED, "");

                    status = "Transaction Declined!";
                    payStatus = "0";
                } else if (html.indexOf("Success") != -1) {
                    status = "Transaction Successful!";
                    payStatus = "1";
                    onPurchaseCompleted(itemdetails, orderModel, order_id);
                    double dPrice = 0.0;
                    String pricee = "";
                    try {
                        if(orderModel != null)
                        {
                            if(orderModel.getProductcost_inrs() != null && orderModel.getProductcost_inrs().length()>0)
                            {
                                pricee = orderModel.getProductcost_inrs();
                                dPrice = Double.valueOf(pricee);
                            }
                        }
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    CUtils.googleAnalyticSendWitPlayServieForPurchased(ActWebViewActivity.this, CGlobalVariables.GOOGLE_ANALYTIC_PRODUCT,
                            CGlobalVariables.GOOGLE_ANALYTIC_LABEL_FOR_ASTROSHOP_PAYMENT_SUCCESS, null,dPrice,"");
                    if (isFromCart) {
                        CUtils.setCartProduct(ActWebViewActivity.this, "");

                    }
                } else if (html.indexOf("Aborted") != -1) {
                    CUtils.googleAnalyticSendWitPlayServie(ActWebViewActivity.this, CGlobalVariables.GOOGLE_ANALYTIC_EVENT, CGlobalVariables.GOOGLE_ANALYTIC_LABEL_FOR_ASTROSHOP_PAYMENT_FAILED, null);
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_LABEL_FOR_ASTROSHOP_PAYMENT_FAILED, CGlobalVariables.FIREBASE_EVENT_PAYMENT_FAILED, "");

                    status = "Transaction Cancelled!";
                    payStatus = "2";

                } else {
                    CUtils.googleAnalyticSendWitPlayServie(ActWebViewActivity.this, CGlobalVariables.GOOGLE_ANALYTIC_EVENT, CGlobalVariables.GOOGLE_ANALYTIC_LABEL_FOR_ASTROSHOP_PAYMENT_FAILED, null);
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_LABEL_FOR_ASTROSHOP_PAYMENT_FAILED, CGlobalVariables.FIREBASE_EVENT_PAYMENT_FAILED, "");

                    payStatus = "0";
                    //    status = "Status Not Known!";
                    status = "Transaction Declined!";

                }

                postPayStatus();
                //Log.e("Status avenue", status);
            }
        }

        final WebView webview = (WebView) findViewById(R.id.webview);
        webview.getSettings().setJavaScriptEnabled(true);
        webview.addJavascriptInterface(new MyJavaScriptInterface(), "HTMLOUT");
        webview.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(webview, url);
                Log.wtf("Url on finish", url);
                if (pd != null && pd.isShowing()) {
                    pd.dismiss();
                }
                // if(url.indexOf("/ccavResponseHandler.jsp")!=-1){
                if (url.indexOf("/ccavresponsehandler.aspx") != -1) {
                    webview.loadUrl("javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>');");
                    Log.wtf("Url after", "javascript:window.HTMLOUT.processHTML('<head>'+document.getElementsByTagName('html')[0].innerHTML+'</head>')");
                    webview.setVisibility(View.GONE);
                }
            }

            @Override
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                Toast.makeText(getApplicationContext(), "Oh no! " + description, Toast.LENGTH_SHORT).show();
            }
        });
        /* An instance of this class will be registered as a JavaScript interface */
        StringBuffer params = new StringBuffer();
        params.append(ServiceUtility.addToPostParams(AvenuesParams.ACCESS_CODE, mainIntent.getStringExtra(AvenuesParams.ACCESS_CODE)));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.MERCHANT_ID, getResources().getString(R.string.merchant_id)));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.ORDER_ID, mainIntent.getStringExtra(AvenuesParams.ORDER_ID)));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.REDIRECT_URL, mainIntent.getStringExtra(AvenuesParams.REDIRECT_URL)));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.CANCEL_URL, mainIntent.getStringExtra(AvenuesParams.CANCEL_URL)));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.LANGUAGE, "EN"));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_NAME, mainIntent.getStringExtra(AvenuesParams.BILLING_NAME)));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_ADDRESS, mainIntent.getStringExtra(AvenuesParams.BILLING_ADDRESS)));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_CITY, mainIntent.getStringExtra(AvenuesParams.BILLING_CITY)));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_STATE, mainIntent.getStringExtra(AvenuesParams.BILLING_STATE)));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_ZIP, mainIntent.getStringExtra(AvenuesParams.BILLING_ZIP)));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_COUNTRY, mainIntent.getStringExtra(AvenuesParams.BILLING_COUNTRY)));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_TEL, mainIntent.getStringExtra(AvenuesParams.BILLING_TEL)));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_EMAIL, mainIntent.getStringExtra(AvenuesParams.BILLING_EMAIL)));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.DELIVERY_NAME, mainIntent.getStringExtra(AvenuesParams.DELIVERY_NAME)));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.DELIVERY_ADDRESS, mainIntent.getStringExtra(AvenuesParams.DELIVERY_ADDRESS)));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.DELIVERY_CITY, mainIntent.getStringExtra(AvenuesParams.DELIVERY_CITY)));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.DELIVERY_STATE, mainIntent.getStringExtra(AvenuesParams.DELIVERY_STATE)));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.DELIVERY_ZIP, mainIntent.getStringExtra(AvenuesParams.DELIVERY_ZIP)));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.DELIVERY_COUNTRY, mainIntent.getStringExtra(AvenuesParams.DELIVERY_COUNTRY)));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.DELIVERY_TEL, mainIntent.getStringExtra(AvenuesParams.DELIVERY_TEL)));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.MERCHANT_PARAM1, "additional Info."));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.MERCHANT_PARAM2, "additional Info."));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.MERCHANT_PARAM3, "additional Info."));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.MERCHANT_PARAM4, "additional Info."));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.PAYMENT_OPTION, mainIntent.getStringExtra(AvenuesParams.PAYMENT_OPTION)));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.CARD_TYPE, mainIntent.getStringExtra(AvenuesParams.CARD_TYPE)));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.CARD_NAME, mainIntent.getStringExtra(AvenuesParams.CARD_NAME)));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.DATA_ACCEPTED_AT,/*mainIntent.getStringExtra(AvenuesParams.DATA_ACCEPTED_AT)*/"N"));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.ISSUING_BANK, mainIntent.getStringExtra(AvenuesParams.ISSUING_BANK)));
        if (encVal != null)
            params.append(ServiceUtility.addToPostParams(AvenuesParams.ENC_VAL, URLEncoder.encode(encVal)));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.EMI_PLAN_ID, mainIntent.getStringExtra(AvenuesParams.EMI_PLAN_ID)));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.EMI_TENURE_ID, mainIntent.getStringExtra(AvenuesParams.EMI_TENURE_ID)));

        ////////For Testing////////
        //	params.append(ServiceUtility.addToPostParams("data_accept","Y"));

        //data_accept
        if (mainIntent.getStringExtra(AvenuesParams.SAVE_CARD) != null)
            params.append(ServiceUtility.addToPostParams(AvenuesParams.SAVE_CARD, mainIntent.getStringExtra(AvenuesParams.SAVE_CARD)));
        String vPostParams = params.substring(0, params.length() - 1);
        try {
            webview.postUrl(Constants.TRANS_URL, EncodingUtils.getBytes(vPostParams, "UTF-8"));
        } catch (Exception e) {
            showToast("Exception occured while opening webview.");
        }
    }
} 