package com.ojassoft.astrosage.varta.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Comparator;

public class AstrologerDetailBean implements Serializable,Comparable {

    String name = "";
    String experience = "";
    String language = "";
    String designation = "";
    String imageFile = "";
    String description = "";
    String servicePrice = "";
    int servicePriceInt;
    String rating = "";
    double ratingDoule;
    String totalRating = "";
    double totalRatingDouble;
    String ratingNote = "";
    String email = "";
    String urlText = "";
    String phoneNumber = "";
    String isOnline = "false";
    boolean isOnlineBool;
    String isBusy = "false";
    boolean isBusyBool;
    String doubleRating = "";
    public double getDoubleRating;
    String halfStarRating = "";
    String astroWalletId = "";
    String expertise = "";
    String primaryexpertise = "";
    String enablefeedbacks = "";
    String astrologerId = "";
    String aiAstrologerId = "";
    String isAvailableForChat = "";
    boolean isAvailableForChatBool;
    String isAvailableForCall = "";
    boolean isAvailableForCallBool;
    String isAvailableForVideoCall = "";
    boolean isAvailableForVideoCallBool;
    String actualServicePriceInt ;
    int actualServicePriceInteger ;
    String isVerified = "";
    boolean isVerifiedBool;
    String education="";
    String focusareas="";
    String city="";
    String state="";
    String country="";
    String availableTimeForCall="";
    String background="";
    String career="";
    String hobbies="";
    String achievements="";
    String abilities="";
    String imageFileLarge="";
    String waitTimeRemaining="";
    String waitTime = "";
    String catId = "";
    boolean useIntroOffer;
    String introOfferType;
    boolean isFreeForCall;
    boolean isFreeForChat;
    boolean astroSingleTimeOffer;
    String isastrologerlive;
    String currentFreeConsultation;
    ArrayList<UserReviewBean> userReviewBeanArrayList = new ArrayList<>();
    UserReviewBean userOwnReviewModel = new UserReviewBean();
    boolean astrologerBookmarked=false;
    int followCount;
    int manipulatedRank;
    boolean isOfferRemaining;
    boolean isFeatured;

    String callSource;

    int acceptInternetCall;
    String profileTitle;

    public String getCurrentFreeConsultation() {
        return currentFreeConsultation;
    }

    public void setCurrentFreeConsultation(String currentFreeConsultation) {
        this.currentFreeConsultation = currentFreeConsultation;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    private String rank;


    public String getCatId() {
        return catId;
    }

    public void setCatId(String catId) {
        this.catId = catId;
    }

    public String getCallSource() {
        return callSource;
    }

    public void setCallSource(String callSource) {
        this.callSource = callSource;
    }

    public boolean isAstrologerBookmarked() {
        return astrologerBookmarked;
    }

    public void setAstrologerBookmarked(boolean astrologerBookmarked) {
        this.astrologerBookmarked = astrologerBookmarked;
    }

    public String getWaitTime() {
        return waitTime;
    }

    public void setWaitTime(String waitTime) {
        this.waitTime = waitTime;
    }

    public String getWaitTimeRemaining() {
        return waitTimeRemaining;
    }

    public void setWaitTimeRemaining(String waitTimeRemaining) {
        this.waitTimeRemaining = waitTimeRemaining;
    }

    public String getIsVerified() {
        return isVerified;
    }

    public void setIsVerified(String isVerified) {
        try {
            isVerifiedBool = Boolean.parseBoolean(isVerified);
        }catch (Exception ignore){}
        this.isVerified = isVerified;
    }

    public String getActualServicePriceInt() {
        return actualServicePriceInt;
    }

    public int getActualServicePriceInteger() {
        return actualServicePriceInteger;
    }

    public void setActualServicePriceInt(String actualServicePriceInt) {
        try {
            this.actualServicePriceInteger = Integer.parseInt(actualServicePriceInt);
        }catch (Exception ignore){}
        this.actualServicePriceInt = actualServicePriceInt;
    }


    public String getIsAvailableForCall() {
        return isAvailableForCall;
    }

    public void setIsAvailableForCall(String isAvailableForCall) {
        try {
            isAvailableForCallBool = Boolean.parseBoolean(isAvailableForCall);
        }catch (Exception ignore){}
        this.isAvailableForCall = isAvailableForCall;
    }

    public String getIsAvailableForVideoCall() {
        return isAvailableForVideoCall;
    }

    public void setIsAvailableForVideoCall(String isAvailableForVideoCall) {
        try {
            isAvailableForVideoCallBool = Boolean.parseBoolean(isAvailableForVideoCall);
        }catch (Exception ignore){}
        this.isAvailableForVideoCall = isAvailableForVideoCall;
    }

    public String getIsAvailableForChat() {
        return isAvailableForChat;
    }

    public void setIsAvailableForChat(String isAvailableForChat) {
        try {
            isAvailableForChatBool = Boolean.parseBoolean(isAvailableForChat);
        }catch (Exception ignore){}
        this.isAvailableForChat = isAvailableForChat;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getFocusareas() {
        return focusareas;
    }

    public void setFocusareas(String focusareas) {
        this.focusareas = focusareas;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
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

    public String getAvailableTimeForCall() {
        return availableTimeForCall;
    }

    public void setAvailableTimeForCall(String availableTimeForCall) {
        this.availableTimeForCall = availableTimeForCall;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getCareer() {
        return career;
    }

    public void setCareer(String career) {
        this.career = career;
    }

    public String getHobbies() {
        return hobbies;
    }

    public void setHobbies(String hobbies) {
        this.hobbies = hobbies;
    }

    public String getAchievements() {
        return achievements;
    }

    public void setAchievements(String achievements) {
        this.achievements = achievements;
    }

    public String getAbilities() {
        return abilities;
    }

    public void setAbilities(String abilities) {
        this.abilities = abilities;
    }

    public String getImageFileLarge() {
        return imageFileLarge;
    }

    public void setImageFileLarge(String imageFileLarge) {
        this.imageFileLarge = imageFileLarge;
    }

    public UserReviewBean getUserOwnReviewModel() {
        return userOwnReviewModel;
    }

    public void setUserOwnReviewModel(UserReviewBean userOwnReviewModel) {
        this.userOwnReviewModel = userOwnReviewModel;
    }

    public String getAstrologerId() {
        return astrologerId;
    }

    public void setAstrologerId(String astrologerId) {
        this.astrologerId = astrologerId;
    }

    public String getEnablefeedbacks() {
        return enablefeedbacks;
    }

    public void setEnablefeedbacks(String enablefeedbacks) {
        this.enablefeedbacks = enablefeedbacks;
    }

    public ArrayList<UserReviewBean> getUserReviewBeanArrayList() {
        return userReviewBeanArrayList;
    }

    public void setUserReviewBeanArrayList(ArrayList<UserReviewBean> userReviewBeanArrayList) {
        this.userReviewBeanArrayList = userReviewBeanArrayList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getServicePrice() {
        return servicePrice;
    }

    public void setServicePrice(String servicePrice) {
        try {
            servicePriceInt = Integer.parseInt(servicePrice);
        }catch (Exception ignore){}
        this.servicePrice = servicePrice;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        try {
            ratingDoule = Double.parseDouble(rating);
        }catch (Exception ignore){}
        this.rating = rating;
    }

    public String getTotalRating() {
        return totalRating;
    }

    public void setTotalRating(String totalRating) {
        try {
            totalRatingDouble = Double.parseDouble(rating);
        }catch (Exception ignore){}
        this.totalRating = totalRating;
    }

    public String getRatingNote() {
        return ratingNote;
    }

    public void setRatingNote(String ratingNote) {
        this.ratingNote = ratingNote;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrlText() {
        return urlText;
    }

    public void setUrlText(String urlText) {
        this.urlText = urlText;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getIsOnline() {
        return isOnline;
    }

    public void setIsOnline(String isOnline) {
        try {
            isOnlineBool = Boolean.parseBoolean(isOnline);
        }catch (Exception ignore){}
        this.isOnline = isOnline;
    }

    public String getIsBusy() {
        return isBusy;
    }

    public void setIsBusy(String isBusy) {
        try {
            isBusyBool = Boolean.parseBoolean(isBusy);
        }catch (Exception ignore){}
        this.isBusy = isBusy;
    }

    public String getDoubleRating() {
        return doubleRating;
    }

    public void setDoubleRating(String doubleRating) {
        try {
            getDoubleRating = Double.parseDouble(doubleRating);
        }catch (Exception ignore){}
        this.doubleRating = doubleRating;
    }

    public String getHalfStarRating() {
        return halfStarRating;
    }

    public void setHalfStarRating(String halfStarRating) {
        this.halfStarRating = halfStarRating;
    }

    public String getAstroWalletId() {
        return astroWalletId;
    }

    public void setAstroWalletId(String astroWalletId) {
        this.astroWalletId = astroWalletId;
    }

    public String getExpertise() {
        return expertise;
    }

    public void setExpertise(String expertise) {
        this.expertise = expertise;
    }
    public String getProfileTitle() {
        return profileTitle;
    }

    public void setProfileTitle(String profileTitle) {
        this.profileTitle = profileTitle;
    }
    public void setPrimaryExpertise(String expertise) {
        this.primaryexpertise = expertise;
    }

    public String getPrimaryExpertise() {
        return primaryexpertise;
    }

    public boolean getUseIntroOffer() {
        return useIntroOffer;
    }

    public void setUseIntroOffer(boolean useIntroOffer) {
        this.useIntroOffer = useIntroOffer;
    }

    public String getIntroOfferType() {
        return introOfferType;
    }

    public void setIntroOfferType(String introOfferType) {
        this.introOfferType = introOfferType;
    }

    public String getIsastrologerlive() {
        return isastrologerlive;
    }

    public void setIsastrologerlive(String isastrologerlive) {
        this.isastrologerlive = isastrologerlive;
    }

    public int getFollowCount() {
        return followCount;
    }

    public void setFollowCount(int followCount) {
        this.followCount = followCount;
    }

    public int getManipulatedRank() {
        return manipulatedRank;
    }

    public void setManipulatedRank(int manipulatedRank) {
        this.manipulatedRank = manipulatedRank;
    }

    public boolean isOfferRemaining() {
        return isOfferRemaining;
    }

    public void setOfferRemaining(boolean offerRemaining) {
        isOfferRemaining = offerRemaining;
    }

    public boolean isFreeForCall() {
        return isFreeForCall;
    }

    public void setFreeForCall(boolean freeForCall) {
        isFreeForCall = freeForCall;
    }

    public boolean isFreeForChat() {
        return isFreeForChat;
    }

    public void setFreeForChat(boolean freeForChat) {
        isFreeForChat = freeForChat;
    }

    public boolean isFeatured() {
        return isFeatured;
    }

    public void setFeatured(boolean featured) {
        isFeatured = featured;
    }

    public int getAcceptInternetCall() {
        return acceptInternetCall;
    }

    public void setAcceptInternetCall(int acceptInternetCall) {
        this.acceptInternetCall = acceptInternetCall;
    }

    public String getAiAstrologerId() {
        return aiAstrologerId;
    }

    public void setAiAstrologerId(String aiAstrologerId) {
        this.aiAstrologerId = aiAstrologerId;
    }
    public double getRatingDoule() {
        return ratingDoule;
    }

    public double getTotalRatingDouble() {
        return totalRatingDouble;
    }

    public boolean isOnlineBool() {
        return isOnlineBool;
    }

    public boolean isBusyBool() {
        return isBusyBool;
    }

    public boolean isAvailableForChatBool() {
        return isAvailableForChatBool;
    }

    public boolean isAvailableForCallBool() {
        return isAvailableForCallBool;
    }

    public boolean isAvailableForVideoCallBool() {
        return isAvailableForVideoCallBool;
    }

    public boolean isVerifiedBool() {
        return isVerifiedBool;
    }
    public int servicePriceInt(){
        return servicePriceInt;
    }
    public boolean isAstroSingleTimeOffer() {
        return astroSingleTimeOffer;
    }

    public void setAstroSingleTimeOffer(boolean astroSingleTimeOffer) {
        this.astroSingleTimeOffer = astroSingleTimeOffer;
    }
    public static Comparator<AstrologerDetailBean> servicePriceCamparatorAssending = new Comparator<AstrologerDetailBean>() {

        public int compare(AstrologerDetailBean s1, AstrologerDetailBean s2) {

            int servicePrice1 = Integer.parseInt(s1.getServicePrice());
            int servicePrice2 = Integer.parseInt(s2.getServicePrice());

            /*For ascending order*/
            return servicePrice1 - servicePrice2;

            /*For descending order*/
            //rollno2-rollno1;
        }
    };
    public static Comparator<AstrologerDetailBean> servicePriceCamparatorDesending = new Comparator<AstrologerDetailBean>() {

        public int compare(AstrologerDetailBean s1, AstrologerDetailBean s2) {

            int servicePrice1 = Integer.parseInt(s1.getServicePrice());
            int servicePrice2 = Integer.parseInt(s2.getServicePrice());

            /*For ascending order*/
            return servicePrice2 - servicePrice1;

            /*For descending order*/
            //rollno2-rollno1;
        }
    };

    public static Comparator<AstrologerDetailBean> serviceRatingCamparatorAssending = new Comparator<AstrologerDetailBean>() {

        public int compare(AstrologerDetailBean s1, AstrologerDetailBean s2) {

            int serviceRank1 = s1.getManipulatedRank();
            int serviceRank2 = s2.getManipulatedRank();

            /*For ascending order*/
            return serviceRank1 - serviceRank2;

            //return Double.compare(Double.parseDouble(s1.getDoubleRating()), Double.parseDouble(s2.getDoubleRating()));
        }
    };

    public static Comparator<AstrologerDetailBean> serviceRatingCamparatorDesending = new Comparator<AstrologerDetailBean>() {

        public int compare(AstrologerDetailBean s1, AstrologerDetailBean s2) {

            int serviceRank1 = s1.getManipulatedRank();
            int serviceRank2 = s2.getManipulatedRank();

            /*For descending order*/
            return serviceRank2 - serviceRank1;
            //return Double.compare(Double.parseDouble(s2.getDoubleRating()), Double.parseDouble(s1.getDoubleRating()));
        }
    };

    public static Comparator<AstrologerDetailBean> followerCamparatorAssending = new Comparator<AstrologerDetailBean>() {

        public int compare(AstrologerDetailBean s1, AstrologerDetailBean s2) {

            int followCount1 = s1.getFollowCount();
            int followCount2 = s2.getFollowCount();

            /*For ascending order*/
           return followCount1 - followCount2;
        }
    };

    public static Comparator<AstrologerDetailBean> followerCamparatorDesending = new Comparator<AstrologerDetailBean>() {

        public int compare(AstrologerDetailBean s1, AstrologerDetailBean s2) {

            int followCount1 = s1.getFollowCount();
            int followCount2 = s2.getFollowCount();

            /*For descending order*/
            return followCount2 - followCount1;
        }
    };

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
