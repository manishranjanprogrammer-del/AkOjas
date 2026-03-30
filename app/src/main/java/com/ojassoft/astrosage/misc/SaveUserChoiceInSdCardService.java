package com.ojassoft.astrosage.misc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import com.libojassoft.android.utils.LibCGlobalVariables;
import com.libojassoft.android.utils.LibCUtils;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;

public class SaveUserChoiceInSdCardService extends Service {
    //String strToSave = "";
    @Override
    public IBinder onBind(Intent intent) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // TODO Auto-generated method stub
        String strToSave = "";
        if (intent != null) {
            strToSave = intent.getExtras().getString("USER_CHOICE");
        }

        try {
            if (strToSave.length() > 0)
                saveUserChoiceInSdCard(strToSave);
        } catch (Exception e) {

        } finally {
            SaveUserChoiceInSdCardService.this.stopSelf();
        }
        return START_STICKY;
    }

    private void  saveUserChoiceInSdCard(String strToSave) throws IOException {

       /* FileOutputStream fOut = null;
        OutputStreamWriter myOutWriter = null;

        if (!LibCUtils.isExternalStorageAvailableToWrite())
            return;


        File magzFile = new File(LibCGlobalVariables.SETTING_DIRECTORY_NAME, LibCGlobalVariables.MAGAZINE_NOTIFICATION_CONFIG_FILE_NAME);
        if (magzFile.exists()) {
            try {
                fOut = new FileOutputStream(magzFile);
                myOutWriter = new OutputStreamWriter(fOut);
                myOutWriter.write(strToSave);

            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } finally {
                myOutWriter.close();
                fOut.close();
            }
        }*/

        SharedPreferences sharedPreferences = getSharedPreferences(LibCGlobalVariables.NOTIFICATION_PREF_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LibCGlobalVariables.NOTIFICATION_CHOICE_PREF_KEY, strToSave);
        editor.commit();
    }

}
