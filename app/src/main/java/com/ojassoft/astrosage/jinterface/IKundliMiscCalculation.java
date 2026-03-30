package com.ojassoft.astrosage.jinterface;

import com.ojassoft.astrosage.beans.InKPPlanetsAndCusp;
import com.ojassoft.astrosage.beans.KundliPlanetArray;
import com.ojassoft.astrosage.beans.PlanetData;


/**
 * This interface is used in CKundliMiscCalculation.java class
 *
 * @author Bijendra
 * @version 1.0.0
 * @date 11 may 2012
 * @modify -
 */
public interface IKundliMiscCalculation {


    /**
     * This function return Kp planet degree array
     *
     * @param obj
     * @return double[]
     * @throws Exception
     */
    double[] getKPPlanetsDegreeArray(InKPPlanetsAndCusp obj) throws Exception;

    /**
     * This function return planet degree array
     *
     * @param obj
     * @return double[]
     * @throws Exception
     */
    double[] getPlanetsDegreeArray(PlanetData obj) throws Exception;
    /*
	 * This function is used to return lalkitab kundli planets array
	  * @param obj
	  * @param yearNo
	 * @return int []
	 * @throws Exception
	 */

    int[] getLalKitabKundliPlanetsRashiArray(PlanetData obj, int yearNo)
            throws Exception;

    /**
     * This function is used to return Manglik points for kundli
     *
     * @param obj
     * @return int []
     * @throws Exception
     */
    int returnManglikPonits(PlanetData obj) throws Exception;

    /**
     * This function is used to return planets rashi array for Shukra chart
     *
     * @param obj
     * @return int []
     * @throws Exception
     */
    int[] getShukraKundliPlanetsRashiArray(PlanetData obj) throws Exception;

    /**
     * This function is used to return Shodashvarga Planets in Rashi Array
     *
     * @param value
     * @return int[]
     * @throws Exception
     */
    int[] getPlanetsInRashiFromShodashvarga(String value) throws Exception;

    /**
     * This function is used to return planets rashi array for lagna chart
     *
     * @param obj
     * @return int []
     * @throws Exception
     */
    int[] getLagnaKundliPlanetsRashiArray(PlanetData obj) throws Exception;

    /**
     * This function is used to return planets rashi array for chandra chart
     *
     * @param obj
     * @return int []
     * @throws Exception
     */
    int[] getChandraKundliPlanetsRashiArray(PlanetData obj) throws Exception;

    /**
     * This function is used to return plansts rashi
     *
     * @param obj
     * @return int []
     * @throws Exception
     */
    int[] getPlanetsInRashi(PlanetData obj) throws Exception;

    /**
     * This function calculate and  return filled object of
     * KundliPlanetArray to show Lagna kundli  on UI
     * according to passed PlanetData object.
     *
     * @param obj
     * @return KundliPlanetArray
     * @throws Exception
     */
    KundliPlanetArray getLagnaKundli(PlanetData obj) throws Exception;

    /**
     * This function calculate and  return filled object of
     * KundliPlanetArray to show Surya kundli  on UI
     * according to passed PlanetData object.
     *
     * @param obj
     * @return KundliPlanetArray
     * @throws Exception
     */
    KundliPlanetArray getSuryaKundli(PlanetData obj) throws Exception;

    /**
     * This function calculate and  return filled object of
     * KundliPlanetArray to show Chandra kundli  on UI
     * according to passed PlanetData object.
     *
     * @param obj
     * @return KundliPlanetArray
     * @throws Exception
     */
    KundliPlanetArray getChandraKundli(PlanetData obj) throws Exception;

    /**
     * This function is used to return cusp array
     *
     * @param cups
     * @param obj
     * @return
     * @throws Exception
     */
    double[] getCuspsDegreeArray(InKPPlanetsAndCusp cups, PlanetData obj) throws Exception;

    /**
     * This function is used to return cusp mid array
     *
     * @param cups
     * @param obj
     * @return
     * @throws Exception
     */
    double[] getCuspsMidDegreeArrayForChalit(InKPPlanetsAndCusp cups, PlanetData obj) throws Exception;

}
