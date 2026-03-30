package com.ojassoft.astrosage.ui.fragments;


import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.DialogFragment;

import com.libojassoft.android.utils.LibCGlobalVariables;
import com.libojassoft.android.utils.LibCUtils;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.TopicDetail;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.FontUtils;

import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by ojas on 13/4/16.
 */
public class NotificationSettingFragment extends DialogFragment {

    public NotificationSettingFragment(){}

    /**
     * Variables
     */

    private Button butNotificationOk, butNotificationCancel;
    // public Typeface typeface;
    // public int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    private int userChoice = -1;
    RadioGroup langOptions;
    RadioButton rdOnce, rdTwice, rdAsMany, rdNever;
    TextView subsHeading, headingTextview;
    Activity activity;
    // AsynFileOperation asynFileOperation;
    LinearLayout containerLayout;
    LinearLayout topicSubscriptionContainer;
    TextView topicFollowTV;
    SwitchCompat topicFollowSwitch;

    ArrayList<TopicDetail> topicList;
    //TextView rdOnceTxt, rdTwiceTxt, rdAsManyTxt, rdNeverTxt;
    int radioBtnId[] = {R.id.radioOnce, R.id.radioTwice, R.id.radioAsManyTimes, R.id.radioNever};
    public static final int PERMISSION_EXTERNAL_STORAGE = 2501;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.notification_new_dialog, container, false);
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        // LANGUAGE_CODE = ((AstrosageKundliApplication) getActivity().getApplication()).getLanguageCode();//ADDED BY HEVENDRA ON 24-12-2014
        //typeface = CUtils.getRobotoFont(getActivity(), LANGUAGE_CODE, CGlobalVariables.regular);
        rdOnce = (RadioButton) view.findViewById(R.id.radioOnce);
        rdTwice = (RadioButton) view.findViewById(R.id.radioTwice);
        rdAsMany = (RadioButton) view.findViewById(R.id.radioAsManyTimes);
        rdNever = (RadioButton) view.findViewById(R.id.radioNever);

        containerLayout = (LinearLayout) view.findViewById(R.id.container);
        topicSubscriptionContainer = (LinearLayout) view.findViewById(R.id.topic_subscription_container);
        subsHeading = (TextView) view.findViewById(R.id.topic_subs_heading);
        headingTextview = ((TextView) view.findViewById(R.id.textViewHeading));
        butNotificationOk = (Button) view.findViewById(R.id.butNotificationOk);
        butNotificationCancel = (Button) view.findViewById(R.id.butNotificationCancel);
        topicFollowTV = (TextView) view.findViewById(R.id.topic_follow_tv);
        topicFollowSwitch = (SwitchCompat) view.findViewById(R.id.topic_follow_switch);

        addViewInLayout(inflater);

        initUserPreviousChoiceForNotifications();


        setTypeFaceOFView();

        if (((AstrosageKundliApplication) getActivity().getApplication()).getLanguageCode() == CGlobalVariables.ENGLISH) {
            butNotificationOk.setText(getResources().getString(R.string.ok).toUpperCase());
            butNotificationCancel.setText(getResources().getString(R.string.cancel).toUpperCase());
        }
        boolean isFollowChecked = CUtils.getBooleanData(getActivity(), CGlobalVariables.IS_FOLLOW_NOTIF_SETTING_CHECKED, true);
        if(isFollowChecked){
            topicFollowSwitch.setChecked(true);
        }else {
            topicFollowSwitch.setChecked(false);
        }

        topicFollowSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                CUtils.saveBooleanData(getActivity(), CGlobalVariables.IS_FOLLOW_NOTIF_SETTING_CHECKED, isChecked);
            }
        });

        butNotificationOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                //boolean isPermissionGranted = CUtils.isStoragePermissionGranted(activity, NotificationSettingFragment.this, BaseInputActivity.PERMISSION_EXTERNAL_STORAGE);
                //if (isPermissionGranted) {
                    saveUserChoiceInSDCard();
                //}
            }
        });

        butNotificationCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                chooseCancel();
            }
        });

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
       /* if (asynFileOperation != null && asynFileOperation.getStatus() == AsyncTask.Status.RUNNING) {
            asynFileOperation.cancel(true);
        }*/
        activity = null;
    }

    private void chooseCancel() {
        NotificationSettingFragment.this.dismiss();
    }

    private void setClickListener(final View parentView, View view, final int id) {
        /*if (view instanceof TextView) {
            ((TextView) view).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doActionOnRadioBtnSelect(parentView, id);
                }
            });
        } else if (view instanceof RadioButton) {
            ((RadioButton) view).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    doActionOnRadioBtnSelect(parentView, id);
                }
            });
        }*/
    }

 /*   private void doActionOnRadioBtnSelect(View view, int selected) {
        for (int i = 0; i < radioBtnId.length; i++) {
            if (selected == radioBtnId[i]) {
                ((RadioButton) view.findViewById(radioBtnId[i]).findViewById(R.id.radioBtn)).setChecked(true);
            } else {
                ((RadioButton) view.findViewById(radioBtnId[i]).findViewById(R.id.radioBtn)).setChecked(false);
            }
        }

    }*/

    @Override
    public void onResume() {
        super.onResume();
    }

    private void initUserPreviousChoiceForNotifications() {
        //boolean isPermissionGranted = CUtils.hasStoragePermission(activity);
        //if (isPermissionGranted) {
        userChoice = LibCUtils.getUserChoiceFromSDCard(activity);

        if (userChoice == LibCGlobalVariables.ONCE_IN_A_DAY) {
            rdOnce.setChecked(true);
        } else if (userChoice == LibCGlobalVariables.TWICE_IN_A_DAY) {
            rdTwice.setChecked(true);
        } else if (userChoice == LibCGlobalVariables.AS_MANY_TIME_IT_COMES) {
            rdAsMany.setChecked(true);
        } else if (userChoice == LibCGlobalVariables.NEVER) {
            rdNever.setChecked(true);
        }
        //}
    }

    private void saveUserChoiceInSdCard() {

        //String strToSave = "";
       /* if (!LibCUtils.isExternalStorageAvailableToWrite())
            return;*/


       /* Intent intentSaveUserChoice = new Intent(activity.getApplicationContext(), SaveUserChoiceInSdCardService.class);
        intentSaveUserChoice.putExtra("USER_CHOICE", strToSave);
        activity.startService(intentSaveUserChoice);*/
        String strToSave = getJsonUserChoiceStringToSave(userChoice);
        SharedPreferences sharedPreferences = activity.getSharedPreferences(LibCGlobalVariables.NOTIFICATION_PREF_KEY, Context.MODE_PRIVATE);
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

    private void saveUserChoiceInSDCard() {
        //Tracker tracker=null;

        if (rdOnce.isChecked()) {
            userChoice = LibCGlobalVariables.ONCE_IN_A_DAY;
            //LibCUtils.googleAnalyticSend(tracker, LibCGlobalVariables.CAT_SCREEN,LibCGlobalVariables.ANALYITICS_ACTION_NOTIFICATION_CONFIG_SCREEN,LibCGlobalVariables.ONCE_IN_A_DAY_txt,0L);
            CUtils.googleAnalyticSendWitPlayServie(activity, LibCGlobalVariables.ANALYITICS_ACTION_NOTIFICATION_CONFIG_SCREEN, LibCGlobalVariables.ONCE_IN_A_DAY_txt, LibCGlobalVariables.CAT_SCREEN);
        } else if (rdTwice.isChecked()) {
            userChoice = LibCGlobalVariables.TWICE_IN_A_DAY;
            //LibCUtils.googleAnalyticSend(tracker, LibCGlobalVariables.CAT_SCREEN,LibCGlobalVariables.ANALYITICS_ACTION_NOTIFICATION_CONFIG_SCREEN,LibCGlobalVariables.TWICE_IN_A_DAY_txt,0L);
            CUtils.googleAnalyticSendWitPlayServie(activity, LibCGlobalVariables.ANALYITICS_ACTION_NOTIFICATION_CONFIG_SCREEN, LibCGlobalVariables.TWICE_IN_A_DAY_txt, LibCGlobalVariables.CAT_SCREEN);
        } else if (rdAsMany.isChecked()) {
            userChoice = LibCGlobalVariables.AS_MANY_TIME_IT_COMES;
            //LibCUtils.googleAnalyticSend(tracker, LibCGlobalVariables.CAT_SCREEN,LibCGlobalVariables.ANALYITICS_ACTION_NOTIFICATION_CONFIG_SCREEN,LibCGlobalVariables.AS_MANY_TIME_IT_COMES_txt,0L);
            CUtils.googleAnalyticSendWitPlayServie(activity, LibCGlobalVariables.ANALYITICS_ACTION_NOTIFICATION_CONFIG_SCREEN, LibCGlobalVariables.AS_MANY_TIME_IT_COMES_txt, LibCGlobalVariables.CAT_SCREEN);
        } else if (rdNever.isChecked()) {
            userChoice = LibCGlobalVariables.NEVER;
            //LibCUtils.googleAnalyticSend(tracker, LibCGlobalVariables.CAT_SCREEN,LibCGlobalVariables.ANALYITICS_ACTION_NOTIFICATION_CONFIG_SCREEN,LibCGlobalVariables.NEVER_txt,0L);
            CUtils.googleAnalyticSendWitPlayServie(activity, LibCGlobalVariables.ANALYITICS_ACTION_NOTIFICATION_CONFIG_SCREEN, LibCGlobalVariables.NEVER_txt, LibCGlobalVariables.CAT_SCREEN);
        }
        /*asynFileOperation = new AsynFileOperation();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
            asynFileOperation.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
        } else {
            asynFileOperation.execute();
        }*/
        saveUserChoiceInSdCard();
        NotificationSettingFragment.this.dismiss();
    }

    /*  class AsynFileOperation extends AsyncTask<String, Void, String> {

     *//* (non-Javadoc)
     * @see android.os.AsyncTask#onPreExecute()
     *//*
        @Override
        protected void onPreExecute() {
            *//*pd = ProgressDialog.show(getActivity(), null, getResources().getString(R.string.lib_pleasewait), true, false);
            TextView messageView = (TextView)pd.findViewById(android.R.id.message);
			messageView.setTypeface1(typeface);*//*
        }

        @Override
        protected String doInBackground(String... arg0) {

            //createSettingFolderAndFileIfDoesNotExist();

           *//* try {
                //saveUserChoiceInSdCard();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }*//*

            return null;
        }

        *//* (non-Javadoc)
     * @see android.os.AsyncTask#onPostExecute(java.lang.Object)
     *//*
        @Override
        protected void onPostExecute(String result) {
            NotificationSettingFragment.this.dismiss();
        }

    }*/

    @Override
    public void onStart() {
        super.onStart();
        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        Dialog dialog = getDialog();
        if (dialog != null) {
            WindowManager.LayoutParams wmlp = dialog.getWindow()
                    .getAttributes();
            wmlp.width = (int) width - 40;
            wmlp.height = android.view.WindowManager.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(wmlp);
            //dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (grantResults != null && grantResults.length > 0) {

            if (requestCode == PERMISSION_EXTERNAL_STORAGE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //openFilePicker();
                //initUserPreviousChoiceForNotifications();
            } else {
                boolean showRationale = ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), permissions[0]);
                if (!showRationale) {
                    CUtils.saveBooleanData(activity, CGlobalVariables.PERMISSION_KEY_STRORAGE, true);
                }
            }
        }
    }

    //Add view when new topic is subscribed in layoutout
    private void addViewInLayout(LayoutInflater inflater) {
        //added politics by abhishek to subscribe the politics topic
        String[] topicName = {getResources().getString(R.string.cricket), getResources().getString(R.string.sharemarket), getResources().getString(R.string.bollywood), getResources().getString(R.string.new_magzine), getResources().getString(R.string.politics)};
        topicList = CUtils.loadTopics(activity);
        if (topicList == null || topicList.size() == 0) {
            topicList = CUtils.addTopicsInList(activity);
        } else {
            if (topicList.size() < topicName.length) {

                /*String[] topicId = {CGlobalVariables.TOPIC_CRICKET,CGlobalVariables.TOPIC_SHARE_MARKET,CGlobalVariables.TOPIC_BOLLYWOOD,CGlobalVariables.TOPIC_NEW_MAGAZINE};

                for(int i=topicList.size(); i<topicName.length;i++){

                    TopicDetail topicDetail = new TopicDetail();
                    topicDetail.setTopicName(topicName[i]);
                    topicDetail.setTopicId(topicId[i]);
                    topicDetail.setSubscribed(false);
                    topicList.add(topicDetail);
                }*/
                topicList = CUtils.addTopicsInList(activity);
                CUtils.storeTopics(activity, topicList);
            }
        }

        if (topicList != null && topicList.size() > 0) {
            topicSubscriptionContainer.setVisibility(View.VISIBLE);
            View v;
            for (int i = 0; i < topicList.size(); i++) {
                v = inflater.inflate(R.layout.text_switch_layout, null);
                TextView textView = (TextView) v.findViewById(R.id.topic_name);
                //textView.setText(topicList.get(i).getTopicName());

                textView.setText(topicName[i]);
                //FontUtils.changeFont(getContext(), textView, CGlobalVariables.FONTS_ROBOTO_BLACK);
                if(getActivity() instanceof BaseInputActivity){
                    textView.setTypeface(((BaseInputActivity) getActivity()).robotRegularTypeface);
                } else {
                    FontUtils.changeFont(getContext(), textView, CGlobalVariables.FONTS_ROBOTO_MEDIUM);
                }
                SwitchCompat topicSwitch = (SwitchCompat) v.findViewById(R.id.topic_switch);
                topicSwitch.setId(i);
                if (topicList.get(i).isSubscribed()) {
                    topicSwitch.setChecked(true);
                } else {
                    topicSwitch.setChecked(false);
                }
                topicSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if (CUtils.isConnectedWithInternet(activity)) {
                            if (isChecked) {
                                topicList.get(buttonView.getId()).setSubscribed(true);
                                subscribeTopic(topicList.get(buttonView.getId()).getTopicId());
                            } else {
                                topicList.get(buttonView.getId()).setSubscribed(false);
                                unSubscribeTopic(topicList.get(buttonView.getId()).getTopicId());
                            }
                            CUtils.storeTopics(activity, topicList);
                        } else {

                        }
                    }
                });
                containerLayout.addView(v);
            }
        } else {
            topicSubscriptionContainer.setVisibility(View.GONE);
        }
    }

    //settypeface of views
    private void setTypeFaceOFView() {
        if (getContext() instanceof BaseInputActivity) {
            headingTextview.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);
            subsHeading.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);
            rdOnce.setTypeface(((BaseInputActivity) getActivity()).regularTypeface);
            rdTwice.setTypeface(((BaseInputActivity) getActivity()).regularTypeface);
            rdAsMany.setTypeface(((BaseInputActivity) getActivity()).regularTypeface);
            rdNever.setTypeface(((BaseInputActivity) getActivity()).regularTypeface);
            butNotificationCancel.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);
            butNotificationOk.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);
            topicFollowTV.setTypeface(((BaseInputActivity) getActivity()).robotRegularTypeface);
        } else {
            FontUtils.changeFont(getContext(), headingTextview, CGlobalVariables.FONTS_ROBOTO_MEDIUM);
            FontUtils.changeFont(getContext(), subsHeading, CGlobalVariables.FONTS_ROBOTO_MEDIUM);
            FontUtils.changeFont(getContext(), rdOnce, CGlobalVariables.FONTS_ROBOTO_MEDIUM);
            FontUtils.changeFont(getContext(), rdTwice, CGlobalVariables.FONTS_ROBOTO_MEDIUM);
            FontUtils.changeFont(getContext(), rdAsMany, CGlobalVariables.FONTS_ROBOTO_MEDIUM);
            FontUtils.changeFont(getContext(), rdNever, CGlobalVariables.FONTS_ROBOTO_MEDIUM);
            FontUtils.changeFont(getContext(), butNotificationCancel, CGlobalVariables.FONTS_ROBOTO_MEDIUM);
            FontUtils.changeFont(getContext(), butNotificationOk, CGlobalVariables.FONTS_ROBOTO_MEDIUM);
            FontUtils.changeFont(getContext(), topicFollowTV, CGlobalVariables.FONTS_ROBOTO_MEDIUM);
        }
    }

    //subscribe the topic
    private void subscribeTopic(final String topicName) {
        final String regid = CUtils.getRegistrationId(activity);
        CUtils.saveStringData(activity, CGlobalVariables.EXTRA_TOPIC_NEED_TO_SEND_TO_GOOGLE_SERVER, "");
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    if (!regid.equals("")) {
                        CUtils.subscribeTopics(regid, topicName, activity);
                    }
                } catch (Exception ex) {
                    Log.e("Exception", ex.getMessage().toString());
                }
                return null;
            }
        }.execute(null, null, null);
    }

    //Unsbscribe the topic
    private void unSubscribeTopic(final String topicName) {
        final String regid = CUtils.getRegistrationId(getActivity().getApplicationContext());
        CUtils.saveStringData(activity, CGlobalVariables.EXTRA_TOPIC_NEED_TO_SEND_TO_GOOGLE_SERVER, "");
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                try {
                    if (!regid.equals("")) {
                        CUtils.unSubscribeTopics(regid, topicName, activity);
                    }
                } catch (Exception ex) {
                    Log.e("Exception", ex.getMessage().toString());
                }
                return null;
            }
        }.execute(null, null, null);
    }

}