package com.ojassoft.astrosage.varta.model;
public class UPIAppModel {
    private final String name;
    private final int iconResId;
    private final String packageName;
    private final String payMethodName;

    public UPIAppModel(String name, int iconResId, String packageName, String payMethodName) {
        this.name = name;
        this.iconResId = iconResId;
        this.packageName = packageName;
        this.payMethodName = payMethodName;
    }

    public String getName() { return name; }
    public int getIconResId() { return iconResId; }
    public String getPackageName() { return packageName; }
    public String getPayMethodName() { return payMethodName; }
}

