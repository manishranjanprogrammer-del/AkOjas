package com.ojassoft.astrosage.varta.model;

import java.io.Serializable;

public class GiftModel implements Serializable {

    private String serviceid;
    private String servicename;
    private String smalliconfile;
    private String categoryid;
    private String rate;
    private String raters;
    private String actualrate;
    private String actualraters;
    private String paymentamount;
    private String offermessage;
    private String offeramout;
    private boolean isSelected;
    private String iconcode;

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

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getIconcode() {
        return iconcode;
    }

    public void setIconcode(String iconcode) {
        this.iconcode = iconcode;
    }
}
