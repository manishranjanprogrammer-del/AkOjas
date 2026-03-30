package com.ojassoft.astrosage.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AdData;
import com.ojassoft.astrosage.customadapters.MainModulesAdapter;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.model.CustomAddModel;
import com.ojassoft.astrosage.ui.act.ActAppModule;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.act.HomeInputScreen;
import com.ojassoft.astrosage.ui.customviews.basic.ScrollableGridView;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.util.ArrayList;

/**
 * Created by Amit Rautela on 23/2/16.
 * This fragment is used to show kundli related modules
 */
public class Reports_frag extends Fragment {

    public Integer[] moduleIconList = {
            R.drawable.ic_predection,
            R.drawable.ic_monthly,
            R.drawable.ic_today,
            R.drawable.ic_sadhe_saati,
            R.drawable.ic_predection,
            R.drawable.ic_varshfal,
            R.drawable.ic_mangal_dosh,
            R.drawable.ic_kalsurp,
            R.drawable.ic_moon,
            R.drawable.ic_lalkitab,
            R.drawable.ic_lalkitab,
            R.drawable.ic_baby,
            R.drawable.ic_lalkitab,
            R.drawable.ic_planet_consideration,
            R.drawable.ic_gemstone,
            R.drawable.ic_transit_today,
            R.drawable.ic_mahadashaphal,
            R.drawable.ic_nakshatra_report
    };


    static final int lifeReport = 0;
    static final int monthlyReport = 1;
    static final int dailyReport = 2;
    static final int sadeSatiReport = 3;
    static final int ascendantPrediction = 4;
    static final int annualPrediction = 5;
    static final int mangalDosh = 6;
    static final int kalSharpDosh = 7;
    static final int moonSingh = 8;
    static final int lalKitabDebt = 9;
    static final int lalKitabTeva = 10;
    static final int babyNames = 11;
    static final int lalkitabRemedies = 12;
    static final int planetConsideration = 13;
    static final int gemstonesReport = 14;
    static final int transitToday = 15;
    static final int mahadashaPhala = 16;
    static final int nakshatraReport = 17;


    private String[] moduleNameList;

    ScrollableGridView gridView;
    NetworkImageView networkImageView;

    // Typeface typeface;

    static Activity activity;
    // add for showimg custom adds

    private static int NUM_PAGES = 0;
    private static int currentPage = 0;
    Typeface typeface;
    private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    CustomProgressDialog pd = null;
    ArrayList<CustomAddModel> sliderList;


    private String IsShowBanner = "False";
    private String IsShowBanner1 = "False";
    private ArrayList<AdData> adList;
    AdData topAdData, bottomAdData;
    private static int currentPage1 = 0;
    private static int NUM_PAGES1 = 0;


    private NetworkImageView topAdImage, bottomoAdImage;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       /* try {
            String result = CUtils.getStringData(getActivity(), "CUSTOMADDS", "");
            if (result != null && !result.equals("")) {
                adList = new Gson().fromJson(result, new TypeToken<ArrayList<AdData>>() {
                }.getType());
                topAdData = CUtils.getSlotData(adList,"2");
                bottomAdData= CUtils.getSlotData(adList,"3");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/

    }


    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        // TODO Auto-generated method stub
        if (activity != null && isVisibleToUser) {
            // If we are becoming invisible, then...
            try {
                String result = CUtils.getStringData(activity, "CUSTOMADDS", "");
                if (result != null && !result.equals("")) {
                    adList = new Gson().fromJson(result, new TypeToken<ArrayList<AdData>>() {
                    }.getType());
                    topAdData = CUtils.getSlotData(adList, "2");
                    bottomAdData = CUtils.getSlotData(adList, "3");
                }

                if (topAdData != null && topAdData.getImageObj() != null && topAdData.getImageObj().size() > 0) {
                    setTopAdd(topAdData);
                }

                if (bottomAdData != null && bottomAdData.getImageObj() != null && bottomAdData.getImageObj().size() > 0) {
                    setBottomAd(bottomAdData);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        super.setUserVisibleHint(isVisibleToUser);
    }


    private void getData()

    {

        try {
            String result = CUtils.getStringData(getActivity(), "CUSTOMADDS", "");
            if (result != null && !result.equals("")) {
                adList = new Gson().fromJson(result, new TypeToken<ArrayList<AdData>>() {
                }.getType());
                topAdData = CUtils.getSlotData(adList, "2");
                bottomAdData = CUtils.getSlotData(adList, "3");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.modules_frag, container, false);
        try {
            /*String result = CUtils.getStringData(getActivity(), "CUSTOMADDS", "");
            if (result != null && !result.equals("")) {
                sliderList = ((ActAppModule) getActivity()).parseData(result);

            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        //typeface=((ActAppModule)getActivity()).typeface;
        LANGUAGE_CODE = ((AstrosageKundliApplication) getActivity().getApplicationContext())
                .getLanguageCode();
        typeface = CUtils.getRobotoFont(
                getActivity(), LANGUAGE_CODE, CGlobalVariables.regular);
        setLayRef(rootView);


        bottomoAdImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CUtils.googleAnalyticSendWitPlayServie(getActivity(),
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_SLOT_3_Add, null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SLOT_3_Add,CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK,"");

                CUtils.createSession(getActivity(),"S3");

                CustomAddModel modal = bottomAdData.getImageObj().get(0);

                /*if (modal.getImgthumbnailurl().contains(CGlobalVariables.buy_astrosage_url)) {
                    CUtils.getUrlLink(modal.getImgthumbnailurl(), getActivity(),LANGUAGE_CODE, 0);
                } else {
                    if (modal.getImgthumbnailurl() != null && !modal.getImgthumbnailurl().equals("")) {
                        Uri uri = Uri.parse(modal.getImgthumbnailurl());
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(uri);
                        startActivity(i);
                    }
                }*/
                CUtils.divertToScreen(getActivity(),modal.getImgthumbnailurl(),LANGUAGE_CODE);

            }
        });

        topAdImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CUtils.googleAnalyticSendWitPlayServie(getActivity(),
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_SLOT_2_Add, null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SLOT_2_Add,CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK,"");

                CUtils.createSession(getActivity(),"S2");

                CustomAddModel modal = topAdData.getImageObj().get(0);

                /*if (modal.getImgthumbnailurl().contains(CGlobalVariables.buy_astrosage_url)) {
                    CUtils.getUrlLink(modal.getImgthumbnailurl(), getActivity(),LANGUAGE_CODE, 0);
                } else {
                    if (modal.getImgthumbnailurl() != null && !modal.getImgthumbnailurl().equals("")) {
                        Uri uri = Uri.parse(modal.getImgthumbnailurl());
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(uri);
                        startActivity(i);
                    }
                }*/
                CUtils.divertToScreen(getActivity(),modal.getImgthumbnailurl(),LANGUAGE_CODE);

            }
        });
        return rootView;
    }

    /**
     * @created by : Amit Rautela
     * @created on : 23/2/16.
     * @desc : This method is used to look up the layout reference
     */
    private void setLayRef(View rootView) {

        networkImageView = (NetworkImageView) rootView.findViewById(R.id.imgBanner);
        ((ActAppModule) getActivity()).getDataFromGTMCointainer(networkImageView);
        moduleNameList = getResources().getStringArray(R.array.reportModulesList);
        gridView = (ScrollableGridView) rootView.findViewById(R.id.gridView);
        gridView.setVisibility(View.VISIBLE);
        //rootView.findViewById(R.id.dummyViewModulesFrag).setVisibility(View.VISIBLE);
        MainModulesAdapter adapter = new MainModulesAdapter(activity, moduleIconList, moduleNameList, ((BaseInputActivity) getActivity()).mediumTypeface, Reports_frag.this);
        gridView.setAdapter(adapter);
        gridView.setExpanded(true);
        gridView.setFocusable(false);
        topAdImage = (NetworkImageView) rootView.findViewById(R.id.topAdImage);
        bottomoAdImage = (NetworkImageView) rootView.findViewById(R.id.bottomoAdImage);


        if (topAdData != null && topAdData.getImageObj() != null && topAdData.getImageObj().size() > 0) {
            setTopAdd(topAdData);
        }

        if (bottomAdData != null && bottomAdData.getImageObj() != null && bottomAdData.getImageObj().size() > 0) {
            setBottomAd(bottomAdData);
        }

    }

    /**
     * @param position
     * @created by : Amit Rautela
     * @created on : 23/2/16.
     */
    public static void callActivity(int position) {

        switch (position) {
            case lifeReport:
                showPredictionsData(CGlobalVariables.SUB_MODULE_PREDICTION_LIFE_PREDICTIONS, CGlobalVariables.GOOGLE_ANALYTIC_LIFE_REPORTS);

                break;
            case monthlyReport:
                showPredictionsData(CGlobalVariables.SUB_MODULE_PREDICTION_MONTHLY_PREDICTIONS, CGlobalVariables.GOOGLE_ANALYTIC_MONTHLY_REPORTS);
                break;
            case dailyReport:
                showPredictionsData(CGlobalVariables.SUB_MODULE_PREDICTION_DAILY_PREDICTIONS, CGlobalVariables.GOOGLE_ANALYTIC_DAILY_REPORTS);
                break;
            case mangalDosh:
                showPredictionsData(CGlobalVariables.SUB_MODULE_PREDICTION_MANGAL_DOSH, CGlobalVariables.GOOGLE_ANALYTIC_MANGAL_DOSH);
                break;
            case sadeSatiReport:
                showPredictionsData(CGlobalVariables.SUB_MODULE_PREDICTION_SADE_SATI, CGlobalVariables.GOOGLE_ANALYTIC_SADESATI_REPORTS);
                break;
            case kalSharpDosh:
                showPredictionsData(CGlobalVariables.SUB_MODULE_PREDICTION_KAAL_SARP_DOSHA, CGlobalVariables.GOOGLE_ANALYTIC_KALSARPL_DOSH);
                break;
            case lalKitabDebt:
                showPredictionsData(CGlobalVariables.SUB_MODULE_PREDICTION_LALKITAB_DEBT, CGlobalVariables.GOOGLE_ANALYTIC_LALKITAB_DEBT);
                break;
            case lalKitabTeva:
                showPredictionsData(CGlobalVariables.SUB_MODULE_PREDICTION_LALKITAB_TEVA_TYPE, CGlobalVariables.GOOGLE_ANALYTIC_LALKITAB_TEVA);
                break;
            case ascendantPrediction:
                showPredictionsData(CGlobalVariables.SUB_MODULE_PREDICTION_ASCENDANT_PREDICTION, CGlobalVariables.GOOGLE_ANALYTIC_ASCENDANT_PREDICTION);
                break;
            case moonSingh:
                showPredictionsData(CGlobalVariables.SUB_MODULE_PREDICTION_MOONWESTERN, CGlobalVariables.GOOGLE_ANALYTIC_MOON_SIGN);
                break;
            case babyNames:
                showPredictionsData(CGlobalVariables.SUB_MODULE_PREDICTION_BABYNAME, CGlobalVariables.GOOGLE_ANALYTIC_BABY_NAME);
                break;
            case annualPrediction:
                showPredictionsData(CGlobalVariables.SUB_MODULE_PREDICTION_VARSHPHAL, CGlobalVariables.GOOGLE_ANALYTIC_ANNUAL_PREDICTION);
                break;
            case lalkitabRemedies:
                showPredictionsData(CGlobalVariables.SUB_MODULE_PREDICTION_LAL_KITAB_REMEDIES, CGlobalVariables.GOOGLE_ANALYTIC_LALKITAB_REMEDIES);
                break;
            case planetConsideration:
                showPredictionsData(CGlobalVariables.SUB_MODULE_PREDICTION_PLANET_CONSIDERATION, CGlobalVariables.GOOGLE_ANALYTIC_PLANET_CONSIDERATION);
                break;
            case gemstonesReport:
                showPredictionsData(CGlobalVariables.SUB_MODULE_PREDICTION_GEMSTONE_REPORT, CGlobalVariables.GOOGLE_ANALYTIC_GEMSTONES_REPORT);
                break;
            case transitToday:
                showPredictionsData(CGlobalVariables.SUB_MODULE_PREDICTION_TRANSIT_TODAY, CGlobalVariables.GOOGLE_ANALYTIC_TRANSIT_TODAY);
                break;
            case mahadashaPhala:
                showPredictionsData(CGlobalVariables.SUB_MODULE_PREDICTION_MAHADASHA_PHALA, CGlobalVariables.GOOGLE_ANALYTIC_MAHADASHA_PHALA);
                break;
            case nakshatraReport:
                showPredictionsData(CGlobalVariables.SUB_MODULE_PREDICTION_NAKSHATRA_REPORT, CGlobalVariables.GOOGLE_ANALYTIC_NAKSHATRA_REPORT);
                break;
            default:
                break;

        }
    }


    @Override
    public void onDetach() {
        super.onDetach();

        /*try {
            Field childFragmentManager = Fragment.class.getDeclaredField("mChildFragmentManager");
            childFragmentManager.setAccessible(true);
            childFragmentManager.set(this, null);

        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }*/
        activity = null;
    }


    private static void showPredictionsData(int subModule, String googleAnalyticAction) {

        CUtils.googleAnalyticSendWitPlayServie(activity,
                CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                googleAnalyticAction,
                null);

        String labell= "report_"+ googleAnalyticAction;
        CUtils.fcmAnalyticsEvents(labell, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");


        Intent intent = new Intent(activity, HomeInputScreen.class);
        intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, CGlobalVariables.MODULE_PREDICTION);
        intent.putExtra(CGlobalVariables.SUB_MODULE_TYPE_KEY, subModule);
        activity.startActivity(intent);

    }


    public void setTopAdd(AdData topData) {
        getData();
        if (topData != null) {
            IsShowBanner = topData.getIsShowBanner();
            IsShowBanner = IsShowBanner == null ? "" : IsShowBanner;

        }
        if (topData == null || topData.getImageObj() == null || topData.getImageObj().size() <= 0 || IsShowBanner.equalsIgnoreCase("False")) {
            if (topAdImage != null) {

                topAdImage.setVisibility(View.GONE);
            }
        } else {
            if (topAdImage != null) {
                topAdImage.setVisibility(View.VISIBLE);
                topAdImage.setImageUrl(topData.getImageObj().get(0).getImgurl(), VolleySingleton.getInstance(getActivity()).getImageLoader());
            }
        }

        if (CUtils.getUserPurchasedPlanFromPreference(activity) != 1) {
            if (topAdImage != null) {
                topAdImage.setVisibility(View.GONE);
            }
        }

    }

    public void setBottomAd(AdData bottomData) {
        getData();
        if (bottomData != null) {
            IsShowBanner1 = bottomData.getIsShowBanner();
            IsShowBanner1 = IsShowBanner1 == null ? "" : IsShowBanner1;

        }
        if (bottomAdData == null || bottomData.getImageObj() == null || bottomData.getImageObj().size() <= 0 || IsShowBanner1.equalsIgnoreCase("False")) {
            if (bottomoAdImage != null) {
                bottomoAdImage.setVisibility(View.GONE);
            }

        } else {
            if (bottomoAdImage != null) {
                bottomoAdImage.setVisibility(View.VISIBLE);
                bottomoAdImage.setImageUrl(bottomData.getImageObj().get(0).getImgurl(), VolleySingleton.getInstance(getActivity()).getImageLoader());
            }
        }

        if (CUtils.getUserPurchasedPlanFromPreference(activity) != 1) {
            if (bottomoAdImage != null) {
                bottomoAdImage.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
