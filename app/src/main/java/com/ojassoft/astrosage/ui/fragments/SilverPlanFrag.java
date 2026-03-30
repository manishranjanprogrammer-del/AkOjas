package com.ojassoft.astrosage.ui.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tagmanager.Container;
import com.google.android.gms.tagmanager.ContainerHolder;
import com.google.android.gms.tagmanager.TagManager;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.CloudPlanData;
import com.ojassoft.astrosage.customadapters.PlanRecyclerViewAdapter;
import com.ojassoft.astrosage.jinterface.IPurchasePlan;
import com.ojassoft.astrosage.tagmanager.ContainerHolderSingleton;
import com.ojassoft.astrosage.tagmanager.ContainerLoadedCallback;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.PurchasePlanHomeActivity;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;


public class SilverPlanFrag extends Fragment {


    public int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    public Typeface regularTypeface, mediumTypeface;
    private TextView textViewAstroFeature, textViewExperiance;

    String questionCount;
    private TextView buyTV;
    private IPurchasePlan iPurchasePlan;
    Activity activity;

    String data_SliverPlanAstrologicalFeaturesEnglish = "";
    String data_SliverPlanAstrologicalFeaturesHindi = "";
    String data_SliverPlanAstrologicalFeaturesTamil = "";
    String data_SliverPlanAstrologicalFeaturesBangali = "";
    String data_SliverPlanAstrologicalFeaturesMarathi = "";
    String data_SliverPlanAstrologicalFeaturesTelugu = "";
    String data_SliverPlanAstrologicalFeaturesKannad = "";
    String data_SliverPlanAstrologicalFeaturesGujrati = "";
    String data_SliverPlanAstrologicalFeaturesMalayalam = "";
    private int initializeTagManagerCount = 0;
    PlanRecyclerViewAdapter planRecyclerViewAdapter;
    private List<CloudPlanData> planDataList;
    private boolean isButtonEnable;
    private LinearLayout buyDisable;

    public SilverPlanFrag() {

    }

    @SuppressLint("ValidFragment")
    public SilverPlanFrag(boolean isButtonEnable) {
        this.isButtonEnable = isButtonEnable;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LANGUAGE_CODE = ((AstrosageKundliApplication) getActivity().getApplication()).getLanguageCode();
        regularTypeface = CUtils.getRobotoFont(getActivity(), LANGUAGE_CODE, CGlobalVariables.regular);
        mediumTypeface = CUtils.getRobotoFont(getActivity(), LANGUAGE_CODE, CGlobalVariables.medium);
        planDataList = new ArrayList<>();
    }

    @Override
    public void onAttach(Activity activity) {
        // TODO Auto-generated method stub
        super.onAttach(activity);
        this.activity = activity;
        iPurchasePlan = (IPurchasePlan) activity;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
        iPurchasePlan = null;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.lay_silver_plan, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.plan_rv);
        planRecyclerViewAdapter = new PlanRecyclerViewAdapter(this,activity, planDataList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(activity);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(planRecyclerViewAdapter);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setFocusable(false);
        questionCount = CUtils.getStringData(getContext(),CGlobalVariables.SILVER_PLAN_QUES_COUNT_KEY,"100");

        textViewAstroFeature = (TextView) view.findViewById(R.id.Astrofeatureheading);
        textViewAstroFeature.setTypeface(mediumTypeface);
        textViewExperiance = (TextView) view.findViewById(R.id.Astrofeaturesubheading);
        textViewExperiance.setTypeface(regularTypeface);

        textViewAstroFeature.setVisibility(View.GONE);
        textViewExperiance.setVisibility(View.GONE);

        final LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.llPlanCointainer);
        getDataFromGTMCointainer(linearLayout);

        buyTV = (TextView) view.findViewById(R.id.buy_tv);
        buyDisable =  view.findViewById(R.id.buy_disable);
        buyTV.setTypeface(mediumTypeface);

        //added by Ankit on 12-9-2019 to set button enable or disable
        if(isButtonEnable){
            buyTV.setEnabled(true);
            buyDisable.setVisibility(View.GONE);
        }else{
            buyTV.setEnabled(false);
            buyDisable.setVisibility(View.VISIBLE);
        }

        buyTV.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                CUtils.fcmAnalyticsEvents(CGlobalVariables.PLAN_UPGRADE_FOR_SILVER, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK,"");
                if (((PurchasePlanHomeActivity) getActivity()).enableMonthlySubscriptionValue) {
                    openPlanSelectDialog();
                } else {
                    // 1 for yearly plan subscription
                    getSilverPlan(1);
                }
            }
        });
        if (((AstrosageKundliApplication) getActivity().getApplication())
                .getLanguageCode() == CGlobalVariables.ENGLISH) {

            String buyText = "";

            if (((PurchasePlanHomeActivity) getActivity()).enableMonthlySubscriptionValue) {
                buyText = getResources().getString(R.string.buy_silver_plan).toUpperCase();
                //buyText = getResources().getString(R.string.join_free_month).toUpperCase();
            } else {
                buyText = getResources().getString(R.string.buy_silver_plan).toUpperCase() + " (" + ((PurchasePlanHomeActivity) getActivity()).silverPlanPriceYear + ")";
            }

            buyTV.setText(buyText);
        } else {
            String buyText = "";

            if (((PurchasePlanHomeActivity) getActivity()).enableMonthlySubscriptionValue) {
                //buyText = getResources().getString(R.string.buy_silver_plan);
                buyText = getResources().getString(R.string.join_free_month);
            } else {
                buyText = getResources().getString(R.string.buy_silver_plan) + " (" + ((PurchasePlanHomeActivity) getActivity()).silverPlanPriceYear + ")";
            }

            buyTV.setText(buyText);
        }

        String ScreenId = "SilverPlanFrag";
        //To show expiry date
        String expiryDate = CUtils.isExpiryDateAvailableToShow(activity, ScreenId);
        LinearLayout expiryDateContainer = (LinearLayout) view.findViewById(R.id.expiry_date_container);
        if (expiryDate != null) {
            TextView tvPlanExpiryDate = (TextView) view.findViewById(R.id.tvPlanExpiryDate);
            tvPlanExpiryDate.setText(expiryDate);
            tvPlanExpiryDate.setTypeface(regularTypeface);
            expiryDateContainer.setVisibility(View.VISIBLE);

        } else {
            expiryDateContainer.setVisibility(View.GONE);

        }
        return view;
    }

    private void openPlanSelectDialog() {
        FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment prev = fragmentManager.findFragmentByTag("HOME_PLANACTIVATOR");
        if (prev != null) {
            fragmentTransaction.remove(prev);
        }
        fragmentTransaction.addToBackStack(null);
        ChoosePlanFragmentDailogSilver choosePlanFragmentDailog = new ChoosePlanFragmentDailogSilver();
        choosePlanFragmentDailog.show(fragmentManager, "HOME_PLANACTIVATOR");
        fragmentTransaction.commit();
    }

    public void getSilverPlan(int planType) {
        if (planType == 0) {
            CUtils.SavePlanForPopupPreference(getActivity(), true);
            iPurchasePlan
                    .selectedPlan(PurchasePlanHomeActivity.SIVLER_PLAN_MONTHLY,null);
        } else if (planType == 1) {
            CUtils.SavePlanForPopupPreference(getActivity(), true);
            iPurchasePlan
                    .selectedPlan(PurchasePlanHomeActivity.SIVLER_PLAN,null);
        }
    }

    /**
     * @author amit Rautela
     * @desc This method is used to get the data from cointainer
     */
    private void getDataFromGTMCointainer(final LinearLayout linearLayout) {
        try {
            if (ContainerHolderSingleton.getContainerHolder() != null) {
                new AsyncTask<Void, Void, String>() {
                    @Override
                    protected String doInBackground(Void... params) {
                        try {
                            Container container = ContainerHolderSingleton.getContainerHolder().getContainer();
                            if (container != null) {
                                data_SliverPlanAstrologicalFeaturesEnglish = container.getString(CGlobalVariables.key_SliverPlanAstrologicalFeaturesEnglishUpdate);
                                data_SliverPlanAstrologicalFeaturesHindi = container.getString(CGlobalVariables.key_SliverPlanAstrologicalFeaturesHindiUpdate);
                                data_SliverPlanAstrologicalFeaturesTamil = container.getString(CGlobalVariables.key_SliverPlanAstrologicalFeaturesTamilUpdate);
                                data_SliverPlanAstrologicalFeaturesBangali = container.getString(CGlobalVariables.key_SliverPlanAstrologicalFeaturesBangaliUpdate);
                                data_SliverPlanAstrologicalFeaturesMarathi = container.getString(CGlobalVariables.key_SliverPlanAstrologicalFeaturesMarathiUpdate);
                                data_SliverPlanAstrologicalFeaturesTelugu = container.getString(CGlobalVariables.key_SliverPlanAstrologicalFeaturesTeluguUpdate);
                                data_SliverPlanAstrologicalFeaturesKannad = container.getString(CGlobalVariables.key_SliverPlanAstrologicalFeaturesKannadUpdate);
                                data_SliverPlanAstrologicalFeaturesGujrati = container.getString(CGlobalVariables.key_SliverPlanAstrologicalFeaturesGujratiUpdate);
                                data_SliverPlanAstrologicalFeaturesMalayalam = container.getString(CGlobalVariables.key_SliverPlanAstrologicalFeaturesMalayalamUpdate);
                                questionCount = container.getString(CGlobalVariables.key_SliverPlanQuestionCount);
                                CUtils.saveStringData(getActivity(), CGlobalVariables.SILVER_PLAN_QUES_COUNT_KEY, questionCount);
                                CUtils.saveStringData(getActivity(),CGlobalVariables.GOLD_PLAN_QUES_COUNT_KEY,container.getString(CGlobalVariables.key_GoldPlanQuestionCount));
                                CUtils.saveStringData(getActivity(),CGlobalVariables.DHRUV_PLAN_QUES_COUNT_KEY,container.getString(CGlobalVariables.key_DhruvPlanQuestionCount));
                            }
                        } catch (Exception e){
                            //
                        }
                        return null;
                    }

                    @Override
                    public void onPostExecute(String result) {
                        setDataInList();
                    }
                }.execute(null, null, null);

            } else {
                if (initializeTagManagerCount == 0) {
                    initializeTagManagerCount = 1;
                    setTagManager(linearLayout);
                }
            }
        } catch (Exception ex) {
        }
    }

    private void setDataInList() {

        String data = "";
        if (LANGUAGE_CODE == CGlobalVariables.ENGLISH) {
            data = data_SliverPlanAstrologicalFeaturesEnglish;
        } else if (LANGUAGE_CODE == CGlobalVariables.HINDI) {
            data = data_SliverPlanAstrologicalFeaturesHindi;
        } else if (LANGUAGE_CODE == CGlobalVariables.TAMIL) {
            data = data_SliverPlanAstrologicalFeaturesTamil;
        } else if (LANGUAGE_CODE == CGlobalVariables.BANGALI) {
            data = data_SliverPlanAstrologicalFeaturesBangali;
        } else if (LANGUAGE_CODE == CGlobalVariables.MARATHI) {
            data = data_SliverPlanAstrologicalFeaturesMarathi;
        } else if (LANGUAGE_CODE == CGlobalVariables.TELUGU) {
            data = data_SliverPlanAstrologicalFeaturesTelugu;
        } else if (LANGUAGE_CODE == CGlobalVariables.KANNADA) {
            data = data_SliverPlanAstrologicalFeaturesKannad;
        } else if (LANGUAGE_CODE == CGlobalVariables.GUJARATI) {
            data = data_SliverPlanAstrologicalFeaturesGujrati;
        } else if (LANGUAGE_CODE == CGlobalVariables.MALAYALAM) {
            data = data_SliverPlanAstrologicalFeaturesMalayalam;
        } else {
            data = data_SliverPlanAstrologicalFeaturesEnglish;
        }
        parseData(data);

    }


    /**
     * @author amit Rautela
     * @desc : This method is used to connect with tag manager
     */
    private void setTagManager(final LinearLayout linearLayout) {

        try {
            TagManager tagManager = TagManager.getInstance(getActivity());

            // Modify the log level of the logger to print out not only
            // warning and error messages, but also verbose, debug, info messages.
            tagManager.setVerboseLoggingEnabled(true);

            PendingResult<ContainerHolder> pending =
                    tagManager.loadContainerPreferNonDefault(CGlobalVariables.CONTAINER_ID,
                            R.raw.binary_file);


            pending.setResultCallback(new ResultCallback<ContainerHolder>() {
                @Override
                public void onResult(ContainerHolder containerHolder) {
                    try {
                        ContainerHolderSingleton.setContainerHolder(containerHolder);
                        Container container = containerHolder.getContainer();
                        if (!containerHolder.getStatus().isSuccess()) {
                            // //Log.e("CuteAnimals", "failure loading container");
                            // displayErrorToUser(R.string.server_error_msg);
                            return;
                        }
                        ContainerHolderSingleton.setContainerHolder(containerHolder);
                        ContainerLoadedCallback.registerCallbacksForContainer(container);
                        containerHolder.setContainerAvailableListener(new ContainerLoadedCallback());
                        getDataFromGTMCointainer(linearLayout);
                    } catch (Exception ex) {
                    }
                }
            }, CGlobalVariables.TIMEOUT_FOR_CONTAINER_OPEN_MILLISECONDS, TimeUnit.MILLISECONDS);
        } catch (Exception ex) {
        }
    }

    //Parse Tag Manager Data
    private void parseData(String jsonString) {
        try {
            boolean isFreeQuestionInfoAdded = false;
            HashMap<String, Integer> icons = ((PurchasePlanHomeActivity) activity).getIcons();
            JSONObject object = new JSONObject(jsonString);
            JSONArray jsonArray = object.getJSONArray("plans");

            if(jsonArray.length()<6){
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("heading",getString(R.string.free_questions));

                jsonObject.put("desc",getString(R.string.free_questions_from_kundli_ai,questionCount));
                jsonObject.put("icon","plan_all_features_icon");
                jsonArray.put(jsonObject);
                isFreeQuestionInfoAdded = true;
            }

            JSONObject innerJsonObject;
            CloudPlanData cloudPlanData;
            for (int i = 0; i < jsonArray.length(); i++) {
                cloudPlanData = new CloudPlanData();
                innerJsonObject = jsonArray.getJSONObject(i);
                cloudPlanData.setHeading(innerJsonObject.getString("heading"));
                cloudPlanData.setDesc(innerJsonObject.getString("desc"));
                cloudPlanData.setImageID(icons.get(innerJsonObject.getString("icon")));
                //cloudPlanData.setImageBackgroundID(R.drawable.silver_solid_circle);
                if(isFreeQuestionInfoAdded && i == jsonArray.length()-1){
                    planDataList.add(0,cloudPlanData);
                }else {
                    planDataList.add(cloudPlanData);
                }
            }
            planRecyclerViewAdapter.notifyDataSetChanged();
        } catch (Exception e) {

        }
    }


}