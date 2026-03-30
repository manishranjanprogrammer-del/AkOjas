package com.libojassoft.android.ui;

import com.libojassoft.android.R;
import com.libojassoft.android.utils.LibCUtils;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

public class ActOtherAppAdvt extends Activity {

	private final static String APPPACKAGE_NAME = "com.ojassoft.astrosage";//"com.ojassoft.Horoscope2012";
	private final static int DAYS_UNTIL_PROMPT = 0;
    private final static int LAUNCH_UNTIL_PROMPT = 12;
	/* (non-Javadoc)
	 * @see android.app.Activity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.layotherappadvt);
		
		
		SharedPreferences prefs = getSharedPreferences("PopUpToPromoteOtherApp", 0);		
		if (LibCUtils.isAppInstalledAlready(ActOtherAppAdvt.this, APPPACKAGE_NAME)){
			this.finish();
		}else {
			SharedPreferences.Editor editor = prefs.edit();
	        
	        //Add to launch Counter
	        long launch_count = prefs.getLong("launch_count", 0) +1;
	        editor.putLong("launch_count", launch_count);
	        
	        //Get Date of first launch
	        Long date_firstLaunch = prefs.getLong("date_first_launch",0);
	        if (date_firstLaunch == 0){
	            date_firstLaunch = System.currentTimeMillis();
	            editor.putLong("date_first_launch", date_firstLaunch);
	        }
	        
	        //Wait at least 'DAYS_UNTIL_PROMPT' days to launch
	        if (launch_count >= LAUNCH_UNTIL_PROMPT) {
	            if (System.currentTimeMillis() >= date_firstLaunch + (DAYS_UNTIL_PROMPT * 24 * 60 * 60 * 1000)){
	            	editor.putLong("launch_count", 0);
	            }else {
	            	this.finish();
	            }
	        }else {
            	this.finish();
            }
	        editor.commit();
		}
    }
	
	public void gotoCancel(View v) {
		setResult(RESULT_CANCELED);
		this.finish();
	}
	public void gotoDownloadApp(View v) {
		/*setResult(RESULT_OK);
			Intent intent = new Intent();
			intent.setAction(Intent.ACTION_VIEW);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.addFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
			try {
				// URL FOR DOWNLOAD APPLICATION
				//For Google Play Only
				//intent.setData(Uri.parse("market://details?id=" + APPPACKAGE_NAME));
				//For Google Play and Web browser
				intent.setData(Uri.parse("https://play.google.com/store/apps/details?id="+APPPACKAGE_NAME));
				startActivity(intent);
				this.finish();
			} catch (Exception e) {

			}
		*/
	}
	
}
