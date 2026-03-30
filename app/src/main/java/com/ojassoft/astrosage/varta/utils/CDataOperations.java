package com.ojassoft.astrosage.varta.utils;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.ojassoft.astrosage.varta.interfacefile.IDataOperations;
import com.ojassoft.astrosage.varta.model.BeanPlace;

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

}
