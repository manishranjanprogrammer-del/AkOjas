package com.ojassoft.astrosage.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class BeanLagnaTable implements Serializable {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("festivalapiname")
    @Expose
    private String festivalapiname;
    @SerializedName("festivalapidate")
    @Expose
    private String festivalapidate;
    @SerializedName("festivalapidata2")
    @Expose
    private Festivalapidata2 festivalapidata2;
    @SerializedName("festivalapidata")
    @Expose
    private List<Festivalapidatum> festivalapidata = null;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getFestivalapiname() {
        return festivalapiname;
    }

    public void setFestivalapiname(String festivalapiname) {
        this.festivalapiname = festivalapiname;
    }

    public String getFestivalapidate() {
        return festivalapidate;
    }

    public void setFestivalapidate(String festivalapidate) {
        this.festivalapidate = festivalapidate;
    }

    public Festivalapidata2 getFestivalapidata2() {
        return festivalapidata2;
    }

    public void setFestivalapidata2(Festivalapidata2 festivalapidata2) {
        this.festivalapidata2 = festivalapidata2;
    }

    public List<Festivalapidatum> getFestivalapidata() {
        return festivalapidata;
    }

    public void setFestivalapidata(List<Festivalapidatum> festivalapidata) {
        this.festivalapidata = festivalapidata;
    }

}





