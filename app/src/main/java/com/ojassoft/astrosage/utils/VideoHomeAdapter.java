package com.ojassoft.astrosage.utils;

import static com.ojassoft.astrosage.customadapters.RecyclerYoutubeCardAdapter.startYoutubeVideo;
import static com.ojassoft.astrosage.ui.act.ActAppModule.LANGUAGE_CODE;

import android.content.Context;
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
import com.ojassoft.astrosage.model.YoutubeKeySingleton;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class VideoHomeAdapter extends RecyclerView.Adapter<VideoHomeAdapter.MyViewHolder> {
    private ArrayList<YoutubeVideoBean> cardList;
    private final Context context;
    private final String youTubeApiKey;

    public VideoHomeAdapter(Context context,ArrayList<YoutubeVideoBean> cardList) {
        this.cardList = cardList;
        this.context = context;
        youTubeApiKey = YoutubeKeySingleton.getInstance().getApiKey();
    }

    public void setData(ArrayList<YoutubeVideoBean> cardList){
        this.cardList = cardList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VideoHomeAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.video_item_home_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VideoHomeAdapter.MyViewHolder holder, int position) {
        YoutubeVideoBean bean = cardList.get(position);

        holder.thumbnailIV.setImageUrl(bean.getThumbnail(), VolleySingleton.getInstance(context).getImageLoader());

       // Log.e("videoBind", "onBindViewHolder: "+bean.getThumbnail()+"\n"+bean.getTitle());
        holder.captionTV.setText(bean.getTitle());
        holder.captionTV.setTypeface(CUtils.getRobotoFont(context, LANGUAGE_CODE, CGlobalVariables.medium));
        holder.captionTV.setTextSize(14);
        holder.captionTV.setMinLines(2);
        String videoId = bean.getVideoId();
        holder.videoLayout.setOnClickListener(view -> {
            CUtils.fcmAnalyticsEvents(CGlobalVariables.HOME_YOUTUBE_VIDEO_CLICK, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "Home screen");
            int pos = holder.getAbsoluteAdapterPosition();
            if (cardList != null && pos >= 0 && cardList.size() > pos) {
                YoutubeVideoBean bean1 = cardList.get(pos);
                if (bean1 != null) {
//                            String videoId = bean.getVideoId();
                    startYoutubeVideo(context,cardList, pos);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return cardList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        NetworkImageView thumbnailIV;
        TextView captionTV;
        ConstraintLayout videoLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            thumbnailIV = itemView.findViewById(R.id.thumbnailIV);
            captionTV = itemView.findViewById(R.id.captionTV);
            videoLayout = itemView.findViewById(R.id.videoLayout);
        }
    }
}
