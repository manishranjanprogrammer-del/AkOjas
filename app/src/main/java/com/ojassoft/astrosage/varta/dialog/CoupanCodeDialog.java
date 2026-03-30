package com.ojassoft.astrosage.varta.dialog;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.ui.activity.PaymentInformationActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;

public class CoupanCodeDialog extends DialogFragment implements View.OnClickListener {

    Activity activity;
    TextView headingTV;
    Button cancelBtn, applyCode;
    EditText editText;

    public CoupanCodeDialog() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        LANGUAGE_CODE = AstrosageKundliApplication.getAppContext().getLanguageCode();
        CUtils.getRobotoFont(getActivity(), LANGUAGE_CODE, CGlobalVariables.regular);

        View view = inflater.inflate(R.layout.coupan_code_dialog_layout, container);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        headingTV = view.findViewById(R.id.heading);
        cancelBtn = view.findViewById(R.id.cancel_btn);
        applyCode = view.findViewById(R.id.apply_code_btn);
        editText = view.findViewById(R.id.coupan_code_et);


        FontUtils.changeFont(getActivity(), headingTV, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(getActivity(), applyCode, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);

        cancelBtn.setOnClickListener(this);
        applyCode.setOnClickListener(this);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
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
            case R.id.cancel_btn:
                dismiss();
                break;

            case R.id.apply_code_btn:

                if (!TextUtils.isEmpty(editText.getText().toString())) {
                    dismiss();
                    ((PaymentInformationActivity) getActivity()).applyCoupanCode(editText.getText().toString());
                } else {

                }
                break;
        }
    }

}
