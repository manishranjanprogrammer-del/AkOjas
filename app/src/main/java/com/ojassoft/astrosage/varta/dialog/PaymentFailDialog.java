package com.ojassoft.astrosage.varta.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
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
import com.ojassoft.astrosage.varta.ui.activity.DashBoardActivity;
import com.ojassoft.astrosage.varta.ui.activity.PaymentInformationActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;

public class PaymentFailDialog extends DialogFragment implements View.OnClickListener {

    Activity activity;
    TextView oopsTV, rechargeUnsuccessfulTV, descTV;
    Button doneBtn;
    TryAgainCallback againCallback;
    public PaymentFailDialog(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LANGUAGE_CODE =AstrosageKundliApplication.getAppContext().getLanguageCode();
        CUtils.getRobotoFont(getActivity(), LANGUAGE_CODE, CGlobalVariables.regular);

        View view = inflater.inflate(R.layout.payment_fail_layout, container);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        oopsTV = view.findViewById(R.id.heading);
        rechargeUnsuccessfulTV = view.findViewById(R.id.recharge_unsucessful);
        descTV = view.findViewById(R.id.recharge_unsucessful_content);
        doneBtn = view.findViewById(R.id.apply_code_btn);


        FontUtils.changeFont(getActivity(), oopsTV, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(getActivity(), rechargeUnsuccessfulTV, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(getActivity(), descTV, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(getActivity(), doneBtn, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);

        doneBtn.setOnClickListener(this);

        if(!TextUtils.isEmpty(PaymentInformationActivity.errorMsg)){
            descTV.setText(PaymentInformationActivity.errorMsg);
            PaymentInformationActivity.errorMsg = "";
        }

        return view;
    }

    @Override
    public void onCancel(@NonNull DialogInterface dialog) {
        super.onCancel(dialog);
        CUtils.fcmAnalyticsEvents("payment_failed_dialog_cancelled", CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int screenWidth = displayMetrics.widthPixels;
            int dialogWidth = (int) (screenWidth * 0.9); // 90% of screen width
            dialog.getWindow().setLayout(dialogWidth, ViewGroup.LayoutParams.WRAP_CONTENT);
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
                if(againCallback != null) againCallback.tryAgain();
                break;

        }
    }

    public void setTryAgainCallback(TryAgainCallback callback) {
        againCallback = callback;
    }

    @FunctionalInterface
    public interface TryAgainCallback {
        void tryAgain();
    }
}

