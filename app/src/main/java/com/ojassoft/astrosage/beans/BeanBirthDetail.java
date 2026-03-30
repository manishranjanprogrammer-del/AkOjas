package com.ojassoft.astrosage.beans;

import java.io.Serializable;

public class BeanBirthDetail implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String _name;
    private String _sex;
    private int _day;
    private int _month;
    private int _year;
    private int _hrs;
    private int _min;
    private int _sec = 0;
    private String _dst = "0";
    private String _longDeg;
    private String _longMin;
    private String _longDir;
    private String _latDeg;

    private String _latMin = "0";
    private String _latDir = "0";
    private float _timeZone;

    private String _strCityName = null;
    private int _ayanType = 0;
    //private long _kundliId=0;
    private String _horaryNumber = "0";
    //private String _chartId="";

    //ADDED BY BIJENDRA (17-NOV-2012)
    private long _locahChartId = -1;
    private String _onlineChartId = "";
    //END


    public BeanBirthDetail() {

    }


    public void setName(String _name) {
        this._name = _name;
    }


    public String getName() {
        return _name;
    }


    public void setSex(String _sex) {
        this._sex = _sex;
    }


    public String getSex() {
        return _sex;
    }


    public void setDay(int _day) {
        this._day = _day;
    }


    public int getDay() {
        return _day;
    }


    public void setMonth(int _month) {
        this._month = _month;
    }


    public int getMonth() {
        return _month;
    }


    public void setYear(int _year) {
        this._year = _year;
    }


    public int getYear() {
        return _year;
    }


    public void setHrs(int _hrs) {
        this._hrs = _hrs;
    }


    public int getHrs() {
        return _hrs;
    }


    public void setMin(int _min) {
        this._min = _min;
    }


    public int getMin() {
        return _min;
    }


    public void setSec(int _sec) {
        this._sec = _sec;
    }


    public int getSec() {
        return _sec;
    }


    public void setDst(String _dst) {
        this._dst = _dst;
    }


    public String getDst() {
        return _dst;
    }


    public void setLongDeg(String _longDeg) {
        this._longDeg = _longDeg;
    }


    public String getLongDeg() {
        return _longDeg;
    }


    public void setLongMin(String _longMin) {
        this._longMin = _longMin;
    }


    public String getLongMin() {
        return _longMin;
    }


    public void setLongDir(String _longDir) {
        this._longDir = _longDir;
    }


    public String getLongDir() {
        return _longDir;
    }


    public void setLatDeg(String _latDeg) {
        this._latDeg = _latDeg;
    }


    public String getLatDeg() {
        return _latDeg;
    }


    public void setLatMin(String _latMin) {
        this._latMin = _latMin;
    }


    public String getLatMin() {
        return _latMin;
    }


    public void setLatDir(String _latDir) {
        this._latDir = _latDir;
    }


    public String getLatDir() {
        return _latDir;
    }


    public void setTimeZone(float _timeZone) {
        this._timeZone = _timeZone;
    }


    public float getTimeZone() {
        return _timeZone;
    }


    public void setCityName(String _strCityName) {
        this._strCityName = _strCityName;
    }


    public String getCityName() {
        return _strCityName;
    }


    public void setAyanType(int _ayanType) {
        this._ayanType = _ayanType;
    }


    public int getAyanType() {
        return _ayanType;
    }



	/*public void set_kundliId(long _kundliId) {
        this._kundliId = _kundliId;
	}



	public long get_kundliId() {
		return _kundliId;
	}
*/


    public void setHoraryNumber(String _horaryNumber) {
        this._horaryNumber = _horaryNumber;
    }


    public String getHoraryNumber() {
        return _horaryNumber;
    }



	/*public void set_ChartId(String _chartId) {
		this._chartId = _chartId;
	}



	public String get_ChartId() {
		return _chartId;
	}*/

    //ADDED BY BIJENDRA (17-NOV-2012)
    public long getLocalChartId() {
        return _locahChartId;
    }

    public void setLocalChartId(long _locahChartId) {
        this._locahChartId = _locahChartId;
    }

    public String getOnlineChartId() {
        return _onlineChartId;
    }

    public void setOnlineChartId(String _onlineChartId) {
        this._onlineChartId = _onlineChartId;
    }
    //END
}


