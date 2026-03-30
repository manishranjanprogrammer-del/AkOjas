package com.ojassoft.astrosage.ui.fragments;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.libojassoft.android.customrssfeed.YoutubeVideoBean;
import com.libojassoft.android.misc.VolleyResponse;
import com.libojassoft.android.misc.VolleySingleton;
import com.libojassoft.android.utils.LibCUtils;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AdData;
import com.ojassoft.astrosage.customadapters.RecyclerYoutubeCardAdapter;
import com.ojassoft.astrosage.model.CustomAddModel;
import com.ojassoft.astrosage.model.VideoListModel;
import com.ojassoft.astrosage.ui.act.ActAppModule;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by Vijay Pathak on 14/1/2019
 * This fragment is used to video playlist
 */
public class Video_Frag extends Fragment implements VolleyResponse {


    static Activity activity;
    NetworkImageView networkImageView;
    RecyclerView videoRecyclerView;
    ArrayList<YoutubeVideoBean> youtubeListItems;
    Typeface typeface;
    AdData topAdData, bottomAdData;
    RecyclerYoutubeCardAdapter recyclerVideoCardAdapter;
    int currentYear;
    int GET_VIDEO_DATA = 0;
    private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    private String IsShowBanner = "False";
    private String IsShowBanner1 = "False";
    private ArrayList<AdData> adList;
    private NetworkImageView topAdImage, bottomoAdImage;
    private LinearLayout progressbarLL;
    private String playListUrl;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        Video_Frag.activity = activity;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getData();
        currentYear = Calendar.getInstance().get(Calendar.YEAR);
        setYouTubeUrl();
    }

    private void setYouTubeUrl() {
        playListUrl = CGlobalVariables.youtubeRssFeedUrl;
    }

    private void getData() {

        try {
            String result = CUtils.getStringData(activity, "CUSTOMADDS", "");
            if (result != null && !result.equals("")) {
                adList = new Gson().fromJson(result, new TypeToken<ArrayList<AdData>>() {
                }.getType());
                topAdData = CUtils.getSlotData(adList, "29");
                bottomAdData = CUtils.getSlotData(adList, "30");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.lay_video_list, container, false);
        LANGUAGE_CODE = ((AstrosageKundliApplication) activity.getApplicationContext())
                .getLanguageCode();
        typeface = CUtils.getRobotoFont(
                activity, LANGUAGE_CODE, CGlobalVariables.regular);
        setLayRef(rootView);

        CUtils.googleAnalyticSendWitPlayServie(getActivity(),
                CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                CGlobalVariables.GOOGLE_ANALYTIC_VIDEO, null);
        CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_VIDEO, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

        initView();
        return rootView;
    }

    private void initView() {

        youtubeListItems = VideoListModel.getInstance().getYoutubeListItems();
        if (youtubeListItems == null) {
            youtubeListItems = new ArrayList();
        }
        setYoutubeAdapter();
        if (youtubeListItems.size() == 0) {
            prepareYoutubeData(true);
        }

        bottomoAdImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CUtils.googleAnalyticSendWitPlayServie(activity,
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_SLOT_30_Add, null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SLOT_30_Add, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                CUtils.createSession(activity, "S30");

                CustomAddModel modal = bottomAdData.getImageObj().get(0);

                CUtils.divertToScreen(getActivity(), modal.getImgthumbnailurl(), LANGUAGE_CODE);

            }
        });

        topAdImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CUtils.googleAnalyticSendWitPlayServie(activity,
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_SLOT_29_Add, null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SLOT_29_Add, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                CUtils.createSession(activity, "S29");

                CustomAddModel modal = topAdData.getImageObj().get(0);

                CUtils.divertToScreen(getActivity(), modal.getImgthumbnailurl(), LANGUAGE_CODE);


            }
        });

    }

    public void updateData() {
       /* if (activity == null || videoRecyclerView == null) return;
        setYouTubeUrl();
        initView();*/
    }


    /**
     * @created by : Amit Rautela
     * @created on : 23/2/16.
     * @desc : This method is used to look up the layout reference
     */
    private void setLayRef(View rootView) {
        videoRecyclerView = rootView.findViewById(R.id.video_recycler_view);
        videoRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        progressbarLL = rootView.findViewById(R.id.progressbarLL);

        networkImageView = rootView.findViewById(R.id.imgBanner);
        ((ActAppModule) activity).getDataFromGTMCointainer(networkImageView);
        topAdImage = rootView.findViewById(R.id.topAdImage);
        bottomoAdImage = rootView.findViewById(R.id.bottomoAdImage);

        /////////////
        if (topAdData != null && topAdData.getImageObj() != null && topAdData.getImageObj().size() > 0) {
            setTopAdd(topAdData);
        }

        if (bottomAdData != null && bottomAdData.getImageObj() != null && bottomAdData.getImageObj().size() > 0) {
            setBottomAd(bottomAdData);
        }

    }

    private void setYoutubeAdapter() {
        if (activity == null || videoRecyclerView == null) return;
        recyclerVideoCardAdapter = new RecyclerYoutubeCardAdapter(youtubeListItems, videoRecyclerView, activity);
        videoRecyclerView.setAdapter(recyclerVideoCardAdapter);
    }

    private void prepareYoutubeData(boolean showPD) {
        if (activity == null) return;
        if (CUtils.isConnectedWithInternet(activity)) {
            if (TextUtils.isEmpty(playListUrl)) return;
            getVideoData(playListUrl, showPD);
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
        activity = null;
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }


    private void getVideoData(String url, boolean isShowPD) {
        if (isShowPD) {
            progressbarLL.setVisibility(View.VISIBLE);
        }
        CUtils.makeGetRequest(Video_Frag.this, url, GET_VIDEO_DATA);
    }

    @Override
    public void onResponse(String response, int method) {

        if (method == GET_VIDEO_DATA) {
            if (activity == null) return;
            progressbarLL.setVisibility(View.GONE);

            try {

                youtubeListItems = (ArrayList<YoutubeVideoBean>) LibCUtils.parseYoutubeRssFeedXML(response);
                ((RecyclerYoutubeCardAdapter) videoRecyclerView.getAdapter()).setData(youtubeListItems);
                VideoListModel.getInstance().setYoutubeListItems(youtubeListItems);

            } catch (Exception e) {
                //Log.i("Error>>", e.getMessage());
            }
        }
    }

    @Override
    public void onError(VolleyError error) {
        if (progressbarLL != null) {
            progressbarLL.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }
}
