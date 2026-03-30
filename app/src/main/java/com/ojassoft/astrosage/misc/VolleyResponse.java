package com.ojassoft.astrosage.misc;



public interface VolleyResponse {

void onResponse(String response,int method);

void onError(com.android.volley.VolleyError error);
}