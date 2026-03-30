package com.ojassoft.astrosage.ui.act;

import static android.view.View.VISIBLE;
import static com.ojassoft.astrosage.ui.act.ActAppModule.LANGUAGE_CODE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_AS_USER_ID;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.model.BigHorscopeProductModel;
import com.ojassoft.astrosage.model.BigHorscopeServiceModel;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.ui.fragments.FreeBrihatKundliDownloadFragment;
import com.ojassoft.astrosage.ui.fragments.VerifyEmailFragment;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.VolleySingleton;
import com.ojassoft.astrosage.varta.utils.CustomProgressDialog;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Activity for handling Brihat Kundli download process.
 * This activity manages the flow of downloading Brihat Kundli, including:
 * - Product price fetching
 * - Email verification
 * - Download confirmation
 * - Navigation between different fragments
 * 
 * The activity uses a fragment-based navigation system with the following flow:
 * 1. Initial price check
 * 2. Email input and verification
 * 3. Download confirmation
 * 4. Thank you screen
 */
public class BrihatKundliDownload extends BaseInputActivity {
    CustomProgressDialog pd;

    public static String priceInRs;
    public BrihatKundliDownload() {
        super(R.id.app_name);
    }

    /**
     * Activity lifecycle method. Sets up the Brihat Kundli download screen.
     * Initializes the UI and starts the product price fetch process.
     * Sets up back press handling to navigate to ActAppModule when back stack is empty.
     * Includes exception handling for robust startup.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.brihat_kundli_download_act_layout);
        CUtils.fcmAnalyticsEvents(CGlobalVariables.BRIHAT_KUNDLI_FROM_SHORTCUT_EVENT, CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "From_Shortcut");
        pd = new CustomProgressDialog(this);
        
        // Add back press callback
        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                try {
                    if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
                        getSupportFragmentManager().popBackStack();
                    } else {
                        Intent intent = new Intent(BrihatKundliDownload.this, ActAppModule.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                    }
                } catch (Exception e) {
                    Log.e("BrihatKundliDownload", "handleOnBackPressed: Exception - " + e.getMessage());
                }
            }
        });

        try {
           getProductPriceFromAPI();
        } catch (Exception e) {
            Log.e("BrihatKundliDownload", "onCreate: Exception occurred -" + e.getMessage());
        }
    }

    /**
     * Navigates to the FreeBrihatKundliDownloadFragment (main download home).
     * This is the initial screen shown after price fetch if user hasn't downloaded before.
     * 
     * @param priceInRs The price of the product in Indian Rupees to display
     */
    public void navigateToBrihatDownloadHome(String priceInRs) {
        try {
            FreeBrihatKundliDownloadFragment fragment = new FreeBrihatKundliDownloadFragment();
            Bundle args = new Bundle();
            args.putString("priceInRs", priceInRs);
            fragment.setArguments(args);
            
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout, fragment)
                    .commit();
        } catch (Exception e) {
            Log.e("BrihatKundliDownload", "navigateToBrihatDownloadHome: Exception - " + e.getMessage());
        }
    }
    /**
     * Navigates to the InputEmailFragment for collecting user email.
     * This is the first step in the download process after price confirmation.
     * Adds the transaction to the back stack for navigation.
     */
    public void navigateToInputEmailFragment() {
        try {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout, new com.ojassoft.astrosage.ui.fragments.InputEmailFragment())
                    .addToBackStack(null)
                    .commit();
        } catch (Exception e) {
            Log.e("BrihatKundliDownload", "navigateToInputEmailFragment: Exception - " + e.getMessage());
        }
    }
    /**
     * Navigates to the ThanksForBrihatDownloadFragment (Thank You screen).
     * This is shown after successful download or if user has already downloaded.
     * Clears the back stack to prevent returning to previous steps.
     * 
     * @param priceInRs The price of the product in Indian Rupees to display
     */
    public void navigateToThankYouFragment(String priceInRs) {
        try {
            // Clear the back stack first
            getSupportFragmentManager().popBackStack(null, getSupportFragmentManager().POP_BACK_STACK_INCLUSIVE);
            
            // Create fragment and set arguments
            com.ojassoft.astrosage.ui.fragments.ThanksForBrihatDownloadFragment fragment = 
                new com.ojassoft.astrosage.ui.fragments.ThanksForBrihatDownloadFragment();
            Bundle args = new Bundle();
            args.putString("priceInRs", priceInRs);
            fragment.setArguments(args);
            
            // Then add the ThanksForBrihatDownloadFragment without adding it to the back stack
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout, fragment)
                    .commit();
        } catch (Exception e) {
            Log.e("BrihatKundliDownload", "navigateToThankYouFragment: Exception - " + e.getMessage());
        }
    }

    /**
     * Navigates to the VerifyEmailFragment for OTP/email verification.
     * This is the second step in the download process after email input.
     * 
     * @param email The user email to verify (must not be null)
     * @throws IllegalArgumentException if email is null
     */
    public void navigateToVerifyEmailFragment(String email) {
        if (email == null) {
            Toast.makeText(this, "Email is required for verification.", Toast.LENGTH_SHORT).show();
            Log.e("BrihatKundliDownload", "navigateToVerifyEmailFragment: email is null");
            return;
        }
        try {
            Bundle bundle = new Bundle();
            bundle.putString("email", email.trim());
            Fragment verifyEmailFragment = new VerifyEmailFragment();
            verifyEmailFragment.setArguments(bundle);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.frame_layout, verifyEmailFragment)
                    .addToBackStack(null)
                    .commit();
        } catch (Exception e) {
            Log.e("BrihatKundliDownload", "navigateToVerifyEmailFragment: Exception - " + e.getMessage());
        }
    }

    /**
     * Parses the API response data for product price information.
     * Updates the UI with the price and navigates to appropriate screen based on download status.
     * 
     * @param saveData The JSON response string from the API containing product details
     */
    private void parseGsonData(String saveData) {
        try {
            BigHorscopeServiceModel data;
            Gson gson = new Gson();
            JsonElement element = gson.fromJson(saveData.toString(), JsonElement.class);
            data = gson.fromJson(saveData, BigHorscopeServiceModel.class);
            if (data != null) {
                boolean alreadyDownloaded = CUtils.getBooleanData(this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FREE_BRIHAT_ALREADY_DOWNLOADED_KEY, false);
                priceInRs = data.getPriceInRS();
                if (alreadyDownloaded) {
                    navigateToThankYouFragment(priceInRs);
                } else {
                    navigateToBrihatDownloadHome(priceInRs);
                }
            } else {
                MyCustomToast mct = new MyCustomToast(this, this.getLayoutInflater(), this, this.regularTypeface);
                mct.show("Data is not avialable");
            }
        }catch(Exception e){
            Log.e("FreeBrihatKundliDownloadFragment", "parseGsonData: Exception-"+e.getMessage() );
        }
    }

    /**
     * Fetches the product price from the API.
     * Makes a POST request to get current pricing and product details.
     * Shows progress dialog during the request.
     * Handles various network errors and timeouts.
     * Includes retry policy for network resilience.
     */
    public void getProductPriceFromAPI(){
        try {
            if(pd!=null){
                pd.show();
            }
            RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, CGlobalVariables.brihatHorscopeWebURl,
                    response -> {
                        hideProgress();
                        if (response != null && !response.isEmpty()) {
                            try {
                                String str = new String(response.getBytes("ISO-8859-1"), "UTF-8");
                                JSONArray jsonArray = new JSONArray(str);
                                JSONObject objService = jsonArray.getJSONObject(0);
                                parseGsonData(objService.toString());
                            } catch (Exception e) {
                                MyCustomToast mct = new MyCustomToast(this, this.getLayoutInflater(), this, this.regularTypeface);
                                mct.show(getResources().getString(R.string.something_wrong_error));
                                this.finish();
                                e.printStackTrace();
                            }

                        }
                    }, error -> {
                hideProgress();
                MyCustomToast mct = new MyCustomToast(this, this.getLayoutInflater(), this, this.regularTypeface);
                mct.show(getResources().getString(R.string.something_wrong_error));

                if (error instanceof TimeoutError) {
                    VolleyLog.d("TimeoutError: " + error.getMessage());
                } else if (error instanceof NoConnectionError) {
                    VolleyLog.d("NoConnectionError: " + error.getMessage());
                } else if (error instanceof AuthFailureError) {
                    VolleyLog.d("AuthFailureError: " + error.getMessage());
                } else if (error instanceof ServerError) {
                    VolleyLog.d("ServerError: " + error.getMessage());
                } else if (error instanceof NetworkError) {
                    VolleyLog.d("NetworkError: " + error.getMessage());
                } else if (error instanceof ParseError) {
                    VolleyLog.d("ParseError: " + error.getMessage());
                }
                finish();
            }) {
                @Override
                public String getBodyContentType() {
                    return "application/x-www-form-urlencoded; charset=UTF-8";
                }

                /**
                 * Passing some request headers
                 */
                @Override
                public Map<String, String> getParams() {
                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("key", CUtils.getApplicationSignatureHashCode(BrihatKundliDownload.this));
                    headers.put("languagecode", "" + LANGUAGE_CODE);
                    // parameter added for discount
                    headers.put(KEY_AS_USER_ID, CUtils.replaceEmailChar(CUtils.getUserName(BrihatKundliDownload.this)));
                    headers.put("asuserplanid", String.valueOf(CUtils.getUserPurchasedPlanFromPreference(BrihatKundliDownload.this)));
                    return headers;
                }

            };
            int socketTimeout = 30000;//30 seconds - change to what you want
            RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
            stringRequest.setRetryPolicy(policy);
            stringRequest.setShouldCache(true);
            queue.add(stringRequest);
        }catch (Exception e){
            hideProgress();
            Log.e("BrihatKundliDownload", "getProductPriceFromAPI: Exception-"+e.getMessage());
        }
    }

    /**
     * Safely dismisses the progress dialog.
     * Includes null checks and exception handling to prevent crashes.
     */
    private void hideProgress(){
        try{
            if(pd!=null && pd.isShowing()){
                pd.dismiss();
            }
        }catch (Exception e){
            Log.e("BrihatKundliDownload", "hideProgress: exception-"+e.getMessage() );
        }
    }

}
