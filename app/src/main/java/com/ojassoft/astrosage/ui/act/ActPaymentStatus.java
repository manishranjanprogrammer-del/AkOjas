package com.ojassoft.astrosage.ui.act;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AstroShopData;
import com.ojassoft.astrosage.beans.AstroShopItemDetails;
import com.ojassoft.astrosage.beans.ItemOrderModel;
import com.ojassoft.astrosage.utils.AvenuesParams;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by ojas on ६/४/१६.
 */
public class ActPaymentStatus extends BaseInputActivity implements View.OnClickListener {
    public AstroShopItemDetails itemdetails;
    public int noOfItem = 1;
    Typeface typeface;
    Intent mainIntent;
    String order_id = "", status = "";
    private TextView tvTitle;
    private Toolbar tool_barAppModule;
    private TabLayout tabLayout;
    private AstroShopData astroShopData;
    private FrameLayout frame_layout;
    private int frameId = R.id.frame_layout;
    private FragmentTransaction ft;
    private ImageView tabLogin, tabShipping, tabPayment;
    private ProgressDialog pDialog;
    private Bundle bundle;
    private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    private NetworkImageView imgProduct;
    private TextView tvPay, tvAmount, tvAddresstitle, tvAddress, tvshippingLabel, tvshippimg, tvOrderLabel, tvOrderid;
    private TextView tvStatuslabel, tvStatus, tvTotalLabel, tvTotal;
    private Button btn_ok, btn_pay;
    private ItemOrderModel orderModel;
    private Boolean isFromCart = false;

    public ActPaymentStatus() {
        super(R.id.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication())
                .getLanguageCode();

        typeface = CUtils.getRobotoFont(
                getApplicationContext(), LANGUAGE_CODE, CGlobalVariables.regular);
        setContentView(R.layout.pay_status_screen);
        mainIntent = getIntent();
        init();
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                goToAstroShop();
//                this.setResult(1);
//                finish();
                break;
            case R.id.btn_pay:
                this.setResult(2);
                finish();
                break;
        }
    }

    private void goToAstroShop() {
        Intent intent = new Intent(getApplicationContext(), ActAstroShop.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
    }

    private void init() {
        if (mainIntent.getExtras() != null) {
            isFromCart = mainIntent.getBooleanExtra("fromCart", false);
            order_id = mainIntent.getStringExtra(AvenuesParams.ORDER_ID);
            if (!isFromCart) {
                itemdetails = (AstroShopItemDetails) mainIntent.getSerializableExtra("Key");

            }
            status = mainIntent.getStringExtra("Status");
            orderModel = (ItemOrderModel) mainIntent.getSerializableExtra("Order_Model");

        }

        tool_barAppModule = (Toolbar) findViewById(R.id.tool_barAppModule);
        setSupportActionBar(tool_barAppModule);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setVisibility(View.GONE);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setText(getResources().getString(R.string.home_astro_shop));
        tvTitle.setTypeface(((BaseInputActivity) this).regularTypeface);
        imgProduct = (NetworkImageView) findViewById(R.id.imgProduct);
        tvPay = (TextView) findViewById(R.id.tvPay);
        tvPay.setTypeface(((BaseInputActivity) this).mediumTypeface);
        tvAmount = (TextView) findViewById(R.id.tvAmount);
        tvAmount.setTypeface(((BaseInputActivity) this).robotRegularTypeface);
        tvAddresstitle = (TextView) findViewById(R.id.tvAddresstitle);
        tvAddresstitle.setTypeface(((BaseInputActivity) this).mediumTypeface);
        tvAddress = (TextView) findViewById(R.id.tvAddress);
        tvAddress.setTypeface(((BaseInputActivity) this).robotRegularTypeface);
        btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_ok.setTypeface(((BaseInputActivity) this).regularTypeface);
        btn_ok.setOnClickListener(this);
        btn_pay = (Button) findViewById(R.id.btn_pay);
        btn_pay.setTypeface(((BaseInputActivity) this).regularTypeface);
        btn_pay.setOnClickListener(this);
        tvStatuslabel = (TextView) findViewById(R.id.tvstatusLabel);
        tvStatuslabel.setTypeface(((BaseInputActivity) this).mediumTypeface);
        tvStatus = (TextView) findViewById(R.id.tvstatus);
        tvStatus.setText(status);
        tvStatus.setTypeface(((BaseInputActivity) this).robotRegularTypeface);
        tvshippingLabel = (TextView) findViewById(R.id.tvShippingTitle);
        tvshippingLabel.setTypeface(((BaseInputActivity) this).mediumTypeface);
        tvshippimg = (TextView) findViewById(R.id.tvshippin);
        tvshippimg.setTypeface(((BaseInputActivity) this).robotRegularTypeface);
        tvOrderLabel = (TextView) findViewById(R.id.tvOrderLabel);
        tvOrderLabel.setTypeface(((BaseInputActivity) this).mediumTypeface);
        tvOrderid = (TextView) findViewById(R.id.tvOrder);
        tvOrderid.setText(order_id);
        tvOrderid.setTypeface(((BaseInputActivity) this).robotRegularTypeface);
        tvTotalLabel = (TextView) findViewById(R.id.tvTotalLabel);
        tvTotalLabel.setTypeface(((BaseInputActivity) this).mediumTypeface);
        tvTotal = (TextView) findViewById(R.id.tvtotal);
        Double doller_total = Double.parseDouble(orderModel.getProductcost()) + Double.parseDouble(orderModel.getShippingcost());
        Double rs_total = Double.parseDouble(orderModel.getProductcost_inrs()) + Double.parseDouble(orderModel.getShippingcost_in_rs());
        tvTotal.setText(getResources().getString(R.string.astroshop_dollar_sign) + roundFunction(doller_total, 2) + " / " + getResources().getString(R.string.astroshop_rupees_sign) + " " + roundFunction(rs_total, 2));
        tvTotal.setTypeface(((BaseInputActivity) this).robotRegularTypeface);
        tvshippimg.setText(getResources().getString(R.string.astroshop_dollar_sign) + orderModel.getShippingcost() + " / " + getResources().getString(R.string.astroshop_rupees_sign) + " " + orderModel.getShippingcost_in_rs());
        if (!isFromCart) {
            imgProduct.setVisibility(View.VISIBLE);
            imgProduct.setImageUrl(itemdetails.getPLargeImgUrl(), VolleySingleton.getInstance(this).getImageLoader());

        } else {
            imgProduct.setVisibility(View.GONE);

        }
        tvAddress.setText(orderModel.getName().trim() + "\n" + orderModel.getAddress().trim() + "\n" + orderModel.getCity().trim() + "\n" + orderModel.getState().trim() + "\n" + orderModel.getPincode().trim() + "\n" + orderModel.getCountry().trim() + "\n" + orderModel.getMobileno().trim());
        tvAmount.setText(getResources().getString(R.string.astroshop_dollar_sign) + roundFunction(Double.parseDouble(orderModel.getProductcost()), 2) + " / " + getResources().getString(R.string.astroshop_rupees_sign) + " " + roundFunction(Double.parseDouble(orderModel.getProductcost_inrs()), 2));
        if (status.equalsIgnoreCase("Transaction Successful!")) {
            btn_pay.setVisibility(View.GONE);
        } else {
            btn_pay.setVisibility(View.VISIBLE);

        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (status.equalsIgnoreCase("Transaction Successful!")) {
            goToAstroShop();
        } else {
            this.setResult(2);
            finish();
        }
        super.onBackPressed();
    }

    private double roundFunction(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
