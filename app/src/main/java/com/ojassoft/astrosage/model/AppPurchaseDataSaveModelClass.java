package com.ojassoft.astrosage.model;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by ojas-10 on 22/4/16.
 */
public class AppPurchaseDataSaveModelClass implements Parcelable {
    private  String signature;
    private String purchaseData;
    private String developerPayload;
    private String purchaseUserID;
    private String price;
    private String priceCurrencycode;
    private String problem;
    private String layoutPosititon;

    public String getLayoutPosititon() {
        return layoutPosititon;
    }

    public void setLayoutPosititon(String layoutPosititon) {
        this.layoutPosititon = layoutPosititon;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getChatID() {
        return chatID;
    }

    public void setChatID(String chatID) {
        this.chatID = chatID;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    private String chatID;
    private String type;


    public String getPriceCurrencycode() {
        return priceCurrencycode;
    }

    public void setPriceCurrencycode(String priceCurrencycode) {
        this.priceCurrencycode = priceCurrencycode;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public  AppPurchaseDataSaveModelClass()
    {

    }

    protected AppPurchaseDataSaveModelClass(Parcel in) {
        signature = in.readString();
        purchaseData = in.readString();
        developerPayload = in.readString();
        purchaseUserID = in.readString();
        price=in.readString();
        priceCurrencycode = in.readString();
        problem=in.readString();
        chatID=in.readString();
        type=in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(signature);
        dest.writeString(purchaseData);
        dest.writeString(developerPayload);
        dest.writeString(purchaseUserID);
        dest.writeString(price);
        dest.writeString(priceCurrencycode);
        dest.writeString(problem);
        dest.writeString(chatID);
        dest.writeString(type);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AppPurchaseDataSaveModelClass> CREATOR = new Creator<AppPurchaseDataSaveModelClass>() {
        @Override
        public AppPurchaseDataSaveModelClass createFromParcel(Parcel in) {
            return new AppPurchaseDataSaveModelClass(in);
        }

        @Override
        public AppPurchaseDataSaveModelClass[] newArray(int size) {
            return new AppPurchaseDataSaveModelClass[size];
        }
    };

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public String getPurchaseData() {
        return purchaseData;
    }

    public void setPurchaseData(String purchaseData) {
        this.purchaseData = purchaseData;
    }

    public String getDeveloperPayload() {
        return developerPayload;
    }

    public void setDeveloperPayload(String developerPayload) {
        this.developerPayload = developerPayload;
    }


    public String getPurchaseUserID() {
        return purchaseUserID;
    }

    public void setPurchaseUserID(String purchaseUserID) {
        this.purchaseUserID = purchaseUserID;
    }


}
