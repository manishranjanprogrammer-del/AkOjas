package com.ojassoft.astrosage.varta.twiliochat.chat.messages;

import android.content.Context;

import com.ojassoft.astrosage.R;

public class CustomStatusMessage extends StatusMessage{

    private String message;

    public CustomStatusMessage(String author, String message) {
        super(author);
        this.message = message;
    }

    @Override
    public String getMessageBody(Context context) {
        return message;
    }

    @Override
    public void setMessageBody(String body) {

    }

}
