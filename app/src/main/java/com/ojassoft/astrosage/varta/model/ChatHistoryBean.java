package com.ojassoft.astrosage.varta.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ChatHistoryBean implements Serializable {

    @SerializedName("userPhoneNo")
    @Expose
    private String userPhoneNo;
    @SerializedName("astrologerPhoneNo")
    @Expose
    private String astrologerPhoneNo;
    @SerializedName("astrologerName")
    @Expose
    private String astrologerName;
    @SerializedName("consultationTime")
    @Expose
    private String consultationTime;
    @SerializedName("callDuration")
    @Expose
    private String callDuration;
    @SerializedName("callAmount")
    @Expose
    private String callAmount;
    @SerializedName("astrologerImageFile")
    @Expose
    private String astrologerImageFile;
    @SerializedName("astrologerServiceRs")
    @Expose
    private String astrologerServiceRs;
    @SerializedName("astroWalletId")
    @Expose
    private String astroWalletId;
    @SerializedName("urlText")
    @Expose
    private String urlText;

    @SerializedName("consultationMode")
    @Expose
    private String consultationMode;
    @SerializedName("callChatId")
    @Expose
    private String callChatId;

    @SerializedName("refundStatus")
    @Expose
    private String refundStatus;

    @SerializedName("consultationId")
    @Expose
    private String consultationId;

    @SerializedName("aiAstroId")
    @Expose
    private String aiAstroId;

    @SerializedName("astroImageFileLarge")
    @Expose
    private String astroImageFileLarge;

    @SerializedName("astroExpertise")
    @Expose
    private String astroExpertise;

    @SerializedName("iofch")
    @Expose
    private boolean isFreeForChat;

    @SerializedName("durationUnitType")
    @Expose
    private String durationUnitType;
    @SerializedName("calldurationmin")
    @Expose
    private String callDurationMin;

    public String getCallDurationMin() {
        return callDurationMin;
    }

    public void setCallDurationMin(String callDurationMin) {
        this.callDurationMin = callDurationMin;
    }

    public String getDurationUnitType() {
        return durationUnitType;
    }

    public void setDurationUnitType(String durationUnitType) {
        this.durationUnitType = durationUnitType;
    }

    public String getConsultationMode() {
        return consultationMode;
    }

    public void setConsultationMode(String consultationMode) {
        this.consultationMode = consultationMode;
    }

    public String getCallChatId() {
        return callChatId;
    }

    public void setCallChatId(String callChatId) {
        this.callChatId = callChatId;
    }

    public String getUserPhoneNo() {
        return userPhoneNo;
    }

    public void setUserPhoneNo(String userPhoneNo) {
        this.userPhoneNo = userPhoneNo;
    }

    public String getAstrologerPhoneNo() {
        return astrologerPhoneNo;
    }

    public void setAstrologerPhoneNo(String astrologerPhoneNo) {
        this.astrologerPhoneNo = astrologerPhoneNo;
    }

    public String getAstrologerName() {
        return astrologerName;
    }

    public void setAstrologerName(String astrologerName) {
        this.astrologerName = astrologerName;
    }

    public String getConsultationTime() {
        return consultationTime;
    }

    public void setConsultationTime(String consultationTime) {
        this.consultationTime = consultationTime;
    }

    public String getCallDuration() {
        return callDuration;
    }

    public void setCallDuration(String callDuration) {
        this.callDuration = callDuration;
    }

    public String getCallAmount() {
        return callAmount;
    }

    public void setCallAmount(String callAmount) {
        this.callAmount = callAmount;
    }

    public String getAstrologerImageFile() {
        return astrologerImageFile;
    }

    public void setAstrologerImageFile(String astrologerImageFile) {
        this.astrologerImageFile = astrologerImageFile;
    }

    public String getAstrologerServiceRs() {
        return astrologerServiceRs;
    }

    public void setAstrologerServiceRs(String astrologerServiceRs) {
        this.astrologerServiceRs = astrologerServiceRs;
    }

    public String getAstroWalletId() {
        return astroWalletId;
    }

    public void setAstroWalletId(String astroWalletId) {
        this.astroWalletId = astroWalletId;
    }

    public String getUrlText() {
        return urlText;
    }

    public void setUrlText(String urlText) {
        this.urlText = urlText;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    public String getConsultationId() {
        return consultationId;
    }

    public void setConsultationId(String consultationId) {
        this.consultationId = consultationId;
    }

    public String getAiAstroId() {
        return aiAstroId;
    }

    public void setAiAstroId(String aiAstroId) {
        this.aiAstroId = aiAstroId;
    }

    public String getAstroImageFileLarge() {
        return astroImageFileLarge;
    }

    public void setAstroImageFileLarge(String astroImageFileLarge) {
        this.astroImageFileLarge = astroImageFileLarge;
    }

    public String getAstroExpertise() {
        return astroExpertise;
    }

    public void setAstroExpertise(String astroExpertise) {
        this.astroExpertise = astroExpertise;
    }

    public boolean isFreeForChat() {
        return isFreeForChat;
    }

    public void setFreeForChat(boolean freeForChat) {
        isFreeForChat = freeForChat;
    }
}
