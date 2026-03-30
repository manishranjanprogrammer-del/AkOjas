package com.ojassoft.kpextension;


public class CKpRefactorExtension implements IKpRefactorExtension {

	

	double[] planetDegree = null;
	double[] cuspDegree = null;

	public CKpRefactorExtension(double[] plntDeg, double[] cupDeg) {
		planetDegree = plntDeg;
		cuspDegree = cupDeg;

	}

	/**
	 * THIS FUNCTION RETURN THE ONWER OF THE HOUSE(RASHI OF THE HOUSE)
	 * @param house degree
	 * @return PLANET NUMBER(0-8)
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
	 * @return PLANET NUMBER(0-8)
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

	/**
	 * THIS FUNCTION RETUNR THE SUB SUB LOARD OF THE PASSED PLANET/CUSP DEGREE
	 * @param degree
	 * @return PLANET NUMBER(0-8)
	 */
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
	 * THIS FUNCTION RETUNR THE HOUSES ON WHICH PLANET RASHIS OCCUPIES
	 * 
	 * @param planet
	 *            number
	 * @return HOUSE NUMBER(1-12)
	 */
	public int[] getHouseOwned(int plntNumber) {
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
					plntNakLordInBhava = CUtilsKPExtension.getPlanetInBhav(
							this.cuspDegree[j], this.cuspDegree[i], i,
							rashiDegree/* RASHI DEGREE */);
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

	
	/**
	 * THIS FUNCTION RETUNR THE HOUSE THAT PLANET OCCUPIE
	 * 
	 * @param planet
	 *            degree
	 * @return HOUSE NUMBER(1-12)
	 */
	public int getHouseOccupied(double planetDegree) {
		int plntNakLordInBhava = -1;
		try {
			int j = 0;
			for (int i = 0; i < 12; i++) {
				j = i + 1;
				if (j > 11)
					j = 0;
				plntNakLordInBhava = CUtilsKPExtension
						.getPlanetInBhav(this.cuspDegree[j],
								this.cuspDegree[i], i, planetDegree);
				if (plntNakLordInBhava > 0)
					break;
			}
		} catch (Exception e) {

		}

		return plntNakLordInBhava;
	}

	/**
	 * THIS FUNCTION IS USED TO RETURN PLANETS (IN NUMBER 0-8) IN CUSP
	 * @param cuspNumber
	 * @return planets
	 */
	public int[] getPlanetsInCusp(int cuspNumber) {
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
			planetNo = CUtilsKPExtension.getPlanetInBhav(
					this.cuspDegree[cusp2Index], this.cuspDegree[cusp1Index],
					cuspNumber, this.planetDegree[j]);
			if (planetNo > 0) {
				planetsInHouse[planetIndex] = j;
				planetIndex++;
				planetNo = -1;
			}
		}
		return CUtilsKPExtension.removeValueFromIntArray(-1, planetsInHouse);
	}

	/**
	 * THIS FUNCTION IS USED TO RETURN PLANETS (IN NUMBER 0-8) IN THE RASHI
	 * @param rashiNumber
	 * @return number of planets
	 */
	public int[] getPlanetsInRashi(int rashiNumber) {
		double rashiDegreeEnd = rashiNumber * 30.00;
		double rashiDegreeBegin = rashiDegreeEnd - 30.00;
		int planetIndex = 0;
		int[] planets = new int[7];

		for (int j = 0; j < planets.length; j++)
			planets[j] = -1;

		for (int i = 0; i < this.planetDegree.length; i++) {
			if ((rashiDegreeBegin < this.planetDegree[i])
					&& (this.planetDegree[i] < rashiDegreeEnd))
				planets[planetIndex++] = i;
		}

		return planets;
	}

	
	public int[] getHousesInPlanetRashi(int plntNumber) {
		int _cuspRashi = -1;
		int plntNakLordInBhava = -1;
		int[] returnHouseArray = null;

		int[] cusp_owned_by_planet_itself = new int[4];

		try {
			for (int k = 0; k < cusp_owned_by_planet_itself.length; k++)
				cusp_owned_by_planet_itself[k] = 0;

			int[] plntNakLordRashi = new int[2];
			plntNakLordRashi[0] = GlobalVariablesKPExtension.PLANET_RASHI[plntNumber][0];
			plntNakLordRashi[1] = GlobalVariablesKPExtension.PLANET_RASHI[plntNumber][1];

			// THIS IS A NEW LOGIC WRITTEN BY BIJENDRA ON 18-MARCH-2013
			for (int ral = 0; ral < plntNakLordRashi.length; ral++) {
				if (plntNakLordRashi[ral] > 0) {
					for (int cuspIndex = 0; cuspIndex < cuspDegree.length; cuspIndex++) {
						_cuspRashi = (int) ((cuspDegree[cuspIndex] / 30) + 1);

						if (plntNakLordRashi[ral] == _cuspRashi)
							cusp_owned_by_planet_itself[++plntNakLordInBhava] = cuspIndex + 1;
					}
				}
			}
			// this code is written by Bijendra on 18-march-2013
			returnHouseArray = new int[plntNakLordInBhava + 1];
			int index = -1;
			//System.out.println("Planet nummber>" + plntNumber);
			for (int len = 0; len < cusp_owned_by_planet_itself.length; len++) {

				if (cusp_owned_by_planet_itself[len] > 0)
					returnHouseArray[++index] = cusp_owned_by_planet_itself[len];
			}
			// end

		} catch (Exception e) {

		}

		return returnHouseArray;
	}

	
	 //THIS FUNCTION RETURN KHULLAR CUSPAL INTERLINK
	
	/**
	 * THIS FUNCTION RETURN THE ARRAY OF CUSP LINKED TO THE PLANET
	 * @param plntNumber
	 * @return int[](array of cusp )
	 */
	public int[] getKCILType1(int plntNumber) {
		int []_kcil=new int[12];
		int[]cuspStarLord=new int[12];
		int[]cuspSubLord=new int[12];
		int[]cuspSubSubLord=new int[12];
		int index=-1,plntStarlord=-1,_rahuKetuRashiLord=-1;
		
	
		// TODO Auto-generated method stub
		for(int ind=0;ind<_kcil.length;ind++)
		
			_kcil[ind]=-1;	
		
		for(int i=0;i<12;i++)
		{
			cuspStarLord[i]=getStarLord(this.cuspDegree[i]);
			cuspSubLord[i]=getSubLord(this.cuspDegree[i]);
			cuspSubSubLord[i]=getSubSubLord(this.cuspDegree[i]);
		}
		//ONLY IF PLANETS IS RAHU/KETU
		if((plntNumber==GlobalVariablesKPExtension.RAHU)||(plntNumber==GlobalVariablesKPExtension.KETU))
		{
			plntStarlord=getStarLord(this.planetDegree[plntNumber]);
			
			for(int i=0;i<12;i++)			
				if((plntNumber==cuspStarLord[i])
						||(plntNumber==cuspSubLord[i])
						||(plntNumber==cuspSubSubLord[i]))
					 _kcil[++index]=i+1;
					
			
			
			int cuspNum=-1;
			cuspNum=getHouseOccupied(this.planetDegree[plntNumber]);//CUSP NUMBER IN WHICH RAHU/KET PLACED
			
			  if(!hasNumberInIntegerArray(_kcil,cuspNum))
				  _kcil[++index]=cuspNum;
			  
		
		   _rahuKetuRashiLord=getRashiLordOfThePlanetWhereItPlaced(this.planetDegree[plntNumber]);//RASHI LORD OF RAHU/KETU
		   int slofRashiLord=getStarLord(this.planetDegree[_rahuKetuRashiLord]);//STAR LORD OF RASHI LORD OF RAHU/KETU
		   
		  //IF PLANET IS RAHU/KETU AND S.L ALSO IS RAHU/KETU
		  if(((plntNumber==GlobalVariablesKPExtension.RAHU)||(plntNumber==GlobalVariablesKPExtension.KETU))
				  && ((plntStarlord==GlobalVariablesKPExtension.RAHU)||(plntStarlord==GlobalVariablesKPExtension.KETU)))
		  {
			  for(int i=0;i<12;i++)
				{
					if((_rahuKetuRashiLord==cuspStarLord[i])
							||(_rahuKetuRashiLord==cuspSubLord[i])
							||(_rahuKetuRashiLord==cuspSubSubLord[i]))
						
						  if(!hasNumberInIntegerArray(_kcil,i+1))
							  _kcil[++index]=i+1;
					
				}
			  
		  }
		  else //IF PLANET IS RAHU/KETU AND S.L  IS NOT RAHU/KETU
		  {
		   for(int i=0;i<12;i++)
			{
				if((slofRashiLord==cuspStarLord[i])
						||(slofRashiLord==cuspSubLord[i])
						||(slofRashiLord==cuspSubSubLord[i]))
				
						
					  if(!hasNumberInIntegerArray(_kcil,i+1))
						  _kcil[++index]=i+1;
				
			}
		  }
		  
		  
		  for(int i=0;i<12;i++)
			{
				if((plntStarlord==cuspStarLord[i])
						||(plntStarlord==cuspSubLord[i])
						||(plntStarlord==cuspSubSubLord[i]))
				   
						  if(!hasNumberInIntegerArray(_kcil,i+1))					  
							  _kcil[++index]=i+1;
					
				
			}
		}
		
		else
		{
			//GET THE STAR LORD OF THE PLANET
			plntStarlord=getStarLord(this.planetDegree[plntNumber]);			
			for(int i=0;i<12;i++)
				{
					if((plntStarlord==cuspStarLord[i])
							||(plntStarlord==cuspSubLord[i])
							||(plntStarlord==cuspSubSubLord[i]))
					   
							  if(!hasNumberInIntegerArray(_kcil,i+1))					  
								  _kcil[++index]=i+1;
						
					
				}
			//IF SUB LORD IF RAHU/KETU
			  if ((plntStarlord==GlobalVariablesKPExtension.RAHU)||(plntStarlord==GlobalVariablesKPExtension.KETU))
			  {
				    int cuspNum=-1;
					cuspNum=getHouseOccupied(this.planetDegree[plntStarlord]);//CUSP NUMBER IN WHICH RAHU/KET PLACED
					
					  if(!hasNumberInIntegerArray(_kcil,cuspNum))
						  _kcil[++index]=cuspNum;
					  
					  _rahuKetuRashiLord=getRashiLordOfThePlanetWhereItPlaced(this.planetDegree[plntStarlord]);//RASHI LORD OF RAHU/KETU
					  for(int i=0;i<12;i++)
						{
							if((_rahuKetuRashiLord==cuspStarLord[i])
									||(_rahuKetuRashiLord==cuspSubLord[i])
									||(_rahuKetuRashiLord==cuspSubSubLord[i]))
							   
									  if(!hasNumberInIntegerArray(_kcil,i+1))					  
										  _kcil[++index]=i+1;
								
							
						}
					  
					   int slofRahuKetu=getStarLord(this.planetDegree[plntStarlord]);//STAR LORD OF RAHU/KETU
					   
					   for(int i=0;i<12;i++)
						{
							if((slofRahuKetu==cuspStarLord[i])
									||(slofRahuKetu==cuspSubLord[i])
									||(slofRahuKetu==cuspSubSubLord[i]))
							   
									  if(!hasNumberInIntegerArray(_kcil,i+1))					  
										  _kcil[++index]=i+1;
								
							
						}
					  
			  }
		}
			
			
			return CUtilsKPExtension.removeValueFromIntArray(-1, _kcil);
		
	}
	/**
	 * THIS FUNCTION RETURN THE ARRAY OF CUSP LINKED TO STAR LORD OF THE PLANET
	 * 
	 * @param plntNumber
	 * @return int[](array of cusp )
	 */

	public int[] getKCILType2(int plntNumber) {
		// TODO Auto-generated method stub
		int []_kcil=new int[12];
		int[]cuspStarLord=new int[12];
		int[]cuspSubLord=new int[12];
		int[]cuspSubSubLord=new int[12];
		int index=-1,plntSublord=-1,_rahuKetuRashiLord=-1;
		
	
		// TODO Auto-generated method stub
		for(int ind=0;ind<_kcil.length;ind++)
		
			_kcil[ind]=-1;	
		
		for(int i=0;i<12;i++)
		{
			cuspStarLord[i]=getStarLord(this.cuspDegree[i]);
			cuspSubLord[i]=getSubLord(this.cuspDegree[i]);
			cuspSubSubLord[i]=getSubSubLord(this.cuspDegree[i]);
		}
			
		
			//GET THE STAR LORD OF THE PLANET
			plntSublord=getSubLord(this.planetDegree[plntNumber]);			
			for(int i=0;i<12;i++)
				{
					if((plntSublord==cuspStarLord[i])
							||(plntSublord==cuspSubLord[i])
							||(plntSublord==cuspSubSubLord[i]))
					   
							  if(!hasNumberInIntegerArray(_kcil,i+1))					  
								  _kcil[++index]=i+1;
						
					
				}
			//IF SUB LORD IF RAHU/KETU
			  if ((plntSublord==GlobalVariablesKPExtension.RAHU)||(plntSublord==GlobalVariablesKPExtension.KETU))
			  {
				    int cuspNum=-1;
					cuspNum=getHouseOccupied(this.planetDegree[plntSublord]);//CUSP NUMBER IN WHICH RAHU/KET PLACED
					
					  if(!hasNumberInIntegerArray(_kcil,cuspNum))
						  _kcil[++index]=cuspNum;
					  
					  _rahuKetuRashiLord=getRashiLordOfThePlanetWhereItPlaced(this.planetDegree[plntSublord]);//RASHI LORD OF RAHU/KETU
					  for(int i=0;i<12;i++)
						{
							if((_rahuKetuRashiLord==cuspStarLord[i])
									||(_rahuKetuRashiLord==cuspSubLord[i])
									||(_rahuKetuRashiLord==cuspSubSubLord[i]))
							   
									  if(!hasNumberInIntegerArray(_kcil,i+1))					  
										  _kcil[++index]=i+1;
								
							
						}
					  
					   int sublofRahuKetu=getSubLord(this.planetDegree[plntSublord]);//STAR LORD OF RAHU/KETU
					   
					   for(int i=0;i<12;i++)
						{
							if((sublofRahuKetu==cuspStarLord[i])
									||(sublofRahuKetu==cuspSubLord[i])
									||(sublofRahuKetu==cuspSubSubLord[i]))
							   
									  if(!hasNumberInIntegerArray(_kcil,i+1))					  
										  _kcil[++index]=i+1;
								
							
						}
					  
			  }
		
			
			
			return CUtilsKPExtension.removeValueFromIntArray(-1, _kcil);
	}

	
	/**
	 * THIS FUNCTION RETURN THE ARRAY OF CUSP LINKED TO SUB LORD OF THE PLANET
	 * 
	 * @param plntNumber
	 * @return int[](array of cusp )
	 */	
	public int[] getKCILType3(int plntNumber) {
		// TODO Auto-generated method stub
		int []_kcil=new int[12];
		int[]cuspStarLord=new int[12];
		int[]cuspSubLord=new int[12];
		int[]cuspSubSubLord=new int[12];
		int index=-1,plntSSublord=-1,_rahuKetuRashiLord=-1;
		
	
		// TODO Auto-generated method stub
		for(int ind=0;ind<_kcil.length;ind++)		
			_kcil[ind]=-1;	
		
		for(int i=0;i<12;i++)
		{
			cuspStarLord[i]=getStarLord(this.cuspDegree[i]);
			cuspSubLord[i]=getSubLord(this.cuspDegree[i]);
			cuspSubSubLord[i]=getSubSubLord(this.cuspDegree[i]);
		}
					
		//GET THE SUB SUB LORD OF THE PLANET
		plntSSublord=getSubSubLord(this.planetDegree[plntNumber]);			
		for(int i=0;i<12;i++)
			{
				if((plntSSublord==cuspStarLord[i])
						||(plntSSublord==cuspSubLord[i])
						||(plntSSublord==cuspSubSubLord[i]))
				   
						  if(!hasNumberInIntegerArray(_kcil,i+1))					  
							  _kcil[++index]=i+1;
					
				
			}
		
		//IF SUB SUB LORD IF RAHU/KETU
		  if ((plntSSublord==GlobalVariablesKPExtension.RAHU)||(plntSSublord==GlobalVariablesKPExtension.KETU))
		  {
			   int cuspNum=-1,sslOfRahuKetu=-1;
			    int slOfRahuKetu=getStarLord(this.planetDegree[plntSSublord]);
			    
				cuspNum=getHouseOccupied(this.planetDegree[plntSSublord]);//CUSP NUMBER IN WHICH RAHU/KET PLACED
				
				  if(!hasNumberInIntegerArray(_kcil,cuspNum))
					  _kcil[++index]=cuspNum;
				  
				   sslOfRahuKetu=getSubSubLord(this.planetDegree[plntSSublord]);
				  
				   if((plntNumber!=GlobalVariablesKPExtension.RAHU && plntNumber!=GlobalVariablesKPExtension.KETU))   
				    for(int i=0;i<12;i++)
						{
							if((sslOfRahuKetu==cuspStarLord[i])
									||(sslOfRahuKetu==cuspSubLord[i])
									||(sslOfRahuKetu==cuspSubSubLord[i]))
							   
									  if(!hasNumberInIntegerArray(_kcil,i+1))					  
										  _kcil[++index]=i+1;
						}		
				   
			   
			 for(int i=0;i<12;i++)
				{
					if((slOfRahuKetu==cuspStarLord[i])
							||(slOfRahuKetu==cuspSubLord[i])
							||(slOfRahuKetu==cuspSubSubLord[i]))
					  
							  if(!hasNumberInIntegerArray(_kcil,i+1))					  
								  _kcil[++index]=i+1;							
					
				}
			  
		  }
		
		return CUtilsKPExtension.removeValueFromIntArray(-1, _kcil);
	}


	/**
	 * THIS FUNCTION RETURN THE ARRAY OF CUSP LINKED TO SUB SUB LORD OF THE
	 * PLANET
	 * 
	 * @param plntNumber
	 * @return int[](array of cusp )
	 */
	public int[] getKCILType4(int plntNumbere) {
		
		int []_kcilType4=new int[12];
		int[]cuspStarLord=new int[12];
		int[]cuspSubLord=new int[12];
		int[]cuspSubSubLord=new int[12];
		int indexType4=-1;
		
		
		boolean nSL=false,ows=false,hasPositionalStutas=false;		
		nSL=checkPlanetIsStarLordOfOtherPlanet(plntNumbere);
		ows=isPlanetInItsOwnStar(plntNumbere);
		if(ows)
		 {				
			hasPositionalStutas=true;
			
		 }
		 else if(!nSL)
		 {
			 hasPositionalStutas=true;
		 }
		
		if(!hasPositionalStutas)
		 return null;
		else			
		{
			for(int ind=0;ind<_kcilType4.length;ind++)		
				_kcilType4[ind]=-1;	
			
			for(int i=0;i<12;i++)
			{
				cuspStarLord[i]=getStarLord(this.cuspDegree[i]);
				cuspSubLord[i]=getSubLord(this.cuspDegree[i]);
				cuspSubSubLord[i]=getSubSubLord(this.cuspDegree[i]);
			}
			
			for(int i=0;i<12;i++)
			{
				if((plntNumbere==cuspStarLord[i])
						||(plntNumbere==cuspSubLord[i])
						||(plntNumbere==cuspSubSubLord[i]))
					_kcilType4[++indexType4]=i+1;
				
				
			}
			if((plntNumbere==GlobalVariablesKPExtension.RAHU)||(plntNumbere==GlobalVariablesKPExtension.KETU))
			{
				int cuspNum=-1;		 
			    
				cuspNum=getHouseOccupied(this.planetDegree[plntNumbere]);
				 if(!hasNumberInIntegerArray(_kcilType4,cuspNum))
					 _kcilType4[++indexType4]=cuspNum;
			} 
			
			return CUtilsKPExtension.removeValueFromIntArray(-1, _kcilType4);
			
		}
			
			
	}
	 //END KHULLAR CUSPAL INTERLINK FUNCTIONS

	 //START 4 STEP TYPE FUNCTIONS
	
	/**
	 * THIS FUNCTION RETURN THE OWNER AND OCCUPANT OF THE PLANET
	 * 
	 * @param plntNumber
	 * @return int[](array of house the planet related)
	 */
	/*public int[] get4StepType1(int plntNumber) {
		// TODO Auto-generated method stub
		int []returnArray=null;
		int plntInBhava = -1,plntInCuspIndex = -1;		
		int[] cuspRelateToPlnt = new int[4];
		try
		{
			//STEP-1:GET THE PLANET RASHIES			
			//ADDED BY BIJENDRA ON 20-MARCH-2013
			
			int []tempHousesInRashi=getHousesInPlanetRashi(plntNumber);
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
			
			double plDegree = this.planetDegree[plntNumber] ;
			int j = 0;
			for (int i = 0; i < 12; i++) {
				j = i + 1;
				if (j > 11)
					j = 0;
				plntInBhava = 0;
				plntInBhava = CUtilsKPExtension.getPlanetInBhav(cuspDegree[j],
						cuspDegree[i], i, plDegreePLANET DEGREE );
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
	}*/

	/**
	 * THIS FUNCTION RETURN THE OWNER AND OCCUPANT OF START LORD THE PLANET
	 * 
	 * @param plntNumber
	 * @return int[](array of house the planet related)
	 */
	/*public int[] get4StepType2(int plntNumber) {
		int plntStarLord=getStarLord( this.planetDegree[plntNumber]);		
		return get4StepType1(plntStarLord);
	}*/

	/**
	 * THIS FUNCTION RETURN THE OWNER AND OCCUPANT OF SUB LORD THE PLANET
	 * 
	 * @param plntNumber
	 * @return int[](array of house the planet related)
	 */
	/*public int[] get4StepType3(int plntNumber) {
		// TODO Auto-generated method stub
		int plntSubLord=getSubLord(this.planetDegree[plntNumber]);		
		return get4StepType1(plntSubLord);
	}
*/
	/**
	 * THIS FUNCTION RETURN THE OWNER AND OCCUPANT OF SUB SUB LORD THE PLANET
	 * 
	 * @param plntNumber
	 * @return int[](array of house the planet related)
	 */
	/*public int[] get4StepType4(int plntNumber) {
		// TODO Auto-generated method stub
		int plntSubSubLord=getSubSubLord(this.planetDegree[plntNumber]);
		return get4StepType1(plntSubSubLord);
	}*/
	 //END 4 STEP TYPE FUNCTIONS
	
	
	 // FUNCTIONS FOR CUSPAL INTERLINKS 
	
	
	/**
	 * THIS FUNCTION RETUNR THE TYPE 1 CUSPAL INTERLINK
	 * 
	 * @param cuspNumber
	 * @return int[] (array of bhava number)
	 */
	public int[] getKP_CIL_Type1(int cuspNumber) {
		int []type1=new int[12];
		
		//STEP-1:GET CUSP SUB LORD(PLANET)
		 int cuspSubLord=getSubLord(this.cuspDegree[cuspNumber]);//PLANET NUMBER OF CUSP SUB LORD
		 
		
		//STEP-2:GET  STAR LORD OF THE PLANET THAT COMES IN STEP 1
		 int plntStarLord=getStarLord(this.planetDegree[cuspSubLord]);
		
		//STEP-3 IDENTIFY THE CUSP THAT HAVE THE SAME SUB LORD PLANET AS PLANET  CAME IN STEP 2
		 int index=-1;
		 int[] arrayCuspSubLord=new int[12];
		 for(int j=0;j<12;j++)
			 arrayCuspSubLord[j]=getSubLord(this.cuspDegree[j]);
		 for(int i=0;i<this.cuspDegree.length;i++)
		 {
			 if(arrayCuspSubLord[i]==plntStarLord)
				 type1[++index]=i+1;
		 }
		 
		
		//STEP-4:GET THE SUB LORD OF THE PLANET THAT HAVE COME IN STEP 1
		 int plntSubLord=getSubLord(this.planetDegree[cuspSubLord]);
		 
		//STEP-5:IDENTIFY THE CUSP THAT HAVE THE SAME SUB LORD PLANET AS PLANET  CAME IN STEP 4
		
		 for(int k=0;k<this.cuspDegree.length;k++)
			 if(arrayCuspSubLord[k]==plntSubLord)
				 type1[++index]=k+1;
		 
		
		 return CUtilsKPExtension.removeValueFromIntArray(0, type1);
	}

	/**
	 * THIS FUNCTION RETUNR THE TYPE 2 CUSPAL INTERLINK
	 * 
	 * @param cuspNumber
	 * @return int[] (array of bhava number)
	 */
	public int[] getKP_CIL_Type2(int cuspNumber) {
		// TODO Auto-generated method stub
		int []type2=new int[13];
				
		//STEP-1:FIND THE SUB LORD OF THE CUSP
		 int cuspSubLord=getSubLord(this.cuspDegree[cuspNumber]);//PLANET NUMBER OF CUSP SUB LORD
		 int index=-1;
		 int[] arrayCuspSubLord=new int[12];
		 for(int j=0;j<12;j++)
			 arrayCuspSubLord[j]=getSubLord(this.cuspDegree[j]);
		 for(int i=0;i<this.cuspDegree.length;i++)
		 {
			 if(i!=cuspNumber)
				 if(arrayCuspSubLord[i]==cuspSubLord)
					 type2[++index]=i+1;
		 }
		 //STEP:2
		 int plntStarLord=getStarLord(this.planetDegree[cuspSubLord]);
		 
		 for(int i=0;i<this.cuspDegree.length;i++)
		 {
			 if(i!=cuspNumber)
				 if(arrayCuspSubLord[i]==plntStarLord)
					 type2[++index]=i+1;
		 }
		 
		 int[] arrayCuspSubSubLord=new int[12];
		 for(int j=0;j<12;j++)
			 arrayCuspSubSubLord[j]=getSubSubLord(this.cuspDegree[j]);
		 
		 for(int i=0;i<this.cuspDegree.length;i++)
		 {
			 if(i!=cuspNumber)
				 if(arrayCuspSubSubLord[i]==plntStarLord)
					 type2[++index]=i+1;
		 }
		
		 return CUtilsKPExtension.removeValueFromIntArray(0, type2);
	}

	/**
	 * THIS FUNCTION RETUNR THE TYPE 3 CUSPAL INTERLINK
	 * 
	 * @param cuspNumber
	 * @return int[] (array of bhava number)
	 */
	public int[] getKP_CIL_Type3(int cuspNumber) {

		// TODO Auto-generated method stub
		//STEP-1:GET THE SUB LORD OF THE CUSP
		 int cuspSubLord=getSubLord(this.cuspDegree[cuspNumber]);//PLANET NUMBER OF CUSP SUB LORD
		 //END STEP-1

		 //STEP-2:GET THE PLANETS WHICH HAS THE SAME STAR LORD AS PLANET FIND IN STEP-1
		 int tempStep2Index=-1;
		 int[] tempStep2=new int[12];
		 
		 //INITIALIZE THE ARRAY WITH VALUE 0
		  for(int fill=0;fill<tempStep2.length;fill++)
			  tempStep2[fill]=0;
		//END	  
		 for(int i=0;i<this.planetDegree.length;i++)
			 if(getStarLord(this.planetDegree[i])==cuspSubLord)
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
			 for(int l=0;l<this.cuspDegree.length;l++)
				if(getSubLord(this.cuspDegree[l])==planetsInStep2[k])
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
		 
		return CUtilsKPExtension.removeValueFromIntArray(0, retType3);
	}

	/**
	 * THIS FUNCTION RETUNR THE TYPE 4 CUSPAL INTERLINK
	 * 
	 * @param cuspNumber
	 * @return int ( bhava number:1-12)
	 */
	public int getKP_CIL_Type4(int cuspNumber) {
		//STEP-1-:GET THE SUB LORD OF THE CUSP
		 int cuspLord=getSubLord(this.cuspDegree[cuspNumber]);
		
		//STEP-2-:GET THE STAR LORD OF THE PLANET FIND IN STEP-1
		 int starLord=getStarLord(this.planetDegree[cuspLord]);
		//FIND THE CUSP IN WHICH THE PLANET FOUND IN STEP-2, IS PLACED.
		 int houseOccupied=getHouseOccupied(this.planetDegree[starLord]);
		 
		return houseOccupied;
	}

	 // END CUSPAL INTERLINKS
	
	
	 // FUNCTIONS FOR  NAKSHTRA NADI 
	
	/**
	 * THIS FUNCTION RETURN THE NADI OF THE PLANET
	 * 
	 * @param plntNumber
	 * @return nadi of planet
	 */
	public int[] getPlanetNadi(int plntNumber) {
		int planetNadiLength=0,index=0;
		int houseOccupiedByPlanet;
		int[] planetRashioccupiedHouse=null;
		/*in which house planet is*/
		 houseOccupiedByPlanet=getHouseOccupied(this.planetDegree[plntNumber]);
		
		/* in which house,the planet rashi are*/
		if((plntNumber==GlobalVariablesKPExtension.RAHU)||(plntNumber==GlobalVariablesKPExtension.KETU))
		{
			//planetRashioccupiedHouse=this.getRahuOrKetuNadi(plntNumber);
			return this.getRahuOrKetuNadi(plntNumber);
		}
		else
			planetRashioccupiedHouse=getHousesInPlanetRashi(plntNumber);
						
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
	 * 
	 * @param plntNumber
	 * @return nadi of planet
	 */
	public int[] getPlanetStarLordNadi(int plntNumber) {
		//GET PLANET START LORD
		int planetStarlord=getStarLord(planetDegree[plntNumber]);
		int []PlanetStarLordNadi=getPlanetNadi(planetStarlord);
		
		return PlanetStarLordNadi;
	}

	/**
	 * THIS FUNCTION RETURN THE NADI OF THE PLANET SUB LORD
	 * 
	 * @param plntNumber
	 * @return nadi of planet
	 */
	public int[] getPlanetSubLordNadi(int plntNumber) {
		//GET SUB LORD OF THE PLANET
		int planetStarlord=getSubLord(planetDegree[plntNumber]);
		int []PlanetStarLordNadi=getPlanetNadi(planetStarlord);		
		return PlanetStarLordNadi;
	}
	
	// NOTE:WE WILL WRITE THE LOGIN LATER,PRESENTLY WE ARE USING HARD CODE VALUES
	private int[] getRahuOrKetuNadi(int plntNumber) {
		int []collection=new int[20];
		int index=-1;
		int [] retVal=null;
				
		// TODO Auto-generated method stub
		for(int i=0;i<collection.length;i++)
			collection[i]=-1;
		// TODO Auto-generated method stub
		//STEP:1=GET THE RASHI LORD OF THE PLANET IN WHICH IT IS PLACED
		int plntInWhichRashi=getRashiLordOfThePlanetWhereItPlaced(this.planetDegree[plntNumber]);
		//System.out.println("Rahu or Ketu="+plntNumber+"   in rashi"+plntInWhichRashi);
		//System.out.println("Rahu or Ketu="+plntNumber);
		int[]nadi1=getPlanetNadi(plntInWhichRashi);
		if(nadi1!=null)
		for(int i=0;i<nadi1.length;i++)
			if(nadi1[i]>0)
				collection[++index]=nadi1[i];
		
		//STEP:2=CONJUCTION	
		int []arrayTemp=null;
		int[] plntCon=getConjuction(plntNumber);
		if(plntCon!=null)
		{
			for(int i=0;i<plntCon.length;i++)
			{
				//System.out.println("getConjuction="+plntCon[i]);
			  arrayTemp=getPlanetNadi(plntCon[i]);
			  if(arrayTemp!=null)
			  {
				  for(int j=0;j<arrayTemp.length;j++)
						if(arrayTemp[j]>0)
							collection[++index]=arrayTemp[j];
			  }
			  arrayTemp=null;  
			}
			
		}
		//STEP:3 -ASPECT
		int []arrayTempAspect=null;
		int[] plntAspect=getPlanetsHaveAspectOnRahuKetu(this.planetDegree[plntNumber]);
		if(plntAspect!=null)
		{
			for(int i=0;i<plntAspect.length;i++)
			{
				
				arrayTempAspect=getPlanetNadi(plntAspect[i]);
			  if(arrayTempAspect!=null)
			  {
				  for(int j=0;j<arrayTempAspect.length;j++)
						if(arrayTempAspect[j]>0)
							collection[++index]=arrayTempAspect[j];
			  }
			  arrayTempAspect=null;  
			}
			
		}
		int houseOccupied=getHouseOccupied(this.planetDegree[plntNumber]);
		//filter values
		//retVal=new int[index+1];
		retVal=new int[index+2];
		int indexTemp=-1;
		for(int i=0;i<collection.length;i++)
		{
			if(collection[i]>0)
				retVal[++indexTemp]=collection[i];
		}
		retVal[retVal.length-1]=houseOccupied;
		
		return retVal;
	}
	
	private  int[] getConjuction(int plntNu)
	{
		int []con=new int[10];
		int[]retValu=null;
		int tempRashi=0,index=-1;
		for(int j=0;j<con.length;j++)
			con[j]=-1;
		
		//System.out.println("Planetnumber="+plntNu);
		int plntRashi=(int) ((this.planetDegree[plntNu]/30)+1);
		//System.out.println("plntRashi="+plntRashi);
		for(int i=0;i<this.planetDegree.length;i++)
		{
			if(plntNu!=i)
			{
				tempRashi=(int) ((this.planetDegree[i]/30)+1);
				if(plntRashi==tempRashi)
				{
					//System.out.println("Aspect of plnt="+i);
					++index;
					con[index]=i;
				}
			}
		}
		if(index>-1)
		{
			int indexN=0;
			
			retValu=new int[index+1];
			for(int j=0;j<retValu.length;j++)
			{
				if(con[j]>-1)
				{
					retValu[indexN]=con[j];
					//System.out.println("Value="+retValu[indexN]);
					indexN++;
				}
			}
		}
		return retValu;
				
	}
	private  int getRashiLordOfThePlanetWhereItPlaced(double plntDeg)
	{
		int rashi=(int) ((plntDeg/30)+1);
		//System.out.print("DEGREE<>"+plntDeg);
		//System.out.print("RASHI<>"+rashi);
		return GlobalVariablesKPExtension.RASHI_TO_PLANET[rashi];
		
		
	}
	public  int getRashiLordOfHouseWherePlanetPlaced(int plntNumber)
	{
		
		double plntDeg=0.0;
		plntDeg=planetDegree[plntNumber];
		int rashi=(int) ((plntDeg/30)+1);
		return GlobalVariablesKPExtension.RASHI_TO_PLANET[rashi];
		
		
	}
	//END NAKSHTRA NADI FUNCTIONS
	
	// FUNCTIONS FOR  HOUSE SIGNIFICATION 
	
	/**
	 * THIS FUNCTION IS USED TO RETURN CUSP LEVEL 1 PLANETS (IN NUMBER 0-8)
	 * 
	 * @param cuspNumber
	 * @return planets
	 */
	public int[] getHouseSignificatorLevel1(int cuspNumber) {
		int index=-1;
		int[]level1=new int[12];
		int []plntInCusp=null;
		int []naklord=new int[9];
		
		
		for(int i=0;i<12;i++)
			level1[i]=-1;
		
		for(int i=0;i<9;i++)		
			naklord[i]=getStarLord(this.planetDegree[i]);
		
		plntInCusp=this.getPlanetsInCusp(cuspNumber);
		
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
	}

	/**
	 * THIS FUNCTION IS USED TO RETURN CUSP LEVEL 2 PLANETS (IN NUMBER 0-8)
	 * 
	 * @param cuspNumber
	 * @return planets
	 */
	public int[] getHouseSignificatorLevel2(int cuspNumber) {
		return getPlanetsInCusp(cuspNumber);
	}

	/**
	 * THIS FUNCTION IS USED TO RETURN CUSP LEVEL 3 PLANETS (IN NUMBER 0-8)
	 * 
	 * @param cuspDegree
	 * @return planets
	 */
	public int[] getHouseSignificatorLevel3(double cuspDegree) {
		int index=-1;
		int[]level3=new int[9];
		int[]naklord=new int[9];
		
		int cuspRashi=(int) (cuspDegree/30+1);
		int plnn=GlobalVariablesKPExtension.RASHI_TO_PLANET[cuspRashi];
				
		
		for(int l=0;l<9;l++)
		{
			level3[l]=-1;
			naklord[l]=this.getStarLord(this.planetDegree[l]);
		}
		
		for(int i=0;i<9;i++)
			if(plnn==naklord[i])
				level3[++index]=i;
				
		
		return CUtilsKPExtension.removeValueFromIntArray(-1,level3);
	}

	
	/**
	 * THIS FUNCTION IS USED TO RETURN CUSP LEVEL 3 PLANET (IN NUMBER 0-8)
	 * 
	 * @param cuspDegree
	 * @return planet number
	 */
	public int getHouseSignificatorLevel4(double cuspDegree) {
		// TODO Auto-generated method stub
		return this.getHouseOwner(cuspDegree);
	}
	// END  HOUSE SIGNIFICATION FUNCTIONS  
	
	/**
	 * This function return the planet strength array(sun-ketu)
	 * 
	 * @return int array(contain the planet strenght (1[strong] or 0[weak]
	 */
	public int[] getPlanetStrength() {
		int[] NakLord = new int[9];
		 int[] plntStrength = new int[9];
		 for (int i = 0; i <= 8; i++) 
				NakLord[i] = CUtilsKPExtension.getPlanetNakLord(this.planetDegree[i]);					
			
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

	// FUNCTIONS FOR  PLANET SIGNIFICATION 
	
	/**
	 * This function return the planet Signification for level 1
	 * 
	 * @param plnt
	 * @return int (bhava number)
	 */
	public int getPlanetSignificationLevel1(int plnt) {
		int plntNakLordInBhava = -1;

		try {
			// GET THE PLANET NAK LORD
			
			int plntNakLord = getStarLord(this.planetDegree[plnt]);

			int j = 0;
			for (int i = 0; i < 12; i++) {
				j = i + 1;
				if (j > 11)
					j = 0;
				plntNakLordInBhava = CUtilsKPExtension.getPlanetInBhav(this.cuspDegree[j],
						this.cuspDegree[i], i, this.planetDegree[plntNakLord]/*
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
	 * This function return the planet Signification for level 2
	 * 
	 * @param planetDegree
	 * @return int (bhava number)
	 */
	public int getPlanetSignificationLevel2(double planetDegree
			) {
		int plntNakLordInBhava = -1;
		try {
			int j = 0;
			for (int i = 0; i < 12; i++) {
				j = i + 1;
				if (j > 11)
					j = 0;
				plntNakLordInBhava = CUtilsKPExtension
						.getPlanetInBhav(this.cuspDegree[j], this.cuspDegree[i], i,
								planetDegree/* PLANET NAK LORD DEGREE */);
				if (plntNakLordInBhava > 0)
					break;
			}
		} catch (Exception e) {

		}

		return plntNakLordInBhava;
	}

	
	/**
	 * This function return the planet Signification for level 3
	 * 
	 * @param planetDegree
	 * @return int[] (array bhava number)
	 */
	public int[] getPlanetSignificationLevel3(double planetDegree
			) {
		int[] starLordInCusp =null;

		try {
			
			int plntNakLord = getStarLord(planetDegree);
			starLordInCusp=getHousesInPlanetRashi(plntNakLord);
			
			} catch (Exception e) {

		}
		return starLordInCusp;
	}
	/**
	 * This function return the planet Signification for level 3
	 * 
	 * @param plntName
	 * @return int[] (array bhava number)
	 */
	public int[] getPlanetSignificationLevel4(int plntName) {
		int[] cusp_owned_by_planet_itself=null;

		try {
			cusp_owned_by_planet_itself=getHousesInPlanetRashi(plntName);
			
		} catch (Exception e) {

		}

		return cusp_owned_by_planet_itself;
	}
	
	// END OF PLANET SIGNIFICATION  FUNCTIONS
		
	/**
	 * This function is used to return position of planet2 from planet1
	 * @param planet1,planet2
	 * @return house number
	 * @author Bijendra(15-may-2013)
	 */
	public int  distanceOfHousePlanet2PositedFromPlanet1(double planet1, double planet2)
	{
		int distance = CUtilsKPExtension.getRashiInPlanetPlaced(planet2)-CUtilsKPExtension.getRashiInPlanetPlaced(planet1);
		if (distance < 0)
			distance += 12;
		return distance + 1;	
	}
	
	/**
	 * This function is used to return planets  has aspect on rahu/ketu
	 * according to passed rahu/ketu degree
	 * @param degRahuKetu
	 * @return planets
	 * @author Bijendra(15-may-2013)
	 */
	public int[] getPlanetsHaveAspectOnRahuKetu(double degRahuKetu)
	{
		int[] aspectedPlanets=null;
		int [] tempArray=new int[8];
		int index=-1,houseNumber=-1;
		//LOOP FROM SUN -SAT
		for(int i=0;i<tempArray.length;i++)
			tempArray[i]=-1;
		 for(int pn=0;pn<7;pn++)
		 {
			 houseNumber=distanceOfHousePlanet2PositedFromPlanet1(this.planetDegree[pn]/*PLANET -1*/,degRahuKetu/*PLANET -2*/);
			 switch(pn)
				{
					case GlobalVariablesKPExtension.SUN:
						if(houseNumber==7)
						{
							++index;
							tempArray[index]=pn;
						}
						break;
					case GlobalVariablesKPExtension.MOON:
						if(houseNumber==7)
						{
							++index;
							tempArray[index]=pn;
						}
						break;
					case GlobalVariablesKPExtension.MARS:
						if((houseNumber==4)||(houseNumber==7)||(houseNumber==8))
						{
							++index;
							tempArray[index]=pn;
						}
						break;
					case GlobalVariablesKPExtension.MERCURY:
						if(houseNumber==7)
						{
							++index;
							tempArray[index]=pn;
						}
						break;
					case GlobalVariablesKPExtension.JUPITER:
						if((houseNumber==5)||(houseNumber==7)||(houseNumber==9))
						{
							++index;
							tempArray[index]=pn;
						}
						break;
					case GlobalVariablesKPExtension.VENUS:
						if(houseNumber==7)
						{
							++index;
							tempArray[index]=pn;
						}
						break;
					case GlobalVariablesKPExtension.SAT:
						if((houseNumber==3)||(houseNumber==7)||(houseNumber==10))
						{
							++index;
							tempArray[index]=pn;
						}
						break;
								
					
				}
		 }
		 if(index>-1)
		 {
			 int indexN=0;			
			 aspectedPlanets=new int[index+1];;
			  for(int i=0;i<aspectedPlanets.length;i++)
			  {
				  if(tempArray[i]>-1)
				  {
					  aspectedPlanets[indexN]=tempArray[i];
					  indexN++;
				  }
			  }		 
					  
		 }
		return aspectedPlanets;
	}

	public int[] get4StepTypeN1(int plntNumber) {
		// TODO Auto-generated method stub

		//STEP1.A-IF PLANET IS NOT IN ANY NAKSHTRA-CALCULATE(IF THERE ARE NO PLANETS IN THE STARS OF THE PLANET)OR IF PLANET IS IN OWN  NAKSHTRA-CALCULATE
			//STEP1.A.1-Planet becomes THE PRINCIPAL Significator	of the House where it Resides
		   //STEP1.A.2-Planet becomes the Principal of the houses owned ONLY IF THAT HOUSE IS EMPTY
		
		//STEP1.B-IF PLANET IS  NAKSHTRA-LEAVE IT
		
		
		boolean isPlntInBhava=false;
		int []plntInBhava=new int[9];
		int []houseOwner=null;
		int houseNumber=-1;	
		int []step4Va1=null;
		boolean nSL=false,ows=false,isCalculate=false;
		
		nSL=checkPlanetIsStarLordOfOtherPlanet(plntNumber);
		ows=isPlanetInItsOwnStar(plntNumber);
		if(ows)
		 {			
			houseNumber=getHouseOccupied(this.planetDegree[plntNumber]);
			isCalculate=true;
			
		 }
		 else if(!nSL)
		 {
			
			 houseNumber=getHouseOccupied(this.planetDegree[plntNumber]);
			 isCalculate=true;
		 }
		if(!isCalculate)
			return null;
		else
		{
			step4Va1=new int[7];
			 for(int f=0;f<step4Va1.length;f++)
				 step4Va1[f]=-1;
			 step4Va1[0]=houseNumber;
			
		}
		if(plntNumber<7)//FROM SUN -SAT
		{	
			for(int i=0;i<9;i++)
			{
				plntInBhava[i]=getHouseOccupied(this.planetDegree[i]);
				//System.out.println("Plnt "+i +" bhava "+plntInBhava[i]);
			}
			
			houseOwner=getHousesInPlanetRashi(plntNumber);
			
			int index=1;
			if(houseOwner!=null)
			{
				//
				for(int j=0;j<houseOwner.length;j++)
				{				
					for(int i=0;i<9;i++)
					{						
						if(i!=plntNumber)
						{
							if(plntInBhava[i]==houseOwner[j])						
								isPlntInBhava=true;							
						}						
					}	
					if(!isPlntInBhava)
						step4Va1[index++]=houseOwner[j];
					
					isPlntInBhava=false;
				}
				
				
			}
			
			
		}
		
		return CUtilsKPExtension.removeUpdatedDuplicates(CUtilsKPExtension.removeValueFromIntArray(-1, step4Va1));
		
	}

	public int[] get4StepTypeN2(int plntNumber) {
		// TODO Auto-generated method stub
		
		//STEP1-GET THE HOUSE NUMBER OCCUPY BY PLANET
		//STEP2-GET THE HOUSE OWNED BY PLANET
		//STEP2.A-IF HOUSE OWNED BY PLANET HAVE OTHER PLANET .IT IS WEAK AND SHOULD LEAVE THIS HOUSE
		//STEP2.B-IF HOUSE OWNED BY PLANET  DOES NOT HAVE ANY PLANET .IT IS STRONG ,COUNT THIS HOUSE	
		
		boolean isPlntInBhava=false;
		int []plntInBhava=new int[9];
		int []houseOwner=null;
		int houseNumber=-1;	
		int []step4Va2=null;
		
		step4Va2=new int[7];
		 for(int f=0;f<step4Va2.length;f++)
			 step4Va2[f]=-1;
		 
		houseNumber=getHouseOccupied(this.planetDegree[plntNumber]);
			
		step4Va2[0]=houseNumber;
			
		if(plntNumber<7)
		{	
			int index=1;
			for(int i=0;i<9;i++)			
				plntInBhava[i]=getHouseOccupied(this.planetDegree[i]);
			
			houseOwner=getHousesInPlanetRashi(plntNumber);
			
			
			if(houseOwner!=null)
			{
				//int index=1;
				for(int j=0;j<houseOwner.length;j++)
				{				
					for(int i=0;i<9;i++)
					{						
						if(i!=plntNumber)
						{
							if(plntInBhava[i]==houseOwner[j])						
								isPlntInBhava=true;							
						}						
					}	
					if(!isPlntInBhava)
						step4Va2[index++]=houseOwner[j];
					
					isPlntInBhava=false;
				}
				
				
			}
			
			
		}
		
		//return CUtilsKPExtension.removeValueFromIntArray(-1, step4Va2);
		return CUtilsKPExtension.removeUpdatedDuplicates( CUtilsKPExtension.removeValueFromIntArray(-1, step4Va2));
		
	}

	public int[] get4StepTypeN3(int plntNumber) {
		// TODO Auto-generated method stub
		//SAME AS TYPE 1
		return get4StepTypeN1(plntNumber);
		//return null;
	}

	public int[] get4StepTypeN4(int plntNumber) {
		// TODO Auto-generated method stub
		
		//SAME AS TYPE 2
		return get4StepTypeN2(plntNumber);
		
	}

	public boolean checkPlanetIsStarLordOfOtherPlanet(int plntNumber) {
		// TODO Auto-generated method stub
		boolean isLord=false;
		//FROM SUN -RAHU
		for(int i=0;i<9;i++)
			if(i!=plntNumber)
				if(plntNumber==getStarLord(planetDegree[i]))
					isLord=true;
			
		return isLord;
	}

	public boolean isPlanetInItsOwnStar(int plntNumber) {
		// TODO Auto-generated method stub
		boolean isPlanetInOwnStar=false;
		int plntStarLord=getStarLord(planetDegree[plntNumber]);
		//GET THE STAR LORD OF THE PLANET
			if(plntNumber==plntStarLord)
				isPlanetInOwnStar=true;
			
		return isPlanetInOwnStar;
		
	}
	
	/**
	 * This function is used to return number of planets making Conjunction with passed planet
	 * @param planetNumber
	 * @return int array
	 * @author Bijendra(20-sep-13)
	 */
	public int [] getPlanetConjunctionIn4Step(int planetNumber)
	{
		// CHECK FOR PLANET CONJUNCTION
		int index = 0;
		int[] step4PlntConjection = null;
		step4PlntConjection = new int[4];
		int plnt1House=0,plnt2House=0;

		
		/*if(!isPlanetStrongSignificatorIn4Step(planetNumber))
			return null;*/
		//IF PLANET IS STRONG SIGNIFICATOR
		for (int f = 0; f < step4PlntConjection.length; f++)
			step4PlntConjection[f] = -1;
		double plntconjDiff = 0.0, plntDegree = 0.0;
		plntDegree = this.planetDegree[planetNumber];
		plnt1House=getHouseOccupied(plntDegree);
		for (int plntN = 0; plntN < 9; plntN++) {
			if (planetNumber != plntN) {
				
				//plntconjDiff = plntDegree - this.planetDegree[plntN];
				
				plnt2House=getHouseOccupied(this.planetDegree[plntN]);
				if(plnt1House==plnt2House)
				{
					plntconjDiff = plntDegree - this.planetDegree[plntN];
					if (plntconjDiff < 0)
						plntconjDiff *= -1;
					int compResult = Double.compare(plntconjDiff, 3.333333333);
					if (!(compResult > 0)) {
						step4PlntConjection[index++] = plntN;
						//System.out.println(plntN);
	
					}
				}
			}
		}
		if(index==0)//NO CONJUCTION
			return null;
		
		return CUtilsKPExtension.removeUpdatedDuplicates(CUtilsKPExtension
				.removeValueFromIntArray(-1, step4PlntConjection));
	  
	}

	/**
	 * This function is used to return cupsp number making Conjunction with passed planet
	 * @param planetNumber
	 * @return int
	 * @author BijendraBijendra(20-sep-13)
	 */
	public int  getPlanetConjunctionWithCuspIn4Step(int planetNumber)
	{
		int plntConjunctionWithCusp=-1;
		//ADDED BY BIJENDRA ON 18-SEP-13
		double plntDegree=this.planetDegree[planetNumber];
	
		double plntBhavaDiff=0.0;
		 for(int bhavN=0;bhavN<12;bhavN++)
		 {
			 plntBhavaDiff=plntDegree-this.cuspDegree[bhavN];
			// System.out.println(plntBhavaDiff);
			 if(plntBhavaDiff<0)
				 plntBhavaDiff *=-1;
			 int compResult=Double.compare(plntBhavaDiff,3.333333333 );
			 if(!(compResult>0))
			 {
				
				 plntConjunctionWithCusp=(bhavN+1);
					/*System.out.println("Planet number > "+planetNumber+"  Planet Degree > "+plntDegree +"  Cusp  Degree > "+-this.cuspDegree[bhavN] +"  Cusp  number > "+plntConjunctionWithCusp);
					System.out.println("diff > "+plntBhavaDiff);*/
			 }
		 }
		 //END		 
		
		return plntConjunctionWithCusp;
		
		
	}

	public int[] getPlanetsAspectToPlanetIn4Step(int planetNumber) {
		// TODO Auto-generated method stub
		int index = 0;
		double plntDegree=0.0;
		int[] aspectToPlanet = null;
		
		aspectToPlanet = new int[4];

		
		/*if(!isPlanetStrongSignificatorIn4Step(planetNumber))
			return null;*/
		
		for (int f = 0; f < aspectToPlanet.length; f++)
			aspectToPlanet[f] = -1;
		plntDegree = this.planetDegree[planetNumber];
		
		/*if((planetNumber!=GlobalVariablesKPExtension.KETU) || (planetNumber!=GlobalVariablesKPExtension.RAHU))		
		{
			for (int cuspN = 0; cuspN < 12; cuspN++) {
				  if(getRahuKetAspectOnCusp(cuspN,plntDegree,this.cuspDegree[cuspN]))					
					 aspectToPlanet[index++] = cuspN+1;
				}
		}		
		else
		{*/if((planetNumber!=GlobalVariablesKPExtension.KETU) || (planetNumber!=GlobalVariablesKPExtension.RAHU))	
		{
			for (int plntN = 0; plntN < 9; plntN++) {
				if (planetNumber != plntN) {
					if(planetNumber==GlobalVariablesKPExtension.KETU && plntN==GlobalVariablesKPExtension.RAHU||
							planetNumber==GlobalVariablesKPExtension.RAHU && plntN==GlobalVariablesKPExtension.KETU	)
							continue;
					else
					{
						if(getPlanetAspect(plntN,plntDegree,this.planetDegree[plntN]))	
						{
							aspectToPlanet[index++] = plntN;						
						
						  //System.out.println(plntN);
						}
							
					}
					
				}
			}
				
			}
		//}
		if(index==0)
			return null;
		
		return CUtilsKPExtension.removeUpdatedDuplicates(CUtilsKPExtension
				.removeValueFromIntArray(-1, aspectToPlanet));
	}
	
	public int[] getAspectOfPlanetOnCuspIn4Step(int plntNumber)
	{
		int index = 0;
		double plntDegree=0.0;
		int[] aspectToPlanet = null;
		
		aspectToPlanet = new int[4];
		
	/*	if(!isPlanetStrongSignificatorIn4Step(plntNumber))
			return null;*/
		
		plntDegree = this.planetDegree[plntNumber];

		for (int f = 0; f < aspectToPlanet.length; f++)
			aspectToPlanet[f] = -1;
		
		for (int cuspN = 0; cuspN < 12; cuspN++) {
			  if(hasAspectPlanetOnCuspIn4step(plntNumber,cuspN,plntDegree,this.cuspDegree[cuspN]))					
				 aspectToPlanet[index++] = cuspN+1;
			}
		if(index==0)
			return null;
		
		return CUtilsKPExtension.removeUpdatedDuplicates(CUtilsKPExtension
				.removeValueFromIntArray(-1, aspectToPlanet));
	}
	public int[] getRahuKetuAspectOnCuspIn4Step(int plntNumber)
	{
		int index = 0;
		double plntDegree=0.0;
		int[] aspectToPlanet = null;
		
		aspectToPlanet = new int[4];
		
	/*	if(!isPlanetStrongSignificatorIn4Step(plntNumber))
			return null;*/
		
		plntDegree = this.planetDegree[plntNumber];

		for (int f = 0; f < aspectToPlanet.length; f++)
			aspectToPlanet[f] = -1;
		
		for (int cuspN = 0; cuspN < 12; cuspN++) {
			  if(getRahuKetAspectOnCusp(cuspN,plntDegree,this.cuspDegree[cuspN]))					
				 aspectToPlanet[index++] = cuspN+1;
			}
		if(index==0)
			return null;
		
		return CUtilsKPExtension.removeUpdatedDuplicates(CUtilsKPExtension
				.removeValueFromIntArray(-1, aspectToPlanet));
	}
	private boolean getRahuKetAspectOnCusp(int frmCuspNumber,double onPlntDegree,double frmCupsDegree)
	{
		boolean hasAspect=false;
		double degDiff=0.00;
		int bhavaDiff= (int)(onPlntDegree/30.00)-(int)(frmCupsDegree/30.00);
		if(bhavaDiff<0)
			bhavaDiff +=12;
		
		bhavaDiff +=1;
		
		degDiff=onPlntDegree-frmCupsDegree;
		if(degDiff<0.00)
			degDiff +=360.00;
	
		
	
		if((bhavaDiff==5)||(bhavaDiff==7)||(bhavaDiff==9))//FOR JUPITER/RAHU/KETU
		{	
			if(bhavaDiff==5)
				degDiff-=120.00;
			
			if(bhavaDiff==7)
				degDiff-=180.00;
			if(bhavaDiff==9)
				degDiff-=240.00;
			
			if(degDiff<0.00)
				degDiff *=-1.00;
			if(degDiff<3.3333333334)
			{
			 //System.out.println("Cusp > "+frmCuspNumbernumber  +"  "+onPlntDegree +"  "+frmCupsDegree +"  "+degDiff);
			 hasAspect=true;
			}			
			
		}
		
		
		
		
		return hasAspect;
	}
	
	private boolean getPlanetAspect(int frmPlntnumber,double onPlntDegree,double frmPlntDegree)
	{
		boolean hasAspect=false;
		double degDiff=0.00;
		int bhavaDiff= (int)(onPlntDegree/30.00)-(int)(frmPlntDegree/30.00);
		if(bhavaDiff<0)
			bhavaDiff +=12;
		
		bhavaDiff +=1;
		
		degDiff=onPlntDegree-frmPlntDegree;
		if(degDiff<0.00)
			degDiff +=360.00;
		
		
		if(bhavaDiff==7)//FOR ALL PLANETS
		{	
			degDiff-=180.00;
			if(degDiff<0.00)
				degDiff *=-1.00;
			
			if(degDiff<3.3333333334)
			{
			 //System.out.println("Planet > "+frmPlntnumber  +"  "+onPlntDegree +"  "+frmPlntDegree +"  "+degDiff);
			 hasAspect=true;
			}			
			
		}
		if(((bhavaDiff==4)||(bhavaDiff==8))&&(frmPlntnumber==GlobalVariablesKPExtension.MARS))//FOR ALL MARS
		{	
			if(bhavaDiff==4)
				degDiff-=90.00;
			if(bhavaDiff==8)
				degDiff-=210.00;
			
			if(degDiff<0.00)
				degDiff *=-1.00;
			
			if(degDiff<3.3333333334)
			{
			 //System.out.println("Planet > "+frmPlntnumber  +"  "+onPlntDegree +"  "+frmPlntDegree +"  "+degDiff);
			 hasAspect=true;
			}			
			
		}
	
		if(((bhavaDiff==5)||(bhavaDiff==9))&&((frmPlntnumber==GlobalVariablesKPExtension.JUPITER)||(frmPlntnumber==GlobalVariablesKPExtension.RAHU)||(frmPlntnumber==GlobalVariablesKPExtension.KETU)))//FOR JUPITER/RAHU/KETU
		{	
			if(bhavaDiff==5)
				degDiff-=120.00;
			if(bhavaDiff==9)
				degDiff-=240.00;
			
			if(degDiff<0.00)
				degDiff *=-1.00;
			if(degDiff<3.3333333334)
			{
			// System.out.println("Planet > "+frmPlntnumber  +"  "+onPlntDegree +"  "+frmPlntDegree +"  "+degDiff);
			 hasAspect=true;
			}			
			
		}
		if(((bhavaDiff==3)||(bhavaDiff==10))&&(frmPlntnumber==GlobalVariablesKPExtension.SAT))//FOR SAT
		{		
			if(bhavaDiff==3)
				degDiff-=60.00;
			if(bhavaDiff==10)
				degDiff-=270.00;
			
			if(degDiff<0.00)
				degDiff *=-1.00;
			
			if(degDiff<3.3333333334)
			{
			 //System.out.println("Planet > "+frmPlntnumber  +"  "+onPlntDegree +"  "+frmPlntDegree +"  "+degDiff);
			 hasAspect=true;
			}			
			
		}
		
		/*int distance=0;	
		double dDiff=onPlntDegree-frmPlntDegree;
		
			
			if(dDiff<0)
				 dDiff +=360.00;
			
			if((176.6666666666<dDiff)&&(dDiff<183.3333333334))//FOR 7TH ASPECT
				hasAspect=true;
			
			if(!hasAspect)
			{
				if(frmPlntnumber==GlobalVariablesKPExtension.MARS)
				{
					if(((86.6666666666<dDiff)&&(dDiff<93.3333333334))||((206.6666666666<dDiff)&&(dDiff<213.3333333334)))//FOR 4RD/8TH ASPECT[MAR]
						hasAspect=true;
				}
				
				if(frmPlntnumber==GlobalVariablesKPExtension.SAT)
				{
				if(((176.6666666666<dDiff)&&(dDiff<183.3333333334))||((266.6666666666<dDiff)&&(dDiff<273.3333333334)))//FOR 3RD/10TH ASPECT[SAT]
					hasAspect=true;
				}
				if((frmPlntnumber==GlobalVariablesKPExtension.JUPITER)||(frmPlntnumber==GlobalVariablesKPExtension.RAHU)||(frmPlntnumber==GlobalVariablesKPExtension.KETU))
				{				
					if(((116.6666666666<dDiff)&&(dDiff<123.3333333334))||((236.6666666666<dDiff)&&(dDiff<243.3333333334)))//FOR 5RD/9TH ASPECT[SAT]
						hasAspect=true;
				}
			}
			
			
		*/
		
		return hasAspect;
	}

	public int[] getPlanetAspectOnCuspIn4Step(int plntNumber) {
		// TODO Auto-generated method stub
		int index = 0;
		double plntDegree=0.0;
		int[] aspectOnCusp = null;
		
		aspectOnCusp = new int[4];
		
		plntDegree = this.planetDegree[plntNumber];

		for (int f = 0; f < aspectOnCusp.length; f++)
			aspectOnCusp[f] = -1;
		
		for (int cuspN = 0; cuspN < 12; cuspN++) {
			  /*if(getRahuKetAspectOnCusp(cuspN,plntDegree,this.cuspDegree[cuspN]))					
				  aspectOnCusp[index++] = cuspN+1;*/
			  if(hasPlanetAspectOnCusp(plntNumber,cuspN,plntDegree,this.cuspDegree[cuspN]))
				  aspectOnCusp[index++] = cuspN+1;
			}
		
		
		return CUtilsKPExtension.removeUpdatedDuplicates(CUtilsKPExtension
				.removeValueFromIntArray(-1, aspectOnCusp));
	}
	
	private boolean hasPlanetAspectOnCusp(int frmPlntnumber,int frmCuspNumbernumber,double onPlntDegree,double frmCupsDegree)
	{
		boolean hasAspect=false;
		double degDiff=0.00;
		int bhavaDiff= (int)(onPlntDegree/30.00)-(int)(frmCupsDegree/30.00);
		if(bhavaDiff<0)
			bhavaDiff +=12;
		
		bhavaDiff +=1;
		
		degDiff=onPlntDegree-frmCupsDegree;
		if(degDiff<0.00)
			degDiff +=360.00;
	
		if(bhavaDiff==7)//FOR ALL PLANETS
		{
			degDiff-=180.00;
			if(degDiff<0.00)
				degDiff *=-1.00;
			if(degDiff<3.3333333334)
			{
			 //System.out.println("Cusp > "+frmCuspNumbernumber  +"  "+onPlntDegree +"  "+frmCupsDegree +"  "+degDiff);
			 hasAspect=true;
			}	
		}
		if(((bhavaDiff==4)||(bhavaDiff==8))&&(frmPlntnumber==GlobalVariablesKPExtension.MARS))//FOR ALL MARS
		{	
			if(bhavaDiff==4)
				degDiff-=90.00;
			if(bhavaDiff==8)
				degDiff-=210.00;
			
			if(degDiff<0.00)
				degDiff *=-1.00;
			
			if(degDiff<3.3333333334)
			{
			 //System.out.println("Planet > "+frmPlntnumber  +"  "+onPlntDegree +"  "+frmPlntDegree +"  "+degDiff);
			 hasAspect=true;
			}			
			
		}
	
		if(((bhavaDiff==5)||(bhavaDiff==9))&&((frmPlntnumber==GlobalVariablesKPExtension.JUPITER)||(frmPlntnumber==GlobalVariablesKPExtension.RAHU)||(frmPlntnumber==GlobalVariablesKPExtension.KETU)))//FOR JUPITER/RAHU/KETU
		{	
			if(bhavaDiff==5)
				degDiff-=120.00;
			
			if(bhavaDiff==7)
				degDiff-=180.00;
			if(bhavaDiff==9)
				degDiff-=240.00;
			
			if(degDiff<0.00)
				degDiff *=-1.00;
			if(degDiff<3.3333333334)
			{
			 //System.out.println("Cusp > "+frmCuspNumbernumber  +"  "+onPlntDegree +"  "+frmCupsDegree +"  "+degDiff);
			 hasAspect=true;
			}	
			
			if(((bhavaDiff==3)||(bhavaDiff==10))&&(frmPlntnumber==GlobalVariablesKPExtension.SAT))//FOR SAT
			{		
				if(bhavaDiff==3)
					degDiff-=60.00;
				if(bhavaDiff==10)
					degDiff-=270.00;
				if(degDiff<3.3333333334)
				{
				// System.out.println("Planet > "+frmPlntnumber  +"  "+onPlntDegree +"  "+frmPlntDegree +"  "+degDiff);
				 hasAspect=true;
				}			
				
			}
			
		}
		
		
		
		
		return hasAspect;
	}
	
	public  boolean isPlanetStrongSignificatorIn4Step(int plntNumber)
	{
		boolean nSL=false,ows=false,isPlanetStrongSignificator=false;		
		nSL=checkPlanetIsStarLordOfOtherPlanet(plntNumber);
		ows=isPlanetInItsOwnStar(plntNumber);
		if(ows)
		 {				
			isPlanetStrongSignificator=true;
			
		 }
		 else if(!nSL)
		 {
			 isPlanetStrongSignificator=true;
		 }
		
		return isPlanetStrongSignificator;
	}
	
	public int[] getPlanetsAspectToPlanetIn4StepN2(int planetNumber) {
		// TODO Auto-generated method stub
		int index = 0;
		double plntDegree=0.0;
		int[] aspectToPlanet = null;
		
		aspectToPlanet = new int[4];

		
		if(get4StepTypeN2(planetNumber)==null)
			return null;
		
		for (int f = 0; f < aspectToPlanet.length; f++)
			aspectToPlanet[f] = -1;
		plntDegree = this.planetDegree[planetNumber];
		
		/*if((planetNumber!=GlobalVariablesKPExtension.KETU) || (planetNumber!=GlobalVariablesKPExtension.RAHU))		
		{
			for (int cuspN = 0; cuspN < 12; cuspN++) {
				  if(getRahuKetAspectOnCusp(cuspN,plntDegree,this.cuspDegree[cuspN]))					
					 aspectToPlanet[index++] = cuspN+1;
				}
		}		
		else
		{*/if((planetNumber!=GlobalVariablesKPExtension.KETU) || (planetNumber!=GlobalVariablesKPExtension.RAHU))	
		{
			for (int plntN = 0; plntN < 9; plntN++) {
				if (planetNumber != plntN) {
					if(planetNumber==GlobalVariablesKPExtension.KETU && plntN==GlobalVariablesKPExtension.RAHU||
							planetNumber==GlobalVariablesKPExtension.RAHU && plntN==GlobalVariablesKPExtension.KETU	)
							continue;
					else
					{
						if(getPlanetAspect(plntN,plntDegree,this.planetDegree[plntN]))	
						{
							aspectToPlanet[index++] = plntN;						
						
						 // System.out.println(plntN);
						}
							
					}
					
				}
			}
				
			}
		//}
		if(index==0)
			return null;
		
		return CUtilsKPExtension.removeUpdatedDuplicates(CUtilsKPExtension
				.removeValueFromIntArray(-1, aspectToPlanet));
	}
	public int [] getPlanetConjunctionIn4StepNew(int planetNumber)
	{
		// CHECK FOR PLANET CONJUNCTION
		int index = 0;
		int[] step4PlntConjection = null;
		step4PlntConjection = new int[4];

			
		//IF PLANET IS STRONG SIGNIFICATOR
		for (int f = 0; f < step4PlntConjection.length; f++)
			step4PlntConjection[f] = -1;
		double plntconjDiff = 0.0, plntDegree = 0.0;
		plntDegree = this.planetDegree[planetNumber];

		for (int plntN = 0; plntN < 9; plntN++) {
			if (planetNumber != plntN) {
				plntconjDiff = plntDegree - this.planetDegree[plntN];
				if (plntconjDiff < 0)
					plntconjDiff *= -1;
				int compResult = Double.compare(plntconjDiff, 3.333333333);
				if (!(compResult > 0)) {
					step4PlntConjection[index++] = plntN;
					//System.out.println(plntN);

				}
			}
		}
		if(index==0)//NO CONJUCTION
			return null;
		
		return CUtilsKPExtension.removeUpdatedDuplicates(CUtilsKPExtension
				.removeValueFromIntArray(-1, step4PlntConjection));
	  
	}
	public int[] getPlanetsAspectToPlanetIn4Step3(int planetNumber) {
		// TODO Auto-generated method stub
		int index = 0;
		double plntDegree=0.0;
		int[] aspectToPlanet = null;
		
		aspectToPlanet = new int[4];

		
		
		
		for (int f = 0; f < aspectToPlanet.length; f++)
			aspectToPlanet[f] = -1;
		plntDegree = this.planetDegree[planetNumber];
		
		/*if((planetNumber!=GlobalVariablesKPExtension.KETU) || (planetNumber!=GlobalVariablesKPExtension.RAHU))		
		{
			for (int cuspN = 0; cuspN < 12; cuspN++) {
				  if(getRahuKetAspectOnCusp(cuspN,plntDegree,this.cuspDegree[cuspN]))					
					 aspectToPlanet[index++] = cuspN+1;
				}
		}		
		else
		{*/if((planetNumber!=GlobalVariablesKPExtension.KETU) || (planetNumber!=GlobalVariablesKPExtension.RAHU))	
		{
			for (int plntN = 0; plntN < 9; plntN++) {
				if (planetNumber != plntN) {
					if(planetNumber==GlobalVariablesKPExtension.KETU && plntN==GlobalVariablesKPExtension.RAHU||
							planetNumber==GlobalVariablesKPExtension.RAHU && plntN==GlobalVariablesKPExtension.KETU	)
							continue;
					else
					{
						if(getPlanetAspect(plntN,plntDegree,this.planetDegree[plntN]))	
						{
							aspectToPlanet[index++] = plntN;						
						
						  //System.out.println(plntN);
						}
							
					}
					
				}
			}
				
			}
		//}
		if(index==0)
			return null;
		
		return CUtilsKPExtension.removeUpdatedDuplicates(CUtilsKPExtension
				.removeValueFromIntArray(-1, aspectToPlanet));
	}
	
	private boolean hasAspectPlanetOnCuspIn4step(int plntNumber,int frmCuspNumber,double onPlntDegree,double frmCupsDegree)
	{
		boolean hasAspect=false;
		double degDiff=0.00;
		//int bhavaDiff= (int)(onPlntDegree/30.00)-(int)(frmCupsDegree/30.00);
		int bhavaDiff= (int)(frmCupsDegree/30.00)-(int)(onPlntDegree/30.00);
		if(bhavaDiff<0)
			bhavaDiff +=12;
		
		bhavaDiff +=1;
		
		//degDiff=onPlntDegree-frmCupsDegree;
		degDiff=frmCupsDegree-onPlntDegree;
		if(degDiff<0.00)
			degDiff +=360.00;
	
		

		if(bhavaDiff==7)//FOR ALL PLANETS
		{				
			if(bhavaDiff==7)
				degDiff-=180.00;
			
			if(degDiff<0.00)
				degDiff *=-1.00;
			
			if(!(Double.compare(degDiff, 3.333333333)>0))
			{
			 hasAspect=true;
			// System.out.println("Cusp MARS> "+frmCuspNumber  +"  "+onPlntDegree +"  "+frmCupsDegree +"  "+degDiff);
			}
			
		}
		if(((bhavaDiff==4)||(bhavaDiff==8))&&(plntNumber==GlobalVariablesKPExtension.MARS))//FOR ALL MARS
		{	
			if(bhavaDiff==4)
				degDiff-=90.00;
			if(bhavaDiff==8)
				degDiff-=210.00;
			
			if(degDiff<0.00)
				degDiff *=-1.00;
			
			if(!(Double.compare(degDiff, 3.333333333)>0))
			{
				 //System.out.println("Cusp MARS> "+frmCuspNumber  +"  "+onPlntDegree +"  "+frmCupsDegree +"  "+degDiff);
			 hasAspect=true;
			}			
			
		}
		if(((bhavaDiff==5)||(bhavaDiff==9))&&((plntNumber==GlobalVariablesKPExtension.JUPITER)||(plntNumber==GlobalVariablesKPExtension.RAHU)||(plntNumber==GlobalVariablesKPExtension.KETU)))//FOR JUPITER/RAHU/KETU
		{	
			if(bhavaDiff==5)
				degDiff-=120.00;
		
			if(bhavaDiff==9)
				degDiff-=240.00;
			
			if(degDiff<0.00)
				degDiff *=-1.00;
			
			//System.out.println("Cusp JUP> "+frmCuspNumber  +"  "+onPlntDegree +"  "+frmCupsDegree +"  "+degDiff +"   "+plntNumber);
			
			if(!(Double.compare(degDiff, 3.333333333)>0))
			{
			 //System.out.println("Cusp JUP> "+frmCuspNumber  +"  "+onPlntDegree +"  "+frmCupsDegree +"  "+degDiff +"   "+plntNumber);
			 hasAspect=true;
			}			
			
		}
		if(((bhavaDiff==3)||(bhavaDiff==10))&&(plntNumber==GlobalVariablesKPExtension.SAT))//FOR SAT
		{	
			
			
			if(bhavaDiff==3)
				degDiff-=60.00;
			if(bhavaDiff==10)
				degDiff-=270.00;
			
			if(degDiff<0.00)
				degDiff *=-1.00;
			
			 //System.out.println("BIJ > "+frmCuspNumber  +"  "+onPlntDegree +"  "+frmCupsDegree +"  "+degDiff +"   "+bhavaDiff);
			 
			if(degDiff<3.3333333334)
			{
				// System.out.println("Cusp SAT > "+frmCuspNumber  +"  "+onPlntDegree +"  "+frmCupsDegree +"  "+degDiff +"   "+bhavaDiff);
			 hasAspect=true;
			}			
			
		}
		
		
		
		
		return hasAspect;
	}
	

	/**
	 * This function is used to check that a passed number is present in array
	 * @param arr
	 * @param number
	 * @return boolean
	 */
	
	private boolean hasNumberInIntegerArray(int[]arr,int number)
	{	
		for(int i =0;i<arr.length;i++)
			if(arr[i]==number)
				return true;	
				
		return false;
	}
	
	
	/**
	 * THIS FUNCTION RETUNR THE TYPE 1 CUSPAL INTERLINK
	 * 
	 * @param cuspNumber
	 * @return int[] (array of bhava number)
	 */
	public int[] getKB_CIL_Type1(int cuspNumber) {
		int []type1=new int[12];
		
		//STEP-1:GET CUSP SUB LORD(PLANET)
		 int cuspSubLord=getSubLord(this.cuspDegree[cuspNumber]);//PLANET NUMBER OF CUSP SUB LORD
		 
		
		//STEP-2:GET  STAR LORD OF THE PLANET THAT COMES IN STEP 1
		 int plntStarLord=getStarLord(this.planetDegree[cuspSubLord]);
		
		//STEP-3 IDENTIFY THE CUSP THAT HAVE THE SAME SUB LORD PLANET AS PLANET  CAME IN STEP 2
		 int index=-1;
		 int[] arrayCuspSubLord=new int[12];
		 for(int j=0;j<12;j++)
			 arrayCuspSubLord[j]=getSubLord(this.cuspDegree[j]);
		 for(int i=0;i<this.cuspDegree.length;i++)
		 {
			 if(arrayCuspSubLord[i]==plntStarLord)
				 type1[++index]=i+1;
		 }
		 
		
		//STEP-4:GET THE SUB LORD OF THE PLANET THAT HAVE COME IN STEP 1
		 int plntSubLord=getSubLord(this.planetDegree[cuspSubLord]);
		 
		//STEP-5:IDENTIFY THE CUSP THAT HAVE THE SAME SUB LORD PLANET AS PLANET  CAME IN STEP 4
		
		 for(int k=0;k<this.cuspDegree.length;k++)
			 if(arrayCuspSubLord[k]==plntSubLord)							
				if(!hasNumberInIntegerArray(type1,(k+1)))
					 type1[++index]=k+1;
			
		 
		 if(index==-1)
			  return null;
		 
		 return CUtilsKPExtension.removeValueFromIntArray(0, type1);
	}

	/**
	 * THIS FUNCTION RETUNR THE TYPE 2 CUSPAL INTERLINK
	 * 
	 * @param cuspNumber
	 * @return int[] (array of bhava number)
	 */
	public int[] getKB_CIL_Type2(int cuspNumber) {
		// TODO Auto-generated method stub
		int []type2=new int[12];
				
		//STEP-1:FIND THE SUB LORD OF THE CUSP
		 int cuspSubLord=getSubLord(this.cuspDegree[cuspNumber]);//PLANET NUMBER OF CUSP SUB LORD
		 int index=-1;
		 int[] arrayCuspSubLord=new int[12];
		 for(int j=0;j<12;j++)
			 arrayCuspSubLord[j]=getSubLord(this.cuspDegree[j]);
		/* for(int i=0;i<this.cuspDegree.length;i++)
		 {
			 if(i!=cuspNumber)
				 if(arrayCuspSubLord[i]==cuspSubLord)
					 type2[++index]=i+1;
		 }*/
		 //STEP:2
		 int plntStarLord=getStarLord(this.planetDegree[cuspSubLord]);
		 
		 for(int i=0;i<this.cuspDegree.length;i++)
		 {
			 if(i!=cuspNumber)
				 if(arrayCuspSubLord[i]==plntStarLord)
					 type2[++index]=i+1;
		 }
		 
		/* int[] arrayCuspSubSubLord=new int[12];
		 for(int j=0;j<12;j++)
			 arrayCuspSubSubLord[j]=getSubSubLord(this.cuspDegree[j]);
		 
		 for(int i=0;i<this.cuspDegree.length;i++)
		 {
			 if(i!=cuspNumber)
				 if(arrayCuspSubSubLord[i]==plntStarLord)
					 type2[++index]=i+1;
		 }*/
		
		 if(index==-1)
			  return null;
		 
		 return CUtilsKPExtension.removeValueFromIntArray(0, type2);
	}

	/**
	 * THIS FUNCTION RETUNR THE TYPE 3 CUSPAL INTERLINK
	 * 
	 * @param cuspNumber
	 * @return int[] (array of bhava number)
	 */
	public int[] getKB_CIL_Type3(int cuspNumber) {

		// TODO Auto-generated method stub
		//STEP-1:GET THE SUB LORD OF THE CUSP
		 int cuspSubLord=getSubLord(this.cuspDegree[cuspNumber]);//PLANET NUMBER OF CUSP SUB LORD
		 //END STEP-1

		
		 int tempStep2Index=-1;
		 int[] tempStep2=new int[12];
		 
		 //INITIALIZE THE ARRAY WITH VALUE 0
		  for(int fill=0;fill<tempStep2.length;fill++)
			  tempStep2[fill]=0;
		  
		  for(int i =0;i<12;i++)
			  if(cuspNumber!=i)
			  {		 
				 if(getStarLord(this.planetDegree[ getSubLord(this.cuspDegree[i])])==cuspSubLord)				
					 tempStep2[++tempStep2Index]=i+1;
			  }
		  
		  if(tempStep2Index==-1)
			  return null;
		//END	  
		 /*for(int i=0;i<this.planetDegree.length;i++)
			 if(getStarLord(this.planetDegree[i])==cuspSubLord)
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
			 for(int l=0;l<this.cuspDegree.length;l++)
				if(getSubLord(this.cuspDegree[l])==planetsInStep2[k])
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
		 */
		
		  return CUtilsKPExtension.removeValueFromIntArray(0,tempStep2 );
		
	}

	/**
	 * THIS FUNCTION RETUNR THE TYPE 4 CUSPAL INTERLINK
	 * 
	 * @param cuspNumber
	 * @return int ( bhava number:1-12)
	 */
	public int getKB_CIL_Type4(int cuspNumber) {
		//STEP-1-:GET THE SUB LORD OF THE CUSP
		 int cuspLord=getSubLord(this.cuspDegree[cuspNumber]);
		
		//STEP-2-:GET THE STAR LORD OF THE PLANET FIND IN STEP-1
		 int starLord=getStarLord(this.planetDegree[cuspLord]);
		//FIND THE CUSP IN WHICH THE PLANET FOUND IN STEP-2, IS PLACED.
		 int houseOccupied=getHouseOccupied(this.planetDegree[starLord]);
		 
		return houseOccupied;
	}
	
}
