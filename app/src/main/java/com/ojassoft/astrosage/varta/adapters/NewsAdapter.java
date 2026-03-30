package com.ojassoft.astrosage.varta.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.ui.activity.WebViewActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {

    Context context;
    private int[] newsImageList;
    private String[] newsImageUrl;

    public NewsAdapter(Context context, int[] newsImageList, String[] newsImageUrl)
    {
        this.context = context;
        this.newsImageList = newsImageList;
        this.newsImageUrl = newsImageUrl;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_varta_home_banner, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int pos) {
        int position = holder.getAbsoluteAdapterPosition();
        holder.bio_img.setImageResource(newsImageList[position]);

        FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) holder.bio_img.getLayoutParams();
        params.setMargins(0, 0, 20, 0);
        holder.bio_img.setLayoutParams(params);
        holder.bio_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //CUtils.openWebBrowser(context, Uri.parse(newsImageUrl[position]));
                Intent intentWbView = new Intent(context, WebViewActivity.class);
                intentWbView.putExtra(CGlobalVariables.ASTRO_WEBVIEW_TITLE_KEY, CGlobalVariables.MODULE_ABOUT_US);
                intentWbView.putExtra("URL",newsImageUrl[position]);
                intentWbView.putExtra("TITLE_TO_SHOW", context.getString(R.string.astrosage_varta_in_news));
                context.startActivity(intentWbView);
            }
        });
    }

    @Override
    public int getItemCount() {
        return newsImageList.length;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView bio_img;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            bio_img = (ImageView)itemView.findViewById(R.id.ivVartaHomeBanner);
        }
    }
}