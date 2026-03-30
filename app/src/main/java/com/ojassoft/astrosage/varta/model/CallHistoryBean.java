package com.ojassoft.astrosage.varta.model;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import androidx.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CallHistoryBean implements Comparable<CallHistoryBean>, Serializable {

    String type;
    String userPhoneNo;
    String astrologerPhoneNo;
    String astrologerName;
    String consultationTime;
    String callDuration;
    String callAmount;
    String astrologerImageFile;
    String astrologerServiceRs;
    String astroWalletId;
    String urlText;
    String consultationMode;
    String callChatId;
    String serviceIdId;
    String refundStatus;
    String consultationId;
    String provider;

    String recordingUrl;

    private String aiAstroId;

    private String astroImageFileLarge;

    private String astroExpertise;

    private boolean isFreeForChat;
    private String durationUnitType;
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

    public String getUrlText() {
        return urlText;
    }

    public void setUrlText(String urlText) {
        this.urlText = urlText;
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

    public String getServiceIdId() {
        return serviceIdId;
    }

    public void setServiceIdId(String serviceIdId) {
        this.serviceIdId = serviceIdId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(String refundStatus) {
        this.refundStatus = refundStatus;
    }

    @Override
    public int compareTo(@NonNull CallHistoryBean o) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
        try {
            return dateFormat.parse(o.getConsultationTime()).compareTo(dateFormat.parse(getConsultationTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String getConsultationId() {
        return consultationId;
    }

    public void setConsultationId(String consultationId) {
        this.consultationId = consultationId;
    }

    public String getRecordingUrl() {
        return recordingUrl;
    }

    public void setRecordingUrl(String recordingUrl) {
        this.recordingUrl = recordingUrl;
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

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }
}
