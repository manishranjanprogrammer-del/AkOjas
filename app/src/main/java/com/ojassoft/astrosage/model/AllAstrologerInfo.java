package com.ojassoft.astrosage.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ojas-20 on 9/3/18.
 */

public class AllAstrologerInfo implements Serializable {
    @SerializedName("AstrologerName")
    @Expose
    private String astrologerName;

    @SerializedName("ImageUrl")
    @Expose
    private String imageUrl;

    @SerializedName("Expertise")
    @Expose
    private String expertise;

    @SerializedName("ExpertIn")
    @Expose
    private String expertIn;

    public String getAstrologerName() {
        return astrologerName;
    }

    public void setAstrologerName(String astrologerName) {
        this.astrologerName = astrologerName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }

    public String getExpertIn() {
        return expertIn;
    }

    public void setExpertIn(String expertIn) {
        this.expertIn = expertIn;
    }
}
