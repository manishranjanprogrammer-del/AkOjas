package com.ojassoft.astrosage.varta.model;

public class ScoreItem {
    private String label;
    private int value;

    public ScoreItem(String label, int value) {
        this.label = label;
        this.value = value;
    }

    public String getLabel() { return label; }
    public int getValue() { return value; }
}
