package com.ojassoft.astrosage.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.cardview.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AdData;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.model.CustomAddModel;
import com.ojassoft.astrosage.ui.act.ActAppModule;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.indnotes.NotesActivity;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;



import java.lang.reflect.Field;
import java.util.ArrayList;


public class IndNotesFrag extends Fragment {


    CardView notesCV;
    NetworkImageView networkImageView;

    static Activity activity;
    Typeface typeface;
    private static int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    CustomProgressDialog pd = null;
    private String IsShowBanner = "False";
    private String IsShowBanner1 = "False";
    private ArrayList<AdData> adList;
    AdData topAdData, bottomAdData;
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

    private void getData() {

        try {
            String result = CUtils.getStringData(getActivity(), "CUSTOMADDS", "");
            if (result != null && !result.equals("")) {
                adList = new Gson().fromJson(result, new TypeToken<ArrayList<AdData>>() {
                }.getType());
                topAdData = CUtils.getSlotData(adList, "8");
                bottomAdData = CUtils.getSlotData(adList, "9");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.indnotes_frag, container, false);
        LANGUAGE_CODE = ((AstrosageKundliApplication) getActivity().getApplicationContext())
                .getLanguageCode();
        typeface = CUtils.getRobotoFont(
                getActivity(), LANGUAGE_CODE, CGlobalVariables.regular);
        setLayRef(rootView);
        notesCV = rootView.findViewById(R.id.notesCV);
        notesCV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), NotesActivity.class);
                startActivity(intent);
            }
        });
        bottomoAdImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CUtils.googleAnalyticSendWitPlayServie(getActivity(),
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_SLOT_9_Add, null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SLOT_9_Add,CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK,"");

                CUtils.createSession(getActivity(), "S9");

                CustomAddModel modal = bottomAdData.getImageObj().get(0);

                CUtils.divertToScreen(getActivity(), modal.getImgthumbnailurl(), LANGUAGE_CODE);


            }
        });

        topAdImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CUtils.googleAnalyticSendWitPlayServie(getActivity(),
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_SLOT_8_Add, null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SLOT_8_Add,CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK,"");

                CUtils.createSession(getActivity(), "S8");

                CustomAddModel modal = topAdData.getImageObj().get(0);

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
        ((ActAppModule) getActivity()).getDataFromGTMCointainer(networkImageView);

        topAdImage = (NetworkImageView) rootView.findViewById(R.id.topAdImage);
        bottomoAdImage = (NetworkImageView) rootView.findViewById(R.id.bottomoAdImage);

        if (topAdData != null && topAdData.getImageObj() != null && topAdData.getImageObj().size() > 0) {
            setTopAdd(topAdData);
        }

        if (bottomAdData != null && bottomAdData.getImageObj() != null && bottomAdData.getImageObj().size() > 0) {
            setBottomAd(bottomAdData);
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
                bottomoAdImage.setVisibility(View.GONE); //by abhishek
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


}
