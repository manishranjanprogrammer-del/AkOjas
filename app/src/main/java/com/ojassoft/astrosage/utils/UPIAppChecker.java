package com.ojassoft.astrosage.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.PackageInfo;
import android.net.Uri;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ojassoft.astrosage.varta.model.UPIAppModel;

import java.util.ArrayList;
import java.util.List;

public class UPIAppChecker {

    // Package names of popular UPI apps
    public static final String PACKAGE_PHONEPE = "com.phonepe.app";
    public static final String PACKAGE_GOOGLE_PAY = "com.google.android.apps.nbu.paisa.user";
    public static final String PACKAGE_PAYTM = "net.one97.paytm";
    public static final String PACKAGE_BHIM = "in.org.npci.upiapp";
    public static final String PACKAGE_AMAZON = "in.amazon.mShop.android.shopping";
    public static final String PACKAGE_CRED = "com.dreamplug.androidapp";

    public static final String PACKAGE_PHONEPE_SIMULATOR = "com.phonepe.simulator";

    // Method to check if app is installed
    public static boolean isAppInstalled(Context context, String packageName) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getApplicationInfo(packageName, 0);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    /**
     * Checks whether a given UPI app is installed on the device
     * AND is ready to handle UPI payments (i.e., user is logged in
     * and has completed UPI setup).
     *
     * How it works:
     * 1. Verifies if the app with the given package name is installed.
     * 2. Creates a dummy/safe UPI deeplink intent (no real transaction).
     * 3. Tries to resolve the intent with the specified app.
     *    - If resolveActivity() is NOT null → app is installed and UPI-ready.
     *    - If null → either the app is not logged in, or UPI is not set up.
     *
     * @param context     Android context (usually Activity or Application)
     * @param packageName The package name of the UPI app (e.g., Google Pay, PhonePe)
     * @return true if the app is installed AND UPI-ready (logged in + setup done), false otherwise
     */
    public static boolean isUpiAppReady(Context context, String packageName) {
        try {
            // Step 1: Check installed
            context.getPackageManager().getApplicationInfo(packageName, 0);

            // Step 2: Dummy UPI intent
            //Uri uri = Uri.parse("upi://pay?pa=invalid@upi&pn=Test&mc=0000&tid=123456&tr=check&tn=Check&am=1&cu=INR");
            Uri uri = Uri.parse("upi://pay?pa=invalid@upi&pn=Test&mc=0000&tid=123456&tr=check&tn=Check&am=1&cu=INR");

            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            intent.setPackage(packageName);

            // Step 3: Check if activity resolves (means app is UPI ready i.e. logged in)
            boolean isIntentSafe = (intent.resolveActivity(context.getPackageManager()) != null);
            Log.e("TestPayment","packageName="+ packageName);
            Log.e("TestPayment","isIntentSafe="+ isIntentSafe);
            return isIntentSafe;

        } catch (PackageManager.NameNotFoundException e) {
            return false; // Not installed
        }
    }
    public static List<UPIAppModel> getInstalledApps(Context context, List<UPIAppModel> candidates) {
        PackageManager pm = context.getPackageManager();
        List<UPIAppModel> installed = new ArrayList<>();
        for (UPIAppModel app : candidates) {
            try {
                pm.getPackageInfo(app.getPackageName(), 0);
                installed.add(app);
            } catch (PackageManager.NameNotFoundException e) {
                // not installed
            }
        }
        return installed;
    }
    public static List<UPIAppModel> getInstalledAndReadyUpiApps(Context context,List<UPIAppModel> candidates) {
        List<UPIAppModel> installedAndReady = new ArrayList<>();
        PackageManager pm = context.getPackageManager();

        for (UPIAppModel app : candidates) {
            try {
                // 1️⃣ Check if app is installed
                pm.getApplicationInfo(app.getPackageName(), 0);

                // 2️⃣ Create safe dummy UPI intent
                Uri uri = Uri.parse(
                        "upi://pay" +
                                "?pa=invalid@upi" +
                                "&pn=Test" +
                                "&mc=0000" +
                                "&tr=upi_ready_check" +
                                "&tn=Check" +
                                "&am=1" +
                                "&cu=INR"
                );

                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                intent.setPackage(app.getPackageName());

                // 3️⃣ Check if app can handle UPI intent (UPI ready)
                boolean isUpiReady = intent.resolveActivity(pm) != null;

                Log.d("UPI_CHECK", app.getPackageName() + " ready = " + isUpiReady);

                if (isUpiReady) {
                    installedAndReady.add(app);
                }

            } catch (PackageManager.NameNotFoundException e) {
                // App not installed → ignore
            }
        }
        return installedAndReady;
    }

    // Example: show icons conditionally
    public static void showAppIcons(Context context, LinearLayout phonePeIcon, LinearLayout gpayIcon, LinearLayout paytmIcon,LinearLayout bhimIcon,LinearLayout credIcon) {
        if (isUpiAppReady(context, PACKAGE_PHONEPE)) {
            phonePeIcon.setVisibility(ImageView.VISIBLE);
        } else {
            phonePeIcon.setVisibility(ImageView.GONE);
        }

        if (isUpiAppReady(context, PACKAGE_GOOGLE_PAY)) {
            gpayIcon.setVisibility(ImageView.VISIBLE);
        } else {
            gpayIcon.setVisibility(ImageView.GONE);
        }

        if (isUpiAppReady(context, PACKAGE_PAYTM)) {
            paytmIcon.setVisibility(ImageView.VISIBLE);
        } else {
            paytmIcon.setVisibility(ImageView.GONE);
        }
        if (isUpiAppReady(context, PACKAGE_BHIM)) {
            bhimIcon.setVisibility(ImageView.VISIBLE);
        } else {
            bhimIcon.setVisibility(ImageView.GONE);
        }
        if (isUpiAppReady(context, PACKAGE_CRED)) {
            credIcon.setVisibility(ImageView.VISIBLE);
        } else {
            credIcon.setVisibility(ImageView.GONE);
        }
    }
}
