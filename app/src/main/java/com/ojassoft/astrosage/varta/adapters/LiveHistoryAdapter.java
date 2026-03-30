package com.ojassoft.astrosage.varta.adapters;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.model.CallHistoryBean;
import com.ojassoft.astrosage.varta.ui.activity.AstrologerDescriptionActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.CircularNetworkImageView;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import java.util.ArrayList;

public class LiveHistoryAdapter extends RecyclerView.Adapter<LiveHistoryAdapter.MyViewHolder> {
    Activity activity;
    ArrayList<CallHistoryBean> arrayList;
    public LiveHistoryAdapter(Activity activity, ArrayList<CallHistoryBean> arrayList) {
        this.activity = activity;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.live_history_litst_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CallHistoryBean callHistoryBean = arrayList.get(position);
        holder.astrologerName.setText(callHistoryBean.getAstrologerName());
        holder.dateTV.setText(CUtils.covertDateFormate(callHistoryBean.getConsultationTime()));
        holder.callRateTV.setText(String.format("@ %s%s/%s",
                activity.getResources().getString(R.string.rs_sign),
                callHistoryBean.getAstrologerServiceRs(),
                activity.getResources().getString(R.string.short_minute)));


        // Determine the duration value and unit label (handles "minute", "second", null, or empty types)
        String unitType = callHistoryBean.getDurationUnitType();
        boolean isMinute = "minute".equalsIgnoreCase(unitType);

        // Minutes use 'getCallDurationMin()'; Seconds/Null/Empty use 'getCallDuration()'
        String durationValue = isMinute ? callHistoryBean.getCallDurationMin() : callHistoryBean.getCallDuration();
        String unitLabel = activity.getString(isMinute ? R.string.minute : R.string.full_second);

        if (isMinute) {
            try {
                // If duration is greater than 1.0, switch to plural "minutes"
                if (!TextUtils.isEmpty(durationValue) && Float.parseFloat(durationValue) > 1.0) {
                    unitLabel = activity.getString(R.string.minutes);
                }
            } catch (Exception e) {
                // Fallback to singular "minute" already set above
            }
        }

        // Set the final formatted text: e.g., "Duration: 5 Minutes" or "Duration: 45 Seconds"
        holder.durationTV.setText(String.format("%s: %s %s",
                activity.getString(R.string.duration),
                durationValue,
                unitLabel));

        holder.rateTV.setText(activity.getResources().getString(R.string.rs_sign) +  callHistoryBean.getCallAmount());

        if(callHistoryBean.getRefundStatus().equalsIgnoreCase("true"))
            holder.refundStatus.setVisibility(View.VISIBLE);
        else
            holder.refundStatus.setVisibility(View.GONE);

        String astrologerProfileUrl = "";
        if (callHistoryBean.getAstrologerImageFile() != null && callHistoryBean.getAstrologerImageFile().length() > 0) {
            astrologerProfileUrl = CGlobalVariables.IMAGE_DOMAIN + callHistoryBean.getAstrologerImageFile();
            //holder.circularNetworkImageView.setImageUrl(astrologerProfileUrl, VolleySingleton.getInstance(activity).getImageLoader());
            Glide.with(activity.getApplicationContext()).load(astrologerProfileUrl).circleCrop().placeholder(R.drawable.ic_profile_view).into(holder.circularNetworkImageView);
        }
    }

    @Override
    public int getItemCount() {
        return arrayList!=null?arrayList.size():0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircularNetworkImageView circularNetworkImageView;
        TextView astrologerName;
        TextView dateTV;
        TextView callRateTV;
        TextView durationTV;
        TextView rateTV;
        TextView callAgainTV;
        TextView refundStatus;
        RelativeLayout linearLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            circularNetworkImageView = itemView.findViewById(R.id.ri_profile_img);
            astrologerName = (TextView) itemView.findViewById(R.id.astrologer_name_tv);
            dateTV = (TextView) itemView.findViewById(R.id.time_tv);
            callRateTV = (TextView) itemView.findViewById(R.id.rate_tv);
            durationTV = (TextView) itemView.findViewById(R.id.duration_tv);
            rateTV = (TextView) itemView.findViewById(R.id.astrologer_price_tv);
            callAgainTV = (TextView) itemView.findViewById(R.id.call_now_btn);
            linearLayout = itemView.findViewById(R.id.container);
            refundStatus = (TextView) itemView.findViewById(R.id.refund_status);

            FontUtils.changeFont(activity, astrologerName, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
            FontUtils.changeFont(activity, dateTV, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
            FontUtils.changeFont(activity, callRateTV, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
            FontUtils.changeFont(activity, durationTV, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
            FontUtils.changeFont(activity, rateTV, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
            FontUtils.changeFont(activity, callAgainTV, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
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
        Bundle bundle = new Bundle();
        bundle.putString("phoneNumber", arrayList.get(pos).getAstrologerPhoneNo());
        bundle.putString("urlText", arrayList.get(pos).getUrlText());
        Intent intent = new Intent(activity, AstrologerDescriptionActivity.class);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    public void historyRecordsUpdate(ArrayList<CallHistoryBean> historyBeans)
    {
        if(historyBeans != null && historyBeans.size()>0) {
            this.arrayList = historyBeans;
        }
        notifyDataSetChanged();
    }

}
