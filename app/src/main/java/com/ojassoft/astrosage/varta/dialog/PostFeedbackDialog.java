package com.ojassoft.astrosage.varta.dialog;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.model.AstrologerDetailBean;
import com.ojassoft.astrosage.varta.model.UserReviewBean;
import com.ojassoft.astrosage.varta.ui.activity.AIChatWindowActivity;
import com.ojassoft.astrosage.varta.ui.activity.AstrologerDescriptionActivity;
import com.ojassoft.astrosage.varta.ui.activity.BaseActivity;
import com.ojassoft.astrosage.varta.ui.activity.ChatWindowActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.ChatUtils;
import com.ojassoft.astrosage.varta.utils.CircularNetworkImageView;
import com.ojassoft.astrosage.varta.utils.FontUtils;
import com.ojassoft.astrosage.vartalive.activities.LiveActivityNew;

public class PostFeedbackDialog extends BottomSheetDialogFragment implements View.OnClickListener {
    Activity activity;
    String astroImage, astroName, mobileNo, astrologerId, urlText;
    CircularNetworkImageView ivPostRatingShare;
    TextView tvPostRatingChatAgain, tvPostRatingCallAgain, tvPostRatingHeading, tvPostRatingGift, tvPostRatingShare, tvPostRatingRateAK;
    ImageView ivPostRatingClose;
    ConstraintLayout clPostRatingGift, clPostRatingShare, clPostRatingRateAK;
    UserReviewBean userReviewBean;
    AstrologerDetailBean astrologerDetailBean;
    LinearLayout llPostRatingButtons;
    public PostFeedbackDialog(Activity activity, String astroName, String astroImage, String mobileNo, String astrologerId, UserReviewBean userReviewBean) {
        this.activity = activity;
        this.astroImage = astroImage;
        this.astroName = astroName;
        this.mobileNo = mobileNo;
        this.astrologerId = astrologerId;
        this.userReviewBean = userReviewBean;
    }

    public PostFeedbackDialog(Activity activity, AstrologerDetailBean astrologerDetailBean, UserReviewBean userReviewBean) {
        this.activity = activity;
        this.astrologerDetailBean = astrologerDetailBean;
        this.userReviewBean = userReviewBean;
    }

    public PostFeedbackDialog() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.MyBottomSheetDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.layout_post_rating_dialog, container, false);
        try {
            tvPostRatingChatAgain = view.findViewById(R.id.tvPostRatingChatAgain);
            tvPostRatingCallAgain = view.findViewById(R.id.tvPostRatingCallAgain);
            ivPostRatingClose = view.findViewById(R.id.ivPostRatingClose);
            clPostRatingGift = view.findViewById(R.id.clPostRatingGift);
            clPostRatingShare = view.findViewById(R.id.clPostRatingShare);
            clPostRatingRateAK = view.findViewById(R.id.clPostRatingRateAK);
            tvPostRatingHeading = view.findViewById(R.id.tvPostRatingHeading);
            tvPostRatingGift = view.findViewById(R.id.tvPostRatingGift);
            tvPostRatingShare = view.findViewById(R.id.tvPostRatingShare);
            tvPostRatingRateAK = view.findViewById(R.id.tvPostRatingRateAK);
            ivPostRatingShare = view.findViewById(R.id.ivPostRatingShare);
            llPostRatingButtons = view.findViewById(R.id.llPostRatingButtons);

            if (activity instanceof LiveActivityNew) {
                llPostRatingButtons.setVisibility(View.GONE);
            }

            // if astrologer is not available for call hide call button
            if (astrologerDetailBean.getIsAvailableForCall().equals("false")) {
                tvPostRatingCallAgain.setVisibility(View.GONE);
            }

            //if astrologer is not available for chat hide chat button
            if (astrologerDetailBean.getIsAvailableForChat().equals("false")) {
                tvPostRatingChatAgain.setVisibility(View.GONE);
            }

            FontUtils.changeFont(activity, tvPostRatingChatAgain, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
            FontUtils.changeFont(activity, tvPostRatingCallAgain, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
            FontUtils.changeFont(activity, tvPostRatingHeading, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
            FontUtils.changeFont(activity, tvPostRatingGift, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
            FontUtils.changeFont(activity, tvPostRatingShare, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
            FontUtils.changeFont(activity, tvPostRatingRateAK, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);

            mobileNo = CUtils.getUserID(activity);
            astrologerId = astrologerDetailBean.getAstrologerId();
            astroImage = astrologerDetailBean.getImageFile();
            astroName = astrologerDetailBean.getName();
            urlText = astrologerDetailBean.getUrlText();

            if (astroImage != null && astroImage.length() > 0) {
                String image = CGlobalVariables.IMAGE_DOMAIN + astroImage;
                Glide.with(activity.getApplicationContext()).load(image).circleCrop().into(ivPostRatingShare);
            }

            ivPostRatingClose.setOnClickListener(this);
            clPostRatingGift.setOnClickListener(this);
            clPostRatingShare.setOnClickListener(this);
            clPostRatingRateAK.setOnClickListener(this);
            tvPostRatingChatAgain.setOnClickListener(this);
            tvPostRatingCallAgain.setOnClickListener(this);
        } catch (Exception e){
            //
        }
        return view;

    }

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            FragmentTransaction ft = manager.beginTransaction();
            ft.add(this, tag).addToBackStack(null);
            ft.commit();
        } catch (IllegalStateException e) {
            Log.d("ABSDIALOGFRAG", "Exception", e);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ivPostRatingClose:
                dismiss();
                break;
            case R.id.clPostRatingGift:
                if (activity instanceof ChatWindowActivity || activity instanceof AIChatWindowActivity) {
                    RatingAndDakshinaDialog ratingAndDakshinaDialog = new RatingAndDakshinaDialog(getContext(), activity, mobileNo, astrologerId, userReviewBean);
                    ratingAndDakshinaDialog.show(((BaseActivity) activity).getSupportFragmentManager(), "FeedbackDialog");
                } else if (activity instanceof AstrologerDescriptionActivity) {
                    ((AstrologerDescriptionActivity) activity).ivAstroDescGift.performClick();
                } else if (activity instanceof LiveActivityNew) {
                    ((LiveActivityNew) activity).imgliveGift.performClick();
                }
                dismiss();
                break;
            case R.id.clPostRatingShare:
                if (activity instanceof AstrologerDescriptionActivity) {
                    ((AstrologerDescriptionActivity) activity).ivAstroDescShare.performClick();
                } else {
                    CUtils.shareAstrologer(getContext(), astrologerDetailBean,null);
                }
                dismiss();
                break;
            case R.id.clPostRatingRateAK:
                com.ojassoft.astrosage.utils.CUtils.rateAppication(activity, true);
                dismiss();
                break;
            case R.id.tvPostRatingChatAgain:
                if (activity instanceof ChatWindowActivity) {
                    ((ChatWindowActivity) activity).btnChatAgain.performClick();
                }else if (activity instanceof AIChatWindowActivity) {
                    ((AIChatWindowActivity) activity).btnChatAgain.performClick();
                } else if (activity instanceof AstrologerDescriptionActivity) {
                    ((AstrologerDescriptionActivity) activity).chatNowBtn.performClick();
                }
                dismiss();
                break;
            case R.id.tvPostRatingCallAgain:
                if (activity instanceof ChatWindowActivity) {
                    ((ChatWindowActivity) activity).btnCallAgain.performClick();
                } else if (activity instanceof AstrologerDescriptionActivity) {
                    ((AstrologerDescriptionActivity) activity).callNowBtn.performClick();
                }
                dismiss();
                break;
        }
    }
}