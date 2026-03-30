package com.ojassoft.astrosage.ui.act;

import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;

import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.analytics.tracking.android.EasyTracker;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanPlace;
import com.ojassoft.astrosage.customadapters.ViewPagerAdapter;
import com.ojassoft.astrosage.jinterface.matching.ICitySearch;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.ui.customcontrols.MyLocation;
import com.ojassoft.astrosage.ui.fragments.CitySearchFrag;
import com.ojassoft.astrosage.ui.fragments.CustomCitySearch;
import com.ojassoft.astrosage.ui.fragments.GPSCitySearch;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.actionbarsherlock.app.SherlockFragmentActivity;
//import com.actionbarsherlock.internal.widget.IcsSpinner;

/**
 * This activity is used for place serach(offline/online)
 * 
 * @author Ojas Softech Pvt Ltd
 * @copyrigh (c) Ojas Softech Pvt Ltd 2014.
 * @version 1.0.0
 * @date 18 feb 2014
 * 
 */
public class ActPlaceSearch extends BaseInputActivity implements ICitySearch{
	/**
	 * Local Variables
	 */
	protected Map<String, String> mapCityID = new HashMap<String, String>();
	double _lat = 0, _long = 0;
	protected SQLiteDatabase sQLiteDatabase = null;
	EditText editTextSearchCity = null, editTxtVLongDeg = null,
			editTxtVLongMin = null, editTxtVLatMin = null,
			editTxtVLatDeg = null;
	List<String> listPlace = new ArrayList<String>();
	protected ListView cityList;
	private boolean isOnlineSearched = false;
	private String[][] arrTimeZone = null;
	private Spinner spiTimeZone;
	ScrollView scrollViewPlaceCustom;
	boolean isCutomLatNorth = true, isCutomLongEast = true;
	RadioGroup rbGroupPlaceLat, rbGroupPlaceLong;
	//public Typeface typeface = Typeface.DEFAULT;
	public int SELECTED_MODULE;
	public int LANGUAGE_CODE;
	CheckBox check_goOnline, make_it_default_city, make_it_default_city2;
	MyCustomToast mct;
	ProgressBar progressBarForGPS;
	String cityName = "Manual_Lat_Long";
	ImageView timeZoneStatus;
	MyLocation myLocation;
	public boolean activityRestarted = false;
	public BeanPlace customCityValue = null;
	RadioGroup radioGroup;
	public final int LOCATION_SETTING_SCREEN_REQUEST_CODE = 2020;
	LinearLayout linearcheckbox;
	private TextView tvToolBarTitle;
	private Toolbar toolBar_search_place;
	int cityId;
	//SearchCityFromAstroSageAtlas searchCityFromAstroSageAtlas;
	ImageView ivToggleImage;
	ViewPager mViewPager;
	private TabLayout tabLayout;
	String [] titles = new String[3];

	GPSCitySearch gpsCitySearch;

	public ActPlaceSearch() {
		super(R.string.app_name);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		final LinearLayout llPlaceSearch;
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lay_place_search);
		toolBar_search_place = (Toolbar) findViewById(R.id.tool_barAppModule);
		tvToolBarTitle = (TextView) toolBar_search_place
				.findViewById(R.id.tvTitle);
		ivToggleImage = (ImageView)findViewById(R.id.ivToggleImage);
		ivToggleImage.setVisibility(View.GONE);
		// Get the navigation icon drawable
		Drawable navIcon = ContextCompat.getDrawable(this, R.drawable.ic_back_arrow);

// Check if the drawable is not null
		if (navIcon != null) {
			// Tint the drawable with the desired color
			navIcon.setTint(ContextCompat.getColor(this, R.color.black));

			// Set the tinted drawable as the navigation icon
			toolBar_search_place.setNavigationIcon(navIcon);
		}

		setSupportActionBar(toolBar_search_place);

		ActionBar actionBar = getSupportActionBar();
		actionBar.setDisplayHomeAsUpEnabled(true);
		actionBar.setDisplayShowTitleEnabled(false);

		SELECTED_MODULE = getIntent()
				.getIntExtra(CGlobalVariables.MODULE_TYPE_KEY,
						CGlobalVariables.MODULE_BASIC);
		// LANGUAGE_CODE = CUtils.getLanguageCodeFromPreference(this);
		LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication())
				.getLanguageCode();// ADDED BY HEVENDRA ON 24-12-2014
		customCityValue = (BeanPlace) getIntent().getSerializableExtra(
				CGlobalVariables.PLACE_BEAN_KEY);

		//typeface = CUtils.getUserSelectedLanguageFontType(this, LANGUAGE_CODE);

		/*llPlaceSearch = (LinearLayout) findViewById(R.id.llPlaceSearch);
		scrollViewPlaceCustom = (ScrollView) findViewById(R.id.scrollViewPlaceCustom);
		editTextSearchCity = (EditText) findViewById(R.id.searchText);
		editTxtVLongDeg = (EditText) findViewById(R.id.editTxtVLongDeg);
		editTxtVLongMin = (EditText) findViewById(R.id.editTxtVLongMin);
		editTxtVLatMin = (EditText) findViewById(R.id.editTxtVLatMin);
		editTxtVLatDeg = (EditText) findViewById(R.id.editTxtVLatDeg);
		cityList = (ListView) findViewById(R.id.lstCity);

		cityList.setFastScrollEnabled(true);
		cityList.setScrollbarFadingEnabled(false);
		cityList.setVerticalScrollBarEnabled(true);
		cityList.setVerticalFadingEdgeEnabled(false);

		spiTimeZone = (Spinner) findViewById(R.id.spiTimeZone);
		timeZoneStatus = (ImageView) findViewById(R.id.timeZoneStatus);
		progressBarForGPS = (ProgressBar) findViewById(R.id.progressBarForGPS);

		check_goOnline = (CheckBox) findViewById(R.id.check_goOnline);
		check_goOnline.setTypeface(typeface);
		check_goOnline.setChecked(true); // ADDED BY HEVENDRA ON 12-01-2015
		make_it_default_city = (CheckBox) findViewById(R.id.check_save_city_in_pref);
		make_it_default_city.setTypeface(typeface);
		make_it_default_city2 = (CheckBox) findViewById(R.id.check_save_city_in_pref_2);
		make_it_default_city2.setTypeface(typeface);

		mct = new MyCustomToast(this, getLayoutInflater(), this, typeface);

		radioGroup = (RadioGroup) findViewById(R.id.rbGroupPlaceSearch);
		linearcheckbox = (LinearLayout) findViewById(R.id.linearcheckbox);
		if (LANGUAGE_CODE == 2 || LANGUAGE_CODE == 4) {
			radioGroup.setOrientation(LinearLayout.VERTICAL);

			linearcheckbox.setOrientation(LinearLayout.VERTICAL);

		} else {
			// check_goOnline.setPadding(left, top, right, bottom);

		}
		radioGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {

				switch (checkedId) {
				case R.id.rbPlaceSearch:
					llPlaceSearch.setVisibility(View.VISIBLE);
					scrollViewPlaceCustom.setVisibility(View.GONE);

					break;
				case R.id.rbPlaceCustom:
					setCustomeCityDetail();
					llPlaceSearch.setVisibility(View.GONE);
					scrollViewPlaceCustom.setVisibility(View.VISIBLE);
					cityName = "Manual_Lat_Long";
					editTxtVLatDeg
							.setBackgroundResource(android.R.drawable.editbox_background);
					editTxtVLatMin
							.setBackgroundResource(android.R.drawable.editbox_background);
					editTxtVLongDeg
							.setBackgroundResource(android.R.drawable.editbox_background);
					editTxtVLongMin
							.setBackgroundResource(android.R.drawable.editbox_background);
					timeZoneStatus.setVisibility(View.GONE);
					progressBarForGPS.setVisibility(View.GONE);
					break;
				case R.id.rbPlaceGps:
					llPlaceSearch.setVisibility(View.GONE);
					scrollViewPlaceCustom.setVisibility(View.VISIBLE);
					cityName = "Current Location";
					findCurrentLocation();
					break;
				}

			}
		});
		cityList.setOnItemClickListener(new OnItemClickListener() {

			public void onItemClick(AdapterView<?> parent, View v,
					int position, long id) {
				goToSelecetdCity(position);
			}
		});

		rbGroupPlaceLat = (RadioGroup) findViewById(R.id.rbGroupPlaceLat);
		rbGroupPlaceLat
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

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

		rbGroupPlaceLong = (RadioGroup) findViewById(R.id.rbGroupPlaceLong);
		rbGroupPlaceLong
				.setOnCheckedChangeListener(new OnCheckedChangeListener() {

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
				});*/
		//setCustomFont(typeface);


		gpsCitySearch = new GPSCitySearch();

		mViewPager = (ViewPager) findViewById(R.id.viewpager);
		tabLayout = (TabLayout) findViewById(R.id.tabs);
		//tabLayout.setTabMode(TabLayout.MODE_FIXED);
		setupViewPager();
	}

	/**
	 * @created by : Amit Rautela
	 * @date : 02-3-2016
	 * @Description : This method is used to set View Pager- page change
	 */
	private void setupViewPager() {

		titles[0] = getResources().getString(R.string.city_search);
		titles[1] = getResources().getString(R.string.custom_city);
		titles[2] = getResources().getString(R.string.gps);

		Bundle bundle = new Bundle();
		bundle.putSerializable("customCityValue", customCityValue);

		final ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(),ActPlaceSearch.this);
		adapter.addFragment(new CitySearchFrag(),titles[0]);
		adapter.addFragment(new CustomCitySearch(), titles[1]);
		adapter.addFragment(gpsCitySearch, titles[2]);

		mViewPager.setAdapter(adapter);

		mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

			@Override
			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
				if(tabLayout != null && adapter!=null) {
					adapter.setAlpha(position,tabLayout);
				}
			}

			@Override
			public void onPageSelected(int position) {

			}

			@Override
			public void onPageScrollStateChanged(int state) {

			}
		});

		tabLayout.setupWithViewPager(mViewPager);

		// Iterate over all tabs and set the custom view
		for (int i = 0; i < tabLayout.getTabCount(); i++) {
			TabLayout.Tab tab = tabLayout.getTabAt(i);
			tab.setCustomView(adapter.getTabView(i));
		}

		adapter.setAlpha(0,tabLayout);
	}

	/*protected void setCustomeCityDetail() {
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

	}*/

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onStart()
	 */
	@Override
	protected void onStart() {
		super.onStart();
		//EasyTracker.getInstance().activityStart(this);
	}

	/*protected void findCurrentLocation() {

		if (isGpsEnabled()) {
			gotoSearchLocation();
		} else {
			showGpsDisabledAlert();
		}
	}*/

	/*private void gotoSearchLocation() {
		progressBarForGPS.setVisibility(View.VISIBLE);
		editTxtVLatDeg
				.setBackgroundResource(android.R.drawable.editbox_background);
		editTxtVLatMin
				.setBackgroundResource(android.R.drawable.editbox_background);
		editTxtVLongDeg
				.setBackgroundResource(android.R.drawable.editbox_background);
		editTxtVLongMin
				.setBackgroundResource(android.R.drawable.editbox_background);
		LocationResult locationResult = new LocationResult() {
			@Override
			public void gotLocation(Location location) {
				if (location != null) {
					_long = location.getLongitude();
					_lat = location.getLatitude();
					runOnUiThread(new Runnable() {

						@Override
						public void run() {
							showCityInformation(_lat, _long);
						}
					});
				}
			}
		};
		myLocation = new MyLocation();
		myLocation.getLocation(this, locationResult);
	}*/

	/*private boolean isGpsEnabled() {
		LocationManager _locationManager;
		_locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		return _locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
	}

	private boolean isNetWorkLocationEnabled() {
		LocationManager _locationManager;
		_locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		return _locationManager
				.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
	}*/

	/*private void showGpsDisabledAlert() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(
				getResources().getString(
						R.string.GPS_is_not_enabled_in_this_phone))
				.setCancelable(true)
				.setPositiveButton(
						getResources().getString(R.string.Enable_GPS),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// show Location Access Setting screen.
								showLocationAccessSettingActivity();
							}
						});
		builder.setNegativeButton(getResources()
				.getString(R.string.use_network),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						if (CUtils.isConnectedWithInternet(ActPlaceSearch.this)) {
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
									ActPlaceSearch.this, ActPlaceSearch.this
											.getLayoutInflater(),
									ActPlaceSearch.this, typeface);
							mct2.show(getResources().getString(
									R.string.no_internet));
						}
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}*/

	private void showNetworkLocationDisabledAlert() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setMessage(
				getResources().getString(
						R.string.network_location_access_is_not_enabled))
				.setCancelable(true)
				.setPositiveButton(
						getResources().getString(
								R.string.Enable_network_location_access),
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								// show Location Access Setting screen.
								showLocationAccessSettingActivity();
							}
						});
		builder.setNegativeButton(
				getResources().getString(R.string.cancel_gps_location_search),
				new DialogInterface.OnClickListener() {
					public void onClick(DialogInterface dialog, int id) {
						dialog.cancel();
					}
				});
		AlertDialog alert = builder.create();
		alert.show();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == android.R.id.home)
			this.finish();

		return super.onOptionsItemSelected(item);
	}

	private void showLocationAccessSettingActivity() {
		try {
			Intent gpsOptionsIntent = new Intent(
					android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
			startActivity(gpsOptionsIntent);
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
			Toast.makeText(ActPlaceSearch.this,
					"Your device does not support GPS", Toast.LENGTH_LONG)
					.show();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void goToPlaceCustomOk(Bundle bundle) {
		Intent intent = new Intent();
		intent.putExtras(bundle);
		setResult(RESULT_OK, intent);
		this.finish();
	}

	@Override
	public void goToPlaceSearchCustomOk(Bundle bundle) {
		Intent intent = new Intent();
		bundle.putString("CityID", "-1");
		intent.putExtras(bundle);
		setResult(RESULT_OK, intent);
		this.finish();
	}

	/*private boolean showCityInformation(double lat, double lon) {
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
			editTxtVLatDeg.setBackgroundResource(R.drawable.edit_text_gps);
			tempCalMin = ((int) ((lat - (int) lat) * 60));
			if (tempCalMin < 0.)
				tempCalMin *= -1;
			editTxtVLatMin.setText(String.valueOf(tempCalMin));
			editTxtVLatMin.setBackgroundResource(R.drawable.edit_text_gps);
			tempCalMin = 0;

			editTxtVLongDeg.setText(String.valueOf((int) lon));
			editTxtVLongDeg.setBackgroundResource(R.drawable.edit_text_gps);
			tempCalMin = ((int) ((lon - (int) lon) * 60));
			if (tempCalMin < 0.)
				tempCalMin *= -1;
			editTxtVLongMin.setText(String.valueOf(tempCalMin));
			editTxtVLongMin.setBackgroundResource(R.drawable.edit_text_gps);
			if (CUtils.isConnectedWithInternet(getApplicationContext())) {
				new TimeZoneAsync(orignal_lat, orignal_lon).execute();
			} else {
				MyCustomToast mct2 = new MyCustomToast(ActPlaceSearch.this,
						ActPlaceSearch.this.getLayoutInflater(),
						ActPlaceSearch.this, typeface);
				mct2.show(getResources().getString(R.string.no_internet));
				MyCustomToast mct3 = new MyCustomToast(ActPlaceSearch.this,
						ActPlaceSearch.this.getLayoutInflater(),
						ActPlaceSearch.this, typeface);
				mct3.show(getResources().getString(
						R.string.failed_choose_timezone_yourself));
				progressBarForGPS.setVisibility(View.GONE);
				spiTimeZone.setBackgroundResource(R.drawable.edit_text_bg);
				timeZoneStatus.setVisibility(View.GONE);
			}
		} catch (Exception e) {
			// Toast.makeText(this, "Error>"+e.getMessage(),
			// Toast.LENGTH_SHORT).show();
		}
		return true;
	}*/

	/*class TimeZoneAsync extends AsyncTask<String, Long, Void> {
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
	}

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
			progressBarForGPS.setVisibility(View.GONE);
		} catch (Exception e) {
			progressBarForGPS.setVisibility(View.GONE);
			MyCustomToast mct2 = new MyCustomToast(ActPlaceSearch.this,
					ActPlaceSearch.this.getLayoutInflater(),
					ActPlaceSearch.this, typeface);
			mct2.show(getResources().getString(
					R.string.failed_choose_timezone_yourself));
			spiTimeZone.setBackgroundResource(R.drawable.edit_text_bg);
			timeZoneStatus.setVisibility(View.GONE);
		}
	}

	public String executeTimezoneHttpGet(double lat, double lon)
			throws Exception {
		BufferedReader in = null;
		String page = null;

		// String
		// _url="http://www.earthtools.org/timezone/"+String.valueOf(lat)+"/"+String.valueOf(lon);
		String _url = "lat="
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
	protected void onRestart() {
		super.onRestart();
		activityRestarted = true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see android.support.v4.app.FragmentActivity#onResume()
	 */
	@Override
	protected void onResume() {
		super.onResume();
		//if pager is in gps position
		if (activityRestarted && mViewPager.getCurrentItem() == 2) {
				gpsCitySearch.findCurrentLocation();
		}

	/*	typeface = CUtils.getUserSelectedLanguageFontType(
				getApplicationContext(),
				CUtils.getLanguageCodeFromPreference(getApplicationContext()));*/
		// CUtils.applyTypeFaceOnActionBarTitle(ActPlaceSearch.this,
		// typeface,getResources().getString(R.string.search_place));
		setScreenTitle(mediumTypeface, getResources()
				.getString(R.string.search_place));
		/*CUtils.showAdvertisement(ActPlaceSearch.this,
				(LinearLayout) findViewById(R.id.advLayout));*/
	}

	@Override
	protected void onPause() {
		super.onPause();
		/*CUtils.removeAdvertisement(ActPlaceSearch.this,
				(LinearLayout) findViewById(R.id.advLayout));*/
		CUtils.hideMyKeyboard(this);
	}

	/*private void setCustomFont(Typeface typeface) {
		((RadioButton) findViewById(R.id.rbPlaceSearch)).setTypeface(typeface);
		((RadioButton) findViewById(R.id.rbPlaceCustom)).setTypeface(typeface);
		((RadioButton) findViewById(R.id.rbPlaceGps)).setTypeface(typeface);
		((TextView) findViewById(R.id.tvSelectTimeZone)).setTypeface(typeface);
		((TextView) findViewById(R.id.tvLatitude)).setTypeface(typeface);
		((TextView) findViewById(R.id.tvLongitude)).setTypeface(typeface);
	}*/

	/*private void intiValues() {
		List<String> listTimeZone = new ArrayList<String>();
		ArrayAdapter<String> adapter = null;
		CDatabaseHelper cDatabaseHelper = null;
		try {
			cDatabaseHelper = new ControllerManager()
					.getDatabaseHelperObject(ActPlaceSearch.this);
			sQLiteDatabase = cDatabaseHelper.getWritableDatabase();
			ControllerManager cm = new ControllerManager();
			arrTimeZone = cm.searchTimeZone(sQLiteDatabase, "");
			for (int i = 0; i < arrTimeZone.length; i++)
				listTimeZone.add(arrTimeZone[i][0].trim());
			adapter = new ArrayAdapter<String>(ActPlaceSearch.this,
					android.R.layout.simple_list_item_checked, listTimeZone);
			spiTimeZone.setAdapter(adapter);
		} catch (UIDataOperationException e) {
			Toast.makeText(ActPlaceSearch.this, e.getMessage(),
					Toast.LENGTH_LONG).show();
		} finally {
			if (cDatabaseHelper != null)
				cDatabaseHelper.close();
		}

	}*/

	/*public void goToSearchPlace(View v) {
		isOnlineSearched = false;
		String searchText = editTextSearchCity.getText().toString().trim();
		CUtils.hideMyKeyboard(ActPlaceSearch.this);
		if (check_goOnline.isChecked()) {
			cityList.setAdapter(null);
			if (searchText.length() >= 3
					&& CUtils.isConnectedWithInternet(ActPlaceSearch.this)) {
				//new SearchCityFromAstroSageAtlas(searchText).execute();
				searchCityFromAstroSageAtlas = new SearchCityFromAstroSageAtlas(searchText);
				searchCityFromAstroSageAtlas.execute();
			} else {
				if (searchText.length() >= 3) {
					mct.show(getResources().getString(R.string.no_internet));
				} else {
					editTextSearchCity.setError("  ");
					mct.show(getResources().getString(R.string.at_least_3_char));
				}
			}
		} else {
			searchCityLocalDatabaseUpdate(searchText);
		}

	}*/

	/**
	 * This function is used to fill values of city in place object and return
	 * to main screen
	 * 
	 * @param selCity
	 * @param itemPositionCity
	 * @param position
	 * @author Bijendra 31-may-13
	 */

	/*private void goToSelecetdCity(final int itemPositionCity) {
		String cityKey = String.valueOf(itemPositionCity);
		cityId = Integer.valueOf(mapCityID.get(cityKey));

		if (isOnlineSearched)
			new FatchCityDetailFromAstroSageAtlas(String.valueOf(cityId))
					.execute();
		else
			fetchCityDetailFromDatabase(cityId);

	}*/

	/**
	 * This function is used to fetch city detail from local database
	 * 
	 * @param cityId
	 */
/*	private void fetchCityDetailFromDatabase(int cityId) {

		ControllerManager cm = null;
		CDatabaseHelper cDatabaseHelper = null;
		try {
			BeanPlace objPlaceTz, objPlace = null;
			cDatabaseHelper = new ControllerManager()
					.getDatabaseHelperObject(ActPlaceSearch.this);
			sQLiteDatabase = cDatabaseHelper.getReadableDatabase();
			cm = new ControllerManager();

			objPlace = cm.getCityById(sQLiteDatabase, cityId);
			objPlaceTz = cm.getTimZoneId(sQLiteDatabase,
					objPlace.getTimeZoneId());
			objPlace.setTimeZoneValue(objPlaceTz.getTimeZoneValue());
			objPlace.setTimeZoneId(objPlaceTz.getTimeZoneId());
			objPlace.setTimeZoneName(objPlaceTz.getTimeZoneName());

			if (make_it_default_city.isChecked()) {
				CUtils.saveCityAsDefaultCity(ActPlaceSearch.this, objPlace);
			}

			// 08-oct-2015 for getting float value of lat long and timeZone
			BeanPlace place = CUtils.getLatLongAndTimeZone(objPlace);

			// Bundle bundle = CUtils.getBundleOfPlaceValues(objPlace);
			Bundle bundle = CUtils.getBundleOfPlaceValues(place);
			Intent intent = new Intent();
			intent.putExtras(bundle);
			setResult(RESULT_OK, intent);
			this.finish();

		} catch (Exception e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		} finally {
			if (cDatabaseHelper != null)
				cDatabaseHelper.close();
		}
	}*/

	/**
	 * This function is sued to search city from local database
	 * 
	 * @param cityName
	 */
	/*
	 * private void searchCityLocalDatabase(String cityName) {
	 * 
	 * CDatabaseHelper cDatabaseHelper = null; try { listPlace.clear();
	 * mapCityID.clear(); String searchText = cityName; cDatabaseHelper = new
	 * ControllerManager() .getDatabaseHelperObject(ActPlaceSearch.this);
	 * sQLiteDatabase = cDatabaseHelper.getWritableDatabase(); if
	 * (sQLiteDatabase.isOpen()) {
	 * 
	 * String[][] cityArr = new ControllerManager().searchCity(sQLiteDatabase,
	 * searchText); if (cityArr != null) { for (int i = 0; i < cityArr.length;
	 * i++) { listPlace.add(cityArr[i][1]); mapCityID.put(String.valueOf(i),
	 * cityArr[i][0]); }
	 * 
	 * }
	 * 
	 * if (listPlace.size() == 0) { if(cityName.length() >= 3 &&
	 * CUtils.isConnectedWithInternet(ActPlaceSearch.this)) { new
	 * SearchCityFromAstroSageAtlas(cityName).execute(); }else {
	 * if(cityName.length() >= 3) {
	 * mct.show(getResources().getString(R.string.no_internet)); }else {
	 * editTextSearchCity.setError("  ");
	 * mct.show(getResources().getString(R.string.at_least_3_char)); } } } }
	 * 
	 * } catch (UIDataOperationException e) {
	 * Toast.makeText(ActPlaceSearch.this,
	 * e.getMessage(),Toast.LENGTH_LONG).show(); } finally { if (cDatabaseHelper
	 * != null) cDatabaseHelper.close(); } cityList.setAdapter(new
	 * ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,
	 * listPlace)); }
	 */
	/*
	 * This function is used to fatch city from local database
	 */
	/*private void searchCityLocalDatabaseUpdate(String cityName) {

		try {

			listPlace.clear();
			mapCityID.clear();
			String searchText = cityName;

			String[][] cityArr = new ControllerManager().searchCityOperation(
					getApplicationContext(), searchText);
			if (cityArr != null) {
				for (int i = 0; i < cityArr.length; i++) {
					listPlace.add(cityArr[i][1]);
					mapCityID.put(String.valueOf(i), cityArr[i][0]);
				}

			}

			if (listPlace.size() == 0) {
				if (cityName.length() >= 3
						&& CUtils.isConnectedWithInternet(ActPlaceSearch.this)) {
					//new SearchCityFromAstroSageAtlas(cityName).execute();
					searchCityFromAstroSageAtlas = new SearchCityFromAstroSageAtlas(cityName);
					searchCityFromAstroSageAtlas.execute();
				} else {
					if (cityName.length() >= 3) {
						mct.show(getResources().getString(R.string.no_internet));
					} else {
						editTextSearchCity.setError("  ");
						mct.show(getResources().getString(
								R.string.at_least_3_char));
					}
				}
			}

		} catch (UIDataOperationException e) {
			Toast.makeText(ActPlaceSearch.this, e.getMessage(),
					Toast.LENGTH_LONG).show();
		}
		cityList.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, listPlace));
	}*/

	/**
	 * This function set the place detail get from server into Place object and
	 * return to main screen
	 * 
	 * @param place
	 * @author Bijendra 31-may-13
	 */
	/*private void setPlaceDetailSearchedFromAstroSageAtlas(LibOutPlace place) {

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
			if (SELECTED_MODULE == CGlobalVariables.MODULE_ASTROSAGE_PANCHANG
					|| SELECTED_MODULE == CGlobalVariables.MODULE_ASTROSAGE_HORA
					|| SELECTED_MODULE == CGlobalVariables.MODULE_ASTROSAGE_CHOGADIA) {
				_objPlace.setCityId(cityId);
			} else {
				_objPlace.setCityId(-1);
			}
			_objPlace.setLatitude(String.valueOf(fLat));
			_objPlace.setLongitude(String.valueOf(fLOng));
			_objPlace.setTimeZone(String.valueOf(fTz));
			_objPlace.setState(place.getState());
			_objPlace.setCountryName(place.getCountry());
			_objPlace.setTimeZoneString(place.getTimeZoneString());

			_objPlace.setCountryId(-1);
			_objPlace.setTimeZoneId(-1);

			_objPlace.setCityName(place.getName());
			_objPlace.setLongDeg(_lonDeg);
			_objPlace.setLongMin(_lonMin);
			_objPlace.setLongDir(_lonDir);

			_objPlace.setLatDeg(_latDeg);
			_objPlace.setLatMin(_latMin);
			_objPlace.setLatDir(_latDir);

			_objPlace.setTimeZoneName(_tzname);
			_objPlace.setTimeZoneValue(fTz);

			_objPlace.setTimeZoneId(-1);

			if (make_it_default_city.isChecked()) {
				CUtils.saveCityAsDefaultCity(ActPlaceSearch.this, _objPlace);
			}

			Bundle bundle = CUtils.getBundleOfPlaceValues(_objPlace);
			Intent intent = new Intent();
			bundle.putString("CityID", "-1");
			intent.putExtras(bundle);
			setResult(RESULT_OK, intent);
			this.finish();

		} catch (Exception e) {

		}
	}*/

	/**
	 * This is a Asyn class used to search city from AstroSage Atlas
	 * 
	 * @author Bijendra 31-may-13
	 */

	/*private class SearchCityFromAstroSageAtlas extends
			AsyncTask<String, Long, Void> {

		boolean isCityFound = false;
		ArrayList<LibOutPlace> places = null;

		String _searchText = "";
		CustomProgressDialog pd = null;

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
			pd = new CustomProgressDialog(ActPlaceSearch.this, typeface);
			pd.show();

		}

		@Override
		protected void onPostExecute(Void result) {

			try {
				if (pd != null & pd.isShowing())
					pd.dismiss();
			} catch (Exception e) {

			}

			if (isCityFound & places != null) {
				isOnlineSearched = true;
				listPlace.clear();
				for (int i = 0; i < places.size(); i++) {
					listPlace.add(places.get(i).getName().trim() + ", "
							+ places.get(i).getState().trim()+" "
							+ "("+places.get(i).getCountry()+")");
					mapCityID.put(String.valueOf(i), places.get(i).getId());
				}
				cityList.setAdapter(new ArrayAdapter<String>(
						ActPlaceSearch.this,
						android.R.layout.simple_list_item_1, listPlace));
			} else {
				mct.show(getResources().getString(R.string.city_not_found));
			}
		}

	}*/

	/**
	 * This is a Asyn class used to get city detail from AstroSage Atlas
	 * 
	 * @author Bijendra 31-may-13
	 */
	/*private class FatchCityDetailFromAstroSageAtlas extends
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
			pd = new CustomProgressDialog(ActPlaceSearch.this, typeface);
			pd.show();
		}

		@Override
		protected void onPostExecute(Void result) {

			try {
				if (pd != null & pd.isShowing())
					pd.dismiss();
			} catch (Exception e) {

			}

			if (isCityFound & place != null) {
				setPlaceDetailSearchedFromAstroSageAtlas(place);
			} else {
				mct.show(getResources().getString(R.string.city_not_found));
			}

		}

	}*/

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.actionbarsherlock.app.SherlockFragmentActivity#onStop()
	 */
	@Override
	protected void onStop() {
		super.onStop();
		/*if ((myLocation != null) && (myLocation.lm != null)
				&& (myLocation.locationListenerGps != null)) {
			myLocation.lm.removeUpdates(myLocation.locationListenerGps);
		}
		if ((myLocation != null) && (myLocation.lm != null)
				&& (myLocation.locationListenerNetwork != null)) {
			myLocation.lm.removeUpdates(myLocation.locationListenerNetwork);
		}*/
		//EasyTracker.getInstance().activityStop(this);
	}

	// CUSTOM CITY INPUT FUNCTIONS
	/*public void goToPlaceCustomOk(View v) {
		if (isValidCustomForm()) {
			Bundle bundle = getBundleOfPlaceValues();
			Intent intent = new Intent();
			intent.putExtras(bundle);
			setResult(RESULT_OK, intent);
			this.finish();
		}
	}*/

	/*public void goToPlaceCustomCancel(View v) {
		this.finish();
	}*/

	/*private boolean isValidCustomForm() {
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
	}*/

	/**
	 * This funciton i suse to prepare bundle for the place object
	 * 
	 * @return Bundle
	 * @author Bijendra
	 * @date 27-nov-2012
	 */

	/*private Bundle getBundleOfPlaceValues() {
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

			if (make_it_default_city2.isChecked()) {
				CUtils.saveCityAsDefaultCity(ActPlaceSearch.this, _objPlace);
			}

			// 12-oct-2015
			BeanPlace beanPlace = CUtils.getLatLongAndTimeZone(_objPlace);

			// bundle = CUtils.getBundleOfPlaceValues(_objPlace);
			bundle = CUtils.getBundleOfPlaceValues(beanPlace);

		} catch (Exception e) {
			Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
		}

		return bundle;

	}*/

	private void setScreenTitle(Typeface typeface, String title) {
		tvToolBarTitle.setTypeface(typeface);
		tvToolBarTitle.setText(title);
	}
	
	@Override
	public void onBackPressed() {
	// TODO Auto-generated method stub
		CUtils.hideMyKeyboard(this);
	super.onBackPressed();

	    /* if (searchCityFromAstroSageAtlas!= null && searchCityFromAstroSageAtlas.getStatus() == Status.RUNNING)
	             searchCityFromAstroSageAtlas.cancel(true);*/
	}

}
