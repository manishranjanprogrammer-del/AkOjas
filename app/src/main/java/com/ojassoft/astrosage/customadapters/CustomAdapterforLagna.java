package com.ojassoft.astrosage.customadapters;

import android.app.Activity;
import android.graphics.Typeface;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.Festivalapidatum;
import com.ojassoft.astrosage.ui.act.InputPanchangActivity;
import com.ojassoft.astrosage.utils.CUtils;

import java.util.List;

import static com.ojassoft.astrosage.utils.CGlobalVariables.LAGNA_NATURE_DUAL;
import static com.ojassoft.astrosage.utils.CGlobalVariables.LAGNA_NATURE_FIXED;
import static com.ojassoft.astrosage.utils.CGlobalVariables.LAGNA_NATURE_MOVABLE;

public class CustomAdapterforLagna extends RecyclerView.Adapter<CustomAdapterforLagna.ViewHolder> {

    Activity context;
    List<Festivalapidatum> lagnaList;
    Typeface typeFace, robotMediumTypeface;

    public CustomAdapterforLagna(Activity context, List<Festivalapidatum> lagnaList) {
        this.context = context;
        this.typeFace = ((InputPanchangActivity) context).regularTypeface;
        this.robotMediumTypeface = ((InputPanchangActivity) context).robotMediumTypeface;
        this.lagnaList = lagnaList;
    }


    @Override
    public CustomAdapterforLagna.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.activity_lagna_custom_list, parent, false);
        CustomAdapterforLagna.ViewHolder viewHolder = new CustomAdapterforLagna.ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final CustomAdapterforLagna.ViewHolder viewHolder, final int position) {
        if (lagnaList != null) {
            viewHolder.tvLagnaName.setText(lagnaList.get(position).getLagnaName());
            viewHolder.tvLagnaName.setTypeface(robotMediumTypeface);

            if (lagnaList.get(position).getLagnaNatureNum().equalsIgnoreCase(LAGNA_NATURE_MOVABLE)) {
                viewHolder.llLagnaNature.setBackgroundColor(context.getResources().getColor(R.color.bg_movable));
                viewHolder.ivNature.setImageResource(R.drawable.movable);
                viewHolder.tvLagnaNature.setTextColor(context.getResources().getColor(R.color.color_text_movable));

            } else if (lagnaList.get(position).getLagnaNatureNum().equalsIgnoreCase(LAGNA_NATURE_FIXED)) {
                viewHolder.llLagnaNature.setBackgroundColor(context.getResources().getColor(R.color.bg_fixed));
                viewHolder.ivNature.setImageResource(R.drawable.fixed);
                viewHolder.tvLagnaNature.setTextColor(context.getResources().getColor(R.color.color_text_fixed));

            } else if (lagnaList.get(position).getLagnaNatureNum().equalsIgnoreCase(LAGNA_NATURE_DUAL)) {
                viewHolder.llLagnaNature.setBackgroundColor(context.getResources().getColor(R.color.bg_dual));
                viewHolder.ivNature.setImageResource(R.drawable.common);
                viewHolder.tvLagnaNature.setTextColor(context.getResources().getColor(R.color.color_text_dual));
            }
            viewHolder.tvLagnaNature.setText(lagnaList.get(position).getLagnaNature());
            viewHolder.tvLagnaNature.setTypeface(robotMediumTypeface);
            viewHolder.tvStartTime.setText(context.getResources().getString(R.string.start) + ": " + CUtils.getTimeInFormate(lagnaList.get(position).getLagnaStart()).replace("+", context.getString(R.string.tomorrow_label) + "\n"));
            viewHolder.tvStartTime.setTypeface(typeFace);
            viewHolder.tvEndTime.setText(context.getResources().getString(R.string.end) + ": " + CUtils.getTimeInFormate(lagnaList.get(position).getLagnaEnd()).replace("+", context.getString(R.string.tomorrow_label) + "\n"));
            viewHolder.tvEndTime.setTypeface(typeFace);
        }

    }


    @Override
    public int getItemCount() {
        return lagnaList.size();
    }

    /**
     * ViewHolder
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvLagnaName;
        TextView tvLagnaNature;
        TextView tvStartTime;
        TextView tvEndTime;
        LinearLayout llLagnaNature;
        ImageView ivNature;

        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);

            tvLagnaName = itemLayoutView.findViewById(R.id.tv_lagna_name);
            tvLagnaNature = itemLayoutView.findViewById(R.id.tv_lagna_nature);
            tvStartTime = itemLayoutView.findViewById(R.id.tv_start_time);
            tvEndTime = itemLayoutView.findViewById(R.id.tv_end_time);
            llLagnaNature = itemLayoutView.findViewById(R.id.ll_lagna_nature);
            ivNature = itemLayoutView.findViewById(R.id.iv_nature);

        }
    }

}
