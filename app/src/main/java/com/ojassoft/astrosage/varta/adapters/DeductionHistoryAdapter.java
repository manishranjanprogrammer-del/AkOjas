package com.ojassoft.astrosage.varta.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.model.DeductionHistoryBean;
import com.ojassoft.astrosage.varta.model.RechargeHistoryBean;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.FontUtils;

import java.util.ArrayList;

public class DeductionHistoryAdapter extends RecyclerView.Adapter<DeductionHistoryAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<DeductionHistoryBean> deductionList;

    public DeductionHistoryAdapter(Context context, ArrayList<DeductionHistoryBean> deductionList) {
        this.context = context;
        this.deductionList = deductionList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.recharge_history_list_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        DeductionHistoryBean deductionHistoryBean = deductionList.get(position);

        Log.e("SAN ", "CHA response parse consultationId " + deductionHistoryBean.getDeductedTime() );
        holder.addedToWalletTV.setText(deductionHistoryBean.getDisplayMsg());
        holder.amountTV.setText(context.getResources().getString(R.string.rs_sign) + "\n" + deductionHistoryBean.getDeductedAmount());
        holder.timeTV.setText(deductionHistoryBean.getDeductedTime());
    }

    @Override
    public int getItemCount() {
        return deductionList != null ? deductionList.size() : 0;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView addedToWalletTV, amountTV, timeTV, orderId;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            addedToWalletTV = itemView.findViewById(R.id.added_to_wallet_tv);
            amountTV = itemView.findViewById(R.id.amount_tv);
            timeTV = itemView.findViewById(R.id.recharge_date);
            orderId = itemView.findViewById(R.id.order_id);
            orderId.setVisibility(View.GONE);

            // Apply custom fonts if required
            FontUtils.changeFont(itemView.getContext(), addedToWalletTV, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
            FontUtils.changeFont(itemView.getContext(), amountTV, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
            FontUtils.changeFont(itemView.getContext(), timeTV, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
            FontUtils.changeFont(itemView.getContext(), orderId, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        }
    }

    public void historyRecordsUpdate(ArrayList<DeductionHistoryBean> historyBeans)
    {
        if(historyBeans != null && historyBeans.size()>0) {
            this.deductionList = historyBeans;
        }
        notifyDataSetChanged();
    }

}
