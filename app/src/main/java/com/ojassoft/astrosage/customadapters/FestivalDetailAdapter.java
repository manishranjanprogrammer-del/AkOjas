package com.ojassoft.astrosage.customadapters;

import android.content.Context;
import android.graphics.Typeface;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.model.FestivalMuhurat;

import java.util.List;

/**
 * Created by ojas-02 on 31/1/18.
 */

public class FestivalDetailAdapter extends RecyclerView.Adapter<FestivalDetailAdapter.ViewHolder> {
    private List<FestivalMuhurat> muhuratList;
    private Context cxt;
    int langCode;
    Typeface typeface;

    public FestivalDetailAdapter(Context cxt, List<FestivalMuhurat> festivalMuhurats, int language_code, Typeface typeface) {
        this.cxt = cxt;
        this.muhuratList = festivalMuhurats;
        this.langCode = language_code;
        this.typeface=typeface;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.festival_detail_adapter, parent, false);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.tvTopHeading.setText(muhuratList.get(position).getMuhuratBoxHeading());


        if (!muhuratList.get(position).getMuhurat1().trim().isEmpty()) {

            holder.muhuratVal1.setVisibility(View.VISIBLE);
            holder.muhuratVal1.setText(Html.fromHtml(muhuratList.get(position).getMuhurat1()));

        }
        if (!muhuratList.get(position).getMuhurat2().trim().isEmpty()) {

            holder.muhuratVal2.setVisibility(View.VISIBLE);
            holder.muhuratVal2.setText(Html.fromHtml(muhuratList.get(position).getMuhurat2()));

        }
        if (!muhuratList.get(position).getMuhurat3().trim().isEmpty()) {
            holder.muhuratVal3.setVisibility(View.VISIBLE);
            holder.muhuratVal3.setText(Html.fromHtml(muhuratList.get(position).getMuhurat3()));

        }
        if (!muhuratList.get(position).getMuhurat4().trim().isEmpty()) {
            holder.muhuratVal4.setVisibility(View.VISIBLE);
            holder.muhuratVal4.setText(Html.fromHtml(muhuratList.get(position).getMuhurat4()));

        }
        if (!muhuratList.get(position).getMuhurat5().trim().isEmpty()) {
            holder.muhuratVal5.setVisibility(View.VISIBLE);
            holder.muhuratVal5.setText(Html.fromHtml(muhuratList.get(position).getMuhurat5()));

        }
        if (!muhuratList.get(position).getMuhurat6().trim().isEmpty()) {
            holder.muhuratVal6.setVisibility(View.VISIBLE);
            holder.muhuratVal6.setText(Html.fromHtml(muhuratList.get(position).getMuhurat6()));

        }
        if (!muhuratList.get(position).getMuhurat7().trim().isEmpty()) {
            holder.muhuratVal7.setVisibility(View.VISIBLE);
            holder.muhuratVal7.setText(Html.fromHtml(muhuratList.get(position).getMuhurat7()));

        }
        if (!muhuratList.get(position).getMuhurat8().trim().isEmpty()) {
            holder.muhuratVal8.setVisibility(View.VISIBLE);
            holder.muhuratVal8.setText(Html.fromHtml(muhuratList.get(position).getMuhurat8()));

        }
    }

    private String[] findTwoSplitString(String splitedInTwoPart) {

        String arr[] = splitedInTwoPart.split(":", 2);
        return arr;
    }

    @Override
    public int getItemCount() {
        return muhuratList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView muhuratVal1, muhuratVal2, muhuratVal3, muhuratVal4, muhuratVal5, muhuratVal6, muhuratVal7, muhuratVal8;
        public TextView tvTopHeading;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTopHeading = (TextView) itemView.findViewById(R.id.muhuratTopName);


            //value of muhurat
            muhuratVal1 = (TextView) itemView.findViewById(R.id.muhuratValue1);
            muhuratVal2 = (TextView) itemView.findViewById(R.id.muhuratValue2);
            muhuratVal3 = (TextView) itemView.findViewById(R.id.muhuratValue3);
            muhuratVal4 = (TextView) itemView.findViewById(R.id.muhuratValue4);
            muhuratVal5 = (TextView) itemView.findViewById(R.id.muhuratValue5);
            muhuratVal6 = (TextView) itemView.findViewById(R.id.muhuratValue6);
            muhuratVal7 = (TextView) itemView.findViewById(R.id.muhuratValue7);
            muhuratVal8 = (TextView) itemView.findViewById(R.id.muhuratValue8);

            //set typeface

            tvTopHeading.setTypeface(typeface);
            muhuratVal1.setTypeface(typeface);
            muhuratVal2.setTypeface(typeface);
            muhuratVal3.setTypeface(typeface);
            muhuratVal4.setTypeface(typeface);
            muhuratVal5.setTypeface(typeface);
            muhuratVal6.setTypeface(typeface);
            muhuratVal7.setTypeface(typeface);
            muhuratVal8.setTypeface(typeface);
            //end

        }
    }
}
