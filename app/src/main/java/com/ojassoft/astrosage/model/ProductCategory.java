package com.ojassoft.astrosage.model;

public class ProductCategory {
    private String categoryFullName, categoryShortName, categorySmallDescription, categoryUrl, categoryImagePath, resourceImage;

    public ProductCategory(String categoryFullName, String categoryShortName, String categorySmallDescription, String categoryUrl, String categoryImagePath, String resourceImage) {
        this.categoryFullName = categoryFullName;
        this.categoryShortName = categoryShortName;
        this.categorySmallDescription = categorySmallDescription;
        this.categoryUrl = categoryUrl;
        this.categoryImagePath = categoryImagePath;
        this.resourceImage = resourceImage;
    }

    public String getCategoryFullName() { return categoryFullName; }
    public String getCategoryShortName() { return categoryShortName; }
    public String getCategoryImagePath() { return categoryImagePath; }
    public String getResourceImage() { return resourceImage; }
    public String getCategoryUrl() { return categoryUrl; }
}
