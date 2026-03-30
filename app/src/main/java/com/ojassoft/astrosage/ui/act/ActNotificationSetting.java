package com.ojassoft.astrosage.ui.act;

import java.util.ArrayList;

import android.graphics.Typeface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import androidx.appcompat.app.AppCompatActivity;

import android.widget.Toast;



/*import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Window;*/
//import com.google.analytics.tracking.android.EasyTracker;
import com.ojassoft.astrosage.ui.fragments.WizardNotificationSetting;
import com.ojassoft.astrosage.ui.fragments.WizardNotificationSetting.IWizardNotificationSettingFragmentInterface;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

public class ActNotificationSetting extends AppCompatActivity implements IWizardNotificationSettingFragmentInterface {

    ViewPager viewPager;
    public int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    public Typeface typeface;


    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstaceState) {
        super.onCreate(savedInstaceState);

        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //LANGUAGE_CODE = CUtils.getLanguageCodeFromPreference(this);
        LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication()).getLanguageCode();//ADDED BY HEVENDRA ON 24-12-2014
        typeface = CUtils.getRobotoFont(getApplicationContext(), LANGUAGE_CODE, CGlobalVariables.regular);
        viewPager = new ViewPager(ActNotificationSetting.this);
        viewPager.setId("VP".hashCode());
        setViewPagerAdapter();
        setContentView(viewPager);
        setViewPagerListeners();
        viewPager.setCurrentItem(0);
    }

    /* (non-Javadoc)
     * @see android.support.v4.app.FragmentActivity#onStart()
     */
    @Override
    protected void onStart() {
        super.onStart();
        //EasyTracker.getInstance().activityStart(this);
    }

    /* (non-Javadoc)
     * @see com.actionbarsherlock.app.SherlockFragmentActivity#onStop()
     */
    @Override
    protected void onStop() {
        super.onStop();
        //EasyTracker.getInstance().activityStop(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        typeface = CUtils.getRobotoFont(getApplicationContext(), CUtils.getLanguageCodeFromPreference(getApplicationContext()), CGlobalVariables.regular);
    }

    private void setViewPagerListeners() {
        viewPager.setOnPageChangeListener(new OnPageChangeListener() {
            @Override
            public void onPageScrollStateChanged(int arg0) {

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {

            }

            @Override
            public void onPageSelected(int position) {

            }

        });
    }

    private void setViewPagerAdapter() {
        try {
            ModulePagerAdapter modulePagerAdapter = new ModulePagerAdapter(getSupportFragmentManager());
            viewPager.setAdapter(modulePagerAdapter);
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }

    }

    public class ModulePagerAdapter extends FragmentStatePagerAdapter {

        private ArrayList<Fragment> mFragments;

        public ModulePagerAdapter(FragmentManager fm) {
            super(fm);
            mFragments = new ArrayList<Fragment>();
//			mFragments.add(new WizardNotificationSetting());
            mFragments.add(WizardNotificationSetting.newInstance(WizardNotificationSetting.ActNotificationScreen));
        }

        @Override
        public int getCount() {
            return mFragments.size();
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        /* (non-Javadoc)
         * @see android.support.v4.view.PagerAdapter#getPageTitle(int)
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return "";
        }

    }

    @Override
    public void clickedNotificationSettingNextButton() {
        this.finish();
    }

}
