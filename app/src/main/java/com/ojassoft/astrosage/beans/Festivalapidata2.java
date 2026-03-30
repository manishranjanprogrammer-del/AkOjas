package com.ojassoft.astrosage.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Festivalapidata2 implements Serializable {

    @SerializedName("sunrise")
    @Expose
    private String sunrise;
    @SerializedName("lagnavalue")
    @Expose
    private String lagnavalue;
    @SerializedName("lagnaDeg")
    @Expose
    private String lagnaDeg;
    @SerializedName("lagnaMin")
    @Expose
    private String lagnaMin;
    @SerializedName("lagnaSec")
    @Expose
    private String lagnaSec;

    public String getSunrise() {
        return sunrise;
    }

    public void setSunrise(String sunrise) {
        this.sunrise = sunrise;
    }

    public String getLagnavalue() {
        return lagnavalue;
    }

    public void setLagnavalue(String lagnavalue) {
        this.lagnavalue = lagnavalue;
    }

    public String getLagnaDeg() {
        return lagnaDeg;
    }

    public void setLagnaDeg(String lagnaDeg) {
        this.lagnaDeg = lagnaDeg;
    }

    public String getLagnaMin() {
        return lagnaMin;
    }

    public void setLagnaMin(String lagnaMin) {
        this.lagnaMin = lagnaMin;
    }

    public String getLagnaSec() {
        return lagnaSec;
    }

    public void setLagnaSec(String lagnaSec) {
        this.lagnaSec = lagnaSec;
    }

}
