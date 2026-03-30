package com.ojassoft.astrosage.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ojas on १५/६/१६.
 */
public class AstrologerServiceInfo implements Serializable {
    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }


    private String couponCode="";

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }

    @SerializedName("key")
    private String key;

    @SerializedName("dst")
    private String dst;

    @SerializedName("regName")
    private String regName;

    public String getDst() {
        return dst;
    }

    public void setDst(String dst) {
        this.dst = dst;
    }

    @SerializedName("emailID")
    private String emailID;

    @SerializedName("gender")
    private String gender;

    @SerializedName("dateOfBirth")
    private String dateOfBirth;

    @SerializedName("monthOfBirth")
    private String monthOfBirth;

    @SerializedName("yearOfBirth")
    private String yearOfBirth;

    @SerializedName("billingAddress")

    private String billingAddress;

    @SerializedName("kphn")
    private String kphn;

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }

    @SerializedName("pincode")
    private String pincode;

    @SerializedName("billingCity")
    private String billingCity;

    public String getBillingAddress() {
        return billingAddress;
    }

    public void setBillingAddress(String billingAddress) {
        this.billingAddress = billingAddress;
    }

    @SerializedName("billingState")
    private String billingState;

    public String getBillingCity() {
        return billingCity;
    }

    public void setBillingCity(String billingCity) {
        this.billingCity = billingCity;
    }

    public String getBillingState() {
        return billingState;
    }

    public void setBillingState(String billingState) {
        this.billingState = billingState;
    }

    public String getBillingCountry() {
        return billingCountry;
    }

    public void setBillingCountry(String billingCountry) {
        this.billingCountry = billingCountry;
    }
    @SerializedName("billingCountry")
    private String billingCountry;

    public String getRegName() {
        return regName;
    }

    public void setRegName(String regName) {
        this.regName = regName;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getMonthOfBirth() {
        return monthOfBirth;
    }

    public void setMonthOfBirth(String monthOfBirth) {
        this.monthOfBirth = monthOfBirth;
    }

    public String getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(String yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public String getHourOfBirth() {
        return hourOfBirth;
    }

    public void setHourOfBirth(String hourOfBirth) {
        this.hourOfBirth = hourOfBirth;
    }

    public String getMinOfBirth() {
        return minOfBirth;
    }

    public void setMinOfBirth(String minOfBirth) {
        this.minOfBirth = minOfBirth;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getNearCity() {
        return nearCity;
    }

    public void setNearCity(String nearCity) {
        this.nearCity = nearCity;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProblem() {
        return problem;
    }

    public void setProblem(String problem) {
        this.problem = problem;
    }

    public String getServiceId() {
        return serviceId;
    }

    public void setServiceId(String serviceId) {
        this.serviceId = serviceId;
    }

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPriceRs() {
        return priceRs;
    }

    public void setPriceRs(String priceRs) {
        this.priceRs = priceRs;
    }

    public String getPayMode() {
        return payMode;
    }

    public void setPayMode(String payMode) {
        this.payMode = payMode;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getKnowTOB() {
        return knowTOB;
    }

    public void setKnowTOB(String knowTOB) {
        this.knowTOB = knowTOB;
    }

    public String getKnowDOB() {
        return knowDOB;
    }

    public void setKnowDOB(String knowDOB) {
        this.knowDOB = knowDOB;
    }

    public String getCcAvenueType() {
        return ccAvenueType;
    }

    public void setCcAvenueType(String ccAvenueType) {
        this.ccAvenueType = ccAvenueType;
    }

    public String getSecOfBirth() {
        return secOfBirth;
    }

    public void setSecOfBirth(String secOfBirth) {
        this.secOfBirth = secOfBirth;
    }

    @SerializedName("secOfBirth")
    private String secOfBirth;

    @SerializedName("minOfBirth")
    private String minOfBirth;

    @SerializedName("hourOfBirth")
    private String hourOfBirth;

    @SerializedName("place")
    private String place;

    @SerializedName("nearCity")
    private String nearCity;

    @SerializedName("state")
    private String state;

    @SerializedName("country")
    private String country;

    @SerializedName("problem")
    private String problem;

    @SerializedName("serviceId")
    private String serviceId;

    @SerializedName("profileId")
    private String profileId;

    @SerializedName("price")
    private String price;

    @SerializedName("priceRs")
    private String priceRs;

    @SerializedName("payMode")
    private String payMode;

    @SerializedName("mobileNo")
    private String mobileNo;

    @SerializedName("knowTOB")
    private String knowTOB;

    @SerializedName("knowDOB")
    private String knowDOB;

    @SerializedName("ccAvenueType")
    private String ccAvenueType;


    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getTimezone() {
        return Timezone;
    }

    public void setTimezone(String timezone) {
        Timezone = timezone;
    }

    @SerializedName("Longitude")
    private String Longitude;
    @SerializedName("Latitude")
    private String Latitude;
    @SerializedName("Timezone")
    private String Timezone;
    public String getKphn() {
        return kphn;
    }

    public void setKphn(String kphn) {
        this.kphn = kphn;
    }


    public String getLongDegOfBirth() {
        return LongDegOfBirth;
    }

    public void setLongDegOfBirth(String longDegOfBirth) {
        LongDegOfBirth = longDegOfBirth;
    }

    public String getLongMinOfBirth() {
        return LongMinOfBirth;
    }

    public void setLongMinOfBirth(String longMinOfBirth) {
        LongMinOfBirth = longMinOfBirth;
    }

    public String getLongEWOfBirth() {
        return LongEWOfBirth;
    }

    public void setLongEWOfBirth(String longEWOfBirth) {
        LongEWOfBirth = longEWOfBirth;
    }

    public String getLatDegOfBirth() {
        return LatDegOfBirth;
    }

    public void setLatDegOfBirth(String latDegOfBirth) {
        LatDegOfBirth = latDegOfBirth;
    }

    public String getLatMinOfBirth() {
        return LatMinOfBirth;
    }

    public void setLatMinOfBirth(String latMinOfBirth) {
        LatMinOfBirth = latMinOfBirth;
    }

    public String getLatNSOfBirth() {
        return LatNSOfBirth;
    }

    public void setLatNSOfBirth(String latNSOfBirth) {
        LatNSOfBirth = latNSOfBirth;
    }

    @SerializedName("LongDegOfBirth")
    private String LongDegOfBirth;
    @SerializedName("LongMinOfBirth")
    private String LongMinOfBirth;
    @SerializedName("LongEWOfBirth")
    private String LongEWOfBirth;


    @SerializedName("LatDegOfBirth")
    private String LatDegOfBirth;
    @SerializedName("LatMinOfBirth")
    private String LatMinOfBirth;
    @SerializedName("LatNSOfBirth")
    private String LatNSOfBirth;

    public String getPrtnr_id() {
        return prtnr_id;
    }

    public void setPrtnr_id(String prtnr_id) {
        this.prtnr_id = prtnr_id;
    }

    @SerializedName("prtnr_id")
    private String prtnr_id;


}
