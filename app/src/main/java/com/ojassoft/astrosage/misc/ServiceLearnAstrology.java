package com.ojassoft.astrosage.misc;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ojas on २/८/१६.
 */
public class ServiceLearnAstrology extends IntentService {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    Context context;
    private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;

    public ServiceLearnAstrology() {
        super("ServiceLearnAstrology");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        SendReportToServerAsyn();
    }

    private void SendReportToServerAsyn() {
        context = getApplicationContext();
        try {
            sendPostRequest();
        } catch (Exception e) {

        }
    }


    private String sendPostRequest()
            throws IOException {
        InputStream is = null;
        String result = "";


       /* try {

            LANGUAGE_CODE = ((AstrosageKundliApplication) context)
                    .getLanguageCode();

            HttpClient hc = CUtils.getNewHttpClient();
            HttpPost postMethod = new HttpPost(CGlobalVariables.LEARN_ASTROLOGY);

            postMethod.setEntity(new UrlEncodedFormEntity(getNameValuePairs_Askquestion(
                    String.valueOf(LANGUAGE_CODE), CUtils.getApplicationSignatureHashCode(this)), HTTP.UTF_8));
            HttpResponse httpResponse = null;

            httpResponse = hc.execute(postMethod);

            HttpEntity httpEntity = httpResponse.getEntity();

            is = httpEntity.getContent();

            BufferedReader reader = new BufferedReader(new InputStreamReader(
                    is, "UTF-8"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            result = sb.toString();
            System.out.println("Responec=====SERVIICEEEEEEEEEEEEEEEEEEEEEEEEEE" + result);
            if (result.trim().contains("[{\"Result\":\"2\"}]")) {
                //
            } else if (result != null && !result.isEmpty()) {
                CUtils.saveStringData(context, "LEARN" + String.valueOf(LANGUAGE_CODE), result);

            }

           *//* if (successResult.equalsIgnoreCase("1")) {
                // if(CUtils.getBooleanData(context, CGlobalVariables.ISGOOLGEVERIFY,true)) {
                //Track User
                //    sendGoogleAnalytics(purchaseData);

                // }

                clearDataInPreferences();
            } else if (successResult.equalsIgnoreCase("0")) {
                //Data Not Inserted On Server, it will resent from ServiceToGetUnConsumedProductFromGoogleAccount
                clearDataInPreferences();
            } else if (successResult.equalsIgnoreCase("InvalidOperation")) {
                clearDataInPreferences();

            }*//*

        } catch (Exception ex) {
            clearDataInPreferences();
            //Log.i("Tag", "1 - " + ex.getMessage());

        } finally {
            if (httpConn != null) {
                httpConn.disconnect();
            }
        }*/

        return result;
    }

    private void clearDataInPreferences() {
        CUtils.saveStringData(context, "LEARN" + String.valueOf(LANGUAGE_CODE), "");
    }

    private List<NameValuePair> getNameValuePairs_Askquestion(
            String languagecode, String key) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
        nameValuePairs.add(new BasicNameValuePair("languageCode", languagecode));
        nameValuePairs.add(new BasicNameValuePair("Key", CUtils.getApplicationSignatureHashCode(this)));
        return nameValuePairs;

    }

}
