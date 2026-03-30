package com.ojassoft.astrosage.varta.interfacefile;


import com.ojassoft.astrosage.varta.model.BeanPlace;

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

}