package com.ojassoft.astrosage.model;

import android.os.Parcel;
import android.os.Parcelable;

public class NameSwarModel implements Parcelable {

    String swar;
    String nakshtra;
    String nakshtranumber;
    String charan;


    protected NameSwarModel(Parcel in) {
        swar = in.readString();
        nakshtra = in.readString();
        nakshtranumber = in.readString();
        charan = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(swar);
        dest.writeString(nakshtra);
        dest.writeString(nakshtranumber);
        dest.writeString(charan);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NameSwarModel> CREATOR = new Creator<NameSwarModel>() {
        @Override
        public NameSwarModel createFromParcel(Parcel in) {
            return new NameSwarModel(in);
        }

        @Override
        public NameSwarModel[] newArray(int size) {
            return new NameSwarModel[size];
        }
    };

    public String getSwar() {
        return swar;
    }

    public void setSwar(String swar) {
        this.swar = swar;
    }

    public String getNakshtra() {
        return nakshtra;
    }

    public void setNakshtra(String nakshtra) {
        this.nakshtra = nakshtra;
    }

    public String getNakshtranumber() {
        return nakshtranumber;
    }

    public void setNakshtranumber(String nakshtranumber) {
        this.nakshtranumber = nakshtranumber;
    }

    public String getCharan() {
        return charan;
    }

    public void setCharan(String charan) {
        this.charan = charan;
    }
}
