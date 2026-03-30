package com.ojassoft.astrosage.varta.utils;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.Map;

public class HttpUtility {

    /**
     * Represents an HTTP connection
     */
    private static String tag ="HttpUtility";


    /**
     * Makes an HTTP request using GET method to the specified URL.
     *
     * @param requestURL
     *            the URL of the remote server
     * @return An HttpURLConnection object
     * @throws IOException
     *             thrown if any I/O error occurred
     */
    public static synchronized String sendGetRequest(String requestURL)
            throws IOException {

        String data = "";
        HttpURLConnection httpConn = null;

        try {
            URL url = new URL(requestURL);
            httpConn = (HttpURLConnection) url.openConnection();

            httpConn.setConnectTimeout(15000);

            httpConn.setReadTimeout(15000);

            httpConn.setUseCaches(false);

            httpConn.setDoInput(true); // true if we want to read server's response
            httpConn.setDoOutput(false); // false indicates this is a GET request

            data = readMultipleLineResponse(httpConn);

        }catch (Exception ex){

        }finally {
            /**
             * Closes the connection if opened
             */
            httpConn.disconnect();
        }

        return data;
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
    public static synchronized String sendPostRequest(String requestURL, Map<String, String> params) throws IOException {

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
                    if(i > 0 && i < params.size()){
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


            //return httpConn;
            data = readMultipleLineResponse(httpConn);

        }
        catch (Exception ex){
            Log.i("Tag","1 - "+ex.getMessage().toString());
        }finally {
            /**
             * Closes the connection if opened
             */
            httpConn.disconnect();
        }

        return data;
    }

    /**
     * Returns an array of lines from the server's response. This method should
     * be used if the server returns multiple lines of String.
     *
     * @return an array of Strings of the server's response
     * @throws IOException
     *             thrown if any I/O error occurred
     */
    public  static String readMultipleLineResponse(HttpURLConnection httpConn) throws IOException {

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
                BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"),8);//iso-8859-1
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                inputStream.close();
                result = sb.toString();
            } catch (Exception e) {
                Log.i(tag,"error-> "+e.getMessage().toString());
            }
        }
        catch (Exception e) {
            Log.i(tag,"error 2 -> "+e.getMessage().toString());
        }

        return result;
    }

    public static synchronized String sendPostRequestXForm(String requestURL, String params) {
        String[] data = new String[1];
        data[0] = "";
//        Log.d ( "MyTag" , "current Thread: "+Thread.currentThread ().getName () );
       Thread t= new Thread(() -> {
            try {
                byte[] postData = params.getBytes(StandardCharsets.UTF_8);
                int postDataLength = postData.length;
                URL url = new URL(requestURL);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setDoOutput(true);
                conn.setInstanceFollowRedirects(false);
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                conn.setRequestProperty("charset", "utf-8");
                conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
                conn.setUseCaches(false);
                DataOutputStream wr = new DataOutputStream(conn.getOutputStream());
                wr.write(postData);
                data[0] = readMultipleLineResponse(conn);
//                Log.d ( "MyTag" , "sendPostRequestXForm data[0]: "+data[0] );
            } catch (Exception e) {
                //
            }
        });
        t.start ();
        try {
            t.join ();
        } catch (InterruptedException e) {
            throw new RuntimeException ( e );
        }

        return data[0];
    }

}