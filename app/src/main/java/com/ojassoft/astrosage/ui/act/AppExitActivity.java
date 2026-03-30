package com.ojassoft.astrosage.ui.act;


import static com.ojassoft.astrosage.ui.act.BaseInputActivity.SUB_ACTIVITY_EXIT_PLAN;
import static com.ojassoft.astrosage.varta.utils.CUtils.getReducedPriceRechargeDetails;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.google.android.material.snackbar.Snackbar;
import com.libojassoft.android.utils.LibCUtils;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.misc.DownloadSourceAdapter;
import com.ojassoft.astrosage.model.DownloadSourceModel;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.varta.service.Loginservice;
import com.ojassoft.astrosage.varta.service.PreFetchDataservice;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.ui.activity.BaseActivity;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
/**
 * <h1>AppExitActivity</h1>
 * <p>
 * This activity is displayed when the user attempts to exit a specific flow or the application.
 * It serves two main purposes:
 * <ol>
 *     <li><b>Data Collection:</b> It prompts the user to select their "Source of Registration" (e.g., Facebook, WhatsApp, Friend) for marketing analytics.</li>
 *     <li><b>Retention Strategy:</b> It checks if the user is eligible for a "Reduced Price Offer" (5 Rupee Offer). If eligible, it attempts to fetch offer details to potentially display them (via {@code parseResponse}) to retain the user.</li>
 * </ol>
 * </p>
 */

public class AppExitActivity extends BaseActivity implements View.OnClickListener {


    // UI Components
    private TextView tvCancel, tvSubmit, tvTitle;
    private EditText etOthers;
    private RecyclerView recyclerView;
    private ImageView ivBack;
    private RelativeLayout containerLayout;
    private LinearLayout llOthers;
    CustomProgressDialog pd;

    // Data & Logic
    private RequestQueue queue; // Volley queue instance
    public int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    private String comments = ""; // User input if "Others" is selected, or source name
    private String commentsType; // ID of the selected source
    public Typeface regularTypeface;

    // Adapter components
    ArrayList<DownloadSourceModel> list;
    DownloadSourceAdapter adapter;

    /**
     * Initializes the activity, hides the title bar, checks for retention offers, and sets up the UI.
     *
     * @param savedInstanceState Saved state bundle.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_app_exit);

        // Get the specific offer type assigned to this user
        String offerType = com.ojassoft.astrosage.varta.utils.CUtils.getUserIntroOfferType(this);

        // Check generic flag (controlled by TagManager/Server) if the 5 Rupee offer is globally enabled
        boolean isShow5RupeeOffer = CUtils.getBooleanData(this, com.ojassoft.astrosage.utils.CGlobalVariables.IS_SHOW_FIVE_RUPEE_OFFER, false);

        Log.e("fiveRupeeTest", "onCreate: isShown tagmanager = " + isShow5RupeeOffer);

        // If offer is enabled globally, user is logged in, and user's offer type matches the reduced price criteria
        if (isShow5RupeeOffer && CUtils.getUserLoginStatus(this) && offerType.contains(CGlobalVariables.REDUCED_PRICE_OFFER)) {
            // Check if we have cached offer details
            String cachedOfferResponse = CUtils.getStringData(this, com.ojassoft.astrosage.utils.CGlobalVariables.RECHARGE_SERVICE_OFFER_RESPONSE_KEY, "");

            if (!TextUtils.isEmpty(cachedOfferResponse)) {
                Log.e("fiveRupeeTest", "onCreate: offer from cache: " + cachedOfferResponse);
                parseResponse(cachedOfferResponse);
            } else {
                // Fetch fresh offer details from server if not cached
                getReducedPriceRechargeDetails(this, response -> {
                    Log.e("fiveRupeeTest", "onCreate: offer from server: " + response);
                    parseResponse(response);
                });
            }
        }
        // -----------------------------

        // --- UI Initialization ---
        LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication()).getLanguageCode();

        tvTitle = findViewById(R.id.tvTitle);
        ivBack = findViewById(R.id.ivBack);
        etOthers = findViewById(R.id.et_others);
        containerLayout = findViewById(R.id.container_layout);
        tvCancel = findViewById(R.id.tv_cancel);
        tvSubmit = findViewById(R.id.tv_submit);
        recyclerView = findViewById(R.id.recycler_view);
        etOthers = findViewById(R.id.et_others);
        llOthers = findViewById(R.id.ll_others);
        FontUtils.changeFont(this, tvTitle, com.ojassoft.astrosage.utils.CGlobalVariables.FONTS_ROBOTO_MEDIUM);
        FontUtils.changeFont(this, tvCancel, com.ojassoft.astrosage.utils.CGlobalVariables.FONTS_ROBOTO_MEDIUM);
        FontUtils.changeFont(this, tvSubmit, com.ojassoft.astrosage.utils.CGlobalVariables.FONTS_ROBOTO_MEDIUM);
        setList();
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        tvTitle.setText(R.string.registration_source);
        initListners();
    }

    /**
     * Initializes click listeners and the RecyclerView adapter.
     */
    protected void initListners() {
        ivBack.setOnClickListener(this);
        tvSubmit.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        adapter = new DownloadSourceAdapter(this,list);
        recyclerView.setLayoutManager(new GridLayoutManager(this,2));
        recyclerView.setAdapter(adapter);
    }


    /**
     * Handles UI click events.
     *
     * @param view The view that was clicked.
     */
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.ivBack:
                finish();
                break;

            case R.id.tv_submit:
                if (commentsType == null) {
                    com.ojassoft.astrosage.varta.utils.CUtils.showSnackbar(containerLayout, getResources().getString(R.string.select_the_source), this);
                    return;
                }else if(commentsType.equals("7")) {
                    comments = etOthers.getText().toString();
                }
                if (comments.isEmpty()) {
                    com.ojassoft.astrosage.varta.utils.CUtils.showSnackbar(containerLayout, getResources().getString(R.string.fill_the_source), this);
                    return;
                }

                sendData();
                break;

            case R.id.tv_cancel:
                exit();
                break;
        }
    }



    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onResume() {
        // TODO Auto-generated method stub
        super.onResume();
    }


    @Override
    public void onError(VolleyError error) {
        hideProgressBar();
    }



    /**
     * Sends the selected registration source data to the backend API.
     * <p>
     * Logic Flow:
     * 1. Check Internet.
     * 2. Make API Call {@code appExitApi}.
     * 3. On Success (Status "1"): Save preference flag {@code USER_AD_EXIT_SCREEN}, show success Snackbar, then exit.
     * 4. On Auth Error (Status "100"): Attempt background login (up to 3 times) to refresh session.
     * 5. On Failure: Show error message.
     * </p>
     */
    private void sendData() {
        showProgressBar();
        if (CUtils.isConnectedWithInternet(this)) {
            ApiList api = RetrofitClient.getInstance2().create(ApiList.class);
            Call<ResponseBody> call = api.appExitApi(getParamsHeader());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        hideProgressBar();
                        String myRespose = response.body().string();
                        JSONObject jObj = new JSONObject(myRespose);
                        String status = jObj.getString(CGlobalVariables.STATUS);
                        String msg = jObj.getString("msg");

                        if (status.equals("1")) {
                            CUtils.saveBooleanData(AppExitActivity.this, CGlobalVariables.USER_AD_EXIT_SCREEN, true);

                            Snackbar snackbar = Snackbar.make(containerLayout, msg, Snackbar.LENGTH_LONG);
                            snackbar.getView().setBackgroundColor(getColor(R.color.colorPrimary_day_night));
                            TextView tv = snackbar.getView().findViewById(com.google.android.material.R.id.snackbar_text);
                            tv.setTextColor(getColor(R.color.white));
                            tv.setTextSize(16);
                            FontUtils.changeFont(AppExitActivity.this, tv, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
                            snackbar.setCallback(new Snackbar.Callback() {

                                @Override
                                public void onDismissed(Snackbar snackbar, int event) {
                                    if (event == Snackbar.Callback.DISMISS_EVENT_TIMEOUT) {
                                        exit();
                                    }
                                }

                                @Override
                                public void onShown(Snackbar snackbar) {
                                    //Do something in shown
                                }
                            });
                            snackbar.show();
                        } else if (status.equals("100")) {
                            if (AstrosageKundliApplication.status100BgLoginCount < 3) {
                                LocalBroadcastManager.getInstance(AppExitActivity.this).registerReceiver(mReceiverBackgroundLoginService, new IntentFilter(com.ojassoft.astrosage.varta.utils.CGlobalVariables.SEND_BROADCAST_BACK_GR_LOGIN));
                                startBackgroundLoginServiceExit(AppExitActivity.this);
                                AstrosageKundliApplication.status100BgLoginCount += 1;
                            }
                        } else {
                            CUtils.showSnackbar(containerLayout, msg, AppExitActivity.this);
                            exit();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    CUtils.showSnackbar(containerLayout, getResources().getString(R.string.something_wrong_error), AppExitActivity.this);
                }
            });
        } else {
            CUtils.showSnackbar(containerLayout, getResources().getString(R.string.no_internet), this);

        }
    }


    /**
     * Constructs the header parameters for the API call.
     * Includes App Version, Device ID, User IDs, and the selected Comment/Source.
     *
     * @return Map of header keys and values.
     */
    public Map<String, String> getParamsHeader() {

        HashMap<String, String> headers = new HashMap<String, String>();
        String deviceId = CUtils.getMyAndroidId(getApplicationContext());
        String key = CUtils.getApplicationSignatureHashCode(this);
        String appVerName = LibCUtils.getApplicationVersionToShow(getApplicationContext());
        String verText = appVerName + "(" + BuildConfig.VERSION_CODE + ")";

        headers.put("appversion", verText);
        headers.put("deviceid", deviceId);
        headers.put("comments", comments);
        headers.put("commentType", commentsType);
        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(this));
        headers.put("astrosageuserid", com.ojassoft.astrosage.utils.CUtils.getUserName(this));
        headers.put("vartauserid", CUtils.getUserIdForBlock(this));
        headers.put(CGlobalVariables.KEY_API, key);

        return headers;
    }
    private void startBackgroundLoginServiceExit(Activity activity) {
        try {
            if (com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(activity)) {
                Intent intent = new Intent(activity, Loginservice.class);
                activity.startService(intent);
            }
        } catch (Exception e) {/**/}
    }
    /**
     * Closes the activity with RESULT_OK.
     */
    private void exit() {
        setResult(RESULT_OK);
        AppExitActivity.this.finish();
    }

    private void showProgressBar() {
        regularTypeface = com.ojassoft.astrosage.utils.CUtils.getRobotoFont(
                getApplicationContext(), LANGUAGE_CODE, com.ojassoft.astrosage.utils.CGlobalVariables.regular);
        if (pd == null) {
            pd = new CustomProgressDialog(this, regularTypeface);
        }
        pd.setCanceledOnTouchOutside(false);
        if (!pd.isShowing()) {
            pd.show();
        }
    }

    private void hideProgressBar() {
        try {
            if (pd != null && pd.isShowing())
                pd.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private final BroadcastReceiver mReceiverBackgroundLoginService = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String status = intent.getStringExtra("status");
                if (status.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.SUCCESS)) {
                    sendData();
                } else {
                    CUtils.showSnackbar(containerLayout, getResources().getString(R.string.something_wrong_error), AppExitActivity.this);
                }
                if (mReceiverBackgroundLoginService != null) {
                    LocalBroadcastManager.getInstance(AppExitActivity.this).unregisterReceiver(mReceiverBackgroundLoginService);
                }
            }catch (Exception e){
                //
            }

        }
    };


    private void setList(){
        list = new ArrayList<>();
        list.add(new DownloadSourceModel(1,R.drawable.facebook,getString(R.string.facebook),CGlobalVariables.FACEBOOK));
        list.add(new DownloadSourceModel(2,R.drawable.instagram,getString(R.string.instagram),CGlobalVariables.INSTAGRAM));
        list.add(new DownloadSourceModel(3,R.drawable.twitter,getString(R.string.twitter),CGlobalVariables.TWITTER));
        list.add(new DownloadSourceModel(4,R.drawable.youtube,getString(R.string.youtube),CGlobalVariables.YOUTUBE));
        list.add(new DownloadSourceModel(5,R.drawable.reffered_by_friend,getString(R.string.refer_by_friend),CGlobalVariables.REFERRED_BY_FRIEND));
        list.add(new DownloadSourceModel(6,R.drawable.google,getString(R.string.google),CGlobalVariables.GOOGLE_SEARCH));
        list.add(new DownloadSourceModel(8,R.drawable.whatsapp,getString(R.string.whatsapp),CGlobalVariables.WHATSAPP));
        Collections.shuffle(list);
        list.add(new DownloadSourceModel(7,R.drawable.others,getString(R.string.cat_other),null));
    }

    public void itemSelected(DownloadSourceModel item){
        if(item.getSourceId() == 7){
            llOthers.setVisibility(View.VISIBLE);
        }else{
            llOthers.setVisibility(View.GONE);
        }
        commentsType = String.valueOf(item.getSourceId());
        comments = item.getComment();
    }
    /**
     * Parses the JSON response for the "Reduced Price Recharge Offer".
     * <p>
     * If the response indicates success (status 1) and contains a valid list of services,
     * this method interrupts the exit flow and redirects the user to the
     * {@link PurchaseRechargeOfferActivity} to show them the offer.
     * </p>
     *
     * @param response The JSON string returned by the offer API or cached preference.
     */
    public void parseResponse(String response){
        try {
            Log.e("fiveRupeeTest", "onResponse: " + response);
            JSONObject jsonObject = new JSONObject(response);

            // Check if the API status is 1 (Success)
            if (jsonObject.has("status") && jsonObject.getInt("status") == 1) {

                // Verify that the 'services' array exists and contains items
                if (jsonObject.has("services") && jsonObject.getJSONArray("services").length() > 0) {
                    JSONArray servicesArray = jsonObject.getJSONArray("services");
                    for (int i = 0; i < servicesArray.length(); i++) {
                        JSONObject serviceObj = servicesArray.getJSONObject(i);

                        String serviceId = serviceObj.getString("serviceid");

                        if (serviceId.equals("261")) { //service id for 5 rupee recharge offer
                            Log.d("fiveRupeeTest", "Service ID 261 found");
                            // Redirect to the Offer Activity expecting a result
                            // SUB_ACTIVITY_EXIT_PLAN handles the callback when the user returns from that screen
                            startActivityForResult(new Intent(AppExitActivity.this, PurchaseRechargeOfferActivity.class), SUB_ACTIVITY_EXIT_PLAN);
                            break;
                        }
                    }





                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==SUB_ACTIVITY_EXIT_PLAN){
            if (resultCode == RESULT_OK) {
                exit();
            }
        }
    }
    public interface OfferAvailableListener{
        void onOfferAvailable(String response);
    }
}
