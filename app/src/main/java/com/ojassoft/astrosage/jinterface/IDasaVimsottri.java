package com.ojassoft.astrosage.jinterface;

import com.ojassoft.astrosage.beans.BeanDasa;
import com.ojassoft.astrosage.beans.PlanetData;


public interface IDasaVimsottri {

    /**
     * This function return the array of
     * dasa objects for first level vimsottri dasha.
     *
     * @param obj
     * @return Dasa[]
     * @throws Exception
     */
    BeanDasa[] firstLevel(PlanetData obj) throws Exception;

    /**
     * This function calculate the other dasha level
     * according to passed planet number and array of dasa objects
     *
     * @param iPos
     * @param objArrPrDasa
     * @return Dasa[]
     * @throws Exception
     */
    BeanDasa[] vimOtherLevelDasha(int iPos, BeanDasa[] objArrPrDasa) throws Exception;

    /**
     * This  is a function to calculate other
     * vimshottri dasha level.
     *
     * @param iPos
     * @param objArrPrDasa
     * @param _lastdashatime
     * @return Dasa[]
     * @throws Exception
     */
    BeanDasa[] getNewVimOtherLevelDasa(int iPos, BeanDasa[] objArrPrDasa, double _lastdashatime) throws Exception;


    /**
     * This function is used to get balance of dasha
     *
     * @param degMoon
     * @return double
     * @throws Exception
     */
    double getBalanceOfDasa(double degMoon) throws Exception;

    /**
     * This function is used to return planet for which the dasa is remaine
     *
     * @param degMoon
     * @return planet number
     * @throws Exception
     */
    int getPlanetForBalanceOfDasa(double degMoon) throws Exception;


}
