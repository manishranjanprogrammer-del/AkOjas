package com.ojassoft.astrosage.ui.fragments.astrochatfragment.fragment;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.controller.ControllerManager;
import com.ojassoft.astrosage.customexceptions.UIDataOperationException;
import com.ojassoft.astrosage.model.AstrologerServiceInfo;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.act.HomeInputScreen;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.util.Map;

/**
 * Created by Chadnan on 23/9/16.
 */

public class ChangeBirthProfileDialog extends DialogFragment {

    TextView personName, birthDate, birthTime, birthPlace, birthDateLabel, birthTimeLabel, birthPlacelabel;
    ImageView genderImage;
    Button changeButton, btnOk, btnGetFirstProfile;
    RelativeLayout birthProfileAvailable, birthProfileNotFound;
    TextView kundliNotFoundTitle, kundliNotFoundDescription, kundliDetailTitle;
    int requestKey;

    private AstrologerServiceInfo details;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.change_birth_profile_dialog, container);
        getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        requestKey = getArguments().getInt("requestCode");
        initAllViews(view);
        loadCurrentBirthDetail();
        setTypeface();
        return view;
    }

    private void setTypeface() {
        int languageCode= CUtils.getLanguageCodeFromPreference(getActivity());
        if (languageCode== CGlobalVariables.HINDI) {
            birthDateLabel.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);
            birthTimeLabel.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);
            birthPlacelabel.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);
            btnOk.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);
            btnGetFirstProfile.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);
            //kundliNotFoundDescription.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);
            kundliNotFoundTitle.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);
            kundliDetailTitle.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface,Typeface.BOLD);
        }
    }

    private void initAllViews(View view) {
        personName = (TextView) view.findViewById(R.id.personName);
        birthDate = (TextView) view.findViewById(R.id.birthDate);
        birthTime = (TextView) view.findViewById(R.id.birthTime);
        birthPlace = (TextView) view.findViewById(R.id.birthPlace);
        genderImage = (ImageView) view.findViewById(R.id.genderImage);
        changeButton = (Button) view.findViewById(R.id.changeButton);
        btnOk = (Button) view.findViewById(R.id.btn_ok);
        birthDateLabel = (TextView) view.findViewById(R.id.birthDate_label);
        birthTimeLabel = (TextView) view.findViewById(R.id.birthTime_label);
        birthPlacelabel = (TextView) view.findViewById(R.id.birthPlace_label);
        birthProfileAvailable = (RelativeLayout) view.findViewById(R.id.birth_profile_found);
        birthProfileNotFound = (RelativeLayout) view.findViewById(R.id.birth_profile_not_found);
        btnGetFirstProfile = (Button) view.findViewById(R.id.butSet);
        kundliNotFoundDescription = (TextView) view.findViewById(R.id.txvSelectProfileDescriptionMessage);
        kundliNotFoundTitle = (TextView) view.findViewById(R.id.txvSelectProfile);
        kundliDetailTitle = (TextView) view.findViewById(R.id.titleView);

        changeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeBirthProfileDialog.this.dismiss();
                goToGetNewBirthProfile();
            }
        });

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeBirthProfileDialog.this.dismiss();
            }
        });

        btnGetFirstProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ChangeBirthProfileDialog.this.dismiss();
                goToGetNewBirthProfile();
            }
        });
    }

    private void goToGetNewBirthProfile() {
        callToGetNewBirthDetails(isLocalKundliAvailable());
    }

    private void loadCurrentBirthDetail() {
        String userProfileChat = CUtils.getStringData(getActivity(), CGlobalVariables.USERPROFILEASTROCHAT, null);
        if (userProfileChat!=null) {
            birthProfileNotFound.setVisibility(View.GONE);
            birthProfileAvailable.setVisibility(View.VISIBLE);
            AstrologerServiceInfo serviceInfo = new Gson().fromJson(userProfileChat, new TypeToken<AstrologerServiceInfo>() {
            }.getType());
            setDetails(serviceInfo);
        } else {
            birthProfileNotFound.setVisibility(View.VISIBLE);
            birthProfileAvailable.setVisibility(View.GONE);
        }
    }

    public void setDetails(AstrologerServiceInfo birthInfo) {
        if(birthInfo!=null) {
            personName.setText(birthInfo.getRegName());
            birthDate.setText(": " + birthInfo.getDateOfBirth() + "-" + birthInfo.getMonthOfBirth() + "-" + birthInfo.getYearOfBirth());
            birthTime.setText(": " + birthInfo.getHourOfBirth() + ":" + birthInfo.getMinOfBirth() + ":" + birthInfo.getSecOfBirth());

            String place = birthInfo.getPlace();
            String state = birthInfo.getState();
            String country = birthInfo.getCountry();

            String address = ": " +place;
            if(state != null && !state.equals("")){
                address = address + ", " + birthInfo.getState();
            }
            if(country != null && !state.equals("")){
                address = address + ", " + birthInfo.getCountry();
            }

            // birthPlace.setText(": " + birthInfo.getPlace() + ", " + birthInfo.getState() + ", " + birthInfo.getCountry());
            birthPlace.setText(address);
            if (birthInfo.getGender().equals("M") || birthInfo.getGender().equals("Male")) {
                genderImage.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_male));
            } else {
                genderImage.setImageDrawable(getActivity().getResources().getDrawable(R.drawable.ic_female));
            }
        }
    }

    private int isLocalKundliAvailable() {
        int screenId = 1;
        try {
            Map<String, String> mapHoroID = new ControllerManager().searchLocalKundliListOperation(
                    getActivity().getApplicationContext(), "", CGlobalVariables.BOTH_GENDER, -1);
            if (mapHoroID != null) {
                screenId = 0;
            } else {
                screenId = 1;
            }

        } catch (UIDataOperationException e) {
            e.printStackTrace();
        }
        return screenId;
    }

    private void callToGetNewBirthDetails(int screenId) {
        Bundle bundle = new Bundle();
        Intent intent = new Intent(getActivity(), HomeInputScreen.class);
        intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, CGlobalVariables.MODULE_BASIC);
        intent.putExtra(CGlobalVariables.ASTROSAGE_CHAT_QUERY_DATA, true);
        intent.putExtra("PAGER_INDEX", screenId);
        intent.putExtras(bundle);
        getActivity().startActivityForResult(intent, requestKey);
    }
}
