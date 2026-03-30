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
import com.ojassoft.astrosage.ui.act.ActCalendar;
import com.ojassoft.astrosage.ui.act.ActHinduCalender;
import com.ojassoft.astrosage.ui.act.ActIndianCalender;
import com.ojassoft.astrosage.ui.act.ActMontlyCalendar;
import com.ojassoft.astrosage.ui.act.ActYearlyVrat;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.act.InputPanchangActivity;
import com.ojassoft.astrosage.ui.act.indnotes.NotesActivity;
import com.ojassoft.astrosage.ui.customviews.basic.ScrollableGridView;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Amit Rautela on 23/2/16.
 * This fragment is used to show kundli related modules
 */
public class Panchang_Frag extends Fragment {

    public Integer[] moduleIconList = {
            R.drawable.ic_panchang,
            R.drawable.ic_monthly,
            R.drawable.ic_hindu_calendar,
            R.drawable.ic_vart,
            R.drawable.ic_indian_calendar,
            R.drawable.ic_hora,
            R.drawable.ic_time,
            R.drawable.ic_do_ghati,
            R.drawable.ic_time,
            R.drawable.ic_today,
            R.mipmap.ic_panchak,
            R.mipmap.ic_bhadra,
            R.mipmap.ic_muhurat_panchang,
            R.mipmap.ic_lagna_table,
    };

    private String[] moduleNameList;


    ScrollableGridView gridView;
    NetworkImageView networkImageView;

    //Typeface typeface;

    static Activity activity;

    static final int dailyPanchang = 0;
    static final int monthView = 1;
    static final int hinduCalender = 2;
    static final int yearltVart = 3;
    static final int indianCalender = 4;
    static final int hora = 5;
    static final int chogadia = 6;
    static final int doGhati = 7;
    static final int rahukaal = 8;
    static final int yearlycalender = 9;
    static final int panchak = 10;
    static final int bhadra = 11;
    static final int muhurat = 12;
    static final int lagna = 13;
    static final int dailyNotes = 14;


    Typeface typeface;
    private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    CustomProgressDialog pd = null;
    ArrayList<CustomAddModel> sliderList;


    private String IsShowBanner = "False";
    private String IsShowBanner1 = "False";
    private ArrayList<AdData> adList;
    AdData topAdData, bottomAdData;


    private NetworkImageView topAdImage, bottomoAdImage;
    int currentYear;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
        currentYear = Calendar.getInstance().get(Calendar.YEAR);

    }


    private void getData() {

        try {
            String result = CUtils.getStringData(activity, "CUSTOMADDS", "");
            if (result != null && !result.equals("")) {
                adList = new Gson().fromJson(result, new TypeToken<ArrayList<AdData>>() {
                }.getType());
                topAdData = CUtils.getSlotData(adList, "4");
                bottomAdData = CUtils.getSlotData(adList, "5");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

/*
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        // TODO Auto-generated method stub
        if (isVisibleToUser) {
            // If we are becoming invisible, then...
            try {
                String result = CUtils.getStringData(activity, "CUSTOMADDS", "");
                if (result != null && !result.equals("")) {
                    adList = new Gson().fromJson(result, new TypeToken<ArrayList<AdData>>() {
                    }.getType());
                    topAdData = CUtils.getSlotData(adList,"4");
                    bottomAdData= CUtils.getSlotData(adList,"5");
                }

                if (topAdData!=null && topAdData.getImageObj() != null && topAdData.getImageObj().size() > 0) {
                    setTopAdd(topAdData);
                }

                if (bottomAdData!=null && bottomAdData.getImageObj() != null && bottomAdData.getImageObj().size() > 0) {
                    setBottomAd(bottomAdData);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }


        }
        super.setUserVisibleHint(isVisibleToUser);
    }*/

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.modules_frag, container, false);
        //typeface=((ActAppModule)activity).typeface;
        LANGUAGE_CODE = ((AstrosageKundliApplication) activity.getApplicationContext())
                .getLanguageCode();
        typeface = CUtils.getRobotoFont(
                activity, LANGUAGE_CODE, CGlobalVariables.regular);
        setLayRef(rootView);
        //  frameLay.setVisibility(View.VISIBLE);

        bottomoAdImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CUtils.googleAnalyticSendWitPlayServie(activity,
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_SLOT_5_Add, null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SLOT_5_Add, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                CUtils.createSession(activity, "S5");

                CustomAddModel modal = bottomAdData.getImageObj().get(0);

                /*if (modal.getImgthumbnailurl().contains(CGlobalVariables.buy_astrosage_url)) {
                    CUtils.getUrlLink(modal.getImgthumbnailurl(), activity,LANGUAGE_CODE, 0);
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

                CUtils.googleAnalyticSendWitPlayServie(activity,
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_SLOT_4_Add, null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SLOT_4_Add, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                CUtils.createSession(activity, "S4");

                CustomAddModel modal = topAdData.getImageObj().get(0);

                /*if (modal.getImgthumbnailurl().contains(CGlobalVariables.buy_astrosage_url)) {
                    CUtils.getUrlLink(modal.getImgthumbnailurl(), activity,LANGUAGE_CODE, 0);
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
        ((ActAppModule) activity).getDataFromGTMCointainer(networkImageView);
        moduleNameList = getResources().getStringArray(R.array.input_page_titles_list_panchang_home);

        moduleNameList[4] = moduleNameList[4] + " " + String.valueOf(currentYear);
        gridView = (ScrollableGridView) rootView.findViewById(R.id.gridView);
        MainModulesAdapter adapter = new MainModulesAdapter(activity, moduleIconList, moduleNameList, ((BaseInputActivity) activity).mediumTypeface, Panchang_Frag.this);
        gridView.setVisibility(View.VISIBLE);
        //rootView.findViewById(R.id.dummyViewModulesFrag).setVisibility(View.VISIBLE);
        gridView.setAdapter(adapter);
        gridView.setExpanded(true);
        gridView.setFocusable(false);
        topAdImage = (NetworkImageView) rootView.findViewById(R.id.topAdImage);
        bottomoAdImage = (NetworkImageView) rootView.findViewById(R.id.bottomoAdImage);

        /////////////
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

        int moduleType = 0;
        if (position == dailyPanchang) {
            moduleType = CGlobalVariables.MODULE_ASTROSAGE_PANCHANG;
            CUtils.googleAnalyticSendWitPlayServie(activity,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                    CGlobalVariables.GOOGLE_ANALYTIC_PANCHANG_PANCHANG,
                    null);
            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_PANCHANG_PANCHANG, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

            Intent intent = new Intent(activity, InputPanchangActivity.class);
            intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, moduleType);
            intent.putExtra("date", Calendar.getInstance());
            intent.putExtra("place", "");
            activity.startActivity(intent);
        } else if (position == hora) {
            moduleType = CGlobalVariables.MODULE_ASTROSAGE_HORA;
            CUtils.googleAnalyticSendWitPlayServie(activity,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                    CGlobalVariables.GOOGLE_ANALYTIC_HORA,
                    null);
            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_HORA, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

            Intent intent = new Intent(activity, InputPanchangActivity.class);
            intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, moduleType);
            intent.putExtra("date", Calendar.getInstance());
            intent.putExtra("place", "");
            activity.startActivity(intent);
        } else if (position == chogadia) {
            moduleType = CGlobalVariables.MODULE_ASTROSAGE_CHOGADIA;
            CUtils.googleAnalyticSendWitPlayServie(activity,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                    CGlobalVariables.GOOGLE_ANALYTIC_CHOGADIA,
                    null);
            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_CHOGADIA, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

            Intent intent = new Intent(activity, InputPanchangActivity.class);
            intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, moduleType);
            intent.putExtra("date", Calendar.getInstance());
            intent.putExtra("place", "");
            activity.startActivity(intent);
        } else if (position == doGhati) {
            moduleType = CGlobalVariables.MODULE_DO_GHATI_MUHURT;
            CUtils.googleAnalyticSendWitPlayServie(activity,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                    CGlobalVariables.GOOGLE_ANALYTIC_DOGHATI,
                    null);
            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_DOGHATI, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

            Intent intent = new Intent(activity, InputPanchangActivity.class);
            intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, moduleType);
            intent.putExtra("date", Calendar.getInstance());
            intent.putExtra("place", "");
            activity.startActivity(intent);
        } else if (position == rahukaal) {
            moduleType = CGlobalVariables.MODULE_RAHUKAAL;
            CUtils.googleAnalyticSendWitPlayServie(activity,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                    CGlobalVariables.GOOGLE_ANALYTIC_RAHUKAAL,
                    null);
            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_RAHUKAAL, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

            Intent intent = new Intent(activity, InputPanchangActivity.class);
            intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, moduleType);
            intent.putExtra("date", Calendar.getInstance());
            intent.putExtra("place", "");
            activity.startActivity(intent);
        } else if (position == yearlycalender) {
            moduleType = CGlobalVariables.MODULE_CALENDAR;
            CUtils.googleAnalyticSendWitPlayServie(activity,
                    CGlobalVariables.GOOGLE_ANALYTIC_LAUNCH,
                    CGlobalVariables.GOOGLE_ANALYTIC_CALENDAR, null);
            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_CALENDAR, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

            Intent intent = new Intent(activity, ActCalendar.class);
            intent.putExtra(CGlobalVariables.ASTRO_WEBVIEW_TITLE_KEY,
                    moduleType);
            activity.startActivity(intent);
        } else if (position == hinduCalender) {
            moduleType = CGlobalVariables.MODULE_ASTROSAGE_HINDU_CALENDER;
            CUtils.googleAnalyticSendWitPlayServie(activity,
                    CGlobalVariables.GOOGLE_ANALYTIC_LAUNCH,
                    CGlobalVariables.GOOGLE_ANALYTIC_HINDU_CALENDAR, null);
            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_HINDU_CALENDAR, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

            Intent intent = new Intent(activity, ActHinduCalender.class);
            intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY,
                    moduleType);
            activity.startActivity(intent);
        } else if (position == yearltVart) {
            moduleType = CGlobalVariables.MODULE_ASTROSAGE_YEARLY_VART;
            CUtils.googleAnalyticSendWitPlayServie(activity,
                    CGlobalVariables.GOOGLE_ANALYTIC_LAUNCH,
                    CGlobalVariables.GOOGLE_ANALYTIC_YEARLY_VART, null);

            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_YEARLY_VART, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

            Intent intent = new Intent(activity, ActYearlyVrat.class);
            intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY,
                    moduleType);
            activity.startActivity(intent);
        } else if (position == indianCalender) {
            moduleType = CGlobalVariables.MODULE_ASTROSAGE_INDIAN_CALENDER;
            CUtils.googleAnalyticSendWitPlayServie(activity,
                    CGlobalVariables.GOOGLE_ANALYTIC_LAUNCH,
                    CGlobalVariables.GOOGLE_ANALYTIC_INDIAN_CALENDAR, null);
            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_INDIAN_CALENDAR, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

            Intent intent = new Intent(activity, ActIndianCalender.class);
            intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY,
                    moduleType);
            activity.startActivity(intent);
        } else if (position == monthView) {
            moduleType = CGlobalVariables.MODULE_ASTROSAGE_INDIAN_CALENDER;
            CUtils.googleAnalyticSendWitPlayServie(activity,
                    CGlobalVariables.GOOGLE_ANALYTIC_LAUNCH,
                    CGlobalVariables.GOOGLE_ANALYTIC_MONTH_VIEW, null);
            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_MONTH_VIEW, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

            Intent intent = new Intent(activity, ActMontlyCalendar.class);
            intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY,
                    moduleType);
            activity.startActivity(intent);
        } else if (position == panchak) {
            moduleType = CGlobalVariables.MODULE_PANCHAK;
            CUtils.googleAnalyticSendWitPlayServie(activity,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                    CGlobalVariables.GOOGLE_ANALYTIC_PANCHAK,
                    null);
            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_PANCHAK, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

            Intent intent = new Intent(activity, InputPanchangActivity.class);
            intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, moduleType);
            intent.putExtra("date", Calendar.getInstance());
            intent.putExtra("place", "");
            activity.startActivity(intent);
        } else if (position == bhadra) {
            moduleType = CGlobalVariables.MODULE_BHADRA;
            CUtils.googleAnalyticSendWitPlayServie(activity,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                    CGlobalVariables.GOOGLE_ANALYTIC_BHADRA,
                    null);
            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_BHADRA, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

            Intent intent = new Intent(activity, InputPanchangActivity.class);
            intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, moduleType);
            intent.putExtra("date", Calendar.getInstance());
            intent.putExtra("place", "");
            activity.startActivity(intent);
        } else if (position == muhurat) {
            moduleType = CGlobalVariables.MODULE_MUHURAT;
            CUtils.googleAnalyticSendWitPlayServie(activity,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                    CGlobalVariables.GOOGLE_ANALYTIC_MUHURAT,
                    null);
            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_MUHURAT, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

            Intent intent = new Intent(activity, InputPanchangActivity.class);
            intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, moduleType);
            intent.putExtra("date", Calendar.getInstance());
            intent.putExtra("place", "");
            activity.startActivity(intent);
        } else if (position == lagna) {

            moduleType = CGlobalVariables.MODULE_LAGNA;
            CUtils.googleAnalyticSendWitPlayServie(activity,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                    CGlobalVariables.GOOGLE_ANALYTIC_LAGNA,
                    null);
            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_LAGNA, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

            Intent intent = new Intent(activity, InputPanchangActivity.class);
            intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, moduleType);
            intent.putExtra("date", Calendar.getInstance());
            intent.putExtra("place", "");
            activity.startActivity(intent);
        } else if (position == dailyNotes) {

            moduleType = CGlobalVariables.MODULE_DAILY_NOTES;
            CUtils.googleAnalyticSendWitPlayServie(activity,
                    CGlobalVariables.GOOGLE_ANALYTIC_LAUNCH,
                    CGlobalVariables.GOOGLE_ANALYTIC_DAILY_NOTES, null);
            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_DAILY_NOTES, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

            Intent intent = new Intent(activity, NotesActivity.class);
            intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, moduleType);
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
                topAdImage.setImageUrl(topData.getImageObj().get(0).getImgurl(), VolleySingleton.getInstance(activity).getImageLoader());
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
                bottomoAdImage.setImageUrl(bottomData.getImageObj().get(0).getImgurl(), VolleySingleton.getInstance(activity).getImageLoader());
            }
        }

        if (CUtils.getUserPurchasedPlanFromPreference(activity) != 1) {
            if (bottomoAdImage != null) {
                bottomoAdImage.setVisibility(View.GONE);
            }
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

    @Override
    public void onResume() {
        super.onResume();
    }
}
