package com.ojassoft.astrosage.ui.fragments;

import android.app.Dialog;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.AstrosageKundliApplication;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.ui.act.HomeInputScreen;
import com.ojassoft.astrosage.ui.act.OutputMasterActivity;
import com.ojassoft.astrosage.ui.act.matching.HomeMatchMakingInputScreen;
import com.ojassoft.astrosage.ui.act.matching.OutputMatchingMasterActivity;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

public class UpdatePlanAfterTenChartNewDialog extends DialogFragment implements
        View.OnClickListener {

    //Typeface typeFace;
    private TextView btnBuyNow, cancelBtn;
    TextView tvthankstext;
    int LANGUAGE_CODE = 0;
    LinearLayout layout;
    int resultId = 2;//2=Not more than 10 charts and 6 Not more than 500 charts
    String screenId = CGlobalVariables.SILVER_PLAN_VALUE_YEAR;
    Dialog dialog;
    String msg;
    ImageView rlCrossBtn;

    public static UpdatePlanAfterTenChartNewDialog newInstance(int resultId) {
        Bundle args = new Bundle();
        args.putInt("resultId", resultId);
        args.putString("msg", "");
        //args.putString("screenId", screenId);
        UpdatePlanAfterTenChartNewDialog fragment = new UpdatePlanAfterTenChartNewDialog();
        fragment.setArguments(args);
        return fragment;
    }

    public static UpdatePlanAfterTenChartNewDialog newInstance(int resultId, String msg) {
        Bundle args = new Bundle();
        args.putInt("resultId", resultId);
        args.putString("msg", msg);
        //args.putString("screenId", screenId);
        UpdatePlanAfterTenChartNewDialog fragment = new UpdatePlanAfterTenChartNewDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        resultId = getArguments().getInt("resultId", 2);
        msg = getArguments().getString("msg");
        LANGUAGE_CODE = ((AstrosageKundliApplication) getActivity().getApplication()).getLanguageCode();
        //screenId = getArguments().getString("screenId");
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.transparentdialog);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        dialog = getDialog();
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        setCancelable(true);
        View view = inflater.inflate(R.layout.upgrade_plan_after_ten_chart_layout, container);
        btnBuyNow = view.findViewById(R.id.buy_now_btn);
        btnBuyNow.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);
        cancelBtn = view.findViewById(R.id.cancel_btn);

        cancelBtn.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);
        rlCrossBtn = view.findViewById(R.id.ivClosePage);
        tvthankstext =  view.findViewById(R.id.headingTV);

        if (resultId == 6) {
            tvthankstext.setText(getResources().getText(R.string.upgrade_plan_text_for_500));
            screenId = CGlobalVariables.GOLD_PLAN_VALUE_YEAR;
        } else if (resultId == -132) {
            tvthankstext.setText(getResources().getText(R.string.share_chart_error));
            cancelBtn.setText(getResources().getString(R.string.cancel));
        } else if (resultId == -133) {
            tvthankstext.setText(getResources().getText(R.string.share_chart_error));
            cancelBtn.setText(getResources().getString(R.string.cancel));
        } else {

        }


        if (!msg.equals("")) {
            tvthankstext.setText(getResources().getText(R.string.save_notes_error));
            cancelBtn.setText(getResources().getString(R.string.cancel));
        }
        tvthankstext.setTypeface(((BaseInputActivity) getActivity()).regularTypeface);
        if (LANGUAGE_CODE == 2) {

            tvthankstext.setTextSize(15);
            btnBuyNow.setTextSize(14);
            cancelBtn.setTextSize(14);
            btnBuyNow.setPadding(5, 7, 5, 7);
            cancelBtn.setPadding(5, 7, 5, 7);

        }
        rlCrossBtn.setOnClickListener(UpdatePlanAfterTenChartNewDialog.this);
        btnBuyNow.setOnClickListener(UpdatePlanAfterTenChartNewDialog.this);

        cancelBtn.setOnClickListener(UpdatePlanAfterTenChartNewDialog.this);
        return view;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.buy_now_btn:
                /*
                 * startActivity(new Intent(UpdatePlanAfterTenChartNewDialog.this,
                 * DialogPlanUpgradeNew.class).putExtra("language_code",
                 * language_code));
                 */
                /*
                 * startActivity(new Intent(UpdatePlanAfterTenChartNewDialog.this,
                 * DialogPlanUpgradeNew.class));
                 * UpdatePlanAfterTenChartNewDialog.this.finish();
                 */

                /*
                 * startActivity(new Intent(UpdatePlanAfterTenChartNewDialog.this,
                 * PurchasePlanHomeActivity.class));
                 * UpdatePlanAfterTenChartNewDialog.this.finish();
                 */
                /*
                 * CUtils.gotoProductPlanListUpdated(this, LANGUAGE_CODE,
                 * HomeInputScreen.SUB_ACTIVITY_UPGRADE_PLAN_DIALOG_PURCHSE);
                 * UpdatePlanAfterTenChartNewDialog.this.finish();
                 */
                CUtils.fcmAnalyticsEvents(CGlobalVariables.UPDATE_PLAN_AFTER_TEN_CHARTS_BUYNOW, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                setPurchaseResult(true);
                dialog.dismiss();
                break;
            case R.id.cancel_btn:
                CUtils.fcmAnalyticsEvents(CGlobalVariables.UPDATE_PLAN_AFTER_TEN_CHARTS_PROCEED, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                if (!(getActivity() instanceof OutputMasterActivity)) {
                    setPurchaseResult(false);
                }
                if (getActivity() instanceof HomeMatchMakingInputScreen) {
                    ((HomeMatchMakingInputScreen) getActivity()).showProgressBar();
                }
                // UpdatePlanAfterTenChartNewDialog.this.finish();
                dialog.dismiss();

                break;
            case R.id.ivClosePage:
                dialog.dismiss();
                break;

            default:
                break;
        }

    }

    private void setPurchaseResult(boolean purchaseSilverPlan) {
        if (getActivity() instanceof HomeInputScreen) {
            ((HomeInputScreen) getActivity()).setDataAfterPurchasePlan(purchaseSilverPlan, screenId);
        } else if (getActivity() instanceof HomeMatchMakingInputScreen) {
            ((HomeMatchMakingInputScreen) getActivity()).setDataAfterPurchasePlan(purchaseSilverPlan, screenId);
        } else if (getActivity() instanceof OutputMatchingMasterActivity) {
            ((OutputMatchingMasterActivity) getActivity()).setDataAfterPurchasePlan(purchaseSilverPlan, screenId);
        } else {
            ((OutputMasterActivity) getActivity()).setDataAfterPurchasePlan(purchaseSilverPlan, screenId);
        }

    }



    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        try {
            getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        } catch (Exception e) {
            //
        }
        super.onViewCreated(view, savedInstanceState);
    }


}

