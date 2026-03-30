package com.ojassoft.astrosage.ui.fragments.matching;

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
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.act.NameMatchingInputActivity;
import com.ojassoft.astrosage.ui.fragments.AstroSageParentDialogFragment;
import com.ojassoft.astrosage.utils.CGlobalVariables;

/**
 * Created by ojas on 4/5/16.
 */
public class NameMatchingDialog extends AstroSageParentDialogFragment {


    Activity activity;
    Typeface regularTypeface;
    Typeface mediumTypeface;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
        regularTypeface = ((BaseInputActivity) activity).regularTypeface;
        mediumTypeface = ((BaseInputActivity) activity).mediumTypeface;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final Dialog dialog = getDialog();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.lay_namematching_dialog, container);
        TextView titleTextView = (TextView) view.findViewById(R.id.title);
        titleTextView.setTypeface(mediumTypeface);
        TextView textViewHeading = (TextView) view.findViewById(R.id.textViewHeading);
        //textViewHeading.setText(getResources().getString(R.string.message_name_matching_result));
        textViewHeading.setTypeface(regularTypeface);

        Button imageyes = view.findViewById(R.id.butChooseAyanOk);

        Button imageno = view.findViewById(R.id.butChooseAyanCancel);
        if (((AstrosageKundliApplication) activity.getApplication()).getLanguageCode() == CGlobalVariables.ENGLISH) {
            imageyes.setText(getResources().getString(R.string.yes).toUpperCase());
            imageno.setText(getResources().getString(R.string.no).toUpperCase());
        }

        imageyes.setTypeface(mediumTypeface);
        imageno.setTypeface(mediumTypeface);
        imageyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ((NameMatchingInputActivity) activity).initBoySwarSpinner();

                dialog.dismiss();
            }
        });
        imageno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                dialog.dismiss();
            }
        });

        return view;

    }

    public static NameMatchingDialog getInstance() {
        NameMatchingDialog nameMatchingDialog = new NameMatchingDialog();
        return nameMatchingDialog;
    }
}
