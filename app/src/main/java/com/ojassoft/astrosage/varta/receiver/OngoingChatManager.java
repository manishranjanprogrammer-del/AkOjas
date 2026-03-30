package com.ojassoft.astrosage.varta.receiver;

import android.content.Intent;

public class OngoingChatManager {
    private static final OngoingChatLiveData ongoingChatLiveData = new OngoingChatLiveData();

    public static void updateViews(Intent intent){
        ongoingChatLiveData.setOngoingChatView(intent);
    }

    public static OngoingChatLiveData getOngoingChatLiveData() {
        return ongoingChatLiveData;
    }
}
