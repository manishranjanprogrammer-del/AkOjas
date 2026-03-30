package com.ojassoft.astrosage.misc;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.ResultReceiver;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.Volley;
import com.ojassoft.astrosage.networkcall.InputStreamVolleyRequest;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.net.URL;
import java.net.URLConnection;

import static com.ojassoft.astrosage.utils.CGlobalVariables.FOLDER_ASTROSAGE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_CHART_STYLE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_FNAME;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_NORTH_CHART;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_PDF_DOWNLOAD_COUNT;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_PDF_SHARE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_PROGRESS;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_RECEIVER;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_REPORT_TYPE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_URL;
import static com.ojassoft.astrosage.utils.CGlobalVariables.PDF_LIMIT_COMPLETE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.PDF_NAME_KUNDALI;
import static com.ojassoft.astrosage.utils.CGlobalVariables.PDF_NAME_PREMIUM_KUNDALI;
import static com.ojassoft.astrosage.utils.CGlobalVariables.PROGRESS_COMPLETE;

import org.apache.commons.logging.LogFactory;

/**
 * Background service for downloading Kundli PDF files using Volley.
 * 
 * This service handles:
 * - Downloading PDF files from the server
 * - Storing files in app's cache directory
 * - Managing download progress and completion status
 * - Handling different PDF types (basic/premium) based on user's plan
 * - Supporting PDF sharing functionality
 * - Managing download limits and remaining count
 * 
 * The service uses Volley for network operations with:
 * - Custom retry policy
 * - Progress tracking
 * - Error handling
 * - File system operations
 * 
 * Key Features:
 * - Downloads are performed in background thread
 * - Progress updates via ResultReceiver
 * - Automatic file naming with timestamps
 * - Cache directory management
 * - Plan-based PDF type selection
 */
public class DownloadPdfService extends IntentService {
    
    /** Progress update identifier for result receiver */
    public static final int UPDATE_PROGRESS = 8344;
    private static final org.apache.commons.logging.Log log = LogFactory.getLog(DownloadPdfService.class);

    /** Result receiver for sending progress updates to UI */
    ResultReceiver receiver;
    
    /** PDF filename, defaults to basic kundli name */
    String fname = PDF_NAME_KUNDALI;
    
    /** Flag indicating if PDF should be shared after download */
    boolean isPdfShare;
    
    /** Chart style parameter for PDF generation */
    int chartStyle;
    
    /** Current progress of download (0-100) */
    int progress = 0;
    
    /** Remaining PDF downloads available to user */
    int pdfRemainingCount = 0;
    String reportType="";

    /**
     * Default constructor required for IntentService.
     * Sets the worker thread name to "DownloadService".
     */
    public DownloadPdfService() {
        super("DownloadService");
    }

    /**
     * Handles the download intent in a background thread.
     * This is the main entry point for the service's work.
     * 
     * Process:
     * 1. Extracts parameters from intent
     * 2. Determines PDF type based on user's plan
     * 3. Initiates download using Volley
     * 4. Handles response/error and file storage
     * 5. Updates progress via receiver
     * 
     * @param intent Intent containing download parameters:
     *               - KEY_PDF_SHARE: Whether to share PDF after download
     *               - KEY_CHART_STYLE: Chart style for PDF
     *               - KEY_RECEIVER: ResultReceiver for progress updates
     *               - KEY_URL: Download URL
     */
    @Override
    protected void onHandleIntent(Intent intent) {
        isPdfShare = intent.getBooleanExtra(KEY_PDF_SHARE, true);
        chartStyle = intent.getIntExtra(KEY_CHART_STYLE, 0);
        receiver = intent.getParcelableExtra(KEY_RECEIVER);
        reportType = intent.getStringExtra(KEY_REPORT_TYPE);
        String urlToDownload = intent.getStringExtra(KEY_URL);
        Log.e("downloadPDF", "onHandleIntent:url== "+urlToDownload );
        
        int planid = CUtils.getUserPurchasedPlanFromPreference(this);

        // Set filename based on user's plan
        if (planid > CGlobalVariables.BASIC_PLAN_ID){
            fname = PDF_NAME_PREMIUM_KUNDALI;
        }else {
            fname = PDF_NAME_KUNDALI;
        }

        // Create and configure Volley request
        InputStreamVolleyRequest request = new InputStreamVolleyRequest(Request.Method.POST, urlToDownload,
                new Response.Listener<byte[]>() {
                    /**
                     * Handles successful download response.
                     * Processes the downloaded PDF and saves it to cache.
                     * 
                     * @param response Downloaded PDF data as byte array
                     */
                    @Override
                    public void onResponse(byte[] response) {
                        try {
                            if (response != null) {
                                String stringToCheckForError = new String(response);
                                if(stringToCheckForError.contains("Error:0")){
                                    pdfRemainingCount = 0;
                                    progress = PDF_LIMIT_COMPLETE;
                                }else {
                                    if(CUtils.isDhruvPlan(getApplicationContext())) { // in case of kundli ai+ no count is returned with free pdf
                                        // Extract remaining download count from response
                                        pdfRemainingCount = Array.getByte(response, response.length - 1);
                                    }
                                    // Generate unique filename with timestamp
                                    fname = fname.replace("#", "_" + System.currentTimeMillis());

                                    // Create cache directory if needed
                                    String root = "";
                                    File myDir = new File(getCacheDir(), FOLDER_ASTROSAGE+"/");

                                    if (!myDir.exists()) {
                                        boolean result = myDir.mkdirs();
                                        if (!result) {
                                            updateProgress();
                                            return;
                                        }
                                    }

                                    // Save PDF file
                                    File file = new File(myDir, fname);
                                    if (file.exists()) {
                                        file.delete();
                                    }
                                    if (!file.exists()) {
                                        file.createNewFile();
                                    }
                                    BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));
                                    bos.write(response);
                                    bos.flush();
                                    bos.close();
                                    progress = PROGRESS_COMPLETE;
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        updateProgress();
                    }
                }, new Response.ErrorListener() {
                    /**
                     * Handles download failure.
                     * Updates progress with error state.
                     * 
                     * @param error Volley error details
                     */
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                        updateProgress();
                    }
                }, null);

        // Configure request timeout and retry policy
        int socketTimeout = 60000; // 60 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 
                                                  DefaultRetryPolicy.DEFAULT_MAX_RETRIES, 
                                                  DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        request.setRetryPolicy(policy);

        // Execute download request
        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext(), new HurlStack());
        mRequestQueue.add(request);
    }

    /**
     * Updates download progress via ResultReceiver.
     * Bundles current progress state and file details.
     * 
     * Bundle contains:
     * - Current progress
     * - Filename
     * - Share flag
     * - Chart style
     * - Remaining download count
     */
    private void updateProgress() {
        Bundle resultData = new Bundle();
        resultData.putInt(KEY_PROGRESS, progress);
        resultData.putString(KEY_FNAME, fname);
        resultData.putBoolean(KEY_PDF_SHARE, isPdfShare);
        resultData.putInt(KEY_CHART_STYLE, chartStyle);
        resultData.putInt(KEY_PDF_DOWNLOAD_COUNT, pdfRemainingCount);
        resultData.putString(KEY_REPORT_TYPE,reportType);
        receiver.send(UPDATE_PROGRESS, resultData);
    }
}