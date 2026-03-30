package com.ojassoft.astrosage.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class BigHorscopeServiceModel {

    @SerializedName("ServiceId")
    @Expose
    private String serviceId;
    @SerializedName("ServiceTitle")
    @Expose
    private String title;
    @SerializedName("OriginalPriceInDollor")
    @Expose
    private String originalPriceInDollor;
    @SerializedName("PriceInDollor")
    @Expose
    private String priceInDollor;
    @SerializedName("OriginalPriceInRS")
    @Expose
    private String originalPriceInRS;
    @SerializedName("PriceInRS")
    @Expose
    private String priceInRS;
    @SerializedName("DeliveryTime")
    @Expose
    private String deliveryTime;
    @SerializedName("DetailDesc")
    @Expose
    private String detailDesc;
    @SerializedName("URLText")
    @Expose
    private String deepLinkUrl;

    @SerializedName("PriceInDollorBeforeCloudPlanDiscount")
    @Expose
    private String priceInDollorBeforeCloudPlanDiscount;

    @SerializedName("PriceInRSBeforeCloudPlanDiscount")
    @Expose
    private String priceInRSBeforeCloudPlanDiscount;

    @SerializedName("LargeImageURL")
    @Expose

    private String largeImageUrl;


    @SerializedName("SmallImageURL")
    @Expose
    private String smallImageUrl;

    @SerializedName("MessageOfCloudPlan1")
    @Expose
    private String messageOfCloudPlanText1 = "";

    @SerializedName("MessageOfCloudPlan2")
    @Expose
    private String messageOfCloudPlanText2 = "";
    @SerializedName("ReportAvailableInLang")
    @Expose
    private String ReportAvailableInLang = "";

    @SerializedName("ServiceName")
    @Expose
    private String serviceName;
    @SerializedName("SamplePdfURL")
    @Expose
    private String samplePdfUrl;
    @SerializedName("ServiceNameTitle")
    @Expose
    private String serviceNameTitle;

    public String getPriceInDollorBeforeCloudPlanDiscount() {
        return priceInDollorBeforeCloudPlanDiscount;
    }

    public void setPriceInDollorBeforeCloudPlanDiscount(String priceInDollorBeforeCloudPlanDiscount) {
        this.priceInDollorBeforeCloudPlanDiscount = priceInDollorBeforeCloudPlanDiscount;
    }

    public String getPriceInRSBeforeCloudPlanDiscount() {
        return priceInRSBeforeCloudPlanDiscount;
    }

    public void setPriceInRSBeforeCloudPlanDiscount(String priceInRSBeforeCloudPlanDiscount) {
        this.priceInRSBeforeCloudPlanDiscount = priceInRSBeforeCloudPlanDiscount;
    }

    public String getLargeImageUrl() {
        return largeImageUrl;
    }

    public void setLargeImageUrl(String largeImageUrl) {
        this.largeImageUrl = largeImageUrl;
    }

    public String getSmallImageUrl() {
        return smallImageUrl;
    }

    public void setSmallImageUrl(String smallImageUrl) {
        this.smallImageUrl = smallImageUrl;
    }


    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOriginalPriceInDollor() {
        return originalPriceInDollor;
    }

    public void setOriginalPriceInDollor(String originalPriceInDollor) {
        this.originalPriceInDollor = originalPriceInDollor;
    }

    public String getPriceInDollor() {
        return priceInDollor;
    }

    public void setPriceInDollor(String priceInDollor) {
        this.priceInDollor = priceInDollor;
    }

    public String getOriginalPriceInRS() {
        return originalPriceInRS;
    }

    public void setOriginalPriceInRS(String originalPriceInRS) {
        this.originalPriceInRS = originalPriceInRS;
    }

    public String getPriceInRS() {
        return priceInRS;
    }

    public void setPriceInRS(String priceInRS) {
        this.priceInRS = priceInRS;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getDetailDesc() {
        return detailDesc;
    }

    public void setDetailDesc(String detailDesc) {
        this.detailDesc = detailDesc;
    }

    public String getDeepLinkUrl() {
        return deepLinkUrl;
    }

    public void setDeepLinkUrl(String deepLinkUrl) {
        this.deepLinkUrl = deepLinkUrl;
    }


    public String getMessageOfCloudPlanText1() {
        return messageOfCloudPlanText1;
    }

    public void setMessageOfCloudPlanText1(String messageOfCloudPlanText1) {
        this.messageOfCloudPlanText1 = messageOfCloudPlanText1;
    }

    public String getMessageOfCloudPlanText2() {
        return messageOfCloudPlanText2;
    }

    public void setMessageOfCloudPlanText2(String messageOfCloudPlanText2) {
        this.messageOfCloudPlanText2 = messageOfCloudPlanText2;
    }

    public String getReportAvailableInLang() {
        return ReportAvailableInLang;
    }

    public void setReportAvailableInLang(String reportAvailableInLang) {
        ReportAvailableInLang = reportAvailableInLang;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getSamplePdfUrl() {
        return samplePdfUrl;
    }

    public void setSamplePdfUrl(String samplePdfUrl) {
        this.samplePdfUrl = samplePdfUrl;
    }

    public String getServiceNameTitle() {
        return serviceNameTitle;
    }

    public void setServiceNameTitle(String serviceNameTitle) {
        this.serviceNameTitle = serviceNameTitle;
    }
}
