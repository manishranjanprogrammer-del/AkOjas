package com.ojassoft.astrosage.utils;

import java.io.IOException;

import org.apache.http.Header;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.HttpResponse;
import org.apache.http.HttpResponseInterceptor;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

/**
 * This class provides http client that store session *
 *
 * @author Ojassoft
 * @version 1.0.0
 * @date 06-August-2014
 */
public class MyHTTPClient {


    /* Variables*/
    private static DefaultHttpClient client;
    private static String sessionId;
    private static MyHTTPClient myHTTPClients;

    private MyHTTPClient() {

    }

    /**
     * This function return http client
     *
     * @return DefaultHttpClient
     */
    public static DefaultHttpClient getDefaultHttpClient() {
        if (client == null) {
            /*client = new DefaultHttpClient();*/
            client = new DefaultHttpClient(getHttpClientTimeoutParameter());//ADDED BY BIJENDRA ON 19-09-14
            myHTTPClients = new MyHTTPClient();
            client.addResponseInterceptor(myHTTPClients.new StoreSession());
            client.addRequestInterceptor(myHTTPClients.new AddSession());
        }

        return client;
    }

    /**
     * This function prepare and return time out parameter for
     * online connection.
     *
     * @return HttpParams
     */
    static HttpParams getHttpClientTimeoutParameter() {
        HttpParams httpParameters = new BasicHttpParams();
        // Set the timeout in milliseconds until a connection is established.
        //int timeoutConnection = 1000 * 60;//DISABLED BY BIJENDRA ON 22-09-14
        int timeoutConnection = 1000 * 30;//ADDED BY BIJENDRA ON 22-09-14
        HttpConnectionParams.setConnectionTimeout(httpParameters,
                timeoutConnection);
        // Set the default socket timeout (SO_TIMEOUT)
        // in milliseconds which is the timeout for waiting for data.
        //int timeoutSocket = 1000 * 60;//DISABLED BY BIJENDRA ON 22-09-14
        int timeoutSocket = 1000 * 30;//ADDED BY BIJENDRA ON 22-09-14
        HttpConnectionParams.setSoTimeout(httpParameters, timeoutSocket);

        return httpParameters;
    }

    /**
     * This class is used to add session id on server request
     *
     * @author ojas
     * @date 06-August-2014
     */
    private class AddSession implements HttpRequestInterceptor {

        @Override
        public void process(HttpRequest request, HttpContext context)
                throws HttpException, IOException {
            if (sessionId != null) {
                request.setHeader("Cookie", sessionId);
                // CUtil.showLogMessage("REQUEST", sessionId);

            }
        }

    }

    /**
     * This class is used to store  session id get from server
     *
     * @author ojas
     * @date 06-August-2014
     */
    private class StoreSession implements HttpResponseInterceptor {

        @Override
        public void process(HttpResponse response, HttpContext context)
                throws HttpException, IOException {
            Header[] headers = response.getHeaders("Set-Cookie");
            if (headers != null && headers.length == 1) {
                sessionId = headers[0].getValue();
                // CUtil.showLogMessage("StoreSession", sessionId);
            }
        }

    }

    public static void removeMyHTTPClient() {
        sessionId = "";//REMOVE COOKIE ADDED BY BIJENDRA ON 26-08-14
        client = null;
        myHTTPClients = null;

    }
}


