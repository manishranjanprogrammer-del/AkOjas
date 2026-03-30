package com.ojassoft.kpextension;


/**
 * This class is used to calculate planet signification from 1st to 4th level
 * 
 * @author Bijendra
 * @date 26-feb2013
 * 
 */
public class CKpExtension implements IKpExtension {

	/**
	 * Level 1. House occupied by the star lord of the planet This function
	 * return first level planet signification
	 * 
	 * @param planet
	 *            number
	 * @param array
	 *            of planet dergee
	 * @param array
	 *            of cusp dergee
	 * @return level1 bhav
	 */

	public int getPlanetSignificationLevel1(int plnt, double[] planetDegree,
			double[] cuspDegree) {

		int plntNakLordInBhava = -1;

		try {
			// GET THE PLANET NAK LORD
			//int plntNakLord = CUtils.getPlanetNakLord(planetDegree[plnt]);
			int plntNakLord = getStarLord(planetDegree[plnt]);

			int j = 0;
			for (int i = 0; i < 12; i++) {
				j = i + 1;
				if (j > 11)
					j = 0;
				plntNakLordInBhava = CUtilsKPExtension.getPlanetInBhav(cuspDegree[j],
						cuspDegree[i], i, planetDegree[plntNakLord]/*
																	 * PLANET
																	 * NAK LORD
																	 * DEGREE
																	 */);
				if (plntNakLordInBhava > 0)
					break;
			}
		} catch (Exception e) {

		}

		return plntNakLordInBhava;
	}

	/**
	 * Level 2. House occupied by the planet itself This function return second
	 * level planet signification
	 * 
	 * @param planet
	 *            degree
	 * @param array
	 *            of cusp dergee
	 * @return level2 bhav
	 */
	public int getPlanetSignificationLevel2(double planetDegree,
			double[] cuspDegree) {
		int plntNakLordInBhava = -1;
		try {
			int j = 0;
			for (int i = 0; i < 12; i++) {
				j = i + 1;
				if (j > 11)
					j = 0;
				plntNakLordInBhava = CUtilsKPExtension
						.getPlanetInBhav(cuspDegree[j], cuspDegree[i], i,
								planetDegree/* PLANET NAK LORD DEGREE */);
				if (plntNakLordInBhava > 0)
					break;
			}
		} catch (Exception e) {

		}

		return plntNakLordInBhava;
	}

	/**
	 * Level 3. Houses owned by the star lord of the planet This function return
	 * third level planet signification
	 * 
	 * @param planet
	 *            degree
	 * @param array
	 *            of cusp dergee
	 * @return level3 bhav
	 */
	public int[] getPlanetSignificationLevel3(double planetDegree,
			double[] cuspDegree) {
		
		int[] starLordInCusp =null;

		try {
			
			int plntNakLord = getStarLord(planetDegree);
			starLordInCusp=getHousesInPlanetRashi(plntNakLord,cuspDegree);
			
			} catch (Exception e) {

		}

		return starLordInCusp;
	}

	/**
	 * Level 4. Houses owned by the planet itself This function return second
	 * level planet signification
	 * 
	 * @param planet
	 *            name(number)
	 * @param array
	 *            of cusp dergee
	 * @return level4 bhav
	 */
	public int[] getPlanetSignificationLevel4(int plntName, double[] cuspDegree) {
	
		int[] cusp_owned_by_planet_itself=null;

		try {
			cusp_owned_by_planet_itself=getHousesInPlanetRashi(plntName,  cuspDegree);
			
		} catch (Exception e) {

		}

		return cusp_owned_by_planet_itself;
	}

	/**
	  * THIS FUNCTION RETURN THE ONWER OF THE HOUSE(RASHI OF THE HOUSE)
	  * @param degree
	  * @return  PLANET NUMBER(0-8)
	  */
	public int getHouseOwner(double houseDegree) {
		// TODO Auto-generated method stub
		return GlobalVariablesKPExtension.RASHI_TO_PLANET[(int) ((houseDegree / 30.00) + 1)];
	}

	/**
	  * THIS FUNCTION RETUNR THE STAR LOARD OF THE PASSED PLANET/CUSP DEGREE
	  * @param degree
	  * @return PLANET NUMBER(0-8)
	  */
	public int getStarLord(double d) {
		// TODO Auto-generated method stub
		int a, b, c, f, i = 0;

		f = (int) (d / 30.0);
		a = (int) (d / 120.0);
		d -= a * 120.0;
		a = (int) (d * 3.0 / 40.0);
		d -= a * 40.0 / 3.0;
		d *= 9;
		for (b = 0; b < 9; b++) {
			i = a + b;
			if (i >= 9)
				i -= 9;
			if (GlobalVariablesKPExtension.y1[i] <= d)
				d -= GlobalVariablesKPExtension.y1[i];
			else
				break;
		}
		b = i;
		d = d / GlobalVariablesKPExtension.y1[b] * (40.0 / 3.0);
		d *= 9;
		for (c = 0; c < 9; c++) {
			i = b + c;
			if (i >= 9)
				i -= 9;
			if (GlobalVariablesKPExtension.y1[i] <= d)
				d -= GlobalVariablesKPExtension.y1[i];
			else
				break;
		}
		c = i;

		// RETURN NAK LORAD
		return GlobalVariablesKPExtension.PLANET_NAKSHTRA_LORD[a];
	}

	/**
	  * THIS FUNCTION RETUNR THE SUB LOARD OF THE PASSED PLANET/CUSP DEGREE
	  * @param degree
	  * @return  PLANET NUMBER(0-8)
	  */
	public int getSubLord(double d) {
		// TODO Auto-generated method stub
		int a, b, c, f, i = 0;

		f = (int) (d / 30.0);
		a = (int) (d / 120.0);
		d -= a * 120.0;
		a = (int) (d * 3.0 / 40.0);
		d -= a * 40.0 / 3.0;
		d *= 9;
		for (b = 0; b < 9; b++) {
			i = a + b;
			if (i >= 9)
				i -= 9;
			if (GlobalVariablesKPExtension.y1[i] <= d)
				d -= GlobalVariablesKPExtension.y1[i];
			else
				break;
		}
		b = i;
		d = d / GlobalVariablesKPExtension.y1[b] * (40.0 / 3.0);
		d *= 9;
		for (c = 0; c < 9; c++) {
			i = b + c;
			if (i >= 9)
				i -= 9;
			if (GlobalVariablesKPExtension.y1[i] <= d)
				d -= GlobalVariablesKPExtension.y1[i];
			else
				break;
		}
		c = i;

		// RETURN SUB LORAD
		return GlobalVariablesKPExtension.PLANET_NAKSHTRA_LORD[b];
	}

	public int getSubSubLord(double d) {
		// TODO Auto-generated method stub
		int a, b, c, f, i = 0;

		f = (int) (d / 30.0);
		a = (int) (d / 120.0);
		d -= a * 120.0;
		a = (int) (d * 3.0 / 40.0);
		d -= a * 40.0 / 3.0;
		d *= 9;
		for (b = 0; b < 9; b++) {
			i = a + b;
			if (i >= 9)
				i -= 9;
			if (GlobalVariablesKPExtension.y1[i] <= d)
				d -= GlobalVariablesKPExtension.y1[i];
			else
				break;
		}
		b = i;
		d = d / GlobalVariablesKPExtension.y1[b] * (40.0 / 3.0);
		d *= 9;
		for (c = 0; c < 9; c++) {
			i = b + c;
			if (i >= 9)
				i -= 9;
			if (GlobalVariablesKPExtension.y1[i] <= d)
				d -= GlobalVariablesKPExtension.y1[i];
			else
				break;
		}
		c = i;

		// RETURN SUB SUB LORAD
		return GlobalVariablesKPExtension.PLANET_NAKSHTRA_LORD[c];
	}

	 /**
	  * THIS FUNCTION RETUNR THE HOUSES ON WHICH  PLANET RASHIS OCCUPIES 
	  * @param planet number
	  * @param cusp degree array
	  * @return  HOUSE NUMBER(1-12)
	  */
	public int[] getHouseOwned(int plntNumber, double[] cuspDegree) {
		int plntNakLordInBhava = -1;
		int cusp_owned_by_planet_itself_Index = 0, plntRashiArrayIndex = 0;
		int[] cusp_owned_by_planet_itself = new int[4];

		try {
			for (int k = 0; k < cusp_owned_by_planet_itself.length; k++)
				cusp_owned_by_planet_itself[k] = 0;

			int[] plntNakLordRashi = new int[2];
			plntNakLordRashi[0] = GlobalVariablesKPExtension.PLANET_RASHI[plntNumber][0];
			plntNakLordRashi[1] = GlobalVariablesKPExtension.PLANET_RASHI[plntNumber][1];

			do {
				double rashiDegree = plntNakLordRashi[plntRashiArrayIndex] * 30.00;
				int j = 0;
				for (int i = 0; i < 12; i++) {
					j = i + 1;
					if (j > 11)
						j = 0;
					plntNakLordInBhava = CUtilsKPExtension.getPlanetInBhav(cuspDegree[j],
							cuspDegree[i], i, rashiDegree/* RASHI DEGREE */);
					if (plntNakLordInBhava > 0) {
						cusp_owned_by_planet_itself[cusp_owned_by_planet_itself_Index] = plntNakLordInBhava;
						cusp_owned_by_planet_itself_Index++;
						plntNakLordInBhava = 0;

					}
				}
				
				plntRashiArrayIndex++;				
			} while (plntNakLordRashi[plntRashiArrayIndex] != 0);
		} catch (Exception e) {

		}

		return cusp_owned_by_planet_itself;
	}

	public int getHouseOccupied(double planetDegree, double[] cuspDegree) {
		int plntNakLordInBhava = -1;
		try {
			int j = 0;
			for (int i = 0; i < 12; i++) {
				j = i + 1;
				if (j > 11)
					j = 0;
				plntNakLordInBhava = CUtilsKPExtension.getPlanetInBhav(cuspDegree[j],
						cuspDegree[i], i, planetDegree);
				if (plntNakLordInBhava > 0)
					break;
			}
		} catch (Exception e) {

		}

		return plntNakLordInBhava;
	}

	/**
	  * THIS FUNCTION IS USED TO RETURN CUSP LEVEL 2 PLANETS (IN NUMBER 0-8) 
	  * @param cuspNumber
	  * @param planetDegree
	  * @param cuspDegree
	  * @return planets
	  */
	public int[] getHouseSignificatorLevel2(int cuspNumber,
			double[] planetDegree, double[] cuspDegree) {
		return getPlanetsInCusp(cuspNumber, planetDegree, cuspDegree);

	}

	/**
	  * THIS FUNCTION IS USED TO RETURN  PLANETS (IN NUMBER 0-8) IN CUSP 
	  * @param cuspNumber
	  * @param planetDegree
	  * @param cuspDegree
	  * @return planets
	  */
	public int[] getPlanetsInCusp(int cuspNumber, double[] planetDegree,
			double[] cuspDegree) {
		int[] planetsInHouse = new int[7];
		int cusp1Index = -1, cusp2Index = -1;
		int planetIndex = 0, planetNo = -1;
		for (int i = 0; i < planetsInHouse.length; i++)
			planetsInHouse[i] = -1;

		if (cuspNumber == 11) {
			cusp1Index = 11;
			cusp2Index = 0;
		} else {
			cusp1Index = cuspNumber;
			cusp2Index = cuspNumber + 1;
		}

		for (int j = 0; j < 9; j++) {
			planetNo = CUtilsKPExtension.getPlanetInBhav(cuspDegree[cusp2Index],
					cuspDegree[cusp1Index], cuspNumber, planetDegree[j]);
			if (planetNo > 0) {
				planetsInHouse[planetIndex] = j;
				planetIndex++;
				planetNo = -1;

			}

		}

		return CUtilsKPExtension.removeValueFromIntArray(-1,planetsInHouse);
		//return planetsInHouse;
	}

	 /**
	  * THIS FUNCTION IS USED TO RETURN CUSP LEVEL 3 PLANET (IN NUMBER 0-8) 
	  * @param cuspDegree
	  * @return planet number
	  */
	public int getHouseSignificatorLevel4(double cuspDegree) {
		// TODO Auto-generated method stub
		return this.getHouseOwner(cuspDegree);
	}

	/**
	  * THIS FUNCTION IS USED TO RETURN CUSP LEVEL 3 PLANETS (IN NUMBER 0-8) 
	  * @param cuspDegree
	  * @param planetDegree
	  * @return planets
	  */
	public int[] getHouseSignificatorLevel3(double cuspDegree,
			double[] planetDegree) {
		int index=-1;
		int[]level3=new int[9];
		int[]naklord=new int[9];
		
		int cuspRashi=(int) (cuspDegree/30+1);
		int plnn=GlobalVariablesKPExtension.RASHI_TO_PLANET[cuspRashi];
				
		
		for(int l=0;l<9;l++)
		{
			level3[l]=-1;
			naklord[l]=this.getStarLord(planetDegree[l]);
		}
		
		for(int i=0;i<9;i++)
			if(plnn==naklord[i])
				level3[++index]=i;
				
		
		return CUtilsKPExtension.removeValueFromIntArray(-1,level3);
	}

	 /**
	  * THIS FUNCTION IS USED TO RETURN  PLANETS (IN NUMBER 0-8) IN THE RASHI
	  * @param rashiNumber
	  * @param planetsDegree
	  * @return number of planets
	  */
	public int[] getPlanetsInRashi(int rashiNumber, double[] planetsDegree) {
		// TODO Auto-generated method stub
		double rashiDegreeEnd = rashiNumber * 30.00;
		double rashiDegreeBegin = rashiDegreeEnd - 30.00;
		int planetIndex = 0;
		int[] planets = new int[7];

		for (int j = 0; j < planets.length; j++)
			planets[j] = -1;

		for (int i = 0; i < planetsDegree.length; i++) {
			if ((rashiDegreeBegin < planetsDegree[i])
					&& (planetsDegree[i] < rashiDegreeEnd))
				planets[planetIndex++] = i;
		}

		return planets;
	}

	 /**
	  * THIS FUNCTION IS USED TO RETURN CUSP LEVEL 1 PLANETS (IN NUMBER 0-8) 
	  * @param cuspNumber
	  * @param planetDegree
	  * @param cuspDegree
	  * @return planets
	  */
	public int[] getHouseSignificatorLevel1(int cuspNumber,
			double[] planetDegree, double[] cuspDegree) {
		int index=-1;
		int[]level1=new int[12];
		int []plntInCusp=null;
		int []naklord=new int[9];
				
		for(int i=0;i<12;i++)
			level1[i]=-1;
		
		for(int i=0;i<9;i++)		
			naklord[i]=getStarLord(planetDegree[i]);
		
		plntInCusp=this.getPlanetsInCusp(cuspNumber,planetDegree,cuspDegree);
		
		if(plntInCusp!=null)
		{
			for(int m=0;m<plntInCusp.length;m++)
			{
				for(int j=0;j<9;j++)
				{ 
					if(plntInCusp[m]==naklord[j])				
						level1[++index]=j;					
					
				}
			}
		}
		return CUtilsKPExtension.removeValueFromIntArray(-1,level1);
		//return level1;
	}

	 /**
	  * THIS FUNCTION RETURN THE NADI OF THE PLANET
	  * @param plntNumber
	  * @param planetDegree
	  * @param array of cuspDegree
	  * @return nadi of planet
	  */
	public int[] getPlanetNadi(int plntNumber, double []planetDegree,
			double[] cuspDegree) {
		int planetNadiLength=0,index=0;
		int houseOccupiedByPlanet;
		int[] planetRashioccupiedHouse=null;
		/*in which house planet is*/
		 houseOccupiedByPlanet=getHouseOccupied(planetDegree[plntNumber],cuspDegree);
		
		/* in which house,the planet rashi are*/
		if((plntNumber==GlobalVariablesKPExtension.RAHU)||(plntNumber==GlobalVariablesKPExtension.KETU))
			planetRashioccupiedHouse=this.getRahuOrKetuNadi(plntNumber, planetDegree, cuspDegree);
		else
			planetRashioccupiedHouse=getHousesInPlanetRashi(plntNumber,cuspDegree);
			//  planetRashioccupiedHouse=getHouseOwned(plntNumber,cuspDegree);
			
		//GET THE LENGTH OF ARRAY
	    for(int i=0;i<planetRashioccupiedHouse.length;i++)
	    	if(planetRashioccupiedHouse[i]>0)
	    		++planetNadiLength;
	    
	    		
	    int []planetNadi=new int[planetNadiLength+1];//ASSIGN THE ARRAY FOR PLANET NADI
	    planetNadi[0]=houseOccupiedByPlanet;
	    if(planetRashioccupiedHouse!=null)
	    {
	    for(int i=0;i<planetRashioccupiedHouse.length;i++)
	    	if(planetRashioccupiedHouse[i]>0)
	    		planetNadi[++index]=planetRashioccupiedHouse[i];
	    }	
	    	
	    //return CUtilsKPExtension.removeDuplicates(planetNadi);
		return planetNadi;
	}

	/**
	  * THIS FUNCTION RETURN THE NADI OF THE PLANET STAR LORD(NAKSHTRA LORD)
	  * @param plntNumber
	  * @param array of planetDegree
	  * @param array of cuspDegree
	  * @return nadi of planet
	  */
	public int[] getPlanetStarLordNadi(int plntNumber, double[] planetDegree,
			double[] cuspDegree) {
		
		//GET PLANET START LORD
		int planetStarlord=getStarLord(planetDegree[plntNumber]);
		int []PlanetStarLordNadi=getPlanetNadi(planetStarlord, planetDegree, cuspDegree);
		
		return PlanetStarLordNadi;
	}

	 /**
	  * THIS FUNCTION RETURN THE NADI OF THE PLANET SUB LORD
	  * @param plntNumber
	  * @param array of planetDegree
	  * @param array of cuspDegree
	  * @return nadi of planet
	  */
	public int[] getPlanetSubLordNadi(int plntNumber, double[] planetDegree,
			double[] cuspDegree) {
		
		//GET SUB LORD OF THE PLANET
		int planetStarlord=getSubLord(planetDegree[plntNumber]);
		int []PlanetStarLordNadi=getPlanetNadi(planetStarlord, planetDegree, cuspDegree);
		
		return PlanetStarLordNadi;
	}

	// NOTE:WE WILL WRITE THE LOGIN LATER,PRESENTLY WE ARE USING HARD CODE VALUES
	public int[] getRahuOrKetuNadi(int plntNumber, double []planetDegree,
			double[] cuspDegree) {
		int []collection=new int[20];
		int index=-1;
		int [] retVal=null;
		
		
		//int []retValue
		// TODO Auto-generated method stub
		for(int i=0;i<collection.length;i++)
			collection[i]=-1;
		// TODO Auto-generated method stub
		//STEP:1=GET THE PLANET IN WHICH IT IS PLACED
		int plntInWhicRashi=getRashiLordOfThePlanetWhereItPlaced(planetDegree[plntNumber]);
		int[]nadi1=getPlanetNadi(plntInWhicRashi, planetDegree, cuspDegree);
		if(nadi1!=null)
		for(int i=0;i<nadi1.length;i++)
			if(nadi1[i]>0)
				collection[++index]=nadi1[i];
		
		//STEP:2=CONJUCTION	
		int []arrayTemp=null;
		int[] plntCon=getConjuction(plntNumber,planetDegree);
		if(plntCon!=null)
		{
			for(int i=0;i<plntCon.length;i++)
			{
			  arrayTemp=getPlanetNadi(plntCon[i], planetDegree, cuspDegree);
			  if(arrayTemp!=null)
			  {
				  for(int j=0;j<arrayTemp.length;j++)
						if(arrayTemp[j]>0)
							collection[++index]=arrayTemp[j];
			  }
			  arrayTemp=null;  
			}
			
		}
		//filter values
		retVal=new int[index+1];
		int indexTemp=-1;
		for(int i=0;i<collection.length;i++)
			if(collection[i]>0)
				retVal[++indexTemp]=collection[i];
				
		
		return retVal;
	}

	 /**
	  * THIS FUNCTION RETUNR THE  TYPE 1 CUSPAL INTERLINK 
	  * @param cuspNumber
	  * @param planetDegree
	  * @param cuspDegree
	  * @return int[] (array of bhava number)
	  */
	public int[] getKP_CIL_Type1(int cuspNumber,
			double[] planetDegree, double[] cuspDegree) {
		int []type1=new int[12];
		
		//STEP-1:GET CUSP SUB LORD(PLANET)
		 int cuspSubLord=getSubLord(cuspDegree[cuspNumber]);//PLANET NUMBER OF CUSP SUB LORD
		 
		
		//STEP-2:GET  STAR LORD OF THE PLANET THAT COMES IN STEP 1
		 int plntStarLord=getStarLord(planetDegree[cuspSubLord]);
		 //System.out.println("plntStarLord>"+ConstGlobalVariables.PlanetNames[plntStarLord]);
		//STEP-3 IDENTIFY THE CUSP THAT HAVE THE SAME SUB LORD PLANET AS PLANET  CAME IN STEP 2
		 int index=-1;
		 int[] arrayCuspSubLord=new int[12];
		 for(int j=0;j<12;j++)
			 arrayCuspSubLord[j]=getSubLord(cuspDegree[j]);
		 for(int i=0;i<cuspDegree.length;i++)
		 {
			 if(arrayCuspSubLord[i]==plntStarLord)
				 type1[++index]=i+1;
		 }
		 
		
		//STEP-4:GET THE SUB LORD OF THE PLANET THAT HAVE COME IN STEP 1
		 int plntSubLord=getSubLord(planetDegree[cuspSubLord]);
		 
		//STEP-5:IDENTIFY THE CUSP THAT HAVE THE SAME SUB LORD PLANET AS PLANET  CAME IN STEP 4
		
		 for(int k=0;k<cuspDegree.length;k++)
			 if(arrayCuspSubLord[k]==plntSubLord)
				 type1[++index]=k+1;
		 
		
		 return CUtilsKPExtension.removeValueFromIntArray(0, type1);
		
	}

	/**
	  * THIS FUNCTION RETUNR THE  TYPE 2 CUSPAL INTERLINK 
	  * @param cuspNumber
	  * @param planetDegree
	  * @param cuspDegree
	  * @return int[] (array of bhava number)
	  */
	public int[]getKP_CIL_Type2(int cuspNumber,
			double[] planetDegree, double[] cuspDegree) {
		// TODO Auto-generated method stub
int []type2=new int[12];
		
		//STEP-1:FIND THE SUB LORD OF THE CUSP
		 int cuspSubLord=getSubLord(cuspDegree[cuspNumber]);//PLANET NUMBER OF CUSP SUB LORD
		 int index=-1;
		 int[] arrayCuspSubLord=new int[12];
		 for(int j=0;j<12;j++)
			 arrayCuspSubLord[j]=getSubLord(cuspDegree[j]);
		 for(int i=0;i<cuspDegree.length;i++)
		 {
			 if(i!=cuspNumber)
				 if(arrayCuspSubLord[i]==cuspSubLord)
					 type2[++index]=i+1;
		 }
		 //STEP:2
		 int plntStarLord=getStarLord(planetDegree[cuspSubLord]);
		 
		 for(int i=0;i<cuspDegree.length;i++)
		 {
			 if(i!=cuspNumber)
				 if(arrayCuspSubLord[i]==plntStarLord)
					 type2[++index]=i+1;
		 }
		 
		 int[] arrayCuspSubSubLord=new int[12];
		 for(int j=0;j<12;j++)
			 arrayCuspSubSubLord[j]=getSubSubLord(cuspDegree[j]);
		 
		 for(int i=0;i<cuspDegree.length;i++)
		 {
			 if(i!=cuspNumber)
				 if(arrayCuspSubSubLord[i]==plntStarLord)
					 type2[++index]=i+1;
		 }
		
		 return CUtilsKPExtension.removeValueFromIntArray(0, type2);
	}

	 /**
	  * THIS FUNCTION RETUNR THE  TYPE 3 CUSPAL INTERLINK 
	  * @param cuspNumber
	  * @param planetDegree
	  * @param cuspDegree
	  * @return int[] (array of bhava number)
	  */
	public int[] getKP_CIL_Type3(int cuspNumber,
			double[] planetDegree, double[] cuspDegree) {
		
		// TODO Auto-generated method stub
		//STEP-1:GET THE SUB LORD OF THE CUSP
		 int cuspSubLord=getSubLord(cuspDegree[cuspNumber]);//PLANET NUMBER OF CUSP SUB LORD
		 //END STEP-1

		 //STEP-2:GET THE PLANETS WHICH HAS THE SAME STAR LORD AS PLANET FIND IN STEP-1
		 int tempStep2Index=-1;
		 int[] tempStep2=new int[12];
		 
		 //INITIALIZE THE ARRAY WITH VALUE 0
		  for(int fill=0;fill<tempStep2.length;fill++)
			  tempStep2[fill]=0;
		//END	  
		 for(int i=0;i<planetDegree.length;i++)
			 if(getStarLord(planetDegree[i])==cuspSubLord)
				 tempStep2[++tempStep2Index]=i;
		 
		 if(tempStep2Index==-1)
			 return null;
		 int []planetsInStep2=new int[tempStep2Index+1];
		 tempStep2Index=-1;
		 
		 for(int j=0;j<tempStep2.length;j++)
			 if(tempStep2[j]>0)
				 planetsInStep2[++tempStep2Index]=tempStep2[j];
		 
		//END STEP-2			
		//STEP-3:FIND THE CUSP WHICH HAVE THE SAME SUB LORD AS THE PLANETS FOUND IN STEP-3
		 int tempStep3Index=-1;
		 int[] tempStep3=new int[12];
		 for(int k=0;k<planetsInStep2.length;k++)
		 {
			 for(int l=0;l<cuspDegree.length;l++)
				if(getSubLord(cuspDegree[l])==planetsInStep2[k])
					tempStep3[++tempStep3Index]=l+1;
		 }
		 //END STEP-3
		 //THIS CODE IS FOR REMOVING 0 FROM THE ARRAY
		 int []retType3=new int[tempStep3Index+1];
		 int retType3Index=-1;
		 for(int m=0;m<tempStep3.length;m++)
		 {
			 if(tempStep3[m]>0)
				 retType3[++retType3Index]=tempStep3[m];
		 }
		 
		//return retType3;
		 return CUtilsKPExtension.removeValueFromIntArray(0, retType3);
	}

	/**
	  * THIS FUNCTION RETUNR THE  TYPE 4 CUSPAL INTERLINK 
	  * @param cuspNumber
	  * @param planetDegree
	  * @param cuspDegree
	  * @return int ( bhava number:1-12)
	  */
	public int getKP_CIL_Type4(int cuspNumber,
			double[] planetDegree, double[] cuspDegree) {
		
		//STEP-1-:GET THE SUB LORD OF THE CUSP
		 int cuspLord=getSubLord(cuspDegree[cuspNumber]);
		
		//STEP-2-:GET THE STAR LORD OF THE PLANET FIND IN STEP-1
		 int starLord=getStarLord(planetDegree[cuspLord]);
		//FIND THE CUSP IN WHICH THE PLANET FOUND IN STEP-2, IS PLACED.
		 int houseOccupied=getHouseOccupied(planetDegree[starLord],cuspDegree);
		 
		return houseOccupied;
	}

	/*public int calculatePlanetStrength(int plntNumber, double[] planetDegree) {
		// TODO Auto-generated method stub
		 int[] NakLord = new int[9];
		 int[] plntStrength = new int[9];
			for (int i = 0; i <= 8; i++) 
				NakLord[i] = CUtilsKPExtension.getPlanetNakLord(planetDegree[i]);			
			
			for (int i = 0; i <= 8; i++)
				plntStrength[i]=1;
			for (int i = 0; i <= 8; i++) {
				for (int j = 0; j <= 8; j++) {
					if(i==j){						
						continue;
					}
					else if(GlobalVariablesKPExtension.PLANET_INDEX[i]==NakLord[j]){
						{
							
								plntStrength[i]=0;
						}
					}
				}
				
			}
			return plntStrength[plntNumber];
	}
	
*/
	 /**
	  * THIS FUNCTION RETURN THE OWNER AND OCCUPANT OF THE PLANET
	  * @param plntNumber
	  * @param planetDegree
	  * @param cuspDegree
	  * @return int[](array of house the planet related)
	  */
	public int[] get4StepType1(int plntNumber, double[] plntDegree,
			double[] cuspDegree) {
		// TODO Auto-generated method stub
		int []returnArray=null;
		int plntInBhava = -1,plntInCuspIndex = -1;		
		int[] cuspRelateToPlnt = new int[4];
		try
		{
			//STEP-1:GET THE PLANET RASHIES
			/*int[] arrPlntRashi = new int[2];
			arrPlntRashi[0] = GlobalVariablesKPExtension.PLANET_RASHI[plntNumber][0];
			arrPlntRashi[1] = GlobalVariablesKPExtension.PLANET_RASHI[plntNumber][1];
			//PLANET RASHI OCCUPY THE CUSP)(OWNER OF THE CUSP)
			for(int plntRashiIndex=0;plntRashiIndex<arrPlntRashi.length;plntRashiIndex++)
			{
				if( GlobalVariablesKPExtension.PLANET_RASHI[plntNumber][plntRashiIndex]>0)
				{
					//WRITE THE LOGIN HERE
					double rashiDegree = arrPlntRashi[plntRashiIndex] * 30.00;
					int j = 0;
					for (int i = 0; i < 12; i++) {
						j = i + 1;
						if (j > 11)
							j = 0;
						plntInBhava = CUtilsKPExtension.getPlanetInBhav(cuspDegree[j],
								cuspDegree[i], i, rashiDegree);
						if (plntInBhava > 0) {
							cuspRelateToPlnt[++plntInCuspIndex] = plntInBhava;
							
							plntInBhava = 0;

						}
					}
					
				}
			}*/
			//ADDED BY BIJENDRA ON 20-MARCH-2013
			
			int []tempHousesInRashi=getHousesInPlanetRashi(plntNumber,cuspDegree);
			if(tempHousesInRashi!=null)
			{
				//fill the array
				for(int i=0;i<tempHousesInRashi.length;i++)
					if(tempHousesInRashi[i]>0)
					cuspRelateToPlnt[++plntInCuspIndex] =tempHousesInRashi[i];
			}
			//END
			//STEP-2:GET THE OCCUPENT OF THE PLANET
			//PLANET OCCUPANT THE CUSP
			
			double plDegree = plntDegree[plntNumber] ;
			int j = 0;
			for (int i = 0; i < 12; i++) {
				j = i + 1;
				if (j > 11)
					j = 0;
				plntInBhava = 0;
				plntInBhava = CUtilsKPExtension.getPlanetInBhav(cuspDegree[j],
						cuspDegree[i], i, plDegree/*PLANET DEGREE */);
				if (plntInBhava > 0) {
					cuspRelateToPlnt[++plntInCuspIndex] = plntInBhava;					
					plntInBhava = 0;

				}
			}
			
			//REMOVE THE BLANK VALUE(0) FROM THE ARRAY
			returnArray=new int[++plntInCuspIndex];
			int fillIndex=-1;
			for(int index=0;index<cuspRelateToPlnt.length;index++)
				if(cuspRelateToPlnt[index]>0)
					returnArray[++fillIndex]=cuspRelateToPlnt[index];
					
			
			
		}
		catch(Exception e)
		{
			
		}
		return returnArray;
	}
	 /**
	  * THIS FUNCTION RETURN THE OWNER AND OCCUPANT OF START LORD THE PLANET
	  * @param plntNumber
	  * @param planetDegree
	  * @param cuspDegree
	  * @return int[](array of house the planet related)
	  */
	public int[] get4StepType2(int plntNumber, double[] plntDegree,
			double[] cuspDegree) {
		// TODO Auto-generated method stub
		int plntStarLord=getStarLord(plntDegree[plntNumber]);
		
		return get4StepType1(plntStarLord,plntDegree,cuspDegree);
	}

	/**
	  * THIS FUNCTION RETURN THE OWNER AND OCCUPANT OF SUB LORD THE PLANET
	  * @param plntNumber
	  * @param planetDegree
	  * @param cuspDegree
	  * @return int[](array of house the planet related)
	  */
	public int[] get4StepType3(int plntNumber, double[] plntDegree,
			double[] cuspDegree) {
		// TODO Auto-generated method stub
		int plntSubLord=getSubLord(plntDegree[plntNumber]);
		
		return get4StepType1(plntSubLord,plntDegree,cuspDegree);
	}

	/**
	  * THIS FUNCTION RETURN THE OWNER AND OCCUPANT OF SUB SUB LORD THE PLANET
	  * @param plntNumber
	  * @param planetDegree
	  * @param cuspDegree
	  * @return int[](array of house the planet related)
	  */
	public int[] get4StepType4(int plntNumber, double[] plntDegree,
			double[] cuspDegree) {
		// TODO Auto-generated method stub
		int plntSubSubLord=getSubSubLord(plntDegree[plntNumber]);
		//System.out.println("SUb-Sub >"+plntSubSubLord);
		
		return get4StepType1(plntSubSubLord,plntDegree,cuspDegree);
	}

	public int[] getPlanetInterlinkPotentialType1(int plntNumber,
			double[] planetDegree, double[] cuspDegree) {
		// TODO Auto-generated method stub
		return null;
	}

	public int[] getPlanetInterlinkPotentialType2(int plntNumber,
			double[] planetDegree, double[] cuspDegree) {
		// TODO Auto-generated method stub
		return null;
	}

	public int[] getPlanetInterlinkPotentialType3(int plntNumber,
			double[] planetDegree, double[] cuspDegree) {
		// TODO Auto-generated method stub
		return null;
	}

	public int[] getPlanetInterlinkPotentialType4(int plntNumber,
			double[] planetDegree, double[] cuspDegree) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	  * THIS FUNCTION RETURN THE ARRAY OF CUSP LINKED TO THE PASSED PLANET 
	  * @param plntNumber
	  * @param cuspDegree
	  * @return int[](array of cusp )
	  */

	public int[] getKCIL(int plntNumber, double[] cuspDegree) {
		int []_kcil=new int[12];
		int index=-1;
		// TODO Auto-generated method stub
		for(int ind=0;ind<_kcil.length;ind++)
			_kcil[ind]=-1;
		
		for(int i=0;i<cuspDegree.length;i++)
		{
			if((getStarLord(cuspDegree[i])==plntNumber)||			
					(getSubLord(cuspDegree[i])==plntNumber)||
					     (getSubSubLord(cuspDegree[i])==plntNumber))
				_kcil[++index]=i+1;			
			
		}	
		
		//REMOVE NULL VALUES(-1) FROM THE ARRAY
		if(index==-1)
			return null;
		
		int [] returnArray=new int[++index];
		int tempIndex=-1;
		for(int i=0;i<_kcil.length;i++)
			if(_kcil[i]>-1)
				returnArray[++tempIndex]=_kcil[i];
			
		
		return returnArray;
	}

	/**
	  * THIS FUNCTION RETURN THE ARRAY OF CUSP LINKED TO THE  PLANET 
	  * @param plntNumber
	  * @param cuspDegree
	  * @return int[](array of cusp )
	  */
	public int[] getKCILType1(int plntNumber, double[] cuspDegree,double[] plntDegree) {
		int []_kcil=new int[12];
		int[]cuspStarLord=new int[12];
		int[]cuspSubLord=new int[12];
		int[]cuspSubSubLord=new int[12];
		int index=-1,plntStarlord=-1;;
		
		// TODO Auto-generated method stub
		for(int ind=0;ind<_kcil.length;ind++)
		
			_kcil[ind]=-1;	
		
		for(int i=0;i<12;i++)
		{
			cuspStarLord[i]=getStarLord(cuspDegree[i]);
			cuspSubLord[i]=getSubLord(cuspDegree[i]);
			cuspSubSubLord[i]=getSubSubLord(cuspDegree[i]);
		}
		//GET THE STAR LORD OF THE PLANET
		plntStarlord=getStarLord(plntDegree[plntNumber]);
		for(int i=0;i<12;i++)
		{
			if((plntStarlord==cuspStarLord[i])
					||(plntStarlord==cuspSubLord[i])
					||(plntStarlord==cuspSubSubLord[i]))
				_kcil[++index]=i+1;
		}	
		return CUtilsKPExtension.removeValueFromIntArray(-1, _kcil);
	}
	 /**
	  * THIS FUNCTION RETURN THE ARRAY OF CUSP LINKED TO STAR LORD OF THE  PLANET 
	  * @param plntNumber
	  * @param cuspDegree
	  * @param plntDegree
	  * @return int[](array of cusp )
	  */
	public int[] getKCILType2(int plntNumber, double[] cuspDegree,
			double[] plntDegree) {
		// TODO Auto-generated method stub
		int []_kcilType2=new int[12];
		int[]cuspStarLord=new int[12];
		int[]cuspSubLord=new int[12];
		int[]cuspSubSubLord=new int[12];
		int indexType2=-1,plntSublord=-1;;
		
		// TODO Auto-generated method stub
		for(int ind=0;ind<_kcilType2.length;ind++)
		
			_kcilType2[ind]=-1;	
		
		for(int i=0;i<12;i++)
		{
			cuspStarLord[i]=getStarLord(cuspDegree[i]);
			cuspSubLord[i]=getSubLord(cuspDegree[i]);
			cuspSubSubLord[i]=getSubSubLord(cuspDegree[i]);
		}
		//GET THE SUB LORD OF THE PLANET
		plntSublord=getSubLord(plntDegree[plntNumber]);
		for(int i=0;i<12;i++)
		{
			if((plntSublord==cuspStarLord[i])
					||(plntSublord==cuspSubLord[i])
					||(plntSublord==cuspSubSubLord[i]))
				_kcilType2[++indexType2]=i+1;
		}	
		return CUtilsKPExtension.removeValueFromIntArray(-1, _kcilType2);
	}

	 /**
	  * THIS FUNCTION RETURN THE ARRAY OF CUSP LINKED TO SUB LORD OF THE  PLANET 
	  * @param plntNumber
	  * @param cuspDegree
	  * @param plntDegree
	  * @return int[](array of cusp )
	  */
	public int[] getKCILType3(int plntNumber, double[] cuspDegree,
			double[] plntDegree) {
		int []_kcilType3=new int[12];
		int[]cuspStarLord=new int[12];
		int[]cuspSubLord=new int[12];
		int[]cuspSubSubLord=new int[12];
		int indexType3=-1,plntSubSublord=-1;;
		
		// TODO Auto-generated method stub
		for(int ind=0;ind<_kcilType3.length;ind++)		
			_kcilType3[ind]=-1;	
		
		for(int i=0;i<12;i++)
		{
			cuspStarLord[i]=getStarLord(cuspDegree[i]);
			cuspSubLord[i]=getSubLord(cuspDegree[i]);
			cuspSubSubLord[i]=getSubSubLord(cuspDegree[i]);
		}
		//GET THE SUBSUB LORD OF THE PLANET
		plntSubSublord=getSubSubLord(plntDegree[plntNumber]);
		for(int i=0;i<12;i++)
		{
			if((plntSubSublord==cuspStarLord[i])
					||(plntSubSublord==cuspSubLord[i])
					||(plntSubSublord==cuspSubSubLord[i]))
				_kcilType3[++indexType3]=i+1;
		}	
		return CUtilsKPExtension.removeValueFromIntArray(-1, _kcilType3);
	}
	/**
	  * THIS FUNCTION RETURN THE ARRAY OF CUSP LINKED TO SUB SUB LORD OF THE  PLANET 
	  * @param plntNumber
	  * @param cuspDegree
	  * @param plntDegree
	  * @return int[](array of cusp )
	  */
	public int[] getKCILType4(int plntNumber, double[] cuspDegree,
			double[] plntDegree) {
		// TODO Auto-generated method stub
		//COULD NOT FIND THE LOGIN FOR THIS TYPE
		
		return null;
	}
	/**
	 * This function return the planet strength array(sun-ketu)
	 * @param plntDegree
	 * @return int array(contain the planet strenght (1[strong] or 0[weak]
	 */
	public  int[] getPlanetStrength(double[] plntDegree)
	{
		int[] NakLord = new int[9];
		 int[] plntStrength = new int[9];
		 for (int i = 0; i <= 8; i++) 
				NakLord[i] = CUtilsKPExtension.getPlanetNakLord(plntDegree[i]);					
			
			for (int i = 0; i <= 8; i++)
				plntStrength[i]=1;
			
			for (int i = 0; i <= 8; i++) {
				for (int j = 0; j <= 8; j++) {
					if(i==j){						
						continue;
					}
					else if(GlobalVariablesKPExtension.PLANET_INDEX[i]==NakLord[j]){
						plntStrength[i]=0;
					}
				}
				
			}
			
		return plntStrength;
	}
	public int[] getHousesInPlanetRashi(int plntNumber, double[] cuspDegree)
	{
		int _cuspRashi=-1;
		int plntNakLordInBhava = -1;
		int []returnHouseArray=null;
		//int cusp_owned_by_planet_itself_Index = 0, plntRashiArrayIndex = 0;
		int[] cusp_owned_by_planet_itself = new int[4];

		try {
			for (int k = 0; k < cusp_owned_by_planet_itself.length; k++)
				cusp_owned_by_planet_itself[k] = 0;

			int[] plntNakLordRashi = new int[2];
			plntNakLordRashi[0] = GlobalVariablesKPExtension.PLANET_RASHI[plntNumber][0];
			plntNakLordRashi[1] = GlobalVariablesKPExtension.PLANET_RASHI[plntNumber][1];
			
			//THIS IS A NEW LOGIC WRITTEN BY BIJENDRA ON 18-MARCH-2013
			for(int ral=0;ral<plntNakLordRashi.length;ral++)
			{
				if(plntNakLordRashi[ral]>0)
				{
					/*double dto=plntNakLordRashi[ral]*30.00;
					double dfrom=dto-30.00;*/
					
					for(int cuspIndex=0;cuspIndex<cuspDegree.length;cuspIndex++)
					{
						_cuspRashi=(int) ((cuspDegree[cuspIndex]/30)+1);
						
						if(plntNakLordRashi[ral]==_cuspRashi)
							cusp_owned_by_planet_itself[++plntNakLordInBhava]=cuspIndex+1;
						/*if(dfrom<cuspDegree[cuspIndex]&& cuspDegree[cuspIndex]<=dto)
							cusp_owned_by_planet_itself[++plntNakLordInBhava]=cuspIndex+1;*/
					}
				}		
			}
			//this code is written by Bijendra on 18-march-2013
			returnHouseArray=new int[plntNakLordInBhava+1];
			int index=-1;
			System.out.println("Planet nummber>"+plntNumber);
			for(int len=0;len<cusp_owned_by_planet_itself.length;len++)
			{
				
				//System.out.print("Houses>"+cusp_owned_by_planet_itself[len]+" ");
				if(cusp_owned_by_planet_itself[len]>0 )
					returnHouseArray[++index]=cusp_owned_by_planet_itself[len];
			}
			//end	

			
		} catch (Exception e) {

		}

		return returnHouseArray;
	}
	private  int[] getConjuction(int plntNu,double[] plntDeg)
	{
		int []con=new int[10];
		int[]retValu=null;
		int tempRashi=0,index=-1,indexN=-1;
		for(int j=0;j<con.length;j++)
			con[j]=-1;
		
		
		int plntRashi=(int) ((plntDeg[plntNu]/30)+1);
		for(int i=0;i<plntDeg.length;i++)
		{
			if(plntNu!=i)
			{
				tempRashi=(int) ((plntDeg[i]/30)+1);
				if(plntRashi==tempRashi)
					con[++index]=i;
			}
		}
		if(index>-1)
		{
			retValu=new int[index+1];
			for(int j=0;j<retValu.length;j++)
			{
				if(retValu[j]>-1)
					retValu[++indexN]=retValu[j];
			}
		}
		return retValu;
		//return CUtilsKPExtension.removeDuplicates(con);		
		
	}
	private  int getRashiLordOfThePlanetWhereItPlaced(double plntDeg)
	{
		int rashi=(int) ((plntDeg/30)+1);
		return GlobalVariablesKPExtension.RASHI_TO_PLANET[rashi];
		
		
	}

	public int distanceOfHousePlanet2PositedFromPlanet1(double planet1,
			double planet2) {
		// TODO Auto-generated method stub
		return 0;
	}

	

	
	
}
