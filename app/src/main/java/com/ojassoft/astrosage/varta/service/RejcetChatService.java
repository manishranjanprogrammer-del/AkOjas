package com.ojassoft.astrosage.varta.service;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.Nullable;

import com.airbnb.lottie.model.layer.NullLayer;

public class RejcetChatService extends IntentService {
    /**
     * @deprecated
     */
    public RejcetChatService() {
        super(null);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Log.d("test_new_chat","onHandleIntent called");
    }
}
