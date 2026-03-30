package com.ojassoft.astrosage.model;

import java.io.Serializable;
import java.util.ArrayList;

public class PrinCategory implements Serializable {
    //String categoryName;
    ArrayList<PrintSubCategory> subCategories;
    boolean isSelectedAll;

    /*public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }*/

    public ArrayList<PrintSubCategory> getSubCategories() {
        return subCategories;
    }

    public void setSubCategories(ArrayList<PrintSubCategory> subCategories) {
        this.subCategories = subCategories;
    }

    public boolean isSelectedAll() {
        return isSelectedAll;
    }

    public void setSelectedAll(boolean selectedAll) {
        isSelectedAll = selectedAll;
    }


}
