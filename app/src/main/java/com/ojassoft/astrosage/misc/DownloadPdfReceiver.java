package com.ojassoft.astrosage.misc;

import static com.ojassoft.astrosage.utils.CGlobalVariables.FOLDER_ASTROSAGE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_CHART_STYLE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_FNAME;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_PDF_DOWNLOAD_COUNT;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_PDF_SHARE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_PROGRESS;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_RECEIVER;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_REPORT_TYPE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.PDF_LIMIT_COMPLETE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.PROGRESS_COMPLETE;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.view.Window;
import android.widget.Toast;

import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.ActPrintKundliCategory;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.act.OutputMasterActivity;
import com.ojassoft.astrosage.ui.act.PdfViewerActivity;
import com.ojassoft.astrosage.ui.fragments.TopUpDialogue;
import com.ojassoft.astrosage.utils.CGlobal;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.io.File;
import java.util.Objects;

/**
 * ResultReceiver implementation for handling PDF download results and user interactions.
 * 
 * This class manages:
 * - PDF download completion handling
 * - Progress tracking and updates
 * - File sharing and viewing
 * - Download limit enforcement
 * - User notifications and dialogs
 * - Activity-specific progress updates
 * 
 * Key Features:
 * - Supports both premium and basic PDF downloads
 * - Handles file sharing across Android versions
 * - Manages download quota for premium users
 * - Provides progress feedback to different activities
 * - Supports PDF viewing in custom viewer
 * 
 * Usage Context:
 * - Used in conjunction with DownloadPdfService
 * - Handles callbacks for PDF download operations
 * - Manages user interaction after download completion
 */
public class DownloadPdfReceiver extends ResultReceiver {
    
    /** Application context for resource access and UI operations */
    Context context;
    
    /** Downloaded PDF filename */
    String fname = "";
    
    /** Title displayed in PDF viewer */
    String topTitle = "";
    String reportType = "";

    /**
     * Constructor for DownloadPdfReceiver.
     * 
     * @param context Application context for UI operations
     * @param handler Handler for processing results on main thread
     */
    public DownloadPdfReceiver(Context context, Handler handler) {
        super(handler);
        this.context = context;
    }

    /**
     * Handles download completion and processes results.
     * 
     * This method:
     * 1. Updates UI progress indicators
     * 2. Manages download quotas for premium users
     * 3. Handles file sharing/viewing based on user preference
     * 4. Shows appropriate dialogs for download status
     * 
     * @param resultCode Result status code
     * @param resultData Bundle containing download details and file info
     */
    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        super.onReceiveResult(resultCode, resultData);
        if (context == null) return;
        if (resultCode == DownloadService.UPDATE_PROGRESS) {
            fname = resultData.getString(KEY_FNAME);
            reportType = resultData.getString(KEY_REPORT_TYPE);
            boolean isPdfShare = resultData.getBoolean(KEY_PDF_SHARE, true);
            int chartStyle = resultData.getInt(KEY_CHART_STYLE, 0);
            int pdfDownloadCount = resultData.getInt(KEY_PDF_DOWNLOAD_COUNT, 0);

            // Update activity-specific progress indicators
            if (context instanceof OutputMasterActivity) {
                ((OutputMasterActivity) context).cancelProgressDialog();
                ((OutputMasterActivity) context).cancelDownloadProgressDialog();
               /* AstrosageKundliApplication.userKundliName = CGlobal.getCGlobalObject().getHoroPersonalInfoObject()
                        .getName().trim();*/
                topTitle = CGlobal.getCGlobalObject().getHoroPersonalInfoObject()
                        .getName().trim();
            } else if (context instanceof ActPrintKundliCategory) {
                ((ActPrintKundliCategory) context).hideProgressBar();
                topTitle = CGlobal.getCGlobalObject().getHoroPersonalInfoObject()
                        .getName().trim();
            }
            int progress = resultData.getInt(KEY_PROGRESS);
            // Handle download completion
            if (progress == PROGRESS_COMPLETE || progress == PDF_LIMIT_COMPLETE) {
                //The permissible limit of 100 premium horoscope pdf downloads in a month has exhausted.
                int planId = CUtils.getUserPurchasedPlanFromPreference(context);

                File myDir = new File(context.getCacheDir(), FOLDER_ASTROSAGE + "/");
                File file = new File(myDir, fname);

                // Handle premium user downloads
                if (planId > CGlobalVariables.BASIC_PLAN_ID) {
                    handlePremiumDownload(pdfDownloadCount, file, isPdfShare);
                } else {
                    // Handle basic user downloads
                    if (isPdfShare) {
                        sharePdf(context, file);
                    } else {
                        viewPdf(context, file);
                    }
                }
            } else {   
                // Handle download failure
                handleDownloadFailure(isPdfShare, chartStyle);
            }
        }
    }

    /**
     * Handles download completion for premium users.
     * Updates the user's remaining download quota, manages notifications, and triggers UI updates.
     *
     * Logic:
     * - If the user has exhausted their downloads and the PDF file does not exist, show a top-up dialog (for Dhruv plan) or a remaining downloads dialog.
     * - Otherwise, share or view the PDF as requested.
     * - Show a dialog with remaining downloads if appropriate (not for Kundli AI+ plan after the first time).
     * - Update the stored download count and notify the UI to refresh premium/free view.
     *
     * @param pdfDownloadCount Remaining download quota
     * @param file Downloaded PDF file
     * @param isPdfShare Whether to share the PDF
     */
    private void handlePremiumDownload(int pdfDownloadCount, File file, boolean isPdfShare) {
        // Check if this is the first time the count is being set
        boolean isFirstTime = (CUtils.getIntData(context, CGlobalVariables.PDF_DOWNLOAD_COUNT_KEY, -1) == -1);
        boolean isPdfNotExists = (!file.exists() || file.length() < 1024 || !file.canRead());

        // If no downloads left and file doesn't exist, show appropriate dialog and return
        if (pdfDownloadCount == 0 && isPdfNotExists) {
            if (CUtils.isDhruvPlan(context)) {
                showTopupDialogue();
            } else {
                showDialog(context.getResources().getString(R.string.pdf_download_remaining)
                        .replace("#", String.valueOf(pdfDownloadCount)));
            }
            return;
        }

        // Save the updated download count before broadcasting
        CUtils.saveIntData(context, CGlobalVariables.PDF_DOWNLOAD_COUNT_KEY, pdfDownloadCount);
        // Notify UI to update premium/free view
        try {
            Intent intent = new Intent(CGlobalVariables.PDF_DOWNLOAD_COUNT_UPDATED);
            LocalBroadcastManager.getInstance(context).sendBroadcast(intent);
        } catch (Exception e) {
            // Ignore
        }

        // Share or view the PDF as requested
        if (isPdfShare) {
            sharePdf(context, file);
        } else {
            viewPdf(context, file);
        }

        // Show remaining downloads dialog if not the first time and not Kundli AI+ plan
        if (pdfDownloadCount != CGlobalVariables.PDF_DOWNLOAD_COUNT && !(CUtils.isKundliAIPlusPlan(context) && !isFirstTime)) {
            showDialog(context.getResources().getString(R.string.pdf_download_remaining)
                    .replace("#", String.valueOf(pdfDownloadCount)));
        }
    }

    /**
     * Handles download failure scenarios.
     * Shows appropriate error messages and fallback options.
     * 
     * @param isPdfShare Whether the intent was to share the PDF
     * @param chartStyle Chart style for fallback download
     */
    private void handleDownloadFailure(boolean isPdfShare, int chartStyle) {
        if (isPdfShare) {
            Toast.makeText(context, context.getString(R.string.faild_share_pdf), Toast.LENGTH_SHORT).show();
        } else {
            SharedPreferences sharedPreferencesForLang = context.getSharedPreferences(
                    CGlobalVariables.MYKUNDLI_LANGUAGE_PREFS_NAME,
                    Context.MODE_PRIVATE);
            int LANGUAGE_CODE = sharedPreferencesForLang.getInt(
                    CGlobalVariables.APP_PREFS_AppLanguage, CGlobalVariables.ENGLISH);
            CUtils.downloadKundliPdf((Activity) context, chartStyle, LANGUAGE_CODE);
        }
    }

    /**
     * Shows a dialog with download status or error message.
     * 
     * @param text Message to display in dialog
     */
    private void showDialog(String text) {
        try {
            ColorDrawable colorDrawable = new ColorDrawable(ContextCompat.getColor(context, R.color.bg_card_view_color));
            Dialog dialog = null;
            AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.CustomAlertDialogTheme)
                    .setMessage(text)
                    .setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });
            dialog = builder.create();
            Objects.requireNonNull(dialog.getWindow()).setBackgroundDrawable(colorDrawable);
            dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            dialog.show();
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    /**
     * Shows the top-up dialog for premium features.
     * Handles fragment transaction for dialog display.
     */
    private void showTopupDialogue() {
        try {
            FragmentManager fm = ((BaseInputActivity) context).getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            Fragment prev = fm.findFragmentByTag("TopUpDialogue");
            if (prev != null) {
                ft.remove(prev);
            }
            ft.addToBackStack(null);
            TopUpDialogue horaMeaning = TopUpDialogue.newInstance();
            horaMeaning.show(fm, "TopUpDialogue");
            ft.commit();
        } catch (Exception e) {
        }
    }

    /**
     * Shares the PDF file using Android's share intent.
     * Handles file URI permissions across Android versions.
     * 
     * @param context Application context
     * @param file PDF file to share
     */
    private void sharePdf(Context context, File file) {
        try {
            String subject = context.getString(R.string.share_pdf_sub_text);
            String body = context.getString(R.string.share_pdf_text);

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

            context.startActivity(share);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens the PDF file in the app's custom PDF viewer.
     * 
     * @param context Application context
     * @param file PDF file to view
     */
    private void viewPdf(Context context, File file) {


// Intent intent;
        try {

     /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

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
            intent.putExtra(CGlobalVariables.IS_FROM_MATCH_MAKING, false);
            intent.putExtra(CGlobalVariables.PDF_KUNDLI_TOP_TITLE, topTitle);
            intent.putExtra("is_from_Receiver", true);
            intent.putExtra("local_chart_id", "");
            intent.putExtra("report_type", reportType);
//            intent.putExtra("pdf_type",)

            context.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}