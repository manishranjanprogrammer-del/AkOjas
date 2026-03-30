package com.ojassoft.astrosage.ui.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.constants.AppConstants;
import com.ojassoft.astrosage.misc.FontUtils;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.act.VartaReqJoinActivity;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

/**
 * Created by ojas on 22/12/16.
 */

public class VerifyOtpDailogFrag extends AstroParentFrag implements View.OnClickListener {
    TextView dialogHeading, dialogDescription;
    ImageView closeDialogButton;
    EditText edtNum1, edtNum2, edtNum3, edtNum4, edtNum5, edtNum6;
    Button btnVerify, btnResend;
    Bundle bundle;
    int LANGUAGE_CODE = 0;
    String countryCode, mobileNo;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.verify_otp_dialog, container);
        final Dialog dialog = getDialog();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawableResource(R.color.bg_transparent);
        dialog.setCanceledOnTouchOutside(false);
        bundle = getArguments();
        initLayout(view);
        initListner();
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private void initLayout(View view) {
        closeDialogButton = view.findViewById(R.id.imgDialogClose);
        dialogHeading = view.findViewById(R.id.textDialogTitle);
        dialogDescription = view.findViewById(R.id.tvDesc);
        edtNum1 = view.findViewById(R.id.edtNum1);
        edtNum2 = view.findViewById(R.id.edtNum2);
        edtNum3 = view.findViewById(R.id.edtNum3);
        edtNum4 = view.findViewById(R.id.edtNum4);
        edtNum5 = view.findViewById(R.id.edtNum5);
        edtNum6 = view.findViewById(R.id.edtNum6);
        btnResend = view.findViewById(R.id.btnResend);
        btnVerify = view.findViewById(R.id.btnVerify);
        edtNum1.requestFocus();

        FontUtils.changeFont(getActivity(), dialogHeading, AppConstants.FONT_ROBOTO_MEDIUM);
        FontUtils.changeFont(getActivity(), dialogDescription, AppConstants.FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(getActivity(), edtNum1, AppConstants.FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(getActivity(), edtNum2, AppConstants.FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(getActivity(), edtNum3, AppConstants.FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(getActivity(), edtNum4, AppConstants.FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(getActivity(), edtNum5, AppConstants.FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(getActivity(), edtNum6, AppConstants.FONT_ROBOTO_REGULAR);
        FontUtils.changeFont(getActivity(), btnResend, AppConstants.FONT_ROBOTO_MEDIUM);
        FontUtils.changeFont(getActivity(), btnVerify, AppConstants.FONT_ROBOTO_MEDIUM);

        if (bundle != null) {
            countryCode = bundle.getString(CGlobalVariables.COUNTRY_CODE);
            mobileNo = bundle.getString(CGlobalVariables.MOBILE);
            String descText = dialogDescription.getText().toString().replace("#", countryCode + "-" + mobileNo);
            dialogDescription.setText(descText);
        }
    }

    private void initListner() {
        closeDialogButton.setOnClickListener(this);
        btnResend.setOnClickListener(this);
        btnVerify.setOnClickListener(this);
        edtNum1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() == 1) {
                    edtNum2.requestFocus();
                }
            }
        });
        edtNum2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() == 1) {
                    edtNum3.requestFocus();
                }
                if (editable.toString().isEmpty()) {
                    edtNum1.requestFocus();
                }
            }
        });
        edtNum3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() == 1) {
                    edtNum4.requestFocus();
                }
                if (editable.toString().isEmpty()) {
                    edtNum2.requestFocus();
                }
            }
        });
        edtNum4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() == 1) {
                    edtNum5.requestFocus();
                }
                if (editable.toString().isEmpty()) {
                    edtNum3.requestFocus();
                }
            }
        });
        edtNum5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().length() == 1) {
                    edtNum6.requestFocus();
                }
                if (editable.toString().isEmpty()) {
                    edtNum4.requestFocus();
                }
            }
        });
        edtNum6.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().isEmpty()) {
                    edtNum5.requestFocus();
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imgDialogClose: {
                //addGoogleAnalytics(CGlobalVariables.GOOGLE_ANALYTIC_PAYMENT_FAILED_POPUP_CLOSE);
                VerifyOtpDailogFrag.this.dismiss();
                break;
            }
            case R.id.btnVerify: {
                try {
                    processVerifyOtp();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            }
            case R.id.btnResend: {
                ((VartaReqJoinActivity) getActivity()).resendOtp();
                break;
            }
        }
    }

    private void addGoogleAnalytics(String googleAnalyticPaymentFailedKey) {
        CUtils.googleAnalyticSendWitPlayServie(getActivity(),
                CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                googleAnalyticPaymentFailedKey, null);
        CUtils.fcmAnalyticsEvents(googleAnalyticPaymentFailedKey, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
    }

    private void processVerifyOtp() {
        String num1 = edtNum1.getText().toString();
        String num2 = edtNum2.getText().toString();
        String num3 = edtNum3.getText().toString();
        String num4 = edtNum4.getText().toString();
        String num5 = edtNum5.getText().toString();
        String num6 = edtNum6.getText().toString();

        if (num1.isEmpty() || num2.isEmpty() || num3.isEmpty() || num4.isEmpty() || num5.isEmpty() || num6.isEmpty()) {
            ((BaseInputActivity) getActivity()).showSnackbar(btnVerify, getActivity().getString(R.string.enter_otp));
            return;
        }
        hideKeyboard();
        String otp = num1 + num2 + num3 + num4 + num5 + num6;
        ((VartaReqJoinActivity) getActivity()).submitOtp(otp);
    }

    private void hideKeyboard() {
        final InputMethodManager im = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        //hide a keyboard on click outside dialog
        View focus = getDialog().getCurrentFocus();
        if (focus != null) {
            im.hideSoftInputFromWindow(focus.getWindowToken(), 0);
        }
    }
}
