package com.ojassoft.astrosage.ui.fragments.astrochatfragment.networkdataload;

import android.content.Context;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by ojas on 28/9/16.
 */
public class NetworkSendRatingStatus {
    Context context;
    String rating;
    String orderId;
    String chatId;

    public NetworkSendRatingStatus(Context context, float rating, String orderId, String chatId) {
        this.context=context;
        this.rating=(int)rating+"";// TypeCast in Integer and then in String..
        this.orderId=orderId;
        this.chatId=chatId;
    }

    public void sendUpdatedRating() {
        StringRequest request = new StringRequest(Request.Method.POST, CGlobalVariables.SENDANSWERRATING,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String responseMessage) {
                        try {
                            JSONArray jsonArray = null;
                            jsonArray = new JSONArray(responseMessage);
                            JSONObject jsonObject = (JSONObject) jsonArray.get(0);
                            String responseCode = jsonObject.getString("Result");

                            if(responseCode.equalsIgnoreCase("0")) {
                                //Toast.makeText(context, "All parameters required", Toast.LENGTH_SHORT).show();
                                //Log.e("SENDRATINGRESPONSE","All parameters required");
                            } else if(responseCode.equalsIgnoreCase("1")) {
                                //Toast.makeText(context, "successful inserted", Toast.LENGTH_SHORT).show();
                                //Log.e("SENDRATINGRESPONSE","Successful inserted");
                            } else if(responseCode.equalsIgnoreCase("2")) {
                                //Toast.makeText(context, "Authentication failed", Toast.LENGTH_SHORT).show();
                                //Log.e("SENDRATINGRESPONSE","Authentication failed");
                            } else if(responseCode.equalsIgnoreCase("3")) {
                                //Toast.makeText(context, "insertion failed into DB", Toast.LENGTH_SHORT).show();
                                //Log.e("SENDRATINGRESPONSE","Insertion failed into DB");
                            }
                        } catch (JSONException e) {
                           // e.printStackTrace();
                            Log.w("Response Message",responseMessage);
                            //Toast.makeText(context, responseMessage, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null) {
                  //  //Log.e("Error on SENDRATING", error.getMessage());
                } else {
                    //Log.e("Error on SENDRATING", "Null VolleyError");
                    //Toast.makeText(context, "SENDRATING error \n (Null Volly Error)", Toast.LENGTH_SHORT).show();
                }

            }

        }) {
            /*@Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }*/

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("orderid", orderId);
                params.put("chatid", chatId);
                params.put("answerrating", rating);
                params.put("key", CUtils.getApplicationSignatureHashCode(context));
                return params;
            }
        };

        VolleySingleton.getInstance(context).getRequestQueue().add(request);
    }
}
