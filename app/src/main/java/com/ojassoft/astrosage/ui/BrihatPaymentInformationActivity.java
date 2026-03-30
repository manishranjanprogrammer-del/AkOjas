package com.ojassoft.astrosage.ui;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

import com.google.gson.Gson;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.constants.PaymentOptions;
import com.ojassoft.astrosage.model.ServicelistModal;
import com.ojassoft.astrosage.ui.act.BaseInputActivity;
import com.ojassoft.astrosage.varta.ui.activity.BaseActivity;
import com.ojassoft.astrosage.varta.ui.activity.WalletActivity;
import com.ojassoft.astrosage.varta.utils.CGlobalVariables;
import com.ojassoft.astrosage.varta.utils.CUtils;
import com.ojassoft.astrosage.varta.utils.FontUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class BrihatPaymentInformationActivity extends BaseActivity {

    private TextView tvTitle, total_mrp_text, total_mrp_amount,
            discount_mrp_text, discount_mrp_amount,
            shipping_fee_text, shipping_fee_amount,
            total_payable_text, total_payable_amount;

    private Button pay_via_paytm, pay_via_razorpay, pay_via_wallet, recharge_button;

    private CheckBox checkBox;

    private ImageView ivBack;

    private double totalPayableAmount, walletAmount;

    private ActivityResultLauncher<Intent> rechargeLauncher;

    public static final int RECHARGE_SUCCESSFUL_CODE = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brihat_payment_information);

        initViews();
        setTypeFace();
        setListeners();
        handleIntent(getIntent());
        initCheckBox();
    }

    private void handleIntent(Intent intent) {
        if (intent != null) {
            ServicelistModal servicelistModal = (ServicelistModal) getIntent().getSerializableExtra("key");
            double priceInRsBeforeDiscount = roundFunction(Double.parseDouble(servicelistModal.getP_OriginalPriceInRs()),
                    2);
            double priceInDollarBeforeDiscount = roundFunction(Double.parseDouble(servicelistModal.getP_OriginalPriceInDollar()),
                    2);
            String RsPriceText = getString(R.string.astroshop_rupees_sign) + priceInRsBeforeDiscount;
            String dollarPriceText = getString(R.string.astroshop_dollar_sign) + priceInDollarBeforeDiscount;

            totalPayableAmount = roundFunction(Double.parseDouble(servicelistModal.getPriceInRS()),
                    2);
            double priceInDollar = roundFunction(Double.parseDouble(servicelistModal.getPriceInDollor()),
                    2);
            String RsPayableAMount = getString(R.string.astroshop_rupees_sign) + totalPayableAmount;
            String dollarPayableAmount = getString(R.string.astroshop_dollar_sign) + priceInDollar;

            double discountInRs = roundFunction(priceInRsBeforeDiscount - totalPayableAmount, 2);
            double discountInDollar = roundFunction(priceInDollarBeforeDiscount - priceInDollar, 2);
            String RsDiscountPriceText = getString(R.string.astroshop_rupees_sign) + discountInRs;
            String dollarDiscountPriceText = getString(R.string.astroshop_dollar_sign) + discountInDollar;

            String text1 = RsPriceText + "/" + dollarPriceText;
            String text2 = RsDiscountPriceText + "/" + dollarDiscountPriceText;
            String text3 = RsPayableAMount + "/" + dollarPayableAmount;

            total_mrp_amount.setText(text1);
            discount_mrp_amount.setText(text2);
            shipping_fee_amount.setText(getString(R.string.text_free));
            total_payable_amount.setText(text3);
        }
    }

    private double roundFunction(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private void initCheckBox() {
        String walletAmountString = CUtils.getWalletRs(this);

        String checkBoxText = checkBox.getText().toString();
        checkBoxText = checkBoxText.replace("###", getString(R.string.astroshop_rupees_sign) + " " + walletAmountString);
        checkBox.setText(checkBoxText);

        if (!TextUtils.isEmpty(walletAmountString)) {
            walletAmount = Double.parseDouble(walletAmountString);
        } else {
            checkBox.setVisibility(View.GONE);
        }

        if (walletAmount >= totalPayableAmount) {
            checkBox.setChecked(true);
            recharge_button.setVisibility(View.GONE);
        } else {
            actionOnCheckBoxChange(false);
            recharge_button.setVisibility(View.VISIBLE);
        }
    }

    private void setListeners() {
        getOnBackPressedDispatcher().addCallback(new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });

        rechargeLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RECHARGE_SUCCESSFUL_CODE) {
                        initCheckBox();
                    }
                }
        );

        ivBack.setOnClickListener(v-> getOnBackPressedDispatcher().onBackPressed());

        checkBox.setOnCheckedChangeListener((compoundButton, isChecked) -> {
            if (walletAmount >= totalPayableAmount) {
                actionOnCheckBoxChange(isChecked);
            } else {
                checkBox.setChecked(false);
                CUtils.showSnackbar(findViewById(android.R.id.content), getString(R.string.recharge_your_wallet), this);
            }
        });

        pay_via_paytm.setOnClickListener(v->{
            selectPaymentOption(PaymentOptions.PAY_VIA_PAYTM);
        });

        pay_via_razorpay.setOnClickListener(v->{
            selectPaymentOption(PaymentOptions.PAY_VIA_RAZORPAY);
        });

        pay_via_wallet.setOnClickListener(v->{
            selectPaymentOption(PaymentOptions.PAY_VIA_WALLET);
        });

        recharge_button.setOnClickListener(v-> rechargeLauncher.launch(new Intent(this, WalletActivity.class)));
    }

    private void selectPaymentOption(int paymentOption) {
        Intent intent = new Intent();
        setResult(paymentOption,intent);
        finish();
    }

    private void actionOnCheckBoxChange(boolean isChecked) {
        if (isChecked) {
            pay_via_wallet.setVisibility(View.VISIBLE);
            /*pay_via_paytm.setVisibility(View.GONE);
            pay_via_razorpay.setVisibility(View.GONE);*/
        } else {
            pay_via_wallet.setVisibility(View.GONE);
            /*pay_via_paytm.setVisibility(View.VISIBLE);
            pay_via_razorpay.setVisibility(View.VISIBLE);*/
        }
    }

    private void initViews() {
        tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(getString(R.string.payment_information));
        ivBack = findViewById(R.id.ivBack);

        checkBox = findViewById(R.id.use_wallet_amount);

        total_mrp_text = findViewById(R.id.total_mrp_text);
        total_mrp_amount = findViewById(R.id.total_mrp_amount);
        discount_mrp_text = findViewById(R.id.discount_mrp_text);
        discount_mrp_amount = findViewById(R.id.discount_mrp_amount);
        shipping_fee_text = findViewById(R.id.shipping_fee_text);
        shipping_fee_amount = findViewById(R.id.shipping_fee_amount);
        total_payable_text = findViewById(R.id.total_payable_text);
        total_payable_amount = findViewById(R.id.total_payable_amount);

        pay_via_paytm = findViewById(R.id.pay_via_paytm);
        pay_via_razorpay = findViewById(R.id.pay_via_razorpay);
        pay_via_wallet = findViewById(R.id.pay_via_wallet);
        recharge_button = findViewById(R.id.recharge_button);
    }

    private void setTypeFace() {
        FontUtils.changeFont(this, tvTitle, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);

        FontUtils.changeFont(this, checkBox, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);

        FontUtils.changeFont(this, total_mrp_text, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(this, discount_mrp_text, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(this, shipping_fee_text, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);
        FontUtils.changeFont(this, total_payable_text, CGlobalVariables.FONTS_OPEN_SANS_REGULAR);

        FontUtils.changeFont(this, total_mrp_amount, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(this, discount_mrp_amount, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(this, shipping_fee_amount, CGlobalVariables.FONTS_OPEN_SANS_BOLD);
        FontUtils.changeFont(this, total_payable_amount, CGlobalVariables.FONTS_OPEN_SANS_BOLD);

        FontUtils.changeFont(this, pay_via_paytm, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(this, pay_via_razorpay, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(this, pay_via_wallet, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
        FontUtils.changeFont(this, recharge_button, CGlobalVariables.FONTS_OPEN_SANS_SEMIBOLD);
    }
}