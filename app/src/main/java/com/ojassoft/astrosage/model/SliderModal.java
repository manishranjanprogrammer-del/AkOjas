package com.ojassoft.astrosage.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ojas on २१/७/१६.
 */
public class SliderModal implements Serializable {


    /*
    * "Videoid": "1",
	"Title": "Graha",
	"Description": "Want to learn astrology very very quickly? No clue how to learn astrology and where to learn astrology? No worries, astrologer Punit Pandey has prepared '2 minute astrology tutorial videos' for you. AstroSage TV makes you learn astrology in the fastest possible way on planet earth :) Time is  changing, we want everything in quick, short and to the point manner. Keeping the need of 21st century people in mind, we have developed two minute tutorials. These Vedic Astrology tutorials are in Hindi.",
	"VideoURL": "kUmbltG9_YA",
	"ImageURL": "",
	"Isfeatured": "1",
	"CategoryName": "2 Minute Astrology Tutorials (2 मिनट का ज्‍योतिष कोर्स)"
    *
    * */
    @SerializedName("Videoid")
    private String Videoid;

    @SerializedName("Description")
    private String Description;

    @SerializedName("ImageURL")
    private String url;

    @SerializedName("Title")
    private String title;

   // @SerializedName("Description")
    private String subTitle;

    @SerializedName("VideoURL")
    private String video_url;

    public String getThumbnailImageURL() {
        return ThumbnailImageURL;
    }

    public void setThumbnailImageURL(String thumbnailImageURL) {
        ThumbnailImageURL = thumbnailImageURL;
    }

    @SerializedName("Isfeatured")
    private String Isfeatured;

    @SerializedName("CategoryName")
    private String CategoryName;


    @SerializedName("ThumbnailImageURL")
    private String ThumbnailImageURL;




    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    private int time=0;



    public String getVideoid() {
        return Videoid;
    }

    public void setVideoid(String videoid) {
        Videoid = videoid;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getIsfeatured() {
        return Isfeatured;
    }

    public void setIsfeatured(String isfeatured) {
        Isfeatured = isfeatured;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public void setSubTitle(String subTitle) {
        this.subTitle = subTitle;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
