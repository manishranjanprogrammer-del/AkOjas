package com.libojassoft.android.misc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.libojassoft.android.utils.LibCGlobalVariables;
import com.libojassoft.android.utils.LibCUtils;

public class CFileOperations {

    // public int CYCLE_INTERVAL=0;//0 for first interval/1 for second interval
    public void checkSettingDirecoryExists() {

        if (!LibCUtils.isExternalStorageAvailableToWrite())//ADDED BY BIJENDRA ON 09-MAY-2013
            return;
        File mySettingFolder = new File(
                LibCGlobalVariables.SETTING_DIRECTORY_NAME);
        if (!mySettingFolder.exists()) {
            mySettingFolder.mkdir();
        }
    }

    public void saveMagazineNotificationJSONObject(Context context, String todayDate,
                                                   String blogId, int firstFlag, int secondFlag) throws IOException {

		/*
		String strToSave = "";
		if(!LibCUtils.isExternalStorageAvailableToWrite())//ADDED BY BIJENDRA ON 09-MAY-2013
			return ;
		FileOutputStream fOut = null;
		OutputStreamWriter myOutWriter = null;
		
		try {
			strToSave = getJsonMagazineObjectStringToSave(todayDate, blogId,
					firstFlag, secondFlag);
			checkSettingDirecoryExists();
			File magzFile = new File(
					LibCGlobalVariables.SETTING_DIRECTORY_NAME,
					LibCGlobalVariables.MAGAZINE_NOTIFICATION_FILE_NAME);
			if (!magzFile.exists()) {
				// Log.d("BIJENDRA_FILE", "Not have");
				magzFile.createNewFile();
			}
			*//*
         * else Log.d("BIJENDRA_FILE", " have");
         *//*

			 fOut = new FileOutputStream(magzFile);
			 myOutWriter = new OutputStreamWriter(fOut);
			myOutWriter.write(strToSave);
			
			// Log.d("SERVICE_WRITE", "Bijendra");
		} catch (Exception e) {
			Log.d("NotificationId_error", e.getMessage());

		} finally {
			myOutWriter.close();
			fOut.close();
		}*/

        String strToSave = getJsonMagazineObjectStringToSave(todayDate, blogId,
                firstFlag, secondFlag);
        SharedPreferences sharedPreferences = context.getSharedPreferences(LibCGlobalVariables.NOTIFICATION_PREF_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LibCGlobalVariables.EN_MAGAZINE_NOTIFICATION_PREF_KEY, strToSave);
        editor.commit();

    }

    public void saveMagazineNotificationJSONObjectHindi(Context context, String todayDate,
                                                        String blogId, int firstFlag, int secondFlag) throws IOException {
		/*String strToSave = "";
		
		if(!LibCUtils.isExternalStorageAvailableToWrite())//ADDED BY BIJENDRA ON 09-MAY-2013
			return ;
		FileOutputStream fOut = null;
		OutputStreamWriter myOutWriter = null;
		try {
			strToSave = getJsonMagazineObjectStringToSaveHindi(todayDate, blogId,
					firstFlag, secondFlag);
			checkSettingDirecoryExists();
			File magzFile = new File(
					LibCGlobalVariables.SETTING_DIRECTORY_NAME,
					LibCGlobalVariables.MAGAZINE_NOTIFICATION_FILE_NAME_HINDI);
			if (!magzFile.exists()) {
				// Log.d("BIJENDRA_FILE", "Not have");
				magzFile.createNewFile();
			}
			*//*
         * else Log.d("BIJENDRA_FILE", " have");
         *//*

			 fOut = new FileOutputStream(magzFile);
			 myOutWriter = new OutputStreamWriter(fOut);
			 myOutWriter.write(strToSave);
			
			// Log.d("SERVICE_WRITE", "Bijendra");
		} catch (Exception e) {
			//String s = e.getMessage();
			//Log.d("BIJENDRA_setMagazineNotificationId_error", s);
			e.printStackTrace();
		}finally {
			myOutWriter.close();
			fOut.close();
		}*/
        String strToSave = getJsonMagazineObjectStringToSave(todayDate, blogId,
                firstFlag, secondFlag);
        SharedPreferences sharedPreferences = context.getSharedPreferences(LibCGlobalVariables.NOTIFICATION_PREF_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LibCGlobalVariables.HI_MAGAZINE_NOTIFICATION_PREF_KEY, strToSave);
        editor.commit();

    }

    /**
     * This function prepare JSONObject according to passed parameters and
     * return String of that object
     *
     * @param todayDate
     * @param blogId
     * @param firstFlag
     * @param secondFlag
     * @return
     */
    private String getJsonMagazineObjectStringToSave(String todayDate,
                                                     String blogId, int firstFlag, int secondFlag) {
        String retVal = "";
        try {
            JSONObject savedObj = new JSONObject();
            savedObj.put(LibCGlobalVariables.JSON_MAGAZINE_TODAY,
                    LibCGlobalVariables.TODAY_DATE_ID);
            savedObj.put(LibCGlobalVariables.JSON_MAGAZINE_BOLG_ID, blogId);
            savedObj.put(LibCGlobalVariables.JSON_MAGZINE_FIRST_CYCLE_FLAG,
                    LibCGlobalVariables.MAGZINE_FIRST_CYCLE_FLAG);
            savedObj.put(LibCGlobalVariables.JSON_MAGZINE_SECOND_CYCLE_FLAG,
                    LibCGlobalVariables.MAGZINE_SECOND_CYCLE_FLAG);

            return savedObj.toString();

        } catch (Exception e) {

        }

        return retVal;
    }

    private String getJsonMagazineObjectStringToSaveHindi(String todayDate,
                                                          String blogId, int firstFlag, int secondFlag) {
        String retVal = "";
        try {
            JSONObject savedObj = new JSONObject();
            savedObj.put(LibCGlobalVariables.JSON_MAGAZINE_TODAY,
                    LibCGlobalVariables.TODAY_DATE_ID);
            savedObj.put(LibCGlobalVariables.JSON_MAGAZINE_BOLG_ID, blogId);
            savedObj.put(LibCGlobalVariables.JSON_MAGZINE_FIRST_CYCLE_FLAG,
                    LibCGlobalVariables.MAGZINE_FIRST_CYCLE_FLAG);
            savedObj.put(LibCGlobalVariables.JSON_MAGZINE_SECOND_CYCLE_FLAG,
                    LibCGlobalVariables.MAGZINE_SECOND_CYCLE_FLAG);

            JSONObject mainObject = new JSONObject();//Added By Hukum
            mainObject.put(LibCGlobalVariables.HINDI_BLOG_SETTING_JSON_OBJECT_NAME, savedObj);
            return mainObject.toString();

        } catch (Exception e) {

        }

        return retVal;
    }

}
