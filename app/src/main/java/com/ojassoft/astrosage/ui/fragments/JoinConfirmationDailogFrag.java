package com.ojassoft.astrosage.ui.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.VartaReqJoinActivity;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

/**
 * Created by ojas on 22/12/16.
 */

public class JoinConfirmationDailogFrag extends AstroParentFrag implements View.OnClickListener {

    TextView dialogHeading, dialogDescription;
    ImageView closeDialogButton;
    Button btnHome;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.join_confirmation_dialog, container);
        final Dialog dialog = getDialog();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(R.color.bg_transparent);
        initLayout(view);
        initListner();
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void initLayout(View view) {
        closeDialogButton = view.findViewById(R.id.imgDialogClose);
        dialogHeading = view.findViewById(R.id.textDialogTitle);
        dialogDescription = view.findViewById(R.id.tvDesc);
        btnHome = view.findViewById(R.id.btnHome);

       /* FontUtils.changeFont(getActivity(), dialogHeading, CGlobalVariables.FONT_RALEWAY_SEMI_BOLD);
        FontUtils.changeFont(getActivity(), dialogDescription, CGlobalVariables.FONT_RALEWAY_REGULAR);
        FontUtils.changeFont(getActivity(), btnHome, CGlobalVariables.FONT_RALEWAY_BOLD);*/

    }

    private void initListner() {
        closeDialogButton.setOnClickListener(this);
        btnHome.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == closeDialogButton) {
            //addGoogleAnalytics(CGlobalVariables.GOOGLE_ANALYTIC_PAYMENT_FAILED_POPUP_CLOSE);
            JoinConfirmationDailogFrag.this.dismiss();
        } else if (v == btnHome) {
            JoinConfirmationDailogFrag.this.dismiss();
            ((VartaReqJoinActivity) getActivity()).goToHome();
        }
    }

    private void addGoogleAnalytics(String googleAnalyticPaymentFailedKey) {
        CUtils.googleAnalyticSendWitPlayServie(getActivity(),
                CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                googleAnalyticPaymentFailedKey, null);
        CUtils.fcmAnalyticsEvents(googleAnalyticPaymentFailedKey, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
    }
}
