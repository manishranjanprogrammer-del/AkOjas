package com.ojassoft.astrosage.model;

import static com.ojassoft.astrosage.ui.act.ActAppModule.LANGUAGE_CODE;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.NetworkImageView;
import com.libojassoft.android.customrssfeed.YoutubeVideoBean;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.ActPlayVideo;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.VideoHomeAdapter;

import java.util.List;


public class AstrologyLessonHomeAdapter extends RecyclerView.Adapter<AstrologyLessonHomeAdapter.ViewHolder> {
    public List<SliderModal> list;
    public Activity activity;

    public AstrologyLessonHomeAdapter(List<SliderModal> list, Activity activity) {
        this.list = list;
        this.activity = activity;
    }

    public void setDataList(List<SliderModal> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AstrologyLessonHomeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item_home_layout, parent, false);
        return new AstrologyLessonHomeAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AstrologyLessonHomeAdapter.ViewHolder holder, int position) {
        // Null and bounds check
        if (list == null || position < 0 || position >= list.size()) {
            holder.titleText.setText("");
            return;
        }
        SliderModal bean = list.get(position);
        String thumbnail = (bean != null && bean.getUrl() != null) ? bean.getUrl() : "";
        if (thumbnail.isEmpty() && bean != null && bean.getThumbnailImageURL() != null) {
            thumbnail = bean.getThumbnailImageURL();
        }

        holder.thumbnailImage.setImageUrl(thumbnail, VolleySingleton.getInstance(activity).getImageLoader());
        holder.titleText.setText(bean != null ? bean.getTitle() : "");
        holder.titleText.setTypeface(CUtils.getRobotoFont(activity, LANGUAGE_CODE, CGlobalVariables.medium));
        holder.titleText.setMinLines(2);
        holder.parentLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CUtils.fcmAnalyticsEvents(CGlobalVariables.HOME_ASTROLOGY_LESSON_CLICK, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "Home screen");
                Bundle bundle = new Bundle();
                bundle.putSerializable("DATA", list.get(holder.getAbsoluteAdapterPosition()));
                Intent i = new Intent(activity, ActPlayVideo.class);
                i.putExtra("DATA", bundle);
                i.putExtra("ISRESUME", true);

                activity.startActivity(i);

            }
        });
    }

    @Override
    public int getItemCount() {
        return list != null ? list.size() : 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        NetworkImageView thumbnailImage;
        TextView titleText;
        ConstraintLayout parentLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnailImage = itemView.findViewById(R.id.thumbnailIV);
            titleText = itemView.findViewById(R.id.captionTV);
            parentLayout = itemView.findViewById(R.id.videoLayout);

        }
    }
}
