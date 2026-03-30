package com.ojassoft.astrosage.varta.adapters;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.RecyclerView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.interfacefile.LoadMoreList;
import com.ojassoft.astrosage.varta.model.UserReviewBean;
import com.ojassoft.astrosage.varta.ui.activity.AstrologerDescriptionActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.CircularNetworkImageView;
import com.ojassoft.astrosage.varta.utils.FontUtils;

import java.util.ArrayList;

public class AstrologerUserReciewAdapter extends RecyclerView.Adapter<AstrologerUserReciewAdapter.MyViewHolder> {

    Context context;
    ArrayList<UserReviewBean> userReviewBeanArrayList;
    boolean isShowLoadMore = true;
    LoadMoreList loadMoreListInterface;

    public AstrologerUserReciewAdapter(Context context, ArrayList<UserReviewBean> userReviewBeanArrayList)
    {
        this.context = context;
        this.userReviewBeanArrayList = userReviewBeanArrayList;
    }

    public void AstrologerUserFeedbackUpdate(ArrayList<UserReviewBean> userReviewBeanArrayList)
    {
        this.context = context;
        if(userReviewBeanArrayList != null && userReviewBeanArrayList.size()>0) {
            this.userReviewBeanArrayList = userReviewBeanArrayList;
        }
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        UserReviewBean userReviewBean = new UserReviewBean();
        userReviewBean = userReviewBeanArrayList.get(position);
        if(userReviewBean.getMaskedusername() != null && userReviewBean.getMaskedusername().length()>0) {
            holder.userNameTxt.setText(userReviewBean.getMaskedusername());
        }else{
            holder.userNameTxt.setText(userReviewBean.getUsername());
        }
        if(userReviewBean.getComment() != null && userReviewBean.getComment().length()>0 && !userReviewBean.getComment().equalsIgnoreCase("null")){
            holder.userReviewTxt.setText(userReviewBean.getComment());
        }

        holder.ratingStar.setRating(Float.parseFloat(userReviewBean.getRate()));
        holder.dateTxt.setText(userReviewBean.getDate());
        if(!TextUtils.isEmpty(userReviewBean.getUserRatingType())&& !TextUtils.isEmpty(userReviewBean.getUserRatingTypeValue())) {
            holder.ratingName.setText(userReviewBean.getUserRatingType());
            switch (userReviewBean.getUserRatingTypeValue()) {
                case "1":
                    holder.ratingName.setTextColor(Color.parseColor("#d03f06"));
                    break;
                case "2":
                    holder.ratingName.setTextColor(Color.parseColor("#dc8f25"));
                    break;
                case "3":
                    holder.ratingName.setTextColor(Color.parseColor("#ff9001"));
                    break;
            }
        }
        /*String astrologerProfileUrl="";
        if(userReviewBean.getImageFile() != null && userReviewBean.getImageFile().length()>0) {
            astrologerProfileUrl = CGlobalVariables.IMAGE_DOMAIN + userReviewBean.getImageFile();
            holder.profile_img.setImageUrl(astrologerProfileUrl, VolleySingleton.getInstance(context).getImageLoader());
        }*/
        final String reviewId = userReviewBean.getActualFeedbackId();
        holder.actionBtn.setOnClickListener(v ->
                showReviewActionPopup(v, reviewId)
        );
        UserReviewBean finalUserReviewBean = userReviewBean;
        holder.knowMoreRating.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!TextUtils.isEmpty(finalUserReviewBean.getUserRatingType())&& !TextUtils.isEmpty(finalUserReviewBean.getUserRatingTypeValue())) {
                    switch (finalUserReviewBean.getUserRatingTypeValue()) {
                        case "1":
                            showInfoDialog(context.getResources().getString(R.string.verified_purchase_ratings_after_5_minute_calls_using_paid_balance));
                            break;
                        case "2":
                            showInfoDialog(context.getResources().getString(R.string.client_rating_ratings_after_calls_under_5_minutes_using_paid_balance));
                            break;
                        case "3":
                            showInfoDialog(context.getResources().getString(R.string.free_rating_ratings_from_sessions_with_promotional_or_free_balance));
                            break;
                    }
                }
            }
        });

    }


    private void showInfoDialog(String info) {
        final Dialog dialog = new Dialog(context);
        Window window = dialog.getWindow();
        window.setGravity(Gravity.CENTER);
        dialog.setTitle(null);
        dialog.setContentView(R.layout.single_message_dialog);
        TextView txtMessage = dialog.findViewById(R.id.txtMessage);
        txtMessage.setText(info);
        dialog.show();
    }
    @Override
    public int getItemCount() {
        int count = 0;
        if(userReviewBeanArrayList.size()>0)
        {
            count = userReviewBeanArrayList.size();
        }
        return count;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView userNameTxt, userReviewTxt, dateTxt,ratingName;
        CircularNetworkImageView profile_img;
        AppCompatRatingBar ratingStar;
        ImageView actionBtn,knowMoreRating;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            dateTxt = (TextView) itemView.findViewById(R.id.date_txt);
            userNameTxt = (TextView) itemView.findViewById(R.id.user_name_txt);
            userReviewTxt = (TextView) itemView.findViewById(R.id.user_review_txt);
            profile_img = (CircularNetworkImageView) itemView.findViewById(R.id.profile_img);
            ratingStar = (AppCompatRatingBar)itemView.findViewById(R.id.rating_star);
            ratingName = itemView.findViewById(R.id.ratingName);
            actionBtn = itemView.findViewById(R.id.actionBtn);
            knowMoreRating = itemView.findViewById(R.id.knowMoreRating);

            //knowMoreRating = itemView.findViewById(R.id.knowMoreRating);
            //LayerDrawable stars = (LayerDrawable) ratingStar.getProgressDrawable();
            //stars.getDrawable(2).setColorFilter(context.getResources().getColor(R.color.yellow), PorterDuff.Mode.SRC_ATOP);

            FontUtils.changeFont(context, userNameTxt, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
            FontUtils.changeFont(context, userReviewTxt, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
            FontUtils.changeFont(context, ratingName, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
            FontUtils.changeFont(context, dateTxt, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);

        }
    }

    public void showReviewActionPopup(View v, String reviewId) {
        try {
            // Create PopupMenu with the applied style
            ContextThemeWrapper wrapper = new ContextThemeWrapper(context, R.style.PopupMenuStyle);
            PopupMenu popup = new PopupMenu(wrapper, v);

            popup.setOnMenuItemClickListener(item -> {
                switch (item.getItemId()) {
                    case R.id.report:
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_REPORT_REVIEW, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                        reviewAction(CGlobalVariables.REPORT_REVIEW, reviewId);
                        return true;
                    case R.id.block:
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_BLOCK_REVIEW, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                        reviewAction(CGlobalVariables.BLOCK_REVIEW, reviewId);
                        return true;
                    case R.id.share:
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.FBA_SHARE_ASTROLOGER_REVIEW, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                        if(context instanceof AstrologerDescriptionActivity) {
                            ((AstrologerDescriptionActivity)context).shareReview(reviewId);
                        }
                        return true;
                    default:
                        return false;
                }
            });
            popup.inflate(R.menu.poupup_menu_review);
            popup.show();
        }catch (Exception e){
            //
        }
    }

    private void reviewAction(int actionType, String reviewId){
        try{
            if(context instanceof AstrologerDescriptionActivity){
                ((AstrologerDescriptionActivity)context).reportOrBlockReview(actionType, reviewId);
            }
        }catch (Exception e){
            //
        }
    }
}