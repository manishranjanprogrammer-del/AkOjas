package com.ojassoft.astrosage.varta.model;

public class RechargeHistoryBean {

    String rechargeType;
    String rechargeAmount;
    String rechargeDateTime;
    String displayMsg;
    String orderId;
    String rechargeId;
    String message;
    String referralMsg;

    public String getReferralMsg() {
        return referralMsg;
    }

    public void setReferralMsg(String referralMsg) {
        this.referralMsg = referralMsg;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDisplayMsg() {
        return displayMsg;
    }

    public void setDisplayMsg(String displayMsg) {
        this.displayMsg = displayMsg;
    }

    public String getRechargeType() {
        return rechargeType;
    }

    public void setRechargeType(String rechargeType) {
        this.rechargeType = rechargeType;
    }

    public String getRechargeAmount() {
        return rechargeAmount;
    }

    public void setRechargeAmount(String rechargeAmount) {
        this.rechargeAmount = rechargeAmount;
    }

    public String getRechargeDateTime() {
        return rechargeDateTime;
    }

    public void setRechargeDateTime(String rechargeDateTime) {
        this.rechargeDateTime = rechargeDateTime;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getOrderId() {
        return orderId;
    }

    public String getRechargeId() {
        return rechargeId;
    }

    public void setRechargeId(String rechargeId) {
        this.rechargeId = rechargeId;
    }


}
