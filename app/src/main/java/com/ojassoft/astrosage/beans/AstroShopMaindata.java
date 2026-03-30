
package com.ojassoft.astrosage.beans;

import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class AstroShopMaindata {

    @SerializedName("Gemstone")
    @Expose
    private List<AstroShopItemDetails> GemStones = new ArrayList<AstroShopItemDetails>();
    @SerializedName("Yantra")
    @Expose
    private List<AstroShopItemDetails> Yantras = new ArrayList<AstroShopItemDetails>();
    @SerializedName("Navagrah Yantra")
    @Expose
    private List<AstroShopItemDetails> NavagrahYantras = new ArrayList<AstroShopItemDetails>();
    @SerializedName("Rudraksha")
    @Expose
    private List<AstroShopItemDetails> Rudraksha = new ArrayList<AstroShopItemDetails>();
    @SerializedName("Mala")
    @Expose
    private List<AstroShopItemDetails> Mala = new ArrayList<AstroShopItemDetails>();
    @SerializedName("Jadi (Tree Roots)")
    @Expose
    private List<AstroShopItemDetails> JadiTreeRoots = new ArrayList<AstroShopItemDetails>();
    @SerializedName("Miscellaneous")
    @Expose
    private List<AstroShopItemDetails> miscellaneous = new ArrayList<AstroShopItemDetails>();



    /**
     * @return The GemStones
     */
    public List<AstroShopItemDetails> getGemStones() {
        return GemStones;
    }

    /**
     * @param GemStones The GemStones
     */
    public void setGemStones(List<AstroShopItemDetails> GemStones) {
        this.GemStones = GemStones;
    }

    /**
     * @return The Yantras
     */
    public List<AstroShopItemDetails> getYantras() {
        return Yantras;
    }

    /**
     * @param Yantras The Yantras
     */
    public void setYantras(List<AstroShopItemDetails> Yantras) {
        this.Yantras = Yantras;
    }

    /**
     * @return The NavagrahYantras
     */
    public List<AstroShopItemDetails> getNavagrahYantras() {
        return NavagrahYantras;
    }

    /**
     * @param NavagrahYantras The Navagrah Yantras
     */
    public void setNavagrahYantras(List<AstroShopItemDetails> NavagrahYantras) {
        this.NavagrahYantras = NavagrahYantras;
    }

    /**
     * @return The Rudraksha
     */
    public List<AstroShopItemDetails> getRudraksha() {
        return Rudraksha;
    }

    /**
     * @param Rudraksha The Rudraksha
     */
    public void setRudraksha(List<AstroShopItemDetails> Rudraksha) {
        this.Rudraksha = Rudraksha;
    }

    /**
     * @return The Mala
     */
    public List<AstroShopItemDetails> getMala() {
        return Mala;
    }

    /**
     * @param Mala The Mala
     */
    public void setMala(List<AstroShopItemDetails> Mala) {
        this.Mala = Mala;
    }

    /**
     * @return The JadiTreeRoots
     */
    public List<AstroShopItemDetails> getJadiTreeRoots() {
        return JadiTreeRoots;
    }

    /**
     * @param JadiTreeRoots The Jadi (Tree Roots)
     */
    public void setJadiTreeRoots(List<AstroShopItemDetails> JadiTreeRoots) {
        this.JadiTreeRoots = JadiTreeRoots;
    }

    /**
     * @return The miscellaneous
     */
    public List<AstroShopItemDetails> getMiscItems() {
        return miscellaneous; }

    /**
     * @param miscellaneous The Miscellaneous items
     */
    public void setMiscItems(List<AstroShopItemDetails> miscellaneous) {
        this.miscellaneous = miscellaneous; }

}
