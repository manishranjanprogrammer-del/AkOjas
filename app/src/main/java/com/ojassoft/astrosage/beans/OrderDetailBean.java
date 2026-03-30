package com.ojassoft.astrosage.beans;

import java.io.Serializable;

public class OrderDetailBean implements Serializable {
    String partnerIdEarning;
    String orderPrice;
    String orderdate;
    String orderName;

    public String getPartnerIdEarning() {
        return partnerIdEarning;
    }

    public void setPartnerIdEarning(String partnerIdEarning) {
        this.partnerIdEarning = partnerIdEarning;
    }

    public String getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        this.orderPrice = orderPrice;
    }

    public String getOrderdate() {
        return orderdate;
    }

    public void setOrderdate(String orderdate) {
        this.orderdate = orderdate;
    }

    public String getOrderName() {
        return orderName;
    }

    public void setOrderName(String orderName) {
        this.orderName = orderName;
    }
}
