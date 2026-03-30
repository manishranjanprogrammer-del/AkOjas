package com.ojassoft.astrosage.customadapters;

import android.app.Activity;
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

import com.android.volley.toolbox.NetworkImageView;
import com.libojassoft.android.customrssfeed.YoutubeVideoBean;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.model.YoutubeKeySingleton;
import com.ojassoft.astrosage.ui.act.ActYouTubeVideoPlayer;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.util.ArrayList;

/**
 * Created by ojas-20 on 24/8/18.
 */

public class RecyclerYoutubeCardAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final int VIEW_TYPE_ITEM = 0;
    private ArrayList<YoutubeVideoBean> cardList;
    private final Context context;
    private final String youTubeApiKey;

    public RecyclerYoutubeCardAdapter(ArrayList<YoutubeVideoBean> cardList, RecyclerView recyclerView, final Context context) {
        this.cardList = cardList;
        this.context = context;
        youTubeApiKey = YoutubeKeySingleton.getInstance().getApiKey();
    }

    @Override
    public int getItemViewType(int position) {
        return VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int i) {

        if (i == VIEW_TYPE_ITEM) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.youtube_card_view, parent, false);
            return new MyViewHolder(view);
        }
        return null;
    }


    public void setData(ArrayList<YoutubeVideoBean> cardList) {
        this.cardList = cardList;
        notifyDataSetChanged();
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
        return cardList == null ? 0 : cardList.size();
    }

    public void addData(ArrayList<YoutubeVideoBean> arrayList) {
        for (int i = 0; i < arrayList.size(); i++) {
            cardList.add(arrayList.get(i));

        }
        notifyDataSetChanged();
    }

    public void addNullVal() {
        cardList.add(null);
        notifyDataSetChanged();
    }

    public void removeNullVal() {
        cardList.remove(null);
        notifyDataSetChanged();
    }

    public static void startYoutubeVideo(Context context ,ArrayList<YoutubeVideoBean> cardList,  int pos) {
        try {

            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_YOUTUBE_PLAY_VIDEO_CLICK, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

            Intent intent = new Intent(context, ActYouTubeVideoPlayer.class);
            Bundle bundle = new Bundle();

            bundle.putSerializable("playlist", cardList);
            bundle.putInt("position", pos);
            intent.putExtras(bundle);
            ((Activity) context).startActivityForResult(intent, 5100);

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
            NetworkImageView youTubeThumbnailView = itemView.findViewById(R.id.youtube_thumbnail);
            final TextView textView1 = itemView.findViewById(R.id.videofeedtitle);
            YoutubeVideoBean bean = cardList.get(position);

            youTubeThumbnailView.setImageUrl(bean.getThumbnail(), VolleySingleton.getInstance(context).getImageLoader());

            textView1.setText(bean.getTitle());
            String videoId = bean.getVideoId();

            /*if(youTubeThumbnailView != null) {
                youTubeThumbnailView.initialize(youTubeApiKey, new YouTubeThumbnailView.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader youTubeThumbnailLoader) {
                        try {
                            YoutubeVideoBean bean = cardList.get(position);
                            if (bean == null) return;
                            textView1.setText(bean.getTitle());
                            String videoId = bean.getVideoId();
                            youTubeThumbnailLoader.setVideo(videoId);
                            youTubeThumbnailLoader.setOnThumbnailLoadedListener(new YouTubeThumbnailLoader.OnThumbnailLoadedListener() {
                                @Override
                                public void onThumbnailLoaded(YouTubeThumbnailView youTubeThumbnailView, String s) {
                                    if (youTubeThumbnailLoader != null) {
                                        youTubeThumbnailLoader.release();
                                    }
                                }

                                @Override
                                public void onThumbnailError(YouTubeThumbnailView youTubeThumbnailView, YouTubeThumbnailLoader.ErrorReason errorReason) {

                                }
                            });

                        } catch (Exception e) {
                            Log.i("Error>>", e.getMessage());
                        }


                    }

                    @Override
                    public void onInitializationFailure(YouTubeThumbnailView youTubeThumbnailView, YouTubeInitializationResult youTubeInitializationResult) {
                        //write something for failure
                        Log.e("", "");
                    }
                });
            }*/
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
                    if (cardList != null && cardList.size() > pos) {
                        YoutubeVideoBean bean = cardList.get(pos);
                        if (bean != null) {
//                            String videoId = bean.getVideoId();
                            startYoutubeVideo(context,cardList, pos);
                        }
                    }
                }
            });

        }

    }
}
