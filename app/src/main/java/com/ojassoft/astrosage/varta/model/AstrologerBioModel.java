package com.ojassoft.astrosage.varta.model;

public class AstrologerBioModel {

    String astrologerBioName="", astrologerBioDescription="";
    int imageFile;

    public int getImageFile() {
        return imageFile;
    }

    public void setImageFile(int imageFile) {
        this.imageFile = imageFile;
    }

    public String getAstrologerBioName() {
        return astrologerBioName;
    }

    public void setAstrologerBioName(String astrologerBioName) {
        this.astrologerBioName = astrologerBioName;
    }

    public String getAstrologerBioDescription() {
        return astrologerBioDescription;
    }

    public void setAstrologerBioDescription(String astrologerBioDescription) {
        this.astrologerBioDescription = astrologerBioDescription;
    }
}
