package com.ojassoft.astrosage.varta.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ConnectAgoraCallBean {

    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("callsid")
    @Expose
    private String callsid;
    @SerializedName("tokenid")
    @Expose
    private String tokenid;
    @SerializedName("userphone")
    @Expose
    private String userphone;
    @SerializedName("countrycode")
    @Expose
    private String countrycode;
    @SerializedName("userfirstname")
    @Expose
    private String userfirstname;
    @SerializedName("identity_user")
    @Expose
    private String identityUser;
    @SerializedName("identity_astrologer")
    @Expose
    private String identityAstrologer;
    @SerializedName("talktime")
    @Expose
    private String talktime;
    @SerializedName("exophone")
    @Expose
    private String exophone;
    @SerializedName("astrologername")
    @Expose
    private String astrologername;
    @SerializedName("callcharge")
    @Expose
    private String callcharge;
    @SerializedName("durationinsecs")
    @Expose
    private String durationinsecs;
    @SerializedName("agoratokenid")
    @Expose
    private String agoratokenid;
    @SerializedName("status")
    @Expose
    private String status;

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

    public String getTokenid() {
        return tokenid;
    }

    public void setTokenid(String tokenid) {
        this.tokenid = tokenid;
    }

    public String getUserphone() {
        return userphone;
    }

    public void setUserphone(String userphone) {
        this.userphone = userphone;
    }

    public String getCountrycode() {
        return countrycode;
    }

    public void setCountrycode(String countrycode) {
        this.countrycode = countrycode;
    }

    public String getUserfirstname() {
        return userfirstname;
    }

    public void setUserfirstname(String userfirstname) {
        this.userfirstname = userfirstname;
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

    public String getTalktime() {
        return talktime;
    }

    public void setTalktime(String talktime) {
        this.talktime = talktime;
    }

    public String getExophone() {
        return exophone;
    }

    public void setExophone(String exophone) {
        this.exophone = exophone;
    }

    public String getAstrologername() {
        return astrologername;
    }

    public void setAstrologername(String astrologername) {
        this.astrologername = astrologername;
    }

    public String getCallcharge() {
        return callcharge;
    }

    public void setCallcharge(String callcharge) {
        this.callcharge = callcharge;
    }

    public String getDurationinsecs() {
        return durationinsecs;
    }

    public void setDurationinsecs(String durationinsecs) {
        this.durationinsecs = durationinsecs;
    }

    public String getAgoratokenid() {
        return agoratokenid;
    }

    public void setAgoratokenid(String agoratokenid) {
        this.agoratokenid = agoratokenid;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

}