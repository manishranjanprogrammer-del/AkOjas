package com.ojassoft.astrosage.ui.act.matching;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import com.google.android.material.tabs.TabLayout;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.libojassoft.android.misc.VolleyResponse;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanDateTime;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.beans.BeanOutMatchmakingNorth;
import com.ojassoft.astrosage.beans.BeanPlace;
import com.ojassoft.astrosage.controller.ControllerManager;
import com.ojassoft.astrosage.customadapters.ViewPagerAdapter;
import com.ojassoft.astrosage.jinterface.IChooseLanguageFragment;
import com.ojassoft.astrosage.jinterface.ISearchBirthDetailsFragment;
import com.ojassoft.astrosage.jinterface.ISeparatorMenuFragment;
import com.ojassoft.astrosage.jinterface.matching.IMatchingInputDetailFragment;
import com.ojassoft.astrosage.misc.CustomDatePicker;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.ui.act.ActAppModule;
import com.ojassoft.astrosage.ui.act.ActPlaceSearch;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.act.HomeInputScreen;
import com.ojassoft.astrosage.ui.customcontrols.DateTimePicker;
import com.ojassoft.astrosage.ui.customcontrols.DateTimePicker.DateWatcher;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.ui.customcontrols.MyNewCustomTimePicker;
import com.ojassoft.astrosage.ui.fragments.ChooseLanguageFragmentDailog;
import com.ojassoft.astrosage.ui.fragments.SearchBirthDetailsFragment;
import com.ojassoft.astrosage.ui.fragments.matching.MatchingInputDetailFragment;
import com.ojassoft.astrosage.utils.CGlobalMatching;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.MyDatePicker;
import com.ojassoft.astrosage.utils.MyDatePickerDialog;
import com.ojassoft.astrosage.utils.MyTimePickerDialog;
import com.ojassoft.astrosage.varta.utils.FontUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLEncoder;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;


public class HomeMatchMakingInputScreen extends BaseInputActivity
        implements ISearchBirthDetailsFragment, IMatchingInputDetailFragment,
        DateWatcher, ISeparatorMenuFragment, IChooseLanguageFragment, VolleyResponse {

    public static int PERSON1 = 0;
    public static int PERSON2 = 1;
    public static int PERSON_CALLED = PERSON1;
    public int PERSON_CALLED_FOR_DB_OPERATION = PERSON1;
    public static int collectDataForPerson = -1;

    private java.text.DateFormat mDateFormat;


    /**
     * This is flag to make app online / Offline
     */
    private final boolean IS_APP_ONLINE = true;
    // CONSTANTS

    String[] pageTitles;
    public ViewPager viewPager;
    final int SAVED_KUNDLI_SCREEN = 0;
    final int INPUT_SCREEN = 1;
    private boolean isEditKundli = false;
    BeanDateTime beanDateTimeInput;
    public SearchBirthDetailsFragment searchBirthDetailsFragment;
    MatchingInputDetailFragment matchingInputDetailFragment;
    public int SELECTED_MODULE, LANGUAGE_CODE;
    public static boolean isMenuItemClicked = false;

    Toolbar toolBar_InputKundli;
    ViewPagerAdapter adapter;
    private TabLayout tabs_input_kundli;

    TextView titleTextView;
    ImageView moreImageView;
    ImageView toggleImageView, imgBackButton;
    CustomProgressDialog pd = null;
    private boolean isReferenceIssue = false;
    private BeanPlace cPlaceBoy = new BeanPlace();
    private BeanPlace cPlaceGirl = new BeanPlace();

    private BeanDateTime beanDateIfIssueBoy = new BeanDateTime();
    private BeanDateTime beanTimeIfIssueBoy = new BeanDateTime();
    private BeanDateTime beanDateIfIssueGirl = new BeanDateTime();
    private BeanDateTime beanTimeIfIssueGirl = new BeanDateTime();
    int SAVE_CHRATS = 0;
    int CALCULATE_CHART = 1;
    boolean isShowToast;
    String saveMsg;
    boolean isUpgradeAfterTenChartDialogShown = false;

    public HomeMatchMakingInputScreen() {
        super(R.string.app_name);

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            isEditKundli = getIntent().getBooleanExtra("IS_EDIT_INPUT_DETAIL",
                    false);
            if (savedInstanceState != null) {
                try {
                    CGlobalMatching cGlobal = (CGlobalMatching) savedInstanceState
                            .getSerializable(CGlobalVariables.outPutMatchingMasterActSavedBundleKey);
                    CGlobalMatching.setCGlobalMatching(cGlobal);

                    // Getting Plac
                    cPlaceBoy = (BeanPlace) savedInstanceState
                            .getSerializable("cPlaceBoy");
                    cPlaceGirl = (BeanPlace) savedInstanceState
                            .getSerializable("cPlaceGirl");

                    beanDateIfIssueBoy = (BeanDateTime) savedInstanceState
                            .getSerializable("beanDateIfIssueBoy");
                    beanTimeIfIssueBoy = (BeanDateTime) savedInstanceState
                            .getSerializable("beanTimeIfIssueBoy");
                    beanDateIfIssueGirl = (BeanDateTime) savedInstanceState
                            .getSerializable("beanDateIfIssueGirl");
                    beanTimeIfIssueGirl = (BeanDateTime) savedInstanceState
                            .getSerializable("beanTimeIfIssueGirl");

                    matchingInputDetailFragment = (MatchingInputDetailFragment) getSupportFragmentManager()
                            .getFragment(savedInstanceState,
                                    MatchingInputDetailFragment.class.getName());
                    searchBirthDetailsFragment = (SearchBirthDetailsFragment) getSupportFragmentManager()
                            .getFragment(savedInstanceState,
                                    SearchBirthDetailsFragment.class.getName());
                } catch (Exception e) {
                    // e.printStackTrace();
                    searchBirthDetailsFragment = new SearchBirthDetailsFragment();
                    matchingInputDetailFragment = new MatchingInputDetailFragment();
                    isReferenceIssue = true;
                }
            } else {
                searchBirthDetailsFragment = new SearchBirthDetailsFragment();
                matchingInputDetailFragment = new MatchingInputDetailFragment();
            }

            //call This method is uset to give recent id if not available in RecentSearchKundli
            CUtils.addRecentIdsInChartIfNotAvailableInRecentSearchCharts(HomeMatchMakingInputScreen.this);

            //matchingHomeInputMenuFrag = new MatchingHomeInputMenuFrag();
            SELECTED_MODULE = getIntent().getIntExtra(
                    CGlobalVariables.MODULE_TYPE_KEY,
                    CGlobalVariables.MODULE_MATCHING);
            // LANGUAGE_CODE = CUtils.getLanguageCodeFromPreference(this);
            LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication())
                    .getLanguageCode();// ADDED BY HEVENDRA ON 24-12-2014

            setContentView(R.layout.lay_input_matching_screen);

            toggleImageView = (ImageView) findViewById(R.id.ivToggleImage);
            titleTextView = (TextView) findViewById(R.id.tvTitle);
            //homeImageView = (ImageView) findViewById(R.id.imgHome);
            moreImageView = (ImageView) findViewById(R.id.imgMoreItem);


            toggleImageView.setVisibility(View.GONE);
            //homeImageView.setVisibility(View.VISIBLE);
            moreImageView.setVisibility(View.VISIBLE);


            setVisibilityOfMoreIconImage(moreImageView, getResources().getStringArray(
                    R.array.matching_home_menu_item_list), getResources().obtainTypedArray(
                    R.array.matching_home_menu_item_list_icon), matching_home_menu_item_list_index);

            titleTextView.setText(R.string.matching);
            titleTextView.setTypeface(mediumTypeface);
            toolBar_InputKundli = (Toolbar) findViewById(R.id.tool_barAppModule);
            // Get the navigation icon drawable
            Drawable navIcon = ContextCompat.getDrawable(this, R.drawable.ic_back_arrow);

// Check if the drawable is not null
            if (navIcon != null) {
                // Tint the drawable with the desired color
                navIcon.setTint(ContextCompat.getColor(this, R.color.black));

                // Set the tinted drawable as the navigation icon
                toolBar_InputKundli.setNavigationIcon(navIcon);
            }
           // toolBar_InputKundli.setNavigationIcon(R.drawable.ic_back_arrow);
            tabs_input_kundli = (TabLayout) findViewById(R.id.tabs);
            tabs_input_kundli.setTabMode(TabLayout.MODE_FIXED);
            setSupportActionBar(toolBar_InputKundli);
            viewPager = (ViewPager) findViewById(R.id.pager);
            try {
                configureActionBarTabStyle();
            } catch (Exception e) {
                e.printStackTrace();
            }

            setViewPagerAdapter();

            getSupportActionBar().setDisplayShowTitleEnabled(false);

            tabs_input_kundli.setupWithViewPager(viewPager);
            for (int i = 0; i < tabs_input_kundli.getTabCount(); i++) {
                TabLayout.Tab tab = tabs_input_kundli.getTabAt(i);
                tab.setCustomView(adapter.getTabView(i));
            }
            setCurrentView(INPUT_SCREEN, false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {

                    // Input Details Fragment
                    if (position == 1) {
                        collectDataForPerson = -1;
                    }

                    setCurrentView(position, false);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });


        } catch (Exception e) {
        }
        mDateFormat = DateFormat.getMediumDateFormat(HomeMatchMakingInputScreen.this);
        if(Intent.ACTION_VIEW.equals(getIntent().getAction())){
           CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_APP_ICON_FREE_MATCH_MAKING, CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");
        }
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

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.FragmentActivity#onStart()
     */
    @Override
    protected void onStart() {
        super.onStart();
        //EasyTracker.getInstance().activityStart(this);
        // ADDED BY DEEPAK ON 08-12-14
        /*
        if (!CUtils.isInterstitialAdReady(HomeMatchMakingInputScreen.this)) {
            ((AstrosageKundliApplication) getApplication())
                    .loadInterstitialAd();
        }*/
        // END ON 08-12-14
    }

    /*
     * (non-Javadoc)
     *
     * @see com.actionbarsherlock.app.SherlockFragmentActivity#onStop()
     */
    @Override
    protected void onStop() {
        super.onStop();
        //EasyTracker.getInstance().activityStop(this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        try {
            outState.putSerializable(
                    CGlobalVariables.outPutMatchingMasterActSavedBundleKey, CGlobalMatching.getCGlobalMatching());
            getSupportFragmentManager().putFragment(outState,
                    MatchingInputDetailFragment.class.getName(), matchingInputDetailFragment);
            getSupportFragmentManager().putFragment(outState,
                    SearchBirthDetailsFragment.class.getName(), searchBirthDetailsFragment);
            outState.putSerializable("cPlaceBoy", cPlaceBoy);
            outState.putSerializable("cPlaceGirl", cPlaceGirl);

            outState.putSerializable("beanDateIfIssueBoy", beanDateIfIssueBoy);
            outState.putSerializable("beanTimeIfIssueBoy", beanTimeIfIssueBoy);

            outState.putSerializable("beanDateIfIssueGirl", beanDateIfIssueGirl);
            outState.putSerializable("beanTimeIfIssueGirl", beanTimeIfIssueGirl);

            super.onSaveInstanceState(outState);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.FragmentActivity#onResume()
     */
    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (pd != null && pd.isShowing()) {
            try {
                pd.dismiss();
            } catch (IllegalArgumentException e) {
                // do nothing
            }
        }

    }

    private void configureActionBarTabStyle() {

        pageTitles = getResources().getStringArray(
                R.array.input_matching_page_title_list);
    }


    private void setViewPagerAdapter() {
        try {

            adapter = new ViewPagerAdapter(getSupportFragmentManager(), HomeMatchMakingInputScreen.this);
            adapter.addFragment(searchBirthDetailsFragment, pageTitles[0]);
            adapter.addFragment(matchingInputDetailFragment, pageTitles[1]);
            viewPager.setAdapter(adapter);

        } catch (Exception e) {
            // Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }


    private void showCustomDatePickerDialog(BeanDateTime beanDateTime) {

        // Create the dialog
        final Dialog mDateTimeDialog = new Dialog(this);
        // Inflate the root layout
        final RelativeLayout mDateTimeDialogView = (RelativeLayout) getLayoutInflater()
                .inflate(R.layout.date_time_dialog, null);
        // Grab widget instance
        final DateTimePicker mDateTimePicker = (DateTimePicker) mDateTimeDialogView
                .findViewById(R.id.DateTimePicker);
        mDateTimePicker.setDateChangedListener(this);
        // Added by hukum to init date picker
        mDateTimePicker.initDateElements(beanDateTime.getYear(),
                beanDateTime.getMonth(), beanDateTime.getDay(),
                beanDateTime.getHour(), beanDateTime.getMin(),
                beanDateTime.getSecond());
        mDateTimePicker.initData();
        // end
        Button setDateBtn = (Button) mDateTimeDialogView.findViewById(R.id.SetDateTime);
        setDateBtn.setTypeface(regularTypeface);
        if (((AstrosageKundliApplication) HomeMatchMakingInputScreen.this.getApplication())
                .getLanguageCode() == CGlobalVariables.ENGLISH) {
            setDateBtn.setText(getResources().getString(R.string.set).toUpperCase());
        } else {
            setDateBtn.setText(getResources().getString(R.string.set));
        }

        // Update demo TextViews when the "OK" button is clicked
        setDateBtn.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                mDateTimePicker.clearFocus();
                mDateTimeDialog.dismiss();
                if (HomeMatchMakingInputScreen.PERSON_CALLED == HomeMatchMakingInputScreen.PERSON1) {
                    matchingInputDetailFragment.updateBirthDatePerson1(beanDateTimeInput);
                    beanDateIfIssueBoy = beanDateTimeInput;
                }
                if (HomeMatchMakingInputScreen.PERSON_CALLED == HomeMatchMakingInputScreen.PERSON2) {
                    matchingInputDetailFragment.updateBirthDatePerson2(beanDateTimeInput);
                    beanDateIfIssueGirl = beanDateTimeInput;
                }

            }
        });

        Button cancelBtn = (Button) mDateTimeDialogView.findViewById(R.id.CancelDialog);
        cancelBtn.setTypeface(regularTypeface);
        // Cancel the dialog when the "Cancel" button is clicked
        cancelBtn.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                mDateTimePicker.reset();
                mDateTimeDialog.cancel();
            }
        });

        // Reset Date and Time pickers when the "Reset" button is clicked

        Button resetBtn = (Button) mDateTimeDialogView.findViewById(R.id.ResetDateTime);
        if (((AstrosageKundliApplication) HomeMatchMakingInputScreen.this.getApplication())
                .getLanguageCode() == CGlobalVariables.ENGLISH) {
            resetBtn.setText(getResources().getString(R.string.cancel).toUpperCase());
        } else {
            setDateBtn.setText(getResources().getString(R.string.cancel));
        }
        resetBtn.setTypeface(regularTypeface);
        resetBtn.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                mDateTimePicker.reset();
            }
        });

        // Setup TimePicker
        // No title on the dialog window
        mDateTimeDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // Set the dialog content view
        mDateTimeDialog.setContentView(mDateTimeDialogView);
        // Display the dialog
        mDateTimeDialog.show();
    }

    public void showCustomDatePickerDialogAboveHoneyComb(BeanDateTime beanDateTime) {
        final MyDatePickerDialog.OnDateChangedListener myDateSetListener = new MyDatePickerDialog.OnDateChangedListener() {
            @Override
            public void onDateSet(MyDatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                beanDateTimeInput = new BeanDateTime();
                beanDateTimeInput.setYear(year);
                beanDateTimeInput.setMonth(monthOfYear);
                beanDateTimeInput.setDay(dayOfMonth);
                beanDateTimeInput.setHour(0);
                beanDateTimeInput.setMin(0);
                beanDateTimeInput.setSecond(0);
                if (HomeMatchMakingInputScreen.PERSON_CALLED == HomeMatchMakingInputScreen.PERSON1) {
                    matchingInputDetailFragment.updateBirthDatePerson1(beanDateTimeInput);
                    beanDateIfIssueBoy = beanDateTimeInput;
                }
                if (HomeMatchMakingInputScreen.PERSON_CALLED == HomeMatchMakingInputScreen.PERSON2) {
                    matchingInputDetailFragment.updateBirthDatePerson2(beanDateTimeInput);
                    beanDateIfIssueGirl = beanDateTimeInput;
                }
            }
        };


        final MyDatePickerDialog myDatePickerDialog = new MyDatePickerDialog(HomeMatchMakingInputScreen.this, R.style.AppCompatAlertDialogStyle, myDateSetListener, beanDateTime.getMonth(), (beanDateTime.getDay()), (beanDateTime.getYear()), false);
        myDatePickerDialog.setCanceledOnTouchOutside(false);
        //mTimePicker.setTitle("");
        myDatePickerDialog.setIcon(getResources().getDrawable(R.drawable.ic_today_black_icon));
        //if device is tablet than i do not need to set DatePicker and Time Picker to be match Parent
        boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
        if (!tabletSize) {
            WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
            lp.copyFrom(myDatePickerDialog.getWindow().getAttributes());
            lp.width = WindowManager.LayoutParams.MATCH_PARENT;
            lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
            myDatePickerDialog.show();
            myDatePickerDialog.getWindow().setAttributes(lp);
        } else {
            myDatePickerDialog.show();
        }

        try {
            //   mTimePicker.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                myDatePickerDialog.getWindow().setBackgroundDrawableResource(android.R.color.white);
            }
            int divierId = myDatePickerDialog.getContext().getResources()
                    .getIdentifier("android:id/titleDivider", null, null);
            View divider = myDatePickerDialog.findViewById(divierId);
            divider.setVisibility(View.GONE);

        } catch (Exception e) {
            //android.util.//Log.e("EXCEPTION", "openTimePicker: " + e.getMessage());
        }

        Button butOK = (Button) myDatePickerDialog.findViewById(android.R.id.button1);
        Button butCancel = (Button) myDatePickerDialog.findViewById(android.R.id.button2);
        butOK.setText(R.string.set);
        butCancel.setText(R.string.cancel);
        butOK.setTypeface(regularTypeface);
        butCancel.setTypeface(regularTypeface);
    }


    private void showCustomTimePickerDialog(BeanDateTime beanDateTime) {
        Intent intent = new Intent(this, MyNewCustomTimePicker.class);
        intent.putExtra("H", beanDateTime.getHour());
        intent.putExtra("M", beanDateTime.getMin());
        intent.putExtra("S", beanDateTime.getSecond());
        intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, SELECTED_MODULE);
        startActivityForResult(intent, SUB_ACTIVITY_TIME_PICKER);
    }

    @Override
    public void onDateChanged(Calendar c) {
        beanDateTimeInput = new BeanDateTime();
        beanDateTimeInput.setYear(c.get(Calendar.YEAR));
        beanDateTimeInput.setMonth(c.get(Calendar.MONTH));
        beanDateTimeInput.setDay(c.get(Calendar.DAY_OF_MONTH));
        beanDateTimeInput.setHour(c.get(Calendar.HOUR_OF_DAY));
        beanDateTimeInput.setMin(c.get(Calendar.MINUTE));
        beanDateTimeInput.setSecond(c.get(Calendar.SECOND));

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SUB_ACTIVITY_PLACE_SEARCH:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    final BeanPlace place = CUtils.getPlaceObjectFromBundle(bundle);
                    if (HomeMatchMakingInputScreen.PERSON_CALLED == HomeMatchMakingInputScreen.PERSON1) {
                        // set place detail which returned from place activity it
                        // should be set, because in case of activity recreation
                        // user should get updated place value.
                        CGlobalMatching.getCGlobalMatching().getBoyPersonalDetail()
                                .setPlace(place);
                        cPlaceBoy = place;
                        if (!isReferenceIssue) {
                            matchingInputDetailFragment.updateBirthPlacePerson1(place);
                        } else {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        matchingInputDetailFragment.updateBirthPlacePerson1(cPlaceBoy);
                                        matchingInputDetailFragment.updateBirthPlacePerson2(cPlaceGirl);

                                        matchingInputDetailFragment
                                                .updateBirthTimePerson1(beanTimeIfIssueBoy);
                                        matchingInputDetailFragment
                                                .updateBirthTimePerson2(beanTimeIfIssueGirl);

                                        matchingInputDetailFragment
                                                .updateBirthDatePerson1(beanDateIfIssueBoy);
                                        matchingInputDetailFragment
                                                .updateBirthDatePerson2(beanDateIfIssueGirl);
                                    } catch (Exception ex) {
                                        //
                                    }
                                }
                            }, 1000);
                            isReferenceIssue = false;
                        }
                    }
                    if (HomeMatchMakingInputScreen.PERSON_CALLED == HomeMatchMakingInputScreen.PERSON2) {
                        // set place detail which returned from place activity it
                        // should be set, because in case of activity recreation
                        // user should get updated place value.
                        CGlobalMatching.getCGlobalMatching()
                                .getGirlPersonalDetail().setPlace(place);
                        cPlaceGirl = place;
                        if (!isReferenceIssue) {
                            matchingInputDetailFragment.updateBirthPlacePerson2(place);
                        } else {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        matchingInputDetailFragment.updateBirthPlacePerson1(cPlaceBoy);
                                        matchingInputDetailFragment.updateBirthPlacePerson2(cPlaceGirl);

                                        matchingInputDetailFragment
                                                .updateBirthTimePerson1(beanTimeIfIssueBoy);
                                        matchingInputDetailFragment
                                                .updateBirthTimePerson2(beanTimeIfIssueGirl);

                                        matchingInputDetailFragment
                                                .updateBirthDatePerson1(beanDateIfIssueBoy);
                                        matchingInputDetailFragment
                                                .updateBirthDatePerson2(beanDateIfIssueGirl);
                                    } catch (Exception ex) {
                                        //
                                    }
                                }
                            }, 1000);
                            isReferenceIssue = false;
                        }
                    }
                }
                break;
            case SUB_ACTIVITY_TIME_PICKER:
                if (resultCode == RESULT_OK) {
                    Bundle bCTP = data.getExtras();
                    final BeanDateTime beanDateTime = new BeanDateTime();
                    beanDateTime.setHour(bCTP.getInt("H"));
                    beanDateTime.setMin(bCTP.getInt("M"));
                    beanDateTime.setSecond(bCTP.getInt("S"));
                    if (HomeMatchMakingInputScreen.PERSON_CALLED == HomeMatchMakingInputScreen.PERSON1) {
                        if (!isReferenceIssue) {
                            matchingInputDetailFragment.updateBirthTimePerson1(beanDateTime);
                        } else {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        matchingInputDetailFragment.updateBirthPlacePerson1(cPlaceBoy);
                                        matchingInputDetailFragment.updateBirthPlacePerson2(cPlaceGirl);

                                        matchingInputDetailFragment
                                                .updateBirthTimePerson1(beanDateTime);
                                        matchingInputDetailFragment
                                                .updateBirthTimePerson2(beanTimeIfIssueGirl);

                                        matchingInputDetailFragment
                                                .updateBirthDatePerson1(beanDateIfIssueBoy);
                                        matchingInputDetailFragment
                                                .updateBirthDatePerson2(beanDateIfIssueGirl);
                                    } catch (Exception ex) {
                                        //
                                    }
                                }
                            }, 1000);
                            isReferenceIssue = false;
                        }
                    }
                    if (HomeMatchMakingInputScreen.PERSON_CALLED == HomeMatchMakingInputScreen.PERSON2) {
                        if (!isReferenceIssue) {
                            matchingInputDetailFragment.updateBirthTimePerson2(beanDateTime);
                        } else {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    try {

                                        matchingInputDetailFragment.updateBirthPlacePerson1(cPlaceBoy);
                                        matchingInputDetailFragment.updateBirthPlacePerson2(cPlaceGirl);

                                        matchingInputDetailFragment.updateBirthTimePerson1(beanTimeIfIssueBoy);
                                        matchingInputDetailFragment.updateBirthTimePerson2(beanDateTime);

                                        matchingInputDetailFragment.updateBirthDatePerson1(beanDateIfIssueBoy);
                                        matchingInputDetailFragment.updateBirthDatePerson2(beanDateIfIssueGirl);
                                    } catch (Exception ex) {
                                        //
                                    }
                                }
                            }, 1000);
                            isReferenceIssue = false;
                        }
                    }
                }
                break;
            case SUB_ACTIVITY_USER_PREFERENCE:
                if (resultCode == RESULT_OK) {

                }
                break;

            case SUB_ACTIVITY_USER_LOGIN: {
                if (resultCode == RESULT_OK) {
                    Bundle b = data.getExtras();
                    String loginName = b.getString("LOGIN_NAME");
                    String loginPwd = b.getString("LOGIN_PWD");
                    setUserLoginDetails(loginName, loginPwd);
                    if (!isReferenceIssue) {
                        searchBirthDetailsFragment.onActivityResultResponce();
                    } else {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    searchBirthDetailsFragment.onActivityResultResponce();
                                } catch (Exception ex) {
                                    //
                                }
                            }
                        }, 1000);
                        isReferenceIssue = false;
                    }
                }
            }
            break;
            case HomeInputScreen.SUB_ACTIVITY_UPGRADE_PLAN_DIALOG:
            case HomeInputScreen.SUB_ACTIVITY_UPGRADE_PLAN_DIALOG_PURCHSE:
                if (resultCode == RESULT_OK) {
               /* new CalculateNorthMatchResult(
                        CGlobalMatching.getCGlobalMatching()
                                .getBoyPersonalDetail(),
                        CGlobalMatching.getCGlobalMatching()
                                .getGirlPersonalDetail(),
                        CUtils.isConnectedWithInternet(getApplicationContext()))
                        .execute();*/

                    isUpgradeAfterTenChartDialogShown = true;
                    calculateMathingResult(CGlobalMatching.getCGlobalMatching().getBoyPersonalDetail(),
                            CGlobalMatching.getCGlobalMatching().getGirlPersonalDetail());
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void calculateMatching(
            BeanHoroPersonalInfo beanHoroPersonalInfoPerson1,
            BeanHoroPersonalInfo beanHoroPersonalInfoPerson2,
            boolean isSaveDetail) {

        CGlobalMatching.getCGlobalMatching().setBoyPersonalDetail(
                beanHoroPersonalInfoPerson1);
        CGlobalMatching.getCGlobalMatching().setGirlPersonalDetail(
                beanHoroPersonalInfoPerson2);
        if (isSaveDetail) {
            //new CSaveKundliPersonalDetailMatchingSync().execute();
            saveKundliPersonalDetailMatching(CGlobalMatching.getCGlobalMatching().getBoyPersonalDetail(),
                    CGlobalMatching.getCGlobalMatching().getGirlPersonalDetail());
        } else { /* new CalculateNorthMatchResult(beanHoroPersonalInfoPerson1,
                    beanHoroPersonalInfoPerson2,
                    CUtils.isConnectedWithInternet(getApplicationContext()))
                    .execute();*/
            calculateMathingResult(beanHoroPersonalInfoPerson1, beanHoroPersonalInfoPerson2);
        }

    }

    @Override
    public void matchingInputDetailFragmentCreated() {
        if (isEditKundli) {
            matchingInputDetailFragment
                    .updateBirthDetailPerson1(CGlobalMatching
                            .getCGlobalMatching().getBoyPersonalDetail());
            matchingInputDetailFragment
                    .updateBirthDetailPerson2(CGlobalMatching
                            .getCGlobalMatching().getGirlPersonalDetail());
        }

    }

    @Override
    public void selectedKundli(BeanHoroPersonalInfo beanHoroPersonalInfo, int position) {
        this.PERSON_CALLED_FOR_DB_OPERATION = position;
        if (collectDataForPerson < 0) {
            if (this.PERSON_CALLED_FOR_DB_OPERATION == HomeMatchMakingInputScreen.PERSON1) {
                matchingInputDetailFragment
                        .updateBirthDetailPerson1(beanHoroPersonalInfo);

            } else if (this.PERSON_CALLED_FOR_DB_OPERATION == HomeMatchMakingInputScreen.PERSON2) {
                matchingInputDetailFragment
                        .updateBirthDetailPerson2(beanHoroPersonalInfo);
            }
        } else {

            if (this.collectDataForPerson == HomeMatchMakingInputScreen.PERSON1) {
                matchingInputDetailFragment
                        .updateBirthDetailPerson1(beanHoroPersonalInfo);

            } else if (this.collectDataForPerson == HomeMatchMakingInputScreen.PERSON2) {
                matchingInputDetailFragment
                        .updateBirthDetailPerson2(beanHoroPersonalInfo);
            }
        }

        // viewPager.setCurrentItem(INPUT_SCREEN);
        setCurrentView(INPUT_SCREEN, false);
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            beanDateTimeInput = new BeanDateTime();
            beanDateTimeInput.setYear(year);
            beanDateTimeInput.setMonth(monthOfYear);
            beanDateTimeInput.setDay(dayOfMonth);
            beanDateTimeInput.setHour(0);
            beanDateTimeInput.setMin(0);
            beanDateTimeInput.setSecond(0);
            if (HomeMatchMakingInputScreen.PERSON_CALLED == HomeMatchMakingInputScreen.PERSON1) {
                matchingInputDetailFragment
                        .updateBirthDatePerson1(beanDateTimeInput);
                beanDateIfIssueBoy = beanDateTimeInput;
            }
            if (HomeMatchMakingInputScreen.PERSON_CALLED == HomeMatchMakingInputScreen.PERSON2) {
                matchingInputDetailFragment
                        .updateBirthDatePerson2(beanDateTimeInput);
                beanDateIfIssueGirl = beanDateTimeInput;
            }
        }
    };

    private void showAndroidDatePicker(BeanDateTime beanDateTime) {
        final CustomDatePicker dg = new CustomDatePicker(this, mDateSetListener,
                beanDateTime.getYear(), beanDateTime.getMonth(),
                beanDateTime.getDay());

        dg.show();
    }


    @Override
    public void openCalendar(BeanDateTime beanDateTime, int personType) {
        HomeMatchMakingInputScreen.PERSON_CALLED = personType;
        if (CUtils.isUserWantsCustomCalender(HomeMatchMakingInputScreen.this)) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                showCustomDatePickerDialogAboveHoneyComb(beanDateTime);
            } else {
                showCustomDatePickerDialog(beanDateTime);
            }
        } else {
            // Use Custom Date time picker for 7.0 and 7.1. due to eror in Nouget Date time picker (It only uses datePickerMode = Calender)
            if (Build.VERSION.SDK_INT == 24 || Build.VERSION.SDK_INT == 25) {
                showCustomDatePickerDialogAboveHoneyComb(beanDateTime);
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                showAndroidDatePicker(beanDateTime);
            } else {
                showCustomDatePickerDialog(beanDateTime);
            }
        }

    }

    @Override
    public void openTimePicker(BeanDateTime beanDateTime, int personType) {


        HomeMatchMakingInputScreen.PERSON_CALLED = personType;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {


            final MyTimePickerDialog.OnTimeSetListener myTimeSetListener = new MyTimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(com.ojassoft.astrosage.utils.TimePicker view, int hourOfDay, int minute, int seconds) {
                    BeanDateTime beanDateTime = new BeanDateTime();
                    beanDateTime.setHour(hourOfDay);
                    beanDateTime.setMin(minute);
                    beanDateTime.setSecond(seconds);
                    if (HomeMatchMakingInputScreen.PERSON_CALLED == HomeMatchMakingInputScreen.PERSON1) {
                        matchingInputDetailFragment
                                .updateBirthTimePerson1(beanDateTime);
                        beanTimeIfIssueBoy = beanDateTime;
                    }
                    if (HomeMatchMakingInputScreen.PERSON_CALLED == HomeMatchMakingInputScreen.PERSON2) {
                        matchingInputDetailFragment
                                .updateBirthTimePerson2(beanDateTime);
                        beanTimeIfIssueGirl = beanDateTime;
                    }
                }

               /*  @Override
                public void onTimeSet(TimePicker view, int hourOfDay, int minute, int seconds) {
                    // TODO Auto-generated method stub
                    *//**//* time.setText(getString(R.string.time) + String.format("%02d", hourOfDay)+
                    ":" + String.format("%02d", minute) +
                            ":" + String.format("%02d", seconds));*//**//*
                }*/
            };


            final MyTimePickerDialog mTimePicker = new MyTimePickerDialog(HomeMatchMakingInputScreen.this, R.style.AppCompatAlertDialogStyle, myTimeSetListener, beanDateTime.getHour(), (beanDateTime.getMin()), (beanDateTime.getSecond()));
            mTimePicker.setCanceledOnTouchOutside(false);
//            //mTimePicker.setTitle("");
//            Drawable drawable = getResources().getDrawable(R.drawable.timer_title);
//            drawable.setTint(ContextCompat.getColor(this, R.color.black));
//            mTimePicker.setIcon(drawable);
            boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
            if (!tabletSize) {
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(mTimePicker.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                mTimePicker.show();
            }
            mTimePicker.show();

            Button butOK = (Button) mTimePicker.findViewById(android.R.id.button1);
            Button butCancel = (Button) mTimePicker.findViewById(android.R.id.button2);
             try {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    mTimePicker.getWindow().setBackgroundDrawableResource(android.R.color.white);
                }
                int divierId = mTimePicker.getContext().getResources()
                        .getIdentifier("android:id/titleDivider", null, null);
                View divider = mTimePicker.findViewById(divierId);
                divider.setVisibility(View.GONE);

            } catch (Exception e) {
                //android.util.//Log.e("EXCEPTION", "openTimePicker: " + e.getMessage());
            }

            butOK.setText(R.string.set);
            butOK.setTextColor(ContextCompat.getColor(this,R.color.colorPrimary_day_night));

            FontUtils.changeFont(this,butOK, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
            butCancel.setText(R.string.cancel);
            FontUtils.changeFont(this,butCancel, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);

            butCancel.setTextColor(ContextCompat.getColor(this,R.color.black));



        } else {
            showCustomTimePickerDialog(beanDateTime);
        }


    }


        @Override
    public void openSavedKundli(int personType) {
        this.PERSON_CALLED_FOR_DB_OPERATION = personType;
        this.collectDataForPerson = personType;
        // viewPager.setCurrentItem(SAVED_KUNDLI_SCREEN);
        setCurrentView(SAVED_KUNDLI_SCREEN, false);
    }

    @Override
    public void openSearchPlace(BeanPlace beanPlace, int personType) {
        HomeMatchMakingInputScreen.PERSON_CALLED = personType;
        Intent intent = new Intent(HomeMatchMakingInputScreen.this,
                ActPlaceSearch.class);
        intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, SELECTED_MODULE);
        intent.putExtra(CGlobalVariables.PLACE_BEAN_KEY, beanPlace);
        startActivityForResult(intent, SUB_ACTIVITY_PLACE_SEARCH);
    }

    private void gotoOutputScreen(BeanOutMatchmakingNorth beanOutMatchmakingNorth) {
        startActivity(new Intent(this, OutputMatchingMasterActivity.class));
    }

    private void calculateMathingResult(BeanHoroPersonalInfo boyKundli, BeanHoroPersonalInfo girlKundli) {

        CUtils.saveBooleanData(this,CGlobalVariables.UPGRADE_AFTER_TEN_CHART_DIALOG_IS_SHOWN,isUpgradeAfterTenChartDialogShown);
        if (CUtils.isConnectedWithInternet(getApplicationContext())) {
           /* new CalculateNorthMatchResult(boyKundli, girlKundli, CUtils.isConnectedWithInternet(getApplicationContext()))
                    .execute();*/
            calculateNorthMatchResult(boyKundli, girlKundli);
        } else {
            MyCustomToast mct2 = new MyCustomToast(
                    HomeMatchMakingInputScreen.this,
                    HomeMatchMakingInputScreen.this.getLayoutInflater(),
                    HomeMatchMakingInputScreen.this, regularTypeface);
            mct2.show(getResources().getString(R.string.no_internet));
        }

    }


    @Override
    public void newKundli() {
        isMenuItemClicked = true;
        resetInputDetailForm();
    }

    @Override
    public void openKundli() {
        openSavedKundli(PERSON_CALLED);
    }

    private void resetInputDetailForm() {
        BeanHoroPersonalInfo boy = new BeanHoroPersonalInfo();
        boy.setGender("M");
        boy.setPlace(CUtils.getUserDefaultCity(HomeMatchMakingInputScreen.this));
        BeanHoroPersonalInfo girl = new BeanHoroPersonalInfo();
        girl.setGender("F");
        girl.setPlace(CUtils
                .getUserDefaultCity(HomeMatchMakingInputScreen.this));

        CGlobalMatching.getCGlobalMatching().setBoyPersonalDetail(boy);
        CGlobalMatching.getCGlobalMatching().setGirlPersonalDetail(girl);

        matchingInputDetailFragment.updateBirthDetailPerson1(CGlobalMatching
                .getCGlobalMatching().getBoyPersonalDetail());
        matchingInputDetailFragment.updateBirthDetailPerson2(CGlobalMatching
                .getCGlobalMatching().getGirlPersonalDetail());
        //viewPager.setCurrentItem(INPUT_SCREEN);
        setCurrentView(INPUT_SCREEN, false);
    }

    private void openLanguageSelectDialog() {

        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        FragmentManager fm = getSupportFragmentManager();
        Fragment prev = fm.findFragmentByTag("MATCH_INPUT_LANGUAGE");
        if (prev != null) {
            ft.remove(prev);
        }
        ft.addToBackStack(null);

        ChooseLanguageFragmentDailog clfd = new ChooseLanguageFragmentDailog();
        clfd.show(fm, "MATCH_INPUT_LANGUAGE");
        ft.commit();

    }

    @Override
    public void onSelectedLanguage(int languageIndex) {
        if (LANGUAGE_CODE != languageIndex) {
            LANGUAGE_CODE = languageIndex;
            applyChangedLanguageInApplication();
        }

    }

    private void applyChangedLanguageInApplication() {
        /*
         * Intent intent = new Intent(getApplicationContext(),
         * HomeMatchMakingInputScreen.class); //
         * intent.putExtra("LANGUAGE_CODE", CGlobalVariables.MM_LANGUAGE_CODE);
         * startActivity(intent); this.finish();
         */
        startActivity(new Intent(getApplicationContext(), ActAppModule.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        this.finish();
    }


    private void setUserLoginDetails(String loginName, String loginPwd) {
        // matchingHomeInputMenuFrag.updateLoginDetials(true, loginName, loginPwd);
    }

    @Override
    public void setPersonVariableValue(int personType) {
        this.PERSON_CALLED_FOR_DB_OPERATION = personType;
    }

    @Override
    public void oneChartDeleted(long kundliId, boolean isOnlineChart) {
        matchingInputDetailFragment
                .oneChartDeleted_IfThatIsCurrentOneThenDeleteChartIdFromIt(
                        kundliId, isOnlineChart);
    }


    @Override
    public void logoutFromAstroSageCloud(boolean isShowToast) {
        // matchingHomeInputMenuFrag.updateLoginDetials(false, "", "");
        MyCustomToast mct = new MyCustomToast(HomeMatchMakingInputScreen.this,
                HomeMatchMakingInputScreen.this.getLayoutInflater(), HomeMatchMakingInputScreen.this,
                regularTypeface);
        mct.show(getResources().getString(R.string.sign_out_success));
        //searchBirthDetailsFragment.doActionOnLogout();

    }

    public void setDataAfterPurchasePlan(boolean purchaseSilverPlan, String screenId) {


        if (purchaseSilverPlan) {
                    /*startActivity(new Intent(this, ActPurchaseDescription.class)
                            .putExtra("language_code", LANGUAGE_CODE));*/
            //08-feb-2016 work for which screen will be open

            CUtils.gotoProductPlanListUpdated(this,
                    LANGUAGE_CODE, HomeInputScreen.SUB_ACTIVITY_UPGRADE_PLAN_DIALOG_PURCHSE, screenId,"home_match_making_input_screen");
        }// END ON 22-12-2014
        else {

           /* new CalculateNorthMatchResult(
                    CGlobalMatching.getCGlobalMatching()
                            .getBoyPersonalDetail(),
                    CGlobalMatching.getCGlobalMatching()
                            .getGirlPersonalDetail(),
                    CUtils.isConnectedWithInternet(getApplicationContext()))
                    .execute();*/
            calculateMathingResult(CGlobalMatching.getCGlobalMatching().getBoyPersonalDetail(),
                    CGlobalMatching.getCGlobalMatching().getGirlPersonalDetail());
        }
    }


    private void setCurrentView(int index, boolean smoothScroll) {
        viewPager.setCurrentItem(index, smoothScroll);
        if (tabs_input_kundli != null && adapter != null) {
            adapter.setAlpha(index, tabs_input_kundli);
        }
    }

    private void calculateNorthMatchResult(BeanHoroPersonalInfo boyKundli, BeanHoroPersonalInfo girlKundli) {

        CUtils.saveKundliInPreference(HomeMatchMakingInputScreen.this, boyKundli);
        CUtils.saveKundliInPreference(HomeMatchMakingInputScreen.this, girlKundli);
        String url = CGlobalVariables.MM_MATCH_MAKING_URL_NORTH;
        Log.e("LoadMore URL ", url);
        //if (CUtils.isConnectedWithInternet(HomeMatchMakingInputScreen.this)) {
        showProgressBar();
        CUtils.vollyPostRequest(HomeMatchMakingInputScreen.this, url, getParams(boyKundli, girlKundli), CALCULATE_CHART);

      /*  } else {
            hideProgressBar();
            MyCustomToast mct = new MyCustomToast(
                    HomeMatchMakingInputScreen.this,
                    HomeMatchMakingInputScreen.this.getLayoutInflater(),
                    HomeMatchMakingInputScreen.this, Typeface.DEFAULT);
            mct.show(getString(R.string.internet_is_not_working));
        }*/


    }

    public Map<String, String> getParams(BeanHoroPersonalInfo _boy, BeanHoroPersonalInfo _girl) {
        int languageCode = CGlobalVariables.ENGLISH;
        if (SCREEN_CONSTANTS.IS_DEVICE_SUPPORT_UNICODE) {
            languageCode = LANGUAGE_CODE;
        }
        HashMap<String, String> params = new HashMap<String, String>();

        // FOR BOY
        params.put("name1", _boy.getName().trim());
        params.put("day1", String.valueOf(_boy.getDateTime().getDay()).trim());
        params.put("month1", String.valueOf(_boy.getDateTime().getMonth() + 1).trim());
        params.put("year1", String.valueOf(_boy.getDateTime().getYear()).trim());
        params.put("hrs1", String.valueOf(_boy.getDateTime().getHour()).trim());
        params.put("min1", String.valueOf(_boy.getDateTime().getMin()).trim());
        params.put("sec1", String.valueOf(_boy.getDateTime().getSecond()).trim());
        params.put("dst1", String.valueOf(_boy.getDST()));
        params.put("place1", _boy.getPlace().getCityName().trim());
        params.put("LongDeg1", _boy.getPlace().getLongDeg().trim());
        params.put("LongMin1", _boy.getPlace().getLongMin().trim());
        params.put("LongSec1", _boy.getPlace().getLongSec().trim());
        params.put("LongEW1", _boy.getPlace().getLongDir().trim());
        params.put("LatDeg1", _boy.getPlace().getLatDeg().trim());
        params.put("LatMin1", _boy.getPlace().getLatMin().trim());
        params.put("LatSec1", _boy.getPlace().getLatSec().trim());
        params.put("LatNS1", _boy.getPlace().getLatDir().trim());
        params.put("timeZone1", String.valueOf(_boy.getPlace().getTimeZoneValue()).trim());
        // END BOY

        // FOR GIRL
        params.put("name2", _girl.getName().trim());
        params.put("day2", String.valueOf(_girl.getDateTime().getDay()).trim());
        params.put("month2", String.valueOf(_girl.getDateTime().getMonth() + 1).trim());
        params.put("year2", String.valueOf(_girl.getDateTime().getYear()).trim());
        params.put("hrs2", String.valueOf(_girl.getDateTime().getHour()).trim());
        params.put("min2", String.valueOf(_girl.getDateTime().getMin()).trim());
        params.put("sec2", String.valueOf(_girl.getDateTime().getSecond()).trim());
        params.put("dst2", String.valueOf(_girl.getDST()));
        // nameValuePairs.add(new BasicNameValuePair("dst2","0"));
        // FOR GIRL CITY DATA
        params.put("place2", _girl.getPlace().getCityName().trim());
        params.put("LongDeg2", _girl.getPlace().getLongDeg().trim());
        params.put("LongMin2", _girl.getPlace().getLongMin().trim());
        params.put("LongSec2", _girl.getPlace().getLongSec().trim());
        params.put("LongEW2", _girl.getPlace().getLongDir().trim());
        params.put("LatDeg2", _girl.getPlace().getLatDeg().trim());
        params.put("LatMin2", _girl.getPlace().getLatMin().trim());
        params.put("LatSec2", _girl.getPlace().getLatSec().trim());
        params.put("LatNS2", _girl.getPlace().getLatDir().trim());
        params.put("timeZone2", String.valueOf(_girl.getPlace().getTimeZoneValue()).trim());

        params.put("LanguageCode", String.valueOf(languageCode));//ADDED BY BIJENDRA ON 2-02-FEB-2013


        return params;
    }

    private Map<String, String> getParamsForSaveCharts(BeanHoroPersonalInfo boyKundli, BeanHoroPersonalInfo girlKundli) {


        HashMap<String, String> params = new HashMap<String, String>();

        params.put(CGlobalVariables.KEY_USER_ID, CUtils.replaceEmailChar(CUtils.getUserName(HomeMatchMakingInputScreen.this)));
        params.put(CGlobalVariables.KEY_PASSWORD, CUtils.getUserPassword(HomeMatchMakingInputScreen.this));
      /*  params.put("UserId", "mohitgarg1985");
        params.put("Password", "123");*/

        params.put("isapi", "1");
        params.put("osname", "Android");
        params.put("key", CUtils.getApplicationSignatureHashCode(HomeMatchMakingInputScreen.this));
        //For boy
        params.put("nameboy", boyKundli.getName().trim());
        params.put("sexboy", boyKundli.getGender().trim());
        params.put("dayboy", String.valueOf(boyKundli.getDateTime().getDay()).trim());
        params.put("monthboy", String.valueOf(boyKundli.getDateTime().getMonth() + 1).trim());
        params.put("yearboy", String.valueOf(boyKundli.getDateTime().getYear()).trim());
        params.put("hrsboy", String.valueOf(boyKundli.getDateTime().getHour()).trim());
        params.put("minboy", String.valueOf(boyKundli.getDateTime().getMin()).trim());
        params.put("secboy", String.valueOf(boyKundli.getDateTime().getSecond()).trim());
        params.put("placeboy", boyKundli.getPlace().getCityName().trim());
        params.put("longdegboy", boyKundli.getPlace().getLongDeg().trim());
        params.put("longminboy", boyKundli.getPlace().getLongMin().trim());
        params.put("longewboy", boyKundli.getPlace().getLongDir().trim());
        params.put("latdegboy", boyKundli.getPlace().getLatDeg().trim());
        params.put("latminboy", boyKundli.getPlace().getLatMin().trim());
        params.put("latnsboy", boyKundli.getPlace().getLatDir().trim());
        params.put("ayanamsaboy", String.valueOf(boyKundli.getAyanIndex()).trim());
        params.put("timezoneboy", String.valueOf(boyKundli.getPlace().getTimeZoneValue()).trim());
        params.put("dstboy", String.valueOf(boyKundli.getDST()).trim());

        if (boyKundli.getOnlineChartId().trim().equals("-1")) {
            params.put("chartidboy", "");
        } else {
            params.put("chartidboy", boyKundli.getOnlineChartId().trim());
        }
        params.put("kphnboy", String.valueOf(boyKundli.getHoraryNumber()));

        //For Girl

        params.put("namegirl", girlKundli.getName().trim());
        params.put("sexgirl", girlKundli.getGender().trim());
        params.put("daygirl", String.valueOf(girlKundli.getDateTime().getDay()).trim());
        params.put("monthgirl", String.valueOf(girlKundli.getDateTime().getMonth() + 1).trim());
        params.put("yeargirl", String.valueOf(girlKundli.getDateTime().getYear()).trim());
        params.put("hrsgirl", String.valueOf(girlKundli.getDateTime().getHour()).trim());
        params.put("mingirl", String.valueOf(girlKundli.getDateTime().getMin()).trim());
        params.put("secgirl", String.valueOf(girlKundli.getDateTime().getSecond()).trim());
        params.put("placegirl", girlKundli.getPlace().getCityName().trim());
        params.put("longdeggirl", girlKundli.getPlace().getLongDeg().trim());
        params.put("longmingirl", girlKundli.getPlace().getLongMin().trim());
        params.put("longewgirl", girlKundli.getPlace().getLongDir().trim());
        params.put("latdeggirl", girlKundli.getPlace().getLatDeg().trim());
        params.put("latmingirl", girlKundli.getPlace().getLatMin().trim());
        params.put("latnsgirl", girlKundli.getPlace().getLatDir().trim());
        params.put("ayanamsagirl", String.valueOf(girlKundli.getAyanIndex()).trim());
        params.put("timezonegirl", String.valueOf(girlKundli.getPlace().getTimeZoneValue()).trim());
        params.put("dstgirl", String.valueOf(girlKundli.getDST()).trim());
        if (girlKundli.getOnlineChartId().trim().equals("-1")) {
            params.put("chartidgirl", "");
        } else {
            params.put("chartidgirl", girlKundli.getOnlineChartId().trim());
        }
        params.put("kphngirl", String.valueOf(girlKundli.getHoraryNumber()));
        return params;
    }

    @Override
    public void onResponse(String response, int method) {

        if (method == CALCULATE_CHART) {
            Log.e("response>>", response);
            hideProgressBar();
            try {
                Log.e("LoadMore URL ", response);
               // response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
            } catch (Exception e) {

            }
            BeanOutMatchmakingNorth beanOutMatchmakingNorth = separatOutoutString(response);
            try {
                if (beanOutMatchmakingNorth != null) {
                    CGlobalMatching.getCGlobalMatching()
                            .setBeanOutMatchmakingNorth(beanOutMatchmakingNorth);
                    gotoOutputScreen(beanOutMatchmakingNorth);
                } else {
                    MyCustomToast mct = new MyCustomToast(
                            HomeMatchMakingInputScreen.this,
                            HomeMatchMakingInputScreen.this.getLayoutInflater(),
                            HomeMatchMakingInputScreen.this, Typeface.DEFAULT);
                    mct.show(getResources().getString(R.string.matching_result));
                }
            } catch (Exception ex) {
                MyCustomToast mct = new MyCustomToast(
                        HomeMatchMakingInputScreen.this,
                        HomeMatchMakingInputScreen.this.getLayoutInflater(),
                        HomeMatchMakingInputScreen.this, Typeface.DEFAULT);
                mct.show(getResources().getString(R.string.matching_result));
            }
        } else if (method == SAVE_CHRATS) {
            doActionAfterSaveChart(response);
        }
    }

    @Override
    public void onError(VolleyError error) {
        Toast.makeText(this, "Error : "+error, Toast.LENGTH_SHORT).show();
        hideProgressBar();
    }

    /**
     * show Progress Bar
     */
    public void showProgressBar() {
        if (pd == null) {
            pd = new CustomProgressDialog(HomeMatchMakingInputScreen.this, regularTypeface);
        }
        pd.setCanceledOnTouchOutside(false);
        if (!pd.isShowing()) {
            pd.show();
        }
    }

    /**
     * hide Progress Bar
     */
    public void hideProgressBar() {
        try {
            if (pd != null & pd.isShowing())
                pd.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * this method separates xml data in local properties.
     *
     * @param strData
     */
    private BeanOutMatchmakingNorth separatOutoutString(String strData) {
        BeanOutMatchmakingNorth bommn = new BeanOutMatchmakingNorth();
        try {
            if (!TextUtils.isEmpty(strData)) {
                bommn.setMatchPointVarna(Double.valueOf(strData.substring(strData.indexOf("<Varna>") +
                        "<Varna>".length(), strData.indexOf("</Varna>"))));
                bommn.setMatchPointVasya(Double.valueOf(strData.substring(strData.indexOf("<Vasya>") +
                        "<Vasya>".length(), strData.indexOf("</Vasya>"))));
                bommn.setMatchPointTara(Double.valueOf(strData.substring(strData.indexOf("<Tara>") +
                        "<Tara>".length(), strData.indexOf("</Tara>"))));
                bommn.setMatchPointYoni(Double.valueOf(strData.substring(strData.indexOf("<Yoni>") +
                        "<Yoni>".length(), strData.indexOf("</Yoni>"))));
                bommn.setMatchPointMaitri(Double.valueOf(strData.substring(strData.indexOf("<Maitri>") +
                        "<Maitri>".length(), strData.indexOf("</Maitri>"))));
                bommn.setMatchPointBhakoot(Double.valueOf(strData.substring(strData.indexOf("<Bhakoot>") +
                        "<Bhakoot>".length(), strData.indexOf("</Bhakoot>"))));
                if(strData.contains("<Nadi>")) {
                    bommn.setMatchPointNadi(Double.valueOf(strData.substring(strData.indexOf("<Nadi>") +
                            "<Nadi>".length(), strData.indexOf("</Nadi>"))));
                }
                bommn.setMatchPointGana(Double.valueOf(strData.substring(strData.indexOf("<Gana>") +
                        "<Gana>".length(), strData.indexOf("</Gana>"))));
                bommn.setConclusion(strData.substring(strData.indexOf("<Conclusion>") + "<Conclusion>".length(),
                        strData.indexOf("</Conclusion>")));
                bommn.setBoyRasiNumber(strData.substring(strData.indexOf("<BoyRasiNumber>") + "<BoyRasiNumber>".length(),
                        strData.indexOf("</BoyRasiNumber>")));
                bommn.setGirlRasiNumber(strData.substring(strData.indexOf("<GirlRasiNumber>") + "<GirlRasiNumber>".length(),
                        strData.indexOf("</GirlRasiNumber>")));
                bommn.setVarnaPrediction(strData.substring(strData.indexOf("<Varna-Prediction>") + "<Varna-Prediction>".length(),
                        strData.indexOf("</Varna-Prediction>")));
                bommn.setVasyaPrediction(strData.substring(strData.indexOf("<Vasya-Prediction>") + "<Vasya-Prediction>".length(),
                        strData.indexOf("</Vasya-Prediction>")));
                bommn.setTaraPrediction(strData.substring(strData.indexOf("<Tara-Prediction>") + "<Tara-Prediction>".length(),
                        strData.indexOf("</Tara-Prediction>")));
                bommn.setYoniPrediction(strData.substring(strData.indexOf("<Yoni-Prediction>") + "<Yoni-Prediction>".length(),
                        strData.indexOf("</Yoni-Prediction>")));
                bommn.setMaitriPrediction(strData.substring(strData.indexOf("<Maitri-Prediction>") + "<Maitri-Prediction>".length(),
                        strData.indexOf("</Maitri-Prediction>")));
                bommn.setBhakootPrediction(strData.substring(strData.indexOf("<Bhakoot-Prediction>") + "<Bhakoot-Prediction>".length(),
                        strData.indexOf("</Bhakoot-Prediction>")));
                bommn.setNadiPrediction(strData.substring(strData.indexOf("<Nadi-Prediction>") + "<Nadi-Prediction>".length(),
                        strData.indexOf("</Nadi-Prediction>")));
                bommn.setGanaPrediction(strData.substring(strData.indexOf("<Gana-Prediction>") + "<Gana-Prediction>".length(),
                        strData.indexOf("</Gana-Prediction>")));
                bommn.setBoyMoonDegree(strData.substring(strData.indexOf("<MoonDegreeOfBoy>") + "<MoonDegreeOfBoy>".length(),
                        strData.indexOf("</MoonDegreeOfBoy>")));
                bommn.setGirlMoonDegree(strData.substring(strData.indexOf("<MoonDegreeOfGirl>") + "<MoonDegreeOfGirl>".length(),
                        strData.indexOf("</MoonDegreeOfGirl>")));
                bommn.setBoyMangalDosha(strData.substring(strData.indexOf("<BoyMangalDosha>") + "<BoyMangalDosha>".length(),
                        strData.indexOf("</BoyMangalDosha>")));
                bommn.setGirlMangalDosha(strData.substring(strData.indexOf("<GirlMangalDosha>") + "<GirlMangalDosha>".length(),
                        strData.indexOf("</GirlMangalDosha>")));
            } else {
                bommn = null;
            }

        } catch (Exception e) {
            bommn = null;
        }
        return bommn;
    }

    private void saveKundliPersonalDetailMatching(BeanHoroPersonalInfo boyKundli, BeanHoroPersonalInfo girlKundli) {
        long boyLocalChartId = saveKundliInLocalDB(boyKundli);
        long girlLocalChartId = saveKundliInLocalDB(girlKundli);
        boyKundli.setLocalChartId(boyLocalChartId);
        girlKundli.setLocalChartId(girlLocalChartId);
        if (CUtils.isConnectedWithInternet(HomeMatchMakingInputScreen.this)) {
            if (CUtils.isUserLogedIn(getApplicationContext())) {
                String url = CGlobalVariables.saveMatchingChart;
                showProgressBar();
                CUtils.vollyPostRequest(HomeMatchMakingInputScreen.this,  url, getParamsForSaveCharts(boyKundli, girlKundli), SAVE_CHRATS);
            } else {
                calculateMathingResult(CGlobalMatching.getCGlobalMatching().getBoyPersonalDetail(),
                        CGlobalMatching.getCGlobalMatching().getGirlPersonalDetail());
            }
        } else {
            hideProgressBar();
            MyCustomToast mct = new MyCustomToast(
                    HomeMatchMakingInputScreen.this,
                    HomeMatchMakingInputScreen.this.getLayoutInflater(),
                    HomeMatchMakingInputScreen.this, Typeface.DEFAULT);
            mct.show(getString(R.string.internet_is_not_working));
        }
    }


    private void doActionAfterSaveChart(String response) {
        try {
            if (!TextUtils.isEmpty(response)) {
                BeanHoroPersonalInfo boyKundli = CGlobalMatching.getCGlobalMatching().getBoyPersonalDetail();
                BeanHoroPersonalInfo girlKundli = CGlobalMatching.getCGlobalMatching().getGirlPersonalDetail();
                JSONArray jsonArray = new JSONArray(response);
                if (jsonArray != null) {
                    JSONObject resultObj = jsonArray.getJSONObject(0);
                    JSONObject boyObj = jsonArray.getJSONObject(1);
                    JSONObject girlObj = jsonArray.getJSONObject(2);
                    if (resultObj != null) {
                        String result = resultObj.getString("Result");
                        if (result.equals("1")) {
                            boolean isBoysKundliSaved = parseSaveKundliData(boyObj, boyKundli);
                            boolean isGirlKundliSaved = parseSaveKundliData(girlObj, girlKundli);
                            //If chart can not saved in current plan
                            if (!isBoysKundliSaved || !isGirlKundliSaved) {
                                hideProgressBar();
                                CUtils.gotoshowCompletedFreeChartPlanScreen(
                                        HomeMatchMakingInputScreen.this, LANGUAGE_CODE,
                                        HomeInputScreen.SUB_ACTIVITY_UPGRADE_PLAN_DIALOG, 6);
                            } else {
                                calculateMathingResult(CGlobalMatching.getCGlobalMatching().getBoyPersonalDetail(),
                                        CGlobalMatching.getCGlobalMatching().getGirlPersonalDetail());
                            }
                        } else {
                            isShowToast = false;
                            calculateMathingResult(CGlobalMatching.getCGlobalMatching().getBoyPersonalDetail(),
                                    CGlobalMatching.getCGlobalMatching().getGirlPersonalDetail());
                            saveMsg = getResources().getString(R.string.chart_not_saved_on_server);
                        }
                    }
                } else {
                    isShowToast = false;
                    calculateMathingResult(CGlobalMatching.getCGlobalMatching().getBoyPersonalDetail(),
                            CGlobalMatching.getCGlobalMatching().getGirlPersonalDetail());
                    saveMsg = getResources().getString(R.string.chart_not_saved_on_server);
                }
            } else {
                isShowToast = false;
                calculateMathingResult(CGlobalMatching.getCGlobalMatching().getBoyPersonalDetail(),
                        CGlobalMatching.getCGlobalMatching().getGirlPersonalDetail());
                saveMsg = getResources().getString(R.string.chart_not_saved_on_server);
            }
        } catch (Exception e) {
            isShowToast = false;
            if (e.getMessage() != null) {
                saveMsg = e.getMessage();
            }
        }
        if (!isShowToast && !TextUtils.isEmpty(saveMsg)) {
            MyCustomToast mct = new MyCustomToast(
                    HomeMatchMakingInputScreen.this,
                    HomeMatchMakingInputScreen.this.getLayoutInflater(),
                    HomeMatchMakingInputScreen.this, Typeface.DEFAULT);
            mct.show(saveMsg);
        }
    }

    private boolean parseSaveKundliData(JSONObject boyObj, BeanHoroPersonalInfo boyKundli) {
        boolean isCalculateResult = true;
        try {
            String bResultCode = boyObj.getString("ResultCode");
            //String gResultCode = boyObj.getString("ResultCode");
            if (bResultCode.equals("2") || bResultCode.equals("5")) {
                String onlineChartid = boyObj.getString("OnlineChartId");
                boyKundli.setOnlineChartId(onlineChartid);
                saveKundliInLocalDB(boyKundli);
                isCalculateResult = true;
                isShowToast = false;
                saveMsg = getResources().getString(R.string.chart_saved_on_server);
            } else if ((bResultCode.equals("3") || bResultCode.equals("4"))) {
                isCalculateResult = false;
                isShowToast = true;
                saveMsg = "";
            } else {
                isShowToast = false;
                saveMsg = getResources().getString(R.string.chart_not_saved_on_server);
            }
        } catch (Exception e) {
            isShowToast = false;
            if (e.getMessage() != null) {
                saveMsg = e.getMessage();
            }

        }
        return isCalculateResult;
    }


    private long saveKundliInLocalDB(BeanHoroPersonalInfo beanHoroPersonalInfo) {
        long localChartID = -1;
        try {
            localChartID = new ControllerManager()
                    .addEditHoroPersonalInfoOperation(getApplicationContext(), beanHoroPersonalInfo);
        } catch (Exception e) {

        }
        return localChartID;
    }
}
