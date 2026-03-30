package com.ojassoft.astrosage.beans;

import java.io.Serializable;


public class BeanNakshtraNadi implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private NadiValues _plntNadi = new NadiValues();
    private NadiValues _starLordNadi = new NadiValues();
    private NadiValues _subLordNadi = new NadiValues();

    public BeanNakshtraNadi() {

    }

    public NadiValues getPlanetNakstraNadi() {
        return _plntNadi;
    }

    public void setPlanetNakstraNadi(NadiValues plntNadi) {
        _plntNadi = plntNadi;
    }

    public NadiValues getPlanetStarLordNadi() {
        return _starLordNadi;
    }

    public void setPlanetStarLordNadi(NadiValues starLordNadi) {
        _starLordNadi = starLordNadi;
    }

    public NadiValues getPlanetSubLordNadi() {
        return _subLordNadi;
    }

    public void setPlanetSubLordNadi(NadiValues subLordNadi) {
        _subLordNadi = subLordNadi;
    }

}

