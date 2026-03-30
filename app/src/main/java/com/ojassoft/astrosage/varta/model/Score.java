package com.ojassoft.astrosage.varta.model;

import com.google.gson.annotations.SerializedName;

public  class Score {
    @SerializedName("label")
    private String label;

    // value in your example is numeric; using Integer to be safe.
    // If sometimes it's string, change to String or Object.
    @SerializedName("value")
    private Integer value;

    @SerializedName("key")
    private String key;

    public String getLabel() { return label; }
    public void setLabel(String label) { this.label = label; }

    public Integer getValue() { return value; }
    public void setValue(Integer value) { this.value = value; }

    public String getKey() { return key; }
    public void setKey(String key) { this.key = key; }
}
