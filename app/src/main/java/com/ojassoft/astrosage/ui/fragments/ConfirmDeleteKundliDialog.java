package com.ojassoft.astrosage.ui.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.jinterface.ISearchBirthDeatils;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.act.HomeInputScreen;
import com.ojassoft.astrosage.ui.act.MychartActivity;
import com.ojassoft.astrosage.ui.act.matching.HomeMatchMakingInputScreen;
import com.ojassoft.astrosage.utils.CGlobalVariables;

/**
 * Created by ojas on 4/5/16.
 */
public class ConfirmDeleteKundliDialog extends AstroSageParentDialogFragment {


    ISearchBirthDeatils callback;
    BeanHoroPersonalInfo beanHoroPersonalInfo;
    boolean isCheckForDeletePersonalKundli, isLocal;
    boolean isRecentKundli;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        beanHoroPersonalInfo = (BeanHoroPersonalInfo) getArguments().getSerializable("beanHoroPersonalInfo");
        isCheckForDeletePersonalKundli = getArguments().getBoolean("isCheckForDeletePersonalKundli");
        isLocal = getArguments().getBoolean("isLocal");
        isRecentKundli = getArguments().getBoolean("isRecentKundli");

        try {
            callback = (ISearchBirthDeatils) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement DialogClickListener interface");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final Dialog dialog = getDialog();
        TextView textViewHeading;
        Button imageyes, imageno;
        if(dialog != null)
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.customdialogforsearch, container);
        TextView heading = (TextView) view
                .findViewById(R.id.textViewHeading1);
        heading.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);
        textViewHeading = (TextView) view
                .findViewById(R.id.textViewHeading);
        imageyes = (Button) view
                .findViewById(R.id.butChooseAyanOks);
        imageno = (Button) view
                .findViewById(R.id.butChooseAyanCancels);

        textViewHeading.setText(getResources().getString(
                R.string.Would_you_like_to_delete_kundli));
        if (((AstrosageKundliApplication) getActivity().getApplication()).getLanguageCode() == CGlobalVariables.ENGLISH) {
            imageyes.setText(getResources().getString(R.string.yes).toUpperCase());
            imageno.setText(getResources().getString(R.string.no).toUpperCase());
        }
        textViewHeading.setTypeface(((BaseInputActivity) getActivity()).regularTypeface);
        imageno.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);
        imageyes.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);

        imageyes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (dialog != null)
                    dialog.dismiss();

                /*if (isCheckForDeletePersonalKundli) {
                    deletePersonalKundli(kundlId);
                }
                deleteKundli(kundlId);*/

                //adjustDefaultKundliAfterDeletion(_nameList, position);
               /* if(isRecentKundli){
                    if (getActivity() instanceof HomeInputScreen) {
                        ((HomeInputScreen) getActivity()).searchBirthDetailsFragment.deleteRecentKundli(beanHoroPersonalInfo);
                    } else if (getActivity() instanceof HomeMatchMakingInputScreen) {
                        ((HomeMatchMakingInputScreen) getActivity()).searchBirthDetailsFragment.deleteRecentKundli(beanHoroPersonalInfo);
                    } else {
                        ((MychartActivity) getActivity()).searchBirthDetailsFragment.deleteRecentKundli(beanHoroPersonalInfo);
                    }
                }else{
                    callback.confirmDeleteKundli(beanHoroPersonalInfo, isCheckForDeletePersonalKundli, isLocal);
                }*/

               /* if (getActivity() instanceof HomeInputScreen) {
                    ((HomeInputScreen) getActivity()).searchBirthDetailsFragment.deleteRecentKundli(beanHoroPersonalInfo);
                } else if (getActivity() instanceof HomeMatchMakingInputScreen) {
                    ((HomeMatchMakingInputScreen) getActivity()).searchBirthDetailsFragment.deleteRecentKundli(beanHoroPersonalInfo);
                } else {
                    ((MychartActivity) getActivity()).searchBirthDetailsFragment.deleteRecentKundli(beanHoroPersonalInfo);
                }*/
                if (callback != null)
                    callback.confirmDeleteKundli(beanHoroPersonalInfo, isCheckForDeletePersonalKundli, isLocal);


            }
        });
        imageno.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                if (dialog != null)
                    dialog.dismiss();
                try {
                    if (getActivity() instanceof HomeInputScreen) {
                        ((HomeInputScreen) getActivity()).searchBirthDetailsFragment.editTextSearchKundli.showDropDown();
                    } else if (getActivity() instanceof HomeMatchMakingInputScreen) {
                        ((HomeMatchMakingInputScreen) getActivity()).searchBirthDetailsFragment.editTextSearchKundli.showDropDown();
                    } else {
                        ((MychartActivity) getActivity()).searchBirthDetailsFragment.editTextSearchKundli.showDropDown();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        });
        return view;
    }

    public static ConfirmDeleteKundliDialog getInstance(BeanHoroPersonalInfo beanHoroPersonalInfo, boolean isCheckForDeletePersonalKundli, boolean isLocal, boolean isRecentKundli) {

        Bundle bundle = new Bundle();
        bundle.putSerializable("beanHoroPersonalInfo", beanHoroPersonalInfo);
        bundle.putBoolean("isCheckForDeletePersonalKundli", isCheckForDeletePersonalKundli);
        bundle.putBoolean("isLocal", isLocal);
        bundle.putBoolean("isRecentKundli", isRecentKundli);

        ConfirmDeleteKundliDialog confirmDeleteKundliDialog = new ConfirmDeleteKundliDialog();
        confirmDeleteKundliDialog.setArguments(bundle);
        return confirmDeleteKundliDialog;
    }
}
