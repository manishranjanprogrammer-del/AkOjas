package com.ojassoft.astrosage.ui.act;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.ui.act.matching.HomeMatchMakingInputScreen;
import com.ojassoft.astrosage.ui.act.matching.OutputMatchingMasterActivity;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

public class ActUpdatePlanAfterTenCharts extends DialogFragment implements
        OnClickListener {

    //Typeface typeFace;
    private Button btnBuyNow, btnProceed;
    private CheckBox btnProceedDonotShowAgain, btnProceedDonotShowAgainTamil;
    TextView tvthankstext;
    int LANGUAGE_CODE = 0;
    LinearLayout layout;
    int resultId = 2;//2=Not more than 10 charts and 6 Not more than 500 charts
    String screenId = CGlobalVariables.SILVER_PLAN_VALUE_YEAR;
    Dialog dialog;
    String msg;
    RelativeLayout rlCrossBtn;

    public static ActUpdatePlanAfterTenCharts newInstance(int resultId) {
        Bundle args = new Bundle();
        args.putInt("resultId", resultId);
        args.putString("msg", "");
        //args.putString("screenId", screenId);
        ActUpdatePlanAfterTenCharts fragment = new ActUpdatePlanAfterTenCharts();
        fragment.setArguments(args);
        return fragment;
    }

    public static ActUpdatePlanAfterTenCharts newInstance(int resultId, String msg) {
        Bundle args = new Bundle();
        args.putInt("resultId", resultId);
        args.putString("msg", msg);
        //args.putString("screenId", screenId);
        ActUpdatePlanAfterTenCharts fragment = new ActUpdatePlanAfterTenCharts();
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
        View view = inflater.inflate(R.layout.upgrade_plan_screen, container);
        btnBuyNow = (Button) view.findViewById(R.id.btnBuyNow);
        btnBuyNow.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);
        btnProceed = (Button) view.findViewById(R.id.btnProceed);

        btnProceed.setTypeface(((BaseInputActivity) getActivity()).mediumTypeface);
        btnProceedDonotShowAgain = (CheckBox) view.findViewById(R.id.checkDobotShowAgain);
        btnProceedDonotShowAgain.setTypeface(((BaseInputActivity) getActivity()).regularTypeface);
        btnProceedDonotShowAgainTamil = (CheckBox) view.findViewById(R.id.checkDobotShowAgainTamil);
        btnProceedDonotShowAgainTamil.setTypeface(((BaseInputActivity) getActivity()).regularTypeface);
        TextView headingText = (TextView) view.findViewById(R.id.tv_upgrade_heading);
        rlCrossBtn = view.findViewById(R.id.cross_btn_layout);
        headingText.setTypeface(((BaseInputActivity) getActivity()).regularTypeface);
        tvthankstext = (TextView) view.findViewById(R.id.tvthankstext);

        if (resultId == 6) {
            tvthankstext.setText(getResources().getText(R.string.upgrade_plan_text_for_500));
            screenId = CGlobalVariables.GOLD_PLAN_VALUE_YEAR;
        } else if (resultId == -132) {
            tvthankstext.setText(getResources().getText(R.string.share_chart_error));
            btnProceed.setText(getResources().getString(R.string.cancel));
        } else if (resultId == -133) {
            tvthankstext.setText(getResources().getText(R.string.share_chart_error));
            btnProceed.setText(getResources().getString(R.string.cancel));
        } else {

        }


        if (!msg.equals("")) {
            tvthankstext.setText(getResources().getText(R.string.save_notes_error));
            btnProceed.setText(getResources().getString(R.string.cancel));
        }
        tvthankstext.setTypeface(((BaseInputActivity) getActivity()).regularTypeface);
        if (LANGUAGE_CODE == 2) {

            btnProceedDonotShowAgain.setVisibility(View.GONE);
            btnProceedDonotShowAgainTamil.setVisibility(View.VISIBLE);

            tvthankstext.setTextSize(15);
            btnBuyNow.setTextSize(14);
            btnProceed.setTextSize(14);
            btnBuyNow.setPadding(5, 7, 5, 7);
            btnProceed.setPadding(5, 7, 5, 7);

        }
        rlCrossBtn.setOnClickListener(ActUpdatePlanAfterTenCharts.this);
        btnBuyNow.setOnClickListener(ActUpdatePlanAfterTenCharts.this);

        btnProceed.setOnClickListener(ActUpdatePlanAfterTenCharts.this);
        return view;
    }

    @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {
            case R.id.btnBuyNow:
            /*
             * startActivity(new Intent(ActUpdatePlanAfterTenCharts.this,
			 * DialogPlanUpgradeNew.class).putExtra("language_code",
			 * language_code));
			 */
            /*
             * startActivity(new Intent(ActUpdatePlanAfterTenCharts.this,
			 * DialogPlanUpgradeNew.class));
			 * ActUpdatePlanAfterTenCharts.this.finish();
			 */

			/*
             * startActivity(new Intent(ActUpdatePlanAfterTenCharts.this,
			 * PurchasePlanHomeActivity.class));
			 * ActUpdatePlanAfterTenCharts.this.finish();
			 */
            /*
             * CUtils.gotoProductPlanListUpdated(this, LANGUAGE_CODE,
			 * HomeInputScreen.SUB_ACTIVITY_UPGRADE_PLAN_DIALOG_PURCHSE);
			 * ActUpdatePlanAfterTenCharts.this.finish();
			 */
                CUtils.fcmAnalyticsEvents(CGlobalVariables.UPDATE_PLAN_AFTER_TEN_CHARTS_BUYNOW, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK,"");
                setPurchaseResult(true);
                dialog.dismiss();
                break;
            case R.id.btnProceed:
                CUtils.fcmAnalyticsEvents(CGlobalVariables.UPDATE_PLAN_AFTER_TEN_CHARTS_PROCEED, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK,"");
                if (!(getActivity() instanceof OutputMasterActivity)) {
                    setPurchaseResult(false);
                }
                if(getActivity() instanceof HomeMatchMakingInputScreen){
                    ((HomeMatchMakingInputScreen)getActivity()).showProgressBar();
                }
                CUtils.setDoNotShowAccountUpgradePopupValueInPreference(
                        getActivity(), btnProceedDonotShowAgain.isChecked());
                // ActUpdatePlanAfterTenCharts.this.finish();
                dialog.dismiss();

                break;
            case R.id.checkDobotShowAgain:

                setPurchaseResult(false);
                // ActUpdatePlanAfterTenCharts.this.finish();
                dialog.dismiss();
                break;
            case R.id.cross_btn_layout:
                dialog.dismiss();
                break;

            default:
                break;
        }

    }

    private void setPurchaseResult(boolean purchaseSilverPlan) {
        CUtils.setDoNotShowAccountUpgradePopupValueInPreference(
                getActivity(), btnProceedDonotShowAgain.isChecked());
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
    public void onStart() {
        super.onStart();
        DisplayMetrics metrics = getActivity().getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        Dialog dialog = getDialog();
        if (dialog != null) {
            WindowManager.LayoutParams wmlp = dialog.getWindow()
                    .getAttributes();
            wmlp.width = (int) width - 40;
            wmlp.height = android.view.WindowManager.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setAttributes(wmlp);
            // dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }
    }

}
