package com.ojassoft.astrosage.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class NameHoroscopeMatchingModel implements Parcelable {

    @SerializedName("boy_swar")
    private String boySwar;
    @SerializedName("girl_swar")
    private String girlSwar;
    @SerializedName("other_swar_comb")
    private List<NameSwarCombModel> otherSwarComb;
    @SerializedName("matching_result")
    private String matchingResult;
    @SerializedName("conclusion")
    private String conclusion;
    @SerializedName("boys_details")
    private NameMatchingDetailModel boysDetails;
    @SerializedName("girl_details")
    private NameMatchingDetailModel girlDetails;
    @SerializedName("gun_milan")
    private List<NameGunaMilanModel> gunaMilanModels;
    @SerializedName("varna_interpretation")
    private String varnaInterpretation;
    @SerializedName("vasya_interpretation")
    private String vasyaInterpretation;
    @SerializedName("tarra_interpretation")
    private String tarraInterpretation;
    @SerializedName("yoni_interpretation")
    private String yoniInterpretation;
    @SerializedName("gana_interpretation")
    private String ganaInterpretation;
    @SerializedName("nadi_interpretation")
    private String nadiInterpretation;
    @SerializedName("rasilord_interpretation")
    private String rasilordInterpretation;
    @SerializedName("bhakoot_interpretation")
    private String bhakootInterpretation;


    protected NameHoroscopeMatchingModel(Parcel in) {
        boySwar = in.readString();
        girlSwar = in.readString();
        otherSwarComb = in.readParcelable(NameSwarCombModel.class.getClassLoader());
        matchingResult = in.readString();
        conclusion = in.readString();
        boysDetails = in.readParcelable(NameMatchingDetailModel.class.getClassLoader());
        girlDetails = in.readParcelable(NameMatchingDetailModel.class.getClassLoader());
        gunaMilanModels = in.createTypedArrayList(NameGunaMilanModel.CREATOR);

        varnaInterpretation = in.readString();
        vasyaInterpretation = in.readString();
        tarraInterpretation = in.readString();
        yoniInterpretation = in.readString();
        ganaInterpretation = in.readString();
        nadiInterpretation = in.readString();
        rasilordInterpretation = in.readString();
        bhakootInterpretation = in.readString();

    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(boySwar);
        dest.writeString(girlSwar);
        dest.writeTypedList(otherSwarComb);
        dest.writeString(matchingResult);
        dest.writeString(conclusion);
        dest.writeParcelable(boysDetails, flags);
        dest.writeParcelable(girlDetails, flags);
        dest.writeTypedList(gunaMilanModels);

        dest.writeString(varnaInterpretation);
        dest.writeString(vasyaInterpretation);
        dest.writeString(tarraInterpretation);
        dest.writeString(yoniInterpretation);
        dest.writeString(ganaInterpretation);
        dest.writeString(nadiInterpretation);
        dest.writeString(rasilordInterpretation);
        dest.writeString(bhakootInterpretation);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NameHoroscopeMatchingModel> CREATOR = new Creator<NameHoroscopeMatchingModel>() {
        @Override
        public NameHoroscopeMatchingModel createFromParcel(Parcel in) {
            return new NameHoroscopeMatchingModel(in);
        }

        @Override
        public NameHoroscopeMatchingModel[] newArray(int size) {
            return new NameHoroscopeMatchingModel[size];
        }
    };

    public String getBoySwar() {
        return boySwar;
    }

    public void setBoySwar(String boySwar) {
        this.boySwar = boySwar;
    }

    public String getGirlSwar() {
        return girlSwar;
    }

    public void setGirlSwar(String girlSwar) {
        this.girlSwar = girlSwar;
    }

    public List<NameSwarCombModel> getOtherSwarComb() {
        return otherSwarComb;
    }

    public void setOtherSwarComb(List<NameSwarCombModel> otherSwarComb) {
        this.otherSwarComb = otherSwarComb;
    }

    public String getMatchingResult() {
        return matchingResult;
    }

    public void setMatchingResult(String matchingResult) {
        this.matchingResult = matchingResult;
    }

    public String getConclusion() {
        return conclusion;
    }

    public void setConclusion(String conclusion) {
        this.conclusion = conclusion;
    }

    public NameMatchingDetailModel getBoysDetails() {
        return boysDetails;
    }

    public void setBoysDetails(NameMatchingDetailModel boysDetails) {
        this.boysDetails = boysDetails;
    }

    public NameMatchingDetailModel getGirlDetails() {
        return girlDetails;
    }

    public void setGirlDetails(NameMatchingDetailModel girlDetails) {
        this.girlDetails = girlDetails;
    }

    public List<NameGunaMilanModel> getGunaMilanModels() {
        return gunaMilanModels;
    }

    public void setGunaMilanModels(List<NameGunaMilanModel> gunaMilanModels) {
        this.gunaMilanModels = gunaMilanModels;
    }

    public String getVarnaInterpretation() {
        return varnaInterpretation;
    }

    public void setVarnaInterpretation(String varnaInterpretation) {
        this.varnaInterpretation = varnaInterpretation;
    }

    public String getVasyaInterpretation() {
        return vasyaInterpretation;
    }

    public void setVasyaInterpretation(String vasyaInterpretation) {
        this.vasyaInterpretation = vasyaInterpretation;
    }

    public String getTarraInterpretation() {
        return tarraInterpretation;
    }

    public void setTarraInterpretation(String tarraInterpretation) {
        this.tarraInterpretation = tarraInterpretation;
    }

    public String getYoniInterpretation() {
        return yoniInterpretation;
    }

    public void setYoniInterpretation(String yoniInterpretation) {
        this.yoniInterpretation = yoniInterpretation;
    }

    public String getGanaInterpretation() {
        return ganaInterpretation;
    }

    public void setGanaInterpretation(String ganaInterpretation) {
        this.ganaInterpretation = ganaInterpretation;
    }

    public String getNadiInterpretation() {
        return nadiInterpretation;
    }

    public void setNadiInterpretation(String nadiInterpretation) {
        this.nadiInterpretation = nadiInterpretation;
    }

    public String getRasilordInterpretation() {
        return rasilordInterpretation;
    }

    public void setRasilordInterpretation(String rasilordInterpretation) {
        this.rasilordInterpretation = rasilordInterpretation;
    }

    public String getBhakootInterpretation() {
        return bhakootInterpretation;
    }

    public void setBhakootInterpretation(String bhakootInterpretation) {
        this.bhakootInterpretation = bhakootInterpretation;
    }
}
