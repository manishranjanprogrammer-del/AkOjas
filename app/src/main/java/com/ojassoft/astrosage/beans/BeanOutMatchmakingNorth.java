package com.ojassoft.astrosage.beans;

import java.io.Serializable;

/**
 * this is out bean for matching making north
 *
 * @author Hukum
 */
public class BeanOutMatchmakingNorth implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    double _matchPointVarna = 0, _matchPointVasya = 0, _matchPointTara = 0,
            _matchPointYoni = 0, _matchPointMaitri = 0, _matchPointGana = 0,
            _matchPointBhakoot = 0, _matchPointNadi = 0;
    /*
     * int _totalPointsObtained=_matchPointVarna+_matchPointVasya+
     * _matchPointTara+_matchPointYoni+_matchPointMaitri+
     * _matchPointGana+_matchPointBhakoot+_matchPointNadi;
     */
    String _varnaPrediction, _vasyaPrediction, _taraPrediction,
            _yoniPrediction, _maitriPrediction, _ganaPrediction,
            _bhakootPrediction, _nadiPrediction;
    String _boyMangalDosha = "", _girlMangalDosha = "", _conclusion = "";
    String _boyRasiNumber = "", _girlRasiNumber = "", _boyMoonDegree = "",
            _girlMoonDegree = "";// ADDED BY BIJENDRA ON 19-06-14

    /**
     * it return varna matching point
     *
     * @return _matchPointVarna
     */
    public double getMatchPointVarna() {
        return _matchPointVarna;
    }

    /**
     * this method sets varna matching points
     *
     * @param _matchPointVarna
     */
    public void setMatchPointVarna(double _matchPointVarna) {
        this._matchPointVarna = _matchPointVarna;
    }

    /**
     * it returns vasya matching points in int.
     *
     * @return int _matchPointVasya
     */
    public double getMatchPointVasya() {
        return _matchPointVasya;
    }

    /**
     * this sets matching points of vasya.
     *
     * @param _matchPointVasya
     */
    public void setMatchPointVasya(double _matchPointVasya) {
        this._matchPointVasya = _matchPointVasya;
    }

    /**
     * it return tara matching points
     *
     * @return
     */
    public double getMatchPointTara() {
        return _matchPointTara;
    }

    /**
     * it sets tara matching points.
     *
     * @param _matchPointTara
     */
    public void setMatchPointTara(double _matchPointTara) {
        this._matchPointTara = _matchPointTara;
    }

    /**
     * it returns yoni matching points.
     *
     * @return
     */
    public double getMatchPointYoni() {
        return _matchPointYoni;
    }

    /**
     * sets yoni matching points.
     *
     * @param _matchPointYoni
     */
    public void setMatchPointYoni(double _matchPointYoni) {
        this._matchPointYoni = _matchPointYoni;
    }

    /**
     * it returns maitri matching points
     *
     * @return
     */
    public double getMatchPointMaitri() {
        return _matchPointMaitri;
    }

    /**
     * sets maitri matching point.
     *
     * @param _matchPointBhakoot
     */
    public void setMatchPointMaitri(double _matchPointMaitri) {
        this._matchPointMaitri = _matchPointMaitri;
    }

    /**
     * it returns gana matching points
     *
     * @return
     */
    public double getMatchPointGana() {
        return _matchPointGana;
    }

    /**
     * sets gana matching point.
     *
     * @param _matchPointBhakoot
     */
    public void setMatchPointGana(double _matchPointGana) {
        this._matchPointGana = _matchPointGana;
    }

    /**
     * it returns bhakoot matching points
     *
     * @return
     */
    public double getMatchPointBhakoot() {
        return _matchPointBhakoot;
    }

    /**
     * sets bhakoot matching point.
     *
     * @param _matchPointBhakoot
     */
    public void setMatchPointBhakoot(double _matchPointBhakoot) {
        this._matchPointBhakoot = _matchPointBhakoot;
    }

    /**
     * it returns nadi matching points
     *
     * @return
     */
    public double getMatchPointNadi() {
        return _matchPointNadi;
    }

    /**
     * sets nadi matching points
     *
     * @param _matchPointNadi
     */
    public void setMatchPointNadi(double _matchPointNadi) {
        this._matchPointNadi = _matchPointNadi;
    }

    /**
     * it returns total matching points
     *
     * @return
     */
    public double getTotalPointsObtained() {
        return (_matchPointVarna + _matchPointVasya + _matchPointTara
                + _matchPointYoni + _matchPointMaitri + _matchPointGana
                + _matchPointBhakoot + _matchPointNadi);
    }

    /**
     * it returns yoni vatch points
     *
     * @return
     */
    public double getVatchPointYoni() {
        return _matchPointYoni;
    }

    /**
     * sets yoni vatch points
     *
     * @param _nadiPrediction
     */
    public void setVatchPointYoni(double _matchPointYoni) {
        this._matchPointYoni = _matchPointYoni;
    }

    /**
     * it returns varna prediction
     *
     * @return
     */
    public String getVarnaPrediction() {
        return _varnaPrediction;
    }

    /**
     * sets varna prediction
     *
     * @param _nadiPrediction
     */
    public void setVarnaPrediction(String _varnaPrediction) {
        this._varnaPrediction = _varnaPrediction;
    }

    /**
     * it returns vasya prediction
     *
     * @return
     */
    public String getVasyaPrediction() {
        return _vasyaPrediction;
    }

    /**
     * sets nadi prediction
     *
     * @param _nadiPrediction
     */
    public void setVasyaPrediction(String _vasyaPrediction) {
        this._vasyaPrediction = _vasyaPrediction;
    }

    /**
     * it returns Tara prediction
     *
     * @return
     */
    public String getTaraPrediction() {
        return _taraPrediction;
    }

    /**
     * sets tara prediction
     *
     * @param _nadiPrediction
     */
    public void setTaraPrediction(String _taraPrediction) {
        this._taraPrediction = _taraPrediction;
    }

    /**
     * it returns yoni prediction
     *
     * @return
     */
    public String getYoniPrediction() {
        return _yoniPrediction;
    }

    /**
     * sets yoni prediction
     *
     * @param _nadiPrediction
     */
    public void setYoniPrediction(String _yoniPrediction) {
        this._yoniPrediction = _yoniPrediction;
    }

    /**
     * it returns maitri prediction
     *
     * @return
     */
    public String getMaitriPrediction() {
        return _maitriPrediction;
    }

    /**
     * sets maitri prediction
     *
     * @param _nadiPrediction
     */
    public void setMaitriPrediction(String _maitriPrediction) {
        this._maitriPrediction = _maitriPrediction;
    }

    /**
     * it returns gana prediction
     *
     * @return
     */
    public String getGanaPrediction() {
        return _ganaPrediction;
    }

    /**
     * sets gana prediction
     *
     * @param _nadiPrediction
     */
    public void setGanaPrediction(String _ganaPrediction) {
        this._ganaPrediction = _ganaPrediction;
    }

    /**
     * it returns vasya prediction
     *
     * @return
     */
    public String getBhakootPrediction() {
        return _bhakootPrediction;
    }

    /**
     * sets bhakoot prediction
     *
     * @param _nadiPrediction
     */
    public void setBhakootPrediction(String _bhakootPrediction) {
        this._bhakootPrediction = _bhakootPrediction;
    }

    /**
     * it returns nadi prediction
     *
     * @return
     */
    public String getNadiPrediction() {
        return _nadiPrediction;
    }

    /**
     * sets nadi prediction
     *
     * @param _nadiPrediction
     */
    public void setNadiPrediction(String _nadiPrediction) {
        this._nadiPrediction = _nadiPrediction;
    }

    /**
     * it returns boy mangal dosha
     *
     * @return
     */
    public String getBoyMangalDosha() {
        return _boyMangalDosha;
    }

    /**
     * sets boy mangal dosha
     *
     * @param _boyMangalDosha
     */
    public void setBoyMangalDosha(String _boyMangalDosha) {
        this._boyMangalDosha = _boyMangalDosha;
    }

    /**
     * it returns girl mangal dosha
     *
     * @return
     */
    public String getGirlMangalDosha() {
        return _girlMangalDosha;
    }

    /**
     * sets girl mangal dosha
     *
     * @param _girlMangalDosha
     */
    public void setGirlMangalDosha(String _girlMangalDosha) {
        this._girlMangalDosha = _girlMangalDosha;
    }

    /**
     * it returns conclusion
     *
     * @return
     */
    public String getConclusion() {
        return _conclusion;
    }

    /**
     * sets conclusion
     *
     * @param _conclusion
     */
    public void setConclusion(String _conclusion) {
        this._conclusion = _conclusion;
    }

    /**
     * return boy rasi number
     *
     * @return
     */
    public String getBoyRasiNumber() {
        return _boyRasiNumber;
    }

    /**
     * sets boy rasi number
     *
     * @param boyRasiNumber
     */
    public void setBoyRasiNumber(String boyRasiNumber) {
        this._boyRasiNumber = boyRasiNumber;
    }

    /**
     * return girl rasi number
     *
     * @return
     */
    public String getGirlRasiNumber() {
        return _girlRasiNumber;
    }

    /**
     * set girl rasi number
     *
     * @param girlRasiNumber
     */
    public void setGirlRasiNumber(String girlRasiNumber) {
        this._girlRasiNumber = girlRasiNumber;
    }

    /**
     * return boy moon degree
     *
     * @return
     */
    public String geBoyMoonDegree() {
        return _boyMoonDegree;
    }

    /**
     * set girl moon degree
     *
     * @param girlMoonDegree
     */
    public void setBoyMoonDegree(String boyMoonDegree) {
        this._boyMoonDegree = boyMoonDegree;
    }

    /**
     * return girl moon degree
     *
     * @return
     */
    public String getGirlMoonDegree() {
        return _girlMoonDegree;
    }

    /**
     * set girl moon degree
     *
     * @param girlMoonDegree
     */
    public void setGirlMoonDegree(String girlMoonDegree) {
        this._girlMoonDegree = girlMoonDegree;
    }
}
