package com.libojassoft.android.customreceiver;

import java.util.Calendar;

import com.libojassoft.android.utils.LibCGlobalVariables;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
/**
 * When phone get reboot then this broadcast receiver restart Alarm for every hour to check new Article Notification service.
 * @author Hukum
 * @since 19-July-2013
 */
public class LibBroadcastAstrosageArticle extends BroadcastReceiver {

	
	private long CHECK_PUBLISH_SERVICE = 60 * 60 * 1000;//1 Hour Interval
	/**
	 * NEW_UNIVERSAL_ARTICLE_INTENT_ACTION is universal Action which will start All Application's repeating broadcast.
	 * this is newly added Broadcast Action in all applications which use this-Lib and implement astrosage article notification service.
	 */
	@Override
	public void onReceive(Context context, Intent intent) {
		
		if("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())){
			
				/*Intent intentToStartService = new Intent(LibCGlobalVariables.NEW_UNIVERSAL_ARTICLE_INTENT_ACTION);
				PendingIntent pendingIntentForRSSFeed = PendingIntent.getBroadcast(context.getApplicationContext(), 0, intentToStartService, 0);
				AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
				Calendar calendar = Calendar.getInstance();
				calendar.setTimeInMillis(System.currentTimeMillis());
				calendar.add(Calendar.SECOND, 59);
				alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), CHECK_PUBLISH_SERVICE,pendingIntentForRSSFeed);
*/
		}
	}

}
