package com.ojassoft.astrosage.varta.ui.activity;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.GlobalRetrofitResponse;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.SuperBaseActivity;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.varta.dialog.BottomSheetDialog;
import com.ojassoft.astrosage.varta.dialog.BottomSheetDialogFreeAIChat;
import com.ojassoft.astrosage.varta.dialog.BottomSheetDialogFreeChat;
import com.ojassoft.astrosage.varta.dialog.PaymentFailDialog;
import com.ojassoft.astrosage.varta.dialog.PaymentProcessDialog;
import com.ojassoft.astrosage.varta.dialog.PaymentSucessfulDialog;
import com.ojassoft.astrosage.varta.model.NextOfferBean;
import com.ojassoft.astrosage.varta.model.UserProfileData;
import com.ojassoft.astrosage.varta.service.PreFetchDataservice;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.ChatUtils;
import com.ojassoft.astrosage.varta.utils.ConnectivityReceiver;
import com.ojassoft.astrosage.varta.utils.CustomProgressDialog;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class BaseActivity extends SuperBaseActivity implements VolleyResponse {

    private ProgressDialog installLiveProgressDialog;
    CustomProgressDialog pd;
    String amount;
    RequestQueue mainQueue;
    protected int METHOD_RECHARGE = 100001;

    private ConnectivityReceiver connectivityReceiver;
    protected AstrosageKundliApplication mVartaAstroApp;
//    int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    PaymentSucessfulDialog paymentSucessfulDialog;
    PaymentProcessDialog paymentProcessDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication()).getLanguageCode();
        CUtils.getRobotoFont(BaseActivity.this, LANGUAGE_CODE, CGlobalVariables.regular);
        //Log.e("SAN=BaseActivity=>", ((AstrosageKundliApplication) getApplication()).getLanguageCode() + "");
        // queue = VolleySingleton.getInstance(this).getRequestQueue();

        // Enable edge-to-edge mode with sensible defaults (from AndroidX EdgeToEdge library)
        EdgeToEdge.enable(this);

        // Apply custom handling of insets (status bar, nav bar, keyboard padding)
        // so views don’t overlap with system bars or the keyboard
        com.ojassoft.astrosage.utils.CUtils.applyEdgeToEdgeInsets(this);

        mVartaAstroApp = (AstrosageKundliApplication) this.getApplicationContext();
        connectivityReceiver = new ConnectivityReceiver();

    }



    public void udatePaymentStatusOnserver(GlobalRetrofitResponse globalRetrofitResponse,View containerLayout, String orderStatus, String orderID, String amount, RequestQueue queue, String paymentMode, String razorpayid) {
        //Log.d("PaymentStatus", "udatePaymentStatusOnserver()");
        this.amount = amount;
        if (!CUtils.isConnectedWithInternet(BaseActivity.this)) {
            CUtils.showSnackbar(containerLayout, getResources().getString(R.string.no_internet), BaseActivity.this);
        } else {
            if (pd == null)
                pd = new CustomProgressDialog(BaseActivity.this);
            pd.show();
            pd.setCancelable(false);

            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.walletRecharge(getParamsForWalletRecharge(orderStatus, orderID,razorpayid));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    hideProgressBar();
                    startPrefatchDataService();
                    //Log.e("TestFreeChat", "chatAstrologerDetailBeanAfterRecharge1="+AstrosageKundliApplication.chatAstrologerDetailBeanAfterRecharge);
                    if(AstrosageKundliApplication.chatAstrologerDetailBeanAfterRecharge != null){ //initiate chat automatically in case of insufficient balance dialog
                        AstrosageKundliApplication.selectedAstrologerDetailBean = AstrosageKundliApplication.chatAstrologerDetailBeanAfterRecharge;
                        AstrosageKundliApplication.isChatScreenOpes = false;
                        if(AstrosageKundliApplication.checkWhichConsultationScreen.equals("AIVOICECALL")){
                            CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_ASTRO_CONTINUE_AI_CALL_BTN, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                            UserProfileData userDetails = com.ojassoft.astrosage.varta.utils.CUtils.getUserSelectedProfileFromPreference(BaseActivity.this);
                            AstrosageKundliApplication.selectedAstrologerDetailBean.setCallSource("AIVoiceCallingActivity");
                            ChatUtils.getInstance(BaseActivity.this).connectAIVoiceCall(AstrosageKundliApplication.selectedAstrologerDetailBean, userDetails);
                        } else if (AstrosageKundliApplication.checkWhichConsultationScreen.equals("HUMAN_CALL")) {
                            ChatUtils.getInstance(BaseActivity.this).initVoiceCall(AstrosageKundliApplication.chatAgainAstrologerDetailBean);
                        } else {
                            ChatUtils.getInstance(BaseActivity.this).callStartChat(CUtils.isAiAstrologer(AstrosageKundliApplication.selectedAstrologerDetailBean));
                        }
                        AstrosageKundliApplication.chatAstrologerDetailBeanAfterRecharge = null;
                        AstrosageKundliApplication.checkWhichConsultationScreen = "";
                    } else {
                        globalRetrofitResponse.onResponse(call, response, METHOD_RECHARGE);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    globalRetrofitResponse.onFailure(call,t);
                }
            });
        }
    }

    private void startPrefatchDataService() {
        try {
            Intent intentService = new Intent(BaseActivity.this, PreFetchDataservice.class);
            startService(intentService);
        } catch (Exception e) {
        }

    }

    // SAN when live make it commented or remove the code

    public Fragment getVisibleFragmentNameSan(){
        FragmentManager fragmentManager = this.getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if(fragments != null){
            for(Fragment fragment : fragments){
                if(fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();

        /*if ( getVisibleFragmentNameSan() != null ) {
            Log.e("SAN BaseActivity =>", "Fragment Tag and Name => " + getVisibleFragmentNameSan().getTag() + "<=>" + getVisibleFragmentNameSan().toString() );
        }*/

        try {
            mVartaAstroApp.setCurrentActivity(this);
        } catch (Exception e) {}

        try {
            IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
            ContextCompat.registerReceiver(this, connectivityReceiver, intentFilter, ContextCompat.RECEIVER_EXPORTED);
        } catch (Exception e) {}

    }

    @Override
    protected void onPause() {
        clearReferences();
        super.onPause();

        if (connectivityReceiver != null) {
            try {
                unregisterReceiver(connectivityReceiver);
            } catch (Exception e) {
            }
        }
    }

    @Override
    protected void onDestroy() {
        clearReferences();
        super.onDestroy();
    }

    private void clearReferences() {
        Activity currActivity = mVartaAstroApp.getCurrentActivity();
        if (this.equals(currActivity))
            mVartaAstroApp.setCurrentActivity(null);
    }

    private Map<String, String> getParamsForWalletRecharge(String orderStatus, String orderId, String razorpayid) {
        HashMap<String, String> headers = new HashMap<String, String>();
        try {
            headers.put("key", CUtils.getApplicationSignatureHashCode(BaseActivity.this));
            headers.put("od", orderId);
            headers.put("isSucess", orderStatus);
            headers.put("paycurr", "INR");
            headers.put("razorpayid", razorpayid);
            //headers.put("callfrom", "admin");

        } catch (Exception e) {

        }
        return CUtils.setRequiredParams(headers);

    }

    @Override
    public void onResponse(String response, int method) {
        //Log.e("PaymentStatus ", "onResponse="+response);
        handleWalletRechargeResponse(response);
    }

    @Override
    public void onError(VolleyError error) {
        hideProgressBar();
        try {
            PaymentFailDialog dialog = new PaymentFailDialog();
            dialog.show(getSupportFragmentManager(), "PaymentFailDialog");
        } catch (Exception e) {
            //Log.e("PaymentStatus ", "exp="+e);
        }
    }

    private void hideProgressBar(){
        try{
            if(pd != null && pd.isShowing()){
                pd.dismiss();
            }
        } catch (Exception e){
            //
        }
    }

    public void handleWalletRechargeResponse(String response){
        //Log.d("PaymentStatus", response);
        hideProgressBar();
        try {
            JSONObject jsonObject = new JSONObject(response);
            String status = jsonObject.getString("status");
            if (status.equals("1") || status.equals("2")) {
                try {
                    //CUtils.clearWalletApiCache(mainQueue);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (paymentSucessfulDialog != null
                        && paymentSucessfulDialog.getDialog() != null
                        && paymentSucessfulDialog.getDialog().isShowing()
                        && !paymentSucessfulDialog.isRemoving()) {
                    //dialog is showing so do nothing
                } else {
                    //dialog is not showing
                    paymentSucessfulDialog = new PaymentSucessfulDialog(amount);
                    paymentSucessfulDialog.show(getSupportFragmentManager(), "PaymentSucessfulDialog");
                }

            } else {
                if (paymentProcessDialog != null
                        && paymentProcessDialog.getDialog() != null
                        && paymentProcessDialog.getDialog().isShowing()
                        && !paymentProcessDialog.isRemoving()) {
                    //dialog is showing so do nothing
                } else {
                    //dialog is not showing
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_OPEN_RECHARGE_PROCESS_DIALOG,
                            CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");
                    paymentProcessDialog = new PaymentProcessDialog(amount);
                    paymentProcessDialog.show(getSupportFragmentManager(), "PaymentProcessDialog");
                }
            }
        } catch (Exception e) {
            //Log.e("PaymentStatus ", "exp="+e);
        }
    }

    public void handleWalletRechargeError(){
        //Log.d("PaymentStatus", "error");
        hideProgressBar();
    }

    public void showBottomSheet(NextOfferBean nextOfferBean) {
        try {
            CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_OPEN_NEXT_OFFER_RECHARGE_DIALOG,
                    CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");
            BottomSheetDialog bottomSheet = new BottomSheetDialog();
            Bundle bndl = new Bundle();
            bndl.putString("message", nextOfferBean.getPromotionalmsg());
            bndl.putString("extra", nextOfferBean.getStatic());
            bndl.putString("amount", nextOfferBean.getOfferamountstring());
            bndl.putString("serviceId", nextOfferBean.getServiceid());
            bottomSheet.setArguments(bndl);
            bottomSheet.show(getSupportFragmentManager(),
                    "ModalBottomSheet");
            AstrosageKundliApplication.isOpenVartaPopup = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     *
     */
    public void callFreeChatPopUnderDialog() {
        try {
            if(CUtils.isAutoConsultationConnected){ //don't show popup in case of auto connect free chat
                //CUtils.isAutoChatConnectFromAds = false;
                return;
            }
            boolean enabledAIFreeChatPopup = com.ojassoft.astrosage.utils.CUtils.isAIfreeChatPopupEnabled(this);
            String firstFreeChatType = CUtils.getStringData(this, com.ojassoft.astrosage.utils.CGlobalVariables.FIRST_FREE_CHAT_TYPE,"");
            String secondFreeChatType = CUtils.getStringData(this, com.ojassoft.astrosage.utils.CGlobalVariables.SECOND_FREE_CHAT_TYPE,"");
            String offerType = CUtils.getCallChatOfferType(this);
            if (enabledAIFreeChatPopup) {
                if(CUtils.isSecondFreeChat(this) && offerType.equals(CGlobalVariables.INTRO_OFFER_TYPE_FREE)){
                    if(secondFreeChatType.equalsIgnoreCase(com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_FREE_CHAT_AI)){
                        openAIPopUnder();
                    } else {
                        openHumanPopUnder();
                    }
                } else {
                    if(firstFreeChatType.equalsIgnoreCase(com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_FREE_CHAT_AI)){
                        openAIPopUnder();
                    } else {
                        openHumanPopUnder();
                    }
                }
            } else {
                openHumanPopUnder();
            }
            AstrosageKundliApplication.isOpenVartaPopup = true;
        } catch (Exception e) {
            //
        }
    }

    private void openAIPopUnder(){
        try {
            BottomSheetDialogFreeAIChat bottomSheetDialogFreeAIChat = new BottomSheetDialogFreeAIChat();
            bottomSheetDialogFreeAIChat.setCancelable(false);
            bottomSheetDialogFreeAIChat.show(getSupportFragmentManager(), "FreeAIChatBottomSheet");
        } catch (Exception e){
            //
        }
    }

    public void openHumanPopUnder(){
        try {
            BottomSheetDialogFreeChat bottomSheetDialogFreeChat = new BottomSheetDialogFreeChat();
            bottomSheetDialogFreeChat.setCancelable(false);
            bottomSheetDialogFreeChat.show(getSupportFragmentManager(), "FreeChatBottomSheet");
        } catch (Exception e){
            //
        }
    }

    public void udatePaymentStatusOnserveronFailed(View containerLayout, final String orderStatus, final String orderID, final String amount, RequestQueue queue, final String paymentMode) {
        String orderUrl = "";

        if (paymentMode.equalsIgnoreCase(CGlobalVariables.PAYTM)) {
            orderUrl = CGlobalVariables.UPDATE_STATUS_RAZOR_PAY;
        } else {
            orderUrl = CGlobalVariables.UPDATE_PAY_STATUS_ASKQUE;
        }
        if (pd == null)
            pd = new CustomProgressDialog(BaseActivity.this);
        pd.show();
        pd.setCancelable(false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, orderUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        hideProgressBar(pd);
                        //Log.e("LoadMore recharge resp ", response);
                        try {
                            JSONArray array = new JSONArray(response);
                            JSONObject obj = array.getJSONObject(0);
                            String result = obj.getString("Result");
                            //for paytm  //1 success or fail update & 5 when s2s update server api
                            //result = 1 for succesfully updated and result =2 for data updated from server to server
                            if (result.equalsIgnoreCase("1") || result.equalsIgnoreCase("2")) {
                                //callback.getCallBack("1", callBack.POST_RAZORPAYSTATUS, "", "");
                            } else {
                                //result = 5 for data not succesfully updated
                                //callback.getCallBack("0", callBack.POST_RAZORPAYSTATUS, "", "");
                            }
                            PaymentFailDialog dialog = new PaymentFailDialog();
                            dialog.show(getSupportFragmentManager(), "PaymentFailDialog");
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                hideProgressBar(pd);
                //callback.getCallBack("0", callBack.POST_RAZORPAYSTATUS, "", "");
                VolleyLog.d("VolleyError: " + error.getMessage());
                try {
                    PaymentFailDialog dialog = new PaymentFailDialog();
                    dialog.show(getSupportFragmentManager(), "PaymentFailDialog");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        ) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded;charset=UTF-8";
            }

            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getParams() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("key", CUtils.getApplicationSignatureHashCode(BaseActivity.this));
                headers.put("amount", amount);
                headers.put("orderid", orderID);
                headers.put("paycurr", "INR");
                headers.put("status", orderStatus);
                headers.put("profile_Id", CUtils.getUserID(BaseActivity.this));

                if (paymentMode.equalsIgnoreCase(CGlobalVariables.PAYTM)) {
                    headers.put("chatid", "");
                } else {
                    headers.put("paymentid", "");
                    headers.put("razorpayresponse", "0");
                }
                headers.put(CGlobalVariables.DEVICE_ID, CUtils.getMyAndroidId(BaseActivity.this));
                headers.put(CGlobalVariables.APP_VERSION, "" + BuildConfig.VERSION_NAME);
                headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(BaseActivity.this));
                return headers;
            }

        };
        ;
        // Add the request to the RequestQueue.
        //com.google.analytics.tracking.android.//Log.e("API HIT HERE");
        int socketTimeout = 60000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
    }

    private void hideProgressBar(CustomProgressDialog pd) {
        try {
            if (pd != null && pd.isShowing())
                pd.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void showLogoProgressbar(ImageView imageView) {
        imageView.setVisibility(View.VISIBLE);
        Glide.with(getApplicationContext()).load(R.drawable.new_ai_loader).into(imageView);
    }

    public void hideLogoProgressbar(ImageView imageView) {
        imageView.setVisibility(View.GONE);
    }


    public void showInstallLiveProgressBar() {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (installLiveProgressDialog == null) {
                        installLiveProgressDialog = new ProgressDialog(BaseActivity.this);
                    }

                    installLiveProgressDialog.setMessage(getResources().getString(R.string.downloading_live_content));
                    installLiveProgressDialog.setIndeterminate(true);
                    installLiveProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                    installLiveProgressDialog.setCancelable(false);
                    installLiveProgressDialog.show();
                }
            });
        } catch (Exception e) {
            //
        }
    }

    public void hideInstallLiveProgressBar() {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (installLiveProgressDialog != null && installLiveProgressDialog.isShowing()) {
                        installLiveProgressDialog.dismiss();
                    }
                }
            });
        } catch (Exception e) {
            //
        }
    }

    public void updateInstallLiveProgressBar(int totalBytes, int progress) {
        try {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(totalBytes <= 0 || progress <= 0){
                        return;
                    }
                    if (installLiveProgressDialog != null) {
                        installLiveProgressDialog.setIndeterminate(false);
                        installLiveProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                        installLiveProgressDialog.setMax(totalBytes);
                        installLiveProgressDialog.setProgress(progress);
                    }
                }
            });
        } catch (Exception e) {
            //
        }
    }

    protected Boolean isActivityRunning(Class activityClass) {
        try {
            ActivityManager activityManager = (ActivityManager) getBaseContext().getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningTaskInfo> tasks = activityManager.getRunningTasks(Integer.MAX_VALUE);

            for (ActivityManager.RunningTaskInfo task : tasks) {
                if (activityClass.getCanonicalName().equalsIgnoreCase(task.baseActivity.getClassName()))
                    return true;
            }
        }catch (Exception e){
            //
        }
        return false;
    }

    public void openWalletScreenDashboard(String openFrom) {
        if(openFrom.equals(CGlobalVariables.SCREEN_OPEN_FROM_INSUFFICIENT)){
            com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_BASE_ACTIVITY_LOW_BALANCE_OPEN_WALLET, CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");
            com.ojassoft.astrosage.utils.CUtils.createSession(this, "BAREC");
        }else {
            com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_BASE_ACTIVITY_LOW_BALANCE_SUBSCRIBE_OPEN_WALLET, CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");
            com.ojassoft.astrosage.utils.CUtils.createSession(this, "BSREC");

        }
        Intent intent = new Intent(BaseActivity.this, WalletActivity.class);
        intent.putExtra(CGlobalVariables.CALLING_ACTIVITY, "DashBoardActivity");
        startActivity(intent);
    }
}
