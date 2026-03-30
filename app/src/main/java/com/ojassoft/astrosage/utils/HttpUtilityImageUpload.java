package com.ojassoft.astrosage.utils;


import android.content.Context;
import android.util.Log;

import com.android.volley.VolleyError;
import com.libojassoft.android.misc.SendDataBackToComponent;
import com.libojassoft.android.misc.VolleyResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by Amit Rautela on 28/4/15.
 */
public class HttpUtilityImageUpload {

    /**
     * Represents an HTTP connection
     */


    private static String tag ="HttpUtility";

    private static XmlPullParserFactory xmlFactoryObject;


    /**
     * Makes an HTTP request using GET method to the specified URL.
     *
     * @param requestURL
     *            the URL of the remote server
     * @return An HttpURLConnection object
     * @throws IOException
     *             thrown if any I/O error occurred
     */
    public static JSONArray sendGetRequest(String requestURL, Context context)
            throws IOException {

        JSONArray jsonArray = null;
        HttpURLConnection httpConn = null;

        try {
            URL url = new URL(requestURL);
            httpConn = (HttpURLConnection) url.openConnection();

            httpConn.setConnectTimeout(15000);

            httpConn.setReadTimeout(15000);

            httpConn.setUseCaches(false);

            httpConn.setDoInput(true); // true if we want to read server's response
            httpConn.setDoOutput(false); // false indicates this is a GET request

            jsonArray = readMultipleLineResponse(context, 0, httpConn);

        }catch (Exception ex){

        }finally {
            /**
             * Closes the connection if opened
             */
            httpConn.disconnect();
        }

        return jsonArray;
    }


    /**
     * Makes an HTTP request using POST method to the specified URL.
     *
     * @param requestURL
     *            the URL of the remote server
     * @param params
     *            A map containing POST data in form of key-value pairs
     * @return An HttpURLConnection object
     * @throws IOException
     *             thrown if any I/O error occurred
     */
    public static JSONArray sendPostRequest(String requestURL,Map<String, String> params,Context context,int screenId) throws IOException {

        JSONArray jsonArray = null;
        HttpURLConnection httpConn = null;

        try {

            URL url = new URL(requestURL);

            httpConn = (HttpURLConnection) url.openConnection();

            httpConn.setConnectTimeout(15000);

            httpConn.setReadTimeout(15000);

            httpConn.setUseCaches(false);

            httpConn.setDoInput(true); // true indicates the server returns response

            StringBuffer requestParams = new StringBuffer();

            if (params != null && params.size() > 0) {

                httpConn.setDoOutput(true); // true indicates POST request

                // creates the params string, encode them using URLEncoder
                Iterator<String> paramIterator = params.keySet().iterator();
                int i = 0;
                while (paramIterator.hasNext()) {
                    if(i > 0 && i < params.size()){
                        requestParams.append("&");
                    }
                    i++;
                    String key = paramIterator.next();
                    String value = params.get(key);
                    requestParams.append(URLEncoder.encode(key, "UTF-8"));
                    requestParams.append("=").append(URLEncoder.encode(value, "UTF-8"));
                }

                // sends POST data
                OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream());
                writer.write(requestParams.toString());
                writer.flush();
            }
            Log.e("log_tag", "requestParams " + requestParams.toString());
            jsonArray = readMultipleLineResponse(context, screenId, httpConn);

        }
        catch (Exception ex){
            Log.i("Tag","1 - "+ex.getMessage().toString());
        }finally {
            /**
             * Closes the connection if opened
             */
            httpConn.disconnect();
        }

        return jsonArray;
    }

    /**
     * Returns an array of lines from the server's response. This method should
     * be used if the server returns multiple lines of String.
     *
     * @return an array of Strings of the server's response
     * @throws IOException
     *             thrown if any I/O error occurred
     */
    public  static JSONArray readMultipleLineResponse(Context context, int screenId, HttpURLConnection httpConn) throws IOException {

        InputStream inputStream = null;
        JSONArray jArray = null;
        String result = "";

        try {

            if (httpConn != null) {
                try {

                    inputStream = httpConn.getInputStream();
                    CUtils.saveStringData(context, "Cookie_Login", httpConn.getHeaderFields().get("Set-Cookie").toString());

                } catch (Exception ex) {
                    //
                }
            } else {
                throw new IOException("Connection is not established.");
            }

            // convert response to string
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"),8);//iso-8859-1
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                    // sb.append(URLDecoder.decode(line + "\n","UTF-8"));
                }
                Log.e("log_tag", "converting result " + sb.toString());
                inputStream.close();
                result = sb.toString();
            } catch (Exception e) {
                Log.e("log_tag", "Error converting result " + e.toString());
            }


            // CUtils.setMessage(context,"Result -> "+result.toString());

            try {
                jArray = new JSONArray(result);

                //Method to get write file
                /*if(screenId == CConstantVariables.MyContest_Screen) {
                    try {

                        File myFile = new File("/sdcard/myerrorfile.txt");
                        myFile.createNewFile();
                        FileOutputStream fOut = new FileOutputStream(myFile);
                        OutputStreamWriter myOutWriter =
                                new OutputStreamWriter(fOut);
                        myOutWriter.append(result);
                        myOutWriter.close();
                        fOut.close();

                    } catch (Exception ex) {
                        Log.i(tag, "error 1.1 -> " + ex.getMessage().toString());
                    }
                }*/

            } catch (JSONException e) {
                Log.i(tag,"error 1 -> "+e.getMessage().toString());
            }
        }
        catch (Exception e) {
            Log.i(tag,"error 2 -> "+e.getMessage().toString());
        }

        return jArray;
    }


    /**
     * Returns an array of lines from the server's response. This method should
     * be used if the server returns multiple lines of String.
     *
     * @return a String of the server's response
     * @throws IOException
     *             thrown if any I/O error occurred
     */

    public static String getFeedbackResponse(String requestURL,Map<String, String> params,Context context,int screenId) throws IOException {
        InputStream inputStream = null;
        String feedbackResponse=null;
        HttpURLConnection httpConn = null;


        try {

            URL url = new URL(requestURL);

            httpConn = (HttpURLConnection) url.openConnection();

            httpConn.setConnectTimeout(15000);

            httpConn.setReadTimeout(15000);

            httpConn.setUseCaches(false);

            httpConn.setDoInput(true); // true indicates the server returns response

            if (!CUtils.getStringData(context, "Cookie_Login", "").equals("")) {

                String cookie = CUtils.getStringData(context, "Cookie_Login", "");
                httpConn.addRequestProperty("Cookie", cookie.split(";", 1)[0]);
            }

            StringBuffer requestParams = new StringBuffer();

            if (params != null && params.size() > 0) {

                httpConn.setDoOutput(true); // true indicates POST request

                // creates the params string, encode them using URLEncoder
                Iterator<String> paramIterator = params.keySet().iterator();
                int i = 0;
                while (paramIterator.hasNext()) {
                    if (i > 0 && i < params.size()) {
                        requestParams.append("&");
                    }
                    i++;
                    String key = paramIterator.next();
                    String value = params.get(key);
                    requestParams.append(URLEncoder.encode(key, "UTF-8"));
                    requestParams.append("=").append(
                            URLEncoder.encode(value, "UTF-8"));
                }

                // Log.i("params",requestParams.toString());
                // CUtils.setMessage(context,"Requested params -> "+requestParams.toString());

                // sends POST data
                OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream());
                writer.write(requestParams.toString());
                writer.flush();
            }
        }catch (Exception ex){

        }


        try {

            if (httpConn != null) {
                try {

                    inputStream = httpConn.getInputStream();
                    xmlFactoryObject = XmlPullParserFactory.newInstance();
                    XmlPullParser myparser = xmlFactoryObject.newPullParser();

                    myparser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
                    myparser.setInput(inputStream, null);
                    feedbackResponse=parseXMLAndStoreIt(myparser);
                    //Log.i("Resop", feedbackResponse);
                    inputStream.close();
                } catch (Exception ex) {
//
                }
            } else {
                throw new IOException("Connection is not established.");
            }

        }catch (Exception e){

        }finally {
            /**
             * Closes the connection if opened
             */
            httpConn.disconnect();
        }

        return feedbackResponse;
    }


    public static String parseXMLAndStoreIt(XmlPullParser myParser) {
        int event;
        String msg=null;
        String text=null;


        try {
            event = myParser.getEventType();

            while (event != XmlPullParser.END_DOCUMENT) {
                String name=myParser.getName();

                switch (event){
                    case XmlPullParser.START_TAG:
                        break;

                    case XmlPullParser.TEXT:
                        text = myParser.getText();
                        break;

                    case XmlPullParser.END_TAG:
                        if(name.equals("msg")){
                            msg = text;
                        }


                        break;
                }
                event = myParser.next();
            }
        }

        catch (Exception e) {
            e.printStackTrace();
        }

        return msg;
    }

    /**
     * Closes the connection if opened
     */
   /* public synchronized static void disconnect() {

        if (httpConn != null) {
            httpConn.disconnect();
        }
    }*/
}