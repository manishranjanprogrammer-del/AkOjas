package com.ojassoft.astrosage.ui.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.DrawableCompat;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

/**
 * Created by ojas on 7/6/16.
 */
public class AppRateFrag extends AstroSageParentDialogFragment {

    public AppRateFrag(){}

    Activity activity;
    Typeface regularTypeface;
    Typeface mediumTypeface;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
        regularTypeface = ((BaseInputActivity) activity).regularTypeface;
        mediumTypeface = ((BaseInputActivity) activity).mediumTypeface;

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final Dialog dialog = getDialog();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.lay_app_rate_frag, container);//lay_app_rate_frag

        TextView textViewHeading = (TextView) view.findViewById(R.id.textViewHeading);
        textViewHeading.setTypeface(((BaseInputActivity) activity).mediumTypeface);
        textViewHeading.setText(getActivity().getResources().getString(R.string.app_mainheading_text_kundali));

        TextView textViewsubheading = (TextView) view.findViewById(R.id.textViewsubheading);
        textViewsubheading.setTypeface(((BaseInputActivity) activity).regularTypeface);
        textViewsubheading.setText(getActivity().getResources().getString(R.string.share_app_sub_heading));

        TextView textViewsubchildheading = (TextView) view.findViewById(R.id.textViewsubchildheading);
        textViewsubchildheading.setTypeface(((BaseInputActivity) activity).regularTypeface);
        textViewsubchildheading.setText(Html.fromHtml(getActivity().getResources().getString(R.string.app_subchild_text_kundali)));

        TextView ratenowtxt = (TextView) view
                .findViewById(R.id.textViewRatenow);
        // ratenowtxt.setTypeface(typeface, Typeface.BOLD);
        ratenowtxt.setTypeface(((BaseInputActivity) activity).mediumTypeface);
        ratenowtxt.setText(getActivity().getString(R.string.app_ratenow));

        TextView ratenotnowtxt = (TextView) view
                .findViewById(R.id.textViewNotNowrating);
        ratenotnowtxt.setTypeface(((BaseInputActivity) activity).mediumTypeface);
        ratenotnowtxt.setText(getActivity().getString(R.string.app_donot_rate_now));

        CUtils.fcmAnalyticsEvents(CGlobalVariables.OPEN_APP_RATE_DIALOG,
                CGlobalVariables.FIREBASE_EVENT_OPEN_SCREEN, "");


        final RatingBar ratingBar = (RatingBar) view.findViewById(R.id.ratingBar);
        //LayerDrawable stars = (LayerDrawable) ratingBar.getProgressDrawable();
        DrawableCompat.setTint(ratingBar.getProgressDrawable(), getResources().getColor(R.color.ColorPrimary));
        // stars.getDrawable(2).setColorFilter(getResources().getColor(R.color.ColorPrimary), PorterDuff.Mode.SRC_ATOP);
        // stars.getDrawable(1).setColorFilter(getResources().getColor(R.color.ColorPrimary), PorterDuff.Mode.SRC_ATOP);
        //  stars.getDrawable(0).setColorFilter(getResources().getColor(R.color.light_slate_gray), PorterDuff.Mode.SRC_ATOP);

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if (rating < 1.0f)
                    ratingBar.setRating(1.0f);
            }
        });


        ratenowtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float rating = ratingBar.getRating();
                CUtils.fcmAnalyticsEvents(CGlobalVariables.DIALOG_RATE_NOW_CLICK,
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                checkRatingData(rating);
                dialog.dismiss();
            }
        });

        ratenotnowtxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CUtils.fcmAnalyticsEvents(CGlobalVariables.DIALOG_RATE_NOW_CANCEL_CLICK,
                        CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                dialog.dismiss();
            }
        });

        return view;

    }

    private void checkRatingData(float rating) {
        try {
            if (rating > 4) {
                CUtils.rateAppication(activity, true);
            } else {
                MyCustomToast mct = new MyCustomToast(activity,
                        activity.getLayoutInflater(),
                        activity, Typeface.DEFAULT);
                mct.show(getActivity().getResources().getString(R.string.feedback_sent));
            }

        } catch (Exception ex) {
            //Log.i("Exception", ex.getMessage().toString());
        }
    }


    public static AppRateFrag getInstance() {
        AppRateFrag appRateFrag = new AppRateFrag();
        return appRateFrag;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }
}
