package com.ojassoft.astrosage.model;

public class PrintModuleData {
    private int indexOfCategory;
    private int indexOfSubcategory;


    public PrintModuleData(int indexOfCategory, int indexOfSubcategory) {
        this.indexOfCategory = indexOfCategory;
        this.indexOfSubcategory = indexOfSubcategory;
    }

    public int getIndexOfCategory() {
        return indexOfCategory;
    }

    public int getIndexOfSubcategory() {
        return indexOfSubcategory;
    }
}
