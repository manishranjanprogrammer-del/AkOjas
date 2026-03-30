package com.ojassoft.astrosage.ui.act;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.FragmentTransaction;
import androidx.appcompat.widget.Toolbar;

import android.text.TextUtils;
import android.text.util.Linkify;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.NetworkImageView;
//import com.google.analytics.tracking.android.EasyTracker;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AstroShopData;
import com.ojassoft.astrosage.model.ServicelistModal;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

/**
 * Created by ojas on २२/६/१६.
 */
public class ActServicePaymentStatus extends BaseInputActivity implements View.OnClickListener {
    private TextView tvTitle;
    private Toolbar tool_barAppModule;
    private TabLayout tabLayout;
    private AstroShopData astroShopData;
    private FrameLayout frame_layout;
    private int frameId = R.id.frame_layout;
    private FragmentTransaction ft;
    private ImageView tabLogin, tabShipping, tabPayment;
    private ProgressDialog pDialog;
    public ServicelistModal itemdetails;
    public int noOfItem = 1;
    private Bundle bundle;
    private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    Typeface typeface;
    Intent mainIntent;
    String order_id = "", status = "";
    private NetworkImageView imgProduct;
    private TextView tvTotalLabel;
    private String emailID = "";
    private String statusCode = "";

    private Button btn_ok, btn_pay;
    //  private AstrologerServiceInfo orderModel;
    private String fullJsonDataObj = "";

    private TextView plan_activation_text_login, tvContactDetails;

    public ActServicePaymentStatus() {
        super(R.id.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication())
                .getLanguageCode();

        typeface = CUtils.getRobotoFont(
                getApplicationContext(), LANGUAGE_CODE, CGlobalVariables.regular);
        setContentView(R.layout.lay_service_payment_status);
        mainIntent = getIntent();
        init();

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_ok:
                if (itemdetails.getServiceId().equalsIgnoreCase("93")) {
                    finish();
                } else {
                    Intent intent = new Intent(getApplicationContext(), ActAstroShopServices.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                    finish();
                }
                break;
            case R.id.btn_pay:
                this.setResult(2);
                finish();
                break;

        }
    }


    private void init() {


        tool_barAppModule = (Toolbar) findViewById(R.id.tool_barAppModule);
        setSupportActionBar(tool_barAppModule);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setVisibility(View.GONE);
        tvTitle = (TextView) findViewById(R.id.tvTitle);

        imgProduct = (NetworkImageView) findViewById(R.id.imgProduct);

        btn_ok = (Button) findViewById(R.id.btn_ok);
        btn_ok.setTypeface(((BaseInputActivity) this).robotRegularTypeface);
        btn_ok.setOnClickListener(this);
        btn_pay = (Button) findViewById(R.id.btn_pay);
        btn_pay.setTypeface(((BaseInputActivity) this).robotRegularTypeface);
        btn_pay.setOnClickListener(this);

        tvTotalLabel = (TextView) findViewById(R.id.tvTotalLabel);
        tvTotalLabel.setTypeface(((BaseInputActivity) this).robotMediumTypeface);
        plan_activation_text_login = (TextView) findViewById(R.id.plan_activation_text_login);
        plan_activation_text_login.setText(getResources().getString(R.string.ask_a_question_thanks_text));
        tvContactDetails = (TextView) findViewById(R.id.tvContactDetails);

        String contactDetails = CGlobalVariables.helpNumberFirst + ",\n" + CGlobalVariables.email_to_care;

        String ph1="+91-9560267006";

        String ph1FromContainer=CUtils.getStringData(ActServicePaymentStatus.this,CGlobalVariables.key_Phone_One,ph1);
        if(ph1FromContainer!=null && contactDetails.contains(ph1) && !ph1FromContainer.isEmpty())
        {
            contactDetails=contactDetails.replace(ph1,ph1FromContainer);
            //Log.e("Replacing","item");
        }

        tvContactDetails.setText(contactDetails);
        tvContactDetails.setLinkTextColor(getResources().getColor(R.color.black));
        Linkify.addLinks(tvContactDetails, Linkify.ALL);

        //    tvTotalLabel.setTypeface(((BaseInputActivity) this).regularTypeface);

        // tvAddress.setText(orderModel.getRegName().trim() + "\n" + orderModel.getBillingAddress().trim() + "\n" + orderModel.getBillingCity().trim() + "\n" + orderModel.getBillingState().trim() + "\n" + orderModel.getPincode().trim() + "\n" + orderModel.getBillingCountry().trim() + "\n" + orderModel.getMobileNo().trim());
        btn_pay.setVisibility(View.GONE);

        if (mainIntent.getExtras() != null) {
            //   order_id = mainIntent.getStringExtra("order_id");
            itemdetails = (ServicelistModal) mainIntent.getSerializableExtra("Key");
            status = mainIntent.getStringExtra("Status");
            emailID = mainIntent.getStringExtra("emailID");
            statusCode = mainIntent.getStringExtra("StatusCode");
            boolean isSendToWhatsapp = false;
            if(emailID.contains("@astrosage.com")){
                String[] array = emailID.split("@");
                if(array[0].matches("\\d+")){
                    isSendToWhatsapp = true;
                }
            }
            tvTitle.setText(itemdetails.getTitle());
            if(!TextUtils.isEmpty(statusCode)) {
                if(statusCode.equals("1")){
                    tvTotalLabel.setText(getText(R.string.text_service_delivery_uncleared));
                }else if (statusCode.equals("7")) {
                    tvTotalLabel.setText(getResources().getString(R.string.thanks_screen_text_service).replace("@", ": " + emailID));
                }else if(statusCode.equals("8")){
                    tvTotalLabel.setText(getString(R.string.text_service_delivered_whatsapp, com.ojassoft.astrosage.varta.utils.CUtils.getUserID(this)));
                }else if(statusCode.equals("9")){
                    tvTotalLabel.setText(getString(R.string.text_service_undelivered_whatsapp) );
                }else if(statusCode.equals("10")){
                    tvTotalLabel.setText(getText(R.string.text_service_undelivered_email));
                }
            }else {
                if(isSendToWhatsapp)
                    tvTotalLabel.setText(getString(R.string.text_service_delivered_whatsapp, com.ojassoft.astrosage.varta.utils.CUtils.getUserID(this)));
                else
                     tvTotalLabel.setText(getResources().getString(R.string.thanks_screen_text_service).replace("@", ": " + emailID));
            }
        }
        imgProduct.setImageUrl(itemdetails.getSmallImgURL(), VolleySingleton.getInstance(this).getImageLoader());

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //  this.setResult(2);
                finish();
                //onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
