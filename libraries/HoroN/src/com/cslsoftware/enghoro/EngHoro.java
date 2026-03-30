package com.cslsoftware.enghoro;

public class EngHoro extends com.cslsoftware.purehoro.PureHoro implements IEngHoro {

	public EngHoro(){
		
	}
	@Override
	public String CalcMangalDoshString() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAscendentDms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAscendentNakshatraName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getAscendentPada() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getAscendentRasiDms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSignNameForLongitude(double longitude) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSignLordNameForLongitude(double longitude) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNakshatraLordNameForLontitude(double longitude) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAscendentSignName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAscendentSubLordName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getAshtakvargaBinduForSignAndPlanet(int signNo, int planetNo) {
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
	public int[] getAspectToBhav() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getAspectValueToBhav() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getAyanamsaDms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getBhavBeginForBhavDms(int bhavNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDayDurationHms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getFortunaDms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getGanaName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getGmtAtBirthHms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHinduWeekdayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getWeekdayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getHouselord(int planetNo, int shodashvargaType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getIndianSunSignName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getIshtkaalDms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getJulianDayValue() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getKaranName() {
		return "VISIT";
	}

	@Override
	public String getKPAscendentNakshatraLordName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getKPAscendentSignLordName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getKPAscendentSubLordName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getKPAyanamsaLongitudeDms() {
		// TODO Auto-generated method stub
		return null;
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
	public String getKPCuspLongitudeDms(int cuspNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getKPDayLordName() {
		return "MER";
	}

	@Override
	public String getKPFortunaLongitudeDms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getKPMoonNakshatraLordName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getKPMoonSignLordName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getKPMoonSubLordName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getKPPlanetLongitudeDms(int planetNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getKPPlnetsInPlanetNakshatra(int planetNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLagnaLordName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLagnaSign() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLMTCorrectionHms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getLMTOfBirthHms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public double getLongitudeOfPlanet(int planetNo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMarsInBhavForMoonChart() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double getMidBhavForBhava(int bhavNo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getMoonSubLordName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNadiName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNakshatraLordName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNakshatraLordNameForKPCusp(int cuspNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNakshatraLordNameForKPPlanet(int planetNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNakshatraName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getNakshatraNameForPlanet(int planetNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getObliquityDms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getPadaOfPlanet(int planetNo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getPakshaName() {
		return "SHUKLA";
	}

	@Override
	public String getPayaName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPlanetaryLongitudeDms(int planetNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPlanetaryRasiDms(int planetNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPlanetaryRasiName(int planetNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPlanetaryRasiNameForShodashvargaDivision(int planetNo,
			int shodashvargaDivision) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getPlanetAspectFromShodashvargaDivision(int planetNo,
			int shodashvargaDivision) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getPlanetInBhav() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int[] getPlanetInBhav(int shodashvargaType) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRashiNameForKPCusp(int cusp) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRashiNameForKPPlanet(int planetNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getRasiAndItsDegree(double totaldeg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String[] getRasiAndRasilordAndItsDegree(double totaldeg) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRasiLordName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getRasiName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getRelationshipForShodashvargaDivision(int planetNumber,
			int shodashvargaDivision) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getShodashvargaSignForVargaAndPlanet(int vargaNo, int planetNo) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getSiderealTimeHms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSubLordNameForKPCusp(int cuspNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSubLordNameForKPPlanet(int planetNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSubSubLordNameForKPCusp(int cuspNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSubSubLordNameForKPPlanet(int planetNo) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSubSubName(double d) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getSunRiseTimeHms() {
		return "05.58.35";
	}

	@Override
	public String getSunSetTimeHms() {
		return "18.39.54";
	}

	@Override
	public int getSunSignLord() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getSunSignName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTithiName() {
		return "PURNIMA";
	}

	@Override
	public String getTithiValueName() {
		// TODO Auto-generated method stub
		return null;
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
	public String getVarnaName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getVasyaName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getWarDaylightCorrectionHms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getYoganame() {
		return "VYAGHATA";
	}

	@Override
	public String getYogaValueName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getYoniName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void initializePlanetAspectToForShodashvargaDivision(int planetNo,
			int shodashvargaDivision) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isMangalDosh() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isMangalDoshForMoonChart() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void newMethod() {
		// TODO Auto-generated method stub

	}

}
