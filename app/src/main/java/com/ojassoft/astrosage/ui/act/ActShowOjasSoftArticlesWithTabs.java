package com.ojassoft.astrosage.ui.act;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.tabs.TabLayout;

import androidx.core.content.ContextCompat;
import androidx.core.graphics.drawable.DrawableCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;
import android.widget.TextView;
import android.widget.Toast;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.customadapters.ViewPagerAdapter;
import com.ojassoft.astrosage.ui.fragments.NewMagazineFrag;
import com.ojassoft.astrosage.ui.fragments.OldMagazineFrag;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.varta.ui.activity.AllLiveAstrologerActivity;
import com.ojassoft.astrosage.varta.utils.FontUtils;

import java.util.Objects;

/**
 * Created by ojas on १९/३/१८.
 */

public class ActShowOjasSoftArticlesWithTabs extends BaseInputActivity {
    public Toolbar toolBar;
    private TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter adapter;
    public int SCREEN_TYPE = -1;
    public String offersUrl = "";
    public String title = "AstroSage.com : All Articles";
    public boolean IS_ASTROSHOP = false;
    public boolean activityRestarted = false;
    public Menu actionMenu;
    int selectedFrag;
    public TextView tvTitle;
    public static String pathStr ="";
    boolean oldMagazineEnabled;
    BottomNavigationView navView;

    public ActShowOjasSoftArticlesWithTabs() {
        super(R.string.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.magazine_layout_with_tabs);
        Intent intent = getIntent();
        if (intent != null) {
            SCREEN_TYPE = intent.getIntExtra(CGlobalVariables.ASTRO_WEBVIEW_TITLE_KEY, -1);
        }
        if (SCREEN_TYPE == CGlobalVariables.MODULE_ABOUT_US || SCREEN_TYPE == CGlobalVariables.MODULE_ASTROSAGE_OFFERS) {
            offersUrl = intent.getStringExtra("URL");
            if (SCREEN_TYPE == CGlobalVariables.MODULE_ABOUT_US) {
                title = intent.getStringExtra("TITLE_TO_SHOW");
            }
        }
        Bundle extras = intent.getExtras();
        if (extras != null) {
            try {
                title = extras.getString("TITLE_TO_SHOW");
                IS_ASTROSHOP = extras.getBoolean("IS_ASTROSHOP");
            } catch (Exception e) {
            }
        }

        Log.d ( "MyTag" , " MagazineActivity onCreate: " );

        oldMagazineEnabled = CUtils.getBooleanData(this, CGlobalVariables.oldMagazineEnabled, false);

        setToolbarItem();
        setViewPagerAdapter();
        setsTabLayout();
        doActionForDeppLink(getIntent());

        navView = findViewById(R.id.nav_view);
        navView.setOnNavigationItemSelectedListener(navigationItemSelectedListener);
        customBottomNavigationFont(navView);
        setBottomNavigationText();


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
                FontUtils.changeFont(ActShowOjasSoftArticlesWithTabs.this, ((TextView) v), com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
                ((TextView) v).setTextSize(11);
                ((TextView) v).setTextColor(getResources().getColor(R.color.black));
                // added by vishal
                ((TextView) v).setEllipsize(TextUtils.TruncateAt.END);
                ((TextView) v).setPadding(0, 0, 0, 0);
            }
        } catch (Exception e) { }
    }

    private void setBottomNavigationText() {
        // get menu from navigationView
        Menu menu = navView.getMenu();
        // find MenuItem you want to change
        MenuItem navHome = menu.findItem(R.id.bottom_nav_home);
        MenuItem navLive = menu.findItem(R.id.bottom_nav_live);
        MenuItem navHistory = menu.findItem(R.id.bottom_nav_history);
        MenuItem navCall = menu.findItem(R.id.bottom_nav_call);
        MenuItem navChat = menu.findItem(R.id.bottom_nav_chat);

        boolean isAIChatDisplayed = com.ojassoft.astrosage.utils.CUtils.isAIChatDisplayed(this);
        if(isAIChatDisplayed){
            navChat.setTitle(getResources().getString(R.string.text_ask));
            navCall.setTitle(getResources().getString(R.string.ai_astrologer));
            navCall.setIcon(R.drawable.nav_ai_icons);
        } else {
            navChat.setTitle(getResources().getString(R.string.chat_now));
            navCall.setTitle(getResources().getString(R.string.call));
            navCall.setIcon(R.drawable.nav_call_icons);
        }

        // set new title to the MenuItem
        navHome.setTitle(getResources().getString(R.string.title_home));
        navLive.setTitle(getResources().getString(R.string.live));
        navHistory.setTitle(getResources().getString(R.string.history));
        if (com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(ActShowOjasSoftArticlesWithTabs.this)) {
            navHistory.setTitle(getResources().getString(R.string.history));
            navHistory.setIcon(R.drawable.nav_more_icons);
        } else {
            navHistory.setTitle(getResources().getString(R.string.sign_up));
            navHistory.setIcon(R.drawable.nav_profile_icons);
        }
        //navLive.setChecked(true);
    }



    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        doActionForDeppLink(intent);

    }

    private void doActionForDeppLink(Intent intent) {
        String action = intent.getAction();
        Uri data = intent.getData();
        if(data == null){
            pathStr = intent.getStringExtra("BLOG_LINK_TO_SHOW");
        }else{
            if(Intent.ACTION_VIEW.equals(action))
                pathStr = data.toString();
            else
                pathStr = "";
        }

        if (pathStr != null && !pathStr.equals("")) {

            if (pathStr.contains("astrology.astrosage.com")
                    || pathStr.contains("www.astrology.astrosage.com")
                    || pathStr.contains("jyotish.astrosage.com")
                    || pathStr.contains("www.jyotish.astrosage.com")) {
                if(oldMagazineEnabled){
                    viewPager.setCurrentItem(1);
                }else{
                    // open on web browser
                    //added by Ankit on 27-12-2019
                    if (URLUtil.isValidUrl(pathStr)) {
                        Uri uri = Uri.parse(pathStr);
                        CUtils.openWebBrowserOnlyChrome(ActShowOjasSoftArticlesWithTabs.this, uri);
                        finish();
                    }
                }
            } else if (pathStr.contains("horoscope.astrosage.com")
                    || pathStr.contains("www.horoscope.astrosage.com")
                    || pathStr.contains("horoscope.astrosage.com/hindi")
                    || pathStr.contains("www.horoscope.astrosage.com/hindi")) {
                viewPager.setCurrentItem(0);
            }
        }
    }

    private void setToolbarItem() {
        toolBar = (Toolbar) findViewById(R.id.tool_barAppModule);
        setSupportActionBar(toolBar);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(getResources().getString(R.string.hindu_calender));
        tvTitle.setTypeface(regularTypeface);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        if(oldMagazineEnabled){
            tabLayout.setVisibility(View.VISIBLE);
        }else{
            tabLayout.setVisibility(View.GONE);
        }

        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    public void setsTabLayout() {

        tabLayout.setupWithViewPager(viewPager);

        // Iterate over all tabs and set the custom view
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(adapter.getTabView(i));
        }

        adapter.setAlpha(0, tabLayout);
    }

    private void setViewPagerAdapter() {
        try {
            adapter = new ViewPagerAdapter(getSupportFragmentManager(), ActShowOjasSoftArticlesWithTabs.this);
            String pageTitle[] = getResources().getStringArray(R.array.magzine_tabs_text);
            adapter.addFragment(new NewMagazineFrag(), pageTitle[0]);
            if(oldMagazineEnabled){
                adapter.addFragment(new OldMagazineFrag(), pageTitle[1]);
            }else{
            }

            viewPager.setAdapter(adapter);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                }

                @Override
                public void onPageSelected(int position) {
                    adapter.setAlpha(position, tabLayout);
                    selectedFrag = position;
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        activityRestarted = true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        this.actionMenu = menu;
        getMenuInflater().inflate(R.menu.article_screen_menu, menu);
        //getSupportMenuInflater().inflate(R.menu.article_screen_menu, menu);

        setShareMenuIconColor(menu);
        return true;
    }

    private void setShareMenuIconColor (Menu menu) {
        try {
            Drawable drawable = menu.findItem(R.id.action_share_menu).getIcon();
            drawable = DrawableCompat.wrap(Objects.requireNonNull(drawable));
            DrawableCompat.setTint(drawable, ContextCompat.getColor(this, R.color.greenish_gray));
            menu.findItem(R.id.action_share_menu).setIcon(drawable);
        } catch (Exception e) {
            //
        }
    }

    private void refreshView() {
        if (adapter != null) {
            Fragment frag = adapter.getFragment(selectedFrag);
            if(frag != null) {
                if (frag instanceof NewMagazineFrag) {
                    ((NewMagazineFrag) frag).refreshWebView();
                } else if (frag instanceof OldMagazineFrag) {
                    ((OldMagazineFrag) frag).refreshWebView();
                }
            }
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                if (SCREEN_TYPE == CGlobalVariables.MODULE_ABOUT_US || SCREEN_TYPE == CGlobalVariables.MODULE_HOROSCOPE_KNOW_YOUR_MOON_SIGN_BY_DOB || SCREEN_TYPE == CGlobalVariables.MODULE_ASK_OUR_ASTROLOGER || SCREEN_TYPE == CGlobalVariables.MODULE_ASTROSAGE_ARTICLES) {
                    finish();
                } else {
                    try {
                        CUtils.gotoHomeScreen(this);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            /*if(!isApplicatonOpened())
                CUtils.restartApplication(ActShowOjasSoftArticles.this);
			ActShowOjasSoftArticles.this.finish();*/
                return true;
            case R.id.action_refresh_menu:
                try {
                    refreshView();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return true;

            case R.id.action_share_menu:
            {
                Fragment frag = adapter.getFragment(selectedFrag);
                String shareUrl=getString ( R.string.i_would_like_to_share_this_important_astrosage_article_with_you )+"\n";
                if(frag != null) {
                    if (frag instanceof NewMagazineFrag) {
                       shareUrl= shareUrl+((NewMagazineFrag) frag).getBlogUrl ();
                    } else if (frag instanceof OldMagazineFrag) {
                        shareUrl=shareUrl+((OldMagazineFrag) frag).getBlogUrl();
                    }
                }

                Log.d ( "MyTag" , "shareUrl :"+shareUrl );


                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_MAGAZINE_SHARE,
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "MAGAZINE");
                com.ojassoft.astrosage.varta.utils.CUtils.shareWithFriends(this,shareUrl);
                return true;
            }

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if (adapter != null) {
                    Fragment frag = adapter.getFragment(selectedFrag);
                    if (frag instanceof NewMagazineFrag) {
                        return ((NewMagazineFrag) frag).doActionOnBackPress();
                    } else if (frag instanceof OldMagazineFrag) {
                        return ((OldMagazineFrag) frag).doActionOnBackPress();
                    }
                }
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

}
