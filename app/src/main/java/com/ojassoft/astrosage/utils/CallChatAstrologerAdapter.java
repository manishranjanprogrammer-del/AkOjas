package com.ojassoft.astrosage.utils;

import static com.ojassoft.astrosage.ui.act.ActAppModule.LANGUAGE_CODE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_KUNDLI_HOME_ONLINE_ASTRO_CARD;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KUNDLI_HOME_ONLINE_ASTRO_CARD_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CUtils.createSession;
import static com.ojassoft.astrosage.utils.CUtils.fcmAnalyticsEvents;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.FILTER_TYPE_CALL;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.FILTER_TYPE_CHAT;
import static com.ojassoft.astrosage.varta.utils.CUtils.removeAcharyaTarot;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.toolbox.NetworkImageView;
import com.bumptech.glide.Glide;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.varta.interfacefile.LoadMoreList;
import com.ojassoft.astrosage.varta.model.AstrologerDetailBean;
import com.ojassoft.astrosage.varta.ui.activity.AstrologerDescriptionActivity;
import com.ojassoft.astrosage.varta.ui.activity.DashBoardActivity;
import com.ojassoft.astrosage.varta.ui.activity.LoginSignUpActivity;
import com.ojassoft.astrosage.varta.ui.fragments.VartaHomeFragment;
import com.ojassoft.astrosage.varta.utils.CircularNetworkImageView;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import java.util.ArrayList;

public class CallChatAstrologerAdapter extends RecyclerView.Adapter<CallChatAstrologerAdapter.MyViewHolder> {
    private Activity activity;
    private ArrayList<AstrologerDetailBean> astrologerDetailBeanArrayList;

    boolean isAIAstro;


    public CallChatAstrologerAdapter(Activity activity, ArrayList<AstrologerDetailBean> astrologerDetailBeanArrayList) {
        this.activity = activity;
        this.astrologerDetailBeanArrayList = astrologerDetailBeanArrayList;
    }

    public void setIsAi(boolean isAIAstro) {
        this.isAIAstro = isAIAstro;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.astro_list_horizontal_new, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int pos) {
        final int position = pos;
        AstrologerDetailBean astrologerDetailBean = astrologerDetailBeanArrayList.get(position);
        if (astrologerDetailBean == null) return;
        String astrologerProfileUrl = "";
        if (astrologerDetailBean.getImageFile() != null && astrologerDetailBean.getImageFile().length() > 0) {
            astrologerProfileUrl = com.ojassoft.astrosage.varta.utils.CGlobalVariables.IMAGE_DOMAIN + astrologerDetailBean.getImageFile();
            //holder.riProfileImg.setImageUrl(astrologerProfileUrl, VolleySingleton.getInstance(activity).getImageLoader());
            //Glide.with(activity).load(astrologerProfileUrl).into(holder.ri_profile_img);
            Glide.with(holder.ri_profile_img).load(astrologerProfileUrl).circleCrop().placeholder(R.drawable.ic_profile_view).into(holder.ri_profile_img);

        }

        holder.txtAstroName.setTypeface(CUtils.getRobotoFont(activity, LANGUAGE_CODE, CGlobalVariables.medium));
        holder.txtAstroName.setTextSize(14);
        holder.txtAstroName.setText(removeAcharyaTarot(astrologerDetailBean.getName()));
        int actualPrice = 0;
        if (astrologerDetailBean.getActualServicePriceInt() != null && astrologerDetailBean.getActualServicePriceInt().length() > 0) {
            actualPrice = Integer.parseInt(astrologerDetailBean.getActualServicePriceInt());
        }

        int servicePrice = 0;
        if (astrologerDetailBean.getServicePrice() != null && astrologerDetailBean.getServicePrice().length() > 0) {
            servicePrice = Integer.parseInt(astrologerDetailBean.getServicePrice());
        }
        if (actualPrice > servicePrice) {
            //show both prices
            String price = astrologerDetailBean.getActualServicePriceInt();
            holder.priceTv1.setVisibility(View.GONE);
            com.ojassoft.astrosage.varta.utils.CUtils.setStrikeOnTextView(holder.priceTv1, price);
            //holder.priceTv1.setText(price);
            //holder.priceTv1.setPaintFlags(holder.priceTv1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            String quePrice = activity.getResources().getString(R.string.question_price).replace("$", astrologerDetailBean.getServicePrice());
            holder.priceTv2.setVisibility(View.VISIBLE);
            holder.priceTv2.setText(quePrice);
            holder.ruppee_sign.setVisibility(View.VISIBLE);

        } else {
            // show only one price(servicePriceInt)
            String quePrice = activity.getResources().getString(R.string.question_price).replace("$", astrologerDetailBean.getServicePrice());
            holder.ruppee_sign.setVisibility(View.VISIBLE);
            holder.priceTv2.setVisibility(View.VISIBLE);
            holder.priceTv2.setText(quePrice);
            holder.priceTv1.setVisibility(View.GONE);


        }
        holder.llAstroListDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isAIAstro) {
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.HOME_AI_ASTROLOGER_LIST_ITEM_CLICK, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "Home screen");

                } else {
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.HOME_ASTROLOGER_LIST_ITEM_CLICK, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "Home screen");
                }
                openAstrologerDetail(pos, v);
            }
        });
//        holder.txtAstroName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                openAstrologerDetail(pos, v);
//            }
//        });
        if ((isAIAstro || !astrologerDetailBean.getAiAstrologerId().equals("0")) && com.ojassoft.astrosage.varta.utils.CUtils.isKundliAiProPlan(activity)) {
            holder.newUser.setVisibility(View.VISIBLE);
            holder.newUser.setText(activity.getResources().getString(R.string.text_free));
            holder.newUser.setVisibility(View.VISIBLE);
            String quePrice = activity.getResources().getString(R.string.question_price).replace("$", astrologerDetailBean.getServicePrice());
            com.ojassoft.astrosage.varta.utils.CUtils.setStrikeOnTextView(holder.priceTv1, quePrice);
            //holder.priceTv1.setText(quePrice);
            //holder.priceTv1.setPaintFlags(holder.priceTv1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.priceTv1.setVisibility(View.GONE);
            holder.priceTv2.setVisibility(View.GONE);
            holder.ruppee_sign.setVisibility(View.GONE);
        } else {
            if (astrologerDetailBean.getUseIntroOffer()) {
                holder.newUser.setVisibility(View.VISIBLE);
                String offerType = astrologerDetailBean.getIntroOfferType();
                if (!TextUtils.isEmpty(offerType) && offerType.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.INTRO_OFFER_TYPE_FREE)) {
                    holder.newUser.setText(activity.getResources().getString(R.string.text_free));
                    holder.newUser.setVisibility(View.VISIBLE);
                    String quePrice = activity.getResources().getString(R.string.question_price).replace("$", astrologerDetailBean.getServicePrice());
                    com.ojassoft.astrosage.varta.utils.CUtils.setStrikeOnTextView(holder.priceTv1, quePrice);
                    //holder.priceTv1.setText(quePrice);
                    //holder.priceTv1.setPaintFlags(holder.priceTv1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.priceTv1.setVisibility(View.GONE);
                    holder.priceTv2.setVisibility(View.GONE);
                    holder.ruppee_sign.setVisibility(View.GONE);
                } else {
                    holder.newUser.setVisibility(View.GONE);
                    //holder.newUser.setText(activity.getResources().getString(R.string.new_user));
                }

            } else {
                holder.newUser.setVisibility(View.GONE);
                holder.ruppee_sign.setVisibility(View.VISIBLE);
                holder.priceTv1.setVisibility(View.GONE);
                String price = astrologerDetailBean.getActualServicePriceInt();
                com.ojassoft.astrosage.varta.utils.CUtils.setStrikeOnTextView(holder.priceTv1, price);
                //holder.priceTv1.setPaintFlags(holder.priceTv1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            }
        }


    }

    @Override
    public int getItemCount() {
        return astrologerDetailBeanArrayList != null ? astrologerDetailBeanArrayList.size() : 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtAstroName, newUser, ruppee_sign, priceTv1, priceTv2;
        CircularNetworkImageView ri_profile_img;
        LinearLayout llAstroListDashboard;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            txtAstroName = itemView.findViewById(R.id.txtAstroName);
            ruppee_sign = itemView.findViewById(R.id.ruppee_sign);
            priceTv1 = itemView.findViewById(R.id.priceTv1);
            priceTv2 = itemView.findViewById(R.id.priceTv2);
            newUser = itemView.findViewById(R.id.new_user);
            ri_profile_img = itemView.findViewById(R.id.ri_profile_img);
            llAstroListDashboard = itemView.findViewById(R.id.llAstroListDashboard);

        }
    }

    private void openAstrologerDetail(int pos, View view) {
        try {
            Bundle bundle = new Bundle();
            String phoneNumber = astrologerDetailBeanArrayList.get(pos).getPhoneNumber();
            String urlText = astrologerDetailBeanArrayList.get(pos).getUrlText();
            bundle.putString("phoneNumber", phoneNumber);
            bundle.putString("urlText", urlText);
            boolean introOffer = astrologerDetailBeanArrayList.get(pos).getUseIntroOffer();
            bundle.putInt("useIntroOffer", introOffer ? 1 : 2);

            if (com.ojassoft.astrosage.varta.utils.CUtils.astroListFilterType == FILTER_TYPE_CHAT) {
                bundle.putInt("isFreeForChat", astrologerDetailBeanArrayList.get(pos).isFreeForChat() ? 1 : 2);
            } else if (com.ojassoft.astrosage.varta.utils.CUtils.astroListFilterType == FILTER_TYPE_CALL) {
                bundle.putInt("isFreeForCall", astrologerDetailBeanArrayList.get(pos).isFreeForCall() ? 1 : 2);
            }

            Intent intent = new Intent(activity, AstrologerDescriptionActivity.class);
            intent.putExtras(bundle);
            activity.startActivity(intent);
        } catch (Exception e) {

        }
    }
}

