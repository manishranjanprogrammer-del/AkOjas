package com.ojassoft.astrosage.varta.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.model.CallHistoryBean;
import com.ojassoft.astrosage.varta.ui.activity.AstrologerDescriptionActivity;
import com.ojassoft.astrosage.varta.ui.activity.ConsultantHistoryActivity;
import com.ojassoft.astrosage.varta.ui.fragments.CallHistoryFragment;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.CircularNetworkImageView;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CallHistoryAdapter extends RecyclerView.Adapter<CallHistoryAdapter.MyViewHolder> {

    Context context;
    ArrayList<CallHistoryBean> callHistoryList;

    CallHistoryFragment callHistoryFragment;

    Fragment fragment;

    public CallHistoryAdapter(Context context, ArrayList<CallHistoryBean> callHistoryList, Fragment fragment) {
        this.context = context;
        this.callHistoryList = callHistoryList;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.call_history_litst_item, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        CallHistoryBean callHistoryBean = callHistoryList.get(position);
        holder.astrologerName.setText(callHistoryBean.getAstrologerName());
        holder.dateTV.setText(CUtils.covertDateFormate(callHistoryBean.getConsultationTime()));
        String callRateStr = callHistoryBean.getAstrologerServiceRs();
        if(!TextUtils.isEmpty(callRateStr) && callRateStr.equals("0.0")){
            holder.callRateTV.setText(context.getResources().getString(R.string.text_free));
        } else {
            holder.callRateTV.setText("@ " + context.getResources().getString(R.string.rs_sign) + callRateStr + "/" + context.getResources().getString(R.string.short_minute));
        }

        // Determine the duration value and unit label (handles "minute", "second", null, or empty types)
        String unitType = callHistoryBean.getDurationUnitType();
        boolean isMinute = "minute".equalsIgnoreCase(unitType);

        // Minutes use 'getCallDurationMin()'; Seconds/Null/Empty use 'getCallDuration()'
        String durationValue = isMinute ? callHistoryBean.getCallDurationMin() : callHistoryBean.getCallDuration();
        String unitLabel = context.getString(isMinute ? R.string.minute : R.string.full_second);

        if (isMinute) {
            try {
                // If duration is greater than 1.0, switch to plural "minutes"
                if (!TextUtils.isEmpty(durationValue) && Float.parseFloat(durationValue) > 1.0) {
                    unitLabel = context.getString(R.string.minutes);
                }
            } catch (Exception e) {
                // Fallback to singular "minute" already set above
            }
        }

        // Set the final formatted text: e.g., "Duration: 5 Minutes" or "Duration: 45 Seconds"
        holder.durationTV.setText(String.format("%s: %s %s",
                context.getString(R.string.duration),
                durationValue,
                unitLabel));

        holder.rateTV.setText(context.getResources().getString(R.string.rs_sign) + callHistoryBean.getCallAmount());
        String astrologerProfileUrl = "";

        if(callHistoryBean.getRefundStatus().equalsIgnoreCase("true"))
            holder.refundStatus.setVisibility(View.VISIBLE);
        else
            holder.refundStatus.setVisibility(View.GONE);

        if ( fragment instanceof CallHistoryFragment ) {
            if ( callHistoryBean.getRecordingUrl().equalsIgnoreCase("null") || TextUtils.isEmpty(callHistoryBean.getRecordingUrl()) ) {
                holder.ivPlay.setVisibility(View.GONE);
            } else {
                holder.ivPlay.setVisibility(View.VISIBLE);
            }
        } else {
            holder.ivPlay.setVisibility(View.GONE);
        }

        if (callHistoryBean.getAstrologerImageFile() != null && callHistoryBean.getAstrologerImageFile().length() > 0) {
            astrologerProfileUrl = CGlobalVariables.IMAGE_DOMAIN + callHistoryBean.getAstrologerImageFile();
           // holder.circularNetworkImageView.setImageUrl(astrologerProfileUrl, VolleySingleton.getInstance(context).getImageLoader());
            Glide.with(context.getApplicationContext()).load(astrologerProfileUrl).circleCrop().placeholder(R.drawable.ic_profile_view).into(holder.circularNetworkImageView);
        }

        holder.circularNetworkImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAstrologerDetail(position, v);
            }
        });

        holder.astrologerName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAstrologerDetail(position, v);
            }
        });

        holder.callAgainTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String aiai = callHistoryBean.getAiAstroId();
                    if(!TextUtils.isEmpty(aiai) && !aiai.equals("0")){
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_AI_CALL_AGAIN_CLICK, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                    }else {
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_CALL_AGAIN_CLICK, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                    }
                    com.ojassoft.astrosage.utils.CUtils.createSession(context, com.ojassoft.astrosage.utils.CGlobalVariables.CONSULT_HISTORY_CALL_BTN_PARTNER_ID);
                    openAstrologerDetail(position, v);
                } catch (Exception e){
                    //
                }
            }
        });

        holder.ivPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(fragment != null ) {
                    if ( fragment instanceof CallHistoryFragment ){
                        ((CallHistoryFragment) fragment).playAudio(callHistoryBean.getCallChatId(),callHistoryBean.getAstrologerName(),callHistoryBean.getProvider(), callHistoryBean.getRecordingUrl());
                    } else {
                        //Log.e("SAN ", " CHA image view fragment != null else " );
                    }
                } else {
                    //Log.e("SAN ", " CHA image view else " );
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return callHistoryList.size();
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

        ImageView ivPlay;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            circularNetworkImageView = itemView.findViewById(R.id.ri_profile_img);
            astrologerName = (TextView) itemView.findViewById(R.id.astrologer_name_tv);
            dateTV = (TextView) itemView.findViewById(R.id.time_tv);
            callRateTV = (TextView) itemView.findViewById(R.id.rate_tv);
            durationTV = (TextView) itemView.findViewById(R.id.duration_tv);
            rateTV = (TextView) itemView.findViewById(R.id.astrologer_price_tv);
            callAgainTV = (TextView) itemView.findViewById(R.id.call_now_btn);
            refundStatus = (TextView) itemView.findViewById(R.id.refund_status);
            linearLayout = itemView.findViewById(R.id.container);
            ivPlay = itemView.findViewById(R.id.iv_play);

            FontUtils.changeFont(context, astrologerName, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
            FontUtils.changeFont(context, dateTV, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
            FontUtils.changeFont(context, callRateTV, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
            FontUtils.changeFont(context, durationTV, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
            FontUtils.changeFont(context, rateTV, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
            FontUtils.changeFont(context, callAgainTV, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
            FontUtils.changeFont(context, refundStatus, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        }
    }

    private void openAstrologerDetail(int pos, View view) {
        Bundle bundle = new Bundle();
        String phoneNumber = callHistoryList.get(pos).getAstrologerPhoneNo();
        String urlText = callHistoryList.get(pos).getUrlText();
        Log.d("ConsultHistry", "call Again click : aiid = " + callHistoryList.get(pos).getAiAstroId() +  " urlText = " +urlText);
        try {
            bundle.putString("phoneNumber", phoneNumber);
            bundle.putString("urlText", urlText);
            Intent intent = new Intent(context, AstrologerDescriptionActivity.class);
            intent.putExtras(bundle);
            context.startActivity(intent);
        } catch (Exception e) {

        }
    }

    public void historyRecordsUpdate(ArrayList<CallHistoryBean> historyBeans) {
        if(historyBeans != null && historyBeans.size()>0) {
            this.callHistoryList = historyBeans;
        }
        notifyDataSetChanged();
    }

}
