package com.ojassoft.astrosage.beans;


public class OutBirthDetail implements IOutBean {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private String _name = null;
    private String _dateOfBirth = null;
    private String _timeOfBirth = null;
    private String _place = null;
    private String _gender = null;
    private String _ayanType = null;
    private String _dst = null;
    private int _mangldoshPoint = 0;
    private int rashi = 0;

    public void setName(String _name) {
        this._name = _name;
    }

    public String getName() {
        return _name;
    }

    public void setDateOfBirth(String _dateOfBirth) {
        this._dateOfBirth = _dateOfBirth;
    }

    public String getDateOfBirth() {
        return _dateOfBirth;
    }

    public void setTimeOfBirth(String _timeOfBirth) {
        this._timeOfBirth = _timeOfBirth;
    }

    public String getTimeOfBirth() {
        return _timeOfBirth;
    }

    public void setPlace(String _place) {
        this._place = _place;
    }

    public String getPlace() {
        return _place;
    }

    public void setGender(String _gender) {
        this._gender = _gender;
    }

    public String getGender() {
        return _gender;
    }

    public void setAyanType(String _ayanType) {
        this._ayanType = _ayanType;
    }

    public String getAyanType() {
        return _ayanType;
    }

    public void setDst(String _dst) {
        this._dst = _dst;
    }

    public String getDst() {
        return _dst;
    }

    public void setMangldoshPoint(int mangldoshPoint) {
        this._mangldoshPoint = mangldoshPoint;
    }

    public int getMangldoshPoint() {
        return _mangldoshPoint;
    }

    /**
     * @param rashi the rashi to set
     */
    public void setRashi(int rashi) {
        this.rashi = rashi;
    }

    /**
     * @return the rashi
     */
    public int getRashi() {
        return rashi;
    }

}
