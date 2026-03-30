package com.ojassoft.astrosage.varta.adapters;


import static com.ojassoft.astrosage.utils.CGlobalVariables.COUNTRY_CODE_IND;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.FILTER_TYPE_CALL;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.FILTER_TYPE_CHAT;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.varta.dao.NotificationDBManager;
import com.ojassoft.astrosage.varta.interfacefile.LoadMoreList;
import com.ojassoft.astrosage.varta.model.AstrologerDetailBean;
import com.ojassoft.astrosage.varta.model.LangAndExpertiseData;
import com.ojassoft.astrosage.varta.ui.activity.AstrologerDescriptionActivity;
import com.ojassoft.astrosage.varta.ui.activity.DashBoardActivity;
import com.ojassoft.astrosage.varta.ui.activity.LoginSignUpActivity;
import com.ojassoft.astrosage.varta.ui.fragments.HomeFragment1;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.CircularNetworkImageView;
import com.ojassoft.astrosage.varta.utils.FontUtils;

import java.util.ArrayList;


public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.ItemViewHolder> {

    private static final int TYPE_ITEM = 1;
    private static final int FOOTER_VIEW = 3;
    public static ArrayList<LangAndExpertiseData> langDataArrayList;
    private final Context context;
    LoadMoreList loadMoreListInterface;
    NotificationDBManager dbManager;
    private final ArrayList<AstrologerDetailBean> arrayListRecyclerView = new ArrayList<>();
    private HomeFragment1 fragment;

    public CustomAdapter(Context context, final LoadMoreList loadMoreListInterface, ArrayList<AstrologerDetailBean> arrayListRecyclerView,HomeFragment1 fragment) {
        this.context = context;
        this.loadMoreListInterface = loadMoreListInterface;
        this.arrayListRecyclerView.addAll(arrayListRecyclerView);
        dbManager = new NotificationDBManager(context);
        setDisplayMetrics();
        this.fragment = fragment;
    }

    private void setDisplayMetrics() {
        try {
            if (context != null) {
                DisplayMetrics displayMetrics = new DisplayMetrics();
                ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
                CGlobalVariables.width = displayMetrics.widthPixels;
                CGlobalVariables.height = displayMetrics.heightPixels;
                CGlobalVariables.modifyHeight = (CGlobalVariables.width / 16) * 5;
            }
        } catch (Exception e) {
            //
        }
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.astrologer_row_layout, parent, false);
        TextView updateFilterTV = itemView.findViewById(R.id.font_auto_astrologer_row_layout_2);
        FontUtils.changeFont(parent.getContext(), updateFilterTV, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder( ItemViewHolder holder, int position) {
        if (arrayListRecyclerView == null || arrayListRecyclerView.size() < position) return;
        AstrologerDetailBean astrologerDetailBean = arrayListRecyclerView.get(position);
        if (astrologerDetailBean == null) return;

        if (position== arrayListRecyclerView.size()-1) {
            holder.linLayoutUpdateFilter.setVisibility(View.VISIBLE);
        } else {
            holder.linLayoutUpdateFilter.setVisibility(View.GONE);
        }
        holder.linLayoutUpdateFilter.setOnClickListener(v->{
            if (fragment != null) {
                fragment.openFilter();
            }
        });

        if (astrologerDetailBean.isAvailableForCallBool() || astrologerDetailBean.isAvailableForChatBool()) {
            if (CUtils.astroListFilterType == FILTER_TYPE_CALL) {
                holder.callSecondNowBtn.setVisibility(View.VISIBLE);
                holder.chatNowBtn.setVisibility(View.GONE);
                if (astrologerDetailBean.isBusyBool()) {
                    holder.callSecondNowBtn.setText(context.getResources().getString(R.string.busy));
                    holder.callSecondNowBtn.setBackgroundResource(R.drawable.bg_button_red);
                    holder.callSecondNowBtn.setTextColor(Color.WHITE);
                } else {
                    holder.callSecondNowBtn.setText(context.getResources().getString(R.string.call));
                    holder.callSecondNowBtn.setBackgroundResource(R.drawable.bg_button_yellow);
                    holder.callSecondNowBtn.setTextColor(context.getColor(R.color.white));
                }
            } else if (CUtils.astroListFilterType == FILTER_TYPE_CHAT) {
                holder.callSecondNowBtn.setVisibility(View.VISIBLE);
                holder.chatNowBtn.setVisibility(View.VISIBLE);
                if (astrologerDetailBean.isBusyBool()) {
                    holder.callSecondNowBtn.setVisibility(View.VISIBLE);
                    holder.chatNowBtn.setVisibility(View.VISIBLE);

                    holder.chatNowBtn.setText(context.getResources().getString(R.string.busy));
                    //holder.chatNowBtn.setText(context.getResources().getString(R.string.chat_now));
                    holder.chatNowBtn.setBackgroundResource(R.drawable.bg_button_red);
                    holder.chatNowBtn.setTextColor(Color.WHITE);

                    holder.callSecondNowBtn.setText(context.getResources().getString(R.string.busy));
                    holder.callSecondNowBtn.setBackgroundResource(R.drawable.bg_button_red);
                    holder.callSecondNowBtn.setTextColor(Color.WHITE);
                    holder.callSecondNowBtn.setVisibility(View.GONE);
                } else {

                    if ( astrologerDetailBean.isAvailableForCallBool() &&
                            astrologerDetailBean.isAvailableForChatBool()) {

                        holder.callSecondNowBtn.setVisibility(View.VISIBLE);
                        holder.chatNowBtn.setVisibility(View.VISIBLE);

                        holder.chatNowBtn.setText(context.getResources().getString(R.string.chat_now));
                        holder.chatNowBtn.setBackgroundResource(R.drawable.bg_button_yellow);
                        holder.chatNowBtn.setTextColor(context.getColor(R.color.white));

                        holder.callSecondNowBtn.setText(context.getResources().getString(R.string.call));
                        holder.callSecondNowBtn.setBackgroundResource(R.drawable.bg_button_yellow);
                        holder.callSecondNowBtn.setTextColor(context.getColor(R.color.white));

                    } else if ( astrologerDetailBean.isAvailableForCallBool()) {

                        holder.callSecondNowBtn.setVisibility(View.VISIBLE);
                        holder.chatNowBtn.setVisibility(View.GONE);

                        holder.callSecondNowBtn.setText(context.getResources().getString(R.string.call));
                        holder.callSecondNowBtn.setBackgroundResource(R.drawable.bg_button_yellow);
                        holder.callSecondNowBtn.setTextColor(context.getColor(R.color.white));
                    } else {
                        holder.callSecondNowBtn.setVisibility(View.GONE);
                        holder.chatNowBtn.setVisibility(View.VISIBLE);
                        holder.chatNowBtn.setText(context.getResources().getString(R.string.chat_now));
                        holder.chatNowBtn.setBackgroundResource(R.drawable.bg_button_yellow);
                        holder.chatNowBtn.setTextColor(context.getColor(R.color.white));
                    }
                }
            }

        }

        /*
         * added by Shreyans
         * to hide call button on Chat list
         * when user logged in with international number
         * and has free chat available
         * */
        if (!CUtils.getCountryCode(context).equals(COUNTRY_CODE_IND) && CUtils.astroListFilterType == FILTER_TYPE_CHAT && astrologerDetailBean.isFreeForChat()) {
            holder.callSecondNowBtn.setVisibility(View.GONE);
        }

        if (astrologerDetailBean.getDoubleRating == 0.0 ||
                astrologerDetailBean.getRatingDoule() == 0  ||
                astrologerDetailBean.getTotalRatingDouble() == 0 ) {
            holder.ratingTxt.setVisibility(View.GONE);
            holder.ratingTxtTotal.setVisibility(View.GONE);
            holder.ratingStar.setVisibility(View.GONE);
        } else {
            holder.ratingTxt.setVisibility(View.VISIBLE);
            holder.ratingTxtTotal.setVisibility(View.VISIBLE);
            holder.ratingStar.setVisibility(View.VISIBLE);
            holder.ratingTxt.setText(String.valueOf(astrologerDetailBean.getDoubleRating()) );
            holder.ratingTxtTotal.setText( "(" + astrologerDetailBean.getTotalRating() +")" );
            //      holder.ratingStar.setRating(Float.parseFloat(astrologerDetailBean.getDoubleRating()));
        }

        int actualPrice = astrologerDetailBean.getActualServicePriceInteger();
        int servicePrice = astrologerDetailBean.servicePriceInt();

        //holder.verifiedTwoImg.setImageUrl(CUtils.getVerfiedImageSmall(), VolleySingleton.getInstance(context).getImageLoader());
        if (actualPrice > servicePrice) {
            //show both prices
            CUtils.setStrikeOnTextView(holder.actualQuestionPriceTxt, String.valueOf(astrologerDetailBean.getActualServicePriceInt()));
            //holder.actualQuestionPriceTxt.setText(price);
            //holder.actualQuestionPriceTxt.setPaintFlags(holder.actualQuestionPriceTxt.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            String quePrice = context.getString(R.string.astrologer_rate,astrologerDetailBean.servicePriceInt());
            holder.questionPriceTxt.setText(quePrice);
            holder.questionPriceTxt.setVisibility(View.VISIBLE);
            holder.actualQuestionPriceTxt.setVisibility(View.GONE); // SAN Gone
            holder.verifiedImg.setVisibility(View.INVISIBLE);

            if (astrologerDetailBean.getIsVerified().equalsIgnoreCase("true")) {
                holder.verifiedTwoImg.setVisibility(View.GONE);
                holder.verifiedImg.setVisibility(View.VISIBLE);
            } else {
                holder.verifiedTwoImg.setVisibility(View.GONE);
                holder.verifiedImg.setVisibility(View.INVISIBLE);
            }

               /* holder.offerImg.setImageUrl(CUtils.getOfferImageSmall(), VolleySingleton.getInstance(context).getImageLoader());
                holder.offerImg.setVisibility(View.VISIBLE);*/

        } else {
            // show only one price(servicePriceInt)
            //Log.e("pricestring"," : "+context.getResources().getString(R.string.question_price));
            String quePrice = context.getString(R.string.astrologer_rate,astrologerDetailBean.servicePriceInt());
            //Log.e("pricestring"," : "+quePrice);
            holder.questionPriceTxt.setText(quePrice);
            holder.questionPriceTxt.setVisibility(View.VISIBLE);
            holder.actualQuestionPriceTxt.setVisibility(View.GONE);

            if (astrologerDetailBean.getIsVerified().equalsIgnoreCase("true")) {
                holder.verifiedImg.setVisibility(View.VISIBLE);
            } else {
                holder.verifiedImg.setVisibility(View.INVISIBLE);
            }
            holder.verifiedTwoImg.setVisibility(View.GONE);
        }

        if (astrologerDetailBean.getIsVerified().equalsIgnoreCase("true")) {
            holder.verifiedImg.setVisibility(View.VISIBLE);
        } else {
            holder.verifiedImg.setVisibility(View.INVISIBLE);
        }
        holder.verifiedTwoImg.setVisibility(View.GONE);
        //Log.d("TestFree", astrologerDetailBean.getName()+", 1="+astrologerDetailBean.getUseIntroOffer()+" 2="+offerType+" 3="+CUtils.astroListFilterType+" 4="+astrologerDetailBean.isFreeForCall()+" 5="+astrologerDetailBean.isFreeForChat());
        if ( !astrologerDetailBean.getAiAstrologerId().equals("0") && com.ojassoft.astrosage.varta.utils.CUtils.isKundliAiProPlan(context)) {
//            holder.newUser.setVisibility(View.VISIBLE);
//            holder.newUser.setText(context.getResources().getString(R.string.text_free));
//            String quePrice = context.getString(R.string.astrologer_rate, astrologerDetailBean.servicePriceInt());
//            holder.questionPriceTxt.setText(quePrice);
//            CUtils.setStrikeOnTextView(holder.questionPriceTxt, quePrice);
//            //holder.questionPriceTxt.setPaintFlags(holder.questionPriceTxt.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//            holder.questionPriceTxt.setVisibility(View.GONE);
//            holder.actualQuestionPriceTxt.setVisibility(View.GONE);
//            holder.ruppee_sign.setVisibility(View.GONE);
            holder.newUser.setVisibility(View.GONE);//hide new user text
            CUtils.removeStrikeOnTextView(holder.questionPriceTxt);
            holder.ruppee_sign.setVisibility(View.VISIBLE);
            holder.chatNowBtn.setText(context.getResources().getString(R.string.free_chat_now));
            FontUtils.changeFont(context, holder.chatNowBtn, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
            FontUtils.changeFont(context, holder.callNowBtn, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
            holder.chatNowBtn.setTextSize(14);
            holder.callNowBtn.setTextSize(14);
        }else {
        if (astrologerDetailBean.getUseIntroOffer()) {
            String offerType = astrologerDetailBean.getIntroOfferType();
            if ((CUtils.astroListFilterType == FILTER_TYPE_CALL && astrologerDetailBean.isFreeForCall()) ||
                    CUtils.astroListFilterType == FILTER_TYPE_CHAT && astrologerDetailBean.isFreeForChat()) {
                if (!TextUtils.isEmpty(offerType) && offerType.equals(CGlobalVariables.INTRO_OFFER_TYPE_FREE)) {
                    holder.newUser.setVisibility(View.VISIBLE);
                    holder.newUser.setText(context.getResources().getString(R.string.text_free));
                    String quePrice = context.getString(R.string.astrologer_rate,astrologerDetailBean.servicePriceInt());
                    holder.questionPriceTxt.setText(quePrice);
                    CUtils.setStrikeOnTextView(holder.questionPriceTxt, quePrice);
                    //holder.questionPriceTxt.setPaintFlags(holder.questionPriceTxt.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                    holder.questionPriceTxt.setVisibility(View.GONE);
                    holder.actualQuestionPriceTxt.setVisibility(View.GONE);
                    holder.ruppee_sign.setVisibility(View.GONE);
                } else if (!TextUtils.isEmpty(offerType) && offerType.contains(CGlobalVariables.REDUCED_PRICE_OFFER)) {
                    CUtils.removeStrikeOnTextView(holder.questionPriceTxt);
                    //holder.questionPriceTxt.setPaintFlags(holder.questionPriceTxt.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                    holder.newUser.setVisibility(View.GONE);
                    holder.newUser.setText(context.getResources().getString(R.string.new_user));
                    holder.actualQuestionPriceTxt.setVisibility(View.GONE); // SAN
                    holder.questionPriceTxt.setVisibility(View.VISIBLE);
                    holder.ruppee_sign.setVisibility(View.VISIBLE);
                }
            } else {
                holder.newUser.setVisibility(View.GONE); //hide new user text
                CUtils.removeStrikeOnTextView(holder.questionPriceTxt);
                holder.ruppee_sign.setVisibility(View.VISIBLE);
                //holder.questionPriceTxt.setPaintFlags(holder.questionPriceTxt.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
            }
            //Log.d("AstroData", "offerType="+offerType);

                /*if (!astrologerDetailBean.isFreeForCall() && !astrologerDetailBean.isFreeForChat()) {
                    holder.newUser.setVisibility(View.VISIBLE);
                    holder.newUser.setText(context.getResources().getString(R.string.new_user));
                }*/
        } else {
            holder.newUser.setVisibility(View.GONE);//hide new user text
            CUtils.removeStrikeOnTextView(holder.questionPriceTxt);
            holder.ruppee_sign.setVisibility(View.VISIBLE);
            //holder.questionPriceTxt.setPaintFlags(holder.questionPriceTxt.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
        }
        }
        if(astrologerDetailBean.getPrimaryExpertise().isEmpty()){
            holder.vedicAstrologerTxt.setText(astrologerDetailBean.getExpertise());
        }else {
            holder.vedicAstrologerTxt.setText(astrologerDetailBean.getPrimaryExpertise());
        }


        holder.languageTxt.setText(astrologerDetailBean.getLanguage());
        if(CUtils.isAiAstrologer(astrologerDetailBean)){
            holder.astrologerNameTxt.setText(astrologerDetailBean.getName());
        } else {
            holder.astrologerNameTxt.setText(CUtils.removeAcharyaTarot(astrologerDetailBean.getName()));
        }
        String astrologerProfileUrl = "";
        if (astrologerDetailBean.getImageFile() != null && astrologerDetailBean.getImageFile().length() > 0) {
            astrologerProfileUrl = CGlobalVariables.IMAGE_DOMAIN + astrologerDetailBean.getImageFile();
            //holder.roundImage.setImageUrl(astrologerProfileUrl, VolleySingleton.getInstance(context).getImageLoader());
            Glide.with(context.getApplicationContext()).load(astrologerProfileUrl).placeholder(R.drawable.ic_profile_view).error(R.drawable.ic_profile_view) .circleCrop().into(holder.roundImage);
        }

         /*   if (!astrologerDetailBean.isAstrologerBookmarked()) {
                holder.bookmarkImg.setImageResource(R.drawable.bookmark_line);
            } else {
                holder.bookmarkImg.setImageResource(R.drawable.bookmark_fill);
            }*/

        if (astrologerDetailBean.getFollowCount() == 0) {
            holder.ratingTxtTotalImg.setVisibility(View.GONE);
            holder.followersCountTV.setVisibility(View.GONE);
        } else {
            holder.ratingTxtTotalImg.setVisibility(View.VISIBLE);
            holder.followersCountTV.setText("" + astrologerDetailBean.getFollowCount());
            holder.followersCountTV.setVisibility(View.VISIBLE);
        }

        //Note - Don't remove it will use later
        String waitTime = context.getResources().getString(R.string.waitTimeText).replace("#", astrologerDetailBean.getWaitTime());

           /* if (!(astrologerDetailBean.getWaitTime().equals("0") || astrologerDetailBean.getWaitTime().equals(""))) {
                holder.tvAstroRowWaitTime.setText(waitTime);
                holder.tvAstroRowWaitTime.setVisibility(View.VISIBLE);
            } else {
                holder.tvAstroRowWaitTime.setVisibility(View.GONE);
            }*/

        holder.callSecondNowBtn.setTag(position);
        holder.callNowBtn.setTag(position);
        holder.chatNowBtn.setTag(position);
        holder.upperLayout.setTag(position);
        //      holder.bookmarkImg.setTag(position);
    }

    @Override
    public int getItemViewType(int position) {
        try {
            int totalSize = 0;
            if (arrayListRecyclerView != null) {
                totalSize = arrayListRecyclerView.size();
            }
            if (position == totalSize) {
                return FOOTER_VIEW;
            }
        } catch (Exception e) {

        }
        return TYPE_ITEM;
    }

    // getItemCount increasing the position to 1. This will be the row Log.i("totalSize>>", totalSize + ", " + arrayListRecyclerView.size() + ", " + position); of header
    @Override
    public int getItemCount() {
        if(arrayListRecyclerView == null) {
            return 0;
        } else {
            return arrayListRecyclerView.size();
        }
    }

    public void updateList() {
        ((HomeFragment1) loadMoreListInterface).selectBookMarkedDataUpdate();
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView ratingTxt, ratingTxtTotal, callNowBtn, chatNowBtn, questionPriceTxt,
                vedicAstrologerTxt, languageTxt, astrologerNameTxt, actualQuestionPriceTxt,
                callSecondNowBtn, ruppee_sign, newUser, followersCountTV;
        ImageView ratingTxtTotalImg;
        CircularNetworkImageView roundImage;
        ImageView  verifiedImg, verifiedTwoImg;
        //RelativeLayout mainLayout;
        ImageView onlineOfflineImg, bookmarkImg, ratingStar;
        ConstraintLayout upperLayout;
        //   AppCompatRatingBar ratingStar;
        ImageView progressBarIV;
        LinearLayout linLayoutUpdateFilter;

        public ItemViewHolder(View itemView) {
            super(itemView);

            linLayoutUpdateFilter = itemView.findViewById(R.id.linLayoutUpdateFilter);
            actualQuestionPriceTxt = itemView.findViewById(R.id.actual_question_price_txt);
            ruppee_sign = itemView.findViewById(R.id.ruppee_sign);
            newUser = itemView.findViewById(R.id.new_user);
            ratingStar = itemView.findViewById(R.id.rating_star);
            ratingTxt = itemView.findViewById(R.id.rating_txt);
            ratingTxtTotal = itemView.findViewById(R.id.rating_txt_total);
            ratingTxtTotalImg = itemView.findViewById(R.id.rating_tag);
            followersCountTV = itemView.findViewById(R.id.followersCountTV);
            callNowBtn = itemView.findViewById(R.id.call_now_btn);
            chatNowBtn = itemView.findViewById(R.id.chat_now_btn);
            callSecondNowBtn = itemView.findViewById(R.id.call_second_now_btn);
            questionPriceTxt = itemView.findViewById(R.id.question_price_txt);
            vedicAstrologerTxt = itemView.findViewById(R.id.vedic_astrologer_txt);
            languageTxt = itemView.findViewById(R.id.language_txt);
            astrologerNameTxt = itemView.findViewById(R.id.astrologer_name_txt);
            roundImage = itemView.findViewById(R.id.ri_profile_img);
            //mainLayout = itemView.findViewById(R.id.mainlayout);
            verifiedImg = itemView.findViewById(R.id.verified_img);
            verifiedTwoImg = itemView.findViewById(R.id.verified_two_img);


            onlineOfflineImg = itemView.findViewById(R.id.online_offline_img);
            upperLayout = itemView.findViewById(R.id.upper_layout);
            LinearLayout rlRatingAndReview = itemView.findViewById(R.id.rlRatingAndReview);
            FontUtils.changeFont(context, astrologerNameTxt, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
            FontUtils.changeFont(context, ratingTxt, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
            FontUtils.changeFont(context, ratingTxtTotal, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
            FontUtils.changeFont(context, followersCountTV, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
            //        FontUtils.changeFont(context, followersTV, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
            FontUtils.changeFont(context, questionPriceTxt, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
            FontUtils.changeFont(context, actualQuestionPriceTxt, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
            FontUtils.changeFont(context, ruppee_sign, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
            FontUtils.changeFont(context, newUser, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
            FontUtils.changeFont(context, vedicAstrologerTxt, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
            FontUtils.changeFont(context, languageTxt, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
            FontUtils.changeFont(context, callNowBtn, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
            //         FontUtils.changeFont(context, tvAstroRowWaitTime, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);

            actualQuestionPriceTxt.setVisibility(View.GONE); // SAN

            FontUtils.changeFont(context, chatNowBtn, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
            FontUtils.changeFont(context, callSecondNowBtn, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
            rlRatingAndReview.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    new AlertDialog.Builder(context)
                            .setTitle("Astrologer")
                            .setMessage("Astrologer Items (  "+CUtils.getAstroList()+"   )")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                    return false;
                }
            });
            upperLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        int pos = Integer.parseInt(upperLayout.getTag().toString());
                        Bundle bundle = new Bundle();
                        bundle.putString("phoneNumber", arrayListRecyclerView.get(pos).getPhoneNumber());
                        bundle.putString("urlText", arrayListRecyclerView.get(pos).getUrlText());
                        boolean introOffer = arrayListRecyclerView.get(pos).getUseIntroOffer();
                        bundle.putInt("useIntroOffer", introOffer ? 1 : 2); //1 means true, 2 means false

                        if (CUtils.astroListFilterType == FILTER_TYPE_CHAT) {
                            bundle.putInt("isFreeForChat", arrayListRecyclerView.get(pos).isFreeForChat() ? 1 : 2);
                        } else if (CUtils.astroListFilterType == FILTER_TYPE_CALL) {
                            bundle.putInt("isFreeForCall", arrayListRecyclerView.get(pos).isFreeForCall() ? 1 : 2);
                        }

                        if (context instanceof DashBoardActivity) {
                            bundle.putBoolean("fromDashboard", true);
                        }
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_HOME_ASTRO_DETAIL_CLICK, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                        Intent intent = new Intent(context, AstrologerDescriptionActivity.class);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    } catch (Exception e) {
                        //
                    }
                }
            });

            callNowBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        int pos = Integer.parseInt(callNowBtn.getTag().toString());
                        String aiai = arrayListRecyclerView.get(pos).getAiAstrologerId();
                        AstrosageKundliApplication.currentEventType = CGlobalVariables.CALL_BTN_CLICKED;

                        if(!TextUtils.isEmpty(aiai) && !aiai.equals("0")){
                            CUtils.fcmAnalyticsEvents("ai_call_btn_click_astrologer_list", AstrosageKundliApplication.currentEventType, "");
                        }else {
                            CUtils.fcmAnalyticsEvents(CGlobalVariables.ASTRO_LIST_AUDIO_CALL_BTN, AstrosageKundliApplication.currentEventType, "");
                        }

                        com.ojassoft.astrosage.utils.CUtils.createSession(context, "ALCAL");

                        boolean isLogin = CUtils.getUserLoginStatus(context);
                        if (isLogin) {
                            AstrologerDetailBean astrologerDetailBean = arrayListRecyclerView.get(pos);
                            if (!TextUtils.isEmpty(aiai) && !aiai.equals("0")) {
                                astrologerDetailBean.setCallSource(CGlobalVariables.HUMAN_LIST_CALL_BTN);
                            }else{
                                astrologerDetailBean.setCallSource(HomeFragment1.TAG);
                            }
                            loadMoreListInterface.callAstrologer(astrologerDetailBean);
                        } else {
                            CUtils.fcmAnalyticsEvents(CGlobalVariables.AUDIO_CALL_USER_NOT_LOGIN,
                                    AstrosageKundliApplication.currentEventType, "");
                            Intent intent = new Intent(context, LoginSignUpActivity.class);
                            intent.putExtra(CGlobalVariables.IS_FROM_SCREEN, CGlobalVariables.DASHBOARD_CALL_NOW_SCRREN);
                            context.startActivity(intent);
                        }
                    } catch (Exception e) {
                        //
                    }
                }
            });

            callSecondNowBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        AstrosageKundliApplication.currentEventType = CGlobalVariables.CALL_BTN_CLICKED;
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.ASTRO_LIST_AUDIO_CALL_BTN, AstrosageKundliApplication.currentEventType, "");
                        com.ojassoft.astrosage.utils.CUtils.createSession(context, "ALCAL");

                        int pos = Integer.parseInt(callSecondNowBtn.getTag().toString());
                        boolean isLogin = CUtils.getUserLoginStatus(context);
                        if (isLogin) {
                            AstrologerDetailBean astrologerDetailBean = arrayListRecyclerView.get(pos);
                            String aiai = astrologerDetailBean.getAiAstrologerId();
                            if (!TextUtils.isEmpty(aiai) && !aiai.equals("0")) {
                                astrologerDetailBean.setCallSource(CGlobalVariables.HUMAN_LIST_CALL_BTN);
                            }else{
                                astrologerDetailBean.setCallSource(HomeFragment1.TAG);
                            }
                            loadMoreListInterface.callAstrologer(astrologerDetailBean);
                        } else {
                            CUtils.fcmAnalyticsEvents(CGlobalVariables.AUDIO_CALL_USER_NOT_LOGIN,
                                    AstrosageKundliApplication.currentEventType, "");
                            Intent intent = new Intent(context, LoginSignUpActivity.class);
                            intent.putExtra(CGlobalVariables.IS_FROM_SCREEN, CGlobalVariables.DASHBOARD_CALL_NOW_SCRREN);
                            context.startActivity(intent);
                        }
                    } catch (Exception e) {
                        //
                    }
                }
            });

            chatNowBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        AstrosageKundliApplication.currentEventType = CGlobalVariables.CHAT_BTN_CLICKED;
                        CUtils.fcmAnalyticsEvents("chat_btn_click_astrologer_list", AstrosageKundliApplication.currentEventType, "");

                        int pos = Integer.parseInt(chatNowBtn.getTag().toString());
                        boolean isLogin = CUtils.getUserLoginStatus(context);
                        if (isLogin) {
                            com.ojassoft.astrosage.utils.CUtils.createSession(context, "ALCHT");
                            CUtils.saveStringData(context, CGlobalVariables.PREF_CHAT_BUTTON_CLICK, "chat");
                            loadMoreListInterface.chatAstrologer(arrayListRecyclerView.get(pos));
                        } else {
                            CUtils.fcmAnalyticsEvents("chat_user_not_login",
                                    AstrosageKundliApplication.currentEventType, "");
                            Intent intent = new Intent(context, LoginSignUpActivity.class);
                            intent.putExtra(CGlobalVariables.IS_FROM_SCREEN, CGlobalVariables.DASHBOARD_CALL_NOW_SCRREN);
                            context.startActivity(intent);
                        }
                    } catch (Exception e) {
                        //
                    }
                }
            });

          /*  bookmarkImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        final int pos = Integer.parseInt(bookmarkImg.getTag().toString());

                        if (!arrayListRecyclerView.get(pos).isAstrologerBookmarked()) {
                            bookmarkImg.setImageResource(R.drawable.bookmark_fill);
                            BookmarkModel bookmarkModel = new BookmarkModel();
                            bookmarkModel.setAstrologerId(arrayListRecyclerView.get(pos).getAstrologerId());
                            bookmarkModel.setBookmarkStatus("" + true);
                            dbManager.addBookmark(bookmarkModel);
                            arrayListRecyclerView.get(pos).setAstrologerBookmarked(true);
                        } else {
                            arrayListRecyclerView.get(pos).setAstrologerBookmarked(false);
                            dbManager.deleteBookmarkRow(arrayListRecyclerView.get(pos).getAstrologerId());
                            bookmarkImg.setImageResource(R.drawable.bookmark_line);
                            if (HomeFragment1.isBookMarkedCBChecked) {
                                updateList();
                            }
                        }
                    }catch (Exception e){
                        //
                    }
                }
            });*/
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    public void updateListData(ArrayList<AstrologerDetailBean> list){
        arrayListRecyclerView.clear();
        arrayListRecyclerView.addAll(list);
        notifyDataSetChanged();
    }


}
