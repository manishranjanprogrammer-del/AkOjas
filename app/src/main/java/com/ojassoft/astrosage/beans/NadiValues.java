package com.ojassoft.astrosage.beans;

import java.io.Serializable;

public class NadiValues implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;
    int _plntNumber = 0;
    int[] _values = null;

    public NadiValues() {

    }

    public NadiValues(int plntNumber, int[] values) {
        _plntNumber = plntNumber;
        _values = values;

    }

    public int getPlanet() {
        return _plntNumber;
    }

    public void setPlanet(int plntNumber) {
        _plntNumber = plntNumber;
    }

    public void setNadi(int[] values) {
        _values = values;
    }

    public int[] getNadi() {
        return _values;
    }


}