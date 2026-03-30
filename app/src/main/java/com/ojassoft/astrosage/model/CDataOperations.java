package com.ojassoft.astrosage.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ojassoft.astrosage.beans.BeanDateTime;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.beans.BeanPlace;
import com.ojassoft.astrosage.jinterface.IDataOperations;
import com.ojassoft.astrosage.utils.CGlobalVariables;

public class CDataOperations implements IDataOperations {

    /**
     * This function return the array of city id and name
     *
     * @param sqLiteDatabase
     * @param cityName
     * @return String[][]
     * @throws Exception
     * @author Bijendra 31-may-13
     */
    @Override
    public String[][] searchCity(SQLiteDatabase sqLiteDatabase, String cityName)
            throws Exception {
        String[][] arrCity = null;
        int index = 0;
        Cursor cursor = null;
        try {
            cursor = sqLiteDatabase.rawQuery(
                    "Select  id, cityName from tblCity WHERE cityName LIKE ?",
                    new String[]{cityName + "%"});

            if (cursor.getCount() < 1)
                arrCity = null;
            else {

                arrCity = new String[cursor.getCount()][2];
                if (cursor.moveToFirst()) {
                    do {

                        arrCity[index][0] = cursor.getString(0);//CITY ID
                        arrCity[index][1] = cursor.getString(1);//CITY NAME
                        index++;

                    } while (cursor.moveToNext());
                }
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            }
        } catch (Exception e) {

            throw e;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }

        return arrCity;
    }


    /**
     * This function is used to fatch city detail based on city id
     *
     * @param sqLiteDatabase
     * @param cityId
     * @return BeanPlace
     * @throws Exception
     */
    @Override
    public BeanPlace getCityById(SQLiteDatabase sqLiteDatabase, int cityId)
            throws Exception {
        BeanPlace objTemp = null;
        Cursor cursor = null;

        try {
//			 mydbConnection=new CDatabaseHelper(_context).getReadableDatabase();
            cursor = sqLiteDatabase.rawQuery("SELECT id, cityName, longDeg,longMin, latDeg, latMin,longDir,latDir,countryId,timeZoneId FROM tblCity  WHERE id = ?",
                    new String[]{"" + cityId});
            if (cursor.getCount() == 1) {
                objTemp = new BeanPlace();
                cursor.moveToFirst();


                objTemp.setCityId(cursor.getInt(cursor.getColumnIndex("id")));
                objTemp.setCountryId(cursor.getInt(cursor.getColumnIndex("countryId")));
                objTemp.setTimeZoneId(cursor.getInt(cursor.getColumnIndex("timeZoneId")));


                //Set value for City
                objTemp.setCityName(cursor.getString(cursor.getColumnIndex("cityName")));
                objTemp.setLongDeg(cursor.getString(cursor.getColumnIndex("longDeg")));
                objTemp.setLongMin(cursor.getString(cursor.getColumnIndex("longMin")));
                objTemp.setLatDeg(cursor.getString(cursor.getColumnIndex("latDeg")));
                objTemp.setLatMin(cursor.getString(cursor.getColumnIndex("latMin")));

                if (cursor.getString(cursor.getColumnIndex("longDir")).equalsIgnoreCase("1"))
                    objTemp.setLongDir("E");
                else
                    objTemp.setLongDir("W");


                if (cursor.getString(cursor.getColumnIndex("latDir")).equalsIgnoreCase("1"))
                    objTemp.setLatDir("N");
                else
                    objTemp.setLatDir("S");

                //FOR TIMEZONE
                BeanPlace objTemp1 = null;
                objTemp1 = getTimZoneId(sqLiteDatabase, objTemp.getTimeZoneId());
                if (objTemp1 != null) {
                    objTemp.setTimeZoneName(objTemp1.getTimeZoneName());
                    objTemp.setTimeZoneValue(objTemp1.getTimeZoneValue());

                }

                //08-oct-2015 //Getting Country Name From Local Database
                objTemp.setCountryName(getCountryNameById(sqLiteDatabase, objTemp.getCountryId()));

            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        } catch (Exception e) {
            //Log.d("time_Zone", e.getMessage());
            throw e;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }

        }

        return objTemp;
    }

    @Override
    public String getCountryNameById(SQLiteDatabase sqLiteDatabase, int countryId) throws Exception {
        // TODO Auto-generated method stub
        String countryName = "";
        Cursor cursor = null;

        try {
//			 mydbConnection=new CDatabaseHelper(_context).getReadableDatabase();
            cursor = sqLiteDatabase.rawQuery("SELECT countryName FROM tblCountry WHERE id = ?",
                    new String[]{"" + countryId});
            if (cursor.getCount() == 1) {
                cursor.moveToFirst();
                countryName = cursor.getString(cursor.getColumnIndex("countryName"));
            }
        } catch (Exception e) {
            //Log.d("time_Zone", e.getMessage());
            throw e;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }

        }

        return countryName;
    }

    /**
     * This function return Place object filled with time zone values(name/value/id)
     * according to passed time zone id that contain in time zone table
     *
     * @param timeZoneId
     * @return BeanPlace
     * @throws Exception
     */

    public BeanPlace getTimZoneId(SQLiteDatabase sqLiteDatabase, int timeZoneId) throws Exception {
        // TODO Auto-generated method stub
        BeanPlace objTemp = null;
        Cursor cursor = null;
        try {


            cursor = sqLiteDatabase.rawQuery("SELECT id, timezoneName,zonevalue FROM tblTimeZone  WHERE id = ?",
                    new String[]{"" + timeZoneId});
            if (cursor.getCount() == 1) {
                objTemp = new BeanPlace();
                cursor.moveToFirst();
                objTemp.setTimeZoneId(cursor.getInt(cursor.getColumnIndex("id")));
                objTemp.setTimeZoneName(cursor.getString(cursor.getColumnIndex("timezoneName")));
                objTemp.setTimeZoneValue(cursor.getFloat(cursor.getColumnIndex("zonevalue")));

            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        } catch (Exception e) {
            throw e;
        }
        return objTemp;
    }

    /**
     * This function return array of time zones from database
     * according to passed time zone name
     *
     * @param sqLiteDatabase
     * @param time           zone name
     * @return String array contain time zone id and name
     * @throws Exception
     */
    @Override
    public String[][] searchTimeZone(SQLiteDatabase sqLiteDatabase,
                                     String searchText) throws Exception {
        String[][] arrTimeZone = null;
        int index = 0;
        Cursor cursor = null;
        try {

            cursor = sqLiteDatabase.query("tblTimeZone", null, null, null, null, null, null, null);

            if (cursor.getCount() < 1)
                arrTimeZone = null;
            else {
                arrTimeZone = new String[cursor.getCount()][2];
                if (cursor.moveToFirst()) {
                    do {
                        arrTimeZone[index][0] = cursor.getString(1).trim();
                        arrTimeZone[index][1] = cursor.getString(2).trim();
                        index++;


                    } while (cursor.moveToNext());
                }
                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }

        }

        return arrTimeZone;
    }

    /**
     * This function return Place object filled with time zone values(name/value/id)
     * according to passed time zone value
     *
     * @param timezonevalue
     * @return BeanPlace
     * @throws Exception
     */
    public BeanPlace getNewTimZoneByName(SQLiteDatabase sqLiteDatabase, String timezonevalue) throws Exception {
        // TODO Auto-generated method stub
        BeanPlace objTemp = null;

        Cursor cursor = null;
        try {
//			 mydbConnection=new CDatabaseHelper(_context).getReadableDatabase();
            cursor = sqLiteDatabase.rawQuery("SELECT id, timezoneName,zonevalue FROM tblTimeZone  WHERE zonevalue LIKE ?",
                    new String[]{"" + timezonevalue + "%"});

            if (cursor.getCount() == 1) {
                objTemp = new BeanPlace();
                cursor.moveToFirst();
                objTemp.setTimeZoneId(cursor.getInt(cursor.getColumnIndex("id")));
                objTemp.setTimeZoneName(cursor.getString(cursor.getColumnIndex("timezoneName")));
                objTemp.setTimeZoneValue(cursor.getFloat(cursor.getColumnIndex("zonevalue")));

            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        } catch (Exception e) {
            throw e;

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }

        }

        return objTemp;

    }

    @Override
    public Map<String, String> searchLocalKundliList(SQLiteDatabase sqLiteDatabase,
                                                     String kundliName, int genderType, int noOfRecords) throws Exception {
        Map<String, String> arrHoro = null;
        Cursor cursor = null;
        try {
//desc|asc
            if (genderType == CGlobalVariables.BOTH_GENDER) {
                if (noOfRecords == -1) {
                    if (kundliName.length() < 1) {
                        cursor = sqLiteDatabase
                                .rawQuery(
                                        "Select  id, M_PName,M_TimeStamp from tblHoroPersonalInfo WHERE M_UserId LIKE ? order by cast(M_TimeStamp as REAL) desc",
                                        new String[]{CGlobalVariables.APP_USER_ID});
                    } else
                        cursor = sqLiteDatabase
                                .rawQuery(
                                        "Select  id, M_PName,M_TimeStamp from tblHoroPersonalInfo WHERE M_PName LIKE ? order by cast(M_TimeStamp as REAL) desc",
                                        new String[]{kundliName + "%"});
                } else {
                    if (kundliName.length() < 1) {
                        cursor = sqLiteDatabase
                                .rawQuery(
                                        "Select id, M_PName,M_TimeStamp from tblHoroPersonalInfo WHERE M_UserId LIKE ? order by cast(M_TimeStamp as REAL) desc LIMIT " + noOfRecords,
                                        new String[]{CGlobalVariables.APP_USER_ID});
                    } else
                        cursor = sqLiteDatabase
                                .rawQuery(
                                        "Select id, M_PName,M_TimeStamp from tblHoroPersonalInfo WHERE M_PName LIKE ? order by cast(M_TimeStamp as REAL) desc LIMIT " + noOfRecords,
                                        new String[]{kundliName + "%"});
                }
                if (cursor.getCount() < 1)
                    arrHoro = null;
                else {
                    arrHoro = new HashMap<String, String>();
                    if (cursor.moveToFirst()) {
                        do {
                            arrHoro.put(cursor.getString(0), cursor.getString(1));
                        } while (cursor.moveToNext());
                    }

                }

                if (cursor != null && !cursor.isClosed()) {
                    cursor.close();
                }

            } else if (genderType == CGlobalVariables.GENDER_BOY || genderType == CGlobalVariables.GENDER_GIRL) {
                String sex;
                if (genderType == 0) {
                    sex = "M";
                } else
                    sex = "F";

                if (kundliName.length() < 1) {
                    cursor = sqLiteDatabase.rawQuery("Select  id, M_PName from tblHoroPersonalInfo WHERE M_Sex = ?", new String[]{sex});
                } else
                    cursor = sqLiteDatabase.rawQuery("Select  id, M_PName from tblHoroPersonalInfo WHERE M_PName LIKE ? AND M_Sex = ?",
                            new String[]{kundliName + "%", sex});

                if (cursor.getCount() < 1)
                    arrHoro = null;

                else {
                    arrHoro = new HashMap<String, String>();
                    if (cursor.moveToFirst()) {
                        do {
                            arrHoro.put(cursor.getString(0), cursor.getString(1));
                        } while (cursor.moveToNext());
                    }

                }


            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }

        }


        return arrHoro;
    }

    /**
     * This function is used to delete local kundli
     *
     * @param sqLiteDatabase
     * @param kundliId
     * @return boolean
     * @throws Exception
     */
    @Override
    public boolean deleteLocalKundli(SQLiteDatabase sqLiteDatabase, long kundliId)
            throws Exception {
        boolean isSuccess = true;
        try {
            sqLiteDatabase.execSQL("DELETE FROM tblHoroPersonalInfo WHERE id=" + kundliId);
        } catch (Exception e) {
            throw e;
        }

        return isSuccess;
    }

    @Override
    public BeanHoroPersonalInfo getLocalKundliDetail(
            SQLiteDatabase sqLiteDatabase, long kundliId) throws Exception {
        BeanHoroPersonalInfo objTemp = null;
        Cursor cursor = null;

        try {

            cursor = sqLiteDatabase.rawQuery("SELECT * FROM tblHoroPersonalInfo  WHERE id = ?",
                    new String[]{"" + kundliId});
            if (cursor.getCount() > 0) {
                objTemp = new BeanHoroPersonalInfo();
                cursor.moveToFirst();

                //objTemp.setHoroID(_id);//DISABLED  BY BIJENDRA (17-NOV-2012)
                objTemp.setLocalChartId(kundliId);//ADDED BY BIJENDRA (17-NOV-2012)
                //ADDED NEW DATA IN THE OBJECT
                //objTemp.setChartId(CGlobalVariables.APP_USER_ID);//COMMENTED BY BIJENDRA(16-NOV-2012)
                objTemp.setName(cursor.getString(cursor.getColumnIndex("M_PName")));

                objTemp.setGender(cursor.getString(cursor.getColumnIndex("M_Sex")));

                /****************************************************************/
                BeanDateTime dTemp = new BeanDateTime();
                dTemp.setDay(cursor.getInt(cursor.getColumnIndex("M_Day")));
                dTemp.setMonth(cursor.getInt(cursor.getColumnIndex("M_Month")));
                dTemp.setYear(cursor.getInt(cursor.getColumnIndex("M_Year")));

                dTemp.setHour(cursor.getInt(cursor.getColumnIndex("M_Hr")));
                dTemp.setMin(cursor.getInt(cursor.getColumnIndex("M_Min")));
                dTemp.setSecond(cursor.getInt(cursor.getColumnIndex("M_Sec")));
                objTemp.setDateTime(dTemp);

                /****************************************************************/
                objTemp.setAyanIndex(cursor.getInt(cursor.getColumnIndex("M_Ayanamsa")));


                /****************************************************************/
                String _cityname = cursor.getString(cursor.getColumnIndex("M_Place"));
                _cityname = _cityname.replace('_', ' ');

                BeanPlace objPlace = getCityByName(sqLiteDatabase, _cityname);
                if (objPlace.getCityId() > 0) {
                    objTemp.setPlace(objPlace);
                    objTemp.setCityID(objPlace.getCityId());
                } else {
                    objTemp.setCityID(objPlace.getCityId());
                    objPlace.setCountryId(-1);
                    objPlace.setCountryName("not define ");

                    /****************************************************************/
                    objPlace.setCityName(_cityname);
                    objPlace.setLongDeg(cursor.getString(cursor.getColumnIndex("M_LongDeg")));
                    objPlace.setLongMin(cursor.getString(cursor.getColumnIndex("M_LongMin")));
                    objPlace.setLongSec(cursor.getString(cursor.getColumnIndex("M_LongSec")));
                    objPlace.setLongDir(cursor.getString(cursor.getColumnIndex("M_LongEW")));

                    objPlace.setLatDeg(cursor.getString(cursor.getColumnIndex("M_LatDeg")));
                    objPlace.setLatMin(cursor.getString(cursor.getColumnIndex("M_LatMin")));
                    objPlace.setLatSec(cursor.getString(cursor.getColumnIndex("M_LatSec")));
                    objPlace.setLatDir(cursor.getString(cursor.getColumnIndex("M_LatNS")));
                    /****************************************************************/

                    //FOR TIMEZONE
                    /****************************************************************/
                    String _strTimezoneValue = cursor.getString(cursor.getColumnIndex("M_TimeZone"));
                    BeanPlace objTz = getTimZoneByName(sqLiteDatabase, _strTimezoneValue);


                    if (objTz != null) {
                        objPlace.setTimeZoneName(objTz.getTimeZoneName());
                        objPlace.setTimeZoneValue(objTz.getTimeZoneValue());
                        objPlace.setTimeZoneId(objTz.getTimeZoneId());

                    }
                    objTemp.setPlace(objPlace);
                    /****************************************************************/

                }
                objTemp.setDST(Integer.parseInt(cursor.getString(cursor.getColumnIndex("M_DST"))));
                objTemp.setHoraryNumber(getHoraryNumber(sqLiteDatabase, kundliId));
                objTemp.setOnlineChartId(cursor.getString(cursor.getColumnIndex("onlinechartid")));
//			        	objTemp.setOnlineChartId(getOnlineChartIdFromLocalDatabase(sqLiteDatabase, String.valueOf(kundliId)));
            }

        } catch (Exception e) {
            throw e;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }

        }
        updateKundliPersonalDetail(sqLiteDatabase, objTemp);
        return objTemp;
    }

    private int getHoraryNumber(SQLiteDatabase sqLiteDatabase, long chartID) throws Exception {

        int _iHoraryNumber = -1;
        String strChartId = String.valueOf(chartID);
        Cursor cursorHoraryNumber = null;
        try {

            cursorHoraryNumber = sqLiteDatabase.rawQuery("SELECT M_Column1 FROM tblHoroPersonalInfoMisc  WHERE id = ?",
                    new String[]{"" + strChartId});
            if (cursorHoraryNumber != null) {
                if (cursorHoraryNumber.getCount() > 0) {

                    cursorHoraryNumber.moveToFirst();
                    _iHoraryNumber = Integer.valueOf(cursorHoraryNumber.getString(cursorHoraryNumber.getColumnIndex("M_Column1")));

                }
            }
        } catch (Exception e) {
            //Log.e("getHoraryNumber", e.getMessage());
        } finally {
            if (cursorHoraryNumber != null && !cursorHoraryNumber.isClosed()) {
                cursorHoraryNumber.close();
            }

        }

        Log.d("_iHoraryNumber", String.valueOf(_iHoraryNumber));
        return _iHoraryNumber;
    }

    /**
     * This function return filled place object with values
     * according to passed place/city name
     *
     * @param cityname
     * @return Place
     * @throws Exception
     */
    private BeanPlace getCityByName(SQLiteDatabase sqLiteDatabase, String cityname) throws Exception {
        BeanPlace objCity = new BeanPlace();
        objCity.setCityId(-1);
        Cursor cursor = null;

        try {
//	    	mydbConnection=new CDatabaseHelper(_context).getReadableDatabase();
            cursor = sqLiteDatabase.rawQuery("SELECT id, cityName, longDeg,longMin, latDeg, latMin,longDir,latDir,countryId,timeZoneId FROM tblCity  WHERE cityName LIKE ?",
                    new String[]{"" + cityname + "%"});
            if (cursor.getCount() == 1) {
                //objTemp=new Place();
                cursor.moveToFirst();


                objCity.setCityId(cursor.getInt(cursor.getColumnIndex("id")));
                objCity.setCountryId(cursor.getInt(cursor.getColumnIndex("countryId")));
                objCity.setTimeZoneId(cursor.getInt(cursor.getColumnIndex("timeZoneId")));


                //Set value for City
                objCity.setCityName(cursor.getString(cursor.getColumnIndex("cityName")));
                objCity.setLongDeg(cursor.getString(cursor.getColumnIndex("longDeg")));
                objCity.setLongMin(cursor.getString(cursor.getColumnIndex("longMin")));
                objCity.setLatDeg(cursor.getString(cursor.getColumnIndex("latDeg")));
                objCity.setLatMin(cursor.getString(cursor.getColumnIndex("latMin")));

                if (cursor.getString(cursor.getColumnIndex("longDir")).equalsIgnoreCase("1"))
                    objCity.setLongDir("E");
                else
                    objCity.setLongDir("W");


                if (cursor.getString(cursor.getColumnIndex("latDir")).equalsIgnoreCase("1"))
                    objCity.setLatDir("N");
                else
                    objCity.setLatDir("S");


                //FOR TIMEZONE
                BeanPlace objTemp1 = null;
                objTemp1 = getTimZone(sqLiteDatabase, objCity.getTimeZoneId());
                if (objTemp1 != null) {
                    objCity.setTimeZoneName(objTemp1.getTimeZoneName());
                    objCity.setTimeZoneValue(objTemp1.getTimeZoneValue());

                }


            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            /* if(mydbConnection!=null && mydbConnection.isOpen())
				{
					mydbConnection.close();
					mydbConnection.releaseReference();
				}*/
        }

        return objCity;
    }

    /**
     * This function return Place object filled with time zone values(name/value/id)
     * according to passed time zone id that contain in time zone table
     *
     * @param _timeZoneId
     * @return Place
     * @throws Exception
     */

    public BeanPlace getTimZone(SQLiteDatabase sqLiteDatabase, int _timeZoneId) throws Exception {
        // TODO Auto-generated method stub
        BeanPlace objTemp = null;
        boolean conOpen = false;

        Cursor cursor = null;
        try {

            cursor = sqLiteDatabase.rawQuery("SELECT id, timezoneName,zonevalue FROM tblTimeZone  WHERE id = ?",
                    new String[]{"" + _timeZoneId});
            if (cursor.getCount() == 1) {
                objTemp = new BeanPlace();
                cursor.moveToFirst();
                objTemp.setTimeZoneId(cursor.getInt(cursor.getColumnIndex("id")));
                objTemp.setTimeZoneName(cursor.getString(cursor.getColumnIndex("timezoneName")));
                objTemp.setTimeZoneValue(cursor.getFloat(cursor.getColumnIndex("zonevalue")));

            }
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        } catch (Exception e) {
            throw e;
        }

        return objTemp;

    }

    /**
     * This function return Place object filled with time zone values(name/value/id)
     * according to passed time zone value
     *
     * @param timezonevalue
     * @return Place
     * @throws Exception
     */

    public BeanPlace getTimZoneByName(SQLiteDatabase sqLiteDatabase, String timezonevalue) throws Exception {
        BeanPlace objTemp = null;
        Cursor cursorTz = null;
        float fOldValue = 0f;
        float fTempValue = Float.valueOf(timezonevalue);
        fOldValue = fTempValue;
        if (fTempValue < 0)
            fTempValue *= -1;
        if ((fTempValue - (int) fTempValue) == 0) {
            timezonevalue = String.valueOf((int) fOldValue);
        }

        try {
            cursorTz = sqLiteDatabase.rawQuery("SELECT id, timezoneName,zonevalue FROM tblTimeZone  WHERE zonevalue= ?",
                    new String[]{"" + timezonevalue});
            if (cursorTz.getCount() == 1) {
                objTemp = new BeanPlace();
                cursorTz.moveToFirst();
                objTemp.setTimeZoneId(cursorTz.getInt(cursorTz.getColumnIndex("id")));
                objTemp.setTimeZoneName(cursorTz.getString(cursorTz.getColumnIndex("timezoneName")));
                objTemp.setTimeZoneValue(cursorTz.getFloat(cursorTz.getColumnIndex("zonevalue")));
            }
        } catch (Exception e) {
            throw e;
        } finally {
            if (cursorTz != null && !cursorTz.isClosed()) {
                cursorTz.close();
            }
        }
        return objTemp;
    }

    /**
     * This function return online chart id from relational table
     * @param localChartID
     * @return String
     */
	/*private String getOnlineChartIdFromLocalDatabase(
			SQLiteDatabase sqLiteDatabase, String localChartID) {
		String _onlineChartId = "";
		Cursor cursor = null;

		try {
			String strQuery = "SELECT onlinechartid FROM tblLocalOnlineChartRelation  WHERE localchartid like '%"
					+ localChartID.trim() + "%'";

			cursor = sqLiteDatabase.rawQuery(strQuery, null);
			if (cursor.getCount() > 0) {

				cursor.moveToFirst();
				_onlineChartId = cursor.getString(cursor
						.getColumnIndex("onlinechartid"));

			}
			if (cursor != null && !cursor.isClosed()) {
				cursor.close();
			}

		} catch (Exception e) {

		}

		return _onlineChartId;
	}*/

    /**
     * This function is used to add/edit kundli personal details
     *
     * @param sqLiteDatabase
     * @param beanHoroPersonalInfo
     * @return (long)kundli local id
     * @throws Exception
     */
    @Override
    public long addEditHoroPersonalInfo(SQLiteDatabase sqLiteDatabase,
                                        BeanHoroPersonalInfo beanHoroPersonalInfo) throws Exception {
        long kundliLocalId = 0;
        long onLineChartId = 0;
        try {
            if ((beanHoroPersonalInfo.getOnlineChartId() != null) && (beanHoroPersonalInfo.getOnlineChartId().trim().length() > 0)) {
                onLineChartId = Long.valueOf(beanHoroPersonalInfo.getOnlineChartId().trim());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        if ((beanHoroPersonalInfo.getOnlineChartId() != null) && (beanHoroPersonalInfo.getOnlineChartId().trim().length() > 0) && (onLineChartId > 0)) {
            if (!(beanHoroPersonalInfo.getLocalChartId() > 0)) {
                beanHoroPersonalInfo.setLocalChartId(getLocalChartIdWithRespactiveOnline(sqLiteDatabase, beanHoroPersonalInfo.getOnlineChartId()));
            }
        }
        if (beanHoroPersonalInfo.getLocalChartId() > 0) {
            //UPDATE KUNDLI
            kundliLocalId = updateKundliPersonalDetail(sqLiteDatabase, beanHoroPersonalInfo);
        } else { //ADD KUNDLI
            kundliLocalId = addKundliPersonalDetail(sqLiteDatabase, beanHoroPersonalInfo);
        }

        return kundliLocalId;
    }

    public long getLocalChartIdWithRespactiveOnline(SQLiteDatabase sqLiteDatabase, String onlineChartId) {
        long localChartId = 0;
        String strQueryCheckLocalId = "SELECT id FROM tblHoroPersonalInfo WHERE onlinechartid like '%" + onlineChartId + "%'";
        Cursor cursorCheckLocalId = null;
        cursorCheckLocalId = sqLiteDatabase.rawQuery(strQueryCheckLocalId, null);

        if (cursorCheckLocalId.getCount() > 0) {
            cursorCheckLocalId.moveToFirst();
            String localId = cursorCheckLocalId.getString(cursorCheckLocalId.getColumnIndex("id"));
            if (localId != null) {
                try {
                    localChartId = Long.valueOf(localId);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                    localChartId = 0;
                }
            }
        } else {
            localChartId = 0;
        }
        if (cursorCheckLocalId != null && !cursorCheckLocalId.isClosed()) {
            cursorCheckLocalId.close();
        }
        return localChartId;
    }

    private long addKundliPersonalDetail(SQLiteDatabase sqLiteDatabase,
                                         BeanHoroPersonalInfo beanHoroPersonalInfo) {
        long kundliLocalId = 0;


        //ADD KUNDLI PERSONAL DETAILS
        String _query = "INSERT INTO tblHoroPersonalInfo(id,M_UserId,M_PName,M_Sex,M_Day ,M_Month,M_Year,M_Hr ,M_Min,M_Sec,M_DST,M_Place,M_LongDeg ,M_LongMin,M_LongSec ,M_LongEW ,M_LatDeg,M_LatMin,M_LatSec,M_LatNS,M_TimeZone,M_Ayanamsa,M_TimeStamp,onlinechartid) VALUES(";

        kundliLocalId = new Date().getTime();

        _query += "'" + String.valueOf(kundliLocalId) + "',";
        _query += "'" + CGlobalVariables.APP_USER_ID + "',";
        _query += "'" + beanHoroPersonalInfo.getName() + "',";
        _query += "'" + beanHoroPersonalInfo.getGender() + "',";
        _query += "'" + String.valueOf(beanHoroPersonalInfo.getDateTime().getDay()) + "',";
        _query += "'" + String.valueOf(beanHoroPersonalInfo.getDateTime().getMonth()) + "',";
        _query += "'" + String.valueOf(beanHoroPersonalInfo.getDateTime().getYear()) + "',";
        _query += "'" + String.valueOf(beanHoroPersonalInfo.getDateTime().getHour()) + "',";
        _query += "'" + String.valueOf(beanHoroPersonalInfo.getDateTime().getMin()) + "',";
        _query += "'" + String.valueOf(beanHoroPersonalInfo.getDateTime().getSecond()) + "',";
        _query += "'" + String.valueOf(beanHoroPersonalInfo.getDST()).trim() + "',";
        _query += "'" + beanHoroPersonalInfo.getPlace().getCityName().trim() + "',";
        _query += "'" + beanHoroPersonalInfo.getPlace().getLongDeg().trim() + "',";
        _query += "'" + beanHoroPersonalInfo.getPlace().getLongMin().trim() + "',";
        _query += "'00',";
        _query += "'" + beanHoroPersonalInfo.getPlace().getLongDir().trim() + "',";
        _query += "'" + beanHoroPersonalInfo.getPlace().getLatDeg().trim() + "',";
        _query += "'" + beanHoroPersonalInfo.getPlace().getLatMin().trim() + "',";
        _query += "'00',";
        _query += "'" + beanHoroPersonalInfo.getPlace().getLatDir().trim() + "',";
        _query += "'" + beanHoroPersonalInfo.getPlace().getTimeZoneValue() + "',";
        _query += "'" + String.valueOf(beanHoroPersonalInfo.getAyanIndex()) + "',";
        //timestamp
        _query += new Date().getTime() + ",";
        //onlinechartid
        _query += "'" + beanHoroPersonalInfo.getOnlineChartId() + "')";

        sqLiteDatabase.execSQL(_query);


        //END KUNDLI PERSONAL DETAILS
        //SAVE HORARY NUMBER
        String _queryHoraryNumber = "INSERT INTO tblHoroPersonalInfoMisc(id," +
                "M_Column1,M_Column2,M_Column3,M_Column4,M_Column5,M_Column6," +
                "M_Column7,M_Column8,M_Column9,M_Column10,M_Column11,M_Column12) VALUES(";

        _queryHoraryNumber += "'" + String.valueOf(kundliLocalId) + "',";
        _queryHoraryNumber += "'" + String.valueOf(beanHoroPersonalInfo.getHoraryNumber()) + "',";//COLUMN -1
        _queryHoraryNumber += "'0',";////COLUMN -2
        _queryHoraryNumber += "'0',";//COLUMN -3
        _queryHoraryNumber += "'0',";//COLUMN -4
        _queryHoraryNumber += "'0',";//COLUMN -5
        _queryHoraryNumber += "'0',";//COLUMN -6
        _queryHoraryNumber += "'0',";//COLUMN -7
        _queryHoraryNumber += "'0',";//COLUMN -8
        _queryHoraryNumber += "'0',";//COLUMN -9
        _queryHoraryNumber += "'0',";//COLUMN -10
        _queryHoraryNumber += "'0',";//COLUMN -11
        _queryHoraryNumber += "'0' )";//COLUMN-12
        sqLiteDatabase.execSQL(_queryHoraryNumber);

        //END

        return kundliLocalId;

    }

    private long updateKundliPersonalDetail(SQLiteDatabase sqLiteDatabase, BeanHoroPersonalInfo beanHoroPersonalInfo) {

        //UPDATE KUNDLI PERTICULAR DETAILS
        ContentValues args = new ContentValues();
        args.put("M_PName", beanHoroPersonalInfo.getName());
        args.put("M_Sex", beanHoroPersonalInfo.getGender());
        args.put("M_Day", String.valueOf(beanHoroPersonalInfo.getDateTime().getDay()));
        args.put("M_Month", String.valueOf(beanHoroPersonalInfo.getDateTime().getMonth()));
        args.put("M_Year", String.valueOf(beanHoroPersonalInfo.getDateTime().getYear()));
        args.put("M_Hr", String.valueOf(beanHoroPersonalInfo.getDateTime().getHour()));
        args.put("M_Min", String.valueOf(beanHoroPersonalInfo.getDateTime().getMin()));
        args.put("M_Sec", String.valueOf(beanHoroPersonalInfo.getDateTime().getSecond()));
        args.put("M_DST", String.valueOf(beanHoroPersonalInfo.getDST()).trim());
        args.put("M_Place", beanHoroPersonalInfo.getPlace().getCityName().trim());
        args.put("M_LongDeg", beanHoroPersonalInfo.getPlace().getLongDeg().trim());
        args.put("M_LongMin", beanHoroPersonalInfo.getPlace().getLongMin().trim());
        args.put("M_LongEW", beanHoroPersonalInfo.getPlace().getLongDir().trim());
        args.put("M_LatDeg", beanHoroPersonalInfo.getPlace().getLatDeg().trim());
        args.put("M_LatMin", beanHoroPersonalInfo.getPlace().getLatMin().trim());
        args.put("M_LatNS", beanHoroPersonalInfo.getPlace().getLatDir().trim());
        args.put("M_TimeZone", beanHoroPersonalInfo.getPlace().getTimeZoneValue());
        args.put("M_Ayanamsa", String.valueOf(beanHoroPersonalInfo.getAyanIndex()));
        args.put("M_TimeStamp", new Date().getTime());
        args.put("onlinechartid", beanHoroPersonalInfo.getOnlineChartId());

        sqLiteDatabase.update("tblHoroPersonalInfo", args, "id" + "=" + beanHoroPersonalInfo.getLocalChartId(), null);


        //UPDATE HORARY NUMBER
        ContentValues argsHorary = new ContentValues();
        argsHorary.put("M_Column1", beanHoroPersonalInfo.getHoraryNumber());
        argsHorary.put("M_Column2", "0");
        argsHorary.put("M_Column3", "0");
        argsHorary.put("M_Column4", "0");
        argsHorary.put("M_Column5", "0");
        argsHorary.put("M_Column6", "0");
        argsHorary.put("M_Column7", "0");
        argsHorary.put("M_Column8", "0");
        argsHorary.put("M_Column9", "0");
        argsHorary.put("M_Column10", "0");
        argsHorary.put("M_Column11", "0");
        argsHorary.put("M_Column12", "0");
        sqLiteDatabase.update("tblHoroPersonalInfoMisc", argsHorary, "id" + "=" + beanHoroPersonalInfo.getLocalChartId(), null);

        return beanHoroPersonalInfo.getLocalChartId();
    }

    public boolean deleteOnlineChartIdFromLocalDatabase(SQLiteDatabase sQLiteDatabase, String onlineChartId) {
        boolean isDeletedOnlineId = false;
        long localChartId = 0;
        localChartId = getLocalChartIdWithRespactiveOnline(sQLiteDatabase, onlineChartId.trim());
        if (localChartId != 0) {
            //UPDATE KUNDLI PERTICULAR DETAILS
            ContentValues args = new ContentValues();
            args.put("onlinechartid", "");
            int noOfRowsEffacted = sQLiteDatabase.update("tblHoroPersonalInfo", args, "id" + "=" + localChartId, null);
            if (noOfRowsEffacted > 0) {
                isDeletedOnlineId = true;
            }
        }
        return isDeletedOnlineId;
    }


}
