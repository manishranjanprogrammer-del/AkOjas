package com.ojassoft.astrosage.ui.act;


import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.constraintlayout.widget.ConstraintLayout;

import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.varta.model.WalletAmountBean;
import com.ojassoft.astrosage.varta.ui.activity.PaymentInformationActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Activity class responsible for handling the purchase of Rs.5 recharge offer.
 * This activity allows users to view available Rs.5 recharge offer and proceed with the payment process.
 * It extends {@link BaseInputActivity} and implements {@link View.OnClickListener} to handle user interactions.
 *
 * <p>The activity initializes UI components, retrieves recharge offer data, and parses the response
 * to display the offers. It handles clicks on the recharge and exit buttons, navigating to the
 * payment screen or finishing the activity accordingly.
 *
 * <p>Key functionalities include:
 * <ul>
 *   <li>Displaying Rs.5  recharge offers retrieved from a service response.</li>
 *   <li>Handling user clicks on recharge and exit buttons.</li>
 *   <li>Navigating to the {@link PaymentInformationActivity} for payment processing.</li>
 *   <li>Showing appropriate messages for network connectivity issues or server errors.</li>
 * </ul>
 *
 * <p>The activity relies on {@link CUtils} for utility functions such as checking internet connectivity
 * and showing snackbar messages. It also uses {@link WalletAmountBean} to store and manage wallet
 * and service information.
 *
 * <p>Firebase Analytics events are logged for button clicks to track user engagement.
 * @author Gaurav
 */
public class PurchaseRechargeOfferActivity extends BaseInputActivity implements View.OnClickListener {

    WalletAmountBean walletAmountBean;
    Button rechargeBtn, exitBtn;
    ConstraintLayout containerLayout;
    String offerRechargeServiceResponse;


    public PurchaseRechargeOfferActivity() {
        super(R.id.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.e("fiveRupeeTest", "PurchaseRechargeOfferActivity: opened" );
        setContentView(R.layout.exit_recharge_layout);
        rechargeBtn = findViewById(R.id.btnRecharge);
        exitBtn = findViewById(R.id.btnExit);
        rechargeBtn.setOnClickListener(this);
        exitBtn.setOnClickListener(this);
        containerLayout = findViewById(R.id.containerLayout);
        walletAmountBean = new WalletAmountBean();
        offerRechargeServiceResponse = CUtils.getStringData(this, com.ojassoft.astrosage.utils.CGlobalVariables.RECHARGE_SERVICE_OFFER_RESPONSE_KEY, "");
        if (!TextUtils.isEmpty(offerRechargeServiceResponse)) {
            parseResponse(offerRechargeServiceResponse);
        }
    }


    /**
     * Handles the click event for the recharge button.
     * Checks for internet connectivity. If connected, it proceeds to the payment information screen.
     * If the service list is empty or null, it displays a server error message.
     * If not connected to the internet, it displays a "no internet" message.
     */
    private void onRechargeClick() {
        if (CUtils.isConnectedWithInternet(this)) {
            if (walletAmountBean.getServiceList() == null || walletAmountBean.getServiceList().isEmpty()) {
                CUtils.showSnackbar(containerLayout, getResources().getString(R.string.server_error), this);
                return;
            }
            Bundle bundle = new Bundle();
            bundle.putSerializable(CGlobalVariables.WALLET_INFO, walletAmountBean);
            bundle.putInt(CGlobalVariables.SELECTED_POSITION, 0);
            bundle.putString(CGlobalVariables.CALLING_ACTIVITY, "exitScreen");
            bundle.putString(CGlobalVariables.ASTROLGER_URL_TEXT, "");
            bundle.putString(CGlobalVariables.ASTROLOGER_MOB_NUMBER, "");
            Intent intent = new Intent(this, PaymentInformationActivity.class);
            intent.putExtras(bundle);
            startActivityForResult(intent, 1);
        } else {
            CUtils.showSnackbar(containerLayout, getResources().getString(R.string.no_internet), this);
        }
    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        try {
            switch (v.getId()) {
                case R.id.btnRecharge:
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.FIVE_RUPEE_OFFER_RECHARGE_BTN_CLICK, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                    onRechargeClick();
                    break;
                case R.id.btnExit:
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.FIVE_RUPEE_OFFER_EXIT_BTN_CLICK, CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, "");
                    exit();
                    break;
            }
        } catch (Exception e) {
            //
        }
    }

    /**
     * Parses the JSON response string to populate the walletAmountBean with service details.
     *
     * <p>This method takes a JSON string as input, which is expected to contain information
     * about various services, including GST rate, dollar conversion rate, and a list of services.
     * Each service in the list has details like service ID, name, icon, category ID, rates,
     * payment amount, offer message, and offer amount.
     *
     * <p>The method extracts these details and uses them to populate a {@link WalletAmountBean} object.
     * If the response string is null or empty, or if any error occurs during parsing (e.g.,
     * JSONException), an error message is displayed using a Snackbar, and the exception
     * stack trace is printed.
     *
     * @param response The JSON string received from the server, containing service details.
     *                 It should not be null or empty for successful parsing.
     *
     *                 <p>Example JSON structure:
     *                 <pre>
     *                 {
     *                   "gstrate": "18",
     *                   "dollarconversionrate": "75",
     *                   "services": [
     *                     {
     *                       "serviceid": "1",
     *                       "servicename": "Service A",
     *                       "smalliconfile": "icon_a.png",
     *                       "categoryid": "cat1",
     *                       "rate": "10",
     *                       "raters": "100",
     *                       "actualrate": "12",
     *                       "actualraters": "120",
     *                       "paymentamount": "1000",
     *                       "offermessage": "20% off",
     *                       "offeramout": "200"
     *                     },
     *                     ...
     *                   ]
     *                 }
     *                 </pre>
     *
     *                 <p>Each field in the JSON corresponds to a setter method in the
     *                 {@link WalletAmountBean.Services} class (for service-specific details)
     *                 and {@link WalletAmountBean} class (for global details like GST rate
     */
    private void parseResponse(String response) {
        if (response != null && response.length() > 0) {
            try {
                WalletAmountBean.Services services;
                JSONObject jsonObject = new JSONObject(response);
                String gstRate = jsonObject.getString("gstrate");
                String dollarconversionrate = jsonObject.getString("dollarconversionrate");
                JSONArray jsonArray = jsonObject.getJSONArray("services");
                JSONObject innerJsonObject;
                ArrayList<WalletAmountBean.Services> servicesArrayList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    innerJsonObject = jsonArray.getJSONObject(i);
                    services = walletAmountBean.new Services();
                    String serviceid = innerJsonObject.getString("serviceid");
                    String servicename = innerJsonObject.getString("servicename");
                    String smalliconfile = innerJsonObject.getString("smalliconfile");
                    String categoryid = innerJsonObject.getString("categoryid");
                    String rate = innerJsonObject.getString("rate");
                    String raters = innerJsonObject.getString("raters");
                    String actualrate = innerJsonObject.getString("actualrate");
                    String actualraters = innerJsonObject.getString("actualraters");
                    String paymentamount = innerJsonObject.getString("paymentamount");
                    String offermessage = innerJsonObject.getString("offermessage");
                    String offerAmount = innerJsonObject.getString("offeramout");

                    services.setServiceid(serviceid);
                    services.setServicename(servicename);
                    services.setSmalliconfile(smalliconfile);
                    services.setCategoryid(categoryid);
                    services.setRate(rate);
                    services.setRaters(raters);
                    services.setActualrate(actualrate);
                    services.setActualraters(actualraters);
                    services.setPaymentamount(paymentamount);
                    services.setOffermessage(offermessage);
                    services.setOfferAmount(offerAmount);
                    servicesArrayList.add(services);
                }
                walletAmountBean.setGstrate(gstRate);
                walletAmountBean.setDollorConverstionRate(dollarconversionrate);
                walletAmountBean.setServiceList(servicesArrayList);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            CUtils.showSnackbar(containerLayout, getResources().getString(R.string.server_error), PurchaseRechargeOfferActivity.this);
        }
    }
    private void exit() {
        setResult(RESULT_OK);
        PurchaseRechargeOfferActivity.this.finish();
    }
}
