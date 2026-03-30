package com.ojassoft.astrosage.misc;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.VolleyError;
import com.libojassoft.android.misc.VolleyResponse;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.jinterface.IAskCallback;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import static com.ojassoft.astrosage.utils.CGlobalVariables.PAYMENT_TYPE_SERVICE;

public class GetCheckSum implements VolleyResponse {

    IAskCallback iAskCallback;
    //SendDataBackToComponent sendDataBackToComponent;
    CustomProgressDialog pd;
    Typeface typeface;
    Context context;

    public GetCheckSum(Context context, Typeface typeface) {
        this.context = context;
        this.iAskCallback = (IAskCallback) context;
       // sendDataBackToComponent = (SendDataBackToComponent) context;
        this.typeface = typeface;
    }

    public void getCheckSum(Map<String, String> params, int method) {
        showProgressBar();
        String url = CGlobalVariables.GET_CHECKSUM_FOR_PAYTM_URL;
        //Log.d("PaytmOrder", "url= "+url);
        //Log.d("PaytmOrder", "params= "+params);
        CUtils.vollyPostRequest(GetCheckSum.this, url, params, method);
    }

    @Override
    public void onResponse(String response, int method) {
        //Log.d("PaytmOrder", "response= "+response);
        hideProgressBar();
        try {
            if (response != null && !response.equalsIgnoreCase("")) {
                // onStartTransaction();
                try {
                    JSONObject Obj = new JSONObject(response);
                    String checksum = Obj.getString("message");
                    if(!TextUtils.isEmpty(checksum)) {
                        iAskCallback.getCallBack(checksum, CUtils.callBack.GET_CHECKSUM, "", "");
                    }else {
                        iAskCallback.getCallBack("", CUtils.callBack.GET_CHECKSUM, "", "");
                    }
                } catch (Exception var7) {
                    Log.i("Exce", var7.toString());
                    iAskCallback.getCallBack("", CUtils.callBack.GET_CHECKSUM, "", "");

                }
            } else {

                iAskCallback.getCallBack("", CUtils.callBack.GET_CHECKSUM, "", "");


            }
        } catch (Exception e) {
            iAskCallback.getCallBack("", CUtils.callBack.GET_CHECKSUM, "", "");

        }
    }

    @Override
    public void onError(VolleyError error) {
        //Log.d("PaytmOrder", "error= "+error.toString());
        hideProgressBar();
        iAskCallback.getCallBack("", CUtils.callBack.GET_CHECKSUM, "", "");
    }

    /**
     * show Progress Bar
     */
    private void showProgressBar() {
        if (pd == null) {
            pd = new CustomProgressDialog(context, typeface);
        }

        if (!pd.isShowing()) {
            pd.setCanceledOnTouchOutside(false);
            pd.show();
        }
    }

    /**
     * hide Progress Bar
     */
    private void hideProgressBar() {
        try {
            if (pd != null && pd.isShowing())
                pd.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
