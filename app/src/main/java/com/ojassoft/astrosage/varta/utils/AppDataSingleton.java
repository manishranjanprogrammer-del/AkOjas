package com.ojassoft.astrosage.varta.utils;

import com.ojassoft.astrosage.varta.model.AstrologerDetailBean;

import java.util.ArrayList;

public class AppDataSingleton {

    private static AppDataSingleton appDataSingleton;

    private String astrologerData;
    private String plusServiceDetails;
    private String premiumServiceDetail;
    private String aiPassServiceDetails;
    private String astrologerDataWithLimit;
    private String aiAstrologerRandomChatDetail;
    private String banneerData;
    private String consultHistoryData;
    private String walletRechargeData;
    private String isAiPassData;
    private ArrayList<AstrologerDetailBean> allAstrologerList;
    private String liveAstrologerData;
    private String historyListData;
    private String havePermissionRecordAudio;
    private String aiAstrologerDataWithLimit;

    public String getEncodedReferralUrl() {
        return encodedReferralUrl;
    }

    public void setEncodedReferralUrl(String encodedReferralUrl) {
        this.encodedReferralUrl = encodedReferralUrl;
    }

    private String encodedReferralUrl;

    private AppDataSingleton() { }

    public static AppDataSingleton getInstance() {
        if (appDataSingleton == null) {
            appDataSingleton = new AppDataSingleton();
        }
        return appDataSingleton;
    }

    public static void setInstanceNull() {
        appDataSingleton = null;
    }

    public String getAstrologerData() {
        return astrologerData;
    }

    public void setAstrologerData(String astrologerData) {
        this.astrologerData = astrologerData;
    }

    public String getPlusServiceDetails() {
        return plusServiceDetails;
    }

    public void setPlusServiceDetails(String plusServiceDetails) {
        this.plusServiceDetails = plusServiceDetails;
    }

    public String getPremiumServiceDetails() {
        return premiumServiceDetail;
    }

    public void setPremiumServiceDetails(String premiumServiceDetail) {
        this.premiumServiceDetail = premiumServiceDetail;
    }
    public String getAstrologerDataWithLimit() {
        return astrologerDataWithLimit;
    }

    public String getAiPassServiceDetails() {
        return aiPassServiceDetails;
    }

    public void setAiPassServiceDetails(String aiPassServiceDetails) {
        this.aiPassServiceDetails = aiPassServiceDetails;
    }

    public void setAstrologerDataWithLimit(String astrologerData) {
        this.astrologerDataWithLimit = astrologerData;
    }
    public String getAiAstrologerRandomChatDetail() {
        return aiAstrologerRandomChatDetail;
    }

    public void setAiAstrologerRandomChatDetail(String astroData) {
        this.aiAstrologerRandomChatDetail = astroData;
    }
    public String getBanneerData() {
        return banneerData;
    }

    public void setBanneerData(String banneerData) {
        this.banneerData = banneerData;
    }

    public String getConsultHistoryData() {
        return consultHistoryData;
    }

    public void setConsultHistoryData(String banneerData) {
        this.consultHistoryData = banneerData;
    }

    public String getWalletRechargeData() {
        return walletRechargeData;
    }

    public void setWalletRechargeData(String walletRechData) {
        this.walletRechargeData = walletRechData;
    }

    public String getIsAiPassData() {
        return isAiPassData;
    }

    public void setIsAiPassData(String isAiPassData) {
        this.isAiPassData = isAiPassData;
    }

    public ArrayList<AstrologerDetailBean> getAllAstrologerList() {
        return allAstrologerList;
    }

    public void setAllAstrologerList(ArrayList<AstrologerDetailBean> allAstrologerList) {
        this.allAstrologerList = allAstrologerList;
    }

    public String getLiveAstrologerData() {
        return liveAstrologerData;
    }

    public void setLiveAstrologerData(String liveAstrologerData) {
        this.liveAstrologerData = liveAstrologerData;
    }

    public String getHistoryListData() {
        return historyListData;
    }

    public void setHistoryListData(String historyListData) {
        this.historyListData = historyListData;
    }
    public String getHavePermissionRecordAudio() {
        return havePermissionRecordAudio;
    }

    public void setHavePermissionRecordAudio(String havePermissionRecordAudio) {
        this.havePermissionRecordAudio = havePermissionRecordAudio;
    }
}
