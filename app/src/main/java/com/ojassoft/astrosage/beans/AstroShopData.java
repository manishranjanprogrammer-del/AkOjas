package com.ojassoft.astrosage.beans;

import android.graphics.drawable.Drawable;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

/**
 * Created by ojas on १/३/१६.
 */
public class AstroShopData implements Serializable {
    @SerializedName("imageUrl")
    public String imageUrl;
    @SerializedName("itemName")
    public String itemName;
    @SerializedName("itemDes")
    public String itemDes;
    @SerializedName("itemCost")
    public String itemCost;

    public String getImageUrl() {
        return imageUrl;
    }

    public String getItemName() {
        return itemName;
    }

    public void setItemName(String itemName) {
        this.itemName = itemName;
    }

    public String getItemDes() {
        return itemDes;
    }

    public void setItemDes(String itemDes) {
        this.itemDes = itemDes;
    }

    public String getItemCost() {
        return itemCost;
    }

    public void setItemCost(String itemCost) {
        this.itemCost = itemCost;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;

    }


}
