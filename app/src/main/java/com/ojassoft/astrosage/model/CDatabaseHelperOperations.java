package com.ojassoft.astrosage.model;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanDateTime;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.beans.BeanPlace;
import com.ojassoft.astrosage.beans.KundliChatHistoryBean;
import com.ojassoft.astrosage.jinterface.IDatabaseHelperOperations;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.twiliochat.chat.messages.ChatMessage;
import com.ojassoft.astrosage.varta.twiliochat.chat.messages.Message;
import com.ojassoft.astrosage.varta.twiliochat.chat.messages.UserMessage;
import com.ojassoft.astrosage.varta.utils.CDatabaseHelper;
import com.ojassoft.astrosage.varta.utils.CUtils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class CDatabaseHelperOperations extends SQLiteOpenHelper implements IDatabaseHelperOperations {

    /**
     * This field is used to create data base,so the database name is assigned
     * in this field ,used in this application.
     */
    public static final String DATABASE_NAME = "OjassoftMKDb";

    /**
     * This is a context variable used in this class.
     */
    protected Context context;

    /**
     * This is the constructor of this class to initialize variables,used in the
     * whole class
     */
    /**
     * This constructor is added by Bijendra on 18-Dec-2012
     *
     * @param context
     */
    public CDatabaseHelperOperations(Context context) {
        super(context, DATABASE_NAME, null, 12 /* in app version 3.0 */);
        //Version 9 Alter State and Country field in tblHoroPersonalInfo
        // DB
        // version
        // 5 in
        // app
        // version
        // 2.99
        this.context = context; // db version increse by 1 before 7 now 8 by
        // Shelendra on 28 .05 .2015 because off
        // timezone fix for LEICESTER city in db.
    }

    /**
     * This function is called with different parameters to create table with
     * data in database for this application.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        createOrInsertData(db, R.raw.sqlcountry);
        createOrInsertData(db, R.raw.sqltimezone);
        createOrInsertData(db, R.raw.sqlcity);
        createOrInsertData(db, R.raw.sqlpersonalinfomisc);
        createOrInsertData(db, R.raw.sqlchartinfo);
        createOrInsertData(db, R.raw.sqlpersonalinfo);
        createOrInsertData(db, R.raw.sqlchathistory);
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * android.database.sqlite.SQLiteOpenHelper#onUpgrade(android.database.sqlite
     * .SQLiteDatabase, int, int)
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if ((oldVersion != newVersion) && (newVersion == 7)) {
            db.execSQL("ALTER TABLE tblHoroPersonalInfo ADD M_TimeStamp long not null default 0");
            db.execSQL("ALTER TABLE tblHoroPersonalInfo ADD onlinechartid VARCHAR(30)");
            Cursor cursorOnlineIds = db
                    .rawQuery(
                            "SELECT localchartid, onlinechartid FROM tblLocalOnlineChartRelation",
                            null);
            String[][] onlineLocalRelation = null;
            if (cursorOnlineIds != null) {
                if (cursorOnlineIds.getCount() > 0) {
                    onlineLocalRelation = new String[cursorOnlineIds.getCount()][2];
                    if (cursorOnlineIds.moveToFirst()) {
                        int i = 0;
                        do {
                            onlineLocalRelation[i][0] = cursorOnlineIds
                                    .getString(0);
                            onlineLocalRelation[i][1] = cursorOnlineIds
                                    .getString(1);
                            i++;
                        } while (cursorOnlineIds.moveToNext());
                        for (int j = 0; j < onlineLocalRelation.length; j++) {
                            ContentValues args = new ContentValues();
                            args.put("onlinechartid", onlineLocalRelation[j][1]);
                            db.update("tblHoroPersonalInfo", args, "id" + "="
                                    + onlineLocalRelation[j][0], null);
                        }
                    }
                }
            }
        }
        if ((oldVersion != newVersion) && (newVersion == 9)) {
            db.execSQL("ALTER TABLE tblHoroPersonalInfo ADD M_State VARCHAR(50) DEFAULT 'not define'");
            db.execSQL("ALTER TABLE tblHoroPersonalInfo ADD M_Country VARCHAR(50) DEFAULT 'not define'");
        }

        if(newVersion == 12){
            db.execSQL("DROP TABLE IF EXISTS tblKundaliAiChatHistory");
            createOrInsertData(db, R.raw.sqlchathistory);
        }
    }

    /**
     * This is a main function called by other function to create single table
     * based on Resource id of xml file in RAW folder in the project
     *
     * @param dbIn
     * @param intResourceID
     * @return void
     */
    private void createOrInsertData(SQLiteDatabase dbIn, int intResourceID) {
        String s;
        try {
            InputStream in = context.getResources().openRawResource(
                    intResourceID);
            DocumentBuilder builder = DocumentBuilderFactory.newInstance()
                    .newDocumentBuilder();
            Document doc = builder.parse(in, null);
            NodeList statements = doc.getElementsByTagName("statement");
            for (int i = 0; i < statements.getLength(); i++) {
                s = statements.item(i).getChildNodes().item(0).getNodeValue();
                dbIn.execSQL(s);
            }
        } catch (Throwable t) {
            Toast.makeText(context, t.toString(), Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void finalize() throws Throwable {
        try {
            this.close();
        } catch (Exception e) {

        }
        super.finalize();
    }

    //DATA BASE OPERATIONS

    /**
     * This function return the array of city id and name
     *
     * @param //sqLiteDatabase
     * @param cityName
     * @return String[][]
     * @throws Exception
     * @author Bijendra 31-may-13
     */

    public String[][] searchCityOperation(String cityName)
            throws Exception {
        String[][] arrCity = null;
        int index = 0;
        Cursor cursor = null;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
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
            sqLiteDatabase.close(); // Closing database connection
        }

        return arrCity;
    }

    /**
     * This function is used to fatch city detail based on city id
     *
     * @param //sqLiteDatabase
     * @param cityId
     * @return BeanPlace
     * @throws Exception
     */

    public BeanPlace getCityByIdOperation(int cityId)
            throws Exception {
        BeanPlace objTemp = null;
        Cursor cursor = null;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
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
                objTemp1 = getTimZoneIdUpdated(sqLiteDatabase, objTemp.getTimeZoneId());
                if (objTemp1 != null) {
                    objTemp.setTimeZoneName(objTemp1.getTimeZoneName());
                    objTemp.setTimeZoneValue(objTemp1.getTimeZoneValue());

                }


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
//            sqLiteDatabase = this.getReadableDatabase();
            sqLiteDatabase.close(); // Closing database connection
        }

        return objTemp;
    }

    /**
     * This function return Place object filled with time zone values(name/value/id)
     * according to passed time zone id that contain in time zone table
     *
     * @param timeZoneId
     * @return BeanPlace
     * @throws Exception
     */

    public BeanPlace getTimZoneIdOperation(int timeZoneId) throws Exception {
        // TODO Auto-generated method stub
        BeanPlace objTemp = null;
        Cursor cursor = null;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
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
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            sqLiteDatabase.close(); // Closing database connection
        }

        return objTemp;
    }

    /**
     * This function return Place object filled with time zone values(name/value/id)
     * according to passed time zone id that contain in time zone table
     *
     * @param timeZoneId
     * @return BeanPlace
     * @throws Exception
     */

    private BeanPlace getTimZoneIdUpdated(SQLiteDatabase sqLiteDatabase, int timeZoneId) throws Exception {
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
     * @param //sqLiteDatabase
     * @param //time           zone name
     * @return String array contain time zone id and name
     * @throws Exception
     */

    public String[][] searchTimeZoneOperation(
            String searchText) throws Exception {
        String[][] arrTimeZone = null;
        int index = 0;
        Cursor cursor = null;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
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
            sqLiteDatabase.close(); // Closing database connection
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
    public BeanPlace getNewTimZoneByNameOperation(String timezonevalue) throws Exception {
        // TODO Auto-generated method stub
        BeanPlace objTemp = null;

        Cursor cursor = null;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        try {

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
            sqLiteDatabase.close();

        }

        return objTemp;

    }


    public Map<String, String> searchLocalKundliListOperation(
            String kundliName, int genderType, int noOfRecords) throws Exception {
        Map<String, String> arrHoro = null;
        Cursor cursor = null;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
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
            sqLiteDatabase.close(); // Closing database connection
        }


        return arrHoro;
    }

    /**
     * This function is used to delete local kundli
     *
     * @param //sqLiteDatabase
     * @param kundliId
     * @return boolean
     * @throws Exception
     */

    public boolean deleteLocalKundliOperation(long kundliId)
            throws Exception {
        boolean isSuccess = true;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        try {
            sqLiteDatabase.execSQL("DELETE FROM tblHoroPersonalInfo WHERE id=" + kundliId);
        } catch (Exception e) {
            throw e;
        } finally {
            sqLiteDatabase.close(); // Closing database connection
        }


        return isSuccess;
    }

    public boolean deleteLocalKundliOperation(long kundliId, long onlineChartId)
            throws Exception {
        boolean isSuccess = true;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        try {
            if (kundliId != -1) {
                sqLiteDatabase.execSQL("DELETE FROM tblHoroPersonalInfo WHERE id=" + kundliId);
            } else {
                sqLiteDatabase.execSQL("DELETE FROM tblHoroPersonalInfo WHERE onlinechartid=" + onlineChartId);
            }

        } catch (Exception e) {
            throw e;
        } finally {
            sqLiteDatabase.close(); // Closing database connection
        }


        return isSuccess;
    }


    public BeanHoroPersonalInfo getLocalKundliDetailOperation(
            long kundliId) throws Exception {
        BeanHoroPersonalInfo objTemp = null;
        Cursor cursor = null;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
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
                        /*if(objPlace.getCityId()>0)
                        {
			        		objTemp.setPlace(objPlace);
			        		objTemp.setCityID(objPlace.getCityId());
			        	}
			        	else 
			        	{*/
                objTemp.setCityID(objPlace.getCityId());
                objPlace.setCountryId(-1);
                //objPlace.setCountryName("not define");
                objPlace.setCountryName(cursor.getString(cursor.getColumnIndex("M_Country")));
                objPlace.setState(cursor.getString(cursor.getColumnIndex("M_State")));
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
                BeanPlace objTz = getTimZoneByNameUpdated(sqLiteDatabase, _strTimezoneValue);


                if (objTz != null) {
                    objPlace.setTimeZoneName(objTz.getTimeZoneName());
                    objPlace.setTimeZoneValue(objTz.getTimeZoneValue());
                    objPlace.setTimeZoneId(objTz.getTimeZoneId());
                    try{
                        objPlace.setTimeZone(String.valueOf(objTz.getTimeZoneValue()));
                    }catch (Exception e){
                        //
                    }
                }
                objTemp.setPlace(objPlace);
                /****************************************************************/

                //}
                objTemp.setDST(Integer.parseInt(cursor.getString(cursor.getColumnIndex("M_DST"))));
                objTemp.setHoraryNumber(getHoraryNumber(sqLiteDatabase, kundliId));
                objTemp.setOnlineChartId(cursor.getString(cursor.getColumnIndex("onlinechartid")));
//			        	objTemp.setOnlineChartId(getOnlineChartIdFromLocalDatabase(sqLiteDatabase, String.valueOf(kundliId)));
            }
            updateKundliPersonalDetail(sqLiteDatabase, objTemp);
        } catch (Exception e) {
            throw e;
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            sqLiteDatabase.close(); // Closing database connection
        }
        //updateKundliPersonalDetail(sqLiteDatabase,objTemp);
        return objTemp;
    }

    private int getHoraryNumber(SQLiteDatabase sqLiteDatabase, long chartID) throws Exception {

        int _iHoraryNumber = -1;


        String strChartId = String.valueOf(chartID);
        Cursor cursorHoraryNumber = null;
        try {

            //cursorHoraryNumber = sqLiteDatabase.rawQuery("SELECT id FROM tblHoroPersonalInfoMisc", null);
            cursorHoraryNumber = sqLiteDatabase.rawQuery("SELECT M_Column1 FROM tblHoroPersonalInfoMisc  WHERE id = ?",
                    new String[]{"" + strChartId});
            if (cursorHoraryNumber != null) {
                if (cursorHoraryNumber.getCount() > 0) {

                    cursorHoraryNumber.moveToFirst();
                    //String id = cursorHoraryNumber.getString(cursorHoraryNumber.getColumnIndex("id"));
                    _iHoraryNumber = Integer.valueOf(cursorHoraryNumber.getString(cursorHoraryNumber.getColumnIndex("M_Column1")));
                    //Log.i("", id);
                }
            }
        } catch (Exception e) {
            Log.e("getHoraryNumber", e.getMessage());
        } finally {
            if (cursorHoraryNumber != null && !cursorHoraryNumber.isClosed()) {
                cursorHoraryNumber.close();
            }

        }

        // Log.d("_iHoraryNumber",String.valueOf(_iHoraryNumber));
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
                objTemp1 = getTimZoneUpdated(sqLiteDatabase, objCity.getTimeZoneId());
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

    public BeanPlace getTimZoneOperation(int _timeZoneId) throws Exception {
        // TODO Auto-generated method stub
        BeanPlace objTemp = null;
        boolean conOpen = false;

        Cursor cursor = null;
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
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
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            sqLiteDatabase.close(); // Closing database connection
        }
        return objTemp;

    }

    /**
     * This function return Place object filled with time zone values(name/value/id)
     * according to passed time zone id that contain in time zone table
     *
     * @param _timeZoneId
     * @return Place
     * @throws Exception
     */

    private BeanPlace getTimZoneUpdated(SQLiteDatabase sqLiteDatabase, int _timeZoneId) throws Exception {
        // TODO Auto-generated method stub
        BeanPlace objTemp = null;
        //boolean conOpen=false;

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

    public BeanPlace getTimZoneByNameOperation(String timezonevalue) throws Exception {
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
        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
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
            sqLiteDatabase.close(); // Closing database connection
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

    private BeanPlace getTimZoneByNameUpdated(SQLiteDatabase sqLiteDatabase, String timezonevalue) throws Exception {
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
     * This function is used to add/edit kundli personal details
     *
     * @param //sqLiteDatabase
     * @param beanHoroPersonalInfo
     * @return (long)kundli local id
     * @throws Exception
     */

    public long addEditHoroPersonalInfoOperation(
            BeanHoroPersonalInfo beanHoroPersonalInfo) throws Exception {
        long kundliLocalId = 0;
        long onLineChartId = 0;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        try {
            try {
                if ((beanHoroPersonalInfo.getOnlineChartId() != null) && (beanHoroPersonalInfo.getOnlineChartId().trim().length() > 0)) {
                    onLineChartId = Long.valueOf(beanHoroPersonalInfo.getOnlineChartId().trim());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if ((beanHoroPersonalInfo.getOnlineChartId() != null) && (beanHoroPersonalInfo.getOnlineChartId().trim().length() > 0) && (onLineChartId > 0)) {
                if (!(beanHoroPersonalInfo.getLocalChartId() > 0)) {
                    beanHoroPersonalInfo.setLocalChartId(getLocalChartIdWithRespactiveOnlineUpdated(sqLiteDatabase, beanHoroPersonalInfo.getOnlineChartId()));
                }
            }
            if (beanHoroPersonalInfo.getLocalChartId() > 0) {
                //UPDATE KUNDLI
                kundliLocalId = updateKundliPersonalDetail(sqLiteDatabase, beanHoroPersonalInfo);
            } else { //ADD KUNDLI
                kundliLocalId = addKundliPersonalDetail(sqLiteDatabase, beanHoroPersonalInfo);
            }

        } finally {
            sqLiteDatabase.close(); // Closing database connection
        }
        return kundliLocalId;
    }

    public long getLocalChartIdWithRespactiveOnlineOperation(String onlineChartId) {
        long localChartId = 0;
        String strQueryCheckLocalId = "SELECT id FROM tblHoroPersonalInfo WHERE onlinechartid like '%" + onlineChartId + "%'";
        Cursor cursorCheckLocalId = null;

        SQLiteDatabase sqLiteDatabase = this.getReadableDatabase();
        try {
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
        } finally {
            sqLiteDatabase.close(); // Closing database connection
        }
        return localChartId;
    }

    private long getLocalChartIdWithRespactiveOnlineUpdated(SQLiteDatabase sqLiteDatabase, String onlineChartId) {
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
        String _query = "INSERT INTO tblHoroPersonalInfo(id,M_UserId,M_PName,M_Sex,M_Day ,M_Month,M_Year,M_Hr ,M_Min,M_Sec,M_DST,M_Place,M_LongDeg ,M_LongMin,M_LongSec ,M_LongEW ,M_LatDeg,M_LatMin,M_LatSec,M_LatNS,M_TimeZone,M_Ayanamsa,M_TimeStamp,onlinechartid,M_State,M_Country) VALUES(";
        //String _query = "INSERT INTO tblHoroPersonalInfo(id,M_UserId,M_PName,M_Sex,M_Day ,M_Month,M_Year,M_Hr ,M_Min,M_Sec,M_DST,M_Place,M_LongDeg ,M_LongMin,M_LongSec ,M_LongEW ,M_LatDeg,M_LatMin,M_LatSec,M_LatNS,M_TimeZone,M_Ayanamsa,M_TimeStamp,onlinechartid) VALUES(";
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
        _query += "'" + beanHoroPersonalInfo.getOnlineChartId() + "',";
        _query += "'" + beanHoroPersonalInfo.getPlace().getState().trim() + "',";
        _query += "'" + beanHoroPersonalInfo.getPlace().getCountryName().trim() + "')";

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
        args.put("M_State", beanHoroPersonalInfo.getPlace().getState().trim());
        args.put("M_Country", beanHoroPersonalInfo.getPlace().getCountryName().trim());

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

    public boolean deleteOnlineChartIdFromLocalDatabaseOperation(String onlineChartId) {
        boolean isDeletedOnlineId = false;
        long localChartId = 0;
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        try {
            localChartId = getLocalChartIdWithRespactiveOnlineUpdated(sqLiteDatabase, onlineChartId.trim());
            if (localChartId != 0) {
                //UPDATE KUNDLI PERTICULAR DETAILS
                ContentValues args = new ContentValues();
                args.put("onlinechartid", "");
                int noOfRowsEffacted = sqLiteDatabase.update("tblHoroPersonalInfo", args, "id" + "=" + localChartId, null);
                if (noOfRowsEffacted > 0) {
                    isDeletedOnlineId = true;
                }
            }
        } finally {
            sqLiteDatabase.close(); // Closing database connection
        }

        return isDeletedOnlineId;
    }

    //get total kundli in local database
    public int getKundliCount() {
        int cnt = 0;
        Cursor cursor = null;
        SQLiteDatabase db = null;
        try {
            String countQuery = "SELECT  * FROM tblHoroPersonalInfo";
            db = this.getReadableDatabase();
            cursor = db.rawQuery(countQuery, null);
            cnt = cursor.getCount();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }

        return cnt;
    }

    //get local kundlis for server sync
    public ArrayList<BeanHoroPersonalInfo> getOfflineKundliForSync() {
        ArrayList<BeanHoroPersonalInfo> kundliArrayList = new ArrayList<BeanHoroPersonalInfo>();
        Cursor cursor = null;
        SQLiteDatabase sqLiteDatabase = null;
        try {
            sqLiteDatabase = this.getReadableDatabase();
            cursor = sqLiteDatabase.rawQuery("SELECT * FROM tblHoroPersonalInfo  WHERE onlinechartid = ? or onlinechartid = ? Limit 50",
                    new String[]{"", "-1"});
            if (cursor.getCount() > 0) {
                BeanHoroPersonalInfo beanHoroPersonalInfo;
                if (cursor.moveToFirst()) {
                    do {
                        beanHoroPersonalInfo = new BeanHoroPersonalInfo();
                        long id = cursor.getLong(cursor.getColumnIndex("id"));
                        beanHoroPersonalInfo.setLocalChartId(id);//ADDED BY BIJENDRA (17-NOV-2012)
                        beanHoroPersonalInfo.setName(cursor.getString(cursor.getColumnIndex("M_PName")));
                        beanHoroPersonalInfo.setGender(cursor.getString(cursor.getColumnIndex("M_Sex")));
                        BeanDateTime dTemp = new BeanDateTime();
                        dTemp.setDay(cursor.getInt(cursor.getColumnIndex("M_Day")));
                        dTemp.setMonth(cursor.getInt(cursor.getColumnIndex("M_Month")));
                        dTemp.setYear(cursor.getInt(cursor.getColumnIndex("M_Year")));
                        dTemp.setHour(cursor.getInt(cursor.getColumnIndex("M_Hr")));
                        dTemp.setMin(cursor.getInt(cursor.getColumnIndex("M_Min")));
                        dTemp.setSecond(cursor.getInt(cursor.getColumnIndex("M_Sec")));
                        beanHoroPersonalInfo.setDateTime(dTemp);
                        beanHoroPersonalInfo.setAyanIndex(cursor.getInt(cursor.getColumnIndex("M_Ayanamsa")));
                        String _cityname = cursor.getString(cursor.getColumnIndex("M_Place"));
                        _cityname = _cityname.replace('_', ' ');
                        BeanPlace objPlace = getCityByName(sqLiteDatabase, _cityname);
                        beanHoroPersonalInfo.setCityID(objPlace.getCityId());
                        objPlace.setCountryId(-1);
                        //objPlace.setCountryName("not define ");
                        objPlace.setCountryName(cursor.getString(cursor.getColumnIndex("M_Country")));
                        objPlace.setState(cursor.getString(cursor.getColumnIndex("M_State")));
                        objPlace.setCityName(_cityname);
                        objPlace.setLongDeg(cursor.getString(cursor.getColumnIndex("M_LongDeg")));
                        objPlace.setLongMin(cursor.getString(cursor.getColumnIndex("M_LongMin")));
                        objPlace.setLongSec(cursor.getString(cursor.getColumnIndex("M_LongSec")));
                        objPlace.setLongDir(cursor.getString(cursor.getColumnIndex("M_LongEW")));
                        objPlace.setLatDeg(cursor.getString(cursor.getColumnIndex("M_LatDeg")));
                        objPlace.setLatMin(cursor.getString(cursor.getColumnIndex("M_LatMin")));
                        objPlace.setLatSec(cursor.getString(cursor.getColumnIndex("M_LatSec")));
                        objPlace.setLatDir(cursor.getString(cursor.getColumnIndex("M_LatNS")));
                        String _strTimezoneValue = cursor.getString(cursor.getColumnIndex("M_TimeZone"));
                        BeanPlace objTz = getTimZoneByNameUpdated(sqLiteDatabase, _strTimezoneValue);
                        if (objTz != null) {
                            objPlace.setTimeZoneName(objTz.getTimeZoneName());
                            objPlace.setTimeZoneValue(objTz.getTimeZoneValue());
                            objPlace.setTimeZoneId(objTz.getTimeZoneId());
                        }
                        beanHoroPersonalInfo.setPlace(objPlace);
                        beanHoroPersonalInfo.setDST(Integer.parseInt(cursor.getString(cursor.getColumnIndex("M_DST"))));
                        beanHoroPersonalInfo.setHoraryNumber(getHoraryNumber(sqLiteDatabase, id));
                        beanHoroPersonalInfo.setOnlineChartId(cursor.getString(cursor.getColumnIndex("onlinechartid")));
                        kundliArrayList.add(beanHoroPersonalInfo);

                    } while (cursor.moveToNext());
                }

            } else {

            }
        } catch (Exception e) {
            Log.e("exception>>>", e.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            sqLiteDatabase.close();
        }

        return kundliArrayList;
    }

    // insert and update kundali in local database on base of onlinechart id
    public long synchDataFromServerToLocal(BeanHoroPersonalInfo beanHoroPersonalInfo) {
        long kundliLocalId = 0;
        ArrayList<BeanHoroPersonalInfo> kundliArrayList = new ArrayList<BeanHoroPersonalInfo>();
        Cursor cursor = null;
        SQLiteDatabase sqLiteDatabase = null;
        try {
            sqLiteDatabase = this.getReadableDatabase();
            cursor = sqLiteDatabase.rawQuery("SELECT * FROM tblHoroPersonalInfo  WHERE onlinechartid = ? ",
                    new String[]{beanHoroPersonalInfo.getOnlineChartId()});
            if (cursor.getCount() == 0) {
                String _query = "INSERT INTO tblHoroPersonalInfo(id,M_UserId,M_PName,M_Sex,M_Day ,M_Month,M_Year,M_Hr ,M_Min,M_Sec,M_DST,M_Place,M_LongDeg ,M_LongMin,M_LongSec ,M_LongEW ,M_LatDeg,M_LatMin,M_LatSec,M_LatNS,M_TimeZone,M_Ayanamsa,M_TimeStamp,onlinechartid,M_State,M_Country) VALUES(";

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
                _query += "'" + beanHoroPersonalInfo.getOnlineChartId() + "',";
                _query += "'" + beanHoroPersonalInfo.getPlace().getState().trim() + "',";
                _query += "'" + beanHoroPersonalInfo.getPlace().getCountryName().trim() + "')";

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


            } else {
                kundliLocalId = updateKundliPersonalDetailOnlineChartID(sqLiteDatabase, beanHoroPersonalInfo);
            }
        } catch (Exception e) {
            Log.e("exception>>>", e.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            if(sqLiteDatabase != null) {
                sqLiteDatabase.close();
            }
        }
        return kundliLocalId;

    }

    //update kunali in database on base of onlinechartid
    private long updateKundliPersonalDetailOnlineChartID(SQLiteDatabase sqLiteDatabase, BeanHoroPersonalInfo beanHoroPersonalInfo) {

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
        //args.put("onlinechartid", beanHoroPersonalInfo.getOnlineChartId());

        sqLiteDatabase.update("tblHoroPersonalInfo", args, "onlinechartid" + "=" + beanHoroPersonalInfo.getOnlineChartId(), null);
        long localID = getLocalChartID(beanHoroPersonalInfo.getOnlineChartId());

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
        sqLiteDatabase.update("tblHoroPersonalInfoMisc", argsHorary, "id" + "=" + localID, null);

        return beanHoroPersonalInfo.getLocalChartId();
    }

    //Return 40 kundlis in accending order name wise
    public ArrayList<BeanHoroPersonalInfo> loadAllLocalKundliPagination(int start, int end) {
        ArrayList<BeanHoroPersonalInfo> kundliArrayList = new ArrayList<BeanHoroPersonalInfo>();
        Cursor cursor = null;
        SQLiteDatabase sqLiteDatabase = null;
        try {
            sqLiteDatabase = this.getReadableDatabase();
            cursor = sqLiteDatabase.rawQuery("SELECT * FROM tblHoroPersonalInfo order by M_PName COLLATE NOCASE ASC Limit " + start + "," + end, null);
            if (cursor.getCount() > 0) {
                BeanHoroPersonalInfo beanHoroPersonalInfo;
                if (cursor.moveToFirst()) {
                    do {
                        beanHoroPersonalInfo = new BeanHoroPersonalInfo();
                        long id = cursor.getLong(cursor.getColumnIndex("id"));
                        beanHoroPersonalInfo.setLocalChartId(id);//ADDED BY BIJENDRA (17-NOV-2012)
                        beanHoroPersonalInfo.setName(cursor.getString(cursor.getColumnIndex("M_PName")));
                        beanHoroPersonalInfo.setGender(cursor.getString(cursor.getColumnIndex("M_Sex")));
                        BeanDateTime dTemp = new BeanDateTime();
                        dTemp.setDay(cursor.getInt(cursor.getColumnIndex("M_Day")));
                        dTemp.setMonth(cursor.getInt(cursor.getColumnIndex("M_Month")));
                        dTemp.setYear(cursor.getInt(cursor.getColumnIndex("M_Year")));
                        dTemp.setHour(cursor.getInt(cursor.getColumnIndex("M_Hr")));
                        dTemp.setMin(cursor.getInt(cursor.getColumnIndex("M_Min")));
                        dTemp.setSecond(cursor.getInt(cursor.getColumnIndex("M_Sec")));
                        beanHoroPersonalInfo.setDateTime(dTemp);
                        beanHoroPersonalInfo.setAyanIndex(cursor.getInt(cursor.getColumnIndex("M_Ayanamsa")));
                        String _cityname = cursor.getString(cursor.getColumnIndex("M_Place"));
                        _cityname = _cityname.replace('_', ' ');
                        BeanPlace objPlace = getCityByName(sqLiteDatabase, _cityname);
                        beanHoroPersonalInfo.setCityID(objPlace.getCityId());
                        objPlace.setCountryId(-1);
                        //objPlace.setCountryName("not define");
                        objPlace.setCountryName(cursor.getString(cursor.getColumnIndex("M_Country")));
                        objPlace.setState(cursor.getString(cursor.getColumnIndex("M_State")));
                        objPlace.setCityName(_cityname);
                        objPlace.setLongDeg(cursor.getString(cursor.getColumnIndex("M_LongDeg")));
                        objPlace.setLongMin(cursor.getString(cursor.getColumnIndex("M_LongMin")));
                        objPlace.setLongSec(cursor.getString(cursor.getColumnIndex("M_LongSec")));
                        objPlace.setLongDir(cursor.getString(cursor.getColumnIndex("M_LongEW")));
                        objPlace.setLatDeg(cursor.getString(cursor.getColumnIndex("M_LatDeg")));
                        objPlace.setLatMin(cursor.getString(cursor.getColumnIndex("M_LatMin")));
                        objPlace.setLatSec(cursor.getString(cursor.getColumnIndex("M_LatSec")));
                        objPlace.setLatDir(cursor.getString(cursor.getColumnIndex("M_LatNS")));
                        String _strTimezoneValue = cursor.getString(cursor.getColumnIndex("M_TimeZone"));
                        BeanPlace objTz = getTimZoneByNameUpdated(sqLiteDatabase, _strTimezoneValue);
                        if (objTz != null) {
                            objPlace.setTimeZoneName(objTz.getTimeZoneName());
                            objPlace.setTimeZoneValue(objTz.getTimeZoneValue());
                            objPlace.setTimeZoneId(objTz.getTimeZoneId());
                        }
                        beanHoroPersonalInfo.setPlace(objPlace);
                        beanHoroPersonalInfo.setDST(Integer.parseInt(cursor.getString(cursor.getColumnIndex("M_DST"))));
                        /*beanHoroPersonalInfo.setHoraryNumber(getHoraryNumber(sqLiteDatabase, cursor.getInt(cursor.getColumnIndex("id"))));*/
                        beanHoroPersonalInfo.setHoraryNumber(getHoraryNumber(sqLiteDatabase, id));
                        beanHoroPersonalInfo.setOnlineChartId(cursor.getString(cursor.getColumnIndex("onlinechartid")));
                        kundliArrayList.add(beanHoroPersonalInfo);

                    } while (cursor.moveToNext());
                }


            } else {

            }
        } catch (Exception e) {
            Log.e("exception>>>", e.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            sqLiteDatabase.close();
        }
        return kundliArrayList;

    }

    private long getLocalChartID(String onlineChartID) {
        Cursor cursor = null;
        SQLiteDatabase sqLiteDatabase = null;
        long id = -1;
        try {
            sqLiteDatabase = this.getReadableDatabase();

            cursor = sqLiteDatabase.rawQuery("SELECT * FROM tblHoroPersonalInfo where onlinechartid= " + onlineChartID, null);
            if (cursor.getCount() > 0) {
                BeanHoroPersonalInfo beanHoroPersonalInfo;
                if (cursor.moveToFirst()) {
                    id = cursor.getLong(cursor.getColumnIndex("id"));

                }
            }

        } catch (Exception e) {
            Log.e("exception>>>", e.getMessage());
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
            sqLiteDatabase.close();
        }
        return id;
    }

}

