package com.ojassoft.astrosage.customadapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.MyEarningBean;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.act.OrderDetailActivity;

import java.util.ArrayList;

public class EarningAdapter extends RecyclerView.Adapter<EarningAdapter.MyViewHolder> {

    Context context;
    ArrayList<MyEarningBean> earningBeanArrayList;
    boolean isProduct;

    public EarningAdapter(Context context, ArrayList<MyEarningBean> earningBeanArrayList, boolean isProduct) {
        this.context = context;
        this.earningBeanArrayList = earningBeanArrayList;
        this.isProduct = isProduct;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_earning_item_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MyEarningBean earningBean = earningBeanArrayList.get(position);
        holder.monthTV.setText(earningBean.getOrderOfMonthStr() + " " + earningBean.getOrderOfYear());
        holder.orderTV.setText(earningBean.getTotalOrders());
        holder.earnTV.setText(earningBean.getAffiliateEarnings() + "/-");
        if (position % 2 == 0) {
            holder.containerLayout.setBackgroundColor(context.getResources().getColor(R.color.white));
        } else {
            holder.containerLayout.setBackgroundColor(context.getResources().getColor(R.color.gray));
        }
    }

    @Override
    public int getItemCount() {

        return earningBeanArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView monthTV, orderTV, earnTV;
        LinearLayout containerLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            monthTV = (TextView) itemView.findViewById(R.id.month_tv);
            orderTV = (TextView) itemView.findViewById(R.id.order_tv);
            earnTV = (TextView) itemView.findViewById(R.id.earning_tv);
            monthTV.setTypeface(((BaseInputActivity) context).regularTypeface);
            orderTV.setTypeface(((BaseInputActivity) context).regularTypeface);
            earnTV.setTypeface(((BaseInputActivity) context).regularTypeface);

            containerLayout = (LinearLayout) itemView.findViewById(R.id.container);
            containerLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Bundle bundle = new Bundle();
                    Intent intent = new Intent(context, OrderDetailActivity.class);
                    bundle.putBoolean("isProduct", isProduct);
                    bundle.putString("month", earningBeanArrayList.get(getLayoutPosition()).getOrderOfMonth());
                    bundle.putString("year", earningBeanArrayList.get(getLayoutPosition()).getOrderOfYear());
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                }
            });

        }
    }
}
