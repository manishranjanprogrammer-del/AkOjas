package com.ojassoft.astrosage.ui.act;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.model.BigHorscopeProductModel;
import com.ojassoft.astrosage.model.BigHorscopeServiceModel;
import com.ojassoft.astrosage.model.ServicelistModal;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.utils.CGlobal;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_AS_USER_ID;

/**
 * Created by ojas-02 on 14/8/17.
 */

public class BigHorscopeActivity extends BaseInputActivity implements View.OnClickListener {
    TabLayout tabs;
    Toolbar toolbar;
    Button buynowPdf, buynowPrintedPdf;
    private Typeface typeface;
    TextView tvTitle, englishSample, hindiSample, bigHorscopeDetailText;
    //private String downloadPrintedPdfUrl = "https://buy.astrosage.com/virtual/astrosage-big-horoscope-url";
    private RequestQueue queue;
    CustomProgressDialog pd = null;
    TextView priceOrginal, priceDiscount, instantDelivery;
    TextView pPriceOriginal, pPriceDiscount, pInstantDelivery,
            textDiscountPlanPrinted, textDiscountPlanPdf,
            msgForBasicPlanTextPdf, unlockPlanTextPdf,
            msgForBasicPlanTextPrinted, unlockPlanTextPrinted;
    LinearLayout basicPlanUserLayoutPdf, basicPlanUserLayoutPrinted;
    String deepLinkServiceUrl, getDeepLinkProductUrl;
    private ServicelistModal servicelistModal;
    ImageView pdfOnly, printedPdf;

    public BigHorscopeActivity() {
        super(R.string.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.big_horscope);
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication())
                .getLanguageCode();
        typeface = CUtils.getRobotoFont(
                getApplicationContext(), LANGUAGE_CODE, CGlobalVariables.regular);
        toolbar = findViewById(R.id.tool_barAppModule);
        tvTitle = findViewById(R.id.tvTitle);
        bigHorscopeDetailText = findViewById(R.id.bighorscope_text);
        priceOrginal = findViewById(R.id.priceoriginal);
        priceDiscount = findViewById(R.id.pricedisount);
        instantDelivery = findViewById(R.id.instantdelivery);

        pPriceOriginal = findViewById(R.id.pricePrintedorginal);
        pPriceDiscount = findViewById(R.id.pricePrinteddiscount);
        pInstantDelivery = findViewById(R.id.instantdeliveryPrinted);

        textDiscountPlanPrinted = findViewById(R.id.text_discount_plan_printed);
        textDiscountPlanPdf = findViewById(R.id.text_discount_plan_pdf);

        unlockPlanTextPrinted = findViewById(R.id.unlock_plan_text_printed);
        unlockPlanTextPdf = findViewById(R.id.unlock_plan_text_pdf);
        msgForBasicPlanTextPrinted = findViewById(R.id.msg_for_basic_plan_text_printed);
        msgForBasicPlanTextPdf = findViewById(R.id.msg_for_basic_plan_text_pdf);
        basicPlanUserLayoutPdf = findViewById(R.id.basic_plan_user_layout_pdf);
        basicPlanUserLayoutPrinted = findViewById(R.id.basic_plan_user_layout_printed);

        bigHorscopeDetailText.setTypeface(robotRegularTypeface);
        tvTitle.setTypeface(typeface);
        tabs = findViewById(R.id.tabs).findViewById(R.id.tabs);
        tabs.setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        buynowPdf = findViewById(R.id.buy_now_pdf);
        buynowPrintedPdf = findViewById(R.id.buy_now_printed_pdf);
        buynowPdf.setTypeface(typeface);
        buynowPrintedPdf.setTypeface(typeface);

        englishSample = findViewById(R.id.english_sample_pdf);
        englishSample.setPaintFlags(priceOrginal.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        englishSample.setTypeface(robotRegularTypeface);
        hindiSample = findViewById(R.id.hind_sample_pdf);
        hindiSample.setPaintFlags(priceOrginal.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        hindiSample.setTypeface(robotRegularTypeface);
        englishSample.setOnClickListener(this);
        hindiSample.setOnClickListener(this);
        buynowPdf.setOnClickListener(this);
        buynowPrintedPdf.setOnClickListener(this);
        servicelistModal = new ServicelistModal();

        basicPlanUserLayoutPrinted.setOnClickListener(this);
        basicPlanUserLayoutPdf.setOnClickListener(this);


        pdfOnly = findViewById(R.id.pdfonlyImage);
        printedPdf = findViewById(R.id.pdfPrintedOnlyImage);
        if (LANGUAGE_CODE == 1) {
            pdfOnly.setImageResource(R.drawable.print_kundli_hi);
            printedPdf.setImageResource(R.drawable.printed_book_hi);
        } else {
            pdfOnly.setImageResource(R.drawable.print_kundli_en);
            printedPdf.setImageResource(R.drawable.printed_book);
        }
        loadBigHorscopeData();


    }

    private void displayData(BigHorscopeServiceModel data) {
        servicelistModal.setServiceId(data.getServiceId());
        servicelistModal.setTitle(data.getTitle());
        servicelistModal.setDetailDesc(data.getDetailDesc());
        servicelistModal.setSmallDesc(data.getDetailDesc());
        servicelistModal.setPriceInDollor(data.getPriceInDollor());
        servicelistModal.setPriceInRS(data.getPriceInRS());
        servicelistModal.setP_OriginalPriceInDollar(data.getOriginalPriceInDollor());
        servicelistModal.setP_OriginalPriceInRs(data.getOriginalPriceInRS());
        servicelistModal.setServiceDeepLinkURL(data.getDeepLinkUrl());
        servicelistModal.setDeliveryTime(data.getDeliveryTime());
        servicelistModal.setSmallImgURL(data.getSmallImageUrl());
        servicelistModal.setLargeImgURL(data.getLargeImageUrl());
        servicelistModal.setMessageOfCloudPlanText1(data.getMessageOfCloudPlanText1());
        servicelistModal.setMessageOfCloudPlanText2(data.getMessageOfCloudPlanText2());
        servicelistModal.setReportAvailableInLang(data.getReportAvailableInLang());
        deepLinkServiceUrl = data.getDeepLinkUrl();
        //String deliveryTime = data.getDeliveryTime();
        tvTitle.setText(data.getTitle());
        String total = getResources().getString(R.string.big_horscope_heading) + " " + data.getDetailDesc();
        SpannableStringBuilder str = new SpannableStringBuilder(total);
        final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD); // Span to make text bold
        if (LANGUAGE_CODE == 1) {
            str.setSpan(bss, 0, 51, Spannable.SPAN_INCLUSIVE_INCLUSIVE); // make first 4 characters Bold

        } else {
            str.setSpan(bss, 0, 46, Spannable.SPAN_INCLUSIVE_INCLUSIVE); // make first 4 characters Bold
        }
        bigHorscopeDetailText.setText(str);
        priceOrginal.setText(getResources().getString(R.string.astroshop_rupees_sign) + " " + data.getOriginalPriceInRS());

        try {

            priceDiscount.setText(getResources().getString(R.string.astroshop_rupees_sign) + " " + data.getPriceInRSBeforeCloudPlanDiscount());

        } catch (Exception e) {
            e.printStackTrace();
        }

        //  instantDelivery.setText(getResources().getString(R.string.delivery_in_Service).replace("#", deliveryTime));

        if ((data.getOriginalPriceInDollor() == null) || (data.getOriginalPriceInRS() == null) ||
                (data.getPriceInDollor().isEmpty()) ||
                data.getPriceInRS().equalsIgnoreCase(data.getOriginalPriceInRS())) {
            priceOrginal.setVisibility(View.GONE);


        } else {
            priceOrginal.setVisibility(View.VISIBLE);
            //priceOrginal.setPaintFlags(priceOrginal.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            //priceOrginal.setText(getResources().getString(R.string.astroshop_rupees_sign) + " " + data.getOriginalPriceInRS());

            com.ojassoft.astrosage.varta.utils.CUtils.setStrikeOnTextView(priceOrginal, getResources().getString(R.string.astroshop_rupees_sign) + " " + data.getOriginalPriceInRS() );

        }

        try {

            CUtils.showServiceProductDiscountedText(BigHorscopeActivity.this, textDiscountPlanPdf,
                    data.getMessageOfCloudPlanText1(), data.getMessageOfCloudPlanText2(), CGlobalVariables.FROM_SERVICE_TEXT);
            CUtils.showBasicPlanUserText(BigHorscopeActivity.this, msgForBasicPlanTextPdf, basicPlanUserLayoutPdf,
                    data.getMessageOfCloudPlanText1(), data.getMessageOfCloudPlanText2());

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void displayDataForProduct(BigHorscopeProductModel data) {
        getDeepLinkProductUrl = data.getDeepLinkUrl();
        pPriceOriginal.setText(getResources().getString(R.string.astroshop_rupees_sign) + " " + data.getOriginalPriceInRS());
        pPriceDiscount.setText(getResources().getString(R.string.astroshop_rupees_sign) + " " + data.getPriceInRS());
        if ((data.getOriginalPriceInDollor() == null) || (data.getOriginalPriceInRS() == null) ||
                (data.getPriceInDollor().isEmpty()) ||
                data.getPriceInRS().equalsIgnoreCase(data.getOriginalPriceInRS())) {
            pPriceOriginal.setVisibility(View.GONE);


        } else {
            pPriceOriginal.setVisibility(View.VISIBLE);
            //pPriceOriginal.setPaintFlags(pPriceOriginal.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            //pPriceOriginal.setText(getResources().getString(R.string.astroshop_rupees_sign) + " " + data.getOriginalPriceInRS());

            com.ojassoft.astrosage.varta.utils.CUtils.setStrikeOnTextView(pPriceOriginal, getResources().getString(R.string.astroshop_rupees_sign) + " " + data.getOriginalPriceInRS() );

        }

        try {

            CUtils.showServiceProductDiscountedText(BigHorscopeActivity.this, textDiscountPlanPrinted,
                    data.getMessageOfCloudPlan1(), data.getMessageOfCloudPlan2(), CGlobalVariables.FROM_PRODUCT_TEXT);

            CUtils.showBasicPlanUserText(BigHorscopeActivity.this, msgForBasicPlanTextPrinted, basicPlanUserLayoutPrinted,
                    data.getMessageOfCloudPlan1(), data.getMessageOfCloudPlan2());


        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    private void loadBigHorscopeData() {
        pd = new CustomProgressDialog(BigHorscopeActivity.this, typeface);
        pd.show();
        pd.setCancelable(false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, CGlobalVariables.bigHorscopeWebURl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Log.e("Simple+", response.toString());
                        if (pd != null && pd.isShowing()) {
                            pd.dismiss();
                        }

                        if (response != null && !response.isEmpty()) {
                            try {
                                String str = new String(response.getBytes("ISO-8859-1"), "UTF-8");
                                JSONArray jsonArray = new JSONArray(str);
                                JSONObject objService = jsonArray.getJSONObject(0);
                                JSONObject objProduct = jsonArray.getJSONObject(1);

                                parseGsonData(objService.toString());
                                parseGsonDataForProduct(objProduct.toString());

                            } catch (Exception e) {
                                MyCustomToast mct = new MyCustomToast(BigHorscopeActivity.this, BigHorscopeActivity.this.getLayoutInflater(), BigHorscopeActivity.this, typeface);
                                mct.show(getResources().getString(R.string.sign_up_validation_authentication_failed));
                                finish();
                                e.printStackTrace();
                            }

                        }

                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.e("tag", "Error Through" + error.getMessage());
                MyCustomToast mct = new MyCustomToast(BigHorscopeActivity.this, BigHorscopeActivity.this.getLayoutInflater(), BigHorscopeActivity.this, typeface);

                mct.show(getResources().getString(R.string.sign_up_validation_authentication_failed));
                //   mTextView.setText("That didn't work!");

                if (error instanceof TimeoutError) {
                    VolleyLog.d("TimeoutError: " + error.getMessage());
                    //If timeout occur it will again hit the request for get the product list
                    //      loadAstroShopData();
                } else if (error instanceof NoConnectionError) {
                    VolleyLog.d("NoConnectionError: " + error.getMessage());
                } else if (error instanceof AuthFailureError) {
                    VolleyLog.d("AuthFailureError: " + error.getMessage());
                } else if (error instanceof ServerError) {
                    VolleyLog.d("ServerError: " + error.getMessage());
                } else if (error instanceof NetworkError) {
                    VolleyLog.d("NetworkError: " + error.getMessage());
                } else if (error instanceof ParseError) {
                    VolleyLog.d("ParseError: " + error.getMessage());

                }
                pd.dismiss();
            }

        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }


            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getParams() {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("key", CUtils.getApplicationSignatureHashCode(BigHorscopeActivity.this));
                headers.put("languagecode", "" + LANGUAGE_CODE);

                // parameter added for discount
                headers.put(KEY_AS_USER_ID, CUtils.replaceEmailChar(CUtils.getUserName(BigHorscopeActivity.this)));
                headers.put("asuserplanid", String.valueOf(CUtils.getUserPurchasedPlanFromPreference(BigHorscopeActivity.this)));


                //headers.put("asuserid", CGlobalVariables.STATIC_USER_IDD_FOR_TEST);
                //headers.put("asuserplanid", CGlobalVariables.STATIC_PLAN_IDD_FOR_TEST);

                //Log.e("Data", headers.toString());
                return headers;
            }

        };

        //Log.e("tag", "API HIT HERE");

        int socketTimeout = 60000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        stringRequest.setShouldCache(true);
        queue.add(stringRequest);

    }

    private void parseGsonDataForProduct(String productData) {
        BigHorscopeProductModel data;
        Gson gson = new Gson();
        //JsonElement element = gson.fromJson(productData.toString(), JsonElement.class);
        data = gson.fromJson(productData, BigHorscopeProductModel.class);
        if (data != null) {
            displayDataForProduct(data);
        } else {
            MyCustomToast mct = new MyCustomToast(BigHorscopeActivity.this, BigHorscopeActivity.this.getLayoutInflater(), BigHorscopeActivity.this, typeface);
            mct.show("Data is not avialable");
        }
    }


    private void parseGsonData(String saveData) {
        BigHorscopeServiceModel data;
        Gson gson = new Gson();
        JsonElement element = gson.fromJson(saveData.toString(), JsonElement.class);
        data = gson.fromJson(saveData, BigHorscopeServiceModel.class);
        if (data != null) {
            displayData(data);
        } else {
            MyCustomToast mct = new MyCustomToast(BigHorscopeActivity.this, BigHorscopeActivity.this.getLayoutInflater(), BigHorscopeActivity.this, typeface);
            mct.show("Data is not avialable");
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buy_now_pdf:
                //   CUtils.getUrlLink(deepLinkServiceUrl, this, LANGUAGE_CODE, 0);
                CUtils.googleAnalyticSendWitPlayServie(this,
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_BIG_HORSCOPE_SERVICE, null);
                sendToPurchasePdf();
                break;
            case R.id.buy_now_printed_pdf:
                CUtils.getUrlLink(getDeepLinkProductUrl, this, LANGUAGE_CODE, 0);
                CUtils.googleAnalyticSendWitPlayServie(this,
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_BIG_HORSCOPE_PRODUCT, null);
                break;
            case R.id.english_sample_pdf:
                CUtils.downloadSamplePdf(0, this);
                break;
            case R.id.hind_sample_pdf:
                CUtils.downloadSamplePdf(1, this);
                break;

            case R.id.basic_plan_user_layout_pdf:
                CUtils.gotoProductPlanListUpdated(BigHorscopeActivity.this,
                        LANGUAGE_CODE, SUB_ACTIVITY_UPGRADE_PLAN_UPDTAE, CGlobalVariables.SCREEN_ID_DHRUV,"big_horscope_activity_pdf");
                break;

            case R.id.basic_plan_user_layout_printed:
                CUtils.gotoProductPlanListUpdated(BigHorscopeActivity.this,
                        LANGUAGE_CODE, SUB_ACTIVITY_UPGRADE_PLAN_UPDTAE, CGlobalVariables.SCREEN_ID_DHRUV,"big_horscope_activity_printed");
                break;
        }

    }

    private void sendToPurchasePdf() {

        boolean isCommingFromDownloadPdf = getIntent().getBooleanExtra(CGlobalVariables.DataComingFromDownloadPdf, false);
        if (isCommingFromDownloadPdf) {
            BeanHoroPersonalInfo beanHoroPersonalInfo = CGlobal.getCGlobalObject().getHoroPersonalInfoObject();

            Intent intent = new Intent(this, ActAstroServicePayment.class);
            intent.putExtra(CGlobalVariables.DataComingFromDownloadPdf, isCommingFromDownloadPdf);
            intent.putExtra("BeanHoroPersonalInfo", beanHoroPersonalInfo);
            intent.putExtra("key", servicelistModal);
            intent.putExtra("ReportAvailableInLang", servicelistModal.getReportAvailableInLang());

            startActivity(intent);

        } else {
            CUtils.getUrlLink(deepLinkServiceUrl, this, LANGUAGE_CODE, 0);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        if (menuItem.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(menuItem);
    }
}
