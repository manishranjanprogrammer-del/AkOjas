package com.ojassoft.astrosage.ui.fragments.horoscope;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.Spinner;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

public class HoroscopeNotificationFragment extends DialogFragment {
    // Typeface _typeface;

    RadioGroup radioGroupHoroscope;
    RadioButton radioYes, radioNo;
    Activity activity = null;
    String[] array_spinner;
    Spinner chooseRashi = null;
    LinearLayout layoutRasi;
    TextView tvChooseRashi;
    TextView titleTextView;
    TextView headingTextView;
    int selectedMoonSign = -1;

    int languageIndex;

    public HoroscopeNotificationFragment() {

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        selectedMoonSign = -1;
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        View view = inflater.inflate(R.layout.lay_horoscope_notofication_categary, container);

        SharedPreferences sharedPreferencesForLang = activity.getSharedPreferences(CGlobalVariables.MYKUNDLI_LANGUAGE_PREFS_NAME, Context.MODE_PRIVATE);
        languageIndex = sharedPreferencesForLang.getInt(CGlobalVariables.APP_PREFS_AppLanguage, 0);

        radioGroupHoroscope = (RadioGroup) view.findViewById(R.id.radioGroupHoroscope);
        radioYes = (RadioButton) view.findViewById(R.id.radioYes);
        radioNo = (RadioButton) view.findViewById(R.id.radioNo);
        chooseRashi = (Spinner) view.findViewById(R.id.spRashiName);
        layoutRasi = (LinearLayout) view.findViewById(R.id.layoutRasi);
        tvChooseRashi = (TextView) view.findViewById(R.id.tvChooseRashi);
        titleTextView = (TextView) view.findViewById(R.id.notification_title);
        headingTextView = (TextView) view.findViewById(R.id.textViewHeading);
        Button okButton = (Button) view.findViewById(R.id.butCooseOk);
        Button cancelButton = (Button) view.findViewById(R.id.butChooseCancel);

        okButton.setTypeface(((BaseInputActivity) activity).mediumTypeface);
        cancelButton.setTypeface(((BaseInputActivity) activity).mediumTypeface);
        titleTextView.setTypeface(((BaseInputActivity) activity).mediumTypeface);
        radioYes.setTypeface(((BaseInputActivity) activity).regularTypeface);
        radioNo.setTypeface(((BaseInputActivity) activity).regularTypeface);
        tvChooseRashi.setTypeface(((BaseInputActivity) activity).regularTypeface);
        headingTextView.setTypeface(((BaseInputActivity) activity).regularTypeface);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT)//added by neeraj 29/4/16 for show spinner higher virsion
        {
            chooseRashi.setPopupBackgroundResource(R.drawable.spinner_dropdown);


        }
        radioNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                selectedMoonSign = -1;
            }
        });


        if (((AstrosageKundliApplication) getActivity().getApplication())
                .getLanguageCode() == CGlobalVariables.ENGLISH) {
            okButton.setText(getResources().getString(R.string.yes).toUpperCase());
            cancelButton.setText(getResources().getString(R.string.no).toUpperCase());
        }
        okButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                goToChooseNotificationOK();
            }
        });
        cancelButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                HoroscopeNotificationFragment.this.dismiss();
            }
        });
        radioGroupHoroscope.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioYes) {
                    layoutRasi.setVisibility(View.VISIBLE);
                } else {
                    layoutRasi.setVisibility(View.GONE);
                }

            }
        });
        chooseRashi.setOnItemSelectedListener(new OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                selectedMoonSign = position; //ADDED BY BIJENDRA ON 29-05-14
                //CUtils.saveMoonSignIndex(getActivity(), position); //DISABLED BY BIJENDRA ON 29-05-14
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        array_spinner = getResources().getStringArray(R.array.rasi_full_name_list);
        ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_list_item, array_spinner) {

            public View getView(int position, View convertView, ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTypeface(((BaseInputActivity) activity).mediumTypeface);
                ((TextView) v).setPadding(10, 0, 10, 0);
                ((TextView) v).setBackgroundColor(getResources().getColor(R.color.white));
                return v;
            }


            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                ((TextView) v).setTypeface(((BaseInputActivity) activity).mediumTypeface);
                return v;
            }
        };

        //spinnerArrayAdapter.setDropDownViewResource(android.R.layout.simple_list_item_1);
        chooseRashi.setAdapter(spinnerArrayAdapter);
        intiValues();
        return view;
    }

    private void intiValues() {
        chooseRashi.setSelection(CUtils.getMoonSignIndex(getActivity()));
        if (CUtils.getHoroscopeNotificationWant(activity)) {
            radioYes.setChecked(true);
            layoutRasi.setVisibility(View.VISIBLE);
        } else {
            radioNo.setChecked(true);
            layoutRasi.setVisibility(View.GONE);
        }


    }

    private void goToChooseNotificationOK() {
        boolean wantNotification = true;
        if (radioYes.isChecked()) {
            wantNotification = true;
        } else {
            wantNotification = false;
        }
        CUtils.saveHoroscopeNotifationWant(activity, wantNotification, selectedMoonSign);
        this.dismiss();

    }


}