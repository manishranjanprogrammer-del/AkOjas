package com.ojassoft.astrosage.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NewFestivalBean {

    @SerializedName("festivals")
    @Expose
    private List<Festival> festivals = null;

    public List<Festival> getFestivals() {
        return festivals;
    }

    public void setFestivals(List<Festival> festivals) {
        this.festivals = festivals;
    }

}



