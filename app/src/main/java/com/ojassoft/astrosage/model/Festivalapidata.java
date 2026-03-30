
package com.ojassoft.astrosage.model;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Festivalapidata {

    @SerializedName("festival_name")
    @Expose
    private String festivalName;
    @SerializedName("festival_date")
    @Expose
    private String festivalDate;
    @SerializedName("festival_image_url")
    @Expose
    private String festivalImageUrl;
    @SerializedName("festival_muhurat")
    @Expose
    private List<FestivalMuhurat> festivalMuhurat = null;
    @SerializedName("festival_contant")
    @Expose
    private String festivalContant;

    public String getFestivalName() {
        return festivalName;
    }

    public void setFestivalName(String festivalName) {
        this.festivalName = festivalName;
    }

    public String getFestivalDate() {
        return festivalDate;
    }

    public void setFestivalDate(String festivalDate) {
        this.festivalDate = festivalDate;
    }

    public String getFestivalImageUrl() {
        return festivalImageUrl;
    }

    public void setFestivalImageUrl(String festivalImageUrl) {
        this.festivalImageUrl = festivalImageUrl;
    }

    public List<FestivalMuhurat> getFestivalMuhurat() {
        return festivalMuhurat;
    }

    public void setFestivalMuhurat(List<FestivalMuhurat> festivalMuhurat) {
        this.festivalMuhurat = festivalMuhurat;
    }

    public String getFestivalContant() {
        return festivalContant;
    }

    public void setFestivalContant(String festivalContant) {
        this.festivalContant = festivalContant;
    }

}
