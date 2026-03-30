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
public class NetWorkSendQuestion {
    Context context;
    String url;
    MessageDecode messageDecode;
    BeanHoroPersonalInfo beanHoroPersonalInfo;
    String fullJSONData;
    Boolean result=false;

    public NetWorkSendQuestion(Context context,MessageDecode messageDecode) {
        this.context = context;
        this.messageDecode=messageDecode;
        beanHoroPersonalInfo = CUtils.getBirthDetailSharedPrefrence(context);
    }

    public boolean sendDataOnServerAskQuestion() {
        StringRequest jsonObjectRequest = new StringRequest(Request.Method.POST, CGlobalVariables.SENDQUESTION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String saveMessage) {
                        try {
                            JSONArray jsonArray = new JSONArray(saveMessage);
                            JSONObject jsonObject=(JSONObject) jsonArray.get(0);
                            String response=  jsonObject.getString("Result");
                            if(response.equalsIgnoreCase("0")) {
                                Toast.makeText(context, "UnSuccessfull", Toast.LENGTH_SHORT).show();
                            }
                            else {
                                Toast.makeText(context, "Successfull", Toast.LENGTH_SHORT).show();
                                int noOfQuestion = CUtils.getIntData(context, CGlobalVariables.NOOFQUESTIONAVAILABLE, 0);
                                if (noOfQuestion > 0) {
                                    noOfQuestion--;
                                    CUtils.saveIntData(context, CGlobalVariables.NOOFQUESTIONAVAILABLE, noOfQuestion);
                                }
                                Toast.makeText(context, "Question Remains: "+noOfQuestion+"   ", Toast.LENGTH_SHORT).show();
                                result=true;
                            }
                        } catch (Exception jsonException) {
                            /*isSuccessfull = false;*/
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error==null) {
                    //Log.e("Error on SENDQUESTION", error.getMessage());
                } else {
                    //Log.e("Error on SENDQUESTION", "Null VolleyError");
                    Toast.makeText(context, "SENDQUESTION error \n (Null Volly Error)", Toast.LENGTH_SHORT).show();
                }
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
                params.put("problem", messageDecode.getMessageText());
                params.put(KEY_EMAIL_ID, CUtils.replaceEmailChar(UserEmailFetcher.getEmail(context)));
              /*  params.put("Day",""+beanHoroPersonalInfo.getDateTime().getDay());
                params.put("Month",""+beanHoroPersonalInfo.getDateTime().getMonth());
                params.put("Year",""+beanHoroPersonalInfo.getDateTime().getYear());
                params.put("Hour",""+beanHoroPersonalInfo.getDateTime().getHour());
                params.put("Min",""+beanHoroPersonalInfo.getDateTime().getMin());
                params.put("Place",""+beanHoroPersonalInfo.getPlace().getCityName());
                params.put("Longitude",""+beanHoroPersonalInfo.getPlace().getLongitude());
                params.put("Latitude",""+beanHoroPersonalInfo.getPlace().getLatitude());
                params.put("Timezone",""+beanHoroPersonalInfo.getPlace().getTimeZone());*/
                params.put("Type","update");
                return params;
            }
        };

        VolleySingleton.getInstance(context).getRequestQueue().add(jsonObjectRequest);
        return result;
    }
}