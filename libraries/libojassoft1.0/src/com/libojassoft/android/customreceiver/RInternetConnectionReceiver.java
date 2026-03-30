package com.libojassoft.android.customreceiver;

import com.libojassoft.android.ui.ActInternetOnOffInformation;
import com.libojassoft.android.utils.LibCGlobalVariables;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

/**
 * This BroadcastReceiver is  receive information about internet connection status
 * @author Bijendra (10-may-13)
 *
 */
public class RInternetConnectionReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
	    // TODO Auto-generated method stub
		boolean isCon=false;
		ConnectivityManager connect=(ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo info=connect.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
		NetworkInfo wifi=connect.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
		
		if(info!=null)
			isCon=info.isConnected();
		
		if(wifi!=null && !isCon)
			isCon=wifi.isConnected();		
			
		if(!isCon && LibCGlobalVariables.IS_ACTIVITY_ON)
			openinternetInformationActivity(context,isCon);
		
	}
	private void openinternetInformationActivity(Context context,boolean isCon)
	{
		Intent intent = new Intent(context,ActInternetOnOffInformation.class);	    
	     intent.putExtra(LibCGlobalVariables.EXTRA_INTERNET_ON,isCon);
	     intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
	     context.startActivity(intent);
	}
}
