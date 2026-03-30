package com.ojassoft.astrosage.beans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class DhruvAstrologerListModel implements Serializable {


    @SerializedName("ProfileId")
    @Expose
    private String profileId;
    @SerializedName("AverageRating")
    @Expose
    private String averageRating;
    @SerializedName("TotalRating")
    @Expose
    private String totalRating;
    @SerializedName("FeaturedAstrologer")
    @Expose
    private String featuredAstrologer;
    @SerializedName("EmailDisplay")
    @Expose
    private String emailDisplay;
    @SerializedName("Email")
    @Expose
    private String email;
    @SerializedName("AlternateEmail")
    @Expose
    private String alternateEmail;
    @SerializedName("Name")
    @Expose
    private String name;
    @SerializedName("AddressDisplay")
    @Expose
    private String addressDisplay;
    @SerializedName("Address1")
    @Expose
    private String address1;
    @SerializedName("Address2")
    @Expose
    private String address2;
    @SerializedName("Address3")
    @Expose
    private String address3;
    @SerializedName("City")
    @Expose
    private String city;
    @SerializedName("PinCode")
    @Expose
    private String pinCode;
    @SerializedName("State")
    @Expose
    private String state;
    @SerializedName("Country")
    @Expose
    private String country;
    @SerializedName("PhoneDisplay")
    @Expose
    private String phoneDisplay;
    @SerializedName("Mobile")
    @Expose
    private String mobile;
    @SerializedName("PhoneRes")
    @Expose
    private String phoneRes;
    @SerializedName("PhoneOff")
    @Expose
    private String phoneOff;
    @SerializedName("Title")
    @Expose
    private String title;
    @SerializedName("Description")
    @Expose
    private String description;
    @SerializedName("ImageFile")
    @Expose
    private String imageFile;
    @SerializedName("Priority")
    @Expose
    private String priority;

    public String getProfileId() {
        return profileId;
    }

    public void setProfileId(String profileId) {
        this.profileId = profileId;
    }

    public String getFeaturedAstrologer() {
        return featuredAstrologer;
    }

    public void setFeaturedAstrologer(String featuredAstrologer) {
        this.featuredAstrologer = featuredAstrologer;
    }

    public String getAverageRating() {
        return averageRating;
    }

    public void setAverageRating(String averageRating) {
        this.averageRating = averageRating;
    }

    public String getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(String totalRating) {
        this.totalRating = totalRating;
    }

    public String getEmailDisplay() {
        return emailDisplay;
    }

    public void setEmailDisplay(String emailDisplay) {
        this.emailDisplay = emailDisplay;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAlternateEmail() {
        return alternateEmail;
    }

    public void setAlternateEmail(String alternateEmail) {
        this.alternateEmail = alternateEmail;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddressDisplay() {
        return addressDisplay;
    }

    public void setAddressDisplay(String addressDisplay) {
        this.addressDisplay = addressDisplay;
    }

    public String getAddress1() {
        return address1;
    }

    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    public String getAddress2() {
        return address2;
    }

    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    public String getAddress3() {
        return address3;
    }

    public void setAddress3(String address3) {
        this.address3 = address3;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPhoneDisplay() {
        return phoneDisplay;
    }

    public void setPhoneDisplay(String phoneDisplay) {
        this.phoneDisplay = phoneDisplay;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPhoneRes() {
        return phoneRes;
    }

    public void setPhoneRes(String phoneRes) {
        this.phoneRes = phoneRes;
    }

    public String getPhoneOff() {
        return phoneOff;
    }

    public void setPhoneOff(String phoneOff) {
        this.phoneOff = phoneOff;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
