package com.ojassoft.astrosage.varta.receiver;

import android.content.Intent;

import androidx.lifecycle.LiveData;

public class OngoingCallChatLiveData extends LiveData<Intent> {
    public void setOngoingChatView(Intent intent) {
        postValue(intent);
    }
}
