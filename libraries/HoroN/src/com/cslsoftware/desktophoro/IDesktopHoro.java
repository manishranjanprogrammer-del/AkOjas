package com.cslsoftware.desktophoro;

public interface IDesktopHoro {

	public static final int SUNDAY = 0;
	public static final int MONDAY = 1;
	public static final int TUESDAY = 2;
	public static final int WEDNESDAY = 3;
	public static final int THURSDAY = 4;
	public static final int FRIDAY = 5;
	public static final int SATURDAY = 6;

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract StringBuffer asp_chalit();

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract StringBuffer asp_kpcusp();

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract StringBuffer asp_planet();

	/**
	 * Insert the method's description here.
	 * Creation date: (21/11/2002 9:35:36 AM)
	 */
	public abstract String[] calcBaladi();

	/**
	 * Insert the method's description here.
	 * Creation date: (15/11/2002 3:46:23 PM)
	 */
	public abstract String[] calcChara();

	/**
	 * Insert the method's description here.
	 * Creation date: (21/11/2002 9:35:36 AM)
	 */
	public abstract String[] calcDeepta();

	/**
	 * Insert the method's description here.
	 * Creation date: (15/11/2002 3:46:23 PM)
	 */
	public abstract double calcEndHoraTime(int dayOrNight);

	/**
	 * Insert the method's description here.
	 * Creation date: (15/11/2002 3:46:23 PM)
	 */
	public abstract void calcHoraDayTimeAndDayLordValue();

	/**
	 * Insert the method's description here.
	 * Creation date: (15/11/2002 3:46:23 PM)
	 * corrected bracket which was wrong for night Hora on 22 June 2012
	 */
	public abstract void calcHoraNightTimeAndDayLordValue();

	/**
	 * Insert the method's description here.
	 * Creation date: (21/11/2002 9:35:36 AM)
	 */
	public abstract String[] calcJaagrat();

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract int calculateBhavNo(int planetRasi, int lagnaRasi);

	/**
	 * This method is used to get vimsottari dasa
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract String getAnterDasa(int i, int j, int k);

	/**
	 * This method is used to get longitude of Bhav Begin for given Bhav number
	 * @ return double
	 * @ param int
	 */
	public abstract StringBuffer getBalanceOfDasa();

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract int[] getBhavaForPrediction(double plnt);

	/**
	 * This method is used to calculate Planet to BhavMadhya Aspect for a given
	 planetNo and Bhav No
	 
	 * @ return int
	 * @ param  int
	 * @ param int
	 */
	public abstract String[] getBhavValueForBhavMadhya(int bhavNo);

	/**
	 * This method is used to calculate Planet to BhavMadhya Aspect for a given
	 planetNo and Bhav No
	 
	 * @ return int
	 * @ param  int
	 * @ param int
	 */
	public abstract String[] getBhavValueForKPCusp(int bhavNo);

	public abstract String[] getBhavValueForPlanets(int planetNo);

	/**
	 * This method is used to get vimsottari dasa
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract String getBhuktiDasa(int i, int j);

	/**
	 * Insert the method's description here.
	 * Creation date: (14/11/2002 2:25:10 PM)
	 */
	public abstract String[] getCharaGrahaForkaraka();

	/**
	 * Insert the method's description here.
	 * Creation date: (14/11/2002 2:16:08 PM)
	 */
	public abstract String[] getChogadiaDayName();

	/**
	 * Insert the method's description here.
	 * Creation date: (14/11/2002 2:16:08 PM)
	 */
	public abstract String[] getChogadiaNightName();

	/**
	 * Insert the method's description here.
	 * Creation date: (14/11/2002 2:25:10 PM)
	 */
	public abstract String[] getChoghadiaDayTime();

	/**
	 * Insert the method's description here.
	 * Creation date: (14/11/2002 2:25:10 PM)
	 */
	public abstract String[] getChoghadiaNightTime();

	/**
	 * Insert the method's description here.
	 * Creation date: (14/11/2002 2:25:10 PM)
	 */
	public abstract String[] getDayForRahu();

	/**
	 * Insert the method's description here.
	 * Creation date: (14/11/2002 2:25:10 PM)
	 */
	public abstract String[] getDayHoraTime();

	/**
	 * Insert the method's description here.
	 * Creation date: (14/11/2002 2:25:10 PM)
	 */
	public abstract String[] getDayLordNameForRahukaal();

	/**
	 * Insert the method's description here.
	 * Creation date: (14/11/2002 2:25:10 PM)
	 */
	public abstract String[] getDayLrdForHora();

	/**
	 * Insert the method's description here.
	 * Creation date: (14/11/2002 2:25:10 PM)
	 * Added this method which was missing earlier on 22 June 2012 
	 */
	public abstract String[] getNightLrdForHora();

	/**
	 * Insert the method's description here.
	 * Creation date: (14/11/2002 2:25:10 PM)
	 */
	public abstract String[] getGrahaForAvastha();

	/**
	 * Insert the method's description here.
	 * Creation date: (14/11/2002 2:25:10 PM)
	 */
	public abstract String[] getkaraka();

	/**
	 * This method is used to get vimsottari dasa
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract String getMahaDasa(int i);

	/**
	 * Insert the method's description here.
	 * Creation date: (14/11/2002 2:25:10 PM)
	 */
	public abstract String[] getNightHoraTime();

	/**
	 * This method is used to get Prediction.
	 * @return StringBuffer
	 */
	public abstract int getNoCorrespondingToMahaDasa(String mahaDasa);

	/**
	 * This method is used to calculate Planet to BhavMadhya Aspect for a given
	 planetNo and Bhav No
	 
	 * @ return int
	 * @ param  int
	 * @ param int
	 */
	public abstract String getPlanetToBhavMadhyaAspect(int planetNo, int bhavNo);

	/**
	 * This method is used to calculate planet to BhavMadhya Weight for a given
	 planetno and bhavno
	 
	 * @ return double
	 * @ param int
	 * @ param int
	 */
	public abstract String getPlanetToBhavMadhyaWeight(int planetNo, int bhavNo);

	/**
	 * This method is used to calculate aspect for planet to cusp
	 for a given planetno and cuspno
	 
	 * @ return int
	 * @ param  int
	 * @ param int
	 */
	public abstract String getPlanetToKPCuspAspect(int planetNo, int cuspNo);

	/**
	 * This method is used to calculate AspectWeight for a planet to KPCusp
	 for a given planetno and cuspno
	 
	 * @ return double
	 * @ param  int
	 * @ param int
	 */
	public abstract String getPlanetToKPCuspAspectWeight(int planetNo,
			int cuspNo);

	/**
	 * This method is used to calculate Aspect for a planet to another planet
	 * @ return int 
	 * @ param  int
	 * @ param int
	 */
	public abstract String getPlanetToPlanetAspect(int planetNo1, int planetNo2);

	/**
	 * This method is used to calculate Aspect Weight for a given planet to another
	   planet
	 * @return double
	 * @param  int
	 * @param  int
	 */
	public abstract String getPlanetToPlanetAspectWeight(int planetNo1,
			int planetNo2);

	public abstract String getPlanetWeight(int planetNo);

	/**
	 * Insert the method's description here.
	 * Creation date: (14/11/2002 2:25:10 PM)
	 */
	public abstract String[] getRahukaalTime();

	/**
	 * Insert the method's description here.
	 * Creation date: (14/11/2002 2:25:10 PM)
	 */
	public abstract String[] getSthirGrahaForkaraka();

	/**
	 * This method is used to get vimsottari dasa
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract String getVimsottariDasaFourLevelString();

	public abstract String getVimsottariDasaThreeLevelString();

	public abstract String getVimsottariDasaTwoLevelString();

	/**
	 * This method is used to get vimsottari dasa
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract StringBuffer getVimsottariDasaFourLevel();

	/**
	 * This method is used to get vimsottari dasa
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract StringBuffer getVimsottariDasaThreeLevel();

	/**
	 * This method is used to get vimsottari dasa
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract StringBuffer getVimsottariDasaTwoLevel();

	/**
	 * This method is used to get Prediction.
	 * @return StringBuffer
	 */
	public abstract int getYearsCorrespondingToMahaDasa(String mahaDasa);

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract StringBuffer horoprn();

	/**
	 * This method is used to initialise values required for mathematical calculations.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract void initialize() throws Exception;

	/**
	 * Insert the method's description here.
	 * Creation date: (14/11/2002 2:25:10 PM)
	 */
	public abstract void initializeChoghadiaStartDayAndNight();

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract void initializesArrayValues() throws Exception;

	/**
	 * Insert the method's description here.
	 * Creation date: (21/11/2002 9:35:36 AM)
	 */
	public abstract StringBuffer printAvastha();

	/**
	 * Insert the method's description here.
	 * Creation date: (14/11/2002 2:16:08 PM)
	 */
	public abstract StringBuffer printChogadia();

	/**
	 * Insert the method's description here.
	 * Creation date: (14/11/2002 2:25:10 PM)
	 */
	public abstract void printChoghadiaTime(double riseORset, double diff);

	/**
	 * Insert the method's description here.
	 * Creation date: (21/11/2002 9:35:36 AM)
	 * broken into two loops for night hora by Punit on 22 June 2012
	 */
	public abstract StringBuffer printHora();

	/**
	 * Insert the method's description here.
	 * Creation date: (21/11/2002 9:35:36 AM)
	 */
	public abstract StringBuffer printKaraka();

	/**
	 * Insert the method's description here.
	 * Creation date: (15/11/2002 3:46:23 PM)
	 */
	public abstract StringBuffer printRahuKaal1() throws Exception;

	/**
	 * This method is used to get longitude of Bhav Begin for given Bhav number
	 * @ return double
	 * @ param int
	 */
	public abstract void setFromYearForSookshmadasa(int fromYear);

	/**
	 * This method is used to get longitude of Bhav Begin for given Bhav number
	 * @ return double
	 * @ param int
	 */
	public abstract void setTillYearForSookshmadasa(int tillYear);

}