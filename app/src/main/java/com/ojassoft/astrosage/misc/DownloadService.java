package com.ojassoft.astrosage.misc;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.os.ResultReceiver;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

import static com.ojassoft.astrosage.utils.CGlobalVariables.FOLDER_ASTROSAGE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_FNAME;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_NORTH_CHART;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_PDF_SHARE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_PROGRESS;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_RECEIVER;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_URL;
import static com.ojassoft.astrosage.utils.CGlobalVariables.PDF_NAME_KUNDALI;
import static com.ojassoft.astrosage.utils.CGlobalVariables.PDF_NAME_MATCHING;
import static com.ojassoft.astrosage.utils.CGlobalVariables.PDF_NAME_PREMIUM_KUNDALI;
import static com.ojassoft.astrosage.utils.CGlobalVariables.PROGRESS_COMPLETE;

import com.ojassoft.astrosage.utils.CUtils;

public class DownloadService extends IntentService {
    public static final int UPDATE_PROGRESS = 8344;
    ResultReceiver receiver;
    String fname = PDF_NAME_KUNDALI;
    boolean isPdfShare;
    int progress = 0;

    public DownloadService() {
        super("DownloadService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        isPdfShare = intent.getBooleanExtra(KEY_PDF_SHARE, true);
        receiver = intent.getParcelableExtra(KEY_RECEIVER);
        String urlToDownload = intent.getStringExtra(KEY_URL);
        //Log.e("urlToDownload Matching", urlToDownload);
        try {
            URL url = new URL(urlToDownload);
            URLConnection connection = url.openConnection();
            connection.connect();
            // this will be useful so that you can show a typical 0-100% progress bar
            int fileLength = connection.getContentLength();
            int planid = CUtils.getUserPurchasedPlanFromPreference(this);
            if (planid > 1){
                fname = PDF_NAME_PREMIUM_KUNDALI;
            }else {
                fname = PDF_NAME_KUNDALI;
            }
            ///////////
            fname = fname.replace("#", "_" + System.currentTimeMillis());
            //String root = Environment.getExternalStorageDirectory().toString();

            File myDir = new File(getCacheDir(), FOLDER_ASTROSAGE+"/");

            if (!myDir.exists()) {
                boolean result = myDir.mkdirs();
                if (!result) {
                    updateProgress();
                    return;
                }
            }

            File file = new File(myDir, fname);
            if (file.exists()) file.delete();

            InputStream input = new BufferedInputStream(connection.getInputStream());
            OutputStream output = new FileOutputStream(file);

            byte data[] = new byte[1024];
            long total = 0;
            int count;
            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....


                output.write(data, 0, count);
            }

            output.flush();
            output.close();
            input.close();

            if (total > 0 && file.length() > 0) {
                progress = PROGRESS_COMPLETE;
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        updateProgress();
    }

    private void updateProgress() {
        Bundle resultData = new Bundle();
        resultData.putInt(KEY_PROGRESS, progress);
        resultData.putString(KEY_FNAME, fname);
        resultData.putBoolean(KEY_PDF_SHARE, isPdfShare);
        receiver.send(UPDATE_PROGRESS, resultData);
    }
}