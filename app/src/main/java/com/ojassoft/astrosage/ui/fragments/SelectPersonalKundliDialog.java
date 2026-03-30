package com.ojassoft.astrosage.ui.fragments;

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
import com.ojassoft.astrosage.jinterface.IDialogFragmentClick;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.act.HomeInputScreen;
import com.ojassoft.astrosage.utils.CGlobalVariables;

/**
 * Created by ojas on 4/5/16.
 */
public class SelectPersonalKundliDialog extends AstroSageParentDialogFragment {

    Activity activity;
    Typeface regularTypeface;
    Typeface mediumTypeface;
    IDialogFragmentClick callback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
        regularTypeface = ((BaseInputActivity)activity).regularTypeface;
        mediumTypeface = ((BaseInputActivity)activity).mediumTypeface;

        try {
            callback = (IDialogFragmentClick) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement DialogClickListener interface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity=null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final Dialog dialog = getDialog();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.custom_dialog_for_check_personal_kundali, container);
        TextView TitleAstrosage_Kundali = (TextView) view.findViewById(R.id.titleAstrosage_Kundali);
        TitleAstrosage_Kundali.setTypeface(((HomeInputScreen) activity).mediumTypeface);
        TextView textViewHeading = (TextView) view.findViewById(R.id.text_view);
        textViewHeading.setTypeface(((HomeInputScreen) activity).regularTypeface);
        textViewHeading.setText(R.string.isyourpersonalkundali);

        Button yesButton = (Button) view.findViewById(R.id.yesbtn);
        Button noButton = (Button) view.findViewById(R.id.nobtn);

        textViewHeading.setText(R.string.isyourpersonalkundali);


        if (((AstrosageKundliApplication) getActivity().getApplication()).getLanguageCode() == CGlobalVariables.ENGLISH) {
            yesButton.setText(getResources().getString(R.string.yes).toUpperCase());
            noButton.setText(getResources().getString(R.string.no).toUpperCase());
        }
        TitleAstrosage_Kundali.setTypeface(((HomeInputScreen) activity).mediumTypeface);
        textViewHeading.setTypeface(((HomeInputScreen) activity).regularTypeface);
        yesButton.setTypeface(((HomeInputScreen) activity).mediumTypeface);
        noButton.setTypeface(((HomeInputScreen) activity).mediumTypeface);

        yesButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                callback.onYesClick();
            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                callback.onNoClick();
            }
        });
        return view;

    }

    public static SelectPersonalKundliDialog getInstance(){
        SelectPersonalKundliDialog selectPersonalKundliDialog = new SelectPersonalKundliDialog();
        return selectPersonalKundliDialog;
    }
}
