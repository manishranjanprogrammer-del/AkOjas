package com.ojassoft.astrosage.varta.interfacefile;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ojassoft.astrosage.varta.model.BeanPlace;
import com.ojassoft.astrosage.varta.utils.CDatabaseHelper;


public interface IModelManager {

    /**
     * This function is used to return Database Helper Object
     *
     * @param context
     * @return CDatabaseHelper
     * @throws Exception
     */
    CDatabaseHelper getDatabaseHelperObject(Context context) throws Exception;

    /**
     * This function return the array of city id and name
     *
     * @param sqLiteDatabase
     * @param cityName
     * @return String[][]
     * @throws Exception
     * @author Bijendra 31-may-13
     */
    String[][] searchCity(SQLiteDatabase sqLiteDatabase, String cityName)
            throws Exception;

    /**
     * This function is used to fatch city detail based on city id
     *
     * @param sqLiteDatabase
     * @param cityId
     * @return BeanPlace
     * @throws Exception
     */
    BeanPlace getCityById(SQLiteDatabase sqLiteDatabase, int cityId)
            throws Exception;

    /**
     * This function return Place object filled with time zone
     * values(name/value/id) according to passed time zone id that contain in
     * time zone table
     *
     * @param timeZoneId
     * @return BeanPlace
     * @throws Exception
     */

    BeanPlace getTimZoneId(SQLiteDatabase sqLiteDatabase, int timeZoneId)
            throws Exception;

    /**
     * This function return array of time zones from database according to
     * passed time zone name
     *
     * @param sqLiteDatabase
     * @return String array contain time zone id and name
     * @throws Exception
     */
    String[][] searchTimeZone(SQLiteDatabase sqLiteDatabase, String searchText)
            throws Exception;

    //NEW DATABASE OPERATIONS ADDED BY BIJENDRA ON 20-08-15
    String[][] searchCityOperation(Context context, String cityName) throws Exception;

    BeanPlace getCityByIdOperation(Context context, int cityId) throws Exception;

    BeanPlace getTimZoneIdOperation(Context context, int timeZoneId) throws Exception;

    String[][] searchTimeZoneOperation(Context context, String searchText) throws Exception;

    void saveUserGCMRegistrationInformationOnOjasServer(Context context,
                                                        String regid, int languageCode, String loginId);
}
