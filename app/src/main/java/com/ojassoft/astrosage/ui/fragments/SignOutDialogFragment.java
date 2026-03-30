package com.ojassoft.astrosage.ui.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.jinterface.ISearchBirthDeatils;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.act.SetPasswordActivity;
import com.ojassoft.astrosage.utils.CGlobalVariables;


/**
 * Created by ojas on 4/5/16.
 */
public class SignOutDialogFragment extends AstroSageParentDialogFragment {

    public SignOutDialogFragment(){}

    ISearchBirthDeatils callback;
    TextView setPasswordTextView, warningTextView;
    Button cancelButton, setPasswordButton;
    // BeanHoroPersonalInfo beanHoroPersonalInfo;
    // boolean isCheckForDeletePersonalKundli, isLocal;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

      /*  beanHoroPersonalInfo = (BeanHoroPersonalInfo) getArguments().getSerializable("beanHoroPersonalInfo");
        isCheckForDeletePersonalKundli = getArguments().getBoolean("isCheckForDeletePersonalKundli");
        isLocal = getArguments().getBoolean("isLocal");*/

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
        View view = inflater.inflate(R.layout.signout_dialog_fragment_layout, container);
        setPasswordTextView = (TextView) view.findViewById(R.id.set_a_password_textview);
        warningTextView = (TextView) view.findViewById(R.id.warning_text);
        cancelButton = (Button) view.findViewById(R.id.cancelButton);
        setPasswordButton = (Button) view.findViewById(R.id.setpasswordButton);
        if (((BaseInputActivity) getActivity()).LANGUAGE_CODE == CGlobalVariables.ENGLISH) {
            setPasswordButton.setText(getResources().getString(R.string.set_password_text).toUpperCase());
            cancelButton.setText(getResources().getString(R.string.cancel).toUpperCase());
        }

        setPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(getActivity(), SetPasswordActivity.class);
                intent.putExtra("dologout", true);
                startActivityForResult(intent, 101);
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        setTypefaceOfVies();
        return view;
    }

    public static SignOutDialogFragment getInstance() {
        SignOutDialogFragment signOutDialogFragment = new SignOutDialogFragment();
        return signOutDialogFragment;
    }

    private void setTypefaceOfVies() {
        setPasswordTextView.setTypeface(((BaseInputActivity) getActivity()).regularTypeface);
        warningTextView.setTypeface(((BaseInputActivity) getActivity()).regularTypeface);
        cancelButton.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);
        setPasswordButton.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);
    }
}


