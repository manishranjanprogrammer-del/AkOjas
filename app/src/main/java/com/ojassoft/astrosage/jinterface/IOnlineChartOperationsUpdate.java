package com.ojassoft.astrosage.jinterface;

import java.util.Map;

import android.content.Context;

import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;

public interface IOnlineChartOperationsUpdate {
    /**
     * This function is used to check/verify user login
     * @param _uid
     * @param  _pwd
     * @return integer value
     * @throws Exception
     */
    //public abstract int verifyLogin(String _uid, String _pwd) throws Exception;

    /**
     * This function is used to check/verify user login
     *
     * @param _uid
     * @param _pwd
     * @return integer[] value have login id and purchased plan id
     * @throws Exception
     */
    //public abstract int[] verifyLoginWithUserPurchasedPlan(String _uid, String _pwd) throws Exception;

    /**
     * This function return string array of chart list
     * according to user id ,password and chart name.
     *
     * @param kundliName
     * @param pwd
     * @return 2 D string array
     * @throws Exception
     */

    //Map<String, String> getOnlineChartList(String kundliName, String uid, String pwd) throws Exception;


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
    //BeanHoroPersonalInfo getOnlineKundliDetail(Context context, long kundliId, String uid, String pwd) throws Exception;

    /**
     * This function is used to save  kundli detail on AstroSage cloud
     *
     * @param beanHoroPersonalInfo
     * @param userId
     * @param pwd
     * @return int[][online kundli idmessage id]
     * @throws Exception
     */


    //long[] saveChartOnServer(BeanHoroPersonalInfo beanHoroPersonalInfo, String userId, String pwd) throws Exception;


    /**
     * This function share kundli name online
     *
     * @param userId
     * @param pwd
     * @param name
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
}
