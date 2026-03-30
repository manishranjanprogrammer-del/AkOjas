package com.ojassoft.astrosage.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.libojassoft.android.customrssfeed.YoutubeVideoBean;
import com.ojassoft.astrosage.beans.PlaylistVedio;

import java.util.ArrayList;

public class VideoListModel implements Parcelable {

    private static VideoListModel videoListModel;

    private VideoListModel() {

    }

    public static VideoListModel getInstance() {
        if (videoListModel == null) {
            videoListModel = new VideoListModel();
        }
        return videoListModel;
    }

    ArrayList<YoutubeVideoBean> youtubeListItems;

    protected VideoListModel(Parcel in) {
    }

    public static final Creator<VideoListModel> CREATOR = new Creator<VideoListModel>() {
        @Override
        public VideoListModel createFromParcel(Parcel in) {
            return new VideoListModel(in);
        }

        @Override
        public VideoListModel[] newArray(int size) {
            return new VideoListModel[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public ArrayList<YoutubeVideoBean> getYoutubeListItems() {
        return youtubeListItems;
    }

    public void setYoutubeListItems(ArrayList<YoutubeVideoBean> youtubeListItems) {
        this.youtubeListItems = youtubeListItems;
    }
}
