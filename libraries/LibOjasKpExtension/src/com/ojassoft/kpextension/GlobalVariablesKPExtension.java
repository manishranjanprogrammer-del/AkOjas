package com.ojassoft.kpextension;

public class GlobalVariablesKPExtension {

	public static int y1[] = { 7, 20, 6, 10, 7, 18, 16, 19, 17 };
	public static final  int SUN=0;
	public static final  int MOON=1;
	public static final  int MARS=2;
	public static final  int MERCURY=3;
	public static final  int JUPITER=4;
	public static final  int VENUS=5;
	public static final  int SAT=6;
	public static final  int RAHU=7;
	public static final  int KETU=8;
	/*public static final  int URANUS=9;
	public static final  int NEPTUNE=10;
	public static final  int PLUTO=11;*/
	
	public static final  int CUSP1=0;
	public static final  int CUSP2=1;
	public static final  int CUSP3=2;
	public static final  int CUSP4=3;
	public static final  int CUSP5=4;
	public static final  int CUSP6=5;
	public static final  int CUSP7=6;
	public static final  int CUSP8=7;
	public static final  int CUSP9=8;
	public static final  int CUSP10=9;
	public static final  int CUSP11=10;
	public static final  int CUSP12=11;

	public static final int[] PLANET_NAKSHTRA_LORD = {
		KETU, VENUS, SUN,MOON, MARS, RAHU, JUPITER, SAT,MERCURY,
		KETU, VENUS, SUN, MOON, MARS,RAHU, JUPITER, SAT, MERCURY,
		KETU, VENUS, SUN, MOON, MARS,RAHU, JUPITER, SAT, MERCURY };
	
	public static final int[][] PLANET_RASHI={
		{5,0},//SUN
		{4,0},//MOON
		{1,8},//MARS
		{3,6},//MERCURY
		{9,12},//JUPITER
		{2,7},//VENUS
		{10,11},//SAT
		{0,0},//RAHU
		{0,0}//KETU
		
		
		};
	/**
	 * HERE WE ARE CONSIDERING PLANET RASHI FROM 1-12
	 */
	public static int[]RASHI_TO_PLANET={
		-1,
		MARS,
		VENUS,
		MERCURY,
		MOON,
		SUN,
		MERCURY,
		VENUS,
		MARS,
		JUPITER,
		SAT,
		SAT,
		JUPITER		
		
	};

	public static final  int RASHI_ARIES=0;
	public static final int RASHI_TAURUS=1;
	public static final int RASHI_GEMINI=2;
	public static final int RASHI_CANCER=3;	
	public static final int RASHI_LEO=4;
	public static final int RASHI_VIRGO=5;
	public static final int RASHI_LIBRA=6;	
	public static final int RASHI_SCORPIO=7;	
	public static final int RASHI_SAGITTARIUS=8;
	public static final int RASHI_CAPRICORN=9;
	public static final int RASHI_AQUARIUS=10;
	public static final int RASHI_PISCES=11;
	public static final int RASHI_NO=-1;
	
	
	public static int[] PLANET_INDEX={SUN,MOON,MARS,MERCURY,JUPITER,VENUS,SAT,RAHU,KETU};//ADDED BY BIJENDRA ON 8-MARCH-2013
	
	public static final boolean[] HAVE_PLANET_TWO_RASHI={
		false,//SUN
		false,//MOON
		true,//MARS
		true,//MERCURY
		true,//JUPITER
		true,//VENUS
		true,//SAT
		false,//RAHU
		false//KETU
		
		};
	
}
