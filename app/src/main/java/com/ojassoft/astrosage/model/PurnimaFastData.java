package com.ojassoft.astrosage.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ojas-02 on 15/1/18.
 */

public class PurnimaFastData implements Serializable {
    @SerializedName("festival_name")
    @Expose

    private String festivalName;


    @SerializedName("festival_date")
    @Expose

    private String festivalDate;


    @SerializedName("festival_url")

    @Expose
    String festival_url;
    @SerializedName("festival_image_url")
    @Expose
    String festival_image_url;
    @SerializedName("festival_page_view")
    @Expose
    String festival_page_view;

    public String getFestivalName() {
        return festivalName;
    }

    public String getFestivalDate() {
        return festivalDate;
    }

    public String getFestival_url() {
        return festival_url;
    }

    public String getFestival_image_url() {
        return festival_image_url;
    }

    public String getFestival_page_view() {
        return festival_page_view;
    }
}
