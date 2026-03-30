package com.ojassoft.astrosage.ui.fragments;

import android.annotation.TargetApi;
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
import com.ojassoft.astrosage.jinterface.IChartStyleFragmentDailog;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.utils.CGlobalVariables;

public class ChartStyleFragmentDailog extends DialogFragment {
    IChartStyleFragmentDailog iChartStyleFragmentDailog;
    int oldChartStyle = 0;

    Button butChooseOk, butChooseCancel;
    RadioGroup chartOptions;
    RadioButton rbNorth,
            rbsouth, rbEast;
    int languageIndex;

    public ChartStyleFragmentDailog() {

    }

    @Override
    @TargetApi(14)
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        iChartStyleFragmentDailog = (IChartStyleFragmentDailog) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        iChartStyleFragmentDailog = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.choose_chart_style_new_dialogbox, container);
        SharedPreferences sharedPreferencesForLang = getActivity().getSharedPreferences(CGlobalVariables.MYKUNDLI_LANGUAGE_PREFS_NAME, Context.MODE_PRIVATE);
        languageIndex = sharedPreferencesForLang.getInt(CGlobalVariables.APP_PREFS_AppLanguage, 0);


        chartOptions = (RadioGroup) view.findViewById(R.id.radioGroupChartStyle);
        rbNorth = (RadioButton) view.findViewById(R.id.radioNorth);
        rbsouth = (RadioButton) view.findViewById(R.id.radioSouth);
        rbEast = (RadioButton) view.findViewById(R.id.radioEast);


        butChooseOk = ((Button) view.findViewById(R.id.butChooseChartStyleOk));
        butChooseCancel = ((Button) view.findViewById(R.id.butChooseChartStyleCancel));

        butChooseOk.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);
        butChooseCancel.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);
        rbNorth.setTypeface(((BaseInputActivity) getActivity()).regularTypeface);
        rbsouth.setTypeface(((BaseInputActivity) getActivity()).regularTypeface);
        rbEast.setTypeface(((BaseInputActivity) getActivity()).regularTypeface);
        ((TextView) view.findViewById(R.id.textViewHeading)).setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);
        if (((AstrosageKundliApplication) getActivity().getApplication())
                .getLanguageCode() == CGlobalVariables.ENGLISH) {
            butChooseOk.setText(getResources().getString(R.string.ok).toUpperCase());
            butChooseCancel.setText(getResources().getString(R.string.cancel).toUpperCase());
        }
//		chartStyleOptions = (IcsSpinner)view.findViewById(R.id.ics_spinner_choose_chartstyle_Options);
//		chartStyleOptions.setAdapter(getSpinnerAdapter(getResources().getStringArray(R.array.chart_style)));
        butChooseOk.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                /*Toast.makeText(getActivity(),"click", Toast.LENGTH_LONG).show();*/
                goToChooseChartStyleOk();
            }
        });

        butChooseCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                goToChooseChartStyleCancel();
            }
        });
        intiValues();
        return view;
    }

    private void goToChooseChartStyleOk() {
        int newChartStyle = getSelectedChartStylePosition();
        if (oldChartStyle != newChartStyle) {
            //Toast.makeText(getActivity(), newChartStyle, Toast.LENGTH_SHORT).show();
            saveChartStyleInPreference(newChartStyle);
            iChartStyleFragmentDailog.onSelectedChartStyle(newChartStyle);
            //Log.e("newChartSyle", "" + newChartStyle);
        }
        this.dismiss();
    }

    private int getSelectedChartStylePosition() {
        int chartIndex = 0;
        switch (chartOptions.getCheckedRadioButtonId()) {
            case R.id.radioNorth:
                chartIndex = 0;
                break;
            case R.id.radioSouth:
                chartIndex = 1;
                break;
            case R.id.radioEast:
                chartIndex = 2;
                break;
        }
        return chartIndex;
    }

    private void saveChartStyleInPreference(int newChartStyle) {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(CGlobalVariables.APP_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        sharedPrefEditor.putInt(CGlobalVariables.APP_PREFS_ChartStyle, newChartStyle);
        sharedPrefEditor.commit();
    }

    private void intiValues() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(CGlobalVariables.APP_PREFS_NAME, Context.MODE_PRIVATE);
        oldChartStyle = sharedPreferences.getInt(CGlobalVariables.APP_PREFS_ChartStyle, CGlobalVariables.CHART_NORTH_STYLE);
        switch (oldChartStyle) {
            case 0:
                chartOptions.check(R.id.radioNorth);
                break;
            case 1:
                chartOptions.check(R.id.radioSouth);
                break;
            case 2:
                chartOptions.check(R.id.radioEast);
                break;
        }
    }

    private void goToChooseChartStyleCancel() {
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
