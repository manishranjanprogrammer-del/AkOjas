package com.ojassoft.astrosage.varta.volley_network;


import com.android.volley.AuthFailureError;
import com.android.volley.Response;

import java.util.Map;

public class MyStringRequest extends com.android.volley.toolbox.StringRequest {
    Map<String, String> params;
    Map<String, String> headerParams;
    int method;

    public MyStringRequest(int methodType, String url, Response.Listener<String> listener, Response.ErrorListener errorListener, Map<String, String> params, int method) {
        super(methodType, url, listener, errorListener);
        this.params = params;
        this.method = method;
    }

    public MyStringRequest(int methodType, String url, Response.Listener<String> listener, Response.ErrorListener errorListener, Map<String, String> params, Map<String, String> headerParams, int method) {
        super(methodType, url, listener, errorListener);
        this.headerParams = headerParams;
        this.params = params;
        this.method = method;
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return params;
    }

   /* @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headerParams;
    }*/

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        if (headerParams != null) {
            return headerParams;
        }
        return super.getHeaders();
    }


    @Override
    public String getBodyContentType() {
        return "application/x-www-form-urlencoded; charset=UTF-8";
    }

    public int getMethodNumber() {
        return method;
    }
}
