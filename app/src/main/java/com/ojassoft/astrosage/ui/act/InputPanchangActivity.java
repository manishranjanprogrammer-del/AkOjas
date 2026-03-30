package com.ojassoft.astrosage.ui.act;

import static android.view.View.GONE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_INPUT_PANCHANG_BOTTOM_CALL;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_INPUT_PANCHANG_BOTTOM_CHAT;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_INPUT_PANCHANG_BOTTOM_KUNDLI;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_INPUT_PANCHANG_BOTTOM_LIVE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.INPUT_PANCHANG_BOTTOM_BAR_CALL_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.INPUT_PANCHANG_BOTTOM_BAR_CHAT_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.INPUT_PANCHANG_BOTTOM_BAR_KUNDLI_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.INPUT_PANCHANG_BOTTOM_BAR_LIVE_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KUNDLI_AI_OPEN_FIRST_TIME_KEY;
import static com.ojassoft.astrosage.utils.CGlobalVariables.MODULE_SUGGESTED_QUESTIONS_KEY;
import static com.ojassoft.astrosage.utils.CGlobalVariables.WALLET_BOTTOM_BAR_KUNDLI_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.matchingScreenIds;
import static com.ojassoft.astrosage.utils.CGlobalVariables.panchangScreenIds;
import static com.ojassoft.astrosage.utils.CUtils.createSession;
import static com.ojassoft.astrosage.utils.CUtils.isPopupLoginShown;
import static com.ojassoft.astrosage.utils.CUtils.openKundliActivity;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.APP_KEY;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.APP_PREFS_AppLanguage;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.CURRENT_SCREEN_ID_KEY;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.FILTER_TYPE_CALL;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.FILTER_TYPE_CHAT;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_CONVERSATION_ID;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.KEY_SCREEN_NAME;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.MATCHING_KEY;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.PANCHANG_DATE_KEY;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.PANCHANG_LID_KEY;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.SOURCE_OF_SCREEN;
import static com.ojassoft.astrosage.varta.utils.CUtils.getApplicationSignatureHashCode;
import static com.ojassoft.astrosage.varta.utils.CUtils.getLocaleResources;
import static com.ojassoft.astrosage.varta.utils.CUtils.gotoAllLiveActivityFromLiveIcon;

import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AajKaPanchangModel;
import com.ojassoft.astrosage.beans.BeanDateTimeForPanchang;
import com.ojassoft.astrosage.beans.BeanPlace;
import com.ojassoft.astrosage.customadapters.ViewPagerAdapter;
import com.ojassoft.astrosage.jinterface.IPanchang;
import com.ojassoft.astrosage.model.FestDataDetail;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.act.horoscope.DetailedHoroscope;
import com.ojassoft.astrosage.ui.act.matching.OutputMatchingMasterActivity;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.ui.fragments.AskQuestionsFragment;
import com.ojassoft.astrosage.ui.fragments.BhadraInputFragment;
import com.ojassoft.astrosage.ui.fragments.ChogadiaInputFragment;
import com.ojassoft.astrosage.ui.fragments.DoGhatiMuhurat;
import com.ojassoft.astrosage.ui.fragments.HomeNavigationDrawerFragment;
import com.ojassoft.astrosage.ui.fragments.HoraInputFragment;
import com.ojassoft.astrosage.ui.fragments.KundliAIFirstTimeMsgFragment;
import com.ojassoft.astrosage.ui.fragments.LagnaInputFragment;
import com.ojassoft.astrosage.ui.fragments.PanchakInputFragment;
import com.ojassoft.astrosage.ui.fragments.PanchangDashboardFrag;
import com.ojassoft.astrosage.ui.fragments.PanchangInputFragment;
import com.ojassoft.astrosage.ui.fragments.PanchangMuhuratFragment;
import com.ojassoft.astrosage.ui.fragments.RahuKaalInputFragment;
import com.ojassoft.astrosage.ui.fragments.horoscope.DailyHoroscopeFragment;
import com.ojassoft.astrosage.utils.CGlobal;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.PopUpLogin;
import com.ojassoft.astrosage.varta.dao.KundliHistoryDao;
import com.ojassoft.astrosage.varta.ui.MiniChatWindow;
import com.ojassoft.astrosage.varta.ui.activity.MyAccountActivity;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.varta.utils.TypeWriter;

import org.checkerframework.checker.units.qual.C;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;


public class InputPanchangActivity extends BaseTtsActivity implements IPanchang {

    public static final int SUB_ACTIVITY_USER_LOGIN = 1003;
    public static final int PRODUCT_PLAN_LIST = 12;
    private static final int CLOUD_SIGN_OUT = 11;

    public final int SHARE_TITLE = 0;
    public final int SHARE_CONTENT = 1;
    public final int MISC = 2;
    public final int FEEDBACK = 3;
    public final int RATE_APP = 4;
    public final int SHARE_APP = 5;
    public final int OUR_OTHER_APPS = 6;
    public final int CALL_US = 7;
    public final int ASTRO_SHOP = 8;
    public final int ASK_OUR_ASTROLOGER = 9;
    public final int ABOUT_US = 10;
    public ImageView imgWhatsApp;
    public TextView placeTV;
    // public Spinner spinnerScreenList;
    public ViewPager pager;
    public int SELECTED_MODULE;
    public BeanPlace beanPlace = null;
    public Calendar calendar;
    public AajKaPanchangModel model;
    public AajKaPanchangModel nextDayModel;
    public AajKaPanchangModel preDayModel;
    Toolbar toolBar_InputKundli;
    TabLayout tabs_input_kundli;
    String[] pageTitles;
    TextView titleTextView;
    ImageView moreImageView;
    ImageView toggleImageView, imgBackButton;
    ViewPagerAdapter adapter;
    public BeanDateTimeForPanchang beanDateTime;
    String cityId = "1261481";
    private ProgressBar progressBar;
    private HomeNavigationDrawerFragment drawerFragment;
    private ArrayList<Object> panchakDataList;
    private ArrayList<Object> badhraDataList;
    private String language;
    private RequestQueue queue;
    LinearLayout navView;
    FloatingActionButton fabOutputMaster;

    String heading = "";
    String subHeading = "";

    RelativeLayout ask_question_layout;
    TypeWriter tv_ask_quest;
    String conversationId;
    private final String GET_SUGGESTED_QUESTION_DATE = "panchang_suggested_questions_date";
    private final String SUGGESTED_QUESTION_KEY = "panchang_suggested_questions";

    HashMap<String, ArrayList<String>> questionMap;
    private boolean isKundliChatWindowShowing = false;

    public InputPanchangActivity() {
        super(R.string.app_name);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case SUB_ACTIVITY_USER_LOGIN: {

                    Bundle b = data.getExtras();
                    String loginName = b.getString("LOGIN_NAME");
                    String loginPwd = b.getString("LOGIN_PWD");
                    setUserLoginDetails(loginName, loginPwd);

                }
                break;
                case SUB_ACTIVITY_PLACE_SEARCH:
                    try {
                        if (data != null) {
                            Bundle bundle = data.getExtras();
                            BeanPlace place = CUtils.getPlaceObjectFromBundle(bundle);
                            if (place != null) {
                                CGlobal.getCGlobalObject().getHoroPersonalInfoObject().setPlace(place);
                                beanPlace = place;
                                CUtils.saveBeanPalce(InputPanchangActivity.this, beanPlace);
                                //updateDataOnPlaceChange(place); //commented by Abhishek bcz it is calling in onresume
                            }
                        }
                    } catch (Exception e) {

                    }
                    break;
            }

        }
    }

    private void setUserLoginDetails(String loginName, String loginPwd) {
        //panchangHomeMenuFragment.updateLoginDetials(true, loginName, loginPwd);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        beanPlace = getBeanPlace();

        // deeplink crash fixed by Ankit on 8/3/2019
        if (getIntent().getSerializableExtra("date") != null) {
            calendar = (Calendar) getIntent().getSerializableExtra("date");
        } else {
            calendar = Calendar.getInstance();
        }

        int month = calendar.get(Calendar.MONTH);
        setContentView(R.layout.activity_input_panchang);
        //spinnerScreenList = (Spinner) findViewById(R.id.spinnerScreenList);
        //setSpinner();

        isPopupLoginShown = false;

        heading = getResources().getString(R.string.pop_up_heading_panchnag);
        subHeading = getResources().getString(R.string.pop_up_sub_heading_panchnag);

        toggleImageView = (ImageView) findViewById(R.id.ivToggleImage);
        titleTextView = (TextView) findViewById(R.id.tvTitle);
        moreImageView = (ImageView) findViewById(R.id.imgMoreItem);


        imgWhatsApp = (ImageView) findViewById(R.id.imgWhatsApp);
        placeTV = (TextView) findViewById(R.id.place_tv);
        placeTV.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                openSearchPlace(beanPlace);
            }
        });
        placeTV.setVisibility(View.VISIBLE);
        placeTV.setText(beanPlace.getCityName().trim());
        if (CUtils.isPackageExisted(this, "com.whatsapp")) {

            //imgWhatsApp.setVisibility(View.VISIBLE);
            imgWhatsApp.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    shareContentData("com.whatsapp");
                }
            });
        }

        toggleImageView.setVisibility(View.VISIBLE);
        moreImageView.setVisibility(View.VISIBLE);
        setVisibilityOfMoreIconImage(moreImageView, getResources().getStringArray(
                R.array.panchang_menu_item_list), getResources().obtainTypedArray(
                R.array.panchang_menu_item_list_icons), panchang_menu_item_list_index);

        titleTextView.setTypeface(mediumTypeface);

        toolBar_InputKundli = (Toolbar) findViewById(R.id.tool_barAppModule);
        tabs_input_kundli = (TabLayout) findViewById(R.id.tabs);

        progressBar = (ProgressBar) findViewById(R.id.progressBar1);

        setSupportActionBar(toolBar_InputKundli);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        drawerFragment = (HomeNavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.myDrawerFrag);
        drawerFragment.setup(R.id.myDrawerFrag, (DrawerLayout) findViewById(R.id.drawerLayout), toolBar_InputKundli, getDrawerListItem(), getDrawerListItemIcon(), getDrawerListItemIndex());


        SELECTED_MODULE = getIntent()
                .getIntExtra(CGlobalVariables.MODULE_TYPE_KEY,
                        CGlobalVariables.MODULE_BASIC);

        pager = (ViewPager) findViewById(R.id.viewPager);

        ask_question_layout = findViewById(R.id.ask_question_layout);
        tv_ask_quest = findViewById(R.id.tv_ask_que);
        // new LoadAsynData().execute();
        progressBar.setVisibility(GONE);
        pageTitles = getResources().getStringArray(R.array.titles_list_panchang);

        setAdapter();

        //pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        if (SELECTED_MODULE == CGlobalVariables.MODULE_ASTROSAGE_PANCHANG) {
            // pager.setCurrentItem(0);
            setCurrentView(1, false);
        } else if (SELECTED_MODULE == CGlobalVariables.MODULE_RASHIFAL) {
            // pager.setCurrentItem(4);
            setCurrentView(2, false);
        } else if (SELECTED_MODULE == CGlobalVariables.MODULE_ASTROSAGE_HORA) {
            // pager.setCurrentItem(1);
            setCurrentView(3, false);
        } else if (SELECTED_MODULE == CGlobalVariables.MODULE_ASTROSAGE_CHOGADIA) {
            // pager.setCurrentItem(2);
            setCurrentView(4, false);
        } else if (SELECTED_MODULE == CGlobalVariables.MODULE_DO_GHATI_MUHURT) {
            //pager.setCurrentItem(3);
            setCurrentView(5, false);
        } else if (SELECTED_MODULE == CGlobalVariables.MODULE_RAHUKAAL) {
            // pager.setCurrentItem(4);
            setCurrentView(6, false);
        } else if (SELECTED_MODULE == CGlobalVariables.MODULE_PANCHAK) {
            // pager.setCurrentItem(4);
            setCurrentView(7, false);
        } else if (SELECTED_MODULE == CGlobalVariables.MODULE_BHADRA) {
            // pager.setCurrentItem(4);
            setCurrentView(8, false);
        } else if (SELECTED_MODULE == CGlobalVariables.MODULE_MUHURAT) {
            // pager.setCurrentItem(4);
            setCurrentView(9, false);
        } else if (SELECTED_MODULE == CGlobalVariables.MODULE_LAGNA) {
            // pager.setCurrentItem(4);
            setCurrentView(10, false);
        } else {
            setCurrentView(0, false);
        }

        onNewIntentPanchang(getIntent());
        // firstTimeAllFragment();

        /*CUtils.showAdvertisement(InputPanchangActivity.this,
                (LinearLayout) findViewById(R.id.advLayout));*/
        fabOutputMaster = findViewById(R.id.fabHome);
        navView = findViewById(R.id.nav_view);
        //navView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        customBottomNavigationFont(navView);
        setBottomNavigationText();
        fabOutputMaster.setOnClickListener(v -> {
            fabActions();
        });
        /*new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                        PopUpLogin popUpLogin = new PopUpLogin
                                (com.ojassoft.astrosage.varta.utils.CGlobalVariables.PANCHNAG,
                                        heading,
                                        subHeading,
                                        R.drawable.astrologer_icon_1);
                        popUpLogin.show(getSupportFragmentManager(), "PopUpFreeCall");

                } catch (Exception e) {
                    //
                }
            }
        }, 15000);*/

        if (ask_question_layout.getVisibility() == View.VISIBLE) {
            getSuggestQuestionForPanchangScreens();
        }
        conversationId = KundliHistoryDao.getInstance(this).getConversationId(  CGlobalVariables.CONVERS_START+CGlobalVariables.AI_PANCHANG_MODULE,getString(R.string.daily_panchang));
        //Log.e("KundliChatAi","Panchang Conversation Id = "+conversationId);
        ask_question_layout.setOnClickListener(v-> {
            if (isKundliChatWindowShowing) {
                return; //prevent to show chat window multiple times
            }
            if (com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(this)) {
                boolean isFirstTime = CUtils.getBooleanData(this, KUNDLI_AI_OPEN_FIRST_TIME_KEY, true);
                if (isFirstTime) {
                    KundliAIFirstTimeMsgFragment kundliAIFirstTimeMsgFragment = new KundliAIFirstTimeMsgFragment(() -> {
                        openKundliAIChatWIndow();
                        CUtils.saveBooleanData(this,KUNDLI_AI_OPEN_FIRST_TIME_KEY,false);
                    });
                    kundliAIFirstTimeMsgFragment.show(getSupportFragmentManager(), "kundliAIWelcomeMsg");

                    return;
                }
               openKundliAIChatWIndow();
            } else {
                isPopupLoginShown = true;
                AstrosageKundliApplication.isOpenVartaPopup = true;
                isKundliChatWindowShowing = true;
                Intent intent1 = new Intent(this, FlashLoginActivity.class);
                startActivity(intent1);
                /*PopUpLogin popUpLogin = new PopUpLogin
                        (com.ojassoft.astrosage.varta.utils.CGlobalVariables.KUNDALI,
                                "ONLY_LOGIN");
                popUpLogin.show(getSupportFragmentManager(), "PopUpLogin");
                isPopupLoginShown = true;
                AstrosageKundliApplication.isOpenVartaPopup = true;*/
                //startActivity(new Intent(OutputMasterActivity.this, LoginSignUpActivity.class));
            }
        });

    }

    private void openKundliAIChatWIndow() {
        int screenId = panchangScreenIds[pager.getCurrentItem()];
        ArrayList<String> suggestedQuestions = getSuggestedQuestionsForScreenId(screenId);
        Intent intent = new Intent(InputPanchangActivity.this, MiniChatWindow.class);
        intent.putStringArrayListExtra(MODULE_SUGGESTED_QUESTIONS_KEY, suggestedQuestions);
        intent.putExtra(CURRENT_SCREEN_ID_KEY, screenId);
        if (LANGUAGE_CODE == 1) {
            intent.putExtra(KEY_SCREEN_NAME, getRomanScreenName(pager.getCurrentItem()));
        } else {
            intent.putExtra(KEY_SCREEN_NAME, adapter.getPageTitle(pager.getCurrentItem()));
        }
        intent.putExtra(PANCHANG_LID_KEY, String.valueOf(beanPlace.getCityId()));
        intent.putExtra(PANCHANG_DATE_KEY, (beanDateTime.getDay()) + "-" + (beanDateTime.getMonth() + 1) + "-" + (beanDateTime.getYear()));
        intent.putExtra(KEY_CONVERSATION_ID,conversationId);
        intent.putExtra(SOURCE_OF_SCREEN, com.ojassoft.astrosage.varta.utils.CGlobalVariables.SOURCE_FROM_PANCHANG_SCREEN);
        startActivity(intent);
        isKundliChatWindowShowing = true;
    }

    public void updateBeanDateTime(boolean isToday) {
        if (beanDateTime == null) {
            beanDateTime = new BeanDateTimeForPanchang();
        }
        Calendar c = Calendar.getInstance();
        if (!isToday) {
            c.add(Calendar.DATE,1);
        }
        beanDateTime.setYear(c.get(Calendar.YEAR));
        beanDateTime.setMonth(c.get(Calendar.MONTH));
        beanDateTime.setDay(c.get(Calendar.DATE));
        //beanDateTime.setCalender(c.get(Calendar.MONTH), c.get(Calendar.DATE), c.get(Calendar.YEAR));
    }


    @Override
    public void sendToHora(int position) {
        if (adapter != null) {
            if (position == CGlobalVariables.MODULE_ASTROSAGE_PANCHANG) {
                setCurrentView(1, false);
            } else if (position == CGlobalVariables.MODULE_RASHIFAL) {
                setCurrentView(2, false);
            } else if (position == CGlobalVariables.MODULE_ASTROSAGE_HORA) {
                setCurrentView(3, false);
            } else if (position == CGlobalVariables.MODULE_ASTROSAGE_CHOGADIA) {
                setCurrentView(4, false);
            } else if (position == CGlobalVariables.MODULE_DO_GHATI_MUHURT) {
                setCurrentView(5, false);
            } else if (position == CGlobalVariables.MODULE_RAHUKAAL) {
                setCurrentView(6, false);
            } else if (position == CGlobalVariables.MODULE_PANCHAK) {
                setCurrentView(7, false);
            } else if (position == CGlobalVariables.MODULE_BHADRA) {
                setCurrentView(8, false);
            } else if (position == CGlobalVariables.MODULE_MUHURAT) {
                setCurrentView(9, false);
            } else if (position == CGlobalVariables.MODULE_LAGNA) {
                setCurrentView(10, false);
            }
        }
    }

    List<Integer> getDrawerListItemIndex() {
        try {
            //return CUtils.getDrawerListItemIndex(OutputMasterActivity.this, app_home_menu_item_list_index, module_list_index);
            return Arrays.asList(module_list_index_for_panchang);
        } catch (Exception ex) {
            //Log.i(ex.getMessage().toString());
            return null;
        }
    }

    private List<Drawable> getDrawerListItemIcon() {
        try {
            TypedArray itemsIcon2 = getResources().obtainTypedArray(R.array.module_icons_for_panchang);
            return CUtils.convertTypedArrayToArrayList(InputPanchangActivity.this, itemsIcon2, module_list_index_for_panchang);
        } catch (Exception ex) {
            //Log.i(ex.getMessage().toString());
            return null;
        }
    }

    private List<String> getDrawerListItem() {
        try {
            String[] menuItems2 = getResources().getStringArray(R.array.input_page_titles_list_panchang);
            return Arrays.asList(menuItems2);
        } catch (Exception ex) {
            //Log.i(ex.getMessage().toString());
            return null;
        }
    }


    private void setAdapter() {
        adapter = getAdapter();

        pager.setAdapter(adapter);
        //pager.setOffscreenPageLimit(8);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                if (LANGUAGE_CODE == 1) {
                    tv_ask_quest.animateTextInfinite(getString(R.string.ask_anything_to_kundli_hi,pageTitles[pager.getCurrentItem()]));
                } else {
                    tv_ask_quest.animateTextInfinite(getString(R.string.ask_anything_to_kundli,pageTitles[pager.getCurrentItem()]));
                }
                setPageTitles(position);
            }

            @Override
            public void onPageSelected(int position) {

                CUtils.googleAnalyticSendWitPlayServie(
                        InputPanchangActivity.this,
                        CGlobalVariables.GOOGLE_ANALYTIC_SCREEN,
                        CGlobalVariables.pagePanchangHoraChogdiaDoghatiRahukaal[position],
                        CGlobalVariables.pagePanchangHoraChogdiaDoghatiRahukaal[position]);

                setCurrentView(position, false);

                updatePlayButton();

                if (position == 11) {
                    placeTV.setVisibility(GONE);
                } else {
                    placeTV.setVisibility(View.VISIBLE);
                }

                if (position == 0) {
                    imgWhatsApp.setVisibility(GONE);
                } else {
                    imgWhatsApp.setVisibility(View.VISIBLE);
                }
                //updateDataOnPlaceChange(beanPlace);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        tabs_input_kundli.setupWithViewPager(pager);
        addCustomViewInTAbs();
    }

    private void addCustomViewInTAbs() {
        if (tabs_input_kundli != null) {
            // Iterate over all tabs and set the custom view
            for (int i = 0; i < tabs_input_kundli.getTabCount(); i++) {
                TabLayout.Tab tab = tabs_input_kundli.getTabAt(i);
                tab.setCustomView(adapter.getTabView(i));
            }
        }
    }

    @Override
    public void setVisibilityOfMoreIconImage(View view, final String[] subMenuItems, final TypedArray subMenuItemsIcon, final Integer[] menuIndex) {
        view.setOnClickListener(view1 -> {
            if (pager != null && pager.getCurrentItem() == 11) {
                settingForpopUpMenu(view1, getResources().getStringArray(
                                R.array.panchang_menu_item_list_without_share),
                        getResources().obtainTypedArray(R.array.panchang_menu_item_list_icons_without_share),
                        panchang_menu_item_list_index_without_share);
            } else {
                settingForpopUpMenu(view1, subMenuItems, subMenuItemsIcon, menuIndex);
            }
        });
    }

    private void setPageTitles(int pos) {
        titleTextView.setText(pageTitles[pos]);
    }


    ViewPagerAdapter getAdapter() {
        int moonSign = CUtils.getMoonSignIndex(InputPanchangActivity.this);
        //moonSign = CUtils.getIntData(InputPanchangActivity.this, "moon_sign", 0);

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager(), InputPanchangActivity.this);
        adapter.addFragment(PanchangDashboardFrag.newInstance("Panchang Dashboard"), pageTitles[0]);
        adapter.addFragment(PanchangInputFragment.newInstance("Panchang"), pageTitles[1]);
        adapter.addFragment(DailyHoroscopeFragment.newInstance(moonSign), pageTitles[2]);
        adapter.addFragment(HoraInputFragment.newInstance("Hora"), pageTitles[3]);
        adapter.addFragment(ChogadiaInputFragment.newInstance("Chogadia"), pageTitles[4]);
        adapter.addFragment(DoGhatiMuhurat.newInstance("Doghati"), pageTitles[5]);
        adapter.addFragment(RahuKaalInputFragment.newInstance("RahuKaal"), pageTitles[6]);
        adapter.addFragment(PanchakInputFragment.newInstance("Panchak"), pageTitles[7]);
        adapter.addFragment(BhadraInputFragment.newInstance("Bhadra"), pageTitles[8]);
        adapter.addFragment(PanchangMuhuratFragment.newInstance("Muhurat"), pageTitles[9]);
        adapter.addFragment(LagnaInputFragment.newInstance("Lagna"), pageTitles[10]);
        adapter.addFragment(AskQuestionsFragment.newInstance(com.ojassoft.astrosage.varta.utils.CGlobalVariables.PANCHNAG), pageTitles[11]);

        return adapter;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    protected void onStart() {
        super.onStart();
        //EasyTracker.getInstance().activityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //EasyTracker.getInstance().activityStop(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setBottomNavigationText();
        /*CUtils.showAdvertisement(InputPanchangActivity.this,
                (LinearLayout) findViewById(R.id.advLayout));*/

        isKundliChatWindowShowing = false;
        if ((!CUtils.isDhruvPlan(InputPanchangActivity.this) || CUtils.getUserPurchasedPlanFromPreference(this) != CGlobalVariables.KUNDLI_AI_PRIMIUM_PLAN_ID_11) && !isPopupLoginShown) {
            //Log.e("SAN ", " openConfuseAboutKundli() ");
            openConfuseAboutKundli();
        }

        try {
            if (beanPlace == null) { // chaned by Abhishek
                beanPlace = getBeanPlace();
            }
            if (beanPlace != null) {
                updateDataOnPlaceChange(beanPlace);
            }
            drawerFragment.updateLayout(getDrawerListItem(), getDrawerListItemIcon(), getDrawerListItemIndex());
        } catch (Exception ex) {
            //Log.i("TAG", ex.getMessage());
        }

        if (LANGUAGE_CODE == 1) {
            tv_ask_quest.animateTextInfinite(getString(R.string.ask_anything_to_kundli_hi,pageTitles[pager.getCurrentItem()]));
        } else {
            tv_ask_quest.animateTextInfinite(getString(R.string.ask_anything_to_kundli,pageTitles[pager.getCurrentItem()]));
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        /*CUtils.removeAdvertisement(InputPanchangActivity.this,
                (LinearLayout) findViewById(R.id.advLayout));*/
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        // addCustomViewInTAbs();
    }


    @Override
    public void logoutFromAstroSageCloud(boolean isShowToast) {
        drawerFragment.updateLoginDetials(false, "", "", getDrawerListItem(), getDrawerListItemIcon(), getDrawerListItemIndex());
        if (isShowToast) {
            MyCustomToast mct = new MyCustomToast(InputPanchangActivity.this,
                    InputPanchangActivity.this.getLayoutInflater(), InputPanchangActivity.this,
                    regularTypeface);
            mct.show(getResources().getString(R.string.sign_out_success));
        }
    }


    public void shareMessageWithWhatsApp() {
        shareContentData("com.whatsapp");
    }

    @Override
    protected void shareContentData(String packageName) {
        try {

            Fragment page = adapter.getItem(pager.getCurrentItem());

            if (pager.getCurrentItem() == 0 && page != null) {
                ((PanchangDashboardFrag) page).shareContentData(packageName, beanPlace);
            } else if (pager.getCurrentItem() == 1 && page != null) {
                ((PanchangInputFragment) page).shareContentData(packageName, beanPlace);
            } else if (pager.getCurrentItem() == 2 && page != null) {
                ((DailyHoroscopeFragment) page).shareData(packageName);
            } else if (pager.getCurrentItem() == 3 && page != null) {
                ((HoraInputFragment) page).shareContentData(packageName, beanPlace);
            } else if (pager.getCurrentItem() == 4 && page != null) {
                ((ChogadiaInputFragment) page).shareContentData(packageName, beanPlace);
            } else if (pager.getCurrentItem() == 5 && page != null) {
                ((DoGhatiMuhurat) page).shareContentData(packageName, beanPlace);
            } else if (pager.getCurrentItem() == 6 && page != null) {
                ((RahuKaalInputFragment) page).shareContentData(packageName, beanPlace);
            } else if (pager.getCurrentItem() == 7 && page != null) {
                ((PanchakInputFragment) page).shareContentData(packageName);
            } else if (pager.getCurrentItem() == 8 && page != null) {
                ((BhadraInputFragment) page).shareContentData(packageName);
            } else if (pager.getCurrentItem() == 9 && page != null) {
                ((PanchangMuhuratFragment) page).shareContentData(packageName);
            } else if (pager.getCurrentItem() == 10 && page != null) {
                ((LagnaInputFragment) page).shareContentData(packageName);
            } else if (pager.getCurrentItem() == 11 && page != null) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, getAppDownloadTextForSharing());
                sendIntent.setType("text/plain");
                sendIntent.setPackage("com.whatsapp");
                startActivity(sendIntent);
            }

        } catch (Exception ex) {
            // Toast.makeText(this,ex.getMessage().toString(),Toast.LENGTH_SHORT).show();
            //Log.e(ex.getMessage().toString());
            Log.e("whatsAppData", "shareContentData() ex="+ex.toString());
        }
    }

    @Override
    public void updateLayout(int id) {

        // pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
        if (adapter == null || pager == null) {
            setAdapter();
        }
        // pager.setCurrentItem(id);
        setCurrentView(id, true);
    }

    public void goToLogin() {

        if (!CUtils.isUserLogedIn(InputPanchangActivity.this)) {

            Intent intent = new Intent(InputPanchangActivity.this,
                    ActLogin.class);
            intent.putExtra("callerActivity",
                    CGlobalVariables.HOME_INPUT_SCREEN);
            startActivityForResult(intent, SUB_ACTIVITY_USER_LOGIN);
        } else {
            //panchangHomeMenuFragment.updateLoginDetials(false, "", "");
            MyCustomToast mct = new MyCustomToast(InputPanchangActivity.this,
                    InputPanchangActivity.this.getLayoutInflater(),
                    InputPanchangActivity.this, regularTypeface);
            mct.show(getResources().getString(R.string.sign_out_success));
        }
        //getSlidingMenu().showContent();
    }

    @Override
    protected void onDestroy() {
        BeanDateTimeForPanchang beanDateTime = null;
        CUtils.saveDateTimeForPanchang(InputPanchangActivity.this, beanDateTime, false);

        super.onDestroy();

    }

    /**
     * @param intent
     * @author : Amit Rautela
     * This method is used for deep linking
     */
    private void onNewIntentPanchang(Intent intent) {

        try {

            String action = intent.getAction();
            Uri data = intent.getData();
            if (Intent.ACTION_VIEW.equals(action) && data != null) {

                String chogadia = "chogadia";
                String hora = "hora";
                String do_ghati_muhurat = "do-ghati-muhurat";
                String rahu_kal = "rahukaal.asp";

                String url = data.toString();

                if (url.contains(hora)) {
                    // pager.setCurrentItem(1);
                    setCurrentView(2, false);
                } else if (url.contains(chogadia)) {
                    // pager.setCurrentItem(2);
                    setCurrentView(3, false);
                } else if (url.contains(do_ghati_muhurat)) {
                    //pager.setCurrentItem(3);
                    setCurrentView(4, false);
                } else if (url.contains(rahu_kal)) {
                    // pager.setCurrentItem(4);
                    setCurrentView(5, false);
                } else {
                    // pager.setCurrentItem(0);
                    setCurrentView(0, false);
                }
            }
        } catch (Exception ex) {
            //Log.i("TAG"+ex.getMessage());
        }
    }

    private void setCurrentView(int index, boolean smoothScroll) {

        LANGUAGE_CODE = AstrosageKundliApplication.getAppContext().getLanguageCode();
        com.ojassoft.astrosage.varta.utils.CUtils.getRobotoFont(InputPanchangActivity.this, LANGUAGE_CODE, com.ojassoft.astrosage.varta.utils.CGlobalVariables.regular);

        pager.setCurrentItem(index, smoothScroll);
        if (tabs_input_kundli != null && adapter != null) {
            adapter.setAlpha(index, tabs_input_kundli);
        }
        if (LANGUAGE_CODE == 1) {
            tv_ask_quest.animateTextInfinite(getString(R.string.ask_anything_to_kundli_hi,pageTitles[index]));
        } else {
            tv_ask_quest.animateTextInfinite(getString(R.string.ask_anything_to_kundli,pageTitles[index]));
        }
    }


    /**
     * get panchak detail
     */
    public void getPanchakDetails() {
        final String url = CGlobalVariables.panchakApiUrl;
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null && !response.isEmpty()) {
                            try {
                                JSONObject jsonObject = new JSONObject(response);
                                JSONArray jsonArray = jsonObject.optJSONArray("festivalapidata");
                                Gson gson = new Gson();
                                List<FestDataDetail> festDataDetails = Arrays.asList(gson.fromJson(jsonArray.toString(), FestDataDetail[].class));
                                panchakDataList.addAll(festDataDetails);

                                if (panchakDataList == null) {
                                    panchakDataList = new ArrayList<>();
                                } else {
                                    panchakDataList.clear();
                                }
                                if (panchakDataList != null) {
                                    //     mRecyclerView.setAdapter(new CustomAdapterPanchaknBhadra(context, panchakDataList, monthArray, weekArray));
                                }

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e("Error Through" + error.getMessage());

                if (error instanceof TimeoutError) {
                    VolleyLog.d("TimeoutError: " + error.getMessage());
                    //If timeout occur it will again hit the request for get the product list
                    //      loadAstroShopData();
                } else if (error instanceof NoConnectionError) {
                    VolleyLog.d("NoConnectionError: " + error.getMessage());
                } else if (error instanceof AuthFailureError) {
                    VolleyLog.d("AuthFailureError: " + error.getMessage());
                    //TODO
                } else if (error instanceof ServerError) {
                    //TODO
                    VolleyLog.d("ServerError: " + error.getMessage());
                } else if (error instanceof NetworkError) {
                    //TODO
                    VolleyLog.d("NetworkError: " + error.getMessage());
                } else if (error instanceof ParseError) {
                    //TODO
                    VolleyLog.d("ParseError: " + error.getMessage());
                }
            }
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }


            @Override
            public Map<String, String> getParams() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("key", CUtils.getApplicationSignatureHashCode(getApplicationContext()));
                headers.put("language", language);
                headers.put("date", "1-" + String.valueOf(beanDateTime.getMonth() + 1) + "-" + String.valueOf(beanDateTime.getYear()));
                headers.put("lid", cityId);
                headers.put("isapi", "1");
                return headers;
            }

        };


        int socketTimeout = 60000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        if (2019 == beanDateTime.getYear()) {
            stringRequest.setShouldCache(true);
        } else {
            stringRequest.setShouldCache(false);
        }
        queue.add(stringRequest);
    }


    public void openSearchPlace(BeanPlace beanPlace) {
        Intent intent = new Intent(InputPanchangActivity.this, ActPlaceSearch.class);
        intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, SELECTED_MODULE);
        intent.putExtra(CGlobalVariables.PLACE_BEAN_KEY, beanPlace);
        startActivityForResult(intent, SUB_ACTIVITY_PLACE_SEARCH);
    }

    private void updateDataOnPlaceChange(BeanPlace place) {
        placeTV.setText(place.getCityName().trim());
        int cureentPage = pager.getCurrentItem();
        Fragment currentFrag = adapter.getFragment(cureentPage);
        if (currentFrag instanceof PanchangDashboardFrag) {
            ((PanchangDashboardFrag) currentFrag).updateAfterPlaceSelect(place);
        } else if (currentFrag instanceof PanchangInputFragment) {
            ((PanchangInputFragment) currentFrag).updateAfterPlaceSelect(place);
        } else if (currentFrag instanceof HoraInputFragment) {
            ((HoraInputFragment) currentFrag).updateAfterPlaceSelect(place);
        } else if (currentFrag instanceof ChogadiaInputFragment) {
            ((ChogadiaInputFragment) currentFrag).updateAfterPlaceSelect(place);
        } else if (currentFrag instanceof DoGhatiMuhurat) {
            ((DoGhatiMuhurat) currentFrag).updateAfterPlaceSelect(place);
        } else if (currentFrag instanceof RahuKaalInputFragment) {
            ((RahuKaalInputFragment) currentFrag).updateAfterPlaceSelect(place);
        } else if (currentFrag instanceof PanchakInputFragment) {
            ((PanchakInputFragment) currentFrag).updateAfterPlaceSelect(place);
        } else if (currentFrag instanceof BhadraInputFragment) {
            ((BhadraInputFragment) currentFrag).updateAfterPlaceSelect(place);
        } else if (currentFrag instanceof LagnaInputFragment) {
            ((LagnaInputFragment) currentFrag).updateAfterPlaceSelect(place);
        }
    }

    BeanPlace getBeanPlace() {
        BeanPlace beanPlace;
        if (getIntent().getSerializableExtra("place") != null && !getIntent().getSerializableExtra("place").equals("")) {
            beanPlace = (BeanPlace) getIntent().getSerializableExtra("place");
            Log.e("ForPanchang", "InputPanchang  beanPlace1=" + beanPlace);
        } else {
            beanPlace = CUtils.getBeanPalce(this);
            Log.e("ForPanchang", "InputPanchang  beanPlace2=" + beanPlace);
        }
        if (beanPlace == null) {
            beanPlace = CUtils.getDefaultPlace();
            Log.e("ForPanchang", "InputPanchang  beanPlace2=" + beanPlace);
        }
        return beanPlace;
    }

    class LoadAsynData extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                Thread.sleep(50);
            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            progressBar.setVisibility(GONE);
            pageTitles = getResources().getStringArray(R.array.titles_list_panchang);

            setAdapter();

            //pager.setAdapter(new MyPagerAdapter(getSupportFragmentManager()));
            if (SELECTED_MODULE == CGlobalVariables.MODULE_ASTROSAGE_PANCHANG) {
                // pager.setCurrentItem(0);
                setCurrentView(1, false);
            } else if (SELECTED_MODULE == CGlobalVariables.MODULE_RASHIFAL) {
                // pager.setCurrentItem(4);
                setCurrentView(2, false);
            } else if (SELECTED_MODULE == CGlobalVariables.MODULE_ASTROSAGE_HORA) {
                // pager.setCurrentItem(1);
                setCurrentView(3, false);
            } else if (SELECTED_MODULE == CGlobalVariables.MODULE_ASTROSAGE_CHOGADIA) {
                // pager.setCurrentItem(2);
                setCurrentView(4, false);
            } else if (SELECTED_MODULE == CGlobalVariables.MODULE_DO_GHATI_MUHURT) {
                //pager.setCurrentItem(3);
                setCurrentView(5, false);
            } else if (SELECTED_MODULE == CGlobalVariables.MODULE_RAHUKAAL) {
                // pager.setCurrentItem(4);
                setCurrentView(6, false);
            } else if (SELECTED_MODULE == CGlobalVariables.MODULE_PANCHAK) {
                // pager.setCurrentItem(4);
                setCurrentView(7, false);
            } else if (SELECTED_MODULE == CGlobalVariables.MODULE_BHADRA) {
                // pager.setCurrentItem(4);
                setCurrentView(8, false);
            } else if (SELECTED_MODULE == CGlobalVariables.MODULE_MUHURAT) {
                // pager.setCurrentItem(4);
                setCurrentView(9, false);
            } else if (SELECTED_MODULE == CGlobalVariables.MODULE_LAGNA) {
                // pager.setCurrentItem(4);
                setCurrentView(10, false);
            } else {
                setCurrentView(0, false);
            }

            onNewIntentPanchang(getIntent());
            // firstTimeAllFragment();

            /*CUtils.showAdvertisement(InputPanchangActivity.this,
                    (LinearLayout) findViewById(R.id.advLayout));*/
        }
    }

    /*BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.actapp_nav_home:
                            Intent intent = new Intent(InputPanchangActivity.this, ActAppModule.class);
                            startActivity(intent);
                            return true;
                        case R.id.actapp_nav_call:
                            com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_INPUT_PANCHANG_BOTTOM_CALL, FIREBASE_EVENT_ITEM_CLICK, "");
                            createSession(InputPanchangActivity.this, INPUT_PANCHANG_BOTTOM_BAR_CALL_PARTNER_ID);
                            switchToConsultTab(FILTER_TYPE_CALL, InputPanchangActivity.this);
                            return true;
                        case R.id.actapp_nav_live:
                            fabActions();
                            return true;
                        case R.id.actapp_nav_chat:
                            com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_INPUT_PANCHANG_BOTTOM_CHAT, FIREBASE_EVENT_ITEM_CLICK, "");
                            createSession(InputPanchangActivity.this, INPUT_PANCHANG_BOTTOM_BAR_CHAT_PARTNER_ID);
                            switchToConsultTab(FILTER_TYPE_CHAT, InputPanchangActivity.this);
                            return true;
                        case R.id.actapp_nav_account:
                            com.ojassoft.astrosage.varta.utils.CUtils.openAccountScreen(InputPanchangActivity.this);
                            return true;
                    }
                    return false;
                }
            };*/

    private void fabActions() {
        try {
            //boolean liveStreamingEnabledForAstrosage = com.ojassoft.astrosage.varta.utils.CUtils.getBooleanData(this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.liveStreamingEnabledForAstrosage, false);
            boolean liveStreamingEnabledForAstrosage = true;
            if (!liveStreamingEnabledForAstrosage) { //fetch data according to tagmanag
                com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_INPUT_PANCHANG_BOTTOM_KUNDLI, FIREBASE_EVENT_ITEM_CLICK, "");
                createSession(InputPanchangActivity.this, INPUT_PANCHANG_BOTTOM_BAR_KUNDLI_PARTNER_ID);
                openKundliActivity(this);
            } else {
                com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_INPUT_PANCHANG_BOTTOM_LIVE, FIREBASE_EVENT_ITEM_CLICK, "");
                createSession(InputPanchangActivity.this, INPUT_PANCHANG_BOTTOM_BAR_LIVE_PARTNER_ID);
                gotoAllLiveActivityFromLiveIcon(InputPanchangActivity.this);
            }
        } catch (Exception e) {
            //
        }
    }

    public void customBottomNavigationFont(final View v) {
        try {
            if (v instanceof ViewGroup) {
                ViewGroup vg = (ViewGroup) v;
                for (int i = 0; i < vg.getChildCount(); i++) {
                    View child = vg.getChildAt(i);
                    customBottomNavigationFont(child);
                }
            } else if (v instanceof TextView) {
                FontUtils.changeFont(InputPanchangActivity.this, ((TextView) v), com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
                ((TextView) v).setTextSize(11);
                ((TextView) v).setTextColor(getResources().getColor(R.color.black));
                // added by vishal
                ((TextView) v).setEllipsize(TextUtils.TruncateAt.END);
                ((TextView) v).setPadding(0, 0, 0, 0);
            }
        } catch (Exception e) {
        }
    }

    private void setBottomNavigationText() {

        // find MenuItem you want to change
        ImageView navCallImg = navView.findViewById(R.id.imgViewCall);
        TextView navCallTxt = navView.findViewById(R.id.txtViewCall);
        TextView navChatTxt = navView.findViewById(R.id.txtViewChat);
        ImageView navHisImg = navView.findViewById(R.id.imgViewHistory);
        TextView navHisTxt = navView.findViewById(R.id.txtViewHistory);
        TextView navLive = navView.findViewById(R.id.txtViewLive);

        boolean isAIChatDisplayed = com.ojassoft.astrosage.utils.CUtils.isAIChatDisplayed(this);
        if(isAIChatDisplayed){
            navChatTxt.setText(getResources().getString(R.string.text_ask));
            navCallTxt.setText(getResources().getString(R.string.ai_astrologer));
            Glide.with(navCallImg).load(R.drawable.ic_ai_astrologer_unselected).into(navCallImg);
        } else {
            navChatTxt.setText(getResources().getString(R.string.chat_now));
            navCallTxt.setText(getResources().getString(R.string.call));
            navCallImg.setImageResource(R.drawable.nav_call_icons);
        }

        // set new title to the MenuItem
        CUtils.handleFabOnActivities(this, fabOutputMaster, navLive);

        if (com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(InputPanchangActivity.this)) {
                navHisTxt.setText(getResources().getString(R.string.history));
                navHisImg.setImageResource(R.drawable.nav_more_icons);
            } else {
                navHisTxt.setText(getResources().getString(R.string.sign_up));
                navHisImg.setImageResource(R.drawable.nav_profile_icons);
            }
        //navView.getMenu().setGroupCheckable(0, false, true);
        //setting Click listener
        navView.findViewById(R.id.bottom_nav_home).setOnClickListener(navbarItemSelectedListener);
        navView.findViewById(R.id.bottom_nav_call).setOnClickListener(navbarItemSelectedListener);
        navView.findViewById(R.id.bottom_nav_live).setOnClickListener(navbarItemSelectedListener);
        navView.findViewById(R.id.bottom_nav_chat).setOnClickListener(navbarItemSelectedListener);
        navView.findViewById(R.id.bottom_nav_history).setOnClickListener(navbarItemSelectedListener);
    }

    @Override
    public void clickCall() {
        com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_INPUT_PANCHANG_BOTTOM_CALL, FIREBASE_EVENT_ITEM_CLICK, "");
        createSession(InputPanchangActivity.this, INPUT_PANCHANG_BOTTOM_BAR_CALL_PARTNER_ID);
        super.clickCall();
    }

    @Override
    public void clickLive() {
        fabActions();
    }

    @Override
    public void clickChat() {
        com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_INPUT_PANCHANG_BOTTOM_CHAT, FIREBASE_EVENT_ITEM_CLICK, "");
        createSession(InputPanchangActivity.this, INPUT_PANCHANG_BOTTOM_BAR_CHAT_PARTNER_ID);
        super.clickChat();
    }


    public String getAppDownloadTextForSharing(){
        String shareText = "Download " + CUtils.getMyApplicationName(this)
                + " App: \n" +
                "https://go.astrosage.com/akwa";
        return shareText;
    }

    private void getSuggestQuestionForPanchangScreens() {
        try {
            // one day caching
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            Date curentDate = new Date();
            String todayDate = formatter.format(curentDate);
            String getSuggestedQuestionDate = CUtils.getStringData(this,GET_SUGGESTED_QUESTION_DATE,"");
            int lang = LANGUAGE_CODE;
            String suggestedQuestionLangKey = SUGGESTED_QUESTION_KEY+"_"+lang;

            if(getSuggestedQuestionDate.equalsIgnoreCase(todayDate)){
                String suggestedQuestion = CUtils.getStringData(this,suggestedQuestionLangKey,"");
                if(!TextUtils.isEmpty(suggestedQuestion)){ //check in local
                    parseSuggestedQuestion(suggestedQuestion, suggestedQuestionLangKey);
                    return;
                }
            }

            Call<ResponseBody> call = RetrofitClient.getAIInstance().create(ApiList.class).getSuggestedQuestionModule(getQuestionListParams(lang));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {

                    try {
                        if (response.isSuccessful() && response.body() != null) {
                            String responseString = response.body().string();
                            //Log.e("TestQuestion", "onResponse: " + responseString);
                            parseSuggestedQuestion(responseString, suggestedQuestionLangKey);
                            CUtils.saveStringData(InputPanchangActivity.this,GET_SUGGESTED_QUESTION_DATE,todayDate);
                        }

                    } catch (Exception e) {
                        //
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable throwable) {

                }
            });
        } catch (Exception e) {
            //
        }
    }

    private void parseSuggestedQuestion(String responseString, String suggestedQuestionLangKey){
        try {
            if (questionMap == null) {
                questionMap = new HashMap<>();
            }
            JSONObject jsonObject = new JSONObject(responseString);
            CUtils.saveStringData(this,suggestedQuestionLangKey,jsonObject.toString());
            Iterator<String> keys = jsonObject.keys();

            Gson gson = new Gson();
            // Iterate over the keys
            while (keys.hasNext()) {
                String key = keys.next();
                JSONArray jsonArray = jsonObject.getJSONArray(key);
                Type listType = new TypeToken<ArrayList<String>>() {
                }.getType();
                ArrayList<String> questionList = (gson.fromJson(jsonArray.toString(), listType));

                questionMap.put(key, questionList);
            }
        } catch (Exception e){
            //
        }
    }

    private Map<String, String> getQuestionListParams(int lang) {
        Map<String, String> headers = new HashMap<>();

        headers.put("lang",String.valueOf(lang));
        headers.put("moduleid", String.valueOf(CGlobalVariables.AI_PANCHANG_MODULE));
        headers.put(APP_KEY, getApplicationSignatureHashCode(this));
        headers.put("methodname", "suggestedquesakmodules");

        return headers;
    }

    private ArrayList<String> getSuggestedQuestionsForScreenId(int screenId) {
        try {
            return new ArrayList<>(questionMap.get(String.valueOf(screenId)));
        } catch (Exception e) {
            return null;
        }
    }

    private String getRomanScreenName(int position) {
        Resources resources = getLocaleResources(this, "en");
        String[] titles = resources.getStringArray(R.array.titles_list_panchang);
        return titles[position];
    }

    Handler handler;
    Runnable runnable;
    private void openConfuseAboutKundli() {
        try {
            if (handler != null && runnable != null) {
                handler.removeCallbacks(runnable);
            }
        } catch (Exception e) {
            //
        }
        handler = new Handler();
        runnable = () -> {
            try {
                if (!isKundliChatWindowShowing) {
                    LANGUAGE_CODE = CUtils.getLanguageCodeFromPreference(InputPanchangActivity.this);
                    CUtils.getRobotoFont(getApplicationContext(), LANGUAGE_CODE, CGlobalVariables.regular);
                    int planId = CUtils.getUserPurchasedPlanFromPreference(InputPanchangActivity.this);
                    if (planId != CGlobalVariables.PLATINUM_PLAN_ID && planId != CGlobalVariables.PLATINUM_PLAN_ID_9 && planId != CGlobalVariables.PLATINUM_PLAN_ID_10 && planId != CGlobalVariables.KUNDLI_AI_PRIMIUM_PLAN_ID_11) {
                        try {
                            if (AstrosageKundliApplication.popUpLogin != null) {
                                AstrosageKundliApplication.popUpLogin.dismiss();
                            }
                        } catch (Exception e) {
                            //
                        }
                        AstrosageKundliApplication.popUpLogin = new PopUpLogin
                                (com.ojassoft.astrosage.varta.utils.CGlobalVariables.HOROSCOPE_MATCHING,
                                        heading,
                                        subHeading,
                                        R.drawable.astrologer_icon_2);
                        AstrosageKundliApplication.popUpLogin.show(getSupportFragmentManager(), "PopUpFreeCall");
                        isPopupLoginShown = true;
                    }

                }
            } catch (Exception e) {
                //
            }
        };
        handler.postDelayed(runnable, 15000);
    }

}
