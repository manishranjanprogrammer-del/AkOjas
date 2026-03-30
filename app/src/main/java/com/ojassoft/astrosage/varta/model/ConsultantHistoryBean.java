package com.ojassoft.astrosage.varta.model;

import java.util.ArrayList;

public class ConsultantHistoryBean {
    String walletbalance;
    ArrayList<CallHistoryBean> callHistoryList;
    ArrayList<CallHistoryBean> videoCallHistoryBeanList;
    ArrayList<RechargeHistoryBean> rechargeHistoryList;
    ArrayList<ChatHistoryBean> chatHistoryBeanList;
    ArrayList<CallHistoryBean> liveHistoryBeanList;
    ArrayList<CallHistoryBean> giftHistoryBeanList;
    ArrayList<DeductionHistoryBean> deductionHistoryBeanArrayList;
    ArrayList<DeductionHistoryBean> deductionHistoryBeans;

    public ArrayList<ChatHistoryBean> getChatHistoryBeanList() {
        return chatHistoryBeanList;
    }

    public void setChatHistoryBeanList(ArrayList<ChatHistoryBean> chatHistoryBeanList) {
        this.chatHistoryBeanList = chatHistoryBeanList;
    }



    public String getWalletbalance() {
        return walletbalance;
    }

    public void setWalletbalance(String walletbalance) {
        this.walletbalance = walletbalance;
    }

    public ArrayList<CallHistoryBean> getCallHistoryList() {
        return callHistoryList;
    }

    public void setCallHistoryList(ArrayList<CallHistoryBean> callHistoryList) {
        this.callHistoryList = callHistoryList;
    }

    public ArrayList<RechargeHistoryBean> getRechargeHistoryList() {
        return rechargeHistoryList;
    }

    public void setRechargeHistoryList(ArrayList<RechargeHistoryBean> rechargeHistoryList) {
        this.rechargeHistoryList = rechargeHistoryList;
    }

    public ArrayList<CallHistoryBean> getLiveHistoryBeanList() {
        return liveHistoryBeanList;
    }

    public void setLiveHistoryBeanList(ArrayList<CallHistoryBean> liveHistoryBeanList) {
        this.liveHistoryBeanList = liveHistoryBeanList;
    }

    public ArrayList<CallHistoryBean> getGiftHistoryBeanList() {
        return giftHistoryBeanList;
    }

    public void setGiftHistoryBeanList(ArrayList<CallHistoryBean> giftHistoryBeanList) {
        this.giftHistoryBeanList = giftHistoryBeanList;
    }
    public ArrayList<DeductionHistoryBean> getDeductionHistoryBeanArrayList() {
        return deductionHistoryBeanArrayList;
    }
    public void setDeductionHistoryBeanArrayList(ArrayList<DeductionHistoryBean> deductionHistoryBeanArrayList) {
        this.deductionHistoryBeanArrayList = deductionHistoryBeanArrayList;
    }
    public ArrayList<CallHistoryBean> getVideoCallHistoryBeanList() {
        return videoCallHistoryBeanList;
    }

    public void setVideoCallHistoryBeanList(ArrayList<CallHistoryBean> videoCallHistoryBeanList) {
        this.videoCallHistoryBeanList = videoCallHistoryBeanList;
    }
}
