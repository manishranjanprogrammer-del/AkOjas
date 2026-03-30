package com.ojassoft.astrosage.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ojas on २१/३/१८.
 */

public class TrackModal implements Serializable {


    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getStatus_detail() {
        return status_detail;
    }

    public void setStatus_detail(String status_detail) {
        this.status_detail = status_detail;
    }

    @SerializedName("time")

    private String time;

    @SerializedName("location")
    private String location;

    @SerializedName("status_detail")
    private String status_detail;
}
