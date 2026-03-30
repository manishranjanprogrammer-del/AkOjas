package com.ojassoft.astrosage.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class CardTypeDTO implements Serializable{

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



