
package com.ojassoft.astrosage.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class PaySubOption implements Serializable{

    @SerializedName("payOptDesc")
    @Expose
    private String payOptDesc;
    @SerializedName("payOpt")
    @Expose
    private String payOpt;
    @SerializedName("cardsList")
    @Expose
    private String cardsList;

    /**
     * 
     * @return
     *     The payOptDesc
     */
    public String getPayOptDesc() {
        return payOptDesc;
    }

    /**
     * 
     * @param payOptDesc
     *     The payOptDesc
     */
    public void setPayOptDesc(String payOptDesc) {
        this.payOptDesc = payOptDesc;
    }

    /**
     * 
     * @return
     *     The payOpt
     */
    public String getPayOpt() {
        return payOpt;
    }

    /**
     * 
     * @param payOpt
     *     The payOpt
     */
    public void setPayOpt(String payOpt) {
        this.payOpt = payOpt;
    }

    /**
     * 
     * @return
     *     The cardsList
     */
    public String getCardsList() {
        return cardsList;
    }

    /**
     * 
     * @param cardsList
     *     The cardsList
     */
    public void setCardsList(String cardsList) {
        this.cardsList = cardsList;
    }

}
