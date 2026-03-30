
package com.ojassoft.astrosage.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FestivalDetailData {

    @SerializedName("status")
    @Expose
    private String status;
    @SerializedName("festivalapiname")
    @Expose
    private String festivalapiname;
    @SerializedName("festivalapidata")
    @Expose
    private List<Festivalapidata> festivalapidata = null;

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

    public List<Festivalapidata> getFestivalapidata() {
        return festivalapidata;
    }

    public void setFestivalapidata(List<Festivalapidata> festivalapidata) {
        this.festivalapidata = festivalapidata;
    }

}
