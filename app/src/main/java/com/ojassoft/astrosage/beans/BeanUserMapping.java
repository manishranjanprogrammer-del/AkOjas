package com.ojassoft.astrosage.beans;

/**
 * Created by ojas on 16/8/17.
 * This class is used as a getter and setter of User Mapping
 */

public class BeanUserMapping {

    private int isAstrologer=-1;// 1= astrologer, 0= non astrologer
    private String birthDetails ="";
    private String name="";
    private String city="";
    private String state="";
    private String country="";

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

    private String phoneNo="";
    private String userName="";
    private String deviceId="";
    private String about="";
    private int status = 0;//0= not updated // 1= ready to send to server // 2= data sent to server
    private String cityLat ="0";
    private String cityLong ="0";
    private String astrocampId="";

    public String getAstrocampId() {
        return astrocampId;
    }

    public void setAstrocampId(String astrocampId) {
        this.astrocampId = astrocampId;
    }

    public String getCityLat() {
        return cityLat;
    }

    public void setCityLat(String cityLat) {
        this.cityLat = cityLat;
    }

    public String getCityLong() {
        return cityLong;
    }

    public void setCityLong(String cityLong) {
        this.cityLong = cityLong;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getIsAstrologer() {
        return isAstrologer;
    }

    public void setIsAstrologer(int isAstrologer) {
        this.isAstrologer = isAstrologer;
    }

    public String getBirthDetails() {
        return birthDetails;
    }

    public void setBirthDetails(String birthDetails) {
        this.birthDetails = birthDetails;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public void setPhoneNo(String phoneNo) {
        this.phoneNo = phoneNo;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }
}
