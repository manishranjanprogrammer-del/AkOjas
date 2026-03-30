package com.ojassoft.astrosage.varta.model;

import java.io.Serializable;


public class BeanHoroPersonalInfo implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String _name = "", _sGender = "M", _sAyan = "Lahiri";
    private BeanDateTime _cDateTime = new BeanDateTime(false,false);
    private BeanPlace _cPlace = new BeanPlace();
    private int _iCityId = 1;
    private int _sDst = 0;
    private int _iAyanIndex = 0;
    private float _fDST;
    private int _horaryNumber = 0;
    //ADDED BY BIJENDRA (17-NOV-2012)
    private long _locahChartId = -1;
    private String _onlineChartId = "";
    //END
    private boolean _isSaveChart = false;
    private int recentId = -1;


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
        return _name;
    }
    public int getRecentId() {
        return recentId;
    }

    public void setRecentId(int recentId) {
        this.recentId = recentId;
    }

}
