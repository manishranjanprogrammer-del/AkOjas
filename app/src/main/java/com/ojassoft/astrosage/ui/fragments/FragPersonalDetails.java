package com.ojassoft.astrosage.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import com.google.android.material.textfield.TextInputLayout;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.jinterface.IPersonalDetails;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

/**
 * Created by ojas-20 on 17/5/18.
 */

public class FragPersonalDetails extends DialogFragment {

    private EditText edtMailId,edtPhone;
    private Button btnCancel,btnOk;
    private Context context;
    private IPersonalDetails iPersonalDetails;
    private int planIndex = -1;
    private boolean isEmailIdVisible,isPhnNumbVisisble;
    private LinearLayout llEmail,llPhone;
    private TextInputLayout input_layout_email,input_layout_phone;
    private TextView textViewHeading;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.context = context;
        iPersonalDetails = (IPersonalDetails)context;
        planIndex = getArguments().getInt("planIndex");
        isPhnNumbVisisble =  getArguments().getBoolean("isPhnNumbVisisble");
        isEmailIdVisible =  getArguments().getBoolean("isEmailIdVisible");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.lay_personal_details, container);

        setLayRef(view);

        setListeners();

        return view;
    }

    /**
     * This method is used to set layout view
     * @param view
     */
    private void setLayRef(View view){
        edtMailId = (EditText)view.findViewById(R.id.edtMailId);
        edtPhone = (EditText)view.findViewById(R.id.edtPhone);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnOk = (Button)view.findViewById(R.id.btnOk);

        edtMailId.setTypeface(((BaseInputActivity) getActivity()).robotRegularTypeface);
        edtPhone.setTypeface(((BaseInputActivity) getActivity()).robotRegularTypeface);
        btnOk.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);
        btnCancel.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);

        if (((AstrosageKundliApplication) getActivity().getApplication())
                .getLanguageCode() == CGlobalVariables.ENGLISH) {
            btnOk.setText(getResources().getString(R.string.ok).toUpperCase());
            btnCancel.setText(getResources().getString(R.string.cancel).toUpperCase());
        }

        llEmail = (LinearLayout)view.findViewById(R.id.llEmail);
        llPhone = (LinearLayout)view.findViewById(R.id.llPhone);

        input_layout_email = (TextInputLayout)view.findViewById(R.id.input_layout_email);
        input_layout_phone = (TextInputLayout)view.findViewById(R.id.input_layout_phone);
        textViewHeading = (TextView)view.findViewById(R.id.textViewHeading);
        textViewHeading.setTypeface(((BaseInputActivity) getActivity()).robotMediumTypeface);

        if(isEmailIdVisible)
            llEmail.setVisibility(View.VISIBLE);
        else
            llEmail.setVisibility(View.GONE);

        if(isPhnNumbVisisble)
            llPhone.setVisibility(View.VISIBLE);
        else
            llPhone.setVisibility(View.GONE);

    }

    private void setListeners(){
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateData()) {
                    iPersonalDetails.onYesClick(planIndex);
                    FragPersonalDetails.this.dismiss();
                }
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragPersonalDetails.this.dismiss();
            }
        });
    }


    private boolean validateData() {
        boolean flag = false;
        if(isEmailIdVisible && isPhnNumbVisisble) {
            if (validateName(edtMailId, input_layout_email, getString(R.string.email_one_v))
                    && validateName(edtPhone, input_layout_phone, getString(R.string.astro_shop_User_mob_no))) {
                    CUtils.saveStringData(context, CGlobalVariables.ASTROASKAQUESTIONUSERPHONENUMBER, edtPhone.getText().toString());
                    CUtils.saveStringData(context, CGlobalVariables.ASTROASKAQUESTIONUSEREMAIL, edtMailId.getText().toString());

                flag = true;
            }
        }else if(isEmailIdVisible){
            if (validateName(edtMailId, input_layout_email, getString(R.string.email_one_v))) {
                CUtils.saveStringData(context, CGlobalVariables.ASTROASKAQUESTIONUSEREMAIL, edtMailId.getText().toString());
                flag = true;
            }
        }else if(isPhnNumbVisisble){
            if (validateName(edtPhone, input_layout_phone, getString(R.string.astro_shop_User_mob_no))) {
                CUtils.saveStringData(context, CGlobalVariables.ASTROASKAQUESTIONUSERPHONENUMBER, edtPhone.getText().toString());
                flag = true;
            }
        }

        return flag;
    }

    private boolean validateName(EditText name, TextInputLayout inputLayout, String message) {
        boolean value = true;

        if (name == edtMailId) {

            if (edtMailId.getText().toString().trim().isEmpty()) {
                input_layout_email.setError(getResources().getString(R.string.email_one_v));
                inputLayout.setErrorEnabled(true);
                requestFocus(edtMailId);
                name.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                value = false;
                // etUseremail.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
            }
            if (!(CUtils.isValidEmail(edtMailId.getText().toString().trim()))) {
                input_layout_email.setError(getResources().getString(R.string.email_one_v_astro_service));
                inputLayout.setErrorEnabled(true);
                requestFocus(edtMailId);
                name.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                value = false;
                // etUseremail.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
            } else {
                input_layout_email.setErrorEnabled(false);
                input_layout_email.setError(null);
                edtMailId.getBackground().setColorFilter(getResources().getColor(R.color.ColorPrimary), PorterDuff.Mode.SRC_ATOP);
            }
        }

        if (name == edtPhone) {
            if (name.getText().toString().trim().isEmpty() || name.getText().toString().trim().length() < 9) {
                inputLayout.setError(message);
                inputLayout.setErrorEnabled(true);
                requestFocus(name);
                name.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                value = false;
            } else {
                inputLayout.setErrorEnabled(false);
                inputLayout.setError(null);
                name.getBackground().setColorFilter(getResources().getColor(R.color.ColorPrimary), PorterDuff.Mode.SRC_ATOP);
            }
        }

        return value;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            ((Activity)context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

}
