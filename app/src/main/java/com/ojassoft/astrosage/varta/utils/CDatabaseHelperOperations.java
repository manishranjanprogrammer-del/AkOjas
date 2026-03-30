package com.ojassoft.astrosage.varta.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.interfacefile.IDatabaseHelperOperations;
import com.ojassoft.astrosage.varta.model.BeanPlace;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

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

    public void deleteAllRecords() {
        SQLiteDatabase sqLiteDatabase = null;
        try {
            sqLiteDatabase = this.getReadableDatabase();
            sqLiteDatabase.execSQL("delete from " + "tblHoroPersonalInfo");
        } catch (Exception e) {
            //Log.e("Exception", e.getMessage());
        } finally {
            sqLiteDatabase.close(); // Closing database connection
        }

    }
}

