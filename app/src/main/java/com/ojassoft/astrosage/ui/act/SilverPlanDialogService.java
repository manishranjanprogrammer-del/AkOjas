package com.ojassoft.astrosage.ui.act;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;

import androidx.annotation.Nullable;
import android.util.Log;

import java.util.Timer;
import java.util.TimerTask;


/*
* Service for open plan subscription dialog after 3 minutes
* Created by Monika
* */

public class SilverPlanDialogService extends IntentService {

    private static String TAG = "SILVER_DIALOG";
    private final int runTime = 30000;
    private final int interval = 1000;
    MyCountDownTimer myCountDownTimer;
    int i=0;
    Context context;

    public SilverPlanDialogService() {
        super("SilverPlanDialogService");
    }

    public SilverPlanDialogService(Context context) {
        super("SilverPlanDialogService");
        this.context = context;
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                // this code will be executed after 3 minutes
                Intent i = new Intent(SilverPlanDialogService.this, SilverPlanSubscriptionFragmentDailog.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(i);

            }
        }, runTime);
    }



    public class MyCountDownTimer extends CountDownTimer {
        /* myCountDownTimer = new MyCountDownTimer(10000, 1000);
              myCountDownTimer.start();*/
        public MyCountDownTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {

            Log.e(TAG,"mill "+millisUntilFinished + " "+ i);
            i++;
        }

        @Override
        public void onFinish() {
            Intent i = new Intent(SilverPlanDialogService.this, SilverPlanSubscriptionFragmentDailog.class);
            startActivity(i);
        }
    }
}
