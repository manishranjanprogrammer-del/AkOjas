package com.ojassoft.astrosage.varta.interfacefile;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ojassoft.astrosage.varta.model.BeanPlace;
import com.ojassoft.astrosage.varta.utils.CDatabaseHelper;
import com.ojassoft.astrosage.varta.utils.UIDataOperationException;

public interface IControllerManager {

    /**
     * This function is sued to create or init database
     *
     * @param context
     * @return SQLiteDatabase
     * @throws UIDataOperationException
     */
    CDatabaseHelper getDatabaseHelperObject(Context context) throws UIDataOperationException;

    /**
     * This function return the array of city id and name
     *
     * @param sqLiteDatabase
     * @param cityName
     * @return String[][]
     * @throws UIDataOperationException
     * @author Bijendra 31-may-13
     */
    String[][] searchCity(SQLiteDatabase sqLiteDatabase, String cityName) throws UIDataOperationException;

    /**
     * This function is used to fatch city detail based on city id
     *
     * @param sqLiteDatabase
     * @param cityId
     * @return BeanPlace
     * @throws UIDataOperationException
     */
    BeanPlace getCityById(SQLiteDatabase sqLiteDatabase, int cityId) throws UIDataOperationException;

    /**
     * This function return Place object filled with time zone values(name/value/id)
     * according to passed time zone id that contain in time zone table
     *
     * @param timeZoneId
     * @return BeanPlace
     * @throws UIDataOperationException
     */

    BeanPlace getTimZoneId(SQLiteDatabase sqLiteDatabase, int timeZoneId) throws UIDataOperationException;

    /**
     * This function return array of time zones from database
     * according to passed time zone name
     *
     * @param sqLiteDatabase
     * @return String array contain time zone id and name
     * @throws UIDataOperationException
     */
    String[][] searchTimeZone(SQLiteDatabase sqLiteDatabase, String searchText) throws UIDataOperationException;

    //NEW DATABASE OPERATIONS ADDED BY BIJENDRA ON 20-08-15
    String[][] searchCityOperation(Context context, String cityName) throws UIDataOperationException;

    BeanPlace getCityByIdOperation(Context context, int cityId) throws UIDataOperationException;

    BeanPlace getTimZoneIdOperation(Context context, int timeZoneId) throws UIDataOperationException;

    String[][] searchTimeZoneOperation(Context context, String searchText) throws UIDataOperationException;

    void saveUserGCMRegistrationInformationOnOjasServer(Context context,
                                                        String regid, int languageCode, String loginId);
}

