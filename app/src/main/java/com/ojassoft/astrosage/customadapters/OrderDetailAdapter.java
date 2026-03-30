package com.ojassoft.astrosage.customadapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.OrderDetailBean;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;

import java.util.ArrayList;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.MyViewHolder> {

    Context context;
    ArrayList<OrderDetailBean> orderDetailArrayList;


    public OrderDetailAdapter(Context context, ArrayList<OrderDetailBean> orderDetailArrayList) {
        this.context = context;
        this.orderDetailArrayList = orderDetailArrayList;
    }


    @NonNull
    @Override
    public OrderDetailAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.order_detail_item_layout, parent, false);
        return new OrderDetailAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailAdapter.MyViewHolder holder, int position) {
        OrderDetailBean orderDetailBean = orderDetailArrayList.get(position);
        holder.serviceNameTV.setText(orderDetailBean.getOrderName());
        holder.orderDateTV.setText(orderDetailBean.getOrderdate());
        holder.discountPriceValTV.setText("-" + orderDetailBean.getOrderPrice() + "/-");
        holder.ptnrIdEarningValTV.setText("-" + orderDetailBean.getPartnerIdEarning() + "/-");

    }

    @Override
    public int getItemCount() {

        return orderDetailArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView serviceNameTV, orderDateTV, discountPriceTV, ptnrIdEarningTV, discountPriceValTV, ptnrIdEarningValTV;
        LinearLayout containerLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            serviceNameTV = (TextView) itemView.findViewById(R.id.service_name_tv);
            orderDateTV = (TextView) itemView.findViewById(R.id.order_date_tv);
            discountPriceTV = (TextView) itemView.findViewById(R.id.discount_price_tv);
            ptnrIdEarningTV = (TextView) itemView.findViewById(R.id.ptnr_id_earning_tv);
            discountPriceValTV = (TextView) itemView.findViewById(R.id.discount_price_val_tv);
            ptnrIdEarningValTV = (TextView) itemView.findViewById(R.id.ptnr_id_earning_val_tv);
            containerLayout = (LinearLayout) itemView.findViewById(R.id.container);

            serviceNameTV.setTypeface(((BaseInputActivity) context).mediumTypeface);
            orderDateTV.setTypeface(((BaseInputActivity) context).mediumTypeface);
            ptnrIdEarningTV.setTypeface(((BaseInputActivity) context).regularTypeface);
            ptnrIdEarningValTV.setTypeface(((BaseInputActivity) context).mediumTypeface);
            discountPriceTV.setTypeface(((BaseInputActivity) context).regularTypeface);
            discountPriceValTV.setTypeface(((BaseInputActivity) context).mediumTypeface);

        }
    }
}

