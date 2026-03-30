package com.ojassoft.astrosage.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ojas on २८/२/१७.
 */
public class CustomAddModel implements Serializable {



    @SerializedName("imgurl")
    private String Imgurl;

    public String getImgthumbnailurl() {
        return Imgthumbnailurl;
    }

    public void setImgthumbnailurl(String imgthumbnailurl) {
        Imgthumbnailurl = imgthumbnailurl;
    }


    public String getImgurl() {
        return Imgurl;
    }

    public void setImgurl(String imgurl) {
        Imgurl = imgurl;
    }

    @SerializedName("deepLinkUrl")
    private String Imgthumbnailurl;


}
