package com.ojassoft.astrosage.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by ojas-02 on 17/8/17.
 */

public class BigHorscopeProductModel {

    @SerializedName("P_Id")
    @Expose
    private String productId;
    @SerializedName("P_Title")
    @Expose
    private String title;
    @SerializedName("P_OriginalPriceInDollar")
    @Expose
    private String originalPriceInDollor;
    @SerializedName("P_PriceInDoller")
    @Expose
    private String priceInDollor;
    @SerializedName("P_OriginalPriceInRs")
    @Expose
    private String originalPriceInRS;
    @SerializedName("P_PriceInRs")
    @Expose
    private String priceInRS;
    @SerializedName("P_DeliveryTime")
    @Expose
    private String deliveryTime;
    @SerializedName("P_DetailDesc")
    @Expose
    private String detailDesc;
    @SerializedName("URLText")
    @Expose
    private String deepLinkUrl;

    @SerializedName("MessageOfCloudPlan1")
    @Expose
    private String messageOfCloudPlan1;

    @SerializedName("MessageOfCloudPlan2")
    @Expose
    private String messageOfCloudPlan2;

    @SerializedName("P_SmallImageURL")
    @Expose
    private String P_SmallImageURL;
    @SerializedName("P_LargeImageURL")
    @Expose
    private String P_LargeImageURL;

    public String getMessageOfCloudPlan1() {
        return messageOfCloudPlan1;
    }

    public void setMessageOfCloudPlan1(String messageOfCloudPlan1) {
        this.messageOfCloudPlan1 = messageOfCloudPlan1;
    }

    public String getMessageOfCloudPlan2() {
        return messageOfCloudPlan2;
    }

    public void setMessageOfCloudPlan2(String messageOfCloudPlan2) {
        this.messageOfCloudPlan2 = messageOfCloudPlan2;
    }


    public String getProductId() {
        return productId;
    }


    public String getTitle() {
        return title;
    }


    public String getOriginalPriceInDollor() {
        return originalPriceInDollor;
    }


    public String getPriceInDollor() {
        return priceInDollor;
    }


    public String getOriginalPriceInRS() {
        return originalPriceInRS;
    }


    public String getPriceInRS() {
        return priceInRS;
    }


    public String getDeliveryTime() {
        return deliveryTime;
    }


    public String getDetailDesc() {
        return detailDesc;
    }


    public String getDeepLinkUrl() {
        return deepLinkUrl;
    }

    public String getP_SmallImageURL() {
        return P_SmallImageURL;
    }

    public void setP_SmallImageURL(String p_SmallImageURL) {
        P_SmallImageURL = p_SmallImageURL;
    }

    public String getP_LargeImageURL() {
        return P_LargeImageURL;
    }

    public void setP_LargeImageURL(String p_LargeImageURL) {
        P_LargeImageURL = p_LargeImageURL;
    }
}
