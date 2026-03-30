package com.ojassoft.astrosage.model;

import static com.ojassoft.astrosage.ui.act.ActAppModule.LANGUAGE_CODE;
import static com.ojassoft.astrosage.ui.act.ActAppModule.brihatHorscopeDeepLinkUrl;
import static com.ojassoft.astrosage.ui.act.BaseInputActivity.SUB_ACTIVITY_UPGRADE_PLAN_UPDTAE;
import static com.ojassoft.astrosage.ui.fragments.Astroshop_Frag.BrihatHorscopeId;
import static com.ojassoft.astrosage.ui.fragments.Astroshop_Frag.GemstoneId;
import static com.ojassoft.astrosage.utils.CGlobalVariables.SCREEN_ID_DHRUV;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.ActAppModule;
import com.ojassoft.astrosage.ui.act.ActAstroShopServices;
import com.ojassoft.astrosage.ui.act.ActUserPlanDetails;
import com.ojassoft.astrosage.ui.act.ActYearly;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.CogniAstroActivity;
import com.ojassoft.astrosage.ui.fragments.Astroshop_Frag;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.util.List;

public class HomeProductAdapter extends RecyclerView.Adapter<HomeProductAdapter.ViewHolder> {
    Activity activity;
    private List<ProductCategory> categoryList;

    public void setCategoryList(List<ProductCategory> categoryList) {
        this.categoryList = categoryList;
        notifyDataSetChanged();
    }

    public HomeProductAdapter(Activity activity, List<ProductCategory> categoryList) {
        this.activity = activity;
        this.categoryList = categoryList;
    }

    @NonNull
    @Override
    public HomeProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.product_home_item_layout, parent, false);
        return new HomeProductAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HomeProductAdapter.ViewHolder holder, int position) {
        // Null and bounds check for categoryList
        if (categoryList == null || position < 0 || position >= categoryList.size()) {
            Log.e("HomeProductAdapter", "Invalid position or categoryList is null");
            return;
        }
        ProductCategory category = categoryList.get(position);
        if (category == null) {
            Log.e("HomeProductAdapter", "Category at position " + position + " is null");
            return;
        }

        String categoryFullName = category.getCategoryShortName();
        String mainModuleId = categoryFullName + "_Main_Module";

        holder.categoryNameTV.setText(categoryFullName);
        holder.categoryNameTV.setTypeface(CUtils.getRobotoFont(activity, LANGUAGE_CODE, CGlobalVariables.medium));
        if (category.getCategoryImagePath() != null) {
            // Load from URL
            Glide.with(activity)
                    .load(category.getCategoryImagePath())
                    .into(holder.categoryIcon);
        } else {
            // Safely parse resource image
            try {
                holder.categoryIcon.setImageResource(Integer.parseInt(category.getResourceImage()));
            } catch (NumberFormatException e) {
                Log.e("HomeProductAdapter", "Invalid resource image for category: " + categoryFullName, e);
            }
        }

        holder.itemView.setOnClickListener(view1 -> {
            callActivity(activity, holder.getAbsoluteAdapterPosition(), category.getCategoryUrl(), mainModuleId,category.getCategoryShortName());
            String analyticsLabel = CGlobalVariables.HOME_PRODUCT_CATEGORY_CLICK + "_" + category.getCategoryFullName();
            analyticsLabel = analyticsLabel.replace(" ", "_");
//            Log.e("LabelCheck", "PRODUCT onBindViewHolder: "+analyticsLabel );
            CUtils.fcmAnalyticsEvents(analyticsLabel, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "Home screen");

        });
    }


    public void callActivity(Activity activity, int position, String deepUrl, String mainModuleId,String titleName) {
        AstrosageKundliApplication.webViewIssue = AstrosageKundliApplication.webViewIssue+"\n"+"callActivity called  ==>>"+activity+" ==>>>>"+position+" ==>>>>"+deepUrl+" ==>>>>"+mainModuleId+" ==>>>>"+titleName;

//        if (position == 0) {
//            CUtils.getUrlLink(brihatHorscopeDeepLinkUrl, activity, LANGUAGE_CODE, 0);
//            CUtils.googleAnalyticSendWitPlayServie(activity,
//                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT, BrihatHorscopeId, null);
//            CUtils.fcmAnalyticsEvents(BrihatHorscopeId, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
//
//        } else if (position == 2) {
//            Intent i = new Intent(activity, ActAstroShopServices.class);
//            activity.startActivity(i);
//            CUtils.googleAnalyticSendWitPlayServie(activity,
//                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
//                    CGlobalVariables.GOOGLE_ANALYTIC_ASTROSHOP_SERVICE, null);
//            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_ASTROSHOP_SERVICE, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
//        }
//        if (position == 8) {
//            CUtils.googleAnalyticSendWitPlayServie(activity,
//                    CGlobalVariables.GOOGLE_ANALYTIC_LAUNCH,
//                    CGlobalVariables.GOOGLE_ANALYTIC_COGNI_ASTRO, null);
//            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_COGNI_ASTRO, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
//
//            Intent intent = new Intent(activity, CogniAstroActivity.class);
//            activity.startActivity(intent);
//
//        }
//        else if (position == 1) {
//            CUtils.googleAnalyticSendWitPlayServie(activity,
//                    CGlobalVariables.GOOGLE_ANALYTIC_LAUNCH,
//                    CGlobalVariables.GOOGLE_ANALYTIC_DHRUV, null);
//            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_DHRUV, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
//
//            if (CUtils.isDhruvPlan(activity)) {
//                Intent intent = new Intent(activity, ActUserPlanDetails.class);
//                activity.startActivity(intent);
//                return;
//            }
//            CUtils.gotoProductPlanListUpdated(activity, ActAppModule.LANGUAGE_CODE, SUB_ACTIVITY_UPGRADE_PLAN_UPDTAE, SCREEN_ID_DHRUV, "upgrade_plan");
//        }
//        else {
        boolean shoppingcartviawebview = com.ojassoft.astrosage.varta.utils.CUtils.getBooleanData(activity, CGlobalVariables.SHOPPING_CART_VIA_WEBVIEW, true);
        AstrosageKundliApplication.webViewIssue = AstrosageKundliApplication.webViewIssue+"\n"+"shoppingcartviawebview  ==>>"+shoppingcartviawebview;
        AstrosageKundliApplication.webViewIssue = AstrosageKundliApplication.webViewIssue+"\n"+"position  ==>>"+position;

        //position==0==Gemstone ||position==1==bracelet ||position==2==rudraksh||position == 5==ynatra||position==6==mala||position==8==pendent
        if(shoppingcartviawebview &&( position==0 ||position==1 ||position==2||position == 5||position==6||position==8)){
            UrlTitleModel urlTitleModel = CUtils.getUrlForWebViewHome(position,activity);

            com.ojassoft.astrosage.varta.utils.CUtils.addFacebookAndFirebaseEvent(CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, urlTitleModel.getCategory(),"HomeProductAdapter");
            CUtils.openAstroSageShopWebView(activity, urlTitleModel.getUrl(),CGlobalVariables.AKHOMELIST, urlTitleModel.getCategory());
            if(!CUtils.getBooleanData(activity,CGlobalVariables.PRODUCT_CATEGORY_NOTIFICATION,false)){
                com.ojassoft.astrosage.varta.utils.CUtils.sendNotificationWithLink(activity, activity.getString(R.string.coupon_code_for_10_off_on_your_next_product_order),activity.getString(R.string.your_discount_coupon_code_for_your_first_product_order_welcome10),urlTitleModel.getUrl());
                CUtils.saveBooleanData(activity,CGlobalVariables.PRODUCT_CATEGORY_NOTIFICATION,true);
            }


            AstrosageKundliApplication.webViewIssue = AstrosageKundliApplication.webViewIssue+"\n"+"urlTitleModel.getUrl()  ==>>"+urlTitleModel.getUrl();
        }else {
            AstrosageKundliApplication.webViewIssue = AstrosageKundliApplication.webViewIssue+"\n"+"else condition for native  ==>>";
            CUtils.getUrlLink(deepUrl, activity, LANGUAGE_CODE, 0);
            CUtils.googleAnalyticSendWitPlayServie(activity,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT, GemstoneId, null);
            CUtils.fcmAnalyticsEvents(mainModuleId, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
//        }
        }
    }

    @Override
    public int getItemCount() {
        return categoryList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView categoryIcon;
        TextView categoryNameTV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryIcon = itemView.findViewById(R.id.imageViewProductIcon);
            categoryNameTV = itemView.findViewById(R.id.textViewProductName);
        }
    }
}
