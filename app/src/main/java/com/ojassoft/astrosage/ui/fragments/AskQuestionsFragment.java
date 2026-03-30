package com.ojassoft.astrosage.ui.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.utils.PopUpLogin;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.FontUtils;

import java.util.ArrayList;


public class AskQuestionsFragment extends Fragment {
    private String viewType;
    private View view;
    TextView txtHeadingPopUp, txtSubHeadingPopUp, firstCallIn1rsTv, consultPremiumAstrologersTv;
    Button btnCallNow;
    Context context;
    ImageView imageViewExpressionIcon;
    private  String consulationType;
    public AskQuestionsFragment(String viewType) {
        this.viewType = viewType;
    }

    public AskQuestionsFragment() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            int LANGUAGE_CODE = com.ojassoft.astrosage.utils.CUtils.getLanguageCodeFromPreference(getActivity());
            com.ojassoft.astrosage.utils.CUtils.getRobotoFont(getActivity(), LANGUAGE_CODE, com.ojassoft.astrosage.utils.CGlobalVariables.regular);
        }catch (Exception e){
            //
        }
    }

    public static AskQuestionsFragment newInstance(String viewType) {
        return new AskQuestionsFragment(viewType);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_ask_questions, container, false);
        try {
            init();
        }catch (Exception e){
            //
        }
        return view;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }
    int astroImageIdRes;
    /**
     *
     */
    private void init() {
        imageViewExpressionIcon = view.findViewById(R.id.iv_astrologer_image);

        ArrayList<Integer> my_list = new ArrayList<Integer>();
        my_list.add(R.drawable.astro_dr_raman);
        my_list.add(R.drawable.astro_anita_jha);
        my_list.add(R.drawable.astro_acharya_joshi);
        my_list.add(R.drawable.astro_mr_krishnamurti);
        my_list.add(R.drawable.mom_the_astrologer);
        for (int i = 0; i < my_list.size(); i++) {
            int index = (int) (Math.random() * my_list.size());
            astroImageIdRes = my_list.get(index);
            Glide.with(getContext())
                    .load(astroImageIdRes)
                    .into(imageViewExpressionIcon);
        }
        //txtHeadingPopUp = view.findViewById(R.id.txtHeadingPopUp);
       // txtSubHeadingPopUp = view.findViewById(R.id.txtSubHeadingPopUp);
        firstCallIn1rsTv = view.findViewById(R.id.first_call_in_1rs_tv);
        consultPremiumAstrologersTv = view.findViewById(R.id.consultPremiumAstrologersTv);
        btnCallNow = view.findViewById(R.id.btnCallNow);
        FontUtils.changeFont(context, consultPremiumAstrologersTv, com.ojassoft.astrosage.utils.CGlobalVariables.FONTS_ROBOTO_MEDIUM);
        FontUtils.changeFont(context, firstCallIn1rsTv, com.ojassoft.astrosage.utils.CGlobalVariables.FONTS_ROBOTO_BLACK);
        FontUtils.changeFont(context, btnCallNow, com.ojassoft.astrosage.utils.CGlobalVariables.FONTS_ROBOTO_BOLD);

        //FontUtils.changeFont(context, txtHeadingPopUp, com.ojassoft.astrosage.utils.CGlobalVariables.FONTS_ROBOTO_BOLD);
        //FontUtils.changeFont(context, txtSubHeadingPopUp, "fonts/Roboto-Regular.ttf");
        if(viewType.equals(CGlobalVariables.NUMEROLOGY)){
            consultPremiumAstrologersTv.setText(getActivity().getResources().getString(R.string.pop_up_2sub_heading_num));
        }
        //setTextAndImages();
        consulationType = CGlobalVariables.TYPE_CALL;
        boolean enabledAIFreeChatPopup = com.ojassoft.astrosage.utils.CUtils.isAIfreeChatPopupEnabled(context);
        String firstConsultType = com.ojassoft.astrosage.varta.utils.CUtils.getStringData(context, com.ojassoft.astrosage.utils.CGlobalVariables.FIRST_CONSULT_TYPE,"");
        String secondFreeChatType = com.ojassoft.astrosage.varta.utils.CUtils.getStringData(context, com.ojassoft.astrosage.utils.CGlobalVariables.SECOND_FREE_CHAT_TYPE,"");
        if (CUtils.getUserLoginStatus(context)) {

            if (CUtils.getCallChatOfferType(context) != null && CUtils.getCallChatOfferType(context).equals(CGlobalVariables.INTRO_OFFER_TYPE_FREE)) {
                if (CUtils.isSecondFreeChat(context) && secondFreeChatType.equals(com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_FREE_CHAT_AI)) { //in case of AI enabled
                    firstCallIn1rsTv.setVisibility(View.GONE);
                    consultPremiumAstrologersTv.setText(context.getResources().getString(R.string.consult_premium_ai_astrologers));
                    btnCallNow.setText(context.getResources().getString(R.string.free_chat));
                    consulationType = CGlobalVariables.TYPE_CHAT;
                } else if(firstConsultType.equals(com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_AI_CALL)) {
                    firstCallIn1rsTv.setText(context.getResources().getString(R.string.first_calL_free));
                    consultPremiumAstrologersTv.setText(context.getResources().getString(R.string.consult_premium_ai_astrologers));
                    btnCallNow.setText(context.getResources().getString(R.string.get_free_call_now));
                    consulationType = CGlobalVariables.TYPE_CALL;
                } else if(firstConsultType.equals(com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_HUMAN_CALL)){
                    firstCallIn1rsTv.setText(context.getResources().getString(R.string.first_calL_free));
                    btnCallNow.setText(context.getResources().getString(R.string.get_free_call_now));
                    consulationType = CGlobalVariables.TYPE_CALL;
                } else {
                    firstCallIn1rsTv.setVisibility(View.VISIBLE);
                    firstCallIn1rsTv.setText(context.getResources().getString(R.string.get_your_guidance));
                    btnCallNow.setText(context.getResources().getString(R.string.first_chat_free));
                    consulationType = CGlobalVariables.TYPE_CALL;
                }
            } else {
                switch(firstConsultType){
                    case com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_AI_CALL:
                    case com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_HUMAN_CALL:
                        firstCallIn1rsTv.setVisibility(View.GONE);
                        btnCallNow.setText(context.getResources().getString(R.string.txt_chat_now));
                        consulationType = CGlobalVariables.TYPE_CALL;
                        break;
                    case com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_AI_CHAT:
                    case com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_HUMAN_CHAT:
                        firstCallIn1rsTv.setVisibility(View.GONE);
                        btnCallNow.setText(context.getResources().getString(R.string.txt_chat_now));
                        consulationType = CGlobalVariables.TYPE_CHAT;
                        break;

                }

            }


        } else {
            if (firstConsultType.equals(com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_AI_CHAT) || firstConsultType.equals(com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_HUMAN_CHAT)) { //in case of AI enabled
                firstCallIn1rsTv.setText(context.getResources().getString(R.string.first_chat_free));
                btnCallNow.setText(context.getResources().getString(R.string.free_chat));
            } else {
                firstCallIn1rsTv.setText(context.getResources().getString(R.string.first_calL_free));
                btnCallNow.setText(context.getResources().getString(R.string.get_free_call_now));
            }
        }

        btnCallNow.setOnClickListener(view -> {
            com.ojassoft.astrosage.utils.CUtils.addFcmAnalytics(astroImageIdRes);
            updateViewTypeEventsCallNow();
            if (!CUtils.getUserLoginStatus(context)) {
                try{
                    PopUpLogin popUpLogin = new PopUpLogin
                            (viewType,
                                    "FRAGMENT");
                    popUpLogin.show(getChildFragmentManager(), "PopUpFreeCall");
                }catch (Exception e){
                  //
                }

            } else {
                CUtils.popUpLoginFreeChatClicked = false;
                CUtils.popUpLoginFreeCallClicked = false;

                String offerType = CUtils.getCallChatOfferType(getActivity());

                if (!TextUtils.isEmpty(offerType) && offerType.equals(CGlobalVariables.INTRO_OFFER_TYPE_FREE)) {

                    if(CUtils.isSecondFreeChat(context) && secondFreeChatType.equals(com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_FREE_CHAT_AI)){
                        CUtils.popUpLoginFreeChatClicked = true;
                        AstrosageKundliApplication.currentEventType = CGlobalVariables.CHAT_BTN_CLICKED;
                        CUtils.fcmAnalyticsEvents("domestic_free_chat_pop_up", CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                    }else if(firstConsultType.equals(com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_AI_CALL) || firstConsultType.equals(com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_HUMAN_CALL)){
                        CUtils.popUpLoginFreeCallClicked = true;
                        AstrosageKundliApplication.currentEventType = CGlobalVariables.CALL_BTN_CLICKED;
                        CUtils.fcmAnalyticsEvents("domestic_free_call_pop_up", CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                    }else{
                        CUtils.popUpLoginFreeChatClicked = true;
                        AstrosageKundliApplication.currentEventType = CGlobalVariables.CHAT_BTN_CLICKED;
                        CUtils.fcmAnalyticsEvents("domestic_free_chat_pop_up", CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                    }

                }

                if(firstConsultType.equals(com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_AI_CHAT)
                        || firstConsultType.equals(com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_AI_CALL)){
                    CUtils.switchToConsultTab(CGlobalVariables.FILTER_TYPE_CALL, context);//redirect to AI list
                }else{
                    CUtils.switchToConsultTab(CGlobalVariables.FILTER_TYPE_CHAT, context);//redirect to Human list
                }

            }
        });
    }

    private void updateViewTypeEventsCallNow() {
        try {
            switch (viewType) {
                case CGlobalVariables.HOROSCOPE_MATCHING:
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_HM_GET_FREE_CALL_NOW, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                    break;
                case CGlobalVariables.KUNDALI:
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_K_GET_FREE_CALL_NOW, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                    break;
                case CGlobalVariables.NUMEROLOGY:
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_N_GET_FREE_CALL_NOW, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                    break;
                case CGlobalVariables.PANCHNAG:
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_P_GET_FREE_CALL_NOW, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                    break;
                case CGlobalVariables.HOROSCOPE:
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_H_GET_FREE_CALL_NOW, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                    break;
            }
        }catch (Exception e){
            //
        }
    }

//    private void setTextAndImages() {
//        try {
//            switch (viewType) {
//                case CGlobalVariables.HOROSCOPE_MATCHING:
//                    txtHeadingPopUp.setText(getActivity().getResources().getString(R.string.pop_up_heading_kundli_matching));
//                    txtSubHeadingPopUp.setText(getActivity().getResources().getString(R.string.pop_up_sub_heading_kundli_matching));
//                    imageViewExpressionIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.astrologer_icon_2));
//                    break;
//                case CGlobalVariables.KUNDALI:
//                    txtHeadingPopUp.setText(getActivity().getResources().getString(R.string.pop_up_heading_kundli));
//                    txtSubHeadingPopUp.setText(getActivity().getResources().getString(R.string.pop_up_sub_heading_kundli));
//                    imageViewExpressionIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.astrologer_icon_2));
//                    break;
//                case CGlobalVariables.NUMEROLOGY:
//                    txtHeadingPopUp.setText(getActivity().getResources().getString(R.string.pop_up_heading_num));
//                    txtSubHeadingPopUp.setText(getActivity().getResources().getString(R.string.pop_up_sub_heading_num));
//                    imageViewExpressionIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.astrologer_icon_3));
//                    break;
//                case CGlobalVariables.PANCHNAG:
//                    txtHeadingPopUp.setText(getActivity().getResources().getString(R.string.pop_up_heading_panchnag));
//                    txtSubHeadingPopUp.setText(getActivity().getResources().getString(R.string.pop_up_sub_heading_panchnag));
//                    imageViewExpressionIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.astrologer_icon_1));
//                    break;
//                case CGlobalVariables.HOROSCOPE:
//                    txtHeadingPopUp.setText(getActivity().getResources().getString(R.string.pop_up_heading_horoscope));
//                    txtSubHeadingPopUp.setText(getActivity().getResources().getString(R.string.pop_up_sub_heading_horoscope));
//                    imageViewExpressionIcon.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.astrologer_icon_1));
//                    break;
//            }
//        }catch (Exception e){
//            //
//        }
//    }
}