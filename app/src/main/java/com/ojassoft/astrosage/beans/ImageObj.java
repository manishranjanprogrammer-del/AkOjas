
package com.ojassoft.astrosage.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ImageObj {

    @SerializedName("imgurl")
    @Expose
    private String imgurl;
    @SerializedName("deepLinkUrl")
    @Expose
    private String deepLinkUrl;

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getDeepLinkUrl() {
        return deepLinkUrl;
    }

    public void setDeepLinkUrl(String deepLinkUrl) {
        this.deepLinkUrl = deepLinkUrl;
    }

}
