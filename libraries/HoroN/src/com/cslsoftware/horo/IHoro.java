package com.cslsoftware.horo;

import java.util.Enumeration;

public interface IHoro {

	//Akshvedamsa
	public abstract int akshvedamsa(double deg);

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract double getAsc();

	/**
	 * This method is used to get other details of the user.
	 * previously other
	 * @return StringBuffer
	 */
	public abstract void calculateAvakadha();

	/**
	 * This method is used to get Significators of Bhavas  
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract void calculateSigBhav();

	//Chaturtamsa
	public abstract int chaturtamsa(double deg);

	/**
	 * This method is used to set the value of TimeZone
	 * @param String Timezone
	 */
	public abstract Enumeration getAllTimeZones();

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract int getAyanamsaType();

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract String getCompanyAddLine1();

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract String getCompanyName();

	/**
	 * This method is used to get user's day of birth 
	 * @return int
	 */
	public abstract int getDayOfBirth();

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract String getDegreeOfLattitude();

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract String getDegreeOfLongitude();

	//Drekkana
	public abstract int getDrekkana(double deg);

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract String getDST();

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract int getDstWRTTimeZone(String timeZone1);

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract String getEastWest();

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract int getHoroscopeStyle();

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract String getHourOfBirth();

	/**
	 * This method is used to get Planetary Longitude (Planetary Degree) for a given planet number
	 * @return double
	 * @param planetNo String
	 */
	public abstract double getJupitor();

	/**
	 * This method is used to get karan
	 * @return String Karan
	 */
	public abstract int getKaran();

	/**
	 * This method is used to get Planetary Longitude (Planetary Degree) for a given planet number
	 * @return double
	 * @param planetNo String
	 */
	public abstract double getKetu();

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract int getKPAyanamsaType();

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract int getKPHorarySeed();

	/**
	 * This method is used to get Planetary Longitude (Planetary Degree) for a given planet number
	 * @return double
	 * @param planetNo String
	 */
	public abstract String getLanguageCode();

	public abstract int getLatitudeType();

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract String getMaleFemale();

	/**
	 * This method is used to get Planetary Longitude (Planetary Degree) for a given planet number
	 * @return double
	 * @param planetNo String
	 */
	public abstract double getMars();

	/**
	 * This method is used to get Planetary Longitude (Planetary Degree) for a given planet number
	 * @return double
	 * @param planetNo String
	 */
	public abstract double getMercury();

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract String getMinuteOfBirth();

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract String getMinuteOfLattitude();

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract String getMinuteOfLongitude();

	/**
	 * Insert the method's description here.
	 * Creation date: (5/22/02 1:11:20 PM)
	 * @return java.lang.String
	 */
	public abstract int getMonthOfBirth();

	/**
	 * This method is used to get Planetary Longitude (Planetary Degree) for a given planet number
	 * @return double
	 * @param planetNo String
	 */
	public abstract double getMoon();

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract String getName();

	/**
	 * This method is used to get Planetary Longitude (Planetary Degree) for a given planet number
	 * @return double
	 * @param planetNo String
	 */
	public abstract double getNeptune();

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract String getNorthSouth();

	/**
	 * This method is used to set the value of place of  birth
	 * @param String userplace
	 */
	public abstract String getPlace();

	/**
	 * This method is used to get Planetary Longitude (Planetary Degree) for a given planet number
	 * @return double
	 * @param planetNo String
	 */
	public abstract double getPluto();

	/**
	 * This method is used to get Planetary Longitude (Planetary Degree) for a given planet number
	 * @return double
	 * @param planetNo String
	 */
	public abstract double getRahu();

	/**
	 * This method is used to get Planetary Longitude (Planetary Degree) for a given planet number
	 * @return double
	 * @param planetNo String
	 */
	public abstract double getSaturn();

	/**
	 * This method is used to set the value of second part of user's birth time
	 * @param String second
	 */
	public abstract String getSecondOfBirth();

	/**
	 * This method is used to set the value of Second part Of Lattitude
	 * @param String latsecond
	 */
	public abstract String getSecondOfLattitude();

	/**
	 * This method is used to set the value of Second part Of Longitude
	 * @param String longsecond
	 */
	public abstract String getSecondOfLongitude();

	/**
	 * This method is used to get Planetary Longitude (Planetary Degree) for a given planet number
	 * @return double
	 * @param planetNo String
	 */
	public abstract double getSun();

	/**
	 * This method is used to set the value of TimeZone
	 * @param String Timezone
	 */
	public abstract String getTimeZone();

	//Trimshamsa

	/**
	 * This method is used to get Planetary Longitude (Planetary Degree) for a given planet number
	 * @return double
	 * @param planetNo String
	 */
	public abstract double getUranus();

	//Akshvedamsa
	public abstract int[][] getVarga();

	/**
	 * This method is used to get Planetary Longitude (Planetary Degree) for a given planet number
	 * @return double
	 * @param planetNo String
	 */
	public abstract double getVenus();

	/**
	 *This method is used to get user's year of Birth
	 *@return  int
	 */
	public abstract int getYearOfBirth();

	/**
	 * This method is used to initialise values required for mathematical calculations.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract void initialize() throws Exception;

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract void initializesArrayValues() throws Exception;

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract boolean isDifferentDst();

	/**
	 * This method calculates julian day for a particular date
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract int jd(int d, int m, int y);

	//Khavedamsa
	public abstract int khavedamsa(double deg);

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract char[] replstr(char string[], int no, String plnt_no2);

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract String rnss(double d);

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract void saptavg();

	/**
	 * 0 -> Lahiri, 1 -> KP, 2 -> Raman, 3 -> Sayan, 4 -> Customize
	 * Creation date: (5/17/02 6:38:34 PM)
	 * @deprecated  replaced by setAyan() which doesn't require to call
	 * setAyanamsaType and setKPAyanamsaType seperately
	 */
	public abstract void setAyanamsaType(int ayanamsaType);

	/**
	 * 0 -> Lahiri, 1 -> KP New, 2 -> KP Old, 3 -> Raman, 4-> KP Khullar, 5 -> Sayan, 6 -> Customized 
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract void setAyan(int ayanamsaType);

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract void setCompanyAddLine1(String companyAddLine1);

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract void setCompanyName(String companyname);

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract void setCustomizedAyanamsa(String ayanamsa);

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract void setDayOfBirth(String day)
			throws NumberFormatException, NullPointerException;

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract void setDegreeOfLattitude(String latdegree)
			throws NumberFormatException, NullPointerException;

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract void setDegreeOfLongitude(String longdegree)
			throws NumberFormatException, NullPointerException;

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract void setDST(String dst) throws NumberFormatException,
			NullPointerException;

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract void setEastWest(String eastWest)
			throws NumberFormatException, NullPointerException;

	/**
	 * This method is used to calculate sunrise and sunset time.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract void setHinduWeekday();

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract void setHoroscopeStyle(int kt);

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract void setHourOfBirth(String hour)
			throws NumberFormatException, NullPointerException;

	/**
	 * 0 -> KP Old, 1 -> KP New, 2 -> KP Khullar
	 * Creation date: (5/17/02 6:38:34 PM)
	 * @deprecated  replaced by setAyan() which doesn't require to call
	 * setAyanamsaType and setKPAyanamsaType seperately
	 */
	public abstract void setKPAyanamsaType(int kpAyanamsaType);

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract void setKPHorarySeed(int seed);

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract void setLanguageCode(String lang);

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract void setLatitudeType(int latitudeType);

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract void setMaleFemale(String gender);

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract void setMinuteOfBirth(String minute)
			throws NumberFormatException, NullPointerException;

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract void setMinuteOfLattitude(String latminute)
			throws NumberFormatException, NullPointerException;

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract void setMinuteOfLongitude(String longminute)
			throws NumberFormatException, NullPointerException;

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract void setMonthOfBirth(String month)
			throws NumberFormatException, NullPointerException;

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract void setName(String username);

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract void setNorthSouth(String northSouth)
			throws NullPointerException;

	/**
	 * This method is used to set the value of place of  birth
	 * @param String userplace
	 */
	public abstract void setPlace(String userplace);

	/**
	 * This method is used to set the value of second part of user's birth time
	 * @param String second
	 */
	public abstract void setSecondOfBirth(String second)
			throws NumberFormatException, NullPointerException;

	/**
	 * This method is used to set the value of Second part Of Lattitude
	 * @param String latsecond
	 */
	public abstract void setSecondOfLattitude(String latsecond)
			throws NumberFormatException, NullPointerException;

	/**
	 * This method is used to set the value of Second part Of Longitude
	 * @param String longsecond
	 */
	public abstract void setSecondOfLongitude(String longsecond)
			throws NumberFormatException, NullPointerException;

	/**
	 * This method is used to set the value of TimeZone
	 * @param String Timezone
	 */
	public abstract void setTimeZone(String timezone)
			throws NumberFormatException, NullPointerException;

	/**
	 * This method is used to set the value of year of birth
	 * @param String year
	 */
	public abstract void setYearOfBirth(String year)
			throws NumberFormatException;

	//Siddhamsa
	public abstract int siddhamsa(double deg);

	//Vimshamsa
	public abstract int vimshamsa(double deg);

	public abstract String toString();

}