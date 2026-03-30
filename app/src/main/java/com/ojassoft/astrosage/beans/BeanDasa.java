package com.ojassoft.astrosage.beans;

import java.io.Serializable;

public class BeanDasa implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;


    private int _iPlanetNo;
    private int _iDay;
    private int _iMonth;
    private int _iYear;
    private double _fDasaTime;


    public BeanDasa() {
    }

    public BeanDasa(int iPlanetNo, int iYear, int iMonth, int iDay) {
        this._iPlanetNo = iPlanetNo;
        this._iDay = iDay;
        this._iMonth = iMonth;
        this._iYear = iYear;


    }

    public int getDay() {
        return _iDay;
    }

    public int getMonth() {
        return _iMonth;
    }

    public int getYear() {
        return _iYear;
    }

    public void setDay(int iDay) {
        this._iDay = iDay;
    }

    public void setMonth(int iMonth) {
        this._iMonth = iMonth;
    }

    public void setYear(int iYear) {
        this._iYear = iYear;
    }

    public int getPlanetNo() {
        return _iPlanetNo;
    }

    public void setPlanetNo(int iPlanetNo) {
        this._iPlanetNo = iPlanetNo;
    }

    public void setDasaTime(double _fDasaTime) {
        this._fDasaTime = _fDasaTime;
    }

    public double getDasaTime() {
        return _fDasaTime;
    }
}
