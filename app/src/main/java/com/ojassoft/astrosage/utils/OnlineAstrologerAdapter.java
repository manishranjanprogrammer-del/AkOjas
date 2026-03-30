package com.ojassoft.astrosage.utils;

import static com.ojassoft.astrosage.utils.CGlobalVariables.GOOGLE_ANALYTICS_KUNDLI_HOME_ONLINE_ASTRO_CARD;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KUNDLI_HOME_ONLINE_ASTRO_CARD_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.NUMEROLOGY_CALC_O_BOTTOM_BAR_KUNDLI_PARTNER_ID;
import static com.ojassoft.astrosage.utils.CUtils.createSession;
import static com.ojassoft.astrosage.utils.CUtils.fcmAnalyticsEvents;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.FILTER_TYPE_CALL;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.FILTER_TYPE_CHAT;
import static com.ojassoft.astrosage.varta.utils.CUtils.removeAcharyaTarot;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
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
import com.ojassoft.astrosage.ui.act.ActAppModule;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.NumerologyCalculatorOutputActivity;
import com.ojassoft.astrosage.varta.interfacefile.LoadMoreList;
import com.ojassoft.astrosage.varta.model.AstrologerDetailBean;
import com.ojassoft.astrosage.varta.ui.activity.AstrologerDescriptionActivity;
import com.ojassoft.astrosage.varta.ui.activity.DashBoardActivity;
import com.ojassoft.astrosage.varta.ui.activity.LoginSignUpActivity;
import com.ojassoft.astrosage.varta.ui.fragments.VartaHomeFragment;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.CircularNetworkImageView;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import java.util.ArrayList;

public class OnlineAstrologerAdapter extends RecyclerView.Adapter<OnlineAstrologerAdapter.MyViewHolder> {
    private Activity activity;
    private ArrayList<AstrologerDetailBean> astrologerDetailBeanArrayList;
    private int filterType;
    private LoadMoreList loadMoreListInterface;
    private VartaHomeFragment vartaHomeFragment;

    public OnlineAstrologerAdapter(Activity activity, ArrayList<AstrologerDetailBean> astrologerDetailBeanArrayList) {
        this.activity = activity;
        this.astrologerDetailBeanArrayList = astrologerDetailBeanArrayList;
    }

    public OnlineAstrologerAdapter(Activity activity, ArrayList<AstrologerDetailBean> astrologerDetailBeanArrayList,
                                   int filterType, LoadMoreList loadMoreListInterface, VartaHomeFragment vartaHomeFragment) {
        this.activity = activity;
        this.astrologerDetailBeanArrayList = astrologerDetailBeanArrayList;
        this.filterType = filterType;
        this.loadMoreListInterface = loadMoreListInterface;
        this.vartaHomeFragment = vartaHomeFragment;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(activity).inflate(R.layout.layout_astrologer_grid_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int pos) {
        final int position = pos;
        AstrologerDetailBean astrologerDetailBean = astrologerDetailBeanArrayList.get(position);
        if (astrologerDetailBean == null) return;
        String astrologerProfileUrl = "";
        if (astrologerDetailBean.getImageFile() != null && astrologerDetailBean.getImageFile().length() > 0) {
            astrologerProfileUrl = CGlobalVariables.IMAGE_DOMAIN + astrologerDetailBean.getImageFile();
            //holder.riProfileImg.setImageUrl(astrologerProfileUrl, VolleySingleton.getInstance(activity).getImageLoader());
            //Glide.with(activity).load(astrologerProfileUrl).into(holder.ri_profile_img);
            Glide.with(activity.getApplicationContext()).load(astrologerProfileUrl).circleCrop().placeholder(R.drawable.ic_profile_view).into(holder.riProfileImg);

        }
        if (astrologerDetailBean.getDoubleRating() != null && astrologerDetailBean.getDoubleRating().length() > 0) {
            if (astrologerDetailBean.getDoubleRating().equalsIgnoreCase("0.0") ||
                    astrologerDetailBean.getRating().equalsIgnoreCase("0") ||
                    astrologerDetailBean.getTotalRating().equalsIgnoreCase("0")) {
                holder.astroRating.setVisibility(View.GONE);
                holder.ivStar.setVisibility(View.GONE);
                holder.sepratorLine.setVisibility(View.GONE);
            } else {
                holder.astroRating.setVisibility(View.VISIBLE);
                holder.ivStar.setVisibility(View.VISIBLE);
                holder.sepratorLine.setVisibility(View.VISIBLE);
                holder.astroRating.setText(astrologerDetailBean.getDoubleRating());
            }
        }

        if(!CUtils.isAiAstrologer(astrologerDetailBean)){
            holder.astroTitleTV.setText(removeAcharyaTarot(astrologerDetailBean.getName()));
        } else {
            holder.astroTitleTV.setText(astrologerDetailBean.getName());
        }

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
            holder.priceTv1.setVisibility(View.VISIBLE);
            CUtils.setStrikeOnTextView(holder.priceTv1, price);
            //holder.priceTv1.setText(price);
            //holder.priceTv1.setPaintFlags(holder.priceTv1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            String quePrice = activity.getResources().getString(R.string.question_price).replace("$", astrologerDetailBean.getServicePrice());
            holder.priceTv2.setVisibility(View.VISIBLE);
            holder.priceTv2.setText(quePrice);
            holder.txtTodayOffer.setVisibility(View.VISIBLE);


            if (astrologerDetailBean.getIsVerified().equalsIgnoreCase("true")) {
                holder.ivVerified.setVisibility(View.VISIBLE);
                holder.ivVerified.setImageUrl(CUtils.getVerfiedImageSmall(), VolleySingleton.getInstance(activity).getImageLoader());
            } else {
                holder.ivVerified.setVisibility(View.GONE);
            }


        } else {
            // show only one price(servicePriceInt)
            String quePrice = activity.getResources().getString(R.string.question_price).replace("$", astrologerDetailBean.getServicePrice());
            holder.priceTv2.setVisibility(View.VISIBLE);
            holder.priceTv2.setText(quePrice);
            holder.priceTv1.setVisibility(View.GONE);
            holder.txtTodayOffer.setVisibility(View.INVISIBLE);

            if (astrologerDetailBean.getIsVerified().equalsIgnoreCase("true")) {
                holder.ivVerified.setVisibility(View.VISIBLE);
                holder.ivVerified.setImageUrl(CUtils.getVerfiedImageSmall(), VolleySingleton.getInstance(activity).getImageLoader());
            } else {
                holder.ivVerified.setVisibility(View.GONE);
            }
        }

        if (astrologerDetailBean.getUseIntroOffer()) {
            holder.newUser.setVisibility(View.VISIBLE);
            String offerType = astrologerDetailBean.getIntroOfferType();
            if (activity instanceof DashBoardActivity) {
                if ((filterType == FILTER_TYPE_CALL) && astrologerDetailBean.isFreeForCall() ||
                        ((filterType == FILTER_TYPE_CHAT) && astrologerDetailBean.isFreeForChat())) {
                    if (!TextUtils.isEmpty(offerType) && offerType.equals(CGlobalVariables.INTRO_OFFER_TYPE_FREE)) {
                        holder.newUser.setVisibility(View.VISIBLE);
                        holder.newUser.setText(activity.getResources().getString(R.string.text_free));
                        String quePrice = activity.getResources().getString(R.string.question_price).replace("$", astrologerDetailBean.getServicePrice());
                        holder.priceTv1.setVisibility(View.VISIBLE);
                        CUtils.setStrikeOnTextView(holder.priceTv1, quePrice);
                        //holder.priceTv1.setText(quePrice);
                        //holder.priceTv1.setPaintFlags(holder.priceTv1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        holder.priceTv2.setVisibility(View.GONE);
                    } else if(!TextUtils.isEmpty(offerType) && offerType.contains(CGlobalVariables.REDUCED_PRICE_OFFER)){
                        holder.newUser.setVisibility(View.GONE);
                        //holder.newUser.setText(activity.getResources().getString(R.string.new_user));
                    }
                } else {
                    holder.newUser.setVisibility(View.GONE);
                    holder.priceTv1.setVisibility(View.VISIBLE);
                    String price = astrologerDetailBean.getActualServicePriceInt();
                    CUtils.setStrikeOnTextView(holder.priceTv1, price);
                    //holder.priceTv1.setPaintFlags(holder.priceTv1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                }
            } else {
                if (!TextUtils.isEmpty(offerType) && offerType.equals(CGlobalVariables.INTRO_OFFER_TYPE_FREE)) {
                    holder.newUser.setText(activity.getResources().getString(R.string.text_free));
                    holder.newUser.setVisibility(View.VISIBLE);
                    String quePrice = activity.getResources().getString(R.string.question_price).replace("$", astrologerDetailBean.getServicePrice());
                    CUtils.setStrikeOnTextView(holder.priceTv1, quePrice);
                    //holder.priceTv1.setText(quePrice);
                    //holder.priceTv1.setPaintFlags(holder.priceTv1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.priceTv1.setVisibility(View.VISIBLE);
                    holder.priceTv2.setVisibility(View.GONE);
                } else {
                    holder.newUser.setVisibility(View.GONE);
                    //holder.newUser.setText(activity.getResources().getString(R.string.new_user));
                }
            }
        } else {
            holder.newUser.setVisibility(View.GONE);
            String price = astrologerDetailBean.getActualServicePriceInt();
            CUtils.setStrikeOnTextView(holder.priceTv1, price);
            //holder.priceTv1.setPaintFlags(holder.priceTv1.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        holder.rlMainView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (vartaHomeFragment == null){
                        createSession(activity, KUNDLI_HOME_ONLINE_ASTRO_CARD_PARTNER_ID);
                        fcmAnalyticsEvents(GOOGLE_ANALYTICS_KUNDLI_HOME_ONLINE_ASTRO_CARD, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                    }
                    Bundle bundle = new Bundle();
                    bundle.putString("phoneNumber", "");
                    bundle.putString("urlText", astrologerDetailBeanArrayList.get(position).getUrlText());
                    boolean introOfferType = astrologerDetailBeanArrayList.get(position).getUseIntroOffer();
                    bundle.putInt("useIntroOffer", introOfferType ? 1 : 2);

                    if (CUtils.astroListFilterType == FILTER_TYPE_CHAT) {
                        bundle.putInt("isFreeForChat", astrologerDetailBeanArrayList.get(pos).isFreeForChat() ? 1 : 2);
                    } else if (CUtils.astroListFilterType == FILTER_TYPE_CALL) {
                        bundle.putInt("isFreeForCall", astrologerDetailBeanArrayList.get(pos).isFreeForCall() ? 1 : 2);
                    }

                    Intent intent = new Intent(activity, AstrologerDescriptionActivity.class);
                    intent.putExtras(bundle);
                    activity.startActivity(intent);
                } catch (Exception e) {
                    //
                }
            }
        });

        if (activity instanceof DashBoardActivity || vartaHomeFragment != null) {
            holder.ivStatus.setVisibility(View.GONE);
            holder.txtTodayOffer.setVisibility(View.VISIBLE);
            /*String waitTime = activity.getResources().getString(R.string.waitTimeText).replace("#", astrologerDetailBean.getWaitTime());
            if (!astrologerDetailBean.getWaitTime().equals("0")) {
                holder.tvAstroGridWaitTime.setText(waitTime);
                holder.tvAstroGridWaitTime.setVisibility(View.VISIBLE);
            } else {
                holder.tvAstroGridWaitTime.setVisibility(View.GONE);
            }*/
            FontUtils.changeFont(activity, holder.tvAstroGridLayoutCall, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
            FontUtils.changeFont(activity, holder.tvAstroGridLayoutChat, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
            if (filterType == FILTER_TYPE_CALL) {
                holder.llAstroGridLayoutCall.setVisibility(View.VISIBLE);
                holder.llAstroGridLayoutChat.setVisibility(View.GONE);
                if (astrologerDetailBean.getIsBusy().equalsIgnoreCase("true")) {
                    holder.tvAstroGridLayoutCall.setText("    "+activity.getResources().getString(R.string.busy)+"    ");
                    holder.llAstroGridLayoutCall.setBackgroundResource(R.drawable.bg_button_red);
                    holder.tvAstroGridLayoutCall.setTextColor(Color.WHITE);
                } else {
                    holder.tvAstroGridLayoutCall.setText(activity.getResources().getString(R.string.call_now));
                    holder.llAstroGridLayoutCall.setBackgroundResource(R.drawable.bg_button_yellow);
                    holder.tvAstroGridLayoutCall.setTextColor(Color.BLACK);
                }
            } else if (filterType == FILTER_TYPE_CHAT) {
                holder.llAstroGridLayoutChat.setVisibility(View.VISIBLE);
                holder.llAstroGridLayoutCall.setVisibility(View.GONE);
                if (astrologerDetailBean.getIsBusy().equalsIgnoreCase("true")) {
                    holder.tvAstroGridLayoutChat.setText("    "+activity.getResources().getString(R.string.busy)+"    ");
                    holder.llAstroGridLayoutChat.setBackgroundResource(R.drawable.bg_button_red);
                    holder.tvAstroGridLayoutChat.setTextColor(Color.WHITE);
                } else {
                    holder.tvAstroGridLayoutChat.setText(activity.getResources().getString(R.string.txt_chat_now));
                    holder.llAstroGridLayoutChat.setBackgroundResource(R.drawable.bg_button_yellow);
                    holder.tvAstroGridLayoutChat.setTextColor(Color.BLACK);
                }
            }
        } else {
            holder.ivStatus.setVisibility(View.VISIBLE);
            holder.txtTodayOffer.setVisibility(View.GONE);
        }

        holder.llAstroGridLayoutChat.setOnClickListener(v -> {
            boolean isLogin = CUtils.getUserLoginStatus(activity);
            AstrosageKundliApplication.currentEventType = CGlobalVariables.CHAT_BTN_CLICKED;
            if (isLogin) {
                CUtils.fcmAnalyticsEvents("chat_btn_click_varta_astrologer_list", AstrosageKundliApplication.currentEventType, "");
                CUtils.saveStringData(activity, CGlobalVariables.PREF_CHAT_BUTTON_CLICK, "chat");
                loadMoreListInterface.chatAstrologer(astrologerDetailBeanArrayList.get(pos));
            } else {
                CUtils.fcmAnalyticsEvents("chat_user_not_login",
                        AstrosageKundliApplication.currentEventType, "");
//                CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_NAV_SIGNUP,
//                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                Intent intent = new Intent(activity, LoginSignUpActivity.class);
                intent.putExtra(CGlobalVariables.IS_FROM_SCREEN, CGlobalVariables.DASHBOARD_CALL_NOW_SCRREN);
                activity.startActivity(intent);
            }
        });

        holder.llAstroGridLayoutCall.setOnClickListener(v -> {
            AstrosageKundliApplication.currentEventType = CGlobalVariables.CALL_BTN_CLICKED;
            CUtils.fcmAnalyticsEvents(CGlobalVariables.VARTA_HOME_AUDIO_CALL_BTN, AstrosageKundliApplication.currentEventType, "");
            boolean isLogin = CUtils.getUserLoginStatus(activity);
            if (isLogin) {
                loadMoreListInterface.callAstrologer(astrologerDetailBeanArrayList.get(pos));
            } else {
                CUtils.fcmAnalyticsEvents(CGlobalVariables.AUDIO_CALL_USER_NOT_LOGIN,
                        AstrosageKundliApplication.currentEventType, "");
                Intent intent = new Intent(activity, LoginSignUpActivity.class);
                intent.putExtra(CGlobalVariables.IS_FROM_SCREEN, CGlobalVariables.DASHBOARD_CALL_NOW_SCRREN);
                activity.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return astrologerDetailBeanArrayList != null ? astrologerDetailBeanArrayList.size() : 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtTodayOffer, astroTitleTV, priceTv1, priceTv2, astroRating, sepratorLine, newUser,tvAstroGridWaitTime;
        CircularNetworkImageView riProfileImg;
        ImageView ivStar, ivStatus;
        NetworkImageView ivVerified;
        RelativeLayout rlMainView;
        LinearLayout llAstroGridLayoutChat, llAstroGridLayoutCall;
        TextView tvAstroGridLayoutChat, tvAstroGridLayoutCall;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            riProfileImg = itemView.findViewById(R.id.ri_profile_img);
            txtTodayOffer = itemView.findViewById(R.id.txt_today_offer);
            astroTitleTV = itemView.findViewById(R.id.astroTitleTV);
            priceTv1 = itemView.findViewById(R.id.priceTv1);
            priceTv2 = itemView.findViewById(R.id.priceTv2);
            ivVerified = itemView.findViewById(R.id.iv_verified);
            astroRating = itemView.findViewById(R.id.astro_rating);
            ivStar = itemView.findViewById(R.id.iv_star);
            rlMainView = itemView.findViewById(R.id.rl_main_view);
            sepratorLine = itemView.findViewById(R.id.seprator_line);
            newUser = itemView.findViewById(R.id.new_user);
            ivStatus = itemView.findViewById(R.id.online_offline_img);
            llAstroGridLayoutChat = itemView.findViewById(R.id.llAstroGridLayoutChat);
            llAstroGridLayoutCall = itemView.findViewById(R.id.llAstroGridLayoutCall);
            tvAstroGridLayoutChat = itemView.findViewById(R.id.tvAstroGridLayoutChat);
            tvAstroGridLayoutCall = itemView.findViewById(R.id.tvAstroGridLayoutCall);
            tvAstroGridWaitTime = itemView.findViewById(R.id.tvAstroGridWaitTime);

            FontUtils.changeFont(activity, txtTodayOffer, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
            FontUtils.changeFont(activity, astroTitleTV, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
            FontUtils.changeFont(activity, astroRating, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
            FontUtils.changeFont(activity, priceTv1, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
            FontUtils.changeFont(activity, priceTv2, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
            FontUtils.changeFont(activity, tvAstroGridWaitTime, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
            FontUtils.changeFont(activity, tvAstroGridLayoutChat, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
            FontUtils.changeFont(activity, tvAstroGridLayoutCall, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        }
    }
}
