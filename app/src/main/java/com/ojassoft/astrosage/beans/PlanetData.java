package com.ojassoft.astrosage.beans;

import java.io.Serializable;

public class PlanetData implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 1L;
    private long _id = 0;
    private double _pLagna;//=new PrPlanet();
    private double _pSun;//=new PrPlanet();
    private double _pMoon;//=new PrPlanet();
    private double _pMarsh;//=new PrPlanet();
    private double _pMer;//=new PrPlanet();
    private double _pJup;//=new PrPlanet();
    private double _pVenus;//=new PrPlanet();
    private double _pSat;//=new PrPlanet();
    private double _pRahu;//=new PrPlanet();
    private double _pKetu;//=new PrPlanet();
    private double _pUranus;//=new PrPlanet();
    private double _pNeptune;//=new PrPlanet();
    private double _pPluto;//=new PrPlanet();
    private double _pAyan;//=new PrPlanet();

    private String _pCombust = "";
    private String _pDirect = "";
    private String _pRelation = "";

    public void setLagna(double _pLagna) {
        this._pLagna = _pLagna;
    }

    public double getLagna() {
        return _pLagna;
    }

    public void setSun(double _pSun) {
        this._pSun = _pSun;
    }

    public double getSun() {
        return _pSun;
    }

    public void setMoon(double _pMoon) {
        this._pMoon = _pMoon;
    }

    public double getMoon() {
        return _pMoon;
    }

    public void setMarsh(double _pMarsh) {
        this._pMarsh = _pMarsh;
    }

    public double getMarsh() {
        return _pMarsh;
    }

    public void setMercury(double _pMer) {
        this._pMer = _pMer;
    }

    public double getMercury() {
        return _pMer;
    }

    public void setJup(double _pJup) {
        this._pJup = _pJup;
    }

    public double getJup() {
        return _pJup;
    }

    public void setVenus(double _pVenus) {
        this._pVenus = _pVenus;
    }

    public double getVenus() {
        return _pVenus;
    }

    public void setSat(double _pSat) {
        this._pSat = _pSat;
    }

    public double getSat() {
        return _pSat;
    }

    public void setRahu(double _pRahu) {
        this._pRahu = _pRahu;
    }

    public double getRahu() {
        return _pRahu;
    }

    public void setKetu(double _pKetu) {
        this._pKetu = _pKetu;
    }

    public double getKetu() {
        return _pKetu;
    }

    public void setUranus(double _pUranus) {
        this._pUranus = _pUranus;
    }

    public double getUranus() {
        return _pUranus;
    }

    public void setNeptune(double _pNeptune) {
        this._pNeptune = _pNeptune;
    }

    public double getNeptune() {
        return _pNeptune;
    }

    public void setPluto(double _pPluto) {
        this._pPluto = _pPluto;
    }

    public double getPluto() {
        return _pPluto;
    }

    public void set_id(long _id) {
        this._id = _id;
    }

    public long get_id() {
        return _id;
    }

    public void setAyan(double _pAyan) {
        this._pAyan = _pAyan;
    }

    public double getAyan() {
        return _pAyan;
    }

    public String getCombust() {
        return _pCombust;

    }

    public void setCombust(String _pCombust) {
        this._pCombust = _pCombust;
    }

    public String getDirect() {
        return _pDirect;
    }

    public void setDirect(String _pDirect) {
        this._pDirect = _pDirect;
    }

    public String getRelation() {
        return _pRelation;
    }

    public void setRelation(String _pRelation) {
        this._pRelation = _pRelation;
    }

}
