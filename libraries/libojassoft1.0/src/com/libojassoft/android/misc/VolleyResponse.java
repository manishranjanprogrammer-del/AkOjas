package com.libojassoft.android.misc;



public interface VolleyResponse {

void onResponse(String response,int method);

void onError(com.android.volley.VolleyError error);
}