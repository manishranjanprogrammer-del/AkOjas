
package com.ojassoft.astrosage.model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class AstrologerInfo implements Serializable{

    @SerializedName("AstrologerID")
    @Expose
    private Integer astrologerID;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("ImageURL")
    @Expose
    private String imageURL;

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    @SerializedName("Description")
    @Expose

    private String description;
    @SerializedName("FocusAreas")
    @Expose
    private String focusAreas;
    @SerializedName("Experience")
    @Expose
    private String experience;

    @SerializedName("UrlText")
    @Expose
    private String getAstrologerDeepLinkURL;
    /**
     * 
     * @return
     *     The astrologerID
     */
    public Integer getAstrologerID() {
        return astrologerID;
    }

    /**
     * 
     * @param astrologerID
     *     The AstrologerID
     */
    public void setAstrologerID(Integer astrologerID) {
        this.astrologerID = astrologerID;
    }

    /**
     * 
     * @return
     *     The name
     */
    public String getName() {
        return name;
    }

    /**
     * 
     * @param name
     *     The Name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 
     * @return
     *     The imageURL
     */
    public String getImageURL() {
        return imageURL;
    }

    /**
     * 
     * @param imageURL
     *     The ImageURL
     */
    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    /**
     * 
     * @return
     *     The description
     */
    public String getDescription() {
        return description;
    }

    /**
     * 
     * @param description
     *     The Description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 
     * @return
     *     The focusAreas
     */
    public String getFocusAreas() {
        return focusAreas;
    }

    /**
     * 
     * @param focusAreas
     *     The FocusAreas
     */
    public void setFocusAreas(String focusAreas) {
        this.focusAreas = focusAreas;
    }


    public String getGetAstrologerDeepLinkURL() {
        return getAstrologerDeepLinkURL;
    }

    public void setGetAstrologerDeepLinkURL(String getAstrologerDeepLinkURL) {
        this.getAstrologerDeepLinkURL = getAstrologerDeepLinkURL;
    }
}
