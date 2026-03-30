package com.ojassoft.astrosage.ui.fragments.astrochatfragment.networkdataload;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.ui.fragments.astrochatfragment.model.MessageDecode;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.UserEmailFetcher;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_EMAIL_ID;

/**
 * Created by ojas-10 on 20/6/16.
 */
public class NetWorkInsertNewRow {
    Context context;
    String url;
    MessageDecode messageDecode;
    BeanHoroPersonalInfo beanHoroPersonalInfo;
    static boolean result = false;

    public NetWorkInsertNewRow(Context context) {
        this.context = context;

        beanHoroPersonalInfo = CUtils.getBirthDetailSharedPrefrence(context);
    }

    public boolean insertBlankRow() {
        Log.d("NetWorkInsertNewRow", "checking for number of question available");
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, CGlobalVariables.SENDQUESTION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String result) {
                        try {
                            Log.d("NetWorkInsertNewRow", "on receive responce");
                            JSONArray jsonArray = new JSONArray(result);
                            JSONObject jsonObject = (JSONObject) jsonArray.get(0);
                            String response = jsonObject.getString("Result");

                            if (response.equalsIgnoreCase("0")) {
                                Toast.makeText(context, "result is:" + response, Toast.LENGTH_SHORT).show();
                                Log.d("Response detail: ", "result is:" + response);
                            } else if (response.equalsIgnoreCase("1")) {
                                int noOfQuestion = CUtils.getIntData(context, CGlobalVariables.NOOFQUESTIONAVAILABLE, 0);
                                noOfQuestion += 1;
                                CUtils.saveIntData(context, CGlobalVariables.NOOFQUESTIONAVAILABLE, noOfQuestion);
                                Toast.makeText(context, "result is:" + response + "   \nNumber of Question:" + noOfQuestion + "  ", Toast.LENGTH_SHORT).show();
                                Log.d("Response detail: ", "result is:" + response + "   \nNumber of Question:" + noOfQuestion);
                                NetWorkInsertNewRow.result = true;
                            } else {
                                Toast.makeText(context, "result is:" + response, Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception jsonException) {
                            /*isSuccessfull = false;*/
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e("Error", "Failed to get a question");
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
                params.put("key", "897584714");
                params.put(KEY_EMAIL_ID, CUtils.replaceEmailChar(UserEmailFetcher.getEmail(context)));
                params.put("useremailid", CUtils.replaceEmailChar(UserEmailFetcher.getEmail(context)));
                params.put("Type", "insert");
                return params;
            }
        };

        VolleySingleton.getInstance(context).getRequestQueue().add(jsonObjectRequest);
        return result;
    }
}
