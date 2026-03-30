package com.ojassoft.astrosage.ui.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.libojassoft.android.beans.LibOutPlace;
import com.libojassoft.android.misc.VolleyResponse;
import com.libojassoft.android.utils.LibCGlobalVariables;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanPlace;
import com.ojassoft.astrosage.jinterface.matching.ICitySearch;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.ui.act.ActPlaceSearch;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by ojas on 2/3/16.
 */
public class CitySearchFrag extends Fragment implements VolleyResponse {

    //Typeface typeface;
    // protected Map<String, String> mapCityID = new HashMap<String, String>();
    //List<String> listPlace = new ArrayList<String>();
    MyCustomToast mct;
    RecyclerView lstCity;
    EditText edtTextSearch;
    int SELECTED_MODULE;

    ICitySearch iCitySearch;
    CheckBox make_it_default_city;
    //SearchCityFromAstroSageAtlas searchCityFromAstroSageAtlas;
    //FatchCityDetailFromAstroSageAtlas feFatchCityDetailFromAstroSageAtlas;

    ProgressBar progressBar1;

    private final String prefSaveCities = "SAVE_CITIES_PREF";
    private final String keySaveCities = "SAVE_CITIES_KEY";
    private final int LOCAL_CITY_MAX_SIZE = 5;
    // private boolean isCityHasBeenSearched = false;
    private boolean needToSearchFromServer = true;
    ArrayList<LibOutPlace> placesListData;
    private String _searchText = "";
    private String ID = "id";
    private String PLACE = "place";
    private String STATE = "state";
    private String LONGITUDE = "longitude";
    private String LATITUDE = "latitude";
    private String TIMEZONE = "timezone";
    private String COUNTRY = "country";
    private String TIMEZONE_STRING = "timezonestring";
    int SEARCH_PLACE = 1;
    int CITY_DETAIL = 0;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        iCitySearch = (ICitySearch) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        /*if (searchCityFromAstroSageAtlas != null && searchCityFromAstroSageAtlas.getStatus() == AsyncTask.Status.RUNNING) {
            searchCityFromAstroSageAtlas.cancel(true);
        }*/
        iCitySearch = null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.citysearchfraglayout, container, false);
        // typeface=((ActPlaceSearch)getActivity()).typeface;
        SELECTED_MODULE = ((ActPlaceSearch) getActivity()).SELECTED_MODULE;

        setLayRef(rootView);

        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        setPrefDataIntoList();
    }

    private void setLayRef(View view) {

        mct = new MyCustomToast(getActivity(), getActivity().getLayoutInflater(), getActivity(), ((ActPlaceSearch) getActivity()).regularTypeface);
        edtTextSearch = (EditText) view.findViewById(R.id.edtTextSearch);
        lstCity = (RecyclerView) view.findViewById(R.id.lstCity);
        make_it_default_city = (CheckBox) view.findViewById(R.id.check_save_city_in_pref_1);
        make_it_default_city.setTypeface(((ActPlaceSearch) getActivity()).regularTypeface);

        progressBar1 = (ProgressBar) view.findViewById(R.id.progressBar1);
        mct = new MyCustomToast(getActivity(), getActivity().getLayoutInflater(), getActivity(), ((ActPlaceSearch) getActivity()).regularTypeface);

       /* appCompatAutoCompleteTextView = (AppCompatAutoCompleteTextView)view.findViewById(R.id.appCompatAutoCompleteTextView);

        appCompatAutoCompleteTextView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                //
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

                if(appCompatAutoCompleteTextView.getText().length() >= 3){
                    new SearchCityFromAstroSageAtlas(appCompatAutoCompleteTextView.getText().toString()).execute();
                }

            }
        });*/

        // lstCity.TextC

       /* edtTextSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isCityHasBeenSearched){
                    setPrefDataIntoList();
                }

                edtTextSearch.setFocusableInTouchMode(true);
                edtTextSearch.setFocusable(true);
            }
        });*/

       /* edtTextSearch.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {

                if(!isCityHasBeenSearched){
                    setPrefDataIntoList();
                }

                return false;
            }
        });*/

        edtTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String input = edtTextSearch.getText().toString().trim();
                if (isAsciiOnly(input)|| input.isEmpty()) {
                    if (input.length() > 3) {
                        if (placesListData != null) {
                            searchCityOffline(input);
                        } else {
                            searchCityOnline();
                        }
                    } else if (input.length() == 3) {
                        searchCityOnline();
                    }
                } else {
                    mct.show(getResources().getString(R.string.please_enter_city_name_in_english));
                }

            }
        });
    }

    // Check for any character outside the ASCII range (English)
    public static boolean isAsciiOnly(String text) {
        if (text == null || text.isEmpty()) {
            return false;
        }
        return text.matches("^[\\x00-\\x7F]*$");
    }
    private void searchCityOffline(String searchText) {

        try {

            ArrayList<LibOutPlace> tempPlacesListData = new ArrayList<>();
            for (int i = 0; i < placesListData.size(); i++) {
                if (placesListData.get(i).getName().toLowerCase().contains(searchText.toLowerCase())) {
                    tempPlacesListData.add(placesListData.get(i));
                }
            }
            if(tempPlacesListData.isEmpty()){
                mct.show(getResources().getString(R.string.city_not_found));
            }
            setHomeNavigationAdapter(tempPlacesListData, true, false);

        } catch (Exception ex) {
            //Log.i("Exception", ex.getMessage().toString());
        }

    }

    private void searchCityOnline() {

        if (CUtils.isConnectedWithInternet(getActivity())) {
            /*if (searchCityFromAstroSageAtlas != null && searchCityFromAstroSageAtlas.getStatus() == AsyncTask.Status.RUNNING) {
                searchCityFromAstroSageAtlas.cancel(true);
            }

            searchCityFromAstroSageAtlas = new SearchCityFromAstroSageAtlas(edtTextSearch.getText().toString().trim());
            searchCityFromAstroSageAtlas.execute();*/
            SearchCityFromAstroSageAtlas(edtTextSearch.getText().toString().trim());
        } else {
            mct.show(getResources().getString(R.string.no_internet));
        }

    }


    private void setHomeNavigationAdapter(ArrayList<LibOutPlace> placesList, boolean isSearchNeedFromServer, boolean isSaveData) {
        needToSearchFromServer = isSearchNeedFromServer;
        if (isSaveData) {
            this.placesListData = placesList;
        }
        CustomAdapter customAdapter = new CustomAdapter(placesList);
        lstCity.setVisibility(View.VISIBLE);
        lstCity.setAdapter(customAdapter);
        lstCity.setLayoutManager(new LinearLayoutManager(getActivity()));

        if (!_searchText.equals(edtTextSearch.getText().toString().trim()) && isSaveData) {
            _searchText = edtTextSearch.getText().toString().trim();
            searchCityOffline(_searchText);
        }
    }


    /**
     * This is a Asyn class used to search city from AstroSage Atlas
     *
     * @author Bijendra 31-may-13
     */

    /*private class SearchCityFromAstroSageAtlas extends
            AsyncTask<String, Long, Void> {

        boolean isCityFound = false;
        ArrayList<LibOutPlace> places;

        // CustomProgressDialog pd = null;

        public SearchCityFromAstroSageAtlas(String searchText) {
            _searchText = searchText;
        }

        @Override
        protected Void doInBackground(String... arg0) {
            try {

                places = new CAstroSageAtlas().searchPlace(_searchText);
                isCityFound = true;
            } catch (Exception e) {
                isCityFound = false;

            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            // pd = new CustomProgressDialog(getActivity(), typeface);
            // pd.show();
            progressBar1.setVisibility(View.VISIBLE);

        }

        @Override
        protected void onPostExecute(Void result) {

            try {
                try {
                   *//* if (pd != null & pd.isShowing())
                        pd.dismiss();*//*
                    progressBar1.setVisibility(View.GONE);
                } catch (Exception e) {
                    //Log.i("Exception", e.getMessage().toString());
                }

                if (isCityFound & places != null) {
                    // isCityHasBeenSearched = true;
                    setHomeNavigationAdapter(places, true, true);
                } else {
                    mct.show(getResources().getString(R.string.city_not_found));
                }
            } catch (Exception ex) {
                //Log.i("Exception", ex.getMessage().toString());
            }
        }

    }*/

    private class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

        ArrayList<LibOutPlace> places;

        CustomAdapter(ArrayList<LibOutPlace> places) {
            this.places = places;
        }

        @Override
        public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

            View view = getActivity().getLayoutInflater().inflate(R.layout.citysearchfrag_customlist, parent, false);
            MyViewHolder myViewHolder = new MyViewHolder(view);

            return myViewHolder;
        }

        @Override
        public void onBindViewHolder(MyViewHolder holder, int position) {
            LibOutPlace libOutPlace = places.get(position);
            holder.text1.setText(libOutPlace.getName().trim() + ", "
                    + libOutPlace.getState().trim());

            holder.text2.setText(libOutPlace.getCountry());
        }

        @Override
        public int getItemCount() {
            return places.size();
        }

        public class MyViewHolder extends RecyclerView.ViewHolder {

            TextView text1;
            TextView text2;

            public MyViewHolder(View itemView) {
                super(itemView);

                text1 = (TextView) itemView.findViewById(R.id.text1);
                text2 = (TextView) itemView.findViewById(R.id.text2);

                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        int position = getLayoutPosition();
                        //setPlaceDetailSearchedFromAstroSageAtlas(places.get(position));
                        LibOutPlace libOutPlace = places.get(position);
                        goToSelecetdCity(libOutPlace.getId());
                    }
                });

            }
        }
    }

    /**
     * This function is used to fill values of city in place object and return
     * to main screen
     *
     * @param itemPositionCity
     * @author Bijendra 31-may-13
     */

    private void goToSelecetdCity(final String itemPositionCity) {

        if (needToSearchFromServer) {
            if (CUtils.isConnectedWithInternet(getActivity())) {
                /*if (feFatchCityDetailFromAstroSageAtlas != null && feFatchCityDetailFromAstroSageAtlas.getStatus() == AsyncTask.Status.RUNNING) {
                    feFatchCityDetailFromAstroSageAtlas.cancel(true);
                }

                feFatchCityDetailFromAstroSageAtlas = new FatchCityDetailFromAstroSageAtlas(String.valueOf(itemPositionCity));
                feFatchCityDetailFromAstroSageAtlas.execute();*/
                fetchCityDetailFromAstroSageAtlas(itemPositionCity);
            } else {
                mct.show(getResources().getString(R.string.no_internet));
            }
        } else {

            setPlaceDetailSearchedFromAstroSageAtlas(getPlaceData(itemPositionCity));
        }

    }


    /**
     * This is a Asyn class used to get city detail from AstroSage Atlas
     *
     * @author Bijendra 31-may-13
     */
   /* private class FatchCityDetailFromAstroSageAtlas extends
            AsyncTask<String, Long, Void> {

        String _cityId = "";
        boolean isCityFound = false;
        LibOutPlace place = null;
        CustomProgressDialog pd = null;

        FatchCityDetailFromAstroSageAtlas(String cityId) {
            _cityId = cityId;

        }

        @Override
        protected Void doInBackground(String... arg0) {
            // TODO Auto-generated method stub
            try {
                place = new CAstroSageAtlas().getPlaceDetail(_cityId);
                isCityFound = true;
            } catch (Exception e) {

            }

            return null;
        }

        @Override
        protected void onPreExecute() {
            pd = new CustomProgressDialog(getActivity(), ((ActPlaceSearch) getActivity()).regularTypeface);
            pd.show();
            //progressBar1.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void result) {

            try {
                try {
                    if (pd != null & pd.isShowing())
                        pd.dismiss();
                    // progressBar1.setVisibility(View.GONE);
                } catch (Exception e) {

                }

                if (isCityFound & place != null) {
                    setPlaceDetailSearchedFromAstroSageAtlas(place);
                } else {
                    mct.show(getResources().getString(R.string.city_not_found));
                }
            } catch (Exception ex) {
                //Log.i("Exception ", ex.getMessage().toString());
            }

        }

    }*/

    /**
     * @param key
     * @return
     * @autor Amit RAutela
     * @date : 28-march-2016
     * @desc : This method is used to get the LibOutPlace object from key
     */
    private LibOutPlace getPlaceData(String key) {

        LibOutPlace places = null;

        try {
            SharedPreferences prefs = getActivity().getSharedPreferences(prefSaveCities, Context.MODE_PRIVATE);
            String storedHashMapString = prefs.getString(keySaveCities, "noDataFound");

            if (!storedHashMapString.equals("noDataFound")) {

                java.lang.reflect.Type type = new TypeToken<ArrayList<LibOutPlace>>() {
                }.getType();
                Gson gson = new Gson();
                ArrayList<LibOutPlace> arrayList = (ArrayList) gson.fromJson(storedHashMapString, type);

                for (int i = 0; i < arrayList.size(); i++) {
                    if (arrayList.get(i).getId().equals(key)) {
                        places = arrayList.get(i);
                        break;
                    }
                }

                //  places = mHashMap.get(key);
            }
        } catch (Exception ex) {
            //Log.i("Exception :- ", ex.getMessage().toString());
        }

        return places;
    }

    /**
     * @autor Amit RAutela
     * @date : 28-march-2016
     * @desc : This method is used to convert city pref data into ArrayList
     */
    private void setPrefDataIntoList() {

        try {

            SharedPreferences prefs = getActivity().getSharedPreferences(prefSaveCities, Context.MODE_PRIVATE);
            String storedHashMapString = prefs.getString(keySaveCities, "noDataFound");

            if (!storedHashMapString.equals("noDataFound")) {

                java.lang.reflect.Type type = new TypeToken<List<LibOutPlace>>() {
                }.getType();
                Gson gson = new Gson();
                ArrayList<LibOutPlace> arrayList = (ArrayList) gson.fromJson(storedHashMapString, type);

                //ArrayList<LibOutPlace> places= new ArrayList<LibOutPlace>(mHashMap.values());
                Collections.reverse(arrayList);

                setHomeNavigationAdapter(arrayList, false, true);
            }

        } catch (Exception ex) {
            //Log.i("Exception : ", ex.getMessage().toString());
        }
    }

    /**
     * @param place
     * @autor Amit RAutela
     * @date : 28-march-2016
     * @desc : This method is used to save city data in local preferences
     */
    private void saveSearchDataInSharedPreference(LibOutPlace place) {

        try {
            SharedPreferences prefs = getActivity().getSharedPreferences(prefSaveCities, Context.MODE_PRIVATE);
            String storedHashMapString = prefs.getString(keySaveCities, "noDataFound");

            //create hashmap
            // HashMap<String, LibOutPlace> mHashMap = null;
            List<LibOutPlace> listObj = null;
            Gson gson = new Gson();

            if (storedHashMapString.equals("noDataFound")) {

                listObj = new ArrayList<LibOutPlace>();
                //  mHashMap.put(place.getId(), place);

            } else {

                //get from shared prefs
                java.lang.reflect.Type type = new TypeToken<List<LibOutPlace>>() {
                }.getType();
                listObj = gson.fromJson(storedHashMapString, type);

                try {
                    //Getting current city id
                    String currentCityId = place.getId();
                    //Checking if current id already exists
                    for (int i = 0; i < listObj.size(); i++) {
                        String saveCityId = listObj.get(i).getId();
                        if (saveCityId.trim().equals(currentCityId.trim())) {
                            listObj.remove(i);
                            break;
                        }
                    }
                } catch (Exception ex) {
                    //Log.i("Exception - ", ex.getMessage());
                }

                if (listObj.size() >= LOCAL_CITY_MAX_SIZE) {
                    listObj.remove(0);
                    // mHashMap.remove(key);
                }/*else{
                    mHashMap.put(place.getId(), place);
                }*/

            }

            //Inserting data
            listObj.add(place);

            //convert to string using gson
            String hashMapString = gson.toJson(listObj);

            //save in shared prefs
            prefs.edit().putString(keySaveCities, hashMapString).apply();


        } catch (Exception ex) {
            //Log.i("Exception - ", ex.getMessage());
        }

    }

    /**
     * This function set the place detail get from server into Place object and
     * return to main screen
     *
     * @param place
     * @author Bijendra 31-may-13
     */
    private void setPlaceDetailSearchedFromAstroSageAtlas(LibOutPlace place) {

        //Do not need to save or remove data, if comes from local click.
        if (needToSearchFromServer) {
            saveSearchDataInSharedPreference(place);
        }

        BeanPlace _objPlace = new BeanPlace();
        int tempCalMin = 0;
        String _latDeg = "", _latMin = "", _latDir = "", _lonDeg = "", _lonMin = "", _lonDir, _tzname;
        float fLat, fLOng, fTz;
        try {
            fLat = Float.valueOf(place.getLatitude().trim());
            fLOng = Float.valueOf(place.getLongitude().trim());
            fTz = Float.valueOf(place.getTimezone().trim());

            // LATITUDE DEGREE,MINUTE AND DIRECTION
            if (fLat < 0)
                _latDeg = String.valueOf((int) (fLat * -1));
            else
                _latDeg = String.valueOf((int) fLat);
            // END

            tempCalMin = ((int) ((fLat - (int) fLat) * 60));
            if (tempCalMin < 0.)
                tempCalMin *= -1;
            _latMin = String.valueOf(tempCalMin);

            tempCalMin = 0;
            // _latDir = (lat < 0) ? "S" : "N";
            _latDir = (fLat < 0) ? "S" : "N";
            // END

            // LONGITUDE DEGREE,MINUTE AND DIRECTION
            if (fLOng < 0)
                _lonDeg = String.valueOf((int) (fLOng) * -1);
            else
                _lonDeg = String.valueOf((int) fLOng);
            // END
            tempCalMin = ((int) ((fLOng - (int) fLOng) * 60));
            if (tempCalMin < 0.)
                tempCalMin *= -1;
            _lonMin = String.valueOf(tempCalMin);
            _lonDir = (fLOng < 0) ? "W" : "E";
            // END
            if (fTz < 0)
                _tzname = "GMT" + String.valueOf(fTz);
            else
                _tzname = "GMT+" + String.valueOf(fTz);

            // SET VALUES IN THE PLACE OBJECT
            // Edited On 22-Sep-2015
            //MODULE_ASTROSAGE_DASHBOARD ADDED by ankit on 15-5-2019
            if (SELECTED_MODULE == CGlobalVariables.MODULE_ASTROSAGE_PANCHANG
                    || SELECTED_MODULE == CGlobalVariables.MODULE_ASTROSAGE_HORA
                    || SELECTED_MODULE == CGlobalVariables.MODULE_ASTROSAGE_CHOGADIA
                    || SELECTED_MODULE == CGlobalVariables.MODULE_ASTROSAGE_YEARLY_VART
                    || SELECTED_MODULE == CGlobalVariables.MODULE_ASTROSAGE_HINDU_CALENDER
                    || SELECTED_MODULE == CGlobalVariables.MODULE_ASTROSAGE_MONTH_VIEW
                    || SELECTED_MODULE == CGlobalVariables.MODULE_ASTROSAGE_INDIAN_CALENDAR
                    || SELECTED_MODULE == CGlobalVariables.MODULE_ASTROSAGE_DASHBOARD
                    || SELECTED_MODULE == CGlobalVariables.MODULE_PANCHAK
                    || SELECTED_MODULE == CGlobalVariables.MODULE_BHADRA
                    || SELECTED_MODULE == CGlobalVariables.MODULE_MUHURAT
                    || SELECTED_MODULE == CGlobalVariables.MODULE_LAGNA) {
                _objPlace.setCityId(Integer.valueOf(place.getId()));//cityId
            } else {
                _objPlace.setCityId(-1);
            }
            _objPlace.setLatitude(String.valueOf(fLat));
            _objPlace.setLongitude(String.valueOf(fLOng));

            _objPlace.setState(place.getState());
            _objPlace.setCountryName(place.getCountry());

            _objPlace.setCountryId(-1);
            _objPlace.setTimeZoneId(-1);

            _objPlace.setCityName(place.getName());
            _objPlace.setLongDeg(_lonDeg);
            _objPlace.setLongMin(_lonMin);
            _objPlace.setLongDir(_lonDir);

            _objPlace.setLatDeg(_latDeg);
            _objPlace.setLatMin(_latMin);
            _objPlace.setLatDir(_latDir);

            if (place.getCountry().trim().equalsIgnoreCase("Nepal")) {

                if (CUtils.getNewKundliSelectedDate() <= 1985) {
                    _tzname = "GMT+5.5";
                    fTz = Float.parseFloat("5.5");
                } else {
                    _tzname = "GMT+5.75";
                    fTz = Float.parseFloat("5.75");
                }

            }
            if (place.getCountry().trim().equalsIgnoreCase("Suriname")) {
                if (CUtils.getNewKundliSelectedDate() <= 1984) {
                    if (CUtils.getNewKundliSelectedDate() == 1984 &&
                            CUtils.getNewKundliSelectedMonth() > 9) {
                        _tzname = "GMT-3.0";
                        fTz = Float.parseFloat("-3.0");
                    } else {
                        _tzname = "GMT-3.5";
                        fTz = Float.parseFloat("-3.5");
                    }
                } else {
                    _tzname = "GMT-3.0";
                    fTz = Float.parseFloat("-3.0");
                }
            }

            _objPlace.setTimeZoneName(_tzname);
            _objPlace.setTimeZoneValue(fTz);
            _objPlace.setTimeZone(String.valueOf(fTz));
            _objPlace.setTimeZoneString(place.getTimeZoneString());
            _objPlace.setTimeZoneId(-1);

            if (make_it_default_city.isChecked()) {
                //_objPlace.setDefaultCity(true);
                CUtils.saveCityAsDefaultCity(getActivity(), _objPlace);
                //CUtils.saveCityAsDefaultCityForVrat(getActivity(), _objPlace);
                // CUtils.saveUsersCheckedDefaultCityForVrat(getActivity(), true);
            }


            Bundle bundle = CUtils.getBundleOfPlaceValues(_objPlace);
            iCitySearch.goToPlaceSearchCustomOk(bundle);

        } catch (Exception e) {

        }
    }

    @Override
    public void onStop() {
        super.onStop();

       /* if (searchCityFromAstroSageAtlas != null && searchCityFromAstroSageAtlas.getStatus() == AsyncTask.Status.RUNNING) {
            searchCityFromAstroSageAtlas.cancel(true);
        }*/

        /*if (feFatchCityDetailFromAstroSageAtlas != null && feFatchCityDetailFromAstroSageAtlas.getStatus() == AsyncTask.Status.RUNNING) {
            feFatchCityDetailFromAstroSageAtlas.cancel(true);
        }*/
    }

    private void SearchCityFromAstroSageAtlas(String placeName) {
        if (progressBar1 != null) {
            progressBar1.setVisibility(View.VISIBLE);
        }
        if (placeName.contains(" ")) {
            placeName = placeName.replaceAll(" ", "%20");
        }
        String url = LibCGlobalVariables.ASTROSAGE_ATLAS_PLACE_SEARCH + placeName;
        CUtils.vollyGetRequest(CitySearchFrag.this, url, SEARCH_PLACE);
    }

    private void fetchCityDetailFromAstroSageAtlas(String placeId) {
        showProgressBar();
        String url = LibCGlobalVariables.ASTROSAGEE_ATLAS_GET_PLACE_DETAIL + placeId;
        CUtils.vollyGetRequest(CitySearchFrag.this, url, CITY_DETAIL);
    }

    @Override
    public void onResponse(String response, int method) {
        try {
            Activity activity = getActivity();
            if (activity != null) {
                if (method == CITY_DETAIL) {
                    hideProgressBar();
                    if (response != null && response.trim().length() > 5) {
                        LibOutPlace place = getPlaceDetailFromJSON(response);
                        if (place != null) {
                            setPlaceDetailSearchedFromAstroSageAtlas(place);
                        } else {
                            mct.show(getResources().getString(R.string.city_not_found));
                        }
                    } else {
                        mct.show(getResources().getString(R.string.city_not_found));
                    }
                } else if (method == SEARCH_PLACE) {
                    if (progressBar1 != null) {
                        progressBar1.setVisibility(View.GONE);
                    }
                    if (response != null && response.trim().length() > 5) {
                        ArrayList<LibOutPlace> places = getSearchedPlaceListFromJSON(response);
                        if (places != null) {
                            setHomeNavigationAdapter(places, true, true);
                        } else {
                            mct.show(getResources().getString(R.string.city_not_found));
                        }
                    } else {
                        mct.show(getResources().getString(R.string.city_not_found));
                    }
                }
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onError(VolleyError error) {
        Activity activity = getActivity();
        if (activity != null && isAdded()) {
            hideProgressBar();
            if (progressBar1 != null) {
                progressBar1.setVisibility(View.GONE);
            }
            if (error != null && error.getMessage() != null) {
                mct.show(error.getMessage());
            }
        }
    }

    CustomProgressDialog pd;

    /**
     * show Progress Bar
     */
    private void showProgressBar() {
        try {
            if (pd == null) {
                pd = new CustomProgressDialog(getActivity(), ((ActPlaceSearch) getActivity()).regularTypeface);
            }
            pd.setCanceledOnTouchOutside(false);
            if (!pd.isShowing()) {
                pd.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * hide Progress Bar
     */
    private void hideProgressBar() {
        try {
            if (pd != null & pd.isShowing())
                pd.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * This function convert string into JSON objects and return object of place
     *
     * @param data
     * @return LibOutPlace
     * @author Bijendra 31-may-13
     */
    private LibOutPlace getPlaceDetailFromJSON(String data) {

        LibOutPlace placeDetail = null;

        try {
            JSONArray array = new JSONArray(data);
            if (array.length() > 0) {
                JSONObject obj = array.getJSONObject(0);
                if (obj != null) {
                    placeDetail = new LibOutPlace();
                    placeDetail.setId(obj.getString(ID));
                    placeDetail.setName(obj.getString(PLACE));
                    placeDetail.setState(obj.getString(STATE));
                    placeDetail.setCountry(obj.getString(COUNTRY));
                    placeDetail.setLatitude(obj.getString(LATITUDE));
                    placeDetail.setLongitude(obj.getString(LONGITUDE));
                    placeDetail.setTimezone(obj.getString(TIMEZONE));
                    placeDetail.setTimeZoneString(obj.getString(TIMEZONE_STRING));

                }
            }
        } catch (Exception e) {

        }
        return placeDetail;

    }

    private ArrayList<LibOutPlace> getSearchedPlaceListFromJSON(String data) {
        ArrayList<LibOutPlace> places = null;

        try {
            JSONArray arr = new JSONArray(data);
            if (arr.length() > 0) {
                places = new ArrayList<LibOutPlace>();
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);
                    LibOutPlace pl = new LibOutPlace();
                    pl.setId(obj.getString(ID));
                    pl.setName(obj.getString(PLACE));
                    pl.setState(obj.getString(STATE));
                    pl.setCountry(obj.getString(COUNTRY));
                    places.add(pl);
                }
            }
        } catch (Exception e) {
            places = null;
        }
        return places;
    }
}