package com.ojassoft.astrosage.jinterface;

import java.util.ArrayList;
import java.util.Map;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;

public interface IOnlineChartOperations {
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
    //public abstract String[] verifyLoginWithUserPurchasedPlan(String _uid, String _pwd, String key) throws Exception;

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

    /**
     * This function is used to register the user on servers
     *
     * @param _uid,_pwd 18-Jan-2016
     */
    //String[] userSignUp(String _uid, String _pwd, String key) throws Exception;

    //String syncChartOnServer(Context context, String userName, String password, String jsonString);

}
