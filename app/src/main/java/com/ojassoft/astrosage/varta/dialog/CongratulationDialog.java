package com.ojassoft.astrosage.varta.dialog;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.ui.activity.WalletActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;

public class CongratulationDialog {
    Context context;
    TextView youHaveGotMsgTxt, amountTxt, forFirstConsultationTxt, msgTxt;
    LinearLayout bottomLayout;
    Button consultNowButton;
    String amount = "";
    Dialog dialog = null;
    boolean isForHundreadRs;
    ImageView img;

    public CongratulationDialog(Context context, String amount, boolean isForHundreadRs) {
        this.context = context;
        this.amount = amount;
        this.isForHundreadRs = isForHundreadRs;
    }

    public Dialog congratsDialog() {
        LANGUAGE_CODE = AstrosageKundliApplication.getAppContext().getLanguageCode();
        CUtils.getRobotoFont(context, LANGUAGE_CODE, CGlobalVariables.regular);

        dialog = new Dialog(context);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.congratulation_layout);

        img = dialog.findViewById(R.id.img);
        youHaveGotMsgTxt = dialog.findViewById(R.id.you_have_got_msg_txt);
        amountTxt = dialog.findViewById(R.id.amount_txt);
        forFirstConsultationTxt = dialog.findViewById(R.id.for_first_consultation_txt);
        msgTxt = dialog.findViewById(R.id.msg_txt);
        consultNowButton = dialog.findViewById(R.id.consult_now_button);
        bottomLayout = dialog.findViewById(R.id.bottom_layout);

        FontUtils.changeFont(context, msgTxt, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(context, forFirstConsultationTxt, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(context, youHaveGotMsgTxt, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(context, amountTxt, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(context, consultNowButton, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);

        if (isForHundreadRs) {
            youHaveGotMsgTxt.setText(context.getResources().getString(R.string.you_have_got));
            forFirstConsultationTxt.setText(context.getResources().getString(R.string.for_first_consultation));
            img.setImageResource(R.drawable.gift);
            consultNowButton.setBackground(context.getResources().getDrawable(R.drawable.consultbutton));
            bottomLayout.setBackgroundResource(R.drawable.congrats_bg);
        } else {
            youHaveGotMsgTxt.setText(context.getResources().getString(R.string.get_bonus_of));
            forFirstConsultationTxt.setText(context.getResources().getString(R.string.recharge_with_just));
            img.setImageResource(R.drawable.rs_one);
            consultNowButton.setBackgroundResource(R.drawable.rechargebutton);
            bottomLayout.setBackgroundResource(R.drawable.unlocked);
        }

        if (amount != null && amount.length() > 0) {
            String amountStr = context.getResources().getString(R.string.rs_signn);
            amountStr = amountStr.replace("#", amount);
            amountTxt.setText(amountStr);

            String msg = context.getResources().getString(R.string.premium_astrosage_msg);
            msg = msg.replace("#", amount);
            msgTxt.setText(msg);
        }

        consultNowButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isForHundreadRs) {
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_HUNDRED_RS_BONUS_OK_CLICK,
                            CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                    if (context != null) {
                        com.ojassoft.astrosage.utils.CUtils.createSession(context, CGlobalVariables.PID_HUNDRED_RS_BONUS_OK_CLICK);
                    }
                    dialog.dismiss();
                } else {
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_ONE_RS_RECHARGENOW_CLICK,
                            CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                    if (context != null) {
                        com.ojassoft.astrosage.utils.CUtils.createSession(context, CGlobalVariables.PID_ONE_RS_RECHARGENOW_CLICK);
                    }
                    openWalletScreen();
                    dialog.dismiss();
                }
            }
        });

        return dialog;
    }

    public void openWalletScreen() {
        if (context == null) return;
        Intent intent = new Intent(context, WalletActivity.class);
        intent.putExtra(CGlobalVariables.CALLING_ACTIVITY, "DashBoardActivity");
        intent.putExtra(CGlobalVariables.FROM_ONE_RS_DIALOG, "1");
        context.startActivity(intent);
    }

}
