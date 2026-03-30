package com.ojassoft.astrosage.ui.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.util.Linkify;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.DialogFragment;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.utils.CGlobalVariables;

/**
 * Created by ojas-20 on 1/3/18.
 */

public class CustomerSupportDialog extends DialogFragment {

    public CustomerSupportDialog(){}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        SharedPreferences sharedPreferencesForLang = getActivity()
                .getSharedPreferences(
                        CGlobalVariables.MYKUNDLI_LANGUAGE_PREFS_NAME,
                        Context.MODE_PRIVATE);

        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.lay_customer_support, container);

        TextView textViewHeading = (TextView)view.findViewById(R.id.textViewHeading);
        TextView tvText = (TextView)view.findViewById(R.id.tvText);
        Button btnOk = (Button)view.findViewById(R.id.btnOk);

        textViewHeading.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);
        tvText.setTypeface(((BaseInputActivity) getActivity()).robotRegularTypeface);
        btnOk.setTypeface(((BaseInputActivity) getActivity()).regularTypeface);

        if (((AstrosageKundliApplication) getActivity().getApplication())
                .getLanguageCode() == CGlobalVariables.ENGLISH) {
            btnOk.setText(getResources().getString(R.string.ok).toUpperCase());
        }

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CustomerSupportDialog.this.dismiss();
            }
        });

        Linkify.addLinks(tvText, Linkify.ALL);

        return view;
    }
}
