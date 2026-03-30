package com.ojassoft.astrosage.model;

import android.os.Parcel;
import android.os.Parcelable;

public class MuhuratModel implements Parcelable {

    private String name;
    private  String dateString;

    public MuhuratModel(){

    }
    protected MuhuratModel(Parcel in) {
        name = in.readString();
        dateString = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(dateString);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<MuhuratModel> CREATOR = new Creator<MuhuratModel>() {
        @Override
        public MuhuratModel createFromParcel(Parcel in) {
            return new MuhuratModel(in);
        }

        @Override
        public MuhuratModel[] newArray(int size) {
            return new MuhuratModel[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDateString() {
        return dateString;
    }

    public void setDateString(String dateString) {
        this.dateString = dateString;
    }
}
