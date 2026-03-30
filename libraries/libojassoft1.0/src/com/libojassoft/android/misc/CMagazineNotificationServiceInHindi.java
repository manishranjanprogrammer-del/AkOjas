package com.libojassoft.android.misc;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.libojassoft.android.R;
import com.libojassoft.android.customrssfeed.CMessage;
import com.libojassoft.android.customrssfeed.CXmlPullFeedParser;
import com.libojassoft.android.dao.NotificationDBManager;
import com.libojassoft.android.models.NotificationModel;
import com.libojassoft.android.utils.LibCGlobalVariables;
import com.libojassoft.android.utils.LibCUtils;


import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import android.text.TextUtils;

import static android.content.Context.NOTIFICATION_SERVICE;
import static com.libojassoft.android.utils.LibCGlobalVariables.KEY_NOTIFICATION_COUNT;
import static com.libojassoft.android.utils.LibCGlobalVariables.NOTIFICATION_ARTICLE;
import static com.libojassoft.android.utils.LibCGlobalVariables.NOTIFICATION_TYPE;
import static com.libojassoft.android.utils.LibCGlobalVariables.UPDATE_NOTIFICATION_COUNT;

public class CMagazineNotificationServiceInHindi implements SendDataBackToComponent {
    Context _context;
    int timeToDelay = 0;
    String applicationAdvId;
    //int cycleIndex=0;//ADDED BY BIJENDRA ON 1-JULY-13
    // String timeFlag="";
    int _presentHour = 0;
    private String blogLink;

    public CMagazineNotificationServiceInHindi(Context context, int timeDelay, String appAdvId) {
        _context = context;
        timeToDelay = timeDelay;
        applicationAdvId = appAdvId;
        _presentHour = Calendar.getInstance().get(Calendar.HOUR_OF_DAY);
    }


    /**
     * This function is used to check that blog  detail saved on SD card or not
     *
     * @author Bijendra (8-july-13)
     */
    private void showNotificationTwiceInADay() {
        JSONObject obj = null;

        boolean isStartService = false;
        LibCGlobalVariables.MAGZINE_FIRST_CYCLE_FLAG = 0;
        LibCGlobalVariables.MAGZINE_SECOND_CYCLE_FLAG = 0;
        LibCGlobalVariables.TODAY_DATE_ID = "";
        LibCGlobalVariables.MAGAZINE_BOLG_ID = "";

        /*if (!LibCUtils.isExternalStorageAvailableToRead())//ADDED BY BIJENDRA ON 09-MAY-2013
            return;*/

        if ((_presentHour >= LibCGlobalVariables.MAGZINE_FIRST_CYCLE_TIME) && (_presentHour <= (LibCGlobalVariables.MAGZINE_FIRST_CYCLE_TIME + LibCGlobalVariables.MAGZINE_CYCLE_TIME_INTERVAL))) {
            LibCGlobalVariables.MAGZINE_FIRST_CYCLE_FLAG = 1;
            LibCGlobalVariables.TODAY_DATE_ID = LibCUtils.getFormattedTodayDate();

            obj = LibCUtils.getSavedJsonMagazineDetailObjectHindi(_context);

            if (obj != null) {
                String jsonToday = "";
                int firstFlag = 0;
                try {
                    jsonToday = obj.getString(LibCGlobalVariables.JSON_MAGAZINE_TODAY);
                    firstFlag = obj.getInt(LibCGlobalVariables.JSON_MAGZINE_FIRST_CYCLE_FLAG);
                    //LibCGlobalVariables.MAGAZINE_BOLG_ID=obj.getString(LibCGlobalVariables.JSON_MAGAZINE_TODAY);
                    //MODIFIED BY BIJENDRA ON 9-JULY-13
                    LibCGlobalVariables.MAGAZINE_BOLG_ID = obj.getString(LibCGlobalVariables.JSON_MAGAZINE_BOLG_ID);


                } catch (Exception e) {
                    e.printStackTrace();
                }

                //STEP-1:CHECK TODAT DATE-IF(TODAY DATE IS SAME BUT FIRST FLAOG IS 0 THEN GO TO THE SERVICE)//
                if (LibCGlobalVariables.TODAY_DATE_ID.equalsIgnoreCase(jsonToday)) {
                    if (firstFlag == 0)
                        isStartService = true;
                } else
                    isStartService = true;
            } else {
                isStartService = true;
                //Log.d("BLOG_P", "JSON-"+LibCGlobalVariables.MAGAZINE_BOLG_ID);
            }
			/*if(isStartService)
				cycleIndex=LibCGlobalVariables.MAGZINE_FIRST_CYCLE_FLAG;*/

            //Log.d("BLOG_P", "isStartService-"+String.valueOf(isStartService));
        }


        if ((_presentHour >= LibCGlobalVariables.MAGZINE_SECOND_CYCLE_TIME) && (_presentHour <= (LibCGlobalVariables.MAGZINE_SECOND_CYCLE_TIME + LibCGlobalVariables.MAGZINE_CYCLE_TIME_INTERVAL))) {
            LibCGlobalVariables.MAGZINE_FIRST_CYCLE_FLAG = 1;
            LibCGlobalVariables.MAGZINE_SECOND_CYCLE_FLAG = 1;
            LibCGlobalVariables.TODAY_DATE_ID = LibCUtils.getFormattedTodayDate();


            obj = LibCUtils.getSavedJsonMagazineDetailObjectHindi(_context);
            if (obj != null) {
                String jsonToday = "";
                int secondFlag = 0;
                try {
                    jsonToday = obj.getString(LibCGlobalVariables.JSON_MAGAZINE_TODAY);
                    secondFlag = obj.getInt(LibCGlobalVariables.JSON_MAGZINE_SECOND_CYCLE_FLAG);

                    //MODIFIED BY BIJENDRA ON 9-JULY-13
                    LibCGlobalVariables.MAGAZINE_BOLG_ID = obj.getString(LibCGlobalVariables.JSON_MAGAZINE_BOLG_ID);
                } catch (Exception e) {

                }

                if (LibCGlobalVariables.TODAY_DATE_ID.equalsIgnoreCase(jsonToday)) {
                    if (secondFlag == 0)
                        isStartService = true;
                } else
                    isStartService = true;
            } else
                isStartService = true;
			
			/*if(isStartService)
				cycleIndex=LibCGlobalVariables.MAGZINE_SECOND_CYCLE_FLAG;*/

            //Log.d("BLOG_P", "isStartService-"+String.valueOf(isStartService));
        }

        if (isStartService) {
            startNotificationServiceWithDelay(_context);

        }
    }

    public void startMyService() {

      /*  if (!LibCUtils.isExternalStorageAvailableToRead())//ADDED BY BIJENDRA ON 09-MAY-2013
            return;*/

        //CHECK INTERNET CONNECTIOIN
        if (LibCUtils.isInternetOn(_context)) {

			/*Timer timer1 = new Timer();
		    timer1.schedule(new TimerTask() {
		        @Override
		        public void run() {
		        	switch(LibCUtils.getUserChoiceFromSDCard()) {
	        		case LibCGlobalVariables.ONCE_IN_A_DAY:
	        			showNotificationOnceInADay();
	        			break;
	        		case LibCGlobalVariables.TWICE_IN_A_DAY:
	        			showNotificationTwiceInADay();
	        			break;
	        		case LibCGlobalVariables.AS_MANY_TIME_IT_COMES:
	        			showAllNotifications();
	        			break;
	        		case LibCGlobalVariables.NEVER:
	        			// do nothing
	        			break;
	        	}			        	  
		    }}, timeToDelay);*/

            switch (LibCUtils.getUserChoiceFromSDCard(_context)) {
                case LibCGlobalVariables.ONCE_IN_A_DAY:
                    showNotificationOnceInADay();
                    break;
                case LibCGlobalVariables.TWICE_IN_A_DAY:
                    showNotificationTwiceInADay();
                    break;
                case LibCGlobalVariables.AS_MANY_TIME_IT_COMES:
                    showAllNotifications();
                    break;
                case LibCGlobalVariables.NEVER:
                    // do nothing
                    break;
            }
        } else {
            //Log.d("BLOG_P", "INTERNET ID OFF");
        }


    }


    protected void showNotificationOnceInADay() {

        JSONObject obj = null;
        boolean isStartService = false;

        LibCGlobalVariables.MAGZINE_FIRST_CYCLE_FLAG = 0;
        LibCGlobalVariables.MAGZINE_SECOND_CYCLE_FLAG = 0;
        LibCGlobalVariables.TODAY_DATE_ID = "";
        LibCGlobalVariables.MAGAZINE_BOLG_ID = "";

       /* if (!LibCUtils.isExternalStorageAvailableToRead())//ADDED BY BIJENDRA ON 09-MAY-2013
            return;*/

        if ((_presentHour >= LibCGlobalVariables.MAGZINE_FIRST_CYCLE_TIME) && (_presentHour <= (LibCGlobalVariables.MAGZINE_FIRST_CYCLE_TIME + LibCGlobalVariables.MAGZINE_CYCLE_TIME_INTERVAL))) {

            LibCGlobalVariables.MAGZINE_FIRST_CYCLE_FLAG = 1;
            LibCGlobalVariables.TODAY_DATE_ID = LibCUtils.getFormattedTodayDate();
            obj = LibCUtils.getSavedJsonMagazineDetailObjectHindi(_context);
            if (obj != null) {
                String jsonToday = "";
                try {
                    jsonToday = obj.getString(LibCGlobalVariables.JSON_MAGAZINE_TODAY);
                    LibCGlobalVariables.MAGAZINE_BOLG_ID = obj.getString(LibCGlobalVariables.JSON_MAGAZINE_BOLG_ID);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (LibCGlobalVariables.TODAY_DATE_ID.equalsIgnoreCase(jsonToday)) {
                    isStartService = false;
                } else {
                    isStartService = true;
                }

            } else {
                isStartService = true;
            }

        } else if ((_presentHour >= LibCGlobalVariables.MAGZINE_SECOND_CYCLE_TIME) && (_presentHour <= (LibCGlobalVariables.MAGZINE_SECOND_CYCLE_TIME + LibCGlobalVariables.MAGZINE_CYCLE_TIME_INTERVAL))) {

            LibCGlobalVariables.MAGZINE_FIRST_CYCLE_FLAG = 1;
            LibCGlobalVariables.MAGZINE_SECOND_CYCLE_FLAG = 1;
            LibCGlobalVariables.TODAY_DATE_ID = LibCUtils.getFormattedTodayDate();
            obj = LibCUtils.getSavedJsonMagazineDetailObjectHindi(_context);
            if (obj != null) {
                String jsonToday = "";
                try {
                    jsonToday = obj.getString(LibCGlobalVariables.JSON_MAGAZINE_TODAY);
                    LibCGlobalVariables.MAGAZINE_BOLG_ID = obj.getString(LibCGlobalVariables.JSON_MAGAZINE_BOLG_ID);
                } catch (Exception e) {
                    e.printStackTrace();
                }

                if (LibCGlobalVariables.TODAY_DATE_ID.equalsIgnoreCase(jsonToday)) {
                    isStartService = false;
                } else {
                    isStartService = true;
                }

            } else {
                isStartService = true;
            }
        }

        if (isStartService) {
            startNotificationServiceWithDelay(_context);

        }
    }

    protected void showAllNotifications() {

        JSONObject obj = null;
        boolean isStartService = false;

        LibCGlobalVariables.MAGZINE_FIRST_CYCLE_FLAG = 0;
        LibCGlobalVariables.MAGZINE_SECOND_CYCLE_FLAG = 0;
        LibCGlobalVariables.TODAY_DATE_ID = "";
        LibCGlobalVariables.MAGAZINE_BOLG_ID = "";

      /*  if (!LibCUtils.isExternalStorageAvailableToRead())//ADDED BY BIJENDRA ON 09-MAY-2013
            return;*/

        if ((_presentHour >= LibCGlobalVariables.MAGZINE_FIRST_CYCLE_TIME) && (_presentHour <= (LibCGlobalVariables.MAGZINE_FIRST_CYCLE_TIME + LibCGlobalVariables.MAGZINE_CYCLE_TIME_INTERVAL))) {

            LibCGlobalVariables.MAGZINE_FIRST_CYCLE_FLAG = 1;
            LibCGlobalVariables.TODAY_DATE_ID = LibCUtils.getFormattedTodayDate();
            obj = LibCUtils.getSavedJsonMagazineDetailObjectHindi(_context);
            if (obj != null) {
                try {
                    LibCGlobalVariables.MAGAZINE_BOLG_ID = obj.getString(LibCGlobalVariables.JSON_MAGAZINE_BOLG_ID);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isStartService = true;
            } else {
                isStartService = true;
            }

        } else if ((_presentHour >= LibCGlobalVariables.MAGZINE_SECOND_CYCLE_TIME) && (_presentHour <= (LibCGlobalVariables.MAGZINE_SECOND_CYCLE_TIME + LibCGlobalVariables.MAGZINE_CYCLE_TIME_INTERVAL))) {

            LibCGlobalVariables.MAGZINE_FIRST_CYCLE_FLAG = 1;
            LibCGlobalVariables.MAGZINE_SECOND_CYCLE_FLAG = 1;
            LibCGlobalVariables.TODAY_DATE_ID = LibCUtils.getFormattedTodayDate();
            obj = LibCUtils.getSavedJsonMagazineDetailObjectHindi(_context);
            if (obj != null) {
                try {
                    LibCGlobalVariables.MAGAZINE_BOLG_ID = obj.getString(LibCGlobalVariables.JSON_MAGAZINE_BOLG_ID);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                isStartService = true;
            } else {
                isStartService = true;
            }
        }

        if (isStartService) {
            startNotificationServiceWithDelay(_context);

        }
    }

    private static int MY_NOTIFICATION_ID = 2006;
    private NotificationManager mNotificationManager;
    String _urlToGo = null;
    public final String RSSFEEDOFCHOICE = LibCGlobalVariables.rssFeedURLHindi;
    public static CFileOperations _fileoperations = null;
    private String CALLER_AD_ID = null;

    int _rssFeedItemIndex = 0;

    protected void startNotificationServiceWithDelay(Context context) {
		/*Intent serviceIntent  = new Intent(context, com.libojassoft.android.custom.services.AstroSageMagazineNotificationServiceForHindi.class);
		serviceIntent.putExtra(com.libojassoft.android.utils.LibCGlobalVariables.CALLER_APP_AD_ID,applicationAdvId.trim());
		//serviceIntent.putExtra("CYCLE_INDEX",cycleIndex);
		*//*serviceIntent.putExtra("TIME_FLAG",timeFlag.trim());
		//getFormattedTodayDateTime()
		serviceIntent.putExtra("NOTI_TITLE",LibCUtils.getSavedNotificationTitle());*//*
		context.startService(serviceIntent);*/
        AstroSageMagazineNotificationServiceForHindi();
    }

    private void AstroSageMagazineNotificationServiceForHindi() {
        LibCGlobalVariables.IS_MAGZINE_NOTIFICATION_SERVICE_RUNNING = true;

        if (_fileoperations == null) {
            _rssFeedItemIndex = 0;
            mNotificationManager = (NotificationManager) _context.getSystemService(NOTIFICATION_SERVICE);
            _fileoperations = new CFileOperations();
            //ADDED BY BIJENDRA ON 9-AUG-13
            CALLER_AD_ID = applicationAdvId;
            new CXmlPullFeedParser(RSSFEEDOFCHOICE, _context, CMagazineNotificationServiceInHindi.this, false).getDataFromServer(_context, RSSFEEDOFCHOICE, getParams(_context), 0);

            //END
            //sendNotification();


        }
    }

    static Map<String, String> getParams(Context context) {
        Map<String, String> params = new HashMap<>();
        params.put("key", LibCUtils.getApplicationSignatureHashCode(context));
        return params;
    }

    private void sendNotification(List<CMessage> listMessage) {

        _fileoperations.checkSettingDirecoryExists();
        MY_NOTIFICATION_ID = LibCUtils.getCustomNotificationId();//ADDED BY BIJENDRA ON 19-06-14

        try//MODIFIED BY BIJENDRA 08-JAN-2013-APPLY  {TRY CATCH}
        {


            if (listMessage != null) {
                String postId = listMessage.get(0).getPostId().trim();
                String postIdSeperator = "post-";
                String _blogId = postId.substring(postId
                        .lastIndexOf(postIdSeperator)
                        + postIdSeperator.length());
                // END ON 16-12-2014
                if (!LibCGlobalVariables.MAGAZINE_BOLG_ID
                        .equalsIgnoreCase(_blogId.trim())) {

                    if (!isSameNotificationSendByOtherAppplication(_blogId
                            .trim())) {

                        String str = listMessage.get(0).getDescription()
                                .toString();
                        str = str.replaceAll("\\<.*?>", "");// to remove Html
                        // image objects
                        // ADDED By Hukum.
                        str = str.replaceAll("&nbsp;", " ");
                        String strTitle = listMessage.get(0).getTitle()
                                .toString();
                        strTitle = strTitle.replace("&amp;", "&");

                        Bitmap icon = BitmapFactory.decodeResource(_context.getResources(), R.mipmap.icon);

						/*NotificationCompat.Builder notification  = new NotificationCompat.Builder(_context)
								.setContentTitle(strTitle)
								.setContentText(str)
								.setSmallIcon(R.drawable.ic_notification)
								.setLargeIcon(icon)
								.setContentIntent(getPendingIntent(listMessage, MY_NOTIFICATION_ID))
								.setAutoCancel(true);


						mNotificationManager.notify(MY_NOTIFICATION_ID,
								notification.build());*/

                        NotificationCompat.Builder notification = new NotificationCompat.Builder(_context)
                                .setContentTitle(strTitle)
                                .setContentText(str)
                                .setSmallIcon(R.drawable.ic_notification)
                                .setLargeIcon(icon)
                                .setDefaults(Notification.DEFAULT_SOUND)
                                .setContentIntent(getPendingIntent(listMessage, MY_NOTIFICATION_ID))
                                .setChannelId(LibCGlobalVariables.NOTIFICATION_CHANNEL_ID)
                                .setAutoCancel(true);

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            NotificationChannel notificationChannel = new NotificationChannel(LibCGlobalVariables.NOTIFICATION_CHANNEL_ID, "horoscopenotification", NotificationManager.IMPORTANCE_HIGH);
                            notificationChannel.enableLights(true);
                            notificationChannel.setLightColor(Color.RED);
                            //notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
                            notificationChannel.enableVibration(true);
                            mNotificationManager.createNotificationChannel(notificationChannel);
                        }
                        mNotificationManager.notify(MY_NOTIFICATION_ID, notification.build());
                        //store notification into local db
                        saveNotificationInLocalDb(strTitle, str, _blogId);
                        _fileoperations
                                .saveMagazineNotificationJSONObjectHindi(_context,
                                        LibCGlobalVariables.TODAY_DATE_ID,
                                        _blogId,
                                        LibCGlobalVariables.MAGZINE_FIRST_CYCLE_FLAG,
                                        LibCGlobalVariables.MAGZINE_SECOND_CYCLE_FLAG);
                    }

                }
            } else {
                //Log.d("DESCRIPTION_ERROR","NOT CALULATE");
            }
        } catch (Exception e) {
            //Log.d("MY_RSS_FEED_ERROR", e.getMessage());
            e.printStackTrace();
        }
    }

    private PendingIntent getPendingIntent(List<CMessage> listMessage, int noticicationId) {


        //String blog_link = LibCGlobalVariables.ASTROSAGE_BLOG_HOME_PAGE_HINDI + "?" + LibCUtils.getNotificationCompaignURL(_context);
        blogLink = listMessage.get(0).getLink().toString();

        Intent notificationIntent = getNotificationIntent();
        // CHANGED BY HUKUM ON 06-MAY-2013 and Ankit on 29-8-2019
        notificationIntent.putExtra("BLOG_LINK_TO_SHOW", blogLink);
        //commented by Ankit on 29-8-2019
        //notificationIntent.putExtra("BLOG_LINK_TO_SHOW", blog_link);// CHANGED BY HUKUM ON 06-MAY-2013
        notificationIntent.putExtra("TITLE_TO_SHOW", listMessage.get(0).getTitle().toString());
        notificationIntent.putExtra(LibCGlobalVariables.CALLER_APP_AD_ID, CALLER_AD_ID);
        //Log.d("BLOG_LINK_TO_SHOW",  listMessage.get(0).getLink().toString());
        //Log.d("NEW_BLOG_LINK_TO_SHOW", blog_link);

        //MODIFIED BY BIJENDRA ON 28-AUG-13
        //PendingIntent contentIntent = PendingIntent.getActivity(this, 0,notificationIntent,android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent contentIntent;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            contentIntent = PendingIntent.getActivity(_context, noticicationId, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            contentIntent = PendingIntent.getActivity(_context, noticicationId, notificationIntent, PendingIntent.FLAG_ONE_SHOT);
        }
        return contentIntent;

    }

    /**
     * This function is used to check for that previous application has send the same notification
     *
     * @return
     * @author Bijendra(11 - july - 13)
     */
    private boolean isSameNotificationSendByOtherAppplication(String newBlogId) {

        boolean _isSameNotificationSend = false;
        //int _firstFlag=0,_secondFlag=0;
        String oldBlogId = null;

        JSONObject obj = null;
        try {
            ////Log.e("BIJENDRA","isSameNotificationSendByOtherAppplication");
            obj = LibCUtils.getSavedJsonMagazineDetailObjectHindi(_context);
            if (obj != null) {
                oldBlogId = obj.getString(LibCGlobalVariables.JSON_MAGAZINE_BOLG_ID);

                if (newBlogId.equalsIgnoreCase(oldBlogId)) {
                    _isSameNotificationSend = true;
                }


				/*_firstFlag=obj.getInt(LibCGlobalVariables.JSON_MAGZINE_FIRST_CYCLE_FLAG);
				_secondFlag=obj.getInt(LibCGlobalVariables.JSON_MAGZINE_SECOND_CYCLE_FLAG);

				if(cycleIndex==LibCGlobalVariables.MAGZINE_FIRST_CYCLE_FLAG)
				{
					if(_firstFlag==0)
						_isSameNotificationSend =false;
					else
						_isSameNotificationSend =true;
				}
				if(cycleIndex==LibCGlobalVariables.MAGZINE_SECOND_CYCLE_FLAG)
				{
					if(_secondFlag==0)
						_isSameNotificationSend =false;
					else
						_isSameNotificationSend =true;
				}*/
            }
        } catch (Exception e) {

        }
        return _isSameNotificationSend;

    }

    /**
     * This function is used to return intent to launch when open notification
     *
     * @return Intent
     * @author Bijendra(28 - aug - 13)
     */
    private Intent getNotificationIntent() {
        Intent notificationIntent = new Intent();
        notificationIntent = new Intent(Intent.ACTION_MAIN);
        String mPackage = _context.getPackageName();
        String mClass = ".ui.act.ActShowOjasSoftArticlesWithTabs";
        notificationIntent.setComponent(new ComponentName(mPackage, mPackage + mClass));

        return notificationIntent;
    }

    private void saveNotificationInLocalDb(String tit, String msg, String blogId) {

        //String blog_link = LibCGlobalVariables.ASTROSAGE_BLOG_HOME_PAGE_HINDI + "?" + LibCUtils.getNotificationCompaignURL(_context);

        NotificationModel notificationModel = new NotificationModel();
        notificationModel.setMessage(msg);
        notificationModel.setTitle(tit);
        notificationModel.setLink(blogLink);
        notificationModel.setNtId("");
        notificationModel.setExtra("");
        notificationModel.setImgUrl("");
        notificationModel.setBlogId(blogId);
        notificationModel.setNotificationType(NOTIFICATION_ARTICLE);
        notificationModel.setTimestamp(System.currentTimeMillis() + "");

        NotificationDBManager dbManager = new NotificationDBManager(_context);
        dbManager.addNotification(notificationModel);
        //update notification count
        int count = LibCUtils.getIntData(_context, KEY_NOTIFICATION_COUNT, 0);
        LibCUtils.saveIntData(_context, KEY_NOTIFICATION_COUNT, ++count);
        Intent intent = new Intent(UPDATE_NOTIFICATION_COUNT);
        intent.putExtra(NOTIFICATION_TYPE, NOTIFICATION_ARTICLE);
        LocalBroadcastManager.getInstance(_context).sendBroadcast(intent);
    }

    @Override
    public void doActionAfterGetResult(String response, int method) {
        try {
            if (!TextUtils.isEmpty(response)) {
                //response = new String(response.getBytes("ISO-8859-1"), "UTF-8");
                List<CMessage> listMessage = LibCUtils.parseXML(response);
                sendNotification(listMessage);

            }
            LibCGlobalVariables.IS_MAGZINE_NOTIFICATION_SERVICE_RUNNING = false;
            _fileoperations = null;
        } catch (Exception e) {

        }

    }

    @Override
    public void doActionOnError(String response) {

    }
}
