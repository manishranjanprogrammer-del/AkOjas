package com.ojassoft.astrosage.vartalive.fragments;

import android.content.Context;
import android.graphics.Paint;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.vartalive.activities.LiveActivityNew;

public class LiveIntroFragment extends Fragment implements View.OnClickListener {

    View view;
    Context context;
    private TextView tvSkipIntro;
    private RelativeLayout rlIntroCall;
    private ImageView ivIntroCall;
    private LinearLayout llIntroCall;
    private ImageView ivIntroGiftNext;
    private ImageView ivIntroGift;
    private LinearLayout llIntroGift;
    private ImageView ivIntroReport;
    private ImageView ivIntroReportNext;
    private LinearLayout llIntroReport;
    private ImageView ivIntroSpeaker;
    private ImageView ivIntroSpeakerNext;
    private LinearLayout llIntroSpeaker;
    private ImageView ivIntroNext;
    private ImageView ivIntroNNext;
    private LinearLayout llIntroNext;
    private LinearLayout llIntroSend;
    private TextView tvIntroDone;
    private TextView tvIntroSend;
    private ImageView ivIntro1;
    private ImageView ivIntro2;
    private ImageView ivIntro3;
    private ImageView ivIntro4;
    private ImageView ivIntro5;
    private ImageView ivIntro6;
    private TextView tvReport;
    private TextView tvCall;
    private TextView tvGift;
    private TextView tvSend;
    private TextView tvSpeaker;
    private TextView tvNext;
    private TextView  tvPriceToCross,tvPriceToCall;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_live_intro, container, false);
        context = getContext();

        tvSkipIntro = view.findViewById(R.id.tvIntroSkip);
        rlIntroCall = view.findViewById(R.id.rlIntroCall);
        llIntroCall = view.findViewById(R.id.llIntroCall);
        ivIntroCall = view.findViewById(R.id.ivIntroCall);
        ivIntroGiftNext = view.findViewById(R.id.ivIntroGiftNext);
        llIntroGift = view.findViewById(R.id.llIntroGift);
        ivIntroGift = view.findViewById(R.id.ivIntroGift);
        ivIntroReportNext = view.findViewById(R.id.ivIntroReportNext);
        llIntroReport = view.findViewById(R.id.llIntroReport);
        ivIntroReport = view.findViewById(R.id.ivIntroReport);
        ivIntroSpeakerNext = view.findViewById(R.id.ivIntroSpeakerNext);
        llIntroSpeaker = view.findViewById(R.id.llIntroSpeaker);
        ivIntroSpeaker = view.findViewById(R.id.ivIntroSpeaker);
        ivIntroNext = view.findViewById(R.id.ivIntroNext);
        llIntroNext = view.findViewById(R.id.llIntroNext);
        ivIntroNNext = view.findViewById(R.id.ivIntroNNext);
        tvIntroDone = view.findViewById(R.id.tvIntroDone);
        llIntroSend = view.findViewById(R.id.llIntroSend);
        tvIntroSend = view.findViewById(R.id.etIntroSend);
        ivIntro1 = view.findViewById(R.id.ivIntro1);
        ivIntro2 = view.findViewById(R.id.ivIntro2);
        ivIntro3 = view.findViewById(R.id.ivIntro3);
        ivIntro4 = view.findViewById(R.id.ivIntro4);
        ivIntro5 = view.findViewById(R.id.ivIntro5);
        ivIntro6 = view.findViewById(R.id.ivIntro6);
        tvReport = view.findViewById(R.id.tvReport);
        tvSpeaker = view.findViewById(R.id.tvSpeaker);
        tvCall = view.findViewById(R.id.tvCall);
        tvSend = view.findViewById(R.id.tvSend);
        tvNext = view.findViewById(R.id.tvNext);
        tvGift = view.findViewById(R.id.tvGift);
        tvPriceToCall = view.findViewById(R.id.tvPriceToCall);
        tvPriceToCross = view.findViewById(R.id.tvPriceToCross);
        //tvPriceToCross.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
        FontUtils.changeFont(context, tvReport, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(context, tvSpeaker, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(context, tvCall, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(context, tvSend, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(context, tvNext, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(context, tvGift, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);

        tvSkipIntro.setOnClickListener(this);
        ivIntroCall.setOnClickListener(this);
        ivIntroGiftNext.setOnClickListener(this);
        ivIntroReportNext.setOnClickListener(this);
        ivIntroSpeakerNext.setOnClickListener(this);
        ivIntroNNext.setOnClickListener(this);
        tvIntroDone.setOnClickListener(this);
        initCallPrice();
        return view;
    }
    private void initCallPrice() {
        tvPriceToCall.setText(LiveActivityNew.showAstroPrice);
        //tvPriceToCross.setText(LiveActivityNew.crossAstroPrice);
        CUtils.setStrikeOnTextView(tvPriceToCross, LiveActivityNew.crossAstroPrice);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tvIntroSkip:
            case R.id.tvIntroDone:
                CUtils.saveBooleanData(context,CGlobalVariables.SHOWN_LIVE_INTRO,true);
                ((LiveActivityNew)getActivity()).hideIntroFragment();
                break;
            case R.id.ivIntroCall:
                rlIntroCall.setVisibility(View.INVISIBLE);
                llIntroCall.setVisibility(View.INVISIBLE);
                llIntroGift.setVisibility(View.VISIBLE);
                ivIntroGift.setVisibility(View.VISIBLE);
                ivIntro1.setImageDrawable(null);
                ivIntro2.setImageDrawable(getResources().getDrawable(R.drawable.circle_orange));
                break;
            case R.id.ivIntroGiftNext:
                llIntroGift.setVisibility(View.INVISIBLE);
                ivIntroGift.setVisibility(View.INVISIBLE);
                llIntroReport.setVisibility(View.VISIBLE);
                ivIntroReport.setVisibility(View.VISIBLE);
                ivIntro2.setImageDrawable(null);
                ivIntro3.setImageDrawable(getResources().getDrawable(R.drawable.circle_orange));
                break;
            case R.id.ivIntroReportNext:
                llIntroReport.setVisibility(View.INVISIBLE);
                ivIntroReport.setVisibility(View.INVISIBLE);
                llIntroSpeaker.setVisibility(View.VISIBLE);
                ivIntroSpeaker.setVisibility(View.VISIBLE);
                ivIntro3.setImageDrawable(null);
                ivIntro4.setImageDrawable(getResources().getDrawable(R.drawable.circle_orange));
                break;
            case R.id.ivIntroSpeakerNext:
                llIntroSpeaker.setVisibility(View.INVISIBLE);
                ivIntroSpeaker.setVisibility(View.INVISIBLE);
                llIntroNext.setVisibility(View.VISIBLE);
                ivIntroNext.setVisibility(View.VISIBLE);
                ivIntro4.setImageDrawable(null);
                ivIntro5.setImageDrawable(getResources().getDrawable(R.drawable.circle_orange));
                break;
            case R.id.ivIntroNNext:
                llIntroNext.setVisibility(View.INVISIBLE);
                ivIntroNext.setVisibility(View.INVISIBLE);
                tvIntroSend.setVisibility(View.VISIBLE);
                llIntroSend.setVisibility(View.VISIBLE);
                tvSkipIntro.setText(getResources().getString(R.string.done));
                ivIntro5.setImageDrawable(null);
                ivIntro6.setImageDrawable(getResources().getDrawable(R.drawable.circle_orange));
                break;
        }
    }
}