package com.ojassoft.astrosage.ui.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.ojassoft.astrosage.R;

public class FullProfileFragment extends DialogFragment {

    ImageView imgViewBack, imgViewAstrologor;
    TextView txtViewAstrologer;
    ProgressBar progressBar;
    public static String TAG = "FullProfileFragment";
    public static String IMAGE_URL = "image_url";
    public static String ASTROLOGER_NAME = "astrologer_name";
    String imageUrl;
    String astrologerName;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.Theme_Astrosage);
        if (getArguments() != null) {
            imageUrl = getArguments().getString(IMAGE_URL);
            astrologerName = getArguments().getString(ASTROLOGER_NAME);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_astrologor_full_profile, container);
        try {
            imgViewBack = view.findViewById(R.id.imgViewBack);
            imgViewAstrologor = view.findViewById(R.id.imgViewAstrologer);
            txtViewAstrologer = view.findViewById(R.id.txtViewAstrologer);
            progressBar = view.findViewById(R.id.progressBar);
            imgViewBack.setOnClickListener(imageview -> dismiss());
            txtViewAstrologer.setText(astrologerName);
            Glide.with(imgViewAstrologor)
                    .addDefaultRequestListener(new RequestListener<Object>(
                    ) {
                        @Override
                        public boolean onLoadFailed(@Nullable GlideException e, @Nullable Object model, @NonNull Target<Object> target, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }

                        @Override
                        public boolean onResourceReady(@NonNull Object resource, @NonNull Object model, Target<Object> target, @NonNull DataSource dataSource, boolean isFirstResource) {
                            progressBar.setVisibility(View.GONE);
                            return false;
                        }
                    })
                    .load( imageUrl)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imgViewAstrologor);


        } catch (Exception e){
            //
        }
        return view;
    }

    public static FullProfileFragment getInstant(String astrologerName,String profileImage){
        FullProfileFragment fragment = new FullProfileFragment();
        Bundle bundle = new Bundle();
        bundle.putString(IMAGE_URL,profileImage);
        bundle.putString(ASTROLOGER_NAME,astrologerName);
        fragment.setArguments(bundle);
        return fragment;
    }

}
