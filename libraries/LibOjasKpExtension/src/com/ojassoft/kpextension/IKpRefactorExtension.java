package com.ojassoft.kpextension;

public interface IKpRefactorExtension {
	/* MISC FUNCTIONS */

	/**
	 * THIS FUNCTION RETURN THE ONWER OF THE HOUSE(RASHI OF THE HOUSE)
	 * @param house degree
	 * @return PLANET NUMBER(0-8)
	 */
	int getHouseOwner(double houseDegree);

	/**
	 * THIS FUNCTION RETUNR THE STAR LOARD OF THE PASSED PLANET/CUSP DEGREE
	 * @param degree
	 * @return PLANET NUMBER(0-8)
	 */
	int getStarLord(double d);

	/**
	 * THIS FUNCTION RETUNR THE SUB LOARD OF THE PASSED PLANET/CUSP DEGREE
	 * @param degree
	 * @return PLANET NUMBER(0-8)
	 */
	int getSubLord(double d);

	/**
	 * THIS FUNCTION RETUNR THE SUB SUB LOARD OF THE PASSED PLANET/CUSP DEGREE
	 * @param degree
	 * @return PLANET NUMBER(0-8)
	 */
	int getSubSubLord(double d);

	/**
	 * THIS FUNCTION RETUNR THE HOUSES ON WHICH PLANET RASHIS OCCUPIES
	 * 
	 * @param planet
	 *            number
	 * @return HOUSE NUMBER(1-12)
	 */
	int[] getHouseOwned(int plntNumber);

	/**
	 * THIS FUNCTION RETUNR THE HOUSE THAT PLANET OCCUPIE
	 * 
	 * @param planet
	 *            degree
	 * @return HOUSE NUMBER(1-12)
	 */
	int getHouseOccupied(double planetDegree);

	/**
	 * THIS FUNCTION IS USED TO RETURN PLANETS (IN NUMBER 0-8) IN CUSP
	 * @param cuspNumber
	 * @return planets
	 */
	int[] getPlanetsInCusp(int cuspNumber);

	/**
	 * THIS FUNCTION IS USED TO RETURN PLANETS (IN NUMBER 0-8) IN THE RASHI
	 * @param rashiNumber
	 * @return number of planets
	 */
	int[] getPlanetsInRashi(int rashiNumber);

	int[] getHousesInPlanetRashi(int plntNumber);

	/* END MISC FUNCTIONS */
	// THIS FUNCTION RETURN KHULLAR CUSPAL INTERLINK

	/**
	 * THIS FUNCTION RETURN THE ARRAY OF CUSP LINKED TO THE PLANET
	 * @param plntNumber
	 * @return int[](array of cusp )
	 */
	int[] getKCILType1(int plntNumber);

	/**
	 * THIS FUNCTION RETURN THE ARRAY OF CUSP LINKED TO STAR LORD OF THE PLANET
	 * 
	 * @param plntNumber
	 * @return int[](array of cusp )
	 */
	int[] getKCILType2(int plntNumber);

	/**
	 * THIS FUNCTION RETURN THE ARRAY OF CUSP LINKED TO SUB LORD OF THE PLANET
	 * 
	 * @param plntNumber
	 * @return int[](array of cusp )
	 */
	int[] getKCILType3(int plntNumber);

	/**
	 * THIS FUNCTION RETURN THE ARRAY OF CUSP LINKED TO SUB SUB LORD OF THE
	 * PLANET
	 * 
	 * @param plntNumber
	 * @return int[](array of cusp )
	 */
	int[] getKCILType4(int plntNumbere);

	// END KHULLAR CUSPAL INTERLINK

	// START 4 STEP TYPE FUNCTIONS

	/**
	 * THIS FUNCTION RETURN THE OWNER AND OCCUPANT OF THE PLANET
	 * 
	 * @param plntNumber
	 * @return int[](array of house the planet related)
	 */
	//int[] get4StepType1(int plntNumber);

	/**
	 * THIS FUNCTION RETURN THE OWNER AND OCCUPANT OF START LORD THE PLANET
	 * 
	 * @param plntNumber
	 * @return int[](array of house the planet related)
	 */
	//int[] get4StepType2(int plntNumber);

	/**
	 * THIS FUNCTION RETURN THE OWNER AND OCCUPANT OF SUB LORD THE PLANET
	 * 
	 * @param plntNumber
	 * @return int[](array of house the planet related)
	 */
	//int[] get4StepType3(int plntNumber);

	/**
	 * THIS FUNCTION RETURN THE OWNER AND OCCUPANT OF SUB SUB LORD THE PLANET
	 * 
	 * @param plntNumber
	 * @return int[](array of house the planet related)
	 */
	//int[] get4StepType4(int plntNumber);

	// END 4 STEP TYPE FUNCTIONS

	// FUNCTIONS FOR CUSPAL INTERLINKS
	/**
	 * THIS FUNCTION RETUNR THE TYPE 1 CUSPAL INTERLINK
	 * 
	 * @param cuspNumber
	 * @return int[] (array of bhava number)
	 */
	int[] getKP_CIL_Type1(int cuspNumber);

	/**
	 * THIS FUNCTION RETUNR THE TYPE 2 CUSPAL INTERLINK
	 * 
	 * @param cuspNumber
	 * @return int[] (array of bhava number)
	 */
	int[] getKP_CIL_Type2(int cuspNumber);

	/**
	 * THIS FUNCTION RETUNR THE TYPE 3 CUSPAL INTERLINK
	 * 
	 * @param cuspNumber
	 * @return int[] (array of bhava number)
	 */
	int[] getKP_CIL_Type3(int cuspNumber);

	/**
	 * THIS FUNCTION RETUNR THE TYPE 4 CUSPAL INTERLINK
	 * 
	 * @param cuspNumber
	 * @return int ( bhava number:1-12)
	 */
	int getKP_CIL_Type4(int cuspNumber);

	// END CUSPAL INTERLINKS

	// FUNCTIONS FOR NAKSHTRA NADI
	/**
	 * THIS FUNCTION RETURN THE NADI OF THE PLANET
	 * 
	 * @param plntNumber
	 * @return nadi of planet
	 */
	int[] getPlanetNadi(int plntNumber);

	/**
	 * THIS FUNCTION RETURN THE NADI OF THE PLANET STAR LORD(NAKSHTRA LORD)
	 * 
	 * @param plntNumber
	 * @return nadi of planet
	 */
	int[] getPlanetStarLordNadi(int plntNumber);

	/**
	 * THIS FUNCTION RETURN THE NADI OF THE PLANET SUB LORD
	 * 
	 * @param plntNumber
	 * @return nadi of planet
	 */
	int[] getPlanetSubLordNadi(int plntNumber);

	// END NAKSHTRA NADI FUNCTIONS

	// FUNCTIONS FOR HOUSE SIGNIFICATION
	/**
	 * THIS FUNCTION IS USED TO RETURN CUSP LEVEL 1 PLANETS (IN NUMBER 0-8)
	 * 
	 * @param cuspNumber
	 * @return planets
	 */
	int[] getHouseSignificatorLevel1(int cuspNumber);

	/**
	 * THIS FUNCTION IS USED TO RETURN CUSP LEVEL 2 PLANETS (IN NUMBER 0-8)
	 * 
	 * @param cuspNumber
	 * @return planets
	 */
	int[] getHouseSignificatorLevel2(int cuspNumber);

	/**
	 * THIS FUNCTION IS USED TO RETURN CUSP LEVEL 3 PLANETS (IN NUMBER 0-8)
	 * 
	 * @param cuspDegree
	 * @return planets
	 */
	int[] getHouseSignificatorLevel3(double cuspDegree);

	/**
	 * THIS FUNCTION IS USED TO RETURN CUSP LEVEL 3 PLANET (IN NUMBER 0-8)
	 * 
	 * @param cuspDegree
	 * @return planet number
	 */
	int getHouseSignificatorLevel4(double cuspDegree);

	// END HOUSE SIGNIFICATION FUNCTIONS

	/**
	 * This function return the planet strength array(sun-ketu)
	 * 
	 * @return int array(contain the planet strenght (1[strong] or 0[weak]
	 */
	int[] getPlanetStrength();

	// FUNCTIONS FOR PLANET SIGNIFICATION
	/**
	 * This function return the planet Signification for level 1
	 * 
	 * @param plnt
	 * @return int (bhava number)
	 */
	int getPlanetSignificationLevel1(int plnt);

	/**
	 * This function return the planet Signification for level 2
	 * 
	 * @param planetDegree
	 * @return int (bhava number)
	 */
	int getPlanetSignificationLevel2(double planetDegree);

	/**
	 * This function return the planet Signification for level 3
	 * 
	 * @param planetDegree
	 * @return int[] (array bhava number)
	 */
	int[] getPlanetSignificationLevel3(double planetDegree);

	/**
	 * This function return the planet Signification for level 3
	 * 
	 * @param plntName
	 * @return int[] (array bhava number)
	 */
	int[] getPlanetSignificationLevel4(int plntName);

	// END OF PLANET SIGNIFICATION FUNCTIONS
	/**
	 * This function is used to return position of planet2 from planet1
	 * @param planet1,planet2
	 * @return house number
	 * @author Bijendra(15-may-2013)
	 */
	int  distanceOfHousePlanet2PositedFromPlanet1(double planet1, double planet2);
	
	/**
	 * This function is used to return planets  has aspect on rahu/ketu
	 * according to passed rahu/ketu degree
	 * @param degRahuKetu
	 * @return planets
	 * @author Bijendra(15-may-2013)
	 */
	int[] getPlanetsHaveAspectOnRahuKetu(double degRahuKetu);
	
	//NEW FUNCTIONS FOR 4-STEP KP-ADDED BY BIJENDRA ON 18-MAY-13	
	/**
	 * THIS FUNCTION RETURN THE OWNER AND OCCUPANT OF THE PLANET
	 * 
	 * @param plntNumber
	 * @return int[](array of house the planet related)
	 */
	int[] get4StepTypeN1(int plntNumber);
	
	/**
	 * THIS FUNCTION RETURN THE OWNER AND OCCUPANT OF START LORD THE PLANET
	 * 
	 * @param plntNumber
	 * @return int[](array of house the planet related)
	 */
	int[] get4StepTypeN2(int plntNumber);
	
	/**
	 * THIS FUNCTION RETURN THE OWNER AND OCCUPANT OF SUB LORD THE PLANET
	 * 
	 * @param plntNumber
	 * @return int[](array of house the planet related)
	 */
	int[] get4StepTypeN3(int plntNumber);
	
	/**
	 * THIS FUNCTION RETURN THE OWNER AND OCCUPANT OF SUB SUB LORD THE PLANET
	 * 
	 * @param plntNumber
	 * @return int[](array of house the planet related)
	 */
	int[] get4StepTypeN4(int plntNumber);
	//END
	//NEW MISC FUNCTIONS -ADDED BY BIJENDRA ON 18-MAY-13
	boolean checkPlanetIsStarLordOfOtherPlanet(int plntNumber);
	boolean isPlanetInItsOwnStar(int plntNumber);
	//END
	
	/**
	 * This function is used to return number of planets making Conjunction with passed planet
	 * @param planetNumber
	 * @return int array
	 * @author Bijendra(20-sep-13)
	 */
	int [] getPlanetConjunctionIn4Step(int planetNumber);//-ADDED BY BIJENDRA ON 20-SEP-13
	
	/**
	 * This function is used to return cupsp number making Conjunction with passed planet
	 * @param planetNumber
	 * @return int
	 * @author BijendraBijendra(20-sep-13)
	 */
	int  getPlanetConjunctionWithCuspIn4Step(int planetNumber);//-ADDED BY BIJENDRA ON 23-SEP-13
	
	int [] getPlanetsAspectToPlanetIn4Step(int planetNumber);//-ADDED BY BIJENDRA ON 23-SEP-13
	int[] getRahuKetuAspectOnCuspIn4Step(int plntNumber);//-ADDED BY BIJENDRA ON 23-SEP-13
	int[] getPlanetAspectOnCuspIn4Step(int plntNumber);//-ADDED BY BIJENDRA ON 23-SEP-13
	
	
	/**
	 * THIS FUNCTION RETUNR THE TYPE 1 CUSPAL INTERLINK
	 * 
	 * @param cuspNumber
	 * @return int[] (array of bhava number)
	 */
	int[] getKB_CIL_Type1(int cuspNumber) ;//-ADDED BY BIJENDRA ON 18-OCT-13
	
	/**
	 * THIS FUNCTION RETUNR THE TYPE 2 CUSPAL INTERLINK
	 * 
	 * @param cuspNumber
	 * @return int[] (array of bhava number)
	 */
	int[] getKB_CIL_Type2(int cuspNumber);//-ADDED BY BIJENDRA ON 18-OCT-13
	
	/**
	 * THIS FUNCTION RETUNR THE TYPE 3 CUSPAL INTERLINK
	 * 
	 * @param cuspNumber
	 * @return int[] (array of bhava number)
	 */
	int[] getKB_CIL_Type3(int cuspNumber) ;//-ADDED BY BIJENDRA ON 18-OCT-13
	
	/**
	 * THIS FUNCTION RETUNR THE TYPE 4 CUSPAL INTERLINK
	 * 
	 * @param cuspNumber
	 * @return int ( bhava number:1-12)
	 */
	int getKB_CIL_Type4(int cuspNumber);//-ADDED BY BIJENDRA ON 18-OCT-13
	

}
