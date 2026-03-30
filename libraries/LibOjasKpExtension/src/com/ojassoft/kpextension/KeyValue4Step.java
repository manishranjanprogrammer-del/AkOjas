package com.ojassoft.kpextension;

public class KeyValue4Step {
	int _plnt=-1;
	int[] _values=null;
	int _rahuKetuInPlnt=-1;
	int[] _rahuKetuInPlntValues=null;
	public void setKey(int plnt)
	{
		_plnt=plnt;
	}
	public int getKey()
	{
		return _plnt;
	}
	public void setValues(int[] values)
	{
		_values=values;
	}
	public int[] getValues()
	{
		return _values;
	}
	
	public int getPlanetInWhichRahuKetuPlaced()
	{
		return _rahuKetuInPlnt;
	}
	public void  setPlanetInWhichRahuKetuPlaced(int rahuKetuInPlnt)
	{
	  _rahuKetuInPlnt=rahuKetuInPlnt;
	}
	public void setPlanetValuesInWhichRahuKetuPlaced(int[] rahuKetuInPlntValues)
	{
		_rahuKetuInPlntValues=rahuKetuInPlntValues;
	}
	public int[] gePlanetValuesInWhichRahuKetuPlaced()
	{
		return _rahuKetuInPlntValues;
	}
	
}
