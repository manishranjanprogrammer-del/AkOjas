package com.ojassoft.astrosage.ui.fragments;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.misc.DownloadPdfReceiver;
import com.ojassoft.astrosage.misc.DownloadPdfService;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BrihatActivity;
import com.ojassoft.astrosage.ui.act.BrihatKundliActivity;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.FontUtils;

import java.util.Date;

import static android.os.Build.VERSION.SDK_INT;
import static com.ojassoft.astrosage.ui.act.BaseInputActivity.PERMISSION_EXTERNAL_STORAGE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTIC_DOWNLOAD_PDF;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTIC_DOWNLOAD_PDF_KUNDLI_LASTSCREEN;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_CHART_STYLE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_PDF_SHARE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_RECEIVER;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_REPORT_TYPE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_URL;

/**
 * DownloadPDF Fragment - Handles PDF download functionality for horoscope/kundli reports
 * 
 * This fragment provides functionality to:
 * 1. Download free or premium kundli PDFs based on user's subscription
 * 2. Navigate to premium kundli purchase flow
 * 3. Handle storage permissions for PDF downloads
 * 4. Track analytics for download and purchase events
 * 
 * The fragment supports two main actions:
 * - Direct PDF download for free/premium users
 * - Navigation to premium kundli purchase screen
 */
public class DownloadPDF extends Fragment {

    // View references
    /**
     * Root view of the fragment
     */
    private View view = null;

    /**
     * Language code for localization, defaults to English
     */
    private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;

    /**
     * Custom typeface for consistent text styling
     */
    private Typeface typeface;

    /**
     * TextViews for displaying pricing and information
     */
    private TextView textView1, textView2, tvOrText;
    private LinearLayout btnDownloadPDF;
    private TextView btnOrderPrintedKundli, pdfPageCountTV, pfdDownloadMsg;
    private int chart_Style = 0;

    /**
     * Tag for chart style bundle parameter
     */
    private static final String chart_Style_Tag = "chart_Style";

    /**
     * URL for premium printed kundli purchase
     */
    private String downloadPrintedPdfUrl = "https://buy.astrosage.com/miscellaneous/astrosage-big-horoscope";

    /**
     * Progress dialog for download operations
     */
    private CustomProgressDialog pd;

    /**
     * Reference to parent activity
     */
    Activity activity;

    // BroadcastReceiver to update UI when download count changes
    private BroadcastReceiver pdfDownloadCountReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.e("downloadPDF", "Receiver: called");
            setUpFreeOrPremiumKundliView();
        }
    };

    /**
     * Lifecycle callback when fragment attaches to activity.
     * Stores reference to parent activity.
     *
     * @param context The activity context
     */
    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        activity = context;
    }

    /**
     * Lifecycle callback for fragment creation.
     * Initializes language settings and typeface.
     *
     * @param savedInstanceState Bundle containing saved state
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LANGUAGE_CODE = ((AstrosageKundliApplication) activity
                .getApplication()).getLanguageCode();
        typeface = CUtils.getRobotoFont(activity,
                LANGUAGE_CODE, CGlobalVariables.regular);
        chart_Style = getArguments().getInt(chart_Style_Tag, 0);
    }

    /**
     * Creates and returns the fragment's view hierarchy.
     * Initializes UI components if view not already created.
     *
     * @param inflater           Layout inflater
     * @param container          Parent view group
     * @param savedInstanceState Saved state bundle
     * @return The fragment's view
     */
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view == null)
            view = inflater.inflate(R.layout.lay_download_pdf, container, false);
        setLayRef(view);

        return view;
    }

    /**
     * Initializes view references and sets up click listeners.
     * Configures text styling and button behaviors.
     *
     * @param view The root view of the fragment
     */
    private void setLayRef(View view) {
        //  textView1 = (TextView) view.findViewById(R.id.textView1);
        // textView1.setPaintFlags(textView1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

//        textView2 = (TextView) view.findViewById(R.id.textView2);
        btnDownloadPDF = view.findViewById(R.id.btnDownloadPDF);
        btnOrderPrintedKundli = view.findViewById(R.id.btnOrderPrintedKundli);
        TextView downloadPdfTitleTV = view.findViewById(R.id.font_auto_lay_download_pdf_2);
        TextView printedKundliTitleTV = view.findViewById(R.id.font_auto_lay_download_pdf_3);
        pdfPageCountTV = view.findViewById(R.id.pdf_page_count_tv);
        pfdDownloadMsg = view.findViewById(R.id.pdf_download_msg_tv);
        FontUtils.changeFont(requireContext(), downloadPdfTitleTV, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(requireContext(), printedKundliTitleTV, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_BOLD);


//        textView2.setText(getContext().getResources().getString(R.string.desc_download_kundli).replace("#",
//                CUtils.getCurrentPlanCustomReturnString(CUtils.getUserPurchasedPlanFromPreference(getContext()))));

//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            textView2.setJustificationMode(Layout.JUSTIFICATION_MODE_INTER_WORD);
//        }

        //   tvOrText = (TextView) view.findViewById(R.id.tvOrText);
        //set Typeface
        // textView1.setTypeface(typeface);
        //  textView2.setTypeface(typeface);
        // btnDownloadPDF.setTypeface(typeface);
        // btnOrderPrintedKundli.setTypeface(typeface);
//        tvOrText.setTypeface(typeface);
        //set the listener of btnDownloadPDF
        btnDownloadPDF.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                downloadPDF();
            }
        });

        btnOrderPrintedKundli.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  CUtils.getUrlLink(downloadPrintedPdfUrl,activity,LANGUAGE_CODE,0);
                CUtils.isConnectedWithInternet(activity);
                {
                    CUtils.createSession(activity, "SOPBH");

                }

                boolean showNewBrihatScreen = CUtils.getBooleanData(activity, CGlobalVariables.SHOW_NEW_BRIHAT_KUNDLI_PAGE, false);
                Intent tvIntent;
                tvIntent = showNewBrihatScreen ? new Intent(activity, BrihatKundliActivity.class) : new Intent(activity, BrihatActivity.class);
                tvIntent.putExtra(CGlobalVariables.DataComingFromDownloadPdf, true);
                CUtils.BRIHAT_KUNDALI_PURCHASE_SOURCE = "Download PDF";
                startActivity(tvIntent);
                CUtils.googleAnalyticSendWitPlayServie(activity,
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_ORDER_NOW_DOWNLOAD_PDF,
                        GOOGLE_ANALYTIC_DOWNLOAD_PDF_KUNDLI_LASTSCREEN);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.FIREBASE_EVENT_ORDER_NOW, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
            }
        });
    }

    /**
     * Configures the view based on the user's subscription plan and remaining downloads.
     * <p>
     * - Premium users with remaining downloads see the premium view.
     * - Premium users with no downloads left, non-premium users, or on error see the free view.
     *
     * This method is robust to errors and always falls back to the free view if any issue occurs.
     */
    private void setUpFreeOrPremiumKundliView() {
        try {
            int planId = CUtils.getUserPurchasedPlanFromPreference(getContext());
            if (planId <= CGlobalVariables.BASIC_PLAN_ID) {
                setFreeKundliView();
                return;
            }
            // Only premium users: check remaining count
            CUtils.getRemainingReportCountFromServer(getContext(), new CUtils.PdfCountResponseListener() {
                @Override
                public void onPdfCountReceived(int count) {
                    Log.e("countcheck", "onPdfCountReceived: "+count);
                    if (count > 0) {
                        setPremiumKundliView();
                    } else {
                        setFreeKundliView();
                    }
                }
                @Override
                public void onPdfCountError() {
                    setFreeKundliView();
                }
            });
        } catch (Exception e) {
            // On any error, show free view as fallback
            setFreeKundliView();
        }
    }

    /**
     * Sets up the premium kundli view with:
     * - 200+ pages indicator
     * - Premium download message
     * This view is shown to premium users who have remaining downloads.
     */
    private void setPremiumKundliView() {
        pdfPageCountTV.setText(R.string._200_plus_pages);
        pfdDownloadMsg.setText(R.string.your_premium_kundli_download_now);
    }

    /**
     * Sets up the free kundli view with:
     * - 50 pages indicator
     * - Free download message
     * This view is shown to:
     * - Non-premium users
     * - Premium users who have exhausted their downloads
     * - When any errors occur during view setup
     */
    private void setFreeKundliView() {
        pdfPageCountTV.setText(R.string._50_pages);
        pfdDownloadMsg.setText(R.string.janam_kundli_absolutely_free);
    }

    /**
     * Instance of the DownloadPDF Class
     *
     * @return
     */
    public static DownloadPDF newInstance(int inner_chart_Style) {
        Bundle bundle = new Bundle();
        bundle.putInt(chart_Style_Tag, inner_chart_Style);
        DownloadPDF downloadPDF = new DownloadPDF();
        downloadPDF.setArguments(bundle);
        return downloadPDF;
    }

    /**
     * Initiates PDF download process.
     * Shows progress dialog and starts download service.
     * Tracks analytics for download event.
     */
    public void downloadPDF() {

        pd = new CustomProgressDialog(activity, typeface);
        pd.setCancelable(false);
        pd.show();
        boolean IS_NORTH_CHART = false;
        if (chart_Style == 0 || chart_Style == 2)
            IS_NORTH_CHART = true;
        Intent intent = new Intent(activity, DownloadPdfService.class);

        intent.putExtra(KEY_PDF_SHARE, false);
        intent.putExtra(KEY_CHART_STYLE, chart_Style);
        intent.putExtra(KEY_URL, CUtils.getPdfUrl(activity, chart_Style, LANGUAGE_CODE) + "&time=" + (new Date().getTime()) + "&pdftype=7");
        intent.putExtra(KEY_RECEIVER, new DownloadPdfReceiver(activity, new Handler()));
        intent.putExtra(KEY_REPORT_TYPE, "7");
        activity.startService(intent);

        CUtils.googleAnalyticSendWitPlayServie(activity,
                CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                GOOGLE_ANALYTIC_DOWNLOAD_PDF,
                GOOGLE_ANALYTIC_DOWNLOAD_PDF_KUNDLI_LASTSCREEN);
        String labell = GOOGLE_ANALYTIC_DOWNLOAD_PDF;// + "_" + GOOGLE_ANALYTIC_DOWNLOAD_PDF_KUNDLI_LASTSCREEN;
        //CUtils.fcmAnalyticsEvents(labell, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");-> now trigger from addFacebookAndFirebaseEvent method
        com.ojassoft.astrosage.varta.utils.CUtils.addFacebookAndFirebaseEvent(CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK,labell,GOOGLE_ANALYTIC_DOWNLOAD_PDF_KUNDLI_LASTSCREEN);
    }

    /**
     * Safely dismisses the progress dialog.
     * Handles various edge cases and exceptions.
     */
    public void cancelProgressDialog() {
        try {
            if (activity == null) return;
            if (pd != null && pd.isShowing())
                pd.dismiss();
        } catch (final IllegalArgumentException e) {
            // Do nothing.
        } catch (final Exception e) {
            // Do nothing.
        } finally {
            pd = null;
        }
    }

    /**
     * Requests storage permissions based on Android version.
     *
     * @param requestCode Permission request code
     */
    private void requestExternalStoragePermission(int requestCode) {
        if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            CUtils.requestForExternalStorageNew(activity, this, requestCode);
        } else {
            CUtils.requestForExternalStorage(activity, this, requestCode);
        }
    }

    /**
     * Lifecycle callback when fragment becomes visible.
     * Updates view based on user's subscription status.
     */
    @Override
    public void onResume() {
        super.onResume();
        setUpFreeOrPremiumKundliView();
        Log.e("downloadPDF", "onResume: called");
        // Register receiver for PDF download count updates
        LocalBroadcastManager.getInstance(activity).registerReceiver(
                pdfDownloadCountReceiver,
                new IntentFilter(CGlobalVariables.PDF_DOWNLOAD_COUNT_UPDATED)
        );
    }

    @Override
    public void onPause() {
        super.onPause();
        // Unregister receiver to avoid leaks
        try {
            LocalBroadcastManager.getInstance(activity).unregisterReceiver(pdfDownloadCountReceiver);
        } catch (Exception e) {
            // Ignore
        }
    }

    /**
     * Handles permission request results.
     * Proceeds with download if permissions granted.
     *
     * @param requestCode  The request code passed to requestPermissions
     * @param permissions  The requested permissions
     * @param grantResults The grant results for the corresponding permissions
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults != null && grantResults.length > 0) {
            if (requestCode == PERMISSION_EXTERNAL_STORAGE) {
                boolean isPermissionGranted = false;
                if (grantResults.length == 1) {
                    boolean READ_EXTERNAL_STORAGE = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    if (READ_EXTERNAL_STORAGE) {
                        isPermissionGranted = true;
                    }
                } else if (grantResults.length == 2) {
                    boolean READ_EXTERNAL_STORAGE = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean WRITE_EXTERNAL_STORAGE = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (READ_EXTERNAL_STORAGE && WRITE_EXTERNAL_STORAGE) {
                        isPermissionGranted = true;
                    }
                }

                if (isPermissionGranted) {
                    downloadPDF();
                } else {
                    Toast.makeText(activity, activity.getResources().getString(R.string.permission_allow), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
