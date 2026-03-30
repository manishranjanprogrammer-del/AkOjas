package com.libojassoft.android.custom.controls;


import com.libojassoft.android.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Typeface;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

public class CustomToast {
	Context _context;
	LayoutInflater _inflater;
	View layout;
	TextView textMsg;
	Activity _activity;
	Typeface  typeface=Typeface.DEFAULT;
	public CustomToast(Context context,LayoutInflater inflater,Activity activity,Typeface  typeface)
	{
		 _context=context;
		 _inflater=inflater;
		 _activity=activity;
		 this.typeface=typeface;
		 layout = inflater.inflate(R.layout.lib_toast_layout,(ViewGroup) _activity.findViewById(R.id.toast_layout_root));		
		textMsg = (TextView) layout.findViewById(R.id.textMsg);

	}
	public void show(String strMsg)
	{
		textMsg.setText(strMsg);
		//textMsg.setTypeface(LibCGlobalVariables.SHOW_FONT_TYPE);
		textMsg.setTypeface( this.typeface);
		Toast toast = new Toast(_context);
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 150);
		toast.setDuration(Toast.LENGTH_LONG);
		toast.setView(layout);
		toast.show();
	}

}
