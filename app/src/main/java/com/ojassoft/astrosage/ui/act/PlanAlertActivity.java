package com.ojassoft.astrosage.ui.act;

import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

/**
 * Created by ojas on ३/४/१८.
 */

public class PlanAlertActivity extends DialogFragment implements
        View.OnClickListener {

    int resultId = 2;//2=Not more than 10 charts and 6 Not more than 500 charts
    Dialog dialog;
    String msg;
    Button okButton;
    Button cancelButton;
    TextView errorText, headingTextView;
    int LANGUAGE_CODE;
    String screenId = CGlobalVariables.SILVER_PLAN_VALUE_YEAR;
    RelativeLayout rlCrossBtn;

    public static PlanAlertActivity newInstance(int resultId) {
        Bundle args = new Bundle();
        args.putInt("resultId", resultId);
        args.putString("msg", "");
        //args.putString("screenId", screenId);
        PlanAlertActivity fragment = new PlanAlertActivity();
        fragment.setArguments(args);
        return fragment;
    }

    /*public static PlanAlertActivity newInstance(int resultId, String msg) {
        Bundle args = new Bundle();
        args.putInt("resultId", resultId);
        args.putString("msg", msg);
        //args.putString("screenId", screenId);
        PlanAlertActivity fragment = new PlanAlertActivity();
        fragment.setArguments(args);
        return fragment;
    }*/

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
        View view = inflater.inflate(R.layout.plan_alert_activity_lay, container);
        okButton = (Button) view.findViewById(R.id.btnBuyNow);
        cancelButton = (Button) view.findViewById(R.id.btnProceed);
        errorText = (TextView) view.findViewById(R.id.tvthankstext);
        rlCrossBtn = view.findViewById(R.id.cross_btn_layout);
        headingTextView = (TextView) view.findViewById(R.id.tv_upgrade_heading);
        if (CUtils.getUserPurchasedPlanFromPreference(getContext()) == CGlobalVariables.BASIC_PLAN_ID) {
            errorText.setText(getResources().getString(R.string.sync_chart_error).replace("#", "10"));
        } else if (CUtils.getUserPurchasedPlanFromPreference(getContext()) == CGlobalVariables.SILVER_PLAN_ID) {
            errorText.setText(getResources().getString(R.string.sync_chart_error).replace("#", "500"));
        }
        if (LANGUAGE_CODE == 2) {
            okButton.setTextSize(14);
            cancelButton.setTextSize(14);
            okButton.setPadding(5, 7, 5, 7);
            cancelButton.setPadding(5, 7, 5, 7);
        }
        rlCrossBtn.setOnClickListener(PlanAlertActivity.this);
        okButton.setOnClickListener(PlanAlertActivity.this);
        cancelButton.setOnClickListener(PlanAlertActivity.this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBuyNow:
                setPurchaseResult();
                dialog.dismiss();
                break;

            case R.id.btnProceed:
                dialog.dismiss();
                break;

            case R.id.cross_btn_layout:
                dialog.dismiss();
                break;

            default:
                break;
        }

    }

    private void setPurchaseResult() {

        CUtils.gotoProductPlanListUpdated(
                getActivity(),
                LANGUAGE_CODE,
                HomeInputScreen.SUB_ACTIVITY_UPGRADE_PLAN_DIALOG_PURCHSE, screenId,"plan_alert_activity");

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
