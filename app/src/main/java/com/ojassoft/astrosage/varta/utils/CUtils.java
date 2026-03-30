package com.ojassoft.astrosage.varta.utils;

import static android.content.Context.ACTIVITY_SERVICE;
import static android.content.Context.MODE_PRIVATE;
import static androidx.core.content.FileProvider.getUriForFile;
import static com.libojassoft.android.utils.LibCGlobalVariables.NOTIFICATION_PUSH;
import static com.ojassoft.astrosage.utils.CGlobalVariables.ENABLED;
import static com.ojassoft.astrosage.utils.CGlobalVariables.FIREBASE_FIRST_FREE_CHAT_CALL_SUCCESS_EVENT;
import static com.ojassoft.astrosage.utils.CGlobalVariables.FIREBASE_FREE_CHAT_CALL_SUCCESS_EVENT;
import static com.ojassoft.astrosage.utils.CGlobalVariables.FIREBASE_SECOND_FREE_CHAT_CALL_SUCCESS_EVENT;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GET_USER_CHAT_CATEGORY_ENABLED;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_SUCCESSFUL_FIRST_FREE_CHAT_CALL;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_SUCCESSFUL_FREE_CHAT_CALL;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_SUCCESSFUL_SECOND_FREE_CHAT_CALL;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_PASSWORD;
import static com.ojassoft.astrosage.utils.CUtils.getRandomNumber;
import static com.ojassoft.astrosage.utils.CUtils.isPurchaseEventAddedByServer;
import static com.ojassoft.astrosage.utils.CUtils.newKundliSelectedMonth;
import static com.ojassoft.astrosage.utils.CUtils.prepareUserProfile;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.CALL_CLICK;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.CHAT_CLICK;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.CONSULT_HISTORY_LIST_ID;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.CONSULT_HISTORY_TYPE;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.DEVICE_ID;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.FILTER_TYPE_CALL;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.FILTER_TYPE_CHAT;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.FREE_OFFER_TAKEN;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.INTRO_OFFER_TYPE_FREE;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.IS_APP_STARTED;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.IS_FROM_SCREEN;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_FILTER_TYPE;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_LATITUDE_PROFILE;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_LONGITUDE_PROFILE;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_SELECTION_SCRREN;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LIVE_ASTRO_API_LAST_HIT_TIME_MS;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.PLATINUM_PLAN;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.PLATINUM_PLAN_MONTHLY;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.REDUCED_PRICE_OFFER;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.REDUCE_PRICE_OFFER_TAKEN;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.content.res.ColorStateList;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.RippleDrawable;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.StrikethroughSpan;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingPeriodicWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.facebook.appevents.AppEventsConstants;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.firebase.database.ServerValue;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.libojassoft.android.models.NotificationModel;
import com.libojassoft.android.utils.AESDecryption;
import com.libojassoft.android.utils.LibCGlobalVariables;
import com.libojassoft.android.utils.LibCUtils;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.controller.ControllerManager;
import com.ojassoft.astrosage.custompushnotification.MyCloudRegistrationService;
import com.ojassoft.astrosage.misc.ServiceToGetIsAIPassSubscriber;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.GlobalRetrofitResponse;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.networkcall.RetrofitResponses;
import com.ojassoft.astrosage.ui.act.ActAppModule;
import com.ojassoft.astrosage.ui.act.AppExitActivity;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.PurchaseDhruvPlanActivity;
import com.ojassoft.astrosage.ui.act.PurchaseRechargeOfferActivity;
import com.ojassoft.astrosage.utils.indnotes.DateTimeUtils;
import com.ojassoft.astrosage.varta.dao.NotificationDBManager;
import com.ojassoft.astrosage.varta.dialog.NotificationAlertDialog;
import com.ojassoft.astrosage.varta.dialog.PremiumAIFreeChatDialog;
import com.ojassoft.astrosage.varta.dialog.RechargeSuggestionBottomSheet;
import com.ojassoft.astrosage.varta.model.AstrologerDetailBean;
import com.ojassoft.astrosage.varta.model.BeanPlace;
import com.ojassoft.astrosage.varta.model.ConnectAgoraCallBean;
import com.ojassoft.astrosage.varta.model.FilterData;
import com.ojassoft.astrosage.varta.model.FollowAstrologerModel;
import com.ojassoft.astrosage.varta.model.GiftModel;
import com.ojassoft.astrosage.varta.model.GmailAccountInfo;
import com.ojassoft.astrosage.varta.model.LiveAstrologerModel;
import com.ojassoft.astrosage.varta.model.ScheduleLiveAstroModel;
import com.ojassoft.astrosage.varta.model.TopicDetail;
import com.ojassoft.astrosage.varta.model.UserProfileData;
import com.ojassoft.astrosage.varta.model.WalletAmountBean;
import com.ojassoft.astrosage.varta.service.AINotificationWorker;
import com.ojassoft.astrosage.varta.service.AIRandomChatAstroService;
import com.ojassoft.astrosage.varta.service.AIVoiceCallingService;
import com.ojassoft.astrosage.varta.service.AstroAcceptRejectService;
import com.ojassoft.astrosage.varta.service.GetUserAIChatCategoryDataService;
import com.ojassoft.astrosage.varta.service.Loginservice;
import com.ojassoft.astrosage.varta.service.OnGoingChatService;
import com.ojassoft.astrosage.varta.service.SubscribeTopicsOnLoginService;
import com.ojassoft.astrosage.varta.twiliochat.chat.messages.ChatMessage;
import com.ojassoft.astrosage.varta.twiliochat.chat.messages.StatusMessage;
import com.ojassoft.astrosage.varta.ui.activity.AIChatWindowActivity;
import com.ojassoft.astrosage.varta.ui.activity.AIVoiceCallingActivity;
import com.ojassoft.astrosage.varta.ui.activity.AllLiveAstrologerActivity;
import com.ojassoft.astrosage.varta.ui.activity.AstrologerDescriptionActivity;
import com.ojassoft.astrosage.varta.ui.activity.ChatWindowActivity;
import com.ojassoft.astrosage.varta.ui.activity.DashBoardActivity;
import com.ojassoft.astrosage.varta.ui.activity.FeedbackActivity;
import com.ojassoft.astrosage.varta.ui.activity.FirstTimeProfileDetailsActivity;
import com.ojassoft.astrosage.varta.ui.activity.LoginSignUpActivity;
import com.ojassoft.astrosage.varta.ui.activity.MyAccountActivity;
import com.ojassoft.astrosage.varta.ui.activity.ProfileForChat;
import com.ojassoft.astrosage.varta.ui.activity.SavedKundliListActivity;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;
import com.ojassoft.astrosage.varta.volley_network.VolleyServiceHandler;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;
import com.ojassoft.astrosage.vartalive.activities.LiveActivityNew;
import com.ojassoft.astrosage.vartalive.activities.VideoCallActivity;
import com.ojassoft.astrosage.vartalive.activities.VoiceCallActivity;

import org.json.JSONArray;
import org.json.JSONObject;
import org.shadow.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.MathContext;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CUtils {

    public static String signatureKey = "";
    public static int newKundliSelectedDate;
    public static int newKverifyOtpRequestundliSelectedMonth;

    private static String servicesImgBaseUrl;
    private static MediaPlayer mChatConnectMediaPlayer;

    public static ArrayList<AstrologerDetailBean> allAstrologersArrayList = new ArrayList<>();
    public static ArrayList<AstrologerDetailBean> aiAstrologersArrayList = new ArrayList<>();
    private static ArrayList<GiftModel> giftModelArrayList;
//    static ArrayList<WalletAmountBean.Services> masterservicesArrayList;
//    private static ArrayList<WalletAmountBean.Services> servicesArrayList;
    private static WalletAmountBean myWalletAmountBean;
    private static  WalletAmountBean myMasterWalletAmountBean;

    public static WalletAmountBean getMyWalletAmountBean() {
        return myWalletAmountBean;
    }

    public static void setMyWalletAmountBean(WalletAmountBean myWalletAmountBean) {
        CUtils.myWalletAmountBean = myWalletAmountBean;
    }

    public static WalletAmountBean getMyMasterWalletAmountBean() {
        return myMasterWalletAmountBean;
    }

    public static void setMyMasterWalletAmountBean(WalletAmountBean myMasterWalletAmountBean) {
        CUtils.myMasterWalletAmountBean = myMasterWalletAmountBean;
    }

    private static String[] blockByAstrologerList;
    private static String userIntroOfferType;
    public static String userOfferAfterLogin;
    public static boolean popUnderFreeChatClicked = false;
    public static boolean popUpLoginFreeCallClicked = false;
    public static boolean popUpLoginFreeChatClicked = false;
    public static boolean isAutoConsultationConnected = false;
    public static boolean isCallChatInitFromAstroList = false;
    public static boolean isPopUpLoginShowing;
    public static int astroListFilterType = 3;
    public static int expertiseFilterType = -1;
    public static int regionalFilterType = -1;
    public static int userCustomFilterType;
    public static ArrayList<LiveAstrologerModel> localLiveList = new ArrayList<>();
    public static ArrayList<FollowAstrologerModel> followAstrologerModelArrayList;
    public static ConnectAgoraCallBean connectAgoraCallBean;
    public static boolean isFreeChat = false;
    public static boolean isPipOpenInHoroscope = false;
    public static boolean isFromAINotification = false;
    public static String AINotificationChatWindowNextQuestion = "", AINotificationChatWindowTitle = "";

    public static boolean editTextTouchFlag, keypadOpenFlag, typingFlag, backPressFlag, sendButtonFlag;
    public static StringBuilder userActionsEvents = new StringBuilder();
    public static String chatWindowOpenType = "";

    public static boolean isFreeConsultationStarted = false;

    public static void setblockByAstrologerList(String[] blockByAstrologerList) {
        CUtils.blockByAstrologerList = blockByAstrologerList;
    }

    public static void clearChatEvents() {
        try {
            editTextTouchFlag = false;
            keypadOpenFlag = false;
            typingFlag = false;
            backPressFlag = false;
            sendButtonFlag = false;

            userActionsEvents.setLength(0);
        } catch (Exception e) {
            //
        }
    }

    public static String[] getblockByAstrologerList() {
        return blockByAstrologerList;
    }

    public static String getUserIntroOfferType(Context context) {
        return getCallChatOfferType(context);
    }

    public static void setUserIntroOfferType(Context context, String userIntroOfferType) {
        //Log.d("TestOffer", "setUserIntroOfferType="+userIntroOfferType);
        try {
            CUtils.userIntroOfferType = userIntroOfferType;

            SharedPreferences sharedPreferences = context.getSharedPreferences(
                    CGlobalVariables.APP_PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
            sharedPrefEditor.putString(CGlobalVariables.PREF_CALL_CHAT_INTRO_OFFER_TYPE, userIntroOfferType);
            sharedPrefEditor.commit();
        } catch (Exception e) {
            //
        }
    }

    public static void setUserOffers(Context context, boolean liveintrooffer, String callChatOfferType) {
        //Log.d("TestOfferType", "setUserOffers="+callChatOfferType);
        CUtils.userIntroOfferType = callChatOfferType;
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                CGlobalVariables.APP_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.putBoolean(CGlobalVariables.PREF_LIVE_INTRO_OFFER, liveintrooffer);
        sharedPrefEditor.putString(CGlobalVariables.PREF_CALL_CHAT_INTRO_OFFER_TYPE, callChatOfferType);
        sharedPrefEditor.commit();

        boolean isShow5RupeeOffer = CUtils.getBooleanData(context, com.ojassoft.astrosage.utils.CGlobalVariables.IS_SHOW_FIVE_RUPEE_OFFER, false);
        if(isShow5RupeeOffer && callChatOfferType != null && callChatOfferType.contains(REDUCED_PRICE_OFFER)) {
            //get 5 rupee offer if available and store in preference
            getReducedPriceRechargeDetails(context,null);
        } else {
            CUtils.saveStringData(context, com.ojassoft.astrosage.utils.CGlobalVariables.RECHARGE_SERVICE_OFFER_RESPONSE_KEY, "");
        }
    }
    /**
     * Fetches details for a reduced price recharge offer.
     * <p>
     * This method initiates an API call to retrieve information about a special recharge offer.
     * It displays a progress dialog while the request is in progress.
     * If the API call is successful and returns valid offer details:
     * - The offer response is saved in shared preferences.
     * - The user is navigated to the {@link PurchaseRechargeOfferActivity} to proceed with the offer.
     * - The current activity (AppExitActivity) is then finished.
     * <p>
     * If the API call fails or there's no internet connection, the progress dialog is dismissed.
     * Any exceptions during JSON parsing are caught and handled silently.
     */
    public static void getReducedPriceRechargeDetails(Context context, AppExitActivity.OfferAvailableListener offerAvailableListener) {
        if (CUtils.isConnectedWithInternet(context)) {
            ApiList apiList = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> callApi  = apiList.getReducedPriceRechargeServiceDetails(CUtils.getRechargeParams(context,CUtils.getUserID(context)));
            callApi.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.body() != null) {
                        try {
                            String myResponse = response.body().string();
                            CUtils.saveStringData(context, com.ojassoft.astrosage.utils.CGlobalVariables.RECHARGE_SERVICE_OFFER_RESPONSE_KEY, myResponse);
                            if (offerAvailableListener != null)
                                offerAvailableListener.onOfferAvailable(myResponse);
                        } catch (Exception e) {
                            Log.e("fiveRupeeTest", "Exception occured" + e.getMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                }
            });
        }
    }


    //    public static void setUserUniqueReferCode(Context context,  String useruniquerefercode) {
//        SharedPreferences sharedPreferences = context.getSharedPreferences(
//                CGlobalVariables.APP_PREFS_NAME, MODE_PRIVATE);
//        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
//        sharedPrefEditor.putString(CGlobalVariables.USER_UNIQUE_REFER_CODE, useruniquerefercode);
//        sharedPrefEditor.commit();
//    }
//
//    public static String getUserUniqueReferCode(Context context) {
//        if (context == null) return "";
//        SharedPreferences sharedPreferences = context.getSharedPreferences(
//                CGlobalVariables.APP_PREFS_NAME, MODE_PRIVATE);
//        return sharedPreferences.getString(CGlobalVariables.USER_UNIQUE_REFER_CODE, "");
//    }
    public static boolean isLiveintrooffer(Context context) {
        if (context == null) return false;
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                CGlobalVariables.APP_PREFS_NAME, MODE_PRIVATE);
        return sharedPreferences.getBoolean(CGlobalVariables.PREF_LIVE_INTRO_OFFER, false);
    }

    public static String getCallChatOfferType(Context context) {
        if (context == null) return "";
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                CGlobalVariables.APP_PREFS_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(CGlobalVariables.PREF_CALL_CHAT_INTRO_OFFER_TYPE, "");
    }

    public static void setSecondFreeChat(Context context, boolean isSecondFreeChat) {
        if (context != null) {
            SharedPreferences sharedPreferences = context.getSharedPreferences(
                    CGlobalVariables.APP_PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
            sharedPrefEditor.putBoolean(CGlobalVariables.PREF_SECOND_FREE_CHAT, isSecondFreeChat);
            sharedPrefEditor.commit();
        }
    }

    public static boolean isSecondFreeChat(Context context) {
        if (context == null) return false;
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                CGlobalVariables.APP_PREFS_NAME, MODE_PRIVATE);
        return sharedPreferences.getBoolean(CGlobalVariables.PREF_SECOND_FREE_CHAT, false);
    }

    public static void setIsKundliAiProPlan(Context context, boolean isKundliAiProPlan) {
        if (context != null) {
        CUtils.saveBooleanData(context, CGlobalVariables.IS_KUNDLI_AI_PRO_PLAN, isKundliAiProPlan);
        }
    }
    public static boolean isKundliAiProPlan(Context context) {
        if (context == null) return false;
        return CUtils.getBooleanData(context, CGlobalVariables.IS_KUNDLI_AI_PRO_PLAN, false);
         }


    public static ArrayList<GiftModel> getGiftModelArrayList() {
        return giftModelArrayList;
    }

    public static void setGiftModelArrayList(ArrayList<GiftModel> giftModelArrayList) {
        if (CUtils.giftModelArrayList == null) {
            CUtils.giftModelArrayList = new ArrayList<>();
        } else {
            CUtils.giftModelArrayList.clear();
        }
        CUtils.giftModelArrayList.addAll(giftModelArrayList);
    }

    public static void setGiftImageBaseUrl(String imgUrl) {
        CUtils.servicesImgBaseUrl = imgUrl;
    }

    public static String getGiftImageBaseUrl() {
        return CUtils.servicesImgBaseUrl;
    }

    //public static boolean isNwLostConSnackBarDisplaying = false;

    public static String getApplicationSignatureHashCode(Context context) {

        if (TextUtils.isEmpty(signatureKey)) {
            try {
                Signature[] sigs = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES).signatures;
                for (Signature sig : sigs) {
                    //Log.i("Signature hashcode : " + sig.hashCode());
                    signatureKey = String.valueOf(sig.hashCode());
                    //Log.e("Key is #####"  key);
                }
            } catch (Exception e) {
            }
        }
        return "-1489918760";
        //return signatureKey;
    }

    public static void showVolleyErrorRespMessage(View view, Context context, VolleyError error, String extraData) {
        try {
            /*if (error instanceof TimeoutError || error instanceof NetworkError) {
                showSnackbar(view, context.getResources().getString(R.string.no_internet), context);
            } else if (error instanceof ParseError) {
                //
            } else {
                showSnackbar(view, context.getResources().getString(R.string.something_wrong_error)+error, context);
            }*/
            if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                showSnackbar(view, context.getResources().getString(R.string.no_internet), context);
            } else if (error instanceof AuthFailureError) {
                showSnackbar(view, context.getResources().getString(R.string.something_wrong_error) + "(" + 1 + extraData + ")", context);
            } else if (error instanceof ServerError) {
                showSnackbar(view, context.getResources().getString(R.string.something_wrong_error) + "(" + 2 + extraData + ")", context);
            } else if (error instanceof NetworkError) {
                showSnackbar(view, context.getResources().getString(R.string.something_wrong_error) + "(" + 3 + extraData + ")", context);
            } else if (error instanceof ParseError) {
                showSnackbar(view, context.getResources().getString(R.string.something_wrong_error) + "(" + 4 + extraData + ")", context);
            }

        } catch (Exception e) {
            //
        }
    }

    /**
     * Snack Bar to Show Error Messages to user
     *
     * @param view
     * @param text
     */
    public static void showSnackbar(View view, String text, Context context) {
        try {
            if (view != null) {
                Snackbar snackbar = Snackbar.make(view, text, Snackbar.LENGTH_SHORT);
                snackbar.getView().setBackgroundColor(context.getResources().getColor(R.color.colorPrimary_day_night));
                TextView tv = snackbar.getView().findViewById(R.id.snackbar_text); //snackbar_text
                tv.setTextColor(context.getResources().getColor(R.color.white));
                tv.setTextSize(16);
                FontUtils.changeFont(context, tv, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
                snackbar.show();
            }

        } catch (Exception e) {
            //
        }
    }

    public static void copyPasteText(Context context, String msg) {

        ClipboardManager cm = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
        //cm.setText(textView.getText());
        cm.setPrimaryClip(ClipData.newPlainText("", msg));
        //Log.e("SAN ", " copyPasteText " + msg );
        //Toast.makeText(context, msg, Toast.LENGTH_LONG).show();

    }


    /**
     * This function check the device is connect with internet.
     *
     * @param context
     * @return boolean
     */
    public static boolean isConnectedWithInternet(Context context) {

        boolean _isNetAvailable = false;
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo wifiNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        if (wifiNetwork != null) {
            _isNetAvailable = wifiNetwork.isConnectedOrConnecting();
        }

        NetworkInfo mobileNetwork = cm.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
        if (mobileNetwork != null) {
            _isNetAvailable = mobileNetwork.isConnectedOrConnecting();
        }

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            _isNetAvailable = activeNetwork.isConnectedOrConnecting();
        }
        return _isNetAvailable;
    }

    /**
     * save User Primary Profile In Preference
     *
     * @param userPrimaryProfile
     */
    public static void saveUserSelectedProfileInPreference(Context context, UserProfileData userPrimaryProfile) {
        SharedPreferences pSharedPref = context.getSharedPreferences(CGlobalVariables.USER_SELECTED_PROFILE_DATA, MODE_PRIVATE);
        if (pSharedPref != null) {
            Gson gson = new Gson();
            String json = gson.toJson(userPrimaryProfile);
            SharedPreferences.Editor editor = pSharedPref.edit();
            editor.remove(CGlobalVariables.USER_SELECTED_PROFILE_DATA).apply();
            editor.putString(CGlobalVariables.USER_SELECTED_PROFILE_DATA, json);
            editor.commit();
        }
    }

    /**
     * load User Profile In Preference
     *
     * @return
     */
    public static UserProfileData getUserSelectedProfileFromPreference(Context context) {
        UserProfileData outputBean = null;
        SharedPreferences pSharedPref = context.getSharedPreferences(CGlobalVariables.USER_SELECTED_PROFILE_DATA,
                MODE_PRIVATE);
        try {
            if (pSharedPref != null) {
                String jsonString = pSharedPref.getString(CGlobalVariables.USER_SELECTED_PROFILE_DATA, (new JSONObject()).toString());
                JSONObject jsonObject = new JSONObject(jsonString);
                Gson gson = new Gson();
                outputBean = new UserProfileData();
                outputBean = gson.fromJson(jsonObject.toString(), UserProfileData.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputBean;
    }

    public static void saveLoginDetailInPrefs(Context context, String userId, boolean isLogin, String walletBal, String isEligibleForBonus) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                CGlobalVariables.APP_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.putString(CGlobalVariables.APP_PREFS_User_Id,
                userId.trim());
        sharedPrefEditor
                .putBoolean(CGlobalVariables.APP_PREFS_IsLogin, isLogin);
        sharedPrefEditor.putString(CGlobalVariables.APP_PREFS_WALLET,
                walletBal.trim());
        sharedPrefEditor.putString(CGlobalVariables.APP_PREFS_IS_ELIGIBLE_FOR_BONUS,
                isEligibleForBonus.trim());
        sharedPrefEditor.commit();
    }


    public static void saveAstrologerIDAndChannelID(Context context, String astrologerID, String channelID) {
        //Log.d("saveAstrologerID",astrologerID+ channelID+context);
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                CGlobalVariables.APP_PREFS_ASTRO_CHANNEL, MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.putString(CGlobalVariables.APP_PREFS_ASTROLOGER_ID,
                astrologerID.trim());
        sharedPrefEditor
                .putString(CGlobalVariables.APP_PREFS_CHANNEL_ID, channelID.trim());
        sharedPrefEditor.commit();
    }

    public static String getSelectedAstrologerID(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                CGlobalVariables.APP_PREFS_ASTRO_CHANNEL, MODE_PRIVATE);
        return sharedPreferences.getString(CGlobalVariables.APP_PREFS_ASTROLOGER_ID,
                "");
    }

    public static String getSelectedChannelID(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                CGlobalVariables.APP_PREFS_ASTRO_CHANNEL, MODE_PRIVATE);
        return sharedPreferences.getString(CGlobalVariables.APP_PREFS_CHANNEL_ID,
                "");
    }

    public static String getCountryCode(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                CGlobalVariables.APP_PREFS_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(CGlobalVariables.PREF_COUNTRY_CODE,
                "91");
    }

    public static void setCountryCode(Context context, String walletBal) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                CGlobalVariables.APP_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.putString(CGlobalVariables.PREF_COUNTRY_CODE,
                walletBal.trim());
        sharedPrefEditor.commit();
    }

    public static String getUserIdForBlock(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                CGlobalVariables.APP_PREFS_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(CGlobalVariables.PREF_USER_ID,
                "");
    }

    public static void setUserIdForBlock(Context context, String userId) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                CGlobalVariables.APP_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.putString(CGlobalVariables.PREF_USER_ID,
                userId.trim());
        sharedPrefEditor.commit();
    }


    public static String getUserLoginPassword(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                CGlobalVariables.APP_PREFS_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(CGlobalVariables.PREF_USER_PASSWORD,
                "");
    }

    public static void setUserLoginPassword(Context context, String password) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                CGlobalVariables.APP_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.putString(CGlobalVariables.PREF_USER_PASSWORD,
                password.trim());
        sharedPrefEditor.commit();
    }

    public static void setWalletRs(Context context, String walletBal) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                CGlobalVariables.APP_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.putString(CGlobalVariables.APP_PREFS_WALLET,
                walletBal.trim());
        sharedPrefEditor.commit();
    }

    public static String getWalletRs(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                CGlobalVariables.APP_PREFS_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(CGlobalVariables.APP_PREFS_WALLET,
                "0.0");
    }

    public static void setWalletPaidAmt(Context context, String paidAmt) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                CGlobalVariables.APP_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.putString(CGlobalVariables.KEY_WALLET_PAID_AMT,
                paidAmt.trim());
        sharedPrefEditor.commit();
    }
    public static String getWalletPaidAmt(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                CGlobalVariables.APP_PREFS_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(CGlobalVariables.KEY_WALLET_PAID_AMT,
                "0.0");
    }
    public static void setWalletFreeAmt(Context context, String paidAmt) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                CGlobalVariables.APP_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.putString(CGlobalVariables.KEY_WALLET_FREE_AMT,
                paidAmt.trim());
        sharedPrefEditor.commit();
    }
    public static String getWalletFreeAmt(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                CGlobalVariables.APP_PREFS_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(CGlobalVariables.KEY_WALLET_FREE_AMT,
                "0.0");
    }


    public static String getUserID(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                CGlobalVariables.APP_PREFS_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(CGlobalVariables.APP_PREFS_User_Id,
                "");
    }

    public static boolean getUserLoginStatus(Context context) {
        if (context == null) return false;
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                CGlobalVariables.APP_PREFS_NAME, MODE_PRIVATE);
        return sharedPreferences.getBoolean(CGlobalVariables.APP_PREFS_IsLogin,
                false);
    }

    public static String getFirstFreeOfferTakenDate(Context context) {
        if (context == null) return "";
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                CGlobalVariables.APP_PREFS_OFFER, MODE_PRIVATE);
        return sharedPreferences.getString(CGlobalVariables.FIRST_FREE_CHAT_DATE,
                "");
    }

    public static void saveFirstFreeOfferTakenDate(Context context, String freechatDate) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                CGlobalVariables.APP_PREFS_OFFER, MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.putString(CGlobalVariables.FIRST_FREE_CHAT_DATE, freechatDate);
        sharedPrefEditor.commit();
    }

    public static String getReducePriceOfferTakenDate(Context context) {
        if (context == null) return "";
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                CGlobalVariables.APP_PREFS_OFFER, MODE_PRIVATE);
        return sharedPreferences.getString(CGlobalVariables.FIRST_REDUCE_PRICE_CHAT_DATE,
                "");
    }

    public static void saveReducePriceOfferTakenDate(Context context, String freechatDate) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                CGlobalVariables.APP_PREFS_OFFER, MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.putString(CGlobalVariables.FIRST_REDUCE_PRICE_CHAT_DATE, freechatDate);
        sharedPrefEditor.commit();
    }

    public static String getUserDisplayName(Context ctx) {
        String userName = "Guest" + getFourDigitRandomNumber(ctx);

        if (!getUserLoginStatus(ctx)) {
            return userName;
        }
        userName = "";
        UserProfileData userProfileData = CUtils.getUserSelectedProfileFromPreference(ctx);
        if (userProfileData != null) {

            userName = userProfileData.getName();

            if (!TextUtils.isEmpty(userName)) {
                if (TextUtils.isDigitsOnly(userName) && userName.length() >= 8) { //i.e. mobile number
                    // userName = "XXXXX" + userName.substring(5);
                    // Example: 8700327485 → 87003XXXXX (last 5 digits masked)
                    int visibleLength = userName.length() - 5;
                    userName = userName.substring(0, visibleLength) + "XXXXX";
                } else {
                    userName = userName.split(" ")[0];
                }

            }

        }
        if (TextUtils.isEmpty(userName)) {
            String phoneNumber = getUserID(ctx);
          /*  if (!TextUtils.isEmpty(phoneNumber)) {
                phoneNumber = "XXXXX" + phoneNumber.substring(5);
                userName = phoneNumber;
            }*/
            if (!TextUtils.isEmpty(phoneNumber) && phoneNumber.length() >= 5) {
                // Keep the first digits, mask the last 5 with "XXXXX"
                //phoneNumber = "XXXXX" + phoneNumber.substring(5);

                // Example: 8700327485 → 87003XXXXX
                // Keep the last digits, mask the last 5 with "XXXXX"
                if (phoneNumber.length() >= 5) {
                    phoneNumber = phoneNumber.substring(0, phoneNumber.length() - 5) + "XXXXX";
                }
                userName = phoneNumber;


            }
        }

        if (TextUtils.isEmpty(userName)) {
            userName = "Guest" + getFourDigitRandomNumber(ctx);
        }
        /* Remove Astrosage keyword */
        userName = userName.replaceAll("  ", " ");
        if (StringUtils.containsIgnoreCase(userName, "Astrosage")) {
            userName = userName.replaceAll("(?i)" + "Astrosage", "Axxxxxxxx");
        } else if (StringUtils.containsIgnoreCase(userName, "Astro sage")) {
            userName = userName.replaceAll("(?i)" + "Astro sage", "Axxxxxxxxx");
        } else if (isContain(userName, CGlobalVariables.astosageKeyword)) {
            userName = CUtils.removeAstrosageFromRegional(userName);
        }

        return userName;
    }

    public static String capitalize(String str) {
        if (str == null || str.isEmpty()) return str;
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }

    public static String getUserFullName(Context ctx) {
        String gustName = "Guest" + getFourDigitRandomNumber(ctx);

        if (!getUserLoginStatus(ctx)) {
            return gustName;
        }
        String userName = "";
        UserProfileData userProfileData = CUtils.getUserSelectedProfileFromPreference(ctx);
        if (userProfileData != null) {
            userName = userProfileData.getName();
            if (TextUtils.isDigitsOnly(userName) && userName.length() >= 8) { //i.e. mobile number
                //return  "XXXXX" + userName.substring(5);

                int visibleLength = userName.length() - 5;
                String masked = userName.substring(0, visibleLength) + "XXXXX";

                return masked;
            } else {
                String[] name = userName.toLowerCase().split(" ");
                StringBuilder str = new StringBuilder();
                for (String letter : name) {
                    Log.d("User naame ", letter);
                    str.append(capitalize(letter)).append(" ");
                }
                Log.d("User naame ", str.toString());
                return str.toString().trim();
            }
        }

        return gustName;
    }

    private static boolean isContain(String inputString, String[] items) {
        String lowerCase = inputString.toLowerCase();
        boolean found = false;
        for (String item : items) {
            String pattern = "\\b" + item + "\\b";
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(lowerCase);
            if (m.find()) {
                found = true;
                break;
            }

        }
        return found;
    }

    public static String removeAstrosageFromRegional(String username) {

        char strTmp;
        if (username.contains(CGlobalVariables.astosageKeyword[0])) {
            strTmp = CGlobalVariables.astosageKeyword[0].charAt(0);
            username = username.replaceAll(CGlobalVariables.astosageKeyword[0], strTmp + "xxxxxxxxx");
        } else if (username.contains(CGlobalVariables.astosageKeyword[1])) {
            strTmp = CGlobalVariables.astosageKeyword[1].charAt(0);
            username = username.replaceAll(CGlobalVariables.astosageKeyword[1], strTmp + "xxxxxxxxx");
        } else if (username.contains(CGlobalVariables.astosageKeyword[2])) {
            strTmp = CGlobalVariables.astosageKeyword[2].charAt(0);
            username = username.replaceAll(CGlobalVariables.astosageKeyword[2], strTmp + "xxxxxxxxx");
        } else if (username.contains(CGlobalVariables.astosageKeyword[3])) {
            strTmp = CGlobalVariables.astosageKeyword[3].charAt(0);
            username = username.replaceAll(CGlobalVariables.astosageKeyword[3], strTmp + "xxxxxxxxx");
        } else if (username.contains(CGlobalVariables.astosageKeyword[4])) {
            strTmp = CGlobalVariables.astosageKeyword[4].charAt(0);
            username = username.replaceAll(CGlobalVariables.astosageKeyword[4], strTmp + "xxxxxxxxx");
        } else if (username.contains(CGlobalVariables.astosageKeyword[5])) {
            strTmp = CGlobalVariables.astosageKeyword[5].charAt(0);
            username = username.replaceAll(CGlobalVariables.astosageKeyword[5], strTmp + "xxxxxxxxx");
        } else if (username.contains(CGlobalVariables.astosageKeyword[6])) {
            strTmp = CGlobalVariables.astosageKeyword[6].charAt(0);
            username = username.replaceAll(CGlobalVariables.astosageKeyword[6], strTmp + "xxxxxxxxx");
        } else if (username.contains(CGlobalVariables.astosageKeyword[7])) {
            strTmp = CGlobalVariables.astosageKeyword[7].charAt(0);
            username = username.replaceAll(CGlobalVariables.astosageKeyword[7], strTmp + "xxxxxxxxx");
        } else if (username.contains(CGlobalVariables.astosageKeyword[8])) {
            strTmp = CGlobalVariables.astosageKeyword[8].charAt(0);
            username = username.replaceAll(CGlobalVariables.astosageKeyword[8], strTmp + "xxxxxxxxx");
        } else if (username.contains(CGlobalVariables.astosageKeyword[9])) {
            strTmp = CGlobalVariables.astosageKeyword[9].charAt(0);
            username = username.replaceAll(CGlobalVariables.astosageKeyword[9], strTmp + "xxxxxxxxx");
        } else if (username.contains(CGlobalVariables.astosageKeyword[10])) {
            strTmp = CGlobalVariables.astosageKeyword[10].charAt(0);
            username = username.replaceAll(CGlobalVariables.astosageKeyword[10], strTmp + "xxxxxxxxx");
        }

        return username;
    }

    public static String getFourDigitRandomNumber(Context context) {
        try {
            String deviceId = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
            return deviceId.substring(deviceId.length() - 4);
        } catch (Exception e) {
            //
        }
        return "";
    }

    public static String getActivityName(Context context) {
        return context.getClass().getSimpleName();
    }

    public static void saveBooleanData(Context context, String key,
                                       boolean value) {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putBoolean(key, value);
        editor.commit();

    }

    public static boolean getBooleanData(Context context, String key,
                                         boolean defaultValue) {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(context);

        boolean value = preferences.getBoolean(key, defaultValue);

        return value;
    }

    public static String getPrefNextOffer(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                CGlobalVariables.APP_PREFS_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(CGlobalVariables.PREF_NEXT_OFFER,
                "");
    }

    public static void setPrefNextOffer(Context context, String date) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                CGlobalVariables.APP_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.putString(CGlobalVariables.PREF_NEXT_OFFER,
                date);
        sharedPrefEditor.commit();
    }

    public static boolean isSameDay(String date1, String date2) {
        try {
            Date dateOne = new SimpleDateFormat("yyyyMMdd").parse(date1);
            Date dateTwo = new SimpleDateFormat("yyyyMMdd").parse(date2);
            SimpleDateFormat fmt = new SimpleDateFormat("yyyyMMdd");
            return fmt.format(dateOne).equals(fmt.format(dateTwo));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static String getCurrentDateInString() {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    /**
     * this function send e-mail to friend from navigation deawer
     *
     * @param context
     */
    public static void shareWithFriends(Context context, String shareText) {

        //CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_KUNDLI_SHARE_APP_WITH_FRIENDS, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

        /*
         * CharSequence body2 = Html.fromHtml(new
         * StringBuilder().append(CGlobalVariables
         * .shareToFriendBody).toString()); String subject = body2.toString();
         */
//        CharSequence body2 = context.getResources().getString(
//                R.string.shareAppBody);
        // String subject = body2.toString();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        // intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, shareText);
        try {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

            context.startActivity(Intent.createChooser(intent, context.getResources().getString(
                    R.string.share_app)));
        } catch (android.content.ActivityNotFoundException ex) {
            // //Log.e("Exception", ex.getMessage());
        }
    }

    public static String getMyAndroidId(Context context) {
        String android_id = "-1";
        try {
            android_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {

        }
        return android_id;
    }

    /**
     * convert Time To Am Pm
     *
     * @param time
     * @return
     */
    public static String convertTimeToAmPm(String time) {

        int hr = 0, min = 0;
        String TimeWithMeradian = "";
        String[] SplitTime = time.split(":");
        try {
            hr = Integer.parseInt(SplitTime[0]);
            min = Integer.parseInt(SplitTime[1]);

            //am
            if (hr < 12) {
                TimeWithMeradian = appendZeroOnSingleDigit(hr) + ":" + appendZeroOnSingleDigit(min) + " AM";
            } else if (hr >= 12 && hr < 24) {   //pm
                hr = hr == 12 ? 12 : hr - 12;
                TimeWithMeradian = appendZeroOnSingleDigit(hr) + ":" + appendZeroOnSingleDigit(min) + " PM";
            } else if (hr >= 24) {  // more than 24
                hr = hr - 24;
                TimeWithMeradian = "+" + appendZeroOnSingleDigit(hr) + ":" + appendZeroOnSingleDigit(min) + " AM";
            }
        } catch (Exception e) {
            TimeWithMeradian = time;
        }
        return TimeWithMeradian;
    }

    /**
     * append Zero On Single Digit
     *
     * @param time
     * @return
     */
    public static String appendZeroOnSingleDigit(int time) {
        String rTime = String.valueOf(time);
        if (time < 10 /*&& time != 0*/) {
            rTime = "0" + time;
        }
        return rTime;
    }

    /*
    // SAN Commented to use make city default value use
    public static BeanPlace getUserDefaultCity(Activity act) {
        BeanPlace place = new BeanPlace();

        place.setCityId(-1);
        place.setCityName("Agra");
        place.setState("");
        place.setCountryName("");
        place.setLatDeg("027");
        place.setLatMin("09");
        place.setLatDir("N");

        place.setLongDeg("078");
        place.setLongMin("00");
        place.setLongDir("E");

        place.setTimeZoneName("+5.5 IST");
        place.setTimeZoneValue(Float.valueOf("5.5f"));
        place.setTimeZoneId(Integer.valueOf("32"));

        // tejinder
        place.setTimeZoneString("Asia/Kolkata");
        //Log.e("###" + settings.getString("TimeZoneString", "Asia/Kolkata"));
        return place;
    }*/

    /**
     * This is a function used to hide soft keyboard in any activity
     *
     * @param activity
     */
    public static void hideMyKeyboard(Activity activity) {
        try {
            InputMethodManager inputManager = (InputMethodManager) activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(activity.getCurrentFocus()
                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        } catch (Exception e) {
            // //Log.e("CUtils-Fun-HideMyKeyboard", e.getMessage());
        }
    }

    /**
     * This function is used to prerape place object from passed bundle and
     * return the object
     *
     * @param bundle
     * @return Place
     * @author Bijendra
     * @date 27-nov-2012
     */
    public static BeanPlace getPlaceObjectFromBundle(Bundle bundle) {
        BeanPlace place = new BeanPlace();

        place.setCityId(bundle.getInt("CITY_ID"));

        place.setTimeZoneId(bundle.getInt("TIMEZONE_ID"));

        place.setCityName(bundle.getString("CITY_NAME"));


        place.setLongDeg(bundle.getString("LONG_DEG"));
        place.setLongMin(bundle.getString("LONG_MIN"));
        place.setLongSec(bundle.getString("LONG_SEC"));
        place.setLongDir(bundle.getString("LONG_DIR"));

        place.setLatDeg(bundle.getString("LAT_DEG"));
        place.setLatMin(bundle.getString("LAT_MIN"));
        place.setLatSec(bundle.getString("LAT_SEC"));
        place.setLatDir(bundle.getString("LAT_DIR"));

        place.setTimeZoneName(bundle.getString("TIMEZONE_NAME"));
        place.setTimeZoneValue(Float.valueOf(bundle.getString("TIMEZONE_VALUE")));

        place.setLatitude(bundle.getString("LATITUDE"));
        place.setLongitude(bundle.getString("LONGITUDE"));
        place.setTimeZone(bundle.getString("TIMEZONE"));
        /*
         * bundle.putString("TIMEZONESTRING",
         * String.valueOf(place.getTimeZoneString()));
         * bundle.putString("STATE_NAME", place.getState());
         */
        place.setTimeZoneString(bundle.getString("TIMEZONESTRING"));
        place.setCountryName(bundle.getString("COUNTRY_NAME"));
        place.setState(bundle.getString("STATE_NAME"));
        place.setCountryId(bundle.getInt("COUNTRY_ID"));

        return place;
    }

    public static void saveCityAsDefaultCity(Activity act, BeanPlace objTempCPlace) {
        SharedPreferences settings = act.getSharedPreferences(CGlobalVariables.APP_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("CityId", objTempCPlace.getCityId());
        editor.putString("CityName", objTempCPlace.getCityName());
        editor.putString("StateName", objTempCPlace.getState());
        editor.putString("CountryName", objTempCPlace.getCountryName());
        editor.putString("LatDeg", objTempCPlace.getLatDeg());
        editor.putString("LatMin", objTempCPlace.getLatMin());
        editor.putString("LongDeg", objTempCPlace.getLongDeg());
        editor.putString("LongMin", objTempCPlace.getLongMin());
        editor.putString("LatDir", objTempCPlace.getLatDir());
        editor.putString("LongDir", objTempCPlace.getLongDir());
        editor.putString("TimeZoneName", objTempCPlace.getTimeZoneName());
        editor.putString("TimeZoneValue", String.valueOf(objTempCPlace.getTimeZoneValue()));
        editor.putString("TimeZoneID", String.valueOf(objTempCPlace.getTimeZoneId()));
        // tejinder
        editor.putString("TimeZoneString", String.valueOf(objTempCPlace.getTimeZoneString()));
        editor.commit();
    }

    // SAN create to use make city default value
    public static BeanPlace getUserDefaultCity(Activity act) {
        BeanPlace place = new BeanPlace();
        //Log.e("SAN ", " VARTA CUtils getUserDefaultCity inside " );
        SharedPreferences settings = act.getSharedPreferences(CGlobalVariables.APP_PREFS_NAME, MODE_PRIVATE);

        place.setCityId(settings.getInt("CityId", -1));
        place.setCityName(settings.getString("CityName", "Agra"));
        place.setState(settings.getString("StateName", ""));
        place.setCountryName(settings.getString("CountryName", ""));
        place.setLatDeg(settings.getString("LatDeg", "027"));
        place.setLatMin(settings.getString("LatMin", "09"));
        place.setLatDir(settings.getString("LatDir", "N"));
        place.setLongDeg(settings.getString("LongDeg", "078"));
        place.setLongMin(settings.getString("LongMin", "00"));
        place.setLongDir(settings.getString("LongDir", "E"));
        place.setTimeZoneName(settings.getString("TimeZoneName", "+5.5 IST"));
        place.setTimeZoneValue(Float.valueOf(settings.getString("TimeZoneValue", "5.5f")));
        place.setTimeZoneId(Integer.valueOf(settings.getString("TimeZoneID", "32")));
        // tejinder
        place.setTimeZoneString(settings.getString("TimeZoneString", "Asia/Kolkata"));
        return place;
    }

    /**
     * This function is used to prepare bundle of place object and retunr it
     *
     * @param place
     * @return Bundle
     * @author Bijendra
     * @date 27-nov-2012
     */
    public static Bundle getBundleOfPlaceValues(BeanPlace place) {
        Bundle bundle = new Bundle();

        bundle.putInt("CITY_ID", place.getCityId());
        bundle.putInt("COUNTRY_ID", place.getCountryId());
        bundle.putInt("TIMEZONE_ID", place.getTimeZoneId());
        bundle.putString("CITY_NAME", place.getCityName());
        bundle.putString("COUNTRY_NAME", place.getCountryName());

        bundle.putString("LONG_DEG", place.getLongDeg());
        bundle.putString("LONG_MIN", place.getLongMin());
        bundle.putString("LONG_SEC", place.getLongSec());
        bundle.putString("LONG_DIR", place.getLongDir());

        bundle.putString("LAT_DEG", place.getLatDeg());
        bundle.putString("LAT_MIN", place.getLatMin());
        bundle.putString("LAT_SEC", place.getLatSec());
        bundle.putString("LAT_DIR", place.getLatDir());

        bundle.putString("TIMEZONE_NAME", place.getTimeZoneName());
        bundle.putString("TIMEZONE_VALUE", String.valueOf(place.getTimeZoneValue()));

        bundle.putString("LATITUDE", String.valueOf(place.getLatitude()));
        bundle.putString("LONGITUDE", String.valueOf(place.getLongitude()));
        bundle.putString("TIMEZONE", String.valueOf(place.getTimeZone()));
        bundle.putString("TIMEZONESTRING", String.valueOf(place.getTimeZoneString()));
        bundle.putString("STATE_NAME", place.getState());
        //bundle.putBoolean("isDafultCity", place.isDefaultCity());

        return bundle;
    }

    public static BeanPlace getLatLongAndTimeZone(BeanPlace beanPlace) {

        try {
            String latitude = "0";
            String longitude = "0";

            float longDeg = Float.parseFloat(beanPlace.getLongDeg());
            float longMin = Float.parseFloat(beanPlace.getLongMin());
            String longDir = beanPlace.getLongDir();

            float latDeg = Float.parseFloat(beanPlace.getLatDeg());
            float latMin = Float.parseFloat(beanPlace.getLatMin());
            String latDir = beanPlace.getLatDir();

            float latMinActual = latMin / 60;
            float longMinActual = longMin / 60;

            String latDirSign = "";
            String longDirSign = "";

            if (latDir.endsWith("S")) {
                latDirSign = "-";
            }
            if (longDir.endsWith("W")) {
                longDirSign = "-";
            }

            float _lat = latDeg + latMinActual;
            float _long = longDeg + longMinActual;

            latitude = latDirSign + _lat;
            longitude = longDirSign + _long;

            beanPlace.setLatitude(latitude);
            beanPlace.setLongitude(longitude);
            beanPlace.setTimeZone(String.valueOf(beanPlace.getTimeZoneValue()));
            if (beanPlace.getTimeZone() == null) {
                beanPlace.setTimeZoneString("");
            }
        } catch (Exception ex) {

            beanPlace.setLatitude("0");
            beanPlace.setLongitude("0");
            beanPlace.setTimeZone("0");
            if (beanPlace.getTimeZone() == null) {
                beanPlace.setTimeZoneString("");
            }
        }

        return beanPlace;

    }

    public static double getLatLngFromDMS(double degrees, double minutes) {

        double data = degrees + (minutes / 60.0);
        return data;
    }

    public static boolean isLocationPermissionGranted(final Activity context, final Object object, final int permission_Id) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (context.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                //Log.v("Permission is granted");
                return true;
            } else {
                if (getBooleanData(context, CGlobalVariables.PERMISSION_KEY_LOCATION, false)) {

                    String permission = context.getResources().getString(R.string.permission_msg_location_settings);
                    showMessageOKCancel(permission, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            openSetting(context, permission_Id);
                        }
                    }, context, permission_Id);

                } else {
                    if (object instanceof Activity) {
                        if (ActivityCompat.shouldShowRequestPermissionRationale(context,
                                android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                            String permission = context.getResources().getString(R.string.permission_msg_location);
                            showMessageOKCancel(permission,
                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            requestForLocationPermission(context, object, permission_Id);
                                        }
                                    }, context, permission_Id);

                        } else {
                            requestForLocationPermission(context, object, permission_Id);
                        }
                    } else if (object instanceof Fragment) {
                        if (((Fragment) object).shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_FINE_LOCATION)) {
                            String permission = context.getResources().getString(R.string.permission_msg_location);
                            showMessageOKCancel(permission,

                                    new View.OnClickListener() {
                                        @Override
                                        public void onClick(View view) {
                                            requestForLocationPermission(context, object, permission_Id);
                                        }
                                    }, context, permission_Id);

                        } else {
                            requestForLocationPermission(context, object, permission_Id);
                        }
                    }
                }
                return false;
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            //Log.v("Permission is granted");
            return true;
        }
    }

    private static void requestForLocationPermission(final Activity context, final Object object, final int permission_Id) {
        try {
            //Log.v("Permission is revoked");
            if (object instanceof Activity)
                ActivityCompat.requestPermissions(context, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, permission_Id);
            else if (object instanceof Fragment)
                ((Fragment) object).requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, permission_Id);
        } catch (Exception ex) {
            //Log.e(ex.getMessage());
        }
    }

    private static void showMessageOKCancel(String message, final View.OnClickListener onClickListener, final Activity activity, final int permissionId) {
        try {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(activity);
            LayoutInflater inflater = activity.getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.lay_permission_dialog, null);
            dialogBuilder.setView(dialogView);
            dialogBuilder.setCancelable(false);

            TextView tvContent = dialogView.findViewById(R.id.tvContent);
            Button btnDeny = dialogView.findViewById(R.id.btnDeny);
            final Button btnAllow = dialogView.findViewById(R.id.btnAllow);

            ImageView imageView = dialogView.findViewById(R.id.imageView);
            if (permissionId == CGlobalVariables.PERMISSION_EXTERNAL_STORAGE) {
                imageView.setImageResource(R.drawable.ic_folder_orange);
            } else if (permissionId == CGlobalVariables.PERMISSION_CONTACTS) {
                imageView.setImageResource(R.drawable.ic_email_orange);
            } else if (permissionId == CGlobalVariables.PERMISSION_LOCATION) {
                imageView.setImageResource(R.drawable.ic_place_orange);
            }

            tvContent.setText(message);

            final AlertDialog alertDialog = dialogBuilder.create();

            btnDeny.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (alertDialog != null) {
                        alertDialog.dismiss();
                    }
                }
            });

            btnAllow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    btnAllow.setOnClickListener(null);
                    btnAllow.setOnClickListener(onClickListener);
                    if (alertDialog != null) {
                        alertDialog.dismiss();
                    }
                    btnAllow.performClick();
                }
            });

            alertDialog.show();
        } catch (Exception ex) {
            //Log.e(ex.getMessage());
        }

    }

    public static void openSetting(Activity ctx, int code) {
        if (ctx == null) return;
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", ctx.getPackageName(), null);
        intent.setData(uri);
        ctx.startActivityForResult(intent, code);
    }

    public static int getNewKundliSelectedDate() {
        return newKundliSelectedDate;
    }

    public static void setNewKundliSelectedDate(int newKundliSelectedDate) {
        CUtils.newKundliSelectedDate = newKundliSelectedDate;
    }

    public static int getNewKundliSelectedMonth() {
        return newKundliSelectedMonth;
    }

    public static void setNewKundliSelectedMonth(int newKundliSelectedMonth) {
        newKundliSelectedMonth = newKundliSelectedMonth;
    }

    public static void vollyGetRequest(VolleyResponse volleyResponse, String url, int method) {
        if (AstrosageKundliApplication.getAppContext() == null) return;
        HashMap<String, String> headerParams = new HashMap();
        headerParams.put("X-Android-Package", AstrosageKundliApplication.getAppContext().getPackageName());
        headerParams.put("X-Android-Cert", CUtils.getKeyHash(AstrosageKundliApplication.getAppContext()));


        HashMap<String, String> params = new HashMap<String, String>();
        RequestQueue queue = VolleySingleton.getInstance(AstrosageKundliApplication.getAppContext()).getRequestQueue();
        StringRequest stringRequest = new VolleyServiceHandler(Request.Method.GET, url, volleyResponse, true, params, headerParams, method).getMyStringRequest();
        queue.add(stringRequest);

    }

    /**
     * This method give sha1 keyhash
     *
     * @param ctx
     * @return
     */
    public static String getKeyHash(Context ctx) {
        PackageInfo info;
        try {
            info = ctx.getPackageManager().getPackageInfo(BuildConfig.APPLICATION_ID, PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA-1");
                md.update(signature.toByteArray());
                byte[] sha1hash = md.digest();

                String sha1 = convertToHex(sha1hash);
                return sha1;

            }
        } catch (PackageManager.NameNotFoundException e1) {
            //Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            //Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            //Log.e("exception", e.toString());
        }
        return "";
    }

    /**
     * This method convert byte[] to hexadecimal string
     *
     * @param data
     * @return
     */
    private static String convertToHex(byte[] data) {
        StringBuilder buf = new StringBuilder();
        for (byte b : data) {
            int halfbyte = (b >>> 4) & 0x0F;
            int two_halfs = 0;
            do {
                buf.append((0 <= halfbyte) && (halfbyte <= 9) ? (char) ('0' + halfbyte) : (char) ('a' + (halfbyte - 10)));
                halfbyte = b & 0x0F;
            } while (two_halfs++ < 1);

        }
        return buf.toString();
    }

    /**
     * convert Time To Am Pm
     *
     * @param time
     * @return
     */
    public static String convertTimeToHrMtScAmPm(String time) {

        int hr = 0, min = 0, sec = 0;
        String TimeWithMeradian = "";
        String[] SplitTime = time.split(":");
        try {
            hr = Integer.parseInt(SplitTime[0]);
            min = Integer.parseInt(SplitTime[1]);
            sec = Integer.parseInt(SplitTime[2]);

            //am
            if (hr < 12) {
                TimeWithMeradian = appendZeroOnSingleDigit(hr) + ":" + appendZeroOnSingleDigit(min) + ":" + appendZeroOnSingleDigit(sec) + " AM";
            } else if (hr >= 12 && hr < 24) {   //pm
                hr = hr == 12 ? 12 : hr - 12;
                TimeWithMeradian = appendZeroOnSingleDigit(hr) + ":" + appendZeroOnSingleDigit(min) + ":" + appendZeroOnSingleDigit(sec) + " PM";
            } else if (hr >= 24) {  // more than 24
                hr = hr - 24;
                TimeWithMeradian = "+" + appendZeroOnSingleDigit(hr) + ":" + appendZeroOnSingleDigit(min) + ":" + appendZeroOnSingleDigit(sec) + " AM";
            }
        } catch (Exception e) {
            TimeWithMeradian = time;
        }
        return TimeWithMeradian;
    }

    /**
     * This function append 0 in the passed int value and return new value as a
     * string.
     *
     * @param c
     * @return String
     */
    public static String pad(int c) {
        if (c >= 10)
            return String.valueOf(c);
        else
            return "0" + c;
    }

    /**
     * This method check the special symbol exist or not
     *
     * @param name
     * @return true if special symbol exist otherwise false
     */

    public static boolean isSpecialCharFound(String name) {
        Pattern regex = Pattern.compile("[$&+~,:;=\\\\?@#|/'<>.^*()%!-]");

        return regex.matcher(name).find();
    }

    public static String covertDateFormate(String dateStr) {
        String newDate = null;
        try {
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat sdf2 = new SimpleDateFormat("MMMM d, yyyy h:mm a");
            Date date = sdf1.parse(dateStr);
            newDate = sdf2.format(date);
            if (newDate.contains("am")) {
                newDate = newDate.replace("am", "AM");
            }
            if (newDate.contains("pm")) {
                newDate = newDate.replace("pm", "PM");
            }
        } catch (Exception e) {

        }
        return newDate;
    }

    public static String covertDateFormat(String dateStr) {
        String newDate = null;
        try {
            SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            SimpleDateFormat sdf2 = new SimpleDateFormat("dd-MM-yyyy");
            Date date = sdf1.parse(dateStr);
            newDate = sdf2.format(date);

        } catch (Exception e) {

        }
        return newDate;
    }

    /* Added by Monika
     * Firebase Analytics
     */
    public static void fcmAnalyticsEvents(String label,
                                          String event, String screenName) {
        Bundle bundle = new Bundle();
        bundle.putString(CGlobalVariables.FIREBASE_LABEL_PARAMS, replaceSpaceDashFromLabelFcmEventName(label));
        FirebaseAnalytics.getInstance(AstrosageKundliApplication.getAppContext()).logEvent(event, bundle);

        //Log.e("FcmAnalyticslabel ", AstrosageKundliApplication.getAppContext()+"");
        //Log.e("Fcm Analytics event ", event);
        //Log.e("Fcm Analytics label ", replaceSpaceDashFromLabelFcmEventName(label));
        //Log.e("Fcm Analytics ", "************");
    }

    /* Added by Abhishek
     * Facebook Analytics
     */
    public static void facebookAnalyticsEvents(String label, String event) {
        try {
            AppEventsLogger logger = AppEventsLogger.newLogger(AstrosageKundliApplication.getAppContext());
            Bundle params = new Bundle();
            params.putString(AppEventsConstants.EVENT_PARAM_LEVEL, replaceSpaceDashFromLabelFcmEventName(label));
            logger.logEvent(event, params);
        } catch (Exception e) {
            //
        }
    }


    /* Added by Monika
     * replace the space and dash with underscore
     * return string(fcm event name) in small case letter
     */
    public static String replaceSpaceDashFromLabelFcmEventName(String label) {
        String upLabel = label.trim();

        if (upLabel.contains(" ")) {
            upLabel = upLabel.replace(" ", "_");
        }

        if (upLabel.contains("-")) {
            upLabel = upLabel.replace("-", "_");
        }

        if (upLabel.contains("__")) {
            upLabel = upLabel.replace("__", "_");
        }

        return upLabel.toLowerCase();
    }

    /* Added by Monika
     * Firebase Analytics for Ecommerce Purchase
     */
    public static void fcmAnalyticsEcommerceEvents(Context context, String label,
                                                   String event, double priceValue,
                                                   String currency, String orderId,String purchaseSource) {
        Log.e("TestPurchase", "isPurchaseEventAddedByServer="+isPurchaseEventAddedByServer(context));
        if (isPurchaseEventAddedByServer(context)) {// in case of purchase event added by server then return(event does not trigger in this case)
            return;
        }
        Log.e("TestPurchase", "label="+label);

        String paramLabel = replaceSpaceDashFromLabelFcmEventName(label);
        String paramPurchaseSource = "";
        Bundle bundle = new Bundle();
        bundle.putString(CGlobalVariables.FIREBASE_LABEL_PARAMS, paramLabel);
        if (!TextUtils.isEmpty(purchaseSource)) {
            paramPurchaseSource = replaceSpaceDashFromLabelFcmEventName(purchaseSource);
        }
        bundle.putString(CGlobalVariables.FIREBASE_PURCHASE_SOURCE_PARAMS, paramPurchaseSource);
        bundle.putDouble(FirebaseAnalytics.Param.VALUE, priceValue);
        bundle.putString(FirebaseAnalytics.Param.CURRENCY, currency);
        FirebaseAnalytics.getInstance(context).logEvent(event, bundle);

        // facebook e-com events
        AppEventsLogger logger = AppEventsLogger.newLogger(context);
        Bundle params = new Bundle();
        String eventId = CUtils.getAppUserId(context) + "_" + orderId;   // final event_id
        params.putString("event_id", eventId);
        params.putString(AppEventsConstants.EVENT_PARAM_LEVEL, paramLabel);
        params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, currency);
        params.putString(AppEventsConstants.EVENT_PARAM_DESCRIPTION, paramPurchaseSource);
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT, priceValue + "");

        BigDecimal priceValueDec = new BigDecimal(priceValue, MathContext.DECIMAL64);
        Currency currency1 = Currency.getInstance(currency);
        logger.logPurchase(priceValueDec, currency1, params);

        //first purchase and repurchase event
        boolean isUserFirstTimePurchaseInVarta = CUtils.getBooleanData(context, CGlobalVariables.IS_USER_FIRST_TIME_PURCHASE_IN_VARTA, true);
        if (isUserFirstTimePurchaseInVarta) {
            CUtils.saveBooleanData(context, CGlobalVariables.IS_USER_FIRST_TIME_PURCHASE_IN_VARTA, false);
            // first purchase event
            logger.logEvent(CGlobalVariables.EVENT_FIRST_PURCHASE, priceValue, params);
            FirebaseAnalytics.getInstance(context).logEvent(CGlobalVariables.EVENT_FIRST_PURCHASE, bundle);
        } else {
            // repurchase event
            logger.logEvent(CGlobalVariables.EVENT_REPURCHASE, priceValue, params);
            FirebaseAnalytics.getInstance(context).logEvent(CGlobalVariables.EVENT_REPURCHASE, bundle);
        }
    }

    /* Added by Abhishek
     * Trigger Ecommerce Purchase Event service wise
     */
    public static void ecommercePurchaseEventsServiceWise(Context context,String label,
                                                          String event, double priceValue,
                                                          String currency, String orderId,String purchaseSource) {
        String paramLabel = replaceSpaceDashFromLabelFcmEventName(label);
        String paramPurchaseSource = "";
        Bundle bundle = new Bundle();
        bundle.putString(CGlobalVariables.FIREBASE_LABEL_PARAMS, paramLabel);
        if (!TextUtils.isEmpty(purchaseSource)) {
            paramPurchaseSource = replaceSpaceDashFromLabelFcmEventName(purchaseSource);
        }
        bundle.putString(CGlobalVariables.FIREBASE_PURCHASE_SOURCE_PARAMS, paramPurchaseSource);
        bundle.putDouble(FirebaseAnalytics.Param.VALUE, priceValue);
        bundle.putString(FirebaseAnalytics.Param.CURRENCY, currency);
        FirebaseAnalytics.getInstance(AstrosageKundliApplication.getAppContext()).logEvent(event, bundle);

        // facebook e-com events
        AppEventsLogger logger = AppEventsLogger.newLogger(AstrosageKundliApplication.getAppContext());
        Bundle params = new Bundle();
        String eventId = CUtils.getAppUserId(context) + "_" + orderId;   // final event_id
        params.putString("event_id", eventId);
        params.putString(AppEventsConstants.EVENT_PARAM_LEVEL, paramLabel);
        params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, currency);
        params.putString(AppEventsConstants.EVENT_PARAM_DESCRIPTION, paramPurchaseSource);
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT, priceValue + "");
        logger.logEvent(event, priceValue, params);
    }

    public static void saveAstroshopUserEmail(Context context,
                                              String obj) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                CGlobalVariables.APP_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.putString(CGlobalVariables.APP_PREFS_ASTROSHOP_USER_PHONE_NUMBER,
                obj.trim());

        sharedPrefEditor.commit();
    }

    public static String getAstroshopUserEmail(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                CGlobalVariables.APP_PREFS_NAME, MODE_PRIVATE);

        return sharedPreferences.getString(CGlobalVariables.APP_PREFS_ASTROSHOP_USER_PHONE_NUMBER, "");

    }

    public static void storeRegistrationId(Context context, String regId) {
        final SharedPreferences prefs = getGCMPreferences(context);
        int appVersion = getAppVersion(context);
        // //Log.e(TAG, "Saving regId on app version " + appVersion);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(CGlobalVariables.PROPERTY_REG_ID, regId);
        editor.putInt(CGlobalVariables.PROPERTY_APP_VERSION, appVersion);
        editor.commit();
        // //Log.e(TAG,"REGID "+ regId);
    }

    private static SharedPreferences getGCMPreferences(Context context) {
        // This sample app persists the registration ID in shared preferences,
        // but
        // how you store the regID in your app is up to you.
        return context.getSharedPreferences(
                CGlobalVariables.PREF_PUSH_NOTIFICATION_TOKEN,
                MODE_PRIVATE);
    }

    public static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }

    public static String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(
                CGlobalVariables.PROPERTY_REG_ID, "");
        if (registrationId.trim().length() == 0) {
            // //Log.i(TAG, "Registration not found.");
            return "";
        }
        // Check if app was updated; if so, it must clear the registration ID
        // since the existing regID is not guaranteed to work with the new
        // app version.
        int registeredVersion = prefs.getInt(
                CGlobalVariables.PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            // //Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    // END ON 21-11-2014

    /**
     * Subscribe to any GCM topics of interest, as defined by the TOPICS constant.
     *
     * @param token   GCM token
     * @param topic
     * @param context
     * @throws IOException if unable to reach the GCM PubSub service
     */
    // [START subscribe_topics]
    public static synchronized void subscribeTopics(String token, String topic, Context context) throws IOException {
        //Toast.makeText(context, "subscribeTopics-"+topic, Toast.LENGTH_SHORT).show();
        AstrosageKundliApplication.notificationIssueLogs = "\n" + AstrosageKundliApplication.notificationIssueLogs + "\n" + "subscribeTopics() varta= " + topic;
        try {
            FirebaseMessaging.getInstance().subscribeToTopic(topic).addOnSuccessListener(unused -> {
                AstrosageKundliApplication.notificationIssueLogs = "\n" + AstrosageKundliApplication.notificationIssueLogs + "\n" + "subscribeTopics() varta sucess=" + topic;

                //Toast.makeText(context, "sub->"+topic, Toast.LENGTH_SHORT).show();
                /*String log = com.ojassoft.astrosage.utils.CUtils.getStringData(context,"subscribeAndUnSubscribe", "") + "\nsubscribe success topic->"+topic;
                com.ojassoft.astrosage.utils.CUtils.saveStringData(context, "subscribeAndUnSubscribe", log);*/
            }).addOnFailureListener(e -> {
                AstrosageKundliApplication.notificationIssueLogs = "\n" + AstrosageKundliApplication.notificationIssueLogs + "\n" + "subscribeTopics() varta failed=" + topic;
                saveTopicsForSubscribe(topic);
                //Toast.makeText(context, "fail->"+topic, Toast.LENGTH_SHORT).show();
                /*String log = com.ojassoft.astrosage.utils.CUtils.getStringData(context,"subscribeAndUnSubscribe", "") + "\nsubscribe fail topic->"+topic+"::Exception"+e;
                com.ojassoft.astrosage.utils.CUtils.saveStringData(context, "subscribeAndUnSubscribe", log);*/
            });
        } catch (Exception ex) {
            AstrosageKundliApplication.notificationIssueLogs = "\n" + AstrosageKundliApplication.notificationIssueLogs + "\n" + "subscribeTopics() varta exp=" + ex;
            //Toast.makeText(context, "sub e->"+ex, Toast.LENGTH_SHORT).show();
            saveTopicsForSubscribe(topic);
            //Log.e("Exception", ex.getMessage());
        }
    }

    public static int myCount = 0, listCount = 0;

    public static void unSubscribeTopics(String token, String topic, Context context) throws IOException {
        //Toast.makeText(context, "unSubscribeTopics-"+topic, Toast.LENGTH_SHORT).show();
        try {
            FirebaseMessaging.getInstance().unsubscribeFromTopic(topic).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    //Toast.makeText(context, "unSubscribeTopics success"+topic, Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    //Toast.makeText(context, "unSubscribeTopics fail"+topic, Toast.LENGTH_SHORT).show();
                }
            });
        } catch (Exception ex) {
            //Toast.makeText(context, "ut->"+ex, Toast.LENGTH_SHORT).show();
            /*String log = com.ojassoft.astrosage.utils.CUtils.getStringData(context,"subscribeAndUnSubscribe", "") + "\nunsubscribe ex topic->"+topic;
            com.ojassoft.astrosage.utils.CUtils.saveStringData(context, "subscribeAndUnSubscribe", log);*/
        }
    }

    private static void saveTopicsForSubscribe(String topic) {
        try {
            if (topic.equals(CGlobalVariables.TOPIC_ALL)) {
                CUtils.saveStringData(AstrosageKundliApplication.getAppContext(), CGlobalVariables.TOPIC_ALL_NEED_TO_SEND_TO_GOOGLE_SERVER, topic);
            } else if (topic.equals(CGlobalVariables.TOPIC_VERSION + BuildConfig.VERSION_CODE)) {
                CUtils.saveStringData(AstrosageKundliApplication.getAppContext(), CGlobalVariables.VERSION_TOPIC_NEED_TO_SEND_TO_GOOGLE_SERVER, topic);
            } else if (isLanguageNotificationTopic(topic)) {
                CUtils.saveStringData(AstrosageKundliApplication.getAppContext(), CGlobalVariables.LANGUAGE_TOPIC_NEED_TO_SEND_TO_GOOGLE_SERVER, topic);
            } else {
                CUtils.saveStringData(AstrosageKundliApplication.getAppContext(), CGlobalVariables.EXTRA_TOPIC_NEED_TO_SEND_TO_GOOGLE_SERVER, topic);
            }
        } catch (Exception ex) {
            //Log.e("Exception", ex.getMessage());
        }
    }

    /**
     * Returns whether the topic belongs to the single active Varta app-language subscription set.
     */
    private static boolean isLanguageNotificationTopic(String topic) {
        return topic.equals(CGlobalVariables.TOPIC_ENGLISH)
                || topic.equals(CGlobalVariables.TOPIC_HINDI)
                || topic.equals(CGlobalVariables.TOPIC_BANGALI)
                || topic.equals(CGlobalVariables.TOPIC_TAMIL)
                || topic.equals(CGlobalVariables.TOPIC_TELUGU)
                || topic.equals(CGlobalVariables.TOPIC_GUJARATI)
                || topic.equals(CGlobalVariables.TOPIC_KANNADA)
                || topic.equals(CGlobalVariables.TOPIC_MARATHI)
                || topic.equals(CGlobalVariables.TOPIC_MALAYALAM)
                || topic.equals(CGlobalVariables.TOPIC_PUNJABI)
                || topic.equals(CGlobalVariables.TOPIC_ODIA)
                || topic.equals(CGlobalVariables.TOPIC_ASAMMESSE)
                || topic.equals(CGlobalVariables.TOPIC_SPANISH)
                || topic.equals(CGlobalVariables.TOPIC_FRENCH)
                || topic.equals(CGlobalVariables.TOPIC_CHINESE)
                || topic.equals(CGlobalVariables.TOPIC_JAPANESE)
                || topic.equals(CGlobalVariables.TOPIC_PORTUGUESE)
                || topic.equals(CGlobalVariables.TOPIC_GERMAN)
                || topic.equals(CGlobalVariables.TOPIC_ITALIAN);
    }

    public static void saveStringData(Context context, String key, String value) {

        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        if (preferences == null)
            return;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(key, value);
        editor.commit();

    }

    public static String getStringData(Context context, String key,
                                       String defaultValue) {
        if (context != null) {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            if (preferences == null)
                return "";
            String value = preferences.getString(key, defaultValue);

            return value;
        } else {
            return "";
        }
    }


    public static void saveLongData(Context context, String key, long value) {

        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        if (preferences == null)
            return;
        SharedPreferences.Editor editor = preferences.edit();
        editor.putLong(key, value);
        editor.commit();

    }

    public static long getLongData(Context context, String key) {
        if (context != null) {
            SharedPreferences preferences = PreferenceManager
                    .getDefaultSharedPreferences(context);
            if (preferences == null)
                return 0;
            long value = preferences.getLong(key, 0);

            return value;
        } else {
            return 0;
        }
    }

    //Load all topics in list
    public static ArrayList<TopicDetail> loadTopics(Context context) {
// used for retrieving arraylist from json formatted string
        try {
            SharedPreferences settings;
            List topicDetailList;
            settings = context.getSharedPreferences(CGlobalVariables.PREFS_NAME, MODE_PRIVATE);
            if (settings.contains(CGlobalVariables.topicListKey)) {
                String jsonFavorites = settings.getString(CGlobalVariables.topicListKey, null);
                Gson gson = new Gson();
                TopicDetail[] topicDetails = gson.fromJson(jsonFavorites, TopicDetail[].class);
                topicDetailList = Arrays.asList(topicDetails);
                topicDetailList = new ArrayList(topicDetailList);
                return (ArrayList<TopicDetail>) topicDetailList;
            } else {
                return null;
            }
        } catch (Exception e) {
            return null;
        }
    }

    //Add topics in list
    public static ArrayList<TopicDetail> addTopicsInList(Context context) {
        TopicDetail topicDetail1 = new TopicDetail();
        topicDetail1.setTopicName("Cricket");
        topicDetail1.setTopicId(CGlobalVariables.TOPIC_CRICKET);
        topicDetail1.setSubscribed(false);

        TopicDetail topicDetail2 = new TopicDetail();
        topicDetail2.setTopicName("Share Market");
        topicDetail2.setTopicId(CGlobalVariables.TOPIC_SHARE_MARKET);
        topicDetail2.setSubscribed(false);

        TopicDetail topicDetail3 = new TopicDetail();
        topicDetail3.setTopicName("Bollywood");
        topicDetail3.setTopicId(CGlobalVariables.TOPIC_BOLLYWOOD);
        topicDetail3.setSubscribed(false);

        TopicDetail topicDetail4 = new TopicDetail();
        topicDetail4.setTopicName("New Magazine");
        topicDetail4.setTopicId(CGlobalVariables.TOPIC_NEW_MAGAZINE);
        topicDetail4.setSubscribed(false);

        //added by abhishek to subscribe the politics topic
        TopicDetail topicDetail5 = new TopicDetail();
        topicDetail5.setTopicName("Politics");
        topicDetail5.setTopicId(CGlobalVariables.TOPIC_POLITICS);
        topicDetail5.setSubscribed(false);

        ArrayList<TopicDetail> topicList = loadTopics(context);
        if (topicList == null) {
            topicList = new ArrayList<TopicDetail>();
        }
        if (!topicList.contains(topicDetail1)) {
            topicList.add(topicDetail1);
        }
        if (!topicList.contains(topicDetail2)) {
            topicList.add(topicDetail2);
        }
        if (!topicList.contains(topicDetail3)) {
            topicList.add(topicDetail3);
        }
        if (!topicList.contains(topicDetail4)) {
            topicList.add(topicDetail4);
        }
        if (!topicList.contains(topicDetail5)) {
            topicList.add(topicDetail5);
        }
        return topicList;

    }

    public static void saveIntData(Context context, String key, int value) {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    public static int getIntData(Context context, String key, int defaultValue) {
        SharedPreferences preferences = PreferenceManager
                .getDefaultSharedPreferences(context);
        int value = preferences.getInt(key, defaultValue);
        return value;
    }

    /**
     * This method is used to get Topics name from language code
     *
     * @param languageCode
     * @return topic name
     */
    public static String getTopicName(int languageCode) {

        String topicName = "";

        switch (languageCode) {
            case CGlobalVariables.ENGLISH:
                topicName = CGlobalVariables.TOPIC_ENGLISH;
                break;
            case CGlobalVariables.HINDI:
                topicName = CGlobalVariables.TOPIC_HINDI;
                break;
            case CGlobalVariables.TAMIL:
                topicName = CGlobalVariables.TOPIC_TAMIL;
                break;
            case CGlobalVariables.MARATHI:
                topicName = CGlobalVariables.TOPIC_MARATHI;
                break;
            case CGlobalVariables.BANGALI:
                topicName = CGlobalVariables.TOPIC_BANGALI;
                break;
            case CGlobalVariables.KANNADA:
                topicName = CGlobalVariables.TOPIC_KANNADA;
                break;
            case CGlobalVariables.TELUGU:
                topicName = CGlobalVariables.TOPIC_TELUGU;
                break;
            case CGlobalVariables.MALAYALAM:
                topicName = CGlobalVariables.TOPIC_MALAYALAM;
                break;
            case CGlobalVariables.GUJARATI:
                topicName = CGlobalVariables.TOPIC_GUJARATI;
                break;
            case CGlobalVariables.PUNJABI:
                topicName = CGlobalVariables.TOPIC_PUNJABI;
                break;
            case CGlobalVariables.ODIA:
                topicName = CGlobalVariables.TOPIC_ODIA;
                break;
            case CGlobalVariables.ASAMMESSE:
                topicName = CGlobalVariables.TOPIC_ASAMMESSE;
                break;
            case CGlobalVariables.SPANISH:
                topicName = CGlobalVariables.TOPIC_SPANISH;
                break;
            case CGlobalVariables.FRENCH:
                topicName = CGlobalVariables.TOPIC_FRENCH;
                break;
            case CGlobalVariables.CHINESE:
                topicName = CGlobalVariables.TOPIC_CHINESE;
                break;
            case CGlobalVariables.JAPANESE:
                topicName = CGlobalVariables.TOPIC_JAPANESE;
                break;
            case CGlobalVariables.PORTUGUESE:
                topicName = CGlobalVariables.TOPIC_PORTUGUESE;
                break;
            case CGlobalVariables.GERMAN:
                topicName = CGlobalVariables.TOPIC_GERMAN;
                break;
            case CGlobalVariables.ITALIAN:
                topicName = CGlobalVariables.TOPIC_ITALIAN;
                break;
        }

        return topicName;
    }

    public static String replaceEmailChar(String emailId) {

        try {
            int xCharacterAhead = 1;
            emailId = encodeWithAsciiValue(emailId, xCharacterAhead);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return emailId;
    }

    /**
     * Method to encode String With Ascii Value
     *
     * @param stringToEncode
     * @param XAheadCharacter
     * @return
     */
    static String encodeWithAsciiValue(String stringToEncode, int XAheadCharacter) {
        // changed string
        String newString = "";
        // iterate for every characters
        for (int iterator = 0; iterator < stringToEncode.length(); ++iterator) {
            // ASCII value
            int val = stringToEncode.charAt(iterator);
            // store the duplicate
            newString += (char) (val + XAheadCharacter);
        }
        // return the new string
        return newString;
    }

    public static void startGcmService(Context context) {
        try {
            if (context != null) {
                context.startService(new Intent(context, MyCloudRegistrationService.class));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean getAppStartSharedPrefrence(Context con) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(con);
        boolean isStarted = prefs.getBoolean(IS_APP_STARTED, false);
        return isStarted;
    }

    public static boolean saveAppStartSharedPrefrence(Context con) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(con);
        SharedPreferences.Editor prefsEditor = prefs.edit();
        prefsEditor.putBoolean(IS_APP_STARTED, true);
        boolean result = prefsEditor.commit();
        return result;
    }

    public static String getSavedNotificationID(Context context, String str) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                CGlobalVariables.APP_PREFS_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(str,
                "");
    }

    public static void setSavedNotificationID(Context context, String id, String str) {
        SharedPreferences.Editor editor = context.getSharedPreferences(CGlobalVariables.APP_PREFS_NAME,
                        MODE_PRIVATE)
                .edit();
        editor.putString(str, id);
        editor.commit();
    }

    public static boolean isChatNotInitiated() {
        return !CUtils.checkServiceRunning(AstroAcceptRejectService.class) &&
                !CUtils.checkServiceRunning(OnGoingChatService.class);
    }
    public static boolean isAICallNotInitiated() {
        return !CUtils.checkServiceRunning(AIVoiceCallingService.class);
    }

    public enum callBack {GET_ORDER_ID, PAYTM_TRANSECTION, POST_STATUS, VERIFY_PAYMENT, POST_RAZORPAYSTATUS, APPLY_SERVICE_COUPON, GET_CHECKSUM, POST_PRODUCT_RAZORPAYSTATUS}

    public static void openWebBrowser(Context context, Uri uri) {
        try {
            Intent i = new Intent(Intent.ACTION_VIEW);
            i.setPackage("com.android.chrome");
            i.setData(uri);
            context.startActivity(i);
        } catch (Exception e) { // while chrome not available
            try {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(uri);
                context.startActivity(i);
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    public static void clearWalletApiCache(RequestQueue queue) {
        try {
            saveWalletRechargeData("");

            /*Calendar calendar = Calendar.getInstance();
            String url = CGlobalVariables.RECHARGESERVICES + "date=" + calendar.get(Calendar.DATE) + "-" + calendar.get(Calendar.MONTH) + "-" + calendar.get(Calendar.YEAR);
            Cache cache = null;
            if (CGlobalVariables.walletRequestQueue != null) {
                cache = CGlobalVariables.walletRequestQueue.getCache();
            } else {
                cache = queue.getCache();
            }
            if (cache != null) {
                cache.remove("1-" + url);
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean checkReceiveSmsPermission(Context context) {
        /*int result1 = ContextCompat.checkSelfPermission(context, Manifest.permission.RECEIVE_SMS);
        if (result1 == PackageManager.PERMISSION_GRANTED) {
            return true;
        } else {
            return false;
        }*/
        return true;
    }

    public static void requestReceiveSmsPermission(Activity context) {
        //ActivityCompat.requestPermissions(context, new String[]{Manifest.permission.RECEIVE_SMS}, CGlobalVariables.RECEIVE_SMS_PERMISSION_REQUEST);
    }

    public static long convertStringDateToMillis(String dateString) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        try {
            Date date = sdf.parse(dateString);
            return date.getTime();
        } catch (Exception e) {
            return -1;
        }
    }

    /**
     * This method convert milliseconds time to string format
     *
     * @param timestamp
     * @return time in string like one day ago
     */
    public static String convertTimeInString(Context adapterContext, long timestamp) {
        String time = "", time_text = "";
        try {
            Date past = new Date(timestamp);

            Date now = new Date();

            long seconds = TimeUnit.MILLISECONDS.toSeconds(now.getTime() - past.getTime());
            long minutes = TimeUnit.MILLISECONDS.toMinutes(now.getTime() - past.getTime());
            long hours = TimeUnit.MILLISECONDS.toHours(now.getTime() - past.getTime());
            long days = TimeUnit.MILLISECONDS.toDays(now.getTime() - past.getTime());

            if (seconds < 60) {
                if (seconds == 0) {
                    time_text = adapterContext.getResources().getString(R.string.text_now);
                    time = (time_text);
                } else if (seconds > 1) {
                    time_text = adapterContext.getResources().getString(R.string.text_seconds_ago);

                    time = (seconds + " " + time_text);
                } else {
                    time_text = adapterContext.getResources().getString(R.string.text_second_ago);
                    time = (seconds + " " + time_text);
                }
            } else if (minutes < 60) {

                if (minutes > 1) {
                    time_text = adapterContext.getResources().getString(R.string.text_minutes_ago);
                    time = (minutes + " " + time_text);
                } else {
                    time_text = adapterContext.getResources().getString(R.string.text_minute_ago);
                    time = (minutes + " " + time_text);
                }
            } else if (hours < 24) {

                if (hours > 1) {
                    time_text = adapterContext.getResources().getString(R.string.text_hours_ago);
                    time = (hours + " " + time_text);
                } else {
                    time_text = adapterContext.getResources().getString(R.string.text_hour_ago);
                    time = (hours + " " + time_text);
                }
            } else {
                if (days > 1) {
                    //time = (days + " days ago");
                    time = getDateByTimestamp(timestamp + "") + " " + getTimeByTimestamp(timestamp + "");
                } else {
                    time_text = adapterContext.getResources().getString(R.string.text_day_ago);
                    time = (days + " " + time_text);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time;
    }

    public static String getDateByTimestamp(String sTime) {
        String date = "";
        try {
            long timestamp = Long.valueOf(sTime);
            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
            cal.setTimeInMillis(timestamp);
            date = android.text.format.DateFormat.format("dd MMM, yyyy", cal).toString();

            SimpleDateFormat sdf = new SimpleDateFormat("dd MMM, yyyy");

        } catch (Exception e) {
            e.printStackTrace();
        }
        return date;
    }

    public static String getTimeByTimestamp(String sTime) {
        String time = "";
        try {
            long timestamp = Long.valueOf(sTime);
            Calendar cal = Calendar.getInstance(Locale.ENGLISH);
            cal.setTimeInMillis(timestamp);
            time = android.text.format.DateFormat.format("hh:mm a", cal).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return time.toUpperCase();
    }

    public static void saveProfileForChatInPreference(Context context, UserProfileData userPrimaryProfile) {
        SharedPreferences pSharedPref = context.getSharedPreferences(CGlobalVariables.USER_SELECTED_PROFILE_DATA_CHAT, MODE_PRIVATE);
        if (pSharedPref != null) {
            Gson gson = new Gson();
            SharedPreferences.Editor editor = pSharedPref.edit();

            if (userPrimaryProfile == null) {
                // Logout / Clear
                editor.remove(CGlobalVariables.USER_SELECTED_PROFILE_DATA_CHAT).apply();
            } else {
                // Save
                String json = gson.toJson(userPrimaryProfile);
                editor.remove(CGlobalVariables.USER_SELECTED_PROFILE_DATA_CHAT).apply();
                editor.putString(CGlobalVariables.USER_SELECTED_PROFILE_DATA_CHAT, json);
                editor.commit();
            }

        }
    }

    /**
     * load User Profile In Preference
     *
     * @return
     */
    public static UserProfileData getProfileForChatFromPreference(Context context) {
        UserProfileData outputBean = null;
        SharedPreferences pSharedPref = context.getSharedPreferences(CGlobalVariables.USER_SELECTED_PROFILE_DATA_CHAT,
                MODE_PRIVATE);
        try {
            if (pSharedPref != null) {
                String jsonString = pSharedPref.getString(CGlobalVariables.USER_SELECTED_PROFILE_DATA_CHAT, (new JSONObject()).toString());
                JSONObject jsonObject = new JSONObject(jsonString);
                Gson gson = new Gson();
                outputBean = new UserProfileData();
                outputBean = gson.fromJson(jsonObject.toString(), UserProfileData.class);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return outputBean;
    }

    public static String getAppPackageName(Context context) {
        try {
            return context.getPackageName();
        } catch (Exception e) {
            return "com.ojassoft.astrosage";
        }
    }

    public static String getApplicationVersionToShow(Context context) {
        // String version ="";
        try {
            return context.getPackageManager().getPackageInfo(
                    context.getPackageName(), 0).versionName;
            /*
             * PackageInfo pInfo =
             * context.getPackageManager().getPackageInfo(context
             * .getPackageName(), 0); version = pInfo.versionName;
             */
        } catch (Exception e) {
            // Log.d("VERSION-Error", e.getMessage());
        }
        // return version;
        return "";
    }

    public static void restartApplication(Activity activity) {
        Intent intent = new Intent(activity, DashBoardActivity.class);
        activity.startActivity(intent);
    }

    public static void shareToFriendMail(Context context) {

        CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_KUNDLI_SHARE_APP, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

        CharSequence body2 = context.getResources().getString(
                R.string.shareAppBody);
        // String subject = body2.toString();

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        // intent.putExtra(Intent.EXTRA_SUBJECT, subject);
        intent.putExtra(Intent.EXTRA_TEXT, body2);
        try {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

            context.startActivity(Intent.createChooser(intent, context.getResources().getString(
                    R.string.share_app)));
        } catch (android.content.ActivityNotFoundException ex) {
            // //Log.e("Exception", ex.getMessage());
        }
    }

    public static void sendFeedbackActivity(Context context) {
        Intent intent = new Intent(context, FeedbackActivity.class);
        context.startActivity(intent);
    }

    public static GmailAccountInfo getGmailAccountInfo(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                CGlobalVariables.APP_PREFS_NAME, MODE_PRIVATE);
        Gson gson = new Gson();
        String json = sharedPreferences.getString("GmailAccountInfo", "");
        GmailAccountInfo obj = gson.fromJson(json,
                GmailAccountInfo.class);
        return obj;
    }

    public static void vollyPostRequest(VolleyResponse volleyResponse, String url, Map<String, String> params, int method) {
        RequestQueue queue = VolleySingleton.getInstance(AstrosageKundliApplication.getAppContext()).getRequestQueue();
        StringRequest stringRequest = new VolleyServiceHandler(Request.Method.POST, url, volleyResponse, true, params, method).getMyStringRequest();
        queue.add(stringRequest);
    }

    public static void verifyOtpRequest(VolleyResponse volleyResponse, String mobileNo, String otp, int method) {
        String url = CGlobalVariables.SEND_JOIN_REQUEST;
        //Log.e("LOGINN ", url);
        vollyPostRequest(volleyResponse, url, getVerifyOtpRequestParams(mobileNo, otp), method);
    }


    public static Map<String, String> getVerifyOtpRequestParams(String mobileNo, String otp) {

        Context context = AstrosageKundliApplication.getAppContext();
        String deviceId = getMyAndroidId(context);
        String key = getApplicationSignatureHashCode(context);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put(CGlobalVariables.KEY_API, key);
        params.put("device_id", deviceId);
        params.put("methodName", "submitOTP");
        params.put("isapi", "1");
        params.put("phno", mobileNo);
        params.put("otpno", otp);
        params.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(context));
        params.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));

        //"JoinReqResponse  params", params.toString());
        return params;
    }

    public static void getConsultationHistory(VolleyResponse volleyResponse, String historyType, String lastId, int method) {
//        String url = CGlobalVariables.CONSULTATIONHISTORY;
//        //Log.e("SAN ", " url" );
//        vollyPostRequest(volleyResponse, url, getConsultationHistoryParams(historyType, lastId), method);
    }

    public static void getConsultationHistoryViaRetrofit(RetrofitResponses retrofitResponses, String historyType, String lastId, String calledFrom) {
        ApiList api = RetrofitClient.getInstance().create(ApiList.class);
        Call<ResponseBody> call = api.getHistory(getConsultationHistoryParams(historyType, lastId));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                retrofitResponses.onResponse(call, response);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                retrofitResponses.onFailure(call, t);
            }
        });
    }

    public static Map<String, String> getConsultationHistoryParams(String historyType, String lastId) {

        Context context = AstrosageKundliApplication.getAppContext();
        String key = getApplicationSignatureHashCode(context);
        String userId = getUserID(context);
        String countryCode = getCountryCode(context);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put(CGlobalVariables.APP_KEY, key);
        params.put(CGlobalVariables.USER_PHONE_NO, userId);
        params.put(CGlobalVariables.COUNTRY_CODE, countryCode);
        params.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
        params.put(CGlobalVariables.IGNORE_ASTRO, "true");
        params.put(CONSULT_HISTORY_TYPE, historyType);
        params.put(CONSULT_HISTORY_LIST_ID, lastId);

        //Log.e("SAN ", " params " + params.toString() );

        //"JoinReqResponse  params", params.toString());
        return CUtils.setRequiredParams(params);
    }


    public final static boolean isValidEmail(CharSequence target) {
        if (target == null) {
            return false;
        } else {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public static void resendOtpRequest(VolleyResponse volleyResponse, String mobileNo, int method) {
        String url = CGlobalVariables.SEND_JOIN_REQUEST;
        //Log.e("LOGINN ", url);
        vollyPostRequest(volleyResponse, url, getResendOtpRequestParams(mobileNo), method);
    }

    public static Map<String, String> getResendOtpRequestParams(String mobileNo) {

        Context context = AstrosageKundliApplication.getAppContext();
        String deviceId = getMyAndroidId(context);
        String key = getApplicationSignatureHashCode(context);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put(CGlobalVariables.KEY_API, key);
        params.put("device_id", deviceId);
        params.put("methodName", "resendOTP");
        params.put("isapi", "1");
        params.put("phno", mobileNo);
        params.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(context));
        //Log.e("JoinReqResponse  params", params.toString());
        params.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));

        return params;
    }

    public static void sendJoinRequest(VolleyResponse volleyResponse, Map<String, String> joinRequestParams, int method) {
        String url = CGlobalVariables.SEND_JOIN_REQUEST;
        //Log.e("LOGINN ", url);
        vollyPostRequest(volleyResponse, url, joinRequestParams, method);
    }

    public static void setDataForFirstTimeUserGetBonus(Context context, boolean isGetBonus) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                CGlobalVariables.APP_PREFS_FIRST_GET_BONUS, MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.putBoolean(CGlobalVariables.APP_PREFS_KEY_IS_GET_BONUS, isGetBonus);
        sharedPrefEditor.commit();
    }

    public static boolean getDataForFirstTimeUserGetBonus(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                CGlobalVariables.APP_PREFS_FIRST_GET_BONUS, MODE_PRIVATE);
        return sharedPreferences.getBoolean(CGlobalVariables.APP_PREFS_KEY_IS_GET_BONUS,
                false);
    }

    public static String getIsEligibleForBonusStatus(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                CGlobalVariables.APP_PREFS_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(CGlobalVariables.APP_PREFS_IS_ELIGIBLE_FOR_BONUS,
                "0");
    }

    public static void setDataForOneRsDialog(Context context, String isShowOneRsDialog) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                CGlobalVariables.APP_PREFS_ONE_RS_DIALOG, MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.putString(CGlobalVariables.APP_PREFS_KEY_ONE_RS_DIALOG, isShowOneRsDialog);
        sharedPrefEditor.commit();
    }

    public static String getDataForOneRsDialog(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                CGlobalVariables.APP_PREFS_ONE_RS_DIALOG, MODE_PRIVATE);
        return sharedPreferences.getString(CGlobalVariables.APP_PREFS_KEY_ONE_RS_DIALOG,
                "0");
    }

    public static void setDataForFreeSessionDialog(Context context, String isFreeSessionDialog) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                CGlobalVariables.APP_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.putString(CGlobalVariables.APP_PREFS_KEY_FREE_SESSION_DIALOG, isFreeSessionDialog);
        sharedPrefEditor.commit();
    }

    public static String getDataForFreeSessionDialog(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                CGlobalVariables.APP_PREFS_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(CGlobalVariables.APP_PREFS_KEY_FREE_SESSION_DIALOG,
                "0");
    }

    public static void setAmountOnDialog(Context context, String isShowOneRsDialog) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                CGlobalVariables.APP_PREFS_AMOUNT_ON_DIALOG, MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.putString(CGlobalVariables.APP_PREFS_KEY_AMOUNT_ON_DIALOG, isShowOneRsDialog);
        sharedPrefEditor.commit();
    }

    public static String getAmountOnDialog(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                CGlobalVariables.APP_PREFS_AMOUNT_ON_DIALOG, MODE_PRIVATE);
        return sharedPreferences.getString(CGlobalVariables.APP_PREFS_KEY_AMOUNT_ON_DIALOG,
                "0");
    }

    public static void cancelNotification(Context context) {
        try {
            NotificationManager nMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            nMgr.cancel(CGlobalVariables.ONGOING_NOTIFICATION, 1001);
        } catch (Exception e) {
            //
        }
    }

    public static void cancelNotification(Context context, String notificationTag, int notificationId) {
        try {
            NotificationManager nMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            nMgr.cancel(notificationTag, notificationId);
        } catch (Exception e) {
            //
        }

    }

    public static void cancelChatNotification(Context context) {
        CGlobalVariables.CHAT_NOTIFICATION_QUEUE = "";
        NotificationManager nMgr = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        nMgr.cancel(CGlobalVariables.CHAT_NOTIFICATION, CGlobalVariables.CHAT_NOTIFICATION_ID);
    }

    public static void changeFirebaseKeyStatus(String channelID, String isUserAccepted, boolean isUserEndChat, String remarks) {
        if (!isUserAccepted.equals("NA")) {
            AstrosageKundliApplication.getmFirebaseDatabase(channelID).
                    child(CGlobalVariables.USER_ACCEPTED_FBD_KEY).setValue(isUserAccepted);
        }

        if (isUserEndChat) {
            AstrosageKundliApplication.getmFirebaseDatabase(channelID).
                    child(CGlobalVariables.END_CHAT_TIME_FBD_KEY).setValue(ServerValue.TIMESTAMP);
            AstrosageKundliApplication.getmFirebaseDatabase(channelID).
                    child(CGlobalVariables.END_CHAT_SATATUS_FBD_KEY).setValue(remarks);
            AstrosageKundliApplication.getmFirebaseDatabase(channelID).
                    child(CGlobalVariables.END_CHAT_FBD_KEY).setValue(isUserEndChat);
            cancelOnDisconnentEvent(channelID);
        }

    }

    public static void cancelOnDisconnentEvent(String channelId) {
        try {
            if (!TextUtils.isEmpty(channelId)) {
                AstrosageKundliApplication.getmFirebaseDatabase(channelId).child(CGlobalVariables.USER_DISCONNECT_TIME_FBD_KEY).onDisconnect().setValue(ServerValue.TIMESTAMP);
                //AstrosageKundliApplication.getmFirebaseDatabase(channelId).child(CGlobalVariables.END_CHAT_FBD_KEY).onDisconnect().setValue(true);
                //AstrosageKundliApplication.getmFirebaseDatabase(channelId).child(CGlobalVariables.END_CHAT_TIME_FBD_KEY).onDisconnect().setValue(ServerValue.TIMESTAMP);
                //AstrosageKundliApplication.getmFirebaseDatabase(channelId).child(CGlobalVariables.END_CHAT_SATATUS_FBD_KEY).onDisconnect().setValue(CGlobalVariables.USER_OFFLINE);
            }
        } catch (Exception e) {
            //
        }
    }

    public static void getFilterCategory(VolleyResponse volleyResponse, int method) {
        String url = CGlobalVariables.GET_FILTER_LIST;
        //Log.e("SAN FirebaseAcceTok 3", " url= "+url);
        //Log.e("SAN FBT param ",  setAstrologerInviteParams().toString());
        vollyPostRequest(volleyResponse, url, setfilterParams(), method);
        //vollyGetRequest(volleyResponse, url, method);

    }

    public static Map<String, String> setRequiredParams(Map<String, String> map) {
        map.put(CGlobalVariables.DEVICE_ID, CUtils.getMyAndroidId(AstrosageKundliApplication.getAppContext()));
        map.put(CGlobalVariables.APP_VERSION, BuildConfig.VERSION_NAME);
        map.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(AstrosageKundliApplication.getAppContext()));
        return map;
    }

    public static Map<String, String> setfilterParams() {

        Context context = AstrosageKundliApplication.getAppContext();
        if (context == null) return null;
        String deviceId = CUtils.getMyAndroidId(context);
        String key = CUtils.getApplicationSignatureHashCode(context);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put(CGlobalVariables.KEY_API, key);
        params.put(DEVICE_ID, deviceId);
        params.put("packageName", BuildConfig.APPLICATION_ID);

        return params;
    }

    public static void getFirebaseAuthToken(GlobalRetrofitResponse globalRetrofitResponse, int method) {
        // String url = CGlobalVariables.GET_FIREBASE_AUTH_TOKEN;
        //Log.e("SAN FirebaseAcceTok 3", " url= "+url);
        //Log.e("SAN FBT param ",  setAstrologerInviteParams().toString());
        ApiList api = RetrofitClient.getInstance().create(ApiList.class);
        Call<ResponseBody> call = api.fetchFireBaseToken(setAstrologerInviteParams());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                globalRetrofitResponse.onResponse(call, response, method);
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                globalRetrofitResponse.onFailure(call, t);
            }
        });
        // vollyPostRequest(volleyResponse, url, setAstrologerInviteParams(), method);
    }

    public static Map<String, String> setAstrologerInviteParams() {

        Context context = AstrosageKundliApplication.getAppContext();
        if (context == null) return null;
        String deviceId = CUtils.getMyAndroidId(context);
        String key = CUtils.getApplicationSignatureHashCode(context);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(context));
        params.put(CGlobalVariables.PHONE_NO, CUtils.getUserID(context));
        params.put(KEY_PASSWORD, getUserLoginPassword(context));
        params.put(CGlobalVariables.KEY_API, key);
        params.put(DEVICE_ID, deviceId);
        params.put("packageName", BuildConfig.APPLICATION_ID);
        params.put(CGlobalVariables.PROPERTY_APP_VERSION, BuildConfig.VERSION_NAME);
        params.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));

        //Log.e("FirebaseAccessToken 3", " params= "+params);
        return params;
    }

    /**
     * This method used for set the Astro data in  Singleton class AppDataSingleton
     *
     * @param astroList
     */
    public static void saveAstroList(String astroList) {
        if (TextUtils.isEmpty(astroList)) {
            if (allAstrologersArrayList != null) { // clear astrologer list
                allAstrologersArrayList.clear();
            }
        }
        AppDataSingleton.getInstance().setAstrologerData(astroList);
    }

    public static String getAstroList() {
        return AppDataSingleton.getInstance().getAstrologerData();
    }

    public static void saveAstroListWithLimit(String astroList) {
        AppDataSingleton.getInstance().setAstrologerDataWithLimit(astroList);
    }

    public static void saveAiRandomChatAstroDeatils(String astroDetails) {
        AppDataSingleton.getInstance().setAiAstrologerRandomChatDetail(astroDetails);
    }

    public static String getAiRandomChatAstroDeatils() {
        return AppDataSingleton.getInstance().getAiAstrologerRandomChatDetail();
    }

    public static String getAstroListWithLimit() {
        return AppDataSingleton.getInstance().getAstrologerDataWithLimit();
    }

    public static void saveHistoryList(String list) {
        AppDataSingleton.getInstance().setHistoryListData(list);
    }

    public static String getHistoryList() {
        return AppDataSingleton.getInstance().getHistoryListData();
    }

    public static void saveBannerData(String bannerData) {
        AppDataSingleton.getInstance().setBanneerData(bannerData);
    }

    public static String getBannerList() {
        return AppDataSingleton.getInstance().getBanneerData();
    }

    public static void saveConsultationHistoryData(String conHisData) {
        AppDataSingleton.getInstance().setConsultHistoryData(conHisData);
    }

    public static String getConsultationHistoryList() {
        return AppDataSingleton.getInstance().getConsultHistoryData();
    }

    public static void saveWalletRechargeData(String walletRechargeData) {
        AppDataSingleton.getInstance().setWalletRechargeData(walletRechargeData);
    }

    public static String getWalletRechargeData() {
        return AppDataSingleton.getInstance().getWalletRechargeData();
    }
    public static void saveIsAiPassData(String isAiPassData) {
        AppDataSingleton.getInstance().setIsAiPassData(isAiPassData);
    }

    public static String getIsAiPassData() {
        return AppDataSingleton.getInstance().getIsAiPassData();
    }

    public static void saveLiveAstroList(String liveAstroList) {
        AppDataSingleton.getInstance().setLiveAstrologerData(liveAstroList);
    }

    public static String getLiveAstroList() {
        return AppDataSingleton.getInstance().getLiveAstrologerData();
    }


    public static String convertAmtIntoIndianFormat(String walletAmt) {

        try {
            double amount = Double.parseDouble(walletAmt);
            DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
            DecimalFormat formatter = new DecimalFormat("0.00", symbols);

            String doubleStr = formatter.format(amount);
            String[] valueArr = doubleStr.split("\\.");
            String value = valueArr[0];
            int len = value.length() - 1;
            char lastDigit = value.charAt(len);
            int nDigits = 0;
            String result = "";

            for (int i = len - 1; i >= 0; i--) {
                result = value.charAt(i) + result;
                nDigits++;
                if (((nDigits % 2) == 0) && (i > 0))
                    result = "," + result;
            }
            return result + lastDigit;//+"."+valueArr[1];
        } catch (Exception e) {
        }

        return "";
    }

    public static String convertAmtIntoIndianDecimalFormat(String walletAmt) {

        try {
            double amount = Double.parseDouble(walletAmt);
            DecimalFormatSymbols symbols = new DecimalFormatSymbols(Locale.US);
            DecimalFormat formatter = new DecimalFormat("0.00", symbols);
            String doubleStr = formatter.format(amount);
            String[] valueArr = doubleStr.split("\\.");
            String value = valueArr[0];
            int len = value.length() - 1;
            char lastDigit = value.charAt(len);
            int nDigits = 0;
            String result = "";

            for (int i = len - 1; i >= 0; i--) {
                result = value.charAt(i) + result;
                nDigits++;
                if (((nDigits % 2) == 0) && (i > 0))
                    result = "," + result;
            }
            return result + lastDigit + "." + valueArr[1];
        } catch (Exception e) {
        }

        return "";
    }

    public static int getLanguageCodeFromPreference(Context context) {
        int language_code = 0;
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                CGlobalVariables.MYKUNDLI_LANGUAGE_PREFS_NAME,
                MODE_PRIVATE);
        language_code = sharedPreferences.getInt(
                CGlobalVariables.APP_PREFS_AppLanguage,
                CGlobalVariables.ENGLISH);
        return language_code;
    }

    public static int getScreenCodeFromPreference(Context context) {
        int language_code = 0;
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                CGlobalVariables.MYKUNDLI_LANGUAGE_PREFS_NAME,
                MODE_PRIVATE);
        language_code = sharedPreferences.getInt(
                CGlobalVariables.APP_PREFS_AppScreen,
                CGlobalVariables.ENGLISH);
        return language_code;
    }

    public static String getLanguage(int language) {
        String lang = "";
        switch (language) {
            case CGlobalVariables.HINDI:
                lang = "Hindi";
                break;
            case CGlobalVariables.GUJARATI:
                lang = "Gujarati";
                break;
            case CGlobalVariables.TELUGU:
                lang = "Telugu";
                break;
            case CGlobalVariables.TAMIL:
                lang = "Tamil";
                break;
            case CGlobalVariables.KANNADA:
                lang = "Kannada";
                break;
            case CGlobalVariables.MARATHI:
                lang = "Marathi";
                break;
            case CGlobalVariables.MALAYALAM:
                lang = "Malayalam";
                break;
            case CGlobalVariables.BANGALI:
                lang = "Bangali";
                break;
            case CGlobalVariables.ODIA:
                lang = "Odia";
                break;
            case CGlobalVariables.ASAMMESSE:
                lang = "Asammesse";
                break;
            case CGlobalVariables.SPANISH:
                lang = "Spanish";
                break;
            case CGlobalVariables.FRENCH:
                lang = "French";
                break;
            case CGlobalVariables.CHINESE:
                lang = "Chinese";
                break;
            case CGlobalVariables.JAPANESE:
                lang = "Japanese";
                break;
            case CGlobalVariables.PORTUGUESE:
                lang = "Portuguese";
                break;
            case CGlobalVariables.GERMAN:
                lang = "German";
                break;
            case CGlobalVariables.ITALIAN:
                lang = "Italian";
                break;
            case CGlobalVariables.PUNJABI:
                lang = "";
                break;
        }

        return lang;
    }

    public static String getLanguageKey(int language) {
        String lang = "en";
        switch (language) {
            case CGlobalVariables.HINDI:
                lang = "hi";
                break;
            case CGlobalVariables.GUJARATI:
                lang = "gu";
                break;
            case CGlobalVariables.TELUGU:
                lang = "te";
                break;
            case CGlobalVariables.TAMIL:
                lang = "ta";
                break;
            case CGlobalVariables.KANNADA:
                lang = "ka";
                break;
            case CGlobalVariables.MARATHI:
                lang = "mr";
                break;
            case CGlobalVariables.MALAYALAM:
                lang = "ml";
                break;
            case CGlobalVariables.BANGALI:
                lang = "bn";
                break;
            case CGlobalVariables.PUNJABI:
                lang = "pa";
                break;
            case CGlobalVariables.ODIA:
                lang = "or";
                break;
            case CGlobalVariables.ASAMMESSE:
                lang = "as";
                break;
            case CGlobalVariables.SPANISH:
                lang = "es";
                break;
            case CGlobalVariables.FRENCH:
                lang = "fr";
                break;
            case CGlobalVariables.CHINESE:
                lang = "zh";
                break;
            case CGlobalVariables.JAPANESE:
                lang = "ja";
                break;
            case CGlobalVariables.PORTUGUESE:
                lang = "pt";
                break;
            case CGlobalVariables.GERMAN:
                lang = "de";
                break;
            case CGlobalVariables.ITALIAN:
                lang = "it";
                break;
        }

        return lang;
    }

    public static Typeface getRobotoFont(Context context, int language, String fontType) {
        Typeface typeface = null;
        if (fontType.equals("Regular")) {
            typeface = Typeface.createFromAsset(context.getAssets(),
                    "fonts/Roboto-Regular.ttf");
        } else if (fontType.equals("Medium")) {
            typeface = Typeface.createFromAsset(context.getAssets(),
                    "fonts/Roboto-Medium.ttf");
        }

        Locale locale = Locale.ENGLISH;
        switch (language) {
            case CGlobalVariables.HINDI:
                locale = new Locale("hi");
                break;
            case CGlobalVariables.GUJARATI:
                typeface = CustomTypefacesForGujrati.get(context, "gu");
                locale = new Locale("gu");
                break;
            case CGlobalVariables.TELUGU:
                locale = new Locale("te");
                break;
            case CGlobalVariables.TAMIL:
                locale = new Locale("ta");
                break;
            case CGlobalVariables.KANNADA:
                locale = new Locale("kn");
                break;
            case CGlobalVariables.MARATHI:
                locale = new Locale("mr");
                break;
            case CGlobalVariables.MALAYALAM:
                locale = new Locale("ml");
                break;
            case CGlobalVariables.BANGALI:
                locale = new Locale("bn");
                break;
            case CGlobalVariables.PUNJABI:
                locale = new Locale("pa");
                break;
            case CGlobalVariables.ODIA:
                locale = new Locale("or");
                break;
            case CGlobalVariables.ASAMMESSE:
                locale = new Locale("as");
                break;
            case CGlobalVariables.SPANISH:
                locale = new Locale("es");
                break;
            case CGlobalVariables.FRENCH:
                locale = new Locale("fr");
                break;
            case CGlobalVariables.CHINESE:
                locale = new Locale("zh");
                break;
            case CGlobalVariables.JAPANESE:
                locale = new Locale("ja");
                break;
            case CGlobalVariables.PORTUGUESE:
                locale = new Locale("pt");
                break;
            case CGlobalVariables.GERMAN:
                locale = new Locale("de");
                break;
            case CGlobalVariables.ITALIAN:
                locale = new Locale("it");
                break;
        }
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());

        return typeface;
    }

    public static boolean isAppInstalled(Context context, String packageName) {
        try {
            context.getPackageManager().getApplicationInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    /**
     * @param pressedColor
     * @param backgroundDrawable
     * @return
     */
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public static RippleDrawable getBackgroundDrawable(int pressedColor, Drawable backgroundDrawable) {
        return new RippleDrawable(getPressedState(pressedColor), backgroundDrawable, null);

    }

    /**
     * @param pressedColor
     * @return
     */
    public static ColorStateList getPressedState(int pressedColor) {
        return new ColorStateList(new int[][]{new int[]{}}, new int[]{pressedColor});
    }


    public static Map<String, String> getFollowingAstroParams(Context context) {

        HashMap<String, String> headers = new HashMap<String, String>();
        if (context == null) return headers;
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(context));
        headers.put(CGlobalVariables.KEY_IAPI, "1");
        headers.put("userid", CUtils.getAppUserId(context));
        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(context));
        headers.put(CGlobalVariables.APP_VERSION, BuildConfig.VERSION_NAME);
        headers.put(CGlobalVariables.DEVICE_ID, CUtils.getMyAndroidId(context));
        return setRequiredParams(headers);
    }

    public static Map<String, String> getIsUserFollowingAstrologerParams(Context context, String astrologerId) {

        HashMap<String, String> headers = new HashMap<String, String>();
        if (context == null) return headers;
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(context));
        headers.put(CGlobalVariables.KEY_IAPI, "1");
        headers.put("userid", CUtils.getUserIdForBlock(context));
        headers.put("userphoneno", CUtils.getUserID(context));
        headers.put("countrycode", CUtils.getCountryCode(context));
        headers.put("astrologerid", astrologerId);
        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(context));
        headers.put(CGlobalVariables.APP_VERSION, BuildConfig.VERSION_NAME);
        headers.put(CGlobalVariables.DEVICE_ID, CUtils.getMyAndroidId(context));
        return setRequiredParams(headers);
    }

    public static Map<String, String> getLiveAstroParams(Context context, String serviceCallingFrom) {

        HashMap<String, String> headers = new HashMap<String, String>();
        if (context == null) return headers;
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(context));
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));

        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(context));
        headers.put(CGlobalVariables.APP_VERSION, BuildConfig.VERSION_NAME);
        headers.put(CGlobalVariables.DEVICE_ID, CUtils.getMyAndroidId(context));

        boolean isLogin = CUtils.getUserLoginStatus(context);
        try {
            if (isLogin) {
                headers.put(CGlobalVariables.PHONE_NO, CUtils.getUserID(context));
                headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(context));
            } else {
                headers.put(CGlobalVariables.PHONE_NO, "");
                headers.put(CGlobalVariables.COUNTRY_CODE, "");
            }
        } catch (Exception e) {
        }
        headers.put(CGlobalVariables.KEY_TESTING_ASTRO, CGlobalVariables.VAL_IS_TESTING_ASTRO);
        headers.put(CGlobalVariables.CALL_SOURCE, serviceCallingFrom);
        headers.put(CGlobalVariables.PREF_SECOND_FREE_CHAT, "" + CUtils.isSecondFreeChat(context));
        String offerType = CUtils.getCallChatOfferType(context);
        headers.put(CGlobalVariables.OFFER_TYPE, offerType);

        return headers;
    }

    public static Map<String, String> getSchedulesLiveAstroParams(Context context) {

        HashMap<String, String> headers = new HashMap<String, String>();
        if (context == null) return headers;
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(context));
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));

        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(context));
        headers.put(CGlobalVariables.APP_VERSION, BuildConfig.VERSION_NAME);
        headers.put(CGlobalVariables.DEVICE_ID, CUtils.getMyAndroidId(context));

        boolean isLogin = CUtils.getUserLoginStatus(context);
        try {
            if (isLogin) {
                headers.put(CGlobalVariables.PHONE_NO, CUtils.getUserID(context));
                headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(context));
            } else {
                headers.put(CGlobalVariables.PHONE_NO, "");
                headers.put(CGlobalVariables.COUNTRY_CODE, "");
            }
        } catch (Exception e) {
        }
        headers.put(CGlobalVariables.KEY_TESTING_ASTRO, CGlobalVariables.VAL_IS_TESTING_ASTRO);
        //Log.e("SAN Cutils ", "Live params  " + headers.toString());
        return headers;
    }

    public static void parseGiftList(String liveAstroData) {
        if (TextUtils.isEmpty(liveAstroData)) {
            return;
        }
        try {
            //Log.e("SAN ", " gift parselist " + liveAstroData);
            JSONObject jsonObject = new JSONObject(liveAstroData);
            String imageBaseUrl = jsonObject.getString("servicesimgbaseurl");
            JSONArray jsonArray = jsonObject.getJSONArray("giftservices");
            ArrayList<GiftModel> giftModelArrayList = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject object = jsonArray.getJSONObject(i);
                GiftModel giftModel = new GiftModel();
                giftModel.setServiceid(object.getString("serviceid"));
                giftModel.setServicename(object.getString("servicename"));
                giftModel.setSmalliconfile(object.getString("smalliconfile"));
                giftModel.setCategoryid(object.getString("categoryid"));
                giftModel.setRate(object.getString("rate"));
                giftModel.setRaters(object.getString("raters"));
                giftModel.setActualrate(object.getString("actualrate"));
                giftModel.setActualraters(object.getString("actualraters"));
                giftModel.setPaymentamount(object.getString("paymentamount"));
                giftModel.setOffermessage(object.getString("offermessage"));
                giftModel.setOffermessage(object.optString("offeramout"));
                giftModel.setIconcode(object.optString("iconcode"));
                giftModelArrayList.add(giftModel);
            }
            CUtils.setGiftModelArrayList(giftModelArrayList);
            CUtils.setGiftImageBaseUrl(imageBaseUrl);
        } catch (Exception e) {

        }
    }

    /**
     * @param astroUrlText
     * @return JSONObject of Astrologer details
     */

    public static JSONObject getAstrologerDetail(Context context, boolean isAIAstrologer, String astroUrlText) {
        JSONObject astrologerObj = null;
        try {
            JSONObject jsonObject;
            if (isAIAstrologer){
                jsonObject = new JSONObject(CUtils.getStringData(context, CGlobalVariables.KEY_AI_ASTROLOGER_LIST,""));
            }else {
                jsonObject = new JSONObject(CUtils.getAstroList());
            }
            //Log.e("followv"," : "+jsonObject);
            JSONArray jsonArray = jsonObject.getJSONArray("astrologers");
            if (jsonArray.length() > 0) {
                for (int i = 0; i < jsonArray.length(); i++) {
                    astrologerObj = jsonArray.getJSONObject(i);
                    String urlText = astrologerObj.getString("urlText");
                    if (urlText.equalsIgnoreCase(astroUrlText)) {
                        break;
                    } else {
                        astrologerObj = null;
                    }
                }
            }

        } catch (Exception e) {
        }
        return astrologerObj;
    }

    public static void setVerifiedAndOfferImage(String iuVerifiedSmall, String iuVerifiedLarge,
                                                String iuOfferSmall, String iuOfferLarge) {
        CGlobalVariables.VERIFIED_IMAGE_URL_SMALL = iuVerifiedSmall;
        CGlobalVariables.VERIFIED_IMAGE_URL_LARGE = iuVerifiedLarge;
        CGlobalVariables.OFFER_IMAGE_URL = iuOfferSmall;
        CGlobalVariables.OFFER_IMAGE_DETAIL_URL = iuOfferLarge;
    }


    public static String getVerfiedImageSmall() {
        return CGlobalVariables.VERIFIED_IMAGE_URL_SMALL;
    }

    public static String getVerfiedImageLarge() {
        return CGlobalVariables.VERIFIED_IMAGE_URL_LARGE;
    }

    public static String getOfferImageSmall() {
        return CGlobalVariables.OFFER_IMAGE_URL;
    }

    public static String getOfferImageLarge() {
        return CGlobalVariables.OFFER_IMAGE_DETAIL_URL;
    }

    public static String getAppUserId(Context context) {
        String userId = CUtils.getMyAndroidId(context);
        try {
            boolean isLogin = CUtils.getUserLoginStatus(context);
            if (isLogin) {
                userId = CUtils.getCountryCode(context) + "-" + CUtils.getUserID(context);
            }
        } catch (Exception e) {
        }
        return userId;
    }

    public static String getAudioMode(Context context) {
        String audioModeStr = "";
        try {
            AudioManager am = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
            int audioMode = am.getMode();
            switch (audioMode) {
                case -2: {
                    audioModeStr = "MODE_INVALID";
                    break;
                }
                case -1: {
                    audioModeStr = "MODE_CURRENT";
                    break;
                }
                case 0: {
                    audioModeStr = "MODE_NORMAL";
                    break;
                }
                case 1: {
                    audioModeStr = "MODE_RINGTONE";
                    break;
                }
                case 2: {
                    audioModeStr = "MODE_IN_CALL";
                    break;
                }
                case 3: {
                    audioModeStr = "MODE_IN_COMMUNICATION";
                    break;
                }
                case 4: {
                    audioModeStr = "MODE_CALL_SCREENING";
                    break;
                }
            }
        } catch (Exception e) {
            //
        }
        return audioModeStr;
    }

    public static String capitalizeString(String str) {
        String retStr = str;
        try { // We can face index out of bound exception if the string is null
            retStr = str.substring(0, 1).toUpperCase() + str.substring(1);
        } catch (Exception e) {
        }
        return retStr;
    }

    public static void clearAllSharedPreferences(Context context) {
        try {
            SharedPreferences sharedPreferences1 = context.getSharedPreferences(CGlobalVariables.APP_PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor sharedPref = sharedPreferences1.edit();
            sharedPref.clear();
            sharedPref.commit();

            SharedPreferences sharedPreferences = context.getSharedPreferences(
                    CGlobalVariables.APP_PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
            sharedPrefEditor.putString(CGlobalVariables.APP_PREFS_WALLET, "0.0");
            sharedPrefEditor.commit();

            SharedPreferences pSharedPref = context.getSharedPreferences(CGlobalVariables.USER_SELECTED_PROFILE_DATA, MODE_PRIVATE);
            SharedPreferences.Editor editor = pSharedPref.edit();
            editor.clear();
            editor.commit();

            SharedPreferences pSharedPref1 = context.getSharedPreferences(CGlobalVariables.PREF_PUSH_NOTIFICATION_TOKEN, MODE_PRIVATE);
            SharedPreferences.Editor editor1 = pSharedPref1.edit();
            editor1.clear();
            editor1.commit();

            SharedPreferences pSharedPref2 = context.getSharedPreferences(CGlobalVariables.PREFS_NAME, MODE_PRIVATE);
            SharedPreferences.Editor editor2 = pSharedPref2.edit();
            editor2.clear();
            editor2.commit();

            SharedPreferences pSharedPref3 = context.getSharedPreferences("MyProfilePicture", MODE_PRIVATE);
            SharedPreferences.Editor editor3 = pSharedPref3.edit();
            editor3.clear();
            editor3.commit();

            CUtils.saveAppStartSharedPrefrence(context);
            CUtils.saveBooleanData(context, CGlobalVariables.PREF_FIRST_INSTALL_APP_KEY, true);

            NotificationDBManager dbManager = new NotificationDBManager(context);
            dbManager.deleteBookmark();
        } catch (Exception e) {
            //
        }
    }

    public static boolean isAppRunning(final Context context) {
        try {
            final String packageName = getAppPackageName(context);
            final ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            final List<ActivityManager.RunningAppProcessInfo> procInfos = activityManager.getRunningAppProcesses();
            if (procInfos != null) {
                for (final ActivityManager.RunningAppProcessInfo processInfo : procInfos) {
                    if (processInfo.processName.equals(packageName)) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            //
        }
        return false;
    }

    public static long getApiLastHitTime() {

        return LIVE_ASTRO_API_LAST_HIT_TIME_MS;

    }

    public static void setApiLastHitTime() {

        LIVE_ASTRO_API_LAST_HIT_TIME_MS = System.currentTimeMillis();

    }

    public static long getApiLastHitAkHomeTime() {

        return CGlobalVariables.AK_HOME_API_LAST_HIT_TIME_MS;

    }

    public static void setApiLastHitAkHomeTime() {

        CGlobalVariables.AK_HOME_API_LAST_HIT_TIME_MS = System.currentTimeMillis();

    }

    public static long getCurrentTimeStamp() {

        return System.currentTimeMillis();

    }

    public static void subscribeFollowTopic(Context context, String astroId) {
        try {
            if (TextUtils.isEmpty(astroId)) return;
            //Toast.makeText(context, "subscribeFollowTopic-"+astroId, Toast.LENGTH_SHORT).show();
            unSubscribeFollowTopic(context, astroId);
            CUtils.subscribeTopics("", CGlobalVariables.TOPIC_FOLLOW_ASTRO + astroId, context);
        } catch (Exception e) {
            //Toast.makeText(context, "sf-"+e, Toast.LENGTH_SHORT).show();
        }
    }

    public static void unSubscribeFollowTopic(Context context, String astroId) {
        try {
            if (TextUtils.isEmpty(astroId)) return;
            //Toast.makeText(context, "unSubscribeFollowTopic-"+astroId, Toast.LENGTH_SHORT).show();
            CUtils.unSubscribeTopics("", CGlobalVariables.TOPIC_FOLLOW_ASTRO + astroId, context);
        } catch (Exception e) {
            //Toast.makeText(context, "use->"+e, Toast.LENGTH_SHORT).show();
        }
    }

    public static String toTitleCases(String input) {

        StringBuilder titleCase = new StringBuilder(input.length());
        boolean nextTitleCase = true;

        for (char c : input.toLowerCase().toCharArray()) {
            if (!Character.isLetterOrDigit(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }
            titleCase.append(c);
        }

        return titleCase.toString();
    }

    public static int convertDpToPx(Context context, int dip) {
        Resources r = context.getResources();
        float px = TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dip,
                r.getDisplayMetrics()
        );
        return (int) px;
    }

    public static int convertBytesToMB(int bytes) {
        return bytes / (1024 * 1024);
    }

    public static List<FilterData> getAstroFilterData() {
        List<FilterData> filterList = new ArrayList<>();
        FilterData filterData5 = new FilterData();
        filterData5.setItem("All");
        filterData5.setItemSelected(false);
        filterList.add(filterData5);
        FilterData filterData = new FilterData();
        filterData.setItem("Available");
        filterData.setItemSelected(false);
        filterList.add(filterData);
        FilterData filterData1 = new FilterData();
        filterData1.setItem("Call");
        filterData1.setItemSelected(false);
        filterList.add(filterData1);
        FilterData filterData2 = new FilterData();
        filterData2.setItem("Chat");
        filterData2.setItemSelected(false);
        filterList.add(filterData2);
        return filterList;
    }

    public static void startFollowerSubscriptionService(Context context) {
        String subscribeFollowDate = CUtils.getStringData(context, com.ojassoft.astrosage.utils.CGlobalVariables.FOLOW_TOPIC_SUBSCRIBE_DATE, "");
        String todayDate = com.ojassoft.astrosage.utils.CUtils.getDialogDate(com.ojassoft.astrosage.utils.CGlobalVariables.ONE_DAY);
        if (TextUtils.isEmpty(subscribeFollowDate) || !todayDate.equalsIgnoreCase(subscribeFollowDate)) {
            try {
                Intent intentService = new Intent(context, SubscribeTopicsOnLoginService.class);
                context.startService(intentService);
            } catch (Exception e) {
                //Toast.makeText(context, "subEx->"+e, Toast.LENGTH_LONG).show();
                com.ojassoft.astrosage.utils.CUtils.saveStringData(context, "subscribeAndUnSubscribe", "subEx->" + e);
            }
            CUtils.saveStringData(context, com.ojassoft.astrosage.utils.CGlobalVariables.FOLOW_TOPIC_SUBSCRIBE_DATE, todayDate);
        }
    }

    public static AstrologerDetailBean parseAstrologerObject(JSONObject object) {
        try {
            AstrologerDetailBean astrologerDetailBean = new AstrologerDetailBean();
            astrologerDetailBean.setName(object.getString("n"));
            astrologerDetailBean.setExperience(object.getString("ex"));
            astrologerDetailBean.setLanguage(object.getString("ln"));
            astrologerDetailBean.setDesignation(object.getString("dg"));
            astrologerDetailBean.setImageFile(object.getString("if"));
            astrologerDetailBean.setServicePrice(object.getString("sp"));
            astrologerDetailBean.setRating(object.getString("rc"));
            astrologerDetailBean.setEmail(object.getString("e"));
            astrologerDetailBean.setUrlText(object.getString("urlText"));
            astrologerDetailBean.setPhoneNumber(object.getString("ph"));
            astrologerDetailBean.setIsOnline(object.getString("io"));
            astrologerDetailBean.setIsBusy(object.getString("ib"));
            astrologerDetailBean.setDoubleRating(object.getString("r"));
            astrologerDetailBean.setAstroWalletId(object.getString("wi"));
            astrologerDetailBean.setAstrologerId(object.getString("ai"));
            astrologerDetailBean.setExpertise(object.getString("et"));
            astrologerDetailBean.setPrimaryExpertise(object.optString("pet"));
            astrologerDetailBean.setOfferRemaining(object.optBoolean("ioffc"));
            astrologerDetailBean.setUseIntroOffer(object.optBoolean("iof"));
            astrologerDetailBean.setFreeForCall(object.optBoolean("iofca"));
            astrologerDetailBean.setFreeForChat(object.optBoolean("iofch"));
            astrologerDetailBean.setIntroOfferType(userIntroOfferType);
            astrologerDetailBean.setTotalRating(object.getString("tr"));
            astrologerDetailBean.setFollowCount(object.getInt("fc"));
            astrologerDetailBean.setManipulatedRank(object.getInt("mr"));

            if (object.has("ct")) {
                astrologerDetailBean.setCatId(object.getString("ct"));
            }

            if (object.has("wt")) {
                astrologerDetailBean.setWaitTime(object.getString("wt"));
            }

            if (object.has("iv")) {
                astrologerDetailBean.setIsVerified(object.getString("iv"));
                //Log.d("setIsVerified","setIsVerified==>>>"+object.getString("iv"));

            }
            if (object.has("ifl")) {
                astrologerDetailBean.setImageFileLarge(object.getString("ifl"));
            }

            if (object.has("asp")) {
                astrologerDetailBean.setActualServicePriceInt(object.getString("asp"));
            }

            if (object.has("iavc")) {
                astrologerDetailBean.setIsAvailableForChat(object.getString("iavc"));
            }

            if (object.has("iavp")) {
                astrologerDetailBean.setIsAvailableForCall(object.getString("iavp"));
            }
            if (object.has("iav")) {
                astrologerDetailBean.setIsAvailableForVideoCall(object.getString("iav"));
            }
            if (object.has("ft")) {
                astrologerDetailBean.setFeatured(object.getBoolean("ft"));
            }

            if (object.has("rank")) {
                astrologerDetailBean.setRank(object.getString("rank"));
            }

            if (object.has("pt")) {
                astrologerDetailBean.setProfileTitle(object.getString("pt"));
            }

            astrologerDetailBean.setAcceptInternetCall(object.optInt("aic"));

            astrologerDetailBean.setAiAstrologerId(object.optString("aiai"));
            return astrologerDetailBean;
        } catch (Exception e) {
            //
            Log.d("testInternetCall", "aic ==Error >>>" + e);

        }
        return null;
    }


    public static LiveAstrologerModel parseLiveAstrologerObject(JSONObject object) {
        try {
            LiveAstrologerModel liveAstrologerModel = new LiveAstrologerModel();
            liveAstrologerModel.setId(object.getString("astrologerid"));
            liveAstrologerModel.setName(object.getString("astrologername"));
            liveAstrologerModel.setChannelName(object.getString("channelname"));
            liveAstrologerModel.setToken(object.getString("audiencetoken"));
            liveAstrologerModel.setExpertise(object.getString("expertise"));
            liveAstrologerModel.setProfileImgUrl(object.getString("imageurl"));
            liveAstrologerModel.setUrltext(object.getString("urltext"));
            liveAstrologerModel.setLiveaudioprice(object.getString("liveaudioprice"));
            liveAstrologerModel.setLiveaudiointroprice(object.getString("liveaudiointroprice"));
            liveAstrologerModel.setActualliveaudioprice(object.getString("actualliveaudioprice"));
            liveAstrologerModel.setExperience(object.optString("experience"));

            liveAstrologerModel.setLivevideoprice(object.getString("livevideoprice"));
            liveAstrologerModel.setLivevideointroprice(object.getString("livevideointroprice"));
            liveAstrologerModel.setActuallivevideoprice(object.getString("actuallivevideoprice"));

            liveAstrologerModel.setLiveanonymousprice(object.getString("liveanonymousprice"));
            liveAstrologerModel.setLiveanonymousintroprice(object.getString("liveanonymousintroprice"));
            liveAstrologerModel.setActualliveanonymousprice(object.getString("actualliveanonymousprice"));
            liveAstrologerModel.setIsavailableforcall(object.getString("isavailableforcall"));
            liveAstrologerModel.setPrivateIntroOffer(object.optBoolean("privateintrooffer"));


            return liveAstrologerModel;
        } catch (Exception e) {
            //
        }
        return null;
    }

    public static void switchToConsultTab(int filterType, Context context) {
        astroListFilterType = filterType;
        Intent intent = new Intent(context, DashBoardActivity.class);
        intent.putExtra(KEY_FILTER_TYPE, filterType);
        context.startActivity(intent);
    }

    public static void gotoAllLiveActivityFromLiveIcon(Context context) {
        Intent allLiveIntent = new Intent(context, AllLiveAstrologerActivity.class);
        allLiveIntent.putExtra("fromLiveIcon", true);
        context.startActivity(allLiveIntent);
    }

    public static void openAstrologerDetail(Context context, String urlText, boolean fromDashboard, boolean hasFollow, String link) {
        //Log.d("TestOpenAstroDes","openAstrologerDetail chat completed");
        boolean isFreechat = false;
        if(link != null && link.contains(CGlobalVariables.IS_FREECHAT_TRUE)){
            isFreechat = true;
        }
        Bundle bundle = new Bundle();
        try {
            bundle.putString("phoneNumber", "");
            bundle.putString("urlText", urlText);
            bundle.putBoolean("fromDashboard", fromDashboard);
            bundle.putBoolean("hasFollow", hasFollow);
            bundle.putBoolean("isFreechat", isFreechat);
            Intent intent = new Intent(context, AstrologerDescriptionActivity.class);
            intent.putExtras(bundle);
            context.startActivity(intent);
        } catch (Exception e) {

        }
    }

    public static void openAstrologerDetail(Context context, String urlText, String offerType, boolean fromDashboard, boolean hasFollow, String link) {
        //Log.d("TestOpenAstroDes","openAstrologerDetail chat completed");
        boolean isFreechat = false;
        if(link != null && link.contains(CGlobalVariables.IS_FREECHAT_TRUE)){
            isFreechat = true;
        }
        Bundle bundle = new Bundle();
        try {
            Log.e("TestChtNoti", "urlText="+urlText);
            bundle.putString("phoneNumber", "");
            bundle.putString("urlText", urlText);
            bundle.putBoolean("fromDashboard", fromDashboard);
            bundle.putBoolean("hasFollow", hasFollow);
            bundle.putString("offerType", offerType);
            bundle.putBoolean("isFreechat", isFreechat);
            Intent intent = new Intent(context, AstrologerDescriptionActivity.class);
            intent.putExtras(bundle);
            context.startActivity(intent);
        } catch (Exception e) {

        }
    }

    public static void shareLiveSession(Context context, LiveAstrologerModel liveAstrologerModel, Uri imageUri) {
        if (liveAstrologerModel == null) return;
        try {
            String astroUrl = CGlobalVariables.varta_astrosage_urls + CGlobalVariables.LINK_HAS_LIVE + liveAstrologerModel.getUrltext() + CGlobalVariables.PARTNER_ID_SHARE_LIVE;
            String body = (context.getResources().getString(R.string.share_live_text)).replace("#", liveAstrologerModel.getName());
            body = body + " " + astroUrl;

            shareDataToOtherApps(context, body, imageUri);
        } catch (Exception e) {
            //
        }
    }

    public static void shareAstrologer(Context context, AstrologerDetailBean astrologerDetailBean, Uri imageUri) {
        if (astrologerDetailBean == null) return;
        try {
            String astroUrl = CGlobalVariables.varta_astrosage_urls + CGlobalVariables.LINK_HAS_ASTROLOGER + astrologerDetailBean.getUrlText() + CGlobalVariables.PARTNER_ID_SHARE_ASTRO;
            String body = context.getString(R.string.share_astrologer_text, astrologerDetailBean.getName(), astroUrl, getUserFullName(context));

            shareDataToOtherApps(context, body, imageUri);
        } catch (Exception e) {
            //Log.d("onResponse",e.toString());
        }
    }

    public static void shareDataToOtherApps(Context context, String body, Uri imageUri) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        if (imageUri != null) {
            intent.setType("image/png");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(Intent.EXTRA_STREAM, imageUri);
        } else {
            intent.setType("text/plain");
        }
        try {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);

            context.startActivity(Intent.createChooser(intent, context.getResources().getString(R.string.share_astrologer)));
        } catch (android.content.ActivityNotFoundException ex) {
            //Log.d("onResponse",ex.toString());
        }
    }

    public static String removeAcharyaTarot(String astroName) {
        try {
            if (astroName != null && (astroName.contains("Acharya") || astroName.contains("Tarot") || astroName.contains("Numero") || astroName.contains("Astrologer") || astroName.contains("Acharyaa") || astroName.contains("Astro"))) {
                astroName = astroName.substring(astroName.indexOf(" "));
            }
        } catch (Exception e) {
            //
        }
        return astroName;
    }

    public static Uri getViewBitmap(Context context, View view) {
        try {
            Bitmap returnedBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
            Canvas canvas = new Canvas(returnedBitmap);
            /*Drawable bgDrawable = view.getBackground();
            if (bgDrawable != null) {
                bgDrawable.draw(canvas);
            } else {
                canvas.drawColor(Color.TRANSPARENT);
            }*/
            view.draw(canvas);
            File file = new File(context.getExternalCacheDir(), "share_media.png");
            FileOutputStream fOut = new FileOutputStream(file);
            returnedBitmap.compress(Bitmap.CompressFormat.PNG, 100, fOut);
            fOut.flush();
            fOut.close();
            return getUriForFile(context, "com.ojassoft.astrosage", file);
        } catch (Exception e) {
            //Log.d("onResponse","exception => " + e);
            return null;
        }
    }

    public static void showNotificationAlertDialog(Activity activity, AstrologerDetailBean astrologerDetailBean, FragmentManager fragmentManager) {
        NotificationAlertDialog notificationAlertDialog = new NotificationAlertDialog(activity, astrologerDetailBean);
        notificationAlertDialog.show(fragmentManager, "NotificationAlertDialog");
    }

    public static long getCurrentTimeMillisecAccordingToUTC() {
        try {
            String format = "dd-MM-yyyy hh:mm:ss a";
            Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            simpleDateFormat.setTimeZone(TimeZone.getTimeZone("Asia/Calcutta"));
            String utcDateTime = simpleDateFormat.format(date);
            //Log.d("CurrentDateTime", "utcDateTime="+utcDateTime);

            Date dateNew = simpleDateFormat.parse(utcDateTime);
            long millis = dateNew.getTime();
            //Log.d("CurrentDateTime", "millis="+millis);
            return millis;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public static ScheduleLiveAstroModel parseScheduleLiveAstrologerObject(JSONObject object) {
        try {
            ScheduleLiveAstroModel scheduleLiveAstroModel = new ScheduleLiveAstroModel();
            scheduleLiveAstroModel.setId(object.getString("prId"));
            scheduleLiveAstroModel.setName(object.getString("name"));
            scheduleLiveAstroModel.setDate(object.getString("date"));
            scheduleLiveAstroModel.setImage(object.getString("image"));
            scheduleLiveAstroModel.setTopic(object.getString("topic"));
            scheduleLiveAstroModel.setTime(object.getString("time"));
            scheduleLiveAstroModel.setUrl(object.getString("url"));


            return scheduleLiveAstroModel;
        } catch (Exception e) {
            //
        }
        return null;
    }

    public static void callBackgroundLogin(Context context) {
        try {
            CUtils.fcmAnalyticsEvents("bg_login_from_cutils", CGlobalVariables.VARTA_BACKGROUND_LOGIN, "");

            boolean isLogin = CUtils.getUserLoginStatus(context);
            if (isLogin) {
                Intent intentService = new Intent(context, Loginservice.class);
                context.startService(intentService);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateChatCallOfferType(Context context, boolean isClearAstroList, String type) {
        try {
            if (AstrosageKundliApplication.isEndChatReqOnGoing) {
                return;
            }
            String offerType = CUtils.getCallChatOfferType(context);
            if (!TextUtils.isEmpty(offerType)) {
                if (isClearAstroList) {
                    CUtils.saveAstroList("");
                    CUtils.saveAstroListWithLimit("");
                    CUtils.saveStringData(context, CGlobalVariables.KEY_AI_ASTROLOGER_LIST,"");
                }
                if (offerType.equalsIgnoreCase(INTRO_OFFER_TYPE_FREE)) {
                    //common events first or second free chat/call success
                    CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_SUCCESSFUL_FREE_CHAT_CALL, FIREBASE_FREE_CHAT_CALL_SUCCESS_EVENT, "");
                    CUtils.facebookAnalyticsEvents(GOOGLE_ANALYTICS_SUCCESSFUL_FREE_CHAT_CALL, FIREBASE_FREE_CHAT_CALL_SUCCESS_EVENT);

                    // Free offer taken event
                    if (type.equalsIgnoreCase(CHAT_CLICK)) {
                        CUtils.fcmAnalyticsEvents(FREE_OFFER_TAKEN, CHAT_CLICK, "");
                        //subscribe topic free chat taken
                        subscribeRegisteredFreeOrPaidTopic(context, 1);
                    } else {
                        CUtils.fcmAnalyticsEvents(FREE_OFFER_TAKEN, CALL_CLICK, "");
                    }
                    CUtils.saveFirstFreeOfferTakenDate(context, DateTimeUtils.currentDate());

                    //Firebase and Facebook event for first or second free chat/call
                    if(isSecondFreeChat(context)) {
                        CUtils.addFacebookAndFirebaseEvent(FIREBASE_SECOND_FREE_CHAT_CALL_SUCCESS_EVENT, GOOGLE_ANALYTICS_SUCCESSFUL_SECOND_FREE_CHAT_CALL, "");
                    } else {
                        CUtils.addFacebookAndFirebaseEvent(FIREBASE_FIRST_FREE_CHAT_CALL_SUCCESS_EVENT, GOOGLE_ANALYTICS_SUCCESSFUL_FIRST_FREE_CHAT_CALL, "");
                    }

                    //callLocalNotificationForNewUser(context);

                } else if ((offerType.contains(REDUCED_PRICE_OFFER))) {
                    CUtils.fcmAnalyticsEvents(offerType, "new_user_chat_call_success", "");
                    CUtils.facebookAnalyticsEvents(offerType, "new_user_chat_call_success");
                    // Free offer taken event
                    if (type.equalsIgnoreCase(CHAT_CLICK)) {
                        CUtils.fcmAnalyticsEvents(REDUCE_PRICE_OFFER_TAKEN, CHAT_CLICK, "");
                        // subscribe topic paid chat taken
                        subscribeRegisteredFreeOrPaidTopic(context, 2);
                    } else {
                        CUtils.fcmAnalyticsEvents(REDUCE_PRICE_OFFER_TAKEN, CALL_CLICK, "");
                    }
                    CUtils.saveReducePriceOfferTakenDate(context, DateTimeUtils.currentDate());

                } else {
                    CUtils.fcmAnalyticsEvents("paid_call_chat", "paid_chat_call_success", "");
                    CUtils.facebookAnalyticsEvents("paid_call_chat", "paid_chat_call_success");
                }
                CUtils.setUserOffers(context, CUtils.isLiveintrooffer(context), "");
                callBackgroundLogin(context);
            }
        } catch (Exception e) {
            //
        }
    }

    public static void stopChatServices(Context context) {
        if (CUtils.checkServiceRunning(OnGoingChatService.class)) {
            context.stopService(new Intent(context, OnGoingChatService.class));
        }
    }

    // SAN this method used for to send notification those user avail the free call or chat
    public static void callLocalNotificationForNewUser(Context ctx) {

        try {
            Bitmap icon = BitmapFactory.decodeResource(ctx.getResources(), R.mipmap.icon);
            Intent intent = new Intent(ctx, ActAppModule.class);
            intent.putExtra(com.ojassoft.astrosage.utils.CGlobalVariables.NOTIFICATION_TYPE,
                    com.ojassoft.astrosage.utils.CGlobalVariables.NEW_USER_RECHARGE_AFTER_FREE_CHAT_CALL);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            intent.setAction(Intent.ACTION_VIEW);
            intent.setData(Uri.parse(com.ojassoft.astrosage.varta.utils.CGlobalVariables.varta_astrosage_urls + "/rechargenow"));
            PendingIntent pendingIntent;
            if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                pendingIntent = PendingIntent.getActivity(ctx, 0, intent, PendingIntent.FLAG_IMMUTABLE);
            } else {
                pendingIntent = PendingIntent.getActivity(ctx, 0, intent, Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx, LibCGlobalVariables.NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_notification)
                    .setContentTitle(ctx.getString(R.string.local_noti_new_user_recharge_chat_call_title))
                    .setContentText(ctx.getString(R.string.local_noti_new_user_recharge_chat_call_desc))
                    .setSmallIcon(R.drawable.ic_notification)
                    .setLargeIcon(icon)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setContentIntent(pendingIntent)
                    .setChannelId(LibCGlobalVariables.NOTIFICATION_CHANNEL_ID)
                    .setAutoCancel(true)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(ctx.getString(R.string.local_noti_new_user_recharge_chat_call_desc)));

            NotificationManager notificationManager = createNotificationChannel(ctx);
            notificationManager.notify(LibCUtils.getRandomNumber(), builder.build());
            saveNotificationInLocalDb(ctx, ctx.getString(R.string.local_noti_new_user_recharge_chat_call_title),
                    ctx.getString(R.string.local_noti_new_user_recharge_chat_call_desc),
                    com.ojassoft.astrosage.varta.utils.CGlobalVariables.varta_astrosage_urls + "/rechargenow", "");

            com.ojassoft.astrosage.utils.CUtils.fcmAnalyticsEvents(com.ojassoft.astrosage.utils.CGlobalVariables.NEW_USER_RECHARGE_NOTIFICATION, com.ojassoft.astrosage.utils.CGlobalVariables.NOTIFICATION_RECEIVED, "");

        } catch (Exception e) {
            //
        }
    }

    private static NotificationManager createNotificationChannel(Context ctx) {
        NotificationManager mNotificationManager =
                (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(LibCGlobalVariables.NOTIFICATION_CHANNEL_ID, "horoscopenotification", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            //notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        return mNotificationManager;
    }

    private static void saveNotificationInLocalDb(Context ctx, String tit, String cont, String link, String imgurl) {

        Context context = ctx;
        if (context == null) return;
        NotificationModel notificationModel = new NotificationModel();
        notificationModel.setMessage(cont);
        notificationModel.setTitle(tit);
        notificationModel.setLink(link);
        notificationModel.setNtId("");
        notificationModel.setExtra("");
        notificationModel.setImgUrl("NEWUSERRECHARGE");
        notificationModel.setBlogId("");
        notificationModel.setNotificationType(NOTIFICATION_PUSH);
        notificationModel.setTimestamp(System.currentTimeMillis() + "");

        com.libojassoft.android.dao.NotificationDBManager dbManager = new com.libojassoft.android.dao.NotificationDBManager(context);
        dbManager.addNotification(notificationModel);

    }

    public static void setFcmAnalyticsByAge(int year, int month, int dayOfMonth) {
        try {
            int age = getAgeFromDOB(year, month, dayOfMonth);
            //Log.d("TestLog", "age="+age);
            if (age < 18) {
                com.ojassoft.astrosage.utils.CUtils.fcmAnalyticsEvents("age_less_18", CGlobalVariables.EVENT_USER_AGE, "");
            } else if (age >= 18 && age <= 24) {
                com.ojassoft.astrosage.utils.CUtils.fcmAnalyticsEvents("age_18_24", CGlobalVariables.EVENT_USER_AGE, "");
            } else if (age >= 25 && age <= 34) {
                com.ojassoft.astrosage.utils.CUtils.fcmAnalyticsEvents("age_25_34", CGlobalVariables.EVENT_USER_AGE, "");
            } else if (age >= 35 && age <= 44) {
                com.ojassoft.astrosage.utils.CUtils.fcmAnalyticsEvents("age_35_44", CGlobalVariables.EVENT_USER_AGE, "");
            } else if (age >= 45 && age <= 54) {
                com.ojassoft.astrosage.utils.CUtils.fcmAnalyticsEvents("age_45_54", CGlobalVariables.EVENT_USER_AGE, "");
            } else if (age >= 55 && age <= 64) {
                com.ojassoft.astrosage.utils.CUtils.fcmAnalyticsEvents("age_55_64", CGlobalVariables.EVENT_USER_AGE, "");
            } else if (age >= 65) {
                com.ojassoft.astrosage.utils.CUtils.fcmAnalyticsEvents("age_greater_64", CGlobalVariables.EVENT_USER_AGE, "");
            }
        } catch (Exception e) {
            //
        }
    }

    public static int getAgeFromDOB(int year, int month, int dayOfMonth) {
        try {
            GregorianCalendar cal = new GregorianCalendar();
            int y, m, d;
            int age;
            y = cal.get(Calendar.YEAR);
            m = cal.get(Calendar.MONTH);
            d = cal.get(Calendar.DAY_OF_MONTH);
            cal.set(year, month, dayOfMonth);
            age = y - cal.get(Calendar.YEAR);
            if ((m < cal.get(Calendar.MONTH))
                    || ((m == cal.get(Calendar.MONTH)) && (d < cal
                    .get(Calendar.DAY_OF_MONTH)))) {
                --age;
            }
            return age;
        } catch (Exception e) {
            //Log.d("TestLog", "exp="+e);
        }
        return 0;
    }

    public static int getRandomNumberByRange(int min, int max) {
        try {
            int random = new Random().nextInt((max - min) + 1) + min;
            return random;
        } catch (Exception e) {
            //
        }
        return 0;
    }

    public static void openProfileOrKundliAct(Activity activity, String url, String fromWhere, int requestCode) {
        UserProfileData userProfileData = getProfileForChatFromPreference(activity);
        int isKundliAvailable = com.ojassoft.astrosage.utils.CUtils.isLocalKundliAvailable(activity);
        if (userProfileData.getName().isEmpty() && isKundliAvailable == 0) { //0 means local kundli available //com.ojassoft.astrosage.utils.CUtils.isCompleteUserData(userProfileData)
            //OPEN SAVEDKUNDLILISTACTIVITY
            openSavedKundliList(activity, url, fromWhere, requestCode);
        } else {
            //OPEN PROFILEFORCHAT
            openProfileForChat(activity, url, fromWhere, null, true, requestCode);
        }
    }

    public static void openSavedKundliList(Activity activity, String url, String fromWhere, int requestCode) {
        Intent i = new Intent(activity, SavedKundliListActivity.class);
        String phone = getUserID(activity);
        i.putExtra("phoneNo", phone);
        i.putExtra("urlText", url);
        i.putExtra("fromWhere", fromWhere);
        activity.startActivityForResult(i, requestCode);
    }
    public static void openSavedKundliList(Activity activity,String fromWhere, int requestCode) {
        Intent i = new Intent(activity, SavedKundliListActivity.class);
        i.putExtra("fromWhere", fromWhere);
        activity.startActivityForResult(i, requestCode);
    }

    public static void openProfileForChat(Activity activity, String url, String fromWhere, Bundle bundle, boolean prefillData, int requestCode) {
        Intent i = new Intent(activity, ProfileForChat.class);
        String phone = getUserID(activity);
        i.putExtra("phoneNo", phone);
        i.putExtra("urlText", url);
        i.putExtra("fromWhere", fromWhere);
        i.putExtra("prefillData", prefillData);
        if (bundle != null) i.putExtras(bundle);
        activity.startActivityForResult(i, requestCode);
    }

    public static void openProfileOrKundliActFromFrag(Activity activity, String url, String fromWhere, Fragment fragment) {
        UserProfileData userProfileData = getProfileForChatFromPreference(activity);
        int isKundliAvailable = com.ojassoft.astrosage.utils.CUtils.isLocalKundliAvailable(activity);
        if (userProfileData.getName().isEmpty() && isKundliAvailable == 0) {
            //OPEN SAVEDKUNDLILISTACTIVITY
            openSavedKundliListFromFrag(activity, url, fromWhere, fragment);
        } else {
            //OPEN PROFILEFORCHAT
            openProfileForChatFromFrag(activity, url, fromWhere, null, true, fragment);
        }
    }

    public static void openSavedKundliListFromFrag(Activity activity, String url, String fromWhere, Fragment fragment) {
        Intent i = new Intent(activity, SavedKundliListActivity.class);
        String phone = getUserID(activity);
        i.putExtra("phoneNo", phone);
        i.putExtra("urlText", url);
        i.putExtra("fromWhere", fromWhere);
        fragment.startActivityForResult(i, DashBoardActivity.BACK_FROM_PROFILECHATDIALOG);
    }

    public static void openProfileForChatFromFrag(Activity activity, String url, String fromWhere, Bundle bundle, boolean prefillData, Fragment fragment) {
        Intent i = new Intent(activity, ProfileForChat.class);
        String phone = getUserID(activity);
        i.putExtra("phoneNo", phone);
        i.putExtra("urlText", url);
        i.putExtra("fromWhere", fromWhere);
        i.putExtra("prefillData", prefillData);
        if (bundle != null) i.putExtras(bundle);
        fragment.startActivityForResult(i, DashBoardActivity.BACK_FROM_PROFILECHATDIALOG);
    }

    public static void openAccountScreen(Context context) {
        boolean isLogin = false;
        isLogin = com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(context);
        if (!isLogin) {
            com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents(com.ojassoft.astrosage.varta.utils.CGlobalVariables.FBA_NAV_SIGNUP,
                    com.ojassoft.astrosage.varta.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
            Intent intent = new Intent(context, LoginSignUpActivity.class);
            intent.putExtra(com.ojassoft.astrosage.varta.utils.CGlobalVariables.IS_FROM_SCREEN, "");
            context.startActivity(intent);
        } else {

            com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents(com.ojassoft.astrosage.varta.utils.CGlobalVariables.FBA_NAV_RECHARGE,
                    com.ojassoft.astrosage.varta.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
            Intent intent = new Intent(context, MyAccountActivity.class);
            intent.putExtra(com.ojassoft.astrosage.varta.utils.CGlobalVariables.CALLING_ACTIVITY, "");
            context.startActivity(intent);

        }
    }

    public static boolean checkServiceRunning(Class<?> serviceClass) {
        try {
            ActivityManager manager = (ActivityManager) AstrosageKundliApplication.getAppContext().getSystemService(ACTIVITY_SERVICE);
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (serviceClass.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
        } catch (Exception e) {
            //
        }

        return false;
    }

    public static void saveInformation(Activity activity, String userId, String password, HashMap<String, String> profileInfo) {
        //Log.d("LoginFlow", " saveInformation()1");
        try {
            String userName = "", mobile = "", occupation = "", maritalStatus = "",
                    companyName = "", address1 = "", address2 = "", userPlanExpiryDate = "",
                    userPlanPurchaseDate = "", userPlanId = "1";

            if (profileInfo.containsKey(com.ojassoft.astrosage.utils.CGlobalVariables.USERID)) {
                userId = profileInfo.get(com.ojassoft.astrosage.utils.CGlobalVariables.USERID);
            }
            if (profileInfo.containsKey(com.ojassoft.astrosage.utils.CGlobalVariables.USER_FIRSTNAME)) {
                userName = profileInfo.get(com.ojassoft.astrosage.utils.CGlobalVariables.USER_FIRSTNAME);
            }
            if (profileInfo.containsKey(com.ojassoft.astrosage.utils.CGlobalVariables.MOBILE)) {
                mobile = profileInfo.get(com.ojassoft.astrosage.utils.CGlobalVariables.MOBILE);
            }
            if (profileInfo.containsKey(com.ojassoft.astrosage.utils.CGlobalVariables.OCCUPATION)) {
                occupation = profileInfo.get(com.ojassoft.astrosage.utils.CGlobalVariables.OCCUPATION);
            }
            if (profileInfo.containsKey(com.ojassoft.astrosage.utils.CGlobalVariables.MARITALSTATUS)) {
                maritalStatus = profileInfo.get(com.ojassoft.astrosage.utils.CGlobalVariables.MARITALSTATUS);
            }
            if (profileInfo.containsKey(com.ojassoft.astrosage.utils.CGlobalVariables.COMPANY_NAME)) {
                companyName = profileInfo.get(com.ojassoft.astrosage.utils.CGlobalVariables.COMPANY_NAME);
            }
            if (profileInfo.containsKey(com.ojassoft.astrosage.utils.CGlobalVariables.ADDRESS1)) {
                address1 = profileInfo.get(com.ojassoft.astrosage.utils.CGlobalVariables.ADDRESS1);
            }
            if (profileInfo.containsKey(com.ojassoft.astrosage.utils.CGlobalVariables.ADDRESS2)) {
                address2 = profileInfo.get(com.ojassoft.astrosage.utils.CGlobalVariables.ADDRESS2);
            }
            if (profileInfo.containsKey(com.ojassoft.astrosage.utils.CGlobalVariables.USERPLAN_ID)) {
                if (profileInfo.get(com.ojassoft.astrosage.utils.CGlobalVariables.USERPLAN_ID).length() > 0) {
                    userPlanId = profileInfo.get(com.ojassoft.astrosage.utils.CGlobalVariables.USERPLAN_ID);
                }
            }
            if (profileInfo.containsKey(com.ojassoft.astrosage.utils.CGlobalVariables.USER_PLAN_EXPIRY_DATE)) {
                userPlanExpiryDate = profileInfo.get(com.ojassoft.astrosage.utils.CGlobalVariables.USER_PLAN_EXPIRY_DATE);
            }
            if (profileInfo.containsKey(com.ojassoft.astrosage.utils.CGlobalVariables.USER_PLAN_PURCHASE_DATE)) {
                userPlanPurchaseDate = profileInfo.get(com.ojassoft.astrosage.utils.CGlobalVariables.USER_PLAN_PURCHASE_DATE);
            }

            com.ojassoft.astrosage.utils.CUtils.saveLoginDetailInPrefs(activity, userId, password, userName, true, false);
            // Clear old userid and password from old app
            SharedPreferences settings = activity.getSharedPreferences(
                    com.ojassoft.astrosage.utils.CGlobalVariables.MYKUNDLI_PREFS_NAME, 0);
            SharedPreferences.Editor editor = settings.edit();
            editor.putString(com.ojassoft.astrosage.utils.CGlobalVariables.PREF_USER_ID, "");
            editor.putString(com.ojassoft.astrosage.utils.CGlobalVariables.PREF_USERR_PWD, "");
            editor.commit();
            com.ojassoft.astrosage.model.GmailAccountInfo gmailAccountInfo = new com.ojassoft.astrosage.model.GmailAccountInfo();
            gmailAccountInfo.setId(userId);
            if (userName.equals("")) {
                gmailAccountInfo.setUserName(userId.split("@")[0]);
            } else {
                gmailAccountInfo.setUserName(userName);
            }

            gmailAccountInfo.setMobileNo(mobile);
            if (!TextUtils.isEmpty(occupation)) {
                gmailAccountInfo.setOccupation(Integer.parseInt(occupation));
            } else {
                gmailAccountInfo.setOccupation(0);
            }
            if (!TextUtils.isEmpty(maritalStatus)) {
                gmailAccountInfo.setMaritalStatus(Integer.parseInt(maritalStatus));
            } else {
                gmailAccountInfo.setMaritalStatus(0);
            }


            gmailAccountInfo.setHeading(companyName);
            gmailAccountInfo.setAddress1(address1);
            gmailAccountInfo.setAddress2(address2);
            com.ojassoft.astrosage.utils.CUtils.saveGmailAccountInfo(activity, gmailAccountInfo);
            com.ojassoft.astrosage.utils.CUtils.storeUserPurchasedPlanInPreference(activity, Integer.parseInt(userPlanId));

            com.ojassoft.astrosage.utils.CUtils.saveStringData(activity, com.ojassoft.astrosage.utils.CGlobalVariables.PREF_USER_PLAN_PURCHASE_DATE, userPlanPurchaseDate);//Purchase plan Date
            com.ojassoft.astrosage.utils.CUtils.saveStringData(activity, com.ojassoft.astrosage.utils.CGlobalVariables.PREF_USER_PLAN_Expiry_DATE, userPlanExpiryDate);//Expiry plan  Date


            int languageCode = ((AstrosageKundliApplication) activity.getApplication()).getLanguageCode();
            String regid = com.ojassoft.astrosage.utils.CUtils.getRegistrationId(activity.getApplicationContext());
            new ControllerManager().saveUserGCMRegistrationInformationOnOjasServer(activity.getApplicationContext(), regid, languageCode, userId);
            //Log.d("LoginFlow", " saveInformation()2");
        } catch (Exception e) {
            //
        }
    }

    public static MediaPlayer player = null;

    public static MediaPlayer playDefaultRingtone(Context context) {

        try {
            player = MediaPlayer.create(context,
                    Settings.System.DEFAULT_RINGTONE_URI);
            player.start();
        } catch (Exception e) {
            //
        }
        return player;
    }

    public static void stopDefaultRingtone(MediaPlayer player) {
        try {
            if (player != null) {
                player.release();
                player.reset();
            }
        } catch (Exception e) {
            //
        }
    }


    public static void openAstroSageHomeActivity(Context context) {
        try {
            Intent intent = new Intent(context, ActAppModule.class);
            context.startActivity(intent);
        } catch (Exception e) {
            //
        }
    }

    public static String getDeviceCountryCode(Context context) {
        try {
            String countryCode;
            TelephonyManager tm = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
            if (tm != null) {
                countryCode = tm.getSimCountryIso();

                if (countryCode != null && countryCode.length() == 2)
                    return countryCode.toLowerCase();

                if (tm.getPhoneType() == TelephonyManager.PHONE_TYPE_CDMA) {
                    countryCode = getCDMACountryIso();
                } else {
                    countryCode = tm.getNetworkCountryIso();
                }

                if (countryCode != null && countryCode.length() == 2)
                    return countryCode.toLowerCase();
            }

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                countryCode = context.getResources().getConfiguration().getLocales().get(0).getCountry();
            } else {
                countryCode = context.getResources().getConfiguration().locale.getCountry();
            }

            if (countryCode.length() == 2)
                return countryCode.toLowerCase();
        } catch (Exception e) {
            //
        }
        return "";
    }

    @SuppressLint("PrivateApi")
    private static String getCDMACountryIso() {
        try {
            Class<?> systemProperties = Class.forName("android.os.SystemProperties");
            Method get = systemProperties.getMethod("get", String.class);

            String homeOperator = ((String) get.invoke(systemProperties,
                    "ro.cdma.home.operator.numeric"));

            int mcc = Integer.parseInt(homeOperator.substring(0, 3));
            switch (mcc) {
                case 330:
                    return "PR";
                case 310:
                    return "US";
                case 311:
                    return "US";
                case 312:
                    return "US";
                case 316:
                    return "US";
                case 283:
                    return "AM";
                case 460:
                    return "CN";
                case 455:
                    return "MO";
                case 414:
                    return "MM";
                case 619:
                    return "SL";
                case 450:
                    return "KR";
                case 634:
                    return "SD";
                case 434:
                    return "UZ";
                case 232:
                    return "AT";
                case 204:
                    return "NL";
                case 262:
                    return "DE";
                case 247:
                    return "LV";
                case 255:
                    return "UA";
            }
        } catch (Exception e) {
        }
        return null;
    }

    public static void setChatInitiateLayout(Activity activity, AstrologerDetailBean astrologerDetailBean, String remTime, String internationalCharges, long remTimeLong) {
        try {
            View chatInitiateInfoLayout = activity.findViewById(R.id.chat_initiate_info_layout);
            // if( !AstrosageKundliApplication.initialiseChatView){
            //Log.d("testTimerView","setChatInitiateLayout called  00000000  ==>>>>  "+remTime);
            // AstrosageKundliApplication.initialiseChatView = true;
            CircularNetworkImageView chatAstroProfileImage = chatInitiateInfoLayout.findViewById(R.id.chatAstroProfileImage);
            TextView chatAstroName = chatInitiateInfoLayout.findViewById(R.id.chatAstroName);
            TextView chatOfferType = chatInitiateInfoLayout.findViewById(R.id.chatOfferType);
            TextView chatRuppeeSign = chatInitiateInfoLayout.findViewById(R.id.chatRuppeeSign);
            TextView chgatPriceTv1 = chatInitiateInfoLayout.findViewById(R.id.chgatPriceTv1);
            TextView chgatPriceTv2 = chatInitiateInfoLayout.findViewById(R.id.chgatPriceTv2);
            TextView initiateType = chatInitiateInfoLayout.findViewById(R.id.initiateType);
            TextView txt_chat_call_heading = chatInitiateInfoLayout.findViewById(R.id.txt_chat_call_heading);
            if (AstrosageKundliApplication.currentConsultType != null) {
                if (AstrosageKundliApplication.currentConsultType.equals(CGlobalVariables.CHAT_TEXT)) {
                    txt_chat_call_heading.setText(activity.getResources().getString(R.string.chat_is_being_connected));
                    initiateType.setText("( " + activity.getResources().getString(R.string.chat_now) + " )");
                } else if (AstrosageKundliApplication.currentConsultType.equals(CGlobalVariables.AUDIO_CALL_TEXT)) {
                    txt_chat_call_heading.setText(activity.getResources().getString(R.string.audio_call_is_being_connected));
                    initiateType.setText("( " + activity.getResources().getString(R.string.audio_call) + " )");
                } else if (AstrosageKundliApplication.currentConsultType.equals(CGlobalVariables.VIDEO_CALL_TEXT)) {
                    txt_chat_call_heading.setText(activity.getResources().getString(R.string.video_call_is_being_connected));
                    initiateType.setText("( " + activity.getResources().getString(R.string.video_call) + " )");
                }
            }
            TextView waitTime = chatInitiateInfoLayout.findViewById(R.id.waitTime);
            //Log.d("testAsmostThere","remTime"+remTime);
            waitTime.setText(remTime);
            ImageView cross_btn = chatInitiateInfoLayout.findViewById(R.id.cross_chat_initiate_view);
            String callingAstroImage = CGlobalVariables.IMAGE_DOMAIN + astrologerDetailBean.getImageFile();
            if (astrologerDetailBean.getImageFile() != null && astrologerDetailBean.getImageFile().length() > 0) {
                chatAstroProfileImage.setVisibility(View.VISIBLE);
                Glide.with(AstrosageKundliApplication.getAppContext()).load(callingAstroImage).circleCrop().placeholder(R.drawable.ic_profile_view).into(chatAstroProfileImage);
            } else chatAstroProfileImage.setVisibility(View.GONE);
            chatAstroName.setText(astrologerDetailBean.getName());
            int actualPrice = 0;
            if (astrologerDetailBean.getActualServicePriceInt() != null && astrologerDetailBean.getActualServicePriceInt().length() > 0) {
                actualPrice = Integer.parseInt(astrologerDetailBean.getActualServicePriceInt());
            }

            int servicePrice = 0;
            if (astrologerDetailBean.getServicePrice() != null && astrologerDetailBean.getServicePrice().length() > 0) {
                servicePrice = Integer.parseInt(astrologerDetailBean.getServicePrice());
            }

            if (actualPrice > servicePrice) {
                //show both prices
                String price = astrologerDetailBean.getActualServicePriceInt();
                chgatPriceTv1.setVisibility(View.VISIBLE);
                CUtils.setStrikeOnTextView(chgatPriceTv1, price);
                //chgatPriceTv1.setText(price);
                //chgatPriceTv1.setPaintFlags(chgatPriceTv1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                String quePrice = activity.getResources().getString(R.string.question_price).replace("$", astrologerDetailBean.getServicePrice());
                chgatPriceTv2.setVisibility(View.VISIBLE);
                chgatPriceTv2.setText(quePrice);
            } else {
                // show only one price(servicePriceInt)
                String quePrice = activity.getResources().getString(R.string.question_price).replace("$", astrologerDetailBean.getServicePrice());
                chgatPriceTv2.setVisibility(View.VISIBLE);
                chgatPriceTv2.setText(quePrice);
                chgatPriceTv1.setVisibility(View.GONE);
            }
            String offerType = CUtils.getCallChatOfferType(activity);
            boolean isOfferAvailable;
            if (AstrosageKundliApplication.currentConsultType.equals(CGlobalVariables.CHAT_TEXT)) {
                isOfferAvailable = astrologerDetailBean.isFreeForChat();
            } else {
                isOfferAvailable = astrologerDetailBean.isFreeForCall();
            }
            if (isOfferAvailable) {
                if (!TextUtils.isEmpty(offerType) && offerType.equals(CGlobalVariables.INTRO_OFFER_TYPE_FREE)) {
                    chatOfferType.setVisibility(View.VISIBLE);
                    chatOfferType.setText(activity.getResources().getString(R.string.text_free));
                    String quePrice = activity.getResources().getString(R.string.question_price).replace("$", astrologerDetailBean.getServicePrice());
                    CUtils.setStrikeOnTextView(chgatPriceTv1, quePrice);

                    //chgatPriceTv1.setText(quePrice);
                    //chgatPriceTv1.setPaintFlags(chgatPriceTv1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

                    chgatPriceTv1.setVisibility(View.VISIBLE);
                    chgatPriceTv2.setVisibility(View.GONE);
                    cross_btn.setVisibility(View.GONE);
                    if (remTimeLong < 119) {
                        cross_btn.setVisibility(View.VISIBLE);
                    } else {
                        cross_btn.setVisibility(View.GONE);
                    }
                } else {
                    chatOfferType.setVisibility(View.VISIBLE);
                    chatOfferType.setText(activity.getResources().getString(R.string.new_user));
                    cross_btn.setVisibility(View.VISIBLE);
                }
            } else {
                cross_btn.setVisibility(View.VISIBLE);
                chatOfferType.setVisibility(View.GONE);
                CUtils.setStrikeOnTextView(chgatPriceTv1, chgatPriceTv1.getText().toString());
                //chgatPriceTv1.setPaintFlags(chgatPriceTv1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }

            try {
                if (AstrosageKundliApplication.currentConsultType != null &&
                        !AstrosageKundliApplication.currentConsultType.equals(CGlobalVariables.CHAT_TEXT)) {
                    if (!TextUtils.isEmpty(internationalCharges) && !internationalCharges.equals("0.0")) {
                        TextView tvInternationalCallNote = chatInitiateInfoLayout.findViewById(R.id.tvInternationalCallNote);
                        tvInternationalCallNote.setVisibility(View.VISIBLE);
                        String tvText = activity.getResources().getString(R.string.international_call_note_internet);
                        tvInternationalCallNote.setText(tvText.replace("###", internationalCharges));
                    }
                }else {
                    TextView tvInternationalCallNote = chatInitiateInfoLayout.findViewById(R.id.tvInternationalCallNote);
                    tvInternationalCallNote.setVisibility(View.GONE);
                }
            } catch (Exception e) {
                //
            }

            chatInitiateInfoLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //
                }
            });
            chatAstroName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openAstroDetailPage(astrologerDetailBean.getUrlText(), activity);
                    // Log.d("TestClickListener","Called 3");
                }
            });
            chatAstroProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    openAstroDetailPage(astrologerDetailBean.getUrlText(), activity);
                    //Log.d("TestClickListener","Called 4");
                }
            });
//            }else {
//                //Log.d("testTimerView","setChatInitiateLayout called  0000001  ==>>>>  "+remTime);
//
//                TextView  waitTime = chatInitiateInfoLayout.findViewById(R.id.waitTime);
//                waitTime.setText(remTime);
//            }

        } catch (Exception e) {

        }

    }

    public static void openAstroDetailPage(String UrlText, Activity activity) {
        try {
            Intent intent1 = new Intent(activity, AstrologerDescriptionActivity.class);
            intent1.putExtra("phoneNumber", CUtils.getUserID(activity));
            intent1.putExtra("urlText", UrlText);
            intent1.putExtra("fromDashboard", true);
            activity.startActivity(intent1);
        } catch (Exception e) {

        }

    }

    public static void joinOngoingChatLayoutView(Activity activity, String remTime, String CHANNEL_ID, String chatJsonObject,
                                                 String astrologerName, String astrologerProfileUrl, String astrologerId,
                                                 String userChatTime, String chatinitiatetype) {
        View ongoingChatInfoLayout = activity.findViewById(R.id.join_ongoing_chat_layout);
        try {
            //if( !AstrosageKundliApplication.initialiseChatRunningView) {
            // Log.d("testTimerView", "setChatInitiateLayout called  00000002  ==>>>>  " + remTime);
            //   AstrosageKundliApplication.initialiseChatRunningView = true;
            CircularNetworkImageView ongoingChatAstroProfileImage = ongoingChatInfoLayout.findViewById(R.id.chatAstroProfileImage);
            TextView ongoingChatAstroName = ongoingChatInfoLayout.findViewById(R.id.chatAstroName);
            FontUtils.changeFont(activity, ongoingChatAstroName, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
            TextView ongoingChatRemTime = ongoingChatInfoLayout.findViewById(R.id.chatRemTime);
            Button joinOngoingChat = ongoingChatInfoLayout.findViewById(R.id.join_ongoing_chat);
            FontUtils.changeFont(activity, joinOngoingChat, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
            joinOngoingChat.setText(activity.getString(R.string.join_chat));
            ongoingChatAstroName.setText(astrologerName);
            String callingAstroImage = CGlobalVariables.IMAGE_DOMAIN + astrologerProfileUrl;
            if (astrologerProfileUrl != null && astrologerProfileUrl.length() > 0) {
                ongoingChatAstroProfileImage.setVisibility(View.VISIBLE);
                Glide.with(AstrosageKundliApplication.getAppContext()).load(callingAstroImage).circleCrop().placeholder(R.drawable.ic_profile_view).into(ongoingChatAstroProfileImage);
            } else ongoingChatAstroProfileImage.setVisibility(View.GONE);
            ongoingChatRemTime.setText(activity.getResources().getString(R.string.time) + " : " + remTime);
            ongoingChatInfoLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //
                }
            });
            ongoingChatAstroProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (AstrosageKundliApplication.selectedAstrologerDetailBean != null) {
                        if (AstrosageKundliApplication.selectedAstrologerDetailBean.getAstrologerId().equals(astrologerId)) {
                            openAstroDetailPage(AstrosageKundliApplication.selectedAstrologerDetailBean.getUrlText(), activity);
                        }
                    }
                    // Log.d("TestClickListener","Called 1");
                }
            });
            ongoingChatAstroName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (AstrosageKundliApplication.selectedAstrologerDetailBean != null) {
                        if (AstrosageKundliApplication.selectedAstrologerDetailBean.getAstrologerId().equals(astrologerId)) {
                            openAstroDetailPage(AstrosageKundliApplication.selectedAstrologerDetailBean.getUrlText(), activity);
                        }
                    }
                    //  Log.d("TestClickListener","Called 2");
                }
            });
            joinOngoingChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    joinOnGoingChatOnCLick(activity, chatinitiatetype, CHANNEL_ID, chatJsonObject,
                            astrologerName, astrologerId, astrologerProfileUrl, userChatTime, null);
                    /*AstrosageKundliApplication.currentChatStatus = CGlobalVariables.CHAT_RUNNING;
                    // Log.d("TestChatIssue","Data is ==>>"+CHANNEL_ID +"  ============="+chatJsonObject+" -----------------"+astrologerName+"----------"+astrologerProfileUrl+"------------"+astrologerId+"------------"+userChatTime);
                    Intent intent1;
                    if (chatinitiatetype.equals(CGlobalVariables.CHAT_INITIATE_TYPE_ASTROLOGER)) {
                        intent1 = new Intent(activity, ChatWindowActivity.class);
                    } else {
                        intent1 = new Intent(activity, AIChatWindowActivity.class);
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString(CGlobalVariables.CHAT_USER_CHANNEL, CHANNEL_ID);
                    bundle.putBoolean("isFromJoinButton", true);
                    bundle.putString("connect_chat_bean", chatJsonObject);
                    bundle.putString("astrologer_name", astrologerName);
                    bundle.putString("astrologer_profile_url", astrologerProfileUrl);
                    bundle.putString("astrologer_id", astrologerId);
                    bundle.putString("userChatTime", userChatTime);
                    intent1.putExtras(bundle);
                    activity.startActivity(intent1);*/
                }
            });
//            }else {
//               // Log.d("testTimerView", "setChatInitiateLayout called  00000003  ==>>>>  " + remTime);
//
//                TextView ongoingChatRemTime = ongoingChatInfoLayout.findViewById(R.id.chatRemTime);
//                ongoingChatRemTime.setText(activity.getResources().getString(R.string.time) + " : " + remTime);
//
//            }
        } catch (Exception e) {
            //Log.d("test_ongoing_chat","Error ==>>"+e);
        }


    }

    public static void joinOngoingCallLayoutView(Activity activity, String remTime, String agoraCallSId, String agoraToken,
                                                 String agoraTokenId, String astrologerName, String astrologerProfileUrl, String astrologerId,
                                                 String agoraCallDuration, String consultationType, boolean micStatus, boolean videoStatus) {
        View ongoingChatInfoLayout = activity.findViewById(R.id.join_ongoing_chat_layout);
        try {
            //if( !AstrosageKundliApplication.initialiseChatRunningView) {
            // Log.d("testTimerView", "setChatInitiateLayout called  00000002  ==>>>>  " + remTime);
            //   AstrosageKundliApplication.initialiseChatRunningView = true;
            CircularNetworkImageView ongoingChatAstroProfileImage = ongoingChatInfoLayout.findViewById(R.id.chatAstroProfileImage);
            TextView ongoingChatAstroName = ongoingChatInfoLayout.findViewById(R.id.chatAstroName);
            FontUtils.changeFont(activity, ongoingChatAstroName, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
            TextView ongoingChatRemTime = ongoingChatInfoLayout.findViewById(R.id.chatRemTime);
            FontUtils.changeFont(activity, ongoingChatRemTime, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
            Button joinOngoingChat = ongoingChatInfoLayout.findViewById(R.id.join_ongoing_chat);
            FontUtils.changeFont(activity, joinOngoingChat, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
            FontUtils.changeFont(activity, joinOngoingChat, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
            joinOngoingChat.setText(activity.getResources().getString(R.string.join_call));
            ongoingChatAstroName.setText(astrologerName);
            String callingAstroImage = CGlobalVariables.IMAGE_DOMAIN + astrologerProfileUrl;
            if (astrologerProfileUrl != null && astrologerProfileUrl.length() > 0) {
                ongoingChatAstroProfileImage.setVisibility(View.VISIBLE);
                Glide.with(AstrosageKundliApplication.getAppContext()).load(callingAstroImage).circleCrop().placeholder(R.drawable.ic_profile_view).into(ongoingChatAstroProfileImage);
            } else ongoingChatAstroProfileImage.setVisibility(View.GONE);
            ongoingChatRemTime.setText(activity.getResources().getString(R.string.time) + " : " + remTime);
            ongoingChatInfoLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //
                }
            });
            ongoingChatAstroProfileImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (AstrosageKundliApplication.selectedAstrologerDetailBean != null) {
                        if (AstrosageKundliApplication.selectedAstrologerDetailBean.getAstrologerId().equals(astrologerId)) {
                            openAstroDetailPage(AstrosageKundliApplication.selectedAstrologerDetailBean.getUrlText(), activity);
                        }
                    }
                    // Log.d("TestClickListener","Called 1");
                }
            });
            ongoingChatAstroName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (AstrosageKundliApplication.selectedAstrologerDetailBean != null) {
                        if (AstrosageKundliApplication.selectedAstrologerDetailBean.getAstrologerId().equals(astrologerId)) {
                            openAstroDetailPage(AstrosageKundliApplication.selectedAstrologerDetailBean.getUrlText(), activity);
                        }
                    }
                    //  Log.d("TestClickListener","Called 2");
                }
            });
            joinOngoingChat.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    AstrosageKundliApplication.currentChatStatus = CGlobalVariables.CHAT_RUNNING;
                    Intent resultIntent;
                    /*if(consultationType.equals(CGlobalVariables.TYPE_VIDEO_CALL)){
                        resultIntent = new Intent(com.ojassoft.astrosage.varta.utils.CGlobalVariables.INTENT_VIDEO_CALL_ACTIVITY);
                    }else{
                        resultIntent = new Intent(com.ojassoft.astrosage.varta.utils.CGlobalVariables.INTENT_VOICE_CALL_ACTIVITY);
                    }*/

                    if (consultationType.equals(CGlobalVariables.TYPE_VIDEO_CALL)) {
                        resultIntent = new Intent(activity, VideoCallActivity.class);
                    } else {
                        resultIntent = new Intent(activity, VoiceCallActivity.class);
                    }

                    Bundle bundle = new Bundle();
                    bundle.putBoolean(CGlobalVariables.AGORA_CALL_MIC_STATUS, micStatus);
                    bundle.putBoolean(CGlobalVariables.AGORA_CALL_VIDEO_STATUS, videoStatus);
                    bundle.putString(CGlobalVariables.AGORA_CALLS_ID, agoraCallSId);
                    bundle.putString(CGlobalVariables.AGORA_TOKEN, agoraToken);
                    bundle.putString(CGlobalVariables.AGORA_TOKEN_ID, agoraTokenId);
                    bundle.putString(CGlobalVariables.ASTROLOGER_NAME, astrologerName);
                    bundle.putString(CGlobalVariables.ASTROLOGER_PROFILE_URL, astrologerProfileUrl);
                    bundle.putString(CGlobalVariables.AGORA_CALL_DURATION, agoraCallDuration);
                    bundle.putString(CGlobalVariables.ASTROLOGER_ID, astrologerId);
                    bundle.putBoolean(CGlobalVariables.IS_FROM_RETURN_TO_CALL, true);
                    resultIntent.putExtras(bundle);
                    activity.startActivity(resultIntent);
                }
            });
//            }else {
//               // Log.d("testTimerView", "setChatInitiateLayout called  00000003  ==>>>>  " + remTime);
//
//                TextView ongoingChatRemTime = ongoingChatInfoLayout.findViewById(R.id.chatRemTime);
//                ongoingChatRemTime.setText(activity.getResources().getString(R.string.time) + " : " + remTime);
//
//            }
        } catch (Exception e) {
            //Log.d("test_ongoing_chat","Error ==>>"+e);
        }


    }

    public static void isDirectSecondFreeChat(String secondFreeChatType, Activity activity) {
        if (secondFreeChatType.equalsIgnoreCase(com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_FREE_CHAT_AI)) {
            //Toast.makeText(activity, " // AI direct Chat", Toast.LENGTH_SHORT).show();
            CUtils.initiateRandomAiChat(activity, CGlobalVariables.DIRECT_SECOND_FREE_CHAT_AI,com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_FREE_CHAT_AI);
        } else {
            //Toast.makeText(activity, " // Human direct Chat", Toast.LENGTH_SHORT).show();
            CUtils.initiateRandomChat(activity, CGlobalVariables.DIRECT_SECOND_FREE_CHAT_HUMAN, com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_FREE_CHAT_HUMAN);
        }
        AstrosageKundliApplication.isOpenVartaPopup = true;//ristrict to repited connect chat
    }

    public static void showConsultPremiumDialogNew(Activity activity) {
        if (CUtils.isAutoConsultationConnected) { //don't show popup in case of auto connect free chat
            //CUtils.isAutoChatConnectFromAds = false;
            AstrosageKundliApplication.isOpenVartaPopup = true;
            return;
        }
        boolean enabledAIFreeChatPopup = com.ojassoft.astrosage.utils.CUtils.isAIfreeChatPopupEnabled(activity);
        //String firstFreeChatType = getStringData(activity, com.ojassoft.astrosage.utils.CGlobalVariables.FIRST_FREE_CHAT_TYPE, "");
        String secondFreeChatType = getStringData(activity, com.ojassoft.astrosage.utils.CGlobalVariables.SECOND_FREE_CHAT_TYPE, "");
        String firstConsultType = getStringData(activity, com.ojassoft.astrosage.utils.CGlobalVariables.FIRST_CONSULT_TYPE,"");
        String offerType = CUtils.getCallChatOfferType(activity);
        if (enabledAIFreeChatPopup) {
            if (CUtils.isSecondFreeChat(activity) && offerType.equals(CGlobalVariables.INTRO_OFFER_TYPE_FREE)) {
                String directSecondFreeChat = getStringData(activity, com.ojassoft.astrosage.utils.CGlobalVariables.DIRECT_SECOND_FREE_CHAT, "");
                if (directSecondFreeChat.equals(ENABLED)) {
                    isDirectSecondFreeChat(secondFreeChatType, activity);
                } else {
                    if (secondFreeChatType.equalsIgnoreCase(com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_FREE_CHAT_AI)) {
                        showConsultPremiumAIFreeChatDialog(activity);
                    } else {
                        showConsultPremiumHumanFreeChatDialog(activity);
                    }
                }
            } else {
                if (firstConsultType.equals(com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_AI_CALL)
                        || firstConsultType.equals(com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_AI_CHAT)) {
                    showConsultPremiumAIFreeChatDialog(activity);
                } else {
                    showConsultPremiumHumanFreeChatDialog(activity);
                }
            }
        } else {
            showConsultPremiumHumanFreeChatDialog(activity);
        }
    }

    public static boolean checkisDirectSecondFreeChatAvailable(Activity activity) {
        boolean enabledAIFreeChatPopup = com.ojassoft.astrosage.utils.CUtils.isAIfreeChatPopupEnabled(activity);
        String offerType = CUtils.getCallChatOfferType(activity);
        if (enabledAIFreeChatPopup) {
            if (CUtils.isSecondFreeChat(activity) && offerType.equals(CGlobalVariables.INTRO_OFFER_TYPE_FREE)) {
                String directSecondFreeChat = getStringData(activity, com.ojassoft.astrosage.utils.CGlobalVariables.DIRECT_SECOND_FREE_CHAT, "");
                return directSecondFreeChat.equals(ENABLED);
            }
        }
        return false;
    }

    static AlertDialog aiFreeDialog;

    public static void showConsultPremiumAIFreeChatDialog(Activity activity) {
        try {
            if (aiFreeDialog != null && aiFreeDialog.isShowing()) {
                return;
            }
            AlertDialog aiFreeDialog = new PremiumAIFreeChatDialog(activity, "");
            aiFreeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            //activity.setFinishOnTouchOutside(false);
            aiFreeDialog.show();
            AstrosageKundliApplication.isOpenVartaPopup = true;

        } catch (Exception e) {
            e.printStackTrace();
            Log.d("DialogOnHomeScreen", "exp1" + e);
        }
    }


    public static void showConsultPremiumHumanFreeChatDialog(Activity activity) {
        //String showFreeCallChatPopupType = com.ojassoft.astrosage.utils.CUtils.getStringData(activity, com.ojassoft.astrosage.utils.CGlobalVariables.showFreeCallChatPopupType, com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_CALL);
        String firstConsultType = CUtils.getStringData(activity, com.ojassoft.astrosage.utils.CGlobalVariables.FIRST_CONSULT_TYPE,"");
        Button btnChatNow;
        ImageView cancelIv;
        TextView consultPremiumAstrologerTV, firstCallRsTV;
        try {
            ViewGroup viewGroup = activity.findViewById(android.R.id.content);
            View dialogView = LayoutInflater.from(activity).inflate(R.layout.consult_preminum_astrologer_new, viewGroup, false);
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setView(dialogView);
            AlertDialog alertDialog = builder.create();
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            alertDialog.setCancelable(false);
            consultPremiumAstrologerTV = dialogView.findViewById(R.id.consult_premium_astrologers_tv);
            firstCallRsTV = dialogView.findViewById(R.id.first_call_in_1rs_tv);
            cancelIv = dialogView.findViewById(R.id.cancel_tv);
            btnChatNow = dialogView.findViewById(R.id.btnChatNow);
            //activity.setFinishOnTouchOutside(false);
            FontUtils.changeFont(activity, consultPremiumAstrologerTV, com.ojassoft.astrosage.utils.CGlobalVariables.FONTS_ROBOTO_BOLD);
            FontUtils.changeFont(activity, firstCallRsTV, com.ojassoft.astrosage.utils.CGlobalVariables.FONTS_ROBOTO_BLACK);
            FontUtils.changeFont(activity, btnChatNow, com.ojassoft.astrosage.utils.CGlobalVariables.FONTS_ROBOTO_BOLD);

            String datee1 = com.ojassoft.astrosage.utils.CUtils.getDialogDate(com.ojassoft.astrosage.utils.CGlobalVariables.ONE_DAY);
            com.ojassoft.astrosage.utils.CUtils.saveSilverPlanDialogData(activity, true, datee1);
            com.ojassoft.astrosage.utils.CUtils.fcmAnalyticsEvents(com.ojassoft.astrosage.utils.CGlobalVariables.CONSULT_PREMIMUM_DIALOG_OPEN, com.ojassoft.astrosage.utils.CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");

            int callChatType;
            String callChatText = activity.getResources().getString(R.string.first_chat_call_free);

            if (firstConsultType.equals(com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_HUMAN_CHAT) || !com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(activity).equals(com.ojassoft.astrosage.utils.CGlobalVariables.COUNTRY_CODE_IND)) {
                callChatText = callChatText.replace("#", activity.getResources().getString(R.string.chat_now));
                btnChatNow.setText(activity.getResources().getString(R.string.txt_chat_now));
                callChatType = FILTER_TYPE_CHAT;
            } else {
                callChatText = callChatText.replace("#", activity.getResources().getString(R.string.call));
                btnChatNow.setText(activity.getResources().getString(R.string.call_now));
                callChatType = FILTER_TYPE_CALL;
            }
            firstCallRsTV.setText(callChatText);

            cancelIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    com.ojassoft.astrosage.utils.CUtils.fcmAnalyticsEvents(com.ojassoft.astrosage.utils.CGlobalVariables.CONSULT_PREMIMUM_DIALOG_CROSS_BTN_CLICK, com.ojassoft.astrosage.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                    String datee = com.ojassoft.astrosage.utils.CUtils.getDialogDate(com.ojassoft.astrosage.utils.CGlobalVariables.WEEK_DAYS);
                    com.ojassoft.astrosage.utils.CUtils.saveConsultPremimumDateData(activity, true, datee);
                    alertDialog.dismiss();
                }
            });
            btnChatNow.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    com.ojassoft.astrosage.utils.CUtils.createSession(activity, com.ojassoft.astrosage.utils.CGlobalVariables.CONSULT_PREMIMUM_DIALOG_CALL_BTN_PARTNER_ID);
                    com.ojassoft.astrosage.utils.CUtils.fcmAnalyticsEvents(com.ojassoft.astrosage.utils.CGlobalVariables.CONSULT_PREMIMUM_DIALOG_CALL_BTN_CLICK, com.ojassoft.astrosage.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                    String datee = com.ojassoft.astrosage.utils.CUtils.getDialogDate(com.ojassoft.astrosage.utils.CGlobalVariables.WEEK_DAYS);
                    com.ojassoft.astrosage.utils.CUtils.saveConsultPremimumDateData(activity, true, datee);


                    if (callChatType == FILTER_TYPE_CHAT) {
                        CUtils.initiateRandomChat(activity, CGlobalVariables.FREE_CHAT_PREMIUM_ASTROLOGER, com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_FREE_CHAT_HUMAN);
                    } else {
                        switchToConsultTab(FILTER_TYPE_CHAT, activity);
                    }

                    alertDialog.dismiss();
                }
            });

            alertDialog.show();
            AstrosageKundliApplication.isOpenVartaPopup = true;
        } catch (Exception e) {
            e.printStackTrace();
            Log.d("DialogOnHomeScreen", "exp1" + e);
        }

    }

    /** Initiate AI Random Chat
     * */
    public static void initiateRandomAiChat(Activity activity, String apiCallingSource, String configType) {
        AstrosageKundliApplication.selectedAstrologerDetailBean = null;
        if (!com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(activity)) {
            AstrosageKundliApplication.apiCallingSource = apiCallingSource;
            AstrosageKundliApplication.chatCallconfigType = configType;
            AstrosageKundliApplication.connectAiChatAfterLogin = true;
            Intent intent = new Intent(activity, LoginSignUpActivity.class);
            if (activity instanceof ActAppModule) {
                intent.putExtra(IS_FROM_SCREEN, LANGUAGE_SELECTION_SCRREN);
            }
            activity.startActivity(intent);
            return;
        }
        try {
            String offerType = CUtils.userOfferAfterLogin;
            if (TextUtils.isEmpty(offerType)) {
                offerType = CUtils.getCallChatOfferType(activity);
            }
            //Log.e("TestChat", "checkNextRechargeOffer offerType="+offerType);
            //Log.e("TestChatOffer", "offerType1="+offerType);
            if (!TextUtils.isEmpty(offerType) && offerType.equals(CGlobalVariables.INTRO_OFFER_TYPE_FREE)) {
                if (com.ojassoft.astrosage.varta.utils.CUtils.isChatNotInitiated()) {
                    ChatUtils.getInstance(activity).initAIChatRandomAstrologer(apiCallingSource, configType);
                } else {
                    Toast.makeText(activity, activity.getResources().getString(R.string.allready_in_chat), Toast.LENGTH_SHORT).show();
                }
            }
            CUtils.userOfferAfterLogin = "";
        } catch (Exception e) {
            //
        }
        // com.ojassoft.astrosage.varta.utils.CUtils.initiateAiRandomChat(this);
    }

    /** Initiate AI Random Call
     * */
    public static void initiateRandomAiCall(Activity activity, String apiCallingSource,String configType) {
        AstrosageKundliApplication.selectedAstrologerDetailBean = null;
        if (!com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(activity)) {
            AstrosageKundliApplication.apiCallingSource = apiCallingSource;
            AstrosageKundliApplication.chatCallconfigType = configType;
            AstrosageKundliApplication.connectAiCallAfterLogin = true;
            Intent intent = new Intent(activity, LoginSignUpActivity.class);
            if (activity instanceof ActAppModule) {
                intent.putExtra(IS_FROM_SCREEN, LANGUAGE_SELECTION_SCRREN);
            }
            activity.startActivity(intent);
            return;
        }
        try {
            String offerType = CUtils.userOfferAfterLogin;
            if (TextUtils.isEmpty(offerType)) {
                offerType = CUtils.getCallChatOfferType(activity);
            }

            if (!TextUtils.isEmpty(offerType) && offerType.equals(CGlobalVariables.INTRO_OFFER_TYPE_FREE)) {
                if (com.ojassoft.astrosage.varta.utils.CUtils.isAICallNotInitiated()) {
                    ChatUtils.getInstance(activity).initAICallRandomAstrologer(apiCallingSource, configType);
                } else {
                    Toast.makeText(activity, activity.getResources().getString(R.string.allready_in_call), Toast.LENGTH_SHORT).show();
                }
            }
            CUtils.userOfferAfterLogin = "";
        } catch (Exception e) {
            //
        }
        // com.ojassoft.astrosage.varta.utils.CUtils.initiateAiRandomChat(this);
    }
    /** Initiate Human Random Chat
     * */
    public static void initiateRandomChat(Activity activity, String apiCallingSource,String configType) {
        AstrosageKundliApplication.selectedAstrologerDetailBean = null;
        Log.e("TestChat", "initiateRandomChat()=" + com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(activity));
        if (!com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(activity)) {
            AstrosageKundliApplication.connectHumanChatAfterLogin = true;
            AstrosageKundliApplication.apiCallingSource = apiCallingSource;
            AstrosageKundliApplication.chatCallconfigType = configType;
            Intent intent = new Intent(activity, LoginSignUpActivity.class);
            if (activity instanceof ActAppModule) {
                intent.putExtra(IS_FROM_SCREEN, LANGUAGE_SELECTION_SCRREN);
            }
            activity.startActivity(intent);
            return;
        }
        try {
            String offerType = CUtils.userOfferAfterLogin;
            if (TextUtils.isEmpty(offerType)) {
                offerType = CUtils.getCallChatOfferType(activity);
            }
            Log.e("TestChat", "initiateRandomChat offerType=" + offerType);
            //Log.e("TestChatOffer", "offerType1="+offerType);
            if (!TextUtils.isEmpty(offerType) && offerType.equals(CGlobalVariables.INTRO_OFFER_TYPE_FREE)) {
                if (com.ojassoft.astrosage.varta.utils.CUtils.isChatNotInitiated()) {
                    ChatUtils.getInstance(activity).initChatRandomAstrologer(apiCallingSource, configType);
                } else {
                    Toast.makeText(activity, activity.getResources().getString(R.string.allready_in_chat), Toast.LENGTH_SHORT).show();
                }
            }
            CUtils.userOfferAfterLogin = "";
        } catch (Exception e) {
            //
        }
        // com.ojassoft.astrosage.varta.utils.CUtils.initiateAiRandomChat(this);
    }


    public static void initiateRandomCall(Activity activity, String apiCallingSource) {
        ChatUtils.getInstance(activity).initCallRandomAstrologer(apiCallingSource);

        try {
            String offerType = CUtils.getCallChatOfferType(activity);
            if (!TextUtils.isEmpty(offerType) && offerType.equals(CGlobalVariables.INTRO_OFFER_TYPE_FREE)) {
                if (com.ojassoft.astrosage.varta.utils.CUtils.isChatNotInitiated()) {
                    ChatUtils.getInstance(activity).initCallRandomAstrologer(apiCallingSource);
                } else {
                    Toast.makeText(activity, activity.getResources().getString(R.string.allready_in_chat), Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            //
        }
        // com.ojassoft.astrosage.varta.utils.CUtils.initiateAiRandomChat(this);
    }

    public static boolean isConnectedMobile(Context activity) {
        ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        return (info != null && info.isConnected() && info.getType() == ConnectivityManager.TYPE_MOBILE);
    }

    public static void setStrikeOnTextView(TextView textView, String text) {
        try {
            StrikethroughSpan STRIKE_THROUGH_SPAN = new StrikethroughSpan();
            textView.setText(text, TextView.BufferType.SPANNABLE);
            Spannable spannable = (Spannable) textView.getText();
            spannable.setSpan(STRIKE_THROUGH_SPAN, 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        } catch (Exception e) {
            //
        }
    }

    public static boolean isNetworkSpeedSlow(Context activity) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null) {
                //should check null because in airplane mode it will be null
                NetworkCapabilities nc = cm.getNetworkCapabilities(cm.getActiveNetwork());
                int downSpeed = nc.getLinkDownstreamBandwidthKbps();
                Log.d("testNetSpped", "downSpeed==>" + downSpeed);
                return downSpeed < 1500;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    public static int getNetworkSpeed(Context activity) {
        int downSpeed = 0;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = cm.getActiveNetworkInfo();
            if (netInfo != null) {
                //should check null because in airplane mode it will be null
                NetworkCapabilities nc = cm.getNetworkCapabilities(cm.getActiveNetwork());
                downSpeed = nc.getLinkDownstreamBandwidthKbps();
                //Log.e("testNetSpped", "downSpeed==>" + downSpeed);
            }
        }
        return downSpeed;
    }

    public static int getNetworkType(Context activity) {
        int networkType = -1;
        try {
            ConnectivityManager cm = (ConnectivityManager) activity.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();
            if (info != null) {
                networkType = info.getType();
            }
        } catch (Exception e) {
            //
        }
        return networkType;
    }


    public static void removeStrikeOnTextView(TextView textView) {
        try {
            if (textView instanceof Spannable) {
                Spannable spannable = (Spannable) textView.getText();
                spannable.removeSpan(Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        } catch (Exception e) {
            //
        }

    }

    public static String errorLogs = "";

    public static void openLiveActivity(Context context) {
        try {
            if(!CUtils.getUserLoginStatus(context)){
                Intent intent = new Intent(context, LoginSignUpActivity.class);
                intent.putExtra(CGlobalVariables.IS_FROM_SCREEN, CGlobalVariables.LIVESTREAMING_SCRREN);
                context.startActivity(intent);
            } else {
                Intent intent = new Intent(context, LiveActivityNew.class);
                context.startActivity(intent);
            }
        } catch (Exception e) {
            //
        }
    }

    public static void displayRequiredPermissionDialog(Activity activity) {
        try {
            Dialog dialogPermission = new Dialog(activity);
            dialogPermission.requestWindowFeature(Window.FEATURE_NO_TITLE);
            Window window = dialogPermission.getWindow();
            WindowManager.LayoutParams wlp = window.getAttributes();
            wlp.gravity = Gravity.CENTER;
            wlp.flags &= ~WindowManager.LayoutParams.FLAG_DIM_BEHIND;
            window.setAttributes(wlp);
            dialogPermission.setCancelable(false);
            dialogPermission.setContentView(R.layout.dialog_permission_required);
            dialogPermission.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
            Button cancel_btn = dialogPermission.findViewById(R.id.cancel_btn);
            Button ok_btn = dialogPermission.findViewById(R.id.ok_btn);
            cancel_btn.setOnClickListener(view -> {
                dialogPermission.dismiss();
            });
            ok_btn.setOnClickListener(view -> {
                try {
                    Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                    Uri uri = Uri.fromParts("package", activity.getPackageName(), null);
                    intent.setData(uri);
                    activity.startActivity(intent);
                    dialogPermission.dismiss();
                } catch (Exception e) {
                    //
                }
            });
            dialogPermission.show();
        } catch (Exception e) {
            //
        }
    }

    public static String getHavePermissionRecordAudio() {
        return AppDataSingleton.getInstance().getHavePermissionRecordAudio();

    }

    public static void setHavePermissionRecordAudio(String permission) {
        AppDataSingleton.getInstance().setHavePermissionRecordAudio(permission);
    }

    public static String getAppIntentCode(String qType) {
        String categoryCode = "";
        switch (qType) {
            case "chat_random_astrologer":
                categoryCode = "9";
                break;
            case "wallet":
                categoryCode = "10";
                break;
            case "history":
                categoryCode = "11";
                break;
            case "recharge":
                categoryCode = "12";
                break;
            case "premium_astrologers":
                categoryCode = "13";
                break;
            case "cheapest_astrologers":
                categoryCode = "14";
                break;
            case "top_rated_astrologers":
                categoryCode = "15";
                break;
            case "chat_last":
                categoryCode = "16";
                break;
            case "contact_astrologer":
                categoryCode = "17";
                break;
        }
        return categoryCode;
    }


    public static void sendLogDataRequest(String astrologerId, String channelID, String message) {
        ApiList api = RetrofitClient.getInstance().create(ApiList.class);
        Call<ResponseBody> call = api.logDataReq(getLogDataParams(astrologerId, channelID, message));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    String myResponse = response.body().string();
                    Log.e("TestLogData", " myResponse= " + myResponse);
                } catch (Exception e) {
                    //Log.e("TestLogData", " Exception= " + e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("TestLogData", " onFailure= " + t);
            }
        });
    }

    public static Map<String, String> getLogDataParams(String astrologerId, String channelId, String message) {
        HashMap<String, String> headers = new HashMap<String, String>();
        Context context = AstrosageKundliApplication.getAppContext();
        headers.put(CGlobalVariables.KEY_API, CUtils.getApplicationSignatureHashCode(context));
        headers.put("channelname", channelId);
        headers.put("astrologerid", astrologerId);
        headers.put("externalid", BuildConfig.APPLICATION_ID + "(" + BuildConfig.VERSION_NAME + ")");
        headers.put("logmsg", message);
        headers.put("callsid", "");

        boolean isLogin = CUtils.getUserLoginStatus(context);
        if (isLogin) {
            String userId = CUtils.getCountryCode(context) + CUtils.getUserID(context);
            headers.put("userid", userId);
        } else {
            headers.put("userid", "NA");
        }
        Log.e("TestLogData", " params= " + headers);
        return headers;
    }

    public static String getCurrentDate(int day, Context context) {
        Calendar calendar = Calendar.getInstance();
        String[] monthName = context.getResources().getStringArray(R.array.MonthName);
        if (day != 0) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        int date = calendar.get(Calendar.DATE);
        String month = monthName[calendar.get(Calendar.MONTH)];
        int year = calendar.get(Calendar.YEAR);

        return date + " " + month + ", " + year;
    }

    public static String removeZero(String time) {
        String timeStr = time;
        if (timeStr.startsWith("0")) {
            timeStr = timeStr.replaceFirst("0", "");
        }
        return timeStr;
    }

    public static boolean checkDate(Date date, String startDate, String endDate, String pattern) {
        boolean boolVal = false;
        boolVal = date.after(getDateFromString(startDate, pattern)) &&
                date.before(getDateFromString(endDate, pattern));
        return boolVal;
    }

    public static Date getDateFromString(String dateStr, String pattern) {
        Date date = null;
        SimpleDateFormat formatter = new SimpleDateFormat(pattern, Locale.US);
        try {
            date = formatter.parse(dateStr);
            System.out.println(date);
            System.out.println(formatter.format(date));

        } catch (ParseException e) {
            ////MyLogger.errorLog("MainActivity 16 - " + e.getMessage());
            e.printStackTrace();
        }
        return date;
    }

    public static String getColor(int langCode, int luckyNumber) {
        String luckyColor;
        if (langCode == CGlobalVariables.HINDI) {
            luckyColor = getLuckyColorInHindi(luckyNumber);
        } else {
            luckyColor = getLuckyColor(luckyNumber);
        }

        return luckyColor;
    }

    public static String getLuckyColor(int luckyNumber) {
        String[][] luckyColor = {{"Purple", "Smoke"}, {"Orange", "Gold"}, {"Silver", "White"},
                {"Saffron", "Yellow"}, {"Brown", "Grey"}, {"Green", "Turquoise"},
                {"Transparent", "Pink"}, {"Cream", "White"}, {"Black", "Blue"}, {"Red", "Maroon"}};
        return luckyColor[luckyNumber][getRandomNumber()];
    }

    public static String getLuckyColorInHindi(int luckyNumber) {
        String[][] luckyColor = {{"बैंगनी", "स्मोक"}, {"नारंगी", "सुनहरा"}, {"सिल्वर", "सफेद"},
                {"केसरिया", "पीला"}, {"भूरा", "सलेटी"}, {"हरा", "फिरोज़ी"},
                {"पारदर्शी", "गुलाबी"}, {"क्रीम", "सफेद"}, {"काला", "नीला"}, {"लाल", "मैरून"}};
        return luckyColor[luckyNumber][getRandomNumber()];
    }

    public static boolean isSupportUnicodeHindi() {
        boolean isAvailble = false;
        Locale[] local = Locale.getAvailableLocales();
        for (Locale locale : local) {
            if (locale.getLanguage().equalsIgnoreCase("hi")) {
                isAvailble = true;
            }
        }
        // ADDED BY BIJENDRA ON 23-JULY-13
        if (!isAvailble) {
            isAvailble = true;
        }
        return isAvailble;
    }

    public static String getCurrentTime(Context context) {
        Date currentDate = Calendar.getInstance().getTime();
        DateFormat df = new SimpleDateFormat("HH:mm");
        String currentTime = df.format(currentDate);
        String response = context.getResources().getString(R.string.current_time);
        String[] timeArray = currentTime.split(":");
        response = response.replace("#hour", removeZero(timeArray[0]));
        response = response.replace("#minute", removeZero(timeArray[1]));
        return response;
    }

    public static String extractYTId(String ytUrl) {
        String pattern = "(?<=youtu.be/|watch\\?v=|/videos/|embed\\/)[^#\\&\\?]*";
        Pattern compiledPattern = Pattern.compile(pattern);
        Matcher matcher = compiledPattern.matcher(ytUrl);
        if (matcher.find()) {
            return matcher.group();
        } else {
            return null;
        }
    }

    public static String getYoutubeUrl(String videoId) {
        return "https://i2.ytimg.com/vi/" + videoId + "/hqdefault.jpg";
    }

//    /**
//     *
//     * @param activity
//     */
//    public static void initiateAiRandomChat(Activity activity) {
//        if (!CUtils.isConnectedWithInternet(activity)) {
//            Toast.makeText(activity, activity.getResources().getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
//        } else {
//            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
//            Call<ResponseBody> call = api.aiChatInitiate(getAiChatParam(activity));
//            call.enqueue(new Callback<ResponseBody>() {
//                @Override
//                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
//                    //stopProgressBar();
//                    try {
//                        String myResponse = response.body().string();
//                        JSONObject jsonObject = new JSONObject("{\n" +
//                                "    \"status\": \"1\",\n" +
//                                "    \"n\": \"Love Oracle Kamini\",\n" +
//                                "    \"if\": \"/images/astrologer/ai-astro/kamini.png\",\n" +
//                                "    \"sp\": \"11\",\n" +
//                                "    \"asp\": \"15\",\n" +
//                                "    \"nurlt\": \"luvorcl-k\",\n" +
//                                "    \"urlt\": \"luvorcl-k\",\n" +
//                                "    \"wi\": \"13692\",\n" +
//                                "    \"et\": \"Vedic\",\n" +
//                                "    \"aiai\": \"0\"\n" +
//                                "}");
//                        Log.d("TestNewApi","myResponse==>>>>   "+jsonObject);
//                        String status = jsonObject.getString(CGlobalVariables.STATUS);
//                        if(status.equals(CGlobalVariables.STATUS_SUCESS)){
//                           AstrologerDetailBean astrologerDetailBean = new AstrologerDetailBean();
//                            astrologerDetailBean.setName(jsonObject.getString("n"));
//                            astrologerDetailBean.setImageFile(jsonObject.getString("if"));
//                            astrologerDetailBean.setAstroWalletId(jsonObject.getString("wi"));
//                            astrologerDetailBean.setUrlText(jsonObject.getString("urlt"));
//                            astrologerDetailBean.setCallSource("ActAppModule.class");
//                            astrologerDetailBean.setFreeForChat(true);
//                            astrologerDetailBean.setAiAstrologerId("12");
//                            astrologerDetailBean.setAstrologerId("13686");
//                            astrologerDetailBean.setAiAstrologerId(jsonObject.optString("aiai"));
//                            astrologerDetailBean.setAstrologerId(jsonObject.optString("ai"));
//                            if(CUtils.isChatNotInitiated() && !TextUtils.isEmpty(astrologerDetailBean.getAiAstrologerId())){
//                                ChatUtils.getInstance(activity).initAIChat(astrologerDetailBean);
//                            }else {
//                                Toast.makeText(activity, activity.getResources().getString(R.string.allready_in_chat), Toast.LENGTH_SHORT).show();
//                            }
//                        }
//
//                    } catch (Exception e) {
//                        Toast.makeText(activity, e+"", Toast.LENGTH_LONG).show();
//
//                        CUtils.fcmAnalyticsEvents("ai_chat_initiate_exception", AstrosageKundliApplication.currentEventType, "");
//                    }
//                }
//
//                @Override
//                public void onFailure(Call<ResponseBody> call, Throwable t) {
//                    //stopProgressBar();
//                   // callMsgDialogData(activity.getResources().getString(R.string.something_wrong_error), "", true, CGlobalVariables.CHAT_CLICK);
//                }
//            });
//        }
//    }

    private static Map<String, String> getAiChatParam(Activity activity) {
        HashMap<String, String> headers = new HashMap<String, String>();
        Context context = AstrosageKundliApplication.getAppContext();
        headers.put(CGlobalVariables.KEY_API, CUtils.getApplicationSignatureHashCode(context));
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));

        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(context));
        headers.put(CGlobalVariables.APP_VERSION, BuildConfig.VERSION_NAME);
        headers.put(CGlobalVariables.DEVICE_ID, CUtils.getMyAndroidId(context));

        boolean isLogin = CUtils.getUserLoginStatus(context);
        try {
            if (isLogin) {
                headers.put(CGlobalVariables.USER_PHONE_NO, CUtils.getUserID(context));
                headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(context));
            } else {
                headers.put(CGlobalVariables.USER_PHONE_NO, "");
                headers.put(CGlobalVariables.COUNTRY_CODE, "");
            }
        } catch (Exception e) {
        }
        return headers;
    }

    public static void getAiRandomChatAstroDetail(Activity activity) {
        try {
            Log.d("TestNewApi", "ApiCalled");
            Intent intentService = new Intent(activity, AIRandomChatAstroService.class);
            activity.startService(intentService);
        } catch (Exception e) {
            //
        }
    }


    public static boolean isAiAstrologer(AstrologerDetailBean astrologerDetailBean) {
        boolean isAiAstro = false;
        try {
            String aiAstroId = astrologerDetailBean.getAiAstrologerId();
            if (!TextUtils.isEmpty(aiAstroId) && !aiAstroId.equals("0")) {
                isAiAstro = true;
            }
        } catch (Exception e) {
            //
        }
        return isAiAstro;
    }

    public static boolean isAstroAITarot(Activity activity) {
        boolean isAiAndTarot = false;

        if (AstrosageKundliApplication.selectedAstrologerDetailBean != null) {
            String designation = AstrosageKundliApplication.selectedAstrologerDetailBean.getDesignation().toLowerCase();
            if (isAiAstrologer(AstrosageKundliApplication.selectedAstrologerDetailBean)) {
                isAiAndTarot = designation.equals(activity.getString(R.string.tarot));
            }
        }
        return isAiAndTarot;
    }

    public static String getCurrentTimeZone() {
        String offset = "";
        try {
            TimeZone tz = TimeZone.getDefault();
            Calendar cal = Calendar.getInstance(tz);
            int offsetInMillis = tz.getOffset(cal.getTimeInMillis());
            offset = String.valueOf(offsetInMillis / (1000.0 * 60 * 60));
        } catch (Exception e) {
            //Log.d("getCurrentTimeZone", "getCurrentTimeZone: "+e);
        }
        return offset;
    }

    public static void openCallList(Context context) {
        try {
            //boolean isAIChatDisplayed = com.ojassoft.astrosage.utils.CUtils.isAIChatDisplayed(context);
            /*if (isAIChatDisplayed) {
                CUtils.switchToConsultTab(CGlobalVariables.FILTER_TYPE_CHAT, context);//redirect to chat list
            } else {*/
                CUtils.switchToConsultTab(CGlobalVariables.FILTER_TYPE_CALL, context);//redirect to ai list
            //}
        } catch (Exception e) {
            //
        }
    }

    /**
     * open AI chat list or human chat list according to condition of ai enabled or disabled
     * @param context
     */
    public static void openAIChatList(Context context) {
        try {
            boolean isAIChatDisplayed = com.ojassoft.astrosage.utils.CUtils.isAIChatDisplayed(context);
            if (isAIChatDisplayed) {
                CUtils.switchToConsultTab(CGlobalVariables.FILTER_TYPE_CALL, context);//redirect to AI chat list
            } else {
                CUtils.switchToConsultTab(FILTER_TYPE_CHAT, context);//redirect to human chat list
            }
        } catch (Exception e) {
            //
        }
    }

    /**
     * play Notification Sound
     *
     * @param context
     * @param seconds
     */
    public static void playChatConnectSound(Context context, int seconds) {
        try {
            stopChatConnectSound(); //stop previous sound
            mChatConnectMediaPlayer = new MediaPlayer();
            mChatConnectMediaPlayer = MediaPlayer.create(context, R.raw.accept_tone);
            if (mChatConnectMediaPlayer != null) {
                mChatConnectMediaPlayer.setLooping(true);
                mChatConnectMediaPlayer.start();
            }
            new Timer().schedule(new TimerTask() {
                @Override
                public void run() {
                    stopChatConnectSound();
                }
            }, seconds * 1000L);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * stop sound
     */
    public static void stopChatConnectSound() {
        try {
            if (mChatConnectMediaPlayer != null) {
                mChatConnectMediaPlayer.stop();
                mChatConnectMediaPlayer.release();
                mChatConnectMediaPlayer = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean checkIsEmojiInString(String text) {
        Pattern pattern = Pattern.compile("[\\p{So}\\p{Cs}]");
        Matcher matcher = pattern.matcher(text);

        return matcher.find();
    }

    public static int getHourIn24HourFormat(int hour12, int am_pm) {
        if (am_pm == 1 && hour12 != 12) { // PM and not 12 PM
            return hour12 + 12;
        } else if (am_pm == 0 && hour12 == 12) { // 12 AM
            return 0;
        }
        return hour12;
    }

    public static void startRandomAIChatAfterAstroNoAnswer(Activity activity) {
        try {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        UserProfileData userProfileData = CUtils.getUserSelectedProfileFromPreference(activity);
                        ChatUtils.getInstance(activity).startAIChatRandom(userProfileData, CGlobalVariables.AI_FREE_CHAT_AFTER_ASTRO_NO_ANSWER);
                    } catch (Exception e) {
                        //
                    }
                }
            }, 500);
        } catch (Exception e) {
        }
    }

    public static void subscribeRegisteredFreeOrPaidTopic(Context context, int type) {
        try {
            if (type == 0) {//registered
                // subscribe topic registered user
                String offerType = CUtils.userOfferAfterLogin;
                if (TextUtils.isEmpty(offerType)) {
                    offerType = CUtils.getCallChatOfferType(context);
                }
                if (offerType.equals(CGlobalVariables.INTRO_OFFER_TYPE_FREE) && !isSecondFreeChat(context)) {//newly registered
                    CUtils.unSubscribeTopics("", com.ojassoft.astrosage.utils.CGlobalVariables.TOPIC_PAID_CHAT_TAKEN_USER, context);
                    CUtils.unSubscribeTopics("", com.ojassoft.astrosage.utils.CGlobalVariables.TOPIC_FREE_CHAT_TAKEN_USER, context);
                    CUtils.subscribeTopics("", com.ojassoft.astrosage.utils.CGlobalVariables.TOPIC_REGISTERED_USER, context);
                }
            } else if (type == 1) {//free chat taken
                // subscribe topic free chat taken
                CUtils.unSubscribeTopics("", com.ojassoft.astrosage.utils.CGlobalVariables.TOPIC_REGISTERED_USER, context);
                CUtils.unSubscribeTopics("", com.ojassoft.astrosage.utils.CGlobalVariables.TOPIC_PAID_CHAT_TAKEN_USER, context);
                CUtils.subscribeTopics("", com.ojassoft.astrosage.utils.CGlobalVariables.TOPIC_FREE_CHAT_TAKEN_USER, context);
            } else if (type == 2) {//paid chat taken
                // subscribe topic registered
                CUtils.unSubscribeTopics("", com.ojassoft.astrosage.utils.CGlobalVariables.TOPIC_REGISTERED_USER, context);
                CUtils.unSubscribeTopics("", com.ojassoft.astrosage.utils.CGlobalVariables.TOPIC_FREE_CHAT_TAKEN_USER, context);
                CUtils.subscribeTopics("", com.ojassoft.astrosage.utils.CGlobalVariables.TOPIC_PAID_CHAT_TAKEN_USER, context);
            }
        } catch (Exception e) {
            //
        }
    }

    public static void setEndChatButtonVisibilityTimer(Context context, String chatType) {

        try {
            String endChatButtonTimeInSecond;
            if (CUtils.isFreeChat) {
                if (chatType.equals(CGlobalVariables.CHAT_INITIATE_TYPE_ASTROLOGER_AI)) {
                    endChatButtonTimeInSecond = CUtils.getStringData(context, com.ojassoft.astrosage.utils.CGlobalVariables.AI_SHOW_END_CHAT_BUTTON_AFTER, "60");
                } else {
                    endChatButtonTimeInSecond = CUtils.getStringData(context, com.ojassoft.astrosage.utils.CGlobalVariables.HUMAN_SHOW_END_CHAT_BUTTON_AFTER, "60");
                }
            } else {
                if (chatType.equals(CGlobalVariables.CHAT_INITIATE_TYPE_ASTROLOGER_AI)) {
                    endChatButtonTimeInSecond = CUtils.getStringData(context, com.ojassoft.astrosage.utils.CGlobalVariables.AI_SHOW_PAID_END_CHAT_BUTTON_AFTER, "60");
                } else {
                    endChatButtonTimeInSecond = CUtils.getStringData(context, com.ojassoft.astrosage.utils.CGlobalVariables.HUMAN_SHOW_END_CHAT_BUTTON_AFTER, "60");
                }
            }

            // Log.e("AI_SHOW_END_CHAT_BUTTON_AFTER", " AI_SHOW_END_CHAT_BUTTON_AFTER= " + endChatButtonTimeInSecond);

            AstrosageKundliApplication.endChatTimeShowMilliSeconds = TimeUnit.SECONDS.toMillis(Long.parseLong(endChatButtonTimeInSecond));

            //Log.e("AI_SHOW_END_CHAT_BUTTON_AFTER", " AI_SHOW_END_CHAT_BUTTON_AFTER= " + AstrosageKundliApplication.endChatTimeShowMilliSeconds);

        } catch (Exception e) {
            //
        }
    }

    public static void chatStartWhenAstrologerAccept(Context context, Bundle bundle) {
        try {
            //Log.d("TestChatIssue","This method Called");
            if (!AstrosageKundliApplication.isChatScreenOpes) {
                Intent intent = new Intent(context, ChatWindowActivity.class);
                intent.putExtras(bundle);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent);
                AstrosageKundliApplication.isChatScreenOpes = true;
            }
        } catch (Exception e) {
            //Log.d("TestChatIssue","Exception e==>>"+e);
            AstrosageKundliApplication.isChatScreenOpes = false;
            //
        }

    }

    public static void updateChatBackgroundBasedOnTheme(Activity activity) {

        ImageView backgroundImage = activity.findViewById(R.id.iv_back_ground);
        View container = activity.findViewById(R.id.containerLayout);
        int currentNightMode = activity.getResources().getConfiguration().uiMode
                & Configuration.UI_MODE_NIGHT_MASK;

        switch (currentNightMode) {
            case Configuration.UI_MODE_NIGHT_NO:
                // Night mode is not active, we're using the light theme
                backgroundImage.setBackgroundResource(R.drawable.chat_bg); // Setting background to null for light theme
                container.setBackgroundResource(R.drawable.chat_bg); // Setting background to null for light theme
                break;
            case Configuration.UI_MODE_NIGHT_YES:
                // Night mode is active, we're using dark theme
                backgroundImage.setBackgroundResource(R.drawable.chat_bg_dark); // Setting background to null for light theme
                container.setBackgroundResource(R.drawable.chat_bg_dark); // Setting background to null for light theme

                break;
        }
    }


    public static void sendNotificationWithLink(Context context, String title, String msg, String link) {
        Intent resultIntent;
        resultIntent = new Intent(context, ActAppModule.class);
        resultIntent.setAction(Intent.ACTION_VIEW);
        resultIntent.setData(Uri.parse(link));
        Bitmap icon = BitmapFactory.decodeResource(context.getResources(), R.drawable.astrosage_app_icon);
        int notificationId = (int) System.currentTimeMillis();
        PendingIntent pending;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            pending = PendingIntent.getActivity(context, notificationId, resultIntent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            pending = PendingIntent.getActivity(context, notificationId, resultIntent, Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        NotificationCompat.Builder notificationBuilder =
                new NotificationCompat.Builder(context)

                        .setContentTitle(title)
                        .setContentText(msg)
                        .setSmallIcon(R.drawable.astrosage_app_icon)
                        .setLargeIcon(icon)
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setContentIntent(pending)
                        .setChannelId(LibCGlobalVariables.NOTIFICATION_CHANNEL_ID)
                        .setAutoCancel(true)
                        .setStyle(new NotificationCompat.BigTextStyle()
                                .bigText(msg));


        final NotificationManager mNotificationManager =
                (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(LibCGlobalVariables.NOTIFICATION_CHANNEL_ID, "upgrade_plan", NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.enableVibration(true);
            mNotificationManager.createNotificationChannel(notificationChannel);
        }
        mNotificationManager.notify(String.valueOf(System.currentTimeMillis()), 100001, notificationBuilder.build());
    }

    public static long getTimeFromMillis() {
        // Get the current time in milliseconds
        long currentTimeMillis = 0;
        //  String currentUTCTime = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            currentTimeMillis = Instant.now().toEpochMilli();
            // Convert milliseconds to Instant and format it to UTC
            //   Instant instant = Instant.ofEpochMilli(currentTimeMillis);
            //   currentUTCTime = DateTimeFormatter.ISO_INSTANT.format(instant);
        }
        // Log.e("SAN ", "getUTCTimeFromMillis " +currentTimeMillis+" "+currentUTCTime);
        return currentTimeMillis;
    }

    public static void joinOnGoingChatOnCLick(Activity activity, String chatinitiatetype, String CHANNEL_ID,
                                              String chatJsonObject, String astrologerName, String astrologerId,
                                              String astrologerProfileUrl, String userChatTime, String newQuestion) {
        AstrosageKundliApplication.currentChatStatus = CGlobalVariables.CHAT_RUNNING;
        // Log.d("TestChatIssue","Data is ==>>"+CHANNEL_ID +"  ============="+chatJsonObject+" -----------------"+astrologerName+"----------"+astrologerProfileUrl+"------------"+astrologerId+"------------"+userChatTime);
        Intent intent1;
        if (chatinitiatetype.equals(CGlobalVariables.CHAT_INITIATE_TYPE_ASTROLOGER)) {
            intent1 = new Intent(activity, ChatWindowActivity.class);
        } else {
            intent1 = new Intent(activity, AIChatWindowActivity.class);
        }
        Bundle bundle = new Bundle();
        bundle.putString(CGlobalVariables.CHAT_USER_CHANNEL, CHANNEL_ID);
        bundle.putBoolean("isFromJoinButton", true);
        bundle.putString("connect_chat_bean", chatJsonObject);
        bundle.putString("astrologer_name", astrologerName);
        bundle.putString("astrologer_profile_url", astrologerProfileUrl);
        bundle.putString("astrologer_id", astrologerId);
        bundle.putString("ai_astrologer_id", getSelectedAIAstrologerID(activity));
        bundle.putString("userChatTime", userChatTime);

        if (!TextUtils.isEmpty(newQuestion)) bundle.putString("newQuestion", newQuestion);

        intent1.putExtras(bundle);
        activity.startActivity(intent1);
    }

    public static void saveLastUsedAIAstrologerDetails(Context context, String aiAstroId, String name, String imageUrl) {
        try {
            saveStringData(context, "lastAiId", aiAstroId);
            saveStringData(context, "lastAiName", name);
            saveStringData(context, "lastAiImgUrl", imageUrl);
        } catch (Exception e) {
            Log.d("LastUsedAIAstro", "saveLastUsedAIAstrologerDetails e: " + e);
        }
    }

    /*
     * index 0 = id
     * index 1 = name
     * index 2 = image
     * */
    public static ArrayList<String> getLastUsedAIAstrologerDetails(Context context) {
        ArrayList<String> list = new ArrayList<>();
        try {
            list.add(getStringData(context, "lastAiId", CGlobalVariables.KEY_DEFAULT_AI_ASTRO_ID));
            list.add(getStringData(context, "lastAiName", CGlobalVariables.KEY_DEFAULT_AI_ASTRO_NAME));
            list.add(getStringData(context, "lastAiImgUrl", CGlobalVariables.KEY_DEFAULT_AI_ASTRO_PROFILE));
        } catch (Exception e) {
            Log.d("LastUsedAIAstro", "getLastUsedAIAstrologerDetails e: " + e);
        }
        return list;
    }

    public static void yogaServiceTask(Context context, String intentAction) {
        try {
            Constraints constraints = new Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build();

            Data.Builder data = new Data.Builder();
            data.putString("method_name", "getnoti");

            PeriodicWorkRequest yogaRequest =
                    new PeriodicWorkRequest.Builder(AINotificationWorker.class, 24, TimeUnit.HOURS)//24, TimeUnit.HOURS
                            .addTag(intentAction)
                            .setBackoffCriteria(BackoffPolicy.LINEAR, PeriodicWorkRequest.MIN_BACKOFF_MILLIS,
                                    TimeUnit.MILLISECONDS)
                            .setInitialDelay(delay("21:00"), TimeUnit.MINUTES)
                            .setConstraints(constraints)
                            .setInputData(data.build())
                            .build();
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(intentAction, ExistingPeriodicWorkPolicy.KEEP, yogaRequest);
        } catch (Exception e) {
            //
        }
    }

    public static void dailyRashifalTask(Context context, String intentAction) {
        try {
            Constraints constraints = new Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build();

            Data.Builder data = new Data.Builder();
            data.putString("method_name", "getpersonalizedhoroscope");

            PeriodicWorkRequest horoscopeRequest =
                    new PeriodicWorkRequest.Builder(AINotificationWorker.class, 24, TimeUnit.HOURS)
                            .addTag(intentAction)
                            .setBackoffCriteria(BackoffPolicy.LINEAR, PeriodicWorkRequest.MIN_BACKOFF_MILLIS,
                                    TimeUnit.MILLISECONDS)
                            .setInitialDelay(delay("23:59"), TimeUnit.MINUTES)
                            .setConstraints(constraints)
                            .setInputData(data.build())
                            .build();
            WorkManager.getInstance(context).enqueueUniquePeriodicWork(intentAction, ExistingPeriodicWorkPolicy.KEEP, horoscopeRequest);

        } catch (Exception e) {
            //
        }
    }

    public static long delay(String delayTime) {

        long minutes = 0;
        try {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
            Date date = simpleDateFormat.parse(delayTime); //21:00
            String time = new SimpleDateFormat("HH:mm").format(new java.util.Date());
            long timeInMilliseconds12 = date.getTime();
            Date dateObj = simpleDateFormat.parse(time);
            long timeInMilliseconds = dateObj.getTime();
            long millis = timeInMilliseconds12 - timeInMilliseconds;
            minutes = TimeUnit.MILLISECONDS.toMinutes(millis);

        } catch (Throwable e) {
            e.printStackTrace();
        }
        return minutes;
    }

    public static boolean isWorkScheduled(Context cntx, String tag) {
        WorkManager instance = WorkManager.getInstance(cntx);
        ListenableFuture<List<WorkInfo>> statuses = instance.getWorkInfosByTag(tag);
        try {
            boolean running = false;
            List<WorkInfo> workInfoList = statuses.get();
            for (WorkInfo workInfo : workInfoList) {
                WorkInfo.State state = workInfo.getState();
                running = state == WorkInfo.State.RUNNING || state == WorkInfo.State.ENQUEUED;
            }
            return running;
        } catch (ExecutionException e) {
            e.printStackTrace();
            return false;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static void removeAINotificationPeriodicTask(Context context) {
        try {
            WorkManager.getInstance(context).cancelAllWorkByTag("yogaService");
            WorkManager.getInstance(context).cancelAllWorkByTag("dailyRashifal");
        } catch (Exception e) {
            //
        }
    }

    public static void saveAIAstrologerID(Context context, String astrologerID) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                CGlobalVariables.APP_PREFS_ASTRO_CHANNEL, MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.putString(CGlobalVariables.APP_PREFS_AI_ASTROLOGER_ID,
                astrologerID.trim());
        sharedPrefEditor.apply();
    }

    public static String getSelectedAIAstrologerID(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                CGlobalVariables.APP_PREFS_ASTRO_CHANNEL, MODE_PRIVATE);
        return sharedPreferences.getString(CGlobalVariables.APP_PREFS_AI_ASTROLOGER_ID,
                "");
    }

    public static void saveGoogleFacebookProfile(Context context, String profileImage) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                CGlobalVariables.APP_PREFS_ASTRO_CHANNEL, MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.putString(CGlobalVariables.APP_PREFS_FACEBOOK_GOOGLE_IMAGE,
                profileImage.trim());
        sharedPrefEditor.apply();
    }

    public static String getGoogleFacebookProfile(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                CGlobalVariables.APP_PREFS_ASTRO_CHANNEL, MODE_PRIVATE);
        return sharedPreferences.getString(CGlobalVariables.APP_PREFS_FACEBOOK_GOOGLE_IMAGE,
                "");
    }
    public static void clearGoogleFacebookProfile(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                CGlobalVariables.APP_PREFS_ASTRO_CHANNEL, Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.remove(CGlobalVariables.APP_PREFS_FACEBOOK_GOOGLE_IMAGE);
        sharedPrefEditor.apply();
    }

    public static void openPurchaseDhruvPlanActivity(Context context, boolean isOpenForAiChat, boolean isFreeLimitExceed, String source, boolean isViewAsAd,boolean isAddFreeBottom) {
        try {
            Intent intent = new Intent(context, PurchaseDhruvPlanActivity.class);
            intent.putExtra(com.ojassoft.astrosage.utils.CGlobalVariables.OPEN_FOR_AI_CHAT, isOpenForAiChat);
            intent.putExtra(com.ojassoft.astrosage.utils.CGlobalVariables.FREE_LIMT_EXCEED, isFreeLimitExceed);
            intent.putExtra(com.ojassoft.astrosage.utils.CGlobalVariables.IS_OPEN_AS_AD, isViewAsAd);
            intent.putExtra(com.ojassoft.astrosage.utils.CGlobalVariables.IS_AD_FREE_BOTTOM, isAddFreeBottom);
            intent.putExtra("source", source);
            context.startActivity(intent);
        } catch (Exception e) {
            //
        }
    }
    public static void openPurchasePlanScreenForAd(Activity activity, boolean isOpenForAiChat, boolean isFreeLimitExceed, String source,boolean isViewAsAd,int requestCode) {
        try {
            com.ojassoft.astrosage.utils.CUtils.saveLongData(activity, com.ojassoft.astrosage.utils.CGlobalVariables.INTERSTITIAL_SESSION_TIME, System.currentTimeMillis());
            Intent intent = new Intent(activity, PurchaseDhruvPlanActivity.class);
            intent.putExtra(com.ojassoft.astrosage.utils.CGlobalVariables.OPEN_FOR_AI_CHAT, isOpenForAiChat);
            intent.putExtra(com.ojassoft.astrosage.utils.CGlobalVariables.FREE_LIMT_EXCEED, isFreeLimitExceed);
            intent.putExtra(com.ojassoft.astrosage.utils.CGlobalVariables.IS_OPEN_AS_AD, isViewAsAd);
            intent.putExtra("source", source);
            activity.startActivityForResult(intent, requestCode);
        } catch (Exception e) {
            //
        }
    }

    public static Resources getLocaleResources(Context context, String langCode) {
        Configuration conf = context.getResources().getConfiguration();
        conf = new Configuration(conf);
        conf.setLocale(Locale.forLanguageTag(normalizeResourceLocaleTag(langCode)));
        Context localizedContext = context.createConfigurationContext(conf);
        return localizedContext.getResources();
    }

    /**
     * Normalizes legacy app locale keys before Android resource resolution so older internal
     * language codes keep loading the intended `values-xx` catalogs.
     */
    private static String normalizeResourceLocaleTag(String langCode) {
        if ("ka".equals(langCode)) {
            return "kn";
        }
        return langCode;
    }

    public static String getLanguageForSpeechRecognize(int selectedLanguage) {
        String lang = "";
        switch (selectedLanguage) {
            case CGlobalVariables.ENGLISH:
                lang = Locale.US.toString();
                break;
            case CGlobalVariables.HINDI:
                lang = "hi-IN";
                break;
            case CGlobalVariables.FRENCH:
                lang = "fr-FR";
                break;
            default:
                lang = Locale.US.toString();
        }

        return lang;
    }

    public static String getDbNowDateAndTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new java.util.Date());
    }

    public static String getConvertDateFormatedForMessage(String inputDate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy HH:mm:ss a", Locale.getDefault());
        SimpleDateFormat outputTimeFormat = new SimpleDateFormat("hh:mm:ss a", Locale.getDefault());
        try {
            Date date = inputFormat.parse(inputDate);
            Date now = new Date();

            // Calculate the time difference in milliseconds
            long diff = now.getTime() - date.getTime();

            // Convert milliseconds to minutes and days
            long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
            long days = TimeUnit.MILLISECONDS.toDays(diff);

            if (days == 0) { // If within the same day
                if (minutes < 1) {
                    return "Just now";
                } else if (minutes < 10) {
                    return minutes + " minutes ago";
                } else {
                    return outputTimeFormat.format(date);
                }
            } else {
                // Return only the date for older timestamps
                return outputFormat.format(date);
            }
        } catch (ParseException e) {
            return inputDate; // Return the original string if parsing fails
        }
    }

    public static String getConvertDateFormat(String inputDate) {

        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        try {
            Date date = inputFormat.parse(inputDate);
            Date now = new Date();

            // Calculate the time difference in milliseconds
            long diff = now.getTime() - date.getTime();

            // Convert milliseconds to minutes and days
            long minutes = TimeUnit.MILLISECONDS.toMinutes(diff);
            long hours = TimeUnit.MILLISECONDS.toHours(diff);
            long days = TimeUnit.MILLISECONDS.toDays(diff);

            if (days == 0) { // If within the same day
                if (minutes < 1) {
                    return "Just now";
                } else if (minutes < 60) {
                    return minutes + " minutes ago";
                } else {
                    return hours + " hours ago";
                }
            } else {
                // Return only the date for older timestamps
                return outputFormat.format(date);
            }
        } catch (ParseException e) {
            return inputDate; // Return the original string if parsing fails
        }

    }

    private static String replaceMarkdown(String text, String regex, String replacement) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        return matcher.replaceAll(replacement);
    }

    public static String convertMarkdownToHTML(String text) {
        // Convert ###### Header 6 to <Strong>Header 6</Strong>
        text = replaceMarkdown(text, "(?m)^###### (.*?)$", "<strong>$1</strong>");
        // Convert ##### Header 5 to <Strong>Header 5</Strong>
        text = replaceMarkdown(text, "(?m)^##### (.*?)$", "<strong>$1</strong>");
        // Convert #### Header 4 to <Strong>Header 4</Strong>
        text = replaceMarkdown(text, "(?m)^#### (.*?)$", "<strong>$1</strong>");
        // Convert ### Header 3 to <Strong>Header 3</Strong>
        text = replaceMarkdown(text, "(?m)^### (.*?)$", "<strong>$1</strong>");
        // Convert ## Header 2 to <Strong>Header 2</Strong>
        text = replaceMarkdown(text, "(?m)^## (.*?)$", "<strong>$1</strong>");
        // Convert # Header 1 to <Strong>Header 1</Strong>
        text = replaceMarkdown(text, "(?m)^# (.*?)$", "<strong>$1</strong>");
        // Convert **bold** or __bold__ to <b>bold</b>
        text = replaceMarkdown(text, "(\\*\\*|__)(.*?)\\1", "<strong>$2</strong>");
        // Convert *italic* or _italic_ to <i>italic</i>
        text = replaceMarkdown(text, "(\\*|_)(.*?)\\1", "<em>$2</em>");
        return text;
    }

    public static void openFirstTimeProfileActivity(Activity activity, String configType, int requestCode) {
        Intent i = new Intent(activity, FirstTimeProfileDetailsActivity.class);
        String phone = getUserID(activity);
        i.putExtra("phoneNo", phone);
        i.putExtra("configType", configType);
        //i.putExtra("consultationType", consultationType);
        activity.startActivityForResult(i, requestCode);
    }

    public static List<ChatMessage> filterStatusMessage(List<ChatMessage> chatMessageList) {
        if (chatMessageList == null) return null;

        ArrayList<ChatMessage> list = new ArrayList<>();
        for (ChatMessage message : chatMessageList) {
            if (message instanceof StatusMessage) {
                continue;
            }
            list.add(message);
        }
        return list;
    }

    /**
     * This method is used to get firebase analytics instance id
     */
    public static void getFirebaseAnalyticsInstanceId(Context context) {
        FirebaseAnalytics.getInstance(context).getAppInstanceId()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (task.isSuccessful()) {
                            String appInstanceId = task.getResult();
                            CUtils.saveStringData(context, CGlobalVariables.APP_INSTANCE_ID, appInstanceId);
                            Log.e("FirebaseAnalytics", "App Instance ID: " + appInstanceId);
                        } else {
                            Log.e("FirebaseAnalytics", "Failed to get App Instance ID", task.getException());
                        }
                    }
                });
    }

    /**
     * This method is used to get firebase analytics app instance id from shared preference
     * in case of purchase event will add from server otherwise return empty
     * @param context
     * @return
     */
    public static String getFirebaseAnalyticsAppInstanceId(Context context) {
        if (isPurchaseEventAddedByServer(context)) {
            return CUtils.getStringData(context, CGlobalVariables.APP_INSTANCE_ID, "");
        } else {
            return "";
        }
    }

    /**
     * This method is used to get facebook analytics app instance id
     * in case of purchase event will add from server otherwise return empty
     * @param context
     * @return
     */
    public static String getFacebookAnalyticsAppInstanceId(Context context) {
        if(isPurchaseEventAddedByServer(context)) {
            return AppEventsLogger.getAnonymousAppDeviceGUID(context);
        } else {
            return "";
        }
    }

    /**
     * This method is used to get first time purchase value
     * in case of purchase event will add from server otherwise return false
     * @param context
     * @return
     */
    public static boolean getFirstTimePurchaseValue(Context context) {
        if (isPurchaseEventAddedByServer(context)) {
            return CUtils.getBooleanData(context, CGlobalVariables.IS_USER_FIRST_TIME_PURCHASE_IN_VARTA, true);
        } else {
            return false;
        }
    }

    public static String getPremiumPlanServiceDetail() {
        return AppDataSingleton.getInstance().getPremiumServiceDetails();
    }

    public static void setPremiumPlanServiceDetail(String serviceDetails) {
        AppDataSingleton.getInstance().setPremiumServiceDetails(serviceDetails);
    }
    public static String getPlusPlanServiceDetail() {
        return AppDataSingleton.getInstance().getPlusServiceDetails();
    }
    public static String getAiPassPlanServiceDetail() {
        return AppDataSingleton.getInstance().getAiPassServiceDetails();
    }

    public static void setAiPassPlanServiceDetail(String aiPassServiceDetails) {
        AppDataSingleton.getInstance().setAiPassServiceDetails(aiPassServiceDetails);
    }

    public static void setPlusPlanServiceDetail(String serviceDetails) {
        AppDataSingleton.getInstance().setPlusServiceDetails(serviceDetails);
    }

    /**
     * This method is used to get encoded referral url from static or shared preference
     * @param context
     * @return
     */
    public static String getEncodedReferralUrl(Context context) {
        String encodedRefurl = AppDataSingleton.getInstance().getEncodedReferralUrl();
        if(!TextUtils.isEmpty(encodedRefurl)){
            return encodedRefurl;
        }
        try {
            String refUrl = com.ojassoft.astrosage.utils.CUtils.getStringData(context, com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_REFERRER_URL, "");
            //Log.d("TestGCM", "refUrl --> " + refUrl);
            if (refUrl.contains(com.ojassoft.astrosage.varta.utils.CGlobalVariables.FB_REFERRER_TEXT)
                    || refUrl.contains(com.ojassoft.astrosage.varta.utils.CGlobalVariables.INSTA_REFERRER_TEXT)) {
                refUrl = AESDecryption.decryptFBReferrer(refUrl);
            }
            encodedRefurl = URLEncoder.encode(refUrl, "UTF-8");
            AppDataSingleton.getInstance().setEncodedReferralUrl(encodedRefurl);
        } catch (Exception e) {
        }
        return encodedRefurl;
    }

    public static Map<String, String> getRechargeParams(Context context, String mobileNo) {

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(context));
        headers.put(CGlobalVariables.USER_PHONE_NO, mobileNo);
        headers.put(CGlobalVariables.COUNTRY_CODE, CUtils.getCountryCode(context));
        headers.put("device_id", CUtils.getMyAndroidId(context));
        headers.put(CGlobalVariables.LANG,CUtils.getLanguageKey(LANGUAGE_CODE));
        String encodedRefurl = CUtils.getEncodedReferralUrl(context);
        headers.put(CGlobalVariables.KEY_REFERRER_URL.toLowerCase(), encodedRefurl);

        Log.e("TestPurchase params=>", "referrerUrl="+encodedRefurl);
        return CUtils.setRequiredParams(headers);
    }

    public static void shareDataToOtherAppsViaWhatsapp(Context context, String body, Uri imageUri) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, body);
        if (imageUri != null) {
            intent.setType("image/png");
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.putExtra(Intent.EXTRA_STREAM, imageUri);
        } else {
            intent.setType("text/plain");
        }
        try {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
            intent.setPackage("com.whatsapp");
            context.startActivity(Intent.createChooser(intent, context.getResources().getString(R.string.share_astrologer)));
        } catch (android.content.ActivityNotFoundException ex) {
            //Log.d("onResponse",ex.toString());
        }
    }
    public static boolean isReferralCodeValid(String referrerUrl) {
        if (referrerUrl.contains(com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_AS_POPUP_BTM) ||
                referrerUrl.contains(com.ojassoft.astrosage.varta.utils.CGlobalVariables.GOOGLE_REFERRER_TEXT) ||
                referrerUrl.contains(com.ojassoft.astrosage.varta.utils.CGlobalVariables.FB_REFERRER_TEXT) ||
                referrerUrl.contains(com.ojassoft.astrosage.varta.utils.CGlobalVariables.INSTA_REFERRER_TEXT) ||
                referrerUrl.contains(com.ojassoft.astrosage.varta.utils.CGlobalVariables.GOOGLE_ORGANIC_REFERRER_TEXT)) {
            return false;
        } else {
            return true;
        }

    }
    public static void setUserUniqueReferCode(Context context,  String useruniquerefercode) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                CGlobalVariables.APP_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.putString(CGlobalVariables.USER_UNIQUE_REFER_CODE, useruniquerefercode);
        sharedPrefEditor.commit();
    }

    public static String getUserUniqueReferCode(Context context) {
        if (context == null) return "";
        SharedPreferences sharedPreferences = context.getSharedPreferences(
                CGlobalVariables.APP_PREFS_NAME, MODE_PRIVATE);
        return sharedPreferences.getString(CGlobalVariables.USER_UNIQUE_REFER_CODE, "");
    }

    /**
     * Controls the visibility of customer service contact information based on user's wallet status.
     * The contact information is only shown if:
     * 1. User is logged in AND
     * 2. User has either:
     *    - A positive wallet balance OR
     *    - A recharge history
     *
     * If shown, the contact number may be overridden by a custom number from app settings.
     */
    public static boolean checkForCsNumberVisibility(Context context) {
//        if (context == null || csNumberView == null || tvCsNumber == null) {
//            Log.e("CUtils", "Invalid parameters provided to checkForCsNumberVisibility");
//            return;
//        }

        try {
            // Hide by default if user is not logged in
            if (!com.ojassoft.astrosage.utils.CUtils.isUserLogedIn(context)) {
                return false;
            }

            // Get wallet balance
            String walletAmountString = com.ojassoft.astrosage.varta.utils.CUtils.getWalletRs(context);
            if (TextUtils.isEmpty(walletAmountString)) {
                return false;
            }

            // Check if user has positive balance or recharge history
            double walletInRs = Double.parseDouble(walletAmountString);
            boolean hasRechargeHistory = com.ojassoft.astrosage.utils.CUtils.getBooleanData(context,
                    com.ojassoft.astrosage.varta.utils.CGlobalVariables.HAS_WALLET_RECHARGE_HISTORY, false);
            boolean is_subcriptin_failure = CUtils.getBooleanData(context, com.ojassoft.astrosage.utils.CGlobalVariables.IS_SUBSCRIPTION_ERROR, false);


            if (walletInRs > 0 || hasRechargeHistory || is_subcriptin_failure) {
                // Show contact info and update phone number if needed
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
           // Log.e("CUtils", "Unexpected error in checkForCsNumberVisibility: " + e.getMessage());
            return false;
        }
    }

    /**
     * Updates the contact number text with any custom number from app settings.
     * If a custom number is configured, it replaces the default number.
     *
     * @param context Application context
     * @param firstContactNumber TextView to update with the contact number
     */
    public static void updateContactNumber(Context context, TextView firstContactNumber) {
        if (context == null || firstContactNumber == null) {
            Log.e("CUtils", "Invalid parameters provided to updateContactNumber");
            return;
        }

        try {
            String defaultNumber = "+91-9560267006";
            String currentNumber = firstContactNumber.getText().toString();
            String customNumber = com.ojassoft.astrosage.utils.CUtils.getStringData(context, 
                    com.ojassoft.astrosage.utils.CGlobalVariables.key_Phone_One, defaultNumber);

            if (!TextUtils.isEmpty(customNumber) && currentNumber.contains(defaultNumber)) {
                firstContactNumber.setText(currentNumber.replace(defaultNumber, customNumber));
            }
        } catch (Exception e) {
            Log.e("CUtils", "Error updating contact number: " + e.getMessage());
        }
    }

    public static void SavePlaninPreference(Context context, int newPlanIndex) {
        SharedPreferences sharedPreferences = context
                .getSharedPreferences(com.ojassoft.astrosage.utils.CGlobalVariables.APP_PREFS_NAME,
                        Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        if (newPlanIndex == PLATINUM_PLAN) {
            sharedPrefEditor.putString(com.ojassoft.astrosage.utils.CGlobalVariables.APP_PREFS_Plan,
                    com.ojassoft.astrosage.utils.CGlobalVariables.PLATINUM_PLAN_VALUE_YEAR);
        } else if (newPlanIndex == PLATINUM_PLAN_MONTHLY) {
            sharedPrefEditor.putString(com.ojassoft.astrosage.utils.CGlobalVariables.APP_PREFS_Plan,
                    com.ojassoft.astrosage.utils.CGlobalVariables.PLATINUM_PLAN_VALUE_MONTH);
        }
        sharedPrefEditor.commit();
    }

    /**
     * Retrieves the recharge service details based on the selected position.
     *
     * @param selectedPos The position of the selected service.
     * @return WalletAmountBean.Services object containing the service details.
     */

    public static WalletAmountBean.Services getRechargeService(int selectedPos) {
        WalletAmountBean walletAmountBean = null;
        String response = CUtils.getWalletRechargeData();
        if (!TextUtils.isEmpty(response)) {
            try {
                walletAmountBean = new WalletAmountBean();
                WalletAmountBean.Services services;
                JSONObject jsonObject = new JSONObject(response);
                String gstRate = jsonObject.getString("gstrate");
                String dollarconversionrate = jsonObject.getString("dollarconversionrate");

                JSONArray jsonArray = jsonObject.getJSONArray("services");
                JSONObject innerJsonObject;
                ArrayList<WalletAmountBean.Services> servicesArrayList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    innerJsonObject = jsonArray.getJSONObject(i);
                    services = walletAmountBean.new Services();
                    String serviceid = innerJsonObject.getString("serviceid");
                    String servicename = innerJsonObject.getString("servicename");
                    String smalliconfile = innerJsonObject.getString("smalliconfile");
                    String categoryid = innerJsonObject.getString("categoryid");
                    String rate = innerJsonObject.getString("rate");
                    String raters = innerJsonObject.getString("raters");
                    String actualrate = innerJsonObject.getString("actualrate");
                    String actualraters = innerJsonObject.getString("actualraters");
                    String paymentamount = innerJsonObject.getString("paymentamount");
                    String offermessage = innerJsonObject.getString("offermessage");
                    String offerAmount = innerJsonObject.getString("offeramout");

                    services.setServiceid(serviceid);
                    services.setServicename(servicename);
                    services.setSmalliconfile(smalliconfile);
                    services.setCategoryid(categoryid);
                    services.setRate(rate);
                    services.setRaters(raters);
                    services.setActualrate(actualrate);
                    services.setActualraters(actualraters);
                    services.setPaymentamount(paymentamount);
                    services.setOffermessage(offermessage);
                    services.setOfferAmount(offerAmount);
                    servicesArrayList.add(services);
                }
                walletAmountBean.setGstrate(gstRate);
                walletAmountBean.setDollorConverstionRate(dollarconversionrate);
                walletAmountBean.setServiceList(servicesArrayList);
            } catch (Exception e) {
                walletAmountBean = null;
            }
        }
        WalletAmountBean.Services services = null;
        if(walletAmountBean != null) {
            services = walletAmountBean.getServiceList().get(selectedPos);
        }
        return services;
    }
    public static void startToGetAiPassSubService(Context context) {
        try {
            if( com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(context)) {
                Intent intent = new Intent(context, ServiceToGetIsAIPassSubscriber.class);
                context.startService(intent);
            }
        } catch (Exception e) {
            //
        }

    }

    public static void setUserDetailsInFirebaseAnalytics(Context context) {
        try {
            String userId = CUtils.getMyAndroidId(context);
            if(CUtils.getUserLoginStatus(context)) {
                userId = CUtils.getCountryCode(context) + CUtils.getUserID(context);
            }
            FirebaseAnalytics.getInstance(context).setUserId(userId);
            Log.e("TestFAData", " userId= " + userId);
            CUtils.fcmAnalyticsEvents("test_firebase_user", "test_analytics", "");
        } catch (Exception e) {
            //
        }

    }

    /**
     * Returns the appropriate string resource ID for AI Pass plan descriptions based on configuration.
     * <p>
     * This method checks the app's configuration (SHOW_AI_PASS_PLAN_KEY) to determine which AI Pass plan description
     * should be shown to the user. It selects between two sets of string resources depending on whether the AI Pass plan
     * is enabled or not, and whether the text is for the 'Talk Endlessly' section or the general AI chat section.
     *
     * @param context The context used to access shared preferences and resources.
     * @param isTalkEndlesslyText If true, returns the string resource for the 'Talk Endlessly' description; otherwise, returns the general AI chat description.
     * @return The resource ID of the string to display for the AI Pass plan description.
     */
        public static int getAIPassStringFromConfig(Context context, boolean isTalkEndlesslyText) { //isTalkEndlesslyText is boolean to decide the content because there are two text to show same content
            String showAIPassPlan = CUtils.getStringData(context, com.ojassoft.astrosage.utils.CGlobalVariables.SHOW_AI_PASS_PLAN_KEY,"0");
            if (showAIPassPlan.equals("0")) {
                return isTalkEndlesslyText ? R.string.unlimited_chat_with_ai_astrologer_desc_without_ai_astro: R.string.Unlimited_free_chat_with_kundli_ai;
            } else {
                return isTalkEndlesslyText ? R.string.unlimited_chat_with_ai_astrologer_desc : R.string.unlimited_chat_with_ai_and_any_astro;
            }
        }

    public static Map<String, String> getGreetingMessageParams(Context context,String aiAstrologerId,int AI_LANGUAGE_CODE,boolean isDynamicGreeting) {

        UserProfileData userProfileData = prepareUserProfile(context);
        HashMap<String, String> postDataParams = new HashMap<String, String>();

        try {
            postDataParams.put("name", userProfileData.getName());
            postDataParams.put("sex", userProfileData.getGender());
            postDataParams.put("day", userProfileData.getDay());
            postDataParams.put("month", String.valueOf(Integer.parseInt(userProfileData.getMonth())));
            postDataParams.put("year", userProfileData.getYear());
            postDataParams.put("hrs", userProfileData.getHour());
            postDataParams.put("min", userProfileData.getMinute());
            postDataParams.put("sec", userProfileData.getSecond());
            postDataParams.put("dst", "0");
            postDataParams.put("place", userProfileData.getPlace());
            postDataParams.put("longdeg", userProfileData.getLongdeg());
            postDataParams.put("longmin", userProfileData.getLongmin());
            postDataParams.put("longew", userProfileData.getLongew());
            postDataParams.put("latdeg", userProfileData.getLatdeg());
            postDataParams.put("latmin", userProfileData.getLatmin());
            postDataParams.put("latns", userProfileData.getLatns());
            postDataParams.put("timezone", userProfileData.getTimezone());
            postDataParams.put("ayanamsa", "0");
            postDataParams.put("charting", "0");
            postDataParams.put("kphn", "0");
            if (isDynamicGreeting) {
                postDataParams.put("methodname", "getselectedastrologerintrov2");
            } else {
                postDataParams.put("methodname", "getselectedastrologerintro");
            }
            postDataParams.put("oc", userProfileData.getOccupation());
            postDataParams.put("ms", userProfileData.getMaritalStatus());
            postDataParams.put("userid", CUtils.getCountryCode(context) + CUtils.getUserID(context));
            postDataParams.put("key", CUtils.getApplicationSignatureHashCode(context));
            postDataParams.put("androidid", CUtils.getMyAndroidId(context));
            postDataParams.put("aiastrologerid", aiAstrologerId);
            postDataParams.put("languagecode", String.valueOf(AI_LANGUAGE_CODE));
            postDataParams.put("isroman", "0");
            postDataParams.put("isnotbd", userProfileData.isBirthDetailsAvailable() ? "0" : "1");
            //postDataParams.put("cid",AIChatWindowActivity.CHANNEL_ID);
            postDataParams.put("pkgname", BuildConfig.APPLICATION_ID);
            postDataParams.put("appversion",BuildConfig.VERSION_NAME);
            postDataParams.put(CGlobalVariables.OFFER_TYPE,CUtils.getUserIntroOfferType(context));
            //postDataParams.put("isend", isEnd);
        } catch (Exception e) {
            //
        }
        //Log.d("GreetinParams", "Params: " + postDataParams.toString());
        return postDataParams;
    }

    /**
     * Saves a chat draft message for a specific astrologer or screen ID.
     * If the message is empty, it removes the entry for that ID.
     *
     * @param context           The application context.
     * @param astroIdOrScreenId The unique identifier for the astrologer or screen.
     * @param chatDraftMessage  The chat draft message to save.
     */
    public static void saveChatDraftMessage(Context context, String astroIdOrScreenId, String chatDraftMessage) {
        try {
            //Log.e("saveChatDraftMessage", "astroIdOrScreenId= " + astroIdOrScreenId + " chatDraftMessage= " + chatDraftMessage);
            HashMap<String, Object> draftMsgMap;
            String chatMessageDraftAll = CUtils.getStringData(context, CGlobalVariables.DRAFT_CHAT_TEXT, "");
            //Log.e("saveChatDraftMessage", "chatMessageDraftAll= " + chatMessageDraftAll);

            if (TextUtils.isEmpty(chatMessageDraftAll)) {
                draftMsgMap = new HashMap<>();
            } else {
                draftMsgMap = new Gson().fromJson(chatMessageDraftAll, new TypeToken<HashMap<String, Object>>() {}.getType());
            }
            if (!TextUtils.isEmpty(chatDraftMessage)) {
                draftMsgMap.put(astroIdOrScreenId, chatDraftMessage);
            } else {
                draftMsgMap.remove(astroIdOrScreenId);
            }
            String json = new Gson().toJson(draftMsgMap);
            Log.e("saveChatDraftMessage", "draftMsgMap= " + json);
            CUtils.saveStringData(context, CGlobalVariables.DRAFT_CHAT_TEXT, json);
        } catch (Exception e) {
            Log.e("saveChatDraftMessage", "Exception= " + e.getMessage());
        }
    }

    /**
     * Retrieves a chat draft message for a specific astrologer or screen ID.
     * If no message is found, it returns an empty string.
     *
     * @param context           The application context.
     * @param astroIdOrScreenId The unique identifier for the astrologer or screen.
     * @return The chat draft message, or an empty string if not found.
     */
    public static String getChatDraftMessage(Context context, String astroIdOrScreenId) {
        try {
            String chatMessageDraftAll = CUtils.getStringData(context, CGlobalVariables.DRAFT_CHAT_TEXT, "");
            if (TextUtils.isEmpty(chatMessageDraftAll)) {
                return "";
            }
            //Log.e("saveChatDraftMessage", astroIdOrScreenId+" chatMessageDraftAll2= " + chatMessageDraftAll);
            HashMap<String, String> draftMsgMap = new Gson().fromJson(chatMessageDraftAll, new TypeToken<HashMap<String, Object>>() {}.getType());
            //Log.e("saveChatDraftMessage", "draftMsgMap= " + draftMsgMap);
            String chatDraftMessage = draftMsgMap.get(astroIdOrScreenId);
            if (chatDraftMessage == null) {
                return "";
            } else {
                return chatDraftMessage;
            }
        } catch (Exception e) {
            Log.e("saveChatDraftMessage", "Exception2= " + e);
            return "";
        }
    }

    /**
     * Extracts the substring that appears before the first occurrence of a hyphen ('-')
     * in the given input string.
     *
     * <p>Examples:
     * <ul>
     *   <li>Input: "aicall-aichat" → Output: "aicall"</li>
     *   <li>Input: "aichat" → Output: "aichat"</li>
     * </ul>
     *
     * @param input the string from which the substring is to be extracted
     * @return the substring before the first hyphen if present;
     *         otherwise returns the original string
     */
    public static String getStringBeforeHyphen(String input) {
        if (input == null || !input.contains("-")) {
            return input;
        }
        return input.substring(0, input.indexOf("-"));
    }

    /**
     * Logs a Facebook event for a payment failure with specified parameters.
     *
     * @param label          The label associated with the event.
     * @param priceValue     The price value related to the payment failure.
     * @param currency       The currency code (e.g., "USD", "INR").
     * @param purchaseSource The source of the purchase attempt.
     */

    public static void addFacebookEventPaymentFailed(String label,double priceValue, String currency, String purchaseSource) {
        String paramLabel = replaceSpaceDashFromLabelFcmEventName(label);
        String paramPurchaseSource = "";
        if (!TextUtils.isEmpty(purchaseSource)) {
            paramPurchaseSource = replaceSpaceDashFromLabelFcmEventName(purchaseSource);
        }

        // facebook event
        AppEventsLogger logger = AppEventsLogger.newLogger(AstrosageKundliApplication.getAppContext());
        Bundle params = new Bundle();
        params.putString(AppEventsConstants.EVENT_PARAM_LEVEL, paramLabel);
        params.putString(AppEventsConstants.EVENT_PARAM_CURRENCY, currency);
        params.putString(AppEventsConstants.EVENT_PARAM_DESCRIPTION, paramPurchaseSource);
        params.putString(AppEventsConstants.EVENT_PARAM_CONTENT, priceValue + "");
        logger.logEvent(CGlobalVariables.FIREBASE_EVENT_PAYMENT_FAILED, params);
        //Log.e("TestPurchase", "params="+params);
    }


    /**
     * Logs a Facebook event with the specified parameters.
     *
     * @param event  The event name.
     * @param label  The label associated with the event.
     * @param source The source of the event.
     * */
    public static void addFacebookAndFirebaseEvent(String event, String label, String source) {
        try {
            String paramLabel = replaceSpaceDashFromLabelFcmEventName(label);
            // facebook
            AppEventsLogger logger = AppEventsLogger.newLogger(AstrosageKundliApplication.getAppContext());
            Bundle params = new Bundle();
            params.putString(AppEventsConstants.EVENT_PARAM_LEVEL, paramLabel);
            params.putString(AppEventsConstants.EVENT_PARAM_DESCRIPTION, source);
            logger.logEvent(event, params);

            //Log.e("TestFacebookEvent", "event="+event+" params="+params);

            //firebase event
            Bundle bundle = new Bundle();
            bundle.putString(com.ojassoft.astrosage.utils.CGlobalVariables.FIREBASE_LABEL_PARAMS, paramLabel);
            FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(AstrosageKundliApplication.getAppContext());
            mFirebaseAnalytics.logEvent(event, bundle);

            //Log.e("TestFacebookEvent", "event="+event+" bundle="+bundle);
        } catch (Exception e) {
            //
        }
    }
    private static final String[] PERMISSION = {
            Manifest.permission.RECORD_AUDIO
    };
    public static boolean checkPermissionsAudio(Activity activity) {
        boolean granted = true;
        for (String per : PERMISSION) {
            if (!permissionGranted(per,activity)) {
                granted = false;
                break;
            }
        }
        return granted;
    }
    private static boolean permissionGranted(String permission,Activity activity) {
        return ContextCompat.checkSelfPermission(
                activity, permission) == PackageManager.PERMISSION_GRANTED;
    }
    
    public static void showPreMicPermissionDialog(Context context, com.ojassoft.astrosage.misc.Callback callback) {

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout for the dialog
        View dialogView = inflater.inflate(R.layout.layout_pre_mic_permission, null);
        builder.setView(dialogView);

        TextView title = dialogView.findViewById(R.id.tvTitle);
        TextView subText = dialogView.findViewById(R.id.tvDescription);
        Button btnOk = dialogView.findViewById(R.id.btnContinue);
        ImageView ivCancel = dialogView.findViewById(R.id.btnClose);
        FontUtils.changeFont(context, title, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(context, subText, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(context, btnOk, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        AlertDialog dialog = builder.create();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        dialog.setCancelable(false);
        ivCancel.setOnClickListener((v)->{
            CUtils.fcmAnalyticsEvents(CGlobalVariables.MIC_PRE_PERMISSION_DIALOG_DISMISS,  CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
            dialog.dismiss();}
        );
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CUtils.fcmAnalyticsEvents(CGlobalVariables.MIC_PRE_PERMISSION_DIALOG_GRNT,  CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                dialog.dismiss();
                callback.call();
            }
        });
        dialog.show();
    }

    private static AlertDialog permissionDialog;
    public static void showPermissionDeniedDialog(Context context) {
        if (permissionDialog != null && permissionDialog.isShowing()) {
            return;
        }

        // Use 'this' as the Context for AlertDialog.Builder
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        LayoutInflater inflater = LayoutInflater.from(context);
        // Inflate the custom layout for the dialog
        View dialogView = inflater.inflate(R.layout.dialog_mic_permission, null);
        builder.setView(dialogView);

        // Get references to the views
        TextView notNowButton = dialogView.findViewById(R.id.btn_not_now);
        ImageView closeBtn = dialogView.findViewById(R.id.close_btn);
        TextView txtTitle = dialogView.findViewById(R.id.txtTitle);
        TextView txtSubText = dialogView.findViewById(R.id.txtSubText);
        TextView txtSubText1 = dialogView.findViewById(R.id.txtSubText1);
        Button settingsButton = dialogView.findViewById(R.id.btn_settings);

        // Apply fonts (Assuming FontUtils and CGlobalVariables are available in Java)
        FontUtils.changeFont(context, notNowButton, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(context, settingsButton, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(context, txtTitle, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(context, txtSubText, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(context, txtSubText1, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);

        // Create and show the dialog
        // In Java, AlertDialog is generally not nullable unless explicitly defined as such.
        // The equivalent of 'permissionDialog?' checks will be 'if (permissionDialog != null)'
        permissionDialog = builder.create();
        if (permissionDialog.getWindow() != null) {
            permissionDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
        permissionDialog.setCancelable(false);
        permissionDialog.show();

        // Set click listeners
        notNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissionDialog.dismiss();
            }
        });

        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissionDialog.dismiss();
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissionDialog.dismiss();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", context.getPackageName(), null);
                intent.setData(uri);
                // Assuming isPermissionsSettingOpen is a boolean class member
                context.startActivity(intent);
            }
        });
    }

    /**
     * Logs a follow event for a specific astrologer.
     *
     * @param astroId The unique identifier of the astrologer.
     * @param source  The source from which the follow event is triggered.
     */
    public static void addFollowEventForAstrologer(String astroId, String source) {
        if(TextUtils.isEmpty(astroId)) return;
        String label = "";

        if (astroId.equals(CGlobalVariables.KRISHNAMURTI)) {
            label = "follow_astro_mr_km";
        } else if (astroId.equals(CGlobalVariables.ARJUN)) {
            label = "follow_astro_arjun_pt";
        } else if (astroId.equals(CGlobalVariables.ANANYA)) {
            label = "follow_astro_ananya";
        } else if (astroId.equals(CGlobalVariables.DR_RAMAN)) {
            label = "follow_astro_dr_raman";
        } else if (astroId.equals(CGlobalVariables.LOVE_GURU)) {
            label = "follow_astro_love_guru";
        } else if (astroId.equals(CGlobalVariables.JOSHI)) {
            label = "follow_astro_joshi";
        }

        if (!TextUtils.isEmpty(label)) {
            //Log.e("TestFollowEvent", "label="+label);
            CUtils.addFacebookAndFirebaseEvent(CGlobalVariables.FIREBASE_EVENT_FOLLOW_ASTROLOGER, label, source);
        }
    }

    /**
     * Logs a user rating event for a specific astrologer if the rating is 4 or higher.
     *
     * @param astroId The unique identifier of the astrologer.
     * @param rating  The rating given by the user (should be 4 or higher to log the event).
     * @param source  The source from which the rating event is triggered.
     */
    public static void addUserRatingEventForAstrologer(String astroId, int rating, String source) {
        if(TextUtils.isEmpty(astroId)) return;
        if(rating < 4) return;
        String label = "";

        if (astroId.equals(CGlobalVariables.KRISHNAMURTI)) {
            label = "user_rating_mr_km_"+rating;
        } else if (astroId.equals(CGlobalVariables.ARJUN)) {
            label = "user_rating_arjun_pt_"+rating;
        } else if (astroId.equals(CGlobalVariables.ANANYA)) {
            label = "user_rating_ananya_"+rating;
        } else if (astroId.equals(CGlobalVariables.DR_RAMAN)) {
            label = "user_rating_dr_raman_"+rating;
        } else if (astroId.equals(CGlobalVariables.LOVE_GURU)) {
            label = "user_rating_love_guru_"+rating;
        } else if (astroId.equals(CGlobalVariables.JOSHI)) {
            label = "user_rating_joshi_"+rating;
        }

        if (!TextUtils.isEmpty(label)) {
            //Log.e("TestFollowEvent", "label="+label);
            CUtils.addFacebookAndFirebaseEvent(AppEventsConstants.EVENT_NAME_RATED, label, source);
        }
    }

    /**
     * Returns the AI Pass coupon code based on the provided plan service ID.
     * for 199 rs plan return AIPASS1
     * for 499 rs plan return AIPASS2
     * for 1999 rs plan return AIPASS3
     * @param planServiceId The plan service ID.
     * @return The AI Pass coupon code.
     */
    public static String getAIPassCouponByPlanServiceId(String planServiceId) {
        if(planServiceId == null) return "";
        String couponCode = "";
        if (planServiceId.equals(CGlobalVariables.PLAN_SERVICE_ID_259)) {
            couponCode = CGlobalVariables.AIPASS1;
        } else if (planServiceId.equals(CGlobalVariables.PLAN_SERVICE_ID_262)) {
            couponCode = CGlobalVariables.AIPASS2;
        } else if (planServiceId.equals(CGlobalVariables.PLAN_SERVICE_ID_260)) {
            couponCode = CGlobalVariables.AIPASS3;
        }
        return couponCode;
    }

    /**
     * Starts the GetUserAIChatCategoryDataService with the specified offer type.
     *
     * @param context   The application context.
     * @param offerType The offer type to be passed to the service.
     */
    public static void startUserAiChatCategoryDataService(Context context, String offerType, String channelID, String source) {
        try {

            boolean getUserChatCategoryEnabled = CUtils.getBooleanData(context,GET_USER_CHAT_CATEGORY_ENABLED, true);
            if(!getUserChatCategoryEnabled){
                Log.e("TestMemoryGen", "GetUserAIChatCategoryDataService disabled in config");
                return;
            }

            Intent intent = new Intent(context, GetUserAIChatCategoryDataService.class);
            intent.putExtra(CGlobalVariables.OFFER_TYPE, offerType);
            intent.putExtra(CGlobalVariables.CHANNEL_ID, channelID);
            intent.putExtra(CGlobalVariables.CALL_SOURCE, source);

            //Log.e("TestMemoryGen", "offerType="+offerType+" channelID="+channelID+" source="+source);
            context.startService(intent);
        } catch (Exception e) {
            //
        }
    }

    public static boolean isSuggestedRechargeDialogShow(Context context) {
        return CUtils.getBooleanData(context, com.ojassoft.astrosage.utils.CGlobalVariables.IS_INSUFFICIENT_SUGGESTED_RECHARGE_DIALOG_SHOW, false);
    }

    public static void openQuickRechargeSheet(Activity activity,String astroName, String minBalanceNeededText, String userbalance, FragmentManager fragmentManager) {
        try {
            RechargeSuggestionBottomSheet rechargeSuggestionBottomSheet = RechargeSuggestionBottomSheet.getInstance();
            Bundle bundle = new Bundle();
            //bundle.putString("mResponse", response);
            // bundle.putString("astrologerUrlText", AstrosageKundliApplication.selectedAstrologerDetailBean.getUrlText());
            bundle.putString(com.ojassoft.astrosage.utils.CGlobalVariables.BOTTOMSERVICELISTUSEDFOR, com.ojassoft.astrosage.utils.CGlobalVariables.CONTINUE_CHAT);
            bundle.putString("minBalanceNeededText", minBalanceNeededText);
            bundle.putString("astroName", astroName);
            bundle.putString("userbalance", userbalance);
            rechargeSuggestionBottomSheet.setArguments(bundle);
            checkCachedData(activity,minBalanceNeededText,userbalance);
            rechargeSuggestionBottomSheet.show(fragmentManager, RechargeSuggestionBottomSheet.TITLE);
        } catch (Exception e) {

        }
    }
    /**
     * Creates a suggested recharge service based on the required amount.
     *
     * @param intRechargeAmount The suggested recharge amount.
     * @return A {@link WalletAmountBean.Services} object representing the suggested recharge.
     */
    public static WalletAmountBean.Services get1stService(int intRechargeAmount) {
         WalletAmountBean walletAmountBean  = new WalletAmountBean();;
        WalletAmountBean.Services services = walletAmountBean.new Services();
        services.setServiceid(CGlobalVariables.SERVICE_ID_VARIABLE_AMOUNT);
        services.setServicename("Recharge " + intRechargeAmount);
        services.setSmalliconfile("");
        services.setCategoryid("");
        services.setRate("");
        services.setRaters(intRechargeAmount + ".0");
        services.setActualrate("");
        services.setActualraters(intRechargeAmount + ".0");
        services.setPaymentamount("0");
        services.setOffermessage("");
        services.setOfferAmount("0");
        return services;
    }

    /**
     * Creates a "More Recharges" service item.
     *
     * @return A {@link WalletAmountBean.Services} object for "More Recharges".
     */
    public static WalletAmountBean.Services getMoreRechargeServices(Context context) {
        WalletAmountBean walletAmountBean  = new WalletAmountBean();;

        WalletAmountBean.Services services = walletAmountBean.new Services();
        services.setServiceid(CGlobalVariables.SERVICE_ID_MORE_RECHARGES);
        services.setServicename(context.getResources().getString(R.string.more_recharge));
        services.setSmalliconfile("");
        services.setCategoryid("");
        services.setRate("");
        services.setRaters("00.0");
        services.setActualrate("");
        services.setActualraters("00.0");
        services.setPaymentamount("00.0");
        services.setOffermessage("");
        services.setOfferAmount("0");
        return services;
    }
    /**
     * Checks for cached recharge data. If available, it parses the cached data.
     * Otherwise, it makes an API call to get the recharge amounts.
     */
    public static void checkCachedData(Context context,String minBalanceNeededText, String userbalance) {
        try {
            ArrayList<WalletAmountBean.Services> servicesArrayList;

            boolean showSuggestedAmount = true;
            double rechargeNeededAmount = 0.0;
            rechargeNeededAmount = Double.parseDouble(minBalanceNeededText) - Double.parseDouble(userbalance);
            int minInternationalRecharge=100;


            servicesArrayList = new ArrayList<>();
            int intRechargeAmount = 0;
            if (TextUtils.isEmpty(CUtils.getUserIntroOfferType(context))) { //user have no offer
                intRechargeAmount = (int) Double.parseDouble(minBalanceNeededText) - (int) Double.parseDouble(userbalance);
                String minRechargeAmt = com.ojassoft.astrosage.utils.CUtils.getStringData(context, com.ojassoft.astrosage.utils.CGlobalVariables.KEY_MIN_RECHARGE_AMOUNT, "00");

                //if suggestion recharge amount is less than minimum recharge amt from server, set it to minimum recharge amt from server
                int minimumRechargeAmount = Integer.parseInt(minRechargeAmt);
                if(intRechargeAmount < minimumRechargeAmount){
                    intRechargeAmount = minimumRechargeAmount;
                }
            }

            if (showSuggestedAmount && rechargeNeededAmount <= intRechargeAmount) {
                String countryCode = com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(context);
                if (TextUtils.isEmpty(countryCode) || countryCode.equals(com.ojassoft.astrosage.utils.CGlobalVariables.COUNTRY_CODE_IND)) {
                    servicesArrayList.add(get1stService(intRechargeAmount));
                } else {
                    if (intRechargeAmount >= minInternationalRecharge) {
                        servicesArrayList.add(get1stService(intRechargeAmount));
                    }
                }

            }
            if (!TextUtils.isEmpty(CUtils.getWalletRechargeData())) {
                parseResponse(context,CUtils.getWalletRechargeData(),servicesArrayList,rechargeNeededAmount);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * Parses the JSON response containing the recharge amounts.
     *
     * @param response             The JSON response string.
     * @param servicesArrayList
     * @param rechargeNeededAmount
     */
    public static void parseResponse(Context context, String response, ArrayList<WalletAmountBean.Services> servicesArrayList, double rechargeNeededAmount) {
        if (response != null && !response.isEmpty()) {
            try {
                 int maxRechargeServicesCount = 7, selectedRechargeServicesPosition = 1,minInternationalRecharge=100;
                boolean isFirstTime= true;
                JSONObject jsonObject = new JSONObject(response);
                String gstRate = jsonObject.getString("gstrate");
                String dollarconversionrate = jsonObject.getString("dollarconversionrate");

                JSONArray jsonArray = jsonObject.getJSONArray("services");
                ArrayList<WalletAmountBean.Services>  masterservicesArrayList = new ArrayList<>();
                WalletAmountBean  walletAmountBean = new WalletAmountBean();
                WalletAmountBean  masterWalletAmountBean = new WalletAmountBean();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject innerJsonObject = jsonArray.getJSONObject(i);
                    WalletAmountBean.Services services = walletAmountBean.new Services();
                    services.setServiceid(innerJsonObject.getString("serviceid"));
                    services.setServicename(innerJsonObject.getString("servicename"));
                    services.setSmalliconfile(innerJsonObject.getString("smalliconfile"));
                    services.setCategoryid(innerJsonObject.getString("categoryid"));
                    services.setRate(innerJsonObject.getString("rate"));
                    services.setRaters(innerJsonObject.getString("raters"));
                    services.setActualrate(innerJsonObject.getString("actualrate"));
                    services.setActualraters(innerJsonObject.getString("actualraters"));
                    services.setPaymentamount(innerJsonObject.getString("paymentamount"));
                    services.setOffermessage(innerJsonObject.getString("offermessage"));
                    services.setOfferAmount(innerJsonObject.getString("offeramout"));

                    if (servicesArrayList.size() < maxRechargeServicesCount) {

                        double nextRechargeAmount = Double.parseDouble(services.getRaters());
                        String countryCode = com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(context);
                        if (rechargeNeededAmount <= nextRechargeAmount) {
                            if (TextUtils.isEmpty(countryCode) || countryCode.equals(com.ojassoft.astrosage.utils.CGlobalVariables.COUNTRY_CODE_IND)) {
                                // if both variable and services has same value
                                if(isFirstTime){
                                    if(servicesArrayList.size()==1){
                                        if(servicesArrayList.get(0).getServiceid().equals(CGlobalVariables.SERVICE_ID_VARIABLE_AMOUNT)){
                                            double intRaters = Double.parseDouble(services.getRaters());
                                            double intRechargeAmount = Double.parseDouble(servicesArrayList.get(0).getRaters());
                                            if (intRechargeAmount == intRaters) {
                                                servicesArrayList.remove(0);
                                            }
                                        }

                                    }
                                    isFirstTime = false;
                                }
                                if (servicesArrayList.size() == selectedRechargeServicesPosition) {
                                    services.setSelected(true);
                                }
                                servicesArrayList.add(services);
                            } else {
                                if (nextRechargeAmount >= minInternationalRecharge) {
                                    if(isFirstTime){
                                        if(servicesArrayList.size()==1){
                                            if(servicesArrayList.get(0).getServiceid().equals(CGlobalVariables.SERVICE_ID_VARIABLE_AMOUNT)){
                                                double intRaters = Double.parseDouble(services.getRaters());
                                                double intRechargeAmount = Double.parseDouble(servicesArrayList.get(0).getRaters());
                                                if (intRechargeAmount == intRaters) {
                                                    servicesArrayList.remove(0);
                                                }
                                            }

                                        }
                                        isFirstTime = false;
                                    }
                                    if (servicesArrayList.size() == selectedRechargeServicesPosition) {
                                        services.setSelected(true);
                                    }
                                    servicesArrayList.add(services);
                                }
                            }

                        }
                    }
                    masterservicesArrayList.add(services);
                }
                //added for more recharge services
                servicesArrayList.add(getMoreRechargeServices(context));
                walletAmountBean.setGstrate(gstRate);
                masterWalletAmountBean.setGstrate(gstRate);
                walletAmountBean.setDollorConverstionRate(dollarconversionrate);
                masterWalletAmountBean.setDollorConverstionRate(dollarconversionrate);
                walletAmountBean.setServiceList(servicesArrayList);
                masterWalletAmountBean.setServiceList(masterservicesArrayList);

                setMyWalletAmountBean(walletAmountBean);
                setMyMasterWalletAmountBean(masterWalletAmountBean);

            } catch (Exception e) {
                Log.d("testException", "e=>>" + e);
                e.printStackTrace();
            }
        }
//        else {
//            CUtils.showSnackbar(mView.findViewById(android.R.id.content), getResources().getString(R.string.server_error), context);
//        }
    }
    /**
     * Adds FCM analytics events based on the calling activity.
     */
    public static void addFcmAnalyticsEvents(Context activity) {
        if (activity instanceof AstrologerDescriptionActivity) {
            CUtils.fcmAnalyticsEvents("fba_astro_desc_low_balance_open_payment_info", CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");
            com.ojassoft.astrosage.utils.CUtils.createSession(activity, "ADREC");
        } else if (activity instanceof DashBoardActivity) {
            CUtils.fcmAnalyticsEvents("fba_DashBoardActivity_low_balance_open_payment_info", CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");
            com.ojassoft.astrosage.utils.CUtils.createSession(activity, "DAREC");
        } else if (activity instanceof ActAppModule) {
            CUtils.fcmAnalyticsEvents("fba_ActAppModuleActivity_low_balance_open_payment_info", CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");
            com.ojassoft.astrosage.utils.CUtils.createSession(activity, "HDREC");
        } else if (activity instanceof ChatWindowActivity) {
            CUtils.fcmAnalyticsEvents("fba_ChatWindowActivity_low_balance_open_payment_info", CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");
            com.ojassoft.astrosage.utils.CUtils.createSession(activity, "CWREC");
        } else if (activity instanceof AIChatWindowActivity) {
            CUtils.fcmAnalyticsEvents("fba_AiChatWindowActivity_low_balance_open_payment_info", CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");
            com.ojassoft.astrosage.utils.CUtils.createSession(activity, CGlobalVariables.AI_CHAT_WINDOW_INSUFFICIANT_BALANCE_RECHARGE_PARTNER_ID);
        } else if (activity instanceof AIVoiceCallingActivity) {
            CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_AI_CHAT_WINDOW_LOW_BALANCE_OPEN_WALLET, CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");
            com.ojassoft.astrosage.utils.CUtils.createSession(activity, CGlobalVariables.AI_CHAT_WINDOW_INSUFFICIANT_BALANCE_RECHARGE_PARTNER_ID);
        } else if (activity instanceof LiveActivityNew) {
            CUtils.fcmAnalyticsEvents("fba_astro_live_low_balance_open_wallet", CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");
            com.ojassoft.astrosage.utils.CUtils.createSession(activity, "LVREC");
        } else {
            CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_BASE_ACTIVITY_LOW_BALANCE_OPEN_WALLET, CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");
            com.ojassoft.astrosage.utils.CUtils.createSession(activity, "BAREC");
        }
    }
}
