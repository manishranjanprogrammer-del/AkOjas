package com.ojassoft.astrosage.ui.fragments;

import static com.ojassoft.astrosage.ui.act.ActAppModule.LANGUAGE_CODE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTIC_DOWNLOAD_PDF;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTIC_DOWNLOAD_PDF_KUNDLI_LASTSCREEN;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_CHART_STYLE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_PDF_SHARE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_RECEIVER;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_REPORT_TYPE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_URL;
import static com.ojassoft.astrosage.utils.CGlobalVariables.currentLalitude;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.misc.DownloadPdfReceiver;
import com.ojassoft.astrosage.misc.DownloadPdfService;
import com.ojassoft.astrosage.model.ServicelistModal;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.varta.adapters.ReportAdapter;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Fragment to display and handle report categories and PDF downloads.
 */
public class ReportsFragment extends Fragment {

    private static final String CHART_STYLE_TAG = "chart_Style";
    public static boolean OPEN_FROM_KUNDLI = false;
    private TextView btnBuyNow,btnDownloadPdf;
    private String selectedPDFType = "";
    private int pdfPosition = -1;
    private CustomProgressDialog progressDialog;
    private int chartStyle = 0;
    private RecyclerView reportCategoryList;
    private ArrayList<ServicelistModal> serviceList;
    /**
     * Required empty public constructor.
     */
    public ReportsFragment() {
        // Required empty public constructor
    }

    /**
     * Factory method to create a new instance of ReportsFragment with chart style argument.
     * @param chartStyle The chart style to use for reports.
     * @return A new instance of ReportsFragment.
     */
    public static ReportsFragment newInstance(int chartStyle) {
        Bundle bundle = new Bundle();
        bundle.putInt(CHART_STYLE_TAG, chartStyle);
        ReportsFragment reportsFragment = new ReportsFragment();
        reportsFragment.setArguments(bundle);
        return reportsFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            chartStyle = getArguments().getInt(CHART_STYLE_TAG, 0);
        }
         serviceList = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_reports, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        reportCategoryList = view.findViewById(R.id.recyclerViewReports);
        btnBuyNow = view.findViewById(R.id.btnBuyNow);
        btnDownloadPdf = view.findViewById(R.id.btnDownloadPdf);
        btnDownloadPdf.setOnClickListener(v -> {
                    if (CUtils.isDhruvPlan(requireContext())){
                        downloadPDF();
                    }else {
                        OPEN_FROM_KUNDLI = true;
                        setDataTosendDescription(pdfPosition);
                    }
                }

        );

        reportCategoryList.setLayoutManager(new GridLayoutManager(getContext(), 2));
        String homeScreenResponse = com.ojassoft.astrosage.varta.utils.CUtils.getStringData(requireContext(), com.ojassoft.astrosage.varta.utils.CGlobalVariables.HOME_SCREEN_UI_RESPONSE_KEY, "");
        parseAndDisplayReports(homeScreenResponse);
    }

    /**
     * Parses the home screen response and displays the report categories.
     * @param response The JSON response string from the home screen API.
     */
    private void parseAndDisplayReports(String response) {
        try {
            if (response == null) {
                return;
            }
             serviceList = parseReportGsonData(response);
            if (serviceList != null) {
                ReportAdapter adapter = new ReportAdapter(new ArrayList<>(serviceList), this);
                reportCategoryList.setAdapter(adapter);
            }
        } catch (Exception e) {
            Log.e("ReportsFragment", "parseAndDisplayReports: parsing error: " + e);
        }
    }

    /**
     * Parses the report service details from the JSON response.
     * @param jsonData The JSON string containing report service details.
     * @return ArrayList of ServicelistModal objects or null if parsing fails.
     */
    private ArrayList<ServicelistModal> parseReportGsonData(String jsonData) {
        try {
            Gson gson = new Gson();
            JsonObject rootObj = gson.fromJson(jsonData, JsonObject.class);
            JsonArray reportServiceArray = rootObj.getAsJsonArray("ReportServiceDetails");
            return gson.fromJson(reportServiceArray, new TypeToken<ArrayList<ServicelistModal>>(){}.getType());
        } catch (Exception ex) {
            Log.e("ReportsFragment", "Error in parseReportGsonData: ", ex);
            return null;
        }
    }

    /**
     * Controls the visibility of the Buy Now button based on selection.
     * @param position The position of the selected item, or -1 if none selected.
     * @param title The title of the selected report.
     * @param pdfType The PDF type identifier.
     */
    public void setBuyButtonVisible(int position, String title, int pdfType) {
        if (position != -1) {
            selectedPDFType = String.valueOf(pdfType);
            pdfPosition = position;
            btnDownloadPdf.setVisibility(View.VISIBLE);

            // Log.d("ReportsFragment", "PDF TYPE: " + selectedPDFType);
            if (CUtils.isDhruvPlan(requireContext())){
                btnDownloadPdf.setText(getString(R.string.download_pdf));

            }else {
                btnDownloadPdf.setText(getString(R.string.buy_now));
            }
        } else {
            btnDownloadPdf.setVisibility(View.GONE);

        }
    }

    /**
     * Initiates the download of the selected PDF report.
     */
    public void downloadPDF() {
        Typeface typeface = CUtils.getRobotoFont(requireContext(),
                LANGUAGE_CODE, com.ojassoft.astrosage.utils.CGlobalVariables.regular);
        progressDialog = new CustomProgressDialog(requireContext(), typeface);
        progressDialog.setCancelable(false);
        progressDialog.show();
        boolean isNorthChart = (chartStyle == 0 || chartStyle == 2);
        Intent intent = new Intent(requireContext(), DownloadPdfService.class);
        String pdfUrl;
        if (selectedPDFType.equals("7")){
             pdfUrl = CUtils.getPdfUrl(requireContext(), chartStyle, LANGUAGE_CODE) + "&time=" + (new Date().getTime() + "&pdftype=" + selectedPDFType+ "&pdfSource=" + "brihat");
        }else {
             pdfUrl = CUtils.getPdfUrl(requireContext(), chartStyle, LANGUAGE_CODE) + "&time=" + (new Date().getTime() + "&pdftype=" + selectedPDFType);
        }
        Log.d("ReportsFragment", "PDF URL: " + pdfUrl);
        intent.putExtra(KEY_PDF_SHARE, false);
        intent.putExtra(KEY_CHART_STYLE, chartStyle);
        intent.putExtra(KEY_URL, pdfUrl);
        intent.putExtra(KEY_REPORT_TYPE,selectedPDFType);
        intent.putExtra(KEY_RECEIVER, new DownloadPdfReceiver(requireContext(), new Handler()));
        requireContext().startService(intent);
        CUtils.fcmAnalyticsEvents(com.ojassoft.astrosage.utils.CGlobalVariables.REPORT_ITEM_CLICK_IN_KUNDLI, com.ojassoft.astrosage.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "ReportsFragment");
    }

    /**
     * Opens the deep link URL for the selected report item.
     *
     * @param itemPosition The position of the selected item in the serviceList.
     *                     If invalid, the method returns without action.
     *
     * This method retrieves the selected {@link ServicelistModal} from the list and
     * opens its deep link using {@link CUtils#getUrlLink(String, Activity, String, int)}.
     */
    private void setDataTosendDescription(int itemPosition) {
        if (serviceList == null || itemPosition < 0 ) {
            Log.w("HomeReportItemAdapter", "Invalid item position: " + itemPosition);
            return;
        }
        //Log.w("HomeReportItemAdapter", "item position: " + itemPosition);

        ServicelistModal servicelistModal = serviceList.get(itemPosition);
        //Log.w("HomeReportItemAdapter", "item url: " + serviceList.get(itemPosition).getServiceDeepLinkURL());

        CUtils.getUrlLink(servicelistModal.getServiceDeepLinkURL(), requireActivity(), LANGUAGE_CODE, 0);

        CUtils.fcmAnalyticsEvents(com.ojassoft.astrosage.utils.CGlobalVariables.REPORT_ITEM_CLICK_BUY_NOW, com.ojassoft.astrosage.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "ReportsFragment");

    }

    /**
     * Cancels and dismisses the progress dialog if it is showing.
     */
    public void cancelProgressDialog() {
        try {
            if (getContext() == null) return;
            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();
        } catch (final IllegalArgumentException e) {
            // Do nothing.
        } catch (final Exception e) {
            // Do nothing.
        } finally {
            progressDialog = null;
        }
    }
}