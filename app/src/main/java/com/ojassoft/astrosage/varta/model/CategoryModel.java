package com.ojassoft.astrosage.varta.model;

public class CategoryModel {
    private String name;
    private String code;
    private String id;
    private Boolean isSelected = false;

    public CategoryModel(String name, String code, String id, Boolean isSelected) {
        this.name = name;
        this.code = code;
        this.id = id;
        this.isSelected = isSelected;
    }

    public String getName() { return name; }
    public void setName(String value) { this.name = value; }

    public String getCode() { return code; }
    public void setCode(String value) { this.code = value; }

    public String getID() { return id; }
    public void setID(String value) { this.id = value; }

    public Boolean getSelected() {
        return isSelected;
    }

    public void setSelected(Boolean selected) {
        isSelected = selected;
    }
}
