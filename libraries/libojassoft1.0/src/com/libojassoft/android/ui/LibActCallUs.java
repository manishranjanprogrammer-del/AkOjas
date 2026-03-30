package com.libojassoft.android.ui;

import com.libojassoft.android.R;
import com.libojassoft.android.utils.LibCGlobalVariables;
import com.libojassoft.android.utils.LibCUtils;

import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;

public class LibActCallUs extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LibCUtils.changeAppResourceTypeForLanguage(LibActCallUs.this,LibCGlobalVariables.LIB_LANGUAGE_CODE);
		setContentView(R.layout.libojassoft_lay_callus);
		ViewGroup gv = (ViewGroup) getWindow().getDecorView();
		LibCUtils.changeAllViewsFonts(gv,LibCGlobalVariables.SHOW_FONT_TYPE);
	}

	
	public void onCallUsCancel(View v)
	{
		this.finish();
	}
	
}
