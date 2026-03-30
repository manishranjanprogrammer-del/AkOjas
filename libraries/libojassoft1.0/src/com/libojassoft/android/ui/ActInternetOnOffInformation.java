package com.libojassoft.android.ui;


import com.libojassoft.android.R;
import com.libojassoft.android.utils.LibCGlobalVariables;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.TextView;
/**
 * This activity is  use to show internet information  is ON/OFF
 * @author Bijendra (10-may-13)
 *
 */
public class ActInternetOnOffInformation extends Activity{
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.libojassoft_internet_info);
		boolean bOnOff=getIntent().getBooleanExtra(LibCGlobalVariables.EXTRA_INTERNET_ON, false);
		showInformation(bOnOff);
	}
	private void showInformation(boolean isOnOff)
	{
		TextView tvInformation=(TextView) findViewById(R.id.tvInternetInfo);
		if(!isOnOff)
			tvInformation.setText("internet is not working");
	}
	public void closeMe(View v)
	{
		this.finish();
	}
	

}
