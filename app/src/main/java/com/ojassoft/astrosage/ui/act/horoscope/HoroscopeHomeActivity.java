package com.ojassoft.astrosage.ui.act.horoscope;

import android.app.Activity;
import android.app.ComponentCaller;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.libojassoft.android.misc.VolleyResponse;
import com.libojassoft.android.misc.VolleyServiceHandler;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.ActAppModule;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.ui.fragments.HoroscopeEnterNameDialog;
import com.ojassoft.astrosage.ui.fragments.HoroscopeRashiPredictionDialog;
import com.ojassoft.astrosage.ui.fragments.HoroscopeRashiResultPopupDialog;
import com.ojassoft.astrosage.ui.fragments.horoscope.HoroscopeNotificationFragment;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.Credentials;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class HoroscopeHomeActivity extends BaseInputActivity implements VolleyResponse {

    public Integer[] rashiIconWithName = {R.drawable.aries, R.drawable.taurus,
            R.drawable.gemini, R.drawable.cancer, R.drawable.leo,
            R.drawable.virgo, R.drawable.libra, R.drawable.scorpio,
            R.drawable.sagittarius, R.drawable.capricorn, R.drawable.aquarius,
            R.drawable.pisces};
    GridView rashiGrid;
    int screenWidth = 240;// Min width
    private int imgWidth = 0, imgHeight = 0;
    private ProgressBar progressBar;

    public final int KNOW_YOUR_MOON_SIGN = 0;
    public final int By_Name = 1;
    public final int BY_DATE_OF_BIRTH = 2;
    public final int SETTINGS = 3;
    public final int HOROSCOPE_NOTIFICATION = 4;
    public final int MISC = 5;
    public final int FEEDBACK = 6;
    public final int RATE_APP = 7;
    public final int SHARE_APP = 8;
    public final int OUR_OTHER_APPS = 9;
    public final int CALL_US = 10;
    public final int ASTRO_SHOP = 11;
    public final int ASK_OUR_ASTROLOGER = 12;
    public final int ABOUT_US = 13;
    private static final int CLOUD_SIGN_OUT = 14;
    public static final int PRODUCT_PLAN_LIST = 15;
    public String[] rashiNames = null;
    private int globalint;
    private Credentials credentials;
    // private Typeface typefaces;

    static final String USER_IS_VERFIY = "user_verify";
    static final String KEY = "verificationkey";
    private Toolbar toolBar;
    private TextView tvToolBarTitle;

    public static final int SUB_ACTIVITY_USER_LOGIN = 1003;
    public static final int BACK_FROM_PLAN_PURCHASE_AD_SCREEN = 210;

    //HoroscopeHomeMenuFragment horoscopeHomeMenuFrag;
    int SUB_MODULE_TYPE_KEY = -1;

    TabLayout tabs;
    ImageView imgMoreItem, imgBackButton;
    int selectedHoroscopePosition ;

   // public static final String TAG = "activityResultCallBackTest";

    public HoroscopeHomeActivity() {
        super(R.string.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.lay_horoscope_home);

        try {

            SUB_MODULE_TYPE_KEY = getIntent().getIntExtra(CGlobalVariables.SUB_MODULE_TYPE_KEY, -1);
        } catch (Exception ex) {
            //Log.i("Exception", ex.getMessage().toString());
        }

        toolBar = (Toolbar) findViewById(R.id.tool_barAppModule);
        // Get the navigation icon drawable
        Drawable navIcon = ContextCompat.getDrawable(this, R.drawable.ic_back_arrow);

// Check if the drawable is not null
        if (navIcon != null) {
            // Tint the drawable with the desired color
            navIcon.setTint(ContextCompat.getColor(this, R.color.black));

            // Set the tinted drawable as the navigation icon
            toolBar.setNavigationIcon(navIcon);
        }
        tvToolBarTitle = (TextView) toolBar.findViewById(R.id.tvTitle);

        imgMoreItem = (ImageView) findViewById(R.id.imgMoreItem);
        imgMoreItem.setVisibility(View.VISIBLE);
        setVisibilityOfMoreIconImage(imgMoreItem, getResources().getStringArray(
                R.array.horoscope_menu_item_list), getResources().obtainTypedArray(
                R.array.horoscope_menu_item_list_icons), horoscope_menu_item_list_index);
        /*imgBackButton = (ImageView) findViewById(R.id.imgBackButton);
        imgBackButton.setVisibility(View.VISIBLE);
        imgBackButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                finishActivity();
            }
        });*/


        tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.setVisibility(View.GONE);

        setSupportActionBar(toolBar);

        credentials = new Credentials(HoroscopeHomeActivity.this);
        rashiGrid = (GridView) findViewById(R.id.myGrid);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        rashiGrid.setAdapter(new ImageAdapter(this));
        rashiGrid.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1,
                                    final int position, long arg3) {
                selectedHoroscopePosition = position;

                // Check if the interstitial/home popup feature is enabled in settings
                boolean isPopupEnabled = com.ojassoft.astrosage.varta.utils.CUtils.getBooleanData(HoroscopeHomeActivity.this,CGlobalVariables.IS_INTERSTICIAL_ENABLED,false);
                // Check if the user is already a premium subscriber (Kundli AI+ or Dhruv Plan)
                boolean isPremiumUser = CUtils.isKundliAIPlusPlan(HoroscopeHomeActivity.this) || CUtils.isDhruvPlan(HoroscopeHomeActivity.this);
                // Logic: Show Ad if feature enabled + user NOT premium + time interval allows
                if (isPopupEnabled && !isPremiumUser && CUtils.canShowInterstitial(HoroscopeHomeActivity.this)) {
                    // Redirect to the Plan Purchase screen (acting as an Ad)
                    com.ojassoft.astrosage.varta.utils.CUtils.openPurchasePlanScreenForAd(
                            HoroscopeHomeActivity.this,
                            false,
                            false,
                            com.ojassoft.astrosage.varta.utils.CGlobalVariables.SOURCE_FROM_HOROSCOPE_AD,
                            true,
                            BACK_FROM_PLAN_PURCHASE_AD_SCREEN
                    );
                } else {
                    // Otherwise, proceed directly to the Rashi Prediction
                    gotoRashiPrediction(position);
                }

                /*
                 * if (CUtils.isInterstitialAdReady()) {
                 * CUtils.displayInterstitialAd(new AdListener() {
                 *
                 * @Override public void onAdClosed() { super.onAdClosed();
                 *
                 * gotoRashiPrediction(position); } }); } else {
                 * gotoRashiPrediction(position); }
                 */
                /*if(CUtils.isConnectedWithInternet(HoroscopeHomeActivity.this)) {
                    gotoRashiPrediction(position);
                }else{
                    //Toast.makeText(HoroscopeHomeActivity.this,"Please Check Your Network Connection",Toast.LENGTH_LONG).show();
                    MyCustomToast customToast=new MyCustomToast(HoroscopeHomeActivity.this, getLayoutInflater(),HoroscopeHomeActivity.this,mediumTypeface);
                    customToast.show(getResources().getString(R.string.no_internet));
                }*/
            }
        });

        screenWidth = (int) SCREEN_CONSTANTS.DeviceScreenWidth;
        imgWidth = (int) (screenWidth / 3.5);
        imgHeight = imgWidth;
        rashiNames = HoroscopeHomeActivity.this.getResources().getStringArray(
                R.array.rashiName_list);

        //horoscopeHomeMenuFrag = new HoroscopeHomeMenuFragment();

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if(Intent.ACTION_QUICK_VIEW.equals(getIntent().getAction())){
            com.ojassoft.astrosage.utils.CUtils.fcmAnalyticsEvents(com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTIC_APP_ICON_TODAYS_HOROSCOPE, com.ojassoft.astrosage.utils.CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");
        }
		/*getSupportActionBar().setLogo(
                getResources().getDrawable(R.drawable.astrosage_logo));*/

        /*
         * if(CUtils.isInterstitialAdReady()){
         * CUtils.displayInterstitialAd(null); } else{
         * ((AstrosageKundliApplication)getApplication()).loadInterstitialAd();
         * }
         */
        // CUtils.showAdvertisement(HoroscopeHomeActivity.this,(LinearLayout)findViewById(R.id.advLayout));
    }

    @Override
    public void setVisibilityOfMoreIconImage(View view, final String[] subMenuItems, final TypedArray subMenuItemsIcon, final Integer[] menuIndex) {
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingForpopUpMenu(view, subMenuItems, subMenuItemsIcon, menuIndex);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }


    /**
     * This is class for loading Rashi_icon from drawble.
     *
     * @author Corei3
     */
    public class ImageAdapter extends BaseAdapter {
        public ImageAdapter(Context c) {

        }

        public int getCount() {
            return HoroscopeHomeActivity.this.rashiIconWithName.length;
        }

        public Object getItem(int position) {
            return position;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                convertView = LayoutInflater.from(HoroscopeHomeActivity.this)
                        .inflate(R.layout.horoscope_grid_item, null);
            }
            ImageView rashiIcon = (ImageView) convertView
                    .findViewById(R.id.imageViewRashiIcon);
            TextView rashiName = (TextView) convertView
                    .findViewById(R.id.textViewRashiName);
            rashiIcon
                    .setImageResource(HoroscopeHomeActivity.this.rashiIconWithName[position]);
            rashiName.setText(rashiNames[position]);
            rashiName.setTypeface(mediumTypeface);
            return convertView;
        }
    }


    public void goToReasiDetailedActivity(final int rashiIndex) {
        showReasiDetailedActivity(rashiIndex);
        /*
        if (CUtils.isInterstitialAdReady(HoroscopeHomeActivity.this)) {
            if (CUtils.isCycleCompleted()) {
                CUtils.displayInterstitialAd(new AdListener() {
                    @Override
                    public void onAdClosed() {
                        super.onAdClosed();

                        showReasiDetailedActivity(rashiIndex);
                    }
                });
            } else {
                showReasiDetailedActivity(rashiIndex);
            }
        } else
            showReasiDetailedActivity(rashiIndex);
        */

    }

    private void showReasiDetailedActivity(int rashiIndex) {
        String label = "";
        switch (rashiIndex) {
            case 0:
                label = CGlobalVariables.RashiType[0];
                break;
            case 1:
                label = CGlobalVariables.RashiType[1];
                break;
            case 2:
                label = CGlobalVariables.RashiType[2];
                break;
            case 3:
                label = CGlobalVariables.RashiType[3];
                break;
            case 4:
                label = CGlobalVariables.RashiType[4];
                break;
            case 5:
                label = CGlobalVariables.RashiType[5];
                break;
            case 6:
                label = CGlobalVariables.RashiType[6];
                break;
            case 7:
                label = CGlobalVariables.RashiType[7];
                break;
            case 8:
                label = CGlobalVariables.RashiType[8];
                break;
            case 9:
                label = CGlobalVariables.RashiType[9];
                break;
            case 10:
                label = CGlobalVariables.RashiType[10];
                break;
            case 11:
                label = CGlobalVariables.RashiType[11];
                break;
        }

        if (SUB_MODULE_TYPE_KEY != -1) {
            CUtils.googleAnalyticSendWitPlayServie(
                    HoroscopeHomeActivity.this,
                    CGlobalVariables.GOOGLE_ANALYTIC_SCREEN,
                    CGlobalVariables.pageViewDailyWeeklyMonthlyHoroscope[SUB_MODULE_TYPE_KEY],
                    label);
        } else {
            CUtils.googleAnalyticSendWitPlayServie(
                    HoroscopeHomeActivity.this,
                    CGlobalVariables.GOOGLE_ANALYTIC_SCREEN,
                    CGlobalVariables.pageViewDailyWeeklyMonthlyHoroscope[0],
                    label);
        }

        Intent rashiPredictionIntent = new Intent(this, DetailedHoroscope.class);
        rashiPredictionIntent.putExtra("rashiType", rashiIndex);
        rashiPredictionIntent.putExtra("prediction_type", SUB_MODULE_TYPE_KEY);
        //rashiPredictionIntent.putExtra("needToShowAD",true);
        startActivity(rashiPredictionIntent);


    }

    /*
     * (non-Javadoc)
     *
     *
     * @see android.support.v4.app.FragmentActivity#onStart()
     */
    @Override
    protected void onStart() {
        super.onStart();
        //EasyTracker.getInstance().activityStart(this);
        /*
        if (!CUtils.isInterstitialAdReady(HoroscopeHomeActivity.this)) {
            ((AstrosageKundliApplication) getApplication())
                    .loadInterstitialAd();
        }*/
    }

    /*
     * (non-Javadoc)
     *
     * @see com.actionbarsherlock.app.SherlockFragmentActivity#onStop()
     */
    @Override
    protected void onStop() {
        super.onStop();
        if (progressBar.isShown())
            progressBar.setVisibility(View.GONE);
        rashiGrid = null;
        //EasyTracker.getInstance().activityStop(this);
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {

        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                this.finish();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*CUtils.removeAdvertisement(HoroscopeHomeActivity.this,
                (LinearLayout) findViewById(R.id.advLayout));*/
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.FragmentActivity#onResume()
     */
    @Override
    protected void onResume() {
        super.onResume();
        if (rashiGrid == null) {
            rashiGrid = (GridView) findViewById(R.id.myGrid);
            rashiGrid.setAdapter(new ImageAdapter(this));
        } else {
            rashiGrid.invalidateViews();
        }
        //etName.getBackground().setColorFilter(null);
        /*typeface = CUtils.getUserSelectedLanguageFontType(
                getApplicationContext(),
				CUtils.getLanguageCodeFromPreference(getApplicationContext()));*/
        /*
         * CUtils.applyTypeFaceOnActionBarTitle( HoroscopeHomeActivity.this,
         * typeface, HoroscopeHomeActivity.this.getResources().getString(
         * R.string.horoscope_home_title));
         */
        showToolBarTitle(regularTypeface, HoroscopeHomeActivity.this.getResources()
                .getString(R.string.text_select_your_sign));

        /*CUtils.showAdvertisement(HoroscopeHomeActivity.this,
                (LinearLayout) findViewById(R.id.advLayout));*/
    }

    /**
     * This function is used to show tool bar title
     *
     * @param typeface
     * @param titleToshow 17-Aug-2015
     */
    private void showToolBarTitle(Typeface typeface, String titleToshow) {
        tvToolBarTitle.setTypeface(typeface);
        if (titleToshow != null)
            tvToolBarTitle.setText(titleToshow);
        else
            // ADDED BY BIJENDRA ON 17-06-14
            tvToolBarTitle.setText("");

    }

    TextView heading;


   /* private class MyTextWatcher implements TextWatcher {

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
                case R.id.etPopupName:
                    validateName();
                    break;

            }
        }
    }*/

   /* private boolean validateName() {
        if (etName.getText().toString().trim().isEmpty() || etName.getText().toString().trim().length() < 3) {

            nameInputLayout.setErrorEnabled(true);
            nameInputLayout.setError(getString(R.string.please_enter_name_v));
            requestFocus(etName);
            etName.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
            return false;
        } else {
            nameInputLayout.setErrorEnabled(false);
            nameInputLayout.setError(null);
            etName.getBackground().setColorFilter(getResources().getColor(R.color.ColorPrimary), PorterDuff.Mode.SRC_ATOP);
        }

        return true;
    }*/

    /*private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);

        }
    }*/

    private int rashiIndexFromServer;

    public void getRashiByName(String name) {
        //new GetRashiByNameFromServerASync(name).execute();
        GetRashiByNameFromServerASync(name);
    }

    /**
     * This is AsyncTask to fetch today's weekly and monthly horoscope RSS feed.
     *
     * @author Hukum
     * @since 15-May-2013
     */
    /*private class GetRashiByNameFromServerASync extends
            AsyncTask<String, Long, Void> {

        String name = "";

        public GetRashiByNameFromServerASync(String _name) {
            name = _name;
        }

        @Override
        protected Void doInBackground(String... params) {

            try {
                rashiIndexFromServer = CUtils.getRashiByName(name);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        *//*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         *//*
        @Override
        protected void onPostExecute(Void result) {
            try {
                if (progressBar.isShown())
                    progressBar.setVisibility(View.GONE);
            } catch (Exception e) {
                // nothing
            }
            if (rashiIndexFromServer != -1) {
                String msg = HoroscopeHomeActivity.this.getResources()
                        .getString(R.string.RashiFromServerText);
                msg = msg
                        .replace(
                                "#",
                                HoroscopeHomeActivity.this.getResources()
                                        .getStringArray(R.array.rashiName_list)[rashiIndexFromServer]);
                showRashiResultDialogPopup(
                        msg,
                        HoroscopeHomeActivity.this.getResources()
                                .getStringArray(R.array.rashiName_list)[rashiIndexFromServer]
                                + " "
                                + HoroscopeHomeActivity.this.getResources()
                                .getString(R.string.horoscopeText));
            } else {
                MyCustomToast mct2 = new MyCustomToast(
                        HoroscopeHomeActivity.this,
                        HoroscopeHomeActivity.this.getLayoutInflater(),
                        HoroscopeHomeActivity.this, mediumTypeface);
                mct2.show(getResources().getString(R.string.RashiNotFound));
            }
            super.onPostExecute(result);
        }

        *//*
         * (non-Javadoc)
         *
         * @see android.os.AsyncTask#onPreExecute()
         *//*
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            super.onPreExecute();
        }
    }*/


    private boolean hasDoNotShownNotificationDialogChecked() {
        SharedPreferences sharedPreferences = getSharedPreferences(KEY,
                Context.MODE_PRIVATE);
        boolean log = sharedPreferences.getBoolean(USER_IS_VERFIY, false);
        return log;
    }

    public void saveDoNotShownNotificationDialogChecked() {
        Editor editor = getSharedPreferences(KEY, Context.MODE_PRIVATE).edit();
        editor.putBoolean(USER_IS_VERFIY, true).commit();

    }


    @Override
    public void logoutFromAstroSageCloud(boolean isShowToast) {
        //horoscopeHomeMenuFrag.updateLoginDetials(false, "", "");
        MyCustomToast mct = new MyCustomToast(HoroscopeHomeActivity.this,
                HoroscopeHomeActivity.this.getLayoutInflater(),
                HoroscopeHomeActivity.this, mediumTypeface);
        mct.show(getResources().getString(R.string.sign_out_success));
    }

    public void gotoRashiPrediction(final int rashiIndex) {
        try {
            if (!hasDoNotShownNotificationDialogChecked()) {
                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                FragmentManager fm = getSupportFragmentManager();
                Fragment prev = fm.findFragmentByTag("HoroscopeRashiPredictionDialog");
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);
                HoroscopeRashiPredictionDialog horoscopeRashiPredictionDialog = HoroscopeRashiPredictionDialog.getInstance(rashiIndex);
                horoscopeRashiPredictionDialog.show(fm, "HoroscopeRashiPredictionDialog");
                ft.commit();

            } else {
                goToReasiDetailedActivity(rashiIndex);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void askUserToEnterName() {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        FragmentManager fm = getSupportFragmentManager();
        Fragment prev = fm.findFragmentByTag("HoroscopeEnterNameDialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        HoroscopeEnterNameDialog horoscopeEnterNameDialog = HoroscopeEnterNameDialog.getInstance();
        horoscopeEnterNameDialog.show(fm, "HoroscopeEnterNameDialog");
        ft.commit();
    }

    private void showRashiResultDialogPopup(final String msg, final String btnText) {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        FragmentManager fm = getSupportFragmentManager();
        Fragment prev = fm.findFragmentByTag("HoroscopeRashiResultPopupDialog");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);
        HoroscopeRashiResultPopupDialog horoscopeRashiResultPopupDialog = HoroscopeRashiResultPopupDialog.getInstance(msg, btnText, rashiIndexFromServer);
        horoscopeRashiResultPopupDialog.show(fm, "HoroscopeRashiResultPopupDialog");
        ft.commit();
    }

    @Override
    public void openNotificationCategaryDialog() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        FragmentManager fm = getSupportFragmentManager();
        Fragment prev = fm.findFragmentByTag("HOROSCOPE_CATEGARY");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        HoroscopeNotificationFragment hnfd = new HoroscopeNotificationFragment();
        hnfd.show(fm, "HOROSCOPE_CATEGARY");
        ft.commit();

    }

    /*
     * private void applyChangedLanguageInApplication() { Intent intent = new
     * Intent(getApplicationContext(), HomeInputScreen.class);
     * intent.putExtra("LANGUAGE_CODE", LANGUAGE_CODE); startActivity(intent);
     * startActivity(new
     * Intent(getApplicationContext(),ActAppModule.class).addFlags
     * (Intent.FLAG_ACTIVITY_CLEAR_TOP)); this.finish(); } private void
     * openLanguageSelectDialog() { FragmentTransaction ft =
     * getSupportFragmentManager().beginTransaction(); FragmentManager fm =
     * getSupportFragmentManager(); Fragment prev =
     * fm.findFragmentByTag("HOME_INPUT_LANGUAGE"); if (prev != null) {
     * ft.remove(prev); } ft.addToBackStack(null); ChooseLanguageFragmentDailog
     * clfd=new ChooseLanguageFragmentDailog(); clfd.show(fm,
     * "HOME_INPUT_LANGUAGE"); ft.commit(); }
     */
    /*
     * @Override public void onSelectedLanguage(int languageIndex) {
     * if(LANGUAGE_CODE!=languageIndex) { LANGUAGE_CODE=languageIndex;
     * applyChangedLanguageInApplication(); } }
     */

	/*public void goToLogin() {

		if (!CUtils.isUserLogedIn(HoroscopeHomeActivity.this)) {
			*//*
     * Intent intent = new Intent(HomeInputScreen.this,
     * ActWizardScreens.class); intent.putExtra("callerActivity",
     * CGlobalVariables.HOME_INPUT_SCREEN);
     * startActivityForResult(intent, SUB_ACTIVITY_USER_LOGIN);
     *//*
            Intent intent = new Intent(HoroscopeHomeActivity.this,
					ActLogin.class);
			intent.putExtra("callerActivity",
					CGlobalVariables.HOME_INPUT_SCREEN);
			startActivityForResult(intent, SUB_ACTIVITY_USER_LOGIN);
		} else {
			//horoscopeHomeMenuFrag.updateLoginDetials(false, "", "");
			MyCustomToast mct = new MyCustomToast(HoroscopeHomeActivity.this,
					HoroscopeHomeActivity.this.getLayoutInflater(),
					HoroscopeHomeActivity.this, typeface);
			mct.show(getResources().getString(R.string.sign_out_success));
		}
		//getSlidingMenu().showContent();
	}*/

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent arg2) {
        // TODO Auto-generated method stub
        super.onActivityResult(requestCode, resultCode, arg2);
        switch (requestCode) {
            case SUB_ACTIVITY_USER_LOGIN: {
                if (resultCode == RESULT_OK) {
                    Bundle b = arg2.getExtras();
                    String loginName = b.getString("LOGIN_NAME");
                    String loginPwd = b.getString("LOGIN_PWD");
                    setUserLoginDetails(loginName, loginPwd);
                }
            }
            break;
            case BACK_FROM_PLAN_PURCHASE_AD_SCREEN:
                gotoRashiPrediction(selectedHoroscopePosition);
                break;
        }
    }

    private void setUserLoginDetails(String loginName, String loginPwd) {
        //horoscopeHomeMenuFrag.updateLoginDetials(true, loginName, loginPwd);
    }

    /*private void finishActivity() {
        this.finish();
    }*/


    private void GetRashiByNameFromServerASync(String name) {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
        }
        RequestQueue queue = VolleySingleton.getInstance(this).getRequestQueue();
        String url = CGlobalVariables.URI_get_moon_sign_by_name + name;
        Log.e("LoadMore URL ", url);
        StringRequest stringRequest = new VolleyServiceHandler(Request.Method.GET, url, this, true, getParamsNew(),0).getMyStringRequest();
        queue.add(stringRequest);
    }

    public Map<String, String> getParamsNew() {

        HashMap<String, String> params = new HashMap<String, String>();

        return params;
    }

    @Override
    public void onResponse(String response, int method) {
        try {
            if (progressBar != null && progressBar.isShown()) {
                progressBar.setVisibility(View.GONE);
            }
            Log.e("LoadMore URL ", response);
            int rashiIndex = -1;
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    String rashiIndexString = jsonArray.getJSONObject(i).getString(
                            "RashiNo");
                    if (rashiIndexString.equalsIgnoreCase("Not Found")) {
                        rashiIndex = -1;
                    } else {
                        try {
                            rashiIndex = Integer.valueOf(rashiIndexString);
                        } catch (Exception e) {
                            rashiIndex = -1;
                        }

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                    rashiIndex = -1;
                }
                rashiIndexFromServer = rashiIndex;

                if (rashiIndexFromServer != -1) {
                    String msg = HoroscopeHomeActivity.this.getResources()
                            .getString(R.string.RashiFromServerText);
                    msg = msg
                            .replace(
                                    "#",
                                    HoroscopeHomeActivity.this.getResources()
                                            .getStringArray(R.array.rashiName_list)[rashiIndexFromServer]);
                    showRashiResultDialogPopup(
                            msg,
                            HoroscopeHomeActivity.this.getResources()
                                    .getStringArray(R.array.rashiName_list)[rashiIndexFromServer]
                                    + " "
                                    + HoroscopeHomeActivity.this.getResources()
                                    .getString(R.string.horoscopeText));
                } else {
                    MyCustomToast mct2 = new MyCustomToast(
                            HoroscopeHomeActivity.this,
                            HoroscopeHomeActivity.this.getLayoutInflater(),
                            HoroscopeHomeActivity.this, mediumTypeface);
                    mct2.show(getResources().getString(R.string.RashiNotFound));
                }
            }
        } catch (Exception e) {
            MyCustomToast mct2 = new MyCustomToast(
                    HoroscopeHomeActivity.this,
                    HoroscopeHomeActivity.this.getLayoutInflater(),
                    HoroscopeHomeActivity.this, mediumTypeface);
            mct2.show(getResources().getString(R.string.RashiNotFound));
        }
    }

    @Override
    public void onError(VolleyError error) {
        if (progressBar != null && progressBar.isShown()) {
            progressBar.setVisibility(View.GONE);
        }
    }
}
