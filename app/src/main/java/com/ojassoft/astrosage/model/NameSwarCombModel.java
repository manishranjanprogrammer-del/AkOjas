package com.ojassoft.astrosage.model;

import android.os.Parcel;
import android.os.Parcelable;

public class NameSwarCombModel implements Parcelable {

    String boyName;
    String boyNakshtra;
    String boyCharan;
    String girlName;
    String girlNakshtra;
    String girlCharan;

    protected NameSwarCombModel(Parcel in) {
        boyName = in.readString();
        boyNakshtra = in.readString();
        boyCharan = in.readString();
        girlName = in.readString();
        girlNakshtra = in.readString();
        girlCharan = in.readString();
    }

    public static final Creator<NameSwarCombModel> CREATOR = new Creator<NameSwarCombModel>() {
        @Override
        public NameSwarCombModel createFromParcel(Parcel in) {
            return new NameSwarCombModel(in);
        }

        @Override
        public NameSwarCombModel[] newArray(int size) {
            return new NameSwarCombModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(boyName);
        dest.writeString(boyNakshtra);
        dest.writeString(boyCharan);
        dest.writeString(girlName);
        dest.writeString(girlNakshtra);
        dest.writeString(girlCharan);
    }

    public String getBoyName() {
        return boyName;
    }

    public void setBoyName(String boyName) {
        this.boyName = boyName;
    }

    public String getBoyNakshtra() {
        return boyNakshtra;
    }

    public void setBoyNakshtra(String boyNakshtra) {
        this.boyNakshtra = boyNakshtra;
    }

    public String getBoyCharan() {
        return boyCharan;
    }

    public void setBoyCharan(String boyCharan) {
        this.boyCharan = boyCharan;
    }

    public String getGirlName() {
        return girlName;
    }

    public void setGirlName(String girlName) {
        this.girlName = girlName;
    }

    public String getGirlNakshtra() {
        return girlNakshtra;
    }

    public void setGirlNakshtra(String girlNakshtra) {
        this.girlNakshtra = girlNakshtra;
    }

    public String getGirlCharan() {
        return girlCharan;
    }

    public void setGirlCharan(String girlCharan) {
        this.girlCharan = girlCharan;
    }
}
