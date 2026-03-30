package com.ojassoft.astrosage.ui.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.jinterface.IPaymentFailed;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

/**
 * Created by ojas on 22/12/16.
 */

public class PaymentFailedDailogFrag extends AstroSageParentDialogFragment implements View.OnClickListener {
    TextView dialogHeading, dialogDescription;
    Button callUs, retryPayment;
    IPaymentFailed paymentFailed;
    ImageView closeDialogButton;
    int LANGUAGE_CODE = 0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.payment_failed_dialog, container);
        initLayout(view);
        LANGUAGE_CODE = CUtils.getLanguageCodeFromPreference(getActivity());
        final Dialog dialog = getDialog();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        paymentFailed = (IPaymentFailed) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        paymentFailed = null;
    }

    private void initLayout(View view) {
        dialogHeading = (TextView) view.findViewById(R.id.textDialogTitle);
        dialogDescription = (TextView) view.findViewById(R.id.textDescription);
        callUs = (Button) view.findViewById(R.id.butCallUs);
        retryPayment = (Button) view.findViewById(R.id.butRetry);
        closeDialogButton = (ImageView) view.findViewById(R.id.imgDialogClose);
        callUs.setOnClickListener(this);
        retryPayment.setOnClickListener(this);
        closeDialogButton.setOnClickListener(this);

        String str=getString(R.string.payment_failure_contact_details);
        String ph1="+91-9560267006";

        String ph1FromContainer=CUtils.getStringData((Context) paymentFailed,CGlobalVariables.key_Phone_One,ph1);
        if(ph1FromContainer!=null && str.contains(ph1) && !ph1FromContainer.isEmpty())
        {
            str=str.replace(ph1,ph1FromContainer);
            //Log.e("Replacing","item");
        }

        dialogDescription.setText(str);


        if (LANGUAGE_CODE == CGlobalVariables.ENGLISH) {
            retryPayment.setText(getResources().getString(R.string.astroshop_try_again).toUpperCase());
        }

        Linkify.addLinks(dialogDescription, Linkify.ALL);
    }

    @Override
    public void onClick(View v) {
        if (v == callUs) {
            addGoogleAnalytics(CGlobalVariables.GOOGLE_ANALYTIC_PAYMENT_FAILED_POPUP_CALL);
            callCustomerCare();
        }
        if (v == retryPayment) {
            addGoogleAnalytics(CGlobalVariables.GOOGLE_ANALYTIC_PAYMENT_FAILED_POPUP_RETRY);
            paymentFailed.onRetryClick();
            PaymentFailedDailogFrag.this.dismiss();
        }
        if (v == closeDialogButton) {
            addGoogleAnalytics(CGlobalVariables.GOOGLE_ANALYTIC_PAYMENT_FAILED_POPUP_CLOSE);
            PaymentFailedDailogFrag.this.dismiss();
        }
    }

    private void addGoogleAnalytics(String googleAnalyticPaymentFailedKey) {
        CUtils.googleAnalyticSendWitPlayServie(getActivity(),
                CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                googleAnalyticPaymentFailedKey, null);
        CUtils.fcmAnalyticsEvents(googleAnalyticPaymentFailedKey, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
    }

    private void callCustomerCare() {
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:" + CGlobalVariables.helpNumberFirst));
        startActivity(intent);
    }


}
