package com.ojassoft.astrosage.beans;

import java.io.Serializable;

public class MyEarningBean implements Serializable {

    /*String affiliateEarnings;
    String totalOfOrders;
    String totalOrders;
    String orderOfMonth;
    String orderOfYear;*/
    String affiliateEarnings;
    String orderOfMonthStr;
    String totalOfOrders;
    String orderOfMonth;
    String totalOrders;
    String orderOfYear;


    public String getAffiliateEarnings() {
        return affiliateEarnings;
    }

    public void setAffiliateEarnings(String affiliateEarnings) {
        this.affiliateEarnings = affiliateEarnings;
    }

    public String getTotalOfOrders() {
        return totalOfOrders;
    }

    public void setTotalOfOrders(String totalOfOrders) {
        this.totalOfOrders = totalOfOrders;
    }

    public String getTotalOrders() {
        return totalOrders;
    }

    public void setTotalOrders(String totalOrders) {
        this.totalOrders = totalOrders;
    }

    public String getOrderOfMonth() {
        return orderOfMonth;
    }

    public void setOrderOfMonth(String orderOfMonth) {
        this.orderOfMonth = orderOfMonth;
    }

    public String getOrderOfYear() {
        return orderOfYear;
    }

    public void setOrderOfYear(String orderOfYear) {
        this.orderOfYear = orderOfYear;
    }

    public String getOrderOfMonthStr() {
        return orderOfMonthStr;
    }

    public void setOrderOfMonthStr(String orderOfMonthStr) {
        this.orderOfMonthStr = orderOfMonthStr;
    }
}
