package com.ojassoft.astrosage.jinterface;

import java.util.ArrayList;
import java.util.Map;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ojassoft.astrosage.beans.BeanDasa;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.beans.BeanKpCIL;
import com.ojassoft.astrosage.beans.BeanKpKhullarCIL;
import com.ojassoft.astrosage.beans.BeanKpPlanetSigWithStrenght;
import com.ojassoft.astrosage.beans.BeanNakshtraNadi;
import com.ojassoft.astrosage.beans.BeanOutMatchmakingNorth;
import com.ojassoft.astrosage.beans.BeanPlace;
import com.ojassoft.astrosage.beans.InKPPlanetsAndCusp;
import com.ojassoft.astrosage.beans.InKpPlanetSignification;
import com.ojassoft.astrosage.beans.OutKpHouseSignificators;
import com.ojassoft.astrosage.beans.OutKpMisc;
import com.ojassoft.astrosage.beans.OutKpRulingPlanets;
import com.ojassoft.astrosage.beans.PlanetData;
import com.ojassoft.astrosage.customexceptions.NoInternetException;
import com.ojassoft.astrosage.customexceptions.UICOnlineChartOperationException;
import com.ojassoft.astrosage.customexceptions.UICOnlineNorthMatchMakingOperationException;
import com.ojassoft.astrosage.customexceptions.UICTajikVarshaphalOperationException;
import com.ojassoft.astrosage.customexceptions.UIDasaVimsottriException;
import com.ojassoft.astrosage.customexceptions.UIDataOperationException;
import com.ojassoft.astrosage.customexceptions.UIKpSystemMiscException;
import com.ojassoft.astrosage.customexceptions.UIKundliMiscCalculationException;
import com.ojassoft.astrosage.model.CDatabaseHelper;
import com.ojassoft.kpextension.BeanKP4Step;

public interface IControllerManager {
    /**
     * This function is used to calculate kundli data
     *
     * @throws Exception
     */
    void calculateKundliData(BeanHoroPersonalInfo obj, boolean isOnline, boolean internetStatus) throws UICOnlineChartOperationException, Exception;

    /**
     * This function return Kp Cuspal Positions used to show on screen.
     *
     * @param obj
     * @return String[][]
     * @throws UIKpSystemMiscException
     */

    String[][] getKpCuspalPositions(com.ojassoft.astrosage.beans.InKPPlanetsAndCusp obj, String[] RasiLord, String[] NakLord, String DegSign, String MinSign, String SecSign) throws UIKpSystemMiscException;

    /**
     * This function return Kp Planetary Positions used to show on screen.
     *
     * @param obj
     * @return String[][]
     * @throws UIKpSystemMiscException
     */
    String[][] getKpPlanetaryPositions(InKPPlanetsAndCusp obj, String[] RasiLord, String[] NakLord, String DegSign, String MinSign, String SecSign) throws UIKpSystemMiscException;

    /**
     * This function put  KpPlanetSignificators into  array
     * for further operations.
     *
     * @param obj
     * @return String[]
     * @throws UIKpSystemMiscException
     */
    String[] getKpPlanetSignificators(InKpPlanetSignification obj) throws UIKpSystemMiscException;

    /**
     * This function return Kp House Significators used to show on screen.
     *
     * @param obj
     * @return OutKpHouseSignificators
     * @throws UIKpSystemMiscException
     */
    OutKpHouseSignificators getKpHouseSignificators(InKpPlanetSignification obj) throws UIKpSystemMiscException;


    BeanKpPlanetSigWithStrenght[] getPlanetSignWithStrengthBeansArray(InKPPlanetsAndCusp plntAndCusp)
            throws UIKpSystemMiscException;


    /**
     * This function return Kp Misc information used to show on screen.
     *
     * @param obj
     * @return OutKpMisc
     * @throws UIKpSystemMiscException
     */
    OutKpMisc getKpMisc(InKPPlanetsAndCusp obj, String[] RasiLordName, String[] NakLordName) throws UIKpSystemMiscException;

    BeanNakshtraNadi[] getNakshtraNadi(InKPPlanetsAndCusp plntAndCusp) throws UIKpSystemMiscException;

    /**
     * This function is used to return KCIL bean after array calculation
     *
     * @param inKPPlanetsAndCuspObject
     * @return array of BeanKpKCIL
     */
    BeanKpKhullarCIL[] getKCILBeansArray(InKPPlanetsAndCusp inKPPlanetsAndCuspObject) throws UIKpSystemMiscException;

    BeanKP4Step[] getBeanKp4StepBeansArrayNew(InKPPlanetsAndCusp inKPPlanetsAndCuspObject) throws UIKpSystemMiscException;

    /**
     * This function return the array of KP cuspal interlink based on cusp
     *
     * @param inKPPlanetsAndCuspObject
     * @return array of BeanKpCIL
     */
    BeanKpCIL[] getKPCILBeansArray(InKPPlanetsAndCusp inKPPlanetsAndCuspObject) throws UIKpSystemMiscException;

    /**
     * This function return ruling planets for KP system.
     *
     * @param obj
     * @return OutKpRulingPlanets
     * @throws UIKpSystemMiscException
     */
    OutKpRulingPlanets getKpRulingPlanets(InKPPlanetsAndCusp obj, String[] RasiLordFullName, String[] NakLordFullName) throws UIKpSystemMiscException;

    /**
     * This function return Kp planet degree array
     *
     * @param obj
     * @return double[]
     * @throws UIKpSystemMiscException
     */

    double[] getKPPlanetsDegreeArray(InKPPlanetsAndCusp obj) throws UIKpSystemMiscException;

    /**
     * This function return kp cusp array
     *
     * @param plntAndCusp
     * @return double[]
     */
    double[] getKpCuspArray(InKPPlanetsAndCusp plntAndCusp) throws UIKpSystemMiscException;

    /**
     * This function calculate KP Rashi kundli and return KundliPlanetArray
     * object.
     *
     * @return KundliPlanetArray
     * @throws UIKpSystemMiscException
     */

    int[] getKPLagnaRashiKundliPlanetsRashiArray(InKPPlanetsAndCusp obj) throws UIKpSystemMiscException;

    /**
     * This function return vimsottri Maha Dasha used to show on screen.
     *
     * @param _dob
     * @param _lastEndDate
     * @param obj
     * @return BeanDasa[]
     * @throws UIDasaVimsottriException
     */
    BeanDasa[] vimsottriMahaDasha(double _dob, double _lastEndDate, PlanetData obj) throws UIDasaVimsottriException;

    /**
     * This function return vimsottri Other Dasha Level used to show on screen.
     *
     * @param _dob
     * @param _lastEndDate
     * @param iPos
     * @param objArrPrDasa
     * @return BeanDasa[]
     */
    BeanDasa[] vimOtherLevelDasha(double _dob, double _lastEndDate, int iPos, BeanDasa[] objArrPrDasa) throws UIDasaVimsottriException;

    /**
     * This is other new function return vimsottri Other Dasha Level used to
     * show on screen.
     *
     * @param _dob
     * @param _lastEndDate
     * @param iPos
     * @param objArrPrDasa
     * @param _lastdashatime
     * @return BeanDasa[]
     * @throws UIDasaVimsottriException
     */

    BeanDasa[] getNewVimOtherLevelDasa(double _dob, double _lastEndDate, int iPos, BeanDasa[] objArrPrDasa, double _lastdashatime) throws UIDasaVimsottriException;

    /*
     * This function is used to return lalkitab kundli planets array
     * @param obj
     * @param yearNo
     * @return int []
     * @throws UIKundliMiscCalculationException
     */
    int[] getLalKitabKundliPlanetsRashiArray(PlanetData obj, int yearNo) throws UIKundliMiscCalculationException;

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
/**
 * This function expose the functionality to verify login .
 *
 * @param _uid
 * @param _pwd
 * @return int(status)
 * @throws UICOnlineChartOperationException
 */

 /*int verifyLogin(String _uid, String _pwd)
        throws UICOnlineChartOperationException;*/

    /**
     * This function expose the functionality to verify login .
     *
     * @param _uid
     * @param _pwd
     * @return integer[] value have login id and purchased plan id
     * @throws UICOnlineChartOperationException
     */
    //String[] verifyLoginWithUserPurchasedPlan(String _uid, String _pwd, String key) throws UICOnlineChartOperationException;

    /**
     * This function return string array of chart list
     * according to user id ,password and chart name.
     *
     * @param kundliName
     * @param pwd
     * @return 2 D string array
     * @throws UICOnlineChartOperationException
     */

    ArrayList<BeanHoroPersonalInfo> getOnlineChartList(Context context, String kundliName, String uid, String pwd, String key, String isapi) throws UICOnlineChartOperationException;

    /**
     * This function is used to delete kundli from AstroSage server
     *
     * @param kundliId
     * @param uid
     * @param pwd
     * @return
     * @throws UICOnlineChartOperationException
     */
    // int deleteOnlineKundli(long kundliId, String uid, String pwd) throws UICOnlineChartOperationException;

    /**
     * This function is used to to return online  kundli detail from AstroSage server
     *
     * @param kundliId
     * @param uid
     * @param pwd
     * @return BeanHoroPersonalInfo
     * @throws UICOnlineChartOperationException
     */
    //BeanHoroPersonalInfo getOnlineKundliDetail(SQLiteDatabase sqLiteDatabase, long kundliId, String uid, String pwd) throws UICOnlineChartOperationException;

    /**
     * This function return array of charts with in database according to
     * passed kundli name.
     *
     * @param sqLiteDatabase
     * @param kundliName
     * @param genderType
     * @return 2D  chart array contain chart id and name
     * @throws UIDataOperationException
     */
    Map<String, String> searchLocalKundliList(SQLiteDatabase sqLiteDatabase, String kundliName, int genderType, int noOfRecords) throws UIDataOperationException;


    /**
     * This function is used to delete local kundli
     *
     * @param sqLiteDatabase
     * @param kundliId
     * @return boolean
     * @throws UIDataOperationException
     */
    boolean deleteLocalKundli(SQLiteDatabase sqLiteDatabase, long kundliId) throws UIDataOperationException;

    /**
     * This function is used to get local kundli detail
     *
     * @param sqLiteDatabase
     * @param kundliId
     * @return BeanHoroPersonalInfo
     * @throws UIDataOperationException
     */
    BeanHoroPersonalInfo getLocalKundliDetail(SQLiteDatabase sqLiteDatabase, long kundliId) throws UIDataOperationException;

    /**
     * This function is sued to calculate tajik varshphal
     *
     * @param objBirthDetail
     * @param yearNumber
     * @return boolean
     * @throws UICTajikVarshaphalOperationException
     */

    //boolean calculateTajikVarshaphal(BeanHoroPersonalInfo objBirthDetail, String yearNumber) throws UICTajikVarshaphalOperationException;

    //BIJENDRA 24-12-13

    /**
     * This function is used to add/edit kundli personal details
     *
     * @param sqLiteDatabase
     * @param beanHoroPersonalInfo
     * @return (long)kundli local id
     * @throws UIDataOperationException
     */
    long addEditHoroPersonalInfo(SQLiteDatabase sqLiteDatabase, BeanHoroPersonalInfo beanHoroPersonalInfo) throws UIDataOperationException;


    /**
     * This function is used to save  kundli detail on AstroSage cloud
     *
     * @param beanHoroPersonalInfo
     * @param userId
     * @param pwd
     * @return int[][online kundli id,message id]
     * @throws UICOnlineChartOperationException
     */

    //long[] saveChartOnServer(BeanHoroPersonalInfo beanHoroPersonalInfo, String userId, String pwd) throws UICOnlineChartOperationException;

    //BeanOutMatchmakingNorth getMatchMakingResultNorth(BeanHoroPersonalInfo boy, BeanHoroPersonalInfo girl, int languageCode, boolean IS_APP_ONLINE, boolean internetStatus) throws UICOnlineNorthMatchMakingOperationException, NoInternetException;

    /**
     * This function share kundli name online
     *
     * @param userId
     * @param pwd
     * @param name
     * @param chartId
     * @return String[]
     * @throws UICOnlineChartOperationException
     */

    //String[] shareKundliNameOnline(String userId, String pwd, String name, String chartId) throws UICOnlineChartOperationException;

    /**
     * This function is used to check the available name to share kundli
     *
     * @param kundliName
     * @return String[]
     * @throws UICOnlineChartOperationException
     */
    //String[] checkAvailableKundliNameToShare(String kundliName) throws UICOnlineChartOperationException;


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
    String[][] searchCityOperation(Context context, String cityName) throws UIDataOperationException;

    BeanPlace getCityByIdOperation(Context context, int cityId) throws UIDataOperationException;

    BeanPlace getTimZoneIdOperation(Context context, int timeZoneId) throws UIDataOperationException;

    String[][] searchTimeZoneOperation(Context context, String searchText) throws UIDataOperationException;

    Map<String, String> searchLocalKundliListOperation(Context context, String kundliName, int genderType, int noOfRecords) throws UIDataOperationException;

    ArrayList<BeanHoroPersonalInfo> searchLocalKundliListOperation(Context context, int genderType, int noOfRecords) throws UIDataOperationException;

    boolean deleteLocalKundliOperation(Context context, long kundliId) throws UIDataOperationException;

    boolean deleteLocalKundliOperation(Context context, long kundliId, long onlineChartId) throws UIDataOperationException;

    BeanHoroPersonalInfo getLocalKundliDetailOperation(Context context, long kundliId) throws UIDataOperationException;

    long addEditHoroPersonalInfoOperation(Context context, BeanHoroPersonalInfo beanHoroPersonalInfo) throws UIDataOperationException;

    boolean deleteOnlineChartIdFromLocalDatabaseOperation(Context context, String onlineChartId);

    //BeanHoroPersonalInfo getOnlineKundliDetailNew(Context context, long kundliId, String uid, String pwd) throws UICOnlineChartOperationException;

    /**
     * This function is used to register the user on servers
     *
     * @param _uid,_pwd 18-Jan-2016
     */
    //String[] userSignUp(String _uid, String _pwd, String key) throws UICOnlineChartOperationException;

    /**
     * this function is used to register user with email only
     */

    //String[] userSignUp(String _uid, String key) throws UICOnlineChartOperationException;

    /**
     * this function is used to update Default Kundli On server
     *
     * @return
     */
   /* String UpdateDefaultKundliOnServer(String kundaliId, String userId,
                                       String password);*/

    //String changePasswordAndProfile(String emailId, String newPassword, String oldPassword, String userName, String key);

    //String saveNotesOnserver(Context context, String userid, String password, String usercomment, String onlinechartid);

    void calculateTransitKundliData(BeanHoroPersonalInfo beanHoroPersonalInfo, boolean is_app_online, boolean connectedWithInternet);

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

