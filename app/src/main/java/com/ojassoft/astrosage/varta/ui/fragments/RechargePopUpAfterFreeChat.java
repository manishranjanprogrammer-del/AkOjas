package com.ojassoft.astrosage.varta.ui.fragments;

import static com.ojassoft.astrosage.utils.CUtils.createSession;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.varta.dialog.QuickRechargeBottomSheet;
import com.ojassoft.astrosage.varta.ui.activity.AIChatWindowActivity;
import com.ojassoft.astrosage.varta.ui.activity.AIVoiceCallingActivity;
import com.ojassoft.astrosage.varta.ui.activity.ChatWindowActivity;
import com.ojassoft.astrosage.varta.ui.activity.WalletActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.CustomProgressDialog;
import com.ojassoft.astrosage.varta.utils.FontUtils;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link RechargePopUpAfterFreeChat#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RechargePopUpAfterFreeChat extends DialogFragment {

    public static RechargePopUpAfterFreeChat mInstance;
    private TextView tvMessageFRPAFC, tvBtnFRPAFC, tvSubheadingFRPAFC, tvTitleFRPAFC;
    private ImageView ivCloseFRPAFC;
    private String rechargeServiceId, amountResponse = "", callingActivity = "";

    public RechargePopUpAfterFreeChat(String callingActivity) {
        this.callingActivity = callingActivity;
        // Required empty public constructor
    }

    public static RechargePopUpAfterFreeChat newInstance() {
        if (mInstance == null) mInstance = new RechargePopUpAfterFreeChat("");
        return mInstance;
    }

    public  RechargePopUpAfterFreeChat(){

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recharge_pop_up_after_free_chat, container, false);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        tvMessageFRPAFC = view.findViewById(R.id.tvMessageFRPAFC);
        tvBtnFRPAFC = view.findViewById(R.id.tvBtnFRPAFC);
        ivCloseFRPAFC = view.findViewById(R.id.ivCloseFRPAFC);
        tvTitleFRPAFC = view.findViewById(R.id.tvTitleFRPAFC);
        tvSubheadingFRPAFC = view.findViewById(R.id.tvSubheadingFRPAFC);
        if (callingActivity.equals(CGlobalVariables.AIVOICECALLINGACTIVITY)) {
            tvTitleFRPAFC.setText(getActivity().getResources().getString(R.string.enjoyed_free_call));
        }
        FontUtils.changeFont(getActivity(), tvBtnFRPAFC, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(getActivity(), tvMessageFRPAFC, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(getActivity(), tvTitleFRPAFC, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(getActivity(), tvSubheadingFRPAFC, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);

        this.getDialog().setOnKeyListener((dialog, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                actionOnClose();
                return true;
            }
            return false;
        });

        tvBtnFRPAFC.setOnClickListener(v -> {
            CUtils.fcmAnalyticsEvents(CGlobalVariables.RECHARGE_CLICK_AFTER_FREE_CHAT,
                    CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

            if (getActivity() != null) {
                createSession(getActivity(), CGlobalVariables.PID_RECHARGE_AFTER_FREE_CHAT);
            }
            //openQuickRechargeSheet();

            openWalletScreen();
           // dismiss();
        });

        ivCloseFRPAFC.setOnClickListener(v -> {
            actionOnClose();
        });

        //setData();

        return view;
    }


    private void actionOnClose() {
        try {
            if (getActivity() instanceof ChatWindowActivity) {
                ((ChatWindowActivity) getActivity()).getAstrologerFeedbackStatus();
            } else if (getActivity() instanceof AIChatWindowActivity) {
                ((AIChatWindowActivity) getActivity()).getAstrologerFeedbackStatus();
            }
            dismiss();
        } catch (Exception e) {
            //
        }
    }

    private void setData() {
        Bundle bndl = getArguments();
        if (bndl != null) {
            tvMessageFRPAFC.setText(bndl.getString("message").replace("Rs.", getString(R.string.astroshop_rupees_sign)));
            rechargeServiceId = bndl.getString("serviceId");
        } else {
            dismiss();
        }
    }

    private void openQuickRechargeSheet() {
        Activity activity = getActivity();
        if (activity == null) return;
        try {
            QuickRechargeBottomSheet quickRechargeBottomSheet = QuickRechargeBottomSheet.getInstance();
            Bundle bundle = new Bundle();
            bundle.putString(com.ojassoft.astrosage.utils.CGlobalVariables.BOTTOMSERVICELISTUSEDFOR, com.ojassoft.astrosage.utils.CGlobalVariables.RECHARGE_AFTER_FREE_CHAT);
            bundle.putString(CGlobalVariables.CALLING_ACTIVITY, this.callingActivity);
            bundle.putString("minBalanceNeededText","");
            quickRechargeBottomSheet.setArguments(bundle);
            quickRechargeBottomSheet.show(getChildFragmentManager(), QuickRechargeBottomSheet.TITLE);
        } catch (Exception e) {
            //
        }
    }

    public void openWalletScreen() {
        Activity activity = getActivity();
        if (activity == null) return;
        try {
            Intent intent = new Intent(activity, WalletActivity.class);
            if (activity instanceof ChatWindowActivity) {
                intent.putExtra(CGlobalVariables.CALLING_ACTIVITY, "ChatWindowActivity");
            } else if (activity instanceof AIChatWindowActivity) {
                intent.putExtra(CGlobalVariables.CALLING_ACTIVITY, "AIChatWindowActivity");
            }else if (activity instanceof AIVoiceCallingActivity) {
                intent.putExtra(CGlobalVariables.CALLING_ACTIVITY, "AIVoiceCallWindowActivity");
            }
            activity.startActivity(intent);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }


    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        CUtils.fcmAnalyticsEvents("recharge_after_free_chat_dialog_cancelled", CGlobalVariables.EVENT_TYPE_CANCEL_DIALOG, "");
    }

}