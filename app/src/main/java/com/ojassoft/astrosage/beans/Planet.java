package com.ojassoft.astrosage.beans;

import java.io.Serializable;

public class Planet implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;


    private double _pDeg;
    private int _pRasi;
    private int _pBhav;
    private int _pPlanetNo;

    public void setPlanetDeg(double _pDeg) {
        this._pDeg = _pDeg;
    }

    public double getPlanetDeg() {
        return _pDeg;
    }

    public void setPlanetRasi(int _pRasi) {
        this._pRasi = _pRasi;
    }

    public int getPlanetRasi() {
        return _pRasi;
    }

    public void setPlanetBhav(int _pBhav) {
        this._pBhav = _pBhav;
    }

    public int getPlanetBhav() {
        return _pBhav;
    }

    public void setPlanetNo(int _pPlanetNo) {
        this._pPlanetNo = _pPlanetNo;
    }

    public int getPlanetNo() {
        return _pPlanetNo;
    }


}
