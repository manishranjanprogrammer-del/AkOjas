package com.ojassoft.astrosage.varta.ui.fragments;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.PorterDuff;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.interfacefile.ICitySearch;
import com.ojassoft.astrosage.varta.model.BeanPlace;
import com.ojassoft.astrosage.varta.ui.activity.ActPlaceSearch;
import com.ojassoft.astrosage.varta.utils.CDatabaseHelper;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.ControllerManager;
import com.ojassoft.astrosage.varta.utils.MyLocation;
import com.ojassoft.astrosage.varta.utils.UIDataOperationException;
import com.ojassoft.astrosage.varta.volley_network.VolleyResponse;
import com.ojassoft.astrosage.varta.volley_network.VolleyServiceHandler;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ojas on 2/3/16.
 */
public class GPSCitySearch extends Fragment implements VolleyResponse {

    // Typeface typeface;
    Spinner spiTimeZone;
    TextView tvLatitude, tvLongitude, tvSelectTimeZone;
    RadioButton rbLatNorth, rbLatSouth, rbLongEast, rbLongWest;
    RadioGroup rbGroupPlaceLat, rbGroupPlaceLong;
    EditText editTxtVLatDeg, editTxtVLatMin, editTxtVLongDeg, editTxtVLongMin;
    CheckBox make_it_default_city2;
    Button butPlaceCustomOk;
    BeanPlace customCityValue = null;
    protected SQLiteDatabase sQLiteDatabase = null;
    private String[][] arrTimeZone = null;
    String cityName = "Current Location";
    boolean isCutomLatNorth = true, isCutomLongEast = true;
    ICitySearch iCitySearch;
    //ProgressBar progressBarForGPS;
    private ImageView loadmore_gif;
    double _lat = 0, _long = 0;
    MyLocation myLocation;
    ImageView timeZoneStatus;
    TextInputLayout inputLayoutLatDeg, inputLayoutLatmin, inputLayoutLongDeg, inputLayoutLongMin;
    boolean isVisibleToUser = false;
    View rootView;
    Activity activity;
    //TimeZoneAsync timeZoneAsync;
    //static boolean isGPSEnabledDialogBoxAlreadyRunning = false;
    int GET_TIME_ZONE = 0;
    String _timezoneString = "";


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        iCitySearch = (ICitySearch) activity;
        this.activity = activity;
        //isGPSEnabledDialogBoxAlreadyRunning = false;
    }

    @Override
    public void onDetach() {
        super.onDetach();
       /* if (timeZoneAsync != null && timeZoneAsync.getStatus() == AsyncTask.Status.RUNNING) {
            timeZoneAsync.cancel(true);
        }*/
        iCitySearch = null;
        activity = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.customcitysearchfraglayout_varta, container, false);
        //typeface=((ActPlaceSearch)getActivity()).typeface;

        try {
            customCityValue = ((ActPlaceSearch) activity).customCityValue;
        } catch (Exception ex) {
            //Log.i("Exception", ex.getMessage().toString());
        }

        setLayRef(rootView);

        intiValues();

        setCustomeCityDetail();

        try {
            if (isVisibleToUser) {
                isVisibleToUser = false;
                findCurrentLocation();
            }
        } catch (Exception ex) {
            //Log.i("Exception", ex.getMessage().toString());
        }

        return rootView;
    }

    private void setLayRef(View view) {

        inputLayoutLatDeg = (TextInputLayout) view.findViewById(R.id.input_layout_lat_deg);
        inputLayoutLatmin = (TextInputLayout) view.findViewById(R.id.input_layout_lat_min);
        inputLayoutLongDeg = (TextInputLayout) view.findViewById(R.id.input_layout_long_deg);
        inputLayoutLongMin = (TextInputLayout) view.findViewById(R.id.input_layout_long_min);

        spiTimeZone = (Spinner) view.findViewById(R.id.spiTimeZone);
        tvLatitude = (TextView) view.findViewById(R.id.tvLatitude);
        //  tvLatitude.setTypeface(((ActPlaceSearch) activity).regularTypeface);
        tvLongitude = (TextView) view.findViewById(R.id.tvLongitude);
        //   tvLongitude.setTypeface(((ActPlaceSearch) activity).regularTypeface);
        tvSelectTimeZone = (TextView) view.findViewById(R.id.tvSelectTimeZone);
        //  tvSelectTimeZone.setTypeface(((ActPlaceSearch) activity).regularTypeface);
        rbGroupPlaceLat = (RadioGroup) view.findViewById(R.id.rbGroupPlaceLat);
        rbGroupPlaceLong = (RadioGroup) view.findViewById(R.id.rbGroupPlaceLong);

        editTxtVLatDeg = (EditText) view.findViewById(R.id.editTxtVLatDeg);
        editTxtVLatDeg.setTextColor(getResources().getColor(R.color.black));
        editTxtVLatDeg.addTextChangedListener(new MyTextWatcher(editTxtVLatDeg));
        editTxtVLatMin = (EditText) view.findViewById(R.id.editTxtVLatMin);
        editTxtVLatMin.setTextColor(getResources().getColor(R.color.black));
        editTxtVLatMin.addTextChangedListener(new MyTextWatcher(editTxtVLatMin));
        editTxtVLongDeg = (EditText) view.findViewById(R.id.editTxtVLongDeg);
        editTxtVLongDeg.setTextColor(getResources().getColor(R.color.black));
        editTxtVLongDeg.addTextChangedListener(new MyTextWatcher(editTxtVLongDeg));
        editTxtVLongMin = (EditText) view.findViewById(R.id.editTxtVLongMin);
        editTxtVLongMin.setTextColor(getResources().getColor(R.color.black));
        editTxtVLongMin.addTextChangedListener(new MyTextWatcher(editTxtVLongMin));


        make_it_default_city2 = (CheckBox) view.findViewById(R.id.check_save_city_in_pref_2);
        //   make_it_default_city2.setTypeface(((ActPlaceSearch) activity).regularTypeface);
        butPlaceCustomOk = (Button) view.findViewById(R.id.butPlaceCustomOk);
        //   butPlaceCustomOk.setTypeface(((ActPlaceSearch) activity).mediumTypeface);
        butPlaceCustomOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToPlaceCustomOk();
            }
        });
        spiTimeZone.setPopupBackgroundResource(R.drawable.spinner_dropdown);
        //cmnt and add by NEERAJ
        // languageOptions.setAdapter(langAdapter);


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

        //progressBarForGPS = (ProgressBar) view.findViewById(R.id.progressBarForGPS);
        loadmore_gif = (ImageView) view.findViewById(R.id.loadmore_gif);

        timeZoneStatus = (ImageView) view.findViewById(R.id.timeZoneStatus);

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
                    .getDatabaseHelperObject(activity);
            sQLiteDatabase = cDatabaseHelper.getWritableDatabase();
            ControllerManager cm = new ControllerManager();
            arrTimeZone = cm.searchTimeZone(sQLiteDatabase, "");
            for (int i = 0; i < arrTimeZone.length; i++)
                listTimeZone.add(arrTimeZone[i][0].trim());
            adapter = new ArrayAdapter<String>(activity, R.layout.spinner_list_item, listTimeZone) {

                public View getView(int position, View convertView, ViewGroup parent) {
                    View v = super.getView(position, convertView, parent);
                    //  ((TextView) v).setTypeface(((BaseInputActivity) activity).robotRegularTypeface);
                    ((TextView) v).setPadding(10, 0, 10, 0);
                    //((TextView) v).setTextSize(activity.getResources().getDimension(R.dimen.spinner_text_size));
                    //((TextView) v).setBackgroundColor(activity.getResources().getColor(R.color.white));
                    return v;
                }


                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    View v = super.getDropDownView(position, convertView, parent);
                    //((TextView) v).setTypeface(((BaseInputActivity) activity).robotRegularTypeface);
                   // ((TextView) v).setTextSize(activity.getResources().getDimension(R.dimen.spinner_text_size));
                    return v;
                }
            };

            spiTimeZone.setAdapter(adapter);
        } catch (UIDataOperationException e) {
            CUtils.showSnackbar(editTxtVLatDeg, e.getMessage(), getActivity());
        } finally {
            if (cDatabaseHelper != null)
                cDatabaseHelper.close();
        }

    }

    // CUSTOM CITY INPUT FUNCTIONS
    public void goToPlaceCustomOk() {
        if (validateData()) {
            Bundle bundle = getBundleOfPlaceValues();
            /*Intent intent = new Intent();
            intent.putExtras(bundle);
            setResult(RESULT_OK, intent);
            this.finish();*/
            iCitySearch.goToPlaceCustomOk(bundle);
        }
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
                CUtils.saveCityAsDefaultCity(activity, _objPlace);
            }

            // 12-oct-2015
            BeanPlace beanPlace = CUtils.getLatLongAndTimeZone(_objPlace);

            // bundle = CUtils.getBundleOfPlaceValues(_objPlace);
            bundle = CUtils.getBundleOfPlaceValues(beanPlace);

        } catch (Exception e) {
            CUtils.showSnackbar(editTxtVLatDeg, e.getMessage(), getActivity());
        }

        return bundle;

    }

    public void findCurrentLocation() {
        if (activity != null && CUtils.isLocationPermissionGranted(activity, this, CGlobalVariables.PERMISSION_LOCATION)) {
            if (isGpsEnabled()) {
                gotoSearchLocation();
            } else {
                showGpsDisabledAlert();
            }
        }
    }

    private boolean isGpsEnabled() {

        LocationManager _locationManager;
        _locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);
        return _locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    private boolean isNetWorkLocationEnabled() {
        LocationManager _locationManager;
        _locationManager = (LocationManager) activity.getSystemService(Context.LOCATION_SERVICE);

        return _locationManager
                .isProviderEnabled(LocationManager.NETWORK_PROVIDER);
    }

    private void gotoSearchLocation() {
        //progressBarForGPS.setVisibility(View.VISIBLE);
        showLogoProgressbar(loadmore_gif);
        /*editTxtVLatDeg
                .setBackgroundResource(android.R.drawable.editbox_background);
        editTxtVLatMin
                .setBackgroundResource(android.R.drawable.editbox_background);
        editTxtVLongDeg
                .setBackgroundResource(android.R.drawable.editbox_background);
        editTxtVLongMin
                .setBackgroundResource(android.R.drawable.editbox_background);*/
        MyLocation.LocationResult locationResult = new MyLocation.LocationResult() {
            @Override
            public void gotLocation(Location location) {
                if (location != null && activity != null) {
                    _long = location.getLongitude();
                    _lat = location.getLatitude();
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            showCityInformation(_lat, _long);
                        }
                    });
                }
            }
        };
        myLocation = new MyLocation();
        myLocation.getLocation(activity, locationResult);
    }

    /*
     * (non-Javadoc)
     *
     * @see com.actionbarsherlock.app.SherlockFragmentActivity#onStop()
     */
    @Override
    public void onStop() {
        super.onStop();
        if ((myLocation != null) && (myLocation.lm != null)
                && (myLocation.locationListenerGps != null)) {
            myLocation.lm.removeUpdates(myLocation.locationListenerGps);
        }
        if ((myLocation != null) && (myLocation.lm != null)
                && (myLocation.locationListenerNetwork != null)) {
            myLocation.lm.removeUpdates(myLocation.locationListenerNetwork);
        }
    }

    AlertDialog alert;

    private void showGpsDisabledAlert() {
        String enable = activity.getResources().getString(R.string.Enable_network_location_access);
        ;
        String cancel;
        /*if (((ActPlaceSearch) activity).LANGUAGE_CODE == CGlobalVariables.HINDI) {
            cancel = activity.getResources().getString(R.string.hindi_cancel);
        } else {*/
        cancel = activity.getResources().getString(R.string.cancel);
        //}

        // if(!isGPSEnabledDialogBoxAlreadyRunning) {
        //    isGPSEnabledDialogBoxAlreadyRunning = true;
        if (alert == null || !alert.isShowing()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(activity);
            builder.setMessage(
                    activity.getResources().getString(
                            R.string.GPS_is_not_enabled_in_this_phone))
                    .setCancelable(true)
                    .setPositiveButton(enable, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // show Location Access Setting screen.
                            showLocationAccessSettingActivity();
                            //                             isGPSEnabledDialogBoxAlreadyRunning = false;
                        }
                    });
            builder.setNegativeButton(cancel,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                           /* if (CUtils.isConnectedWithInternet(activity)) {
                                // check network location is enabled or not.
                                // If not then show alert and ask user to enalble
                                // network location.
                                if (isNetWorkLocationEnabled()) {
                                    gotoSearchLocation();
                                } else {
                                    showNetworkLocationDisabledAlert();
                                }
                            } else {
                                MyCustomToast mct2 = new MyCustomToast(
                                        activity, activity
                                        .getLayoutInflater(),
                                        activity, ((ActPlaceSearch) activity).regularTypeface);
                                mct2.show(activity.getResources().getString(
                                        R.string.no_internet));
                            }*/
                            ///                      isGPSEnabledDialogBoxAlreadyRunning = false;
                            dialog.cancel();
                        }
                    });
            alert = builder.create();
            alert.show();
        }

        //  }
    }

    private void showLocationAccessSettingActivity() {
        try {
            Intent gpsOptionsIntent = new Intent(
                    android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(gpsOptionsIntent);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
            CUtils.showSnackbar(editTxtVLatDeg, "Your device does not support GPS", getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showNetworkLocationDisabledAlert() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(activity);
        builder.setMessage(
                activity.getResources().getString(
                        R.string.network_location_access_is_not_enabled))
                .setCancelable(true)
                .setPositiveButton(
                        activity.getResources().getString(
                                R.string.Enable_network_location_access),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // show Location Access Setting screen.
                                showLocationAccessSettingActivity();
                            }
                        });
        builder.setNegativeButton(
                activity.getResources().getString(R.string.cancel_gps_location_search),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        android.app.AlertDialog alert = builder.create();
        alert.show();
    }

    private boolean showCityInformation(double lat, double lon) {
        int tempCalMin = 0;
        double orignal_lat = lat;
        double orignal_lon = lon;
        try {
            if (lat < 0) {
                rbGroupPlaceLat.check(R.id.rbLatSouth);
            } else {
                rbGroupPlaceLat.check(R.id.rbLatNorth);
            }
            if (lon < 0) {
                rbGroupPlaceLong.check(R.id.rbLongWest);
            } else {
                rbGroupPlaceLong.check(R.id.rbLongEast);
            }
            if (lat < 0.)
                lat *= -1;
            if (lon < 0.)
                lon *= -1;

            editTxtVLatDeg.setText(String.valueOf((int) lat));
            //editTxtVLatDeg.setBackgroundResource(R.drawable.edit_text_gps);
            tempCalMin = ((int) ((lat - (int) lat) * 60));
            if (tempCalMin < 0.)
                tempCalMin *= -1;
            editTxtVLatMin.setText(String.valueOf(tempCalMin));
            // editTxtVLatMin.setBackgroundResource(R.drawable.edit_text_gps);
            tempCalMin = 0;

            editTxtVLongDeg.setText(String.valueOf((int) lon));
            //editTxtVLongDeg.setBackgroundResource(R.drawable.edit_text_gps);
            tempCalMin = ((int) ((lon - (int) lon) * 60));
            if (tempCalMin < 0.)
                tempCalMin *= -1;
            editTxtVLongMin.setText(String.valueOf(tempCalMin));
            //editTxtVLongMin.setBackgroundResource(R.drawable.edit_text_gps);
            if (CUtils.isConnectedWithInternet(activity)) {
                /*timeZoneAsync = new TimeZoneAsync(orignal_lat, orignal_lon);
                timeZoneAsync.execute();*/
                getTimeZoneFromServer(orignal_lat, orignal_lon);
            } else {
                if (activity != null) {
                    CUtils.showSnackbar(editTxtVLatDeg, activity.getResources().getString(R.string.no_internet), getActivity());
                }
                               /*MyCustomToast mct3 = new MyCustomToast(activity,
                        activity.getLayoutInflater(),
                        activity, ((ActPlaceSearch) activity).regularTypeface);
                mct3.show(activity.getResources().getString(
                        R.string.failed_choose_timezone_yourself));*/
                //progressBarForGPS.setVisibility(View.GONE);
                hideLogoProgressbar(loadmore_gif);
                //spiTimeZone.setBackgroundResource(R.drawable.edit_text_bg);
                timeZoneStatus.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            // Toast.makeText(this, "Error>"+e.getMessage(),
            // Toast.LENGTH_SHORT).show();
        }
        return true;
    }


   /* class TimeZoneAsync extends AsyncTask<String, Long, Void> {
        double mylat = 0.0, mylong = 0.0;
        boolean success = true;
        String timezonevalue = "";

        public TimeZoneAsync(double lat, double lon) {
            mylat = lat;
            mylong = lon;
            Log.d("TimeZoneAsync", "onPreExecute");
        }

        @Override
        protected Void doInBackground(String... arg0) {
            try {
                timezonevalue = executeTimezoneHttpGet(mylat, mylong);
            } catch (Exception e) {
                success = false;
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            success = true;
            timezonevalue = "";
        }

        @Override
        protected void onPostExecute(Void result) {
            if (success)
                showTimeZone(timezonevalue);
        }
    }*/

    private void showTimeZone(String timezonevalue) {
        try {
            int timeZoneIndex = 0;
            String _timezone = timezonevalue.substring(
                    timezonevalue.indexOf("<dstOffset>")
                            + "<dstOffset>".length(),
                    timezonevalue.indexOf("</dstOffset>"));
            _timezone = _timezone.trim();
            for (int i = 0; i < arrTimeZone.length; i++) {
                if (arrTimeZone[i][1].equals(_timezone)) {
                    timeZoneIndex = i;
                    break;
                }
            }
            spiTimeZone.setSelection(timeZoneIndex);
            timeZoneStatus.setVisibility(View.VISIBLE);
            //progressBarForGPS.setVisibility(View.GONE);
            hideLogoProgressbar(loadmore_gif);

            _timezoneString = timezonevalue.substring(
                    timezonevalue.indexOf("<timezoneId>")
                            + "<timezoneId>".length(),
                    timezonevalue.indexOf("</timezoneId>"));
            _timezoneString = _timezoneString.trim();

        } catch (Exception e) {
            //progressBarForGPS.setVisibility(View.GONE);
            hideLogoProgressbar(loadmore_gif);
            if (activity != null) {
                CUtils.showSnackbar(editTxtVLatDeg, activity.getResources().getString(
                        R.string.failed_choose_timezone_yourself), getActivity());
            }

            //spiTimeZone.setBackgroundResource(R.drawable.edit_text_bg);
            timeZoneStatus.setVisibility(View.GONE);
        }
    }

    private void getTimeZoneFromServer(double lat, double lon) {
        String url = "https://secure.geonames.org/timezone?lat="
                + String.valueOf(lat) + "&lng=" + String.valueOf(lon)
                + "&username="+ com.ojassoft.astrosage.utils.CUtils.getRandomUsername();
        //LibCUtils.vollyGetRequest(activity, GPSCitySearch.this, url, GET_TIME_ZONE);
        HashMap<String, String> params = new HashMap<String, String>();
        RequestQueue queue = VolleySingleton.getInstance(activity).getRequestQueue();
        StringRequest stringRequest = new VolleyServiceHandler(Request.Method.GET, url,
                GPSCitySearch.this, true, params, GET_TIME_ZONE).getMyStringRequest();
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);

    }

   /* public String executeTimezoneHttpGet(double lat, double lon)
            throws Exception {
        BufferedReader in = null;
        String page = null;

        // String
        // _url="http://www.earthtools.org/timezone/"+String.valueOf(lat)+"/"+String.valueOf(lon);
        String _url = "?lat="
                + String.valueOf(lat) + "&lng=" + String.valueOf(lon)
                + "&username=fleshearth";
        // String
        // _url="?lat="+String.valueOf(lat)+"&lng="+String.valueOf(lon)+"&username=hukumsingh87";
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet request = new HttpGet();
            request.setURI(new URI(_url));
            HttpResponse response = client.execute(request);
            in = new BufferedReader(new InputStreamReader(response.getEntity()
                    .getContent()));
            StringBuffer sb = new StringBuffer("");
            String line = "";
            while ((line = in.readLine()) != null)
                sb.append(line);
            in.close();
            page = sb.toString();
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return page;
    }*/

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {

        this.isVisibleToUser = isVisibleToUser;
        //activity=null is use check that setUserVisibleHint is called before oncreateview.
        if (activity != null && isVisibleToUser && rootView != null) {
            // isGPSEnabledDialogBoxAlreadyRunning = false;
            findCurrentLocation();
        }

        super.setUserVisibleHint(isVisibleToUser);
    }

    /*@Override
    public void onResume() {
        super.onResume();
    }*/

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
                name.getBackground().setColorFilter(activity.getResources().getColor(R.color.colorPurple), PorterDuff.Mode.SRC_ATOP);
                value = false;
            } else if (Integer.parseInt(name.getText().toString()) < 0 || Integer.parseInt(name.getText().toString()) > 89) {
                inputLayout.setError("0-89");
                inputLayout.setErrorEnabled(true);
                requestFocus(name);
                name.getBackground().setColorFilter(activity.getResources().getColor(R.color.colorPurple), PorterDuff.Mode.SRC_ATOP);
                value = false;
            } else {
                inputLayout.setErrorEnabled(false);
                inputLayout.setError(null);
                name.getBackground().setColorFilter(activity.getResources().getColor(R.color.colorPurple), PorterDuff.Mode.SRC_ATOP);
            }
        }
        if (name == editTxtVLatMin) {
            if (name.getText().toString().trim().isEmpty()) {
                inputLayout.setError(message);
                inputLayout.setErrorEnabled(true);
                requestFocus(name);
                name.getBackground().setColorFilter(activity.getResources().getColor(R.color.colorPurple), PorterDuff.Mode.SRC_ATOP);
                value = false;
            } else if (Integer.parseInt(name.getText().toString()) < 0 || Integer.parseInt(name.getText().toString()) > 59) {
                inputLayout.setError("0-59");
                inputLayout.setErrorEnabled(true);
                requestFocus(name);
                name.getBackground().setColorFilter(activity.getResources().getColor(R.color.colorPurple), PorterDuff.Mode.SRC_ATOP);
                value = false;
            } else {
                inputLayout.setErrorEnabled(false);
                inputLayout.setError(null);
                name.getBackground().setColorFilter(activity.getResources().getColor(R.color.colorPurple), PorterDuff.Mode.SRC_ATOP);
            }
        }
        if (name == editTxtVLongDeg) {
            if (name.getText().toString().trim().isEmpty()) {
                inputLayout.setError(message);
                inputLayout.setErrorEnabled(true);
                requestFocus(name);
                name.getBackground().setColorFilter(activity.getResources().getColor(R.color.colorPurple), PorterDuff.Mode.SRC_ATOP);
                value = false;
            } else if (Integer.parseInt(name.getText().toString()) < 0 || Integer.parseInt(name.getText().toString()) > 179) {
                inputLayout.setError("0-179");
                inputLayout.setErrorEnabled(true);
                requestFocus(name);
                name.getBackground().setColorFilter(activity.getResources().getColor(R.color.colorPurple), PorterDuff.Mode.SRC_ATOP);
                value = false;
            } else {
                inputLayout.setErrorEnabled(false);
                inputLayout.setError(null);
                name.getBackground().setColorFilter(activity.getResources().getColor(R.color.colorPurple), PorterDuff.Mode.SRC_ATOP);
            }
        }
        if (name == editTxtVLongMin) {
            if (name.getText().toString().trim().isEmpty()) {
                inputLayout.setError(message);
                inputLayout.setErrorEnabled(true);
                requestFocus(name);
                name.getBackground().setColorFilter(activity.getResources().getColor(R.color.colorPurple), PorterDuff.Mode.SRC_ATOP);
                value = false;
            } else if (Integer.parseInt(name.getText().toString()) < 0 || Integer.parseInt(name.getText().toString()) > 59) {
                inputLayout.setError("0-59");
                inputLayout.setErrorEnabled(true);
                requestFocus(name);
                name.getBackground().setColorFilter(activity.getResources().getColor(R.color.colorPurple), PorterDuff.Mode.SRC_ATOP);
                value = false;
            } else {
                inputLayout.setErrorEnabled(false);
                inputLayout.setError(null);
                name.getBackground().setColorFilter(activity.getResources().getColor(R.color.colorPurple), PorterDuff.Mode.SRC_ATOP);
            }
        }
        return value;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults != null && grantResults.length > 0) {
            if (requestCode == CGlobalVariables.PERMISSION_LOCATION && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                findCurrentLocation();
            } else {
                boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissions[0]);
                if (!showRationale) {
                    CUtils.saveBooleanData(activity, CGlobalVariables.PERMISSION_KEY_LOCATION, true);
                }
            }
        }
    }

    @Override
    public void onResponse(String response, int method) {
        if (activity != null) {
            if (!TextUtils.isEmpty(response)) {
                showTimeZone(response);
            }

        }

    }

    @Override
    public void onError(VolleyError error) {
        if (activity != null) {
            if (error != null && error.getMessage() != null) {
                CUtils.showSnackbar(editTxtVLatDeg, error.getMessage(), getActivity());
            }
        }


    }

    public void showLogoProgressbar(ImageView imageView) {
        imageView.setVisibility(View.VISIBLE);
        Glide.with(getActivity().getApplicationContext()).load(R.drawable.new_ai_loader).into(imageView);
    }
    public void hideLogoProgressbar(ImageView imageView) {
        imageView.setVisibility(View.GONE);
    }

}
