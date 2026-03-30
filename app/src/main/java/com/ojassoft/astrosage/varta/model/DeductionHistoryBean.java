package com.ojassoft.astrosage.varta.model;

public class DeductionHistoryBean {
    private String deductedTime;
    private String displayMsg;
    private String orderId;
    private String deductedAmount;
    private String purchaseId;

    public String getDeductedTime() {
        return deductedTime;
    }

    public void setDeductedTime(String deductedTime) {
        this.deductedTime = deductedTime;
    }

    public String getDisplayMsg() {
        return displayMsg;
    }

    public void setDisplayMsg(String displayMsg) {
        this.displayMsg = displayMsg;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getDeductedAmount() {
        return deductedAmount;
    }

    public void setDeductedAmount(String deductedAmount) {
        this.deductedAmount = deductedAmount;
    }

    public String getPurchaseId() {
        return purchaseId;
    }

    public void setPurchaseId(String purchaseId) {
        this.purchaseId = purchaseId;
    }
}
