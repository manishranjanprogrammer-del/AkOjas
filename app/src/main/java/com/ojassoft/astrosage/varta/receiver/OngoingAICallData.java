package com.ojassoft.astrosage.varta.receiver;

import android.content.Intent;

public class OngoingAICallData {

    private static final OngoingCallChatLiveData ONGOING_CALL_LIVE_DATA = new OngoingCallChatLiveData();

    public static void updateViews(Intent intent){
        ONGOING_CALL_LIVE_DATA.setOngoingChatView(intent);
    }

    public static OngoingCallChatLiveData getOngoingChatLiveData() {
        return ONGOING_CALL_LIVE_DATA;
    }
}
