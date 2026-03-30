package com.ojassoft.astrosage.ui.fragments;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.textfield.TextInputLayout;
import androidx.fragment.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanPlace;
import com.ojassoft.astrosage.controller.ControllerManager;
import com.ojassoft.astrosage.customexceptions.UIDataOperationException;
import com.ojassoft.astrosage.jinterface.matching.ICitySearch;
import com.ojassoft.astrosage.model.CDatabaseHelper;
import com.ojassoft.astrosage.ui.act.ActPlaceSearch;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;
import com.ojassoft.astrosage.varta.volley_network.VolleyServiceHandler;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ojas on 2/3/16.
 */
public class CustomCitySearch extends Fragment implements VolleyResponse {

    //Typeface typeface;
    Spinner spiTimeZone;
    TextView tvLatitude, tvLongitude, tvSelectTimeZone;
    RadioButton rbLatNorth, rbLatSouth, rbLongEast, rbLongWest;
    RadioGroup rbGroupPlaceLat, rbGroupPlaceLong;
    EditText editTxtVLatDeg, editTxtVLatMin, editTxtVLongDeg, editTxtVLongMin;
    CheckBox make_it_default_city2;
    Button butPlaceCustomOk;
    BeanPlace customCityValue = null;
    TextInputLayout inputLayoutLatDeg, inputLayoutLatmin, inputLayoutLongDeg, inputLayoutLongMin;

    protected SQLiteDatabase sQLiteDatabase = null;
    private String[][] arrTimeZone = null;
    String cityName = "Manual_Lat_Long";
    boolean isCutomLatNorth = true, isCutomLongEast = true;
    ICitySearch iCitySearch;
    int GET_TIME_ZONE = 0;
    String _timezoneString = "";


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        iCitySearch = (ICitySearch) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        iCitySearch = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.customcitysearchfraglayout, container, false);
        //typeface=((ActPlaceSearch)getActivity()).typeface;

        try {
            customCityValue = ((ActPlaceSearch) getActivity()).customCityValue;
        } catch (Exception ex) {
            //Log.i("Exception", ex.getMessage().toString());
        }

        setLayRef(rootView);

        intiValues();

        setCustomeCityDetail();

        return rootView;
    }

    private void setLayRef(View view) {

        inputLayoutLatDeg = (TextInputLayout) view.findViewById(R.id.input_layout_lat_deg);
        inputLayoutLatmin = (TextInputLayout) view.findViewById(R.id.input_layout_lat_min);
        inputLayoutLongDeg = (TextInputLayout) view.findViewById(R.id.input_layout_long_deg);
        inputLayoutLongMin = (TextInputLayout) view.findViewById(R.id.input_layout_long_min);

        spiTimeZone = (Spinner) view.findViewById(R.id.spiTimeZone);
        tvLatitude = (TextView) view.findViewById(R.id.tvLatitude);
        tvLatitude.setTypeface(((ActPlaceSearch) getActivity()).regularTypeface);
        tvLongitude = (TextView) view.findViewById(R.id.tvLongitude);
        tvLongitude.setTypeface(((ActPlaceSearch) getActivity()).regularTypeface);
        tvSelectTimeZone = (TextView) view.findViewById(R.id.tvSelectTimeZone);
        tvSelectTimeZone.setTypeface(((ActPlaceSearch) getActivity()).regularTypeface);
        rbGroupPlaceLat = (RadioGroup) view.findViewById(R.id.rbGroupPlaceLat);
        rbGroupPlaceLong = (RadioGroup) view.findViewById(R.id.rbGroupPlaceLong);

        editTxtVLatDeg = (EditText) view.findViewById(R.id.editTxtVLatDeg);
        editTxtVLatDeg.addTextChangedListener(new MyTextWatcher(editTxtVLatDeg));
        editTxtVLatMin = (EditText) view.findViewById(R.id.editTxtVLatMin);
        editTxtVLatMin.addTextChangedListener(new MyTextWatcher(editTxtVLatMin));
        editTxtVLongDeg = (EditText) view.findViewById(R.id.editTxtVLongDeg);
        editTxtVLongDeg.addTextChangedListener(new MyTextWatcher(editTxtVLongDeg));
        editTxtVLongMin = (EditText) view.findViewById(R.id.editTxtVLongMin);
        editTxtVLongMin.addTextChangedListener(new MyTextWatcher(editTxtVLongMin));

        make_it_default_city2 = (CheckBox) view.findViewById(R.id.check_save_city_in_pref_2);
        make_it_default_city2.setTypeface(((ActPlaceSearch) getActivity()).regularTypeface);
        butPlaceCustomOk = (Button) view.findViewById(R.id.butPlaceCustomOk);
        butPlaceCustomOk.setTypeface(((ActPlaceSearch) getActivity()).regularTypeface);
        butPlaceCustomOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToPlaceCustomOk();
            }
        });
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT)//added by neeraj 29/4/16 for visible spinner on higher virsions
        {
            spiTimeZone.setPopupBackgroundResource(R.drawable.spinner_dropdown);
            //cmnt and add by NEERAJ
            // languageOptions.setAdapter(langAdapter);
        }

        if (((AstrosageKundliApplication) getActivity().getApplication())
                .getLanguageCode() == CGlobalVariables.ENGLISH) {
            butPlaceCustomOk.setText(getResources().getString(R.string.ok).toUpperCase());
        }

        rbGroupPlaceLat
                .setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {

                        switch (checkedId) {
                            case R.id.rbLatNorth:
                                isCutomLatNorth = true;
                                break;
                            case R.id.rbLatSouth:
                                isCutomLatNorth = false;
                                break;
                        }
                    }
                });


        rbGroupPlaceLong
                .setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {

                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {

                        switch (checkedId) {
                            case R.id.rbLongEast:
                                isCutomLongEast = true;
                                break;
                            case R.id.rbLongWest:
                                isCutomLongEast = false;
                                break;
                        }
                    }
                });

    }

    protected void setCustomeCityDetail() {
        int latDeg = 0, latMin = 0, longDeg = 0, longMin = 0;

        try {
            latDeg = Integer.valueOf(customCityValue.getLatDeg().trim());
            latMin = Integer.valueOf(customCityValue.getLatMin().trim());
            longDeg = Integer.valueOf(customCityValue.getLongDeg().trim());
            longMin = Integer.valueOf(customCityValue.getLongMin().trim());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        int timeZoneIndex = 0;
        String _timezone = String.valueOf(customCityValue.getTimeZoneValue());
        _timezone = _timezone.trim();
        String convertedValue = null;
        float convertedFloatValue = 0.0f;
        if(arrTimeZone != null) {
            for (int i = 0; i < arrTimeZone.length; i++) {
                convertedValue = arrTimeZone[i][1];
                if (convertedValue != null) {
                    try {
                        convertedFloatValue = Float.valueOf(convertedValue);
                    } catch (NumberFormatException e) {
                        e.printStackTrace();
                    }
                    convertedValue = String.valueOf(convertedFloatValue);
                }
                if (convertedValue.equals(_timezone)) {
                    timeZoneIndex = i;
                    break;
                }
            }
        }
        spiTimeZone.setSelection(timeZoneIndex);

        editTxtVLatDeg.setText(String.valueOf(latDeg));
        editTxtVLatMin.setText(String.valueOf(latMin));
        editTxtVLongDeg.setText(String.valueOf(longDeg));
        editTxtVLongMin.setText(String.valueOf(longMin));

        if (customCityValue.getLatDir().trim().equalsIgnoreCase("N")) {
            rbGroupPlaceLat.check(R.id.rbLatNorth);
        } else {
            rbGroupPlaceLat.check(R.id.rbLatSouth);
        }
        if (customCityValue.getLongDir().trim().equalsIgnoreCase("E")) {
            rbGroupPlaceLong.check(R.id.rbLongEast);
        } else {
            rbGroupPlaceLong.check(R.id.rbLongWest);
        }

    }

    private void intiValues() {
        List<String> listTimeZone = new ArrayList<String>();
        ArrayAdapter<String> adapter = null;
        CDatabaseHelper cDatabaseHelper = null;
        try {
            cDatabaseHelper = new ControllerManager()
                    .getDatabaseHelperObject(getActivity());
            sQLiteDatabase = cDatabaseHelper.getWritableDatabase();
            ControllerManager cm = new ControllerManager();
            arrTimeZone = cm.searchTimeZone(sQLiteDatabase, "");
            if(arrTimeZone != null) {
                for (int i = 0; i < arrTimeZone.length; i++)
                    listTimeZone.add(arrTimeZone[i][0].trim());
            }
            adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_list_item, listTimeZone) {

                public View getView(int position, View convertView, ViewGroup parent) {
                    View v = super.getView(position, convertView, parent);
                    ((TextView) v).setTypeface(((BaseInputActivity) getActivity()).robotRegularTypeface);
                    ((TextView) v).setPadding(10, 0, 10, 0);
                    return v;
                }


                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    View v = super.getDropDownView(position, convertView, parent);
                    ((TextView) v).setTypeface(((BaseInputActivity) getActivity()).robotRegularTypeface);
                    return v;
                }
            };
            spiTimeZone.setAdapter(adapter);
        } catch (UIDataOperationException e) {
            Toast.makeText(getActivity(), e.getMessage(),
                    Toast.LENGTH_LONG).show();
        } finally {
            if (cDatabaseHelper != null)
                cDatabaseHelper.close();
            if (sQLiteDatabase != null) {
                sQLiteDatabase.close();
            }
        }

    }

    // CUSTOM CITY INPUT FUNCTIONS
    public void goToPlaceCustomOk() {
        if (validateData()) {

            double orignal_lat  =  CUtils.getLatLngFromDMS( Double.parseDouble(editTxtVLatDeg.getText().toString().trim()),
                    Double.parseDouble( editTxtVLatMin.getText().toString().trim() ) ) ;
            double orignal_lon = CUtils.getLatLngFromDMS( Double.parseDouble(editTxtVLongDeg.getText().toString().trim()),
                    Double.parseDouble( editTxtVLongMin.getText().toString().trim() ) ) ;

            //orignal_lat = 38.9637;
            //orignal_lon = 35.2433;

            getTimeZoneFromServer(orignal_lat, orignal_lon);

            //Bundle bundle = getBundleOfPlaceValues();
            /*Intent intent = new Intent();
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            this.finish();*/
            //iCitySearch.goToPlaceCustomOk(bundle);
        }
    }

    private void callGoToPlaceCustomOk(){
        Bundle bundle = getBundleOfPlaceValues();
        iCitySearch.goToPlaceCustomOk(bundle);
    }

    private boolean isValidCustomForm() {
        boolean _isValid = true;

        if (editTxtVLongDeg.getText().toString().length() < 1) {
            _isValid = false;
            editTxtVLongDeg.setError("  ");
        } else if ((Integer
                .valueOf(editTxtVLongDeg.getText().toString().trim()) < 0)
                || (Integer
                .valueOf(editTxtVLongDeg.getText().toString().trim()) > 179)) {
            _isValid = false;
            editTxtVLongDeg.setError(" 0-179 ");
        }
        if (editTxtVLongMin.getText().toString().length() < 1) {
            _isValid = false;
            editTxtVLongMin.setError("  ");
        } else if ((Integer
                .valueOf(editTxtVLongMin.getText().toString().trim()) < 0)
                || (Integer
                .valueOf(editTxtVLongMin.getText().toString().trim()) > 59)) {
            _isValid = false;
            editTxtVLongMin.setError(" 0-59 ");
        }
        if (editTxtVLatDeg.getText().toString().length() < 1) {
            _isValid = false;
            editTxtVLatDeg.setError("  ");
        } else if ((Integer.valueOf(editTxtVLatDeg.getText().toString().trim()) < 0)
                || (Integer.valueOf(editTxtVLatDeg.getText().toString().trim()) > 89)) {
            _isValid = false;
            editTxtVLatDeg.setError(" 0-89 ");
        }
        if (editTxtVLatMin.getText().toString().length() < 1) {
            _isValid = false;
            editTxtVLatMin.setError("  ");
        } else if ((Integer.valueOf(editTxtVLatMin.getText().toString().trim()) < 0)
                || (Integer.valueOf(editTxtVLatMin.getText().toString().trim()) > 59)) {
            _isValid = false;
            editTxtVLatMin.setError(" 0-59 ");
        }

        return _isValid;
    }

    @Override
    public void onResume() {
        super.onResume();
        /*desableColorFilter(editTxtVLatDeg);
        desableColorFilter(editTxtVLatMin);
        desableColorFilter(editTxtVLongDeg);
        desableColorFilter(editTxtVLongMin);*/
        desableLayoutPro(inputLayoutLatDeg);
        desableLayoutPro(inputLayoutLatmin);
        desableLayoutPro(inputLayoutLongDeg);
        desableLayoutPro(inputLayoutLongMin);
    }

    private void desableLayoutPro(TextInputLayout inputLayout) {
        inputLayout.setHintEnabled(false);
        //inputLayout.setErrorEnabled(false);
    }

    private void desableColorFilter(EditText editText) {
        editText.getBackground().setColorFilter(null);
    }

    private boolean validateData() {
        boolean flag = false;
        if (validateName(editTxtVLatDeg, inputLayoutLatDeg, getString(R.string.mandatory_fields))
                && validateName(editTxtVLatMin, inputLayoutLatmin, getString(R.string.mandatory_fields))
                && validateName(editTxtVLongDeg, inputLayoutLongDeg, getString(R.string.mandatory_fields))
                && validateName(editTxtVLongMin, inputLayoutLongMin, getString(R.string.mandatory_fields)))
            flag = true;

        return flag;
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
                case R.id.editTxtVLatDeg:
                    validateName(editTxtVLatDeg, inputLayoutLatDeg, getString(R.string.mandatory_fields));
                    break;
                case R.id.editTxtVLatMin:
                    validateName(editTxtVLatMin, inputLayoutLatmin, getString(R.string.mandatory_fields));
                    break;
                case R.id.editTxtVLongDeg:
                    validateName(editTxtVLongDeg, inputLayoutLongDeg, getString(R.string.mandatory_fields));
                    break;
                case R.id.editTxtVLongMin:
                    validateName(editTxtVLongMin, inputLayoutLongMin, getString(R.string.mandatory_fields));
                    break;

            }
        }
    }

    private boolean validateName(EditText name, TextInputLayout inputLayout, String message) {
        boolean value = true;
        if (name == editTxtVLatDeg) {
            if (name.getText().toString().trim().isEmpty()) {
                inputLayout.setError(message);
                inputLayout.setErrorEnabled(true);
                requestFocus(name);
                name.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                value = false;
            } else if (Integer.parseInt(name.getText().toString()) < 0 || Integer.parseInt(name.getText().toString()) > 89) {
                inputLayout.setError("0-89");
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
        if (name == editTxtVLatMin) {
            if (name.getText().toString().trim().isEmpty()) {
                inputLayout.setError(message);
                inputLayout.setErrorEnabled(true);
                requestFocus(name);
                name.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                value = false;
            } else if (Integer.parseInt(name.getText().toString()) < 0 || Integer.parseInt(name.getText().toString()) > 59) {
                inputLayout.setError("0-59");
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
        if (name == editTxtVLongDeg) {
            if (name.getText().toString().trim().isEmpty()) {
                inputLayout.setError(message);
                inputLayout.setErrorEnabled(true);
                requestFocus(name);
                name.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                value = false;
            } else if (Integer.parseInt(name.getText().toString()) < 0 || Integer.parseInt(name.getText().toString()) > 179) {
                inputLayout.setError("0-179");
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
        if (name == editTxtVLongMin) {
            if (name.getText().toString().trim().isEmpty()) {
                inputLayout.setError(message);
                inputLayout.setErrorEnabled(true);
                requestFocus(name);
                name.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                value = false;
            } else if (Integer.parseInt(name.getText().toString()) < 0 || Integer.parseInt(name.getText().toString()) > 59) {
                inputLayout.setError("0-59");
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
            getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        }
    }

    /**
     * This funciton i suse to prepare bundle for the place object
     *
     * @return Bundle
     * @author Bijendra
     * @date 27-nov-2012
     */

    private Bundle getBundleOfPlaceValues() {
        Bundle bundle = null;

        BeanPlace _objPlace = new BeanPlace();
        try {

            _objPlace.setCityId(-1);
            _objPlace.setCountryId(-1);
            _objPlace.setTimeZoneId(-1);

            _objPlace.setCityName(cityName);
            _objPlace.setLongDeg(editTxtVLongDeg.getText().toString());
            _objPlace.setLongMin(editTxtVLongMin.getText().toString());

            if (isCutomLongEast)
                _objPlace.setLongDir("E");
            else
                _objPlace.setLongDir("W");

            _objPlace.setLatDeg(editTxtVLatDeg.getText().toString());
            _objPlace.setLatMin(editTxtVLatMin.getText().toString());

            if (isCutomLatNorth)
                _objPlace.setLatDir("N");
            else
                _objPlace.setLatDir("S");

            _objPlace.setTimeZoneName(arrTimeZone[spiTimeZone
                    .getSelectedItemPosition()][0]);
            _objPlace.setTimeZoneValue(Float.valueOf(arrTimeZone[spiTimeZone
                    .getSelectedItemPosition()][1]));

            _objPlace.setTimeZoneString(_timezoneString);
            if (make_it_default_city2.isChecked()) {
                CUtils.saveCityAsDefaultCity(getActivity(), _objPlace);
            }

            // 12-oct-2015
            BeanPlace beanPlace = CUtils.getLatLongAndTimeZone(_objPlace);

            // bundle = CUtils.getBundleOfPlaceValues(_objPlace);
            bundle = CUtils.getBundleOfPlaceValues(beanPlace);

        } catch (Exception e) {
            Toast.makeText(getActivity(), e.getMessage(), Toast.LENGTH_LONG).show();
        }

        return bundle;

    }

    private void getTimeZoneFromServer(double lat, double lon) {
        String url = "https://secure.geonames.org/timezone?lat="
                + lat + "&lng=" + lon
                + "&username="+CUtils.getRandomUsername();
        //LibCUtils.vollyGetRequest(activity, GPSCitySearch.this, url, GET_TIME_ZONE);
       // Log.e("SAN CCS Varta", " getTimeZoneFromServer " + url);

        HashMap<String, String> params = new HashMap<String, String>();
        RequestQueue queue = VolleySingleton.getInstance(getActivity()).getRequestQueue();
        StringRequest stringRequest = new VolleyServiceHandler(Request.Method.GET, url,
                CustomCitySearch.this, true, params, GET_TIME_ZONE).getMyStringRequest();
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);

    }

    @Override
    public void onResponse(String response, int method) {

        Log.e("CCSTest 1", " onResponse " + response);
        if (getActivity() != null) {
            if (!TextUtils.isEmpty(response)) {
                try {
                    _timezoneString = response.substring(
                            response.indexOf("<timezoneId>")
                                    + "<timezoneId>".length(),
                            response.indexOf("</timezoneId>"));
                    _timezoneString = _timezoneString.trim();
                    //Log.e("SAN CCS Varta", " _timezoneString " + _timezoneString);
                    callGoToPlaceCustomOk();
                } catch (Exception e){
                    //
                }

            }

        }

    }

    @Override
    public void onError(VolleyError error) {

        if (getActivity() != null) {
            if (error != null && error.getMessage() != null) {
                com.ojassoft.astrosage.varta.utils.CUtils.showSnackbar(editTxtVLatDeg, error.getMessage(), getActivity());
            }
        }

    }

}
