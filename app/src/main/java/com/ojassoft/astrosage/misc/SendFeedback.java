package com.ojassoft.astrosage.misc;

import android.content.Context;

import com.android.volley.VolleyError;
import com.libojassoft.android.misc.SendDataBackToComponent;
import com.libojassoft.android.misc.VolleyResponse;
import com.libojassoft.android.utils.LibCGlobalVariables;
import com.libojassoft.android.utils.LibCUtils;

import java.util.Map;

public class SendFeedback implements VolleyResponse {
    Context context;
    SendDataBackToComponent sendDataBackToComponent;

    public SendFeedback(Context context) {
        this.context = context;
        sendDataBackToComponent = (SendDataBackToComponent) context;
    }

    public void sendFeedbackToServer(Map<String, String> params, int method) {
        LibCUtils.vollyPostRequest(context, SendFeedback.this, LibCGlobalVariables.FEED_BACK, params, method);
    }

    @Override
    public void onResponse(String response, int method) {
        sendDataBackToComponent.doActionAfterGetResult(response, method);
    }

    @Override
    public void onError(VolleyError error) {
        if (error != null && error.getMessage() != null) {
            sendDataBackToComponent.doActionOnError(error.getMessage());
        }

    }
}
