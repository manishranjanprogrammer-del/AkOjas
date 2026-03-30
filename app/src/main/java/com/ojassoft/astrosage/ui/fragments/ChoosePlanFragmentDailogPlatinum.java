package com.ojassoft.astrosage.ui.fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.text.Html;
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
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.act.PurchasePlanHomeActivity;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

public class ChoosePlanFragmentDailogPlatinum extends DialogFragment {
    RadioButton yearlyPlan, monthlyPlan;
    int radioBtnId[] = {0,1};
    private int oldLanguageIndex = 0, checkedRadioButtonId = 0;
    private Button butChooseLanguageOk, butChooseLanguageCancel;
    public Typeface mediumTypeface;
    public int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    private TextView mCustomerCareText,termsAndCondition;

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
        View view = inflater.inflate(R.layout.choose_plan_dialog_platinum, container);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        termsAndCondition=(TextView)view.findViewById(R.id.terms_and_condition_tv);
        yearlyPlan = (RadioButton) view.findViewById(R.id.radioyearly);
        yearlyPlan.setText(((PurchasePlanHomeActivity)getActivity()).platinumPlanPriceYear);
        monthlyPlan = (RadioButton) view.findViewById(R.id.radiomonthly);
        monthlyPlan.setText(((PurchasePlanHomeActivity)getActivity()).platinumPlanPriceMonth);

        mCustomerCareText = (TextView) view.findViewById(R.id.customer_care);
        mCustomerCareText.setText(getResources().getString(R.string.customer_support)
                + getResources().getString(R.string.customer_care_popup));
        String termsAndConditionText=getResources().getString(R.string.dhruv_terms_and_condition);
        CUtils.setClickableSpan(termsAndCondition,
                Html.fromHtml(getString(R.string.dhruv_terms_and_condition)), getActivity(), ((BaseInputActivity)getActivity()).regularTypeface);

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

        butChooseLanguageOk = (Button) view
                .findViewById(R.id.butChooseLanguageOk);
        butChooseLanguageOk.setTypeface(mediumTypeface);
        butChooseLanguageCancel = (Button) view
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
        ((PlatinumPlanFrag) getParentFragment()).getPlatinumPlan(checkedRadioButtonId);
    }

    private void goToChooseLanguageCancel() {
        this.dismiss();
    }

}
