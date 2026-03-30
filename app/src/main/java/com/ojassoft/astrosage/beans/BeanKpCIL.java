package com.ojassoft.astrosage.beans;

import java.io.Serializable;

public class BeanKpCIL implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    int _cusp = 0;
    int[] _type1 = null;
    int[] _type2 = null;
    int[] _type3 = null;
    int _type4 = 0;


    public int getCusp() {
        return _cusp;
    }

    public void setCusp(int cusp) {
        this._cusp = cusp;
    }

    public int[] getKpCILType1() {
        return _type1;
    }

    public void setKpCILType1(int[] type1) {
        this._type1 = type1;
    }

    public int[] getKpCILType2() {
        return _type2;
    }

    public void setKpCILType2(int[] type2) {
        this._type2 = type2;
    }

    public int[] getKpCILType3() {
        return _type3;
    }

    public void setKpCILType3(int[] type3) {
        this._type3 = type3;
    }

    public int getKpCILType4() {
        return _type4;
    }

    public void setKpCILType4(int type4) {
        this._type4 = type4;
    }

}
