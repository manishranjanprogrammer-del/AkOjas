package com.ojassoft.astrosage.varta.adapters;

import static com.ojassoft.astrosage.varta.utils.CUtils.removeAcharyaTarot;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.ActAppModule;
import com.ojassoft.astrosage.varta.model.LiveAstrologerModel;
import com.ojassoft.astrosage.varta.ui.activity.AllLiveAstrologerActivity;
import com.ojassoft.astrosage.varta.ui.activity.DashBoardActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.CircularNetworkImageView;
import com.ojassoft.astrosage.varta.utils.FontUtils;

import java.util.ArrayList;

public class LiveAstrologerAdapter extends RecyclerView.Adapter<LiveAstrologerAdapter.MyViewHolder> {

    Context context;
    ArrayList<LiveAstrologerModel> liveAstrologerModelArrayList;
    public boolean layoutTypeSmall;
    int showAllAstro;
    private static final int HOME_ASTRO_LIST_LIMIT = 20;
    public LiveAstrologerAdapter(Context context, ArrayList<LiveAstrologerModel> liveAstrologerModels,int showAllAstro) {
        this.context = context;
        this.liveAstrologerModelArrayList = liveAstrologerModels;
        this.showAllAstro = showAllAstro;
        if (liveAstrologerModelArrayList != null) {
            layoutTypeSmall = liveAstrologerModelArrayList.size() > 4;
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;
        if (context instanceof DashBoardActivity || context instanceof ActAppModule) {
            view = LayoutInflater.from(parent.getContext()).inflate(layoutTypeSmall ? R.layout.layout_live_astrologer_grid_small : R.layout.item_live_astrologer, parent, false);
//            view.setLayoutParams(new ViewGroup.LayoutParams((int)(parent.getWidth() * 0.7),ViewGroup.LayoutParams.WRAP_CONTENT));
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(layoutTypeSmall ? R.layout.layout_live_astrologer_grid_small : R.layout.item_live_astrologer_all, parent, false);
        }

        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if (liveAstrologerModelArrayList != null && liveAstrologerModelArrayList.size() > position) {
            LiveAstrologerModel liveAstrologerModel = liveAstrologerModelArrayList.get(position);
            if (liveAstrologerModel == null) return;

            if (context instanceof DashBoardActivity || context instanceof ActAppModule||context instanceof AllLiveAstrologerActivity) {
                if (!layoutTypeSmall) {
                    LinearLayout.LayoutParams layoutParams;

                    if (liveAstrologerModelArrayList.size() == 1) {
                        layoutParams = new LinearLayout.LayoutParams((int) (CGlobalVariables.width) - 40, (int) (CGlobalVariables.height * 0.175));
                        layoutParams.setMargins(20, 0, 20, 0);
                    } else {
                        layoutParams = new LinearLayout.LayoutParams((int) (CGlobalVariables.width * 0.8), (int) (CGlobalVariables.height * 0.175));
                        if (position == liveAstrologerModelArrayList.size() - 1) {
                            layoutParams.setMargins(20, 0, 20, 0);
                        } else {
                            layoutParams.setMargins(20, 0, 0, 0);
                        }
                    }

                    if (!(context instanceof AllLiveAstrologerActivity)) {
                        holder.mainlayoutLL.setLayoutParams(layoutParams);
                    }
                    holder.astroTitleTv.setText(liveAstrologerModel.getName());
                }
                else {
                    String astroName = liveAstrologerModel.getName();
                    astroName = removeAcharyaTarot( astroName);
                    if(astroName != null){
                        astroName = astroName.trim();
                    }
                    holder.astroTitleTv.setText(astroName);
                }
            } else {
               //
            }
            Animation startAnimation = AnimationUtils.loadAnimation(context, R.anim.blinking_animation);
            holder.onlineImage.startAnimation(startAnimation);
            holder.expertiseNameTV.setText(CUtils.toTitleCases(liveAstrologerModel.getExpertise()));
            String astrologerProfileUrl = "";
            if (liveAstrologerModel.getProfileImgUrl() != null && liveAstrologerModel.getProfileImgUrl().length() > 0) {
                astrologerProfileUrl = CGlobalVariables.IMAGE_DOMAIN + liveAstrologerModel.getProfileImgUrl();
                //holder.roundImage.setImageUrl(astrologerProfileUrl, VolleySingleton.getInstance(context).getImageLoader());
                //Log.e("astrologerProfileUrl", "astrologerProfileUrl="+astrologerProfileUrl);
                Glide.with(context.getApplicationContext()).load(astrologerProfileUrl).circleCrop().placeholder(R.drawable.ic_profile_view).into(holder.roundImage);
            }
            holder.mainlayoutLL.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (context instanceof DashBoardActivity) {
                            CUtils.fcmAnalyticsEvents(com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTIC_VARTA_TAB_LIVE_JOIN, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                            com.ojassoft.astrosage.utils.CUtils.createSession(context, com.ojassoft.astrosage.utils.CGlobalVariables.VARTA_TAB_LIVE_JOIN_PARTNER_ID);
                            ((DashBoardActivity) context).checkPermissions(liveAstrologerModel);
                        } else if (context instanceof AllLiveAstrologerActivity) {
                            ((AllLiveAstrologerActivity) context).checkPermissions(liveAstrologerModel);
                        } else if (context instanceof ActAppModule) {
                            CUtils.fcmAnalyticsEvents(com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTIC_VARTA_HOME_LIVE_JOIN, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                            com.ojassoft.astrosage.utils.CUtils.createSession(context, com.ojassoft.astrosage.utils.CGlobalVariables.VARTA_HOME_LIVE_JOIN_PARTNER_ID);
                            ((ActAppModule) context).checkPermissions(liveAstrologerModel);
                        }
                    } catch (Exception e) {
                    }
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if(liveAstrologerModelArrayList!=null){
            if(showAllAstro==0){
                count = Math.min(liveAstrologerModelArrayList.size(), HOME_ASTRO_LIST_LIMIT);
            }
            else
                count = liveAstrologerModelArrayList.size();
            }

        return count;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView astroTitleTv, expertiseNameTV;
        CircularNetworkImageView roundImage;
        CardView mainlayoutLL;
        ImageView onlineImage;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            astroTitleTv = itemView.findViewById(R.id.astroTitleTV);
            expertiseNameTV = itemView.findViewById(R.id.expertiseNameTV);
            mainlayoutLL = itemView.findViewById(R.id.mainlayoutLL);
            roundImage = itemView.findViewById(R.id.ri_profile_img);
            onlineImage = itemView.findViewById(R.id.online_offline_img);
            FontUtils.changeFont(context, expertiseNameTV, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
            if (context instanceof DashBoardActivity) {
                FontUtils.changeFont(context, astroTitleTv, com.ojassoft.astrosage.utils.CGlobalVariables.FONTS_ROBOTO_MEDIUM);
            }
        }
    }
}