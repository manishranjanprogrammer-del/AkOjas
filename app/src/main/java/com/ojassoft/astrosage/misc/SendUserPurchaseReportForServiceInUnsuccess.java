package com.ojassoft.astrosage.misc;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;



//this file not in use

/**
 * Created by ojas on १८/७/१६.
 */
public class SendUserPurchaseReportForServiceInUnsuccess extends IntentService {


    // String requestURL = "https://inappverify.astrosage.com/inapppurchaseverificationv3";

    // String requestURL ="http://192.168.1.70:8089/inapppurchaseverification";
    //   String requestURL ="http://1-dot-inappverify.appspot.com/inapppurchaseverification";
    String requestURL = "https://inappverify.astrosage.com/inapppurchaseverificationv3";

    // String requestURL = "http://192.168.1.132:8880/inapppurchaseverificationv3";
    Context context;
    String key3 = "isPaymentDonestatus", key2 = "jsonInputForService";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public SendUserPurchaseReportForServiceInUnsuccess() {
        super("SendUserPurchaseReportForServiceInUnsuccess");
    }


   /* @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        //com.google.analytics.tracking.android.//Log.e("FROM SERVICE LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL");


        boolean checkStatus =CUtils.getBooleanData(SendUserPurchaseReportForServiceInUnsuccess.this, CGlobalVariables.PAYMENTDONE_KEY_FOR_ASTRO_SERVICES_ISPAYMENTDONE, true);
        //com.google.analytics.tracking.android.//Log.e("FROM SERVICE checkStatus = "+checkStatus);
        if (!checkStatus) {
            if (CUtils.getStringData(getApplicationContext(), CGlobalVariables.PAYMENTDONE_KEY_FOR_ASTRO_SERVICES_JSONOBJ, "") != null) {
                //com.google.analytics.tracking.android.//Log.e("Start async task");
                new SendReportToServerAsyn().execute();
            } else {
                //com.google.analytics.tracking.android.//Log.e("STOP SERVICE 1111");
                SendUserPurchaseReportForServiceInUnsuccess.this.stopSelf();

            }
        }else {
            //com.google.analytics.tracking.android.//Log.e("STOP SERVICE 22222");
            SendUserPurchaseReportForServiceInUnsuccess.this.stopSelf();
        }


        return START_NOT_STICKY;
    }*/

    @Override
    protected void onHandleIntent(Intent intent) {
        //com.google.analytics.tracking.android.//Log.e("FROM SERVICE LLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLLL");


        boolean checkStatus = CUtils.getBooleanData(SendUserPurchaseReportForServiceInUnsuccess.this, CGlobalVariables.PAYMENTDONE_KEY_FOR_ASTRO_SERVICES_ISPAYMENTDONE, true);
        //com.google.analytics.tracking.android.//Log.e("FROM SERVICE checkStatus = " + checkStatus);
        if (!checkStatus) {
            if (CUtils.getStringData(getApplicationContext(), CGlobalVariables.PAYMENTDONE_KEY_FOR_ASTRO_SERVICES_JSONOBJ, "") != null) {
                //com.google.analytics.tracking.android.//Log.e("Start async task");
                //    new SendReportToServerAsyn().execute();
                //SendReportToServerAsyn();
            }
        }
    }

  /*  private void SendReportToServerAsyn() {
        context = getApplicationContext();
        try {
            sendPostRequest();
        } catch (Exception e) {

        }
    }*/
/*

    class SendReportToServerAsyn extends AsyncTask<String, Long, Void> {


        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            context = getApplicationContext();
        }

        @Override
        protected Void doInBackground(String... params) {
            // TODO Auto-generated method stub
            try {
                sendPostRequest();
            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            // TODO Auto-generated method stub
            super.onPostExecute(result);
        }

    }
*/

   /* private String sendPostRequest()
            throws IOException {
        InputStream inputStream = null;
        String result = "";


        try {

            URL url = new URL(requestURL);

            httpConn = (HttpURLConnection) url.openConnection();

            httpConn.setConnectTimeout(15000);
            httpConn.setRequestMethod("POST");
            httpConn.setReadTimeout(15000);

            httpConn.setUseCaches(false);

            httpConn.setDoInput(true); // true indicates the server returns			 					// response

            StringBuffer requestParams = new StringBuffer();
            httpConn.setDoOutput(true); // true indicates POST request


            // add for send service data in object of json
            requestParams.append(URLEncoder.encode("jsonInputOther", "UTF-8"));
            requestParams.append("=").append(URLEncoder.encode("", "UTF-8"));
            requestParams.append("&");
            requestParams.append(URLEncoder.encode(key2, "UTF-8"));
            String value2 = CUtils.getStringData(context, CGlobalVariables.PAYMENTDONE_KEY_FOR_ASTRO_SERVICES_JSONOBJ, "");
            requestParams.append("=").append(URLEncoder.encode(value2, "UTF-8"));
            requestParams.append("&");
            requestParams.append(URLEncoder.encode(key3, "UTF-8"));
            String paymentstatus = "" + CUtils.getBooleanData(context, CGlobalVariables.PAYMENTDONE_KEY_FOR_ASTRO_SERVICES_ISPAYMENTDONE, true);
            requestParams.append("=").append(URLEncoder.encode(paymentstatus, "UTF-8"));
            requestParams.append("&");
            requestParams.append(URLEncoder.encode("appVerName", "UTF-8"));
            requestParams.append("=").append(URLEncoder.encode(LibCUtils.getApplicationVersionToShow(context), "UTF-8"));
            requestParams.append("&");
            requestParams.append(URLEncoder.encode("type", "UTF-8"));
            requestParams.append("=").append(URLEncoder.encode("insert", "UTF-8"));
            requestParams.append("&");
            requestParams.append(URLEncoder.encode("problem", "UTF-8"));
            requestParams.append("=").append(URLEncoder.encode("", "UTF-8"));
            requestParams.append("&");
            String chatid = CUtils.getStringData(context,CGlobalVariables.PAYMENTDONE_KEY_FOR_ASTRO_SERVICES_CHATID,"");
            requestParams.append(URLEncoder.encode("chatid", "UTF-8"));
            requestParams.append("=").append(URLEncoder.encode(chatid, "UTF-8"));
            requestParams.append("&");
            requestParams.append(URLEncoder.encode("payMode", "UTF-8"));
            requestParams.append("=").append(URLEncoder.encode("Google", "UTF-8"));
            requestParams.append("&");
            requestParams.append(URLEncoder.encode("price", "UTF-8"));
            requestParams.append("=").append(URLEncoder.encode("0", "UTF-8"));
            requestParams.append("&");
            requestParams.append(URLEncoder.encode("ordersource", "UTF-8"));
            requestParams.append("=").append(URLEncoder.encode("astroSage_Kundli", "UTF-8"));
            requestParams.append("&");
            requestParams.append(URLEncoder.encode("chattitle", "UTF-8"));
            requestParams.append("=").append(URLEncoder.encode("", "UTF-8"));
            requestParams.append("&");
            requestParams.append(URLEncoder.encode("androidid", "UTF-8"));
            requestParams.append("=").append(URLEncoder.encode(CUtils.getMyAndroidId(context), "UTF-8"));

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
            if (successResult.equalsIgnoreCase("1")) {
                clearDataInPreferences();
            } else {
                clearDataInPreferences();
            }
        } catch (Exception ex) {
            clearDataInPreferences();
        } finally {
            if (httpConn != null) {
                httpConn.disconnect();
            }
        }
        return result;
    }*/

    /*private void clearDataInPreferences() {
        CUtils.saveBooleanData(context, CGlobalVariables.PAYMENTDONE_KEY_FOR_ASTRO_SERVICES_ISPAYMENTDONE, true);
        CUtils.saveStringData(context,CGlobalVariables.PAYMENTDONE_KEY_FOR_ASTRO_SERVICES_JSONOBJ,"");
        CUtils.saveStringData(context,CGlobalVariables.PAYMENTDONE_KEY_FOR_ASTRO_SERVICES_CHATID,"");

    }*/
}
