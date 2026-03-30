package com.ojassoft.astrosage.ui.act;

import static com.libojassoft.android.utils.LibCGlobalVariables.NOTIFICATION_PUSH;
import static com.ojassoft.astrosage.utils.CGlobalVariables.ASTROSAGE_AI_PACKAGE_NAME;
import static com.ojassoft.astrosage.utils.CGlobalVariables.FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_NOTIFICATION_LINK;
import static com.ojassoft.astrosage.utils.CGlobalVariables.PLAY_URL;
import static com.ojassoft.astrosage.utils.CGlobalVariables.UPDATE_INTERVAL_IN_MILLISECONDS;
import static com.ojassoft.astrosage.utils.CUtils.getLatLangCityFromIPDetails;
import static com.ojassoft.astrosage.utils.CUtils.saveAppInstallTime;
import static com.ojassoft.astrosage.utils.CUtils.saveAppInstallTimeRecorded;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.CONTINUE_AI_CHAT_TYPE;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowInsets;
import android.view.WindowInsetsController;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.app.ActivityCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.work.BackoffPolicy;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.NetworkType;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;

import com.android.installreferrer.api.InstallReferrerClient;
import com.android.installreferrer.api.InstallReferrerStateListener;
import com.android.installreferrer.api.ReferrerDetails;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.perf.metrics.AddTrace;
import com.libojassoft.android.dao.NotificationDBManager;
import com.libojassoft.android.models.NotificationModel;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.UnlockBrihatHelperClass;
import com.ojassoft.astrosage.misc.FetchPlanDetailsService;
import com.ojassoft.astrosage.misc.GetTagManagerDataService;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.JobServices.FindPlaceService;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.varta.model.AstrologerDetailBean;
import com.ojassoft.astrosage.varta.service.Loginservice;
import com.ojassoft.astrosage.varta.twiliochat.chat.history.ChatHistoryNew;
import com.ojassoft.astrosage.varta.ui.activity.BaseActivity;
import com.ojassoft.astrosage.varta.ui.activity.FirstTimeProfileDetailsActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import bolts.AppLinks;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ActAppSplash extends BaseInputActivity {//implements IPermissionCallback

    private final int PERMISSION_LOCATION = 2504;
    private final int PERMISSION_NOTIFICATION = 2505;
    private final BroadcastReceiver mDeepLinkReceiver = null;
    ImageView imgViewKundli, imgViewLogo;
    long scaleDuration = 500l;
    long alphaDuration = 500l;
    long splashTotalDuration = 1500l;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest mLocationRequest;
    private LocationCallback mLocationCallback;
    private Location mCurrentLocation;
    private InstallReferrerClient referrerClient;

    public ActAppSplash() {
        super(R.string.astrosage_name);
    }

    public void onCreate(Bundle arg0) {
        super.onCreate(arg0);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            final WindowInsetsController insetsController = getWindow().getInsetsController();
            if (insetsController != null) {
                // Hide status bar and navigation bar
                insetsController.hide(WindowInsets.Type.navigationBars());
                insetsController.setSystemBarsBehavior(WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE);
            }
        }
        try {
            // added by abhishek to prevent restarting the app while resume
            if (!isTaskRoot() && getIntent().hasCategory(Intent.CATEGORY_LAUNCHER)
                    && getIntent().getAction() != null && getIntent().getAction().equals(Intent.ACTION_MAIN)) {
                redirectToActivity();
                finish();
                return;
            }//end
            this.requestWindowFeature(Window.FEATURE_NO_TITLE);
            setContentView(R.layout.splash_screen);
            TextView splashTaglineTV = findViewById(R.id.txtViewInnovators);
            FontUtils.changeFont(this, splashTaglineTV, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
            com.ojassoft.astrosage.vartalive.activities.BaseActivity.applyEdgeToEdgeInsets(this);
            // Only fetch and set install time if first launch
            UnlockBrihatHelperClass unlockBrihatHelperClass = new UnlockBrihatHelperClass(this);
            unlockBrihatHelperClass.handleFirstTimeInstall();
            // Logging for install time retrieval is handled in UnlockBrihatHelperClass

            hourlyTopicSubscription();

            // --- Check for user login status and premium plan ---
            // This logic determines if the user is logged in and what subscription plan they have.
            boolean isUserLoggedIn = CUtils.isUserLogedIn(this);
            boolean hasPremiumPlan = false;

            if (isUserLoggedIn) {
                // User is logged in, now check their subscription plan ID from SharedPreferences.
                int currentPlanId = CUtils.getUserPurchasedPlanFromPreference(this);

                // Define a list of premium plans that should prevent this dialog from showing.
                boolean isUserOnPremiumPlan =
                        currentPlanId == CGlobalVariables.PLATINUM_PLAN_ID ||
                                currentPlanId == CGlobalVariables.PLATINUM_PLAN_ID_9 ||
                                currentPlanId == CGlobalVariables.PLATINUM_PLAN_ID_10 ||
                                currentPlanId == CGlobalVariables.KUNDLI_AI_PRIMIUM_PLAN_ID_11;
                Log.d("UserPlanCheck", "User is logged in. Plan ID: " + currentPlanId + ", HasPremium: " + hasPremiumPlan);
                if(!isUserOnPremiumPlan){
                    startService(new Intent(ActAppSplash.this,FetchPlanDetailsService.class));
                }
            }
            getConfigDataRequest(this);

            imgViewKundli = findViewById(R.id.imageViewKundli);
            imgViewLogo = findViewById(R.id.imageViewLogo);

            Intent intent = getIntent();
            Log.d("InstallRef", "intent1=" + intent);
            if (intent != null) {
                Log.d("InstallRef", "data1=" + intent.getData());
            }
            CUtils.fcmAnalyticsUserId(this);
            getFbDeepLinkUrl();
            //initFbDefferedLink();
            initInstallReferrer();
            initLocationService();
            startTagManagerService();
            handleOnIntent();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                if (!CUtils.checkNotificationPermissions(this)) {
                    CUtils.requestForNotificationPermissions(this, PERMISSION_NOTIFICATION);
                } else {
                    locationPermissionCheck();
                }
            } else {
                locationPermissionCheck();
            }
        } catch (Exception ex) {
            //
        }
        //callStoreDeviceDetailsApi();

    }



    public  void callStoreDeviceDetailsApi() {
       // ApiList api = RetrofitClient.getInstance().create(ApiList.class);
      //  Call<ResponseBody> call = api.getNotificationIfAstroBusy(getQueueParams());


    }
    public Map<String, String> getQueueParams() {

        String manufacturer = Build.MANUFACTURER; // Brand (e.g., Samsung, Xiaomi)
        String model = Build.MODEL; // Device Model (e.g., Galaxy S21)
        String device = Build.DEVICE; // Internal Device Name
        String brand = Build.BRAND; // Brand Name
        String hardware = Build.HARDWARE; // Hardware Info
        String product = Build.PRODUCT; // Product Name
        String board = Build.BOARD; // Board Name
        String androidVersion = Build.VERSION.RELEASE; // Android Version (e.g., 13)
        int sdkVersion = Build.VERSION.SDK_INT; // SDK Version (e.g., 33 for Android 13)
        String fingerprint = Build.FINGERPRINT; // Unique Build Fingerprint

//            Log.d("DeviceInfo", "Manufacturer: " + manufacturer);
//            Log.d("DeviceInfo", "Model: " + model);
//            Log.d("DeviceInfo", "Device: " + device);
//            Log.d("DeviceInfo", "Brand: " + brand);
//            Log.d("DeviceInfo", "Hardware: " + hardware);
//            Log.d("DeviceInfo", "Product: " + product);
//            Log.d("DeviceInfo", "Board: " + board);
//            Log.d("DeviceInfo", "Android Version: " + androidVersion);
//            Log.d("DeviceInfo", "SDK Version: " + sdkVersion);
//            Log.d("DeviceInfo", "Fingerprint: " + fingerprint);

        String category = getPhoneCategory();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.APP_KEY, com.ojassoft.astrosage.varta.utils.CUtils.getApplicationSignatureHashCode(this));
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.PACKAGE_NAME, com.ojassoft.astrosage.varta.utils.CUtils.getAppPackageName(this));
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.APP_VERSION, "" + BuildConfig.VERSION_NAME);
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.DEVICE_ID, "" + com.ojassoft.astrosage.varta.utils.CUtils.getMyAndroidId(this));

        Log.d("queueParams", "getQueueParams: "+headers);
        return headers;
    }

    public static String getPhoneCategory() {
        String manufacturer = Build.MANUFACTURER != null ? Build.MANUFACTURER.toLowerCase() : "";
        String model = Build.MODEL != null ? Build.MODEL.toLowerCase() : "";
        Log.d("DeviceInfo", "Model: " + model);

        // Samsung categorization
        if (manufacturer.contains("samsung")) {
            if (model.contains("sm-s") || model.contains("galaxy s") || model.contains("galaxy note")) {
                return "flagship";
            } else if (model.contains("galaxy a") || model.contains("galaxy m")) {
                return "mid-range";
            } else {
                return "budget";
            }
        }
        // Google Pixel categorization
        else if (manufacturer.contains("google")) {
            if (model.contains("pixel 8") || model.contains("pixel 7") || model.contains("pixel 6 pro")) {
                return "flagship";
            } else if (model.contains("pixel 6a") || model.contains("pixel 5")) {
                return "mid-range";
            } else {
                return "budget";
            }
        }
        // OnePlus categorization
        else if (manufacturer.contains("oneplus")) {
            if (model.contains("11") || model.contains("10 pro")) {
                return "flagship";
            } else if (model.contains("8t") || model.contains("7t")) {
                return "mid-range";
            } else {
                return "budget";
            }
        }
        // Xiaomi categorization
        else if (manufacturer.contains("xiaomi")) {
            if (model.contains("mi 12") || model.contains("mi 11") || model.contains("mi 10") || model.contains("mi 9t pro")) {
                return "flagship";
            } else if (model.contains("redmi note") || model.contains("redmi k")) {
                return "mid-range";
            } else {
                return "budget";
            }
        }
        // Default fallback for other manufacturers
        else {
            // Additional rules can be added here for other manufacturers
            return "unknown";
        }
    }


    private void toPerformSplashOperation() {
        applyAnimationOnKundli();
        applyAnimationOnLogo();
        holdScreen();
    }

    private void locationPermissionCheck() {
        if (!CUtils.checkLocationPermissions(this)) {
            CUtils.requestForLocationPermissions(this, this, PERMISSION_LOCATION);
        } else {
            getCurrentLocation();
            toPerformSplashOperation();
        }
    }

    private void startTagManagerService() {
        try {
            startService(new Intent(ActAppSplash.this, GetTagManagerDataService.class));
        } catch (Exception e) {
            //
        }
    }

    private void handleOnIntent() {
        Intent intent = getIntent();
        if (intent != null) {
            try {
                String kundliVirtualUrl = intent.getStringExtra(CGlobalVariables.KEY_KUNDALI_VIRTUAL_URL);
                if (kundliVirtualUrl == null) {
                    Uri data = intent.getData();
                    if (data != null) {
                        CUtils.saveStringData(ActAppSplash.this, CGlobalVariables.KEY_KUNDALI_VIRTUAL_URL, data.toString());
                        //Toast.makeText(this, "Data= "+data, Toast.LENGTH_LONG).show();
                    }
                } else {
                    CUtils.saveStringData(ActAppSplash.this, CGlobalVariables.KEY_KUNDALI_VIRTUAL_URL, kundliVirtualUrl);
                    //Toast.makeText(this, "URL= "+kundliVirtualUrl, Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                //
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    private void holdScreen() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                clearProductCache();
                onStartService();
            }
        }, splashTotalDuration);
    }

    public void onStartService() {
        startGcmRegistrationService();
        if (!CUtils.oldPreferencesHasBeenUpdated(ActAppSplash.this)) {
            getOldPreferences();
        }
        redirectToActivity();
    }

    private void startGcmRegistrationService() {
        try {
            startService(new Intent(this, com.ojassoft.astrosage.custompushnotification.MyCloudRegistrationService.class));//ADDED BY DEEPAK ON 21-11-2014
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void getOldPreferences() {

        SharedPreferences sharedPreferences = getSharedPreferences("KundliPref", MODE_PRIVATE);
        boolean NorthChartStyle = sharedPreferences.getBoolean("NorthChartStyle", true);
        int Ayanmashas = sharedPreferences.getInt("Ayanmashas", 0);
        boolean NotShowPlUnNa = sharedPreferences.getBoolean("NotShowPlUnNa", false);
        boolean UserWantCustomDatePicker = sharedPreferences.getBoolean(CGlobalVariables.APP_PREFS_UserWantCustomDatePicker, false);
        SharedPreferences sharedPreferencesFor10Charts = getSharedPreferences("KundliLanguagePref2", MODE_PRIVATE);
        boolean DO_NOT_SHOW_ME_UPGRADE_CHECK_BOX_AGAIN = sharedPreferencesFor10Charts.getBoolean("DO_NOT_SHOW_ME_UPGRADE_CHECK_BOX_AGAIN", false);

//		update in new preferences
        SharedPreferences sharedPreferencesNew = getSharedPreferences(CGlobalVariables.APP_PREFS_NAME, MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferencesNew.edit();
        if (NorthChartStyle) {
            sharedPrefEditor.putInt(CGlobalVariables.APP_PREFS_ChartStyle, CGlobalVariables.CHART_NORTH_STYLE);
        } else {
            sharedPrefEditor.putInt(CGlobalVariables.APP_PREFS_ChartStyle, CGlobalVariables.CHART_SOUTH_STYLE);
        }
        sharedPrefEditor.putInt(CGlobalVariables.APP_PREFS_Ayanmasha, Ayanmashas);
        sharedPrefEditor.putBoolean(CGlobalVariables.APP_PREFS_NotShowPlUnNa, NotShowPlUnNa);
        sharedPrefEditor.putBoolean(CGlobalVariables.APP_PREFS_UserWantCustomDatePicker, UserWantCustomDatePicker);
        sharedPrefEditor.putBoolean(CGlobalVariables.APP_PREFS_NotShowUpgradePlanPopUp, DO_NOT_SHOW_ME_UPGRADE_CHECK_BOX_AGAIN);
        sharedPrefEditor.putBoolean(CGlobalVariables.APP_PREFS_oldPreferencesCopied, true);
        sharedPrefEditor.commit();
    }


    private void applyAnimationOnLogo() {
        // Load animation
        Animation zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        imgViewLogo.startAnimation(zoomIn);
       /* imgViewLogo.post(new Runnable() {
            @Override
            public void run() {

                Animation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
                Animation ScaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                ScaleAnimation.setDuration(scaleDuration);
                alphaAnimation.setDuration(scaleDuration);
                imgViewLogo.startAnimation(alphaAnimation);
                imgViewLogo.startAnimation(ScaleAnimation);
            }
        });

        imgViewLogo.postDelayed(new Runnable() {

            @Override
            public void run() {
                Animation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
                alphaAnimation.setDuration(alphaDuration);
                imgViewLogo.startAnimation(alphaAnimation);
            }
        }, scaleDuration * 2);

        imgViewLogo.postDelayed(new Runnable() {

            @Override
            public void run() {
                imgViewLogo.setVisibility(View.GONE);
            }
        }, scaleDuration * 2 + alphaDuration);*/
    }

    private void applyAnimationOnKundli() {
// Load animation
        Animation zoomIn = AnimationUtils.loadAnimation(this, R.anim.zoom_in);
        imgViewKundli.startAnimation(zoomIn);
       /* imgViewKundli.post(new Runnable() {
            @Override
            public void run() {

                Animation alphaAnimation = new AlphaAnimation(0.0f, 1.0f);
                Animation ScaleAnimation = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                ScaleAnimation.setDuration(scaleDuration);
                alphaAnimation.setDuration(scaleDuration);
                imgViewKundli.startAnimation(alphaAnimation);
                imgViewKundli.startAnimation(ScaleAnimation);
            }
        });


        imgViewKundli.postDelayed(new Runnable() {

            @Override
            public void run() {
                Animation alphaAnimation = new AlphaAnimation(1.0f, 0.0f);
                alphaAnimation.setDuration(alphaDuration);
                imgViewKundli.startAnimation(alphaAnimation);
            }
        }, scaleDuration * 2);


        imgViewKundli.postDelayed(new Runnable() {

            @Override
            public void run() {
                imgViewKundli.setVisibility(View.GONE);
            }
        }, scaleDuration * 2 + alphaDuration);
*/
    }

    private void redirectToActivity() {

        try {
            //handle push notification from fcm console while user tab on notification tray.
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                String link = bundle.getString(KEY_NOTIFICATION_LINK);
                //String imgurl = bundle.getString(KEY_NOTIFICATION_IMG_URL);
                String title = bundle.getString("title");
                String body = bundle.getString("message");
                // if link(url) available in notification
                if (!TextUtils.isEmpty(link)) {

                    saveNotificationInLocalDb(body, title, link, "");

                    Intent resultIntent;
                    if (link.contains(PLAY_URL)) {
                        resultIntent = new Intent(Intent.ACTION_VIEW);
                        resultIntent.setData(Uri.parse(link));
                    } else {
                        resultIntent = new Intent(this, ActAppModule.class);
                        resultIntent.setAction(Intent.ACTION_VIEW);
                        resultIntent.setData(Uri.parse(link));
                    }
                    startActivity(resultIntent);
                    finish();
                    return;
                }
            }
        } catch (Exception ex) {
            // Log.e("ActAppSplashException ", ex.getMessage());
        }

        try {
            Intent intent;
            if (!CUtils.hasWizardShowFirstTime(ActAppSplash.this)) {
                intent = new Intent(ActAppSplash.this, ActWizardScreens.class);
                intent.putExtra("callerActivity", CGlobalVariables.APP_MODULE_SCREEN);
                intent.putExtra("isFromSplash", true);
            } else
                intent = new Intent(ActAppSplash.this, ActAppModule.class);
            startActivity(intent);
            ActAppSplash.this.finish();
        } catch (Exception ex) {
            //
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);


        if (requestCode == PERMISSION_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getCurrentLocation();
            } else {
                // Permission denied get lat lang city from IP details
                getLatLangCityFromIPDetails(this);
            }
            toPerformSplashOperation();
        } else if (requestCode == PERMISSION_NOTIFICATION) {
            locationPermissionCheck();
        }
    }

    /**
     * Function to clear existing product cache
     */
    private void clearProductCache() {
        String key = CGlobalVariables.Astroshop_Data_First_Time;
        boolean isFirstTime = CUtils.getBooleanData(this, key, true);
        if (isFirstTime) {
            for (int i = 0; i < 10; i++) {
                CUtils.saveStringData(this, CGlobalVariables.Astroshop_Data + i, "");

            }
            CUtils.saveBooleanData(this, key, false);
        }
    }


    private void getCurrentLocation() {
        startLocationUpdates();
    }

    private void initLocationService() {
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult != null) {
                    mCurrentLocation = locationResult.getLastLocation();
                    updateLocationUI();
                }
            }
        };

        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Update the UI displaying the location data
     * and toggling the buttons
     */
    private void updateLocationUI() {
        if (mCurrentLocation != null) {
            //Toast.makeText(this, "Lat: " + mCurrentLocation.getLatitude() + ", " + "Lng: " + mCurrentLocation.getLongitude(), Toast.LENGTH_LONG).show();
            double latitude = mCurrentLocation.getLatitude();
            double longitude = mCurrentLocation.getLongitude();

            CUtils.saveStringData(ActAppSplash.this, CGlobalVariables.KEY_LATITUDE, latitude + "");
            CUtils.saveStringData(ActAppSplash.this, CGlobalVariables.KEY_LONGITUDE, longitude + "");

            try {
                Data input = new Data.Builder()
                        .putString("latitude", latitude + "")
                        .putString("longitude", longitude + "").build();

                Constraints constraints = new Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build();

                OneTimeWorkRequest requestFindLocation = new OneTimeWorkRequest.Builder(FindPlaceService.class)
                        .setInputData(input)
                        .setInitialDelay(1, TimeUnit.SECONDS)
                        .setBackoffCriteria(BackoffPolicy.LINEAR,
                                OneTimeWorkRequest.MIN_BACKOFF_MILLIS,
                                TimeUnit.MILLISECONDS)
                        .setConstraints(constraints)
                        .addTag("FindPlaceService")
                        .build();

                WorkManager.getInstance(getApplicationContext())
                        .enqueueUniqueWork("FindPlaceService", ExistingWorkPolicy.KEEP, requestFindLocation);
            } catch (Exception e) {
                //
            }
            stopLocationUpdates();
        }
    }

    private void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            getLatLangCityFromIPDetails(this); // If permissions are not granted, get lat lang city from IP details
            return;
        }
        mFusedLocationClient.requestLocationUpdates(mLocationRequest, mLocationCallback, Looper.myLooper());
    }

    public void stopLocationUpdates() {
        // Removing location updates
        if (mFusedLocationClient == null) return;
        mFusedLocationClient
                .removeLocationUpdates(mLocationCallback)
                .addOnCompleteListener(this, new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                });
    }

    private void saveNotificationInLocalDb(String mss, String tit, String link, String imgurl) {

        Context context = ActAppSplash.this;
        if (context == null) return;
        NotificationModel notificationModel = new NotificationModel();
        notificationModel.setMessage(mss);
        notificationModel.setTitle(tit);
        notificationModel.setLink(link);
        notificationModel.setNtId("");
        notificationModel.setExtra("");
        notificationModel.setImgUrl(imgurl);
        notificationModel.setBlogId("");
        notificationModel.setNotificationType(NOTIFICATION_PUSH);
        notificationModel.setTimestamp(System.currentTimeMillis() + "");

        NotificationDBManager dbManager = new NotificationDBManager(context);
        dbManager.addNotification(notificationModel);
    }

    private void initInstallReferrer() {
        referrerClient = InstallReferrerClient.newBuilder(this).build();
        referrerClient.startConnection(new InstallReferrerStateListener() {
            @Override
            public void onInstallReferrerSetupFinished(int responseCode) {
                Log.e("InstallRef", "responseCode = " + responseCode);
                switch (responseCode) {
                    case InstallReferrerClient.InstallReferrerResponse.OK: {
                        Log.e("InstallRef", "responseCode = OK");
                        try {
                            ReferrerDetails response = referrerClient.getInstallReferrer();
                            String referrerUrl = response.getInstallReferrer();
                            Log.e("InstallRef", "referrerUrl =" + referrerUrl);
                            String referrerUrlPref = CUtils.getStringData(ActAppSplash.this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_INSTALL_REFERRER, "");
                            if (referrerUrl != null && TextUtils.isEmpty(referrerUrlPref)) {
                                CUtils.saveStringData(ActAppSplash.this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_INSTALL_REFERRER, referrerUrl);
                            }
                            CUtils.saveStringData(ActAppSplash.this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_REFERRER_URL, referrerUrl);
                            //long referrerClickTime = response.getReferrerClickTimestampSeconds();
                            //long appInstallTime = response.getInstallBeginTimestampSeconds();
                            //boolean instantExperienceLaunched = response.getGooglePlayInstantParam();

                        } catch (RemoteException e) {
                            e.printStackTrace();
                        }
                        break;
                    }
                    case InstallReferrerClient.InstallReferrerResponse.FEATURE_NOT_SUPPORTED:
                        // API not available on the current Play Store app.
                        Log.e("InstallRef", "responseCode = FEATURE_NOT_SUPPORTED");
                        break;
                    case InstallReferrerClient.InstallReferrerResponse.SERVICE_UNAVAILABLE:
                        // Connection couldn't be established.
                        Log.e("InstallRef", "responseCode = SERVICE_UNAVAILABLE");
                        break;
                }
            }

            @Override
            public void onInstallReferrerServiceDisconnected() {
                // Try to restart the connection on the next request to
                // Google Play by calling the startConnection() method.
            }
        });
    }


    private void getFbDeepLinkUrl() {
        try {
            Uri targetUrl = AppLinks.getTargetUrlFromInboundIntent(this, getIntent());
            Log.d("TestGCM", "targetUrl=" + targetUrl);
            if (targetUrl == null) return;
            String targetUri = targetUrl.toString();
            if (!TextUtils.isEmpty(targetUri)) {
                AstrosageKundliApplication.facebookDeepLinkUrl = targetUri;
                CUtils.saveStringData(ActAppSplash.this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_FB_AD_LINK, targetUri);
            }
        } catch (Exception e) {
            //
        }
    }

    public void getConfigDataRequest(Context context) {
        ApiList api = RetrofitClient.getInstance().create(ApiList.class);
        Call<ResponseBody> call = api.getConfigDataReq(CUtils.getApplicationSignatureHashCode(context), "",BuildConfig.VERSION_NAME, com.ojassoft.astrosage.varta.utils.CUtils.getAppPackageName(AstrosageKundliApplication.getAppContext()));
        //Log.e("ConfigDataReq", " myResponseUrl= " + call.request().url());
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.body() != null) {
                        String myResponse = response.body().string();
                        CUtils.parseAndSaveAppConfigData(ActAppSplash.this, myResponse);
                    }
                } catch (Exception e) {
                    //Log.e("ConfigDataReq", " onFailure= " + e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("ConfigDataReq", " onFailure= " + t);
            }
        });
    }

    private void hourlyTopicSubscription() {
        if (TextUtils.isEmpty(CUtils.getStringData(this, CGlobalVariables.IS_NEW_APP_INSTALL, ""))) {
            String topic = CGlobalVariables.APP_INSTALL_TOPIC_TEMPLATE + CUtils.getHourOfTheDay();
            CUtils.saveStringData(this, CGlobalVariables.IS_NEW_APP_INSTALL, topic);
            try {
                CUtils.subscribeTopics("", topic, getApplicationContext());
            } catch (Exception e) {
                Log.e("FIREBASE_TOPIC_LOGS", "Exception 1: ", e);
            }
        }

        String oldTopic = "";

        if (!TextUtils.isEmpty(CUtils.getStringData(this, CGlobalVariables.LAST_APP_USED_TOPIC_KEY, ""))) {
            oldTopic = CUtils.getStringData(this, CGlobalVariables.LAST_APP_USED_TOPIC_KEY, "");
        }

        String newTopic = CGlobalVariables.LAST_USED_HOUR_TOPIC_TEMPLATE + CUtils.getHourOfTheDay();
        Log.d("hourlyTopic", "hourlyTopicSubscription newTopic: " + newTopic);
        CUtils.saveStringData(this, CGlobalVariables.LAST_APP_USED_TOPIC_KEY, newTopic);
        try {
            if (!oldTopic.equalsIgnoreCase(newTopic)) {
                CUtils.unSubscribeTopics("", oldTopic, getApplicationContext());
                CUtils.subscribeTopics("", newTopic, getApplicationContext());
            }
        } catch (Exception e) {
            Log.e("FIREBASE_TOPIC_LOGS", "Exception 3: ", e);
        }
    }

}
