package com.ojassoft.astrosage.customadapters;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.constants.AppConstants;
import com.ojassoft.astrosage.model.NameGunaMilanModel;
import com.ojassoft.astrosage.utils.indnotes.FontUtils;

import java.util.List;

public class NameMatchingDetailAdapter extends RecyclerView.Adapter<NameMatchingDetailAdapter.MyViewHolder> {

    private Context context;
    private List<NameGunaMilanModel> milanModelList;

    public NameMatchingDetailAdapter(Context context, List<NameGunaMilanModel> milanModelList) {
        this.milanModelList = milanModelList;
        this.context = context;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_gun_detail, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        if (milanModelList != null && milanModelList.size() > position) {
            NameGunaMilanModel milanModel = milanModelList.get(position);
            if (milanModel == null) {
                return;
            }

            holder.valGunTV.setText(milanModel.getGun());
            holder.valMaxOptdTV.setText(milanModel.getMaxObtain());
            holder.valOptdPointTV.setText(milanModel.getObtainPoint());
            holder.valLifeAreaTV.setText(milanModel.getAreaLife());

        }
    }

    @Override
    public int getItemCount() {
        if (milanModelList == null) return 0;
        return milanModelList.size();
    }

    public int getItemViewType(int position) {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        public TextView valMaxOptdTV;
        public TextView valGunTV;
        public TextView valOptdPointTV;
        private TextView valLifeAreaTV;

        public MyViewHolder(View view) {
            super(view);

            valMaxOptdTV = view.findViewById(R.id.valMaxOptdTV);
            valGunTV = view.findViewById(R.id.valGunTV);
            valOptdPointTV = view.findViewById(R.id.valOptdPointTV);
            valLifeAreaTV= view.findViewById(R.id.valLifeAreaTV);
            
            FontUtils.changeFont(context, valGunTV, AppConstants.FONT_ROBOTO_MEDIUM);
            FontUtils.changeFont(context, valMaxOptdTV, AppConstants.FONT_ROBOTO_MEDIUM);
            FontUtils.changeFont(context, valOptdPointTV, AppConstants.FONT_ROBOTO_MEDIUM);
            FontUtils.changeFont(context, valLifeAreaTV, AppConstants.FONT_ROBOTO_MEDIUM);
        }
    }
}
