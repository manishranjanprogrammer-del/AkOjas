package com.ojassoft.astrosage.ui.JobServices;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.work.Data;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.ojassoft.astrosage.notification.DailyWeeklyMonthlyPredictions;
import com.ojassoft.astrosage.notification.StaticDailyWeeklyMonthlyPredictions;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

/**
 * Created by ojas-20 on 29/8/17.
 */

public class HoroscopeService extends Worker {

    private Context context;

    public HoroscopeService(@NonNull Context appContext, @NonNull WorkerParameters params) {
        super(appContext, params);
        context = appContext;
    }

    @NonNull
    @Override
    public Result doWork() {
        Log.e("HoroscopeService", "doWork()");
        if(context == null){
            context = AstrosageKundliApplication.getAppContext();
        }
        Data input = getInputData();

        boolean showStaticHoroscope = CUtils.getBooleanData(context, CGlobalVariables.showStaticHoroscope, true);
        String key = input.getString("key");
        if (showStaticHoroscope) {
            new StaticDailyWeeklyMonthlyPredictions(context, key);
        } else {
            new DailyWeeklyMonthlyPredictions(context, key);
        }
        return Result.success();
    }

    @Override
    public void onStopped() {

    }
}
