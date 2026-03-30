package com.ojassoft.astrosage.varta.model;

import com.ojassoft.astrosage.utils.CGlobal;
import com.ojassoft.astrosage.utils.CGlobalVariables;

import java.io.Serializable;

public class UserProfileData implements Serializable {

    String place = "";
    String day="";
    String month="";
    String year="";
    String hour="";
    String minute="";
    String second="";
    String longew = "";
    String latdeg = "";
    String longdeg = "";
    String latmin = "";
    String longmin = "";
    String latns = "";
    String timezone = "";
    String walletbalance="";
    String name = "";
    String gender="";
    String userPhoneNo="";
    String maritalStatus="Not Specified";
    String occupation="Not Specified";
    boolean isProfileSendToAstrologer = true;
    boolean liveintrooffer;
    String privateintrooffertype;
    String referstatus;
    boolean sfc;
    boolean isBirthDetailsAvailable;
    boolean isnewuser;
    public boolean isBirthDetailsAvailable() {
        return isBirthDetailsAvailable;
    }

    public void setBirthDetailsAvailable(boolean birthDetailsAvailable) {
        isBirthDetailsAvailable = birthDetailsAvailable;
    }

    public String getReferstatus() {
        return referstatus;
    }

    public void setReferstatus(String referstatus) {
        this.referstatus = referstatus;
    }

    public boolean isProfileSendToAstrologer() {
        return isProfileSendToAstrologer;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public void setProfileSendToAstrologer(boolean profileSendToAstrologer) {
        isProfileSendToAstrologer = profileSendToAstrologer;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUserPhoneNo() {
        return userPhoneNo;
    }

    public void setUserPhoneNo(String userPhoneNo) {
        this.userPhoneNo = userPhoneNo;
    }

    public String getWalletbalance() {
        return walletbalance;
    }

    public void setWalletbalance(String walletbalance) {
        this.walletbalance = walletbalance;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public String getMinute() {
        return minute;
    }

    public void setMinute(String minute) {
        this.minute = minute;
    }

    public String getSecond() {
        return second;
    }

    public void setSecond(String second) {
        this.second = second;
    }

    public String getLongew() {
        return longew;
    }

    public void setLongew(String longew) {
        this.longew = longew;
    }

    public String getLatdeg() {
        return latdeg;
    }

    public void setLatdeg(String latdeg) {
        this.latdeg = latdeg;
    }

    public String getLongdeg() {
        return longdeg;
    }

    public void setLongdeg(String longdeg) {
        this.longdeg = longdeg;
    }

    public String getLatmin() {
        return latmin;
    }

    public void setLatmin(String latmin) {
        this.latmin = latmin;
    }

    public String getLongmin() {
        return longmin;
    }

    public void setLongmin(String longmin) {
        this.longmin = longmin;
    }

    public String getLatns() {
        return latns;
    }

    public void setLatns(String latns) {
        this.latns = latns;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public boolean isLiveintrooffer() {
        return liveintrooffer;
    }

    public void setLiveintrooffer(boolean liveintrooffer) {
        this.liveintrooffer = liveintrooffer;
    }

    public String getPrivateintrooffertype() {
        return privateintrooffertype;
    }

    public void setPrivateintrooffertype(String privateintrooffertype) {
        this.privateintrooffertype = privateintrooffertype;
    }

    public boolean isSecondFreeChat() {
        return sfc;
    }

    public void setSecondFreeChat(boolean sfc) {
        this.sfc = sfc;
    }

    public boolean isIsnewuser() {
        return isnewuser;
    }

    public void setIsnewuser(boolean isnewuser) {
        this.isnewuser = isnewuser;
    }
}
