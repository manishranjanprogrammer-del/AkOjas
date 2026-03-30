package com.ojassoft.astrosage.jinterface;

import java.util.Map;

import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.beans.BeanPlace;

public interface IDatabaseHelperOperations {
    /**
     * This function return the array of city id and name
     *
     * @param sqLiteDatabase
     * @param cityName
     * @return String[][]
     * @throws Exception
     * @author Bijendra 31-may-13
     */
    String[][] searchCityOperation(String cityName) throws Exception;

    /**
     * This function is used to fatch city detail based on city id
     *
     * @param sqLiteDatabase
     * @param cityId
     * @return BeanPlace
     * @throws Exception
     */
    BeanPlace getCityByIdOperation(int cityId) throws Exception;

    /**
     * This function return Place object filled with time zone values(name/value/id)
     * according to passed time zone id that contain in time zone table
     *
     * @param timeZoneId
     * @return BeanPlace
     * @throws Exception
     */

    BeanPlace getTimZoneIdOperation(int timeZoneId) throws Exception;

    /**
     * This function return array of time zones from database
     * according to passed time zone name
     *
     * @param sqLiteDatabase
     * @param time           zone name
     * @return String array contain time zone id and name
     * @throws Exception
     */
    String[][] searchTimeZoneOperation(String searchText) throws Exception;

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
    Map<String, String> searchLocalKundliListOperation(String kundliName, int genderType, int noOfRecords) throws Exception;

    /**
     * This function is used to delete local kundli
     *
     * @param sqLiteDatabase
     * @param kundliId
     * @return boolean
     * @throws Exception
     */
    boolean deleteLocalKundliOperation(long kundliId) throws Exception;


    /**
     * This function is sued to get local kundli detail
     *
     * @param sqLiteDatabase
     * @param kundliId
     * @return BeanHoroPersonalInfo
     * @throws Exception
     */
    BeanHoroPersonalInfo getLocalKundliDetailOperation(long kundliId) throws Exception;


    /**
     * This function is used to add/edit kundli personal details
     *
     * @param sqLiteDatabase
     * @param beanHoroPersonalInfo
     * @return (long)kundli local id
     * @throws Exception
     */
    long addEditHoroPersonalInfoOperation(BeanHoroPersonalInfo beanHoroPersonalInfo) throws Exception;

    boolean deleteOnlineChartIdFromLocalDatabaseOperation(String onlineChartId);

}