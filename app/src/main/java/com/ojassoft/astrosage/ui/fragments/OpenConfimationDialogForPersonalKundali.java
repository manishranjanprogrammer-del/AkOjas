package com.ojassoft.astrosage.ui.fragments;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.jinterface.ISearchBirthDeatils;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.utils.CGlobalVariables;

/**
 * Created by ojas on 5/5/16.
 */
public class OpenConfimationDialogForPersonalKundali extends AstroSageParentDialogFragment {

    ISearchBirthDeatils callback;
    BeanHoroPersonalInfo beanHoroPersonalInfo;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            beanHoroPersonalInfo = (BeanHoroPersonalInfo)getArguments().getSerializable("BeanHoroPersonalInfo");
            callback = (ISearchBirthDeatils) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement DialogClickListener interface");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final Dialog dialog = getDialog();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.custom_dialog_for_check_personal_kundali, container);
        TextView textViewHeading = (TextView) view.findViewById(R.id.text_view);
        Button yesButton = (Button) view.findViewById(R.id.yesbtn);
        Button noButton = (Button) view.findViewById(R.id.nobtn);
        TextView titleTextView = (TextView) view.findViewById(R.id.titleAstrosage_Kundali);
        textViewHeading.setText(R.string.mychart_dialog_text);
        if (((AstrosageKundliApplication) getActivity().getApplication()).getLanguageCode() == CGlobalVariables.ENGLISH) {
            yesButton.setText(getResources().getString(R.string.yes).toUpperCase());
            noButton.setText(getResources().getString(R.string.no).toUpperCase());
        }

        textViewHeading.setTypeface(((BaseInputActivity) getActivity()).regularTypeface);
        titleTextView.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);
        titleTextView.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);
        yesButton.setTypeface(((BaseInputActivity) getActivity()).regularTypeface);
        textViewHeading.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);
        noButton.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);
        yesButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                callback.openConfirmationDialogForPersonalKundaliCallBack(beanHoroPersonalInfo);
            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        return view;
    }

    public static OpenConfimationDialogForPersonalKundali getInstance(BeanHoroPersonalInfo beanHoroPersonalInfo){

        Bundle bundle = new Bundle();
        bundle.putSerializable("BeanHoroPersonalInfo",beanHoroPersonalInfo);

        OpenConfimationDialogForPersonalKundali openConfimationDialogForPersonalKundali = new OpenConfimationDialogForPersonalKundali();
        openConfimationDialogForPersonalKundali.setArguments(bundle);
        return openConfimationDialogForPersonalKundali;
    }

}
