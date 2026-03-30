package com.ojassoft.astrosage.ui.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.jinterface.IChooseAyanamsaFragment;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.OutputMasterActivity;
import com.ojassoft.astrosage.utils.CGlobalVariables;

public class AyanamsaFragmentDailog extends DialogFragment {
    //Typeface _typeface;
    IChooseAyanamsaFragment iChooseAyanamsaFragment;

    Button butChooseAyanOk, butChooseAyanCancel;
    RadioGroup ayanOptions;
    RadioButton rbNClahiri,
            rbKPNew,
            rbKPold,
            rbRaman,
            rbKPKhullar,
            rbSayan;

    int oldAyan = 0;
    int languageIndex;

    public AyanamsaFragmentDailog() {

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        iChooseAyanamsaFragment = (IChooseAyanamsaFragment) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        iChooseAyanamsaFragment = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.choose_ayanamsa_new_dialogbox, container);

        SharedPreferences sharedPreferencesForLang = getActivity().getSharedPreferences(CGlobalVariables.MYKUNDLI_LANGUAGE_PREFS_NAME, Context.MODE_PRIVATE);
        languageIndex = sharedPreferencesForLang.getInt(CGlobalVariables.APP_PREFS_AppLanguage, 0);

        ayanOptions = (RadioGroup) view.findViewById(R.id.radioGroupAyan);
        rbNClahiri = (RadioButton) view.findViewById(R.id.radioNCLahiri);
        rbKPNew = (RadioButton) view.findViewById(R.id.radioKPnew);
        rbKPold = (RadioButton) view.findViewById(R.id.radioKPold);
        rbRaman = (RadioButton) view.findViewById(R.id.radioRaman);
        rbKPKhullar = (RadioButton) view.findViewById(R.id.radioKPKhullar);
        rbSayan = (RadioButton) view.findViewById(R.id.radioSayan);

        ((TextView) view.findViewById(R.id.textViewHeading)).setTypeface(((OutputMasterActivity) getActivity()).mediumTypeface);
        butChooseAyanOk = ((Button) view.findViewById(R.id.butChooseAyanOk));
        butChooseAyanCancel = ((Button) view.findViewById(R.id.butChooseAyanCancel));


        rbNClahiri.setTypeface(((OutputMasterActivity) getActivity()).regularTypeface);
        rbKPNew.setTypeface(((OutputMasterActivity) getActivity()).regularTypeface);
        rbKPold.setTypeface(((OutputMasterActivity) getActivity()).regularTypeface);
        rbRaman.setTypeface(((OutputMasterActivity) getActivity()).regularTypeface);
        rbKPKhullar.setTypeface(((OutputMasterActivity) getActivity()).regularTypeface);
        rbSayan.setTypeface(((OutputMasterActivity) getActivity()).regularTypeface);
        butChooseAyanOk.setTypeface(((OutputMasterActivity) getActivity()).mediumTypeface);
        butChooseAyanCancel.setTypeface(((OutputMasterActivity) getActivity()).mediumTypeface);
        if (((AstrosageKundliApplication) getActivity().getApplication())
                .getLanguageCode() == CGlobalVariables.ENGLISH) {
            butChooseAyanOk.setText(getResources().getString(R.string.ok));
            butChooseAyanCancel.setText(getResources().getString(R.string.cancel));
        }

        butChooseAyanOk.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                goToChooseAyanOK();
            }
        });

        butChooseAyanCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                goToChooseAyanCancel();
            }
        });
        intiValues();

        return view;
    }

    private void intiValues() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(CGlobalVariables.APP_PREFS_NAME, Context.MODE_PRIVATE);
        oldAyan = sharedPreferences.getInt(CGlobalVariables.APP_PREFS_Ayanmasha, 0);
        switch (oldAyan) {
            case 0:
                ayanOptions.check(R.id.radioNCLahiri);
                break;
            case 1:
                ayanOptions.check(R.id.radioKPnew);
                break;
            case 2:
                ayanOptions.check(R.id.radioKPold);
                break;
            case 3:
                ayanOptions.check(R.id.radioRaman);
                break;
            case 4:
                ayanOptions.check(R.id.radioKPKhullar);
                break;
            case 5:
                ayanOptions.check(R.id.radioSayan);
                break;
        }

    }

    private void goToChooseAyanOK() {
        int ayanIndex = getSelectedAyanPosition();
        if (oldAyan != ayanIndex) {
            saveAyanInPreference(ayanIndex);
            iChooseAyanamsaFragment.onSelectedAyanamsa(ayanIndex);
        }
        this.dismiss();
    }

    private int getSelectedAyanPosition() {
        int ayanIndex = 0;
        switch (ayanOptions.getCheckedRadioButtonId()) {
            case R.id.radioNCLahiri:
                ayanIndex = 0;
                break;
            case R.id.radioKPnew:
                ayanIndex = 1;
                break;
            case R.id.radioKPold:
                ayanIndex = 2;
                break;
            case R.id.radioRaman:
                ayanIndex = 3;
                break;
            case R.id.radioKPKhullar:
                ayanIndex = 4;
                break;
            case R.id.radioSayan:
                ayanIndex = 5;
                break;
        }
        return ayanIndex;
    }

    private void saveAyanInPreference(int newAyan) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(CGlobalVariables.APP_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.putInt(CGlobalVariables.APP_PREFS_Ayanmasha, newAyan);
        sharedPrefEditor.commit();
    }

    private void goToChooseAyanCancel() {
        this.dismiss();
    }

    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        Dialog dialog = getDialog();
        if (dialog != null) {
            WindowManager.LayoutParams wmlp = dialog.getWindow()
                    .getAttributes();
            wmlp.width = (int) width - 40;
            wmlp.height = android.view.WindowManager.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(wmlp);
            //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

	/*private ArrayAdapter<CharSequence> getSpinnerAdapter(String[] spinnerOptions) {
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(getActivity(),  android.R.layout.simple_list_item_checked, spinnerOptions){
			
		     public View getView(int position, View convertView, ViewGroup parent) {
		             View v = super.getView(position, convertView, parent);
		             ((TextView) v).setTypeface(_typeface);
		             return v;
		     }
		     public View getDropDownView(int position,  View convertView,  ViewGroup parent) {
	             View v = super.getDropDownView(position, convertView, parent);
	            ((TextView) v).setTypeface(_typeface);
	            return v;
	    }
	  };
		return adapter;
   }*/

}
