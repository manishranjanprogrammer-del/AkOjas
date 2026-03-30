package com.ojassoft.astrosage.ui.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.jinterface.ISearchBirthDeatils;
import com.ojassoft.astrosage.ui.act.DirectoryListingProfileImageActivity;
import com.ojassoft.astrosage.ui.act.JoinVartaProfileImageActivity;

/**
 * Created by ojas on 4/5/16.
 */
public class SetChoosePhotoDialoge extends AstroSageParentDialogFragment {


    ISearchBirthDeatils callback;
    BeanHoroPersonalInfo beanHoroPersonalInfo;
    boolean isCheckForDeletePersonalKundli, isLocal;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final Dialog dialog = getDialog();
        LinearLayout picfromcamera, picfromgallery;

        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.set_profile_picture_dialoge, container);


        picfromcamera = view
                .findViewById(R.id.picfromcamera);
        picfromgallery = view
                .findViewById(R.id.picfromgallery);

        picfromcamera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {
                    dialog.dismiss();
                    Activity currentActivity = getActivity();
                    if (currentActivity == null) {
                        return;
                    }
                    if (currentActivity instanceof DirectoryListingProfileImageActivity) {
                        DirectoryListingProfileImageActivity activity = (DirectoryListingProfileImageActivity) getActivity();
                        activity.captureImage();
                    } else if (currentActivity instanceof JoinVartaProfileImageActivity) {
                        JoinVartaProfileImageActivity activity = (JoinVartaProfileImageActivity) getActivity();
                        activity.captureImage();
                    }
                } catch (Exception e) {
                    //
                }

            }
        });
        picfromgallery.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                try {
                    dialog.dismiss();
                    Activity currentActivity = getActivity();
                    if (currentActivity == null) {
                        return;
                    }
                    if (currentActivity instanceof DirectoryListingProfileImageActivity) {
                        DirectoryListingProfileImageActivity activity = (DirectoryListingProfileImageActivity) getActivity();
                        activity.galleryImage();
                    } else if (currentActivity instanceof JoinVartaProfileImageActivity) {
                        JoinVartaProfileImageActivity activity = (JoinVartaProfileImageActivity) getActivity();
                        activity.galleryImage();
                    }
                } catch (Exception e) {
                    //
                }
            }
        });
        return view;
    }


}
