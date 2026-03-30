package com.ojassoft.astrosage.beans;

import java.io.Serializable;
import java.util.ArrayList;

public class KundliPlanetArray implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    //FOR NORTH KUNDLI
    public int LagnaRasi = 0;
    public ArrayList<Planet> Bhav1 = new ArrayList<Planet>();
    public ArrayList<Planet> Bhav2 = new ArrayList<Planet>();
    public ArrayList<Planet> Bhav3 = new ArrayList<Planet>();
    public ArrayList<Planet> Bhav4 = new ArrayList<Planet>();
    public ArrayList<Planet> Bhav5 = new ArrayList<Planet>();
    public ArrayList<Planet> Bhav6 = new ArrayList<Planet>();
    public ArrayList<Planet> Bhav7 = new ArrayList<Planet>();
    public ArrayList<Planet> Bhav8 = new ArrayList<Planet>();
    public ArrayList<Planet> Bhav9 = new ArrayList<Planet>();
    public ArrayList<Planet> Bhav10 = new ArrayList<Planet>();
    public ArrayList<Planet> Bhav11 = new ArrayList<Planet>();
    public ArrayList<Planet> Bhav12 = new ArrayList<Planet>();

    // FOR SOUTH KUNDLI
    public ArrayList<Planet> Rasi1 = new ArrayList<Planet>();
    public ArrayList<Planet> Rasi2 = new ArrayList<Planet>();
    public ArrayList<Planet> Rasi3 = new ArrayList<Planet>();
    public ArrayList<Planet> Rasi4 = new ArrayList<Planet>();
    public ArrayList<Planet> Rasi5 = new ArrayList<Planet>();
    public ArrayList<Planet> Rasi6 = new ArrayList<Planet>();
    public ArrayList<Planet> Rasi7 = new ArrayList<Planet>();
    public ArrayList<Planet> Rasi8 = new ArrayList<Planet>();
    public ArrayList<Planet> Rasi9 = new ArrayList<Planet>();
    public ArrayList<Planet> Rasi10 = new ArrayList<Planet>();
    public ArrayList<Planet> Rasi11 = new ArrayList<Planet>();
    public ArrayList<Planet> Rasi12 = new ArrayList<Planet>();

    private Planet _pLagna;//=new com.ojassoft.beans.Planet ();
    private Planet _pSun;//=new com.ojassoft.beans.Planet ();
    private Planet _pMoon;//=new com.ojassoft.beans.Planet ();
    private Planet _pMarsh;//=new com.ojassoft.beans.Planet ();
    private Planet _pMer;//=new com.ojassoft.beans.Planet ();
    private Planet _pJup;//=new com.ojassoft.beans.Planet ();
    private Planet _pVenus;//=new com.ojassoft.beans.Planet ();
    private Planet _pSat;//=new com.ojassoft.beans.Planet ();
    private Planet _pRahu;//=new com.ojassoft.beans.Planet ();
    private Planet _pKetu;//=new com.ojassoft.beans.Planet ();
    private Planet _pUranus;//=new com.ojassoft.beans.Planet ();
    private Planet _pNeptune;//=new com.ojassoft.beans.Planet ();
    private Planet _pPluto;//=new com.ojassoft.beans.Planet ();

    public void setLagna(Planet _pLagna) {
        this._pLagna = _pLagna;
    }


    public Planet getLagna() {
        return _pLagna;
    }


    public void setSun(Planet _pSun) {
        this._pSun = _pSun;
    }


    public Planet getSun() {
        return _pSun;
    }


    public void setMoon(Planet _pMoon) {
        this._pMoon = _pMoon;
    }


    public Planet getMoon() {
        return _pMoon;
    }


    public void setMarsh(Planet _pMarsh) {
        this._pMarsh = _pMarsh;
    }


    public Planet getMarsh() {
        return _pMarsh;
    }


    public void setMercury(Planet _pMer) {
        this._pMer = _pMer;
    }


    public Planet getMercury() {
        return _pMer;
    }


    public void setJupiter(Planet _pJup) {
        this._pJup = _pJup;
    }


    public Planet getJupiter() {
        return _pJup;
    }


    public void setVenus(Planet _pVenus) {
        this._pVenus = _pVenus;
    }


    public Planet getVenus() {
        return _pVenus;
    }


    public void setSaturn(Planet _pSat) {
        this._pSat = _pSat;
    }


    public Planet getSaturn() {
        return _pSat;
    }


    public void setRahu(Planet _pRahu) {
        this._pRahu = _pRahu;
    }


    public Planet getRahu() {
        return _pRahu;
    }


    public void setKetu(Planet _pKetu) {
        this._pKetu = _pKetu;
    }


    public Planet getKetu() {
        return _pKetu;
    }


    public void setUranus(Planet _pUranus) {
        this._pUranus = _pUranus;
    }


    public Planet getUranus() {
        return _pUranus;
    }


    public void setNeptune(Planet _pNeptune) {
        this._pNeptune = _pNeptune;
    }


    public Planet getNeptune() {
        return _pNeptune;
    }


    public void setPluto(Planet _pPluto) {
        this._pPluto = _pPluto;
    }


    public Planet getPluto() {
        return _pPluto;
    }


}
