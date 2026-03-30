package com.libojassoft.android.utils;

import com.libojassoft.android.R;
import com.libojassoft.android.beans.LibCGroupMenu;
import com.libojassoft.android.misc.ICustomMenuEvent;
import com.libojassoft.android.misc.LibMenuExpandableListAdapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.TextView;
import android.widget.ExpandableListView.OnChildClickListener;

public class CMisc {

	public static void showMyCustomMenu(String title,LibCGroupMenu cGroupMenu,ICustomMenuEvent iCustomMenuEvent,Typeface customFontType)
	{
		/*DisplayMetrics metrics;
		 int width;

		AlertDialog.Builder builder;
		
		final ICustomMenuEvent _iCustomPopupSubMenu=iCustomMenuEvent;
		int currentapiVersion = android.os.Build.VERSION.SDK_INT;
		//THIS SERVICE RUN MORE THAN 1.6 VERSION
	
	//builder = new AlertDialog.Builder(_context);
		if(currentapiVersion > android.os.Build.VERSION_CODES.GINGERBREAD_MR1)
			 builder = new AlertDialog.Builder(LibCGlobalVariables.context, R.style.MyMenuTheme);
		else
		{
			//ContextThemeWrapper ctw = new ContextThemeWrapper( activity, R.style.MyMenuTheme );
			ContextThemeWrapper ctw = new ContextThemeWrapper(LibCGlobalVariables.activity, R.style.MyMenuTheme );
			 builder= new AlertDialog.Builder( ctw );
			//AlertDialog.Builder builder = new AlertDialog.Builder(context);
		}
		//BEFORE SDK 11
		//ContextThemeWrapper ctw = new ContextThemeWrapper( activity, R.style.MyTheme );
		//AlertDialog.Builder builder= new AlertDialog.Builder( ctw );
		//AlertDialog.Builder builder = new AlertDialog.Builder(context);
		//END
		//FROM SDK 11
		// builder = new AlertDialog.Builder(context, R.style.MyTheme);
		//END
		
		builder.setTitle(title);
		
		//
		metrics = new DisplayMetrics();
		LibCGlobalVariables.activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
	    width = metrics.widthPixels;

		//
		ExpandableListView myList = new ExpandableListView(LibCGlobalVariables.context);
		LibMenuExpandableListAdapter myAdapter = new LibMenuExpandableListAdapter(LibCGlobalVariables.context,LibCGlobalVariables.activity, cGroupMenu,customFontType);
		
		//
		Drawable d = LibCGlobalVariables.activity.getResources().getDrawable(R.drawable.custom_menu_group_indicator_selector);
		myList.setGroupIndicator(d);
		//
		myList.setIndicatorBounds(width - GetDipsFromPixel(50,LibCGlobalVariables.activity), width - GetDipsFromPixel(10,LibCGlobalVariables.activity));

		myList.setAdapter(myAdapter);
		builder.setIcon(R.drawable.custom_menu_group_indicator);
		
		
		builder.setView(myList);
		final AlertDialog   dialog = builder.create();
		
		dialog.show();
		myList.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				// TODO Auto-generated method stub
				//test1(groupPosition, childPosition);
				_iCustomPopupSubMenu.OnClickListenerCustomMenu(groupPosition, childPosition);
				dialog.dismiss();
				return false;
			}
		});
		final int alertTitle = LibCGlobalVariables.context.getResources().getIdentifier( "alertTitle", "id", "android" );
	//Toast.makeText(context, String.valueOf(alertTitle), Toast.LENGTH_LONG).show();
		TextView tv=(TextView)dialog.findViewById( alertTitle );
		tv.setText(title);
		tv.setTypeface(customFontType);
		*/
		
		
	}
	
	public static int GetDipsFromPixel(float pixels,Activity activity)
	{
	 // Get the screen's density scale
	 final float scale = activity.getResources().getDisplayMetrics().density;
	 // Convert the dps to pixels, based on density scale
	 return (int) (pixels * scale + 0.5f);
	}


}
