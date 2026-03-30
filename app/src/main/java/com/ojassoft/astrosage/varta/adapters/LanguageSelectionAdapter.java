package com.ojassoft.astrosage.varta.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.ui.activity.LanguageSelectionActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;

import java.util.ArrayList;
import java.util.List;

public class LanguageSelectionAdapter extends RecyclerView.Adapter<LanguageSelectionAdapter.ViewHolder> {
    private String[] listdata;
    private int[] listdataValue;
    Context context;
    Activity activity;
    List<CardView> cardViewList = new ArrayList<>();
    List<TextView> textViewList = new ArrayList<>();
    int languageCode;

    public LanguageSelectionAdapter(Activity activity, Context context, String[] listdata, int[] myListDataValue) {
        this.listdata = listdata;
        this.listdataValue = myListDataValue;
        this.context = context;
        this.activity = activity;
        languageCode = CUtils.getLanguageCodeFromPreference(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.lay_language_adapter, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {

        cardViewList.add(holder.cardView);
        textViewList.add(holder.textView);

        holder.textView.setText(listdata[position]);

        if (languageCode == listdataValue[position]) {
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.Orangecolor));
            holder.textView.setTextColor(context.getResources().getColor(R.color.white));
        } else {
            holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.orange_light));
            holder.textView.setTextColor(context.getResources().getColor(R.color.black));
        }

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (CardView cardView : cardViewList) {
                    cardView.setCardBackgroundColor(context.getResources().getColor(R.color.orange_light));
                }
                for (TextView textView : textViewList) {
                    textView.setTextColor(context.getResources().getColor(R.color.black));
                }
                holder.cardView.setCardBackgroundColor(context.getResources().getColor(R.color.Orangecolor));
                holder.textView.setTextColor(context.getResources().getColor(R.color.white));

                goToChooseLanguage(listdataValue[holder.getAdapterPosition()], holder.getAdapterPosition());
            }
        });
    }

    private void goToChooseLanguage(int newLanguageIndexVar, int position) {
        String labell = "launch_langauge_change_" + CGlobalVariables.arrLang[position];
        //Log.e("GCMRegistration", labell + "");
        CUtils.fcmAnalyticsEvents(labell, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
        final int oldLanguageIndex = languageCode;
        saveLanguageInPreference(newLanguageIndexVar, 1);
        ((AstrosageKundliApplication) activity.getApplication()).setLanguageCode(newLanguageIndexVar);
        Log.e("SAN=ChooseLanguages3=>", ((AstrosageKundliApplication) activity.getApplication()).getLanguageCode() + "");

        new AsyncTask<Integer, Void, String>() {
            @Override
            protected String doInBackground(Integer... params) {
                try {
                    if (oldLanguageIndex != params[0]) {
                        String oldTopicName = CUtils.getTopicName(oldLanguageIndex);
                        CUtils.unSubscribeTopics("", oldTopicName, context);
                    }

                    String topicName = CUtils.getTopicName(params[0]);
                    //Log.e("topicName","topicName2="+topicName);
                    CUtils.subscribeTopics("", topicName, context);
                } catch (Exception ex) {
                    //Log.e("Exception", ex.getMessage().toString());
                }
                return null;
            }
        }.execute(newLanguageIndexVar, null, null);

        goToLoginScreen();
    }

    private void saveLanguageInPreference(int newLanguageIndex, int newScreenIndex) {
        SharedPreferences sharedPreferencesForLang = context.getSharedPreferences(CGlobalVariables.MYKUNDLI_LANGUAGE_PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor sharedPrefEditor = sharedPreferencesForLang.edit();
        sharedPrefEditor.putInt(CGlobalVariables.APP_PREFS_AppLanguage, newLanguageIndex);
        sharedPrefEditor.putInt(CGlobalVariables.APP_PREFS_AppScreen, newScreenIndex);
        sharedPrefEditor.commit();
    }

    private void goToLoginScreen() {
        CGlobalVariables.fromSetting = 0;
        activity.startActivity(new Intent(activity, LanguageSelectionActivity.class));
        activity.finishAffinity();
    }

    @Override
    public int getItemCount() {
        return listdata.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;
        public CardView cardView;

        public ViewHolder(View itemView) {
            super(itemView);
            this.textView = (TextView) itemView.findViewById(R.id.textView);
            this.cardView = (CardView) itemView.findViewById(R.id.cardView);
        }
    }
}  
