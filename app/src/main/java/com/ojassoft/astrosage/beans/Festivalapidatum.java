package com.ojassoft.astrosage.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class Festivalapidatum implements Serializable {

    @SerializedName("lagnaName")
    @Expose
    private String lagnaName;
    @SerializedName("lagnaStart")
    @Expose
    private String lagnaStart;
    @SerializedName("lagnaEnd")
    @Expose
    private String lagnaEnd;
    @SerializedName("lagnaNatureNum")
    @Expose
    private String lagnaNatureNum;
    @SerializedName("lagnaNature")
    @Expose
    private String lagnaNature;

    public String getLagnaName() {
        return lagnaName;
    }

    public void setLagnaName(String lagnaName) {
        this.lagnaName = lagnaName;
    }

    public String getLagnaStart() {
        return lagnaStart;
    }

    public void setLagnaStart(String lagnaStart) {
        this.lagnaStart = lagnaStart;
    }

    public String getLagnaEnd() {
        return lagnaEnd;
    }

    public void setLagnaEnd(String lagnaEnd) {
        this.lagnaEnd = lagnaEnd;
    }

    public String getLagnaNatureNum() {
        return lagnaNatureNum;
    }

    public void setLagnaNatureNum(String lagnaNatureNum) {
        this.lagnaNatureNum = lagnaNatureNum;
    }

    public String getLagnaNature() {
        return lagnaNature;
    }

    public void setLagnaNature(String lagnaNature) {
        this.lagnaNature = lagnaNature;
    }

}
