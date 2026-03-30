package com.ojassoft.astrosage.networkcall;

import com.android.volley.Cache;
import com.android.volley.VolleyError;

/**
 * Created on 17/7/2019 by Ankit
 * Volley Result Listener
 */
public interface OnVolleyResultListener {

     void onVolleySuccess(String result, Cache cache);

    void onVolleyError(VolleyError result);
}
