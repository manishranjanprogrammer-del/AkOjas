package com.ojassoft.astrosage.varta.model;

import java.io.Serializable;
import java.util.ArrayList;

public class WalletAmountBean implements Serializable {
    String gstrate, dollorconvrate;
    ArrayList<Services> ServiceList;

    public String getGstrate() {
        return gstrate;
    }

    public void setGstrate(String gstrate) {
        this.gstrate = gstrate;
    }

    public String getDollorConverstionRate() {
        return dollorconvrate;
    }

    public void setDollorConverstionRate(String dollorconvrate) {
        this.dollorconvrate = dollorconvrate;
    }

    public ArrayList<Services> getServiceList() {
        return ServiceList;
    }

    public void setServiceList(ArrayList<Services> serviceList) {
        ServiceList = serviceList;
    }

    public class Services implements Serializable {
        String serviceid;
        String servicename;
        String smalliconfile;
        String categoryid;
        String rate;
        String raters;
        String actualrate;
        String actualraters;
        String paymentamount;
        String offermessage;
        String offerAmount;
        boolean selected;

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

        public String getOfferAmount() {
            return offerAmount;
        }

        public void setOfferAmount(String offerAmount) {
            this.offerAmount = offerAmount;
        }

        public boolean isSelected() {
            return selected;
        }

        public void setSelected(boolean selected) {
            this.selected = selected;
        }
    }

}
