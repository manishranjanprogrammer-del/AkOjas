package com.ojassoft.astrosage.beans;

import java.io.Serializable;

public class OutKpMisc implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;


    private String _fortunaRasit;
    private double _fortunaDeg = 0;
    private String _fortunaSubSub;
    private double _kpAyan = 0;


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
