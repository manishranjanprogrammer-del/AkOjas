package com.libojassoft.android.custom.services;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.json.JSONObject;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ComponentName;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.IBinder;
import androidx.core.app.NotificationCompat;

import com.libojassoft.android.customrssfeed.CMessage;
import com.libojassoft.android.misc.CFileOperations;
import com.libojassoft.android.utils.LibCGlobalVariables;
import com.libojassoft.android.utils.LibCUtils;

@TargetApi(8)
public class AstroSageMagazineNotificationService extends Service {

    /**
     * Local variables
     **/
    private static int MY_NOTIFICATION_ID = 2005;
    private NotificationManager mNotificationManager;
    String _urlToGo = null;
    public final String RSSFEEDOFCHOICE = LibCGlobalVariables.rssFeedURL;
    public static CFileOperations _fileoperations = null;
    private String CALLER_AD_ID = null;

    int _rssFeedItemIndex = 0;

    // int cycleIndex=0;//ADDED BY BIJENDRA ON 1-JULY-13

    @Override
    public IBinder onBind(Intent arg0) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        LibCGlobalVariables.IS_MAGZINE_NOTIFICATION_SERVICE_RUNNING = true;

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        if (AstroSageMagazineNotificationService._fileoperations == null) {
            _rssFeedItemIndex = 0;
            mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            AstroSageMagazineNotificationService._fileoperations = new CFileOperations();

            // ADDED BY BIJENDRA ON 9-AUG-13
            try {
                Bundle extras = intent.getExtras();
                if (extras != null) {
                    try {
                        CALLER_AD_ID = extras
                                .getString(LibCGlobalVariables.CALLER_APP_AD_ID);
                        // cycleIndex =extras.getInt("CYCLE_INDEX");

                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                    }
                }
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }// END

            Timer timer1 = new Timer();
            timer1.schedule(new TimerTask() {
                @Override
                public void run() {
                    scheduleNotification();
                }
            }, 30000);

        }

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LibCGlobalVariables.IS_MAGZINE_NOTIFICATION_SERVICE_RUNNING = false;
        AstroSageMagazineNotificationService._fileoperations = null;

    }

    private void scheduleNotification() {
        sendNotification();
    }

    private void sendNotification() {

        _fileoperations.checkSettingDirecoryExists();
        MY_NOTIFICATION_ID = LibCUtils.getCustomNotificationId();// ADDED BY
        // BIJENDRA
        // ON
        // 19-06-14
        /*try// MODIFIED BY BIJENDRA 08-JAN-2013-APPLY {TRY CATCH}
        {

            List<CMessage> listMessage = new CXmlPullFeedParser(RSSFEEDOFCHOICE,this,false)
                    .parse();

            if (listMessage != null) { // DISABLED BY DEEPAK ON 16-12-2014
                // String publishedDate =
                // listMessage.get(0).getDate().toString().trim();

                // Date pubDate=new Date(publishedDate.trim());
                // String _blogId=String.valueOf(pubDate.getDate())+
                // String.valueOf(pubDate.getMonth())+
                // String.valueOf(pubDate.getYear());
                // _blogId +=String.valueOf(pubDate.getHours())+
                // String.valueOf(pubDate.getMinutes());
                // ADDED BY DEEPAK ON 16-12-2014
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
                        *//*@SuppressWarnings("deprecation")
						Notification notification = new Notification(
								R.drawable.magz_notification_icon,
								TextUtils.htmlEncode(listMessage.get(0)
										.getTitle().toString()),
								System.currentTimeMillis());
						notification.flags = Notification.FLAG_AUTO_CANCEL;
*//*
         *//*	RemoteViews contentView = new RemoteViews(
								getPackageName(), R.layout.custom_notification);
						contentView.setImageViewResource(R.id.image,
								R.drawable.magz_notification_icon);
*//*
                        String str = listMessage.get(0).getDescription()
                                .toString();
                        str = str.replaceAll("\\<.*?>", "");// to remove Html
                        // image objects
                        // ADDED By Hukum.
                        str = str.replaceAll("&nbsp;", " ");
                        // String description =
                        // LibCUtils.formatNotificationDescription(str,
                        // "title=", 200);
                        // ADDED BY BIJENDRA ON 29-JUNE-13
                        String strTitle = listMessage.get(0).getTitle()
                                .toString();
                        strTitle = strTitle.replace("&amp;", "&");
                        // Log.d("TITLE", strTitle);
                        // contentView.setTextViewText(R.id.title,
                        // listMessage.get(0).getTitle().toString()+" : "+str);
						*//*contentView.setTextViewText(R.id.title, strTitle + ": "
								+ str);*//*
                       // String CHANNEL_ID = "astrosage_magzine_notification_service_notification";
                        Bitmap icon = BitmapFactory.decodeResource(AstroSageMagazineNotificationService.this.getResources(), R.mipmap.icon);

                        NotificationCompat.Builder notification = new NotificationCompat.Builder(AstroSageMagazineNotificationService.this)
                                .setContentTitle(strTitle)
                                .setContentText(str)
                                .setSmallIcon(R.drawable.ic_notification)
                                .setLargeIcon(icon)
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


                        // END
                        // This is custom Notification.
						*//*notification.contentView = contentView;
						notification.contentIntent = getPendingIntent(
								listMessage, MY_NOTIFICATION_ID);*//*
                        mNotificationManager.notify(MY_NOTIFICATION_ID,
                                notification.build());// DISABLED BY BIJENDRA ON
                        // 19-06-14

                        // Update setting file in sdCard.

                        _fileoperations.saveMagazineNotificationJSONObject(
                                AstroSageMagazineNotificationService.this,
                                LibCGlobalVariables.TODAY_DATE_ID, _blogId,
                                LibCGlobalVariables.MAGZINE_FIRST_CYCLE_FLAG,
                                LibCGlobalVariables.MAGZINE_SECOND_CYCLE_FLAG);
                    }

                }
				*//*
         * _fileoperations.setMagazineNotificationId(LibCGlobalVariables.
         * TODAY_DATE_ID
         * ,_blogId,LibCGlobalVariables.MAGZINE_FIRST_CYCLE_FLAG,
         * LibCGlobalVariables.MAGZINE_SECOND_CYCLE_FLAG);
         *//*
            } else {
                // Log.d("DESCRIPTION_ERROR","NOT CALULATE");
            }
        } catch (Exception e) {
            // Log.d("MY_RSS_FEED_ERROR", e.getMessage());
            e.printStackTrace();
        }*/

        AstroSageMagazineNotificationService.this.stopSelf();
    }

    private PendingIntent getPendingIntent(List<CMessage> listMessage,
                                           int noticicationId) {

        // String
        // blog_link=listMessage.get(0).getLink().toString()+"?"+LibCUtils.getNotificationCompaignURL(getApplicationContext());;
        // ADDED BY BIJENRA ON 6-JULY-13
        //String blog_link = LibCGlobalVariables.ASTROSAGE_BLOG_HOME_PAGE + "?"+ LibCUtils.getNotificationCompaignURL(getApplicationContext());

        // END

        // Intent notificationIntent = new
        // Intent(getApplicationContext(),ActShowOjasSoftProducts.class);
        /*
         * Intent notificationIntent = new Intent(Intent.ACTION_MAIN); String
         * mPackage = getApplicationContext().getPackageName(); String mClass =
         * ".notification.ActShowOjasSoftArticles";
         * notificationIntent.setComponent(new ComponentName(mPackage,
         * mPackage+mClass));
         */

        Intent notificationIntent = getNotificationIntent();

        // notificationIntent.putExtra("BLOG_LINK_TO_SHOW",
        // listMessage.get(0).getLink().toString());// CHANGED BY HUKUM ON
        // 06-MAY-2013
        // Commented BY Ankit ON 29-8-2019
       // notificationIntent.putExtra("BLOG_LINK_TO_SHOW", blog_link);// CHANGED
        // CHANGED BY Ankit ON 29-8-2019
        notificationIntent.putExtra("BLOG_LINK_TO_SHOW", listMessage.get(0).getLink().toString());
        // BY HUKUM
        // ON
        // 06-MAY-2013
        notificationIntent.putExtra("TITLE_TO_SHOW", listMessage.get(0)
                .getTitle().toString());
        notificationIntent.putExtra(LibCGlobalVariables.CALLER_APP_AD_ID,
                CALLER_AD_ID);

        // MODIFIED BY BIJENDRA ON 28-AUG-13
        /*
         * PendingIntent contentIntent =
         * PendingIntent.getActivity(getApplicationContext(),
         * noticicationId,notificationIntent,PendingIntent.FLAG_ONE_SHOT);
         */
        /*
         * PendingIntent contentIntent =
         * PendingIntent.getActivity(getApplicationContext(), noticicationId,
         * notificationIntent
         * ,PendingIntent.FLAG_ONE_SHOT|PendingIntent.FLAG_UPDATE_CURRENT);
         */// PendingIntent contentIntent = PendingIntent.getActivity(this,
        // 0,notificationIntent,android.content.Intent.FLAG_ACTIVITY_NEW_TASK);
        /*
         * PendingIntent contentIntent =
         * PendingIntent.getActivity(getApplicationContext(), noticicationId,
         * notificationIntent
         * ,PendingIntent.FLAG_ONE_SHOT|PendingIntent.FLAG_UPDATE_CURRENT);
         */

        PendingIntent contentIntent;
        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            contentIntent = PendingIntent.getActivity(this, noticicationId, notificationIntent, PendingIntent.FLAG_IMMUTABLE);
        } else {
            contentIntent = PendingIntent.getActivity(this, noticicationId, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        }

        return contentIntent;

    }

    /**
     * This function is used to check for that previous application has send the
     * same notification
     *
     * @return
     * @author Bijendra(11 - july - 13)
     */
    private boolean isSameNotificationSendByOtherAppplication(String newBlogId) {
        boolean _isSameNotificationSend = false;
        // int _firstFlag=0,_secondFlag=0;
        String oldBlogId = null;

        JSONObject obj = null;
        try {
            // //Log.e("BIJENDRA","isSameNotificationSendByOtherAppplication");
            obj = LibCUtils.getSavedJsonMagazineDetailObject(AstroSageMagazineNotificationService.this);
            if (obj != null) {
                oldBlogId = obj
                        .getString(LibCGlobalVariables.JSON_MAGAZINE_BOLG_ID);

                if (newBlogId.equalsIgnoreCase(oldBlogId)) {
                    _isSameNotificationSend = true;
                }

                /*
                 * _firstFlag=obj.getInt(LibCGlobalVariables.
                 * JSON_MAGZINE_FIRST_CYCLE_FLAG);
                 * _secondFlag=obj.getInt(LibCGlobalVariables
                 * .JSON_MAGZINE_SECOND_CYCLE_FLAG);
                 *
                 * if(cycleIndex==LibCGlobalVariables.MAGZINE_FIRST_CYCLE_FLAG)
                 * { if(_firstFlag==0) _isSameNotificationSend =false; else
                 * _isSameNotificationSend =true; }
                 * if(cycleIndex==LibCGlobalVariables.MAGZINE_SECOND_CYCLE_FLAG)
                 * { if(_secondFlag==0) _isSameNotificationSend =false; else
                 * _isSameNotificationSend =true; }
                 */
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
        String mPackage = getApplicationContext().getPackageName();
        String mClass = ".notification.ActShowOjasSoftArticles";
        notificationIntent.setComponent(new ComponentName(mPackage, mPackage
                + mClass));
        return notificationIntent;
    }

}
