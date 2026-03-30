package com.cslsoftware.enghoro;

public interface IEngHoro {

	/**************************************************************************
	 * Function :CalcMangalDosh(This function calculate the mangal dosh of user)
	 *
	 * Paremeeters:no
	 *
	 *Return Type : no
	 ****************************************************************************/
	public abstract String CalcMangalDoshString();

	/**
	 * This method is used to get Longitude of Ascendent
	 * @return Double
	 */
	public abstract String getAscendentDms();

	/**
	 * This method is used to get Longitude of Ascendent
	 * @return Double
	 */
	public abstract String getAscendentNakshatraName();

	/**
	 * This method is used to get Longitude of Ascendent
	 * @return Double
	 */
	public abstract int getAscendentPada();

	/**
	 * This method is used to get Longitude of Ascendent
	 * @return Double
	 */
	public abstract String getAscendentRasiDms();

	/**
	 * This method is used to get sign name for given Longitude NEWMETHOD
	 * @return String
	 */
	public abstract String getSignNameForLongitude(double longitude);

	/**
	 * This method returns sign lord name for a given longitude NEWMETHOD
	 * @ return int
	 */
	public abstract String getSignLordNameForLongitude(double longitude);

	/**
	 * This method returns nakshatra lord name for given longitude NEWMETHOD
	 * @return Double
	 */
	public abstract String getNakshatraLordNameForLontitude(double longitude);

	/**
	 * This method is used to get Longitude of Ascendent
	 * @return Double
	 */
	public abstract String getAscendentSignName();

	/**
	 * This method is used to get Longitude of Ascendent
	 * @return Double
	 */
	public abstract String getAscendentSubLordName();

	/**
	 * This method is used to get Ashtakvarga Bindu for a given signNo&planetNo
	 * @ return int array
	 * @ param int,int 
	 */
	public abstract int getAshtakvargaBinduForSignAndPlanet(int planetNo,
			int signNo);

	/**
	 * This method is used to get karan
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract int[] getAspectToBhav();

	/**
	 * This method is used to get karan
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract String[] getAspectValueToBhav();

	/**
	 * This method is used to get Longitude of Ascendent
	 * @return Double
	 */
	public abstract String getAyanamsaDms();

	/**
	 * This method is used to get longitude of Bhav Begin for given Bhav number
	 * @ return double
	 * @ param int
	 */
	public abstract String getBhavBeginForBhavDms(int bhavNo);

	/**
	 * This method is used to get Longitude of Ascendent
	 * @return Double
	 */
	public abstract String getDayDurationHms();

	/**
	 * This method is used to get  Fortuna
	 * @return double
	 */
	public abstract String getFortunaDms();

	/**
	 * This method is used to get Gana Name 
	 * @return String
	 */
	public abstract String getGanaName();

	/**
	 * This method is used to get GMT at the time of Birth
	 * @return String
	 */
	public abstract String getGmtAtBirthHms();

	/**
	 * This method is used to calculate Hindu Weekday.
	 * Hindu Day changes after sunrise whereas English day 
	 * changes after 12:00 night.
	 */
	public abstract String getHinduWeekdayName();

	/**
	 * This method is used to calculate English Weekday.
	 * English day changes after 12:00 night whereas
	 * Hindu Day changes after sunrise.
	 */
	public abstract String getWeekdayName();

	/**
	 * This method is used to calculate sunrise and sunset time.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract int[] getHouselord(int planetNo, int shodashvargaType);

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract String getIndianSunSignName();

	/**
	 * This method is used to get Ishtkaal
	 * @return String Ishtkaal
	 */
	public abstract String getIshtkaalDms();

	/**
	 * This method is used to get Julian day
	 * @return int Julian day
	 */
	public abstract String getJulianDayValue();

	/**
	 * This method is used to get karan
	 * @return String Karan
	 */
	public abstract String getKaranName();

	/**
	 * This method is used to get Longitude of Ascendent
	 * @return Double
	 */
	public abstract String getKPAscendentNakshatraLordName();

	/**
	 * This method is used to get the Lord of Ascendent by KP System
	 * @ return int
	 */
	public abstract String getKPAscendentSignLordName();

	/**
	 * This method is used to get Longitude of Ascendent
	 * @return Double
	 */
	public abstract String getKPAscendentSubLordName();

	/**
	 * This method is used to get KP Ayanamsa
	 *@  return double
	 */
	public abstract String getKPAyanamsaLongitudeDms();

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
	public abstract String getKPCuspLongitudeDms(int cuspNo);

	/**
	 * This method is used to calculate the DayLord by KPSystem
	 * @return int
	 */
	public abstract String getKPDayLordName();

	/**
	 * This method is used to calculate the longitude of Fortuna by KPSystem
	 * @ return double
	 */
	public abstract String getKPFortunaLongitudeDms();

	/**
	 * This method is used to calculate the NakshatraLord of Moon by
	   KPSystem
	 * @ return int
	 */
	public abstract String getKPMoonNakshatraLordName();

	/**
	 * This method is used to calculate SignLord of Moon by
	 KPSystem
	 * @ return int
	 */
	public abstract String getKPMoonSignLordName();

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract String getKPMoonSubLordName();

	/**
	 * This method is used to calculate the PlanetDegree by KPSystem for a given
	  planetNo
	 
	 * @ return double
	 * @ param int
	 */
	public abstract String getKPPlanetLongitudeDms(int planetNo);

	/**
	 * not sure
	 * @ return int[]
	 * @ param int
	 */
	public abstract int[] getKPPlnetsInPlanetNakshatra(int planetNo);

	/**
	 * This method is used to get Lagna Lord
	 * @return String
	 */
	public abstract String getLagnaLordName();

	/**
	 * This method is used to get Lagna Degree
	 * @return Double
	 */
	public abstract String getLagnaSign();

	/**
	 * This method is used to get LMT correction 
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract String getLMTCorrectionHms();

	/**
	 * This method is used to get LMT of Birth
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract String getLMTOfBirthHms();

	/**
	 * This method is used to calculate the Longitude Of Planet
	  for a given panetNo
	 * @ return double
	 * @ param int 
	 */
	public abstract double getLongitudeOfPlanet(int planetNo);

	public abstract int getMarsInBhavForMoonChart();

	/**
	 * This method is used to get MidBhav for given Bhav Number
	 * @return double
	 * @param int
	 */
	public abstract double getMidBhavForBhava(int bhavNo);

	/**
	 * Commented by Punit on Nov. 14, 2005 as seems incorrect and already there in PureHoro
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	/*
	public int getMoonSign() {
		double sunSign = plnt[1] + plnt[0];
		if (sunSign >= 360.0)
			sunSign -= 360.0;
		int rasi = (int) (sunSign / 30);
		return rasi;
	}
	 */
	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract String getMoonSubLordName();

	/**
	 * This method is used to get NadiName 
	 *@ return String
	 */
	public abstract String getNadiName();

	/**
	 *This method is used to get Nakshatra lord forMoon
	 *@return  String
	 */
	public abstract String getNakshatraLordName();

	/**
	 * This method is used to get  Nakshatra for a given cusp number
	  by KPSystem 
	 * @ return int
	 * @ param  int
	 */
	public abstract String getNakshatraLordNameForKPCusp(int cuspNo);

	/**
	 *This method is used to get Nakshatra for a given planet number
	 *@ return  int
	 *@ param int 
	 */
	public abstract String getNakshatraLordNameForKPPlanet(int planetNo);

	/**
	 * This method is used to get Nakshatra Name (Moon's nakshatra)
	 
	 * @ return int
	 */
	public abstract String getNakshatraName();

	/**
	 * This method is used to get Nakshatra Name (Moon's nakshatra)
	 
	 * @ return int
	 */
	public abstract String getNakshatraNameForPlanet(int planetNo);

	/**
	 *This method is used to get Obliquity
	 *@return double
	 */
	public abstract String getObliquityDms();

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
	public abstract String getPakshaName();

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract String getPayaName();

	/**
	 * This method is used to get Planetary Longitude (Planetary Degree) for a given planet number
	 * @return double
	 * @param planetNo String
	 */
	public abstract String getPlanetaryLongitudeDms(int planetNo);

	/**
	 * This method is used to get Planetary Rasi for a given planet number
	 * @return int
	 * @param planetNo int
	 */
	public abstract String getPlanetaryRasiDms(int planetNo);

	/**
	 * This method is used to get Planetary Rasi for a given planet number
	 * @return int
	 * @param planetNo int
	 */
	public abstract String getPlanetaryRasiName(int planetNo);

	/**
	 * This method is used to get Planetary Rasi for a given planet number
	 * @return int
	 * @param planetNo int
	 */
	public abstract String getPlanetaryRasiNameForShodashvargaDivision(
			int planetNo, int shodashvargaDivision);

	/**
	 * Insert the method's description here.
	 * Creation date: (04-03-2003 6:05:49 PM)
	 */
	public abstract int[] getPlanetAspectFromShodashvargaDivision(int planetNo,
			int shodashvargaDivision);

	/**
	 * This method is used to get karan
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract int[] getPlanetInBhav();

	/**
	 * This method is used to get karan
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract int[] getPlanetInBhav(int shodashvargaType);

	/**
	 * This method is used to calculate the Rashi for a given cusp
	 * @ return int 
	 * @ param int
	 */
	public abstract String getRashiNameForKPCusp(int cusp);

	/**
	 * This method is used to calculate the Rashi for a given cusp
	 * @ return int 
	 * @ param int
	 */
	public abstract String getRashiNameForKPPlanet(int planetNo);

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract String[] getRasiAndItsDegree(double totaldeg);

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract String[] getRasiAndRasilordAndItsDegree(double totaldeg);

	/**
	 * This method is used to get (Moon's) Rasi Lord name
	 * MODIFIED - PLEASE TEST IT
	 * @return String
	 */
	public abstract String getRasiLordName();

	/**
	 * This method is used to get (Moon's) Rasi name 
	 * @return String
	 */
	public abstract String getRasiName();

	/**
	 * Insert the method's description here.
	 * Creation date: (04-03-2003 5:24:25 PM)
	 */
	public abstract int getRelationshipForShodashvargaDivision(
			int planetNumber, int shodashvargaDivision);

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
	public abstract String getSiderealTimeHms();

	/**
	 *This method is used to get KP SubLord for a given cusp number
	 *@return  int
	 *@param cuspNumber int 
	 */
	public abstract String getSubLordNameForKPCusp(int cuspNo);

	/**
	 *This method is used to get KP SubLord for a given cusp number
	 *@return  int
	 *@param cuspNumber int 
	 */
	public abstract String getSubLordNameForKPPlanet(int planetNo);

	/**
	 *This method is used to get KP SubSub for a given degree
	 * @return String
	 * @param degree double
	 */
	public abstract String getSubSubLordNameForKPCusp(int cuspNo);

	/**
	 *This method is used to get KP SubSub for a given degree
	 * @return String
	 * @param degree double
	 */
	public abstract String getSubSubLordNameForKPPlanet(int planetNo);

	/**
	 *This method is used to get KP SubSub for a given degree
	 * @return String
	 * @param degree double
	 */
	public abstract String getSubSubName(double d);

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract String getSunRiseTimeHms();

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract String getSunSetTimeHms();

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract int getSunSignLord();

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract String getSunSignName();

	/**
	 * This method is used to get Tithi Name of user's birth day according to Hindu System
	 * @return String 
	 */
	public abstract String getTithiName();

	/**
	 * This method is used to get Tithi Name of user's birth day according to Hindu System
	 * @return String 
	 */
	public abstract String getTithiValueName();

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
	public abstract String getVarnaName();

	/**
	 *This method is used to get Vasya Name 
	 *@return  String
	 */
	public abstract String getVasyaName();

	/**
	 * This method is used to get user's day of birth 
	 * @return int
	 */
	public abstract String getWarDaylightCorrectionHms();

	/**
	 *This method is used to get Yoga
	 *@return String
	 */
	public abstract String getYoganame();

	/**
	 *This method is used to get Yoga
	 *@return String
	 */
	public abstract String getYogaValueName();

	/**
	 *This method is used to get Yoni
	 *@return  String
	 */
	public abstract String getYoniName();

	/**
	 * Insert the method's description here.
	 * Creation date: (04-03-2003 6:05:49 PM)
	 */
	public abstract void initializePlanetAspectToForShodashvargaDivision(
			int planetNo, int shodashvargaDivision);

	public abstract boolean isMangalDosh();

	public abstract boolean isMangalDoshForMoonChart();

	/**
	 * Insert the method's description here.
	 * Creation date: (04-03-2003 6:05:49 PM)
	 */
	public abstract void newMethod();

}