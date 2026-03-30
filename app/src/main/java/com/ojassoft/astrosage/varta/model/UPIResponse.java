package com.ojassoft.astrosage.varta.model;

public class UPIResponse {
    public String status;        // SUCCESS / FAILURE / SUBMITTED
    public String txnId;
    public String txnRef;
    public String responseCode;

    @Override
    public String toString() {
        return "Status=" + status +
                ", TxnId=" + txnId +
                ", ResponseCode=" + responseCode +
                ", TxnRef=" + txnRef;
    }
}
