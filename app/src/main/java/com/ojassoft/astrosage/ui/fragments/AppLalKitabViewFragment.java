package com.ojassoft.astrosage.ui.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.jinterface.IOpenInputYearCalendar;
import com.ojassoft.astrosage.misc.CGenerateAppViews;
import com.ojassoft.astrosage.ui.act.OutputMasterActivity;
import com.ojassoft.astrosage.ui.customviews.predictions.WebViewPredictions;
import com.ojassoft.astrosage.utils.CGlobal;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

public class AppLalKitabViewFragment extends Fragment {
    View _view = null;
    int year;
    IOpenInputYearCalendar iOpenInputYearCalendar;
    int moduleId;
    int subModuleId;
    int lalKitabInputYear;
    int languageIndex;
    Typeface typeface;

    public static AppLalKitabViewFragment newInstance(int moduleId, int subModuleId, int lalKitabInputYear) {
        AppLalKitabViewFragment appViewFragment = new AppLalKitabViewFragment();

        Bundle args = new Bundle();
        args.putInt("moduleId", moduleId);
        args.putInt("subModuleId", subModuleId);
        args.putInt("lalKitabInputYear", lalKitabInputYear);
        appViewFragment.setArguments(args);

        return appViewFragment;
    }

    public AppLalKitabViewFragment() {
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        moduleId = getArguments().getInt("moduleId", 0);
        subModuleId = getArguments().getInt("subModuleId", 0);
        lalKitabInputYear = getArguments().getInt("lalKitabInputYear", 0);
        SharedPreferences sharedPreferencesForLang = getActivity().getSharedPreferences(CGlobalVariables.MYKUNDLI_LANGUAGE_PREFS_NAME, Context.MODE_PRIVATE);
        languageIndex = sharedPreferencesForLang.getInt(CGlobalVariables.APP_PREFS_AppLanguage, 0);

        typeface = CUtils.getRobotoFont(getActivity(), languageIndex, CGlobalVariables.regular);

        View view = inflater.inflate(R.layout.lay_lalkitab, container, false);
        View llLalkitabTitle = (View) view.findViewById(R.id.date_view);
        LinearLayout linearLayout = ((LinearLayout) view.findViewById(R.id.llLalkitabContent));
        if (!(subModuleId == CGlobalVariables.SUB_MODULE_LALKITAB_VARSHA_CHART)) {
            llLalkitabTitle.setVisibility(View.GONE);
        }
        TextView tvLalkitabYearTitle = (TextView) view.findViewById(R.id.tvTajikYearTitle);
        year = lalKitabInputYear + CGlobal.getCGlobalObject().getHoroPersonalInfoObject().getDateTime().getYear();
        tvLalkitabYearTitle.setText(getYearText(year));
        tvLalkitabYearTitle.setTypeface(typeface);
        tvLalkitabYearTitle.setOnClickListener(v -> {
            //((OutputMasterActivity) getActivity()).initMonthPicker(year);
            ((OutputMasterActivity) getActivity()).showYearPicker(year);
        });

        //tvLalkitabYearTitle.setTextColor(((OutputMasterActivity) getActivity()).SCREEN_CONSTANTS.HeadingColor.getColor());
        ProgressBar progressBar = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyle);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        params.gravity = Gravity.CENTER;
        progressBar.setLayoutParams(params);
        linearLayout.addView(progressBar);
        progressBar.setVisibility(View.GONE);


        if (_view == null) {
            try {
                _view = CGenerateAppViews.getViewFor(moduleId, subModuleId, getActivity(), typeface,
                        ((OutputMasterActivity) getActivity()).chart_Style, languageIndex,
                        ((OutputMasterActivity) getActivity()).SCREEN_CONSTANTS, lalKitabInputYear);
            } catch (Exception e) {
                // Handle exception and log
                Log.e("WebView Error", "Error while generating view: " + e.getMessage());
            }
        }

        // Check if _view has a tag and add the WebView dynamically
        if (_view != null && _view.getTag() != null) {
            try {
                WebView webView = getWebView(progressBar);

                // Load the URL or HTML content from the tag
                String urlOrHtmlContent = _view.getTag().toString();
                webView.loadUrl(urlOrHtmlContent);

                // Add WebView to the linear layout
                _view = webView;

            } catch (Exception e) {
                // If WebView fails, show error message in a TextView
                _view = new TextView(getActivity());
                ((TextView) _view).setText(e.getMessage());

            }
        }

        linearLayout.addView(_view);



       // Log.e("fragCheck ", " WVP onCreateView() " + view.toString());

        return view;
    }

    @SuppressLint("SetJavaScriptEnabled")
    private WebView getWebView(ProgressBar progressBar) {
        WebView webView = new WebView(getActivity());
        webView.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));

        // Set WebViewClient to handle page loading inside the WebView

        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                progressBar.setVisibility(View.VISIBLE);  // Show the ProgressBar when loading starts
            }

            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                progressBar.setVisibility(View.GONE);  // Hide the ProgressBar when loading finishes
            }
        });
        webView.getSettings().setJavaScriptEnabled(true);
       // webView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        webView.clearCache(true);
        //this.getSettings().setBuiltInZoomControls(true);
        //this.getSettings().setSupportZoom(true);
        webView.setWebChromeClient(new WebChromeClient() {
            public void onProgressChanged(WebView view, int progress) {

            }
        });
        return webView;
    }

    private String getYearText(int year) {
        int iYear = year;
        int monthNumber = CGlobal.getCGlobalObject().getHoroPersonalInfoObject().getDateTime().getMonth();
        StringBuilder sb = new StringBuilder();
        String monthName = getActivity().getResources().getStringArray(R.array.month_short_name_list)[monthNumber];
        sb.append(monthName + " " + String.valueOf(iYear) + " " + getActivity().getResources().getString(R.string.desh_character) + " ");
        sb.append(monthName + " " + String.valueOf(iYear + 1));
        return sb.toString();
    }

    private void fireCalenderOpenEvent() {
        iOpenInputYearCalendar.openInputYearCalendar();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        iOpenInputYearCalendar = (IOpenInputYearCalendar) activity;
       // Log.e("fragCheck ", " WVP onAttach()  " );
    }

    @Override
    public void onDetach() {
        super.onDetach();
        iOpenInputYearCalendar=null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (_view != null) {
            ViewGroup parentViewGroup = (ViewGroup) _view.getParent();
            if (parentViewGroup != null) {
                parentViewGroup.removeAllViews();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.e("fragCheck ", " WVP onStart()  " );
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("fragCheck ", " WVP onResume()  " );
    }
}
