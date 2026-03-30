package com.ojassoft.astrosage.model;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.libojassoft.android.misc.VolleyResponse;
import com.libojassoft.android.misc.VolleyServiceHandler;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.libojassoft.android.utils.AESDecryption;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.UserEmailFetcher;

import org.json.JSONObject;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

public class CGCMRegistrationInfoSaveOnOjas implements VolleyResponse {

    Context context;

    public void saveUserGCMRegistrationInformationOnOjasServer(Context context, String regid, int languageCode, String loginId) {
        this.context = context;
        JSONObject obj = getRegisterTokenParams(context, regid, languageCode, loginId);
        if (obj != null) {
            HashMap<String, String> params = new HashMap<String, String>();
            params.put("op", "savewithdeviceid");
            params.put("value", obj.toString());
            String url = CGlobalVariables.GCM_REGISTRATION_URL;
            //Log.e("ResponseGCM", params.toString());
            RequestQueue queue = VolleySingleton.getInstance(AstrosageKundliApplication.getAppContext()).getRequestQueue();
            StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, url, this, true, params, 0).getMyStringRequest();
            queue.add(stringRequest);
        }
    }

    private String getCustomSecurityKey() {
        int col;
        String token = "";
        String MY_TOKEN = "OJAS@MOBILE";
        char[] charToken = MY_TOKEN.toCharArray();
        for (int index = 0; index < charToken.length; index++) {
            col = (int) charToken[index];
            col = col + index;
            token += new Character((char) col).toString();
        }
        return token.trim();

    }

    @Override
    public void onResponse(String response, int method) {
        Log.e("TestGCM", "response=" + response);
    }

    @Override
    public void onError(VolleyError error) {
        //Log.e("ResponseGCM error", error.getMessage());
    }

    public JSONObject getRegisterTokenParams(Context context, String regid, int languageCode, String loginId) {
        JSONObject obj = new JSONObject();
        try {
            int isEmailEncrypt = 0;
            String serkey = getCustomSecurityKey();
            String emailId = CUtils.replaceEmailChar(UserEmailFetcher.getEmail(this.context));
            if (emailId == null || emailId.equals("")) {
                emailId = CUtils.getMyAndroidId(context);
            } else {
                //emailId = CUtils.replaceEmailChar(emailId);
                isEmailEncrypt = 1;
            }
            String myAndropidId = CUtils.getMyAndroidId(this.context);
            String packageName = this.context.getPackageName();

            String latitude = CUtils.getStringData(context, CGlobalVariables.KEY_LATITUDE, "");//signupabhishek
            String longitude = CUtils.getStringData(context, CGlobalVariables.KEY_LONGITUDE, "");
            String city = CUtils.getStringData(context, CGlobalVariables.KEY_CITY, "");

            obj.accumulate(CGlobalVariables.KEY_EMAIL_ID, emailId);//  DISABLED BY BIJENDRA NOT TO SEND E-MAIL ID ON SERVER.
            obj.accumulate("deviceid", myAndropidId);// CHANGED BY BIJENDRA ON 11-12-14
            obj.accumulate("pkgname", packageName);
            obj.accumulate("tokenid", regid);
            obj.accumulate("serkey", serkey);
            obj.accumulate("applang", (languageCode + 1));
            obj.accumulate("latitude", latitude);
            obj.accumulate("longitude", longitude);
            obj.accumulate("city", city);
            obj.accumulate("ismaencrypt", isEmailEncrypt);
				/* NOTE
				 In application language code start from 0, while on server it start from 1.so
				 we have added 1 here in the language code to save on server.
				 */
            obj.accumulate("appusername", CUtils.replaceEmailChar(loginId));
            //Info related to app and android sdk
            obj.accumulate("android_sdk_version", android.os.Build.VERSION.SDK_INT);
            obj.accumulate("android_release_version", android.os.Build.VERSION.RELEASE);
            obj.accumulate("app_version_code", BuildConfig.VERSION_CODE);
            obj.accumulate("app_version_name", BuildConfig.VERSION_NAME);

            int isInstanceId = CUtils.getIntData(context, CGlobalVariables.ISInstanceID, 1);
            obj.accumulate(CGlobalVariables.ISInstanceID, isInstanceId);

            String encodedRefurl = com.ojassoft.astrosage.varta.utils.CUtils.getEncodedReferralUrl(context);
            obj.accumulate(com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_REFERRER_URL, encodedRefurl);
            Log.e("TestGCM", "encodedRefurl=" + encodedRefurl);

            String encodedFburl = "";
            try {
                String fbUrl = AstrosageKundliApplication.facebookDeepLinkUrl;
                if(!TextUtils.isEmpty(fbUrl)) {
                    encodedFburl = URLEncoder.encode(fbUrl, "UTF-8");
                }
            }catch (Exception e){
            }
            obj.accumulate(com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_FB_AD_LINK, encodedFburl);
            Log.d("TestGCM", "encodedFburl=" + encodedFburl);

            obj.accumulate(CGlobalVariables.IS_FIRST_INSTALL_KEY, String.valueOf(AstrosageKundliApplication.isUserFirstTimeInstallTheApp));
        } catch (Exception e) {
            //obj = null;
        }
        //Log.d("TestGCM", "parms=" + obj);
        return obj;
    }

}
