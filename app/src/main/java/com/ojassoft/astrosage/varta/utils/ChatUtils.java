package com.ojassoft.astrosage.varta.utils;

import static com.ojassoft.astrosage.utils.CGlobalVariables.BACK_FROM_PROFILE_CHAT_DIALOG;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_PASSWORD;
import static com.ojassoft.astrosage.utils.CUtils.isCompleteUserData;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.DEVICE_ID;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.FILTER_TYPE_CHAT;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.TYPE_AI_CALL_RANDOM;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.TYPE_AI_CHAT;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.TYPE_AI_CHAT_RANDOM;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.TYPE_CHAT;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.TYPE_CHAT_RANDOM;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.TYPE_VIDEO_CALL;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.TYPE_VOICE_CALL;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.USER_SPEED;
import static com.ojassoft.astrosage.varta.utils.CUtils.errorLogs;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.android.volley.VolleyError;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.act.ActAppModule;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.varta.dialog.AstroBusyAlertDialog;
import com.ojassoft.astrosage.varta.dialog.CallInitiatedDialog;
import com.ojassoft.astrosage.varta.dialog.CallMsgDialog;
import com.ojassoft.astrosage.varta.dialog.InSufficientBalanceDialog;
import com.ojassoft.astrosage.varta.dialog.LowBalanceSubscribeAiAstroDialog;
import com.ojassoft.astrosage.varta.model.AstrologerDetailBean;
import com.ojassoft.astrosage.varta.model.ConnectAgoraCallBean;
import com.ojassoft.astrosage.varta.model.ConnectChatBean;
import com.ojassoft.astrosage.varta.model.UserProfileData;
import com.ojassoft.astrosage.varta.service.AgoraCallInitiateService;
import com.ojassoft.astrosage.varta.service.AstroAcceptRejectService;
import com.ojassoft.astrosage.varta.ui.activity.AIChatWindowActivity;
import com.ojassoft.astrosage.varta.ui.activity.AINotificationChatActivity;
import com.ojassoft.astrosage.varta.ui.activity.AIVoiceCallingActivity;
import com.ojassoft.astrosage.varta.ui.activity.AstrologerDescriptionActivity;
import com.ojassoft.astrosage.varta.ui.activity.ChatWindowActivity;
import com.ojassoft.astrosage.varta.ui.activity.DashBoardActivity;
import com.ojassoft.astrosage.varta.ui.activity.FirstTimeProfileDetailsActivity;
import com.ojassoft.astrosage.varta.ui.fragments.HomeFragment1;
import com.ojassoft.astrosage.varta.ui.fragments.VartaHomeFragment;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/* Must Read
 *  This is helper class for chat process, api call and firebase authentication implemented
 *  Profile dialog also open from here
 *
 * */

public class ChatUtils implements VolleyResponse {
    private static final int GET_FIREBASE_AUTH_TOKEN = 23;
    private static Activity activity;
    private CustomProgressDialog progressDialog;
    CustomProgressDialog pd;

    private final int CONNECT_CHAT_API_RESPONSE = 4004;
    private final int CONNECT_AUDIO_VIDEO_CALL_API_RESPONSE = 4005;
    private CallInitiatedDialog callInitiatedDialog;
    private static ChatUtils chatUtils;
    public String consultationType;
    boolean isOpenProfile = true;

    public static ChatUtils getInstance(Activity activity) {
        ChatUtils.activity = activity;
        if (chatUtils == null) {
            chatUtils = new ChatUtils();
        }
        return chatUtils;
    }

    public void initChat(AstrologerDetailBean chatAstrologerDetailBean) {
        try {
            AstrosageKundliApplication.backgroundLoginCountForChat = 0;
            consultationType = CGlobalVariables.TYPE_CHAT;
            AstrosageKundliApplication.currentChatStatus = CGlobalVariables.CHAT_INITIATE;
            AstrosageKundliApplication.isChatScreenOpes = false;
            AstrosageKundliApplication.currentConsultType = "chat";
            AstrosageKundliApplication.endChatTimeShowMilliSeconds = TimeUnit.MINUTES.toMillis(1);

            AstrosageKundliApplication.selectedAstrologerDetailBean = getAstrologerBeanObject(chatAstrologerDetailBean);
            AstrosageKundliApplication.chatAgainAstrologerDetailBean = getAstrologerBeanObject(chatAstrologerDetailBean);
            processConsultation();
        } catch (Exception e) {
            //
        }
    }

    public void initAIChat(AstrologerDetailBean chatAstrologerDetailBean) {
        try {
            AstrosageKundliApplication.backgroundLoginCountForChat = 0;
            consultationType = CGlobalVariables.TYPE_AI_CHAT;
            AstrosageKundliApplication.currentChatStatus = CGlobalVariables.CHAT_INITIATE;
            AstrosageKundliApplication.isChatScreenOpes = false;
            AstrosageKundliApplication.currentConsultType = "chat";
            AstrosageKundliApplication.endChatTimeShowMilliSeconds = TimeUnit.MINUTES.toMillis(1);
            AstrosageKundliApplication.selectedAstrologerDetailBean = chatAstrologerDetailBean;
            AstrosageKundliApplication.chatAgainAstrologerDetailBean = chatAstrologerDetailBean;
            processConsultation();
        } catch (Exception e) {
            //
        }
    }

    /**
     * to initiate random chat with AI astrologer
     */
    public void initAIChatRandomAstrologer(String apiCallingSource, String configType) {
        try {
            CUtils.isFreeConsultationStarted = true;
            com.ojassoft.astrosage.varta.utils.CUtils.isAutoConsultationConnected = true;
            AstrosageKundliApplication.backgroundLoginCountForChat = 0;
            consultationType = CGlobalVariables.TYPE_AI_CHAT_RANDOM;
            AstrosageKundliApplication.currentChatStatus = CGlobalVariables.CHAT_INITIATE;
            AstrosageKundliApplication.isChatScreenOpes = false;
            AstrosageKundliApplication.currentConsultType = "chat";
            AstrosageKundliApplication.endChatTimeShowMilliSeconds = TimeUnit.MINUTES.toMillis(1);
            AstrosageKundliApplication.selectedAstrologerDetailBean = null;
            AstrosageKundliApplication.chatAgainAstrologerDetailBean = null;
            if (CUtils.getProfileForChatFromPreference(activity) != null && !TextUtils.isEmpty(CUtils.getProfileForChatFromPreference(activity).getName())) {
                UserProfileData userProfileData = CUtils.getUserSelectedProfileFromPreference(activity);
                if (userProfileData != null) {
                    userProfileData.setProfileSendToAstrologer(true);
                    CUtils.saveUserSelectedProfileInPreference(activity, userProfileData);
                    CUtils.saveProfileForChatInPreference(activity, userProfileData);
                }
                startAIChatRandom(userProfileData, apiCallingSource);
            } else {
                AstrosageKundliApplication.apiCallingSource = apiCallingSource;
                //replace ProfileForChat call with FirstTimeProfileDetailsActivity
                //CUtils.openProfileOrKundliAct(activity, CGlobalVariables.TYPE_AI_CHAT_RANDOM, consultationType, BACK_FROM_PROFILE_CHAT_DIALOG);
                CUtils.openFirstTimeProfileActivity(activity, configType, BACK_FROM_PROFILE_CHAT_DIALOG);
            }
        } catch (Exception e) {
//             Log.d("TestLogsDeepCopy","Exception e 1"+e);
        }
    }


    /**
     * to initiate random call with AI astrologer
     */
    public void initAICallRandomAstrologer(String apiCallingSource, String configType) {
        try {
            CUtils.isFreeConsultationStarted = true;
            com.ojassoft.astrosage.varta.utils.CUtils.isAutoConsultationConnected = true;
            AstrosageKundliApplication.backgroundLoginCountForChat = 0;
            consultationType = TYPE_AI_CALL_RANDOM;
            AstrosageKundliApplication.currentConsultType = CGlobalVariables.AI_AUDIO_CALL_TEXT;
            AstrosageKundliApplication.currentEventType = CGlobalVariables.AI_CALL_RANDOM;
            AstrosageKundliApplication.selectedAstrologerDetailBean = null;
            AstrosageKundliApplication.chatAgainAstrologerDetailBean = null;

            if (CUtils.getProfileForChatFromPreference(activity) != null && !TextUtils.isEmpty(CUtils.getProfileForChatFromPreference(activity).getName())) {
                UserProfileData userProfileData = CUtils.getUserSelectedProfileFromPreference(activity);
                if (userProfileData != null) {
                    userProfileData.setProfileSendToAstrologer(true);
                    CUtils.saveUserSelectedProfileInPreference(activity, userProfileData);
                    CUtils.saveProfileForChatInPreference(activity, userProfileData);
                }
                if(CUtils.checkPermissionsAudio(activity)){
                    connectAIVoiceCallRandom(userProfileData, apiCallingSource);
                }else {
                    ChatUtils.getInstance(activity).initAIChatRandomAstrologer(apiCallingSource + "_connect_chat", "");
                }
            } else {
                AstrosageKundliApplication.apiCallingSource = apiCallingSource;
                CUtils.openFirstTimeProfileActivity(activity, configType, BACK_FROM_PROFILE_CHAT_DIALOG);
            }
        } catch (Exception e) {
            //
        }
    }
    /**
     * to initiate random chat with human astrologer
     */
    public void initChatRandomAstrologer(String apiCallingSource, String configType) {
        try {
            com.ojassoft.astrosage.varta.utils.CUtils.isAutoConsultationConnected = true;
            AstrosageKundliApplication.backgroundLoginCountForChat = 0;
            consultationType = CGlobalVariables.TYPE_CHAT_RANDOM;
            AstrosageKundliApplication.currentChatStatus = CGlobalVariables.CHAT_INITIATE;
            AstrosageKundliApplication.isChatScreenOpes = false;
            AstrosageKundliApplication.currentConsultType = "chat";
            AstrosageKundliApplication.endChatTimeShowMilliSeconds = TimeUnit.MINUTES.toMillis(1);
            AstrosageKundliApplication.selectedAstrologerDetailBean = null;
            AstrosageKundliApplication.chatAgainAstrologerDetailBean = null;
            if (CUtils.getProfileForChatFromPreference(activity) != null && !TextUtils.isEmpty(CUtils.getProfileForChatFromPreference(activity).getName())) {
                UserProfileData userProfileData = CUtils.getUserSelectedProfileFromPreference(activity);
                if (userProfileData != null) {
                    userProfileData.setProfileSendToAstrologer(true);
                    CUtils.saveUserSelectedProfileInPreference(activity, userProfileData);
                    CUtils.saveProfileForChatInPreference(activity, userProfileData);
                }
                startChatRandom(userProfileData, apiCallingSource);
                //CUtils.openProfileOrKundliAct(activity, CGlobalVariables.TYPE_CHAT_RANDOM, consultationType, BACK_FROM_PROFILE_CHAT_DIALOG);
            } else {
                AstrosageKundliApplication.apiCallingSource = apiCallingSource;
                //replace ProfileForChat call with FirstTimeProfileDetailsActivity
                //CUtils.openProfileOrKundliAct(activity, CGlobalVariables.TYPE_CHAT_RANDOM, consultationType, BACK_FROM_PROFILE_CHAT_DIALOG);
                CUtils.openFirstTimeProfileActivity(activity, configType, BACK_FROM_PROFILE_CHAT_DIALOG);
            }
        } catch (Exception e) {
//             Log.d("TestLogsDeepCopy","Exception e 1"+e);
        }
    }


    private AstrologerDetailBean getAstrologerBeanObject(AstrologerDetailBean chatAstrologerDetailBean) {
        try {
            AstrologerDetailBean astrologerDetailBean = new AstrologerDetailBean();
            astrologerDetailBean.setName(chatAstrologerDetailBean.getName());
            astrologerDetailBean.setExperience(chatAstrologerDetailBean.getExperience());
            astrologerDetailBean.setLanguage(chatAstrologerDetailBean.getLanguage());
            astrologerDetailBean.setDesignation(chatAstrologerDetailBean.getDesignation());
            astrologerDetailBean.setImageFile(chatAstrologerDetailBean.getImageFile());
            astrologerDetailBean.setServicePrice(chatAstrologerDetailBean.getServicePrice());
            astrologerDetailBean.setRating(chatAstrologerDetailBean.getRating());
            astrologerDetailBean.setEmail(chatAstrologerDetailBean.getEmail());
            astrologerDetailBean.setUrlText(chatAstrologerDetailBean.getUrlText());
            astrologerDetailBean.setPhoneNumber(chatAstrologerDetailBean.getPhoneNumber());
            astrologerDetailBean.setIsOnline(chatAstrologerDetailBean.getIsOnline());
            astrologerDetailBean.setIsBusy(chatAstrologerDetailBean.getIsBusy());
            astrologerDetailBean.setDoubleRating(chatAstrologerDetailBean.getDoubleRating());
            astrologerDetailBean.setAstroWalletId(chatAstrologerDetailBean.getAstroWalletId());
            astrologerDetailBean.setAstrologerId(chatAstrologerDetailBean.getAstrologerId());
            astrologerDetailBean.setExpertise(chatAstrologerDetailBean.getExpertise());
            astrologerDetailBean.setOfferRemaining(chatAstrologerDetailBean.isOfferRemaining());
            astrologerDetailBean.setUseIntroOffer(chatAstrologerDetailBean.getUseIntroOffer());
            astrologerDetailBean.setFreeForCall(chatAstrologerDetailBean.isFreeForCall());
            astrologerDetailBean.setFreeForChat(chatAstrologerDetailBean.isFreeForChat());
            astrologerDetailBean.setIntroOfferType(chatAstrologerDetailBean.getIntroOfferType());
            astrologerDetailBean.setTotalRating(chatAstrologerDetailBean.getTotalRating());
            astrologerDetailBean.setFollowCount(chatAstrologerDetailBean.getFollowCount());
            astrologerDetailBean.setManipulatedRank(chatAstrologerDetailBean.getManipulatedRank());

            if (chatAstrologerDetailBean.getCatId() != null) {
                astrologerDetailBean.setCatId(chatAstrologerDetailBean.getCatId());
            }

            if (chatAstrologerDetailBean.getWaitTime() != null) {
                astrologerDetailBean.setWaitTime(chatAstrologerDetailBean.getWaitTime());
            }

            if (chatAstrologerDetailBean.getIsVerified() != null) {
                astrologerDetailBean.setIsVerified(chatAstrologerDetailBean.getIsVerified());
            }

            if (chatAstrologerDetailBean.getActualServicePriceInt() != null) {
                astrologerDetailBean.setActualServicePriceInt(chatAstrologerDetailBean.getActualServicePriceInt());
            }

            if (chatAstrologerDetailBean.getIsAvailableForChat() != null) {
                astrologerDetailBean.setIsAvailableForChat(chatAstrologerDetailBean.getIsAvailableForChat());
            }

            if (chatAstrologerDetailBean.getIsAvailableForCall() != null) {
                astrologerDetailBean.setIsAvailableForCall(chatAstrologerDetailBean.getIsAvailableForCall());
            }
            if (chatAstrologerDetailBean.getCallSource() != null) {
                astrologerDetailBean.setCallSource(chatAstrologerDetailBean.getCallSource());
            }
            astrologerDetailBean.setFeatured(chatAstrologerDetailBean.isFeatured());
            astrologerDetailBean.setAiAstrologerId(chatAstrologerDetailBean.getAiAstrologerId());

            return astrologerDetailBean;
        } catch (Exception e) {
            //Log.d("TestLogsDeepCopy","Exception e 2"+e);

        }
        return chatAstrologerDetailBean;
    }

    private void processConsultation() {
        if (AstrosageKundliApplication.selectedAstrologerDetailBean != null) {
            errorLogs = errorLogs + " call openProfileOrKundliAct \n";
            String aiai = AstrosageKundliApplication.selectedAstrologerDetailBean.getAiAstrologerId();
            if (consultationType.equals(CGlobalVariables.TYPE_VOICE_CALL) || consultationType.equals(TYPE_VIDEO_CALL)) {
                if (!TextUtils.isEmpty(aiai) && !aiai.equals("0")) {
                    UserProfileData userProfileData = CUtils.getUserSelectedProfileFromPreference(activity);
                    if (isCompleteUserData(userProfileData)) {
                        startAudioCall(userProfileData);
                        return;
                    }
                }
                CUtils.openProfileOrKundliAct(activity, AstrosageKundliApplication.selectedAstrologerDetailBean.getUrlText(), consultationType, BACK_FROM_PROFILE_CHAT_DIALOG);
            } else {
                if (activity instanceof AIChatWindowActivity && ((AIChatWindowActivity) activity).isChatAgainClicked) {
                    callStartChat(true);
                } else if (isCompleteUserData(CUtils.getUserSelectedProfileFromPreference(activity))) {
                    callStartChat(!TextUtils.isEmpty(aiai) && !aiai.equals("0"));
                } else {
                    CUtils.openProfileOrKundliAct(activity, AstrosageKundliApplication.selectedAstrologerDetailBean.getUrlText(), consultationType, BACK_FROM_PROFILE_CHAT_DIALOG);
                }
            }
        }
    }

    /*public void checkFirebaseAuthentication() {
        FirebaseAuth firebaseAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
        if (firebaseUser == null) {
            isOpenProfile = true;
            fetchFirebaseAuthTokenFromServer(activity);
        } else {
            if (AstrosageKundliApplication.selectedAstrologerDetailBean != null) {
                errorLogs = errorLogs + " call openProfileOrKundliAct \n";
                String aiai = AstrosageKundliApplication.selectedAstrologerDetailBean.getAiAstrologerId();

                if (consultationType.equals(CGlobalVariables.TYPE_VOICE_CALL) || consultationType.equals(TYPE_VIDEO_CALL)) {
                    CUtils.openProfileOrKundliAct(activity, AstrosageKundliApplication.selectedAstrologerDetailBean.getUrlText(), consultationType, BACK_FROM_PROFILE_CHAT_DIALOG);
                } else {
                    if (activity instanceof AIChatWindowActivity && ((AIChatWindowActivity) activity).isChatAgainClicked) {
                        callStartChat(true);
                    } else if (CUtils.getProfileForChatFromPreference(activity) != null && !TextUtils.isEmpty(CUtils.getProfileForChatFromPreference(activity).getName())) {
                        callStartChat(!TextUtils.isEmpty(aiai) && !aiai.equals("0"));
                    } else {
                        CUtils.openProfileOrKundliAct(activity, AstrosageKundliApplication.selectedAstrologerDetailBean.getUrlText(), consultationType, BACK_FROM_PROFILE_CHAT_DIALOG);
                    }
                }
            }
            firebaseUser.getIdToken(true).addOnSuccessListener(new OnSuccessListener<GetTokenResult>() {
                @Override
                public void onSuccess(GetTokenResult result) {
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull @NotNull Exception e) {
                    isOpenProfile = false;
                    fetchFirebaseAuthTokenFromServer(activity);
                }
            });
        }
    }*/

    public void callStartChat(boolean isAIAstrologer) {
        AstrosageKundliApplication.backgroundLoginCountForChat = 0;
        UserProfileData userProfileData = CUtils.getUserSelectedProfileFromPreference(activity);
        if (userProfileData != null) {
            userProfileData.setProfileSendToAstrologer(true);
            CUtils.saveUserSelectedProfileInPreference(activity, userProfileData);
            CUtils.saveProfileForChatInPreference(activity, userProfileData);
        }
        if (isAIAstrologer) {
            startAIChat(userProfileData);
        } else {
            startChat(userProfileData);
        }
    }

    private void fetchFirebaseAuthTokenFromServer(Activity activity) {

        if (CUtils.isConnectedWithInternet(activity)) {
            if (TextUtils.isEmpty(CUtils.getUserLoginPassword(activity))) {
                return;
            }
            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.fetchFireBaseToken(setAstrologerInviteParams());
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        Log.d("testFirebaseAuth123", " Response is ==>> " + response);
                        String myRespose = response.body().string();
                        JSONObject jsonObject = new JSONObject(myRespose);
                        if (jsonObject.has("status")) {
                            String status = jsonObject.getString("status");
                            if (status.equals("1")) {
                                String authToken = jsonObject.getString("token");
                                firebaseSignIn(authToken);
                            } else {
                                if (AstrosageKundliApplication.selectedAstrologerDetailBean != null) {
                                    hideProgressBarCustomProgressDialog();
                                    callMsgDialogData(activity.getString(R.string.msg_auth_failed), "", true, CGlobalVariables.CHAT_CLICK);
                                    AstrosageKundliApplication.selectedAstrologerDetailBean = null;
                                } else {
                                    //Log.e("SAN CI DA ", " onResponse nothing write "  );
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    stopProgressBar();
                }
            });

            //CUtils.getFirebaseAuthToken(this, GET_FIREBASE_AUTH_TOKEN);
        } else {
            Toast.makeText(activity, activity.getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }
    }

    public Map<String, String> setAstrologerInviteParams() {

        Context context = AstrosageKundliApplication.getAppContext();
        if (context == null) return null;
        String deviceId = CUtils.getMyAndroidId(context);
        String key = CUtils.getApplicationSignatureHashCode(context);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(context));
        params.put(CGlobalVariables.PHONE_NO, CUtils.getUserID(context));
        params.put(KEY_PASSWORD, CUtils.getUserLoginPassword(context));
        params.put(CGlobalVariables.KEY_API, key);
        params.put(DEVICE_ID, deviceId);
        params.put("packageName", BuildConfig.APPLICATION_ID);
        params.put(CGlobalVariables.PROPERTY_APP_VERSION, BuildConfig.VERSION_NAME);
        params.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));

        //Log.e("FirebaseAccessToken 3", " params= "+params);
        return params;
    }

    /**
     * All Api Response
     *
     * @param response
     * @param method
     */
    @Override
    public void onResponse(String response, int method) {
        //Log.d("testVideoCall", "Response is All ==>>>>>" + response);

//        if (method == GET_FIREBASE_AUTH_TOKEN) {
//            try {
//                Log.d("testFirebaseAuth"," Response is ==>> "+response);
//                JSONObject jsonObject = new JSONObject(response);
//                if (jsonObject.has("status")) {
//                    String status = jsonObject.getString("status");
//                    if (status.equals("1")) {
//                        String authToken = jsonObject.getString("token");
//                        firebaseSignIn(authToken);
//                    } else {
//                        if (AstrosageKundliApplication.selectedAstrologerDetailBean != null) {
//                            hideProgressBarCustomProgressDialog();
//                            callMsgDialogData(activity.getString(R.string.msg_auth_failed), "", true, CGlobalVariables.CHAT_CLICK);
//                            AstrosageKundliApplication.selectedAstrologerDetailBean = null;
//                        } else {
//                            //Log.e("SAN CI DA ", " onResponse nothing write "  );
//                        }
//                    }
//                }
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        } else

        if (method == CONNECT_CHAT_API_RESPONSE) {
            //chatInitResponseTime = Calendar.getInstance().getTimeInMillis() - chatInitResponseTime;
            /*stopProgressBar();
            try {
                //Log.e("TestStatus", " onresponse method == CONNECT_CHAT_API_RESPONSE response " + response );
                JSONObject jsonObject = new JSONObject(response);
                String status = jsonObject.getString("status");
                String message = jsonObject.getString("msg");
                boolean isChatStarted = false;
                AstrosageKundliApplication.currentChatStatus = "";
                if (status.equals(CGlobalVariables.CHAT_INITIATED)) {
                    AstrosageKundliApplication.currentChatStatus = CGlobalVariables.CHAT_STARTED;
                    isChatStarted = true;
                    initChatAfterResponse(jsonObject);
                } else if (status.equals(CGlobalVariables.FREE_CONSULTATION_NOT_AVAILABLE)) {
                    CUtils.fcmAnalyticsEvents("chat_api_res_free_consultation_not_available", AstrosageKundliApplication.currentEventType, "");
                    String dialogMsg = activity.getResources().getString(R.string.unable_to_connect_free_consultation);
                    callMsgDialogData(dialogMsg, "", true, CGlobalVariables.CHAT_CLICK);
                } else if (status.equals(CGlobalVariables.INSUFFICIENT_BALANCE)) {
                    CUtils.fcmAnalyticsEvents("chat_api_res_insufficient_bal", AstrosageKundliApplication.currentEventType, "");
                    //stopActivityIndicator();
                    String astrologerName = jsonObject.getString("astrologername");
                    String userbalance = jsonObject.getString("userbalance");
                    String minBalance = jsonObject.getString("minbalance");

                    InSufficientBalanceDialog dialog = new InSufficientBalanceDialog(astrologerName, userbalance, minBalance, "chat_global");
                    FragmentManager ft = ((FragmentActivity) activity).getSupportFragmentManager();
                    dialog.show(ft, "Dialog");
                } else if (status.equals(CGlobalVariables.ASTROLOGER_BUSY_OFFLINE)) {

                    String fcm_label = "chat_api_res_" + message;
                    CUtils.fcmAnalyticsEvents(fcm_label, AstrosageKundliApplication.currentEventType, "");
                    //stopActivityIndicator();
                    String dialogMsg = "";
                    if (message.equalsIgnoreCase(CGlobalVariables.USER_BLOCKED_MSG)) {
                        dialogMsg = activity.getResources().getString(R.string.user_blocked_msg);
                    } else if (message.equalsIgnoreCase(CGlobalVariables.ASTROLOGER_STATUS_BUSY_MSG)) {
                        dialogMsg = activity.getResources().getString(R.string.astrologer_busy_msg_chat);
                    } else if (message.equalsIgnoreCase(CGlobalVariables.ASTROLOGER_STATUS_OFFLINE_MSG)) {
                        String stringMsg = activity.getResources().getString(R.string.astrologer_offline_msg);
                        dialogMsg = stringMsg;
                    } else if (message.equalsIgnoreCase(CGlobalVariables.ASTROLOGER_STATUS_DISABLED)) {
                        dialogMsg = activity.getResources().getString(R.string.astrologer_status_disable);
                    } else if (message.equalsIgnoreCase(CGlobalVariables.CALL_API_FAILED)) {
                        dialogMsg = activity.getResources().getString(R.string.call_api_failed_msg);
                    } else if (message.equalsIgnoreCase(CGlobalVariables.USE_ANOTHER_CALL_MSG)) {
                        dialogMsg = activity.getResources().getString(R.string.existing_chat_msg);
                    } else {
                        dialogMsg = activity.getResources().getString(R.string.something_wrong_error);
                    }

                    //destroyChatFragment();
                    callMsgDialogData(dialogMsg, "", true, CGlobalVariables.CHAT_CLICK);
                    getVisibleFragmentAndUpdateStatus();
                } else if (status.equals(CGlobalVariables.UNABLE_TO_CONNECT_CHAT)) {
                    CUtils.fcmAnalyticsEvents("chat_api_response_status_3", AstrosageKundliApplication.currentEventType, "");
                    //stopActivityIndicator();
                    String dialogMsg = "";
                    dialogMsg = activity.getResources().getString(R.string.unable_to_connect_chat);
                    dialogMsg = dialogMsg.replace("#", CGlobalVariables.UNABLE_TO_CONNECT_CHAT);
                    callMsgDialogData(dialogMsg, "", true, CGlobalVariables.CHAT_CLICK);
                } else {
                    CUtils.fcmAnalyticsEvents("chat_api_response_status_error", AstrosageKundliApplication.currentEventType, "");
                    Toast.makeText(activity, "Authentication issue, please try again! ( Error code: " + status + " )", Toast.LENGTH_LONG).show();
                }
                try {
                    if(!isChatStarted){
                        if(activity instanceof ChatWindowActivity){
                            ((ChatWindowActivity) activity).showChatGainButton();
                        }
                    }

                }catch (Exception e){
                    //
                }
            } catch (Exception e) {
                CUtils.fcmAnalyticsEvents("chat_api_response_status_exception", AstrosageKundliApplication.currentEventType, "");
                //stopActivityIndicator();
                e.printStackTrace();
                //Log.e("SAN CI DA ", " onresponse method == CONNECT_CHAT_API_RESPONSE exp " + e.toString() );
            }*/
        } else if (method == CONNECT_AUDIO_VIDEO_CALL_API_RESPONSE) {
//            stopProgressBar();
//            try {
//                errorLogs = errorLogs + "api response->"+response+"\n";
//                //Log.d("xxccddssff","Response is ==>>>>>" + response);
//               //Log.e("TestChat", "Response is ==>>>>>" + response);
//                JSONObject jsonObject = new JSONObject(response);
//                String status = jsonObject.getString("status");
//                String message = jsonObject.getString("msg");
//                if (status.equals(CGlobalVariables.AGORA_CALL_INITIATED)) {
//                    CUtils.fcmAnalyticsEvents(CGlobalVariables.AGORA_CALL_RES_AUDIO_VIDEO_INITIATED, AstrosageKundliApplication.currentEventType, "");
//                    initAudioVideoCallAfterResponse(jsonObject);
//                } else if (status.equals(CGlobalVariables.CALL_INITIATED)) {
//                    CUtils.fcmAnalyticsEvents(CGlobalVariables.AGORA_CALL_RES_NETWORK_PHONE_INITIATED, AstrosageKundliApplication.currentEventType, "");
//
//                    if (activity instanceof AstrologerDescriptionActivity) {
//                        ((AstrologerDescriptionActivity) activity).responseNetworkCall(response);
//                    }
//                    if (activity instanceof DashBoardActivity) {
//                        getVisibleFragment(response);
//                    }
//                    if (activity instanceof ActAppModule) {
//                        getVisibleFragment(response);
//                    }
//                    if (activity instanceof ChatWindowActivity) {
//                        activity.finish();
//                    }
//                } else if (status.equals(CGlobalVariables.INSUFFICIENT_BALANCE)) {
//                    CUtils.fcmAnalyticsEvents(CGlobalVariables.AGORA_CALL_RES_INSUFF_BAL, AstrosageKundliApplication.currentEventType, "");
//                    //stopActivityIndicator();
//                    String astrologerName = jsonObject.getString("astrologername");
//                    String userbalance = jsonObject.getString("userbalance");
//                    String minBalance = jsonObject.getString("minbalance");
//
//                    InSufficientBalanceDialog dialog = new InSufficientBalanceDialog(astrologerName, userbalance, minBalance, "chat_global");
//                    FragmentManager ft = ((FragmentActivity) activity).getSupportFragmentManager();
//                    dialog.show(ft, "Dialog");
//
//                } else if (status.equals(CGlobalVariables.ASTROLOGER_BUSY_OFFLINE)) {
//                    String fcm_label = CGlobalVariables.VIDEO_CALL_API_RES_MSG + message;
//                    CUtils.fcmAnalyticsEvents(fcm_label, AstrosageKundliApplication.currentEventType, "");
//                    //stopActivityIndicator();
//                    String dialogMsg = "";
//                    if (message.equalsIgnoreCase(CGlobalVariables.USER_BLOCKED_MSG)) {
//                        dialogMsg = activity.getResources().getString(R.string.user_blocked_msg);
//                    } else if (message.equalsIgnoreCase(CGlobalVariables.ASTROLOGER_STATUS_BUSY_MSG)) {
//                        dialogMsg = activity.getResources().getString(R.string.astrologer_busy_msg_chat);
//                    } else if (message.equalsIgnoreCase(CGlobalVariables.ASTROLOGER_STATUS_OFFLINE_MSG)) {
//                        String stringMsg = "";
//                        if ((AstrosageKundliApplication.selectedAstrologerDetailBean.getIsAvailableForChat().equalsIgnoreCase("true")) && (AstrosageKundliApplication.selectedAstrologerDetailBean.getIsAvailableForCall().equalsIgnoreCase("false"))) {
//                            stringMsg = activity.getResources().getString(R.string.astrologer_offline_call);
//                        } else if ((AstrosageKundliApplication.selectedAstrologerDetailBean.getIsAvailableForChat().equalsIgnoreCase("false")) && (AstrosageKundliApplication.selectedAstrologerDetailBean.getIsAvailableForCall().equalsIgnoreCase("true"))) {
//                            stringMsg = activity.getResources().getString(R.string.astrologer_offline_chat);
//                        } else {
//                            stringMsg = activity.getResources().getString(R.string.astrologer_offline_msg);
//                        }
//                        dialogMsg = stringMsg;
//                    } else if (message.equalsIgnoreCase(CGlobalVariables.ASTROLOGER_STATUS_DISABLED)) {
//                        dialogMsg = activity.getResources().getString(R.string.astrologer_status_disable);
//                    } else if (message.equalsIgnoreCase(CGlobalVariables.CALL_API_FAILED)) {
//                        dialogMsg = activity.getResources().getString(R.string.call_api_failed_msg);
//                    } else if (message.equalsIgnoreCase(CGlobalVariables.USE_ANOTHER_CALL_MSG)) {
//                        dialogMsg = activity.getResources().getString(R.string.existing_chat_msg);
//                    } else if (message.equalsIgnoreCase(CGlobalVariables.ASTROLOGER_STATUS_UNAVAILABLE)) {
//                        dialogMsg = activity.getResources().getString(R.string.astrologer_unavailable_video_call);
//                    } else {
//                        dialogMsg = activity.getResources().getString(R.string.existing_chat_msg);
//                    }
//
//                    //destroyChatFragment();
//                    callMsgDialogData(dialogMsg, "", true, CGlobalVariables.CHAT_CLICK);
//                    if (activity instanceof ChatWindowActivity) {
//                        ((ChatWindowActivity) activity).showHideCallAgainButton(false);
//                    }
//
//                } else if (status.equals(CGlobalVariables.UNABLE_TO_CONNECT_CHAT)) {
//                    CUtils.fcmAnalyticsEvents(CGlobalVariables.VIDEO_CALL_RES_STATUS_3, AstrosageKundliApplication.currentEventType, "");
//                    //stopActivityIndicator();
//                    String dialogMsg = "";
//                    dialogMsg = activity.getResources().getString(R.string.unable_to_connect_chat);
//                    dialogMsg = dialogMsg.replace("#", CGlobalVariables.UNABLE_TO_CONNECT_CHAT);
//                    callMsgDialogData(dialogMsg, "", true, CGlobalVariables.CHAT_CLICK);
//                } else {
//                    CUtils.fcmAnalyticsEvents("video_call_res_status_error", AstrosageKundliApplication.currentEventType, "");
//                    Toast.makeText(activity, "Authentication issue, please try again! ( Error code: " + status + " )", Toast.LENGTH_LONG).show();
//                }
//            } catch (Exception e) {
//                CUtils.fcmAnalyticsEvents(CGlobalVariables.VIDEO_CALL_RES_STATUS_EXCEPTION, AstrosageKundliApplication.currentEventType, "");
//                //stopActivityIndicator();
//                e.printStackTrace();
//                //Log.e("SAN CI DA ", " onresponse method == CONNECT_CHAT_API_RESPONSE exp " + e.toString() );
//            }
        }
    }

    /**
     * @param error
     */
    @Override
    public void onError(VolleyError error) {
        //Log.d("TestStatus", "error="+error);
        stopProgressBar();
    }

    public void getVisibleFragment(String response) {
        try {
            FragmentManager fm = ((FragmentActivity) activity).getSupportFragmentManager();
            List<Fragment> fragments = fm.getFragments();
            for (int i = fragments.size() - 1; i >= 0; i--) {
                Fragment fragment = fragments.get(i);
                if (fragment instanceof VartaHomeFragment) {
                    VartaHomeFragment vartaHomeFragment = (VartaHomeFragment)
                            ((FragmentActivity) activity).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                    if (vartaHomeFragment != null) {
                        vartaHomeFragment.responseNetworkCall(response);
                    }
                }
                if (fragment instanceof HomeFragment1) {
                    HomeFragment1 homeFragment1 = (HomeFragment1)
                            ((FragmentActivity) activity).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                    if (homeFragment1 != null) {
                        homeFragment1.responseNetworkCall(response);
                    }
                }
            }
        } catch (Exception e) {
            //
        }
    }


    private void firebaseSignIn(String authToken) {
        try {
            //Log.e("SAN CI DA ", " onResponse firebaseSignIn() inside "  );
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signInWithCustomToken(authToken)
                    .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            try {
                                if (isOpenProfile) {
                                    isOpenProfile = false;
                                    if (activity != null && AstrosageKundliApplication.selectedAstrologerDetailBean != null) {
                                        if (consultationType.equalsIgnoreCase(TYPE_CHAT)) {
                                            ChatUtils.getInstance(activity).startChat(null);
                                        } else if (consultationType.equalsIgnoreCase(TYPE_AI_CHAT)) {
                                            ChatUtils.getInstance(activity).startAIChat(null);
                                        } else {
                                            CUtils.openProfileOrKundliAct(activity, AstrosageKundliApplication.selectedAstrologerDetailBean.getUrlText(), consultationType, BACK_FROM_PROFILE_CHAT_DIALOG);
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                //
                            }
                        }
                    });
        } catch (Exception e) {
            //Log.e("SAN CI DA ", " onResponse firebaseSignIn() excep " + e.toString() );
        }
    }

    private void firebaseSignInOnly(String authToken) {
        if (TextUtils.isEmpty(authToken)) return;
        try {
            FirebaseAuth mAuth = FirebaseAuth.getInstance();
            mAuth.signInWithCustomToken(authToken)
                    .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //Log.e("TestChat", "onComplete="+task.isComplete());
                        }
                    });
        } catch (Exception e) {
            //Log.e("SAN CI DA ", " onResponse firebaseSignIn() excep " + e.toString() );
        }
    }

    long chatInitResponseTime = 0;

    /**
     * Initiates a chat session with a selected human astrologer.
     * <p>
     * This method orchestrates the entire process of starting a chat, including:
     * <ul>
     *     <li>Checking for internet connectivity and an active astrologer selection.</li>
     *     <li>Displaying a progress indicator to the user.</li>
     *     <li>Making a network request to the 'connectChat' API endpoint to establish the session.</li>
     *     <li>Handling various API responses, such as success, insufficient balance, astrologer busy/offline, or other errors.</li>
     *     <li>On success, it calls {@link #initChatAfterResponse(JSONObject)} to process the response and launch the chat window.</li>
     *     <li>On failure, it displays appropriate dialogs or toasts to inform the user.</li>
     * </ul>
     *
     * @param userProfileData The user's profile data, which will be sent to the astrologer.
     *                        If the user has chosen to share their details, this object contains all necessary information.
     */
    public void startChat(UserProfileData userProfileData) {
        // Clear any lingering chat-related event flags before starting a new session.
        CUtils.clearChatEvents();

        // Retrieve the globally selected astrologer details.
        AstrologerDetailBean astrologerDetailBean = AstrosageKundliApplication.selectedAstrologerDetailBean;
        // If no astrologer is selected, something has gone wrong in the user flow.
        if (astrologerDetailBean == null) {
            try {
                // Inform the user and abort the process.
                Toast.makeText(activity, activity.getResources().getString(R.string.something_wrong_error), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                // Silently fail if the activity context is invalid.
            }
            return;
        }

        if (!CUtils.isConnectedWithInternet(activity)) {
            // If there's no internet, show a toast and exit.
            Toast.makeText(activity, activity.getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        } else {
            // Show a progress bar to indicate that the chat connection is in progress.
            showProgressBar();
            // Create a Retrofit API service instance.
            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            // Prepare the API call to connect to the chat service.
            Call<ResponseBody> call = api.connectChat(getCallParams(userProfileData, "", astrologerDetailBean));
            // Asynchronously execute the API call.
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    // Hide the progress bar once the response is received.
                    stopProgressBar();
                    try {
                        String myResponse = response.body().string();
                        JSONObject jsonObject = new JSONObject(myResponse);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("msg");
                        boolean isChatStarted = false;
                        AstrosageKundliApplication.currentChatStatus = "";
                        // Check the status of the chat initiation from the API response.
                        if (status.equals(CGlobalVariables.CHAT_INITIATED)) {
                            // If the chat is successfully initiated, update the application status.
                            AstrosageKundliApplication.currentChatStatus = CGlobalVariables.CHAT_STARTED;
                            isChatStarted = true;
                            // Proceed to the next step after a successful response.
                            initChatAfterResponse(jsonObject);
                        } else if (status.equals(CGlobalVariables.FREE_CONSULTATION_NOT_AVAILABLE)) {
                            // Handle the case where free consultation is not available.
                            CUtils.fcmAnalyticsEvents("chat_api_res_free_consultation_not_available", AstrosageKundliApplication.currentEventType, "");
                            String dialogMsg = activity.getResources().getString(R.string.unable_to_connect_free_consultation);
                            openAstroBusyAlertDialog(activity, astrologerDetailBean, dialogMsg);
                        } else if (status.equals(CGlobalVariables.INSUFFICIENT_BALANCE)) {
                            // Handle the case of insufficient balance to start a chat.
                            CUtils.fcmAnalyticsEvents("chat_api_res_insufficient_bal", AstrosageKundliApplication.currentEventType, "");
                            String astrologerName = jsonObject.getString("astrologername");
                            String userbalance = jsonObject.getString("userbalance");
                            String minBalance = jsonObject.getString("minbalance");
                            FragmentManager ft = ((FragmentActivity) activity).getSupportFragmentManager();
//                            boolean isInsufficientDialogueHide = CUtils.getBooleanData(activity, CGlobalVariables.ISINSUFFICIENTDIALOGUEHIDE, false);
//                            if (!isInsufficientDialogueHide) {
//                                InSufficientBalanceDialog dialog = new InSufficientBalanceDialog(astrologerName, userbalance, minBalance, "chat_global");
//                                dialog.show(ft, "Dialog");
//                            } else {
//                                if (activity instanceof DashBoardActivity) {
//                                    ((DashBoardActivity) activity).openWalletScreen(CGlobalVariables.SCREEN_OPEN_FROM_INSUFFICIENT);
//                                }
//                            }
//                            AstrosageKundliApplication.chatAstrologerDetailBeanAfterRecharge = astrologerDetailBean;
                                if(CUtils.isSuggestedRechargeDialogShow(activity)){
                                com.ojassoft.astrosage.varta.utils.CUtils.openQuickRechargeSheet(activity,astrologerName,minBalance,userbalance,ft);
                            }else{
                                InSufficientBalanceDialog dialog = new InSufficientBalanceDialog(astrologerName, userbalance, minBalance, "chat_global");
                                dialog.show(ft, "Dialog");
                            }
                            AstrosageKundliApplication.chatAstrologerDetailBeanAfterRecharge = astrologerDetailBean;

                        } else if (status.equals(CGlobalVariables.ASTROLOGER_BUSY_OFFLINE)) {
                            // Handle cases where the astrologer is busy or offline.
                            if (message.equalsIgnoreCase(CGlobalVariables.USER_BLOCKED_MSG)) {
                                message = activity.getResources().getString(R.string.user_blocked_msg);
                                callMsgDialogData(message, "", true, CGlobalVariables.CHAT_CLICK);
                            } else if (message.equalsIgnoreCase(CGlobalVariables.USE_ANOTHER_CALL_MSG)) {
                                message = activity.getResources().getString(R.string.existing_chat_msg);
                                callMsgDialogData(message, "", true, CGlobalVariables.CHAT_CLICK);
                            } else {
                                String fcm_label = "chat_api_res_" + message;
                                CUtils.fcmAnalyticsEvents(fcm_label, AstrosageKundliApplication.currentEventType, "");
                                String callSource = "";
                                if (astrologerDetailBean != null) {
                                    callSource = astrologerDetailBean.getCallSource();
                                    if (callSource == null) callSource = "";
                                }
                                if (callSource.equalsIgnoreCase(CGlobalVariables.HUMAN_CONTINUE_CHAT_DIALOG)) {
                                    // Fallback to AI astrologer if the human astrologer is busy.
                                    if (AstrosageKundliApplication.lastChatAIAstrologerDetailBean != null) {
                                        AstrosageKundliApplication.lastChatAIAstrologerDetailBean.setCallSource(CGlobalVariables.HUMAN_CONTINUE_CHAT_FALLBACK_TO_SAME_AI);
                                        ChatUtils.getInstance(activity).initAIChat(AstrosageKundliApplication.lastChatAIAstrologerDetailBean);
                                    }
                                } else {
                                    String msg = activity.getString(R.string.astrologer_is_busy_in_another_chat_please_wait_for_some_time_and_re_initiate_the_chat);
                                    openAstroBusyAlertDialog(activity, astrologerDetailBean, msg);
                                }
                            }
                        } else if (status.equals(CGlobalVariables.UNABLE_TO_CONNECT_CHAT)) {
                            // Handle generic connection failure.
                            CUtils.fcmAnalyticsEvents("chat_api_response_status_3", AstrosageKundliApplication.currentEventType, "");
                            String dialogMsg = activity.getResources().getString(R.string.unable_to_connect_chat);
                            dialogMsg = dialogMsg.replace("#", CGlobalVariables.UNABLE_TO_CONNECT_CHAT);
                            openAstroBusyAlertDialog(activity, astrologerDetailBean, dialogMsg);
                        } else if (status.equals("100")) {
                            // Handle session expiration or invalid token, trigger a background login.
                            if (AstrosageKundliApplication.backgroundLoginCountForChat < 2) {
                                callBackgroundLoginApi(activity, TYPE_CHAT, userProfileData);
                                AstrosageKundliApplication.backgroundLoginCountForChat += 1;
                            }
                        } else {
                            // Handle any other error status from the API.
                            CUtils.fcmAnalyticsEvents("chat_api_response_status_error", AstrosageKundliApplication.currentEventType, "");
                            String dialogMsg = activity.getResources().getString(R.string.unable_to_connect_chat);
                            dialogMsg = dialogMsg.replace("#", CGlobalVariables.UNABLE_TO_CONNECT_CHAT);
                            openAstroBusyAlertDialog(activity, astrologerDetailBean, dialogMsg);
                        }

                        try {
                            // Re-enable the chat button on the astrologer's profile page.
                            if (activity instanceof AstrologerDescriptionActivity) {
                                ((AstrologerDescriptionActivity) activity).enableChatBtn();
                            }
                            // If the chat did not start, show the "Chat Again" button in the chat window.
                            if (!isChatStarted) {
                                if (activity instanceof ChatWindowActivity) {
                                    ((ChatWindowActivity) activity).showChatGainButton();
                                }
                            }
                        } catch (Exception e) {
                            //
                        }
                    } catch (Exception e) {
                        // Log any exceptions that occur during response handling.
                        CUtils.fcmAnalyticsEvents("chat_api_response_status_exception", AstrosageKundliApplication.currentEventType, "");
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    // Handle API call failure (e.g., network error).
                    stopProgressBar();
                    callMsgDialogData(activity.getResources().getString(R.string.something_wrong_error), "", true, CGlobalVariables.CHAT_CLICK);
                }
            });
        }
    }

    private void openAstroBusyAlertDialog(Activity activity, AstrologerDetailBean astrologerDetailBean, String msg) {
        AstroBusyAlertDialog astroBusyAlertDialog = AstroBusyAlertDialog.newInstance(astrologerDetailBean, msg);
        astroBusyAlertDialog.show(((FragmentActivity) activity).getSupportFragmentManager(), "astroBusyAlertDialog");
    }


    public void startAIChat(UserProfileData userProfileData) {
        CUtils.clearChatEvents();

        // Log.d("testChatNew","Connect Chat Api called");
        AstrologerDetailBean astrologerDetailBean = AstrosageKundliApplication.selectedAstrologerDetailBean;
        if (astrologerDetailBean == null) {
            try {
                Toast.makeText(activity, activity.getResources().getString(R.string.something_wrong_error), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                e.printStackTrace();
            }
            showChatAgainBtn(false);
            return;
        }

        if (!CUtils.isConnectedWithInternet(activity)) {
            Toast.makeText(activity, activity.getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            showChatAgainBtn(false);
        } else {
            showProgressBar();
            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.connectChatAI(getCallParams(userProfileData, "", astrologerDetailBean));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    stopProgressBar();
                    boolean isChatStarted = false;
                    try {
                        String myResponse = response.body().string();
                        //Log.e("TestChat"," myResponse AI Chat ==>>>>>>>>"+ myResponse);
                        JSONObject jsonObject = new JSONObject(myResponse);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("msg");
                        AstrosageKundliApplication.currentChatStatus = "";
                        if (status.equals(CGlobalVariables.CHAT_INITIATED)) {
                            AstrosageKundliApplication.currentChatStatus = CGlobalVariables.CHAT_STARTED;
                            isChatStarted = true;
                            initAIChatAfterResponse(jsonObject);
                        } else if (status.equals(CGlobalVariables.FREE_CONSULTATION_NOT_AVAILABLE)) {
                            CUtils.fcmAnalyticsEvents("chat_api_res_free_consultation_not_available", AstrosageKundliApplication.currentEventType, "");
                            String dialogMsg = activity.getResources().getString(R.string.unable_to_connect_free_consultation);
                            callMsgDialogData(dialogMsg, "", true, CGlobalVariables.CHAT_CLICK);
                        } else if (status.equals(CGlobalVariables.AI_PASS_QUESTION_LIMIT_EXCEEDED)) {
                            CUtils.fcmAnalyticsEvents("chat_api_response_status_limit_exceed", AstrosageKundliApplication.currentEventType, "");
                            String dialogMsg = "";
                            dialogMsg = activity.getResources().getString(R.string.error_ai_chat_limit_reached);
                            callMsgDialogData(dialogMsg, "", true, CGlobalVariables.CHAT_CLICK);
                        } else if (status.equals(CGlobalVariables.INSUFFICIENT_BALANCE)) {
                            CUtils.fcmAnalyticsEvents("chat_api_res_insufficient_bal", AstrosageKundliApplication.currentEventType, "");
                            String astrologerName = jsonObject.getString("astrologername");
                            String userbalance = jsonObject.getString("userbalance");
                            String minBalance = jsonObject.getString("minbalance");
                            String servicePrice = "", imageUrl = "";
                            if (AstrosageKundliApplication.selectedAstrologerDetailBean != null) {
                                servicePrice = AstrosageKundliApplication.selectedAstrologerDetailBean.getServicePrice();
                                imageUrl = AstrosageKundliApplication.selectedAstrologerDetailBean.getImageFile();
                            }
                            if (jsonObject.has("astrologer")) {
                                JSONObject astrologer = jsonObject.getJSONObject("astrologer");
                                astrologerName = astrologer.getString("name");
                                servicePrice = astrologer.getString("serviceprice");
                                imageUrl = astrologer.getString("imagefile");
                            }

                            // This flag is used to determine whether to show LowBalanceSubscribeAiAstroDialog or InSufficientBalanceDialog
                            // If showaipass is true, then LowBalanceSubscribeAiAstroDialog is shown else InSufficientBalanceDialog is shown
                            // LowBalanceSubscribeAiAstroDialog is used to show the option to subscribe to Ai Astrologer monthly plan
                            // InSufficientBalanceDialog is used to show the option to recharge the wallet to continue the chat
                            String isShowAiChatSub = jsonObject.getString("showaipass");
                            boolean showaipass = false;
                            FragmentManager ft = ((FragmentActivity) activity).getSupportFragmentManager();
//                            if (showaipass) {
//                                CUtils.fcmAnalyticsEvents(com.ojassoft.astrosage.utils.CGlobalVariables.UNLIMITED_AI_CHAT_PLAN_LOW_BALANCE, com.ojassoft.astrosage.utils.CGlobalVariables.SHOW_UNLIMITED_AI_CHAT_PLAN_VIEW, "");
//                                LowBalanceSubscribeAiAstroDialog lowBalanceSubscribeAiAstroDialog = new LowBalanceSubscribeAiAstroDialog(minBalance, astrologerName, servicePrice, imageUrl);
//                                lowBalanceSubscribeAiAstroDialog.show(ft, "LowBalanceSubscribeAiAstroDialog");
//                                AstrosageKundliApplication.chatAstrologerDetailBeanAfterRecharge = astrologerDetailBean;
//                            } else {

//                                boolean isInsufficientDialogueHide = CUtils.getBooleanData(activity, CGlobalVariables.ISINSUFFICIENTDIALOGUEHIDE, false);
//                                if (!isInsufficientDialogueHide) {
//                                    InSufficientBalanceDialog dialog = new InSufficientBalanceDialog(astrologerName, userbalance, minBalance, "chat_global");
//                                    dialog.show(ft, "Dialog");
//                                } else {
//                                    if (activity instanceof AIChatWindowActivity) {
//                                        ((AIChatWindowActivity) activity).openWalletScreen(CGlobalVariables.SCREEN_OPEN_FROM_INSUFFICIENT);
//                                    }
//                                }

                            if(CUtils.isSuggestedRechargeDialogShow(activity)){
                                com.ojassoft.astrosage.varta.utils.CUtils.openQuickRechargeSheet(activity,astrologerName,minBalance,userbalance,ft);
                            }else{
                                InSufficientBalanceDialog dialog = new InSufficientBalanceDialog(astrologerName, userbalance, minBalance, "chat_global");
                                dialog.show(ft, "Dialog");
                            }
                                AstrosageKundliApplication.chatAstrologerDetailBeanAfterRecharge = astrologerDetailBean;
                            //}
                        } else if (status.equals(CGlobalVariables.ASTROLOGER_BUSY_OFFLINE)) {
                            String fcm_label = "chat_api_res_" + message;
                            CUtils.fcmAnalyticsEvents(fcm_label, AstrosageKundliApplication.currentEventType, "");
                            String dialogMsg = "";
                            if (message.equalsIgnoreCase(CGlobalVariables.USER_BLOCKED_MSG)) {
                                dialogMsg = activity.getResources().getString(R.string.user_blocked_msg);
                            } else if (message.equalsIgnoreCase(CGlobalVariables.ASTROLOGER_STATUS_BUSY_MSG)) {
                                dialogMsg = activity.getResources().getString(R.string.astrologer_busy_msg_chat);
                            } else if (message.equalsIgnoreCase(CGlobalVariables.ASTROLOGER_STATUS_OFFLINE_MSG)) {
                                String stringMsg = activity.getResources().getString(R.string.astrologer_offline_msg);
                                dialogMsg = stringMsg;
                            } else if (message.equalsIgnoreCase(CGlobalVariables.ASTROLOGER_STATUS_DISABLED)) {
                                dialogMsg = activity.getResources().getString(R.string.astrologer_status_disable);
                            } else if (message.equalsIgnoreCase(CGlobalVariables.CALL_API_FAILED)) {
                                dialogMsg = activity.getResources().getString(R.string.call_api_failed_msg);
                            } else if (message.equalsIgnoreCase(CGlobalVariables.USE_ANOTHER_CALL_MSG)) {
                                dialogMsg = activity.getResources().getString(R.string.existing_chat_msg);
                            } else {
                                dialogMsg = activity.getResources().getString(R.string.something_wrong_error);
                            }

                            callMsgDialogData(dialogMsg, "", true, CGlobalVariables.CHAT_CLICK);
                            getVisibleFragmentAndUpdateStatus();
                        } else if (status.equals(CGlobalVariables.UNABLE_TO_CONNECT_CHAT)) {
                            CUtils.fcmAnalyticsEvents("chat_api_response_status_3", AstrosageKundliApplication.currentEventType, "");
                            String dialogMsg = "";
                            dialogMsg = activity.getResources().getString(R.string.unable_to_connect_chat);
                            dialogMsg = dialogMsg.replace("#", CGlobalVariables.UNABLE_TO_CONNECT_CHAT);
                            callMsgDialogData(dialogMsg, "", true, CGlobalVariables.CHAT_CLICK);
                        } else if (status.equals("100")) {
                            if (AstrosageKundliApplication.backgroundLoginCountForChat < 2) {
                                callBackgroundLoginApi(activity, TYPE_AI_CHAT, userProfileData);
                                AstrosageKundliApplication.backgroundLoginCountForChat += 1;
                            }
                        } else {
                            CUtils.fcmAnalyticsEvents("chat_api_response_status_error", AstrosageKundliApplication.currentEventType, "");
                            Toast.makeText(activity, "Authentication issue, please try again! ( Error code: " + status + " )", Toast.LENGTH_LONG).show();
                        }

                        try {
                            if (activity instanceof AstrologerDescriptionActivity) {
                                ((AstrologerDescriptionActivity) activity).enableChatBtn();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        CUtils.fcmAnalyticsEvents("chat_api_response_status_exception", AstrosageKundliApplication.currentEventType, "");
                    }

                    showChatAgainBtn(isChatStarted);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("TestChat", " onFailure ==>>>>>>>>" + call);
                    showChatAgainBtn(false);
                    stopProgressBar();
                    callMsgDialogData(activity.getResources().getString(R.string.something_wrong_error), "", true, CGlobalVariables.CHAT_CLICK);
                }
            });
        }
    }


    public void startAIChatRandom(UserProfileData userProfileData, String apiCallingSource) {
        CUtils.clearChatEvents();
        if (!CUtils.isConnectedWithInternet(activity)) {
            Toast.makeText(activity, activity.getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        } else {
            showProgressBar();
            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.connectChatAI(getAIChatParamsRandom(userProfileData, apiCallingSource));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    stopProgressBar();
                    boolean isChatStarted = false;
                    try {
                        String myResponse = response.body().string();
                        //Log.d("testChatApiResReq","myResponse ===>>>"+myResponse);
                        JSONObject jsonObject = new JSONObject(myResponse);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("msg");
                        AstrosageKundliApplication.currentChatStatus = "";
                        if (status.equals(CGlobalVariables.CHAT_INITIATED)) {
                            AstrosageKundliApplication.currentChatStatus = CGlobalVariables.CHAT_STARTED;
                            isChatStarted = true;
                            initAIChatAfterResponseRandom(jsonObject);
                        } else if (status.equals(CGlobalVariables.FREE_CONSULTATION_NOT_AVAILABLE)) {
                            CUtils.fcmAnalyticsEvents("chat_api_res_free_consultation_not_available", AstrosageKundliApplication.currentEventType, "");
                            String dialogMsg = activity.getResources().getString(R.string.unable_to_connect_free_consultation);
                            callMsgDialogData(dialogMsg, "", true, CGlobalVariables.CHAT_CLICK);
                        } else if (status.equals(CGlobalVariables.INSUFFICIENT_BALANCE)) {
                            CUtils.fcmAnalyticsEvents("chat_api_res_insufficient_bal", AstrosageKundliApplication.currentEventType, "");
                            String astrologerName = jsonObject.getString("astrologername");
                            String userbalance = jsonObject.getString("userbalance");
                            String minBalance = jsonObject.getString("minbalance");

                            InSufficientBalanceDialog dialog = new InSufficientBalanceDialog(astrologerName, userbalance, minBalance, "chat_global");
                            FragmentManager ft = ((FragmentActivity) activity).getSupportFragmentManager();
                            dialog.show(ft, "Dialog");
                        } else if (status.equals(CGlobalVariables.ASTROLOGER_BUSY_OFFLINE)) {

                            String fcm_label = "chat_api_res_" + message;
                            CUtils.fcmAnalyticsEvents(fcm_label, AstrosageKundliApplication.currentEventType, "");
                            String dialogMsg = "";
                            if (message.equalsIgnoreCase(CGlobalVariables.USER_BLOCKED_MSG)) {
                                dialogMsg = activity.getResources().getString(R.string.user_blocked_msg);
                            } else if (message.equalsIgnoreCase(CGlobalVariables.ASTROLOGER_STATUS_BUSY_MSG)) {
                                dialogMsg = activity.getResources().getString(R.string.astrologer_busy_msg_chat);
                            } else if (message.equalsIgnoreCase(CGlobalVariables.ASTROLOGER_STATUS_OFFLINE_MSG)) {
                                String stringMsg = activity.getResources().getString(R.string.astrologer_offline_msg);
                                dialogMsg = stringMsg;
                            } else if (message.equalsIgnoreCase(CGlobalVariables.ASTROLOGER_STATUS_DISABLED)) {
                                dialogMsg = activity.getResources().getString(R.string.astrologer_status_disable);
                            } else if (message.equalsIgnoreCase(CGlobalVariables.CALL_API_FAILED)) {
                                dialogMsg = activity.getResources().getString(R.string.call_api_failed_msg);
                            } else if (message.equalsIgnoreCase(CGlobalVariables.USE_ANOTHER_CALL_MSG)) {
                                dialogMsg = activity.getResources().getString(R.string.existing_chat_msg);
                            } else {
                                dialogMsg = activity.getResources().getString(R.string.something_wrong_error);
                            }

                            callMsgDialogData(dialogMsg, "", true, CGlobalVariables.CHAT_CLICK);
                            getVisibleFragmentAndUpdateStatus();
                        } else if (status.equals(CGlobalVariables.UNABLE_TO_CONNECT_CHAT)) {
                            CUtils.fcmAnalyticsEvents("chat_api_response_status_3", AstrosageKundliApplication.currentEventType, "");
                            String dialogMsg = "";
                            dialogMsg = activity.getResources().getString(R.string.unable_to_connect_chat);
                            dialogMsg = dialogMsg.replace("#", CGlobalVariables.UNABLE_TO_CONNECT_CHAT);
                            callMsgDialogData(dialogMsg, "", true, CGlobalVariables.CHAT_CLICK);
                        } else if (status.equals("100")) {
                            if (AstrosageKundliApplication.backgroundLoginCountForChat < 2) {
                                AstrosageKundliApplication.apiCallingSource = apiCallingSource;
                                callBackgroundLoginApi(activity, TYPE_AI_CHAT_RANDOM, userProfileData);
                                AstrosageKundliApplication.backgroundLoginCountForChat += 1;
                            }
                        } else {
                            CUtils.fcmAnalyticsEvents("chat_api_response_status_error", AstrosageKundliApplication.currentEventType, "");
                            Toast.makeText(activity, "Authentication issue, please try again! ( Error code: " + status + " )", Toast.LENGTH_LONG).show();
                        }

                        try {
                            if (activity instanceof AstrologerDescriptionActivity) {
                                ((AstrologerDescriptionActivity) activity).enableChatBtn();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        CUtils.fcmAnalyticsEvents("chat_api_response_status_exception", AstrosageKundliApplication.currentEventType, "");
                    }

                    showChatAgainBtn(isChatStarted);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("TestChat", " onFailure ==>>>>>>>>" + call);
                    CUtils.isAutoConsultationConnected = false;
                    showChatAgainBtn(false);
                    stopProgressBar();
                    callMsgDialogData(activity.getResources().getString(R.string.something_wrong_error), "", true, CGlobalVariables.CHAT_CLICK);
                }
            });
        }
    }

    public void startChatRandom(UserProfileData userProfileData, String apiCallingSource) {
        CUtils.clearChatEvents();
        if (!CUtils.isConnectedWithInternet(activity)) {
            Toast.makeText(activity, activity.getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        } else {
            showProgressBar();
            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            // Log.d("testChatApiResReq","apiCallingSource==>>"+apiCallingSource+"      "+getChatParamsRandom(userProfileData,apiCallingSource));
            Call<ResponseBody> call = api.connectChat(getChatParamsRandom(userProfileData, apiCallingSource));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    stopProgressBar();
                    boolean isChatStarted = false;
                    try {
                        String myResponse = response.body().string();
                        JSONObject jsonObject = new JSONObject(myResponse);
                        //Log.d("testChatApiResReq","myResponse ===>>>"+myResponse);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("msg");
                        AstrosageKundliApplication.currentChatStatus = "";
                        if (status.equals(CGlobalVariables.CHAT_INITIATED)) {
                            AstrosageKundliApplication.currentChatStatus = CGlobalVariables.CHAT_STARTED;
                            isChatStarted = true;
                            // Log.d("testChatApiResReq","initChatAfterResponseRandom called ===>>>");
                            String callsId = jsonObject.getString("callsid");
                            if (callsId.startsWith(CGlobalVariables.FCHAI)) {
                                initAIChatAfterResponseRandom(jsonObject);
                            } else {
                                initChatAfterResponseRandom(jsonObject);
                            }

                        } else if (status.equals(CGlobalVariables.FREE_CONSULTATION_NOT_AVAILABLE)) {
                            CUtils.fcmAnalyticsEvents("chat_api_res_free_consultation_not_available", AstrosageKundliApplication.currentEventType, "");
                            //String dialogMsg = activity.getResources().getString(R.string.unable_to_connect_free_consultation);
                            //callMsgDialogData(dialogMsg, "", true, CGlobalVariables.CHAT_CLICK);

                            startAIChatRandom(userProfileData, CGlobalVariables.AI_FREE_CHAT_AFTER_BUSY);
                        } else if (status.equals(CGlobalVariables.INSUFFICIENT_BALANCE)) {
                            CUtils.fcmAnalyticsEvents("chat_api_res_insufficient_bal", AstrosageKundliApplication.currentEventType, "");
                            String astrologerName = jsonObject.getString("astrologername");
                            String userbalance = jsonObject.getString("userbalance");
                            String minBalance = jsonObject.getString("minbalance");

                            InSufficientBalanceDialog dialog = new InSufficientBalanceDialog(astrologerName, userbalance, minBalance, "chat_global");
                            FragmentManager ft = ((FragmentActivity) activity).getSupportFragmentManager();
                            dialog.show(ft, "Dialog");
                            // AstrosageKundliApplication.chatAstrologerDetailBeanAfterRecharge = astrologerDetailBean;
                        } else if (status.equals(CGlobalVariables.ASTROLOGER_BUSY_OFFLINE)) {

                            String fcm_label = "chat_api_res_" + message;
                            CUtils.fcmAnalyticsEvents(fcm_label, AstrosageKundliApplication.currentEventType, "");
                            String dialogMsg = "";
                            if (message.equalsIgnoreCase(CGlobalVariables.USER_BLOCKED_MSG)) {
                                dialogMsg = activity.getResources().getString(R.string.user_blocked_msg);
                            } else if (message.equalsIgnoreCase(CGlobalVariables.ASTROLOGER_STATUS_BUSY_MSG)) {
                                //dialogMsg = activity.getResources().getString(R.string.astrologer_busy_msg_chat);
                            } else if (message.equalsIgnoreCase(CGlobalVariables.ASTROLOGER_STATUS_OFFLINE_MSG)) {
                                //String stringMsg = activity.getResources().getString(R.string.astrologer_offline_msg);
                                //dialogMsg = stringMsg;
                            } else if (message.equalsIgnoreCase(CGlobalVariables.ASTROLOGER_STATUS_DISABLED)) {
                                //dialogMsg = activity.getResources().getString(R.string.astrologer_status_disable);
                            } else if (message.equalsIgnoreCase(CGlobalVariables.CALL_API_FAILED)) {
                                //dialogMsg = activity.getResources().getString(R.string.call_api_failed_msg);
                            } else if (message.equalsIgnoreCase(CGlobalVariables.USE_ANOTHER_CALL_MSG)) {
                                dialogMsg = activity.getResources().getString(R.string.existing_chat_msg);
                            } else {
                                //dialogMsg = activity.getResources().getString(R.string.something_wrong_error);
                            }
                            if (!TextUtils.isEmpty(dialogMsg)) {
                                callMsgDialogData(dialogMsg, "", true, CGlobalVariables.CHAT_CLICK);
                                getVisibleFragmentAndUpdateStatus();
                            } else {
                                startAIChatRandom(userProfileData, CGlobalVariables.AI_FREE_CHAT_AFTER_BUSY);
                            }
                        } else if (status.equals(CGlobalVariables.UNABLE_TO_CONNECT_CHAT)) {
                            CUtils.fcmAnalyticsEvents("chat_api_response_status_3", AstrosageKundliApplication.currentEventType, "");
                            String dialogMsg = "";
                            dialogMsg = activity.getResources().getString(R.string.unable_to_connect_chat);
                            dialogMsg = dialogMsg.replace("#", CGlobalVariables.UNABLE_TO_CONNECT_CHAT);
                            callMsgDialogData(dialogMsg, "", true, CGlobalVariables.CHAT_CLICK);
                        } else if (status.equals("100")) {
                            if (AstrosageKundliApplication.backgroundLoginCountForChat < 2) {
                                AstrosageKundliApplication.apiCallingSource = apiCallingSource;
                                callBackgroundLoginApi(activity, TYPE_CHAT_RANDOM, userProfileData);
                                AstrosageKundliApplication.backgroundLoginCountForChat += 1;
                            }
                        } else {
                            CUtils.fcmAnalyticsEvents("chat_api_response_status_error", AstrosageKundliApplication.currentEventType, "");
                            Toast.makeText(activity, "Authentication issue, please try again! ( Error code: " + status + " )", Toast.LENGTH_LONG).show();
                        }

                        try {
                            if (activity instanceof AstrologerDescriptionActivity) {
                                ((AstrologerDescriptionActivity) activity).enableChatBtn();
                            }
                            if (!isChatStarted) {
                                if (activity instanceof ChatWindowActivity) {
                                    ((ChatWindowActivity) activity).showChatGainButton();
                                }
                            }
                        } catch (Exception e) {
                            //
                        }
                    } catch (Exception e) {
                        CUtils.fcmAnalyticsEvents("chat_api_response_status_exception", AstrosageKundliApplication.currentEventType, "");
                    }

                    showChatAgainBtn(isChatStarted);
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    CUtils.isAutoConsultationConnected = false;
                    Log.e("TestChat", " onFailure ==>>>>>>>>" + call);
                    showChatAgainBtn(false);
                    stopProgressBar();
                    callMsgDialogData(activity.getResources().getString(R.string.something_wrong_error), "", true, CGlobalVariables.CHAT_CLICK);
                }
            });
        }
    }

    private void showChatAgainBtn(Boolean isChatStarted) {
        if (!isChatStarted) {
            try {
                if (activity instanceof ChatWindowActivity) {
                    ((ChatWindowActivity) activity).showChatGainButton();
                } else if (activity instanceof AIChatWindowActivity) {
                    ((AIChatWindowActivity) activity).changeVisibilitySendMsgLayout(false);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public Map<String, String> getCallParams(UserProfileData userProfileDataBean, String connectType, AstrologerDetailBean astrologerDetailBean) {
        //set checkWhichConsultationScreen to empty string to avoid any issue
        AstrosageKundliApplication.checkWhichConsultationScreen = "";
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(activity));
        headers.put(CGlobalVariables.USER_PHONE_NO, CUtils.getUserID(activity));
        headers.put(CGlobalVariables.URL_TEXT, astrologerDetailBean.getUrlText());
        headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(activity));
        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(activity));
        headers.put(CGlobalVariables.APP_VERSION, BuildConfig.VERSION_NAME);
        headers.put(CGlobalVariables.KEY_USER_ID, CUtils.getUserIdForBlock(activity));
        String _pwd = CUtils.getUserLoginPassword(activity);
        headers.put(KEY_PASSWORD, _pwd);
        if (userProfileDataBean != null && userProfileDataBean.isProfileSendToAstrologer()) {
            headers.put("isprofilepresent", "1");
            headers.put("name", userProfileDataBean.getName());
            headers.put("gender", userProfileDataBean.getGender());
            headers.put("place", userProfileDataBean.getPlace());
            headers.put("day", userProfileDataBean.getDay());
            headers.put("month", userProfileDataBean.getMonth());
            headers.put("year", userProfileDataBean.getYear());
            headers.put("hour", userProfileDataBean.getHour());
            headers.put("minute", userProfileDataBean.getMinute());
            headers.put("second", userProfileDataBean.getSecond());
            headers.put("longdeg", userProfileDataBean.getLongdeg());
            headers.put("longmin", userProfileDataBean.getLongmin());
            headers.put("longew", userProfileDataBean.getLongew());
            headers.put("latmin", userProfileDataBean.getLatmin());
            headers.put("latdeg", userProfileDataBean.getLatdeg());
            headers.put("latns", userProfileDataBean.getLatns());
            headers.put("timezone", userProfileDataBean.getTimezone());
            headers.put("maritalStatus", userProfileDataBean.getMaritalStatus());
            headers.put("occupation", userProfileDataBean.getOccupation());
        } else {
            headers.put("isprofilepresent", "0");
        }
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
        headers.put(CGlobalVariables.CALL_SOURCE, astrologerDetailBean.getCallSource());
        String currentOfferType = CUtils.getUserIntroOfferType(activity);
        CUtils.isFreeChat = astrologerDetailBean.isFreeForChat() && currentOfferType.equals(CGlobalVariables.INTRO_OFFER_TYPE_FREE);
        if (connectType.isEmpty()) {
            headers.put(CGlobalVariables.ISFREE_CONSULTATION, "" + astrologerDetailBean.isFreeForChat());
        } else {
            headers.put(CGlobalVariables.ISFREE_CONSULTATION, "" + astrologerDetailBean.isFreeForCall());
        }
        headers.put(CGlobalVariables.OFFER_TYPE, currentOfferType);
        boolean offerFromNotif = astrologerDetailBean.isAstroSingleTimeOffer();
        headers.put(CGlobalVariables.OFFER_FROM_NOTIFICATION, offerFromNotif ? "1" : "0");
        if (offerFromNotif) {
            headers.put(CGlobalVariables.CALL_SOURCE, CGlobalVariables.OFFER_FREE_CHAT_FROM_NOTIFICATION);
        } else {
            headers.put(CGlobalVariables.CALL_SOURCE, astrologerDetailBean.getCallSource());
        }

        headers.put(DEVICE_ID, CUtils.getMyAndroidId(activity));

        headers.put("type", connectType);
        String permission = "0";
        if (!TextUtils.isEmpty(CUtils.getHavePermissionRecordAudio())) {
            permission = CUtils.getHavePermissionRecordAudio();
        }
        headers.put("userpermission", permission);
        headers.put(USER_SPEED, String.valueOf(CUtils.getNetworkSpeed(activity)));
        int networkType = CUtils.getNetworkType(activity);
        headers.put("usernetworktype", String.valueOf(networkType));
        //headers.put("israndomconsultation","1");
        //send sfc
        headers.put(CGlobalVariables.PREF_SECOND_FREE_CHAT, "" + CUtils.isSecondFreeChat(activity));
        // Log.e("TestChat"," headers ==>>>>>>>>"+ headers);
        errorLogs = errorLogs + "api params->" + headers + "\n";
        //Log.d("testChatApiResReq","headers ===>>>"+headers);
        return headers;
    }

    public Map<String, String> getAIChatParamsRandom(UserProfileData userProfileDataBean, String apiCallingSource) {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(activity));
        headers.put(CGlobalVariables.USER_PHONE_NO, CUtils.getUserID(activity));
        headers.put(CGlobalVariables.URL_TEXT, "");
        headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(activity));
        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(activity));
        headers.put(CGlobalVariables.APP_VERSION, BuildConfig.VERSION_NAME);
        headers.put(CGlobalVariables.KEY_USER_ID, CUtils.getUserIdForBlock(activity));
        String _pwd = CUtils.getUserLoginPassword(activity);
        headers.put(KEY_PASSWORD, _pwd);

        if (userProfileDataBean != null && userProfileDataBean.isProfileSendToAstrologer()) {
            headers.put("isprofilepresent", "1");
            headers.put("name", userProfileDataBean.getName());
            headers.put("gender", userProfileDataBean.getGender());
            headers.put("place", userProfileDataBean.getPlace());
            headers.put("day", userProfileDataBean.getDay());
            headers.put("month", userProfileDataBean.getMonth());
            headers.put("year", userProfileDataBean.getYear());
            headers.put("hour", userProfileDataBean.getHour());
            headers.put("minute", userProfileDataBean.getMinute());
            headers.put("second", userProfileDataBean.getSecond());
            headers.put("longdeg", userProfileDataBean.getLongdeg());
            headers.put("longmin", userProfileDataBean.getLongmin());
            headers.put("longew", userProfileDataBean.getLongew());
            headers.put("latmin", userProfileDataBean.getLatmin());
            headers.put("latdeg", userProfileDataBean.getLatdeg());
            headers.put("latns", userProfileDataBean.getLatns());
            headers.put("timezone", userProfileDataBean.getTimezone());
            headers.put("maritalStatus", userProfileDataBean.getMaritalStatus());
            headers.put("occupation", userProfileDataBean.getOccupation());
        } else {
            headers.put("isprofilepresent", "0");
        }
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
        headers.put(CGlobalVariables.CALL_SOURCE, apiCallingSource);
        String currentOfferType = CUtils.getUserIntroOfferType(activity);
        CUtils.isFreeChat = true;
        headers.put(CGlobalVariables.ISFREE_CONSULTATION, "" + CUtils.isFreeChat);
        headers.put(CGlobalVariables.OFFER_TYPE, currentOfferType);
        headers.put("israndomconsultation", "1");
        headers.put(DEVICE_ID, CUtils.getMyAndroidId(activity));

        headers.put("type", "");
        String permission = "0";
        headers.put("userpermission", permission);
        headers.put(USER_SPEED, String.valueOf(CUtils.getNetworkSpeed(activity)));
        int networkType = CUtils.getNetworkType(activity);
        headers.put("usernetworktype", String.valueOf(networkType));
        //send sfc
        headers.put(CGlobalVariables.PREF_SECOND_FREE_CHAT, "" + CUtils.isSecondFreeChat(activity));


        // Log.e("TestChat"," headers ==>>>>>>>>"+ headers);
        //Log.d("testcallAstrodeatils123","headers ===>>>"+headers);
        return headers;
    }

    public Map<String, String> getChatParamsRandom(UserProfileData userProfileDataBean, String apiCallingSource) {
        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(activity));
        headers.put(CGlobalVariables.USER_PHONE_NO, CUtils.getUserID(activity));
        //headers.put(CGlobalVariables.URL_TEXT, "test-profile-manish");
        headers.put(CGlobalVariables.URL_TEXT, "");
        headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(activity));
        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(activity));
        headers.put(CGlobalVariables.APP_VERSION, BuildConfig.VERSION_NAME);
        headers.put(CGlobalVariables.KEY_USER_ID, CUtils.getUserIdForBlock(activity));
        String _pwd = CUtils.getUserLoginPassword(activity);
        headers.put(KEY_PASSWORD, _pwd);

        if (userProfileDataBean != null && userProfileDataBean.isProfileSendToAstrologer()) {
            headers.put("isprofilepresent", "1");
            headers.put("name", userProfileDataBean.getName());
            headers.put("gender", userProfileDataBean.getGender());
            headers.put("place", userProfileDataBean.getPlace());
            headers.put("day", userProfileDataBean.getDay());
            headers.put("month", userProfileDataBean.getMonth());
            headers.put("year", userProfileDataBean.getYear());
            headers.put("hour", userProfileDataBean.getHour());
            headers.put("minute", userProfileDataBean.getMinute());
            headers.put("second", userProfileDataBean.getSecond());
            headers.put("longdeg", userProfileDataBean.getLongdeg());
            headers.put("longmin", userProfileDataBean.getLongmin());
            headers.put("longew", userProfileDataBean.getLongew());
            headers.put("latmin", userProfileDataBean.getLatmin());
            headers.put("latdeg", userProfileDataBean.getLatdeg());
            headers.put("latns", userProfileDataBean.getLatns());
            headers.put("timezone", userProfileDataBean.getTimezone());
            headers.put("maritalStatus", userProfileDataBean.getMaritalStatus());
            headers.put("occupation", userProfileDataBean.getOccupation());
        } else {
            headers.put("isprofilepresent", "0");
        }
        headers.put(DEVICE_ID, CUtils.getMyAndroidId(activity));
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
        headers.put(CGlobalVariables.CALL_SOURCE, apiCallingSource);
        String currentOfferType = CUtils.getUserIntroOfferType(activity);
        CUtils.isFreeChat = true;
        headers.put(CGlobalVariables.ISFREE_CONSULTATION, "" + CUtils.isFreeChat);
        headers.put(CGlobalVariables.OFFER_TYPE, currentOfferType);
        headers.put("israndomconsultation", "1");

        headers.put("type", "");
        String permission = "0";
        headers.put("userpermission", permission);
        headers.put(USER_SPEED, String.valueOf(CUtils.getNetworkSpeed(activity)));
        int networkType = CUtils.getNetworkType(activity);
        headers.put("usernetworktype", String.valueOf(networkType));
        //Log.d("testChatApiResReq","headers ===>>>"+headers);
        return headers;
    }

    private void showProgressBar() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                try {
                    progressDialog = new CustomProgressDialog(activity);
                    progressDialog.setMessage(activity.getResources().getString(R.string.msg_please_wait));
                    progressDialog.show();
                    progressDialog.setCanceledOnTouchOutside(false);
                    progressDialog.setCancelable(false);
                } catch (Exception e) {
                    //
                }
            }
        });
    }

    private void stopProgressBar() {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                hideProgressBarCustomProgressDialog();
                if (progressDialog != null && progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
            }
        });
    }

    private void showProgressBarCustomProgressDialog() {
        try {
            if (pd == null)
                pd = new CustomProgressDialog(activity);
            pd.show();
            pd.setCancelable(false);
        } catch (Exception e) {
            //
        }
    }

    private void hideProgressBarCustomProgressDialog() {
        try {
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }
        } catch (Exception e) {
            //
        }
    }

    public void callMsgDialogData(String message, String title, boolean isShowOkBtn, String fromWhich) {
        try {

            CallMsgDialog dialog = new CallMsgDialog(message, title, isShowOkBtn, fromWhich);
            FragmentManager ft = ((FragmentActivity) activity).getSupportFragmentManager();

            dialog.show(ft, "Dialog");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initAIChatAfterResponse(JSONObject jsonObject) {
        try {
            AstrosageKundliApplication.IS_AGORA_CALL_RUNNING = false;
            Gson gson = new Gson();
            ConnectChatBean connectChatBean = new ConnectChatBean();
            connectChatBean = gson.fromJson(jsonObject.toString(), ConnectChatBean.class);
            firebaseSignInOnly(connectChatBean.getToken());
            if (connectChatBean.getTalktime() != null && connectChatBean.getTalktime().length() > 0) {
                CGlobalVariables.userChatTime = connectChatBean.getTalktime();
            }
            final String callsId = jsonObject.getString("callsid");

            //CHANNEL_ID = callsId;
            CUtils.saveAstrologerIDAndChannelID(activity, AstrosageKundliApplication.selectedAstrologerDetailBean.getAstrologerId(), callsId);


            String astroName = "", profileUrl = "", aiAstrologerId = "", profileLargeImage = "";
            if (AstrosageKundliApplication.selectedAstrologerDetailBean != null) {
                astroName = AstrosageKundliApplication.selectedAstrologerDetailBean.getName();
                profileUrl = AstrosageKundliApplication.selectedAstrologerDetailBean.getImageFile();
                aiAstrologerId = AstrosageKundliApplication.selectedAstrologerDetailBean.getAiAstrologerId();
                profileLargeImage = AstrosageKundliApplication.selectedAstrologerDetailBean.getImageFileLarge();
            }
            startAIChatWindow(callsId, jsonObject, CGlobalVariables.userChatTime, astroName, profileUrl, aiAstrologerId, profileLargeImage);
            if (activity instanceof AINotificationChatActivity) {
                activity.finish();
            }
            AstrosageKundliApplication.chatJsonObject = jsonObject.toString();


            AstrosageKundliApplication.currentChatStatus = CGlobalVariables.CHAT_ACCEPTED;
        } catch (Exception e) {
            //
        }

    }

    private void initAIChatAfterResponseRandom(JSONObject jsonObject) {
        try {
            JSONObject astrologerObject = jsonObject.getJSONObject("astrologer");

            AstrologerDetailBean astrologerDetailBean = new AstrologerDetailBean();
            astrologerDetailBean.setName(astrologerObject.getString("name"));
            astrologerDetailBean.setImageFile(astrologerObject.getString("imagefile"));
            astrologerDetailBean.setAstroWalletId(astrologerObject.getString("astrologerid"));
            astrologerDetailBean.setUrlText(astrologerObject.getString("urltext"));
            astrologerDetailBean.setAstrologerId(astrologerObject.getString("astrologerid"));

            astrologerDetailBean.setDesignation(astrologerObject.optString("expertise"));
            astrologerDetailBean.setAiAstrologerId(astrologerObject.optString("aiai"));
            astrologerDetailBean.setImageFileLarge(astrologerObject.optString("imagefilelarge"));

            astrologerDetailBean.setCallSource("AIFreePopUp");
            astrologerDetailBean.setFreeForChat(true);

            AstrosageKundliApplication.selectedAstrologerDetailBean = astrologerDetailBean;
            AstrosageKundliApplication.chatAgainAstrologerDetailBean = astrologerDetailBean;

            AstrosageKundliApplication.IS_AGORA_CALL_RUNNING = false;
            Gson gson = new Gson();
            ConnectChatBean connectChatBean = gson.fromJson(jsonObject.toString(), ConnectChatBean.class);
            firebaseSignInOnly(connectChatBean.getToken());
            if (connectChatBean.getTalktime() != null && connectChatBean.getTalktime().length() > 0) {
                CGlobalVariables.userChatTime = connectChatBean.getTalktime();
            }
            final String callsId = jsonObject.getString("callsid");

            //CHANNEL_ID = callsId;
            CUtils.saveAstrologerIDAndChannelID(activity, AstrosageKundliApplication.selectedAstrologerDetailBean.getAstrologerId(), callsId);


            String astroName = "", profileUrl = "", aiAstrologerId = "", profileLargeImage = "";
            if (AstrosageKundliApplication.selectedAstrologerDetailBean != null) {
                astroName = AstrosageKundliApplication.selectedAstrologerDetailBean.getName();
                profileUrl = AstrosageKundliApplication.selectedAstrologerDetailBean.getImageFile();
                aiAstrologerId = AstrosageKundliApplication.selectedAstrologerDetailBean.getAiAstrologerId();
                profileLargeImage = AstrosageKundliApplication.selectedAstrologerDetailBean.getImageFileLarge();
            }
            startAIChatWindow(callsId, jsonObject, CGlobalVariables.userChatTime, astroName, profileUrl, aiAstrologerId, profileLargeImage);

            AstrosageKundliApplication.chatJsonObject = jsonObject.toString();


            AstrosageKundliApplication.currentChatStatus = CGlobalVariables.CHAT_ACCEPTED;
        } catch (Exception e) {
            Log.d("TestChat", "e=========" + e);
        }

    }

    private void initChatAfterResponseRandom(JSONObject jsonObject) {
        try {
            JSONObject astrologerObject = jsonObject.getJSONObject("astrologer");

            AstrologerDetailBean astrologerDetailBean = new AstrologerDetailBean();
            astrologerDetailBean.setName(astrologerObject.getString("name"));
            astrologerDetailBean.setImageFile(astrologerObject.getString("imagefile"));
            astrologerDetailBean.setAstroWalletId(astrologerObject.getString("astrologerid"));
            astrologerDetailBean.setUrlText(astrologerObject.getString("urltext"));
            astrologerDetailBean.setAstrologerId(astrologerObject.getString("astrologerid"));

            astrologerDetailBean.setDesignation(astrologerObject.optString("expertise"));
            astrologerDetailBean.setAiAstrologerId(astrologerObject.optString("aiai"));
            astrologerDetailBean.setImageFileLarge(astrologerObject.optString("imagefilelarge"));
            astrologerDetailBean.setActualServicePriceInt(astrologerObject.optString("actualservicepriceInt"));
            astrologerDetailBean.setServicePrice(astrologerObject.optString("serviceprice"));
            astrologerDetailBean.setCallSource("ChatFreePopUp");
            astrologerDetailBean.setFreeForChat(true);
            //used in case of free chat with human astrologer when astrologer not answer for 30 sec in chat window.
            astrologerDetailBean.setCurrentFreeConsultation(CGlobalVariables.CURRENT_FREE_CONSULTATION_TYPE_USER_CHAT);

            AstrosageKundliApplication.selectedAstrologerDetailBean = astrologerDetailBean;
            AstrosageKundliApplication.chatAgainAstrologerDetailBean = astrologerDetailBean;

            AstrosageKundliApplication.IS_AGORA_CALL_RUNNING = false;
            Gson gson = new Gson();
            ConnectChatBean connectChatBean = gson.fromJson(jsonObject.toString(), ConnectChatBean.class);
            firebaseSignInOnly(connectChatBean.getToken());
            if (connectChatBean.getTalktime() != null && connectChatBean.getTalktime().length() > 0) {
                CGlobalVariables.userChatTime = connectChatBean.getTalktime();
            }
            final String callsId = jsonObject.getString("callsid");

            //CHANNEL_ID = callsId;
            CUtils.saveAstrologerIDAndChannelID(activity, AstrosageKundliApplication.selectedAstrologerDetailBean.getAstrologerId(), callsId);


            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (CUtils.checkServiceRunning(AstroAcceptRejectService.class)) {
                            activity.stopService(new Intent(activity, AstroAcceptRejectService.class));
                        }
                        startService(callsId, jsonObject, CGlobalVariables.userChatTime, true);
                        //Log.d("testChatApiResReq","initChatAfterResponseRandom startService ===>>>");

                        AstrosageKundliApplication.channelIdTempStore = callsId;
                        AstrosageKundliApplication.chatJsonObject = jsonObject.toString();
                        try {
                            if (activity instanceof AstrologerDescriptionActivity) {
                                ((AstrologerDescriptionActivity) activity).showOrHideCallChatInitiate();
                                ((AstrologerDescriptionActivity) activity).updateCallChatButton();
                            }
                            if (activity instanceof DashBoardActivity) {
                                ((DashBoardActivity) activity).showOrHideCallChatInitiate();
                                //((AstrologerDescriptionActivity) activity).updateCallChatButton();
                            }
                        } catch (Exception e) {
                            //
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 500);

        } catch (Exception e) {

            //Log.d("testChatApiResReq","e========="+e);
        }

    }

    private void startAIChatWindow(String callsId, JSONObject jsonObject, String userChatTime, String astroName, String profileUrl, String aiAstrologerId, String profileLargeImage) {
        if(activity instanceof FirstTimeProfileDetailsActivity){
            activity.finish();
        }
        Intent intent1 = new Intent(activity, AIChatWindowActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("msg", activity.getResources().getString(R.string.astrologer_accepted_chat_request));
        bundle.putString(CGlobalVariables.CHAT_USER_CHANNEL, callsId);
        bundle.putString("connect_chat_bean", jsonObject.toString());
        bundle.putString("astrologer_name", astroName);
        bundle.putString("astrologer_profile_url", profileUrl);
        bundle.putString("astrologer_id", AstrosageKundliApplication.selectedAstrologerDetailBean.getAstrologerId());
        bundle.putString("userChatTime", userChatTime);
        bundle.putString("ai_astrologer_id", aiAstrologerId);
        bundle.putString("image_file_large", profileLargeImage);
        //bundle.putSerializable("chatInitiateAstrologerDetailBean",astrologerDetailBean);
        intent1.putExtras(bundle);
        activity.startActivity(intent1);

    }

    private void initChatAfterResponse(JSONObject jsonObject) {
        try {
            AstrosageKundliApplication.IS_AGORA_CALL_RUNNING = false;
            Gson gson = new Gson();
            ConnectChatBean connectChatBean = new ConnectChatBean();
            connectChatBean = gson.fromJson(jsonObject.toString(), ConnectChatBean.class);
            firebaseSignInOnly(connectChatBean.getToken());
            if (connectChatBean.getTalktime() != null && connectChatBean.getTalktime().length() > 0) {
                CGlobalVariables.userChatTime = connectChatBean.getTalktime();
            }
            final String callsId = jsonObject.getString("callsid");
            final String talkTime = jsonObject.getString("talktime");
            final String exophoneNo = jsonObject.getString("exophone");
            final String internationalCharges = jsonObject.getString("callcharge");

            //CHANNEL_ID = callsId;
            CUtils.saveAstrologerIDAndChannelID(activity, AstrosageKundliApplication.selectedAstrologerDetailBean.getAstrologerId(), callsId);


            String astroName = "", profileUrl = "";
            if (AstrosageKundliApplication.selectedAstrologerDetailBean != null) {
                astroName = AstrosageKundliApplication.selectedAstrologerDetailBean.getName();
                profileUrl = AstrosageKundliApplication.selectedAstrologerDetailBean.getImageFile();

            }
            callInitiatedDialog = new CallInitiatedDialog(callsId, talkTime, exophoneNo, CGlobalVariables.CHAT_CLICK, internationalCharges, astroName, profileUrl);
            FragmentManager ft = ((FragmentActivity) activity).getSupportFragmentManager();

            callInitiatedDialog.show(ft, "Dialog");
            CUtils.fcmAnalyticsEvents(CGlobalVariables.SHOW_CHAT_INITIATE_DIALOG, AstrosageKundliApplication.currentEventType, "");
            handleChatOnDisconnect(callsId);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (CUtils.checkServiceRunning(AstroAcceptRejectService.class)) {
                            activity.stopService(new Intent(activity, AstroAcceptRejectService.class));
                        }
                        startService(callsId, jsonObject, CGlobalVariables.userChatTime, false);
                        AstrosageKundliApplication.channelIdTempStore = callsId;
                        AstrosageKundliApplication.chatJsonObject = jsonObject.toString();

                        try {
                            if (activity instanceof AstrologerDescriptionActivity) {
                                ((AstrologerDescriptionActivity) activity).showOrHideCallChatInitiate();
                                ((AstrologerDescriptionActivity) activity).updateCallChatButton();
                            }
                            if (activity instanceof DashBoardActivity) {
                                ((DashBoardActivity) activity).showOrHideCallChatInitiate();
                                //((AstrologerDescriptionActivity) activity).updateCallChatButton();
                            }
                        } catch (Exception e) {
                            //
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 500);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleChatOnDisconnect(String channelId) {
        // Write a string when this client loses connection
        AstrosageKundliApplication.getmFirebaseDatabase(channelId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        setOnDisconnectListner();
    }

    private void setOnDisconnectListner() {
        try {
            String channelId = CUtils.getSelectedChannelID(activity);
            AstrosageKundliApplication.getmFirebaseDatabase(channelId).child(CGlobalVariables.USER_DISCONNECT_TIME_FBD_KEY).onDisconnect().setValue(ServerValue.TIMESTAMP);
        } catch (Exception e) {
            //
        }
    }

    public void startCallInitiateService(JSONObject jsonObject) {
        try {
            //String jObject = "{\"msg\":\"CALL_INITIATED\",\"callsid\":\"IAC91M9934546862T1687081353460\",\"tokenid\":\"eyJhbGciOiJSUzI1NiJ9.eyJhdWQiOiJodHRwczovL2lkZW50aXR5dG9vbGtpdC5nb29nbGVhcGlzLmNvbS9nb29nbGUuaWRlbnRpdHkuaWRlbnRpdHl0b29sa2l0LnYxLklkZW50aXR5VG9vbGtpdCIsImV4cCI6MTY4NTQyOTQwOCwiaWF0IjoxNjg1NDI1ODA4LCJpc3MiOiJmaXJlYmFzZS1hZG1pbnNkay1tZ2xtMUBzdG9rZWQtdmlydHVlLTc2OS5pYW0uZ3NlcnZpY2VhY2NvdW50LmNvbSIsInN1YiI6ImZpcmViYXNlLWFkbWluc2RrLW1nbG0xQHN0b2tlZC12aXJ0dWUtNzY5LmlhbS5nc2VydmljZWFjY291bnQuY29tIiwidWlkIjoiVVNFUl85MS05OTM0NTQ2ODYyIn0.kodJ9P2d2RUtxFIll5BLd5SRUxicPTtuIrg5BMNtGf34ZLewOK8dOuQZb9WxwqDRmE5twB37OJmw-IheuHOpAgEfzo3EWxJtVprOdH80RANcGY9wIHaWAgY7qmIExLxntIWZHtpW1JqZsetncP75Yc0mPUFuiECZdVAfdtaoWdCowR-OlK5VmlkBZ-6v3TcclrUmqqHfGRbjdHbHz1P38w4KtexDMw_mEDuGuBL-32Aa3e8uIuU8m8Tur7XOqTQ9lfOOmJP7EQS7L3C7k1usr4lBRXVuAy2_nGmM2aIoxJ-l33GEZpYv9Gxh5MWVc2HGLyuzqdo248gj7qt59noBFA\",\"userphone\":\"9934546862\",\"countrycode\":\"91\",\"userfirstname\":\"null\",\"identity_user\":\"null\",\"identity_astrologer\":\"null\",\"talktime\":\"60:0\",\"exophone\":\"null\",\"astrologername\":\"Test+Profile+Manish\",\"callcharge\":\"0.0\",\"durationinsecs\":\"3600\",\"initialmessage\":\"\",\"agoratokenid\":\"007eJxTYIgIXWxZ+txwysN3y6Ylfs95+Lrmv/tT1lsc5Xtill98p2+pwGBsYZFqYZ5klJhkbGaSaJiWaJZobmFsnpJmkmiZbGlh8TVpYkpDICNDTP9eFkYGCATx5Rg8HZ0tDX0tLY1NTE3MLMyMQgzNLMwNLAyNTY1NzAwYGAD/BSeb\",\"status\":\"1\"}";
            String jObject = jsonObject.toString();
            AstrosageKundliApplication.isAgoraCallScreenOpened = false;
            Gson gson = new Gson();
            CUtils.connectAgoraCallBean = gson.fromJson(jObject, ConnectAgoraCallBean.class);
            Intent serviceIntent = new Intent(activity, AgoraCallInitiateService.class);
            Bundle bundle = new Bundle();
            bundle.putString(CGlobalVariables.AGORA_CALL_INITIATE_OBJECT, jObject);
            bundle.putString(CGlobalVariables.AGORA_CALL_TYPE, consultationType);
            serviceIntent.putExtras(bundle);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                activity.startForegroundService(serviceIntent);
            } else {
                activity.startService(serviceIntent);
            }
            if (!(activity instanceof AstrologerDescriptionActivity)) {
                CUtils.switchToConsultTab(FILTER_TYPE_CHAT, activity);
            }
        } catch (Exception e) {
            //
        }
    }

    private void startService(String callsId, JSONObject jsonObject, String userChatTime, boolean isFreeHumanRandomChat) {
        try {
            //Log.d("testChatApiResReq"," ChatUtils CHAT_CHANNEL_ID  =====   "+callsId);
            Intent serviceIntent = new Intent(activity, AstroAcceptRejectService.class);
            Bundle bundle = new Bundle();
            bundle.putString(CGlobalVariables.CHAT_USER_CHANNEL, callsId);
            bundle.putString(CGlobalVariables.CONNECT_CHAT_BEAN, jsonObject.toString());
            bundle.putString(CGlobalVariables.ASTROLOGER_NAME, AstrosageKundliApplication.selectedAstrologerDetailBean.getName());
            bundle.putString(CGlobalVariables.ASTROLOGER_PROFILE_URL, AstrosageKundliApplication.selectedAstrologerDetailBean.getImageFile());
            bundle.putString(CGlobalVariables.CHAT_ASTROLOGER_ID, AstrosageKundliApplication.selectedAstrologerDetailBean.getAstrologerId());
            bundle.putString(CGlobalVariables.USER_CHAT_TIME, userChatTime);
            bundle.putBoolean(CGlobalVariables.IS_FREE_HUMAN_RANDOM_CHAT, isFreeHumanRandomChat);
            // bundle.putSerializable("chatInitiateAstrologerDetailBean",AstrosageKundliApplication.chatInitiateAstrologerDetailBean);
            serviceIntent.putExtras(bundle);

            CUtils.sendLogDataRequest(AstrosageKundliApplication.selectedAstrologerDetailBean.getAstrologerId(), callsId, "Chat initiated & AstroAcceptRejectService is going to be start");

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                activity.startForegroundService(serviceIntent);
            } else {
                activity.startService(serviceIntent);
            }
            if (!(activity instanceof AstrologerDescriptionActivity)) {
                CUtils.switchToConsultTab(FILTER_TYPE_CHAT, activity);
            }
            closeChatInitiatedDialog();

            /**
             * this method call when chat initiate service not started.
             */
            AstrosageKundliApplication.startChatServiceTimer(callsId, jsonObject.toString(), AstrosageKundliApplication.selectedAstrologerDetailBean.getName(),
                    AstrosageKundliApplication.selectedAstrologerDetailBean.getImageFile(), AstrosageKundliApplication.selectedAstrologerDetailBean.getAstrologerId());


        } catch (Exception e) {
            //Log.d("testChatApiResReq","Exception 0000 ===>>>"+e);

            CUtils.sendLogDataRequest(AstrosageKundliApplication.selectedAstrologerDetailBean.getAstrologerId(), callsId, "Chat initiated & Exception in start AstroAcceptRejectService=" + e);
        }
    }


    public void closeChatInitiatedDialog() {
        if (callInitiatedDialog != null && callInitiatedDialog.isVisible()) {
            callInitiatedDialog.dismiss();
        }

    }

    public void initVideoCall(AstrologerDetailBean astrologerDetailBean) {
        try {
            CUtils.fcmAnalyticsEvents(CGlobalVariables.VIDEO_CALL_FIREBASE_AUTH,
                    AstrosageKundliApplication.currentEventType, "");
            consultationType = CGlobalVariables.TYPE_VIDEO_CALL;
            AstrosageKundliApplication.currentConsultType = "video_call";
            AstrosageKundliApplication.selectedAstrologerDetailBean = astrologerDetailBean;
            processConsultation();
        } catch (Exception e) {
            //
        }
    }

    public void initVoiceCall(AstrologerDetailBean astrologerDetailBean) {
        try {
            CUtils.fcmAnalyticsEvents(CGlobalVariables.AUDIO_CALL_FIREBASE_AUTH,
                    AstrosageKundliApplication.currentEventType, "");
            consultationType = CGlobalVariables.TYPE_VOICE_CALL;
            AstrosageKundliApplication.currentConsultType = "audio_call";
            AstrosageKundliApplication.selectedAstrologerDetailBean = astrologerDetailBean;
            processConsultation();
        } catch (Exception e) {
            //
        }
    }

    public void startAudioCall(UserProfileData userProfileData) {
        // Log.d("testChatNew","Connect Chat Api called");
        AstrologerDetailBean astrologerDetailBean = AstrosageKundliApplication.selectedAstrologerDetailBean;
        if (astrologerDetailBean == null) {
            try {
                Toast.makeText(activity, activity.getResources().getString(R.string.something_wrong_error), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                //
            }
            return;
        }
        String aiai = AstrosageKundliApplication.selectedAstrologerDetailBean.getAiAstrologerId();
        AstrosageKundliApplication.astrologerDetailBeanForBusyDialog = astrologerDetailBean;
        if (!TextUtils.isEmpty(aiai) && !aiai.equals("0")) {
            connectAIVoiceCall(astrologerDetailBean, userProfileData);
        } else {
            connectHumanVoiceCall(astrologerDetailBean, userProfileData);
        }
    }

    private void connectHumanVoiceCall(AstrologerDetailBean astrologerDetailBean, UserProfileData userProfileData) {
        showProgressBar();
        try {
            if (!CUtils.isConnectedWithInternet(activity)) {
                Toast.makeText(activity, activity.getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
            } else {
                errorLogs = errorLogs + "call api\n";
                CUtils.fcmAnalyticsEvents(CGlobalVariables.AUDIO_CALL_CONNECT, AstrosageKundliApplication.currentEventType, "");

                ApiList api = RetrofitClient.getInstance().create(ApiList.class);
                Call<ResponseBody> call = api.connectCall(getCallParams(userProfileData, "internetaudiocall", astrologerDetailBean));
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        stopProgressBar();
                        try {
                            errorLogs = errorLogs + "api response->" + response + "\n";
                            //Log.d("xxccddssff","Response is ==>>>>>" + response);
                            String myResponse = response.body().string();
                            //Log.d("testcallAstrodeatils","myResponse==>>"+myResponse);
                            JSONObject jsonObject = new JSONObject(myResponse);
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("msg");
                            if (status.equals(CGlobalVariables.AGORA_CALL_INITIATED)) {
                                CUtils.fcmAnalyticsEvents(CGlobalVariables.AGORA_CALL_RES_AUDIO_VIDEO_INITIATED, AstrosageKundliApplication.currentEventType, "");
                                initAudioVideoCallAfterResponse(jsonObject);
                            } else if (status.equals(CGlobalVariables.CALL_INITIATED)) {
                                CUtils.fcmAnalyticsEvents(CGlobalVariables.AGORA_CALL_RES_NETWORK_PHONE_INITIATED, AstrosageKundliApplication.currentEventType, "");

                                if (activity instanceof AstrologerDescriptionActivity) {
                                    ((AstrologerDescriptionActivity) activity).responseNetworkCall(myResponse);
                                }
                                if (activity instanceof DashBoardActivity) {
                                    getVisibleFragment(myResponse);
                                }
                                if (activity instanceof ActAppModule) {
                                    getVisibleFragment(myResponse);
                                }
                                if (activity instanceof ChatWindowActivity) {
                                    activity.finish();
                                }
                            } else if (status.equals(CGlobalVariables.INSUFFICIENT_BALANCE)) {
                                CUtils.fcmAnalyticsEvents(CGlobalVariables.AGORA_CALL_RES_INSUFF_BAL, AstrosageKundliApplication.currentEventType, "");
                                //stopActivityIndicator();
                                String astrologerName = jsonObject.getString("astrologername");
                                String userbalance = jsonObject.getString("userbalance");
                                String minBalance = jsonObject.getString("minbalance");
                                FragmentManager ft = ((FragmentActivity) activity).getSupportFragmentManager();
                                if(CUtils.isSuggestedRechargeDialogShow(activity)){
                                    com.ojassoft.astrosage.varta.utils.CUtils.openQuickRechargeSheet(activity,astrologerName,minBalance,userbalance,ft);
                                }else {
                                    InSufficientBalanceDialog dialog = new InSufficientBalanceDialog(astrologerName, userbalance, minBalance, "chat_global");
                                    dialog.show(ft, "Dialog");
                                }
                                AstrosageKundliApplication.chatAstrologerDetailBeanAfterRecharge = astrologerDetailBean;
                                AstrosageKundliApplication.checkWhichConsultationScreen = "HUMAN_CALL";

                            } else if (status.equals(CGlobalVariables.ASTROLOGER_BUSY_OFFLINE)) {
                                String fcm_label = CGlobalVariables.VIDEO_CALL_API_RES_MSG + message;
                                CUtils.fcmAnalyticsEvents(fcm_label, AstrosageKundliApplication.currentEventType, "");
                                //stopActivityIndicator();
                                String dialogMsg = "";
                                if (message.equalsIgnoreCase(CGlobalVariables.USER_BLOCKED_MSG)) {
                                    dialogMsg = activity.getResources().getString(R.string.user_blocked_msg);
                                } else if (message.equalsIgnoreCase(CGlobalVariables.ASTROLOGER_STATUS_BUSY_MSG)) {
                                    dialogMsg = activity.getResources().getString(R.string.astrologer_busy_msg_chat);
                                } else if (message.equalsIgnoreCase(CGlobalVariables.ASTROLOGER_STATUS_OFFLINE_MSG)) {
                                    String stringMsg = "";
                                    if ((AstrosageKundliApplication.selectedAstrologerDetailBean.getIsAvailableForChat().equalsIgnoreCase("true")) && (AstrosageKundliApplication.selectedAstrologerDetailBean.getIsAvailableForCall().equalsIgnoreCase("false"))) {
                                        stringMsg = activity.getResources().getString(R.string.astrologer_offline_call);
                                    } else if ((AstrosageKundliApplication.selectedAstrologerDetailBean.getIsAvailableForChat().equalsIgnoreCase("false")) && (AstrosageKundliApplication.selectedAstrologerDetailBean.getIsAvailableForCall().equalsIgnoreCase("true"))) {
                                        stringMsg = activity.getResources().getString(R.string.astrologer_offline_chat);
                                    } else {
                                        stringMsg = activity.getResources().getString(R.string.astrologer_offline_msg);
                                    }
                                    dialogMsg = stringMsg;
                                } else if (message.equalsIgnoreCase(CGlobalVariables.ASTROLOGER_STATUS_DISABLED)) {
                                    dialogMsg = activity.getResources().getString(R.string.astrologer_status_disable);
                                } else if (message.equalsIgnoreCase(CGlobalVariables.CALL_API_FAILED)) {
                                    dialogMsg = activity.getResources().getString(R.string.call_api_failed_msg);
                                } else if (message.equalsIgnoreCase(CGlobalVariables.USE_ANOTHER_CALL_MSG)) {
                                    dialogMsg = activity.getResources().getString(R.string.existing_chat_msg);
                                } else if (message.equalsIgnoreCase(CGlobalVariables.ASTROLOGER_STATUS_UNAVAILABLE)) {
                                    dialogMsg = activity.getResources().getString(R.string.astrologer_unavailable_video_call);
                                } else {
                                    dialogMsg = activity.getResources().getString(R.string.existing_chat_msg);
                                }

                                //destroyChatFragment();
                                callMsgDialogData(dialogMsg, "", true, CGlobalVariables.CHAT_CLICK);
                                if (activity instanceof ChatWindowActivity) {
                                    ((ChatWindowActivity) activity).showHideCallAgainButton(false);
                                }

                            } else if (status.equals(CGlobalVariables.UNABLE_TO_CONNECT_CHAT)) {
                                CUtils.fcmAnalyticsEvents(CGlobalVariables.VIDEO_CALL_RES_STATUS_3, AstrosageKundliApplication.currentEventType, "");
                                //stopActivityIndicator();
                                String dialogMsg = "";
                                dialogMsg = activity.getResources().getString(R.string.unable_to_connect_chat);
                                dialogMsg = dialogMsg.replace("#", CGlobalVariables.UNABLE_TO_CONNECT_CHAT);
                                callMsgDialogData(dialogMsg, "", true, CGlobalVariables.CHAT_CLICK);
                            } else if (status.equals("55")) {
                                String permission = "0";
                                if (!TextUtils.isEmpty(CUtils.getHavePermissionRecordAudio())) {
                                    permission = CUtils.getHavePermissionRecordAudio();
                                }
                               if(permission.equals("0")){
                                   CUtils.displayRequiredPermissionDialog(activity);
                               }else {
                                   Toast.makeText(activity, "please try again! ( Error code: " + status + " )", Toast.LENGTH_LONG).show();
                               }

                            } else if (status.equals("100")) {
                                if (AstrosageKundliApplication.backgroundLoginCountForChat < 2) {
                                    callBackgroundLoginApi(activity, CGlobalVariables.TYPE_VOICE_CALL, userProfileData);
                                    AstrosageKundliApplication.backgroundLoginCountForChat += 1;
                                }
                            } else {
                                CUtils.fcmAnalyticsEvents("video_call_res_status_error", AstrosageKundliApplication.currentEventType, "");
                                Toast.makeText(activity, "Authentication issue, please try again! ( Error code: " + status + " )", Toast.LENGTH_LONG).show();
                            }
                        } catch (Exception e) {
                            CUtils.fcmAnalyticsEvents(CGlobalVariables.VIDEO_CALL_RES_STATUS_EXCEPTION, AstrosageKundliApplication.currentEventType, "");
                            //stopActivityIndicator();
                            e.printStackTrace();
                            //Log.e("SAN CI DA ", " onresponse method == CONNECT_CHAT_API_RESPONSE exp " + e.toString() );
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        stopProgressBar();
                    }
                });
            }
        } catch (Exception e) {
            //
        }

    }

    public void startVideoCall(UserProfileData userProfileData) {
        // Log.d("testChatNew","Connect Chat Api called");
        AstrologerDetailBean astrologerDetailBean = AstrosageKundliApplication.selectedAstrologerDetailBean;
        if (astrologerDetailBean == null) {
            try {
                Toast.makeText(activity, activity.getResources().getString(R.string.something_wrong_error), Toast.LENGTH_SHORT).show();
            } catch (Exception e) {
                //
            }
            return;
        }
        showProgressBar();
        if (!CUtils.isConnectedWithInternet(activity)) {
            Toast.makeText(activity, activity.getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        } else {
            CUtils.fcmAnalyticsEvents(CGlobalVariables.VIDEO_CALL_CONNECT, AstrosageKundliApplication.currentEventType, "");

            ApiList apiList = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> apiCall = apiList.connectinternetvideocall(getCallParams(userProfileData, "internetvideocall", astrologerDetailBean));
            apiCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        String responses = response.body().string();
                        stopProgressBar();
                        errorLogs = errorLogs + "api response->" + responses + "\n";
                        //Log.d("xxccddssff","Response is ==>>>>>" + response);
                        JSONObject jsonObject = new JSONObject(responses);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("msg");
                        if (status.equals(CGlobalVariables.AGORA_CALL_INITIATED)) {
                            CUtils.fcmAnalyticsEvents(CGlobalVariables.AGORA_CALL_RES_AUDIO_VIDEO_INITIATED, AstrosageKundliApplication.currentEventType, "");
                            initAudioVideoCallAfterResponse(jsonObject);
                        } else if (status.equals(CGlobalVariables.CALL_INITIATED)) {
                            CUtils.fcmAnalyticsEvents(CGlobalVariables.AGORA_CALL_RES_NETWORK_PHONE_INITIATED, AstrosageKundliApplication.currentEventType, "");

                            if (activity instanceof AstrologerDescriptionActivity) {
                                ((AstrologerDescriptionActivity) activity).responseNetworkCall(responses);
                            }
                            if (activity instanceof DashBoardActivity) {
                                getVisibleFragment(responses);
                            }
                            if (activity instanceof ActAppModule) {
                                getVisibleFragment(responses);
                            }
                            if (activity instanceof ChatWindowActivity) {
                                activity.finish();
                            }
                        } else if (status.equals(CGlobalVariables.INSUFFICIENT_BALANCE)) {
                            CUtils.fcmAnalyticsEvents(CGlobalVariables.AGORA_CALL_RES_INSUFF_BAL, AstrosageKundliApplication.currentEventType, "");
                            //stopActivityIndicator();
                            String astrologerName = jsonObject.getString("astrologername");
                            String userbalance = jsonObject.getString("userbalance");
                            String minBalance = jsonObject.getString("minbalance");

                            InSufficientBalanceDialog dialog = new InSufficientBalanceDialog(astrologerName, userbalance, minBalance, "chat_global");
                            FragmentManager ft = ((FragmentActivity) activity).getSupportFragmentManager();
                            dialog.show(ft, "Dialog");

                        } else if (status.equals(CGlobalVariables.ASTROLOGER_BUSY_OFFLINE)) {
                            String fcm_label = CGlobalVariables.VIDEO_CALL_API_RES_MSG + message;
                            CUtils.fcmAnalyticsEvents(fcm_label, AstrosageKundliApplication.currentEventType, "");
                            //stopActivityIndicator();
                            String dialogMsg = "";
                            if (message.equalsIgnoreCase(CGlobalVariables.USER_BLOCKED_MSG)) {
                                dialogMsg = activity.getResources().getString(R.string.user_blocked_msg);
                            } else if (message.equalsIgnoreCase(CGlobalVariables.ASTROLOGER_STATUS_BUSY_MSG)) {
                                dialogMsg = activity.getResources().getString(R.string.astrologer_busy_msg_chat);
                            } else if (message.equalsIgnoreCase(CGlobalVariables.ASTROLOGER_STATUS_OFFLINE_MSG)) {
                                String stringMsg = "";
                                if ((AstrosageKundliApplication.selectedAstrologerDetailBean.getIsAvailableForChat().equalsIgnoreCase("true")) && (AstrosageKundliApplication.selectedAstrologerDetailBean.getIsAvailableForCall().equalsIgnoreCase("false"))) {
                                    stringMsg = activity.getResources().getString(R.string.astrologer_offline_call);
                                } else if ((AstrosageKundliApplication.selectedAstrologerDetailBean.getIsAvailableForChat().equalsIgnoreCase("false")) && (AstrosageKundliApplication.selectedAstrologerDetailBean.getIsAvailableForCall().equalsIgnoreCase("true"))) {
                                    stringMsg = activity.getResources().getString(R.string.astrologer_offline_chat);
                                } else {
                                    stringMsg = activity.getResources().getString(R.string.astrologer_offline_msg);
                                }
                                dialogMsg = stringMsg;
                            } else if (message.equalsIgnoreCase(CGlobalVariables.ASTROLOGER_STATUS_DISABLED)) {
                                dialogMsg = activity.getResources().getString(R.string.astrologer_status_disable);
                            } else if (message.equalsIgnoreCase(CGlobalVariables.CALL_API_FAILED)) {
                                dialogMsg = activity.getResources().getString(R.string.call_api_failed_msg);
                            } else if (message.equalsIgnoreCase(CGlobalVariables.USE_ANOTHER_CALL_MSG)) {
                                dialogMsg = activity.getResources().getString(R.string.existing_chat_msg);
                            } else if (message.equalsIgnoreCase(CGlobalVariables.ASTROLOGER_STATUS_UNAVAILABLE)) {
                                dialogMsg = activity.getResources().getString(R.string.astrologer_unavailable_video_call);
                            } else {
                                dialogMsg = activity.getResources().getString(R.string.existing_chat_msg);
                            }

                            //destroyChatFragment();
                            callMsgDialogData(dialogMsg, "", true, CGlobalVariables.CHAT_CLICK);
                            if (activity instanceof ChatWindowActivity) {
                                ((ChatWindowActivity) activity).showHideCallAgainButton(false);
                            }

                        } else if (status.equals(CGlobalVariables.UNABLE_TO_CONNECT_CHAT)) {
                            CUtils.fcmAnalyticsEvents(CGlobalVariables.VIDEO_CALL_RES_STATUS_3, AstrosageKundliApplication.currentEventType, "");
                            //stopActivityIndicator();
                            String dialogMsg = "";
                            dialogMsg = activity.getResources().getString(R.string.unable_to_connect_chat);
                            dialogMsg = dialogMsg.replace("#", CGlobalVariables.UNABLE_TO_CONNECT_CHAT);
                            callMsgDialogData(dialogMsg, "", true, CGlobalVariables.CHAT_CLICK);
                        } else if (status.equals("100")) {
                            if (AstrosageKundliApplication.backgroundLoginCountForChat < 2) {
                                callBackgroundLoginApi(activity, TYPE_VIDEO_CALL, userProfileData);
                                AstrosageKundliApplication.backgroundLoginCountForChat += 1;
                            }
                        } else {
                            CUtils.fcmAnalyticsEvents("video_call_res_status_error", AstrosageKundliApplication.currentEventType, "");
                            Toast.makeText(activity, "Authentication issue, please try again! ( Error code: " + status + " )", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.VIDEO_CALL_RES_STATUS_EXCEPTION, AstrosageKundliApplication.currentEventType, "");
                        //stopActivityIndicator();
                        e.printStackTrace();
                        //Log.e("SAN CI DA ", " onresponse method == CONNECT_CHAT_API_RESPONSE exp " + e.toString() );
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    stopProgressBar();
                }
            });

        }
    }


    public void initAudioVideoCallAfterResponse(JSONObject jsonObject) {
        try {
            AstrosageKundliApplication.IS_AGORA_CALL_RUNNING = false;

            firebaseSignInOnly(jsonObject.optString("tokenid"));

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (CUtils.checkServiceRunning(AgoraCallInitiateService.class)) {
                            activity.stopService(new Intent(activity, AgoraCallInitiateService.class));
                        }
                        startCallInitiateService(jsonObject);
                        //AstrosageKundliApplication.chatJsonObject = jsonObject.toString();

                        try {
                            if (activity instanceof AstrologerDescriptionActivity) {
                                ((AstrologerDescriptionActivity) activity).showOrHideCallChatInitiate();
                                ((AstrologerDescriptionActivity) activity).updateCallChatButton();
                            }
                            if (activity instanceof DashBoardActivity) {
                                ((DashBoardActivity) activity).showOrHideCallChatInitiate();
                            }
                        } catch (Exception e) {
                            //Log.d("testAgoraCall",e.toString());
                        }

                    } catch (Exception e) {
                        // Log.d("testAgoraCall",e.toString());
                        e.printStackTrace();
                    }
                }
            }, 500);
        } catch (Exception e) {
            e.printStackTrace();
            // Log.d("testAgoraCall",e.toString());
        }
    }

    public void getVisibleFragmentAndUpdateStatus() {
        try {
            FragmentManager fm = ((FragmentActivity) activity).getSupportFragmentManager();
            List<Fragment> fragments = fm.getFragments();
            for (int i = fragments.size() - 1; i >= 0; i--) {
                Fragment fragment = fragments.get(i);
                if (fragment instanceof HomeFragment1) {
                    HomeFragment1 homeFragment1 = (HomeFragment1) ((FragmentActivity) activity).getSupportFragmentManager().findFragmentById(R.id.nav_host_fragment);
                    assert homeFragment1 != null;
                    homeFragment1.getAstrologersStatusPrice();
                }
            }
        } catch (Exception e) {
            //
        }
    }

    private void callBackgroundLoginApi(Activity activity, String consultationType, UserProfileData userProfileData) {
        String _pwd = CUtils.getUserLoginPassword(activity);
        if (TextUtils.isEmpty(_pwd)) {
            return;
        }
        ApiList api = RetrofitClient.getInstance().create(ApiList.class);
        Call<ResponseBody> call = api.backgroundLogin(getVerifyLoginParams(_pwd));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull retrofit2.Response<ResponseBody> response) {
                try {
                    assert response.body() != null;
                    String myResponse = response.body().string();
                    //Log.d("TestChat","TestChat==>> callBackgroundLoginApi myResponse"+myResponse);
                    parseLoginResponse(myResponse, activity, consultationType, userProfileData);

                } catch (Exception e) {
                    //
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {


            }
        });
    }

    public static Map<String, String> getVerifyLoginParams(String _pwd) {
        Context context = AstrosageKundliApplication.getAppContext();
        LANGUAGE_CODE = AstrosageKundliApplication.getAppContext().getLanguageCode();
        String deviceId = CUtils.getMyAndroidId(context);
        String key = CUtils.getApplicationSignatureHashCode(context);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(context));
        params.put(CGlobalVariables.PHONE_NO, CUtils.getUserID(context));
        params.put(KEY_PASSWORD, _pwd);
        params.put(CGlobalVariables.KEY_API, key);
        params.put(DEVICE_ID, deviceId);
        params.put("packageName", BuildConfig.APPLICATION_ID);
        params.put(CGlobalVariables.PROPERTY_APP_VERSION, BuildConfig.VERSION_NAME);
        params.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
        // Log.e("SAN CHAT ", " Loginservice params " + params.toString() );
        return params;
    }

    private void parseLoginResponse(String response, Activity activity, String consultationType, UserProfileData userProfileData) {
        try {
            JSONObject jsonObject = new JSONObject(response);
            if (jsonObject.getString("status").equals("1")) {
                //login success
                boolean liveintrooffer = jsonObject.getBoolean("liveintrooffer");
                String privateintrooffertype = jsonObject.getString("privateintrooffertype");
                CUtils.setUserOffers(activity, liveintrooffer, privateintrooffertype);
                if (consultationType.equals(TYPE_AI_CHAT)) {
                    ChatUtils.getInstance(activity).startAIChat(userProfileData);
                }
                if (consultationType.equals(TYPE_CHAT)) {
                    ChatUtils.getInstance(activity).startChat(userProfileData);
                }
                if (consultationType.equals(CGlobalVariables.TYPE_AI_CHAT_RANDOM)) {
                    ChatUtils.getInstance(activity).startAIChatRandom(userProfileData, AstrosageKundliApplication.apiCallingSource);
                }
                if (consultationType.equals(CGlobalVariables.TYPE_CHAT_RANDOM)) {
                    ChatUtils.getInstance(activity).startChatRandom(userProfileData, AstrosageKundliApplication.apiCallingSource);
                }
                if (consultationType.equals(CGlobalVariables.TYPE_VOICE_CALL)) {
                    ChatUtils.getInstance(activity).startAudioCall(userProfileData);
                }
                if (consultationType.equals(CGlobalVariables.TYPE_VIDEO_CALL)) {
                    ChatUtils.getInstance(activity).startVideoCall(userProfileData);
                }
                if (consultationType.equals(TYPE_AI_CALL_RANDOM)) {
                    ChatUtils.getInstance(activity).connectAIVoiceCallRandom(userProfileData, AstrosageKundliApplication.apiCallingSource);
                }
            } else {
                CUtils.fcmAnalyticsEvents("bg_varta_login_fail", CGlobalVariables.VARTA_BACKGROUND_LOGIN, "");
                //Log.e("LoginResponse1", "onUserLogout2");
                //sendBroadcastMesage(CGlobalVariables.FAIL);
            }

        } catch (Exception e) {
            Log.d("TestChat", "TestChat==>> callBackgroundLoginApi Exception" + e);

            e.printStackTrace();
        }
    }


    public void initCallRandomAstrologer(String apiCallingSource) {
        try {
            consultationType = CGlobalVariables.TYPE_CALL_RANDOM;
            UserProfileData userProfileData = CUtils.getUserSelectedProfileFromPreference(activity);
            if (isCompleteUserData(userProfileData)) {
                startCallRandom(userProfileData, apiCallingSource);
            } else {
                AstrosageKundliApplication.apiCallingSource = apiCallingSource;
                CUtils.openProfileOrKundliAct(activity, CGlobalVariables.TYPE_CALL_RANDOM, consultationType, BACK_FROM_PROFILE_CHAT_DIALOG);
            }
        } catch (Exception e) {
//             Log.d("TestLogsDeepCopy","Exception e 1"+e);
        }

    }

    private void startCallRandom(UserProfileData userProfileData, String apiCallingSource) {
        if (!CUtils.isConnectedWithInternet(activity)) {
            Toast.makeText(activity, activity.getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        } else {
            showProgressBar();
            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Log.d("testCallRandApiResReq", "apiCallingSource==>>" + apiCallingSource + "      " + getChatParamsRandom(userProfileData, apiCallingSource));
            Call<ResponseBody> call = api.connectCall(getChatParamsRandom(userProfileData, apiCallingSource));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    stopProgressBar();
                    try {
                        Log.d("testCallRandApiResReq", "Response is ==>>>>>" + response);
                        String myResponse = response.body().string();
                        Log.d("testCallRandApiResReq", "myResponse==>>" + myResponse);
                        JSONObject jsonObject = new JSONObject(myResponse);
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("msg");
                        if (status.equals(CGlobalVariables.AGORA_CALL_INITIATED)) {
                            CUtils.fcmAnalyticsEvents(CGlobalVariables.AGORA_CALL_RES_AUDIO_VIDEO_INITIATED, AstrosageKundliApplication.currentEventType, "");
                            initAudioVideoCallAfterResponse(jsonObject);
                        } else if (status.equals(CGlobalVariables.CALL_INITIATED)) {
                            CUtils.fcmAnalyticsEvents(CGlobalVariables.AGORA_CALL_RES_NETWORK_PHONE_INITIATED, AstrosageKundliApplication.currentEventType, "");

                            if (activity instanceof AstrologerDescriptionActivity) {
                                ((AstrologerDescriptionActivity) activity).responseNetworkCall(myResponse);
                            }
                            if (activity instanceof FirstTimeProfileDetailsActivity) {
                                ((FirstTimeProfileDetailsActivity) activity).showCallInitiatedDialog(myResponse);
                            }
                            if (activity instanceof DashBoardActivity) {
                                getVisibleFragment(myResponse);
                            }
                            if (activity instanceof ActAppModule) {
                                getVisibleFragment(myResponse);
                            }
                            if (activity instanceof ChatWindowActivity) {
                                activity.finish();
                            }
                        } else if (status.equals(CGlobalVariables.INSUFFICIENT_BALANCE)) {
                            CUtils.fcmAnalyticsEvents(CGlobalVariables.AGORA_CALL_RES_INSUFF_BAL, AstrosageKundliApplication.currentEventType, "");
                            //stopActivityIndicator();
                            String astrologerName = jsonObject.getString("astrologername");
                            String userbalance = jsonObject.getString("userbalance");
                            String minBalance = jsonObject.getString("minbalance");

                            InSufficientBalanceDialog dialog = new InSufficientBalanceDialog(astrologerName, userbalance, minBalance, "chat_global");
                            FragmentManager ft = ((FragmentActivity) activity).getSupportFragmentManager();
                            dialog.show(ft, "Dialog");

                        } else if (status.equals(CGlobalVariables.ASTROLOGER_BUSY_OFFLINE)) {
                            String fcm_label = CGlobalVariables.VIDEO_CALL_API_RES_MSG + message;
                            CUtils.fcmAnalyticsEvents(fcm_label, AstrosageKundliApplication.currentEventType, "");
                            //stopActivityIndicator();
                            String dialogMsg = "";
                            if (message.equalsIgnoreCase(CGlobalVariables.USER_BLOCKED_MSG)) {
                                dialogMsg = activity.getResources().getString(R.string.user_blocked_msg);
                            } else if (message.equalsIgnoreCase(CGlobalVariables.ASTROLOGER_STATUS_BUSY_MSG)) {
                                dialogMsg = activity.getResources().getString(R.string.astrologer_busy_msg_chat);
                            } else if (message.equalsIgnoreCase(CGlobalVariables.ASTROLOGER_STATUS_OFFLINE_MSG)) {
                                String stringMsg = "";
                                if ((AstrosageKundliApplication.selectedAstrologerDetailBean.getIsAvailableForChat().equalsIgnoreCase("true")) && (AstrosageKundliApplication.selectedAstrologerDetailBean.getIsAvailableForCall().equalsIgnoreCase("false"))) {
                                    stringMsg = activity.getResources().getString(R.string.astrologer_offline_call);
                                } else if ((AstrosageKundliApplication.selectedAstrologerDetailBean.getIsAvailableForChat().equalsIgnoreCase("false")) && (AstrosageKundliApplication.selectedAstrologerDetailBean.getIsAvailableForCall().equalsIgnoreCase("true"))) {
                                    stringMsg = activity.getResources().getString(R.string.astrologer_offline_chat);
                                } else {
                                    stringMsg = activity.getResources().getString(R.string.astrologer_offline_msg);
                                }
                                dialogMsg = stringMsg;
                            } else if (message.equalsIgnoreCase(CGlobalVariables.ASTROLOGER_STATUS_DISABLED)) {
                                dialogMsg = activity.getResources().getString(R.string.astrologer_status_disable);
                            } else if (message.equalsIgnoreCase(CGlobalVariables.CALL_API_FAILED)) {
                                dialogMsg = activity.getResources().getString(R.string.call_api_failed_msg);
                            } else if (message.equalsIgnoreCase(CGlobalVariables.USE_ANOTHER_CALL_MSG)) {
                                dialogMsg = activity.getResources().getString(R.string.existing_chat_msg);
                            } else if (message.equalsIgnoreCase(CGlobalVariables.ASTROLOGER_STATUS_UNAVAILABLE)) {
                                dialogMsg = activity.getResources().getString(R.string.astrologer_unavailable_video_call);
                            } else {
                                dialogMsg = activity.getResources().getString(R.string.existing_chat_msg);
                            }

                            //destroyChatFragment();
                            callMsgDialogData(dialogMsg, "", true, CGlobalVariables.CHAT_CLICK);
                            if (activity instanceof ChatWindowActivity) {
                                ((ChatWindowActivity) activity).showHideCallAgainButton(false);
                            }

                        } else if (status.equals(CGlobalVariables.UNABLE_TO_CONNECT_CHAT)) {
                            CUtils.fcmAnalyticsEvents(CGlobalVariables.VIDEO_CALL_RES_STATUS_3, AstrosageKundliApplication.currentEventType, "");
                            //stopActivityIndicator();
                            String dialogMsg = "";
                            dialogMsg = activity.getResources().getString(R.string.unable_to_connect_chat);
                            dialogMsg = dialogMsg.replace("#", CGlobalVariables.UNABLE_TO_CONNECT_CHAT);
                            callMsgDialogData(dialogMsg, "", true, CGlobalVariables.CHAT_CLICK);
                        } else if (status.equals("55")) {
                            CUtils.displayRequiredPermissionDialog(activity);
                        } else if (status.equals("100")) {
                            if (AstrosageKundliApplication.backgroundLoginCountForChat < 2) {
                                callBackgroundLoginApi(activity, CGlobalVariables.TYPE_VOICE_CALL, userProfileData);
                                AstrosageKundliApplication.backgroundLoginCountForChat += 1;
                            }
                        } else {
                            CUtils.fcmAnalyticsEvents("video_call_res_status_error", AstrosageKundliApplication.currentEventType, "");
                            Toast.makeText(activity, "Authentication issue, please try again! ( Error code: " + status + " )", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.VIDEO_CALL_RES_STATUS_EXCEPTION, AstrosageKundliApplication.currentEventType, "");
                        //stopActivityIndicator();
                        e.printStackTrace();
                        //Log.e("SAN CI DA ", " onresponse method == CONNECT_CHAT_API_RESPONSE exp " + e.toString() );
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    CUtils.isAutoConsultationConnected = false;
                    Log.e("TestChat", " onFailure ==>>>>>>>>" + call);
                    showChatAgainBtn(false);
                    stopProgressBar();
                    callMsgDialogData(activity.getResources().getString(R.string.something_wrong_error), "", true, CGlobalVariables.CHAT_CLICK);
                }
            });
        }
    }

    /**
     * This method is used to submit data on server while user cancel the recharge dialog after free chat complete
     */

    public void cancelRechargeAfterChat(String channelId, String action) {
        try {
            if (!CUtils.isConnectedWithInternet(activity)) {
                return;
            }
            ApiList apiList = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> apiCall = apiList.cancelRechargeAfterChat(getCancelRechargeParams(activity, channelId, action));
            apiCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                    try {
                        String responses = response.body().string();
                        Log.e("TestCancelRecharge", "responses=" + responses);
                    } catch (Exception e) {
                        //
                    }

                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("TestCancelRecharge", "onFailure=" + t);
                }
            });
        } catch (Exception e) {
            //
        }
    }

    public static Map<String, String> getCancelRechargeParams(Context context, String channelId, String action) {

        HashMap<String, String> headers = new HashMap<String, String>();
        if (context == null) return headers;
        String currentOfferType = CUtils.getCallChatOfferType(context);
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(context));
        headers.put("channelid", channelId);
        headers.put("userphoneno", CUtils.getUserID(context));
        headers.put("action", action);
        headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(context));
        headers.put(CGlobalVariables.OFFER_TYPE, currentOfferType);
        //Log.e("TestCancelRecharge","headers="+headers);
        return CUtils.setRequiredParams(headers);
    }

    public void connectAIVoiceCall(AstrologerDetailBean astrologerDetailBean, UserProfileData userProfileData) {
        // Log.d("testChatNew","Connect Chat Api called");

        showProgressBar();
        if (!CUtils.isConnectedWithInternet(activity)) {
            Toast.makeText(activity, activity.getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        } else {
            CUtils.fcmAnalyticsEvents(CGlobalVariables.AI_AUDIO_CALL_CONNECT, AstrosageKundliApplication.currentEventType, "");

            ApiList apiList = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> apiCall = apiList.connectAICall(getCallParams(userProfileData, "aiVoiceCall", astrologerDetailBean));
            boolean isFreeCall;
            if (astrologerDetailBean.getIntroOfferType() != null) {
                isFreeCall = astrologerDetailBean.isFreeForCall() && (astrologerDetailBean.getIntroOfferType().equals(CGlobalVariables.INTRO_OFFER_TYPE_FREE));
            } else {
                isFreeCall = false;
            }
            apiCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        String responses = response.body().string();
                        stopProgressBar();
                        errorLogs = errorLogs + "api response->" + responses + "\n";
                        //Log.d("testAiCAll","connectAIVoiceCall req_url ==>>>>>" + response.raw().request().url());
                        JSONObject jsonObject = new JSONObject(responses);
                        //Log.d("testAiCAll","connectAIVoiceCall Json  is ==>>>>>" + jsonObject.toString());
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("msg");
                        if (activity instanceof AstrologerDescriptionActivity) {
                            ((AstrologerDescriptionActivity) activity).enableChatBtn();
                        }
                        if (status.equals(CGlobalVariables.CHAT_INITIATED)) {
                            CUtils.fcmAnalyticsEvents(CGlobalVariables.AI_CALL_RES_AUDIO_INITIATED, AstrosageKundliApplication.currentEventType, "");
                            initAIVoiceCallAfterResponse(jsonObject,isFreeCall);
                        } else if (status.equals(CGlobalVariables.INSUFFICIENT_BALANCE)) {
                            CUtils.fcmAnalyticsEvents(CGlobalVariables.AI_CALL_RES_INSUFF_BAL, AstrosageKundliApplication.currentEventType, "");
                            //stopActivityIndicator();
                            String astrologerName = jsonObject.getString("astrologername");
                            String userbalance = jsonObject.getString("userbalance");
                            String minBalance = jsonObject.getString("minbalance");
                            String fromWhichScreen;
                            if(activity instanceof AIChatWindowActivity){
                                fromWhichScreen = CGlobalVariables.AICHATWINDOWACTIVITY;
                            }else if(activity instanceof AIVoiceCallingActivity){
                                fromWhichScreen = CGlobalVariables.AIVOICECALLINGACTIVITY;
                            } else {
                                fromWhichScreen = "";
                            }
                            FragmentManager ft = ((FragmentActivity) activity).getSupportFragmentManager();
                            if(CUtils.isSuggestedRechargeDialogShow(activity)){
                                com.ojassoft.astrosage.varta.utils.CUtils.openQuickRechargeSheet(activity,astrologerName,minBalance,userbalance,ft);
                            }else {
                                InSufficientBalanceDialog dialog = new InSufficientBalanceDialog(astrologerName, userbalance, minBalance, fromWhichScreen);
                                dialog.show(ft, "Dialog");
                            }
                            AstrosageKundliApplication.checkWhichConsultationScreen= "AIVOICECALL";
                            AstrosageKundliApplication.chatAstrologerDetailBeanAfterRecharge = astrologerDetailBean;

                        } else if (status.equals(CGlobalVariables.ASTROLOGER_BUSY_OFFLINE)) {
                            String fcm_label = CGlobalVariables.AI_CALL_API_RES_MSG + message;
                            CUtils.fcmAnalyticsEvents(fcm_label, AstrosageKundliApplication.currentEventType, "");
                            //stopActivityIndicator();
                            String dialogMsg = "";
                            if (message.equalsIgnoreCase(CGlobalVariables.USER_BLOCKED_MSG)) {
                                dialogMsg = activity.getResources().getString(R.string.user_blocked_msg);
                            } else if (message.equalsIgnoreCase(CGlobalVariables.ASTROLOGER_STATUS_BUSY_MSG)) {
                                dialogMsg = activity.getResources().getString(R.string.astrologer_busy_msg_chat);
                            } else if (message.equalsIgnoreCase(CGlobalVariables.ASTROLOGER_STATUS_OFFLINE_MSG)) {
                                String stringMsg = "";
                                if ((AstrosageKundliApplication.selectedAstrologerDetailBean.getIsAvailableForChat().equalsIgnoreCase("true")) && (AstrosageKundliApplication.selectedAstrologerDetailBean.getIsAvailableForCall().equalsIgnoreCase("false"))) {
                                    stringMsg = activity.getResources().getString(R.string.astrologer_offline_call);
                                } else if ((AstrosageKundliApplication.selectedAstrologerDetailBean.getIsAvailableForChat().equalsIgnoreCase("false")) && (AstrosageKundliApplication.selectedAstrologerDetailBean.getIsAvailableForCall().equalsIgnoreCase("true"))) {
                                    stringMsg = activity.getResources().getString(R.string.astrologer_offline_chat);
                                } else {
                                    stringMsg = activity.getResources().getString(R.string.astrologer_offline_msg);
                                }
                                dialogMsg = stringMsg;
                            } else if (message.equalsIgnoreCase(CGlobalVariables.ASTROLOGER_STATUS_DISABLED)) {
                                dialogMsg = activity.getResources().getString(R.string.astrologer_status_disable);
                            } else if (message.equalsIgnoreCase(CGlobalVariables.CALL_API_FAILED)) {
                                dialogMsg = activity.getResources().getString(R.string.call_api_failed_msg);
                            } else if (message.equalsIgnoreCase(CGlobalVariables.USE_ANOTHER_CALL_MSG)) {
                                dialogMsg = activity.getResources().getString(R.string.existing_chat_msg);
                            } else if (message.equalsIgnoreCase(CGlobalVariables.ASTROLOGER_STATUS_UNAVAILABLE)) {
                                dialogMsg = activity.getResources().getString(R.string.astrologer_unavailable_call);
                            } else {
                                dialogMsg = activity.getResources().getString(R.string.existing_chat_msg);
                            }


                            callMsgDialogData(dialogMsg, "", true, CGlobalVariables.CHAT_CLICK);
                            if (activity instanceof ChatWindowActivity) {
                                ((ChatWindowActivity) activity).showHideCallAgainButton(false);
                            }

                        } else if (status.equals(CGlobalVariables.UNABLE_TO_CONNECT_CHAT)) {
                            CUtils.fcmAnalyticsEvents(CGlobalVariables.VIDEO_CALL_RES_STATUS_3, AstrosageKundliApplication.currentEventType, "");

                            String dialogMsg = "";
                            dialogMsg = activity.getResources().getString(R.string.unable_to_connect_chat);
                            dialogMsg = dialogMsg.replace("#", CGlobalVariables.UNABLE_TO_CONNECT_CHAT);
                            callMsgDialogData(dialogMsg, "", true, CGlobalVariables.CHAT_CLICK);
                        } else if (status.equals("100")) {
                            if (AstrosageKundliApplication.backgroundLoginCountForChat < 2) {
                                callBackgroundLoginApi(activity, TYPE_VOICE_CALL, userProfileData);
                                AstrosageKundliApplication.backgroundLoginCountForChat += 1;
                            }
                        } else {
                            //CUtils.fcmAnalyticsEvents("video_call_res_status_error", AstrosageKundliApplication.currentEventType, "");
                            Toast.makeText(activity, "Authentication issue, please try again! ( Error code: " + status + " )", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.AI_CALL_RES_STATUS_EXCEPTION, AstrosageKundliApplication.currentEventType, "");
                        //stopActivityIndicator();
                        e.printStackTrace();
                        //Log.e("SAN CI DA ", " onresponse method == CONNECT_CHAT_API_RESPONSE exp " + e.toString() );
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    stopProgressBar();
                }
            });

        }
    }

    public void initAIVoiceCallAfterResponse(JSONObject jsonObject,boolean isFreeCall) {
        try {
            AstrosageKundliApplication.IS_AGORA_CALL_RUNNING = false;

            firebaseSignInOnly(jsonObject.optString("tokenid"));

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        if(activity instanceof AIVoiceCallingActivity){
                            activity.finish();
                        }else if(activity instanceof FirstTimeProfileDetailsActivity){
                            activity.finish();
                        }else if(activity instanceof ChatWindowActivity){
                            activity.finish();
                        }
                        Intent resultIntent = new Intent(activity, AIVoiceCallingActivity.class);
                        resultIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        Bundle bundle = new Bundle();
                        bundle.putString(CGlobalVariables.AGORA_CALLS_ID, jsonObject.getString("callsid"));
                        bundle.putString(CGlobalVariables.AGORA_TOKEN, jsonObject.optString("participanttoken"));
                        //bundle.putString(CGlobalVariables.AGORA_TOKEN_ID, agoraTokenId);
                        bundle.putString(CGlobalVariables.ASTROLOGER_NAME, jsonObject.getString("astrologername"));
                        bundle.putString(CGlobalVariables.ASTROLOGER_PROFILE_URL, jsonObject.getString("astrologerimg"));
                        bundle.putString(CGlobalVariables.AGORA_CALL_DURATION, jsonObject.getString("talktime"));
                        bundle.putString(CGlobalVariables.ASTROLOGER_ID, jsonObject.getString("astrologerid"));
                        bundle.putString(CGlobalVariables.LIVEKIT_SERVER_URL, jsonObject.optString("livekitserverurl"));
                        bundle.putBoolean(CGlobalVariables.ISFREE_CONSULTATION, isFreeCall);
                        bundle.putString("connect_chat_bean", jsonObject.toString());

                        resultIntent.setPackage(BuildConfig.APPLICATION_ID);
                        resultIntent.putExtras(bundle);
                        AIVoiceCallingActivity.startEndCallScheduler(activity, jsonObject.getString("callsid"), jsonObject.getString("astrologerid"));
                        activity.startActivity(resultIntent);
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.AI_CALLING_ACTIVITY_STARTED, AstrosageKundliApplication.currentEventType, "");
                        if (activity instanceof AINotificationChatActivity){
                            activity.finish();
                        }

                    } catch (Exception e) {
                        // Log.d("testAgoraCall",e.toString());
                        e.printStackTrace();
                    }
                }
            }, 500);
        } catch (Exception e) {
            e.printStackTrace();
            // Log.d("testAgoraCall",e.toString());
        }
    }


    public void connectAIVoiceCallRandom(UserProfileData userProfileData, String apiCallingSource) {
        // Log.d("testChatNew","Connect Chat Api called");

        showProgressBar();
        if (!CUtils.isConnectedWithInternet(activity)) {
            Toast.makeText(activity, activity.getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        } else {
            CUtils.fcmAnalyticsEvents(CGlobalVariables.AI_RANDOM_AUDIO_CALL_CONNECT, AstrosageKundliApplication.currentEventType, "");

            ApiList apiList = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> apiCall = apiList.connectAICall(getAIChatParamsRandom(userProfileData, apiCallingSource));
            apiCall.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        String responses = response.body().string();
                        stopProgressBar();
                        errorLogs = errorLogs + "api response->" + responses + "\n";
                        //Log.d("testAiCAll","connectAIVoiceCall req_url ==>>>>>" + response.raw().request().url());
                        JSONObject jsonObject = new JSONObject(responses);
                        //Log.d("testAiCAll","connectAIVoiceCall Json  is ==>>>>>" + jsonObject.toString());
                        String status = jsonObject.getString("status");
                        String message = jsonObject.getString("msg");
                        if (status.equals(CGlobalVariables.CHAT_INITIATED)) {
                            CUtils.fcmAnalyticsEvents(CGlobalVariables.AI_CALL_RES_AUDIO_INITIATED, AstrosageKundliApplication.currentEventType, "");
                            initRandomAICallAfterResponse(jsonObject);
                        } else if (status.equals(CGlobalVariables.INSUFFICIENT_BALANCE)) {
                            CUtils.fcmAnalyticsEvents(CGlobalVariables.AI_CALL_RES_INSUFF_BAL, AstrosageKundliApplication.currentEventType, "");
                            //stopActivityIndicator();
                            String astrologerName = jsonObject.getString("astrologername");
                            String userbalance = jsonObject.getString("userbalance");
                            String minBalance = jsonObject.getString("minbalance");

                            InSufficientBalanceDialog dialog = new InSufficientBalanceDialog(astrologerName, userbalance, minBalance, "chat_global");
                            FragmentManager ft = ((FragmentActivity) activity).getSupportFragmentManager();
                            dialog.show(ft, "Dialog");

                        } else if (status.equals(CGlobalVariables.ASTROLOGER_BUSY_OFFLINE)) {
                            String fcm_label = CGlobalVariables.AI_CALL_API_RES_MSG + message;
                            CUtils.fcmAnalyticsEvents(fcm_label, AstrosageKundliApplication.currentEventType, "");
                            //stopActivityIndicator();
                            String dialogMsg = "";
                            if (message.equalsIgnoreCase(CGlobalVariables.USER_BLOCKED_MSG)) {
                                dialogMsg = activity.getResources().getString(R.string.user_blocked_msg);
                            } else if (message.equalsIgnoreCase(CGlobalVariables.ASTROLOGER_STATUS_BUSY_MSG)) {
                                dialogMsg = activity.getResources().getString(R.string.astrologer_busy_msg_chat);
                            } else if (message.equalsIgnoreCase(CGlobalVariables.ASTROLOGER_STATUS_OFFLINE_MSG)) {
                                String stringMsg = "";
                                if ((AstrosageKundliApplication.selectedAstrologerDetailBean.getIsAvailableForChat().equalsIgnoreCase("true")) && (AstrosageKundliApplication.selectedAstrologerDetailBean.getIsAvailableForCall().equalsIgnoreCase("false"))) {
                                    stringMsg = activity.getResources().getString(R.string.astrologer_offline_call);
                                } else if ((AstrosageKundliApplication.selectedAstrologerDetailBean.getIsAvailableForChat().equalsIgnoreCase("false")) && (AstrosageKundliApplication.selectedAstrologerDetailBean.getIsAvailableForCall().equalsIgnoreCase("true"))) {
                                    stringMsg = activity.getResources().getString(R.string.astrologer_offline_chat);
                                } else {
                                    stringMsg = activity.getResources().getString(R.string.astrologer_offline_msg);
                                }
                                dialogMsg = stringMsg;
                            } else if (message.equalsIgnoreCase(CGlobalVariables.ASTROLOGER_STATUS_DISABLED)) {
                                dialogMsg = activity.getResources().getString(R.string.astrologer_status_disable);
                            } else if (message.equalsIgnoreCase(CGlobalVariables.CALL_API_FAILED)) {
                                dialogMsg = activity.getResources().getString(R.string.call_api_failed_msg);
                            } else if (message.equalsIgnoreCase(CGlobalVariables.USE_ANOTHER_CALL_MSG)) {
                                dialogMsg = activity.getResources().getString(R.string.existing_chat_msg);
                            } else if (message.equalsIgnoreCase(CGlobalVariables.ASTROLOGER_STATUS_UNAVAILABLE)) {
                                dialogMsg = activity.getResources().getString(R.string.astrologer_unavailable_call);
                            } else {
                                dialogMsg = activity.getResources().getString(R.string.existing_chat_msg);
                            }


                            callMsgDialogData(dialogMsg, "", true, CGlobalVariables.CHAT_CLICK);
                            if (activity instanceof ChatWindowActivity) {
                                ((ChatWindowActivity) activity).showHideCallAgainButton(false);
                            }

                        } else if (status.equals(CGlobalVariables.UNABLE_TO_CONNECT_CHAT)) {
                            CUtils.fcmAnalyticsEvents(CGlobalVariables.VIDEO_CALL_RES_STATUS_3, AstrosageKundliApplication.currentEventType, "");

                            String dialogMsg = "";
                            dialogMsg = activity.getResources().getString(R.string.unable_to_connect_chat);
                            dialogMsg = dialogMsg.replace("#", CGlobalVariables.UNABLE_TO_CONNECT_CHAT);
                            callMsgDialogData(dialogMsg, "", true, CGlobalVariables.CHAT_CLICK);
                        } else if (status.equals("100")) {
                            if (AstrosageKundliApplication.backgroundLoginCountForChat < 2) {
                                callBackgroundLoginApi(activity, TYPE_AI_CALL_RANDOM, userProfileData);
                                AstrosageKundliApplication.backgroundLoginCountForChat += 1;
                            }
                        } else {
                            //CUtils.fcmAnalyticsEvents("video_call_res_status_error", AstrosageKundliApplication.currentEventType, "");
                            Toast.makeText(activity, "Authentication issue, please try again! ( Error code: " + status + " )", Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.AI_CALL_RES_STATUS_EXCEPTION, AstrosageKundliApplication.currentEventType, "");
                        //stopActivityIndicator();
                        e.printStackTrace();
                        //Log.e("SAN CI DA ", " onresponse method == CONNECT_CHAT_API_RESPONSE exp " + e.toString() );
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    stopProgressBar();
                }
            });

        }
    }

    private void initRandomAICallAfterResponse(JSONObject jsonObject) {
        try {
            JSONObject astrologerObject = jsonObject.getJSONObject("astrologer");

            AstrologerDetailBean astrologerDetailBean = new AstrologerDetailBean();
            astrologerDetailBean.setName(astrologerObject.getString("name"));
            astrologerDetailBean.setImageFile(astrologerObject.getString("imagefile"));
            astrologerDetailBean.setAstroWalletId(astrologerObject.getString("astrologerid"));
            astrologerDetailBean.setUrlText(astrologerObject.getString("urltext"));
            astrologerDetailBean.setAstrologerId(astrologerObject.getString("astrologerid"));

            astrologerDetailBean.setDesignation(astrologerObject.optString("expertise"));
            astrologerDetailBean.setAiAstrologerId(astrologerObject.optString("aiai"));
            astrologerDetailBean.setImageFileLarge(astrologerObject.optString("imagefilelarge"));

            astrologerDetailBean.setCallSource("AIFreePopUp");
            astrologerDetailBean.setFreeForChat(true);

            AstrosageKundliApplication.selectedAstrologerDetailBean = astrologerDetailBean;

            initAIVoiceCallAfterResponse(jsonObject,true);
        } catch (Exception e) {
            Log.d("TestChat", "e=========" + e);
        }

    }
}
