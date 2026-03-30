package com.ojassoft.astrosage.ui.fragments;

import static com.ojassoft.astrosage.ui.act.BaseInputActivity.SUB_ACTIVITY_UPGRADE_PLAN_UPDTAE;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.tagmanager.Container;
import com.google.android.gms.tagmanager.ContainerHolder;
import com.google.android.gms.tagmanager.TagManager;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.CloudPlanData;
import com.ojassoft.astrosage.jinterface.IPurchasePlan;
import com.ojassoft.astrosage.notification.ActShowOjasSoftArticles;
import com.ojassoft.astrosage.tagmanager.ContainerHolderSingleton;
import com.ojassoft.astrosage.tagmanager.ContainerLoadedCallback;
import com.ojassoft.astrosage.ui.act.ActAppModule;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.PurchasePlanHomeActivity;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.PlanLocalizationUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by ojas on 18/10/16.
 */
public class FragAdvertisementView extends Fragment {

    private View view = null;
    public int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    public Typeface typeface;
    private TextView textViewAstroFeature, textViewExperiance;
    Activity activity;

    //View exploreSilverPlanView;
    private Button btnSilverPlan;
    private IPurchasePlan iPurchasePlan;

    /* String data_SliverPlanAstrologicalFeaturesEnglish = "";
     String data_SliverPlanAstrologicalFeaturesHindi = "";
     String data_SliverPlanAstrologicalFeaturesTamil = "";
     String data_SliverPlanAstrologicalFeaturesBangali = "";
     String data_SliverPlanAstrologicalFeaturesMarathi = "";
     String data_SliverPlanAstrologicalFeaturesTelugu = "";
     String data_SliverPlanAstrologicalFeaturesKannad = "";
     String data_SliverPlanAstrologicalFeaturesGujrati = "";
     String data_SliverPlanAstrologicalFeaturesMalayalam = "";*/
   /* String data_GoldPlanAstrologicalFeaturesEnglish = "";
    String data_GoldPlanAstrologicalFeaturesHindi = "";
    String data_GoldPlanAstrologicalFeaturesTamil = "";
    String data_GoldPlanAstrologicalFeaturesBangali = "";
    String data_GoldPlanAstrologicalFeaturesMarathi = "";
    String data_GoldPlanAstrologicalFeaturesTelugu = "";
    String data_GoldPlanAstrologicalFeaturesKannad = "";
    String data_GoldPlanAstrologicalFeaturesGujrati = "";
    String data_GoldPlanAstrologicalFeaturesMalayalam = "";*/
    String data_DhruvPlanAstrologicalFeaturesEnglish = "";
    String data_DhruvPlanAstrologicalFeaturesHindi = "";
    String data_DhruvPlanAstrologicalFeaturesTamil = "";
    String data_DhruvPlanAstrologicalFeaturesBangali = "";
    String data_DhruvPlanAstrologicalFeaturesMarathi = "";
    String data_DhruvPlanAstrologicalFeaturesTelugu = "";
    String data_DhruvPlanAstrologicalFeaturesKannad = "";
    String data_DhruvPlanAstrologicalFeaturesGujrati = "";
    String data_DhruvPlanAstrologicalFeaturesMalayalam = "";
    String data_DhruvPlanAstrologicalFeaturesOdia = "";
    String data_DhruvPlanAstrologicalFeaturesAssamese = "";

    private int initializeTagManagerCount = 0;
    private List<CloudPlanData> planDataList;

    public static FragAdvertisementView newInstance() {
        FragAdvertisementView appViewFragment = new FragAdvertisementView();
        return appViewFragment;
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

    public FragAdvertisementView() {
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        LANGUAGE_CODE = ((AstrosageKundliApplication) activity
                .getApplication()).getLanguageCode();
        typeface = CUtils.getRobotoFont(activity,
                LANGUAGE_CODE, CGlobalVariables.regular);
        planDataList = new ArrayList<>();
        if (view == null)
            view = inflater.inflate(R.layout.lay_frag_advertisement_view, container, false);


        textViewAstroFeature = (TextView) view
                .findViewById(R.id.Astrofeatureheading);
        textViewAstroFeature.setTypeface(typeface);
        textViewExperiance = (TextView) view
                .findViewById(R.id.Astrofeaturesubheading);
        textViewExperiance.setTypeface(typeface);


        final LinearLayout linearLayout = (LinearLayout) view.findViewById(R.id.llPlanCointainer);
        getDataFromGTMCointainer(linearLayout);

        /*exploreSilverPlanView=(View)view.findViewById(R.id.exploremorefeaturedivider);
        moreExploreSilverPlan=(TextView)view.findViewById(R.id.moreexploresilverplan);
        moreExploreSilverPlan.setTypeface(typeface);
        moreExploreSilverPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                moreExploreSilverPlan.setVisibility(View.GONE);
                exploreSilverPlanView.setVisibility(View.GONE);
                addViewsInLayout(linearLayout,true);
            }
        });*/

        // btnselectPlanpermonth.setTypeface(typeface);
        btnSilverPlan = (Button) view.findViewById(R.id.btnSilverPlan);
        // btnSilverPlan.setTypeface(typeface);
        btnSilverPlan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                CUtils.fcmAnalyticsEvents(CGlobalVariables.KUNDLI_CLOUD_PLAN, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                openPlanSelectDialog();
            }
        });

        //String buy_now = getActivity().getResources().getString(R.string.astroshop_buy_now);

        if (((AstrosageKundliApplication) activity.getApplication())
                .getLanguageCode() == CGlobalVariables.ENGLISH) {

            //btnSilverPlan.setText(buy_now.toUpperCase() + " - " + ((OutputMasterActivity) activity).silverPlanPriceMonth);

            String buyText;
          /*  if (((OutputMasterActivity) getActivity()).enableMonthlySubscriptionValue) {
                buyText = getResources().getString(R.string.join_free_month).toUpperCase();
                //buyText = getResources().getString(R.string.buy_gold_plan);
            } else {*/
                buyText = getActivity().getResources().getString(R.string.buy_platinum_plan).toUpperCase() ;
           // }
            btnSilverPlan.setText(buyText);
        } else {
            //btnSilverPlan.setText(buy_now + " - " + ((OutputMasterActivity) activity).silverPlanPriceMonth);
            String buyText;
           /* if (((OutputMasterActivity) getActivity()).enableMonthlySubscriptionValue) {
                buyText = getResources().getString(R.string.join_free_month);
                //buyText = getResources().getString(R.string.buy_gold_plan);
            } else {*/
                buyText = getActivity().getResources().getString(R.string.buy_platinum_plan);
            //}
            btnSilverPlan.setText(buyText);
        }


        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }

   /* @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (view != null) {
            ViewGroup parentViewGroup = (ViewGroup) view.getParent();
            if (parentViewGroup != null) {
                parentViewGroup.removeAllViews();
            }
        }
    }*/

    private void openPlanSelectDialog() {
        CUtils.fcmAnalyticsEvents(CGlobalVariables.KUNDLI_CLOUD_PLAN_CLICK, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
        CUtils.gotoProductPlanListUpdated(activity, LANGUAGE_CODE, SUB_ACTIVITY_UPGRADE_PLAN_UPDTAE, CGlobalVariables.PLATINUM_PLAN_VALUE_MONTH, CGlobalVariables.SOURCE_FROM_KUNDLI_CLOUD_FRAG
        );
       /* FragmentManager fragmentManager = getChildFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        Fragment prev = fragmentManager.findFragmentByTag("HOME_PLANACTIVATOR");
        if (prev != null) {
            fragmentTransaction.remove(prev);
        }
        fragmentTransaction.addToBackStack(null);
        ChoosePlanFragmentDailogSilver choosePlanFragmentDailog = new ChoosePlanFragmentDailogSilver();
        choosePlanFragmentDailog.show(fragmentManager, "HOME_PLANACTIVATOR");
        fragmentTransaction.commit();*/
//        iPurchasePlan
//                .selectedPlan(PurchasePlanHomeActivity.PLATINUM_PLAN_MONTHLY);
    }

    /**
     * @author amit Rautela
     * @desc This method is used to get the data from cointainer
     */
    private void getDataFromGTMCointainer(final LinearLayout linearLayout) {

        data_DhruvPlanAstrologicalFeaturesEnglish = CGlobalVariables.dataPlatinumPlanAstrologicalFeaturesEnglish;
        data_DhruvPlanAstrologicalFeaturesHindi = CGlobalVariables.dataPlatinumPlanAstrologicalFeaturesHindi;
        data_DhruvPlanAstrologicalFeaturesTamil = CGlobalVariables.dataPlatinumPlanAstrologicalFeaturesTamil;
        data_DhruvPlanAstrologicalFeaturesBangali = CGlobalVariables.dataPlatinumPlanAstrologicalFeaturesBangali;
        data_DhruvPlanAstrologicalFeaturesMarathi = CGlobalVariables.dataPlatinumPlanAstrologicalFeaturesMarathi;
        data_DhruvPlanAstrologicalFeaturesTelugu = CGlobalVariables.dataPlatinumPlanAstrologicalFeaturesTelugu;
        data_DhruvPlanAstrologicalFeaturesKannad = CGlobalVariables.dataPlatinumPlanAstrologicalFeaturesKannad;
        data_DhruvPlanAstrologicalFeaturesGujrati = CGlobalVariables.dataPlatinumPlanAstrologicalFeaturesGujrati;
        data_DhruvPlanAstrologicalFeaturesMalayalam = CGlobalVariables.dataPlatinumPlanAstrologicalFeaturesMalayalam;
        data_DhruvPlanAstrologicalFeaturesOdia = CGlobalVariables.dataPlatinumPlanAstrologicalFeaturesOdia;
        data_DhruvPlanAstrologicalFeaturesAssamese = CGlobalVariables.dataPlatinumPlanAstrologicalFeaturesAssamese;

        addViewsInLayout(linearLayout, true);
    }

    private void addViewsInLayout(LinearLayout linearLayout, boolean showAll) {

        String data = "";
        try {
            if (linearLayout != null) {
                linearLayout.removeAllViews();
            }
            data = PlanLocalizationUtil.getPlatinumPlanFeatureJson(LANGUAGE_CODE);
        } catch (Exception e) {

        }
        /*
        Example Json Data :
        {"plans":["Create kundli", "Horoscope matching", "Save 10 charts on cloud","Save unlimited charts on your device"]}
         */
        try {
            parseData(data);


            LinearLayout.LayoutParams params =
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.gravity = Gravity.CENTER;
            // if(showAll) {
            for (int i = 0; i < planDataList.size(); i++) {
                LinearLayout linearLayout1 = new LinearLayout(activity);
                linearLayout1.setLayoutParams(params);
                linearLayout1.setOrientation(LinearLayout.HORIZONTAL);
                if (i % 2 == 0) {
                    linearLayout1.setBackgroundColor(getActivity().getResources().getColor(R.color.light_gray_bg));
                } else {
                    linearLayout1.setBackgroundColor(getActivity().getResources().getColor(R.color.white));
                }

                linearLayout1.setPadding(50, 10, 50, 10);

                //  TextView textViewDot = createTextViewDot(jsonArray.getString(i),i);
                TextView textView = createTextView(planDataList.get(i).getHeading(), i, planDataList.size());

                //linearLayout1.addView(textViewDot);
                if (textView != null) {
                    linearLayout1.addView(textView);
                }
                linearLayout.addView(linearLayout1);
            }
           /* }else{
                for (int i = 0; i < 4; i++) {
                    LinearLayout linearLayout1 = new LinearLayout(activity);
                    linearLayout1.setLayoutParams(params);
                    linearLayout1.setOrientation(LinearLayout.HORIZONTAL);

                    TextView textViewDot = createTextViewDot(jsonArray.getString(i),i);
                    TextView textView = createTextView(jsonArray.getString(i),i);

                    linearLayout1.addView(textViewDot);
                    linearLayout1.addView(textView);

                    linearLayout.addView(linearLayout1);
                }
            }*/
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private TextView createTextViewDot(final String plan, final int pos) {
        TextView textView = new TextView(activity);
        // if(pos == 0) {
        //textView.setText(Html.fromHtml("<br/>" + "&nbsp;" + activity.getResources().getString(R.string.dotsign) + "&nbsp;&nbsp;"));
        textView.setText(activity.getResources().getString(R.string.dotsign));
        /*}else{
           // textView.setText(Html.fromHtml("&nbsp;" + activity.getResources().getString(R.string.dotsign) + "&nbsp;&nbsp;"));
            textView.setText("\n  "+activity.getResources().getString(R.string.dotsign)+"  ");
        }*/
        textView.setTypeface(typeface);
        textView.setTextColor(activity.getResources().getColor(R.color.black));
        textView.setTextSize(14f);

        return textView;
    }

    private TextView createTextView(final String plan, final int pos, int length) {
        if (activity == null) {
            return null;
        }
        TextView textView = new TextView(activity);
        //   if(pos == 0) {
        // textView.setText(Html.fromHtml("<br/>" +plan +"<br/>"));
        try {
            if (plan != null && !plan.isEmpty()) {
                textView.setText(Html.fromHtml(plan));
            } else {
                textView.setText(getActivity().getResources().getStringArray(R.array.cloud_plans)[pos]);
            }

       /* }else{
            //textView.setText(Html.fromHtml(plan +"<br/>"));
            textView.setText("\n  "+plan);
        }*/
            textView.setTypeface(typeface);
            if (pos % 2 == 0) {
                textView.setTextColor(activity.getResources().getColor(R.color.black));
            }else{
                textView.setTextColor(activity.getResources().getColor(R.color.black));
            }
            textView.setTextSize(14f);
            if (pos == length - 1) {

                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(activity, ActShowOjasSoftArticles.class);
                        intent.putExtra(CGlobalVariables.ASTRO_WEBVIEW_TITLE_KEY, CGlobalVariables.MODULE_ABOUT_US);
                        intent.putExtra("URL", CGlobalVariables.ASTROSAGE_TERMS_CONDITIONS_URL);
                        intent.putExtra("TITLE_TO_SHOW", "Terms and Conditions");
                        startActivity(intent);

                    }
                });
            }

        } catch (Exception e) {

        }
        return textView;
    }

    private void parseData(String jsonString) {
        try {
            //HashMap<String, Integer> icons = ((OutputMasterActivity) activity).getIcons();
            JSONObject object = new JSONObject(jsonString);
            JSONArray jsonArray = object.getJSONArray("plans");
            JSONObject innerJsonObject;
            CloudPlanData cloudPlanData;
            for (int i = 0; i < jsonArray.length(); i++) {
                cloudPlanData = new CloudPlanData();
                innerJsonObject = jsonArray.getJSONObject(i);
                cloudPlanData.setHeading(innerJsonObject.getString("heading"));
                cloudPlanData.setDesc(innerJsonObject.getString("desc"));
                //cloudPlanData.setImageID(icons.get(innerJsonObject.getString("icon")));
                cloudPlanData.setImageBackgroundID(R.drawable.silver_solid_circle);
                planDataList.add(cloudPlanData);
            }
            Log.e("", "" + planDataList.size());
        } catch (Exception e) {
            Log.e("", "" + e.getMessage());
        }
    }
}
