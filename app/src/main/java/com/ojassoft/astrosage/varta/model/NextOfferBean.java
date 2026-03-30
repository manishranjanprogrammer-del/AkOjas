package com.ojassoft.astrosage.varta.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class NextOfferBean {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("msg")
    @Expose
    private String msg;
    @SerializedName("serviceid")
    @Expose
    private String serviceid;
    @SerializedName("servicename")
    @Expose
    private String servicename;
    @SerializedName("smalliconfile")
    @Expose
    private String smalliconfile;
    @SerializedName("categoryid")
    @Expose
    private String categoryid;
    @SerializedName("rate")
    @Expose
    private String rate;
    @SerializedName("raters")
    @Expose
    private String raters;
    @SerializedName("actualrate")
    @Expose
    private String actualrate;
    @SerializedName("actualraters")
    @Expose
    private String actualraters;
    @SerializedName("paymentamount")
    @Expose
    private String paymentamount;
    @SerializedName("offermessage")
    @Expose
    private String offermessage;
    @SerializedName("offeramout")
    @Expose
    private String offeramout;
    @SerializedName("offeramountstring")
    @Expose
    private String offeramountstring;
    @SerializedName("static")
    @Expose
    private String _static;
    @SerializedName("promotionalmsg")
    @Expose
    private String promotionalmsg;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getServiceid() {
        return serviceid;
    }

    public void setServiceid(String serviceid) {
        this.serviceid = serviceid;
    }

    public String getServicename() {
        return servicename;
    }

    public void setServicename(String servicename) {
        this.servicename = servicename;
    }

    public String getSmalliconfile() {
        return smalliconfile;
    }

    public void setSmalliconfile(String smalliconfile) {
        this.smalliconfile = smalliconfile;
    }

    public String getCategoryid() {
        return categoryid;
    }

    public void setCategoryid(String categoryid) {
        this.categoryid = categoryid;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public String getRaters() {
        return raters;
    }

    public void setRaters(String raters) {
        this.raters = raters;
    }

    public String getActualrate() {
        return actualrate;
    }

    public void setActualrate(String actualrate) {
        this.actualrate = actualrate;
    }

    public String getActualraters() {
        return actualraters;
    }

    public void setActualraters(String actualraters) {
        this.actualraters = actualraters;
    }

    public String getPaymentamount() {
        return paymentamount;
    }

    public void setPaymentamount(String paymentamount) {
        this.paymentamount = paymentamount;
    }

    public String getOffermessage() {
        return offermessage;
    }

    public void setOffermessage(String offermessage) {
        this.offermessage = offermessage;
    }

    public String getOfferamout() {
        return offeramout;
    }

    public void setOfferamout(String offeramout) {
        this.offeramout = offeramout;
    }

    public String getOfferamountstring() {
        return offeramountstring;
    }

    public void setOfferamountstring(String offeramountstring) {
        this.offeramountstring = offeramountstring;
    }

    public String getStatic() {
        return _static;
    }

    public void setStatic(String _static) {
        this._static = _static;
    }

    public String getPromotionalmsg() {
        return promotionalmsg;
    }

    public void setPromotionalmsg(String promotionalmsg) {
        this.promotionalmsg = promotionalmsg;
    }

}
