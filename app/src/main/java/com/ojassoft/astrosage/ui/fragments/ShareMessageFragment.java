package com.ojassoft.astrosage.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.provider.MediaStore;
import android.text.Html;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.varta.ui.activity.AIChatWindowActivity;
import com.ojassoft.astrosage.varta.ui.activity.AINotificationChatActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.FontUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Calendar;

public class ShareMessageFragment extends DialogFragment {
    public static final String QUESTION = "question";
    public static final String ANSWER = "answer";
    public static final String BIRTH_DETAILS = "birthDetails";
    public static final String TAG = "ShareMessageFragment";
    View view;
    String getQuestion = "";
    String birthDetails;
    String getAnswer = "";
    boolean isShared = false;
    Activity activity;

    ImageView ivCross;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme);
        if(getArguments() == null)
            dismiss();

        birthDetails = getArguments().getString(BIRTH_DETAILS);
        getAnswer = getArguments().getString(ANSWER);
        getQuestion = getArguments().getString(QUESTION);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        try {
            activity = getActivity();
            /*if (getActivity() instanceof AIChatWindowActivity) {
                activity = (AIChatWindowActivity) getActivity();
            }*/
            view = inflater.inflate(R.layout.share_maessage_layout, container);
            ImageView astroImg = view.findViewById(R.id.iv_profile_img);


            TextView tvAstroName = view.findViewById(R.id.tv_astrologer_name);
            FontUtils.changeFont(getActivity(), tvAstroName, "fonts/Roboto-Bold.ttf");
            TextView tvExperties = view.findViewById(R.id.tv_astrologer_exp);
            FontUtils.changeFont(getActivity(), tvExperties, "fonts/OpenSans-Light.ttf");


            TextView tvQuestion = view.findViewById(R.id.tv_question);
            TextView tv_birth_details = view.findViewById(R.id.tv_birth_details);
            TextView tvAnswer = view.findViewById(R.id.tv_answer);
            FontUtils.changeFont(getActivity(), tvQuestion, "fonts/OpenSans-Semibold.ttf");
            FontUtils.changeFont(getActivity(), tv_birth_details, "fonts/OpenSans-Light.ttf");
            FontUtils.changeFont(getActivity(), tvAnswer, "fonts/OpenSans-Light.ttf");

            if(birthDetails != null && !birthDetails.isEmpty()){
                tv_birth_details.setVisibility(View.VISIBLE);
            }else {
                tv_birth_details.setVisibility(View.GONE);
            }
            tv_birth_details.setText(birthDetails);

            Log.d("Question",getQuestion);
            if(getQuestion.isEmpty()){
                tvQuestion.setVisibility(View.GONE);
            }else {
                tvQuestion.setVisibility(View.VISIBLE);
            }
            tvQuestion.setText(getQuestion);

// Set the formatted text to the TextView
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                tvAnswer.setText(Html.fromHtml(getAnswer, Html.FROM_HTML_MODE_LEGACY));
            } else {
                tvAnswer.setText(Html.fromHtml(getAnswer));
            }
           // tvAnswer.setText(getAnswer);
            try {

                String largeImage = "", astrologerName = "";

                if (activity instanceof AIChatWindowActivity) {
                    largeImage = ((AIChatWindowActivity) activity).astrologerLageProfile;
                    astrologerName = ((AIChatWindowActivity) activity).astrologerName;
                } else if (activity instanceof AINotificationChatActivity) {
                    largeImage = ((AINotificationChatActivity) activity).astrologerDetailBean.getImageFileLarge();
                    astrologerName = ((AINotificationChatActivity) activity).astrologerDetailBean.getName();
                }

                if (activity != null && !TextUtils.isEmpty(largeImage)) {
                    String astroImage = CGlobalVariables.IMAGE_DOMAIN + largeImage;
                    Glide.with(astroImg)
                            .addDefaultRequestListener(new RequestListener<Object>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, @Nullable Object model, @NonNull Target<Object> target, boolean isFirstResource) {
                                   // startSharing();
                                    dismiss();
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
                }else {
                    Glide.with(getContext())
                            .addDefaultRequestListener(new RequestListener<Object>() {
                                @Override
                                public boolean onLoadFailed(@Nullable GlideException e, @Nullable Object model, @NonNull Target<Object> target, boolean isFirstResource) {
                                    // startSharing();
                                    dismiss();
                                    return false;
                                }

                                @Override
                                public boolean onResourceReady(@NonNull Object resource, @NonNull Object model, Target<Object> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                                    startSharing();
                                    return false;
                                }
                            })
                            .load(CGlobalVariables.IMAGE_DOMAIN + "/images/astrologer/ai-astro-2x/kundli-ai_sqr.jpg")
                            .into(astroImg);
                   // startSharing();
                }
                if(AstrosageKundliApplication.selectedAstrologerDetailBean!=null && AstrosageKundliApplication.selectedAstrologerDetailBean.getPrimaryExpertise()!=null){
                    tvExperties.setText(AstrosageKundliApplication.selectedAstrologerDetailBean.getPrimaryExpertise());
                }
                tvExperties.setText(AstrosageKundliApplication.selectedAstrologerDetailBean.getPrimaryExpertise());
                if(activity!=null &&  !TextUtils.isEmpty(astrologerName)){
                    tvAstroName.setText(astrologerName);
                }else {
                    tvAstroName.setText("AstroGPT");
                }
            }catch (Exception e){

            }
        } catch (Exception e){
            //
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

    public static ShareMessageFragment getInstance(String question, String answer) {
        Bundle bundle = new Bundle();
        bundle.putString(QUESTION, question);
        bundle.putString(ANSWER, answer);
        ShareMessageFragment fragment = new ShareMessageFragment();
        fragment.setArguments(bundle);
        return fragment;
    }
    public static ShareMessageFragment getInstance(String question, String answer,String birthDetails) {
        Bundle bundle = new Bundle();
        bundle.putString(QUESTION, question);
        bundle.putString(ANSWER, answer);
        bundle.putString(BIRTH_DETAILS, birthDetails);
        ShareMessageFragment fragment = new ShareMessageFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isShared) {
            clearCache();
            dismiss();
        }
    }

    public static Bitmap getScreenShot(View view) {
        if(view == null) return null;
        ScrollView scrollView = (ScrollView) view.findViewById(R.id.container_layout);
        int totalHeight = scrollView.getChildAt(0).getHeight();
        int totalWidth = scrollView.getChildAt(0).getWidth();
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

            // Prepare the share intent
            String body = requireContext().getString(R.string.shareAppBody);
            if (uri != null) {
                Intent shareIntent = new Intent();
                shareIntent.setAction(Intent.ACTION_SEND);
                shareIntent.putExtra(Intent.EXTRA_TEXT, body);
                shareIntent.setType("image/*");
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

}
