package com.ojassoft.astrosage.beans;


public class OutKpRulingPlanets implements IOutBean {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String _birthDayLord;
    private String _ascSignLord;
    private String _ascNakLord;
    private String _ascSubLord;
    private String _moonSignLord;
    private String _moonNakLord;
    private String _moonSubLord;
    private String _fortunaRasit;
    private double _fortunaDeg = 0;
    private String _fortunaSubSub;
    private double _kpAyan = 0;

    public void setBirthDayLord(String _birthDayLord) {
        this._birthDayLord = _birthDayLord;
    }

    public String getBirthDayLord() {
        return _birthDayLord;
    }

    public void setAscSignLord(String _ascSignLord) {
        this._ascSignLord = _ascSignLord;
    }

    public String getAscSignLord() {
        return _ascSignLord;
    }

    public void setAscNakLord(String _ascNakLord) {
        this._ascNakLord = _ascNakLord;
    }

    public String getAscNakLord() {
        return _ascNakLord;
    }

    public void setAscSubLord(String _ascSubLord) {
        this._ascSubLord = _ascSubLord;
    }

    public String getAscSubLord() {
        return _ascSubLord;
    }

    public void setMoonSignLord(String _moonSignLord) {
        this._moonSignLord = _moonSignLord;
    }

    public String getMoonSignLord() {
        return _moonSignLord;
    }

    public void setMoonNakLord(String _moonNakLord) {
        this._moonNakLord = _moonNakLord;
    }

    public String getMoonNakLord() {
        return _moonNakLord;
    }

    public void setMoonSubLord(String _moonSubLord) {
        this._moonSubLord = _moonSubLord;
    }

    public String getMoonSubLord() {
        return _moonSubLord;
    }

    public void setFortunaDeg(double _fortunaDeg) {
        this._fortunaDeg = _fortunaDeg;
    }

    public double getFortunaDeg() {
        return _fortunaDeg;
    }

    public void setFortunaSubSub(String _fortunaSubSub) {
        this._fortunaSubSub = _fortunaSubSub;
    }

    public String getFortunaSubSub() {
        return _fortunaSubSub;
    }

    public void setKpAyan(double _kpAyan) {
        this._kpAyan = _kpAyan;
    }

    public double getKpAyan() {
        return _kpAyan;
    }

    public void setFortunaRasit(String _fortunaRasit) {
        this._fortunaRasit = _fortunaRasit;
    }

    public String getFortunaRasit() {
        return _fortunaRasit;
    }


}


