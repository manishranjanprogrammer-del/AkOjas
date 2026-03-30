package com.ojassoft.astrosage.ui.fragments;

import org.json.JSONObject;

import android.app.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import com.libojassoft.android.utils.LibCGlobalVariables;
import com.libojassoft.android.utils.LibCUtils;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

public class WizardNotificationSetting extends Fragment {
    IWizardNotificationSettingFragmentInterface iWizardNotificationSettingFragmentInterface;
    TextView nextButton;
    RadioButton rdOnce, rdTwice, rdAsMany, rdNever;
    TextView rdOnceTxt, rdTwiceTxt, rdAsManyTxt, rdNeverTxt;
    private int userChoice = -1;
    //AsynFileOperation asynFileOperation;
    //	private ProgressDialog pd;
    int callerScreen;
    public static final int ActNotificationScreen = 1;
    int radioBtnId[] = {R.id.radioOnce, R.id.radioTwice, R.id.radioAsManyTimes, R.id.radioNever};

    public static WizardNotificationSetting newInstance(int callerScreen) {
        WizardNotificationSetting wizardNotificationSetting = new WizardNotificationSetting();

        Bundle args = new Bundle();
        args.putInt("callerScreen", callerScreen);
        wizardNotificationSetting.setArguments(args);

        return wizardNotificationSetting;
    }

    public WizardNotificationSetting() {
        setRetainInstance(true);

    }

    public int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    public Typeface typeface;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //LANGUAGE_CODE = CUtils.getLanguageCodeFromPreference(getActivity());
        LANGUAGE_CODE = ((AstrosageKundliApplication) getActivity().getApplication()).getLanguageCode();//ADDED BY HEVENDRA ON 24-12-2014
        typeface = CUtils.getRobotoFont(getActivity(), LANGUAGE_CODE, CGlobalVariables.regular);
        callerScreen = getArguments().getInt("callerScreen", -1);
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.lay_wizard_notification_setting, container, false);
        TextView heading = (TextView) view.findViewById(R.id.textViewHeading);
        heading.setTypeface(typeface);
        rdOnce = (RadioButton) view.findViewById(R.id.radioOnce).findViewById(R.id.radioBtn);
        rdTwice = (RadioButton) view.findViewById(R.id.radioTwice).findViewById(R.id.radioBtn);
        rdAsMany = (RadioButton) view.findViewById(R.id.radioAsManyTimes).findViewById(R.id.radioBtn);
        rdNever = (RadioButton) view.findViewById(R.id.radioNever).findViewById(R.id.radioBtn);

        rdOnceTxt = (TextView) view.findViewById(R.id.radioOnce).findViewById(R.id.txt);
        rdTwiceTxt = (TextView) view.findViewById(R.id.radioTwice).findViewById(R.id.txt);
        rdAsManyTxt = (TextView) view.findViewById(R.id.radioAsManyTimes).findViewById(R.id.txt);
        rdNeverTxt = (TextView) view.findViewById(R.id.radioNever).findViewById(R.id.txt);

        setClickListener(view, rdOnce, R.id.radioOnce);
        setClickListener(view, rdTwice, R.id.radioTwice);
        setClickListener(view, rdAsMany, R.id.radioAsManyTimes);
        setClickListener(view, rdNever, R.id.radioNever);
        setClickListener(view, rdOnceTxt, R.id.radioOnce);
        setClickListener(view, rdTwiceTxt, R.id.radioTwice);
        setClickListener(view, rdAsManyTxt, R.id.radioAsManyTimes);
        setClickListener(view, rdNeverTxt, R.id.radioNever);
        rdOnceTxt.setText(R.string.once_in_a_day);
        rdTwiceTxt.setText(R.string.twice_in_a_day);
        rdAsManyTxt.setText(R.string.as_many_time_as_it_comes);
        rdNeverTxt.setText(R.string.title_never);

        rdOnceTxt.setTypeface(typeface);
        rdTwiceTxt.setTypeface(typeface);
        rdAsManyTxt.setTypeface(typeface);
        rdNeverTxt.setTypeface(typeface);

        nextButton = (TextView) view.findViewById(R.id.buttonNext);
        nextButton.setTypeface(typeface);

        if (callerScreen == ActNotificationScreen) {

            if (((AstrosageKundliApplication) getActivity().getApplication())
                    .getLanguageCode() == CGlobalVariables.ENGLISH) {
                nextButton.setText(getResources().getString(R.string.show_match).toUpperCase());
            } else {
                nextButton.setText(R.string.save);
            }
        }

        if (((AstrosageKundliApplication) getActivity().getApplication())
                .getLanguageCode() == CGlobalVariables.ENGLISH) {
            nextButton.setText(getResources().getString(R.string.button_next).toUpperCase());
        }

        initUserPreviousChoiceForNotifications();
        nextButton.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                saveUserChoiceInSDCard();
            }
        });
        return view;
    }


    /* (non-Javadoc)
     * @see android.support.v4.app.Fragment#onResume()
     */
    @Override
    public void onResume() {
        super.onResume();
    }

    private void initUserPreviousChoiceForNotifications() {
        userChoice = LibCUtils.getUserChoiceFromSDCard(getActivity());

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


    private void saveUserChoiceInSDCard() {
        //Tracker tracker=null;
        if (rdOnce.isChecked()) {
            userChoice = LibCGlobalVariables.ONCE_IN_A_DAY;
            //LibCUtils.googleAnalyticSend(tracker, LibCGlobalVariables.CAT_SCREEN,LibCGlobalVariables.ANALYITICS_ACTION_NOTIFICATION_CONFIG_SCREEN,LibCGlobalVariables.ONCE_IN_A_DAY_txt,0L);
            CUtils.googleAnalyticSendWitPlayServie(getActivity(), LibCGlobalVariables.ANALYITICS_ACTION_NOTIFICATION_CONFIG_SCREEN, LibCGlobalVariables.ONCE_IN_A_DAY_txt, LibCGlobalVariables.CAT_SCREEN);
        } else if (rdTwice.isChecked()) {
            userChoice = LibCGlobalVariables.TWICE_IN_A_DAY;
            //LibCUtils.googleAnalyticSend(tracker, LibCGlobalVariables.CAT_SCREEN,LibCGlobalVariables.ANALYITICS_ACTION_NOTIFICATION_CONFIG_SCREEN,LibCGlobalVariables.TWICE_IN_A_DAY_txt,0L);
            CUtils.googleAnalyticSendWitPlayServie(getActivity(), LibCGlobalVariables.ANALYITICS_ACTION_NOTIFICATION_CONFIG_SCREEN, LibCGlobalVariables.TWICE_IN_A_DAY_txt, LibCGlobalVariables.CAT_SCREEN);
        } else if (rdAsMany.isChecked()) {
            userChoice = LibCGlobalVariables.AS_MANY_TIME_IT_COMES;
            //LibCUtils.googleAnalyticSend(tracker, LibCGlobalVariables.CAT_SCREEN,LibCGlobalVariables.ANALYITICS_ACTION_NOTIFICATION_CONFIG_SCREEN,LibCGlobalVariables.AS_MANY_TIME_IT_COMES_txt,0L);
            CUtils.googleAnalyticSendWitPlayServie(getActivity(), LibCGlobalVariables.ANALYITICS_ACTION_NOTIFICATION_CONFIG_SCREEN, LibCGlobalVariables.AS_MANY_TIME_IT_COMES_txt, LibCGlobalVariables.CAT_SCREEN);
        } else if (rdNever.isChecked()) {
            userChoice = LibCGlobalVariables.NEVER;
            //LibCUtils.googleAnalyticSend(tracker, LibCGlobalVariables.CAT_SCREEN,LibCGlobalVariables.ANALYITICS_ACTION_NOTIFICATION_CONFIG_SCREEN,LibCGlobalVariables.NEVER_txt,0L);
            CUtils.googleAnalyticSendWitPlayServie(getActivity(), LibCGlobalVariables.ANALYITICS_ACTION_NOTIFICATION_CONFIG_SCREEN, LibCGlobalVariables.NEVER_txt, LibCGlobalVariables.CAT_SCREEN);
        }

     /*   asynFileOperation = new AsynFileOperation();
        asynFileOperation.execute();*/
        saveUserChoiceInSdCard();
    }

    class AsynFileOperation extends AsyncTask<String, Void, String> {

        /* (non-Javadoc)
         * @see android.os.AsyncTask#onPreExecute()
         */
        @Override
        protected void onPreExecute() {
            /*pd = ProgressDialog.show(getActivity(), null, getResources().getString(R.string.lib_pleasewait), true, false);
            TextView messageView = (TextView)pd.findViewById(android.R.id.message);
			messageView.setTypeface(typeface);*/
        }

        @Override
        protected String doInBackground(String... arg0) {

            //createSettingFolderAndFileIfDoesNotExist();

           /* try {
                saveUserChoiceInSdCard();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }*/

            return null;
        }

        /* (non-Javadoc)
         * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
         */
        @Override
        protected void onPostExecute(String result) {
            /*try {
                if(pd.isShowing()) {
					pd.dismiss();
				}
					
			} catch (Exception e) {
				e.printStackTrace();
			}*/
            iWizardNotificationSettingFragmentInterface.clickedNotificationSettingNextButton();
        }

    }

    private void saveUserChoiceInSdCard()  {
        /*String strToSave = "";
        FileOutputStream fOut = null;
		OutputStreamWriter myOutWriter = null;
		
		if(!LibCUtils.isExternalStorageAvailableToWrite())
			return;
		
		strToSave = getJsonUserChoiceStringToSave(userChoice);
		
		File magzFile = new File(LibCGlobalVariables.SETTING_DIRECTORY_NAME, LibCGlobalVariables.MAGAZINE_NOTIFICATION_CONFIG_FILE_NAME);
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
        String strToSave = getJsonUserChoiceStringToSave(userChoice);
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences(LibCGlobalVariables.NOTIFICATION_PREF_KEY, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(LibCGlobalVariables.NOTIFICATION_CHOICE_PREF_KEY, strToSave);
        editor.commit();


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


   /* private void createSettingFolderAndFileIfDoesNotExist() {

        if (!LibCUtils.isExternalStorageAvailableToRead()) {
            return;
        } else {
            if (!new File(LibCGlobalVariables.SETTING_DIRECTORY_NAME).exists()) {
                if (!LibCUtils.isExternalStorageAvailableToWrite())
                    return;
                File mySettingFolder = new File(LibCGlobalVariables.SETTING_DIRECTORY_NAME);
                if (!mySettingFolder.exists()) {
                    mySettingFolder.mkdir();
                }
            }

            if (!new File(LibCGlobalVariables.SETTING_DIRECTORY_NAME, LibCGlobalVariables.MAGAZINE_NOTIFICATION_CONFIG_FILE_NAME).exists()) {
                if (!LibCUtils.isExternalStorageAvailableToWrite())
                    return;
                try {
                    File magzFile = new File(LibCGlobalVariables.SETTING_DIRECTORY_NAME, LibCGlobalVariables.MAGAZINE_NOTIFICATION_CONFIG_FILE_NAME);
                    if (!magzFile.exists()) {
                        magzFile.createNewFile();
                    }
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }*/

    private void setClickListener(final View parentView, View view, final int id) {
        if (view instanceof TextView) {
            ((TextView) view).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    doActionOnRadioBtnSelect(parentView, id);
                }
            });
        } else if (view instanceof RadioButton) {
            ((RadioButton) view).setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    doActionOnRadioBtnSelect(parentView, id);
                }
            });
        }
    }

    private void doActionOnRadioBtnSelect(View view, int selected) {
        for (int i = 0; i < radioBtnId.length; i++) {
            if (selected == radioBtnId[i]) {
                ((RadioButton) view.findViewById(radioBtnId[i]).findViewById(R.id.radioBtn)).setChecked(true);
            } else {
                ((RadioButton) view.findViewById(radioBtnId[i]).findViewById(R.id.radioBtn)).setChecked(false);
            }
        }

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        iWizardNotificationSettingFragmentInterface = (IWizardNotificationSettingFragmentInterface) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
       /* if (asynFileOperation != null && asynFileOperation.getStatus() == AsyncTask.Status.RUNNING) {
            asynFileOperation.cancel(true);
        }*/
        iWizardNotificationSettingFragmentInterface = null;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    public interface IWizardNotificationSettingFragmentInterface {
        public void clickedNotificationSettingNextButton();
    }
}