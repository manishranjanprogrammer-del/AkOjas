package com.ojassoft.astrosage.ui.act;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.varta.service.Loginservice;
import com.ojassoft.astrosage.varta.ui.activity.BaseActivity;
import com.ojassoft.astrosage.varta.ui.activity.PaymentInformationActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.CustomProgressDialog;
import com.ojassoft.astrosage.varta.utils.FontUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * InviteAppActivity - Referral System Implementation
 * 
 * Purpose:
 * This activity implements the app's referral system that allows users to invite friends to
 * AstroSage AI and earn rewards for successful referrals.
 * 
 * Requirements:
 * 1. Fetch user's unique referral code from server
 * 2. Store referral code in shared preferences for future use
 * 3. Allow users to share referral code via WhatsApp or any other app
 * 4. Provide UI elements showing referral rewards and instructions
 * 5. Support reward distribution to both referrer and referee after a paid consultation
 * 
 * Flow:
 * 1. On activity creation, check if referral code exists in preferences
 * 2. If not, fetch it from server and store it
 * 3. Display sharing options to user (WhatsApp or other apps)
 * 4. When user shares, format message with referral code and app link
 * 5. Track installations and paid consultations for reward distribution
 * 
 * Reward Logic:
 * - Both referrer and referee receive reward amount (configured server-side)
 * - Reward is credited after referee completes a paid consultation
 * - Reward amounts are displayed in the UI from shared preferences
 */
public class InviteAppActivity extends BaseActivity {
    private LinearLayout llShareViaWhatsapp, llShareVialink;
    private boolean isLoginPerformed;
    private TextView txtReferAmount, txtSubHead2, txtSubHead3, txt_refer_a_friend_amount;
    private CustomProgressDialog pd;
    private String referCode;  // Stores user's unique referral code
    private ConstraintLayout main;

    /**
     * Android lifecycle method – called when the activity is first created.
     * Sets the layout and delegates view initialization to {@link #inti()}.
     * 
     * Key operations:
     * 1. Set content view and initialize UI components
     * 2. Get language code for localization
     * 3. Check if referral code exists in preferences
     * 4. If not, initiate API call to fetch referral code from server
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_app);
        LANGUAGE_CODE = AstrosageKundliApplication.getAppContext().getLanguageCode();
        main = findViewById(R.id.main);
        inti();
        
        // Check if referral code exists in preferences, if not fetch from server
        referCode = CUtils.getUserUniqueReferCode(InviteAppActivity.this);
        if (TextUtils.isEmpty(referCode)) {
            getReferralCode();
        }
    }
    
    /**
     * Flag to track API response status 100 (session expired)
     * Used to retry API call after background login
     */
    boolean isStatus100 = false;
    
    /**
     * Fetches the user's unique referral code from the server.
     * 
     * API call flow:
     * 1. Show progress dialog
     * 2. Call API with user credentials and device info
     * 3. On success (status 1), store code in preferences
     * 4. On session expired (status 100), trigger background login then retry
     * 5. On failure, show error and close activity
     */
    private void getReferralCode() {
        if (pd == null)
            pd = new CustomProgressDialog(InviteAppActivity.this);
        pd.show();
        pd.setCancelable(false);
        ApiList api = RetrofitClient.getInstance().create(ApiList.class);
        // Log.d("getParamsReferral", "getParamsReferral   parms==>>> " + getParamsReferral());
        Call<ResponseBody> call = api.getUserUniqueReferCode(getParamsReferral());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                hideProgressBar();
                if (response.body() != null) {
                    try {
                        String myResponse = response.body().string();
                        //Log.d("getParamsReferral", "getParamsReferral   myResponse==>>> " + myResponse);
                        JSONObject jsonObject = new JSONObject(myResponse);
                        String status = jsonObject.getString("status");
                        if (status.equals("1")) {
                            // Success - store referral code in preferences
                            referCode = jsonObject.getString("refercode");
                            CUtils.setUserUniqueReferCode(InviteAppActivity.this, referCode);
                        } else if (status.equals("100")) {
                            // Session expired - trigger background login
                            isStatus100 = true;
                            LocalBroadcastManager.getInstance(InviteAppActivity.this).registerReceiver(mReceiverBackgroundLoginService, new IntentFilter(com.ojassoft.astrosage.varta.utils.CGlobalVariables.SEND_BROADCAST_BACK_GR_LOGIN));
                            startBackgroundLoginService();
                        }else {
                            CUtils.showSnackbar(main, getString(R.string.something_wrong_error) + "(" + status + ")", InviteAppActivity.this);
                            new Handler().postDelayed(() -> finish(), 2000);
                        }

                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    } catch (JSONException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                hideProgressBar();
                CUtils.showSnackbar(main, getString(R.string.something_wrong_error) + "(" + throwable.getMessage() + ")", InviteAppActivity.this);
                new Handler().postDelayed(() -> finish(), 2000);

            }
        });
    }
    
    /**
     * Starts the background login service to refresh user session.
     * Only triggers if user is already logged in.
     */
    private void startBackgroundLoginService() {
        try {
            if (com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(this)) {
                Intent intent = new Intent(this, Loginservice.class);
                startService(intent);
            }
        } catch (Exception e) {/**/}
    }
    
    /**
     * BroadcastReceiver that listens for background login completion.
     * When login is successful and isStatus100 flag is true, retries
     * fetching the referral code.
     */
    private final BroadcastReceiver mReceiverBackgroundLoginService = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                String status = intent.getStringExtra("status");
                if (status.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.SUCCESS)) {
                    if (isStatus100) {
                        getReferralCode();
                        isStatus100 = false;
                    }
                }
                if (mReceiverBackgroundLoginService != null) {
                    LocalBroadcastManager.getInstance(InviteAppActivity.this).unregisterReceiver(mReceiverBackgroundLoginService);
                }
            } catch (Exception e) {
                //
            }

        }
    };
    
    /**
     * Helper method to dismiss progress dialog safely.
     * Handles potential exceptions that might occur during dismissal.
     */
    private void hideProgressBar() {
        try {
            if (pd != null && pd.isShowing())
                pd.dismiss();
        } catch (Exception e) {
        }

    }

    /**
     * Prepares request parameters for referral code API call.
     * 
     * Parameters included:
     * - User ID (for blocking purposes)
     * - Phone number
     * - Country code
     * - Language code
     * - App signature hash (for security verification)
     * 
     * @return Map containing all required parameters for the API call
     */
    private Map<String, String> getParamsReferral() {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.KEY_USER_ID, CUtils.getUserIdForBlock(InviteAppActivity.this));
        headers.put(CGlobalVariables.PHONE_NO, CUtils.getUserID(InviteAppActivity.this));
        headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(InviteAppActivity.this));
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(InviteAppActivity.this));
        return CUtils.setRequiredParams(headers);
    }

    /**
     * Initializes UI components and sets up click listeners.
     * 
     * Key operations:
     * 1. Retrieve reward amount from shared preferences
     * 2. Initialize view references
     * 3. Set dynamic text in UI elements showing reward amounts
     * 4. Configure click listeners for share buttons and back navigation
     */
    private void inti() {
        // Get the reward amount stored in preferences
        String referAndEarn = com.ojassoft.astrosage.utils.CUtils.getStringData(InviteAppActivity.this, com.ojassoft.astrosage.utils.CGlobalVariables.REFER_AND_EARN_AMOUNT, "");

        // Initialize UI components
        llShareViaWhatsapp = findViewById(R.id.llShareViaWhatsapp);
        llShareVialink = findViewById(R.id.llShareVialink);

        txtReferAmount = findViewById(R.id.txtReferAmount);
        txtSubHead2 = findViewById(R.id.txtSubHead2);
        txtSubHead3 = findViewById(R.id.txtSubHead3);
        txt_refer_a_friend_amount = findViewById(R.id.txt_refer_a_friend_amount);

        // Replace placeholder text with actual reward amount
        txtReferAmount.setText(txtReferAmount.getText().toString().replace("####", referAndEarn));
        txtSubHead2.setText(txtSubHead2.getText().toString().replace("####", referAndEarn));
        txtSubHead3.setText(txtSubHead3.getText().toString().replace("####", referAndEarn));
        txt_refer_a_friend_amount.setText(txt_refer_a_friend_amount.getText().toString().replace("####", referAndEarn));

        ImageView ivBack = findViewById(R.id.ivBack);
        ImageView shareViaLink = findViewById(R.id.shareViaLink);
        ImageView imgWhatsApp = findViewById(R.id.imgWhatsApp);
        TextView tvShareViaWhatApp = findViewById(R.id.tv_shareviaWhatapp);
        TextView tvShare = findViewById(R.id.tv_share);
        TextView txt_refer_a_friend = findViewById(R.id.txt_refer_a_friend);
        int nightModeFlags = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        String s4 = "";
        if (nightModeFlags == Configuration.UI_MODE_NIGHT_YES) {
            s4 = getString(R.string.congrats_you_recived_refer_amount).replace("####", "<font color='#ffc107'>" + "\u20B9" + referAndEarn + "</font>");

        } else if (nightModeFlags == Configuration.UI_MODE_NIGHT_NO) {
            s4 = getString(R.string.congrats_you_recived_refer_amount).replace("####", "<font color='#ff6f00'>" + "\u20B9" + referAndEarn + "</font>");
        }
        txt_refer_a_friend.setText(Html.fromHtml(s4));
        FontUtils.changeFont(this, txt_refer_a_friend, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(this, txt_refer_a_friend_amount, CGlobalVariables.FONTS_OPEN_SANS_BOLD);

        ivBack.setOnClickListener(v -> finish());
        imgWhatsApp.setOnClickListener(v -> shareViaWhatsapp());
        tvShareViaWhatApp.setOnClickListener(v -> shareViaWhatsapp());
        llShareViaWhatsapp.setOnClickListener(v -> shareViaWhatsapp());
        shareViaLink.setOnClickListener(v -> shareViaLink());
        tvShare.setOnClickListener(v -> shareViaLink());
        llShareVialink.setOnClickListener(v -> shareViaLink());
    }

    /**
     * Shares the referral link using a generic chooser.
     * 
     * 1. Captures a bitmap of the root view for richer sharing experience (some apps show the image).
     * 2. Constructs the Play‑Store link with the user’s referral code appended.
     * 3. Launches a chooser so the user can pick any compatible sharing application.
     */
    private void shareViaLink() {
        // Abort if referral code not found
        com.ojassoft.astrosage.utils.CUtils.fcmAnalyticsEvents(com.ojassoft.astrosage.utils.CGlobalVariables.APP_SHARE_AND_EARN_VIA_APPLINK, com.ojassoft.astrosage.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

        boolean isLogin = com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(InviteAppActivity.this);
        if (isLogin) {
            String referCode = CUtils.getUserUniqueReferCode(InviteAppActivity.this);
            if (TextUtils.isEmpty(referCode)) return;
            try {
                View view = findViewById(R.id.clShareView);
                // view.setVisibility(View.VISIBLE);
                Uri imageUri = CUtils.getViewBitmap(InviteAppActivity.this, view);
                String msgInfo = getResources().getString(R.string.join_astrosage_ai_using_refer).replace("####", referCode);
                String shareMessage = "\n" + msgInfo + "\n\n" +
                        "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "&referrer=" + referCode + "\n\n";
                CUtils.shareDataToOtherApps(InviteAppActivity.this, shareMessage, imageUri);


            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, R.string.something_wrong_error, Toast.LENGTH_SHORT).show();
            }
        } else {
            com.ojassoft.astrosage.utils.CUtils.fcmAnalyticsEvents(com.ojassoft.astrosage.utils.CGlobalVariables.APP_SHARE_AND_EARN_VIA_APPLINK_NOT_LOGGED_IN, com.ojassoft.astrosage.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
            isLoginPerformed = true;
            Intent intent1 = new Intent(InviteAppActivity.this, FlashLoginActivity.class);
            startActivity(intent1);
        }

    }

    /**
     * Opens WhatsApp directly with a text‑only referral message. Falls back to default chooser if WhatsApp is not
     * installed.
     */
    private void shareViaWhatsapp() {
        com.ojassoft.astrosage.utils.CUtils.fcmAnalyticsEvents(com.ojassoft.astrosage.utils.CGlobalVariables.APP_SHARE_AND_EARN_VIA_WHATSAPP, com.ojassoft.astrosage.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
        boolean isLogin = com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(InviteAppActivity.this);
        if (isLogin) {
            try {
                String referCode = CUtils.getUserUniqueReferCode(InviteAppActivity.this);
                if (TextUtils.isEmpty(referCode)) return;

                View view = findViewById(R.id.clShareView);
                //view.setVisibility(View.VISIBLE);
                Uri imageUri = CUtils.getViewBitmap(InviteAppActivity.this, view);
                String msgInfo = getResources().getString(R.string.join_astrosage_ai_using_refer).replace("####", referCode);

                String shareMessage = "\n" + msgInfo + "\n\n" +
                        "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID + "&referrer=" + referCode + "\n\n";
                CUtils.shareDataToOtherAppsViaWhatsapp(InviteAppActivity.this, shareMessage, imageUri);

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            com.ojassoft.astrosage.utils.CUtils.fcmAnalyticsEvents(com.ojassoft.astrosage.utils.CGlobalVariables.APP_SHARE_AND_EARN_VIA_WHATSAPP_NOT_LOGGED_IN, com.ojassoft.astrosage.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
            isLoginPerformed = true;
            Intent intent1 = new Intent(InviteAppActivity.this, FlashLoginActivity.class);
            startActivity(intent1);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isLoginPerformed) {
            boolean isLogin = com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(InviteAppActivity.this);
            if (isLogin) {
                isLoginPerformed = false;
                shareViaLink();
            }
        }
    }
}