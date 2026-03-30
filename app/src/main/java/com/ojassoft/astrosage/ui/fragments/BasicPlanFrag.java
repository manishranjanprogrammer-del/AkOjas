package com.ojassoft.astrosage.ui.fragments;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tagmanager.Container;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.CloudPlanData;
import com.ojassoft.astrosage.customadapters.PlanRecyclerViewAdapter;
import com.ojassoft.astrosage.jinterface.IPurchasePlan;
import com.ojassoft.astrosage.tagmanager.ContainerHolderSingleton;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.PurchasePlanHomeActivity;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.PlanLocalizationUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class BasicPlanFrag extends Fragment {

    public int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    public Typeface regularTypeface, midumTypeface;
    private TextView textViewAstroFeature, textViewExperiance;
    private TextView buyTV;
    IPurchasePlan iPurchasePlan;
    String PlanPreferencename;
    String data_BasicPlanAstrologicalFeaturesEnglish = "";
    String data_BasicPlanAstrologicalFeaturesHindi = "";
    String data_BasicPlanAstrologicalFeaturesTamil = "";
    String data_BasicPlanAstrologicalFeaturesBangali = "";
    String data_BasicPlanAstrologicalFeaturesMarathi = "";
    String data_BasicPlanAstrologicalFeaturesTelugu = "";
    String data_BasicPlanAstrologicalFeaturesKannad = "";
    String data_BasicPlanAstrologicalFeaturesGujrati = "";
    String data_BasicPlanAstrologicalFeaturesMalayalam = "";
    String data_BasicPlanAstrologicalFeaturesOdia = "";
    String data_BasicPlanAstrologicalFeaturesAssamese = "";
    Activity activity;
    PlanRecyclerViewAdapter planRecyclerViewAdapter;
    private List<CloudPlanData> planDataList;

    public BasicPlanFrag() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LANGUAGE_CODE = ((AstrosageKundliApplication) activity.getApplication()).getLanguageCode();
        regularTypeface = CUtils.getRobotoFont(activity, LANGUAGE_CODE, CGlobalVariables.regular);
        midumTypeface = CUtils.getRobotoFont(activity, LANGUAGE_CODE, CGlobalVariables.medium);
        planDataList = new ArrayList<>();
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        iPurchasePlan = (IPurchasePlan) activity;
        this.activity = activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        iPurchasePlan = null;
        this.activity = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.lay_basic_plan, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.plan_rv);
        planRecyclerViewAdapter = new PlanRecyclerViewAdapter(this,activity, planDataList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(planRecyclerViewAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setFocusable(false);
        //prepareCloudPlanData();
        textViewAstroFeature = (TextView) view.findViewById(R.id.Astrofeatureheading);
        textViewAstroFeature.setTypeface(midumTypeface);
        textViewExperiance = (TextView) view.findViewById(R.id.Astrofeaturesubheading);
        textViewExperiance.setTypeface(regularTypeface);

        textViewAstroFeature.setVisibility(View.GONE);
        textViewExperiance.setVisibility(View.GONE);

        //getDataFromGTMCointainer();
        setDataInList();

        buyTV = (TextView) view.findViewById(R.id.buy_tv);
        buyTV.setTypeface(midumTypeface);
        if (((AstrosageKundliApplication) activity.getApplication()).getLanguageCode() == CGlobalVariables.ENGLISH) {
            buyTV.setText(getResources().getString(R.string.select_basic));
        }

        if (LANGUAGE_CODE == 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
                //buyTV.setAllCaps(true);
            }
        }

        PlanPreferencename = CUtils.GetPlaninPreference(activity);

        if (PlanPreferencename != null && !PlanPreferencename.equals("")) {
            buyTV.setEnabled(false);
        } else {
            buyTV.setEnabled(true);
        }

        buyTV.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                CUtils.fcmAnalyticsEvents(CGlobalVariables.PLAN_UPGRADE_FOR_BASIC, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK,"");
                CUtils.SavePlanForPopupPreference(activity, true);
                iPurchasePlan.selectedPlan(PurchasePlanHomeActivity.BASIC_PLAN,null);
                // activity.finish();
            }
        });
        return view;
    }

    private void setDataInList() {
        String data = PlanLocalizationUtil.getBasicPlanFeatureJson(LANGUAGE_CODE);
        parseData(data);
    }

    //Parse Tag Manager Data
    private void parseData(String jsonString) {
        try {
            HashMap<String, Integer> icons = ((PurchasePlanHomeActivity) activity).getIcons();
            JSONObject object = new JSONObject(jsonString);
            JSONArray jsonArray = object.getJSONArray("plans");
            JSONObject innerJsonObject;
            CloudPlanData cloudPlanData;
            for (int i = 0; i < jsonArray.length(); i++) {
                cloudPlanData = new CloudPlanData();
                innerJsonObject = jsonArray.getJSONObject(i);
                cloudPlanData.setHeading(innerJsonObject.getString("heading"));
                cloudPlanData.setDesc(innerJsonObject.getString("desc"));
                cloudPlanData.setImageID(icons.get(innerJsonObject.getString("icon")));
                //cloudPlanData.setImageBackgroundID(R.drawable.basic_solid_circle); commit by tushar
                //cloudPlanData.setImageBackgroundID(R.drawable.basic_solid_circle);
                planDataList.add(cloudPlanData);
            }
            planRecyclerViewAdapter.notifyDataSetChanged();
        } catch (Exception e) {

        }
    }
}
