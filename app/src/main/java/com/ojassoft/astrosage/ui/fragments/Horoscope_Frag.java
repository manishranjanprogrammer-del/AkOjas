package com.ojassoft.astrosage.ui.fragments;

import android.app.Activity;
import android.content.Context;
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
import com.ojassoft.astrosage.ui.customviews.basic.ScrollableGridView;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amit Rautela on 23/2/16.
 * This fragment is used to show kundli related modules
 */
public class Horoscope_Frag extends Fragment {

    public final static int MODULE_HOROSCOPE_DAILY = 0;
    public final static int MODULE_HOROSCOPE_WEEKLY = 1;
    public final static int MODULE_HOROSCOPE_WEEKLYLOVE = 2;
    public final static int MODULE_HOROSCOPE_MONTHLY = 3;
    public final static int MODULE_HOROSCOPE_YEARLY = 4;


    public Integer[] moduleIconList = {
            R.drawable.ic_today,
            R.drawable.ic_weekly,
            R.drawable.ic_weekly_love,
            R.drawable.ic_monthly,
            R.drawable.ic_varshfal};

    private String[] moduleNameList;

    ScrollableGridView gridView;
    NetworkImageView networkImageView;

    //Typeface typeface;

    Activity activity;

    // add for showimg custom adds

    private String cacheResult;
    private static int NUM_PAGES = 0;
    private static int currentPage = 0;
    private List<CustomAddModel> customaddmodel;
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
        getData();
    }


    private void getData()

    {

        try {
            String result = CUtils.getStringData(activity, "CUSTOMADDS", "");
            if (result != null && !result.equals("")) {
                adList = new Gson().fromJson(result, new TypeToken<ArrayList<AdData>>() {
                }.getType());
                topAdData = CUtils.getSlotData(adList, "6");
                bottomAdData = CUtils.getSlotData(adList, "7");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


   /* @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        // TODO Auto-generated method stub
        if (this.isVisible()) {
            // If we are becoming invisible, then...
            try {
                String result = CUtils.getStringData(activity, "CUSTOMADDS", "");
                if (result != null && !result.equals("")) {
                    adList = new Gson().fromJson(result, new TypeToken<ArrayList<AdData>>() {
                    }.getType());
                    topAdData = CUtils.getSlotData(adList,"6");
                    bottomAdData= CUtils.getSlotData(adList,"7");
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
        LANGUAGE_CODE = ((AstrosageKundliApplication) activity.getApplicationContext())
                .getLanguageCode();
        typeface = CUtils.getRobotoFont(
                activity, LANGUAGE_CODE, CGlobalVariables.regular);
        // typeface=((ActAppModule)activity).typeface;
        setLayRef(rootView);

        bottomoAdImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CUtils.googleAnalyticSendWitPlayServie(activity,
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_SLOT_7_Add, null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SLOT_7_Add, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                CUtils.createSession(activity, "S7");

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
                        CGlobalVariables.GOOGLE_ANALYTIC_SLOT_6_Add, null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SLOT_6_Add, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                CUtils.createSession(activity, "S6");

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
        moduleNameList = getResources().getStringArray(R.array.horoscopeModulesList);
        gridView = (ScrollableGridView) rootView.findViewById(R.id.gridView);
        gridView.setVisibility(View.VISIBLE);
        MainModulesAdapter adapter = new MainModulesAdapter(activity, moduleIconList, moduleNameList, ((BaseInputActivity) activity).mediumTypeface, Horoscope_Frag.this);
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


        //////////


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


    /**
     * @param position
     * @created by : Amit Rautela
     * @created on : 23/2/16.
     */
    public static void callActivity(int position, Context context) {

        Activity activity = (Activity) context;
        if (position > 0) {
            position = position + 1;
        }
        if (position == 0) {
            CUtils.googleAnalyticSendWitPlayServie(activity,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                    CGlobalVariables.GOOGLE_ANALYTIC_HOROSCOPE_DAILY, null);
            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SLOT_6_Add, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

        } else if (position == 1) {
            CUtils.googleAnalyticSendWitPlayServie(activity,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                    CGlobalVariables.GOOGLE_ANALYTIC_PANCHANG_PANCHANG, null);
            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_PANCHANG_PANCHANG,CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN,"");


        } else if (position == 2) {
            CUtils.googleAnalyticSendWitPlayServie(activity,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                    CGlobalVariables.GOOGLE_ANALYTIC_HOROSCOPE_WEEKLY, null);
            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_HOROSCOPE_WEEKLY,CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN,"");

        } else if (position == 3) {
            CUtils.googleAnalyticSendWitPlayServie(activity,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                    CGlobalVariables.GOOGLE_ANALYTIC_HOROSCOPE_WEEKLY_LOVE, null);
            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_HOROSCOPE_WEEKLY_LOVE,CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN,"");

        } else if (position == 4) {
            CUtils.googleAnalyticSendWitPlayServie(activity,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                    CGlobalVariables.GOOGLE_ANALYTIC_HOROSCOPE_MONTHLY, null);
            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_HOROSCOPE_MONTHLY,CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN,"");

        } else if (position == 5) {
            CUtils.googleAnalyticSendWitPlayServie(activity,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                    CGlobalVariables.GOOGLE_ANALYTIC_HOROSCOPE_YEARLY, null);
            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_HOROSCOPE_YEARLY,CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN,"");

        }


        CUtils.callHoroscopeActivity(activity, CGlobalVariables.MODULE_HOROSCOPE, position, 1);

    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
