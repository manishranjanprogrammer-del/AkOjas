package com.ojassoft.astrosage.ui.fragments;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.fragment.app.DialogFragment;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.jinterface.IYearInputBoxPopupFragmentDialog;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

public class YearInputBoxPopupFragmentDialog extends DialogFragment {
    private EditText _ebtYear;
    private int _year = 0;
    private int _birthYear = 0;
    private TextView tvMsgToShow;
    private int _languageCode = CGlobalVariables.HINDI;
    private Typeface typeface;
    ImageButton butSet, butCancel;
    ImageButton butDecrease, butIncrease;
    IYearInputBoxPopupFragmentDialog _iYearInputBoxPopupFragmentDialog;

    /*public YearInputBoxPopupFragmentDialog(int inputYear,int birthYear,Typeface typeface,int languageCode)
    {
        _year=inputYear;
        _birthYear=birthYear;
        this.typeface=typeface;

    }*/
    public YearInputBoxPopupFragmentDialog() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        Bundle myArg = this.getArguments();
        _languageCode = myArg.getInt("LANGUAGE_CODE");
        _year = myArg.getInt("INPUT_YEAR");
        _birthYear = myArg.getInt("BIRTH_YEAR");
        typeface = CUtils.getRobotoFont(getActivity(), _languageCode, CGlobalVariables.regular);
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);

        View view = inflater.inflate(R.layout.layyearinputboxpopup, container);
        tvMsgToShow = (TextView) view.findViewById(R.id.tvMsgToShow);
        tvMsgToShow.setTypeface(typeface);
        _ebtYear = (EditText) view.findViewById(R.id.ebtYear);
        butSet = (ImageButton) view.findViewById(R.id.butSet);
        butCancel = (ImageButton) view.findViewById(R.id.butCancel);
        butDecrease = (ImageButton) view.findViewById(R.id.butDecrease);
        butIncrease = (ImageButton) view.findViewById(R.id.butIncrease);

        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(4);
        _ebtYear.setFilters(FilterArray);

        int iYear = _birthYear + _year;
        _ebtYear.setText(String.valueOf(iYear));

        String msg = null;
        if (_languageCode == CGlobalVariables.ENGLISH)
            msg = getResources().getString(R.string.change_year_label) + " "
                    + _birthYear
                    + getResources().getString(R.string.reverse)
                    + (_birthYear + 120);
        else if (_languageCode == CGlobalVariables.HINDI)
            msg = _birthYear
                    + getResources().getString(R.string.reverse)
                    + (_birthYear + 120) + " "
                    + getResources().getString(R.string.change_year_label);

        tvMsgToShow.setText(msg);

        butDecrease.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                toGoDecrease();
            }
        });

        butIncrease.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                toGoIncrease();
            }
        });

        butSet.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                setValueForCalender();
            }
        });
        butCancel.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                YearInputBoxPopupFragmentDialog.this.dismiss();
            }
        });
        return view;
    }

    private void toGoIncrease() {
        try {
            int _yn = Integer.valueOf(_ebtYear.getText().toString().trim());
            _yn += 1;
            if (_yn > (_birthYear + 120))
                _yn = _birthYear;
            _ebtYear.setText(String.valueOf(_yn));
        } catch (Exception e) {

        }

    }

    private void toGoDecrease() {
        try {
            int _yn = Integer.valueOf(_ebtYear.getText().toString().trim());
            _yn -= 1;
            if (_yn < _birthYear)
                _yn = _birthYear + 120;
            _ebtYear.setText(String.valueOf(_yn));
        } catch (Exception e) {

        }
    }

    public void setValueForCalender() {
        int year1 = 0, yearDiff = 0;
        if (checkYearInput()) {
            year1 = Integer.valueOf(_ebtYear.getText().toString().trim());
            yearDiff = year1 - com.ojassoft.astrosage.utils.CGlobal.getCGlobalObject().getHoroPersonalInfoObject()
                    .getDateTime().getYear();

            _iYearInputBoxPopupFragmentDialog.onSelectedInputYear(yearDiff);
            this.dismiss();

        }
    }

    private boolean checkYearInput() {
        int userInputYear = -1;
        int birthYear = com.ojassoft.astrosage.utils.CGlobal.getCGlobalObject().getHoroPersonalInfoObject()
                .getDateTime().getYear();

        boolean _isValid = true;
        // THIS FUNCTION IS UPDATED ON 3-SEP-13(BIJENDRA)
        try {
            userInputYear = Integer.valueOf(_ebtYear.getText().toString()
                    .trim());
        } catch (Exception e) {
            _isValid = true;

        }
        if (!_isValid) {
            _ebtYear.setError("Please enter valid year");
            _isValid = false;
        }

        if (_isValid) {
            int diff = userInputYear - birthYear;
            if (diff < 0) {
                _ebtYear.setError("Year can not less than birth year");
                _isValid = false;
            }
            if (diff > 119) {
                _ebtYear.setError("Please enter valid year");
                _isValid = false;
            }

        }
        /*
         * if(_ebtYear.getText().toString().trim().length()==0 ||
		 * _ebtYear.getText().toString().trim().length()<4 || userInputYear <
		 * _birthYear) { _ebtYear.setError("Please enter valid year");
		 * _isValid=false; } if(_ebtYear.getText().toString().trim().length()==0
		 * || _ebtYear.getText().toString().trim().length()<4 || userInputYear <
		 * _birthYear) { _ebtYear.setError("Please enter valid year");
		 * _isValid=false; }
		 */
        return _isValid;
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        _iYearInputBoxPopupFragmentDialog = (IYearInputBoxPopupFragmentDialog) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        _iYearInputBoxPopupFragmentDialog = null;
    }

    public void updateInputYear(int inputYear) {
        _year = inputYear;
        int iYear = _birthYear + _year;
        _ebtYear.setText(String.valueOf(iYear));
    }


}
