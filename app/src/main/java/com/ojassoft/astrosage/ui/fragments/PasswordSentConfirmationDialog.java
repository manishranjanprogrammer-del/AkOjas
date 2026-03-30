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

//import com.google.analytics.tracking.android.Log;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.ActForgetPassword;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;

/**
 * Created by ojas on 4/5/16.
 */
public class PasswordSentConfirmationDialog extends AstroSageParentDialogFragment {

    Activity activity;
    Typeface regularTypeface;
    Typeface mediumTypeface;
    int LANGUAGE_CODE;
    String msg, btnText;
    static final String msgKey = "MsgKey";
    static final String btnTxtKey = "btnTxtKey";

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
        msg = getArguments().getString(msgKey);
        btnText = getArguments().getString(btnTxtKey);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final Dialog dialog = getDialog();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.lay_alert_pop_up, container);
        TextView heading = (TextView) view.findViewById(R.id.tvPopupHeading);
        heading.setTypeface(regularTypeface);
        heading.setText(msg);
        Button btnChange = (Button) view.findViewById(R.id.btnPopupGetRashi);
        //	btnChange.setText(getResources().getString(R.string.ok));
        //added by neeraj to display text
        if (LANGUAGE_CODE == 0) {
            btnChange.setText(btnText.toUpperCase());
        } else {
            btnChange.setText(btnText);
        }

        btnChange.setTypeface(mediumTypeface);

        btnChange.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                dialog.dismiss();
                closeActivity();
            }

        });
        return view;
    }

    public static PasswordSentConfirmationDialog getInstance(String msg, String btnText) {
        Bundle bundle = new Bundle();
        bundle.putString(msgKey, msg);
        bundle.putString(btnTxtKey, btnText);
        PasswordSentConfirmationDialog passwordSentConfirmationDialog = new PasswordSentConfirmationDialog();
        passwordSentConfirmationDialog.setArguments(bundle);
        return passwordSentConfirmationDialog;
    }

    private void closeActivity() {
        try {
            ((ActForgetPassword) activity).closeActivity();
        } catch (Exception ex) {
            //Log.i(ex.getMessage().toString());
        }
    }
}
