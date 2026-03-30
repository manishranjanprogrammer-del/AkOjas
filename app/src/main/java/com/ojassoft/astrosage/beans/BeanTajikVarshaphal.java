package com.ojassoft.astrosage.beans;

import java.io.Serializable;

public class BeanTajikVarshaphal implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private PlanetData _planetData;
    private String _muntha = "";
    private String _yearNumber = "";

    public void setPlanetData(PlanetData planetData) {
        this._planetData = planetData;
    }

    public PlanetData getPlanetData() {
        return _planetData;
    }

    public void setMuntha(String _muntha) {
        this._muntha = _muntha;
    }

    public String getMuntha() {
        return _muntha;
    }

    public void setYearNumber(String _yearNumber) {
        this._yearNumber = _yearNumber;
    }

    public String getYearNumber() {
        return _yearNumber;
    }

}
