package com.ojassoft.astrosage.ui.JobServices;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by ojas-20 on 29/8/17.
 */

public class FindPlaceService extends Worker {

    private static final int MAX_RETRY_ATTEMPTS = 1;
    private Context context;

    public FindPlaceService(@NonNull Context appContext, @NonNull WorkerParameters params) {
        super(appContext, params);
        context = appContext;
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.e("FindPlaceService", "doWork()");
        if (context == null) {
            context = AstrosageKundliApplication.getAppContext();
        }

        int attempt = getRunAttemptCount(); // 0 = first run, 1 = 1st retry, etc.

        Data input = getInputData();
        String latitude = input.getString("latitude");
        String longitude = input.getString("longitude");
        List<Address> addresses = null;
        Geocoder geocoder = new Geocoder(context, Locale.ENGLISH);
        try {
            addresses = geocoder.getFromLocation(
                    Double.parseDouble(latitude),
                    Double.parseDouble(longitude), 1);
            //Log.e("FindPlaceService", "doWork() attempt=" + attempt + " addresses=" + addresses);
            if (addresses == null || addresses.isEmpty()) {
                // No result → retry
                if (attempt < MAX_RETRY_ATTEMPTS) {
                    return Result.retry(); // retry up to MAX_RETRY_ATTEMPTS times
                } else {
                    return Result.failure(); // stop after MAX_RETRY_ATTEMPTS retries
                }
            }
            getCityFromAddress(addresses);

        } catch (IOException e) {
            //Log.e("FindPlaceService", attempt+" doWork() IOException: " + e.getMessage());
            // Service not available → retry
            if (attempt < MAX_RETRY_ATTEMPTS) {
                return Result.retry(); // retry up to MAX_RETRY_ATTEMPTS times
            } else {
                return Result.failure(); // stop after MAX_RETRY_ATTEMPTS retries
            }
        } catch (Exception e) {
            //Log.e("FindPlaceService", attempt+" doWork() Exception: " + e.getMessage());
            // Unexpected error → fail
            return Result.failure();
        }

        return Result.success();
    }

    @Override
    public void onStopped() {

    }

    private void getCityFromAddress(List<Address> addresses) {
        if (addresses == null || addresses.size() == 0) {
            return;
        }
        try {
            Address address = addresses.get(0);

            String state = address.getAdminArea();
            String city = address.getLocality();
            if (TextUtils.isEmpty(city)) {
                city = address.getSubAdminArea();
                if (TextUtils.isEmpty(city)) {
                    city = address.getAdminArea();
                }
            }
            //Log.e("FindPlaceService", state + " city=" + city);
            CUtils.saveStringData(context, CGlobalVariables.KEY_CITY, city);
            CUtils.saveStringData(context, CGlobalVariables.KEY_STATE, state);
        } catch (Exception e) {
            //
        }
    }
}
