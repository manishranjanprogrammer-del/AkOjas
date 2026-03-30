package com.libojassoft.android.beans;


/**
 * This class contain the city information
 * @author Bijendra on 30-may-13
 *
 */
public class LibOutPlace {
	private String _id,_name,_state,_country,_latitude,_longitude,_timezone,_timeZoneString;
	
	public void setTimeZoneString(String timeZoneString)
	{
		this._timeZoneString=timeZoneString;
	}
	public String getTimeZoneString()
	{
		return _timeZoneString;
	}
	public void setCountry(String country)
	{
		this._country=country;
	}
	public String getCountry()
	{
		return _country;
	}
	public void setId(String id)
	{
		this._id=id;
	}
	public void setName(String name)
	{
		this._name=name;
	}
	public void setState(String state)
	{
		this._state=state;
	}
	public void setLatitude(String latitude)
	{
		this._latitude=latitude;
	}
	public void setLongitude(String longitude)
	{
		this._longitude=longitude;
	}
	public void setTimezone(String timezone)
	{
		this._timezone=timezone;
	}
	
	
	public String getId()
	{
		return this._id;
	}
	public String getName()
	{
		return this._name;
	}
	public String getState()
	{
		return this._state;
	}
	public String getLatitude()
	{
		return this._latitude;
	}
	public String getLongitude()
	{
		return this._longitude;
	}
	public String getTimezone()
	{
		return this._timezone;
	}

}
