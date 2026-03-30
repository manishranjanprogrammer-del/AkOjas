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
import com.ojassoft.astrosage.ui.act.InputPanchangActivity;
import com.ojassoft.astrosage.ui.act.NumerologyCalculatorOutputActivity;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.util.ArrayList;

public class NumerologyHomeFragment extends Fragment {

    public int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    Activity currentActivity;
    Context context;
    private View view;
    private TextView valDestinyNumTV;
    private TextView valRadicalNumberTV;
    private TextView valNameNumberTV;
    private TextView nameLblTV;
    private TextView nameValTV;
    private TextView dobLblTV;
    private TextView dobTV;
    private TextView valRashiTV;
    private TextView lblFavRashiTV;
    private TextView lblFavAlphabetsTV;
    private TextView valFevAlphabetsTV;
    private TextView valGemstoneTV;
    private TextView valDirectionTV;
    private TextView valDayTV;
    private TextView valFavDayTV;
    private TextView lblDestinyNumTV;
    private TextView lblRadicalNumberTV;
    private TextView lblNameNumberTV;
    private TextView valFevNumberTV;
    private TextView lblFevNumberTV;
    private TextView lblFevDateTV;
    private TextView lblMantraTV;
    private TextView valMantraTV;
    private TextView lblAuspColorTV;
    private TextView valAuspColorTV;
    private TextView lblPlanetTV;
    private TextView valPlanetTV;
    private TextView lblGodTV;
    private TextView valGodTV;
    private TextView lblFastTV;
    private TextView valFastTV;

    private String name;
    private String dob;
    private NumerologyOutputModel numerologyOutputModel;
    private NetworkImageView topAdImage;
    private String IsShowBanner = "False";
    private ArrayList<AdData> adList;
    private AdData topAdData;
    private LinearLayout llCustomAdv;
    
    public NumerologyHomeFragment() {
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
        view = inflater.inflate(R.layout.fragment_numrology_home, container, false);
        initViews();
        initListner();
        return view;
    }

    private void initViews() {
        if (currentActivity == null) return;
        LANGUAGE_CODE = ((AstrosageKundliApplication) currentActivity.getApplication()).getLanguageCode();

        lblDestinyNumTV = view.findViewById(R.id.lblDestinyNumTV);
        lblRadicalNumberTV = view.findViewById(R.id.lblRadicalNumberTV);
        lblNameNumberTV = view.findViewById(R.id.lblNameNumberTV);
        valDestinyNumTV = view.findViewById(R.id.valDestinyNumTV);
        valRadicalNumberTV = view.findViewById(R.id.valRadicalNumberTV);
        valNameNumberTV = view.findViewById(R.id.valNameNumberTV);
        nameLblTV = view.findViewById(R.id.nameLblTV);
        nameValTV = view.findViewById(R.id.nameValTV);
        dobLblTV = view.findViewById(R.id.dobLblTV);
        dobTV = view.findViewById(R.id.dobTV);
        valRashiTV = view.findViewById(R.id.valRashiTV);
        valFevAlphabetsTV = view.findViewById(R.id.valFevAlphabetsTV);
        lblFavRashiTV = view.findViewById(R.id.lblFavRashiTV);
        lblFavAlphabetsTV = view.findViewById(R.id.lblFavAlphabetsTV);
        valGemstoneTV = view.findViewById(R.id.valGemstoneTV);
        valDayTV = view.findViewById(R.id.valDayTV);
        valDirectionTV = view.findViewById(R.id.valDirectionTV);
        valFevNumberTV = view.findViewById(R.id.valFevNumberTV);
        lblFevNumberTV = view.findViewById(R.id.lblFevNumberTV);
        lblFevDateTV = view.findViewById(R.id.lblFevDateTV);
        valFavDayTV = view.findViewById(R.id.valFavDayTV);
        lblMantraTV = view.findViewById(R.id.lblMantraTV);
        valMantraTV = view.findViewById(R.id.valMantraTV);
        lblAuspColorTV = view.findViewById(R.id.lblAuspColorTV);
        valAuspColorTV = view.findViewById(R.id.valAuspColorTV);
        lblPlanetTV = view.findViewById(R.id.lblPlanetTV);
        valPlanetTV = view.findViewById(R.id.valPlanetTV);
        lblGodTV = view.findViewById(R.id.lblGodTV);
        valGodTV = view.findViewById(R.id.valGodTV);
        lblFastTV = view.findViewById(R.id.lblFastTV);
        valFastTV = view.findViewById(R.id.valFastTV);
        topAdImage = (NetworkImageView) view.findViewById(R.id.topAdImage);

        lblDestinyNumTV.setTypeface(((BaseInputActivity) currentActivity).regularTypeface);
        lblRadicalNumberTV.setTypeface(((BaseInputActivity) currentActivity).regularTypeface);
        lblNameNumberTV.setTypeface(((BaseInputActivity) currentActivity).regularTypeface);
        valDestinyNumTV.setTypeface(((BaseInputActivity) currentActivity).mediumTypeface, Typeface.BOLD);
        valRadicalNumberTV.setTypeface(((BaseInputActivity) currentActivity).mediumTypeface, Typeface.BOLD);
        valNameNumberTV.setTypeface(((BaseInputActivity) currentActivity).mediumTypeface, Typeface.BOLD);
        nameLblTV.setTypeface(((BaseInputActivity) currentActivity).regularTypeface);
        dobLblTV.setTypeface(((BaseInputActivity) currentActivity).regularTypeface);
        nameValTV.setTypeface(((BaseInputActivity) currentActivity).mediumTypeface, Typeface.BOLD);
        dobTV.setTypeface(((BaseInputActivity) currentActivity).mediumTypeface, Typeface.BOLD);
        valRashiTV.setTypeface(((BaseInputActivity) currentActivity).mediumTypeface, Typeface.BOLD);
        valFevAlphabetsTV.setTypeface(((BaseInputActivity) currentActivity).mediumTypeface, Typeface.BOLD);
        lblFavRashiTV.setTypeface(((BaseInputActivity) currentActivity).regularTypeface);
        lblFavAlphabetsTV.setTypeface(((BaseInputActivity) currentActivity).regularTypeface);
        valGemstoneTV.setTypeface(((BaseInputActivity) currentActivity).mediumTypeface, Typeface.BOLD);
        valDayTV.setTypeface(((BaseInputActivity) currentActivity).mediumTypeface, Typeface.BOLD);
        lblFavAlphabetsTV.setTypeface(((BaseInputActivity) currentActivity).regularTypeface);
        valDirectionTV.setTypeface(((BaseInputActivity) currentActivity).mediumTypeface, Typeface.BOLD);
        lblFevNumberTV.setTypeface(((BaseInputActivity) currentActivity).regularTypeface);
        valFevNumberTV.setTypeface(((BaseInputActivity) currentActivity).mediumTypeface, Typeface.BOLD);
        lblFevDateTV.setTypeface(((BaseInputActivity) currentActivity).regularTypeface);
        valFavDayTV.setTypeface(((BaseInputActivity) currentActivity).mediumTypeface, Typeface.BOLD);
        lblMantraTV.setTypeface(((BaseInputActivity) currentActivity).regularTypeface);
        valMantraTV.setTypeface(((BaseInputActivity) currentActivity).mediumTypeface, Typeface.BOLD);
        lblAuspColorTV.setTypeface(((BaseInputActivity) currentActivity).regularTypeface);
        valAuspColorTV.setTypeface(((BaseInputActivity) currentActivity).mediumTypeface, Typeface.BOLD);
        lblPlanetTV.setTypeface(((BaseInputActivity) currentActivity).regularTypeface);
        valPlanetTV.setTypeface(((BaseInputActivity) currentActivity).mediumTypeface, Typeface.BOLD);
        lblGodTV.setTypeface(((BaseInputActivity) currentActivity).regularTypeface);
        valGodTV.setTypeface(((BaseInputActivity) currentActivity).mediumTypeface, Typeface.BOLD);
        lblFastTV.setTypeface(((BaseInputActivity) currentActivity).regularTypeface);
        valFastTV.setTypeface(((BaseInputActivity) currentActivity).mediumTypeface, Typeface.BOLD);

        name = ((NumerologyCalculatorOutputActivity) currentActivity).name;
        dob = ((NumerologyCalculatorOutputActivity) currentActivity).dob;
        numerologyOutputModel = ((NumerologyCalculatorOutputActivity) currentActivity).numerologyOutputModel;
        llCustomAdv = (LinearLayout) view.findViewById(R.id.llCustomAdv);
        llCustomAdv.addView(CUtils.getCustomAdvertismentView(currentActivity, false, ((NumerologyCalculatorOutputActivity) currentActivity).regularTypeface, "AKNHO"));
        initAdClickListner();
        topAdData = ((NumerologyCalculatorOutputActivity) currentActivity).getAddData("41");
        if (topAdData != null && topAdData.getImageObj() != null && topAdData.getImageObj().size() > 0) {
            setTopAdd(topAdData);
        }
        if (currentActivity != null)
            ((NumerologyCalculatorOutputActivity) currentActivity).addExtraSpaceInBottom(view);

        setDataOnViews();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void setDataOnViews() {
        if(numerologyOutputModel == null) return;
        valDestinyNumTV.setText(numerologyOutputModel.getDestinyNumber());  // Bhagyank
        valRadicalNumberTV.setText(numerologyOutputModel.getRadicalNumber()); // Mulank
        valNameNumberTV.setText(numerologyOutputModel.getNameNumber()); // Namank
        nameValTV.setText(name);
        dobTV.setText(dob);
        
        valRashiTV.setText(numerologyOutputModel.getFavourableSign());
        valFevAlphabetsTV.setText(numerologyOutputModel.getFavourableAlphabets());
        valGemstoneTV.setText(numerologyOutputModel.getGemstone());
        valDirectionTV.setText(numerologyOutputModel.getDirection());
        valDayTV.setText(numerologyOutputModel.getFavourableDay());
        valFavDayTV.setText(numerologyOutputModel.getFavourableDate());
        valAuspColorTV.setText(numerologyOutputModel.getAuspiciousColour());
        valPlanetTV.setText(numerologyOutputModel.getRulingPlanet());
        valGodTV.setText(numerologyOutputModel.getGod());
        valFastTV.setText(numerologyOutputModel.getFast());
        valMantraTV.setText(numerologyOutputModel.getMantra());
        valFevNumberTV.setText(numerologyOutputModel.getFavourableNumber());

    }

    private void initAdClickListner(){
        topAdImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CUtils.googleAnalyticSendWitPlayServie(currentActivity,
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_SLOT_41_Add, null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SLOT_41_Add, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                CUtils.createSession(currentActivity, "S41");
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
