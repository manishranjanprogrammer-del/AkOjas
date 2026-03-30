package com.ojassoft.astrosage.ui.act;

import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.BASE_INPUT_SCREEN;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.IS_FROM_SCREEN;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Spannable;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;

import com.google.gson.Gson;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.misc.PurchaseVerificationService;
import com.ojassoft.astrosage.misc.ServiceToGetPurchasePlanDetails;
import com.ojassoft.astrosage.model.AppPurchaseDataSaveModelClass;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.varta.ui.activity.LoginSignUpActivity;


/**
 * Activity that displays a thank-you screen after successful plan purchase.
 * Handles post-purchase logic such as preference updates and verification.
 */
public class ThanksProductPurchaseScreen extends Activity {
    int language_code;
    Typeface typeFace;
    String purchaseData = "", signature = "",
            astroSageUserId = "", price = "0", successFrom = "", priceCurrencycode = "INR", plan = "", freetrialperiod = "";
    //    LinearLayout llUserLogin;
    Button doneBtn;
    boolean sendDataToServerWithoutLogin = true;
    PlanVerification receiver;
    //private Button txtSignupUser;
    private TextView thankYouTextView, tvContactDetails, msgTextView;
//    ProgressBar progressbar;

    /**
     * Initializes the thank-you screen, retrieves purchase data, and saves preferences.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        Log.d("PurchaseFlow", "ThanksProductPurchaseScreen onCreate called");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));
        setFinishOnTouchOutside(false);
        CUtils.fcmAnalyticsEvents(CGlobalVariables.PLAN_UPGRADE_FOR_ASTROSAGE_AI_PLUS_SUCCESS_DIALOG_SHOW_OLD, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");

        signature = getIntent().getExtras().getString("SIGNATURE");
        purchaseData = getIntent().getExtras().getString("PURCHASE_DATA");
        astroSageUserId = CUtils.getUserName(ThanksProductPurchaseScreen.this);
        price = getIntent().getExtras().getString("price");
        successFrom = getIntent().getExtras().getString("isSuccesFrom");
        priceCurrencycode = getIntent().getExtras().getString("priceCurrencycode");
        try {
            freetrialperiod = getIntent().getExtras().getString("freetrialperiod");
        } catch (Exception e) {
            e.printStackTrace();
        }
        saveDataInPreferences();

        typeFace = CUtils.getRobotoFont(
                getApplicationContext(), ((AstrosageKundliApplication) getApplication())
                        .getLanguageCode(), CGlobalVariables.regular);
        language_code = ((AstrosageKundliApplication) getApplication())
                .getLanguageCode();
        setContentView(R.layout.thanks_screen_of_upgrade_plan);

//        progressbar = (ProgressBar) findViewById(R.id.progressbar);
        plan = getIntent().getExtras().getString("PLAN");

        // etUserId = (EditText) findViewById(R.id.etUserId);
        //txtSignupUser = (Button) findViewById(R.id.txtSignupUser);
        /*plan_activation_text = (TextView) findViewById(R.id.plan_activation_text);
        plan_activation_text.setTypeface(typeFace);

        btnOk = (Button) findViewById(R.id.btnOk);
        tvthankstext = (TextView) findViewById(R.id.tvthankstext);
        //provide_user_name_for_plan = (TextView) findViewById(R.id.provide_user_name_for_plan);
        txtNewUser = (TextView) findViewById(R.id.txtNewUser);
        txtNewUser.setOnClickListener(ThanksProductPurchaseScreen.this);
        btnOk.setTypeface(typeFace);
        tvthankstext.setTypeface(typeFace);*/

        tvContactDetails = (TextView) findViewById(R.id.tvContactDetails);
        thankYouTextView = (TextView) findViewById(R.id.heading2);
        msgTextView = findViewById(R.id.recharge_sucessful_content);
        doneBtn = (Button) findViewById(R.id.done_btn);

        if (CUtils.isUserLogedIn(ThanksProductPurchaseScreen.this)) {
            receiver = new PlanVerification();
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                registerReceiver(receiver, new IntentFilter(CGlobalVariables.BROADCAST_PLAN_PURCHASE), Context.RECEIVER_NOT_EXPORTED);  //<----Register
            } else {
                registerReceiver(receiver, new IntentFilter(CGlobalVariables.BROADCAST_PLAN_PURCHASE));
            }
            callMethodIfUserLogin(true);
        } else {

            callMethodIfUserNotLogin();
        }

        doneBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (successFrom.equals(com.ojassoft.astrosage.varta.utils.CGlobalVariables.RAZORPAY)) {
                    if (CUtils.isUserLogedIn(ThanksProductPurchaseScreen.this)
                            && CUtils.isConnectedWithInternet(ThanksProductPurchaseScreen.this)) {
                        Intent intent = new Intent(ThanksProductPurchaseScreen.this, ServiceToGetPurchasePlanDetails.class);
                        startService(intent);
                    }
                }
                //goToSignIn();
                finish();
            }
        });

        this.setTitle("");

    }

    private void callMethodIfUserLogin(boolean isSendToServer) {
        CGlobalVariables.PLAN_PURCHASE_ID = getPlanID(plan);
        CGlobalVariables.isFromPlanPurchaseActivity = true;

        String thanksText = getResources().getString(R.string.thanks_for_purchasing_membership);
        if (plan.equals(CGlobalVariables.GOLD_PLAN_VALUE_YEAR) || plan.equals(CGlobalVariables.GOLD_PLAN_VALUE_MONTH)) {
            thanksText = thanksText.replace("$", getResources().getString(R.string.golden_plan));
        } else if (plan.equals(CGlobalVariables.SILVER_PLAN_VALUE_YEAR) || plan.equals(CGlobalVariables.SILVER_PLAN_VALUE_MONTH)) {
            thanksText = thanksText.replace("$", getResources().getString(R.string.silver_plan));
        } else if (plan.equals(CGlobalVariables.PLATINUM_PLAN_VALUE_YEAR) || plan.equals(CGlobalVariables.PLATINUM_PLAN_VALUE_MONTH)
                || plan.equals(CGlobalVariables.PLATINUM_PLAN_VALUE_YEAR_OMF) || plan.equals(CGlobalVariables.PLATINUM_PLAN_VALUE_MONTH_OMF)) {
            thanksText = thanksText.replace("$", getResources().getString(R.string.dhruv));
        }else if(plan.equals(CGlobalVariables.KUNDLI_AI_PLAN_VALUE_MONTH)){
            thanksText = thanksText.replace("$", getResources().getString(R.string.platinum_plan));
        }

        thankYouTextView.setText(thanksText);
        msgTextView.setText(getString(R.string.plan_login_activation_text));
        thankYouTextView.setTypeface(typeFace);
        doneBtn.setTypeface(typeFace);


        String contactDetails = CGlobalVariables.helpNumberFirst + ",\n" + CGlobalVariables.email_to_care;
        String ph1 = "+91-9560267006";

        String ph1FromContainer = CUtils.getStringData(ThanksProductPurchaseScreen.this, CGlobalVariables.key_Phone_One, ph1);
        if (ph1FromContainer != null && contactDetails.contains(ph1) && !ph1FromContainer.isEmpty()) {
            contactDetails = contactDetails.replace(ph1, ph1FromContainer);
            //Log.e("Replacing","item");
        }
        tvContactDetails.setText(contactDetails);
        Linkify.addLinks(tvContactDetails, Linkify.ALL);
        removeUnderlineAndChangeLinkColor(tvContactDetails);
        tvContactDetails.setMovementMethod(LinkMovementMethod.getInstance());
    }

    private void callMethodIfUserNotLogin() {

        //verify and send purchase info to server( updated on 2-April-2019 by abhishek )
        setPreferenceToVariables("");
        CUtils.saveBooleanData(ThanksProductPurchaseScreen.this, CGlobalVariables.needToSendDeviceIdForLogin, true);
        if (CUtils.isConnectedWithInternet(ThanksProductPurchaseScreen.this)) {
            //verifyPurchaseFromService();
        }


        String thanksText = getResources().getString(R.string.thanks_for_purchasing_membership);
        if (plan.equals(CGlobalVariables.GOLD_PLAN_VALUE_YEAR) || plan.equals(CGlobalVariables.GOLD_PLAN_VALUE_MONTH)) {
            thanksText = thanksText.replace("$", getResources().getString(R.string.golden_plan));
        } else if (plan.equals(CGlobalVariables.SILVER_PLAN_VALUE_YEAR) || plan.equals(CGlobalVariables.SILVER_PLAN_VALUE_MONTH)) {
            thanksText = thanksText.replace("$", getResources().getString(R.string.silver_plan));
        } else if (plan.equals(CGlobalVariables.PLATINUM_PLAN_VALUE_YEAR) || plan.equals(CGlobalVariables.PLATINUM_PLAN_VALUE_MONTH)) {
            thanksText = thanksText.replace("$", getResources().getString(R.string.platinum_plan));
        }


        // thanksText = thanksText.replace("0",CGlobalVariables.email_to_care);


        /*if (language_code == CGlobalVariables.HINDI) {
            thanksText = thanksText.replace("!", CGlobalVariables.helpNumberKrutidev);
        } else {
            thanksText = thanksText.replace("!", CGlobalVariables.helpNumber);
        }*/

        thankYouTextView.setText(thanksText);
        msgTextView.setText(getString(R.string.plan_not_login_activation_text));
        thankYouTextView.setTypeface(typeFace);

        String buttontext = getResources().getString(R.string.sign_in_capital);
        doneBtn.setText(buttontext);
        doneBtn.setTypeface(typeFace);
//        progressbar.setVisibility(View.GONE);
        doneBtn.setVisibility(View.VISIBLE);

        String contactDetails = CGlobalVariables.helpNumberFirst + ",\n" + CGlobalVariables.email_to_care;
        String ph1 = "+91-9560267006";

        String ph1FromContainer = CUtils.getStringData(ThanksProductPurchaseScreen.this, CGlobalVariables.key_Phone_One, ph1);
        if (ph1FromContainer != null && contactDetails.contains(ph1) && !ph1FromContainer.isEmpty()) {
            contactDetails = contactDetails.replace(ph1, ph1FromContainer);
        }
        tvContactDetails.setText(contactDetails);
        tvContactDetails.setAutoLinkMask(0); // Disable auto-link styling
        Linkify.addLinks(tvContactDetails, Linkify.ALL);
        removeUnderlineAndChangeLinkColor(tvContactDetails);
        tvContactDetails.setMovementMethod(LinkMovementMethod.getInstance());
    }

//    private void goToSignIn() {
//
//        if (CUtils.isUserLogedIn(ThanksProductPurchaseScreen.this)) {
//            int purchasePlanId = CUtils.getUserPurchasedPlanFromPreference(this);
//            if (purchasePlanId == CGlobalVariables.PLATINUM_PLAN_ID
//                    || purchasePlanId == CGlobalVariables.PLATINUM_PLAN_ID_9
//                    || purchasePlanId == CGlobalVariables.PLATINUM_PLAN_ID_10
//                    || purchasePlanId == CGlobalVariables.KUNDLI_AI_PRIMIUM_PLAN_ID_11) {
//                Intent intent = new Intent(this, ActUserPlanDetails.class);
//                startActivity(intent);
//            }
//            this.finish();
//        } else {
//            //Intent intent = new Intent(ThanksProductPurchaseScreen.this, ActLogin.class);
//            //intent.putExtra("callerActivity", CGlobalVariables.Thanks_Product_Purchase_Screen);
//            //intent.putExtra("screenId", 0);
//            Intent intent = new Intent(ThanksProductPurchaseScreen.this, LoginSignUpActivity.class);
//            intent.putExtra(IS_FROM_SCREEN, BASE_INPUT_SCREEN);
//            startActivityForResult(intent, BaseInputActivity.SUB_ACTIVITY_USER_LOGIN);
//        }
//    }

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

   /* @Override
    public void onClick(View v) {
        // TODO Auto-generated method stub
        switch (v.getId()) {

            case R.id.btnThanks:
                goToSignIn();
                break;
            default:
                break;
        }

    }*/

    @Override
    public void onBackPressed() {

        //commented by abhishek on 02-April-2019
        //If user press back without going login screen
        /*if (!CUtils.isUserLogedIn(ThanksProductPurchaseScreen.this) && sendDataToServerWithoutLogin) {
            CUtils.saveBooleanData(ThanksProductPurchaseScreen.this,CGlobalVariables.needToSendDeviceIdForLogin,true);
            //verify and send purchase info to server ( update on 23-November-2015 )
            if (CUtils.isConnectedWithInternet(ThanksProductPurchaseScreen.this)) {
                verifyPurchaseFromService();
            }
        }*/

        //super.onBackPressed();

    }

//    private void verifyPurchaseFromService() {
//        Intent pvsIntent = new Intent(getApplicationContext(),
//                PurchaseVerificationService.class);
//        pvsIntent.putExtra("SIGNATURE", signature);
//        pvsIntent.putExtra("PURCHASE_DATA", purchaseData);
//        pvsIntent.putExtra("ASTRO_USERID", astroSageUserId);
//        pvsIntent.putExtra("price", price);
//        pvsIntent.putExtra("priceCurrencycode", priceCurrencycode);
//        pvsIntent.putExtra("freetrialperiod", freetrialperiod);
//        startService(pvsIntent);
//
//    }

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
        //CUtils.saveBooleanData(ThanksProductPurchaseScreen.this,CGlobalVariables.ISSERVERVERIFY,true);
        // CUtils.saveBooleanData(ThanksProductPurchaseScreen.this,CGlobalVariables.ISGOOLGEVERIFY,true);

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

                    if (CUtils.isUserLogedIn(ThanksProductPurchaseScreen.this)) {
                        callMethodIfUserLogin(false);
                    }
                } else {
                    //commented by abhishek on 02-April-2019
                    /*setPreferenceToVariables("");

                    CUtils.saveBooleanData(ThanksProductPurchaseScreen.this,CGlobalVariables.needToSendDeviceIdForLogin,true);

                    if (CUtils.isConnectedWithInternet(ThanksProductPurchaseScreen.this)) {
                        verifyPurchaseFromService();
                    }*/

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

    private void removeUnderlineAndChangeLinkColor(TextView textView) {
        Spannable spannable = (Spannable) textView.getText();
        URLSpan[] urlSpans = spannable.getSpans(0, spannable.length(), URLSpan.class);

        for (URLSpan urlSpan : urlSpans) {
            int start = spannable.getSpanStart(urlSpan);
            int end = spannable.getSpanEnd(urlSpan);
            spannable.removeSpan(urlSpan);
            spannable.setSpan(new CustomClickableSpan(urlSpan.getURL(), this), start, end, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        }
    }

    private static class CustomClickableSpan extends ClickableSpan {
        private final String url;
        Activity activity;

        public CustomClickableSpan(String url, Activity activity) {
            this.url = url;
            this.activity = activity;
        }

        @Override
        public void onClick(View widget) {
            if (url.startsWith("mailto:")) {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
                emailIntent.setData(Uri.parse(url));
                widget.getContext().startActivity(emailIntent);
            } else if (url.startsWith("tel:")) {
                Intent dialIntent = new Intent(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse(url));
                widget.getContext().startActivity(dialIntent);
            } else {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW);
                browserIntent.setData(Uri.parse(url));
                widget.getContext().startActivity(browserIntent);
            }
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            super.updateDrawState(ds);
            ds.setColor(activity.getColor(R.color.colorPrimary_day_night)); // Set custom link color
            ds.setUnderlineText(false); // Remove underline
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

                    CUtils.storeUserPurchasedPlanInPreference(ThanksProductPurchaseScreen.this, CGlobalVariables.PLAN_PURCHASE_ID);
                }
//                progressbar.setVisibility(View.GONE);
                doneBtn.setVisibility(View.VISIBLE);

                // Toast.makeText(ThanksProductPurchaseScreen.this,"MSG "+verifyTxt,Toast.LENGTH_LONG).show();
                Log.e("CNNNNN ", verifyTxt);
            }
        }

    }

}
