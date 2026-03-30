package com.ojassoft.astrosage.misc;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.util.Log;

import com.ojassoft.astrosage.beans.BeanUserMapping;
import com.ojassoft.astrosage.utils.CUtils;

import java.util.List;
import java.util.Locale;

/**
 * Created by ojas-08 on 29/8/17.
 */
public class GetCityInBackground extends IntentService {
    public GetCityInBackground() {
        super("GetCityInBackground");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        GPSTracker gps = new GPSTracker(GetCityInBackground.this);
        // check if GPS enabled
        if (gps.canGetLocation()) {

            double latitude = gps.getLatitude();
            double longitude = gps.getLongitude();
            getCityName(latitude, longitude);
            // \n is for new line
        }/*else{
            // can't get location
            // GPS or Network is not enabled
            // Ask user to enable GPS/network in settings
            gps.showSettingsAlert();
        }*/
    }

    private String getCityName(double lat, double lng) {
        Log.i("coordinateq", "lat: " + lat + "lat: " + lng);
        String cityName = "";
        try {
            BeanUserMapping beanUserMapping = CUtils.getUserMappingData(GetCityInBackground.this);
            if (beanUserMapping == null) {
                beanUserMapping = new BeanUserMapping();
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> addresses = geocoder.getFromLocation(lat, lng, 1);
                cityName = addresses.get(0).getLocality();
                String stateName = addresses.get(0).getAdminArea();
                String countryName = addresses.get(0).getCountryName();
                beanUserMapping.setCity(cityName);
                beanUserMapping.setState(stateName);
                beanUserMapping.setCountry(countryName);
                CUtils.saveUserMappingData(GetCityInBackground.this, beanUserMapping);
            }
        } catch (Exception e) {
            Log.i("dsfv", "" + e.getMessage());
        }
        return cityName;
    }

   /* public void getAddressFromLocation(final double latitude, final double longitude) {
        Thread thread = new Thread() {
            @Override
            public void run() {
                Geocoder geocoder = new Geocoder(GetCityInBackground.this, Locale.getDefault());
                String result = null;
                try {
                    List<Address> addressList = geocoder.getFromLocation(
                            latitude, longitude, 1);
                    if (addressList != null && addressList.size() > 0) {
                        Address address = addressList.get(0);
                        StringBuilder sb = new StringBuilder();
                        for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                            sb.append(address.getAddressLine(i)).append("\n");
                        }
                        sb.append(address.getLocality()).append("\n");
                        sb.append(address.getPostalCode()).append("\n");
                        sb.append(address.getCountryName());
                        result = sb.toString();
                    }
                } catch (IOException e) {
                    Log.e("", "Unable connect to Geocoder", e);
                }
            }
        };
        thread.start();
    }*/
}
