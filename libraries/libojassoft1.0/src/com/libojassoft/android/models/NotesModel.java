package com.libojassoft.android.models;

import android.os.Parcel;
import android.os.Parcelable;

public class NotesModel implements Parcelable {

    private int id;
    private int month;
    private int day;
    private int year;
    private int categoryId;
    private String tags;
    private String date;
    private String notes;
    private String timestamp;
    private boolean isCurrentDay;

    public NotesModel() {

    }


    protected NotesModel(Parcel in) {
        id = in.readInt();
        month = in.readInt();
        day = in.readInt();
        year = in.readInt();
        categoryId = in.readInt();
        tags = in.readString();
        date = in.readString();
        notes = in.readString();
        timestamp = in.readString();
        isCurrentDay = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeInt(month);
        dest.writeInt(day);
        dest.writeInt(year);
        dest.writeInt(categoryId);
        dest.writeString(tags);
        dest.writeString(date);
        dest.writeString(notes);
        dest.writeString(timestamp);
        dest.writeByte((byte) (isCurrentDay ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NotesModel> CREATOR = new Creator<NotesModel>() {
        @Override
        public NotesModel createFromParcel(Parcel in) {
            return new NotesModel(in);
        }

        @Override
        public NotesModel[] newArray(int size) {
            return new NotesModel[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public boolean isCurrentDay() {
        return isCurrentDay;
    }

    public void setCurrentDay(boolean currentDay) {
        isCurrentDay = currentDay;
    }
}
