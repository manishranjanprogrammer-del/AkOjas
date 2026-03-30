package com.ojassoft.astrosage.varta.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ojassoft.astrosage.model.CGCMRegistrationInfoSaveOnOjas;
import com.ojassoft.astrosage.varta.interfacefile.IModelManager;
import com.ojassoft.astrosage.varta.model.BeanPlace;


public class ModelManager implements IModelManager {


    Context context;

    /**
     * This function is used to return  Database Helper Object
     * @param context
     * @return CDatabaseHelper
     * @throws Exception
     */
    public CDatabaseHelper getDatabaseHelperObject(Context context)
            throws Exception {
        try {
            return (new CDatabaseHelper(context));
        } catch (Exception e) {
            throw e;
        }
    }

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

        return new CDataOperations().searchCity(sqLiteDatabase, cityName);
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
        // TODO Auto-generated method stub
        return new CDataOperations().getCityById(sqLiteDatabase, cityId);
    }

    @Override
    public BeanPlace getTimZoneId(SQLiteDatabase sqLiteDatabase, int timeZoneId)
            throws Exception {
        return new CDataOperations().getTimZoneId(sqLiteDatabase, timeZoneId);
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
        return new CDataOperations().searchTimeZone(sqLiteDatabase, searchText);
    }

    /**
     * This function expose the functionality to
     * verify login .
     *
     * @return int(status)
     * @throws Exception
     */




    @Override
    public String[][] searchCityOperation(Context context, String cityName)
            throws Exception {
        // TODO Auto-generated method stub
        return new CDatabaseHelperOperations(context).searchCityOperation(cityName);
    }

    @Override
    public BeanPlace getCityByIdOperation(Context context, int cityId)
            throws Exception {
        // TODO Auto-generated method stub
        return new CDatabaseHelperOperations(context).getCityByIdOperation(cityId);

    }

    @Override
    public BeanPlace getTimZoneIdOperation(Context context, int timeZoneId)
            throws Exception {
        // TODO Auto-generated method stub
        return new CDatabaseHelperOperations(context).getTimZoneIdOperation(timeZoneId);
    }

    @Override
    public String[][] searchTimeZoneOperation(Context context, String searchText)
            throws Exception {
        // TODO Auto-generated method stub
        return new CDatabaseHelperOperations(context).searchTimeZoneOperation(searchText);
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

        new CGCMRegistrationInfoSaveOnOjas().saveUserGCMRegistrationInformationOnOjasServer(context,
                regid, languageCode, loginId);

    }
}