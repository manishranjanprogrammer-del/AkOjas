package com.ojassoft.astrosage.misc;

import android.content.Context;
import android.graphics.Typeface;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.model.FestDataDetail;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.utils.CUtils;

import java.util.ArrayList;

/**
 * Created by Ankit on 9-5-2019
 */
public class CustomAdapterPanchaknBhadra extends RecyclerView.Adapter<CustomAdapterPanchaknBhadra.ViewHolder> {

    Context cxt;
    ArrayList data;
    Typeface typeFace;
    String[] monthArray;
    String[] weekArray;

    public CustomAdapterPanchaknBhadra(Context context, ArrayList data, String[] monthArray, String[] weekArray) {

        this.cxt = context;
        this.data = data;
        this.monthArray = monthArray;
        this.weekArray = weekArray;
    }

    @Override
    public CustomAdapterPanchaknBhadra.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_view_panchak_bhadra, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        this.typeFace = ((BaseInputActivity) cxt).regularTypeface;

            viewHolder.startDate.setText(CUtils.getDateInFormate(((FestDataDetail) data.get(position)).getFestStartDate(),weekArray,monthArray));
            viewHolder.startDate.setTypeface(typeFace);
            viewHolder.endDate.setText(CUtils.getDateInFormate(((FestDataDetail) data.get(position)).getFestEndDate(),weekArray,monthArray));
            viewHolder.endDate.setTypeface(typeFace);

            viewHolder.startTime.setText(CUtils.getTimeInFormate(((FestDataDetail) data.get(position)).getStarthr(), ((FestDataDetail) data.get(position)).getStartmin()).toUpperCase());
            viewHolder.startTime.setTypeface(typeFace);
            viewHolder.endTime.setText(CUtils.getTimeInFormate(((FestDataDetail) data.get(position)).getEndhr(), ((FestDataDetail) data.get(position)).getEndmin()).toUpperCase());
            viewHolder.endTime.setTypeface(typeFace);

        if (getItemCount() == 0) {
            viewHolder.startDate.setText("-");
            viewHolder.endDate.setText("-");
            viewHolder.startTime.setVisibility(View.GONE);
            viewHolder.endTime.setVisibility(View.GONE);
        }

    }

    @Override
    public int getItemCount() {
        int size = data.size();
        return size;
    }

    /**
     * ViewHolder
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView startDate, startTime;
        public TextView endDate, endTime;
        public LinearLayout ll_main;


        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            startDate = (TextView) itemLayoutView.findViewById(R.id.tv_start_date);
            endDate = (TextView) itemLayoutView.findViewById(R.id.tv_end_date);
            startTime = (TextView) itemLayoutView.findViewById(R.id.tv_start_time);
            endTime = (TextView) itemLayoutView.findViewById(R.id.tv_end_time);
            ll_main = (LinearLayout) itemLayoutView.findViewById(R.id.ll_main);

        }
    }
}
