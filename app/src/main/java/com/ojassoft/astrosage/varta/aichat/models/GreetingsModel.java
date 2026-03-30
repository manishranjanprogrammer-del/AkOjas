package com.ojassoft.astrosage.varta.aichat.models;

import com.google.gson.annotations.SerializedName;

public class GreetingsModel {
    @SerializedName("status")
    private int status;

    @SerializedName("msg")
    private String message;

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
