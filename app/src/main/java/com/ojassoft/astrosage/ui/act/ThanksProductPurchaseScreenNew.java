package com.ojassoft.astrosage.ui.act;

import static com.ojassoft.astrosage.utils.CGlobalVariables.FREE_QUESTIONS_RECEIVED_KEY;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.misc.ServiceToGetPurchasePlanDetails;
import com.ojassoft.astrosage.model.AppPurchaseDataSaveModelClass;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.varta.ui.activity.BaseActivity;
import com.ojassoft.astrosage.varta.utils.FontUtils;


/**
 * Activity that displays a thank-you screen after successful plan purchase (new design).
 * Handles post-purchase logic such as preference updates and verification.
 */
public class ThanksProductPurchaseScreenNew extends BaseActivity {
    int language_code;
    Typeface typeFace;
    String purchaseData = "", signature = "",
            astroSageUserId = "", price = "0", priceCurrencycode = "INR", plan = "", successFrom = "", freetrialperiod = "";
    LinearLayout llUserLogin;
    Button doneBtn;
    boolean sendDataToServerWithoutLogin = true;
    PlanVerification receiver;
    ProgressBar progressbar;
    String freeQuestionCount;
    TextView heading1, heading2;
    private TextView descriptionTV, tvContactDetails;

    /**
     * Initializes the thank-you screen, retrieves purchase data, and saves preferences.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        CUtils.fcmAnalyticsEvents(CGlobalVariables.PLAN_UPGRADE_FOR_ASTROSAGE_AI_PLUS_SUCCESS_DIALOG_SHOW, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        setFinishOnTouchOutside(false);
        setContentView(R.layout.thanks_purchase_layout);
        signature = getIntent().getExtras().getString("SIGNATURE");
        purchaseData = getIntent().getExtras().getString("PURCHASE_DATA");
        astroSageUserId = CUtils.getUserName(ThanksProductPurchaseScreenNew.this);
        price = getIntent().getExtras().getString("price");
        successFrom = getIntent().getExtras().getString("isSuccesFrom");
        priceCurrencycode = getIntent().getExtras().getString("priceCurrencycode");
        try {
            freetrialperiod = getIntent().getExtras().getString("freetrialperiod");
        } catch (Exception e) {
            e.printStackTrace();
        }
        freeQuestionCount = getIntent().getExtras().getString(FREE_QUESTIONS_RECEIVED_KEY);

        saveDataInPreferences();

        typeFace = CUtils.getRobotoFont(
                getApplicationContext(), ((AstrosageKundliApplication) getApplication())
                        .getLanguageCode(), CGlobalVariables.regular);
        language_code = ((AstrosageKundliApplication) getApplication())
                .getLanguageCode();
        heading1 = findViewById(R.id.heading1);
        heading2 = findViewById(R.id.heading2);

        FontUtils.changeFont(this, heading1, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(this, heading2, com.ojassoft.astrosage.varta.utils.CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);


        progressbar = findViewById(R.id.progressbar);
        plan = getIntent().getExtras().getString("PLAN");
        descriptionTV = findViewById(R.id.recharge_sucessful_content);
        doneBtn = findViewById(R.id.done_btn);


        //user is already logged in for this case
        callMethodIfUserLogin(true);
        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (successFrom.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.RAZORPAY)) {
                    if (CUtils.isUserLogedIn(ThanksProductPurchaseScreenNew.this)
                            && CUtils.isConnectedWithInternet(ThanksProductPurchaseScreenNew.this)) {
                        Intent intent = new Intent(ThanksProductPurchaseScreenNew.this, ServiceToGetPurchasePlanDetails.class);
                        startService(intent);
                    }
                }
                finish();
            }
        });


        this.setTitle("");

    }

    private void callMethodIfUserLogin(boolean isSendToServer) {
        CGlobalVariables.PLAN_PURCHASE_ID = getPlanID(plan);
        CGlobalVariables.isFromPlanPurchaseActivity = true;

        if (successFrom.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.RAZORPAY)) {
            //CGlobalVariables.PLAN_PURCHASE_ID = getPlanID(plan);
            CUtils.storeUserPurchasedPlanInPreference(this, CGlobalVariables.PLAN_PURCHASE_ID);
        }
        String descText = getString(R.string.plan_purchase_success_desc);

        descriptionTV.setText(descText);
        descriptionTV.setTypeface(typeFace);
        doneBtn.setText(getResources().getString(R.string.done));
        doneBtn.setTypeface(typeFace);
    }


    private int getPlanID(String purchasedPlan) {
        int planID = 1;

        if (purchasedPlan.equals(CGlobalVariables.GOLD_PLAN_VALUE_YEAR)) {
            planID = CGlobalVariables.GOLD_PLAN_ID;
        } else if (purchasedPlan.equals(CGlobalVariables.GOLD_PLAN_VALUE_MONTH)) {
            planID = CGlobalVariables.GOLD_PLAN_ID_6;
        } else if (purchasedPlan.equals(CGlobalVariables.SILVER_PLAN_VALUE_YEAR)) {
            planID = CGlobalVariables.SILVER_PLAN_ID;
        } else if (purchasedPlan.equals(CGlobalVariables.SILVER_PLAN_VALUE_MONTH)) {
            planID = CGlobalVariables.SILVER_PLAN_ID_4;
        } else if (purchasedPlan.equals(CGlobalVariables.PLATINUM_PLAN_VALUE_YEAR)) {
            planID = CGlobalVariables.PLATINUM_PLAN_ID;
        } else if (purchasedPlan.equals(CGlobalVariables.PLATINUM_PLAN_VALUE_MONTH)) {
            planID = CGlobalVariables.PLATINUM_PLAN_ID_10;
        } else if (purchasedPlan.equals(CGlobalVariables.KUNDLI_AI_PLAN_VALUE_MONTH)) {
            planID = CGlobalVariables.KUNDLI_AI_PRIMIUM_PLAN_ID_11;
        }

        return planID;
    }


    /**
     * Saves the current purchase data in shared preferences for later verification.
     */
    private void saveDataInPreferences() {

        SharedPreferences purchasageDataPlan = getSharedPreferences(CGlobalVariables.ASTROSAGEPURCHASEPLAN, Context.MODE_PRIVATE);
        SharedPreferences.Editor purchasePlanEditor = purchasageDataPlan.edit();
        AppPurchaseDataSaveModelClass appPurchaseDataSaveModelClass = CUtils.getObjectSavePref(signature, purchaseData, "", astroSageUserId, price, priceCurrencycode);
        Gson gson = new Gson();
        String purchaseDataJSONObject = gson.toJson(appPurchaseDataSaveModelClass); // myObject - instance of MyObject
        purchasePlanEditor.putString(CGlobalVariables.ASTROSAGEPURCHASEPLANOBJECT, purchaseDataJSONObject);
        purchasePlanEditor.putBoolean(CGlobalVariables.ACTIVITYUPDATEAFTERPLANPURCHASE, true);
        purchasePlanEditor.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case BaseInputActivity.SUB_ACTIVITY_USER_LOGIN: {

                sendDataToServerWithoutLogin = false;

                if (resultCode == RESULT_OK) {
                    Bundle b = data.getExtras();
                    astroSageUserId = b.getString("LOGIN_NAME");
                    //String loginPwd = b.getString("LOGIN_PWD");
                    setPreferenceToVariables(astroSageUserId);
                    saveDataInPreferences();

                    if (CUtils.isUserLogedIn(ThanksProductPurchaseScreenNew.this)) {
                        callMethodIfUserLogin(false);
                    }
                } else {
                    this.finish();
                }

            }
            default:
                break;
        }

    }

    @Override
    public void onStop() {
        super.onStop();

        try {
            if (receiver != null) {
                unregisterReceiver(receiver);           //<-- Unregister to avoid memoryleak
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setPreferenceToVariables(String newUserId) {

        SharedPreferences purchasageDataPlanSharedPreferences = getSharedPreferences(CGlobalVariables.ASTROSAGEPURCHASEPLAN, Context.MODE_PRIVATE);
        Gson gson = new Gson();
        String json = purchasageDataPlanSharedPreferences.getString(CGlobalVariables.ASTROSAGEPURCHASEPLANOBJECT, "");
        AppPurchaseDataSaveModelClass appPurchaseDataSaveModelClassArrayList = gson.fromJson(json, AppPurchaseDataSaveModelClass.class);

        signature = appPurchaseDataSaveModelClassArrayList.getSignature();
        purchaseData = appPurchaseDataSaveModelClassArrayList.getPurchaseData();
        if (newUserId.equals("")) {
            astroSageUserId = appPurchaseDataSaveModelClassArrayList.getPurchaseUserID();
        } else {
            astroSageUserId = newUserId;
        }
        price = appPurchaseDataSaveModelClassArrayList.getPrice();
        priceCurrencycode = appPurchaseDataSaveModelClassArrayList.getPriceCurrencycode();

    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            receiver = new PlanVerification();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                registerReceiver(receiver, new IntentFilter(CGlobalVariables.BROADCAST_PLAN_PURCHASE), Context.RECEIVER_NOT_EXPORTED);  //<----Register
            } else {
                registerReceiver(receiver, new IntentFilter(CGlobalVariables.BROADCAST_PLAN_PURCHASE));
            }
        } catch (Exception e) {
            //
        }
    }

    /*
     * Broadcast receiver get verification information after verify server
     * */
    class PlanVerification extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {

            if (intent.getAction().equals(CGlobalVariables.BROADCAST_PLAN_PURCHASE)) {
                String verifyTxt = intent.getStringExtra(CGlobalVariables.KEY_PLAN_DATA);
                if (verifyTxt.equalsIgnoreCase(CGlobalVariables.PLAN_VERIFY)) {
                    SharedPreferences purchasageDataPlan = context.getSharedPreferences(CGlobalVariables.ASTROSAGEPURCHASEPLAN, Context.MODE_PRIVATE);
                    SharedPreferences.Editor purchasePlanEditor = purchasageDataPlan.edit();
                    purchasePlanEditor.putBoolean(CGlobalVariables.ACTIVITYUPDATEAFTERPLANPURCHASE, true);
                    purchasePlanEditor.commit();

                    CUtils.storeUserPurchasedPlanInPreference(ThanksProductPurchaseScreenNew.this, CGlobalVariables.PLAN_PURCHASE_ID);
                }
                progressbar.setVisibility(View.GONE);
                doneBtn.setVisibility(View.VISIBLE);

                // Toast.makeText(ThanksProductPurchaseScreen.this,"MSG "+verifyTxt,Toast.LENGTH_LONG).show();
                Log.e("CNNNNN ", verifyTxt);
            } else {
                progressbar.setVisibility(View.GONE);
            }
        }

    }
}
