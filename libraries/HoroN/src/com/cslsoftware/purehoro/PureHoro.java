package com.cslsoftware.purehoro;

public class PureHoro extends com.cslsoftware.horo.Horo implements IPureHoro {

	@Override
	public StringBuffer balance(double moon) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int CalcMangalDosh() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double degreesInSign(double degree) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getAscendentLongitude() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getAscendentSign() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getAscendentSubLord() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getAshtakvargaBinduForSignAndPlanet(int planetNo, int signNo) {
		// TODO Auto-generated method stub
		int[][] ashtakvargaBinduForSignAndPlanet = new int[8][13];
		String	_sValues = "3,3,5,4,4,3,2,7,5,4,4,4";
		String[] temp3 = new String[13];
		temp3 = _sValues.split(",");

		for (int i = 0; i < temp3.length; i++) {
			ashtakvargaBinduForSignAndPlanet[0][i] = Integer.valueOf(temp3[i]);
		}
		_sValues = "3,5,6,5,3,3,5,4,5,5,3,2";
		temp3 = _sValues.split(",");

		for (int i = 0; i < temp3.length; i++) {
			ashtakvargaBinduForSignAndPlanet[1][i] = Integer.valueOf(temp3[i]);
		}
		_sValues = "3,4,4,5,3,3,1,3,4,4,3,2";
		temp3 = _sValues.split(",");

		for (int i = 0; i < temp3.length; i++) {
			ashtakvargaBinduForSignAndPlanet[2][i] = Integer.valueOf(temp3[i]);
		}
		_sValues = "5,4,6,4,3,4,4,4,6,3,7,4";
		temp3 = _sValues.split(",");

		for (int i = 0; i < temp3.length; i++) {
			ashtakvargaBinduForSignAndPlanet[3][i] = Integer.valueOf(temp3[i]);
		}

		_sValues = "4,4,5,6,2,4,7,3,6,7,2,6";
		temp3 = _sValues.split(",");

		for (int i = 0; i < temp3.length; i++) {
			ashtakvargaBinduForSignAndPlanet[4][i] = Integer.valueOf(temp3[i]);
		}

		_sValues = "5,7,2,4,3,3,5,7,4,5,4,3";
		temp3 = _sValues.split(",");

		for (int i = 0; i < temp3.length; i++) {
			ashtakvargaBinduForSignAndPlanet[5][i] = Integer.valueOf(temp3[i]);
		}

		_sValues = "1,2,4,4,2,2,3,4,7,5,4,1";
		temp3 = _sValues.split(",");

		for (int i = 0; i < temp3.length; i++) {
			ashtakvargaBinduForSignAndPlanet[6][i] = Integer.valueOf(temp3[i]);
		}
		return ashtakvargaBinduForSignAndPlanet[signNo][planetNo];
	}

	@Override
	public double getAyanamsa() {
		return 23.5670478581315;
	}

	@Override
	public double getBhavBeginForBhav(int bhavNo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getDayDuration() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getFortuna() {
		return 342.917027549523;
	}

	@Override
	public int getGana() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getGmtAtBirth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getHinduWeekday() {
		return 2;
	}

	@Override
	public int getIndianSunSign() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getIshtkaal() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getJulianDay() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getjvalue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getKPAscendentNakshatraLord() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getKPAscendentSubLord() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getKPAyanamsaLongitude() {
		return 23.4773128832021;
	}

	@Override
	public int[] getKPBhavOccupants(int bhavNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getKPBhavOwner(int bhavaNo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getKPCuspLongitude(int cuspNo) {
		// TODO Auto-generated method stub
		double[] kPCuspLongitude = new double[12];
		kPCuspLongitude[0] = 174.65398489518;
		kPCuspLongitude[1] = 203.396878120619;
		kPCuspLongitude[2] = 233.851559113634;
		kPCuspLongitude[3] = 265.353094945615;
		kPCuspLongitude[4] = 297.092044451122;
		kPCuspLongitude[5] = 327.414023927425;
		kPCuspLongitude[6] = 354.65398489518;
		kPCuspLongitude[7] = 23.3968781206191;
		kPCuspLongitude[8] = 53.8515591136335;
		kPCuspLongitude[9] = 85.3530949456147;
		kPCuspLongitude[10] = 117.092044451122;
		kPCuspLongitude[11] = 147.414023927425;
		return kPCuspLongitude[cuspNo-1];
	}

	@Override
	public double getKPFortunaLongitude() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getKPMoonSubLord() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getKPNakshatraLord() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getKPPlanetLongitude(int planetNo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int[] getKPPlanetSignification(int planetNo) {
		// TODO Auto-generated method stub
		String _sValues = "1 6 7 10 12";
		int[][] kPPlanetSignification = new int[10][13];
		String[] temp = _sValues.split(" ");

		for (int i = 0; i < temp.length; i++) {
			kPPlanetSignification[0][i] = Integer.valueOf(temp[i]);
		}
		_sValues = "11 12";
		temp = _sValues.split(" ");
		for (int i = 0; i < temp.length; i++) {
			kPPlanetSignification[1][i] = Integer.valueOf(temp[i]);
		}
		_sValues = "3 5 6 8 11";
		temp = _sValues.split(" ");
		for (int i = 0; i < temp.length; i++) {
			kPPlanetSignification[2][i] = Integer.valueOf(temp[i]);
		}
		_sValues = "1 4 6 7 10";
		temp = _sValues.split(" ");
		for (int i = 0; i < temp.length; i++) {
			kPPlanetSignification[3][i] = Integer.valueOf(temp[i]);
		}
		_sValues = "4 5 6 7 10 11";
		temp = _sValues.split(" ");
		for (int i = 0; i < temp.length; i++) {
			kPPlanetSignification[4][i] = Integer.valueOf(temp[i]);
		}
		_sValues = "2 4 5 7 9 10";
		temp = _sValues.split(" ");
		for (int i = 0; i < temp.length; i++) {
			kPPlanetSignification[5][i] = Integer.valueOf(temp[i]);
		}

		_sValues = "2 5 6 9 11";
		temp = _sValues.split(" ");
		for (int i = 0; i < temp.length; i++) {
			kPPlanetSignification[6][i] = Integer.valueOf(temp[i]);
		}
		_sValues = "2 5 9 11";
		temp = _sValues.split(" ");
		for (int i = 0; i < temp.length; i++) {
			kPPlanetSignification[7][i] = Integer.valueOf(temp[i]);
		}
		_sValues = "4 5 7 10";
		temp = _sValues.split(" ");
		for (int i = 0; i < temp.length; i++) {
			kPPlanetSignification[8][i] = Integer.valueOf(temp[i]);
		}
		return kPPlanetSignification[planetNo];
	}

	@Override
	public int[] getKPPlanetSignificator(int planetNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getKPPlnetsInPlanetNakshatra(int planetNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getLagna() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getLMTCorrection() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getLMTOfBirth() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getLongitudeOfPlanet(int planetNo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getMidBhavForBhava(int bhavNo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMoonSign() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMoonSubLord() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNadi() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNakshatra() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNakshatra(double d) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNakshatraForKPCusp(int cuspNo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNakshatraForKPPlanet(int planetNo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNakshatraForPlanet(int j) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getNakshatraLord() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getObliquity() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getPadaOfPlanet(int planetNo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getPaksha() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getPanchadhaFriendshipOfPlanet(int planetNo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getPaya() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getPermanentFriendshipOfPlanet(int planetNo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getPlanetaryLongitude(int planetNo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getPlanetaryPada(int planetNo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getPlanetaryRasi(int planetNo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getPlanetName(int planetNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getPositionForShodasvarg(int position) {
		// TODO Auto-generated method stub
		int[][] positionForShodasvarg = new int[17][14];
		String _sValues = "6,12,6,12,12,4,11,5,5,11,7,8,6";
		String[] temp2 = new String[13];
		temp2 = _sValues.split(",");

		for (int i = 0; i < temp2.length; i++) {
			positionForShodasvarg[0][i] = Integer.valueOf(temp2[i]);
		}

		_sValues = "5,5,5,4,4,4,4,5,4,4,4,5,5";
		temp2 = _sValues.split(",");

		for (int i = 0; i < temp2.length; i++) {
			positionForShodasvarg[1][i] = Integer.valueOf(temp2[i]);
		}

		_sValues = "2,8,10,12,12,4,7,9,1,7,3,4,2";
		temp2 = _sValues.split(",");

		for (int i = 0; i < temp2.length; i++) {
			positionForShodasvarg[2][i] = Integer.valueOf(temp2[i]);
		}
		_sValues = "3,9,12,3,12,4,5,8,11,5,4,5,3";
		temp2 = _sValues.split(",");

		for (int i = 0; i < temp2.length; i++) {
			positionForShodasvarg[3][i] = Integer.valueOf(temp2[i]);
		}
		_sValues = "5,0,3,8,6,11,4,8,10,4,1,8,5";
		temp2 = _sValues.split(",");

		for (int i = 0; i < temp2.length; i++) {
			positionForShodasvarg[4][i] = Integer.valueOf(temp2[i]);
		}
		_sValues = "5,12,2,6,4,5,1,5,7,1,2,12,5";
		temp2 = _sValues.split(",");

		for (int i = 0; i < temp2.length; i++) {
			positionForShodasvarg[5][i] = Integer.valueOf(temp2[i]);
		}

		_sValues = "10,5,7,11,8,1,6,9,12,6,3,12,10";
		temp2 = _sValues.split(",");

		for (int i = 0; i < temp2.length; i++) {
			positionForShodasvarg[6][i] = Integer.valueOf(temp2[i]);
		}

		_sValues = "3,10,12,3,12,6,7,10,1,7,5,6,3";
		temp2 = _sValues.split(",");

		for (int i = 0; i < temp2.length; i++) {
			positionForShodasvarg[7][i] = Integer.valueOf(temp2[i]);
		}

		_sValues = "10,11,5,2,10,4,4,12,4,4,3,7,9";
		temp2 = _sValues.split(",");

		for (int i = 0; i < temp2.length; i++) {
			positionForShodasvarg[8][i] = Integer.valueOf(temp2[i]);
		}

		_sValues = "9,11,3,11,6,4,11,6,11,11,6,2,9";
		temp2 = _sValues.split(",");

		for (int i = 0; i < temp2.length; i++) {
			positionForShodasvarg[9][i] = Integer.valueOf(temp2[i]);
		}

		_sValues = "2,10,6,6,12,3,3,1,9,3,6,10,1";
		temp2 = _sValues.split(",");

		for (int i = 0; i < temp2.length; i++) {
			positionForShodasvarg[10][i] = Integer.valueOf(temp2[i]);
		}

		_sValues = "11,1,4,11,6,8,10,4,10,10,2,1,11";
		temp2 = _sValues.split(",");

		for (int i = 0; i < temp2.length; i++) {
			positionForShodasvarg[11][i] = Integer.valueOf(temp2[i]);
		}

		_sValues = "10,8,12,6,2,6,3,9,3,3,7,8,10";
		temp2 = _sValues.split(",");

		for (int i = 0; i < temp2.length; i++) {
			positionForShodasvarg[12][i] = Integer.valueOf(temp2[i]);
		}

		_sValues = "3,7,3,8,10,2,6,7,6,6,12,6,3";
		temp2 = _sValues.split(",");

		for (int i = 0; i < temp2.length; i++) {
			positionForShodasvarg[13][i] = Integer.valueOf(temp2[i]);
		}

		_sValues = "9,2,8,11,1,9,2,2,2,2,4,9,9";
		temp2 = _sValues.split(",");

		for (int i = 0; i < temp2.length; i++) {
			positionForShodasvarg[14][i] = Integer.valueOf(temp2[i]);
		}

		_sValues = "7,6,1,7,5,3,7,9,1,7,12,1,6";
		temp2 = _sValues.split(",");

		for (int i = 0; i < temp2.length; i++) {
			positionForShodasvarg[15][i] = Integer.valueOf(temp2[i]);
		}
		return positionForShodasvarg[position];
	}

	@Override
	public int getPrastharashtakvargaTables(int tableNo1, int planetNo1,
			int bindu) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getRashiForKPCusp(int cusp) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getRashiForKPPlanet(int planetNo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getRasi() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getRasiLord() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getShodashvargaSignForVargaAndPlanet(int vargaNo, int planetNo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getSiderealTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSookshmaDasaForPlanet(int planetNo1, int planetNo2,
			int planetNo3, int planetNo4) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSthiraAnatykarkaPlanet() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSthiraAtmakarakaPlanet() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSthiraBhatruPlanet() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSthiraDarakarakaPlanet() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSthiraGrathikarakaPlanet() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSthiraMatruKarkaPlanet() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSthiraPutrakarakaPlanet() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSubLordForKPCusp(int cuspNo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSubLordForKPPlanet(int planetNo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSubSubLordForKPCusp(int cuspNo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSubSubLordForKPPlanet(int planetNo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getSunRiseTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getSunSetTime() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSunSign() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getSunSignLord() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTemporaryFriendshipOfPlanet(int planetNo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getTithi() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getTithiValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int[] getTotalAshtakVargaValue() {
		int[] totalAshtakVargaValue = {24,29,32,32,20,22,27,32,37,33,27,22};
		return totalAshtakVargaValue;
	}

	@Override
	public double getTropicalCuspalLongitude() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getTropicalPlanetLongitude(int planetNo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getVarna() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getVasya() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getWarDaylightCorrection() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getYoga() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getYogaValue() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getYoni() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void initialize() throws Exception {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isAccelerate(int planetNumber) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isCombust(int planetNumber) {
		return false;
	}

	@Override
	public boolean isDeblited(int planetNumber, int sign) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isExalted(int planetNumber, int sign) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isInFriendSign(int planetNumber, int sign) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isInLastQuarter(int planetNumber) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isInNeutralSign(int planetNumber, int sign) {
		// TODO Auto-generated method stub
		return false;
	}

	boolean[] planetDirect = new boolean[13];
	
	@Override
	public boolean isPlanetDirect(int planetNo) {
		String _sDirect = "1,1,1,1,1,1,0,0,0,0,0,0";
		
		String[] temp4 = new String[13];
		temp4 = _sDirect.split(",");
		String[] s = new String[13];
		for (int i = 0; i <= 11; i++) {
			temp4[i] = temp4[i].replace(" ", "");
			s[i + 1] = temp4[i];
		}
		for (int i = 1; i <= 12; i++) {
			if (Integer.valueOf(s[i]) == 1)
				planetDirect[i] = true;
			else
				planetDirect[i] = false;
		}
		return planetDirect[planetNo];
	}

	@Override
	public boolean isPlanetRetrograde(int planetNo) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isRetrograde(int planetNumber) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int returnMangalDoshPoints() {
		// TODO Auto-generated method stub
		return 0;
	}

}
