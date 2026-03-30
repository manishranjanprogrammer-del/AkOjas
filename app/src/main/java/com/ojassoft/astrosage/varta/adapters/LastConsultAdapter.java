package com.ojassoft.astrosage.varta.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.model.AstrologerDetailBean;
import com.ojassoft.astrosage.varta.model.CallHistoryBean;
import com.ojassoft.astrosage.varta.twiliochat.chat.history.ChatHistoryNew;
import com.ojassoft.astrosage.varta.ui.activity.AstrologerDescriptionActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.ChatUtils;
import com.ojassoft.astrosage.varta.utils.CircularNetworkImageView;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import java.util.ArrayList;

public class LastConsultAdapter extends RecyclerView.Adapter<LastConsultAdapter.MyViewHolder> {

    private final Context context;
    private final ArrayList<CallHistoryBean> callHistoryList;
    private final int fromFragment; /*1 == KundliModules_Frag, 2 == VartaHomeFragment*/
    private static final int HOME_CONSULTATION_LIST_LIMIT = 30;

    public LastConsultAdapter(Context context, ArrayList<CallHistoryBean> callHistoryList, int fromFragment) {
        this.context = context;
        this.callHistoryList = callHistoryList;
        this.fromFragment = fromFragment;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if ( getItemCount() == 1 ){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_last_consultation_full, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_last_consultation_home, parent, false);
        }
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final int pos = position;
        CallHistoryBean callHistoryBean = callHistoryList.get(pos);
        String astroName = callHistoryBean.getAstrologerName();
        try {
            if (!callHistoryBean.getCallChatId().startsWith("FCHAI")) {
                astroName = CUtils.removeAcharyaTarot(astroName);
            }
        } catch (Exception e){

        }
        if (astroName != null) {
            astroName = astroName.trim();
        }
        holder.astrologerName.setText(astroName);
        holder.dateTV.setText(CUtils.covertDateFormat(callHistoryBean.getConsultationTime()));
        String callRateStr = callHistoryBean.getAstrologerServiceRs();
        String astrologerProfileUrl = "";
        if (callHistoryBean.getType().equalsIgnoreCase("Call")) {
            holder.callAgain.setVisibility(View.VISIBLE);;
            holder.btnViewChat.setVisibility(View.GONE);
            holder.chatAgain.setVisibility(View.GONE);
        } else if (callHistoryBean.getType().equalsIgnoreCase("Chat")) {
            holder.callAgain.setVisibility(View.GONE);;
            holder.btnViewChat.setVisibility(View.VISIBLE);
            holder.chatAgain.setVisibility(View.VISIBLE);
        }

        if (callHistoryBean.getAstrologerImageFile() != null && callHistoryBean.getAstrologerImageFile().length() > 0) {
            astrologerProfileUrl = CGlobalVariables.IMAGE_DOMAIN + callHistoryBean.getAstrologerImageFile();
            //holder.circularNetworkImageView.setImageUrl(astrologerProfileUrl, VolleySingleton.getInstance(context).getImageLoader());
            Glide.with(holder.circularNetworkImageView).load(astrologerProfileUrl).circleCrop().placeholder(R.drawable.ic_profile_view).into(holder.circularNetworkImageView);
        }

//        holder.circularNetworkImageView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openAstrologerDetail(pos, v);
//            }
//        });
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openAstrologerDetail(pos, v);
            }
        });
//        holder.astrologerName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openAstrologerDetail(pos, v);
//            }
//        });
        holder.callAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (fromFragment == 1){
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_AK_HOME_CALL_AGAIN_CLICK, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                        com.ojassoft.astrosage.utils.CUtils.createSession(context, CGlobalVariables.AK_HOME_CALL_AGAIN_PARTNER_ID);
                    } else if (fromFragment == 2){
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_VARTA_HOME_CALL_AGAIN_CLICK, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                        com.ojassoft.astrosage.utils.CUtils.createSession(context, CGlobalVariables.VARTA_HOME_CALL_AGAIN_PARTNER_ID);
                    }
                    openAstrologerDetail(pos, v);
                } catch (Exception e) {
                    //
                }
            }
        });
        holder.btnViewChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent openChatHistory = new Intent(context, ChatHistoryNew.class);
                openChatHistory.putExtra(CGlobalVariables.CHANNEL_ID, callHistoryBean.getCallChatId());
                openChatHistory.putExtra(CGlobalVariables.ASTROLOGER_NAME, callHistoryBean.getAstrologerName());
                openChatHistory.putExtra(CGlobalVariables.ASTROLOGER_PROFILE_PIC, callHistoryBean.getAstrologerImageFile());
                openChatHistory.putExtra("astrologerphone",callHistoryBean.getAstrologerPhoneNo());
                openChatHistory.putExtra(CGlobalVariables.URL_TEXT,callHistoryBean.getUrlText());
                openChatHistory.putExtra(CGlobalVariables.CALL_HISTORY_BEAN, callHistoryBean); //for initiate chat
                context.startActivity(openChatHistory);
            }
        });
        holder.chatAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (fromFragment == 1){
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_AK_HOME_CHAT_AGAIN_CLICK, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                        com.ojassoft.astrosage.utils.CUtils.createSession(context, CGlobalVariables.AK_HOME_CHAT_AGAIN_PARTNER_ID);
                    } else if (fromFragment == 2){
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_VARTA_HOME_CHAT_AGAIN_CLICK, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                        com.ojassoft.astrosage.utils.CUtils.createSession(context, CGlobalVariables.VARTA_HOME_CHAT_AGAIN_PARTNER_ID);
                    }
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_CHAT_AGAIN_CLICK, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                    com.ojassoft.astrosage.utils.CUtils.createSession(context, com.ojassoft.astrosage.utils.CGlobalVariables.CONSULT_HISTORY_CHAT_BTN_PARTNER_ID);
                    //openAstrologerDetail(pos, v);

                    processChatAgain(pos,v);
                } catch (Exception e) {
                    //
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        int count = 0;
        if (callHistoryList != null) {
            count = Math.min(callHistoryList.size(), HOME_CONSULTATION_LIST_LIMIT);
        }
        return count;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        CircularNetworkImageView circularNetworkImageView;
        TextView astrologerName;
        TextView dateTV;
        LinearLayout linearLayout;
        LinearLayout llChat;
        LinearLayout chatAgain, btnViewChat, callAgain;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            circularNetworkImageView = itemView.findViewById(R.id.ri_profile_img);
            astrologerName = (TextView) itemView.findViewById(R.id.astrologer_name_tv);
            dateTV = (TextView) itemView.findViewById(R.id.time_tv);
            callAgain = itemView.findViewById(R.id.call_now_btn);
            linearLayout = itemView.findViewById(R.id.container);
            llChat = itemView.findViewById(R.id.ll_chat);
            chatAgain = itemView.findViewById(R.id.chat_again_btn);
            btnViewChat = itemView.findViewById(R.id.btnViewChat);

            FontUtils.changeFont(context, astrologerName, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
            FontUtils.changeFont(context, dateTV, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
            //FontUtils.changeFont(context, callAgain, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        }
    }

    private void openAstrologerDetail(int pos, View view) {
        try {
            Bundle bundle = new Bundle();
            String phoneNumber = callHistoryList.get(pos).getAstrologerPhoneNo();
            String urlText = callHistoryList.get(pos).getUrlText();
            bundle.putString("phoneNumber", phoneNumber);
            bundle.putString("urlText", urlText);
            Intent intent = new Intent(context, AstrologerDescriptionActivity.class);
            intent.putExtras(bundle);
            context.startActivity(intent);
        } catch (Exception e) {

        }
    }

    private void processChatAgain(int pos, View v){
        try {
            try {
                CallHistoryBean callHistoryBean = callHistoryList.get(pos);
                AstrologerDetailBean astrologerDetailBean = new AstrologerDetailBean();
                astrologerDetailBean.setAiAstrologerId(callHistoryBean.getAiAstroId());
                if (CUtils.isAiAstrologer(astrologerDetailBean)) {
                    astrologerDetailBean.setName(callHistoryBean.getAstrologerName());
                    astrologerDetailBean.setImageFile(callHistoryBean.getAstrologerImageFile());
                    astrologerDetailBean.setImageFileLarge(callHistoryBean.getAstroImageFileLarge());
                    astrologerDetailBean.setAstroWalletId(callHistoryBean.getAstroWalletId());
                    astrologerDetailBean.setUrlText(callHistoryBean.getUrlText());
                    astrologerDetailBean.setDesignation(callHistoryBean.getAstroExpertise());
                    String className = CUtils.getActivityName(context);
                    if(TextUtils.isEmpty(className)){className = "";}
                    astrologerDetailBean.setCallSource(className);
                    astrologerDetailBean.setAiAstrologerId(callHistoryBean.getAiAstroId());
                    astrologerDetailBean.setAstrologerId(callHistoryBean.getAstroWalletId());
                    String offerType = CUtils.getCallChatOfferType(context);
                    if(callHistoryBean.isFreeForChat() && !TextUtils.isEmpty(offerType)) {
                        astrologerDetailBean.setFreeForChat(true);
                        astrologerDetailBean.setUseIntroOffer(true);
                    }
                    if (com.ojassoft.astrosage.varta.utils.CUtils.isChatNotInitiated() && !TextUtils.isEmpty(astrologerDetailBean.getAiAstrologerId())) {
                        ChatUtils.getInstance((Activity) context).initAIChat(astrologerDetailBean);
                    } else {
                        Toast.makeText(context, context.getResources().getString(R.string.allready_in_chat), Toast.LENGTH_SHORT).show();
                    }

                    return;
                }
            }catch (Exception e){
                //
            }
            openAstrologerDetail(pos, v);
        }catch (Exception e){
            //
        }
    }
}
