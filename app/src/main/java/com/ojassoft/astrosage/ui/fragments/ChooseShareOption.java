package com.ojassoft.astrosage.ui.fragments;

import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTIC_DOWNLOAD_PDF;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.act.OutputMasterActivity;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;


/**
 * Created by ojas on ८/२/१८.
 */

public class ChooseShareOption extends AstroSageParentDialogFragment {


    Activity activity;
    Typeface regularTypeface;
    Typeface mediumTypeface;
    int LANGUAGE_CODE;
    Button buttonPdf, buttonUrl;
    Button okButton, cancelButton;
    TextView headingTextView;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        this.activity = activity;
        regularTypeface = ((BaseInputActivity) activity).regularTypeface;
        mediumTypeface = ((BaseInputActivity) activity).mediumTypeface;
        LANGUAGE_CODE = ((BaseInputActivity) activity).LANGUAGE_CODE;

    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //expiryDate = getArguments().getString(ExpiryDateKey);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final Dialog dialog = getDialog();
        dialog.setCancelable(true);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.choose_share_option_layout, container);
        headingTextView = (TextView) view.findViewById(R.id.textViewHeading);
        buttonPdf = view.findViewById(R.id.buttonPdf);
        buttonUrl = view.findViewById(R.id.buttonUrl);
        okButton = (Button) view.findViewById(R.id.ok_btn);
        cancelButton = (Button) view.findViewById(R.id.cancel_btn);

        buttonPdf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CUtils.googleAnalyticSendWitPlayServie(getActivity(),
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_KUNDLI_SHARE_PDF, null);
                //CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_KUNDLI_SHARE_PDF,CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK,"");
                com.ojassoft.astrosage.varta.utils.CUtils.addFacebookAndFirebaseEvent(CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK,GOOGLE_ANALYTIC_DOWNLOAD_PDF,CGlobalVariables.GOOGLE_ANALYTIC_KUNDLI_SHARE_PDF);
                ((OutputMasterActivity) activity).sharePDF(true);
                dialog.dismiss();
            }
        });
        buttonUrl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CUtils.googleAnalyticSendWitPlayServie(getActivity(),
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_KUNDLI_SHARE_URL, null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_KUNDLI_SHARE_URL,CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK,"");

                ((OutputMasterActivity) activity).shareChart();
                dialog.dismiss();
            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        setTypefaceOfView();

        return view;

    }

    private void setTypefaceOfView() {
        okButton.setTypeface(mediumTypeface);
        cancelButton.setTypeface(mediumTypeface);
        headingTextView.setTypeface(mediumTypeface);
    }

    public static ChooseShareOption getInstance(String expiryDate) {
        Bundle bundle = new Bundle();
        ChooseShareOption chooseHinduMonth = new ChooseShareOption();
        chooseHinduMonth.setArguments(bundle);
        return chooseHinduMonth;
    }
}
