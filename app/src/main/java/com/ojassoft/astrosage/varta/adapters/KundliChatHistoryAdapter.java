package com.ojassoft.astrosage.varta.adapters;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.bumptech.glide.Glide;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.CircularNetworkImageView;
import com.ojassoft.astrosage.beans.KundliChatHistoryBean;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.FontUtils;

import java.util.ArrayList;

public class KundliChatHistoryAdapter extends RecyclerView.Adapter<KundliChatHistoryAdapter.ViewHolder> {
    ArrayList<KundliChatHistoryBean> historyBeans = new ArrayList<>();
    OnChatItemClick onChatItemClick;
    Context context;
    public KundliChatHistoryAdapter(Context context,OnChatItemClick itemClicked){
        this.onChatItemClick = itemClicked;
        this.context = context;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.kundli_chat_history_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        KundliChatHistoryBean currentItem = historyBeans.get(position);
        holder.tvHeading.setText(currentItem.getTitle());
        holder.tvDesc.setText(Html.fromHtml(currentItem.getDescription()));
        holder.ivAstroPic.setImageResource(R.drawable.ic_new_logo_kkundli_ai);
        holder.tvScreenName.setText(currentItem.getModuleName());
        holder.itemView.setOnClickListener(v -> {
            if(onChatItemClick != null)
                onChatItemClick.onClick(currentItem);
        });
        holder.tvConsultTime.setText(CUtils.getConvertDateFormat(currentItem.getLastConversationDate()));
    }

    @Override
    public int getItemCount() {
        return historyBeans.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{
        CircularNetworkImageView ivAstroPic;
        TextView tvScreenName,tvHeading, tvDesc, tvConsultTime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvHeading = itemView.findViewById(R.id.tv_heading);
            tvScreenName = itemView.findViewById(R.id.tv_screen_name);
            tvDesc = itemView.findViewById(R.id.tv_desc);
            tvConsultTime = itemView.findViewById(R.id.tv_consult_time);
            ivAstroPic = itemView.findViewById(R.id.iv_astrologer_image);
            FontUtils.changeFont(itemView.getContext(), tvHeading, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        }
    }

    public void setHistoryList(ArrayList<KundliChatHistoryBean> list){
        historyBeans = list;
        notifyDataSetChanged();
    }

    public void addHistoryData(ArrayList<KundliChatHistoryBean> list){
        int previousSize = historyBeans.size();
        historyBeans.addAll(list);
        notifyItemRangeChanged(previousSize,list.size());
    }

    @FunctionalInterface
    public interface OnChatItemClick{
        void onClick(KundliChatHistoryBean item);
    }
}
