package com.ojassoft.kpextension;

import java.util.ArrayList;
import java.util.Arrays;



public class CUtilsKPExtension {
	static int y1[] = { 7, 20, 6, 10, 7, 18, 16, 19, 17 };

	/**
	 * return nak lord for given planet longitude
	 * 
	 * @param d
	 *            (planet longitude)
	 * @return nak lord
	 * @author hukum
	 */
	public static int getPlanetNakLord(double d) {
		// String sb = null;
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
			if (y1[i] <= d)
				d -= y1[i];
			else
				break;
		}
		b = i;
		d = d / y1[b] * (40.0 / 3.0);
		d *= 9;
		for (c = 0; c < 9; c++) {
			i = b + c;
			if (i >= 9)
				i -= 9;
			if (y1[i] <= d)
				d -= y1[i];
			else
				break;
		}
		// System.out.println(ConstGlobalVariables.nakLord[a]);
		// c = i;
		// sb = ConstGlobalVariables.nakLord[a];

		return GlobalVariablesKPExtension.PLANET_NAKSHTRA_LORD[a];
	}
	public static int getSubLord(double d) {
		//String[] result = new String[4];
		
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
			if (y1[i] <= d)
				d -= y1[i];
			else
				break;
		}
		b = i;
		d = d / y1[b] * (40.0 / 3.0);
		d *= 9;
		for (c = 0; c < 9; c++) {
			i = b + c;
			if (i >= 9)
				i -= 9;
			if (y1[i] <= d)
				d -= y1[i];
			else
				break;
		}
		c = i;
		
		/*result[0] = ConstGlobalVariables.rasiLord[f];
	    result[1] = ConstGlobalVariables.nakLord[a];
		result[2] = ConstGlobalVariables.nakLord[b];
		result[3] = ConstGlobalVariables.nakLord[c];*/

		//RETURN SUB LORAD
		return GlobalVariablesKPExtension.PLANET_NAKSHTRA_LORD[b];
	}

	public static int getSubSubLord(double d) {
		//String[] result = new String[4];
		
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
			if (y1[i] <= d)
				d -= y1[i];
			else
				break;
		}
		b = i;
		d = d / y1[b] * (40.0 / 3.0);
		d *= 9;
		for (c = 0; c < 9; c++) {
			i = b + c;
			if (i >= 9)
				i -= 9;
			if (y1[i] <= d)
				d -= y1[i];
			else
				break;
		}
		c = i;
		
		/*result[0] = ConstGlobalVariables.rasiLord[f];
	    result[1] = ConstGlobalVariables.nakLord[a];
		result[2] = ConstGlobalVariables.nakLord[b];
		result[3] = ConstGlobalVariables.nakLord[c];*/

		//RETURN SUB SUB LORAD
		return GlobalVariablesKPExtension.PLANET_NAKSHTRA_LORD[c];
	}

	/**
	 * This function put planet that comes between passed cups1 and cusp2 values
	 * @param cusp2
	 * @param cusp1
	 * @param cuspIndex
	 * @return Bhave number from 0-11;
	 */
	public static int getPlanetInBhav(double cusp2, double cusp1,
			int cuspIndex, double plntDegree) {
		double temp2 = cusp2;
		double temp1 = cusp1;
		int plantInBhava = -1;

		if ((temp2 - temp1) < 0)
			temp2 += 360.00;

		for (int i = 0; i < 12; i++) {
			if ((temp1 < plntDegree) && (plntDegree < temp2)) {
				plantInBhava = cuspIndex;
			}
			if ((temp1 < plntDegree + 360.0) && (plntDegree + 360.0 < temp2)) {
				plantInBhava = cuspIndex;
			}
		}
		return (plantInBhava + 1);

	}

	/**
	 * This function put planet that comes between passed cups1 and cusp2 values
	 * 
	 * @param cusp2
	 * @param cusp1
	 * @param cuspIndex
	 * @return Bhave number from 0-11;
	 */
	public static int getRashiInBhav(double cusp2, double cusp1, int cuspIndex,
			double plntDegree) {
		double temp2 = cusp2;
		double temp1 = cusp1;
		int plantInBhava = -1;

		if ((temp2 - temp1) < 0)
			temp2 += 360.00;

		for (int i = 0; i < 12; i++) {
			if ((temp1 < plntDegree) && (plntDegree < temp2)) {
				plantInBhava = cuspIndex;
			}
			if ((temp1 < plntDegree + 360.0) && (plntDegree + 360.0 < temp2)) {
				plantInBhava = cuspIndex;
			}
		}
		return (plantInBhava + 1);

	}
	/**
	 * @author Bijendra(20-march-2013)
	 * This function is used to remove duplicate values from the array
	 * @param array
	 * @return int[]
	 */
	 
	/* public static int[] removeDuplicates(int[] array) {
	        int del = 0;
	        Arrays.sort(array);
	        for( int i = 0; i < array.length - (1 + del); ++i ) {
	            for( int j = array.length - (1 + del); j > i; --j ) {
	                if( array[i] == array[j]) {
	                    for( int k = j; k < array.length - (1 + del); ++k ) {
	                        array[k] = array[k + 1];
	                    }
	                    array[array.length - 1] = 0;
	                    del++;
	                }
	            }
	        }
	        return Arrays.copyOfRange(array, 0, array.length - del);
	    }*/
	 /**
	  * This function is used to remove the  value passed as first parameter from the array
	  * @param valueToRemove
	  * @param array
	  * @return int[]
	  */
	 public static int[] removeValueFromIntArray(int valueToRemove,int[] array)
	 {
		 int []returnVal=null;
		 int arrayLength=0,index=-1;
		 
		 if(array==null)
			 return null;
		 
		 if(array.length==0)
			 return array;
		 
		 for(int i=0;i<array.length;i++)
			 if(array[i]!=valueToRemove)
				 ++arrayLength;
		 
		 returnVal=new int[arrayLength];
		 
		 for(int i=0;i<array.length;i++)
			 if(array[i]!=valueToRemove)
				 returnVal[++index]=array[i];			 
		 
		 
		 return returnVal;
	 }
	 /**
	  * This function is used to return rashi number in which planet is placed
	  * @param value
	  * @return int
	  */
	public static int getRashiInPlanetPlaced(double value)
	{
		return (int) ((value/30.00)+1);
	}
	public static int[] removeUpdatedDuplicates(int[] array) {
		
		if(array==null)
			return null;
		Arrays.sort(array);
		boolean hasDuplicate=false;
		 ArrayList<Integer> list=new ArrayList<Integer>();
		 
			 for(int i=0;i<array.length;i++)
			 {
				 hasDuplicate=false;
				 for(int j=i+1;j<array.length;j++)
					 if(array[i]==array[j])
						 hasDuplicate=true;
				 
				 if(!hasDuplicate)
					 list.add(array[i]);
						 
				 
			 }
			 
				 int []p=new int[list.size()];
				 for(int k=0;k<list.size();k++)
					 p[k]=list.get(k);
		 
		  return p;
	    }
	
	
}
