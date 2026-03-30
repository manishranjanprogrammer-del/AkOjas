package com.ojassoft.astrosage.model;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ojassoft.astrosage.R;

import java.util.ArrayList;

public class CloudTopUpPriceItem extends RecyclerView.Adapter<CloudTopUpPriceItem.MyViewHolder> {
    ArrayList<CloudTopupModel> cloudTopupModels;
    Activity activity;
    TopUpAmountSelectedCallback topUpAmountSelectedCallback;
    int selectedPricePosition = -1;

    public CloudTopUpPriceItem(ArrayList<CloudTopupModel> cloudTopupModels, Activity activity, TopUpAmountSelectedCallback topUpAmountSelectedCallback) {
        this.cloudTopupModels = cloudTopupModels;
        this.activity = activity;
        this.topUpAmountSelectedCallback = topUpAmountSelectedCallback;
    }

    @NonNull
    @Override
    public CloudTopUpPriceItem.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.top_up_amount_item_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CloudTopUpPriceItem.MyViewHolder holder, int position) {

        CloudTopupModel item = cloudTopupModels.get(position);
        String topUpPrice = activity.getString(R.string.astroshop_rupees_sign) + item.getRechargeAmount();
        holder.topUpPriceTV.setText(topUpPrice);
        holder.topUpPriceTV.setSelected(item.isSelected);
        holder.topUpPriceTV.setOnClickListener(v -> {
            CloudTopupModel selectedItem = cloudTopupModels.get(holder.getAbsoluteAdapterPosition());
            if (selectedItem.isSelected) {
                return;
            }
            if (topUpAmountSelectedCallback != null) {
                selectedItem.setSelected(!selectedItem.isSelected);
                if (selectedPricePosition != -1) {
                    cloudTopupModels.get(selectedPricePosition).setSelected(false);
                    notifyItemChanged(selectedPricePosition);
                }
                selectedPricePosition = holder.getAbsoluteAdapterPosition();
                notifyItemChanged(selectedPricePosition);
                topUpAmountSelectedCallback.onAmountSelected(holder.getAbsoluteAdapterPosition());
            }
        });
    }

    @Override
    public int getItemCount() {
        return cloudTopupModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView topUpPriceTV;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            topUpPriceTV = itemView.findViewById(R.id.top_up_price_tv);
        }
    }

    public static interface TopUpAmountSelectedCallback {
        void onAmountSelected(int pos);
    }
}
