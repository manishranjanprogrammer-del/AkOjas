package com.ojassoft.astrosage.varta.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;

import com.bumptech.glide.Glide;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.ChatUtils;
import com.ojassoft.astrosage.varta.utils.FontUtils;

import java.util.ArrayList;

public class PremiumAIFreeChatDialog extends AlertDialog {
    Button btnChatNow;
    ImageView cancelIv;
    TextView consultPremiumAstrologerTV, firstCallRsTV;
    ImageView iv_astrologer_image;
    String astrologerProfileUrl = "";
    String firstConsultType;
    Activity activity;
    int astroImageIdRes;
    public PremiumAIFreeChatDialog(Activity activity,String aiAstroImage){
        super(activity);
        this.activity = activity;
        this.astrologerProfileUrl = aiAstroImage;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.consult_preminum_ai_free_chat);
        consultPremiumAstrologerTV = findViewById(R.id.consult_premium_astrologers_tv);
        firstCallRsTV =  findViewById(R.id.first_call_in_1rs_tv);
        cancelIv =  findViewById(R.id.cancel_tv);
        btnChatNow =  findViewById(R.id.btnAiChatNow);
        iv_astrologer_image =  findViewById(R.id.iv_astrologer_image);
        setCancelable(false);
        ArrayList<Integer> my_list = new ArrayList<Integer>();
//        my_list.add(R.drawable.astro_dr_raman);
//        my_list.add(R.drawable.astro_anita_jha);
//        my_list.add(R.drawable.astro_acharya_joshi);
        my_list.add(R.drawable.astro_mr_krishnamurti);
//        my_list.add(R.drawable.mom_the_astrologer);
        my_list.add(R.drawable.astro_rahasya_veda);
        for (int i = 0; i < my_list.size(); i++) {
            int index = (int) (Math.random() * my_list.size());
            astroImageIdRes  = my_list.get(index);
            Glide.with(activity)
                    .load(astroImageIdRes)
                    .into(iv_astrologer_image);
        }

        FontUtils.changeFont(getContext(), consultPremiumAstrologerTV, com.ojassoft.astrosage.utils.CGlobalVariables.FONTS_ROBOTO_MEDIUM);
//        FontUtils.changeFont(getContext(), firstCallRsTV, com.ojassoft.astrosage.utils.CGlobalVariables.FONTS_ROBOTO_BLACK);
        FontUtils.changeFont(getContext(), btnChatNow, com.ojassoft.astrosage.utils.CGlobalVariables.FONTS_ROBOTO_MEDIUM);

        firstConsultType = CUtils.getStringData(activity, com.ojassoft.astrosage.utils.CGlobalVariables.FIRST_CONSULT_TYPE,"");

        if (firstConsultType.equals(com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_AI_CALL)){
            firstCallRsTV.setText(activity.getResources().getString(R.string.get_your_guidance));
            btnChatNow.setText(activity.getResources().getString(R.string.call_free_now));
        }

        String datee1 = com.ojassoft.astrosage.utils.CUtils.getDialogDate(com.ojassoft.astrosage.utils.CGlobalVariables.ONE_DAY);
        com.ojassoft.astrosage.utils.CUtils.saveSilverPlanDialogData(getContext(), true, datee1);

        com.ojassoft.astrosage.utils.CUtils.fcmAnalyticsEvents(com.ojassoft.astrosage.utils.CGlobalVariables.CONSULT_PREMIMUM_DIALOG_OPEN_AI, com.ojassoft.astrosage.utils.CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");

        cancelIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                com.ojassoft.astrosage.utils.CUtils.fcmAnalyticsEvents(com.ojassoft.astrosage.utils.CGlobalVariables.CONSULT_PREMIMUM_DIALOG_CROSS_BTN_CLICK_AI, com.ojassoft.astrosage.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                String datee = com.ojassoft.astrosage.utils.CUtils.getDialogDate(com.ojassoft.astrosage.utils.CGlobalVariables.WEEK_DAYS);
                com.ojassoft.astrosage.utils.CUtils.saveConsultPremimumDateData(getContext(), true, datee);
                dismiss();
            }
        });

        btnChatNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
              com.ojassoft.astrosage.utils.CUtils.addFcmAnalytics(astroImageIdRes);
                com.ojassoft.astrosage.utils.CUtils.createSession(getContext(), com.ojassoft.astrosage.utils.CGlobalVariables.CONSULT_PREMIMUM_DIALOG_CALL_BTN_PARTNER_ID);
                com.ojassoft.astrosage.utils.CUtils.fcmAnalyticsEvents(com.ojassoft.astrosage.utils.CGlobalVariables.CONSULT_PREMIMUM_DIALOG_CALL_BTN_CLICK_AI, com.ojassoft.astrosage.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                String datee = com.ojassoft.astrosage.utils.CUtils.getDialogDate(com.ojassoft.astrosage.utils.CGlobalVariables.WEEK_DAYS);
                com.ojassoft.astrosage.utils.CUtils.saveConsultPremimumDateData(getContext(), true, datee);
                if(firstConsultType.equals(com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_AI_CALL)){
                    ChatUtils.getInstance(activity).initAICallRandomAstrologer(CGlobalVariables.PREMIUM_AI_FREE_CALL_CHAT_DIALOG,com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_AI_CALL);
                }else {
                    CUtils.initiateRandomAiChat(activity, CGlobalVariables.PREMIUM_AI_FREE_CALL_CHAT_DIALOG, com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_AI_CHAT);
                }
                dismiss();
            }
        });


    }


}
