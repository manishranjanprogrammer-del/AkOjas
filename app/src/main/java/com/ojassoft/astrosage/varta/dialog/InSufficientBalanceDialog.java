package com.ojassoft.astrosage.varta.dialog;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.ActAppModule;
import com.ojassoft.astrosage.varta.ui.activity.AIChatWindowActivity;
import com.ojassoft.astrosage.varta.ui.activity.AIVoiceCallingActivity;
import com.ojassoft.astrosage.varta.ui.activity.AstrologerDescriptionActivity;
import com.ojassoft.astrosage.varta.ui.activity.BaseActivity;
import com.ojassoft.astrosage.varta.ui.activity.ChatWindowActivity;
import com.ojassoft.astrosage.varta.ui.activity.DashBoardActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.vartalive.activities.LiveActivityNew;

import static com.ojassoft.astrosage.utils.CUtils.createSession;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.PID_INSUFFICIENT_BALANCE_DIALOG_RECHARGE_BUTTON;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.PID_TOPUP_RECHARGE_ADD_MONEY;


public class InSufficientBalanceDialog extends DialogFragment implements View.OnClickListener {

    Activity activity;
    TextView headingTxt, availableBalTxt,
            balanceTxt, balanceMsgTxt,
            cancelButton, rechargeButton;
    LinearLayout mainlayout;
    String astrologerName, userbalance, minBalance;
    String fromWhich="";


   /* public InSufficientBalanceDialog(String astrologerName, String userbalance, String minBalance) {
        this.astrologerName = astrologerName;
        this.userbalance = userbalance;
        this.minBalance = minBalance;
    }
*/
    public InSufficientBalanceDialog(String astrologerName, String userbalance, String minBalance, String fromWhich) {
        this.astrologerName = astrologerName;
        this.userbalance = userbalance;
        this.minBalance = minBalance;
        this.fromWhich = fromWhich;
    }

    public InSufficientBalanceDialog(){

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LANGUAGE_CODE = AstrosageKundliApplication.getAppContext().getLanguageCode();
        CUtils.getRobotoFont(getActivity(), LANGUAGE_CODE, CGlobalVariables.regular);

        View view = inflater.inflate(R.layout.insufficient_bal_layout, container);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        headingTxt = view.findViewById(R.id.heading_txt);
        availableBalTxt = view.findViewById(R.id.available_bal_txt);
        balanceTxt = view.findViewById(R.id.balance_txt);
        balanceMsgTxt = view.findViewById(R.id.balance_msg_txt);
        cancelButton = view.findViewById(R.id.cancel_button);
        rechargeButton = view.findViewById(R.id.recharge_button);
        mainlayout = view.findViewById(R.id.mainlayout);

        FontUtils.changeFont(getActivity(), headingTxt, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(getActivity(), balanceMsgTxt, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(getActivity(), balanceTxt, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(getActivity(), availableBalTxt, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(getActivity(), cancelButton, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(getActivity(), rechargeButton, CGlobalVariables.FONTS_OPEN_SANS_BOLD);

        balanceTxt.setText(activity.getResources().getString(R.string.astroshop_rupees_sign) + " " + userbalance);
        String msges = getResources().getString(R.string.max_wallet_msg);
        if(astrologerName == null){
            astrologerName = "";
        }
        if(minBalance == null){
            minBalance = "";
        }
        if(astrologerName.contains("+")){
            astrologerName = astrologerName.replace("+"," ");
            msges = msges.replace("#", astrologerName);
        }else{
            msges = msges.replace("#", astrologerName);
        }
        msges = msges.replace("$", minBalance);

        if(TextUtils.isEmpty(minBalance)){
            msges = getResources().getString(R.string.insuffi_wallet_msg);
        }

        balanceMsgTxt.setText(msges);

        cancelButton.setOnClickListener(this);
        rechargeButton.setOnClickListener(this);
        com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents(com.ojassoft.astrosage.varta.utils.CGlobalVariables.FBA_DIALOG_INUFFICIENT_OPEN, CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        CUtils.fcmAnalyticsEvents("insufficient_balance_dialog_cancelled", CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        //Log.e("LoadMore ", "Dialog onDismiss");
        try {
            if(fromWhich.equals(CGlobalVariables.CALL_CLICK)) {
                if (activity instanceof DashBoardActivity) {
                    ((DashBoardActivity) activity).RefreshHomeFragment(fromWhich);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.cancel_button:
                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_INSUFFICIENT_BALANCE_DIALOG_CANCEL_BUTTON,
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                AstrosageKundliApplication.chatAstrologerDetailBeanAfterRecharge = null;
                try{
                    if (activity instanceof ChatWindowActivity) {
                       ((ChatWindowActivity) activity).cancelRechargeAfterChat();
                    } else if (activity instanceof AIChatWindowActivity) {
                        ((AIChatWindowActivity) activity).cancelRechargeAfterChat();
                    } else if (activity instanceof AIVoiceCallingActivity) {
                        ((AIVoiceCallingActivity) activity).cancelRechargeAfterChat();
                    }
                } catch (Exception e){
                    //
                }
                dismiss();
                break;

            case R.id.recharge_button:
                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_DIALOG_RECHARGE_BTN,
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                createSession(activity,PID_INSUFFICIENT_BALANCE_DIALOG_RECHARGE_BUTTON);
                try {
                    activity = requireActivity();
                    if (activity != null) {
                        com.ojassoft.astrosage.utils.CUtils.createSession(activity, CGlobalVariables.PID_INSUFFICIANT_BALANCE_CLICK);
                        if (activity instanceof AstrologerDescriptionActivity) {
                            ((AstrologerDescriptionActivity) activity).openWalletScreen(CGlobalVariables.SCREEN_OPEN_FROM_INSUFFICIENT);
                        } else if (activity instanceof DashBoardActivity) {
                            ((DashBoardActivity) activity).openWalletScreen(CGlobalVariables.SCREEN_OPEN_FROM_INSUFFICIENT);
                        } else if (activity instanceof ActAppModule) {
                            ((ActAppModule) activity).openWalletScreen(CGlobalVariables.SCREEN_OPEN_FROM_INSUFFICIENT);
                        } else if (activity instanceof ChatWindowActivity) {
                            ((ChatWindowActivity) activity).openWalletScreen(CGlobalVariables.SCREEN_OPEN_FROM_INSUFFICIENT);
                        } else if (activity instanceof AIChatWindowActivity) {
                            ((AIChatWindowActivity) activity).openWalletScreen(CGlobalVariables.SCREEN_OPEN_FROM_INSUFFICIENT);
                        } else if (activity instanceof AIVoiceCallingActivity) {
                            ((AIVoiceCallingActivity) activity).openWalletScreen(CGlobalVariables.SCREEN_OPEN_FROM_INSUFFICIENT);
                        } else if (activity instanceof LiveActivityNew) {
                            ((LiveActivityNew) activity).openWalletScreen(CGlobalVariables.SCREEN_OPEN_FROM_INSUFFICIENT);
                        } else {
                            ((BaseActivity) activity).openWalletScreenDashboard(CGlobalVariables.SCREEN_OPEN_FROM_INSUFFICIENT);
                        }
                    }
                } catch (Exception e) {
                    try {
                        CUtils.showSnackbar(requireActivity().findViewById(android.R.id.content), e.getMessage(), requireContext());
                    } catch (Exception e2) {
                        //
                    }
                }
                dismiss();

                break;
        }
    }
}
