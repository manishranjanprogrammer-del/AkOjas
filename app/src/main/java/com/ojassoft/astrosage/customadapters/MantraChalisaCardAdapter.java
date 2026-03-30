package com.ojassoft.astrosage.customadapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.model.ChalisaDataModel;
import com.ojassoft.astrosage.model.YoutubeKeySingleton;
import com.ojassoft.astrosage.ui.act.ActMantraVideoPlayer;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.util.ArrayList;

public class MantraChalisaCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<ChalisaDataModel> chalisaDataModelArrayList;
    private Context context;
    private String youTubeApiKey;

    public MantraChalisaCardAdapter(ArrayList<ChalisaDataModel> chalisaDataList, final Context context) {
        this.chalisaDataModelArrayList = chalisaDataList;
        this.context = context;
        youTubeApiKey = YoutubeKeySingleton.getInstance().getApiKey();
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.youtube_card_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        try {
            if (holder instanceof MyViewHolder) {
                ((MyViewHolder) holder).llcardView.removeAllViews();
                View view = getYoutubeView(position);
                view.setId(position);
                ((MyViewHolder) holder).llcardView.addView(view);
            }
            if (position == 0) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(CUtils.convertDpToPx(context, 4), CUtils.convertDpToPx(context, 4), CUtils.convertDpToPx(context, 4), 0);
                ((MyViewHolder) holder).llcardView.setLayoutParams(params);
            } else {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(CUtils.convertDpToPx(context, 4), 0, CUtils.convertDpToPx(context, 4), 0);
                ((MyViewHolder) holder).llcardView.setLayoutParams(params);
            }
        } catch (Exception ex) {
            Log.i("TAG", ex.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return chalisaDataModelArrayList == null ? 0 : chalisaDataModelArrayList.size();
    }

    private void startYoutubeVideo(int pos) {
        try {

            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_YOUTUBE_PLAY_VIDEO_CLICK, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

            Intent intent = new Intent(context, ActMantraVideoPlayer.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("playlist", chalisaDataModelArrayList);
            bundle.putInt("position", pos);
            intent.putExtras(bundle);
            context.startActivity(intent);
        } catch (Exception ex) {
            Log.e("", ex.getMessage());
        }
    }

    private View getYoutubeView(final int position) {

        View itemView = LayoutInflater.from(context).inflate(R.layout.lay_video_card, null, false);
        try {
            itemView.setLayoutParams(new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            ));
            //YouTubeThumbnailView youTubeThumbnailView = itemView.findViewById(R.id.youtube_thumbnail);
            final TextView textView1 = itemView.findViewById(R.id.videofeedtitle);
            ChalisaDataModel bean = chalisaDataModelArrayList.get(position);
            //if (bean == null) return;
            textView1.setText(bean.getTitle());
            String videoId = bean.getVideoURL();

        } catch (Exception ex) {
            Log.i("", ex.getMessage());
        }

        return itemView;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public RelativeLayout llcardView;

        public MyViewHolder(View itemView) {
            super(itemView);
            llcardView = itemView.findViewById(R.id.llcardView);
            llcardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int pos = getLayoutPosition();
                    if (chalisaDataModelArrayList != null && chalisaDataModelArrayList.size() > pos) {
                        ChalisaDataModel bean = chalisaDataModelArrayList.get(pos);
                        if (bean != null) {
                            startYoutubeVideo(pos);
                        }
                    }
                }
            });
        }
    }
}
