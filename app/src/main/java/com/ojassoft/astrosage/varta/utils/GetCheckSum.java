package com.ojassoft.astrosage.varta.utils;

import android.content.Context;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.VolleyError;
import com.ojassoft.astrosage.varta.interfacefile.IAskCallback;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;

import org.json.JSONObject;

import java.util.Iterator;
import java.util.Map;

public class GetCheckSum implements VolleyResponse {

    IAskCallback iAskCallback;
    //SendDataBackToComponent sendDataBackToComponent;
    CustomProgressDialog pd;
    Typeface typeface;
    Context context;

    public GetCheckSum(Context context) {
        this.context = context;
        this.iAskCallback = (IAskCallback) context;
    }

    public void getCheckSum(Map<String, String> params, int method) {
        showProgressBar();
        CUtils.vollyPostRequest(GetCheckSum.this, CGlobalVariables.GET_CHECKSUM_FOR_PAYTM, params, method);
    }

    @Override
    public void onResponse(String response, int method) {
        hideProgressBar();
        try {
            if (response != null && !response.equalsIgnoreCase("")) {
                // onStartTransaction();
                //Log.d("LOG", "Payment Transaction is checksum " + response);
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
        hideProgressBar();
        iAskCallback.getCallBack("", CUtils.callBack.GET_CHECKSUM, "", "");
    }

    /**
     * show Progress Bar
     */
    private void showProgressBar() {
        if (pd == null) {
            pd = new CustomProgressDialog(context);
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
            if (pd != null & pd.isShowing())
                pd.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
