package com.ojassoft.astrosage.utils;


import com.android.volley.VolleyError;
import com.libojassoft.android.misc.SendDataBackToComponent;
import com.libojassoft.android.misc.VolleyResponse;

import java.io.IOException;
import java.util.Map;

/**
 * Created by Amit Rautela on 28/4/15.
 */
public class HttpUtility implements VolleyResponse {

    /**
     * Represents an HTTP connection
     */
    private static String tag = "HttpUtility";
    SendDataBackToComponent sendDataBackToComponent;

    public HttpUtility(SendDataBackToComponent context) {
        sendDataBackToComponent = context;
    }


    /**
     * Makes an HTTP request using POST method to the specified URL.
     *
     * @param requestURL the URL of the remote server
     * @param params     A map containing POST data in form of key-value pairs
     * @return String data
     * @throws IOException thrown if any I/O error occurred
     */
    /*public static String sendPostRequest1(String requestURL, Map<String, String> params) throws IOException {

        String data = "";
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

                // sends POST data
                OutputStreamWriter writer = new OutputStreamWriter(httpConn.getOutputStream());
                writer.write(requestParams.toString());
                writer.flush();
            }

            data = readMultipleLineResponse(httpConn);
        } catch (Exception ex) {
            Log.i("Tag", "1 - " + ex.getMessage().toString());
        } finally {
            *//**
     * Closes the connection if opened
     *//*
            if (httpConn != null) {
                httpConn.disconnect();
            }
        }

        return data;
    }*/

    /**
     * Returns an array of lines from the server's response. This method should
     * be used if the server returns multiple lines of String.
     *
     * @return String data of the server's response
     * @throws IOException thrown if any I/O error occurred
     */
   /* public static String readMultipleLineResponse(HttpURLConnection httpConn) throws IOException {

        InputStream inputStream = null;
        String result = "";

        try {

            if (httpConn != null) {
                try {

                    inputStream = httpConn.getInputStream();

                } catch (Exception ex) {
                    //
                }
            } else {
                throw new IOException("Connection is not established.");
            }

            // convert response to string
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"), 8);//iso-8859-1
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                    // sb.append(URLDecoder.decode(line + "\n","UTF-8"));
                }
                inputStream.close();
                result = sb.toString();
            } catch (Exception e) {
                // Log.e("log_tag get data string ", "Error converting result " + e.toString());
            }
        } catch (Exception e) {
            Log.i(tag, "error 2 -> " + e.getMessage());
        }

        return result;
    }*/
    public void sendPostRequest(String requestURL, Map<String, String> params, int method) {
        CUtils.vollyPostRequest(this, requestURL, params, method);
    }

    @Override
    public void onResponse(String response, int method) {
        sendDataBackToComponent.doActionAfterGetResult(response, method);
    }

    @Override
    public void onError(VolleyError error) {
        if (sendDataBackToComponent != null && error != null) {
            sendDataBackToComponent.doActionOnError(error.getMessage());
        }
    }
}