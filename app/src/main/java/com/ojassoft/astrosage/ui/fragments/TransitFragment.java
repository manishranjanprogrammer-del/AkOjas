package com.ojassoft.astrosage.ui.fragments;


import static com.ojassoft.astrosage.ui.act.ActAppModule.LANGUAGE_CODE;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanDateTime;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.beans.BeanPlace;
import com.ojassoft.astrosage.controller.ControllerManager;
import com.ojassoft.astrosage.jinterface.ICategoryPrediction;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.OutputMasterActivity;
import com.ojassoft.astrosage.ui.customviews.common.ViewDrawRotateKundli;
import com.ojassoft.astrosage.utils.CGlobal;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.PanchangUtil;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by ojas-02 on 12/10/16.
 */
public class TransitFragment extends Fragment {


    //public int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    public int chart_Style = 0;
    Context context;
    int languageIndex;
    Typeface typeface;
    LinearLayout linearLayout;
    CustomProgressDialog pd = null;
    View view = null;
    LinearLayout linearLayoutToAttach;
    Activity activity;
    ControllerManager objControllManager = new ControllerManager();
    FrameLayout frameLayout;
    BeanHoroPersonalInfo beanHoroPersonalInfo;
    TextView lagnaBtn;
    TextView chandraBtn;
    int lagnaPos = 12;
    private Typeface mediumTypeface;
    private boolean IS_APP_ONLINE = true;
    private ICategoryPrediction iCategoryPrediction;

    public TransitFragment() {
        setRetainInstance(true);
    }

    public static Fragment newInstance(BeanHoroPersonalInfo beanHoroPersonalInfo) {
        TransitFragment transitFragment = new TransitFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("beanHoroPersonalInfo", beanHoroPersonalInfo);
        transitFragment.setArguments(bundle);
        return transitFragment;
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        //Log.e("SAN ", " Transit onAttach() ");
        iCategoryPrediction = (ICategoryPrediction) activity;
        this.activity = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Log.e("SAN ", " Transit onCreate() ");
        beanHoroPersonalInfo = (BeanHoroPersonalInfo) getArguments().getSerializable("beanHoroPersonalInfo");
        LANGUAGE_CODE = CUtils.getLanguageCodeFromPreference(getActivity());
        mediumTypeface = CUtils.getRobotoFont( getActivity(), LANGUAGE_CODE, CGlobalVariables.medium );
        SharedPreferences sharedPreferencesForLang = getActivity().getSharedPreferences(CGlobalVariables.MYKUNDLI_LANGUAGE_PREFS_NAME, Context.MODE_PRIVATE);
        languageIndex = sharedPreferencesForLang.getInt(CGlobalVariables.APP_PREFS_AppLanguage, 0);
        typeface = CUtils.getRobotoFont(getActivity(), LANGUAGE_CODE, CGlobalVariables.regular);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //view=new View(getActivity());

        view = inflater.inflate(R.layout.transit_kundali, container, false);
        linearLayoutToAttach = (LinearLayout) view.findViewById(R.id.transitView);
     /*   final RelativeLayout v = new RelativeLayout(getActivity());
        v.setBackgroundColor(getResources().getColor(R.color.white));
        v.setVerticalScrollBarEnabled(true);
        v.setId("VI".hashCode());*/


        frameLayout = (FrameLayout) view.findViewById(R.id.frameProgress);
        calculateOnlineData(getBeanHoroscopeForTransit(beanHoroPersonalInfo));
        lagnaBtn = view.findViewById(R.id.lagna_btn);
        chandraBtn = view.findViewById(R.id.chandra_btn);
        // by default
        ViewCompat.setBackgroundTintList(lagnaBtn, ContextCompat.getColorStateList(activity, R.color.colorPrimary_day_night));
        lagnaBtn.setTextColor(getResources().getColor(R.color.white));
        ViewCompat.setBackgroundTintList(chandraBtn, ContextCompat.getColorStateList(activity, R.color.lightt_gray));
        chandraBtn.setTextColor(getResources().getColor(R.color.no_change_black));

        lagnaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewCompat.setBackgroundTintList(lagnaBtn, ContextCompat.getColorStateList(activity, R.color.colorPrimary_day_night));
                //lagnaBtn.setBackgroundTintList(ContextCompat.getColorStateList(activity, R.color.orange));
                lagnaBtn.setTextColor(getResources().getColor(R.color.white));
                ViewCompat.setBackgroundTintList(chandraBtn, ContextCompat.getColorStateList(activity, R.color.lightt_gray));
                //chandraBtn.setBackgroundTintList(ContextCompat.getColorStateList(activity, R.color.lightt_gray));
                chandraBtn.setTextColor(getResources().getColor(R.color.no_change_black));

                //Log.e("SAN ", " TransitFragment lagnaBtn.setOnClickListener " );

                displayTransitKundli(1);
            }
        });
        chandraBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ViewCompat.setBackgroundTintList(chandraBtn, ContextCompat.getColorStateList(activity, R.color.colorPrimary_day_night));
                //chandraBtn.setBackgroundTintList(ContextCompat.getColorStateList(activity, R.color.orange));
                chandraBtn.setTextColor(getResources().getColor(R.color.white));
                ViewCompat.setBackgroundTintList(lagnaBtn, ContextCompat.getColorStateList(activity, R.color.lightt_gray));
                //lagnaBtn.setBackgroundTintList(ContextCompat.getColorStateList(activity, R.color.lightt_gray));
                lagnaBtn.setTextColor(getResources().getColor(R.color.no_change_black));

                //Log.e("SAN ", " TransitFragment chandraBtn.setOnClickListener " );

                displayTransitKundli(2);
            }
        });

        return view;
    }

    private void calculateOnlineData(BeanHoroPersonalInfo beanHoroPersonalInfo) {

        IS_APP_ONLINE = CUtils.getUserPurchasedPlanFromPreference(activity) != 1;
        ControllerManager _controllerManager = new ControllerManager(TransitFragment.this, getActivity());
        try {
            _controllerManager.calculateTransitKundliData(beanHoroPersonalInfo,
                    IS_APP_ONLINE, CUtils.isConnectedWithInternet(getActivity()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void showProgressbar() {
        pd = new CustomProgressDialog(getActivity(), typeface);
        pd.show();
    }

    public void dismissProgressbar() {
       /* try {
            if (pd != null & pd.isShowing())
                pd.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        frameLayout.setVisibility(View.GONE);
    }


   /* public void displayTransitKundli(int chartType) {
        try {
            View viewScroll = null;
            int LANGUAGECODE = ((AstrosageKundliApplication) getActivity().getApplication()).getLanguageCode();
            int chartstyle = -1;
            if (chartstyle == -1) {
                chartstyle = CUtils.getChartStyleFromPreference(getActivity());
                chart_Style = chartstyle;
            } else {
                chart_Style = 0;
            }

            boolean isTablet = CUtils.isTablet(getActivity());
            int[] Transit = new int[13];
            double[] TransitArray = new double[13];
            int[] transitLangnaArrayInRashi = new int[13];
            double[] langnaArrayInRashi = new double[13];
            try {
                Transit = objControllManager.getTransitKundliPlanetsRashiArray(CGlobal.getCGlobalObject().getPlanetDataObjectTransit());
                transitLangnaArrayInRashi = objControllManager.getLagnaKundliPlanetsRashiArray(CGlobal.getCGlobalObject().getPlanetDataObject());
                TransitArray = objControllManager.getTransitPlanetsDegreeArray(CGlobal.getCGlobalObject().getPlanetDataObjectTransit());
                langnaArrayInRashi = objControllManager.getPlanetsDegreeArray(CGlobal.getCGlobalObject().getPlanetDataObject());
                if (TransitArray.length >= 12 && langnaArrayInRashi.length >= 12) {
                    if (chartType == 1) {
                        Transit[12] = Transit[12];
                    } else {
                        Transit[12] = Transit[1];
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            context = getActivity();
            if (chart_Style == 0) {
                final RelativeLayout layout = new RelativeLayout(context);
                final ScrollView scrollView = new ScrollView(context);
                layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                scrollView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//End
                //View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), Transit, CGlobalVariables.enuKundliType.NORTH, isTablet, ((OutputMasterActivity) getActivity()).SCREEN_CONSTANTS, -1, 0, "SWAMSA", TransitArray);

                //View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), Transit, CGlobalVariables.enuKundliType.NORTH, isTablet, ((OutputMasterActivity) getActivity()).SCREEN_CONSTANTS, CGlobalVariables.SUB_MODULE_BASIC_LAGNA_CHART, 0,TransitArray);

                if (chartType == 1) {
                    lagnaPos = 12;
                } else {
                    lagnaPos = 1;
                }
                View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), Transit, TransitArray, CGlobalVariables.enuKundliType.NORTH, isTablet, ((OutputMasterActivity) getActivity()).SCREEN_CONSTANTS, CGlobalVariables.SUB_MODULE_BASIC_LAGNA_CHART, LANGUAGECODE, transitLangnaArrayInRashi, lagnaPos);
                resultView.setLayoutParams(new ViewGroup.LayoutParams((int) ((OutputMasterActivity) getActivity()).SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) ((OutputMasterActivity) getActivity()).SCREEN_CONSTANTS.DeviceScreenHeight));
                layout.addView(resultView);
                scrollView.addView(layout);
                viewScroll = scrollView;

            } else if (chart_Style == 1) {
                final RelativeLayout layout = new RelativeLayout(context);
                final ScrollView scrollView = new ScrollView(context);
                layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                scrollView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//End
                //View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), Transit, CGlobalVariables.enuKundliType.NORTH, isTablet, ((OutputMasterActivity) getActivity()).SCREEN_CONSTANTS, -1, 0, "SWAMSA", TransitArray);

                //View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), Transit, CGlobalVariables.enuKundliType.NORTH, isTablet, ((OutputMasterActivity) getActivity()).SCREEN_CONSTANTS, CGlobalVariables.SUB_MODULE_BASIC_LAGNA_CHART, 0,TransitArray);
                View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), Transit, TransitArray, CGlobalVariables.enuKundliType.SOUTH, isTablet, ((OutputMasterActivity) getActivity()).SCREEN_CONSTANTS, CGlobalVariables.SUB_MODULE_BASIC_LAGNA_CHART, LANGUAGECODE, transitLangnaArrayInRashi, lagnaPos);
                resultView.setLayoutParams(new ViewGroup.LayoutParams((int) ((OutputMasterActivity) getActivity()).SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) ((OutputMasterActivity) getActivity()).SCREEN_CONSTANTS.DeviceScreenHeight));
                layout.addView(resultView);
                scrollView.addView(layout);
                viewScroll = scrollView;
                linearLayoutToAttach.addView(viewScroll);
            } else if (chart_Style == 2) {
                final RelativeLayout layout = new RelativeLayout(context);
                final ScrollView scrollView = new ScrollView(context);
                layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                scrollView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//End
                //View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), Transit, CGlobalVariables.enuKundliType.NORTH, isTablet, ((OutputMasterActivity) getActivity()).SCREEN_CONSTANTS, -1, 0, "SWAMSA", TransitArray);

                //View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), Transit, CGlobalVariables.enuKundliType.NORTH, isTablet, ((OutputMasterActivity) getActivity()).SCREEN_CONSTANTS, CGlobalVariables.SUB_MODULE_BASIC_LAGNA_CHART, 0,TransitArray);
                View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), Transit, TransitArray, CGlobalVariables.enuKundliType.EAST, isTablet, ((OutputMasterActivity) getActivity()).SCREEN_CONSTANTS, CGlobalVariables.SUB_MODULE_BASIC_LAGNA_CHART, LANGUAGECODE, transitLangnaArrayInRashi, lagnaPos);
                resultView.setLayoutParams(new ViewGroup.LayoutParams((int) ((OutputMasterActivity) getActivity()).SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) ((OutputMasterActivity) getActivity()).SCREEN_CONSTANTS.DeviceScreenHeight));
                layout.addView(resultView);
                scrollView.addView(layout);
                viewScroll = scrollView;
                linearLayoutToAttach.addView(viewScroll);
            }
            if (viewScroll != null) {
                linearLayoutToAttach.removeAllViews();
                linearLayoutToAttach.addView(viewScroll);
            }

        } catch (Exception ex) {
            //
        }
    }*/

    public void displayTransitKundli(int chartType) {
        try {

            //Log.e("SAN ", " TransitFragment displayTransitKundli()" );

            View viewScroll = null;
            int LANGUAGECODE = ((AstrosageKundliApplication) getActivity().getApplication()).getLanguageCode();
            //Log.e("SAN ", " TransitFragment displayTransitKundli() LANGUAGECODE " + LANGUAGECODE );
            int chartstyle = -1;
            if (chartstyle == -1) {
                chartstyle = CUtils.getChartStyleFromPreference(getActivity());
                chart_Style = chartstyle;
            } else {
                chart_Style = 0;
            }

            boolean isTablet = CUtils.isTablet(getActivity());
            int[] Transit = new int[13];
            double[] TransitArray = new double[13];
            int[] transitLangnaArrayInRashi = new int[13];
            double[] langnaArrayInRashi = new double[13];
            try {
                if (chartType == 1) {
                    Transit = objControllManager.getTransitKundliPlanetsRashiArray(CGlobal.getCGlobalObject().getPlanetDataObjectTransit());
                    TransitArray = objControllManager.getTransitPlanetsDegreeArray(CGlobal.getCGlobalObject().getPlanetDataObjectTransit());

                    transitLangnaArrayInRashi = objControllManager.getLagnaKundliPlanetsRashiArray(CGlobal.getCGlobalObject().getPlanetDataObject());
                    langnaArrayInRashi = objControllManager.getPlanetsDegreeArray(CGlobal.getCGlobalObject().getPlanetDataObject());
                    if (TransitArray.length >= 12 && langnaArrayInRashi.length >= 12)
                        Transit[12] = transitLangnaArrayInRashi[12];
                } else {
                    Transit = objControllManager.getTransitKundliPlanetsRashiArrayForMoon(CGlobal.getCGlobalObject().getPlanetDataObjectTransit());
                    TransitArray = objControllManager.getTransitPlanetsDegreeArray(CGlobal.getCGlobalObject().getPlanetDataObjectTransit());

                    transitLangnaArrayInRashi = objControllManager.getChandraKundliPlanetsRashiArray(CGlobal.getCGlobalObject().getPlanetDataObject());
                    langnaArrayInRashi = objControllManager.getPlanetsDegreeArray(CGlobal.getCGlobalObject().getPlanetDataObject());

                    if (TransitArray.length >= 12 && langnaArrayInRashi.length >= 12)
                        Transit[12] = transitLangnaArrayInRashi[12];
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            context = getActivity();
            //Log.e("SAN ", " TransitFragment displayTransitKundli() ViewDrawRotateKundli() LANGUAGE_CODE " + LANGUAGE_CODE );
            LANGUAGE_CODE = CUtils.getLanguageCodeFromPreference(context);
            CUtils.getRobotoFont(context, LANGUAGE_CODE, CGlobalVariables.regular);

            if (chart_Style == 0) {
                final RelativeLayout layout = new RelativeLayout(context);
                final ScrollView scrollView = new ScrollView(context);
                layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                scrollView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//End
                //View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), Transit, CGlobalVariables.enuKundliType.NORTH, isTablet, ((OutputMasterActivity) getActivity()).SCREEN_CONSTANTS, -1, 0, "SWAMSA", TransitArray);

                //View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), Transit, CGlobalVariables.enuKundliType.NORTH, isTablet, ((OutputMasterActivity) getActivity()).SCREEN_CONSTANTS, CGlobalVariables.SUB_MODULE_BASIC_LAGNA_CHART, 0,TransitArray);
                //Log.e("SAN ", " TransitFragment displayTransitKundli() ViewDrawRotateKundli() 1 " );

                View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), Transit, TransitArray, CGlobalVariables.enuKundliType.NORTH, isTablet, ((OutputMasterActivity) getActivity()).SCREEN_CONSTANTS, CGlobalVariables.SUB_MODULE_BASIC_LAGNA_CHART, LANGUAGECODE, transitLangnaArrayInRashi, true);
                resultView.setLayoutParams(new ViewGroup.LayoutParams((int) ((OutputMasterActivity) getActivity()).SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) ((OutputMasterActivity) getActivity()).SCREEN_CONSTANTS.DeviceScreenHeight));
                layout.addView(resultView);
                scrollView.addView(layout);
                viewScroll = scrollView;
                linearLayoutToAttach.addView(viewScroll);
            } else if (chart_Style == 1) {
                final RelativeLayout layout = new RelativeLayout(context);
                final ScrollView scrollView = new ScrollView(context);
                layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                scrollView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//End
                //View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), Transit, CGlobalVariables.enuKundliType.NORTH, isTablet, ((OutputMasterActivity) getActivity()).SCREEN_CONSTANTS, -1, 0, "SWAMSA", TransitArray);

                //View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), Transit, CGlobalVariables.enuKundliType.NORTH, isTablet, ((OutputMasterActivity) getActivity()).SCREEN_CONSTANTS, CGlobalVariables.SUB_MODULE_BASIC_LAGNA_CHART, 0,TransitArray);
                //Log.e("SAN ", " TransitFragment displayTransitKundli() ViewDrawRotateKundli() 2 " );
                View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), Transit, TransitArray, CGlobalVariables.enuKundliType.SOUTH, isTablet, ((OutputMasterActivity) getActivity()).SCREEN_CONSTANTS, CGlobalVariables.SUB_MODULE_BASIC_LAGNA_CHART, LANGUAGECODE, transitLangnaArrayInRashi, true);
                resultView.setLayoutParams(new ViewGroup.LayoutParams((int) ((OutputMasterActivity) getActivity()).SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) ((OutputMasterActivity) getActivity()).SCREEN_CONSTANTS.DeviceScreenHeight));
                layout.addView(resultView);
                scrollView.addView(layout);
                viewScroll = scrollView;

                linearLayoutToAttach.addView(viewScroll);
            } else if (chart_Style == 2) {
                final RelativeLayout layout = new RelativeLayout(context);
                final ScrollView scrollView = new ScrollView(context);
                layout.setLayoutParams(new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT));
                scrollView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
//End
                //View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), Transit, CGlobalVariables.enuKundliType.NORTH, isTablet, ((OutputMasterActivity) getActivity()).SCREEN_CONSTANTS, -1, 0, "SWAMSA", TransitArray);

                //View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), Transit, CGlobalVariables.enuKundliType.NORTH, isTablet, ((OutputMasterActivity) getActivity()).SCREEN_CONSTANTS, CGlobalVariables.SUB_MODULE_BASIC_LAGNA_CHART, 0,TransitArray);
                //Log.e("SAN ", " TransitFragment displayTransitKundli() ViewDrawRotateKundli() 3 " );
                View resultView = new ViewDrawRotateKundli(context, context.getResources().getStringArray(R.array.VarChartPlanets), Transit, TransitArray, CGlobalVariables.enuKundliType.EAST, isTablet, ((OutputMasterActivity) getActivity()).SCREEN_CONSTANTS, CGlobalVariables.SUB_MODULE_BASIC_LAGNA_CHART, LANGUAGECODE, transitLangnaArrayInRashi, true);
                resultView.setLayoutParams(new ViewGroup.LayoutParams((int) ((OutputMasterActivity) getActivity()).SCREEN_CONSTANTS.DeviceScreenWidth + 3, (int) ((OutputMasterActivity) getActivity()).SCREEN_CONSTANTS.DeviceScreenHeight));
                layout.addView(resultView);
                scrollView.addView(layout);
                viewScroll = scrollView;
                linearLayoutToAttach.addView(viewScroll);
            }
            if (viewScroll != null) {
                linearLayoutToAttach.removeAllViews();
                linearLayoutToAttach.addView(viewScroll);
            }
        } catch (Exception ex) {
            //
        }
    }

    private BeanHoroPersonalInfo getBeanHoroscopeForTransit(BeanHoroPersonalInfo beanHoroPersonalInfoObj) {


        int seconds, min, hrs, month, year, day;
        Calendar c = Calendar.getInstance();
        seconds = c.get(Calendar.SECOND);
        min = c.get(Calendar.MINUTE);
        hrs = c.get(Calendar.HOUR_OF_DAY);
        year = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day = c.get(Calendar.DAY_OF_MONTH);
        Log.i("Time", "hrs-" + hrs + "min-" + min + "seconds-" + seconds);


        BeanHoroPersonalInfo beanHoroPersonalInfo = new BeanHoroPersonalInfo();


        BeanDateTime beanDateTime = new BeanDateTime();
        BeanPlace beanPlace = new BeanPlace();
        beanHoroPersonalInfo.setName(beanHoroPersonalInfoObj.getName());
        beanHoroPersonalInfo.setGender(beanHoroPersonalInfoObj.getGender());
        beanHoroPersonalInfo.setAyan(beanHoroPersonalInfoObj.getAyan());
        beanHoroPersonalInfo.setCityID(beanHoroPersonalInfoObj.getCityID());
        beanHoroPersonalInfo.setDST(beanHoroPersonalInfoObj.getDST());
        beanHoroPersonalInfo.setAyanIndex(beanHoroPersonalInfoObj.getAyanIndex());
        // beanHoroPersonalInfo.setFloatDST(beanHoroPersonalInfoObj.getFloatDST());
        beanHoroPersonalInfo.setHoraryNumber(beanHoroPersonalInfoObj.getHoraryNumber());
        beanHoroPersonalInfo.setLocalChartId(beanHoroPersonalInfoObj.getLocalChartId());
        beanHoroPersonalInfo.setOnlineChartId(beanHoroPersonalInfoObj.getOnlineChartId());
        beanHoroPersonalInfo.setSaveChart(beanHoroPersonalInfoObj.isSaveChart());
        //beanHoroPersonalInfo.set_isDefault(beanHoroPersonalInfoObj.is_isDefault());
        beanHoroPersonalInfo.setRecentId(beanHoroPersonalInfoObj.getRecentId());

        beanDateTime.setSecond(seconds);
        beanDateTime.setMin(min);
        beanDateTime.setHour(hrs);
        beanDateTime.setYear(year);
        beanDateTime.setMonth(month);
        beanDateTime.setDay(day);


        beanPlace.setCountryId(beanHoroPersonalInfoObj.getPlace().getCountryId());
        beanPlace.setCityId(beanHoroPersonalInfoObj.getPlace().getCityId());
        beanPlace.setTimeZoneId(beanHoroPersonalInfoObj.getPlace().getTimeZoneId());
        beanPlace.setTimeZoneValue(beanHoroPersonalInfoObj.getPlace().getTimeZoneValue());
        beanPlace.setCountryName(beanHoroPersonalInfoObj.getPlace().getCountryName());
        beanPlace.setState(beanHoroPersonalInfoObj.getPlace().getState());
        beanPlace.setCityName(beanHoroPersonalInfoObj.getPlace().getCityName());
        beanPlace.setLatDeg(beanHoroPersonalInfoObj.getPlace().getLatDeg());
        beanPlace.setLatMin(beanHoroPersonalInfoObj.getPlace().getLatMin());
        beanPlace.setLongDir(beanHoroPersonalInfoObj.getPlace().getLongDir());
        beanPlace.setLongMin(beanHoroPersonalInfoObj.getPlace().getLongMin());
        beanPlace.setLatDir(beanHoroPersonalInfoObj.getPlace().getLatDir());
        beanPlace.setLongDir(beanHoroPersonalInfoObj.getPlace().getLongDir());
        beanPlace.setTimeZoneName(beanHoroPersonalInfoObj.getPlace().getTimeZoneName());
        beanPlace.setLatSec(beanHoroPersonalInfoObj.getPlace().getLatSec());
        beanPlace.setLongSec(beanHoroPersonalInfoObj.getPlace().getLongSec());
        beanPlace.setTimeZone(beanHoroPersonalInfoObj.getPlace().getTimeZone());
        beanPlace.setTimeZoneString(beanHoroPersonalInfoObj.getPlace().getTimeZoneString());
        beanHoroPersonalInfo.setDateTime(beanDateTime);
        beanHoroPersonalInfo.setPlace(beanPlace);
        beanDateTime.setDST(getDSTValue(beanPlace, beanDateTime));
        return beanHoroPersonalInfo;
    }

    public boolean isDSTTimeEligible(String timeZoneName, Date dateobj) {
        boolean dstValue = false;
        PanchangUtil objPanchangUtil;
        objPanchangUtil = new PanchangUtil();
        dstValue = objPanchangUtil.isDst(timeZoneName, dateobj);
        return dstValue;
    }

    // take string type date to convet Date object
    // @Tejinder Singh
    private String getFormatedTextToShowDateToParse(BeanDateTime beanDateTime) {
        String strDateTime = null;
        String[] months = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul",
                "Aug", "Sep", "Oct", "Nov", "Dec"};
        strDateTime = CUtils.pad(beanDateTime.getDay()) + "-"
                + months[beanDateTime.getMonth()] + "-"
                + beanDateTime.getYear();
        return strDateTime;
    }

    public Date getStringtoDateObject(String date) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy",
                Locale.US);
        String dateInString = date;
        Date dateReturn = null;

        try {
            dateReturn = formatter.parse(dateInString);
            System.out.println(dateReturn);
            System.out.println(formatter.format(dateReturn));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateReturn;
    }

    public float getDSTValue(BeanPlace beanPlace, BeanDateTime beanDateTime) {
        Float dstVal = 0f;
        String dateString = getFormatedTextToShowDateToParse(beanDateTime);
        Date date = getStringtoDateObject(dateString);
        if (beanPlace.getTimeZoneString() != null) {
            boolean isDST = isDSTTimeEligible(beanPlace.getTimeZoneString(),
                    date);
            if (isDST) {
                dstVal = 1f;
            }
        }
        return dstVal;
    }

}









