package com.ojassoft.astrosage.varta.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.model.BannerLinkModel;
import com.ojassoft.astrosage.varta.ui.activity.DashBoardActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;

import java.util.ArrayList;

public class VartaHomeBannerAdapter extends RecyclerView.Adapter<VartaHomeBannerAdapter.BannerViewHolder> {

    private final Context mContext;
    private ArrayList<String> bannerArrayList;
    private ArrayList<BannerLinkModel> bannerLinkArrayList;

    public VartaHomeBannerAdapter(Context mContext, ArrayList<String> bannerArrayList, ArrayList<BannerLinkModel> bannerLinkArrayList){
        this.mContext = mContext;
        this.bannerArrayList = bannerArrayList;
        if (this.bannerArrayList == null) {
            this.bannerArrayList = new ArrayList<>();
        }
        this.bannerLinkArrayList = bannerLinkArrayList;
        if (this.bannerLinkArrayList == null) {
            this.bannerLinkArrayList = new ArrayList<>();
        }
    }

    @NonNull
    @Override
    public VartaHomeBannerAdapter.BannerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BannerViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.layout_varta_home_banner, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VartaHomeBannerAdapter.BannerViewHolder holder, int position) {
        if (bannerArrayList != null) {
            String imgUrl = bannerArrayList.get(position);
            //holder.ivVartaHomeBanner.setImageUrl(imgUrl, VolleySingleton.getInstance(mContext).getImageLoader());
            Glide.with(mContext.getApplicationContext()).load(imgUrl).into(holder.ivVartaHomeBanner);
        }

        holder.ivVartaHomeBanner.setOnClickListener(v -> {
            if (mContext instanceof DashBoardActivity) {
                try {
                    BannerLinkModel linkModel = bannerLinkArrayList.get(position);
                    String link = linkModel.getLink();
                    if(link == null) link = "";
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_HOME_BANNER_CLICK, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, link);
                    ((DashBoardActivity) mContext).onBannerClicked(linkModel);
                } catch (Exception e) {
                    //
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        if (bannerArrayList == null) return 0;
        return bannerArrayList.size();
    }

    public class BannerViewHolder extends RecyclerView.ViewHolder {
        ImageView ivVartaHomeBanner;
        public BannerViewHolder(@NonNull View itemView) {
            super(itemView);
            ivVartaHomeBanner = itemView.findViewById(R.id.ivVartaHomeBanner);
        }
    }
}
