package com.ojassoft.astrosage.beans;

import com.google.gson.annotations.SerializedName;

import org.shadow.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

public class BeanPlace implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @SerializedName("a")
    private int _iCountryId = 91;
    @SerializedName("b")
    private int _iCityId = 1423;
    @SerializedName("c")
    private int _iTimeZoneId = 32;
    @SerializedName("d")
    private float _fTimeZoneValue = 5.5f;

    @SerializedName("e")
    private String _sCountryName = "India";
    @SerializedName("f")
    private String _sStateName = "Uttar Pradesh";
    @SerializedName("g")
    private String _sCityName = "Agra";
    @SerializedName("h")
    private String _sLatDeg = "27";
    @SerializedName("i")
    private String _sLatMin = "09";
    @SerializedName("j")
    private String _sLongDeg = "78";
    @SerializedName("k")
    private String _sLongMin = "00";
    @SerializedName("l")
    private String _sLatDir = "N";
    @SerializedName("m")
    private String _sLongDir = "E";
    @SerializedName("n")
    private String _sTimeZoneName = "+5.30 IST";

    @SerializedName("o")
    private String _latSec = "00";
    @SerializedName("p")
    private String _longSec = "00";

    @SerializedName("q")
    private String _lat = "";
    @SerializedName("r")
    private String _lng = "";
    @SerializedName("s")
    private String _timeZone = "";
    @SerializedName("t")
    private String _timeZoneString = "";

    public void setState(String _sStateName) {
        this._sStateName = _sStateName;
    }

    public void setTimeZoneString(String _timeZoneString) {
        this._timeZoneString = _timeZoneString;
    }

    public void setLatitude(String _lat) {
        this._lat = _lat;
    }

    public void setLongitude(String _lng) {
        this._lng = _lng;
    }

    public void setTimeZone(String _timeZone) {
        this._timeZone = _timeZone;
    }

    public String getState() {
        return _sStateName;
    }

    public String getTimeZoneString() {
        return _timeZoneString;
    }

    public String getLatitude() {
        return _lat;
    }

    public String getLongitude() {
        return _lng;
    }

    public String getTimeZone() {
        return _timeZone;
    }

    public int getCountryId() {
        return this._iCountryId;
    }

    public int getTimeZoneId() {
        return this._iTimeZoneId;
    }

    public void setTimeZoneValue(float fTimeZoneValue) {
        this._fTimeZoneValue = fTimeZoneValue;
    }

    public float getTimeZoneValue() {
        return this._fTimeZoneValue;
    }

    public void setTimeZoneId(int iTimeZoneId) {
        this._iTimeZoneId = iTimeZoneId;
    }

    public void setCountryId(int iCountryId) {
        this._iCountryId = iCountryId;
    }

    public int getCityId() {
        return this._iCityId;
    }

    public void setCityId(int iCityId) {
        this._iCityId = iCityId;
    }

    public void setCountryName(String sCountryName) {
        this._sCountryName = sCountryName;
    }

    public String getCountryName() {
        return this._sCountryName;
    }

    public String getCityName() {
        return this._sCityName;
    }

    public void setCityName(String sCityName) {
        this._sCityName = sCityName;
    }


    public String getLatDeg() {
        return this._sLatDeg;
    }

    public void setLatDeg(String sLatDeg) {
        this._sLatDeg = sLatDeg;
    }

    public String getLatMin() {
        return this._sLatMin;
    }

    public void setLatMin(String sLatMin) {
        this._sLatMin = sLatMin;
    }

    public String getLongDeg() {
        return this._sLongDeg;
    }

    public void setLongDeg(String sLongDeg) {
        this._sLongDeg = sLongDeg;
    }

    public String getLongMin() {
        return this._sLongMin;
    }

    public void setLongMin(String sLongMin) {
        this._sLongMin = sLongMin;
    }

    public String getLatDir() {
        return this._sLatDir;
    }

    public void setLatDir(String sLatDir) {
        this._sLatDir = sLatDir;
    }


    public String getLongDir() {
        return this._sLongDir;
    }

    public void setLongDir(String sLongDir) {
        this._sLongDir = sLongDir;
    }


    public String getTimeZoneName() {
        return this._sTimeZoneName;
    }

    public void setTimeZoneName(String sTimeZoneName) {
        this._sTimeZoneName = sTimeZoneName;
    }

    public void setLatSec(String _latSec) {
        this._latSec = _latSec;
    }

    public String getLatSec() {
        return _latSec;
    }

    public void setLongSec(String _longSec) {
        this._longSec = _longSec;
    }

    public String getLongSec() {
        return _longSec;
    }


    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}

