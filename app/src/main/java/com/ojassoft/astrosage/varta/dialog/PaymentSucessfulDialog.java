package com.ojassoft.astrosage.varta.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.ui.activity.AstrologerDescriptionActivity;
import com.ojassoft.astrosage.varta.ui.activity.DashBoardActivity;
import com.ojassoft.astrosage.varta.ui.activity.PaymentInformationActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;

public class PaymentSucessfulDialog extends DialogFragment implements View.OnClickListener {
    Activity activity;
    TextView oopsTV, rechargeUnsuccessfulTV, descTV, amountTV;
    Button doneBtn;
    String rechargeMount;

    public PaymentSucessfulDialog(){

    }

    public PaymentSucessfulDialog(String rechargeMount) {
        this.rechargeMount = rechargeMount;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LANGUAGE_CODE = AstrosageKundliApplication.getAppContext().getLanguageCode();
        CUtils.getRobotoFont(getActivity(), LANGUAGE_CODE, CGlobalVariables.regular);

        View view = inflater.inflate(R.layout.payment_success_layout, container);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        oopsTV = view.findViewById(R.id.heading);
        rechargeUnsuccessfulTV = view.findViewById(R.id.recharge_unsucessful);
        descTV = view.findViewById(R.id.recharge_unsucessful_content);
        doneBtn = view.findViewById(R.id.apply_code_btn);
        amountTV = view.findViewById(R.id.amount_tv);

        amountTV.setText(getResources().getString(R.string.rs_sign) + " " + rechargeMount);
        FontUtils.changeFont(getActivity(), oopsTV, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(getActivity(), rechargeUnsuccessfulTV, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(getActivity(), descTV, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(getActivity(), doneBtn, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(getActivity(), amountTV, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);

        doneBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        CUtils.fcmAnalyticsEvents("payment_successful_dialog_cancelled", CGlobalVariables.EVENT_TYPE_CANCEL_DIALOG, "");
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.apply_code_btn:
                dismiss();
                if (getActivity() instanceof PaymentInformationActivity) {
                    ((PaymentInformationActivity) getActivity()).closeActivity();
                }
              /*  Intent intent = new Intent(getActivity(), DashBoardActivity.class);
                startActivity(intent);*/
                break;

        }
    }

    /*@Override
    public void dismiss() {
        super.dismiss();
        try {
            Activity activity = getActivity();
            if (activity instanceof DashBoardActivity) {
                ((DashBoardActivity) getActivity()).nextRechargeOffer();
            }else if (activity instanceof AstrologerDescriptionActivity) {
                ((AstrologerDescriptionActivity) getActivity()).nextRechargeOffer();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/
}

