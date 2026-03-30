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
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.utils.AvenuesParams;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.Constants;
import com.ojassoft.astrosage.utils.RSAUtility;
import com.ojassoft.astrosage.utils.ServiceUtility;

import org.apache.http.util.EncodingUtils;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;


public class ActCCAvenueServicePaymentActivity extends Activity {

    Intent mainIntent;
    String encVal;
    String currency = "INR";
    String amount = "0";
    MyCustomToast mct;
    private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    private Typeface typeface;
    private RequestQueue queue;
    private String payStatus = "";
    private String order_id = "";
    private CustomProgressDialog pd = null;


    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.activity_webview);
        LANGUAGE_CODE = ((AstrosageKundliApplication) ActCCAvenueServicePaymentActivity.this.getApplication())
                .getLanguageCode();

        typeface = CUtils.getRobotoFont(
                ActCCAvenueServicePaymentActivity.this, LANGUAGE_CODE, CGlobalVariables.regular);
        mainIntent = getIntent();
        init();
        getData();
    }

    public void showToast(String msg) {
        Toast.makeText(this, "Toast: " + msg, Toast.LENGTH_LONG).show();
    }

    private void init() {
        pd = new CustomProgressDialog(this, typeface);
        pd.setCancelable(false);
        pd.show();

        queue = VolleySingleton.getInstance(this).getRequestQueue();
        mct = new MyCustomToast(this, this
                .getLayoutInflater(), this, typeface);

        currency = mainIntent.getStringExtra(AvenuesParams.CURRENCY);
        amount = mainIntent.getStringExtra(AvenuesParams.AMOUNT);
        order_id = mainIntent.getStringExtra(AvenuesParams.ORDER_ID);
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

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
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
            StringBuffer vEncVal = new StringBuffer();
            vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.AMOUNT, mainIntent.getStringExtra(AvenuesParams.AMOUNT)));
            vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.CURRENCY, mainIntent.getStringExtra(AvenuesParams.CURRENCY)));
            vEncVal.append(ServiceUtility.addToPostParams(AvenuesParams.CUSTOMER_IDENTIFIER, mainIntent.getStringExtra(AvenuesParams.CUSTOMER_IDENTIFIER)));
            encVal = RSAUtility.encrypt(vEncVal.substring(0, vEncVal.length() - 1), response);
        }
        @SuppressWarnings("unused")
        class MyJavaScriptInterface {
            @JavascriptInterface
            public void processHTML(String html) {
                Log.wtf("Html returned from handler", html);

                // process the html as needed by the app
                if (html.indexOf("Success") != -1) {
                    payStatus = "1";
                } else {
                    payStatus = "0";
                }
                Intent data = new Intent();
                data.putExtra("payStatus", payStatus);
                setResult(RESULT_OK, data);
                finish();
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
        params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_TEL, mainIntent.getStringExtra(AvenuesParams.BILLING_TEL)));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.BILLING_EMAIL, mainIntent.getStringExtra(AvenuesParams.BILLING_EMAIL)));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.DELIVERY_TEL, mainIntent.getStringExtra(AvenuesParams.DELIVERY_TEL)));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.MERCHANT_PARAM1, "additional Info."));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.MERCHANT_PARAM2, "additional Info."));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.MERCHANT_PARAM3, "additional Info."));
        params.append(ServiceUtility.addToPostParams(AvenuesParams.MERCHANT_PARAM4, "additional Info."));
        if (encVal != null)
            params.append(ServiceUtility.addToPostParams(AvenuesParams.ENC_VAL, URLEncoder.encode(encVal)));
        
        String vPostParams = params.substring(0, params.length() - 1);
        try {
            webview.postUrl(Constants.TRANS_URL, EncodingUtils.getBytes(vPostParams, "UTF-8"));
        } catch (Exception e) {
            showToast("Exception occured while opening webview.");
        }
    }

    @Override
    public void onBackPressed() {
        try {
            payStatus = "0";
            Intent data = new Intent();
            data.putExtra("payStatus", payStatus);
            setResult(RESULT_OK, data);
            finish();
        }catch (Exception e){
            //
        }
    }
}