package com.ojassoft.astrosage.jinterface;

import java.util.ArrayList;
import java.util.Map;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.beans.BeanOutMatchmakingNorth;
import com.ojassoft.astrosage.beans.BeanPlace;
import com.ojassoft.astrosage.model.CDatabaseHelper;

public interface IModelManager {
    /**
     * This function is used to calculate kundli data
     *
     * @throws Exception
     */
    void calculateKundliData(BeanHoroPersonalInfo obj, boolean isOnline, boolean isInternetAvailble)
            throws Exception;

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

    /**
     * This function expose the functionality to verify login .
     *
     * @param _uid
     * @param _pwd
     * @return int(status)
     * @throws Exception
     */

    //int verifyLogin(String _uid, String _pwd) throws Exception;

    /**
     * This function is used to check/verify user login
     *
     * @param _uid
     * @param _pwd
     * @return integer[] value have login id and purchased plan id
     * @throws Exception
     */
    //String[] verifyLoginWithUserPurchasedPlan(String _uid, String _pwd, String key) throws Exception;

    /**
     * This function return string array of chart list
     * according to user id ,password and chart name.
     *
     * @param kundliName
     * @param pwd
     * @return 2 D string array
     * @throws Exception
     */

    ArrayList<BeanHoroPersonalInfo> getOnlineChartList(Context context, String kundliName, String uid, String pwd, String key, String isapi) throws Exception;

    /**
     * This function is used to delete kundli from AstroSage server
     *
     * @param kundliId
     * @param uid
     * @param pwd
     * @return
     * @throws Exception
     */
    //int deleteOnlineKundli(long kundliId, String uid, String pwd) throws Exception;

    /**
     * This function is used to to return online  kundli detail from AstroSage server
     *
     * @param kundliId
     * @param uid
     * @param pwd
     * @return BeanHoroPersonalInfo
     * @throws Exception
     */
    //BeanHoroPersonalInfo getOnlineKundliDetail(SQLiteDatabase sqLiteDatabase, long kundliId, String uid, String pwd) throws Exception;


    /**
     * This function return array of charts with in database according to
     * passed kundli name.
     *
     * @param sqLiteDatabase
     * @param kundliName
     * @param genderType
     * @return 2D  chart array contain chart id and name
     * @throws Exception
     */
    Map<String, String> searchLocalKundliList(SQLiteDatabase sqLiteDatabase, String kundliName, int genderType, int noOfRecords) throws Exception;

    /**
     * This function is used to delete local kundli
     *
     * @param sqLiteDatabase
     * @param kundliId
     * @return boolean
     * @throws Exception
     */
    boolean deleteLocalKundli(SQLiteDatabase sqLiteDatabase, long kundliId) throws Exception;

    /**
     * This function is sued to get local kundli detail
     *
     * @param sqLiteDatabase
     * @param kundliId
     * @return BeanHoroPersonalInfo
     * @throws Exception
     */
    BeanHoroPersonalInfo getLocalKundliDetail(SQLiteDatabase sqLiteDatabase, long kundliId) throws Exception;

    /**
     * This function is sued to calculate tajik varshphal
     *
     * @param objBirthDetail
     * @param yearNumber
     * @return boolean
     * @throws Exception
     */

    //boolean calculateTajikVarshaphal(BeanHoroPersonalInfo objBirthDetail, String yearNumber) throws Exception;


    //BIJENDRA 24-12-13

    /**
     * This function is used to add/edit kundli personal details
     *
     * @param sqLiteDatabase
     * @param beanHoroPersonalInfo
     * @return (long)kundli local id
     * @throws Exception
     */
    long addEditHoroPersonalInfo(SQLiteDatabase sqLiteDatabase, BeanHoroPersonalInfo beanHoroPersonalInfo) throws Exception;

    /**
     * This function is used to save  kundli detail on AstroSage cloud
     *
     * @param beanHoroPersonalInfo
     * @throws Exception
     * @returnint[][online kundli id,message id]
     */
    //long[] saveChartOnServer(BeanHoroPersonalInfo beanHoroPersonalInfo, String userId, String pwd) throws Exception;

    //MATCHING FUNCTION STARTS
    /*BeanOutMatchmakingNorth getMatchMakingResultNorth(
            BeanHoroPersonalInfo boy, BeanHoroPersonalInfo girl, int languageCode, boolean iS_APP_ONLINE, boolean internetStatus) throws Exception;
*/
    //END MATCHINHG FUNCTIONS

    /**
     * This function share kundli name online
     *
     * @param userId
     * @param pwd
     * @param name
     * @param chartId
     * @return String[]
     * @throws Exception
     */

    //String[] shareKundliNameOnline(String userId, String pwd, String name, String chartId) throws Exception;


    /**
     * This function is used to check the available name to share kundli
     *
     * @param kundliName
     * @return String[]
     * @throws Exception
     */

    //String[] checkAvailableKundliNameToShare(String kundliName) throws Exception;

    /**
     * This function is used to save user  user GCM  registration information on ojas  server
     *
     * @param context
     * @param regid
     * @param languageCode
     * @param loginId      26-Dec-2014
     */
    void saveUserGCMRegistrationInformationOnOjasServer(Context context, String regid, int languageCode, String loginId);

    //NEW DATABASE OPERATIONS ADDED BY BIJENDRA ON 20-08-15
    String[][] searchCityOperation(Context context, String cityName) throws Exception;

    BeanPlace getCityByIdOperation(Context context, int cityId) throws Exception;

    BeanPlace getTimZoneIdOperation(Context context, int timeZoneId) throws Exception;

    String[][] searchTimeZoneOperation(Context context, String searchText) throws Exception;

    Map<String, String> searchLocalKundliListOperation(Context context, String kundliName, int genderType, int noOfRecords) throws Exception;

    ArrayList<BeanHoroPersonalInfo> searchLocalKundliListOperation(Context context, int startIndex, int endIndex) throws Exception;

    boolean deleteLocalKundliOperation(Context context, long kundliId) throws Exception;

    boolean deleteLocalKundliOperation(Context context, long kundliId, long onlineChartId) throws Exception;

    BeanHoroPersonalInfo getLocalKundliDetailOperation(Context context, long kundliId) throws Exception;

    long addEditHoroPersonalInfoOperation(Context context, BeanHoroPersonalInfo beanHoroPersonalInfo) throws Exception;

    boolean deleteOnlineChartIdFromLocalDatabaseOperation(Context context, String onlineChartId);

    //BeanHoroPersonalInfo getOnlineKundliDetailNew(Context context, long kundliId, String uid, String pwd) throws Exception;

    /**
     * this function is used to register user with emailid only
     *
     * @param _uid
     * @param key
     * @return
     * @throws Exception
     */
    //String[] userSignUp(String _uid, String key) throws Exception;

    /**
     * This function is used to register the user on servers
     *
     * @param _uid,_pwd 18-Jan-2016
     */
/*
    String[] userSignUp(String _uid, String _pwd, String key)
            throws Exception;*/

    /**
     * this function is used to update Default Kundli On server
     * @return
     */
    /*String UpdateDefaultKundliOnServer(String kundaliId, String userId,
                                       String password);*/

    //String changePasswordAndProfile(String emailId, String newPassword, String oldPassword, String userName, String key);

    //String saveNotesOnserver(Context context, String userid, String password, String usercomment, String onlinechartid);

    //String syncChartOnServer(Context context, String userName, String password, String jsonString);

    /**
     * This function is used to extract chart id after saving on
     * chart on astrosage server
     *
     * @param chartSavedId
     * @return chart id
     */
    long[] getSaveOnlineChartId(String chartSavedId, String updatedOnlineChartId) throws Exception;

}
