package com.ojassoft.astrosage.varta.model;

import android.os.Parcel;
import android.os.Parcelable;

public class LanguageModel implements Parcelable {

    public static final Creator<LanguageModel> CREATOR = new Creator<LanguageModel>() {
        @Override
        public LanguageModel createFromParcel(Parcel in) {
            return new LanguageModel(in);
        }

        @Override
        public LanguageModel[] newArray(int size) {
            return new LanguageModel[size];
        }
    };
    private String languageName;
    private boolean isSelected;
    private String languageCode;

    public LanguageModel() {

    }

    protected LanguageModel(Parcel in) {
        languageName = in.readString();
        isSelected = in.readByte() != 0;
        languageCode = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(languageName);
        dest.writeByte((byte) (isSelected ? 1 : 0));
        dest.writeString(languageCode);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getLanguageName() {
        return languageName;
    }

    public void setLanguageName(String languageName) {
        this.languageName = languageName;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public String getLanguageCode() {
        return languageCode;
    }

    public void setLanguageCode(String languageCode) {
        this.languageCode = languageCode;
    }
}
