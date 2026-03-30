package com.cslsoftware.lalkitab;

public interface ILalKitab {

	public static final int LalKitabKundli = 1;
	public static final int ChandraKundli = 2;
	public static final int VarshaphalaKundli = 3;
	public static final int SUN = 1;
	public static final int MOON = 2;
	public static final int MARS = 3;
	public static final int MERCURY = 4;
	public static final int JUPITER = 5;
	public static final int VENUS = 6;
	public static final int SATURN = 7;
	public static final int RAHU = 8;
	public static final int KETU = 9;
	public static final int URANUS = 10;
	public static final int NEPTUNE = 11;
	public static final int PLUTO = 12;

	/**
	 * Insert the method's description here.
	 * Creation date: (11-03-2003 10:50:16 AM)
	 */
	public abstract int[] aamHaalatPlanetForKhanaFrom(int khanaNo);

	/**
	 * Insert the method's description here.
	 * Creation date: (11-03-2003 10:50:16 AM)
	 */
	public abstract int[] aamHaalatPlanetForKhanaTo(int khanaNo);

	/**
	 * Insert the method's description here.
	 * Creation date: (11-03-2003 10:50:16 AM)
	 */
	public abstract int[][] achanakChotPlanetForKhanaFrom(int khanaNo);

	/**
	 * Insert the method's description here.
	 * Creation date: (11-03-2003 10:50:16 AM)
	 */
	public abstract int[][] achanakChotPlanetForKhanaTo(int khanaNo);

	/**
	 * Insert the method's description here.
	 * Creation date: (11-03-2003 10:50:16 AM)
	 */
	public abstract int[] bahamMadadPlanetForKhanaFrom(int khanaNo);

	/**
	 * Insert the method's description here.
	 * Creation date: (11-03-2003 10:50:16 AM)
	 */
	public abstract int[] bahamMadadPlanetForKhanaTo(int khanaNo);

	/**
	 * Insert the method's description here.
	 * Creation date: (11-03-2003 10:50:16 AM)
	 */
	public abstract int[] dhokhaPlanetForKhanaFrom(int khanaNo);

	/**
	 * Insert the method's description here.
	 * Creation date: (11-03-2003 10:50:16 AM)
	 */
	public abstract int[] dhokhaPlanetForKhanaTo(int khanaNo);

	/**
	 * Insert the method's description here.
	 * Creation date: (9/18/02 4:09:43 PM)
	 */
	public abstract String[] getDeb();

	/**
	 * Insert the method's description here.
	 * Creation date: (9/18/02 4:09:43 PM)
	 */
	public abstract String getDeb(int khanaNo);

	/**
	 * Insert the method's description here.
	 * Creation date: (9/18/02 4:09:43 PM)
	 */
	public abstract String[] getExal();

	/**
	 * Insert the method's description here.
	 * Creation date: (9/18/02 4:09:43 PM)
	 */
	public abstract String getExal(int khanaNo);

	/**
	 * Insert the method's description here.
	 * Creation date: (9/18/02 4:09:43 PM)
	 */
	public abstract int getHouseDistance(int planetsHouse, int aspect);

	/**
	 * Insert the method's description here.
	 * Creation date: (9/18/02 4:09:43 PM)
	 */
	public abstract String[] getIstLevelLalKitabDasa();

	/**
	 * Insert the method's description here.
	 * Creation date: (9/18/02 4:09:43 PM)
	 */
	public abstract String[] getKhanaNo();

	/**
	 * Insert the method's description here.
	 * Creation date: (9/18/02 4:09:43 PM)
	 */
	public abstract String[] getKismatJagaanewaala();

	/**
	 * Insert the method's description here.
	 * Creation date: (11-03-2003 10:50:16 AM)
	 */
	public abstract int getlalkitabVarshaphalTableElement(int age, int khana);

	/**
	 * Insert the method's description here.
	 * Creation date: (9/18/02 4:09:43 PM)
	 */
	public abstract String[] getMaalik();

	/**
	 * Insert the method's description here.
	 * Creation date: (9/18/02 4:09:43 PM)
	 */
	public abstract String[] getPakkaGhar();

	/**
	 * Insert the method's description here.
	 * Creation date: (11-03-2003 10:50:16 AM)
	 */
	public abstract String getPlanetaryRashiNameForLalkitab(int planetNo);

	/**
	 * Insert the method's description here.
	 * Creation date: (11-03-2003 10:50:16 AM)
	 */
	public abstract int getPlanetaryRashiForLalkitab(int planetNo);

	/**
	 * Insert the method's description here.
	 * Creation date: (11-03-2003 10:50:16 AM)
	 */
	public abstract String getPlanetaryRashiNameForLalkitabVarshphal(
			int planetNo);

	/**
	 * Added by Punit for Lal Kitab Varshphal - Benefic/ Malefic.
	 * Returns house number of planet in LK varshphal.
	 * Creation date: (11-05-2005  10:50:16 AM)
	 */
	public abstract int getPlanetaryRashiForLalkitabVarshphal(int planetNo);

	/**
	 * Insert the method's description here.
	 * Creation date: (9/18/02 4:09:43 PM)
	 */
	public abstract int[] getPlanetInBhav(int typeOfKundli);

	/**
	 * Insert the method's description here.
	 * Creation date: (9/18/02 4:09:43 PM)
	 */
	public abstract int[] getPlanetInBhavForVarshaphala(int typeOfKundli);

	/**
	 * Insert the method's description here.
	 * Need to change for Rahu / Ketu as traditional function doesn't return value for Rahu and Ketu
	 */
	public abstract int getRelationship(int planetNumber);

	/**
	 * Insert the method's description here.
	 * Creation date: (2/3/03 1:11:46 PM)
	 */
	public abstract int getRelationship(int planetNumber, int house[]);

	/**
	 * Insert the method's description here.
	 * Creation date: (2/3/03 1:11:46 PM)
	 */
	public abstract int getRelationshipForVarshphal(int planetNumber,
			int house[]);

	/**
	 * Insert the method's description here.
	 * Creation date: (9/18/02 4:09:43 PM)
	 */
	public abstract String[] getSecondLevelLalKitabDasa();

	/**
	 * Insert the method's description here.
	 * Creation date: (9/18/02 4:09:43 PM)
	 */
	public abstract StringBuffer getTevaStatus();

	/**
	 * Insert the method's description here.
	 * Creation date: (9/10/02 10:47:42 AM)
	 */
	public abstract StringBuffer getTevaStatusForVarYear();

	/**
	 * Insert the method's description here.
	 * Creation date: (9/18/02 4:09:43 PM)
	 */
	public abstract int getVarshphalYear();

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract void initializesArrayValuesOfLalKitab();

	/**
	 * Insert the method's description here.
	 * Creation date: (9/18/02 4:09:43 PM)
	 */
	public abstract String isAndhaaTeva();

	/**
	 * Insert the method's description here.
	 * Creation date: (9/10/02 10:47:42 AM)
	 */
	public abstract String isAndhaaTevaForVarYear();

	/**
	 * Insert the method's description here.
	 * Creation date: (9/18/02 4:09:43 PM)
	 */
	public abstract String isDharmi(int planetNo);

	/**
	 * Insert the method's description here.
	 * Creation date: (9/18/02 4:09:43 PM)
	 */
	public abstract String isDharmiForVarshYear(int planetNo);

	/**
	 * Insert the method's description here.
	 * Creation date: (9/18/02 4:09:43 PM)
	 */
	public abstract String isInPakkaGhar(int planetNo);

	/**
	 * Insert the method's description here.
	 * Creation date: (9/18/02 4:09:43 PM)
	 */
	public abstract String isInPakkaGharForVarshYear(int planetNo);

	/**
	 * Insert the method's description here.
	 * Creation date: (9/18/02 4:09:43 PM)
	 */
	public abstract String isNabaligTeva();

	/**
	 * Insert the method's description here.
	 * Creation date: (9/10/02 10:47:42 AM)
	 */
	public abstract String isNabaligTevaForVarYear();

	/**
	 * Insert the method's description here.
	 * Creation date: (9/18/02 4:09:43 PM)
	 */
	public abstract boolean isOfDaughterSisterDebt();

	/**
	 * Insert the method's description here.
	 * Creation date: (9/18/02 4:09:43 PM)
	 */
	public abstract boolean isOfFatherDebt();

	/**
	 * Insert the method's description here.
	 * Creation date: (9/18/02 4:09:43 PM)
	 */
	public abstract boolean isOfJalimanaDebt();

	/**
	 * Insert the method's description here.
	 * Creation date: (9/18/02 4:09:43 PM)
	 */
	public abstract boolean isOfMangalDebt();

	/**
	 * Insert the method's description here.
	 * Creation date: (9/18/02 4:09:43 PM)
	 */
	public abstract boolean isOfMothresDebt();

	/**
	 * Insert the method's description here.
	 * Creation date: (9/18/02 4:09:43 PM)
	 */
	public abstract boolean isOfNaturalDebt();

	/**
	 * Insert the method's description here.
	 * Creation date: (9/18/02 4:09:43 PM)
	 */
	public abstract boolean isOfOwnDebt();

	/**
	 * Insert the method's description here.
	 * Creation date: (9/18/02 4:09:43 PM)
	 */
	public abstract boolean isOfPaidaHiNaHuaDebt();

	/**
	 * Insert the method's description here.
	 * Creation date: (9/18/02 4:09:43 PM)
	 */
	public abstract boolean isOfRelativesDebt();

	/**
	 * Insert the method's description here.
	 * Creation date: (9/18/02 4:09:43 PM)
	 */
	public abstract boolean isOfSisterDebt();

	/**
	 * Insert the method's description here.
	 * Creation date: (9/18/02 4:09:43 PM)
	 */
	public abstract boolean isOfWifesDebt();

	/**
	 * Insert the method's description here.
	 * Creation date: (9/18/02 4:09:43 PM)
	 */
	public abstract int[] isPapiGraha();

	/**
	 * Insert the method's description here.
	 * Creation date: (9/18/02 4:09:43 PM)
	 */
	public abstract boolean isPlanetAspectingSomeOtherPlanet(int planetNo);

	/**
	 * Insert the method's description here.
	 * Creation date: (9/10/02 10:47:42 AM)
	 */
	public abstract boolean isPlanetAspectingSomeOtherPlanetForVarYear(
			int planetNo);

	/**
	 * Insert the method's description here.
	 * Creation date: (9/18/02 4:09:43 PM)
	 */
	public abstract int isPlanetInKhana(int planetNo, int khanaNo);

	/**
	 * Insert the method's description here.
	 * Creation date: (9/18/02 4:09:43 PM)
	 */
	public abstract boolean isPlanetKismatJagganewala(int planetNo);

	/**
	 * Insert the method's description here.
	 * Creation date: (9/18/02 4:09:43 PM)
	 */
	public abstract boolean isPlanetKismatJagganewalaForVarshphala(int planetNo);

	/**
	 * Insert the method's description here.
	 * Creation date: (9/18/02 4:09:43 PM)
	 */
	public abstract boolean isPlanetSoya(int planetNo);

	/**
	 * Insert the method's description here.
	 * Creation date: (9/18/02 4:09:43 PM)
	 */
	public abstract boolean isPlanetSoyaForVarshphal(int planetNo);

	/**
	 * Insert the method's description here.
	 * Creation date: (9/18/02 4:09:43 PM)
	 */
	public abstract String isRataandhTeva();

	/**
	 * Insert the method's description here.
	 * Creation date: (9/10/02 10:47:42 AM)
	 */
	public abstract String isRataandhTevaForVarYear();

	/**
	 * Insert the method's description here.
	 * Creation date: (9/18/02 4:09:43 PM)
	 */
	public abstract StringBuffer lalkitabHouseDetail();

	public abstract StringBuffer lalkitabHousePosition();

	/**
	 * Insert the method's description here.
	 * Creation date: (11-03-2003 10:50:16 AM)
	 */
	public abstract int[][] mushtarkadeewarPlanetForKhanaFrom(int khanaNo);

	/**
	 * Insert the method's description here.
	 * Creation date: (11-03-2003 10:50:16 AM)
	 */
	public abstract int[][] mushtarkadeewarPlanetForKhanaTo(int khanaNo);

	/**
	 * Insert the method's description here.
	 * Creation date: (11-03-2003 10:50:16 AM)
	 */
	public abstract int[] nichPlanetForKhanaFrom(int khanaNo);

	/**
	 * Insert the method's description here.
	 * Creation date: (11-03-2003 10:50:16 AM)
	 */
	public abstract int[] nichPlanetForKhanaTo(int khanaNo);

	/**
	 * Insert the method's description here.
	 * Creation date: (11-03-2003 10:50:16 AM)
	 */
	public abstract int[] PlanetForKhana(int khanaNo);

	/**
	 * Insert the method's description here.
	 * Creation date: (9/18/02 4:09:43 PM)
	 */
	public abstract int PlanetInKhana(int planetNo);

	/**
	 * Insert the method's description here.
	 * Creation date: (9/18/02 4:09:43 PM)
	 */
	public abstract int[] PlanetsInKhana(int khanaNo);

	/**
	 * Insert the method's description here.
	 * Creation date: (9/18/02 4:09:43 PM)
	 */
	public abstract void setVarshphalYear(String vyear);

	/**
	 * Insert the method's description here.
	 * Creation date: (11-03-2003 10:50:16 AM)
	 */
	public abstract int[] takraoBuniyadPlanetForKhanaFrom(int khanaNo);

	/**
	 * Insert the method's description here.
	 * Creation date: (11-03-2003 10:50:16 AM)
	 */
	public abstract int[] takraoBuniyadPlanetForKhanaTo(int khanaNo);

	/**
	 * Insert the method's description here.
	 * Creation date: (9/18/02 4:09:43 PM)
	 */
	public abstract void vargaAndHouseInitialization();

	/**
	 * Insert the method's description here.
	 * Creation date: (9/18/02 4:09:43 PM)
	 */
	public abstract void vargaAndHouseInitializationForVarshaphala();

}