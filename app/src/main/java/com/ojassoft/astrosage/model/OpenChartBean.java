package com.ojassoft.astrosage.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ojas-02 on 5/4/18.
 */

public class OpenChartBean implements Serializable {

    @SerializedName("chartid")
    @Expose

    private String chartId;


    @SerializedName("chartname")
    @Expose

    private String chartName;
    @SerializedName("gender")
    @Expose
    String gender;
    @SerializedName("dayofbirth")
    @Expose
    String dayOfBirth;
    @SerializedName("monthofbirth")
    @Expose
    String monthOfBirth;


    @SerializedName("yearofbirth")
    @Expose

    private String yearOfBirth;


    @SerializedName("hourofbirth")
    @Expose

    private String hourOfBirth;

    @SerializedName("minuteofbirth")
    @Expose
    String minuteOfBirth;
    @SerializedName("secondofbirth")
    @Expose
    String secondOfBirth;
    @SerializedName("place_of_birth")
    @Expose
    String placeOfBirth;

    @SerializedName("longdeg")
    @Expose

    private String longDeg;


    @SerializedName("longmin")
    @Expose

    private String longMin;
    @SerializedName("longEW")
    @Expose
    String longEW;
    @SerializedName("latdeg")
    @Expose
    String latDeg;
    @SerializedName("latmin")
    @Expose
    String latMin;


    @SerializedName("latNS")
    @Expose

    private String latNS;


    @SerializedName("timezone")
    @Expose

    private String timeZone;

    @SerializedName("ayanamsa")
    @Expose
    String ayanamsa;
    @SerializedName("kphn")
    @Expose
    String kphn;
    @SerializedName("charttype")
    @Expose
    String charttype;

    @SerializedName("DST")
    @Expose
    String DST;



    public String getChartId() {
        return chartId;
    }

    public void setChartId(String chartId) {
        this.chartId = chartId;
    }

    public String getChartName() {
        return chartName;
    }

    public void setChartName(String chartName) {
        this.chartName = chartName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDayOfBirth() {
        return dayOfBirth;
    }

    public void setDayOfBirth(String dayOfBirth) {
        this.dayOfBirth = dayOfBirth;
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

    public String getMinuteOfBirth() {
        return minuteOfBirth;
    }

    public void setMinuteOfBirth(String minuteOfBirth) {
        this.minuteOfBirth = minuteOfBirth;
    }

    public String getSecondOfBirth() {
        return secondOfBirth;
    }

    public void setSecondOfBirth(String secondOfBirth) {
        this.secondOfBirth = secondOfBirth;
    }

    public String getPlaceOfBirth() {
        return placeOfBirth;
    }

    public void setPlaceOfBirth(String placeOfBirth) {
        this.placeOfBirth = placeOfBirth;
    }

    public String getLongDeg() {
        return longDeg;
    }

    public void setLongDeg(String longDeg) {
        this.longDeg = longDeg;
    }

    public String getLongMin() {
        return longMin;
    }

    public void setLongMin(String longMin) {
        this.longMin = longMin;
    }

    public String getLongEW() {
        return longEW;
    }

    public void setLongEW(String longEW) {
        this.longEW = longEW;
    }

    public String getLatDeg() {
        return latDeg;
    }

    public void setLatDeg(String latDeg) {
        this.latDeg = latDeg;
    }

    public String getLatMin() {
        return latMin;
    }

    public void setLatMin(String latMin) {
        this.latMin = latMin;
    }

    public String getLatNS() {
        return latNS;
    }

    public void setLatNS(String latNS) {
        this.latNS = latNS;
    }

    public String getTimeZone() {
        return timeZone;
    }

    public void setTimeZone(String timeZone) {
        this.timeZone = timeZone;
    }

    public String getAyanamsa() {
        return ayanamsa;
    }

    public void setAyanamsa(String ayanamsa) {
        this.ayanamsa = ayanamsa;
    }

    public String getKphn() {
        return kphn;
    }

    public void setKphn(String kphn) {
        this.kphn = kphn;
    }

    public String getCharttype() {
        return charttype;
    }

    public void setCharttype(String charttype) {
        this.charttype = charttype;
    }

    public String getDST() {
        return DST;
    }

    public void setDST(String DST) {
        this.DST = DST;
    }


}
