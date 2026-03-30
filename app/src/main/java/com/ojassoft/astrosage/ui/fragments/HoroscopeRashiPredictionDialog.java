package com.ojassoft.astrosage.ui.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.act.horoscope.HoroscopeHomeActivity;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

/**
 * Created by ojas on 4/5/16.
 */
public class HoroscopeRashiPredictionDialog extends AstroSageParentDialogFragment {


    Activity activity;
    Typeface regularTypeface;
    Typeface mediumTypeface;
    int rashiIndex;
    CheckBox checkBoxButton;
    public static final String rashiIndexKey = "RashiIndexKey";

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
        regularTypeface = ((BaseInputActivity) activity).regularTypeface;
        mediumTypeface = ((BaseInputActivity) activity).mediumTypeface;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        rashiIndex = getArguments().getInt(rashiIndexKey);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final Dialog dialog = getDialog();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.customalertdialogxml, container);
        TextView titleTextView = (TextView) view.findViewById(R.id.title);
        titleTextView.setTypeface(mediumTypeface);
        TextView textViewHeading = (TextView) view
                .findViewById(R.id.textViewHeading);
        textViewHeading.setText(getResources().getString(
                R.string.do_you_want_daily_notification_for_rashi).replace(
                "%", ((HoroscopeHomeActivity) activity).rashiNames[rashiIndex]));
        textViewHeading.setTypeface(regularTypeface);

        Button imageyes = (Button) view
                .findViewById(R.id.butChooseAyanOk);

        Button imageno = (Button) view
                .findViewById(R.id.butChooseAyanCancel);
        if (((AstrosageKundliApplication) activity.getApplication()).getLanguageCode() == CGlobalVariables.ENGLISH) {
            imageyes.setText(getResources().getString(R.string.yes).toUpperCase());
            imageno.setText(getResources().getString(R.string.no).toUpperCase());
        }

        checkBoxButton = (CheckBox) view
                .findViewById(R.id.cbDoNotShowRasiDialog);
        checkBoxButton.setTypeface(regularTypeface);
        imageyes.setTypeface(mediumTypeface);
        imageno.setTypeface(mediumTypeface);
        imageyes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CUtils.saveHoroscopeNotifationWant(
                        getActivity(), true, rashiIndex);

                ((HoroscopeHomeActivity) activity).saveDoNotShownNotificationDialogChecked();

                ((HoroscopeHomeActivity) activity).goToReasiDetailedActivity(rashiIndex);

                dialog.dismiss();
            }
        });
        imageno.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                doNotShownNotification();

                ((HoroscopeHomeActivity) activity).goToReasiDetailedActivity(rashiIndex);

                dialog.dismiss();
            }
        });

        return view;

    }

    private void doNotShownNotification() {
        if (checkBoxButton.isChecked()) {
            ((HoroscopeHomeActivity) activity).saveDoNotShownNotificationDialogChecked();
        }
    }

    public static HoroscopeRashiPredictionDialog getInstance(int rashiIndex) {

        Bundle bundle = new Bundle();
        bundle.putInt(rashiIndexKey, rashiIndex);

        HoroscopeRashiPredictionDialog horoscopeRashiPredictionDialog = new HoroscopeRashiPredictionDialog();
        horoscopeRashiPredictionDialog.setArguments(bundle);
        return horoscopeRashiPredictionDialog;
    }
}
