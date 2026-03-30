package com.ojassoft.astrosage.varta.adapters;

import static com.ojassoft.astrosage.varta.utils.CUtils.removeAcharyaTarot;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.model.LiveAstrologerModel;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CircularNetworkImageView;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import java.util.ArrayList;

public class LiveAstrologerBottomAdapter extends RecyclerView.Adapter<LiveAstrologerBottomAdapter.MyViewHolder> {
    private Activity activity;
    private ArrayList<LiveAstrologerModel> liveAstrologerModelList;
    public OnItemClick onItemClick;

    public LiveAstrologerBottomAdapter(Activity activity, ArrayList<LiveAstrologerModel> liveAstrologerModelList) {
        this.activity = activity;
        this.liveAstrologerModelList = liveAstrologerModelList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_circular_astrologer, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (liveAstrologerModelList.get(position).getProfileImgUrl() != null && liveAstrologerModelList.get(position).getProfileImgUrl().length() > 0) {
            Glide.with(activity.getApplicationContext())
                    .load(CGlobalVariables.IMAGE_DOMAIN + liveAstrologerModelList.get(position).getProfileImgUrl())
                    .circleCrop()
                    .into(holder.profileImg);
        }
        String astroName = liveAstrologerModelList.get(holder.getAdapterPosition()).getName();
        astroName = removeAcharyaTarot( astroName);
        if(astroName != null){
            astroName = astroName.trim();
        }
        //holder.astroTitleTv.setText(astroName);
        holder.astrologerName.setText(astroName);
        holder.constraintLayout.setOnClickListener(view -> {
            onItemClick.itemClick(view, holder.getAdapterPosition());
        });
    }

    @Override
    public int getItemCount() {
        return liveAstrologerModelList != null ? liveAstrologerModelList.size() : 0;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView profileImg;
        LinearLayout llButton;
        TextView astrologerName, textLive;
        ConstraintLayout constraintLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImg = itemView.findViewById(R.id.profile_img);
            llButton = itemView.findViewById(R.id.ll_button);
            astrologerName = itemView.findViewById(R.id.astrologerName);
            textLive = itemView.findViewById(R.id.text_live);
            constraintLayout = itemView.findViewById(R.id.constraint_layout);
            FontUtils.changeFont(activity, astrologerName, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
            FontUtils.changeFont(activity, textLive, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        }
    }

    public interface OnItemClick {
        void itemClick(View view, int position);
    }
}
