package com.ojassoft.astrosage.model;

public class DownloadSourceModel {
    private int sourceId;
    private String sourceNam;
    private int sourceIcon;
    private String comment;

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    private boolean isSelected = false;
    public void setSourceId(int sourceId) {
        this.sourceId = sourceId;
    }

    public void setSourceNam(String sourceNam) {
        this.sourceNam = sourceNam;
    }

    public void setSourceIcon(int sourceIcon) {
        this.sourceIcon = sourceIcon;
    }

    public int getSourceId() {
        return sourceId;
    }

    public String getSourceNam() {
        return sourceNam;
    }

    public int getSourceIcon() {
        return sourceIcon;
    }

    public DownloadSourceModel(int id, int icon, String sourceName, String comment){
        this.sourceId = id;
        this.sourceIcon = icon;
        this.sourceNam = sourceName;
        this.comment = comment;
    }

    public String getComment() {
        return comment;
    }
}
