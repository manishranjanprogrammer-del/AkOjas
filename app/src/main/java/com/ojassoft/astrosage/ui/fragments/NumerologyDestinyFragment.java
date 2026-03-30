package com.ojassoft.astrosage.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AdData;
import com.ojassoft.astrosage.model.CustomAddModel;
import com.ojassoft.astrosage.model.NumerologyOutputModel;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.act.NumerologyCalculatorOutputActivity;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.util.ArrayList;

public class NumerologyDestinyFragment extends Fragment {

    public int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    Activity currentActivity;
    Context context;
    private View view;
    private TextView destinyNumTV;
    private TextView valDestinyNumTV;
    private TextView lblHeadingTV;

    private NetworkImageView topAdImage;
    private String IsShowBanner = "False";
    private ArrayList<AdData> adList;
    private AdData topAdData;
    private LinearLayout llCustomAdv;

    private NumerologyOutputModel numerologyOutputModel;

    public NumerologyDestinyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        currentActivity = getActivity();
        context = getActivity();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_numrology_destiny, container, false);
        initViews();
        initListner();
        return view;
    }

    private void initViews() {
        if (currentActivity == null) return;
        LANGUAGE_CODE = ((AstrosageKundliApplication) currentActivity.getApplication()).getLanguageCode();
        destinyNumTV = view.findViewById(R.id.destinyNumTV);
        valDestinyNumTV = view.findViewById(R.id.valDestinyNumTV);
        lblHeadingTV = view.findViewById(R.id.lblHeadingTV);
        topAdImage = (NetworkImageView) view.findViewById(R.id.topAdImage);

        valDestinyNumTV.setTypeface(((BaseInputActivity) currentActivity).mediumTypeface, Typeface.BOLD);
        destinyNumTV.setTypeface(((BaseInputActivity) currentActivity).regularTypeface);
        lblHeadingTV.setTypeface(((BaseInputActivity) currentActivity).mediumTypeface);

        if (currentActivity != null) {
            numerologyOutputModel = ((NumerologyCalculatorOutputActivity) currentActivity).numerologyOutputModel;
            ((NumerologyCalculatorOutputActivity) currentActivity).addExtraSpaceInBottom(view);
        }
        llCustomAdv = (LinearLayout) view.findViewById(R.id.llCustomAdv);
        llCustomAdv.addView(CUtils.getCustomAdvertismentView(currentActivity, false, ((NumerologyCalculatorOutputActivity) currentActivity).regularTypeface, "AKNDE"));
        initAdClickListner();
        topAdData = ((NumerologyCalculatorOutputActivity) currentActivity).getAddData("43");
        if (topAdData != null && topAdData.getImageObj() != null && topAdData.getImageObj().size() > 0) {
            setTopAdd(topAdData);
        }

        if (numerologyOutputModel != null) {
            valDestinyNumTV.setText(numerologyOutputModel.getDestinyNumber());
            destinyNumTV.setText(numerologyOutputModel.getDestinyNumberDesc());
        }
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void initAdClickListner(){
        topAdImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CUtils.googleAnalyticSendWitPlayServie(currentActivity,
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_SLOT_43_Add, null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SLOT_43_Add, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                CUtils.createSession(currentActivity, "S43");
                CustomAddModel modal = topAdData.getImageObj().get(0);
                CUtils.divertToScreen(getActivity(), modal.getImgthumbnailurl(), LANGUAGE_CODE);
            }
        });
    }

    public void setTopAdd(AdData topData) {
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
                topAdImage.setImageUrl(topData.getImageObj().get(0).getImgurl(), com.libojassoft.android.misc.VolleySingleton.getInstance(currentActivity).getImageLoader());
            }
        }

        if (CUtils.getUserPurchasedPlanFromPreference(currentActivity) != 1) {
            if (topAdImage != null) {
                topAdImage.setVisibility(View.GONE);
            }
        }

    }

    private void initListner() {

    }

}
