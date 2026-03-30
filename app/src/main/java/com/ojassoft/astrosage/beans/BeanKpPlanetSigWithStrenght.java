package com.ojassoft.astrosage.beans;

import java.io.Serializable;

public class BeanKpPlanetSigWithStrenght implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    int _plntNumber = 0;
    int _plntStrength = -1;

    int _level1 = 0;
    int _level2 = 0;
    int[] _level3 = null;
    int[] _level4 = null;

    public int getPlanetStrength() {
        return _plntStrength;
    }

    public void setPlanetStrength(int plntStrength) {
        this._plntStrength = plntStrength;
    }

    public int getPlanet() {
        return _plntNumber;
    }

    public void setPlanet(int plnt) {
        this._plntNumber = plnt;
    }

    public int getLevel1() {
        return _level1;
    }

    public void setLevel1(int level1) {
        this._level1 = level1;
    }

    public int getLevel2() {
        return _level2;
    }

    public void setLevel2(int level2) {
        this._level2 = level2;
    }

    public int[] getLevel3() {
        return _level3;
    }

    public void setLevel3(int[] level3) {
        this._level3 = level3;
    }

    public int[] getLevel4() {
        return _level4;
    }

    public void setLevel4(int[] level4) {
        this._level4 = level4;
    }


}
