package com.ojassoft.astrosage.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PaymentOptionDTO implements Serializable {
	@SerializedName("payOptId")
	private String payOptId;
	@SerializedName("payOptName")
	private String payOptName;
	
	public PaymentOptionDTO(String payOptId, String payOptName) {
		super();
		this.payOptId = payOptId;
		this.payOptName = payOptName;
	}
	public String getPayOptId() {
		return payOptId;
	}
	public void setPayOptId(String payOptId) {
		this.payOptId = payOptId;
	}
	public String getPayOptName() {
		return payOptName;
	}
	public void setPayOptName(String payOptName) {
		this.payOptName = payOptName;
	}
}
