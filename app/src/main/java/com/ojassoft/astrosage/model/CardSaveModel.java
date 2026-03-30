package com.ojassoft.astrosage.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ojas on २९/६/१६.
 */
public class CardSaveModel implements Serializable {

    private String getEmail() {
        return email;
    }

    public String currency;

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @SerializedName("email")
    private String email;


    @SerializedName("cardName")
    private String cardName;

    @SerializedName("cardType")
    private String cardType;

    @SerializedName("payOptType")
    private String payOptType;

    @SerializedName("dataAcceptedAt")
    private String dataAcceptedAt;

    @SerializedName("statusMessage")
    private String statusMessage;


    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getExpMonth() {
        return expMonth;
    }

    public void setExpMonth(String expMonth) {
        this.expMonth = expMonth;
    }

    public String getExpYear() {
        return expYear;
    }

    public void setExpYear(String expYear) {
        this.expYear = expYear;
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }

    @SerializedName("cardNumber")

    private String cardNumber;


    @SerializedName("expYear")
    private String expYear;


    @SerializedName("expMonth")
    private String expMonth;

    @SerializedName("bankName")
    private String bankName;

    public String getCardName() {
        return cardName;
    }
    public void setCardName(String cardName) {
        this.cardName = cardName;
    }
    public String getCardType() {
        return cardType;
    }
    public void setCardType(String cardType) {
        this.cardType = cardType;
    }
    public String getPayOptType() {
        return payOptType;
    }
    public void setPayOptType(String payOptType) {
        this.payOptType = payOptType;
    }
    public String getDataAcceptedAt() {
        return dataAcceptedAt;
    }
    public void setDataAcceptedAt(String dataAcceptedAt) {
        this.dataAcceptedAt = dataAcceptedAt;
    }
    public String getStatus() {
        return statusMessage;
    }
    public void setStatus(String status) {
        this.statusMessage = status;
    }
}
