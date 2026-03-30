package com.ojassoft.astrosage.beans;

import androidx.annotation.IntegerRes;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ojas on १५/३/१६.
 */
public class AstroShopItemDetails implements Serializable {



    @SerializedName("MessageOfCloudPlan1")
    @Expose
    private String messageOfCloudPlan1;

    public String getMessageOfCloudPlan1() {
        return messageOfCloudPlan1;
    }

    public void setMessageOfCloudPlan1(String messageOfCloudPlan1) {
        this.messageOfCloudPlan1 = messageOfCloudPlan1;
    }

    public String getMessageOfCloudPlan2() {
        return messageOfCloudPlan2;
    }

    public void setMessageOfCloudPlan2(String messageOfCloudPlan2) {
        this.messageOfCloudPlan2 = messageOfCloudPlan2;
    }

    @SerializedName("MessageOfCloudPlan2")
    @Expose
    private String messageOfCloudPlan2;


    public String getP_ParentId() {
        return P_ParentId;
    }

    public void setP_ParentId(String p_ParentId) {
        P_ParentId = p_ParentId;
    }

    @SerializedName("P_ParentId")
    @Expose
    private String P_ParentId;

    public String getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(String isDefault) {
        this.isDefault = isDefault;
    }

    @SerializedName("IsDefault")
    @Expose
    private String isDefault="true";

    @SerializedName("P_VirtualName")
    @Expose
    private String P_VirtualName;

    public String getP_VirtualName() {
        return P_VirtualName;
    }

    public void setP_VirtualName(String p_VirtualName) {
        P_VirtualName = p_VirtualName;
    }

    public String getHaschild() {
        return haschild;
    }

    public void setHaschild(String haschild) {
        this.haschild = haschild;
    }

    @SerializedName("haschild")

    @Expose
    private String haschild;


    @SerializedName("P_Id")
    @Expose
    private String PId;
    @SerializedName("P_Name")
    @Expose
    private String PName;
    @SerializedName("P_PriceInDoller")
    @Expose
    private String PPriceInDoller;
    @SerializedName("P_PriceInRs")
    @Expose
    private String PPriceInRs;
    @SerializedName("P_SmallDesc")
    @Expose
    private String PSmallDesc;
    @SerializedName("P_FullDesc")
    @Expose
    private String PFullDesc;
    @SerializedName("P_ImgUrl")
    @Expose
    private String PImgUrl;
    @SerializedName("P_LargeImgUrl")
    @Expose
    private String PLargeImgUrl;
    @SerializedName("P_OutOfStock")
    @Expose
    private String P_OutOfStock;

    public String getpDeepLinkUrl() {
        return pDeepLinkUrl;
    }

    public void setpDeepLinkUrl(String pDeepLinkUrl) {
        this.pDeepLinkUrl = pDeepLinkUrl;
    }

    @SerializedName("P_url_text")
    @Expose
    private String P_url_text;


    @SerializedName("P_Deep_Link_Url")
    @Expose
    private String pDeepLinkUrl;

    public String getO_Id() {
        return O_Id;
    }

    public void setO_Id(String o_Id) {
        O_Id = o_Id;
    }

    @SerializedName("O_Id")

    @Expose
    private String O_Id;

    private String kdetail;

    public String getP_Status() {
        return P_Status;
    }

    public void setP_Status(String p_Status) {
        P_Status = p_Status;
    }

    public String getD_Date() {
        return D_Date;
    }

    public void setD_Date(String d_Date) {
        D_Date = d_Date;
    }

    public String getO_Date() {
        return O_Date;
    }

    public void setO_Date(String o_Date) {
        O_Date = o_Date;
    }

    @SerializedName("P_Status")
    @Expose
    private String P_Status;
    @SerializedName("D_Date")
    @Expose
    private String D_Date;
    @SerializedName("O_Date")
    @Expose
    private String O_Date;
    @SerializedName("P_OriginalPriceInDollar")
    @Expose
    private String P_OriginalPriceInDollar;
    @SerializedName("P_OriginalPriceInRs")
    @Expose
    private String P_OriginalPriceInRs;

    public String getFilterKey() {
        return filterKey;
    }

    public void setFilterKey(String filterKey) {
        this.filterKey = filterKey;
    }

    @SerializedName("value")
    @Expose
    private String filterKey;

    public String getFilterlabel() {
        return filterlabel;
    }

    public void setFilterlabel(String filterlabel) {
        this.filterlabel = filterlabel;
    }

    @SerializedName("key")
    @Expose
    private String filterlabel;


    public String getP_OriginalPriceInDollar() {
        return P_OriginalPriceInDollar;
    }

    public void setP_OriginalPriceInDollar(String p_OriginalPriceInDollar) {
        P_OriginalPriceInDollar = p_OriginalPriceInDollar;
    }

    public String getP_OriginalPriceInRs() {
        return P_OriginalPriceInRs;
    }

    public void setP_OriginalPriceInRs(String p_OriginalPriceInRs) {
        P_OriginalPriceInRs = p_OriginalPriceInRs;
    }

    public String getP_SaveAmountInDollar() {
        return P_SaveAmountInDollar;
    }

    public void setP_SaveAmountInDollar(String p_SaveAmountInDollar) {
        P_SaveAmountInDollar = p_SaveAmountInDollar;
    }

    public String getP_SavePercentOfDollar() {
        return P_SavePercentOfDollar;
    }

    public void setP_SavePercentOfDollar(String p_SavePercentOfDollar) {
        P_SavePercentOfDollar = p_SavePercentOfDollar;
    }

    public String getP_SaveAmountInRs() {
        return P_SaveAmountInRs;
    }

    public void setP_SaveAmountInRs(String p_SaveAmountInRs) {
        P_SaveAmountInRs = p_SaveAmountInRs;
    }

    public String getP_SavePercentOfRs() {
        return P_SavePercentOfRs;
    }

    public void setP_SavePercentOfRs(String p_SavePercentOfRs) {
        P_SavePercentOfRs = p_SavePercentOfRs;
    }

    @SerializedName("P_SaveAmountInDollar")
    @Expose
    private String P_SaveAmountInDollar;
    @SerializedName("P_SavePercentOfDollar")
    @Expose
    private String P_SavePercentOfDollar;
    @SerializedName("P_SaveAmountInRs")
    @Expose
    private String P_SaveAmountInRs;
    @SerializedName("P_SavePercentOfRs")
    @Expose
    private String P_SavePercentOfRs;

    public String getIsShowProfile() {
        return isShowProfile;
    }

    public void setIsShowProfile(String isShowProfile) {
        this.isShowProfile = isShowProfile;
    }

    @SerializedName("P_IsShowProfileScreen")
    @Expose
    private String isShowProfile="0";

    public String getP_url_text() {
        return P_url_text;
    }

    public void setP_url_text(String p_url_text) {
        P_url_text = p_url_text;
    }

    public String getP_CatId() {
        return P_CatId;
    }

    public void setP_CatId(String p_CatId) {
        P_CatId = p_CatId;
    }

    public ArrayList<String> getSmallImageList() {
        return smallImageList;
    }

    public void setSmallImageList(ArrayList<String> smallImageList) {
        this.smallImageList = smallImageList;
    }

    public ArrayList<String> getLargeImageList() {
        return largeImageList;
    }

    public void setLargeImageList(ArrayList<String> largeImageList) {
        this.largeImageList = largeImageList;
    }

    @SerializedName("P_CatId")
    @Expose

    private String P_CatId;


    @SerializedName("smallImage")
    @Expose
    private ArrayList<String> smallImageList;

    @SerializedName("largeImage")
    @Expose
    private ArrayList<String> largeImageList;

    /**
     * @return The PId
     */
    public String getPId() {
        return PId;
    }

    /**
     * @param PId The P_Id
     */
    public void setPId(String PId) {
        this.PId = PId;
    }

    /**
     * @return The PName
     */
    public String getPName() {
        return PName;
    }

    /**
     * @param PName The P_Name
     */
    public void setPName(String PName) {
        this.PName = PName;
    }

    /**
     * @return The PPriceInDoller
     */
    public String getPPriceInDoller() {
        return PPriceInDoller;
    }

    /**
     * @param PPriceInDoller The P_PriceInDoller
     */
    public void setPPriceInDoller(String PPriceInDoller) {
        this.PPriceInDoller = PPriceInDoller;
    }

    /**
     * @return The PPriceInRs
     */
    public String getPPriceInRs() {
        return PPriceInRs;
    }

    /**
     * @param PPriceInRs The P_PriceInRs
     */
    public void setPPriceInRs(String PPriceInRs) {
        this.PPriceInRs = PPriceInRs;
    }

    /**
     * @return The PSmallDesc
     */
    public String getPSmallDesc() {
        return PSmallDesc;
    }

    /**
     * @param PSmallDesc The P_SmallDesc
     */
    public void setPSmallDesc(String PSmallDesc) {
        this.PSmallDesc = PSmallDesc;
    }

    /**
     * @return The PFullDesc
     */
    public String getPFullDesc() {
        return PFullDesc;
    }

    /**
     * @param PFullDesc The P_FullDesc
     */
    public void setPFullDesc(String PFullDesc) {
        this.PFullDesc = PFullDesc;
    }

    /**
     * @return The PImgUrl
     */
    public String getPImgUrl() {
        return PImgUrl;
    }

    /**
     * @param PImgUrl The P_ImgUrl
     */
    public void setPImgUrl(String PImgUrl) {
        this.PImgUrl = PImgUrl;
    }

    public AstroShopItemDetails() {
    }

    /**
     * @return The PLargeImgUrl
     */
    public String getPLargeImgUrl() {
        return PLargeImgUrl;
    }

    /**
     * @param PLargeImgUrl The P_LargeImgUrl
     */
    public void setPLargeImgUrl(String PLargeImgUrl) {
        this.PLargeImgUrl = PLargeImgUrl;
    }


    public AstroShopItemDetails(String PId) {
        this.PId = PId;
    }

    /**

     * @return The P_OutOfStock
     */
    public String getP_OutOfStock() {
        return P_OutOfStock;
    }

    /**
     * @param P_OutOfStock The P_LargeImgUrl
     */
    public void setP_OutOfStock(String P_OutOfStock) {
        this.P_OutOfStock = P_OutOfStock;
    }


    public String getKdetail() {
        return kdetail;
    }

    public void setKdetail(String kdetail) {
        this.kdetail = kdetail;
    }


    @Override
    public boolean equals(Object o){
        if(o == null)
        {
            return false;

        }
       else if(!(o instanceof AstroShopItemDetails))
        {
            return false;

        }
        else
        {
            AstroShopItemDetails other = (AstroShopItemDetails) o;

            if(!this.getPId().equalsIgnoreCase(other.getPId()))
            {
                return false;
            }
            else
            {
                return  true;
            }
        }


    }

     @Override
    public int hashCode() {
        return  this.getPId().hashCode();
    }
}
