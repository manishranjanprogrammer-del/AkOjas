package com.ojassoft.astrosage.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class NumerologyOutputModel implements Parcelable {


    @SerializedName("Radical_Number")
    String radicalNumber;
    @SerializedName("Destiny_Number")
    String destinyNumber;
    @SerializedName("Name_Number")
    String nameNumber;
    @SerializedName("Favourable_Sign")
    String favourableSign;
    @SerializedName("Favourable_Alphabets")
    String favourableAlphabets;
    @SerializedName("Gemstone")
    String gemstone;
    @SerializedName("Favourable_Day")
    String favourableDay;
    @SerializedName("Favourable_Number")
    String favourableNumber;
    @SerializedName("Direction")
    String direction;
    @SerializedName("Auspicious_Colour")
    String auspiciousColour;
    @SerializedName("Ruling_Planet")
    String rulingPlanet;
    @SerializedName("God")
    String god;
    @SerializedName("Fast")
    String fast;
    @SerializedName("Favourable_Date")
    String favourableDate;
    @SerializedName("Mantra")
    String mantra;
    @SerializedName("Radical_Number_Desc")
    String radicalNumberDesc;
    @SerializedName("Radical_Number_Text_Desc")
    String radicalNumberTextDesc;
    @SerializedName("Destiny_Number_Desc")
    String destinyNumberDesc;
    @SerializedName("Name_Number_Desc")
    String nameNumberDesc;
    @SerializedName("Name_Number_Text_Desc")
    String nameNumberTextDesc;
    @SerializedName("Auspicious_Place_Desc")
    String auspiciousPlaceDesc;
    @SerializedName("Health_Desc")
    String healthDesc;
    @SerializedName("Auspicious_Time_Desc")
    String auspiciousTimeDesc;
    @SerializedName("Career_Desc")
    String careerDesc;
    @SerializedName("Fasts_Remedies_Desc")
    String fastsRemediesDesc;
    @SerializedName("Yantra_Desc")
    String yantraDesc;
    @SerializedName("Exclusive_Facts_Desc")
    String exclusiveFactsDesc;
    @SerializedName("Exclusive_Facts_Text_Desc")
    String exclusiveFactsTextDesc;


    protected NumerologyOutputModel(Parcel in) {
        radicalNumber = in.readString();
        destinyNumber = in.readString();
        nameNumber = in.readString();
        favourableSign = in.readString();
        favourableAlphabets = in.readString();
        gemstone = in.readString();
        favourableDay = in.readString();
        favourableNumber = in.readString();
        direction = in.readString();
        auspiciousColour = in.readString();
        rulingPlanet = in.readString();
        god = in.readString();
        fast = in.readString();
        favourableDate = in.readString();
        mantra = in.readString();
        radicalNumberDesc = in.readString();
        radicalNumberTextDesc = in.readString();
        destinyNumberDesc = in.readString();
        nameNumberDesc = in.readString();
        nameNumberTextDesc = in.readString();
        auspiciousPlaceDesc = in.readString();
        healthDesc = in.readString();
        auspiciousTimeDesc = in.readString();
        careerDesc = in.readString();
        fastsRemediesDesc = in.readString();
        yantraDesc = in.readString();
        exclusiveFactsDesc = in.readString();
        exclusiveFactsTextDesc = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(radicalNumber);
        dest.writeString(destinyNumber);
        dest.writeString(nameNumber);
        dest.writeString(favourableSign);
        dest.writeString(favourableAlphabets);
        dest.writeString(gemstone);
        dest.writeString(favourableDay);
        dest.writeString(favourableNumber);
        dest.writeString(direction);
        dest.writeString(auspiciousColour);
        dest.writeString(rulingPlanet);
        dest.writeString(god);
        dest.writeString(fast);
        dest.writeString(favourableDate);
        dest.writeString(mantra);
        dest.writeString(radicalNumberDesc);
        dest.writeString(radicalNumberTextDesc);
        dest.writeString(destinyNumberDesc);
        dest.writeString(nameNumberDesc);
        dest.writeString(nameNumberTextDesc);
        dest.writeString(auspiciousPlaceDesc);
        dest.writeString(healthDesc);
        dest.writeString(auspiciousTimeDesc);
        dest.writeString(careerDesc);
        dest.writeString(fastsRemediesDesc);
        dest.writeString(yantraDesc);
        dest.writeString(exclusiveFactsDesc);
        dest.writeString(exclusiveFactsTextDesc);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NumerologyOutputModel> CREATOR = new Creator<NumerologyOutputModel>() {
        @Override
        public NumerologyOutputModel createFromParcel(Parcel in) {
            return new NumerologyOutputModel(in);
        }

        @Override
        public NumerologyOutputModel[] newArray(int size) {
            return new NumerologyOutputModel[size];
        }
    };

    public String getRadicalNumber() {
        return radicalNumber;
    }

    public void setRadicalNumber(String radicalNumber) {
        this.radicalNumber = radicalNumber;
    }

    public String getDestinyNumber() {
        return destinyNumber;
    }

    public void setDestinyNumber(String destinyNumber) {
        this.destinyNumber = destinyNumber;
    }

    public String getNameNumber() {
        return nameNumber;
    }

    public void setNameNumber(String nameNumber) {
        this.nameNumber = nameNumber;
    }

    public String getFavourableSign() {
        return favourableSign;
    }

    public void setFavourableSign(String favourableSign) {
        this.favourableSign = favourableSign;
    }

    public String getFavourableAlphabets() {
        return favourableAlphabets;
    }

    public void setFavourableAlphabets(String favourableAlphabets) {
        this.favourableAlphabets = favourableAlphabets;
    }

    public String getGemstone() {
        return gemstone;
    }

    public void setGemstone(String gemstone) {
        this.gemstone = gemstone;
    }

    public String getFavourableDay() {
        return favourableDay;
    }

    public void setFavourableDay(String favourableDay) {
        this.favourableDay = favourableDay;
    }

    public String getFavourableNumber() {
        return favourableNumber;
    }

    public void setFavourableNumber(String favourableNumber) {
        this.favourableNumber = favourableNumber;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getAuspiciousColour() {
        return auspiciousColour;
    }

    public void setAuspiciousColour(String auspiciousColour) {
        this.auspiciousColour = auspiciousColour;
    }

    public String getRulingPlanet() {
        return rulingPlanet;
    }

    public void setRulingPlanet(String rulingPlanet) {
        this.rulingPlanet = rulingPlanet;
    }

    public String getGod() {
        return god;
    }

    public void setGod(String god) {
        this.god = god;
    }

    public String getFast() {
        return fast;
    }

    public void setFast(String fast) {
        this.fast = fast;
    }

    public String getFavourableDate() {
        return favourableDate;
    }

    public void setFavourableDate(String favourableDate) {
        this.favourableDate = favourableDate;
    }

    public String getMantra() {
        return mantra;
    }

    public void setMantra(String mantra) {
        this.mantra = mantra;
    }

    public String getRadicalNumberDesc() {
        return radicalNumberDesc;
    }

    public void setRadicalNumberDesc(String radicalNumberDesc) {
        this.radicalNumberDesc = radicalNumberDesc;
    }

    public String getRadicalNumberTextDesc() {
        return radicalNumberTextDesc;
    }

    public void setRadicalNumberTextDesc(String radicalNumberTextDesc) {
        this.radicalNumberTextDesc = radicalNumberTextDesc;
    }

    public String getDestinyNumberDesc() {
        return destinyNumberDesc;
    }

    public void setDestinyNumberDesc(String destinyNumberDesc) {
        this.destinyNumberDesc = destinyNumberDesc;
    }

    public String getNameNumberDesc() {
        return nameNumberDesc;
    }

    public void setNameNumberDesc(String nameNumberDesc) {
        this.nameNumberDesc = nameNumberDesc;
    }

    public String getNameNumberTextDesc() {
        return nameNumberTextDesc;
    }

    public void setNameNumberTextDesc(String nameNumberTextDesc) {
        this.nameNumberTextDesc = nameNumberTextDesc;
    }

    public String getAuspiciousPlaceDesc() {
        return auspiciousPlaceDesc;
    }

    public void setAuspiciousPlaceDesc(String auspiciousPlaceDesc) {
        this.auspiciousPlaceDesc = auspiciousPlaceDesc;
    }

    public String getHealthDesc() {
        return healthDesc;
    }

    public void setHealthDesc(String healthDesc) {
        this.healthDesc = healthDesc;
    }

    public String getAuspiciousTimeDesc() {
        return auspiciousTimeDesc;
    }

    public void setAuspiciousTimeDesc(String auspiciousTimeDesc) {
        this.auspiciousTimeDesc = auspiciousTimeDesc;
    }

    public String getCareerDesc() {
        return careerDesc;
    }

    public void setCareerDesc(String careerDesc) {
        this.careerDesc = careerDesc;
    }

    public String getFastsRemediesDesc() {
        return fastsRemediesDesc;
    }

    public void setFastsRemediesDesc(String fastsRemediesDesc) {
        this.fastsRemediesDesc = fastsRemediesDesc;
    }

    public String getYantraDesc() {
        return yantraDesc;
    }

    public void setYantraDesc(String yantraDesc) {
        this.yantraDesc = yantraDesc;
    }

    public String getExclusiveFactsDesc() {
        return exclusiveFactsDesc;
    }

    public void setExclusiveFactsDesc(String exclusiveFactsDesc) {
        this.exclusiveFactsDesc = exclusiveFactsDesc;
    }

    public String getExclusiveFactsTextDesc() {
        return exclusiveFactsTextDesc;
    }

    public void setExclusiveFactsTextDesc(String exclusiveFactsTextDesc) {
        this.exclusiveFactsTextDesc = exclusiveFactsTextDesc;
    }
}
