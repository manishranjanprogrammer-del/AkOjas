package com.ojassoft.astrosage.ui.fragments.vratfragment;

import static com.ojassoft.astrosage.utils.CUtils.appendDarkModeCheckForURL;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.ojassoft.astrosage.ui.act.ActYearly;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.customviews.basic.ScrollableGridView;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;

/**
 * Created by ojas-20 on 7/12/18.
 */

public class Frag_Year extends Fragment {

    public Integer[] moduleIconList = {
            R.drawable.ic_horoscope,
            R.drawable.ic_transit_today,
            R.drawable.ic_sadhe_saati,
            R.drawable.ic_matching,
            R.mipmap.ic_career,
            R.mipmap.ic_chinese,
            R.mipmap.ic_education,
            R.mipmap.ic_finance,
            R.drawable.ic_lalkitab,
            R.mipmap.ic_numerology,
            //R.mipmap.ic_yearly_calendar,
            /*R.mipmap.ic_muhurat,
            R.mipmap.ic_sports,
            R.mipmap.ic_movies,
            R.mipmap.ic_election,
            R.mipmap.ic_ipl,
            R.mipmap.ic_festivals,
            R.mipmap.ic_grahan*/
            R.mipmap.ic_marriage,
            R.mipmap.ic_mundan,
            R.mipmap.ic_home,
            R.mipmap.ic_name,
            R.mipmap.ic_muhurat,
            R.mipmap.ic_ear,
            R.mipmap.ic_books,

            R.mipmap.ic_ketu,
            R.mipmap.ic_rahu,
            R.mipmap.ic_grahan,
            R.mipmap.ic_grahan,
            R.mipmap.ic_planets_in_retrograde,
            R.mipmap.ic_mercury_retrograde
    };


    private static final int horoscope = 0;
    private static final int jupiter_transit = 1;
    private static final int saturn_transit = 2;
    private static final int loveHoroscope = 3;
    private static final int careerHoroscope = 4;
    private static final int chineseHoroscope = 5;
    private static final int educationHoroscope = 6;
    private static final int financeHoroscope = 7;
    private static final int lalkitabHoroscope = 8;
    private static final int numerology = 9;

    private static final int vivahMuhurat = 10;
    private static final int mundanMuhurat = 11;
    private static final int grihaMuhurat = 12;
    private static final int namkaranMuhurat = 13;
    private static final int annaprashanMuhurat = 14;
    private static final int karnavedhaMuhurat = 15;
    private static final int vidyarambhMuhurat = 16;

    private static final int ketuTransit = 17;
    private static final int rahuTransit = 18;
    private static final int lunarEclipse = 19;
    private static final int solarEclipse = 20;
    private static final int planetsInRetrograde = 21;
    private static final int mercuryRetrograde = 22;

    //private static final int yearCalendar = 10;
    /*private static final int muhurat = 11;
    private static final int sports = 12;
    private static final int movies = 13;
    private static final int elections = 14;
    private static final int ipl = 15;
    private static final int festivals = 16;
    private static final int grahan = 17;*/


    private static String[] moduleNameList;

    ScrollableGridView gridView;
    NetworkImageView networkImageView;

    // Typeface typeface;

    static Activity activity;
    // add for showimg custom adds

    private static int NUM_PAGES = 0;
    private static int currentPage = 0;
    Typeface typeface;
    private static int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
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
                    topAdData = CUtils.getSlotData(adList, "26");
                    bottomAdData = CUtils.getSlotData(adList, "27");

                    if (topAdData == null) {
                        topAdData = CUtils.getSlotData(adList, "0");
                    }
                    if (bottomAdData == null) {
                        bottomAdData = CUtils.getSlotData(adList, "1");
                    }
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


    private void getData() {

        try {
            String result = CUtils.getStringData(getActivity(), "CUSTOMADDS", "");
            if (result != null && !result.equals("")) {
                adList = new Gson().fromJson(result, new TypeToken<ArrayList<AdData>>() {
                }.getType());
                topAdData = CUtils.getSlotData(adList, "26");
                bottomAdData = CUtils.getSlotData(adList, "27");

                if (topAdData == null) {
                    topAdData = CUtils.getSlotData(adList, "0");
                }
                if (bottomAdData == null) {
                    bottomAdData = CUtils.getSlotData(adList, "1");
                }
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
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SLOT_3_Add, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                CUtils.createSession(getActivity(), "S27");

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
                CUtils.divertToScreen(getActivity(), modal.getImgthumbnailurl(), LANGUAGE_CODE);

            }
        });

        topAdImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CUtils.googleAnalyticSendWitPlayServie(getActivity(),
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_SLOT_2_Add, null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SLOT_2_Add, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                CUtils.createSession(getActivity(), "S26");

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
                CUtils.divertToScreen(getActivity(), modal.getImgthumbnailurl(), LANGUAGE_CODE);

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
        //((ActAppModule) getActivity()).getDataFromGTMCointainer(networkImageView);
        moduleNameList = getResources().getStringArray(R.array.yearlyModulesList);

        //add year
        if (LANGUAGE_CODE == CGlobalVariables.HINDI) {
            for (int i = 0; i < moduleNameList.length - 2; i++) {
                moduleNameList[i] = moduleNameList[i] + " " + getResources().getString(R.string.t_2019);
            /*if(i == 0 || i == 1 || i == 2){
                moduleNameList[i] = moduleNameList[i]+"\n";//Add extra space
            }*/
            }
        } else {
            for (int i = 0; i < moduleNameList.length; i++) {
                moduleNameList[i] = moduleNameList[i] + " " + getResources().getString(R.string.t_2019);
            /*if(i == 0 || i == 1 || i == 2){
                moduleNameList[i] = moduleNameList[i]+"\n";//Add extra space
            }*/
            }
        }

        gridView = (ScrollableGridView) rootView.findViewById(R.id.gridView1);
        gridView.setVisibility(View.VISIBLE);
        MainModulesAdapter adapter = new MainModulesAdapter(activity, moduleIconList, moduleNameList, ((BaseInputActivity) getActivity()).mediumTypeface, Frag_Year.this);
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

    public static String getUrl(int key) {
        String url = activity.getResources().getString(key);
        return url;
    }

    /**
     * @param position
     * @created on : 23/2/16.
     */
    public static void callActivity(int position) {
        String url = "";
        String title = "";
        switch (position) {
            case horoscope:
                url = getUrl(R.string.horoscope_url);
                title = moduleNameList[0];
                showYarlyData(CGlobalVariables.GOOGLE_ANALYTIC_Yearly_Horoscope, url, title);
                break;
            case jupiter_transit:
                url = getUrl(R.string.jupiter_transit_url);
                title = moduleNameList[1];
                showYarlyData(CGlobalVariables.GOOGLE_ANALYTIC_Yearly_Jupiter_Transits, url, title);
                break;
            case saturn_transit:
                url = getUrl(R.string.saturn_transit_url);
                title = moduleNameList[2];
                showYarlyData(CGlobalVariables.GOOGLE_ANALYTIC_Yearly_Saturn_Transits, url, title);
                break;
            case loveHoroscope:
                url = getUrl(R.string.loveHoroscope_url);
                title = moduleNameList[3];
                showYarlyData(CGlobalVariables.GOOGLE_ANALYTIC_Yearly_Love_Horoscope, url, title);
                break;
            case careerHoroscope:
                url = getUrl(R.string.careerHoroscope_url);
                title = moduleNameList[4];
                showYarlyData(CGlobalVariables.GOOGLE_ANALYTIC_Yearly_Career_Horoscope, url, title);
                break;
            case chineseHoroscope:
                url = getUrl(R.string.chineseHoroscope_url);
                title = moduleNameList[5];
                showYarlyData(CGlobalVariables.GOOGLE_ANALYTIC_Yearly_Chinese_Horoscope, url, title);
                break;
            case educationHoroscope:
                url = getUrl(R.string.educationHoroscope_url);
                title = moduleNameList[6];
                showYarlyData(CGlobalVariables.GOOGLE_ANALYTIC_Yearly_Education_Horoscope, url, title);
                break;
            case financeHoroscope:
                url = getUrl(R.string.financeHoroscope_url);
                title = moduleNameList[7];
                showYarlyData(CGlobalVariables.GOOGLE_ANALYTIC_Yearly_Finance_Horoscope, url, title);
                break;
            case lalkitabHoroscope:
                url = getUrl(R.string.lalkitabHoroscope_url);
                title = moduleNameList[8];
                showYarlyData(CGlobalVariables.GOOGLE_ANALYTIC_Yearly_Lalkitab_Horoscope, url, title);
                break;
            case numerology:
                url = getUrl(R.string.numerology_url);
                title = moduleNameList[9];
                showYarlyData(CGlobalVariables.GOOGLE_ANALYTIC_Yearly_Numerology, url, title);
                break;

            case vivahMuhurat:
                url = getUrl(R.string.viah_muhurat_url);
                title = moduleNameList[10];
                showYarlyData(CGlobalVariables.GOOGLE_ANALYTIC_Yearly_Viah_Muhurat, url, title);
                break;
            case mundanMuhurat:
                url = getUrl(R.string.mundan_muhurat_url);
                //         Log.e("urlcheck", "URL: " +url);
                title = moduleNameList[11];
                showYarlyData(CGlobalVariables.GOOGLE_ANALYTIC_Yearly_Mundan_Muhurat, url, title);
                break;
            case grihaMuhurat:
                url = getUrl(R.string.griha_muhurat_url);
                title = moduleNameList[12];
                //      Log.e("urlcheck", "URL: " +url);
                showYarlyData(CGlobalVariables.GOOGLE_ANALYTIC_Yearly_Griha_Muhurat, url, title);
                break;
            case namkaranMuhurat:
                url = getUrl(R.string.namkaran_muhurat_url);
                title = moduleNameList[13];
                //     Log.e("urlcheck", "URL: " +url);
                showYarlyData(CGlobalVariables.GOOGLE_ANALYTIC_Yearly_Namkaran_Muhurat, url, title);
                break;
            case annaprashanMuhurat:
                url = getUrl(R.string.annaprashan_muhurat_url);
                title = moduleNameList[14];
                showYarlyData(CGlobalVariables.GOOGLE_ANALYTIC_Yearly_Annaprashan_Muhurat, url, title);
                break;
            case karnavedhaMuhurat:
                url = getUrl(R.string.karnavedha_muhurat_url);
                title = moduleNameList[15];
                showYarlyData(CGlobalVariables.GOOGLE_ANALYTIC_Yearly_Karnavedha_Muhurat, url, title);
                break;
            case vidyarambhMuhurat:
                url = getUrl(R.string.vidyarambh_muhurat_url);
                title = moduleNameList[16];
                showYarlyData(CGlobalVariables.GOOGLE_ANALYTIC_Yearly_Vidyarambh_Muhurat, url, title);
                break;


            case ketuTransit:
                url = getUrl(R.string.ketu_transit_url);
                title = moduleNameList[17];
                showYarlyData(CGlobalVariables.GOOGLE_ANALYTIC_Yearly_Ketu_Transit, url, title);
                break;


            case rahuTransit:
                url = getUrl(R.string.rahu_transit_url);
                title = moduleNameList[18];
                showYarlyData(CGlobalVariables.GOOGLE_ANALYTIC_Yearly_Rahu_Transit, url, title);
                break;

            case lunarEclipse:
                url = getUrl(R.string.lunar_eclipse_url);
                title = moduleNameList[19];
                showYarlyData(CGlobalVariables.GOOGLE_ANALYTIC_Yearly_Lunar_Eclipse, url, title);
                break;

            case solarEclipse:
                url = getUrl(R.string.solar_eclipse_url);
                title = moduleNameList[20];
                showYarlyData(CGlobalVariables.GOOGLE_ANALYTIC_Yearly_Solar_Eclipse, url, title);
                break;

            case planetsInRetrograde:
                url = getUrl(R.string.planets_in_retrograde_url);
                title = moduleNameList[21];
                showYarlyData(CGlobalVariables.GOOGLE_ANALYTIC_Yearly_PlanetsInRetrograde, url, title);
                break;

            case mercuryRetrograde:
                url = getUrl(R.string.mercury_retrograde_url);
                title = moduleNameList[22];
                showYarlyData(CGlobalVariables.GOOGLE_ANALYTIC_Yearly_MercuryRetrograde, url, title);
                break;
            /*case yearCalendar:
                url = getUrl(R.string.yearCalendar_url);
                title =moduleNameList[10];
                showYarlyData(CGlobalVariables.GOOGLE_ANALYTIC_Yearly_Calendar,url,title);
                break;*/
            /*case muhurat:
                url  = getUrl(R.string.muhurat_url);
                title =moduleNameList[11];
                showYarlyData(CGlobalVariables.GOOGLE_ANALYTIC_Yearly_Muhurat,url,title);
                break;
            case sports:
                url  = getUrl(R.string.sports_url);
                title =moduleNameList[12];
                showYarlyData(CGlobalVariables.GOOGLE_ANALYTIC_Yearly_Sports,url,title);
                break;
            case movies:
                url  = getUrl(R.string.movies_url);
                title =moduleNameList[13];
                showYarlyData(CGlobalVariables.GOOGLE_ANALYTIC_Yearly_Movies,url,title);
                break;
            case elections:
                url = getUrl(R.string.elections_url);
                title =moduleNameList[14];
                showYarlyData(CGlobalVariables.GOOGLE_ANALYTIC_Yearly_Elections,url,title);
                break;
            case ipl:
                url = getUrl(R.string.ipl_url);
                title =moduleNameList[15];
                showYarlyData(CGlobalVariables.GOOGLE_ANALYTIC_Yearly_IPL,url,title);
                break;
            case festivals:
                url = getUrl(R.string.festivals_url);
                title =moduleNameList[16];
                showYarlyData(CGlobalVariables.GOOGLE_ANALYTIC_Yearly_Festivals,url,title);
                break;
            case grahan:
                url = getUrl(R.string.grahan_url);
                title =moduleNameList[17];
                showYarlyData(CGlobalVariables.GOOGLE_ANALYTIC_Yearly_Grahan,url,title);
                break;*/
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


    private static void showYarlyData(String googleAnalyticAction, String url, String title) {
        Log.e("2021 URL Title ", title);
        Log.e("2021 URL ", url);
        CUtils.googleAnalyticSendWitPlayServie(activity,
                CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                googleAnalyticAction,
                null);

        String labell = googleAnalyticAction + "_" + "2019";
        CUtils.fcmAnalyticsEvents(labell, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");


        if (url != null && !url.equals("")) {
            Intent intent = new Intent(activity, ActYearly.class);
            intent.putExtra("url", url);
            intent.putExtra("title", title);
            intent.putExtra("isBottomNavigation", false);

            activity.startActivity(intent);
        }
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
