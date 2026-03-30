package com.ojassoft.astrosage.beans;

import java.io.Serializable;

public class BeanKpKhullarCIL implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    int _plntNumber = 0;

    int[] _type1 = null;
    int[] _type2 = null;
    int[] _type3 = null;
    int[] _type4 = null;


    public int getPlanet() {
        return _plntNumber;
    }

    public void setPlanet(int plntNumber) {
        this._plntNumber = plntNumber;
    }

    public int[] getKCILType1() {
        return _type1;
    }

    public void setKCILType1(int[] type1) {
        this._type1 = type1;
    }

    public int[] getKCILType2() {
        return _type2;
    }

    public void setKCILType2(int[] type2) {
        this._type2 = type2;
    }

    public int[] getKCILType3() {
        return _type3;
    }

    public void setKCILType3(int[] type3) {
        this._type3 = type3;
    }

    public int[] getKCILType4() {
        return _type4;
    }

    public void setKCILType4(int[] type4) {
        this._type4 = type4;
    }

}
