package com.libojassoft.android.ui;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import org.json.JSONObject;

/*import com.google.ads.AdRequest;
import com.google.ads.AdSize;
import com.google.ads.AdView;*/
////import com.google.analytics.tracking.android.EasyTracker;
////import com.google.analytics.tracking.android.Tracker;
import com.libojassoft.android.R;
import com.libojassoft.android.utils.LibCGlobalVariables;
import com.libojassoft.android.utils.LibCUtils;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;

public class LibActConfigArticleNotification extends Activity {

    private RadioButton rdOnce, rdTwice, rdAsMany, rdNever;
    private int userChoice = -1;
    private ProgressDialog pd;
    TextView heading;
    // This is default Ad-Id of horoscope2013 app. if user of this screen does not pass his advrt-id then this id will show ad.
    private String advertId = "a14ec4f16ca9dd7";

    //Tracker tracker=null;
    /* (non-Javadoc)
     * @see android.app.Activity#onCreate(android.os.Bundle)
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        LibCUtils.changeAppResourceTypeForLanguage(LibActConfigArticleNotification.this, LibCGlobalVariables.LIB_LANGUAGE_CODE);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.libojassoft_lay_notification_config);
        rdOnce = (RadioButton) findViewById(R.id.radioOnce);
        rdTwice = (RadioButton) findViewById(R.id.radioTwice);
        rdAsMany = (RadioButton) findViewById(R.id.radioAsManyTimes);
        rdNever = (RadioButton) findViewById(R.id.radioNever);
        heading = (TextView) findViewById(R.id.textViewHeading);

        advertId = getIntent().getStringExtra(LibCGlobalVariables.CALLER_APP_AD_ID);

        ViewGroup gv = (ViewGroup) getWindow().getDecorView();
        LibCUtils.changeAllViewsFonts(gv, LibCGlobalVariables.SHOW_FONT_TYPE);
        if (!(LibCGlobalVariables.LIB_LANGUAGE_CODE == LibCGlobalVariables.LIB_HINDI)) {
            heading.setTypeface(Typeface.SERIF);
        }

        initUserPreviousChoiceForNotifications(LibActConfigArticleNotification.this);

        showAdvertisement();
    }


    /* (non-Javadoc)
     * @see android.app.Activity#onStart()
     */
    @Override
    protected void onStart() {
        // TODO Auto-generated method stub
        super.onStart();
        //EasyTracker.getInstance().activityStart(this);
        //tracker = EasyTracker.getTracker();
        //LibCUtils.googleAnalyticSend(tracker, LibCGlobalVariables.CAT_SCREEN, LibCGlobalVariables.ANALYITICS_ACTION_NOTIFICATION_CONFIG_SCREEN, LibCGlobalVariables.ANALYITICS_ACTION_NOTIFICATION_CONFIG_SCREEN,0L);
    }


    /* (non-Javadoc)
     * @see android.app.Activity#onStop()
     */
    @Override
    protected void onStop() {
        // TODO Auto-generated method stub
        super.onStop();
        //EasyTracker.getInstance().activityStop(this);
    }


    /**
     * This method shows google advertisement on screen upper side.
     */
    private void showAdvertisement() {
        LinearLayout advLayoutUpper = (LinearLayout) findViewById(R.id.advLayout);
        try {
            advLayoutUpper.removeAllViews();
        } catch (Exception e) {

        }
		/*// Create the adView
		AdView adViewUpper = new AdView(this, AdSize.SMART_BANNER,advertId);
		// Add the adView to it
		advLayoutUpper.addView(adViewUpper);
		// Initiate a generic request to load it with an ad
		AdRequest requestUpper = new AdRequest();			
		adViewUpper.loadAd(requestUpper);
		adViewUpper.setFocusable(true);*/
    }

    private void initUserPreviousChoiceForNotifications(Context context) {
        userChoice = LibCUtils.getUserChoiceFromSDCard(context);

        if (userChoice == LibCGlobalVariables.ONCE_IN_A_DAY) {
            rdOnce.setChecked(true);
        } else if (userChoice == LibCGlobalVariables.TWICE_IN_A_DAY) {
            rdTwice.setChecked(true);
        } else if (userChoice == LibCGlobalVariables.AS_MANY_TIME_IT_COMES) {
            rdAsMany.setChecked(true);
        } else if (userChoice == LibCGlobalVariables.NEVER) {
            rdNever.setChecked(true);
        }
    }


    public void saveUserChoiceInSDCard(View v) {

        if (rdOnce.isChecked()) {
            userChoice = LibCGlobalVariables.ONCE_IN_A_DAY;
            //LibCUtils.googleAnalyticSend(tracker, LibCGlobalVariables.CAT_SCREEN,LibCGlobalVariables.ANALYITICS_ACTION_NOTIFICATION_CONFIG_SCREEN,LibCGlobalVariables.ONCE_IN_A_DAY_txt,0L);
        } else if (rdTwice.isChecked()) {
            userChoice = LibCGlobalVariables.TWICE_IN_A_DAY;
            //LibCUtils.googleAnalyticSend(tracker, LibCGlobalVariables.CAT_SCREEN,LibCGlobalVariables.ANALYITICS_ACTION_NOTIFICATION_CONFIG_SCREEN,LibCGlobalVariables.TWICE_IN_A_DAY_txt,0L);
        } else if (rdAsMany.isChecked()) {
            userChoice = LibCGlobalVariables.AS_MANY_TIME_IT_COMES;
            //LibCUtils.googleAnalyticSend(tracker, LibCGlobalVariables.CAT_SCREEN,LibCGlobalVariables.ANALYITICS_ACTION_NOTIFICATION_CONFIG_SCREEN,LibCGlobalVariables.AS_MANY_TIME_IT_COMES_txt,0L);
        } else if (rdNever.isChecked()) {
            userChoice = LibCGlobalVariables.NEVER;
            //LibCUtils.googleAnalyticSend(tracker, LibCGlobalVariables.CAT_SCREEN,LibCGlobalVariables.ANALYITICS_ACTION_NOTIFICATION_CONFIG_SCREEN,LibCGlobalVariables.NEVER_txt,0L);
        }

        new AsynFileOperation().execute();
		/*try
		{
			saveUserChoiceInSdCard();
		}
		catch(Exception e)
		{
			
		}
		this.setResult(RESULT_CANCELED);
		this.finish();*/
    }

    class AsynFileOperation extends AsyncTask<String, Void, String> {

        /* (non-Javadoc)
         * @see android.os.AsyncTask#onPreExecute()
         */
        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show(LibActConfigArticleNotification.this, null, getResources().getString(R.string.lib_pleasewait), true, false);
            TextView messageView = (TextView) pd.findViewById(android.R.id.message);
            messageView.setTypeface(LibCGlobalVariables.SHOW_FONT_TYPE);


        }

        @Override
        protected String doInBackground(String... arg0) {

            createSettingFolderAndFileIfDoesNotExist();

            try {
                saveUserChoiceInSdCard();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            return null;
        }

        /* (non-Javadoc)
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(String result) {
            try {
                if (pd.isShowing()) {
                    pd.dismiss();
                }

            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            LibActConfigArticleNotification.this.setResult(RESULT_OK);
            LibActConfigArticleNotification.this.finish();

        }

    }

    private void saveUserChoiceInSdCard() throws IOException {
        String strToSave = "";
        FileOutputStream fOut = null;
        OutputStreamWriter myOutWriter = null;

        if (!LibCUtils.isExternalStorageAvailableToWrite())
            return;

        strToSave = getJsonUserChoiceStringToSave(userChoice);
		
		/*File magzFile = new File(LibCGlobalVariables.SETTING_DIRECTORY_NAME, LibCGlobalVariables.MAGAZINE_NOTIFICATION_CONFIG_FILE_NAME);
		if (magzFile.exists()) {			
			try {
				fOut = new FileOutputStream(magzFile);
				myOutWriter = new OutputStreamWriter(fOut);
				myOutWriter.write(strToSave);
			
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}finally {
				myOutWriter.close();
				fOut.close();
			}
		}*/
    }

    private String getJsonUserChoiceStringToSave(int userChoice) {
        String retVal = "";
        try {
            JSONObject savedObj = new JSONObject();
            savedObj.put(LibCGlobalVariables.JSON_USER_CHOICE, userChoice);
            return savedObj.toString();
        } catch (Exception e) {

        }
        return retVal;
    }


    private void createSettingFolderAndFileIfDoesNotExist() {

      /*  if (!LibCUtils.isExternalStorageAvailableToRead()) {
            return;
        } else {*/
			/*if(!new File(LibCGlobalVariables.SETTING_DIRECTORY_NAME).exists()) {
				if(!LibCUtils.isExternalStorageAvailableToWrite())
					return ;
				File mySettingFolder = new File(LibCGlobalVariables.SETTING_DIRECTORY_NAME);
				if (!mySettingFolder.exists()) {
					mySettingFolder.mkdir();
				}
			}	
			
			if(!new File(LibCGlobalVariables.SETTING_DIRECTORY_NAME,LibCGlobalVariables.MAGAZINE_NOTIFICATION_CONFIG_FILE_NAME).exists()) {
				if(!LibCUtils.isExternalStorageAvailableToWrite())
					return ;
				try {
					File magzFile = new File(LibCGlobalVariables.SETTING_DIRECTORY_NAME, LibCGlobalVariables.MAGAZINE_NOTIFICATION_CONFIG_FILE_NAME);
					if (!magzFile.exists()) {			
						magzFile.createNewFile();
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}*/
       // }
    }

    public void goBack(View v) {
        LibActConfigArticleNotification.this.setResult(RESULT_CANCELED);
        this.finish();
    }

}
