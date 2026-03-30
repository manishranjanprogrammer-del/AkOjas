package com.ojassoft.astrosage.model;

import com.google.gson.annotations.SerializedName;

public class ArticleModel {
    @SerializedName("creator")
    private String creator;

    @SerializedName("link")
    private String link;

    @SerializedName("title")
    private String title;

    @SerializedName("pubDate")
    private String pubDate;

    @SerializedName("thumbnailUrl")
    private String thumbnailUrl;

    public ArticleModel(String creator, String link, String title, String pubDate, String thumbnailUrl) {
        this.creator = creator;
        this.link = link;
        this.title = title;
        this.pubDate = pubDate;
        this.thumbnailUrl = thumbnailUrl;
    }

    public ArticleModel() {
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    public String getThumbnailUrl() {
        return thumbnailUrl;
    }

    public void setThumbnailUrl(String thumbnailUrl) {
        this.thumbnailUrl = thumbnailUrl;
    }

    @Override
    public String toString() {
        return "ArticleModel{" +
                "creator='" + creator + '\'' +
                ", link='" + link + '\'' +
                ", title='" + title + '\'' +
                ", pubDate='" + pubDate + '\'' +
                ", thumbnailUrl='" + thumbnailUrl + '\'' +
                '}';
    }
}

