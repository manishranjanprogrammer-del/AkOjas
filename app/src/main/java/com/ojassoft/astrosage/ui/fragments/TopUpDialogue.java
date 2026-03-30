package com.ojassoft.astrosage.ui.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.act.TopupRechargeActivity;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

public class TopUpDialogue extends DialogFragment implements OnClickListener {

    TextView tvthankstext;
    int LANGUAGE_CODE = 0;
    Dialog dialog;
    RelativeLayout rlCrossBtn;
    private Button btnBuyNow, btnProceed;

    public static TopUpDialogue newInstance() {
        TopUpDialogue fragment = new TopUpDialogue();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LANGUAGE_CODE = ((AstrosageKundliApplication) getActivity().getApplication()).getLanguageCode();
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.transparentdialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dialog = getDialog();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        View view = inflater.inflate(R.layout.dialogue_topup, container);
        btnBuyNow = view.findViewById(R.id.btnBuyNow);
        btnBuyNow.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);
        btnProceed = view.findViewById(R.id.btnProceed);
        btnProceed.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);
        TextView headingText = view.findViewById(R.id.tv_upgrade_heading);
        rlCrossBtn = view.findViewById(R.id.cross_btn_layout);
        headingText.setTypeface(((BaseInputActivity) getActivity()).regularTypeface);
        tvthankstext = view.findViewById(R.id.tvthankstext);
        tvthankstext.setTypeface(((BaseInputActivity) getActivity()).regularTypeface);
        rlCrossBtn.setOnClickListener(TopUpDialogue.this);
        btnBuyNow.setOnClickListener(TopUpDialogue.this);
        btnProceed.setOnClickListener(TopUpDialogue.this);
        return view;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btnBuyNow:
                CUtils.fcmAnalyticsEvents(CGlobalVariables.OPEN_TOPUP_RECHARGE_AFTER_LIMIT_EXCEED, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                Intent intent = new Intent(getActivity(), TopupRechargeActivity.class);
                startActivity(intent);
                dialog.dismiss();
                break;
            case R.id.btnProceed:
                CUtils.fcmAnalyticsEvents(CGlobalVariables.CANCEL_TOPUP_RECHARGE_AFTER_LIMIT_EXCEED, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                dialog.dismiss();
                break;
            case R.id.cross_btn_layout:
                dialog.dismiss();
                break;
            default:
                break;
        }

    }

    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        Dialog dialog = getDialog();
        if (dialog != null) {
            WindowManager.LayoutParams wmlp = dialog.getWindow()
                    .getAttributes();
            wmlp.width = width - 40;
            wmlp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(wmlp);
        }
    }

}
