package com.ojassoft.astrosage.varta.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ConnectChatBean implements Serializable {

    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("callsid")
    @Expose
    private String callsid;
    @SerializedName("talktime")
    @Expose
    private String talktime;
    @SerializedName("durationinsecs")
    @Expose
    private String durationinsecs;
    @SerializedName("exophone")
    @Expose
    private String exophone;
    @SerializedName("userphone")
    @Expose
    private String userphone;
    @SerializedName("callcharge")
    @Expose
    private String callcharge;
    @SerializedName("identity_user")
    @Expose
    private String identityUser;
    @SerializedName("identity_astrologer")
    @Expose
    private String identityAstrologer;
    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("tokenid")
    @Expose
    private String token;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getCallsid() {
        return callsid;
    }

    public void setCallsid(String callsid) {
        this.callsid = callsid;
    }

    public String getTalktime() {
        return talktime;
    }

    public void setTalktime(String talktime) {
        this.talktime = talktime;
    }

    public String getDurationinsecs() {
        return durationinsecs;
    }

    public void setDurationinsecs(String durationinsecs) {
        this.durationinsecs = durationinsecs;
    }

    public String getExophone() {
        return exophone;
    }

    public void setExophone(String exophone) {
        this.exophone = exophone;
    }

    public String getUserphone() {
        return userphone;
    }

    public void setUserphone(String userphone) {
        this.userphone = userphone;
    }

    public String getCallcharge() {
        return callcharge;
    }

    public void setCallcharge(String callcharge) {
        this.callcharge = callcharge;
    }

    public String getIdentityUser() {
        return identityUser;
    }

    public void setIdentityUser(String identityUser) {
        this.identityUser = identityUser;
    }

    public String getIdentityAstrologer() {
        return identityAstrologer;
    }

    public void setIdentityAstrologer(String identityAstrologer) {
        this.identityAstrologer = identityAstrologer;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
