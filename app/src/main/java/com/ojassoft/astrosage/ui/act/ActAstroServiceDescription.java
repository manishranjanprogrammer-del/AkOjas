package com.ojassoft.astrosage.ui.act;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
//import com.google.analytics.tracking.android.EasyTracker;
//import com.google.analytics.tracking.android.Log;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.model.ServicelistModal;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * Created by ojas on १/३/१६.
 */
public class ActAstroServiceDescription extends BaseInputActivity implements View.OnClickListener {
    private TextView tvTitle;
    private Toolbar tool_barAppModule;
    private NetworkImageView image_url;
    private TextView item_name;
    private TextView item_des,text_time;
    private TextView item_cost;
    private TabLayout tabLayout;
    private ImageView imgicmore, imgshopingcart;
    private Button buy_now, btn_check;

    private CheckBox checkitemavailable, checkdelivery;
    private EditText edt_pincode;
    private ServicelistModal itemdetails;
    Typeface typeface;
    private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    String astrologerId="";

    public ActAstroServiceDescription() {
        super(R.string.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LANGUAGE_CODE = ((AstrosageKundliApplication) ActAstroServiceDescription.this.getApplication())
                .getLanguageCode();

        typeface = CUtils.getRobotoFont(
                ActAstroServiceDescription.this.getApplicationContext(), LANGUAGE_CODE, CGlobalVariables.regular);
        setContentView(R.layout.lay_astroshop_item_description);

        tool_barAppModule = (Toolbar) findViewById(R.id.tool_barAppModule);
        tool_barAppModule.setNavigationIcon(R.drawable.ic_back_arrow);
        //    itemdetails = new AstroShopItemDetails();
        setSupportActionBar(tool_barAppModule);
        Bundle bundle = getIntent().getExtras();
        itemdetails = (ServicelistModal) bundle.getSerializable("key");
        astrologerId=bundle.getString("astrologerId");
        //  astroShopData.getItemCost();
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        //   tvTitle.setTypeface(typeface);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setVisibility(View.GONE);
        image_url = (NetworkImageView) findViewById(R.id.image_view);
        item_name = (TextView) findViewById(R.id.text_title);
        item_name.setTypeface(((BaseInputActivity) this).robotMediumTypeface);
        //    item_name.setTypeface(typeface);

        text_time=(TextView) findViewById(R.id.text_time);
        text_time.setTypeface(((BaseInputActivity) this).robotMediumTypeface);
        text_time.setTypeface(typeface);

        item_des = (TextView) findViewById(R.id.text_sub_title);
        item_des.setTypeface(((BaseInputActivity) this).robotRegularTypeface);
        //     item_des.setTypeface(typeface);
        item_cost = (TextView) findViewById(R.id.text__title_description);
        item_cost.setTypeface(((BaseInputActivity) this).robotMediumTypeface);
        //      item_cost.setTypeface(typeface);
        imgicmore = (ImageView) findViewById(R.id.imgMoreItem);
        buy_now = (Button) findViewById(R.id.buy_now);
        buy_now.setTypeface(((BaseInputActivity) this).regularTypeface);
        imgshopingcart = (ImageView) findViewById(R.id.imgshopingcart);
        checkitemavailable = (CheckBox) findViewById(R.id.checkitemavailable);
        checkdelivery = (CheckBox) findViewById(R.id.checkdelivery);
        edt_pincode = (EditText) findViewById(R.id.edt_pincode);
        btn_check = (Button) findViewById(R.id.btn_check);

        setViewItem();

        imgicmore.setVisibility(View.GONE);
        imgshopingcart.setVisibility(View.GONE);
        imgshopingcart.setOnClickListener(this);
        imgicmore.setOnClickListener(this);
        buy_now.setOnClickListener(this);
        //btn_check.setOnClickListener(this);

        /*getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
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

            case R.id.imgshopingcart:

                break;

            case R.id.imgMoreItem:


                break;
            case R.id.buy_now:
                Intent itemdescriptionIntent = new Intent(ActAstroServiceDescription.this, ActAstroPaymentOptions.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("key", itemdetails);
                bundle.putString("astrologerId",astrologerId);
                itemdescriptionIntent.putExtras(bundle);
                ActAstroServiceDescription.this.startActivityForResult(itemdescriptionIntent,1);
                break;
            case R.id.btn_check:

                break;
            default:
                break;
        }
    }

    private void setViewItem() {
        String[] separated = null;
        if (itemdetails != null) {
            tvTitle.setText(itemdetails.getTitle());
            item_name.setText(itemdetails.getTitle());
            item_des.setText(itemdetails.getSmallDesc());
            item_cost.setText(getResources().getString(R.string.astroshop_dollar_sign) + roundFunction(Double.parseDouble(itemdetails.getPriceInDollor()), 2) + " / " + getResources().getString(R.string.astroshop_rupees_sign) + roundFunction(Double.parseDouble(itemdetails.getPriceInRS()), 2));
            //     //Log.e("LARGEIMAGE" + itemdetails.getPId() + itemdetails.getPImgUrl() + itemdetails.getPLargeImgUrl());
            image_url.setImageUrl(itemdetails.getSmallImgURL(), VolleySingleton.getInstance(ActAstroServiceDescription.this).getImageLoader());
       text_time.setText(getResources().getString(R.string.astro_service_deliverytime).replace("@",itemdetails.getDeliveryTime()));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //android.util.//Log.e("Result recieve in frag" + requestCode, "Done" + resultCode);

        if (requestCode == 1 && resultCode == 1) {
            finish();
        }

    }


    private double roundFunction(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
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
