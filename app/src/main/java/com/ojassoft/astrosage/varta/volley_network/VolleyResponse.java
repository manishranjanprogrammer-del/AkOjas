package com.ojassoft.astrosage.varta.volley_network;



public interface VolleyResponse {

void onResponse(String response, int method);

void onError(com.android.volley.VolleyError error);
}