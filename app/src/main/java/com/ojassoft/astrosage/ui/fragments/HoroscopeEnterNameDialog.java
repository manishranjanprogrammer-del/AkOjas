package com.ojassoft.astrosage.ui.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import com.google.android.material.textfield.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

//import com.google.analytics.tracking.android.Log;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.act.horoscope.HoroscopeHomeActivity;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

/**
 * Created by ojas on 4/5/16.
 */
public class HoroscopeEnterNameDialog extends AstroSageParentDialogFragment {

    Activity activity;
    Typeface regularTypeface;
    Typeface mediumTypeface;
    EditText etName;
    TextInputLayout nameInputLayout;

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
        activity=null;
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
        View view = inflater.inflate(R.layout.lay_pop_up_enter_name, container);
        TextView heading = (TextView) view.findViewById(R.id.tvPopupHeading);
        TextView tvHeading = (TextView) view.findViewById(R.id.tvHeading);
        tvHeading.setText(getResources().getString(R.string.moon_sign_by_name));
        nameInputLayout = (TextInputLayout) view.findViewById(R.id.input_layout_name);
        etName = (EditText) view.findViewById(R.id.etPopupName);
        etName.addTextChangedListener(new MyTextWatcher(etName));
        Button btnChange = (Button) view.findViewById(R.id.btnPopupGetRashi);

        if (((AstrosageKundliApplication) getActivity().getApplication())
                .getLanguageCode() == CGlobalVariables.ENGLISH) {
            btnChange.setText(getResources().getString(
                    R.string.get_your_moon_sign_hindi).toUpperCase());
        } else {
            btnChange.setText(getResources().getString(
                    R.string.get_your_moon_sign_hindi));

        }
        btnChange.setTypeface(mediumTypeface);
        tvHeading.setTypeface(mediumTypeface);
        heading.setTypeface(regularTypeface);
        btnChange.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (validateName()) {
                    if (CUtils.isConnectedWithInternet(activity)) {
                        getRashiByName();
                        // CUtils.googleAnalyticSend(null,
                        // CGlobalVariables.GOOGLE_ANALYTIC_LAUNCH,"Choose Rashi Screen","Know Your Moon Sign By Name",0L);
                        CUtils.googleAnalyticSendWitPlayServie(
                                activity,
                                CGlobalVariables.GOOGLE_ANALYTIC_LAUNCH,
                                "Choose Rashi Screen",
                                "Know Your Moon Sign By Name");
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_KNOW_YOUR_MOON_BY_NAME, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

                        dialog.dismiss();
                    } else {
                        dialog.dismiss();
                        MyCustomToast mct2 = new MyCustomToast(
                                activity,
                                activity.getLayoutInflater(),
                                activity, mediumTypeface);
                        mct2.show(getResources()
                                .getString(R.string.no_internet));
                    }

                }
                CUtils.hideMyKeyboard(getActivity());

            }
        });
        return view;

    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.etPopupName:
                    validateName();
                    break;

            }
        }
    }

    private boolean validateName() {
        if (etName.getText().toString().trim().isEmpty() || etName.getText().toString().trim().length() < 3) {

            nameInputLayout.setErrorEnabled(true);
            nameInputLayout.setError(getString(R.string.please_enter_name_v));
            //requestFocus(etName);
            etName.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
            return false;
        } else {
            nameInputLayout.setErrorEnabled(false);
            nameInputLayout.setError(null);
            etName.getBackground().setColorFilter(getResources().getColor(R.color.ColorPrimary), PorterDuff.Mode.SRC_ATOP);
        }

        return true;
    }

    /* private void requestFocus(View view) {
         if (view.requestFocus()) {
             //activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
         }
     }
 */
    public static HoroscopeEnterNameDialog getInstance() {
        HoroscopeEnterNameDialog horoscopeEnterNameDialog = new HoroscopeEnterNameDialog();
        return horoscopeEnterNameDialog;
    }

    private void getRashiByName() {

        try {
            ((HoroscopeHomeActivity) activity).getRashiByName(etName.getText().toString().trim());
        } catch (Exception ex) {
            //Log.i(ex.getMessage().toString());
        }

    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        CUtils.hideMyKeyboard(getActivity());
    }
}
