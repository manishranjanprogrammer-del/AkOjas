package com.ojassoft.astrosage.varta.model;

import java.io.Serializable;
import java.util.Calendar;

public class BeanDateTime implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private int _mYear = 0, _mMonth = 0, _mDay = 0, _mHour = 0, _mMin = 0, _mSecond = 0;
    private float _fDST = 0.0f;

    public BeanDateTime(boolean setCurrentTime,boolean setCurrentDate) {
        Calendar c = Calendar.getInstance();
        if(setCurrentTime) {
            _mHour = c.get(Calendar.HOUR_OF_DAY);
            _mMin = c.get(Calendar.MINUTE);
            _mSecond = c.get(Calendar.SECOND);
        }
        if(setCurrentDate){
            _mYear = c.get(Calendar.YEAR);
            _mMonth = c.get(Calendar.MONTH);
            _mDay = c.get(Calendar.DAY_OF_MONTH);
        }
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

}
