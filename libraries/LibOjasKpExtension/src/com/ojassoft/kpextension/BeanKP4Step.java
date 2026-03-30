package com.ojassoft.kpextension;

public class BeanKP4Step {
	boolean _isPlntStrong=false,_isPlntSubLordStrong=false;
	private BeanKp4StepPlanet _kp4Step1=null,_kp4Step2=null,_kp4Step3=null,_kp4Step4=null;

	public boolean isPlanetStrong() {
		return _isPlntStrong;
	}

	public void setPlanetStrong(boolean isPlntStrong) {
		this._isPlntStrong = isPlntStrong;
	}
	
	public boolean isPlanetSubLordStrong() {
		return _isPlntSubLordStrong;
	}
	public void setPlanetSubLordStrong(boolean isPlntSubLordStrong) {
		this._isPlntSubLordStrong = isPlntSubLordStrong;
	}

	public void setKp4Step1(BeanKp4StepPlanet _kp4Step1) {
		this._kp4Step1 = _kp4Step1;
	}

	public BeanKp4StepPlanet getKp4Step1() {
		return _kp4Step1;
	}

	public void setKp4Step2(BeanKp4StepPlanet _kp4Step2) {
		this._kp4Step2 = _kp4Step2;
	}

	public BeanKp4StepPlanet getKp4Step2() {
		return _kp4Step2;
	}

	public void setKp4Step3(BeanKp4StepPlanet _kp4Step3) {
		this._kp4Step3 = _kp4Step3;
	}

	public BeanKp4StepPlanet getKp4Step3() {
		return _kp4Step3;
	}

	public void setKp4Step4(BeanKp4StepPlanet _kp4Step4) {
		this._kp4Step4 = _kp4Step4;
	}

	public BeanKp4StepPlanet getKp4Step4() {
		return _kp4Step4;
	}
	
	
}
