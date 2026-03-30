package com.ojassoft.astrosage.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class NameMatchingDetailModel implements Parcelable {

    @SerializedName("nakshatra")
    String nakshatra;
    @SerializedName("charan")
    String charan;
    @SerializedName("rashi")
    String rashi;

    protected NameMatchingDetailModel(Parcel in) {
        nakshatra = in.readString();
        charan = in.readString();
        rashi = in.readString();
    }

    public static final Creator<NameMatchingDetailModel> CREATOR = new Creator<NameMatchingDetailModel>() {
        @Override
        public NameMatchingDetailModel createFromParcel(Parcel in) {
            return new NameMatchingDetailModel(in);
        }

        @Override
        public NameMatchingDetailModel[] newArray(int size) {
            return new NameMatchingDetailModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(nakshatra);
        dest.writeString(charan);
        dest.writeString(rashi);
    }

    public String getNakshatra() {
        return nakshatra;
    }

    public void setNakshatra(String nakshatra) {
        this.nakshatra = nakshatra;
    }

    public String getCharan() {
        return charan;
    }

    public void setCharan(String charan) {
        this.charan = charan;
    }

    public String getRashi() {
        return rashi;
    }

    public void setRashi(String rashi) {
        this.rashi = rashi;
    }
}
