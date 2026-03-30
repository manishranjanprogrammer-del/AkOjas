package com.ojassoft.astrosage.ui.fragments;


import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.misc.DownloadReceiver;
import com.ojassoft.astrosage.misc.DownloadService;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;

import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import static android.os.Build.VERSION.SDK_INT;
import static com.ojassoft.astrosage.ui.act.BaseInputActivity.PERMISSION_EXTERNAL_STORAGE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTIC_DOWNLOAD_PDF;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTIC_DOWNLOAD_PDF_MATCHING_LASTSCREEN;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_PDF_SHARE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_RECEIVER;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_URL;

/**
 * Created by ojas on 13/12/16.
 * This class is used to download the PDF of User
 */

public class DownloadMarriagePDF extends Fragment {
    Activity activity;
    private View view = null;
    private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    private Typeface typeface;
    private TextView textView1, textView2, tvOrText;
    private Button btnDownloadPDF, btnOrderPrintedKundli;
    private CustomProgressDialog pd;
    //private int chart_Style = 0;
    private static final String chart_Style_Tag = "chart_Style";
    // private String downloadPrintedPdfUrl = "https://buy.astrosage.com/miscellaneous/buy-kundli-janam-patri";
    private String downloadPrintedPdfUrl = "https://buy.astrosage.com/miscellaneous/astrosage-big-horoscope";

    @Override
    public void onAttach(Activity context) {
        super.onAttach(context);
        activity = context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LANGUAGE_CODE = ((AstrosageKundliApplication) getActivity()
                .getApplication()).getLanguageCode();
        typeface = CUtils.getRobotoFont(getActivity(),
                LANGUAGE_CODE, CGlobalVariables.regular);
        //chart_Style = getArguments().getInt(chart_Style_Tag, 0);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (view == null)
            view = inflater.inflate(R.layout.lay_marriage_download_pdf, container, false);

        setLayRef(view);

        return view;
    }

    /**
     * @param view
     * @author Amit Rautela
     * @desc This method is used to set the layout of Components
     */
    private void setLayRef(View view) {
        textView1 = (TextView) view.findViewById(R.id.textView1);
        textView1.setPaintFlags(textView1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);

        textView2 = (TextView) view.findViewById(R.id.textView2);
        btnDownloadPDF = (Button) view.findViewById(R.id.btnDownloadPDF);
        btnOrderPrintedKundli = (Button) view.findViewById(R.id.btnOrderPrintedKundli);

        tvOrText = (TextView) view.findViewById(R.id.tvOrText);
        //set Typeface
        textView1.setTypeface(typeface);
        textView2.setTypeface(typeface);
        btnDownloadPDF.setTypeface(typeface);
        btnOrderPrintedKundli.setTypeface(typeface);
        tvOrText.setTypeface(typeface);
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
                CUtils.createSession(getActivity(), "SMAAQ");
                CUtils.googleAnalyticSendWitPlayServie(activity,
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_ASK_A_QUESTION_FOR_MARRIAGE_MATCH_MAKING, null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_ASK_A_QUESTION_FOR_MARRIAGE_MATCH_MAKING, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                //  CUtils.getUrlLink(downloadPrintedPdfUrl,getActivity(),LANGUAGE_CODE,0);

               /* Intent bigHorscopeIntent = new Intent(getActivity(), BigHorscopeActivity.class);
                bigHorscopeIntent.putExtra(CGlobalVariables.DataComingFromDownloadPdf, true);
                startActivity(bigHorscopeIntent);*/
                // CUtils.sendToActAskQuestion(activity, CGlobalVariables.ask_A_Question_Android);
                CUtils.getUrlLink("https://buy.astrosage.com/" + CGlobalVariables.ask_A_Question_For_Marriage_Android, activity, LANGUAGE_CODE, 0);
            }
        });
    }

    /**
     * Instance of the DownloadPDF Class
     *
     * @return
     */
    public static DownloadMarriagePDF newInstance(int inner_chart_Style) {
        Bundle bundle = new Bundle();
        bundle.putInt(chart_Style_Tag, inner_chart_Style);
        DownloadMarriagePDF downloadMarriagePDF = new DownloadMarriagePDF();
        downloadMarriagePDF.setArguments(bundle);
        return downloadMarriagePDF;
    }

    /**
     * Instance of the DownloadPDF Class
     *
     * @return
     */
    public static DownloadMarriagePDF newInstance() {
        DownloadMarriagePDF downloadMarriagePDF = new DownloadMarriagePDF();
        return downloadMarriagePDF;
    }

    /**
     * This method is used to call downloadKundliPdf to download the PDF.
     */
    public void downloadPDF() {
        /*if (!checkStoragePermission()) {
            requestExternalStoragePermission(PERMISSION_EXTERNAL_STORAGE);
            return;
        }*/
        pd = new CustomProgressDialog(getActivity(), typeface);
        pd.setCancelable(false);
        pd.show();
        Intent intent = new Intent(getActivity(), DownloadService.class);
        intent.putExtra(KEY_PDF_SHARE, false);
        intent.putExtra(KEY_URL, CUtils.getMatchingPdfUrl(activity,LANGUAGE_CODE));
        intent.putExtra(KEY_RECEIVER, new DownloadReceiver(getActivity(), new Handler()));
        getActivity().startService(intent);

        CUtils.googleAnalyticSendWitPlayServie(getActivity(),
                CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                GOOGLE_ANALYTIC_DOWNLOAD_PDF,
                GOOGLE_ANALYTIC_DOWNLOAD_PDF_MATCHING_LASTSCREEN);
        String labell = GOOGLE_ANALYTIC_DOWNLOAD_PDF + "_" + GOOGLE_ANALYTIC_DOWNLOAD_PDF_MATCHING_LASTSCREEN;
        CUtils.fcmAnalyticsEvents(labell, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

    }

    public void cancelProgressDialog() {
        try {
            if(activity == null) return;
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



    private void requestExternalStoragePermission(int requestCode) {

        if (SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            CUtils.requestForExternalStorageNew(getActivity(), this, requestCode);
        } else {
            CUtils.requestForExternalStorage(getActivity(), this, requestCode);
        }
    }

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
                    Toast.makeText(getActivity(), getResources().getString(R.string.permission_allow), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}
