package com.ojassoft.astrosage.beans;


public class OutPanchang implements IOutBean {
    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private String _strDay = "Sunday";
    private String _strNakshtraAndCharan = "Mriga(2)";

    private String _strRahuKaal = "10:30 a.m to 00:14 p.m";

    private String _hinduWeekDay = "";
    private String _strPaksha = "Sukla";
    private String _strTitAtBirth = "Chaturthi";
    private String _strYoga = "Sukarma";
    private String _strKaran = "Vanij";
    private String _sunRise = "";
    private String _sunSet = "";
    private int _nakshatraIndex = 0;
    private int _nakshatraCharan = 0;


    public void setPaksha(String _strPaksha) {
        this._strPaksha = _strPaksha;
    }

    public String getPaksha() {
        return _strPaksha;
    }

    public void setDay(String _strDay) {
        this._strDay = _strDay;
    }

    public String getDay() {
        return _strDay;
    }

    public void setTitAtBirth(String _strTitAtBirth) {
        this._strTitAtBirth = _strTitAtBirth;
    }

    public String getTitAtBirth() {
        return _strTitAtBirth;
    }

    public void setNakshtraAndCharan(String _strNakshtraAndCharan) {
        this._strNakshtraAndCharan = _strNakshtraAndCharan;
    }

    public String getNakshtraAndCharan() {
        return _strNakshtraAndCharan;
    }

    public void setYoga(String _strYoga) {
        this._strYoga = _strYoga;
    }

    public String getYoga() {
        return _strYoga;
    }

    public void setKaran(String _strKaran) {
        this._strKaran = _strKaran;
    }

    public String getKaran() {
        return _strKaran;
    }

    public void setRahuKaal(String _strRahuKaal) {
        this._strRahuKaal = _strRahuKaal;
    }

    public String getRahuKaal() {
        return _strRahuKaal;
    }

    public void set_HinduWeekDay(String _hinduWeekDay) {
        this._hinduWeekDay = _hinduWeekDay;
    }

    public String get_HinduWeekDay() {
        return _hinduWeekDay;
    }

    public void set_SunRise(String _sunRise) {
        this._sunRise = _sunRise;
    }

    public String get_SunRise() {
        return _sunRise;
    }

    public void set_SunSet(String _sunSet) {
        this._sunSet = _sunSet;
    }

    public String get_SunSet() {
        return _sunSet;
    }

    public void setNakshatraIndex(int _nakshatraIndex) {
        this._nakshatraIndex = _nakshatraIndex;
    }

    public int getNakshatraIndex() {
        return _nakshatraIndex;
    }

    public void setNakshatraCharan(int _nakshatraCharan) {
        this._nakshatraCharan = _nakshatraCharan;
    }

    public int getNakshatraCharan() {
        return _nakshatraCharan;
    }


}
