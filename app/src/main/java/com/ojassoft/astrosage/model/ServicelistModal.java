
package com.ojassoft.astrosage.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ServicelistModal implements Serializable {

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    private String couponCode = "";

    @SerializedName("IsShowProfileScreen")
    @Expose
    private String isShowProfile = "1";

    @SerializedName("IsShowProblemField")
    @Expose
    private String isShowProblem = "1";

    public String getIsShowProfile() {
        return isShowProfile;
    }

    public void setIsShowProfile(String isShowProfile) {
        this.isShowProfile = isShowProfile;
    }

    public String getIsShowProblem() {
        return isShowProblem;
    }

    public void setIsShowProblem(String isShowProblem) {
        this.isShowProblem = isShowProblem;
    }

    @SerializedName("ServiceId")
    @Expose
    private String serviceId;
    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("PriceInDollor")
    @Expose
    private String priceInDollor;
    @SerializedName("PriceInRS")
    @Expose
    private String priceInRS;
    @SerializedName("DeliveryTime")
    @Expose
    private String deliveryTime;
    @SerializedName("SmallDesc")
    @Expose
    private String smallDesc;
    @SerializedName("DetailDesc")
    @Expose
    private String detailDesc;
    @SerializedName("IsAvailable")
    @Expose
    private String isAvailable;
    @SerializedName("SmallImgURL")
    @Expose
    private String smallImgURL;
    @SerializedName("LargeImgURL")
    @Expose
    private String largeImgURL;
    @SerializedName("UrlText")
    @Expose
    private String serviceDeepLinkURL;

    @SerializedName("AstrologerImageURL")
    @Expose
    private String astrologerImageURL;
     @SerializedName("IsFreeTrialAvailable")
    @Expose
    private boolean isFreeTrialAvailable;

     @SerializedName("FreeTrialPeriod")
    @Expose
    private int freeTrialPeriod;

     @SerializedName("FreeTrialNotifyPeriod")
    @Expose
    private int freeTrialNotifyPeriod;

    @SerializedName("QuestionLimit")
    @Expose
     private String questionLimit;

    @SerializedName("FreeTrialAmount")
    @Expose
     private int freeTrialAmount;
    @SerializedName("PhonePeFreeTrialAmount")
    @Expose
     private String phonePeFreeTrialAmount;

    public int getFreeTrialAmount() {
        return freeTrialAmount;
    }

    public void setFreeTrialAmount(int freeTrialAmount) {
        this.freeTrialAmount = freeTrialAmount;
    }

    public String getPhonePeFreeTrialAmount() {
        return phonePeFreeTrialAmount;
    }

    public void setPhonePeFreeTrialAmount(String phonePeFreeTrialAmount) {
        this.phonePeFreeTrialAmount = phonePeFreeTrialAmount;
    }

    public String getQuestionLimit() {
        return questionLimit;
    }

    public void setQuestionLimit(String questionLimit) {
        this.questionLimit = questionLimit;
    }

    private boolean isSelected;

    @SerializedName("PdfType")
    @Expose
    private int pdfType=-1; // true for premium, false for normal



    public int getPdfType() {
        return pdfType;
    }

    public void setPdfType(int pdfType) {
        this.pdfType = pdfType;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getAstrologerImageURL() {
        return astrologerImageURL;
    }

    public void setAstrologerImageURL(String astrologerImageURL) {
        this.astrologerImageURL = astrologerImageURL;
    }

    public String getP_OriginalPriceInDollar() {
        return P_OriginalPriceInDollar;
    }

    public void setP_OriginalPriceInDollar(String p_OriginalPriceInDollar) {
        P_OriginalPriceInDollar = p_OriginalPriceInDollar;
    }

    public String getP_OriginalPriceInRs() {
        return P_OriginalPriceInRs;
    }

    public void setP_OriginalPriceInRs(String p_OriginalPriceInRs) {
        P_OriginalPriceInRs = p_OriginalPriceInRs;
    }

    public String getP_SaveAmountInDollar() {
        return P_SaveAmountInDollar;
    }

    public void setP_SaveAmountInDollar(String p_SaveAmountInDollar) {
        P_SaveAmountInDollar = p_SaveAmountInDollar;
    }

    public String getP_SavePercentOfDollar() {
        return P_SavePercentOfDollar;
    }

    public void setP_SavePercentOfDollar(String p_SavePercentOfDollar) {
        P_SavePercentOfDollar = p_SavePercentOfDollar;
    }

    public String getP_SaveAmountInRs() {
        return P_SaveAmountInRs;
    }

    public void setP_SaveAmountInRs(String p_SaveAmountInRs) {
        P_SaveAmountInRs = p_SaveAmountInRs;
    }

    public String getP_SavePercentOfRs() {
        return P_SavePercentOfRs;
    }

    public void setP_SavePercentOfRs(String p_SavePercentOfRs) {
        P_SavePercentOfRs = p_SavePercentOfRs;
    }

    @SerializedName("OriginalPriceInDollor")
    @Expose
    private String P_OriginalPriceInDollar;
    @SerializedName("OriginalPriceInRS")
    @Expose
    private String P_OriginalPriceInRs;
    @SerializedName("SaveAmountInDollar")
    @Expose
    private String P_SaveAmountInDollar;
    @SerializedName("SavePercentOfDollar")
    @Expose
    private String P_SavePercentOfDollar;
    @SerializedName("SaveAmountInRs")
    @Expose
    private String P_SaveAmountInRs;
    @SerializedName("SavePercentOfRs")
    @Expose
    private String P_SavePercentOfRs;

    @SerializedName("HtmlContent")
    @Expose
    private String htmlContent = "";
    @SerializedName("HtmlContent1")
    @Expose
    private String HtmlContent1 = "";
    @SerializedName("ReportAvailableInLang")
    @Expose
    private String ReportAvailableInLang = "";

    public String getHtmlContent1() {
        return HtmlContent1;
    }

    public String getReportAvailableInLang() {
        return ReportAvailableInLang;
    }

    public void setReportAvailableInLang(String reportAvailableInLang) {
        ReportAvailableInLang = reportAvailableInLang;
    }

    public void setHtmlContent1(String htmlContent1) {
        HtmlContent1 = htmlContent1;
    }

    public String getHtmlContentImgURL() {
        return htmlContentImgURL;
    }

    public void setHtmlContentImgURL(String htmlContentImgURL) {
        this.htmlContentImgURL = htmlContentImgURL;
    }

    @SerializedName("HtmlContentImgURL")
    @Expose
    private String htmlContentImgURL = "";

    public String getHtmlContentImgURL1() {
        return HtmlContentImgURL1;
    }

    public void setHtmlContentImgURL1(String htmlContentImgURL1) {
        HtmlContentImgURL1 = htmlContentImgURL1;
    }

    @SerializedName("HtmlContentImgURL1")
    @Expose
    private String HtmlContentImgURL1 = "";

    public String getHtmlContent() {
        return htmlContent;
    }

    public void setHtmlContent(String htmlContent) {
        this.htmlContent = htmlContent;
    }

    /**
     * @return The serviceId
     */
    public String getServiceId() {
        return serviceId;
    }

    /**
     * @param serviceId The ServiceId
     */
    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    /**
     * @return The title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title The Title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return The priceInDollor
     */
    public String getPriceInDollor() {
        return priceInDollor;
    }

    /**
     * @param priceInDollor The PriceInDollor
     */
    public void setPriceInDollor(String priceInDollor) {
        this.priceInDollor = priceInDollor;
    }

    /**
     * @return The priceInRS
     */
    public String getPriceInRS() {
        return priceInRS;
    }

    /**
     * @param priceInRS The PriceInRS
     */
    public void setPriceInRS(String priceInRS) {
        this.priceInRS = priceInRS;
    }

    /**
     * @return The deliveryTime
     */
    public String getDeliveryTime() {
        return deliveryTime;
    }

    /**
     * @param deliveryTime The DeliveryTime
     */
    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    /**
     * @return The smallDesc
     */
    public String getSmallDesc() {
        return smallDesc;
    }

    /**
     * @param smallDesc The SmallDesc
     */
    public void setSmallDesc(String smallDesc) {
        this.smallDesc = smallDesc;
    }

    /**
     * @return The detailDesc
     */
    public String getDetailDesc() {
        return detailDesc;
    }

    /**
     * @param detailDesc The DetailDesc
     */
    public void setDetailDesc(String detailDesc) {
        this.detailDesc = detailDesc;
    }

    /**
     * @return The isAvailable
     */
    public String getIsAvailable() {
        return isAvailable;
    }

    /**
     * @param isAvailable The IsAvailable
     */
    public void setIsAvailable(String isAvailable) {
        this.isAvailable = isAvailable;
    }

    /**
     * @return The smallImgURL
     */
    public String getSmallImgURL() {
        return smallImgURL;
    }

    /**
     * @param smallImgURL The SmallImgURL
     */
    public void setSmallImgURL(String smallImgURL) {
        this.smallImgURL = smallImgURL;
    }

    /**
     * @return The largeImgURL
     */
    public String getLargeImgURL() {
        return largeImgURL;
    }

    /**
     * @param largeImgURL The LargeImgURL
     */
    public void setLargeImgURL(String largeImgURL) {
        this.largeImgURL = largeImgURL;
    }

    public String getServiceDeepLinkURL() {
        return serviceDeepLinkURL;
    }

    public void setServiceDeepLinkURL(String serviceDeepLinkURL) {
        this.serviceDeepLinkURL = serviceDeepLinkURL;
    }

    @SerializedName("UserPlanId")
    @Expose
    private String userPlanId = "";

    @SerializedName("UserCloudDiscount")
    @Expose
    private String userCloudDiscount = "";

    @SerializedName("PriceInDollorBeforeCloudPlanDiscount")
    @Expose
    private String priceInDollorBeforeCloudPlanDiscount = "";

    @SerializedName("PriceInRSBeforeCloudPlanDiscount")
    @Expose
    private String priceInRSBeforeCloudPlanDiscount = "";

   /* @SerializedName("PriceInDollorAfterCloudPlanDiscount")
    @Expose
    private String priceInDollorAfterCloudPlanDiscount="";

    @SerializedName("PriceInRSAfterCloudPlanDiscount")
    @Expose
    private String priceInRSAfterCloudPlanDiscount="";*/

    @SerializedName("MessageOfCloudPlan1")
    @Expose
    private String messageOfCloudPlanText1 = "";

    @SerializedName("MessageOfCloudPlan2")
    @Expose
    private String messageOfCloudPlanText2 = "";

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

    public String getUserPlanId() {
        return userPlanId;
    }

    public void setUserPlanId(String userPlanId) {
        this.userPlanId = userPlanId;
    }

    public String getUserCloudDiscount() {
        return userCloudDiscount;
    }

    public void setUserCloudDiscount(String userCloudDiscount) {
        this.userCloudDiscount = userCloudDiscount;
    }

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


    public String getNonCloudPriceInDollor() {
        return nonCloudPriceInDollor;
    }

    public void setNonCloudPriceInDollor(String nonCloudPriceInDollor) {
        this.nonCloudPriceInDollor = nonCloudPriceInDollor;
    }

    public String getNonCloudPriceInRS() {
        return nonCloudPriceInRS;
    }

    public void setNonCloudPriceInRS(String nonCloudPriceInRS) {
        this.nonCloudPriceInRS = nonCloudPriceInRS;
    }


    @SerializedName("PriceInDollorNonCloudPlan")
    @Expose
    private String nonCloudPriceInDollor = "";

    @SerializedName("PriceInRSForNonCloudPlan")
    @Expose
    private String nonCloudPriceInRS = "";

    public boolean isFreeTrialAvailable() {
        return isFreeTrialAvailable;
    }

    public void setFreeTrialAvailable(boolean freeTrialAvailable) {
        isFreeTrialAvailable = freeTrialAvailable;
    }

    public int getFreeTrialPeriod() {
        return freeTrialPeriod;
    }

    public void setFreeTrialPeriod(int freeTrialPeriod) {
        this.freeTrialPeriod = freeTrialPeriod;
    }

    public int getFreeTrialNotifyPeriod() {
        return freeTrialNotifyPeriod;
    }

    public void setFreeTrialNotifyPeriod(int freeTrialNotifyPeriod) {
        this.freeTrialNotifyPeriod = freeTrialNotifyPeriod;
    }
}