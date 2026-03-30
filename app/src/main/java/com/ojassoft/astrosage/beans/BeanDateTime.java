package com.ojassoft.astrosage.beans;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.Calendar;

public class BeanDateTime implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @SerializedName("a")
    private int _mYear = 2003;
    @SerializedName("b")
    private int _mMonth = 11;
    @SerializedName("c")
    private int _mDay = 22;
    @SerializedName("d")
    private int _mHour = 1;
    @SerializedName("e")
    private int _mMin = 11;
    @SerializedName("f")
    private int _mSecond = 0;
    @SerializedName("g")
    private float _fDST = 0.0f;

    public BeanDateTime() {
        Calendar c = Calendar.getInstance();
        _mYear = c.get(Calendar.YEAR);
        _mMonth = c.get(Calendar.MONTH);
        _mDay = c.get(Calendar.DAY_OF_MONTH);
        _mHour = c.get(Calendar.HOUR_OF_DAY);
        _mMin = c.get(Calendar.MINUTE);
        _mSecond = c.get(Calendar.SECOND);
    }

    public float getDST() {
        return this._fDST;
    }

    public void setDST(float fDST) {
        this._fDST = fDST;
    }

    public int getYear() {
        return this._mYear;
    }

    public void setYear(int iYear) {
        this._mYear = iYear;
    }

    public int getMonth() {
        return this._mMonth;
    }

    public void setMonth(int iMonth) {
        this._mMonth = iMonth;
    }

    public int getDay() {
        return this._mDay;
    }

    public void setDay(int iDay) {
        this._mDay = iDay;
    }

    public int getHour() {
        return this._mHour;
    }

    public void setHour(int iHour) {
        this._mHour = iHour;
    }

    public int getMin() {
        return this._mMin;
    }

    public void setMin(int iMin) {
        this._mMin = iMin;
    }

    public void setSecond(int _mSecond) {
        this._mSecond = _mSecond;
    }

    public int getSecond() {
        return _mSecond;
    }

    public String getDateOfBirth(){
        return ""+_mDay+_mMonth+_mYear;
    }

    public String getTimeOfBirth(){
        return _mHour+":"+_mMin+":"+_mSecond;
    }
    public String getFormattedDateOfBirth(){
        return ""+_mDay+"-"+(_mMonth+ 1)+"-"+_mYear;
    }
}
