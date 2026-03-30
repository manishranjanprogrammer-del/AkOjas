package com.ojassoft.astrosage.varta.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.model.LangAndExpertiseData;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.FontUtils;

import java.util.ArrayList;

public class FilterAstrologerAdapter extends RecyclerView.Adapter<FilterAstrologerAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<LangAndExpertiseData> filtereDataArrayList;
    private FilterClickCallbacks callBack;
    private int view_id;
    private int type;
    public FilterAstrologerAdapter(Context context, ArrayList<LangAndExpertiseData> filtereDataArrayList,FilterClickCallbacks callBack,int view_id,int type) {
        this.context = context;
        this.filtereDataArrayList = filtereDataArrayList;
        this.callBack = callBack;
        this.view_id = view_id;
        this.type = type;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View  view = LayoutInflater.from(parent.getContext()).inflate(view_id, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") int position) {

        LangAndExpertiseData filterAstrologerModel = filtereDataArrayList.get(position);
        if (filterAstrologerModel != null) {
            holder.tv_astrologer_filter.setText(filterAstrologerModel.getLangName());

            if (filterAstrologerModel.isLangSelected()) {
                holder.tv_astrologer_filter.setBackgroundResource(R.drawable.bg_button_orange_min);
                holder.tv_astrologer_filter.setTextColor(context.getResources().getColor(R.color.white));
            } else {
                holder.tv_astrologer_filter.setBackgroundResource(R.drawable.bg_background);
                holder.tv_astrologer_filter.setTextColor(context.getResources().getColor(R.color.black));
            }

            holder.tv_astrologer_filter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CUtils.regionalFilterType = -1;
                    CUtils.expertiseFilterType = -1;
                    callBack.onFilterItemClick(position, type);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return filtereDataArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_astrologer_filter;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tv_astrologer_filter = itemView.findViewById(R.id.tv_astrologer_filter);
            FontUtils.changeFont(context, tv_astrologer_filter, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        }
    }

     public interface FilterClickCallbacks {
         void  onFilterItemClick(int position,int type);
    }
}