package com.ojassoft.astrosage.ui;

import static com.ojassoft.astrosage.ui.act.ActAppModule.liveAstrologerModelArrayList;
import static com.ojassoft.astrosage.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_MY_ACCOUNT_BOTTOM_KUNDLI;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_MY_ACCOUNT_BOTTOM_LIVE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.MY_ACCOUNT_BOTTOM_BAR_KUNDLI_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.MY_ACCOUNT_BOTTOM_BAR_LIVE_PARTNER_ID;
// SAN For SCALE CHANGE
import static com.ojassoft.astrosage.utils.CGlobalVariables.fontSizeChange;
import static com.ojassoft.astrosage.utils.CGlobalVariables.isFontSizeChange;
import static com.ojassoft.astrosage.utils.CUtils.createSession;
import static com.ojassoft.astrosage.utils.CUtils.openKundliActivity;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.FILTER_TYPE_CALL;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.FILTER_TYPE_CHAT;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.IS_GOOGLE_FACEBOOK_VISIBLE;
import static com.ojassoft.astrosage.varta.utils.CUtils.gotoAllLiveActivityFromLiveIcon;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;


import com.ojassoft.astrosage.BuildConfig;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.ActAppModule;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.varta.dialog.CallMsgDialog;
import com.ojassoft.astrosage.varta.model.LiveAstrologerModel;
import com.ojassoft.astrosage.varta.service.AstroAcceptRejectService;
import com.ojassoft.astrosage.varta.ui.activity.AllLiveAstrologerActivity;
import com.ojassoft.astrosage.varta.ui.activity.AstrologerDescriptionActivity;
import com.ojassoft.astrosage.varta.ui.activity.ChatWindowActivity;
import com.ojassoft.astrosage.varta.ui.activity.ConsultantHistoryActivity;
import com.ojassoft.astrosage.varta.ui.activity.LoginSignUpActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.vartalive.activities.VideoCallActivity;
import com.ojassoft.astrosage.vartalive.activities.VoiceCallActivity;

import java.util.List;

/* Must Read
 * managing broadcast related to chat
 * */

public class SuperBaseActivity extends AppCompatActivity {

    /**
     * Applies configured custom fonts after an activity layout is attached to the window.
     */
    @Override
    public void setContentView(int layoutResID) {
        super.setContentView(layoutResID);
    }

    /**
     * Applies configured custom fonts after a caller provides a fully inflated content view.
     */
    @Override
    public void setContentView(View view) {
        super.setContentView(view);
    }

    /**
     * Applies configured custom fonts after a caller provides a content view with layout params.
     */
    @Override
    public void setContentView(View view, ViewGroup.LayoutParams params) {
        super.setContentView(view, params);
    }

    public boolean isActAppModuleResumed = false;
    private static final int PERMISSION_REQ_CODE = 1;

    public BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            item -> {
                switch (item.getItemId()) {
                    case R.id.bottom_nav_home:
                        if (isActAppModuleResumed){
                            isActAppModuleResumed = false;
                        } else {
                            clickHome();
                        }
                        return true;
                    case R.id.bottom_nav_call:
                        clickCall();
                        return true;
                    case R.id.bottom_nav_live:
                        clickLive();
                        return true;
                    case R.id.bottom_nav_chat:
                        clickChat();
                        return true;
                    case R.id.bottom_nav_history:
                        clickHistory();
                        return true;
                }
                return false;
            };

    public View.OnClickListener navbarItemSelectedListener = new View.OnClickListener() {
        @Override
        public void onClick(View item) {
            switch (item.getId()) {
                case R.id.bottom_nav_home:
                    if (isActAppModuleResumed){
                        isActAppModuleResumed = false;
                    } else {
                        clickHome();
                    }
                    break;
                case R.id.bottom_nav_call:
                    clickCall();
                    break;
                case R.id.bottom_nav_live:
                    clickLive();
                    break;
                case R.id.bottom_nav_chat:
                    clickChat();
                    break;
                case R.id.bottom_nav_history:
                    clickHistory();
                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Enable edge-to-edge mode with sensible defaults (from AndroidX EdgeToEdge library)
        EdgeToEdge.enable(this);

        // Apply custom handling of insets (status bar, nav bar, keyboard padding)
        // so views don’t overlap with system bars or the keyboard
        com.ojassoft.astrosage.utils.CUtils.applyEdgeToEdgeInsets(this);
        //  Log.d("testNewChat", "SuperBaseActivity onCreate chatAcceptedOrRejected");
        try {
            LocalBroadcastManager.getInstance(SuperBaseActivity.this).registerReceiver(chatAcceptedOrRejected, new IntentFilter(com.ojassoft.astrosage.varta.utils.CGlobalVariables.SEND_BROADCAST_CHAT_ACCEPTED_OR_REJCET));
            LocalBroadcastManager.getInstance(SuperBaseActivity.this).registerReceiver(receiver, new IntentFilter(CGlobalVariables.CALL_BROAD_ACTION));
            LocalBroadcastManager.getInstance(SuperBaseActivity.this).registerReceiver(agoraCallAstrologerAcceptReject, new IntentFilter(com.ojassoft.astrosage.varta.utils.CGlobalVariables.SEND_BROADCAST_AGORA_CALL_ASTROLOGER_ACCEPT_REJECT));

        } catch (Exception e) {
//            Log.d("testNewChat", "SuperBaseActivity onCreate chatAcceptedOrRejected");
//            LocalBroadcastManager.getInstance(SuperBaseActivity.this).registerReceiver(chatAcceptedOrRejected, new IntentFilter(com.ojassoft.astrosage.varta.utils.CGlobalVariables.SEND_BROADCAST_CHAT_ACCEPTED_OR_REJCET));
//            if (receiver != null) {
//                LocalBroadcastManager.getInstance(SuperBaseActivity.this).registerReceiver((receiver),
//                        new IntentFilter(CGlobalVariables.CALL_BROAD_ACTION)
//                );
//            }
        }
        // SAN For SCALE CHANGE
        checkFontConfigration();

    }

    /**
     * Added by Manish Ranjan 2/16/2023
     */
    private final BroadcastReceiver chatAcceptedOrRejected = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
          //  Log.d("testNewChat", "BroadcastReceiver called ");
            if (!AstrosageKundliApplication.isChatScreenOpes) {
                String status = intent.getStringExtra("status");
                String CHAT_CHANNEL_ID = intent.getStringExtra(CGlobalVariables.CHAT_USER_CHANNEL);
                String chatJsonObject = intent.getStringExtra(com.ojassoft.astrosage.utils.CGlobalVariables.CONNECT_CHAT_BEAN);
                String astrologerName = intent.getStringExtra(CGlobalVariables.ASTROLOGER_NAME);
                String astrologerProfileUrl = intent.getStringExtra(CGlobalVariables.ASTROLOGER_PROFILE_URL);
                String astrologerId = intent.getStringExtra(CGlobalVariables.CHAT_ASTROLOGER_ID);
                String userChatTime = intent.getStringExtra(com.ojassoft.astrosage.utils.CGlobalVariables.USERCHATTIME);
                //AstrologerDetailBean astrologerDetailBean = (AstrologerDetailBean)intent.getSerializableExtra("chatInitiateAstrologerDetailBean");

                if (status.equals(com.ojassoft.astrosage.utils.CGlobalVariables.ACCEPTED)) {
                  //  Log.d("testNewChat", " BroadcastReceiver Accepted ");
                    Intent intent1 = new Intent(context, ChatWindowActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("msg", getResources().getString(R.string.astrologer_accepted_chat_request));
                    bundle.putString(CGlobalVariables.CHAT_USER_CHANNEL, CHAT_CHANNEL_ID);
                    bundle.putString(com.ojassoft.astrosage.utils.CGlobalVariables.CONNECT_CHAT_BEAN, chatJsonObject);
                    bundle.putString(CGlobalVariables.ASTROLOGER_NAME, astrologerName);
                    bundle.putString(CGlobalVariables.ASTROLOGER_PROFILE_URL, astrologerProfileUrl);
                    bundle.putString(CGlobalVariables.CHAT_ASTROLOGER_ID, astrologerId);
                    bundle.putString(com.ojassoft.astrosage.utils.CGlobalVariables.USERCHATTIME, userChatTime);
                    //bundle.putSerializable("chatInitiateAstrologerDetailBean",astrologerDetailBean);

                    intent1.putExtras(bundle);
                    startActivity(intent1);
                } else if (status.equals("Rejected")) {
                  //  Log.d("testNewChat", " BroadcastReceiver Rejected ");

                  //  Toast.makeText(SuperBaseActivity.this, "Rejected", Toast.LENGTH_LONG).show();
                }
            }
            AstrosageKundliApplication.isChatScreenOpes = true;
        }
    };

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //Log.d("NewIssueNoti","Called from this superbase activity");
            if(!AstrosageKundliApplication.IS_CALL_BROAD_ACTION_SEEN){
                AstrosageKundliApplication.IS_CALL_BROAD_ACTION_SEEN = true;
                //Log.e("SAN CI DA ", " onReceive inside");
                String message = intent.getStringExtra(CGlobalVariables.BROAD_MSG_RESULT);
                String title = intent.getStringExtra(CGlobalVariables.BROAD_TITLE_RESULT);
                String urlText = intent.getStringExtra(CGlobalVariables.BROAD_LINK_RESULT);
                try {
//                if (urlText != null && urlText.length() > 0) {
//                    if (urlText.contains(CGlobalVariables.ASTROLOGER_ACCEPT_CHAT_REQUEST_NOTIFICATION)) {
//                        String[] channelIDArr = urlText.split("-");
//                        String channelID = "";
//                        if (channelIDArr != null && channelIDArr.length > 1) {
//                            channelID = channelIDArr[1];
//                        }
//                        Log.d("testNewChat", "Called Accept chat from notification");
//
//                        if (AstrosageKundliApplication.selectedAstrologerDetailBean != null && AstrosageKundliApplication.isActivityVisible()) {
//                            if (AstrosageKundliApplication.channelIdTempStore.equals(channelID)) {
//                                AstrosageKundliApplication.currentChatStatus = CGlobalVariables.CHAT_ACCEPTED;
//                                AstrosageKundliApplication.channelIdTempStore = "";
//                                AstrosageKundliApplication.isChatScreenOpes = true;
//                                CUtils.fcmAnalyticsEvents("astrologer_accept_chat_request_notification", AstrosageKundliApplication.currentEventType, "");
//
//                                Log.d("testChatNew", "stopService  AstroAcceptRejectService  ");
//                                stopService(new Intent(context, AstroAcceptRejectService.class));
//
//                                Intent intent1 = new Intent(context, ChatWindowActivity.class);
//                                Bundle bundle = new Bundle();
//                                bundle.putString("msg", getResources().getString(R.string.astrologer_accepted_chat_request));
//                                bundle.putString(CGlobalVariables.CHAT_USER_CHANNEL, channelID);
//                                bundle.putString("connect_chat_bean", AstrosageKundliApplication.chatJsonObject);
//                                bundle.putString("astrologer_name", AstrosageKundliApplication.selectedAstrologerDetailBean.getName());
//                                bundle.putString("astrologer_profile_url", AstrosageKundliApplication.selectedAstrologerDetailBean.getImageFile());
//                                bundle.putString("astrologer_id", AstrosageKundliApplication.selectedAstrologerDetailBean.getAstrologerId());
//                                bundle.putString("userChatTime", CGlobalVariables.userChatTime);
//                                intent1.putExtras(bundle);
//                                startActivity(intent1);
//                            }
//                        }
//                    } else {
//                        Intent intent1 = new Intent(SuperBaseActivity.this, AstrologerDescriptionActivity.class);
//                        intent1.putExtra("phoneNumber", CUtils.getUserID(SuperBaseActivity.this));
//                        intent1.putExtra("urlText", urlText);
//                        intent1.putExtra("msg", message);
//                        intent1.putExtra("title", title);
//                        intent1.putExtra("fromDashboard", true);
//                        startActivity(intent1);
//                    }
//                } else {
                    if ((message != null && message.length() > 0) || (title != null && title.length() > 0)) {
                        try {
                            Log.d("newTestNotification","called from super"+message+" Title "+title);
                            if (CUtils.getStringData(SuperBaseActivity.this, CGlobalVariables.PREF_CHAT_BUTTON_CLICK, "").length() > 0) {
                                callMsgDialogData(message, title, true, CGlobalVariables.CHAT_CLICK);
                            } else {
                                callMsgDialogData(message, title, true, CGlobalVariables.CALL_CLICK);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
//                }
                } catch (Exception e) {
                    e.printStackTrace();
                    //Log.e("SAN CI DA ", " onReceive exp=>" + e.toString());
                }
            }

        }
    };


    public void callMsgDialogData(String message, String title, boolean isShowOkBtn, String fromWhich) {

        try {
//            if (title.equals(getResources().getString(R.string.chat_failed_user)) || title.equalsIgnoreCase(getResources().getString(R.string.chat_not_completed))) {
//            }
            CallMsgDialog dialog = new CallMsgDialog(message, title, isShowOkBtn, fromWhich);
            dialog.show(getSupportFragmentManager(), "Dialog");
        } catch (Exception e) {
           // Toast.makeText(this, title+" "+message, Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        AstrosageKundliApplication.activityResumed();
        // Log.d("checkOnResAndPause","AstrosageKundliApplication.activityResumed()");
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            AstrosageKundliApplication.activityPaused();
        }catch (Exception e){
           //
        }

        //  Log.d("checkOnResAndPause","AstrosageKundliApplication.activityPaused()");

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (receiver != null) {
            //Log.d("testNewChat", "SuperBaseActivity onStop unregisterReceiver chatAcceptedOrRejected");
            LocalBroadcastManager.getInstance(SuperBaseActivity.this).unregisterReceiver(receiver);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (chatAcceptedOrRejected != null) {
            //Log.d("testNewChat", "SuperBaseActivity onStop unregisterReceiver chatAcceptedOrRejected");
            LocalBroadcastManager.getInstance(SuperBaseActivity.this).unregisterReceiver(chatAcceptedOrRejected);
        }
        if (agoraCallAstrologerAcceptReject != null) {
            //Log.d("testNewChat", "SuperBaseActivity onStop unregisterReceiver chatAcceptedOrRejected");
            LocalBroadcastManager.getInstance(SuperBaseActivity.this).unregisterReceiver(agoraCallAstrologerAcceptReject);
        }
        
    }


    /**
     * it starts
     */
    private final BroadcastReceiver agoraCallAstrologerAcceptReject = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Intent resultIntent = null;
            if (!AstrosageKundliApplication.isAgoraCallScreenOpened) {
                String status = intent.getStringExtra(CGlobalVariables.STATUS);
                String consultationType = intent.getStringExtra(CGlobalVariables.AGORA_CALL_TYPE);
                String agoraCallSId = intent.getStringExtra(CGlobalVariables.AGORA_CALLS_ID);
                String agoraToken = intent.getStringExtra(CGlobalVariables.AGORA_TOKEN);
                String agoraTokenId = intent.getStringExtra(CGlobalVariables.AGORA_TOKEN_ID);
                String astrologerName = intent.getStringExtra(CGlobalVariables.ASTROLOGER_NAME);
                String astrologerProfileUrl = intent.getStringExtra(CGlobalVariables.ASTROLOGER_PROFILE_URL);
                String agoraCallDuration = intent.getStringExtra(CGlobalVariables.AGORA_CALL_DURATION);
                String astrologerId = intent.getStringExtra(CGlobalVariables.ASTROLOGER_ID);
                if (status.equals(CGlobalVariables.ACCEPTED)) {
                    if(consultationType.equals(CGlobalVariables.TYPE_VIDEO_CALL)){
                         resultIntent = new Intent(SuperBaseActivity.this, VideoCallActivity.class);
                    }else{
                         resultIntent = new Intent(SuperBaseActivity.this, VoiceCallActivity.class);
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString(CGlobalVariables.AGORA_CALLS_ID, agoraCallSId);
                    bundle.putString(CGlobalVariables.AGORA_TOKEN, agoraToken);
                    bundle.putString(CGlobalVariables.AGORA_TOKEN_ID, agoraTokenId);
                    bundle.putString(CGlobalVariables.ASTROLOGER_NAME, astrologerName);
                    bundle.putString(CGlobalVariables.ASTROLOGER_PROFILE_URL, astrologerProfileUrl);
                    bundle.putString(CGlobalVariables.AGORA_CALL_DURATION, agoraCallDuration);
                    bundle.putString(CGlobalVariables.ASTROLOGER_ID, astrologerId);
                    resultIntent.setPackage(BuildConfig.APPLICATION_ID);
                    resultIntent.putExtras(bundle);

                    startActivity(resultIntent);
                } else if (status.equals(CGlobalVariables.REJECTED)) {
                   // Log.d("testAgoraCall","Called broadcasteReceiver 5");
                    //Toast.makeText(SuperBaseActivity.this, "Agora Call Rejected by astrologer", Toast.LENGTH_LONG).show();
                }
            }
           // Log.d("testAgoraCall","Called broadcasteReceiver 6");
            AstrosageKundliApplication.isAgoraCallScreenOpened = true;
        }
    };


    public void clickHome(){
        Intent intent = new Intent(this, ActAppModule.class);
        startActivity(intent);
    }

    public void clickCall(){
        CUtils.switchToConsultTab(FILTER_TYPE_CALL,this);
    }

    public void clickChat(){
        CUtils.switchToConsultTab(FILTER_TYPE_CHAT,this);
    }

    public void clickLive() {
        if(liveAstrologerModelArrayList != null && !liveAstrologerModelArrayList.isEmpty()){
            checkPermissions(liveAstrologerModelArrayList.get(0));
        } else {
            startActivity(new Intent(this, AllLiveAstrologerActivity.class));
        }
    }

    public void clickHistory(){
        boolean isLogin;
        isLogin = CUtils.getUserLoginStatus(this);
        if (!isLogin) {
            CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_NAV_SIGNUP,
                    CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
            Intent intent = new Intent(this, LoginSignUpActivity.class);
            intent.putExtra(IS_GOOGLE_FACEBOOK_VISIBLE, true);
            startActivity(intent);
        } else {
            CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_NAV_HISTORY, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
            com.ojassoft.astrosage.utils.CUtils.createSession(this, com.ojassoft.astrosage.utils.CGlobalVariables.AK_HOME_BOTTOM_BAR_HISTORY_PARTNER_ID);
            startActivity(new Intent(this, ConsultantHistoryActivity.class));
        }
    }


    // SAN For SCALE CHANGE
    public void checkFontConfigration(){
        try {
            Configuration configuration = getResources().getConfiguration();
            if (isFontSizeChange && fontSizeChange != 0) {
                //Log.e("SAN ", "font size keep in old format ");
                configuration.fontScale = fontSizeChange;
                DisplayMetrics metrics = getResources().getDisplayMetrics();
                WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
                wm.getDefaultDisplay().getMetrics(metrics);
                metrics.scaledDensity = configuration.fontScale * metrics.density;
                getResources().updateConfiguration(configuration, metrics);
                isFontSizeChange = false;
                fontSizeChange = 0.0f;
            }
        } catch (Exception e){
            //
        }
    }

    public void checkPermissions(LiveAstrologerModel liveAstrologerModel) {
        AstrosageKundliApplication.liveAstrologerModel = liveAstrologerModel;
        openLiveStramingScreen();
//        boolean granted = true;
//        for (String per : PERMISSIONS) {
//            if (!permissionGranted(per)) {
//                granted = false;
//                break;
//            }
//        }
//
//        if (granted) {
//            Log.e("liveAstrologerModel", "checkPermissions2");
//            openLiveStramingScreen();
//        } else {
//            requestPermissions();
//        }
    }

//    private final String[] PERMISSIONS = {
//            Manifest.permission.RECORD_AUDIO,
//            Manifest.permission.CAMERA
//    };
//
//    private boolean permissionGranted(String permission) {
//        return ContextCompat.checkSelfPermission(
//                this, permission) == PackageManager.PERMISSION_GRANTED;
//    }
//
//    private void requestPermissions() {
//        ActivityCompat.requestPermissions(this, PERMISSIONS, PERMISSION_REQ_CODE);
//    }

//    private void toastNeedPermissions() {
//        //openAlertDialogForOpenSetting();
//        com.ojassoft.astrosage.utils.CUtils.showSnackbar(findViewById(android.R.id.content), getString(R.string.need_necessary_permissions), this);
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        if (grantResults != null && grantResults.length > 0) {
//            if (requestCode == PERMISSION_REQ_CODE) {
//                boolean granted = true;
//                for (int result : grantResults) {
//                    granted = (result == PackageManager.PERMISSION_GRANTED);
//                    if (!granted) break;
//                }
//
//                if (granted) {
//                    openLiveStramingScreen();
//                } else {
//                    toastNeedPermissions();
//                }
//            }
//        }
//    }

    private void openLiveStramingScreen() {
        try {
            if (AstrosageKundliApplication.liveAstrologerModel == null) {
                return;
            }
            com.ojassoft.astrosage.varta.utils.CUtils.openLiveActivity(this);
        } catch (Exception e) {
        }
    }

}
