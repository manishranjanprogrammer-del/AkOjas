package com.ojassoft.astrosage.beans;

import com.google.gson.annotations.SerializedName;

/**
 * Created by ojas on २८/३/१६.
 */
public class AstroShopCountryModel {
    public String getCn_SId() {
        return Cn_SId;
    }

    public void setCn_SId(String cn_SId) {
        Cn_SId = cn_SId;
    }

    public String getCn_Name() {
        return Cn_Name;
    }

    public void setCn_Name(String cn_Name) {
        Cn_Name = cn_Name;
    }

    public String getCn_SPrice() {
        return Cn_SPrice;
    }

    public void setCn_SPrice(String cn_SPrice) {
        Cn_SPrice = cn_SPrice;
    }
    @SerializedName("Cn_SId")
    String Cn_SId;
    @SerializedName("Cn_Name")
    String Cn_Name;
    @SerializedName("Cn_SPrice")
    String Cn_SPrice;
}
