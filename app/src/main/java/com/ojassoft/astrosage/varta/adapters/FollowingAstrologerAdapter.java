package com.ojassoft.astrosage.varta.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.model.FollowAstrologerModel;
import com.ojassoft.astrosage.varta.ui.activity.FollowingAstrologerActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CircularNetworkImageView;
import com.ojassoft.astrosage.varta.utils.FontUtils;

import java.util.ArrayList;

public class FollowingAstrologerAdapter extends RecyclerView.Adapter<FollowingAstrologerAdapter.MyViewHolder> {

    Context context;
    ArrayList<FollowAstrologerModel> followAstrologerModelArrayList;

    public FollowingAstrologerAdapter(Context context, ArrayList<FollowAstrologerModel> followAstrologerModels) {
        this.context = context;
        this.followAstrologerModelArrayList = followAstrologerModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_follwing_astrologer_all, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if (followAstrologerModelArrayList != null && followAstrologerModelArrayList.size() > position) {
            FollowAstrologerModel followAstrologerModel = followAstrologerModelArrayList.get(position);
            if (followAstrologerModel == null) return;

            holder.astroTitleTv.setText(followAstrologerModel.getAstrologerName());
            String astrologerProfileUrl = "";
            if (followAstrologerModel.getAstrologerImage() != null && followAstrologerModel.getAstrologerImage().length() > 0) {
                astrologerProfileUrl = CGlobalVariables.IMAGE_DOMAIN + followAstrologerModel.getAstrologerImage();
                Glide.with(context.getApplicationContext()).load(astrologerProfileUrl).circleCrop().placeholder(R.drawable.ic_profile_view).into(holder.roundImage);
            }
        }
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (followAstrologerModelArrayList != null) {
            count = followAstrologerModelArrayList.size();
        }
        return count;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView astroTitleTv,astroTitleFollow;
        CircularNetworkImageView roundImage;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            astroTitleTv = itemView.findViewById(R.id.astroTitleTV);
            roundImage = itemView.findViewById(R.id.ri_profile_img);
            astroTitleFollow = itemView.findViewById(R.id.astroTitleFollow);
            FontUtils.changeFont(context, astroTitleTv, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
            FontUtils.changeFont(context, astroTitleFollow, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        }
    }
}