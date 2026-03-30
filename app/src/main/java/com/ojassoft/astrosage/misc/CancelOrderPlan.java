package com.ojassoft.astrosage.misc;

import android.content.Context;



import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_USER_ID;

/**
 * Created by ojas-10 on 22/7/16.
 */
public class CancelOrderPlan  {
    String deviceID="",orderID="";
    //String requestURL ="https://api2.astrosage.com/ac/cloud-plan-verification/cancel-cloud-plan-v1.asp";
    Context context;
    CancelOrderPlan(Context context,String orderID,String deviceID){
        this.context=context;
        this.deviceID=deviceID;
        this.orderID=orderID;
    }
    public void sendReportToServer(){
        /*try {
            sendPostRequest();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
    }

   /* private String sendPostRequest()
            throws IOException {
        InputStream inputStream = null;
        String result = "",useridentity="useridentity",sendKey="key";


        try {

            URL url = new URL(requestURL);
            HttpClient hc = CUtils.getNewHttpClient();
            HttpPost hp = new HttpPost(requestURL);


                BufferedReader in = null;
                String userid=UserEmailFetcher.getEmail(context);
                String key=CUtils.getApplicationSignatureHashCode(context);
                hp.setEntity(new UrlEncodedFormEntity(getNameValuePairs_Signup(
                        userid, key), HTTP.UTF_8));
                HttpResponse response = hc.execute(hp);
                in = new BufferedReader(new InputStreamReader(response.getEntity()
                        .getContent()));
                StringBuffer sb = new StringBuffer("");
                String data = "";
                while ((data = in.readLine()) != null)
                    sb.append(data);

                in.close();
                result = sb.toString();
                JSONArray jsonArray=new JSONArray(result);
                JSONObject jsonObject=jsonArray.getJSONObject(0);
                String successResult=jsonObject.getString("Result");
                if(successResult.equalsIgnoreCase("1")) {

                }
                else if(successResult.equalsIgnoreCase("0")) {

                }
        } catch (Exception ex) {
            Log.e("EXCEPTION::", "sendPostRequest: "+ex.getMessage() );
        } finally {
            if (httpConn != null) {
               // httpConn.disconnect();
            }
        }

        return result;
    }*/
    /*private List<NameValuePair> getNameValuePairs_Signup(String userid,
                                                           String key) {
        List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
        nameValuePairs.add(new BasicNameValuePair(KEY_USER_ID, CUtils.replaceEmailChar(userid)));
        nameValuePairs.add(new BasicNameValuePair("key", key));
        nameValuePairs.add(new BasicNameValuePair("orderid", orderID));
        nameValuePairs.add(new BasicNameValuePair("deviceid", deviceID));

        return nameValuePairs;

    }*/
}
