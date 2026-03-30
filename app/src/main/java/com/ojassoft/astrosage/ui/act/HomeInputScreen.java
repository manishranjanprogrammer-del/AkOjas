package com.ojassoft.astrosage.ui.act;

import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_KUNDALI_DETAILS;
import static com.ojassoft.astrosage.utils.CGlobalVariables.PAYMENT_TYPE_SERVICE;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.android.billingclient.api.BillingClient;
import com.android.billingclient.api.BillingFlowParams;
import com.android.billingclient.api.BillingResult;
import com.android.billingclient.api.ProductDetails;
import com.android.billingclient.api.Purchase;
import com.android.billingclient.api.QueryProductDetailsParams;
import com.android.volley.Cache;
import com.android.volley.RequestQueue;
import com.android.volley.VolleyError;
import com.google.android.material.tabs.TabLayout;
import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.libojassoft.android.misc.SendDataBackToComponent;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanDateTime;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.beans.BeanPlace;
import com.ojassoft.astrosage.beans.CScreenHistoryItemCollectionStack;
import com.ojassoft.astrosage.billing.BillingEventHandler;
import com.ojassoft.astrosage.customadapters.ViewPagerAdapter;
import com.ojassoft.astrosage.jinterface.IAskCallback;
import com.ojassoft.astrosage.jinterface.IBirthDetailInputFragment;
import com.ojassoft.astrosage.jinterface.IChooseLanguageFragment;
import com.ojassoft.astrosage.jinterface.IChoosePayOption;
import com.ojassoft.astrosage.jinterface.IPaymentFailed;
import com.ojassoft.astrosage.jinterface.IPermissionCallback;
import com.ojassoft.astrosage.jinterface.ISearchBirthDetailsFragment;
import com.ojassoft.astrosage.jinterface.IUpdateMenus;
import com.ojassoft.astrosage.misc.CalculateKundli;
import com.ojassoft.astrosage.misc.CustomDatePicker;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.misc.GetCheckSum;
import com.ojassoft.astrosage.misc.SendUserPurchaseReportForServiceToServerForAstroChat;
import com.ojassoft.astrosage.misc.VerificationServiceForInAppBillingChat;
import com.ojassoft.astrosage.model.AppPurchaseDataSaveModelClass;
import com.ojassoft.astrosage.model.AstrologerServiceInfo;
import com.ojassoft.astrosage.model.ServicelistModal;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.OnVolleyResultListener;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.customcontrols.DateTimePicker;
import com.ojassoft.astrosage.ui.customcontrols.DateTimePicker.DateWatcher;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.ui.customcontrols.MyNewCustomTimePicker;
import com.ojassoft.astrosage.ui.fragments.BirthDetailInputFragment;
import com.ojassoft.astrosage.ui.fragments.ChoosePayOptionFragmentDailog;
import com.ojassoft.astrosage.ui.fragments.ChooseServicePayOPtionDialog;
import com.ojassoft.astrosage.ui.fragments.HomeNavigationDrawerFragment;
import com.ojassoft.astrosage.ui.fragments.PaymentFailedDailogFrag;
import com.ojassoft.astrosage.ui.fragments.SearchBirthDetailsFragment;
import com.ojassoft.astrosage.ui.fragments.astrochatfragment.model.MessageDecode;
import com.ojassoft.astrosage.ui.fragments.astrochatfragment.savedata.SaveDataInternalStorage;
import com.ojassoft.astrosage.utils.CGlobal;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.MyDatePicker;
import com.ojassoft.astrosage.utils.MyDatePickerDialog;
import com.ojassoft.astrosage.utils.MyTimePickerDialog;
import com.ojassoft.astrosage.utils.UserEmailFetcher;
import com.ojassoft.astrosage.varta.model.UserProfileData;
import com.ojassoft.astrosage.varta.ui.activity.FirstTimeProfileDetailsActivity;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.paytm.pgsdk.PaytmOrder;
import com.paytm.pgsdk.PaytmPaymentTransactionCallback;
import com.paytm.pgsdk.TransactionManager;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeInputScreen extends BaseInputActivity implements IPaymentFailed,
        IBirthDetailInputFragment, ISearchBirthDetailsFragment, DateWatcher,
        IChooseLanguageFragment, IUpdateMenus, IChoosePayOption, IAskCallback,
        PaymentResultListener, IPermissionCallback, SendDataBackToComponent,
        OnVolleyResultListener, BillingEventHandler {

    public static final String SKU_ASKQUESTION_PLAN = "ask_a_question";
    static final int resultCodeCancel = 110;
    static final int resultCodeTryAgain = 111;
    static final int requestCodePaymentFailureScreen = 112;
    public static boolean drawerNewKundliOptionClicked = false;
    public static int ASK_QUESTION_PLAN = 0;
    // SlidingMenu slidingMenu;
    final int SAVED_KUNDLI_SCREEN = 0;
    final int INPUT_SCREEN = 1;
    /**
     * This is flag to make app online / Offline
     */
    private final boolean IS_APP_ONLINE = true;
    private final String payMode = "Google";
    public String[] pageTitles;
    public ViewPager viewPager;
    // ModulePagerAdapter modulePagerAdapter;
    public boolean isEditKundli = false;
    public SearchBirthDetailsFragment searchBirthDetailsFragment;
    public Toolbar toolBar_InputKundli;
    public View appbarAppModule;
    public int SELECTED_MODULE;
    public boolean ASK_QUESTION_QUERY_DATA, BHRIGOO_QUERY_DATA, NUMROLOGY_QUERY_DATA, VARTA_PROFILE_QUERY_DATA, ASTROSAGE_CHAT_QUERY_DATA, ASTRO_SERVICE_QUERY_DATA, ASTRO_PRODUCT_DATA;
    public ServicelistModal itemdetails;
    // CONSTANTS
    String order_Id = "";
    String chat_Id = "";
    String payStatus = "0";
    String payId = "";
    BirthDetailInputFragment birthDetailInputFragment;
    private final TimePickerDialog.OnTimeSetListener mTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // Toast.makeText(HomeInputScreen.this, hourOfDay + " >>" + minute, Toast.LENGTH_SHORT).show();
            BeanDateTime beanDateTime = new BeanDateTime();
            beanDateTime.setHour(hourOfDay);
            beanDateTime.setMin(minute);
            beanDateTime.setSecond(0);
            birthDetailInputFragment.updateBirthTime(beanDateTime);
        }
    };
    // KundliHomeInputMenuFrag kundliHomeInputMenuFrag;
    HomeNavigationDrawerFragment drawerFragment;
    boolean isChooseProfileAstroChat;
    TextView titleTextView;
    ImageView homeImageView;
    ImageView moreImageView;
    ImageView toggleImageView;
    ViewPagerAdapter adapter;
    ArrayList<String> arayaskquestionPlan = new ArrayList<String>(5);
    ProductDetails askAQSkuDetails;
    String[] errorResponse = {"Success",
            "Billing response result user canceled",
            "Network connection is down",
            "Billing API version is not supported for the type requested",
            "Requested product is not available for purchase",
            "Invalid arguments provided to the API",
            "Fatal error during the API action",
            "Failure to purchase since item is already owned",
            "Failure to consume since item is not owned"};
    String developerPayload = "", purchaseData = "", signature = "";
    int price_amount_micros = 5, price_currency_code = 6;
    int price_amount = 2;
    String chatID = "";
    boolean isFreeQuestion = true;
    CustomProgressDialog pd;
    String priceValue = "";
    String dumpDataString = "";
    private TabLayout tabs_input_kundli;
    private BeanHoroPersonalInfo beanAskAQue;
    //public static Intent i1;
    private String email_id_for_question = "";
    private String mobile_num_for_question = "";
    private String query_for_question = "";
    private AstrologerServiceInfo astrologerServiceInfo;
    private String fullJsonDataObj = "";
    private boolean isPaymentDone = true, isUserHAsPlanBoolean = false;
    private java.text.DateFormat mDateFormat;
    //private boolean isMessageNeedToSend=true;
    private int layoutPosition = 0;
    private ArrayList<MessageDecode> chatMessageArrayList;
    private String messageChatID = "";
    private boolean isReferenceIssue = false;
    private BeanDateTime beanDateTimeIfIssue = new BeanDateTime();
    // private int LANGUAGE_CODE;
    private final DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {

        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            BeanDateTime beanDateTime = new BeanDateTime();
            beanDateTime.setYear(year);
            beanDateTime.setMonth(monthOfYear);
            beanDateTime.setDay(dayOfMonth);
            beanDateTime.setHour(0);
            beanDateTime.setMin(0);
            beanDateTime.setSecond(0);
            birthDetailInputFragment.updateBirthDate(beanDateTime);
            beanDateTimeIfIssue = beanDateTime;
        }
    };
    private BeanDateTime beanTimeIfIssue = new BeanDateTime();
    private BeanHoroPersonalInfo openKundliBirthDetail;

    public HomeInputScreen() {
        super(R.string.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null) {
            try {
                //Check come from AstroChat
                isChooseProfileAstroChat = getIntent().getBooleanExtra("USERPROFILESELECTION", false);
                CGlobal cGlobal = (CGlobal) savedInstanceState
                        .getSerializable(CGlobalVariables.outPutMasterActSavedBundleKey);
                CGlobal.setCGlobalObject(cGlobal);

                beanDateTimeIfIssue = (BeanDateTime) savedInstanceState
                        .getSerializable("beanDateTimeIfIssue");

                beanTimeIfIssue = (BeanDateTime) savedInstanceState
                        .getSerializable("beanTimeIfIssue");

                birthDetailInputFragment = (BirthDetailInputFragment) getSupportFragmentManager()
                        .getFragment(savedInstanceState,
                                BirthDetailInputFragment.class.getName());
                searchBirthDetailsFragment = (SearchBirthDetailsFragment) getSupportFragmentManager()
                        .getFragment(savedInstanceState,
                                SearchBirthDetailsFragment.class.getName());
            } catch (Exception e) {
                // e.printStackTrace();
                searchBirthDetailsFragment = new SearchBirthDetailsFragment();
                birthDetailInputFragment = new BirthDetailInputFragment();
                isReferenceIssue = true;
            }
        } else {
            searchBirthDetailsFragment = new SearchBirthDetailsFragment();
            birthDetailInputFragment = new BirthDetailInputFragment();
        }
        //call This method is uset to give recent id if not available in RecentSearchKundli
        CUtils.addRecentIdsInChartIfNotAvailableInRecentSearchCharts(HomeInputScreen.this);
        chatMessageArrayList = CUtils.getDataFromPrefrence(HomeInputScreen.this);
        //tejinder
        mDateFormat = DateFormat.getMediumDateFormat(HomeInputScreen.this);
        /*typeface = CUtils.getUserSelectedLanguageFontType(
                getApplicationContext(),
                CUtils.getLanguageCodeFromPreference(getApplicationContext()));*/
        // i1= getIntent();

        isEditKundli = getIntent().getBooleanExtra("IS_EDIT_KUNDLI", false);
        int pagerIndex = getIntent().getIntExtra("PAGER_INDEX", INPUT_SCREEN);// ADDED
        //Getting intent ASK_QUESTION_QUERY_DATA from ActAskquestionDescription
        openKundliBirthDetail = (BeanHoroPersonalInfo) getIntent().getSerializableExtra(CGlobalVariables.BIRTH_DETAILS_KEY);
        ASK_QUESTION_QUERY_DATA = getIntent().getBooleanExtra(CGlobalVariables.ASK_QUESTION_QUERY_DATA, false);
        NUMROLOGY_QUERY_DATA = getIntent().getBooleanExtra(CGlobalVariables.NUMROLOGY_QUERY_DATA, false);
        VARTA_PROFILE_QUERY_DATA = getIntent().getBooleanExtra(CGlobalVariables.VARTA_PROFILE_QUERY_DATA, false);
        BHRIGOO_QUERY_DATA = getIntent().getBooleanExtra(CGlobalVariables.BHRIGOO_QUERY_DATA, false);
        ASTRO_SERVICE_QUERY_DATA = getIntent().getBooleanExtra(CGlobalVariables.ASTRO_SERVICE_QUERY_DATA, false);
        ASTROSAGE_CHAT_QUERY_DATA = getIntent().getBooleanExtra(CGlobalVariables.ASTROSAGE_CHAT_QUERY_DATA, false);
        ASTRO_PRODUCT_DATA = getIntent().getBooleanExtra(CGlobalVariables.ASTRO_PRODUCT_DATA, false);
        messageChatID = getIntent().getStringExtra(CGlobalVariables.key_messageChatID);
        layoutPosition = getIntent().getIntExtra(CGlobalVariables.key_layoutPosition, 0);

        layoutPosition = getIntent().getIntExtra(CGlobalVariables.key_layoutPosition, 0);

        if (getIntent().getExtras() != null && getIntent().getExtras().containsKey(CGlobalVariables.IS_USER_HAS_PLAN)) {
            isUserHAsPlanBoolean = getIntent().getBooleanExtra(CGlobalVariables.IS_USER_HAS_PLAN, false);
        }

        if (messageChatID == null) {
            messageChatID = "";
            layoutPosition = 0;
        }
        // BY
        // DEEPAK
        // ON
        // 31-10-2014
        setContentView(R.layout.lay_input_kundli_screen);
        CUtils.applyEdgeToEdgeInsets(this);
        setBillingEventHandler(this);
        appbarAppModule = findViewById(R.id.appbarAppModule);
        toolBar_InputKundli = findViewById(R.id.tool_barAppModule);
        setSupportActionBar(toolBar_InputKundli);

        if (!ASK_QUESTION_QUERY_DATA && !BHRIGOO_QUERY_DATA && !NUMROLOGY_QUERY_DATA && !VARTA_PROFILE_QUERY_DATA && !ASTROSAGE_CHAT_QUERY_DATA && !ASTRO_SERVICE_QUERY_DATA && !ASTRO_PRODUCT_DATA) {
            drawerFragment = (HomeNavigationDrawerFragment) getSupportFragmentManager().
                    findFragmentById(R.id.myDrawerFrag);
            drawerFragment.setup(R.id.myDrawerFrag, findViewById(R.id.drawerLayout), toolBar_InputKundli, getDrawerListItem(), getDrawerListItemIcon(), getDrawerListItemIndex());
            drawerFragment.setDrawerLockMode(true);
        } else {
            drawerFragment = (HomeNavigationDrawerFragment) getSupportFragmentManager().
                    findFragmentById(R.id.myDrawerFrag);
            drawerFragment.setup(R.id.myDrawerFrag, findViewById(R.id.drawerLayout), toolBar_InputKundli, getDrawerListItem(), getDrawerListItemIcon(), getDrawerListItemIndex());
            drawerFragment.setDrawerLockMode(false);
        }

        toggleImageView = findViewById(R.id.ivToggleImage);
        titleTextView = findViewById(R.id.tvTitle);
        homeImageView = findViewById(R.id.imgHome);
        moreImageView = findViewById(R.id.imgMoreItem);
        toggleImageView.setVisibility(View.VISIBLE);
        homeImageView.setVisibility(View.VISIBLE);
        moreImageView.setVisibility(View.GONE);
        titleTextView.setText(R.string.new_kundli);
        titleTextView.setTypeface(mediumTypeface);

        tabs_input_kundli = findViewById(R.id.tabs);
        tabs_input_kundli.setTabMode(TabLayout.MODE_FIXED);
        homeImageView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                CUtils.gotoHomeScreen(HomeInputScreen.this);
            }
        });

        viewPager = findViewById(R.id.pager);
        SELECTED_MODULE = getIntent()
                .getIntExtra(CGlobalVariables.MODULE_TYPE_KEY,
                        CGlobalVariables.MODULE_BASIC);

        SELECTED_SUB_SCREEN = getIntent().getIntExtra(
                CGlobalVariables.SUB_MODULE_TYPE_KEY, 0);

        try {
            configureActionBarTabStyle();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setViewPagerAdapter();
        //If HomeInputScreen is open by ActAskquestionDescription
        if (ASK_QUESTION_QUERY_DATA) {
            fetchProductFromGoogleServer();
            astrologerServiceInfo = new AstrologerServiceInfo();
            itemdetails = (ServicelistModal) getIntent().getExtras().getSerializable("keyItemDetails");
            email_id_for_question = getIntent().getExtras().getString("QUERY_EMAIL_ID");
            mobile_num_for_question = getIntent().getExtras().getString("QUERY_PHONE_NUM");
            query_for_question = getIntent().getExtras().getString("QUERY_QUESTION");

            homeImageView.setVisibility(View.GONE);
            toggleImageView.setVisibility(View.GONE);
            titleTextView.setText(getResources().getString(R.string.select_profile_title));
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            drawerFragment.setsetDrawerIndicatorEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {

                    setCurrentView(position, false);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            //viewPager.setCurrentItem(0);
        } else if (NUMROLOGY_QUERY_DATA) {

            homeImageView.setVisibility(View.GONE);
            toggleImageView.setVisibility(View.GONE);
            titleTextView.setText(getResources().getString(R.string.select_profile_numrology_title));
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            drawerFragment.setsetDrawerIndicatorEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {

                    setCurrentView(position, false);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            //viewPager.setCurrentItem(0);
        } else if (VARTA_PROFILE_QUERY_DATA) {

            homeImageView.setVisibility(View.GONE);
            toggleImageView.setVisibility(View.GONE);
            titleTextView.setText(getResources().getString(R.string.select_profile_text));
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            drawerFragment.setsetDrawerIndicatorEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {

                    setCurrentView(position, false);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            //viewPager.setCurrentItem(0);
        } else if (BHRIGOO_QUERY_DATA) {

            homeImageView.setVisibility(View.GONE);
            toggleImageView.setVisibility(View.GONE);
            titleTextView.setText("Select Profile");
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            drawerFragment.setsetDrawerIndicatorEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {

                    setCurrentView(position, false);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            //viewPager.setCurrentItem(0);
        } else if (ASTROSAGE_CHAT_QUERY_DATA) {

            homeImageView.setVisibility(View.GONE);
            toggleImageView.setVisibility(View.GONE);
            titleTextView.setText(getResources().getString(R.string.select_profile_title));
            astrologerServiceInfo = new AstrologerServiceInfo();
            fetchProductFromGoogleServer();
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            drawerFragment.setsetDrawerIndicatorEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    setCurrentView(position, false);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
            //viewPager.setCurrentItem(0);
        } else if (ASTRO_SERVICE_QUERY_DATA) {
            astrologerServiceInfo = new AstrologerServiceInfo();
            itemdetails = (ServicelistModal) getIntent().getExtras().getSerializable("keyItemDetails");
            email_id_for_question = getIntent().getExtras().getString("QUERY_EMAIL_ID");
            mobile_num_for_question = getIntent().getExtras().getString("QUERY_PHONE_NUM");
            query_for_question = getIntent().getExtras().getString("QUERY_QUESTION");
            homeImageView.setVisibility(View.GONE);
            toggleImageView.setVisibility(View.GONE);
            if (itemdetails != null && itemdetails.getServiceId().equalsIgnoreCase("114") || itemdetails.getIsShowProblem().equalsIgnoreCase("0")) {
                titleTextView.setText(getResources().getString(R.string.select_profile_text));

            } else {
                titleTextView.setText(getResources().getString(R.string.select_profile_title));

            }
            fetchProductFromGoogleServer();
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            drawerFragment.setsetDrawerIndicatorEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    setCurrentView(position, false);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        } else if (ASTRO_PRODUCT_DATA) {
            titleTextView.setText(getResources().getString(R.string.select_profile_text));
            homeImageView.setVisibility(View.GONE);
            toggleImageView.setVisibility(View.GONE);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            drawerFragment.setsetDrawerIndicatorEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    setCurrentView(position, false);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

        } else {

            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    try {
                        //changes by neeraj to set heading dynamically 2/5/16
                        if (SELECTED_MODULE == CGlobalVariables.MODULE_PREDICTION) {

                            String[] testArray = getResources().getStringArray(R.array.reportModulesListForHeading);
                            titleTextView.setText(testArray[SELECTED_SUB_SCREEN]);

                            //added by monika
                            if (SELECTED_SUB_SCREEN == CGlobalVariables.SUB_MODULE_PREDICTION_BABYNAME) {
                                CGlobalVariables.FROM_BABY_NAMES = SELECTED_SUB_SCREEN;
                            } else {
                                CGlobalVariables.FROM_BABY_NAMES = 0;
                            }

                        }
                        if (SELECTED_MODULE == CGlobalVariables.MODULE_FREE_PDF) {
                            String[] testArray2 = getResources().getStringArray(R.array.reportModulesListForHeadingModules);
                            titleTextView.setText(testArray2[0]);
                            SELECTED_MODULE = CGlobalVariables.MODULE_BASIC;
                            SELECTED_SUB_SCREEN = 21;
                        } else {
                            String[] testArray2 = getResources().getStringArray(R.array.reportModulesListForHeadingModules);
                            titleTextView.setText(testArray2[SELECTED_MODULE]);

                        }

                        setCurrentView(position, false);
                    }catch (Exception e){
                        //
                    }

                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            //drawerFragment.setsetDrawerIndicatorEnabled(true);
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            //viewPager.setCurrentItem(pagerIndex);// ADDED BY DEEPAK ON 31-10-2014
        }

        // ADDED BY BIJENDRA ON 28-05-14
        try {
            CScreenHistoryItemCollectionStack.getScreenHistoryItemCollection(
                    HomeInputScreen.this).clearHistoryCollectionStack();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        tabs_input_kundli.setupWithViewPager(viewPager);
        // Iterate over all tabs and set the custom view
        for (int i = 0; i < tabs_input_kundli.getTabCount(); i++) {
            TabLayout.Tab tab = tabs_input_kundli.getTabAt(i);
            tab.setCustomView(adapter.getTabView(i));
        }

        // adapter.setAlpha(pagerIndex,tabs_input_kundli);
        setCurrentView(pagerIndex, false);

        // END ON 08-12-14
        if (getIntent().getStringExtra("from") != null
                && getIntent().getStringExtra("from").equals("Notification")) {
            /*
             * SELECTED_MODULE = getIntent().getIntExtra(
             * CGlobalVariables.MODULE_TYPE_KEY, CGlobalVariables.MODULE_BASIC);
             */

            BeanHoroPersonalInfo notificationKundli = resolveNotificationKundli();
            if (notificationKundli != null) {
                calculateKundli(notificationKundli, false);
            }
            finish();
            // return;

        }
        if (birthDetailInputFragment != null && openKundliBirthDetail != null) {
            setCurrentView(INPUT_SCREEN, false);
        }
        //tejinder
        mDateFormat = DateFormat.getMediumDateFormat(HomeInputScreen.this);
        //app shortcut firebase logging
        if(Intent.ACTION_VIEW.equals(getIntent().getAction())){
            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_APP_ICON_FREE_KUNDLI, CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                CUtils.hideMyKeyboard(this);

                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
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
    }

    /**
     * Add to handle back key event
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                try {
                    if (!ASK_QUESTION_QUERY_DATA && !BHRIGOO_QUERY_DATA && !NUMROLOGY_QUERY_DATA && !VARTA_PROFILE_QUERY_DATA && !ASTROSAGE_CHAT_QUERY_DATA && !ASTRO_SERVICE_QUERY_DATA && !ASTRO_PRODUCT_DATA && drawerFragment != null) {
                        if (drawerFragment.isDrawerOpen) {
                            drawerFragment.closeDrawer();
                        } else {
                            this.finish();
                        }
                    } else {
                        this.finish();
                    }
                } catch (Exception ex) {
                    //Log.i(ex.getMessage().toString());
                    this.finish();
                }
                return true;

        }
        return super.onKeyDown(keyCode, event);
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

    /*
     * (non-Javadoc)
     *
     * @see android.app.Activity#onSaveInstanceState(android.os.Bundle)
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putSerializable(
                CGlobalVariables.outPutMasterActSavedBundleKey,
                CGlobal.getCGlobalObject());
        try {
            outState.putSerializable("beanDateTimeIfIssue", beanDateTimeIfIssue);

            outState.putSerializable("beanTimeIfIssue", beanTimeIfIssue);

            getSupportFragmentManager().putFragment(outState,
                    BirthDetailInputFragment.class.getName(),
                    birthDetailInputFragment);
            getSupportFragmentManager().putFragment(outState,
                    SearchBirthDetailsFragment.class.getName(),
                    searchBirthDetailsFragment);

        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onSaveInstanceState(outState);
    }
    //end by neeraj

    /*
     * (non-Javadoc)
     *
     * @see android.support.v4.app.FragmentActivity#onResume()
     */
    @Override
    protected void onResume() {
        super.onResume();
        CUtils.hideMyKeyboard(this);

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        updateMenus();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }


    //added by neeraj To shift layout on keybord open 9/5/16
    public void headerVisibility(boolean b) {


        if (viewPager.getCurrentItem() == 0) {
            if (b) {
                appbarAppModule.setVisibility(View.GONE);

                getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
            } else {
                appbarAppModule.setVisibility(View.VISIBLE);
                getWindow().clearFlags(
                        WindowManager.LayoutParams.FLAG_FULLSCREEN);

            }
        }
    }

    private void configureActionBarTabStyle() {

        if (ASK_QUESTION_QUERY_DATA || BHRIGOO_QUERY_DATA || NUMROLOGY_QUERY_DATA || VARTA_PROFILE_QUERY_DATA || ASTROSAGE_CHAT_QUERY_DATA || ASTRO_SERVICE_QUERY_DATA || ASTRO_PRODUCT_DATA) {
            pageTitles = getResources().getStringArray(
                    R.array.input_page_titles_list_for_ask_question);
        } else {
            // DISABLED BY BIJENDRA ON 14-08-15
            pageTitles = getResources().getStringArray(
                    R.array.input_page_titles_list);
        }
    }

    private void setViewPagerAdapter() {
        try {
            adapter = new ViewPagerAdapter(getSupportFragmentManager(), HomeInputScreen.this);

            adapter.addFragment(searchBirthDetailsFragment, pageTitles[0]);
            adapter.addFragment(birthDetailInputFragment, pageTitles[1]);

            viewPager.setAdapter(adapter);

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    private void showCustomDatePickerDialog(BeanDateTime beanDateTime) {
        // Create the dialog
        final Dialog mDateTimeDialog = new Dialog(this);
        // Inflate the root layout
        final RelativeLayout mDateTimeDialogView = (RelativeLayout) getLayoutInflater()
                .inflate(R.layout.date_time_dialog, null);
        // Grab widget instance
        final DateTimePicker mDateTimePicker = mDateTimeDialogView
                .findViewById(R.id.DateTimePicker);
        mDateTimePicker.setDateChangedListener(this);
        // Added by hukum to init date picker
        mDateTimePicker.initDateElements(beanDateTime.getYear(),
                beanDateTime.getMonth(), beanDateTime.getDay(),
                beanDateTime.getHour(), beanDateTime.getMin(),
                beanDateTime.getSecond());
        mDateTimePicker.initData();
        // end
        Button setDateBtn = mDateTimeDialogView
                .findViewById(R.id.SetDateTime);
        setDateBtn.setTypeface(regularTypeface);
        // Update demo TextViews when the "OK" button is clicked
        setDateBtn.setOnClickListener(new OnClickListener() {
            public void onClick(View v) {
                // birthDetailInputFragment.isDstOrNot();
                mDateTimePicker.clearFocus();
                mDateTimeDialog.dismiss();
            }
        });

        Button cancelBtn = mDateTimeDialogView
                .findViewById(R.id.CancelDialog);
        cancelBtn.setTypeface(regularTypeface);
        // Cancel the dialog when the "Cancel" button is clicked
        cancelBtn.setOnClickListener(new OnClickListener() {

            public void onClick(View v) {
                mDateTimePicker.reset();
                mDateTimeDialog.cancel();
            }
        });

        // Reset Date and Time pickers when the "Reset" button is clicked

        Button resetBtn = mDateTimeDialogView
                .findViewById(R.id.ResetDateTime);
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
                BeanDateTime beanDateTime = new BeanDateTime();
                beanDateTime.setYear(year);
                beanDateTime.setMonth(monthOfYear);
                beanDateTime.setDay(CUtils.getDayOfMonth(view, dayOfMonth, monthOfYear, year));
                beanDateTime.setHour(0);
                beanDateTime.setMin(0);
                beanDateTime.setSecond(0);
                birthDetailInputFragment.updateBirthDate(beanDateTime);
                beanDateTimeIfIssue = beanDateTime;
            }

        };


        final MyDatePickerDialog myDatePickerDialog = new MyDatePickerDialog(HomeInputScreen.this, R.style.AppCompatAlertDialogStyle, myDateSetListener, beanDateTime.getMonth(), (beanDateTime.getDay()), (beanDateTime.getYear()), false);
        myDatePickerDialog.setCanceledOnTouchOutside(false);
        //mTimePicker.setTitle("hello");
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

        Button butOK = myDatePickerDialog.findViewById(android.R.id.button1);
        Button butCancel = myDatePickerDialog.findViewById(android.R.id.button2);
        butOK.setText(R.string.set);
        butCancel.setText(R.string.cancel);

        butOK.setTypeface(regularTypeface);
        butCancel.setTypeface(regularTypeface);

    }

    private void showAndroidDatePicker(BeanDateTime beanDateTime) {
        final CustomDatePicker dg = new CustomDatePicker(this, mDateSetListener,
                beanDateTime.getYear(), beanDateTime.getMonth(),
                beanDateTime.getDay());
        dg.setCanceledOnTouchOutside(false);
         dg.show();
    }

    @Override
    public void openCalendar(BeanDateTime beanDateTime) {
        if (CUtils.isUserWantsCustomCalender(HomeInputScreen.this)) {
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
    public void openSavedKundli() {
        // viewPager.setCurrentItem(SAVED_KUNDLI_SCREEN);
        setCurrentView(SAVED_KUNDLI_SCREEN, false);
    }

    @Override
    public void openSearchPlace(BeanPlace beanPlace) {
        Intent intent = new Intent(HomeInputScreen.this, ActPlaceSearch.class);
        intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, SELECTED_MODULE);
        intent.putExtra(CGlobalVariables.PLACE_BEAN_KEY, beanPlace);
        startActivityForResult(intent, SUB_ACTIVITY_PLACE_SEARCH);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, final Intent data) {
        // //Log.e("Bijendra", String.valueOf(requestCode));
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case SUB_ACTIVITY_PLACE_SEARCH:
                if (resultCode == RESULT_OK) {
                    Bundle bundle = data.getExtras();
                    if (bundle != null) {
                        final BeanPlace place = CUtils.getPlaceObjectFromBundle(bundle);
                        // set place detail which returned from place activity it should
                        // be set, because in case of activity recreation user should
                        // get updated place value.
                        CGlobal.getCGlobalObject().getHoroPersonalInfoObject()
                                .setPlace(place);
                        if (!isReferenceIssue) {
                            birthDetailInputFragment.updateBirthPlace(place);
                        } else {
                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    try {
                                        birthDetailInputFragment.updateBirthPlace(place);
                                        birthDetailInputFragment.updateBirthDate(beanDateTimeIfIssue);
                                        birthDetailInputFragment.updateBirthTime(beanTimeIfIssue);
                                    } catch (Exception ex) {
                                        //
                                    }
                                }
                            }, 500);
                            isReferenceIssue = false;
                        }

                        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
                        CUtils.hideMyKeyboard(this);
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

                    if (!isReferenceIssue) {
                        birthDetailInputFragment.updateBirthTime(beanDateTime);
                    } else {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    birthDetailInputFragment.updateBirthDate(beanDateTimeIfIssue);
                                    birthDetailInputFragment.updateBirthTime(beanDateTime);
                                } catch (Exception ex) {
                                    //
                                }
                            }
                        }, 500);
                        isReferenceIssue = false;
                    }
                }
                break;
            case SUB_ACTIVITY_USER_PREFERENCE:
                if (resultCode == RESULT_OK) {
                    if (!isReferenceIssue) {
                        birthDetailInputFragment.updateAynamshaFromPrfrenceScreen(data
                                .getIntExtra(CGlobalVariables.AYANAMSHA_KEY, 0));
                    } else {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    birthDetailInputFragment.updateAynamshaFromPrfrenceScreen(data
                                            .getIntExtra(CGlobalVariables.AYANAMSHA_KEY, 0));
                                } catch (Exception ex) {
                                    //
                                }
                            }
                        }, 500);
                        isReferenceIssue = false;
                    }
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
                        }, 500);
                        isReferenceIssue = false;
                    }
                }
            }
            break;
            case SUB_ACTIVITY_UPGRADE_PLAN_DIALOG:
            case SUB_ACTIVITY_UPGRADE_PLAN_DIALOG_PURCHSE:
                if (resultCode == RESULT_OK) {
                    CalculateKundli kundli = new CalculateKundli(CGlobal.getCGlobalObject()
                            .getHoroPersonalInfoObject(), false, HomeInputScreen.this, regularTypeface, SELECTED_MODULE, CGlobalVariables.HOME_INPUT_SCREEN, false, 0, 0, SELECTED_SUB_SCREEN);
                    kundli.new CCalculateOnlineDataSync(
                            CGlobal.getCGlobalObject()
                                    .getHoroPersonalInfoObject(),
                            CUtils.isConnectedWithInternet(getApplicationContext()))
                            .execute();
                }
            break;
            case BOOK_MARK_LIST_ACTIVITY_CODE:
                if (resultCode == RESULT_OK) {
                    Bundle bCTP = data.getExtras();
                    int gid = bCTP.getInt("GORUP_ID");
                    int cid = bCTP.getInt("CHILD_ID");
                    //	openSelectedBookMarkScreen(gid, cid);

                    callCalculateKundli(0, true, gid, cid);
                }
                break;
            case requestCodePaymentFailureScreen:
                if (resultCode == resultCodeTryAgain) {
                    gotBuyAskQuestionPlan();
                } else if (requestCode == resultCodeCancel) {
                    //Do nothing
                }
                break;
            case CGlobalVariables.REQUEST_CODE_PAYTM: {
                try {
                    String responseData = data.getStringExtra("response");
                    //Log.e("PaytmOrder", "resp data1=" + responseData);
                    if (TextUtils.isEmpty(responseData)) {
                        processPaytmTransaction("TXN_FAILED");
                    } else {
                        JSONObject respObj = new JSONObject(responseData);
                        String status = respObj.getString("STATUS");
                        processPaytmTransaction(status);
                    }
                } catch (Exception e) {
                    //
                }
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void onPurchasesUpdated(BillingResult billingResult, @Nullable List<Purchase> purchases) {
        Log.e("BillingClient", "onPurchasesUpdated() 2");
        String astroSageUserId = CUtils.getUserName(this);
        dumpDataString = dumpDataString + " onPurchasesUpdated() responseCode=" + billingResult.getResponseCode() + " astroSageUserId=" + astroSageUserId;
        // Logic from onActivityResult should be moved here.
        if (billingResult.getResponseCode() == BillingClient.BillingResponseCode.OK) {
            Log.e("BillingClient", "onPurchasesUpdated() OK");
            dumpDataString = dumpDataString + " ResponseOK";
            if (purchases != null && !purchases.isEmpty()) {
                Purchase purchase = purchases.get(0);
                dumpDataString = dumpDataString + " signature=" + purchase.getSignature() + " purchaseData=" + purchase.getOriginalJson();
                processGooglePaymentAfterSuccess(purchase);
            }
        } else {
            dumpDataString = dumpDataString + " ResponseFAIL";
            Log.e("BillingClient", "onPurchasesUpdated() FAIL = " + purchases);

            CUtils.googleAnalyticSendWitPlayServie(this,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                    CGlobalVariables.GOOGLE_ANALYTIC_PAYMENT_FAILED, null);
            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_PAYMENT_FAILED, CGlobalVariables.FIREBASE_EVENT_PAYMENT_FAILED, "");

            openPaymentFailedDialog();

            String price = "0";
            if (arayaskquestionPlan.get(price_amount_micros) != null)
                price = arayaskquestionPlan.get(price_amount_micros);

            if(!TextUtils.isEmpty(chat_Id)){
                messageChatID = chat_Id;
            }
            SendUserPurchaseReportForServiceToServerForAstroChat sendObj = new SendUserPurchaseReportForServiceToServerForAstroChat(this, "", CUtils.getStringData(this, CGlobalVariables.USERREGISTEREMAILID, UserEmailFetcher.getEmail(this)), CUtils.getUserName(this), price, "", fullJsonDataObj, "" + layoutPosition, query_for_question, messageChatID/*chatID GOING HERE*/, "", order_Id);
            sendObj.sendReportToServer();

        }
        CUtils.postDumpDataToServer(HomeInputScreen.this, dumpDataString);
        //Log.e("BillingClient", "onPurchasesUpdated() FAIL purchases size="+purchases.size());
    }

    private void processGooglePaymentAfterSuccess(Purchase purchase) {
        try {
            SavePlaninPreference(ASK_QUESTION_PLAN);
            String plan = CGlobalVariables.ASK_QUESTION_PLAN_VALUE;
            String price = arayaskquestionPlan.get(price_amount_micros);// 2
            String priceCurrencycode = arayaskquestionPlan.get(price_currency_code);
            if (TextUtils.isEmpty(price)) {
                price = "0";
            }
            if (TextUtils.isEmpty(priceCurrencycode)) {
                priceCurrencycode = "INR";
            }
            isPaymentDone = true;
            CUtils.saveBooleanData(HomeInputScreen.this, CGlobalVariables.PAYMENTDONE_KEY_FOR_ASTRO_SERVICES_ISPAYMENTDONE, isPaymentDone);

            double dPrice = 0.0;

            try {
                if (price != null && price.length() > 0) {
                    dPrice = Double.valueOf(price);
                    dPrice = (dPrice / CGlobalVariables.PRICE_IN_ONE_UNIT);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }

            CUtils.googleAnalyticSendWitPlayServieForPurchased(this,
                    CGlobalVariables.GOOGLE_ANALYTIC_ASTROSHOP_SERVICE,
                    CGlobalVariables.GOOGLE_ANALYTIC_PAYMENT_SUCCESS, null, dPrice, "");

            gotoThanksPage(purchase, plan, price, priceCurrencycode);
        } catch (Exception e) {
            Log.e("BillingClient", "AfterSuccess exp=" + e);
        }
    }

    private void openPaymentFailedDialog() {
        CUtils.saveBooleanData(this,CGlobalVariables.IS_SUBSCRIPTION_ERROR,true);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        PaymentFailedDailogFrag pfdf = new PaymentFailedDailogFrag();
        ft.add(pfdf, null);
        ft.commitAllowingStateLoss();
    }

    private void setUserLoginDetails(String loginName, String loginPwd) {
        //kundliHomeInputMenuFrag.updateLoginDetials(true, loginName, loginPwd);
        if (drawerFragment != null) {
            drawerFragment.updateLoginDetials(true, loginName, loginPwd, getDrawerListItem(), getDrawerListItemIcon(), getDrawerListItemIndex());
        }
    }

    @Override
    public void calculateKundli(BeanHoroPersonalInfo beanHoroPersonalInfo,
                                boolean isSaveDetail) {
        try {
            CalculateKundli kundli = new CalculateKundli(beanHoroPersonalInfo, isSaveDetail, HomeInputScreen.this, regularTypeface, SELECTED_MODULE, CGlobalVariables.HOME_INPUT_SCREEN, false, 0, 0, SELECTED_SUB_SCREEN);
            kundli.calculate();
            boolean isLogin = com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(this); //if user login in varta section
            if(isLogin) {
                UserProfileData userProfileData = com.ojassoft.astrosage.varta.utils.CUtils.getProfileForChatFromPreference(this);
                if (userProfileData == null || userProfileData.getName().isEmpty()) {//if user profile not persent in varta section then create profile
                    userProfileData = CUtils.getUserProfileObjFromUserKundliData(beanHoroPersonalInfo);
                    sendToServer(userProfileData);
                }
            }
        } catch (Exception e) {

        }
    }

    /**
     * Resolves the kundli payload for notification launches, preferring the saved default object and falling back to
     * the serialized notification extras when needed.
     *
     * @return kundli details to use for notification-driven chart calculation, or null when none are available
     */
    private BeanHoroPersonalInfo resolveNotificationKundli() {
        Object savedObject = CUtils.getCustomObject(HomeInputScreen.this);
        if (savedObject instanceof BeanHoroPersonalInfo) {
            return (BeanHoroPersonalInfo) savedObject;
        }

        BeanHoroPersonalInfo intentKundli = (BeanHoroPersonalInfo) getIntent()
                .getSerializableExtra(CGlobalVariables.BIRTH_DETAILS_KEY);
        if (intentKundli != null) {
            CUtils.saveCustomObject(HomeInputScreen.this, intentKundli);
        }
        return intentKundli;
    }

    private void sendToServer(UserProfileData userProfileData) {
        try {
            int year = Integer.parseInt(userProfileData.getYear());
            int month = Integer.parseInt(userProfileData.getMonth());
            int day = Integer.parseInt(userProfileData.getDay());
            com.ojassoft.astrosage.varta.utils.CUtils.setFcmAnalyticsByAge(year, month, day);
        } catch (Exception e) {
            //
        }
        if (com.ojassoft.astrosage.varta.utils.CUtils.isConnectedWithInternet(this)) {
            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.updateUserProfile(getProfileParams(userProfileData));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        String myResponse = response.body().string();
                        JSONObject jsonObject = new JSONObject(myResponse);
                        //Log.e("TestProfile", "myResponse= "+jsonObject);
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("1")) {
                            com.ojassoft.astrosage.varta.utils.CUtils.saveUserSelectedProfileInPreference(HomeInputScreen.this, userProfileData);
                        }
                    } catch (Exception e) {
                        //Log.e("TestProfile", e.toString());
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    //Log.e("TestProfile", "onFailure() "+t);
                }
            });
        }
    }

    public Map<String, String> getProfileParams(UserProfileData userProfileDataBean) {

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.APP_KEY, com.ojassoft.astrosage.varta.utils.CUtils.getApplicationSignatureHashCode(this));
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.COUNTRY_CODE, com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(this));
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.USER_PHONE_NO, com.ojassoft.astrosage.varta.utils.CUtils.getUserID(this));
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.REG_SOURCE, com.ojassoft.astrosage.varta.utils.CGlobalVariables.APP_SOURCE);
        headers.put("name", userProfileDataBean.getName());
        headers.put("gender", userProfileDataBean.getGender());
        headers.put("place", userProfileDataBean.getPlace());
        headers.put("day", userProfileDataBean.getDay());
        headers.put("month", userProfileDataBean.getMonth());
        headers.put("year", userProfileDataBean.getYear());
        headers.put("hour", userProfileDataBean.getHour());
        headers.put("minute", userProfileDataBean.getMinute());
        headers.put("second", userProfileDataBean.getSecond());
        headers.put("longdeg", userProfileDataBean.getLongdeg());
        headers.put("longmin", userProfileDataBean.getLongmin());
        headers.put("longew", userProfileDataBean.getLongew());
        headers.put("latmin", userProfileDataBean.getLatmin());
        headers.put("latdeg", userProfileDataBean.getLatdeg());
        headers.put("latns", userProfileDataBean.getLatns());
        headers.put("timezone", userProfileDataBean.getTimezone());
        headers.put("maritalStatus", userProfileDataBean.getMaritalStatus());
        headers.put("occupation", userProfileDataBean.getOccupation());
        headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANG, com.ojassoft.astrosage.varta.utils.CUtils.getLanguageKey(LANGUAGE_CODE));
        Log.d("TestProfile", "headers=" + headers);
        return com.ojassoft.astrosage.varta.utils.CUtils.setRequiredParams(headers);
    }

    public void sendDataToActAstroService(BeanHoroPersonalInfo beanHoroPersonalInfo) {
        AstrologerServiceInfo astrologerServiceInfo = CUtils.setDataModel(HomeInputScreen.this, beanHoroPersonalInfo, payMode, itemdetails);
        String jsonFullAstrosageData = CUtils.convertInJsonObj(astrologerServiceInfo);
/*        isPaymentDone = false;
        CUtils.saveBooleanData(HomeInputScreen.this, CGlobalVariables.PAYMENTDONE_KEY_FOR_ASTRO_SERVICES_ISPAYMENTDONE, isPaymentDone);
        CUtils.saveStringData(HomeInputScreen.this, CGlobalVariables.PAYMENTDONE_KEY_FOR_ASTRO_SERVICES_JSONOBJ, fullJsonDataObj);
        //  purchaseServiceByInApp();*/
        Intent resultIntent = new Intent();
        CUtils.saveBooleanData(HomeInputScreen.this, CGlobalVariables.IS_USER_PROFILE_FILLED, true);
        resultIntent.putExtra("JSONFULLASTROSAGEDATA", jsonFullAstrosageData);
        setResult(Activity.RESULT_OK, resultIntent);
        HomeInputScreen.this.finish();
    }

    public void sendDataToActAstroProduct(BeanHoroPersonalInfo beanHoroPersonalInfo) {
        Intent resultIntent = new Intent();
        CUtils.saveBooleanData(HomeInputScreen.this, CGlobalVariables.IS_USER_PROFILE_FILLED, true);
        resultIntent.putExtra("PROFILEDATA", new Gson().toJson(beanHoroPersonalInfo));
        setResult(Activity.RESULT_OK, resultIntent);
        HomeInputScreen.this.finish();
    }

    /**
     * This method return kundali data for numrology calculation
     *
     * @param beanHoroPersonalInfo
     */
    public void sendDataForProfileUse(BeanHoroPersonalInfo beanHoroPersonalInfo) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_KUNDALI_DETAILS, beanHoroPersonalInfo);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    /**
     * This method return kundali data for numrology calculation
     *
     * @param beanHoroPersonalInfo
     */
    public void sendDataForBhrigoo(BeanHoroPersonalInfo beanHoroPersonalInfo) {
        Intent intent = new Intent();
        Bundle bundle = new Bundle();
        bundle.putSerializable(KEY_KUNDALI_DETAILS, beanHoroPersonalInfo);
        intent.putExtras(bundle);
        setResult(RESULT_OK, intent);
        finish();
    }

    public void sendDataToActAstroPaymentOptionsForAskAQuestion(BeanHoroPersonalInfo beanHoroPersonalInfo) {
        beanAskAQue = beanHoroPersonalInfo;

        boolean googleWalletPaymentVisibility = CUtils.getBooleanData(HomeInputScreen.this, CGlobalVariables.key_GoogleWalletPaymentVisibility, true);
        boolean paytmPaymentVisibility = CUtils.getBooleanData(HomeInputScreen.this, CGlobalVariables.key_PaytmPaymentVisibility, true);
        boolean razorPaymentVisibility = CUtils.getBooleanData(HomeInputScreen.this, CGlobalVariables.key_RazorPayVisibilityServices, true);
        boolean paytmPaymentVisibilityService = CUtils.getBooleanData(HomeInputScreen.this, CGlobalVariables.key_PaytmWalletVisibilityServices, true);

        int noOfQuestion = CUtils.getIntData(HomeInputScreen.this, CGlobalVariables.NOOFQUESTIONAVAILABLE, 0);
        if (noOfQuestion > 0) {

            showProgressBar();

            if (!CUtils.isConnectedWithInternet(HomeInputScreen.this)) {
                MyCustomToast mct = new MyCustomToast(HomeInputScreen.this, HomeInputScreen.this
                        .getLayoutInflater(), HomeInputScreen.this, regularTypeface);
                mct.show(getResources().getString(R.string.no_internet));
            } else {
                CUtils.saveIntData(HomeInputScreen.this, CGlobalVariables.NOOFQUESTIONAVAILABLE, --noOfQuestion);
                isFreeQuestion = false;

                if (messageChatID == null || messageChatID.equals("")) {
                    setMessageDataAskByUser();
                    saveDataOnInternalStorage();
                    layoutPosition = chatMessageArrayList.size() - 1;
                }

                CUtils.saveBooleanData(HomeInputScreen.this, CGlobalVariables.Type_PAYMENT, true);
                int count = CUtils.getIntData(HomeInputScreen.this, CGlobalVariables.COUNTNOTIFICATIONCENTER, 0);
                CUtils.saveIntData(HomeInputScreen.this, CGlobalVariables.COUNTNOTIFICATIONCENTER, ++count);
                setDataModel(beanAskAQue, "Free");
                convertInJsonObj(astrologerServiceInfo);

                String price = "0";
                if (arayaskquestionPlan.get(price_amount_micros) != null)
                    price = arayaskquestionPlan.get(price_amount_micros);

                SendUserPurchaseReportForServiceToServerForAstroChat sendObj = new SendUserPurchaseReportForServiceToServerForAstroChat(this, "", UserEmailFetcher.getEmail(this), CUtils.getUserName(this), price, "", fullJsonDataObj, String.valueOf(layoutPosition), query_for_question, messageChatID, "Order Free Question");
                sendObj.sendReportToServerForFreeQue();
            }

        } else {
            CUtils.saveBooleanData(HomeInputScreen.this, CGlobalVariables.Type_PAYMENT, false);

            if (isUserHAsPlanBoolean) {
                //astrologerServiceInfo = CUtils.setDataModel(this, beanHoroPersonalInfo, "Paytm", servicelistModal);

                if (razorPaymentVisibility && !paytmPaymentVisibilityService) {
                    onSelectedOption(R.id.radioRazor, "");
                } else if (!razorPaymentVisibility && paytmPaymentVisibilityService) {
                    onSelectedOption(R.id.radioPaytm, "");
                } else {
                    Fragment diog = getSupportFragmentManager().findFragmentByTag("Dialog");
                    if (diog == null) {
                        ChooseServicePayOPtionDialog dialog = new ChooseServicePayOPtionDialog();
                        dialog.show(getSupportFragmentManager(), "Dialog");
                    }
                }
            } else {

                if (googleWalletPaymentVisibility && !razorPaymentVisibility && !paytmPaymentVisibility) {
                    onSelectedOption(R.id.radioGoogle, "");
                } else if (razorPaymentVisibility && !googleWalletPaymentVisibility && !paytmPaymentVisibility) {
                    onSelectedOption(R.id.radioRazor, "");
                } else if (paytmPaymentVisibility && !googleWalletPaymentVisibility && !razorPaymentVisibility) {
                    onSelectedOption(R.id.radioPaytm, "");
                } else {
                    Fragment diog = getSupportFragmentManager().findFragmentByTag("Dialog");
                    if (diog == null) {
                        ChoosePayOptionFragmentDailog dialog = new ChoosePayOptionFragmentDailog();
                        dialog.show(getSupportFragmentManager(), "Dialog");
                    }
                }
            }
        }

    }

    public void sendDataToActAstroPaymentOptionsForAstroChat(BeanHoroPersonalInfo beanHoroPersonalInfo) {
        AstrologerServiceInfo astrologerServiceInfo = CUtils.setDataModel(HomeInputScreen.this, beanHoroPersonalInfo, payMode, itemdetails);
        String jsonFullAstrosageData = CUtils.convertInJsonObj(astrologerServiceInfo);
        isPaymentDone = false;
        CUtils.saveBooleanData(HomeInputScreen.this, CGlobalVariables.PAYMENTDONE_KEY_FOR_ASTRO_SERVICES_ISPAYMENTDONE, isPaymentDone);
        CUtils.saveStringData(HomeInputScreen.this, CGlobalVariables.PAYMENTDONE_KEY_FOR_ASTRO_SERVICES_JSONOBJ, fullJsonDataObj);
        //  purchaseServiceByInApp();
        Intent resultIntent = new Intent();
        CUtils.saveBooleanData(HomeInputScreen.this, CGlobalVariables.IS_USER_PROFILE_FILLED, true);
        resultIntent.putExtra("JSONFULLASTROSAGEDATA", jsonFullAstrosageData);
        setResult(Activity.RESULT_OK, resultIntent);
        HomeInputScreen.this.finish();
    }

    private void gotoThanksPage(Purchase purchase, String plan, String price, String priceCurrencycode) {
        signature = purchase.getSignature();
        purchaseData = purchase.getOriginalJson();
        //
        HomeInputScreen.this.getSharedPreferences("MISC_PUR_SERVICE", Context.MODE_PRIVATE).edit()
                .putString("VALUE_SERVICE", purchaseData).commit();
        String astroSageUserId = CUtils.getUserName(HomeInputScreen.this);
        saveDataInPreferences(astroSageUserId, price, priceCurrencycode);
        // FOR START A SERVICE
        verifyPurchaseFromService(astroSageUserId, price, priceCurrencycode);
        // update status
        CUtils.updatePaidStatus(HomeInputScreen.this, layoutPosition, "", "1", messageChatID, astrologerServiceInfo);

        Intent intent = new Intent(HomeInputScreen.this, ActNotificationLanding.class);
        startActivity(intent);
    }

    private void verifyPurchaseFromService(String astroSageUserId, String price, String priceCurrencycode) {

        Intent pvsIntent = new Intent(this,
                VerificationServiceForInAppBillingChat.class);
        pvsIntent.putExtra("SIGNATURE", signature);
        pvsIntent.putExtra("PURCHASE_DATA", purchaseData);
        pvsIntent.putExtra("DEVELOPER_PAYLOAD", developerPayload);
        pvsIntent.putExtra("ASTRO_USERID", astroSageUserId);
        String textMsg = query_for_question;
        /*if (spouseFullDetail != null&&) {
            textMsg = textMsg + "<>" + CUtils.convertInJsonObj(beanHoroPersonalInfo2);
        }*/
        if(!TextUtils.isEmpty(chat_Id)){
            messageChatID = chat_Id;
        }
        pvsIntent.putExtra("MESSAGE_TEXT", textMsg);
        pvsIntent.putExtra("MESSAGE_CHAT_ID", messageChatID);
        pvsIntent.putExtra("price", price);
        pvsIntent.putExtra("priceCurrencycode", priceCurrencycode);
        pvsIntent.putExtra("messageTitle", "Order Insert");

        pvsIntent.putExtra("FullJsonDataObj", fullJsonDataObj);
        pvsIntent.putExtra("layoutPostion", "" + layoutPosition);
        pvsIntent.putExtra("ORDER_ID", order_Id);
        //pvsIntent.putExtra("CHAT_ID", chat_Id);
        startService(pvsIntent);

        CUtils.saveStringData(getApplicationContext(), "MISC_PUR_SERVICE_LAYOUT_POSITION", "" + layoutPosition);
        CUtils.saveStringData(getApplicationContext(), "MISC_PUR_SERVICE_MSG_TEXT", "" + textMsg);
        CUtils.saveStringData(getApplicationContext(), "MISC_PUR_SERVICE_MSG_CHAT_ID", messageChatID);
    }

    private void saveDataInPreferences(String astroSageUserId, String price, String priceCurrencycode) {
        SharedPreferences purchasageDataPlan = getSharedPreferences(CGlobalVariables.ASTROSAGEPURCHASEPLANFORSERVICE, Context.MODE_PRIVATE);
        SharedPreferences.Editor purchasePlanEditor = purchasageDataPlan.edit();
        AppPurchaseDataSaveModelClass appPurchaseDataSaveModelClass = CUtils.getObjectSavePref(signature, purchaseData, developerPayload, astroSageUserId, price, priceCurrencycode);
        Gson gson = new Gson();
        String purchaseDataJSONObject = gson.toJson(appPurchaseDataSaveModelClass); // myObject - instance of MyObject
        purchasePlanEditor.putString(CGlobalVariables.ASTROSAGEPURCHASEPLANFORSERVICEOBJECT, purchaseDataJSONObject);
        purchasePlanEditor.commit();
    }

    private String convertInJsonObj(AstrologerServiceInfo localOrderModal) {
        Gson gson = new Gson();
        localOrderModal.setPrtnr_id(CGlobalVariables.currentSession);
        fullJsonDataObj = gson.toJson(localOrderModal);
        return fullJsonDataObj;
    }

    private void purchaseServiceByInApp() {
        dumpDataString = "HomeInputScreen purchaseServiceByInApp() fullJsonDataObj=" + fullJsonDataObj;
        gotBuyAskQuestionPlan();
    }

    @Override
    public void onRetryClick() {
        //gotBuyAskQuestionPlan();

        boolean googleWalletPaymentVisibility = CUtils.getBooleanData(HomeInputScreen.this, CGlobalVariables.key_GoogleWalletPaymentVisibility, true);
        boolean paytmPaymentVisibility = CUtils.getBooleanData(HomeInputScreen.this, CGlobalVariables.key_PaytmPaymentVisibility, true);
        boolean razorPaymentVisibility = CUtils.getBooleanData(HomeInputScreen.this, CGlobalVariables.key_RazorPayVisibilityServices, true);
        boolean paytmPaymentVisibilityService = CUtils.getBooleanData(HomeInputScreen.this, CGlobalVariables.key_PaytmWalletVisibilityServices, true);


        if (isUserHAsPlanBoolean) {
            //astrologerServiceInfo = CUtils.setDataModel(this, beanHoroPersonalInfo, "Paytm", servicelistModal);

            if (razorPaymentVisibility && !paytmPaymentVisibilityService) {
                onSelectedOption(R.id.radioRazor, "");
            } else if (!razorPaymentVisibility && paytmPaymentVisibilityService) {
                onSelectedOption(R.id.radioPaytm, "");
            } else {
                Fragment diog = getSupportFragmentManager().findFragmentByTag("Dialog");
                if (diog == null) {
                    ChooseServicePayOPtionDialog dialog = new ChooseServicePayOPtionDialog();
                    dialog.show(getSupportFragmentManager(), "Dialog");
                }
            }
        } else {

            if (googleWalletPaymentVisibility && !razorPaymentVisibility && !paytmPaymentVisibility) {
                onSelectedOption(R.id.radioGoogle, "");
            } else if (razorPaymentVisibility && !googleWalletPaymentVisibility && !paytmPaymentVisibility) {
                onSelectedOption(R.id.radioRazor, "");
            } else if (paytmPaymentVisibility && !googleWalletPaymentVisibility && !razorPaymentVisibility) {
                onSelectedOption(R.id.radioPaytm, "");
            } else {
                Fragment diog = getSupportFragmentManager().findFragmentByTag("Dialog");
                if (diog == null) {
                    ChoosePayOptionFragmentDailog dialog = new ChoosePayOptionFragmentDailog();
                    dialog.show(getSupportFragmentManager(), "Dialog");

                }
            }
        }
    }

    @Override
    public void onSelectedOption(int opt, String mob) {

        if (opt == R.id.radioGoogle) {
            setDataModel(beanAskAQue, "Google");
        } else if (opt == R.id.radioRazor) {
            setDataModel(beanAskAQue, "RazorPay");
        } else {
            setDataModel(beanAskAQue, "Paytm");
        }

        //setDataModel(beanAskAQue);
        convertInJsonObj(astrologerServiceInfo);
        String jsonFullAstrosageData = convertInJsonObj(astrologerServiceInfo);
        if (messageChatID == null || messageChatID.equals("")) {
            setMessageDataAskByUser();
            saveDataOnInternalStorage();
            layoutPosition = chatMessageArrayList.size() - 1;
        }
        int count = CUtils.getIntData(HomeInputScreen.this, CGlobalVariables.COUNTNOTIFICATIONCENTER, 0);
        CUtils.saveIntData(HomeInputScreen.this, CGlobalVariables.COUNTNOTIFICATIONCENTER, ++count);
        //layoutPosition=chatMessageArrayList.size()-1;

        if (opt == R.id.radioGoogle) {

            // astrologerServiceInfo.setPayMode(payMode);
            isPaymentDone = false;
            CUtils.saveBooleanData(HomeInputScreen.this, CGlobalVariables.PAYMENTDONE_KEY_FOR_ASTRO_SERVICES_ISPAYMENTDONE, isPaymentDone);
            CUtils.saveStringData(HomeInputScreen.this, CGlobalVariables.PAYMENTDONE_KEY_FOR_ASTRO_SERVICES_JSONOBJ, fullJsonDataObj);
            //purchaseServiceByInApp();

            Typeface typeface = CUtils.getRobotoFont(
                    HomeInputScreen.this, LANGUAGE_CODE, CGlobalVariables.regular);
            RequestQueue queue = VolleySingleton.getInstance(HomeInputScreen.this).getRequestQueue();

            if (!CUtils.isConnectedWithInternet(HomeInputScreen.this)) {
                MyCustomToast mct = new MyCustomToast(HomeInputScreen.this, HomeInputScreen.this
                        .getLayoutInflater(), HomeInputScreen.this, typeface);
                mct.show(getResources().getString(R.string.no_internet));
            } else {
                CUtils.getOrderIDAndChatId(HomeInputScreen.this, typeface, queue, itemdetails, fullJsonDataObj, astrologerServiceInfo, messageChatID);
            }

        } else if (opt == R.id.radioRazor) {
            CUtils.googleAnalyticSendWitPlayServie(HomeInputScreen.this,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                    CGlobalVariables.GOOGLE_ANALYTIC_ASKAQUESTION_RAZORPAY, null);
            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_ASKAQUESTION_RAZORPAY, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

            Typeface typeface = CUtils.getRobotoFont(
                    HomeInputScreen.this, LANGUAGE_CODE, CGlobalVariables.regular);
            RequestQueue queue = VolleySingleton.getInstance(HomeInputScreen.this).getRequestQueue();

            if (!CUtils.isConnectedWithInternet(HomeInputScreen.this)) {
                MyCustomToast mct = new MyCustomToast(HomeInputScreen.this, HomeInputScreen.this
                        .getLayoutInflater(), HomeInputScreen.this, typeface);
                mct.show(getResources().getString(R.string.no_internet));
            } else {
                // CUtils.getOrderID(HomeInputScreen.this, typeface, queue, astrologerServiceInfo);
                CUtils.getOrderIDAndChatId(HomeInputScreen.this, typeface, queue, itemdetails, fullJsonDataObj, astrologerServiceInfo, messageChatID);
            }
        } else {
            LANGUAGE_CODE = ((AstrosageKundliApplication) HomeInputScreen.this.getApplication())
                    .getLanguageCode();

            Typeface typeface = CUtils.getRobotoFont(
                    HomeInputScreen.this, LANGUAGE_CODE, CGlobalVariables.regular);
            RequestQueue queue = VolleySingleton.getInstance(HomeInputScreen.this).getRequestQueue();

            CUtils.googleAnalyticSendWitPlayServie(this,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                    CGlobalVariables.GOOGLE_ANALYTIC_ASKAQUESTION_PAYTM, null);
            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_ASKAQUESTION_PAYTM, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

            if (!CUtils.isConnectedWithInternet(HomeInputScreen.this)) {
                MyCustomToast mct = new MyCustomToast(HomeInputScreen.this, HomeInputScreen.this
                        .getLayoutInflater(), HomeInputScreen.this, typeface);
                mct.show(getResources().getString(R.string.no_internet));
            } else {
                //astrologerServiceInfo.setPayMode("Paytm");
                // CUtils.getOrderID(HomeInputScreen.this, typeface, queue, astrologerServiceInfo);
                CUtils.getOrderIDAndChatId(HomeInputScreen.this, typeface, queue, itemdetails, fullJsonDataObj, astrologerServiceInfo, messageChatID);
            }
        }

    }

    public void saveDataOnInternalStorage() {
        Intent intent = new Intent(HomeInputScreen.this, SaveDataInternalStorage.class);
        intent.putParcelableArrayListExtra(CGlobalVariables.CHATWITHASTROLOGER, chatMessageArrayList);
        intent.putExtra(CGlobalVariables.ISINSERT, false);
        startService(intent);

    }

    private void addToList(MessageDecode messageDecode) {
        if (chatMessageArrayList == null)
            chatMessageArrayList = new ArrayList<MessageDecode>();
        chatMessageArrayList.add(messageDecode);

    }

    private MessageDecode setMessageDataAskByUser() {
        MessageDecode messageDecode = new MessageDecode();
        messageDecode.setUserType("user");
        messageDecode.setNotificationShow("False");
        String currentDateTime = CUtils.getCurrentDateTime();
        messageDecode.setDateTimeShow(currentDateTime);
        messageDecode.setMessageText(query_for_question);
        messageDecode.setColorOfMessage("#ffffff");
        messageDecode.setRateShow("False");
        messageDecode.setShareLinkShow("False");
        //  messageDecode.setNoOfQuestion(CUtils.getIntData(HomeInputScreen.this, CGlobalVariables.NOOFQUESTIONAVAILABLE, 0));
        int noOfQuestion = CUtils.getIntData(HomeInputScreen.this, CGlobalVariables.NOOFQUESTIONAVAILABLE, 0);
        if (noOfQuestion > 0) {
            messageDecode.setNotPaidLayoutShow("False");
        } else {
            messageDecode.setNotPaidLayoutShow("True");
            messageDecode.setOrderId("0");
        }
        if (astrologerServiceInfo != null) {
            messageDecode.setAstrologerServiceInfo(astrologerServiceInfo);
        }
        addToList(messageDecode);
        return messageDecode;
    }

    private void goToRazorPayFlow() {

        final Checkout co = new Checkout();
        co.setFullScreenDisable(true);


        try {
            JSONObject options = new JSONObject();
            options.put("name", "AstroSage");
            options.put("description", getResources().getString(R.string.askaquestion));
            //You can omit the image option to fetch the image from dashboard
            //    options.put("image", "http://astrosage.com/images/logo.png");
            options.put("currency", "INR");
            Double amount = Double.parseDouble(astrologerServiceInfo.getPriceRs());
            //Need to send amount to razor pay in paise
            Double paiseAmount = amount * 100;
            options.put("amount", paiseAmount);
            options.put("color", "#ff6f00");
            //options.put("order_id", razorpayOrderId);


            JSONObject preFill = new JSONObject();
            preFill.put("email", astrologerServiceInfo.getEmailID().trim());
            preFill.put("contact", astrologerServiceInfo.getMobileNo().trim());
            options.put("prefill", preFill);


            JSONObject notes = new JSONObject();
            //added by Ankit on 3-7-2019 for Razorpay Webhook
            notes.put("orderId", order_Id);
            notes.put("chatId", chat_Id);
            notes.put("orderType", CGlobalVariables.PAYMENT_TYPE_SERVICE);
            notes.put("appVersion", BuildConfig.VERSION_NAME);
            notes.put("appName", BuildConfig.APPLICATION_ID);
            notes.put("name", astrologerServiceInfo.getRegName());
            notes.put("firebaseinstanceid", com.ojassoft.astrosage.varta.utils.CUtils.getFirebaseAnalyticsAppInstanceId(this));
            notes.put("facebookinstanceid", com.ojassoft.astrosage.varta.utils.CUtils.getFacebookAnalyticsAppInstanceId(this));
            options.put("notes", notes);


            co.open(this, options);
        } catch (Exception e) {
            Toast.makeText(this, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    @Override
    public void getCallBack(String result, CUtils.callBack callback, String priceInDollor, String priceInRs) {
        RequestQueue queue = VolleySingleton.getInstance(HomeInputScreen.this).getRequestQueue();

        if (callback == CUtils.callBack.GET_ORDER_ID) {
            order_Id = result;
            //com.google.analytics.tracking.android.//Log.e("order" + order_Id);
            if (order_Id != null && !order_Id.isEmpty()) {

                if (priceInRs != null && priceInRs.length() > 0) {
                    astrologerServiceInfo.setPriceRs(priceInRs);
                }

                if (priceInDollor != null && priceInDollor.length() > 0) {
                    astrologerServiceInfo.setPrice(priceInDollor);
                }

                if (astrologerServiceInfo.getPayMode().equalsIgnoreCase("Google")) {
                    purchaseServiceByInApp();
                } else if (astrologerServiceInfo.getPayMode().equalsIgnoreCase("RazorPay")) {
                    Double amount = Double.parseDouble(astrologerServiceInfo.getPriceRs());
                    Double paiseAmount = amount * 100;
                    // API calling
                    //showProgressBar();
                    //new VolleyServerRequest(this, (OnVolleyResultListener) HomeInputScreen.this, CGlobalVariables.RAZORPAY_ORDERID_URL, CUtils.getRazorOrderIdParams(paiseAmount,"INR",order_Id));
                    goToRazorPayFlow();
                } else {
                    new GetCheckSum(HomeInputScreen.this, regularTypeface).getCheckSum(getchecksumparams(), 0);
                    //CUtils.getCheckSum(HomeInputScreen.this, getchecksumparams(), regularTypeface);
                }

            } else {
                MyCustomToast mct = new MyCustomToast(HomeInputScreen.this,
                        HomeInputScreen.this.getLayoutInflater(),
                        HomeInputScreen.this, regularTypeface);
                mct.show(getResources().getString(R.string.order_fail));
            }
        } else if (callback == CUtils.callBack.GET_CHECKSUM) {
            String checksum = result;
            if (!result.isEmpty()) {
                startPaytmPayment(order_Id, astrologerServiceInfo.getPriceRs(), checksum);
            } else {
                MyCustomToast mct = new MyCustomToast(HomeInputScreen.this,
                        HomeInputScreen.this.getLayoutInflater(),
                        HomeInputScreen.this, regularTypeface);
                mct.show(getResources().getString(R.string.order_fail));
            }
        } else if (callback == CUtils.callBack.POST_STATUS) {
            String postStatus = result;
            // String postStatus = "1";
            if (postStatus != null && postStatus.equalsIgnoreCase("1")) {

                if (payStatus.equalsIgnoreCase("1")) {
                    onPurchaseCompleted(itemdetails, order_Id);

                    Intent intent = new Intent(HomeInputScreen.this, ActNotificationLanding.class);
                    startActivity(intent);

                } else {
                    CUtils.googleAnalyticSendWitPlayServie(this,
                            CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                            CGlobalVariables.GOOGLE_ANALYTIC_PAYMENT_FAILED_PAYTM, null);
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_PAYMENT_FAILED_PAYTM, CGlobalVariables.FIREBASE_EVENT_PAYMENT_FAILED, "");

                    openPaymentFailedDialog();
                }

            } else {
                MyCustomToast mct = new MyCustomToast(HomeInputScreen.this,
                        HomeInputScreen.this.getLayoutInflater(),
                        HomeInputScreen.this, regularTypeface);
                mct.show(getResources().getString(R.string.order_fail));


            }
        } else if (callback == CUtils.callBack.POST_RAZORPAYSTATUS) {
            String postStatus = result;
            if (postStatus != null && postStatus.equalsIgnoreCase("1")) {
                if (payStatus.equalsIgnoreCase("1")) {

                    //CUtils.emailPDF(HomeInputScreen.this,CGlobalVariables.RAZORPAY_EMAIL_PDF, mediumTypeface, queue, order_Id, payStatus, astrologerServiceInfo);
                    onPurchaseCompleted(itemdetails, order_Id);

                    Intent intent = new Intent(HomeInputScreen.this, ActNotificationLanding.class);
                    startActivity(intent);

                } else {
                    openPaymentFailedDialog();
                    CUtils.googleAnalyticSendWitPlayServie(this,
                            CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                            CGlobalVariables.GOOGLE_ANALYTIC_SERVICE_RAZOR_PAYMENT_FAILED, null);
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SERVICE_RAZOR_PAYMENT_FAILED, CGlobalVariables.FIREBASE_EVENT_PAYMENT_FAILED, "");

                }

            } else {
                MyCustomToast mct = new MyCustomToast(HomeInputScreen.this,
                        HomeInputScreen.this.getLayoutInflater(),
                        HomeInputScreen.this, regularTypeface);
                //mct.show(getResources().getString(R.string.order_fail));
                openPaymentFailedDialog();
            }
        }
    }

    @Override
    public void getCallBackForChat(String[] result, CUtils.callBack callback, String priceInDollor, String priceInRs) {
        if (callback == CUtils.callBack.GET_ORDER_ID) {
            order_Id = result[0];
            chat_Id = result[1];
            //com.google.analytics.tracking.android.//Log.e("order" + order_Id);
            if (order_Id != null && !order_Id.isEmpty() && chat_Id != null && !chat_Id.isEmpty()) {

                if (priceInRs != null && priceInRs.length() > 0) {
                    astrologerServiceInfo.setPriceRs(priceInRs);
                }

                if (priceInDollor != null && priceInDollor.length() > 0) {
                    astrologerServiceInfo.setPrice(priceInDollor);
                }
                CUtils.updatePaidStatus(HomeInputScreen.this, layoutPosition, order_Id, "0", chat_Id, astrologerServiceInfo);

                if (astrologerServiceInfo.getPayMode().equalsIgnoreCase("Google")) {
                    purchaseServiceByInApp();
                } else if (astrologerServiceInfo.getPayMode().equalsIgnoreCase("RazorPay")) {
                    Double amount = Double.parseDouble(astrologerServiceInfo.getPriceRs());
                    Double paiseAmount = amount * 100;
                    // API calling
                    //showProgressBar();
                    //new VolleyServerRequest(this, (OnVolleyResultListener) HomeInputScreen.this, CGlobalVariables.RAZORPAY_ORDERID_URL, CUtils.getRazorOrderIdParams(paiseAmount,"INR",order_Id));
                    goToRazorPayFlow();
                } else {
                    new GetCheckSum(HomeInputScreen.this, regularTypeface).getCheckSum(getchecksumparams(), 1);
                    //CUtils.getCheckSum(HomeInputScreen.this, getchecksumparams(), regularTypeface);
                }

            } else {
                MyCustomToast mct = new MyCustomToast(HomeInputScreen.this,
                        HomeInputScreen.this.getLayoutInflater(),
                        HomeInputScreen.this, regularTypeface);
                mct.show(getResources().getString(R.string.order_fail));
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults != null && grantResults.length > 0) {
            if (requestCode == PERMISSION_CONTACTS && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                setEmailIdFromUserAccount();
            } else {
                boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0]);
                if (!showRationale) {
                    CUtils.saveBooleanData(this, CGlobalVariables.PERMISSION_KEY_CONTACTS, true);
                }
            }
        }
    }

    @Override
    public void isEmailIdPermissionGranted(boolean isPermissionGranted) {
        checkForContactPermission(true);
    }

    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {

            double dPrice = 0.0;
            String price = "";
            try {
                if (astrologerServiceInfo != null) {
                    if (astrologerServiceInfo.getPriceRs() != null && astrologerServiceInfo.getPriceRs().length() > 0) {
                        price = astrologerServiceInfo.getPriceRs();
                        dPrice = Double.valueOf(price);
                    }
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            // in case of purchase event not added by server then add event
            if(!com.ojassoft.astrosage.utils.CUtils.isPurchaseEventAddedByServer(this)) {
                CUtils.googleAnalyticSendWitPlayServieForPurchased(this,
                        CGlobalVariables.GOOGLE_ANALYTIC_ASTROSHOP_SERVICE,
                        CGlobalVariables.GOOGLE_ANALYTIC_SERVICE_RAZOR_PAYMENT_SUCCESS, null, dPrice, "");
            }
            payStatus = "1";
            payId = razorpayPaymentID;
            RequestQueue queue = VolleySingleton.getInstance(HomeInputScreen.this).getRequestQueue();

            CUtils.updatePaidStatus(HomeInputScreen.this, layoutPosition, order_Id, payStatus, chat_Id, astrologerServiceInfo);

            CUtils.postAskQuestionRazorpayDataToServer(HomeInputScreen.this, mediumTypeface, queue, order_Id, chat_Id, payStatus, astrologerServiceInfo, CGlobalVariables.UPDATE_RAZORPAY_STATUS_CHAT, payId, "");

            //  Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            //Log.e("ServicePay", "Exception in onPaymentSuccess", e);
        }
    }

    @Override
    public void onPaymentError(int i, String response) {
        try {
            String status = "Code-" + i + " " + "Message-" + response;
            payStatus = "0";
            RequestQueue queue = VolleySingleton.getInstance(HomeInputScreen.this).getRequestQueue();

            CUtils.updatePaidStatus(HomeInputScreen.this, layoutPosition, order_Id, payStatus, chat_Id, astrologerServiceInfo);
            CUtils.postAskQuestionRazorpayDataToServer(HomeInputScreen.this, mediumTypeface, queue, order_Id, chat_Id, payStatus, astrologerServiceInfo, CGlobalVariables.UPDATE_RAZORPAY_STATUS_CHAT, "", status);
            CUtils.saveBooleanData(this,CGlobalVariables.IS_SUBSCRIPTION_ERROR,true);
            // Toast.makeText(this, "Payment failed: " + i + " " + response, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            //Log.e("Service pay", "Exception in onPaymentError", e);
        }
    }

    private void checkForContactPermission(boolean isUSerPermissionDialogOpen) {
        if (CUtils.isContactsPermissionGranted(this, this, PERMISSION_CONTACTS, isUSerPermissionDialogOpen)) {
            setEmailIdFromUserAccount();
        }
    }

    private void setEmailIdFromUserAccount() {
        String email = UserEmailFetcher.getEmail(this);
        if (email != null) {
            //etUseremail.setText(email);
        }
    }

    private void fetchProductFromGoogleServer() {
        QueryProductDetailsParams queryProductDetailsParams =
                QueryProductDetailsParams.newBuilder()
                        .setProductList(ImmutableList.of(QueryProductDetailsParams.Product.newBuilder()
                                .setProductId(SKU_ASKQUESTION_PLAN)
                                .setProductType(BillingClient.ProductType.INAPP)
                                .build())).build();

        AstrosageKundliApplication.billingClient.queryProductDetailsAsync(
                queryProductDetailsParams, (billingResult, productDetailsList) -> {
                    int response = billingResult.getResponseCode();
                    Log.e("BillingClient", "onProductDetailsResponse() response=" + response);
                    if (response == BillingClient.BillingResponseCode.OK) {
                        for (ProductDetails productDetails : productDetailsList) {
                            intiProductPlan(productDetails);
                        }
                    } else {
                        showMsg(response);
                    }
                }
        );
    }

    private void showMsg(final int response) {
        HomeInputScreen.this.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (response >= 0) {
                    //Toast.makeText(HomeInputScreen.this, errorResponse[response], Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void intiProductPlan(ProductDetails productDetails) {
        try {
            if (productDetails.getProductId().equalsIgnoreCase(SKU_ASKQUESTION_PLAN)) {
                arayaskquestionPlan.add(productDetails.getProductId());// 0
                arayaskquestionPlan.add(productDetails.getTitle());// 1
                arayaskquestionPlan.add(productDetails.getOneTimePurchaseOfferDetails().getFormattedPrice()); // 2
                arayaskquestionPlan.add(productDetails.getDescription());// 3
                arayaskquestionPlan.add(productDetails.getProductType());// 4
                arayaskquestionPlan.add(productDetails.getOneTimePurchaseOfferDetails().getPriceAmountMicros() + "");//5
                arayaskquestionPlan.add(productDetails.getOneTimePurchaseOfferDetails().getPriceCurrencyCode());
                askAQSkuDetails = productDetails;
            }

        } catch (Exception e) {
            System.out.print(e.getMessage());
        }
    }

    public void gotBuyAskQuestionPlan() {
        try {
            dumpDataString = dumpDataString + " gotBuyAskQuestionPlan()";
            CUtils.googleAnalyticSendWitPlayServie(HomeInputScreen.this,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                    CGlobalVariables.GOOGLE_ANALYTIC_ASKAQUESTION_GOOGLE_WALLET, null);
            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_ASKAQUESTION_GOOGLE_WALLET, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

            if (askAQSkuDetails != null) {

                ImmutableList productDetailsParamsList =
                        ImmutableList.of(
                                BillingFlowParams.ProductDetailsParams.newBuilder()
                                        // retrieve a value for "productDetails" by calling queryProductDetailsAsync()
                                        .setProductDetails(askAQSkuDetails)
                                        .build()
                        );

                BillingFlowParams billingFlowParams = BillingFlowParams.newBuilder()
                        .setProductDetailsParamsList(productDetailsParamsList)
                        .build();

                // Launch the billing flow
                int responseCode = AstrosageKundliApplication.billingClient.launchBillingFlow(this, billingFlowParams).getResponseCode();
                dumpDataString = dumpDataString + " gotBuyAskQuestionPlan() prepayment responseCode=" + responseCode;
                Log.e("BillingClient", "dumpDataString=" + dumpDataString);
                if (responseCode != BillingClient.BillingResponseCode.OK) {
                    Toast.makeText(HomeInputScreen.this, "Fail to purchase", Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            //Log.e(e.getMessage());
        }
        CUtils.postDumpDataToServer(HomeInputScreen.this, dumpDataString);
    }

    private void SavePlaninPreference(int newPlanIndex) {
        SharedPreferences sharedPreferences = HomeInputScreen.this
                .getSharedPreferences(CGlobalVariables.APP_PREFS_NAME_FOR_SERVICE,
                        Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
        if (newPlanIndex == ASK_QUESTION_PLAN) {
            sharedPrefEditor.putString(CGlobalVariables.APP_PREFS_Plan_for_service,
                    CGlobalVariables.ASK_QUESTION_PLAN_VALUE);
        }
        sharedPrefEditor.commit();
    }

    private String calculatePayloadKey(String planTyle) {
        Calendar calendar = Calendar.getInstance();
        StringBuilder sb = new StringBuilder();
        sb.append(planTyle);
        sb.append(calendar.getTime().getTime());
        return sb.toString();
    }

    // END ON 08-12-14
    @Override
    public void selectedKundli(BeanHoroPersonalInfo beanHoroPersonalInfo, int position) {
        //viewPager.setCurrentItem(INPUT_SCREEN);
        setCurrentView(INPUT_SCREEN, false);
        birthDetailInputFragment.updateBirthDetail(beanHoroPersonalInfo);
    }

    @Override
    public void onDateChanged(Calendar c) {
        BeanDateTime beanDateTime = new BeanDateTime();
        beanDateTime.setYear(c.get(Calendar.YEAR));
        beanDateTime.setMonth(c.get(Calendar.MONTH));
        beanDateTime.setDay(c.get(Calendar.DAY_OF_MONTH));
        beanDateTime.setHour(c.get(Calendar.HOUR_OF_DAY));
        beanDateTime.setMin(c.get(Calendar.MINUTE));
        beanDateTime.setSecond(c.get(Calendar.SECOND));
        birthDetailInputFragment.updateBirthDate(beanDateTime);
    }

    @Override
    public void openTimePicker(final BeanDateTime beanDateTime) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            final MyTimePickerDialog.OnTimeSetListener myTimeSetListener = new MyTimePickerDialog.OnTimeSetListener() {
                @Override
                public void onTimeSet(com.ojassoft.astrosage.utils.TimePicker view, int hourOfDay, int minute, int seconds) {
                    BeanDateTime beanDateTime = new BeanDateTime();
                    beanDateTime.setHour(hourOfDay);
                    beanDateTime.setMin(minute);
                    beanDateTime.setSecond(seconds);
                    birthDetailInputFragment.updateBirthTime(beanDateTime);
                    beanTimeIfIssue = beanDateTime;
                }
            };


            final MyTimePickerDialog mTimePicker = new MyTimePickerDialog(HomeInputScreen.this, R.style.AppCompatAlertDialogStyle, myTimeSetListener, beanDateTime.getHour(), (beanDateTime.getMin()), (beanDateTime.getSecond()));
            mTimePicker.setCanceledOnTouchOutside(false);
//            Drawable drawable = getResources().getDrawable(R.drawable.timer_title, null);
//            drawable.setTint(ContextCompat.getColor(this, R.color.black));
//           // mTimePicker.setIcon(getResources().getDrawable(R.drawable.timer_title));
//            mTimePicker.setIcon(drawable);
            boolean tabletSize = getResources().getBoolean(R.bool.isTablet);
            if (!tabletSize) {
                WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
                lp.copyFrom(mTimePicker.getWindow().getAttributes());
                lp.width = WindowManager.LayoutParams.MATCH_PARENT;
                lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
                mTimePicker.show();
                mTimePicker.getWindow().setAttributes(lp);
                // Set the background color
            }
            mTimePicker.show();

            Button butOK = mTimePicker.findViewById(android.R.id.button1);
            Button butCancel = mTimePicker.findViewById(android.R.id.button2);
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

    private void showCustomTimePickerDialog(BeanDateTime beanDateTime) {
        Intent intent = new Intent(this, MyNewCustomTimePicker.class);
        intent.putExtra("H", beanDateTime.getHour());
        intent.putExtra("M", beanDateTime.getMin());
        intent.putExtra("S", beanDateTime.getSecond());
        intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, SELECTED_MODULE);
        startActivityForResult(intent, SUB_ACTIVITY_TIME_PICKER);
    }

    public void goToLogin() {

        if (!CUtils.isUserLogedIn(HomeInputScreen.this)) {

            Intent intent = new Intent(HomeInputScreen.this, ActLogin.class);
            intent.putExtra("callerActivity",
                    CGlobalVariables.HOME_INPUT_SCREEN);
            startActivityForResult(intent, SUB_ACTIVITY_USER_LOGIN);
        } else {
            // kundliHomeInputMenuFrag.updateLoginDetials(false, "", "");
            MyCustomToast mct = new MyCustomToast(HomeInputScreen.this,
                    HomeInputScreen.this.getLayoutInflater(),
                    HomeInputScreen.this, regularTypeface);
            mct.show(getResources().getString(R.string.sign_out_success));
            searchBirthDetailsFragment.clearOnlineChartsListAfterSignOut();
        }
    }

    private void applyChangedLanguageInApplication() {

        startActivity(new Intent(getApplicationContext(), ActAppModule.class)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        this.finish();
    }

    @Override
    public void onSelectedLanguage(int languageIndex) {
        if (LANGUAGE_CODE != languageIndex) {
            LANGUAGE_CODE = languageIndex;
            applyChangedLanguageInApplication();
        }
    }

    @Override
    public void birthDetailInputFragmentCreated() {
        if (isEditKundli) {
            isEditKundli = false;
            birthDetailInputFragment.updateBirthDetail(CGlobal
                    .getCGlobalObject().getHoroPersonalInfoObject());
        } else {
            if (openKundliBirthDetail != null) {
                birthDetailInputFragment.updateBirthDetail(openKundliBirthDetail);
            }
        }
    }

    @Override
    public void oneChartDeleted(long kundliId, boolean isOnlineChart) {
        birthDetailInputFragment
                .oneChartDeleted_IfThatIsCurrentOneThenDeleteChartIdFromIt(
                        kundliId, isOnlineChart);
    }

    public void newKundli() {
        drawerNewKundliOptionClicked = true;
        birthDetailInputFragment.resetBirthDetailForm();
        // viewPager.setCurrentItem(INPUT_SCREEN);
        setCurrentView(INPUT_SCREEN, false);
    }

    public void openKundli() {
        // viewPager.setCurrentItem(SAVED_KUNDLI_SCREEN);
        setCurrentView(SAVED_KUNDLI_SCREEN, false);
    }

    @Override
    public void logoutFromAstroSageCloud(boolean isShowToast) {
        if (drawerFragment != null) {
            drawerFragment.updateLoginDetials(false, "", "", getDrawerListItem(), getDrawerListItemIcon(), getDrawerListItemIndex());
        }
        MyCustomToast mct = new MyCustomToast(HomeInputScreen.this,
                HomeInputScreen.this.getLayoutInflater(), HomeInputScreen.this,
                regularTypeface);
        mct.show(getResources().getString(R.string.sign_out_success));
        //searchBirthDetailsFragment.doActionOnLogout();
    }

    private List<String> getDrawerListItem() {

        try {
            String[] menuItems1 = getResources().getStringArray(R.array.kundli_home_menu_item_list);
            String[] menuItems2 = getResources().getStringArray(R.array.module_list);
            return CUtils.getDrawerListItem(HomeInputScreen.this, menuItems1, menuItems2, kundli_home_menu_item_list_index);
        } catch (Exception ex) {
            //Log.i(ex.getMessage().toString());
            return null;
        }

    }

    private List<Drawable> getDrawerListItemIcon() {

        try {
            TypedArray itemsIcon1 = getResources().obtainTypedArray(R.array.kundli_home_menu_item_list_icon);
            TypedArray itemsIcon2 = getResources().obtainTypedArray(R.array.module_icons);

            return CUtils.getDrawerListItemIcon(HomeInputScreen.this, itemsIcon1, itemsIcon2, kundli_home_menu_item_list_index);
        } catch (Exception ex) {
            //Log.i(ex.getMessage().toString());
            return null;
        }

    }

    private List<Integer> getDrawerListItemIndex() {
        try {
            return CUtils.getDrawerListItemIndex(HomeInputScreen.this, kundli_home_menu_item_list_index, module_list_index);
        } catch (Exception ex) {
            //Log.i(ex.getMessage().toString());
            return null;
        }
    }

    public void updateMenus() {

        try {
            if (!ASK_QUESTION_QUERY_DATA && !BHRIGOO_QUERY_DATA && !NUMROLOGY_QUERY_DATA && !VARTA_PROFILE_QUERY_DATA && !ASTROSAGE_CHAT_QUERY_DATA && !ASTRO_SERVICE_QUERY_DATA && !ASTRO_PRODUCT_DATA && drawerFragment != null) {
                drawerFragment.updateLayout(getDrawerListItem(), getDrawerListItemIcon(), getDrawerListItemIndex());
            }
        } catch (Exception ex) {
            //Log.i(ex.getMessage().toString());
        }

    }

    public void setDataAfterPurchasePlan(boolean purchaseSilverPlan, String screenId) {

        if (purchaseSilverPlan) {

            //08-feb-2016 work for which screen will be open
            //String ScreenId = data.getStringExtra("ScreenId");
            CUtils.gotoProductPlanListUpdated(
                    HomeInputScreen.this,
                    LANGUAGE_CODE,
                    HomeInputScreen.SUB_ACTIVITY_UPGRADE_PLAN_DIALOG_PURCHSE, screenId, "home_input_screen");

        }// END ON 22-12-2014
        else {

            CalculateKundli kundli = new CalculateKundli(CGlobal.getCGlobalObject()
                    .getHoroPersonalInfoObject(), false, HomeInputScreen.this, regularTypeface, SELECTED_MODULE, CGlobalVariables.HOME_INPUT_SCREEN, false, 0, 0, SELECTED_SUB_SCREEN);
            kundli.new CCalculateOnlineDataSync(
                    CGlobal.getCGlobalObject()
                            .getHoroPersonalInfoObject(),
                    CUtils.isConnectedWithInternet(getApplicationContext()))
                    .execute();
        }
    }

    private void setNumberPickerDividerColour(NumberPicker number_picker) {
        final int count = number_picker.getChildCount();

        for (int i = 0; i < count; i++) {

            try {
                Field dividerField = number_picker.getClass().getDeclaredField("mSelectionDivider");
                dividerField.setAccessible(true);
                ColorDrawable colorDrawable = new ColorDrawable(getResources().getColor(R.color
                        .numberPickerDividerColor));
                dividerField.set(number_picker, colorDrawable);

                number_picker.invalidate();
            } catch (NoSuchFieldException e) {
                //android.util.//Log.e("TAGEx", "setNumberPickerDividerColour: " + e.getMessage());
            } catch (IllegalAccessException e) {
                //android.util.//Log.e("TAGEx", "setNumberPickerDividerColour: " + e.getMessage());
            } catch (IllegalArgumentException e) {
                //android.util.//Log.e("TAGEx", "setNumberPickerDividerColour: " + e.getMessage());
            }
        }
    }

    private void setDataModel(BeanHoroPersonalInfo beanHoroPersonalInfo, String paymode) {

        if (beanHoroPersonalInfo != null) {
            astrologerServiceInfo.setKey(CUtils.getApplicationSignatureHashCode(this));
            astrologerServiceInfo.setPayMode(paymode);
            astrologerServiceInfo.setKphn("" + beanHoroPersonalInfo.getHoraryNumber());
            String gender = beanHoroPersonalInfo.getGender();
            astrologerServiceInfo.setGender(gender);
            // email_id_for_question = UserEmailFetcher.getEmail(HomeInputScreen.this);
            if (email_id_for_question == null) {
                email_id_for_question = UserEmailFetcher.getEmail(HomeInputScreen.this);

            } else {

                astrologerServiceInfo.setEmailID(email_id_for_question);
            }

            String username = beanHoroPersonalInfo.getName();
            astrologerServiceInfo.setRegName(username); //Temporarily changes by chandan
            //astrologerServiceInfo.setRegName(email_id_for_question);

            BeanDateTime beanDateTime = beanHoroPersonalInfo.getDateTime();
            astrologerServiceInfo.setDateOfBirth(String.valueOf(beanDateTime.getDay()));
            astrologerServiceInfo.setMonthOfBirth(String.valueOf(beanDateTime.getMonth() + 1));
            astrologerServiceInfo.setYearOfBirth(String.valueOf(beanDateTime.getYear()));
            astrologerServiceInfo.setHourOfBirth(String.valueOf(beanDateTime.getHour()));
            astrologerServiceInfo.setMinOfBirth(String.valueOf(beanDateTime.getMin()));
            astrologerServiceInfo.setDst("" + beanHoroPersonalInfo.getDST());

            BeanPlace place = beanHoroPersonalInfo.getPlace();
            String city = place.getCityName();
            String state = place.getState();
            String country = place.getCountryName();

            astrologerServiceInfo.setLongMinOfBirth(place.getLongMin());
            astrologerServiceInfo.setLongEWOfBirth(place.getLongDir());
            astrologerServiceInfo.setLongDegOfBirth(place.getLongDeg());
            astrologerServiceInfo.setLatDegOfBirth(place.getLatDeg());
            astrologerServiceInfo.setLatMinOfBirth(place.getLatMin());
            astrologerServiceInfo.setLatNSOfBirth(place.getLatDir());
            astrologerServiceInfo.setPlace(city);
            astrologerServiceInfo.setNearCity(city);
            if (country.trim().equals("not define")) {
                astrologerServiceInfo.setState("");
                astrologerServiceInfo.setCountry("");
            } else {
                astrologerServiceInfo.setState(state);
                astrologerServiceInfo.setCountry(country);
            }
            if (place.getTimeZone() != null && !place.getTimeZone().isEmpty()) {
                astrologerServiceInfo.setTimezone(place.getTimeZone());
            } else {
                astrologerServiceInfo.setTimezone("5.5");
            }
            float tz = CUtils.getAdjustedTimezone(astrologerServiceInfo.getDst(), astrologerServiceInfo.getTimezone());
            astrologerServiceInfo.setTimezone("" + tz);
            astrologerServiceInfo.setProblem(query_for_question);
                /*if (checkBoxbirth.isChecked()) {
                    astrologerServiceInfo.setKnowDOB("1");
                } else {*/
            astrologerServiceInfo.setKnowDOB("0");
            //}
                /*if (checkBoxtimeofbirth.isChecked()) {
                    astrologerServiceInfo.setKnowTOB("1");
                } else {*/
            astrologerServiceInfo.setKnowTOB("0");
            //}
            astrologerServiceInfo.setMobileNo(mobile_num_for_question);
            astrologerServiceInfo.setPriceRs(itemdetails.getPriceInRS());
            astrologerServiceInfo.setPrice(itemdetails.getPriceInDollor());
            astrologerServiceInfo.setServiceId(itemdetails.getServiceId());
            astrologerServiceInfo.setProfileId("");
            astrologerServiceInfo.setBillingCountry("");

            astrologerServiceInfo.setLatitude(place.getLatDeg() + place.getLatDir() + place.getLatMin());
            astrologerServiceInfo.setLongitude(place.getLongDeg() + place.getLongDir() + place.getLongMin());
            if (itemdetails != null)
                astrologerServiceInfo.setPriceRs(itemdetails.getPriceInRS());
            else
                astrologerServiceInfo.setPriceRs("");


        }
        // }
    }

    public void onPurchaseCompleted(ServicelistModal itemdetails, String order_id) {

        CUtils.trackEcommerceProduct(HomeInputScreen.this, itemdetails.getServiceId(), itemdetails.getTitle(), itemdetails.getPriceInRS(), order_id, "INR", CGlobalVariables.Ecommerce_Paytm_Purchase, "In-App Store", "0", CGlobalVariables.TOPIC_ASKAQUESTION);
    }

    private void setCurrentView(int index, boolean smoothScroll) {
        viewPager.setCurrentItem(index, smoothScroll);
        if (tabs_input_kundli != null && adapter != null) {
            adapter.setAlpha(index, tabs_input_kundli);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        if (SearchBirthDetailsFragment.popup != null) {
            SearchBirthDetailsFragment.popup.dismiss();
        }
    }

    @Override
    public void doActionAfterGetResult(String response, int method) {
        try {
            hideProgressBar();
            JSONArray jsonArray = new JSONArray(response);
            JSONObject jsonObject = jsonArray.getJSONObject(0);
            String successResult = jsonObject.getString("Result");

            if (successResult.equalsIgnoreCase("1") || successResult.equalsIgnoreCase("3")) {

                double dPrice = 0.0;
                try {
                    if (priceValue != null && priceValue.length() > 0) {
                        dPrice = Double.valueOf(priceValue);
                    }
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
                CUtils.googleAnalyticSendWitPlayServieForPurchased(HomeInputScreen.this,
                        CGlobalVariables.GOOGLE_ANALYTIC_ASTROSHOP_SERVICE,
                        CGlobalVariables.GOOGLE_ANALYTIC_ASK_A_QUESTION_FREE, null, dPrice, "");

                Intent intent = new Intent(HomeInputScreen.this, ActNotificationLanding.class);
                startActivity(intent);

            } else if (successResult.equalsIgnoreCase("8")) {
                //8 = If Free question is deactivated or expired
                CUtils.openFreeQuestionDeactivateFrag(HomeInputScreen.this);
            } else {
                Toast.makeText(HomeInputScreen.this, getResources().getString(R.string.server_error_msg), Toast.LENGTH_LONG).show();
            }
        } catch (Exception ex) {
            Toast.makeText(HomeInputScreen.this, getResources().getString(R.string.server_error_msg), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void doActionOnError(String response) {
        hideProgressBar();
        Toast.makeText(HomeInputScreen.this, getResources().getString(R.string.server_error_msg), Toast.LENGTH_LONG).show();
    }

    /**
     * show Progress Bar
     */
    public void showProgressBar() {
        if (pd == null) {
            pd = new CustomProgressDialog(HomeInputScreen.this, regularTypeface);
        }

        if (!pd.isShowing()) {
            pd.setCanceledOnTouchOutside(false);
            pd.show();
        }
    }

    /**
     * hide Progress Bar
     */
    public void hideProgressBar() {
        try {
            if (pd != null && pd.isShowing())
                pd.dismiss();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onVolleySuccess(String result, Cache cache) {
        try {
            hideProgressBar();
            //JSONObject jsonObject = new JSONObject(result);
            //goToRazorPayFlow(jsonObject.optString("id"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onVolleyError(VolleyError result) {
        hideProgressBar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CGlobalVariables.FROM_BABY_NAMES = 0;
        openKundliBirthDetail = null;
    }

    /*************************************** Paytm Transaction ************************************************************/

    Map<String, String> getchecksumparams() {
        String email = astrologerServiceInfo.getEmailID();
        Map<String, String> params = new HashMap<>();

        params.put("key", CUtils.getApplicationSignatureHashCode(this));
        params.put("MID", "Ojasso36077880907527");
        params.put("ORDER_ID", order_Id);
        params.put("WEBSITE", "OjassoWAP");
        params.put("CALLBACK_URL", CGlobalVariables.CALLBACK_URL + order_Id);
        params.put("TXN_AMOUNT", astrologerServiceInfo.getPriceRs());
        params.put("CUST_ID", email);

        chat_Id = chat_Id.equalsIgnoreCase("") ? "0" : chat_Id;
        String extraData = "chatId_" + chat_Id + "_type_" + PAYMENT_TYPE_SERVICE
                + "_appVersion_" + BuildConfig.VERSION_NAME + "_appName_" + BuildConfig.APPLICATION_ID+
                "_firebaseinstanceid_"+ com.ojassoft.astrosage.varta.utils.CUtils.getFirebaseAnalyticsAppInstanceId(this)+
                "_facebookinstanceid_"+ com.ojassoft.astrosage.varta.utils.CUtils.getFacebookAnalyticsAppInstanceId(this);

        params.put("MERC_UNQ_REF", extraData);

        return params;
    }

    private void startPaytmPayment(String oId, String amount, String tnxToken) {
        //Log.e("PaytmOrder", "resp oId=" + oId+" amount="+amount+ " tnxToken"+tnxToken);
        String midString = CGlobalVariables.PAYTM_MID;
        String callBackUrl = CGlobalVariables.CALLBACK_URL + oId;

        PaytmOrder paytmOrder = new PaytmOrder(oId, midString, tnxToken, amount, callBackUrl);

        TransactionManager transactionManager = new TransactionManager(paytmOrder, new PaytmPaymentTransactionCallback() {

            @Override
            public void onTransactionResponse(Bundle bundle) {
                try {
                    String status = bundle.getString("STATUS");
                    //Log.e("PaytmOrder", "resp status=" + status);
                    processPaytmTransaction(status);
                } catch (Exception e) {
                    //
                }
            }

            @Override
            public void networkNotAvailable() {

            }

            @Override
            public void onErrorProceed(String s) {

            }

            @Override
            public void clientAuthenticationFailed(String s) {

            }

            @Override
            public void someUIErrorOccurred(String s) {

            }

            @Override
            public void onErrorLoadingWebPage(int i, String s, String s1) {

            }

            @Override
            public void onBackPressedCancelTransaction() {

            }

            @Override
            public void onTransactionCancel(String s, Bundle bundle) {

            }
        });
        //transactionManager.setAppInvokeEnabled(false);
        transactionManager.setShowPaymentUrl(CGlobalVariables.PAYTM_PAYMENT_URL);
        transactionManager.startTransaction(this, CGlobalVariables.REQUEST_CODE_PAYTM);
    }

    private void processPaytmTransaction(String status) {
        if (status.equals(CGlobalVariables.TXN_SUCCESS)) {
            payStatus = "1";
        } else {
            payStatus = "0";
        }

        double dPrice = 0.0;
        String price = "";
        try {
            if (astrologerServiceInfo != null) {
                if (astrologerServiceInfo.getPriceRs() != null && astrologerServiceInfo.getPriceRs().length() > 0) {
                    price = astrologerServiceInfo.getPriceRs();
                    dPrice = Double.valueOf(price);
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (!payStatus.equals("0")) { //sucess
            // in case of purchase event not added by server then add event
            if(!com.ojassoft.astrosage.utils.CUtils.isPurchaseEventAddedByServer(this)) {
                CUtils.googleAnalyticSendWitPlayServieForPurchased(this,
                        CGlobalVariables.GOOGLE_ANALYTIC_ASTROSHOP_SERVICE,
                        CGlobalVariables.GOOGLE_ANALYTIC_PAYMENT_SUCCESS_PAYTM, null, dPrice, "");
            }
        }
        RequestQueue queue = VolleySingleton.getInstance(HomeInputScreen.this).getRequestQueue();
        CUtils.updatePaidStatus(HomeInputScreen.this, layoutPosition, order_Id, payStatus, chat_Id, astrologerServiceInfo);
        //Activity activity, Typeface typeface, RequestQueue queue, final String orderId, final String statusCode, final AstrologerServiceInfo astrologerServiceInfo
        CUtils.postDataToServer(HomeInputScreen.this, mediumTypeface, queue, order_Id, chat_Id, payStatus, astrologerServiceInfo, CGlobalVariables.UPDATE_PAY_STATUS_CHAT);
    }
}
