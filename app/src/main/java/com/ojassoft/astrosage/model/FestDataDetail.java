package com.ojassoft.astrosage.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class FestDataDetail implements Parcelable {

    @SerializedName("festival_date")
    private String festStartDate;
    @SerializedName("festival_end_date")
    private String festEndDate;
    @SerializedName("starthr")
    private String starthr;
    @SerializedName("startmin")
    private String startmin;
    @SerializedName("endhr")
    private String endhr;
    @SerializedName("endmin")
    private String endmin;

    protected FestDataDetail(Parcel in) {
        festStartDate = in.readString();
        festEndDate = in.readString();
        starthr = in.readString();
        startmin = in.readString();
        endhr = in.readString();
        endmin = in.readString();
    }

    public static final Creator<FestDataDetail> CREATOR = new Creator<FestDataDetail>() {
        @Override
        public FestDataDetail createFromParcel(Parcel in) {
            return new FestDataDetail(in);
        }

        @Override
        public FestDataDetail[] newArray(int size) {
            return new FestDataDetail[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(festStartDate);
        dest.writeString(festEndDate);
        dest.writeString(starthr);
        dest.writeString(startmin);
        dest.writeString(endhr);
        dest.writeString(endmin);
    }

    public String getFestStartDate() {
        return festStartDate;
    }

    public void setFestStartDate(String festStartDate) {
        this.festStartDate = festStartDate;
    }

    public String getFestEndDate() {
        return festEndDate;
    }

    public void setFestEndDate(String festEndDate) {
        this.festEndDate = festEndDate;
    }

    public String getStarthr() {
        return starthr;
    }

    public void setStarthr(String starthr) {
        this.starthr = starthr;
    }

    public String getStartmin() {
        return startmin;
    }

    public void setStartmin(String startmin) {
        this.startmin = startmin;
    }

    public String getEndhr() {
        return endhr;
    }

    public void setEndhr(String endhr) {
        this.endhr = endhr;
    }

    public String getEndmin() {
        return endmin;
    }

    public void setEndmin(String endmin) {
        this.endmin = endmin;
    }
}

