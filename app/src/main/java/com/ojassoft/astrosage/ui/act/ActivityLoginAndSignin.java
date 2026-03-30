package com.ojassoft.astrosage.ui.act;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Build;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.customadapters.ViewPagerAdapter;
import com.ojassoft.astrosage.jinterface.IPermissionCallback;
import com.ojassoft.astrosage.ui.fragments.WizardLoginFrag;
import com.ojassoft.astrosage.ui.fragments.WizardLogingRegister;
import com.ojassoft.astrosage.ui.fragments.WizardSignupFrag;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

/**
 * Created by ojas-08 on 13/6/16.
 */
public class ActivityLoginAndSignin extends BaseInputActivity implements
        WizardLogingRegister.IWizardLoginRegisterFragmentInterface,
        WizardSignupFrag.IUserAlreadyRegistered,IPermissionCallback {
    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter adapter;
    Button skipButton;
    TextView headingTextView, desTextView;
    ImageView imageView;
    LinearLayout mainContainerLayout, topContainerLayout, bottomContainerLayout;
    View view;

    String[] pageTitles;

    public ActivityLoginAndSignin() {
        super(R.string.app_name);
    }

    @Override
    public void isEmailIdPermissionGranted(boolean isPermissionGranted) {
        try {
            //WizardSignupFrag fragSignUp = (WizardSignupFrag) adapter.getItem(0);
            //fragSignUp.checkForPermission(true);
        }catch (Exception ex){
            //Log.e("Exception",ex.getMessage());
        }
    }

    public enum EnumFrag{
        WizardLoginFrag
    };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity_layout);
        getIdOfAllView();
        setTypefaceOfView();
        //doActionOnOpenKeyboard();

        tabLayout.setTabMode(TabLayout.MODE_FIXED);

        pageTitles = getResources().getStringArray(
                R.array.Login_titles_list);
        setViewPagerAdapter();
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            tab.setCustomView(adapter.getTabView(i));
        }
        adapter.setAlpha(0,tabLayout);
        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToHome();
            }
        });
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

    private void getIdOfAllView() {
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        viewPager = (ViewPager) findViewById(R.id.pager);
        skipButton = (Button) findViewById(R.id.skip_textview);
        headingTextView = (TextView) findViewById(R.id.heading_text);
        desTextView = (TextView) findViewById(R.id.des_text);
        topContainerLayout = (LinearLayout) findViewById(R.id.top_container_layout);
        bottomContainerLayout = (LinearLayout) findViewById(R.id.bottom_container_layout);
        mainContainerLayout = (LinearLayout) findViewById(R.id.main_container_layout);
        imageView = (ImageView) findViewById(R.id.image_icon);
        view = (View) findViewById(R.id.sperator_view);
        setMarginOfViewFromTop(skipButton, 36);
        if (LANGUAGE_CODE == CGlobalVariables.ENGLISH) {
            skipButton.setText(getResources().getString(R.string.SKIP).toUpperCase());
        }
        skipButton.setVisibility(View.VISIBLE);
        setImageHeightAndWidth(imageView);
    }

    private void setTypefaceOfView() {
        skipButton.setTypeface(mediumTypeface);
        headingTextView.setTypeface(mediumTypeface);
        desTextView.setTypeface(regularTypeface);
    }

    private void setViewPagerAdapter() {
        try {
            adapter = new ViewPagerAdapter(getSupportFragmentManager(), ActivityLoginAndSignin.this);
            if (LANGUAGE_CODE == CGlobalVariables.ENGLISH) {
                pageTitles[1] = getResources().getString(R.string.open_account);
            } else if (LANGUAGE_CODE == CGlobalVariables.HINDI) {
                pageTitles[1] = getResources().getString(R.string.open_account);
            }

            adapter.addFragment(new WizardSignupFrag(), pageTitles[1]);
            adapter.addFragment(new WizardLoginFrag(), pageTitles[0]);
            viewPager.setAdapter(adapter);
            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    setAlpha(position);
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    public void goToHome() {
        CUtils.googleAnalyticSendWitPlayServie(
                ActivityLoginAndSignin.this,
                CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                CGlobalVariables.GOOGLE_ANALYTIC_ACTION_ASTROSAGE_LOGIN_SKIP,
                null);
        CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_ACTION_ASTROSAGE_LOGIN_SKIP,CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK,"");

        CUtils.saveWizardShownFirstTime(ActivityLoginAndSignin.this);
        startActivity(new Intent(ActivityLoginAndSignin.this, ActAppModule.class));
        finish();
    }


    @Override
    public void clickedSignInButton(String userId, String password) {

    }

    @Override
    public void clickedSignUpButton(int pos) {
        viewPager.setCurrentItem(pos);
    }

    @Override
    public void clickedSkipSignInButton() {

    }

    @Override
    public void clickedForgetPasswordButton() {

    }

    @Override
    public boolean hideSkipButton() {
        return false;
    }

    @Override
    public void applyBackButon(boolean b) {

    }

    private void setMarginOfViewFromTop(View view, int margin) {
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) view.getLayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            layoutParams.topMargin = margin;
        } else {
            layoutParams.topMargin = 0;
        }
        view.setLayoutParams(layoutParams);

    }

    private void setImageHeightAndWidth(ImageView imageView) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int height = displaymetrics.heightPixels;
        LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) imageView.getLayoutParams();
        layoutParams.width = height / 4;
        layoutParams.height = height / 4;
        imageView.setLayoutParams(layoutParams);
    }

    private void doActionOnOpenKeyboard() {
        //final View activityRootView = view.findViewById(R.id.scrollView);
        mainContainerLayout.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect r = new Rect();
                mainContainerLayout.getWindowVisibleDisplayFrame(r);
               // int heightDiff = mainContainerLayout.getRootView().getHeight() - (r.bottom - r.top);
                int screenHeight = mainContainerLayout.getRootView().getHeight();
                int keypadHeight = screenHeight - r.bottom;
                if (keypadHeight > screenHeight * 0.15) {
                    setVisibilityOfTopViewOpenKeyboard(false);
                } else {
                    setVisibilityOfTopViewOpenKeyboard(true);
                }
            }
        });
    }

    private void setVisibilityOfTopViewOpenKeyboard(boolean isShow) {
        if (isShow) {
            topContainerLayout.setVisibility(View.VISIBLE);
            view.setVisibility(View.GONE);
        } else {
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
                view.setVisibility(View.VISIBLE);
                setMarginOfViewFromTop(bottomContainerLayout, 0);
            }else{
                view.setVisibility(View.GONE);
            }
            topContainerLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void setEmailId(EnumFrag frag, String emailId) {
        //viewPager.setCurrentItem(pos);
        if(frag.equals(ActivityLoginAndSignin.EnumFrag.WizardLoginFrag)){
            WizardLoginFrag wizardLoginFrag =  (WizardLoginFrag)adapter.getFragment(1);
            wizardLoginFrag.updateEmailIdInEditTextView(emailId);
            viewPager.setCurrentItem(1);
            setAlpha(1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
           /* case PERMISSION_CONTACTS:
                try {
                    WizardSignupFrag fragSignUp = (WizardSignupFrag) adapter.getItem(0);
                    fragSignUp.checkForPermission(false);
                }catch (Exception ex){
                    //Log.e("Exception",ex.getMessage());
                }
                break;*/
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void setAlpha(int position){
        if(tabLayout != null && adapter!=null) {
            adapter.setAlpha(position,tabLayout);
        }
    }
    public void showSkipButton() {
        skipButton.setVisibility(View.VISIBLE);
        //skipButton.setVisibility(View.GONE);
    }
}
