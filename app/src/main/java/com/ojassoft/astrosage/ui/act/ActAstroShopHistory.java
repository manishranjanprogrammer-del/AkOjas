package com.ojassoft.astrosage.ui.act;

import android.os.Bundle;
import com.google.android.material.tabs.TabLayout;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
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
import com.google.gson.reflect.TypeToken;
import com.libojassoft.android.misc.VolleySingleton;
import com.ojassoft.astrosage.R;
import com.ojassoft.astrosage.beans.AstroShopItemDetails;
import com.ojassoft.astrosage.misc.AstroShopAdapterHistory;
import com.ojassoft.astrosage.misc.CustomProgressDialog;
import com.ojassoft.astrosage.ui.customcontrols.MyCustomToast;
import com.ojassoft.astrosage.utils.CGlobalVariables;
import com.ojassoft.astrosage.utils.CUtils;
import com.ojassoft.astrosage.utils.UserEmailFetcher;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.ojassoft.astrosage.utils.CGlobalVariables.KEY_EMAIL_ID;

/**
 * Created by ojas-20 on 18/11/16.
 */
public class ActAstroShopHistory extends BaseInputActivity {

    CustomProgressDialog pd = null;
    private RequestQueue queue;
    private int LANGUAGE_CODE = CGlobalVariables.ENGLISH;
    private NestedScrollView nScroll;

    //String mainData;
    RecyclerView recyclerViewOrderHistory;
    ArrayList<AstroShopItemDetails> astroShopItemDetailsArrayListHistory;
    TextView tvTitle, text_title, watermark;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_order_history);
        initXML();
        initValue();
        initToolBar();
        loadProductHistory();
    }

    private void initToolBar() {
        Toolbar tool_barAppModule = (Toolbar) findViewById(R.id.tool_barAppModule);
        setSupportActionBar(tool_barAppModule);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initXML() {
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        text_title = (TextView) findViewById(R.id.text_title);
        LANGUAGE_CODE = ((AstrosageKundliApplication) getApplication())
                .getLanguageCode();
        watermark = (TextView) findViewById(R.id.watermark);
        nScroll = (NestedScrollView) findViewById(R.id.nScroll);
        watermark.setTypeface(regularTypeface);
        // tvTitle.setText(getResources().getString(R.string.home_astro_shop));
        // tvTitle.setTypeface(typeface);
        tvTitle.setText(getResources().getString(R.string.your_orders_title_text));
        text_title.setTypeface(regularTypeface);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        recyclerViewOrderHistory = (RecyclerView) findViewById(R.id.my_recycler_view);
        tabLayout.setVisibility(View.GONE);
    }

    private void initValue() {
        queue = VolleySingleton.getInstance(this).getRequestQueue();

    }

    public ActAstroShopHistory() {
        super(R.string.app_name);
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

    /**
     * method to load productPurchase history
     */
    private void loadProductHistory() {
        pd = new CustomProgressDialog(ActAstroShopHistory.this, regularTypeface);
        pd.show();
        pd.setCancelable(false);
// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, CGlobalVariables.astroShopHistory,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response != null && !response.isEmpty()) {
                            try {
                                String str = new String(response.getBytes("ISO-8859-1"), "UTF-8");
                                response = str;
                                JSONArray jsonArray = new JSONArray(response);
                                JSONObject obj = jsonArray.getJSONObject(0);
                                String responseResult = "1";
                                if (obj.has("Result")) {
                                    responseResult = obj.getString("Result");
                                }
                                if (!responseResult.equals("")) {
                                    if (responseResult.equalsIgnoreCase("0")) {
                                        nScroll.setVisibility(View.GONE);
                                        watermark.setVisibility(View.VISIBLE);
                                    } else if (responseResult.equalsIgnoreCase("1")) {
                                        nScroll.setVisibility(View.VISIBLE);
                                        watermark.setVisibility(View.GONE);
                                        Gson gson = new Gson();
                                        JsonElement element = gson.fromJson(response.toString(), JsonElement.class);
                                        parseAstroShopData(response);
                                    } else if (responseResult.equalsIgnoreCase("2")) {
                                        nScroll.setVisibility(View.GONE);
                                        watermark.setVisibility(View.VISIBLE);
                                    } else if (responseResult.equalsIgnoreCase("3")) {
                                        nScroll.setVisibility(View.GONE);
                                        watermark.setVisibility(View.VISIBLE);

                                    } else if (responseResult.equalsIgnoreCase("4")) {
                                      /*  MyCustomToast mct2 = new MyCustomToast(ActAstroShopHistory.this, ActAstroShopHistory.this.getLayoutInflater(), ActAstroShopHistory.this, regularTypeface);
                                        mct2.show(getResources().getString(R.string.history_not_available));
                                        ActAstroShopHistory.this.finish();*/
                                        nScroll.setVisibility(View.GONE);
                                        watermark.setVisibility(View.VISIBLE);
                                    }


                                }
                            } catch (Exception e) {
                                e.printStackTrace();

                            }


                        }
                        pd.dismiss();
                    }

                }, new Response.ErrorListener()

        {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("tag", "Error Through" + error.getMessage());
                MyCustomToast mct = new MyCustomToast(ActAstroShopHistory.this, ActAstroShopHistory.this.getLayoutInflater(), ActAstroShopHistory.this, regularTypeface);
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
                    //TODO
                } else if (error instanceof ServerError) {
                    //TODO
                    VolleyLog.d("ServerError: " + error.getMessage());
                } else if (error instanceof NetworkError) {
                    //TODO
                    VolleyLog.d("NetworkError: " + error.getMessage());
                } else if (error instanceof ParseError) {
                    //TODO
                    VolleyLog.d("ParseError: " + error.getMessage());

                }
                pd.dismiss();
            }

        })

        {
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
                headers.put("Key", CUtils.getApplicationSignatureHashCode(ActAstroShopHistory.this));
                String emailId = UserEmailFetcher.getEmail(ActAstroShopHistory.this);
                if (emailId == null) {
                    emailId = "";
                }
                headers.put(KEY_EMAIL_ID, CUtils.replaceEmailChar(emailId));
                headers.put("langcode", "" + LANGUAGE_CODE);

                headers.put("deviceid", CUtils.getMyAndroidId(ActAstroShopHistory.this));
                // headers.put("deviceid","44080f4648a2cc2b" );
                //   headers.put("Key","9865");
                return headers;
            }

        };

// Add the request to the RequestQueue.
        Log.e("tag", "API HIT HERE");

        int socketTimeout = 60000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, DefaultRetryPolicy.DEFAULT_MAX_RETRIES, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        stringRequest.setShouldCache(true);
        queue.add(stringRequest);
    }

    private void parseAstroShopData(String response) {
        try {
            text_title.setVisibility(View.VISIBLE);
            astroShopItemDetailsArrayListHistory = new Gson().fromJson(response, new TypeToken<ArrayList<AstroShopItemDetails>>() {
            }.getType());
            astroShopItemDetailsArrayListHistory.remove(0);
      /*      JSONObject obj;
            AstroShopItemDetails astroShopItemDetails;
            for (int i = 1; i <= jsonArray.length() - 1; i++) {
                obj = jsonArray.getJSONObject(i);
                astroShopItemDetails = new AstroShopItemDetails();
                astroShopItemDetails.setPId(obj.getString("P_Id"));
                astroShopItemDetails.setPName(obj.getString("P_Name"));
                astroShopItemDetails.setO_Date(obj.getString("O_Date"));
                astroShopItemDetails.setPImgUrl(obj.getString("P_ImgUrl"));
                astroShopItemDetails.setP_Status(obj.getString("P_Status"));
                astroShopItemDetails.setD_Date(obj.getString("D_Date"));
                astroShopItemDetails.setP_OutOfStock(obj.getString("P_OutOfStock"));
                astroShopItemDetails.setP_OriginalPriceInDollar(obj.getString("P_OriginalPriceInDollar"));
                astroShopItemDetails.setP_OriginalPriceInRs(obj.getString("P_OriginalPriceInRs"));
                astroShopItemDetails.setPPriceInDoller(obj.getString("P_PriceInDoller"));
                astroShopItemDetails.setPPriceInRs(obj.getString("P_PriceInRs"));
                astroShopItemDetails.setP_SaveAmountInDollar(obj.getString("P_SaveAmountInDollar"));
                astroShopItemDetails.setP_SaveAmountInRs(obj.getString("P_SaveAmountInRs"));
                astroShopItemDetails.setP_SavePercentOfDollar(obj.getString("P_SavePercentOfDollar"));
                astroShopItemDetails.setP_SavePercentOfRs(obj.getString("P_SavePercentOfRs"));
                astroShopItemDetails.setP_CatId(obj.getString("P_CatId"));
                astroShopItemDetails.setPSmallDesc(obj.getString("P_SmallDesc"));
                astroShopItemDetails.setPFullDesc(obj.getString("P_FullDesc"));
                astroShopItemDetails.setPLargeImgUrl(obj.getString("P_LargeImgUrl"));
                astroShopItemDetails.setP_url_text(obj.getString("P_url_text"));
                astroShopItemDetails.setO_Id(obj.getString("O_Id"));

                astroShopItemDetailsArrayListHistory.add(astroShopItemDetails);
            }*/
            setOrderhistoryData();
        } catch (Exception e) {

        }
               /* // mainData="[{\"P_Id\": \"17\",\"P_Name\": \"Yellow Sapphire  Pukhraj (2 Carat) - Lab Certified\",\"P_OriginalPriceInDollar\": \"34.77612\",\"P_OriginalPriceInRs\": \"2000\",\"P_PriceInDoller\": \"42.77612\",\"P_PriceInRs\": \"1000\",\"P_SaveAmountInDollar\": \"0\",\"P_SavePercentOfDollar\": \"0\",\"P_SaveAmountInRs\": \"0\",\"P_SavePercentOfRs\": \"0\",\"P_CatId\": \"1\",\"P_OutOfStock\": \"False\",\"P_SmallDesc\": \"Yellow Sapphire (Pukhraj) is also known as Topaz. This astrological gem is ruled by planet Jupiter.\",\"P_FullDesc\": \"Ruling planet: Jupiter&#10;Zodiac sign: Sagittarius and Pieces&#10;Yellow Sapphire (Pukhraj) is also known as Topaz. This astrological gem is ruled by planet Jupiter. Jupiter is the planet of prosperity, abundance, education, happiness and success, also the benefactor of children and divinity. Wearing topaz bring wealth as well as success in marriage and good children. People born with Jupiter enhanced in their personal horoscope have in-depth knowledge of the ancient texts, scriptures and are firm believers in God. They make good teachers, scholars and are balanced in their approach. They are impartial and love to visit holy places. Yellow sapphire is also a unique and exquisite gem piece.Yellow sapphire protects the wearer from enemies and also gives the wearer beauty and splendor. Golden topaz also helps the wearer to attain health, wealth and happiness.&#10;&#10;<b>Note:</b> The image represents actual product though color of the image and product may slightly differ.\",\"P_LargeImgUrl\": \"http://www.astrocamp.com/images/product-images/yellow-sapphire.jpg\",\"P_ImgUrl\": \"http://www.astrocamp.com/images/product-images/thumbnails/yellow-sapphire.jpg\",\"P_url_text\": \"yellow-sapphire-2-ratti\",\"P_Status\": \"Delivered\",\"O_Id\": \"astro_0012shop\",\"O_Date\": \"12-may-2016\",\"D_Date\": \"20-may-2016\"}]";//D_Date
        try {
            astroShopItemDetailsArrayListHistory = new Gson().fromJson(response, new TypeToken<ArrayList<AstroShopItemDetails>>() {
            }.getType());
            Log.e("TAG", "parseAstroShopData: " + astroShopItemDetailsArrayListHistory.size());
            setupFragmentInViewPager();
        } catch (Exception e) {
            e.printStackTrace();
        }*/
    }

    private void setOrderhistoryData() {
        AstroShopAdapterHistory astroShopAdapter = new AstroShopAdapterHistory(ActAstroShopHistory.this, astroShopItemDetailsArrayListHistory);
        recyclerViewOrderHistory.setAdapter(astroShopAdapter);
        recyclerViewOrderHistory.setNestedScrollingEnabled(false);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(this);
        recyclerViewOrderHistory.setLayoutManager(mLayoutManager);

    }


}
