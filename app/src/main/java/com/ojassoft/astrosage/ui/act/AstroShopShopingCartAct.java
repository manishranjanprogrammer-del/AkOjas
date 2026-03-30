package com.ojassoft.astrosage.ui.act;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

//import com.google.analytics.tracking.android.EasyTracker;
//import com.google.analytics.tracking.android.Log;
import com.google.gson.Gson;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AstroShopItemDetails;
import com.ojassoft.astrosage.customadapters.AstroShopShippingCartAdapter;
import com.ojassoft.astrosage.jinterface.IHandleSavedCards;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ojas on ३/३/१६.
 */
public class AstroShopShopingCartAct extends BaseInputActivity implements View.OnClickListener, IHandleSavedCards {

    private TextView tvTitle, text_pay_able_ammont, text_pay;
    private Toolbar tool_barAppModule;
    private TabLayout tabLayout;
    private Button btn_continue_shopping, btn_check_out;
    private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    private Typeface typeface;
    private String title;
    Activity activity;
    private RecyclerView mRecyclerView;
    public RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private List<AstroShopItemDetails> arrayListdata;


    public AstroShopShopingCartAct() {
        super(R.id.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        activity = this;
        LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication())
                .getLanguageCode();

        typeface = CUtils.getRobotoFont(
                getApplicationContext(), LANGUAGE_CODE, CGlobalVariables.regular);
        setContentView(R.layout.lay_astroshop_shoping_cart);
        // itemCounter = 1;
        tool_barAppModule = (Toolbar) findViewById(R.id.tool_barAppModule);
        tool_barAppModule.setNavigationIcon(R.drawable.ic_back_arrow);
        setSupportActionBar(tool_barAppModule);
        arrayListdata = CUtils.getCartProducts(this);


        text_pay_able_ammont = (TextView) findViewById(R.id.text_pay_able_ammont);
        text_pay = (TextView) findViewById(R.id.text_pay);
        text_pay_able_ammont.setTypeface(((BaseInputActivity) this).mediumTypeface);

        tvTitle = (TextView) findViewById(R.id.tvTitle);
       // tvTitle.setTypeface(typeface);
        tvTitle.setText(R.string.astro_shoping_cart);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setVisibility(View.GONE);

        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(AstroShopShopingCartAct.this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        btn_continue_shopping = (Button) findViewById(R.id.btn_continue_shopping);
        btn_continue_shopping.setTypeface(((BaseInputActivity) this).regularTypeface);

        btn_check_out = (Button) findViewById(R.id.btn_check_out);
        btn_check_out.setTypeface(((BaseInputActivity) this).regularTypeface);

        if (arrayListdata != null) {
            mAdapter = new AstroShopShippingCartAdapter(this, arrayListdata, this);
            mRecyclerView.setAdapter(mAdapter);
        }

        btn_continue_shopping.setOnClickListener(this);
        btn_check_out.setOnClickListener(this);
        if (arrayListdata.size() >= 0) {
            btn_check_out.setVisibility(View.VISIBLE);
        } else {
            btn_check_out.setVisibility(View.INVISIBLE);

        }

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTotalPay();
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
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.btn_continue_shopping:
                Intent intent = new Intent(AstroShopShopingCartAct.this, ActAstroShop.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                finish();

                break;
            case R.id.btn_check_out:
                if (CUtils.getCartProducts(this).size() >= 0) {
                    Boolean outOfStock = false;
                    for (AstroShopItemDetails item : arrayListdata) {
                        if (item.getP_OutOfStock().equalsIgnoreCase("True")) {
                            outOfStock = true;
                            break;
                        }
                    }
                    if (outOfStock) {

                        MyCustomToast mct = new MyCustomToast(AstroShopShopingCartAct.this,
                                AstroShopShopingCartAct.this.getLayoutInflater(),
                                AstroShopShopingCartAct.this, typeface);
                        mct.show(getResources().getString(R.string.astroshop_outof_stock_msg));


                    } else {
                        Intent itemdescriptionIntent = new Intent(AstroShopShopingCartAct.this, ActAstroShopShippingDetails.class);
                        Bundle bundle = new Bundle();
                        bundle.putBoolean("fromCart", true);
                        itemdescriptionIntent.putExtras(bundle);
                        startActivity(itemdescriptionIntent);
                    }
                }


                break;

            default:
                break;
        }
    }


    @Override
    public void deleteCard(int position) {
        arrayListdata.remove(position);
        //Log.e("POS" + position);
        CUtils.setCartProduct(this, new Gson().toJson(arrayListdata));

        //  arrayListdata=CUtils.getCartProducts(this);
        mAdapter.notifyDataSetChanged();

        if (CUtils.getCartProducts(this).size() <= 0) {
            btn_check_out.setVisibility(View.INVISIBLE);
        }
        setTotalPay();

    }

    @Override
    public void ProceedPay(String cvv, int position) {

    }


    private double roundFunction(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private void setTotalPay() {
        Double rsTotal = 0d;
        Double dlTotal = 0d;
        ArrayList<AstroShopItemDetails> list = CUtils.getCartProducts(this);
        for (AstroShopItemDetails item : list) {
            dlTotal = dlTotal + roundFunction((Double.parseDouble(item.getPPriceInDoller())), 2);
            rsTotal = rsTotal + roundFunction((Double.parseDouble(item.getPPriceInRs())), 2);
        }
    text_pay.setText(getResources().getString(R.string.astroshop_dollar_sign) + roundFunction(Double.parseDouble(String.valueOf(dlTotal)), 2) + " / " + getResources().getString(R.string.astroshop_rupees_sign) +" "+ roundFunction(Double.parseDouble(String.valueOf(rsTotal)), 2));

    }

    @Override
    protected void onStart() {
        super.onStart();
        //EasyTracker.getInstance().activityStart(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //EasyTracker.getInstance().activityStop(this);
    }
}


