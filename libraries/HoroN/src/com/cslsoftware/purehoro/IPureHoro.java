package com.cslsoftware.purehoro;

public interface IPureHoro {

	public static final int SUN = 1;
	public static final int MOON = 2;
	public static final int MARS = 3;
	public static final int MERCURY = 4;
	public static final int JUPITER = 5;
	public static final int VENUS = 6;
	public static final int SATURN = 7;

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract StringBuffer balance(double moon);

	/**************************************************************************
	 * Function :CalcMangalDosh(This function calculate the mangal dosh of user)
	 *
	 * Paremeeters:no
	 *
	 *Return Type : no
	 ****************************************************************************/
	public abstract int CalcMangalDosh();

	public abstract double degreesInSign(double degree);

	/**
	 * This method is used to get Longitude of Ascendent
	 * @return Double
	 */
	public abstract double getAscendentLongitude();

	/**
	 * This method is used to get Longitude of Ascendent
	 * @return Double
	 */
	public abstract int getAscendentSign();

	/**
	 * This method is used to get Longitude of Ascendent
	 * @return Double
	 */
	public abstract int getAscendentSubLord();

	/**
	 * This method is used to get Ashtakvarga Bindu for a given signNo&planetNo
	 * @ return int array
	 * @ param int,int 
	 */
	public abstract int getAshtakvargaBinduForSignAndPlanet(int signNo,
			int planetNo);

	/**
	 * This method is used to get Longitude of Ascendent
	 * @return Double
	 */
	public abstract double getAyanamsa();

	/**
	 * This method is used to get longitude of Bhav Begin for given Bhav number
	 * @ return double
	 * @ param int
	 */
	public abstract double getBhavBeginForBhav(int bhavNo);

	/**
	 * This method is used to get Longitude of Ascendent
	 * @return Double
	 */
	public abstract double getDayDuration();

	/**
	 * This method is used to get  Fortuna
	 * @return double
	 */
	public abstract double getFortuna();

	/**
	 * This method is used to get Gana Name 
	 * @return String
	 */
	public abstract int getGana();

	/**
	 * This method is used to get  Fortuna
	 * @return double
	 */
	public abstract double getGmtAtBirth();

	/**
	 * This method is used to calculate sunrise and sunset time.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract int getHinduWeekday();

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract int getIndianSunSign();

	/**
	 * This method is used to get Ishtkaal
	 * @return String Ishtkaal
	 */
	public abstract double getIshtkaal();

	/**
	 * This method is used to get Julian day
	 * @return int Julian day
	 */
	public abstract int getJulianDay();

	/**
	 * This method is used to get Lagna Degree
	 * @return Double
	 */
	public abstract int getjvalue();

	/**
	 *This method is used to get Nakshatra lord forMoon
	 *@return  String
	 */
	public abstract int getKPAscendentNakshatraLord();

	/**
	 * This method is used to get Longitude of Ascendent
	 * @return Double
	 */
	public abstract int getKPAscendentSubLord();

	/**
	 * This method is used to get KP Ayanamsa
	 *@  return double
	 */
	public abstract double getKPAyanamsaLongitude();

	/**
	 * This method is used to calculate the Occuptants of Bhav
	 for a given bhav no by KP System
	 * @ return int []
	 * @ param int
	 */
	public abstract int[] getKPBhavOccupants(int bhavNo);

	/**
	 * This method is used to calculate the owner of Bhav for a
	 given BhavNo by KPSystem 
	 *@ return int
	 */
	public abstract int getKPBhavOwner(int bhavaNo);

	/**
	 * This method is used to get KP Cusp Degree for a given cusp number
	 * @ return double
	 * @ param  int
	 */
	public abstract double getKPCuspLongitude(int cuspNo);

	/**
	 * This method is used to calculate the longitude of Fortuna by KPSystem
	 * @ return double
	 */
	public abstract double getKPFortunaLongitude();

	/**
	 * This method is used to calculate SignLord of Moon by
	 KPSystem
	 * @ return int
	 */
	public abstract int getKPMoonSubLord();

	/**
	 *This method is used to get Nakshatra lord forMoon
	 *@return  String
	 */
	public abstract int getKPNakshatraLord();

	/**
	 * This method is used to calculate the PlanetDegree by KPSystem for a given
	  planetNo
	 
	 * @ return double
	 * @ param int
	 */
	public abstract double getKPPlanetLongitude(int planetNo);

	/**
	 * This method is used to get Planet's Signification for a given planet number
	 * PROBABLY WRONG - PLEASE TEST IT
	 * @return  String
	 * @param planetNo int
	 */
	public abstract int[] getKPPlanetSignification(int planetNo);

	/**
	 * This method is used to calculate signification of a planet
	   by KPSystem for a given planetNo
	 
	 * @ return int[]
	 * @ param  int
	 */
	public abstract int[] getKPPlanetSignificator(int planetNo);

	/**
	 * not sure
	 * @ return int[]
	 * @ param int
	 */
	public abstract int[] getKPPlnetsInPlanetNakshatra(int planetNo);

	/**
	 * This method is used to get Lagna Degree
	 * @return Double
	 */
	public abstract int getLagna();

	/**
	 * This method is used to get LMT correction 
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract double getLMTCorrection();

	/**
	 * This method is used to get LMT of Birth
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract double getLMTOfBirth();

	/**
	 * This method is used to calculate the Longitude Of Planet
	  for a given panetNo
	 * @ return double
	 * @ param int 
	 */
	public abstract double getLongitudeOfPlanet(int planetNo);

	/**
	 * This method is used to get MidBhav for given Bhav Number
	 * @return double
	 * @param int
	 */
	public abstract double getMidBhavForBhava(int bhavNo);

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract int getMoonSign();

	/**
	 * This method is used to calculate SignLord of Moon by
	 KPSystem
	 * @ return int
	 */
	public abstract int getMoonSubLord();

	/**
	 * This method is used to get NadiName 
	 *@ return String
	 */
	public abstract int getNadi();

	/**
	 * This method is used to get Nakshatra Name (Moon's nakshatra)
	 
	 * @ return int
	 */
	public abstract int getNakshatra();

	/**
	 * Insert the method's description here.
	 * Creation date: (7/26/02 12:41:36 PM)
	 */
	public abstract int getNakshatra(double d);

	/**
	 * This method is used to get  Nakshatra for a given cusp number
	  by KPSystem 
	 * @ return int
	 * @ param  int
	 */
	public abstract int getNakshatraForKPCusp(int cuspNo);

	/**
	 *This method is used to get Nakshatra for a given planet number
	 *@ return  int
	 *@ param int 
	 */
	public abstract int getNakshatraForKPPlanet(int planetNo);

	/**
	 *This method is used to get Nakshatra for a given planet number
	 *@return  int
	 *@param planetNumber int 
	 */
	public abstract int getNakshatraForPlanet(int j);

	/**
	 *This method is used to get Nakshatra lord forMoon
	 *@return  String
	 */
	public abstract int getNakshatraLord();

	/**
	 *This method is used to get Obliquity
	 *@return double
	 */
	public abstract double getObliquity();

	/**
	 * This method is used to get  Pada for a given planet number
	 * @ return  int
	 * @ param  int 
	 */
	public abstract int getPadaOfPlanet(int planetNo);

	/**
	 * This method is used to get karan
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract int getPaksha();

	/**
	 * This method is used to get longitude of Bhav Begin for given Bhav number
	 * @ return double
	 * @ param int
	 */
	public abstract int getPanchadhaFriendshipOfPlanet(int planetNo);

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract int getPaya();

	/**
	 * This method is used to get longitude of Bhav Begin for given Bhav number
	 * @ return double
	 * @ param int
	 */
	public abstract int getPermanentFriendshipOfPlanet(int planetNo);

	/**
	 * This method is used to get Planetary Longitude (Planetary Degree) for a given planet number
	 * @return double
	 * @param planetNo String
	 */
	public abstract double getPlanetaryLongitude(int planetNo);

	/**
	 * This method is used to get Planetary Pada for a given planet number
	 * @return  int
	 * @param planetNo int 
	 */
	public abstract int getPlanetaryPada(int planetNo);

	/**
	 * This method is used to get Planetary Rasi for a given planet number
	 * @return int
	 * @param planetNo int
	 */
	public abstract int getPlanetaryRasi(int planetNo);

	/**
	 * Insert the method's description here.
	 * Creation date: (25/11/2002 11:56:43 AM)
	 */

	public abstract String getPlanetName(int planetNo);

	/**
	 * This method is used to get longitude of Bhav Begin for given Bhav number
	 * @ return double
	 * @ param int
	 */
	public abstract int[] getPositionForShodasvarg(int position);

	/**
	 * This method is used to get Ashtakvarga Bindu for a given signNo&planetNo
	 * @ return int array
	 * @ param int,int 
	 */
	public abstract int getPrastharashtakvargaTables(int tableNo1,
			int planetNo1, int bindu);

	/**
	 * This method is used to calculate the Rashi for a given cusp
	 * @ return int 
	 * @ param int
	 */
	public abstract int getRashiForKPCusp(int cusp);

	/**
	 * This method is used to calculate the Rashi for a given cusp
	 * @ return int 
	 * @ param int
	 */
	public abstract int getRashiForKPPlanet(int planetNo);

	/**
	 * This method is used to get (Moon's) Rasi name 
	 * @return String
	 */
	public abstract int getRasi();

	/**
	 * This method is used to get (Moon's) Rasi Lord name
	 * MODIFIED - PLEASE TEST IT
	 * @return String
	 */
	public abstract int getRasiLord();

	/**
	 * This method is used to get the Shodashvarga sign for a given varga and planetno
	 * @ return int[][]
	 * @ param int
	 * @ param int
	 */
	public abstract int getShodashvargaSignForVargaAndPlanet(int vargaNo,
			int planetNo);

	/**
	 *This method is used to get Sidereal Time
	 *@return double
	 */
	public abstract double getSiderealTime();

	/**
	 * This method is used to get longitude of Bhav Begin for given Bhav number
	 * @ return double
	 * @ param int
	 */
	public abstract int getSookshmaDasaForPlanet(int planetNo1, int planetNo2,
			int planetNo3, int planetNo4);

	/**
	 * This method is used to get Longitude of Ascendent
	 * @return Double
	 */
	public abstract int getSthiraAnatykarkaPlanet();

	/**
	 * This method is used to get Longitude of Ascendent
	 * @return Double
	 */
	public abstract int getSthiraAtmakarakaPlanet();

	/**
	 * This method is used to get Longitude of Ascendent
	 * @return Double
	 */
	public abstract int getSthiraBhatruPlanet();

	/**
	 * This method is used to get Longitude of Ascendent
	 * @return Double
	 */
	public abstract int getSthiraDarakarakaPlanet();

	/**
	 * This method is used to get Longitude of Ascendent
	 * @return Double
	 */
	public abstract int getSthiraGrathikarakaPlanet();

	/**
	 * This method is used to get Longitude of Ascendent
	 * @return Double
	 */
	public abstract int getSthiraMatruKarkaPlanet();

	/**
	 * This method is used to get Longitude of Ascendent
	 * @return Double
	 */
	public abstract int getSthiraPutrakarakaPlanet();

	/**
	 *This method is used to get KP SubLord for a given cusp number
	 *@return  int
	 *@param cuspNumber int 
	 */
	public abstract int getSubLordForKPCusp(int cuspNo);

	/**
	 *This method is used to get KP SubLord for a given cusp number
	 *@return  int
	 *@param cuspNumber int 
	 */
	public abstract int getSubLordForKPPlanet(int planetNo);

	/**
	 *This method is used to get KP SubSub for a given degree
	 * @return String
	 * @param degree double
	 */
	public abstract int getSubSubLordForKPCusp(int cuspNo);

	/**
	 *This method is used to get KP SubSub for a given degree
	 * @return String
	 * @param degree double
	 */
	public abstract int getSubSubLordForKPPlanet(int planetNo);

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract double getSunRiseTime();

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract double getSunSetTime();

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract int getSunSign();

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract int getSunSignLord();

	/**
	 * This method is used to get longitude of Bhav Begin for given Bhav number
	 * @ return double
	 * @ param int
	 */
	public abstract int getTemporaryFriendshipOfPlanet(int planetNo);

	/**
	 * This method is used to get Tithi Name of user's birth day according to Hindu System
	 * @return String 
	 */
	public abstract int getTithi();

	/**
	 * This method is used to get Tithi Name of user's birth day according to Hindu System
	 * @return String 
	 */
	public abstract double getTithiValue();

	/**
	 * This method is used to get Tithi Name of user's birth day according to Hindu System
	 * @return String 
	 */
	public abstract int[] getTotalAshtakVargaValue();

	/**
	 *This method is used to get Sidereal Time
	 *@return double
	 */
	public abstract double getTropicalCuspalLongitude();

	/**
	 *This method is used to get Sidereal Time
	 *@return double
	 */
	public abstract double getTropicalPlanetLongitude(int planetNo);

	/**
	 *This method is used to get Varna Name 
	 *@return  String
	 */
	public abstract int getVarna();

	/**
	 *This method is used to get Vasya Name 
	 *@return  String
	 */
	public abstract int getVasya();

	/**
	 * This method is used to get user's day of birth 
	 * @return int
	 */
	public abstract double getWarDaylightCorrection();

	/**
	 *This method is used to get Yoga
	 *@return String
	 */
	public abstract int getYoga();

	/**
	 *This method is used to get Yoga
	 *@return String
	 */
	public abstract double getYogaValue();

	/**
	 *This method is used to get Yoni
	 *@return  String
	 */
	public abstract int getYoni();

	/**
	 * This method is used to initialise values required for mathematical calculations.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract void initialize() throws Exception;

	/**
	 * Insert the method's description here.
	 * Creation date: (23/11/2002 3:00:03 PM)
	 */
	public abstract boolean isAccelerate(int planetNumber);

	/**
	 * Insert the method's description here.
	 * Creation date: (23/11/2002 3:00:03 PM)
	 */
	public abstract boolean isCombust(int planetNumber);

	/**
	 * Insert the method's description here.
	 * Creation date: (23/11/2002 3:00:03 PM)
	 */
	public abstract boolean isDeblited(int planetNumber, int sign);

	/**
	 * Insert the method's description here.
	 * Creation date: (23/11/2002 3:00:03 PM)
	 */
	public abstract boolean isExalted(int planetNumber, int sign);

	/**
	 * Insert the method's description here.
	 * Creation date: (23/11/2002 3:00:03 PM)
	 */
	public abstract boolean isInFriendSign(int planetNumber, int sign);

	/**
	 * Insert the method's description here.
	 * Creation date: (23/11/2002 3:00:03 PM)
	 */
	public abstract boolean isInLastQuarter(int planetNumber);

	/**
	 * Insert the method's description here.
	 * Creation date: (23/11/2002 3:00:03 PM)
	 */
	public abstract boolean isInNeutralSign(int planetNumber, int sign);

	/**
	 *This method is used to know if the Planetis direct or not
	 *@return boolean
	 *@param planetNo int
	 */
	public abstract boolean isPlanetDirect(int planetNo);

	/**
	 *This method is used to know if the Planetis direct or not
	 *@return boolean
	 *@param planetNo int
	 */
	public abstract boolean isPlanetRetrograde(int planetNo);

	/**
	 * Insert the method's description here.
	 * Creation date: (23/11/2002 3:00:03 PM)
	 */
	public abstract boolean isRetrograde(int planetNumber);

	/*****************************************************************************************
	 * Function :returnMangalDoshPoints(This function calculate the lavelof mangal dosh of user)
	 *
	 * Paremeeters:lagna number and moonrasi number
	 *
	 *Return Type : lavel of mangal dosh in points
	 ****************************************************************************/

	public abstract int returnMangalDoshPoints();

}