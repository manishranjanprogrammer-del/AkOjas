package com.ojassoft.astrosage.varta.model;

import java.io.Serializable;

public class BeanPlace implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
/*    public boolean isDefaultCity;

    public boolean isDefaultCity() {
        return isDefaultCity;
    }

    public void setDefaultCity(boolean defaultCity) {
        isDefaultCity = defaultCity;
    }*/

    private int _iCountryId, _iCityId, _iTimeZoneId;
    private float _fTimeZoneValue;
    private String _sCountryName, _sStateName, _sCityName, _sLatDeg="0", _sLatMin="0", _sLongDeg="0", _sLongMin="0", _sLatDir, _sLongDir, _sTimeZoneName;
    private String _latSec = "00", _longSec = "00";
    private String _lat, _lng, _timeZone, _timeZoneString;
   // private String _lat="", _lng="", _timeZone, _timeZoneString;


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


}

