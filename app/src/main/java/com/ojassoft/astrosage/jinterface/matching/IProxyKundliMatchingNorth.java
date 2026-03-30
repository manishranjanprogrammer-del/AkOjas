package com.ojassoft.astrosage.jinterface.matching;

/**
 * this is proxy interface of north match making.
 *
 * @author Hukum
 */
public interface IProxyKundliMatchingNorth {

    //boolean initialize() throws Exception;

    /**
     * this method separate prediction
     *
     * @param strCategory
     * @param intForBoy
     * @param intForGirl
     * @return
     */
    String subGetMatchMakingInterpretation(String strCategory, int intForBoy, int intForGirl);

    /**
     * return vrna matching points
     *
     * @param maleRasiNumber
     * @param femaleRasiNumber
     * @return
     */
    double matchVarnaGuna(int maleRasiNumber, int femaleRasiNumber);

    /**
     * return vasya matching points
     *
     * @param degOfBoy
     * @param degOfGirl
     * @return
     */
    double matchVashyaGuna(double degOfBoy, double degOfGirl);

    /**
     * return tara matching points
     *
     * @param degOfBoy
     * @param degOfGirl
     * @return
     */
    double matchTaraGuna(double degOfBoy, double degOfGirl);

    /**
     * return yoni matching points
     *
     * @param degOfBoy
     * @param degOfGirl
     * @return
     */
    double matchYoniGuna(double degOfBoy, double degOfGirl);

    /**
     * return maitri matching points
     *
     * @param maleRasiNumber
     * @param femaleRasiNumber
     * @return
     */
    double matchGrahaMitraGuna(int maleRasiNumber, int femaleRasiNumber);

    /**
     * return gana matching points
     *
     * @param degOfBoy
     * @param degOfGirl
     * @return
     */
    double matchGanaGuna(double degOfBoy, double degOfGirl);

    /**
     * return bhakut matching points
     *
     * @param maleRasiNumber
     * @param femaleRasiNumber
     * @return
     */
    double matchBhakutGuna(int maleRasiNumber, int femaleRasiNumber);

    /**
     * return nadi matching points
     *
     * @param degOfBoy
     * @param degOfGirl
     * @return
     */
    double matchNadiGuna(double degOfBoy, double degOfGirl);

    //boolean calculateCompatibilityForMangalDosh(int maleMangalDoshNumber,int femaleMangalDoshNumber);
    //int CalcMangalDosh(int lagnaRashi,int mangalRashi);

    //int totalGuna (int matchVarna,int macthVashya,int matchTara,int matchYoni,int matchGrahaMitra,int matchGana,int matchBhakut,int matchNadi);

    //String CalcMangalDoshString(int lagnaRashi,int mangalRashi);

    /**
     * return boy mangal dosha
     */
    String boyMangalDosh();

    /**
     * return girl mangal dosha
     *
     * @return
     */
    String girlMangalDosh();

    /**
     * return overall conclution
     *
     * @return
     */
    String getConclusion();

    /**
     * This function return boy rashi
     *
     * @return String
     */
    String getBoyRashiNumber();

    /**
     * This function return girl rashi
     *
     * @return String
     */
    String getGirlRashiNumber();

    /**
     * This function return boy moon degree
     *
     * @return String
     */
    String getBoyMoonDegree();

    /**
     * This function return girl  moon degree
     *
     * @return String
     */
    String getGirlMoonDegree();


}
