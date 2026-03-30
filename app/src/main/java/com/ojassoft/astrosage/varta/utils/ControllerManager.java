package com.ojassoft.astrosage.varta.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import com.ojassoft.astrosage.varta.interfacefile.IControllerManager;
import com.ojassoft.astrosage.varta.model.BeanPlace;


public class ControllerManager implements IControllerManager {
    Context context;
    //CalculateKundli calculateKundli;
   // TransitFragment transitFragment;

    public ControllerManager() {

    }

    /**
     * This function is sued to create or init database
     *
     * @param context
     * @return SQLiteDatabase
     * @throws Exception
     */
    public CDatabaseHelper getDatabaseHelperObject(Context context)
            throws UIDataOperationException {
        try {
            return (new ModelManager().getDatabaseHelperObject(context));
        } catch (Exception e) {
            throw (new UIDataOperationException(
                    "Unable to create database .", e));
        }

    }

    /**
     * This function return the array of city id and name
     *
     * @param sqLiteDatabase
     * @param cityName
     * @return String[][]
     * @throws UIDataOperationException
     * @author Bijendra 31-may-13
     */
    @Override
    public String[][] searchCity(SQLiteDatabase sqLiteDatabase, String cityName)
            throws UIDataOperationException {
        try {

            return new ModelManager().searchCity(sqLiteDatabase, cityName);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            throw (new UIDataOperationException("City  not found", e));
        }
    }

    /**
     * This function is used to fatch city detail based on city id
     *
     * @param sqLiteDatabase
     * @param cityId
     * @return BeanPlace
     * @throws UIDataOperationException
     */
    @Override
    public BeanPlace getCityById(SQLiteDatabase sqLiteDatabase, int cityId)
            throws UIDataOperationException {
        try {
            return new ModelManager().getCityById(sqLiteDatabase, cityId);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            throw (new UIDataOperationException("City not found", e));
        }
    }

    /**
     * This function return Place object filled with time zone values(name/value/id)
     * according to passed time zone id that contain in time zone table
     *
     * @return BeanPlace
     * @throws UIDataOperationException
     */
    @Override
    public BeanPlace getTimZoneId(SQLiteDatabase sqLiteDatabase, int getTimZoneId)
            throws UIDataOperationException {
        try {
            return new ModelManager().getTimZoneId(sqLiteDatabase, getTimZoneId);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            throw (new UIDataOperationException("Time zone not found", e));
        }
    }

    /**
     * This function return array of time zones from database
     * according to passed time zone name
     *
     * @param sqLiteDatabase
     * @return String array contain time zone id and name
     * @throws UIDataOperationException
     */
    @Override
    public String[][] searchTimeZone(SQLiteDatabase sqLiteDatabase,
                                     String searchText) throws UIDataOperationException {
        try {
            return (new ModelManager().searchTimeZone(sqLiteDatabase, searchText));
        } catch (Exception e) {
            //Log.d("Database-operations", "Time Zone list not found");
            throw (new UIDataOperationException("Time Zone list not found", e));

        }
    }

    @Override
    public String[][] searchCityOperation(Context context, String cityName)
            throws UIDataOperationException {
        // TODO Auto-generated method stub
        try {

            return new ModelManager().searchCityOperation(context, cityName);

        } catch (Exception e) {
            // TODO Auto-generated catch block
            throw (new UIDataOperationException("City  not found", e));
        }
    }


    @Override
    public BeanPlace getCityByIdOperation(Context context, int cityId)
            throws UIDataOperationException {
        // TODO Auto-generated method stub
        try {
            return new ModelManager().getCityByIdOperation(context, cityId);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            throw (new UIDataOperationException("City not found", e));
        }
    }


    @Override
    public BeanPlace getTimZoneIdOperation(Context context, int timeZoneId)
            throws UIDataOperationException {
        // TODO Auto-generated method stub
        try {
            return new ModelManager().getTimZoneIdOperation(context, timeZoneId);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            throw (new UIDataOperationException("Time zone not found", e));
        }
    }


    @Override
    public String[][] searchTimeZoneOperation(Context context, String searchText)
            throws UIDataOperationException {
        // TODO Auto-generated method stub
        try {
            return (new ModelManager().searchTimeZoneOperation(context, searchText));
        } catch (Exception e) {
            //Log.d("Database-operations", "Time Zone list not found");
            throw (new UIDataOperationException("Time Zone list not found", e));

        }
    }

    /**
     * This function is used to save user  user GCM  registration information on ojas  server
     *
     * @param context
     * @param regid
     * @param languageCode
     * @param loginId      26-Dec-2014
     */
    @Override
    public void saveUserGCMRegistrationInformationOnOjasServer(Context context,
                                                               String regid, int languageCode, String loginId) {
        // TODO Auto-generated method stub

        new ModelManager().saveUserGCMRegistrationInformationOnOjasServer(context,
                regid, languageCode, loginId);

    }

}