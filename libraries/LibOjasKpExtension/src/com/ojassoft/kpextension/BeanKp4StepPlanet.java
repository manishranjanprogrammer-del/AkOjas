package com.ojassoft.kpextension;

import java.util.ArrayList;
import java.util.List;

public class BeanKp4StepPlanet {
	// plnt-star lord-sub lord-sub start lord
	private int _plnt=0;
	private int []_plntStepValues=null;
	
	// plnt-star lord-sub lord-sub start lord[Cusp Conjunction]
	private int _plntCuspConjunction=0;
	
	//planet conjunction
	private List<KeyValue4Step> _plntConjunction = new ArrayList<KeyValue4Step>();	

	//planet aspect
	private List<KeyValue4Step> _plntAspect = new ArrayList<KeyValue4Step>();	

	// plnt-star lord-sub lord-sub start lord[Cusp Conjunction]
	private int []_cuspAspect=null;	

	//boolean _isPlntStrong=false,_isPlntSubLordStrong=false;
	private KeyValue4Step _rahuKetPlacedInPlanetRashi = new KeyValue4Step();
	
	boolean _isPlntStrong=false;
	boolean _isPlanetInItsOwnStar=false;
	boolean _isPlanetStarLordOfOtherPlanet=false;
	
	public boolean isPlanetStarLordOfOtherPlanet() {
		return _isPlanetStarLordOfOtherPlanet;
	}

	public void setPlanetStarLordOfOtherPlanet(
			boolean _isPlanetStarLordOfOtherPlanet) {
		this._isPlanetStarLordOfOtherPlanet = _isPlanetStarLordOfOtherPlanet;
	}

	public boolean isPlanetInItsOwnStar() {
		return _isPlanetInItsOwnStar;
	}

	public void setPlanetInItsOwnStar(boolean _isPlanetInItsOwnStar) {
		this._isPlanetInItsOwnStar = _isPlanetInItsOwnStar;
	}

	public boolean isPlanetStrong() {
		return _isPlntStrong;
	}

	public void setPlanetStrong(boolean isPlntStrong) {
		this._isPlntStrong = isPlntStrong;
	}
	

	public int getPlanet() {
		return _plnt;
	}

	public void setPlanet(int plnt) {
		this._plnt = plnt;
	}
	
	public int[] getPlanetStepValues() {
		return _plntStepValues;
	}

	public void setPlanetStepValues(int[] plntStepValues) {
		this._plntStepValues = plntStepValues;
	}

	public List<KeyValue4Step> getPlanetConjunction() {
		return _plntConjunction;
	}

	public void setPlanetConjunction(List<KeyValue4Step> plntConjunction) {
		this._plntConjunction = plntConjunction;
	}
	public List<KeyValue4Step> getPlanetAspect() {
		return _plntAspect;
	}

	public void setPlanetAspect(List<KeyValue4Step> plntAspect) {
		this._plntAspect = plntAspect;
	}

	public int[] getCuspAspect() {
		return _cuspAspect;
	}

	public void setCuspAspect(int[] cuspAspect) {
		this._cuspAspect = cuspAspect;
	}
	public KeyValue4Step getRahuKetPlacedInPlanetRashi() {
		return _rahuKetPlacedInPlanetRashi;
	}

	public void setRahuKetPlacedInPlanetRashi(
			KeyValue4Step rahuKetPlacedInPlanetRashi) {
		this._rahuKetPlacedInPlanetRashi = rahuKetPlacedInPlanetRashi;
	}

	public int getPlanetCuspConjunction() {
		return _plntCuspConjunction;
	}

	public void setPlanetCuspConjunction(int _plntCuspConjunction) {
		this._plntCuspConjunction = _plntCuspConjunction;
	}

}
