package com.ojassoft.astrosage.controller;

import com.ojassoft.astrosage.beans.InKPPlanetsAndCusp;
import com.ojassoft.astrosage.beans.KundliPlanetArray;
import com.ojassoft.astrosage.beans.Planet;
import com.ojassoft.astrosage.beans.PlanetData;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.jinterface.IKundliMiscCalculation;


/**
 * This class is used for basic chart and other calculation.
 *
 * @author Bijendra
 * @version 1.0.0
 * @date 10 may 2012
 * @modify -
 */

class CKundliMiscCalculation implements IKundliMiscCalculation {
    /**
     * These are the local variables use  to store local values
     * which will be used in miscellaneous  operations.
     */

    private KundliPlanetArray objKundliPlanetArray = null;
    private com.ojassoft.astrosage.beans.Planet[] _planets = null;


    /**
     * Constructor of the class used to initialize
     * local reference variables.
     */
    public CKundliMiscCalculation() {
        objKundliPlanetArray = new KundliPlanetArray();
        _planets = new com.ojassoft.astrosage.beans.Planet[13];
    }


    /**
     * This function calculate and  return filled object of
     * KundliPlanetArray to show Lagna kundli  on UI
     * according to passed PlanetData object.
     *
     * @param obj
     * @return KundliPlanetArray
     * @throws Exception
     */

    public KundliPlanetArray getLagnaKundli(PlanetData obj) throws Exception {
        try {
            setPlanetsRasi(obj);
            setPlanetsLagnaBhav();
        } catch (Exception e) {
            throw e;
        }

        return objKundliPlanetArray;
    }

    /**
     * This function calculate and  return filled object of
     * KundliPlanetArray to show Surya kundli  on UI
     * according to passed PlanetData object.
     *
     * @param obj
     * @return KundliPlanetArray
     * @throws Exception
     */

    public KundliPlanetArray getSuryaKundli(PlanetData obj) throws Exception {
        // TODO Auto-generated method stub
        try {
            setPlanetsRasi(obj);
            setPlanetsSuryaBhav();
        } catch (Exception e) {
            throw e;
        }

        return objKundliPlanetArray;
    }

    /**
     * This function calculate and  return filled object of
     * KundliPlanetArray to show Chandra kundli  on UI
     * according to passed PlanetData object.
     *
     * @param obj
     * @return KundliPlanetArray
     * @throws Exception
     */
    public KundliPlanetArray getChandraKundli(PlanetData obj) throws Exception {
        // TODO Auto-generated method stub
        try {
            setPlanetsRasi(obj);
            setPlanetsChandraBhav();
        } catch (Exception e) {
            throw e;
        }
        return objKundliPlanetArray;
    }

    /**
     * This function put the Planets value
     * into local _planet array for further
     * operations.
     *
     * @param obj
     * @return
     */
    private void setPlanetsRasi(PlanetData obj) {

        //Sun
        _planets[0] = new com.ojassoft.astrosage.beans.Planet();
        _planets[0].setPlanetNo(0);
        _planets[0].setPlanetDeg(obj.getSun());
        _planets[0].setPlanetRasi((int) (obj.getSun() / 30.00) + 1);

        objKundliPlanetArray.setSun(_planets[0]);

        //Moon
        _planets[1] = new com.ojassoft.astrosage.beans.Planet();
        _planets[1].setPlanetNo(1);
        _planets[1].setPlanetDeg(obj.getMoon());
        _planets[1].setPlanetRasi((int) (obj.getMoon() / 30.00) + 1);
        objKundliPlanetArray.setMoon(_planets[1]);
        //objKundliPlanetArray.setMoon(objMoon);

        //Marsh
        _planets[2] = new com.ojassoft.astrosage.beans.Planet();
        _planets[2].setPlanetDeg(obj.getMarsh());
        _planets[2].setPlanetNo(2);
        _planets[2].setPlanetRasi((int) (obj.getMarsh() / 30.00) + 1);
        objKundliPlanetArray.setMarsh(_planets[2]);

        //Mer
        _planets[3] = new com.ojassoft.astrosage.beans.Planet();
        _planets[3].setPlanetNo(3);
        _planets[3].setPlanetDeg(obj.getMercury());
        _planets[3].setPlanetRasi((int) (obj.getMercury() / 30.00) + 1);
        objKundliPlanetArray.setMercury(_planets[3]);


        //Jup

        _planets[4] = new com.ojassoft.astrosage.beans.Planet();
        _planets[4].setPlanetNo(4);
        _planets[4].setPlanetDeg(obj.getJup());
        _planets[4].setPlanetRasi((int) (obj.getJup() / 30.00) + 1);
        objKundliPlanetArray.setJupiter(_planets[4]);

        //Ven
        _planets[5] = new com.ojassoft.astrosage.beans.Planet();
        _planets[5].setPlanetNo(5);
        _planets[5].setPlanetDeg(obj.getVenus());
        _planets[5].setPlanetRasi((int) (obj.getVenus() / 30.00) + 1);
        objKundliPlanetArray.setVenus(_planets[5]);

        //Sat
        _planets[6] = new com.ojassoft.astrosage.beans.Planet();
        _planets[6].setPlanetNo(6);
        _planets[6].setPlanetDeg(obj.getSat());
        _planets[6].setPlanetRasi((int) (obj.getSat() / 30.00) + 1);
        objKundliPlanetArray.setSaturn(_planets[6]);

        //Rahu
        _planets[7] = new com.ojassoft.astrosage.beans.Planet();
        _planets[7].setPlanetNo(7);
        _planets[7].setPlanetDeg(obj.getRahu());
        _planets[7].setPlanetRasi((int) (obj.getRahu() / 30.00) + 1);
        objKundliPlanetArray.setRahu(_planets[7]);

        //Ketu
        _planets[8] = new com.ojassoft.astrosage.beans.Planet();
        _planets[8].setPlanetDeg(obj.getKetu());
        _planets[8].setPlanetRasi((int) (obj.getKetu() / 30.00) + 1);
        _planets[8].setPlanetNo(8);
        objKundliPlanetArray.setKetu(_planets[8]);

        //Uranus
        _planets[9] = new com.ojassoft.astrosage.beans.Planet();
        _planets[9].setPlanetNo(9);
        _planets[9].setPlanetDeg(obj.getUranus());
        _planets[9].setPlanetRasi((int) (obj.getUranus() / 30.00) + 1);
        objKundliPlanetArray.setUranus(_planets[9]);

        //Neptune
        _planets[10] = new com.ojassoft.astrosage.beans.Planet();
        _planets[10].setPlanetNo(10);
        _planets[10].setPlanetDeg(obj.getNeptune());
        _planets[10].setPlanetRasi((int) (obj.getNeptune() / 30.00) + 1);
        objKundliPlanetArray.setNeptune(_planets[10]);

        //Pluto
        _planets[11] = new com.ojassoft.astrosage.beans.Planet();
        _planets[11].setPlanetNo(11);
        _planets[11].setPlanetDeg(obj.getPluto());
        _planets[11].setPlanetRasi((int) (obj.getPluto() / 30.00) + 1);
        objKundliPlanetArray.setPluto(_planets[11]);

        _planets[12] = new com.ojassoft.astrosage.beans.Planet();
        _planets[12].setPlanetDeg(obj.getLagna());
        _planets[12].setPlanetRasi((int) (obj.getLagna() / 30.00) + 1);
        _planets[12].setPlanetNo(12);
        objKundliPlanetArray.setLagna(_planets[12]);

    }

    /**
     * This function calculate planet distance
     * from lagna rashi and put in chart object.
     *
     * @param
     * @return
     */
    private void setPlanetsLagnaBhav() {
        objKundliPlanetArray.LagnaRasi = _planets[12].getPlanetRasi();
        for (int i = 0; i < 12; i++) {
            _planets[i].setPlanetBhav(getCalculatePlanetBhav(_planets[12].getPlanetRasi(), _planets[i].getPlanetRasi()));
            putPlanetIntoBhav(_planets[i]);
            putPlanetIntoRasi(_planets[i]);
        }
        putPlanetIntoRasi(_planets[12]);
    }

    /**
     * This function calculate Surya kundli.
     *
     * @param
     * @return
     */
    private void setPlanetsSuryaBhav() {
        objKundliPlanetArray.LagnaRasi = _planets[0].getPlanetRasi();

        _planets[12].setPlanetRasi(_planets[0].getPlanetRasi());//for south kundli
        for (int i = 0; i < 12; i++) {
            _planets[i].setPlanetBhav(getCalculatePlanetBhav(objKundliPlanetArray.LagnaRasi, _planets[i].getPlanetRasi()));
            putPlanetIntoBhav(_planets[i]);
            putPlanetIntoRasi(_planets[i]);
        }
        putPlanetIntoRasi(_planets[12]);//for south kundli
    }

    /**
     * This function calculate Chandra kundli.
     *
     * @param
     * @return
     */
    private void setPlanetsChandraBhav() {
        objKundliPlanetArray.LagnaRasi = _planets[1].getPlanetRasi();
        _planets[12].setPlanetRasi(_planets[1].getPlanetRasi());//for south kundli
        for (int i = 0; i < 12; i++) {
            _planets[i].setPlanetBhav(getCalculatePlanetBhav(objKundliPlanetArray.LagnaRasi, _planets[i].getPlanetRasi()));
            putPlanetIntoBhav(_planets[i]);
            putPlanetIntoRasi(_planets[i]);
        }
        putPlanetIntoRasi(_planets[12]);//for south kundli
    }

    /**
     * This function put planet into  its
     * related bhava  array list according to passed
     * planet object.
     *
     * @param objPlanet
     * @return
     */
    private void putPlanetIntoBhav(Planet objPlanet) {

        switch (objPlanet.getPlanetBhav()) {
            case 1:
                objKundliPlanetArray.Bhav1.add(objPlanet);
                break;
            case 2:
                objKundliPlanetArray.Bhav2.add(objPlanet);
                break;
            case 3:
                objKundliPlanetArray.Bhav3.add(objPlanet);
                break;
            case 4:
                objKundliPlanetArray.Bhav4.add(objPlanet);
                break;
            case 5:
                objKundliPlanetArray.Bhav5.add(objPlanet);
                break;
            case 6:
                objKundliPlanetArray.Bhav6.add(objPlanet);
                break;
            case 7:
                objKundliPlanetArray.Bhav7.add(objPlanet);
                break;
            case 8:
                objKundliPlanetArray.Bhav8.add(objPlanet);
                break;
            case 9:
                objKundliPlanetArray.Bhav9.add(objPlanet);
                break;
            case 10:
                objKundliPlanetArray.Bhav10.add(objPlanet);
                break;
            case 11:
                objKundliPlanetArray.Bhav11.add(objPlanet);
                break;
            case 12:
                objKundliPlanetArray.Bhav12.add(objPlanet);
                break;

        }
    }
//THIS FUNCTION WILL USE TO PUT PLANETS INTO RASI TO SHOW IN SOUTHINDIAN STYLE

    /**
     * This function put planet into  its
     * related rashi array  list according to passed
     * planet object.
     *
     * @param objPlanet
     * @return
     */
    private void putPlanetIntoRasi(Planet objPlanet) {

        switch (objPlanet.getPlanetRasi()) {
            case 1:
                objKundliPlanetArray.Rasi1.add(objPlanet);
                break;
            case 2:
                objKundliPlanetArray.Rasi2.add(objPlanet);
                break;
            case 3:
                objKundliPlanetArray.Rasi3.add(objPlanet);
                break;
            case 4:
                objKundliPlanetArray.Rasi4.add(objPlanet);
                break;
            case 5:
                objKundliPlanetArray.Rasi5.add(objPlanet);
                break;
            case 6:
                objKundliPlanetArray.Rasi6.add(objPlanet);
                break;
            case 7:
                objKundliPlanetArray.Rasi7.add(objPlanet);
                break;
            case 8:
                objKundliPlanetArray.Rasi8.add(objPlanet);
                break;
            case 9:
                objKundliPlanetArray.Rasi9.add(objPlanet);
                break;
            case 10:
                objKundliPlanetArray.Rasi10.add(objPlanet);
                break;
            case 11:
                objKundliPlanetArray.Rasi11.add(objPlanet);
                break;
            case 12:
                objKundliPlanetArray.Rasi12.add(objPlanet);
                break;

        }
    }

    /**
     * This function calculate in distance
     * of planet rashi  from lagna rashi.
     *
     * @param lRasi
     * @param pRasi
     * @return int
     */
    private int getCalculatePlanetBhav(int lRasi, int pRasi) {
        int _iTemo = 0;

        _iTemo = pRasi - lRasi;
        if (_iTemo < 0)
            _iTemo += 12;
        _iTemo += 1;

        return _iTemo;
    }


    public int[] getPlanetsInRashi(PlanetData obj) throws Exception {
        int[] _planetsInRashi = new int[13];
        _planetsInRashi[0] = (int) ((obj.getSun() / 30.0) + 1);
        _planetsInRashi[1] = (int) ((obj.getMoon() / 30.0) + 1);
        _planetsInRashi[21] = (int) ((obj.getMarsh() / 30.0) + 1);
        _planetsInRashi[3] = (int) ((obj.getMercury() / 30.0) + 1);
        _planetsInRashi[4] = (int) ((obj.getJup() / 30.0) + 1);
        _planetsInRashi[5] = (int) ((obj.getVenus() / 30.0) + 1);
        _planetsInRashi[6] = (int) ((obj.getSat() / 30.0) + 1);
        _planetsInRashi[7] = (int) ((obj.getRahu() / 30.0) + 1);
        _planetsInRashi[8] = (int) ((obj.getKetu() / 30.0) + 1);
        _planetsInRashi[9] = (int) ((obj.getUranus() / 30.0) + 1);
        _planetsInRashi[10] = (int) ((obj.getNeptune() / 30.0) + 1);
        _planetsInRashi[11] = (int) ((obj.getPluto() / 30.0) + 1);

        _planetsInRashi[12] = (int) ((obj.getLagna() / 30.0) + 1);
        // TODO Auto-generated method stub
        return _planetsInRashi;
    }

    /**
     * This function is used to return planets rashi array for lagna chart
     *
     * @param obj
     * @return int []
     * @throws Exception
     */


    public int[] getLagnaKundliPlanetsRashiArray(PlanetData obj)
            throws Exception {
        // TODO Auto-generated method stub
        int[] planestRashi = new int[13];

        planestRashi[0] = (int) (obj.getSun() / 30.00) + 1;
        planestRashi[1] = (int) (obj.getMoon() / 30.00) + 1;
        planestRashi[2] = (int) (obj.getMarsh() / 30.00) + 1;
        planestRashi[3] = (int) (obj.getMercury() / 30.00) + 1;
        planestRashi[4] = (int) (obj.getJup() / 30.00) + 1;
        planestRashi[5] = (int) (obj.getVenus() / 30.00) + 1;
        planestRashi[6] = (int) (obj.getSat() / 30.00) + 1;
        planestRashi[7] = (int) (obj.getRahu() / 30.00) + 1;
        planestRashi[8] = (int) (obj.getKetu() / 30.00) + 1;
        planestRashi[9] = (int) (obj.getUranus() / 30.00) + 1;
        planestRashi[10] = (int) (obj.getNeptune() / 30.00) + 1;
        planestRashi[11] = (int) (obj.getPluto() / 30.00) + 1;
        planestRashi[12] = (int) (obj.getLagna() / 30.00) + 1;


        return planestRashi;
    }


    /**
     * This function is used to return planets rashi array for chandra chart
     *
     * @param obj
     * @return int []
     * @throws Exception
     */

    public int[] getChandraKundliPlanetsRashiArray(PlanetData obj)
            throws Exception {
        // TODO Auto-generated method stub

        int[] planestRashi = new int[13];

        planestRashi[0] = (int) (obj.getSun() / 30.00) + 1;
        planestRashi[1] = (int) (obj.getMoon() / 30.00) + 1;
        planestRashi[2] = (int) (obj.getMarsh() / 30.00) + 1;
        planestRashi[3] = (int) (obj.getMercury() / 30.00) + 1;
        planestRashi[4] = (int) (obj.getJup() / 30.00) + 1;
        planestRashi[5] = (int) (obj.getVenus() / 30.00) + 1;
        planestRashi[6] = (int) (obj.getSat() / 30.00) + 1;
        planestRashi[7] = (int) (obj.getRahu() / 30.00) + 1;
        planestRashi[8] = (int) (obj.getKetu() / 30.00) + 1;
        planestRashi[9] = (int) (obj.getUranus() / 30.00) + 1;
        planestRashi[10] = (int) (obj.getNeptune() / 30.00) + 1;
        planestRashi[11] = (int) (obj.getPluto() / 30.00) + 1;
        planestRashi[12] = planestRashi[1];
        return planestRashi;
    }


    /**
     * This function is used to return Shodashvarga Planets in Rashi Array
     *
     * @param value
     * @return int[]
     * @throws Exception
     */

    public int[] getPlanetsInRashiFromShodashvarga(String value)
            throws Exception {
        int[] planestRashi = new int[13];
        String[] _sepPlaRashi = new String[13];

        _sepPlaRashi = value.trim().split(",");

        planestRashi[0] = Integer.valueOf(_sepPlaRashi[1]);
        planestRashi[1] = Integer.valueOf(_sepPlaRashi[2]);
        planestRashi[2] = Integer.valueOf(_sepPlaRashi[3]);
        planestRashi[3] = Integer.valueOf(_sepPlaRashi[4]);
        planestRashi[4] = Integer.valueOf(_sepPlaRashi[5]);
        planestRashi[5] = Integer.valueOf(_sepPlaRashi[6]);
        planestRashi[6] = Integer.valueOf(_sepPlaRashi[7]);
        planestRashi[7] = Integer.valueOf(_sepPlaRashi[8]);
        planestRashi[8] = Integer.valueOf(_sepPlaRashi[9]);
        planestRashi[9] = Integer.valueOf(_sepPlaRashi[10]);
        planestRashi[10] = Integer.valueOf(_sepPlaRashi[11]);
        planestRashi[11] = Integer.valueOf(_sepPlaRashi[12]);
        planestRashi[12] = Integer.valueOf(_sepPlaRashi[0]);

        // TODO Auto-generated method stub
        return planestRashi;
    }


    /**
     * This function is used to return planets rashi array for Shukra chart
     *
     * @param obj
     * @return int []
     * @throws Exception
     */

    public int[] getShukraKundliPlanetsRashiArray(PlanetData obj)
            throws Exception {
        // TODO Auto-generated method stub

        int[] planestRashi = new int[13];

        planestRashi[0] = (int) (obj.getSun() / 30.00) + 1;
        planestRashi[1] = (int) (obj.getMoon() / 30.00) + 1;
        planestRashi[2] = (int) (obj.getMarsh() / 30.00) + 1;
        planestRashi[3] = (int) (obj.getMercury() / 30.00) + 1;
        planestRashi[4] = (int) (obj.getJup() / 30.00) + 1;
        planestRashi[5] = (int) (obj.getVenus() / 30.00) + 1;
        planestRashi[6] = (int) (obj.getSat() / 30.00) + 1;
        planestRashi[7] = (int) (obj.getRahu() / 30.00) + 1;
        planestRashi[8] = (int) (obj.getKetu() / 30.00) + 1;
        planestRashi[9] = (int) (obj.getUranus() / 30.00) + 1;
        planestRashi[10] = (int) (obj.getNeptune() / 30.00) + 1;
        planestRashi[11] = (int) (obj.getPluto() / 30.00) + 1;
        planestRashi[12] = planestRashi[5];
        return planestRashi;
    }


    /**
     * This function is used to return Manglik points for kundli
     *
     * @param obj
     * @return int []
     * @throws Exception
     */

    public int returnManglikPonits(PlanetData obj) throws Exception {
        // TODO Auto-generated method stub
        int _manglickPoints = 0;

        int marshRashi = (int) (obj.getMarsh() / 30.00) + 1;

        int lagnaRashi = (int) (obj.getLagna() / 30.00) + 1;
        int moonRashi = (int) (obj.getMoon() / 30.00) + 1;
        int venusRashi = (int) (obj.getVenus() / 30.00) + 1;

        //_manglickPoints = manglikVoteOfLagna(marshRashi, lagnaRashi) + manglikVoteOfMoon(marshRashi, moonRashi) + manglikVoteOfVenus(marshRashi, venusRashi);
        _manglickPoints = manglikVoteOfLagna(marshRashi, lagnaRashi) + manglikVoteOfMoon(marshRashi, moonRashi);
        //Log.d("Mangal_points", String.valueOf(_manglickPoints));
        return _manglickPoints;
    }


    /**
     * Thsi function is used to check mangal dosh in Lagna Kundli
     *
     * @param marshRashi
     * @param lagnaRashi
     * @return int
     */
    private int manglikVoteOfLagna(int marshRashi, int lagnaRashi) {

        int retValue = 0;
        int checkPoint = -1;
        if ((marshRashi - lagnaRashi) < 0)
            checkPoint = marshRashi - lagnaRashi + 12;
        else
            checkPoint = marshRashi - lagnaRashi;


        if (checkPoint == CGlobalVariables.HOUSE_1 || checkPoint == CGlobalVariables.HOUSE_4
                || checkPoint == CGlobalVariables.HOUSE_7 || checkPoint == CGlobalVariables.HOUSE_8 ||
                checkPoint == CGlobalVariables.HOUSE_12)

            retValue = 1;
        else
            retValue = 0;


        return retValue;

    }

    /**
     * Thsi function is used to check mangal dosh in Moon Kundli
     *
     * @param marshRashi
     * @param lagnaRashi
     * @return int
     */
    private int manglikVoteOfMoon(int marshRashi, int moonRashi) {
        // TODO Auto-generated method stub
        int retValue = 0;
        int checkPoint = -1;
        if ((marshRashi - moonRashi) < 0)
            checkPoint = marshRashi - moonRashi + 12;
        else
            checkPoint = marshRashi - moonRashi;

		/*if (checkPoint == 0 || checkPoint == 3 || checkPoint == 6
                || checkPoint == 7 || checkPoint == 11)
			retValue= 1;
		else
			retValue= 0;*/
        if (checkPoint == CGlobalVariables.HOUSE_1 || checkPoint == CGlobalVariables.HOUSE_4
                || checkPoint == CGlobalVariables.HOUSE_7 || checkPoint == CGlobalVariables.HOUSE_8 ||
                checkPoint == CGlobalVariables.HOUSE_12)
            retValue = 1;
        else
            retValue = 0;

        //Log.d("Mangal_moon", String.valueOf(checkPoint));
        //Log.d("Mangal_Moon_Return", String.valueOf(retValue));
        return retValue;


    }

    /**
     * Thsi function is used to check mangal dosh in Shukra Kundli
     *
     * @param marshRashi
     * @param lagnaRashi
     * @return int
     */
    private int manglikVoteOfVenus(int marshRashi, int venusRashi) {
        // TODO Auto-generated method stub
        int checkPoint = -1;
        if ((marshRashi - venusRashi) < 0)
            checkPoint = marshRashi - venusRashi + 12;
        else
            checkPoint = marshRashi - venusRashi;

        if (checkPoint == 0 || checkPoint == 3 || checkPoint == 6
                || checkPoint == 7 || checkPoint == 1)
            return 1;
        else
            return 0;
    }

    /*
     * This function is used to return lalkitab kundli planets array
     * @param obj
     * @param yearNo
     * @return int []
     * @throws Exception
     */

    public int[] getLalKitabKundliPlanetsRashiArray(PlanetData obj, int yearNo)
            throws Exception {
        // TODO Auto-generated method stub
        int[] presentBhava = new int[13];
        int[] planetBhava = new int[13];
        int[] planetRashi = new int[13];
        int lagnaRashi = 0;

        planetRashi[0] = (int) (obj.getSun() / 30.00) + 1;
        planetRashi[1] = (int) (obj.getMoon() / 30.00) + 1;
        planetRashi[2] = (int) (obj.getMarsh() / 30.00) + 1;
        planetRashi[3] = (int) (obj.getMercury() / 30.00) + 1;
        planetRashi[4] = (int) (obj.getJup() / 30.00) + 1;
        planetRashi[5] = (int) (obj.getVenus() / 30.00) + 1;
        planetRashi[6] = (int) (obj.getSat() / 30.00) + 1;
        planetRashi[7] = (int) (obj.getRahu() / 30.00) + 1;
        planetRashi[8] = (int) (obj.getKetu() / 30.00) + 1;
        planetRashi[9] = (int) (obj.getUranus() / 30.00) + 1;
        planetRashi[10] = (int) (obj.getNeptune() / 30.00) + 1;
        planetRashi[11] = (int) (obj.getPluto() / 30.00) + 1;
        planetRashi[12] = (int) (obj.getLagna() / 30.00) + 1;

        lagnaRashi = planetRashi[12];

        for (int i = 0; i < 12; i++) {
            planetBhava[i] = planetRashi[i] - lagnaRashi;

            if (planetBhava[i] < 0) {
                planetBhava[i] = planetBhava[i] + 12 + 1;

            } else {
                planetBhava[i] = planetBhava[i] + 1;

            }


            if (yearNo > -1)
                presentBhava[i] = CGlobalVariables.LalKitabVarshaPhalTable[yearNo][planetBhava[i] - 1];
            else
                presentBhava[i] = planetBhava[i];


        }
        presentBhava[12] = 1;
        return presentBhava;
    }

    /**
     * This function return planet degree array.
     *
     * @param obj
     * @return double[]
     * @throws Exception
     */

    public double[] getPlanetsDegreeArray(PlanetData obj) throws Exception {
        double[] _planetDegree = new double[13];
        _planetDegree[0] = obj.getSun();
        _planetDegree[1] = obj.getMoon();
        _planetDegree[2] = obj.getMarsh();
        _planetDegree[3] = obj.getMercury();
        _planetDegree[4] = obj.getJup();
        _planetDegree[5] = obj.getVenus();
        _planetDegree[6] = obj.getSat();
        _planetDegree[7] = obj.getRahu();
        _planetDegree[8] = obj.getKetu();
        _planetDegree[9] = obj.getUranus();
        _planetDegree[10] = obj.getNeptune();
        _planetDegree[11] = obj.getPluto();
        _planetDegree[12] = obj.getLagna();

		/*for(int i=0;i<12;i++)
			//Log.e("Planet "+String.valueOf(i+1), String.valueOf(_planetDegree[i]));*/

        return _planetDegree;
    }

    /**
     * This function return Kp planet degree array
     *
     * @param obj
     * @return double[]
     * @throws Exception
     */

    public double[] getKPPlanetsDegreeArray(InKPPlanetsAndCusp obj) throws Exception {
        double[] _planetDegree = new double[13];

        _planetDegree[0] = obj.getSun();
        _planetDegree[1] = obj.getMoon();
        _planetDegree[2] = obj.getMarsh();
        _planetDegree[3] = obj.getMercury();
        _planetDegree[4] = obj.getJup();
        _planetDegree[5] = obj.getVenus();
        _planetDegree[6] = obj.getSat();
        _planetDegree[7] = obj.getRahu();
        _planetDegree[8] = obj.getKetu();
        _planetDegree[9] = obj.getUranus();
        _planetDegree[10] = obj.getNeptune();
        _planetDegree[11] = obj.getPluto();
        _planetDegree[12] = obj.getLagna();


        return _planetDegree;
    }


    public double[] getCuspsDegreeArray(InKPPlanetsAndCusp cups, PlanetData obj) throws Exception {

        double[] _cuspDegree = new double[12];
        double[] _tempDegree = new double[12];
        double diff2 = 0.0, temp1 = 0;

        _cuspDegree = getCuspsMidDegreeArrayForChalit(cups, obj);

        // CLACULATE CUSP DEGREE
        for (int i = 0; i < 12; i++) {

            if (i == 0) {
                diff2 = _cuspDegree[0] - _cuspDegree[11];
                if (diff2 < 0) {
                    diff2 = 360.0 - _cuspDegree[11];
                    diff2 = diff2 + _cuspDegree[0];
                }
            } else {
                diff2 = _cuspDegree[i] - _cuspDegree[i - 1];
                if (diff2 < 0) {
                    diff2 = 360.0 - _cuspDegree[i - 1];
                    diff2 = diff2 + _cuspDegree[i];
                }
            }

            diff2 /= 2.0;

            temp1 = _cuspDegree[i] - diff2;

            if (temp1 < 0.0)
                temp1 += 360.0;

            _tempDegree[i] = temp1;

        }

        /*
         * for(int i=0;i<12;i++) //Log.e("Bhava "+String.valueOf(i+1),
         * String.valueOf(_tempDegree[i]));
         */

        // return _cuspDegree;

        return _tempDegree;
    }

    public double[] getCuspsMidDegreeArrayForChalit(InKPPlanetsAndCusp cups, PlanetData obj) throws Exception {

        double[] _cuspDegree = new double[12];
        double ayaDiff = 0.0;
        double diff = 0.0;

        ayaDiff = cups.getKpAyan() - obj.getAyan();


        //CUSP -1
        _cuspDegree[0] = cups.getKpCusp1() + ayaDiff;
        _cuspDegree[0] = checkDegree(_cuspDegree[0]);

        //CUSP -10
        _cuspDegree[9] = cups.getKpCusp10() + ayaDiff;
        _cuspDegree[9] = checkDegree(_cuspDegree[9]);


        //CUSP -7
        _cuspDegree[6] = _cuspDegree[0] + 180;
        _cuspDegree[6] = checkDegree(_cuspDegree[6]);

        //CUSP -4
        _cuspDegree[3] = _cuspDegree[9] - 180;
        _cuspDegree[3] = checkDegree(_cuspDegree[3]);

        //CUSP -2,3
        diff = _cuspDegree[3] - _cuspDegree[0];
        if (diff < 0)
            diff += 360.0;

        _cuspDegree[1] = _cuspDegree[0] + diff / 3;
        _cuspDegree[1] = checkDegree(_cuspDegree[1]);

        _cuspDegree[2] = _cuspDegree[1] + diff / 3;
        _cuspDegree[2] = checkDegree(_cuspDegree[2]);

        //CUSP -5,6
        diff = _cuspDegree[6] - _cuspDegree[3];
        if (diff < 0)
            diff += 360.0;

        _cuspDegree[4] = _cuspDegree[3] + diff / 3;
        _cuspDegree[4] = checkDegree(_cuspDegree[4]);

        _cuspDegree[5] = _cuspDegree[4] + diff / 3;
        _cuspDegree[5] = checkDegree(_cuspDegree[5]);


        //CUSP -8,9
        diff = _cuspDegree[9] - _cuspDegree[6];
        if (diff < 0)
            diff += 360.0;

        _cuspDegree[7] = _cuspDegree[6] + diff / 3;
        _cuspDegree[7] = checkDegree(_cuspDegree[7]);

        _cuspDegree[8] = _cuspDegree[7] + diff / 3;
        _cuspDegree[8] = checkDegree(_cuspDegree[8]);


        //CUSP -11,12
        diff = _cuspDegree[0] - _cuspDegree[9];
        if (diff < 0)
            diff += 360.0;

        _cuspDegree[10] = _cuspDegree[9] + diff / 3;
        _cuspDegree[10] = checkDegree(_cuspDegree[10]);

        _cuspDegree[11] = _cuspDegree[10] + diff / 3;
        _cuspDegree[11] = checkDegree(_cuspDegree[11]);


		/*
		for(int i=0;i<12;i++)
			//Log.e("Bhava  Mid "+String.valueOf(i+1), String.valueOf(_cuspDegree[i]));*/


        return _cuspDegree;
    }


    private double checkDegree(double deg) {
        double temp = deg;

        if (temp < 0)
            temp += 360.00;

        if (temp > 360.0)
            temp -= 360.00;

        return temp;
    }

    public int[] getTransitKundliPlanetsRashiArray(PlanetData obj)
            throws Exception {
        // TODO Auto-generated method stub
        int[] planestRashi = new int[13];

        planestRashi[0] = (int) (obj.getSun() / 30.00) + 1;
        planestRashi[1] = (int) (obj.getMoon() / 30.00) + 1;
        planestRashi[2] = (int) (obj.getMarsh() / 30.00) + 1;
        planestRashi[3] = (int) (obj.getMercury() / 30.00) + 1;
        planestRashi[4] = (int) (obj.getJup() / 30.00) + 1;
        planestRashi[5] = (int) (obj.getVenus() / 30.00) + 1;
        planestRashi[6] = (int) (obj.getSat() / 30.00) + 1;
        planestRashi[7] = (int) (obj.getRahu() / 30.00) + 1;
        planestRashi[8] = (int) (obj.getKetu() / 30.00) + 1;
        planestRashi[9] = (int) (obj.getUranus() / 30.00) + 1;
        planestRashi[10] = (int) (obj.getNeptune() / 30.00) + 1;
        planestRashi[11] = (int) (obj.getPluto() / 30.00) + 1;
        planestRashi[12] = (int) (obj.getLagna() / 30.00) + 1;
        //planestRashi[12] = planestRashi[1];

        return planestRashi;
    }

    public double[] getTransitPlanetsDegreeArray(PlanetData obj) throws Exception {
        double[] _planetDegree = new double[13];
        _planetDegree[0] = obj.getSun();
        _planetDegree[1] = obj.getMoon();
        _planetDegree[2] = obj.getMarsh();
        _planetDegree[3] = obj.getMercury();
        _planetDegree[4] = obj.getJup();
        _planetDegree[5] = obj.getVenus();
        _planetDegree[6] = obj.getSat();
        _planetDegree[7] = obj.getRahu();
        _planetDegree[8] = obj.getKetu();
        _planetDegree[9] = obj.getUranus();
        _planetDegree[10] = obj.getNeptune();
        _planetDegree[11] = obj.getPluto();
        _planetDegree[12] = obj.getLagna();

		/*for(int i=0;i<12;i++)
			Log.e("Planet "+String.valueOf(i+1), String.valueOf(_planetDegree[i]));*/

        return _planetDegree;
    }

    public int[] getTransitKundliPlanetsRashiArrayForMoon(PlanetData obj)
            throws Exception {
        // TODO Auto-generated method stub
        int[] planestRashi = new int[13];

        planestRashi[0] = (int) (obj.getSun() / 30.00) + 1;
        planestRashi[1] = (int) (obj.getMoon() / 30.00) + 1;
        planestRashi[2] = (int) (obj.getMarsh() / 30.00) + 1;
        planestRashi[3] = (int) (obj.getMercury() / 30.00) + 1;
        planestRashi[4] = (int) (obj.getJup() / 30.00) + 1;
        planestRashi[5] = (int) (obj.getVenus() / 30.00) + 1;
        planestRashi[6] = (int) (obj.getSat() / 30.00) + 1;
        planestRashi[7] = (int) (obj.getRahu() / 30.00) + 1;
        planestRashi[8] = (int) (obj.getKetu() / 30.00) + 1;
        planestRashi[9] = (int) (obj.getUranus() / 30.00) + 1;
        planestRashi[10] = (int) (obj.getNeptune() / 30.00) + 1;
        planestRashi[11] = (int) (obj.getPluto() / 30.00) + 1;
        planestRashi[12] = (int) (obj.getLagna() / 30.00) + 1;
        planestRashi[12] = planestRashi[1];

        return planestRashi;
    }

    public double[] getTransitPlanetsDegreeArrayForMoon(PlanetData obj) throws Exception {
        double[] _planetDegree = new double[13];
        _planetDegree[0] = obj.getSun();
        _planetDegree[1] = obj.getMoon();
        _planetDegree[2] = obj.getMarsh();
        _planetDegree[3] = obj.getMercury();
        _planetDegree[4] = obj.getJup();
        _planetDegree[5] = obj.getVenus();
        _planetDegree[6] = obj.getSat();
        _planetDegree[7] = obj.getRahu();
        _planetDegree[8] = obj.getKetu();
        _planetDegree[9] = obj.getUranus();
        _planetDegree[10] = obj.getNeptune();
        _planetDegree[11] = obj.getPluto();
        _planetDegree[12] = obj.getLagna();
        _planetDegree[12] = _planetDegree[1];

		/*for(int i=0;i<12;i++)
			Log.e("Planet "+String.valueOf(i+1), String.valueOf(_planetDegree[i]));*/

        return _planetDegree;
    }
}
