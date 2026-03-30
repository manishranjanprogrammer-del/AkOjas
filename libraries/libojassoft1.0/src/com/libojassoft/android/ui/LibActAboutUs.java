package com.libojassoft.android.ui;

import com.libojassoft.android.R;
import com.libojassoft.android.utils.LibCGlobalVariables;
import com.libojassoft.android.utils.LibCUtils;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

public class LibActAboutUs extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LibCUtils.changeAppResourceTypeForLanguage(LibActAboutUs.this,LibCGlobalVariables.LIB_LANGUAGE_CODE);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.libojassoft_lay_aboutus);		
		ViewGroup gv = (ViewGroup) getWindow().getDecorView();
		LibCUtils.changeAllViewsFonts(gv,LibCGlobalVariables.SHOW_FONT_TYPE);
		writeApplicationVersion();// ADDED BY BIJENDRA ON 29-JUNE-2013
	}


	public void openAstroShop(View view)
	{
		this.finish();
		LibCUtils.goToAstroSageShop(LibActAboutUs.this, view.getTag().toString());
	}
	public void goToClose(View v)
	{
		this.finish();
	}
	
	/**
	 * This function write the Version code of the application
	 */
	private void writeApplicationVersion() {
		TextView _tvAppVersion = (TextView) findViewById(R.id.tvAppVersion1);
		try {
			String appVerName = LibCUtils
					.getApplicationVersionToShow(getApplicationContext());
			if (LibCGlobalVariables.LIB_LANGUAGE_CODE == LibCGlobalVariables.LIB_HINDI)
				appVerName = appVerName.replace(".", "-");
			
		

			String verText = getResources()
					.getString(R.string.lib_app_version_text) + " " + appVerName;
			_tvAppVersion.setText(verText);

		} catch (Exception e) {
			Log.d("VERSION-Error", e.getMessage());
		}

	}
	

}
