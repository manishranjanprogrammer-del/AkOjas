package com.ojassoft.astrosage.ui.act;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import com.google.android.material.tabs.TabLayout;
import androidx.fragment.app.FragmentManager;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.NetworkImageView;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AstroShopItemDetails;
import com.ojassoft.astrosage.controller.ControllerManager;
import com.ojassoft.astrosage.customexceptions.UIDataOperationException;
import com.ojassoft.astrosage.jinterface.IAstroOffersData;
import com.ojassoft.astrosage.misc.AstroShopDataDownloadService;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.misc.SliderAdapter;
import com.ojassoft.astrosage.ui.customcontrols.HeightWrappingViewPager;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.ui.fragments.FullImageFragment;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

////import com.google.analytics.tracking.android.Log;

/**
 * Created by ojas on १/३/१६.
 */
public class AstroShopItemDescription extends BaseInputActivity implements View.OnClickListener, IAstroOffersData {
    private TextView tvTitle;
    private Toolbar tool_barAppModule;
    private NetworkImageView image_url;
    private TextView item_name, text__title_label, text__title_label_price, text__title_description_price, text__title_label_you_save, text__title_description_price_you_save;
    private TextView item_des, txt_disscount, tvOffer, text_DescriptioLbl, text_Description, msgForBasicPlanText, msgForBasicPlanPrice, unlockPlanText;
    private TextView item_cost;
    private TabLayout tabLayout;
    private ImageView imgicmore, imgshopingcart,ic_share;
    private Button buy_now, add__cart;
    private RelativeLayout rlcart;
    private TextView txcartCount;
    private AstroShopItemDetails itemdetails;
    private AstroShopItemDetails deepLinkItem;
    private SliderAdapter adapter;
    private ArrayList<String> largeImageList;
    private int defaultCheckId = 0;
    TextView tvPlus, tvMinus;

    Typeface typeface;
    private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    Activity activity;
    String title;
    private BroadcastReceiver receiver;
    private String referdata;
    String mainData;
    private ArrayList<AstroShopItemDetails> allData = new ArrayList<>();
    ArrayList<String> categoryFullName;
    ArrayList<String> categoryShortName;
    private String kdetail = "";
    private final int REQUEST_KEY_SELECT_PROFILE = 1109;
    private final int REQUEST_KEY_SELECT_PROFILE_CART = 1110;
    private RequestQueue queue;
    private HeightWrappingViewPager viewpager;
    private TabLayout dots;
    private TextView filterText,text_discount_plan;
    private RadioGroup radioGroup;
    private ArrayList<AstroShopItemDetails> groupList;
    private LinearLayout filterView,basicPlanUserLayout;
    private MyCustomToast mct;
    private ProgressBar pBar;
    LinearLayout llOffer;

    /*
        private ArrayList<String> urls;
    */
    public AstroShopItemDescription() {
        super(R.string.app_name);
    }

    private static CustomProgressDialog pd = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = this;
        LANGUAGE_CODE = ((AstrosageKundliApplication) AstroShopItemDescription.this.getApplication())
                .getLanguageCode();

        typeface = CUtils.getRobotoFont(
                AstroShopItemDescription.this.getApplicationContext(), LANGUAGE_CODE, CGlobalVariables.regular);
        setContentView(R.layout.lay_astroshop_item_description);
        //setContentView(R.layout.a_description);

        tool_barAppModule = (Toolbar) findViewById(R.id.tool_barAppModule);
        llOffer = findViewById(R.id.llOffer);
        tool_barAppModule.setNavigationIcon(R.drawable.ic_back_arrow);
        //    itemdetails = new AstroShopItemDetails();
        setSupportActionBar(tool_barAppModule);
        Bundle bundle = getIntent().getExtras();

        groupList=(ArrayList<AstroShopItemDetails>) bundle.getSerializable("key");
        if(bundle.getSerializable("Item")!=null)
            deepLinkItem=(AstroShopItemDetails) bundle.getSerializable("Item");
        //   itemdetails = getDefaultItem(list);
        pd = new CustomProgressDialog(this, typeface);
        queue = VolleySingleton.getInstance(this).getRequestQueue();

        if (groupList.get(0).getKdetail() != null && !groupList.get(0).getKdetail().isEmpty()) {
            kdetail = groupList.get(0).getKdetail();
        }

        setdefaultProduct();

        //Log.e("tag", "fulldata==" + itemdetails.toString());


        //  astroShopData.getItemCost();
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        filterView = (LinearLayout) findViewById(R.id.filterView);
  /*int maxLength = 20;
        InputFilter[] fArray = new InputFilter[1];
        fArray[0] = new InputFilter.LengthFilter(maxLength);
        tvTitle.setFilters(fArray);*/
        //   tvTitle.setTypeface(typeface);
        text_discount_plan = (TextView) findViewById(R.id.text_discount_plan);
        msgForBasicPlanText = (TextView)findViewById(R.id.msg_for_basic_plan_text);
        msgForBasicPlanPrice = (TextView)findViewById(R.id.msg_for_basic_plan_price);
        unlockPlanText = (TextView)findViewById(R.id.unlock_plan_text);
        basicPlanUserLayout = (LinearLayout) findViewById(R.id.basic_plan_user_layout);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        filterText = (TextView) findViewById(R.id.filtertext);
        filterText.setTypeface(((BaseInputActivity) this).robotMediumTypeface, Typeface.BOLD);

        radioGroup = (RadioGroup) findViewById(R.id.radiogroup);
        radioGroup.setOrientation(LinearLayout.HORIZONTAL);


        tabLayout.setVisibility(View.GONE);
        image_url = (NetworkImageView) findViewById(R.id.image_view);
        item_name = (TextView) findViewById(R.id.text_title);
        //  item_name.setTypeface(((BaseInputActivity) this).robotRegularTypeface);
        item_name.setTypeface(((BaseInputActivity) this).robotMediumTypeface, Typeface.BOLD);
        text__title_label = (TextView) findViewById(R.id.text__title_label);
        text__title_label_price = (TextView) findViewById(R.id.text__title_label_price);
        tvOffer = (TextView) findViewById(R.id.tvOffer);
        text__title_description_price = (TextView) findViewById(R.id.text__title_description_price);
        text__title_label_you_save = (TextView) findViewById(R.id.text__title_label_you_save);
        text__title_description_price_you_save = (TextView) findViewById(R.id.text__title_description_price_you_save);
        txt_disscount = (TextView) findViewById(R.id.txt_disscount);
        text__title_description_price_you_save.setTypeface(((BaseInputActivity) this).robotMediumTypeface);
        text__title_description_price.setTypeface(((BaseInputActivity) this).robotMediumTypeface);

        tvPlus=(TextView) findViewById(R.id.plus);
        tvPlus.setTypeface(((BaseInputActivity) this).robotMediumTypeface, Typeface.BOLD);


        tvMinus=(TextView) findViewById(R.id.minus);
        tvMinus.setTypeface(this.robotMediumTypeface, Typeface.BOLD);

        // filterText.setTypeface(((BaseInputActivity) this).robotMediumTypeface, Typeface.BOLD);


        text__title_label_price.setTypeface(this.robotMediumTypeface,Typeface.BOLD);
        text__title_label.setTypeface(this.robotMediumTypeface, Typeface.BOLD);
        text__title_label_you_save.setTypeface(this.robotMediumTypeface, Typeface.BOLD);

        text_DescriptioLbl = (TextView) findViewById(R.id.text_DescriptioLbl);
        text_DescriptioLbl.setTypeface(this.robotMediumTypeface, Typeface.BOLD);

        text_Description = (TextView) findViewById(R.id.text_Description);
        text_Description.setTypeface(this.robotRegularTypeface);

        //    item_name.setTypeface(typeface);
        item_des = (TextView) findViewById(R.id.text_sub_title);
        item_des.setTypeface(this.robotRegularTypeface);
        //     item_des.setTypeface(typeface);
        item_cost = (TextView) findViewById(R.id.text__title_description);
        item_cost.setTypeface(this.robotMediumTypeface);
        //      item_cost.setTypeface(typeface);
        imgicmore = (ImageView) findViewById(R.id.imgMoreItem);
        buy_now = (Button) findViewById(R.id.buy_now);
        add__cart = (Button) findViewById(R.id.add__cart);

        buy_now.setTypeface(this.regularTypeface);
        add__cart.setTypeface(this.regularTypeface);
        imgshopingcart = (ImageView) findViewById(R.id.imgshopingcart);
        rlcart = (RelativeLayout) findViewById(R.id.rlcart);
        txcartCount = (TextView) findViewById(R.id.txtcartCount);
        rlcart.setVisibility(View.VISIBLE);
        pBar=(ProgressBar) findViewById(R.id.pBar);

        ic_share=(ImageView) findViewById(R.id.share);
      //  ic_share=(ImageView) tool_barAppModule.findViewById(R.id.share);

        ic_share.setVisibility(View.VISIBLE);
        imgicmore.setVisibility(View.GONE);
        imgshopingcart.setOnClickListener(this);
        imgicmore.setOnClickListener(this);
        buy_now.setOnClickListener(this);
        add__cart.setOnClickListener(this);
        image_url.setOnClickListener(this);
        viewpager = (HeightWrappingViewPager) findViewById(R.id.sliderPager);
        viewpager.setOnClickListener(this);
        tvMinus.setOnClickListener(this);
        tvPlus.setOnClickListener(this);
        ic_share.setOnClickListener(this);
        basicPlanUserLayout.setOnClickListener(this);
        dots = (TabLayout) findViewById(R.id.dots);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mct = new MyCustomToast(this,
                this.getLayoutInflater(), this, regularTypeface);
        /* urls=new ArrayList<>();
        urls.add("http://www.appinventor.org/assets/img/android_peekaboo.png");
        urls.add("https://thumb1.shutterstock.com/display_pic_with_logo/3208709/520427839/stock-vector-blue-friendly-android-robot-character-with-two-antennas-vector-cartoon-illustration-520427839.jpg");
        urls.add("http://ndl.mgccw.com/mu3/app/20140314/07/1394754882016/sss/4_small.png");
*/


        //  setViewItem(itemdetails);

        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                AstroShopItemDetails item = new AstroShopItemDetails("" + i);
                int index = groupList.indexOf(item);
                AstroShopItemDetails newItem = groupList.get(index);
                itemdetails = newItem;
                setViewItem(itemdetails, true,index);
                getFullDescription(index);
            }
        });
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                referdata = "0";//intent.getStringExtra(AstroShopDataDownloadService.COPA_MESSAGE);

                // //Log.e("tag","DTAGEYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYYY" + referdata.size());
                if (referdata.equals("1")) {
                    // Cache cache = VolleySingleton.getInstance(AstroShopItemDescription.this).getRequestQueue().getCache();
                    //Cache.Entry entry = cache.get(CGlobalVariables.astroShopItemsLive);
                    //if (entry != null) {
                    String entry = CUtils.getStringData(AstroShopItemDescription.this, CGlobalVariables.Astroshop_Data + String.valueOf(LANGUAGE_CODE), "");
                    if (entry != null && !entry.isEmpty()) {
                        try {
                            String saveData = entry;//new String(entry.data, "UTF-8");
                            mainData = saveData;
                            allData.clear();
                            parseAstroShopData();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        };

        queue = VolleySingleton.getInstance(AstroShopItemDescription.this).getRequestQueue();
        CUtils.fetchProductsOffer(AstroShopItemDescription.this, queue, "2", String.valueOf(LANGUAGE_CODE));
        setDataofProduct();
        setCartItems();

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
    protected void onResume() {

        super.onResume();
        String title = "";
        title = getItemName();

        CUtils.googleAnalyticSendWitPlayServieForAstroshop(activity,
                CGlobalVariables.GOOGLE_ANALYTIC_EVENT_FOR_ASTROSHOP_VIEW,
                title, null);
        //Log.e("tag", "GOOGLE_ANALYTIC_EVENT_FOR_ASTROSHOP_VIEW" + title);
        setCartItems();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                CUtils.hideMyKeyboard(AstroShopItemDescription.this);
            }
        },500);
    }


    private String getItemName() {
        if (itemdetails != null) {

            title = itemdetails.getPName();
        }
        return title;

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {


            case R.id.image_view:
                FragmentManager fm = getSupportFragmentManager();
                if (image_url.getDrawable() != null) {
                    Bitmap bitmap = ((BitmapDrawable) image_url.getDrawable()).getBitmap();
                    FullImageFragment obj = new FullImageFragment();
                    obj.newInstance(bitmap);
                    Bundle b = new Bundle();
                    b.putParcelable("bitmap", bitmap);
                    obj.setArguments(b);
                    obj.show(fm, "show");
                }


                break;

            case R.id.rlcart:
            case R.id.imgshopingcart:
            case R.id.txtcartCount:
                if (CUtils.getCartProducts(this).size() > 0) {
                    Intent addtocart1 = new Intent(AstroShopItemDescription.this, AstroShopShopingCartAct.class);
                    startActivity(addtocart1);
                }
                break;

            case R.id.share:
                try {
                    String urlToShare=itemdetails.getpDeepLinkUrl();
                    //   String urlToShare="https://buy.astrosage.com/rudraksha/gauri-shankar-rudraksha";


                    if (urlToShare != null && !urlToShare.isEmpty()) {
                        Intent intent = new Intent();
                        intent.setAction(Intent.ACTION_SEND);
                        intent.setType("text/plain");
                        intent.putExtra(Intent.EXTRA_TEXT, itemdetails.getPName()+" - "+urlToShare);
                        intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.astro_produ_share));
                        startActivity(Intent.createChooser(intent, "Share"));
                    } else {
                        Toast.makeText(this, "Bad url", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {

                }
                break;


            case R.id.imgMoreItem:


                break;
            case R.id.buy_now:
                String title = "";
                title = getItemName();
                CUtils.googleAnalyticSendWitPlayServieForAstroshop(activity,
                        CGlobalVariables.GOOGLE_ANALYTIC_EVENT_FOR_ASTROSHOP_BUY,
                        title, null);

                if(!itemdetails.getP_OutOfStock().equalsIgnoreCase("true")) {
                    if ((itemdetails.getPId().equalsIgnoreCase("169") || itemdetails.getPId().equalsIgnoreCase("151") ||
                            itemdetails.getIsShowProfile().equalsIgnoreCase("1")) && ((itemdetails.getKdetail() == null || itemdetails.getKdetail().isEmpty()))) {
                        callToGetNewBirthDetails(isLocalKundliAvailable(), REQUEST_KEY_SELECT_PROFILE);

                    } else {

                        proceedSingleProduct();

                    }
                }
                else
                {
                    mct.show(getResources().getString(R.string.out_stock));

                }
                //Log.e("tag", "GOOGLE_ANALYTIC_EVENT_FOR_ASTROSHOP_BUY" + title);
                break;
            case R.id.btn_check:

                break;
            case R.id.add__cart:
                if(itemdetails.getP_OutOfStock().equalsIgnoreCase("true"))
                {
                    mct.show(getResources().getString(R.string.out_stock));
                }
                else
                {
                    proceedForCart();
                }

                /*  if(itemdetails.getPId().equalsIgnoreCase("169") ||itemdetails.getPId().equalsIgnoreCase("151") )
                {
                    callToGetNewBirthDetails(isLocalKundliAvailable(),REQUEST_KEY_SELECT_PROFILE_CART);
                }
                else {
                    proceedForCart();
                }*/
                break;

            case R.id.plus:
                tvPlus.setVisibility(View.GONE);
                tvMinus.setVisibility(View.VISIBLE);
                text_Description.setMaxLines(Integer.MAX_VALUE);
                break;
            case R.id.minus:

                tvMinus.setVisibility(View.GONE);
                tvPlus.setVisibility(View.VISIBLE);
                text_Description.setMaxLines(3);

                break;

            case R.id.basic_plan_user_layout:
                CUtils.gotoProductPlanListUpdated(AstroShopItemDescription.this,
                        LANGUAGE_CODE, SUB_ACTIVITY_UPGRADE_PLAN_UPDTAE, CGlobalVariables.SCREEN_ID_DHRUV,"product_description");
                break;
            default:
                break;
        }
    }

    /***
     * method to set the view once all data is availbale
     * @param itemdetails
     * @param isSetAdapter   to tell whether to reload adapter or not
     * @param setselected
     */
    private void setViewItem(AstroShopItemDetails itemdetails, Boolean isSetAdapter,int setselected) {
        String[] separated = null;
        itemdetails.setKdetail(kdetail);
        if (itemdetails != null) {
            if (itemdetails.getPName() != null && itemdetails.getPName().contains("(")) {
                separated = itemdetails.getPName().split("\\(");
                String str = separated[0].trim();
                if (str.length() >= 20) {
                    str = itemdetails.getPName().substring(0, 18).concat("...");
                }
                tvTitle.setText(str);
            } else {
                String str = itemdetails.getPName();
                if (str.length() >= 20) {
                    str = itemdetails.getPName().substring(0, 18).concat("...");
                }
                tvTitle.setText(str);
            }

            item_name.setText(itemdetails.getPName());
            item_des.setText(itemdetails.getPSmallDesc());
            item_cost.setText(getResources().getString(R.string.astroshop_dollar_sign).trim() + roundFunction(Double.parseDouble(itemdetails.getPPriceInDoller()), 2) + " / " + getResources().getString(R.string.astroshop_rupees_sign) + " " + roundFunction(Double.parseDouble(itemdetails.getPPriceInRs()), 2));

            if ((itemdetails.getP_OriginalPriceInDollar() == null) || (itemdetails.getP_OriginalPriceInRs() == null) || (itemdetails.getP_OriginalPriceInRs().isEmpty()) || itemdetails.getPPriceInRs().equalsIgnoreCase(itemdetails.getP_OriginalPriceInRs())) {
                txt_disscount.setVisibility(View.GONE);
                text__title_description_price.setVisibility(View.GONE);
                text__title_description_price_you_save.setVisibility(View.GONE);
                text__title_label_you_save.setVisibility(View.GONE);
                text__title_label_price.setVisibility(View.GONE);
                text__title_label.setText(getResources().getString(R.string.astroshop_price));
                item_cost.setTextColor(getResources().getColor(R.color.black));

            } else {

                String discoumt = "";
                if (itemdetails.getP_SavePercentOfRs().contains(".")) {
                    String str[] = itemdetails.getP_SavePercentOfRs().split("\\.");
                    discoumt = str[0];
                } else {
                    discoumt = itemdetails.getP_SavePercentOfRs();
                }
                txt_disscount.setVisibility(View.VISIBLE);
                txt_disscount.setText(discoumt.trim() + "%" + "\n" + getString(R.string.off));
                //text__title_description_price.setText(getResources().getString(R.string.astroshop_dollar_sign).trim() + roundFunction(Double.parseDouble(itemdetails.getP_OriginalPriceInDollar()), 2) + " / " + getResources().getString(R.string.astroshop_rupees_sign) + " " + roundFunction(Double.parseDouble(itemdetails.getP_OriginalPriceInRs()), 2));
                //text__title_description_price.setPaintFlags(text__title_description_price.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                text__title_description_price_you_save.setText(getResources().getString(R.string.astroshop_dollar_sign).trim() + roundFunction(Double.parseDouble(itemdetails.getP_SaveAmountInDollar()), 2) + " / " + getResources().getString(R.string.astroshop_rupees_sign) + " " + roundFunction(Double.parseDouble(itemdetails.getP_SaveAmountInRs()), 2) + " " + "(" + discoumt.trim() + "%" + ")");

                com.ojassoft.astrosage.varta.utils.CUtils.setStrikeOnTextView(text__title_description_price, getResources().getString(R.string.astroshop_dollar_sign).trim() + roundFunction(Double.parseDouble(itemdetails.getP_OriginalPriceInDollar()), 2) + " / " + getResources().getString(R.string.astroshop_rupees_sign) + " " + roundFunction(Double.parseDouble(itemdetails.getP_OriginalPriceInRs()), 2) );

            }


            CUtils.showServiceProductDiscountedText(AstroShopItemDescription.this, text_discount_plan,
                    itemdetails.getMessageOfCloudPlan1(), itemdetails.getMessageOfCloudPlan2(), CGlobalVariables.FROM_PRODUCT_TEXT);

            CUtils.showBasicPlanUserText(AstroShopItemDescription.this,msgForBasicPlanText,basicPlanUserLayout,
                    itemdetails.getMessageOfCloudPlan1(),itemdetails.getMessageOfCloudPlan2());


            String lineSep = System.getProperty("line.separator");

            //     //Log.e("LARGEIMAGE" + itemdetails.getPId() + itemdetails.getPImgUrl() + itemdetails.getPLargeImgUrl());
            // image_url.setImageUrl(itemdetails.getPLargeImgUrl(), VolleySingleton.getInstance(AstroShopItemDescription.this).getImageLoader());
            //   text_Description.setText(Html.fromHtml(itemdetails.getPFullDesc()));
          /*  if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                text_Description.setText(Html.fromHtml(itemdetails.getPFullDesc(), Html.FROM_HTML_MODE_LEGACY));
            } else {
                boolean contains1 = itemdetails.getPFullDesc().contains("&#10;&#10;");
                boolean contains2 = itemdetails.getPFullDesc().contains("<br />");
                String html = itemdetails.getPFullDesc();
                if (itemdetails.getPFullDesc().contains("&#10;&#10;")) {
                    html = itemdetails.getPFullDesc().replaceAll("&#10;&#10;", "<br />");

                }
                text_Description.setText(Html.fromHtml(html));
            }*/

            // itemdetails.setLargeImageList(largeImageList);
            if(isSetAdapter) {
                adapter = new SliderAdapter(AstroShopItemDescription.this, itemdetails);
                viewpager.setAdapter(adapter);
                dots.setupWithViewPager(viewpager, true);
                RadioButton btn = (RadioButton) radioGroup.getChildAt(setselected);
                btn.setChecked(true);
            }
            if (itemdetails.getLargeImageList().size() <= 1) {
                dots.setVisibility(View.GONE);
            }
            else
            {
                dots.setVisibility(View.VISIBLE);

            }


        }

    }

    /**
     * Round the double to given places
     * @param value
     * @param places
     * @return
     */
    private double roundFunction(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }


    private void setCartItems() {
        ArrayList<AstroShopItemDetails> list = CUtils.getCartProducts(this);
        if (list.size() > 0) {
            txcartCount.setVisibility(View.VISIBLE);
            txcartCount.setText(String.valueOf(list.size()));
        } else {
            txcartCount.setVisibility(View.GONE);

        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        //EasyTracker.getInstance().activityStart(this);
        LocalBroadcastManager.getInstance(this).registerReceiver((receiver),
                new IntentFilter(AstroShopDataDownloadService.COPA_RESULT)
        );
    }

    @Override
    protected void onStop() {
        super.onStop();
        //EasyTracker.getInstance().activityStop(this);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }


    private void parseAstroShopData() {
        categoryFullName = new ArrayList<>();
        categoryShortName = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(mainData);
            JSONObject productCategoryNames = jsonArray.getJSONObject(0);
            JSONArray jsonArrayProductCategoryName = productCategoryNames.optJSONArray("ProductsCategoryName");
            for (int i = 0; i < jsonArrayProductCategoryName.length(); i++) {
                categoryFullName.add(jsonArrayProductCategoryName.getJSONObject(i).getString("CategoryFullName"));
                categoryShortName.add(jsonArrayProductCategoryName.getJSONObject(i).getString("CategoryShortName"));
            }

            for (int i = 0; i < categoryFullName.size(); i++) {
                getItemsInDetail(categoryFullName.get(i));
            }

            for(int i=0;i<groupList.size();i++)
            {
                AstroShopItemDetails item = new AstroShopItemDetails(groupList.get(i).getPId());

                int index = allData.indexOf(item);
                String isDefault=groupList.get(i).getIsDefault();
                if(isDefault.equalsIgnoreCase("true"))

                {
                    defaultCheckId=i;
                }
                AstroShopItemDetails newItem=allData.get(i);
                newItem.setKdetail(kdetail);
                newItem.setIsDefault(isDefault);
                groupList.set(i,newItem);

            }
            setViewItem(groupList.get(defaultCheckId),false,defaultCheckId);

        /*    AstroShopItemDetails item = new AstroShopItemDetails(itemdetails.getPId());
            int i = allData.indexOf(item);
            if (i >= 0) {
                itemdetails = allData.get(i);
                itemdetails.setKdetail(kdetail);
                setViewItem(itemdetails,false);

            }*/
            //Log.i("Question Done******",i+"");

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private JSONArray getItemsInDetail(String title) {
        try {
            JSONArray jsonArray = new JSONArray(mainData);
            JSONObject innerObject;
            JSONArray arrayOfItems;
            for (int i = 1; i <= jsonArray.length(); i++) {
                innerObject = jsonArray.getJSONObject(i);
                Iterator<String> iter = innerObject.keys();
                while (iter.hasNext()) {
                    String key = iter.next();
                    if (key.equals(title)) {
                        arrayOfItems = innerObject.getJSONArray(title);
                        if (arrayOfItems != null) {
                            allData.addAll((ArrayList<AstroShopItemDetails>) new Gson().fromJson(arrayOfItems.toString(), new TypeToken<ArrayList<AstroShopItemDetails>>() {
                            }.getType()));
                            //Log.e("All length", "" + allData.size());
                            return arrayOfItems;
                        }
                    }
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void showHideDetailButton(boolean show) {
        if (show) {
            buy_now.setVisibility(View.GONE);
            add__cart.setVisibility(View.GONE);
        } else {
            buy_now.setVisibility(View.VISIBLE);
            add__cart.setVisibility(View.VISIBLE);
        }

    }


    private int isLocalKundliAvailable() {
        int screenId = 1;
        try {
            Map<String, String> mapHoroID = new ControllerManager().searchLocalKundliListOperation(
                    this.getApplicationContext(), "", CGlobalVariables.BOTH_GENDER, -1);
            if (mapHoroID != null) {
                screenId = 0;
            } else {
                screenId = 1;
            }

        } catch (UIDataOperationException e) {
            e.printStackTrace();
        }
        return screenId;
    }


    private void callToGetNewBirthDetails(int screenId, int reqCode) {
        Bundle bundle = new Bundle();
        Intent intent = new Intent(this, HomeInputScreen.class);
        intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, CGlobalVariables.MODULE_BASIC);
        intent.putExtra(CGlobalVariables.ASTRO_PRODUCT_DATA, true);
        intent.putExtra("PAGER_INDEX", screenId);
        intent.putExtras(bundle);
        startActivityForResult(intent, reqCode);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            switch (requestCode) {
                case REQUEST_KEY_SELECT_PROFILE:
                    // showHideDetailButton(false);
                    String profileData = data.getStringExtra("PROFILEDATA");
                    Log.i("PData", profileData);
                    itemdetails.setKdetail(profileData);
                    proceedSingleProduct();
                    //PROFILEDATA
                    break;
                case REQUEST_KEY_SELECT_PROFILE_CART:
                    //  showHideDetailButton(false);
                    String profileData1 = data.getStringExtra("PROFILEDATA");
                    Log.i("PData", profileData1);
                    itemdetails.setKdetail(profileData1);
                    proceedForCart();
                    break;
            }
        }
    }

    private void proceedForCart() {
        CUtils.addproductToCart(this, itemdetails);
        try {
            firebaseAnalytics(itemdetails,FirebaseAnalytics.Event.ADD_TO_CART);
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        Intent addtocart = new Intent(AstroShopItemDescription.this, AstroShopShopingCartAct.class);
        AstroShopItemDescription.this.startActivity(addtocart);
    }

    private void proceedSingleProduct() {
        Intent itemdescriptionIntent = new Intent(AstroShopItemDescription.this, ActAstroShopShippingDetails.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("key", itemdetails);
        bundle.putBoolean("fromCart", false);
        itemdescriptionIntent.putExtras(bundle);
        AstroShopItemDescription.this.startActivity(itemdescriptionIntent);
    }

    @Override
    public void getResult(String result) {
        if (result != null && result.length() > 0) {
            llOffer.setVisibility(View.VISIBLE);
            Log.e("mtTag", "getResult: "+result );
            tvOffer.setText(result);
            tvOffer.setTypeface(this.robotMediumTypeface);
        } else {
            llOffer.setVisibility(View.GONE);
        }
    }


    private void getFullDescription(final int index) {
        //  pd.show();
        // pd.setCancelable(false);
        text_Description.setVisibility(View.GONE);
        if(pBar!=null)
            pBar.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, CGlobalVariables.GET_FULL_DESCRIPTION,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        Log.e("without Convert String+", response.toString());
                        if (response != null && !(response.isEmpty())) {
                            try {
                                text_Description.setVisibility(View.VISIBLE);
                                String str = new String(response.getBytes("ISO-8859-1"), "UTF-8");
                                Log.e(" Convert String+", str);
                                JSONArray array=new JSONArray(str);
                                JSONObject obj=array.getJSONObject(0);
                                String result=obj.getString("result");
                                if(result.equalsIgnoreCase("1")) {
                                    //tvPlus.setVisibility(View.VISIBLE);
                                    String desc = obj.getString("PFullDesc");
                                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                                        CUtils.setClickableSpan(text_Description,
                                                Html.fromHtml(desc, Html.FROM_HTML_MODE_LEGACY), AstroShopItemDescription.this, regularTypeface);
                                        //text_Description.setText(Html.fromHtml(desc, Html.FROM_HTML_MODE_LEGACY));
                                        //makeTextViewResizable(text_Description, 2, "View More", true);

                                    } else {
                                        boolean contains1 = desc.contains("&#10;&#10;");
                                        boolean contains2 = desc.contains("<br />");
                                        String html = desc;
                                        if (desc.contains("&#10;&#10;")) {
                                            html = desc.replaceAll("&#10;&#10;", "<br />");

                                        }
                                        CUtils.setClickableSpan(text_Description,
                                                Html.fromHtml(desc), AstroShopItemDescription.this, regularTypeface);
                                    }

                                    if(tvMinus != null){
                                      /*  if(tvMinus.getVisibility() == View.VISIBLE)
                                            tvPlus.performClick();
                                        else if(tvPlus.getVisibility() == View.VISIBLE)
                                            tvMinus.performClick();*/
                                        tvMinus.performClick();
                                    }
                                }
                                else
                                {

                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                                MyCustomToast mct = new MyCustomToast(AstroShopItemDescription.this, AstroShopItemDescription.this.getLayoutInflater(), AstroShopItemDescription.this, typeface);
                                mct.show(getResources().getString(R.string.sign_up_validation_authentication_failed));
                                //finish();
                            }


                        }
                        /*if (pd.isShowing())
                            pd.dismiss();*/

                        if(pBar!=null)
                            pBar.setVisibility(View.GONE);
                    }

                }

                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MyCustomToast mct = new MyCustomToast(AstroShopItemDescription.this, AstroShopItemDescription.this.getLayoutInflater(), AstroShopItemDescription.this, typeface);
                mct.show(getResources().getString(R.string.sign_up_validation_authentication_failed));
                // finish();
                // //Log.e("Error Through" + error.getMessage());
                VolleyLog.d("Error: " + error.getMessage());
                //   mTextView.setText("That didn't work!");
                //   pd.dismiss();
                if(pBar!=null)
                    pBar.setVisibility(View.GONE);

            }


        }


        ) {
            @Override
            public String getBodyContentType() {
                return "application/x-www-form-urlencoded; charset=UTF-8";
            }


            /**
             * Passing some request headers
             * */
            @Override
            public Map<String, String> getParams() {
                /*
                 * key,name,emailid,address,city,landmark,state,country,pincode,mobileno*/
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("key", CUtils.getApplicationSignatureHashCode(AstroShopItemDescription.this));
                headers.put("prId", groupList.get(index).getPId());
                headers.put("langCode", "" + LANGUAGE_CODE);
                headers.put("prCatId",  groupList.get(index).getP_CatId());




               /* headers.put("prCatId", itemdetails.getP_CatId());
                headers.put("langcode", "" + LANGUAGE_CODE);
                headers.put("prParentId", "" + itemdetails.getP_ParentId());*/


                //Log.e("Product desc body", headers.toString());
                return headers;
            }
        };

// Add the request to the RequestQueue.
        //Log.e("API HIT HERE");

        int socketTimeout = 60000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        stringRequest.setShouldCache(false);
        queue.add(stringRequest);
    }



    void setDataofProduct() {
        try
        {
            largeImageList=groupList.get(0).getLargeImageList();
            String str = getResources().getString(R.string.select);
            title = LANGUAGE_CODE == CGlobalVariables.ENGLISH ? str.substring(0, 1).toUpperCase() + str.substring(1).toLowerCase() : str;
            filterText.setText( groupList.get(0).getFilterlabel() + ":");

            List<String> category = new ArrayList<>();
            if (groupList.size() > 1) {
                filterView.setVisibility(View.VISIBLE);
                radioGroup.setVisibility(View.VISIBLE);

            } else {
                filterText.setVisibility(View.GONE);
                radioGroup.setVisibility(View.GONE);
                getFullDescription(0);
            }
            //  list.size()>1?filterView.setVisibility(View.VISIBLE):filterText.setVisibility(View.GONE);
            RadioGroup.LayoutParams layoutParams = new RadioGroup.LayoutParams(
                    RadioGroup.LayoutParams.MATCH_PARENT,
                    RadioGroup.LayoutParams.WRAP_CONTENT);
            layoutParams.setMargins(0, 0, 0, 0);
            layoutParams.gravity = Gravity.CENTER;
            AstroShopItemDetails showItem = null;
            for (int i = 0; i < groupList.size(); i++) {
                AstroShopItemDetails item = groupList.get(i);
                if (item.getIsDefault().equalsIgnoreCase("true")) {
                    showItem = item;
                    defaultCheckId = i;
                }
                category.add(item.getFilterKey());
                RadioButton rdbtn = new RadioButton(this);
                rdbtn.setId(Integer.parseInt(item.getPId()));
                //  String filterkey=item.getFilterKey().length()<=1?"0"+item.getFilterKey():item.getFilterKey();
                rdbtn.setText(item.getFilterKey());
                if(i==groupList.size()-1)
                {
                    rdbtn.setBackgroundResource(R.drawable.radio_elector);



                }
                else
                {
                    rdbtn.setBackgroundResource(R.drawable.radio_elector_with_three_lines);

                }
                rdbtn.setButtonDrawable(android.R.color.transparent);
                //  rdbtn.setBackgroundResource(R.drawable.radio_elector);
                rdbtn.setTextSize(16);
                rdbtn.setTextColor(getResources().getColor(R.color.black));
                rdbtn.setPadding(30, 25, 30, 25);
                rdbtn.setGravity(Gravity.CENTER_VERTICAL);
                rdbtn.setLayoutParams(layoutParams);
                if(item.getP_OutOfStock().equalsIgnoreCase("true")) {
                    //rdbtn.setBackgroundResource(R.drawable.out_stock_selector);
                    rdbtn.setFocusable(false);
                    rdbtn.setEnabled(false);

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                        rdbtn.setAlpha(0.5f);
                    }

                }
                radioGroup.addView(rdbtn);

            }

            showItem = showItem == null ? groupList.get(0) : showItem;
            itemdetails=showItem;

            try {
                firebaseAnalytics(itemdetails,FirebaseAnalytics.Event.VIEW_ITEM);
            }catch (Exception e)
            {
                e.printStackTrace();
            }

            setViewItem(itemdetails, true,defaultCheckId);


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void firebaseAnalytics(AstroShopItemDetails shopItemDetails , String fcmEvent)
    {
        String[] nameArray = new String[2];
        if(shopItemDetails != null)
        {
            if(shopItemDetails.getPName() != null && shopItemDetails.getPName().length()>0)
            {
                if(shopItemDetails.getPName().contains("/")) {
                    nameArray = shopItemDetails.getPName().split("/");
                }else
                {
                    nameArray[0] = shopItemDetails.getPName();
                }

                if(nameArray[0] != null && nameArray.length>0)
                {
                    if(nameArray[0].contains(CGlobalVariables.LAB_CERTIFIED_REMOVE))
                    {
                        nameArray[0] = nameArray[0].replace(CGlobalVariables.LAB_CERTIFIED_REMOVE,"");
                    }else if(nameArray[0].contains(CGlobalVariables.LAB_CERTIFIED_BRAC_REMOVE))
                    {
                        nameArray[0] = nameArray[0].replace(CGlobalVariables.LAB_CERTIFIED_BRAC_REMOVE,"");
                    }

                    if(nameArray[0].contains("(") && nameArray[0].contains(")"))
                    {
                        nameArray[0] = nameArray[0].replace("(","");
                        nameArray[0] = nameArray[0].replace(")","");
                    }
                    //Log.e("FCMM ",nameArray[0]);
                    CUtils.fcmAnalyticsEvents(nameArray[0],fcmEvent, "");
                }
            }
        }
    }

    private void setdefaultProduct()
    {
        if(deepLinkItem!=null)
        {
            for(AstroShopItemDetails item:groupList)
            {
                if(item.getPId().equalsIgnoreCase(deepLinkItem.getPId()))
                {
                    item.setIsDefault("true");
                }
                else
                {
                    item.setIsDefault("false");
                }
            }

        }

    }

}

