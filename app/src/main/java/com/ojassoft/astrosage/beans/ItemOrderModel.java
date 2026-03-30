package com.ojassoft.astrosage.beans;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ojas on ३०/३/१६.
 */
public class ItemOrderModel implements Serializable
{
    /*
    * key,name,emailid,address,city,landmark,state,country,paymode,prId,pincode
mobileno,prCatId,shippingcost,productcost,makeitdefault
    * */

    private String name;
    private String emailid;
    private String address;
    private String city;
    private String landmark;
    private String state;
    private String country;
    private String paymode;
    private String prId;
    private String pincode;
    private String mobileno;
    private String prCatId;
    private String pr_rs_Shippingmsg;
    private String pr_dl_Shippingmsg_rs;

    public String getPr_rs_Shippingmsg() {
        return pr_rs_Shippingmsg;
    }

    public void setPr_rs_Shippingmsg(String pr_rs_Shippingmsg) {
        this.pr_rs_Shippingmsg = pr_rs_Shippingmsg;
    }

    public String getPr_dl_Shippingmsg_rs() {
        return pr_dl_Shippingmsg_rs;
    }

    public void setPr_dl_Shippingmsg_rs(String pr_dl_Shippingmsg_rs) {
        this.pr_dl_Shippingmsg_rs = pr_dl_Shippingmsg_rs;
    }

    public ArrayList<AstroShopItemDetails> getCartList() {
        return cartList;
    }

    public void setCartList(ArrayList<AstroShopItemDetails> cartList) {
        this.cartList = cartList;
    }

    private String shippingcost;
    private String productcost;

    private ArrayList<AstroShopItemDetails> cartList;

    public String getProductcost_inrs() {
        return productcost_inrs;
    }

    public void setProductcost_inrs(String productcost_inrs) {
        this.productcost_inrs = productcost_inrs;
    }

    private String productcost_inrs;

    private String makeitdefault;

    public String getShippingcost_in_rs() {
        return shippingcost_in_rs;
    }

    public void setShippingcost_in_rs(String shippingcost_in_rs) {
        this.shippingcost_in_rs = shippingcost_in_rs;
    }

    private String shippingcost_in_rs;


    public String getLandmark() {
        return landmark;
    }

    public void setLandmark(String landmark) {
        this.landmark = landmark;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmailid() {
        return emailid;
    }

    public void setEmailid(String emailid) {
        this.emailid = emailid;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPaymode() {
        return paymode;
    }

    public void setPaymode(String paymode) {
        this.paymode = paymode;
    }

    public String getPrId() {
        return prId;
    }

    public void setPrId(String prId) {
        this.prId = prId;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getPrCatId() {
        return prCatId;
    }

    public void setPrCatId(String prCatId) {
        this.prCatId = prCatId;
    }

    public String getShippingcost() {
        return shippingcost;
    }

    public void setShippingcost(String shippingcost) {
        this.shippingcost = shippingcost;
    }

    public String getProductcost() {
        return productcost;
    }

    public void setProductcost(String productcost) {
        this.productcost = productcost;
    }

    public String getMakeitdefault() {
        return makeitdefault;
    }

    public void setMakeitdefault(String makeitdefault) {
        this.makeitdefault = makeitdefault;
    }




}
