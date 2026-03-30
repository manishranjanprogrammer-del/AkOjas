package com.ojassoft.astrosage.misc;

import android.app.IntentService;
import android.content.Intent;
import androidx.annotation.Nullable;
import android.util.Log;

import com.libojassoft.android.misc.SendDataBackToComponent;
import com.ojassoft.astrosage.beans.BeanUserMapping;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.HttpUtility;

import org.json.JSONArray;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ojas on ३/११/१७.
 */

public class SaveAstroLogerInfoService extends IntentService implements SendDataBackToComponent {
    /**
     * Creates an IntentService.  Invoked by your subclass's constructor
     */
    BeanUserMapping beanUserMapping;

    public SaveAstroLogerInfoService() {
        super("SaveAstroLogerInfoService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        String key = CUtils.getApplicationSignatureHashCode(SaveAstroLogerInfoService.this);
        beanUserMapping = CUtils.getUserMappingData(SaveAstroLogerInfoService.this);
        sendDataToServer(beanUserMapping, key);


    }

    private void sendDataToServer(BeanUserMapping beanUserMapping, String key) {

        //String resultStr = "";
        if (CUtils.isConnectedWithInternet(SaveAstroLogerInfoService.this)) {
            //Collecting information in Map
            Map<String, String> params = new HashMap<>();
            params.put("name", beanUserMapping.getName());
            params.put("isastrologer", String.valueOf(beanUserMapping.getIsAstrologer()));
            params.put("mobileno", beanUserMapping.getPhoneNo());
            params.put("city", beanUserMapping.getCity());
            params.put("aboutuser", beanUserMapping.getAbout());
            params.put("astrocampid", beanUserMapping.getAstrocampId());
            params.put("deviceid", beanUserMapping.getDeviceId());
            params.put("key", key);

            try {
                String url = "http://192.168.1.64/as/GCM/update-user-info.asp?";
                new HttpUtility(SaveAstroLogerInfoService.this).sendPostRequest(url, params, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        //return resultStr;
        //checkForUpdateData(beanUserMapping,data);
    }

    private String parseResult(String resultStr) {
        String result = "";
        try {
            if (resultStr != null && !resultStr.equals("")) {
                JSONArray jArray = new JSONArray(resultStr);
                result = jArray.getJSONObject(jArray.length() - 1).getString("Result");
            }
        } catch (Exception e) {
            Log.i("Error", "" + e.getMessage());
        }
        return result;
    }

    @Override
    public void doActionAfterGetResult(String response, int method) {
        String result = parseResult(response);
        if (!result.equals("") && result.equals("1")) {
            beanUserMapping.setStatus(2);
            CUtils.saveUserMappingData(SaveAstroLogerInfoService.this, beanUserMapping);
        }
    }

    @Override
    public void doActionOnError(String response) {

    }
}

