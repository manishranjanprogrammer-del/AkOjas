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
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
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

import static com.ojassoft.astrosage.utils.CGlobalVariables.COGNI_ASTRO_GRADE10;
import static com.ojassoft.astrosage.utils.CGlobalVariables.COGNI_ASTRO_GRADE12;
import static com.ojassoft.astrosage.utils.CGlobalVariables.COGNI_ASTRO_PROFESSIONAL;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_AS_USER_ID;

/**
 * Created by ojas-02 on 14/8/17.
 */

public class CogniAstroActivity extends BaseInputActivity implements View.OnClickListener {
    ImageLoader mImageLoader;
    TabLayout tabs;
    Toolbar toolbar;
    private Typeface typeface;
    TextView tvTitle, instantDelivery, bigHorscopeDetailText, msgForBasicPlanTextPdf, unlockPlanTextPdf, textDiscountPlanPdf;
    private RequestQueue queue;
    CustomProgressDialog pd = null;
    private LinearLayout basicPlanUserLayoutPdf;

    // for grade 10
    private TextView priceOrginal10, priceDiscount10, englishSample10;
    private TextView pdfonlytext10;
    private String deepLinkServiceUrl10;
    private ServicelistModal servicelistModal10;
    private NetworkImageView pdfOnly10;
    private Button buynowPdf10;

    // for grade 12
    private TextView priceOrginal12, priceDiscount12, englishSample12;
    private TextView pdfonlytext12;
    private String deepLinkServiceUrl12;
    private ServicelistModal servicelistModal12;
    private NetworkImageView pdfOnly12;
    private Button buynowPdf12;

    // for professional
    private TextView priceOrginalProf, priceDiscountProf, englishSampleProf;
    private TextView pdfonlytextProf;
    private String deepLinkServiceUrlProf;
    private ServicelistModal servicelistModalProf;
    private NetworkImageView pdfOnlyProf;
    private Button buynowPdfProf;

    String urlProf = "", urlGrade10 = "", urlGrade12 = "";

    public CogniAstroActivity() {
        super(R.string.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cogni_astro);
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication()).getLanguageCode();
        typeface = CUtils.getRobotoFont(getApplicationContext(), LANGUAGE_CODE, CGlobalVariables.regular);
        initLayoutView();
        loadBigHorscopeData();
    }

    private void initLayoutView() {
        toolbar = (Toolbar) findViewById(R.id.tool_barAppModule);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        bigHorscopeDetailText = (TextView) findViewById(R.id.bighorscope_text);
        instantDelivery = (TextView) findViewById(R.id.instantdelivery);
        unlockPlanTextPdf = (TextView) findViewById(R.id.unlock_plan_text_pdf);
        msgForBasicPlanTextPdf = (TextView) findViewById(R.id.msg_for_basic_plan_text_pdf);
        textDiscountPlanPdf = (TextView) findViewById(R.id.text_discount_plan_pdf);
        basicPlanUserLayoutPdf = (LinearLayout) findViewById(R.id.basic_plan_user_layout_pdf);

        bigHorscopeDetailText.setTypeface(robotRegularTypeface);
        tvTitle.setTypeface(typeface);
        instantDelivery.setTypeface(mediumTypeface);
        tabs = (TabLayout) findViewById(R.id.tabs).findViewById(R.id.tabs);
        tabs.setVisibility(View.GONE);
        toolbar.setNavigationIcon(R.drawable.ic_back_arrow);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        basicPlanUserLayoutPdf.setOnClickListener(this);

        initGrade10View();
        initGrade12View();
        initGradeProfView();
    }


    private void initGrade10View() {
        pdfonlytext10 = findViewById(R.id.pdfonlytext);
        priceOrginal10 = (TextView) findViewById(R.id.priceoriginal);
        priceDiscount10 = (TextView) findViewById(R.id.pricedisount);
        englishSample10 = (TextView) findViewById(R.id.english_sample_pdf);
        buynowPdf10 = (Button) findViewById(R.id.buy_now_pdf);
        pdfOnly10 = (NetworkImageView) findViewById(R.id.pdfonlyImage);

        buynowPdf10.setTypeface(mediumTypeface);
        pdfonlytext10.setTypeface(mediumTypeface, Typeface.BOLD);
        englishSample10.setPaintFlags(englishSample10.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        englishSample10.setTypeface(robotRegularTypeface);
        englishSample10.setOnClickListener(this);
        buynowPdf10.setOnClickListener(this);
        servicelistModal10 = new ServicelistModal();

    }

    private void initGrade12View() {
        pdfonlytext12 = findViewById(R.id.pdfonlytext_12);
        priceOrginal12 = (TextView) findViewById(R.id.priceoriginal_12);
        priceDiscount12 = (TextView) findViewById(R.id.pricedisount_12);
        englishSample12 = (TextView) findViewById(R.id.english_sample_pdf_12);
        buynowPdf12 = (Button) findViewById(R.id.buy_now_pdf_12);
        pdfOnly12 = (NetworkImageView) findViewById(R.id.pdfonlyImage_12);

        buynowPdf12.setTypeface(mediumTypeface);
        pdfonlytext12.setTypeface(mediumTypeface, Typeface.BOLD);
        englishSample12.setPaintFlags(englishSample12.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        englishSample12.setTypeface(robotRegularTypeface);
        englishSample12.setOnClickListener(this);
        buynowPdf12.setOnClickListener(this);
        servicelistModal12 = new ServicelistModal();
    }

    private void initGradeProfView() {
        pdfonlytextProf = findViewById(R.id.pdfonlytext_p);
        priceOrginalProf = (TextView) findViewById(R.id.priceoriginal_p);
        priceDiscountProf = (TextView) findViewById(R.id.pricedisount_p);
        englishSampleProf = (TextView) findViewById(R.id.english_sample_pdf_p);
        buynowPdfProf = (Button) findViewById(R.id.buy_now_pdf_p);
        pdfOnlyProf = (NetworkImageView) findViewById(R.id.pdfonlyImage_p);

        buynowPdfProf.setTypeface(mediumTypeface);
        pdfonlytextProf.setTypeface(mediumTypeface, Typeface.BOLD);
        englishSampleProf.setPaintFlags(englishSampleProf.getPaintFlags() | Paint.UNDERLINE_TEXT_FLAG);
        englishSampleProf.setTypeface(robotRegularTypeface);
        englishSampleProf.setOnClickListener(this);
        buynowPdfProf.setOnClickListener(this);
        servicelistModalProf = new ServicelistModal();
    }

    private void displayData10(BigHorscopeServiceModel data) {
        servicelistModal10.setServiceId(data.getServiceId());
        servicelistModal10.setTitle(data.getTitle());
        servicelistModal10.setDetailDesc(data.getDetailDesc());
        servicelistModal10.setSmallDesc(data.getDetailDesc());
        servicelistModal10.setPriceInDollor(data.getPriceInDollor());
        servicelistModal10.setPriceInRS(data.getPriceInRS());
        servicelistModal10.setP_OriginalPriceInDollar(data.getOriginalPriceInDollor());
        servicelistModal10.setP_OriginalPriceInRs(data.getOriginalPriceInRS());
        servicelistModal10.setServiceDeepLinkURL(data.getDeepLinkUrl());
        servicelistModal10.setDeliveryTime(data.getDeliveryTime());
        servicelistModal10.setSmallImgURL(data.getSmallImageUrl());
        servicelistModal10.setLargeImgURL(data.getLargeImageUrl());
        servicelistModal10.setMessageOfCloudPlanText1(data.getMessageOfCloudPlanText1());
        servicelistModal10.setMessageOfCloudPlanText2(data.getMessageOfCloudPlanText2());
        servicelistModal10.setReportAvailableInLang(data.getReportAvailableInLang());
        deepLinkServiceUrl10 = data.getDeepLinkUrl();
        pdfonlytext10.setText(data.getServiceNameTitle());
        tvTitle.setText(Html.fromHtml(data.getTitle()));
        urlGrade10 = data.getSamplePdfUrl();
        bigHorscopeDetailText.setText(Html.fromHtml(data.getDetailDesc()));
        priceOrginal10.setText(getResources().getString(R.string.astroshop_rupees_sign) + " " + data.getOriginalPriceInRS());
        try {
            priceDiscount10.setText(getResources().getString(R.string.astroshop_rupees_sign) + " " + data.getPriceInRSBeforeCloudPlanDiscount());
        } catch (Exception e) {
            e.printStackTrace();
        }
        instantDelivery.setText(data.getDeliveryTime());
        mImageLoader = VolleySingleton.getInstance(this).getImageLoader();

        pdfOnly10.setImageUrl(data.getSmallImageUrl(), mImageLoader);
        if ((data.getOriginalPriceInDollor() == null) || (data.getOriginalPriceInRS() == null) ||
                (data.getPriceInDollor().isEmpty()) ||
                data.getPriceInRS().equalsIgnoreCase(data.getOriginalPriceInRS())) {
            priceOrginal10.setVisibility(View.GONE);
        } else {
            priceOrginal10.setVisibility(View.VISIBLE);
            //priceOrginal10.setPaintFlags(priceOrginal10.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            //priceOrginal10.setText(getResources().getString(R.string.astroshop_rupees_sign) + " " + data.getOriginalPriceInRS());

            com.ojassoft.astrosage.varta.utils.CUtils.setStrikeOnTextView(priceOrginal10, getResources().getString(R.string.astroshop_rupees_sign) + " " + data.getOriginalPriceInRS() );

        }

        try {
            CUtils.showServiceProductDiscountedText(CogniAstroActivity.this, textDiscountPlanPdf,
                    data.getMessageOfCloudPlanText1(), data.getMessageOfCloudPlanText2(), CGlobalVariables.FROM_SERVICE_TEXT);
            CUtils.showBasicPlanUserText(CogniAstroActivity.this, msgForBasicPlanTextPdf, basicPlanUserLayoutPdf,
                    data.getMessageOfCloudPlanText1(), data.getMessageOfCloudPlanText2());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void displayData12(BigHorscopeServiceModel data) {
        servicelistModal12.setServiceId(data.getServiceId());
        servicelistModal12.setTitle(data.getTitle());
        servicelistModal12.setDetailDesc(data.getDetailDesc());
        servicelistModal12.setSmallDesc(data.getDetailDesc());
        servicelistModal12.setPriceInDollor(data.getPriceInDollor());
        servicelistModal12.setPriceInRS(data.getPriceInRS());
        servicelistModal12.setP_OriginalPriceInDollar(data.getOriginalPriceInDollor());
        servicelistModal12.setP_OriginalPriceInRs(data.getOriginalPriceInRS());
        servicelistModal12.setServiceDeepLinkURL(data.getDeepLinkUrl());
        servicelistModal12.setDeliveryTime(data.getDeliveryTime());
        servicelistModal12.setSmallImgURL(data.getSmallImageUrl());
        servicelistModal12.setLargeImgURL(data.getLargeImageUrl());
        servicelistModal12.setMessageOfCloudPlanText1(data.getMessageOfCloudPlanText1());
        servicelistModal12.setMessageOfCloudPlanText2(data.getMessageOfCloudPlanText2());
        servicelistModal12.setReportAvailableInLang(data.getReportAvailableInLang());

        deepLinkServiceUrl12 = data.getDeepLinkUrl();
        pdfonlytext12.setText(data.getServiceNameTitle());
        urlGrade12 = data.getSamplePdfUrl();
        priceOrginal12.setText(getResources().getString(R.string.astroshop_rupees_sign) + " " + data.getOriginalPriceInRS());
        try {
            priceDiscount12.setText(getResources().getString(R.string.astroshop_rupees_sign) + " " + data.getPriceInRSBeforeCloudPlanDiscount());
        } catch (Exception e) {
            e.printStackTrace();
        }
        pdfOnly12.setImageUrl(data.getSmallImageUrl(), mImageLoader);
        if ((data.getOriginalPriceInDollor() == null) || (data.getOriginalPriceInRS() == null) ||
                (data.getPriceInDollor().isEmpty()) ||
                data.getPriceInRS().equalsIgnoreCase(data.getOriginalPriceInRS())) {
            priceOrginal12.setVisibility(View.GONE);
        } else {
            priceOrginal12.setVisibility(View.VISIBLE);
            //priceOrginal12.setPaintFlags(priceOrginal12.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            //priceOrginal12.setText(getResources().getString(R.string.astroshop_rupees_sign) + " " + data.getOriginalPriceInRS());

            com.ojassoft.astrosage.varta.utils.CUtils.setStrikeOnTextView(priceOrginal12, getResources().getString(R.string.astroshop_rupees_sign) + " " + data.getOriginalPriceInRS() );

        }

        try {
            CUtils.showServiceProductDiscountedText(CogniAstroActivity.this, textDiscountPlanPdf,
                    data.getMessageOfCloudPlanText1(), data.getMessageOfCloudPlanText2(), CGlobalVariables.FROM_SERVICE_TEXT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayDataProf(BigHorscopeServiceModel data) {
        servicelistModalProf.setServiceId(data.getServiceId());
        servicelistModalProf.setTitle(data.getTitle());
        servicelistModalProf.setDetailDesc(data.getDetailDesc());
        servicelistModalProf.setSmallDesc(data.getDetailDesc());
        servicelistModalProf.setPriceInDollor(data.getPriceInDollor());
        servicelistModalProf.setPriceInRS(data.getPriceInRS());
        servicelistModalProf.setP_OriginalPriceInDollar(data.getOriginalPriceInDollor());
        servicelistModalProf.setP_OriginalPriceInRs(data.getOriginalPriceInRS());
        servicelistModalProf.setServiceDeepLinkURL(data.getDeepLinkUrl());
        servicelistModalProf.setDeliveryTime(data.getDeliveryTime());
        servicelistModalProf.setSmallImgURL(data.getSmallImageUrl());
        servicelistModalProf.setLargeImgURL(data.getLargeImageUrl());
        servicelistModalProf.setMessageOfCloudPlanText1(data.getMessageOfCloudPlanText1());
        servicelistModalProf.setMessageOfCloudPlanText2(data.getMessageOfCloudPlanText2());
        servicelistModalProf.setReportAvailableInLang(data.getReportAvailableInLang());

        deepLinkServiceUrlProf = data.getDeepLinkUrl();
        pdfonlytextProf.setText(data.getServiceNameTitle());
        urlProf = data.getSamplePdfUrl();
        priceOrginalProf.setText(getResources().getString(R.string.astroshop_rupees_sign) + " " + data.getOriginalPriceInRS());
        try {
            priceDiscountProf.setText(getResources().getString(R.string.astroshop_rupees_sign) + " " + data.getPriceInRSBeforeCloudPlanDiscount());
        } catch (Exception e) {
            e.printStackTrace();
        }
        pdfOnlyProf.setImageUrl(data.getSmallImageUrl(), mImageLoader);
        if ((data.getOriginalPriceInDollor() == null) || (data.getOriginalPriceInRS() == null) ||
                (data.getPriceInDollor().isEmpty()) ||
                data.getPriceInRS().equalsIgnoreCase(data.getOriginalPriceInRS())) {
            priceOrginalProf.setVisibility(View.GONE);
        } else {
            priceOrginalProf.setVisibility(View.VISIBLE);
            //priceOrginalProf.setPaintFlags(priceOrginalProf.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            //priceOrginalProf.setText(getResources().getString(R.string.astroshop_rupees_sign) + " " + data.getOriginalPriceInRS());

            com.ojassoft.astrosage.varta.utils.CUtils.setStrikeOnTextView(priceOrginalProf, getResources().getString(R.string.astroshop_rupees_sign) + " " + data.getOriginalPriceInRS() );

        }

        try {
            CUtils.showServiceProductDiscountedText(CogniAstroActivity.this, textDiscountPlanPdf,
                    data.getMessageOfCloudPlanText1(), data.getMessageOfCloudPlanText2(), CGlobalVariables.FROM_SERVICE_TEXT);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void loadBigHorscopeData() {
        pd = new CustomProgressDialog(CogniAstroActivity.this, typeface);
        pd.show();
        pd.setCancelable(false);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, CGlobalVariables.cogniAstroWebURl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response != null && !response.isEmpty()) {
                            try {
                                String str = new String(response.getBytes("ISO-8859-1"), "UTF-8");
                                Log.i("onResponseCogni", str);
                                JSONArray jsonArray = new JSONArray(str);

                                JSONObject objServiceGrade10 = jsonArray.getJSONObject(COGNI_ASTRO_GRADE10);
                                parseGsonData(objServiceGrade10.toString(), COGNI_ASTRO_GRADE10);

                                JSONObject objServiceGrade12 = jsonArray.getJSONObject(COGNI_ASTRO_GRADE12);
                                parseGsonData(objServiceGrade12.toString(), COGNI_ASTRO_GRADE12);

                                JSONObject objServiceProf = jsonArray.getJSONObject(COGNI_ASTRO_PROFESSIONAL);
                                parseGsonData(objServiceProf.toString(), COGNI_ASTRO_PROFESSIONAL);

                            } catch (Exception e) {
                                MyCustomToast mct = new MyCustomToast(CogniAstroActivity.this, CogniAstroActivity.this.getLayoutInflater(), CogniAstroActivity.this, typeface);
                                mct.show(getResources().getString(R.string.sign_up_validation_authentication_failed));
//                                 finish();
                                e.printStackTrace();
                            }

                        }
                        pd.dismiss();
                    }

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyCustomToast mct = new MyCustomToast(CogniAstroActivity.this, CogniAstroActivity.this.getLayoutInflater(), CogniAstroActivity.this, typeface);
                mct.show(getResources().getString(R.string.sign_up_validation_authentication_failed));

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
                headers.put("key", CUtils.getApplicationSignatureHashCode(CogniAstroActivity.this));
                headers.put("languagecode", "" + LANGUAGE_CODE);
                // parameter added for discount
                headers.put(KEY_AS_USER_ID, CUtils.replaceEmailChar(CUtils.getUserName(CogniAstroActivity.this)));
                headers.put("asuserplanid", String.valueOf(CUtils.getUserPurchasedPlanFromPreference(CogniAstroActivity.this)));

                return headers;
            }

        };
        int socketTimeout = 60000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        stringRequest.setShouldCache(true);
        queue.add(stringRequest);
    }


    private void parseGsonData(String saveData, int serviceType) {
        BigHorscopeServiceModel data;
        Gson gson = new Gson();
        data = gson.fromJson(saveData, BigHorscopeServiceModel.class);
        if (data != null) {
            if (serviceType == COGNI_ASTRO_GRADE10) {
                displayData10(data);
            } else if (serviceType == COGNI_ASTRO_GRADE12) {
                displayData12(data);
            } else if (serviceType == COGNI_ASTRO_PROFESSIONAL) {
                displayDataProf(data);
            }
        } else {
            MyCustomToast mct = new MyCustomToast(CogniAstroActivity.this, CogniAstroActivity.this.getLayoutInflater(), CogniAstroActivity.this, typeface);
            mct.show("Data is not avialable");
        }
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.buy_now_pdf:
                CUtils.googleAnalyticSendWitPlayServie(this,
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_COGNI_ASTRO_SERVICE_GRADE10, null);
                sendToPurchasePdf(servicelistModal10, deepLinkServiceUrl10);
                break;
            case R.id.buy_now_pdf_12:
                CUtils.googleAnalyticSendWitPlayServie(this,
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_COGNI_ASTRO_SERVICE_GRADE12, null);
                sendToPurchasePdf(servicelistModal12, deepLinkServiceUrl12);
                break;
            case R.id.buy_now_pdf_p:
                CUtils.googleAnalyticSendWitPlayServie(this,
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                        CGlobalVariables.GOOGLE_ANALYTIC_DEEPLINK_LABEL_FOR_COGNI_ASTRO_SERVICE_PROF, null);
                sendToPurchasePdf(servicelistModalProf, deepLinkServiceUrlProf);
                break;
            case R.id.english_sample_pdf:
                CUtils.downloadBrihatSamplePdf(CogniAstroActivity.this, urlGrade10, CGlobalVariables.GOOGLE_ANALYTIC_BRIHAT_ENGISH_SAMPLE_PDF_DOWNLOAD);
                break;
            case R.id.english_sample_pdf_12:
                CUtils.downloadBrihatSamplePdf(CogniAstroActivity.this, urlGrade12, CGlobalVariables.GOOGLE_ANALYTIC_BRIHAT_ENGISH_SAMPLE_PDF_DOWNLOAD);
                break;
            case R.id.english_sample_pdf_p:
                CUtils.downloadBrihatSamplePdf(CogniAstroActivity.this, urlProf, CGlobalVariables.GOOGLE_ANALYTIC_BRIHAT_ENGISH_SAMPLE_PDF_DOWNLOAD);
                break;
            case R.id.basic_plan_user_layout_pdf:
                CUtils.gotoProductPlanListUpdated(CogniAstroActivity.this,
                        LANGUAGE_CODE, SUB_ACTIVITY_UPGRADE_PLAN_UPDTAE, CGlobalVariables.SCREEN_ID_DHRUV, "brihat_activity_pdf");
                break;

            case R.id.basic_plan_user_layout_printed:
                CUtils.gotoProductPlanListUpdated(CogniAstroActivity.this,
                        LANGUAGE_CODE, SUB_ACTIVITY_UPGRADE_PLAN_UPDTAE, CGlobalVariables.SCREEN_ID_DHRUV, "brihat_activity_printed");
                break;
        }
    }

    private void sendToPurchasePdf(ServicelistModal servicelistModal, String deepLinkServiceUrl) {
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