package com.ojassoft.astrosage.varta.adapters;

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
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
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
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.CircularNetworkImageView;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.varta.volley_network.VolleySingleton;

import java.util.ArrayList;


public class AIAstrologerAdapter extends RecyclerView.Adapter<AIAstrologerAdapter.ItemViewHolder> {

    private static final int TYPE_ITEM = 1;
    private static final int FOOTER_VIEW = 3;
    private final Context context;
    LoadMoreList loadMoreListInterface;
    private final ArrayList<AstrologerDetailBean> arrayListRecyclerView;


    public AIAstrologerAdapter(Context context, final LoadMoreList loadMoreListInterface, ArrayList<AstrologerDetailBean> arrayListRecyclerView) {
        this.context = context;
        this.loadMoreListInterface = loadMoreListInterface;
        this.arrayListRecyclerView = arrayListRecyclerView;
        setDisplayMetrics();
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

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.ai_astrologer_layout, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ItemViewHolder holder, int position) {

            if (arrayListRecyclerView == null || arrayListRecyclerView.size() < position) return;
            AstrologerDetailBean astrologerDetailBean = arrayListRecyclerView.get(position);
            if (astrologerDetailBean == null) return;

            if (astrologerDetailBean.isAvailableForChatBool() && astrologerDetailBean.isAvailableForCallBool()) {
                holder.chatNowBtn.setVisibility(View.VISIBLE);
                holder.callNowBtn.setVisibility(View.VISIBLE);
                if (astrologerDetailBean.getIsBusy().equalsIgnoreCase("true")) {
                    holder.chatNowBtn.setText(context.getResources().getString(R.string.busy));
                    holder.chatNowBtn.setBackgroundResource(R.drawable.bg_button_red);
                    holder.chatNowBtn.setTextColor(Color.WHITE);

                    holder.callNowBtn.setText(context.getResources().getString(R.string.busy));
                    holder.callNowBtn.setBackgroundResource(R.drawable.bg_button_red);
                    holder.callNowBtn.setTextColor(Color.WHITE);
                } else {
                    holder.chatNowBtn.setText(context.getResources().getString(R.string.chat_now));
                    holder.chatNowBtn.setBackgroundResource(R.drawable.bg_button_yellow);
                    holder.chatNowBtn.setTextColor(ActivityCompat.getColor(context, R.color.white));

                    holder.callNowBtn.setText(context.getResources().getString(R.string.call));
                    holder.callNowBtn.setBackgroundResource(R.drawable.bg_button_yellow);
                    holder.callNowBtn.setTextColor(ActivityCompat.getColor(context, R.color.white));
                }
            }else if(astrologerDetailBean.isAvailableForChatBool()) {
                holder.chatNowBtn.setVisibility(View.VISIBLE);
                holder.callNowBtn.setVisibility(View.GONE);
                if (astrologerDetailBean.getIsBusy().equalsIgnoreCase("true")) {
                    holder.chatNowBtn.setText(context.getResources().getString(R.string.busy));
                    holder.chatNowBtn.setBackgroundResource(R.drawable.bg_button_red);
                    holder.chatNowBtn.setTextColor(Color.WHITE);
                } else {
                    holder.chatNowBtn.setText(context.getResources().getString(R.string.chat_now));
                    holder.chatNowBtn.setBackgroundResource(R.drawable.bg_button_yellow);
                    holder.chatNowBtn.setTextColor(ActivityCompat.getColor(context, R.color.white));
                }
            }else{
                holder.callNowBtn.setVisibility(View.GONE);
                holder.chatNowBtn.setVisibility(View.VISIBLE);
                holder.chatNowBtn.setText(context.getResources().getString(R.string.chat_now));
                holder.chatNowBtn.setBackgroundResource(R.drawable.bg_button_yellow);
                holder.chatNowBtn.setTextColor(context.getColor(R.color.white));
            }

            if (astrologerDetailBean.getDoubleRating() != null && astrologerDetailBean.getDoubleRating().length() > 0) {
                if (astrologerDetailBean.getDoubleRating().equalsIgnoreCase("0.0") ||
                        astrologerDetailBean.getRating().equalsIgnoreCase("0") ||
                        astrologerDetailBean.getTotalRating().equalsIgnoreCase("0")) {
                    holder.ratingTxt.setVisibility(View.GONE);
                    holder.ratingTxtTotal.setVisibility(View.GONE);
                    holder.commentTV.setVisibility(View.GONE);
                    holder.ratingStar.setVisibility(View.GONE);
                } else {
                    holder.ratingTxt.setVisibility(View.VISIBLE);
                    holder.ratingTxtTotal.setVisibility(View.VISIBLE);
                    holder.commentTV.setVisibility(View.VISIBLE);
                    holder.ratingStar.setVisibility(View.VISIBLE);
                    holder.ratingTxt.setText(astrologerDetailBean.getDoubleRating());
                    holder.ratingTxtTotal.setText("(" + astrologerDetailBean.getTotalRating() + ")");
                }
            }

            String expStr = context.getResources().getString(R.string.year_of_experiance_astro).replace("#", astrologerDetailBean.getExperience());
            //holder.experienceTxt.setText(expStr);

            int actualPrice = 0;
            if (astrologerDetailBean.getActualServicePriceInt() != null && astrologerDetailBean.getActualServicePriceInt().length() > 0) {
                actualPrice = Integer.parseInt(astrologerDetailBean.getActualServicePriceInt());
            }

            int servicePrice = 0;
            if (astrologerDetailBean.getServicePrice() != null && astrologerDetailBean.getServicePrice().length() > 0) {
                servicePrice = Integer.parseInt(astrologerDetailBean.getServicePrice());
            }

            holder.verifiedTwoImg.setImageUrl(CUtils.getVerfiedImageSmall(), VolleySingleton.getInstance(context).getImageLoader());
            holder.verifiedImg.setImageUrl(CUtils.getVerfiedImageSmall(), VolleySingleton.getInstance(context).getImageLoader());

            if (actualPrice > servicePrice) {
                //show both prices
                String price = astrologerDetailBean.getActualServicePriceInt();
                CUtils.setStrikeOnTextView(holder.actualQuestionPriceTxt, price);
                String quePrice = context.getResources().getString(R.string.question_price).replace("$", astrologerDetailBean.getServicePrice());
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
            } else {
                String quePrice = context.getResources().getString(R.string.question_price).replace("$", astrologerDetailBean.getServicePrice());
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
            holder.newUser.setVisibility(View.GONE);//hide new user text
            CUtils.removeStrikeOnTextView(holder.questionPriceTxt);
            holder.ruppee_sign.setVisibility(View.VISIBLE);
            if (astrologerDetailBean.getPrimaryExpertise().isEmpty()) {
                holder.vedicAstrologerTxt.setText(astrologerDetailBean.getExpertise());
            } else {
                holder.vedicAstrologerTxt.setText(astrologerDetailBean.getPrimaryExpertise());
            }

            holder.languageTxt.setText(astrologerDetailBean.getLanguage());
            holder.astrologerNameTxt.setText(astrologerDetailBean.getName());
            holder.txtViewExpertise.setVisibility(View.VISIBLE);


            holder.txtViewExpertise.setText(astrologerDetailBean.getProfileTitle());

            String astrologerProfileUrl = "";
            if (astrologerDetailBean.getImageFile() != null && astrologerDetailBean.getImageFile().length() > 0) {
                astrologerProfileUrl = CGlobalVariables.IMAGE_DOMAIN + astrologerDetailBean.getImageFile();
                Glide.with(context.getApplicationContext()).load(astrologerProfileUrl).placeholder(R.drawable.ic_profile_view).error(R.drawable.ic_profile_view).circleCrop().into(holder.roundImage);
            }

            if (astrologerDetailBean.getFollowCount() == 0) {
                holder.ratingTxtTotalImg.setVisibility(View.GONE);
                holder.followersCountTV.setVisibility(View.GONE);
            } else {
                holder.ratingTxtTotalImg.setVisibility(View.VISIBLE);
                holder.followersCountTV.setText(String.valueOf(astrologerDetailBean.getFollowCount()));
                holder.followersCountTV.setVisibility(View.VISIBLE);
            }
            //Log.e("TestFree", "getUseIntroOffer()="+astrologerDetailBean.getUseIntroOffer());

            if (CUtils.isKundliAiProPlan(context)) {
//                holder.newUser.setVisibility(View.VISIBLE);
//                holder.newUser.setText(context.getResources().getString(R.string.text_free));
//                String quePrice = context.getResources().getString(R.string.question_price).replace("$", astrologerDetailBean.getServicePrice());
//                holder.questionPriceTxt.setText(quePrice);
//                CUtils.setStrikeOnTextView(holder.questionPriceTxt, quePrice);
//                holder.questionPriceTxt.setVisibility(View.GONE);
//                holder.actualQuestionPriceTxt.setVisibility(View.GONE);
//                holder.ruppee_sign.setVisibility(View.GONE);

                holder.newUser.setVisibility(View.GONE);//hide new user text
                CUtils.removeStrikeOnTextView(holder.questionPriceTxt);
                holder.ruppee_sign.setVisibility(View.VISIBLE);
                holder.chatNowBtn.setText(context.getResources().getString(R.string.free_chat_now));
                FontUtils.changeFont(context, holder.chatNowBtn, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
                FontUtils.changeFont(context, holder.callNowBtn, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
                holder.chatNowBtn.setTextSize(14);
                holder.callNowBtn.setTextSize(14);

            }
            //else {

                if (astrologerDetailBean.getUseIntroOffer()) {
                    String offerType = astrologerDetailBean.getIntroOfferType();
                    //Log.e("TestFree", "offerType()="+offerType);
                    if (astrologerDetailBean.isFreeForChat()) {
                        if (!TextUtils.isEmpty(offerType) && offerType.equals(CGlobalVariables.INTRO_OFFER_TYPE_FREE)) {
                            holder.newUser.setVisibility(View.VISIBLE);
                            holder.newUser.setText(context.getResources().getString(R.string.text_free));
                            String quePrice = context.getResources().getString(R.string.question_price).replace("$", astrologerDetailBean.getServicePrice());
                            holder.questionPriceTxt.setText(quePrice);
                            CUtils.setStrikeOnTextView(holder.questionPriceTxt, quePrice);
                            holder.questionPriceTxt.setVisibility(View.GONE);
                            holder.actualQuestionPriceTxt.setVisibility(View.GONE);
                            holder.ruppee_sign.setVisibility(View.GONE);
                            holder.chatNowBtn.setText(context.getResources().getString(R.string.chat_now));
                        } else if (!TextUtils.isEmpty(offerType) && offerType.contains(CGlobalVariables.REDUCED_PRICE_OFFER)) {
                            CUtils.removeStrikeOnTextView(holder.questionPriceTxt);
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
                    }
                } else {
                    holder.newUser.setVisibility(View.GONE);//hide new user text
                    CUtils.removeStrikeOnTextView(holder.questionPriceTxt);
                    holder.ruppee_sign.setVisibility(View.VISIBLE);
                }

           // }
            holder.chatNowBtn.setTag(position);
            holder.upperLayout.setTag(position);

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

    @Override
    public int getItemCount() {
        if (arrayListRecyclerView == null) {
            return 0;
        } else {
            return arrayListRecyclerView.size();
        }
    }

    public class ItemViewHolder extends RecyclerView.ViewHolder {

        TextView ratingTxt, ratingTxtTotal, chatNowBtn, callNowBtn, questionPriceTxt,
                vedicAstrologerTxt, languageTxt, astrologerNameTxt, actualQuestionPriceTxt,txtViewExpertise,
                ruppee_sign, newUser, followersCountTV, tvExp;
        ImageView ratingTxtTotalImg, commentTV;
        CircularNetworkImageView roundImage;
        NetworkImageView verifiedImg, verifiedTwoImg;
        RelativeLayout mainLayout;
        ImageView onlineOfflineImg, ratingStar;
        LinearLayout upperLayout;
        ImageView progressBarIV;

        public ItemViewHolder(View itemView) {
            super(itemView);

            actualQuestionPriceTxt = itemView.findViewById(R.id.actual_question_price_txt);
            ruppee_sign = itemView.findViewById(R.id.ruppee_sign);
            newUser = itemView.findViewById(R.id.new_user);
            ratingStar = itemView.findViewById(R.id.rating_star);
            ratingTxt = itemView.findViewById(R.id.rating_txt);
            ratingTxtTotal = itemView.findViewById(R.id.rating_txt_total);
            ratingTxtTotalImg = itemView.findViewById(R.id.rating_tag);
            //tvExp = itemView.findViewById(R.id.tv_exp_lbl);
            commentTV = itemView.findViewById(R.id.comment);
            followersCountTV = itemView.findViewById(R.id.followersCountTV);
            //experienceTxt = itemView.findViewById(R.id.experience_txt);
            chatNowBtn = itemView.findViewById(R.id.chat_now_btn);
            callNowBtn = itemView.findViewById(R.id.call_now_btn);
            questionPriceTxt = itemView.findViewById(R.id.question_price_txt);
            vedicAstrologerTxt = itemView.findViewById(R.id.vedic_astrologer_txt);
            languageTxt = itemView.findViewById(R.id.language_txt);
            astrologerNameTxt = itemView.findViewById(R.id.astrologer_name_txt);
            txtViewExpertise = itemView.findViewById(R.id.tetViewExpertise);
            roundImage = itemView.findViewById(R.id.ri_profile_img);
            mainLayout = itemView.findViewById(R.id.mainlayout);
            verifiedImg = itemView.findViewById(R.id.verified_img);
            verifiedTwoImg = itemView.findViewById(R.id.verified_two_img);
            progressBarIV = itemView.findViewById(R.id.progressBarIV);

            onlineOfflineImg = itemView.findViewById(R.id.online_offline_img);
            upperLayout = itemView.findViewById(R.id.upper_layout);
            RelativeLayout rlRatingAndReview = itemView.findViewById(R.id.rlRatingAndReview);
            FontUtils.changeFont(context, astrologerNameTxt, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
            FontUtils.changeFont(context, txtViewExpertise, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
            FontUtils.changeFont(context, ratingTxt, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
            FontUtils.changeFont(context, ratingTxtTotal, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
            FontUtils.changeFont(context, followersCountTV, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
            //FontUtils.changeFont(context, tvExp, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
            //FontUtils.changeFont(context, experienceTxt, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
            FontUtils.changeFont(context, questionPriceTxt, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
            FontUtils.changeFont(context, actualQuestionPriceTxt, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
            FontUtils.changeFont(context, ruppee_sign, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
            FontUtils.changeFont(context, newUser, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
            FontUtils.changeFont(context, vedicAstrologerTxt, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
            FontUtils.changeFont(context, languageTxt, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);

            actualQuestionPriceTxt.setVisibility(View.GONE); // SAN

            FontUtils.changeFont(context, chatNowBtn, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
            rlRatingAndReview.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    new AlertDialog.Builder(context)
                            .setTitle("Astrologer")
                            .setMessage("Astrologer Items (  " + CUtils.getAstroList() + "   )")
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
                        bundle.putInt("useIntroOffer", introOffer ? 1 : 2);
                        bundle.putInt("isFreeForChat", arrayListRecyclerView.get(pos).isFreeForChat() ? 1 : 2);
                        bundle.putInt("isFreeForCall", arrayListRecyclerView.get(pos).isFreeForChat() ? 1 : 2);
                        if (context instanceof DashBoardActivity) {
                            bundle.putBoolean("fromDashboard", true);
                        }
                        bundle.putBoolean("isAIAstrologer", true);
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_HOME_ASTRO_DETAIL_CLICK, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                        Intent intent = new Intent(context, AstrologerDescriptionActivity.class);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
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
                        CUtils.fcmAnalyticsEvents("ai_chat_btn_click_astrologer_list", AstrosageKundliApplication.currentEventType, "");

                        int pos = Integer.parseInt(chatNowBtn.getTag().toString());
                        boolean isLogin = CUtils.getUserLoginStatus(context);
                        if (isLogin) {
                            com.ojassoft.astrosage.utils.CUtils.createSession(context, "ALCHT");
                            CUtils.saveStringData(context, CGlobalVariables.PREF_CHAT_BUTTON_CLICK, "chat");
                            loadMoreListInterface.chatAstrologer(arrayListRecyclerView.get(pos));
                        } else {
                            CUtils.fcmAnalyticsEvents("ai_chat_user_not_login",
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

            callNowBtn.setOnClickListener((v)->{
                try {
                    AstrosageKundliApplication.currentEventType = CGlobalVariables.CALL_BTN_CLICKED;
                    CUtils.fcmAnalyticsEvents("ai_call_btn_click_astrologer_list", AstrosageKundliApplication.currentEventType, "");

                    int pos = Integer.parseInt(chatNowBtn.getTag().toString());
                    boolean isLogin = CUtils.getUserLoginStatus(context);
                    if (isLogin) {
                        com.ojassoft.astrosage.utils.CUtils.createSession(context, "ALCHT");
                        CUtils.saveStringData(context, CGlobalVariables.PREF_CHAT_BUTTON_CLICK, "call");
                        AstrologerDetailBean astrologerDetailBean = arrayListRecyclerView.get(pos);
                        astrologerDetailBean.setCallSource(CGlobalVariables.AI_LIST_VIEW_CALL_BTN);
                        loadMoreListInterface.callAstrologer(astrologerDetailBean);
                    } else {
                        CUtils.fcmAnalyticsEvents("ai_call_user_not_login",
                                AstrosageKundliApplication.currentEventType, "");
                        Intent intent = new Intent(context, LoginSignUpActivity.class);
                        intent.putExtra(CGlobalVariables.IS_FROM_SCREEN, CGlobalVariables.DASHBOARD_CALL_NOW_SCRREN);
                        context.startActivity(intent);
                    }
                } catch (Exception e) {
                    //
                }
            });
        }

    }

}
