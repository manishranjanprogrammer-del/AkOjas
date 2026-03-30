package com.ojassoft.astrosage.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ojas on ९/११/१६.
 */
public class ProductCategorymodal implements Serializable
{
    public String getPId() {
        return PId;
    }

    public String getP_CatId() {
        return P_CatId;
    }

    public void setP_CatId(String p_CatId) {
        P_CatId = p_CatId;
    }

    public void setPId(String PId) {
        this.PId = PId;
    }

    @SerializedName("P_Id")
    @Expose

    private String PId;

    @SerializedName("P_CatId")
    @Expose
    private String P_CatId;

    @SerializedName("P_PriceInDoller")
    @Expose
    private String PPriceInDoller;
    @SerializedName("P_PriceInRs")
    @Expose
    private String PPriceInRs;

    public String getPPriceInDoller() {
        return PPriceInDoller;
    }

    public void setPPriceInDoller(String PPriceInDoller) {
        this.PPriceInDoller = PPriceInDoller;
    }

    public String getPPriceInRs() {
        return PPriceInRs;
    }

    public void setPPriceInRs(String PPriceInRs) {
        this.PPriceInRs = PPriceInRs;
    }
}
