package com.ojassoft.astrosage.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ojas-02 on 16/1/18.
 */

public class AllPanchangData implements Serializable {

    public ArrayList<PurnimaFastData> getVratFestivalApiData() {
        return vratFestivalApiData;
    }

    @SerializedName("festivalapidata")
    @Expose
    public ArrayList<PurnimaFastData> vratFestivalApiData;


    @SerializedName("purnimaapi")
    @Expose
   public ArrayList<PurnimaFastData> purnimaFastData;

    @SerializedName("ekadashiapi")
    @Expose
    public ArrayList<PurnimaFastData> ekadashiFastData;

    @SerializedName("pradoshvratapi")
    @Expose
    public ArrayList<PurnimaFastData> pradoshFastData;

    @SerializedName("masikshivratriapi")
    @Expose
    public ArrayList<PurnimaFastData> masikShivratriFastData;


    @SerializedName("sankashtichaturthiapi")
    @Expose
    public ArrayList<PurnimaFastData> sankashtiFastData;

    @SerializedName("amavasyaapi")
    @Expose
    public ArrayList<PurnimaFastData> amavasyaFastData;

    @SerializedName("sankrantiapi")
    @Expose
    public ArrayList<PurnimaFastData> sankrantiFastData;

    @SerializedName("indiancalendarapi")
    @Expose
    public ArrayList<PurnimaFastData> indianCalenderData;

    public ArrayList<PurnimaFastData> getPurnimaFastData() {
        return purnimaFastData;
    }
    public ArrayList<PurnimaFastData> getEkadashiFastData() {
        return ekadashiFastData;
    }


    public ArrayList<PurnimaFastData> getPradoshFastData() {
        return pradoshFastData;
    }

    public ArrayList<PurnimaFastData> getMasikShivratriFastData() {
        return masikShivratriFastData;
    }

    public ArrayList<PurnimaFastData> getSankashtiFastData() {
        return sankashtiFastData;
    }

    public ArrayList<PurnimaFastData> getAmavasyaFastData() {
        return amavasyaFastData;
    }

    public ArrayList<PurnimaFastData> getSankrantiFastData() {
        return sankrantiFastData;
    }

    public ArrayList<PurnimaFastData> getIndianCalenderData() {
        return indianCalenderData;
    }
}
