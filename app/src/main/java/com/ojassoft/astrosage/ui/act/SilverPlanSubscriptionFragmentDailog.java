package com.ojassoft.astrosage.ui.act;

import android.app.Activity;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.Nullable;

import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;


/*
 * Plan subscription Dialog
 * Created by Monika
 * */
public class SilverPlanSubscriptionFragmentDailog extends Activity implements View.OnClickListener {

    Button buyNowText;
    RelativeLayout crossBtnLayout;
    Typeface typeFace;
    private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        setFinishOnTouchOutside(false);
        setContentView(R.layout.silver_plan_dialog_layout);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        inItView();
    }

    private void inItView()
    {
        buyNowText = (Button) findViewById(R.id.buy_now_text);
        crossBtnLayout=(RelativeLayout) findViewById(R.id.cross_btn_layout);
        buyNowText.setOnClickListener(this);
        crossBtnLayout.setOnClickListener(this);

        LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication()).getLanguageCode();

        typeFace = CUtils.getRobotoFont(
                getApplicationContext(), LANGUAGE_CODE, CGlobalVariables.medium);
        buyNowText.setTypeface(typeFace);

        String datee = CUtils.getDialogDate(CGlobalVariables.ONE_DAY);
        CUtils.saveSilverPlanDialogData(SilverPlanSubscriptionFragmentDailog.this,true,datee);

    }

    @Override
    public void onClick(View view) {
        String datee = "";
        switch (view.getId())
        {
            case R.id.buy_now_text:
                CUtils.fcmAnalyticsEvents(CGlobalVariables.SUBSCRIPTION_DIALOG_BUYNOW_BTN_CLICK,CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK,"");
                CUtils.gotoProductPlanListUpdated(SilverPlanSubscriptionFragmentDailog.this,
                        LANGUAGE_CODE, BaseInputActivity.SUB_ACTIVITY_UPGRADE_PLAN_UPDTAE, CGlobalVariables.PLATINUM_PLAN_VALUE_YEAR,"platinum_plan_subscription_fragment_dailog");
                finish();
                break;
            case R.id.cross_btn_layout:
                CUtils.fcmAnalyticsEvents(CGlobalVariables.SUBSCRIPTION_DIALOG_CROSS_BTN_CLICK,CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK,"");
                datee = CUtils.getDialogDate(CGlobalVariables.WEEK_DAYS);
                CUtils.saveSilverPlanDialogData(SilverPlanSubscriptionFragmentDailog.this,true,datee);
                finish();
                break;
        }
    }



    @Override
    public void onBackPressed() {
        //super.onBackPressed();
    }

    @Override
    protected void onStart() {
        super.onStart();

    }
}
