package com.ojassoft.astrosage.ui.act;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.widget.Toolbar;

import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.NetworkImageView;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AstroShopItemDetails;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by ojas on ६/१२/१७.
 */

public class OrderedItemDetailActivity extends BaseInputActivity implements View.OnClickListener {
    public OrderedItemDetailActivity() {
        super(R.string.app_name);
    }
    private CustomProgressDialog pd = null;
    private Typeface typeface;
    private TextView headingText,orderDate,estimatedDate,orderId,orderTotal,orderStatus,estimatedDateKey,orderDateKey,orderIdKey,orderTotalKey,orderStatusKey,orderSummaryText,itemsKey,discountKey,totalBeforeTaxKey,totalAfterTaxKey,itemsVal,discountVal,totalBeforeTaxVal;
    private TextView totalAfterTaxVal,itemName,orderDateTextView,tvTitle,c_pick_up,c_pick_up_val,c_status,c_status_val,estimated_date_key,estimated_date_val,s_detail,heading_text_shipping,awb_key,awb_value;
    private TextView orign_key,orign_value,dest_key,dest_value;
    NetworkImageView imageView;
    private RequestQueue queue;
    AstroShopItemDetails astroShopItemDetails;
    LinearLayout ll_estimate,llship,llstatus;
    String trackUrl="";
    String trackdata="";
    JSONObject shipObject;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_item_detail_layout);
        initToolBar();
        Bundle bundle = getIntent().getExtras();
         astroShopItemDetails = (AstroShopItemDetails) bundle.get("orderdetail");

         headingText = (TextView) findViewById(R.id.heading_text);
         orderDate = (TextView) findViewById(R.id.order_date_val);
         estimatedDate = (TextView) findViewById(R.id.estimated_date_val);
         orderId = (TextView) findViewById(R.id.order_id_val);
         orderTotal = (TextView) findViewById(R.id.order_total_val);
         orderStatus = (TextView) findViewById(R.id.order_status_val);
         estimatedDateKey = (TextView) findViewById(R.id.estimated_date_key);
         orderDateKey = (TextView) findViewById(R.id.order_date_key);
         orderIdKey = (TextView) findViewById(R.id.order_id_key);
         orderTotalKey = (TextView) findViewById(R.id.order_total_key);
         orderStatusKey = (TextView) findViewById(R.id.order_status_key);
         orderSummaryText = (TextView) findViewById(R.id.order_summary_text);
         itemsKey = (TextView) findViewById(R.id.items_key);
         discountKey = (TextView) findViewById(R.id.discount_key);
         totalBeforeTaxKey = (TextView) findViewById(R.id.total_before_tax_key);
         totalAfterTaxKey = (TextView) findViewById(R.id.total_after_tax_key);
         itemsVal = (TextView) findViewById(R.id.items_val);
         discountVal = (TextView) findViewById(R.id.discount_val);
         totalBeforeTaxVal = (TextView) findViewById(R.id.total_before_tax_val);
         totalAfterTaxVal = (TextView) findViewById(R.id.total_after_tax_val);
        c_pick_up= (TextView) findViewById(R.id.c_pick_up);
        c_pick_up_val= (TextView) findViewById(R.id.c_pick_up_val);
        c_status= (TextView) findViewById(R.id.c_status);
        c_status_val= (TextView) findViewById(R.id.c_status_val);
        estimated_date_key= (TextView) findViewById(R.id.estimated_date_key);
        estimated_date_val= (TextView) findViewById(R.id.estimated_date_val);
        heading_text_shipping=(TextView)  findViewById(R.id.heading_text_shipping);
        s_detail= (TextView) findViewById(R.id.s_detail);
        awb_key= (TextView) findViewById(R.id.awb_key);
        awb_value= (TextView) findViewById(R.id.awb_value);

        orign_key=(TextView) findViewById(R.id.orign_key);
        orign_value=(TextView) findViewById(R.id.orign_value);
        dest_key=(TextView) findViewById(R.id.dest_key);
        dest_value=(TextView) findViewById(R.id.dest_value);
        s_detail.setOnClickListener(this);


         itemName = (TextView) findViewById(R.id.product_name);
         orderDateTextView = (TextView) findViewById(R.id.order_date);
         imageView = (NetworkImageView) findViewById(R.id.product_imag);
         tvTitle = (TextView) findViewById(R.id.tvTitle);

        ll_estimate=(LinearLayout) findViewById(R.id.ll_estimate);
        llship=(LinearLayout) findViewById(R.id.llship);
        llstatus=(LinearLayout) findViewById(R.id.llstatus);

        typeface = CUtils.getRobotoFont(
                getApplicationContext(), LANGUAGE_CODE, CGlobalVariables.regular);
        queue = VolleySingleton.getInstance(this).getRequestQueue();

        //  item_name.setTypeface(((BaseInputActivity) context).robotRegularTypeface, Typeface.BOLD);
       // item_cost.setTypeface(((BaseInputActivity) context).robotMediumTypeface);

        orign_key.setTypeface(robotMediumTypeface);
        orign_value.setTypeface(robotRegularTypeface, Typeface.BOLD);
        dest_key.setTypeface(robotMediumTypeface);
        dest_value.setTypeface(robotRegularTypeface, Typeface.BOLD);

        heading_text_shipping.setTypeface(robotRegularTypeface, Typeface.BOLD);
        headingText.setTypeface(robotRegularTypeface, Typeface.BOLD);
        orderDate.setTypeface(robotMediumTypeface);
        estimatedDate.setTypeface(robotMediumTypeface);
        orderId.setTypeface(robotMediumTypeface);
        orderTotal.setTypeface(robotMediumTypeface);
        orderStatus.setTypeface(robotMediumTypeface);
        orderDateKey.setTypeface(robotMediumTypeface);
        estimatedDateKey.setTypeface(robotMediumTypeface);
        orderIdKey.setTypeface(robotMediumTypeface);
        orderTotalKey.setTypeface(robotMediumTypeface);
        orderStatusKey.setTypeface(robotMediumTypeface);
        orderSummaryText.setTypeface(robotRegularTypeface, Typeface.BOLD);
        itemsKey.setTypeface(robotMediumTypeface);
        discountKey.setTypeface(robotMediumTypeface);
        totalBeforeTaxKey.setTypeface(robotMediumTypeface);
        totalAfterTaxKey.setTypeface(robotRegularTypeface, Typeface.BOLD);
        itemsVal.setTypeface(robotMediumTypeface);
        discountVal.setTypeface(robotMediumTypeface);
        totalBeforeTaxVal.setTypeface(robotMediumTypeface);
        totalAfterTaxVal.setTypeface(robotRegularTypeface, Typeface.BOLD);
        itemName.setTypeface(robotRegularTypeface, Typeface.BOLD);
        orderDateTextView.setTypeface(robotMediumTypeface);
        tvTitle.setTypeface(robotMediumTypeface);


        c_pick_up.setTypeface(robotMediumTypeface);
        c_pick_up_val.setTypeface(robotMediumTypeface);
        c_status.setTypeface(robotMediumTypeface);
        c_status_val.setTypeface(robotMediumTypeface);
        estimated_date_key.setTypeface(robotMediumTypeface);
        estimated_date_val.setTypeface(robotMediumTypeface);
        s_detail.setTypeface(robotMediumTypeface);
        awb_key.setTypeface(robotMediumTypeface);
        awb_value.setTypeface(robotMediumTypeface);


        orderDate.setText(astroShopItemDetails.getO_Date());
        estimatedDate.setText(addDayInDate(astroShopItemDetails.getO_Date()));
        orderId.setText(astroShopItemDetails.getO_Id());
      //  orderTotal.setText(getString(R.string.astroshop_rupees_sign) + " " + astroShopItemDetails.getP_OriginalPriceInRs());
        orderTotal.setText(getString(R.string.astroshop_rupees_sign) + " " + roundFunction((Double.parseDouble(astroShopItemDetails.getPPriceInRs())),2));


        String orderStatusStr = astroShopItemDetails.getP_Status();
        if (orderStatusStr.equalsIgnoreCase("1") || orderStatusStr.equalsIgnoreCase("7") || orderStatusStr.equalsIgnoreCase("3")) {
            orderStatus.setText(getResources().getString(R.string.orderCancelled));
        } else if (orderStatusStr.equalsIgnoreCase("6")) {
            orderStatus.setText(getResources().getString(R.string.delivered));
        } else if (orderStatusStr.equalsIgnoreCase("2") || orderStatusStr.equalsIgnoreCase("5")) {
            orderStatus.setText(getResources().getString(R.string.processing));
        } else if (orderStatusStr.equalsIgnoreCase("4")||orderStatusStr.equalsIgnoreCase("8"))  {
            orderStatus.setText(getResources().getString(R.string.waitingpayment));
        } else if (orderStatusStr.equalsIgnoreCase("9")) {
            orderStatus.setText(getResources().getString(R.string.returned));
        }

        tvTitle.setText(getResources().getString(R.string.your_orders_title_text));
      //  itemsVal.setText(getString(R.string.astroshop_rupees_sign) + " " + astroShopItemDetails.getP_OriginalPriceInRs());
       // discountVal.setText(getString(R.string.astroshop_rupees_sign) + " " + astroShopItemDetails.getP_SaveAmountInRs());
       // totalBeforeTaxVal.setText(getString(R.string.astroshop_rupees_sign) + " " + (Integer.parseInt(astroShopItemDetails.getP_OriginalPriceInRs()) - Integer.parseInt(astroShopItemDetails.getP_SaveAmountInRs())));
      //  totalAfterTaxVal.setText(getString(R.string.astroshop_rupees_sign) + " " + (Integer.parseInt(astroShopItemDetails.getP_OriginalPriceInRs()) - Integer.parseInt(astroShopItemDetails.getP_SaveAmountInRs())));


        if (astroShopItemDetails.getPName().contains("(")) {
            //viewHolder.item_des.setVisibility(View.VISIBLE);
            String[] separated = astroShopItemDetails.getPName().split("\\(");
            itemName.setText(separated[0].trim());
            //viewHolder.item_des.setText("(" + separated[1].trim());
        } else {
            itemName.setText(astroShopItemDetails.getPName());
        }

        if (astroShopItemDetails.getPImgUrl() != null && !astroShopItemDetails.getPImgUrl().isEmpty()) {
            String imageUrl = astroShopItemDetails.getPImgUrl();
            imageView.setImageUrl(astroShopItemDetails.getPImgUrl(), VolleySingleton.getInstance(OrderedItemDetailActivity.this).getImageLoader());
        } else {
            imageView.setImageDrawable(null);
        }

        orderDateTextView.setText(getResources().getString(R.string.order_on)+" "+ astroShopItemDetails.getO_Date());
     //   trackUserOrder();
        trackOrder();
    }

    private void initToolBar() {
        Toolbar tool_barAppModule = (Toolbar) findViewById(R.id.tool_barAppModule);
        setSupportActionBar(tool_barAppModule);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setVisibility(View.GONE);
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

    private String addDayInDate(String date) {
        DateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(dateFormat.parse(date));
            c.add(Calendar.DATE, 15);
        } catch (Exception e) {

        }
        return dateFormat.format(c.getTime());
    }


    /**
     * method to track order from shipway with order id
     */

private void trackOrder()
{
    pd = new CustomProgressDialog(OrderedItemDetailActivity.this, typeface);
    pd.show();
    pd.setCancelable(false);
    final String URL = CGlobalVariables.TRACK_SHIPWAY_ORDER;
// Post params to be sent to the server
    HashMap<String, String> params = new HashMap<String, String>();
    params.put("username", CGlobalVariables.SHIPWAY_USERNAME);
    params.put("password", CGlobalVariables.SHIPWAY_LICENSE);
    params.put("order_id", astroShopItemDetails.getO_Id());

    JsonObjectRequest req = new JsonObjectRequest(URL, new JSONObject(params),
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {
                        JSONObject extrafield=null;
                        String awb="",source="",destination="",pickUpdate="",currentStatus="";
                        trackdata=response.toString();
                        String estDate="";
                        pd.dismiss();
                        String status=response.getString("status");
                        if(status.equalsIgnoreCase("Success"))
                        {
                            heading_text_shipping.setVisibility(View.VISIBLE);
                            llship.setVisibility(View.VISIBLE);
                            llstatus.setVisibility(View.GONE);
                  shipObject=response.getJSONObject("response");
                  if(!shipObject.has("scan"))
                            {
                                s_detail.setVisibility(View.GONE);
                            }
                 if(shipObject.has("awbno"))
                  awb=shipObject.getString("awbno");

                            if(shipObject.has("tracking_url"))
                                trackUrl=shipObject.getString("tracking_url");

                                awb_value.setText(awb);
                            if(shipObject.has("from"))
                             source=shipObject.getString("from");

                            if(shipObject.has("to"))
                                destination=shipObject.getString("to");

                            orign_value.setText(source);
                            dest_value.setText(destination);

                            if(shipObject.has("pickupdate"))
                             pickUpdate=shipObject.getString("pickupdate");
                            String date=CUtils.formatDateForShiping(pickUpdate);
                        c_pick_up_val.setText(date);

                            if(shipObject.has("current_status"))
                       currentStatus=shipObject.getString("current_status");
                  c_status_val.setText(currentStatus);

                  if(shipObject.has("carrier")) {
                      if (shipObject.getString("carrier").equalsIgnoreCase("Fedex")) {
                         if(shipObject.has("extra_fields")) {
                             if (!shipObject.getString("extra_fields").contains("[]")) {
                                 extrafield = shipObject.getJSONObject("extra_fields");
                             }
                         }
                          if (extrafield != null) {
                              estDate = extrafield.getString("expected_delivery_date");
                              if (estDate.isEmpty()) {
                                  estimated_date_val.setText("NA");

                              } else {
                                  estimated_date_val.setText(CUtils.formatDateForShiping(estDate));

                              }
                          } else {
                              estimated_date_val.setText("NA");

                              //   ll_estimate.setVisibility(View.GONE);
                          }
                      }
                      else
                      {
                          estimated_date_val.setText("NA");

                      }
                  }
                 else
                 {
                     estimated_date_val.setText("NA");

                     // ll_estimate.setVisibility(View.GONE);

                 }


                        }
                        else
                        {
                            heading_text_shipping.setVisibility(View.GONE);
                            llship.setVisibility(View.GONE);
                            llstatus.setVisibility(View.VISIBLE);

                        }
                        VolleyLog.v("Response:%n %s", response.toString(4));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            pd.dismiss();
            MyCustomToast mct = new MyCustomToast(OrderedItemDetailActivity.this, OrderedItemDetailActivity.this.getLayoutInflater(), OrderedItemDetailActivity.this, typeface);
            mct.show(getResources().getString(R.string.sign_up_validation_authentication_failed));
         //  OrderedItemDetailActivity.this.finish();
            VolleyLog.e("Error: ", error.getMessage());
        }
    });

// add the request object to the queue to be executed
    int socketTimeout = 60000;//30 seconds - change to what you want
    RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
    req.setRetryPolicy(policy);
    req.setShouldCache(true);
    queue.add(req);
}

    @Override
    public void onClick(View view) {
        switch(view.getId())
        {
            case R.id.s_detail:
                if(shipObject!=null &&shipObject.has("scan")) {

                    CUtils.googleAnalyticSendWitPlayServie(OrderedItemDetailActivity.this,
                            CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                            CGlobalVariables.GOOGLE_ANALYTIC_TRACK_ORDER, null);



                    Intent i = new Intent(OrderedItemDetailActivity.this, ActOrderTracking.class);
                    i.putExtra("DATA", trackdata);
                    startActivity(i);
                }
                else
                {
                    s_detail.setVisibility(View.GONE);
                }
                break;
        }
    }


    private double roundFunction(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }
}
