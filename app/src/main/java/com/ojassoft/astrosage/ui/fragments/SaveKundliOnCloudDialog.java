package com.ojassoft.astrosage.ui.fragments;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.BASE_INPUT_SCREEN;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.IS_FROM_SCREEN;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.jinterface.IDialogFragmentClick;
import com.ojassoft.astrosage.ui.act.ActLogin;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.act.ThanksProductPurchaseScreen;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.varta.ui.activity.LoginSignUpActivity;

/**
 * Created by ojas-08 on 7/6/16.
 */
public class SaveKundliOnCloudDialog extends AstroSageParentDialogFragment {
    Activity activity;
    Typeface regularTypeface;
    Typeface mediumTypeface;
    IDialogFragmentClick callback;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
        regularTypeface = ((BaseInputActivity) activity).regularTypeface;
        mediumTypeface = ((BaseInputActivity) activity).mediumTypeface;

        try {
            callback = (IDialogFragmentClick) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling fragment must implement DialogClickListener interface");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity=null;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final Dialog dialog = getDialog();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.custom_dialog_for_check_personal_kundali, container);
        TextView titleAstrosage_Kundali = (TextView) view.findViewById(R.id.titleAstrosage_Kundali);
        TextView textViewHeading = (TextView) view.findViewById(R.id.text_view);
        Button yesButton = (Button) view.findViewById(R.id.yesbtn);
        Button noButton = (Button) view.findViewById(R.id.nobtn);

        textViewHeading.setText(R.string.save_kundli_on_cloud);
        titleAstrosage_Kundali.setText(R.string.upload_kundli_on_server);

        if (((AstrosageKundliApplication) getActivity().getApplication()).getLanguageCode() == CGlobalVariables.ENGLISH) {
            yesButton.setText(getResources().getString(R.string.yes).toUpperCase());
            noButton.setText(getResources().getString(R.string.no).toUpperCase());
        }
        titleAstrosage_Kundali.setTypeface(((BaseInputActivity) activity).mediumTypeface);
        textViewHeading.setTypeface(((BaseInputActivity) activity).regularTypeface);
        yesButton.setTypeface(((BaseInputActivity) activity).mediumTypeface);
        noButton.setTypeface(((BaseInputActivity) activity).mediumTypeface);

        yesButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                goToLogin();
                CUtils.googleAnalyticSendWitPlayServie(
                        getActivity(),
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.signin_screen_from_output_activity_yes,
                        null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.signin_screen_from_output_activity_yes,CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK,"");

                dialog.dismiss();
            }
        });
        noButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                dialog.dismiss();
                CUtils.googleAnalyticSendWitPlayServie(
                        getActivity(),
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.signin_screen_from_output_activity_no,
                        null);
                CUtils.fcmAnalyticsEvents(CGlobalVariables.signin_screen_from_output_activity_no,CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK,"");

                //callback.onNoClick();
            }
        });
        return view;

    }

    public static SaveKundliOnCloudDialog getInstance() {
        SaveKundliOnCloudDialog saveKundliOnCloudDialog = new SaveKundliOnCloudDialog();
        return saveKundliOnCloudDialog;
    }

    private void goToLogin() {
        //Intent intent = new Intent(getActivity(), ActLogin.class);
        Intent intent = new Intent(getActivity(), LoginSignUpActivity.class);
        intent.putExtra(IS_FROM_SCREEN, BASE_INPUT_SCREEN);
        getActivity().startActivityForResult(intent, BaseInputActivity.SUB_ACTIVITY_USER_LOGIN_UPLOAD_KUNDLI);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(getActivity(), "onActivity result" + requestCode, Toast.LENGTH_SHORT).show();
    }
}
