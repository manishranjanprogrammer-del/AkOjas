package com.ojassoft.astrosage.customadapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.NetworkImageView;
import com.bumptech.glide.Glide;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.model.SliderModal;
import com.ojassoft.astrosage.ui.act.ActLearnAstrology;
import com.ojassoft.astrosage.ui.act.ActPlayVideo;
import com.ojassoft.astrosage.utils.CGlobalVariables;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by ojas on २५/७/१६.
 */
public class VideoGridAdapter extends RecyclerView.Adapter<VideoGridAdapter.ViewHolder> {

    private final List<SliderModal> entries;
    private final LayoutInflater inflater;
    private final Context context;

    public VideoGridAdapter(Context context, List<SliderModal> entries) {
        this.entries = entries;
        inflater = LayoutInflater.from(context);
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.lay_video_grid, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SliderModal entry = entries.get(position);
        String img_url;
        if (entry.getThumbnailImageURL() == null || entry.getThumbnailImageURL().isEmpty()) {
            // img_url=CGlobalVariables.youTubeBaseUrl+entry.getVideo_url().trim()+getRandomImage();
            img_url = CGlobalVariables.youTubeBaseUrl + entry.getVideo_url().trim() + "/0.jpg";

        } else {
            img_url = entry.getThumbnailImageURL();
        }
        
        Glide.with(holder.imageView).load(img_url).into(holder.imageView);

        holder.label.setText(entry.getTitle());
        holder.itemView.setOnClickListener((v)->{
            try {
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("DATA", entries.get(position));
                    Intent i = new Intent(context, ActPlayVideo.class);
                    i.putExtra("DATA", bundle);
                    i.putExtra("ISRESUME", false);
                    context.startActivity(i);

            }catch (Exception e){
                //
            }
        });
    }

    @Override
    public int getItemCount() {
        return entries.size();
    }


    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView label;

        public ViewHolder(View view) {
            super(view);
            label = view.findViewById(R.id.tvTitle);
            imageView = view.findViewById(R.id.imgSlides);
        }

    }

}



