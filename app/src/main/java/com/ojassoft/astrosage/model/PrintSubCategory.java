package com.ojassoft.astrosage.model;

import java.io.Serializable;
import java.util.ArrayList;

public class PrintSubCategory implements Serializable {
    String catName;
    boolean isAllSubCatSelected;
    ArrayList<SubCatDetail> subCatDetails;

    public String getCatName() {
        return catName;
    }

    public void setCatName(String catName) {
        this.catName = catName;
    }

    public boolean isAllSubCatSelected() {
        return isAllSubCatSelected;
    }

    public void setAllSubCatSelected(boolean allSubCatSelected) {
        isAllSubCatSelected = allSubCatSelected;
    }

    public ArrayList<SubCatDetail> getSubCatDetails() {
        return subCatDetails;
    }

    public void setSubCatDetails(ArrayList<SubCatDetail> subCatDetails) {
        this.subCatDetails = subCatDetails;
    }

    public class SubCatDetail implements Serializable{
        String suCatName;
        boolean isCatSelected;
        String indexOfModule;
        String valueOfModule;

        public String getSuCatName() {
            return suCatName;
        }

        public void setSuCatName(String suCatName) {
            this.suCatName = suCatName;
        }

        public boolean isCatSelected() {
            return isCatSelected;
        }

        public void setCatSelected(boolean catSelected) {
            isCatSelected = catSelected;
        }

        public String getIndexOfModule() {
            return indexOfModule;
        }

        public void setIndexOfModule(String indexOfModule) {
            this.indexOfModule = indexOfModule;
        }

        public String getValueOfModule() {
            return valueOfModule;
        }

        public void setValueOfModule(String valueOfModule) {
            this.valueOfModule = valueOfModule;
        }
    }

}