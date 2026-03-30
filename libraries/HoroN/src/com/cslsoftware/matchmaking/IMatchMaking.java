package com.cslsoftware.matchmaking;

import com.cslsoftware.util.ProcessingErrorException;

public interface IMatchMaking {

	public abstract boolean calculateCompatibilityForMangalDosh(
			int maleMangalDoshNumber, int femaleMangalDoshNumber);

	/*
	 This Function calculates the Gana Of Boy and girl 
	 */
	public abstract int calculateGana(double moonDeg);

	/*
	 The Function Returns The Lord Of Moon Rashi of Boy And Girl 
	 */

	public abstract int calculateGraha(int ras);

	/*
	 This Function calculates the Nadi Of Boy and girl 
	 */

	public abstract int calculateNadi(double moonDeg);

	/* 
	This Function Calculates the TARA of the boy and the girl
	 */
	// should be changed
	public abstract int calculateNak(double moonDeg);

	/* 
	This Function Calculates the TARA of the boy and the girl
	 */
	// should be changed

	public abstract int[] CalculateTara(double degOfBoy, double degOfGirl);

	/*
	The getVarna Calculates The Varna Of the boy and the girl 
	 */

	public abstract int calculateVarna(int rasi);

	/*
	 The getVashya Function Below Calulates Vashya of the Boy and Girl 
	 */

	public abstract int calculateVashya(double moonDeg)
			throws ProcessingErrorException;

	/*
	 function to calculate YONI  of boy and girl
	 */

	public abstract int calculateYoni(double moonDeg);

	/*
	 This Function Matches Rashis of Boy and Girl and Returns BHAKUT 
	 */

	public abstract String getAreaOfLife(int areaOfLife);

	/*
	 This Function calculates the Gana Of Boy and girl 
	 */

	public abstract String getGana(double moonDeg);

	/*
	 The Function Returns The Lord Of Moon Rashi of Boy And Girl 
	 */

	public abstract String getGraha(int ras);

	/*
	 This Function Matches Rashis of Boy and Girl and Returns BHAKUT 
	 */

	public abstract int getMaximumBhakoot();

	/*
	 This Function Matches Rashis of Boy and Girl and Returns BHAKUT 
	 */

	public abstract int getMaximumGana();

	/*
	 This Function Matches Rashis of Boy and Girl and Returns BHAKUT 
	 */

	public abstract int getMaximumGrahaMaitri();

	/*
	 This Function Matches Rashis of Boy and Girl and Returns BHAKUT 
	 */

	public abstract int getMaximumNadi();

	/*
	 This Function Matches Rashis of Boy and Girl and Returns BHAKUT 
	 */

	public abstract int getMaximumTara();

	/*
	 This Function Matches Rashis of Boy and Girl and Returns BHAKUT 
	 */

	public abstract int getMaximumVarna();

	/*
	 This Function Matches Rashis of Boy and Girl and Returns BHAKUT 
	 */

	public abstract int getMaximumVashya();

	/*
	 This Function Matches Rashis of Boy and Girl and Returns BHAKUT 
	 */

	public abstract int getMaximumYoni();

	/*
	 This Function calculates the Nadi Of Boy and girl 
	 */

	public abstract String getNadi(double moonDeg);

	/* 
	This Function Calculates the TARA of the boy and the girl
	 */
	// should be changed

	public abstract String[] getTara(double degOfBoy, double degOfGirl);

	/*
	 This Function calculates the Gana Of Boy and girl 
	 */

	public abstract double getTotal();

	/*
	The getVarna Calculates The Varna Of the boy and the girl 
	 */

	public abstract String getVarna(int rasi);

	/*
	 The getVashya Function Below Calulates Vashya of the Boy and Girl 
	 */

	public abstract String getVashya(double moonDeg);

	/*
	 function to calculate YONI  of boy and girl
	 */

	public abstract String getYoni(double moonDeg);

	/**
	 * Insert the method's description here.
	 * Creation date: (5/17/02 6:38:34 PM)
	 */
	public abstract void initializesArrayValues() throws Exception;

	public abstract int matchBhakutGuna(int maleRashi, int femaleRashi);

	/*
	 This Function Matches Gana of boy and Girl 
	 */

	public abstract int matchGanaGuna(double degOfBoy, double degOfGirl);

	/*
	  The Function Below Matches the Moon Lords Of Boy And Girl 
	 */

	public abstract double matchGrahaMitraGuna(int maleRashi, int femaleRashi);

	/*
	 This Function Matches Nadi of boy and Girl 
	 */

	public abstract int matchNadiGuna(double degOfBoy, double degOfGirl);

	public abstract double matchTaraGuna(double degOfBoy, double degOfGirl);

	/* 
	The Function below matches the boy's Varna with the Girl's Varna
	and then returns 1 on success or 0 on failure 
	 */

	public abstract int matchVarnaGuna(int maleRashi, int femaleRashi);

	/*
	 This Function Matches the Vashya of boy and girl and returns
	appropriate value 
	 */

	public abstract double matchVashyaGuna(double degOfBoy, double degOfGirl)
			throws ProcessingErrorException;

	/*
	 This Function matches the Yoni of Boy and Girl 
	 */

	public abstract int matchYoniGuna(double degOfBoy, double degOfGirl);

	/*
	 The Function Returns The Lord Of Moon Rashi of Boy And Girl 
	 */

	public abstract void setLanguageCode(String lang);

}