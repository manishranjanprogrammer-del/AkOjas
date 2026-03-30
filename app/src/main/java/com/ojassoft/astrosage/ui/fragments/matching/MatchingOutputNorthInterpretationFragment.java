package com.ojassoft.astrosage.ui.fragments.matching;

import android.graphics.Typeface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AdData;
import com.ojassoft.astrosage.model.CustomAddModel;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.matching.OutputMatchingMasterActivity;
import com.ojassoft.astrosage.utils.CGlobalMatching;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.util.ArrayList;

public class MatchingOutputNorthInterpretationFragment extends Fragment {
    int interpType = 1;
    int LANGUAGE_CODE;

    private ArrayList<AdData> adList;
    private NetworkImageView bottomoAdImage;
    AdData bottomAdData;
    private  String IsShowBanner1="False";
    //public Typeface typeface = Typeface.DEFAULT;

    public MatchingOutputNorthInterpretationFragment() {
        setRetainInstance(true);
    }

    public static MatchingOutputNorthInterpretationFragment newInstance(int subModuleId,AdData bottomAdData) {
        MatchingOutputNorthInterpretationFragment matchingOutputNorthInterpretationFragment = new MatchingOutputNorthInterpretationFragment();

        Bundle args = new Bundle();
        args.putInt("subModuleId", subModuleId);
        args.putSerializable("DATA",bottomAdData);
        matchingOutputNorthInterpretationFragment.setArguments(args);

        return matchingOutputNorthInterpretationFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        //LANGUAGE_CODE = CUtils.getLanguageCodeFromPreference(getActivity());
        LANGUAGE_CODE = ((AstrosageKundliApplication) getActivity().getApplication()).getLanguageCode();//ADDED BY HEVENDRA ON 24-12-2014
        //typeface = CUtils.getUserSelectedLanguageFontType(getActivity(), LANGUAGE_CODE);
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.interpType = getArguments().getInt("subModuleId", 0);
        this.bottomAdData=(AdData) getArguments().getSerializable("DATA");

        View view = inflater.inflate(R.layout.lay_matching_interpretation,
                container, false);

        initValues(view);
        return view;
    }

    private void initValues(View view) {
        TextView tvMatchingInterpretationDetail = (TextView) view
                .findViewById(R.id.tvMatchingInterpretationDetail);
        TextView tvMatchingInterpretationHeading = (TextView) view
                .findViewById(R.id.tvMatchingInterpretationHeading);
        tvMatchingInterpretationHeading.setTypeface(((OutputMatchingMasterActivity)getActivity()).regularTypeface,Typeface.BOLD);
        tvMatchingInterpretationHeading.setText(getResources().getStringArray(
                R.array.matching_detail_heading_list)[interpType]);
        bottomoAdImage=(NetworkImageView) view.findViewById(R.id.adImage);

        if (interpType == OutputMatchingMasterActivity.VARNA)
            tvMatchingInterpretationDetail.setText(CGlobalMatching
                    .getCGlobalMatching().getBeanOutMatchmakingNorth()
                    .getVarnaPrediction());

        if (interpType == OutputMatchingMasterActivity.VASYA)
            tvMatchingInterpretationDetail.setText(CGlobalMatching
                    .getCGlobalMatching().getBeanOutMatchmakingNorth()
                    .getVasyaPrediction());

        if (interpType == OutputMatchingMasterActivity.TARA)
            tvMatchingInterpretationDetail.setText(CGlobalMatching
                    .getCGlobalMatching().getBeanOutMatchmakingNorth()
                    .getTaraPrediction());

        if (interpType == OutputMatchingMasterActivity.YONI)
            tvMatchingInterpretationDetail.setText(CGlobalMatching
                    .getCGlobalMatching().getBeanOutMatchmakingNorth()
                    .getYoniPrediction());

        if (interpType == OutputMatchingMasterActivity.MAITRI)
            tvMatchingInterpretationDetail.setText(CGlobalMatching
                    .getCGlobalMatching().getBeanOutMatchmakingNorth()
                    .getMaitriPrediction());

        if (interpType == OutputMatchingMasterActivity.GANA)
            tvMatchingInterpretationDetail.setText(CGlobalMatching
                    .getCGlobalMatching().getBeanOutMatchmakingNorth()
                    .getGanaPrediction());

        if (interpType == OutputMatchingMasterActivity.BHAKOOT)
            tvMatchingInterpretationDetail.setText(CGlobalMatching
                    .getCGlobalMatching().getBeanOutMatchmakingNorth()
                    .getBhakootPrediction());

        if (interpType == OutputMatchingMasterActivity.NADI)
            tvMatchingInterpretationDetail.setText(CGlobalMatching
                    .getCGlobalMatching().getBeanOutMatchmakingNorth()
                    .getNadiPrediction());


        bottomoAdImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch(interpType)
                {
                    case 1:
                        CUtils.googleAnalyticSendWitPlayServie(getActivity(),
                                CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                                CGlobalVariables.GOOGLE_ANALYTIC_SLOT_11_Add, null);
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SLOT_11_Add,CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK,"");

                        CUtils.createSession(getActivity(),"S11");

                        break;
                    case 2:
                        CUtils.googleAnalyticSendWitPlayServie(getActivity(),
                                CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                                CGlobalVariables.GOOGLE_ANALYTIC_SLOT_12_Add, null);
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SLOT_12_Add,CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK,"");

                        CUtils.createSession(getActivity(),"S12");

                        break;
                    case 3:
                        CUtils.googleAnalyticSendWitPlayServie(getActivity(),
                                CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                                CGlobalVariables.GOOGLE_ANALYTIC_SLOT_13_Add, null);
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SLOT_13_Add,CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK,"");

                        CUtils.createSession(getActivity(),"S13");

                        break;
                    case 4:
                        CUtils.googleAnalyticSendWitPlayServie(getActivity(),
                                CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                                CGlobalVariables.GOOGLE_ANALYTIC_SLOT_14_Add, null);
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SLOT_14_Add,CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK,"");

                        CUtils.createSession(getActivity(),"S14");

                        break;
                    case 5:
                        CUtils.googleAnalyticSendWitPlayServie(getActivity(),
                                CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                                CGlobalVariables.GOOGLE_ANALYTIC_SLOT_15_Add, null);
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SLOT_15_Add,CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK,"");

                        CUtils.createSession(getActivity(),"S15");

                        break;
                    case 6:
                        CUtils.googleAnalyticSendWitPlayServie(getActivity(),
                                CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                                CGlobalVariables.GOOGLE_ANALYTIC_SLOT_16_Add, null);
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SLOT_16_Add,CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK,"");

                        CUtils.createSession(getActivity(),"S16");

                        break;
                    case 7:
                        CUtils.googleAnalyticSendWitPlayServie(getActivity(),
                                CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                                CGlobalVariables.GOOGLE_ANALYTIC_SLOT_17_Add, null);
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SLOT_17_Add,CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK,"");

                        CUtils.createSession(getActivity(),"S17");

                        break;
                    case 8:
                    CUtils.googleAnalyticSendWitPlayServie(getActivity(),
                            CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                            CGlobalVariables.GOOGLE_ANALYTIC_SLOT_18_Add, null);
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SLOT_18_Add,CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK,"");

                        CUtils.createSession(getActivity(),"S18");

                        break;
                    default:
                        CUtils.googleAnalyticSendWitPlayServie(getActivity(),
                                CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                                CGlobalVariables.GOOGLE_ANALYTIC_SLOT_11_Add, null);
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SLOT_11_Add,CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK,"");

                        CUtils.createSession(getActivity(),"S11");

                        break;



                }



                CustomAddModel modal=bottomAdData.getImageObj().get(0);

               /* if(modal.getImgthumbnailurl().contains(CGlobalVariables.buy_astrosage_url)){
                    CUtils.getUrlLink(modal.getImgthumbnailurl(), getActivity(),LANGUAGE_CODE, 0);
                }
                else{
                    if(modal.getImgthumbnailurl()!=null && !modal.getImgthumbnailurl().isEmpty()) {
                        Uri uri = Uri.parse(modal.getImgthumbnailurl());
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(uri);
                        startActivity(i);
                    }
                }*/
                CUtils.divertToScreen(getActivity(),modal.getImgthumbnailurl(),LANGUAGE_CODE);


            }
        });

        if (bottomAdData!=null && bottomAdData.getImageObj() != null && bottomAdData.getImageObj().size() > 0) {
            setBottomAd(bottomAdData);
        }


    }


    public void setBottomAd(AdData bottomData) {
        if (bottomData != null) {
            IsShowBanner1 = bottomData.getIsShowBanner();
            IsShowBanner1 = IsShowBanner1 == null ? "" : IsShowBanner1;

        }
        if (bottomAdData == null || bottomData.getImageObj() == null || bottomData.getImageObj().size() <= 0 || IsShowBanner1.equalsIgnoreCase("False")) {
            if(bottomoAdImage!=null)
            {
                bottomoAdImage.setVisibility(View.GONE);
            }
        } else {
            if(bottomoAdImage!=null) {
                bottomoAdImage.setVisibility(View.VISIBLE);
                bottomoAdImage.setImageUrl(bottomData.getImageObj().get(0).getImgurl(), VolleySingleton.getInstance(getActivity()).getImageLoader());
            }
        }

        if(CUtils.getUserPurchasedPlanFromPreference(getActivity())!=1)
        {
            if (bottomoAdImage != null) {
                bottomoAdImage.setVisibility(View.GONE);
            }
        }

    }
}
