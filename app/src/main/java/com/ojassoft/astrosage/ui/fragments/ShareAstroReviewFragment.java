package com.ojassoft.astrosage.ui.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatRatingBar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.model.AstrologerDetailBean;
import com.ojassoft.astrosage.varta.model.UserReviewBean;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.FontUtils;

import java.io.File;
import java.io.FileOutputStream;

public class ShareAstroReviewFragment extends DialogFragment {
    View view;
    Context context;
    boolean isShared = false;
    public static final String ASTROLOGER_NAME_KEY = "astrologer_name_key";
    public static final String ASTRO_EXP_KEY = "astrologer_exp_key";
    public static final String REVIEW_DATE_KEY = "review_date_key";
    public static final String REVIEW_COUNT_KEY = "review_count_key";
    public static final String REVIEW_COMMENT_KEY = "review_comment_key";
    public static final String ASTROLOGER_IMAGE_PATH_KEY = "astrologer_img_path_key";
    public static final String ASTROLOGER_PROFILE_URL = "astrologer_profile_url_key";

    private String astrologerName;
    private String astroExp;
    private String reviewDate;
    private String reviewCount;
    private String reviewComment;
    private String imagePath;
    private String astroUrl;


    public static ShareAstroReviewFragment getInstance(UserReviewBean userReviewBean, AstrologerDetailBean astrologerDetailBean) {
        Bundle bundle = new Bundle();
        bundle.putString(ASTROLOGER_NAME_KEY, astrologerDetailBean.getName());
        bundle.putString(ASTRO_EXP_KEY, astrologerDetailBean.getPrimaryExpertise());
        bundle.putString(REVIEW_DATE_KEY, userReviewBean.getDate());
        bundle.putString(REVIEW_COUNT_KEY, userReviewBean.getRate());
        bundle.putString(ASTROLOGER_IMAGE_PATH_KEY, astrologerDetailBean.getImageFileLarge());
        bundle.putString(REVIEW_COMMENT_KEY, userReviewBean.getComment());
        bundle.putString(ASTROLOGER_PROFILE_URL,astrologerDetailBean.getUrlText());

        ShareAstroReviewFragment fragment = new ShareAstroReviewFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_NoActionBar);
        if(getArguments() == null)
            dismiss();

        context = getContext();
        astrologerName = getArguments().getString(ASTROLOGER_NAME_KEY);
        astroExp = getArguments().getString(ASTRO_EXP_KEY);
        reviewDate = getArguments().getString(REVIEW_DATE_KEY);
        reviewCount = getArguments().getString(REVIEW_COUNT_KEY);
        reviewComment = getArguments().getString(REVIEW_COMMENT_KEY);
        imagePath = getArguments().getString(ASTROLOGER_IMAGE_PATH_KEY);
        astroUrl = getArguments().getString(ASTROLOGER_PROFILE_URL);

    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        try {
            view = inflater.inflate(R.layout.share_astrologer_review_layout, container, false);
            TextView tvAstroName = view.findViewById(R.id.astro_name_tv);
            TextView tvExperties = view.findViewById(R.id.astro_exp_tv);
            TextView tvReviewDate = view.findViewById(R.id.review_date_tv);
            TextView tvReviewText = view.findViewById(R.id.review_text_tv);
            com.ojassoft.astrosage.varta.utils.RoundImage astroImg = view.findViewById(R.id.astro_profile_image);

            FontUtils.changeFont(context, tvAstroName, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
            FontUtils.changeFont(context, tvExperties, CGlobalVariables.FONTS_OPEN_SANS_LIGHT);
            FontUtils.changeFont(context, tvReviewText, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);


            AppCompatRatingBar ratingBar = view.findViewById(R.id.star_rating);
            ratingBar.setRating(Float.parseFloat(reviewCount));
            tvAstroName.setText(astrologerName);
            tvExperties.setText(astroExp);
            tvReviewDate.setText(reviewDate);
            tvReviewText.setText(reviewComment);

            String astroImage = CGlobalVariables.IMAGE_DOMAIN + imagePath;
            Glide.with(context)
                    .addDefaultRequestListener(new RequestListener<Object>() {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, @Nullable Object model, @NonNull Target<Object> target, boolean isFirstResource) {
                             startSharing();
//                            dismiss();
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(@NonNull Object resource, @NonNull Object model, Target<Object> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                            startSharing();
                            return false;
                        }
                    })
                    .load(astroImage)
                    .into(astroImg);

        }catch(Exception e){
            dismiss();
        }

        return view;

    }


    private void startSharing() {
        try {
            new Handler(Looper.getMainLooper()).postDelayed(() -> {
                if(!isShared) {
                    shareImage(getScreenShot(view));
                    isShared = true;
                }
            }, 1000);
        }catch(Exception e){
            shareImage(getScreenShot(view));
            isShared = true;
        }
    }
    public static Bitmap getScreenShot(View view) {
        if(view == null) return null;
        ScrollView mainView = view.findViewById(R.id.review_container_layout);
        int totalHeight = mainView.getChildAt(0).getHeight();
        int totalWidth = mainView.getChildAt(0).getWidth();
        Bitmap bitmap = Bitmap.createBitmap(totalWidth, totalHeight, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);
        return bitmap;
    }
    File cachePath;
    private void shareImage(Bitmap bitmap) {
        try {
            cachePath = new File(requireContext().getCacheDir(), "images/");
            cachePath.mkdirs(); // Create the directory if it doesn't exist
            File file = new File(cachePath, "shared_image.png");
            FileOutputStream stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream); // Save the bitmap
            stream.close();

            // Get the URI of the saved file
            Uri uri = FileProvider.getUriForFile(requireContext(),
                    requireContext().getPackageName(),
                    file);

            if (uri != null) {
                String astroProfileUrl = CGlobalVariables.varta_astrosage_urls + CGlobalVariables.LINK_HAS_ASTROLOGER +astroUrl + CGlobalVariables.PARTNER_ID_SHARE_ASTRO;

                String body = "View Profile: "+astroProfileUrl;
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.setType("image/*");
                shareIntent.putExtra(Intent.EXTRA_TEXT, body);
                shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); // Temp permission for receiving app to read this file
                shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                startActivity(Intent.createChooser(shareIntent, "Share Image on Social Media"));
            }
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    private void clearCache() {
        if (cachePath != null && cachePath.isDirectory()) {
            File[] files = cachePath.listFiles();
            if (files != null) {
                for (File file : files) {
                    file.delete();
                }
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (isShared) {
            clearCache();
            dismiss();
        }
    }
}
