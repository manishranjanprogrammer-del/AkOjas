package com.ojassoft.astrosage.varta.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.model.CallHistoryBean;
import com.ojassoft.astrosage.varta.model.GiftModel;
import com.ojassoft.astrosage.varta.ui.activity.AstrologerDescriptionActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.CircularNetworkImageView;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import java.util.ArrayList;

public class GiftHistoryAdapter extends RecyclerView.Adapter<GiftHistoryAdapter.MyViewHolder> {
    Context activity;
    ArrayList<CallHistoryBean> arrayList;
    ArrayList<GiftModel> giftModelArrayList;

    public GiftHistoryAdapter(Context activity, ArrayList<CallHistoryBean> arrayList, ArrayList<GiftModel> giftModelArrayList) {
        this.activity = activity;
        this.arrayList = arrayList;
        this.giftModelArrayList = giftModelArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.gift_history_litst_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CallHistoryBean callHistoryBean = arrayList.get(position);
        holder.astrologerName.setText(callHistoryBean.getAstrologerName());
        if(giftModelArrayList!=null)
        for (int giftItem = 0; giftItem < giftModelArrayList.size(); giftItem++) {
            if (callHistoryBean.getServiceIdId().equals(giftModelArrayList.get(giftItem).getServiceid())) {
                holder.giftName.setText(giftModelArrayList.get(giftItem).getServicename());
                String smallIconFile = "";
                if (giftModelArrayList.get(giftItem).getSmalliconfile() != null && giftModelArrayList.get(giftItem).getSmalliconfile().length() > 0) {
                    smallIconFile = CGlobalVariables.IMAGE_DOMAIN + giftModelArrayList.get(giftItem).getSmalliconfile();
                    Glide.with(activity.getApplicationContext()).load(smallIconFile).into(holder.imageView);
                }
                break;
            }
        }
        holder.dateTV.setText(CUtils.covertDateFormate(callHistoryBean.getConsultationTime()));
        holder.rateTV.setText(activity.getResources().getString(R.string.rs_sign) + callHistoryBean.getCallAmount());

        if(callHistoryBean.getRefundStatus().equalsIgnoreCase("true"))
            holder.refundStatus.setVisibility(View.VISIBLE);
        else
            holder.refundStatus.setVisibility(View.GONE);

        String astrologerProfileUrl = "";
        if (callHistoryBean.getAstrologerImageFile() != null && callHistoryBean.getAstrologerImageFile().length() > 0) {
            astrologerProfileUrl = CGlobalVariables.IMAGE_DOMAIN + callHistoryBean.getAstrologerImageFile();
           // holder.circularNetworkImageView.setImageUrl(astrologerProfileUrl, VolleySingleton.getInstance(activity).getImageLoader());
            Glide.with(activity.getApplicationContext()).load(astrologerProfileUrl).circleCrop().placeholder(R.drawable.ic_profile_view).into(holder.circularNetworkImageView);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList != null ? arrayList.size() : 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircularNetworkImageView circularNetworkImageView;
        TextView astrologerName;
        TextView dateTV;
        TextView giftName;
        TextView rateTV;
        RelativeLayout linearLayout;
        ImageView imageView;
        TextView refundStatus;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            circularNetworkImageView = itemView.findViewById(R.id.ri_profile_img);
            astrologerName = (TextView) itemView.findViewById(R.id.astrologer_name_tv);
            dateTV = (TextView) itemView.findViewById(R.id.time_tv);
            giftName = (TextView) itemView.findViewById(R.id.gift_name);
            imageView = itemView.findViewById(R.id.smallIconFile);
            rateTV = (TextView) itemView.findViewById(R.id.astrologer_price_tv);
            linearLayout = itemView.findViewById(R.id.container);
            refundStatus = (TextView) itemView.findViewById(R.id.refund_status);

            FontUtils.changeFont(activity, astrologerName, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
            FontUtils.changeFont(activity, dateTV, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
            FontUtils.changeFont(activity, giftName, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
            FontUtils.changeFont(activity, rateTV, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
            FontUtils.changeFont(activity, refundStatus, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
            circularNetworkImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openAstrologerDetail(getLayoutPosition());
                }
            });
            astrologerName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    openAstrologerDetail(getLayoutPosition());
                }
            });
        }
    }

    private void openAstrologerDetail(int pos) {
        try {
            Bundle bundle = new Bundle();
            bundle.putString("phoneNumber", arrayList.get(pos).getAstrologerPhoneNo());
            bundle.putString("urlText", arrayList.get(pos).getUrlText());
            Intent intent = new Intent(activity, AstrologerDescriptionActivity.class);
            intent.putExtras(bundle);
            activity.startActivity(intent);
        }catch (Exception e){
            //
        }
    }

    public void historyRecordsUpdate(ArrayList<CallHistoryBean> historyBeans)
    {
        if(historyBeans != null && historyBeans.size()>0) {
            this.arrayList = historyBeans;
        }
        notifyDataSetChanged();
    }

}
