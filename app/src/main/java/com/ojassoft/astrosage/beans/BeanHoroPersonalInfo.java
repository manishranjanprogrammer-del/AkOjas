package com.ojassoft.astrosage.beans;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class BeanHoroPersonalInfo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    @SerializedName("a")
    private String _name = "";
    @SerializedName("b")
    private String _sGender = "M";
    @SerializedName("c")
    private String _sAyan = "Lahiri";
    @SerializedName("d")
    private BeanDateTime _cDateTime = new BeanDateTime();
    @SerializedName("e")
    private BeanPlace _cPlace = new BeanPlace();
    @SerializedName("f")
    private int _iCityId = 1;
    @SerializedName("g")
    private int _sDst = 0;
    @SerializedName("h")
    private int _iAyanIndex = 0;
    private float _fDST;//not in proguard json
    @SerializedName("o")
    private int _horaryNumber = 0;
    @SerializedName("p")
    private long _locahChartId = -1;
    @SerializedName("q")
    private String _onlineChartId = "";
    @SerializedName("r")
    private boolean _isSaveChart = false;
    @SerializedName("s")
    private int recentId = -1;
    @SerializedName("t")
    private String _occupation = "";
    @SerializedName("u")
    private String maritalStatus = "";


    public BeanHoroPersonalInfo() {
        /*Calendar c = Calendar.getInstance();
        _name=String.valueOf( c.get(Calendar.YEAR));
		_name +=String.valueOf(c.get(Calendar.MONTH));
		_name +=String.valueOf(c.get(Calendar.DAY_OF_MONTH));
		_name +=String.valueOf(c.get(Calendar.HOUR_OF_DAY));
		_name +=String.valueOf(c.get(Calendar.MINUTE));*/
    }

    public void setName(String sName) {
        this._name = sName;
    }

    public String getName() {
        return this._name;
    }

    public void setDateTime(BeanDateTime cDateTime) {
        this._cDateTime = cDateTime;
    }

    public String getGender() {
        return this._sGender;
    }

    public void setGender(String sGender) {
        this._sGender = sGender;
    }

    public BeanDateTime getDateTime() {
        return this._cDateTime;
    }


    public void setPlace(BeanPlace cPlace) {
        this._cPlace = cPlace;
    }

    public BeanPlace getPlace() {
        return this._cPlace;
    }

    public String getAyan() {
        return this._sAyan;
    }

    public void setAyan(String sAyan) {
        this._sAyan = sAyan;
    }

    public void setCityID(int iCityId) {
        this._iCityId = iCityId;
    }

    public int getCityID() {
        return this._iCityId;
    }

    public void setDST(int sDst) {
        this._sDst = sDst;
    }

    public int getDST() {
        return this._sDst;
    }

    public void setAyanIndex(int iAyanIndex) {
        this._iAyanIndex = iAyanIndex;
    }

    public int getAyanIndex() {
        return this._iAyanIndex;
    }

    public void setFloatDST(float _fDST) {
        this._fDST = _fDST;
    }

    public float getFloatDST() {
        return _fDST;
    }

    public void setHoraryNumber(int _horaryNumber) {
        this._horaryNumber = _horaryNumber;
    }

    public int getHoraryNumber() {
        return _horaryNumber;
    }

    //ADDED BY BIJENDRA (17-NOV-2012)
    public long getLocalChartId() {
        return this._locahChartId;
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
    public boolean isSaveChart() {
        return _isSaveChart;
    }

    public void setSaveChart(boolean _isSaveChart) {
        this._isSaveChart = _isSaveChart;
    }

    @Override
    public String toString() {
        return _name.split(" ")[0]+_cDateTime.getDateOfBirth();
    }

    public int getRecentId() {
        return recentId;
    }

    public void setRecentId(int recentId) {
        this.recentId = recentId;
    }

    public String get_occupation() {
        return _occupation;
    }

    public void set_occupation(String _occupation) {
        this._occupation = _occupation;
    }

    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

}
