package com.ojassoft.astrosage.misc;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.ResultReceiver;

import androidx.core.content.FileProvider;

import android.widget.Toast;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.PdfViewerActivity;
import com.ojassoft.astrosage.ui.act.matching.OutputMatchingMasterActivity;
import com.ojassoft.astrosage.utils.CGlobalMatching;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.io.File;

import static com.ojassoft.astrosage.utils.CGlobalVariables.FOLDER_ASTROSAGE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_FNAME;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_PDF_SHARE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_PROGRESS;
import static com.ojassoft.astrosage.utils.CGlobalVariables.PROGRESS_COMPLETE;

public class DownloadReceiver extends ResultReceiver {
    Context context;
    String fname = "";
    boolean isFromMatchMaking = false;
    String topTitle;
    public DownloadReceiver(Context context, Handler handler) {
        super(handler);
        this.context = context;
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        super.onReceiveResult(resultCode, resultData);
        if (context == null) return;

        if (resultCode == DownloadService.UPDATE_PROGRESS) {
            fname = resultData.getString(KEY_FNAME);
            boolean isPdfShare = resultData.getBoolean(KEY_PDF_SHARE, true);

            if (context instanceof OutputMatchingMasterActivity) {
                ((OutputMatchingMasterActivity) context).cancelProgressDialog();
                ((OutputMatchingMasterActivity) context).cancelDownloadProgressDialog();
                isFromMatchMaking = true;
                //AstrosageKundliApplication.userKundliName = CGlobalMatching.getCGlobalMatching().getBoyPersonalDetail().getName().trim()+" & "+CGlobalMatching.getCGlobalMatching().getGirlPersonalDetail().getName().trim();
                topTitle = CGlobalMatching.getCGlobalMatching().getBoyPersonalDetail().getName().trim()+" & "+CGlobalMatching.getCGlobalMatching().getGirlPersonalDetail().getName().trim();
            }

            int progress = resultData.getInt(KEY_PROGRESS);
            if (progress == PROGRESS_COMPLETE) {
                if (isPdfShare) {
                    sharePdf(context);
                } else {
                    viewPdf(context);
                }
            } else {
                if (isPdfShare) {
                    Toast.makeText(context, context.getString(R.string.faild_share_pdf), Toast.LENGTH_SHORT).show();
                } else {
                    // failed to download pdf--now open the url for download
                    SharedPreferences sharedPreferencesForLang = context.getSharedPreferences(CGlobalVariables.MYKUNDLI_LANGUAGE_PREFS_NAME, Context.MODE_PRIVATE);
                    int LANGUAGE_CODE = sharedPreferencesForLang.getInt(CGlobalVariables.APP_PREFS_AppLanguage, CGlobalVariables.ENGLISH);
                    CUtils.downloadKundliPdfForMatching((Activity) context, LANGUAGE_CODE);
                }
            }
        }
    }

    private void sharePdf(Context context) {
        try {
            BeanHoroPersonalInfo boyBeanHoroPersonalInfo = CGlobalMatching.getCGlobalMatching().getBoyPersonalDetail();
            BeanHoroPersonalInfo girlBeanHoroPersonalInfo = CGlobalMatching.getCGlobalMatching().getGirlPersonalDetail();
            String subject = context.getString(R.string.matching_pdf_sub_text);
            subject = subject.replace("#boy", boyBeanHoroPersonalInfo.getName());
            subject = subject.replace("#girl", girlBeanHoroPersonalInfo.getName());
            String body = context.getString(R.string.matching_pdf_text);
            body = body.replace("#boy", boyBeanHoroPersonalInfo.getName());
            body = body.replace("#girl", girlBeanHoroPersonalInfo.getName());
            //String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(context.getCacheDir(), FOLDER_ASTROSAGE + "/");

            File file = new File(myDir, fname);
            Uri uri = Uri.fromFile(file);
            Intent share = new Intent();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                share.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                uri = FileProvider.getUriForFile(context, "com.ojassoft.astrosage", file);
            }

            share.setAction(Intent.ACTION_SEND);
            share.setType("application/pdf");
            share.putExtra(Intent.EXTRA_STREAM, uri);
            share.putExtra(Intent.EXTRA_SUBJECT, subject);
            share.putExtra(Intent.EXTRA_TEXT, body);
            //share.setPackage("com.whatsapp");

            context.startActivity(share);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void viewPdf(Context context) {
        try {
            //String root = Environment.getExternalStorageDirectory().toString();
            File myDir = new File(context.getCacheDir(), FOLDER_ASTROSAGE + "/");

            File file = new File(myDir, fname);
            /*Intent intent;

if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

    Uri uri = FileProvider.getUriForFile(context, "com.ojassoft.astrosage", file);
    intent = new Intent(Intent.ACTION_VIEW);
    intent.setData(uri);
    intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
    context.startActivity(intent);
} else {
    intent = new Intent(Intent.ACTION_VIEW);
    intent.setDataAndType(Uri.fromFile(file), "application/pdf");
    intent = Intent.createChooser(intent, "Open File");
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    context.startActivity(intent);
}*/

            Intent intent = new Intent(context, PdfViewerActivity.class);
            intent.putExtra(CGlobalVariables.PDF_FILE, file.getAbsolutePath());
            intent.putExtra(CGlobalVariables.IS_FROM_MATCH_MAKING, isFromMatchMaking);
            intent.putExtra(CGlobalVariables.PDF_KUNDLI_TOP_TITLE, topTitle);
            intent.putExtra("is_from_Receiver", true);
            intent.putExtra("local_chart_id", "");

            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}