package com.ojassoft.astrosage.varta.adapters;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.model.RechargeHistoryBean;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.FontUtils;

import java.util.ArrayList;

public class RechargeHistoryAdapter extends RecyclerView.Adapter<RechargeHistoryAdapter.MyViewHolder> {

    Context context;
    ArrayList<RechargeHistoryBean> rechargeHistoryBeanArrayList;

    public RechargeHistoryAdapter(Context context, ArrayList<RechargeHistoryBean> rechargeHistoryBeanArrayList) {
        this.context = context;
        this.rechargeHistoryBeanArrayList = rechargeHistoryBeanArrayList;
    }

    @NonNull
    @Override
    public RechargeHistoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recharge_history_list_item, parent, false);
        return new RechargeHistoryAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RechargeHistoryAdapter.MyViewHolder holder, int position) {
        // Get the RechargeHistoryBean object for the current position
        RechargeHistoryBean rechargeHistoryBean = rechargeHistoryBeanArrayList.get(position);

        // Set the display message for the transaction
        holder.addedToWalletTV.setText(rechargeHistoryBean.getDisplayMsg());

        // Set the recharge amount, formatting it with the currency symbol
        holder.amountTV.setText(context.getResources().getString(R.string.rs_sign) + "\n" + rechargeHistoryBean.getRechargeAmount());

        // Set the transaction time, converting it to the desired date format
        holder.timeTV.setText(CUtils.covertDateFormate(rechargeHistoryBean.getRechargeDateTime()));

        // Check if the order ID is "null"
        if (rechargeHistoryBean.getOrderId().equals("null")) {
            // If order ID is "null", check if there's a custom message
            if (!TextUtils.isEmpty(rechargeHistoryBean.getReferralMsg())&&!rechargeHistoryBean.getReferralMsg().equals("null")) {
                // If a custom message exists, display it as the order ID
                holder.orderId.setText(rechargeHistoryBean.getReferralMsg());
            } else {
                // If no custom message, hide the order ID view
                holder.orderId.setVisibility(View.INVISIBLE);
            }
        } else {
            // If order ID is not "null", make the order ID view visible
            holder.orderId.setVisibility(View.VISIBLE);
            // Display the order ID with a label
            holder.orderId.setText(context.getResources().getString(R.string.orderid) + " " + rechargeHistoryBean.getOrderId());
        }
    }

    @Override
    public int getItemCount() {
        return rechargeHistoryBeanArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView addedToWalletTV;
        TextView amountTV;
        TextView timeTV;
        TextView orderId;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            addedToWalletTV = (TextView) itemView.findViewById(R.id.added_to_wallet_tv);
            amountTV = (TextView) itemView.findViewById(R.id.amount_tv);
            timeTV = (TextView) itemView.findViewById(R.id.recharge_date);
            orderId = (TextView) itemView.findViewById(R.id.order_id);

            FontUtils.changeFont(context, addedToWalletTV, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
            FontUtils.changeFont(context, amountTV, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
            FontUtils.changeFont(context, timeTV, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
            FontUtils.changeFont(context, orderId, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);

        }
    }

    public void historyRecordsUpdate(ArrayList<RechargeHistoryBean> historyBeans)
    {
        if(historyBeans != null && historyBeans.size()>0) {
            this.rechargeHistoryBeanArrayList = historyBeans;
        }
        notifyDataSetChanged();
    }

}
