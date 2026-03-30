package com.ojassoft.astrosage.ui.fragments.astrochatfragment.networkdataload;

import android.app.Activity;
import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.ui.act.ActAskQuestion;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_EMAIL_ID;

/**
 * Created by ojas on 29/9/16.
 */
public class NetworkQuestionDetail {
    Activity activity;
    String email;

    public NetworkQuestionDetail(Activity context, String email){
        this.activity =context;
        this.email=email;
    }

    public void getAvailableQuestion() {
        StringRequest request= new StringRequest(Request.Method.POST, CGlobalVariables.AVAILABLEQUESTION, new Response.Listener<String>() {
            @Override
            public void onResponse(String result) {
                try {
                    Log.d("NetWorkQuestionDetail", "on receive responce"+result);
                    JSONArray jsonArray = new JSONArray(result);
                    JSONObject jsonObject = (JSONObject) jsonArray.get(0);
                    String response = jsonObject.getString("NumberOfQuestionAvailable");
                    //  Toast.makeText(activity,"NumberOfQuestionAvailable: "+response, Toast.LENGTH_LONG).show();
                    if (response!=null && !response.isEmpty()){
                        CUtils.saveIntData(activity, CGlobalVariables.NOOFQUESTIONAVAILABLE,Integer.parseInt(response));
                        if(activity!=null && activity instanceof ActAskQuestion &&Integer.parseInt(response)>0){
                            ((ActAskQuestion)activity).showFreeQuestion(Integer.parseInt(response));
                        }
                    }

                } catch (Exception jsonException) {
                            /*isSuccessfull = false;*/
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("Error", "Failed to get a question");
               /* isSuccessfull = false;*/
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put(KEY_EMAIL_ID, CUtils.replaceEmailChar(email));
                params.put("key", CUtils.getApplicationSignatureHashCode(activity));
                params.put("androidid", CUtils.getMyAndroidId(activity));
                return params;
            }
        };

        VolleySingleton.getInstance(activity).getRequestQueue().add(request);
    }

}
