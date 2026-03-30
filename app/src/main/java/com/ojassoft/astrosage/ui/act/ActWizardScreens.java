package com.ojassoft.astrosage.ui.act;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import androidx.appcompat.app.AppCompatActivity;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.widget.Toast;

//import com.google.analytics.tracking.android.EasyTracker;
import com.ojassoft.astrosage.ui.fragments.WizardChooseLanguage;
import com.ojassoft.astrosage.ui.fragments.WizardChooseLanguage.IWizardChooseLanguageFragmentInterface;
import com.ojassoft.astrosage.ui.fragments.WizardDisclaimer.IWizardDisclaimerFragmentInterface;
import com.ojassoft.astrosage.ui.fragments.WizardLogingRegister;
import com.ojassoft.astrosage.ui.fragments.WizardLogingRegister.IWizardLoginRegisterFragmentInterface;
import com.ojassoft.astrosage.ui.fragments.WizardNotificationSetting.IWizardNotificationSettingFragmentInterface;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.varta.ui.activity.BaseActivity;

import java.util.ArrayList;

/*import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Window;*/

public class ActWizardScreens extends BaseActivity implements IWizardNotificationSettingFragmentInterface,IWizardChooseLanguageFragmentInterface,IWizardDisclaimerFragmentInterface,IWizardLoginRegisterFragmentInterface{

	
	NonSwipeableViewPager viewPager;
	//DISABLED BY BIJENDRA ON 13-02-15 TO STOP APPEARING  DISCLAIMER SCREEN
/*	final int DISCLAIMER_SCREEN = 0; 
	final int CHOOSE_LANGUAGE_SCREEN = 1;
	final int NOTIFICATION_SETTING_SCREEN = 2;
	final int SIGN_IN_SCREEN = 3;*/
	
	final int CHOOSE_LANGUAGE_SCREEN = 0;
	final int NOTIFICATION_SETTING_SCREEN = 1;
	final int SIGN_IN_SCREEN = 2;
	
	int SCREEN_INDEX;
	public int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
	public Typeface typeface;
	int callerActivity;
	boolean FLAG_HIDE_SPIP_BUTTON=false;
	private boolean FLAG_WEB_LAYOUT_VISIBLE=false;
	private void initValues() {
		typeface=CUtils.getRobotoFont(getApplicationContext(), LANGUAGE_CODE, CGlobalVariables.regular);
		SCREEN_INDEX = getScreenIndex();
	}
	public static boolean isFromSplash = false;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//this.requestWindowFeature(Window.FEATURE_NO_TITLE);//DISABLED BY BIJENDRA ON 14-08-15
		
		//LANGUAGE_CODE = CUtils.getLanguageCodeFromPreference(this);
		LANGUAGE_CODE = ((AstrosageKundliApplication)getApplication()).getLanguageCode();//ADDED BY HEVENDRA ON 24-12-2014
		callerActivity = getIntent().getIntExtra("callerActivity", CGlobalVariables.APP_MODULE_SCREEN);
		if (getIntent() != null && getIntent().hasExtra("isFromSplash")) {
			isFromSplash = getIntent().getBooleanExtra("isFromSplash", false);
		}
		initValues();
		
		viewPager = new NonSwipeableViewPager(ActWizardScreens.this);
		viewPager.setId("VP".hashCode());
	    setViewPagerAdapter();
	    setContentView(viewPager);
	    setViewPagerListeners();
	    
		viewPager.setCurrentItem(SCREEN_INDEX);
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

	private void setViewPagerAdapter() {
		try {
			ModulePagerAdapter modulePagerAdapter=new ModulePagerAdapter(getSupportFragmentManager());
			viewPager.setAdapter(modulePagerAdapter);
		}
		catch(Exception e)
		{
		 Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();	
		}
		
	}
	
	private void setViewPagerListeners(){
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
	private ArrayList<Fragment> mFragments;
	public class ModulePagerAdapter extends FragmentStatePagerAdapter {
		
	
		
		public ModulePagerAdapter(FragmentManager fm) {
			super(fm);
			mFragments = new ArrayList<Fragment>();
			//DISABLED BY BIJENDRA ON 13-02-15 TO STOP APPEARING  DISCLAIMER SCREEN
		//	mFragments.add(new WizardDisclaimer());
			mFragments.add(new WizardChooseLanguage());
			//mFragments.add(WizardNotificationSetting.newInstance(0));
			/*mFragments.add(new WizardLogingRegister());*/
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
	
	public class NonSwipeableViewPager extends ViewPager {

	    public NonSwipeableViewPager(Context context) {
	        super(context);
	    }

	    public NonSwipeableViewPager(Context context, AttributeSet attrs) {
	        super(context, attrs);
	    }

	    @Override
	    public boolean onInterceptTouchEvent(MotionEvent arg0) {
	        // Never allow swiping to switch between pages
	        return false;
	    }

	    @Override
	    public boolean onTouchEvent(MotionEvent event) {
	        // Never allow swiping to switch between pages
	        return false;
	    }
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		switch(keyCode) {
			 
			case KeyEvent.KEYCODE_BACK:
				if(FLAG_WEB_LAYOUT_VISIBLE) {
					((WizardLogingRegister) mFragments.get(SIGN_IN_SCREEN)).hideWebLayout();
					 return true;
						 
					} 
				break;
		}
	    return super.onKeyDown(keyCode, event);
	}
	@Override
	public void clickedSignInButton(String userId, String password) {
		saveScreenInsexInPrefs(SIGN_IN_SCREEN);
		if(callerActivity == CGlobalVariables.APP_MODULE_SCREEN) {
			startActivity(new Intent(ActWizardScreens.this,ActAppModule.class));
		}else if(callerActivity == CGlobalVariables.HOME_INPUT_SCREEN) {
			
			 Intent intent = new Intent();
				Bundle bundle=new Bundle();
				bundle.putString("LOGIN_NAME",userId );
				bundle.putString("LOGIN_PWD",password );
				intent.putExtras(bundle);
				
				setResult(RESULT_OK,intent);
		}
		 
		ActWizardScreens.this.finish();
	}

	@Override
	public void clickedSignUpButton(int pos) {
		saveScreenInsexInPrefs(SIGN_IN_SCREEN);		
	}



	@Override
	public void clickedSkipSignInButton() {
		saveScreenInsexInPrefs(SIGN_IN_SCREEN);
		if(callerActivity == CGlobalVariables.APP_MODULE_SCREEN) {
			startActivity(new Intent(ActWizardScreens.this,ActAppModule.class));
		}else if(callerActivity == CGlobalVariables.HOME_INPUT_SCREEN) {
				setResult(RESULT_CANCELED);
		}
		ActWizardScreens.this.finish();
	}


	@Override
	public void clickedForgetPasswordButton() {
		saveScreenInsexInPrefs(SIGN_IN_SCREEN);
	}



	@Override
	public void clickedDisclaimerNextButton() {
		saveScreenInsexInPrefs(CHOOSE_LANGUAGE_SCREEN);
		viewPager.setCurrentItem(CHOOSE_LANGUAGE_SCREEN);
	}

	@Override
	public void clickedChooseLanguageNextButton(int languageCode) {
		if(languageCode != LANGUAGE_CODE) {
			Intent intent = new Intent(getApplicationContext(), ActWizardScreens.class);
			intent.putExtra("LANGUAGE_CODE", LANGUAGE_CODE);
			startActivity(intent);
			saveScreenInsexInPrefs(NOTIFICATION_SETTING_SCREEN);
			this.finish();
		}else {
			saveScreenInsexInPrefs(NOTIFICATION_SETTING_SCREEN);
			viewPager.setCurrentItem(NOTIFICATION_SETTING_SCREEN);
		}
	}

	private void saveScreenInsexInPrefs(int Screen_Index) {
		 SharedPreferences sharedPreferences = getSharedPreferences(CGlobalVariables.APP_PREFS_NAME, Context.MODE_PRIVATE);
	     SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
	     sharedPrefEditor.putInt(CGlobalVariables.APP_PREFS_AppWizardScreenIndex,Screen_Index);
	     sharedPrefEditor.commit();
	}
	
	private int getScreenIndex() {
		SharedPreferences sharedPreferences = getSharedPreferences(CGlobalVariables.APP_PREFS_NAME, Context.MODE_PRIVATE);
		//UPDATED BY BIJENDRA ON 13-02-15 TO DISABLE DISCLAIMER SCREEN
		/*return sharedPreferences.getInt(CGlobalVariables.APP_PREFS_AppWizardScreenIndex, DISCLAIMER_SCREEN);*/
		return sharedPreferences.getInt(CGlobalVariables.APP_PREFS_AppWizardScreenIndex, CHOOSE_LANGUAGE_SCREEN);
	}

	@Override
	public void clickedNotificationSettingNextButton() {
		saveScreenInsexInPrefs(SIGN_IN_SCREEN);
		/*viewPager.setCurrentItem(SIGN_IN_SCREEN);*/
		CUtils.saveWizardShownFirstTime(ActWizardScreens.this);
		startActivity(new Intent(ActWizardScreens.this,ActAppModule.class));
		ActWizardScreens.this.finish();
	}

	@Override
	public boolean hideSkipButton() {
		if(callerActivity == CGlobalVariables.HOME_INPUT_SCREEN) {
			FLAG_HIDE_SPIP_BUTTON=true;
		}  
		return FLAG_HIDE_SPIP_BUTTON;
	}

	@Override
	public void applyBackButon(boolean b) {
		FLAG_WEB_LAYOUT_VISIBLE=b;
		
	}


	
}
