package com.ojassoft.astrosage.ui.act;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.appcompat.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.NetworkImageView;
//import com.google.analytics.tracking.android.EasyTracker;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.reflect.TypeToken;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.model.ServicelistModal;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;

/*import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;*/
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ojas on २२/७/१६.
 */
public class ActAsKqquestionDescription extends BaseInputActivity implements View.OnClickListener {
    private TextView tvTitle;
    private Toolbar tool_barAppModule;
    private NetworkImageView image_url;
    private TextView item_name;
    private TextView item_des, text_time;
    private TextView item_cost;
    private TabLayout tabLayout;
    private ImageView imgicmore, imgshopingcart;
    private Button buy_now, btn_check;
    private RequestQueue queue;
    private CheckBox checkitemavailable, checkdelivery;
    private EditText edt_pincode;
    private ServicelistModal itemdetails;
    Typeface typeface;
    private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    private String cacheResult;

    CustomProgressDialog pd = null;
    private String price_google = "";

    public ActAsKqquestionDescription() {
        super(R.string.app_name);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LANGUAGE_CODE = ((AstrosageKundliApplication) ActAsKqquestionDescription.this.getApplication())
                .getLanguageCode();

        typeface = CUtils.getRobotoFont(
                ActAsKqquestionDescription.this.getApplicationContext(), LANGUAGE_CODE, CGlobalVariables.regular);
        setContentView(R.layout.lay_astroshop_askaquestion_description);

        tool_barAppModule = (Toolbar) findViewById(R.id.tool_barAppModule);
        queue = VolleySingleton.getInstance(this).getRequestQueue();
        //    itemdetails = new AstroShopItemDetails();
        setSupportActionBar(tool_barAppModule);


        //     loadAstroShopServiceData();


        //  astroShopData.getItemCost();
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvTitle.setTypeface(typeface);
        tvTitle.setText(getResources().getString(R.string.askaquestion).replace("$", ""));
        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setVisibility(View.GONE);
        image_url = (NetworkImageView) findViewById(R.id.image_view);
        item_name = (TextView) findViewById(R.id.text_title);
        item_name.setTypeface(((BaseInputActivity) this).regularTypeface);
        //    item_name.setTypeface(typeface);

        text_time = (TextView) findViewById(R.id.text_time);
        text_time.setTypeface(((BaseInputActivity) this).robotMediumTypeface);
        text_time.setTypeface(typeface);

        item_des = (TextView) findViewById(R.id.text_sub_title);
        item_des.setTypeface(((BaseInputActivity) this).regularTypeface);
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

        if (!CUtils.isConnectedWithInternet(ActAsKqquestionDescription.this)) {
            MyCustomToast mct = new MyCustomToast(ActAsKqquestionDescription.this, ActAsKqquestionDescription.this
                    .getLayoutInflater(), ActAsKqquestionDescription.this, typeface);
            mct.show(getResources().getString(R.string.no_internet));
            ActAsKqquestionDescription.this.finish();
        } else {
            cacheResult = CUtils.getStringData(ActAsKqquestionDescription.this, "ASK" + String.valueOf(LANGUAGE_CODE), "");
            if (!cacheResult.equals("")) {
                parseGsonData(cacheResult);
            } else {
                // new GetData().execute();
            }

            //  loadAstroShopData();
        }


        imgicmore.setVisibility(View.GONE);
        imgshopingcart.setVisibility(View.GONE);
        imgshopingcart.setOnClickListener(this);
        imgicmore.setOnClickListener(this);
        buy_now.setOnClickListener(this);
        btn_check.setOnClickListener(this);

        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
                /*Intent itemdescriptionIntent = new Intent(ActAsKqquestionDescription.this, ActAstroPaymentOptions.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("key", itemdetails);
                itemdescriptionIntent.putExtras(bundle);
                ActAsKqquestionDescription.this.startActivityForResult(itemdescriptionIntent, 1);*/
                callToGetDetailsFromHomeInputModule();
                break;
            case R.id.btn_check:

                break;
            default:
                break;
        }
    }

    /**
     * @author : Amit RAutela
     * This method is used to call HomeInputScreen to get the details of user
     */
    private void callToGetDetailsFromHomeInputModule() {
        Bundle bundle = new Bundle();
        bundle.putSerializable("keyItemDetails", itemdetails);

        Intent intent = new Intent(this, HomeInputScreen.class);
        intent.putExtra(CGlobalVariables.MODULE_TYPE_KEY, CGlobalVariables.MODULE_BASIC);
        intent.putExtra(CGlobalVariables.ASK_QUESTION_QUERY_DATA, true);
        intent.putExtras(bundle);
        startActivity(intent);
    }

    /**
     * @author : Amit RAutela
     * This method is used to call ItemDesc Act
     */
   /* private void callToItemDesc(Intent intent){
        BeanHoroPersonalInfo beanHoroPersonalInfo = ((BeanHoroPersonalInfo)intent.getSerializableExtra("BeanHoroPersonalInfo"));
    private void callToItemDesc(Intent intent) {
        BeanHoroPersonalInfo beanHoroPersonalInfo = ((BeanHoroPersonalInfo) intent.getSerializableExtra("BeanHoroPersonalInfo"));
        Intent itemdescriptionIntent = new Intent(ActAsKqquestionDescription.this, ActAstroPaymentOptions.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("BeanHoroPersonalInfo", beanHoroPersonalInfo);
        bundle.putSerializable("key", itemdetails);
        itemdescriptionIntent.putExtras(bundle);
        startActivity(itemdescriptionIntent);
    }*/
    private void setViewItem() {
        String[] separated = null;
        if (itemdetails != null) {
            // tvTitle.setText(itemdetails.getTitle());
            item_name.setText(itemdetails.getTitle());
            item_des.setText(itemdetails.getSmallDesc());
            item_cost.setText(CUtils.getStringData(ActAsKqquestionDescription.this, CGlobalVariables.ASTROASKAQUESTIONPRICE, ""));
            //  item_cost.setText(getResources().getString(R.string.astroshop_dollar_sign) + roundFunction(Double.parseDouble(itemdetails.getPriceInDollor()), 2) + " / " + getResources().getString(R.string.astroshop_rupees_sign) + roundFunction(Double.parseDouble(itemdetails.getPriceInRS()), 2));
            //     //Log.e("LARGEIMAGE" + itemdetails.getPId() + itemdetails.getPImgUrl() + itemdetails.getPLargeImgUrl());
            image_url.setImageUrl(itemdetails.getSmallImgURL(), VolleySingleton.getInstance(ActAsKqquestionDescription.this).getImageLoader());
            // text_time.setText(getResources().getString(R.string.astro_service_deliverytime).replace("@", itemdetails.getDeliveryTime()));
        }
    }

    /*@Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //android.util.//Log.e("Result recieve in frag" + requestCode, "Done" + resultCode);

        if (requestCode == 1 && resultCode == 1) {
            callToItemDesc(data);
            // finish();
        }

    }*/


    private double roundFunction(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);
        return bd.doubleValue();
    }

    private void parseGsonData(String saveData) {

        List<ServicelistModal> data;
        Gson gson = new Gson();
        JsonElement element = gson.fromJson(saveData.toString(), JsonElement.class);
        data = gson.fromJson(saveData, new TypeToken<ArrayList<ServicelistModal>>() {
        }.getType());
        itemdetails = data.get(0);
        if (!cacheResult.equals("")) {
            setViewItem();
        }


    }


   /* private class GetData extends AsyncTask<Void, Void, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pd = new CustomProgressDialog(ActAsKqquestionDescription.this, typeface);
            pd.show();
            pd.setCancelable(false);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            InputStream inputStream = null;
            String result = "";
            InputStream is = null;

            try {


                HttpClient hc = CUtils.getNewHttpClient();
                HttpPost postMethod = new HttpPost(CGlobalVariables.astroShopServiceAskaQuestion);

                postMethod.setEntity(new UrlEncodedFormEntity(getNameValuePairs_Askquestion(
                        String.valueOf(LANGUAGE_CODE), CUtils.getApplicationSignatureHashCode(ActAsKqquestionDescription.this)), HTTP.UTF_8));
                HttpResponse httpResponse = null;

                httpResponse = hc.execute(postMethod);

                HttpEntity httpEntity = httpResponse.getEntity();

                is = httpEntity.getContent();

                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        is, "UTF-8"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) {
                    sb.append(line + "\n");
                }
                is.close();
                result = sb.toString();
                System.out.println("Responec=====" + result);
                if (result != null && !result.isEmpty()) {
                    CUtils.saveStringData(ActAsKqquestionDescription.this, "ASK"+String.valueOf(LANGUAGE_CODE), result);

                    parseGsonData(result);
                    return true;
                }


            } catch (Exception ex) {
                //android.util.//Log.i("Tag", "1 - " + ex.getMessage().toString());
            } finally {
                if (httpConn != null) {
                    httpConn.disconnect();
                }
            }


            return false;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            pd.dismiss();
            if (aBoolean) {
                setViewItem();
            }

        }


        private List<NameValuePair> getNameValuePairs_Askquestion(
                String languagecode, String key) {
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);


            nameValuePairs.add(new BasicNameValuePair("languageCode", languagecode));
            nameValuePairs.add(new BasicNameValuePair("Key", key));

             nameValuePairs.add(new BasicNameValuePair(KEY_AS_USER_ID, CUtils.replaceEmailChar(CUtils.getUserName(ActAsKqquestionDescription.this))));
             nameValuePairs.add(new BasicNameValuePair("asuserplanid", String.valueOf(CUtils.getUserPurchasedPlanFromPreference(ActAsKqquestionDescription.this))));

             //nameValuePairs.add(new BasicNameValuePair("asuserid", CGlobalVariables.STATIC_USER_IDD_FOR_TEST));
             //nameValuePairs.add(new BasicNameValuePair("asuserplanid", CGlobalVariables.STATIC_PLAN_IDD_FOR_TEST));

            return nameValuePairs;

        }


    }*/
}
