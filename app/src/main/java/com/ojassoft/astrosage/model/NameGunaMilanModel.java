package com.ojassoft.astrosage.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class NameGunaMilanModel implements Parcelable {

    @SerializedName("gun")
    String gun;
    @SerializedName("boy")
    String boy;
    @SerializedName("girl")
    String girl;
    @SerializedName("max_obtain")
    String maxObtain;
    @SerializedName("obtain_point")
    String obtainPoint;
    @SerializedName("area_life")
    String areaLife;

    protected NameGunaMilanModel(Parcel in) {
        gun = in.readString();
        boy = in.readString();
        girl = in.readString();
        maxObtain = in.readString();
        obtainPoint = in.readString();
        areaLife = in.readString();
    }

    public static final Creator<NameGunaMilanModel> CREATOR = new Creator<NameGunaMilanModel>() {
        @Override
        public NameGunaMilanModel createFromParcel(Parcel in) {
            return new NameGunaMilanModel(in);
        }

        @Override
        public NameGunaMilanModel[] newArray(int size) {
            return new NameGunaMilanModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(gun);
        dest.writeString(boy);
        dest.writeString(girl);
        dest.writeString(maxObtain);
        dest.writeString(obtainPoint);
        dest.writeString(areaLife);
    }

    public String getGun() {
        return gun;
    }

    public void setGun(String gun) {
        this.gun = gun;
    }

    public String getBoy() {
        return boy;
    }

    public void setBoy(String boy) {
        this.boy = boy;
    }

    public String getGirl() {
        return girl;
    }

    public void setGirl(String girl) {
        this.girl = girl;
    }

    public String getMaxObtain() {
        return maxObtain;
    }

    public void setMaxObtain(String maxObtain) {
        this.maxObtain = maxObtain;
    }

    public String getObtainPoint() {
        return obtainPoint;
    }

    public void setObtainPoint(String obtainPoint) {
        this.obtainPoint = obtainPoint;
    }

    public String getAreaLife() {
        return areaLife;
    }

    public void setAreaLife(String areaLife) {
        this.areaLife = areaLife;
    }
}
