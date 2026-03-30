package com.ojassoft.astrosage.varta.dialog;

import android.content.DialogInterface;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.varta.ui.activity.DashBoardActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.FontUtils;

import org.json.JSONObject;

import java.util.ArrayList;

public class BottomSheetDialogFreeAIChat extends BottomSheetDialogFragment {

    public BottomSheetDialogFreeAIChat(){
        //
    }
    int astroImageIdRes;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable
    ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.free_consult_ai_astrologer_pop_under, container, false);
        inti(v);
        return v;
    }

    private void inti(View v) {
        Button btnAiChatNow = v.findViewById(R.id.button_chat_now);
        TextView textView1 = v.findViewById(R.id.textView_1);
        TextView textView2 = v.findViewById(R.id.textView_2);
        TextView textView3 = v.findViewById(R.id.textView_3);
        ImageView ivAstrologerImage = v.findViewById(R.id.iv_astrologer_image);

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
                    .into(ivAstrologerImage);
        }

        FontUtils.changeFont(getContext(), btnAiChatNow, "fonts/Roboto-Bold.ttf");
        FontUtils.changeFont(getContext(), textView1, "fonts/Roboto-Regular.ttf");
        FontUtils.changeFont(getContext(), textView2, "fonts/Roboto-Medium.ttf");
        FontUtils.changeFont(getContext(), textView3, "fonts/Roboto-Medium.ttf");

        ImageView crossButton = v.findViewById(R.id.iv_close);
        crossButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        btnAiChatNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (getContext() instanceof DashBoardActivity) {
                    com.ojassoft.astrosage.utils.CUtils.addFcmAnalytics(astroImageIdRes);
                    AstrosageKundliApplication.currentEventType = CGlobalVariables.CHAT_BTN_CLICKED;
                    CUtils.fcmAnalyticsEvents("pop_under_free_ai_chat_now", AstrosageKundliApplication.currentEventType, "");
                    CUtils.initiateRandomAiChat(getActivity(),CGlobalVariables.POP_UNDER_AI_POP_UP,com.ojassoft.astrosage.utils.CGlobalVariables.TYPE_FREE_CHAT_AI);
                }
                dismiss();
            }
        });
        CUtils.setPrefNextOffer(getActivity(), CUtils.getCurrentDateInString());
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        CUtils.fcmAnalyticsEvents("pop_under_free_ai_chat_cancelled", CGlobalVariables.EVENT_TYPE_CANCEL_DIALOG, "");
    }

}