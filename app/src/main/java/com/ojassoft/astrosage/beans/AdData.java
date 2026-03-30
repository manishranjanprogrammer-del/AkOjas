package com.ojassoft.astrosage.beans;

/**
 * Created by ojas on २४/४/१७.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;


import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.ojassoft.astrosage.model.CustomAddModel;

public class AdData implements Serializable {

    @SerializedName("IsShowBanner")
    @Expose
    private String isShowBanner;
    @SerializedName("slot")
    @Expose
    private String slot;
    @SerializedName("ImageObj")
    @Expose
    private List<CustomAddModel> imageObj = null;

    public String getIsShowBanner() {
        return isShowBanner;
    }

    public void setIsShowBanner(String isShowBanner) {
        this.isShowBanner = isShowBanner;
    }

    public String getSlot() {
        return slot;
    }

    public void setSlot(String slot) {
        this.slot = slot;
    }

    public List<CustomAddModel> getImageObj() {
        return imageObj;
    }

    public void setImageObj(List<CustomAddModel> imageObj) {
        this.imageObj = imageObj;
    }

}
