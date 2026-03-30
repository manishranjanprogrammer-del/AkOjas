package com.ojassoft.astrosage.ui.act;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;

import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AdData;
import com.ojassoft.astrosage.beans.BeanDateTime;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.customadapters.ViewPagerAdapter;
import com.ojassoft.astrosage.misc.CGenerateAppViews;
import com.ojassoft.astrosage.model.NumerologyOutputModel;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.fragments.AskQuestionsFragment;
import com.ojassoft.astrosage.ui.fragments.KundliAIFirstTimeMsgFragment;
import com.ojassoft.astrosage.ui.fragments.NumerologyCareerFragment;
import com.ojassoft.astrosage.ui.fragments.NumerologyDestinyFragment;
import com.ojassoft.astrosage.ui.fragments.NumerologyFastFragment;
import com.ojassoft.astrosage.ui.fragments.NumerologyHealthFragment;
import com.ojassoft.astrosage.ui.fragments.NumerologyHomeFragment;
import com.ojassoft.astrosage.ui.fragments.NumerologyLoShuGridFragment;
import com.ojassoft.astrosage.ui.fragments.NumerologyNameFragment;
import com.ojassoft.astrosage.ui.fragments.NumerologyPlaceFragment;
import com.ojassoft.astrosage.ui.fragments.NumerologyRadicalFragment;
import com.ojassoft.astrosage.ui.fragments.NumerologySpecialFragment;
import com.ojassoft.astrosage.ui.fragments.NumerologyTimeFragment;
import com.ojassoft.astrosage.ui.fragments.NumerologyYantraFragment;
import com.ojassoft.astrosage.utils.CGlobal;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.PopUpLogin;
import com.ojassoft.astrosage.varta.dao.KundliHistoryDao;
import com.ojassoft.astrosage.varta.ui.MiniChatWindow;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.varta.utils.TypeWriter;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;

import static com.ojassoft.astrosage.utils.CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_NUMEROLOGY_CALC_O_BOTTOM_CALL;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_NUMEROLOGY_CALC_O_BOTTOM_CHAT;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_NUMEROLOGY_CALC_O_BOTTOM_KUNDLI;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_NUMEROLOGY_CALC_O_BOTTOM_LIVE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_DOB;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_NAME;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_NUMROLOGY_MODEL;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_TYPE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KUNDLI_AI_OPEN_FIRST_TIME_KEY;
import static com.ojassoft.astrosage.utils.CGlobalVariables.MODULE_SUGGESTED_QUESTIONS_KEY;
import static com.ojassoft.astrosage.utils.CGlobalVariables.NUMEROLOGY_CALC_O_BOTTOM_BAR_CALL_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.NUMEROLOGY_CALC_O_BOTTOM_BAR_CHAT_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.NUMEROLOGY_CALC_O_BOTTOM_BAR_KUNDLI_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.NUMEROLOGY_CALC_O_BOTTOM_BAR_LIVE_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.NUMEROLOGY_KARMIC13;
import static com.ojassoft.astrosage.utils.CGlobalVariables.NUMEROLOGY_KARMIC14;
import static com.ojassoft.astrosage.utils.CGlobalVariables.NUMEROLOGY_KARMIC16;
import static com.ojassoft.astrosage.utils.CGlobalVariables.NUMEROLOGY_KARMIC19;
import static com.ojassoft.astrosage.utils.CGlobalVariables.NUMEROLOGY_MASTER11;
import static com.ojassoft.astrosage.utils.CGlobalVariables.NUMEROLOGY_MASTER22;
import static com.ojassoft.astrosage.utils.CGlobalVariables.numerologyScreenIds;
import static com.ojassoft.astrosage.utils.CUtils.createSession;
import static com.ojassoft.astrosage.utils.CUtils.isPopupLoginShown;
import static com.ojassoft.astrosage.utils.CUtils.openKundliActivity;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.*;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.APP_PREFS_AppLanguage;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.CURRENT_SCREEN_ID_KEY;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANGUAGE_CODE;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.NUMERO_CLNY_TYPE_KEY;
import static com.ojassoft.astrosage.varta.utils.CUtils.getApplicationSignatureHashCode;
import static com.ojassoft.astrosage.varta.utils.CUtils.getLocaleResources;
import static com.ojassoft.astrosage.varta.utils.CUtils.gotoAllLiveActivityFromLiveIcon;

import org.json.JSONArray;
import org.json.JSONObject;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created By Abhishek Raj
 */
public class NumerologyCalculatorOutputActivity extends BaseInputActivity {

    public String name;
    public String dob;
    public int type;
    public NumerologyOutputModel numerologyOutputModel;
    Activity currentActivity;
    Context context;
    private Toolbar tool_barAppModule;
    private TabLayout tabLayout;
    private TextView tvTitle;
    private ViewPager mViewPager;
    private ViewPagerAdapter adapter;
    public int day;
    private AdData topAdData;
    private ArrayList<AdData> adList;
    LinearLayout navView;
    FloatingActionButton fabOutputMaster;
    String conversationId;
    String heading = "";
    String subHeading = "";
    KundliHistoryDao chatDao;
    RelativeLayout ask_question_layout;
    TypeWriter tv_ask_quest;
    HashMap<String, ArrayList<String>> questionMap;

    private final String GET_SUGGESTED_QUESTION_DATE = "numero_suggested_questions_date";
    private final String SUGGESTED_QUESTION_KEY = "numero_suggested_questions";


    public NumerologyCalculatorOutputActivity() {
        super(R.string.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numerology_calculator_output);
        initContext();
        initViews();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (LANGUAGE_CODE == 1) {
            tv_ask_quest.animateTextInfinite(getString(R.string.ask_anything_to_kundli_hi, adapter.getPageTitle(mViewPager.getCurrentItem())));
        } else {
            tv_ask_quest.animateTextInfinite(getString(R.string.ask_anything_to_kundli, adapter.getPageTitle(mViewPager.getCurrentItem())));
        }
        /*CUtils.showAdvertisement(NumerologyCalculatorOutputActivity.this,
                (LinearLayout) findViewById(R.id.advLayout));*/
    }

    private void initViews() {
        tool_barAppModule = findViewById(R.id.tool_barAppModule);
        tool_barAppModule.setNavigationIcon(R.drawable.ic_back_arrow);
        setSupportActionBar(tool_barAppModule);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setVisibility(View.VISIBLE);
        mViewPager = findViewById(R.id.viewpager);
        tvTitle = findViewById(R.id.tvTitle);
        ask_question_layout = findViewById(R.id.ask_question_layout);
        tvTitle.setText(getString(R.string.title_numrology_output));
        tv_ask_quest = findViewById(R.id.tv_ask_que);
        tvTitle.setTypeface(regularTypeface);

        name = getIntent().getStringExtra(KEY_NAME);
        dob = getIntent().getStringExtra(KEY_DOB);
        type = getIntent().getIntExtra(KEY_TYPE, 0);
        try {
            numerologyOutputModel = getIntent().getParcelableExtra(KEY_NUMROLOGY_MODEL);
        }catch (Exception e){
            //
        }
        if (!TextUtils.isEmpty(dob)) {
            day = Integer.parseInt(dob.split("-")[0]);
        }
        chatDao = KundliHistoryDao.getInstance(this);
        conversationId = chatDao.getConversationId(name.split(" ")[0] + dob.replace("-",""),getString(R.string.title_numrology_input));
        Log.e("KundliAiChat","Output Conversation Id = " + conversationId);
        setupViewPager();

        heading = getResources().getString(R.string.pop_up_heading_num);
        subHeading = getResources().getString(R.string.pop_up_sub_heading_num);

        fabOutputMaster = findViewById(R.id.fabHome);
        navView = findViewById(R.id.nav_view);
        //navView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        customBottomNavigationFont(navView);
        setBottomNavigationText();
        fabOutputMaster.setOnClickListener(v->{
            fabActions();
        });
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                try {
                        PopUpLogin popUpLogin = new PopUpLogin
                                (NUMEROLOGY,
                                        heading,
                                        subHeading,
                                        R.drawable.astrologer_icon_3);
                        popUpLogin.show(getSupportFragmentManager(), "PopUpFreeCall");
                }catch (Exception e){
                    //
                }
            }
        }, 15000);

        if (ask_question_layout.getVisibility() == View.VISIBLE) {
            getSuggestQuestionForNumerologyScreens();
        }

        ask_question_layout.setOnClickListener(v-> {
            if (com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(this)) {
                boolean isFirstTime = CUtils.getBooleanData(this, KUNDLI_AI_OPEN_FIRST_TIME_KEY, true);
                if (isFirstTime) {
                    KundliAIFirstTimeMsgFragment kundliAIFirstTimeMsgFragment = new KundliAIFirstTimeMsgFragment(() -> {
                        openKundliAIChatWindow();
                        CUtils.saveBooleanData(this,KUNDLI_AI_OPEN_FIRST_TIME_KEY,false);
                    });
                    kundliAIFirstTimeMsgFragment.show(getSupportFragmentManager(), "kundliAIWelcomeMsg");
                    return;
                }
                openKundliAIChatWindow();
            } else {
                PopUpLogin popUpLogin = new PopUpLogin
                        (com.ojassoft.astrosage.varta.utils.CGlobalVariables.KUNDALI,
                                "ONLY_LOGIN");
                popUpLogin.show(getSupportFragmentManager(), "PopUpLogin");
                isPopupLoginShown = true;
                AstrosageKundliApplication.isOpenVartaPopup = true;
                //startActivity(new Intent(OutputMasterActivity.this, LoginSignUpActivity.class));
            }
        });

        createModelForKundliAI();

    }

    private void openKundliAIChatWindow() {
        int screenId = numerologyScreenIds[mViewPager.getCurrentItem()];
        if (day != NUMEROLOGY_MASTER11 && day != NUMEROLOGY_MASTER22 && day != NUMEROLOGY_KARMIC13
                && day != NUMEROLOGY_KARMIC14 && day != NUMEROLOGY_KARMIC16 && day != NUMEROLOGY_KARMIC19) {
            if (screenId > 111) {
                screenId += 1;
            }
        }
        ArrayList<String> suggestedQuestions = getSuggestedQuestionsForScreenId(screenId);
        Intent intent = new Intent(context, MiniChatWindow.class);
        intent.putStringArrayListExtra(MODULE_SUGGESTED_QUESTIONS_KEY, suggestedQuestions);
        intent.putExtra(NUMERO_CLNY_TYPE_KEY, String.valueOf(type));
        intent.putExtra(CURRENT_SCREEN_ID_KEY, screenId);
        intent.putExtra(SOURCE_OF_SCREEN, com.ojassoft.astrosage.varta.utils.CGlobalVariables.SOURCE_FROM_NUMEROLOGY_SCREEN);
        if (LANGUAGE_CODE == 1) {
            intent.putExtra(KEY_SCREEN_NAME, getRomanScreenName(mViewPager.getCurrentItem()));
        } else {
            intent.putExtra(KEY_SCREEN_NAME, adapter.getPageTitle(mViewPager.getCurrentItem()));
        }
        intent.putExtra(KEY_CONVERSATION_ID,conversationId);
        startActivity(intent);
    }

    private void initContext() {
        currentActivity = NumerologyCalculatorOutputActivity.this;
        context = NumerologyCalculatorOutputActivity.this;
    }

    private void setupViewPager() {
        if (LANGUAGE_CODE == 1) {
            tv_ask_quest.animateTextInfinite(getString(R.string.ask_anything_to_kundli_hi,getString(R.string.lbl_overview)));
        } else {
            tv_ask_quest.animateTextInfinite(getString(R.string.ask_anything_to_kundli,getString(R.string.lbl_overview)));
        }
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), currentActivity);
        adapter.addFragment(new NumerologyHomeFragment(), getString(R.string.lbl_overview));
        adapter.addFragment(new NumerologyRadicalFragment(), getString(R.string.lbl_radical_num));
        adapter.addFragment(new NumerologyDestinyFragment(), getString(R.string.lbl_destiny_num));
        adapter.addFragment(new NumerologyNameFragment(), getString(R.string.lbl_name_num));
        adapter.addFragment(new NumerologyPlaceFragment(), getString(R.string.lbl_ausp_place));
        adapter.addFragment(new NumerologyHealthFragment(), getString(R.string.health));
        adapter.addFragment(new NumerologyTimeFragment(), getString(R.string.lbl_ausp_time));
        adapter.addFragment(new NumerologyCareerFragment(), getString(R.string.lbl_career_chioce));
        adapter.addFragment(new NumerologyFastFragment(), getString(R.string.lbl_fast_remd));
        adapter.addFragment(new NumerologyYantraFragment(), getString(R.string.lbl_yantra));
        adapter.addFragment(new NumerologyLoShuGridFragment(), getString(R.string.lo_shu_grid));
        if (day == NUMEROLOGY_MASTER11 || day == NUMEROLOGY_MASTER22 || day == NUMEROLOGY_KARMIC13
                || day == NUMEROLOGY_KARMIC14 || day == NUMEROLOGY_KARMIC16 || day == NUMEROLOGY_KARMIC19) {
            adapter.addFragment(new NumerologySpecialFragment(), getString(R.string.lbl_special_tab));
        }
        adapter.addFragment(AskQuestionsFragment.newInstance(NUMEROLOGY), getString(R.string.ask_questions));
        mViewPager.setAdapter(adapter);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                if (LANGUAGE_CODE == 1) {
                    tv_ask_quest.animateTextInfinite(getString(R.string.ask_anything_to_kundli_hi,adapter.getPageTitle(position)));
                } else {
                    tv_ask_quest.animateTextInfinite(getString(R.string.ask_anything_to_kundli,adapter.getPageTitle(position)));
                }
            }

            @Override
            public void onPageSelected(int position) {

                LANGUAGE_CODE = AstrosageKundliApplication.getAppContext().getLanguageCode();
                com.ojassoft.astrosage.varta.utils.CUtils.getRobotoFont(NumerologyCalculatorOutputActivity.this, LANGUAGE_CODE, com.ojassoft.astrosage.varta.utils.CGlobalVariables.regular);

                if (tabLayout != null && adapter != null) {
                    adapter.setAlpha(position, tabLayout);
                }
                try {
                    String labell = CGlobalVariables.NUMEROLOGY_EVENT + "_" +CGlobalVariables.GOOGLE_ANALYTIC_NUMEROLOGY[position];
                    CUtils.fcmAnalyticsEvents(labell, CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");

                }catch (Exception e)
                {
                    e.printStackTrace();
                }
                if (LANGUAGE_CODE == 1) {
                    tv_ask_quest.animateTextInfinite(getString(R.string.ask_anything_to_kundli_hi,adapter.getPageTitle(position)));
                } else {
                    tv_ask_quest.animateTextInfinite(getString(R.string.ask_anything_to_kundli,adapter.getPageTitle(position)));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        setsTabLayout();
    }

    private void setsTabLayout() {

        tabLayout.setupWithViewPager(mViewPager);

        // Iterate over all tabs and set the custom view
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(adapter.getTabView(i));
        }

        adapter.setAlpha(0, tabLayout);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public AdData getAddData(String addSlot) {
        try {
            //addSlot = "19";
            String result = CUtils.getStringData(currentActivity, "CUSTOMADDS", "");
            if (result != null && !result.equals("")) {
                adList = new Gson().fromJson(result, new TypeToken<ArrayList<AdData>>() {
                }.getType());
                topAdData = CUtils.getSlotData(adList, addSlot);
                //return topAdData;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return topAdData;
    }

    /*BottomNavigationView.OnNavigationItemSelectedListener navigationItemSelectedListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.actapp_nav_home:
                            Intent intent = new Intent(NumerologyCalculatorOutputActivity.this, ActAppModule.class);
                            startActivity(intent);
                            return true;
                        case R.id.actapp_nav_call:
                            com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_NUMEROLOGY_CALC_O_BOTTOM_CALL, FIREBASE_EVENT_ITEM_CLICK, "");
                            createSession(NumerologyCalculatorOutputActivity.this, NUMEROLOGY_CALC_O_BOTTOM_BAR_CALL_PARTNER_ID);
                            switchToConsultTab(FILTER_TYPE_CALL, NumerologyCalculatorOutputActivity.this);
                            return true;
                        case R.id.actapp_nav_live:
                            fabActions();
                            return true;
                        case R.id.actapp_nav_chat:
                            com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_NUMEROLOGY_CALC_O_BOTTOM_CHAT, FIREBASE_EVENT_ITEM_CLICK, "");
                            createSession(NumerologyCalculatorOutputActivity.this, NUMEROLOGY_CALC_O_BOTTOM_BAR_CHAT_PARTNER_ID);
                            switchToConsultTab(FILTER_TYPE_CHAT, NumerologyCalculatorOutputActivity.this);
                            return true;
                        case R.id.actapp_nav_account:
                            com.ojassoft.astrosage.varta.utils.CUtils.openAccountScreen(NumerologyCalculatorOutputActivity.this);
                            return true;
                    }
                    return false;
                }
            };*/

    private void fabActions(){
        try {
            //boolean liveStreamingEnabledForAstrosage = com.ojassoft.astrosage.varta.utils.CUtils.getBooleanData(this, com.ojassoft.astrosage.varta.utils.CGlobalVariables.liveStreamingEnabledForAstrosage, false);
            boolean liveStreamingEnabledForAstrosage = true;
            if(!liveStreamingEnabledForAstrosage){ //fetch data according to tagmanag
                com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_NUMEROLOGY_CALC_O_BOTTOM_KUNDLI, FIREBASE_EVENT_ITEM_CLICK, "");
                createSession(NumerologyCalculatorOutputActivity.this, NUMEROLOGY_CALC_O_BOTTOM_BAR_KUNDLI_PARTNER_ID);
                openKundliActivity(this);
            }
            else {
                com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_NUMEROLOGY_CALC_O_BOTTOM_LIVE, FIREBASE_EVENT_ITEM_CLICK, "");
                createSession(NumerologyCalculatorOutputActivity.this, NUMEROLOGY_CALC_O_BOTTOM_BAR_LIVE_PARTNER_ID);
                gotoAllLiveActivityFromLiveIcon(NumerologyCalculatorOutputActivity.this);
            }
        }catch (Exception e){
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
                FontUtils.changeFont(NumerologyCalculatorOutputActivity.this, ((TextView) v), FONTS_OPEN_SANS_REGULAR);
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
        CUtils.handleFabOnActivities(this,fabOutputMaster,navLive);

        if (com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(NumerologyCalculatorOutputActivity.this)) {
                navHisTxt.setText(getResources().getString(R.string.history));
                navHisImg.setImageResource(R.drawable.nav_more_icons);
            } else {
                navHisTxt.setText(getResources().getString(R.string.sign_up));
                navHisImg.setImageResource(R.drawable.nav_profile_icons);
            }
        //navView.getMenu().setGroupCheckable(0,false,true);
        //setting Click listener
        navView.findViewById(R.id.bottom_nav_home).setOnClickListener(navbarItemSelectedListener);
        navView.findViewById(R.id.bottom_nav_call).setOnClickListener(navbarItemSelectedListener);
        navView.findViewById(R.id.bottom_nav_live).setOnClickListener(navbarItemSelectedListener);
        navView.findViewById(R.id.bottom_nav_chat).setOnClickListener(navbarItemSelectedListener);
        navView.findViewById(R.id.bottom_nav_history).setOnClickListener(navbarItemSelectedListener);
    }

    @Override
    public void clickCall() {
        com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_NUMEROLOGY_CALC_O_BOTTOM_CALL, FIREBASE_EVENT_ITEM_CLICK, "");
        createSession(NumerologyCalculatorOutputActivity.this, NUMEROLOGY_CALC_O_BOTTOM_BAR_CALL_PARTNER_ID);
        super.clickCall();
    }

    @Override
    public void clickChat() {
        com.ojassoft.astrosage.varta.utils.CUtils.fcmAnalyticsEvents(GOOGLE_ANALYTICS_NUMEROLOGY_CALC_O_BOTTOM_CHAT, FIREBASE_EVENT_ITEM_CLICK, "");
        createSession(NumerologyCalculatorOutputActivity.this, NUMEROLOGY_CALC_O_BOTTOM_BAR_CHAT_PARTNER_ID);
        super.clickChat();
    }

    @Override
    public void clickLive() {
        fabActions();
    }

    private void getSuggestQuestionForNumerologyScreens() {
        try {
            // one day caching
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
            Date curentDate = new Date();
            String todayDate = formatter.format(curentDate);
            String getSuggestedQuestionDate = CUtils.getStringData(context,GET_SUGGESTED_QUESTION_DATE,"");
            int lang = LANGUAGE_CODE;
            String suggestedQuestionLangKey = SUGGESTED_QUESTION_KEY+"_"+lang;

            if(getSuggestedQuestionDate.equalsIgnoreCase(todayDate)){
                String suggestedQuestion = CUtils.getStringData(context,suggestedQuestionLangKey,"");
                if(!TextUtils.isEmpty(suggestedQuestion)){ //check in local
                    parseSuggestedQuestion(suggestedQuestion, suggestedQuestionLangKey);
                    return;
                }
            }

            Call<ResponseBody> call = RetrofitClient.getAIInstance().create(ApiList.class).getSuggestedQuestionModule(getQuestionListParams(lang));
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {

                    try {
                        if (response.isSuccessful() && response.body() != null) {
                            String responseString = response.body().string();
                            //Log.e("TestQuestion", "onResponse: " + responseString);
                            parseSuggestedQuestion(responseString, suggestedQuestionLangKey);
                            CUtils.saveStringData(context,GET_SUGGESTED_QUESTION_DATE,todayDate);
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
            CUtils.saveStringData(context,suggestedQuestionLangKey,jsonObject.toString());
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
        headers.put("moduleid", String.valueOf(CGlobalVariables.MODULE_NUMEROLOGY));
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

    public void addExtraSpaceInBottom(View view){
        try {
            if (ask_question_layout.getVisibility() == View.VISIBLE) {
                LinearLayout adView = view.findViewById(R.id.adView);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(adView.getLayoutParams());
                params.bottomMargin = CGenerateAppViews.convertDpToPixel(68, currentActivity);
                adView.setLayoutParams(params);
            }
        }catch(Exception e){
            //ignore
        }
    }

    private String getRomanScreenName(int position) {
        Resources resources = getLocaleResources(this, "en");
        ArrayList<String> titles = new ArrayList<>();
        titles.add(resources.getString(R.string.lbl_overview));
        titles.add(resources.getString(R.string.lbl_radical_num));
        titles.add(resources.getString(R.string.lbl_destiny_num));
        titles.add(resources.getString(R.string.lbl_name_num));
        titles.add(resources.getString(R.string.lbl_ausp_place));
        titles.add(resources.getString(R.string.health));
        titles.add(resources.getString(R.string.lbl_ausp_time));
        titles.add(resources.getString(R.string.lbl_career_chioce));
        titles.add(resources.getString(R.string.lbl_fast_remd));
        titles.add(resources.getString(R.string.lbl_yantra));
        titles.add(resources.getString(R.string.lo_shu_grid));
        if (day == NUMEROLOGY_MASTER11 || day == NUMEROLOGY_MASTER22 || day == NUMEROLOGY_KARMIC13
                || day == NUMEROLOGY_KARMIC14 || day == NUMEROLOGY_KARMIC16 || day == NUMEROLOGY_KARMIC19) {
            titles.add(getString(R.string.lbl_special_tab));
        }
        titles.add(getString(R.string.ask_questions));
        return titles.get(position);
    }

    private void createModelForKundliAI() {
        BeanHoroPersonalInfo beanHoroPersonalInfo = new BeanHoroPersonalInfo();
        beanHoroPersonalInfo.setName(name);
        BeanDateTime beanDateTime = new BeanDateTime();

        if (!TextUtils.isEmpty(dob)) {
            String[] dobArray = dob.split("-");
            beanDateTime.setDay(Integer.parseInt(dobArray[0]));
            beanDateTime.setMonth(Integer.parseInt(dobArray[1]) - 1);
            beanDateTime.setYear(Integer.parseInt(dobArray[2]));
        }

        beanHoroPersonalInfo.setDateTime(beanDateTime);

        //CGlobal.getCGlobalObject().setHoroPersonalInfoObject(beanHoroPersonalInfo);
        CUtils.setNumeroBeanHoroPersonalInfo(beanHoroPersonalInfo);

    }

}
