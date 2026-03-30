package com.ojassoft.astrosage.ui.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.TopicDetail;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.util.ArrayList;

/**
 * Created by ojas on ६/३/१८.
 */

public class TopicSubscriptionDialog extends AstroSageParentDialogFragment {
    Activity activity;
    String url;
   /* String topicName;
    String topicId;
    boolean isSubscribed;*/


    Typeface regularTypeface;
    Typeface mediumTypeface;
    Typeface robotMediumTypeface;
    Typeface robotRegularTypeface;
    //IDialogFragmentClick callback;
    TextView topicHeading, topicDesc;
    Button yesButton, noButton, okButton;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        int LANGUAGE_CODE = ((AstrosageKundliApplication) activity.getApplication()).getLanguageCode();
        regularTypeface = CUtils.getRobotoFont(activity.getApplicationContext(), LANGUAGE_CODE, CGlobalVariables.regular);
        mediumTypeface = CUtils.getRobotoFont(activity.getApplicationContext(), LANGUAGE_CODE, CGlobalVariables.medium);
        robotMediumTypeface = CUtils.getRobotoMedium(activity.getApplicationContext());
        robotRegularTypeface = CUtils.getRobotoRegular(activity.getApplicationContext());
        url = getArguments().getString("topic_url");

        //save topic list in shared preference
        CUtils.storeTopics(activity, CUtils.addTopicsInList(activity));

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final Dialog dialog = getDialog();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.topic_subscription_layout, container);
        topicHeading = (TextView) view.findViewById(R.id.titleAstrosage_Kundali);
        topicDesc = (TextView) view.findViewById(R.id.text_view);
        yesButton = (Button) view.findViewById(R.id.yesbtn);
        noButton = (Button) view.findViewById(R.id.nobtn);
        okButton = (Button) view.findViewById(R.id.okbtn);


        if (((AstrosageKundliApplication) getActivity().getApplication()).getLanguageCode() == CGlobalVariables.ENGLISH) {
            yesButton.setText(getResources().getString(R.string.yes).toUpperCase());
            noButton.setText(getResources().getString(R.string.no).toUpperCase());
        }
        setTypefaceOfViews();
        ArrayList<TopicDetail> topicList = CUtils.loadTopics(activity);
        String topicName = "";
        String topicDesc1 = getResources().getString(R.string.topic_subscription_desc);
        String topicDesc2 = getResources().getString(R.string.already_subscribe_text);
        if (url.equals(CGlobalVariables.cricketTopicUrl) || url.equals(CGlobalVariables.cricketTopicUrls)) {
            if (!topicList.get(0).isSubscribed()) {
                setVisibalityOfButtons(View.VISIBLE, View.GONE);
                topicName = topicDesc1.replace("#", getResources().getString(R.string.cricket));
            } else {
                setVisibalityOfButtons(View.GONE, View.VISIBLE);
                topicName = topicDesc2.replace("#", getResources().getString(R.string.cricket));
            }
        } else if (url.equals(CGlobalVariables.shareMarketTopicUrl) || url.equals(CGlobalVariables.shareMarketTopicUrls)) {
            if (!topicList.get(1).isSubscribed()) {
                setVisibalityOfButtons(View.VISIBLE, View.GONE);
                topicName = topicDesc1.replace("#", getResources().getString(R.string.sharemarket));
            } else {
                setVisibalityOfButtons(View.GONE, View.VISIBLE);
                topicName = topicDesc2.replace("#", getResources().getString(R.string.sharemarket));
            }
        } else if (url.equals(CGlobalVariables.bollywoodTopicUrl) || url.equals(CGlobalVariables.bollywoodTopicUrls)) {
            if (!topicList.get(2).isSubscribed()) {
                setVisibalityOfButtons(View.VISIBLE, View.GONE);
                topicName = topicDesc1.replace("#", getResources().getString(R.string.bollywood));
            } else {
                setVisibalityOfButtons(View.GONE, View.VISIBLE);
                topicName = topicDesc2.replace("#", getResources().getString(R.string.bollywood));
            }
        } else if (url.equals(CGlobalVariables.newMagazineTopicUrl) || url.equals(CGlobalVariables.newMagazineTopicUrl)) {
            if (!topicList.get(3).isSubscribed()) {
                setVisibalityOfButtons(View.VISIBLE, View.GONE);
                topicName = topicDesc1.replace("#", getResources().getString(R.string.new_magzine));
            } else {
                setVisibalityOfButtons(View.GONE, View.VISIBLE);
                topicName = topicDesc2.replace("#", getResources().getString(R.string.new_magzine));
            }
        }
        else if (url.equals(CGlobalVariables.politicsTopicUrl) || url.equals(CGlobalVariables.politicsTopicUrls)) {
            if (!topicList.get(4).isSubscribed()) {
                setVisibalityOfButtons(View.VISIBLE, View.GONE);
                topicName = topicDesc1.replace("#", getResources().getString(R.string.politics));
            } else {
                setVisibalityOfButtons(View.GONE, View.VISIBLE);
                topicName = topicDesc2.replace("#", getResources().getString(R.string.politics));
            }
        }
        topicDesc.setText(topicName);
        yesButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (CUtils.isConnectedWithInternet(activity)) {
                    //subscribe for topics
                    String topicId = "";
                    ArrayList<TopicDetail> topicList = CUtils.loadTopics(activity);
                    if (url.equals(CGlobalVariables.cricketTopicUrl) || url.equals(CGlobalVariables.cricketTopicUrls)) {
                        topicList.get(0).setSubscribed(true);
                        topicId = topicList.get(0).getTopicId();
                    } else if (url.equals(CGlobalVariables.shareMarketTopicUrl) || url.equals(CGlobalVariables.shareMarketTopicUrls)) {
                        topicList.get(1).setSubscribed(true);
                        topicId = topicList.get(1).getTopicId();
                    } else if (url.equals(CGlobalVariables.bollywoodTopicUrl) || url.equals(CGlobalVariables.bollywoodTopicUrls)) {
                        topicList.get(2).setSubscribed(true);
                        topicId = topicList.get(2).getTopicId();
                    } else if (url.equals(CGlobalVariables.newMagazineTopicUrl) || url.equals(CGlobalVariables.newMagazineTopicUrls)) {
                        topicList.get(3).setSubscribed(true);
                        topicId = topicList.get(3).getTopicId();
                    }else if (url.equals(CGlobalVariables.politicsTopicUrl) || url.equals(CGlobalVariables.politicsTopicUrls)) {
                        topicList.get(4).setSubscribed(true);
                        topicId = topicList.get(4).getTopicId();
                    }

                    CUtils.storeTopics(activity, topicList);
                    subscribeTopic(topicId);
                }
                dialog.dismiss();
            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        okButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        return view;

    }

    public static TopicSubscriptionDialog getInstance(String url) {
        TopicSubscriptionDialog topicSubscriptionDialog = new TopicSubscriptionDialog();
        Bundle bundle = new Bundle();
        bundle.putString("topic_url", url);
       /* bundle.putString("topic_name", topicName);
        bundle.putString("topic_id", topicId);
        bundle.putBoolean("is_subscribed", isSubscribed);*/
        topicSubscriptionDialog.setArguments(bundle);
        return topicSubscriptionDialog;
    }

    //subscribe topics
    private void subscribeTopic(final String topicName) {
        final String regid = CUtils.getRegistrationId(activity.getApplicationContext());
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

    //set typeface of views
    private void setTypefaceOfViews() {
        topicHeading.setTypeface(mediumTypeface);
        topicDesc.setTypeface(regularTypeface);
        yesButton.setTypeface(mediumTypeface);
        noButton.setTypeface(mediumTypeface);
        okButton.setTypeface(mediumTypeface);
    }

    private void setVisibalityOfButtons(int val1, int val2) {
        yesButton.setVisibility(val1);
        noButton.setVisibility(val1);
        okButton.setVisibility(val2);
    }
}

