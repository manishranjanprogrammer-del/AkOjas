package com.ojassoft.astrosage.beans;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ojas-08 on 15/12/16.
 */
public class SyncDataModel implements Serializable {



    @SerializedName("fDST")
    private float fDST = 0.0f;
    @SerializedName("mDay")
    private int mDay = 22;
    @SerializedName("mHour")
    private int mHour = 1;
    @SerializedName("mMin")
    private int mMin = 11;
    @SerializedName("mSecond")
    private int mSecond = 0;
    @SerializedName("mYear")
    private int mYear = 2003;
    @SerializedName("mMonth")
    private int mMonth = 11;
    @SerializedName("fTimeZoneValue")
    private float fTimeZoneValue = 5.5f;
    @SerializedName("horaryNumber")
    private int horaryNumber = 0;
    @SerializedName("iCityId")
    private int iCityId = 1;
    @SerializedName("sDst")
    private int sDst = 0;
    @SerializedName("iAyanIndex")
    private int iAyanIndex = 0;
    @SerializedName("recentId")
    private int recentId = -1;
    @SerializedName("iCountryId")
    private int iCountryId = 91;
    @SerializedName("iTimeZoneId")
    private int iTimeZoneId = 32;
    @SerializedName("name")
    private String name = "";
    @SerializedName("sGender")
    private String sGender = "M";
    @SerializedName("sAyan")
    private String sAyan = "Lahiri";
    @SerializedName("onlineChartId")
    private String onlineChartId = "";
    @SerializedName("sCountryName")
    private String sCountryName = "India";
    @SerializedName("sStateName")
    private String sStateName = "Uttar Pradesh";
    @SerializedName("sCityName")
    private String sCityName = "Agra";
    @SerializedName("sLatDeg")
    private String sLatDeg = "27";
    @SerializedName("sLatMin")
    private String sLatMin = "09";
    @SerializedName("sLongDeg")
    private String sLongDeg = "78";
    @SerializedName("sLongMin")
    private String sLongMin = "00";
    @SerializedName("sLatDir")
    private String sLatDir = "N";
    @SerializedName("sLongDir")
    private String sLongDir = "E";
    @SerializedName("sTimeZoneName")
    private String sTimeZoneName = "+5.30 IST";
    @SerializedName("latSec")
    private String latSec = "00";
    @SerializedName("longSec")
    private String longSec = "00";
    @SerializedName("localChartId")
    private long localChartId = -1;
    @SerializedName("isSaveChart")
    private boolean isSaveChart = false;
    @SerializedName("isDefault")
    private boolean isDefault = false;

    public float getfDST() {
        return fDST;
    }

    public void setfDST(float fDST) {
        this.fDST = fDST;
    }

    public int getmDay() {
        return mDay;
    }

    public void setmDay(int mDay) {
        this.mDay = mDay;
    }

    public int getmHour() {
        return mHour;
    }

    public void setmHour(int mHour) {
        this.mHour = mHour;
    }

    public int getmMin() {
        return mMin;
    }

    public void setmMin(int mMin) {
        this.mMin = mMin;
    }

    public int getmSecond() {
        return mSecond;
    }

    public void setmSecond(int mSecond) {
        this.mSecond = mSecond;
    }

    public int getmYear() {
        return mYear;
    }

    public void setmYear(int mYear) {
        this.mYear = mYear;
    }

    public int getmMonth() {
        return mMonth;
    }

    public void setmMonth(int mMonth) {
        this.mMonth = mMonth;
    }

    public float getfTimeZoneValue() {
        return fTimeZoneValue;
    }

    public void setfTimeZoneValue(float fTimeZoneValue) {
        this.fTimeZoneValue = fTimeZoneValue;
    }

    public int getHoraryNumber() {
        return horaryNumber;
    }

    public void setHoraryNumber(int horaryNumber) {
        this.horaryNumber = horaryNumber;
    }

    public int getiCityId() {
        return iCityId;
    }

    public void setiCityId(int iCityId) {
        this.iCityId = iCityId;
    }

    public int getsDst() {
        return sDst;
    }

    public void setsDst(int sDst) {
        this.sDst = sDst;
    }

    public int getiAyanIndex() {
        return iAyanIndex;
    }

    public void setiAyanIndex(int iAyanIndex) {
        this.iAyanIndex = iAyanIndex;
    }

    public int getRecentId() {
        return recentId;
    }

    public void setRecentId(int recentId) {
        this.recentId = recentId;
    }

    public int getiCountryId() {
        return iCountryId;
    }

    public void setiCountryId(int iCountryId) {
        this.iCountryId = iCountryId;
    }

    public int getiTimeZoneId() {
        return iTimeZoneId;
    }

    public void setiTimeZoneId(int iTimeZoneId) {
        this.iTimeZoneId = iTimeZoneId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getsGender() {
        return sGender;
    }

    public void setsGender(String sGender) {
        this.sGender = sGender;
    }

    public String getsAyan() {
        return sAyan;
    }

    public void setsAyan(String sAyan) {
        this.sAyan = sAyan;
    }

    public String getOnlineChartId() {
        return onlineChartId;
    }

    public void setOnlineChartId(String onlineChartId) {
        this.onlineChartId = onlineChartId;
    }

    public String getsCountryName() {
        return sCountryName;
    }

    public void setsCountryName(String sCountryName) {
        this.sCountryName = sCountryName;
    }

    public String getsStateName() {
        return sStateName;
    }

    public void setsStateName(String sStateName) {
        this.sStateName = sStateName;
    }

    public String getsCityName() {
        return sCityName;
    }

    public void setsCityName(String sCityName) {
        this.sCityName = sCityName;
    }

    public String getsLatDeg() {
        return sLatDeg;
    }

    public void setsLatDeg(String sLatDeg) {
        this.sLatDeg = sLatDeg;
    }

    public String getsLatMin() {
        return sLatMin;
    }

    public void setsLatMin(String sLatMin) {
        this.sLatMin = sLatMin;
    }

    public String getsLongDeg() {
        return sLongDeg;
    }

    public void setsLongDeg(String sLongDeg) {
        this.sLongDeg = sLongDeg;
    }

    public String getsLongMin() {
        return sLongMin;
    }

    public void setsLongMin(String sLongMin) {
        this.sLongMin = sLongMin;
    }

    public String getsLatDir() {
        return sLatDir;
    }

    public void setsLatDir(String sLatDir) {
        this.sLatDir = sLatDir;
    }

    public String getsLongDir() {
        return sLongDir;
    }

    public void setsLongDir(String sLongDir) {
        this.sLongDir = sLongDir;
    }

    public String getsTimeZoneName() {
        return sTimeZoneName;
    }

    public void setsTimeZoneName(String sTimeZoneName) {
        this.sTimeZoneName = sTimeZoneName;
    }

    public String getLatSec() {
        return latSec;
    }

    public void setLatSec(String latSec) {
        this.latSec = latSec;
    }

    public String getLongSec() {
        return longSec;
    }

    public void setLongSec(String longSec) {
        this.longSec = longSec;
    }

    public long getLocalChartId() {
        return localChartId;
    }

    public void setLocalChartId(long localChartId) {
        this.localChartId = localChartId;
    }

    public boolean isSaveChart() {
        return isSaveChart;
    }

    public void setSaveChart(boolean saveChart) {
        isSaveChart = saveChart;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

}
