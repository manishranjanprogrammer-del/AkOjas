package com.ojassoft.astrosage.ui.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.Nullable;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.ActMontlyCalendar;
import com.ojassoft.astrosage.ui.act.AstroPrefrenceActivity;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

/**
 * Created by ojas on ८/२/१८.
 */

public class ChooseHinduMonth extends AstroSageParentDialogFragment {


    Activity activity;
    Typeface regularTypeface;
    Typeface mediumTypeface;
    int LANGUAGE_CODE;
    RadioButton rbAamant, rbPurnimant;
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
        View view = inflater.inflate(R.layout.choose_hindu_month_layout, container);
        headingTextView = (TextView) view.findViewById(R.id.textViewHeading);
        rbAamant = (RadioButton) view.findViewById(R.id.radioAmmanth);
        rbPurnimant = (RadioButton) view.findViewById(R.id.radioPurnimant);
        okButton = (Button) view.findViewById(R.id.ok_btn);
        cancelButton = (Button) view.findViewById(R.id.cancel_btn);
        if (CUtils.getIntData(activity, CGlobalVariables.HINDU_MONTH_KEY, 0) == 0) {
            rbAamant.setChecked(true);
        } else {
            rbPurnimant.setChecked(true);
        }
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* if (rbAamant.isChecked()) {
                    CUtils.saveIntData(activity, CGlobalVariables.HINDU_MONTH_KEY, 0);
                } else if (rbPurnimant.isChecked()) {
                    CUtils.saveIntData(activity, CGlobalVariables.HINDU_MONTH_KEY, 1);
                }
                ((ActMontlyCalendar) activity).updateData(((ActMontlyCalendar) activity).selectedfragment);*/
                dialog.dismiss();

            }
        });
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(getActivity() instanceof AstroPrefrenceActivity)) {
                    startActivity(new Intent(getActivity(), AstroPrefrenceActivity.class));
                }
                dialog.dismiss();
            }
        });
        setTypefaceOfView();

        return view;

    }

    private void setTypefaceOfView() {
        rbAamant.setTypeface(regularTypeface);
        rbPurnimant.setTypeface(regularTypeface);
        okButton.setTypeface(mediumTypeface);
        cancelButton.setTypeface(mediumTypeface);
        headingTextView.setTypeface(mediumTypeface);
    }

    public static ChooseHinduMonth getInstance(String expiryDate) {
        Bundle bundle = new Bundle();
        ChooseHinduMonth chooseHinduMonth = new ChooseHinduMonth();
        chooseHinduMonth.setArguments(bundle);
        return chooseHinduMonth;
    }
}
