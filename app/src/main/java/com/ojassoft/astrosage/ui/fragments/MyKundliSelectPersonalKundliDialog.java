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
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.jinterface.ISearchBirthDeatils;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.MychartActivity;
import com.ojassoft.astrosage.utils.CGlobalVariables;

/**
 * Created by ojas on 5/5/16.
 */
public class MyKundliSelectPersonalKundliDialog extends AstroSageParentDialogFragment {

    BeanHoroPersonalInfo beanHoroPersonalInfo;
    ISearchBirthDeatils callback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        beanHoroPersonalInfo = (BeanHoroPersonalInfo)getArguments().getSerializable("beanHoroPersonalInfo");

        try {
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
        TextView TitleAstrosage_Kundali = (TextView) view.findViewById(R.id.titleAstrosage_Kundali);
        TextView textViewHeading = (TextView) view.findViewById(R.id.text_view);
        Button yesButton = (Button) view.findViewById(R.id.yesbtn);
        Button noButton = (Button) view.findViewById(R.id.nobtn);
        textViewHeading.setText(R.string.isyourpersonalkundali);

        TitleAstrosage_Kundali.setTypeface(((MychartActivity) getActivity()).mediumTypeface);
        textViewHeading.setTypeface(((MychartActivity) getActivity()).regularTypeface);
        yesButton.setTypeface(((MychartActivity) getActivity()).mediumTypeface);
        noButton.setTypeface(((MychartActivity) getActivity()).mediumTypeface);

        if (((AstrosageKundliApplication) getActivity().getApplication()).getLanguageCode() == CGlobalVariables.ENGLISH) {
            yesButton.setText(getResources().getString(R.string.yes).toUpperCase());
            noButton.setText(getResources().getString(R.string.no).toUpperCase());
        }
        yesButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                callback.selectPersonalMyKundali(beanHoroPersonalInfo);
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

    public static MyKundliSelectPersonalKundliDialog getInstance(BeanHoroPersonalInfo beanHoroPersonalInfo){

        Bundle bundle = new Bundle();
        bundle.putSerializable("beanHoroPersonalInfo", beanHoroPersonalInfo);
        MyKundliSelectPersonalKundliDialog myKundliSelectPersonalKundliDialog = new MyKundliSelectPersonalKundliDialog();
        myKundliSelectPersonalKundliDialog.setArguments(bundle);

        return myKundliSelectPersonalKundliDialog;
    }
}
