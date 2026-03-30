package com.ojassoft.astrosage.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ojas-02 on 5/4/18.
 */

public class OpenChartData implements Serializable {

    public ArrayList<OpenChartBean> getOpenChartBean() {
        return openChartBean;
    }

    @SerializedName("UserData")
    @Expose

    public ArrayList<OpenChartBean> openChartBean;
}
