package com.ojassoft.astrosage.varta.service;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Person;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Icon;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.text.TextUtils;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Foreground service that keeps the vartalive voice-call notification visible
 * with a countdown timer and call action shortcuts while the activity is in background.
 */
public class VoiceCallOngoingNotificationService extends Service {

    public static final String NOTIFICATION_CHANNEL_ID = "ForegroundServiceOngoingCall";
    public static String REMAINING_TIME = null;

    private static final int NOTIFICATION_ID = 301;
    private static final long ONE_SECOND = 1000L;
    private static final String DEFAULT_VARTALIVE_ACTIVITY = "com.ojassoft.vartalive.activities.VoiceCallActivity";
    private static final String DEFAULT_BASE_ACTIVITY = "com.ojassoft.astrosage.vartalive.activities.VoiceCallActivity";

    private Context context;
    private long callTimerTime;
    private long longTotalVerificationTime = 60000L;
    private String consultationType;
    private String agoraCallSId;
    private String agoraToken;
    private String agoraTokenId;
    private String astrologerName;
    private String astrologerProfileUrl;
    private String agoraCallDuration;
    private String astrologerId;
    private String timeRemaining;
    private String currentRemainingDuration;
    private String activityClassName;
    private boolean micStatus;
    private boolean videoStatus;
    private boolean endCallData;
    private boolean notificationOnlyMode;
    private Bitmap astrologerProfileBitmap;
    private CountDownTimer countDownTimer;
    private ValueEventListener endCallValueEventListener;
    private DatabaseReference readRef;
    private FirebaseDatabase firebaseInstance;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        context = this;
        if (!hasValidStartData(intent)) {
            stopSelf(startId);
            return START_NOT_STICKY;
        }
        try {
            getDataFromIntent(intent);
            createNotificationChannel();
            REMAINING_TIME = getCurrentTimerForActivity();
            notificationWithNoAstrologerImage();
            if (!TextUtils.isEmpty(astrologerProfileUrl)) {
                createNotification(CGlobalVariables.IMAGE_DOMAIN + astrologerProfileUrl);
            }
            if (!notificationOnlyMode) {
                timeSetOnTimer(getInitialTimerSeed());
                initEndChatListener();
            }
        } catch (Exception e) {
            //
        }
        return START_REDELIVER_INTENT;
    }

    /**
     * Validates the minimum launch data required to recreate the ongoing-call notification safely.
     */
    private boolean hasValidStartData(@Nullable Intent intent) {
        if (intent == null) {
            return false;
        }
        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            return false;
        }
        String channelId = bundle.getString(CGlobalVariables.AGORA_CALLS_ID);
        return !TextUtils.isEmpty(channelId);
    }

    /**
     * Creates the notification channel required by foreground notifications on Android O+.
     */
    private void createNotificationChannel() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel serviceChannel = new NotificationChannel(
                        NOTIFICATION_CHANNEL_ID,
                        "Foreground Service Ongoing Call",
                        NotificationManager.IMPORTANCE_DEFAULT
                );
                NotificationManager manager = getSystemService(NotificationManager.class);
                if (manager != null) {
                    manager.createNotificationChannel(serviceChannel);
                }
            }
        } catch (Exception ignored) {
            //
        }
    }

    /**
     * Loads the astrologer image so the notification matches the AI call card.
     */
    private void createNotification(String astrologerImageUrl) {
        try {
            Glide.with(this)
                    .asBitmap()
                    .load(astrologerImageUrl)
                    .into(new com.bumptech.glide.request.target.CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable com.bumptech.glide.request.transition.Transition<? super Bitmap> transition) {
                            astrologerProfileBitmap = resource;
                            Notification notification = buildNotification(resource, getNotificationCountdownSeed());
                            NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                            if (manager != null && notification != null) {
                                manager.notify(NOTIFICATION_ID, notification);
                            }
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                            //
                        }

                        @Override
                        public void onLoadFailed(@Nullable Drawable errorDrawable) {
                            super.onLoadFailed(errorDrawable);
                            notificationWithNoAstrologerImage();
                        }
                    });
        } catch (Exception e) {
            notificationWithNoAstrologerImage();
        }
    }

    /**
     * Falls back to the default astrologer artwork when no remote image is available.
     */
    private void notificationWithNoAstrologerImage() {
        astrologerProfileBitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.ai_astro_default);
        Notification notification = buildNotification(astrologerProfileBitmap, getNotificationCountdownSeed());
        if (notification != null) {
            startForeground(NOTIFICATION_ID, notification);
        } else {
            stopSelf();
        }
    }

    /**
     * Builds the call-style notification shown while the call is in background.
     */
    @SuppressLint("NewApi")
    private Notification buildNotification(Bitmap icon, String countdownSeed) {
        PendingIntent contentPendingIntent = PendingIntent.getActivity(
                this,
                0,
                buildCallActivityIntent(null, true),
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        PendingIntent hangUpPendingIntent = PendingIntent.getActivity(
                this,
                0,
                buildCallActivityIntent(CGlobalVariables.HANG_UP_ACTION, false),
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        PendingIntent speakerPendingIntent = PendingIntent.getActivity(
                this,
                0,
                buildCallActivityIntent(CGlobalVariables.SPEAKER_ACTION, false),
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );
        PendingIntent micPendingIntent = PendingIntent.getActivity(
                this,
                0,
                buildCallActivityIntent(CGlobalVariables.MIC_ACTION, false),
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE
        );

        String callerName = getDisplayAstrologerName();
        String message = getString(R.string.ongoing_call);
        if (icon == null) {
            icon = BitmapFactory.decodeResource(context.getResources(), R.mipmap.icon);
        }

        Notification notification = null;
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                Person caller = new Person.Builder()
                        .setName(callerName)
                        .setImportant(true)
                        .setIcon(Icon.createWithAdaptiveBitmap(icon))
                        .build();

                Notification.Action speakerAction = new Notification.Action.Builder(
                        Icon.createWithResource(this, R.drawable.speaker),
                        "Speaker",
                        speakerPendingIntent
                ).build();

                Notification.Action micAction = new Notification.Action.Builder(
                        Icon.createWithResource(this, R.drawable.ic_live_mute),
                        "Mic",
                        micPendingIntent
                ).build();

                notification = new Notification.Builder(context, NOTIFICATION_CHANNEL_ID)
                        .setContentTitle(callerName)
                        .setContentText(message)
                        .setSmallIcon(R.drawable.calling_icon_)
                        .setCategory(Notification.CATEGORY_CALL)
                        .setOngoing(true)
                        .setUsesChronometer(true)
                        .setChronometerCountDown(true)
                        .setWhen(getEndTimeMillisFromRemaining(countdownSeed))
                        .setContentIntent(contentPendingIntent)
                        .addAction(speakerAction)
                        .addAction(micAction)
                        .setStyle(Notification.CallStyle.forOngoingCall(caller, hangUpPendingIntent))
                        .setOnlyAlertOnce(true)
                        .build();
            } else {
                RemoteViews customView = new RemoteViews(context.getPackageName(), R.layout.custom_notification_layout);
                customView.setTextViewText(R.id.title_text, callerName);
                customView.setTextViewText(R.id.description_text, message);
                customView.setOnClickPendingIntent(R.id.button_hangup, hangUpPendingIntent);
                customView.setImageViewBitmap(R.id.astro_avatar, icon);
                customView.setImageViewBitmap(R.id.icon, BitmapFactory.decodeResource(context.getResources(), R.drawable.calling_icon_));

                notification = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                        .setContentTitle(callerName)
                        .setContentText(message)
                        .setSmallIcon(R.drawable.calling_icon_)
                        .setPriority(NotificationCompat.PRIORITY_LOW)
                        .setCategory(NotificationCompat.CATEGORY_CALL)
                        .setContentIntent(contentPendingIntent)
                        .setOngoing(true)
                        .setUsesChronometer(true)
                        .setChronometerCountDown(true)
                        .setWhen(getEndTimeMillisFromRemaining(countdownSeed))
                        .setOnlyAlertOnce(true)
                        .build();
            }
        } catch (Exception e) {
            //
        }
        return notification;
    }

    /**
     * Creates an explicit activity intent so notification taps reopen the same voice-call screen.
     */
    private Intent buildCallActivityIntent(@Nullable String action, boolean fromNotificationClick) {
        Intent intent = new Intent();
        intent.setClassName(getPackageName(), getResolvedActivityClassName());
        intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtras(getIntentBundle());
        intent.putExtra("ongoing_notification", fromNotificationClick);
        if (!TextUtils.isEmpty(action)) {
            intent.setAction(action);
        }
        return intent;
    }

    /**
     * Packages the latest call state so the activity can resume from the notification.
     */
    private Bundle getIntentBundle() {
        Bundle bundle = new Bundle();
        bundle.putBoolean(CGlobalVariables.IS_FROM_RETURN_TO_CALL, true);
        bundle.putBoolean(CGlobalVariables.AGORA_CALL_MIC_STATUS, micStatus);
        bundle.putBoolean(CGlobalVariables.AGORA_CALL_VIDEO_STATUS, videoStatus);
        bundle.putString(CGlobalVariables.AGORA_CALL_TYPE, consultationType);
        bundle.putString(CGlobalVariables.AGORA_CALLS_ID, agoraCallSId);
        bundle.putString(CGlobalVariables.AGORA_TOKEN, agoraToken);
        bundle.putString(CGlobalVariables.AGORA_TOKEN_ID, agoraTokenId);
        bundle.putString(CGlobalVariables.ASTROLOGER_NAME, astrologerName);
        bundle.putString(CGlobalVariables.ASTROLOGER_PROFILE_URL, astrologerProfileUrl);
        bundle.putString(CGlobalVariables.AGORA_CALL_DURATION, getCurrentTimerForActivity());
        bundle.putString(CGlobalVariables.CALL_TIME_REMAINING, getCurrentTimerForActivity());
        bundle.putString(CGlobalVariables.ASTROLOGER_ID, astrologerId);
        bundle.putString(CGlobalVariables.AGORA_CALL_ACTIVITY_CLASS, activityClassName);
        return bundle;
    }

    /**
     * Starts the background timer state used for API bookkeeping and resume data.
     */
    private void timeSetOnTimer(String talkTime) {
        longTotalVerificationTime = parseDurationToMillis(talkTime);
        if (longTotalVerificationTime <= 0L) {
            longTotalVerificationTime = 60000L;
        }
        initializingCountDownTimer(longTotalVerificationTime);
    }

    /**
     * Tracks the remaining call duration while the foreground notification is active.
     */
    private void initializingCountDownTimer(long totalDurationMillis) {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(totalDurationMillis, ONE_SECOND) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeRemaining = String.format(
                        Locale.US,
                        "%02d:%02d:%02d",
                        TimeUnit.MILLISECONDS.toHours(millisUntilFinished) - TimeUnit.DAYS.toHours(TimeUnit.MILLISECONDS.toDays(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(millisUntilFinished)),
                        TimeUnit.MILLISECONDS.toSeconds(millisUntilFinished) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(millisUntilFinished))
                );
                callTimerTime = millisUntilFinished;
                REMAINING_TIME = getCurrentTimerForActivity();
            }

            @Override
            public void onFinish() {
                callTimerTime = 0L;
                REMAINING_TIME = "00:00";
                try {
                    updateNotification("00:00");
                    CUtils.changeFirebaseKeyStatus(agoraCallSId, "NA", true, CGlobalVariables.TIME_OVER);
                    voiceCallCompleted(CGlobalVariables.TIME_OVER);
                } catch (Exception ignored) {
                    //
                }
                stopSelf();
            }
        }.start();
    }

    /**
     * Stops the service when the backend marks the call as ended from another client.
     */
    private void initEndChatListener() {
        endCallValueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot == null) {
                    return;
                }
                try {
                    endCallData = (boolean) dataSnapshot.getValue();
                    if (endCallData) {
                        if (countDownTimer != null) {
                            countDownTimer.cancel();
                        }
                        updateNotification("00:00");
                        CUtils.changeFirebaseKeyStatus(agoraCallSId, "NA", true, CGlobalVariables.TIME_OVER);
                        voiceCallCompleted(CGlobalVariables.ASTROLOGER);
                        stopSelf();
                    }
                } catch (Exception ignored) {
                    //
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                //
            }
        };
        readRef = getFirebaseDatabase(agoraCallSId).child(CGlobalVariables.END_CHAT_OVER_FBD_KEY);
        readRef.addValueEventListener(endCallValueEventListener);
    }

    /**
     * Returns the channel reference used by the voice-call lifecycle flags.
     */
    private DatabaseReference getFirebaseDatabase(String channelId) {
        if (channelId == null || channelId.isEmpty()) {
            channelId = "";
        }
        if (firebaseInstance == null) {
            firebaseInstance = FirebaseDatabase.getInstance();
        }
        return firebaseInstance.getReference(CGlobalVariables.CHANNELS).child(channelId);
    }

    /**
     * Extracts the call metadata required to recreate the notification state.
     */
    private void getDataFromIntent(Intent intent) {
        if (intent == null) {
            return;
        }
        micStatus = intent.getBooleanExtra(CGlobalVariables.AGORA_CALL_MIC_STATUS, true);
        videoStatus = intent.getBooleanExtra(CGlobalVariables.AGORA_CALL_VIDEO_STATUS, true);
        consultationType = intent.getStringExtra(CGlobalVariables.AGORA_CALL_TYPE);
        agoraCallSId = intent.getStringExtra(CGlobalVariables.AGORA_CALLS_ID);
        agoraToken = intent.getStringExtra(CGlobalVariables.AGORA_TOKEN);
        agoraTokenId = intent.getStringExtra(CGlobalVariables.AGORA_TOKEN_ID);
        astrologerName = intent.getStringExtra(CGlobalVariables.ASTROLOGER_NAME);
        astrologerProfileUrl = intent.getStringExtra(CGlobalVariables.ASTROLOGER_PROFILE_URL);
        agoraCallDuration = intent.getStringExtra(CGlobalVariables.AGORA_CALL_DURATION);
        astrologerId = intent.getStringExtra(CGlobalVariables.ASTROLOGER_ID);
        currentRemainingDuration = intent.getStringExtra(CGlobalVariables.CALL_TIME_REMAINING);
        activityClassName = intent.getStringExtra(CGlobalVariables.AGORA_CALL_ACTIVITY_CLASS);
        notificationOnlyMode = intent.getBooleanExtra(CGlobalVariables.CALL_NOTIFICATION_ONLY_MODE, false);
    }

    /**
     * Calls the existing end-call API so background timeouts match the normal call flow.
     */
    private void voiceCallCompleted(String remark) {
        if (!CUtils.isConnectedWithInternet(context)) {
            return;
        }
        ApiList api = RetrofitClient.getInstance().create(ApiList.class);
        Call<ResponseBody> call = api.endInternetcall(getCallCompleteParams(remark));
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(@NonNull Call<ResponseBody> call, @NonNull Response<ResponseBody> response) {
                //
            }

            @Override
            public void onFailure(@NonNull Call<ResponseBody> call, @NonNull Throwable t) {
                //
            }
        });
    }

    /**
     * Builds the payload consumed by the existing end-call API.
     */
    private Map<String, String> getCallCompleteParams(String remarks) {
        HashMap<String, String> headers = new HashMap<>();
        CGlobalVariables.CHAT_END_STATUS = CGlobalVariables.COMPLETED;
        headers.put(CGlobalVariables.APP_KEY, CUtils.getApplicationSignatureHashCode(context));
        headers.put(CGlobalVariables.STATUS, CGlobalVariables.COMPLETED);
        headers.put(CGlobalVariables.CHAT_DURATION, timeChangeInSecond(timeRemaining));
        headers.put(CGlobalVariables.CHANNEL_ID, agoraCallSId);
        headers.put(CGlobalVariables.ASTROLOGER_ID, astrologerId);
        headers.put(CGlobalVariables.PACKAGE_NAME, CUtils.getAppPackageName(this));
        headers.put(CGlobalVariables.REMARKS, remarks);
        headers.put(CGlobalVariables.LANG, CUtils.getLanguageKey(LANGUAGE_CODE));
        return CUtils.setRequiredParams(headers);
    }

    /**
     * Converts the remaining timer text back into elapsed seconds for the API.
     */
    private String timeChangeInSecond(String userRemainingTime) {
        String userChatDuration = "00";
        long totalTime = parseDurationToMillis(agoraCallDuration);
        long remainingUserTime = parseDurationToMillis(userRemainingTime);
        long actualChatUserTime = Math.max(totalTime - remainingUserTime, 0L);

        try {
            long seconds = TimeUnit.MILLISECONDS.toSeconds(actualChatUserTime);
            userChatDuration = String.valueOf((int) seconds);
        } catch (Exception ignored) {
            //
        }
        return userChatDuration;
    }

    /**
     * Refreshes the notification when the countdown reaches its terminal states.
     */
    private void updateNotification(String countdownSeed) {
        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (manager != null) {
            manager.notify(NOTIFICATION_ID, buildNotification(astrologerProfileBitmap, countdownSeed));
        }
    }

    /**
     * Resolves the seed value used by the system chronometer countdown.
     */
    private String getNotificationCountdownSeed() {
        if (!TextUtils.isEmpty(getCurrentTimerForActivity())) {
            return getCurrentTimerForActivity();
        }
        return getInitialTimerSeed();
    }

    /**
     * Returns the starting countdown text, preferring a live remainder over the original duration.
     */
    private String getInitialTimerSeed() {
        if (!TextUtils.isEmpty(currentRemainingDuration)) {
            return currentRemainingDuration;
        }
        return agoraCallDuration;
    }

    /**
     * Formats the latest timer state as `MM:SS` for activity resume and notification intents.
     */
    private String getCurrentTimerForActivity() {
        if (callTimerTime > 0L) {
            return String.format(
                    Locale.US,
                    "%02d:%02d",
                    TimeUnit.MILLISECONDS.toMinutes(callTimerTime),
                    TimeUnit.MILLISECONDS.toSeconds(callTimerTime) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(callTimerTime))
            );
        }
        if (!TextUtils.isEmpty(currentRemainingDuration)) {
            return toMinuteSecondText(parseDurationToMillis(currentRemainingDuration));
        }
        return toMinuteSecondText(parseDurationToMillis(agoraCallDuration));
    }

    /**
     * Normalizes supported duration formats into a millisecond value.
     */
    private long parseDurationToMillis(String durationText) {
        if (TextUtils.isEmpty(durationText)) {
            return 0L;
        }
        String normalizedText = durationText.trim();
        if (normalizedText.contains(" ")) {
            normalizedText = normalizedText.split(" ")[0];
        }
        String[] parts = normalizedText.split(":");
        try {
            if (parts.length == 3) {
                long hours = Long.parseLong(parts[0].trim());
                long minutes = Long.parseLong(parts[1].trim());
                long seconds = Long.parseLong(parts[2].trim());
                return TimeUnit.HOURS.toMillis(hours) + TimeUnit.MINUTES.toMillis(minutes) + TimeUnit.SECONDS.toMillis(seconds);
            }
            if (parts.length == 2) {
                long minutes = Long.parseLong(parts[0].trim());
                long seconds = Long.parseLong(parts[1].trim());
                return TimeUnit.MINUTES.toMillis(minutes) + TimeUnit.SECONDS.toMillis(seconds);
            }
        } catch (Exception ignored) {
            //
        }
        return 0L;
    }

    /**
     * Converts milliseconds into the `MM:SS` text expected by the voice-call activity.
     */
    private String toMinuteSecondText(long durationMillis) {
        if (durationMillis <= 0L) {
            return "00:00";
        }
        long totalSeconds = TimeUnit.MILLISECONDS.toSeconds(durationMillis);
        long minutes = totalSeconds / 60L;
        long seconds = totalSeconds % 60L;
        return String.format(Locale.US, "%02d:%02d", minutes, seconds);
    }

    /**
     * Computes the absolute wall-clock value required by the system chronometer.
     */
    private long getEndTimeMillisFromRemaining(String remainingDuration) {
        long durationMillis = parseDurationToMillis(remainingDuration);
        if (durationMillis <= 0L) {
            return System.currentTimeMillis();
        }
        return System.currentTimeMillis() + durationMillis;
    }

    /**
     * Resolves the activity class name used by notification taps across base and dynamic modules.
     */
    private String getResolvedActivityClassName() {
        if (!TextUtils.isEmpty(activityClassName)) {
            return activityClassName;
        }
        try {
            Class.forName(DEFAULT_VARTALIVE_ACTIVITY);
            return DEFAULT_VARTALIVE_ACTIVITY;
        } catch (ClassNotFoundException ignored) {
            return DEFAULT_BASE_ACTIVITY;
        }
    }

    /**
     * Returns the human-readable astrologer name used in the notification title.
     */
    private String getDisplayAstrologerName() {
        if (TextUtils.isEmpty(astrologerName)) {
            return getString(R.string.ongoing_call);
        }
        return astrologerName.replace("+", " ");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        if (readRef != null && endCallValueEventListener != null) {
            readRef.removeEventListener(endCallValueEventListener);
        }
    }
}
