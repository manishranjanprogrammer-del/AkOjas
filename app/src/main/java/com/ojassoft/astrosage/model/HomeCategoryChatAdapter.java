package com.ojassoft.astrosage.model;

import static com.ojassoft.astrosage.ui.act.ActAppModule.LANGUAGE_CODE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.CLICKED_CATEGORY_ENUM_KEY;
import static com.ojassoft.astrosage.utils.CGlobalVariables.IS_OPENED_FROM_K_AI_CHAT_BTN;
import static com.ojassoft.astrosage.utils.CUtils.isPopupLoginShown;
import static com.ojassoft.astrosage.varta.ui.activity.DashBoardActivity.BACK_FROM_PROFILECHATDIALOG;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.HOME_KUNDLI_CHAT;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.misc.CategorySortHelper;
import com.ojassoft.astrosage.misc.PersonalizedCategoryENUM;
import com.ojassoft.astrosage.ui.act.ActAppModule;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.FlashLoginActivity;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.CallChatAstrologerAdapter;
import com.ojassoft.astrosage.varta.model.UserProfileData;

import java.util.ArrayList;

public class HomeCategoryChatAdapter extends RecyclerView.Adapter<HomeCategoryChatAdapter.MyViewHolder> {

    Activity activity;
    ArrayList<PersonalizedCategoryENUM> personalizedCategoryENUMArrayList;


    public HomeCategoryChatAdapter(Activity activity, ArrayList<PersonalizedCategoryENUM> personalizedCategoryENUMArrayList) {
        this.activity = activity;
        this.personalizedCategoryENUMArrayList = personalizedCategoryENUMArrayList;
    }

    @NonNull
    @Override
    public HomeCategoryChatAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.home_category_item_layout, parent, false);
        return new HomeCategoryChatAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PersonalizedCategoryENUM personalizedCategoryENUM = personalizedCategoryENUMArrayList.get(position);
        holder.categoryNameTV.setText(personalizedCategoryENUM.getTitle(activity, null));
        holder.categoryNameTV.setTypeface(CUtils.getRobotoFont(activity, LANGUAGE_CODE, CGlobalVariables.medium));
        holder.categoryNameTV.setTextSize(14);

        holder.categoryIconIV.setImageResource(personalizedCategoryENUM.getIconResID());
        holder.parentView.setOnClickListener(v -> {
            try {
                //CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_KUNDLI_AI_HOME_ICON+"_"+personalizedCategoryENUM.name(), CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                com.ojassoft.astrosage.varta.utils.CUtils.addFacebookAndFirebaseEvent(CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK,CGlobalVariables.GOOGLE_ANALYTIC_KUNDLI_AI_HOME_ICON+"_"+personalizedCategoryENUM.name(),"HomeCategoryChatAdapter");

                if (CUtils.isUserLogedIn(activity)) {//check astrosage login
                    //TODO analytics missing
                    UserProfileData userProfileData = com.ojassoft.astrosage.varta.utils.CUtils.getUserSelectedProfileFromPreference(activity);
                    if (userProfileData != null && CUtils.isCompleteUserData(userProfileData)) {
                        ((ActAppModule)activity).openKundliAIScreen(personalizedCategoryENUM,userProfileData,false);
                        CategorySortHelper.updateClickCount(activity,personalizedCategoryENUM);
                    } else {
                        Bundle bundle = new Bundle();
                        bundle.putString(CLICKED_CATEGORY_ENUM_KEY,personalizedCategoryENUM.name());
                        bundle.putBoolean(IS_OPENED_FROM_K_AI_CHAT_BTN,false);
                        com.ojassoft.astrosage.varta.utils.CUtils.openProfileForChat(activity, null, HOME_KUNDLI_CHAT, bundle, true, BACK_FROM_PROFILECHATDIALOG);
                    }
                }
                else {
                    isPopupLoginShown = true;
                    AstrosageKundliApplication.isOpenVartaPopup = true;
                    Intent intent = new Intent(activity, FlashLoginActivity.class);
                    activity.startActivity(intent);
                }
            } catch (Exception e) {
                Log.e("userDetails", "onClick: exception :" + e);
            }
        });
    }


    @Override
    public int getItemCount() {
        return personalizedCategoryENUMArrayList != null ? personalizedCategoryENUMArrayList.size() : 0;
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        ImageView categoryIconIV;
        TextView categoryNameTV;
        ConstraintLayout parentView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryIconIV = itemView
                    .findViewById(R.id.imageViewRashiIcon);
            categoryNameTV = itemView
                    .findViewById(R.id.textViewRashiName);
            parentView = itemView
                    .findViewById(R.id.parent_layout);


        }
    }
}
