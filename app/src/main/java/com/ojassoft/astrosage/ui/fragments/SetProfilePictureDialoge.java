package com.ojassoft.astrosage.ui.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.jinterface.ISearchBirthDeatils;
import com.ojassoft.astrosage.ui.act.EditProfileActivity;

/**
 * Created by ojas on 4/5/16.
 */
public class SetProfilePictureDialoge extends AstroSageParentDialogFragment {


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
        LinearLayout picfromcamera,picfromgallery;

        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.set_profile_picture_dialoge, container);


        picfromcamera = (LinearLayout) view
                .findViewById(R.id.picfromcamera);
        picfromgallery = (LinearLayout) view
                .findViewById(R.id.picfromgallery);




        //picfromcamera.setTypeface(mediumTypeface);
       // picfromgallery.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);

        picfromcamera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                try {
                    // TODO Auto-generated method stub
                    dialog.dismiss();

                    EditProfileActivity activity = (EditProfileActivity) getActivity();
                    activity.checkPermissions();
                }catch (Exception e){
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
                    EditProfileActivity activity = (EditProfileActivity) getActivity();
                    activity.galleryImage();
                }catch (Exception e){
                    //
                }


            }
        });
        return view;
    }


}
