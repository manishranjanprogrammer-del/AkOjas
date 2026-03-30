package com.ojassoft.astrosage.ui.fragments.matching;

import android.graphics.Typeface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanOutMatchmakingNorth;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.matching.OutputMatchingMasterActivity;
import com.ojassoft.astrosage.utils.CGlobalMatching;

public class MatchingOutputDetailResultNorthFragment extends Fragment {
    //public Typeface typeface = Typeface.DEFAULT;

    public MatchingOutputDetailResultNorthFragment() {
        setRetainInstance(true);
    }

    int LANGUAGE_CODE;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //LANGUAGE_CODE = CUtils.getLanguageCodeFromPreference(getActivity());
        LANGUAGE_CODE = ((AstrosageKundliApplication) getActivity().getApplication()).getLanguageCode();//ADDED BY HEVENDRA ON 24-12-2014
        //typeface = CUtils.getUserSelectedLanguageFontType(getActivity(), LANGUAGE_CODE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lay_matching_resultdetail_north,
                container, false);
        ((TextView) view.findViewById(R.id.tvMatchingDetailHeading)).setTypeface(((OutputMatchingMasterActivity)getActivity()).regularTypeface,Typeface.BOLD);
        ((TextView) view.findViewById(R.id.tvHeadind1)).setTypeface(((OutputMatchingMasterActivity)getActivity()).regularTypeface,Typeface.BOLD);
        ((TextView) view.findViewById(R.id.tvHeadind2)).setTypeface(((OutputMatchingMasterActivity)getActivity()).regularTypeface,Typeface.BOLD);
        ((TextView) view.findViewById(R.id.tvHeadind3)).setTypeface(((OutputMatchingMasterActivity)getActivity()).regularTypeface,Typeface.BOLD);
        ((TextView) view.findViewById(R.id.tvHeadind4)).setTypeface(((OutputMatchingMasterActivity)getActivity()).regularTypeface,Typeface.BOLD);
//		((TextView) view.findViewById(R.id.tvTotalGunamilan)).setTypeface(typeface);
        ((TextView) view.findViewById(R.id.tvVarnaHd)).setTypeface(((OutputMatchingMasterActivity)getActivity()).regularTypeface);
        ((TextView) view.findViewById(R.id.tvVasyaHd)).setTypeface(((OutputMatchingMasterActivity)getActivity()).regularTypeface);
        ((TextView) view.findViewById(R.id.tvTaraHd)).setTypeface(((OutputMatchingMasterActivity)getActivity()).regularTypeface);
        ((TextView) view.findViewById(R.id.tvYoniHd)).setTypeface(((OutputMatchingMasterActivity)getActivity()).regularTypeface);
        ((TextView) view.findViewById(R.id.tvMaitriHd)).setTypeface(((OutputMatchingMasterActivity)getActivity()).regularTypeface);
        ((TextView) view.findViewById(R.id.tvGanaHd)).setTypeface(((OutputMatchingMasterActivity)getActivity()).regularTypeface);
        ((TextView) view.findViewById(R.id.tvBhakootHd)).setTypeface(((OutputMatchingMasterActivity)getActivity()).regularTypeface);
        ((TextView) view.findViewById(R.id.tvNadiHd)).setTypeface(((OutputMatchingMasterActivity)getActivity()).regularTypeface);

        ((TextView) view.findViewById(R.id.tvVarnaHd2)).setTypeface(((OutputMatchingMasterActivity)getActivity()).regularTypeface);
        ((TextView) view.findViewById(R.id.tvVasyaHd2)).setTypeface(((OutputMatchingMasterActivity)getActivity()).regularTypeface);
        ((TextView) view.findViewById(R.id.tvTaraHd2)).setTypeface(((OutputMatchingMasterActivity)getActivity()).regularTypeface);
        ((TextView) view.findViewById(R.id.tvYoniHd2)).setTypeface(((OutputMatchingMasterActivity)getActivity()).regularTypeface);
        ((TextView) view.findViewById(R.id.tvMaitriHd2)).setTypeface(((OutputMatchingMasterActivity)getActivity()).regularTypeface);
        ((TextView) view.findViewById(R.id.tvGanaHd2)).setTypeface(((OutputMatchingMasterActivity)getActivity()).regularTypeface);
        ((TextView) view.findViewById(R.id.tvBhakootHd2)).setTypeface(((OutputMatchingMasterActivity)getActivity()).regularTypeface);
        ((TextView) view.findViewById(R.id.tvNadiHd2)).setTypeface(((OutputMatchingMasterActivity)getActivity()).regularTypeface);


        initValues(view);
        return view;
    }

    private void initValues(View view) {

        TextView tvVarna = (TextView) view.findViewById(R.id.tvVarna);
        TextView tvVasya = (TextView) view.findViewById(R.id.tvVasya);
        TextView tvTara = (TextView) view.findViewById(R.id.tvTara);
        TextView tvYoni = (TextView) view.findViewById(R.id.tvYoni);
        TextView tvMaitri = (TextView) view.findViewById(R.id.tvMaitri);
        TextView tvGana = (TextView) view.findViewById(R.id.tvGana);
        TextView tvBhakoot = (TextView) view.findViewById(R.id.tvBhakoot);
        TextView tvNadi = (TextView) view.findViewById(R.id.tvNadi);
        TextView tvTotalGunamilan = (TextView) view.findViewById(R.id.tvTotalGunamilan);

        BeanOutMatchmakingNorth beanOutMatchmakingNorth = CGlobalMatching.getCGlobalMatching().getBeanOutMatchmakingNorth();
        if (beanOutMatchmakingNorth == null) {
            return;
        }
        tvVarna.setText(String.valueOf(beanOutMatchmakingNorth.getMatchPointVarna()));
        tvVasya.setText(String.valueOf(beanOutMatchmakingNorth.getMatchPointVasya()));
        tvTara.setText(String.valueOf(beanOutMatchmakingNorth.getMatchPointTara()));
        tvYoni.setText(String.valueOf(beanOutMatchmakingNorth.getMatchPointYoni()));
        tvMaitri.setText(String.valueOf(beanOutMatchmakingNorth.getMatchPointMaitri()));
        tvGana.setText(String.valueOf(beanOutMatchmakingNorth.getMatchPointGana()));
        tvBhakoot.setText(String.valueOf(beanOutMatchmakingNorth.getMatchPointBhakoot()));
        tvNadi.setText(String.valueOf(beanOutMatchmakingNorth.getMatchPointNadi()));

        tvTotalGunamilan.setText(String.valueOf(beanOutMatchmakingNorth
                .getTotalPointsObtained()));

    }
}
