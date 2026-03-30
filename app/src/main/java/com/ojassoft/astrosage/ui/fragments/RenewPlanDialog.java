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
import android.widget.ImageView;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

/**
 * Created by ojas on 4/5/16.
 */
public class RenewPlanDialog extends AstroSageParentDialogFragment {

    Activity activity;
    Typeface regularTypeface;
    int LANGUAGE_CODE;
    String expiryDate = "";
    static final String ExpiryDateKey = "ExpiryDateKey";


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        this.activity = activity;
        regularTypeface = ((BaseInputActivity) activity).regularTypeface;
        LANGUAGE_CODE = ((BaseInputActivity) activity).LANGUAGE_CODE;

    }

    @Override
    public void onDetach() {
        super.onDetach();
        activity = null;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        expiryDate = getArguments().getString(ExpiryDateKey);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // return super.onCreateView(inflater, container, savedInstanceState);
        final Dialog dialog = getDialog();
        dialog.setCancelable(true);
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        View view = inflater.inflate(R.layout.update_plan_alert_dialog, container);
        TextView tvUpdatePlan = (TextView) view
                .findViewById(R.id.tvUpdatePlan);

        int purchasePlanId = CUtils.getUserPurchasedPlanFromPreference(activity);
        if (purchasePlanId == 3 || purchasePlanId == 6 || purchasePlanId == 7) {
            tvUpdatePlan.setText(getResources().getString(R.string.update_user_gold_plan_text));
        }else if (purchasePlanId == 8 || purchasePlanId == 9 || purchasePlanId == 10) {
            tvUpdatePlan.setText(getResources().getString(R.string.update_user_platinum_plan_text));
        } else {
            tvUpdatePlan.setText(getResources().getString(R.string.update_user_silver_plan_text));
        }

        final CheckBox checkBoxDoNotShowAgain = (CheckBox) view
                .findViewById(R.id.checkBoxDoNotShowAgain);
        Button btnUpdatePlan = (Button) view
                .findViewById(R.id.btnUpdatePlan);
        // TextView tvHelp =f
        // (TextView)alertDialogBuilder.findViewById(R.id.tvHelp);
        ImageView imgCancel = (ImageView) view
                .findViewById(R.id.imgCancel);

        //tvUpdatePlan.setTypeface(regularTypeface);//added by neeraj 19/4/16
        String updatePlan = tvUpdatePlan.getText().toString();
        updatePlan = updatePlan.replace("$", expiryDate);

        /* // silver plan
        if (purchasePlanId == 2) {
            updatePlan = updatePlan.replaceAll("!",
                    getResources().getString(R.string.silver_plan_thanks_page));
        } else {
            updatePlan = updatePlan.replaceAll("!",
                    getResources().getString(R.string.golden_plan_thanks_page));
        }*/

        tvUpdatePlan.setText(updatePlan);

        checkBoxDoNotShowAgain.setTypeface(regularTypeface);
        // btnUpdatePlan.setTypeface(typeface);
        // tvHelp.setTypeface(typeface);

        btnUpdatePlan.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (checkBoxDoNotShowAgain.isChecked()) {
                    CUtils.saveBooleanData(
                            activity,
                            CGlobalVariables.doNotShowMessageAgainForUpdatePlan,
                            true);
                }

                CUtils.gotoProductPlanListUpdated(activity,
                        LANGUAGE_CODE,
                        ((BaseInputActivity) activity).SUB_ACTIVITY_UPGRADE_PLAN_UPDTAE, "","renew_plan_dialog");

                dialog.dismiss();
            }
        });

        imgCancel.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (checkBoxDoNotShowAgain.isChecked()) {
                    CUtils.saveBooleanData(
                            activity,
                            CGlobalVariables.doNotShowMessageAgainForUpdatePlan,
                            true);
                }

                dialog.dismiss();
            }
        });

        return view;

    }

    public static RenewPlanDialog getInstance(String expiryDate) {
        Bundle bundle = new Bundle();
        bundle.putString(ExpiryDateKey, expiryDate);
        RenewPlanDialog renewPlanDialog = new RenewPlanDialog();
        renewPlanDialog.setArguments(bundle);
        return renewPlanDialog;
    }
}
