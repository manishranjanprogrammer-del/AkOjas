package com.ojassoft.astrosage.ui.act;


import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

//import com.google.analytics.tracking.android.EasyTracker;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

public class YearInputBoxPopup extends Activity {
    private EditText _ebtYear;
    private int _year = 0;
    private int _birthYear = 0;
    private TextView tvMsgToShow;
    private int _languageCode = CGlobalVariables.HINDI;
    Typeface _typeface = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layyearinputboxpopup);
        _year = getIntent().getExtras().getInt("YN");
        _birthYear = getIntent().getExtras().getInt("BY");
        _languageCode = getIntent().getExtras().getInt("LC");
        _ebtYear = (EditText) findViewById(R.id.ebtYear);

        tvMsgToShow = (TextView) findViewById(R.id.tvMsgToShow);
        InputFilter[] FilterArray = new InputFilter[1];
        FilterArray[0] = new InputFilter.LengthFilter(4);
        _ebtYear.setFilters(FilterArray);

        int iYear = com.ojassoft.astrosage.utils.CGlobal.getCGlobalObject().getHoroPersonalInfoObject()
                .getDateTime().getYear() + _year;
        _ebtYear.setText(String.valueOf(iYear));


        String msg = null;
        if (_languageCode == CGlobalVariables.ENGLISH)
            msg = getResources().getString(R.string.change_year_label) + " "
                    + _birthYear
                    + getResources().getString(R.string.under_score_character)
                    + (_birthYear + 120);
        else if (_languageCode == CGlobalVariables.HINDI)
            msg = _birthYear
                    + getResources().getString(R.string.under_score_character)
                    + (_birthYear + 120) + " "
                    + getResources().getString(R.string.change_year_label);

        tvMsgToShow.setText(msg);
        setCustomTypeFace();

    }

    private void setCustomTypeFace() {
        _typeface = CUtils.getRobotoFont(getApplicationContext(), _languageCode, CGlobalVariables.regular);
        tvMsgToShow.setTypeface(_typeface);

    }

    public void toGoIncrease(View v) {
        try {
            int _yn = Integer.valueOf(_ebtYear.getText().toString().trim());
            _yn += 1;
            if (_yn > (_birthYear + 120))
                _yn = _birthYear;
            _ebtYear.setText(String.valueOf(_yn));
        } catch (Exception e) {

        }

    }

    public void toGoDecrease(View v) {
        try {
            int _yn = Integer.valueOf(_ebtYear.getText().toString().trim());
            _yn -= 1;
            if (_yn < _birthYear)
                _yn = _birthYear + 120;
            _ebtYear.setText(String.valueOf(_yn));
        } catch (Exception e) {

        }
    }

    public void goToCancel(View v) {
        setResult(RESULT_CANCELED);

        this.finish();
    }

    public void goToShow(View v) {
        int year1 = 0, yearDiff = 0;
        if (checkYearInput()) {
            Intent intent = new Intent();
            year1 = Integer.valueOf(_ebtYear.getText().toString().trim());
            yearDiff = year1 - com.ojassoft.astrosage.utils.CGlobal.getCGlobalObject().getHoroPersonalInfoObject()
                    .getDateTime().getYear();
            Bundle b = new Bundle();
            b.putInt("YN", yearDiff);

            intent.putExtras(b);
            setResult(RESULT_OK, intent);
            this.finish();
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
        return _isValid;
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onStart()
     */
    @Override
    protected void onStart() {
        super.onStart();
        //EasyTracker.getInstance().activityStart(this);
    }

    /* (non-Javadoc)
     * @see com.actionbarsherlock.app.SherlockFragmentActivity#onStop()
     */
    @Override
    protected void onStop() {
        super.onStop();
        //EasyTracker.getInstance().activityStop(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        _typeface = CUtils.getRobotoFont(getApplicationContext(), CUtils.getLanguageCodeFromPreference(getApplicationContext()), CGlobalVariables.regular);
    }


}
