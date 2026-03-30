package com.ojassoft.astrosage.varta.model;

import android.os.Parcel;
import android.os.Parcelable;

public class NotificationModel implements Parcelable {

    private int id;
    private String message;
    private String title;
    private String link;
    private String ntId;
    private String extra;
    private String imgUrl;
    private String blogId;
    private int notificationType;
    private String timestamp;
    private String isDropDownShow="false";

    public NotificationModel() {

    }

    protected NotificationModel(Parcel in) {
        id = in.readInt();
        message = in.readString();
        title = in.readString();
        link = in.readString();
        ntId = in.readString();
        extra = in.readString();
        imgUrl = in.readString();
        blogId = in.readString();
        notificationType = in.readInt();
        timestamp = in.readString();
        isDropDownShow = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(message);
        dest.writeString(title);
        dest.writeString(link);
        dest.writeString(ntId);
        dest.writeString(extra);
        dest.writeString(imgUrl);
        dest.writeString(blogId);
        dest.writeInt(notificationType);
        dest.writeString(timestamp);
        dest.writeString(isDropDownShow);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<NotificationModel> CREATOR = new Creator<NotificationModel>() {
        @Override
        public NotificationModel createFromParcel(Parcel in) {
            return new NotificationModel(in);
        }

        @Override
        public NotificationModel[] newArray(int size) {
            return new NotificationModel[size];
        }
    };

    public String isDropDownShow() {
        return isDropDownShow;
    }

    public void setDropDownShow(String dropDownShow) {
        isDropDownShow = dropDownShow;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getNtId() {
        return ntId;
    }

    public void setNtId(String ntId) {
        this.ntId = ntId;
    }

    public String getExtra() {
        return extra;
    }

    public void setExtra(String extra) {
        this.extra = extra;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public String getBlogId() {
        return blogId;
    }

    public void setBlogId(String blogId) {
        this.blogId = blogId;
    }

    public int getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(int notificationType) {
        this.notificationType = notificationType;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}

