package com.ojassoft.astrosage.ui.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.interfaces.DiaglogDismiss;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.FontUtils;

public class DhruvLeadSucessfulDialog extends DialogFragment implements View.OnClickListener {

    Activity activity;
    TextView doneBtn;
    String rechargeMount;
    //DiaglogDismiss diaglogDismiss;

    public DhruvLeadSucessfulDialog() {
        //this.diaglogDismiss = diaglogDismiss;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.lead_success_msg_dialog, container);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        doneBtn = view.findViewById(R.id.doneBtn);

        FontUtils.changeFont(getActivity(), doneBtn, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);

        doneBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        /*Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }*/
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.doneBtn:
                try {
                    //diaglogDismiss.onDialogDismiss();
                    dismiss();
                }catch (Exception e){

                }
                break;

        }
    }
}

