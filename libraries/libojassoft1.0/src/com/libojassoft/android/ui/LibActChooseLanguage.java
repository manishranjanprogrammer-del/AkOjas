package com.libojassoft.android.ui;

import com.libojassoft.android.R;
import com.libojassoft.android.R.layout;
import com.libojassoft.android.utils.LibCGlobalVariables;
import com.libojassoft.android.utils.LibCUtils;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;
import android.view.View;

public class LibActChooseLanguage extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.libojassoft_lay_chooselanguage);
	}
	public void goToOk(View v)
	{
		
		LibCUtils.saveUserLanguageCode(LibCGlobalVariables.LIB_ENGLISH);
		this.finish();
	}

	
}
