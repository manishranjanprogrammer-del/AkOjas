package com.ojassoft.astrosage.misc;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.VolleySingleton;
import com.ojassoft.astrosage.varta.volley_network.VolleyServiceHandler;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * A background service dedicated to fetching Kundli AI and Dhruv plan service details from the server.
 * <p>
 * This service performs the following actions:
 * 1. Checks for an internet connection.
 * 2. Makes a POST request to the SERVICE_LIST_KUNDLI_AI_PLANS API.
 * 3. On a successful response, it parses the plan details.
 * 4. Saves the 'Kundli AI+' and 'Dhruv' plan details into SharedPreferences.
 * 5. Sends a LocalBroadcast with the fetched data for any active UI to receive.
 * 6. Stops itself once the task is complete.
 * </p>
 */
public class FetchPlanDetailsService extends Service implements com.ojassoft.astrosage.varta.volley_network.VolleyResponse {

    private static final String TAG = "FetchPlanDetailsService";
    private static final int GET_SERVICE_DETAILS_METHOD_ID = 101; // Unique ID for this service's request

    private RequestQueue requestQueue;

    @Override
    public void onCreate() {
        super.onCreate();
        requestQueue = VolleySingleton.getInstance(this).getRequestQueue();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "Service started. Fetching plan details...");
        fetchPlanDetailsFromServer();
        // Return START_NOT_STICKY because this service should not be recreated if killed.
        // It runs, completes its task, and stops.
        return START_NOT_STICKY;
    }

    /**
     * Initiates the network request to fetch plan details if an internet connection is available.
     * If not, it logs the issue and stops the service.
     */
    private void fetchPlanDetailsFromServer() {
        if (!CUtils.isConnectedWithInternet(this)) {
            Log.w(TAG, "No internet connection. Stopping service.");
            stopSelf(); // Stop the service if there's no internet.
            return;
        }

        String url = com.ojassoft.astrosage.varta.utils.CGlobalVariables.SERVICE_LIST_KUNDLI_AI_PLANS;
        Log.d(TAG, "Requesting URL: " + url);

        // Create the Volley request using the existing handler structure

        requestQueue = com.ojassoft.astrosage.varta.volley_network.VolleySingleton.getInstance(this).getRequestQueue();
         StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, url,
                FetchPlanDetailsService.this, false, CUtils.planDetailsRequestParams(this), GET_SERVICE_DETAILS_METHOD_ID).getMyStringRequest();
        stringRequest.setShouldCache(false);
        requestQueue.add(stringRequest);
    }

    @Override
    public void onResponse(String response, int method) {
        if (method == GET_SERVICE_DETAILS_METHOD_ID) {
            Log.d(TAG, "Successfully received plan details response.");
            try {
                // Parse the JSON response
                JSONArray array = new JSONArray(response);
                JSONObject plusService = array.getJSONObject(0);     // Kundli AI+ plan service
                JSONObject premiumService = array.getJSONObject(1);  // Dhruv plan service

                // Save the plan details to SharedPreferences using the existing utility methods
                com.ojassoft.astrosage.varta.utils.CUtils.setPlusPlanServiceDetail(plusService.toString());
                com.ojassoft.astrosage.varta.utils.CUtils.setPremiumPlanServiceDetail(premiumService.toString());

                // Broadcast the results for any listening activities
                broadcastPlanDetails(plusService.toString(), premiumService.toString());

            } catch (Exception e) {
                Log.e(TAG, "Error parsing plan details JSON response.", e);
                Toast.makeText(this, "Failed to parse plan data.", Toast.LENGTH_LONG).show();
            } finally {
                // Ensure the service stops itself after processing the response.
                stopSelf();
            }
        }
    }

    @Override
    public void onError(VolleyError error) {
        Log.e(TAG, "Volley error while fetching plan details.", error);
        // Stop the service even if there is an error.
        stopSelf();
    }

    /**
     * Sends the fetched plan details via a LocalBroadcastManager intent.
     *
     * @param plusPlanDetails    The JSON string for the Kundli AI+ plan.
     * @param premiumPlanDetails The JSON string for the Dhruv (Premium) plan.
     */
    private void broadcastPlanDetails(String plusPlanDetails, String premiumPlanDetails) {
        Intent intent = new Intent(CGlobalVariables.SERVICE_DETAILS_BROADCAST);
        intent.putExtra("plusPlanServiceDetails", plusPlanDetails);
        intent.putExtra("premiumPlanServiceDetails", premiumPlanDetails);
        LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
        Log.d(TAG, "Broadcast sent with updated plan details.");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        // This is a started service, not a bound one, so return null.
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Service stopped.");
    }
}
