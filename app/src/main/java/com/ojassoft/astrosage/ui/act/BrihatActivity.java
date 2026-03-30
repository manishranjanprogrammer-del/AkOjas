package com.ojassoft.astrosage.ui.act;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
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
import com.ojassoft.astrosage.utils.VolleySingleton;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_AS_USER_ID;

/**
 * Created by ojas-02 on 14/8/17.
 */

public class BrihatActivity extends BaseInputActivity implements View.OnClickListener {
    TabLayout tabs;
    Toolbar toolbar;
    Button buynowPdf, buynowPrintedPdf;
    private Typeface typeface;
    TextView tvTitle, englishSample, hindiSample, bigHorscopeDetailText;
    //private String downloadPrintedPdfUrl = "https://buy.astrosage.com/virtual/astrosage-big-horoscope-url";
    private RequestQueue queue;
    CustomProgressDialog pd = null;
    TextView priceOrginal, priceDiscount, instantDelivery;
    TextView pPriceOriginal, pPriceDiscount, pInstantDelivery, textDiscountPlanPrinted, textDiscountPlanPdf,
            msgForBasicPlanTextPdf, unlockPlanTextPdf, msgForBasicPlanTextPrinted, unlockPlanTextPrinted;
    LinearLayout basicPlanUserLayoutPdf, basicPlanUserLayoutPrinted;
    String deepLinkServiceUrl, getDeepLinkProductUrl;
    private ServicelistModal servicelistModal;
    NetworkImageView pdfOnly;
    NetworkImageView printedPdf;
    ImageLoader mImageLoader;

    public BrihatActivity() {
        super(R.string.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.big_horscope);
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication()).getLanguageCode();
        typeface = CUtils.getRobotoFont(getApplicationContext(), LANGUAGE_CODE, CGlobalVariables.regular);
        toolbar = (Toolbar) findViewById(R.id.tool_barAppModule);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        bigHorscopeDetailText = (TextView) findViewById(R.id.bighorscope_text);
        priceOrginal = (TextView) findViewById(R.id.priceoriginal);
        priceDiscount = (TextView) findViewById(R.id.pricedisount);
        instantDelivery = (TextView) findViewById(R.id.instantdelivery);
        pPriceOriginal = (TextView) findViewById(R.id.pricePrintedorginal);
        pPriceDiscount = (TextView) findViewById(R.id.pricePrinteddiscount);
        pInstantDelivery = (TextView) findViewById(R.id.instantdeliveryPrinted);
        textDiscountPlanPrinted = (TextView) findViewById(R.id.text_discount_plan_printed);
        textDiscountPlanPdf = (TextView) findViewById(R.id.text_discount_plan_pdf);
        unlockPlanTextPrinted = (TextView) findViewById(R.id.unlock_plan_text_printed);
        unlockPlanTextPdf = (TextView) findViewById(R.id.unlock_plan_text_pdf);
        msgForBasicPlanTextPrinted = (TextView) findViewById(R.id.msg_for_basic_plan_text_printed);
        msgForBasicPlanTextPdf = (TextView) findViewById(R.id.msg_for_basic_plan_text_pdf);
        basicPlanUserLayoutPdf = (LinearLayout) findViewById(R.id.basic_plan_user_layout_pdf);
        basicPlanUserLayoutPrinted = (LinearLayout) findViewById(R.id.basic_plan_user_layout_printed);
        bigHorscopeDetailText.setTypeface(robotRegularTypeface);
        tvTitle.setTypeface(typeface);
        tabs = (TabLayout) findViewById(R.id.tabs).findViewById(R.id.tabs);
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        tabs.setVisibility(View.GONE);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        buynowPdf = (Button) findViewById(R.id.buy_now_pdf);
        buynowPrintedPdf = (Button) findViewById(R.id.buy_now_printed_pdf);
        buynowPdf.setTypeface(typeface);
        buynowPrintedPdf.setTypeface(typeface);
        englishSample = (TextView) findViewById(R.id.english_sample_pdf);
        englishSample.setPaintFlags(priceOrginal.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        englishSample.setTypeface(robotRegularTypeface);
        hindiSample = (TextView) findViewById(R.id.hind_sample_pdf);
        hindiSample.setPaintFlags(priceOrginal.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        hindiSample.setTypeface(robotRegularTypeface);
        englishSample.setOnClickListener(this);
        hindiSample.setOnClickListener(this);
        buynowPdf.setOnClickListener(this);
        buynowPrintedPdf.setOnClickListener(this);
        servicelistModal = new ServicelistModal();
        basicPlanUserLayoutPrinted.setOnClickListener(this);
        basicPlanUserLayoutPdf.setOnClickListener(this);
        pdfOnly = (NetworkImageView) findViewById(R.id.pdfonlyImage);
        printedPdf = (NetworkImageView) findViewById(R.id.pdfPrintedOnlyImage);
       /* if (LANGUAGE_CODE == 1) {
            pdfOnly.setImageResource(R.drawable.print_kundli_hi);
            printedPdf.setImageResource(R.drawable.printed_book_hi);
        } else {
            pdfOnly.setImageResource(R.drawable.print_kundli_en);
            printedPdf.setImageResource(R.drawable.printed_book);
        }*/

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
        String deliveryTime = data.getDeliveryTime();
        tvTitle.setText(data.getTitle());
        String total = /*getResources().getString(R.string.big_horscope_heading) + " " + */data.getDetailDesc();
        /*SpannableStringBuilder str = new SpannableStringBuilder(total);
        final StyleSpan bss = new StyleSpan(android.graphics.Typeface.BOLD); // Span to make text bold
        if (LANGUAGE_CODE == 1) {
            str.setSpan(bss, 0, 51, Spannable.SPAN_INCLUSIVE_INCLUSIVE); // make first 4 characters Bold

        } else {
            str.setSpan(bss, 0, 46, Spannable.SPAN_INCLUSIVE_INCLUSIVE); // make first 4 characters Bold
        }*/
        bigHorscopeDetailText.setText(Html.fromHtml(total));
        priceOrginal.setText(getResources().getString(R.string.astroshop_rupees_sign) + " " + data.getOriginalPriceInRS());
        try {
            priceDiscount.setText(getResources().getString(R.string.astroshop_rupees_sign) + " " + data.getPriceInRSBeforeCloudPlanDiscount());
        } catch (Exception e) {
            e.printStackTrace();
        }
        instantDelivery.setText(data.getDeliveryTime());
        mImageLoader = VolleySingleton.getInstance(this).getImageLoader();

        pdfOnly.setImageUrl(data.getSmallImageUrl(), mImageLoader);
        if ((data.getOriginalPriceInDollor() == null) || (data.getOriginalPriceInRS() == null) ||
                (data.getPriceInDollor().isEmpty()) ||
                data.getPriceInRS().equalsIgnoreCase(data.getOriginalPriceInRS())) {
            priceOrginal.setVisibility(View.GONE);
        } else {
            priceOrginal.setVisibility(View.VISIBLE);
            //priceOrginal.setPaintFlags(priceOrginal.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            //priceOrginal.setText(getResources().getString(R.string.astroshop_rupees_sign) + " " + data.getOriginalPriceInRS());
            com.ojassoft.astrosage.varta.utils.CUtils.setStrikeOnTextView(priceOrginal, getResources().getString(R.string.astroshop_rupees_sign) + " " + data.getOriginalPriceInRS());

        }

        try {
            CUtils.showServiceProductDiscountedText(BrihatActivity.this, textDiscountPlanPdf,
                    data.getMessageOfCloudPlanText1(), data.getMessageOfCloudPlanText2(), CGlobalVariables.FROM_PRODUCT_TEXT);
            CUtils.showBasicPlanUserText(BrihatActivity.this, msgForBasicPlanTextPdf, basicPlanUserLayoutPdf,
                    data.getMessageOfCloudPlanText1(), data.getMessageOfCloudPlanText2());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayDataForProduct(BigHorscopeProductModel data) {
        getDeepLinkProductUrl = data.getDeepLinkUrl();
        pPriceOriginal.setText(getResources().getString(R.string.astroshop_rupees_sign) + " " + data.getOriginalPriceInRS());
        pPriceDiscount.setText(getResources().getString(R.string.astroshop_rupees_sign) + " " + data.getPriceInRS());
        pInstantDelivery.setText(data.getDeliveryTime());
        printedPdf.setImageUrl(data.getP_SmallImageURL(), mImageLoader);
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
            CUtils.showServiceProductDiscountedText(BrihatActivity.this, textDiscountPlanPrinted,
                    data.getMessageOfCloudPlan1(), data.getMessageOfCloudPlan2(), CGlobalVariables.FROM_PRODUCT_TEXT);
            CUtils.showBasicPlanUserText(BrihatActivity.this, msgForBasicPlanTextPrinted, basicPlanUserLayoutPrinted,
                    data.getMessageOfCloudPlan1(), data.getMessageOfCloudPlan2());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void loadBigHorscopeData() {
        pd = new CustomProgressDialog(BrihatActivity.this, typeface);
        pd.show();
        pd.setCancelable(false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, CGlobalVariables.brihatHorscopeWebURl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (response != null && !response.isEmpty()) {
                            try {
                                String str = new String(response.getBytes("ISO-8859-1"), "UTF-8");
                                JSONArray jsonArray = new JSONArray(str);
                                JSONObject objService = jsonArray.getJSONObject(0);
                                JSONObject objProduct = jsonArray.getJSONObject(1);

                                parseGsonData(objService.toString());
                                parseGsonDataForProduct(objProduct.toString());

                            } catch (Exception e) {
                                MyCustomToast mct = new MyCustomToast(BrihatActivity.this, BrihatActivity.this.getLayoutInflater(), BrihatActivity.this, typeface);
                                mct.show(getResources().getString(R.string.something_wrong_error));
                                finish();
                                e.printStackTrace();
                            }

                        }
                        hideProgressBar();
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyCustomToast mct = new MyCustomToast(BrihatActivity.this, BrihatActivity.this.getLayoutInflater(), BrihatActivity.this, typeface);
                mct.show(getResources().getString(R.string.something_wrong_error));

                if (error instanceof TimeoutError) {
                    VolleyLog.d("TimeoutError: " + error.getMessage());
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
                hideProgressBar();
                finish();
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
                headers.put("key", CUtils.getApplicationSignatureHashCode(BrihatActivity.this));
                headers.put("languagecode", "" + LANGUAGE_CODE);
                // parameter added for discount
                headers.put(KEY_AS_USER_ID, CUtils.replaceEmailChar(CUtils.getUserName(BrihatActivity.this)));
                headers.put("asuserplanid", String.valueOf(CUtils.getUserPurchasedPlanFromPreference(BrihatActivity.this)));
                return headers;
            }

        };
        int socketTimeout = 60000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        stringRequest.setShouldCache(true);
        queue.add(stringRequest);
    }

    private void parseGsonDataForProduct(String productData) {
        BigHorscopeProductModel data;
        Gson gson = new Gson();
        JsonElement element = gson.fromJson(productData.toString(), JsonElement.class);
        data = gson.fromJson(productData, BigHorscopeProductModel.class);
        if (data != null) {
            displayDataForProduct(data);
        } else {
            MyCustomToast mct = new MyCustomToast(BrihatActivity.this, BrihatActivity.this.getLayoutInflater(), BrihatActivity.this, typeface);
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
            MyCustomToast mct = new MyCustomToast(BrihatActivity.this, BrihatActivity.this.getLayoutInflater(), BrihatActivity.this, typeface);
            mct.show("Data is not avialable");
        }
    }


    @Override
    public void onClick(View v) {
        String url;
        switch (v.getId()) {
            case R.id.buy_now_pdf:
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
                url = CGlobalVariables.ASTROSAGE_BASE_URL + "pdf/brihat-horoscope.pdf";
                CUtils.downloadBrihatSamplePdf(BrihatActivity.this, url, CGlobalVariables.GOOGLE_ANALYTIC_BRIHAT_ENGISH_SAMPLE_PDF_DOWNLOAD);
                //CUtils.downloadSamplePdf(0, this);
                break;
            case R.id.hind_sample_pdf:
                url = CGlobalVariables.ASTROSAGE_BASE_URL + "pdf/brihat-horoscope-hi.pdf";
                CUtils.downloadBrihatSamplePdf(BrihatActivity.this, url, CGlobalVariables.GOOGLE_ANALYTIC_BRIHAT_HINDI_SAMPLE_PDF_DOWNLOAD);

                //CUtils.downloadSamplePdf(1, this);
                break;

            case R.id.basic_plan_user_layout_pdf:
                CUtils.gotoProductPlanListUpdated(BrihatActivity.this,
                        LANGUAGE_CODE, SUB_ACTIVITY_UPGRADE_PLAN_UPDTAE, CGlobalVariables.SCREEN_ID_DHRUV,"brihat_activity_pdf");
                break;

            case R.id.basic_plan_user_layout_printed:
                CUtils.gotoProductPlanListUpdated(BrihatActivity.this,
                        LANGUAGE_CODE, SUB_ACTIVITY_UPGRADE_PLAN_UPDTAE, CGlobalVariables.SCREEN_ID_DHRUV,"brihat_activity_printed");
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

    /**
     * hide Progress Bar
     */
    private void hideProgressBar() {
        try {
            if (pd != null && pd.isShowing())
                pd.dismiss();
        } catch (final IllegalArgumentException e) {
            // Do nothing.
        } catch (final Exception e) {
            // Do nothing.
        } finally {
            pd = null;
        }
    }
}