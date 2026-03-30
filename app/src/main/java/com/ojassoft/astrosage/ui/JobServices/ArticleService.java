package com.ojassoft.astrosage.ui.JobServices;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.libojassoft.android.misc.CStartMagazineNotificationService;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

/**
 * Created by ojas-20 on 29/8/17.
 */

public class ArticleService extends Worker {

    private Context context;

    public ArticleService(@NonNull Context appContext, @NonNull WorkerParameters params) {
        super(appContext, params);
        context = appContext;
    }

    @NonNull
    @Override
    public Result doWork() {
        if (context == null) {
            context = AstrosageKundliApplication.getAppContext();
        }
        new CStartMagazineNotificationService(context,
                com.libojassoft.android.utils.LibCGlobalVariables.CONST_ASTROSAGE_KUNDLI_SERVICE_START_DELAY,
                CGlobalVariables.MY_AD_ID.trim(), CUtils.getLanguageCodeFromPreference(context))
                .startMyService();
        return Result.success();
    }

    @Override
    public void onStopped() {

    }
}
