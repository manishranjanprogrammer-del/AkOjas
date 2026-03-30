package com.ojassoft.astrosage.ui.act;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager.widget.ViewPager.OnPageChangeListener;
import androidx.appcompat.widget.Toolbar;

import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

//import com.google.analytics.tracking.android.EasyTracker;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.customadapters.ViewPagerAdapter;
import com.ojassoft.astrosage.jinterface.IPermissionCallback;
import com.ojassoft.astrosage.ui.fragments.FragSignUp;
import com.ojassoft.astrosage.ui.fragments.WizardLogingRegister;
import com.ojassoft.astrosage.ui.fragments.WizardLogingRegister.IWizardLoginRegisterFragmentInterface;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.util.ArrayList;

public class ActLogin extends BaseInputActivity implements IWizardLoginRegisterFragmentInterface,IPermissionCallback{

	
//	ViewPager viewPager;
	
	final int SIGN_IN_SCREEN = 2;
	
	//int SCREEN_INDEX=0;
	public int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
	//public Typeface typeface;
	int callerActivity;
	int position = 0;
	boolean FLAG_HIDE_SPIP_BUTTON=false;
	TextView textViewNotes;
	String[] pageTitles;
//	ViewPagerAdapter adapter;
	//WizardLogingRegister loginRegister;
	//FragSignUp signUpPage;
	//ImageView imgBackButton;
	//TabLayout tabLayout;
	Toolbar toolBar_InputKundli;
	private TabLayout tabs_input_kundli;
	private void initValues() {
		//typeface=CUtils.getUserSelectedLanguageFontType(getApplicationContext(), LANGUAGE_CODE);
		/*textViewNotes = (TextView)findViewById(R.id.textViewNotes);
		textViewNotes.setTypeface(regularTypeface);
		CUtils.setClickableSpan(textViewNotes,
				Html.fromHtml(getString(R.string.login_note)),this,regularTypeface);*/
		/*imgBackButton = (ImageView)findViewById(R.id.imgBackButton);
		imgBackButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				finishActivity();
			}
		});*/
		pageTitles = getResources().getStringArray(
				R.array.Login_titles_list);
	}

	public ActLogin() {
		super(R.string.app_name);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	//	this.requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_login);
		LANGUAGE_CODE = ((AstrosageKundliApplication)getApplication()).getLanguageCode();//ADDED BY HEVENDRA ON 24-12-2014

		try {
			callerActivity = getIntent().getIntExtra("callerActivity", CGlobalVariables.APP_MODULE_SCREEN);
			position = getIntent().getIntExtra("screenId", 0);
		}catch (Exception ex){
			//Log.i("Exception",ex.getMessage().toString());
		}
		initValues();

		// Show login fragment first
		if (savedInstanceState == null) {
			replaceFragment(new WizardLogingRegister(), false);
		}
		
//		viewPager = (ViewPager)findViewById(R.id.pager);
//		viewPager.setId("VP".hashCode());
//		toolBar_InputKundli = (Toolbar) findViewById(R.id.tool_barAppModule);
//		toolBar_InputKundli.setNavigationIcon(R.drawable.ic_back_arrow);
//		setSupportActionBar(toolBar_InputKundli);
//
	 /*   tabLayout = (TabLayout) findViewById(R.id.tab_layout);
        tabLayout.addTab(tabLayout.newTab().setText("Tab 1"));
        tabLayout.addTab(tabLayout.newTab().setText("Tab 2"));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);*/

//		((TextView)findViewById(R.id.tvTitle)).setTypeface(mediumTypeface);
//      //  setListenerForTabs();
//		tabs_input_kundli = (TabLayout) findViewById(R.id.tabs);
//		tabs_input_kundli.setTabMode(TabLayout.MODE_FIXED);
		/*tabs_input_kundli.initTextTypeface(typeface);
		tabs_input_kundli.setCustomTabView(R.layout.lay_input_kundli_tab_title,
				R.id.tabtext);
		tabs_input_kundli.setDistributeEvenly(true); 
		tabs_input_kundli
		.setCustomTabColorizer(new SlidingTabLayoutInputKundli.TabColorizer() {
			@Override
			public int getIndicatorColor(int position) {
				return getResources().getColor(R.color.tabsScrollColor);
			}
		});*/
		
//		getSupportActionBar().setDisplayShowTitleEnabled(false);
//		getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//		//typeface = CUtils.getUserSelectedLanguageFontType(getApplicationContext(),CUtils.getLanguageCodeFromPreference(getApplicationContext()));
//
//		setViewPagerAdapter();
//
//	    setViewPagerListeners();
//
//		tabs_input_kundli.setupWithViewPager(viewPager);
//
//
//		for (int i = 0; i < tabs_input_kundli.getTabCount(); i++) {
//			TabLayout.Tab tab = tabs_input_kundli.getTabAt(i);
//			tab.setCustomView(adapter.getTabView(i));
//		}
//
//		viewPager.setCurrentItem(position);
//
//		adapter.setAlpha(position, tabs_input_kundli);
	}

	public void replaceFragment(Fragment fragment, boolean addToBackStack) {
		FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
		transaction.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out);
		transaction.replace(R.id.fragment_container, fragment);
		if (addToBackStack) transaction.addToBackStack(null);
		transaction.commit();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			//CUtils.gotoHomeScreen(ActLogin.this);
			CUtils.hideMyKeyboard(ActLogin.this);
			this.finish();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}	

	private void setListenerForTabs(){
		
		/*tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }
 
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
 
            }
 
            @Override
            public void onTabReselected(TabLayout.Tab tab) {
 
            }
        });*/
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
		}

	private void setViewPagerAdapter() {
		try {
			/*ModulePagerAdapter modulePagerAdapter=new ModulePagerAdapter(getSupportFragmentManager());
			viewPager.setAdapter(modulePagerAdapter);*/
			//loginRegister=new WizardLogingRegister();
			//signUpPage=new FragSignUp();
//			adapter = new ViewPagerAdapter(getSupportFragmentManager(), ActLogin.this);
//			adapter.addFragment(new WizardLogingRegister(), pageTitles[0]);
//			adapter.addFragment(new FragSignUp(), pageTitles[1]);
//			viewPager.setAdapter(adapter);

		}
		catch(Exception e)
		{
		 Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();	
		}
		
	}
	
//	private void setViewPagerListeners(){
//		viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//
//			@Override
//			public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//			}
//
//			@Override
//			public void onPageSelected(int position) {
//
//				if (tabs_input_kundli != null && adapter != null) {
//					adapter.setAlpha(position, tabs_input_kundli);
//				}
//			}
//
//			@Override
//			public void onPageScrollStateChanged(int state) {
//
//			}
//		});
//	}
	private ArrayList<Fragment> mFragments;

	@Override
	public void isEmailIdPermissionGranted(boolean isPermissionGranted) {
		/*try {
			FragSignUp fragSignUp = (FragSignUp) adapter.getItem(1);
			fragSignUp.checkForPermission(true);
		}catch (Exception ex){
			//Log.e("Exception",ex.getMessage());
		}*/
	}

	public class ModulePagerAdapter extends FragmentStatePagerAdapter {
		
	
		int mNumOfTabs;
		public ModulePagerAdapter(FragmentManager fm) {
			super(fm);
			mFragments = new ArrayList<Fragment>();
			//DISABLED BY BIJENDRA ON 13-02-15 TO STOP APPEARING  DISCLAIMER SCREEN
		
			mFragments.add(new WizardLogingRegister());
			mFragments.add(new FragSignUp());
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
			return pageTitles[position];
		}

	}
	
	/*public class NonSwipeableViewPager extends ViewPager {

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
	}*/

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		
		if(keyCode==KeyEvent.KEYCODE_BACK)
		{
			this.finish();
			 return true;
		}
	    return super.onKeyDown(keyCode, event);
	}
	@Override
	public void clickedSignInButton(String userId, String password) {		
		
		 Intent intent = new Intent();
			Bundle bundle=new Bundle();
			bundle.putString("LOGIN_NAME",userId );
			bundle.putString("LOGIN_PWD",password );
			intent.putExtras(bundle);
			
			setResult(RESULT_OK,intent);
			ActLogin.this.finish();
	}

	@Override
	public void clickedSignUpButton(int pos) {
		//saveScreenInsexInPrefs(SIGN_IN_SCREEN);
//		viewPager.setCurrentItem(pos);

		if (pos == 1){
			replaceFragment(new FragSignUp(), true);
		}else {
			replaceFragment(new WizardLogingRegister(), false);
			// Go back to Login page
			//getSupportFragmentManager().popBackStack();
		}
	}
//
//	@Override
//	public void onBackPressed() {
//		super.onBackPressed();
//		// If signup is open, go back to login instead of closing the activity
//		if (getSupportFragmentManager().getBackStackEntryCount() > 0) {
//			getSupportFragmentManager().popBackStack();
//		} else {
//			super.onBackPressed();
//		}
//	}

	@Override
	public void clickedSkipSignInButton() {
		saveScreenInsexInPrefs(SIGN_IN_SCREEN);
		if(callerActivity == CGlobalVariables.APP_MODULE_SCREEN) {
			startActivity(new Intent(ActLogin.this,ActAppModule.class));
		}else if(callerActivity == CGlobalVariables.HOME_INPUT_SCREEN) {
				setResult(RESULT_CANCELED);
		}
		ActLogin.this.finish();
	}


	@Override
	public void clickedForgetPasswordButton() {
		saveScreenInsexInPrefs(SIGN_IN_SCREEN);
	}




	private void saveScreenInsexInPrefs(int Screen_Index) {
		 SharedPreferences sharedPreferences = getSharedPreferences(CGlobalVariables.APP_PREFS_NAME, Context.MODE_PRIVATE);
	     SharedPreferences.Editor sharedPrefEditor = sharedPreferences.edit();
	     sharedPrefEditor.putInt(CGlobalVariables.APP_PREFS_AppWizardScreenIndex,Screen_Index);
	     sharedPrefEditor.commit();
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
		
		
	}

	private void finishActivity(){
		this.finish();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			/*case PERMISSION_CONTACTS:
				try {
					FragSignUp fragSignUp = (FragSignUp) adapter.getItem(1);
					fragSignUp.checkForPermission(false);
				}catch (Exception ex){
					//Log.e("Exception",ex.getMessage());
				}
				break;*/
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}
