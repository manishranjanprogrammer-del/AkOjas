package com.ojassoft.astrosage.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.Html;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.L;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.controller.ControllerManager;
import com.ojassoft.astrosage.customadapters.ModuleBoardListAdapter;
import com.ojassoft.astrosage.misc.CGenerateAppViews;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.act.OutputMasterActivity;
import com.ojassoft.astrosage.ui.customviews.common.ViewDrawRotateKundli;
import com.ojassoft.astrosage.ui.customviews.predictions.WebViewPredictions;
import com.ojassoft.astrosage.utils.CGlobal;
import com.ojassoft.astrosage.utils.CScreenConstants;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.FontUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;
import java.util.zip.Inflater;


public class ShareKundliMessageFragment extends DialogFragment {
    public static final String BIRTH_DETAILS = "birthDetails";
    public static final String QUESTION = "question";
    public static final String ANSWER = "answer";
    public static final String TAG = "ShareMessageFragment";
    View containerView;
    String birthDetails;
    String getQuestion = "";
    String getAnswer = "";
    int kundliModule = -1,kundliSubModule=-1;
    CScreenConstants SCREEN_CONSTANTS;
    int chart_Style;
    Activity activity;
    private boolean isShared = false;
    File cachePath;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_Astrosage);
        if(getArguments() == null)
            dismiss();

        birthDetails = getArguments().getString(BIRTH_DETAILS);
        getAnswer = getArguments().getString(ANSWER);
        getQuestion = getArguments().getString(QUESTION);
        kundliModule = getArguments().getInt(CGlobalVariables.KEY_MODULE_ID,-1);
        kundliSubModule = getArguments().getInt(CGlobalVariables.KEY_SUB_MODULE_ID,-1);
        Log.d("Question", "KEY_MODULE_ID= "+ kundliModule+" KEY_SUB_MODULE_ID= " +kundliSubModule);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        containerView =  inflater.inflate(R.layout.fragment_share_kundli_message, container, false);
        try {
            activity = requireActivity();
            TextView tvQuestion = containerView.findViewById(R.id.tv_question);
            TextView tvAnswer = containerView.findViewById(R.id.tv_answer);
            LinearLayout transit_lyout = containerView.findViewById(R.id.ll_transit_button);
            FontUtils.changeFont(activity, tvQuestion, "fonts/Roboto-Bold.ttf");
            FontUtils.changeFont(activity, tvAnswer, "fonts/Roboto-Regular.ttf");
            int lang = CUtils.getIntData(requireActivity(), com.ojassoft.astrosage.utils.CGlobalVariables.app_language_key, com.ojassoft.astrosage.utils.CGlobalVariables.ENGLISH);
            //Display size calculation
            DisplayMetrics displaymetrics = new DisplayMetrics();
            activity.getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
            int width = displaymetrics.widthPixels-CUtils.convertDpToPx(activity,5);
            //TypeFace
            Typeface regularTypeface = CUtils.getRobotoFont(activity,lang , com.ojassoft.astrosage.utils.CGlobalVariables.regular);
            Typeface mediumTypeface = CUtils.getRobotoFont(activity,lang , com.ojassoft.astrosage.utils.CGlobalVariables.medium);
            //view size
            if(kundliModule == com.ojassoft.astrosage.utils.CGlobalVariables.MODULE_BASIC  && (kundliSubModule == com.ojassoft.astrosage.utils.CGlobalVariables.SUB_MODULE_BASIC_LAGNA_CHART || kundliSubModule == 13))
                SCREEN_CONSTANTS = new CScreenConstants(activity,regularTypeface);
            else
                SCREEN_CONSTANTS = new CScreenConstants(activity,width,regularTypeface);
            //chart_Style
            SharedPreferences sharedPreferences = activity.getSharedPreferences(
                    com.ojassoft.astrosage.utils.CGlobalVariables.APP_PREFS_NAME, Context.MODE_PRIVATE);
            chart_Style =  sharedPreferences.getInt(com.ojassoft.astrosage.utils.CGlobalVariables.APP_PREFS_ChartStyle,
                    com.ojassoft.astrosage.utils.CGlobalVariables.CHART_NORTH_STYLE);
            //kundli view
            View kundliView;
            if(kundliModule == com.ojassoft.astrosage.utils.CGlobalVariables.MODULE_BASIC  &&   kundliSubModule == 13) {
                kundliView = getTransitView(lang);
                transit_lyout.setVisibility(View.VISIBLE);
            }else{
                kundliView = CGenerateAppViews.getViewFor(kundliModule, kundliSubModule, activity, mediumTypeface,
                        chart_Style, lang, SCREEN_CONSTANTS, 0);
            }
            //add view
            LinearLayout relativeLayout = containerView.findViewById(R.id.rel_layout);
            if (kundliView != null) {
                if (kundliView.getTag() != null) {

                    try {
                        relativeLayout.addView(new WebViewPredictions(getActivity(), kundliView.getTag().toString(),(view,url)->startSharing()));
                    } catch (Exception e) {
                        kundliView = new TextView(getActivity());
                        ((TextView) kundliView).setText(e.getMessage());
                        relativeLayout.addView(kundliView);
                    }

                } else {
                    kundliView.setOnTouchListener((v, event) -> true);
                    relativeLayout.addView(kundliView);
                   startSharing();
                }
            }else {
                Log.d("Question", "kundliView ==  null");
                Toast.makeText(activity, "Unable to load view", Toast.LENGTH_SHORT).show();
                dismiss();
            }
            TextView tv_birth_details = containerView.findViewById(R.id.tv_birth_details);

            if(birthDetails != null && !birthDetails.isEmpty()){
                tv_birth_details.setVisibility(View.VISIBLE);
            }else {
                tv_birth_details.setVisibility(View.GONE);
            }
            tv_birth_details.setText(birthDetails);

            if (getQuestion.isEmpty()) {
                tvQuestion.setVisibility(View.GONE);
            } else {
                tvQuestion.setVisibility(View.VISIBLE);
            }
            tvQuestion.setText(getQuestion);

            // Set the formatted text to the TextView
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
                tvAnswer.setText(Html.fromHtml(getAnswer, Html.FROM_HTML_MODE_LEGACY));
            else
                tvAnswer.setText(Html.fromHtml(getAnswer));

            containerView.setOnClickListener((v)->{

            });
        }catch(Exception e){
            e.printStackTrace();
        }

        return containerView;
    }

    public static ShareKundliMessageFragment getInstance(String question, String answer,int kundliModule,int kundliSubModule){
        Bundle bundle = new Bundle();
        bundle.putString(QUESTION, question);
        bundle.putString(ANSWER, answer);
        bundle.putInt(CGlobalVariables.KEY_MODULE_ID,kundliModule);
        bundle.putInt(CGlobalVariables.KEY_SUB_MODULE_ID,kundliSubModule);
        ShareKundliMessageFragment fragment = new ShareKundliMessageFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
    public static ShareKundliMessageFragment getInstance(String question, String answer,int kundliModule,int kundliSubModule,String birthDetails){
        Bundle bundle = new Bundle();
        bundle.putString(QUESTION, question);
        bundle.putString(ANSWER, answer);
        bundle.putInt(CGlobalVariables.KEY_MODULE_ID,kundliModule);
        bundle.putInt(CGlobalVariables.KEY_SUB_MODULE_ID,kundliSubModule);
        bundle.putString(BIRTH_DETAILS, birthDetails);
        ShareKundliMessageFragment fragment = new ShareKundliMessageFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    private void startSharing() {
        try {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                if(!isShared) {
                    shareImage(getScreenShot());
                    isShared = true;
                }
            }, 1000);
        }catch(Exception e){
            shareImage(getScreenShot());
            isShared = true;
        }
    }



    public  Bitmap getScreenShot() {
        if(containerView == null) return null;
        ScrollView scrollView = containerView.findViewById(R.id.container_layout);
        int totalHeight = scrollView.getChildAt(0).getHeight();
        int totalWidth = scrollView.getChildAt(0).getWidth();
        Bitmap bitmap = Bitmap.createBitmap(totalWidth, totalHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        containerView.draw(canvas);
        return bitmap;
    }

    private void shareImage(Bitmap bitmap) {
        try {
            cachePath = new File(requireContext().getCacheDir(), "images/");
            cachePath.mkdirs(); // Create the directory if it doesn't exist
            File file = new File(cachePath, "shared_image.png");
            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream); // Save the bitmap
            stream.close();

            // Get the URI of the saved file
            Uri uri = FileProvider.getUriForFile(requireContext(),
                    requireContext().getPackageName(),
                    file);

            // Prepare the share intent
            String body = requireContext().getString(R.string.shareAppBody);
            if (uri != null) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, body);
                shareIntent.setType("image/*");
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // Grant URI permission
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(shareIntent, "Share Image on Social Media"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void clearCache() {
        if (cachePath != null && cachePath.isDirectory()) {
            File[] files = cachePath.listFiles();
            if (files != null) {
                for (File file : files) {
                    file.delete();
                }
            }
        }
    }

    private View getTransitView(int lang){
        final RelativeLayout layout = new RelativeLayout(requireContext());
        ControllerManager objControllManager = new ControllerManager();
        boolean isTablet = CUtils.isTablet(requireActivity());
        View view = null;
        try {

            int[] Transit = objControllManager.getTransitKundliPlanetsRashiArray(CGlobal.getCGlobalObject().getPlanetDataObjectTransit());
            double[] TransitArray = objControllManager.getTransitPlanetsDegreeArray(CGlobal.getCGlobalObject().getPlanetDataObjectTransit());

            int[] transitLangnaArrayInRashi = objControllManager.getLagnaKundliPlanetsRashiArray(CGlobal.getCGlobalObject().getPlanetDataObject());
            double[] langnaArrayInRashi = objControllManager.getPlanetsDegreeArray(CGlobal.getCGlobalObject().getPlanetDataObject());
            if (TransitArray.length >= 12 && langnaArrayInRashi.length >= 12)
                Transit[12] = transitLangnaArrayInRashi[12];

            if (chart_Style == 0)
                view = new ViewDrawRotateKundli(requireContext(), requireContext().getResources().getStringArray(R.array.VarChartPlanets), Transit, TransitArray, com.ojassoft.astrosage.utils.CGlobalVariables.enuKundliType.NORTH, isTablet, SCREEN_CONSTANTS, com.ojassoft.astrosage.utils.CGlobalVariables.SUB_MODULE_BASIC_LAGNA_CHART, lang, transitLangnaArrayInRashi, true);
            else if (chart_Style == 1) {
                view = new ViewDrawRotateKundli(requireContext(), requireContext().getResources().getStringArray(R.array.VarChartPlanets), Transit, TransitArray, com.ojassoft.astrosage.utils.CGlobalVariables.enuKundliType.SOUTH, isTablet, SCREEN_CONSTANTS, com.ojassoft.astrosage.utils.CGlobalVariables.SUB_MODULE_BASIC_LAGNA_CHART, lang, transitLangnaArrayInRashi, true);
            } else if (chart_Style == 2) {
                view = new ViewDrawRotateKundli(requireContext(), requireContext().getResources().getStringArray(R.array.VarChartPlanets), Transit, TransitArray, com.ojassoft.astrosage.utils.CGlobalVariables.enuKundliType.EAST, isTablet, SCREEN_CONSTANTS, com.ojassoft.astrosage.utils.CGlobalVariables.SUB_MODULE_BASIC_LAGNA_CHART, lang, transitLangnaArrayInRashi, true);
            }
        }catch (Exception ignore){}
        if(view != null) {
            view.setLayoutParams(new ViewGroup.LayoutParams((int) SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) SCREEN_CONSTANTS.DeviceScreenHeight - 400));
            layout.addView(view);
        }else {
            return null;
        }

        return layout;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(isShared){
            clearCache();
            dismiss();
        }
    }
}