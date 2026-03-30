package com.ojassoft.astrosage.ui.fragments;

import static com.ojassoft.astrosage.ui.act.ActAppModule.LANGUAGE_CODE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KUNDLI_AI_OPEN_FIRST_TIME_KEY;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.utils.CUtils;

public class KundliAIFirstTimeMsgFragment extends DialogFragment {
    KundliAIHandlingCallBack kundliAIHandlingCallBack;
    public KundliAIFirstTimeMsgFragment(){

    }
    public KundliAIFirstTimeMsgFragment(KundliAIHandlingCallBack kundliAIHandlingCallBack) {
        this.kundliAIHandlingCallBack = kundliAIHandlingCallBack;
    }

    @NonNull
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.kundli_ai_welcome_msg_layout, container);
        inItUI(view);
        try {
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
            getDialog().setCanceledOnTouchOutside(false);
            getDialog().getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        } catch (Exception e) {
            //
        }
        return view;
    }

    private void inItUI(View view) {
        ImageView imgHeading = view.findViewById(R.id.imgHeading);
        TextView tvHeading = view.findViewById(R.id.tv_heading);
        TextView exploreBtn = view.findViewById(R.id.explore_tv_btn);
        ImageView ivClosePage = view.findViewById(R.id.ivClosePage);
        if (LANGUAGE_CODE != 0) {
            imgHeading.setVisibility(View.GONE);
            tvHeading.setVisibility(View.VISIBLE);
        } else {
            imgHeading.setVisibility(View.VISIBLE);
            tvHeading.setVisibility(View.GONE);
        }
        exploreBtn.setOnClickListener(v -> {
            if (kundliAIHandlingCallBack != null) {
                dismiss();
                kundliAIHandlingCallBack.onExploreClicked();
            }
        });
        ivClosePage.setOnClickListener(v -> dismiss());
        //tvHeading.setText(CUtils.getKundliAIPlusSpannableText(getContext(),getString(R.string.platinum_plan)));

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        try {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        } catch (Exception e) {
            //
        }
        super.onViewCreated(view, savedInstanceState);
    }

    public interface KundliAIHandlingCallBack {
        void onExploreClicked();
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        CUtils.saveBooleanData(getContext(), KUNDLI_AI_OPEN_FIRST_TIME_KEY, false);
    }
}
