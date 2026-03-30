package com.ojassoft.astrosage.ui.fragments;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;

import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.PurchasePlanHomeActivity;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

/**
 * Created by ojas-10 on 11/7/16.
 */
public class ChoosePlanFragmentDailogSilver extends DialogFragment {
    RadioButton yearlyPlan, monthlyPlan;
    int radioBtnId[] = {0,1};
    private int oldLanguageIndex = 0, checkedRadioButtonId = 0;
    public Typeface mediumTypeface;
    public int LANGUAGE_CODE = CGlobalVariables.ENGLISH;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LANGUAGE_CODE = ((AstrosageKundliApplication) getActivity().getApplication()).getLanguageCode();
        mediumTypeface = CUtils.getRobotoFont(
                getActivity(), LANGUAGE_CODE, CGlobalVariables.medium);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_plan_dialog, container);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        yearlyPlan = (RadioButton) view.findViewById(R.id.radioyearly);
        yearlyPlan.setText(((PurchasePlanHomeActivity)getActivity()).silverPlanPriceYear);
        monthlyPlan = (RadioButton) view.findViewById(R.id.radiomonthly);
        monthlyPlan.setText(((PurchasePlanHomeActivity)getActivity()).silverPlanPriceMonth);
        monthlyPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkedRadioButtonId = radioBtnId[0];
            }
        });
        yearlyPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkedRadioButtonId = radioBtnId[1];
            }
        });
        TextView mCustomerCareText = (TextView) view.findViewById(R.id.customer_care);
        mCustomerCareText.setText(getResources().getString(R.string.customer_support)
                + getResources().getString(R.string.customer_care_popup));

        Button butChooseLanguageOk = (Button) view
                .findViewById(R.id.butChooseLanguageOk);
        butChooseLanguageOk.setTypeface(mediumTypeface);
        Button butChooseLanguageCancel = (Button) view
                .findViewById(R.id.butChooseLanguageCancel);
        butChooseLanguageCancel.setTypeface(mediumTypeface);
        if (((AstrosageKundliApplication) getActivity().getApplication())
                .getLanguageCode() == CGlobalVariables.ENGLISH) {
            butChooseLanguageCancel.setText(getResources().getString(R.string.cancel).toUpperCase());
            butChooseLanguageOk.setText(getResources().getString(R.string.ok).toUpperCase());
        }
        butChooseLanguageOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToChoosePlanOK();
            }
        });
        butChooseLanguageCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                goToChooseLanguageCancel();
            }
        });
        Linkify.addLinks(mCustomerCareText, Linkify.ALL);
        return view;
    }

    private void goToChoosePlanOK() {
            ((SilverPlanFrag) getParentFragment()).getSilverPlan(checkedRadioButtonId);
    }

    private void goToChooseLanguageCancel() {
        this.dismiss();
    }

}
