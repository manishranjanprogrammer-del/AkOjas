package com.cslsoftware.util;

import java.io.InputStream;

public interface IConstants {

	public static final String dayChoghadia = "DAY_CHOGHADIA_VALUES";
	public static final String nightChoghadia = "NIGHT_CHOGHADIA_VALUES";
	public static final String aspect = "ASPECT_VALUES";
	public static final String ras1 = "RAS1_VALUES";
	public static final String yoginiNameInFull = "YOGINI_NAME_IN_FULL_VALUES";
	public static final String yogini = "YOGINI_VALUES";
	public static final String yoginiLord = "LORD_OF_YOGINI";
	public static final String day = "DAY_VALUES";
	public static final String daylrd = "DAYLRD_VALUES";
	public static final String ras = "RAS_VALUES";
	public static final String raslrd = "RASLRD_VALUES";
	public static final String naklrd = "NAKLRD_VALUES";
	public static final String tit = "TIT_VALUES";
	public static final String nak = "NAK_VALUES";
	public static final String yog = "YOG_VALUES";
	public static final String x1 = "X1_VALUES";
	public static final String graha = "GRAHA_VALUES";
	public static final String bhavanum = "BHAVANUM_VALUES";
	public static final String Karan = "KARAN_VALUES";
	public static final String paksha = "PAKSHA_VALUES";
	public static final String Jaagrat = "JAAGRAT_VALUES";
	public static final String Baladi = "BALADI_VALUES";
	public static final String Deepta = "DEEPTA_VALUES";
	public static final String karaka = "KARAKA_VALUES";
	public static final String dayrashipati = "DAY_TRIRASHIPATI";
	public static final String nightrashipati = "NIGHT_TRIRASHIPATI";
	public static final int punyaSaham = 0;
	public static final int educationSaham = 1;
	public static final int popularitySaham = 2;
	public static final int friendsSaham = 3;
	public static final int fatherSaham = 4;
	public static final int motherSaham = 5;
	public static final int lifeSaham = 6;
	//public static final int foreignSaham=7;
	public static final int karmaSaham = 7;
	public static final int deathSaham = 8;
	public static final int foreignTravelsSaham = 9;
	public static final int wealthSaham = 10;
	public static final int adulterySaham = 11;
	public static final int diseaseSaham = 12;
	public static final int anyaKarmaSaham = 13;
	public static final int tradeSaham = 14;
	public static final int karyaSiddhiSaham = 15;
	public static final int marriageSaham = 16;
	public static final int progenySaham = 17;
	public static final int loveSaham = 18;
	public static final int businessSaham = 19;
	public static final int enemiesSaham = 20;
	public static final int imprisonmentSaham = 21;
	public static final int financialGainsSaham = 22;
	//String div[] = {"  LAGNA   ", " DREKKANA ", "SAPTHAMSA ", " NAVAMSA  ", "  DASAMSA ", "DWADASAMSA", "SHODASAMSA"};
	public static final String div = "DIV_VALUES";
	//String divnum[] = {" 01 ", " 02 ", " 03 ", " 04 ", " 07 ", " 09 ", " 10 ", " 12 ", " 16 ", " 20 ", " BH ", " SI ", " 03 ", " KH ", " AK ", " 60 "};	 
	public static final String divnum[] = { " 1", " 2", " 3", " 4", " 7", " 9",
			"10", "12", "16", "20", "27", "24", "13", "40", "45", "60" };
	public static final int y144[] = { 4, 5, 6, 7, 7, 7, 9, 9, 9, 11, 11, 13,
			14, 2, 2, 2, 3, 3, 3, 4, 4, 4, 5, 5, 6, 7, 5, 6, 7, 8, 9, 10, 11,
			12, 13, 7, 8, 10, 9, 12, 13, 14, 14, 16, 16, 16, 18, 18, 18, 19,
			20, 16, 19, 20, 21, 22, 23, 24, 25, 26, 27, 21, 22, 24, 23, 26, 27,
			27, 28, 28, 28, 29, 29, 29, 30, 30, 30, 25, 19, 21, 21, 23, 23, 23,
			25, 25, 25, 26, 27, 28, 18, 26, 27, 27, 28, 28, 28, 29, 29, 29, 30,
			30, 30, 25, 19, 20, 21, 22, 23, 24, 25, 26, 27, 21, 22, 24, 23, 12,
			13, 14, 14, 16, 16, 16, 18, 18, 18, 19, 20, 16, 7, 8, 10, 5, 6, 7,
			8, 9, 10, 11, 12, 13, 9, 2, 2, 2, 3, 3, 3, 4, 4, 4, 5, 5, 6, 7 };
	public static final int x144[] = { 28, 25, 30, 22, 28, 34, 19, 25, 37, 24,
			30, 27, 29, 5, 10, 15, 7, 13, 19, 8, 14, 19, 11, 16, 13, 15, 3, 3,
			3, 3, 3, 3, 3, 3, 3, 8, 8, 8, 13, 13, 13, 10, 16, 7, 13, 19, 8, 14,
			20, 16, 12, 25, 3, 3, 3, 3, 3, 3, 3, 3, 3, 8, 8, 8, 12, 13, 11, 16,
			8, 14, 19, 7, 13, 19, 5, 10, 15, 15, 27, 24, 30, 21, 28, 35, 22,
			28, 34, 30, 25, 28, 29, 41, 39, 44, 36, 42, 47, 35, 41, 47, 33, 38,
			43, 43, 53, 53, 53, 53, 53, 53, 53, 53, 53, 48, 48, 48, 46, 41, 41,
			38, 44, 37, 43, 49, 36, 42, 48, 44, 40, 33, 48, 48, 48, 53, 53, 53,
			53, 53, 53, 53, 53, 53, 46, 33, 38, 43, 35, 41, 47, 36, 42, 47, 39,
			44, 41, 43 };
	//For lalKItab
	public static final String kismat = "KISMAT_VALUES";
	public static final String maalik = "MAALIK_VALUES";
	public static final String pakkaGhar = "PAKKA_GHAR_VALUES";
	public static final int igana[][] = { { 1, 5, 7, 8, 13, 15, 17, 22, 27 },
			{ 3, 9, 10, 14, 16, 18, 19, 23, 24 },
			{ 2, 4, 6, 11, 12, 20, 21, 25, 26 } };
	public static final char rlp[] = "364214635775".toCharArray(); //char rlp[13] = "432712345665"
	//
	public static final int HEALTH_BHAVA = 1;
	public static final int FINANCE_BHAVA = 2;
	public static final int FAMILY_BHAVA = 2;
	public static final int ROMANCE_BHAVA = 5;
	public static final int MARRIED_LIFE_BHAVA = 7;
	public static final int PROFESSION_BHAVA = 10;
	public static final int MISC_BHAVA = 11;
	//
	public static final int aspectBhava[] = { HEALTH_BHAVA, FINANCE_BHAVA,
			FAMILY_BHAVA, ROMANCE_BHAVA, MARRIED_LIFE_BHAVA, PROFESSION_BHAVA,
			MISC_BHAVA };
	//
	public static final int HEALTH = 1;
	public static final int FINANCE = 2;
	public static final int FAMILY = 3;
	public static final int ROMANCE = 4;
	public static final int MARRIED_LIFE = 5;
	public static final int PROFESSION = 6;
	public static final int MISC = 7;
	//
	public static final int ENGLISH = 1;
	public static final int HINDI = 2;
	public static final int FOREIGN = 3;
	//
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
	public static final int NEUTRAL = 0;
	public static final int FRIEND = 1;
	public static final int INTIMATE = 2;
	public static final int ENEMY = -1;
	public static final int BITTER = -2;
	//
	public static final int WORST = 1;
	public static final int BAD = 2;
	public static final int MODERATE_BAD = 3;
	public static final int MODERATE_GOOD = 4;
	public static final int GOOD = 5;
	public static final int BEST = 6;
	//
	public static final int FIRST_HOUSE = GOOD;
	public static final int SECOND_HOUSE = BEST;
	public static final int THIRD_HOUSE = MODERATE_GOOD;
	public static final int FOURTH_HOUSE = GOOD;
	public static final int FIFTH_HOUSE = BEST;
	public static final int SIXTH_HOUSE = BAD;
	public static final int SEVENTH_HOUSE = MODERATE_GOOD;
	public static final int EIGHTH_HOUSE = WORST;
	public static final int NINTH_HOUSE = BEST;
	public static final int TENTH_HOUSE = GOOD;
	public static final int ELEVENTH_HOUSE = BEST;
	public static final int TWELTH_HOUSE = WORST;
	//
	//
	public static final int houseResult[] = { GOOD, BEST, MODERATE_GOOD, GOOD,
			BEST, BAD, MODERATE_GOOD, WORST, BEST, GOOD, BEST, WORST };
	//
	public static final int HOURLY = 0;
	public static final int WEEKLY = 2;
	public static final int FORTNIGHTLY = 3;
	public static final int DAILY = 1;
	public static final int ANNUALLY = 5;
	public static final int MONTHLY = 4;
	public static final int DAILY_ON_NAK = 7;
	public static final int SubBase = 1;
	public static final int NakBase = 2;
	//ADDED BY BIKRAM 
	public static final int STAR_RATING = 1;
	public static final int NUMBER_RATING = 2;
	public static final int hourlyPrediction = 1;
	public static final int dailyPrediction = 2;
	public static final int weaklyPrediction = 3;
	public static final int fortnightlyPrediction = 4;
	public static final int monthlyPrediction = 5;
	public static final int annuallyPrediction = 6;
	//ADDED BY BIKRAM
	//String ras[] = {"ARIES", "TAURAS", "GEMINI", "CANCER", "LEO", "VIRGO", "LIBRA", "SCORPIO", "SAGITTARIUS", "CAPRICORN", "AQUARIUS", "PISCES"};
	public static final int ARIES = 1;
	public static final int TAURAS = 2;
	public static final int GEMINI = 3;
	public static final int CANCER = 4;
	public static final int LEO = 5;
	public static final int VIRGO = 6;
	public static final int LIBRA = 7;
	public static final int SCORPIO = 8;
	public static final int SAGITTARIUS = 9;
	public static final int CAPRICORN = 10;
	public static final int AQUARIUS = 11;
	public static final int PISCES = 12;
	public static final int Lahiri = 0;
	public static final int Krishnamurti = 1;
	public static final int Raman = 2;
	public static final String YrString = "YR_STRING_KEY";
	public static final String YearsString = "YEARS_STRING_KEY";
	public static final String hrString = "HR_STRING_KEY";
	public static final String hourString = "HOUR_STRING_KEY";
	public static final String entTimeString = "ENT_TIME_STRING_KEY";
	public static final String dayString = "DAY_STRING_KEY";
	public static final String nightString = "NIGHT_STRING_KEY";
	public static final String exitTimeString = "EXIT_TIME_STRING_KEY";
	public static final String planetString = "PLANET_STRING_KEY";
	public static final String dayLrdString = "DAY_LORD_STRING_KEY";
	public static final String rahuKaalString = "RAHUKAAL_STRING_KEY";
	public static final String amString = "AM_STRING_KEY";
	public static final String pmString = "PM_STRING_KEY";
	public static final String toString = "TO_STRING_KEY";
	public static final String yoginiMahaDasaString = "YOGINI_MAHA_DASA_STRING_KEY";

	public abstract void setConstPath(String constPath);

	public abstract void setInputStream(InputStream is);

}