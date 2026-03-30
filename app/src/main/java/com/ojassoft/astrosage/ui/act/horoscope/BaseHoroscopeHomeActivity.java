package com.ojassoft.astrosage.ui.act.horoscope;

import android.graphics.Typeface;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.appcompat.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;



/*import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;*/
/*import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;*/
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CScreenConstants;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.varta.ui.activity.BaseActivity;

public class BaseHoroscopeHomeActivity extends BaseActivity {

    public int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    public Typeface typeface;
    public CScreenConstants SCREEN_CONSTANTS = null;

    protected Fragment mFrag;


    private void initValues() {
        typeface = CUtils.getRobotoFont(getApplicationContext(), LANGUAGE_CODE, CGlobalVariables.regular);
        SCREEN_CONSTANTS = new CScreenConstants(this, typeface);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //LANGUAGE_CODE = CUtils.getLanguageCodeFromPreference(this);
        LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication()).getLanguageCode();//ADDED BY HEVENDRA ON 24-12-2014

        initValues();
        // set the Behind View
/*
        setBehindContentView(R.layout.menu_frame);
        if (savedInstanceState == null) {
            FragmentTransaction t = this.getSupportFragmentManager().beginTransaction();
            mFrag = new HoroscopeHomeMenuFragment();
            t.replace(R.id.menu_frame, mFrag);
            t.commit();
        } else {
            mFrag = (Fragment) this.getSupportFragmentManager().findFragmentById(R.id.menu_frame);
        }
*/

        // customize the SlidingMenu
      /*  SlidingMenu sm = getSlidingMenu();
        sm.setShadowWidthRes(R.dimen.shadow_width);
        sm.setShadowDrawable(R.drawable.shadowright);
        sm.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        sm.setFadeDegree(0.35f);
        sm.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);*/

        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.horoscope_home_menu, menu);

		/*getSupportMenuInflater().inflate(R.menu.horoscope_home_menu, menu);*/
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                CUtils.gotoHomeScreen(BaseHoroscopeHomeActivity.this);
                return true;
            case R.id.action_Secondary_Menu:
                //showSecondaryMenu();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        switch (keyCode) {
            case KeyEvent.KEYCODE_MENU:
                //showSecondaryMenu();
                break;
        }
        return super.onKeyDown(keyCode, event);
    }
}
