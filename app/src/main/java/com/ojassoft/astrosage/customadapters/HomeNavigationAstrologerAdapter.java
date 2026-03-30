package com.ojassoft.astrosage.customadapters;

import android.content.Context;
import android.graphics.Typeface;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.model.AllAstrologerInfo;

import java.util.Collections;
import java.util.List;

/**
 * Created by ojas-20 on 12/3/18.
 */

public class HomeNavigationAstrologerAdapter extends RecyclerView.Adapter<HomeNavigationAstrologerAdapter.MyViewHolder> {

    private Typeface typeface;
    private LayoutInflater inflater;
    private List<AllAstrologerInfo> data = Collections.emptyList();
    private ImageLoader imageLoader;
    private VolleySingleton vsing;

    public HomeNavigationAstrologerAdapter(Context context, Typeface typeface, List<AllAstrologerInfo> data ){
        inflater = LayoutInflater.from(context);
        this.typeface = typeface;
        this.data = data;
        vsing = VolleySingleton.getInstance(context);
        imageLoader = vsing.getImageLoader();
    }

    @Override
    public HomeNavigationAstrologerAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.custom_astrologer_recyclerview_row, parent, false);
        HomeNavigationAstrologerAdapter.MyViewHolder myViewHolder = new HomeNavigationAstrologerAdapter.MyViewHolder(view);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(HomeNavigationAstrologerAdapter.MyViewHolder holder, int position) {
        AllAstrologerInfo allAstrologerInfo = data.get(position);
        holder.title.setText(allAstrologerInfo.getAstrologerName());
        holder.content.setText(allAstrologerInfo.getExpertIn());
        holder.icon.setImageUrl(data.get(position).getImageUrl(), imageLoader);
        /*if (current.isSeparator) {
            holder.viewSeparator.setVisibility(View.VISIBLE);
        } else {
            holder.viewSeparator.setVisibility(View.GONE);
        }*/
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title,content;
        NetworkImageView icon;
        LinearLayout viewSeparator;

        public MyViewHolder(View itemView) {
            super(itemView);

            title = (TextView) itemView.findViewById(R.id.listTitle);
            title.setTypeface(typeface);
            content = (TextView) itemView.findViewById(R.id.listSubTitle);
            title.setTypeface(typeface);
            icon = (NetworkImageView) itemView.findViewById(R.id.listIcon);
            viewSeparator = (LinearLayout) itemView.findViewById(R.id.viewSeparator);
        }
    }
}
