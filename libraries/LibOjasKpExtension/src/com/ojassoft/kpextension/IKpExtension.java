package com.ojassoft.kpextension;

public interface IKpExtension {
	
	/* FUNCTIONS FOR  PLANET SIGNIFICATION */
	/**
	 * This function  return the planet Signification for level 1 
	 * @param plnt
	 * @param planetDegree
	 * @param cuspDegree
	 * @return int (bhava number)
	 */
	 int getPlanetSignificationLevel1(int plnt,double[] planetDegree,double [] cuspDegree);
	
	 /**
	  * This function  return the planet Signification for level 2
	  * @param planetDegree
	  * @param cuspDegree
	  * @return int (bhava number)
	  */
	 int getPlanetSignificationLevel2(double planetDegree,double [] cuspDegree);
	 
	 /**
	  * This function  return the planet Signification for level 3
	  * @param planetDegree
	  * @param cuspDegree
	  * @return int[] (array bhava number)
	  */
	 int[] getPlanetSignificationLevel3(double planetDegree,double [] cuspDegree);
	 
	 /**
	  * This function  return the planet Signification for level 3
	  * @param plntName
	  * @param cuspDegree
	  * @return int[] (array bhava number)
	  */
	 int[] getPlanetSignificationLevel4(int plntName,double [] cuspDegree);
	 
	/* END OF PLANET SIGNIFICATION  FUNCTIONS*/
	 
	/* FUNCTIONS FOR  HOUSE SIGNIFICATION */
	 /**
	  * THIS FUNCTION IS USED TO RETURN CUSP LEVEL 1 PLANETS (IN NUMBER 0-8) 
	  * @param cuspNumber
	  * @param planetDegree
	  * @param cuspDegree
	  * @return planets
	  */
	 int [] getHouseSignificatorLevel1(int cuspNumber,double[] planetDegree,double[] cuspDegree);
	 
	 /**
	  * THIS FUNCTION IS USED TO RETURN CUSP LEVEL 2 PLANETS (IN NUMBER 0-8) 
	  * @param cuspNumber
	  * @param planetDegree
	  * @param cuspDegree
	  * @return planets
	  */
	 int [] getHouseSignificatorLevel2(int cuspNumber,double[] planetDegree,double[] cuspDegree);
	 
	 /**
	  * THIS FUNCTION IS USED TO RETURN CUSP LEVEL 3 PLANETS (IN NUMBER 0-8) 
	  * @param cuspDegree
	  * @param planetDegree
	  * @return planets
	  */
	 int [] getHouseSignificatorLevel3(double cuspDegree,double[] planetDegree);
	 /**
	  * THIS FUNCTION IS USED TO RETURN CUSP LEVEL 3 PLANET (IN NUMBER 0-8) 
	  * @param cuspDegree
	  * @return planet number
	  */
	 int  getHouseSignificatorLevel4(double cuspDegree);
	 
	/* END  HOUSE SIGNIFICATION FUNCTIONS */  
	 
	/* FUNCTIONS FOR  NAKSHTRA NADI */	 
	 /**
	  * THIS FUNCTION RETURN THE NADI OF THE PLANET
	  * @param plntNumber
	  * @param planetDegree
	  * @param array of cuspDegree
	  * @return nadi of planet
	  */
	 int[] getPlanetNadi(int plntNumber,double []planetDegree,double[] cuspDegree);
	 
	 /**
	  * THIS FUNCTION RETURN THE NADI OF THE PLANET STAR LORD(NAKSHTRA LORD)
	  * @param plntNumber
	  * @param array of planetDegree
	  * @param array of cuspDegree
	  * @return nadi of planet
	  */
	 int[] getPlanetStarLordNadi(int plntNumber,double[] planetDegree,double[] cuspDegree);
	 
	 /**
	  * THIS FUNCTION RETURN THE NADI OF THE PLANET SUB LORD
	  * @param plntNumber
	  * @param array of planetDegree
	  * @param array of cuspDegree
	  * @return nadi of planet
	  */
	 int[] getPlanetSubLordNadi(int plntNumber,double[] planetDegree,double[] cuspDegree);
	// int[] getRahuOrKetuNadi(int plntNumber,double planetDegree,double[] cuspDegree);
	 /*END NAKSHTRA NADI FUNCTIONS */
	 
	 /* MISC FUNCTIONS */	 
	 
	 /**
	  * THIS FUNCTION RETURN THE ONWER OF THE HOUSE(RASHI OF THE HOUSE)
	  * @param degree
	  * @return  PLANET NUMBER(0-8)
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
	  * @return  PLANET NUMBER(0-8)
	  */
	 int getSubLord(double d);
	 
	 /**
	  * THIS FUNCTION RETUNR THE SUB SUB LOARD OF THE PASSED PLANET/CUSP DEGREE
	  * @param degree
	  * @return  PLANET NUMBER(0-8)
	  */
	 int getSubSubLord(double d);
	 
	 /**
	  * THIS FUNCTION RETUNR THE HOUSES ON WHICH  PLANET RASHIS OCCUPIES 
	  * @param planet number
	  * @param cusp degree array
	  * @return  HOUSE NUMBER(1-12)
	  */
	 int [] getHouseOwned(int plntNumber,double[] cuspDegree); 
	 
	 /**
	  * THIS FUNCTION RETUNR THE HOUSE THAT PLANET OCCUPIE 
	  * @param planet degree
	  * @param cusp degree array
	  * @return  HOUSE NUMBER(1-12)
	  */
	 int  getHouseOccupied(double planetDegree,double[] cuspDegree);
	 
	
	 
	 /**
	  * THIS FUNCTION IS USED TO RETURN  PLANETS (IN NUMBER 0-8) IN CUSP 
	  * @param cuspNumber
	  * @param planetDegree
	  * @param cuspDegree
	  * @return planets
	  */
	 int [] getPlanetsInCusp(int cuspNumber,double[]planetDegree,double[] cuspDegree);
	 
	 /**
	  * THIS FUNCTION IS USED TO RETURN  PLANETS (IN NUMBER 0-8) IN THE RASHI
	  * @param rashiNumber
	  * @param planetsDegree
	  * @return number of planets
	  */
	 int [] getPlanetsInRashi(int rashiNumber,double[] planetsDegree);
	 int[] getHousesInPlanetRashi(int plntNumber, double[] cuspDegree);
	/* END MISC FUNCTIONS */		 
	 
	 /* FUNCTIONS FOR CUSPAL INTERLINKS */ 
	 /**
	  * THIS FUNCTION RETUNR THE  TYPE 1 CUSPAL INTERLINK 
	  * @param cuspNumber
	  * @param planetDegree
	  * @param cuspDegree
	  * @return int[] (array of bhava number)
	  */
	 int [] getKP_CIL_Type1(int cuspNumber,double[] planetDegree,double[] cuspDegree);
	
	 /**
	  * THIS FUNCTION RETUNR THE  TYPE 2 CUSPAL INTERLINK 
	  * @param cuspNumber
	  * @param planetDegree
	  * @param cuspDegree
	  * @return int[] (array of bhava number)
	  */
	 int [] getKP_CIL_Type2(int cuspNumber,double[] planetDegree,double[] cuspDegree);
	 /**
	  * THIS FUNCTION RETUNR THE  TYPE 3 CUSPAL INTERLINK 
	  * @param cuspNumber
	  * @param planetDegree
	  * @param cuspDegree
	  * @return int[] (array of bhava number)
	  */
	 int [] getKP_CIL_Type3(int cuspNumber,double[] planetDegree,double[] cuspDegree);
	 /**
	  * THIS FUNCTION RETUNR THE  TYPE 4 CUSPAL INTERLINK 
	  * @param cuspNumber
	  * @param planetDegree
	  * @param cuspDegree
	  * @return int ( bhava number:1-12)
	  */
	 int  getKP_CIL_Type4(int cuspNumber,double[] planetDegree,double[] cuspDegree);
	 /* END CUSPAL INTERLINKS*/
	 /* FUNCTION PLANET STRENGTH */
	 //int calculatePlanetStrength(int plntNumber,double[] planetDegree);
	 /**
		 * This function return the planet strength array(sun-ketu)
		 * @param plntDegree
		 * @return int array(contain the planet strenght (1[strong] or 0[weak]
		 */
	 int[] getPlanetStrength(double[] plntDegree);
	/* END PLANET STRENGTH FUNCTION*/
	 
	 /*FUNCTION PLANET INTERLINK */
	 
	 /**
	  * THIS FUNCTION RETURN THE PLANET INTERLINK TYPE 1
	  * @param plntNumber
	  * @param planetDegree
	  * @param cuspDegree
	  * @return int[](array of planet number)
	  */
	 int [] getPlanetInterlinkPotentialType1(int plntNumber,double[] planetDegree,double[] cuspDegree);
	 
	 /**
	  * THIS FUNCTION RETURN THE PLANET INTERLINK TYPE 2
	  * @param plntNumber
	  * @param planetDegree
	  * @param cuspDegree
	  * @return int[](array of planet number)
	  */
	 int [] getPlanetInterlinkPotentialType2(int plntNumber,double[] planetDegree,double[] cuspDegree);
	
	 /**
	  * THIS FUNCTION RETURN THE PLANET INTERLINK TYPE 3
	  * @param plntNumber
	  * @param planetDegree
	  * @param cuspDegree
	  * @return int[](array of planet number)
	  */
	 int [] getPlanetInterlinkPotentialType3(int plntNumber,double[] planetDegree,double[] cuspDegree);
	
	 /**
	  * THIS FUNCTION RETURN THE PLANET INTERLINK TYPE 4
	  * @param plntNumber
	  * @param planetDegree
	  * @param cuspDegree
	  * @return int[](array of planet number)
	  */
	 int [] getPlanetInterlinkPotentialType4(int plntNumber,double[] planetDegree,double[] cuspDegree);
	
	 /* END PLANET INTERLINK FUNCTION */ 
//START 4 STEP TYPE FUNCTIONS
	 
	 /**
	  * THIS FUNCTION RETURN THE OWNER AND OCCUPANT OF THE PLANET
	  * @param plntNumber
	  * @param planetDegree
	  * @param cuspDegree
	  * @return int[](array of house the planet related)
	  */
	 int[] get4StepType1(int plntNumber,double[] plntDegree,double[] cuspDegree);
	 /**
	  * THIS FUNCTION RETURN THE OWNER AND OCCUPANT OF START LORD THE PLANET
	  * @param plntNumber
	  * @param planetDegree
	  * @param cuspDegree
	  * @return int[](array of house the planet related)
	  */
	 int[] get4StepType2(int plntNumber,double[] plntDegree,double[] cuspDegree);
	 
	 /**
	  * THIS FUNCTION RETURN THE OWNER AND OCCUPANT OF SUB LORD THE PLANET
	  * @param plntNumber
	  * @param planetDegree
	  * @param cuspDegree
	  * @return int[](array of house the planet related)
	  */
	 int[] get4StepType3(int plntNumber,double[] plntDegree,double[] cuspDegree);
	 
	 /**
	  * THIS FUNCTION RETURN THE OWNER AND OCCUPANT OF SUB SUB LORD THE PLANET
	  * @param plntNumber
	  * @param planetDegree
	  * @param cuspDegree
	  * @return int[](array of house the planet related)
	  */
	 int[] get4StepType4(int plntNumber,double[] plntDegree,double[] cuspDegree);
	 //END 4 STEP TYPE FUNCTIONS
	 
	 /**************************************/
	 //THIS FUNCTION RETURN KHULLAR CUSPAL INTERLINK
	 
	 /**
	  * THIS FUNCTION RETURN THE ARRAY OF CUSP LINKED TO THE  PLANET 
	  * @param plntNumber
	  * @param cuspDegree
	  * @return int[](array of cusp )
	  */
	 int[] getKCILType1(int plntNumber,double[] cuspDegree,double[] plntDegree);

	 /**
	  * THIS FUNCTION RETURN THE ARRAY OF CUSP LINKED TO STAR LORD OF THE  PLANET 
	  * @param plntNumber
	  * @param cuspDegree
	  * @param plntDegree
	  * @return int[](array of cusp )
	  */
	 int[] getKCILType2(int plntNumber,double[] cuspDegree,double[] plntDegree);
	 /**
	  * THIS FUNCTION RETURN THE ARRAY OF CUSP LINKED TO SUB LORD OF THE  PLANET 
	  * @param plntNumber
	  * @param cuspDegree
	  * @param plntDegree
	  * @return int[](array of cusp )
	  */
	 int[] getKCILType3(int plntNumber,double[] cuspDegree,double[] plntDegree);
	 
	 /**
	  * THIS FUNCTION RETURN THE ARRAY OF CUSP LINKED TO SUB SUB LORD OF THE  PLANET 
	  * @param plntNumber
	  * @param cuspDegree
	  * @param plntDegree
	  * @return int[](array of cusp )
	  */
	 int[] getKCILType4(int plntNumber,double[] cuspDegree,double[] plntDegree);
	 
	
	int  distanceOfHousePlanet2PositedFromPlanet1(double planet1, double planet2);
}
