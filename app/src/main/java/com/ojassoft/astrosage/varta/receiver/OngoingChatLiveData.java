package com.ojassoft.astrosage.varta.receiver;

import android.content.Intent;

import androidx.lifecycle.LiveData;

public class OngoingChatLiveData extends LiveData<Intent> {
    public void setOngoingChatView(Intent intent) {
        postValue(intent);
    }
}
