package com.ojassoft.astrosage.varta.ui.fragments;

import static com.ojassoft.astrosage.utils.CUtils.createSession;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.ui.activity.WalletActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.FontUtils;

public class OfferRechargeDialog extends DialogFragment {
    private TextView tvMessageFRPAFC, tvBtnFRPAFC, tvTitleFRPAFC;
    public static OfferRechargeDialog mInstance;
    private int rateRs;
    private int offerAmount;
    public OfferRechargeDialog() {
        // Required empty public constructor
    }

    public OfferRechargeDialog(int rateRs, int offerAmount) {
        this.rateRs = rateRs;
        this.offerAmount = offerAmount;
    }

    public static OfferRechargeDialog newInstance(String param1, String param2) {
        if (mInstance == null) mInstance = new OfferRechargeDialog();
        return mInstance;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.offer_recharge_dialog_layout, container, false);
        try {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            tvMessageFRPAFC = view.findViewById(R.id.tvMessageFRPAFC);
            tvBtnFRPAFC = view.findViewById(R.id.tvBtnFRPAFC);
            FrameLayout ivCloseFRPAFC = view.findViewById(R.id.ivCloseFRPAFC);
            tvTitleFRPAFC = view.findViewById(R.id.tvTitleFRPAFC);


            FontUtils.changeFont(getActivity(), tvBtnFRPAFC, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
            FontUtils.changeFont(getActivity(), tvMessageFRPAFC, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
            FontUtils.changeFont(getActivity(), tvTitleFRPAFC, CGlobalVariables.FONTS_OPEN_SANS_BOLD);

            try {
                String offerText = getActivity().getResources().getString(R.string.get_100_percent_cashback_text);
                String rateAmount = "<font color='#ff6f00'> \u20B9 "+rateRs+" </font>";
                String rateAmountFinal = "<font color='#ff6f00'> \u20B9 "+(rateRs + offerAmount)+" </font>";

                offerText = offerText.replace("#", "" + rateAmount);
                offerText = offerText.replace("*", "" +rateAmountFinal);

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    tvMessageFRPAFC.setText(Html.fromHtml(offerText, Html.FROM_HTML_MODE_COMPACT));
                } else {
                    tvMessageFRPAFC.setText(Html.fromHtml(offerText));
                }
            }catch (Exception e){
                //
            }

            tvBtnFRPAFC.setOnClickListener(v -> {
                try {
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.RECHARGE_AND_GET_OFFER_BTN_CLICKED, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                    if (getActivity() != null) {
                        createSession(getActivity(), CGlobalVariables.PID_100_PERCENT_CASHBACK_DIALOG);
                    }
                    openWalletScreen();
                    dismiss();
                } catch (Exception e) {
                    //
                }
            });

            ivCloseFRPAFC.setOnClickListener(v -> dismiss());
        }catch (Exception e){
            //
        }
        return view;
    }

   

    public void openWalletScreen() {
        Activity activity = getActivity();
        if (activity == null) return;
        try {
            Intent intent = new Intent(activity, WalletActivity.class);
            intent.putExtra(CGlobalVariables.CALLING_ACTIVITY, "DashBoardActivity");
            activity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart()
    {
        super.onStart();
        try {
            Dialog dialog = getDialog();
            if (dialog != null) {
                int width = ViewGroup.LayoutParams.MATCH_PARENT;
                int height = ViewGroup.LayoutParams.MATCH_PARENT;
                dialog.getWindow().setLayout(width, height);
            }
        }catch (Exception e){
            //
        }
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        CUtils.fcmAnalyticsEvents("100_percent_cashback_dialog_cancelled", CGlobalVariables.EVENT_TYPE_CANCEL_DIALOG, "");
    }
}
