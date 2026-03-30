package com.ojassoft.astrosage.ui.act;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.ojassoft.astrosage.utils.CGlobalVariables.BACK_FROM_PROFILE_CHAT_DIALOG;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_API;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_AS_USER_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_EMAIL_ID;
import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_KUNDALI_DETAILS;
import static com.ojassoft.astrosage.utils.CGlobalVariables.PACKAGE_NAME;
import static com.ojassoft.astrosage.utils.CGlobalVariables.USERID;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.APP_VERSION;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.COUNTRY_CODE;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.DEVICE_ID;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.REQUEST_CODE_SELECT_KUNDALI;
import static com.ojassoft.astrosage.varta.utils.CGlobalVariables.USER_PHONE_NO;
import static com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode;
import static com.ojassoft.astrosage.varta.utils.CUtils.getUserID;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentTransaction;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.RetryPolicy;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.libojassoft.android.misc.VolleySingleton;
import com.libojassoft.android.utils.LibCUtils;
import com.ojassoft.astrosage.BuildConfig;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.BeanHoroPersonalInfo;
import com.ojassoft.astrosage.jinterface.IAskCallback;
import com.ojassoft.astrosage.jinterface.IAstroOffersData;
import com.ojassoft.astrosage.jinterface.IPaymentFailed;
import com.ojassoft.astrosage.misc.AstroServiceDownloader;
import com.ojassoft.astrosage.model.AstrologerServiceInfo;
import com.ojassoft.astrosage.model.BigHorscopeProductModel;
import com.ojassoft.astrosage.model.ServicelistModal;
import com.ojassoft.astrosage.networkcall.ApiList;
import com.ojassoft.astrosage.networkcall.RetrofitClient;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.ui.fragments.PaymentFailedDailogFrag;
import com.ojassoft.astrosage.ui.fragments.ReportsFragment;
import com.ojassoft.astrosage.utils.CGlobal;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.CustomTypefaces;
import com.ojassoft.astrosage.utils.UserEmailFetcher;
import com.ojassoft.astrosage.varta.dialog.InSufficientBalanceDialog;
import com.ojassoft.astrosage.varta.model.UserProfileData;
import com.ojassoft.astrosage.varta.service.Loginservice;
import com.ojassoft.astrosage.varta.ui.activity.DashBoardActivity;
import com.ojassoft.astrosage.varta.ui.activity.ProfileForChat;
import com.ojassoft.astrosage.varta.utils.CustomProgressDialog;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by ojas-02 on 28/2/17.
 * /
 * /**
 * Modification by Rahul Sah on 20/5/25
 * Added Wallet payment option for report purchase and RazorPay
 * payment on single click.
 */

public class ActAstrologerDescription extends BaseInputActivity implements View.OnClickListener, IAstroOffersData, IAskCallback, PaymentResultListener, IPaymentFailed {
    static final String TAG = "ActServiceDescription";
    private TextView tvHtmlContent, tvHtmlContent1, textSilverGoldPlanPrice, textPlandiscountPrice;
    private TextView tvTitle, tvOffer, tvFinalprice, tvlabelFinalprice, msgForBasicPlanText, msgForBasicPlanPrice, unlockPlanText;
    private Toolbar tool_barAppModule;
    private NetworkImageView image_url, nivHtmlContentImage, nivHtmlContentImage1;
    private TextView item_name, item_des, txt_disscount, text__title_label, text__title_label_price, text__title_description_price, text__title_label_you_save, text__title_description_price_you_save;
    private TextView item_cost;
    private TabLayout tabLayout;
    private ImageView imgicmore, imgshopingcart, ic_share;
    private Button buy_now, btnRedeem;
    private TextInputLayout edt_promo_layout;
    private AstrologerServiceInfo astrologerServiceInfo;
    String order_Id = "";
    String payStatus = "0";
    String payId = "";
    private ServicelistModal itemdetails;
    private boolean isServicePDFReport = false;
    Typeface typeface;
    private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    String astrologerId = "";
    private BroadcastReceiver receiver;
    private ArrayList<ServicelistModal> referdata;
    private TextView tvpromo;
    private LinearLayout llPromo, llentercoupanview, llcoupanview, basicPlanUserLayout;
    private EditText etPromo, editEmail;
    private RequestQueue queue;
    private ScrollView scrollView;
    private MyCustomToast mct;
    LinearLayout servicePlanDiscountLayout;
    private TextView tvUserProfileName, tvUserProfileDetails, tvOrderFor, tvCollEdi, tvEmailError, tvLangError;
    private ImageView ivUserProfile, ivChangeProfile;
    private CheckBox checkBoxUseWallet;
    String[] monthNames = {"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
    private ActivityResultLauncher<Intent> resultLauncher;
    private String payMode = "RazorPay";
    UserProfileData userProfileData;
    private static final String COLLECTOR_EDITION_PRICE_DEFAULT = "1999";
    private Spinner languageOptions;
    String[] availLangArray;
    LinearLayout llChooseProfile;
    boolean isPopupLoginShown = false;
    String collectorsEditionDeepLink, collectorsEditionPrice;
    CustomProgressDialog pd;

    /**
     * Constructor for the ActAstrologerDescription class.
     */
    public ActAstrologerDescription() {
        super(R.string.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LANGUAGE_CODE = ((AstrosageKundliApplication) ActAstrologerDescription.this.getApplication())
                .getLanguageCode();

        typeface = CUtils.getRobotoFont(
                ActAstrologerDescription.this.getApplicationContext(), LANGUAGE_CODE, CGlobalVariables.regular);
        setContentView(R.layout.lay_astroshop_astrologer);
        mct = new MyCustomToast(ActAstrologerDescription.this, ActAstrologerDescription.this.getLayoutInflater(), ActAstrologerDescription.this, typeface);
        tool_barAppModule = findViewById(R.id.tool_barAppModule);
        //    itemdetails = new AstroShopItemDetails();
        tool_barAppModule.setNavigationIcon(R.drawable.ic_back_arrow);
        setSupportActionBar(tool_barAppModule);
        Bundle bundle = getIntent().getExtras();
        astrologerId = bundle.getString("astrologerId");
        itemdetails = (ServicelistModal) bundle.getSerializable("key");

        //  astroShopData.getItemCost();
        tvpromo = findViewById(R.id.tvpromo);
        tvTitle = findViewById(R.id.tvTitle);
        tabLayout = findViewById(R.id.tabs);
        tabLayout.setVisibility(View.GONE);
        image_url = findViewById(R.id.image_view);
        nivHtmlContentImage = findViewById(R.id.nivHtmlContentImage);
        nivHtmlContentImage1 = findViewById(R.id.nivHtmlContentImage1);

        item_name = findViewById(R.id.text_title);
        item_name.setTypeface(this.robotMediumTypeface, Typeface.BOLD);
        llPromo = findViewById(R.id.llPromo);
        llentercoupanview = findViewById(R.id.llentercoupanview);
        llcoupanview = findViewById(R.id.llcoupanview);
        text__title_label = findViewById(R.id.text__title_label);
        text__title_label_price = findViewById(R.id.text__title_label_price);
        tvFinalprice = findViewById(R.id.tvFinalprice);
        text__title_description_price = findViewById(R.id.text__title_description_price);
        edt_promo_layout = findViewById(R.id.edt_promo_layout);
        etPromo = findViewById(R.id.etPromo);
        tvOffer = findViewById(R.id.tvOffer);

        llChooseProfile = findViewById(R.id.ll_choose_profile);
        ivChangeProfile = findViewById(R.id.iv_change_profile);
        tvUserProfileName = findViewById(R.id.tv_user_name);
        tvUserProfileName.setTypeface(this.robotMediumTypeface);
        tvOrderFor = findViewById(R.id.tv_order_for);
        tvOrderFor.setTypeface(this.robotMediumTypeface);
        tvUserProfileDetails = findViewById(R.id.tv_user_dob_address);
        tvUserProfileDetails.setTypeface(this.regularTypeface);
        ivUserProfile = findViewById(R.id.iv_profile_img);
        checkBoxUseWallet = findViewById(R.id.checkbox_use_wallet);
        checkBoxUseWallet.setTypeface(this.mediumTypeface);
        languageOptions = findViewById(R.id.spinner_lang_option);
        tvCollEdi = findViewById(R.id.tv_collectors_edi);
        tvCollEdi.setTypeface(this.regularTypeface);
        editEmail = findViewById(R.id.edit_email);
        editEmail.setTypeface(this.robotMediumTypeface);
        tvEmailError = findViewById(R.id.tv_email_error);
        tvLangError = findViewById(R.id.tv_language_error);
        scrollView = findViewById(R.id.scrollView);

        ic_share = tool_barAppModule.findViewById(R.id.share);
        ic_share.setVisibility(VISIBLE);

        item_des = findViewById(R.id.text_sub_title);
        item_des.setTypeface(this.robotRegularTypeface);
        item_cost = findViewById(R.id.text__title_description);
        item_cost.setTypeface(this.robotMediumTypeface);
        imgicmore = findViewById(R.id.imgMoreItem);
        buy_now = findViewById(R.id.buy_now);
        buy_now.setTypeface(this.regularTypeface);
        btnRedeem = findViewById(R.id.btnRedeem);
        btnRedeem.setTypeface(this.regularTypeface);

        tvHtmlContent = findViewById(R.id.tvHtmlContent);
        tvHtmlContent1 = findViewById(R.id.tvHtmlContent1);

        tvHtmlContent.setTypeface(this.robotRegularTypeface);
        tvHtmlContent.setMovementMethod(LinkMovementMethod.getInstance());

        tvHtmlContent1.setTypeface(this.robotRegularTypeface);
        tvHtmlContent1.setMovementMethod(LinkMovementMethod.getInstance());

        imgshopingcart = findViewById(R.id.imgshopingcart);

        text__title_label_you_save = findViewById(R.id.text__title_label_you_save);
        text__title_description_price_you_save = findViewById(R.id.text__title_description_price_you_save);
        txt_disscount = findViewById(R.id.txt_disscount);
        text__title_description_price_you_save.setTypeface(this.robotMediumTypeface);
        text__title_label_you_save.setTypeface(this.robotMediumTypeface, Typeface.BOLD);

        text__title_description_price.setTypeface(this.robotMediumTypeface);
        text__title_label_price.setTypeface(this.robotMediumTypeface, Typeface.BOLD);
        text__title_label.setTypeface(this.robotMediumTypeface, Typeface.BOLD);
        text__title_label_you_save.setTypeface(this.robotMediumTypeface, Typeface.BOLD);
        tvlabelFinalprice = findViewById(R.id.tvlabelFinalprice);
        tvlabelFinalprice.setTypeface(this.robotMediumTypeface, Typeface.BOLD);

        tvFinalprice.setTypeface(this.robotMediumTypeface);

        textSilverGoldPlanPrice = findViewById(R.id.text_silver_gold_plan_price);
        textPlandiscountPrice = findViewById(R.id.text_plan_discount_price);
        servicePlanDiscountLayout = findViewById(R.id.service_plan_discount_layout);
        textSilverGoldPlanPrice.setTypeface(this.robotMediumTypeface, Typeface.BOLD);
        textPlandiscountPrice.setTypeface(this.robotMediumTypeface, Typeface.BOLD);

        msgForBasicPlanText = findViewById(R.id.msg_for_basic_plan_text);
        msgForBasicPlanPrice = findViewById(R.id.msg_for_basic_plan_price);
        unlockPlanText = findViewById(R.id.unlock_plan_text);
        basicPlanUserLayout = findViewById(R.id.basic_plan_user_layout);

        // msgForBasicPlanText.setTypeface(((BaseInputActivity) this).robotMediumTypeface, Typeface.BOLD);
        // msgForBasicPlanPrice.setTypeface(((BaseInputActivity) this).robotMediumTypeface, Typeface.BOLD);
        // unlockPlanText.setTypeface(((BaseInputActivity) this).robotMediumTypeface, Typeface.NORMAL);


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
//            Checkout.preload(getApplicationContext());
//        }

        setViewItem(itemdetails);

        imgicmore.setVisibility(View.GONE);
        imgshopingcart.setVisibility(View.GONE);
        imgshopingcart.setOnClickListener(this);
        imgicmore.setOnClickListener(this);
        buy_now.setOnClickListener(this);
        tvpromo.setOnClickListener(this);
        btnRedeem.setOnClickListener(this);
        ic_share.setOnClickListener(this);
        basicPlanUserLayout.setOnClickListener(this);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                referdata = (ArrayList<ServicelistModal>) intent.getSerializableExtra(AstroServiceDownloader.BROAD_RESULT);

                //Log.e("Data trecived on" + referdata.size());
                if (referdata != null && referdata.size() > 0) {

                    refreshitem(referdata);
                }
            }
        };


        etPromo.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (etPromo.getText().toString().trim().isEmpty()) {
                    edt_promo_layout.setError(getString(R.string.enter_coupon_code));
                    edt_promo_layout.setErrorEnabled(true);
                    requestFocus(etPromo);
                    etPromo.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                    // etUseremail.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                } else if ((etPromo.getText().toString().length() <= 3)) {
                    edt_promo_layout.setError(getString(R.string._enter_valid_coupon_code));
                    edt_promo_layout.setErrorEnabled(true);
                    requestFocus(etPromo);
                    etPromo.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                    // etUseremail.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                } else {
                    edt_promo_layout.setErrorEnabled(false);
                    edt_promo_layout.setError(null);
                    etPromo.getBackground().setColorFilter(getResources().getColor(R.color.ColorPrimary), PorterDuff.Mode.SRC_ATOP);
                }

            }
        });

        queue = VolleySingleton.getInstance(ActAstrologerDescription.this).getRequestQueue();
        CUtils.fetchProductsOffer(ActAstrologerDescription.this, queue, "1", String.valueOf(LANGUAGE_CODE));
      /*  scrollView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // Ready, move up
                scrollView.fullScroll(View.FOCUS_DOWN);
            }
        });*/
        //Log.d("ServiceId", "onCreate: serviceId = " + itemdetails.getServiceId());
        if (itemdetails.getIsShowProfile().equals("1") && itemdetails.getIsShowProblem().equals("0")) {
            /**
             * Check if profile needs to be shown for the current item and if a problem description is not required.
             * - If both conditions are true, hide the promo text view and show the profile selection layout.
             * - Set an onClickListener for the change profile image view to open the ProfileForChat activity.
             */
            isServicePDFReport = true;
            tvpromo.setVisibility(View.GONE);
            llChooseProfile.setVisibility(VISIBLE);
            ivChangeProfile.setOnClickListener((v) -> {
                AstrosageKundliApplication.currentEventType = "";
                Intent intent = new Intent(this, ProfileForChat.class);
                intent.putExtra("fromWhere", "profile_send");
                intent.putExtra("prefillData", true);
                startActivityForResult(intent, BACK_FROM_PROFILE_CHAT_DIALOG);
            });

            checkBoxUseWallet.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked)
                        payMode = "Wallet";
                    else
                        payMode = "RazorPay";
                }
            });
            /**
             * Get user profile data, available languages, and initialize UI elements.
             */
            userProfileData = com.ojassoft.astrosage.varta.utils.CUtils.getUserSelectedProfileFromPreference(this);
            availLangArray = getLangForAvailableReport(itemdetails.getReportAvailableInLang());
            languageOptions.setAdapter(getSpinnerAdapterLanguage(availLangArray));
            if (ReportsFragment.OPEN_FROM_KUNDLI){
                setUserProfileData(CGlobal.getCGlobalObject().getHoroPersonalInfoObject());
            }
            setProfileForReport();
        } else {
            llChooseProfile.setVisibility(View.GONE);
            tvpromo.setVisibility(VISIBLE);
        }
        String buyText = getResources().getString(R.string.buy_now_pdf, itemdetails.getPriceInRS());
        buy_now.setText(buyText);
        if (itemdetails.getServiceId().equals("148")) {
            tvCollEdi.setVisibility(VISIBLE);
            String text = getResources().getString(R.string.collector_s_edition_price, "1999");
            SpannableString spannableString = new SpannableString(text);
            spannableString.setSpan(new UnderlineSpan(), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvCollEdi.setText(spannableString);
            tvCollEdi.setOnClickListener((v) -> CUtils.getUrlLink(CGlobalVariables.BRIHAT_PRODUCT_DEEPLINK_URL, this, LANGUAGE_CODE, 0));
            loadBigHorscopeData();
        } else {
            tvCollEdi.setVisibility(View.GONE);
        }
        editEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (!CUtils.isValidEmail(editEmail.getText().toString().trim())) {
                    tvEmailError.setText(R.string.please_enter_valid_email);
                    tvEmailError.setVisibility(VISIBLE);
                } else {
                    tvEmailError.setText("");
                    tvEmailError.setVisibility(View.GONE);
                    editEmail.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(ActAstrologerDescription.this, R.color.black)));
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN | WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
        if (itemdetails != null) {
            CUtils.googleAnalyticSendWitPlayServieForAstroshop(ActAstrologerDescription.this,
                    CGlobalVariables.GOOGLE_ANALYTIC_EVENT_FOR_ASTROSHOP_VIEW,
                    itemdetails.getTitle(), null);
            showWalletOptions();
        }

    }

    /**
     * Checks if wallet options should be shown based on user login status, item properties, and send problem get user profile.
     */
    private void showWalletOptions() {
        try {
            if (com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(this)
                    && itemdetails.getIsShowProfile().equals("1") && itemdetails.getIsShowProblem().equals("0")) {
                if (AstrosageKundliApplication.isActivityVisible())
                    startService(new Intent(this, Loginservice.class));
                tvpromo.setVisibility(GONE);
                llChooseProfile.setVisibility(VISIBLE);
                getWalletPriceData();
                if (isPopupLoginShown) setProfileForReport();

            } else {
                llChooseProfile.setVisibility(View.GONE);
            }
        } catch (Exception e) {
            //
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //EasyTracker.getInstance().activityStart(this);
        LocalBroadcastManager.getInstance(this).registerReceiver((receiver),
                new IntentFilter(AstroServiceDownloader.BROAD_ACTION)
        );
    }

    @Override
    protected void onStop() {
        super.onStop();
        //EasyTracker.getInstance().activityStop(this);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
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
            case R.id.tvpromo:
                tvpromo.setVisibility(View.GONE);
                llPromo.setVisibility(VISIBLE);
                llentercoupanview.setVisibility(VISIBLE);
                etPromo.requestFocus();
                scrollView.fullScroll(View.FOCUS_DOWN);
              /*  if (llPromo.getVisibility() == View.VISIBLE) {
                    llPromo.setVisibility(View.GONE);
                    buy_now.requestFocus();
                } else {
                    llPromo.setVisibility(View.VISIBLE);
                    llentercoupanview.setVisibility(View.VISIBLE);
                    etPromo.requestFocus();
                    scrollView.fullScroll(View.FOCUS_DOWN);
                }*/
                break;

            case R.id.share:
                try {
                    String urlToShare = itemdetails.getServiceDeepLinkURL();
                    if (urlToShare != null && !urlToShare.isEmpty()) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_TEXT, itemdetails.getTitle() + " - " + urlToShare);
                        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.astro_service_share_title));
                        startActivity(Intent.createChooser(intent, "Share"));
                    } else {
                        Toast.makeText(this, "Bad url", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {

                }
                break;

            case R.id.imgshopingcart:

                break;


            case R.id.imgMoreItem:
                break;
            case R.id.buy_now:
                /**
                 * if user is not logged in to vartathen show login popup
                 */
                if (!com.ojassoft.astrosage.varta.utils.CUtils.getUserLoginStatus(this)) {
                    isPopupLoginShown = true;
                    AstrosageKundliApplication.isOpenVartaPopup = true;
                    Intent intent1 = new Intent(this, FlashLoginActivity.class);
                    startActivity(intent1);
                    return;
                }
                String title = "";
                if (itemdetails != null) {
                    title = itemdetails.getTitle();
                    CUtils.googleAnalyticSendWitPlayServieForAstroshop(ActAstrologerDescription.this,
                            CGlobalVariables.GOOGLE_ANALYTIC_EVENT_FOR_ASTROSHOP_BUY,
                            title, null);
                }

                /**
                 * If the item is report type then single click on buy now button,
                 * proceed with payment via wallet or RazorPay based on checkbox selection.
                 */
                if (itemdetails.getIsShowProfile().equals("1") && itemdetails.getIsShowProblem().equals("0")) {
                    if (astrologerServiceInfo == null) {
                        Toast.makeText(this, getString(R.string.please_select_order_for), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (!validateField()) return;
                    //String problem = "## " + "(lang: " + getSelectedLanguageCode() + ", " + "appversion: " + BuildConfig.VERSION_NAME + ")";
                    astrologerServiceInfo.setProblem(getProblemText());
                    astrologerServiceInfo.setEmailID(editEmail.getText().toString().trim());

                    CUtils.saveAstroshopUserEmail(this, editEmail.getText().toString().trim());

                    if (checkBoxUseWallet.isChecked()) {
                        astrologerServiceInfo.setPayMode("Wallet");
                        payViaWallet(astrologerServiceInfo);
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, CGlobalVariables.FIREBASE_EVENT_REPORT_PURCHASE_WITH_WALLET, "");
                    } else {
                        showLoader();
                        astrologerServiceInfo.setPayMode("RazorPay");

                        CUtils.getOrderID(ActAstrologerDescription.this, typeface, queue, astrologerServiceInfo);
                        CUtils.fcmAnalyticsEvents(CGlobalVariables.FIREBASE_EVENT_ITEM_CLICK, CGlobalVariables.FIREBASE_EVENT_REPORT_PURCHASE_WITH_REZORPAY, "");
                    }
                } else {
                    /**
                     * is item is not report type old flow
                     */
                    Intent itemdescriptionIntent = new Intent(ActAstrologerDescription.this, ActAstroServicePayment.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("key", itemdetails);
                    bundle.putString("astrologerId", astrologerId);
                    bundle.putString("ReportAvailableInLang", itemdetails.getReportAvailableInLang());
                    itemdescriptionIntent.putExtras(bundle);
                    ActAstrologerDescription.this.startActivityForResult(itemdescriptionIntent, 1);
                    break;
                }
            case R.id.btn_check:

                break;
            case R.id.btnRedeem:
                if (validateName(etPromo, edt_promo_layout, "")) {
                    CUtils.applyCoupon(ActAstrologerDescription.this, typeface, queue, etPromo.getText().toString().trim(), itemdetails.getServiceId());
                }
                break;

            case R.id.basic_plan_user_layout:
                CUtils.gotoProductPlanListUpdated(ActAstrologerDescription.this,
                        LANGUAGE_CODE, SUB_ACTIVITY_UPGRADE_PLAN_UPDTAE, CGlobalVariables.SCREEN_ID_DHRUV, "act_astrologer_description");

                break;
            default:
                break;
        }
    }


    private void setViewItem(ServicelistModal itemdetails) {
        try {
            String[] separated = null;
            if (itemdetails == null) {
                return;
            }
            tvTitle.setText(itemdetails.getTitle());
            item_name.setText(itemdetails.getTitle());
            item_des.setText(itemdetails.getSmallDesc());
            String buyText = getResources().getString(R.string.buy_now_pdf, itemdetails.getPriceInRS());
            buy_now.setText(buyText);
            CUtils.fcmAnalyticsEvents(itemdetails.getTitle(),
                    FirebaseAnalytics.Event.VIEW_ITEM, "");

            int planId = 1;
            planId = CUtils.getUserPurchasedPlanFromPreference(ActAstrologerDescription.this);

            if (planId == CGlobalVariables.SILVER_PLAN_ID ||
                    planId == CGlobalVariables.SILVER_PLAN_ID_5 ||
                    planId == CGlobalVariables.SILVER_PLAN_ID_4 ||
                    planId == CGlobalVariables.GOLD_PLAN_ID ||
                    planId == CGlobalVariables.GOLD_PLAN_ID_7 ||
                    planId == CGlobalVariables.GOLD_PLAN_ID_6 ||
                    planId == CGlobalVariables.PLATINUM_PLAN_ID ||
                    planId == CGlobalVariables.PLATINUM_PLAN_ID_9 ||
                    planId == CGlobalVariables.PLATINUM_PLAN_ID_10 ||
                    planId == CGlobalVariables.KUNDLI_AI_PRIMIUM_PLAN_ID_11) {
                if (itemdetails.getPriceInRSBeforeCloudPlanDiscount().equalsIgnoreCase(itemdetails.getP_OriginalPriceInRs())) {
                    item_cost.setVisibility(View.GONE);
                    text__title_label.setVisibility(View.GONE);
                } else {
                    item_cost.setVisibility(VISIBLE);
                    text__title_label.setVisibility(VISIBLE);
                    //item_cost.setText(getResources().getString(R.string.astroshop_dollar_sign) + roundFunction(Double.parseDouble(itemdetails.getPriceInDollor()), 2) + " / " + getResources().getString(R.string.astroshop_rupees_sign) + " " + roundFunction(Double.parseDouble(itemdetails.getPriceInRS()), 2));
                    item_cost.setText(getResources().getString(R.string.astroshop_dollar_sign) +
                            roundFunction(Double.parseDouble(itemdetails.getPriceInDollorBeforeCloudPlanDiscount()),
                                    2) + " / " + getResources().getString(R.string.astroshop_rupees_sign) +
                            " " + roundFunction(Double.parseDouble(itemdetails.getPriceInRSBeforeCloudPlanDiscount()),
                            2));
                }
            } else {
                item_cost.setVisibility(VISIBLE);
                text__title_label.setVisibility(VISIBLE);
                item_cost.setText(getResources().getString(R.string.astroshop_dollar_sign) + roundFunction(Double.parseDouble(itemdetails.getPriceInDollor()), 2) + " / " + getResources().getString(R.string.astroshop_rupees_sign) + " " + roundFunction(Double.parseDouble(itemdetails.getPriceInRS()), 2));
            }

            image_url.setImageUrl(itemdetails.getSmallImgURL(), VolleySingleton.getInstance(ActAstrologerDescription.this).getImageLoader());

            if ((itemdetails.getP_OriginalPriceInDollar() == null) ||
                    (itemdetails.getP_OriginalPriceInRs() == null) ||
                    (itemdetails.getP_OriginalPriceInRs().isEmpty()) ||
                    itemdetails.getPriceInRS().equalsIgnoreCase(itemdetails.getP_OriginalPriceInRs())) {
                txt_disscount.setVisibility(View.GONE);
                text__title_description_price.setVisibility(View.GONE);
                text__title_description_price_you_save.setVisibility(View.GONE);
                text__title_label_you_save.setVisibility(View.GONE);
                text__title_label_price.setVisibility(View.GONE);

                text__title_description_price.setVisibility(View.GONE);
                servicePlanDiscountLayout.setVisibility(View.GONE);

                text__title_label.setText(getResources().getString(R.string.astroshop_price));
                item_cost.setTextColor(getResources().getColor(R.color.black));

            } else {

                servicePlanDiscountLayout.setVisibility(VISIBLE);

                String discoumt = "";
                if (itemdetails.getP_SavePercentOfRs().contains(".")) {
                    String[] str = itemdetails.getP_SavePercentOfRs().split("\\.");
                    discoumt = str[0];
                } else {
                    discoumt = itemdetails.getP_SavePercentOfRs();
                }
                txt_disscount.setText(discoumt.trim() + "%" + "\n" + getString(R.string.off));
                //text__title_description_price.setText(getResources().getString(R.string.astroshop_dollar_sign).trim() + roundFunction(Double.parseDouble(itemdetails.getP_OriginalPriceInDollar()), 2) + " / " + getResources().getString(R.string.astroshop_rupees_sign) + " " + roundFunction(Double.parseDouble(itemdetails.getP_OriginalPriceInRs()), 2));
                //text__title_description_price.setPaintFlags(text__title_description_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                text__title_description_price_you_save.setText(getResources().getString(R.string.astroshop_dollar_sign).trim() + roundFunction(Double.parseDouble(itemdetails.getP_SaveAmountInDollar()), 2) + " / " + getResources().getString(R.string.astroshop_rupees_sign) + " " + roundFunction(Double.parseDouble(itemdetails.getP_SaveAmountInRs()), 2) + " " + "(" + discoumt.trim() + "%" + ")");

                com.ojassoft.astrosage.varta.utils.CUtils.setStrikeOnTextView(text__title_description_price, getResources().getString(R.string.astroshop_dollar_sign).trim() + roundFunction(Double.parseDouble(itemdetails.getP_OriginalPriceInDollar()), 2) + " / " + getResources().getString(R.string.astroshop_rupees_sign) + " " + roundFunction(Double.parseDouble(itemdetails.getP_OriginalPriceInRs()), 2));

            }


            Log.e("Plan IDD ", "" + planId);

            //If plan id = basic plan , silver monthly, gold monthly
            if (!CUtils.isDhruvPlan(this) /*|| planId == CGlobalVariables.SILVER_PLAN_ID_4 ||
                        planId == CGlobalVariables.GOLD_PLAN_ID_6*/) {
                basicPlanUserLayout.setVisibility(VISIBLE);
                textSilverGoldPlanPrice.setVisibility(View.GONE);
                textPlandiscountPrice.setVisibility(View.GONE);

                String plan_name = itemdetails.getMessageOfCloudPlanText1();
                String discountText = itemdetails.getMessageOfCloudPlanText2();

                if (!TextUtils.isEmpty(plan_name) && !TextUtils.isEmpty(discountText)) {
                    discountText = "<b><font color='#e65100'>  " + discountText + "</font></b>";
                    msgForBasicPlanText.setText(Html.fromHtml("  " + plan_name + " " + discountText));

                } else {
                    basicPlanUserLayout.setVisibility(View.GONE);
                }
                msgForBasicPlanPrice.setText(getResources().getString(R.string.astroshop_dollar_sign) +
                        roundFunction(Double.parseDouble(itemdetails.getNonCloudPriceInDollor()), 2) +
                        " / " + getResources().getString(R.string.astroshop_rupees_sign) +
                        " " + roundFunction(Double.parseDouble(itemdetails.getNonCloudPriceInRS()), 2));

            } else {

                basicPlanUserLayout.setVisibility(View.GONE);

                textSilverGoldPlanPrice.setVisibility(View.VISIBLE);
                textPlandiscountPrice.setVisibility(View.VISIBLE);


                //if plan id = silver yearly plan
                if (planId == CGlobalVariables.SILVER_PLAN_ID || planId == CGlobalVariables.SILVER_PLAN_ID_5 || planId == CGlobalVariables.SILVER_PLAN_ID_4) {
                    textSilverGoldPlanPrice.setText(getResources().getString(R.string.silver_plan_price));
                } else if (planId == CGlobalVariables.GOLD_PLAN_ID || planId == CGlobalVariables.GOLD_PLAN_ID_6 || planId == CGlobalVariables.GOLD_PLAN_ID_7) {
                    textSilverGoldPlanPrice.setText(getResources().getString(R.string.gold_plan_price));
                } else {
                    String plan_name = itemdetails.getMessageOfCloudPlanText1() +" "+ getResources().getString(R.string.astroshop_price);
                    textSilverGoldPlanPrice.setText(plan_name);
                }
                textPlandiscountPrice.setText(getResources().getString(R.string.astroshop_dollar_sign) +
                        roundFunction(Double.parseDouble(itemdetails.getPriceInDollor()), 2) +
                        " / " + getResources().getString(R.string.astroshop_rupees_sign) +
                        " " + roundFunction(Double.parseDouble(itemdetails.getPriceInRS()), 2));
            }

            String htmlContent = itemdetails.getHtmlContent();
            String htmlContent1 = itemdetails.getHtmlContent1();
            if (htmlContent != null && !htmlContent.equals("")) {
                tvHtmlContent.setVisibility(View.VISIBLE);
                tvHtmlContent.setText(Html.fromHtml(htmlContent));
            }
            if (htmlContent1 != null && !htmlContent1.equals("")) {
                tvHtmlContent1.setVisibility(View.VISIBLE);
                tvHtmlContent1.setText(Html.fromHtml(htmlContent1));
            }

            if (itemdetails.getHtmlContentImgURL() != null && !itemdetails.getHtmlContentImgURL().equals("")) {
                nivHtmlContentImage.setVisibility(VISIBLE);
                nivHtmlContentImage.setImageUrl(itemdetails.getHtmlContentImgURL(), VolleySingleton.getInstance(ActAstrologerDescription.this).getImageLoader());
            }
            if (itemdetails.getHtmlContentImgURL1() != null && !itemdetails.getHtmlContentImgURL1().equals("")) {
                nivHtmlContentImage1.setVisibility(VISIBLE);
                nivHtmlContentImage1.setImageUrl(itemdetails.getHtmlContentImgURL(), VolleySingleton.getInstance(ActAstrologerDescription.this).getImageLoader());
            }

        } catch (Exception ex) {
            //
        }

    }

    private void refreshitem(List<ServicelistModal> datasaved) {
        //Log.e("refreshing data");
        if (datasaved != null && datasaved.size() > 0) {
            for (ServicelistModal modal : datasaved) {
                if (modal.getServiceId().equalsIgnoreCase(this.itemdetails.getServiceId())) {
                    this.itemdetails = modal;
                    setViewItem(this.itemdetails);
                    break;
                }
            }
        }

    }


    /**
     * Called when an activity you launched exits, giving you the requestCode
     * you started it with, the resultCode it returned, and any additional
     * data from it.  The <var>resultCode</var> will be
     * {@link #RESULT_CANCELED} if the activity explicitly returned that,
     * didn't return any result, or crashed during its operation.
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode  The integer result code returned by the child activity
     *                    through its setResult().
     * @param data        An Intent, which can return result data to the caller
     *                    (various data can be attached to Intent "extras").
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //android.util.//Log.e("Result recieve in frag" + requestCode, "Done" + resultCode);

        if (requestCode == 1 && resultCode == 1) {
            finish();
        } else if (resultCode == DashBoardActivity.BACK_FROM_PROFILECHATDIALOG) {
            boolean isProceed = data.getBooleanExtra("IS_PROCEED", false);

            if (!isProceed && data.getExtras().containsKey("openKundliList")) {

                com.ojassoft.astrosage.varta.utils.CUtils.openSavedKundliList(this, "profile_send", BACK_FROM_PROFILE_CHAT_DIALOG);
            } else if (data.getExtras().containsKey("openProfileForChat") && !data.hasExtra(CGlobalVariables.KEY_KUNDALI_DETAILS)) {
                Bundle bundle = new Bundle();
                bundle.putBoolean(com.ojassoft.astrosage.utils.CGlobalVariables.VARTA_PROFILE_QUERY_DATA, true);
                //bundle.putInt("PAGER_INDEX", isKundliAvailable);
                Intent intent = new Intent(this, HomeInputScreen.class);
                intent.putExtras(bundle);
                startActivityForResult(intent, REQUEST_CODE_SELECT_KUNDALI);
            }

            if (data.hasExtra(CGlobalVariables.KEY_KUNDALI_DETAILS)) {
                // Handle result from ProfileForChat
                BeanHoroPersonalInfo beanHoroPersonalInfo = (BeanHoroPersonalInfo) data.getSerializableExtra(CGlobalVariables.KEY_KUNDALI_DETAILS);
                setUserProfileData(beanHoroPersonalInfo);
            } else if (data.hasExtra("USER_DETAIL")) {
                userProfileData = (UserProfileData) data.getSerializableExtra("USER_DETAIL");
            }
            setServiceInfo(userProfileData, payMode);
            setProfileForReport();
        } else if (resultCode == RESULT_OK && data.hasExtra(CGlobalVariables.KEY_KUNDALI_DETAILS)) {
            // Handle result from HomeInputScreen
            BeanHoroPersonalInfo beanHoroPersonalInfo = (BeanHoroPersonalInfo) data.getSerializableExtra(KEY_KUNDALI_DETAILS);
            setUserProfileData(beanHoroPersonalInfo);
            setServiceInfo(userProfileData, payMode);
            setProfileForReport();
        }

    }


    /**
     * Rounds a double value to a specified number of decimal places.
     *
     * @param value  The value to be rounded.
     * @param places The number of decimal places.
     * @return The rounded value.
     */
    private double roundFunction(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    /**
     * Requests focus on a view and shows the soft input mode.
     *
     * @param view The view to request focus on.
     */
    private void requestFocus(View view) {
        if (view.requestFocus()) {
            ActAstrologerDescription.this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    /**
     * Validates the name entered in the EditText.
     *
     * @param name        The EditText to validate.
     * @param inputLayout The TextInputLayout associated with the EditText.
     * @param message     The error message to display.
     * @return True if the name is valid, false otherwise.
     */
    private boolean validateName(EditText name, TextInputLayout inputLayout, String message) {
        boolean value = true;


        if (name == etPromo) {

            if (name.getText().toString().trim().isEmpty()) {
                inputLayout.setError(getString(R.string.enter_coupon_code));
                inputLayout.setErrorEnabled(true);
                requestFocus(name);
                name.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                value = false;
                // etUseremail.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
            } else if ((name.getText().toString().length() <= 3)) {
                inputLayout.setError(getString(R.string._enter_valid_coupon_code));
                inputLayout.setErrorEnabled(true);
                requestFocus(name);
                name.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                value = false;
                // etUseremail.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
            } else {
                inputLayout.setErrorEnabled(false);
                inputLayout.setError(null);
                name.getBackground().setColorFilter(getResources().getColor(R.color.ColorPrimary), PorterDuff.Mode.SRC_ATOP);
            }


        }
        return value;
    }


    @Override
    public void getResult(String result) {
        if (result != null && result.length() > 0) {
            tvOffer.setVisibility(VISIBLE);
            tvOffer.setText(result);
        } else {
            tvOffer.setVisibility(View.GONE);
        }
    }

    /**
     * Callback method for handling the result of CUtils API call.
     */
    @Override
    public void getCallBack(String result, CUtils.callBack callback, String priceInDollor, String priceInRs) {
        if (callback == CUtils.callBack.APPLY_SERVICE_COUPON) {
            Log.e("response", result);
            if (result != null && !result.isEmpty()) {
                try {
                    JSONArray array = new JSONArray(result);
                    JSONObject resultObject = array.getJSONObject(0);
                    String status = resultObject.getString("P_Status");
                    if (status != null && !status.isEmpty() && status.equalsIgnoreCase("1")) {
                        CUtils.googleAnalyticSendWitPlayServie(ActAstrologerDescription.this,
                                CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                                CGlobalVariables.GOOGLE_ANALYTIC_Coupon_Applied_Success, null);
                        CUtils.hideMyKeyboard(ActAstrologerDescription.this);
                        updateModal(array.getJSONObject(1));
                    } else {
                        CUtils.googleAnalyticSendWitPlayServie(ActAstrologerDescription.this,
                                CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                                CGlobalVariables.GOOGLE_ANALYTIC_Coupon_Applied_Failed, null);
                        Animation animShake = AnimationUtils.loadAnimation(getApplicationContext(),
                                R.anim.shake);
                        etPromo.startAnimation(animShake);
                        //  MyCustomToast mct = new MyCustomToast(ActAstroShop.this, ActAstroShop.this.getLayoutInflater(), ActAstroShop.this, typeface);
                        mct.show(getResources().getString(R.string.sign_up_validation_authentication_failed));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                mct.show(getResources().getString(R.string.sign_up_validation_authentication_failed));
                CUtils.hideMyKeyboard(ActAstrologerDescription.this);

            }
        } else if (callback == CUtils.callBack.GET_ORDER_ID) {
            /**
             * order_id generated from server and redirect to razor pay PG
             */
            if (result != null && result.equalsIgnoreCase(CGlobalVariables.RESULT_CODE_THREE)) {
                mct.show(getString(R.string.coupon_not_valid));
            } else {
                hideLoader();
                order_Id = result;
                //VolleyLog.d("Ask from server order_Id " + order_Id);
                //com.google.analytics.tracking.android.//Log.e("order" + order_Id);
                if (order_Id != null && !order_Id.isEmpty()) {

                    if (priceInRs != null && priceInRs.length() > 0) {
                        astrologerServiceInfo.setPriceRs(priceInRs);
                    }

                    if (priceInDollor != null && priceInDollor.length() > 0) {
                        astrologerServiceInfo.setPrice(priceInDollor);
                    }

                    if (astrologerServiceInfo.getPayMode() != null && astrologerServiceInfo.getPayMode().equalsIgnoreCase("RazorPay")) {
                        Double paise = Double.parseDouble(astrologerServiceInfo.getPriceRs()) * 100;
                        CUtils.googleAnalyticSendWitPlayServie(ActAstrologerDescription.this,
                                CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                                CGlobalVariables.ORDER_ID_GENERATED, null);
                        goToRazorPayFlow();
                    }

                } else {
                    MyCustomToast mct = new MyCustomToast(ActAstrologerDescription.this,
                            ActAstrologerDescription.this.getLayoutInflater(),
                            ActAstrologerDescription.this, regularTypeface);
                    mct.show(getResources().getString(R.string.order_fail));
                }
            }
        } else if (callback == CUtils.callBack.POST_RAZORPAYSTATUS) {
            /**
             * get the status of razor pay payment from our server
             */
            String postStatus = result;
            if (postStatus != null && postStatus.equalsIgnoreCase("1")) {
                if (payStatus != null && payStatus.equalsIgnoreCase("1")) {

                    CUtils.emailPDF(ActAstrologerDescription.this, CGlobalVariables.RAZORPAY_EMAIL_PDF, mediumTypeface, queue, order_Id, payStatus, astrologerServiceInfo);
                    Intent tppsIntent = new Intent(ActAstrologerDescription.this,
                            ActServicePaymentStatus.class);
                    tppsIntent.putExtra("Key", itemdetails);
                    tppsIntent.putExtra("Status", "success");
                    tppsIntent.putExtra("emailID", astrologerServiceInfo.getEmailID());
                    startActivity(tppsIntent);
                    onPurchaseCompleted(itemdetails, order_Id);
                    finish();
                } else {
                    openPaymentFailedDialog();
                    CUtils.googleAnalyticSendWitPlayServie(this,
                            CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                            CGlobalVariables.GOOGLE_ANALYTIC_SERVICE_RAZOR_PAYMENT_FAILED, null);
                    CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SERVICE_RAZOR_PAYMENT_FAILED, CGlobalVariables.FIREBASE_EVENT_PAYMENT_FAILED, "");

                }

            } else {
                openPaymentFailedDialog();
                CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SERVICE_RAZOR_PAYMENT_FAILED, CGlobalVariables.FIREBASE_EVENT_PAYMENT_FAILED, "");

            }
        }
    }

    /**
     * Callback method for getting the result of a chat API call.
     *
     * @param result        The result string array.
     * @param callback      The callback type.
     * @param priceInDollor The price in dollars.
     * @param priceInRs     The price in rupees.
     */
    @Override
    public void getCallBackForChat(String[] result, CUtils.callBack callback, String priceInDollor, String priceInRs) {
        //
    }

    private void updateModal(JSONObject obj) {
        try {

            String S_Status = obj.getString("S_Status");
            if (S_Status != null && !S_Status.isEmpty() && S_Status.equalsIgnoreCase("true")) {
                String priceRS = obj.getString("S_DiscountPriceInRs");
                String priceDl = obj.getString("S_DiscountPriceInDoller");
                itemdetails.setPriceInRS(priceRS);
                itemdetails.setPriceInDollor(priceDl);
                itemdetails.setCouponCode(etPromo.getText().toString().trim());
                updateUI(itemdetails, true);
            } else {
                String priceRS1 = obj.getString("S_PriceInRs");
                String priceDl1 = obj.getString("S_PriceInDoller");
                //itemdetails.setPriceInDollor(priceDl1);
                //itemdetails.setPriceInRS(priceRS1);
                itemdetails.setCouponCode("");
                llcoupanview.setVisibility(View.GONE);
                //  setViewItem(itemdetails);
                //     mct.show(getString(R.string.coupon_not_valid));
                edt_promo_layout.setError(getString(R.string.coupon_not_valid));
                edt_promo_layout.setErrorEnabled(true);
                //  requestFocus(name);
                etPromo.getBackground().setColorFilter(getResources().getColor(R.color.red1), PorterDuff.Mode.SRC_ATOP);
                Animation animShake = AnimationUtils.loadAnimation(getApplicationContext(),
                        R.anim.shake);
                etPromo.startAnimation(animShake);
                View current = getCurrentFocus();
                if (current != null)
                    current.clearFocus();
                CUtils.hideMyKeyboard(ActAstrologerDescription.this);

            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateUI(ServicelistModal itemDetails, boolean showCouponUI) {
        if (showCouponUI) {
            llPromo.setVisibility(View.GONE);
            mct.show(getString(R.string.coupon_applied));
            llcoupanview.setVisibility(VISIBLE);
            tvFinalprice.setText(getResources().getString(R.string.astroshop_dollar_sign) + roundFunction(Double.parseDouble(itemdetails.getPriceInDollor()), 2) + " / " + getResources().getString(R.string.astroshop_rupees_sign) + " " + roundFunction(Double.parseDouble(itemdetails.getPriceInRS()), 2));
            View current = getCurrentFocus();
            if (current != null)
                current.clearFocus();               // etPromo.clearFocus();
        }
        CUtils.hideMyKeyboard(ActAstrologerDescription.this);
    }

    /**
     * Used to set UserProfileData to UI and display the profile information.
     */
    private void setProfileForReport() {
        try {
            Calendar calendar = Calendar.getInstance();
            String profileName = "Select Profile";
            SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
            String dob = sdf.format(calendar.getTime());
            String dot = calendar.get(Calendar.HOUR) + ":" + calendar.get(Calendar.MINUTE) + ":" + calendar.get(Calendar.SECOND);
            String place = "Agra India";
            String gender = "M";
            if (userProfileData != null && !TextUtils.isEmpty(userProfileData.getName())) {
                try {
                    profileName = userProfileData.getName();
                    place = userProfileData.getPlace();
                    int birthMonth = Integer.parseInt(userProfileData.getMonth());
                    if (birthMonth < 1)
                        birthMonth = 1; // Default to January if month is invalid
                    else if (birthMonth > 12)
                        birthMonth = 12; // Default to December if month is invalid

                    dob = userProfileData.getDay() + "-" + monthNames[birthMonth - 1] + "-" + userProfileData.getYear();
                    dot = userProfileData.getHour() + ":" + userProfileData.getMinute() + ":" + userProfileData.getSecond();
                    gender = userProfileData.getGender();
                    setServiceInfo(userProfileData, payMode);
                } catch (Exception ignore) {
                }
            } else {
                BeanHoroPersonalInfo beanHoroPersonalInfo = (BeanHoroPersonalInfo) com.ojassoft.astrosage.utils.CUtils.getCustomObject(this);
                if (beanHoroPersonalInfo != null) {
                    try {
                        profileName = beanHoroPersonalInfo.getName();
                        int birthMonth = beanHoroPersonalInfo.getDateTime().getMonth();
                        if (birthMonth < 1)
                            birthMonth = 1; // Default to January if month is invalid
                        else if (birthMonth > 12)
                            birthMonth = 12; // Default to December if month is invalid
                        place = beanHoroPersonalInfo.getPlace().getCityName();
                        dob = beanHoroPersonalInfo.getDateTime().getDay() + "-" + monthNames[birthMonth - 1] + "-" + beanHoroPersonalInfo.getDateTime().getYear();
                        dot = beanHoroPersonalInfo.getDateTime().getHour() + ":" + beanHoroPersonalInfo.getDateTime().getMin() + ":" + beanHoroPersonalInfo.getDateTime().getSecond();
                        gender = beanHoroPersonalInfo.getGender();
                        setUserProfileData(beanHoroPersonalInfo);
                        setServiceInfo(userProfileData, payMode);
                    } catch (Exception ignored) {
                    }
                }
            }

            tvUserProfileName.setText(profileName);
            if (gender.equals("F") || gender.equalsIgnoreCase("Female")) {
                ivUserProfile.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.female_user));
            } else { // Default to male icon if gender is "M", "Male", or any other value
                ivUserProfile.setImageDrawable(ContextCompat.getDrawable(this, R.drawable.male_user));
            }
            tvUserProfileDetails.setText(dob + "," + dot + "," + place);
        } catch (Exception e) {
            //
        }
    }

    /**
     * This method is used to set the service information for the astrologer for purchase.
     *
     * @param userProfileData - UserProfileData object containing user profile data
     * @param payMode         - Payment mode (e.g., "Wallet", "RazorPay")
     */
    private void setServiceInfo(UserProfileData userProfileData, String payMode) {
        astrologerServiceInfo = new AstrologerServiceInfo();

        if (userProfileData != null) {
            astrologerServiceInfo.setKey(CUtils.getApplicationSignatureHashCode(this));
            astrologerServiceInfo.setPayMode(payMode);
            astrologerServiceInfo.setKphn("");
            astrologerServiceInfo.setProfileId("");
            astrologerServiceInfo.setMobileNo(com.ojassoft.astrosage.varta.utils.CUtils.getUserID(this));
            String gender = userProfileData.getGender();
            astrologerServiceInfo.setGender(gender);

            astrologerServiceInfo.setEmailID(getEmailFromPreference());
            editEmail.setText(getEmailFromPreference());
            String username = userProfileData.getName();
            astrologerServiceInfo.setRegName(username);


            astrologerServiceInfo.setDateOfBirth(String.valueOf(userProfileData.getDay()));
            astrologerServiceInfo.setMonthOfBirth(userProfileData.getMonth());
            astrologerServiceInfo.setYearOfBirth(String.valueOf(userProfileData.getYear()));
            astrologerServiceInfo.setHourOfBirth(String.valueOf(userProfileData.getHour()));
            astrologerServiceInfo.setMinOfBirth(String.valueOf(userProfileData.getMinute()));
            astrologerServiceInfo.setSecOfBirth(String.valueOf(userProfileData.getSecond()));
            astrologerServiceInfo.setDst("");

            astrologerServiceInfo.setPlace(userProfileData.getPlace());
            astrologerServiceInfo.setLongMinOfBirth(userProfileData.getLongmin());
            astrologerServiceInfo.setLongEWOfBirth(userProfileData.getLongew());
            astrologerServiceInfo.setLongDegOfBirth(userProfileData.getLongdeg());
            astrologerServiceInfo.setLatDegOfBirth(userProfileData.getLatdeg());
            astrologerServiceInfo.setLatMinOfBirth(userProfileData.getLatmin());
            astrologerServiceInfo.setLatNSOfBirth(userProfileData.getLatns());
            astrologerServiceInfo.setTimezone(userProfileData.getTimezone());
            astrologerServiceInfo.setMobileNo(com.ojassoft.astrosage.varta.utils.CUtils.getUserID(this));

           /* if (!userProfileData.getTimezone().isEmpty()) {
                astrologerServiceInfo.setTimezone(userProfileData.getTimezone());
            } else {
                astrologerServiceInfo.setTimezone(String.valueOf(place.getTimeZoneValue()));
            }
            float tz = CUtils.getAdjustedTimezone(astrologerServiceInfo.getDst(), astrologerServiceInfo.getTimezone());
            astrologerServiceInfo.setTimezone("" + tz);
            astrologerServiceInfo.setLatitude(place.getLatDeg() + place.getLatDir() + place.getLatMin());
            astrologerServiceInfo.setLongitude(place.getLongDeg() + place.getLongDir() + place.getLongMin());*/

            if (itemdetails != null) {
                astrologerServiceInfo.setPriceRs(itemdetails.getPriceInRS());
                astrologerServiceInfo.setPrice(itemdetails.getPriceInDollor());
                astrologerServiceInfo.setServiceId(itemdetails.getServiceId());
            } else
                astrologerServiceInfo.setPriceRs("");


        }
    }

    /**
     * This method is used to convert BeanHoroPersonalInfo to UserProfileData.
     *
     * @param beanHoroPersonalInfo - BeanHoroPersonalInfo object containing user profile data
     */
    private void setUserProfileData(BeanHoroPersonalInfo beanHoroPersonalInfo) {
        try {
            com.ojassoft.astrosage.beans.BeanDateTime beanDateTime = beanHoroPersonalInfo.getDateTime();
            com.ojassoft.astrosage.beans.BeanPlace beanPlace = beanHoroPersonalInfo.getPlace();

            userProfileData.setName(beanHoroPersonalInfo.getName());
            userProfileData.setUserPhoneNo(com.ojassoft.astrosage.varta.utils.CUtils.getUserID(this));

            userProfileData.setGender(beanHoroPersonalInfo.getGender());

            userProfileData.setMaritalStatus(com.ojassoft.astrosage.varta.utils.CGlobalVariables.NOT_SPECIFIED);

            userProfileData.setOccupation(com.ojassoft.astrosage.varta.utils.CGlobalVariables.NOT_SPECIFIED);

            if (beanDateTime != null) {
                userProfileData.setDay("" + beanDateTime.getDay());
                userProfileData.setMonth("" + (beanDateTime.getMonth() + 1));
                userProfileData.setYear("" + beanDateTime.getYear());
                userProfileData.setHour("" + beanDateTime.getHour());
                userProfileData.setMinute("" + beanDateTime.getMin());
                userProfileData.setSecond("" + beanDateTime.getSecond());
            }
            if (beanPlace != null) {
                String place = beanPlace.getCityName();
                if (beanPlace.getState() != null && !beanPlace.getState().trim().isEmpty()) {
                    if (!place.contains(",")) {
                        place = place + ", " + beanPlace.getState();
                    }
                }
                userProfileData.setPlace(place);
                userProfileData.setLatdeg(beanPlace.getLatDeg());
                userProfileData.setLongdeg(beanPlace.getLongDeg());
                userProfileData.setLongmin(beanPlace.getLongMin());
                userProfileData.setLatmin(beanPlace.getLatMin());
                userProfileData.setLongew(beanPlace.getLongDir());
                userProfileData.setLatns(beanPlace.getLatDir());
                userProfileData.setTimezone(beanPlace.getTimeZoneValue() + "");
            }

        } catch (Exception e) {
            Log.d("joinLiveAud", e.toString());
        }
    }

    /**
     * This method is used create spinner adapter instance for language selection.
     *
     * @param spinnerOptions - array of available languages
     * @return - ArrayAdapter for the spinner
     */
    private ArrayAdapter<CharSequence> getSpinnerAdapterLanguage(final String[] spinnerOptions) {
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this, R.layout.spinner_list_item, spinnerOptions) {

            public View getView(int position, View convertView, ViewGroup parent) {
                if (spinnerOptions != null && spinnerOptions.length == 1 && position == 1) {
                    position = 0;
                }
                View v = super.getView(position, convertView, parent);
                if (position == 0) {
                    ((TextView) v).setTextColor(ContextCompat.getColor(ActAstrologerDescription.this, R.color.hint_color));
                } else if (position == 2) {
                    ((TextView) v).setTypeface(CustomTypefaces.get(ActAstrologerDescription.this, "hi")); ///cmnt by neeraj 5/5/16
                } else {
                    ((TextView) v).setTypeface(robotMediumTypeface); //cmnt by neeraj 5/5/16
                }
                ((TextView) v).setTextSize(18);
                v.setPadding(5, 10, 10, 0);
                return v;
            }

            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);

                if (position == 2) {
                    ((TextView) v).setTypeface(CustomTypefaces.get(ActAstrologerDescription.this, "hi")); //cmnt by neeraj 5/5/16

                } else {
                    ((TextView) v).setTypeface(robotRegularTypeface); //cmnt by neeraj 5/5/16
                }
                v.setBackgroundColor(ContextCompat.getColor(ActAstrologerDescription.this, R.color.backgroundColor));
                ((TextView) v).setTextSize(18);
                parent.setBackgroundColor(ContextCompat.getColor(ActAstrologerDescription.this, R.color.backgroundColor));
                return v;
            }

        };
        languageOptions.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (languageOptions.getSelectedItemPosition() > 0) {
                    tvLangError.setVisibility(GONE);
                    tvLangError.setText("");
                    languageOptions.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(ActAstrologerDescription.this, R.color.black)));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        return adapter;
    }

    /**
     * this method is used to get the available languages for report
     *
     * @param langString - list of available languages
     * @return - array of available languages for Adapter
     */
    private String[] getLangForAvailableReport(String langString) {

        List<String> availableLang = new ArrayList<>();
        HashMap<String, String> langOption = new HashMap<String, String>();
        langOption.put("10", getResources().getString(R.string.select_lang));
        langOption.put("0", getResources().getString(R.string.english));
        langOption.put("1", getResources().getString(R.string.hindi));
        langOption.put("2", getResources().getString(R.string.tamil));
        langOption.put("4", getResources().getString(R.string.kannad));
        langOption.put("5", getResources().getString(R.string.telugu));
        langOption.put("6", getResources().getString(R.string.bangali));
        langOption.put("7", getResources().getString(R.string.gujarati));
        langOption.put("8", getResources().getString(R.string.malayalam));
        langOption.put("9", getResources().getString(R.string.marathi));
        langOption.put("13", getResources().getString(R.string.odia));
        langOption.put("14", getResources().getString(R.string.assamese));
        //international
//        langOption.put("16", getResources().getString(R.string.spanish));
//        langOption.put("17", getResources().getString(R.string.chinese));
//        langOption.put("18", getResources().getString(R.string.japanese));
//        langOption.put("19", getResources().getString(R.string.portugese));
//        langOption.put("20", getResources().getString(R.string.german));
//        langOption.put("21", getResources().getString(R.string.italian));
        if (langString != null && langString.length() > 0) {
            String[] langArray = langString.split(",");
            for (int i = 0; i < langArray.length; i++) {
                //This condition is for gujrati language:Amit Sharma
                if (langArray[i].equals("7") && android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.M) {
                    continue;
                }
                availableLang.add(langOption.get(langArray[i]));
            }
        } else {

            availableLang.add(langOption.get("0"));
            availableLang.add(langOption.get("1"));
        }
        availableLang.add(0, langOption.get("10"));
        String[] availableLangArray = availableLang.toArray(new String[availableLang.size()]);
        return availableLangArray;
    }

    /**
     * Initializes the checkbox with the wallet amount and sets visibility of related views.
     */
    private void initCheckBox() {
        String walletAmountString = com.ojassoft.astrosage.varta.utils.CUtils.getWalletRs(this);
        //Log.e("walletAmountString", "initCheckBox: " + walletAmountString);
        checkBoxUseWallet.setText(getString(R.string.wallet_amount, walletAmountString));
        try {
            double walletAmount = Double.parseDouble(walletAmountString);
            double reportPrice = Double.parseDouble(itemdetails.getPriceInRS());
            if (walletAmount >= reportPrice) {
                checkBoxUseWallet.setVisibility(VISIBLE);
                checkBoxUseWallet.setChecked(true);
            } else {
                checkBoxUseWallet.setVisibility(View.GONE);
            }
        } catch (NumberFormatException e) {
            checkBoxUseWallet.setVisibility(View.GONE);
        }
    }

    /**
     * This method is used to purchase report the via wallet API.
     *
     * @param info - astrologerServiceInfo
     */
    private void payViaWallet(AstrologerServiceInfo info) {
        if (!CUtils.isConnectedWithInternet(this)) {
            MyCustomToast mct = new MyCustomToast(ActAstrologerDescription.this, ActAstrologerDescription.this
                    .getLayoutInflater(), ActAstrologerDescription.this, typeface);
            mct.show(getResources().getString(R.string.no_internet));
        } else {
            showLoader();
            Call<ResponseBody> call = RetrofitClient
                    .getInstance().create(ApiList.class)
                    .purchaseReportFromWallet(getPurchaseFromWalletParams(info));
            CUtils.fcmAnalyticsEvents(CGlobalVariables.FIREBASE_EVENT_API_CALLED, CGlobalVariables.FIREBASE_EVENT_WALLTE_PAYMENT, "");
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    try {
                        if (response.isSuccessful()) {
                            String rBody = response.body().string();
                            Log.d(TAG, "PayWallet onResponse: " + rBody);
                            hideLoader();
                            JSONObject jsonObject = new JSONObject(rBody);
                            String status = jsonObject.optString("status");
                            if (!TextUtils.isEmpty(status)) {
                                if (status.equals("2")) {
                                    new InSufficientBalanceDialog("", com.ojassoft.astrosage.varta.utils.CUtils.getWalletRs(ActAstrologerDescription.this), "", "")
                                            .show(getSupportFragmentManager(), "InsufficientBalanceDialog");

                                } else if (status.equals("1") || status.equals("7") || status.equals("8") || status.equals("9") || status.equals("10")) {
                                    CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SERVICE_PAYMENT_SUCCESS, CGlobalVariables.FIREBASE_EVENT_REPORT_PURCHASE_WITH_WALLET, "");
                                    Intent tppsIntent = new Intent(ActAstrologerDescription.this,
                                            ActServicePaymentStatus.class);
                                    tppsIntent.putExtra("Key", itemdetails);
                                    tppsIntent.putExtra("Status", "success");
                                    tppsIntent.putExtra("emailID", astrologerServiceInfo.getEmailID());
                                    tppsIntent.putExtra("statusCode", status);
                                    startActivity(tppsIntent);
                                    finish();
                                } else {
                                    openPaymentFailedDialog();
                                    CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SERVICE_PAYMENT_FAILED, CGlobalVariables.FIREBASE_EVENT_WALLET_PAYMENT_FAILED, "");
                                }
                            }

                        }
                    } catch (Exception e) {
                        Log.d(TAG, "onResponse e: " + e);
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable throwable) {
                    Log.d(TAG, "onFailure: " + throwable);
                }
            });
        }
    }

    /**
     * This method is used to get the parameters for purchase from wallet.
     *
     * @param info - astrologerServiceInfo
     * @return - parameters Map<> for purchase from wallet
     */
    private Map<String, String> getPurchaseFromWalletParams(AstrologerServiceInfo info) {
        Map<String, String> headers = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        String year = String.valueOf(calendar.get(Calendar.YEAR));
        String month = String.valueOf(calendar.get(Calendar.MONTH) + 1);
        String day = String.valueOf(calendar.get(Calendar.DAY_OF_MONTH));
        String minute = String.valueOf(calendar.get(Calendar.MINUTE));
        String hour = String.valueOf(calendar.get(Calendar.HOUR_OF_DAY));

        SharedPreferences sharedPreferences = getSharedPreferences(
                CGlobalVariables.APP_PREFS_NAME, Context.MODE_PRIVATE);
        int chartStyle = sharedPreferences.getInt(CGlobalVariables.APP_PREFS_ChartStyle,
                CGlobalVariables.CHART_NORTH_STYLE);
        if (itemdetails.getServiceId().equals("143")) {
            chartStyle = chartStyle + 1;
        }
        //Log.d(TAG, "getPurchaseFromWalletParams: Chart type = " + chartStyle);
        int language = getSelectedLanguageCode();

        headers.put(KEY_API, CUtils.getApplicationSignatureHashCode(this));
        headers.put(COUNTRY_CODE, getCountryCode(this));
        headers.put(USER_PHONE_NO, getUserID(this));
        headers.put(USERID, com.ojassoft.astrosage.varta.utils.CUtils.getUserIdForBlock(this));
        headers.put("asuserid", com.ojassoft.astrosage.utils.CUtils.getUserName(this));
        headers.put(PACKAGE_NAME, BuildConfig.APPLICATION_ID);
        headers.put(APP_VERSION, BuildConfig.VERSION_NAME);
        headers.put(DEVICE_ID, CUtils.getMyAndroidId(this));
        headers.put("regName", info.getRegName() == null ? info.getEmailID().split("@")[0] : info.getRegName());
        headers.put(KEY_EMAIL_ID, CUtils.replaceEmailChar(info.getEmailID()));

        headers.put("gender", info.getGender() == null ? "M" : info.getGender());
        headers.put("monthOfBirth", info.getMonthOfBirth() == null ? month : info.getMonthOfBirth());
        headers.put("minOfBirth", info.getMinOfBirth() == null ? minute : info.getMinOfBirth());
        headers.put("dateOfBirth", info.getDateOfBirth() == null ? day : info.getDateOfBirth());
        headers.put("yearOfBirth", info.getYearOfBirth() == null ? year : info.getYearOfBirth());
        headers.put("hourOfBirth", info.getHourOfBirth() == null ? hour : info.getHourOfBirth());
        headers.put("place", info.getPlace() == null ? "Agra" : info.getPlace());
        headers.put("LongDegOfBirth", info.getLongDegOfBirth() == null ? "" : info.getLongDegOfBirth());
        headers.put("LongMinOfBirth", info.getLongMinOfBirth() == null ? "" : info.getLongMinOfBirth());
        headers.put("LongEWOfBirth", info.getLongEWOfBirth() == null ? "" : info.getLongEWOfBirth());
        headers.put("LatDegOfBirth", info.getLatDegOfBirth() == null ? "" : info.getLatDegOfBirth());
        headers.put("LatMinOfBirth", info.getLatMinOfBirth() == null ? "" : info.getLatMinOfBirth());
        headers.put("LatNSOfBirth", info.getLatNSOfBirth() == null ? "" : info.getLatNSOfBirth());
        headers.put("TimezoneOfBirth", info.getTimezone() == null ? "" : info.getTimezone());
        headers.put("ChartType", "" + chartStyle);

        headers.put("problem", info.getProblem());
        headers.put("serviceId", info.getServiceId());
        headers.put("profileId", info.getProfileId());
        headers.put("price", info.getPrice());
        headers.put("priceRs", info.getPriceRs());
        headers.put("mobileNo", info.getMobileNo());
        headers.put("email", info.getEmailID());
        headers.put("languageCode", "" + language);

        headers.put(KEY_AS_USER_ID, CUtils.replaceEmailChar(CUtils.getUserName(this)));
        headers.put("asplanid", String.valueOf(CUtils.getUserPurchasedPlanFromPreference(this)));
        headers.put("device_id", CUtils.getMyAndroidId(this));
        headers.put("purchasesource", CUtils.BRIHAT_KUNDALI_PURCHASE_SOURCE);
        Log.e(TAG, "getPurchaseFromWalletParams: "+headers);
        return headers;
    }

    private void openPaymentFailedDialog() {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        PaymentFailedDailogFrag pfdf = new PaymentFailedDailogFrag();
        ft.add(pfdf, null);
        ft.commitAllowingStateLoss();
    }


    /**
     * Initiates the RazorPay payment .
     */
    private void goToRazorPayFlow() {

        final Checkout co = new Checkout();
        co.setFullScreenDisable(true);
        CUtils.googleAnalyticSendWitPlayServie(ActAstrologerDescription.this,
                CGlobalVariables.GOOGLE_ANALYTIC_EVENT,
                CGlobalVariables.REPORT_PURCHASE_PAYMENT_INTITATE, null);

        try {
            JSONObject options = new JSONObject();
            options.put("name", "AstroSage");
            options.put("description", itemdetails.getTitle());
            //You can omit the image option to fetch the image from dashboard
            //    options.put("image", "http://astrosage.com/images/logo.png");
            options.put("currency", "INR");
            double amount = Double.parseDouble(astrologerServiceInfo.getPriceRs());
            //Need to send amount to razor pay in paise
            double paiseAmount = amount * 100;
            options.put("amount", paiseAmount);
            options.put("color", "#ff6f00");
            //options.put("order_id", razorpayOrderId);


            JSONObject preFill = new JSONObject();
            //preFill.put("email", etUseremail.getText().toString().trim());
            preFill.put("contact", com.ojassoft.astrosage.varta.utils.CUtils.getUserID(this));
            options.put("prefill", preFill);


            JSONObject notes = new JSONObject();
            //added by Ankit on 17-5-2019 for Razorpay Webhook
            notes.put("orderId", order_Id);
            notes.put("chatId", "");
            notes.put("orderType", CGlobalVariables.PAYMENT_TYPE_SERVICE);
            notes.put("appVersion", BuildConfig.VERSION_NAME);
            notes.put("appName", BuildConfig.APPLICATION_ID);
            notes.put("name", astrologerServiceInfo.getRegName());
            notes.put("firebaseinstanceid", com.ojassoft.astrosage.varta.utils.CUtils.getFirebaseAnalyticsAppInstanceId(this));
            notes.put("facebookinstanceid", com.ojassoft.astrosage.varta.utils.CUtils.getFacebookAnalyticsAppInstanceId(this));
            options.put("notes", notes);


            co.open(this, options);
        } catch (Exception e) {
            Toast.makeText(this, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    /**
     * This method is called when the payment is successful.
     *
     * @param razorpayPaymentID
     */
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        try {
            payStatus = "1";
            payId = razorpayPaymentID;

            double dPrice = 0.0;
            String price = "";
            try {
                if (astrologerServiceInfo != null) {
                    if (astrologerServiceInfo.getPriceRs() != null && astrologerServiceInfo.getPriceRs().length() > 0) {
                        price = astrologerServiceInfo.getPriceRs();
                        dPrice = Double.valueOf(price);
                    }
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            // in case of purchase event not added by server then add event
            if (!com.ojassoft.astrosage.utils.CUtils.isPurchaseEventAddedByServer(this)) {
                CUtils.googleAnalyticSendWitPlayServieForPurchased(this,
                        CGlobalVariables.GOOGLE_ANALYTIC_ASTROSHOP_SERVICE,
                        CGlobalVariables.GOOGLE_ANALYTIC_SERVICE_RAZOR_PAYMENT_SUCCESS, null, dPrice, "");
            }
            payStatus = "1";
            payId = razorpayPaymentID;
            /**
             * post razorpay payment status verify from server
             */
            CUtils.postRazorPayDetail(ActAstrologerDescription.this, mediumTypeface, queue, order_Id, payStatus, astrologerServiceInfo, razorpayPaymentID, "");

        } catch (Exception e) {
            //Log.e("ServicePay", "Exception in onPaymentSuccess", e);
        }
    }

    /**
     * This method is called when the payment fails.
     */
    @Override
    public void onPaymentError(int statusCode, String response) {
        try {
            String status = "Code-" + statusCode + " " + "Message-" + response;
            payStatus = "0";
            /**
             * post razorpay payment status to server
             */
            CUtils.saveBooleanData(this,CGlobalVariables.IS_SUBSCRIPTION_ERROR,true);
            CUtils.postRazorPayDetail(ActAstrologerDescription.this, mediumTypeface, queue, order_Id, payStatus, astrologerServiceInfo, "", status);
            CUtils.fcmAnalyticsEvents(CGlobalVariables.GOOGLE_ANALYTIC_SERVICE_PAYMENT_FAILED, CGlobalVariables.FIREBASE_EVENT_PAYMENT_FAILED, "");
        } catch (Exception e) {
            //Log.e("Service pay", "Exception in onPaymentError", e);
        }
    }

    /**
     * Returns the integer code for the selected language from the language spinner.
     *
     * @return The integer code for the selected language.
     */
    private int getSelectedLanguageCode() {
        int language = languageOptions.getSelectedItemPosition();

        if (availLangArray[language].equals(getResources().getString(R.string.english))) {
            language = CGlobalVariables.ENGLISH;
        } else if (availLangArray[language].equals(getResources().getString(R.string.bangali))) {
            language = CGlobalVariables.BANGALI;
        } else if (availLangArray[language].equals(getResources().getString(R.string.hindi))) {
            language = CGlobalVariables.HINDI;
        } else if (availLangArray[language].equals(getResources().getString(R.string.tamil))) {
            language = CGlobalVariables.TAMIL;
        } else if (availLangArray[language].equals(getResources().getString(R.string.telugu))) {
            language = CGlobalVariables.TELUGU;
        } else if (availLangArray[language].equals(getResources().getString(R.string.gujarati))) {
            language = CGlobalVariables.GUJARATI;
        } else if (availLangArray[language].equals(getResources().getString(R.string.kannad))) {
            language = CGlobalVariables.KANNADA;
        } else if (availLangArray[language].equals(getResources().getString(R.string.marathi))) {
            language = CGlobalVariables.MARATHI;
        } else if (availLangArray[language].equals(getResources().getString(R.string.malayalam))) {
            language = CGlobalVariables.MALAYALAM;
        }
        return language;
    }

    @Override
    public void onRetryClick() {
        if (astrologerServiceInfo != null) {
            if (astrologerServiceInfo.getPayMode().equalsIgnoreCase("Wallet")) {
                payViaWallet(astrologerServiceInfo);
            } else {
                CUtils.getOrderID(ActAstrologerDescription.this, mediumTypeface, queue, astrologerServiceInfo);
            }
        }

    }

    /**
     * Loads BrihatKundli details from the server for collection edition.
     */
    private void loadBigHorscopeData() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, CGlobalVariables.brihatHorscopeWebURl,
                response -> {
                    if (response != null && !response.isEmpty()) {
                        try {
                            String str = new String(response.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                            JSONArray jsonArray = new JSONArray(str);

                            JSONObject objProduct = jsonArray.getJSONObject(1);
                            parseGsonDataForProduct(objProduct.toString());
                        } catch (Exception e) {
                            MyCustomToast mct = new MyCustomToast(ActAstrologerDescription.this, ActAstrologerDescription.this.getLayoutInflater(), this, regularTypeface);
                            mct.show(getResources().getString(R.string.something_wrong_error));
                            finish();
                            e.printStackTrace();
                        }
                    }
                }, error -> {
            Log.e(TAG, "Volley error: ", error); // Log error for debugging

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
            finish();
        }) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }

            /**
             * Passing some request headers
             */
            @Override
            public Map<String, String> getParams() {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("key", CUtils.getApplicationSignatureHashCode(ActAstrologerDescription.this));
                headers.put("languagecode", "" + LANGUAGE_CODE);
                headers.put(KEY_AS_USER_ID, CUtils.replaceEmailChar(CUtils.getUserName(ActAstrologerDescription.this)));
                headers.put("asuserplanid", String.valueOf(CUtils.getUserPurchasedPlanFromPreference(ActAstrologerDescription.this)));
                return headers;
            }

        };
        int socketTimeout = 30000; // 30 seconds
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        stringRequest.setShouldCache(true);
        queue.add(stringRequest);
    }

    /**
     * Parses product data JSON using Gson and updates deep link URL.
     */
    private void parseGsonDataForProduct(String productData) {
        BigHorscopeProductModel data;
        Gson gson = new Gson();
        data = gson.fromJson(productData, BigHorscopeProductModel.class);
        if (data != null) {
            collectorsEditionDeepLink = data.getDeepLinkUrl();
            collectorsEditionPrice = data.getPriceInRS();

            String text = getString(R.string.collector_s_edition_price, collectorsEditionPrice);
            SpannableString spannableString = new SpannableString(text);
            spannableString.setSpan(new UnderlineSpan(), 0, text.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            tvCollEdi.setText(spannableString);
            String buyText = getResources().getString(R.string.buy_now_pdf, itemdetails.getPriceInRS());
            buy_now.setText(buyText);
            tvCollEdi.setOnClickListener((v) -> CUtils.getUrlLink(collectorsEditionDeepLink, this, LANGUAGE_CODE, 0));

        } else {
            MyCustomToast mct = new MyCustomToast(this, this.getLayoutInflater(), this, regularTypeface);
            mct.show("Data is not available");
        }
    }

    /**
     * Shows the loader.
     */
    private void showLoader() {
        try {
            if (pd == null) {
                pd = new CustomProgressDialog(this);
                pd.setCancelable(false);
            }
            if (!pd.isShowing()) {
                pd.show();
            }
        } catch (Exception e) {
            //
        }
    }

    /**
     * Hides the loader.
     */
    private void hideLoader() {
        try {
            if (pd != null && pd.isShowing()) {
                pd.dismiss();
            }
        } catch (Exception e) {
            //
        }
    }

    private boolean validateField() {
        if (!CUtils.isValidEmail(editEmail.getText().toString().trim())) {
            tvEmailError.setVisibility(VISIBLE);
            tvEmailError.setText(R.string.please_enter_valid_email);
            editEmail.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red1)));
            scrollView.post(() -> scrollView.scrollTo(0, scrollView.getBottom()));
            return false;
        } else if (languageOptions.getSelectedItemPosition() < 1) {
            tvLangError.setVisibility(VISIBLE);
            tvLangError.setText(R.string.please_select_language_for_report);
            languageOptions.setBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(this, R.color.red1)));
            scrollView.post(() -> {
                scrollView.scrollTo(0, scrollView.getBottom());
            });
            return false;
        }
        return true;
    }

    public void onPurchaseCompleted(ServicelistModal itemdetails, String order_id) {
        CUtils.trackEcommerceProduct(this, itemdetails.getServiceId(), itemdetails.getTitle(), itemdetails.getPriceInRS(), order_id, "INR", CGlobalVariables.Ecommerce_Paytm_Purchase, "In-App Store", "0", CGlobalVariables.TOPIC_ASKAQUESTION);
    }

    private String getEmailFromPreference() {
        String astrosageId = com.ojassoft.astrosage.utils.CUtils.getUserName(this);
        String email = "";
        if (astrosageId.contains("@")) {
            email = astrosageId;
        } else {
            email = UserEmailFetcher.getEmail(this);
        }
        return email;
    }

    private void getWalletPriceData() {
        if (com.ojassoft.astrosage.varta.utils.CUtils.isConnectedWithInternet(ActAstrologerDescription.this)) {
            HashMap<String, String> headers = new HashMap<String, String>();
            headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.APP_KEY, com.ojassoft.astrosage.varta.utils.CUtils.getApplicationSignatureHashCode(this));
            headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.USER_PHONE_NO, com.ojassoft.astrosage.varta.utils.CUtils.getUserID(this));
            headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.COUNTRY_CODE, com.ojassoft.astrosage.varta.utils.CUtils.getCountryCode(this));
            headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.LANG, com.ojassoft.astrosage.varta.utils.CUtils.getLanguageKey(LANGUAGE_CODE));
            headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.DEVICE_ID, com.ojassoft.astrosage.varta.utils.CUtils.getMyAndroidId(this));
            headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.APP_VERSION, BuildConfig.VERSION_NAME);
            headers.put(com.ojassoft.astrosage.varta.utils.CGlobalVariables.PACKAGE_NAME, com.ojassoft.astrosage.varta.utils.CUtils.getAppPackageName(this));
            ApiList api = RetrofitClient.getInstance().create(ApiList.class);
            Call<ResponseBody> call = api.getWalletBalance(headers);
            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.body() != null) {//{"userphone":"7838624890", "userbalancce":"120.25"}
                        try {
                            String myResponse = response.body().string();
                            JSONObject object = new JSONObject(myResponse);
                            String walletBal = object.getString("userbalancce");
                            if (walletBal != null && walletBal.length() > 0) {
                                com.ojassoft.astrosage.varta.utils.CUtils.setWalletRs(ActAstrologerDescription.this, walletBal);
                                initCheckBox();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {

                }
            });
        }
    }

    private String getProblemText(){
        String problem = "";
        String appVerName = LibCUtils.getApplicationVersionToShow(this);
        if (itemdetails != null && itemdetails.getServiceId() != null && itemdetails.getServiceId().equalsIgnoreCase("114") ||
                itemdetails.getServiceId().equalsIgnoreCase("148")
                || (itemdetails.getIsShowProblem() != null && itemdetails.getIsShowProblem().equalsIgnoreCase("0"))) {

            int language = languageOptions.getSelectedItemPosition();
            if (availLangArray[language].equals(getResources().getString(R.string.english))) {
                language = CGlobalVariables.ENGLISH;
            } else if (availLangArray[language].equals(getResources().getString(R.string.bangali))) {
                language = CGlobalVariables.BANGALI;
            } else if (availLangArray[language].equals(getResources().getString(R.string.hindi))) {
                language = CGlobalVariables.HINDI;
            } else if (availLangArray[language].equals(getResources().getString(R.string.tamil))) {
                language = CGlobalVariables.TAMIL;
            } else if (availLangArray[language].equals(getResources().getString(R.string.telugu))) {
                language = CGlobalVariables.TELUGU;
            } else if (availLangArray[language].equals(getResources().getString(R.string.gujarati))) {
                language = CGlobalVariables.GUJARATI;
            } else if (availLangArray[language].equals(getResources().getString(R.string.kannad))) {
                language = CGlobalVariables.KANNADA;
            } else if (availLangArray[language].equals(getResources().getString(R.string.marathi))) {
                language = CGlobalVariables.MARATHI;
            } else if (availLangArray[language].equals(getResources().getString(R.string.malayalam))) {
                language = CGlobalVariables.MALAYALAM;
            }
            problem = itemdetails.getTitle() + "-" + CUtils.getLanguageKey(language) + " " + "## " + "(lang: " + CUtils.getLanguageKey(language) + ", " + "appversion: " + appVerName + ")";

        } else {
            problem = astrologerServiceInfo.getProblem() + " " + "## " + "(lang: " + CUtils.getLanguageKey(LANGUAGE_CODE) + ", " + "appversion: " + appVerName + ")";
        }
        return problem;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ReportsFragment.OPEN_FROM_KUNDLI = false; // Reset the flag when the activity is destroyed
    }
}
