package com.ojassoft.astrosage.misc;



import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.model.DownloadSourceModel;
import com.ojassoft.astrosage.ui.act.AppExitActivity;

import java.util.ArrayList;

public class DownloadSourceAdapter extends RecyclerView.Adapter<DownloadSourceAdapter.ViewHolder>{
    ArrayList<DownloadSourceModel> list ;
    Activity context;
    public DownloadSourceAdapter(Activity context,ArrayList<DownloadSourceModel> list){
        this.list = list;
        this.context = context;
    }
    @NonNull
    @Override
    public DownloadSourceAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.download_source_item, parent, false);
        return new DownloadSourceAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DownloadSourceAdapter.ViewHolder holder, int position) {
        try {
            holder.name.setText(list.get(position).getSourceNam());
            holder.icon.setImageResource(list.get(position).getSourceIcon());
            holder.itemView.setOnClickListener(v -> {
                if (!list.get(position).isSelected()){
                    for (DownloadSourceModel model:list) {
                        model.setSelected(false);
                    }
                    list.get(position).setSelected(true);
                }else {
                    list.get(position).setSelected(false);
                }
                notifyDataSetChanged();
                ((AppExitActivity)context).itemSelected(list.get(position));
            });
            if(list.get(position).isSelected()){
                holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context,R.color.colorPrimary_day_night));
                holder.name.setTextColor(ContextCompat.getColor(context,R.color.white));
            }else{
                holder.cardView.setCardBackgroundColor(ContextCompat.getColor(context,R.color.bg_card_exit_color));
                holder.name.setTextColor(ContextCompat.getColor(context,R.color.black));
            }

        }catch (Exception ignore){}
    }

    @Override
    public int getItemCount() {
        return this.list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView icon;
        TextView name;
        CardView cardView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.image_icon);
            name = itemView.findViewById(R.id.tv_name);
            cardView = itemView.findViewById(R.id.card_view);
        }

    }
}
